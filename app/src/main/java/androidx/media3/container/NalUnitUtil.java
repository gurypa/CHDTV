package androidx.media3.container;

import androidx.media3.common.ColorInfo;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.CodecSpecificDataUtil;
import androidx.media3.common.util.Log;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import com.google.common.math.DoubleMath;
import java.lang.reflect.Array;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class NalUnitUtil {
    public static final float[] ASPECT_RATIO_IDC_VALUES = null;
    public static final int EXTENDED_SAR = 255;
    public static final int H264_NAL_UNIT_TYPE_AUD = 9;
    public static final int H264_NAL_UNIT_TYPE_IDR = 5;
    public static final int H264_NAL_UNIT_TYPE_NON_IDR = 1;
    public static final int H264_NAL_UNIT_TYPE_PARTITION_A = 2;
    public static final int H264_NAL_UNIT_TYPE_PPS = 8;
    public static final int H264_NAL_UNIT_TYPE_PREFIX = 14;
    public static final int H264_NAL_UNIT_TYPE_SEI = 6;
    public static final int H264_NAL_UNIT_TYPE_SPS = 7;
    public static final int H265_NAL_UNIT_TYPE_AUD = 35;
    public static final int H265_NAL_UNIT_TYPE_BLA_W_LP = 16;
    public static final int H265_NAL_UNIT_TYPE_CRA = 21;
    public static final int H265_NAL_UNIT_TYPE_PPS = 34;
    public static final int H265_NAL_UNIT_TYPE_PREFIX_SEI = 39;
    public static final int H265_NAL_UNIT_TYPE_RASL_R = 9;
    public static final int H265_NAL_UNIT_TYPE_SPS = 33;
    public static final int H265_NAL_UNIT_TYPE_SUFFIX_SEI = 40;
    public static final int H265_NAL_UNIT_TYPE_VPS = 32;
    private static final int INVALID_ID = -1;
    public static final byte[] NAL_START_CODE = null;

    @Deprecated
    public static final int NAL_UNIT_TYPE_AUD = 9;

    @Deprecated
    public static final int NAL_UNIT_TYPE_IDR = 5;

    @Deprecated
    public static final int NAL_UNIT_TYPE_NON_IDR = 1;

    @Deprecated
    public static final int NAL_UNIT_TYPE_PARTITION_A = 2;

    @Deprecated
    public static final int NAL_UNIT_TYPE_PPS = 8;

    @Deprecated
    public static final int NAL_UNIT_TYPE_PREFIX = 14;

    @Deprecated
    public static final int NAL_UNIT_TYPE_SEI = 6;

    @Deprecated
    public static final int NAL_UNIT_TYPE_SPS = 7;
    private static final String TAG = "NalUnitUtil";
    private static int[] scratchEscapePositions;
    private static final Object scratchEscapePositionsLock = null;

    public static final class H265LayerInfo {
        public final int layerIdInVps;
        public final int viewId;

        public H265LayerInfo(int r1, int r2) {
            this.layerIdInVps = r1;
            this.viewId = r2;
        }
    }

    public static final class H265NalHeader {
        public final int layerId;
        public final int nalUnitType;
        public final int temporalId;

        public H265NalHeader(int r1, int r2, int r3) {
            this.nalUnitType = r1;
            this.layerId = r2;
            this.temporalId = r3;
        }
    }

    public static final class H265ProfileTierLevel {
        public final int[] constraintBytes;
        public final int generalLevelIdc;
        public final int generalProfileCompatibilityFlags;
        public final int generalProfileIdc;
        public final int generalProfileSpace;
        public final boolean generalTierFlag;

        public H265ProfileTierLevel(int r1, boolean r2, int r3, int r4, int[] r5, int r6) {
            this.generalProfileSpace = r1;
            this.generalTierFlag = r2;
            this.generalProfileIdc = r3;
            this.generalProfileCompatibilityFlags = r4;
            this.constraintBytes = r5;
            this.generalLevelIdc = r6;
        }
    }

    public static final class H265ProfileTierLevelsAndIndices {
        public final int[] indices;
        public final ImmutableList<H265ProfileTierLevel> profileTierLevels;

        public H265ProfileTierLevelsAndIndices(List<H265ProfileTierLevel> r2, int[] r3) {
            this.profileTierLevels = ImmutableList.copyOf(r2);
            this.indices = r3;
        }
    }

    public static final class H265RepFormat {
        public final int bitDepthChromaMinus8;
        public final int bitDepthLumaMinus8;
        public final int chromaFormatIdc;
        public final int height;
        public final int width;

        public H265RepFormat(int r1, int r2, int r3, int r4, int r5) {
            this.chromaFormatIdc = r1;
            this.bitDepthLumaMinus8 = r2;
            this.bitDepthChromaMinus8 = r3;
            this.width = r4;
            this.height = r5;
        }
    }

    public static final class H265RepFormatsAndIndices {
        public final int[] indices;
        public final ImmutableList<H265RepFormat> repFormats;

        public H265RepFormatsAndIndices(List<H265RepFormat> r2, int[] r3) {
            this.repFormats = ImmutableList.copyOf(r2);
            this.indices = r3;
        }
    }

    public static final class H265Sei3dRefDisplayInfoData {
        public final int exponentRefDisplayWidth;
        public final int exponentRefViewingDist;
        public final int leftViewId;
        public final int mantissaRefDisplayWidth;
        public final int mantissaRefViewingDist;
        public final int numRefDisplays;
        public final int precRefDisplayWidth;
        public final int precRefViewingDist;
        public final int rightViewId;

        public H265Sei3dRefDisplayInfoData(int r1, int r2, int r3, int r4, int r5, int r6, int r7, int r8, int r9) {
            this.precRefDisplayWidth = r1;
            this.precRefViewingDist = r2;
            this.numRefDisplays = r3;
            this.leftViewId = r4;
            this.rightViewId = r5;
            this.exponentRefDisplayWidth = r6;
            this.mantissaRefDisplayWidth = r7;
            this.exponentRefViewingDist = r8;
            this.mantissaRefViewingDist = r9;
        }
    }

    public static final class H265SpsData {
        public final int bitDepthChromaMinus8;
        public final int bitDepthLumaMinus8;
        public final int chromaFormatIdc;
        public final int colorRange;
        public final int colorSpace;
        public final int colorTransfer;
        public final int height;
        public final int maxNumReorderPics;
        public final H265NalHeader nalHeader;
        public final float pixelWidthHeightRatio;
        public final H265ProfileTierLevel profileTierLevel;
        public final int seqParameterSetId;
        public final int width;

        public H265SpsData(H265NalHeader r1, H265ProfileTierLevel r2, int r3, int r4, int r5, int r6, int r7, int r8, float r9, int r10, int r11, int r12, int r13) {
            this.nalHeader = r1;
            this.profileTierLevel = r2;
            this.chromaFormatIdc = r3;
            this.bitDepthLumaMinus8 = r4;
            this.bitDepthChromaMinus8 = r5;
            this.seqParameterSetId = r6;
            this.width = r7;
            this.height = r8;
            this.pixelWidthHeightRatio = r9;
            this.maxNumReorderPics = r10;
            this.colorSpace = r11;
            this.colorRange = r12;
            this.colorTransfer = r13;
        }
    }

    public static final class H265VideoSignalInfo {
        public final int colorRange;
        public final int colorSpace;
        public final int colorTransfer;

        public H265VideoSignalInfo(int r1, int r2, int r3) {
            this.colorSpace = r1;
            this.colorRange = r2;
            this.colorTransfer = r3;
        }
    }

    public static final class H265VideoSignalInfosAndIndices {
        public final int[] indices;
        public final ImmutableList<H265VideoSignalInfo> videoSignalInfos;

        public H265VideoSignalInfosAndIndices(List<H265VideoSignalInfo> r2, int[] r3) {
            this.videoSignalInfos = ImmutableList.copyOf(r2);
            this.indices = r3;
        }
    }

    public static final class H265VpsData {
        public final ImmutableList<H265LayerInfo> layerInfos;
        public final H265NalHeader nalHeader;
        public final H265ProfileTierLevelsAndIndices profileTierLevelsAndIndices;
        public final H265RepFormatsAndIndices repFormatsAndIndices;
        public final H265VideoSignalInfosAndIndices videoSignalInfosAndIndices;

        public H265VpsData(H265NalHeader r2, List<H265LayerInfo> r3, H265ProfileTierLevelsAndIndices r4, H265RepFormatsAndIndices r5, H265VideoSignalInfosAndIndices r6) {
            this.nalHeader = r2;
            if (r3 == null) goto L5;
            ImmutableList<H265LayerInfo> r0 = ImmutableList.copyOf(r3);
        L6:
            this.layerInfos = r0;
            this.profileTierLevelsAndIndices = r4;
            this.repFormatsAndIndices = r5;
            this.videoSignalInfosAndIndices = r6;
            return;
        L5:
            r0 = ImmutableList.m212of();
            goto L6
        }
    }

    public static final class PpsData {
        public final boolean bottomFieldPicOrderInFramePresentFlag;
        public final int picParameterSetId;
        public final int seqParameterSetId;

        public PpsData(int r1, int r2, boolean r3) {
            this.picParameterSetId = r1;
            this.seqParameterSetId = r2;
            this.bottomFieldPicOrderInFramePresentFlag = r3;
        }
    }

    public static final class SpsData {
        public final int bitDepthChromaMinus8;
        public final int bitDepthLumaMinus8;
        public final int colorRange;
        public final int colorSpace;
        public final int colorTransfer;
        public final int constraintsFlagsAndReservedZero2Bits;
        public final boolean deltaPicOrderAlwaysZeroFlag;
        public final boolean frameMbsOnlyFlag;
        public final int frameNumLength;
        public final int height;
        public final int levelIdc;
        public final int maxNumRefFrames;
        public final int maxNumReorderFrames;
        public final int picOrderCntLsbLength;
        public final int picOrderCountType;
        public final float pixelWidthHeightRatio;
        public final int profileIdc;
        public final boolean separateColorPlaneFlag;
        public final int seqParameterSetId;
        public final int width;

        public SpsData(int r17, int r18, int r19, int r20, int r21, int r22, int r23, float r24, int r25, int r26, boolean r27, boolean r28, int r29, int r30, int r31, boolean r32, int r33, int r34, int r35, int r36) {
            this.profileIdc = r17;
            this.constraintsFlagsAndReservedZero2Bits = r18;
            this.levelIdc = r19;
            this.seqParameterSetId = r20;
            this.maxNumRefFrames = r21;
            this.width = r22;
            this.height = r23;
            this.pixelWidthHeightRatio = r24;
            this.bitDepthLumaMinus8 = r25;
            this.bitDepthChromaMinus8 = r26;
            this.separateColorPlaneFlag = r27;
            this.frameMbsOnlyFlag = r28;
            this.frameNumLength = r29;
            this.picOrderCountType = r30;
            this.picOrderCntLsbLength = r31;
            this.deltaPicOrderAlwaysZeroFlag = r32;
            this.colorSpace = r33;
            this.colorRange = r34;
            this.colorTransfer = r35;
            this.maxNumReorderFrames = r36;
        }
    }

    static {
        NAL_START_CODE = new byte[]{0, 0, 0, 1};
        ASPECT_RATIO_IDC_VALUES = new float[]{1.0f, 1.0f, 1.0909091f, 0.90909094f, 1.4545455f, 1.2121212f, 2.1818182f, 1.8181819f, 2.909091f, 2.4242425f, 1.6363636f, 1.3636364f, 1.939394f, 1.6161616f, 1.3333334f, 1.5f, 2.0f};
        scratchEscapePositionsLock = new Object();
        scratchEscapePositions = new int[10];
    }

    private NalUnitUtil() {
    }

    private static int applyConformanceWindowToHeight(int r2, int r3, int r4, int r5) {
        int r0 = 1;
        if (r3 != 1) goto L6;
        r0 = 2;
    L6:
        return r2 - ((r4 + r5) * r0);
    }

    private static int applyConformanceWindowToWidth(int r2, int r3, int r4, int r5) {
        int r0 = 2;
        if (r3 == 1) goto L8;
        if (r3 == 2) goto L8;
        r0 = 1;
    L8:
        return r2 - ((r4 + r5) * r0);
    }

    public static void clearPrefixFlags(boolean[] r2) {
        r2[0] = false;
        r2[1] = false;
        r2[2] = false;
    }

    private static String createCodecStringFromH265SpsPalyoad(ParsableNalUnitBitArray r8) {
        r8.skipBits(4);
        int r0 = r8.readBits(3);
        r8.skipBit();
        H265ProfileTierLevel r1 = parseH265ProfileTierLevel(r8, true, r0, null);
        return CodecSpecificDataUtil.buildHevcCodecString(r1.generalProfileSpace, r1.generalTierFlag, r1.generalProfileIdc, r1.generalProfileCompatibilityFlags, r1.constraintBytes, r1.generalLevelIdc);
    }

    public static void discardToSps(ByteBuffer r6) {
        int r0 = r6.position();
        int r1 = 0;
        int r2 = 0;
    L4:
        if ((r2 + 1) >= r0) goto L18;
        int r3 = r6.get(r2) & 255;
        if (r1 == 3) goto L8;
        if (r3 != 0) goto L15;
        r1 = r1 + 1;
    L15:
        if (r3 == 0) goto L17;
        r1 = 0;
    L17:
        r2 = r2 + 1;
        goto L4
    L8:
        if (r3 != 1) goto L15;
        if ((r6.get(r2 + 1) & Ascii.f91US) != 7) goto L15;
        ByteBuffer r4 = r6.duplicate();
        r4.position(r2 - 3);
        r4.limit(r0);
        r6.position(0);
        r6.put(r4);
        return;
    L18:
        r6.clear();
    }

    public static int findNalUnit(byte[] r7, int r8, int r9, boolean[] r10) {
        int r0 = r9 - r8;
        boolean r1 = false;
        if (r0 < 0) goto L5;
        boolean r3 = true;
    L6:
        Assertions.checkState(r3);
        if (r0 != 0) goto L10;
        return r9;
    L10:
        if (r10[0] == false) goto L13;
        clearPrefixFlags(r10);
        return r8 - 3;
    L13:
        if (r0 <= 1) goto L21;
        if (r10[1] == false) goto L21;
        if (r7[r8] != 1) goto L21;
        clearPrefixFlags(r10);
        return r8 - 2;
    L21:
        if (r0 > 2) goto L23;
    L30:
        int r4 = r9 - 1;
        int r5 = r8 + 2;
    L31:
        if (r5 >= r4) goto L46;
        if ((r7[r5] & 254) != 0) goto L44;
        if (r7[r5 - 2] == 0) goto L38;
    L43:
        r5 = r5 - 2;
        goto L44
    L38:
        if (r7[r5 - 1] != 0) goto L43;
        if (r7[r5] != 1) goto L43;
        clearPrefixFlags(r10);
        return r5 - 2;
    L44:
        r5 = r5 + 3;
        goto L31
    L46:
        if (r0 > 2) goto L48;
        if (r0 != 2) goto L65;
        if (r10[2] == true) goto L59;
    L63:
        boolean r6 = false;
    L70:
        r10[0] = r6;
        if (r0 <= 1) goto L79;
        if (r7[r9 - 2] == 0) goto L75;
    L77:
        boolean r11 = false;
    L84:
        r10[1] = r11;
        if (r7[r9 - 1] != 0) goto L87;
        r1 = true;
    L87:
        r10[2] = r1;
        return r9;
    L75:
        if (r7[r9 - 1] != 0) goto L77;
        r11 = true;
        goto L84
    L79:
        if (r10[2] == true) goto L81;
    L83:
        r11 = false;
        goto L84
    L81:
        if (r7[r9 - 1] != 0) goto L83;
        r11 = true;
        goto L84
    L59:
        if (r7[r9 - 2] != 0) goto L63;
        if (r7[r9 - 1] != 1) goto L63;
        r6 = true;
        goto L70
    L65:
        if (r10[1] == true) goto L67;
    L69:
        r6 = false;
        goto L70
    L67:
        if (r7[r9 - 1] != 1) goto L69;
        r6 = true;
        goto L70
    L48:
        if (r7[r9 - 3] == 0) goto L50;
    L54:
        r6 = false;
        goto L70
    L50:
        if (r7[r9 - 2] != 0) goto L54;
        if (r7[r9 - 1] != 1) goto L54;
        r6 = true;
        goto L70
    L23:
        if (r10[2] == false) goto L30;
        if (r7[r8] != 0) goto L30;
        if (r7[r8 + 1] != 1) goto L30;
        clearPrefixFlags(r10);
        return r8 - 1;
    L5:
        r3 = false;
        goto L6
    }

    private static ImmutableList<Integer> findNalUnitPositions(byte[] r5) {
        int r0 = 0;
        boolean[] r1 = new boolean[3];
        ImmutableList.Builder r2 = ImmutableList.builder();
    L4:
        if (r0 >= r5.length) goto L10;
        int r3 = findNalUnit(r5, r0, r5.length, r1);
        if (r3 == r5.length) goto L8;
        r2.add(Integer.valueOf(r3));
    L8:
        r0 = r3 + 3;
        goto L4
    L10:
        return r2.build();
    }

    private static int findNextUnescapeIndex(byte[] r3, int r4, int r5) {
        int r0 = r4;
    L4:
        if (r0 >= (r5 - 2)) goto L13;
        if (r3[r0] != 0) goto L12;
        if (r3[r0 + 1] != 0) goto L12;
        if (r3[r0 + 2] != 3) goto L12;
        return r0;
    L12:
        r0 = r0 + 1;
        goto L4
    L13:
        return r5;
    }

    public static String getH265BaseLayerCodecsString(List<byte[]> r10) {
        int r0 = 0;
    L4:
        if (r0 >= r10.size()) goto L20;
        byte[] r1 = r10.get(r0);
        int r2 = r1.length;
        if (r2 <= 3) goto L19;
        ImmutableList<Integer> r4 = findNalUnitPositions(r1);
        int r5 = 0;
    L9:
        if (r5 >= r4.size()) goto L19;
        if ((r4.get(r5).intValue() + 3) >= r2) goto L18;
        ParsableNalUnitBitArray r6 = new ParsableNalUnitBitArray(r1, r4.get(r5).intValue() + 3, r2);
        H265NalHeader r7 = parseH265NalHeader(r6);
        if (r7.nalUnitType != 33) goto L18;
        if (r7.layerId != 0) goto L18;
        return createCodecStringFromH265SpsPalyoad(r6);
    L18:
        r5 = r5 + 1;
    L19:
        r0 = r0 + 1;
        goto L4
    L20:
        return null;
    }

    public static int getH265NalUnitType(byte[] r1, int r2) {
        return (r1[r2 + 3] & 126) >> 1;
    }

    public static int getNalUnitType(byte[] r1, int r2) {
        return r1[r2 + 3] & Ascii.f91US;
    }

    public static boolean isH264NalUnitDependedOn(byte r5) {
        if (((r5 & 96) >> 5) == 0) goto L5;
        return true;
    L5:
        int r2 = r5 & Ascii.f91US;
        if (r2 != 1) goto L9;
        return false;
    L9:
        if (r2 != 9) goto L12;
        return false;
    L12:
        if (r2 != 14) goto L14;
        return false;
    L14:
        return true;
    }

    public static boolean isNalUnitSei(Format r3, byte r4) {
        if (Objects.equals(r3.sampleMimeType, MimeTypes.VIDEO_H264) == true) goto L7;
        if (MimeTypes.containsCodecsCorrespondingToMimeType(r3.codecs, MimeTypes.VIDEO_H264) == true) goto L7;
    L9:
        if (Objects.equals(r3.sampleMimeType, MimeTypes.VIDEO_H265) == true) goto L13;
        if (MimeTypes.containsCodecsCorrespondingToMimeType(r3.codecs, MimeTypes.VIDEO_H265) == true) goto L13;
    L15:
        return false;
    L13:
        if (((r4 & 126) >> 1) != 39) goto L15;
        return true;
    L7:
        if ((r4 & Ascii.f91US) != 6) goto L9;
        return true;
    }

    public static boolean isNalUnitSei(String r3, byte r4) {
        if (MimeTypes.VIDEO_H264.equals(r3) == false) goto L7;
        if ((r4 & Ascii.f91US) != 6) goto L7;
        return true;
    L7:
        if (MimeTypes.VIDEO_H265.equals(r3) == true) goto L9;
    L11:
        return false;
    L9:
        if (((r4 & 126) >> 1) != 39) goto L11;
        return true;
    }

    private static H265NalHeader parseH265NalHeader(ParsableNalUnitBitArray r4) {
        r4.skipBit();
        return new H265NalHeader(r4.readBits(6), r4.readBits(6), r4.readBits(3) - 1);
    }

    private static H265ProfileTierLevel parseH265ProfileTierLevel(ParsableNalUnitBitArray r17, boolean r18, int r19, H265ProfileTierLevel r20) {
        int[] r7 = new int[6];
        if (r18 == false) goto L16;
        int r3 = r17.readBits(2);
        boolean r4 = r17.readBit();
        int r5 = r17.readBits(5);
        int r6 = 0;
        int r10 = 0;
    L6:
        if (r10 >= 32) goto L11;
        if (r17.readBit() == false) goto L10;
        r6 = r6 | (1 << r10);
    L10:
        r10 = r10 + 1;
        goto L6
    L11:
        int r11 = 0;
    L13:
        if (r11 >= r7.length) goto L15;
        r7[r11] = r17.readBits(8);
        r11 = r11 + 1;
        goto L13
    L15:
        int r12 = r3;
        boolean r13 = r4;
        int r14 = r5;
        int r15 = r6;
        int[] r16 = r7;
    L19:
        int r110 = r17.readBits(8);
        int r8 = 0;
        int r9 = 0;
    L20:
        if (r9 >= r19) goto L28;
        if (r17.readBit() == false) goto L25;
        r8 = r8 + 88;
    L25:
        if (r17.readBit() == false) goto L27;
        r8 = r8 + 8;
    L27:
        r9 = r9 + 1;
        goto L20
    L28:
        r17.skipBits(r8);
        if (r19 <= 0) goto L32;
        r17.skipBits((8 - r19) * 2);
    L32:
        return new H265ProfileTierLevel(r12, r13, r14, r15, r16, r110);
    L16:
        if (r20 == null) goto L18;
        int r21 = r20.generalProfileSpace;
        boolean r22 = r20.generalTierFlag;
        int r23 = r20.generalProfileIdc;
        r12 = r21;
        r13 = r22;
        r14 = r23;
        r15 = r20.generalProfileCompatibilityFlags;
        r16 = r20.constraintBytes;
        goto L19
    L18:
        r12 = 0;
        r13 = false;
        r14 = 0;
        r15 = 0;
        r16 = r7;
        goto L19
    }

    private static H265RepFormat parseH265RepFormat(ParsableNalUnitBitArray r11) {
        int r1 = r11.readBits(16);
        int r0 = r11.readBits(16);
        if (r11.readBit() == false) goto L8;
        int r2 = r11.readBits(2);
        if (r2 != 3) goto L7;
        r11.skipBit();
    L7:
        int r6 = r2;
        int r7 = r11.readBits(4);
        int r8 = r11.readBits(4);
    L10:
        if (r11.readBit() == false) goto L12;
        int r3 = r11.readUnsignedExpGolombCodedInt();
        int r4 = r11.readUnsignedExpGolombCodedInt();
        int r5 = r11.readUnsignedExpGolombCodedInt();
        int r9 = r11.readUnsignedExpGolombCodedInt();
        int r10 = applyConformanceWindowToWidth(r1, r6, r3, r4);
        int r12 = applyConformanceWindowToHeight(r0, r6, r5, r9);
        int r13 = r10;
    L14:
        return new H265RepFormat(r6, r7, r8, r13, r12);
    L12:
        r12 = r0;
        r13 = r1;
        goto L14
    L8:
        r6 = 0;
        r7 = 0;
        r8 = 0;
        goto L10
    }

    private static H265RepFormatsAndIndices parseH265RepFormatsAndIndices(ParsableNalUnitBitArray r6, int r7) {
        int r0 = r6.readUnsignedExpGolombCodedInt() + 1;
        ImmutableList.Builder r2 = ImmutableList.builderWithExpectedSize(r0);
        int[] r3 = new int[r7];
        int r4 = 0;
    L3:
        if (r4 >= r0) goto L5;
        r2.add(parseH265RepFormat(r6));
        r4 = r4 + 1;
        goto L3
    L5:
        if (r0 > 1) goto L7;
    L12:
        int r1 = 1;
    L13:
        if (r1 >= r7) goto L16;
        r3[r1] = Math.min(r1, r0 - 1);
        r1 = r1 + 1;
    L16:
        return new H265RepFormatsAndIndices(r2.build(), r3);
    L7:
        if (r6.readBit() == false) goto L12;
        int r5 = DoubleMath.log2(r0, RoundingMode.CEILING);
        int r8 = 1;
    L9:
        if (r8 >= r7) goto L16;
        r3[r8] = r6.readBits(r5);
        r8 = r8 + 1;
        goto L9
    }

    public static H265Sei3dRefDisplayInfoData parseH265Sei3dRefDisplayInfo(byte[] r23, int r24, int r25) {
        int r1 = r24 + 2;
        int r2 = r25 - 1;
    L4:
        if (r23[r2] != 0) goto L8;
        if (r2 <= r1) goto L8;
        r2 = r2 - 1;
    L8:
        if (r23[r2] == 0) goto L63;
        if (r2 <= r1) goto L78;
        ParsableNalUnitBitArray r3 = new ParsableNalUnitBitArray(r23, r1, r2 + 1);
    L13:
        if (r3.canReadBits(16) == false) goto L60;
        int r5 = 0;
        int r7 = r3.readBits(8);
    L16:
        if (r7 != 255) goto L18;
        r5 = r5 + 255;
        r7 = r3.readBits(8);
        goto L16
    L18:
        int r6 = r5 + r7;
        int r9 = 0;
        int r8 = r3.readBits(8);
    L19:
        if (r8 != 255) goto L21;
        r9 = r9 + 255;
        r8 = r3.readBits(8);
        goto L19
    L21:
        int r10 = r9 + r8;
        if (r10 == 0) goto L59;
        if (r3.canReadBits(r10) == false) goto L77;
        if (r6 != 176) goto L13;
        int r11 = r3.readUnsignedExpGolombCodedInt();
        boolean r12 = r3.readBit();
        if (r12 == false) goto L31;
        int r13 = r3.readUnsignedExpGolombCodedInt();
    L32:
        int r14 = r3.readUnsignedExpGolombCodedInt();
        int r15 = -1;
        int r16 = 0;
        int r18 = -1;
        int r17 = -1;
        int r19 = -1;
        int r110 = -1;
        int r111 = -1;
    L33:
        if (r16 > r14) goto L56;
        r15 = r3.readUnsignedExpGolombCodedInt();
        r111 = r3.readUnsignedExpGolombCodedInt();
        int r4 = r3.readBits(6);
        if (r4 == 63) goto L36;
        if (r4 != 0) goto L40;
        int r0 = Math.max(0, r11 - 30);
    L41:
        r110 = r3.readBits(r0);
        if (r12 == false) goto L50;
        int r112 = r3.readBits(6);
        if (r112 == 63) goto L45;
        if (r112 != 0) goto L48;
        int r20 = Math.max(0, r13 - 30);
    L49:
        r19 = r3.readBits(r20);
        r18 = r112;
    L52:
        if (r3.readBit() == false) goto L54;
        r3.skipBits(10);
    L54:
        r16 = r16 + 1;
        r17 = r4;
        r1 = r1;
        goto L33
    L48:
        r20 = Math.max(0, (r112 + r13) - 31);
        goto L49
    L45:
        return null;
    L50:
        r1 = r1;
        goto L52
    L40:
        r0 = Math.max(0, (r4 + r11) - 31);
        goto L41
    L36:
        return null;
    L56:
        return new H265Sei3dRefDisplayInfoData(r11, r13, r14 + 1, r15, r111, r17, r110, r18, r19);
    L31:
        r13 = 0;
        goto L32
    L77:
        return null;
    L59:
        return null;
    L60:
        return null;
    L78:
        return null;
    L63:
        return null;
    }

    public static H265SpsData parseH265SpsNalUnit(byte[] r2, int r3, int r4, H265VpsData r5) {
        int r1 = r3 + 2;
        return parseH265SpsNalUnitPayload(r2, r1, r4, parseH265NalHeader(new ParsableNalUnitBitArray(r2, r3, r4)), r5);
    }

    public static H265SpsData parseH265SpsNalUnitPayload(byte[] r28, int r29, int r30, H265NalHeader r31, H265VpsData r32) {
        ParsableNalUnitBitArray r0 = new ParsableNalUnitBitArray(r28, r29, r30);
        r0.skipBits(4);
        int r5 = r0.readBits(3);
        if (r31.layerId != 0) goto L5;
    L7:
        boolean r6 = false;
    L8:
        boolean r16 = r6;
        if (r32 != null) goto L11;
    L13:
        int r17 = 0;
    L14:
        H265ProfileTierLevel r7 = null;
        if (r16 == true) goto L17;
        r0.skipBit();
        r7 = parseH265ProfileTierLevel(r0, true, r5, null);
    L21:
        H265ProfileTierLevel r2 = r7;
        int r8 = r0.readUnsignedExpGolombCodedInt();
        int r9 = 0;
        int r11 = 0;
        int r13 = 0;
        if (r16 == true) goto L24;
        r9 = r0.readUnsignedExpGolombCodedInt();
        if (r9 != 3) goto L43;
        r0.skipBit();
    L43:
        int r4 = r0.readUnsignedExpGolombCodedInt();
        int r10 = r0.readUnsignedExpGolombCodedInt();
        if (r0.readBit() == false) goto L46;
        int r12 = r0.readUnsignedExpGolombCodedInt();
        int r14 = r0.readUnsignedExpGolombCodedInt();
        int r15 = r0.readUnsignedExpGolombCodedInt();
        int r1 = r0.readUnsignedExpGolombCodedInt();
        int r18 = applyConformanceWindowToWidth(r4, r9, r12, r14);
        r11 = applyConformanceWindowToHeight(r10, r9, r15, r1);
    L47:
        int r19 = r0.readUnsignedExpGolombCodedInt();
        r13 = r0.readUnsignedExpGolombCodedInt();
        int r20 = r18;
        int r21 = r19;
    L48:
        int r22 = r0.readUnsignedExpGolombCodedInt();
        int r3 = -1;
        if (r16 == false) goto L51;
        int r110 = -1;
    L59:
        r0.readUnsignedExpGolombCodedInt();
        r0.readUnsignedExpGolombCodedInt();
        r0.readUnsignedExpGolombCodedInt();
        r0.readUnsignedExpGolombCodedInt();
        r0.readUnsignedExpGolombCodedInt();
        r0.readUnsignedExpGolombCodedInt();
        if (r0.readBit() == false) goto L69;
        boolean r23 = false;
        if (r16 == false) goto L64;
        r23 = r0.readBit();
    L64:
        if (r23 == false) goto L67;
        r0.skipBits(6);
        goto L69
    L67:
        if (r0.readBit() == false) goto L69;
        skipH265ScalingList(r0);
    L69:
        r0.skipBits(2);
        if (r0.readBit() == false) goto L72;
        r0.skipBits(8);
        r0.readUnsignedExpGolombCodedInt();
        r0.readUnsignedExpGolombCodedInt();
        r0.skipBit();
    L72:
        skipH265ShortTermReferencePictureSets(r0);
        if (r0.readBit() == false) goto L77;
        int r24 = r0.readUnsignedExpGolombCodedInt();
        int r111 = 0;
    L75:
        if (r111 >= r24) goto L77;
        r0.skipBits((r22 + 4) + 1);
        r111 = r111 + 1;
    L77:
        r0.skipBits(2);
        int r25 = -1;
        int r112 = -1;
        int r113 = -1;
        float r26 = 1.0f;
        if (r0.readBit() == true) goto L80;
        H265ProfileTierLevel r27 = r2;
        int r210 = r21;
        int r211 = r11;
        int r114 = -1;
        int r33 = r211;
    L122:
        return new H265SpsData(r31, r27, r9, r210, r13, r8, r20, r33, r26, r110, r114, r112, r113);
    L80:
        if (r0.readBit() == false) goto L93;
        r27 = r2;
        int r34 = r0.readBits(8);
        if (r34 != 255) goto L89;
        int r35 = r0.readBits(16);
        int r36 = r0.readBits(16);
        if (r35 == 0) goto L87;
        if (r36 == 0) goto L87;
        r210 = r21;
        r26 = r35 / r36;
    L95:
        if (r0.readBit() == false) goto L98;
        r0.skipBit();
    L98:
        if (r0.readBit() == false) goto L107;
        r0.skipBits(3);
        if (r0.readBit() == false) goto L102;
        int r212 = 1;
    L104:
        if (r0.readBit() == false) goto L106;
        int r37 = r0.readBits(8);
        int r38 = r0.readBits(8);
        r0.skipBits(8);
        r25 = ColorInfo.isoColorPrimariesToColorSpace(r37);
        r113 = ColorInfo.isoTransferCharacteristicsToColorTransfer(r38);
        r112 = r212;
    L114:
        if (r0.readBit() == false) goto L116;
        r0.readUnsignedExpGolombCodedInt();
        r0.readUnsignedExpGolombCodedInt();
    L116:
        r0.skipBit();
        if (r0.readBit() == false) goto L119;
        int r115 = r11 * 2;
        r114 = r25;
        r33 = r115;
        goto L122
    L119:
        int r213 = r11;
        r114 = r25;
        r33 = r213;
        goto L122
    L106:
        r112 = r212;
        goto L114
    L102:
        r212 = 2;
        goto L104
    L107:
        if (r32 == null) goto L114;
        if (r32.videoSignalInfosAndIndices == null) goto L114;
        int r39 = r32.videoSignalInfosAndIndices.indices[r17];
        if (r32.videoSignalInfosAndIndices.videoSignalInfos.size() <= r39) goto L114;
        H265VideoSignalInfo r40 = r32.videoSignalInfosAndIndices.videoSignalInfos.get(r39);
        int r41 = r40.colorSpace;
        r112 = r40.colorRange;
        r113 = r40.colorTransfer;
        r25 = r41;
    L87:
        r210 = r21;
        goto L95
    L89:
        r210 = r21;
        if (r34 >= ASPECT_RATIO_IDC_VALUES.length) goto L92;
        r26 = ASPECT_RATIO_IDC_VALUES[r34];
        goto L95
    L92:
        Log.m48w(TAG, "Unexpected aspect_ratio_idc value: " + r34);
        goto L95
    L93:
        r27 = r2;
        r210 = r21;
        goto L95
    L51:
        if (r0.readBit() == false) goto L53;
        int r116 = 0;
    L54:
        int r42 = r116;
    L55:
        if (r42 > r5) goto L57;
        r0.readUnsignedExpGolombCodedInt();
        r3 = Math.max(r0.readUnsignedExpGolombCodedInt(), r3);
        r0.readUnsignedExpGolombCodedInt();
        r42 = r42 + 1;
        goto L55
    L57:
        r110 = r3;
        goto L59
    L53:
        r116 = r5;
        goto L54
    L46:
        r18 = r4;
        r11 = r10;
        goto L47
    L24:
        if (r0.readBit() == false) goto L26;
        int r43 = r0.readBits(8);
    L27:
        if (r32 != null) goto L29;
    L39:
        r20 = 0;
        r21 = 0;
        goto L48
    L29:
        if (r32.repFormatsAndIndices == null) goto L39;
        if (r43 != (-1)) goto L33;
        int r44 = r32.repFormatsAndIndices.indices[r17];
    L34:
        if (r44 != (-1)) goto L36;
    L38:
        r20 = 0;
        r21 = 0;
        goto L48
    L36:
        if (r32.repFormatsAndIndices.repFormats.size() <= r44) goto L38;
        H265RepFormat r45 = r32.repFormatsAndIndices.repFormats.get(r44);
        r9 = r45.chromaFormatIdc;
        int r117 = r45.width;
        r11 = r45.height;
        int r118 = r45.bitDepthLumaMinus8;
        r13 = r45.bitDepthChromaMinus8;
        r20 = r117;
        r21 = r118;
        goto L48
    L33:
        r44 = r43;
        goto L34
    L26:
        r43 = -1;
        goto L27
    L17:
        if (r32 == null) goto L21;
        int r46 = r32.profileTierLevelsAndIndices.indices[r17];
        if (r32.profileTierLevelsAndIndices.profileTierLevels.size() <= r46) goto L21;
        r7 = r32.profileTierLevelsAndIndices.profileTierLevels.get(r46);
        goto L21
    L11:
        if (r32.layerInfos.isEmpty() == true) goto L13;
        r17 = r32.layerInfos.get(Math.min(r31.layerId, r32.layerInfos.size() - 1)).layerIdInVps;
        goto L14
    L5:
        if (r5 != 7) goto L7;
        r6 = true;
        goto L8
    }

    private static H265VideoSignalInfo parseH265VideoSignalInfo(ParsableNalUnitBitArray r4) {
        r4.skipBits(3);
        if (r4.readBit() == false) goto L5;
        int r0 = 1;
    L6:
        int r2 = ColorInfo.isoColorPrimariesToColorSpace(r4.readBits(8));
        int r3 = ColorInfo.isoTransferCharacteristicsToColorTransfer(r4.readBits(8));
        r4.skipBits(8);
        return new H265VideoSignalInfo(r2, r0, r3);
    L5:
        r0 = 2;
        goto L6
    }

    private static H265VideoSignalInfosAndIndices parseH265VideoSignalInfosAndIndices(ParsableNalUnitBitArray r11, int r12, int r13, int[] r14) {
        boolean r0 = true;
        if (r11.readBit() == true) goto L5;
        r0 = r11.readBit();
    L5:
        if (r0 == false) goto L7;
        r11.skipBit();
    L7:
        boolean r1 = r11.readBit();
        boolean r2 = r11.readBit();
        if (r1 == true) goto L10;
        if (r2 == true) goto L10;
    L26:
        int r3 = r12;
        boolean r4 = r11.readBit();
        if (r4 == false) goto L29;
        r3 = r11.readBits(4) + 1;
    L29:
        ImmutableList.Builder r7 = ImmutableList.builderWithExpectedSize(r3);
        int[] r8 = new int[r12];
        int r9 = 0;
    L30:
        if (r9 >= r3) goto L32;
        r7.add(parseH265VideoSignalInfo(r11));
        r9 = r9 + 1;
        goto L30
    L32:
        if (r4 == false) goto L38;
        if (r3 <= 1) goto L38;
        int r6 = 0;
    L35:
        if (r6 >= r12) goto L38;
        r8[r6] = r11.readBits(4);
        r6 = r6 + 1;
    L38:
        return new H265VideoSignalInfosAndIndices(r7.build(), r8);
    L10:
        int r5 = 0;
    L11:
        if (r5 >= r13) goto L26;
        int r10 = 0;
    L14:
        if (r10 >= r14[r5]) goto L25;
        boolean r15 = false;
        boolean r16 = false;
        if (r1 == false) goto L18;
        r15 = r11.readBit();
    L18:
        if (r2 == false) goto L20;
        r16 = r11.readBit();
    L20:
        if (r15 == false) goto L22;
        r11.skipBits(32);
    L22:
        if (r16 == false) goto L24;
        r11.skipBits(18);
    L24:
        r10 = r10 + 1;
        goto L14
    L25:
        r5 = r5 + 1;
        goto L11
    }

    public static H265VpsData parseH265VpsNalUnit(byte[] r3, int r4, int r5) {
        ParsableNalUnitBitArray r0 = new ParsableNalUnitBitArray(r3, r4, r5);
        return parseH265VpsNalUnitPayload(r0, parseH265NalHeader(r0));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static H265VpsData parseH265VpsNalUnitPayload(ParsableNalUnitBitArray r67, H265NalHeader r68) {
        r67.skipBits(4);
        boolean r2 = r67.readBit();
        boolean r3 = r67.readBit();
        int r5 = r67.readBits(6) + 1;
        int r8 = r67.readBits(3);
        r67.skipBits(17);
        H265ProfileTierLevel r9 = parseH265ProfileTierLevel(r67, true, r8, null);
        if (r67.readBit() == false) goto L5;
        int r10 = 0;
    L6:
        if (r10 > r8) goto L8;
        r67.readUnsignedExpGolombCodedInt();
        r67.readUnsignedExpGolombCodedInt();
        r67.readUnsignedExpGolombCodedInt();
        r10 = r10 + 1;
        goto L6
    L8:
        int r11 = r67.readBits(6);
        int r12 = r67.readUnsignedExpGolombCodedInt() + 1;
        ImmutableList r13 = ImmutableList.m213of(r9);
        H265ProfileTierLevelsAndIndices r14 = new H265ProfileTierLevelsAndIndices(r13, new int[1]);
        if (r5 < 2) goto L12;
        if (r12 < 2) goto L12;
        boolean r15 = true;
    L13:
        if (r2 == false) goto L16;
        if (r3 == false) goto L16;
        boolean r16 = true;
    L17:
        boolean r22 = r16;
        if ((r11 + 1) < r5) goto L20;
        boolean r1 = true;
    L21:
        if (r15 == false) goto L326;
        if (r22 == false) goto L326;
        if (r1 == false) goto L326;
        int r18 = 1;
        int r17 = 6;
        int[][] r4 = (int[][]) Array.newInstance(Integer.TYPE, new int[]{r12, r11 + 1});
        int[] r6 = new int[r12];
        int[] r19 = new int[r12];
        r4[0][0] = 0;
        r6[0] = 1;
        r19[0] = 0;
        int r7 = 1;
    L26:
        if (r7 >= r12) goto L35;
        int r21 = 0;
        int r110 = 0;
    L28:
        if (r110 > r11) goto L33;
        if (r67.readBit() == false) goto L32;
        r4[r7][r21] = r110;
        r19[r7] = r110;
        r21 = r21 + 1;
    L32:
        r6[r7] = r21;
        r110 = r110 + 1;
        goto L28
    L33:
        r7 = r7 + 1;
        goto L26
    L35:
        if (r67.readBit() == false) goto L52;
        r67.skipBits(64);
        if (r67.readBit() == false) goto L39;
        r67.readUnsignedExpGolombCodedInt();
    L39:
        int r20 = r67.readUnsignedExpGolombCodedInt();
        int r111 = 0;
        boolean r23 = r1;
    L40:
        if (r111 >= r20) goto L52;
        r67.readUnsignedExpGolombCodedInt();
        if (r111 != 0) goto L44;
    L47:
        boolean r24 = true;
    L48:
        skipH265HrdParameters(r67, r24, r8);
        r111 = r111 + 1;
        r23 = r23;
        goto L40
    L44:
        if (r67.readBit() == true) goto L47;
        r24 = false;
    L52:
        if (r67.readBit() == false) goto L54;
        r67.byteAlign();
        H265ProfileTierLevel r25 = parseH265ProfileTierLevel(r67, false, r8, r9);
        boolean r26 = r67.readBit();
        boolean[] r27 = new boolean[16];
        int r28 = 0;
        int r29 = 0;
    L56:
        if (r29 >= 16) goto L61;
        r27[r29] = r67.readBit();
        if (r27[r29] == false) goto L60;
        r28 = r28 + 1;
    L60:
        r29 = r29 + 1;
        goto L56
    L61:
        if (r28 == 0) goto L323;
        if (r27[1] == false) goto L323;
        int[] r30 = new int[r28];
        int r112 = 0;
    L66:
        int[] r35 = r30;
        if (r112 >= (r28 - (r26 ? 1 : 0))) goto L69;
        r35[r112] = r67.readBits(3);
        r112 = r112 + 1;
        r30 = r35;
        goto L66
    L69:
        int[] r31 = new int[r28 + 1];
        if (r26 == false) goto L78;
        int r113 = 1;
    L72:
        if (r113 >= r28) goto L77;
        int[] r36 = r31;
        int r32 = 0;
    L74:
        if (r32 >= r113) goto L76;
        r36[r113] = r36[r113] + (r35[r32] + 1);
        r32 = r32 + 1;
        goto L74
    L76:
        r113 = r113 + 1;
        r31 = r36;
        goto L72
    L77:
        int[] r37 = r31;
        r37[r28] = 6;
    L79:
        int[][] r33 = (int[][]) Array.newInstance(Integer.TYPE, new int[]{r5, r28});
        int[] r114 = new int[r5];
        r114[0] = 0;
        boolean r38 = r67.readBit();
        int r34 = 1;
    L80:
        if (r34 >= r5) goto L93;
        if (r38 == false) goto L83;
        int r210 = r34;
        r114[r210] = r67.readBits(r17);
    L84:
        if (r26 == true) goto L89;
        int r39 = 0;
    L86:
        if (r39 >= r28) goto L92;
        int r211 = r39;
        r33[r210][r211] = r67.readBits(r35[r39] + 1);
        r39 = r211 + 1;
    L92:
        r34 = r210 + 1;
        r17 = 6;
        goto L80
    L89:
        int r40 = 0;
    L90:
        if (r40 >= r28) goto L92;
        r33[r210][r40] = (r114[r210] & ((1 << r37[r40 + 1]) - 1)) >> r37[r40];
        r40 = r40 + 1;
        goto L90
    L83:
        r210 = r34;
        r114[r210] = r210;
        goto L84
    L93:
        int[] r41 = new int[r11 + 1];
        int r42 = 1;
        int r43 = 0;
    L94:
        boolean r44 = r3;
        if (r43 >= r5) goto L118;
        r41[r114[r43]] = -1;
        int r45 = 0;
        int r115 = 0;
    L97:
        int[][] r46 = r4;
        if (r45 >= 16) goto L106;
        if (r27[r45] == false) goto L105;
        if (r45 != r18) goto L104;
        r41[r114[r43]] = r33[r43][r115];
    L104:
        r115 = r115 + 1;
    L105:
        r45 = r45 + 1;
        r4 = r46;
        r18 = 1;
        goto L97
    L106:
        if (r43 <= 0) goto L116;
        boolean r47 = true;
        int r48 = 0;
    L108:
        if (r48 >= r43) goto L113;
        int r212 = r43;
        boolean r213 = r47;
        if (r41[r114[r43]] == r41[r114[r48]]) goto L111;
        r48 = r48 + 1;
        r43 = r212;
        r47 = r213;
        goto L108
    L111:
        r47 = false;
    L114:
        if (r47 == false) goto L117;
        r42 = r42 + 1;
    L117:
        r43 = r212 + 1;
        r3 = r44;
        r4 = r46;
        r18 = 1;
        goto L94
    L113:
        r212 = r43;
        goto L114
    L116:
        r212 = r43;
        goto L117
    L118:
        int[][] r49 = r4;
        int r50 = r67.readBits(4);
        if (r42 < 2) goto L320;
        if (r50 == 0) goto L320;
        int[] r51 = new int[r42];
        int r52 = 0;
    L123:
        if (r52 >= r42) goto L125;
        r51[r52] = r67.readBits(r50);
        r52 = r52 + 1;
        goto L123
    L125:
        int[] r53 = new int[r11 + 1];
        int r410 = r42;
        int r54 = 0;
    L126:
        if (r54 >= r5) goto L128;
        int r116 = r54;
        r53[Math.min(r114[r116], r11)] = r116;
        r54 = r116 + 1;
        goto L126
    L128:
        ImmutableList.Builder r55 = ImmutableList.builder();
        int r56 = 0;
    L129:
        if (r56 > r11) goto L135;
        int r117 = r56;
        int[] r411 = r53;
        int r57 = Math.min(r41[r117], r410 - 1);
        if (r57 < 0) goto L133;
        int r58 = r51[r57];
    L134:
        r55.add(new H265LayerInfo(r411[r117], r58));
        r56 = r117 + 1;
        r53 = r411;
        r51 = r51;
        goto L129
    L133:
        r58 = -1;
        goto L134
    L135:
        ImmutableList r59 = r55.build();
        if (((H265LayerInfo) r59.get(0)).viewId == (-1)) goto L138;
        int r60 = -1;
        int r61 = 1;
    L140:
        if (r61 > r11) goto L147;
        ImmutableList.Builder r412 = r55;
        int r118 = r60;
        if (((H265LayerInfo) r59.get(r61)).viewId != (-1)) goto L143;
        r61 = r61 + 1;
        r60 = r118;
        r55 = r412;
        goto L140
    L143:
        r60 = r61;
    L147:
        if (r60 == (-1)) goto L149;
        boolean[][] r62 = (boolean[][]) Array.newInstance(Boolean.TYPE, new int[]{r5, r5});
        boolean[][] r63 = (boolean[][]) Array.newInstance(Boolean.TYPE, new int[]{r5, r5});
        int r119 = 1;
    L151:
        if (r119 >= r5) goto L156;
        boolean[][] r413 = r63;
        int r64 = 0;
    L153:
        if (r64 >= r119) goto L155;
        boolean[] r120 = r62[r119];
        boolean[] r121 = r413[r119];
        boolean r214 = r67.readBit();
        r121[r64] = r214;
        r120[r64] = r214;
        r64 = r64 + 1;
        goto L153
    L155:
        r119 = r119 + 1;
        r63 = r413;
        goto L151
    L156:
        boolean[][] r414 = r63;
        int r65 = 1;
    L157:
        if (r65 >= r5) goto L171;
        int r122 = 0;
    L159:
        int[] r510 = r114;
        if (r122 >= (r5 - 1)) goto L170;
        int r123 = 0;
    L162:
        if (r123 >= r65) goto L169;
        if (r414[r65][r123] == false) goto L168;
        if (r414[r123][r122] == false) goto L168;
        r414[r65][r122] = true;
    L168:
        r123 = r123 + 1;
    L169:
        r122 = r122 + 1;
        r114 = r510;
        goto L159
    L170:
        r65 = r65 + 1;
        r114 = r510;
        goto L157
    L171:
        int[] r511 = r114;
        int[] r66 = new int[r11 + 1];
        int r124 = 0;
    L172:
        if (r124 >= r5) goto L177;
        int r125 = 0;
        int[] r512 = r66;
        int r69 = 0;
    L174:
        if (r69 >= r124) goto L176;
        r125 = r125 + (r62[r124][r69] ? 1 : 0);
        r69 = r69 + 1;
        goto L174
    L176:
        r512[r511[r124]] = r125;
        r124 = r124 + 1;
        r66 = r512;
        goto L172
    L177:
        int[] r513 = r66;
        int r70 = 0;
        int r126 = 0;
    L178:
        if (r126 >= r5) goto L184;
        if (r513[r511[r126]] != 0) goto L182;
        r70 = r70 + 1;
    L182:
        r126 = r126 + 1;
        goto L178
    L184:
        if (r70 > 1) goto L186;
        int[] r127 = new int[r5];
        int[] r128 = new int[r12];
        if (r67.readBit() == false) goto L193;
        int r71 = 0;
    L190:
        if (r71 >= r5) goto L194;
        int r129 = r71;
        r127[r129] = r67.readBits(3);
        r71 = r129 + 1;
    L194:
        int r72 = 0;
    L195:
        if (r72 >= r12) goto L202;
        int r215 = r72;
        int[] r514 = r127;
        int r73 = 0;
        int r130 = 0;
    L197:
        ImmutableList r216 = r13;
        if (r130 >= r6[r215]) goto L200;
        r73 = Math.max(r73, r514[((H265LayerInfo) r59.get(r49[r215][r130])).layerIdInVps]);
        r130 = r130 + 1;
        r13 = r216;
        goto L197
    L200:
        r128[r215] = r73 + 1;
        r72 = r215 + 1;
        r13 = r216;
        r127 = r514;
        goto L195
    L202:
        if (r67.readBit() == false) goto L214;
        int r74 = 0;
    L205:
        if (r74 >= (r5 - 1)) goto L214;
        int r131 = r74 + 1;
    L207:
        if (r131 >= r5) goto L213;
        if (r62[r131][r74] == false) goto L212;
        r67.skipBits(3);
    L212:
        r131 = r131 + 1;
        goto L207
    L213:
        r74 = r74 + 1;
    L214:
        r67.skipBit();
        int r75 = r67.readUnsignedExpGolombCodedInt() + 1;
        ImmutableList.Builder r132 = ImmutableList.builder();
        r132.add(r9);
        if (r75 <= 1) goto L221;
        r132.add(r25);
        H265ProfileTierLevel r133 = r25;
        int r76 = 2;
    L217:
        if (r76 >= r75) goto L221;
        int r134 = r76;
        H265ProfileTierLevel r77 = parseH265ProfileTierLevel(r67, r67.readBit(), r8, r133);
        r132.add(r77);
        r133 = r77;
        r76 = r134 + 1;
    L221:
        ImmutableList r78 = r132.build();
        int r135 = r67.readUnsignedExpGolombCodedInt() + r12;
        if (r135 > r12) goto L224;
        int r79 = r67.readBits(2);
        boolean[][] r80 = (boolean[][]) Array.newInstance(Boolean.TYPE, new int[]{r135, r11 + 1});
        int[] r136 = new int[r135];
        int[] r81 = new int[r135];
        int r82 = 0;
        boolean r137 = r15;
    L226:
        if (r82 >= r12) goto L243;
        int r138 = r82;
        r136[r138] = 0;
        r81[r138] = r19[r138];
        if (r79 != 0) goto L230;
        int[] r515 = r136;
        int[] r610 = r19;
        boolean r611 = r137;
        Arrays.fill(r80[r138], 0, r6[r138], true);
        r515[r138] = r6[r138];
    L242:
        r82 = r138 + 1;
        r136 = r515;
        r19 = r610;
        r137 = r611;
        goto L226
    L230:
        r515 = r136;
        r610 = r19;
        r611 = r137;
        if (r79 != 1) goto L241;
        int r83 = r610[r138];
        int r139 = 0;
    L234:
        if (r139 >= r6[r138]) goto L240;
        boolean[] r140 = r80[r138];
        if (r49[r138][r139] != r83) goto L238;
        boolean r141 = true;
    L239:
        r140[r139] = r141;
        r139 = r139 + 1;
        goto L234
    L238:
        r141 = false;
        goto L239
    L240:
        r515[r138] = 1;
        goto L242
    L241:
        r80[0][0] = true;
        r515[0] = 1;
        goto L242
    L243:
        int[] r516 = r136;
        int[] r84 = new int[r11 + 1];
        boolean[][] r142 = (boolean[][]) Array.newInstance(Boolean.TYPE, new int[]{r135, r11 + 1});
        int r143 = 0;
        int r144 = 1;
    L244:
        if (r144 >= r135) goto L302;
        int r612 = r143;
        if (r79 != 2) goto L254;
        int r145 = 0;
    L248:
        int r613 = r79;
        if (r145 >= r6[r144]) goto L255;
        r80[r144][r145] = r67.readBit();
        r516[r144] = r516[r144] + (r80[r144][r145] ? 1 : 0);
        if (r80[r144][r145] == false) goto L253;
        r81[r144] = r49[r144][r145];
    L253:
        r145 = r145 + 1;
        r79 = r613;
    L255:
        if (r612 != 0) goto L272;
        if (r49[r144][0] != 0) goto L272;
        if (r80[r144][0] == false) goto L272;
        int r85 = 1;
        int r146 = r612;
    L261:
        int r147 = r146;
        if (r85 >= r6[r144]) goto L270;
        if (r49[r144][r85] == r60) goto L266;
    L268:
        r146 = r147;
    L269:
        r85 = r85 + 1;
        goto L261
    L266:
        if (r80[r144][r60] == false) goto L268;
        r146 = r144;
        goto L269
    L270:
        r143 = r147;
    L273:
        int r86 = 0;
    L274:
        int r614 = r60;
        if (r86 >= r6[r144]) goto L296;
        if (r75 <= 1) goto L294;
        r142[r144][r86] = r80[r144][r86];
        int[] r615 = r84;
        ImmutableList r217 = r78;
        int r87 = DoubleMath.log2(r75, RoundingMode.CEILING);
        if (r142[r144][r86] == true) goto L287;
        int r88 = ((H265LayerInfo) r59.get(r49[r144][r86])).layerIdInVps;
        int r89 = 0;
    L281:
        if (r89 >= r86) goto L286;
        int r616 = r75;
        if (r414[r88][((H265LayerInfo) r59.get(r49[r144][r89])).layerIdInVps] == true) goto L284;
        r89 = r89 + 1;
        r75 = r616;
        goto L281
    L284:
        r142[r144][r86] = true;
    L289:
        if (r142[r144][r86] == false) goto L295;
        if (r143 <= 0) goto L293;
        if (r144 != r143) goto L293;
        r615[r86] = r67.readBits(r87);
    L293:
        r67.skipBits(r87);
    L295:
        r86 = r86 + 1;
        r78 = r217;
        r60 = r614;
        r84 = r615;
        r75 = r616;
        goto L274
    L286:
        r616 = r75;
        goto L289
    L287:
        r616 = r75;
        goto L289
    L294:
        r616 = r75;
        r217 = r78;
        r615 = r84;
        goto L295
    L296:
        int r617 = r75;
        ImmutableList r218 = r78;
        int[] r618 = r84;
        if (r516[r144] != 1) goto L301;
        if (r513[r81[r144]] <= 0) goto L301;
        r67.skipBit();
    L301:
        r144 = r144 + 1;
        r78 = r218;
        r79 = r613;
        r60 = r614;
        r84 = r618;
        r75 = r617;
    L272:
        r143 = r612;
        goto L273
    L254:
        r613 = r79;
        goto L255
    L302:
        ImmutableList r219 = r78;
        int[] r619 = r84;
        if (r143 == 0) goto L305;
        H265RepFormatsAndIndices r310 = parseH265RepFormatsAndIndices(r67, r5);
        r67.skipBits(2);
        int r90 = 1;
    L307:
        if (r90 >= r5) goto L312;
        if (r513[r511[r90]] != 0) goto L311;
        r67.skipBit();
    L311:
        r90 = r90 + 1;
        goto L307
    L312:
        skipH265DpbSize(r67, r135, r128, r6, r142);
        skipToH265VuiPresentFlagAfterDpbSize(r67, r5, r62);
        if (r67.readBit() == false) goto L315;
        r67.byteAlign();
        H265VideoSignalInfosAndIndices r311 = parseH265VideoSignalInfosAndIndices(r67, r5, r12, r128);
    L317:
        return new H265VpsData(r68, r59, new H265ProfileTierLevelsAndIndices(r219, r619), r310, r311);
    L315:
        r311 = null;
        goto L317
    L305:
        return new H265VpsData(r68, null, r14, null, null);
    L224:
        return new H265VpsData(r68, null, r14, null, null);
    L193:
        Arrays.fill(r127, 0, r5, r8);
        goto L194
    L186:
        return new H265VpsData(r68, null, r14, null, null);
    L149:
        return new H265VpsData(r68, null, r14, null, null);
    L138:
        return new H265VpsData(r68, null, r14, null, null);
    L320:
        return new H265VpsData(r68, null, r14, null, null);
    L78:
        r37 = r31;
    L323:
        return new H265VpsData(r68, null, r14, null, null);
    L54:
        return new H265VpsData(r68, null, r14, null, null);
    L326:
        return new H265VpsData(r68, null, r14, null, null);
    L20:
        r1 = false;
    L16:
        r16 = false;
    L12:
        r15 = false;
        goto L13
    L5:
        r10 = r8;
        goto L6
    }

    public static PpsData parsePpsNalUnit(byte[] r1, int r2, int r3) {
        return parsePpsNalUnitPayload(r1, r2 + 1, r3);
    }

    public static PpsData parsePpsNalUnitPayload(byte[] r5, int r6, int r7) {
        ParsableNalUnitBitArray r0 = new ParsableNalUnitBitArray(r5, r6, r7);
        int r1 = r0.readUnsignedExpGolombCodedInt();
        int r2 = r0.readUnsignedExpGolombCodedInt();
        r0.skipBit();
        return new PpsData(r1, r2, r0.readBit());
    }

    public static SpsData parseSpsNalUnit(byte[] r1, int r2, int r3) {
        return parseSpsNalUnitPayload(r1, r2 + 1, r3);
    }

    /* JADX WARN: Failed to calculate best type for var: r0v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v0 ??, new type: androidx.media3.container.ParsableNalUnitBitArray
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 6 more
     */
    /* JADX WARN: Failed to calculate best type for var: r0v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v0 ??, new type: androidx.media3.container.ParsableNalUnitBitArray
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r11v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r11v3 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r23v15 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r23v15 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r23v16 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r23v16 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r28v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r28v0 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r28v2 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r28v2 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v3 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 6 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v3 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v4 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v4 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r32v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r32v3 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r33v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r33v3 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r38v5 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r38v5 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r38v6 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r38v6 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r38v7 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r38v7 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /*  JADX ERROR: Types fix failed
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v3 ??, new type: int
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:186)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:245)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
        Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
        	... 5 more
        */
    public static androidx.media3.container.NalUnitUtil.SpsData parseSpsNalUnitPayload(byte[] r40, int r41, int r42) {
        /*  JADX ERROR: Simple mode code generation failed
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0008: CONSTRUCTOR (r0v0 ?? I:androidx.media3.container.ParsableNalUnitBitArray) = (r40v0 byte[]), (r41v0 int), (r42v0 int) A[DECLARE_VAR, MD:(byte[], int, int):void (m)] call: androidx.media3.container.ParsableNalUnitBitArray.<init>(byte[], int, int):void type: CONSTRUCTOR in method: androidx.media3.container.NalUnitUtil.parseSpsNalUnitPayload(byte[], int, int):androidx.media3.container.NalUnitUtil$SpsData, file: classes.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:310)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:273)
            	at jadx.core.codegen.MethodGen.generateSimpleCode(MethodGen.java:348)
            	at jadx.core.codegen.MethodGen.addSimpleMethodCode(MethodGen.java:316)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:279)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:420)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:345)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:299)
            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
            	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
            Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.ArgType.getPrimitiveType()" because "type" is null
            	at jadx.core.codegen.ClassGen.useType(ClassGen.java:564)
            	at jadx.core.codegen.InsnGen.useType(InsnGen.java:269)
            	at jadx.core.codegen.InsnGen.declareVar(InsnGen.java:166)
            	at jadx.core.codegen.InsnGen.declareVar(InsnGen.java:159)
            	at jadx.core.codegen.InsnGen.assignVar(InsnGen.java:152)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:299)
            	... 11 more
            */
        /*
            Method dump skipped, instruction units count: 672
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.media3.container.NalUnitUtil.parseSpsNalUnitPayload(byte[], int, int):androidx.media3.container.NalUnitUtil$SpsData");
    }

    private static void skipH265DpbSize(ParsableNalUnitBitArray r6, int r7, int[] r8, int[] r9, boolean[][] r10) {
        int r0 = 1;
    L3:
        if (r0 >= r7) goto L24;
        boolean r1 = r6.readBit();
        int r2 = 0;
    L6:
        if (r2 >= r8[r0]) goto L23;
        if (r2 <= 0) goto L10;
        if (r1 == false) goto L10;
        boolean r3 = r6.readBit();
    L13:
        if (r3 == false) goto L22;
        int r4 = 0;
    L16:
        if (r4 >= r9[r0]) goto L21;
        if (r10[r0][r4] == false) goto L20;
        r6.readUnsignedExpGolombCodedInt();
    L20:
        r4 = r4 + 1;
        goto L16
    L21:
        r6.readUnsignedExpGolombCodedInt();
        r6.readUnsignedExpGolombCodedInt();
    L22:
        r2 = r2 + 1;
    L10:
        if (r2 != 0) goto L12;
        r3 = true;
        goto L13
    L12:
        r3 = false;
        goto L13
    L23:
        r0 = r0 + 1;
        goto L3
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1, types: [int] */
    /* JADX WARN: Type inference failed for: r0v3 */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1, types: [int] */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4 */
    /* JADX WARN: Type inference failed for: r8v1, types: [int] */
    private static void skipH265HrdParameters(ParsableNalUnitBitArray r11, boolean r12, int r13) {
        ?? r0 = 0;
        ?? r1 = 0;
        boolean r2 = false;
        if (r12 == false) goto L14;
        boolean r3 = r11.readBit();
        boolean r4 = r11.readBit();
        r0 = r3;
        r1 = r4;
        if (r3 == true) goto L7;
        if (r4 == false) goto L14;
    L7:
        r2 = r11.readBit();
        if (r2 == false) goto L10;
        r11.skipBits(19);
    L10:
        r11.skipBits(8);
        if (r2 == false) goto L13;
        r11.skipBits(4);
    L13:
        r11.skipBits(15);
        r0 = r3;
        r1 = r4;
    L14:
        int r5 = 0;
    L15:
        if (r5 > r13) goto L34;
        boolean r6 = r11.readBit();
        boolean r7 = r6;
        boolean r8 = false;
        int r9 = 0;
        if (r6 == true) goto L19;
        r7 = r11.readBit();
    L19:
        if (r7 == false) goto L21;
        r11.readUnsignedExpGolombCodedInt();
    L22:
        if (r8 == true) goto L24;
        r9 = r11.readUnsignedExpGolombCodedInt();
    L24:
        int r10 = (0 + r0) + r1;
        int r14 = 0;
    L25:
        if (r14 >= r10) goto L33;
        int r15 = 0;
    L27:
        if (r15 > r9) goto L32;
        r11.readUnsignedExpGolombCodedInt();
        r11.readUnsignedExpGolombCodedInt();
        if (r2 == false) goto L31;
        r11.readUnsignedExpGolombCodedInt();
        r11.readUnsignedExpGolombCodedInt();
    L31:
        r11.skipBit();
        r15 = r15 + 1;
        goto L27
    L32:
        r14 = r14 + 1;
        goto L25
    L33:
        r5 = r5 + 1;
        goto L15
    L21:
        r8 = r11.readBit();
        goto L22
    }

    private static void skipH265ScalingList(ParsableNalUnitBitArray r6) {
        int r0 = 0;
    L4:
        if (r0 >= 4) goto L22;
        int r2 = 0;
    L7:
        if (r2 >= 6) goto L21;
        int r4 = 1;
        if (r6.readBit() == true) goto L11;
        r6.readUnsignedExpGolombCodedInt();
    L18:
        if (r0 != 3) goto L20;
        r4 = 3;
    L20:
        r2 = r2 + r4;
        goto L7
    L11:
        int r3 = Math.min(64, 1 << ((r0 << 1) + 4));
        if (r0 <= 1) goto L14;
        r6.readSignedExpGolombCodedInt();
    L14:
        int r5 = 0;
    L15:
        if (r5 >= r3) goto L18;
        r6.readSignedExpGolombCodedInt();
        r5 = r5 + 1;
        goto L15
    L21:
        r0 = r0 + 1;
        goto L4
    }

    private static void skipH265ShortTermReferencePictureSets(ParsableNalUnitBitArray r21) {
        int r0 = r21.readUnsignedExpGolombCodedInt();
        int r1 = -1;
        int r2 = -1;
        boolean r3 = false;
        int[] r4 = new int[0];
        int[] r5 = new int[0];
        int r6 = 0;
    L3:
        if (r6 >= r0) goto L73;
        if (r6 != 0) goto L7;
    L9:
        boolean r8 = r3;
    L10:
        if (r8 == false) goto L59;
        int r9 = r1 + r2;
        boolean r10 = r21.readBit();
        int r12 = (1 - ((r10 ? 1 : 0) * 2)) * (r21.readUnsignedExpGolombCodedInt() + 1);
        boolean[] r13 = new boolean[r9 + 1];
        int r14 = 0;
    L12:
        if (r14 > r9) goto L18;
        if (r21.readBit() == true) goto L16;
        r13[r14] = r21.readBit();
    L17:
        r14 = r14 + 1;
        goto L12
    L16:
        r13[r14] = true;
        goto L17
    L18:
        int r7 = 0;
        int[] r15 = new int[r9 + 1];
        int[] r16 = new int[r9 + 1];
        int r17 = r2 - 1;
    L19:
        if (r17 < 0) goto L26;
        int r18 = r5[r17] + r12;
        if (r18 >= 0) goto L25;
        if (r13[r1 + r17] == false) goto L25;
        r15[r7] = r18;
        r7 = r7 + 1;
    L25:
        r17 = r17 - 1;
        goto L19
    L26:
        if (r12 < 0) goto L28;
    L30:
        int r11 = 0;
    L31:
        if (r11 >= r1) goto L38;
        int r19 = r4[r11] + r12;
        if (r19 >= 0) goto L37;
        if (r13[r11] == false) goto L37;
        r15[r7] = r19;
        r7 = r7 + 1;
    L37:
        r11 = r11 + 1;
        goto L31
    L38:
        int r20 = r7;
        int[] r110 = Arrays.copyOf(r15, r20);
        int r22 = 0;
        int r111 = r1 - 1;
    L39:
        if (r111 < 0) goto L46;
        int r112 = r4[r111] + r12;
        if (r112 <= 0) goto L45;
        if (r13[r111] == false) goto L45;
        r16[r22] = r112;
        r22 = r22 + 1;
    L45:
        r111 = r111 - 1;
        goto L39
    L46:
        if (r12 > 0) goto L48;
    L50:
        int r23 = 0;
    L51:
        if (r23 >= r2) goto L58;
        int r113 = r5[r23] + r12;
        if (r113 <= 0) goto L57;
        if (r13[r1 + r23] == false) goto L57;
        r16[r22] = r113;
        r22 = r22 + 1;
    L57:
        r23 = r23 + 1;
        goto L51
    L58:
        int r24 = r22;
        int[] r25 = Arrays.copyOf(r16, r24);
    L72:
        r1 = r20;
        r2 = r24;
        r4 = r110;
        r5 = r25;
        r6 = r6 + 1;
        r0 = r0;
        r3 = false;
        goto L3
    L48:
        if (r13[r9] == false) goto L50;
        r16[r22] = r12;
        r22 = r22 + 1;
        goto L50
    L28:
        if (r13[r9] == false) goto L30;
        r15[r7] = r12;
        r7 = r7 + 1;
        goto L30
    L59:
        r20 = r21.readUnsignedExpGolombCodedInt();
        r24 = r21.readUnsignedExpGolombCodedInt();
        r110 = new int[r20];
        int r26 = 0;
    L60:
        if (r26 >= r20) goto L65;
        if (r26 <= 0) goto L63;
        int r114 = r110[r26 - 1];
    L64:
        r110[r26] = r114 - (r21.readUnsignedExpGolombCodedInt() + 1);
        r21.skipBit();
        r26 = r26 + 1;
        goto L60
    L63:
        r114 = 0;
        goto L64
    L65:
        int[] r27 = new int[r24];
        int r115 = 0;
    L66:
        if (r115 >= r24) goto L71;
        if (r115 <= 0) goto L69;
        int r116 = r27[r115 - 1];
    L70:
        r27[r115] = r116 + (r21.readUnsignedExpGolombCodedInt() + 1);
        r21.skipBit();
        r115 = r115 + 1;
        goto L66
    L69:
        r116 = 0;
        goto L70
    L71:
        r25 = r27;
        goto L72
    L7:
        if (r21.readBit() == false) goto L9;
        r8 = true;
        goto L10
    }

    private static void skipHrdParameters(ParsableNalUnitBitArray r2) {
        int r0 = r2.readUnsignedExpGolombCodedInt() + 1;
        r2.skipBits(8);
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        r2.readUnsignedExpGolombCodedInt();
        r2.readUnsignedExpGolombCodedInt();
        r2.skipBit();
        r1 = r1 + 1;
        goto L3
    L5:
        r2.skipBits(20);
    }

    private static void skipScalingList(ParsableNalUnitBitArray r5, int r6) {
        int r0 = 8;
        int r1 = 8;
        int r2 = 0;
    L3:
        if (r2 >= r6) goto L10;
        if (r1 == 0) goto L6;
        r1 = ((r0 + r5.readSignedExpGolombCodedInt()) + 256) % 256;
    L6:
        if (r1 != 0) goto L8;
        int r3 = r0;
    L9:
        r0 = r3;
        r2 = r2 + 1;
        goto L3
    L8:
        r3 = r1;
        goto L9
    }

    private static void skipToH265VuiPresentFlagAfterDpbSize(ParsableNalUnitBitArray r4, int r5, boolean[][] r6) {
        int r0 = r4.readUnsignedExpGolombCodedInt() + 2;
        if (r4.readBit() == false) goto L5;
        r4.skipBits(r0);
    L14:
        int r1 = r4.readUnsignedExpGolombCodedInt();
        int r2 = 1;
    L15:
        if (r2 > r1) goto L17;
        r4.skipBits(8);
        r2 = r2 + 1;
        goto L15
    L17:
        return;
    L5:
        int r3 = 1;
    L6:
        if (r3 >= r5) goto L14;
        int r7 = 0;
    L8:
        if (r7 >= r3) goto L13;
        if (r6[r3][r7] == false) goto L12;
        r4.skipBits(r0);
    L12:
        r7 = r7 + 1;
        goto L8
    L13:
        r3 = r3 + 1;
        goto L6
    }

    public static int unescapeStream(byte[] r11, int r12) {
        Object r0 = scratchEscapePositionsLock;
        monitor-enter(r0);
        int r1 = 0;
        int r2 = 0;
    L5:
        if (r1 >= r12) goto L14;
        r1 = findNextUnescapeIndex(r11, r1, r12);     // Catch: Throwable -> L12
        if (r1 >= r12) goto L5;
        if (scratchEscapePositions.length > r2) goto L11;
        scratchEscapePositions = Arrays.copyOf(scratchEscapePositions, scratchEscapePositions.length * 2);     // Catch: Throwable -> L12
    L11:
        scratchEscapePositions[r2] = r1;     // Catch: Throwable -> L12
        r1 = r1 + 3;     // Catch: Throwable -> L12
        r2 = r2 + 1;     // Catch: Throwable -> L12
    L12:
        th = move-exception;
        throw th;
    L14:
        int r3 = r12 - r2;     // Catch: Throwable -> L12
        int r4 = 0;
        int r5 = 0;
        int r6 = 0;
    L15:
        if (r6 >= r2) goto L17;
        int r8 = scratchEscapePositions[r6] - r4;     // Catch: Throwable -> L12
        System.arraycopy(r11, r4, r11, r5, r8);     // Catch: Throwable -> L12
        int r7 = r5 + r8;     // Catch: Throwable -> L12
        int r9 = r7 + 1;     // Catch: Throwable -> L12
        r11[r7] = 0;     // Catch: Throwable -> L12
        r5 = r9 + 1;     // Catch: Throwable -> L12
        r11[r9] = 0;     // Catch: Throwable -> L12
        r4 = r4 + (r8 + 3);     // Catch: Throwable -> L12
        r6 = r6 + 1;     // Catch: Throwable -> L12
        goto L15
    L17:
        System.arraycopy(r11, r4, r11, r5, r3 - r5);     // Catch: Throwable -> L12
        monitor-exit(r0);     // Catch: Throwable -> L12
        return r3;
    }
}
