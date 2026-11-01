package androidx.media3.exoplayer.source.preload;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.DefaultRendererCapabilitiesList;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.ExoPlayer$Builder$$ExternalSyntheticLambda17;
import androidx.media3.exoplayer.LoadControl;
import androidx.media3.exoplayer.PlaybackLooperProvider;
import androidx.media3.exoplayer.RendererCapabilitiesList;
import androidx.media3.exoplayer.RenderersFactory;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.trackselection.TrackSelector;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultPreloadManager extends BasePreloadManager<Integer> {
    private final boolean deprecatedConstructorCalled;
    private final Handler preloadHandler;
    private final PlaybackLooperProvider preloadLooperProvider;
    private final PreloadMediaSource.Factory preloadMediaSourceFactory;
    private final RendererCapabilitiesList rendererCapabilitiesList;
    private final TrackSelector trackSelector;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.preload.DefaultPreloadManager$1 */
    static /* synthetic */ class C03541 {
    }

    public static final class Builder extends BasePreloadManager.BuilderBase<Integer> {
        private Supplier<BandwidthMeter> bandwidthMeterSupplier;
        private boolean buildCalled;
        private boolean buildExoPlayerCalled;
        private final Context context;
        private Supplier<LoadControl> loadControlSupplier;
        private PlaybackLooperProvider preloadLooperProvider;
        private Supplier<RenderersFactory> renderersFactorySupplier;
        private TrackSelector.Factory trackSelectorFactory;

        public Builder(final Context r3, TargetPreloadStatusControl<Integer> r4) {
            super(new RankingDataComparator(), r4, Suppliers.memoize(new DefaultPreloadManager$Builder$$ExternalSyntheticLambda3(r3)));
            this.context = r3;
            this.preloadLooperProvider = new PlaybackLooperProvider();
            this.trackSelectorFactory = new DefaultPreloadManager$Builder$$ExternalSyntheticLambda4();
            this.bandwidthMeterSupplier = new DefaultPreloadManager$Builder$$ExternalSyntheticLambda5(r3);
            this.renderersFactorySupplier = Suppliers.memoize(new DefaultPreloadManager$Builder$$ExternalSyntheticLambda6(r3));
            this.loadControlSupplier = Suppliers.memoize(new ExoPlayer$Builder$$ExternalSyntheticLambda17());
        }

        static /* synthetic */ Supplier access$100(Builder r1) {
            return r1.renderersFactorySupplier;
        }

        static /* synthetic */ PlaybackLooperProvider access$200(Builder r1) {
            return r1.preloadLooperProvider;
        }

        static /* synthetic */ Context access$300(Builder r1) {
            return r1.context;
        }

        static /* synthetic */ TrackSelector.Factory access$400(Builder r1) {
            return r1.trackSelectorFactory;
        }

        static /* synthetic */ Supplier access$500(Builder r1) {
            return r1.bandwidthMeterSupplier;
        }

        static /* synthetic */ Supplier access$700(Builder r1) {
            return r1.loadControlSupplier;
        }

        static /* synthetic */ MediaSource.Factory lambda$new$0(Context r1) {
            return new DefaultMediaSourceFactory(r1);
        }

        static /* synthetic */ BandwidthMeter lambda$new$1(Context r1) {
            return DefaultBandwidthMeter.getSingletonInstance(r1);
        }

        static /* synthetic */ RenderersFactory lambda$new$2(Context r1) {
            return new DefaultRenderersFactory(r1);
        }

        static /* synthetic */ BandwidthMeter lambda$setBandwidthMeter$6(BandwidthMeter r0) {
            return r0;
        }

        static /* synthetic */ LoadControl lambda$setLoadControl$5(LoadControl r0) {
            return r0;
        }

        static /* synthetic */ MediaSource.Factory lambda$setMediaSourceFactory$3(MediaSource.Factory r0) {
            return r0;
        }

        static /* synthetic */ RenderersFactory lambda$setRenderersFactory$4(RenderersFactory r0) {
            return r0;
        }

        @Override // androidx.media3.exoplayer.source.preload.BasePreloadManager.BuilderBase
        public /* bridge */ /* synthetic */ BasePreloadManager<Integer> build() {
            return build2();
        }

        @Override // androidx.media3.exoplayer.source.preload.BasePreloadManager.BuilderBase
        /* JADX INFO: renamed from: build, reason: avoid collision after fix types in other method */
        public BasePreloadManager<Integer> build2() {
            Assertions.checkState(!this.buildCalled);
            this.buildCalled = true;
            return new DefaultPreloadManager(this, null);
        }

        public ExoPlayer buildExoPlayer() {
            return buildExoPlayer(new ExoPlayer.Builder(this.context));
        }

        public ExoPlayer buildExoPlayer(ExoPlayer.Builder r4) {
            this.buildExoPlayerCalled = true;
            return r4.setMediaSourceFactory(this.mediaSourceFactorySupplier.get()).setBandwidthMeter(this.bandwidthMeterSupplier.get()).setRenderersFactory(this.renderersFactorySupplier.get()).setLoadControl(this.loadControlSupplier.get()).setPlaybackLooperProvider(this.preloadLooperProvider).setTrackSelector(this.trackSelectorFactory.createTrackSelector(this.context)).build();
        }

        public Builder setBandwidthMeter(final BandwidthMeter r2) {
            if (this.buildCalled == false) goto L5;
        L7:
            boolean r0 = false;
        L8:
            Assertions.checkState(r0);
            this.bandwidthMeterSupplier = new DefaultPreloadManager$Builder$$ExternalSyntheticLambda2(r2);
            return this;
        L5:
            if (this.buildExoPlayerCalled == true) goto L7;
            r0 = true;
            goto L8
        }

        public Builder setLoadControl(final LoadControl r2) {
            if (this.buildCalled == false) goto L5;
        L7:
            boolean r0 = false;
        L8:
            Assertions.checkState(r0);
            this.loadControlSupplier = new DefaultPreloadManager$Builder$$ExternalSyntheticLambda0(r2);
            return this;
        L5:
            if (this.buildExoPlayerCalled == true) goto L7;
            r0 = true;
            goto L8
        }

        public Builder setMediaSourceFactory(final MediaSource.Factory r2) {
            if (this.buildCalled == false) goto L5;
        L7:
            boolean r0 = false;
        L8:
            Assertions.checkState(r0);
            this.mediaSourceFactorySupplier = new DefaultPreloadManager$Builder$$ExternalSyntheticLambda7(r2);
            return this;
        L5:
            if (this.buildExoPlayerCalled == true) goto L7;
            r0 = true;
            goto L8
        }

        public Builder setPreloadLooper(Looper r2) {
            if (this.buildCalled == false) goto L5;
        L7:
            boolean r0 = false;
        L8:
            Assertions.checkState(r0);
            this.preloadLooperProvider = new PlaybackLooperProvider(r2);
            return this;
        L5:
            if (this.buildExoPlayerCalled == true) goto L7;
            r0 = true;
            goto L8
        }

        public Builder setRenderersFactory(final RenderersFactory r2) {
            if (this.buildCalled == false) goto L5;
        L7:
            boolean r0 = false;
        L8:
            Assertions.checkState(r0);
            this.renderersFactorySupplier = new DefaultPreloadManager$Builder$$ExternalSyntheticLambda1(r2);
            return this;
        L5:
            if (this.buildExoPlayerCalled == true) goto L7;
            r0 = true;
            goto L8
        }

        public Builder setTrackSelectorFactory(TrackSelector.Factory r2) {
            if (this.buildCalled == false) goto L5;
        L7:
            boolean r0 = false;
        L8:
            Assertions.checkState(r0);
            this.trackSelectorFactory = r2;
            return this;
        L5:
            if (this.buildExoPlayerCalled == true) goto L7;
            r0 = true;
            goto L8
        }
    }

    private static final class RankingDataComparator implements Comparator<Integer> {
        public int currentPlayingIndex;

        public RankingDataComparator() {
            this.currentPlayingIndex = -1;
        }

        /* JADX INFO: renamed from: compare, reason: avoid collision after fix types in other method */
        public int compare2(Integer r4, Integer r5) {
            return Integer.compare(Math.abs(r4.intValue() - this.currentPlayingIndex), Math.abs(r5.intValue() - this.currentPlayingIndex));
        }

        @Override // java.util.Comparator
        public /* bridge */ /* synthetic */ int compare(Integer r1, Integer r2) {
            return compare2(r1, r2);
        }
    }

    private final class SourcePreloadControl implements PreloadMediaSource.PreloadControl {
        final /* synthetic */ DefaultPreloadManager this$0;

        private SourcePreloadControl(DefaultPreloadManager r1) {
            this.this$0 = r1;
        }

        /* synthetic */ SourcePreloadControl(DefaultPreloadManager r1, C03541 r2) {
            this(r1);
        }

        private boolean continueOrCompletePreloading(PreloadMediaSource r4, Predicate<Status> r5, boolean r6) {
            TargetPreloadStatusControl.PreloadStatus r0 = this.this$0.getTargetPreloadStatus(r4);
            if (r0 != null) goto L5;
            this.this$0.onPreloadSkipped(r4);
            return false;
        L5:
            if (r5.apply((Status) Assertions.checkNotNull((Status) r0)) == false) goto L8;
            return true;
        L8:
            if (r6 == false) goto L10;
            this.this$0.clearSourceInternal(r4);
        L10:
            this.this$0.onPreloadCompleted(r4);
            return false;
        }

        static /* synthetic */ boolean lambda$onContinueLoadingRequested$2(long r4, Status r6) {
            if (r6.getStage() == 2) goto L5;
        L7:
            return false;
        L5:
            if (r6.getValue() <= Util.usToMs(r4)) goto L7;
            return true;
        }

        static /* synthetic */ boolean lambda$onSourcePrepared$0(Status r1) {
            if (r1.getStage() <= 0) goto L5;
            return true;
        L5:
            return false;
        }

        static /* synthetic */ boolean lambda$onTracksSelected$1(Status r2) {
            if (r2.getStage() > 1) goto L7;
            return false;
        L7:
            return true;
        }

        @Override // androidx.media3.exoplayer.source.preload.PreloadMediaSource.PreloadControl
        public boolean onContinueLoadingRequested(PreloadMediaSource r3, final long r4) {
            return continueOrCompletePreloading(r3, new C0357x5b4610df(r4), false);
        }

        @Override // androidx.media3.exoplayer.source.preload.PreloadMediaSource.PreloadControl
        public void onLoadedToTheEndOfSource(PreloadMediaSource r2) {
            this.this$0.onPreloadCompleted(r2);
        }

        @Override // androidx.media3.exoplayer.source.preload.PreloadMediaSource.PreloadControl
        public void onPreloadError(PreloadException r2, PreloadMediaSource r3) {
            this.this$0.onPreloadError(r2, r3);
        }

        @Override // androidx.media3.exoplayer.source.preload.PreloadMediaSource.PreloadControl
        public boolean onSourcePrepared(PreloadMediaSource r3) {
            return continueOrCompletePreloading(r3, new C0356x5b4610de(), true);
        }

        @Override // androidx.media3.exoplayer.source.preload.PreloadMediaSource.PreloadControl
        public boolean onTracksSelected(PreloadMediaSource r3) {
            return continueOrCompletePreloading(r3, new C0355x5b4610dd(), false);
        }

        @Override // androidx.media3.exoplayer.source.preload.PreloadMediaSource.PreloadControl
        public void onUsedByPlayer(PreloadMediaSource r2) {
            this.this$0.onPreloadSkipped(r2);
        }
    }

    public static class Status implements TargetPreloadStatusControl.PreloadStatus {
        public static final int STAGE_LOADED_FOR_DURATION_MS = 2;
        public static final int STAGE_SOURCE_PREPARED = 0;
        public static final int STAGE_TRACKS_SELECTED = 1;
        private final int stage;
        private final long value;

        @Target({ElementType.TYPE_USE})
        @Documented
        @Retention(RetentionPolicy.SOURCE)
        public @interface Stage {
        }

        public Status(int r3) {
            this(r3, C0180C.TIME_UNSET);
        }

        public Status(int r1, long r2) {
            this.stage = r1;
            this.value = r2;
        }

        @Override // androidx.media3.exoplayer.source.preload.TargetPreloadStatusControl.PreloadStatus
        public int getStage() {
            return this.stage;
        }

        @Override // androidx.media3.exoplayer.source.preload.TargetPreloadStatusControl.PreloadStatus
        public long getValue() {
            return this.value;
        }
    }

    private DefaultPreloadManager(Builder r10) {
        super(new RankingDataComparator(), r10.targetPreloadStatusControl, r10.mediaSourceFactorySupplier.get());
        this.rendererCapabilitiesList = new DefaultRendererCapabilitiesList.Factory((RenderersFactory) Builder.access$100(r10).get()).createRendererCapabilitiesList();
        this.preloadLooperProvider = Builder.access$200(r10);
        this.trackSelector = Builder.access$400(r10).createTrackSelector(Builder.access$300(r10));
        BandwidthMeter r5 = (BandwidthMeter) Builder.access$500(r10).get();
        this.trackSelector.init(new DefaultPreloadManager$$ExternalSyntheticLambda1(), r5);
        Looper r8 = this.preloadLooperProvider.obtainLooper();
        this.preloadMediaSourceFactory = new PreloadMediaSource.Factory(r10.mediaSourceFactorySupplier.get(), new SourcePreloadControl(this, null), this.trackSelector, r5, this.rendererCapabilitiesList.getRendererCapabilities(), ((LoadControl) Builder.access$700(r10).get()).getAllocator(), r8);
        this.preloadHandler = Util.createHandler(r8, null);
        this.deprecatedConstructorCalled = false;
    }

    /* synthetic */ DefaultPreloadManager(Builder r1, C03541 r2) {
        this(r1);
    }

    @Deprecated
    public DefaultPreloadManager(TargetPreloadStatusControl<Integer> r11, MediaSource.Factory r12, TrackSelector r13, BandwidthMeter r14, RendererCapabilitiesList.Factory r15, Allocator r16, Looper r17) {
        super(new RankingDataComparator(), r11, r12);
        this.rendererCapabilitiesList = r15.createRendererCapabilitiesList();
        this.preloadLooperProvider = new PlaybackLooperProvider(r17);
        this.trackSelector = r13;
        Looper r9 = this.preloadLooperProvider.obtainLooper();
        this.preloadMediaSourceFactory = new PreloadMediaSource.Factory(r12, new SourcePreloadControl(this, null), r13, r14, this.rendererCapabilitiesList.getRendererCapabilities(), r16, r9);
        this.preloadHandler = Util.createHandler(r9, null);
        this.deprecatedConstructorCalled = true;
    }

    static /* synthetic */ void lambda$new$0() {
    }

    @Override // androidx.media3.exoplayer.source.preload.BasePreloadManager
    protected void clearSourceInternal(MediaSource r2) {
        Assertions.checkArgument(r2 instanceof PreloadMediaSource);
        ((PreloadMediaSource) r2).clear();
    }

    @Override // androidx.media3.exoplayer.source.preload.BasePreloadManager
    public MediaSource createMediaSourceForPreloading(MediaSource r2) {
        return this.preloadMediaSourceFactory.createMediaSource(r2);
    }

    /* JADX INFO: renamed from: lambda$releaseInternal$1$androidx-media3-exoplayer-source-preload-DefaultPreloadManager */
    /* synthetic */ void m140xafd85f69() {
        this.rendererCapabilitiesList.release();
        if (this.deprecatedConstructorCalled == true) goto L5;
        this.trackSelector.release();
    L5:
        this.preloadLooperProvider.releaseLooper();
    }

    @Override // androidx.media3.exoplayer.source.preload.BasePreloadManager
    protected void preloadSourceInternal(MediaSource r2, long r3) {
        Assertions.checkArgument(r2 instanceof PreloadMediaSource);
        ((PreloadMediaSource) r2).preload(r3);
    }

    @Override // androidx.media3.exoplayer.source.preload.BasePreloadManager
    protected void releaseInternal() {
        this.preloadHandler.post(new DefaultPreloadManager$$ExternalSyntheticLambda0(this));
    }

    @Override // androidx.media3.exoplayer.source.preload.BasePreloadManager
    protected void releaseSourceInternal(MediaSource r2) {
        Assertions.checkArgument(r2 instanceof PreloadMediaSource);
        ((PreloadMediaSource) r2).releasePreloadMediaSource();
    }

    public void setCurrentPlayingIndex(int r2) {
        ((RankingDataComparator) this.rankingDataComparator).currentPlayingIndex = r2;
    }
}
