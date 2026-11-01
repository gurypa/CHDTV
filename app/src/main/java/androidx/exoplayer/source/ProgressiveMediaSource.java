package androidx.media3.exoplayer.source;

import android.net.Uri;
import android.os.Looper;
import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.drm.DefaultDrmSessionManagerProvider;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import androidx.media3.exoplayer.drm.DrmSessionManager;
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.DefaultLoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.exoplayer.util.ReleasableExecutor;
import androidx.media3.extractor.DefaultExtractorsFactory;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.base.Supplier;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public final class ProgressiveMediaSource extends BaseMediaSource implements ProgressiveMediaPeriod.Listener {
    public static final int DEFAULT_LOADING_CHECK_INTERVAL_BYTES = 1048576;
    private final int continueLoadingCheckIntervalBytes;
    private final DataSource.Factory dataSourceFactory;
    private final Supplier<ReleasableExecutor> downloadExecutorSupplier;
    private final DrmSessionManager drmSessionManager;
    private final LoadErrorHandlingPolicy loadableLoadErrorHandlingPolicy;
    private MediaItem mediaItem;
    private final ProgressiveMediaExtractor.Factory progressiveMediaExtractorFactory;
    private final boolean suppressPrepareError;
    private long timelineDurationUs;
    private boolean timelineIsLive;
    private boolean timelineIsPlaceholder;
    private boolean timelineIsSeekable;
    private TransferListener transferListener;

    public static final class Factory implements MediaSourceFactory {
        private int continueLoadingCheckIntervalBytes;
        private final DataSource.Factory dataSourceFactory;
        private Supplier<ReleasableExecutor> downloadExecutorSupplier;
        private DrmSessionManagerProvider drmSessionManagerProvider;
        private LoadErrorHandlingPolicy loadErrorHandlingPolicy;
        private ProgressiveMediaExtractor.Factory progressiveMediaExtractorFactory;
        private boolean suppressPrepareError;

        public Factory(DataSource.Factory r2) {
            this(r2, new DefaultExtractorsFactory());
        }

        public Factory(DataSource.Factory r7, ProgressiveMediaExtractor.Factory r8) {
            this(r7, r8, new DefaultDrmSessionManagerProvider(), new DefaultLoadErrorHandlingPolicy(), 1048576);
        }

        public Factory(DataSource.Factory r1, ProgressiveMediaExtractor.Factory r2, DrmSessionManagerProvider r3, LoadErrorHandlingPolicy r4, int r5) {
            this.dataSourceFactory = r1;
            this.progressiveMediaExtractorFactory = r2;
            this.drmSessionManagerProvider = r3;
            this.loadErrorHandlingPolicy = r4;
            this.continueLoadingCheckIntervalBytes = r5;
        }

        public Factory(DataSource.Factory r2, final ExtractorsFactory r3) {
            this(r2, new ProgressiveMediaSource$Factory$$ExternalSyntheticLambda0(r3));
        }

        static /* synthetic */ ProgressiveMediaExtractor lambda$new$0(ExtractorsFactory r1, PlayerId r2) {
            return new BundledExtractorsAdapter(r1);
        }

        static /* synthetic */ ReleasableExecutor lambda$setDownloadExecutor$1(Supplier r1, Consumer r2) {
            return ReleasableExecutor.CC.from((Executor) r1.get(), r2);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource createMediaSource(MediaItem r1) {
            return createMediaSource(r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public ProgressiveMediaSource createMediaSource(MediaItem r12) {
            Assertions.checkNotNull(r12.localConfiguration);
            return new ProgressiveMediaSource(r12, this.dataSourceFactory, this.progressiveMediaExtractorFactory, this.drmSessionManagerProvider.get(r12), this.loadErrorHandlingPolicy, this.continueLoadingCheckIntervalBytes, this.suppressPrepareError, this.downloadExecutorSupplier, null);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* synthetic */ MediaSource.Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
            return MediaSource.Factory.CC.$default$experimentalParseSubtitlesDuringExtraction(this, r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public int[] getSupportedTypes() {
            return new int[]{4};
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* synthetic */ MediaSource.Factory setCmcdConfigurationFactory(CmcdConfiguration.Factory r1) {
            return MediaSource.Factory.CC.$default$setCmcdConfigurationFactory(this, r1);
        }

        public Factory setContinueLoadingCheckIntervalBytes(int r1) {
            this.continueLoadingCheckIntervalBytes = r1;
            return this;
        }

        public <T extends Executor> Factory setDownloadExecutor(final Supplier<T> r2, final Consumer<T> r3) {
            this.downloadExecutorSupplier = new ProgressiveMediaSource$Factory$$ExternalSyntheticLambda1(r2, r3);
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource.Factory setDrmSessionManagerProvider(DrmSessionManagerProvider r1) {
            return setDrmSessionManagerProvider(r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public Factory setDrmSessionManagerProvider(DrmSessionManagerProvider r2) {
            this.drmSessionManagerProvider = (DrmSessionManagerProvider) Assertions.checkNotNull(r2, "MediaSource.Factory#setDrmSessionManagerProvider no longer handles null by instantiating a new DefaultDrmSessionManagerProvider. Explicitly construct and pass an instance in order to retain the old behavior.");
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* bridge */ /* synthetic */ MediaSource.Factory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r1) {
            return setLoadErrorHandlingPolicy(r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public Factory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r2) {
            this.loadErrorHandlingPolicy = (LoadErrorHandlingPolicy) Assertions.checkNotNull(r2, "MediaSource.Factory#setLoadErrorHandlingPolicy no longer handles null by instantiating a new DefaultLoadErrorHandlingPolicy. Explicitly construct and pass an instance in order to retain the old behavior.");
            return this;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.Factory
        public /* synthetic */ MediaSource.Factory setSubtitleParserFactory(SubtitleParser.Factory r1) {
            return MediaSource.Factory.CC.$default$setSubtitleParserFactory(this, r1);
        }

        Factory setSuppressPrepareError(boolean r1) {
            this.suppressPrepareError = r1;
            return this;
        }
    }

    private ProgressiveMediaSource(MediaItem r3, DataSource.Factory r4, ProgressiveMediaExtractor.Factory r5, DrmSessionManager r6, LoadErrorHandlingPolicy r7, int r8, boolean r9, Supplier<ReleasableExecutor> r10) {
        this.mediaItem = r3;
        this.dataSourceFactory = r4;
        this.progressiveMediaExtractorFactory = r5;
        this.drmSessionManager = r6;
        this.loadableLoadErrorHandlingPolicy = r7;
        this.continueLoadingCheckIntervalBytes = r8;
        this.suppressPrepareError = r9;
        this.timelineIsPlaceholder = true;
        this.timelineDurationUs = C0180C.TIME_UNSET;
        this.downloadExecutorSupplier = r10;
    }

    /* synthetic */ ProgressiveMediaSource(MediaItem r1, DataSource.Factory r2, ProgressiveMediaExtractor.Factory r3, DrmSessionManager r4, LoadErrorHandlingPolicy r5, int r6, boolean r7, Supplier r8, C03461 r9) {
        this(r1, r2, r3, r4, r5, r6, r7, r8);
    }

    private MediaItem.LocalConfiguration getLocalConfiguration() {
        return (MediaItem.LocalConfiguration) Assertions.checkNotNull(getMediaItem().localConfiguration);
    }

    private void notifySourceInfoRefreshed() {
        final Timeline r0 = new SinglePeriodTimeline(this.timelineDurationUs, this.timelineIsSeekable, false, this.timelineIsLive, null, getMediaItem());
        if (this.timelineIsPlaceholder == false) goto L5;
        r0 = new C03461(this, r0);
    L5:
        refreshSourceInfo(r0);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean canUpdateMediaItem(MediaItem r7) {
        MediaItem.LocalConfiguration r0 = getLocalConfiguration();
        MediaItem.LocalConfiguration r1 = r7.localConfiguration;
        if (r1 != null) goto L5;
    L11:
        return false;
    L5:
        if (r1.uri.equals(r0.uri) == false) goto L11;
        if (r1.imageDurationMs != r0.imageDurationMs) goto L11;
        if (Util.areEqual(r1.customCacheKey, r0.customCacheKey) == false) goto L11;
        return true;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r18, Allocator r19, long r20) {
        DataSource r2 = this.dataSourceFactory.createDataSource();
        if (this.transferListener == null) goto L5;
        r2.addTransferListener(this.transferListener);
    L5:
        MediaItem.LocalConfiguration r0 = getLocalConfiguration();
        Uri r1 = r0.uri;
        ProgressiveMediaExtractor r4 = this.progressiveMediaExtractorFactory.createProgressiveMediaExtractor(getPlayerId());
        DrmSessionManager r5 = this.drmSessionManager;
        DrmSessionEventListener.EventDispatcher r6 = createDrmEventDispatcher(r18);
        LoadErrorHandlingPolicy r7 = this.loadableLoadErrorHandlingPolicy;
        MediaSourceEventListener.EventDispatcher r8 = createEventDispatcher(r18);
        String r10 = r0.customCacheKey;
        int r11 = this.continueLoadingCheckIntervalBytes;
        boolean r12 = this.suppressPrepareError;
        long r13 = Util.msToUs(r0.imageDurationMs);
        if (this.downloadExecutorSupplier == null) goto L8;
        ReleasableExecutor r15 = this.downloadExecutorSupplier.get();
    L10:
        return new ProgressiveMediaPeriod(r1, r2, r4, r5, r6, r7, r8, this, r19, r10, r11, r12, r13, r15);
    L8:
        r15 = null;
        goto L10
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
    public void maybeThrowSourceInfoRefreshError() {
    }

    @Override // androidx.media3.exoplayer.source.ProgressiveMediaPeriod.Listener
    public void onSourceInfoRefreshed(long r3, boolean r5, boolean r6) {
        if (r3 != C0180C.TIME_UNSET) goto L5;
        long r0 = this.timelineDurationUs;
    L7:
        if (this.timelineIsPlaceholder == false) goto L9;
    L15:
        this.timelineDurationUs = r0;
        this.timelineIsSeekable = r5;
        this.timelineIsLive = r6;
        this.timelineIsPlaceholder = false;
        notifySourceInfoRefreshed();
        return;
    L9:
        if (this.timelineDurationUs != r0) goto L15;
        if (this.timelineIsSeekable != r5) goto L15;
        if (this.timelineIsLive != r6) goto L15;
        return;
    L5:
        r0 = r3;
        goto L7
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void prepareSourceInternal(TransferListener r4) {
        this.transferListener = r4;
        this.drmSessionManager.setPlayer((Looper) Assertions.checkNotNull(Looper.myLooper()), getPlayerId());
        this.drmSessionManager.prepare();
        notifySourceInfoRefreshed();
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r2) {
        ((ProgressiveMediaPeriod) r2).release();
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource
    protected void releaseSourceInternal() {
        this.drmSessionManager.release();
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
