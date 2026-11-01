package androidx.media3.extractor;

import androidx.media3.common.C0180C;

/* JADX INFO: loaded from: classes.dex */
public class ConstantBitrateSeekMap implements SeekMap {
    private final boolean allowSeeksIfLengthUnknown;
    private final int bitrate;
    private final long dataSize;
    private final long durationUs;
    private final long firstFrameBytePosition;
    private final int frameSize;
    private final long inputLength;

    public ConstantBitrateSeekMap(long r9, long r11, int r13, int r14) {
        this(r9, r11, r13, r14, false);
    }

    public ConstantBitrateSeekMap(long r4, long r6, int r8, int r9, boolean r10) {
        this.inputLength = r4;
        this.firstFrameBytePosition = r6;
        if (r9 != (-1)) goto L5;
        int r0 = 1;
    L6:
        this.frameSize = r0;
        this.bitrate = r8;
        this.allowSeeksIfLengthUnknown = r10;
        if (r4 != (-1)) goto L9;
        this.dataSize = -1;
        this.durationUs = C0180C.TIME_UNSET;
        return;
    L9:
        this.dataSize = r4 - r6;
        this.durationUs = getTimeUsAtPosition(r4, r6, r8);
        return;
    L5:
        r0 = r9;
        goto L6
    }

    private long getFramePositionForTimeUs(long r7) {
        long r2 = (((((long) this.bitrate) * r7) / 8000000) / ((long) this.frameSize)) * ((long) this.frameSize);
        if (this.dataSize == (-1)) goto L5;
        r2 = Math.min(r2, this.dataSize - ((long) this.frameSize));
    L5:
        long r0 = Math.max(r2, 0);
        return this.firstFrameBytePosition + r0;
    }

    private static long getTimeUsAtPosition(long r4, long r6, int r8) {
        return ((Math.max(0, r4 - r6) * 8) * 1000000) / ((long) r8);
    }

    @Override // androidx.media3.extractor.SeekMap
    public long getDurationUs() {
        return this.durationUs;
    }

    @Override // androidx.media3.extractor.SeekMap
    public SeekMap.SeekPoints getSeekPoints(long r12) {
        if (this.dataSize == (-1)) goto L5;
    L8:
        long r0 = getFramePositionForTimeUs(r12);
        long r4 = getTimeUsAtPosition(r0);
        SeekPoint r6 = new SeekPoint(r4, r0);
        if (this.dataSize == (-1)) goto L18;
        if (r4 >= r12) goto L18;
        if ((((long) this.frameSize) + r0) >= this.inputLength) goto L18;
        long r2 = ((long) this.frameSize) + r0;
        return new SeekMap.SeekPoints(r6, new SeekPoint(getTimeUsAtPosition(r2), r2));
    L18:
        return new SeekMap.SeekPoints(r6);
    L5:
        if (this.allowSeeksIfLengthUnknown == true) goto L8;
        return new SeekMap.SeekPoints(new SeekPoint(0, this.firstFrameBytePosition));
    }

    public long getTimeUsAtPosition(long r4) {
        return getTimeUsAtPosition(r4, this.firstFrameBytePosition, this.bitrate);
    }

    @Override // androidx.media3.extractor.SeekMap
    public boolean isSeekable() {
        if (this.dataSize == (-1)) goto L5;
    L8:
        return true;
    L5:
        if (this.allowSeeksIfLengthUnknown == true) goto L8;
        return false;
    }
}
