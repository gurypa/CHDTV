package androidx.media3.extractor.mp3;

import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.MpegAudioUtil;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.SeekPoint;

/* JADX INFO: loaded from: classes.dex */
final class VbriSeeker implements Seeker {
    private static final String TAG = "VbriSeeker";
    private final int bitrate;
    private final long dataEndPosition;
    private final long durationUs;
    private final long[] positions;
    private final long[] timesUs;

    private VbriSeeker(long[] r1, long[] r2, long r3, long r5, int r7) {
        this.timesUs = r1;
        this.positions = r2;
        this.durationUs = r3;
        this.dataEndPosition = r5;
        this.bitrate = r7;
    }

    public static VbriSeeker create(long r26, long r28, MpegAudioUtil.Header r30, ParsableByteArray r31) {
        r31.skipBytes(6);
        int r4 = r31.readInt();
        long r5 = (r28 + ((long) r30.frameSize)) + ((long) r4);
        int r7 = r31.readInt();
        if (r7 > 0) goto L5;
        return null;
    L5:
        int r9 = r30.sampleRate;
        long r10 = r7;
        if (r9 < 32000) goto L8;
        int r12 = 1152;
    L9:
        long r19 = Util.scaleLargeTimestamp(r10, ((long) r12) * 1000000, r9);
        int r11 = r31.readUnsignedShort();
        int r13 = r31.readUnsignedShort();
        int r14 = r31.readUnsignedShort();
        r31.skipBytes(2);
        long r15 = r28 + ((long) r30.frameSize);
        long[] r16 = new long[r11];
        long[] r8 = new long[r11];
        int r3 = 0;
    L10:
        if (r3 >= r11) goto L19;
        int r24 = r7;
        long[] r18 = r8;
        int r17 = r3;
        int r25 = r4;
        r16[r17] = (((long) r3) * r19) / ((long) r11);
        r18[r17] = r15;
        switch(r14) {
            case 1: goto L17;
            case 2: goto L16;
            case 3: goto L15;
            case 4: goto L14;
            default: goto L13;
        };
    L14:
        int r6 = r31.readUnsignedIntToInt();
    L18:
        r15 = r15 + (((long) r6) * ((long) r13));
        r3 = r17 + 1;
        r8 = r18;
        r7 = r24;
        r4 = r25;
        goto L10
    L15:
        r6 = r31.readUnsignedInt24();
        goto L18
    L16:
        r6 = r31.readUnsignedShort();
        goto L18
    L17:
        r6 = r31.readUnsignedByte();
        goto L18
    L13:
        return null;
    L19:
        long[] r110 = r8;
        if (r26 == (-1)) goto L25;
        if (r26 == r5) goto L25;
        Log.m48w(TAG, "VBRI data size mismatch: " + r26 + ", " + r5);
    L25:
        if (r5 == r15) goto L27;
        Log.m48w(TAG, "VBRI bytes and ToC mismatch (using max): " + r5 + ", " + r15 + "\nSeeking will be inaccurate.");
        long r21 = Math.max(r5, r15);
    L29:
        return new VbriSeeker(r16, r110, r19, r21, r30.bitrate);
    L27:
        r21 = r5;
        goto L29
    L8:
        r12 = 576;
        goto L9
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
    public SeekMap.SeekPoints getSeekPoints(long r9) {
        int r0 = Util.binarySearchFloor(this.timesUs, r9, true, true);
        SeekPoint r2 = new SeekPoint(this.timesUs[r0], this.positions[r0]);
        if (r2.timeUs >= r9) goto L10;
        if (r0 == (this.timesUs.length - 1)) goto L10;
        return new SeekMap.SeekPoints(r2, new SeekPoint(this.timesUs[r0 + 1], this.positions[r0 + 1]));
    L10:
        return new SeekMap.SeekPoints(r2);
    }

    @Override // androidx.media3.extractor.mp3.Seeker
    public long getTimeUs(long r4) {
        return this.timesUs[Util.binarySearchFloor(this.positions, r4, true, true)];
    }

    @Override // androidx.media3.extractor.SeekMap
    public boolean isSeekable() {
        return true;
    }
}
