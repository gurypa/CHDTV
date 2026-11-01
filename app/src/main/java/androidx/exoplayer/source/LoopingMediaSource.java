package androidx.media3.exoplayer.source;

import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.exoplayer.AbstractConcatenatedTimeline;
import androidx.media3.exoplayer.upstream.Allocator;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class LoopingMediaSource extends WrappingMediaSource {
    private final Map<MediaSource.MediaPeriodId, MediaSource.MediaPeriodId> childMediaPeriodIdToMediaPeriodId;
    private final int loopCount;
    private final Map<MediaPeriod, MediaSource.MediaPeriodId> mediaPeriodToChildMediaPeriodId;

    private static final class InfinitelyLoopingTimeline extends ForwardingTimeline {
        public InfinitelyLoopingTimeline(Timeline r1) {
            super(r1);
        }

        @Override // androidx.media3.exoplayer.source.ForwardingTimeline, androidx.media3.common.Timeline
        public int getNextWindowIndex(int r3, int r4, boolean r5) {
            int r0 = this.timeline.getNextWindowIndex(r3, r4, r5);
            if (r0 != (-1)) goto L6;
            return getFirstWindowIndex(r5);
        L6:
            return r0;
        }

        @Override // androidx.media3.exoplayer.source.ForwardingTimeline, androidx.media3.common.Timeline
        public int getPreviousWindowIndex(int r3, int r4, boolean r5) {
            int r0 = this.timeline.getPreviousWindowIndex(r3, r4, r5);
            if (r0 != (-1)) goto L6;
            return getLastWindowIndex(r5);
        L6:
            return r0;
        }
    }

    private static final class LoopingTimeline extends AbstractConcatenatedTimeline {
        private final int childPeriodCount;
        private final Timeline childTimeline;
        private final int childWindowCount;
        private final int loopCount;

        public LoopingTimeline(Timeline r4, int r5) {
            boolean r1 = false;
            super(false, new ShuffleOrder.UnshuffledShuffleOrder(r5));
            this.childTimeline = r4;
            this.childPeriodCount = r4.getPeriodCount();
            this.childWindowCount = r4.getWindowCount();
            this.loopCount = r5;
            if (this.childPeriodCount > 0) goto L5;
            return;
        L5:
            if (r5 > (Integer.MAX_VALUE / this.childPeriodCount)) goto L7;
            r1 = true;
        L7:
            Assertions.checkState(r1, "LoopingMediaSource contains too many periods");
        }

        @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
        protected int getChildIndexByChildUid(Object r2) {
            if ((r2 instanceof Integer) == true) goto L7;
            return -1;
        L7:
            return ((Integer) r2).intValue();
        }

        @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
        protected int getChildIndexByPeriodIndex(int r2) {
            return r2 / this.childPeriodCount;
        }

        @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
        protected int getChildIndexByWindowIndex(int r2) {
            return r2 / this.childWindowCount;
        }

        @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
        protected Object getChildUidByChildIndex(int r2) {
            return Integer.valueOf(r2);
        }

        @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
        protected int getFirstPeriodIndexByChildIndex(int r2) {
            return this.childPeriodCount * r2;
        }

        @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
        protected int getFirstWindowIndexByChildIndex(int r2) {
            return this.childWindowCount * r2;
        }

        @Override // androidx.media3.common.Timeline
        public int getPeriodCount() {
            return this.childPeriodCount * this.loopCount;
        }

        @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
        protected Timeline getTimelineByChildIndex(int r2) {
            return this.childTimeline;
        }

        @Override // androidx.media3.common.Timeline
        public int getWindowCount() {
            return this.childWindowCount * this.loopCount;
        }
    }

    public LoopingMediaSource(MediaSource r2) {
        this(r2, Integer.MAX_VALUE);
    }

    public LoopingMediaSource(MediaSource r3, int r4) {
        boolean r1 = false;
        super(new MaskingMediaSource(r3, false));
        if (r4 <= 0) goto L5;
        r1 = true;
    L5:
        Assertions.checkArgument(r1);
        this.loopCount = r4;
        this.childMediaPeriodIdToMediaPeriodId = new HashMap();
        this.mediaPeriodToChildMediaPeriodId = new HashMap();
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r5, Allocator r6, long r7) {
        if (this.loopCount == Integer.MAX_VALUE) goto L5;
        MediaSource.MediaPeriodId r1 = r5.copyWithPeriodUid(LoopingTimeline.getChildPeriodUidFromConcatenatedUid(r5.periodUid));
        this.childMediaPeriodIdToMediaPeriodId.put(r1, r5);
        MediaPeriod r2 = this.mediaSource.createPeriod(r1, r6, r7);
        this.mediaPeriodToChildMediaPeriodId.put(r2, r1);
        return r2;
    L5:
        return this.mediaSource.createPeriod(r5, r6, r7);
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public Timeline getInitialTimeline() {
        MaskingMediaSource r0 = (MaskingMediaSource) this.mediaSource;
        if (this.loopCount == Integer.MAX_VALUE) goto L6;
        return new LoopingTimeline(r0.getTimeline(), this.loopCount);
    L6:
        return new InfinitelyLoopingTimeline(r0.getTimeline());
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource
    protected MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(MediaSource.MediaPeriodId r3) {
        if (this.loopCount == Integer.MAX_VALUE) goto L6;
        return this.childMediaPeriodIdToMediaPeriodId.get(r3);
    L6:
        return r3;
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean isSingleWindow() {
        return false;
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource
    protected void onChildSourceInfoRefreshed(Timeline r3) {
        if (this.loopCount == Integer.MAX_VALUE) goto L5;
        Timeline r0 = new LoopingTimeline(r3, this.loopCount);
    L6:
        refreshSourceInfo(r0);
        return;
    L5:
        r0 = new InfinitelyLoopingTimeline(r3);
        goto L6
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r3) {
        this.mediaSource.releasePeriod(r3);
        MediaSource.MediaPeriodId r0 = this.mediaPeriodToChildMediaPeriodId.remove(r3);
        if (r0 == null) goto L6;
        this.childMediaPeriodIdToMediaPeriodId.remove(r0);
        return;
    }
}
