package androidx.media3.extractor.text;

import androidx.media3.common.C0180C;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class CuesWithTimingSubtitle implements Subtitle {
    private static final Ordering<CuesWithTiming> CUES_BY_START_TIME_ASCENDING = null;
    private static final String TAG = "CuesWithTimingSubtitle";
    private final ImmutableList<ImmutableList<Cue>> eventCues;
    private final long[] eventTimesUs;

    static {
        CUES_BY_START_TIME_ASCENDING = Ordering.natural().onResultOf(new CuesWithTimingSubtitle$$ExternalSyntheticLambda0());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CuesWithTimingSubtitle(List<CuesWithTiming> r15) {
        if (r15.size() != 1) goto L9;
        CuesWithTiming r0 = (CuesWithTiming) Iterables.getOnlyElement(r15);
        long r5 = normalizeUnsetStartTimeToZero(r0.startTimeUs);
        if (r0.durationUs != C0180C.TIME_UNSET) goto L7;
        this.eventCues = ImmutableList.m213of(r0.cues);
        this.eventTimesUs = new long[]{r5};
        return;
    L7:
        this.eventCues = ImmutableList.m214of(r0.cues, ImmutableList.m212of());
        this.eventTimesUs = new long[]{r5, r0.durationUs + r5};
        return;
    L9:
        this.eventTimesUs = new long[r15.size() * 2];
        Arrays.fill(this.eventTimesUs, Long.MAX_VALUE);
        ArrayList r1 = new ArrayList();
        ImmutableList r3 = ImmutableList.sortedCopyOf(CUES_BY_START_TIME_ASCENDING, r15);
        int r4 = 0;
        int r6 = 0;
    L11:
        if (r6 >= r3.size()) goto L28;
        CuesWithTiming r7 = (CuesWithTiming) r3.get(r6);
        long r8 = normalizeUnsetStartTimeToZero(r7.startTimeUs);
        long r9 = r7.durationUs + r8;
        if (r4 != 0) goto L15;
    L23:
        this.eventTimesUs[r4] = r8;
        r1.add(r7.cues);
        r4 = r4 + 1;
    L25:
        if (r7.durationUs == C0180C.TIME_UNSET) goto L27;
        this.eventTimesUs[r4] = r9;
        r1.add(ImmutableList.m212of());
        r4 = r4 + 1;
    L27:
        r6 = r6 + 1;
        goto L11
    L15:
        if (this.eventTimesUs[r4 - 1] < r8) goto L23;
        if (this.eventTimesUs[r4 - 1] == r8) goto L20;
    L22:
        Log.m48w(TAG, "Truncating unsupported overlapping cues.");
        this.eventTimesUs[r4 - 1] = r8;
        r1.set(r4 - 1, r7.cues);
        goto L25
    L20:
        if (((ImmutableList) r1.get(r4 - 1)).isEmpty() == false) goto L22;
        r1.set(r4 - 1, r7.cues);
        goto L25
    L28:
        this.eventCues = ImmutableList.copyOf(r1);
    }

    static /* synthetic */ Comparable lambda$static$0(CuesWithTiming r2) {
        return Long.valueOf(normalizeUnsetStartTimeToZero(r2.startTimeUs));
    }

    private static long normalizeUnsetStartTimeToZero(long r2) {
        if (r2 != C0180C.TIME_UNSET) goto L6;
        return 0;
    L6:
        return r2;
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public ImmutableList<Cue> getCues(long r4) {
        int r0 = Util.binarySearchFloor(this.eventTimesUs, r4, true, false);
        if (r0 != (-1)) goto L6;
        return ImmutableList.m212of();
    L6:
        return this.eventCues.get(r0);
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public /* bridge */ /* synthetic */ List getCues(long r1) {
        return getCues(r1);
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public long getEventTime(int r4) {
        if (r4 >= this.eventCues.size()) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        return this.eventTimesUs[r4];
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public int getEventTimeCount() {
        return this.eventCues.size();
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public int getNextEventTimeIndex(long r3) {
        int r0 = Util.binarySearchCeil(this.eventTimesUs, r3, false, false);
        if (r0 < this.eventCues.size()) goto L7;
        return -1;
    L7:
        return r0;
    }
}
