package androidx.media3.exoplayer.video.spherical;

import android.content.Context;
import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
final class TouchTracker extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener, OrientationListener.Listener {
    static final float MAX_PITCH_DEGREES = 45.0f;
    private final PointF accumulatedTouchOffsetDegrees;
    private final GestureDetector gestureDetector;
    private final Listener listener;
    private final PointF previousTouchPointPx;
    private final float pxPerDegrees;
    private volatile float roll;

    public interface Listener {

        /* JADX INFO: renamed from: androidx.media3.exoplayer.video.spherical.TouchTracker$Listener$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static boolean $default$onSingleTapUp(Listener r1, MotionEvent r2) {
                return false;
            }
        }

        void onScrollChange(PointF r1);

        boolean onSingleTapUp(MotionEvent r1);
    }

    public TouchTracker(Context r2, Listener r3, float r4) {
        this.previousTouchPointPx = new PointF();
        this.accumulatedTouchOffsetDegrees = new PointF();
        this.listener = r3;
        this.pxPerDegrees = r4;
        this.gestureDetector = new GestureDetector(r2, this);
        this.roll = 3.1415927f;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent r4) {
        this.previousTouchPointPx.set(r4.getX(), r4.getY());
        return true;
    }

    @Override // androidx.media3.exoplayer.video.spherical.OrientationListener.Listener
    public void onOrientationChange(float[] r2, float r3) {
        this.roll = -r3;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent r10, MotionEvent r11, float r12, float r13) {
        float r0 = (r11.getX() - this.previousTouchPointPx.x) / this.pxPerDegrees;
        float r1 = (r11.getY() - this.previousTouchPointPx.y) / this.pxPerDegrees;
        this.previousTouchPointPx.set(r11.getX(), r11.getY());
        float r2 = this.roll;
        float r3 = (float) Math.cos(r2);
        float r4 = (float) Math.sin(r2);
        this.accumulatedTouchOffsetDegrees.x -= (r3 * r0) - (r4 * r1);
        this.accumulatedTouchOffsetDegrees.y += (r4 * r0) + (r3 * r1);
        this.accumulatedTouchOffsetDegrees.y = Math.max(-45.0f, Math.min(MAX_PITCH_DEGREES, this.accumulatedTouchOffsetDegrees.y));
        this.listener.onScrollChange(this.accumulatedTouchOffsetDegrees);
        return true;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent r2) {
        return this.listener.onSingleTapUp(r2);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View r2, MotionEvent r3) {
        return this.gestureDetector.onTouchEvent(r3);
    }
}
