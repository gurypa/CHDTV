package androidx.media3.exoplayer;

import androidx.media3.common.Timeline;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.source.ShuffleOrder;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class PlaylistTimeline extends AbstractConcatenatedTimeline {
    private final HashMap<Object, Integer> childIndexByUid;
    private final int[] firstPeriodInChildIndices;
    private final int[] firstWindowInChildIndices;
    private final int periodCount;
    private final Timeline[] timelines;
    private final Object[] uids;
    private final int windowCount;

    public PlaylistTimeline(Collection<? extends MediaSourceInfoHolder> r3, ShuffleOrder r4) {
        this(getTimelines(r3), getUids(r3), r4);
    }

    private PlaylistTimeline(Timeline[] r11, Object[] r12, ShuffleOrder r13) {
        int r0 = 0;
        super(false, r13);
        int r1 = r11.length;
        this.timelines = r11;
        this.firstPeriodInChildIndices = new int[r1];
        this.firstWindowInChildIndices = new int[r1];
        this.uids = r12;
        this.childIndexByUid = new HashMap();
        int r2 = 0;
        int r3 = 0;
        int r4 = 0;
        int r5 = r11.length;
    L3:
        if (r0 >= r5) goto L5;
        Timeline r6 = r11[r0];
        this.timelines[r2] = r6;
        this.firstWindowInChildIndices[r2] = r3;
        this.firstPeriodInChildIndices[r2] = r4;
        r3 = r3 + this.timelines[r2].getWindowCount();
        r4 = r4 + this.timelines[r2].getPeriodCount();
        int r9 = r2 + 1;
        this.childIndexByUid.put(r12[r2], Integer.valueOf(r2));
        r0 = r0 + 1;
        r2 = r9;
        goto L3
    L5:
        this.windowCount = r3;
        this.periodCount = r4;
    }

    private static Timeline[] getTimelines(Collection<? extends MediaSourceInfoHolder> r6) {
        Timeline[] r0 = new Timeline[r6.size()];
        int r1 = 0;
        Iterator<? extends MediaSourceInfoHolder> r2 = r6.iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        r0[r1] = r2.next().getTimeline();
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    private static Object[] getUids(Collection<? extends MediaSourceInfoHolder> r6) {
        Object[] r0 = new Object[r6.size()];
        int r1 = 0;
        Iterator<? extends MediaSourceInfoHolder> r2 = r6.iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        r0[r1] = r2.next().getUid();
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    public PlaylistTimeline copyWithPlaceholderTimeline(ShuffleOrder r5) {
        Timeline[] r0 = new Timeline[this.timelines.length];
        int r1 = 0;
    L4:
        if (r1 >= this.timelines.length) goto L7;
        final Timeline r3 = this.timelines[r1];
        r0[r1] = new C02491(this, r3);
        r1 = r1 + 1;
        goto L4
    L7:
        return new PlaylistTimeline(r0, this.uids, r5);
    }

    @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
    protected int getChildIndexByChildUid(Object r3) {
        Integer r0 = this.childIndexByUid.get(r3);
        if (r0 != null) goto L6;
        return -1;
    L6:
        return r0.intValue();
    }

    @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
    protected int getChildIndexByPeriodIndex(int r4) {
        return Util.binarySearchFloor(this.firstPeriodInChildIndices, r4 + 1, false, false);
    }

    @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
    protected int getChildIndexByWindowIndex(int r4) {
        return Util.binarySearchFloor(this.firstWindowInChildIndices, r4 + 1, false, false);
    }

    List<Timeline> getChildTimelines() {
        return Arrays.asList(this.timelines);
    }

    @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
    protected Object getChildUidByChildIndex(int r2) {
        return this.uids[r2];
    }

    @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
    protected int getFirstPeriodIndexByChildIndex(int r2) {
        return this.firstPeriodInChildIndices[r2];
    }

    @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
    protected int getFirstWindowIndexByChildIndex(int r2) {
        return this.firstWindowInChildIndices[r2];
    }

    @Override // androidx.media3.common.Timeline
    public int getPeriodCount() {
        return this.periodCount;
    }

    @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
    protected Timeline getTimelineByChildIndex(int r2) {
        return this.timelines[r2];
    }

    @Override // androidx.media3.common.Timeline
    public int getWindowCount() {
        return this.windowCount;
    }
}
