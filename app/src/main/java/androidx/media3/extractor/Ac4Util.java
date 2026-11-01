package androidx.media3.extractor;

import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import com.google.common.primitives.SignedBytes;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public final class Ac4Util {
    public static final int AC40_SYNCWORD = 44096;
    public static final int AC41_SYNCWORD = 44097;
    private static final int CHANNEL_COUNT_2 = 2;
    private static final int CHANNEL_MODE_22_2 = 15;
    private static final int CHANNEL_MODE_3_0 = 2;
    private static final int CHANNEL_MODE_5_0 = 3;
    private static final int CHANNEL_MODE_5_1 = 4;
    private static final int CHANNEL_MODE_7_0_322 = 9;
    private static final int CHANNEL_MODE_7_0_34 = 5;
    private static final int CHANNEL_MODE_7_0_4 = 11;
    private static final int CHANNEL_MODE_7_0_52 = 7;
    private static final int CHANNEL_MODE_7_1_322 = 10;
    private static final int CHANNEL_MODE_7_1_34 = 6;
    private static final int CHANNEL_MODE_7_1_4 = 12;
    private static final int CHANNEL_MODE_7_1_52 = 8;
    private static final int CHANNEL_MODE_9_0_4 = 13;
    private static final int CHANNEL_MODE_9_1_4 = 14;
    private static final int CHANNEL_MODE_MONO = 0;
    private static final int CHANNEL_MODE_STEREO = 1;
    private static final int CHANNEL_MODE_UNKNOWN = -1;
    public static final int HEADER_SIZE_FOR_PARSER = 16;
    public static final int MAX_RATE_BYTES_PER_SECOND = 336000;
    private static final int[] SAMPLE_COUNT = null;
    public static final int SAMPLE_HEADER_SIZE = 7;

    /* JADX INFO: renamed from: androidx.media3.extractor.Ac4Util$1 */
    static /* synthetic */ class C04031 {
    }

    private static final class Ac4Presentation {
        public int channelMode;
        public boolean hasBackChannels;
        public boolean isChannelCoded;
        public int level;
        public int numOfUmxObjects;
        public int topChannelPairs;

        private Ac4Presentation() {
            this.isChannelCoded = true;
            this.channelMode = -1;
            this.numOfUmxObjects = -1;
            this.hasBackChannels = true;
            this.topChannelPairs = 2;
            this.level = 0;
        }

        /* synthetic */ Ac4Presentation(C04031 r1) {
            this();
        }
    }

    public static final class SyncFrameInfo {
        public final int bitstreamVersion;
        public final int channelCount;
        public final int frameSize;
        public final int sampleCount;
        public final int sampleRate;

        private SyncFrameInfo(int r1, int r2, int r3, int r4, int r5) {
            this.bitstreamVersion = r1;
            this.channelCount = r2;
            this.sampleRate = r3;
            this.frameSize = r4;
            this.sampleCount = r5;
        }

        /* synthetic */ SyncFrameInfo(int r1, int r2, int r3, int r4, int r5, C04031 r6) {
            this(r1, r2, r3, r4, r5);
        }
    }

    static {
        SAMPLE_COUNT = new int[]{PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT, 2000, 1920, 1601, 1600, 1001, 1000, 960, 800, 800, 480, 400, 400, 2048};
    }

    private Ac4Util() {
    }

    public static void getAc4SampleHeader(int r3, ParsableByteArray r4) {
        r4.reset(7);
        byte[] r0 = r4.getData();
        r0[0] = -84;
        r0[1] = SignedBytes.MAX_POWER_OF_TWO;
        r0[2] = -1;
        r0[3] = -1;
        r0[4] = (byte) ((r3 >> 16) & 255);
        r0[5] = (byte) ((r3 >> 8) & 255);
        r0[6] = (byte) (r3 & 255);
    }

    private static int getAdjustedChannelCount(int r2, boolean r3, int r4) {
        int r0 = getChannelCountFromChannelMode(r2);
        if (r2 != 11) goto L5;
    L10:
        if (r3 == true) goto L12;
        r0 = r0 - 2;
    L12:
        switch(r4) {
            case 0: goto L16;
            case 1: goto L18;
            default: goto L19;
        };
    L19:
        return r0;
    L18:
        return r0 - 2;
    L16:
        return r0 - 4;
    L5:
        if (r2 == 12) goto L10;
        if (r2 == 13) goto L10;
        if (r2 == 14) goto L10;
        return r0;
    }

    private static int getChannelCountFromChannelMode(int r1) {
        switch(r1) {
            case 0: goto L27;
            case 1: goto L25;
            case 2: goto L23;
            case 3: goto L21;
            case 4: goto L19;
            case 5: goto L17;
            case 6: goto L15;
            case 7: goto L17;
            case 8: goto L15;
            case 9: goto L17;
            case 10: goto L15;
            case 11: goto L13;
            case 12: goto L11;
            case 13: goto L9;
            case 14: goto L7;
            case 15: goto L5;
            default: goto L3;
        };
    L3:
        return -1;
    L5:
        return 24;
    L7:
        return 14;
    L9:
        return 13;
    L11:
        return 12;
    L13:
        return 11;
    L15:
        return 8;
    L17:
        return 7;
    L19:
        return 6;
    L21:
        return 5;
    L23:
        return 3;
    L25:
        return 2;
    L27:
        return 1;
    }

    public static Format parseAc4AnnexEFormat(ParsableByteArray r21, String r22, String r23, DrmInitData r24) throws ParserException {
        ParsableBitArray r0 = new ParsableBitArray();
        r0.reset(r21);
        int r2 = r0.bitsLeft();
        int r4 = r0.readBits(3);
        if (r4 > 1) goto L178;
        int r7 = r0.readBits(7);
        if (r0.readBit() == false) goto L7;
        int r8 = OpusUtil.SAMPLE_RATE;
    L8:
        r0.skipBits(4);
        int r10 = r0.readBits(9);
        if (r7 <= 1) goto L18;
        if (r4 == 0) goto L17;
        if (r0.readBit() == false) goto L18;
        r0.skipBits(16);
        if (r0.readBit() == false) goto L18;
        r0.skipBits(128);
        goto L18
    L17:
        throw ParserException.createForUnsupportedContainerFeature("Invalid AC-4 DSI version: " + r4);
    L18:
        if (r4 == 1) goto L20;
    L24:
        Ac4Presentation r12 = new Ac4Presentation(null);
        int r13 = 0;
    L25:
        if (r13 >= r10) goto L162;
        boolean r14 = false;
        boolean r15 = false;
        int r17 = 0;
        if (r4 == 0) goto L28;
        int r3 = r0.readBits(8);
        int r9 = r0.readBits(8);
        if (r9 != 255) goto L32;
        r9 = r9 + r0.readBits(16);
    L32:
        if (r3 <= 2) goto L34;
        r0.skipBits(r9 * 8);
        r13 = r13 + 1;
        goto L25
    L34:
        r17 = (r2 - r0.bitsLeft()) / 8;
        int r11 = r0.readBits(5);
        if (r11 != 31) goto L37;
        boolean r6 = true;
    L38:
        r15 = r6;
    L39:
        if (r14 == true) goto L44;
        if (r15 == true) goto L44;
        if (r11 != 6) goto L44;
        boolean r5 = true;
    L128:
        if (r5 == false) goto L133;
        int r1 = r0.readBits(7);
        int r16 = 0;
    L130:
        if (r16 >= r1) goto L133;
        r0.skipBits(15);
        r16 = r16 + 1;
        r1 = r1;
    L133:
        if (r3 > 0) goto L135;
    L147:
        r0.byteAlign();
        if (r4 != 1) goto L155;
        int r18 = ((r2 - r0.bitsLeft()) / 8) - r17;
        if (r9 < r18) goto L153;
        r0.skipBytes(r9 - r18);
        goto L155
    L153:
        throw ParserException.createForUnsupportedContainerFeature("pres_bytes is smaller than presentation bytes read.");
    L155:
        if (r12.isChannelCoded == false) goto L162;
        if (r12.channelMode != (-1)) goto L162;
        throw ParserException.createForUnsupportedContainerFeature("Can't determine channel mode of presentation " + r13);
    L135:
        if (r0.readBit() == false) goto L142;
        if (skipDsiBitrate(r0) == true) goto L142;
        throw ParserException.createForUnsupportedContainerFeature("Can't parse bitrate DSI.");
    L142:
        if (r0.readBit() == false) goto L147;
        r0.byteAlign();
        r0.skipBytes(r0.readBits(16));
        int r19 = r0.readBits(5);
        int r20 = 0;
    L144:
        if (r20 >= r19) goto L147;
        r0.skipBits(3);
        r0.skipBits(8);
        r20 = r20 + 1;
    L44:
        r12.level = r0.readBits(3);
        if (r0.readBit() == false) goto L47;
        r0.skipBits(5);
    L47:
        r0.skipBits(2);
        if (r4 != 1) goto L52;
        if (r3 == 1) goto L51;
        if (r3 != 2) goto L52;
    L51:
        r0.skipBits(2);
    L52:
        r0.skipBits(5);
        r0.skipBits(10);
        if (r4 != 1) goto L87;
        if (r3 <= 0) goto L57;
        r12.isChannelCoded = r0.readBit();
    L57:
        if (r12.isChannelCoded == false) goto L74;
        if (r3 != 1) goto L61;
    L62:
        int r25 = r0.readBits(5);
        if (r25 < 0) goto L68;
        if (r25 > 15) goto L68;
        r12.channelMode = r25;
    L68:
        if (r25 >= 11) goto L70;
    L72:
        r0.skipBits(24);
        goto L74
    L70:
        if (r25 > 14) goto L72;
        r12.hasBackChannels = r0.readBit();
        r12.topChannelPairs = r0.readBits(2);
        goto L72
    L61:
        if (r3 != 2) goto L72;
    L74:
        if (r3 == 1) goto L78;
        if (r3 != 2) goto L87;
    L78:
        if (r0.readBit() == false) goto L83;
        if (r0.readBit() == false) goto L83;
        r0.skipBits(2);
    L83:
        if (r0.readBit() == false) goto L87;
        r0.skipBit();
        int r26 = 8;
        int r27 = r0.readBits(8);
        int r28 = 0;
    L85:
        if (r28 >= r27) goto L87;
        r0.skipBits(r26);
        r28 = r28 + 1;
        r26 = 8;
    L87:
        if (r14 == true) goto L124;
        if (r15 == true) goto L124;
        r0.skipBit();
        switch(r11) {
            case 0: goto L111;
            case 1: goto L111;
            case 2: goto L111;
            case 3: goto L100;
            case 4: goto L100;
            case 5: goto L93;
            default: goto L92;
        };
    L92:
        int r29 = r0.readBits(7);
        int r30 = 0;
    L122:
        if (r30 >= r29) goto L127;
        r0.skipBits(8);
        r30 = r30 + 1;
    L127:
        r0.skipBit();
        r5 = r0.readBit();
        goto L128
    L93:
        if (r3 != 0) goto L95;
        parseDsiSubstream(r0, r12);
        goto L127
    L95:
        int r31 = r0.readBits(3);
        int r32 = 0;
    L97:
        if (r32 >= (r31 + 2)) goto L127;
        parseDsiSubstreamGroup(r0, r12);
        r32 = r32 + 1;
        goto L97
    L100:
        if (r3 != 0) goto L106;
        int r33 = 0;
    L103:
        if (r33 >= 3) goto L127;
        parseDsiSubstream(r0, r12);
        r33 = r33 + 1;
        goto L103
    L106:
        int r34 = 0;
    L108:
        if (r34 >= 3) goto L127;
        parseDsiSubstreamGroup(r0, r12);
        r34 = r34 + 1;
        goto L108
    L111:
        if (r3 != 0) goto L117;
        int r35 = 0;
    L114:
        if (r35 >= 2) goto L127;
        parseDsiSubstream(r0, r12);
        r35 = r35 + 1;
        goto L114
    L117:
        int r36 = 0;
    L119:
        if (r36 >= 2) goto L127;
        parseDsiSubstreamGroup(r0, r12);
        r36 = r36 + 1;
    L124:
        if (r3 != 0) goto L126;
        parseDsiSubstream(r0, r12);
        goto L127
    L126:
        parseDsiSubstreamGroup(r0, r12);
        goto L127
    L37:
        r6 = false;
        goto L38
    L28:
        r14 = r0.readBit();
        r9 = 0;
        r11 = r0.readBits(5);
        r3 = r0.readBits(5);
    L162:
        if (r12.isChannelCoded == false) goto L164;
        int r37 = getAdjustedChannelCount(r12.channelMode, r12.hasBackChannels, r12.topChannelPairs);
    L172:
        if (r37 <= 0) goto L176;
        return new Format.Builder().setId(r22).setSampleMimeType(MimeTypes.AUDIO_AC4).setChannelCount(r37).setSampleRate(r8).setDrmInitData(r24).setLanguage(r23).build();
    L176:
        throw ParserException.createForUnsupportedContainerFeature("Can't determine channel count of presentation.");
    L164:
        int r38 = r12.numOfUmxObjects + 1;
        if (r12.level == 4) goto L167;
        r37 = r38;
        goto L172
    L167:
        if (r38 != 17) goto L169;
        int r39 = 21;
    L170:
        r37 = r39;
        goto L172
    L169:
        r39 = r38;
        goto L170
    L20:
        if (skipDsiBitrate(r0) == false) goto L23;
        r0.byteAlign();
        goto L24
    L23:
        throw ParserException.createForUnsupportedContainerFeature("Invalid AC-4 DSI bitrate.");
    L7:
        r8 = 44100;
        goto L8
    L178:
        throw ParserException.createForUnsupportedContainerFeature("Unsupported AC-4 DSI version: " + r4);
    }

    public static int parseAc4SyncframeAudioSampleCount(ByteBuffer r3) {
        byte[] r0 = new byte[16];
        int r1 = r3.position();
        r3.get(r0);
        r3.position(r1);
        return parseAc4SyncframeInfo(new ParsableBitArray(r0)).sampleCount;
    }

    public static SyncFrameInfo parseAc4SyncframeInfo(ParsableBitArray r12) {
        int r2 = r12.readBits(16);
        int r1 = r12.readBits(16);
        int r0 = (0 + 2) + 2;
        if (r1 != 65535) goto L5;
        r1 = r12.readBits(24);
        r0 = r0 + 3;
    L5:
        int r3 = r1 + r0;
        if (r2 != 44097) goto L8;
        int r8 = r3 + 2;
    L9:
        int r4 = r12.readBits(2);
        if (r4 != 3) goto L12;
        int r5 = r4 + readVariableBits(r12, 2);
    L13:
        int r6 = r12.readBits(10);
        if (r12.readBit() == false) goto L19;
        if (r12.readBits(3) <= 0) goto L19;
        r12.skipBits(2);
    L19:
        if (r12.readBit() == false) goto L21;
        int r7 = 48000;
    L22:
        int r11 = r12.readBits(4);
        int r9 = 0;
        if (r7 == 44100) goto L25;
    L27:
        if (r7 == 48000) goto L29;
    L44:
        return new SyncFrameInfo(r5, 2, r7, r8, r9, null);
    L29:
        if (r11 >= SAMPLE_COUNT.length) goto L44;
        r9 = SAMPLE_COUNT[r11];
        switch((r6 % 5)) {
            case 1: goto L40;
            case 2: goto L37;
            case 3: goto L40;
            case 4: goto L33;
            default: goto L44;
        };
    L33:
        if (r11 == 3) goto L36;
        if (r11 == 8) goto L36;
        if (r11 != 11) goto L44;
    L36:
        r9 = r9 + 1;
        goto L44
    L37:
        if (r11 == 8) goto L39;
        if (r11 != 11) goto L44;
    L39:
        r9 = r9 + 1;
        goto L44
    L40:
        if (r11 == 3) goto L42;
        if (r11 != 8) goto L44;
    L42:
        r9 = r9 + 1;
        goto L44
    L25:
        if (r11 != 13) goto L27;
        r9 = SAMPLE_COUNT[r11];
        goto L44
    L21:
        r7 = 44100;
        goto L22
    L12:
        r5 = r4;
        goto L13
    L8:
        r8 = r3;
        goto L9
    }

    public static int parseAc4SyncframeSize(byte[] r4, int r5) {
        if (r4.length >= 7) goto L6;
        return -1;
    L6:
        int r2 = ((r4[2] & 255) << 8) | (r4[3] & 255);
        int r0 = 2 + 2;
        if (r2 != 65535) goto L10;
        r2 = (((r4[4] & 255) << 16) | ((r4[5] & 255) << 8)) | (r4[6] & 255);
        r0 = r0 + 3;
    L10:
        if (r5 != 44097) goto L13;
        r0 = r0 + 2;
    L13:
        return r2 + r0;
    }

    private static void parseDsiSubstream(ParsableBitArray r4, Ac4Presentation r5) throws ParserException {
        int r1 = r4.readBits(5);
        r4.skipBits(2);
        if (r4.readBit() == false) goto L6;
        r4.skipBits(5);
    L6:
        if (r1 < 7) goto L11;
        if (r1 > 10) goto L11;
        r4.skipBit();
    L11:
        if (r4.readBit() == false) goto L25;
        int r0 = r4.readBits(3);
        if (r5.channelMode != (-1)) goto L22;
        if (r1 < 0) goto L22;
        if (r1 > 15) goto L22;
        if (r0 != 0) goto L19;
    L20:
        r5.channelMode = r1;
        goto L22
    L19:
        if (r0 == 1) goto L20;
    L22:
        if (r4.readBit() == false) goto L26;
        skipDsiLanguage(r4);
        return;
    L26:
        return;
    }

    private static void parseDsiSubstreamGroup(ParsableBitArray r6, Ac4Presentation r7) throws ParserException {
        r6.skipBits(2);
        boolean r1 = r6.readBit();
        int r2 = r6.readBits(8);
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L18;
        r6.skipBits(2);
        if (r6.readBit() == false) goto L7;
        r6.skipBits(5);
    L7:
        if (r1 == false) goto L10;
        r6.skipBits(24);
    L16:
        r3 = r3 + 1;
        goto L3
    L10:
        if (r6.readBit() == true) goto L12;
    L15:
        r6.skipBits(4);
        goto L16
    L12:
        if (r6.readBit() == true) goto L14;
        r6.skipBits(4);
    L14:
        r7.numOfUmxObjects = r6.readBits(6) + 1;
        goto L15
    L18:
        if (r6.readBit() == false) goto L26;
        r6.skipBits(3);
        if (r6.readBit() == false) goto L27;
        skipDsiLanguage(r6);
        return;
    L27:
        return;
    }

    private static int readVariableBits(ParsableBitArray r2, int r3) {
        int r0 = 0;
    L3:
        int r1 = r0 + r2.readBits(r3);
        if (r2.readBit() == false) goto L5;
        r0 = (r1 + 1) << r3;
        goto L3
    L5:
        return r1;
    }

    private static boolean skipDsiBitrate(ParsableBitArray r2) {
        if (r2.bitsLeft() >= 66) goto L6;
        return false;
    L6:
        r2.skipBits(66);
        return true;
    }

    private static void skipDsiLanguage(ParsableBitArray r4) throws ParserException {
        int r0 = r4.readBits(6);
        if (r0 < 2) goto L9;
        if (r0 > 42) goto L9;
        r4.skipBits(r0 * 8);
        return;
    L9:
        throw ParserException.createForUnsupportedContainerFeature(String.format("Invalid language tag bytes number: %d. Must be between 2 and 42.", new Object[]{Integer.valueOf(r0)}));
    }
}
