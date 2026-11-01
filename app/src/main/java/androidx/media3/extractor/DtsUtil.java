package androidx.media3.extractor;

import androidx.media3.common.C0180C;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.media3.extractor.p001ts.PsExtractor;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public final class DtsUtil {
    private static final int[] CHANNELS_BY_AMODE = null;
    public static final int DTS_EXPRESS_MAX_RATE_BITS_PER_SECOND = 768000;
    public static final int DTS_HD_MAX_RATE_BYTES_PER_SECOND = 2250000;
    public static final int DTS_MAX_RATE_BYTES_PER_SECOND = 192000;
    private static final byte FIRST_BYTE_14B_BE = 31;
    private static final byte FIRST_BYTE_14B_LE = -1;
    private static final byte FIRST_BYTE_BE = 127;
    private static final byte FIRST_BYTE_EXTSS_BE = 100;
    private static final byte FIRST_BYTE_EXTSS_LE = 37;
    private static final byte FIRST_BYTE_LE = -2;
    private static final byte FIRST_BYTE_UHD_FTOC_NONSYNC_BE = 113;
    private static final byte FIRST_BYTE_UHD_FTOC_NONSYNC_LE = -24;
    private static final byte FIRST_BYTE_UHD_FTOC_SYNC_BE = 64;
    private static final byte FIRST_BYTE_UHD_FTOC_SYNC_LE = -14;
    public static final int FRAME_TYPE_CORE = 1;
    public static final int FRAME_TYPE_EXTENSION_SUBSTREAM = 2;
    public static final int FRAME_TYPE_UHD_NON_SYNC = 4;
    public static final int FRAME_TYPE_UHD_SYNC = 3;
    public static final int FRAME_TYPE_UNKNOWN = 0;
    private static final int[] SAMPLE_RATE_BY_INDEX = null;
    private static final int[] SAMPLE_RATE_BY_SFREQ = null;
    private static final int SYNC_VALUE_14B_BE = 536864768;
    private static final int SYNC_VALUE_14B_LE = -14745368;
    private static final int SYNC_VALUE_BE = 2147385345;
    private static final int SYNC_VALUE_EXTSS_BE = 1683496997;
    private static final int SYNC_VALUE_EXTSS_LE = 622876772;
    private static final int SYNC_VALUE_LE = -25230976;
    private static final int SYNC_VALUE_UHD_FTOC_NONSYNC_BE = 1908687592;
    private static final int SYNC_VALUE_UHD_FTOC_NONSYNC_LE = -398277519;
    private static final int SYNC_VALUE_UHD_FTOC_SYNC_BE = 1078008818;
    private static final int SYNC_VALUE_UHD_FTOC_SYNC_LE = -233094848;
    private static final int[] TWICE_BITRATE_KBPS_BY_RATE = null;
    private static final int[] UHD_AUDIO_CHUNK_ID_LENGTH_TABLE = null;
    private static final int[] UHD_AUDIO_CHUNK_SIZE_LENGTH_TABLE = null;
    private static final int[] UHD_FTOC_PAYLOAD_LENGTH_TABLE = null;
    private static final int[] UHD_HEADER_SIZE_LENGTH_TABLE = null;
    private static final int[] UHD_METADATA_CHUNK_SIZE_LENGTH_TABLE = null;

    /* JADX INFO: renamed from: androidx.media3.extractor.DtsUtil$1 */
    static /* synthetic */ class C04041 {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface DtsAudioMimeType {
    }

    public static final class DtsHeader {
        public final int bitrate;
        public final int channelCount;
        public final long frameDurationUs;
        public final int frameSize;
        public final String mimeType;
        public final int sampleRate;

        private DtsHeader(String r1, int r2, int r3, int r4, long r5, int r7) {
            this.mimeType = r1;
            this.channelCount = r2;
            this.sampleRate = r3;
            this.frameSize = r4;
            this.frameDurationUs = r5;
            this.bitrate = r7;
        }

        /* synthetic */ DtsHeader(String r1, int r2, int r3, int r4, long r5, int r7, C04041 r8) {
            this(r1, r2, r3, r4, r5, r7);
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface FrameType {
    }

    static {
        CHANNELS_BY_AMODE = new int[]{1, 2, 2, 2, 2, 3, 3, 4, 4, 5, 6, 6, 6, 7, 8, 8};
        SAMPLE_RATE_BY_SFREQ = new int[]{-1, 8000, AacUtil.AAC_HE_V1_MAX_RATE_BYTES_PER_SECOND, 32000, -1, -1, 11025, 22050, 44100, -1, -1, 12000, 24000, OpusUtil.SAMPLE_RATE, -1, -1};
        TWICE_BITRATE_KBPS_BY_RATE = new int[]{64, 112, 128, PsExtractor.AUDIO_STREAM, 224, 256, RendererCapabilities.DECODER_SUPPORT_MASK, 448, 512, 640, 768, 896, 1024, 1152, 1280, 1536, 1920, 2048, 2304, 2560, 2688, 2816, 2823, 2944, 3072, 3840, 4096, 6144, 7680};
        SAMPLE_RATE_BY_INDEX = new int[]{8000, AacUtil.AAC_HE_V1_MAX_RATE_BYTES_PER_SECOND, 32000, 64000, 128000, 22050, 44100, 88200, 176400, 352800, 12000, 24000, OpusUtil.SAMPLE_RATE, 96000, DTS_MAX_RATE_BYTES_PER_SECOND, 384000};
        UHD_FTOC_PAYLOAD_LENGTH_TABLE = new int[]{5, 8, 10, 12};
        UHD_METADATA_CHUNK_SIZE_LENGTH_TABLE = new int[]{6, 9, 12, 15};
        UHD_AUDIO_CHUNK_ID_LENGTH_TABLE = new int[]{2, 4, 6, 8};
        UHD_AUDIO_CHUNK_SIZE_LENGTH_TABLE = new int[]{9, 11, 13, 16};
        UHD_HEADER_SIZE_LENGTH_TABLE = new int[]{5, 8, 10, 12};
    }

    private DtsUtil() {
    }

    private static void checkCrc(byte[] r5, int r6) throws ParserException {
        if ((((r5[r6 - 2] << 8) & 65535) | (r5[r6 - 1] & 255)) != Util.crc16(r5, 0, r6 - 2, 65535)) goto L6;
        return;
    L6:
        throw ParserException.createForMalformedContainer("CRC check failed", null);
    }

    public static int getDtsFrameSize(byte[] r5) {
        boolean r0 = false;
        switch(r5[0]) {
            case -2: goto L7;
            case -1: goto L6;
            case 31: goto L5;
            default: goto L4;
        };
    L4:
        int r1 = ((((r5[5] & 3) << 12) | ((r5[6] & 255) << 4)) | ((r5[7] & 240) >> 4)) + 1;
    L8:
        if (r0 == false) goto L11;
        return (r1 * 16) / 14;
    L11:
        return r1;
    L5:
        r1 = ((((r5[6] & 3) << 12) | ((r5[7] & 255) << 4)) | ((r5[8] & 60) >> 2)) + 1;
        r0 = true;
        goto L8
    L6:
        r1 = ((((r5[7] & 3) << 12) | ((r5[6] & 255) << 4)) | ((r5[9] & 60) >> 2)) + 1;
        r0 = true;
        goto L8
    L7:
        r1 = ((((r5[4] & 3) << 12) | ((r5[7] & 255) << 4)) | ((r5[6] & 240) >> 4)) + 1;
        goto L8
    }

    public static int getFrameType(int r1) {
        if (r1 != SYNC_VALUE_BE) goto L5;
        return 1;
    L5:
        if (r1 != SYNC_VALUE_LE) goto L7;
        return 1;
    L7:
        if (r1 != SYNC_VALUE_14B_BE) goto L9;
        return 1;
    L9:
        if (r1 != SYNC_VALUE_14B_LE) goto L12;
        return 1;
    L12:
        if (r1 != SYNC_VALUE_EXTSS_BE) goto L14;
        return 2;
    L14:
        if (r1 != SYNC_VALUE_EXTSS_LE) goto L17;
        return 2;
    L17:
        if (r1 != SYNC_VALUE_UHD_FTOC_SYNC_BE) goto L19;
        return 3;
    L19:
        if (r1 != SYNC_VALUE_UHD_FTOC_SYNC_LE) goto L22;
        return 3;
    L22:
        if (r1 != SYNC_VALUE_UHD_FTOC_NONSYNC_BE) goto L24;
        return 4;
    L24:
        if (r1 == SYNC_VALUE_UHD_FTOC_NONSYNC_LE) goto L36;
        return 0;
    L36:
        return 4;
    }

    private static ParsableBitArray getNormalizedFrame(byte[] r4) {
        if (r4[0] == 127) goto L26;
        if (r4[0] == 100) goto L26;
        if (r4[0] == 64) goto L26;
        if (r4[0] == 113) goto L26;
        byte[] r5 = Arrays.copyOf(r4, r4.length);
        if (isLittleEndianFrameHeader(r5) == false) goto L17;
        int r1 = 0;
    L15:
        if (r1 >= (r5.length - 1)) goto L17;
        byte r2 = r5[r1];
        r5[r1] = r5[r1 + 1];
        r5[r1 + 1] = r2;
        r1 = r1 + 2;
    L17:
        ParsableBitArray r3 = new ParsableBitArray(r5);
        if (r5[0] != 31) goto L23;
        ParsableBitArray r0 = new ParsableBitArray(r5);
    L21:
        if (r0.bitsLeft() < 16) goto L23;
        r0.skipBits(2);
        r3.putInt(r0.readBits(14), 14);
    L23:
        r3.reset(r5);
        return r3;
    L26:
        return new ParsableBitArray(r4);
    }

    private static boolean isLittleEndianFrameHeader(byte[] r3) {
        if (r3[0] != (-2)) goto L5;
    L12:
        return true;
    L5:
        if (r3[0] == (-1)) goto L12;
        if (r3[0] == 37) goto L12;
        if (r3[0] == (-14)) goto L12;
        if (r3[0] == (-24)) goto L12;
        return false;
    }

    public static int parseDtsAudioSampleCount(ByteBuffer r3) {
        if (r3.getInt(0) != SYNC_VALUE_UHD_FTOC_SYNC_LE) goto L5;
        return 1024;
    L5:
        if (r3.getInt(0) != SYNC_VALUE_UHD_FTOC_NONSYNC_LE) goto L8;
        return 1024;
    L8:
        if (r3.getInt(0) != SYNC_VALUE_EXTSS_LE) goto L11;
        return 4096;
    L11:
        int r0 = r3.position();
        switch(r3.get(r0)) {
            case -2: goto L16;
            case -1: goto L15;
            case 31: goto L14;
            default: goto L13;
        };
    L13:
        int r1 = ((r3.get(r0 + 4) & 1) << 6) | ((r3.get(r0 + 5) & 252) >> 2);
    L18:
        return (r1 + 1) * 32;
    L14:
        r1 = ((r3.get(r0 + 5) & 7) << 4) | ((r3.get(r0 + 6) & 60) >> 2);
        goto L18
    L15:
        r1 = ((r3.get(r0 + 4) & 7) << 4) | ((r3.get(r0 + 7) & 60) >> 2);
        goto L18
    L16:
        r1 = ((r3.get(r0 + 5) & 1) << 6) | ((r3.get(r0 + 4) & 252) >> 2);
        goto L18
    }

    public static int parseDtsAudioSampleCount(byte[] r5) {
        switch(r5[0]) {
            case -2: goto L7;
            case -1: goto L6;
            case 31: goto L5;
            default: goto L4;
        };
    L4:
        int r0 = ((r5[4] & 1) << 6) | ((r5[5] & 252) >> 2);
    L9:
        return (r0 + 1) * 32;
    L5:
        r0 = ((r5[5] & 7) << 4) | ((r5[6] & 60) >> 2);
        goto L9
    L6:
        r0 = ((r5[4] & 7) << 4) | ((r5[7] & 60) >> 2);
        goto L9
    L7:
        r0 = ((r5[5] & 1) << 6) | ((r5[4] & 252) >> 2);
        goto L9
    }

    public static Format parseDtsFormat(byte[] r9, String r10, String r11, int r12, DrmInitData r13) {
        ParsableBitArray r0 = getNormalizedFrame(r9);
        r0.skipBits(60);
        int r1 = r0.readBits(6);
        int r2 = CHANNELS_BY_AMODE[r1];
        int r3 = r0.readBits(4);
        int r4 = SAMPLE_RATE_BY_SFREQ[r3];
        int r5 = r0.readBits(5);
        if (r5 < TWICE_BITRATE_KBPS_BY_RATE.length) goto L5;
        int r6 = -1;
    L6:
        r0.skipBits(10);
        if (r0.readBits(2) <= 0) goto L9;
        int r7 = 1;
    L10:
        int r8 = r2 + r7;
        return new Format.Builder().setId(r10).setSampleMimeType(MimeTypes.AUDIO_DTS).setAverageBitrate(r6).setChannelCount(r8).setSampleRate(r4).setDrmInitData(r13).setLanguage(r11).setRoleFlags(r12).build();
    L9:
        r7 = 0;
        goto L10
    L5:
        r6 = (TWICE_BITRATE_KBPS_BY_RATE[r5] * 1000) / 2;
        goto L6
    }

    public static DtsHeader parseDtsHdHeader(byte[] r22) throws ParserException {
        ParsableBitArray r0 = getNormalizedFrame(r22);
        r0.skipBits(40);
        int r2 = r0.readBits(2);
        if (r0.readBit() == true) goto L5;
        int r3 = 8;
        int r4 = 16;
    L6:
        r0.skipBits(r3);
        int r11 = r0.readBits(r4) + 1;
        boolean r5 = r0.readBit();
        if (r5 == false) goto L29;
        int r8 = r0.readBits(2);
        int r10 = (r0.readBits(3) + 1) * 512;
        if (r0.readBit() == false) goto L11;
        r0.skipBits(36);
    L11:
        int r12 = r0.readBits(3) + 1;
        int r9 = r0.readBits(3) + 1;
        if (r12 != 1) goto L28;
        if (r9 != 1) goto L28;
        int r13 = r0.readBits(r2 + 1);
        int r14 = 0;
    L16:
        if (r14 >= (r2 + 1)) goto L22;
        if (((r13 >> r14) & 1) != 1) goto L20;
        r0.skipBits(8);
    L20:
        r14 = r14 + 1;
        goto L16
    L22:
        if (r0.readBit() == false) goto L26;
        r0.skipBits(2);
        int r15 = (r0.readBits(2) + 1) << 2;
        int r1 = r0.readBits(2) + 1;
        int r16 = 0;
    L24:
        if (r16 >= r1) goto L26;
        r0.skipBits(r15);
        r16 = r16 + 1;
    L26:
        int r6 = r8;
        int r17 = r10;
    L30:
        r0.skipBits(r4);
        r0.skipBits(12);
        if (r5 == true) goto L33;
        int r18 = -2147483647;
        int r19 = -1;
    L44:
        if (r5 == false) goto L52;
        switch(r6) {
            case 0: goto L50;
            case 1: goto L49;
            case 2: goto L48;
            default: goto L47;
        };
    L48:
        int r110 = OpusUtil.SAMPLE_RATE;
    L51:
        long r111 = Util.scaleLargeTimestamp(r17, 1000000, r110);
    L54:
        return new DtsHeader(MimeTypes.AUDIO_DTS_EXPRESS, r19, r18, r11, r111, 0, null);
    L49:
        r110 = 44100;
        goto L51
    L50:
        r110 = 32000;
        goto L51
    L47:
        throw ParserException.createForMalformedContainer("Unsupported reference clock code in DTS HD header: " + r6, null);
    L52:
        r111 = -9223372036854775807L;
        goto L54
    L33:
        if (r0.readBit() == false) goto L36;
        r0.skipBits(4);
    L36:
        if (r0.readBit() == false) goto L39;
        r0.skipBits(24);
    L39:
        if (r0.readBit() == false) goto L41;
        r0.skipBytes(r0.readBits(10) + 1);
    L41:
        r0.skipBits(5);
        r18 = SAMPLE_RATE_BY_INDEX[r0.readBits(4)];
        r19 = r0.readBits(8) + 1;
    L28:
        throw ParserException.createForUnsupportedContainerFeature("Multiple audio presentations or assets not supported");
    L29:
        r6 = -1;
        r17 = 0;
        goto L30
    L5:
        r3 = 12;
        r4 = 20;
        goto L6
    }

    public static int parseDtsHdHeaderSize(byte[] r3) {
        ParsableBitArray r0 = getNormalizedFrame(r3);
        r0.skipBits(42);
        if (r0.readBit() == false) goto L5;
        int r1 = 12;
    L7:
        return r0.readBits(r1) + 1;
    L5:
        r1 = 8;
        goto L7
    }

    public static DtsHeader parseDtsUhdHeader(byte[] r25, AtomicInteger r26) throws ParserException {
        ParsableBitArray r0 = getNormalizedFrame(r25);
        if (r0.readBits(32) != SYNC_VALUE_UHD_FTOC_SYNC_BE) goto L5;
        boolean r2 = true;
    L6:
        int r5 = parseUnsignedVarInt(r0, UHD_FTOC_PAYLOAD_LENGTH_TABLE, true) + 1;
        long r7 = C0180C.TIME_UNSET;
        if (r2 == true) goto L9;
        int r13 = -2147483647;
    L31:
        int r3 = 0;
        if (r2 == false) goto L34;
        int r10 = 1;
    L35:
        int r4 = r10;
        int r6 = 0;
    L36:
        if (r6 >= r4) goto L38;
        r3 = r3 + parseUnsignedVarInt(r0, UHD_METADATA_CHUNK_SIZE_LENGTH_TABLE, true);
        r6 = r6 + 1;
        goto L36
    L38:
        int r8 = 0;
    L39:
        if (r8 >= 1) goto L49;
        if (r2 == false) goto L42;
        boolean r11 = true;
        AtomicInteger r12 = r26;
        r12.set(parseUnsignedVarInt(r0, UHD_AUDIO_CHUNK_ID_LENGTH_TABLE, true));
    L44:
        if (r12.get() == 0) goto L46;
        int r14 = parseUnsignedVarInt(r0, UHD_AUDIO_CHUNK_SIZE_LENGTH_TABLE, r11);
    L47:
        r3 = r3 + r14;
        r8 = r8 + 1;
        goto L39
    L46:
        r14 = 0;
        goto L47
    L42:
        r12 = r26;
        r11 = true;
        goto L44
    L49:
        return new DtsHeader(MimeTypes.AUDIO_DTS_X, 2, r13, r5 + r3, r7, 0, null);
    L34:
        r10 = 0;
        goto L35
    L9:
        if (r0.readBit() == false) goto L29;
        checkCrc(r25, r5);
        int r15 = r0.readBits(2);
        switch(r15) {
            case 0: goto L16;
            case 1: goto L15;
            case 2: goto L14;
            default: goto L13;
        };
    L14:
        int r16 = RendererCapabilities.DECODER_SUPPORT_MASK;
    L17:
        int r17 = (r0.readBits(3) + 1) * r16;
        int r18 = r0.readBits(2);
        switch(r18) {
            case 0: goto L23;
            case 1: goto L22;
            case 2: goto L21;
            default: goto L20;
        };
    L21:
        int r19 = OpusUtil.SAMPLE_RATE;
    L25:
        if (r0.readBit() == false) goto L27;
        r0.skipBits(36);
    L27:
        int r9 = r19 * (1 << r0.readBits(2));
        r7 = Util.scaleLargeTimestamp(r17, 1000000, r19);
        r13 = r9;
        goto L31
    L22:
        r19 = 44100;
        goto L25
    L23:
        r19 = 32000;
        goto L25
    L20:
        throw ParserException.createForMalformedContainer("Unsupported clock rate index in DTS UHD header: " + r18, null);
    L15:
        r16 = 480;
        goto L17
    L16:
        r16 = 512;
        goto L17
    L13:
        throw ParserException.createForMalformedContainer("Unsupported base duration index in DTS UHD header: " + r15, null);
    L29:
        throw ParserException.createForUnsupportedContainerFeature("Only supports full channel mask-based audio presentation");
    L5:
        r2 = false;
        goto L6
    }

    public static int parseDtsUhdHeaderSize(byte[] r3) {
        ParsableBitArray r0 = getNormalizedFrame(r3);
        r0.skipBits(32);
        return parseUnsignedVarInt(r0, UHD_HEADER_SIZE_LENGTH_TABLE, true) + 1;
    }

    private static int parseUnsignedVarInt(ParsableBitArray r5, int[] r6, boolean r7) {
        int r0 = 0;
        int r1 = 0;
    L4:
        if (r1 >= 3) goto L8;
        if (r5.readBit() == false) goto L8;
        r0 = r0 + 1;
        r1 = r1 + 1;
    L8:
        int r2 = 0;
        if (r7 == false) goto L14;
        int r3 = 0;
    L11:
        if (r3 >= r0) goto L14;
        r2 = r2 + (1 << r6[r3]);
        r3 = r3 + 1;
    L14:
        return r5.readBits(r6[r0]) + r2;
    }
}
