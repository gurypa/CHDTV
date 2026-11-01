package androidx.media3.extractor.p001ts;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.extractor.AacUtil;
import androidx.media3.extractor.MpegAudioUtil;
import androidx.media3.extractor.OpusUtil;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
final class MpeghUtil {
    private static final int MHAS_SYNC_WORD = 12583333;

    /* JADX INFO: renamed from: androidx.media3.extractor.ts.MpeghUtil$1 */
    static /* synthetic */ class C04481 {
    }

    public static class MhasPacketHeader {
        public static final int PACTYPE_EARCON = 19;
        public static final int PACTYPE_PCMCONFIG = 20;
        public static final int PACTYPE_PCMDATA = 21;
        public static final int PACTYP_AUDIOSCENEINFO = 3;
        public static final int PACTYP_AUDIOTRUNCATION = 17;
        public static final int PACTYP_BUFFERINFO = 14;
        public static final int PACTYP_CRC16 = 9;
        public static final int PACTYP_CRC32 = 10;
        public static final int PACTYP_DESCRIPTOR = 11;
        public static final int PACTYP_FILLDATA = 0;
        public static final int PACTYP_GENDATA = 18;
        public static final int PACTYP_GLOBAL_CRC16 = 15;
        public static final int PACTYP_GLOBAL_CRC32 = 16;
        public static final int PACTYP_LOUDNESS = 22;
        public static final int PACTYP_LOUDNESS_DRC = 13;
        public static final int PACTYP_MARKER = 8;
        public static final int PACTYP_MPEGH3DACFG = 1;
        public static final int PACTYP_MPEGH3DAFRAME = 2;
        public static final int PACTYP_SYNC = 6;
        public static final int PACTYP_SYNCGAP = 7;
        public static final int PACTYP_USERINTERACTION = 12;
        public long packetLabel;
        public int packetLength;
        public int packetType;

        @Target({ElementType.TYPE_USE})
        @Documented
        @Retention(RetentionPolicy.SOURCE)
        public @interface Type {
        }

        public MhasPacketHeader() {
        }
    }

    public static class Mpegh3daConfig {
        public final byte[] compatibleProfileLevelSet;
        public final int profileLevelIndication;
        public final int samplingFrequency;
        public final int standardFrameLength;

        private Mpegh3daConfig(int r1, int r2, int r3, byte[] r4) {
            this.profileLevelIndication = r1;
            this.samplingFrequency = r2;
            this.standardFrameLength = r3;
            this.compatibleProfileLevelSet = r4;
        }

        /* synthetic */ Mpegh3daConfig(int r1, int r2, int r3, byte[] r4, C04481 r5) {
            this(r1, r2, r3, r4);
        }
    }

    private MpeghUtil() {
    }

    private static int getOutputFrameLength(int r2) throws ParserException {
        switch(r2) {
            case 0: goto L11;
            case 1: goto L9;
            case 2: goto L7;
            case 3: goto L7;
            case 4: goto L5;
            default: goto L4;
        };
    L5:
        return 4096;
    L7:
        return 2048;
    L9:
        return 1024;
    L11:
        return 768;
    L4:
        throw ParserException.createForUnsupportedContainerFeature("Unsupported coreSbrFrameLengthIndex " + r2);
    }

    private static double getResamplingRatio(int r2) throws ParserException {
        switch(r2) {
            case 14700: goto L11;
            case 16000: goto L11;
            case 22050: goto L9;
            case 24000: goto L9;
            case 29400: goto L7;
            case 32000: goto L7;
            case 44100: goto L5;
            case 48000: goto L5;
            case 58800: goto L7;
            case 64000: goto L7;
            case 88200: goto L5;
            case 96000: goto L5;
            default: goto L4;
        };
    L5:
        return 1.0d;
    L7:
        return 1.5d;
    L9:
        return 2.0d;
    L11:
        return 3.0d;
    L4:
        throw ParserException.createForUnsupportedContainerFeature("Unsupported sampling rate " + r2);
    }

    private static int getSamplingFrequency(int r2) throws ParserException {
        switch(r2) {
            case 0: goto L55;
            case 1: goto L53;
            case 2: goto L51;
            case 3: goto L49;
            case 4: goto L47;
            case 5: goto L45;
            case 6: goto L43;
            case 7: goto L41;
            case 8: goto L39;
            case 9: goto L37;
            case 10: goto L35;
            case 11: goto L33;
            case 12: goto L31;
            case 13: goto L4;
            case 14: goto L4;
            case 15: goto L29;
            case 16: goto L27;
            case 17: goto L25;
            case 18: goto L23;
            case 19: goto L21;
            case 20: goto L19;
            case 21: goto L17;
            case 22: goto L15;
            case 23: goto L13;
            case 24: goto L11;
            case 25: goto L9;
            case 26: goto L7;
            case 27: goto L5;
            default: goto L4;
        };
    L5:
        return 9600;
    L7:
        return 12800;
    L9:
        return 14400;
    L11:
        return 17075;
    L13:
        return 19200;
    L15:
        return AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH;
    L17:
        return 25600;
    L19:
        return 28800;
    L21:
        return 34150;
    L23:
        return 38400;
    L25:
        return MpegAudioUtil.MAX_RATE_BYTES_PER_SECOND;
    L27:
        return 51200;
    L29:
        return 57600;
    L31:
        return 7350;
    L33:
        return 8000;
    L35:
        return 11025;
    L37:
        return 12000;
    L39:
        return AacUtil.AAC_HE_V1_MAX_RATE_BYTES_PER_SECOND;
    L41:
        return 22050;
    L43:
        return 24000;
    L45:
        return 32000;
    L47:
        return 44100;
    L49:
        return OpusUtil.SAMPLE_RATE;
    L51:
        return 64000;
    L53:
        return 88200;
    L55:
        return 96000;
    L4:
        throw ParserException.createForUnsupportedContainerFeature("Unsupported sampling rate index " + r2);
    }

    private static int getSbrRatioIndex(int r2) throws ParserException {
        switch(r2) {
            case 0: goto L11;
            case 1: goto L11;
            case 2: goto L9;
            case 3: goto L7;
            case 4: goto L5;
            default: goto L4;
        };
    L5:
        return 1;
    L7:
        return 3;
    L9:
        return 2;
    L11:
        return 0;
    L4:
        throw ParserException.createForUnsupportedContainerFeature("Unsupported coreSbrFrameLengthIndex " + r2);
    }

    public static boolean isSyncWord(int r2) {
        if ((16777215 & r2) != MHAS_SYNC_WORD) goto L5;
        return true;
    L5:
        return false;
    }

    public static int parseAudioTruncationInfo(ParsableBitArray r1) {
        if (r1.readBit() == false) goto L6;
        r1.skipBits(2);
        return r1.readBits(13);
    L6:
        return 0;
    }

    public static boolean parseMhasPacketHeader(ParsableBitArray r7, MhasPacketHeader r8) throws ParserException {
        r7.getBytePosition();
        r8.packetType = readEscapedIntValue(r7, 3, 8, 8);
        if (r8.packetType != (-1)) goto L5;
        return false;
    L5:
        r8.packetLabel = readEscapedLongValue(r7, 2, 8, 32);
        if (r8.packetLabel != (-1)) goto L9;
        return false;
    L9:
        if (r8.packetLabel > 16) goto L26;
        if (r8.packetLabel == 0) goto L13;
    L21:
        r8.packetLength = readEscapedIntValue(r7, 11, 24, 24);
        if (r8.packetLength == (-1)) goto L27;
        return true;
    L27:
        return false;
    L13:
        switch(r8.packetType) {
            case 1: goto L20;
            case 2: goto L18;
            case 17: goto L16;
            default: goto L21;
        };
    L16:
        throw ParserException.createForMalformedContainer("AudioTruncation packet with invalid packet label 0", null);
    L18:
        throw ParserException.createForMalformedContainer("Mpegh3daFrame packet with invalid packet label 0", null);
    L20:
        throw ParserException.createForMalformedContainer("Mpegh3daConfig packet with invalid packet label 0", null);
    L26:
        throw ParserException.createForUnsupportedContainerFeature("Contains sub-stream with an invalid packet label " + r8.packetLabel);
    }

    public static Mpegh3daConfig parseMpegh3daConfig(ParsableBitArray r17) throws ParserException {
        byte[] r1 = null;
        int r4 = r17.readBits(8);
        int r9 = r17.readBits(5);
        if (r9 != 31) goto L5;
        int r10 = r17.readBits(24);
    L6:
        int r11 = r17.readBits(3);
        int r12 = getOutputFrameLength(r11);
        int r13 = getSbrRatioIndex(r11);
        r17.skipBits(2);
        skipSpeakerConfig3d(r17);
        skipMpegh3daDecoderConfig(r17, parseSignals3d(r17), r13);
        if (r17.readBit() == false) goto L19;
        int r5 = 4;
        int r3 = readEscapedIntValue(r17, 2, 4, 8) + 1;
        int r6 = 0;
    L9:
        if (r6 >= r3) goto L18;
        int r8 = readEscapedIntValue(r17, r5, 8, 16);
        int r7 = readEscapedIntValue(r17, r5, 8, 16);
        if (r8 != 7) goto L16;
        int r15 = r17.readBits(r5) + 1;
        r17.skipBits(r5);
        r1 = new byte[r15];
        int r14 = 0;
    L13:
        if (r14 >= r15) goto L17;
        byte[] r16 = r1;
        r16[r14] = (byte) r17.readBits(8);
        r14 = r14 + 1;
        r1 = r16;
    L17:
        r6 = r6 + 1;
        r5 = 4;
        goto L9
    L16:
        r17.skipBits(r7 * 8);
        goto L17
    L18:
        byte[] r18 = r1;
    L20:
        double r2 = getResamplingRatio(r10);
        return new Mpegh3daConfig(r4, (int) (((double) r10) * r2), (int) (((double) r12) * r2), r18, null);
    L19:
        r18 = null;
        goto L20
    L5:
        r10 = getSamplingFrequency(r9);
        goto L6
    }

    private static boolean parseMpegh3daCoreConfig(ParsableBitArray r2) {
        r2.skipBits(3);
        boolean r0 = r2.readBit();
        if (r0 == false) goto L5;
        r2.skipBits(13);
    L5:
        return r0;
    }

    private static int parseSignals3d(ParsableBitArray r7) {
        int r0 = 0;
        int r2 = r7.readBits(5);
        int r3 = 0;
    L4:
        if (r3 >= (r2 + 1)) goto L13;
        int r4 = r7.readBits(3);
        r0 = r0 + (readEscapedIntValue(r7, 5, 8, 16) + 1);
        if (r4 == 0) goto L10;
        if (r4 == 2) goto L10;
    L12:
        r3 = r3 + 1;
    L10:
        if (r7.readBit() == false) goto L12;
        skipSpeakerConfig3d(r7);
        goto L12
    L13:
        return r0;
    }

    private static int readEscapedIntValue(ParsableBitArray r7, int r8, int r9, int r10) {
        if (Math.max(Math.max(r8, r9), r10) > 31) goto L5;
        boolean r1 = true;
    L6:
        Assertions.checkArgument(r1);
        IntMath.checkedAdd(IntMath.checkedAdd((1 << r8) - 1, (1 << r9) - 1), 1 << r10);
        if (r7.bitsLeft() >= r8) goto L9;
        return -1;
    L9:
        int r3 = r7.readBits(r8);
        if (r3 == ((1 << r8) - 1)) goto L12;
        return r3;
    L12:
        if (r7.bitsLeft() >= r9) goto L14;
        return -1;
    L14:
        int r5 = r7.readBits(r9);
        int r4 = r3 + r5;
        if (r5 == ((1 << r9) - 1)) goto L17;
        return r4;
    L17:
        if (r7.bitsLeft() >= r10) goto L20;
        return -1;
    L20:
        return r4 + r7.readBits(r10);
    L5:
        r1 = false;
        goto L6
    }

    private static long readEscapedLongValue(ParsableBitArray r18, int r19, int r20, int r21) {
        if (Math.max(Math.max(r19, r20), r21) > 63) goto L5;
        boolean r5 = true;
    L6:
        Assertions.checkArgument(r5);
        LongMath.checkedAdd(LongMath.checkedAdd((1 << r19) - 1, (1 << r20) - 1), 1 << r21);
        if (r18.bitsLeft() >= r19) goto L9;
        return -1;
    L9:
        long r12 = r18.readBitsToLong(r19);
        if (r12 == ((1 << r19) - 1)) goto L12;
        return r12;
    L12:
        if (r18.bitsLeft() >= r20) goto L14;
        return -1;
    L14:
        long r14 = r18.readBitsToLong(r20);
        long r13 = r12 + r14;
        if (r14 == ((1 << r20) - 1)) goto L17;
        return r13;
    L17:
        if (r18.bitsLeft() >= r21) goto L20;
        return -1;
    L20:
        return r13 + r18.readBitsToLong(r21);
    L5:
        r5 = false;
        goto L6
    }

    private static void skipMpegh3daDecoderConfig(ParsableBitArray r13, int r14, int r15) {
        int r3 = readEscapedIntValue(r13, 4, 8, 16) + 1;
        r13.skipBit();
        int r4 = 0;
    L3:
        if (r4 >= r3) goto L44;
        switch(r13.readBits(2)) {
            case 0: goto L40;
            case 1: goto L14;
            case 2: goto L43;
            case 3: goto L7;
            default: goto L43;
        };
    L7:
        readEscapedIntValue(r13, 4, 8, 16);
        int r5 = readEscapedIntValue(r13, 4, 8, 16);
        if (r13.readBit() == false) goto L10;
        readEscapedIntValue(r13, 8, 16, 0);
    L10:
        r13.skipBit();
        if (r5 <= 0) goto L43;
        r13.skipBits(r5 * 8);
        goto L43
    L40:
        parseMpegh3daCoreConfig(r13);
        if (r15 <= 0) goto L43;
        skipSbrConfig(r13);
        goto L43
    L14:
        if (parseMpegh3daCoreConfig(r13) == false) goto L16;
        r13.skipBit();
    L16:
        int r8 = 0;
        if (r15 <= 0) goto L19;
        skipSbrConfig(r13);
        r8 = r13.readBits(2);
    L19:
        if (r8 <= 0) goto L29;
        r13.skipBits(6);
        int r10 = r13.readBits(2);
        r13.skipBits(4);
        if (r13.readBit() == false) goto L23;
        r13.skipBits(5);
    L23:
        if (r8 != 2) goto L25;
    L26:
        r13.skipBits(6);
    L27:
        if (r10 != 2) goto L29;
        r13.skipBit();
        goto L29
    L25:
        if (r8 != 3) goto L27;
    L29:
        int r9 = ((int) Math.floor(Math.log(r14 - 1) / Math.log(2.0d))) + 1;
        int r6 = r13.readBits(2);
        if (r6 <= 0) goto L35;
        if (r13.readBit() == false) goto L35;
        r13.skipBits(r9);
    L35:
        if (r13.readBit() == false) goto L37;
        r13.skipBits(r9);
    L37:
        if (r15 != 0) goto L43;
        if (r6 != 0) goto L43;
        r13.skipBit();
    L43:
        r4 = r4 + 1;
        goto L3
    }

    private static void skipMpegh3daFlexibleSpeakerConfig(ParsableBitArray r12, int r13) {
        boolean r0 = r12.readBit();
        int r1 = 5;
        if (r0 == false) goto L5;
        int r3 = 1;
    L7:
        if (r0 == false) goto L9;
        r1 = 7;
    L9:
        if (r0 == false) goto L11;
        int r5 = 8;
    L12:
        int r6 = 0;
    L13:
        if (r6 >= r13) goto L33;
        int r7 = 0;
        if (r12.readBit() == false) goto L18;
        r12.skipBits(7);
    L27:
        if (r7 == 0) goto L32;
        if (r7 == 180) goto L32;
        if (r12.readBit() == false) goto L32;
        r6 = r6 + 1;
    L32:
        r6 = r6 + 1;
        goto L13
    L18:
        if (r12.readBits(2) == 3) goto L20;
    L22:
        r7 = r12.readBits(r5) * r3;
        if (r7 == 0) goto L26;
        if (r7 == 180) goto L26;
        r12.skipBit();
    L26:
        r12.skipBit();
        goto L27
    L20:
        if ((r12.readBits(r1) * r3) == 0) goto L22;
        r12.skipBit();
        goto L22
    L33:
        return;
    L11:
        r5 = 6;
        goto L12
    L5:
        r3 = 5;
        goto L7
    }

    private static void skipSbrConfig(ParsableBitArray r3) {
        r3.skipBits(3);
        r3.skipBits(8);
        boolean r0 = r3.readBit();
        boolean r1 = r3.readBit();
        if (r0 == false) goto L5;
        r3.skipBits(5);
    L5:
        if (r1 == false) goto L8;
        r3.skipBits(6);
        return;
    }

    private static void skipSpeakerConfig3d(ParsableBitArray r5) {
        int r1 = r5.readBits(2);
        if (r1 != 0) goto L6;
        r5.skipBits(6);
        return;
    L6:
        int r2 = readEscapedIntValue(r5, 5, 8, 16) + 1;
        if (r1 != 1) goto L9;
        r5.skipBits(r2 * 7);
        return;
    L9:
        if (r1 != 2) goto L13;
        skipMpegh3daFlexibleSpeakerConfig(r5, r2);
        return;
    }
}
