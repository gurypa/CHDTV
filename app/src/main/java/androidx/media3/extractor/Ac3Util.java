package androidx.media3.extractor;

import androidx.core.location.LocationRequestCompat;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.media3.extractor.p001ts.PsExtractor;
import androidx.media3.extractor.p001ts.TsExtractor;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public final class Ac3Util {
    public static final int AC3_MAX_RATE_BYTES_PER_SECOND = 80000;
    private static final int AC3_SYNCFRAME_AUDIO_SAMPLE_COUNT = 1536;
    private static final int AUDIO_SAMPLES_PER_AUDIO_BLOCK = 256;
    private static final int[] BITRATE_BY_HALF_FRMSIZECOD = null;
    private static final int[] BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD = null;
    private static final int[] CHANNEL_COUNT_BY_ACMOD = null;
    public static final int E_AC3_MAX_RATE_BYTES_PER_SECOND = 768000;
    private static final int[] SAMPLE_RATE_BY_FSCOD = null;
    private static final int[] SAMPLE_RATE_BY_FSCOD2 = null;
    private static final int[] SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1 = null;
    public static final int TRUEHD_MAX_RATE_BYTES_PER_SECOND = 3062500;
    public static final int TRUEHD_RECHUNK_SAMPLE_COUNT = 16;
    public static final int TRUEHD_SYNCFRAME_PREFIX_LENGTH = 10;

    /* JADX INFO: renamed from: androidx.media3.extractor.Ac3Util$1 */
    static /* synthetic */ class C04021 {
    }

    public static final class SyncFrameInfo {
        public static final int STREAM_TYPE_TYPE0 = 0;
        public static final int STREAM_TYPE_TYPE1 = 1;
        public static final int STREAM_TYPE_TYPE2 = 2;
        public static final int STREAM_TYPE_UNDEFINED = -1;
        public final int bitrate;
        public final int channelCount;
        public final int frameSize;
        public final String mimeType;
        public final int sampleCount;
        public final int sampleRate;
        public final int streamType;

        @Target({ElementType.TYPE_USE})
        @Documented
        @Retention(RetentionPolicy.SOURCE)
        public @interface StreamType {
        }

        private SyncFrameInfo(String r1, int r2, int r3, int r4, int r5, int r6, int r7) {
            this.mimeType = r1;
            this.streamType = r2;
            this.channelCount = r3;
            this.sampleRate = r4;
            this.frameSize = r5;
            this.sampleCount = r6;
            this.bitrate = r7;
        }

        /* synthetic */ SyncFrameInfo(String r1, int r2, int r3, int r4, int r5, int r6, int r7, C04021 r8) {
            this(r1, r2, r3, r4, r5, r6, r7);
        }
    }

    static {
        BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD = new int[]{1, 2, 3, 6};
        SAMPLE_RATE_BY_FSCOD = new int[]{OpusUtil.SAMPLE_RATE, 44100, 32000};
        SAMPLE_RATE_BY_FSCOD2 = new int[]{24000, 22050, AacUtil.AAC_HE_V1_MAX_RATE_BYTES_PER_SECOND};
        CHANNEL_COUNT_BY_ACMOD = new int[]{2, 1, 2, 3, 3, 4, 4, 5};
        BITRATE_BY_HALF_FRMSIZECOD = new int[]{32, 40, 48, 56, 64, 80, 96, 112, 128, 160, PsExtractor.AUDIO_STREAM, 224, 256, 320, RendererCapabilities.DECODER_SUPPORT_MASK, 448, 512, 576, 640};
        SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1 = new int[]{69, 87, LocationRequestCompat.QUALITY_LOW_POWER, 121, TsExtractor.TS_STREAM_TYPE_DTS_UHD, 174, 208, 243, 278, 348, 417, 487, 557, 696, 835, 975, 1114, 1253, 1393};
    }

    private Ac3Util() {
    }

    private static int calculateEac3Bitrate(int r2, int r3, int r4) {
        return (r2 * r3) / (r4 * 32);
    }

    public static int findTrueHdSyncframeOffset(ByteBuffer r5) {
        int r0 = r5.position();
        int r1 = r5.limit() - 10;
        int r2 = r0;
    L3:
        if (r2 > r1) goto L9;
        if ((Util.getBigEndianInt(r5, r2 + 4) & (-2)) == (-126718022)) goto L7;
        r2 = r2 + 1;
        goto L3
    L7:
        return r2 - r0;
    L9:
        return -1;
    }

    private static int getAc3SyncframeSize(int r4, int r5) {
        int r0 = r5 / 2;
        if (r4 >= 0) goto L5;
        return -1;
    L5:
        if (r4 >= SAMPLE_RATE_BY_FSCOD.length) goto L22;
        if (r5 >= 0) goto L8;
        return -1;
    L8:
        if (r0 >= SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1.length) goto L24;
        int r1 = SAMPLE_RATE_BY_FSCOD[r4];
        if (r1 == 44100) goto L13;
        int r2 = BITRATE_BY_HALF_FRMSIZECOD[r0];
        if (r1 != 32000) goto L19;
        return r2 * 6;
    L19:
        return r2 * 4;
    L13:
        return (SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1[r0] + (r5 % 2)) * 2;
    L24:
        return -1;
    L22:
        return -1;
    }

    public static Format parseAc3AnnexFFormat(ParsableByteArray r8, String r9, String r10, DrmInitData r11) {
        ParsableBitArray r0 = new ParsableBitArray();
        r0.reset(r8);
        int r1 = r0.readBits(2);
        int r2 = SAMPLE_RATE_BY_FSCOD[r1];
        r0.skipBits(8);
        int r3 = CHANNEL_COUNT_BY_ACMOD[r0.readBits(3)];
        if (r0.readBits(1) == 0) goto L5;
        r3 = r3 + 1;
    L5:
        int r4 = r0.readBits(5);
        int r5 = BITRATE_BY_HALF_FRMSIZECOD[r4] * 1000;
        r0.byteAlign();
        r8.setPosition(r0.getBytePosition());
        return new Format.Builder().setId(r9).setSampleMimeType(MimeTypes.AUDIO_AC3).setChannelCount(r3).setSampleRate(r2).setDrmInitData(r11).setLanguage(r10).setAverageBitrate(r5).setPeakBitrate(r5).build();
    }

    public static int parseAc3SyncframeAudioSampleCount(ByteBuffer r4) {
        int r1 = 3;
        if (((r4.get(r4.position() + 5) & 248) >> 3) <= 10) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == true) goto L8;
        return AC3_SYNCFRAME_AUDIO_SAMPLE_COUNT;
    L8:
        if (((r4.get(r4.position() + 4) & 192) >> 6) == 3) goto L12;
        r1 = (r4.get(r4.position() + 4) & 48) >> 4;
    L12:
        return BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[r1] * 256;
    L5:
        r0 = false;
        goto L6
    }

    public static SyncFrameInfo parseAc3SyncframeInfo(ParsableBitArray r31) {
        int r1 = r31.getPosition();
        r31.skipBits(40);
        if (r31.readBits(5) <= 10) goto L5;
        boolean r3 = true;
    L6:
        r31.setPosition(r1);
        if (r3 == false) goto L164;
        r31.skipBits(16);
        switch(r31.readBits(2)) {
            case 0: goto L13;
            case 1: goto L12;
            case 2: goto L11;
            default: goto L10;
        };
    L10:
        int r6 = -1;
    L14:
        r31.skipBits(3);
        int r12 = (r31.readBits(11) + 1) * 2;
        int r13 = r31.readBits(2);
        if (r13 != 3) goto L17;
        int r14 = 3;
        int r15 = SAMPLE_RATE_BY_FSCOD2[r31.readBits(2)];
        int r8 = 6;
    L18:
        int r10 = r8 * 256;
        int r18 = calculateEac3Bitrate(r12, r15, r8);
        int r11 = r31.readBits(3);
        boolean r20 = r31.readBit();
        int r21 = CHANNEL_COUNT_BY_ACMOD[r11] + (r20 ? 1 : 0);
        r31.skipBits(10);
        if (r31.readBit() == false) goto L21;
        r31.skipBits(8);
    L21:
        if (r11 != 0) goto L25;
        r31.skipBits(5);
        if (r31.readBit() == false) goto L25;
        r31.skipBits(8);
    L25:
        if (r6 != 1) goto L30;
        if (r31.readBit() == false) goto L30;
        r31.skipBits(16);
    L30:
        if (r31.readBit() == false) goto L127;
        if (r11 <= 2) goto L35;
        r31.skipBits(2);
    L35:
        if ((r11 & 1) == 0) goto L38;
        if (r11 <= 2) goto L38;
        int r5 = 6;
        r31.skipBits(6);
    L40:
        if ((r11 & 4) == 0) goto L42;
        r31.skipBits(r5);
    L42:
        if (r20 == true) goto L44;
    L46:
        if (r6 != 0) goto L127;
        if (r31.readBit() == false) goto L50;
        int r7 = 6;
        r31.skipBits(6);
    L51:
        if (r11 != 0) goto L56;
        if (r31.readBit() == false) goto L56;
        r31.skipBits(r7);
    L56:
        if (r31.readBit() == false) goto L58;
        r31.skipBits(r7);
    L58:
        int r9 = r31.readBits(2);
        if (r9 != 1) goto L61;
        r31.skipBits(5);
    L108:
        if (r11 >= 2) goto L117;
        if (r31.readBit() == false) goto L112;
        r31.skipBits(14);
    L112:
        if (r11 != 0) goto L117;
        if (r31.readBit() == false) goto L117;
        r31.skipBits(14);
    L117:
        if (r31.readBit() == false) goto L127;
        if (r14 != 0) goto L120;
        r31.skipBits(5);
        goto L127
    L120:
        int r4 = 0;
    L121:
        if (r4 >= r8) goto L127;
        if (r31.readBit() == false) goto L125;
        r31.skipBits(5);
    L125:
        r4 = r4 + 1;
        goto L121
    L61:
        if (r9 != 2) goto L63;
        r31.skipBits(12);
        goto L108
    L63:
        if (r9 != 3) goto L108;
        int r16 = r31.readBits(5);
        if (r31.readBit() == false) goto L97;
        r31.skipBits(5);
        if (r31.readBit() == false) goto L69;
        int r17 = 4;
        r31.skipBits(4);
    L71:
        if (r31.readBit() == false) goto L74;
        r31.skipBits(r17);
    L74:
        if (r31.readBit() == false) goto L77;
        r31.skipBits(r17);
    L77:
        if (r31.readBit() == false) goto L80;
        r31.skipBits(r17);
    L80:
        if (r31.readBit() == false) goto L83;
        r31.skipBits(r17);
    L83:
        if (r31.readBit() == false) goto L86;
        r31.skipBits(r17);
    L86:
        if (r31.readBit() == false) goto L89;
        r31.skipBits(r17);
    L89:
        if (r31.readBit() == false) goto L97;
        if (r31.readBit() == false) goto L94;
        r31.skipBits(r17);
    L94:
        if (r31.readBit() == false) goto L97;
        r31.skipBits(r17);
        goto L97
    L69:
        r17 = 4;
    L97:
        if (r31.readBit() == false) goto L105;
        r31.skipBits(5);
        if (r31.readBit() == false) goto L104;
        r31.skipBits(7);
        if (r31.readBit() == false) goto L103;
        char r19 = '\b';
        r31.skipBits(8);
    L106:
        r31.skipBits((r16 + 2) * 8);
        r31.byteAlign();
        goto L108
    L103:
        r19 = '\b';
        goto L106
    L104:
        r19 = '\b';
        goto L106
    L105:
        r19 = '\b';
        goto L106
    L50:
        r7 = 6;
        goto L51
    L44:
        if (r31.readBit() == false) goto L46;
        r31.skipBits(5);
    L38:
        r5 = 6;
    L127:
        if (r31.readBit() == false) goto L144;
        r31.skipBits(5);
        if (r11 != 2) goto L132;
        r31.skipBits(4);
    L132:
        if (r11 < 6) goto L135;
        r31.skipBits(2);
    L135:
        if (r31.readBit() == false) goto L137;
        int r22 = 8;
        r31.skipBits(8);
    L138:
        if (r11 == 0) goto L140;
    L142:
        if (r13 >= 3) goto L144;
        r31.skipBit();
        goto L144
    L140:
        if (r31.readBit() == false) goto L142;
        r31.skipBits(r22);
        goto L142
    L137:
        r22 = 8;
    L144:
        if (r6 != 0) goto L148;
        if (r14 == 3) goto L148;
        r31.skipBit();
    L148:
        if (r6 != 2) goto L155;
        if (r14 != 3) goto L151;
    L154:
        int r23 = 6;
        r31.skipBits(6);
    L156:
        String r2 = MimeTypes.AUDIO_E_AC3;
        if (r31.readBit() == true) goto L159;
    L163:
        int r29 = r18;
        String r24 = r2;
        int r25 = r6;
        int r28 = r10;
        int r27 = r12;
        int r26 = r15;
        int r210 = r21;
    L184:
        return new SyncFrameInfo(r24, r25, r210, r26, r27, r28, r29, null);
    L159:
        if (r31.readBits(r23) != 1) goto L163;
        if (r31.readBits(8) != 1) goto L163;
        r2 = MimeTypes.AUDIO_E_AC3_JOC;
        goto L163
    L151:
        if (r31.readBit() == true) goto L154;
        r23 = 6;
        goto L156
    L155:
        r23 = 6;
        goto L156
    L17:
        r14 = r31.readBits(2);
        int r110 = BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[r14];
        r15 = SAMPLE_RATE_BY_FSCOD[r13];
        r8 = r110;
        goto L18
    L11:
        r6 = 2;
        goto L14
    L12:
        r6 = 1;
        goto L14
    L13:
        r6 = 0;
        goto L14
    L164:
        String r30 = MimeTypes.AUDIO_AC3;
        r31.skipBits(32);
        int r32 = r31.readBits(2);
        if (r32 != 3) goto L167;
        r30 = null;
    L167:
        int r33 = r31.readBits(6);
        int r34 = BITRATE_BY_HALF_FRMSIZECOD[r33 / 2] * 1000;
        int r111 = getAc3SyncframeSize(r32, r33);
        r31.skipBits(8);
        int r112 = r31.readBits(3);
        if ((r112 & 1) != 0) goto L170;
    L172:
        int r35 = 2;
    L174:
        if ((r112 & 4) == 0) goto L176;
        r31.skipBits(r35);
    L176:
        if (r112 != r35) goto L179;
        r31.skipBits(r35);
    L179:
        if (r32 >= SAMPLE_RATE_BY_FSCOD.length) goto L181;
        int r36 = SAMPLE_RATE_BY_FSCOD[r32];
    L182:
        int r211 = CHANNEL_COUNT_BY_ACMOD[r112] + (r31.readBit() ? 1 : 0);
        r29 = r34;
        r24 = r30;
        r25 = -1;
        r28 = AC3_SYNCFRAME_AUDIO_SAMPLE_COUNT;
        r27 = r111;
        r26 = r36;
        r210 = r211;
        goto L184
    L181:
        r36 = -1;
        goto L182
    L170:
        if (r112 == 1) goto L172;
        r35 = 2;
        r31.skipBits(2);
        goto L174
    L5:
        r3 = false;
        goto L6
    }

    public static int parseAc3SyncframeSize(byte[] r4) {
        if (r4.length >= 6) goto L7;
        return -1;
    L7:
        if (((r4[5] & 248) >> 3) <= 10) goto L9;
        boolean r0 = true;
    L10:
        if (r0 == false) goto L14;
        int r3 = (r4[2] & 7) << 8;
        return (((r4[3] & 255) | r3) + 1) * 2;
    L14:
        return getAc3SyncframeSize((r4[4] & 192) >> 6, r4[4] & 63);
    L9:
        r0 = false;
        goto L10
    }

    public static Format parseEAc3AnnexFFormat(ParsableByteArray r10, String r11, String r12, DrmInitData r13) {
        ParsableBitArray r0 = new ParsableBitArray();
        r0.reset(r10);
        int r1 = r0.readBits(13) * 1000;
        r0.skipBits(3);
        int r3 = r0.readBits(2);
        int r4 = SAMPLE_RATE_BY_FSCOD[r3];
        r0.skipBits(10);
        int r5 = CHANNEL_COUNT_BY_ACMOD[r0.readBits(3)];
        if (r0.readBits(1) == 0) goto L5;
        r5 = r5 + 1;
    L5:
        r0.skipBits(3);
        int r2 = r0.readBits(4);
        r0.skipBits(1);
        if (r2 <= 0) goto L11;
        r0.skipBits(6);
        if (r0.readBits(1) == 0) goto L10;
        r5 = r5 + 2;
    L10:
        r0.skipBits(1);
    L11:
        String r7 = MimeTypes.AUDIO_E_AC3;
        if (r0.bitsLeft() <= 7) goto L16;
        r0.skipBits(7);
        if (r0.readBits(1) == 0) goto L16;
        r7 = MimeTypes.AUDIO_E_AC3_JOC;
    L16:
        r0.byteAlign();
        r10.setPosition(r0.getBytePosition());
        return new Format.Builder().setId(r11).setSampleMimeType(r7).setChannelCount(r5).setSampleRate(r4).setDrmInitData(r13).setLanguage(r12).setPeakBitrate(r1).build();
    }

    public static int parseTrueHdSyncframeAudioSampleCount(ByteBuffer r3, int r4) {
        if ((r3.get((r3.position() + r4) + 7) & 255) != 187) goto L5;
        boolean r0 = true;
    L6:
        int r1 = r3.position() + r4;
        if (r0 == false) goto L9;
        int r2 = 9;
    L11:
        return 40 << ((r3.get(r1 + r2) >> 4) & 7);
    L9:
        r2 = 8;
        goto L11
    L5:
        r0 = false;
        goto L6
    }

    public static int parseTrueHdSyncframeAudioSampleCount(byte[] r5) {
        boolean r3 = false;
        if (r5[4] == (-8)) goto L5;
    L19:
        return 0;
    L5:
        if (r5[5] != 114) goto L19;
        if (r5[6] != 111) goto L19;
        if ((r5[7] & 254) != 186) goto L19;
        if ((r5[7] & 255) != 187) goto L14;
        r3 = true;
    L14:
        if (r3 == false) goto L16;
        char r2 = '\t';
    L18:
        return 40 << ((r5[r2] >> 4) & 7);
    L16:
        r2 = '\b';
        goto L18
    }
}
