package androidx.media3.exoplayer.video.spherical;

import android.graphics.SurfaceTexture;
import android.media.MediaFormat;
import android.opengl.GLES20;
import android.opengl.Matrix;
import androidx.media3.common.Format;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.GlUtil;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.TimedValueQueue;
import androidx.media3.exoplayer.video.VideoFrameMetadataListener;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
final class SceneRenderer implements VideoFrameMetadataListener, CameraMotionListener {
    private static final String TAG = "SceneRenderer";
    private volatile int defaultStereoMode;
    private final AtomicBoolean frameAvailable;
    private final FrameRotationQueue frameRotationQueue;
    private byte[] lastProjectionData;
    private int lastStereoMode;
    private final TimedValueQueue<Projection> projectionQueue;
    private final ProjectionRenderer projectionRenderer;
    private final AtomicBoolean resetRotationAtNextFrame;
    private final float[] rotationMatrix;
    private final TimedValueQueue<Long> sampleTimestampQueue;
    private SurfaceTexture surfaceTexture;
    private final float[] tempMatrix;
    private int textureId;

    public SceneRenderer() {
        this.frameAvailable = new AtomicBoolean();
        this.resetRotationAtNextFrame = new AtomicBoolean(true);
        this.projectionRenderer = new ProjectionRenderer();
        this.frameRotationQueue = new FrameRotationQueue();
        this.sampleTimestampQueue = new TimedValueQueue();
        this.projectionQueue = new TimedValueQueue();
        this.rotationMatrix = new float[16];
        this.tempMatrix = new float[16];
        this.defaultStereoMode = 0;
        this.lastStereoMode = -1;
    }

    private void setProjection(byte[] r6, int r7, long r8) {
        byte[] r0 = this.lastProjectionData;
        int r1 = this.lastStereoMode;
        this.lastProjectionData = r6;
        if (r7 != (-1)) goto L5;
        int r2 = this.defaultStereoMode;
    L6:
        this.lastStereoMode = r2;
        if (r1 == this.lastStereoMode) goto L9;
    L11:
        Projection r3 = null;
        if (this.lastProjectionData == null) goto L14;
        r3 = ProjectionDecoder.decode(this.lastProjectionData, this.lastStereoMode);
    L14:
        if (r3 != null) goto L16;
    L18:
        Projection r4 = Projection.createEquirectangular(this.lastStereoMode);
    L19:
        this.projectionQueue.add(r8, r4);
        return;
    L16:
        if (ProjectionRenderer.isSupported(r3) == false) goto L18;
        r4 = r3;
        goto L19
    L9:
        if (Arrays.equals(r0, this.lastProjectionData) == false) goto L11;
        return;
    L5:
        r2 = r7;
        goto L6
    }

    public void drawFrame(float[] r12, boolean r13) {
        GLES20.glClear(16384);
        GlUtil.checkGlError();     // Catch: GlUtil.GlException -> L5
    L8:
        if (this.frameAvailable.compareAndSet(true, false) == false) goto L23;
        ((SurfaceTexture) Assertions.checkNotNull(this.surfaceTexture)).updateTexImage();
        GlUtil.checkGlError();     // Catch: GlUtil.GlException -> L12
    L15:
        if (this.resetRotationAtNextFrame.compareAndSet(true, false) == false) goto L17;
        GlUtil.setToIdentity(this.rotationMatrix);
    L17:
        long r0 = this.surfaceTexture.getTimestamp();
        Long r2 = this.sampleTimestampQueue.poll(r0);
        if (r2 == null) goto L20;
        this.frameRotationQueue.pollRotationMatrix(this.rotationMatrix, r2.longValue());
    L20:
        Projection r3 = this.projectionQueue.pollFloor(r0);
        if (r3 == null) goto L23;
        this.projectionRenderer.setProjection(r3);
    L12:
        e = move-exception;
        Log.m45e(TAG, "Failed to draw a frame", e);
    L23:
        Matrix.multiplyMM(this.tempMatrix, 0, r12, 0, this.rotationMatrix, 0);
        this.projectionRenderer.draw(this.textureId, this.tempMatrix, r13);
        return;
    L5:
        e = move-exception;
        Log.m45e(TAG, "Failed to draw a frame", e);
        goto L8
    }

    public SurfaceTexture init() {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);     // Catch: GlUtil.GlException -> L5
        GlUtil.checkGlError();     // Catch: GlUtil.GlException -> L5
        this.projectionRenderer.init();     // Catch: GlUtil.GlException -> L5
        GlUtil.checkGlError();     // Catch: GlUtil.GlException -> L5
        this.textureId = GlUtil.createExternalTexture();     // Catch: GlUtil.GlException -> L5
    L7:
        this.surfaceTexture = new SurfaceTexture(this.textureId);
        this.surfaceTexture.setOnFrameAvailableListener(new SceneRenderer$$ExternalSyntheticLambda0(this));
        return this.surfaceTexture;
    L5:
        e = move-exception;
        Log.m45e(TAG, "Failed to initialize the renderer", e);
        goto L7
    }

    /* JADX INFO: renamed from: lambda$init$0$androidx-media3-exoplayer-video-spherical-SceneRenderer */
    /* synthetic */ void m164x200ab998(SurfaceTexture r3) {
        this.frameAvailable.set(true);
    }

    @Override // androidx.media3.exoplayer.video.spherical.CameraMotionListener
    public void onCameraMotion(long r2, float[] r4) {
        this.frameRotationQueue.setRotation(r2, r4);
    }

    @Override // androidx.media3.exoplayer.video.spherical.CameraMotionListener
    public void onCameraMotionReset() {
        this.sampleTimestampQueue.clear();
        this.frameRotationQueue.reset();
        this.resetRotationAtNextFrame.set(true);
    }

    @Override // androidx.media3.exoplayer.video.VideoFrameMetadataListener
    public void onVideoFrameAboutToBeRendered(long r3, long r5, Format r7, MediaFormat r8) {
        this.sampleTimestampQueue.add(r5, Long.valueOf(r3));
        setProjection(r7.projectionData, r7.stereoMode, r5);
    }

    public void setDefaultStereoMode(int r1) {
        this.defaultStereoMode = r1;
    }

    public void shutdown() {
        this.projectionRenderer.shutdown();
    }
}
