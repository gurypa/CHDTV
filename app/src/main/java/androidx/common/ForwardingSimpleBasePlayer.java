package androidx.media3.common;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class ForwardingSimpleBasePlayer extends SimpleBasePlayer {
    private ForwardingPositionSupplier currentPositionSupplier;
    private Metadata lastTimedMetadata;
    private int pendingDiscontinuityReason;
    private boolean pendingFirstFrameRendered;
    private long pendingPositionDiscontinuityNewPositionMs;
    private int playWhenReadyChangeReason;
    private final Player player;

    private static final class ForwardingPositionSupplier {
        private long contentPositionMs;
        private final Player player;
        private long positionsMs;

        public ForwardingPositionSupplier(Player r3) {
            this.player = r3;
            this.positionsMs = C0180C.TIME_UNSET;
            this.contentPositionMs = C0180C.TIME_UNSET;
        }

        public long getBufferedPositionMs() {
            if (this.positionsMs != C0180C.TIME_UNSET) goto L6;
            return this.player.getBufferedPosition();
        L6:
            return this.positionsMs;
        }

        public long getContentBufferedPositionMs() {
            if (this.contentPositionMs != C0180C.TIME_UNSET) goto L6;
            return this.player.getContentBufferedPosition();
        L6:
            return this.contentPositionMs;
        }

        public long getContentPositionMs() {
            if (this.contentPositionMs != C0180C.TIME_UNSET) goto L6;
            return this.player.getContentPosition();
        L6:
            return this.contentPositionMs;
        }

        public long getCurrentPositionMs() {
            if (this.positionsMs != C0180C.TIME_UNSET) goto L6;
            return this.player.getCurrentPosition();
        L6:
            return this.positionsMs;
        }

        public long getTotalBufferedDurationMs() {
            if (this.positionsMs == C0180C.TIME_UNSET) goto L7;
            return 0;
        L7:
            return this.player.getTotalBufferedDuration();
        }

        public void setConstant(long r1, long r3) {
            this.positionsMs = r1;
            this.contentPositionMs = r3;
        }
    }

    public ForwardingSimpleBasePlayer(final Player r5) {
        super(r5.getApplicationLooper());
        this.player = r5;
        this.lastTimedMetadata = new Metadata(C0180C.TIME_UNSET, new Metadata.Entry[0]);
        this.playWhenReadyChangeReason = 1;
        this.pendingDiscontinuityReason = 5;
        this.currentPositionSupplier = new ForwardingPositionSupplier(r5);
        r5.addListener(new C01871(this, r5));
    }

    static /* synthetic */ Metadata access$002(ForwardingSimpleBasePlayer r0, Metadata r1) {
        r0.lastTimedMetadata = r1;
        return r1;
    }

    static /* synthetic */ int access$102(ForwardingSimpleBasePlayer r0, int r1) {
        r0.playWhenReadyChangeReason = r1;
        return r1;
    }

    static /* synthetic */ int access$202(ForwardingSimpleBasePlayer r0, int r1) {
        r0.pendingDiscontinuityReason = r1;
        return r1;
    }

    static /* synthetic */ long access$302(ForwardingSimpleBasePlayer r0, long r1) {
        r0.pendingPositionDiscontinuityNewPositionMs = r1;
        return r1;
    }

    static /* synthetic */ ForwardingPositionSupplier access$400(ForwardingSimpleBasePlayer r1) {
        return r1.currentPositionSupplier;
    }

    static /* synthetic */ ForwardingPositionSupplier access$402(ForwardingSimpleBasePlayer r0, ForwardingPositionSupplier r1) {
        r0.currentPositionSupplier = r1;
        return r1;
    }

    static /* synthetic */ boolean access$502(ForwardingSimpleBasePlayer r0, boolean r1) {
        r0.pendingFirstFrameRendered = r1;
        return r1;
    }

    protected final Player getPlayer() {
        return this.player;
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected SimpleBasePlayer.State getState() {
        SimpleBasePlayer.State.Builder r0 = new SimpleBasePlayer.State.Builder();
        final ForwardingPositionSupplier r1 = this.currentPositionSupplier;
        if (this.player.isCommandAvailable(16) == false) goto L6;
        Objects.requireNonNull(r1);
        r0.setAdBufferedPositionMs(new ForwardingSimpleBasePlayer$$ExternalSyntheticLambda0(r1));
        Objects.requireNonNull(r1);
        r0.setAdPositionMs(new ForwardingSimpleBasePlayer$$ExternalSyntheticLambda1(r1));
    L6:
        if (this.player.isCommandAvailable(21) == false) goto L8;
        r0.setAudioAttributes(this.player.getAudioAttributes());
    L8:
        r0.setAvailableCommands(this.player.getAvailableCommands());
        if (this.player.isCommandAvailable(16) == false) goto L14;
        Objects.requireNonNull(r1);
        r0.setContentBufferedPositionMs(new ForwardingSimpleBasePlayer$$ExternalSyntheticLambda2(r1));
        Objects.requireNonNull(r1);
        r0.setContentPositionMs(new ForwardingSimpleBasePlayer$$ExternalSyntheticLambda3(r1));
        if (this.player.isCommandAvailable(17) == false) goto L14;
        r0.setCurrentAd(this.player.getCurrentAdGroupIndex(), this.player.getCurrentAdIndexInAdGroup());
    L14:
        if (this.player.isCommandAvailable(28) == false) goto L17;
        r0.setCurrentCues(this.player.getCurrentCues());
    L17:
        if (this.player.isCommandAvailable(17) == false) goto L19;
        r0.setCurrentMediaItemIndex(this.player.getCurrentMediaItemIndex());
    L19:
        r0.setDeviceInfo(this.player.getDeviceInfo());
        if (this.player.isCommandAvailable(23) == false) goto L22;
        r0.setDeviceVolume(this.player.getDeviceVolume());
        r0.setIsDeviceMuted(this.player.isDeviceMuted());
    L22:
        r0.setIsLoading(this.player.isLoading());
        r0.setMaxSeekToPreviousPositionMs(this.player.getMaxSeekToPreviousPosition());
        if (this.pendingFirstFrameRendered == false) goto L25;
        r0.setNewlyRenderedFirstFrame(true);
        this.pendingFirstFrameRendered = false;
    L25:
        r0.setPlaybackParameters(this.player.getPlaybackParameters());
        r0.setPlaybackState(this.player.getPlaybackState());
        r0.setPlaybackSuppressionReason(this.player.getPlaybackSuppressionReason());
        r0.setPlayerError(this.player.getPlayerError());
        if (this.player.isCommandAvailable(17) == false) goto L37;
        if (this.player.isCommandAvailable(30) == false) goto L30;
        Tracks r2 = this.player.getCurrentTracks();
    L32:
        if (this.player.isCommandAvailable(18) == false) goto L34;
        MediaMetadata r5 = this.player.getMediaMetadata();
    L35:
        r0.setPlaylist(this.player.getCurrentTimeline(), r2, r5);
        goto L37
    L34:
        r5 = null;
        goto L35
    L30:
        r2 = Tracks.EMPTY;
    L37:
        if (this.player.isCommandAvailable(18) == false) goto L39;
        r0.setPlaylistMetadata(this.player.getPlaylistMetadata());
    L39:
        r0.setPlayWhenReady(this.player.getPlayWhenReady(), this.playWhenReadyChangeReason);
        if (this.pendingPositionDiscontinuityNewPositionMs == C0180C.TIME_UNSET) goto L42;
        r0.setPositionDiscontinuity(this.pendingDiscontinuityReason, this.pendingPositionDiscontinuityNewPositionMs);
        this.pendingPositionDiscontinuityNewPositionMs = C0180C.TIME_UNSET;
    L42:
        r0.setRepeatMode(this.player.getRepeatMode());
        r0.setSeekBackIncrementMs(this.player.getSeekBackIncrement());
        r0.setSeekForwardIncrementMs(this.player.getSeekForwardIncrement());
        r0.setShuffleModeEnabled(this.player.getShuffleModeEnabled());
        r0.setSurfaceSize(this.player.getSurfaceSize());
        r0.setTimedMetadata(this.lastTimedMetadata);
        if (this.player.isCommandAvailable(16) == false) goto L45;
        Objects.requireNonNull(r1);
        r0.setTotalBufferedDurationMs(new ForwardingSimpleBasePlayer$$ExternalSyntheticLambda4(r1));
    L45:
        r0.setTrackSelectionParameters(this.player.getTrackSelectionParameters());
        r0.setVideoSize(this.player.getVideoSize());
        if (this.player.isCommandAvailable(22) == false) goto L49;
        r0.setVolume(this.player.getVolume());
    L49:
        return r0.build();
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleAddMediaItems(int r3, List<MediaItem> r4) {
        if (r4.size() != 1) goto L5;
        this.player.addMediaItem(r3, r4.get(0));
    L7:
        return Futures.immediateVoidFuture();
    L5:
        this.player.addMediaItems(r3, r4);
        goto L7
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleClearVideoOutput(Object r3) {
        if ((r3 instanceof SurfaceView) == false) goto L6;
        this.player.clearVideoSurfaceView((SurfaceView) r3);
    L17:
        return Futures.immediateVoidFuture();
    L6:
        if ((r3 instanceof TextureView) == false) goto L9;
        this.player.clearVideoTextureView((TextureView) r3);
        goto L17
    L9:
        if ((r3 instanceof SurfaceHolder) == false) goto L12;
        this.player.clearVideoSurfaceHolder((SurfaceHolder) r3);
        goto L17
    L12:
        if ((r3 instanceof Surface) == false) goto L14;
        this.player.clearVideoSurface((Surface) r3);
        goto L17
    L14:
        if (r3 != null) goto L19;
        this.player.clearVideoSurface();
        goto L17
    L19:
        throw new IllegalStateException();
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleDecreaseDeviceVolume(int r3) {
        if (this.player.isCommandAvailable(34) == false) goto L5;
        this.player.decreaseDeviceVolume(r3);
    L7:
        return Futures.immediateVoidFuture();
    L5:
        this.player.decreaseDeviceVolume();
        goto L7
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleIncreaseDeviceVolume(int r3) {
        if (this.player.isCommandAvailable(34) == false) goto L5;
        this.player.increaseDeviceVolume(r3);
    L7:
        return Futures.immediateVoidFuture();
    L5:
        this.player.increaseDeviceVolume();
        goto L7
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleMoveMediaItems(int r2, int r3, int r4) {
        if (r3 != (r2 + 1)) goto L5;
        this.player.moveMediaItem(r2, r4);
    L7:
        return Futures.immediateVoidFuture();
    L5:
        this.player.moveMediaItems(r2, r3, r4);
        goto L7
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handlePrepare() {
        this.player.prepare();
        return Futures.immediateVoidFuture();
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleRelease() {
        this.player.release();
        return Futures.immediateVoidFuture();
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleRemoveMediaItems(int r2, int r3) {
        if (r3 != (r2 + 1)) goto L5;
        this.player.removeMediaItem(r2);
    L7:
        return Futures.immediateVoidFuture();
    L5:
        this.player.removeMediaItems(r2, r3);
        goto L7
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleReplaceMediaItems(int r3, int r4, List<MediaItem> r5) {
        if (r4 == (r3 + 1)) goto L5;
    L7:
        this.player.replaceMediaItems(r3, r4, r5);
    L9:
        return Futures.immediateVoidFuture();
    L5:
        if (r5.size() != 1) goto L7;
        this.player.replaceMediaItem(r3, r5.get(0));
        goto L9
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleSeek(int r2, long r3, int r5) {
        switch(r5) {
            case 4: goto L15;
            case 5: goto L14;
            case 6: goto L13;
            case 7: goto L12;
            case 8: goto L11;
            case 9: goto L10;
            case 10: goto L8;
            case 11: goto L6;
            case 12: goto L5;
            default: goto L4;
        };
    L5:
        this.player.seekForward();
    L17:
        return Futures.immediateVoidFuture();
    L6:
        this.player.seekBack();
        goto L17
    L10:
        this.player.seekToNext();
        goto L17
    L11:
        this.player.seekToNextMediaItem();
        goto L17
    L12:
        this.player.seekToPrevious();
        goto L17
    L13:
        this.player.seekToPreviousMediaItem();
        goto L17
    L14:
        this.player.seekTo(r3);
        goto L17
    L15:
        this.player.seekToDefaultPosition();
        goto L17
    L4:
        throw new IllegalStateException();
    L8:
        if (r2 == (-1)) goto L17;
        this.player.seekTo(r2, r3);
        goto L17
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleSetAudioAttributes(AudioAttributes r2, boolean r3) {
        this.player.setAudioAttributes(r2, r3);
        return Futures.immediateVoidFuture();
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleSetDeviceMuted(boolean r3, int r4) {
        if (this.player.isCommandAvailable(34) == false) goto L5;
        this.player.setDeviceMuted(r3, r4);
    L7:
        return Futures.immediateVoidFuture();
    L5:
        this.player.setDeviceMuted(r3);
        goto L7
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleSetDeviceVolume(int r3, int r4) {
        if (this.player.isCommandAvailable(33) == false) goto L5;
        this.player.setDeviceVolume(r3, r4);
    L7:
        return Futures.immediateVoidFuture();
    L5:
        this.player.setDeviceVolume(r3);
        goto L7
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleSetMediaItems(List<MediaItem> r5, int r6, long r7) {
        boolean r2 = true;
        if (r5.size() == 1) goto L5;
    L7:
        r2 = false;
    L9:
        if (r6 != (-1)) goto L13;
        if (r2 == false) goto L12;
        this.player.setMediaItem(r5.get(0));
    L17:
        return Futures.immediateVoidFuture();
    L12:
        this.player.setMediaItems(r5);
        goto L17
    L13:
        if (r2 == false) goto L15;
        this.player.setMediaItem(r5.get(0), r7);
        goto L17
    L15:
        this.player.setMediaItems(r5, r6, r7);
        goto L17
    L5:
        if (this.player.isCommandAvailable(31) == false) goto L7;
        goto L7
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleSetPlayWhenReady(boolean r2) {
        this.player.setPlayWhenReady(r2);
        return Futures.immediateVoidFuture();
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleSetPlaybackParameters(PlaybackParameters r2) {
        this.player.setPlaybackParameters(r2);
        return Futures.immediateVoidFuture();
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleSetPlaylistMetadata(MediaMetadata r2) {
        this.player.setPlaylistMetadata(r2);
        return Futures.immediateVoidFuture();
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleSetRepeatMode(int r2) {
        this.player.setRepeatMode(r2);
        return Futures.immediateVoidFuture();
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleSetShuffleModeEnabled(boolean r2) {
        this.player.setShuffleModeEnabled(r2);
        return Futures.immediateVoidFuture();
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleSetTrackSelectionParameters(TrackSelectionParameters r2) {
        this.player.setTrackSelectionParameters(r2);
        return Futures.immediateVoidFuture();
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleSetVideoOutput(Object r3) {
        if ((r3 instanceof SurfaceView) == false) goto L6;
        this.player.setVideoSurfaceView((SurfaceView) r3);
    L15:
        return Futures.immediateVoidFuture();
    L6:
        if ((r3 instanceof TextureView) == false) goto L9;
        this.player.setVideoTextureView((TextureView) r3);
        goto L15
    L9:
        if ((r3 instanceof SurfaceHolder) == false) goto L12;
        this.player.setVideoSurfaceHolder((SurfaceHolder) r3);
        goto L15
    L12:
        if ((r3 instanceof Surface) == false) goto L17;
        this.player.setVideoSurface((Surface) r3);
        goto L15
    L17:
        throw new IllegalStateException();
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleSetVolume(float r2) {
        this.player.setVolume(r2);
        return Futures.immediateVoidFuture();
    }

    @Override // androidx.media3.common.SimpleBasePlayer
    protected ListenableFuture<?> handleStop() {
        this.player.stop();
        return Futures.immediateVoidFuture();
    }
}
