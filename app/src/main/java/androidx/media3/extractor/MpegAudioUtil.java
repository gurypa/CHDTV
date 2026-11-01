package androidx.media3.extractor;

import androidx.media3.common.MimeTypes;

/* JADX INFO: loaded from: classes.dex */
public final class MpegAudioUtil {
    private static final int[] BITRATE_V1_L1 = null;
    private static final int[] BITRATE_V1_L2 = null;
    private static final int[] BITRATE_V1_L3 = null;
    private static final int[] BITRATE_V2 = null;
    private static final int[] BITRATE_V2_L1 = null;
    public static final int MAX_FRAME_SIZE_BYTES = 4096;
    public static final int MAX_RATE_BYTES_PER_SECOND = 40000;
    private static final String[] MIME_TYPE_BY_LAYER = null;
    private static final int SAMPLES_PER_FRAME_L1 = 384;
    private static final int SAMPLES_PER_FRAME_L2 = 1152;
    private static final int SAMPLES_PER_FRAME_L3_V1 = 1152;
    private static final int SAMPLES_PER_FRAME_L3_V2 = 576;
    private static final int[] SAMPLING_RATE_V1 = null;

    public static final class Header {
        public int bitrate;
        public int channels;
        public int frameSize;
        public String mimeType;
        public int sampleRate;
        public int samplesPerFrame;
        public int version;

        public Header() {
        }

        public Header(Header r2) {
            this.version = r2.version;
            this.mimeType = r2.mimeType;
            this.frameSize = r2.frameSize;
            this.sampleRate = r2.sampleRate;
            this.channels = r2.channels;
            this.bitrate = r2.bitrate;
            this.samplesPerFrame = r2.samplesPerFrame;
        }

        public boolean setForHeaderData(int r12) {
            if (MpegAudioUtil.access$000(r12) == true) goto L5;
            return false;
        L5:
            int r0 = (r12 >>> 19) & 3;
            if (r0 != 1) goto L8;
            return false;
        L8:
            int r4 = (r12 >>> 17) & 3;
            if (r4 != 0) goto L11;
            return false;
        L11:
            int r5 = (r12 >>> 12) & 15;
            if (r5 == 0) goto L44;
            if (r5 == 15) goto L44;
            int r6 = (r12 >>> 10) & 3;
            if (r6 != 3) goto L18;
            return false;
        L18:
            this.version = r0;
            this.mimeType = MpegAudioUtil.access$100()[3 - r4];
            this.sampleRate = MpegAudioUtil.access$200()[r6];
            int r1 = 2;
            if (r0 != 2) goto L21;
            this.sampleRate /= 2;
        L23:
            int r7 = (r12 >>> 9) & 1;
            this.samplesPerFrame = MpegAudioUtil.access$300(r0, r4);
            if (r4 != 3) goto L29;
            if (r0 != 3) goto L27;
            int r8 = MpegAudioUtil.access$400()[r5 - 1];
        L28:
            this.bitrate = r8;
            this.frameSize = (((this.bitrate * 12) / this.sampleRate) + r7) * 4;
        L40:
            if (((r12 >> 6) & 3) != 3) goto L42;
            r1 = 1;
        L42:
            this.channels = r1;
            return true;
        L27:
            r8 = MpegAudioUtil.access$500()[r5 - 1];
            goto L28
        L29:
            int r9 = 144;
            if (r0 != 3) goto L35;
            if (r4 != 2) goto L33;
            int r10 = MpegAudioUtil.access$600()[r5 - 1];
        L34:
            this.bitrate = r10;
            this.frameSize = ((this.bitrate * 144) / this.sampleRate) + r7;
            goto L40
        L33:
            r10 = MpegAudioUtil.access$700()[r5 - 1];
            goto L34
        L35:
            this.bitrate = MpegAudioUtil.access$800()[r5 - 1];
            if (r4 != 1) goto L38;
            r9 = 72;
        L38:
            this.frameSize = ((r9 * this.bitrate) / this.sampleRate) + r7;
            goto L40
        L21:
            if (r0 != 0) goto L23;
            this.sampleRate /= 4;
        L44:
            return false;
        }
    }

    static {
        MIME_TYPE_BY_LAYER = new String[]{MimeTypes.AUDIO_MPEG_L1, MimeTypes.AUDIO_MPEG_L2, MimeTypes.AUDIO_MPEG};
        SAMPLING_RATE_V1 = new int[]{44100, OpusUtil.SAMPLE_RATE, 32000};
        BITRATE_V1_L1 = new int[]{32000, 64000, 96000, 128000, 160000, DtsUtil.DTS_MAX_RATE_BYTES_PER_SECOND, 224000, AacUtil.AAC_XHE_MAX_RATE_BYTES_PER_SECOND, 288000, 320000, 352000, 384000, 416000, 448000};
        BITRATE_V2_L1 = new int[]{32000, OpusUtil.SAMPLE_RATE, 56000, 64000, Ac3Util.AC3_MAX_RATE_BYTES_PER_SECOND, 96000, 112000, 128000, 144000, 160000, 176000, DtsUtil.DTS_MAX_RATE_BYTES_PER_SECOND, 224000, AacUtil.AAC_XHE_MAX_RATE_BYTES_PER_SECOND};
        BITRATE_V1_L2 = new int[]{32000, OpusUtil.SAMPLE_RATE, 56000, 64000, Ac3Util.AC3_MAX_RATE_BYTES_PER_SECOND, 96000, 112000, 128000, 160000, DtsUtil.DTS_MAX_RATE_BYTES_PER_SECOND, 224000, AacUtil.AAC_XHE_MAX_RATE_BYTES_PER_SECOND, 320000, 384000};
        BITRATE_V1_L3 = new int[]{32000, MAX_RATE_BYTES_PER_SECOND, OpusUtil.SAMPLE_RATE, 56000, 64000, Ac3Util.AC3_MAX_RATE_BYTES_PER_SECOND, 96000, 112000, 128000, 160000, DtsUtil.DTS_MAX_RATE_BYTES_PER_SECOND, 224000, AacUtil.AAC_XHE_MAX_RATE_BYTES_PER_SECOND, 320000};
        BITRATE_V2 = new int[]{8000, AacUtil.AAC_HE_V1_MAX_RATE_BYTES_PER_SECOND, 24000, 32000, MAX_RATE_BYTES_PER_SECOND, OpusUtil.SAMPLE_RATE, 56000, 64000, Ac3Util.AC3_MAX_RATE_BYTES_PER_SECOND, 96000, 112000, 128000, 144000, 160000};
    }

    private MpegAudioUtil() {
    }

    static /* synthetic */ boolean access$000(int r1) {
        return isMagicPresent(r1);
    }

    static /* synthetic */ String[] access$100() {
        return MIME_TYPE_BY_LAYER;
    }

    static /* synthetic */ int[] access$200() {
        return SAMPLING_RATE_V1;
    }

    static /* synthetic */ int access$300(int r1, int r2) {
        return getFrameSizeInSamples(r1, r2);
    }

    static /* synthetic */ int[] access$400() {
        return BITRATE_V1_L1;
    }

    static /* synthetic */ int[] access$500() {
        return BITRATE_V2_L1;
    }

    static /* synthetic */ int[] access$600() {
        return BITRATE_V1_L2;
    }

    static /* synthetic */ int[] access$700() {
        return BITRATE_V1_L3;
    }

    static /* synthetic */ int[] access$800() {
        return BITRATE_V2;
    }

    public static int getFrameSize(int r10) {
        if (isMagicPresent(r10) == true) goto L5;
        return -1;
    L5:
        int r0 = (r10 >>> 19) & 3;
        if (r0 != 1) goto L8;
        return -1;
    L8:
        int r4 = (r10 >>> 17) & 3;
        if (r4 != 0) goto L11;
        return -1;
    L11:
        int r5 = (r10 >>> 12) & 15;
        if (r5 == 0) goto L44;
        if (r5 == 15) goto L44;
        int r6 = (r10 >>> 10) & 3;
        if (r6 != 3) goto L18;
        return -1;
    L18:
        int r1 = SAMPLING_RATE_V1[r6];
        if (r0 != 2) goto L21;
        r1 = r1 / 2;
    L23:
        int r8 = (r10 >>> 9) & 1;
        if (r4 != 3) goto L30;
        if (r0 != 3) goto L27;
        int r2 = BITRATE_V1_L1[r5 - 1];
    L29:
        return (((r2 * 12) / r1) + r8) * 4;
    L27:
        r2 = BITRATE_V2_L1[r5 - 1];
        goto L29
    L30:
        if (r0 != 3) goto L35;
        if (r4 != 2) goto L33;
        int r7 = BITRATE_V1_L2[r5 - 1];
    L36:
        if (r0 == 3) goto L38;
        if (r4 != 1) goto L41;
        int r3 = 72;
    L43:
        return ((r3 * r7) / r1) + r8;
    L41:
        r3 = 144;
        goto L43
    L38:
        return ((r7 * 144) / r1) + r8;
    L33:
        r7 = BITRATE_V1_L3[r5 - 1];
        goto L36
    L35:
        r7 = BITRATE_V2[r5 - 1];
        goto L36
    L21:
        if (r0 != 0) goto L23;
        r1 = r1 / 4;
    L44:
        return -1;
    }

    private static int getFrameSizeInSamples(int r2, int r3) {
        switch(r3) {
            case 1: goto L10;
            case 2: goto L8;
            case 3: goto L6;
            default: goto L5;
        };
    L6:
        return 384;
    L8:
        return 1152;
    L5:
        throw new IllegalArgumentException();
    L10:
        if (r2 == 3) goto L14;
        return SAMPLES_PER_FRAME_L3_V2;
    L14:
        return 1152;
    }

    private static boolean isMagicPresent(int r2) {
        if ((r2 & (-2097152)) != (-2097152)) goto L5;
        return true;
    L5:
        return false;
    }

    public static int parseMpegAudioFrameSampleCount(int r7) {
        if (isMagicPresent(r7) == true) goto L5;
        return -1;
    L5:
        int r0 = (r7 >>> 19) & 3;
        if (r0 != 1) goto L8;
        return -1;
    L8:
        int r3 = (r7 >>> 17) & 3;
        if (r3 != 0) goto L11;
        return -1;
    L11:
        int r4 = (r7 >>> 12) & 15;
        int r6 = (r7 >>> 10) & 3;
        if (r4 == 0) goto L18;
        if (r4 == 15) goto L18;
        if (r6 == 3) goto L18;
        return getFrameSizeInSamples(r0, r3);
    L18:
        return -1;
    }
}
