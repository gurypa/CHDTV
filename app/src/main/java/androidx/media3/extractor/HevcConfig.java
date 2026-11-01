package androidx.media3.extractor;

import androidx.media3.common.ParserException;
import androidx.media3.common.util.CodecSpecificDataUtil;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.container.NalUnitUtil;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class HevcConfig {
    public final int bitdepthChroma;
    public final int bitdepthLuma;
    public final String codecs;
    public final int colorRange;
    public final int colorSpace;
    public final int colorTransfer;
    public final int height;
    public final List<byte[]> initializationData;
    public final int maxNumReorderPics;
    public final int nalUnitLengthFieldLength;
    public final float pixelWidthHeightRatio;
    public final int stereoMode;
    public final NalUnitUtil.H265VpsData vpsData;
    public final int width;

    private HevcConfig(List<byte[]> r1, int r2, int r3, int r4, int r5, int r6, int r7, int r8, int r9, int r10, float r11, int r12, String r13, NalUnitUtil.H265VpsData r14) {
        this.initializationData = r1;
        this.nalUnitLengthFieldLength = r2;
        this.width = r3;
        this.height = r4;
        this.bitdepthLuma = r5;
        this.bitdepthChroma = r6;
        this.colorSpace = r7;
        this.colorRange = r8;
        this.colorTransfer = r9;
        this.stereoMode = r10;
        this.pixelWidthHeightRatio = r11;
        this.maxNumReorderPics = r12;
        this.codecs = r13;
        this.vpsData = r14;
    }

    public static HevcConfig parse(ParsableByteArray r2) throws ParserException {
        return parseImpl(r2, false, null);
    }

    private static HevcConfig parseImpl(ParsableByteArray r34, boolean r35, NalUnitUtil.H265VpsData r36) throws ParserException {
        if (r35 == false) goto L5;
        r34.skipBytes(4);     // Catch: ArrayIndexOutOfBoundsException -> L49
    L6:
        int r2 = r34.readUnsignedByte() & 3;     // Catch: ArrayIndexOutOfBoundsException -> L49
        int r3 = r34.readUnsignedByte();     // Catch: ArrayIndexOutOfBoundsException -> L49
        int r4 = 0;
        int r5 = r34.getPosition();     // Catch: ArrayIndexOutOfBoundsException -> L49
        int r6 = 0;
    L7:
        if (r6 >= r3) goto L12;
        r34.skipBytes(1);     // Catch: ArrayIndexOutOfBoundsException -> L49
        int r7 = r34.readUnsignedShort();     // Catch: ArrayIndexOutOfBoundsException -> L49
        int r8 = 0;
    L9:
        if (r8 >= r7) goto L11;
        int r9 = r34.readUnsignedShort();     // Catch: ArrayIndexOutOfBoundsException -> L49
        r4 = r4 + (r9 + 4);     // Catch: ArrayIndexOutOfBoundsException -> L49
        r34.skipBytes(r9);     // Catch: ArrayIndexOutOfBoundsException -> L49
        r8 = r8 + 1;     // Catch: ArrayIndexOutOfBoundsException -> L49
        goto L9
    L11:
        r6 = r6 + 1;     // Catch: ArrayIndexOutOfBoundsException -> L49
        goto L7
    L12:
        r34.setPosition(r5);     // Catch: ArrayIndexOutOfBoundsException -> L49
        byte[] r10 = new byte[r4];     // Catch: ArrayIndexOutOfBoundsException -> L49
        int r11 = 0;
        int r12 = -1;
        int r13 = 0;
        int r20 = -1;
        int r17 = -1;
        int r14 = -1;
        String r21 = null;
        NalUnitUtil.H265VpsData r22 = r36;
        int r18 = -1;
        float r19 = 1.0f;
        int r15 = -1;
        int r16 = -1;
        int r110 = -1;
        int r111 = -1;
    L13:
        if (r13 >= r3) goto L43;
        int r23 = r34.readUnsignedByte() & 63;     // Catch: ArrayIndexOutOfBoundsException -> L49
        int r112 = r34.readUnsignedShort();     // Catch: ArrayIndexOutOfBoundsException -> L49
        int r24 = r2;
        NalUnitUtil.H265VpsData r0 = r22;
        int r25 = 0;
    L15:
        if (r25 >= r112) goto L42;
        int r26 = r34.readUnsignedShort();     // Catch: ArrayIndexOutOfBoundsException -> L49
        int r27 = r25;
        int r28 = r3;
        int r29 = r4;
        System.arraycopy(NalUnitUtil.NAL_START_CODE, 0, r10, r11, NalUnitUtil.NAL_START_CODE.length);     // Catch: ArrayIndexOutOfBoundsException -> L49
        int r30 = r11 + NalUnitUtil.NAL_START_CODE.length;     // Catch: ArrayIndexOutOfBoundsException -> L49
        System.arraycopy(r34.getData(), r34.getPosition(), r10, r30, r26);     // Catch: ArrayIndexOutOfBoundsException -> L49
        if (r23 != 32) goto L21;
        if (r27 != 0) goto L21;
        r0 = NalUnitUtil.parseH265VpsNalUnit(r10, r30, r30 + r26);     // Catch: ArrayIndexOutOfBoundsException -> L49
    L41:
        r11 = r30 + r26;     // Catch: ArrayIndexOutOfBoundsException -> L49
        r34.skipBytes(r26);     // Catch: ArrayIndexOutOfBoundsException -> L49
        r25 = r27 + 1;     // Catch: ArrayIndexOutOfBoundsException -> L49
        r3 = r28;
        r4 = r29;
    L21:
        if (r23 != 33) goto L28;
        if (r27 != 0) goto L28;
        NalUnitUtil.H265SpsData r31 = NalUnitUtil.parseH265SpsNalUnit(r10, r30, r30 + r26, r0);     // Catch: ArrayIndexOutOfBoundsException -> L49
        r12 = r31.width;     // Catch: ArrayIndexOutOfBoundsException -> L49
        r110 = r31.height;     // Catch: ArrayIndexOutOfBoundsException -> L49
        r111 = r31.bitDepthLumaMinus8 + 8;     // Catch: ArrayIndexOutOfBoundsException -> L49
        r14 = r31.bitDepthChromaMinus8 + 8;     // Catch: ArrayIndexOutOfBoundsException -> L49
        r15 = r31.colorSpace;     // Catch: ArrayIndexOutOfBoundsException -> L49
        r16 = r31.colorRange;     // Catch: ArrayIndexOutOfBoundsException -> L49
        r17 = r31.colorTransfer;     // Catch: ArrayIndexOutOfBoundsException -> L49
        r19 = r31.pixelWidthHeightRatio;     // Catch: ArrayIndexOutOfBoundsException -> L49
        r20 = r31.maxNumReorderPics;     // Catch: ArrayIndexOutOfBoundsException -> L49
        if (r31.profileTierLevel == null) goto L41;
        r21 = CodecSpecificDataUtil.buildHevcCodecString(r31.profileTierLevel.generalProfileSpace, r31.profileTierLevel.generalTierFlag, r31.profileTierLevel.generalProfileIdc, r31.profileTierLevel.generalProfileCompatibilityFlags, r31.profileTierLevel.constraintBytes, r31.profileTierLevel.generalLevelIdc);     // Catch: ArrayIndexOutOfBoundsException -> L49
    L28:
        if (r23 != 39) goto L40;
        if (r27 != 0) goto L40;
        NalUnitUtil.H265Sei3dRefDisplayInfoData r32 = NalUnitUtil.parseH265Sei3dRefDisplayInfo(r10, r30, r30 + r26);     // Catch: ArrayIndexOutOfBoundsException -> L49
        if (r32 == null) goto L40;
        if (r0 == null) goto L40;
        NalUnitUtil.H265VpsData r210 = r0;
        if (r32.leftViewId != r0.layerInfos.get(0).viewId) goto L36;
        int r1 = 4;
    L37:
        r18 = r1;
        r0 = r210;
        goto L41
    L36:
        r1 = 5;
    L40:
        r0 = r0;
        goto L41
    L42:
        r13 = r13 + 1;     // Catch: ArrayIndexOutOfBoundsException -> L49
        r2 = r24;
        r22 = r0;
        goto L13
    L43:
        int r211 = r2;
        if (r4 != 0) goto L46;
        List r33 = Collections.emptyList();     // Catch: ArrayIndexOutOfBoundsException -> L49
    L47:
        return new HevcConfig(r33, r211 + 1, r12, r110, r111, r14, r15, r16, r17, r18, r19, r20, r21, r22);
    L46:
        r33 = Collections.singletonList(r10);     // Catch: ArrayIndexOutOfBoundsException -> L49
        goto L47
    L5:
        r34.skipBytes(21);     // Catch: ArrayIndexOutOfBoundsException -> L49
    L49:
        e = move-exception;
        StringBuilder r37 = new StringBuilder().append("Error parsing");
        if (r35 == false) goto L53;
        String r38 = "L-HEVC config";
    L55:
        throw ParserException.createForMalformedContainer(r37.append(r38).toString(), e);
    L53:
        r38 = "HEVC config";
        goto L55
    }

    public static HevcConfig parseLayered(ParsableByteArray r1, NalUnitUtil.H265VpsData r2) throws ParserException {
        return parseImpl(r1, true, r2);
    }
}
