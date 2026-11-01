package androidx.media3.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class GlUtil {
    public static final int[] EGL_CONFIG_ATTRIBUTES_RGBA_1010102 = null;
    public static final int[] EGL_CONFIG_ATTRIBUTES_RGBA_8888 = null;
    private static final int EGL_GL_COLORSPACE_BT2020_HLG_EXT = 13632;
    private static final int EGL_GL_COLORSPACE_BT2020_PQ_EXT = 13120;
    private static final int EGL_GL_COLORSPACE_KHR = 12445;
    private static final int[] EGL_WINDOW_SURFACE_ATTRIBUTES_BT2020_HLG = null;
    private static final int[] EGL_WINDOW_SURFACE_ATTRIBUTES_BT2020_PQ = null;
    private static final int[] EGL_WINDOW_SURFACE_ATTRIBUTES_NONE = null;
    private static final String EXTENSION_COLORSPACE_BT2020_HLG = "EGL_EXT_gl_colorspace_bt2020_hlg";
    private static final String EXTENSION_COLORSPACE_BT2020_PQ = "EGL_EXT_gl_colorspace_bt2020_pq";
    private static final String EXTENSION_PROTECTED_CONTENT = "EGL_EXT_protected_content";
    private static final String EXTENSION_SURFACELESS_CONTEXT = "EGL_KHR_surfaceless_context";
    private static final String EXTENSION_YUV_TARGET = "GL_EXT_YUV_target";
    private static final long GL_FENCE_SYNC_FAILED = 0;
    public static final int HOMOGENEOUS_COORDINATE_VECTOR_SIZE = 4;
    public static final float LENGTH_NDC = 2.0f;

    public static final class GlException extends Exception {
        public GlException(String r1) {
            super(r1);
        }
    }

    static {
        EGL_CONFIG_ATTRIBUTES_RGBA_8888 = new int[]{12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 0, 12326, 0, 12344};
        EGL_CONFIG_ATTRIBUTES_RGBA_1010102 = new int[]{12352, 4, 12324, 10, 12323, 10, 12322, 10, 12321, 2, 12325, 0, 12326, 0, 12344};
        EGL_WINDOW_SURFACE_ATTRIBUTES_BT2020_PQ = new int[]{EGL_GL_COLORSPACE_KHR, EGL_GL_COLORSPACE_BT2020_PQ_EXT, 12344, 12344};
        EGL_WINDOW_SURFACE_ATTRIBUTES_BT2020_HLG = new int[]{EGL_GL_COLORSPACE_KHR, EGL_GL_COLORSPACE_BT2020_HLG_EXT, 12344, 12344};
        EGL_WINDOW_SURFACE_ATTRIBUTES_NONE = new int[]{12344};
    }

    private GlUtil() {
    }

    private static void assertValidTextureSize(int r5, int r6) throws GlException {
        boolean r0 = true;
        int[] r1 = new int[1];
        GLES20.glGetIntegerv(3379, r1, 0);
        int r2 = r1[0];
        if (r2 > 0) goto L6;
        r0 = false;
    L6:
        Assertions.checkState(r0, "Create a OpenGL context first or run the GL methods on an OpenGL thread.");
        if (r5 < 0) goto L15;
        if (r6 < 0) goto L15;
        if (r5 > r2) goto L13;
        if (r6 > r2) goto L13;
        return;
    L13:
        throw new GlException("width or height is greater than GL_MAX_TEXTURE_SIZE " + r2);
    L15:
        throw new GlException("width or height is less than 0");
    }

    public static void awaitSyncObject(long r3) throws GlException {
        if (r3 != 0) goto L5;
        GLES20.glFinish();
        return;
    L5:
        GLES30.glWaitSync(r3, 0, -1);
        checkGlError();
    }

    public static void bindTexture(int r2, int r3, int r4) throws GlException {
        GLES20.glBindTexture(r2, r3);
        checkGlError();
        GLES20.glTexParameteri(r2, 10240, r4);
        checkGlError();
        GLES20.glTexParameteri(r2, 10241, r4);
        checkGlError();
        GLES20.glTexParameteri(r2, 10242, 33071);
        checkGlError();
        GLES20.glTexParameteri(r2, 10243, 33071);
        checkGlError();
    }

    public static void blitFrameBuffer(int r17, GlRect r18, int r19, GlRect r20) throws GlException {
        int[] r2 = new int[1];
        GLES20.glGetIntegerv(36006, r2, 0);
        checkGlError();
        GLES30.glBindFramebuffer(36008, r17);
        checkGlError();
        GLES30.glBindFramebuffer(36009, r19);
        checkGlError();
        GLES30.glBlitFramebuffer(r18.left, r18.bottom, r18.right, r18.top, r20.left, r20.bottom, r20.right, r20.top, 16384, 9729);
        checkGlError();
        GLES20.glBindFramebuffer(36160, r2[0]);
        checkGlError();
    }

    private static void checkEglException(String r4) throws GlException {
        int r0 = EGL14.eglGetError();
        if (r0 != 12288) goto L6;
        return;
    L6:
        throw new GlException(r4 + ", error code: 0x" + Integer.toHexString(r0));
    }

    public static void checkGlError() throws GlException {
        StringBuilder r0 = new StringBuilder();
        boolean r1 = false;
    L3:
        int r2 = GLES20.glGetError();
        if (r2 == 0) goto L11;
        if (r1 == false) goto L7;
        r0.append('\n');
    L7:
        String r3 = GLU.gluErrorString(r2);
        if (r3 != null) goto L10;
        r3 = "error code: 0x" + Integer.toHexString(r2);
    L10:
        r0.append("glError: ").append(r3);
        r1 = true;
        goto L3
    L11:
        if (r1 == true) goto L14;
        return;
    L14:
        throw new GlException(r0.toString());
    }

    public static void checkGlException(boolean r1, String r2) throws GlException {
        if (r1 == false) goto L5;
        return;
    L5:
        throw new GlException(r2);
    }

    public static void clearFocusedBuffers() throws GlException {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(16640);
        checkGlError();
    }

    public static float[] create4x4IdentityMatrix() {
        float[] r0 = new float[16];
        setToIdentity(r0);
        return r0;
    }

    private static FloatBuffer createBuffer(int r2) {
        return ByteBuffer.allocateDirect(r2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public static FloatBuffer createBuffer(float[] r1) {
        return (FloatBuffer) createBuffer(r1.length).put(r1).flip();
    }

    public static EGLContext createEglContext(EGLContext r5, EGLDisplay r6, int r7, int[] r8) throws GlException {
        boolean r1 = true;
        if (Arrays.equals(r8, EGL_CONFIG_ATTRIBUTES_RGBA_8888) == false) goto L5;
    L8:
        boolean r0 = true;
    L9:
        Assertions.checkArgument(r0);
        if (r7 != 2) goto L12;
    L15:
        Assertions.checkArgument(r1);
        EGLContext r2 = EGL14.eglCreateContext(r6, getEglConfig(r6, r8), r5, new int[]{12440, r7, 12344}, 0);
        if (r2 != null) goto L18;
    L21:
        EGL14.eglTerminate(r6);
        throw new GlException("eglCreateContext() failed to create a valid context. The device may not support EGL version " + r7);
    L18:
        if (r2.equals(EGL14.EGL_NO_CONTEXT) == true) goto L21;
        checkGlError();
        return r2;
    L12:
        if (r7 == 3) goto L15;
        r1 = false;
        goto L15
    L5:
        if (Arrays.equals(r8, EGL_CONFIG_ATTRIBUTES_RGBA_1010102) == true) goto L8;
        r0 = false;
        goto L9
    }

    public static EGLContext createEglContext(EGLDisplay r3) throws GlException {
        return createEglContext(EGL14.EGL_NO_CONTEXT, r3, 2, EGL_CONFIG_ATTRIBUTES_RGBA_8888);
    }

    public static EGLSurface createEglSurface(EGLDisplay r4, Object r5, int r6, boolean r7) throws GlException {
        if (r6 != 3) goto L5;
    L27:
        int[] r0 = EGL_CONFIG_ATTRIBUTES_RGBA_8888;
        int[] r1 = EGL_WINDOW_SURFACE_ATTRIBUTES_NONE;
    L28:
        EGLSurface r2 = EGL14.eglCreateWindowSurface(r4, getEglConfig(r4, r0), r5, r1, 0);
        checkEglException("Error creating a new EGL surface");
        return r2;
    L5:
        if (r6 == 10) goto L27;
        if (r6 == 7) goto L13;
        if (r6 == 6) goto L13;
        throw new IllegalArgumentException("Unsupported color transfer: " + r6);
    L13:
        r0 = EGL_CONFIG_ATTRIBUTES_RGBA_1010102;
        if (r7 == false) goto L16;
        r1 = EGL_WINDOW_SURFACE_ATTRIBUTES_NONE;
        goto L28
    L16:
        if (r6 != 6) goto L23;
        if (isBt2020PqExtensionSupported() == false) goto L21;
        r1 = EGL_WINDOW_SURFACE_ATTRIBUTES_BT2020_PQ;
        goto L28
    L21:
        throw new GlException("BT.2020 PQ OpenGL output isn't supported.");
    L23:
        if (isBt2020HlgExtensionSupported() == false) goto L26;
        r1 = EGL_WINDOW_SURFACE_ATTRIBUTES_BT2020_HLG;
        goto L28
    L26:
        throw new GlException("BT.2020 HLG OpenGL output isn't supported.");
    }

    public static int createExternalTexture() throws GlException {
        int r0 = generateTexture();
        bindTexture(36197, r0, 9729);
        return r0;
    }

    public static int createFboForTexture(int r5) throws GlException {
        int[] r1 = new int[1];
        GLES20.glGenFramebuffers(1, r1, 0);
        checkGlError();
        GLES20.glBindFramebuffer(36160, r1[0]);
        checkGlError();
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, r5, 0);
        checkGlError();
        return r1[0];
    }

    public static EGLSurface createFocusedPlaceholderEglSurface(EGLContext r3, EGLDisplay r4) throws GlException {
        int[] r0 = EGL_CONFIG_ATTRIBUTES_RGBA_8888;
        if (isSurfacelessContextExtensionSupported() == false) goto L5;
        EGLSurface r1 = EGL14.EGL_NO_SURFACE;
    L6:
        focusEglSurface(r4, r3, r1, 1, 1);
        return r1;
    L5:
        r1 = createPbufferSurface(r4, 1, 1, r0);
        goto L6
    }

    public static long createGlSyncFence() throws GlException {
        if (getContextMajorVersion() < 3) goto L6;
        long r0 = GLES30.glFenceSync(37143, 0);
        checkGlError();
        GLES20.glFlush();
        checkGlError();
        return r0;
    L6:
        return 0;
    }

    private static EGLSurface createPbufferSurface(EGLDisplay r3, int r4, int r5, int[] r6) throws GlException {
        EGLConfig r1 = getEglConfig(r3, r6);
        EGLSurface r2 = EGL14.eglCreatePbufferSurface(r3, r1, new int[]{12375, r4, 12374, r5, 12344}, 0);
        checkEglException("Error creating a new EGL Pbuffer surface");
        return r2;
    }

    public static int createPixelBufferObject(int r5) throws GlException {
        int[] r1 = new int[1];
        GLES30.glGenBuffers(1, r1, 0);
        checkGlError();
        GLES30.glBindBuffer(35051, r1[0]);
        checkGlError();
        GLES30.glBufferData(35051, r5, null, 35049);
        checkGlError();
        GLES30.glBindBuffer(35051, 0);
        checkGlError();
        return r1[0];
    }

    public static int createTexture(int r2, int r3, boolean r4) throws GlException {
        if (r4 == false) goto L6;
        return createTextureUninitialized(r2, r3, 34842, 5131);
    L6:
        return createTextureUninitialized(r2, r3, 6408, 5121);
    }

    public static int createTexture(Bitmap r1) throws GlException {
        int r0 = generateTexture();
        setTexture(r0, r1);
        return r0;
    }

    private static int createTextureUninitialized(int r12, int r13, int r14, int r15) throws GlException {
        assertValidTextureSize(r12, r13);
        int r0 = generateTexture();
        bindTexture(3553, r0, 9729);
        GLES20.glTexImage2D(3553, 0, r14, r12, r13, 0, 6408, r15, null);
        checkGlError();
        return r0;
    }

    public static float[] createVertexBuffer(List<float[]> r6) {
        float[] r0 = new float[r6.size() * 4];
        int r2 = 0;
    L4:
        if (r2 >= r6.size()) goto L6;
        System.arraycopy(r6.get(r2), 0, r0, r2 * 4, 4);
        r2 = r2 + 1;
        goto L4
    L6:
        return r0;
    }

    public static void deleteBuffer(int r3) throws GlException {
        GLES20.glDeleteBuffers(1, new int[]{r3}, 0);
        checkGlError();
    }

    public static void deleteFbo(int r3) throws GlException {
        GLES20.glDeleteFramebuffers(1, new int[]{r3}, 0);
        checkGlError();
    }

    public static void deleteRbo(int r3) throws GlException {
        GLES20.glDeleteRenderbuffers(1, new int[]{r3}, 0);
        checkGlError();
    }

    public static void deleteSyncObject(long r0) throws GlException {
        deleteSyncObjectQuietly(r0);
        checkGlError();
    }

    public static void deleteSyncObjectQuietly(long r0) {
        GLES30.glDeleteSync(r0);
    }

    public static void deleteTexture(int r3) throws GlException {
        GLES20.glDeleteTextures(1, new int[]{r3}, 0);
        checkGlError();
    }

    public static void destroyEglContext(EGLDisplay r3, EGLContext r4) throws GlException {
        if (r3 != null) goto L4;
        return;
    L4:
        if (r3.equals(EGL14.EGL_NO_DISPLAY) == true) goto L14;
        EGL14.eglMakeCurrent(r3, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        checkEglException("Error releasing context");
        if (r4 != null) goto L9;
    L11:
        EGL14.eglReleaseThread();
        checkEglException("Error releasing thread");
        EGL14.eglTerminate(r3);
        checkEglException("Error terminating display");
        return;
    L9:
        if (r4.equals(EGL14.EGL_NO_CONTEXT) == true) goto L11;
        EGL14.eglDestroyContext(r3, r4);
        checkEglException("Error destroying context");
        goto L11
    }

    public static void destroyEglSurface(EGLDisplay r1, EGLSurface r2) throws GlException {
        if (r1 != null) goto L4;
        return;
    L4:
        if (r1.equals(EGL14.EGL_NO_DISPLAY) == true) goto L15;
        if (r2 != null) goto L8;
        return;
    L8:
        if (r2.equals(EGL14.EGL_NO_SURFACE) == true) goto L14;
        EGL14.eglDestroySurface(r1, r2);
        checkEglException("Error destroying surface");
        return;
    L14:
        return;
    }

    public static void focusEglSurface(EGLDisplay r6, EGLContext r7, EGLSurface r8, int r9, int r10) throws GlException {
        focusRenderTarget(r6, r7, r8, 0, r9, r10);
    }

    public static void focusFramebuffer(EGLDisplay r0, EGLContext r1, EGLSurface r2, int r3, int r4, int r5) throws GlException {
        focusRenderTarget(r0, r1, r2, r3, r4, r5);
    }

    public static void focusFramebufferUsingCurrentContext(int r3, int r4, int r5) throws GlException {
        int[] r0 = new int[1];
        GLES20.glGetIntegerv(36006, r0, 0);
        if (r0[0] == r3) goto L5;
        GLES20.glBindFramebuffer(36160, r3);
    L5:
        checkGlError();
        GLES20.glViewport(0, 0, r4, r5);
        checkGlError();
    }

    private static void focusRenderTarget(EGLDisplay r1, EGLContext r2, EGLSurface r3, int r4, int r5, int r6) throws GlException {
        EGL14.eglMakeCurrent(r1, r3, r3, r2);
        checkEglException("Error making context current");
        focusFramebufferUsingCurrentContext(r4, r5, r6);
    }

    public static int generateTexture() throws GlException {
        int[] r1 = new int[1];
        GLES20.glGenTextures(1, r1, 0);
        checkGlError();
        return r1[0];
    }

    public static long getContextMajorVersion() throws GlException {
        int[] r0 = new int[1];
        EGL14.eglQueryContext(EGL14.eglGetDisplay(0), EGL14.eglGetCurrentContext(), 12440, r0, 0);
        checkGlError();
        return r0[0];
    }

    public static EGLContext getCurrentContext() {
        return EGL14.eglGetCurrentContext();
    }

    public static EGLDisplay getDefaultEglDisplay() throws GlException {
        EGLDisplay r1 = EGL14.eglGetDisplay(0);
        checkGlException(!r1.equals(EGL14.EGL_NO_DISPLAY), "No EGL display.");
        checkGlException(EGL14.eglInitialize(r1, new int[1], 0, new int[1], 0), "Error in eglInitialize.");
        checkGlError();
        return r1;
    }

    private static EGLConfig getEglConfig(EGLDisplay r9, int[] r10) throws GlException {
        EGLConfig[] r4 = new EGLConfig[1];
        if (EGL14.eglChooseConfig(r9, r10, 0, r4, 0, 1, new int[1], 0) == false) goto L7;
        return r4[0];
    L7:
        throw new GlException("eglChooseConfig failed.");
    }

    public static float[] getNormalizedCoordinateBounds() {
        return new float[]{-1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f};
    }

    public static float[] getTextureCoordinateBounds() {
        return new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f};
    }

    public static boolean isBt2020HlgExtensionSupported() {
        return isExtensionSupported(EXTENSION_COLORSPACE_BT2020_HLG);
    }

    public static boolean isBt2020PqExtensionSupported() {
        if (Util.SDK_INT >= 33) goto L5;
    L7:
        return false;
    L5:
        if (isExtensionSupported(EXTENSION_COLORSPACE_BT2020_PQ) == false) goto L7;
        return true;
    }

    private static boolean isExtensionSupported(String r4) {
        String r2 = EGL14.eglQueryString(EGL14.eglGetDisplay(0), 12373);
        if (r2 != null) goto L5;
        return false;
    L5:
        if (r2.contains(r4) == false) goto L9;
        return true;
    L9:
        return false;
    }

    public static boolean isProtectedContentExtensionSupported(Context r4) {
        if (Util.SDK_INT >= 24) goto L6;
        return false;
    L6:
        if (Util.SDK_INT >= 26) goto L13;
        if ("samsung".equals(Util.MANUFACTURER) == false) goto L10;
    L11:
        return false;
    L10:
        if ("XT1650".equals(Util.MODEL) == true) goto L11;
    L13:
        if (Util.SDK_INT >= 26) goto L18;
        if (r4.getPackageManager().hasSystemFeature("android.hardware.vr.high_performance") == true) goto L18;
        return false;
    L18:
        return isExtensionSupported(EXTENSION_PROTECTED_CONTENT);
    }

    public static boolean isSurfacelessContextExtensionSupported() {
        return isExtensionSupported(EXTENSION_SURFACELESS_CONTEXT);
    }

    public static boolean isYuvTargetExtensionSupported() {
        if (Util.areEqual(EGL14.eglGetCurrentContext(), EGL14.EGL_NO_CONTEXT) == true) goto L14;
        String r2 = GLES20.glGetString(7939);
    L9:
        if (r2 != null) goto L11;
        return false;
    L11:
        if (r2.contains(EXTENSION_YUV_TARGET) == false) goto L17;
        return true;
    L17:
        return false;
    L14:
        EGLDisplay r0 = getDefaultEglDisplay();     // Catch: GlException -> L6
        EGLContext r3 = createEglContext(r0);     // Catch: GlException -> L6
        createFocusedPlaceholderEglSurface(r3, r0);     // Catch: GlException -> L6
        r2 = GLES20.glGetString(7939);     // Catch: GlException -> L6
        destroyEglContext(r0, r3);     // Catch: GlException -> L6
    L7:
        return false;
    }

    public static ByteBuffer mapPixelBufferObject(int r3, int r4) throws GlException {
        GLES20.glBindBuffer(35051, r3);
        checkGlError();
        ByteBuffer r2 = (ByteBuffer) GLES30.glMapBufferRange(35051, 0, r4, 1);
        checkGlError();
        GLES30.glBindBuffer(35051, 0);
        checkGlError();
        return r2;
    }

    public static void schedulePixelBufferRead(int r9, int r10, int r11, int r12) throws GlException {
        focusFramebufferUsingCurrentContext(r9, r10, r11);
        GLES30.glBindBuffer(35051, r12);
        checkGlError();
        GLES30.glReadBuffer(36064);
        GLES30.glReadPixels(0, 0, r10, r11, 6408, 5121, 0);
        checkGlError();
        GLES30.glBindBuffer(35051, 0);
        checkGlError();
    }

    public static void setTexture(int r2, Bitmap r3) throws GlException {
        assertValidTextureSize(r3.getWidth(), r3.getHeight());
        bindTexture(3553, r2, 9729);
        GLUtils.texImage2D(3553, 0, r3, 0);
        checkGlError();
    }

    public static void setToIdentity(float[] r1) {
        Matrix.setIdentityM(r1, 0);
    }

    public static void unmapPixelBufferObject(int r2) throws GlException {
        GLES30.glBindBuffer(35051, r2);
        checkGlError();
        GLES30.glUnmapBuffer(35051);
        checkGlError();
        GLES30.glBindBuffer(35051, 0);
        checkGlError();
    }
}
