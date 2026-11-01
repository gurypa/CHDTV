package androidx.media3.exoplayer.video.spherical;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.WindowManager;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.GlUtil;
import androidx.media3.exoplayer.video.VideoFrameMetadataListener;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: loaded from: classes.dex */
public final class SphericalGLSurfaceView extends GLSurfaceView {
    private static final int FIELD_OF_VIEW_DEGREES = 90;
    private static final float PX_PER_DEGREES = 25.0f;
    static final float UPRIGHT_ROLL = 3.1415927f;
    private static final float Z_FAR = 100.0f;
    private static final float Z_NEAR = 0.1f;
    private boolean isOrientationListenerRegistered;
    private boolean isStarted;
    private final Handler mainHandler;
    private final OrientationListener orientationListener;
    private final Sensor orientationSensor;
    private final SceneRenderer scene;
    private final SensorManager sensorManager;
    private Surface surface;
    private SurfaceTexture surfaceTexture;
    private final TouchTracker touchTracker;
    private boolean useSensorRotation;
    private final CopyOnWriteArrayList<VideoSurfaceListener> videoSurfaceListeners;

    final class Renderer implements GLSurfaceView.Renderer, TouchTracker.Listener, OrientationListener.Listener {
        private final float[] deviceOrientationMatrix;
        private float deviceRoll;
        private final float[] projectionMatrix;
        private final SceneRenderer scene;
        private final float[] tempMatrix;
        final /* synthetic */ SphericalGLSurfaceView this$0;
        private float touchPitch;
        private final float[] touchPitchMatrix;
        private final float[] touchYawMatrix;
        private final float[] viewMatrix;
        private final float[] viewProjectionMatrix;

        public Renderer(SphericalGLSurfaceView r3, SceneRenderer r4) {
            this.this$0 = r3;
            this.projectionMatrix = new float[16];
            this.viewProjectionMatrix = new float[16];
            this.deviceOrientationMatrix = new float[16];
            this.touchPitchMatrix = new float[16];
            this.touchYawMatrix = new float[16];
            this.viewMatrix = new float[16];
            this.tempMatrix = new float[16];
            this.scene = r4;
            GlUtil.setToIdentity(this.deviceOrientationMatrix);
            GlUtil.setToIdentity(this.touchPitchMatrix);
            GlUtil.setToIdentity(this.touchYawMatrix);
            this.deviceRoll = SphericalGLSurfaceView.UPRIGHT_ROLL;
        }

        private float calculateFieldOfViewInYDirection(float r10) {
            if (r10 <= 1.0f) goto L5;
            boolean r0 = true;
        L6:
            if (r0 == true) goto L8;
            return 90.0f;
        L8:
            return (float) (2.0d * Math.toDegrees(Math.atan(Math.tan(Math.toRadians(45.0d)) / ((double) r10))));
        L5:
            r0 = false;
            goto L6
        }

        private void updatePitchMatrix() {
            Matrix.setRotateM(this.touchPitchMatrix, 0, -this.touchPitch, (float) Math.cos(this.deviceRoll), (float) Math.sin(this.deviceRoll), 0.0f);
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onDrawFrame(GL10 r13) {
            monitor-enter(this);
            Matrix.multiplyMM(this.tempMatrix, 0, this.deviceOrientationMatrix, 0, this.touchYawMatrix, 0);     // Catch: Throwable -> L7
            Matrix.multiplyMM(this.viewMatrix, 0, this.touchPitchMatrix, 0, this.tempMatrix, 0);     // Catch: Throwable -> L7
            monitor-exit(this);     // Catch: Throwable -> L7
            Matrix.multiplyMM(this.viewProjectionMatrix, 0, this.projectionMatrix, 0, this.viewMatrix, 0);
            this.scene.drawFrame(this.viewProjectionMatrix, false);
            return;
        L7:
            th = move-exception;
            throw th;
        }

        @Override // androidx.media3.exoplayer.video.spherical.OrientationListener.Listener
        public synchronized void onOrientationChange(float[] r4, float r5) {
            monitor-enter(this);
            System.arraycopy(r4, 0, this.deviceOrientationMatrix, 0, this.deviceOrientationMatrix.length);     // Catch: Throwable -> L6
            this.deviceRoll = -r5;     // Catch: Throwable -> L6
            updatePitchMatrix();     // Catch: Throwable -> L6
            monitor-exit(this);
            return;
        L6:
            th = move-exception;
            throw th;
        }

        @Override // androidx.media3.exoplayer.video.spherical.TouchTracker.Listener
        public synchronized void onScrollChange(PointF r8) {
            monitor-enter(this);
            this.touchPitch = r8.y;     // Catch: Throwable -> L6
            updatePitchMatrix();     // Catch: Throwable -> L6
            Matrix.setRotateM(this.touchYawMatrix, 0, -r8.x, 0.0f, 1.0f, 0.0f);     // Catch: Throwable -> L6
            monitor-exit(this);
            return;
        L6:
            th = move-exception;
            throw th;
        }

        @Override // androidx.media3.exoplayer.video.spherical.TouchTracker.Listener
        public boolean onSingleTapUp(MotionEvent r2) {
            return this.this$0.performClick();
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceChanged(GL10 r9, int r10, int r11) {
            GLES20.glViewport(0, 0, r10, r11);
            float r5 = r10 / r11;
            Matrix.perspectiveM(this.projectionMatrix, 0, calculateFieldOfViewInYDirection(r5), r5, 0.1f, SphericalGLSurfaceView.Z_FAR);
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public synchronized void onSurfaceCreated(GL10 r3, EGLConfig r4) {
            monitor-enter(this);
            SphericalGLSurfaceView.access$000(this.this$0, this.scene.init());     // Catch: Throwable -> L6
            monitor-exit(this);
            return;
        L6:
            th = move-exception;
            throw th;
        }
    }

    public interface VideoSurfaceListener {
        void onVideoSurfaceCreated(Surface r1);

        void onVideoSurfaceDestroyed(Surface r1);
    }

    public SphericalGLSurfaceView(Context r2) {
        this(r2, null);
    }

    public SphericalGLSurfaceView(Context r10, AttributeSet r11) {
        super(r10, r11);
        this.videoSurfaceListeners = new CopyOnWriteArrayList();
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.sensorManager = (SensorManager) Assertions.checkNotNull(r10.getSystemService("sensor"));
        Sensor r0 = this.sensorManager.getDefaultSensor(15);
        if (r0 != null) goto L5;
        r0 = this.sensorManager.getDefaultSensor(11);
    L5:
        this.orientationSensor = r0;
        this.scene = new SceneRenderer();
        Renderer r1 = new Renderer(this, this.scene);
        this.touchTracker = new TouchTracker(r10, r1, PX_PER_DEGREES);
        this.orientationListener = new OrientationListener(((WindowManager) Assertions.checkNotNull((WindowManager) r10.getSystemService("window"))).getDefaultDisplay(), new OrientationListener.Listener[]{this.touchTracker, r1});
        this.useSensorRotation = true;
        setEGLContextClientVersion(2);
        setRenderer(r1);
        setOnTouchListener(this.touchTracker);
    }

    static /* synthetic */ void access$000(SphericalGLSurfaceView r0, SurfaceTexture r1) {
        r0.onSurfaceTextureAvailable(r1);
    }

    private void onSurfaceTextureAvailable(final SurfaceTexture r3) {
        this.mainHandler.post(new SphericalGLSurfaceView$$ExternalSyntheticLambda1(this, r3));
    }

    private static void releaseSurface(SurfaceTexture r0, Surface r1) {
        if (r0 == null) goto L4;
        r0.release();
    L4:
        if (r1 == null) goto L7;
        r1.release();
        return;
    }

    private void updateOrientationListenerRegistration() {
        if (this.useSensorRotation == true) goto L5;
    L7:
        boolean r0 = false;
    L9:
        if (this.orientationSensor != null) goto L11;
        return;
    L11:
        if (r0 == this.isOrientationListenerRegistered) goto L19;
        if (r0 == false) goto L15;
        this.sensorManager.registerListener(this.orientationListener, this.orientationSensor, 0);
    L16:
        this.isOrientationListenerRegistered = r0;
        return;
    L15:
        this.sensorManager.unregisterListener(this.orientationListener);
        goto L16
    L19:
        return;
    L5:
        if (this.isStarted == false) goto L7;
        r0 = true;
        goto L9
    }

    public void addVideoSurfaceListener(VideoSurfaceListener r2) {
        this.videoSurfaceListeners.add(r2);
    }

    public CameraMotionListener getCameraMotionListener() {
        return this.scene;
    }

    public VideoFrameMetadataListener getVideoFrameMetadataListener() {
        return this.scene;
    }

    public Surface getVideoSurface() {
        return this.surface;
    }

    /* JADX INFO: renamed from: lambda$onDetachedFromWindow$0$androidx-media3-exoplayer-video-spherical-SphericalGLSurfaceView */
    /* synthetic */ void m165x99583d2c() {
        Surface r0 = this.surface;
        if (r0 == null) goto L8;
        Iterator<VideoSurfaceListener> r1 = this.videoSurfaceListeners.iterator();
    L6:
        if (r1.hasNext() == false) goto L8;
        r1.next().onVideoSurfaceDestroyed(r0);
    L8:
        releaseSurface(this.surfaceTexture, r0);
        this.surfaceTexture = null;
        this.surface = null;
    }

    /* JADX INFO: renamed from: lambda$onSurfaceTextureAvailable$1$androidx-media3-exoplayer-video-spherical-SphericalGLSurfaceView */
    /* synthetic */ void m166x24c550f4(SurfaceTexture r6) {
        SurfaceTexture r0 = this.surfaceTexture;
        Surface r1 = this.surface;
        Surface r2 = new Surface(r6);
        this.surfaceTexture = r6;
        this.surface = r2;
        Iterator<VideoSurfaceListener> r3 = this.videoSurfaceListeners.iterator();
    L4:
        if (r3.hasNext() == false) goto L6;
        r3.next().onVideoSurfaceCreated(r2);
        goto L4
    L6:
        releaseSurface(r0, r1);
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mainHandler.post(new SphericalGLSurfaceView$$ExternalSyntheticLambda0(this));
    }

    @Override // android.opengl.GLSurfaceView
    public void onPause() {
        this.isStarted = false;
        updateOrientationListenerRegistration();
        super.onPause();
    }

    @Override // android.opengl.GLSurfaceView
    public void onResume() {
        super.onResume();
        this.isStarted = true;
        updateOrientationListenerRegistration();
    }

    public void removeVideoSurfaceListener(VideoSurfaceListener r2) {
        this.videoSurfaceListeners.remove(r2);
    }

    public void setDefaultStereoMode(int r2) {
        this.scene.setDefaultStereoMode(r2);
    }

    public void setUseSensorRotation(boolean r1) {
        this.useSensorRotation = r1;
        updateOrientationListenerRegistration();
    }
}
