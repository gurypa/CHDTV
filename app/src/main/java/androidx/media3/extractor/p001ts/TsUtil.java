package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.ParsableByteArray;

/* JADX INFO: loaded from: classes.dex */
public final class TsUtil {
    private TsUtil() {
    }

    public static int findSyncBytePosition(byte[] r3, int r4, int r5) {
        int r0 = r4;
    L3:
        if (r0 >= r5) goto L7;
        if (r3[r0] == 71) goto L7;
        r0 = r0 + 1;
    L7:
        return r0;
    }

    public static boolean isStartOfTsPacket(byte[] r5, int r6, int r7, int r8) {
        int r0 = 0;
        int r1 = -4;
    L4:
        if (r1 > 4) goto L17;
        int r2 = (r1 * TsExtractor.TS_PACKET_SIZE) + r8;
        if (r2 < r6) goto L15;
        if (r2 >= r7) goto L15;
        if (r5[r2] != 71) goto L15;
        r0 = r0 + 1;
        if (r0 != 5) goto L16;
        return true;
    L16:
        r1 = r1 + 1;
    L15:
        r0 = 0;
        goto L16
    L17:
        return false;
    }

    public static long readPcrFromPacket(ParsableByteArray r11, int r12, int r13) {
        r11.setPosition(r12);
        if (r11.bytesLeft() >= 5) goto L5;
        return C0180C.TIME_UNSET;
    L5:
        int r0 = r11.readInt();
        if ((8388608 & r0) == 0) goto L9;
        return C0180C.TIME_UNSET;
    L9:
        if (((2096896 & r0) >> 8) == r13) goto L11;
        return C0180C.TIME_UNSET;
    L11:
        boolean r5 = true;
        if ((r0 & 32) == 0) goto L14;
        boolean r4 = true;
    L15:
        if (r4 == true) goto L18;
        return C0180C.TIME_UNSET;
    L18:
        if (r11.readUnsignedByte() >= 7) goto L20;
    L28:
        return C0180C.TIME_UNSET;
    L20:
        if (r11.bytesLeft() < 7) goto L28;
        if ((r11.readUnsignedByte() & 16) == 16) goto L25;
        r5 = false;
    L25:
        if (r5 == false) goto L28;
        byte[] r2 = new byte[6];
        r11.readBytes(r2, 0, r2.length);
        return readPcrValueFromPcrBytes(r2);
    L14:
        r4 = false;
        goto L15
    }

    private static long readPcrValueFromPcrBytes(byte[] r8) {
        return (((((((long) r8[0]) & 255) << 25) | ((((long) r8[1]) & 255) << 17)) | ((((long) r8[2]) & 255) << 9)) | ((((long) r8[3]) & 255) << 1)) | ((255 & ((long) r8[4])) >> 7);
    }
}
