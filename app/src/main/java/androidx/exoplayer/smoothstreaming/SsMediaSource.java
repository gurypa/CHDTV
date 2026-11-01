package androidx.media3.exoplayer.smoothstreaming;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaLibraryInfo;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.StreamKey;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.drm.DefaultDrmSessionManagerProvider;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import androidx.media3.exoplayer.drm.DrmSessionManager;
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider;
import androidx.media3.exoplayer.offline.FilteringManifestParser;
import androidx.media3.exoplayer.smoothstreaming.manifest.SsManifest;
import androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser;
import androidx.media3.exoplayer.source.BaseMediaSource;
import androidx.media3.exoplayer.source.CompositeSequenceableLoaderFactory;
import androidx.media3.exoplayer.source.DefaultCompositeSequenceableLoaderFactory;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MediaLoadData;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.MediaSourceEventListener;
import androidx.media3.exoplayer.source.MediaSourceFactory;
import androidx.media3.exoplayer.source.SinglePeriodTimeline;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.DefaultLoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.Loader;
import androidx.media3.exoplayer.upstream.LoaderErrorThrower;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class SsMediaSource extends BaseMediaSource implements Loader.Callback<ParsingLoadable<SsManifest>> {
    public static final long DEFAULT_LIVE_PRESENTATION_DELAY_MS = 30000;
    private static final int MINIMUM_MANIFEST_REFRESH_PERIOD_MS = 5000;
    private static final long MIN_LIVE_DEFAULT_START_POSITION_US = 5000000;
    private final SsChunkSource.Factory chunkSourceFactory;
    private final CmcdConfiguration cmcdConfiguration;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final DrmSessionManager drmSessionManager;
    private final long livePresentationDelayMs;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private SsManifest manifest;
    private DataSource manifestDataSource;
    private final DataSource.Factory manifestDataSourceFactory;
    private final MediaSourceEventListener.EventDispatcher manifestEventDispatcher;
    private long manifestLoadStartTimestamp;
    private Loader manifestLoader;
    private LoaderErrorThrower manifestLoaderErrorThrower;
    private final ParsingLoadable.Parser<? extends SsManifest> manifestParser;
    private Handler manifestRefreshHandler;
    private final Uri manifestUri;
    private MediaItem mediaItem;
    private final ArrayList<SsMediaPeriod> mediaPeriods;
    private TransferListener mediaTransferListener;
    private final boolean sideloadedManifest;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.smoothstreaming.SsMediaSource$1 */
    static /* synthetic */ class C03251 {
    }

    public static final class Factory implements MediaSourceFactory {
        private final SsChunkSource.Factory chunkSourceFactory;
        private CmcdConfiguration.Factory cmcdConfigurationFactory;
        private CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
        private DrmSessionManagerProvider drmSessionManagerProvider;
        private long livePresentationDelayMs;
        private LoadErrorHandlingPolicy loadErrorHandlingPolicy;
        private final DataSource.Factory manifestDataSourceFactory;
        private ParsingLoadable.Parser<? extends SsManifest> manifestParser;

        public Factory(DataSource.Factory r2) {
            this(new DefaultSsChunkSource.Factory(r2), r2);
        }

        public Factory(SsChunkSource.Factory r3, DataSource.Factory r4) {
            this.chunkSourceFactory = (SsChunkSource.Factory) Assertions.checkNotNull(r3);
            this.manifestDataSourceFactory = r4;
            this.drmSessionManagerProvider = new DefaultDrmSessionManagerProvider();
            this.loadErrorHandlingPolicy = new DefaultLoadErrorHandlingPolicy();
            this.livePresentationDelayMs = 30000;
            this.compositeSequenceableLoaderFactory = new DefaultCompositeSequenceableLoaderFactory();
            experimentalParseSubtitlesDuringExtraction(true);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public SsMediaSource createMediaSource(MediaItem r15) {
            Assertions.checkNotNull(r15.localConfiguration);
            ParsingLoadable.Parser<? extends SsManifest> r0 = this.manifestParser;
            if (r0 != null) goto L5;
            r0 = new SsManifestParser();
        L5:
            List<StreamKey> r13 = r15.localConfiguration.streamKeys;
            if (r13.isEmpty() == true) goto L8;
            ParsingLoadable.Parser<? extends SsManifest> r4 = new FilteringManifestParser(r0, r13);
        L10:
            if (this.cmcdConfigurationFactory != null) goto L12;
            CmcdConfiguration r7 = null;
        L14:
            return new SsMediaSource(r15, null, this.manifestDataSourceFactory, r4, this.chunkSourceFactory, this.compositeSequenceableLoaderFactory, r7, this.drmSessionManagerProvider.get(r15), this.loadErrorHandlingPolicy, this.livePresentationDelayMs, null);
        L12:
            r7 = this.cmcdConfigurationFactory.createCmcdConfiguration(r15);
            goto L14
        L8:
            r4 = r0;
            goto L10
        }

        public SsMediaSource createMediaSource(SsManifest r2) {
            return createMediaSource(r2, MediaItem.fromUri(Uri.EMPTY));
        }

        public SsMediaSource createMediaSource(SsManifest r19, MediaItem r20) {
            boolean r4 = true;
            Assertions.checkArgument(!r19.isLive);
            if (r20.localConfiguration == null) goto L5;
            List<StreamKey> r3 = r20.localConfiguration.streamKeys;
        L7:
            if (r3.isEmpty() == true) goto L9;
            SsManifest r7 = r19.copy(r3);
        L11:
            if (r20.localConfiguration != null) goto L14;
            r4 = false;
        L14:
            MediaItem.Builder r1 = r20.buildUpon().setMimeType(MimeTypes.APPLICATION_SS);
            if (r4 == false) goto L17;
            Uri r5 = r20.localConfiguration.uri;
        L18:
            MediaItem r6 = r1.setUri(r5).build();
            if (this.cmcdConfigurationFactory != null) goto L21;
            CmcdConfiguration r12 = null;
        L23:
            return new SsMediaSource(r6, r7, null, null, this.chunkSourceFactory, this.compositeSequenceableLoaderFactory, r12, this.drmSessionManagerProvider.get(r6), this.loadErrorHandlingPolicy, this.livePresentationDelayMs, null);
        L21:
            r12 = this.cmcdConfigurationFactory.createCmcdConfiguration(r6);
            goto L23
        L17:
            r5 = Uri.EMPTY;
            goto L18
        L9:
            r7 = r19;
            goto L11
        L5:
            r3 = ImmutableList.m212of();
            goto L7
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
            return new int[]{1};
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
            this.compositeSequenceableLoaderFactory = (CompositeSequenceableLoaderFactory) Assertions.checkNotNull(r2, "SsMediaSource.Factory#setCompositeSequenceableLoaderFactory no longer handles null by instantiating a new DefaultCompositeSequenceableLoaderFactory. Explicitly construct and pass an instance in order to retain the old behavior.");
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

        public Factory setLivePresentationDelayMs(long r1) {
            this.livePresentationDelayMs = r1;
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

        public Factory setManifestParser(ParsingLoadable.Parser<? extends SsManifest> r1) {
            this.manifestParser = r1;
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

    /* JADX INFO: renamed from: $r8$lambda$Y-Vfqjh0h8BD5_7Z0ZPDUGJlBf0, reason: not valid java name */
    public static /* synthetic */ void m477$r8$lambda$YVfqjh0h8BD5_7Z0ZPDUGJlBf0(SsMediaSource r0) {
        r0.startLoadingManifest();
    }

    static {
        MediaLibraryInfo.registerModule("media3.exoplayer.smoothstreaming");
    }

    private SsMediaSource(MediaItem r17, SsManifest r18, DataSource.Factory r19, ParsingLoadable.Parser<? extends SsManifest> r20, SsChunkSource.Factory r21, CompositeSequenceableLoaderFactory r22, CmcdConfiguration r23, DrmSessionManager r24, LoadErrorHandlingPolicy r25, long r26) {
        boolean r3 = false;
        if (r18 != null) goto L5;
    L8:
        boolean r5 = true;
    L9:
        Assertions.checkState(r5);
        this.mediaItem = r17;
        MediaItem.LocalConfiguration r6 = (MediaItem.LocalConfiguration) Assertions.checkNotNull(r17.localConfiguration);
        this.manifest = r18;
        if (r6.uri.equals(Uri.EMPTY) == false) goto L12;
        Uri r7 = null;
    L13:
        this.manifestUri = r7;
        this.manifestDataSourceFactory = r19;
        this.manifestParser = r20;
        this.chunkSourceFactory = r21;
        this.compositeSequenceableLoaderFactory = r22;
        this.cmcdConfiguration = r23;
        this.drmSessionManager = r24;
        this.loadErrorHandlingPolicy = r25;
        this.livePresentationDelayMs = r26;
        this.manifestEventDispatcher = createEventDispatcher(null);
        if (r18 == null) goto L16;
        r3 = true;
    L16:
        this.sideloadedManifest = r3;
        this.mediaPeriods = new ArrayList();
        return;
    L12:
        r7 = Util.fixSmoothStreamingIsmManifestUri(r6.uri);
        goto L13
    L5:
        if (r18.isLive == false) goto L8;
        r5 = false;
        goto L9
    }

    /* synthetic */ SsMediaSource(MediaItem r1, SsManifest r2, DataSource.Factory r3, ParsingLoadable.Parser r4, SsChunkSource.Factory r5, CompositeSequenceableLoaderFactory r6, CmcdConfiguration r7, DrmSessionManager r8, LoadErrorHandlingPolicy r9, long r10, C03251 r12) {
        this(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
    }

    private void processManifest() {
        int r1 = 0;
    L4:
        if (r1 >= this.mediaPeriods.size()) goto L6;
        this.mediaPeriods.get(r1).updateManifest(this.manifest);
        r1 = r1 + 1;
        goto L4
    L6:
        long r3 = Long.MIN_VALUE;
        SsManifest.StreamElement[] r5 = this.manifest.streamElements;
        int r6 = r5.length;
        long r13 = Long.MAX_VALUE;
        int r2 = 0;
    L7:
        if (r2 >= r6) goto L12;
        SsManifest.StreamElement r4 = r5[r2];
        if (r4.chunkCount <= 0) goto L11;
        long r8 = Math.min(r13, r4.getStartTimeUs(0));
        r3 = Math.max(r3, r4.getStartTimeUs(r4.chunkCount - 1) + r4.getChunkDurationUs(r4.chunkCount - 1));
        r13 = r8;
    L11:
        r2 = r2 + 1;
        goto L7
    L12:
        long r7 = 0;
        if (r13 != Long.MAX_VALUE) goto L19;
        if (this.manifest.isLive == false) goto L17;
        r7 = -9223372036854775807L;
    L17:
        SinglePeriodTimeline r15 = new SinglePeriodTimeline(r7, 0, 0, 0, true, this.manifest.isLive, this.manifest.isLive, this.manifest, getMediaItem());
    L36:
        refreshSourceInfo(r15);
        return;
    L19:
        if (this.manifest.isLive == false) goto L32;
        if (this.manifest.dvrWindowLengthUs != C0180C.TIME_UNSET) goto L23;
    L25:
        long r20 = r13;
    L26:
        long r18 = r3 - r20;
        long r9 = r18 - Util.msToUs(this.livePresentationDelayMs);
        if (r9 >= 5000000) goto L29;
        long r22 = Math.min(5000000, r18 / 2);
    L30:
        r15 = new SinglePeriodTimeline(C0180C.TIME_UNSET, r18, r20, r22, true, true, true, this.manifest, getMediaItem());
        goto L36
    L29:
        r22 = r9;
        goto L30
    L23:
        if (this.manifest.dvrWindowLengthUs <= 0) goto L25;
        r20 = Math.max(r13, r3 - this.manifest.dvrWindowLengthUs);
        goto L26
    L32:
        if (this.manifest.durationUs == C0180C.TIME_UNSET) goto L34;
        long r10 = this.manifest.durationUs;
    L35:
        long r11 = r10;
        r15 = new SinglePeriodTimeline(r13 + r11, r11, r13, 0, true, false, false, this.manifest, getMediaItem());
        goto L36
    L34:
        r10 = r3 - r13;
        goto L35
    }

    private void scheduleManifestRefresh() {
        if (this.manifest.isLive == true) goto L5;
        return;
    L5:
        long r2 = Math.max(0, (this.manifestLoadStartTimestamp + 5000) - SystemClock.elapsedRealtime());
        this.manifestRefreshHandler.postDelayed(new SsMediaSource$$ExternalSyntheticLambda0(this), r2);
    }

    private void startLoadingManifest() {
        if (this.manifestLoader.hasFatalError() == false) goto L5;
        return;
    L5:
        ParsingLoadable r0 = new ParsingLoadable(this.manifestDataSource, this.manifestUri, 4, this.manifestParser);
        long r7 = this.manifestLoader.startLoading(r0, this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(r0.type));
        this.manifestEventDispatcher.loadStarted(new LoadEventInfo(r0.loadTaskId, r0.dataSpec, r7), r0.type);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean canUpdateMediaItem(MediaItem r5) {
        MediaItem.LocalConfiguration r0 = (MediaItem.LocalConfiguration) Assertions.checkNotNull(getMediaItem().localConfiguration);
        MediaItem.LocalConfiguration r1 = r5.localConfiguration;
        if (r1 != null) goto L5;
    L11:
        return false;
    L5:
        if (r1.uri.equals(r0.uri) == false) goto L11;
        if (r1.streamKeys.equals(r0.streamKeys) == false) goto L11;
        if (Util.areEqual(r1.drmConfiguration, r0.drmConfiguration) == false) goto L11;
        return true;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r13, Allocator r14, long r15) {
        MediaSourceEventListener.EventDispatcher r9 = createEventDispatcher(r13);
        DrmSessionEventListener.EventDispatcher r7 = createDrmEventDispatcher(r13);
        SsMediaPeriod r0 = new SsMediaPeriod(this.manifest, this.chunkSourceFactory, this.mediaTransferListener, this.compositeSequenceableLoaderFactory, this.cmcdConfiguration, this.drmSessionManager, r7, this.loadErrorHandlingPolicy, r9, this.manifestLoaderErrorThrower, r14);
        this.mediaPeriods.add(r0);
        return r0;
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

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.manifestLoaderErrorThrower.maybeThrowError();
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ void onLoadCanceled(Loader.Loadable r8, long r9, long r11, boolean r13) {
        onLoadCanceled((ParsingLoadable) r8, r9, r11, r13);
    }

    public void onLoadCanceled(ParsingLoadable<SsManifest> r13, long r14, long r16, boolean r18) {
        LoadEventInfo r0 = new LoadEventInfo(r13.loadTaskId, r13.dataSpec, r13.getUri(), r13.getResponseHeaders(), r14, r16, r13.bytesLoaded());
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r13.loadTaskId);
        this.manifestEventDispatcher.loadCanceled(r0, r13.type);
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ void onLoadCompleted(Loader.Loadable r7, long r8, long r10) {
        onLoadCompleted((ParsingLoadable) r7, r8, r10);
    }

    public void onLoadCompleted(ParsingLoadable<SsManifest> r13, long r14, long r16) {
        LoadEventInfo r0 = new LoadEventInfo(r13.loadTaskId, r13.dataSpec, r13.getUri(), r13.getResponseHeaders(), r14, r16, r13.bytesLoaded());
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r13.loadTaskId);
        this.manifestEventDispatcher.loadCompleted(r0, r13.type);
        this.manifest = r13.getResult();
        this.manifestLoadStartTimestamp = r14 - r16;
        processManifest();
        scheduleManifestRefresh();
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ Loader.LoadErrorAction onLoadError(Loader.Loadable r9, long r10, long r12, IOException r14, int r15) {
        return onLoadError((ParsingLoadable) r9, r10, r12, r14, r15);
    }

    public Loader.LoadErrorAction onLoadError(ParsingLoadable<SsManifest> r14, long r15, long r17, IOException r19, int r20) {
        LoadEventInfo r1 = new LoadEventInfo(r14.loadTaskId, r14.dataSpec, r14.getUri(), r14.getResponseHeaders(), r15, r17, r14.bytesLoaded());
        long r3 = this.loadErrorHandlingPolicy.getRetryDelayMsFor(new LoadErrorHandlingPolicy.LoadErrorInfo(r1, new MediaLoadData(r14.type), r19, r20));
        if (r3 != C0180C.TIME_UNSET) goto L5;
        Loader.LoadErrorAction r6 = Loader.DONT_RETRY_FATAL;
    L6:
        boolean r7 = !r6.isRetry();
        this.manifestEventDispatcher.loadError(r1, r14.type, r19, r7);
        if (r7 == false) goto L9;
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r14.loadTaskId);
    L9:
        return r6;
    L5:
        r6 = Loader.createRetryAction(false, r3);
        goto L6
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* synthetic */ void onLoadStarted(Loader.Loadable r1, long r2, long r4, int r6) {
        Loader.Callback.CC.$default$onLoadStarted(this, r1, r2, r4, r6);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void prepareSourceInternal(TransferListener r4) {
        this.mediaTransferListener = r4;
        this.drmSessionManager.setPlayer(Looper.myLooper(), getPlayerId());
        this.drmSessionManager.prepare();
        if (this.sideloadedManifest == false) goto L5;
        this.manifestLoaderErrorThrower = new LoaderErrorThrower.Placeholder();
        processManifest();
        return;
    L5:
        this.manifestDataSource = this.manifestDataSourceFactory.createDataSource();
        this.manifestLoader = new Loader("SsMediaSource");
        this.manifestLoaderErrorThrower = this.manifestLoader;
        this.manifestRefreshHandler = Util.createHandlerForCurrentLooper();
        startLoadingManifest();
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r2) {
        ((SsMediaPeriod) r2).release();
        this.mediaPeriods.remove(r2);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void releaseSourceInternal() {
        if (this.sideloadedManifest == false) goto L5;
        SsManifest r0 = this.manifest;
    L6:
        this.manifest = r0;
        this.manifestDataSource = null;
        this.manifestLoadStartTimestamp = 0;
        if (this.manifestLoader == null) goto L10;
        this.manifestLoader.release();
        this.manifestLoader = null;
    L10:
        if (this.manifestRefreshHandler == null) goto L12;
        this.manifestRefreshHandler.removeCallbacksAndMessages(null);
        this.manifestRefreshHandler = null;
    L12:
        this.drmSessionManager.release();
        return;
    L5:
        r0 = null;
        goto L6
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
