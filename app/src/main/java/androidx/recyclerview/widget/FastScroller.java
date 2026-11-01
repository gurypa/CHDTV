package androidx.recyclerview.widget;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.MotionEvent;
import androidx.core.view.ViewCompat;

/* JADX INFO: loaded from: classes.dex */
class FastScroller extends RecyclerView.ItemDecoration implements RecyclerView.OnItemTouchListener {
    private static final int ANIMATION_STATE_FADING_IN = 1;
    private static final int ANIMATION_STATE_FADING_OUT = 3;
    private static final int ANIMATION_STATE_IN = 2;
    private static final int ANIMATION_STATE_OUT = 0;
    private static final int DRAG_NONE = 0;
    private static final int DRAG_X = 1;
    private static final int DRAG_Y = 2;
    private static final int[] EMPTY_STATE_SET = null;
    private static final int HIDE_DELAY_AFTER_DRAGGING_MS = 1200;
    private static final int HIDE_DELAY_AFTER_VISIBLE_MS = 1500;
    private static final int HIDE_DURATION_MS = 500;
    private static final int[] PRESSED_STATE_SET = null;
    private static final int SCROLLBAR_FULL_OPAQUE = 255;
    private static final int SHOW_DURATION_MS = 500;
    private static final int STATE_DRAGGING = 2;
    private static final int STATE_HIDDEN = 0;
    private static final int STATE_VISIBLE = 1;
    int mAnimationState;
    private int mDragState;
    private final Runnable mHideRunnable;
    float mHorizontalDragX;
    private final int[] mHorizontalRange;
    int mHorizontalThumbCenterX;
    private final StateListDrawable mHorizontalThumbDrawable;
    private final int mHorizontalThumbHeight;
    int mHorizontalThumbWidth;
    private final Drawable mHorizontalTrackDrawable;
    private final int mHorizontalTrackHeight;
    private final int mMargin;
    private boolean mNeedHorizontalScrollbar;
    private boolean mNeedVerticalScrollbar;
    private final RecyclerView.OnScrollListener mOnScrollListener;
    private RecyclerView mRecyclerView;
    private int mRecyclerViewHeight;
    private int mRecyclerViewWidth;
    private final int mScrollbarMinimumRange;
    final ValueAnimator mShowHideAnimator;
    private int mState;
    float mVerticalDragY;
    private final int[] mVerticalRange;
    int mVerticalThumbCenterY;
    final StateListDrawable mVerticalThumbDrawable;
    int mVerticalThumbHeight;
    private final int mVerticalThumbWidth;
    final Drawable mVerticalTrackDrawable;
    private final int mVerticalTrackWidth;

    private class AnimatorListener extends AnimatorListenerAdapter {
        private boolean mCanceled;
        final /* synthetic */ FastScroller this$0;

        AnimatorListener(FastScroller r1) {
            this.this$0 = r1;
            this.mCanceled = false;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator r2) {
            this.mCanceled = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator r4) {
            if (this.mCanceled == false) goto L7;
            this.mCanceled = false;
            return;
        L7:
            if (((Float) this.this$0.mShowHideAnimator.getAnimatedValue()).floatValue() != 0.0f) goto L9;
            this.this$0.mAnimationState = 0;
            this.this$0.setState(0);
            return;
        L9:
            this.this$0.mAnimationState = 2;
            this.this$0.requestRedraw();
        }
    }

    private class AnimatorUpdater implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ FastScroller this$0;

        AnimatorUpdater(FastScroller r1) {
            this.this$0 = r1;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator r3) {
            int r0 = (int) (((Float) r3.getAnimatedValue()).floatValue() * 255.0f);
            this.this$0.mVerticalThumbDrawable.setAlpha(r0);
            this.this$0.mVerticalTrackDrawable.setAlpha(r0);
            this.this$0.requestRedraw();
        }
    }

    static {
        PRESSED_STATE_SET = new int[]{R.attr.state_pressed};
        EMPTY_STATE_SET = new int[0];
    }

    FastScroller(RecyclerView r4, StateListDrawable r5, Drawable r6, StateListDrawable r7, Drawable r8, int r9, int r10, int r11) {
        this.mRecyclerViewWidth = 0;
        this.mRecyclerViewHeight = 0;
        this.mNeedVerticalScrollbar = false;
        this.mNeedHorizontalScrollbar = false;
        this.mState = 0;
        this.mDragState = 0;
        this.mVerticalRange = new int[2];
        this.mHorizontalRange = new int[2];
        this.mShowHideAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mAnimationState = 0;
        this.mHideRunnable = new RunnableC04851(this);
        this.mOnScrollListener = new C04862(this);
        this.mVerticalThumbDrawable = r5;
        this.mVerticalTrackDrawable = r6;
        this.mHorizontalThumbDrawable = r7;
        this.mHorizontalTrackDrawable = r8;
        this.mVerticalThumbWidth = Math.max(r9, r5.getIntrinsicWidth());
        this.mVerticalTrackWidth = Math.max(r9, r6.getIntrinsicWidth());
        this.mHorizontalThumbHeight = Math.max(r9, r7.getIntrinsicWidth());
        this.mHorizontalTrackHeight = Math.max(r9, r8.getIntrinsicWidth());
        this.mScrollbarMinimumRange = r10;
        this.mMargin = r11;
        this.mVerticalThumbDrawable.setAlpha(255);
        this.mVerticalTrackDrawable.setAlpha(255);
        this.mShowHideAnimator.addListener(new AnimatorListener(this));
        this.mShowHideAnimator.addUpdateListener(new AnimatorUpdater(this));
        attachToRecyclerView(r4);
    }

    private void cancelHide() {
        this.mRecyclerView.removeCallbacks(this.mHideRunnable);
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration(this);
        this.mRecyclerView.removeOnItemTouchListener(this);
        this.mRecyclerView.removeOnScrollListener(this.mOnScrollListener);
        cancelHide();
    }

    private void drawHorizontalScrollbar(Canvas r8) {
        int r1 = this.mRecyclerViewHeight - this.mHorizontalThumbHeight;
        int r2 = this.mHorizontalThumbCenterX - (this.mHorizontalThumbWidth / 2);
        this.mHorizontalThumbDrawable.setBounds(0, 0, this.mHorizontalThumbWidth, this.mHorizontalThumbHeight);
        this.mHorizontalTrackDrawable.setBounds(0, 0, this.mRecyclerViewWidth, this.mHorizontalTrackHeight);
        r8.translate(0.0f, r1);
        this.mHorizontalTrackDrawable.draw(r8);
        r8.translate(r2, 0.0f);
        this.mHorizontalThumbDrawable.draw(r8);
        r8.translate(-r2, -r1);
    }

    private void drawVerticalScrollbar(Canvas r8) {
        int r1 = this.mRecyclerViewWidth - this.mVerticalThumbWidth;
        int r2 = this.mVerticalThumbCenterY - (this.mVerticalThumbHeight / 2);
        this.mVerticalThumbDrawable.setBounds(0, 0, this.mVerticalThumbWidth, this.mVerticalThumbHeight);
        this.mVerticalTrackDrawable.setBounds(0, 0, this.mVerticalTrackWidth, this.mRecyclerViewHeight);
        if (isLayoutRTL() == false) goto L5;
        this.mVerticalTrackDrawable.draw(r8);
        r8.translate(this.mVerticalThumbWidth, r2);
        r8.scale(-1.0f, 1.0f);
        this.mVerticalThumbDrawable.draw(r8);
        r8.scale(-1.0f, 1.0f);
        r8.translate(-this.mVerticalThumbWidth, -r2);
        return;
    L5:
        r8.translate(r1, 0.0f);
        this.mVerticalTrackDrawable.draw(r8);
        r8.translate(0.0f, r2);
        this.mVerticalThumbDrawable.draw(r8);
        r8.translate(-r1, -r2);
    }

    private int[] getHorizontalRange() {
        this.mHorizontalRange[0] = this.mMargin;
        this.mHorizontalRange[1] = this.mRecyclerViewWidth - this.mMargin;
        return this.mHorizontalRange;
    }

    private int[] getVerticalRange() {
        this.mVerticalRange[0] = this.mMargin;
        this.mVerticalRange[1] = this.mRecyclerViewHeight - this.mMargin;
        return this.mVerticalRange;
    }

    private void horizontalScrollTo(float r9) {
        int[] r3 = getHorizontalRange();
        float r2 = Math.max(r3[0], Math.min(r3[1], r9));
        if (Math.abs(this.mHorizontalThumbCenterX - r2) >= 2.0f) goto L5;
        return;
    L5:
        int r10 = scrollTo(this.mHorizontalDragX, r2, r3, this.mRecyclerView.computeHorizontalScrollRange(), this.mRecyclerView.computeHorizontalScrollOffset(), this.mRecyclerViewWidth);
        if (r10 == 0) goto L8;
        this.mRecyclerView.scrollBy(r10, 0);
    L8:
        this.mHorizontalDragX = r2;
    }

    private boolean isLayoutRTL() {
        if (ViewCompat.getLayoutDirection(this.mRecyclerView) == 1) goto L7;
        return false;
    L7:
        return true;
    }

    private void resetHideDelay(int r5) {
        cancelHide();
        this.mRecyclerView.postDelayed(this.mHideRunnable, r5);
    }

    private int scrollTo(float r7, float r8, int[] r9, int r10, int r11, int r12) {
        int r0 = r9[1] - r9[0];
        if (r0 != 0) goto L5;
        return 0;
    L5:
        int r3 = r10 - r12;
        int r4 = (int) (r3 * ((r8 - r7) / r0));
        int r5 = r11 + r4;
        if (r5 >= r3) goto L9;
        if (r5 < 0) goto L9;
        return r4;
    L9:
        return 0;
    }

    private void setupCallbacks() {
        this.mRecyclerView.addItemDecoration(this);
        this.mRecyclerView.addOnItemTouchListener(this);
        this.mRecyclerView.addOnScrollListener(this.mOnScrollListener);
    }

    private void verticalScrollTo(float r9) {
        int[] r3 = getVerticalRange();
        float r2 = Math.max(r3[0], Math.min(r3[1], r9));
        if (Math.abs(this.mVerticalThumbCenterY - r2) >= 2.0f) goto L5;
        return;
    L5:
        int r10 = scrollTo(this.mVerticalDragY, r2, r3, this.mRecyclerView.computeVerticalScrollRange(), this.mRecyclerView.computeVerticalScrollOffset(), this.mRecyclerViewHeight);
        if (r10 == 0) goto L8;
        this.mRecyclerView.scrollBy(0, r10);
    L8:
        this.mVerticalDragY = r2;
    }

    public void attachToRecyclerView(RecyclerView r2) {
        if (this.mRecyclerView != r2) goto L6;
        return;
    L6:
        if (this.mRecyclerView == null) goto L8;
        destroyCallbacks();
    L8:
        this.mRecyclerView = r2;
        if (this.mRecyclerView == null) goto L12;
        setupCallbacks();
        return;
    }

    Drawable getHorizontalThumbDrawable() {
        return this.mHorizontalThumbDrawable;
    }

    Drawable getHorizontalTrackDrawable() {
        return this.mHorizontalTrackDrawable;
    }

    Drawable getVerticalThumbDrawable() {
        return this.mVerticalThumbDrawable;
    }

    Drawable getVerticalTrackDrawable() {
        return this.mVerticalTrackDrawable;
    }

    void hide(int r5) {
        switch(this.mAnimationState) {
            case 1: goto L5;
            case 2: goto L6;
            default: goto L8;
        };
    L5:
        this.mShowHideAnimator.cancel();
    L6:
        this.mAnimationState = 3;
        this.mShowHideAnimator.setFloatValues(new float[]{((Float) this.mShowHideAnimator.getAnimatedValue()).floatValue(), 0.0f});
        this.mShowHideAnimator.setDuration(r5);
        this.mShowHideAnimator.start();
        return;
    }

    public boolean isDragging() {
        if (this.mState != 2) goto L5;
        return true;
    L5:
        return false;
    }

    boolean isPointInsideHorizontalThumb(float r3, float r4) {
        if (r4 >= (this.mRecyclerViewHeight - this.mHorizontalThumbHeight)) goto L5;
    L9:
        return false;
    L5:
        if (r3 < (this.mHorizontalThumbCenterX - (this.mHorizontalThumbWidth / 2))) goto L9;
        if (r3 > (this.mHorizontalThumbCenterX + (this.mHorizontalThumbWidth / 2))) goto L9;
        return true;
    }

    boolean isPointInsideVerticalThumb(float r3, float r4) {
        if (isLayoutRTL() == false) goto L8;
        if (r3 <= this.mVerticalThumbWidth) goto L10;
    L14:
        return false;
    L10:
        if (r4 < (this.mVerticalThumbCenterY - (this.mVerticalThumbHeight / 2))) goto L14;
        if (r4 > (this.mVerticalThumbCenterY + (this.mVerticalThumbHeight / 2))) goto L14;
        return true;
    L8:
        if (r3 < (this.mRecyclerViewWidth - this.mVerticalThumbWidth)) goto L14;
        goto L14
    }

    boolean isVisible() {
        if (this.mState == 1) goto L7;
        return false;
    L7:
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDrawOver(Canvas r3, RecyclerView r4, RecyclerView.State r5) {
        if (this.mRecyclerViewWidth == this.mRecyclerView.getWidth()) goto L5;
    L16:
        this.mRecyclerViewWidth = this.mRecyclerView.getWidth();
        this.mRecyclerViewHeight = this.mRecyclerView.getHeight();
        setState(0);
        return;
    L5:
        if (this.mRecyclerViewHeight != this.mRecyclerView.getHeight()) goto L16;
        if (this.mAnimationState != 0) goto L10;
        return;
    L10:
        if (this.mNeedVerticalScrollbar == false) goto L13;
        drawVerticalScrollbar(r3);
    L13:
        if (this.mNeedHorizontalScrollbar == false) goto L19;
        drawHorizontalScrollbar(r3);
        return;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public boolean onInterceptTouchEvent(RecyclerView r6, MotionEvent r7) {
        if (this.mState != 1) goto L16;
        boolean r0 = isPointInsideVerticalThumb(r7.getX(), r7.getY());
        boolean r3 = isPointInsideHorizontalThumb(r7.getX(), r7.getY());
        if (r7.getAction() != 0) goto L13;
        if (r0 == true) goto L8;
        if (r3 == false) goto L13;
    L8:
        if (r3 == false) goto L10;
        this.mDragState = 1;
        this.mHorizontalDragX = (int) r7.getX();
    L12:
        setState(2);
        return true;
    L10:
        if (r0 == false) goto L12;
        this.mDragState = 2;
        this.mVerticalDragY = (int) r7.getY();
    L13:
        return false;
    L16:
        if (this.mState != 2) goto L18;
        return true;
    L18:
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public void onRequestDisallowInterceptTouchEvent(boolean r1) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public void onTouchEvent(RecyclerView r6, MotionEvent r7) {
        if (this.mState != 0) goto L6;
        return;
    L6:
        if (r7.getAction() != 0) goto L17;
        boolean r0 = isPointInsideVerticalThumb(r7.getX(), r7.getY());
        boolean r3 = isPointInsideHorizontalThumb(r7.getX(), r7.getY());
        if (r0 == true) goto L10;
        if (r3 == true) goto L10;
        return;
    L10:
        if (r3 == false) goto L12;
        this.mDragState = 1;
        this.mHorizontalDragX = (int) r7.getX();
    L14:
        setState(2);
        return;
    L12:
        if (r0 == false) goto L14;
        this.mDragState = 2;
        this.mVerticalDragY = (int) r7.getY();
        goto L14
    L17:
        if (r7.getAction() != 1) goto L22;
        if (this.mState != 2) goto L22;
        this.mVerticalDragY = 0.0f;
        this.mHorizontalDragX = 0.0f;
        setState(1);
        this.mDragState = 0;
        return;
    L22:
        if (r7.getAction() == 2) goto L24;
        return;
    L24:
        if (this.mState != 2) goto L34;
        show();
        if (this.mDragState != 1) goto L29;
        horizontalScrollTo(r7.getX());
    L29:
        if (this.mDragState != 2) goto L35;
        verticalScrollTo(r7.getY());
        return;
    L35:
        return;
    }

    void requestRedraw() {
        this.mRecyclerView.invalidate();
    }

    void setState(int r4) {
        if (r4 == 2) goto L5;
    L7:
        if (r4 != 0) goto L9;
        requestRedraw();
    L11:
        if (this.mState != 2) goto L15;
        if (r4 == 2) goto L15;
        this.mVerticalThumbDrawable.setState(EMPTY_STATE_SET);
        resetHideDelay(HIDE_DELAY_AFTER_DRAGGING_MS);
    L17:
        this.mState = r4;
        return;
    L15:
        if (r4 != 1) goto L17;
        resetHideDelay(HIDE_DELAY_AFTER_VISIBLE_MS);
        goto L17
    L9:
        show();
        goto L11
    L5:
        if (this.mState == 2) goto L7;
        this.mVerticalThumbDrawable.setState(PRESSED_STATE_SET);
        cancelHide();
        goto L7
    }

    public void show() {
        switch(this.mAnimationState) {
            case 0: goto L6;
            case 3: goto L5;
            default: goto L8;
        };
    L5:
        this.mShowHideAnimator.cancel();
    L6:
        this.mAnimationState = 1;
        this.mShowHideAnimator.setFloatValues(new float[]{((Float) this.mShowHideAnimator.getAnimatedValue()).floatValue(), 1.0f});
        this.mShowHideAnimator.setDuration(500);
        this.mShowHideAnimator.setStartDelay(0);
        this.mShowHideAnimator.start();
        return;
    }

    void updateScrollPosition(int r10, int r11) {
        int r0 = this.mRecyclerView.computeVerticalScrollRange();
        int r1 = this.mRecyclerViewHeight;
        if ((r0 - r1) > 0) goto L5;
    L7:
        boolean r2 = false;
    L8:
        this.mNeedVerticalScrollbar = r2;
        int r3 = this.mRecyclerView.computeHorizontalScrollRange();
        int r5 = this.mRecyclerViewWidth;
        if ((r3 - r5) > 0) goto L11;
    L13:
        boolean r6 = false;
    L14:
        this.mNeedHorizontalScrollbar = r6;
        if (this.mNeedVerticalScrollbar == true) goto L23;
        if (this.mNeedHorizontalScrollbar == true) goto L23;
        if (this.mState == 0) goto L34;
        setState(0);
        return;
    L34:
        return;
    L23:
        if (this.mNeedVerticalScrollbar == false) goto L26;
        this.mVerticalThumbCenterY = (int) ((r1 * (r11 + (r1 / 2.0f))) / r0);
        this.mVerticalThumbHeight = Math.min(r1, (r1 * r1) / r0);
    L26:
        if (this.mNeedHorizontalScrollbar == false) goto L29;
        this.mHorizontalThumbCenterX = (int) ((r5 * (r10 + (r5 / 2.0f))) / r3);
        this.mHorizontalThumbWidth = Math.min(r5, (r5 * r5) / r3);
    L29:
        if (this.mState != 0) goto L31;
    L32:
        setState(1);
        return;
    L31:
        if (this.mState == 1) goto L32;
        return;
    L11:
        if (this.mRecyclerViewWidth < this.mScrollbarMinimumRange) goto L13;
        r6 = true;
        goto L14
    L5:
        if (this.mRecyclerViewHeight < this.mScrollbarMinimumRange) goto L7;
        r2 = true;
        goto L8
    }
}
