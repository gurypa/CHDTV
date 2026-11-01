package androidx.core.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;

/* JADX INFO: loaded from: classes.dex */
public abstract class AutoScrollHelper implements View.OnTouchListener {
    private static final int DEFAULT_ACTIVATION_DELAY = 0;
    private static final int DEFAULT_EDGE_TYPE = 1;
    private static final float DEFAULT_MAXIMUM_EDGE = Float.MAX_VALUE;
    private static final int DEFAULT_MAXIMUM_VELOCITY_DIPS = 1575;
    private static final int DEFAULT_MINIMUM_VELOCITY_DIPS = 315;
    private static final int DEFAULT_RAMP_DOWN_DURATION = 500;
    private static final int DEFAULT_RAMP_UP_DURATION = 500;
    private static final float DEFAULT_RELATIVE_EDGE = 0.2f;
    private static final float DEFAULT_RELATIVE_VELOCITY = 1.0f;
    public static final int EDGE_TYPE_INSIDE = 0;
    public static final int EDGE_TYPE_INSIDE_EXTEND = 1;
    public static final int EDGE_TYPE_OUTSIDE = 2;
    private static final int HORIZONTAL = 0;
    public static final float NO_MAX = Float.MAX_VALUE;
    public static final float NO_MIN = 0.0f;
    public static final float RELATIVE_UNSPECIFIED = 0.0f;
    private static final int VERTICAL = 1;
    private int mActivationDelay;
    private boolean mAlreadyDelayed;
    boolean mAnimating;
    private final Interpolator mEdgeInterpolator;
    private int mEdgeType;
    private boolean mEnabled;
    private boolean mExclusive;
    private float[] mMaximumEdges;
    private float[] mMaximumVelocity;
    private float[] mMinimumVelocity;
    boolean mNeedsCancel;
    boolean mNeedsReset;
    private float[] mRelativeEdges;
    private float[] mRelativeVelocity;
    private Runnable mRunnable;
    final ClampedScroller mScroller;
    final View mTarget;

    private static class ClampedScroller {
        private long mDeltaTime;
        private int mDeltaX;
        private int mDeltaY;
        private int mEffectiveRampDown;
        private int mRampDownDuration;
        private int mRampUpDuration;
        private long mStartTime;
        private long mStopTime;
        private float mStopValue;
        private float mTargetVelocityX;
        private float mTargetVelocityY;

        ClampedScroller() {
            this.mStartTime = Long.MIN_VALUE;
            this.mStopTime = -1;
            this.mDeltaTime = 0;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }

        private float getValueAt(long r9) {
            if (r9 >= this.mStartTime) goto L6;
            return 0.0f;
        L6:
            if (this.mStopTime < 0) goto L13;
            if (r9 < this.mStopTime) goto L13;
            return (1.0f - this.mStopValue) + (this.mStopValue * AutoScrollHelper.constrain((r9 - this.mStopTime) / this.mEffectiveRampDown, 0.0f, 1.0f));
        L13:
            return AutoScrollHelper.constrain((r9 - this.mStartTime) / this.mRampUpDuration, 0.0f, 1.0f) * 0.5f;
        }

        private float interpolateValue(float r3) {
            return (((-4.0f) * r3) * r3) + (4.0f * r3);
        }

        public void computeScrollDelta() {
            if (this.mDeltaTime == 0) goto L7;
            long r0 = AnimationUtils.currentAnimationTimeMillis();
            float r3 = interpolateValue(getValueAt(r0));
            long r4 = r0 - this.mDeltaTime;
            this.mDeltaTime = r0;
            this.mDeltaX = (int) ((r4 * r3) * this.mTargetVelocityX);
            this.mDeltaY = (int) ((r4 * r3) * this.mTargetVelocityY);
            return;
        L7:
            throw new RuntimeException("Cannot compute scroll delta before calling start()");
        }

        public int getDeltaX() {
            return this.mDeltaX;
        }

        public int getDeltaY() {
            return this.mDeltaY;
        }

        public int getHorizontalDirection() {
            return (int) (this.mTargetVelocityX / Math.abs(this.mTargetVelocityX));
        }

        public int getVerticalDirection() {
            return (int) (this.mTargetVelocityY / Math.abs(this.mTargetVelocityY));
        }

        public boolean isFinished() {
            if (this.mStopTime > 0) goto L5;
        L7:
            return false;
        L5:
            if (AnimationUtils.currentAnimationTimeMillis() <= (this.mStopTime + ((long) this.mEffectiveRampDown))) goto L7;
            return true;
        }

        public void requestStop() {
            long r0 = AnimationUtils.currentAnimationTimeMillis();
            this.mEffectiveRampDown = AutoScrollHelper.constrain((int) (r0 - this.mStartTime), 0, this.mRampDownDuration);
            this.mStopValue = getValueAt(r0);
            this.mStopTime = r0;
        }

        public void setRampDownDuration(int r1) {
            this.mRampDownDuration = r1;
        }

        public void setRampUpDuration(int r1) {
            this.mRampUpDuration = r1;
        }

        public void setTargetVelocity(float r1, float r2) {
            this.mTargetVelocityX = r1;
            this.mTargetVelocityY = r2;
        }

        public void start() {
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mStopTime = -1;
            this.mDeltaTime = this.mStartTime;
            this.mStopValue = 0.5f;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }
    }

    private class ScrollAnimationRunnable implements Runnable {
        final /* synthetic */ AutoScrollHelper this$0;

        ScrollAnimationRunnable(AutoScrollHelper r1) {
            this.this$0 = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.this$0.mAnimating == true) goto L6;
            return;
        L6:
            if (this.this$0.mNeedsReset == false) goto L8;
            this.this$0.mNeedsReset = false;
            this.this$0.mScroller.start();
        L8:
            ClampedScroller r0 = this.this$0.mScroller;
            if (r0.isFinished() == false) goto L11;
        L18:
            this.this$0.mAnimating = false;
            return;
        L11:
            if (this.this$0.shouldAnimate() == false) goto L18;
            if (this.this$0.mNeedsCancel == false) goto L16;
            this.this$0.mNeedsCancel = false;
            this.this$0.cancelTargetTouch();
        L16:
            r0.computeScrollDelta();
            int r1 = r0.getDeltaX();
            int r2 = r0.getDeltaY();
            this.this$0.scrollTargetBy(r1, r2);
            ViewCompat.postOnAnimation(this.this$0.mTarget, this);
        }
    }

    static {
        DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
    }

    public AutoScrollHelper(View r6) {
        this.mScroller = new ClampedScroller();
        this.mEdgeInterpolator = new AccelerateInterpolator();
        this.mRelativeEdges = new float[]{0.0f, 0.0f};
        this.mMaximumEdges = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
        this.mRelativeVelocity = new float[]{0.0f, 0.0f};
        this.mMinimumVelocity = new float[]{0.0f, 0.0f};
        this.mMaximumVelocity = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
        this.mTarget = r6;
        DisplayMetrics r0 = Resources.getSystem().getDisplayMetrics();
        int r2 = (int) ((r0.density * 1575.0f) + 0.5f);
        int r1 = (int) ((r0.density * 315.0f) + 0.5f);
        setMaximumVelocity(r2, r2);
        setMinimumVelocity(r1, r1);
        setEdgeType(1);
        setMaximumEdges(Float.MAX_VALUE, Float.MAX_VALUE);
        setRelativeEdges(0.2f, 0.2f);
        setRelativeVelocity(1.0f, 1.0f);
        setActivationDelay(DEFAULT_ACTIVATION_DELAY);
        setRampUpDuration(500);
        setRampDownDuration(500);
    }

    private float computeTargetVelocity(int r9, float r10, float r11, float r12) {
        float r2 = getEdgeValue(this.mRelativeEdges[r9], r11, this.mMaximumEdges[r9], r10);
        if (r2 != 0.0f) goto L5;
        return 0.0f;
    L5:
        float r4 = this.mRelativeVelocity[r9];
        float r5 = this.mMinimumVelocity[r9];
        float r6 = this.mMaximumVelocity[r9];
        float r7 = r4 * r12;
        if (r2 <= 0.0f) goto L10;
        return constrain(r2 * r7, r5, r6);
    L10:
        return -constrain((-r2) * r7, r5, r6);
    }

    static float constrain(float r1, float r2, float r3) {
        if (r1 <= r3) goto L6;
        return r3;
    L6:
        if (r1 >= r2) goto L8;
        return r2;
    L8:
        return r1;
    }

    static int constrain(int r0, int r1, int r2) {
        if (r0 <= r2) goto L4;
        return r2;
    L4:
        if (r0 >= r1) goto L6;
        return r1;
    L6:
        return r0;
    }

    private float constrainEdgeValue(float r5, float r6) {
        if (r6 != 0.0f) goto L6;
        return 0.0f;
    L6:
        switch(this.mEdgeType) {
            case 0: goto L13;
            case 1: goto L13;
            case 2: goto L9;
            default: goto L23;
        };
    L23:
        return 0.0f;
    L9:
        if (r5 >= 0.0f) goto L23;
        return r5 / (-r6);
    L13:
        if (r5 >= r6) goto L23;
        if (r5 < 0.0f) goto L19;
        return 1.0f - (r5 / r6);
    L19:
        if (this.mAnimating == false) goto L23;
        if (this.mEdgeType != 1) goto L23;
        return 1.0f;
    }

    private float getEdgeValue(float r8, float r9, float r10, float r11) {
        float r0 = constrain(r8 * r9, 0.0f, r10);
        float r2 = constrainEdgeValue(r11, r0);
        float r4 = constrainEdgeValue(r9 - r11, r0) - r2;
        if (r4 >= 0.0f) goto L6;
        float r1 = -this.mEdgeInterpolator.getInterpolation(-r4);
    L9:
        return constrain(r1, -1.0f, 1.0f);
    L6:
        if (r4 <= 0.0f) goto L10;
        r1 = this.mEdgeInterpolator.getInterpolation(r4);
        goto L9
    L10:
        return 0.0f;
    }

    private void requestStop() {
        if (this.mNeedsReset == false) goto L5;
        this.mAnimating = false;
        return;
    L5:
        this.mScroller.requestStop();
    }

    private void startAnimating() {
        if (this.mRunnable != null) goto L5;
        this.mRunnable = new ScrollAnimationRunnable(this);
    L5:
        this.mAnimating = true;
        this.mNeedsReset = true;
        if (this.mAlreadyDelayed == false) goto L8;
    L10:
        this.mRunnable.run();
    L11:
        this.mAlreadyDelayed = true;
        return;
    L8:
        if (this.mActivationDelay <= 0) goto L10;
        ViewCompat.postOnAnimationDelayed(this.mTarget, this.mRunnable, this.mActivationDelay);
        goto L11
    }

    public abstract boolean canTargetScrollHorizontally(int r1);

    public abstract boolean canTargetScrollVertically(int r1);

    void cancelTargetTouch() {
        long r0 = SystemClock.uptimeMillis();
        MotionEvent r2 = MotionEvent.obtain(r0, r0, 3, 0.0f, 0.0f, 0);
        this.mTarget.onTouchEvent(r2);
        r2.recycle();
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public boolean isExclusive() {
        return this.mExclusive;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View r8, MotionEvent r9) {
        if (this.mEnabled == true) goto L6;
        return false;
    L6:
        switch(r9.getActionMasked()) {
            case 0: goto L9;
            case 1: goto L8;
            case 2: goto L10;
            case 3: goto L8;
            default: goto L16;
        };
    L8:
        requestStop();
        goto L16
    L9:
        this.mNeedsCancel = true;
        this.mAlreadyDelayed = false;
    L10:
        this.mScroller.setTargetVelocity(computeTargetVelocity(0, r9.getX(), r8.getWidth(), this.mTarget.getWidth()), computeTargetVelocity(1, r9.getY(), r8.getHeight(), this.mTarget.getHeight()));
        if (this.mAnimating == true) goto L16;
        if (shouldAnimate() == false) goto L16;
        startAnimating();
    L16:
        if (this.mExclusive == true) goto L18;
        return false;
    L18:
        if (this.mAnimating == true) goto L20;
        return false;
    L20:
        return true;
    }

    public abstract void scrollTargetBy(int r1, int r2);

    public AutoScrollHelper setActivationDelay(int r1) {
        this.mActivationDelay = r1;
        return this;
    }

    public AutoScrollHelper setEdgeType(int r1) {
        this.mEdgeType = r1;
        return this;
    }

    public AutoScrollHelper setEnabled(boolean r2) {
        if (this.mEnabled == false) goto L6;
        if (r2 == true) goto L6;
        requestStop();
    L6:
        this.mEnabled = r2;
        return this;
    }

    public AutoScrollHelper setExclusive(boolean r1) {
        this.mExclusive = r1;
        return this;
    }

    public AutoScrollHelper setMaximumEdges(float r3, float r4) {
        this.mMaximumEdges[0] = r3;
        this.mMaximumEdges[1] = r4;
        return this;
    }

    public AutoScrollHelper setMaximumVelocity(float r5, float r6) {
        this.mMaximumVelocity[0] = r5 / 1000.0f;
        this.mMaximumVelocity[1] = r6 / 1000.0f;
        return this;
    }

    public AutoScrollHelper setMinimumVelocity(float r5, float r6) {
        this.mMinimumVelocity[0] = r5 / 1000.0f;
        this.mMinimumVelocity[1] = r6 / 1000.0f;
        return this;
    }

    public AutoScrollHelper setRampDownDuration(int r2) {
        this.mScroller.setRampDownDuration(r2);
        return this;
    }

    public AutoScrollHelper setRampUpDuration(int r2) {
        this.mScroller.setRampUpDuration(r2);
        return this;
    }

    public AutoScrollHelper setRelativeEdges(float r3, float r4) {
        this.mRelativeEdges[0] = r3;
        this.mRelativeEdges[1] = r4;
        return this;
    }

    public AutoScrollHelper setRelativeVelocity(float r5, float r6) {
        this.mRelativeVelocity[0] = r5 / 1000.0f;
        this.mRelativeVelocity[1] = r6 / 1000.0f;
        return this;
    }

    boolean shouldAnimate() {
        ClampedScroller r0 = this.mScroller;
        int r1 = r0.getVerticalDirection();
        int r2 = r0.getHorizontalDirection();
        if (r1 != 0) goto L5;
    L6:
        if (r2 != 0) goto L8;
    L10:
        return false;
    L8:
        if (canTargetScrollHorizontally(r2) == false) goto L10;
    L9:
        return true;
    L5:
        if (canTargetScrollVertically(r1) == true) goto L9;
        goto L6
    }
}
