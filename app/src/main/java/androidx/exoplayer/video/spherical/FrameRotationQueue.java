package androidx.media3.exoplayer.video.spherical;

import android.opengl.Matrix;
import androidx.media3.common.util.GlUtil;
import androidx.media3.common.util.TimedValueQueue;

/* JADX INFO: loaded from: classes.dex */
final class FrameRotationQueue {
    private final float[] recenterMatrix;
    private boolean recenterMatrixComputed;
    private final float[] rotationMatrix;
    private final TimedValueQueue<float[]> rotations;

    public FrameRotationQueue() {
        this.recenterMatrix = new float[16];
        this.rotationMatrix = new float[16];
        this.rotations = new TimedValueQueue();
    }

    public static void computeRecenterMatrix(float[] r6, float[] r7) {
        GlUtil.setToIdentity(r6);
        float r3 = (float) Math.sqrt((r7[10] * r7[10]) + (r7[8] * r7[8]));
        r6[0] = r7[10] / r3;
        r6[2] = r7[8] / r3;
        r6[8] = (-r7[8]) / r3;
        r6[10] = r7[10] / r3;
    }

    private static void getRotationMatrixFromAngleAxis(float[] r12, float[] r13) {
        float r0 = r13[0];
        float r1 = -r13[1];
        float r2 = -r13[2];
        float r3 = Matrix.length(r0, r1, r2);
        if (r3 == 0.0f) goto L5;
        Matrix.setRotateM(r12, 0, (float) Math.toDegrees(r3), r0 / r3, r1 / r3, r2 / r3);
        return;
    L5:
        GlUtil.setToIdentity(r12);
    }

    public boolean pollRotationMatrix(float[] r11, long r12) {
        float[] r0 = this.rotations.pollFloor(r12);
        if (r0 != null) goto L6;
        return false;
    L6:
        getRotationMatrixFromAngleAxis(this.rotationMatrix, r0);
        if (this.recenterMatrixComputed == true) goto L9;
        computeRecenterMatrix(this.recenterMatrix, this.rotationMatrix);
        this.recenterMatrixComputed = true;
    L9:
        Matrix.multiplyMM(r11, 0, this.recenterMatrix, 0, this.rotationMatrix, 0);
        return true;
    }

    public void reset() {
        this.rotations.clear();
        this.recenterMatrixComputed = false;
    }

    public void setRotation(long r2, float[] r4) {
        this.rotations.add(r2, r4);
    }
}
