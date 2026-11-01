package androidx.media3.common;

import android.os.Looper;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import androidx.media3.common.text.Cue;
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.Size;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ForwardingPlayer implements Player {
    private final Player player;

    private static final class ForwardingListener implements Player.Listener {
        private final ForwardingPlayer forwardingPlayer;
        private final Player.Listener listener;

        public ForwardingListener(ForwardingPlayer r1, Player.Listener r2) {
            this.forwardingPlayer = r1;
            this.listener = r2;
        }

        public boolean equals(Object r5) {
            if (this != r5) goto L6;
            return true;
        L6:
            if ((r5 instanceof ForwardingListener) == true) goto L8;
            return false;
        L8:
            ForwardingListener r0 = (ForwardingListener) r5;
            if (this.forwardingPlayer.equals(r0.forwardingPlayer) == true) goto L12;
            return false;
        L12:
            return this.listener.equals(r0.listener);
        }

        public int hashCode() {
            return (this.forwardingPlayer.hashCode() * 31) + this.listener.hashCode();
        }

        @Override // androidx.media3.common.Player.Listener
        public void onAudioAttributesChanged(AudioAttributes r2) {
            this.listener.onAudioAttributesChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onAudioSessionIdChanged(int r2) {
            this.listener.onAudioSessionIdChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onAvailableCommandsChanged(Player.Commands r2) {
            this.listener.onAvailableCommandsChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onCues(CueGroup r2) {
            this.listener.onCues(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onCues(List<Cue> r2) {
            this.listener.onCues(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onDeviceInfoChanged(DeviceInfo r2) {
            this.listener.onDeviceInfoChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onDeviceVolumeChanged(int r2, boolean r3) {
            this.listener.onDeviceVolumeChanged(r2, r3);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onEvents(Player r3, Player.Events r4) {
            this.listener.onEvents(this.forwardingPlayer, r4);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onIsLoadingChanged(boolean r2) {
            this.listener.onIsLoadingChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onIsPlayingChanged(boolean r2) {
            this.listener.onIsPlayingChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onLoadingChanged(boolean r2) {
            this.listener.onIsLoadingChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onMaxSeekToPreviousPositionChanged(long r2) {
            this.listener.onMaxSeekToPreviousPositionChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onMediaItemTransition(MediaItem r2, int r3) {
            this.listener.onMediaItemTransition(r2, r3);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onMediaMetadataChanged(MediaMetadata r2) {
            this.listener.onMediaMetadataChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onMetadata(Metadata r2) {
            this.listener.onMetadata(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlayWhenReadyChanged(boolean r2, int r3) {
            this.listener.onPlayWhenReadyChanged(r2, r3);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlaybackParametersChanged(PlaybackParameters r2) {
            this.listener.onPlaybackParametersChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlaybackStateChanged(int r2) {
            this.listener.onPlaybackStateChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlaybackSuppressionReasonChanged(int r2) {
            this.listener.onPlaybackSuppressionReasonChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlayerError(PlaybackException r2) {
            this.listener.onPlayerError(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlayerErrorChanged(PlaybackException r2) {
            this.listener.onPlayerErrorChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlayerStateChanged(boolean r2, int r3) {
            this.listener.onPlayerStateChanged(r2, r3);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlaylistMetadataChanged(MediaMetadata r2) {
            this.listener.onPlaylistMetadataChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPositionDiscontinuity(int r2) {
            this.listener.onPositionDiscontinuity(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPositionDiscontinuity(Player.PositionInfo r2, Player.PositionInfo r3, int r4) {
            this.listener.onPositionDiscontinuity(r2, r3, r4);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onRenderedFirstFrame() {
            this.listener.onRenderedFirstFrame();
        }

        @Override // androidx.media3.common.Player.Listener
        public void onRepeatModeChanged(int r2) {
            this.listener.onRepeatModeChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onSeekBackIncrementChanged(long r2) {
            this.listener.onSeekBackIncrementChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onSeekForwardIncrementChanged(long r2) {
            this.listener.onSeekForwardIncrementChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onShuffleModeEnabledChanged(boolean r2) {
            this.listener.onShuffleModeEnabledChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onSkipSilenceEnabledChanged(boolean r2) {
            this.listener.onSkipSilenceEnabledChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onSurfaceSizeChanged(int r2, int r3) {
            this.listener.onSurfaceSizeChanged(r2, r3);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onTimelineChanged(Timeline r2, int r3) {
            this.listener.onTimelineChanged(r2, r3);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onTrackSelectionParametersChanged(TrackSelectionParameters r2) {
            this.listener.onTrackSelectionParametersChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onTracksChanged(Tracks r2) {
            this.listener.onTracksChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onVideoSizeChanged(VideoSize r2) {
            this.listener.onVideoSizeChanged(r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onVolumeChanged(float r2) {
            this.listener.onVolumeChanged(r2);
        }
    }

    public ForwardingPlayer(Player r1) {
        this.player = r1;
    }

    @Override // androidx.media3.common.Player
    public void addListener(Player.Listener r3) {
        this.player.addListener(new ForwardingListener(this, r3));
    }

    @Override // androidx.media3.common.Player
    public void addMediaItem(int r2, MediaItem r3) {
        this.player.addMediaItem(r2, r3);
    }

    @Override // androidx.media3.common.Player
    public void addMediaItem(MediaItem r2) {
        this.player.addMediaItem(r2);
    }

    @Override // androidx.media3.common.Player
    public void addMediaItems(int r2, List<MediaItem> r3) {
        this.player.addMediaItems(r2, r3);
    }

    @Override // androidx.media3.common.Player
    public void addMediaItems(List<MediaItem> r2) {
        this.player.addMediaItems(r2);
    }

    @Override // androidx.media3.common.Player
    public boolean canAdvertiseSession() {
        return this.player.canAdvertiseSession();
    }

    @Override // androidx.media3.common.Player
    public void clearMediaItems() {
        this.player.clearMediaItems();
    }

    @Override // androidx.media3.common.Player
    public void clearVideoSurface() {
        this.player.clearVideoSurface();
    }

    @Override // androidx.media3.common.Player
    public void clearVideoSurface(Surface r2) {
        this.player.clearVideoSurface(r2);
    }

    @Override // androidx.media3.common.Player
    public void clearVideoSurfaceHolder(SurfaceHolder r2) {
        this.player.clearVideoSurfaceHolder(r2);
    }

    @Override // androidx.media3.common.Player
    public void clearVideoSurfaceView(SurfaceView r2) {
        this.player.clearVideoSurfaceView(r2);
    }

    @Override // androidx.media3.common.Player
    public void clearVideoTextureView(TextureView r2) {
        this.player.clearVideoTextureView(r2);
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void decreaseDeviceVolume() {
        this.player.decreaseDeviceVolume();
    }

    @Override // androidx.media3.common.Player
    public void decreaseDeviceVolume(int r2) {
        this.player.decreaseDeviceVolume(r2);
    }

    @Override // androidx.media3.common.Player
    public Looper getApplicationLooper() {
        return this.player.getApplicationLooper();
    }

    @Override // androidx.media3.common.Player
    public AudioAttributes getAudioAttributes() {
        return this.player.getAudioAttributes();
    }

    @Override // androidx.media3.common.Player
    public Player.Commands getAvailableCommands() {
        return this.player.getAvailableCommands();
    }

    @Override // androidx.media3.common.Player
    public int getBufferedPercentage() {
        return this.player.getBufferedPercentage();
    }

    @Override // androidx.media3.common.Player
    public long getBufferedPosition() {
        return this.player.getBufferedPosition();
    }

    @Override // androidx.media3.common.Player
    public long getContentBufferedPosition() {
        return this.player.getContentBufferedPosition();
    }

    @Override // androidx.media3.common.Player
    public long getContentDuration() {
        return this.player.getContentDuration();
    }

    @Override // androidx.media3.common.Player
    public long getContentPosition() {
        return this.player.getContentPosition();
    }

    @Override // androidx.media3.common.Player
    public int getCurrentAdGroupIndex() {
        return this.player.getCurrentAdGroupIndex();
    }

    @Override // androidx.media3.common.Player
    public int getCurrentAdIndexInAdGroup() {
        return this.player.getCurrentAdIndexInAdGroup();
    }

    @Override // androidx.media3.common.Player
    public CueGroup getCurrentCues() {
        return this.player.getCurrentCues();
    }

    @Override // androidx.media3.common.Player
    public long getCurrentLiveOffset() {
        return this.player.getCurrentLiveOffset();
    }

    @Override // androidx.media3.common.Player
    public Object getCurrentManifest() {
        return this.player.getCurrentManifest();
    }

    @Override // androidx.media3.common.Player
    public MediaItem getCurrentMediaItem() {
        return this.player.getCurrentMediaItem();
    }

    @Override // androidx.media3.common.Player
    public int getCurrentMediaItemIndex() {
        return this.player.getCurrentMediaItemIndex();
    }

    @Override // androidx.media3.common.Player
    public int getCurrentPeriodIndex() {
        return this.player.getCurrentPeriodIndex();
    }

    @Override // androidx.media3.common.Player
    public long getCurrentPosition() {
        return this.player.getCurrentPosition();
    }

    @Override // androidx.media3.common.Player
    public Timeline getCurrentTimeline() {
        return this.player.getCurrentTimeline();
    }

    @Override // androidx.media3.common.Player
    public Tracks getCurrentTracks() {
        return this.player.getCurrentTracks();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public int getCurrentWindowIndex() {
        return this.player.getCurrentWindowIndex();
    }

    @Override // androidx.media3.common.Player
    public DeviceInfo getDeviceInfo() {
        return this.player.getDeviceInfo();
    }

    @Override // androidx.media3.common.Player
    public int getDeviceVolume() {
        return this.player.getDeviceVolume();
    }

    @Override // androidx.media3.common.Player
    public long getDuration() {
        return this.player.getDuration();
    }

    @Override // androidx.media3.common.Player
    public long getMaxSeekToPreviousPosition() {
        return this.player.getMaxSeekToPreviousPosition();
    }

    @Override // androidx.media3.common.Player
    public MediaItem getMediaItemAt(int r2) {
        return this.player.getMediaItemAt(r2);
    }

    @Override // androidx.media3.common.Player
    public int getMediaItemCount() {
        return this.player.getMediaItemCount();
    }

    @Override // androidx.media3.common.Player
    public MediaMetadata getMediaMetadata() {
        return this.player.getMediaMetadata();
    }

    @Override // androidx.media3.common.Player
    public int getNextMediaItemIndex() {
        return this.player.getNextMediaItemIndex();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public int getNextWindowIndex() {
        return this.player.getNextWindowIndex();
    }

    @Override // androidx.media3.common.Player
    public boolean getPlayWhenReady() {
        return this.player.getPlayWhenReady();
    }

    @Override // androidx.media3.common.Player
    public PlaybackParameters getPlaybackParameters() {
        return this.player.getPlaybackParameters();
    }

    @Override // androidx.media3.common.Player
    public int getPlaybackState() {
        return this.player.getPlaybackState();
    }

    @Override // androidx.media3.common.Player
    public int getPlaybackSuppressionReason() {
        return this.player.getPlaybackSuppressionReason();
    }

    @Override // androidx.media3.common.Player
    public PlaybackException getPlayerError() {
        return this.player.getPlayerError();
    }

    @Override // androidx.media3.common.Player
    public MediaMetadata getPlaylistMetadata() {
        return this.player.getPlaylistMetadata();
    }

    @Override // androidx.media3.common.Player
    public int getPreviousMediaItemIndex() {
        return this.player.getPreviousMediaItemIndex();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public int getPreviousWindowIndex() {
        return this.player.getPreviousWindowIndex();
    }

    @Override // androidx.media3.common.Player
    public int getRepeatMode() {
        return this.player.getRepeatMode();
    }

    @Override // androidx.media3.common.Player
    public long getSeekBackIncrement() {
        return this.player.getSeekBackIncrement();
    }

    @Override // androidx.media3.common.Player
    public long getSeekForwardIncrement() {
        return this.player.getSeekForwardIncrement();
    }

    @Override // androidx.media3.common.Player
    public boolean getShuffleModeEnabled() {
        return this.player.getShuffleModeEnabled();
    }

    @Override // androidx.media3.common.Player
    public Size getSurfaceSize() {
        return this.player.getSurfaceSize();
    }

    @Override // androidx.media3.common.Player
    public long getTotalBufferedDuration() {
        return this.player.getTotalBufferedDuration();
    }

    @Override // androidx.media3.common.Player
    public TrackSelectionParameters getTrackSelectionParameters() {
        return this.player.getTrackSelectionParameters();
    }

    @Override // androidx.media3.common.Player
    public VideoSize getVideoSize() {
        return this.player.getVideoSize();
    }

    @Override // androidx.media3.common.Player
    public float getVolume() {
        return this.player.getVolume();
    }

    public Player getWrappedPlayer() {
        return this.player;
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public boolean hasNext() {
        return this.player.hasNext();
    }

    @Override // androidx.media3.common.Player
    public boolean hasNextMediaItem() {
        return this.player.hasNextMediaItem();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public boolean hasNextWindow() {
        return this.player.hasNextWindow();
    }

    @Override // androidx.media3.common.Player
    public boolean hasPreviousMediaItem() {
        return this.player.hasPreviousMediaItem();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void increaseDeviceVolume() {
        this.player.increaseDeviceVolume();
    }

    @Override // androidx.media3.common.Player
    public void increaseDeviceVolume(int r2) {
        this.player.increaseDeviceVolume(r2);
    }

    @Override // androidx.media3.common.Player
    public boolean isCommandAvailable(int r2) {
        return this.player.isCommandAvailable(r2);
    }

    @Override // androidx.media3.common.Player
    public boolean isCurrentMediaItemDynamic() {
        return this.player.isCurrentMediaItemDynamic();
    }

    @Override // androidx.media3.common.Player
    public boolean isCurrentMediaItemLive() {
        return this.player.isCurrentMediaItemLive();
    }

    @Override // androidx.media3.common.Player
    public boolean isCurrentMediaItemSeekable() {
        return this.player.isCurrentMediaItemSeekable();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public boolean isCurrentWindowDynamic() {
        return this.player.isCurrentWindowDynamic();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public boolean isCurrentWindowLive() {
        return this.player.isCurrentWindowLive();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public boolean isCurrentWindowSeekable() {
        return this.player.isCurrentWindowSeekable();
    }

    @Override // androidx.media3.common.Player
    public boolean isDeviceMuted() {
        return this.player.isDeviceMuted();
    }

    @Override // androidx.media3.common.Player
    public boolean isLoading() {
        return this.player.isLoading();
    }

    @Override // androidx.media3.common.Player
    public boolean isPlaying() {
        return this.player.isPlaying();
    }

    @Override // androidx.media3.common.Player
    public boolean isPlayingAd() {
        return this.player.isPlayingAd();
    }

    @Override // androidx.media3.common.Player
    public void moveMediaItem(int r2, int r3) {
        this.player.moveMediaItem(r2, r3);
    }

    @Override // androidx.media3.common.Player
    public void moveMediaItems(int r2, int r3, int r4) {
        this.player.moveMediaItems(r2, r3, r4);
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void next() {
        this.player.next();
    }

    @Override // androidx.media3.common.Player
    public void pause() {
        this.player.pause();
    }

    @Override // androidx.media3.common.Player
    public void play() {
        this.player.play();
    }

    @Override // androidx.media3.common.Player
    public void prepare() {
        this.player.prepare();
    }

    @Override // androidx.media3.common.Player
    public void release() {
        this.player.release();
    }

    @Override // androidx.media3.common.Player
    public void removeListener(Player.Listener r3) {
        this.player.removeListener(new ForwardingListener(this, r3));
    }

    @Override // androidx.media3.common.Player
    public void removeMediaItem(int r2) {
        this.player.removeMediaItem(r2);
    }

    @Override // androidx.media3.common.Player
    public void removeMediaItems(int r2, int r3) {
        this.player.removeMediaItems(r2, r3);
    }

    @Override // androidx.media3.common.Player
    public void replaceMediaItem(int r2, MediaItem r3) {
        this.player.replaceMediaItem(r2, r3);
    }

    @Override // androidx.media3.common.Player
    public void replaceMediaItems(int r2, int r3, List<MediaItem> r4) {
        this.player.replaceMediaItems(r2, r3, r4);
    }

    @Override // androidx.media3.common.Player
    public void seekBack() {
        this.player.seekBack();
    }

    @Override // androidx.media3.common.Player
    public void seekForward() {
        this.player.seekForward();
    }

    @Override // androidx.media3.common.Player
    public void seekTo(int r2, long r3) {
        this.player.seekTo(r2, r3);
    }

    @Override // androidx.media3.common.Player
    public void seekTo(long r2) {
        this.player.seekTo(r2);
    }

    @Override // androidx.media3.common.Player
    public void seekToDefaultPosition() {
        this.player.seekToDefaultPosition();
    }

    @Override // androidx.media3.common.Player
    public void seekToDefaultPosition(int r2) {
        this.player.seekToDefaultPosition(r2);
    }

    @Override // androidx.media3.common.Player
    public void seekToNext() {
        this.player.seekToNext();
    }

    @Override // androidx.media3.common.Player
    public void seekToNextMediaItem() {
        this.player.seekToNextMediaItem();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void seekToNextWindow() {
        this.player.seekToNextWindow();
    }

    @Override // androidx.media3.common.Player
    public void seekToPrevious() {
        this.player.seekToPrevious();
    }

    @Override // androidx.media3.common.Player
    public void seekToPreviousMediaItem() {
        this.player.seekToPreviousMediaItem();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void seekToPreviousWindow() {
        this.player.seekToPreviousWindow();
    }

    @Override // androidx.media3.common.Player
    public void setAudioAttributes(AudioAttributes r2, boolean r3) {
        this.player.setAudioAttributes(r2, r3);
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void setDeviceMuted(boolean r2) {
        this.player.setDeviceMuted(r2);
    }

    @Override // androidx.media3.common.Player
    public void setDeviceMuted(boolean r2, int r3) {
        this.player.setDeviceMuted(r2, r3);
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void setDeviceVolume(int r2) {
        this.player.setDeviceVolume(r2);
    }

    @Override // androidx.media3.common.Player
    public void setDeviceVolume(int r2, int r3) {
        this.player.setDeviceVolume(r2, r3);
    }

    @Override // androidx.media3.common.Player
    public void setMediaItem(MediaItem r2) {
        this.player.setMediaItem(r2);
    }

    @Override // androidx.media3.common.Player
    public void setMediaItem(MediaItem r2, long r3) {
        this.player.setMediaItem(r2, r3);
    }

    @Override // androidx.media3.common.Player
    public void setMediaItem(MediaItem r2, boolean r3) {
        this.player.setMediaItem(r2, r3);
    }

    @Override // androidx.media3.common.Player
    public void setMediaItems(List<MediaItem> r2) {
        this.player.setMediaItems(r2);
    }

    @Override // androidx.media3.common.Player
    public void setMediaItems(List<MediaItem> r2, int r3, long r4) {
        this.player.setMediaItems(r2, r3, r4);
    }

    @Override // androidx.media3.common.Player
    public void setMediaItems(List<MediaItem> r2, boolean r3) {
        this.player.setMediaItems(r2, r3);
    }

    @Override // androidx.media3.common.Player
    public void setPlayWhenReady(boolean r2) {
        this.player.setPlayWhenReady(r2);
    }

    @Override // androidx.media3.common.Player
    public void setPlaybackParameters(PlaybackParameters r2) {
        this.player.setPlaybackParameters(r2);
    }

    @Override // androidx.media3.common.Player
    public void setPlaybackSpeed(float r2) {
        this.player.setPlaybackSpeed(r2);
    }

    @Override // androidx.media3.common.Player
    public void setPlaylistMetadata(MediaMetadata r2) {
        this.player.setPlaylistMetadata(r2);
    }

    @Override // androidx.media3.common.Player
    public void setRepeatMode(int r2) {
        this.player.setRepeatMode(r2);
    }

    @Override // androidx.media3.common.Player
    public void setShuffleModeEnabled(boolean r2) {
        this.player.setShuffleModeEnabled(r2);
    }

    @Override // androidx.media3.common.Player
    public void setTrackSelectionParameters(TrackSelectionParameters r2) {
        this.player.setTrackSelectionParameters(r2);
    }

    @Override // androidx.media3.common.Player
    public void setVideoSurface(Surface r2) {
        this.player.setVideoSurface(r2);
    }

    @Override // androidx.media3.common.Player
    public void setVideoSurfaceHolder(SurfaceHolder r2) {
        this.player.setVideoSurfaceHolder(r2);
    }

    @Override // androidx.media3.common.Player
    public void setVideoSurfaceView(SurfaceView r2) {
        this.player.setVideoSurfaceView(r2);
    }

    @Override // androidx.media3.common.Player
    public void setVideoTextureView(TextureView r2) {
        this.player.setVideoTextureView(r2);
    }

    @Override // androidx.media3.common.Player
    public void setVolume(float r2) {
        this.player.setVolume(r2);
    }

    @Override // androidx.media3.common.Player
    public void stop() {
        this.player.stop();
    }
}
