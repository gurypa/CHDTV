package androidx.media3.extractor.mp3;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.SeekPoint;

/* JADX INFO: loaded from: classes.dex */
final class XingSeeker implements Seeker {
    private static final String TAG = "XingSeeker";
    private final int bitrate;
    private final long dataEndPosition;
    private final long dataSize;
    private final long dataStartPosition;
    private final long durationUs;
    private final long[] tableOfContents;
    private final int xingFrameSize;

    private XingSeeker(long r11, int r13, long r14, int r16) {
        this(r11, r13, r14, r16, -1, null);
    }

    private XingSeeker(long r4, int r6, long r7, int r9, long r10, long[] r12) {
        this.dataStartPosition = r4;
        this.xingFrameSize = r6;
        this.durationUs = r7;
        this.bitrate = r9;
        this.dataSize = r10;
        this.tableOfContents = r12;
        long r0 = -1;
        if (r10 == (-1)) goto L6;
        r0 = r4 + r10;
    L6:
        this.dataEndPosition = r0;
    }

    public static XingSeeker create(XingFrame r10, long r11) {
        long r4 = r10.computeDurationUs();
        if (r4 != C0180C.TIME_UNSET) goto L7;
        return null;
    L7:
        if (r10.dataSize == (-1)) goto L15;
        if (r10.tableOfContents == null) goto L15;
        return new XingSeeker(r11, r10.header.frameSize, r4, r10.header.bitrate, r10.dataSize, r10.tableOfContents);
    L15:
        return new XingSeeker(r11, r10.header.frameSize, r4, r10.header.bitrate);
    }

    private long getTimeUsForTableIndex(int r5) {
        return (this.durationUs * ((long) r5)) / 100;
    }

    @Override // androidx.media3.extractor.mp3.Seeker
    public int getAverageBitrate() {
        return this.bitrate;
    }

    @Override // androidx.media3.extractor.mp3.Seeker
    public long getDataEndPosition() {
        return this.dataEndPosition;
    }

    @Override // androidx.media3.extractor.SeekMap
    public long getDurationUs() {
        return this.durationUs;
    }

    @Override // androidx.media3.extractor.SeekMap
    public SeekMap.SeekPoints getSeekPoints(long r19) {
        if (isSeekable() == false) goto L5;
        long r1 = Util.constrainValue(r19, 0, this.durationUs);
        double r3 = (r1 * 100.0d) / this.durationUs;
        if (r3 > 0.0d) goto L10;
        double r5 = 0.0d;
    L18:
        return new SeekMap.SeekPoints(new SeekPoint(r1, this.dataStartPosition + Util.constrainValue(Math.round((r5 / 256.0d) * this.dataSize), this.xingFrameSize, this.dataSize - 1)));
    L10:
        if (r3 < 100.0d) goto L12;
        r5 = 256.0d;
        goto L18
    L12:
        int r6 = (int) r3;
        long[] r7 = (long[]) Assertions.checkStateNotNull(this.tableOfContents);
        double r10 = r7[r6];
        if (r6 != 99) goto L15;
        double r12 = 256.0d;
    L16:
        r5 = r10 + ((r12 - r10) * (r3 - ((double) r6)));
        goto L18
    L15:
        r12 = r7[r6 + 1];
        goto L16
    L5:
        return new SeekMap.SeekPoints(new SeekPoint(0, this.dataStartPosition + ((long) this.xingFrameSize)));
    }

    @Override // androidx.media3.extractor.mp3.Seeker
    public long getTimeUs(long r22) {
        long r1 = r22 - this.dataStartPosition;
        if (isSeekable() == false) goto L18;
        if (r1 <= this.xingFrameSize) goto L20;
        long[] r3 = (long[]) Assertions.checkStateNotNull(this.tableOfContents);
        double r4 = (r1 * 256.0d) / this.dataSize;
        int r6 = Util.binarySearchFloor(r3, (long) r4, true, true);
        long r7 = getTimeUsForTableIndex(r6);
        long r9 = r3[r6];
        long r11 = getTimeUsForTableIndex(r6 + 1);
        if (r6 != 99) goto L10;
        long r13 = 256;
    L12:
        if (r9 != r13) goto L14;
        double r0 = 0.0d;
    L16:
        return Math.round((r11 - r7) * r0) + r7;
    L14:
        r0 = (r4 - r9) / (r13 - r9);
        goto L16
    L10:
        r13 = r3[r6 + 1];
        goto L12
    L20:
        return 0;
    L18:
        return 0;
    }

    @Override // androidx.media3.extractor.SeekMap
    public boolean isSeekable() {
        if (this.tableOfContents == null) goto L5;
        return true;
    L5:
        return false;
    }
}
