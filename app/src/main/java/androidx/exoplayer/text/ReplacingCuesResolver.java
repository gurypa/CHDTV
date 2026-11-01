package androidx.media3.exoplayer.text;

import androidx.media3.common.C0180C;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Assertions;
import androidx.media3.extractor.text.CuesWithTiming;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
final class ReplacingCuesResolver implements CuesResolver {
    private final ArrayList<CuesWithTiming> cuesWithTimingList;

    public ReplacingCuesResolver() {
        this.cuesWithTimingList = new ArrayList();
    }

    private int getIndexOfCuesStartingAfter(long r4) {
        int r0 = 0;
    L4:
        if (r0 >= this.cuesWithTimingList.size()) goto L10;
        if (r4 < this.cuesWithTimingList.get(r0).startTimeUs) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r0;
    L10:
        return this.cuesWithTimingList.size();
    }

    @Override // androidx.media3.exoplayer.text.CuesResolver
    public boolean addCues(CuesWithTiming r8, long r9) {
        if (r8.startTimeUs == C0180C.TIME_UNSET) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        if (r8.startTimeUs <= r9) goto L9;
    L13:
        boolean r1 = false;
    L14:
        int r2 = this.cuesWithTimingList.size() - 1;
    L15:
        if (r2 < 0) goto L24;
        if (r8.startTimeUs >= this.cuesWithTimingList.get(r2).startTimeUs) goto L18;
        if (this.cuesWithTimingList.get(r2).startTimeUs > r9) goto L23;
        r1 = false;
    L23:
        r2 = r2 - 1;
        goto L15
    L18:
        this.cuesWithTimingList.add(r2 + 1, r8);
        return r1;
    L24:
        this.cuesWithTimingList.add(0, r8);
        return r1;
    L9:
        if (r8.endTimeUs != C0180C.TIME_UNSET) goto L11;
    L12:
        r1 = true;
        goto L14
    L11:
        if (r9 >= r8.endTimeUs) goto L13;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.text.CuesResolver
    public void clear() {
        this.cuesWithTimingList.clear();
    }

    @Override // androidx.media3.exoplayer.text.CuesResolver
    public void discardCuesBeforeTimeUs(long r7) {
        int r0 = getIndexOfCuesStartingAfter(r7);
        if (r0 != 0) goto L5;
        return;
    L5:
        CuesWithTiming r1 = this.cuesWithTimingList.get(r0 - 1);
        if (r1.endTimeUs != C0180C.TIME_UNSET) goto L8;
    L9:
        r0 = r0 - 1;
    L10:
        this.cuesWithTimingList.subList(0, r0).clear();
        return;
    L8:
        if (r1.endTimeUs < r7) goto L10;
        goto L9
    }

    @Override // androidx.media3.exoplayer.text.CuesResolver
    public ImmutableList<Cue> getCuesAtTimeUs(long r7) {
        int r0 = getIndexOfCuesStartingAfter(r7);
        if (r0 == 0) goto L5;
        CuesWithTiming r1 = this.cuesWithTimingList.get(r0 - 1);
        if (r1.endTimeUs == C0180C.TIME_UNSET) goto L13;
        if (r7 < r1.endTimeUs) goto L13;
        return ImmutableList.m212of();
    L13:
        return r1.cues;
    L5:
        return ImmutableList.m212of();
    }

    @Override // androidx.media3.exoplayer.text.CuesResolver
    public long getNextCueChangeTimeUs(long r9) {
        if (this.cuesWithTimingList.isEmpty() == false) goto L6;
        return Long.MIN_VALUE;
    L6:
        if (r9 < this.cuesWithTimingList.get(0).startTimeUs) goto L8;
        int r0 = 1;
    L11:
        if (r0 >= this.cuesWithTimingList.size()) goto L24;
        CuesWithTiming r3 = this.cuesWithTimingList.get(r0);
        if (r9 < r3.startTimeUs) goto L14;
        r0 = r0 + 1;
        goto L11
    L14:
        CuesWithTiming r1 = this.cuesWithTimingList.get(r0 - 1);
        if (r1.endTimeUs == C0180C.TIME_UNSET) goto L22;
        if (r1.endTimeUs <= r9) goto L22;
        if (r1.endTimeUs >= r3.startTimeUs) goto L22;
        return r1.endTimeUs;
    L22:
        return r3.startTimeUs;
    L24:
        CuesWithTiming r2 = (CuesWithTiming) Iterables.getLast(this.cuesWithTimingList);
        if (r2.endTimeUs != C0180C.TIME_UNSET) goto L27;
        return Long.MIN_VALUE;
    L27:
        if (r9 < r2.endTimeUs) goto L30;
        return Long.MIN_VALUE;
    L30:
        return r2.endTimeUs;
    L8:
        return this.cuesWithTimingList.get(0).startTimeUs;
    }

    @Override // androidx.media3.exoplayer.text.CuesResolver
    public long getPreviousCueChangeTimeUs(long r9) {
        if (this.cuesWithTimingList.isEmpty() == false) goto L5;
    L31:
        return C0180C.TIME_UNSET;
    L5:
        if (r9 < this.cuesWithTimingList.get(0).startTimeUs) goto L31;
        int r0 = 1;
    L9:
        if (r0 >= this.cuesWithTimingList.size()) goto L23;
        long r3 = this.cuesWithTimingList.get(r0).startTimeUs;
        if (r9 == r3) goto L12;
        if (r9 < r3) goto L15;
        r0 = r0 + 1;
        goto L9
    L15:
        CuesWithTiming r5 = this.cuesWithTimingList.get(r0 - 1);
        if (r5.endTimeUs == C0180C.TIME_UNSET) goto L21;
        if (r5.endTimeUs > r9) goto L21;
        return r5.endTimeUs;
    L21:
        return r5.startTimeUs;
    L12:
        return r3;
    L23:
        CuesWithTiming r1 = (CuesWithTiming) Iterables.getLast(this.cuesWithTimingList);
        if (r1.endTimeUs == C0180C.TIME_UNSET) goto L30;
        if (r9 < r1.endTimeUs) goto L30;
        return r1.endTimeUs;
    L30:
        return r1.startTimeUs;
    }
}
