package androidx.media3.common.util;

import androidx.media3.common.C0180C;

/* JADX INFO: loaded from: classes.dex */
public final class ConstantRateTimestampIterator implements TimestampIterator {
    private final long endPositionUs;
    private final float frameRate;
    private int framesAdded;
    private final double framesDurationUs;
    private final long startPositionUs;
    private final int totalNumberOfFramesToAdd;

    public ConstantRateTimestampIterator(long r7, float r9) {
        this(0, r7, r9);
    }

    public ConstantRateTimestampIterator(long r6, long r8, float r10) {
        boolean r3 = true;
        if (r8 <= 0) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkArgument(r2);
        if (r10 <= 0.0f) goto L9;
        boolean r4 = true;
    L10:
        Assertions.checkArgument(r4);
        if (0 <= r6) goto L13;
    L15:
        r3 = false;
    L16:
        Assertions.checkArgument(r3);
        this.startPositionUs = r6;
        this.endPositionUs = r8;
        this.frameRate = r10;
        this.totalNumberOfFramesToAdd = Math.round(r10 * ((r8 - r6) / 1000000.0f));
        this.framesDurationUs = 1000000.0f / r10;
        return;
    L13:
        if (r6 >= r8) goto L15;
    L9:
        r4 = false;
        goto L10
    L5:
        r2 = false;
        goto L6
    }

    private long getTimestampUsAfter(int r7) {
        long r0 = this.startPositionUs + Math.round(this.framesDurationUs * ((double) r7));
        if (r0 < 0) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkState(r2);
        return r0;
    L5:
        r2 = false;
        goto L6
    }

    @Override // androidx.media3.common.util.TimestampIterator
    public ConstantRateTimestampIterator copyOf() {
        return new ConstantRateTimestampIterator(this.startPositionUs, this.endPositionUs, this.frameRate);
    }

    @Override // androidx.media3.common.util.TimestampIterator
    public /* bridge */ /* synthetic */ TimestampIterator copyOf() {
        return copyOf();
    }

    @Override // androidx.media3.common.util.TimestampIterator
    public long getLastTimestampUs() {
        if (this.totalNumberOfFramesToAdd != 0) goto L7;
        return C0180C.TIME_UNSET;
    L7:
        return getTimestampUsAfter(this.totalNumberOfFramesToAdd - 1);
    }

    @Override // androidx.media3.common.util.TimestampIterator
    public boolean hasNext() {
        if (this.framesAdded >= this.totalNumberOfFramesToAdd) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.common.util.TimestampIterator
    public long next() {
        Assertions.checkState(hasNext());
        int r0 = this.framesAdded;
        this.framesAdded = r0 + 1;
        return getTimestampUsAfter(r0);
    }
}
