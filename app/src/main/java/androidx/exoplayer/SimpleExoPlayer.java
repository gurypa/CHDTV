package androidx.media3.exoplayer;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.os.Looper;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.AuxEffectInfo;
import androidx.media3.common.BasePlayer;
import androidx.media3.common.DeviceInfo;
import androidx.media3.common.Effect;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.PriorityTaskManager;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.Tracks;
import androidx.media3.common.VideoSize;
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.ConditionVariable;
import androidx.media3.common.util.Size;
import androidx.media3.exoplayer.analytics.AnalyticsCollector;
import androidx.media3.exoplayer.analytics.AnalyticsListener;
import androidx.media3.exoplayer.image.ImageOutput;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ShuffleOrder;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.trackselection.TrackSelectionArray;
import androidx.media3.exoplayer.trackselection.TrackSelector;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import androidx.media3.exoplayer.video.VideoFrameMetadataListener;
import androidx.media3.exoplayer.video.spherical.CameraMotionListener;
import androidx.media3.extractor.ExtractorsFactory;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class SimpleExoPlayer extends BasePlayer implements ExoPlayer, ExoPlayer.AudioComponent, ExoPlayer.VideoComponent, ExoPlayer.TextComponent, ExoPlayer.DeviceComponent {
    private final ConditionVariable constructorFinished;
    private final ExoPlayerImpl player;

    @Deprecated
    public static final class Builder {
        private final ExoPlayer.Builder wrappedBuilder;

        @Deprecated
        public Builder(Context r2) {
            this.wrappedBuilder = new ExoPlayer.Builder(r2);
        }

        @Deprecated
        public Builder(Context r2, RenderersFactory r3) {
            this.wrappedBuilder = new ExoPlayer.Builder(r2, r3);
        }

        @Deprecated
        public Builder(Context r9, RenderersFactory r10, TrackSelector r11, MediaSource.Factory r12, LoadControl r13, BandwidthMeter r14, AnalyticsCollector r15) {
            this.wrappedBuilder = new ExoPlayer.Builder(r9, r10, r12, r11, r13, r14, r15);
        }

        @Deprecated
        public Builder(Context r3, RenderersFactory r4, ExtractorsFactory r5) {
            this.wrappedBuilder = new ExoPlayer.Builder(r3, r4, new DefaultMediaSourceFactory(r3, r5));
        }

        @Deprecated
        public Builder(Context r3, ExtractorsFactory r4) {
            this.wrappedBuilder = new ExoPlayer.Builder(r3, new DefaultMediaSourceFactory(r3, r4));
        }

        static /* synthetic */ ExoPlayer.Builder access$000(Builder r1) {
            return r1.wrappedBuilder;
        }

        @Deprecated
        public SimpleExoPlayer build() {
            return this.wrappedBuilder.buildSimpleExoPlayer();
        }

        @Deprecated
        public Builder experimentalSetForegroundModeTimeoutMs(long r2) {
            this.wrappedBuilder.experimentalSetForegroundModeTimeoutMs(r2);
            return this;
        }

        @Deprecated
        public Builder setAnalyticsCollector(AnalyticsCollector r2) {
            this.wrappedBuilder.setAnalyticsCollector(r2);
            return this;
        }

        @Deprecated
        public Builder setAudioAttributes(AudioAttributes r2, boolean r3) {
            this.wrappedBuilder.setAudioAttributes(r2, r3);
            return this;
        }

        @Deprecated
        public Builder setBandwidthMeter(BandwidthMeter r2) {
            this.wrappedBuilder.setBandwidthMeter(r2);
            return this;
        }

        @Deprecated
        public Builder setClock(Clock r2) {
            this.wrappedBuilder.setClock(r2);
            return this;
        }

        @Deprecated
        public Builder setDetachSurfaceTimeoutMs(long r2) {
            this.wrappedBuilder.setDetachSurfaceTimeoutMs(r2);
            return this;
        }

        @Deprecated
        public Builder setHandleAudioBecomingNoisy(boolean r2) {
            this.wrappedBuilder.setHandleAudioBecomingNoisy(r2);
            return this;
        }

        @Deprecated
        public Builder setLivePlaybackSpeedControl(LivePlaybackSpeedControl r2) {
            this.wrappedBuilder.setLivePlaybackSpeedControl(r2);
            return this;
        }

        @Deprecated
        public Builder setLoadControl(LoadControl r2) {
            this.wrappedBuilder.setLoadControl(r2);
            return this;
        }

        @Deprecated
        public Builder setLooper(Looper r2) {
            this.wrappedBuilder.setLooper(r2);
            return this;
        }

        @Deprecated
        public Builder setMediaSourceFactory(MediaSource.Factory r2) {
            this.wrappedBuilder.setMediaSourceFactory(r2);
            return this;
        }

        @Deprecated
        public Builder setPauseAtEndOfMediaItems(boolean r2) {
            this.wrappedBuilder.setPauseAtEndOfMediaItems(r2);
            return this;
        }

        @Deprecated
        public Builder setPriorityTaskManager(PriorityTaskManager r2) {
            this.wrappedBuilder.setPriorityTaskManager(r2);
            return this;
        }

        @Deprecated
        public Builder setReleaseTimeoutMs(long r2) {
            this.wrappedBuilder.setReleaseTimeoutMs(r2);
            return this;
        }

        @Deprecated
        public Builder setSeekBackIncrementMs(long r2) {
            this.wrappedBuilder.setSeekBackIncrementMs(r2);
            return this;
        }

        @Deprecated
        public Builder setSeekForwardIncrementMs(long r2) {
            this.wrappedBuilder.setSeekForwardIncrementMs(r2);
            return this;
        }

        @Deprecated
        public Builder setSeekParameters(SeekParameters r2) {
            this.wrappedBuilder.setSeekParameters(r2);
            return this;
        }

        @Deprecated
        public Builder setSkipSilenceEnabled(boolean r2) {
            this.wrappedBuilder.setSkipSilenceEnabled(r2);
            return this;
        }

        @Deprecated
        public Builder setTrackSelector(TrackSelector r2) {
            this.wrappedBuilder.setTrackSelector(r2);
            return this;
        }

        @Deprecated
        public Builder setUseLazyPreparation(boolean r2) {
            this.wrappedBuilder.setUseLazyPreparation(r2);
            return this;
        }

        @Deprecated
        public Builder setVideoChangeFrameRateStrategy(int r2) {
            this.wrappedBuilder.setVideoChangeFrameRateStrategy(r2);
            return this;
        }

        @Deprecated
        public Builder setVideoScalingMode(int r2) {
            this.wrappedBuilder.setVideoScalingMode(r2);
            return this;
        }

        @Deprecated
        public Builder setWakeMode(int r2) {
            this.wrappedBuilder.setWakeMode(r2);
            return this;
        }
    }

    @Deprecated
    protected SimpleExoPlayer(Context r9, RenderersFactory r10, TrackSelector r11, MediaSource.Factory r12, LoadControl r13, BandwidthMeter r14, AnalyticsCollector r15, boolean r16, Clock r17, Looper r18) {
        this(new ExoPlayer.Builder(r9, r10, r12, r11, r13, r14, r15).setUseLazyPreparation(r16).setClock(r17).setLooper(r18));
    }

    SimpleExoPlayer(ExoPlayer.Builder r3) {
        this.constructorFinished = new ConditionVariable();
        this.player = new ExoPlayerImpl(r3, this);     // Catch: Throwable -> L6
        this.constructorFinished.open();
        return;
    L6:
        th = move-exception;
        this.constructorFinished.open();
        throw th;
    }

    protected SimpleExoPlayer(Builder r2) {
        this(Builder.access$000(r2));
    }

    private void blockUntilConstructorFinished() {
        this.constructorFinished.blockUninterruptible();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void addAnalyticsListener(AnalyticsListener r2) {
        blockUntilConstructorFinished();
        this.player.addAnalyticsListener(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void addAudioOffloadListener(ExoPlayer.AudioOffloadListener r2) {
        blockUntilConstructorFinished();
        this.player.addAudioOffloadListener(r2);
    }

    @Override // androidx.media3.common.Player
    public void addListener(Player.Listener r2) {
        blockUntilConstructorFinished();
        this.player.addListener(r2);
    }

    @Override // androidx.media3.common.Player
    public void addMediaItems(int r2, List<MediaItem> r3) {
        blockUntilConstructorFinished();
        this.player.addMediaItems(r2, r3);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void addMediaSource(int r2, MediaSource r3) {
        blockUntilConstructorFinished();
        this.player.addMediaSource(r2, r3);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void addMediaSource(MediaSource r2) {
        blockUntilConstructorFinished();
        this.player.addMediaSource(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void addMediaSources(int r2, List<MediaSource> r3) {
        blockUntilConstructorFinished();
        this.player.addMediaSources(r2, r3);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void addMediaSources(List<MediaSource> r2) {
        blockUntilConstructorFinished();
        this.player.addMediaSources(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.AudioComponent
    public void clearAuxEffectInfo() {
        blockUntilConstructorFinished();
        this.player.clearAuxEffectInfo();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public void clearCameraMotionListener(CameraMotionListener r2) {
        blockUntilConstructorFinished();
        this.player.clearCameraMotionListener(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public void clearVideoFrameMetadataListener(VideoFrameMetadataListener r2) {
        blockUntilConstructorFinished();
        this.player.clearVideoFrameMetadataListener(r2);
    }

    @Override // androidx.media3.common.Player
    public void clearVideoSurface() {
        blockUntilConstructorFinished();
        this.player.clearVideoSurface();
    }

    @Override // androidx.media3.common.Player
    public void clearVideoSurface(Surface r2) {
        blockUntilConstructorFinished();
        this.player.clearVideoSurface(r2);
    }

    @Override // androidx.media3.common.Player
    public void clearVideoSurfaceHolder(SurfaceHolder r2) {
        blockUntilConstructorFinished();
        this.player.clearVideoSurfaceHolder(r2);
    }

    @Override // androidx.media3.common.Player
    public void clearVideoSurfaceView(SurfaceView r2) {
        blockUntilConstructorFinished();
        this.player.clearVideoSurfaceView(r2);
    }

    @Override // androidx.media3.common.Player
    public void clearVideoTextureView(TextureView r2) {
        blockUntilConstructorFinished();
        this.player.clearVideoTextureView(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public PlayerMessage createMessage(PlayerMessage.Target r2) {
        blockUntilConstructorFinished();
        return this.player.createMessage(r2);
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void decreaseDeviceVolume() {
        blockUntilConstructorFinished();
        this.player.decreaseDeviceVolume();
    }

    @Override // androidx.media3.common.Player
    public void decreaseDeviceVolume(int r2) {
        blockUntilConstructorFinished();
        this.player.decreaseDeviceVolume(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public AnalyticsCollector getAnalyticsCollector() {
        blockUntilConstructorFinished();
        return this.player.getAnalyticsCollector();
    }

    @Override // androidx.media3.common.Player
    public Looper getApplicationLooper() {
        blockUntilConstructorFinished();
        return this.player.getApplicationLooper();
    }

    @Override // androidx.media3.common.Player
    public AudioAttributes getAudioAttributes() {
        blockUntilConstructorFinished();
        return this.player.getAudioAttributes();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    @Deprecated
    public ExoPlayer.AudioComponent getAudioComponent() {
        return this;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public DecoderCounters getAudioDecoderCounters() {
        blockUntilConstructorFinished();
        return this.player.getAudioDecoderCounters();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public Format getAudioFormat() {
        blockUntilConstructorFinished();
        return this.player.getAudioFormat();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.AudioComponent
    public int getAudioSessionId() {
        blockUntilConstructorFinished();
        return this.player.getAudioSessionId();
    }

    @Override // androidx.media3.common.Player
    public Player.Commands getAvailableCommands() {
        blockUntilConstructorFinished();
        return this.player.getAvailableCommands();
    }

    @Override // androidx.media3.common.Player
    public long getBufferedPosition() {
        blockUntilConstructorFinished();
        return this.player.getBufferedPosition();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public Clock getClock() {
        blockUntilConstructorFinished();
        return this.player.getClock();
    }

    @Override // androidx.media3.common.Player
    public long getContentBufferedPosition() {
        blockUntilConstructorFinished();
        return this.player.getContentBufferedPosition();
    }

    @Override // androidx.media3.common.Player
    public long getContentPosition() {
        blockUntilConstructorFinished();
        return this.player.getContentPosition();
    }

    @Override // androidx.media3.common.Player
    public int getCurrentAdGroupIndex() {
        blockUntilConstructorFinished();
        return this.player.getCurrentAdGroupIndex();
    }

    @Override // androidx.media3.common.Player
    public int getCurrentAdIndexInAdGroup() {
        blockUntilConstructorFinished();
        return this.player.getCurrentAdIndexInAdGroup();
    }

    @Override // androidx.media3.common.Player
    public CueGroup getCurrentCues() {
        blockUntilConstructorFinished();
        return this.player.getCurrentCues();
    }

    @Override // androidx.media3.common.Player
    public int getCurrentMediaItemIndex() {
        blockUntilConstructorFinished();
        return this.player.getCurrentMediaItemIndex();
    }

    @Override // androidx.media3.common.Player
    public int getCurrentPeriodIndex() {
        blockUntilConstructorFinished();
        return this.player.getCurrentPeriodIndex();
    }

    @Override // androidx.media3.common.Player
    public long getCurrentPosition() {
        blockUntilConstructorFinished();
        return this.player.getCurrentPosition();
    }

    @Override // androidx.media3.common.Player
    public Timeline getCurrentTimeline() {
        blockUntilConstructorFinished();
        return this.player.getCurrentTimeline();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    @Deprecated
    public TrackGroupArray getCurrentTrackGroups() {
        blockUntilConstructorFinished();
        return this.player.getCurrentTrackGroups();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    @Deprecated
    public TrackSelectionArray getCurrentTrackSelections() {
        blockUntilConstructorFinished();
        return this.player.getCurrentTrackSelections();
    }

    @Override // androidx.media3.common.Player
    public Tracks getCurrentTracks() {
        blockUntilConstructorFinished();
        return this.player.getCurrentTracks();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    @Deprecated
    public ExoPlayer.DeviceComponent getDeviceComponent() {
        return this;
    }

    @Override // androidx.media3.common.Player
    public DeviceInfo getDeviceInfo() {
        blockUntilConstructorFinished();
        return this.player.getDeviceInfo();
    }

    @Override // androidx.media3.common.Player
    public int getDeviceVolume() {
        blockUntilConstructorFinished();
        return this.player.getDeviceVolume();
    }

    @Override // androidx.media3.common.Player
    public long getDuration() {
        blockUntilConstructorFinished();
        return this.player.getDuration();
    }

    @Override // androidx.media3.common.Player
    public long getMaxSeekToPreviousPosition() {
        blockUntilConstructorFinished();
        return this.player.getMaxSeekToPreviousPosition();
    }

    @Override // androidx.media3.common.Player
    public MediaMetadata getMediaMetadata() {
        blockUntilConstructorFinished();
        return this.player.getMediaMetadata();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public boolean getPauseAtEndOfMediaItems() {
        blockUntilConstructorFinished();
        return this.player.getPauseAtEndOfMediaItems();
    }

    @Override // androidx.media3.common.Player
    public boolean getPlayWhenReady() {
        blockUntilConstructorFinished();
        return this.player.getPlayWhenReady();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public Looper getPlaybackLooper() {
        blockUntilConstructorFinished();
        return this.player.getPlaybackLooper();
    }

    @Override // androidx.media3.common.Player
    public PlaybackParameters getPlaybackParameters() {
        blockUntilConstructorFinished();
        return this.player.getPlaybackParameters();
    }

    @Override // androidx.media3.common.Player
    public int getPlaybackState() {
        blockUntilConstructorFinished();
        return this.player.getPlaybackState();
    }

    @Override // androidx.media3.common.Player
    public int getPlaybackSuppressionReason() {
        blockUntilConstructorFinished();
        return this.player.getPlaybackSuppressionReason();
    }

    @Override // androidx.media3.common.Player
    public /* bridge */ /* synthetic */ PlaybackException getPlayerError() {
        return getPlayerError();
    }

    @Override // androidx.media3.common.Player
    public ExoPlaybackException getPlayerError() {
        blockUntilConstructorFinished();
        return this.player.getPlayerError();
    }

    @Override // androidx.media3.common.Player
    public MediaMetadata getPlaylistMetadata() {
        blockUntilConstructorFinished();
        return this.player.getPlaylistMetadata();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public ExoPlayer.PreloadConfiguration getPreloadConfiguration() {
        blockUntilConstructorFinished();
        return this.player.getPreloadConfiguration();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public Renderer getRenderer(int r2) {
        blockUntilConstructorFinished();
        return this.player.getRenderer(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public int getRendererCount() {
        blockUntilConstructorFinished();
        return this.player.getRendererCount();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public int getRendererType(int r2) {
        blockUntilConstructorFinished();
        return this.player.getRendererType(r2);
    }

    @Override // androidx.media3.common.Player
    public int getRepeatMode() {
        blockUntilConstructorFinished();
        return this.player.getRepeatMode();
    }

    @Override // androidx.media3.common.Player
    public long getSeekBackIncrement() {
        blockUntilConstructorFinished();
        return this.player.getSeekBackIncrement();
    }

    @Override // androidx.media3.common.Player
    public long getSeekForwardIncrement() {
        blockUntilConstructorFinished();
        return this.player.getSeekForwardIncrement();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public SeekParameters getSeekParameters() {
        blockUntilConstructorFinished();
        return this.player.getSeekParameters();
    }

    @Override // androidx.media3.common.Player
    public boolean getShuffleModeEnabled() {
        blockUntilConstructorFinished();
        return this.player.getShuffleModeEnabled();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.AudioComponent
    public boolean getSkipSilenceEnabled() {
        blockUntilConstructorFinished();
        return this.player.getSkipSilenceEnabled();
    }

    @Override // androidx.media3.common.Player
    public Size getSurfaceSize() {
        blockUntilConstructorFinished();
        return this.player.getSurfaceSize();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    @Deprecated
    public ExoPlayer.TextComponent getTextComponent() {
        return this;
    }

    @Override // androidx.media3.common.Player
    public long getTotalBufferedDuration() {
        blockUntilConstructorFinished();
        return this.player.getTotalBufferedDuration();
    }

    @Override // androidx.media3.common.Player
    public TrackSelectionParameters getTrackSelectionParameters() {
        blockUntilConstructorFinished();
        return this.player.getTrackSelectionParameters();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public TrackSelector getTrackSelector() {
        blockUntilConstructorFinished();
        return this.player.getTrackSelector();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public int getVideoChangeFrameRateStrategy() {
        blockUntilConstructorFinished();
        return this.player.getVideoChangeFrameRateStrategy();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    @Deprecated
    public ExoPlayer.VideoComponent getVideoComponent() {
        return this;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public DecoderCounters getVideoDecoderCounters() {
        blockUntilConstructorFinished();
        return this.player.getVideoDecoderCounters();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public Format getVideoFormat() {
        blockUntilConstructorFinished();
        return this.player.getVideoFormat();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public int getVideoScalingMode() {
        blockUntilConstructorFinished();
        return this.player.getVideoScalingMode();
    }

    @Override // androidx.media3.common.Player
    public VideoSize getVideoSize() {
        blockUntilConstructorFinished();
        return this.player.getVideoSize();
    }

    @Override // androidx.media3.common.Player
    public float getVolume() {
        blockUntilConstructorFinished();
        return this.player.getVolume();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void increaseDeviceVolume() {
        blockUntilConstructorFinished();
        this.player.increaseDeviceVolume();
    }

    @Override // androidx.media3.common.Player
    public void increaseDeviceVolume(int r2) {
        blockUntilConstructorFinished();
        this.player.increaseDeviceVolume(r2);
    }

    @Override // androidx.media3.common.Player
    public boolean isDeviceMuted() {
        blockUntilConstructorFinished();
        return this.player.isDeviceMuted();
    }

    @Override // androidx.media3.common.Player
    public boolean isLoading() {
        blockUntilConstructorFinished();
        return this.player.isLoading();
    }

    @Override // androidx.media3.common.Player
    public boolean isPlayingAd() {
        blockUntilConstructorFinished();
        return this.player.isPlayingAd();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public boolean isReleased() {
        return this.player.isReleased();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public boolean isSleepingForOffload() {
        blockUntilConstructorFinished();
        return this.player.isSleepingForOffload();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public boolean isTunnelingEnabled() {
        blockUntilConstructorFinished();
        return this.player.isTunnelingEnabled();
    }

    @Override // androidx.media3.common.Player
    public void moveMediaItems(int r2, int r3, int r4) {
        blockUntilConstructorFinished();
        this.player.moveMediaItems(r2, r3, r4);
    }

    @Override // androidx.media3.common.Player
    public void prepare() {
        blockUntilConstructorFinished();
        this.player.prepare();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    @Deprecated
    public void prepare(MediaSource r2) {
        blockUntilConstructorFinished();
        this.player.prepare(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    @Deprecated
    public void prepare(MediaSource r2, boolean r3, boolean r4) {
        blockUntilConstructorFinished();
        this.player.prepare(r2, r3, r4);
    }

    @Override // androidx.media3.common.Player
    public void release() {
        blockUntilConstructorFinished();
        this.player.release();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void removeAnalyticsListener(AnalyticsListener r2) {
        blockUntilConstructorFinished();
        this.player.removeAnalyticsListener(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void removeAudioOffloadListener(ExoPlayer.AudioOffloadListener r2) {
        blockUntilConstructorFinished();
        this.player.removeAudioOffloadListener(r2);
    }

    @Override // androidx.media3.common.Player
    public void removeListener(Player.Listener r2) {
        blockUntilConstructorFinished();
        this.player.removeListener(r2);
    }

    @Override // androidx.media3.common.Player
    public void removeMediaItems(int r2, int r3) {
        blockUntilConstructorFinished();
        this.player.removeMediaItems(r2, r3);
    }

    @Override // androidx.media3.common.Player
    public void replaceMediaItems(int r2, int r3, List<MediaItem> r4) {
        blockUntilConstructorFinished();
        this.player.replaceMediaItems(r2, r3, r4);
    }

    @Override // androidx.media3.common.BasePlayer
    public void seekTo(int r7, long r8, int r10, boolean r11) {
        blockUntilConstructorFinished();
        this.player.seekTo(r7, r8, r10, r11);
    }

    @Override // androidx.media3.common.Player
    public void setAudioAttributes(AudioAttributes r2, boolean r3) {
        blockUntilConstructorFinished();
        this.player.setAudioAttributes(r2, r3);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.AudioComponent
    public void setAudioSessionId(int r2) {
        blockUntilConstructorFinished();
        this.player.setAudioSessionId(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.AudioComponent
    public void setAuxEffectInfo(AuxEffectInfo r2) {
        blockUntilConstructorFinished();
        this.player.setAuxEffectInfo(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public void setCameraMotionListener(CameraMotionListener r2) {
        blockUntilConstructorFinished();
        this.player.setCameraMotionListener(r2);
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void setDeviceMuted(boolean r2) {
        blockUntilConstructorFinished();
        this.player.setDeviceMuted(r2);
    }

    @Override // androidx.media3.common.Player
    public void setDeviceMuted(boolean r2, int r3) {
        blockUntilConstructorFinished();
        this.player.setDeviceMuted(r2, r3);
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void setDeviceVolume(int r2) {
        blockUntilConstructorFinished();
        this.player.setDeviceVolume(r2);
    }

    @Override // androidx.media3.common.Player
    public void setDeviceVolume(int r2, int r3) {
        blockUntilConstructorFinished();
        this.player.setDeviceVolume(r2, r3);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setForegroundMode(boolean r2) {
        blockUntilConstructorFinished();
        this.player.setForegroundMode(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setHandleAudioBecomingNoisy(boolean r2) {
        blockUntilConstructorFinished();
        this.player.setHandleAudioBecomingNoisy(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setImageOutput(ImageOutput r2) {
        blockUntilConstructorFinished();
        this.player.setImageOutput(r2);
    }

    @Override // androidx.media3.common.Player
    public void setMediaItems(List<MediaItem> r2, int r3, long r4) {
        blockUntilConstructorFinished();
        this.player.setMediaItems(r2, r3, r4);
    }

    @Override // androidx.media3.common.Player
    public void setMediaItems(List<MediaItem> r2, boolean r3) {
        blockUntilConstructorFinished();
        this.player.setMediaItems(r2, r3);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setMediaSource(MediaSource r2) {
        blockUntilConstructorFinished();
        this.player.setMediaSource(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setMediaSource(MediaSource r2, long r3) {
        blockUntilConstructorFinished();
        this.player.setMediaSource(r2, r3);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setMediaSource(MediaSource r2, boolean r3) {
        blockUntilConstructorFinished();
        this.player.setMediaSource(r2, r3);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setMediaSources(List<MediaSource> r2) {
        blockUntilConstructorFinished();
        this.player.setMediaSources(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setMediaSources(List<MediaSource> r2, int r3, long r4) {
        blockUntilConstructorFinished();
        this.player.setMediaSources(r2, r3, r4);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setMediaSources(List<MediaSource> r2, boolean r3) {
        blockUntilConstructorFinished();
        this.player.setMediaSources(r2, r3);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setPauseAtEndOfMediaItems(boolean r2) {
        blockUntilConstructorFinished();
        this.player.setPauseAtEndOfMediaItems(r2);
    }

    @Override // androidx.media3.common.Player
    public void setPlayWhenReady(boolean r2) {
        blockUntilConstructorFinished();
        this.player.setPlayWhenReady(r2);
    }

    @Override // androidx.media3.common.Player
    public void setPlaybackParameters(PlaybackParameters r2) {
        blockUntilConstructorFinished();
        this.player.setPlaybackParameters(r2);
    }

    @Override // androidx.media3.common.Player
    public void setPlaylistMetadata(MediaMetadata r2) {
        blockUntilConstructorFinished();
        this.player.setPlaylistMetadata(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setPreferredAudioDevice(AudioDeviceInfo r2) {
        blockUntilConstructorFinished();
        this.player.setPreferredAudioDevice(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setPreloadConfiguration(ExoPlayer.PreloadConfiguration r2) {
        blockUntilConstructorFinished();
        this.player.setPreloadConfiguration(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setPriority(int r2) {
        blockUntilConstructorFinished();
        this.player.setPriority(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setPriorityTaskManager(PriorityTaskManager r2) {
        blockUntilConstructorFinished();
        this.player.setPriorityTaskManager(r2);
    }

    @Override // androidx.media3.common.Player
    public void setRepeatMode(int r2) {
        blockUntilConstructorFinished();
        this.player.setRepeatMode(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setSeekParameters(SeekParameters r2) {
        blockUntilConstructorFinished();
        this.player.setSeekParameters(r2);
    }

    @Override // androidx.media3.common.Player
    public void setShuffleModeEnabled(boolean r2) {
        blockUntilConstructorFinished();
        this.player.setShuffleModeEnabled(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setShuffleOrder(ShuffleOrder r2) {
        blockUntilConstructorFinished();
        this.player.setShuffleOrder(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.AudioComponent
    public void setSkipSilenceEnabled(boolean r2) {
        blockUntilConstructorFinished();
        this.player.setSkipSilenceEnabled(r2);
    }

    void setThrowsWhenUsingWrongThread(boolean r2) {
        blockUntilConstructorFinished();
        this.player.setThrowsWhenUsingWrongThread(r2);
    }

    @Override // androidx.media3.common.Player
    public void setTrackSelectionParameters(TrackSelectionParameters r2) {
        blockUntilConstructorFinished();
        this.player.setTrackSelectionParameters(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public void setVideoChangeFrameRateStrategy(int r2) {
        blockUntilConstructorFinished();
        this.player.setVideoChangeFrameRateStrategy(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setVideoEffects(List<Effect> r2) {
        blockUntilConstructorFinished();
        this.player.setVideoEffects(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public void setVideoFrameMetadataListener(VideoFrameMetadataListener r2) {
        blockUntilConstructorFinished();
        this.player.setVideoFrameMetadataListener(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public void setVideoScalingMode(int r2) {
        blockUntilConstructorFinished();
        this.player.setVideoScalingMode(r2);
    }

    @Override // androidx.media3.common.Player
    public void setVideoSurface(Surface r2) {
        blockUntilConstructorFinished();
        this.player.setVideoSurface(r2);
    }

    @Override // androidx.media3.common.Player
    public void setVideoSurfaceHolder(SurfaceHolder r2) {
        blockUntilConstructorFinished();
        this.player.setVideoSurfaceHolder(r2);
    }

    @Override // androidx.media3.common.Player
    public void setVideoSurfaceView(SurfaceView r2) {
        blockUntilConstructorFinished();
        this.player.setVideoSurfaceView(r2);
    }

    @Override // androidx.media3.common.Player
    public void setVideoTextureView(TextureView r2) {
        blockUntilConstructorFinished();
        this.player.setVideoTextureView(r2);
    }

    @Override // androidx.media3.common.Player
    public void setVolume(float r2) {
        blockUntilConstructorFinished();
        this.player.setVolume(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setWakeMode(int r2) {
        blockUntilConstructorFinished();
        this.player.setWakeMode(r2);
    }

    @Override // androidx.media3.common.Player
    public void stop() {
        blockUntilConstructorFinished();
        this.player.stop();
    }
}
