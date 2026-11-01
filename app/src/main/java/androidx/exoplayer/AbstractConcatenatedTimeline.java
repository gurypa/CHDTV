package androidx.media3.exoplayer;

import android.util.Pair;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.exoplayer.source.ShuffleOrder;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractConcatenatedTimeline extends Timeline {
    private final int childCount;
    private final boolean isAtomic;
    private final ShuffleOrder shuffleOrder;

    public AbstractConcatenatedTimeline(boolean r2, ShuffleOrder r3) {
        this.isAtomic = r2;
        this.shuffleOrder = r3;
        this.childCount = r3.getLength();
    }

    public static Object getChildPeriodUidFromConcatenatedUid(Object r1) {
        return ((Pair) r1).second;
    }

    public static Object getChildTimelineUidFromConcatenatedUid(Object r1) {
        return ((Pair) r1).first;
    }

    public static Object getConcatenatedUid(Object r1, Object r2) {
        return Pair.create(r1, r2);
    }

    private int getNextChildIndex(int r2, boolean r3) {
        if (r3 == false) goto L5;
        return this.shuffleOrder.getNextIndex(r2);
    L5:
        if (r2 < (this.childCount - 1)) goto L10;
        return -1;
    L10:
        return r2 + 1;
    }

    private int getPreviousChildIndex(int r2, boolean r3) {
        if (r3 == true) goto L8;
        if (r2 > 0) goto L9;
        return -1;
    L9:
        return r2 - 1;
    L8:
        return this.shuffleOrder.getPreviousIndex(r2);
    }

    protected abstract int getChildIndexByChildUid(Object r1);

    protected abstract int getChildIndexByPeriodIndex(int r1);

    protected abstract int getChildIndexByWindowIndex(int r1);

    protected abstract Object getChildUidByChildIndex(int r1);

    protected abstract int getFirstPeriodIndexByChildIndex(int r1);

    @Override // androidx.media3.common.Timeline
    public int getFirstWindowIndex(boolean r4) {
        if (this.childCount != 0) goto L6;
        return -1;
    L6:
        if (this.isAtomic == false) goto L8;
        r4 = false;
    L8:
        if (r4 == false) goto L10;
        int r0 = this.shuffleOrder.getFirstIndex();
    L12:
        if (getTimelineByChildIndex(r0).isEmpty() == false) goto L17;
        r0 = getNextChildIndex(r0, r4);
        if (r0 != (-1)) goto L12;
        return -1;
    L17:
        return getFirstWindowIndexByChildIndex(r0) + getTimelineByChildIndex(r0).getFirstWindowIndex(r4);
    L10:
        r0 = 0;
        goto L12
    }

    protected abstract int getFirstWindowIndexByChildIndex(int r1);

    @Override // androidx.media3.common.Timeline
    public final int getIndexOfPeriod(Object r6) {
        if ((r6 instanceof Pair) == true) goto L5;
        return -1;
    L5:
        Object r0 = getChildTimelineUidFromConcatenatedUid(r6);
        Object r2 = getChildPeriodUidFromConcatenatedUid(r6);
        int r3 = getChildIndexByChildUid(r0);
        if (r3 != (-1)) goto L8;
        return -1;
    L8:
        int r4 = getTimelineByChildIndex(r3).getIndexOfPeriod(r2);
        if (r4 != (-1)) goto L12;
        return -1;
    L12:
        return getFirstPeriodIndexByChildIndex(r3) + r4;
    }

    @Override // androidx.media3.common.Timeline
    public int getLastWindowIndex(boolean r4) {
        if (this.childCount != 0) goto L6;
        return -1;
    L6:
        if (this.isAtomic == false) goto L8;
        r4 = false;
    L8:
        if (r4 == false) goto L10;
        int r0 = this.shuffleOrder.getLastIndex();
    L12:
        if (getTimelineByChildIndex(r0).isEmpty() == false) goto L17;
        r0 = getPreviousChildIndex(r0, r4);
        if (r0 != (-1)) goto L12;
        return -1;
    L17:
        return getFirstWindowIndexByChildIndex(r0) + getTimelineByChildIndex(r0).getLastWindowIndex(r4);
    L10:
        r0 = this.childCount - 1;
        goto L12
    }

    @Override // androidx.media3.common.Timeline
    public int getNextWindowIndex(int r8, int r9, boolean r10) {
        if (this.isAtomic == true) goto L5;
    L9:
        int r0 = getChildIndexByWindowIndex(r8);
        int r2 = getFirstWindowIndexByChildIndex(r0);
        Timeline r3 = getTimelineByChildIndex(r0);
        int r4 = r8 - r2;
        if (r9 != 2) goto L12;
        int r5 = 0;
    L13:
        int r6 = r3.getNextWindowIndex(r4, r5, r10);
        if (r6 != (-1)) goto L16;
        int r7 = getNextChildIndex(r0, r10);
    L18:
        if (r7 == (-1)) goto L22;
        if (getTimelineByChildIndex(r7).isEmpty() == false) goto L22;
        r7 = getNextChildIndex(r7, r10);
    L22:
        if (r7 != (-1)) goto L24;
        if (r9 == 2) goto L27;
        return -1;
    L27:
        return getFirstWindowIndex(r10);
    L24:
        return getFirstWindowIndexByChildIndex(r7) + getTimelineByChildIndex(r7).getFirstWindowIndex(r10);
    L16:
        return r2 + r6;
    L12:
        r5 = r9;
        goto L13
    L5:
        if (r9 != 1) goto L7;
        int r1 = 2;
    L8:
        r9 = r1;
        r10 = false;
        goto L9
    L7:
        r1 = r9;
        goto L8
    }

    @Override // androidx.media3.common.Timeline
    public final Timeline.Period getPeriod(int r6, Timeline.Period r7, boolean r8) {
        int r0 = getChildIndexByPeriodIndex(r6);
        int r1 = getFirstWindowIndexByChildIndex(r0);
        getTimelineByChildIndex(r0).getPeriod(r6 - getFirstPeriodIndexByChildIndex(r0), r7, r8);
        r7.windowIndex += r1;
        if (r8 == false) goto L5;
        r7.uid = getConcatenatedUid(getChildUidByChildIndex(r0), Assertions.checkNotNull(r7.uid));
    L5:
        return r7;
    }

    @Override // androidx.media3.common.Timeline
    public final Timeline.Period getPeriodByUid(Object r6, Timeline.Period r7) {
        Object r0 = getChildTimelineUidFromConcatenatedUid(r6);
        Object r1 = getChildPeriodUidFromConcatenatedUid(r6);
        int r2 = getChildIndexByChildUid(r0);
        int r3 = getFirstWindowIndexByChildIndex(r2);
        getTimelineByChildIndex(r2).getPeriodByUid(r1, r7);
        r7.windowIndex += r3;
        r7.uid = r6;
        return r7;
    }

    @Override // androidx.media3.common.Timeline
    public int getPreviousWindowIndex(int r8, int r9, boolean r10) {
        if (this.isAtomic == true) goto L5;
    L9:
        int r0 = getChildIndexByWindowIndex(r8);
        int r2 = getFirstWindowIndexByChildIndex(r0);
        Timeline r3 = getTimelineByChildIndex(r0);
        int r4 = r8 - r2;
        if (r9 != 2) goto L12;
        int r5 = 0;
    L13:
        int r6 = r3.getPreviousWindowIndex(r4, r5, r10);
        if (r6 != (-1)) goto L16;
        int r7 = getPreviousChildIndex(r0, r10);
    L18:
        if (r7 == (-1)) goto L22;
        if (getTimelineByChildIndex(r7).isEmpty() == false) goto L22;
        r7 = getPreviousChildIndex(r7, r10);
    L22:
        if (r7 != (-1)) goto L24;
        if (r9 == 2) goto L27;
        return -1;
    L27:
        return getLastWindowIndex(r10);
    L24:
        return getFirstWindowIndexByChildIndex(r7) + getTimelineByChildIndex(r7).getLastWindowIndex(r10);
    L16:
        return r2 + r6;
    L12:
        r5 = r9;
        goto L13
    L5:
        if (r9 != 1) goto L7;
        int r1 = 2;
    L8:
        r9 = r1;
        r10 = false;
        goto L9
    L7:
        r1 = r9;
        goto L8
    }

    protected abstract Timeline getTimelineByChildIndex(int r1);

    @Override // androidx.media3.common.Timeline
    public final Object getUidOfPeriod(int r5) {
        int r0 = getChildIndexByPeriodIndex(r5);
        int r1 = getFirstPeriodIndexByChildIndex(r0);
        Object r2 = getTimelineByChildIndex(r0).getUidOfPeriod(r5 - r1);
        return getConcatenatedUid(getChildUidByChildIndex(r0), r2);
    }

    @Override // androidx.media3.common.Timeline
    public final Timeline.Window getWindow(int r7, Timeline.Window r8, long r9) {
        int r0 = getChildIndexByWindowIndex(r7);
        int r1 = getFirstWindowIndexByChildIndex(r0);
        int r2 = getFirstPeriodIndexByChildIndex(r0);
        getTimelineByChildIndex(r0).getWindow(r7 - r1, r8, r9);
        Object r3 = getChildUidByChildIndex(r0);
        if (Timeline.Window.SINGLE_WINDOW_UID.equals(r8.uid) == false) goto L5;
        Object r4 = r3;
    L6:
        r8.uid = r4;
        r8.firstPeriodIndex += r2;
        r8.lastPeriodIndex += r2;
        return r8;
    L5:
        r4 = getConcatenatedUid(r3, r8.uid);
        goto L6
    }
}
