package androidx.media3.extractor;

import androidx.media3.common.ParserException;
import androidx.media3.common.util.CodecSpecificDataUtil;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.container.NalUnitUtil;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class AvcConfig {
    public final int bitdepthChroma;
    public final int bitdepthLuma;
    public final String codecs;
    public final int colorRange;
    public final int colorSpace;
    public final int colorTransfer;
    public final int height;
    public final List<byte[]> initializationData;
    public final int maxNumReorderFrames;
    public final int nalUnitLengthFieldLength;
    public final float pixelWidthHeightRatio;
    public final int width;

    private AvcConfig(List<byte[]> r1, int r2, int r3, int r4, int r5, int r6, int r7, int r8, int r9, int r10, float r11, String r12) {
        this.initializationData = r1;
        this.nalUnitLengthFieldLength = r2;
        this.width = r3;
        this.height = r4;
        this.bitdepthLuma = r5;
        this.bitdepthChroma = r6;
        this.colorSpace = r7;
        this.colorRange = r8;
        this.colorTransfer = r9;
        this.maxNumReorderFrames = r10;
        this.pixelWidthHeightRatio = r11;
        this.codecs = r12;
    }

    private static byte[] buildNalUnitForChild(ParsableByteArray r3) {
        int r0 = r3.readUnsignedShort();
        int r1 = r3.getPosition();
        r3.skipBytes(r0);
        return CodecSpecificDataUtil.buildNalUnit(r3.getData(), r1, r0);
    }

    public static AvcConfig parse(ParsableByteArray r19) throws ParserException {
        r19.skipBytes(4);     // Catch: ArrayIndexOutOfBoundsException -> L19
        int r5 = (r19.readUnsignedByte() & 3) + 1;     // Catch: ArrayIndexOutOfBoundsException -> L19
        if (r5 == 3) goto L18;
        ArrayList r4 = new ArrayList();     // Catch: ArrayIndexOutOfBoundsException -> L19
        int r0 = r19.readUnsignedByte() & 31;     // Catch: ArrayIndexOutOfBoundsException -> L19
        int r2 = 0;
    L6:
        if (r2 >= r0) goto L8;
        r4.add(buildNalUnitForChild(r19));     // Catch: ArrayIndexOutOfBoundsException -> L19
        r2 = r2 + 1;     // Catch: ArrayIndexOutOfBoundsException -> L19
        goto L6
    L8:
        int r3 = r19.readUnsignedByte();     // Catch: ArrayIndexOutOfBoundsException -> L19
        int r6 = 0;
    L9:
        if (r6 >= r3) goto L12;
        r4.add(buildNalUnitForChild(r19));     // Catch: ArrayIndexOutOfBoundsException -> L19
        r6 = r6 + 1;     // Catch: ArrayIndexOutOfBoundsException -> L19
        goto L9
    L12:
        if (r0 <= 0) goto L14;
        NalUnitUtil.SpsData r1 = NalUnitUtil.parseSpsNalUnit((byte[]) r4.get(0), r5, ((byte[]) r4.get(0)).length);     // Catch: ArrayIndexOutOfBoundsException -> L19
        int r15 = r1.width;     // Catch: ArrayIndexOutOfBoundsException -> L19
        int r16 = r1.height;     // Catch: ArrayIndexOutOfBoundsException -> L19
        int r7 = r1.bitDepthLumaMinus8 + 8;     // Catch: ArrayIndexOutOfBoundsException -> L19
        int r8 = r1.bitDepthChromaMinus8 + 8;     // Catch: ArrayIndexOutOfBoundsException -> L19
        int r17 = r1.colorSpace;     // Catch: ArrayIndexOutOfBoundsException -> L19
        int r18 = r1.colorRange;     // Catch: ArrayIndexOutOfBoundsException -> L19
        int r110 = r1.colorTransfer;     // Catch: ArrayIndexOutOfBoundsException -> L19
        int r111 = r1.maxNumReorderFrames;     // Catch: ArrayIndexOutOfBoundsException -> L19
        float r112 = r1.pixelWidthHeightRatio;     // Catch: ArrayIndexOutOfBoundsException -> L19
        String r113 = CodecSpecificDataUtil.buildAvcCodecString(r1.profileIdc, r1.constraintsFlagsAndReservedZero2Bits, r1.levelIdc);     // Catch: ArrayIndexOutOfBoundsException -> L19
        int r13 = r111;
        float r14 = r112;
        int r12 = r110;
        int r11 = r18;
        int r10 = r17;
        int r9 = r8;
        int r20 = r7;
        int r21 = r16;
        int r22 = r15;
    L16:
        return new AvcConfig(r4, r5, r22, r21, r20, r9, r10, r11, r12, r13, r14, r113);
    L14:
        r113 = null;
        r13 = 16;
        r14 = 1.0f;
        r12 = -1;
        r11 = -1;
        r10 = -1;
        r9 = -1;
        r20 = -1;
        r21 = -1;
        r22 = -1;
        goto L16
    L18:
        throw new IllegalStateException();     // Catch: ArrayIndexOutOfBoundsException -> L19
    L19:
        e = move-exception;
        throw ParserException.createForMalformedContainer("Error parsing AVC config", e);
    }
}
