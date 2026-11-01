package androidx.media3.extractor.mp4;

import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.container.Mp4Box;
import java.nio.ByteBuffer;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public final class PsshAtomUtil {
    private static final String TAG = "PsshAtomUtil";

    public static final class PsshAtom {
        public final UUID[] keyIds;
        public final byte[] schemeData;
        public final UUID uuid;
        public final int version;

        PsshAtom(UUID r1, int r2, byte[] r3, UUID[] r4) {
            this.uuid = r1;
            this.version = r2;
            this.schemeData = r3;
            this.keyIds = r4;
        }
    }

    private PsshAtomUtil() {
    }

    public static byte[] buildPsshAtom(UUID r1, byte[] r2) {
        return buildPsshAtom(r1, null, r2);
    }

    public static byte[] buildPsshAtom(UUID r9, UUID[] r10, byte[] r11) {
        if (r11 == null) goto L5;
        int r1 = r11.length;
    L6:
        int r2 = r1 + 32;
        if (r10 == null) goto L9;
        r2 = r2 + ((r10.length * 16) + 4);
    L9:
        ByteBuffer r3 = ByteBuffer.allocate(r2);
        r3.putInt(r2);
        r3.putInt(Mp4Box.TYPE_pssh);
        if (r10 == null) goto L12;
        int r4 = 16777216;
    L13:
        r3.putInt(r4);
        r3.putLong(r9.getMostSignificantBits());
        r3.putLong(r9.getLeastSignificantBits());
        if (r10 == null) goto L18;
        r3.putInt(r10.length);
        int r5 = r10.length;
        int r6 = 0;
    L16:
        if (r6 >= r5) goto L18;
        UUID r7 = r10[r6];
        r3.putLong(r7.getMostSignificantBits());
        r3.putLong(r7.getLeastSignificantBits());
        r6 = r6 + 1;
    L18:
        if (r11 != null) goto L20;
    L22:
        r3.putInt(0);
    L24:
        return r3.array();
    L20:
        if (r11.length == 0) goto L22;
        r3.putInt(r11.length);
        r3.put(r11);
        goto L24
    L12:
        r4 = 0;
        goto L13
    L5:
        r1 = 0;
        goto L6
    }

    public static boolean isPsshAtom(byte[] r1) {
        if (parsePsshAtom(r1) == null) goto L5;
        return true;
    L5:
        return false;
    }

    public static PsshAtom parsePsshAtom(byte[] r18) {
        ParsableByteArray r0 = new ParsableByteArray(r18);
        PsshAtom r4 = null;
        if (r0.limit() >= 32) goto L5;
        return null;
    L5:
        r0.setPosition(0);
        int r3 = r0.bytesLeft();
        int r5 = r0.readInt();
        if (r5 == r3) goto L9;
        Log.m48w(TAG, "Advertised atom size (" + r5 + ") does not match buffer size: " + r3);
        return null;
    L9:
        int r7 = r0.readInt();
        if (r7 == 1886614376) goto L13;
        Log.m48w(TAG, "Atom type is not pssh: " + r7);
        return null;
    L13:
        int r8 = BoxParser.parseFullBoxVersion(r0.readInt());
        if (r8 <= 1) goto L17;
        Log.m48w(TAG, "Unsupported pssh version: " + r8);
        return null;
    L17:
        UUID r10 = new UUID(r0.readLong(), r0.readLong());
        UUID[] r11 = null;
        if (r8 != 1) goto L23;
        int r9 = r0.readUnsignedIntToInt();
        r11 = new UUID[r9];
        int r12 = 0;
    L20:
        if (r12 >= r9) goto L22;
        r11[r12] = new UUID(r0.readLong(), r0.readLong());
        r12 = r12 + 1;
        r4 = r4;
        r5 = r5;
        goto L20
    L22:
        PsshAtom r16 = r4;
    L24:
        int r6 = r0.readUnsignedIntToInt();
        int r13 = r0.bytesLeft();
        if (r6 == r13) goto L28;
        Log.m48w(TAG, "Atom data size (" + r6 + ") does not match the bytes left: " + r13);
        return r16;
    L28:
        byte[] r14 = new byte[r6];
        r0.readBytes(r14, 0, r6);
        return new PsshAtom(r10, r8, r14, r11);
    L23:
        r16 = null;
        goto L24
    }

    public static byte[] parseSchemeSpecificData(byte[] r4, UUID r5) {
        PsshAtom r0 = parsePsshAtom(r4);
        if (r0 != null) goto L6;
        return null;
    L6:
        if (r5.equals(r0.uuid) == true) goto L10;
        Log.m48w(TAG, "UUID mismatch. Expected: " + r5 + ", got: " + r0.uuid + ".");
        return null;
    L10:
        return r0.schemeData;
    }

    public static UUID parseUuid(byte[] r2) {
        PsshAtom r0 = parsePsshAtom(r2);
        if (r0 != null) goto L7;
        return null;
    L7:
        return r0.uuid;
    }

    public static int parseVersion(byte[] r2) {
        PsshAtom r0 = parsePsshAtom(r2);
        if (r0 != null) goto L7;
        return -1;
    L7:
        return r0.version;
    }
}
