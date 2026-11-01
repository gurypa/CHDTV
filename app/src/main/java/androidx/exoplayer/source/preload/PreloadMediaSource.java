package androidx.media3.exoplayer.source.preload;

import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.SequenceableLoader;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.source.WrappingMediaSource;
import androidx.media3.exoplayer.trackselection.TrackSelector;
import androidx.media3.exoplayer.trackselection.TrackSelectorResult;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.extractor.text.SubtitleParser;
import java.io.IOException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class PreloadMediaSource extends WrappingMediaSource {
    private static final long CHECK_FOR_PRELOAD_ERROR_INTERVAL_MS = 100;
    private static final String TAG = "PreloadMediaSource";
    private final Allocator allocator;
    private final BandwidthMeter bandwidthMeter;
    private boolean onSourcePreparedNotified;
    private boolean onUsedByPlayerNotified;
    private Pair<PreloadMediaPeriod, MediaSource.MediaPeriodId> playingPreloadedMediaPeriodAndId;
    private boolean preloadCalled;
    private final PreloadControl preloadControl;
    private final Handler preloadHandler;
    private Pair<PreloadMediaPeriod, MediaPeriodKey> preloadingMediaPeriodAndKey;
    private boolean prepareChildSourceCalled;
    private final Handler releaseHandler;
    private final RendererCapabilities[] rendererCapabilities;
    private long startPositionUs;
    private Timeline timeline;
    private final TrackSelector trackSelector;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.preload.PreloadMediaSource$1 */
    static /* synthetic */ class C03591 {
    }

    public static final class Factory implements MediaSource.Factory {
        private final Allocator allocator;
        private final BandwidthMeter bandwidthMeter;
        private final MediaSource.Factory mediaSourceFactory;
        private final PreloadControl preloadControl;
        private final Looper preloadLooper;
        private final RendererCapabilities[] rendererCapabilities;
        private final TrackSelector trackSelector;

        public Factory(MediaSource.Factory r2, PreloadControl r3, TrackSelector r4, BandwidthMeter r5, RendererCapabilities[] r6, Allocator r7, Looper r8) {
            this.mediaSourceFactory = r2;
            this.preloadControl = r3;
            this.trackSelector = r4;
            this.bandwidthMeter = r5;
            this.rendererCapabilities = (RendererCapabilities[]) Arrays.copyOf(r6, r6.length);
            this.allocator = r7;
            this.preloadLooper = r8;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource createMediaSource(MediaItem r1) {
            return createMediaSource(r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public PreloadMediaSource createMediaSource(MediaItem r10) {
            return new PreloadMediaSource(this.mediaSourceFactory.createMediaSource(r10), this.preloadControl, this.trackSelector, this.bandwidthMeter, this.rendererCapabilities, this.allocator, this.preloadLooper, null);
        }

        public PreloadMediaSource createMediaSource(MediaSource r10) {
            return new PreloadMediaSource(r10, this.preloadControl, this.trackSelector, this.bandwidthMeter, this.rendererCapabilities, this.allocator, this.preloadLooper, null);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* synthetic */ MediaSource.Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
            return MediaSource.Factory.CC.$default$experimentalParseSubtitlesDuringExtraction(this, r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public int[] getSupportedTypes() {
            return this.mediaSourceFactory.getSupportedTypes();
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource.Factory setCmcdConfigurationFactory(CmcdConfiguration.Factory r1) {
            return setCmcdConfigurationFactory(r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public Factory setCmcdConfigurationFactory(CmcdConfiguration.Factory r2) {
            this.mediaSourceFactory.setCmcdConfigurationFactory(r2);
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource.Factory setDrmSessionManagerProvider(DrmSessionManagerProvider r1) {
            return setDrmSessionManagerProvider(r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public Factory setDrmSessionManagerProvider(DrmSessionManagerProvider r2) {
            this.mediaSourceFactory.setDrmSessionManagerProvider(r2);
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource.Factory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r1) {
            return setLoadErrorHandlingPolicy(r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public Factory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r2) {
            this.mediaSourceFactory.setLoadErrorHandlingPolicy(r2);
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* synthetic */ MediaSource.Factory setSubtitleParserFactory(SubtitleParser.Factory r1) {
            return MediaSource.Factory.CC.$default$setSubtitleParserFactory(this, r1);
        }
    }

    private static class MediaPeriodKey {
        public final MediaSource.MediaPeriodId mediaPeriodId;
        private final Long startPositionUs;

        public MediaPeriodKey(MediaSource.MediaPeriodId r2, long r3) {
            this.mediaPeriodId = r2;
            this.startPositionUs = Long.valueOf(r3);
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if ((r6 instanceof MediaPeriodKey) == true) goto L8;
            return false;
        L8:
            MediaPeriodKey r1 = (MediaPeriodKey) r6;
            if (PreloadMediaSource.access$900(this.mediaPeriodId, r1.mediaPeriodId) == false) goto L14;
            if (this.startPositionUs.equals(r1.startPositionUs) == false) goto L14;
            return true;
        L14:
            return false;
        }

        public int hashCode() {
            return (((((((((17 * 31) + this.mediaPeriodId.periodUid.hashCode()) * 31) + this.mediaPeriodId.adGroupIndex) * 31) + this.mediaPeriodId.adIndexInAdGroup) * 31) + this.mediaPeriodId.nextAdGroupIndex) * 31) + this.startPositionUs.intValue();
        }
    }

    public interface PreloadControl {

        /* JADX INFO: renamed from: androidx.media3.exoplayer.source.preload.PreloadMediaSource$PreloadControl$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static void $default$onLoadedToTheEndOfSource(PreloadControl r0, PreloadMediaSource r1) {
            }
        }

        boolean onContinueLoadingRequested(PreloadMediaSource r1, long r2);

        void onLoadedToTheEndOfSource(PreloadMediaSource r1);

        void onPreloadError(PreloadException r1, PreloadMediaSource r2);

        boolean onSourcePrepared(PreloadMediaSource r1);

        boolean onTracksSelected(PreloadMediaSource r1);

        void onUsedByPlayer(PreloadMediaSource r1);
    }

    private class PreloadMediaPeriodCallback implements MediaPeriod.Callback {
        private final long periodStartPositionUs;
        private boolean prepared;
        final /* synthetic */ PreloadMediaSource this$0;

        public PreloadMediaPeriodCallback(PreloadMediaSource r1, long r2) {
            this.this$0 = r1;
            this.periodStartPositionUs = r2;
        }

        /* JADX INFO: renamed from: lambda$onContinueLoadingRequested$1$androidx-media3-exoplayer-source-preload-PreloadMediaSource$PreloadMediaPeriodCallback */
        /* synthetic */ void m145xba37e565(MediaPeriod r8) {
            if (PreloadMediaSource.access$200(this.this$0) == false) goto L5;
            return;
        L5:
            PreloadMediaPeriod r0 = (PreloadMediaPeriod) r8;
            long r1 = r8.getBufferedPositionUs();
            if (this.prepared == false) goto L12;
            if (r1 != Long.MIN_VALUE) goto L12;
            PreloadMediaSource.access$300(this.this$0).onLoadedToTheEndOfSource(this.this$0);
            PreloadMediaSource.access$400(this.this$0);
            return;
        L12:
            if (this.prepared == true) goto L14;
        L17:
            r0.continueLoading(new LoadingInfo.Builder().setPlaybackPositionUs(this.periodStartPositionUs).build());
            return;
        L14:
            if (PreloadMediaSource.access$300(this.this$0).onContinueLoadingRequested(this.this$0, r1 - this.periodStartPositionUs) == true) goto L17;
            PreloadMediaSource.access$400(this.this$0);
        }

        /* JADX INFO: renamed from: lambda$onPrepared$0$androidx-media3-exoplayer-source-preload-PreloadMediaSource$PreloadMediaPeriodCallback */
        /* synthetic */ void m146x5a2a8722(MediaPeriod r9) {
            if (PreloadMediaSource.access$200(this.this$0) == false) goto L5;
            return;
        L5:
            PreloadMediaPeriod r0 = (PreloadMediaPeriod) r9;
            TrackGroupArray r1 = r0.getTrackGroups();
            TrackSelectorResult r2 = null;
            MediaPeriodKey r3 = (MediaPeriodKey) ((Pair) Assertions.checkNotNull(PreloadMediaSource.access$500(this.this$0))).second;
            r2 = PreloadMediaSource.access$800(this.this$0).selectTracks(PreloadMediaSource.access$600(this.this$0), r1, r3.mediaPeriodId, (Timeline) Assertions.checkNotNull(PreloadMediaSource.access$700(this.this$0)));     // Catch: ExoPlaybackException -> L8
        L10:
            if (r2 != null) goto L13;
            PreloadMediaSource.access$400(this.this$0);
            return;
        L13:
            r0.selectTracksForPreloading(r2.selections, this.periodStartPositionUs);
            if (PreloadMediaSource.access$300(this.this$0).onTracksSelected(this.this$0) == true) goto L17;
            PreloadMediaSource.access$400(this.this$0);
            return;
        L17:
            r0.continueLoading(new LoadingInfo.Builder().setPlaybackPositionUs(this.periodStartPositionUs).build());
            return;
        L8:
            e = move-exception;
            Log.m45e(PreloadMediaSource.TAG, "Failed to select tracks", e);
            goto L10
        }

        public void onContinueLoadingRequested(final MediaPeriod r3) {
            PreloadMediaSource.access$100(this.this$0).post(new RunnableC0361x8c2037f(this, r3));
        }

        @Override // androidx.media3.exoplayer.source.SequenceableLoader.Callback
        public /* bridge */ /* synthetic */ void onContinueLoadingRequested(SequenceableLoader r1) {
            onContinueLoadingRequested((MediaPeriod) r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod.Callback
        public void onPrepared(final MediaPeriod r3) {
            this.prepared = true;
            PreloadMediaSource.access$100(this.this$0).post(new RunnableC0360x8c2037e(this, r3));
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$PFEfUw-OjJlAGDPg8YCJvEWz75k, reason: not valid java name */
    public static /* synthetic */ void m480$r8$lambda$PFEfUwOjJlAGDPg8YCJvEWz75k(PreloadMediaSource r0) {
        r0.checkForPreloadError();
    }

    private PreloadMediaSource(MediaSource r3, PreloadControl r4, TrackSelector r5, BandwidthMeter r6, RendererCapabilities[] r7, Allocator r8, Looper r9) {
        super(r3);
        this.preloadControl = r4;
        this.trackSelector = r5;
        this.bandwidthMeter = r6;
        this.rendererCapabilities = r7;
        this.allocator = r8;
        this.preloadHandler = Util.createHandler(r9, null);
        this.releaseHandler = Util.createHandler(r9, null);
        this.startPositionUs = C0180C.TIME_UNSET;
    }

    /* synthetic */ PreloadMediaSource(MediaSource r1, PreloadControl r2, TrackSelector r3, BandwidthMeter r4, RendererCapabilities[] r5, Allocator r6, Looper r7, C03591 r8) {
        this(r1, r2, r3, r4, r5, r6, r7);
    }

    static /* synthetic */ Handler access$100(PreloadMediaSource r1) {
        return r1.preloadHandler;
    }

    static /* synthetic */ boolean access$200(PreloadMediaSource r1) {
        return r1.isUsedByPlayer();
    }

    static /* synthetic */ PreloadControl access$300(PreloadMediaSource r1) {
        return r1.preloadControl;
    }

    static /* synthetic */ void access$400(PreloadMediaSource r0) {
        r0.stopPreloading();
    }

    static /* synthetic */ Pair access$500(PreloadMediaSource r1) {
        return r1.preloadingMediaPeriodAndKey;
    }

    static /* synthetic */ RendererCapabilities[] access$600(PreloadMediaSource r1) {
        return r1.rendererCapabilities;
    }

    static /* synthetic */ Timeline access$700(PreloadMediaSource r1) {
        return r1.timeline;
    }

    static /* synthetic */ TrackSelector access$800(PreloadMediaSource r1) {
        return r1.trackSelector;
    }

    static /* synthetic */ boolean access$900(MediaSource.MediaPeriodId r1, MediaSource.MediaPeriodId r2) {
        return mediaPeriodIdEqualsWithoutWindowSequenceNumber(r1, r2);
    }

    private void checkForPreloadError() {
        maybeThrowSourceInfoRefreshError();     // Catch: IOException -> L10
        if (this.preloadingMediaPeriodAndKey == null) goto L8;
        PreloadMediaPeriod r0 = (PreloadMediaPeriod) this.preloadingMediaPeriodAndKey.first;     // Catch: IOException -> L10
        if (r0.prepared == true) goto L7;
        r0.maybeThrowPrepareError();     // Catch: IOException -> L10
        goto L8
    L7:
        r0.maybeThrowStreamError();     // Catch: IOException -> L10
    L8:
        this.preloadHandler.postDelayed(new PreloadMediaSource$$ExternalSyntheticLambda2(this), CHECK_FOR_PRELOAD_ERROR_INTERVAL_MS);     // Catch: IOException -> L10
        return;
    L10:
        e = move-exception;
        this.preloadControl.onPreloadError(new PreloadException(getMediaItem(), null, e), this);
        stopPreloading();
    }

    private boolean isUsedByPlayer() {
        return prepareSourceCalled();
    }

    private static boolean mediaPeriodIdEqualsWithoutWindowSequenceNumber(MediaSource.MediaPeriodId r2, MediaSource.MediaPeriodId r3) {
        if (r2.periodUid.equals(r3.periodUid) == true) goto L5;
    L11:
        return false;
    L5:
        if (r2.adGroupIndex != r3.adGroupIndex) goto L11;
        if (r2.adIndexInAdGroup != r3.adIndexInAdGroup) goto L11;
        if (r2.nextAdGroupIndex != r3.nextAdGroupIndex) goto L11;
        return true;
    }

    private void onUsedByPlayer() {
        this.preloadControl.onUsedByPlayer(this);
        stopPreloading();
        this.onUsedByPlayerNotified = true;
    }

    private void stopPreloading() {
        this.preloadHandler.removeCallbacksAndMessages(null);
    }

    public void clear() {
        Util.postOrRun(this.preloadHandler, new PreloadMediaSource$$ExternalSyntheticLambda1(this));
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.MediaSource
    public /* bridge */ /* synthetic */ MediaPeriod createPeriod(MediaSource.MediaPeriodId r1, Allocator r2, long r3) {
        return createPeriod(r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.MediaSource
    public PreloadMediaPeriod createPeriod(MediaSource.MediaPeriodId r5, Allocator r6, long r7) {
        MediaPeriodKey r0 = new MediaPeriodKey(r5, r7);
        if (this.preloadingMediaPeriodAndKey == null) goto L11;
        if (r0.equals(this.preloadingMediaPeriodAndKey.second) == false) goto L11;
        PreloadMediaPeriod r1 = (PreloadMediaPeriod) ((Pair) Assertions.checkNotNull(this.preloadingMediaPeriodAndKey)).first;
        if (isUsedByPlayer() == false) goto L9;
        this.preloadingMediaPeriodAndKey = null;
        this.playingPreloadedMediaPeriodAndId = new Pair(r1, r5);
    L9:
        return r1;
    L11:
        if (this.preloadingMediaPeriodAndKey == null) goto L13;
        this.mediaSource.releasePeriod(((PreloadMediaPeriod) ((Pair) Assertions.checkNotNull(this.preloadingMediaPeriodAndKey)).first).mediaPeriod);
        this.preloadingMediaPeriodAndKey = null;
    L13:
        PreloadMediaPeriod r2 = new PreloadMediaPeriod(this.mediaSource.createPeriod(r5, r6, r7));
        if (isUsedByPlayer() == true) goto L16;
        this.preloadingMediaPeriodAndKey = new Pair(r2, r0);
    L16:
        return r2;
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource
    protected MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(MediaSource.MediaPeriodId r2) {
        if (this.playingPreloadedMediaPeriodAndId != null) goto L5;
    L8:
        return r2;
    L5:
        if (mediaPeriodIdEqualsWithoutWindowSequenceNumber(r2, (MediaSource.MediaPeriodId) ((Pair) Assertions.checkNotNull(this.playingPreloadedMediaPeriodAndId)).second) == false) goto L8;
        return (MediaSource.MediaPeriodId) ((Pair) Assertions.checkNotNull(this.playingPreloadedMediaPeriodAndId)).second;
    }

    /* JADX INFO: renamed from: lambda$clear$1$androidx-media3-exoplayer-source-preload-PreloadMediaSource */
    /* synthetic */ void m141xcf4780db() {
        if (this.preloadingMediaPeriodAndKey == null) goto L6;
        this.mediaSource.releasePeriod(((PreloadMediaPeriod) this.preloadingMediaPeriodAndKey.first).mediaPeriod);
        this.preloadingMediaPeriodAndKey = null;
        return;
    }

    /* JADX INFO: renamed from: lambda$onChildSourceInfoRefreshed$2$androidx-media3-exoplayer-source-preload-PreloadMediaSource */
    /* synthetic */ void m142xd674fafd(Timeline r8) {
        if (isUsedByPlayer() == true) goto L14;
        if (this.onSourcePreparedNotified == true) goto L15;
        this.onSourcePreparedNotified = true;
        if (this.preloadControl.onSourcePrepared(this) == true) goto L11;
        stopPreloading();
        return;
    L11:
        Pair<Object, Long> r9 = r8.getPeriodPositionUs(new Timeline.Window(), new Timeline.Period(), 0, this.startPositionUs);
        createPeriod(new MediaSource.MediaPeriodId(r9.first), this.allocator, ((Long) r9.second).longValue()).preload(new PreloadMediaPeriodCallback(this, ((Long) r9.second).longValue()), ((Long) r9.second).longValue());
        return;
    L15:
        return;
    }

    /* JADX INFO: renamed from: lambda$preload$0$androidx-media3-exoplayer-source-preload-PreloadMediaSource */
    /* synthetic */ void m143xf99e9a56(long r2) {
        this.preloadCalled = true;
        this.startPositionUs = r2;
        this.onSourcePreparedNotified = false;
        if (isUsedByPlayer() == false) goto L5;
        onUsedByPlayer();
        return;
    L5:
        setPlayerId(PlayerId.UNSET);
        prepareSourceInternal(this.bandwidthMeter.getTransferListener());
        checkForPreloadError();
    }

    /* JADX INFO: renamed from: lambda$releasePreloadMediaSource$3$androidx-media3-exoplayer-source-preload-PreloadMediaSource */
    /* synthetic */ void m144xd9831bcd() {
        this.preloadCalled = false;
        this.startPositionUs = C0180C.TIME_UNSET;
        this.onSourcePreparedNotified = false;
        if (this.preloadingMediaPeriodAndKey == null) goto L5;
        this.mediaSource.releasePeriod(((PreloadMediaPeriod) this.preloadingMediaPeriodAndKey.first).mediaPeriod);
        this.preloadingMediaPeriodAndKey = null;
    L5:
        releaseSourceInternal();
        this.preloadHandler.removeCallbacksAndMessages(null);
        this.releaseHandler.removeCallbacksAndMessages(null);
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource
    protected void onChildSourceInfoRefreshed(final Timeline r3) {
        this.timeline = r3;
        refreshSourceInfo(r3);
        this.preloadHandler.post(new PreloadMediaSource$$ExternalSyntheticLambda4(this, r3));
    }

    public void preload(final long r3) {
        this.preloadHandler.post(new PreloadMediaSource$$ExternalSyntheticLambda3(this, r3));
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource
    protected void prepareSourceInternal() {
        if (isUsedByPlayer() == false) goto L8;
        if (this.onUsedByPlayerNotified == true) goto L8;
        onUsedByPlayer();
    L8:
        if (this.timeline == null) goto L11;
        onChildSourceInfoRefreshed(this.timeline);
        return;
    L11:
        if (this.prepareChildSourceCalled == true) goto L15;
        this.prepareChildSourceCalled = true;
        prepareChildSource();
        return;
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r4) {
        PreloadMediaPeriod r0 = (PreloadMediaPeriod) r4;
        if (this.preloadingMediaPeriodAndKey == null) goto L8;
        if (r0 != ((Pair) Assertions.checkNotNull(this.preloadingMediaPeriodAndKey)).first) goto L8;
        this.preloadingMediaPeriodAndKey = null;
    L12:
        this.mediaSource.releasePeriod(r0.mediaPeriod);
        return;
    L8:
        if (this.playingPreloadedMediaPeriodAndId == null) goto L12;
        if (r0 != ((Pair) Assertions.checkNotNull(this.playingPreloadedMediaPeriodAndId)).first) goto L12;
        this.playingPreloadedMediaPeriodAndId = null;
        goto L12
    }

    public void releasePreloadMediaSource() {
        this.releaseHandler.post(new PreloadMediaSource$$ExternalSyntheticLambda0(this));
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    protected void releaseSourceInternal() {
        if (isUsedByPlayer() == true) goto L8;
        this.onUsedByPlayerNotified = false;
        if (this.preloadCalled == true) goto L9;
        this.timeline = null;
        this.prepareChildSourceCalled = false;
        super.releaseSourceInternal();
        return;
    L9:
        return;
    }
}
