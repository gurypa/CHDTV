package androidx.media3.extractor;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.LongArray;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class IndexSeekMap implements SeekMap {
    private long durationUs;
    private final LongArray positions;
    private final LongArray timesUs;

    public IndexSeekMap(long[] r6, long[] r7, long r8) {
        if (r6.length != r7.length) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        int r1 = r7.length;
        if (r1 > 0) goto L9;
    L11:
        this.positions = new LongArray(r1);
        this.timesUs = new LongArray(r1);
    L12:
        this.positions.addAll(r6);
        this.timesUs.addAll(r7);
        this.durationUs = r8;
        return;
    L9:
        if (r7[0] <= 0) goto L11;
        this.positions = new LongArray(r1 + 1);
        this.timesUs = new LongArray(r1 + 1);
        this.positions.add(0);
        this.timesUs.add(0);
        goto L12
    L5:
        r0 = false;
        goto L6
    }

    public void addSeekPoint(long r4, long r6) {
        if (this.timesUs.size() == 0) goto L5;
    L7:
        this.positions.add(r6);
        this.timesUs.add(r4);
        return;
    L5:
        if (r4 <= 0) goto L7;
        this.positions.add(0);
        this.timesUs.add(0);
        goto L7
    }

    @Override // androidx.media3.extractor.SeekMap
    public long getDurationUs() {
        return this.durationUs;
    }

    @Override // androidx.media3.extractor.SeekMap
    public SeekMap.SeekPoints getSeekPoints(long r8) {
        if (this.timesUs.size() == 0) goto L5;
        int r0 = Util.binarySearchFloor(this.timesUs, r8, true, true);
        SeekPoint r2 = new SeekPoint(this.timesUs.get(r0), this.positions.get(r0));
        if (r2.timeUs == r8) goto L14;
        if (r0 == (this.timesUs.size() - 1)) goto L14;
        return new SeekMap.SeekPoints(r2, new SeekPoint(this.timesUs.get(r0 + 1), this.positions.get(r0 + 1)));
    L14:
        return new SeekMap.SeekPoints(r2);
    L5:
        return new SeekMap.SeekPoints(SeekPoint.START);
    }

    public long getTimeUs(long r4) {
        if (this.timesUs.size() != 0) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        int r0 = Util.binarySearchFloor(this.positions, r4, true, true);
        return this.timesUs.get(r0);
    }

    @Override // androidx.media3.extractor.SeekMap
    public boolean isSeekable() {
        if (this.timesUs.size() <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isTimeUsInIndex(long r7, long r9) {
        if (this.timesUs.size() != 0) goto L6;
        return false;
    L6:
        if ((r7 - this.timesUs.get(this.timesUs.size() - 1)) < r9) goto L8;
        return false;
    L8:
        return true;
    }

    public void setDurationUs(long r1) {
        this.durationUs = r1;
    }
}
