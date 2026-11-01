package androidx.media3.exoplayer.text;

import androidx.media3.common.C0180C;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Assertions;
import androidx.media3.extractor.text.CuesWithTiming;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class MergingCuesResolver implements CuesResolver {
    private static final Ordering<CuesWithTiming> CUES_DISPLAY_PRIORITY_COMPARATOR = null;
    private final List<CuesWithTiming> cuesWithTimingList;

    static {
        CUES_DISPLAY_PRIORITY_COMPARATOR = Ordering.natural().onResultOf(new MergingCuesResolver$$ExternalSyntheticLambda0()).compound(Ordering.natural().reverse().onResultOf(new MergingCuesResolver$$ExternalSyntheticLambda1()));
    }

    public MergingCuesResolver() {
        this.cuesWithTimingList = new ArrayList();
    }

    static /* synthetic */ Long lambda$static$0(CuesWithTiming r2) {
        return Long.valueOf(r2.startTimeUs);
    }

    static /* synthetic */ Long lambda$static$1(CuesWithTiming r2) {
        return Long.valueOf(r2.durationUs);
    }

    @Override // androidx.media3.exoplayer.text.CuesResolver
    public boolean addCues(CuesWithTiming r8, long r9) {
        if (r8.startTimeUs == C0180C.TIME_UNSET) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        if (r8.durationUs == C0180C.TIME_UNSET) goto L9;
        boolean r1 = true;
    L10:
        Assertions.checkArgument(r1);
        if (r8.startTimeUs <= r9) goto L13;
    L15:
        boolean r2 = false;
    L16:
        int r3 = this.cuesWithTimingList.size() - 1;
    L17:
        if (r3 < 0) goto L23;
        if (r8.startTimeUs >= this.cuesWithTimingList.get(r3).startTimeUs) goto L20;
        r3 = r3 - 1;
        goto L17
    L20:
        this.cuesWithTimingList.add(r3 + 1, r8);
        return r2;
    L23:
        this.cuesWithTimingList.add(0, r8);
        return r2;
    L13:
        if (r9 >= r8.endTimeUs) goto L15;
        r2 = true;
        goto L16
    L9:
        r1 = false;
        goto L10
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.text.CuesResolver
    public void clear() {
        this.cuesWithTimingList.clear();
    }

    @Override // androidx.media3.exoplayer.text.CuesResolver
    public void discardCuesBeforeTimeUs(long r6) {
        int r0 = 0;
    L4:
        if (r0 >= this.cuesWithTimingList.size()) goto L14;
        long r1 = this.cuesWithTimingList.get(r0).startTimeUs;
        if (r6 <= r1) goto L11;
        if (r6 <= this.cuesWithTimingList.get(r0).endTimeUs) goto L11;
        this.cuesWithTimingList.remove(r0);
        r0 = r0 - 1;
    L13:
        r0 = r0 + 1;
    L11:
        if (r6 >= r1) goto L13;
        return;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.media3.exoplayer.text.CuesResolver
    public ImmutableList<Cue> getCuesAtTimeUs(long r6) {
        if (this.cuesWithTimingList.isEmpty() == true) goto L26;
        if (r6 < this.cuesWithTimingList.get(0).startTimeUs) goto L26;
        ArrayList r0 = new ArrayList();
        int r1 = 0;
    L9:
        if (r1 >= this.cuesWithTimingList.size()) goto L19;
        CuesWithTiming r2 = this.cuesWithTimingList.get(r1);
        if (r6 < r2.startTimeUs) goto L16;
        if (r6 >= r2.endTimeUs) goto L16;
        r0.add(r2);
    L16:
        if (r6 < r2.startTimeUs) goto L19;
        r1 = r1 + 1;
    L19:
        ImmutableList r3 = ImmutableList.sortedCopyOf(CUES_DISPLAY_PRIORITY_COMPARATOR, r0);
        ImmutableList.Builder r4 = ImmutableList.builder();
        int r5 = 0;
    L21:
        if (r5 >= r3.size()) goto L24;
        r4.addAll(((CuesWithTiming) r3.get(r5)).cues);
        r5 = r5 + 1;
        goto L21
    L24:
        return r4.build();
    L26:
        return ImmutableList.m212of();
    }

    @Override // androidx.media3.exoplayer.text.CuesResolver
    public long getNextCueChangeTimeUs(long r13) {
        long r0 = C0180C.TIME_UNSET;
        int r2 = 0;
    L4:
        if (r2 >= this.cuesWithTimingList.size()) goto L21;
        long r6 = this.cuesWithTimingList.get(r2).startTimeUs;
        long r8 = this.cuesWithTimingList.get(r2).endTimeUs;
        if (r13 < r6) goto L8;
        if (r13 >= r8) goto L19;
        if (r0 != C0180C.TIME_UNSET) goto L17;
        long r3 = r8;
    L18:
        r0 = r3;
        goto L19
    L17:
        r3 = Math.min(r0, r8);
    L19:
        r2 = r2 + 1;
        goto L4
    L8:
        if (r0 != C0180C.TIME_UNSET) goto L10;
        long r10 = r6;
    L11:
        r0 = r10;
        goto L21
    L10:
        r10 = Math.min(r0, r6);
    L21:
        if (r0 != C0180C.TIME_UNSET) goto L29;
        return Long.MIN_VALUE;
    L29:
        return r0;
    }

    @Override // androidx.media3.exoplayer.text.CuesResolver
    public long getPreviousCueChangeTimeUs(long r9) {
        if (this.cuesWithTimingList.isEmpty() == false) goto L5;
        return C0180C.TIME_UNSET;
    L5:
        if (r9 < this.cuesWithTimingList.get(0).startTimeUs) goto L24;
        long r0 = this.cuesWithTimingList.get(0).startTimeUs;
        int r2 = 0;
    L9:
        if (r2 >= this.cuesWithTimingList.size()) goto L17;
        long r3 = this.cuesWithTimingList.get(r2).startTimeUs;
        long r5 = this.cuesWithTimingList.get(r2).endTimeUs;
        if (r5 > r9) goto L14;
        r0 = Math.max(r0, r5);
    L16:
        r2 = r2 + 1;
        goto L9
    L14:
        if (r3 > r9) goto L17;
        r0 = Math.max(r0, r3);
    L17:
        return r0;
    L24:
        return C0180C.TIME_UNSET;
    }
}
