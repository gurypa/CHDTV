package androidx.core.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/* JADX INFO: loaded from: classes.dex */
public final class GestureDetectorCompat {
    private final GestureDetectorCompatImpl mImpl;

    interface GestureDetectorCompatImpl {
        boolean isLongpressEnabled();

        boolean onTouchEvent(MotionEvent r1);

        void setIsLongpressEnabled(boolean r1);

        void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener r1);
    }

    static class GestureDetectorCompatImplBase implements GestureDetectorCompatImpl {
        private static final int DOUBLE_TAP_TIMEOUT = 0;
        private static final int LONG_PRESS = 2;
        private static final int SHOW_PRESS = 1;
        private static final int TAP = 3;
        private static final int TAP_TIMEOUT = 0;
        private boolean mAlwaysInBiggerTapRegion;
        private boolean mAlwaysInTapRegion;
        MotionEvent mCurrentDownEvent;
        boolean mDeferConfirmSingleTap;
        GestureDetector.OnDoubleTapListener mDoubleTapListener;
        private int mDoubleTapSlopSquare;
        private float mDownFocusX;
        private float mDownFocusY;
        private final Handler mHandler;
        private boolean mInLongPress;
        private boolean mIsDoubleTapping;
        private boolean mIsLongpressEnabled;
        private float mLastFocusX;
        private float mLastFocusY;
        final GestureDetector.OnGestureListener mListener;
        private int mMaximumFlingVelocity;
        private int mMinimumFlingVelocity;
        private MotionEvent mPreviousUpEvent;
        boolean mStillDown;
        private int mTouchSlopSquare;
        private VelocityTracker mVelocityTracker;

        private class GestureHandler extends Handler {
            final /* synthetic */ GestureDetectorCompatImplBase this$0;

            GestureHandler(GestureDetectorCompatImplBase r1) {
                this.this$0 = r1;
            }

            GestureHandler(GestureDetectorCompatImplBase r1, Handler r2) {
                this.this$0 = r1;
                super(r2.getLooper());
            }

            @Override // android.os.Handler
            public void handleMessage(Message r4) {
                switch(r4.what) {
                    case 1: goto L13;
                    case 2: goto L12;
                    case 3: goto L7;
                    default: goto L5;
                };
            L12:
                this.this$0.dispatchLongPress();
                return;
            L13:
                this.this$0.mListener.onShowPress(this.this$0.mCurrentDownEvent);
                return;
            L5:
                throw new RuntimeException("Unknown message " + r4);
            L7:
                if (this.this$0.mDoubleTapListener != null) goto L9;
                return;
            L9:
                if (this.this$0.mStillDown == true) goto L11;
                this.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$0.mCurrentDownEvent);
                return;
            L11:
                this.this$0.mDeferConfirmSingleTap = true;
            }
        }

        static {
            TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
            DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
        }

        GestureDetectorCompatImplBase(Context r2, GestureDetector.OnGestureListener r3, Handler r4) {
            if (r4 == null) goto L5;
            this.mHandler = new GestureHandler(this, r4);
        L6:
            this.mListener = r3;
            if ((r3 instanceof GestureDetector.OnDoubleTapListener) == false) goto L9;
            setOnDoubleTapListener((GestureDetector.OnDoubleTapListener) r3);
        L9:
            init(r2);
            return;
        L5:
            this.mHandler = new GestureHandler(this);
            goto L6
        }

        private void cancel() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mIsDoubleTapping = false;
            this.mStillDown = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress == false) goto L6;
            this.mInLongPress = false;
            return;
        }

        private void cancelTaps() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mIsDoubleTapping = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress == false) goto L6;
            this.mInLongPress = false;
            return;
        }

        private void init(Context r5) {
            if (r5 == null) goto L10;
            if (this.mListener == null) goto L8;
            this.mIsLongpressEnabled = true;
            ViewConfiguration r0 = ViewConfiguration.get(r5);
            int r1 = r0.getScaledTouchSlop();
            int r2 = r0.getScaledDoubleTapSlop();
            this.mMinimumFlingVelocity = r0.getScaledMinimumFlingVelocity();
            this.mMaximumFlingVelocity = r0.getScaledMaximumFlingVelocity();
            this.mTouchSlopSquare = r1 * r1;
            this.mDoubleTapSlopSquare = r2 * r2;
            return;
        L8:
            throw new IllegalArgumentException("OnGestureListener must not be null");
        L10:
            throw new IllegalArgumentException("Context must not be null");
        }

        private boolean isConsideredDoubleTap(MotionEvent r7, MotionEvent r8, MotionEvent r9) {
            if (this.mAlwaysInBiggerTapRegion == true) goto L6;
            return false;
        L6:
            if ((r9.getEventTime() - r8.getEventTime()) <= DOUBLE_TAP_TIMEOUT) goto L8;
            return false;
        L8:
            int r0 = ((int) r7.getX()) - ((int) r9.getX());
            int r2 = ((int) r7.getY()) - ((int) r9.getY());
            if (((r0 * r0) + (r2 * r2)) >= this.mDoubleTapSlopSquare) goto L12;
            return true;
        L12:
            return false;
        }

        void dispatchLongPress() {
            this.mHandler.removeMessages(3);
            this.mDeferConfirmSingleTap = false;
            this.mInLongPress = true;
            this.mListener.onLongPress(this.mCurrentDownEvent);
        }

        @Override // androidx.core.view.GestureDetectorCompat.GestureDetectorCompatImpl
        public boolean isLongpressEnabled() {
            return this.mIsLongpressEnabled;
        }

        @Override // androidx.core.view.GestureDetectorCompat.GestureDetectorCompatImpl
        public boolean onTouchEvent(MotionEvent r23) {
            int r2 = r23.getAction();
            if (this.mVelocityTracker != null) goto L5;
            this.mVelocityTracker = VelocityTracker.obtain();
        L5:
            this.mVelocityTracker.addMovement(r23);
            if ((r2 & 255) != 6) goto L8;
            boolean r3 = true;
        L9:
            if (r3 == false) goto L11;
            int r4 = r23.getActionIndex();
        L12:
            float r7 = 0.0f;
            float r8 = 0.0f;
            int r9 = r23.getPointerCount();
            int r10 = 0;
        L13:
            if (r10 >= r9) goto L18;
            if (r4 == r10) goto L17;
            r7 = r7 + r23.getX(r10);
            r8 = r8 + r23.getY(r10);
        L17:
            r10 = r10 + 1;
            goto L13
        L18:
            if (r3 == false) goto L20;
            int r11 = r9 - 1;
        L21:
            float r12 = r7 / r11;
            float r13 = r8 / r11;
            boolean r14 = false;
            switch((r2 & 255)) {
                case 0: goto L84;
                case 1: goto L58;
                case 2: goto L38;
                case 3: goto L36;
                case 4: goto L112;
                case 5: goto L35;
                case 6: goto L24;
                default: goto L112;
            };
        L24:
            this.mLastFocusX = r12;
            this.mDownFocusX = r12;
            this.mLastFocusY = r13;
            this.mDownFocusY = r13;
            this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
            int r5 = r23.getActionIndex();
            int r6 = r23.getPointerId(r5);
            float r15 = this.mVelocityTracker.getXVelocity(r6);
            float r16 = this.mVelocityTracker.getYVelocity(r6);
            int r17 = 0;
        L25:
            if (r17 >= r9) goto L113;
            if (r17 == r5) goto L32;
            int r18 = r23.getPointerId(r17);
            if (((this.mVelocityTracker.getXVelocity(r18) * r15) + (this.mVelocityTracker.getYVelocity(r18) * r16)) >= 0.0f) goto L32;
            this.mVelocityTracker.clear();
        L32:
            r17 = r17 + 1;
            r3 = r3;
        L113:
            return false;
        L35:
            this.mLastFocusX = r12;
            this.mDownFocusX = r12;
            this.mLastFocusY = r13;
            this.mDownFocusY = r13;
            cancelTaps();
            return false;
        L36:
            cancel();
            return false;
        L58:
            this.mStillDown = false;
            MotionEvent r19 = MotionEvent.obtain(r23);
            if (this.mIsDoubleTapping == false) goto L62;
            r14 = false | this.mDoubleTapListener.onDoubleTapEvent(r23);
        L77:
            if (this.mPreviousUpEvent == null) goto L79;
            this.mPreviousUpEvent.recycle();
        L79:
            this.mPreviousUpEvent = r19;
            if (this.mVelocityTracker == null) goto L82;
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        L82:
            this.mIsDoubleTapping = false;
            this.mDeferConfirmSingleTap = false;
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            return r14;
        L62:
            if (this.mInLongPress == false) goto L65;
            this.mHandler.removeMessages(3);
            this.mInLongPress = false;
            goto L77
        L65:
            if (this.mAlwaysInTapRegion == false) goto L71;
            r14 = this.mListener.onSingleTapUp(r23);
            if (this.mDeferConfirmSingleTap == false) goto L77;
            if (this.mDoubleTapListener == null) goto L77;
            this.mDoubleTapListener.onSingleTapConfirmed(r23);
            goto L77
        L71:
            VelocityTracker r20 = this.mVelocityTracker;
            int r21 = r23.getPointerId(0);
            r20.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
            float r22 = r20.getYVelocity(r21);
            float r24 = r20.getXVelocity(r21);
            if (Math.abs(r22) <= this.mMinimumFlingVelocity) goto L74;
        L75:
            r14 = this.mListener.onFling(this.mCurrentDownEvent, r23, r24, r22);
            goto L77
        L74:
            if (Math.abs(r24) <= this.mMinimumFlingVelocity) goto L77;
        L112:
            return false;
        L38:
            if (this.mInLongPress == true) goto L116;
            float r25 = this.mLastFocusX - r12;
            float r26 = this.mLastFocusY - r13;
            if (this.mIsDoubleTapping == false) goto L44;
            return false | this.mDoubleTapListener.onDoubleTapEvent(r23);
        L44:
            if (this.mAlwaysInTapRegion == false) goto L54;
            int r110 = (int) (r12 - this.mDownFocusX);
            int r111 = (int) (r13 - this.mDownFocusY);
            int r27 = (r110 * r110) + (r111 * r111);
            if (r27 <= this.mTouchSlopSquare) goto L50;
            boolean r28 = this.mListener.onScroll(this.mCurrentDownEvent, r23, r25, r26);
            this.mLastFocusX = r12;
            this.mLastFocusY = r13;
            this.mAlwaysInTapRegion = false;
            this.mHandler.removeMessages(3);
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            r14 = r28;
        L50:
            if (r27 <= this.mTouchSlopSquare) goto L121;
            this.mAlwaysInBiggerTapRegion = false;
            return r14;
        L121:
            return r14;
        L54:
            if (Math.abs(r25) < 1.0f) goto L56;
        L57:
            boolean r112 = this.mListener.onScroll(this.mCurrentDownEvent, r23, r25, r26);
            this.mLastFocusX = r12;
            this.mLastFocusY = r13;
            return r112;
        L56:
            if (Math.abs(r26) >= 1.0f) goto L57;
            return false;
        L116:
            return false;
        L84:
            if (this.mDoubleTapListener == null) goto L97;
            boolean r29 = this.mHandler.hasMessages(3);
            if (r29 == false) goto L89;
            this.mHandler.removeMessages(3);
        L89:
            if (this.mCurrentDownEvent != null) goto L91;
        L96:
            this.mHandler.sendEmptyMessageDelayed(3, DOUBLE_TAP_TIMEOUT);
            goto L97
        L91:
            if (this.mPreviousUpEvent == null) goto L96;
            if (r29 == false) goto L96;
            if (isConsideredDoubleTap(this.mCurrentDownEvent, this.mPreviousUpEvent, r23) == false) goto L96;
            this.mIsDoubleTapping = true;
            r14 = (this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | false) | this.mDoubleTapListener.onDoubleTapEvent(r23);
        L97:
            this.mLastFocusX = r12;
            this.mDownFocusX = r12;
            this.mLastFocusY = r13;
            this.mDownFocusY = r13;
            if (this.mCurrentDownEvent == null) goto L100;
            this.mCurrentDownEvent.recycle();
        L100:
            this.mCurrentDownEvent = MotionEvent.obtain(r23);
            this.mAlwaysInTapRegion = true;
            this.mAlwaysInBiggerTapRegion = true;
            this.mStillDown = true;
            this.mInLongPress = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mIsLongpressEnabled == false) goto L103;
            this.mHandler.removeMessages(2);
            this.mHandler.sendEmptyMessageAtTime(2, (this.mCurrentDownEvent.getDownTime() + ((long) TAP_TIMEOUT)) + ((long) ViewConfiguration.getLongPressTimeout()));
        L103:
            this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + ((long) TAP_TIMEOUT));
            return r14 | this.mListener.onDown(r23);
        L20:
            r11 = r9;
            goto L21
        L11:
            r4 = -1;
            goto L12
        L8:
            r3 = false;
            goto L9
        }

        @Override // androidx.core.view.GestureDetectorCompat.GestureDetectorCompatImpl
        public void setIsLongpressEnabled(boolean r1) {
            this.mIsLongpressEnabled = r1;
        }

        @Override // androidx.core.view.GestureDetectorCompat.GestureDetectorCompatImpl
        public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener r1) {
            this.mDoubleTapListener = r1;
        }
    }

    static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompatImpl {
        private final GestureDetector mDetector;

        GestureDetectorCompatImplJellybeanMr2(Context r2, GestureDetector.OnGestureListener r3, Handler r4) {
            this.mDetector = new GestureDetector(r2, r3, r4);
        }

        @Override // androidx.core.view.GestureDetectorCompat.GestureDetectorCompatImpl
        public boolean isLongpressEnabled() {
            return this.mDetector.isLongpressEnabled();
        }

        @Override // androidx.core.view.GestureDetectorCompat.GestureDetectorCompatImpl
        public boolean onTouchEvent(MotionEvent r2) {
            return this.mDetector.onTouchEvent(r2);
        }

        @Override // androidx.core.view.GestureDetectorCompat.GestureDetectorCompatImpl
        public void setIsLongpressEnabled(boolean r2) {
            this.mDetector.setIsLongpressEnabled(r2);
        }

        @Override // androidx.core.view.GestureDetectorCompat.GestureDetectorCompatImpl
        public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener r2) {
            this.mDetector.setOnDoubleTapListener(r2);
        }
    }

    public GestureDetectorCompat(Context r2, GestureDetector.OnGestureListener r3) {
        this(r2, r3, null);
    }

    public GestureDetectorCompat(Context r2, GestureDetector.OnGestureListener r3, Handler r4) {
        this.mImpl = new GestureDetectorCompatImplJellybeanMr2(r2, r3, r4);
    }

    public boolean isLongpressEnabled() {
        return this.mImpl.isLongpressEnabled();
    }

    public boolean onTouchEvent(MotionEvent r2) {
        return this.mImpl.onTouchEvent(r2);
    }

    public void setIsLongpressEnabled(boolean r2) {
        this.mImpl.setIsLongpressEnabled(r2);
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener r2) {
        this.mImpl.setOnDoubleTapListener(r2);
    }
}
