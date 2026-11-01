package androidx.media3.exoplayer.source;

import androidx.media3.common.Timeline;

/* JADX INFO: loaded from: classes.dex */
public abstract class ForwardingTimeline extends Timeline {
    protected final Timeline timeline;

    public ForwardingTimeline(Timeline r1) {
        this.timeline = r1;
    }

    @Override // androidx.media3.common.Timeline
    public int getFirstWindowIndex(boolean r2) {
        return this.timeline.getFirstWindowIndex(r2);
    }

    @Override // androidx.media3.common.Timeline
    public int getIndexOfPeriod(Object r2) {
        return this.timeline.getIndexOfPeriod(r2);
    }

    @Override // androidx.media3.common.Timeline
    public int getLastWindowIndex(boolean r2) {
        return this.timeline.getLastWindowIndex(r2);
    }

    @Override // androidx.media3.common.Timeline
    public int getNextWindowIndex(int r2, int r3, boolean r4) {
        return this.timeline.getNextWindowIndex(r2, r3, r4);
    }

    @Override // androidx.media3.common.Timeline
    public Timeline.Period getPeriod(int r2, Timeline.Period r3, boolean r4) {
        return this.timeline.getPeriod(r2, r3, r4);
    }

    @Override // androidx.media3.common.Timeline
    public int getPeriodCount() {
        return this.timeline.getPeriodCount();
    }

    @Override // androidx.media3.common.Timeline
    public int getPreviousWindowIndex(int r2, int r3, boolean r4) {
        return this.timeline.getPreviousWindowIndex(r2, r3, r4);
    }

    @Override // androidx.media3.common.Timeline
    public Object getUidOfPeriod(int r2) {
        return this.timeline.getUidOfPeriod(r2);
    }

    @Override // androidx.media3.common.Timeline
    public Timeline.Window getWindow(int r2, Timeline.Window r3, long r4) {
        return this.timeline.getWindow(r2, r3, r4);
    }

    @Override // androidx.media3.common.Timeline
    public int getWindowCount() {
        return this.timeline.getWindowCount();
    }
}
