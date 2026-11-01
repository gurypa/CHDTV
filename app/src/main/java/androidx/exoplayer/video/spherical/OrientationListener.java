package androidx.media3.exoplayer.video.spherical;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.view.Display;
import androidx.media3.extractor.p001ts.TsExtractor;

/* JADX INFO: loaded from: classes.dex */
final class OrientationListener implements SensorEventListener {
    private final float[] angles;
    private final float[] deviceOrientationMatrix4x4;
    private final Display display;
    private final Listener[] listeners;
    private final float[] recenterMatrix4x4;
    private boolean recenterMatrixComputed;
    private final float[] tempMatrix4x4;

    public interface Listener {
        void onOrientationChange(float[] r1, float r2);
    }

    public OrientationListener(Display r3, Listener... r4) {
        this.deviceOrientationMatrix4x4 = new float[16];
        this.tempMatrix4x4 = new float[16];
        this.recenterMatrix4x4 = new float[16];
        this.angles = new float[3];
        this.display = r3;
        this.listeners = r4;
    }

    private float extractRoll(float[] r4) {
        SensorManager.remapCoordinateSystem(r4, 1, 131, this.tempMatrix4x4);
        SensorManager.getOrientation(this.tempMatrix4x4, this.angles);
        return this.angles[2];
    }

    private void notifyListeners(float[] r5, float r6) {
        Listener[] r0 = this.listeners;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].onOrientationChange(r5, r6);
        r2 = r2 + 1;
        goto L3
    }

    private void recenter(float[] r10) {
        if (this.recenterMatrixComputed == true) goto L5;
        FrameRotationQueue.computeRecenterMatrix(this.recenterMatrix4x4, r10);
        this.recenterMatrixComputed = true;
    L5:
        System.arraycopy(r10, 0, this.tempMatrix4x4, 0, this.tempMatrix4x4.length);
        Matrix.multiplyMM(r10, 0, this.tempMatrix4x4, 0, this.recenterMatrix4x4, 0);
    }

    private void rotateAroundZ(float[] r6, int r7) {
        switch(r7) {
            case 0: goto L10;
            case 1: goto L7;
            case 2: goto L6;
            case 3: goto L5;
            default: goto L4;
        };
    L5:
        int r0 = TsExtractor.TS_STREAM_TYPE_HDMV_DTS;
        int r1 = 1;
    L8:
        System.arraycopy(r6, 0, this.tempMatrix4x4, 0, this.tempMatrix4x4.length);
        SensorManager.remapCoordinateSystem(this.tempMatrix4x4, r0, r1, r6);
        return;
    L6:
        r0 = TsExtractor.TS_STREAM_TYPE_AC3;
        r1 = TsExtractor.TS_STREAM_TYPE_HDMV_DTS;
        goto L8
    L7:
        r0 = 2;
        r1 = TsExtractor.TS_STREAM_TYPE_AC3;
        goto L8
    L10:
        return;
    L4:
        throw new IllegalStateException();
    }

    private static void rotateYtoSky(float[] r6) {
        Matrix.rotateM(r6, 0, 90.0f, 1.0f, 0.0f, 0.0f);
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor r1, int r2) {
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent r3) {
        SensorManager.getRotationMatrixFromVector(this.deviceOrientationMatrix4x4, r3.values);
        rotateAroundZ(this.deviceOrientationMatrix4x4, this.display.getRotation());
        float r0 = extractRoll(this.deviceOrientationMatrix4x4);
        rotateYtoSky(this.deviceOrientationMatrix4x4);
        recenter(this.deviceOrientationMatrix4x4);
        notifyListeners(this.deviceOrientationMatrix4x4, r0);
    }
}
