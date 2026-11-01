package androidx.media3.common;

import androidx.media3.common.util.Util;
import com.google.common.collect.ImmutableList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class BasePlayer implements Player {
    protected final Timeline.Window window;

    protected BasePlayer() {
        this.window = new Timeline.Window();
    }

    private int getRepeatModeForNavigation() {
        int r0 = getRepeatMode();
        if (r0 != 1) goto L6;
        return 0;
    L6:
        return r0;
    }

    private void ignoreSeek(int r7) {
        seekTo(-1, C0180C.TIME_UNSET, r7, false);
    }

    private void repeatCurrentMediaItem(int r7) {
        seekTo(getCurrentMediaItemIndex(), C0180C.TIME_UNSET, r7, true);
    }

    private void seekToCurrentItem(long r7, int r9) {
        seekTo(getCurrentMediaItemIndex(), r7, r9, false);
    }

    private void seekToDefaultPositionInternal(int r7, int r8) {
        seekTo(r7, C0180C.TIME_UNSET, r8, false);
    }

    private void seekToNextMediaItemInternal(int r3) {
        int r0 = getNextMediaItemIndex();
        if (r0 != (-1)) goto L7;
        ignoreSeek(r3);
        return;
    L7:
        if (r0 != getCurrentMediaItemIndex()) goto L9;
        repeatCurrentMediaItem(r3);
        return;
    L9:
        seekToDefaultPositionInternal(r0, r3);
    }

    private void seekToOffset(long r7, int r9) {
        long r0 = getCurrentPosition() + r7;
        long r2 = getDuration();
        if (r2 == C0180C.TIME_UNSET) goto L5;
        r0 = Math.min(r0, r2);
    L5:
        seekToCurrentItem(Math.max(r0, 0), r9);
    }

    private void seekToPreviousMediaItemInternal(int r3) {
        int r0 = getPreviousMediaItemIndex();
        if (r0 != (-1)) goto L7;
        ignoreSeek(r3);
        return;
    L7:
        if (r0 != getCurrentMediaItemIndex()) goto L9;
        repeatCurrentMediaItem(r3);
        return;
    L9:
        seekToDefaultPositionInternal(r0, r3);
    }

    @Override // androidx.media3.common.Player
    public final void addMediaItem(int r2, MediaItem r3) {
        addMediaItems(r2, ImmutableList.m213of(r3));
    }

    @Override // androidx.media3.common.Player
    public final void addMediaItem(MediaItem r2) {
        addMediaItems(ImmutableList.m213of(r2));
    }

    @Override // androidx.media3.common.Player
    public final void addMediaItems(List<MediaItem> r2) {
        addMediaItems(Integer.MAX_VALUE, r2);
    }

    @Override // androidx.media3.common.Player
    public final boolean canAdvertiseSession() {
        return true;
    }

    @Override // androidx.media3.common.Player
    public final void clearMediaItems() {
        removeMediaItems(0, Integer.MAX_VALUE);
    }

    @Override // androidx.media3.common.Player
    public final int getBufferedPercentage() {
        long r0 = getBufferedPosition();
        long r2 = getDuration();
        if (r0 != C0180C.TIME_UNSET) goto L5;
        return 0;
    L5:
        if (r2 != C0180C.TIME_UNSET) goto L8;
        return 0;
    L8:
        if (r2 != 0) goto L13;
        return 100;
    L13:
        return Util.constrainValue((int) ((100 * r0) / r2), 0, 100);
    }

    @Override // androidx.media3.common.Player
    public final long getContentDuration() {
        Timeline r0 = getCurrentTimeline();
        if (r0.isEmpty() == false) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        return r0.getWindow(getCurrentMediaItemIndex(), this.window).getDurationMs();
    }

    @Override // androidx.media3.common.Player
    public final long getCurrentLiveOffset() {
        Timeline r0 = getCurrentTimeline();
        if (r0.isEmpty() == false) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        if (r0.getWindow(getCurrentMediaItemIndex(), this.window).windowStartTimeMs != C0180C.TIME_UNSET) goto L9;
        return C0180C.TIME_UNSET;
    L9:
        return (this.window.getCurrentUnixTimeMs() - this.window.windowStartTimeMs) - getContentPosition();
    }

    @Override // androidx.media3.common.Player
    public final Object getCurrentManifest() {
        Timeline r0 = getCurrentTimeline();
        if (r0.isEmpty() == false) goto L6;
        return null;
    L6:
        return r0.getWindow(getCurrentMediaItemIndex(), this.window).manifest;
    }

    @Override // androidx.media3.common.Player
    public final MediaItem getCurrentMediaItem() {
        Timeline r0 = getCurrentTimeline();
        if (r0.isEmpty() == false) goto L6;
        return null;
    L6:
        return r0.getWindow(getCurrentMediaItemIndex(), this.window).mediaItem;
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final int getCurrentWindowIndex() {
        return getCurrentMediaItemIndex();
    }

    @Override // androidx.media3.common.Player
    public final MediaItem getMediaItemAt(int r3) {
        return getCurrentTimeline().getWindow(r3, this.window).mediaItem;
    }

    @Override // androidx.media3.common.Player
    public final int getMediaItemCount() {
        return getCurrentTimeline().getWindowCount();
    }

    @Override // androidx.media3.common.Player
    public final int getNextMediaItemIndex() {
        Timeline r0 = getCurrentTimeline();
        if (r0.isEmpty() == false) goto L6;
        return -1;
    L6:
        return r0.getNextWindowIndex(getCurrentMediaItemIndex(), getRepeatModeForNavigation(), getShuffleModeEnabled());
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final int getNextWindowIndex() {
        return getNextMediaItemIndex();
    }

    @Override // androidx.media3.common.Player
    public final int getPreviousMediaItemIndex() {
        Timeline r0 = getCurrentTimeline();
        if (r0.isEmpty() == false) goto L6;
        return -1;
    L6:
        return r0.getPreviousWindowIndex(getCurrentMediaItemIndex(), getRepeatModeForNavigation(), getShuffleModeEnabled());
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final int getPreviousWindowIndex() {
        return getPreviousMediaItemIndex();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final boolean hasNext() {
        return hasNextMediaItem();
    }

    @Override // androidx.media3.common.Player
    public final boolean hasNextMediaItem() {
        if (getNextMediaItemIndex() == (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final boolean hasNextWindow() {
        return hasNextMediaItem();
    }

    @Override // androidx.media3.common.Player
    public final boolean hasPreviousMediaItem() {
        if (getPreviousMediaItemIndex() == (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.common.Player
    public final boolean isCommandAvailable(int r2) {
        return getAvailableCommands().contains(r2);
    }

    @Override // androidx.media3.common.Player
    public final boolean isCurrentMediaItemDynamic() {
        Timeline r0 = getCurrentTimeline();
        if (r0.isEmpty() == false) goto L5;
    L7:
        return false;
    L5:
        if (r0.getWindow(getCurrentMediaItemIndex(), this.window).isDynamic == false) goto L7;
        return true;
    }

    @Override // androidx.media3.common.Player
    public final boolean isCurrentMediaItemLive() {
        Timeline r0 = getCurrentTimeline();
        if (r0.isEmpty() == false) goto L5;
    L7:
        return false;
    L5:
        if (r0.getWindow(getCurrentMediaItemIndex(), this.window).isLive() == false) goto L7;
        return true;
    }

    @Override // androidx.media3.common.Player
    public final boolean isCurrentMediaItemSeekable() {
        Timeline r0 = getCurrentTimeline();
        if (r0.isEmpty() == false) goto L5;
    L7:
        return false;
    L5:
        if (r0.getWindow(getCurrentMediaItemIndex(), this.window).isSeekable == false) goto L7;
        return true;
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final boolean isCurrentWindowDynamic() {
        return isCurrentMediaItemDynamic();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final boolean isCurrentWindowLive() {
        return isCurrentMediaItemLive();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final boolean isCurrentWindowSeekable() {
        return isCurrentMediaItemSeekable();
    }

    @Override // androidx.media3.common.Player
    public final boolean isPlaying() {
        if (getPlaybackState() == 3) goto L5;
    L9:
        return false;
    L5:
        if (getPlayWhenReady() == false) goto L9;
        if (getPlaybackSuppressionReason() != 0) goto L9;
        return true;
    }

    @Override // androidx.media3.common.Player
    public final void moveMediaItem(int r2, int r3) {
        if (r2 == r3) goto L5;
        moveMediaItems(r2, r2 + 1, r3);
        return;
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final void next() {
        seekToNextMediaItem();
    }

    @Override // androidx.media3.common.Player
    public final void pause() {
        setPlayWhenReady(false);
    }

    @Override // androidx.media3.common.Player
    public final void play() {
        setPlayWhenReady(true);
    }

    @Override // androidx.media3.common.Player
    public final void removeMediaItem(int r2) {
        removeMediaItems(r2, r2 + 1);
    }

    @Override // androidx.media3.common.Player
    public final void replaceMediaItem(int r3, MediaItem r4) {
        replaceMediaItems(r3, r3 + 1, ImmutableList.m213of(r4));
    }

    @Override // androidx.media3.common.Player
    public final void seekBack() {
        seekToOffset(-getSeekBackIncrement(), 11);
    }

    @Override // androidx.media3.common.Player
    public final void seekForward() {
        seekToOffset(getSeekForwardIncrement(), 12);
    }

    @Override // androidx.media3.common.Player
    public final void seekTo(int r7, long r8) {
        seekTo(r7, r8, 10, false);
    }

    public abstract void seekTo(int r1, long r2, int r4, boolean r5);

    @Override // androidx.media3.common.Player
    public final void seekTo(long r2) {
        seekToCurrentItem(r2, 5);
    }

    @Override // androidx.media3.common.Player
    public final void seekToDefaultPosition() {
        seekToDefaultPositionInternal(getCurrentMediaItemIndex(), 4);
    }

    @Override // androidx.media3.common.Player
    public final void seekToDefaultPosition(int r2) {
        seekToDefaultPositionInternal(r2, 10);
    }

    @Override // androidx.media3.common.Player
    public final void seekToNext() {
        if (getCurrentTimeline().isEmpty() == false) goto L5;
    L17:
        ignoreSeek(9);
        return;
    L5:
        if (isPlayingAd() == true) goto L17;
        if (hasNextMediaItem() == false) goto L11;
        seekToNextMediaItemInternal(9);
        return;
    L11:
        if (isCurrentMediaItemLive() == true) goto L13;
    L15:
        ignoreSeek(9);
        return;
    L13:
        if (isCurrentMediaItemDynamic() == false) goto L15;
        seekToDefaultPositionInternal(getCurrentMediaItemIndex(), 9);
    }

    @Override // androidx.media3.common.Player
    public final void seekToNextMediaItem() {
        seekToNextMediaItemInternal(8);
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final void seekToNextWindow() {
        seekToNextMediaItem();
    }

    @Override // androidx.media3.common.Player
    public final void seekToPrevious() {
        if (getCurrentTimeline().isEmpty() == false) goto L5;
    L20:
        ignoreSeek(7);
        return;
    L5:
        if (isPlayingAd() == true) goto L20;
        boolean r1 = hasPreviousMediaItem();
        if (isCurrentMediaItemLive() == true) goto L10;
    L14:
        if (r1 == true) goto L16;
    L18:
        seekToCurrentItem(0, 7);
        return;
    L16:
        if (getCurrentPosition() > getMaxSeekToPreviousPosition()) goto L18;
        seekToPreviousMediaItemInternal(7);
        return;
    L10:
        if (isCurrentMediaItemSeekable() == true) goto L14;
        if (r1 == false) goto L13;
        seekToPreviousMediaItemInternal(7);
        return;
    L13:
        ignoreSeek(7);
    }

    @Override // androidx.media3.common.Player
    public final void seekToPreviousMediaItem() {
        seekToPreviousMediaItemInternal(6);
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final void seekToPreviousWindow() {
        seekToPreviousMediaItem();
    }

    @Override // androidx.media3.common.Player
    public final void setMediaItem(MediaItem r2) {
        setMediaItems(ImmutableList.m213of(r2));
    }

    @Override // androidx.media3.common.Player
    public final void setMediaItem(MediaItem r3, long r4) {
        setMediaItems(ImmutableList.m213of(r3), 0, r4);
    }

    @Override // androidx.media3.common.Player
    public final void setMediaItem(MediaItem r2, boolean r3) {
        setMediaItems(ImmutableList.m213of(r2), r3);
    }

    @Override // androidx.media3.common.Player
    public final void setMediaItems(List<MediaItem> r2) {
        setMediaItems(r2, true);
    }

    @Override // androidx.media3.common.Player
    public final void setPlaybackSpeed(float r2) {
        setPlaybackParameters(getPlaybackParameters().withSpeed(r2));
    }
}
