package androidx.media3.exoplayer;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.AuxEffectInfo;
import androidx.media3.common.C0180C;
import androidx.media3.common.DeviceInfo;
import androidx.media3.common.Effect;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.PriorityTaskManager;
import androidx.media3.common.VideoSize;
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.analytics.AnalyticsCollector;
import androidx.media3.exoplayer.analytics.AnalyticsListener;
import androidx.media3.exoplayer.image.ImageOutput;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ShuffleOrder;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.exoplayer.trackselection.TrackSelectionArray;
import androidx.media3.exoplayer.trackselection.TrackSelector;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter;
import androidx.media3.exoplayer.video.VideoFrameMetadataListener;
import androidx.media3.exoplayer.video.spherical.CameraMotionListener;
import androidx.media3.extractor.DefaultExtractorsFactory;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface ExoPlayer extends Player {
    public static final long DEFAULT_DETACH_SURFACE_TIMEOUT_MS = 2000;
    public static final long DEFAULT_RELEASE_TIMEOUT_MS = 500;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.ExoPlayer$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ PlaybackException $default$getPlayerError(ExoPlayer r1) {
            return r1.getPlayerError();
        }
    }

    @Deprecated
    public interface AudioComponent {
        @Deprecated
        void clearAuxEffectInfo();

        @Deprecated
        AudioAttributes getAudioAttributes();

        @Deprecated
        int getAudioSessionId();

        @Deprecated
        boolean getSkipSilenceEnabled();

        @Deprecated
        float getVolume();

        @Deprecated
        void setAudioAttributes(AudioAttributes r1, boolean r2);

        @Deprecated
        void setAudioSessionId(int r1);

        @Deprecated
        void setAuxEffectInfo(AuxEffectInfo r1);

        @Deprecated
        void setSkipSilenceEnabled(boolean r1);

        @Deprecated
        void setVolume(float r1);
    }

    public interface AudioOffloadListener {

        /* JADX INFO: renamed from: androidx.media3.exoplayer.ExoPlayer$AudioOffloadListener$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static void $default$onOffloadedPlayback(AudioOffloadListener r0, boolean r1) {
            }

            public static void $default$onSleepingForOffloadChanged(AudioOffloadListener r0, boolean r1) {
            }
        }

        void onOffloadedPlayback(boolean r1);

        void onSleepingForOffloadChanged(boolean r1);
    }

    public static final class Builder {
        Function<Clock, AnalyticsCollector> analyticsCollectorFunction;
        AudioAttributes audioAttributes;
        Supplier<BandwidthMeter> bandwidthMeterSupplier;
        boolean buildCalled;
        Clock clock;
        final Context context;
        long detachSurfaceTimeoutMs;
        boolean deviceVolumeControlEnabled;
        boolean dynamicSchedulingEnabled;
        long foregroundModeTimeoutMs;
        boolean handleAudioBecomingNoisy;
        boolean handleAudioFocus;
        LivePlaybackSpeedControl livePlaybackSpeedControl;
        Supplier<LoadControl> loadControlSupplier;
        Looper looper;
        long maxSeekToPreviousPositionMs;
        Supplier<MediaSource.Factory> mediaSourceFactorySupplier;
        boolean pauseAtEndOfMediaItems;
        PlaybackLooperProvider playbackLooperProvider;
        String playerName;
        int priority;
        PriorityTaskManager priorityTaskManager;
        long releaseTimeoutMs;
        Supplier<RenderersFactory> renderersFactorySupplier;
        long seekBackIncrementMs;
        long seekForwardIncrementMs;
        SeekParameters seekParameters;
        boolean skipSilenceEnabled;
        SuitableOutputChecker suitableOutputChecker;
        boolean suppressPlaybackOnUnsuitableOutput;
        Supplier<TrackSelector> trackSelectorSupplier;
        boolean useLazyPreparation;
        boolean usePlatformDiagnostics;
        int videoChangeFrameRateStrategy;
        int videoScalingMode;
        int wakeMode;

        public Builder(final Context r3) {
            this(r3, new ExoPlayer$Builder$$ExternalSyntheticLambda2(r3), new ExoPlayer$Builder$$ExternalSyntheticLambda3(r3));
        }

        public Builder(final Context r3, final RenderersFactory r4) {
            this(r3, new ExoPlayer$Builder$$ExternalSyntheticLambda7(r4), new ExoPlayer$Builder$$ExternalSyntheticLambda8(r3));
            Assertions.checkNotNull(r4);
        }

        public Builder(Context r3, final RenderersFactory r4, final MediaSource.Factory r5) {
            this(r3, new ExoPlayer$Builder$$ExternalSyntheticLambda5(r4), new ExoPlayer$Builder$$ExternalSyntheticLambda6(r5));
            Assertions.checkNotNull(r4);
            Assertions.checkNotNull(r5);
        }

        public Builder(Context r9, final RenderersFactory r10, final MediaSource.Factory r11, final TrackSelector r12, final LoadControl r13, final BandwidthMeter r14, final AnalyticsCollector r15) {
            this(r9, new ExoPlayer$Builder$$ExternalSyntheticLambda9(r10), new ExoPlayer$Builder$$ExternalSyntheticLambda10(r11), new ExoPlayer$Builder$$ExternalSyntheticLambda12(r12), new ExoPlayer$Builder$$ExternalSyntheticLambda13(r13), new ExoPlayer$Builder$$ExternalSyntheticLambda14(r14), new ExoPlayer$Builder$$ExternalSyntheticLambda15(r15));
            Assertions.checkNotNull(r10);
            Assertions.checkNotNull(r11);
            Assertions.checkNotNull(r12);
            Assertions.checkNotNull(r14);
            Assertions.checkNotNull(r15);
        }

        public Builder(final Context r3, final MediaSource.Factory r4) {
            this(r3, new ExoPlayer$Builder$$ExternalSyntheticLambda20(r3), new ExoPlayer$Builder$$ExternalSyntheticLambda21(r4));
            Assertions.checkNotNull(r4);
        }

        private Builder(final Context r9, Supplier<RenderersFactory> r10, Supplier<MediaSource.Factory> r11) {
            this(r9, r10, r11, new ExoPlayer$Builder$$ExternalSyntheticLambda16(r9), new ExoPlayer$Builder$$ExternalSyntheticLambda17(), new ExoPlayer$Builder$$ExternalSyntheticLambda18(r9), new ExoPlayer$Builder$$ExternalSyntheticLambda19());
        }

        private Builder(Context r5, Supplier<RenderersFactory> r6, Supplier<MediaSource.Factory> r7, Supplier<TrackSelector> r8, Supplier<LoadControl> r9, Supplier<BandwidthMeter> r10, Function<Clock, AnalyticsCollector> r11) {
            this.context = (Context) Assertions.checkNotNull(r5);
            this.renderersFactorySupplier = r6;
            this.mediaSourceFactorySupplier = r7;
            this.trackSelectorSupplier = r8;
            this.loadControlSupplier = r9;
            this.bandwidthMeterSupplier = r10;
            this.analyticsCollectorFunction = r11;
            this.looper = Util.getCurrentOrMainLooper();
            this.audioAttributes = AudioAttributes.DEFAULT;
            this.wakeMode = 0;
            this.videoScalingMode = 1;
            this.videoChangeFrameRateStrategy = 0;
            this.useLazyPreparation = true;
            this.seekParameters = SeekParameters.DEFAULT;
            this.seekBackIncrementMs = 5000;
            this.seekForwardIncrementMs = C0180C.DEFAULT_SEEK_FORWARD_INCREMENT_MS;
            this.maxSeekToPreviousPositionMs = C0180C.DEFAULT_MAX_SEEK_TO_PREVIOUS_POSITION_MS;
            this.livePlaybackSpeedControl = new DefaultLivePlaybackSpeedControl.Builder().build();
            this.clock = Clock.DEFAULT;
            this.releaseTimeoutMs = 500;
            this.detachSurfaceTimeoutMs = ExoPlayer.DEFAULT_DETACH_SURFACE_TIMEOUT_MS;
            this.usePlatformDiagnostics = true;
            this.playerName = "";
            this.priority = -1000;
        }

        static /* synthetic */ RenderersFactory lambda$new$0(Context r1) {
            return new DefaultRenderersFactory(r1);
        }

        static /* synthetic */ MediaSource.Factory lambda$new$1(Context r2) {
            return new DefaultMediaSourceFactory(r2, new DefaultExtractorsFactory());
        }

        static /* synthetic */ TrackSelector lambda$new$10(TrackSelector r0) {
            return r0;
        }

        static /* synthetic */ LoadControl lambda$new$11(LoadControl r0) {
            return r0;
        }

        static /* synthetic */ BandwidthMeter lambda$new$12(BandwidthMeter r0) {
            return r0;
        }

        static /* synthetic */ AnalyticsCollector lambda$new$13(AnalyticsCollector r0, Clock r1) {
            return r0;
        }

        static /* synthetic */ TrackSelector lambda$new$14(Context r1) {
            return new DefaultTrackSelector(r1);
        }

        static /* synthetic */ BandwidthMeter lambda$new$15(Context r1) {
            return DefaultBandwidthMeter.getSingletonInstance(r1);
        }

        static /* synthetic */ RenderersFactory lambda$new$2(RenderersFactory r0) {
            return r0;
        }

        static /* synthetic */ MediaSource.Factory lambda$new$3(Context r2) {
            return new DefaultMediaSourceFactory(r2, new DefaultExtractorsFactory());
        }

        static /* synthetic */ RenderersFactory lambda$new$4(Context r1) {
            return new DefaultRenderersFactory(r1);
        }

        static /* synthetic */ MediaSource.Factory lambda$new$5(MediaSource.Factory r0) {
            return r0;
        }

        static /* synthetic */ RenderersFactory lambda$new$6(RenderersFactory r0) {
            return r0;
        }

        static /* synthetic */ MediaSource.Factory lambda$new$7(MediaSource.Factory r0) {
            return r0;
        }

        static /* synthetic */ RenderersFactory lambda$new$8(RenderersFactory r0) {
            return r0;
        }

        static /* synthetic */ MediaSource.Factory lambda$new$9(MediaSource.Factory r0) {
            return r0;
        }

        static /* synthetic */ AnalyticsCollector lambda$setAnalyticsCollector$21(AnalyticsCollector r0, Clock r1) {
            return r0;
        }

        static /* synthetic */ BandwidthMeter lambda$setBandwidthMeter$20(BandwidthMeter r0) {
            return r0;
        }

        static /* synthetic */ LoadControl lambda$setLoadControl$19(LoadControl r0) {
            return r0;
        }

        static /* synthetic */ MediaSource.Factory lambda$setMediaSourceFactory$17(MediaSource.Factory r0) {
            return r0;
        }

        static /* synthetic */ RenderersFactory lambda$setRenderersFactory$16(RenderersFactory r0) {
            return r0;
        }

        static /* synthetic */ TrackSelector lambda$setTrackSelector$18(TrackSelector r0) {
            return r0;
        }

        public ExoPlayer build() {
            Assertions.checkState(!this.buildCalled);
            this.buildCalled = true;
            if (this.suitableOutputChecker != null) goto L10;
            if (Util.SDK_INT < 35) goto L10;
            if (this.suppressPlaybackOnUnsuitableOutput == false) goto L10;
            this.suitableOutputChecker = new DefaultSuitableOutputChecker(this.context, new Handler(this.looper));
        L10:
            return new ExoPlayerImpl(this, null);
        }

        SimpleExoPlayer buildSimpleExoPlayer() {
            Assertions.checkState(!this.buildCalled);
            this.buildCalled = true;
            return new SimpleExoPlayer(this);
        }

        public Builder experimentalSetDynamicSchedulingEnabled(boolean r2) {
            Assertions.checkState(!this.buildCalled);
            this.dynamicSchedulingEnabled = r2;
            return this;
        }

        public Builder experimentalSetForegroundModeTimeoutMs(long r2) {
            Assertions.checkState(!this.buildCalled);
            this.foregroundModeTimeoutMs = r2;
            return this;
        }

        public Builder setAnalyticsCollector(final AnalyticsCollector r2) {
            Assertions.checkState(!this.buildCalled);
            Assertions.checkNotNull(r2);
            this.analyticsCollectorFunction = new ExoPlayer$Builder$$ExternalSyntheticLambda1(r2);
            return this;
        }

        public Builder setAudioAttributes(AudioAttributes r2, boolean r3) {
            Assertions.checkState(!this.buildCalled);
            this.audioAttributes = (AudioAttributes) Assertions.checkNotNull(r2);
            this.handleAudioFocus = r3;
            return this;
        }

        public Builder setBandwidthMeter(final BandwidthMeter r2) {
            Assertions.checkState(!this.buildCalled);
            Assertions.checkNotNull(r2);
            this.bandwidthMeterSupplier = new ExoPlayer$Builder$$ExternalSyntheticLambda11(r2);
            return this;
        }

        public Builder setClock(Clock r2) {
            Assertions.checkState(!this.buildCalled);
            this.clock = r2;
            return this;
        }

        public Builder setDetachSurfaceTimeoutMs(long r2) {
            Assertions.checkState(!this.buildCalled);
            this.detachSurfaceTimeoutMs = r2;
            return this;
        }

        public Builder setDeviceVolumeControlEnabled(boolean r2) {
            Assertions.checkState(!this.buildCalled);
            this.deviceVolumeControlEnabled = r2;
            return this;
        }

        public Builder setHandleAudioBecomingNoisy(boolean r2) {
            Assertions.checkState(!this.buildCalled);
            this.handleAudioBecomingNoisy = r2;
            return this;
        }

        public Builder setLivePlaybackSpeedControl(LivePlaybackSpeedControl r2) {
            Assertions.checkState(!this.buildCalled);
            this.livePlaybackSpeedControl = (LivePlaybackSpeedControl) Assertions.checkNotNull(r2);
            return this;
        }

        public Builder setLoadControl(final LoadControl r2) {
            Assertions.checkState(!this.buildCalled);
            Assertions.checkNotNull(r2);
            this.loadControlSupplier = new ExoPlayer$Builder$$ExternalSyntheticLambda0(r2);
            return this;
        }

        public Builder setLooper(Looper r2) {
            Assertions.checkState(!this.buildCalled);
            Assertions.checkNotNull(r2);
            this.looper = r2;
            return this;
        }

        public Builder setMaxSeekToPreviousPositionMs(long r3) {
            if (r3 < 0) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            Assertions.checkState(!this.buildCalled);
            this.maxSeekToPreviousPositionMs = r3;
            return this;
        L5:
            r0 = false;
            goto L6
        }

        public Builder setMediaSourceFactory(final MediaSource.Factory r2) {
            Assertions.checkState(!this.buildCalled);
            Assertions.checkNotNull(r2);
            this.mediaSourceFactorySupplier = new ExoPlayer$Builder$$ExternalSyntheticLambda23(r2);
            return this;
        }

        public Builder setName(String r2) {
            Assertions.checkState(!this.buildCalled);
            this.playerName = r2;
            return this;
        }

        public Builder setPauseAtEndOfMediaItems(boolean r2) {
            Assertions.checkState(!this.buildCalled);
            this.pauseAtEndOfMediaItems = r2;
            return this;
        }

        public Builder setPlaybackLooper(Looper r2) {
            Assertions.checkState(!this.buildCalled);
            this.playbackLooperProvider = new PlaybackLooperProvider(r2);
            return this;
        }

        public Builder setPlaybackLooperProvider(PlaybackLooperProvider r2) {
            Assertions.checkState(!this.buildCalled);
            this.playbackLooperProvider = r2;
            return this;
        }

        public Builder setPriority(int r2) {
            Assertions.checkState(!this.buildCalled);
            this.priority = r2;
            return this;
        }

        public Builder setPriorityTaskManager(PriorityTaskManager r2) {
            Assertions.checkState(!this.buildCalled);
            this.priorityTaskManager = r2;
            return this;
        }

        public Builder setReleaseTimeoutMs(long r2) {
            Assertions.checkState(!this.buildCalled);
            this.releaseTimeoutMs = r2;
            return this;
        }

        public Builder setRenderersFactory(final RenderersFactory r2) {
            Assertions.checkState(!this.buildCalled);
            Assertions.checkNotNull(r2);
            this.renderersFactorySupplier = new ExoPlayer$Builder$$ExternalSyntheticLambda4(r2);
            return this;
        }

        public Builder setSeekBackIncrementMs(long r3) {
            if (r3 <= 0) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            Assertions.checkState(!this.buildCalled);
            this.seekBackIncrementMs = r3;
            return this;
        L5:
            r0 = false;
            goto L6
        }

        public Builder setSeekForwardIncrementMs(long r3) {
            if (r3 <= 0) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            Assertions.checkState(!this.buildCalled);
            this.seekForwardIncrementMs = r3;
            return this;
        L5:
            r0 = false;
            goto L6
        }

        public Builder setSeekParameters(SeekParameters r2) {
            Assertions.checkState(!this.buildCalled);
            this.seekParameters = (SeekParameters) Assertions.checkNotNull(r2);
            return this;
        }

        public Builder setSkipSilenceEnabled(boolean r2) {
            Assertions.checkState(!this.buildCalled);
            this.skipSilenceEnabled = r2;
            return this;
        }

        public Builder setSuitableOutputChecker(SuitableOutputChecker r2) {
            Assertions.checkState(!this.buildCalled);
            this.suitableOutputChecker = r2;
            return this;
        }

        public Builder setSuppressPlaybackOnUnsuitableOutput(boolean r2) {
            Assertions.checkState(!this.buildCalled);
            this.suppressPlaybackOnUnsuitableOutput = r2;
            return this;
        }

        public Builder setTrackSelector(final TrackSelector r2) {
            Assertions.checkState(!this.buildCalled);
            Assertions.checkNotNull(r2);
            this.trackSelectorSupplier = new ExoPlayer$Builder$$ExternalSyntheticLambda22(r2);
            return this;
        }

        public Builder setUseLazyPreparation(boolean r2) {
            Assertions.checkState(!this.buildCalled);
            this.useLazyPreparation = r2;
            return this;
        }

        public Builder setUsePlatformDiagnostics(boolean r2) {
            Assertions.checkState(!this.buildCalled);
            this.usePlatformDiagnostics = r2;
            return this;
        }

        public Builder setVideoChangeFrameRateStrategy(int r2) {
            Assertions.checkState(!this.buildCalled);
            this.videoChangeFrameRateStrategy = r2;
            return this;
        }

        public Builder setVideoScalingMode(int r2) {
            Assertions.checkState(!this.buildCalled);
            this.videoScalingMode = r2;
            return this;
        }

        public Builder setWakeMode(int r2) {
            Assertions.checkState(!this.buildCalled);
            this.wakeMode = r2;
            return this;
        }
    }

    @Deprecated
    public interface DeviceComponent {
        @Deprecated
        void decreaseDeviceVolume();

        @Deprecated
        DeviceInfo getDeviceInfo();

        @Deprecated
        int getDeviceVolume();

        @Deprecated
        void increaseDeviceVolume();

        @Deprecated
        boolean isDeviceMuted();

        @Deprecated
        void setDeviceMuted(boolean r1);

        @Deprecated
        void setDeviceVolume(int r1);
    }

    public static class PreloadConfiguration {
        public static final PreloadConfiguration DEFAULT = null;
        public final long targetPreloadDurationUs;

        static {
            DEFAULT = new PreloadConfiguration(C0180C.TIME_UNSET);
        }

        public PreloadConfiguration(long r1) {
            this.targetPreloadDurationUs = r1;
        }
    }

    @Deprecated
    public interface TextComponent {
        @Deprecated
        CueGroup getCurrentCues();
    }

    @Deprecated
    public interface VideoComponent {
        @Deprecated
        void clearCameraMotionListener(CameraMotionListener r1);

        @Deprecated
        void clearVideoFrameMetadataListener(VideoFrameMetadataListener r1);

        @Deprecated
        void clearVideoSurface();

        @Deprecated
        void clearVideoSurface(Surface r1);

        @Deprecated
        void clearVideoSurfaceHolder(SurfaceHolder r1);

        @Deprecated
        void clearVideoSurfaceView(SurfaceView r1);

        @Deprecated
        void clearVideoTextureView(TextureView r1);

        @Deprecated
        int getVideoChangeFrameRateStrategy();

        @Deprecated
        int getVideoScalingMode();

        @Deprecated
        VideoSize getVideoSize();

        @Deprecated
        void setCameraMotionListener(CameraMotionListener r1);

        @Deprecated
        void setVideoChangeFrameRateStrategy(int r1);

        @Deprecated
        void setVideoFrameMetadataListener(VideoFrameMetadataListener r1);

        @Deprecated
        void setVideoScalingMode(int r1);

        @Deprecated
        void setVideoSurface(Surface r1);

        @Deprecated
        void setVideoSurfaceHolder(SurfaceHolder r1);

        @Deprecated
        void setVideoSurfaceView(SurfaceView r1);

        @Deprecated
        void setVideoTextureView(TextureView r1);
    }

    void addAnalyticsListener(AnalyticsListener r1);

    void addAudioOffloadListener(AudioOffloadListener r1);

    void addMediaSource(int r1, MediaSource r2);

    void addMediaSource(MediaSource r1);

    void addMediaSources(int r1, List<MediaSource> r2);

    void addMediaSources(List<MediaSource> r1);

    void clearAuxEffectInfo();

    void clearCameraMotionListener(CameraMotionListener r1);

    void clearVideoFrameMetadataListener(VideoFrameMetadataListener r1);

    PlayerMessage createMessage(PlayerMessage.Target r1);

    AnalyticsCollector getAnalyticsCollector();

    @Deprecated
    AudioComponent getAudioComponent();

    DecoderCounters getAudioDecoderCounters();

    Format getAudioFormat();

    int getAudioSessionId();

    Clock getClock();

    @Deprecated
    TrackGroupArray getCurrentTrackGroups();

    @Deprecated
    TrackSelectionArray getCurrentTrackSelections();

    @Deprecated
    DeviceComponent getDeviceComponent();

    boolean getPauseAtEndOfMediaItems();

    Looper getPlaybackLooper();

    @Override // androidx.media3.common.Player
    ExoPlaybackException getPlayerError();

    PreloadConfiguration getPreloadConfiguration();

    Renderer getRenderer(int r1);

    int getRendererCount();

    int getRendererType(int r1);

    SeekParameters getSeekParameters();

    boolean getSkipSilenceEnabled();

    @Deprecated
    TextComponent getTextComponent();

    TrackSelector getTrackSelector();

    int getVideoChangeFrameRateStrategy();

    @Deprecated
    VideoComponent getVideoComponent();

    DecoderCounters getVideoDecoderCounters();

    Format getVideoFormat();

    int getVideoScalingMode();

    boolean isReleased();

    boolean isSleepingForOffload();

    boolean isTunnelingEnabled();

    @Deprecated
    void prepare(MediaSource r1);

    @Deprecated
    void prepare(MediaSource r1, boolean r2, boolean r3);

    @Override // androidx.media3.common.Player
    void release();

    void removeAnalyticsListener(AnalyticsListener r1);

    void removeAudioOffloadListener(AudioOffloadListener r1);

    @Override // androidx.media3.common.Player
    void replaceMediaItem(int r1, MediaItem r2);

    @Override // androidx.media3.common.Player
    void replaceMediaItems(int r1, int r2, List<MediaItem> r3);

    void setAudioSessionId(int r1);

    void setAuxEffectInfo(AuxEffectInfo r1);

    void setCameraMotionListener(CameraMotionListener r1);

    void setForegroundMode(boolean r1);

    void setHandleAudioBecomingNoisy(boolean r1);

    void setImageOutput(ImageOutput r1);

    void setMediaSource(MediaSource r1);

    void setMediaSource(MediaSource r1, long r2);

    void setMediaSource(MediaSource r1, boolean r2);

    void setMediaSources(List<MediaSource> r1);

    void setMediaSources(List<MediaSource> r1, int r2, long r3);

    void setMediaSources(List<MediaSource> r1, boolean r2);

    void setPauseAtEndOfMediaItems(boolean r1);

    void setPreferredAudioDevice(AudioDeviceInfo r1);

    void setPreloadConfiguration(PreloadConfiguration r1);

    void setPriority(int r1);

    void setPriorityTaskManager(PriorityTaskManager r1);

    void setSeekParameters(SeekParameters r1);

    void setShuffleOrder(ShuffleOrder r1);

    void setSkipSilenceEnabled(boolean r1);

    void setVideoChangeFrameRateStrategy(int r1);

    void setVideoEffects(List<Effect> r1);

    void setVideoFrameMetadataListener(VideoFrameMetadataListener r1);

    void setVideoScalingMode(int r1);

    void setWakeMode(int r1);
}
