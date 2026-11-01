package androidx.media3.exoplayer.video.spherical;

import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import java.util.ArrayList;
import java.util.zip.Inflater;

/* JADX INFO: loaded from: classes.dex */
final class ProjectionDecoder {
    private static final int MAX_COORDINATE_COUNT = 10000;
    private static final int MAX_TRIANGLE_INDICES = 128000;
    private static final int MAX_VERTEX_COUNT = 32000;
    private static final int TYPE_DFL8 = 1684433976;
    private static final int TYPE_MESH = 1835365224;
    private static final int TYPE_MSHP = 1836279920;
    private static final int TYPE_PROJ = 1886547818;
    private static final int TYPE_RAW = 1918990112;
    private static final int TYPE_YTMP = 2037673328;

    private ProjectionDecoder() {
    }

    public static Projection decode(byte[] r5, int r6) {
        ParsableByteArray r0 = new ParsableByteArray(r5);
        ArrayList<Projection.Mesh> r1 = null;
        if (isProj(r0) == false) goto L6;
        ArrayList<Projection.Mesh> r2 = parseProj(r0);     // Catch: ArrayIndexOutOfBoundsException -> L8
    L7:
        r1 = r2;
        goto L10
    L6:
        r2 = parseMshp(r0);     // Catch: ArrayIndexOutOfBoundsException -> L8
    L10:
        if (r1 != null) goto L13;
        return null;
    L13:
        switch(r1.size()) {
            case 1: goto L18;
            case 2: goto L16;
            default: goto L14;
        };
    L14:
        return null;
    L16:
        return new Projection(r1.get(0), r1.get(1), r6);
    L18:
        return new Projection(r1.get(0), r6);
    }

    private static int decodeZigZag(int r2) {
        return (r2 >> 1) ^ (-(r2 & 1));
    }

    private static boolean isProj(ParsableByteArray r3) {
        r3.skipBytes(4);
        int r0 = r3.readInt();
        r3.setPosition(0);
        if (r0 != 1886547818) goto L6;
        return true;
    L6:
        return false;
    }

    private static Projection.Mesh parseMesh(ParsableByteArray r29) {
        int r0 = r29.readInt();
        Projection.Mesh r2 = null;
        if (r0 <= 10000) goto L5;
        return null;
    L5:
        float[] r1 = new float[r0];
        int r3 = 0;
    L6:
        if (r3 >= r0) goto L8;
        r1[r3] = r29.readFloat();
        r3 = r3 + 1;
        goto L6
    L8:
        int r4 = r29.readInt();
        if (r4 <= MAX_VERTEX_COUNT) goto L11;
        return null;
    L11:
        double r5 = 2.0d;
        double r6 = Math.log(2.0d);
        int r8 = (int) Math.ceil(Math.log(((double) r0) * 2.0d) / r6);
        ParsableBitArray r9 = new ParsableBitArray(r29.getData());
        int r11 = 8;
        r9.setPosition(r29.getPosition() * 8);
        float[] r10 = new float[r4 * 5];
        int[] r13 = new int[5];
        int r14 = 0;
        int r15 = 0;
    L12:
        if (r15 >= r4) goto L22;
        Projection.Mesh r16 = r2;
        int r7 = 0;
    L14:
        if (r7 >= 5) goto L21;
        double r19 = r5;
        int r12 = r13[r7] + decodeZigZag(r9.readBits(r8));
        if (r12 >= r0) goto L20;
        if (r12 < 0) goto L20;
        r10[r14] = r1[r12];
        r13[r7] = r12;
        r7 = r7 + 1;
        r14 = r14 + 1;
        r5 = r19;
    L20:
        return r16;
    L21:
        r15 = r15 + 1;
        r2 = r16;
        goto L12
    L22:
        Projection.Mesh r17 = r2;
        double r110 = r5;
        r9.setPosition((r9.getPosition() + 7) & (-8));
        int r18 = 32;
        int r20 = r9.readBits(32);
        Projection.SubMesh[] r21 = new Projection.SubMesh[r20];
        int r111 = 0;
    L23:
        if (r111 >= r20) goto L37;
        int r112 = r9.readBits(r11);
        int r113 = r0;
        int r22 = r9.readBits(r11);
        int r114 = r9.readBits(r18);
        if (r114 > MAX_TRIANGLE_INDICES) goto L26;
        float[] r23 = r1;
        int r24 = (int) Math.ceil(Math.log(((double) r4) * r110) / r6);
        int r25 = 0;
        float[] r26 = new float[r114 * 3];
        int r27 = r20;
        float[] r28 = new float[r114 * 2];
        double r210 = r6;
        int r30 = 0;
    L28:
        if (r30 >= r114) goto L35;
        int r31 = r25 + decodeZigZag(r9.readBits(r24));
        if (r31 < 0) goto L34;
        if (r31 >= r4) goto L34;
        r26[r30 * 3] = r10[r31 * 5];
        r26[(r30 * 3) + 1] = r10[(r31 * 5) + 1];
        r26[(r30 * 3) + 2] = r10[(r31 * 5) + 2];
        r28[r30 * 2] = r10[(r31 * 5) + 3];
        r28[(r30 * 2) + 1] = r10[(r31 * 5) + 4];
        r30 = r30 + 1;
        r25 = r31;
    L34:
        return r17;
    L35:
        r21[r111] = new Projection.SubMesh(r112, r26, r28, r22);
        r111 = r111 + 1;
        r0 = r113;
        r1 = r23;
        r20 = r27;
        r6 = r210;
        r18 = 32;
        r11 = 8;
        goto L23
    L26:
        return r17;
    L37:
        return new Projection.Mesh(r21);
    }

    private static ArrayList<Projection.Mesh> parseMshp(ParsableByteArray r6) {
        if (r6.readUnsignedByte() == 0) goto L5;
        return null;
    L5:
        r6.skipBytes(7);
        int r2 = r6.readInt();
        if (r2 != TYPE_DFL8) goto L17;
        ParsableByteArray r3 = new ParsableByteArray();
        Inflater r4 = new Inflater(true);
        if (Util.inflate(r6, r3, r4) == true) goto L12;
        r4.end();
        return null;
    L12:
        r4.end();
        r6 = r3;
    L20:
        return parseRawMshpData(r6);
    L13:
        th = move-exception;
        r4.end();
        throw th;
    L17:
        if (r2 == TYPE_RAW) goto L20;
        return null;
    }

    private static ArrayList<Projection.Mesh> parseProj(ParsableByteArray r5) {
        r5.skipBytes(8);
        int r0 = r5.getPosition();
        int r1 = r5.limit();
    L4:
        if (r0 >= r1) goto L18;
        int r3 = r5.readInt() + r0;
        if (r3 <= r0) goto L17;
        if (r3 > r1) goto L17;
        int r2 = r5.readInt();
        if (r2 == TYPE_YTMP) goto L15;
        if (r2 == TYPE_MSHP) goto L15;
        r0 = r3;
        r5.setPosition(r0);
    L15:
        r5.setLimit(r3);
        return parseMshp(r5);
    L17:
        return null;
    L18:
        return null;
    }

    private static ArrayList<Projection.Mesh> parseRawMshpData(ParsableByteArray r7) {
        ArrayList<Projection.Mesh> r0 = new ArrayList();
        int r1 = r7.getPosition();
        int r2 = r7.limit();
    L3:
        if (r1 >= r2) goto L16;
        int r3 = r7.readInt() + r1;
        if (r3 <= r1) goto L15;
        if (r3 > r2) goto L15;
        if (r7.readInt() != TYPE_MESH) goto L14;
        Projection.Mesh r6 = parseMesh(r7);
        if (r6 == null) goto L12;
        r0.add(r6);
        goto L14
    L12:
        return null;
    L14:
        r1 = r3;
        r7.setPosition(r1);
    L15:
        return null;
    L16:
        return r0;
    }
}
