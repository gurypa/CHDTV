package androidx.media3.extractor.mp4;

import androidx.media3.common.C0180C;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.metadata.mp4.SmtaMetadataEntry;
import androidx.media3.extractor.p001ts.PsExtractor;

/* JADX INFO: loaded from: classes.dex */
public final class SmtaAtomUtil {
    private static final int CAMCORDER_FRC_SUPERSLOW_MOTION = 9;
    private static final int CAMCORDER_FRC_SUPERSLOW_MOTION_HEVC = 22;
    private static final int CAMCORDER_NORMAL = 0;
    private static final int CAMCORDER_QFRC_SUPERSLOW_MOTION = 23;
    private static final int CAMCORDER_SINGLE_SUPERSLOW_MOTION = 7;
    private static final int CAMCORDER_SLOW_MOTION_V2 = 12;
    private static final int CAMCORDER_SLOW_MOTION_V2_120 = 13;
    private static final int CAMCORDER_SLOW_MOTION_V2_HEVC = 21;
    private static final int NO_VALUE = -1;

    private SmtaAtomUtil() {
    }

    private static int getCaptureFrameRate(int r4, ParsableByteArray r5, int r6) {
        if (r4 != 12) goto L7;
        return PsExtractor.VIDEO_STREAM_MASK;
    L7:
        if (r4 != 13) goto L11;
        return 120;
    L11:
        if (r4 == 21) goto L14;
        return C0180C.RATE_UNSET_INT;
    L14:
        if (r5.bytesLeft() >= 8) goto L16;
    L26:
        return C0180C.RATE_UNSET_INT;
    L16:
        if ((r5.getPosition() + 8) > r6) goto L26;
        int r1 = r5.readInt();
        int r3 = r5.readInt();
        if (r1 >= 12) goto L21;
    L25:
        return C0180C.RATE_UNSET_INT;
    L21:
        if (r3 != 1936877170) goto L25;
        return r5.readUnsignedFixedPoint1616();
    }

    public static Metadata parseSmta(ParsableByteArray r10, int r11) {
        r10.skipBytes(12);
    L4:
        if (r10.getPosition() >= r11) goto L25;
        int r0 = r10.getPosition();
        int r2 = r10.readInt();
        if (r10.readInt() == 1935766900) goto L8;
        r10.setPosition(r0 + r2);
        goto L4
    L8:
        if (r2 >= 16) goto L10;
        return null;
    L10:
        r10.skipBytes(4);
        int r4 = -1;
        int r5 = 0;
        int r6 = 0;
    L12:
        if (r6 >= 2) goto L19;
        int r7 = r10.readUnsignedByte();
        int r9 = r10.readUnsignedByte();
        if (r7 != 0) goto L16;
        r4 = r9;
    L18:
        r6 = r6 + 1;
        goto L12
    L16:
        if (r7 != 1) goto L18;
        r5 = r9;
        goto L18
    L19:
        int r8 = getCaptureFrameRate(r4, r10, r11);
        if (r8 != (-2147483647)) goto L23;
        return null;
    L23:
        return new Metadata(new Metadata.Entry[]{new SmtaMetadataEntry(r8, r5)});
    L25:
        return null;
    }
}
