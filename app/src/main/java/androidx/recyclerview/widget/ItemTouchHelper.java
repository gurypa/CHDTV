package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.C0472R;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ItemTouchHelper extends RecyclerView.ItemDecoration implements RecyclerView.OnChildAttachStateChangeListener {
    static final int ACTION_MODE_DRAG_MASK = 16711680;
    private static final int ACTION_MODE_IDLE_MASK = 255;
    static final int ACTION_MODE_SWIPE_MASK = 65280;
    public static final int ACTION_STATE_DRAG = 2;
    public static final int ACTION_STATE_IDLE = 0;
    public static final int ACTION_STATE_SWIPE = 1;
    private static final int ACTIVE_POINTER_ID_NONE = -1;
    public static final int ANIMATION_TYPE_DRAG = 8;
    public static final int ANIMATION_TYPE_SWIPE_CANCEL = 4;
    public static final int ANIMATION_TYPE_SWIPE_SUCCESS = 2;
    private static final boolean DEBUG = false;
    static final int DIRECTION_FLAG_COUNT = 8;
    public static final int DOWN = 2;
    public static final int END = 32;
    public static final int LEFT = 4;
    private static final int PIXELS_PER_SECOND = 1000;
    public static final int RIGHT = 8;
    public static final int START = 16;
    private static final String TAG = "ItemTouchHelper";

    /* JADX INFO: renamed from: UP */
    public static final int f71UP = 1;
    private int mActionState;
    int mActivePointerId;
    Callback mCallback;
    private RecyclerView.ChildDrawingOrderCallback mChildDrawingOrderCallback;
    private List<Integer> mDistances;
    private long mDragScrollStartTimeInMs;
    float mDx;
    float mDy;
    GestureDetectorCompat mGestureDetector;
    float mInitialTouchX;
    float mInitialTouchY;
    private ItemTouchHelperGestureListener mItemTouchHelperGestureListener;
    private float mMaxSwipeVelocity;
    private final RecyclerView.OnItemTouchListener mOnItemTouchListener;
    View mOverdrawChild;
    int mOverdrawChildPosition;
    final List<View> mPendingCleanup;
    List<RecoverAnimation> mRecoverAnimations;
    RecyclerView mRecyclerView;
    final Runnable mScrollRunnable;
    RecyclerView.ViewHolder mSelected;
    int mSelectedFlags;
    private float mSelectedStartX;
    private float mSelectedStartY;
    private int mSlop;
    private List<RecyclerView.ViewHolder> mSwapTargets;
    private float mSwipeEscapeVelocity;
    private final float[] mTmpPosition;
    private Rect mTmpRect;
    VelocityTracker mVelocityTracker;

    /* JADX INFO: renamed from: androidx.recyclerview.widget.ItemTouchHelper$5 */
    class C04925 implements RecyclerView.ChildDrawingOrderCallback {
        final /* synthetic */ ItemTouchHelper this$0;

        C04925(ItemTouchHelper r1) {
            this.this$0 = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ChildDrawingOrderCallback
        public int onGetChildDrawingOrder(int r4, int r5) {
            if (this.this$0.mOverdrawChild != null) goto L5;
            return r5;
        L5:
            int r0 = this.this$0.mOverdrawChildPosition;
            if (r0 != (-1)) goto L9;
            r0 = this.this$0.mRecyclerView.indexOfChild(this.this$0.mOverdrawChild);
            this.this$0.mOverdrawChildPosition = r0;
        L9:
            if (r5 != (r4 - 1)) goto L11;
            return r0;
        L11:
            if (r5 >= r0) goto L14;
            return r5;
        L14:
            return r5 + 1;
        }
    }

    public static abstract class Callback {
        private static final int ABS_HORIZONTAL_DIR_FLAGS = 789516;
        public static final int DEFAULT_DRAG_ANIMATION_DURATION = 200;
        public static final int DEFAULT_SWIPE_ANIMATION_DURATION = 250;
        private static final long DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS = 2000;
        static final int RELATIVE_DIR_FLAGS = 3158064;
        private static final Interpolator sDragScrollInterpolator = null;
        private static final Interpolator sDragViewScrollCapInterpolator = null;
        private int mCachedMaxScrollSpeed;

        static {
            sDragScrollInterpolator = new InterpolatorC04931();
            sDragViewScrollCapInterpolator = new InterpolatorC04942();
        }

        public Callback() {
            this.mCachedMaxScrollSpeed = -1;
        }

        public static int convertToRelativeDirection(int r4, int r5) {
            int r1 = r4 & ABS_HORIZONTAL_DIR_FLAGS;
            if (r1 != 0) goto L5;
            return r4;
        L5:
            int r6 = r4 & (~r1);
            if (r5 != 0) goto L10;
            return r6 | (r1 << 2);
        L10:
            return (r6 | ((r1 << 1) & (-789517))) | ((ABS_HORIZONTAL_DIR_FLAGS & (r1 << 1)) << 2);
        }

        public static ItemTouchUIUtil getDefaultUIUtil() {
            return ItemTouchUIUtilImpl.INSTANCE;
        }

        private int getMaxDragScroll(RecyclerView r3) {
            if (this.mCachedMaxScrollSpeed != (-1)) goto L6;
            this.mCachedMaxScrollSpeed = r3.getResources().getDimensionPixelSize(C0472R.dimen.item_touch_helper_max_drag_scroll_per_frame);
        L6:
            return this.mCachedMaxScrollSpeed;
        }

        public static int makeFlag(int r1, int r2) {
            return r2 << (r1 * 8);
        }

        public static int makeMovementFlags(int r2, int r3) {
            return (makeFlag(0, r3 | r2) | makeFlag(1, r3)) | makeFlag(2, r2);
        }

        public boolean canDropOver(RecyclerView r2, RecyclerView.ViewHolder r3, RecyclerView.ViewHolder r4) {
            return true;
        }

        public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder r13, List<RecyclerView.ViewHolder> r14, int r15, int r16) {
            int r0 = r13.itemView.getWidth() + r15;
            int r1 = r16 + r13.itemView.getHeight();
            RecyclerView.ViewHolder r2 = null;
            int r3 = -1;
            int r4 = r15 - r13.itemView.getLeft();
            int r5 = r16 - r13.itemView.getTop();
            int r6 = r14.size();
            int r7 = 0;
        L3:
            if (r7 >= r6) goto L38;
            RecyclerView.ViewHolder r8 = r14.get(r7);
            if (r4 <= 0) goto L13;
            int r9 = r8.itemView.getRight() - r0;
            if (r9 >= 0) goto L13;
            if (r8.itemView.getRight() <= r13.itemView.getRight()) goto L13;
            int r10 = Math.abs(r9);
            if (r10 <= r3) goto L13;
            r3 = r10;
            r2 = r8;
        L13:
            if (r4 >= 0) goto L21;
            int r11 = r8.itemView.getLeft() - r15;
            if (r11 <= 0) goto L21;
            if (r8.itemView.getLeft() >= r13.itemView.getLeft()) goto L21;
            int r12 = Math.abs(r11);
            if (r12 <= r3) goto L21;
            r3 = r12;
            r2 = r8;
        L21:
            if (r5 >= 0) goto L29;
            int r17 = r8.itemView.getTop() - r16;
            if (r17 <= 0) goto L29;
            if (r8.itemView.getTop() >= r13.itemView.getTop()) goto L29;
            int r18 = Math.abs(r17);
            if (r18 <= r3) goto L29;
            r3 = r18;
            r2 = r8;
        L29:
            if (r5 <= 0) goto L37;
            int r19 = r8.itemView.getBottom() - r1;
            if (r19 >= 0) goto L37;
            if (r8.itemView.getBottom() <= r13.itemView.getBottom()) goto L37;
            int r110 = Math.abs(r19);
            if (r110 <= r3) goto L37;
            r3 = r110;
            r2 = r8;
        L37:
            r7 = r7 + 1;
            goto L3
        L38:
            return r2;
        }

        public void clearView(RecyclerView r3, RecyclerView.ViewHolder r4) {
            ItemTouchUIUtilImpl.INSTANCE.clearView(r4.itemView);
        }

        public int convertToAbsoluteDirection(int r5, int r6) {
            int r1 = r5 & RELATIVE_DIR_FLAGS;
            if (r1 != 0) goto L5;
            return r5;
        L5:
            int r7 = r5 & (~r1);
            if (r6 != 0) goto L10;
            return r7 | (r1 >> 2);
        L10:
            return (r7 | ((r1 >> 1) & (-3158065))) | ((RELATIVE_DIR_FLAGS & (r1 >> 1)) >> 2);
        }

        final int getAbsoluteMovementFlags(RecyclerView r3, RecyclerView.ViewHolder r4) {
            return convertToAbsoluteDirection(getMovementFlags(r3, r4), ViewCompat.getLayoutDirection(r3));
        }

        public long getAnimationDuration(RecyclerView r4, int r5, float r6, float r7) {
            RecyclerView.ItemAnimator r0 = r4.getItemAnimator();
            if (r0 != null) goto L8;
            if (r5 != 8) goto L6;
            return 200;
        L6:
            return 250;
        L8:
            if (r5 != 8) goto L11;
            return r0.getMoveDuration();
        L11:
            return r0.getRemoveDuration();
        }

        public int getBoundingBoxMargin() {
            return 0;
        }

        public float getMoveThreshold(RecyclerView.ViewHolder r2) {
            return 0.5f;
        }

        public abstract int getMovementFlags(RecyclerView r1, RecyclerView.ViewHolder r2);

        public float getSwipeEscapeVelocity(float r1) {
            return r1;
        }

        public float getSwipeThreshold(RecyclerView.ViewHolder r2) {
            return 0.5f;
        }

        public float getSwipeVelocityThreshold(float r1) {
            return r1;
        }

        boolean hasDragFlag(RecyclerView r3, RecyclerView.ViewHolder r4) {
            if ((ItemTouchHelper.ACTION_MODE_DRAG_MASK & getAbsoluteMovementFlags(r3, r4)) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        boolean hasSwipeFlag(RecyclerView r3, RecyclerView.ViewHolder r4) {
            if ((65280 & getAbsoluteMovementFlags(r3, r4)) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        public int interpolateOutOfBoundsScroll(RecyclerView r9, int r10, int r11, int r12, long r13) {
            int r0 = getMaxDragScroll(r9);
            int r1 = Math.abs(r11);
            int r4 = (int) ((((int) Math.signum(r11)) * r0) * sDragViewScrollCapInterpolator.getInterpolation(Math.min(1.0f, (r1 * 1.0f) / r10)));
            if (r13 <= 2000) goto L5;
            float r5 = 1.0f;
        L6:
            int r6 = (int) (r4 * sDragScrollInterpolator.getInterpolation(r5));
            if (r6 != 0) goto L12;
            if (r11 <= 0) goto L10;
            return 1;
        L10:
            return -1;
        L12:
            return r6;
        L5:
            r5 = r13 / 2000.0f;
            goto L6
        }

        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        public boolean isLongPressDragEnabled() {
            return true;
        }

        public void onChildDraw(Canvas r9, RecyclerView r10, RecyclerView.ViewHolder r11, float r12, float r13, int r14, boolean r15) {
            ItemTouchUIUtilImpl.INSTANCE.onDraw(r9, r10, r11.itemView, r12, r13, r14, r15);
        }

        public void onChildDrawOver(Canvas r9, RecyclerView r10, RecyclerView.ViewHolder r11, float r12, float r13, int r14, boolean r15) {
            ItemTouchUIUtilImpl.INSTANCE.onDrawOver(r9, r10, r11.itemView, r12, r13, r14, r15);
        }

        void onDraw(Canvas r14, RecyclerView r15, RecyclerView.ViewHolder r16, List<RecoverAnimation> r17, int r18, float r19, float r20) {
            int r8 = r17.size();
            int r9 = 0;
        L3:
            if (r9 >= r8) goto L6;
            RecoverAnimation r11 = r17.get(r9);
            r11.update();
            int r12 = r14.save();
            onChildDraw(r14, r15, r11.mViewHolder, r11.f72mX, r11.f73mY, r11.mActionState, false);
            r14.restoreToCount(r12);
            r9 = r9 + 1;
            goto L3
        L6:
            if (r16 == null) goto L10;
            int r10 = r14.save();
            onChildDraw(r14, r15, r16, r19, r20, r18, true);
            r14.restoreToCount(r10);
            return;
        }

        void onDrawOver(Canvas r14, RecyclerView r15, RecyclerView.ViewHolder r16, List<RecoverAnimation> r17, int r18, float r19, float r20) {
            int r9 = r17.size();
            int r10 = 0;
        L3:
            if (r10 >= r9) goto L5;
            RecoverAnimation r11 = r17.get(r10);
            int r12 = r14.save();
            onChildDrawOver(r14, r15, r11.mViewHolder, r11.f72mX, r11.f73mY, r11.mActionState, false);
            r14.restoreToCount(r12);
            r10 = r10 + 1;
            goto L3
        L5:
            if (r16 == null) goto L7;
            int r13 = r14.save();
            onChildDrawOver(r14, r15, r16, r19, r20, r18, true);
            r14.restoreToCount(r13);
        L7:
            boolean r0 = false;
            int r2 = r9 - 1;
        L8:
            if (r2 < 0) goto L18;
            RecoverAnimation r3 = r17.get(r2);
            if (r3.mEnded == false) goto L15;
            if (r3.mIsPendingCleanup == true) goto L15;
            r17.remove(r2);
        L17:
            r2 = r2 - 1;
        L15:
            if (r3.mEnded == true) goto L17;
            r0 = true;
            goto L17
        L18:
            if (r0 == false) goto L26;
            r15.invalidate();
            return;
        }

        public abstract boolean onMove(RecyclerView r1, RecyclerView.ViewHolder r2, RecyclerView.ViewHolder r3);

        /* JADX WARN: Multi-variable type inference failed */
        public void onMoved(RecyclerView r6, RecyclerView.ViewHolder r7, int r8, RecyclerView.ViewHolder r9, int r10, int r11, int r12) {
            RecyclerView.LayoutManager r0 = r6.getLayoutManager();
            if ((r0 instanceof ViewDropHandler) == false) goto L7;
            ((ViewDropHandler) r0).prepareForDrop(r7.itemView, r9.itemView, r11, r12);
            return;
        L7:
            if (r0.canScrollHorizontally() == false) goto L15;
            if (r0.getDecoratedLeft(r9.itemView) > r6.getPaddingLeft()) goto L12;
            r6.scrollToPosition(r10);
        L12:
            if (r0.getDecoratedRight(r9.itemView) < (r6.getWidth() - r6.getPaddingRight())) goto L15;
            r6.scrollToPosition(r10);
        L15:
            if (r0.canScrollVertically() == true) goto L17;
            return;
        L17:
            if (r0.getDecoratedTop(r9.itemView) > r6.getPaddingTop()) goto L20;
            r6.scrollToPosition(r10);
        L20:
            if (r0.getDecoratedBottom(r9.itemView) < (r6.getHeight() - r6.getPaddingBottom())) goto L24;
            r6.scrollToPosition(r10);
            return;
        }

        public void onSelectedChanged(RecyclerView.ViewHolder r3, int r4) {
            if (r3 == null) goto L5;
            ItemTouchUIUtilImpl.INSTANCE.onSelected(r3.itemView);
            return;
        }

        public abstract void onSwiped(RecyclerView.ViewHolder r1, int r2);
    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean mShouldReactToLongPress;
        final /* synthetic */ ItemTouchHelper this$0;

        ItemTouchHelperGestureListener(ItemTouchHelper r1) {
            this.this$0 = r1;
            this.mShouldReactToLongPress = true;
        }

        void doNotReactToLongPress() {
            this.mShouldReactToLongPress = false;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent r2) {
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent r10) {
            if (this.mShouldReactToLongPress == true) goto L5;
            return;
        L5:
            View r0 = this.this$0.findChildView(r10);
            if (r0 == null) goto L18;
            RecyclerView.ViewHolder r1 = this.this$0.mRecyclerView.getChildViewHolder(r0);
            if (r1 != null) goto L10;
            return;
        L10:
            if (this.this$0.mCallback.hasDragFlag(this.this$0.mRecyclerView, r1) == true) goto L13;
            return;
        L13:
            if (r10.getPointerId(0) != this.this$0.mActivePointerId) goto L20;
            int r3 = r10.findPointerIndex(this.this$0.mActivePointerId);
            float r4 = r10.getX(r3);
            float r5 = r10.getY(r3);
            this.this$0.mInitialTouchX = r4;
            this.this$0.mInitialTouchY = r5;
            ItemTouchHelper r6 = this.this$0;
            this.this$0.mDy = 0.0f;
            r6.mDx = 0.0f;
            if (this.this$0.mCallback.isLongPressDragEnabled() == false) goto L21;
            this.this$0.select(r1, 2);
            return;
        L21:
            return;
        L20:
            return;
        }
    }

    static class RecoverAnimation implements Animator.AnimatorListener {
        final int mActionState;
        final int mAnimationType;
        boolean mEnded;
        private float mFraction;
        boolean mIsPendingCleanup;
        boolean mOverridden;
        final float mStartDx;
        final float mStartDy;
        final float mTargetX;
        final float mTargetY;
        final ValueAnimator mValueAnimator;
        final RecyclerView.ViewHolder mViewHolder;

        /* JADX INFO: renamed from: mX */
        float f72mX;

        /* JADX INFO: renamed from: mY */
        float f73mY;

        RecoverAnimation(RecyclerView.ViewHolder r3, int r4, int r5, float r6, float r7, float r8, float r9) {
            this.mOverridden = false;
            this.mEnded = false;
            this.mActionState = r5;
            this.mAnimationType = r4;
            this.mViewHolder = r3;
            this.mStartDx = r6;
            this.mStartDy = r7;
            this.mTargetX = r8;
            this.mTargetY = r9;
            this.mValueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.mValueAnimator.addUpdateListener(new C04951(this));
            this.mValueAnimator.setTarget(r3.itemView);
            this.mValueAnimator.addListener(this);
            setFraction(0.0f);
        }

        public void cancel() {
            this.mValueAnimator.cancel();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator r2) {
            setFraction(1.0f);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator r3) {
            if (this.mEnded == true) goto L5;
            this.mViewHolder.setIsRecyclable(true);
        L5:
            this.mEnded = true;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator r1) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator r1) {
        }

        public void setDuration(long r2) {
            this.mValueAnimator.setDuration(r2);
        }

        public void setFraction(float r1) {
            this.mFraction = r1;
        }

        public void start() {
            this.mViewHolder.setIsRecyclable(false);
            this.mValueAnimator.start();
        }

        public void update() {
            if (this.mStartDx != this.mTargetX) goto L5;
            this.f72mX = this.mViewHolder.itemView.getTranslationX();
        L7:
            if (this.mStartDy != this.mTargetY) goto L9;
            this.f73mY = this.mViewHolder.itemView.getTranslationY();
            return;
        L9:
            this.f73mY = this.mStartDy + (this.mFraction * (this.mTargetY - this.mStartDy));
            return;
        L5:
            this.f72mX = this.mStartDx + (this.mFraction * (this.mTargetX - this.mStartDx));
            goto L7
        }
    }

    public static abstract class SimpleCallback extends Callback {
        private int mDefaultDragDirs;
        private int mDefaultSwipeDirs;

        public SimpleCallback(int r1, int r2) {
            this.mDefaultSwipeDirs = r2;
            this.mDefaultDragDirs = r1;
        }

        public int getDragDirs(RecyclerView r2, RecyclerView.ViewHolder r3) {
            return this.mDefaultDragDirs;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView r3, RecyclerView.ViewHolder r4) {
            return makeMovementFlags(getDragDirs(r3, r4), getSwipeDirs(r3, r4));
        }

        public int getSwipeDirs(RecyclerView r2, RecyclerView.ViewHolder r3) {
            return this.mDefaultSwipeDirs;
        }

        public void setDefaultDragDirs(int r1) {
            this.mDefaultDragDirs = r1;
        }

        public void setDefaultSwipeDirs(int r1) {
            this.mDefaultSwipeDirs = r1;
        }
    }

    public interface ViewDropHandler {
        void prepareForDrop(View r1, View r2, int r3, int r4);
    }

    public ItemTouchHelper(Callback r4) {
        this.mPendingCleanup = new ArrayList();
        this.mTmpPosition = new float[2];
        this.mSelected = null;
        this.mActivePointerId = -1;
        this.mActionState = 0;
        this.mRecoverAnimations = new ArrayList();
        this.mScrollRunnable = new RunnableC04881(this);
        this.mChildDrawingOrderCallback = null;
        this.mOverdrawChild = null;
        this.mOverdrawChildPosition = -1;
        this.mOnItemTouchListener = new C04892(this);
        this.mCallback = r4;
    }

    private void addChildDrawingOrderCallback() {
    }

    private int checkHorizontalSwipe(RecyclerView.ViewHolder r8, int r9) {
        if ((r9 & 12) == 0) goto L29;
        int r2 = 8;
        if (this.mDx <= 0.0f) goto L7;
        int r0 = 8;
    L9:
        if (this.mVelocityTracker != null) goto L11;
    L24:
        float r1 = this.mRecyclerView.getWidth() * this.mCallback.getSwipeThreshold(r8);
        if ((r9 & r0) != 0) goto L27;
        return 0;
    L27:
        if (Math.abs(this.mDx) <= r1) goto L32;
        return r0;
    L32:
        return 0;
    L11:
        if (this.mActivePointerId <= (-1)) goto L24;
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
        float r4 = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
        float r5 = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
        if (r4 > 0.0f) goto L16;
        r2 = 4;
    L16:
        float r3 = Math.abs(r4);
        if ((r2 & r9) == 0) goto L24;
        if (r0 != r2) goto L24;
        if (r3 < this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity)) goto L24;
        if (r3 <= Math.abs(r5)) goto L24;
        return r2;
    L7:
        r0 = 4;
        goto L9
    L29:
        return 0;
    }

    private int checkVerticalSwipe(RecyclerView.ViewHolder r8, int r9) {
        if ((r9 & 3) == 0) goto L29;
        int r2 = 2;
        if (this.mDy <= 0.0f) goto L7;
        int r0 = 2;
    L9:
        if (this.mVelocityTracker != null) goto L11;
    L24:
        float r1 = this.mRecyclerView.getHeight() * this.mCallback.getSwipeThreshold(r8);
        if ((r9 & r0) != 0) goto L27;
        return 0;
    L27:
        if (Math.abs(this.mDy) <= r1) goto L32;
        return r0;
    L32:
        return 0;
    L11:
        if (this.mActivePointerId <= (-1)) goto L24;
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
        float r4 = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
        float r5 = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
        if (r5 > 0.0f) goto L16;
        r2 = 1;
    L16:
        float r3 = Math.abs(r5);
        if ((r2 & r9) == 0) goto L24;
        if (r2 != r0) goto L24;
        if (r3 < this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity)) goto L24;
        if (r3 <= Math.abs(r4)) goto L24;
        return r2;
    L7:
        r0 = 1;
        goto L9
    L29:
        return 0;
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration(this);
        this.mRecyclerView.removeOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.removeOnChildAttachStateChangeListener(this);
        int r1 = this.mRecoverAnimations.size() - 1;
    L3:
        if (r1 < 0) goto L5;
        RecoverAnimation r2 = this.mRecoverAnimations.get(0);
        r2.cancel();
        this.mCallback.clearView(this.mRecyclerView, r2.mViewHolder);
        r1 = r1 - 1;
        goto L3
    L5:
        this.mRecoverAnimations.clear();
        this.mOverdrawChild = null;
        this.mOverdrawChildPosition = -1;
        releaseVelocityTracker();
        stopGestureDetection();
    }

    private List<RecyclerView.ViewHolder> findSwapTargets(RecyclerView.ViewHolder r21) {
        RecyclerView.ViewHolder r1 = r21;
        if (this.mSwapTargets != null) goto L5;
        this.mSwapTargets = new ArrayList();
        this.mDistances = new ArrayList();
    L6:
        int r2 = this.mCallback.getBoundingBoxMargin();
        int r3 = Math.round(this.mSelectedStartX + this.mDx) - r2;
        int r4 = Math.round(this.mSelectedStartY + this.mDy) - r2;
        int r5 = (r1.itemView.getWidth() + r3) + (r2 * 2);
        int r6 = (r1.itemView.getHeight() + r4) + (r2 * 2);
        int r7 = (r3 + r5) / 2;
        int r8 = (r4 + r6) / 2;
        RecyclerView.LayoutManager r9 = this.mRecyclerView.getLayoutManager();
        int r10 = r9.getChildCount();
        int r11 = 0;
    L7:
        if (r11 >= r10) goto L34;
        View r12 = r9.getChildAt(r11);
        if (r12 != r1.itemView) goto L12;
    L32:
        r11 = r11 + 1;
        r1 = r21;
        r3 = r3;
        r2 = r2;
        goto L7
    L12:
        if (r12.getBottom() < r4) goto L32;
        if (r12.getTop() > r6) goto L32;
        if (r12.getRight() < r3) goto L32;
        if (r12.getLeft() > r5) goto L32;
        RecyclerView.ViewHolder r13 = this.mRecyclerView.getChildViewHolder(r12);
        if (this.mCallback.canDropOver(this.mRecyclerView, this.mSelected, r13) == false) goto L32;
        int r14 = Math.abs(r7 - ((r12.getLeft() + r12.getRight()) / 2));
        int r15 = Math.abs(r8 - ((r12.getTop() + r12.getBottom()) / 2));
        int r16 = (r14 * r14) + (r15 * r15);
        int r17 = this.mSwapTargets.size();
        int r18 = 0;
        int r19 = 0;
    L23:
        if (r19 >= r17) goto L28;
        int r110 = r17;
        if (r16 <= this.mDistances.get(r19).intValue()) goto L28;
        r18 = r18 + 1;
        r19 = r19 + 1;
        r17 = r110;
    L28:
        this.mSwapTargets.add(r18, r13);
        this.mDistances.add(r18, Integer.valueOf(r16));
        goto L32
    L34:
        return this.mSwapTargets;
    L5:
        this.mSwapTargets.clear();
        this.mDistances.clear();
        goto L6
    }

    private RecyclerView.ViewHolder findSwipedView(MotionEvent r9) {
        RecyclerView.LayoutManager r0 = this.mRecyclerView.getLayoutManager();
        if (this.mActivePointerId != (-1)) goto L5;
        return null;
    L5:
        int r1 = r9.findPointerIndex(this.mActivePointerId);
        float r2 = r9.getX(r1) - this.mInitialTouchX;
        float r4 = r9.getY(r1) - this.mInitialTouchY;
        float r5 = Math.abs(r2);
        float r6 = Math.abs(r4);
        if (r5 >= this.mSlop) goto L11;
        if (r6 >= this.mSlop) goto L11;
        return null;
    L11:
        if (r5 <= r6) goto L16;
        if (r0.canScrollHorizontally() == false) goto L16;
        return null;
    L16:
        if (r6 > r5) goto L18;
    L20:
        View r7 = findChildView(r9);
        if (r7 != null) goto L24;
        return null;
    L24:
        return this.mRecyclerView.getChildViewHolder(r7);
    L18:
        if (r0.canScrollVertically() == false) goto L20;
        return null;
    }

    private void getSelectedDxDy(float[] r4) {
        if ((this.mSelectedFlags & 12) == 0) goto L5;
        r4[0] = (this.mSelectedStartX + this.mDx) - this.mSelected.itemView.getLeft();
    L7:
        if ((this.mSelectedFlags & 3) == 0) goto L9;
        r4[1] = (this.mSelectedStartY + this.mDy) - this.mSelected.itemView.getTop();
        return;
    L9:
        r4[1] = this.mSelected.itemView.getTranslationY();
        return;
    L5:
        r4[0] = this.mSelected.itemView.getTranslationX();
        goto L7
    }

    private static boolean hitTest(View r1, float r2, float r3, float r4, float r5) {
        if (r2 >= r4) goto L5;
    L11:
        return false;
    L5:
        if (r2 > (r1.getWidth() + r4)) goto L11;
        if (r3 < r5) goto L11;
        if (r3 > (r1.getHeight() + r5)) goto L11;
        return true;
    }

    private void releaseVelocityTracker() {
        if (this.mVelocityTracker == null) goto L6;
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
        return;
    }

    private void setupCallbacks() {
        this.mSlop = ViewConfiguration.get(this.mRecyclerView.getContext()).getScaledTouchSlop();
        this.mRecyclerView.addItemDecoration(this);
        this.mRecyclerView.addOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.addOnChildAttachStateChangeListener(this);
        startGestureDetection();
    }

    private void startGestureDetection() {
        this.mItemTouchHelperGestureListener = new ItemTouchHelperGestureListener(this);
        this.mGestureDetector = new GestureDetectorCompat(this.mRecyclerView.getContext(), this.mItemTouchHelperGestureListener);
    }

    private void stopGestureDetection() {
        if (this.mItemTouchHelperGestureListener == null) goto L6;
        this.mItemTouchHelperGestureListener.doNotReactToLongPress();
        this.mItemTouchHelperGestureListener = null;
    L6:
        if (this.mGestureDetector == null) goto L9;
        this.mGestureDetector = null;
        return;
    }

    private int swipeIfNecessary(RecyclerView.ViewHolder r8) {
        if (this.mActionState != 2) goto L5;
        return 0;
    L5:
        int r0 = this.mCallback.getMovementFlags(this.mRecyclerView, r8);
        int r4 = (this.mCallback.convertToAbsoluteDirection(r0, ViewCompat.getLayoutDirection(this.mRecyclerView)) & 65280) >> 8;
        if (r4 != 0) goto L8;
        return 0;
    L8:
        int r3 = (65280 & r0) >> 8;
        if (Math.abs(this.mDx) <= Math.abs(this.mDy)) goto L20;
        int r5 = checkHorizontalSwipe(r8, r4);
        if (r5 > 0) goto L13;
        int r6 = checkVerticalSwipe(r8, r4);
        if (r6 <= 0) goto L30;
        return r6;
    L30:
        return 0;
    L13:
        if ((r3 & r5) == 0) goto L15;
        return r5;
    L15:
        return Callback.convertToRelativeDirection(r5, ViewCompat.getLayoutDirection(this.mRecyclerView));
    L20:
        int r7 = checkVerticalSwipe(r8, r4);
        if (r7 <= 0) goto L23;
        return r7;
    L23:
        int r9 = checkHorizontalSwipe(r8, r4);
        if (r9 <= 0) goto L30;
        if ((r3 & r9) == 0) goto L28;
        return r9;
    L28:
        return Callback.convertToRelativeDirection(r9, ViewCompat.getLayoutDirection(this.mRecyclerView));
    }

    public void attachToRecyclerView(RecyclerView r3) {
        if (this.mRecyclerView != r3) goto L6;
        return;
    L6:
        if (this.mRecyclerView == null) goto L8;
        destroyCallbacks();
    L8:
        this.mRecyclerView = r3;
        if (r3 == null) goto L12;
        Resources r0 = r3.getResources();
        this.mSwipeEscapeVelocity = r0.getDimension(C0472R.dimen.item_touch_helper_swipe_escape_velocity);
        this.mMaxSwipeVelocity = r0.getDimension(C0472R.dimen.item_touch_helper_swipe_escape_max_velocity);
        setupCallbacks();
        return;
    }

    void checkSelectForSwipe(int r13, MotionEvent r14, int r15) {
        if (this.mSelected == null) goto L5;
        return;
    L5:
        if (r13 == 2) goto L7;
        return;
    L7:
        if (this.mActionState != 2) goto L9;
        return;
    L9:
        if (this.mCallback.isItemViewSwipeEnabled() == true) goto L12;
        return;
    L12:
        if (this.mRecyclerView.getScrollState() != 1) goto L14;
        return;
    L14:
        RecyclerView.ViewHolder r0 = findSwipedView(r14);
        if (r0 != null) goto L17;
        return;
    L17:
        int r3 = (65280 & this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, r0)) >> 8;
        if (r3 != 0) goto L20;
        return;
    L20:
        float r4 = r14.getX(r15);
        float r5 = r14.getY(r15);
        float r6 = r4 - this.mInitialTouchX;
        float r7 = r5 - this.mInitialTouchY;
        float r8 = Math.abs(r6);
        float r9 = Math.abs(r7);
        if (r8 >= this.mSlop) goto L26;
        if (r9 >= this.mSlop) goto L26;
        return;
    L26:
        if (r8 <= r9) goto L38;
        if (r6 >= 0.0f) goto L33;
        if ((r3 & 4) != 0) goto L33;
        return;
    L33:
        if (r6 > 0.0f) goto L35;
    L47:
        this.mDy = 0.0f;
        this.mDx = 0.0f;
        this.mActivePointerId = r14.getPointerId(0);
        select(r0, 1);
        return;
    L35:
        if ((r3 & 8) != 0) goto L47;
        return;
    L38:
        if (r7 >= 0.0f) goto L43;
        if ((r3 & 1) != 0) goto L43;
        return;
    L43:
        if (r7 <= 0.0f) goto L47;
        if ((r3 & 2) != 0) goto L47;
    }

    void endRecoverAnimation(RecyclerView.ViewHolder r5, boolean r6) {
        int r1 = this.mRecoverAnimations.size() - 1;
    L3:
        if (r1 < 0) goto L12;
        RecoverAnimation r2 = this.mRecoverAnimations.get(r1);
        if (r2.mViewHolder == r5) goto L6;
        r1 = r1 - 1;
        goto L3
    L6:
        r2.mOverridden |= r6;
        if (r2.mEnded == true) goto L9;
        r2.cancel();
    L9:
        this.mRecoverAnimations.remove(r1);
        return;
    }

    RecoverAnimation findAnimation(MotionEvent r6) {
        if (this.mRecoverAnimations.isEmpty() == false) goto L5;
        return null;
    L5:
        View r0 = findChildView(r6);
        int r2 = this.mRecoverAnimations.size() - 1;
    L6:
        if (r2 < 0) goto L11;
        RecoverAnimation r3 = this.mRecoverAnimations.get(r2);
        if (r3.mViewHolder.itemView == r0) goto L9;
        r2 = r2 - 1;
        goto L6
    L9:
        return r3;
    L11:
        return null;
    }

    View findChildView(MotionEvent r8) {
        float r0 = r8.getX();
        float r1 = r8.getY();
        if (this.mSelected == null) goto L7;
        View r2 = this.mSelected.itemView;
        if (hitTest(r2, r0, r1, this.mSelectedStartX + this.mDx, this.mSelectedStartY + this.mDy) == false) goto L7;
        return r2;
    L7:
        int r3 = this.mRecoverAnimations.size() - 1;
    L8:
        if (r3 < 0) goto L14;
        RecoverAnimation r4 = this.mRecoverAnimations.get(r3);
        View r5 = r4.mViewHolder.itemView;
        if (hitTest(r5, r0, r1, r4.f72mX, r4.f73mY) == true) goto L11;
        r3 = r3 - 1;
        goto L8
    L11:
        return r5;
    L14:
        return this.mRecyclerView.findChildViewUnder(r0, r1);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect r1, View r2, RecyclerView r3, RecyclerView.State r4) {
        r1.setEmpty();
    }

    boolean hasRunningRecoverAnim() {
        int r0 = this.mRecoverAnimations.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L9;
        if (this.mRecoverAnimations.get(r1).mEnded == false) goto L6;
        r1 = r1 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    void moveIfNecessary(RecyclerView.ViewHolder r11) {
        if (this.mRecyclerView.isLayoutRequested() == false) goto L6;
        return;
    L6:
        if (this.mActionState == 2) goto L8;
        return;
    L8:
        float r0 = this.mCallback.getMoveThreshold(r11);
        int r8 = (int) (this.mSelectedStartX + this.mDx);
        int r9 = (int) (this.mSelectedStartY + this.mDy);
        if (Math.abs(r9 - r11.itemView.getTop()) < (r11.itemView.getHeight() * r0)) goto L11;
    L13:
        List<RecyclerView.ViewHolder> r1 = findSwapTargets(r11);
        if (r1.size() != 0) goto L16;
        return;
    L16:
        RecyclerView.ViewHolder r6 = this.mCallback.chooseDropTarget(r11, r1, r8, r9);
        if (r6 != null) goto L20;
        this.mSwapTargets.clear();
        this.mDistances.clear();
        return;
    L20:
        int r7 = r6.getAbsoluteAdapterPosition();
        int r5 = r11.getAbsoluteAdapterPosition();
        if (this.mCallback.onMove(this.mRecyclerView, r11, r6) == false) goto L24;
        this.mCallback.onMoved(this.mRecyclerView, r11, r5, r6, r7, r8, r9);
        return;
    L24:
        return;
    L11:
        if (Math.abs(r8 - r11.itemView.getLeft()) >= (r11.itemView.getWidth() * r0)) goto L13;
    }

    void obtainVelocityTracker() {
        if (this.mVelocityTracker == null) goto L5;
        this.mVelocityTracker.recycle();
    L5:
        this.mVelocityTracker = VelocityTracker.obtain();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
    public void onChildViewAttachedToWindow(View r1) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
    public void onChildViewDetachedFromWindow(View r4) {
        removeChildDrawingOrderCallbackIfNecessary(r4);
        RecyclerView.ViewHolder r0 = this.mRecyclerView.getChildViewHolder(r4);
        if (r0 != null) goto L6;
        return;
    L6:
        if (this.mSelected != null) goto L8;
    L10:
        endRecoverAnimation(r0, false);
        if (this.mPendingCleanup.remove(r0.itemView) == false) goto L15;
        this.mCallback.clearView(this.mRecyclerView, r0);
        return;
    L15:
        return;
    L8:
        if (r0 != this.mSelected) goto L10;
        select(null, 0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDraw(Canvas r11, RecyclerView r12, RecyclerView.State r13) {
        this.mOverdrawChildPosition = -1;
        if (this.mSelected == null) goto L5;
        getSelectedDxDy(this.mTmpPosition);
        float r8 = this.mTmpPosition[0];
        float r9 = this.mTmpPosition[1];
    L6:
        this.mCallback.onDraw(r11, r12, this.mSelected, this.mRecoverAnimations, this.mActionState, r8, r9);
        return;
    L5:
        r8 = 0.0f;
        r9 = 0.0f;
        goto L6
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDrawOver(Canvas r11, RecyclerView r12, RecyclerView.State r13) {
        if (this.mSelected == null) goto L5;
        getSelectedDxDy(this.mTmpPosition);
        float r8 = this.mTmpPosition[0];
        float r9 = this.mTmpPosition[1];
    L6:
        this.mCallback.onDrawOver(r11, r12, this.mSelected, this.mRecoverAnimations, this.mActionState, r8, r9);
        return;
    L5:
        r8 = 0.0f;
        r9 = 0.0f;
        goto L6
    }

    void postDispatchSwipe(final RecoverAnimation r3, final int r4) {
        this.mRecyclerView.post(new RunnableC04914(this, r3, r4));
    }

    void removeChildDrawingOrderCallbackIfNecessary(View r3) {
        if (r3 != this.mOverdrawChild) goto L8;
        this.mOverdrawChild = null;
        if (this.mChildDrawingOrderCallback == null) goto L9;
        this.mRecyclerView.setChildDrawingOrderCallback(null);
        return;
    L9:
        return;
    }

    boolean scrollIfNecessary() {
        if (this.mSelected != null) goto L6;
        this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
        return false;
    L6:
        long r5 = System.currentTimeMillis();
        if (this.mDragScrollStartTimeInMs != Long.MIN_VALUE) goto L9;
        long r7 = 0;
    L10:
        long r14 = r7;
        RecyclerView.LayoutManager r1 = this.mRecyclerView.getLayoutManager();
        if (this.mTmpRect != null) goto L13;
        this.mTmpRect = new Rect();
    L13:
        int r8 = 0;
        r1.calculateItemDecorationsForChild(this.mSelected.itemView, this.mTmpRect);
        if (r1.canScrollHorizontally() == false) goto L25;
        int r9 = (int) (this.mSelectedStartX + this.mDx);
        int r11 = (r9 - this.mTmpRect.left) - this.mRecyclerView.getPaddingLeft();
        if (this.mDx >= 0.0f) goto L20;
        if (r11 >= 0) goto L20;
        boolean r16 = false;
        int r12 = r11;
    L28:
        if (r1.canScrollVertically() == false) goto L38;
        int r2 = (int) (this.mSelectedStartY + this.mDy);
        int r10 = (r2 - this.mTmpRect.top) - this.mRecyclerView.getPaddingTop();
        if (this.mDy >= 0.0f) goto L34;
        if (r10 >= 0) goto L34;
        r8 = r10;
    L34:
        if (this.mDy <= 0.0f) goto L38;
        int r13 = ((this.mSelected.itemView.getHeight() + r2) + this.mTmpRect.bottom) - (this.mRecyclerView.getHeight() - this.mRecyclerView.getPaddingBottom());
        if (r13 <= 0) goto L38;
        r8 = r13;
    L38:
        if (r12 == 0) goto L40;
        int r3 = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getWidth(), r12, this.mRecyclerView.getWidth(), r14);
    L41:
        if (r8 == 0) goto L44;
        r8 = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getHeight(), r8, this.mRecyclerView.getHeight(), r14);
    L44:
        if (r3 != 0) goto L50;
        if (r8 != 0) goto L50;
        this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
        return r16;
    L50:
        if (this.mDragScrollStartTimeInMs != Long.MIN_VALUE) goto L52;
        this.mDragScrollStartTimeInMs = r5;
    L52:
        this.mRecyclerView.scrollBy(r3, r8);
        return true;
    L40:
        r3 = r12;
    L20:
        if (this.mDx <= 0.0f) goto L24;
        r16 = false;
        r12 = ((this.mSelected.itemView.getWidth() + r9) + this.mTmpRect.right) - (this.mRecyclerView.getWidth() - this.mRecyclerView.getPaddingRight());
        if (r12 > 0) goto L28;
    L26:
        r12 = 0;
        goto L28
    L24:
        r16 = false;
        goto L26
    L25:
        r16 = false;
        goto L26
    L9:
        r7 = r5 - this.mDragScrollStartTimeInMs;
        goto L10
    }

    void select(RecyclerView.ViewHolder r20, int r21) {
        if (r20 == this.mSelected) goto L5;
    L7:
        this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
        final int r4 = this.mActionState;
        endRecoverAnimation(r20, true);
        this.mActionState = r21;
        if (r21 != 2) goto L13;
        if (r20 == null) goto L12;
        this.mOverdrawChild = r20.itemView;
        addChildDrawingOrderCallback();
        goto L13
    L12:
        throw new IllegalArgumentException("Must pass a ViewHolder when dragging");
    L13:
        int r15 = (1 << ((r21 * 8) + 8)) - 1;
        boolean r16 = false;
        if (this.mSelected == null) goto L34;
        final RecyclerView.ViewHolder r2 = this.mSelected;
        if (r2.itemView.getParent() == null) goto L31;
        if (r4 != 2) goto L19;
        final int r9 = 0;
    L20:
        releaseVelocityTracker();
        switch(r9) {
            case 1: goto L24;
            case 2: goto L24;
            case 4: goto L23;
            case 8: goto L23;
            case 16: goto L23;
            case 32: goto L23;
            default: goto L22;
        };
    L22:
        final float r8 = 0.0f;
        final float r7 = 0.0f;
    L25:
        if (r4 != 2) goto L27;
        final int r3 = 8;
    L30:
        getSelectedDxDy(this.mTmpPosition);
        final float r5 = this.mTmpPosition[0];
        final float r6 = this.mTmpPosition[1];
        RecoverAnimation r0 = new C04903(this, r2, r3, r4, r5, r6, r7, r8, r9, r2);
        r0.setDuration(this.mCallback.getAnimationDuration(this.mRecyclerView, r3, r7 - r5, r8 - r6));
        this.mRecoverAnimations.add(r0);
        r0.start();
        r16 = true;
    L32:
        this.mSelected = null;
        goto L34
    L27:
        if (r9 <= 0) goto L29;
        r3 = 2;
        goto L30
    L29:
        r3 = 4;
        goto L30
    L23:
        r8 = 0.0f;
        r7 = Math.signum(this.mDx) * this.mRecyclerView.getWidth();
        goto L25
    L24:
        r8 = Math.signum(this.mDy) * this.mRecyclerView.getHeight();
        r7 = 0.0f;
        goto L25
    L19:
        r9 = swipeIfNecessary(r2);
        goto L20
    L31:
        removeChildDrawingOrderCallbackIfNecessary(r2.itemView);
        this.mCallback.clearView(this.mRecyclerView, r2);
    L34:
        if (r20 == null) goto L39;
        this.mSelectedFlags = (this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, r20) & r15) >> (this.mActionState * 8);
        this.mSelectedStartX = r20.itemView.getLeft();
        this.mSelectedStartY = r20.itemView.getTop();
        this.mSelected = r20;
        if (r21 != 2) goto L38;
        boolean r13 = false;
        this.mSelected.itemView.performHapticFeedback(0);
    L40:
        ViewParent r1 = this.mRecyclerView.getParent();
        if (r1 != null) goto L43;
    L46:
        if (r16 == true) goto L48;
        this.mRecyclerView.getLayoutManager().requestSimpleAnimationsInNextLayout();
    L48:
        this.mCallback.onSelectedChanged(this.mSelected, this.mActionState);
        this.mRecyclerView.invalidate();
        return;
    L43:
        if (this.mSelected == null) goto L45;
        r13 = true;
    L45:
        r1.requestDisallowInterceptTouchEvent(r13);
        goto L46
    L38:
        r13 = false;
        goto L40
    L39:
        r13 = false;
        goto L40
    L5:
        if (r21 != this.mActionState) goto L7;
    }

    public void startDrag(RecyclerView.ViewHolder r4) {
        if (this.mCallback.hasDragFlag(this.mRecyclerView, r4) == true) goto L7;
        Log.e(TAG, "Start drag has been called but dragging is not enabled");
        return;
    L7:
        if (r4.itemView.getParent() == this.mRecyclerView) goto L10;
        Log.e(TAG, "Start drag has been called with a view holder which is not a child of the RecyclerView which is controlled by this ItemTouchHelper.");
        return;
    L10:
        obtainVelocityTracker();
        this.mDy = 0.0f;
        this.mDx = 0.0f;
        select(r4, 2);
    }

    public void startSwipe(RecyclerView.ViewHolder r4) {
        if (this.mCallback.hasSwipeFlag(this.mRecyclerView, r4) == true) goto L7;
        Log.e(TAG, "Start swipe has been called but swiping is not enabled");
        return;
    L7:
        if (r4.itemView.getParent() == this.mRecyclerView) goto L10;
        Log.e(TAG, "Start swipe has been called with a view holder which is not a child of the RecyclerView controlled by this ItemTouchHelper.");
        return;
    L10:
        obtainVelocityTracker();
        this.mDy = 0.0f;
        this.mDx = 0.0f;
        select(r4, 1);
    }

    void updateDxDy(MotionEvent r5, int r6, int r7) {
        float r0 = r5.getX(r7);
        float r1 = r5.getY(r7);
        this.mDx = r0 - this.mInitialTouchX;
        this.mDy = r1 - this.mInitialTouchY;
        if ((r6 & 4) != 0) goto L6;
        this.mDx = Math.max(0.0f, this.mDx);
    L6:
        if ((r6 & 8) != 0) goto L9;
        this.mDx = Math.min(0.0f, this.mDx);
    L9:
        if ((r6 & 1) != 0) goto L12;
        this.mDy = Math.max(0.0f, this.mDy);
    L12:
        if ((r6 & 2) != 0) goto L15;
        this.mDy = Math.min(0.0f, this.mDy);
        return;
    }
}
