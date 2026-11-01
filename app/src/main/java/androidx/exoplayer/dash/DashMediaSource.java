package androidx.media3.exoplayer.dash;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.SparseArray;
import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaLibraryInfo;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.StreamKey;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.audio.SilenceSkippingAudioProcessor;
import androidx.media3.exoplayer.dash.manifest.AdaptationSet;
import androidx.media3.exoplayer.dash.manifest.DashManifest;
import androidx.media3.exoplayer.dash.manifest.DashManifestParser;
import androidx.media3.exoplayer.dash.manifest.Period;
import androidx.media3.exoplayer.dash.manifest.Representation;
import androidx.media3.exoplayer.dash.manifest.UtcTimingElement;
import androidx.media3.exoplayer.drm.DefaultDrmSessionManagerProvider;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import androidx.media3.exoplayer.drm.DrmSessionManager;
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider;
import androidx.media3.exoplayer.offline.FilteringManifestParser;
import androidx.media3.exoplayer.source.BaseMediaSource;
import androidx.media3.exoplayer.source.CompositeSequenceableLoaderFactory;
import androidx.media3.exoplayer.source.DefaultCompositeSequenceableLoaderFactory;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MediaLoadData;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.MediaSourceEventListener;
import androidx.media3.exoplayer.source.MediaSourceFactory;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.DefaultLoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.Loader;
import androidx.media3.exoplayer.upstream.LoaderErrorThrower;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import androidx.media3.exoplayer.util.SntpClient;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.math.LongMath;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class DashMediaSource extends BaseMediaSource {
    public static final long DEFAULT_FALLBACK_TARGET_LIVE_OFFSET_MS = 30000;
    public static final String DEFAULT_MEDIA_ID = "DashMediaSource";
    private static final long DEFAULT_NOTIFY_MANIFEST_INTERVAL_MS = 5000;
    public static final long MIN_LIVE_DEFAULT_START_POSITION_US = 5000000;
    private static final String TAG = "DashMediaSource";
    private final BaseUrlExclusionList baseUrlExclusionList;
    private final DashChunkSource.Factory chunkSourceFactory;
    private final CmcdConfiguration cmcdConfiguration;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private DataSource dataSource;
    private final DrmSessionManager drmSessionManager;
    private long elapsedRealtimeOffsetMs;
    private long expiredManifestPublishTimeUs;
    private final long fallbackTargetLiveOffsetMs;
    private int firstPeriodId;
    private Handler handler;
    private Uri initialManifestUri;
    private MediaItem.LiveConfiguration liveConfiguration;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private Loader loader;
    private DashManifest manifest;
    private final ManifestCallback manifestCallback;
    private final DataSource.Factory manifestDataSourceFactory;
    private final MediaSourceEventListener.EventDispatcher manifestEventDispatcher;
    private IOException manifestFatalError;
    private long manifestLoadEndTimestampMs;
    private final LoaderErrorThrower manifestLoadErrorThrower;
    private boolean manifestLoadPending;
    private long manifestLoadStartTimestampMs;
    private final ParsingLoadable.Parser<? extends DashManifest> manifestParser;
    private Uri manifestUri;
    private final Object manifestUriLock;
    private MediaItem mediaItem;
    private TransferListener mediaTransferListener;
    private final long minLiveStartPositionUs;
    private final SparseArray<DashMediaPeriod> periodsById;
    private final PlayerEmsgHandler.PlayerEmsgCallback playerEmsgCallback;
    private final Runnable refreshManifestRunnable;
    private final boolean sideloadedManifest;
    private final Runnable simulateManifestRefreshRunnable;
    private int staleManifestReloadAttempt;

    private static final class DashTimeline extends Timeline {
        private final long elapsedRealtimeEpochOffsetMs;
        private final int firstPeriodId;
        private final MediaItem.LiveConfiguration liveConfiguration;
        private final DashManifest manifest;
        private final MediaItem mediaItem;
        private final long offsetInFirstPeriodUs;
        private final long presentationStartTimeMs;
        private final long windowDefaultStartPositionUs;
        private final long windowDurationUs;
        private final long windowStartTimeMs;

        public DashTimeline(long r17, long r19, long r21, int r23, long r24, long r26, long r28, DashManifest r30, MediaItem r31, MediaItem.LiveConfiguration r32) {
            boolean r3 = r30.dynamic;
            boolean r4 = true;
            if (r32 == null) goto L5;
            boolean r6 = true;
        L6:
            if (r3 == r6) goto L9;
            r4 = false;
        L9:
            Assertions.checkState(r4);
            this.presentationStartTimeMs = r17;
            this.windowStartTimeMs = r19;
            this.elapsedRealtimeEpochOffsetMs = r21;
            this.firstPeriodId = r23;
            this.offsetInFirstPeriodUs = r24;
            this.windowDurationUs = r26;
            this.windowDefaultStartPositionUs = r28;
            this.manifest = r30;
            this.mediaItem = r31;
            this.liveConfiguration = r32;
            return;
        L5:
            r6 = false;
            goto L6
        }

        private long getAdjustedWindowDefaultStartPositionUs(long r15) {
            long r0 = this.windowDefaultStartPositionUs;
            if (isMovingLiveWindow(this.manifest) == true) goto L6;
            return r0;
        L6:
            if (r15 <= 0) goto L11;
            r0 = r0 + r15;
            if (r0 <= this.windowDurationUs) goto L11;
            return C0180C.TIME_UNSET;
        L11:
            int r4 = 0;
            long r5 = this.offsetInFirstPeriodUs + r0;
            long r7 = this.manifest.getPeriodDurationUs(0);
        L13:
            if (r4 >= (this.manifest.getPeriodCount() - 1)) goto L17;
            if (r5 < r7) goto L17;
            r5 = r5 - r7;
            r4 = r4 + 1;
            r7 = this.manifest.getPeriodDurationUs(r4);
        L17:
            Period r9 = this.manifest.getPeriod(r4);
            int r10 = r9.getAdaptationSetIndex(2);
            if (r10 != (-1)) goto L20;
            return r0;
        L20:
            DashSegmentIndex r11 = r9.adaptationSets.get(r10).representations.get(0).getIndex();
            if (r11 != null) goto L23;
        L27:
            return r0;
        L23:
            if (r11.getSegmentCount(r7) == 0) goto L27;
            return (r11.getTimeUs(r11.getSegmentNum(r5, r7)) + r0) - r5;
        }

        private static boolean isMovingLiveWindow(DashManifest r4) {
            if (r4.dynamic == true) goto L5;
        L9:
            return false;
        L5:
            if (r4.minUpdatePeriodMs == C0180C.TIME_UNSET) goto L9;
            if (r4.durationMs != C0180C.TIME_UNSET) goto L9;
            return true;
        }

        @Override // androidx.media3.common.Timeline
        public int getIndexOfPeriod(Object r5) {
            if ((r5 instanceof Integer) == true) goto L5;
            return -1;
        L5:
            int r2 = ((Integer) r5).intValue() - this.firstPeriodId;
            if (r2 >= 0) goto L8;
            return -1;
        L8:
            if (r2 < getPeriodCount()) goto L11;
            return -1;
        L11:
            return r2;
        }

        @Override // androidx.media3.common.Timeline
        public Timeline.Period getPeriod(int r12, Timeline.Period r13, boolean r14) {
            Assertions.checkIndex(r12, 0, getPeriodCount());
            Integer r0 = null;
            if (r14 == false) goto L5;
            String r2 = this.manifest.getPeriod(r12).f40id;
        L6:
            String r4 = r2;
            if (r14 == false) goto L10;
            r0 = Integer.valueOf(this.firstPeriodId + r12);
        L10:
            return r13.set(r4, r0, 0, this.manifest.getPeriodDurationUs(r12), Util.msToUs(this.manifest.getPeriod(r12).startMs - this.manifest.getPeriod(0).startMs) - this.offsetInFirstPeriodUs);
        L5:
            r2 = null;
            goto L6
        }

        @Override // androidx.media3.common.Timeline
        public int getPeriodCount() {
            return this.manifest.getPeriodCount();
        }

        @Override // androidx.media3.common.Timeline
        public Object getUidOfPeriod(int r3) {
            Assertions.checkIndex(r3, 0, getPeriodCount());
            return Integer.valueOf(this.firstPeriodId + r3);
        }

        @Override // androidx.media3.common.Timeline
        public Timeline.Window getWindow(int r28, Timeline.Window r29, long r30) {
            Assertions.checkIndex(r28, 0, 1);
            return r29.set(Timeline.Window.SINGLE_WINDOW_UID, this.mediaItem, this.manifest, this.presentationStartTimeMs, this.windowStartTimeMs, this.elapsedRealtimeEpochOffsetMs, true, isMovingLiveWindow(this.manifest), this.liveConfiguration, getAdjustedWindowDefaultStartPositionUs(r30), this.windowDurationUs, 0, getPeriodCount() - 1, this.offsetInFirstPeriodUs);
        }

        @Override // androidx.media3.common.Timeline
        public int getWindowCount() {
            return 1;
        }
    }

    private final class DefaultPlayerEmsgCallback implements PlayerEmsgHandler.PlayerEmsgCallback {
        final /* synthetic */ DashMediaSource this$0;

        private DefaultPlayerEmsgCallback(DashMediaSource r1) {
            this.this$0 = r1;
        }

        /* synthetic */ DefaultPlayerEmsgCallback(DashMediaSource r1, C02741 r2) {
            this(r1);
        }

        @Override // androidx.media3.exoplayer.dash.PlayerEmsgHandler.PlayerEmsgCallback
        public void onDashManifestPublishTimeExpired(long r2) {
            this.this$0.onDashManifestPublishTimeExpired(r2);
        }

        @Override // androidx.media3.exoplayer.dash.PlayerEmsgHandler.PlayerEmsgCallback
        public void onDashManifestRefreshRequested() {
            this.this$0.onDashManifestRefreshRequested();
        }
    }

    public static final class Factory implements MediaSourceFactory {
        private final DashChunkSource.Factory chunkSourceFactory;
        private CmcdConfiguration.Factory cmcdConfigurationFactory;
        private CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
        private DrmSessionManagerProvider drmSessionManagerProvider;
        private long fallbackTargetLiveOffsetMs;
        private LoadErrorHandlingPolicy loadErrorHandlingPolicy;
        private final DataSource.Factory manifestDataSourceFactory;
        private ParsingLoadable.Parser<? extends DashManifest> manifestParser;
        private long minLiveStartPositionUs;

        public Factory(DataSource.Factory r2) {
            this(new DefaultDashChunkSource.Factory(r2), r2);
        }

        public Factory(DashChunkSource.Factory r3, DataSource.Factory r4) {
            this.chunkSourceFactory = (DashChunkSource.Factory) Assertions.checkNotNull(r3);
            this.manifestDataSourceFactory = r4;
            this.drmSessionManagerProvider = new DefaultDrmSessionManagerProvider();
            this.loadErrorHandlingPolicy = new DefaultLoadErrorHandlingPolicy();
            this.fallbackTargetLiveOffsetMs = 30000;
            this.minLiveStartPositionUs = DashMediaSource.MIN_LIVE_DEFAULT_START_POSITION_US;
            this.compositeSequenceableLoaderFactory = new DefaultCompositeSequenceableLoaderFactory();
            experimentalParseSubtitlesDuringExtraction(true);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public DashMediaSource createMediaSource(MediaItem r18) {
            Assertions.checkNotNull(r18.localConfiguration);
            ParsingLoadable.Parser<? extends DashManifest> r1 = this.manifestParser;
            if (r1 != null) goto L5;
            r1 = new DashManifestParser();
        L5:
            List<StreamKey> r3 = r18.localConfiguration.streamKeys;
            if (r3.isEmpty() == true) goto L8;
            ParsingLoadable.Parser<? extends DashManifest> r5 = new FilteringManifestParser(r1, r3);
        L10:
            if (this.cmcdConfigurationFactory != null) goto L12;
            CmcdConfiguration r8 = null;
        L14:
            return new DashMediaSource(r18, null, this.manifestDataSourceFactory, r5, this.chunkSourceFactory, this.compositeSequenceableLoaderFactory, r8, this.drmSessionManagerProvider.get(r18), this.loadErrorHandlingPolicy, this.fallbackTargetLiveOffsetMs, this.minLiveStartPositionUs, null);
        L12:
            r8 = this.cmcdConfigurationFactory.createCmcdConfiguration(r18);
            goto L14
        L8:
            r5 = r1;
            goto L10
        }

        public DashMediaSource createMediaSource(DashManifest r3) {
            return createMediaSource(r3, new MediaItem.Builder().setUri(Uri.EMPTY).setMediaId("DashMediaSource").setMimeType(MimeTypes.APPLICATION_MPD).build());
        }

        public DashMediaSource createMediaSource(DashManifest r18, MediaItem r19) {
            Assertions.checkArgument(!r18.dynamic);
            MediaItem.Builder r1 = r19.buildUpon().setMimeType(MimeTypes.APPLICATION_MPD);
            if (r19.localConfiguration != null) goto L5;
            r1.setUri(Uri.EMPTY);
        L5:
            MediaItem r2 = r1.build();
            if (this.cmcdConfigurationFactory != null) goto L8;
            CmcdConfiguration r8 = null;
        L10:
            return new DashMediaSource(r2, r18, null, null, this.chunkSourceFactory, this.compositeSequenceableLoaderFactory, r8, this.drmSessionManagerProvider.get(r2), this.loadErrorHandlingPolicy, this.fallbackTargetLiveOffsetMs, this.minLiveStartPositionUs, null);
        L8:
            r8 = this.cmcdConfigurationFactory.createCmcdConfiguration(r2);
            goto L10
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource createMediaSource(MediaItem r1) {
            return createMediaSource(r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        @Deprecated
        public Factory experimentalParseSubtitlesDuringExtraction(boolean r2) {
            this.chunkSourceFactory.experimentalParseSubtitlesDuringExtraction(r2);
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        @Deprecated
        public /* bridge */ /* synthetic */ MediaSource.Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
            return experimentalParseSubtitlesDuringExtraction(r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public int[] getSupportedTypes() {
            return new int[]{0};
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public Factory setCmcdConfigurationFactory(CmcdConfiguration.Factory r2) {
            this.cmcdConfigurationFactory = (CmcdConfiguration.Factory) Assertions.checkNotNull(r2);
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource.Factory setCmcdConfigurationFactory(CmcdConfiguration.Factory r1) {
            return setCmcdConfigurationFactory(r1);
        }

        public Factory setCompositeSequenceableLoaderFactory(CompositeSequenceableLoaderFactory r2) {
            this.compositeSequenceableLoaderFactory = (CompositeSequenceableLoaderFactory) Assertions.checkNotNull(r2, "DashMediaSource.Factory#setCompositeSequenceableLoaderFactory no longer handles null by instantiating a new DefaultCompositeSequenceableLoaderFactory. Explicitly construct and pass an instance in order to retain the old behavior.");
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public Factory setDrmSessionManagerProvider(DrmSessionManagerProvider r2) {
            this.drmSessionManagerProvider = (DrmSessionManagerProvider) Assertions.checkNotNull(r2, "MediaSource.Factory#setDrmSessionManagerProvider no longer handles null by instantiating a new DefaultDrmSessionManagerProvider. Explicitly construct and pass an instance in order to retain the old behavior.");
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource.Factory setDrmSessionManagerProvider(DrmSessionManagerProvider r1) {
            return setDrmSessionManagerProvider(r1);
        }

        public Factory setFallbackTargetLiveOffsetMs(long r1) {
            this.fallbackTargetLiveOffsetMs = r1;
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public Factory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r2) {
            this.loadErrorHandlingPolicy = (LoadErrorHandlingPolicy) Assertions.checkNotNull(r2, "MediaSource.Factory#setLoadErrorHandlingPolicy no longer handles null by instantiating a new DefaultLoadErrorHandlingPolicy. Explicitly construct and pass an instance in order to retain the old behavior.");
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource.Factory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r1) {
            return setLoadErrorHandlingPolicy(r1);
        }

        public Factory setManifestParser(ParsingLoadable.Parser<? extends DashManifest> r1) {
            this.manifestParser = r1;
            return this;
        }

        public Factory setMinLiveStartPositionUs(long r1) {
            this.minLiveStartPositionUs = r1;
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public Factory setSubtitleParserFactory(SubtitleParser.Factory r3) {
            this.chunkSourceFactory.setSubtitleParserFactory((SubtitleParser.Factory) Assertions.checkNotNull(r3));
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource.Factory setSubtitleParserFactory(SubtitleParser.Factory r1) {
            return setSubtitleParserFactory(r1);
        }
    }

    static final class Iso8601Parser implements ParsingLoadable.Parser<Long> {
        private static final Pattern TIMESTAMP_WITH_TIMEZONE_PATTERN = null;

        static {
            TIMESTAMP_WITH_TIMEZONE_PATTERN = Pattern.compile("(.+?)(Z|((\\+|-|−)(\\d\\d)(:?(\\d\\d))?))");
        }

        Iso8601Parser() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
        public Long parse(Uri r21, InputStream r22) throws IOException {
            String r1 = new BufferedReader(new InputStreamReader(r22, StandardCharsets.UTF_8)).readLine();
            Matcher r0 = TIMESTAMP_WITH_TIMEZONE_PATTERN.matcher(r1);     // Catch: ParseException -> L21
            if (r0.matches() == false) goto L20;
            String r4 = r0.group(1);     // Catch: ParseException -> L21
            SimpleDateFormat r5 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);     // Catch: ParseException -> L21
            r5.setTimeZone(TimeZone.getTimeZone("UTC"));     // Catch: ParseException -> L21
            long r6 = r5.parse(r4).getTime();     // Catch: ParseException -> L21
            if ("Z".equals(r0.group(2)) == false) goto L9;
        L18:
            return Long.valueOf(r6);
        L9:
            if ("+".equals(r0.group(4)) == false) goto L11;
            long r9 = 1;
        L12:
            long r11 = Long.parseLong(r0.group(5));     // Catch: ParseException -> L21
            String r13 = r0.group(7);     // Catch: ParseException -> L21
            if (TextUtils.isEmpty(r13) == false) goto L15;
            long r14 = 0;
        L16:
            r6 = r6 - (((((r11 * 60) + r14) * 60) * 1000) * r9);     // Catch: ParseException -> L21
            goto L18
        L15:
            r14 = Long.parseLong(r13);     // Catch: ParseException -> L21
            goto L16
        L11:
            r9 = -1;
            goto L12
        L20:
            throw ParserException.createForMalformedManifest("Couldn't parse timestamp: " + r1, null);     // Catch: ParseException -> L21
        L21:
            e = move-exception;
            throw ParserException.createForMalformedManifest(null, e);
        }

        @Override // androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
        public /* bridge */ /* synthetic */ Long parse(Uri r1, InputStream r2) throws IOException {
            return parse(r1, r2);
        }
    }

    private final class ManifestCallback implements Loader.Callback<ParsingLoadable<DashManifest>> {
        final /* synthetic */ DashMediaSource this$0;

        private ManifestCallback(DashMediaSource r1) {
            this.this$0 = r1;
        }

        /* synthetic */ ManifestCallback(DashMediaSource r1, C02741 r2) {
            this(r1);
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public /* bridge */ /* synthetic */ void onLoadCanceled(Loader.Loadable r8, long r9, long r11, boolean r13) {
            onLoadCanceled((ParsingLoadable) r8, r9, r11, r13);
        }

        public void onLoadCanceled(ParsingLoadable<DashManifest> r7, long r8, long r10, boolean r12) {
            this.this$0.onLoadCanceled(r7, r8, r10);
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public /* bridge */ /* synthetic */ void onLoadCompleted(Loader.Loadable r7, long r8, long r10) {
            onLoadCompleted((ParsingLoadable) r7, r8, r10);
        }

        public void onLoadCompleted(ParsingLoadable<DashManifest> r7, long r8, long r10) {
            this.this$0.onManifestLoadCompleted(r7, r8, r10);
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public /* bridge */ /* synthetic */ Loader.LoadErrorAction onLoadError(Loader.Loadable r9, long r10, long r12, IOException r14, int r15) {
            return onLoadError((ParsingLoadable) r9, r10, r12, r14, r15);
        }

        public Loader.LoadErrorAction onLoadError(ParsingLoadable<DashManifest> r9, long r10, long r12, IOException r14, int r15) {
            return this.this$0.onManifestLoadError(r9, r10, r12, r14, r15);
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public /* synthetic */ void onLoadStarted(Loader.Loadable r1, long r2, long r4, int r6) {
            Loader.Callback.CC.$default$onLoadStarted(this, r1, r2, r4, r6);
        }
    }

    final class ManifestLoadErrorThrower implements LoaderErrorThrower {
        final /* synthetic */ DashMediaSource this$0;

        ManifestLoadErrorThrower(DashMediaSource r1) {
            this.this$0 = r1;
        }

        private void maybeThrowManifestError() throws IOException {
            if (DashMediaSource.access$800(this.this$0) != null) goto L6;
            return;
        L6:
            throw DashMediaSource.access$800(this.this$0);
        }

        @Override // androidx.media3.exoplayer.upstream.LoaderErrorThrower
        public void maybeThrowError() throws IOException {
            DashMediaSource.access$700(this.this$0).maybeThrowError();
            maybeThrowManifestError();
        }

        @Override // androidx.media3.exoplayer.upstream.LoaderErrorThrower
        public void maybeThrowError(int r2) throws IOException {
            DashMediaSource.access$700(this.this$0).maybeThrowError(r2);
            maybeThrowManifestError();
        }
    }

    private final class UtcTimestampCallback implements Loader.Callback<ParsingLoadable<Long>> {
        final /* synthetic */ DashMediaSource this$0;

        private UtcTimestampCallback(DashMediaSource r1) {
            this.this$0 = r1;
        }

        /* synthetic */ UtcTimestampCallback(DashMediaSource r1, C02741 r2) {
            this(r1);
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public /* bridge */ /* synthetic */ void onLoadCanceled(Loader.Loadable r8, long r9, long r11, boolean r13) {
            onLoadCanceled((ParsingLoadable) r8, r9, r11, r13);
        }

        public void onLoadCanceled(ParsingLoadable<Long> r7, long r8, long r10, boolean r12) {
            this.this$0.onLoadCanceled(r7, r8, r10);
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public /* bridge */ /* synthetic */ void onLoadCompleted(Loader.Loadable r7, long r8, long r10) {
            onLoadCompleted((ParsingLoadable) r7, r8, r10);
        }

        public void onLoadCompleted(ParsingLoadable<Long> r7, long r8, long r10) {
            this.this$0.onUtcTimestampLoadCompleted(r7, r8, r10);
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public /* bridge */ /* synthetic */ Loader.LoadErrorAction onLoadError(Loader.Loadable r9, long r10, long r12, IOException r14, int r15) {
            return onLoadError((ParsingLoadable) r9, r10, r12, r14, r15);
        }

        public Loader.LoadErrorAction onLoadError(ParsingLoadable<Long> r8, long r9, long r11, IOException r13, int r14) {
            return this.this$0.onUtcTimestampLoadError(r8, r9, r11, r13);
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public /* synthetic */ void onLoadStarted(Loader.Loadable r1, long r2, long r4, int r6) {
            Loader.Callback.CC.$default$onLoadStarted(this, r1, r2, r4, r6);
        }
    }

    private static final class XsDateTimeParser implements ParsingLoadable.Parser<Long> {
        private XsDateTimeParser() {
        }

        /* synthetic */ XsDateTimeParser(C02741 r1) {
            this();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
        public Long parse(Uri r4, InputStream r5) throws IOException {
            return Long.valueOf(Util.parseXsDateTime(new BufferedReader(new InputStreamReader(r5)).readLine()));
        }

        @Override // androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
        public /* bridge */ /* synthetic */ Long parse(Uri r1, InputStream r2) throws IOException {
            return parse(r1, r2);
        }
    }

    public static /* synthetic */ void $r8$lambda$iFt22VLWMPRWv6j8zcIpQ90WqWU(DashMediaSource r0) {
        r0.startLoadingManifest();
    }

    static {
        MediaLibraryInfo.registerModule("media3.exoplayer.dash");
    }

    private DashMediaSource(MediaItem r18, DashManifest r19, DataSource.Factory r20, ParsingLoadable.Parser<? extends DashManifest> r21, DashChunkSource.Factory r22, CompositeSequenceableLoaderFactory r23, CmcdConfiguration r24, DrmSessionManager r25, LoadErrorHandlingPolicy r26, long r27, long r29) {
        this.mediaItem = r18;
        this.liveConfiguration = r18.liveConfiguration;
        this.manifestUri = ((MediaItem.LocalConfiguration) Assertions.checkNotNull(r18.localConfiguration)).uri;
        this.initialManifestUri = r18.localConfiguration.uri;
        this.manifest = r19;
        this.manifestDataSourceFactory = r20;
        this.manifestParser = r21;
        this.chunkSourceFactory = r22;
        this.cmcdConfiguration = r24;
        this.drmSessionManager = r25;
        this.loadErrorHandlingPolicy = r26;
        this.fallbackTargetLiveOffsetMs = r27;
        this.minLiveStartPositionUs = r29;
        this.compositeSequenceableLoaderFactory = r23;
        this.baseUrlExclusionList = new BaseUrlExclusionList();
        if (r19 == null) goto L5;
        boolean r15 = true;
    L6:
        this.sideloadedManifest = r15;
        C02741 r16 = null;
        this.manifestEventDispatcher = createEventDispatcher(null);
        this.manifestUriLock = new Object();
        this.periodsById = new SparseArray();
        this.playerEmsgCallback = new DefaultPlayerEmsgCallback(this, r16);
        this.expiredManifestPublishTimeUs = C0180C.TIME_UNSET;
        this.elapsedRealtimeOffsetMs = C0180C.TIME_UNSET;
        if (this.sideloadedManifest == false) goto L9;
        Assertions.checkState(!r19.dynamic);
        this.manifestCallback = null;
        this.refreshManifestRunnable = null;
        this.simulateManifestRefreshRunnable = null;
        this.manifestLoadErrorThrower = new LoaderErrorThrower.Placeholder();
        return;
    L9:
        this.manifestCallback = new ManifestCallback(this, r16);
        this.manifestLoadErrorThrower = new ManifestLoadErrorThrower(this);
        this.refreshManifestRunnable = new DashMediaSource$$ExternalSyntheticLambda0(this);
        this.simulateManifestRefreshRunnable = new DashMediaSource$$ExternalSyntheticLambda1(this);
        return;
    L5:
        r15 = false;
        goto L6
    }

    /* synthetic */ DashMediaSource(MediaItem r1, DashManifest r2, DataSource.Factory r3, ParsingLoadable.Parser r4, DashChunkSource.Factory r5, CompositeSequenceableLoaderFactory r6, CmcdConfiguration r7, DrmSessionManager r8, LoadErrorHandlingPolicy r9, long r10, long r12, C02741 r14) {
        this(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r12);
    }

    static /* synthetic */ void access$500(DashMediaSource r0, long r1) {
        r0.onUtcTimestampResolved(r1);
    }

    static /* synthetic */ void access$600(DashMediaSource r0, IOException r1) {
        r0.onUtcTimestampResolutionError(r1);
    }

    static /* synthetic */ Loader access$700(DashMediaSource r1) {
        return r1.loader;
    }

    static /* synthetic */ IOException access$800(DashMediaSource r1) {
        return r1.manifestFatalError;
    }

    private static long getAvailableEndTimeInManifestUs(Period r24, long r25, long r27) {
        Period r0 = r24;
        long r5 = Util.msToUs(r0.startMs);
        long r7 = Long.MAX_VALUE;
        boolean r9 = hasVideoOrAudioAdaptationSets(r0);
        int r10 = 0;
    L4:
        if (r10 >= r0.adaptationSets.size()) goto L25;
        AdaptationSet r11 = r0.adaptationSets.get(r10);
        List<Representation> r12 = r11.representations;
        if (r11.type != 1) goto L8;
    L10:
        boolean r15 = false;
    L11:
        if (r9 == false) goto L14;
        if (r15 == false) goto L14;
    L24:
        r10 = r10 + 1;
        r0 = r24;
    L14:
        if (r12.isEmpty() == true) goto L24;
        DashSegmentIndex r13 = r12.get(0).getIndex();
        if (r13 == null) goto L19;
        long r16 = r13.getAvailableSegmentCount(r25, r27);
        if (r16 == 0) goto L22;
        long r3 = (r13.getFirstAvailableSegmentNum(r25, r27) + r16) - 1;
        r7 = Math.min(r7, (r5 + r13.getTimeUs(r3)) + r13.getDurationUs(r3, r25));
        goto L24
    L22:
        return r5;
    L19:
        return r5 + r25;
    L8:
        if (r11.type == 2) goto L10;
        r15 = true;
        goto L11
    L25:
        return r7;
    }

    private static long getAvailableStartTimeInManifestUs(Period r22, long r23, long r25) {
        Period r0 = r22;
        long r5 = Util.msToUs(r0.startMs);
        long r7 = r5;
        boolean r9 = hasVideoOrAudioAdaptationSets(r0);
        int r10 = 0;
    L4:
        if (r10 >= r0.adaptationSets.size()) goto L24;
        AdaptationSet r11 = r0.adaptationSets.get(r10);
        List<Representation> r12 = r11.representations;
        if (r11.type != 1) goto L8;
    L10:
        boolean r15 = false;
    L11:
        if (r9 == false) goto L14;
        if (r15 == false) goto L14;
    L15:
        long r18 = r5;
    L23:
        r10 = r10 + 1;
        r0 = r22;
        r5 = r18;
    L14:
        if (r12.isEmpty() == true) goto L15;
        DashSegmentIndex r13 = r12.get(0).getIndex();
        if (r13 == null) goto L18;
        if (r13.getAvailableSegmentCount(r23, r25) == 0) goto L21;
        r18 = r5;
        r7 = Math.max(r7, r18 + r13.getTimeUs(r13.getFirstAvailableSegmentNum(r23, r25)));
        goto L23
    L21:
        return r5;
    L18:
        return r5;
    L8:
        if (r11.type == 2) goto L10;
        r15 = true;
        goto L11
    L24:
        return r7;
    }

    private static long getIntervalUntilNextManifestRefreshMs(DashManifest r24, long r25) {
        int r1 = r24.getPeriodCount() - 1;
        Period r2 = r24.getPeriod(r1);
        long r3 = Util.msToUs(r2.startMs);
        long r5 = r24.getPeriodDurationUs(r1);
        long r7 = Util.msToUs(r25);
        long r9 = Util.msToUs(r24.availabilityStartTimeMs);
        long r11 = Util.msToUs(5000);
        int r13 = 0;
    L4:
        if (r13 >= r2.adaptationSets.size()) goto L19;
        List<Representation> r14 = r2.adaptationSets.get(r13).representations;
        if (r14.isEmpty() == true) goto L17;
        DashSegmentIndex r15 = r14.get(0).getIndex();
        if (r15 == null) goto L17;
        long r18 = ((r9 + r3) + r15.getNextSegmentAvailableTimeUs(r5, r7)) - r7;
        if (r18 >= (r11 - SilenceSkippingAudioProcessor.DEFAULT_MINIMUM_SILENCE_DURATION_US)) goto L13;
    L16:
        r11 = r18;
        goto L17
    L13:
        if (r18 <= r11) goto L17;
        if (r18 < (r11 + SilenceSkippingAudioProcessor.DEFAULT_MINIMUM_SILENCE_DURATION_US)) goto L16;
    L17:
        r13 = r13 + 1;
        goto L4
    L19:
        return LongMath.divide(r11, 1000, RoundingMode.CEILING);
    }

    private long getManifestLoadRetryDelayMillis() {
        return Math.min((this.staleManifestReloadAttempt - 1) * 1000, 5000);
    }

    private static boolean hasVideoOrAudioAdaptationSets(Period r4) {
        int r0 = 0;
    L4:
        if (r0 >= r4.adaptationSets.size()) goto L12;
        int r1 = r4.adaptationSets.get(r0).type;
        if (r1 == 1) goto L11;
        if (r1 == 2) goto L11;
        r0 = r0 + 1;
    L11:
        return true;
    L12:
        return false;
    }

    private static boolean isIndexExplicit(Period r3) {
        int r0 = 0;
    L4:
        if (r0 >= r3.adaptationSets.size()) goto L13;
        DashSegmentIndex r1 = r3.adaptationSets.get(r0).representations.get(0).getIndex();
        if (r1 == null) goto L11;
        if (r1.isExplicit() == true) goto L17;
        r0 = r0 + 1;
        goto L4
    L17:
        return true;
    L11:
        return true;
    L13:
        return false;
    }

    private void loadNtpTimeOffset() {
        SntpClient.initialize(this.loader, new C02741(this));
    }

    private void onUtcTimestampResolutionError(IOException r5) {
        Log.m45e("DashMediaSource", "Failed to resolve time offset.", r5);
        this.elapsedRealtimeOffsetMs = System.currentTimeMillis() - SystemClock.elapsedRealtime();
        processManifest(true);
    }

    private void onUtcTimestampResolved(long r2) {
        this.elapsedRealtimeOffsetMs = r2;
        processManifest(true);
    }

    private void processManifest(boolean r39) {
        int r1 = 0;
    L4:
        if (r1 >= this.periodsById.size()) goto L9;
        int r2 = this.periodsById.keyAt(r1);
        if (r2 < this.firstPeriodId) goto L8;
        this.periodsById.valueAt(r1).updateManifest(this.manifest, r2 - this.firstPeriodId);
    L8:
        r1 = r1 + 1;
        goto L4
    L9:
        Period r3 = this.manifest.getPeriod(0);
        int r4 = this.manifest.getPeriodCount() - 1;
        Period r5 = this.manifest.getPeriod(r4);
        long r6 = this.manifest.getPeriodDurationUs(r4);
        long r8 = Util.msToUs(Util.getNowUnixTimeMs(this.elapsedRealtimeOffsetMs));
        long r10 = getAvailableStartTimeInManifestUs(r3, this.manifest.getPeriodDurationUs(0), r8);
        long r12 = getAvailableEndTimeInManifestUs(r5, r6, r8);
        if (this.manifest.dynamic == true) goto L12;
    L14:
        boolean r14 = false;
    L16:
        if (r14 == false) goto L21;
        if (this.manifest.timeShiftBufferDepthMs == C0180C.TIME_UNSET) goto L21;
        r10 = Math.max(r10, r12 - Util.msToUs(this.manifest.timeShiftBufferDepthMs));
    L21:
        long r7 = r12 - r10;
        if (this.manifest.dynamic == true) goto L24;
        long r30 = r7;
        long r17 = -9223372036854775807L;
        long r32 = 0;
        long r23 = -9223372036854775807L;
    L32:
        long r28 = r10 - Util.msToUs(r3.startMs);
        long r9 = this.manifest.availabilityStartTimeMs;
        long r11 = this.elapsedRealtimeOffsetMs;
        int r15 = this.firstPeriodId;
        DashManifest r13 = this.manifest;
        MediaItem r35 = getMediaItem();
        if (this.manifest.dynamic == false) goto L35;
        MediaItem.LiveConfiguration r16 = this.liveConfiguration;
    L36:
        refreshSourceInfo(new DashTimeline(r9, r23, r11, r15, r28, r30, r32, r13, r35, r16));
        if (this.sideloadedManifest == true) goto L56;
        this.handler.removeCallbacks(this.simulateManifestRefreshRunnable);
        if (r14 == false) goto L43;
        this.handler.postDelayed(this.simulateManifestRefreshRunnable, getIntervalUntilNextManifestRefreshMs(this.manifest, Util.getNowUnixTimeMs(this.elapsedRealtimeOffsetMs)));
    L43:
        if (this.manifestLoadPending == false) goto L45;
        startLoadingManifest();
        return;
    L45:
        if (r39 == false) goto L62;
        if (this.manifest.dynamic == false) goto L62;
        if (this.manifest.minUpdatePeriodMs == r17) goto L62;
        long r18 = this.manifest.minUpdatePeriodMs;
        if (r18 != 0) goto L53;
        r18 = 5000;
    L53:
        scheduleManifestRefresh(Math.max(0, (this.manifestLoadStartTimestampMs + r18) - SystemClock.elapsedRealtime()));
        return;
    L62:
        return;
    L56:
        return;
    L35:
        r16 = null;
        goto L36
    L24:
        if (this.manifest.availabilityStartTimeMs == C0180C.TIME_UNSET) goto L26;
        boolean r19 = true;
    L27:
        Assertions.checkState(r19);
        long r20 = (r8 - Util.msToUs(this.manifest.availabilityStartTimeMs)) - r10;
        updateLiveConfiguration(r20, r7);
        r17 = -9223372036854775807L;
        long r21 = this.manifest.availabilityStartTimeMs + Util.usToMs(r10);
        long r22 = r20 - Util.msToUs(this.liveConfiguration.targetOffsetMs);
        r30 = r7;
        long r24 = Math.min(this.minLiveStartPositionUs, r30 / 2);
        if (r22 >= r24) goto L30;
        r32 = r24;
        r23 = r21;
        goto L32
    L30:
        r32 = r22;
        r23 = r21;
        goto L32
    L26:
        r19 = false;
        goto L27
    L12:
        if (isIndexExplicit(r5) == true) goto L14;
        r14 = true;
        goto L16
    }

    private void resolveUtcTimingElement(UtcTimingElement r4) {
        String r0 = r4.schemeIdUri;
        if (Util.areEqual(r0, "urn:mpeg:dash:utc:direct:2014") == false) goto L5;
    L26:
        resolveUtcTimingElementDirect(r4);
        return;
    L5:
        if (Util.areEqual(r0, "urn:mpeg:dash:utc:direct:2012") == true) goto L26;
        if (Util.areEqual(r0, "urn:mpeg:dash:utc:http-iso:2014") == false) goto L10;
    L25:
        resolveUtcTimingElementHttp(r4, new Iso8601Parser());
        return;
    L10:
        if (Util.areEqual(r0, "urn:mpeg:dash:utc:http-iso:2012") == true) goto L25;
        if (Util.areEqual(r0, "urn:mpeg:dash:utc:http-xsdate:2014") == false) goto L15;
    L24:
        resolveUtcTimingElementHttp(r4, new XsDateTimeParser(null));
        return;
    L15:
        if (Util.areEqual(r0, "urn:mpeg:dash:utc:http-xsdate:2012") == true) goto L24;
        if (Util.areEqual(r0, "urn:mpeg:dash:utc:ntp:2014") == false) goto L20;
    L23:
        loadNtpTimeOffset();
        return;
    L20:
        if (Util.areEqual(r0, "urn:mpeg:dash:utc:ntp:2012") == true) goto L23;
        onUtcTimestampResolutionError(new IOException("Unsupported UTC timing scheme"));
    }

    private void resolveUtcTimingElementDirect(UtcTimingElement r5) {
        onUtcTimestampResolved(Util.parseXsDateTime(r5.value) - this.manifestLoadEndTimestampMs);     // Catch: ParserException -> L4
        return;
    L4:
        e = move-exception;
        onUtcTimestampResolutionError(e);
    }

    private void resolveUtcTimingElementHttp(UtcTimingElement r5, ParsingLoadable.Parser<Long> r6) {
        startLoading(new ParsingLoadable(this.dataSource, Uri.parse(r5.value), 5, r6), new UtcTimestampCallback(this, null), 1);
    }

    private void scheduleManifestRefresh(long r3) {
        this.handler.postDelayed(this.refreshManifestRunnable, r3);
    }

    private <T> void startLoading(ParsingLoadable<T> r8, Loader.Callback<ParsingLoadable<T>> r9, int r10) {
        long r5 = this.loader.startLoading(r8, r9, r10);
        this.manifestEventDispatcher.loadStarted(new LoadEventInfo(r8.loadTaskId, r8.dataSpec, r5), r8.type);
    }

    private void startLoadingManifest() {
        this.handler.removeCallbacks(this.refreshManifestRunnable);
        if (this.loader.hasFatalError() == false) goto L6;
        return;
    L6:
        if (this.loader.isLoading() == false) goto L9;
        this.manifestLoadPending = true;
        return;
    L9:
        Object r0 = this.manifestUriLock;
        monitor-enter(r0);
        Uri r1 = this.manifestUri;     // Catch: Throwable -> L15
        monitor-exit(r0);     // Catch: Throwable -> L15
        this.manifestLoadPending = false;
        startLoading(new ParsingLoadable(this.dataSource, r1, 4, this.manifestParser), this.manifestCallback, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(4));
        return;
    L15:
        th = move-exception;
        throw th;
    }

    private void updateLiveConfiguration(long r19, long r21) {
        MediaItem.LiveConfiguration r1 = getMediaItem().liveConfiguration;
        long r6 = Util.usToMs(r19);
        if (r1.maxOffsetMs == C0180C.TIME_UNSET) goto L6;
        long r10 = Math.min(r6, r1.maxOffsetMs);
    L11:
        long r2 = Util.usToMs(r19 - r21);
        if (r2 >= 0) goto L17;
        if (r10 <= 0) goto L17;
        r2 = 0;
    L17:
        if (this.manifest.minBufferTimeMs == C0180C.TIME_UNSET) goto L19;
        long r4 = Math.min(this.manifest.minBufferTimeMs + r2, r6);
    L21:
        if (r1.minOffsetMs == C0180C.TIME_UNSET) goto L24;
        long r14 = Util.constrainValue(r1.minOffsetMs, r4, r6);
    L30:
        if (r14 <= r10) goto L32;
        long r16 = r14;
    L34:
        if (this.liveConfiguration.targetOffsetMs == C0180C.TIME_UNSET) goto L37;
        long r3 = this.liveConfiguration.targetOffsetMs;
    L46:
        if (r3 >= r14) goto L49;
        r3 = r14;
    L49:
        if (r3 <= r16) goto L52;
        r3 = Util.constrainValue(Util.usToMs(r19 - Math.min(this.minLiveStartPositionUs, r21 / 2)), r14, r16);
    L52:
        float r5 = -3.4028235E38f;
        if (r1.minPlaybackSpeed == (-3.4028235E38f)) goto L56;
        r5 = r1.minPlaybackSpeed;
    L58:
        float r7 = -3.4028235E38f;
        if (r1.maxPlaybackSpeed == (-3.4028235E38f)) goto L62;
        r7 = r1.maxPlaybackSpeed;
    L65:
        if (r5 == (-3.4028235E38f)) goto L67;
    L73:
        this.liveConfiguration = new MediaItem.LiveConfiguration.Builder().setTargetOffsetMs(r3).setMinOffsetMs(r14).setMaxOffsetMs(r16).setMinPlaybackSpeed(r5).setMaxPlaybackSpeed(r7).build();
        return;
    L67:
        if (r7 != (-3.4028235E38f)) goto L73;
        if (this.manifest.serviceDescription != null) goto L71;
    L72:
        r5 = 1.0f;
        r7 = 1.0f;
        goto L73
    L71:
        if (this.manifest.serviceDescription.targetOffsetMs != C0180C.TIME_UNSET) goto L73;
    L62:
        if (this.manifest.serviceDescription == null) goto L65;
        r7 = this.manifest.serviceDescription.maxPlaybackSpeed;
        goto L65
    L56:
        if (this.manifest.serviceDescription == null) goto L58;
        r5 = this.manifest.serviceDescription.minPlaybackSpeed;
        goto L58
    L37:
        if (this.manifest.serviceDescription == null) goto L42;
        if (this.manifest.serviceDescription.targetOffsetMs == C0180C.TIME_UNSET) goto L42;
        r3 = this.manifest.serviceDescription.targetOffsetMs;
    L42:
        if (this.manifest.suggestedPresentationDelayMs == C0180C.TIME_UNSET) goto L44;
        r3 = this.manifest.suggestedPresentationDelayMs;
        goto L46
    L44:
        r3 = this.fallbackTargetLiveOffsetMs;
        goto L46
    L32:
        r16 = r10;
        goto L34
    L24:
        if (this.manifest.serviceDescription != null) goto L26;
    L28:
        r14 = r4;
        goto L30
    L26:
        if (this.manifest.serviceDescription.minOffsetMs == C0180C.TIME_UNSET) goto L28;
        r14 = Util.constrainValue(this.manifest.serviceDescription.minOffsetMs, r4, r6);
        goto L30
    L19:
        r4 = r2;
        goto L21
    L6:
        if (this.manifest.serviceDescription != null) goto L8;
    L10:
        r10 = r6;
        goto L11
    L8:
        if (this.manifest.serviceDescription.maxOffsetMs == C0180C.TIME_UNSET) goto L10;
        r10 = Math.min(r6, this.manifest.serviceDescription.maxOffsetMs);
        goto L11
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean canUpdateMediaItem(MediaItem r6) {
        MediaItem r0 = getMediaItem();
        MediaItem.LocalConfiguration r1 = (MediaItem.LocalConfiguration) Assertions.checkNotNull(r0.localConfiguration);
        MediaItem.LocalConfiguration r2 = r6.localConfiguration;
        if (r2 != null) goto L5;
    L13:
        return false;
    L5:
        if (r2.uri.equals(r1.uri) == false) goto L13;
        if (r2.streamKeys.equals(r1.streamKeys) == false) goto L13;
        if (Util.areEqual(r2.drmConfiguration, r1.drmConfiguration) == false) goto L13;
        if (r0.liveConfiguration.equals(r6.liveConfiguration) == false) goto L13;
        return true;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r24, Allocator r25, long r26) {
        int r8 = ((Integer) r24.periodUid).intValue() - this.firstPeriodId;
        MediaSourceEventListener.EventDispatcher r15 = createEventDispatcher(r24);
        DrmSessionEventListener.EventDispatcher r13 = createDrmEventDispatcher(r24);
        DashMediaPeriod r4 = new DashMediaPeriod(this.firstPeriodId + r8, this.manifest, this.baseUrlExclusionList, r8, this.chunkSourceFactory, this.mediaTransferListener, this.cmcdConfiguration, this.drmSessionManager, r13, this.loadErrorHandlingPolicy, r15, this.elapsedRealtimeOffsetMs, this.manifestLoadErrorThrower, r25, this.compositeSequenceableLoaderFactory, this.playerEmsgCallback, getPlayerId());
        this.periodsById.put(r4.f37id, r4);
        return r4;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public synchronized MediaItem getMediaItem() {
        monitor-enter(this);
        MediaItem r0 = this.mediaItem;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    /* JADX INFO: renamed from: lambda$new$0$androidx-media3-exoplayer-dash-DashMediaSource, reason: not valid java name */
    /* synthetic */ void m472lambda$new$0$androidxmedia3exoplayerdashDashMediaSource() {
        processManifest(false);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.manifestLoadErrorThrower.maybeThrowError();
    }

    void onDashManifestPublishTimeExpired(long r5) {
        if (this.expiredManifestPublishTimeUs != C0180C.TIME_UNSET) goto L5;
    L6:
        this.expiredManifestPublishTimeUs = r5;
        return;
    L5:
        if (this.expiredManifestPublishTimeUs < r5) goto L6;
    }

    void onDashManifestRefreshRequested() {
        this.handler.removeCallbacks(this.simulateManifestRefreshRunnable);
        startLoadingManifest();
    }

    void onLoadCanceled(ParsingLoadable<?> r13, long r14, long r16) {
        LoadEventInfo r0 = new LoadEventInfo(r13.loadTaskId, r13.dataSpec, r13.getUri(), r13.getResponseHeaders(), r14, r16, r13.bytesLoaded());
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r13.loadTaskId);
        this.manifestEventDispatcher.loadCanceled(r0, r13.type);
    }

    void onManifestLoadCompleted(ParsingLoadable<DashManifest> r19, long r20, long r22) {
        LoadEventInfo r3 = new LoadEventInfo(r19.loadTaskId, r19.dataSpec, r19.getUri(), r19.getResponseHeaders(), r20, r22, r19.bytesLoaded());
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r19.loadTaskId);
        this.manifestEventDispatcher.loadCompleted(r3, r19.type);
        DashManifest r4 = r19.getResult();
        boolean r5 = false;
        if (this.manifest != null) goto L5;
        int r0 = 0;
    L6:
        int r6 = r0;
        long r7 = r4.getPeriod(0).startMs;
        int r11 = 0;
    L7:
        if (r11 >= r6) goto L12;
        if (this.manifest.getPeriod(r11).startMs >= r7) goto L12;
        r11 = r11 + 1;
    L12:
        if (r4.dynamic == false) goto L29;
        boolean r1 = false;
        if ((r6 - r11) <= r4.getPeriodCount()) goto L17;
        Log.m48w("DashMediaSource", "Loaded out of sync manifest");
        r1 = true;
        long r16 = -9223372036854775807L;
    L22:
        if (r1 == false) goto L28;
        int r8 = this.staleManifestReloadAttempt;
        this.staleManifestReloadAttempt = r8 + 1;
        if (r8 >= this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(r19.type)) goto L26;
        scheduleManifestRefresh(getManifestLoadRetryDelayMillis());
        return;
    L26:
        this.manifestFatalError = new DashManifestStaleException();
        return;
    L28:
        this.staleManifestReloadAttempt = 0;
    L30:
        this.manifest = r4;
        this.manifestLoadPending &= this.manifest.dynamic;
        this.manifestLoadStartTimestampMs = r20 - r22;
        this.manifestLoadEndTimestampMs = r20;
        this.firstPeriodId += r11;
        Object r12 = this.manifestUriLock;
        monitor-enter(r12);
    L52:
        th = move-exception;
        throw th;
    L33:
        if (r19.dataSpec.uri != this.manifestUri) goto L35;
        r5 = true;
    L35:
        if (r5 == true) goto L37;
    L41:
        monitor-exit(r12);     // Catch: Throwable -> L52
        if (this.manifest.dynamic == true) goto L45;
    L50:
        processManifest(true);
        return;
    L45:
        if (this.elapsedRealtimeOffsetMs != r16) goto L50;
        if (this.manifest.utcTiming == null) goto L49;
        resolveUtcTimingElement(this.manifest.utcTiming);
        return;
    L49:
        loadNtpTimeOffset();
        return;
    L37:
        if (this.manifest.location == null) goto L39;
        Uri r2 = this.manifest.location;     // Catch: Throwable -> L52
    L40:
        this.manifestUri = r2;     // Catch: Throwable -> L52
        goto L41
    L39:
        r2 = r19.getUri();     // Catch: Throwable -> L52
        goto L40
    L17:
        if (this.expiredManifestPublishTimeUs == C0180C.TIME_UNSET) goto L21;
        r16 = -9223372036854775807L;
        if ((r4.publishTimeMs * 1000) > this.expiredManifestPublishTimeUs) goto L22;
        Log.m48w("DashMediaSource", "Loaded stale dynamic manifest: " + r4.publishTimeMs + ", " + this.expiredManifestPublishTimeUs);
        r1 = true;
        goto L22
    L21:
        r16 = -9223372036854775807L;
        goto L22
    L29:
        r16 = -9223372036854775807L;
        goto L30
    L5:
        r0 = this.manifest.getPeriodCount();
        goto L6
    }

    Loader.LoadErrorAction onManifestLoadError(ParsingLoadable<DashManifest> r14, long r15, long r17, IOException r19, int r20) {
        LoadEventInfo r1 = new LoadEventInfo(r14.loadTaskId, r14.dataSpec, r14.getUri(), r14.getResponseHeaders(), r15, r17, r14.bytesLoaded());
        long r5 = this.loadErrorHandlingPolicy.getRetryDelayMsFor(new LoadErrorHandlingPolicy.LoadErrorInfo(r1, new MediaLoadData(r14.type), r19, r20));
        if (r5 != C0180C.TIME_UNSET) goto L5;
        Loader.LoadErrorAction r7 = Loader.DONT_RETRY_FATAL;
    L6:
        boolean r8 = !r7.isRetry();
        this.manifestEventDispatcher.loadError(r1, r14.type, r19, r8);
        if (r8 == false) goto L9;
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r14.loadTaskId);
    L9:
        return r7;
    L5:
        r7 = Loader.createRetryAction(false, r5);
        goto L6
    }

    void onUtcTimestampLoadCompleted(ParsingLoadable<Long> r13, long r14, long r16) {
        LoadEventInfo r0 = new LoadEventInfo(r13.loadTaskId, r13.dataSpec, r13.getUri(), r13.getResponseHeaders(), r14, r16, r13.bytesLoaded());
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r13.loadTaskId);
        this.manifestEventDispatcher.loadCompleted(r0, r13.type);
        onUtcTimestampResolved(r13.getResult().longValue() - r14);
    }

    Loader.LoadErrorAction onUtcTimestampLoadError(ParsingLoadable<Long> r17, long r18, long r20, IOException r22) {
        this.manifestEventDispatcher.loadError(new LoadEventInfo(r17.loadTaskId, r17.dataSpec, r17.getUri(), r17.getResponseHeaders(), r18, r20, r17.bytesLoaded()), r17.type, r22, true);
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r17.loadTaskId);
        onUtcTimestampResolutionError(r22);
        return Loader.DONT_RETRY;
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void prepareSourceInternal(TransferListener r4) {
        this.mediaTransferListener = r4;
        this.drmSessionManager.setPlayer(Looper.myLooper(), getPlayerId());
        this.drmSessionManager.prepare();
        if (this.sideloadedManifest == false) goto L5;
        processManifest(false);
        return;
    L5:
        this.dataSource = this.manifestDataSourceFactory.createDataSource();
        this.loader = new Loader("DashMediaSource");
        this.handler = Util.createHandlerForCurrentLooper();
        startLoadingManifest();
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r4) {
        DashMediaPeriod r0 = (DashMediaPeriod) r4;
        r0.release();
        this.periodsById.remove(r0.f37id);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void releaseSourceInternal() {
        this.manifestLoadPending = false;
        this.dataSource = null;
        if (this.loader == null) goto L5;
        this.loader.release();
        this.loader = null;
    L5:
        this.manifestLoadStartTimestampMs = 0;
        this.manifestLoadEndTimestampMs = 0;
        this.manifestUri = this.initialManifestUri;
        this.manifestFatalError = null;
        if (this.handler == null) goto L8;
        this.handler.removeCallbacksAndMessages(null);
        this.handler = null;
    L8:
        this.elapsedRealtimeOffsetMs = C0180C.TIME_UNSET;
        this.staleManifestReloadAttempt = 0;
        this.expiredManifestPublishTimeUs = C0180C.TIME_UNSET;
        this.periodsById.clear();
        this.baseUrlExclusionList.reset();
        this.drmSessionManager.release();
    }

    public void replaceManifestUri(Uri r3) {
        Object r0 = this.manifestUriLock;
        monitor-enter(r0);
        this.manifestUri = r3;     // Catch: Throwable -> L7
        this.initialManifestUri = r3;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public synchronized void updateMediaItem(MediaItem r1) {
        monitor-enter(this);
        this.mediaItem = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }
}
