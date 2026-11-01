package androidx.media3.exoplayer.video;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Surface;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.EGLSurfaceTexture;
import androidx.media3.common.util.GlUtil;
import androidx.media3.common.util.Log;

/* JADX INFO: loaded from: classes.dex */
public final class PlaceholderSurface extends Surface {
    private static final String TAG = "PlaceholderSurface";
    private static int secureMode;
    private static boolean secureModeInitialized;
    public final boolean secure;
    private final PlaceholderSurfaceThread thread;
    private boolean threadReleased;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.video.PlaceholderSurface$1 */
    static /* synthetic */ class C03831 {
    }

    private static class PlaceholderSurfaceThread extends HandlerThread implements Handler.Callback {
        private static final int MSG_INIT = 1;
        private static final int MSG_RELEASE = 2;
        private EGLSurfaceTexture eglSurfaceTexture;
        private Handler handler;
        private Error initError;
        private RuntimeException initException;
        private PlaceholderSurface surface;

        public PlaceholderSurfaceThread() {
            super("ExoPlayer:PlaceholderSurface");
        }

        private void initInternal(int r5) throws GlUtil.GlException {
            Assertions.checkNotNull(this.eglSurfaceTexture);
            this.eglSurfaceTexture.init(r5);
            SurfaceTexture r1 = this.eglSurfaceTexture.getSurfaceTexture();
            if (r5 == 0) goto L5;
            boolean r2 = true;
        L6:
            this.surface = new PlaceholderSurface(this, r1, r2, null);
            return;
        L5:
            r2 = false;
            goto L6
        }

        private void releaseInternal() {
            Assertions.checkNotNull(this.eglSurfaceTexture);
            this.eglSurfaceTexture.release();
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message r5) {
            switch(r5.what) {
                case 1: goto L75;
                case 2: goto L73;
                default: goto L4;
            };
        L4:
            return true;
        L73:
            releaseInternal();     // Catch: Throwable -> L7
        L6:
            quit();
            return true;
        L7:
            th = move-exception;
            Log.m45e(PlaceholderSurface.TAG, "Failed to release placeholder surface", th);     // Catch: Throwable -> L11
        L11:
            th = move-exception;
            quit();
            throw th;
        L75:
            initInternal(r5.arg1);     // Catch: Throwable -> L22 Error -> L24 GlUtil.GlException -> L33 RuntimeException -> L42
            monitor-enter(this);
            notify();     // Catch: Throwable -> L19
            monitor-exit(this);     // Catch: Throwable -> L19
        L18:
            return true;
        L19:
            th = move-exception;
            throw th;
        L33:
            e = move-exception;
            Log.m45e(PlaceholderSurface.TAG, "Failed to initialize placeholder surface", e);     // Catch: Throwable -> L22
            this.initException = new IllegalStateException(e);     // Catch: Throwable -> L22
            monitor-enter(this);
            notify();     // Catch: Throwable -> L39
        L39:
            th = move-exception;
            throw th;
        L24:
            e = move-exception;
            Log.m45e(PlaceholderSurface.TAG, "Failed to initialize placeholder surface", e);     // Catch: Throwable -> L22
            this.initError = e;     // Catch: Throwable -> L22
            monitor-enter(this);
            notify();     // Catch: Throwable -> L30
        L30:
            th = move-exception;
            throw th;
        L42:
            e = move-exception;
            Log.m45e(PlaceholderSurface.TAG, "Failed to initialize placeholder surface", e);     // Catch: Throwable -> L22
            this.initException = e;     // Catch: Throwable -> L22
            monitor-enter(this);
            notify();     // Catch: Throwable -> L49
        L49:
            th = move-exception;
            throw th;
        L22:
            th = move-exception;
            monitor-enter(this);
            notify();     // Catch: Throwable -> L56
            throw th;
        L56:
            th = move-exception;
            throw th;
        }

        public PlaceholderSurface init(int r5) {
            start();
            this.handler = new Handler(getLooper(), this);
            this.eglSurfaceTexture = new EGLSurfaceTexture(this.handler);
            boolean r0 = false;
            monitor-enter(this);
            this.handler.obtainMessage(1, r5, 0).sendToTarget();     // Catch: Throwable -> L28
        L6:
            if (this.surface != null) goto L15;
            if (this.initException != null) goto L15;
            if (this.initError != null) goto L15;
            wait();     // Catch: InterruptedException -> L13 Throwable -> L28
        L14:
            r0 = true;
        L15:
            monitor-exit(this);     // Catch: Throwable -> L28
            if (r0 == false) goto L19;
            Thread.currentThread().interrupt();
        L19:
            if (this.initException != null) goto L27;
            if (this.initError != null) goto L25;
            return (PlaceholderSurface) Assertions.checkNotNull(this.surface);
        L25:
            throw this.initError;
        L27:
            throw this.initException;
        L28:
            th = move-exception;
            throw th;
        }

        public void release() {
            Assertions.checkNotNull(this.handler);
            this.handler.sendEmptyMessage(2);
        }
    }

    private PlaceholderSurface(PlaceholderSurfaceThread r1, SurfaceTexture r2, boolean r3) {
        super(r2);
        this.thread = r1;
        this.secure = r3;
    }

    /* synthetic */ PlaceholderSurface(PlaceholderSurfaceThread r1, SurfaceTexture r2, boolean r3, C03831 r4) {
        this(r1, r2, r3);
    }

    private static int getSecureMode(Context r1) {
        if (GlUtil.isProtectedContentExtensionSupported(r1) == true) goto L5;
        return 0;
    L5:
        if (GlUtil.isSurfacelessContextExtensionSupported() == false) goto L8;
        return 1;
    L8:
        return 2;
    }

    public static synchronized boolean isSecureSupported(Context r3) {
        monitor-enter(PlaceholderSurface.class);
        boolean r2 = true;
        if (secureModeInitialized == true) goto L7;
        secureMode = getSecureMode(r3);     // Catch: Throwable -> L13
        secureModeInitialized = true;     // Catch: Throwable -> L13
    L7:
        if (secureMode != 0) goto L11;
        r2 = false;
    L11:
        monitor-exit(PlaceholderSurface.class);
        return r2;
    L13:
        th = move-exception;
        throw th;
    }

    public static PlaceholderSurface newInstance(Context r2, boolean r3) {
        int r0 = 0;
        if (r3 == true) goto L5;
    L8:
        boolean r1 = true;
    L9:
        Assertions.checkState(r1);
        PlaceholderSurfaceThread r4 = new PlaceholderSurfaceThread();
        if (r3 == false) goto L13;
        r0 = secureMode;
    L13:
        return r4.init(r0);
    L5:
        if (isSecureSupported(r2) == true) goto L8;
        r1 = false;
        goto L9
    }

    @Deprecated
    public static PlaceholderSurface newInstanceV17(Context r1, boolean r2) {
        return newInstance(r1, r2);
    }

    @Override // android.view.Surface
    public void release() {
        super.release();
        PlaceholderSurfaceThread r0 = this.thread;
        monitor-enter(r0);
    L9:
        th = move-exception;
        throw th;
    L5:
        if (this.threadReleased == true) goto L7;
        this.thread.release();     // Catch: Throwable -> L9
        this.threadReleased = true;     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
    }
}
