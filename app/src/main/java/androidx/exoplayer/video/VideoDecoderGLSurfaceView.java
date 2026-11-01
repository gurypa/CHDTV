package androidx.media3.exoplayer.video;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.GlProgram;
import androidx.media3.common.util.GlUtil;
import androidx.media3.decoder.VideoDecoderOutputBuffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.atomic.AtomicReference;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class VideoDecoderGLSurfaceView extends GLSurfaceView implements VideoDecoderOutputBufferRenderer {
    private static final String TAG = "VideoDecoderGLSV";
    private final Renderer renderer;

    private static final class Renderer implements GLSurfaceView.Renderer {
        private static final String FRAGMENT_SHADER = "precision mediump float;\nvarying vec2 interp_tc_y;\nvarying vec2 interp_tc_u;\nvarying vec2 interp_tc_v;\nuniform sampler2D y_tex;\nuniform sampler2D u_tex;\nuniform sampler2D v_tex;\nuniform mat3 mColorConversion;\nvoid main() {\n  vec3 yuv;\n  yuv.x = texture2D(y_tex, interp_tc_y).r - 0.0625;\n  yuv.y = texture2D(u_tex, interp_tc_u).r - 0.5;\n  yuv.z = texture2D(v_tex, interp_tc_v).r - 0.5;\n  gl_FragColor = vec4(mColorConversion * yuv, 1.0);\n}\n";
        private static final String[] TEXTURE_UNIFORMS = null;
        private static final FloatBuffer TEXTURE_VERTICES = null;
        private static final String VERTEX_SHADER = "varying vec2 interp_tc_y;\nvarying vec2 interp_tc_u;\nvarying vec2 interp_tc_v;\nattribute vec4 in_pos;\nattribute vec2 in_tc_y;\nattribute vec2 in_tc_u;\nattribute vec2 in_tc_v;\nvoid main() {\n  gl_Position = in_pos;\n  interp_tc_y = in_tc_y;\n  interp_tc_u = in_tc_u;\n  interp_tc_v = in_tc_v;\n}\n";
        private static final float[] kColorConversion2020 = null;
        private static final float[] kColorConversion601 = null;
        private static final float[] kColorConversion709 = null;
        private int colorMatrixLocation;
        private final AtomicReference<VideoDecoderOutputBuffer> pendingOutputBufferReference;
        private final int[] previousStrides;
        private final int[] previousWidths;
        private GlProgram program;
        private VideoDecoderOutputBuffer renderedOutputBuffer;
        private final GLSurfaceView surfaceView;
        private final int[] texLocations;
        private final FloatBuffer[] textureCoords;
        private final int[] yuvTextures;

        static {
            kColorConversion601 = new float[]{1.164f, 1.164f, 1.164f, 0.0f, -0.392f, 2.017f, 1.596f, -0.813f, 0.0f};
            kColorConversion709 = new float[]{1.164f, 1.164f, 1.164f, 0.0f, -0.213f, 2.112f, 1.793f, -0.533f, 0.0f};
            kColorConversion2020 = new float[]{1.168f, 1.168f, 1.168f, 0.0f, -0.188f, 2.148f, 1.683f, -0.652f, 0.0f};
            TEXTURE_UNIFORMS = new String[]{"y_tex", "u_tex", "v_tex"};
            TEXTURE_VERTICES = GlUtil.createBuffer(new float[]{-1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f});
        }

        public Renderer(GLSurfaceView r6) {
            this.surfaceView = r6;
            this.yuvTextures = new int[3];
            this.texLocations = new int[3];
            this.previousWidths = new int[3];
            this.previousStrides = new int[3];
            this.pendingOutputBufferReference = new AtomicReference();
            this.textureCoords = new FloatBuffer[3];
            int r1 = 0;
        L3:
            if (r1 >= 3) goto L5;
            int[] r2 = this.previousWidths;
            this.previousStrides[r1] = -1;
            r2[r1] = -1;
            r1 = r1 + 1;
            goto L3
        }

        @RequiresNonNull({"program"})
        private void setupTextures() {
            GLES20.glGenTextures(3, this.yuvTextures, 0);     // Catch: GlUtil.GlException -> L7
            int r0 = 0;
        L3:
            if (r0 >= 3) goto L5;
            GLES20.glUniform1i(this.program.getUniformLocation(TEXTURE_UNIFORMS[r0]), r0);     // Catch: GlUtil.GlException -> L7
            GLES20.glActiveTexture(33984 + r0);     // Catch: GlUtil.GlException -> L7
            GlUtil.bindTexture(3553, this.yuvTextures[r0], 9729);     // Catch: GlUtil.GlException -> L7
            r0 = r0 + 1;     // Catch: GlUtil.GlException -> L7
            goto L3
        L5:
            GlUtil.checkGlError();     // Catch: GlUtil.GlException -> L7
            return;
        L7:
            e = move-exception;
            Log.e(VideoDecoderGLSurfaceView.TAG, "Failed to set up the textures", e);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.opengl.GLSurfaceView.Renderer
        public void onDrawFrame(GL10 r24) {
            VideoDecoderOutputBuffer r2 = this.pendingOutputBufferReference.getAndSet(null);
            if (r2 == null) goto L5;
        L7:
            if (r2 != null) goto L9;
        L12:
            VideoDecoderOutputBuffer r3 = (VideoDecoderOutputBuffer) Assertions.checkNotNull(this.renderedOutputBuffer);
            float[] r0 = kColorConversion709;
            switch(r3.colorspace) {
                case 1: goto L16;
                case 2: goto L14;
                case 3: goto L15;
                default: goto L14;
            };
        L14:
            float[] r4 = r0;
        L17:
            char r5 = 1;
            GLES20.glUniformMatrix3fv(this.colorMatrixLocation, 1, false, r4, 0);
            int[] r7 = (int[]) Assertions.checkNotNull(r3.yuvStrides);
            ByteBuffer[] r8 = (ByteBuffer[]) Assertions.checkNotNull(r3.yuvPlanes);
            int r1 = 0;
        L19:
            if (r1 >= 3) goto L24;
            if (r1 != 0) goto L22;
            int r9 = r3.height;
        L23:
            GLES20.glActiveTexture(33984 + r1);
            GLES20.glBindTexture(3553, this.yuvTextures[r1]);
            GLES20.glPixelStorei(3317, 1);
            GLES20.glTexImage2D(3553, 0, 6409, r7[r1], r9, 0, 6409, 5121, r8[r1]);
            r1 = r1 + 1;
            goto L19
        L22:
            r9 = (r3.height + 1) / 2;
            goto L23
        L24:
            int r6 = (r11[0] + 1) / 2;
            int[] r11 = {r3.width, r6, r6};
            int r10 = 0;
        L26:
            if (r10 >= 3) goto L39;
            if (this.previousWidths[r10] != r11[r10]) goto L34;
            if (this.previousStrides[r10] != r7[r10]) goto L34;
            char r16 = r5;
        L38:
            r10 = r10 + 1;
            r5 = r16;
        L34:
            if (r7[r10] == 0) goto L36;
            boolean r14 = r5;
        L37:
            Assertions.checkState(r14);
            float r15 = r11[r10] / r7[r10];
            FloatBuffer[] r17 = this.textureCoords;
            r16 = r5;
            float[] r12 = new float[8];
            r12[0] = 0.0f;
            r12[r16] = 0.0f;
            r12[2] = 0.0f;
            r12[3] = 1.0f;
            r12[4] = r15;
            r12[5] = 0.0f;
            r12[6] = r15;
            r12[7] = 1.0f;
            r17[r10] = GlUtil.createBuffer(r12);
            GLES20.glVertexAttribPointer(this.texLocations[r10], 2, 5126, false, 0, this.textureCoords[r10]);
            this.previousWidths[r10] = r11[r10];
            this.previousStrides[r10] = r7[r10];
            goto L38
        L36:
            r14 = 0;
            goto L37
        L39:
            GLES20.glClear(16384);
            GLES20.glDrawArrays(5, 0, 4);
            GlUtil.checkGlError();     // Catch: GlUtil.GlException -> L42
            return;
        L42:
            e = move-exception;
            Log.e(VideoDecoderGLSurfaceView.TAG, "Failed to draw a frame", e);
            return;
        L15:
            r4 = kColorConversion2020;
            goto L17
        L16:
            r4 = kColorConversion601;
            goto L17
        L9:
            if (this.renderedOutputBuffer == null) goto L11;
            this.renderedOutputBuffer.release();
        L11:
            this.renderedOutputBuffer = r2;
            goto L12
        L5:
            if (this.renderedOutputBuffer != null) goto L7;
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceChanged(GL10 r2, int r3, int r4) {
            GLES20.glViewport(0, 0, r3, r4);
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceCreated(GL10 r8, EGLConfig r9) {
            this.program = new GlProgram(VERTEX_SHADER, FRAGMENT_SHADER);     // Catch: GlUtil.GlException -> L4
            GLES20.glVertexAttribPointer(this.program.getAttributeArrayLocationAndEnable("in_pos"), 2, 5126, false, 0, TEXTURE_VERTICES);     // Catch: GlUtil.GlException -> L4
            this.texLocations[0] = this.program.getAttributeArrayLocationAndEnable("in_tc_y");     // Catch: GlUtil.GlException -> L4
            this.texLocations[1] = this.program.getAttributeArrayLocationAndEnable("in_tc_u");     // Catch: GlUtil.GlException -> L4
            this.texLocations[2] = this.program.getAttributeArrayLocationAndEnable("in_tc_v");     // Catch: GlUtil.GlException -> L4
            this.colorMatrixLocation = this.program.getUniformLocation("mColorConversion");     // Catch: GlUtil.GlException -> L4
            GlUtil.checkGlError();     // Catch: GlUtil.GlException -> L4
            setupTextures();     // Catch: GlUtil.GlException -> L4
            GlUtil.checkGlError();     // Catch: GlUtil.GlException -> L4
            return;
        L4:
            e = move-exception;
            Log.e(VideoDecoderGLSurfaceView.TAG, "Failed to set up the textures and program", e);
        }

        public void setOutputBuffer(VideoDecoderOutputBuffer r3) {
            VideoDecoderOutputBuffer r0 = this.pendingOutputBufferReference.getAndSet(r3);
            if (r0 == null) goto L5;
            r0.release();
        L5:
            this.surfaceView.requestRender();
        }
    }

    public VideoDecoderGLSurfaceView(Context r2) {
        this(r2, null);
    }

    public VideoDecoderGLSurfaceView(Context r2, AttributeSet r3) {
        super(r2, r3);
        this.renderer = new Renderer(this);
        setPreserveEGLContextOnPause(true);
        setEGLContextClientVersion(2);
        setRenderer(this.renderer);
        setRenderMode(0);
    }

    @Deprecated
    public VideoDecoderOutputBufferRenderer getVideoDecoderOutputBufferRenderer() {
        return this;
    }

    @Override // androidx.media3.exoplayer.video.VideoDecoderOutputBufferRenderer
    public void setOutputBuffer(VideoDecoderOutputBuffer r2) {
        this.renderer.setOutputBuffer(r2);
    }
}
