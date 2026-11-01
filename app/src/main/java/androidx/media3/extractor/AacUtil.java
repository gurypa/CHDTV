package androidx.media3.extractor;

import androidx.media3.common.ParserException;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableBitArray;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public final class AacUtil {
    public static final int AAC_ELD_MAX_RATE_BYTES_PER_SECOND = 8000;
    public static final int AAC_HE_AUDIO_SAMPLE_COUNT = 2048;
    public static final int AAC_HE_V1_MAX_RATE_BYTES_PER_SECOND = 16000;
    public static final int AAC_HE_V2_MAX_RATE_BYTES_PER_SECOND = 7000;
    public static final int AAC_LC_AUDIO_SAMPLE_COUNT = 1024;
    public static final int AAC_LC_MAX_RATE_BYTES_PER_SECOND = 100000;
    public static final int AAC_LD_AUDIO_SAMPLE_COUNT = 512;
    public static final int AAC_XHE_AUDIO_SAMPLE_COUNT = 1024;
    public static final int AAC_XHE_MAX_RATE_BYTES_PER_SECOND = 256000;
    public static final int AUDIO_OBJECT_TYPE_AAC_ELD = 23;
    public static final int AUDIO_OBJECT_TYPE_AAC_ER_BSAC = 22;
    public static final int AUDIO_OBJECT_TYPE_AAC_LC = 2;
    public static final int AUDIO_OBJECT_TYPE_AAC_PS = 29;
    public static final int AUDIO_OBJECT_TYPE_AAC_SBR = 5;
    public static final int AUDIO_OBJECT_TYPE_AAC_XHE = 42;
    private static final int AUDIO_OBJECT_TYPE_ESCAPE = 31;
    private static final int AUDIO_SPECIFIC_CONFIG_CHANNEL_CONFIGURATION_INVALID = -1;
    private static final int[] AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE = null;
    private static final int AUDIO_SPECIFIC_CONFIG_FREQUENCY_INDEX_ARBITRARY = 15;
    private static final int[] AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE = null;
    private static final String CODECS_STRING_PREFIX = "mp4a.40.";
    private static final String TAG = "AacUtil";

    /* JADX INFO: renamed from: androidx.media3.extractor.AacUtil$1 */
    static /* synthetic */ class C04011 {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface AacAudioObjectType {
    }

    public static final class Config {
        public final int channelCount;
        public final String codecs;
        public final int sampleRateHz;

        private Config(int r1, int r2, String r3) {
            this.sampleRateHz = r1;
            this.channelCount = r2;
            this.codecs = r3;
        }

        /* synthetic */ Config(int r1, int r2, String r3, C04011 r4) {
            this(r1, r2, r3);
        }
    }

    static {
        AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE = new int[]{96000, 88200, 64000, OpusUtil.SAMPLE_RATE, 44100, 32000, 24000, 22050, AAC_HE_V1_MAX_RATE_BYTES_PER_SECOND, 12000, 11025, 8000, 7350};
        AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE = new int[]{0, 1, 2, 3, 4, 5, 6, 8, -1, -1, -1, 7, 8, -1, 8, -1};
    }

    private AacUtil() {
    }

    public static byte[] buildAacLcAudioSpecificConfig(int r5, int r6) {
        int r0 = -1;
        int r1 = 0;
    L4:
        if (r1 >= AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE.length) goto L9;
        if (r5 != AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE[r1]) goto L8;
        r0 = r1;
    L8:
        r1 = r1 + 1;
        goto L4
    L9:
        int r2 = -1;
        int r3 = 0;
    L11:
        if (r3 >= AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE.length) goto L17;
        if (r6 != AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE[r3]) goto L15;
        r2 = r3;
    L15:
        r3 = r3 + 1;
        goto L11
    L17:
        if (r5 == (-1)) goto L22;
        if (r2 == (-1)) goto L22;
        return buildAudioSpecificConfig(2, r0, r2);
    L22:
        throw new IllegalArgumentException("Invalid sample rate or number of channels: " + r5 + ", " + r6);
    }

    public static byte[] buildAudioSpecificConfig(int r3, int r4, int r5) {
        return new byte[]{(byte) (((r3 << 3) & 248) | ((r4 >> 1) & 7)), (byte) (((r4 << 7) & 128) | ((r5 << 3) & 120))};
    }

    private static int getAudioObjectType(ParsableBitArray r2) {
        int r0 = r2.readBits(5);
        if (r0 == 31) goto L5;
        return r0;
    L5:
        return r2.readBits(6) + 32;
    }

    private static int getSamplingFrequency(ParsableBitArray r4) throws ParserException {
        int r0 = r4.readBits(4);
        if (r0 != 15) goto L10;
        if (r4.bitsLeft() < 24) goto L8;
        return r4.readBits(24);
    L8:
        throw ParserException.createForMalformedContainer("AAC header insufficient data", null);
    L10:
        if (r0 >= 13) goto L14;
        return AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE[r0];
    L14:
        throw ParserException.createForMalformedContainer("AAC header wrong Sampling Frequency Index", null);
    }

    public static Config parseAudioSpecificConfig(ParsableBitArray r7, boolean r8) throws ParserException {
        int r0 = getAudioObjectType(r7);
        int r1 = getSamplingFrequency(r7);
        int r3 = r7.readBits(4);
        String r4 = CODECS_STRING_PREFIX + r0;
        if (r0 != 5) goto L5;
    L6:
        r1 = getSamplingFrequency(r7);
        r0 = getAudioObjectType(r7);
        if (r0 != 22) goto L9;
        r3 = r7.readBits(4);
    L9:
        if (r8 == false) goto L23;
        switch(r0) {
            case 1: goto L13;
            case 2: goto L13;
            case 3: goto L13;
            case 4: goto L13;
            case 5: goto L12;
            case 6: goto L13;
            case 7: goto L13;
            case 8: goto L12;
            case 9: goto L12;
            case 10: goto L12;
            case 11: goto L12;
            case 12: goto L12;
            case 13: goto L12;
            case 14: goto L12;
            case 15: goto L12;
            case 16: goto L12;
            case 17: goto L13;
            case 18: goto L12;
            case 19: goto L13;
            case 20: goto L13;
            case 21: goto L13;
            case 22: goto L13;
            case 23: goto L13;
            default: goto L12;
        };
    L13:
        parseGaSpecificConfig(r7, r0, r3);
        switch(r0) {
            case 17: goto L16;
            case 18: goto L23;
            case 19: goto L16;
            case 20: goto L16;
            case 21: goto L16;
            case 22: goto L16;
            case 23: goto L16;
            default: goto L23;
        };
    L16:
        int r5 = r7.readBits(2);
        if (r5 == 2) goto L22;
        if (r5 != 3) goto L23;
    L22:
        throw ParserException.createForUnsupportedContainerFeature("Unsupported epConfig: " + r5);
    L12:
        throw ParserException.createForUnsupportedContainerFeature("Unsupported audio object type: " + r0);
    L23:
        int r2 = AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE[r3];
        C04011 r6 = null;
        if (r2 == (-1)) goto L28;
        return new Config(r1, r2, r4, r6);
    L28:
        throw ParserException.createForMalformedContainer(null, null);
    L5:
        if (r0 != 29) goto L9;
        goto L6
    }

    public static Config parseAudioSpecificConfig(byte[] r2) throws ParserException {
        return parseAudioSpecificConfig(new ParsableBitArray(r2), false);
    }

    private static void parseGaSpecificConfig(ParsableBitArray r6, int r7, int r8) {
        if (r6.readBit() == false) goto L6;
        Log.m48w(TAG, "Unexpected frameLengthFlag = 1");
    L6:
        if (r6.readBit() == false) goto L8;
        r6.skipBits(14);
    L8:
        boolean r2 = r6.readBit();
        if (r8 == 0) goto L29;
        if (r7 == 6) goto L13;
        if (r7 == 20) goto L13;
    L14:
        if (r2 == true) goto L16;
        return;
    L16:
        if (r7 != 22) goto L19;
        r6.skipBits(16);
    L19:
        if (r7 != 17) goto L21;
    L25:
        r6.skipBits(3);
    L26:
        r6.skipBits(1);
        return;
    L21:
        if (r7 == 19) goto L25;
        if (r7 == 20) goto L25;
        if (r7 != 23) goto L26;
    L13:
        r6.skipBits(3);
        goto L14
    L29:
        throw new UnsupportedOperationException();
    }
}
