package androidx.media3.extractor.mp3;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.MpegAudioUtil;

/* JADX INFO: loaded from: classes.dex */
final class XingFrame {
    public final long dataSize;
    public final int encoderDelay;
    public final int encoderPadding;
    public final long frameCount;
    public final MpegAudioUtil.Header header;
    public final long[] tableOfContents;

    private XingFrame(MpegAudioUtil.Header r2, long r3, long r5, long[] r7, int r8, int r9) {
        this.header = new MpegAudioUtil.Header(r2);
        this.frameCount = r3;
        this.dataSize = r5;
        this.tableOfContents = r7;
        this.encoderDelay = r8;
        this.encoderPadding = r9;
    }

    public static XingFrame parse(MpegAudioUtil.Header r13, ParsableByteArray r14) {
        int r0 = r14.readInt();
        if ((r0 & 1) == 0) goto L5;
        int r1 = r14.readUnsignedIntToInt();
    L7:
        if ((r0 & 2) == 0) goto L9;
        long r2 = r14.readUnsignedInt();
    L10:
        long r8 = r2;
        if ((r0 & 4) != 4) goto L16;
        long[] r4 = new long[100];
        int r5 = 0;
    L13:
        if (r5 >= 100) goto L15;
        r4[r5] = r14.readUnsignedByte();
        r5 = r5 + 1;
        goto L13
    L15:
        long[] r10 = r4;
    L18:
        if ((r0 & 8) == 0) goto L21;
        r14.skipBytes(4);
    L21:
        if (r14.bytesLeft() < (21 + 3)) goto L23;
        r14.skipBytes(21);
        int r3 = r14.readUnsignedInt24();
        int r12 = r3 & 4095;
        int r11 = (16773120 & r3) >> 12;
    L25:
        return new XingFrame(r13, r1, r8, r10, r11, r12);
    L23:
        r12 = -1;
        r11 = -1;
        goto L25
    L16:
        r10 = null;
        goto L18
    L9:
        r2 = -1;
        goto L10
    L5:
        r1 = -1;
        goto L7
    }

    public long computeDurationUs() {
        if (this.frameCount != (-1)) goto L5;
        return C0180C.TIME_UNSET;
    L5:
        if (this.frameCount != 0) goto L8;
        return C0180C.TIME_UNSET;
    L8:
        return Util.sampleCountToDurationUs((this.frameCount * ((long) this.header.samplesPerFrame)) - 1, this.header.sampleRate);
    }
}
