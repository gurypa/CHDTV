package androidx.media3.extractor;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;

/* JADX INFO: loaded from: classes.dex */
public final class CeaUtil {
    private static final int COUNTRY_CODE = 181;
    private static final int PAYLOAD_TYPE_CC = 4;
    private static final int PROVIDER_CODE_ATSC = 49;
    private static final int PROVIDER_CODE_DIRECTV = 47;
    private static final String TAG = "CeaUtil";
    public static final int USER_DATA_IDENTIFIER_GA94 = 1195456820;
    public static final int USER_DATA_TYPE_CODE_MPEG_CC = 3;

    private CeaUtil() {
    }

    public static void consume(long r12, ParsableByteArray r14, TrackOutput[] r15) {
    L2:
        boolean r1 = true;
        if (r14.bytesLeft() <= 1) goto L37;
        int r0 = readNon255TerminatedValue(r14);
        int r2 = readNon255TerminatedValue(r14);
        int r3 = r14.getPosition() + r2;
        if (r2 != (-1)) goto L7;
    L35:
        Log.m48w(TAG, "Skipping remainder of malformed SEI NAL unit.");
        r3 = r14.limit();
    L36:
        r14.setPosition(r3);
        goto L2
    L7:
        if (r2 > r14.bytesLeft()) goto L35;
        if (r0 != 4) goto L36;
        if (r2 < 8) goto L36;
        int r4 = r14.readUnsignedByte();
        int r5 = r14.readUnsignedShort();
        int r6 = 0;
        if (r5 != PROVIDER_CODE_ATSC) goto L16;
        r6 = r14.readInt();
    L16:
        int r8 = r14.readUnsignedByte();
        if (r5 != 47) goto L20;
        r14.skipBytes(1);
    L20:
        if (r4 != COUNTRY_CODE) goto L26;
        if (r5 == PROVIDER_CODE_ATSC) goto L24;
        if (r5 != 47) goto L26;
    L24:
        if (r8 != 3) goto L26;
        boolean r9 = true;
    L27:
        if (r5 == PROVIDER_CODE_ATSC) goto L29;
    L33:
        if (r9 == false) goto L36;
        consumeCcData(r12, r14, r15);
        goto L36
    L29:
        if (r6 == 1195456820) goto L32;
        r1 = false;
    L32:
        r9 = r9 & r1;
    L26:
        r9 = false;
        goto L27
    }

    public static void consumeCcData(long r17, ParsableByteArray r19, TrackOutput[] r20) {
        int r2 = r19.readUnsignedByte();
        if ((r2 & 64) == 0) goto L5;
        boolean r3 = true;
    L6:
        if (r3 == true) goto L8;
        return;
    L8:
        r19.skipBytes(1);
        int r11 = (r2 & 31) * 3;
        int r14 = r19.getPosition();
        int r15 = r20.length;
        int r7 = 0;
    L9:
        if (r7 >= r15) goto L15;
        int r8 = r7;
        TrackOutput r9 = r20[r8];
        r19.setPosition(r14);
        r9.sampleData(r19, r11);
        if (r17 == C0180C.TIME_UNSET) goto L13;
        boolean r10 = true;
    L14:
        Assertions.checkState(r10);
        r9.sampleMetadata(r17, 1, r11, 0, null);
        r7 = r8 + 1;
        goto L9
    L13:
        r10 = false;
        goto L14
    L15:
        return;
    L5:
        r3 = false;
        goto L6
    }

    private static int readNon255TerminatedValue(ParsableByteArray r3) {
        int r0 = 0;
    L4:
        if (r3.bytesLeft() == 0) goto L5;
        int r1 = r3.readUnsignedByte();
        r0 = r0 + r1;
        if (r1 == 255) goto L4;
        return r0;
    L5:
        return -1;
    }
}
