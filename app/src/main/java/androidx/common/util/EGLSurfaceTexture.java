package androidx.media3.common.util;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Handler;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public final class EGLSurfaceTexture implements SurfaceTexture.OnFrameAvailableListener, Runnable {
    private static final int[] EGL_CONFIG_ATTRIBUTES = null;
    private static final int EGL_PROTECTED_CONTENT_EXT = 12992;
    private static final int EGL_SURFACE_HEIGHT = 1;
    private static final int EGL_SURFACE_WIDTH = 1;
    public static final int SECURE_MODE_NONE = 0;
    public static final int SECURE_MODE_PROTECTED_PBUFFER = 2;
    public static final int SECURE_MODE_SURFACELESS_CONTEXT = 1;
    private final TextureImageListener callback;
    private EGLContext context;
    private EGLDisplay display;
    private final Handler handler;
    private EGLSurface surface;
    private SurfaceTexture texture;
    private final int[] textureIdHolder;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface SecureMode {
    }

    public interface TextureImageListener {
        void onFrameAvailable();
    }

    static {
        EGL_CONFIG_ATTRIBUTES = new int[]{12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 0, 12327, 12344, 12339, 4, 12344};
    }

    public EGLSurfaceTexture(Handler r2) {
        this(r2, null);
    }

    public EGLSurfaceTexture(Handler r2, TextureImageListener r3) {
        this.handler = r2;
        this.callback = r3;
        this.textureIdHolder = new int[1];
    }

    private static EGLConfig chooseEGLConfig(EGLDisplay r10) throws GlUtil.GlException {
        EGLConfig[] r4 = new EGLConfig[1];
        int[] r7 = new int[1];
        boolean r11 = EGL14.eglChooseConfig(r10, EGL_CONFIG_ATTRIBUTES, 0, r4, 0, 1, r7, 0);
        if (r11 == true) goto L5;
    L9:
        boolean r3 = false;
    L10:
        GlUtil.checkGlException(r3, Util.formatInvariant("eglChooseConfig failed: success=%b, numConfigs[0]=%d, configs[0]=%s", new Object[]{Boolean.valueOf(r11), Integer.valueOf(r7[0]), r4[0]}));
        return r4[0];
    L5:
        if (r7[0] <= 0) goto L9;
        if (r4[0] == null) goto L9;
        r3 = true;
        goto L10
    }

    private static EGLContext createEGLContext(EGLDisplay r5, EGLConfig r6, int r7) throws GlUtil.GlException {
        boolean r1 = true;
        if (r7 != 0) goto L5;
        int[] r0 = {12440, 2, 12344};
    L6:
        EGLContext r2 = EGL14.eglCreateContext(r5, r6, EGL14.EGL_NO_CONTEXT, r0, 0);
        if (r2 != null) goto L10;
        r1 = false;
    L10:
        GlUtil.checkGlException(r1, "eglCreateContext failed");
        return r2;
    L5:
        r0 = new int[]{12440, 2, EGL_PROTECTED_CONTENT_EXT, 1, 12344};
        goto L6
    }

    private static EGLSurface createEGLSurface(EGLDisplay r4, EGLConfig r5, EGLContext r6, int r7) throws GlUtil.GlException {
        boolean r0 = true;
        if (r7 != 1) goto L6;
        EGLSurface r1 = EGL14.EGL_NO_SURFACE;
    L14:
        GlUtil.checkGlException(EGL14.eglMakeCurrent(r4, r1, r1, r6), "eglMakeCurrent failed");
        return r1;
    L6:
        if (r7 != 2) goto L8;
        int[] r2 = {12375, 1, 12374, 1, EGL_PROTECTED_CONTENT_EXT, 1, 12344};
    L9:
        EGLSurface r3 = EGL14.eglCreatePbufferSurface(r4, r5, r2, 0);
        if (r3 != null) goto L13;
        r0 = false;
    L13:
        GlUtil.checkGlException(r0, "eglCreatePbufferSurface failed");
        r1 = r3;
        goto L14
    L8:
        r2 = new int[]{12375, 1, 12374, 1, 12344};
        goto L9
    }

    private void dispatchOnFrameAvailable() {
        if (this.callback == null) goto L6;
        this.callback.onFrameAvailable();
        return;
    }

    private static void generateTextureIds(int[] r2) throws GlUtil.GlException {
        GLES20.glGenTextures(1, r2, 0);
        GlUtil.checkGlError();
    }

    private static EGLDisplay getDefaultDisplay() throws GlUtil.GlException {
        EGLDisplay r1 = EGL14.eglGetDisplay(0);
        if (r1 == null) goto L5;
        boolean r3 = true;
    L6:
        GlUtil.checkGlException(r3, "eglGetDisplay failed");
        int[] r4 = new int[2];
        GlUtil.checkGlException(EGL14.eglInitialize(r1, r4, 0, r4, 1), "eglInitialize failed");
        return r1;
    L5:
        r3 = false;
        goto L6
    }

    public SurfaceTexture getSurfaceTexture() {
        return (SurfaceTexture) Assertions.checkNotNull(this.texture);
    }

    public void init(int r5) throws GlUtil.GlException {
        this.display = getDefaultDisplay();
        EGLConfig r0 = chooseEGLConfig(this.display);
        this.context = createEGLContext(this.display, r0, r5);
        this.surface = createEGLSurface(this.display, r0, this.context, r5);
        generateTextureIds(this.textureIdHolder);
        this.texture = new SurfaceTexture(this.textureIdHolder[0]);
        this.texture.setOnFrameAvailableListener(this);
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture r2) {
        this.handler.post(this);
    }

    public void release() {
        this.handler.removeCallbacks(this);
    L26:
        th = move-exception;
        if (this.display == null) goto L33;
        if (this.display.equals(EGL14.EGL_NO_DISPLAY) == true) goto L33;
        EGL14.eglMakeCurrent(this.display, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
    L33:
        if (this.surface == null) goto L38;
        if (this.surface.equals(EGL14.EGL_NO_SURFACE) == true) goto L38;
        EGL14.eglDestroySurface(this.display, this.surface);
    L38:
        if (this.context == null) goto L40;
        EGL14.eglDestroyContext(this.display, this.context);
    L40:
        EGL14.eglReleaseThread();
        if (this.display != null) goto L43;
    L45:
        this.display = null;
        this.context = null;
        this.surface = null;
        this.texture = null;
        throw th;
    L43:
        if (this.display.equals(EGL14.EGL_NO_DISPLAY) == true) goto L45;
        EGL14.eglTerminate(this.display);
        goto L45
    L4:
        if (this.texture == null) goto L7;
        this.texture.release();     // Catch: Throwable -> L26
        GLES20.glDeleteTextures(1, this.textureIdHolder, 0);     // Catch: Throwable -> L26
    L7:
        if (this.display == null) goto L12;
        if (this.display.equals(EGL14.EGL_NO_DISPLAY) == true) goto L12;
        EGL14.eglMakeCurrent(this.display, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
    L12:
        if (this.surface == null) goto L17;
        if (this.surface.equals(EGL14.EGL_NO_SURFACE) == true) goto L17;
        EGL14.eglDestroySurface(this.display, this.surface);
    L17:
        if (this.context == null) goto L19;
        EGL14.eglDestroyContext(this.display, this.context);
    L19:
        EGL14.eglReleaseThread();
        if (this.display != null) goto L22;
    L24:
        this.display = null;
        this.context = null;
        this.surface = null;
        this.texture = null;
        return;
    L22:
        if (this.display.equals(EGL14.EGL_NO_DISPLAY) == true) goto L24;
        EGL14.eglTerminate(this.display);
        goto L24
    }

    @Override // java.lang.Runnable
    public void run() {
        dispatchOnFrameAvailable();
        if (this.texture == null) goto L10;
        this.texture.updateTexImage();     // Catch: RuntimeException -> L6
        return;
    L7:
        return;
    }
}
