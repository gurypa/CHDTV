package androidx.media3.extractor;

import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class WavUtil {
    public static final int DATA_FOURCC = 1684108385;
    public static final int DS64_FOURCC = 1685272116;
    public static final int FMT_FOURCC = 1718449184;
    public static final int RF64_FOURCC = 1380333108;
    public static final int RIFF_FOURCC = 1380533830;
    public static final int TYPE_ALAW = 6;
    public static final int TYPE_FLOAT = 3;
    public static final int TYPE_IMA_ADPCM = 17;
    public static final int TYPE_MLAW = 7;
    public static final int TYPE_PCM = 1;
    public static final int TYPE_WAVE_FORMAT_EXTENSIBLE = 65534;
    public static final int WAVE_FOURCC = 1463899717;

    private WavUtil() {
    }

    public static int getPcmEncodingForType(int r2, int r3) {
        switch(r2) {
            case 1: goto L10;
            case 3: goto L6;
            case 65534: goto L10;
            default: goto L4;
        };
    L4:
        return 0;
    L6:
        if (r3 != 32) goto L11;
        return 4;
    L11:
        return 0;
    L10:
        return Util.getPcmEncoding(r3);
    }

    public static int getTypeForPcmEncoding(int r1) {
        switch(r1) {
            case 2: goto L7;
            case 3: goto L7;
            case 4: goto L5;
            case 21: goto L7;
            case 22: goto L7;
            default: goto L4;
        };
    L5:
        return 3;
    L7:
        return 1;
    L4:
        throw new IllegalArgumentException();
    }
}
