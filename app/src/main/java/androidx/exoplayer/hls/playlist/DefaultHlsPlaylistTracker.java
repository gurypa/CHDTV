package androidx.media3.exoplayer.hls.playlist;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import androidx.media3.common.C0180C;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.exoplayer.hls.HlsDataSourceFactory;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MediaLoadData;
import androidx.media3.exoplayer.source.MediaSourceEventListener;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.Loader;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultHlsPlaylistTracker implements HlsPlaylistTracker, Loader.Callback<ParsingLoadable<HlsPlaylist>> {
    public static final double DEFAULT_PLAYLIST_STUCK_TARGET_DURATION_COEFFICIENT = 3.5d;
    public static final HlsPlaylistTracker.Factory FACTORY = null;
    private final HlsDataSourceFactory dataSourceFactory;
    private MediaSourceEventListener.EventDispatcher eventDispatcher;
    private Loader initialPlaylistLoader;
    private long initialStartTimeUs;
    private boolean isLive;
    private final CopyOnWriteArrayList<HlsPlaylistTracker.PlaylistEventListener> listeners;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private HlsMultivariantPlaylist multivariantPlaylist;
    private final HashMap<Uri, MediaPlaylistBundle> playlistBundles;
    private final HlsPlaylistParserFactory playlistParserFactory;
    private Handler playlistRefreshHandler;
    private final double playlistStuckTargetDurationCoefficient;
    private HlsMediaPlaylist primaryMediaPlaylistSnapshot;
    private Uri primaryMediaPlaylistUrl;
    private HlsPlaylistTracker.PrimaryPlaylistListener primaryPlaylistListener;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.hls.playlist.DefaultHlsPlaylistTracker$1 */
    static /* synthetic */ class C02981 {
    }

    private class FirstPrimaryMediaPlaylistListener implements HlsPlaylistTracker.PlaylistEventListener {
        final /* synthetic */ DefaultHlsPlaylistTracker this$0;

        private FirstPrimaryMediaPlaylistListener(DefaultHlsPlaylistTracker r1) {
            this.this$0 = r1;
        }

        /* synthetic */ FirstPrimaryMediaPlaylistListener(DefaultHlsPlaylistTracker r1, C02981 r2) {
            this(r1);
        }

        @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker.PlaylistEventListener
        public void onPlaylistChanged() {
            DefaultHlsPlaylistTracker.access$1900(this.this$0).remove(this);
        }

        @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker.PlaylistEventListener
        public boolean onPlaylistError(Uri r11, LoadErrorHandlingPolicy.LoadErrorInfo r12, boolean r13) {
            if (DefaultHlsPlaylistTracker.access$2000(this.this$0) != null) goto L20;
            long r2 = SystemClock.elapsedRealtime();
            int r0 = 0;
            List<HlsMultivariantPlaylist.Variant> r4 = ((HlsMultivariantPlaylist) Util.castNonNull(DefaultHlsPlaylistTracker.access$1200(this.this$0))).variants;
            int r5 = 0;
        L6:
            if (r5 >= r4.size()) goto L13;
            MediaPlaylistBundle r6 = (MediaPlaylistBundle) DefaultHlsPlaylistTracker.access$2100(this.this$0).get(r4.get(r5).url);
            if (r6 == null) goto L12;
            if (r2 >= MediaPlaylistBundle.access$300(r6)) goto L12;
            r0 = r0 + 1;
        L12:
            r5 = r5 + 1;
            goto L6
        L13:
            LoadErrorHandlingPolicy.FallbackOptions r7 = new LoadErrorHandlingPolicy.FallbackOptions(1, 0, DefaultHlsPlaylistTracker.access$1200(this.this$0).variants.size(), r0);
            LoadErrorHandlingPolicy.FallbackSelection r8 = DefaultHlsPlaylistTracker.access$900(this.this$0).getFallbackSelectionFor(r7, r12);
            if (r8 == null) goto L20;
            if (r8.type != 2) goto L20;
            MediaPlaylistBundle r9 = (MediaPlaylistBundle) DefaultHlsPlaylistTracker.access$2100(this.this$0).get(r11);
            if (r9 == null) goto L20;
            MediaPlaylistBundle.access$000(r9, r8.exclusionDurationMs);
        L20:
            return false;
        }
    }

    private final class MediaPlaylistBundle implements Loader.Callback<ParsingLoadable<HlsPlaylist>> {
        private static final String BLOCK_MSN_PARAM = "_HLS_msn";
        private static final String BLOCK_PART_PARAM = "_HLS_part";
        private static final String SKIP_PARAM = "_HLS_skip";
        private boolean activeForPlayback;
        private long earliestNextLoadTimeMs;
        private long excludeUntilMs;
        private long lastSnapshotChangeMs;
        private long lastSnapshotLoadMs;
        private boolean loadPending;
        private final DataSource mediaPlaylistDataSource;
        private final Loader mediaPlaylistLoader;
        private IOException playlistError;
        private HlsMediaPlaylist playlistSnapshot;
        private final Uri playlistUrl;
        final /* synthetic */ DefaultHlsPlaylistTracker this$0;

        public MediaPlaylistBundle(DefaultHlsPlaylistTracker r3, Uri r4) {
            this.this$0 = r3;
            this.playlistUrl = r4;
            this.mediaPlaylistLoader = new Loader("DefaultHlsPlaylistTracker:MediaPlaylist");
            this.mediaPlaylistDataSource = DefaultHlsPlaylistTracker.access$700(r3).createDataSource(4);
        }

        static /* synthetic */ boolean access$000(MediaPlaylistBundle r1, long r2) {
            return r1.excludePlaylist(r2);
        }

        static /* synthetic */ void access$200(MediaPlaylistBundle r0, HlsMediaPlaylist r1, LoadEventInfo r2) {
            r0.processLoadedPlaylist(r1, r2);
        }

        static /* synthetic */ long access$300(MediaPlaylistBundle r2) {
            return r2.excludeUntilMs;
        }

        static /* synthetic */ Uri access$400(MediaPlaylistBundle r1) {
            return r1.playlistUrl;
        }

        static /* synthetic */ void access$500(MediaPlaylistBundle r0, Uri r1) {
            r0.loadPlaylistInternal(r1);
        }

        static /* synthetic */ HlsMediaPlaylist access$600(MediaPlaylistBundle r1) {
            return r1.playlistSnapshot;
        }

        private boolean excludePlaylist(long r3) {
            this.excludeUntilMs = SystemClock.elapsedRealtime() + r3;
            if (this.playlistUrl.equals(DefaultHlsPlaylistTracker.access$1700(this.this$0)) == true) goto L5;
        L7:
            return false;
        L5:
            if (DefaultHlsPlaylistTracker.access$1800(this.this$0) == true) goto L7;
            return true;
        }

        private Uri getMediaPlaylistUriForReload() {
            if (this.playlistSnapshot == null) goto L29;
            if (this.playlistSnapshot.serverControl.skipUntilUs == C0180C.TIME_UNSET) goto L7;
        L9:
            Uri.Builder r0 = this.playlistUrl.buildUpon();
            if (this.playlistSnapshot.serverControl.canBlockReload == false) goto L20;
            r0.appendQueryParameter(BLOCK_MSN_PARAM, String.valueOf(this.playlistSnapshot.mediaSequence + ((long) this.playlistSnapshot.segments.size())));
            if (this.playlistSnapshot.partTargetDurationUs == C0180C.TIME_UNSET) goto L20;
            List<HlsMediaPlaylist.Part> r1 = this.playlistSnapshot.trailingParts;
            int r6 = r1.size();
            if (r1.isEmpty() == false) goto L16;
        L18:
            r0.appendQueryParameter(BLOCK_PART_PARAM, String.valueOf(r6));
            goto L20
        L16:
            if (((HlsMediaPlaylist.Part) Iterables.getLast(r1)).isPreload == false) goto L18;
            r6 = r6 - 1;
        L20:
            if (this.playlistSnapshot.serverControl.skipUntilUs == C0180C.TIME_UNSET) goto L27;
            if (this.playlistSnapshot.serverControl.canSkipDateRanges == false) goto L24;
            String r2 = "v2";
        L25:
            r0.appendQueryParameter(SKIP_PARAM, r2);
            goto L27
        L24:
            r2 = "YES";
        L27:
            return r0.build();
        L7:
            if (this.playlistSnapshot.serverControl.canBlockReload == true) goto L9;
        L29:
            return this.playlistUrl;
        }

        private void loadPlaylistImmediately(Uri r11) {
            ParsingLoadable.Parser<HlsPlaylist> r0 = DefaultHlsPlaylistTracker.access$1300(this.this$0).createPlaylistParser(DefaultHlsPlaylistTracker.access$1200(this.this$0), this.playlistSnapshot);
            ParsingLoadable r1 = new ParsingLoadable(this.mediaPlaylistDataSource, r11, 4, r0);
            long r8 = this.mediaPlaylistLoader.startLoading(r1, this, DefaultHlsPlaylistTracker.access$900(this.this$0).getMinimumLoadableRetryCount(r1.type));
            DefaultHlsPlaylistTracker.access$800(this.this$0).loadStarted(new LoadEventInfo(r1.loadTaskId, r1.dataSpec, r8), r1.type);
        }

        private void loadPlaylistInternal(final Uri r7) {
            this.excludeUntilMs = 0;
            if (this.loadPending == false) goto L5;
            return;
        L5:
            if (this.mediaPlaylistLoader.isLoading() == false) goto L7;
            return;
        L7:
            if (this.mediaPlaylistLoader.hasFatalError() == true) goto L17;
            long r0 = SystemClock.elapsedRealtime();
            if (r0 >= this.earliestNextLoadTimeMs) goto L12;
            this.loadPending = true;
            DefaultHlsPlaylistTracker.access$1100(this.this$0).postDelayed(new RunnableC0299x8000b21b(this, r7), this.earliestNextLoadTimeMs - r0);
            return;
        L12:
            loadPlaylistImmediately(r7);
            return;
        }

        private void processLoadedPlaylist(HlsMediaPlaylist r12, LoadEventInfo r13) {
            HlsMediaPlaylist r0 = this.playlistSnapshot;
            long r1 = SystemClock.elapsedRealtime();
            this.lastSnapshotLoadMs = r1;
            this.playlistSnapshot = DefaultHlsPlaylistTracker.access$1400(this.this$0, r0, r12);
            if (this.playlistSnapshot == r0) goto L6;
            this.playlistError = null;
            this.lastSnapshotChangeMs = r1;
            DefaultHlsPlaylistTracker.access$1500(this.this$0, this.playlistUrl, this.playlistSnapshot);
        L15:
            long r3 = 0;
            if (this.playlistSnapshot.serverControl.canBlockReload == false) goto L18;
        L22:
            this.earliestNextLoadTimeMs = (Util.usToMs(r3) + r1) - r13.loadDurationMs;
            if (this.playlistSnapshot.hasEndTag == false) goto L25;
            return;
        L25:
            if (this.playlistUrl.equals(DefaultHlsPlaylistTracker.access$1700(this.this$0)) == false) goto L27;
        L28:
            loadPlaylistInternal(getMediaPlaylistUriForReload());
            return;
        L27:
            if (this.activeForPlayback == true) goto L28;
            return;
        L18:
            if (this.playlistSnapshot == r0) goto L20;
            long r5 = this.playlistSnapshot.targetDurationUs;
        L21:
            r3 = r5;
            goto L22
        L20:
            r5 = this.playlistSnapshot.targetDurationUs / 2;
            goto L21
        L6:
            if (this.playlistSnapshot.hasEndTag == true) goto L15;
            boolean r4 = false;
            IOException r6 = null;
            if ((r12.mediaSequence + ((long) r12.segments.size())) >= this.playlistSnapshot.mediaSequence) goto L11;
            r4 = true;
            r6 = new HlsPlaylistTracker.PlaylistResetException(this.playlistUrl);
        L13:
            if (r6 == null) goto L15;
            this.playlistError = r6;
            DefaultHlsPlaylistTracker.access$1000(this.this$0, this.playlistUrl, new LoadErrorHandlingPolicy.LoadErrorInfo(r13, new MediaLoadData(4), r6, 1), r4);
            goto L15
        L11:
            if ((r1 - this.lastSnapshotChangeMs) <= (Util.usToMs(this.playlistSnapshot.targetDurationUs) * DefaultHlsPlaylistTracker.access$1600(this.this$0))) goto L13;
            r6 = new HlsPlaylistTracker.PlaylistStuckException(this.playlistUrl);
            goto L13
        }

        public HlsMediaPlaylist getPlaylistSnapshot() {
            return this.playlistSnapshot;
        }

        public boolean isActiveForPlayback() {
            return this.activeForPlayback;
        }

        public boolean isSnapshotValid() {
            if (this.playlistSnapshot != null) goto L5;
            return false;
        L5:
            long r2 = SystemClock.elapsedRealtime();
            long r4 = Math.max(30000, Util.usToMs(this.playlistSnapshot.durationUs));
            if (this.playlistSnapshot.hasEndTag == true) goto L14;
            if (this.playlistSnapshot.playlistType == 2) goto L14;
            if (this.playlistSnapshot.playlistType == 1) goto L14;
            if ((this.lastSnapshotLoadMs + r4) > r2) goto L14;
            return false;
        L14:
            return true;
        }

        /* JADX INFO: renamed from: lambda$loadPlaylistInternal$0$androidx-media3-exoplayer-hls-playlist-DefaultHlsPlaylistTracker$MediaPlaylistBundle */
        /* synthetic */ void m109x7d3061bf(Uri r2) {
            this.loadPending = false;
            loadPlaylistImmediately(r2);
        }

        public void loadPlaylist(boolean r2) {
            if (r2 == false) goto L4;
            Uri r0 = getMediaPlaylistUriForReload();
        L5:
            loadPlaylistInternal(r0);
            return;
        L4:
            r0 = this.playlistUrl;
            goto L5
        }

        public void maybeThrowPlaylistRefreshError() throws IOException {
            this.mediaPlaylistLoader.maybeThrowError();
            if (this.playlistError != null) goto L6;
            return;
        L6:
            throw this.playlistError;
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public /* bridge */ /* synthetic */ void onLoadCanceled(Loader.Loadable r8, long r9, long r11, boolean r13) {
            onLoadCanceled((ParsingLoadable) r8, r9, r11, r13);
        }

        public void onLoadCanceled(ParsingLoadable<HlsPlaylist> r13, long r14, long r16, boolean r18) {
            LoadEventInfo r0 = new LoadEventInfo(r13.loadTaskId, r13.dataSpec, r13.getUri(), r13.getResponseHeaders(), r14, r16, r13.bytesLoaded());
            DefaultHlsPlaylistTracker.access$900(this.this$0).onLoadTaskConcluded(r13.loadTaskId);
            DefaultHlsPlaylistTracker.access$800(this.this$0).loadCanceled(r0, 4);
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public /* bridge */ /* synthetic */ void onLoadCompleted(Loader.Loadable r7, long r8, long r10) {
            onLoadCompleted((ParsingLoadable) r7, r8, r10);
        }

        public void onLoadCompleted(ParsingLoadable<HlsPlaylist> r14, long r15, long r17) {
            HlsPlaylist r0 = r14.getResult();
            LoadEventInfo r1 = new LoadEventInfo(r14.loadTaskId, r14.dataSpec, r14.getUri(), r14.getResponseHeaders(), r15, r17, r14.bytesLoaded());
            if ((r0 instanceof HlsMediaPlaylist) == false) goto L5;
            processLoadedPlaylist((HlsMediaPlaylist) r0, r1);
            DefaultHlsPlaylistTracker.access$800(this.this$0).loadCompleted(r1, 4);
        L6:
            DefaultHlsPlaylistTracker.access$900(this.this$0).onLoadTaskConcluded(r14.loadTaskId);
            return;
        L5:
            this.playlistError = ParserException.createForMalformedManifest("Loaded playlist has unexpected type.", null);
            DefaultHlsPlaylistTracker.access$800(this.this$0).loadError(r1, 4, this.playlistError, true);
            goto L6
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public /* bridge */ /* synthetic */ Loader.LoadErrorAction onLoadError(Loader.Loadable r9, long r10, long r12, IOException r14, int r15) {
            return onLoadError((ParsingLoadable) r9, r10, r12, r14, r15);
        }

        public Loader.LoadErrorAction onLoadError(ParsingLoadable<HlsPlaylist> r17, long r18, long r20, IOException r22, int r23) {
            LoadEventInfo r3 = new LoadEventInfo(r17.loadTaskId, r17.dataSpec, r17.getUri(), r17.getResponseHeaders(), r18, r20, r17.bytesLoaded());
            if (r17.getUri().getQueryParameter(BLOCK_MSN_PARAM) == null) goto L5;
            boolean r4 = true;
        L6:
            boolean r7 = r22 instanceof HlsPlaylistParser.DeltaUpdateException;
            if (r4 == true) goto L9;
            if (r7 == true) goto L9;
        L18:
            LoadErrorHandlingPolicy.LoadErrorInfo r9 = new LoadErrorHandlingPolicy.LoadErrorInfo(r3, new MediaLoadData(r17.type), r22, r23);
            if (DefaultHlsPlaylistTracker.access$1000(this.this$0, this.playlistUrl, r9, false) == false) goto L25;
            long r12 = DefaultHlsPlaylistTracker.access$900(this.this$0).getRetryDelayMsFor(r9);
            if (r12 == C0180C.TIME_UNSET) goto L23;
            Loader.LoadErrorAction r6 = Loader.createRetryAction(false, r12);
        L26:
            boolean r5 = true ^ r6.isRetry();
            DefaultHlsPlaylistTracker.access$800(this.this$0).loadError(r3, r17.type, r22, r5);
            if (r5 == false) goto L29;
            DefaultHlsPlaylistTracker.access$900(this.this$0).onLoadTaskConcluded(r17.loadTaskId);
        L29:
            return r6;
        L23:
            r6 = Loader.DONT_RETRY_FATAL;
            goto L26
        L25:
            r6 = Loader.DONT_RETRY;
        L9:
            int r8 = Integer.MAX_VALUE;
            if ((r22 instanceof HttpDataSource.InvalidResponseCodeException) == false) goto L12;
            r8 = ((HttpDataSource.InvalidResponseCodeException) r22).responseCode;
        L12:
            if (r7 == true) goto L31;
            if (r8 == 400) goto L31;
            if (r8 != 503) goto L18;
        L31:
            this.earliestNextLoadTimeMs = SystemClock.elapsedRealtime();
            loadPlaylist(false);
            ((MediaSourceEventListener.EventDispatcher) Util.castNonNull(DefaultHlsPlaylistTracker.access$800(this.this$0))).loadError(r3, r17.type, r22, true);
            return Loader.DONT_RETRY;
        L5:
            r4 = false;
            goto L6
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public /* synthetic */ void onLoadStarted(Loader.Loadable r1, long r2, long r4, int r6) {
            Loader.Callback.CC.$default$onLoadStarted(this, r1, r2, r4, r6);
        }

        public void release() {
            this.mediaPlaylistLoader.release();
        }

        public void setActiveForPlayback(boolean r1) {
            this.activeForPlayback = r1;
        }
    }

    static {
        FACTORY = new DefaultHlsPlaylistTracker$$ExternalSyntheticLambda0();
    }

    public DefaultHlsPlaylistTracker(HlsDataSourceFactory r7, LoadErrorHandlingPolicy r8, HlsPlaylistParserFactory r9) {
        this(r7, r8, r9, 3.5d);
    }

    public DefaultHlsPlaylistTracker(HlsDataSourceFactory r3, LoadErrorHandlingPolicy r4, HlsPlaylistParserFactory r5, double r6) {
        this.dataSourceFactory = r3;
        this.playlistParserFactory = r5;
        this.loadErrorHandlingPolicy = r4;
        this.playlistStuckTargetDurationCoefficient = r6;
        this.listeners = new CopyOnWriteArrayList();
        this.playlistBundles = new HashMap();
        this.initialStartTimeUs = C0180C.TIME_UNSET;
    }

    static /* synthetic */ boolean access$1000(DefaultHlsPlaylistTracker r1, Uri r2, LoadErrorHandlingPolicy.LoadErrorInfo r3, boolean r4) {
        return r1.notifyPlaylistError(r2, r3, r4);
    }

    static /* synthetic */ Handler access$1100(DefaultHlsPlaylistTracker r1) {
        return r1.playlistRefreshHandler;
    }

    static /* synthetic */ HlsMultivariantPlaylist access$1200(DefaultHlsPlaylistTracker r1) {
        return r1.multivariantPlaylist;
    }

    static /* synthetic */ HlsPlaylistParserFactory access$1300(DefaultHlsPlaylistTracker r1) {
        return r1.playlistParserFactory;
    }

    static /* synthetic */ HlsMediaPlaylist access$1400(DefaultHlsPlaylistTracker r1, HlsMediaPlaylist r2, HlsMediaPlaylist r3) {
        return r1.getLatestPlaylistSnapshot(r2, r3);
    }

    static /* synthetic */ void access$1500(DefaultHlsPlaylistTracker r0, Uri r1, HlsMediaPlaylist r2) {
        r0.onPlaylistUpdated(r1, r2);
    }

    static /* synthetic */ double access$1600(DefaultHlsPlaylistTracker r2) {
        return r2.playlistStuckTargetDurationCoefficient;
    }

    static /* synthetic */ Uri access$1700(DefaultHlsPlaylistTracker r1) {
        return r1.primaryMediaPlaylistUrl;
    }

    static /* synthetic */ boolean access$1800(DefaultHlsPlaylistTracker r1) {
        return r1.maybeSelectNewPrimaryUrl();
    }

    static /* synthetic */ CopyOnWriteArrayList access$1900(DefaultHlsPlaylistTracker r1) {
        return r1.listeners;
    }

    static /* synthetic */ HlsMediaPlaylist access$2000(DefaultHlsPlaylistTracker r1) {
        return r1.primaryMediaPlaylistSnapshot;
    }

    static /* synthetic */ HashMap access$2100(DefaultHlsPlaylistTracker r1) {
        return r1.playlistBundles;
    }

    static /* synthetic */ HlsDataSourceFactory access$700(DefaultHlsPlaylistTracker r1) {
        return r1.dataSourceFactory;
    }

    static /* synthetic */ MediaSourceEventListener.EventDispatcher access$800(DefaultHlsPlaylistTracker r1) {
        return r1.eventDispatcher;
    }

    static /* synthetic */ LoadErrorHandlingPolicy access$900(DefaultHlsPlaylistTracker r1) {
        return r1.loadErrorHandlingPolicy;
    }

    private void createBundles(List<Uri> r6) {
        int r0 = r6.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        Uri r2 = r6.get(r1);
        MediaPlaylistBundle r3 = new MediaPlaylistBundle(this, r2);
        this.playlistBundles.put(r2, r3);
        r1 = r1 + 1;
        goto L3
    }

    private static HlsMediaPlaylist.Segment getFirstOldOverlappingSegment(HlsMediaPlaylist r4, HlsMediaPlaylist r5) {
        int r0 = (int) (r5.mediaSequence - r4.mediaSequence);
        List<HlsMediaPlaylist.Segment> r1 = r4.segments;
        if (r0 < r1.size()) goto L7;
        return null;
    L7:
        return r1.get(r0);
    }

    private HlsMediaPlaylist getLatestPlaylistSnapshot(HlsMediaPlaylist r5, HlsMediaPlaylist r6) {
        if (r6.isNewerThan(r5) == true) goto L10;
        if (r6.hasEndTag == true) goto L7;
        return r5;
    L7:
        return r5.copyWithEndTag();
    L10:
        return r6.copyWith(getLoadedPlaylistStartTimeUs(r5, r6), getLoadedPlaylistDiscontinuitySequence(r5, r6));
    }

    private int getLoadedPlaylistDiscontinuitySequence(HlsMediaPlaylist r6, HlsMediaPlaylist r7) {
        if (r7.hasDiscontinuitySequence == false) goto L7;
        return r7.discontinuitySequence;
    L7:
        if (this.primaryMediaPlaylistSnapshot == null) goto L9;
        int r0 = this.primaryMediaPlaylistSnapshot.discontinuitySequence;
    L11:
        if (r6 != null) goto L13;
        return r0;
    L13:
        HlsMediaPlaylist.Segment r2 = getFirstOldOverlappingSegment(r6, r7);
        if (r2 != null) goto L16;
        return r0;
    L16:
        return (r6.discontinuitySequence + r2.relativeDiscontinuitySequence) - r7.segments.get(0).relativeDiscontinuitySequence;
    L9:
        r0 = 0;
        goto L11
    }

    private long getLoadedPlaylistStartTimeUs(HlsMediaPlaylist r11, HlsMediaPlaylist r12) {
        if (r12.hasProgramDateTime == false) goto L7;
        return r12.startTimeUs;
    L7:
        if (this.primaryMediaPlaylistSnapshot == null) goto L9;
        long r0 = this.primaryMediaPlaylistSnapshot.startTimeUs;
    L10:
        if (r11 != null) goto L12;
        return r0;
    L12:
        int r2 = r11.segments.size();
        HlsMediaPlaylist.Segment r3 = getFirstOldOverlappingSegment(r11, r12);
        if (r3 == null) goto L17;
        return r11.startTimeUs + r3.relativeStartTimeUs;
    L17:
        if (r2 == (r12.mediaSequence - r11.mediaSequence)) goto L19;
        return r0;
    L19:
        return r11.getEndTimeUs();
    L9:
        r0 = 0;
        goto L10
    }

    private Uri getRequestUriForPrimaryChange(Uri r5) {
        if (this.primaryMediaPlaylistSnapshot != null) goto L5;
    L13:
        return r5;
    L5:
        if (this.primaryMediaPlaylistSnapshot.serverControl.canBlockReload == false) goto L13;
        HlsMediaPlaylist.RenditionReport r0 = this.primaryMediaPlaylistSnapshot.renditionReports.get(r5);
        if (r0 == null) goto L13;
        Uri.Builder r1 = r5.buildUpon();
        r1.appendQueryParameter("_HLS_msn", String.valueOf(r0.lastMediaSequence));
        if (r0.lastPartIndex == (-1)) goto L12;
        r1.appendQueryParameter("_HLS_part", String.valueOf(r0.lastPartIndex));
    L12:
        return r1.build();
    }

    private boolean isVariantUrl(Uri r4) {
        List<HlsMultivariantPlaylist.Variant> r0 = this.multivariantPlaylist.variants;
        int r1 = 0;
    L4:
        if (r1 >= r0.size()) goto L10;
        if (r4.equals(r0.get(r1).url) == true) goto L7;
        r1 = r1 + 1;
        goto L4
    L7:
        return true;
    L10:
        return false;
    }

    private void maybeActivateForPlayback(Uri r5) {
        MediaPlaylistBundle r0 = this.playlistBundles.get(r5);
        HlsMediaPlaylist r1 = r0.getPlaylistSnapshot();
        if (r0.isActiveForPlayback() == false) goto L5;
        return;
    L5:
        r0.setActiveForPlayback(true);
        if (r1 != null) goto L8;
        return;
    L8:
        if (r1.hasEndTag == true) goto L12;
        r0.loadPlaylist(true);
        return;
    }

    private boolean maybeSelectNewPrimaryUrl() {
        List<HlsMultivariantPlaylist.Variant> r0 = this.multivariantPlaylist.variants;
        int r1 = r0.size();
        long r2 = SystemClock.elapsedRealtime();
        int r4 = 0;
    L3:
        if (r4 >= r1) goto L9;
        MediaPlaylistBundle r5 = (MediaPlaylistBundle) Assertions.checkNotNull(this.playlistBundles.get(r0.get(r4).url));
        if (r2 > MediaPlaylistBundle.access$300(r5)) goto L6;
        r4 = r4 + 1;
        goto L3
    L6:
        this.primaryMediaPlaylistUrl = MediaPlaylistBundle.access$400(r5);
        MediaPlaylistBundle.access$500(r5, getRequestUriForPrimaryChange(this.primaryMediaPlaylistUrl));
        return true;
    L9:
        return false;
    }

    private void maybeSetPrimaryUrl(Uri r4) {
        if (r4.equals(this.primaryMediaPlaylistUrl) == false) goto L5;
        return;
    L5:
        if (isVariantUrl(r4) == true) goto L7;
        return;
    L7:
        if (this.primaryMediaPlaylistSnapshot != null) goto L9;
    L11:
        this.primaryMediaPlaylistUrl = r4;
        MediaPlaylistBundle r0 = this.playlistBundles.get(this.primaryMediaPlaylistUrl);
        HlsMediaPlaylist r1 = MediaPlaylistBundle.access$600(r0);
        if (r1 != null) goto L14;
    L16:
        MediaPlaylistBundle.access$500(r0, getRequestUriForPrimaryChange(r4));
        return;
    L14:
        if (r1.hasEndTag == false) goto L16;
        this.primaryMediaPlaylistSnapshot = r1;
        this.primaryPlaylistListener.onPrimaryPlaylistRefreshed(r1);
        return;
    L9:
        if (this.primaryMediaPlaylistSnapshot.hasEndTag == false) goto L11;
    }

    private boolean notifyPlaylistError(Uri r5, LoadErrorHandlingPolicy.LoadErrorInfo r6, boolean r7) {
        boolean r0 = false;
        Iterator<HlsPlaylistTracker.PlaylistEventListener> r1 = this.listeners.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0 = r0 | (!r1.next().onPlaylistError(r5, r6, r7));
        goto L4
    L6:
        return r0;
    }

    private void onPlaylistUpdated(Uri r3, HlsMediaPlaylist r4) {
        if (r3.equals(this.primaryMediaPlaylistUrl) == true) goto L5;
    L8:
        Iterator<HlsPlaylistTracker.PlaylistEventListener> r0 = this.listeners.iterator();
    L10:
        if (r0.hasNext() == false) goto L12;
        r0.next().onPlaylistChanged();
        goto L10
    L12:
        return;
    L5:
        if (this.primaryMediaPlaylistSnapshot != null) goto L7;
        this.isLive = !r4.hasEndTag;
        this.initialStartTimeUs = r4.startTimeUs;
    L7:
        this.primaryMediaPlaylistSnapshot = r4;
        this.primaryPlaylistListener.onPrimaryPlaylistRefreshed(r4);
        goto L8
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker
    public void addListener(HlsPlaylistTracker.PlaylistEventListener r2) {
        Assertions.checkNotNull(r2);
        this.listeners.add(r2);
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker
    public void deactivatePlaylistForPlayback(Uri r3) {
        MediaPlaylistBundle r0 = this.playlistBundles.get(r3);
        if (r0 == null) goto L6;
        r0.setActiveForPlayback(false);
        return;
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker
    public boolean excludeMediaPlaylist(Uri r3, long r4) {
        MediaPlaylistBundle r0 = this.playlistBundles.get(r3);
        if (r0 != null) goto L5;
        return false;
    L5:
        return !MediaPlaylistBundle.access$000(r0, r4);
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker
    public long getInitialStartTimeUs() {
        return this.initialStartTimeUs;
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker
    public HlsMultivariantPlaylist getMultivariantPlaylist() {
        return this.multivariantPlaylist;
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker
    public HlsMediaPlaylist getPlaylistSnapshot(Uri r3, boolean r4) {
        HlsMediaPlaylist r1 = this.playlistBundles.get(r3).getPlaylistSnapshot();
        if (r1 == null) goto L6;
        if (r4 == false) goto L6;
        maybeSetPrimaryUrl(r3);
        maybeActivateForPlayback(r3);
    L6:
        return r1;
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker
    public boolean isLive() {
        return this.isLive;
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker
    public boolean isSnapshotValid(Uri r2) {
        return this.playlistBundles.get(r2).isSnapshotValid();
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker
    public void maybeThrowPlaylistRefreshError(Uri r2) throws IOException {
        this.playlistBundles.get(r2).maybeThrowPlaylistRefreshError();
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker
    public void maybeThrowPrimaryPlaylistRefreshError() throws IOException {
        if (this.initialPlaylistLoader == null) goto L6;
        this.initialPlaylistLoader.maybeThrowError();
    L6:
        if (this.primaryMediaPlaylistUrl == null) goto L9;
        maybeThrowPlaylistRefreshError(this.primaryMediaPlaylistUrl);
        return;
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ void onLoadCanceled(Loader.Loadable r8, long r9, long r11, boolean r13) {
        onLoadCanceled((ParsingLoadable) r8, r9, r11, r13);
    }

    public void onLoadCanceled(ParsingLoadable<HlsPlaylist> r13, long r14, long r16, boolean r18) {
        LoadEventInfo r0 = new LoadEventInfo(r13.loadTaskId, r13.dataSpec, r13.getUri(), r13.getResponseHeaders(), r14, r16, r13.bytesLoaded());
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r13.loadTaskId);
        this.eventDispatcher.loadCanceled(r0, 4);
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ void onLoadCompleted(Loader.Loadable r7, long r8, long r10) {
        onLoadCompleted((ParsingLoadable) r7, r8, r10);
    }

    public void onLoadCompleted(ParsingLoadable<HlsPlaylist> r20, long r21, long r23) {
        HlsPlaylist r2 = r20.getResult();
        boolean r3 = r2 instanceof HlsMediaPlaylist;
        if (r3 == false) goto L5;
        HlsMultivariantPlaylist r4 = HlsMultivariantPlaylist.createSingleVariantMultivariantPlaylist(r2.baseUri);
    L6:
        this.multivariantPlaylist = r4;
        this.primaryMediaPlaylistUrl = r4.variants.get(0).url;
        this.listeners.add(new FirstPrimaryMediaPlaylistListener(this, null));
        createBundles(r4.mediaPlaylistUrls);
        LoadEventInfo r7 = new LoadEventInfo(r20.loadTaskId, r20.dataSpec, r20.getUri(), r20.getResponseHeaders(), r21, r23, r20.bytesLoaded());
        MediaPlaylistBundle r5 = this.playlistBundles.get(this.primaryMediaPlaylistUrl);
        if (r3 == false) goto L9;
        MediaPlaylistBundle.access$200(r5, (HlsMediaPlaylist) r2, r7);
    L10:
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r20.loadTaskId);
        this.eventDispatcher.loadCompleted(r7, 4);
        return;
    L9:
        r5.loadPlaylist(false);
        goto L10
    L5:
        r4 = (HlsMultivariantPlaylist) r2;
        goto L6
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ Loader.LoadErrorAction onLoadError(Loader.Loadable r9, long r10, long r12, IOException r14, int r15) {
        return onLoadError((ParsingLoadable) r9, r10, r12, r14, r15);
    }

    public Loader.LoadErrorAction onLoadError(ParsingLoadable<HlsPlaylist> r14, long r15, long r17, IOException r19, int r20) {
        LoadEventInfo r1 = new LoadEventInfo(r14.loadTaskId, r14.dataSpec, r14.getUri(), r14.getResponseHeaders(), r15, r17, r14.bytesLoaded());
        long r3 = this.loadErrorHandlingPolicy.getRetryDelayMsFor(new LoadErrorHandlingPolicy.LoadErrorInfo(r1, new MediaLoadData(r14.type), r19, r20));
        if (r3 != C0180C.TIME_UNSET) goto L5;
        boolean r6 = true;
    L6:
        this.eventDispatcher.loadError(r1, r14.type, r19, r6);
        if (r6 == false) goto L9;
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r14.loadTaskId);
    L9:
        if (r6 == false) goto L12;
        return Loader.DONT_RETRY_FATAL;
    L12:
        return Loader.createRetryAction(false, r3);
    L5:
        r6 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* synthetic */ void onLoadStarted(Loader.Loadable r1, long r2, long r4, int r6) {
        Loader.Callback.CC.$default$onLoadStarted(this, r1, r2, r4, r6);
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker
    public void refreshPlaylist(Uri r3) {
        this.playlistBundles.get(r3).loadPlaylist(true);
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker
    public void removeListener(HlsPlaylistTracker.PlaylistEventListener r2) {
        this.listeners.remove(r2);
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker
    public void start(Uri r10, MediaSourceEventListener.EventDispatcher r11, HlsPlaylistTracker.PrimaryPlaylistListener r12) {
        this.playlistRefreshHandler = Util.createHandlerForCurrentLooper();
        this.eventDispatcher = r11;
        this.primaryPlaylistListener = r12;
        ParsingLoadable r0 = new ParsingLoadable(this.dataSourceFactory.createDataSource(4), r10, 4, this.playlistParserFactory.createPlaylistParser());
        if (this.initialPlaylistLoader != null) goto L5;
        boolean r1 = true;
    L6:
        Assertions.checkState(r1);
        this.initialPlaylistLoader = new Loader("DefaultHlsPlaylistTracker:MultivariantPlaylist");
        long r7 = this.initialPlaylistLoader.startLoading(r0, this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(r0.type));
        r11.loadStarted(new LoadEventInfo(r0.loadTaskId, r0.dataSpec, r7), r0.type);
        return;
    L5:
        r1 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker
    public void stop() {
        this.primaryMediaPlaylistUrl = null;
        this.primaryMediaPlaylistSnapshot = null;
        this.multivariantPlaylist = null;
        this.initialStartTimeUs = C0180C.TIME_UNSET;
        this.initialPlaylistLoader.release();
        this.initialPlaylistLoader = null;
        Iterator<MediaPlaylistBundle> r1 = this.playlistBundles.values().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r1.next().release();
        goto L4
    L6:
        this.playlistRefreshHandler.removeCallbacksAndMessages(null);
        this.playlistRefreshHandler = null;
        this.playlistBundles.clear();
    }
}
