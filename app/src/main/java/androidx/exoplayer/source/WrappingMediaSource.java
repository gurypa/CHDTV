package androidx.media3.exoplayer.source;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.upstream.Allocator;

/* JADX INFO: loaded from: classes.dex */
public abstract class WrappingMediaSource extends CompositeMediaSource<Void> {
    private static final Void CHILD_SOURCE_ID = null;
    protected final MediaSource mediaSource;

    static {
        CHILD_SOURCE_ID = null;
    }

    protected WrappingMediaSource(MediaSource r1) {
        this.mediaSource = r1;
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean canUpdateMediaItem(MediaItem r2) {
        return this.mediaSource.canUpdateMediaItem(r2);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r2, Allocator r3, long r4) {
        return this.mediaSource.createPeriod(r2, r3, r4);
    }

    protected final void disableChildSource() {
        disableChildSource(CHILD_SOURCE_ID);
    }

    protected final void enableChildSource() {
        enableChildSource(CHILD_SOURCE_ID);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public Timeline getInitialTimeline() {
        return this.mediaSource.getInitialTimeline();
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaItem getMediaItem() {
        return this.mediaSource.getMediaItem();
    }

    protected MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(MediaSource.MediaPeriodId r1) {
        return r1;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(Void r1, MediaSource.MediaPeriodId r2) {
        return getMediaPeriodIdForChildMediaPeriodId2(r1, r2);
    }

    /* JADX INFO: renamed from: getMediaPeriodIdForChildMediaPeriodId, reason: avoid collision after fix types in other method */
    protected final MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId2(Void r2, MediaSource.MediaPeriodId r3) {
        return getMediaPeriodIdForChildMediaPeriodId(r3);
    }

    protected long getMediaTimeForChildMediaTime(long r1, MediaSource.MediaPeriodId r3) {
        return r1;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ long getMediaTimeForChildMediaTime(Void r1, long r2, MediaSource.MediaPeriodId r4) {
        return getMediaTimeForChildMediaTime2(r1, r2, r4);
    }

    /* JADX INFO: renamed from: getMediaTimeForChildMediaTime, reason: avoid collision after fix types in other method */
    protected final long getMediaTimeForChildMediaTime2(Void r3, long r4, MediaSource.MediaPeriodId r6) {
        return getMediaTimeForChildMediaTime(r4, r6);
    }

    protected int getWindowIndexForChildWindowIndex(int r1) {
        return r1;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ int getWindowIndexForChildWindowIndex(Void r1, int r2) {
        return getWindowIndexForChildWindowIndex2(r1, r2);
    }

    /* JADX INFO: renamed from: getWindowIndexForChildWindowIndex, reason: avoid collision after fix types in other method */
    protected final int getWindowIndexForChildWindowIndex2(Void r2, int r3) {
        return getWindowIndexForChildWindowIndex(r3);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean isSingleWindow() {
        return this.mediaSource.isSingleWindow();
    }

    protected void onChildSourceInfoRefreshed(Timeline r1) {
        refreshSourceInfo(r1);
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ void onChildSourceInfoRefreshed(Void r1, MediaSource r2, Timeline r3) {
        onChildSourceInfoRefreshed2(r1, r2, r3);
    }

    /* JADX INFO: renamed from: onChildSourceInfoRefreshed, reason: avoid collision after fix types in other method */
    protected final void onChildSourceInfoRefreshed2(Void r1, MediaSource r2, Timeline r3) {
        onChildSourceInfoRefreshed(r3);
    }

    protected final void prepareChildSource() {
        prepareChildSource(CHILD_SOURCE_ID, this.mediaSource);
    }

    protected void prepareSourceInternal() {
        prepareChildSource();
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    protected final void prepareSourceInternal(TransferListener r1) {
        super.prepareSourceInternal(r1);
        prepareSourceInternal();
    }

    protected final void releaseChildSource() {
        releaseChildSource(CHILD_SOURCE_ID);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r2) {
        this.mediaSource.releasePeriod(r2);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public void updateMediaItem(MediaItem r2) {
        this.mediaSource.updateMediaItem(r2);
    }
}
