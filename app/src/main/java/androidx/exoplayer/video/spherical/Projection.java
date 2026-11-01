package androidx.media3.exoplayer.video.spherical;

import androidx.media3.common.util.Assertions;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
final class Projection {
    public static final int DRAW_MODE_TRIANGLES = 0;
    public static final int DRAW_MODE_TRIANGLES_FAN = 2;
    public static final int DRAW_MODE_TRIANGLES_STRIP = 1;
    public static final int POSITION_COORDS_PER_VERTEX = 3;
    public static final int TEXTURE_COORDS_PER_VERTEX = 2;
    public final Mesh leftMesh;
    public final Mesh rightMesh;
    public final boolean singleMesh;
    public final int stereoMode;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface DrawMode {
    }

    public static final class Mesh {
        private final SubMesh[] subMeshes;

        public Mesh(SubMesh... r1) {
            this.subMeshes = r1;
        }

        public SubMesh getSubMesh(int r2) {
            return this.subMeshes[r2];
        }

        public int getSubMeshCount() {
            return this.subMeshes.length;
        }
    }

    public static final class SubMesh {
        public static final int VIDEO_TEXTURE_ID = 0;
        public final int mode;
        public final float[] textureCoords;
        public final int textureId;
        public final float[] vertices;

        public SubMesh(int r7, float[] r8, float[] r9, int r10) {
            this.textureId = r7;
            if ((((long) r8.length) * 2) != (((long) r9.length) * 3)) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            this.vertices = r8;
            this.textureCoords = r9;
            this.mode = r10;
            return;
        L5:
            r0 = false;
            goto L6
        }

        public int getVertexCount() {
            return this.vertices.length / 3;
        }
    }

    public Projection(Mesh r1, int r2) {
        this(r1, r1, r2);
    }

    public Projection(Mesh r2, Mesh r3, int r4) {
        this.leftMesh = r2;
        this.rightMesh = r3;
        this.stereoMode = r4;
        if (r2 != r3) goto L5;
        boolean r0 = true;
    L6:
        this.singleMesh = r0;
        return;
    L5:
        r0 = false;
        goto L6
    }

    public static Projection createEquirectangular(float r26, int r27, int r28, float r29, float r30, int r31) {
        int r1 = r27;
        if (r26 <= 0.0f) goto L5;
        boolean r6 = true;
    L6:
        Assertions.checkArgument(r6);
        if (r1 < 1) goto L9;
        boolean r7 = true;
    L10:
        Assertions.checkArgument(r7);
        if (r28 < 1) goto L13;
        boolean r8 = true;
    L14:
        Assertions.checkArgument(r8);
        if (r29 > 0.0f) goto L17;
    L19:
        boolean r9 = false;
    L20:
        Assertions.checkArgument(r9);
        if (r30 > 0.0f) goto L23;
    L25:
        boolean r5 = false;
    L26:
        Assertions.checkArgument(r5);
        float r10 = (float) Math.toRadians(r29);
        float r11 = (float) Math.toRadians(r30);
        float r12 = r10 / r1;
        float r13 = r11 / r28;
        int r14 = (((r28 + 1) * 2) + 2) * r1;
        float[] r15 = new float[r14 * 3];
        float[] r16 = new float[r14 * 2];
        int r17 = 0;
        int r18 = 0;
        int r19 = 0;
    L27:
        if (r19 >= r1) goto L49;
        float r20 = (r19 * r12) - (r10 / 2.0f);
        float r110 = ((r19 + 1) * r12) - (r10 / 2.0f);
        int r2 = 0;
    L30:
        if (r2 >= (r28 + 1)) goto L47;
        int r3 = 0;
    L33:
        if (r3 >= 2) goto L46;
        if (r3 != 0) goto L36;
        float r4 = r20;
    L37:
        float r21 = r10;
        float r22 = ((r2 * r13) + 3.1415927f) - (r11 / 2.0f);
        int r23 = r17 + 1;
        float r24 = r11;
        int r25 = r19;
        r15[r17] = -((float) (Math.cos(r4) * (Math.sin(r22) * ((double) r26))));
        int r32 = r23 + 1;
        r15[r23] = (float) (Math.sin(r4) * ((double) r26));
        int r33 = r32 + 1;
        float r210 = r20;
        r15[r32] = (float) (Math.cos(r4) * (Math.cos(r22) * ((double) r26)));
        int r34 = r18 + 1;
        r16[r18] = (r2 * r13) / r24;
        int r35 = r34 + 1;
        r16[r34] = ((r25 + r3) * r12) / r21;
        if (r2 != 0) goto L40;
        if (r3 != 0) goto L40;
    L43:
        System.arraycopy(r15, r33 - 3, r15, r33, 3);
        r33 = r33 + 3;
        System.arraycopy(r16, r35 - 2, r16, r35, 2);
        r35 = r35 + 2;
    L45:
        r18 = r35;
        r3 = r3 + 1;
        r17 = r33;
        r10 = r21;
        r20 = r210;
        r11 = r24;
        r19 = r25;
    L40:
        if (r2 != r28) goto L45;
        if (r3 != 1) goto L45;
    L36:
        r4 = r110;
        goto L37
    L46:
        r2 = r2 + 1;
        goto L30
    L47:
        r19 = r19 + 1;
        r1 = r27;
        goto L27
    L49:
        return new Projection(new Mesh(new SubMesh[]{new SubMesh(0, r15, r16, 1)}), r31);
    L23:
        if (r30 > 360.0f) goto L25;
        r5 = true;
        goto L26
    L17:
        if (r29 > 180.0f) goto L19;
        r9 = true;
        goto L20
    L13:
        r8 = false;
        goto L14
    L9:
        r7 = false;
        goto L10
    L5:
        r6 = false;
        goto L6
    }

    public static Projection createEquirectangular(int r6) {
        return createEquirectangular(50.0f, 36, 72, 180.0f, 360.0f, r6);
    }
}
