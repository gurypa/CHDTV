package androidx.media3.exoplayer.video.spherical;

import android.opengl.GLES20;
import android.util.Log;
import androidx.media3.common.util.GlProgram;
import androidx.media3.common.util.GlUtil;
import java.nio.FloatBuffer;

/* JADX INFO: loaded from: classes.dex */
final class ProjectionRenderer {
    private static final String FRAGMENT_SHADER = "// This is required since the texture data is GL_TEXTURE_EXTERNAL_OES.\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\n// Standard texture rendering shader.\nuniform samplerExternalOES uTexture;\nvarying vec2 vTexCoords;\nvoid main() {\n  gl_FragColor = texture2D(uTexture, vTexCoords);\n}\n";
    private static final String TAG = "ProjectionRenderer";
    private static final float[] TEX_MATRIX_BOTTOM = null;
    private static final float[] TEX_MATRIX_LEFT = null;
    private static final float[] TEX_MATRIX_RIGHT = null;
    private static final float[] TEX_MATRIX_TOP = null;
    private static final float[] TEX_MATRIX_WHOLE = null;
    private static final String VERTEX_SHADER = "uniform mat4 uMvpMatrix;\nuniform mat3 uTexMatrix;\nattribute vec4 aPosition;\nattribute vec2 aTexCoords;\nvarying vec2 vTexCoords;\n// Standard transformation.\nvoid main() {\n  gl_Position = uMvpMatrix * aPosition;\n  vTexCoords = (uTexMatrix * vec3(aTexCoords, 1)).xy;\n}\n";
    private MeshData leftMeshData;
    private int mvpMatrixHandle;
    private int positionHandle;
    private GlProgram program;
    private MeshData rightMeshData;
    private int stereoMode;
    private int texCoordsHandle;
    private int textureHandle;
    private int uTexMatrixHandle;

    private static class MeshData {
        private final int drawMode;
        private final FloatBuffer textureBuffer;
        private final FloatBuffer vertexBuffer;
        private final int vertexCount;

        public MeshData(Projection.SubMesh r2) {
            this.vertexCount = r2.getVertexCount();
            this.vertexBuffer = GlUtil.createBuffer(r2.vertices);
            this.textureBuffer = GlUtil.createBuffer(r2.textureCoords);
            switch(r2.mode) {
                case 1: goto L6;
                case 2: goto L5;
                default: goto L4;
            };
        L4:
            this.drawMode = 4;
            return;
        L5:
            this.drawMode = 6;
            return;
        L6:
            this.drawMode = 5;
        }

        static /* synthetic */ FloatBuffer access$000(MeshData r1) {
            return r1.vertexBuffer;
        }

        static /* synthetic */ FloatBuffer access$100(MeshData r1) {
            return r1.textureBuffer;
        }

        static /* synthetic */ int access$200(MeshData r1) {
            return r1.drawMode;
        }

        static /* synthetic */ int access$300(MeshData r1) {
            return r1.vertexCount;
        }
    }

    static {
        TEX_MATRIX_WHOLE = new float[]{1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f};
        TEX_MATRIX_TOP = new float[]{1.0f, 0.0f, 0.0f, 0.0f, -0.5f, 0.0f, 0.0f, 0.5f, 1.0f};
        TEX_MATRIX_BOTTOM = new float[]{1.0f, 0.0f, 0.0f, 0.0f, -0.5f, 0.0f, 0.0f, 1.0f, 1.0f};
        TEX_MATRIX_LEFT = new float[]{0.5f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f};
        TEX_MATRIX_RIGHT = new float[]{0.5f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.5f, 1.0f, 1.0f};
    }

    ProjectionRenderer() {
    }

    public static boolean isSupported(Projection r5) {
        Projection.Mesh r0 = r5.leftMesh;
        Projection.Mesh r1 = r5.rightMesh;
        if (r0.getSubMeshCount() == 1) goto L5;
        return false;
    L5:
        if (r0.getSubMesh(0).textureId == 0) goto L7;
        return false;
    L7:
        if (r1.getSubMeshCount() == 1) goto L9;
        return false;
    L9:
        if (r1.getSubMesh(0).textureId == 0) goto L12;
        return false;
    L12:
        return true;
    }

    public void draw(int r13, float[] r14, boolean r15) {
        if (r15 == false) goto L5;
        MeshData r0 = this.rightMeshData;
    L6:
        MeshData r2 = r0;
        if (r2 != null) goto L10;
        return;
    L10:
        if (this.stereoMode != 1) goto L16;
        if (r15 == false) goto L13;
        float[] r1 = TEX_MATRIX_BOTTOM;
    L14:
        float[] r4 = r1;
    L22:
        GLES20.glUniformMatrix3fv(this.uTexMatrixHandle, 1, false, r4, 0);
        GLES20.glUniformMatrix4fv(this.mvpMatrixHandle, 1, false, r14, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(36197, r13);
        GLES20.glUniform1i(this.textureHandle, 0);
        GlUtil.checkGlError();     // Catch: GlUtil.GlException -> L25
    L27:
        GLES20.glVertexAttribPointer(this.positionHandle, 3, 5126, false, 12, MeshData.access$000(r2));
        GlUtil.checkGlError();     // Catch: GlUtil.GlException -> L30
    L32:
        GLES20.glVertexAttribPointer(this.texCoordsHandle, 2, 5126, false, 8, MeshData.access$100(r2));
        GlUtil.checkGlError();     // Catch: GlUtil.GlException -> L35
    L37:
        GLES20.glDrawArrays(MeshData.access$200(r2), 0, MeshData.access$300(r2));
        GlUtil.checkGlError();     // Catch: GlUtil.GlException -> L40
        return;
    L40:
        e = move-exception;
        Log.e(TAG, "Failed to render", e);
        return;
    L35:
        e = move-exception;
        Log.e(TAG, "Failed to load texture data", e);
    L30:
        e = move-exception;
        Log.e(TAG, "Failed to load position data", e);
    L25:
        e = move-exception;
        Log.e(TAG, "Failed to bind uniforms", e);
        goto L27
    L13:
        r1 = TEX_MATRIX_TOP;
        goto L14
    L16:
        if (this.stereoMode != 2) goto L21;
        if (r15 == false) goto L19;
        float[] r3 = TEX_MATRIX_RIGHT;
    L20:
        r4 = r3;
        goto L22
    L19:
        r3 = TEX_MATRIX_LEFT;
        goto L20
    L21:
        r4 = TEX_MATRIX_WHOLE;
        goto L22
    L5:
        r0 = this.leftMeshData;
        goto L6
    }

    public void init() {
        this.program = new GlProgram(VERTEX_SHADER, FRAGMENT_SHADER);     // Catch: GlUtil.GlException -> L4
        this.mvpMatrixHandle = this.program.getUniformLocation("uMvpMatrix");     // Catch: GlUtil.GlException -> L4
        this.uTexMatrixHandle = this.program.getUniformLocation("uTexMatrix");     // Catch: GlUtil.GlException -> L4
        this.positionHandle = this.program.getAttributeArrayLocationAndEnable("aPosition");     // Catch: GlUtil.GlException -> L4
        this.texCoordsHandle = this.program.getAttributeArrayLocationAndEnable("aTexCoords");     // Catch: GlUtil.GlException -> L4
        this.textureHandle = this.program.getUniformLocation("uTexture");     // Catch: GlUtil.GlException -> L4
        return;
    L4:
        e = move-exception;
        Log.e(TAG, "Failed to initialize the program", e);
    }

    public void setProjection(Projection r4) {
        if (isSupported(r4) == true) goto L5;
        return;
    L5:
        this.stereoMode = r4.stereoMode;
        this.leftMeshData = new MeshData(r4.leftMesh.getSubMesh(0));
        if (r4.singleMesh == false) goto L8;
        MeshData r0 = this.leftMeshData;
    L9:
        this.rightMeshData = r0;
        return;
    L8:
        r0 = new MeshData(r4.rightMesh.getSubMesh(0));
        goto L9
    }

    public void shutdown() {
        if (this.program == null) goto L11;
        this.program.delete();     // Catch: GlUtil.GlException -> L6
        return;
    L6:
        e = move-exception;
        Log.e(TAG, "Failed to delete the shader program", e);
        return;
    }
}
