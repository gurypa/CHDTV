package androidx.customview.widget;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import androidx.core.view.ViewCompat;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class ViewDragHelper {
    private static final int BASE_SETTLE_DURATION = 256;
    public static final int DIRECTION_ALL = 3;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_VERTICAL = 2;
    public static final int EDGE_ALL = 15;
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    private static final int EDGE_SIZE = 20;
    public static final int EDGE_TOP = 4;
    public static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 600;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "ViewDragHelper";
    private static final Interpolator sInterpolator = null;
    private int mActivePointerId;
    private final Callback mCallback;
    private View mCapturedView;
    private int mDragState;
    private int[] mEdgeDragsInProgress;
    private int[] mEdgeDragsLocked;
    private int mEdgeSize;
    private int[] mInitialEdgesTouched;
    private float[] mInitialMotionX;
    private float[] mInitialMotionY;
    private float[] mLastMotionX;
    private float[] mLastMotionY;
    private float mMaxVelocity;
    private float mMinVelocity;
    private final ViewGroup mParentView;
    private int mPointersDown;
    private boolean mReleaseInProgress;
    private OverScroller mScroller;
    private final Runnable mSetIdleRunnable;
    private int mTouchSlop;
    private int mTrackingEdges;
    private VelocityTracker mVelocityTracker;

    public static abstract class Callback {
        public Callback() {
        }

        public int clampViewPositionHorizontal(View r2, int r3, int r4) {
            return 0;
        }

        public int clampViewPositionVertical(View r2, int r3, int r4) {
            return 0;
        }

        public int getOrderedChildIndex(int r1) {
            return r1;
        }

        public int getViewHorizontalDragRange(View r2) {
            return 0;
        }

        public int getViewVerticalDragRange(View r2) {
            return 0;
        }

        public void onEdgeDragStarted(int r1, int r2) {
        }

        public boolean onEdgeLock(int r2) {
            return false;
        }

        public void onEdgeTouched(int r1, int r2) {
        }

        public void onViewCaptured(View r1, int r2) {
        }

        public void onViewDragStateChanged(int r1) {
        }

        public void onViewPositionChanged(View r1, int r2, int r3, int r4, int r5) {
        }

        public void onViewReleased(View r1, float r2, float r3) {
        }

        public abstract boolean tryCaptureView(View r1, int r2);
    }

    static {
        sInterpolator = new InterpolatorC01441();
    }

    private ViewDragHelper(Context r5, ViewGroup r6, Callback r7) {
        this.mActivePointerId = -1;
        this.mSetIdleRunnable = new RunnableC01452(this);
        if (r6 == null) goto L10;
        if (r7 == null) goto L8;
        this.mParentView = r6;
        this.mCallback = r7;
        ViewConfiguration r0 = ViewConfiguration.get(r5);
        this.mEdgeSize = (int) ((20.0f * r5.getResources().getDisplayMetrics().density) + 0.5f);
        this.mTouchSlop = r0.getScaledTouchSlop();
        this.mMaxVelocity = r0.getScaledMaximumFlingVelocity();
        this.mMinVelocity = r0.getScaledMinimumFlingVelocity();
        this.mScroller = new OverScroller(r5, sInterpolator);
        return;
    L8:
        throw new IllegalArgumentException("Callback may not be null");
    L10:
        throw new IllegalArgumentException("Parent view may not be null");
    }

    private boolean checkNewEdgeDrag(float r6, float r7, int r8, int r9) {
        float r0 = Math.abs(r6);
        float r1 = Math.abs(r7);
        if ((this.mInitialEdgesTouched[r8] & r9) == r9) goto L5;
    L27:
        return false;
    L5:
        if ((this.mTrackingEdges & r9) == 0) goto L27;
        if ((this.mEdgeDragsLocked[r8] & r9) == r9) goto L27;
        if ((this.mEdgeDragsInProgress[r8] & r9) == r9) goto L27;
        if (r0 > this.mTouchSlop) goto L16;
        if (r1 <= this.mTouchSlop) goto L27;
    L16:
        if (r0 >= (0.5f * r1)) goto L22;
        if (this.mCallback.onEdgeLock(r9) == false) goto L22;
        int[] r2 = this.mEdgeDragsLocked;
        r2[r8] = r2[r8] | r9;
        return false;
    L22:
        if ((this.mEdgeDragsInProgress[r8] & r9) == 0) goto L24;
        return false;
    L24:
        if (r0 <= this.mTouchSlop) goto L29;
        return true;
    L29:
        return false;
    }

    private boolean checkTouchSlop(View r8, float r9, float r10) {
        if (r8 != null) goto L6;
        return false;
    L6:
        if (this.mCallback.getViewHorizontalDragRange(r8) <= 0) goto L8;
        boolean r1 = true;
    L10:
        if (this.mCallback.getViewVerticalDragRange(r8) <= 0) goto L12;
        boolean r3 = true;
    L13:
        if (r1 == false) goto L19;
        if (r3 == false) goto L19;
        if (((r9 * r9) + (r10 * r10)) > (this.mTouchSlop * this.mTouchSlop)) goto L18;
        return false;
    L18:
        return true;
    L19:
        if (r1 == true) goto L21;
        if (r3 == true) goto L26;
        return false;
    L26:
        if (Math.abs(r10) > this.mTouchSlop) goto L28;
        return false;
    L28:
        return true;
    L21:
        if (Math.abs(r9) > this.mTouchSlop) goto L23;
        return false;
    L23:
        return true;
    L12:
        r3 = false;
        goto L13
    L8:
        r1 = false;
        goto L10
    }

    private float clampMag(float r4, float r5, float r6) {
        float r0 = Math.abs(r4);
        if (r0 >= r5) goto L6;
        return 0.0f;
    L6:
        if (r0 > r6) goto L8;
        return r4;
    L8:
        if (r4 <= 0.0f) goto L11;
        return r6;
    L11:
        return -r6;
    }

    private int clampMag(int r3, int r4, int r5) {
        int r0 = Math.abs(r3);
        if (r0 >= r4) goto L6;
        return 0;
    L6:
        if (r0 <= r5) goto L11;
        if (r3 <= 0) goto L10;
        return r5;
    L10:
        return -r5;
    L11:
        return r3;
    }

    private void clearMotionHistory() {
        if (this.mInitialMotionX != null) goto L5;
        return;
    L5:
        Arrays.fill(this.mInitialMotionX, 0.0f);
        Arrays.fill(this.mInitialMotionY, 0.0f);
        Arrays.fill(this.mLastMotionX, 0.0f);
        Arrays.fill(this.mLastMotionY, 0.0f);
        Arrays.fill(this.mInitialEdgesTouched, 0);
        Arrays.fill(this.mEdgeDragsInProgress, 0);
        Arrays.fill(this.mEdgeDragsLocked, 0);
        this.mPointersDown = 0;
    }

    private void clearMotionHistory(int r3) {
        if (this.mInitialMotionX != null) goto L5;
        return;
    L5:
        if (isPointerDown(r3) == false) goto L10;
        this.mInitialMotionX[r3] = 0.0f;
        this.mInitialMotionY[r3] = 0.0f;
        this.mLastMotionX[r3] = 0.0f;
        this.mLastMotionY[r3] = 0.0f;
        this.mInitialEdgesTouched[r3] = 0;
        this.mEdgeDragsInProgress[r3] = 0;
        this.mEdgeDragsLocked[r3] = 0;
        this.mPointersDown &= ~(1 << r3);
        return;
    }

    private int computeAxisDuration(int r8, int r9, int r10) {
        if (r8 != 0) goto L5;
        return 0;
    L5:
        int r0 = this.mParentView.getWidth();
        int r1 = r0 / 2;
        float r4 = r1 + (r1 * distanceInfluenceForSnapDuration(Math.min(1.0f, Math.abs(r8) / r0)));
        int r11 = Math.abs(r9);
        if (r11 <= 0) goto L8;
        int r3 = Math.round(Math.abs(r4 / r11) * 1000.0f) * 4;
    L10:
        return Math.min(r3, MAX_SETTLE_DURATION);
    L8:
        r3 = (int) ((1.0f + (Math.abs(r8) / r10)) * 256.0f);
        goto L10
    }

    private int computeSettleDuration(View r17, int r18, int r19, int r20, int r21) {
        int r2 = clampMag(r20, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        int r3 = clampMag(r21, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        int r4 = Math.abs(r18);
        int r5 = Math.abs(r19);
        int r6 = Math.abs(r2);
        int r7 = Math.abs(r3);
        int r8 = r6 + r7;
        int r9 = r4 + r5;
        if (r2 == 0) goto L5;
        float r10 = r6;
        float r11 = r8;
    L6:
        float r12 = r10 / r11;
        if (r3 == 0) goto L9;
        float r13 = r7;
        float r14 = r8;
    L10:
        float r15 = r13 / r14;
        return (int) ((computeAxisDuration(r18, r2, this.mCallback.getViewHorizontalDragRange(r17)) * r12) + (computeAxisDuration(r19, r3, this.mCallback.getViewVerticalDragRange(r17)) * r15));
    L9:
        r13 = r5;
        r14 = r9;
        goto L10
    L5:
        r10 = r4;
        r11 = r9;
        goto L6
    }

    public static ViewDragHelper create(ViewGroup r3, float r4, Callback r5) {
        ViewDragHelper r0 = create(r3, r5);
        r0.mTouchSlop = (int) (r0.mTouchSlop * (1.0f / r4));
        return r0;
    }

    public static ViewDragHelper create(ViewGroup r2, Callback r3) {
        return new ViewDragHelper(r2.getContext(), r2, r3);
    }

    private void dispatchViewReleased(float r4, float r5) {
        this.mReleaseInProgress = true;
        this.mCallback.onViewReleased(this.mCapturedView, r4, r5);
        this.mReleaseInProgress = false;
        if (this.mDragState != 1) goto L6;
        setDragState(0);
        return;
    }

    private float distanceInfluenceForSnapDuration(float r3) {
        return (float) Math.sin((r3 - 0.5f) * 0.47123894f);
    }

    private void dragTo(int r17, int r18, int r19, int r20) {
        int r5 = this.mCapturedView.getLeft();
        int r6 = this.mCapturedView.getTop();
        if (r19 == 0) goto L5;
        int r3 = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, r17, r19);
        ViewCompat.offsetLeftAndRight(this.mCapturedView, r3 - r5);
        int r12 = r3;
    L6:
        if (r20 == 0) goto L8;
        int r4 = this.mCallback.clampViewPositionVertical(this.mCapturedView, r18, r20);
        ViewCompat.offsetTopAndBottom(this.mCapturedView, r4 - r6);
        int r13 = r4;
    L9:
        if (r19 != 0) goto L11;
        if (r20 != 0) goto L11;
        return;
    L11:
        Callback r10 = this.mCallback;
        View r11 = this.mCapturedView;
        r10.onViewPositionChanged(r11, r12, r13, r12 - r5, r13 - r6);
        return;
    L8:
        r13 = r18;
        goto L9
    L5:
        r12 = r17;
        goto L6
    }

    private void ensureMotionHistorySizeForId(int r11) {
        if (this.mInitialMotionX != null) goto L5;
    L6:
        float[] r0 = new float[r11 + 1];
        float[] r1 = new float[r11 + 1];
        float[] r2 = new float[r11 + 1];
        float[] r3 = new float[r11 + 1];
        int[] r4 = new int[r11 + 1];
        int[] r5 = new int[r11 + 1];
        int[] r6 = new int[r11 + 1];
        if (this.mInitialMotionX == null) goto L9;
        System.arraycopy(this.mInitialMotionX, 0, r0, 0, this.mInitialMotionX.length);
        System.arraycopy(this.mInitialMotionY, 0, r1, 0, this.mInitialMotionY.length);
        System.arraycopy(this.mLastMotionX, 0, r2, 0, this.mLastMotionX.length);
        System.arraycopy(this.mLastMotionY, 0, r3, 0, this.mLastMotionY.length);
        System.arraycopy(this.mInitialEdgesTouched, 0, r4, 0, this.mInitialEdgesTouched.length);
        System.arraycopy(this.mEdgeDragsInProgress, 0, r5, 0, this.mEdgeDragsInProgress.length);
        System.arraycopy(this.mEdgeDragsLocked, 0, r6, 0, this.mEdgeDragsLocked.length);
    L9:
        this.mInitialMotionX = r0;
        this.mInitialMotionY = r1;
        this.mLastMotionX = r2;
        this.mLastMotionY = r3;
        this.mInitialEdgesTouched = r4;
        this.mEdgeDragsInProgress = r5;
        this.mEdgeDragsLocked = r6;
        return;
    L5:
        if (this.mInitialMotionX.length <= r11) goto L6;
    }

    private boolean forceSettleCapturedViewAt(int r11, int r12, int r13, int r14) {
        int r2 = this.mCapturedView.getLeft();
        int r3 = this.mCapturedView.getTop();
        int r4 = r11 - r2;
        int r5 = r12 - r3;
        if (r4 != 0) goto L7;
        if (r5 != 0) goto L7;
        this.mScroller.abortAnimation();
        setDragState(0);
        return false;
    L7:
        int r15 = computeSettleDuration(this.mCapturedView, r4, r5, r13, r14);
        this.mScroller.startScroll(r2, r3, r4, r5, r15);
        setDragState(2);
        return true;
    }

    private int getEdgesTouched(int r4, int r5) {
        int r0 = 0;
        if (r4 >= (this.mParentView.getLeft() + this.mEdgeSize)) goto L6;
        r0 = 0 | 1;
    L6:
        if (r5 >= (this.mParentView.getTop() + this.mEdgeSize)) goto L9;
        r0 = r0 | 4;
    L9:
        if (r4 <= (this.mParentView.getRight() - this.mEdgeSize)) goto L12;
        r0 = r0 | 2;
    L12:
        if (r5 > (this.mParentView.getBottom() - this.mEdgeSize)) goto L14;
        return r0;
    L14:
        return r0 | 8;
    }

    private boolean isValidPointerForActionMove(int r3) {
        if (isPointerDown(r3) == true) goto L6;
        Log.e(TAG, "Ignoring pointerId=" + r3 + " because ACTION_DOWN was not received for this pointer before ACTION_MOVE. It likely happened because  ViewDragHelper did not receive all the events in the event stream.");
        return false;
    L6:
        return true;
    }

    private void releaseViewForPointerUp() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        dispatchViewReleased(clampMag(this.mVelocityTracker.getXVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), clampMag(this.mVelocityTracker.getYVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
    }

    private void reportNewEdgeDrags(float r4, float r5, int r6) {
        int r0 = 0;
        if (checkNewEdgeDrag(r4, r5, r6, 1) == false) goto L6;
        r0 = 0 | 1;
    L6:
        if (checkNewEdgeDrag(r5, r4, r6, 4) == false) goto L9;
        r0 = r0 | 4;
    L9:
        if (checkNewEdgeDrag(r4, r5, r6, 2) == false) goto L12;
        r0 = r0 | 2;
    L12:
        if (checkNewEdgeDrag(r5, r4, r6, 8) == false) goto L14;
        r0 = r0 | 8;
    L14:
        if (r0 == 0) goto L17;
        int[] r1 = this.mEdgeDragsInProgress;
        r1[r6] = r1[r6] | r0;
        this.mCallback.onEdgeDragStarted(r0, r6);
        return;
    }

    private void saveInitialMotion(float r4, float r5, int r6) {
        ensureMotionHistorySizeForId(r6);
        float[] r0 = this.mInitialMotionX;
        this.mLastMotionX[r6] = r4;
        r0[r6] = r4;
        float[] r1 = this.mInitialMotionY;
        this.mLastMotionY[r6] = r5;
        r1[r6] = r5;
        this.mInitialEdgesTouched[r6] = getEdgesTouched((int) r4, (int) r5);
        this.mPointersDown |= 1 << r6;
    }

    private void saveLastMotion(MotionEvent r7) {
        int r0 = r7.getPointerCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L9;
        int r2 = r7.getPointerId(r1);
        if (isValidPointerForActionMove(r2) == false) goto L8;
        float r3 = r7.getX(r1);
        float r4 = r7.getY(r1);
        this.mLastMotionX[r2] = r3;
        this.mLastMotionY[r2] = r4;
    L8:
        r1 = r1 + 1;
        goto L3
    }

    public void abort() {
        cancel();
        if (this.mDragState != 2) goto L5;
        int r0 = this.mScroller.getCurrX();
        int r1 = this.mScroller.getCurrY();
        this.mScroller.abortAnimation();
        int r5 = this.mScroller.getCurrX();
        int r6 = this.mScroller.getCurrY();
        this.mCallback.onViewPositionChanged(this.mCapturedView, r5, r6, r5 - r0, r6 - r1);
    L5:
        setDragState(0);
    }

    protected boolean canScroll(View r14, boolean r15, int r16, int r17, int r18, int r19) {
        if ((r14 instanceof ViewGroup) == false) goto L18;
        ViewGroup r0 = (ViewGroup) r14;
        int r2 = r14.getScrollX();
        int r3 = r14.getScrollY();
        int r5 = r0.getChildCount() - 1;
    L5:
        if (r5 < 0) goto L18;
        View r7 = r0.getChildAt(r5);
        if ((r18 + r2) < r7.getLeft()) goto L17;
        if ((r18 + r2) >= r7.getRight()) goto L17;
        if ((r19 + r3) < r7.getTop()) goto L17;
        if ((r19 + r3) >= r7.getBottom()) goto L17;
        if (canScroll(r7, true, r16, r17, (r18 + r2) - r7.getLeft(), (r19 + r3) - r7.getTop()) == false) goto L17;
        return true;
    L17:
        r5 = r5 - 1;
    L18:
        if (r15 == false) goto L26;
        if (r14.canScrollHorizontally(-r16) == true) goto L35;
        if (r14.canScrollVertically(-r17) == false) goto L26;
        return true;
    L35:
        return true;
    L26:
        return false;
    }

    public void cancel() {
        this.mActivePointerId = -1;
        clearMotionHistory();
        if (this.mVelocityTracker == null) goto L6;
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
        return;
    }

    public void captureChildView(View r4, int r5) {
        if (r4.getParent() != this.mParentView) goto L7;
        this.mCapturedView = r4;
        this.mActivePointerId = r5;
        this.mCallback.onViewCaptured(r4, r5);
        setDragState(1);
        return;
    L7:
        throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (" + this.mParentView + ")");
    }

    public boolean checkTouchSlop(int r4) {
        int r0 = this.mInitialMotionX.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L9;
        if (checkTouchSlop(r4, r1) == true) goto L6;
        r1 = r1 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    public boolean checkTouchSlop(int r10, int r11) {
        if (isPointerDown(r11) == true) goto L6;
        return false;
    L6:
        if ((r10 & 1) != 1) goto L8;
        boolean r0 = true;
    L10:
        if ((r10 & 2) != 2) goto L12;
        boolean r3 = true;
    L13:
        float r4 = this.mLastMotionX[r11] - this.mInitialMotionX[r11];
        float r5 = this.mLastMotionY[r11] - this.mInitialMotionY[r11];
        if (r0 == false) goto L20;
        if (r3 == false) goto L20;
        if (((r4 * r4) + (r5 * r5)) > (this.mTouchSlop * this.mTouchSlop)) goto L19;
        return false;
    L19:
        return true;
    L20:
        if (r0 == true) goto L22;
        if (r3 == true) goto L27;
        return false;
    L27:
        if (Math.abs(r5) > this.mTouchSlop) goto L29;
        return false;
    L29:
        return true;
    L22:
        if (Math.abs(r4) > this.mTouchSlop) goto L24;
        return false;
    L24:
        return true;
    L12:
        r3 = false;
        goto L13
    L8:
        r0 = false;
        goto L10
    }

    public boolean continueSettling(boolean r11) {
        if (this.mDragState != 2) goto L23;
        boolean r0 = this.mScroller.computeScrollOffset();
        int r6 = this.mScroller.getCurrX();
        int r7 = this.mScroller.getCurrY();
        int r8 = r6 - this.mCapturedView.getLeft();
        int r9 = r7 - this.mCapturedView.getTop();
        if (r8 == 0) goto L7;
        ViewCompat.offsetLeftAndRight(this.mCapturedView, r8);
    L7:
        if (r9 == 0) goto L9;
        ViewCompat.offsetTopAndBottom(this.mCapturedView, r9);
    L9:
        if (r8 != 0) goto L11;
        if (r9 != 0) goto L11;
    L12:
        if (r0 == true) goto L14;
    L18:
        if (r0 == true) goto L23;
        if (r11 == false) goto L21;
        this.mParentView.post(this.mSetIdleRunnable);
        goto L23
    L21:
        setDragState(0);
        goto L23
    L14:
        if (r6 != this.mScroller.getFinalX()) goto L18;
        if (r7 != this.mScroller.getFinalY()) goto L18;
        this.mScroller.abortAnimation();
        r0 = false;
    L11:
        this.mCallback.onViewPositionChanged(this.mCapturedView, r6, r7, r8, r9);
    L23:
        if (this.mDragState != 2) goto L26;
        return true;
    L26:
        return false;
    }

    public View findTopChildUnder(int r5, int r6) {
        int r1 = this.mParentView.getChildCount() - 1;
    L3:
        if (r1 < 0) goto L14;
        View r2 = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(r1));
        if (r5 < r2.getLeft()) goto L13;
        if (r5 >= r2.getRight()) goto L13;
        if (r6 < r2.getTop()) goto L13;
        if (r6 >= r2.getBottom()) goto L13;
        return r2;
    L13:
        r1 = r1 - 1;
        goto L3
    L14:
        return null;
    }

    public void flingCapturedView(int r11, int r12, int r13, int r14) {
        if (this.mReleaseInProgress == false) goto L7;
        this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int) this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int) this.mVelocityTracker.getYVelocity(this.mActivePointerId), r11, r13, r12, r14);
        setDragState(2);
        return;
    L7:
        throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
    }

    public int getActivePointerId() {
        return this.mActivePointerId;
    }

    public View getCapturedView() {
        return this.mCapturedView;
    }

    public int getEdgeSize() {
        return this.mEdgeSize;
    }

    public float getMinVelocity() {
        return this.mMinVelocity;
    }

    public int getTouchSlop() {
        return this.mTouchSlop;
    }

    public int getViewDragState() {
        return this.mDragState;
    }

    public boolean isCapturedViewUnder(int r2, int r3) {
        return isViewUnder(this.mCapturedView, r2, r3);
    }

    public boolean isEdgeTouched(int r4) {
        int r0 = this.mInitialEdgesTouched.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L9;
        if (isEdgeTouched(r4, r1) == true) goto L6;
        r1 = r1 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    public boolean isEdgeTouched(int r2, int r3) {
        if (isPointerDown(r3) == true) goto L5;
    L7:
        return false;
    L5:
        if ((this.mInitialEdgesTouched[r3] & r2) == 0) goto L7;
        return true;
    }

    public boolean isPointerDown(int r4) {
        if ((this.mPointersDown & (1 << r4)) != 0) goto L7;
        return false;
    L7:
        return true;
    }

    public boolean isViewUnder(View r3, int r4, int r5) {
        if (r3 != null) goto L6;
        return false;
    L6:
        if (r4 >= r3.getLeft()) goto L8;
        return false;
    L8:
        if (r4 < r3.getRight()) goto L10;
        return false;
    L10:
        if (r5 >= r3.getTop()) goto L12;
        return false;
    L12:
        if (r5 >= r3.getBottom()) goto L19;
        return true;
    L19:
        return false;
    }

    public void processTouchEvent(MotionEvent r13) {
        int r0 = r13.getActionMasked();
        int r1 = r13.getActionIndex();
        if (r0 != 0) goto L6;
        cancel();
    L6:
        if (this.mVelocityTracker != null) goto L8;
        this.mVelocityTracker = VelocityTracker.obtain();
    L8:
        this.mVelocityTracker.addMovement(r13);
        switch(r0) {
            case 0: goto L68;
            case 1: goto L65;
            case 2: goto L44;
            case 3: goto L40;
            case 4: goto L90;
            case 5: goto L30;
            case 6: goto L11;
            default: goto L90;
        };
    L11:
        int r3 = r13.getPointerId(r1);
        if (this.mDragState == 1) goto L14;
    L29:
        clearMotionHistory(r3);
        return;
    L14:
        if (r3 != this.mActivePointerId) goto L29;
        int r2 = -1;
        int r4 = r13.getPointerCount();
        int r5 = 0;
    L16:
        if (r5 >= r4) goto L27;
        int r6 = r13.getPointerId(r5);
        if (r6 == this.mActivePointerId) goto L25;
        float r7 = r13.getX(r5);
        if (findTopChildUnder((int) r7, (int) r13.getY(r5)) != this.mCapturedView) goto L25;
        if (tryCaptureViewForDrag(this.mCapturedView, r6) == false) goto L25;
        r2 = this.mActivePointerId;
    L25:
        r5 = r5 + 1;
    L27:
        if (r2 != (-1)) goto L29;
        releaseViewForPointerUp();
        goto L29
    L30:
        int r8 = r13.getPointerId(r1);
        float r9 = r13.getX(r1);
        float r10 = r13.getY(r1);
        saveInitialMotion(r9, r10, r8);
        if (this.mDragState != 0) goto L36;
        tryCaptureViewForDrag(findTopChildUnder((int) r9, (int) r10), r8);
        int r11 = this.mInitialEdgesTouched[r8];
        if ((this.mTrackingEdges & r11) == 0) goto L91;
        this.mCallback.onEdgeTouched(this.mTrackingEdges & r11, r8);
        return;
    L91:
        return;
    L36:
        if (isCapturedViewUnder((int) r9, (int) r10) == false) goto L93;
        tryCaptureViewForDrag(this.mCapturedView, r8);
        return;
    L93:
        return;
    L68:
        float r12 = r13.getX();
        float r14 = r13.getY();
        int r15 = r13.getPointerId(0);
        View r16 = findTopChildUnder((int) r12, (int) r14);
        saveInitialMotion(r12, r14, r15);
        tryCaptureViewForDrag(r16, r15);
        int r17 = this.mInitialEdgesTouched[r15];
        if ((this.mTrackingEdges & r17) == 0) goto L89;
        this.mCallback.onEdgeTouched(this.mTrackingEdges & r17, r15);
        return;
    L89:
        return;
    L90:
        return;
    L40:
        if (this.mDragState != 1) goto L42;
        dispatchViewReleased(0.0f, 0.0f);
    L42:
        cancel();
        return;
    L44:
        if (this.mDragState == 1) goto L46;
        int r18 = r13.getPointerCount();
        int r19 = 0;
    L50:
        if (r19 >= r18) goto L63;
        int r20 = r13.getPointerId(r19);
        if (isValidPointerForActionMove(r20) == false) goto L62;
        float r21 = r13.getX(r19);
        float r22 = r13.getY(r19);
        float r23 = r21 - this.mInitialMotionX[r20];
        float r24 = r22 - this.mInitialMotionY[r20];
        reportNewEdgeDrags(r23, r24, r20);
        if (this.mDragState == 1) goto L63;
        View r110 = findTopChildUnder((int) r21, (int) r22);
        if (checkTouchSlop(r110, r23, r24) == false) goto L62;
        if (tryCaptureViewForDrag(r110, r20) == true) goto L63;
    L62:
        r19 = r19 + 1;
    L63:
        saveLastMotion(r13);
        return;
    L46:
        if (isValidPointerForActionMove(this.mActivePointerId) == false) goto L94;
        int r25 = r13.findPointerIndex(this.mActivePointerId);
        float r26 = r13.getX(r25);
        float r27 = r13.getY(r25);
        int r28 = (int) (r26 - this.mLastMotionX[this.mActivePointerId]);
        int r29 = (int) (r27 - this.mLastMotionY[this.mActivePointerId]);
        dragTo(this.mCapturedView.getLeft() + r28, this.mCapturedView.getTop() + r29, r28, r29);
        saveLastMotion(r13);
        return;
    L94:
        return;
    L65:
        if (this.mDragState != 1) goto L67;
        releaseViewForPointerUp();
    L67:
        cancel();
    }

    void setDragState(int r3) {
        this.mParentView.removeCallbacks(this.mSetIdleRunnable);
        if (this.mDragState == r3) goto L8;
        this.mDragState = r3;
        this.mCallback.onViewDragStateChanged(r3);
        if (this.mDragState != 0) goto L9;
        this.mCapturedView = null;
        return;
    L9:
        return;
    }

    public void setEdgeTrackingEnabled(int r1) {
        this.mTrackingEdges = r1;
    }

    public void setMinVelocity(float r1) {
        this.mMinVelocity = r1;
    }

    public boolean settleCapturedViewAt(int r4, int r5) {
        if (this.mReleaseInProgress == false) goto L7;
        return forceSettleCapturedViewAt(r4, r5, (int) this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int) this.mVelocityTracker.getYVelocity(this.mActivePointerId));
    L7:
        throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
    }

    public boolean shouldInterceptTouchEvent(MotionEvent r22) {
        int r2 = r22.getActionMasked();
        int r3 = r22.getActionIndex();
        if (r2 != 0) goto L6;
        cancel();
    L6:
        if (this.mVelocityTracker != null) goto L8;
        this.mVelocityTracker = VelocityTracker.obtain();
    L8:
        this.mVelocityTracker.addMovement(r22);
        switch(r2) {
            case 0: goto L62;
            case 1: goto L61;
            case 2: goto L25;
            case 3: goto L61;
            case 4: goto L10;
            case 5: goto L12;
            case 6: goto L11;
            default: goto L10;
        };
    L10:
        boolean r5 = false;
    L71:
        if (this.mDragState == 1) goto L73;
        return r5;
    L73:
        return true;
    L11:
        clearMotionHistory(r22.getPointerId(r3));
        r5 = false;
        goto L71
    L12:
        int r7 = r22.getPointerId(r3);
        float r8 = r22.getX(r3);
        float r9 = r22.getY(r3);
        saveInitialMotion(r8, r9, r7);
        if (this.mDragState != 0) goto L18;
        int r4 = this.mInitialEdgesTouched[r7];
        if ((this.mTrackingEdges & r4) == 0) goto L23;
        this.mCallback.onEdgeTouched(this.mTrackingEdges & r4, r7);
    L23:
        r5 = false;
        goto L71
    L18:
        if (this.mDragState != 2) goto L23;
        View r6 = findTopChildUnder((int) r8, (int) r9);
        if (r6 != this.mCapturedView) goto L22;
        tryCaptureViewForDrag(r6, r7);
    L22:
        r5 = false;
        goto L71
    L61:
        cancel();
        r5 = false;
        goto L71
    L62:
        float r10 = r22.getX();
        float r11 = r22.getY();
        r5 = false;
        int r12 = r22.getPointerId(0);
        saveInitialMotion(r10, r11, r12);
        View r13 = findTopChildUnder((int) r10, (int) r11);
        if (r13 == this.mCapturedView) goto L65;
    L67:
        int r14 = this.mInitialEdgesTouched[r12];
        if ((this.mTrackingEdges & r14) == 0) goto L71;
        this.mCallback.onEdgeTouched(this.mTrackingEdges & r14, r12);
        goto L71
    L65:
        if (this.mDragState != 2) goto L67;
        tryCaptureViewForDrag(r13, r12);
        goto L67
    L25:
        if (this.mInitialMotionX != null) goto L27;
        r5 = false;
        goto L71
    L27:
        if (this.mInitialMotionY != null) goto L29;
        r5 = false;
        goto L71
    L29:
        int r15 = r22.getPointerCount();
        int r16 = 0;
    L30:
        if (r16 >= r15) goto L59;
        int r17 = r22.getPointerId(r16);
        if (isValidPointerForActionMove(r17) == true) goto L34;
        int r18 = r2;
        int r19 = r3;
        int r110 = r15;
    L57:
        r16 = r16 + 1;
        r2 = r18;
        r3 = r19;
        r15 = r110;
        goto L30
    L34:
        float r20 = r22.getX(r16);
        float r111 = r22.getY(r16);
        float r112 = r20 - this.mInitialMotionX[r17];
        float r113 = r111 - this.mInitialMotionY[r17];
        View r114 = findTopChildUnder((int) r20, (int) r111);
        if (r114 != null) goto L37;
    L39:
        boolean r115 = false;
    L40:
        if (r115 == false) goto L49;
        int r116 = r114.getLeft();
        Callback r21 = this.mCallback;
        r18 = r2;
        int r23 = r21.clampViewPositionHorizontal(r114, ((int) r112) + r116, (int) r112);
        int r24 = r114.getTop();
        r19 = r3;
        r110 = r15;
        Callback r25 = this.mCallback;
        int r26 = r25.clampViewPositionVertical(r114, ((int) r113) + r24, (int) r113);
        int r27 = this.mCallback.getViewHorizontalDragRange(r114);
        int r28 = this.mCallback.getViewVerticalDragRange(r114);
        if (r27 == 0) goto L45;
        if (r27 <= 0) goto L50;
        if (r23 == r116) goto L45;
    L50:
        reportNewEdgeDrags(r112, r113, r17);
        if (this.mDragState == 1) goto L59;
        if (r115 == false) goto L57;
        if (tryCaptureViewForDrag(r114, r17) == false) goto L57;
    L45:
        if (r28 == 0) goto L59;
        if (r28 <= 0) goto L50;
        if (r26 != r24) goto L50;
    L49:
        r18 = r2;
        r19 = r3;
        r110 = r15;
        goto L50
    L37:
        if (checkTouchSlop(r114, r112, r113) == false) goto L39;
        r115 = true;
    L59:
        saveLastMotion(r22);
        r5 = false;
        goto L71
    }

    public boolean smoothSlideViewTo(View r3, int r4, int r5) {
        this.mCapturedView = r3;
        this.mActivePointerId = -1;
        boolean r0 = forceSettleCapturedViewAt(r4, r5, 0, 0);
        if (r0 == false) goto L5;
    L9:
        return r0;
    L5:
        if (this.mDragState != 0) goto L9;
        if (this.mCapturedView == null) goto L9;
        this.mCapturedView = null;
        goto L9
    }

    boolean tryCaptureViewForDrag(View r3, int r4) {
        if (r3 == this.mCapturedView) goto L5;
    L7:
        if (r3 != null) goto L9;
        return false;
    L9:
        if (this.mCallback.tryCaptureView(r3, r4) == false) goto L14;
        this.mActivePointerId = r4;
        captureChildView(r3, r4);
        return true;
    L14:
        return false;
    L5:
        if (this.mActivePointerId != r4) goto L7;
        return true;
    }
}
