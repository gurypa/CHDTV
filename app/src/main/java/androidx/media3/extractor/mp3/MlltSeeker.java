package androidx.media3.extractor.mp3;

import android.util.Pair;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.SeekPoint;
import androidx.media3.extractor.metadata.id3.MlltFrame;

/* JADX INFO: loaded from: classes.dex */
final class MlltSeeker implements Seeker {
    private final long durationUs;
    private final long[] referencePositions;
    private final long[] referenceTimesMs;

    private MlltSeeker(long[] r3, long[] r4, long r5) {
        this.referencePositions = r3;
        this.referenceTimesMs = r4;
        if (r5 == C0180C.TIME_UNSET) goto L5;
        long r0 = r5;
    L6:
        this.durationUs = r0;
        return;
    L5:
        r0 = Util.msToUs(r4[r4.length - 1]);
        goto L6
    }

    public static MlltSeeker create(long r11, MlltFrame r13, long r14) {
        int r0 = r13.bytesDeviations.length;
        long[] r1 = new long[r0 + 1];
        long[] r2 = new long[r0 + 1];
        r1[0] = r11;
        r2[0] = 0;
        long r3 = r11;
        long r5 = 0;
        int r7 = 1;
    L3:
        if (r7 > r0) goto L6;
        r3 = r3 + ((long) (r13.bytesBetweenReference + r13.bytesDeviations[r7 - 1]));
        r5 = r5 + ((long) (r13.millisecondsBetweenReference + r13.millisecondsDeviations[r7 - 1]));
        r1[r7] = r3;
        r2[r7] = r5;
        r7 = r7 + 1;
        goto L3
    L6:
        return new MlltSeeker(r1, r2, r14);
    }

    private static Pair<Long, Long> linearlyInterpolate(long r18, long[] r20, long[] r21) {
        int r3 = Util.binarySearchFloor(r20, r18, true, true);
        long r4 = r20[r3];
        long r6 = r21[r3];
        int r8 = r3 + 1;
        if (r8 == r20.length) goto L5;
        long r9 = r20[r8];
        long r11 = r21[r8];
        if (r9 != r4) goto L9;
        double r13 = 0.0d;
    L11:
        return Pair.create(Long.valueOf(r18), Long.valueOf(((long) ((r11 - r6) * r13)) + r6));
    L9:
        r13 = (r18 - r4) / (r9 - r4);
        goto L11
    L5:
        return Pair.create(Long.valueOf(r4), Long.valueOf(r6));
    }

    @Override // androidx.media3.extractor.mp3.Seeker
    public int getAverageBitrate() {
        return C0180C.RATE_UNSET_INT;
    }

    @Override // androidx.media3.extractor.mp3.Seeker
    public long getDataEndPosition() {
        return -1;
    }

    @Override // androidx.media3.extractor.SeekMap
    public long getDurationUs() {
        return this.durationUs;
    }

    @Override // androidx.media3.extractor.SeekMap
    public SeekMap.SeekPoints getSeekPoints(long r7) {
        Pair<Long, Long> r0 = linearlyInterpolate(Util.usToMs(Util.constrainValue(r7, 0, this.durationUs)), this.referenceTimesMs, this.referencePositions);
        return new SeekMap.SeekPoints(new SeekPoint(Util.msToUs(((Long) r0.first).longValue()), ((Long) r0.second).longValue()));
    }

    @Override // androidx.media3.extractor.mp3.Seeker
    public long getTimeUs(long r4) {
        return Util.msToUs(((Long) linearlyInterpolate(r4, this.referencePositions, this.referenceTimesMs).second).longValue());
    }

    @Override // androidx.media3.extractor.SeekMap
    public boolean isSeekable() {
        return true;
    }
}
