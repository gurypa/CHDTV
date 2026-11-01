package androidx.core.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AnimationUtils;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;
import androidx.core.C0039R;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityRecordCompat;
import androidx.media3.extractor.p001ts.TsExtractor;
import androidx.recyclerview.widget.ItemTouchHelper;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class NestedScrollView extends FrameLayout implements NestedScrollingParent3, NestedScrollingChild3, ScrollingView {
    private static final AccessibilityDelegate ACCESSIBILITY_DELEGATE = null;
    static final int ANIMATED_SCROLL_GAP = 250;
    private static final float DECELERATION_RATE = 0.0f;
    private static final int DEFAULT_SMOOTH_SCROLL_DURATION = 250;
    private static final float FLING_DESTRETCH_FACTOR = 4.0f;
    private static final float INFLEXION = 0.35f;
    private static final int INVALID_POINTER = -1;
    static final float MAX_SCROLL_FACTOR = 0.5f;
    private static final int[] SCROLLVIEW_STYLEABLE = null;
    private static final float SCROLL_FRICTION = 0.015f;
    private static final String TAG = "NestedScrollView";
    private int mActivePointerId;
    private final NestedScrollingChildHelper mChildHelper;
    private View mChildToScrollTo;
    public EdgeEffect mEdgeGlowBottom;
    public EdgeEffect mEdgeGlowTop;
    private boolean mFillViewport;
    private boolean mIsBeingDragged;
    private boolean mIsLaidOut;
    private boolean mIsLayoutDirty;
    private int mLastMotionY;
    private long mLastScroll;
    private int mLastScrollerY;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private int mNestedYOffset;
    private OnScrollChangeListener mOnScrollChangeListener;
    private final NestedScrollingParentHelper mParentHelper;
    private final float mPhysicalCoeff;
    private SavedState mSavedState;
    private final int[] mScrollConsumed;
    private final int[] mScrollOffset;
    private OverScroller mScroller;
    private boolean mSmoothScrollingEnabled;
    private final Rect mTempRect;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private float mVerticalScrollFactor;

    static class AccessibilityDelegate extends AccessibilityDelegateCompat {
        AccessibilityDelegate() {
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityEvent(View r4, AccessibilityEvent r5) {
            super.onInitializeAccessibilityEvent(r4, r5);
            NestedScrollView r0 = (NestedScrollView) r4;
            r5.setClassName(ScrollView.class.getName());
            if (r0.getScrollRange() <= 0) goto L5;
            boolean r1 = true;
        L6:
            r5.setScrollable(r1);
            r5.setScrollX(r0.getScrollX());
            r5.setScrollY(r0.getScrollY());
            AccessibilityRecordCompat.setMaxScrollX(r5, r0.getScrollX());
            AccessibilityRecordCompat.setMaxScrollY(r5, r0.getScrollRange());
            return;
        L5:
            r1 = false;
            goto L6
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View r4, AccessibilityNodeInfoCompat r5) {
            super.onInitializeAccessibilityNodeInfo(r4, r5);
            NestedScrollView r0 = (NestedScrollView) r4;
            r5.setClassName(ScrollView.class.getName());
            if (r0.isEnabled() == false) goto L13;
            int r1 = r0.getScrollRange();
            if (r1 <= 0) goto L14;
            r5.setScrollable(true);
            if (r0.getScrollY() <= 0) goto L10;
            r5.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD);
            r5.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_UP);
        L10:
            if (r0.getScrollY() >= r1) goto L15;
            r5.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD);
            r5.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_DOWN);
            return;
        L15:
            return;
        L14:
            return;
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean performAccessibilityAction(View r9, int r10, Bundle r11) {
            if (super.performAccessibilityAction(r9, r10, r11) == false) goto L5;
            return true;
        L5:
            NestedScrollView r0 = (NestedScrollView) r9;
            if (r0.isEnabled() == true) goto L8;
            return false;
        L8:
            int r2 = r0.getHeight();
            Rect r4 = new Rect();
            if (r0.getMatrix().isIdentity() == true) goto L11;
        L13:
            switch(r10) {
                case 4096: goto L20;
                case 8192: goto L15;
                case 16908344: goto L15;
                case 16908346: goto L20;
                default: goto L14;
            };
        L14:
            return false;
        L15:
            int r5 = (r2 - r0.getPaddingBottom()) - r0.getPaddingTop();
            int r6 = Math.max(r0.getScrollY() - r5, 0);
            if (r6 == r0.getScrollY()) goto L19;
            r0.smoothScrollTo(0, r6, true);
            return true;
        L19:
            return false;
        L20:
            int r7 = (r2 - r0.getPaddingBottom()) - r0.getPaddingTop();
            int r8 = Math.min(r0.getScrollY() + r7, r0.getScrollRange());
            if (r8 == r0.getScrollY()) goto L24;
            r0.smoothScrollTo(0, r8, true);
            return true;
        L24:
            return false;
        L11:
            if (r0.getGlobalVisibleRect(r4) == false) goto L13;
            r2 = r4.height();
            goto L13
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static boolean getClipToPadding(ViewGroup r1) {
            return r1.getClipToPadding();
        }
    }

    public interface OnScrollChangeListener {
        void onScrollChange(NestedScrollView r1, int r2, int r3, int r4, int r5);
    }

    static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = null;
        public int scrollPosition;

        static {
            CREATOR = new C01311();
        }

        SavedState(Parcel r2) {
            super(r2);
            this.scrollPosition = r2.readInt();
        }

        SavedState(Parcelable r1) {
            super(r1);
        }

        public String toString() {
            return "HorizontalScrollView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " scrollPosition=" + this.scrollPosition + "}";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel r2, int r3) {
            super.writeToParcel(r2, r3);
            r2.writeInt(this.scrollPosition);
        }
    }

    static {
        DECELERATION_RATE = (float) (Math.log(0.78d) / Math.log(0.9d));
        ACCESSIBILITY_DELEGATE = new AccessibilityDelegate();
        SCROLLVIEW_STYLEABLE = new int[]{R.attr.fillViewport};
    }

    public NestedScrollView(Context r2) {
        this(r2, null);
    }

    public NestedScrollView(Context r2, AttributeSet r3) {
        this(r2, r3, C0039R.attr.nestedScrollViewStyle);
    }

    public NestedScrollView(Context r6, AttributeSet r7, int r8) {
        super(r6, r7, r8);
        this.mTempRect = new Rect();
        this.mIsLayoutDirty = true;
        this.mIsLaidOut = false;
        this.mChildToScrollTo = null;
        this.mIsBeingDragged = false;
        this.mSmoothScrollingEnabled = true;
        this.mActivePointerId = -1;
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mEdgeGlowTop = EdgeEffectCompat.create(r6, r7);
        this.mEdgeGlowBottom = EdgeEffectCompat.create(r6, r7);
        this.mPhysicalCoeff = (386.0878f * (r6.getResources().getDisplayMetrics().density * 160.0f)) * 0.84f;
        initScrollView();
        TypedArray r3 = r6.obtainStyledAttributes(r7, SCROLLVIEW_STYLEABLE, r8, 0);
        setFillViewport(r3.getBoolean(0, false));
        r3.recycle();
        this.mParentHelper = new NestedScrollingParentHelper(this);
        this.mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        ViewCompat.setAccessibilityDelegate(this, ACCESSIBILITY_DELEGATE);
    }

    private void abortAnimatedScroll() {
        this.mScroller.abortAnimation();
        stopNestedScroll(1);
    }

    private boolean canOverScroll() {
        int r0 = getOverScrollMode();
        if (r0 == 0) goto L11;
        if (r0 == 1) goto L6;
    L8:
        return false;
    L6:
        if (getScrollRange() <= 0) goto L8;
        return true;
    L11:
        return true;
    }

    private boolean canScroll() {
        if (getChildCount() <= 0) goto L8;
        View r0 = getChildAt(0);
        FrameLayout.LayoutParams r2 = (FrameLayout.LayoutParams) r0.getLayoutParams();
        if (((r0.getHeight() + r2.topMargin) + r2.bottomMargin) <= ((getHeight() - getPaddingTop()) - getPaddingBottom())) goto L9;
        return true;
    L9:
        return false;
    L8:
        return false;
    }

    private static int clamp(int r1, int r2, int r3) {
        if (r2 >= r3) goto L10;
        if (r1 >= 0) goto L6;
        return 0;
    L6:
        if ((r2 + r1) > r3) goto L8;
        return r1;
    L8:
        return r3 - r2;
    L10:
        return 0;
    }

    private void doScrollY(int r3) {
        if (r3 != 0) goto L4;
        return;
    L4:
        if (this.mSmoothScrollingEnabled == false) goto L6;
        smoothScrollBy(0, r3);
        return;
    L6:
        scrollBy(0, r3);
    }

    private boolean edgeEffectFling(int r4) {
        if (EdgeEffectCompat.getDistance(this.mEdgeGlowTop) == 0.0f) goto L9;
        if (shouldAbsorb(this.mEdgeGlowTop, r4) == false) goto L7;
        this.mEdgeGlowTop.onAbsorb(r4);
        return true;
    L7:
        fling(-r4);
        return true;
    L9:
        if (EdgeEffectCompat.getDistance(this.mEdgeGlowBottom) != 0.0f) goto L11;
        return false;
    L11:
        if (shouldAbsorb(this.mEdgeGlowBottom, -r4) == false) goto L13;
        this.mEdgeGlowBottom.onAbsorb(-r4);
        return true;
    L13:
        fling(-r4);
        return true;
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        recycleVelocityTracker();
        stopNestedScroll(0);
        this.mEdgeGlowTop.onRelease();
        this.mEdgeGlowBottom.onRelease();
    }

    private View findFocusableViewInBounds(boolean r13, int r14, int r15) {
        ArrayList r0 = getFocusables(2);
        View r1 = null;
        boolean r2 = false;
        int r3 = r0.size();
        int r4 = 0;
    L3:
        if (r4 >= r3) goto L31;
        View r5 = (View) r0.get(r4);
        int r6 = r5.getTop();
        int r7 = r5.getBottom();
        if (r14 >= r7) goto L30;
        if (r6 >= r15) goto L30;
        boolean r8 = false;
        if (r14 >= r6) goto L11;
        if (r7 >= r15) goto L11;
        boolean r10 = true;
    L12:
        if (r1 != null) goto L14;
        r1 = r5;
        r2 = r10;
        goto L30
    L14:
        if (r13 == true) goto L16;
    L17:
        if (r13 == false) goto L19;
    L22:
        if (r2 == false) goto L26;
        if (r10 == false) goto L30;
        if (r8 == false) goto L30;
        r1 = r5;
        goto L30
    L26:
        if (r10 == false) goto L28;
        r1 = r5;
        r2 = true;
        goto L30
    L28:
        if (r8 == false) goto L30;
        r1 = r5;
        goto L30
    L19:
        if (r7 <= r1.getBottom()) goto L22;
    L20:
        r8 = true;
        goto L22
    L16:
        if (r6 < r1.getTop()) goto L20;
    L11:
        r10 = false;
    L30:
        r4 = r4 + 1;
        goto L3
    L31:
        return r1;
    }

    private float getSplineFlingDistance(int r10) {
        return (float) (((double) (this.mPhysicalCoeff * SCROLL_FRICTION)) * Math.exp((((double) DECELERATION_RATE) / (((double) DECELERATION_RATE) - 1.0d)) * Math.log((Math.abs(r10) * INFLEXION) / (this.mPhysicalCoeff * SCROLL_FRICTION))));
    }

    private float getVerticalScrollFactorCompat() {
        if (this.mVerticalScrollFactor != 0.0f) goto L10;
        TypedValue r0 = new TypedValue();
        Context r1 = getContext();
        if (r1.getTheme().resolveAttribute(R.attr.listPreferredItemHeight, r0, true) == false) goto L8;
        this.mVerticalScrollFactor = r0.getDimension(r1.getResources().getDisplayMetrics());
        goto L10
    L8:
        throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
    L10:
        return this.mVerticalScrollFactor;
    }

    private boolean inChild(int r5, int r6) {
        if (getChildCount() <= 0) goto L15;
        int r0 = getScrollY();
        View r2 = getChildAt(0);
        if (r6 >= (r2.getTop() - r0)) goto L7;
        return false;
    L7:
        if (r6 < (r2.getBottom() - r0)) goto L9;
        return false;
    L9:
        if (r5 >= r2.getLeft()) goto L11;
        return false;
    L11:
        if (r5 >= r2.getRight()) goto L19;
        return true;
    L19:
        return false;
    L15:
        return false;
    }

    private void initOrResetVelocityTracker() {
        if (this.mVelocityTracker != null) goto L5;
        this.mVelocityTracker = VelocityTracker.obtain();
        return;
    L5:
        this.mVelocityTracker.clear();
    }

    private void initScrollView() {
        this.mScroller = new OverScroller(getContext());
        setFocusable(true);
        setDescendantFocusability(262144);
        setWillNotDraw(false);
        ViewConfiguration r0 = ViewConfiguration.get(getContext());
        this.mTouchSlop = r0.getScaledTouchSlop();
        this.mMinimumVelocity = r0.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = r0.getScaledMaximumFlingVelocity();
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker != null) goto L6;
        this.mVelocityTracker = VelocityTracker.obtain();
        return;
    }

    private boolean isOffScreen(View r3) {
        return !isWithinDeltaOfScreen(r3, 0, getHeight());
    }

    private static boolean isViewDescendantOf(View r3, View r4) {
        if (r3 != r4) goto L5;
        return true;
    L5:
        Object r1 = r3.getParent();
        if ((r1 instanceof ViewGroup) == true) goto L8;
    L10:
        return false;
    L8:
        if (isViewDescendantOf((View) r1, r4) == false) goto L10;
        return true;
    }

    private boolean isWithinDeltaOfScreen(View r3, int r4, int r5) {
        r3.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(r3, this.mTempRect);
        if ((this.mTempRect.bottom + r4) >= getScrollY()) goto L5;
    L7:
        return false;
    L5:
        if ((this.mTempRect.top - r4) > (getScrollY() + r5)) goto L7;
        return true;
    }

    private void onNestedScrollInternal(int r11, int r12, int[] r13) {
        int r0 = getScrollY();
        scrollBy(0, r11);
        int r4 = getScrollY() - r0;
        if (r13 == null) goto L5;
        r13[1] = r13[1] + r4;
    L5:
        NestedScrollingChildHelper r2 = this.mChildHelper;
        r2.dispatchNestedScroll(0, r4, 0, r11 - r4, null, r12, r13);
    }

    private void onSecondaryPointerUp(MotionEvent r5) {
        int r0 = r5.getActionIndex();
        if (r5.getPointerId(r0) != this.mActivePointerId) goto L11;
        if (r0 != 0) goto L6;
        int r2 = 1;
    L7:
        this.mLastMotionY = (int) r5.getY(r2);
        this.mActivePointerId = r5.getPointerId(r2);
        if (this.mVelocityTracker == null) goto L12;
        this.mVelocityTracker.clear();
        return;
    L12:
        return;
    L6:
        r2 = 0;
        goto L7
    }

    private void recycleVelocityTracker() {
        if (this.mVelocityTracker == null) goto L6;
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
        return;
    }

    private int releaseVerticalGlow(int r7, float r8) {
        float r0 = 0.0f;
        float r1 = r8 / getWidth();
        float r2 = r7 / getHeight();
        if (EdgeEffectCompat.getDistance(this.mEdgeGlowTop) == 0.0f) goto L8;
        r0 = -EdgeEffectCompat.onPullDistance(this.mEdgeGlowTop, -r2, r1);
        if (EdgeEffectCompat.getDistance(this.mEdgeGlowTop) != 0.0f) goto L12;
        this.mEdgeGlowTop.onRelease();
    L12:
        int r3 = Math.round(getHeight() * r0);
        if (r3 == 0) goto L15;
        invalidate();
    L15:
        return r3;
    L8:
        if (EdgeEffectCompat.getDistance(this.mEdgeGlowBottom) == 0.0f) goto L12;
        r0 = EdgeEffectCompat.onPullDistance(this.mEdgeGlowBottom, r2, 1.0f - r1);
        if (EdgeEffectCompat.getDistance(this.mEdgeGlowBottom) != 0.0f) goto L12;
        this.mEdgeGlowBottom.onRelease();
        goto L12
    }

    private void runAnimatedScroll(boolean r3) {
        if (r3 == false) goto L5;
        startNestedScroll(2, 1);
    L6:
        this.mLastScrollerY = getScrollY();
        ViewCompat.postInvalidateOnAnimation(this);
        return;
    L5:
        stopNestedScroll(1);
        goto L6
    }

    private boolean scrollAndFocus(int r8, int r9, int r10) {
        boolean r0 = true;
        int r1 = getHeight();
        int r2 = getScrollY();
        int r3 = r2 + r1;
        if (r8 != 33) goto L5;
        boolean r4 = true;
    L6:
        View r5 = findFocusableViewInBounds(r4, r9, r10);
        if (r5 != null) goto L9;
        r5 = this;
    L9:
        if (r9 < r2) goto L12;
        if (r10 > r3) goto L12;
        r0 = false;
    L17:
        if (r5 == findFocus()) goto L19;
        r5.requestFocus(r8);
    L19:
        return r0;
    L12:
        if (r4 == false) goto L14;
        int r6 = r9 - r2;
    L15:
        doScrollY(r6);
        goto L17
    L14:
        r6 = r10 - r3;
        goto L15
    L5:
        r4 = false;
        goto L6
    }

    private void scrollToChild(View r3) {
        r3.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(r3, this.mTempRect);
        int r0 = computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
        if (r0 == 0) goto L6;
        scrollBy(0, r0);
        return;
    }

    private boolean scrollToChildRect(Rect r4, boolean r5) {
        int r0 = computeScrollDeltaToGetChildRectOnScreen(r4);
        if (r0 == 0) goto L5;
        boolean r2 = true;
    L6:
        if (r2 == false) goto L10;
        if (r5 == false) goto L9;
        scrollBy(0, r0);
        goto L10
    L9:
        smoothScrollBy(0, r0);
    L10:
        return r2;
    L5:
        r2 = false;
        goto L6
    }

    private boolean shouldAbsorb(EdgeEffect r5, int r6) {
        if (r6 <= 0) goto L6;
        return true;
    L6:
        if (getSplineFlingDistance(-r6) < (EdgeEffectCompat.getDistance(r5) * getHeight())) goto L10;
        return false;
    L10:
        return true;
    }

    private void smoothScrollBy(int r15, int r16, int r17, boolean r18) {
        if (getChildCount() != 0) goto L6;
        return;
    L6:
        if ((AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll) <= 250) goto L9;
        View r3 = getChildAt(0);
        FrameLayout.LayoutParams r4 = (FrameLayout.LayoutParams) r3.getLayoutParams();
        int r5 = (r3.getHeight() + r4.topMargin) + r4.bottomMargin;
        int r6 = (getHeight() - getPaddingTop()) - getPaddingBottom();
        int r9 = getScrollY();
        int r13 = Math.max(0, r5 - r6);
        int r11 = Math.max(0, Math.min(r9 + r16, r13)) - r9;
        this.mScroller.startScroll(getScrollX(), r9, 0, r11, r17);
        runAnimatedScroll(r18);
    L12:
        this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
        return;
    L9:
        if (this.mScroller.isFinished() == true) goto L11;
        abortAnimatedScroll();
    L11:
        scrollBy(r15, r16);
        goto L12
    }

    private boolean stopGlowAnimations(MotionEvent r6) {
        boolean r0 = false;
        if (EdgeEffectCompat.getDistance(this.mEdgeGlowTop) == 0.0f) goto L6;
        EdgeEffectCompat.onPullDistance(this.mEdgeGlowTop, 0.0f, r6.getX() / getWidth());
        r0 = true;
    L6:
        if (EdgeEffectCompat.getDistance(this.mEdgeGlowBottom) == 0.0f) goto L9;
        EdgeEffectCompat.onPullDistance(this.mEdgeGlowBottom, 0.0f, 1.0f - (r6.getX() / getWidth()));
        return true;
    L9:
        return r0;
    }

    @Override // android.view.ViewGroup
    public void addView(View r3) {
        if (getChildCount() > 0) goto L7;
        super.addView(r3);
        return;
    L7:
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    @Override // android.view.ViewGroup
    public void addView(View r3, int r4) {
        if (getChildCount() > 0) goto L7;
        super.addView(r3, r4);
        return;
    L7:
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    @Override // android.view.ViewGroup
    public void addView(View r3, int r4, ViewGroup.LayoutParams r5) {
        if (getChildCount() > 0) goto L7;
        super.addView(r3, r4, r5);
        return;
    L7:
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void addView(View r3, ViewGroup.LayoutParams r4) {
        if (getChildCount() > 0) goto L7;
        super.addView(r3, r4);
        return;
    L7:
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    public boolean arrowScroll(int r12) {
        View r0 = findFocus();
        if (r0 != this) goto L5;
        r0 = null;
    L5:
        View r1 = FocusFinder.getInstance().findNextFocus(this, r0, r12);
        int r2 = getMaxScrollAmount();
        if (r1 != null) goto L8;
    L10:
        int r3 = r2;
        if (r12 == 33) goto L13;
    L15:
        if (r12 == 130) goto L17;
    L19:
        if (r3 != 0) goto L21;
        return false;
    L21:
        if (r12 != 130) goto L23;
        int r4 = r3;
    L24:
        doScrollY(r4);
    L25:
        if (r0 != null) goto L27;
        return true;
    L27:
        if (r0.isFocused() == true) goto L29;
        return true;
    L29:
        if (isOffScreen(r0) == false) goto L35;
        int r5 = getDescendantFocusability();
        setDescendantFocusability(131072);
        requestFocus();
        setDescendantFocusability(r5);
        return true;
    L35:
        return true;
    L23:
        r4 = -r3;
        goto L24
    L17:
        if (getChildCount() <= 0) goto L19;
        View r6 = getChildAt(0);
        FrameLayout.LayoutParams r7 = (FrameLayout.LayoutParams) r6.getLayoutParams();
        r3 = Math.min((r6.getBottom() + r7.bottomMargin) - ((getScrollY() + getHeight()) - getPaddingBottom()), r2);
        goto L19
    L13:
        if (getScrollY() >= r3) goto L15;
        r3 = getScrollY();
        goto L19
    L8:
        if (isWithinDeltaOfScreen(r1, r2, getHeight()) == false) goto L10;
        r1.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(r1, this.mTempRect);
        doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
        r1.requestFocus(r12);
        goto L25
    }

    @Override // android.view.View, androidx.core.view.ScrollingView
    public int computeHorizontalScrollExtent() {
        return super.computeHorizontalScrollExtent();
    }

    @Override // android.view.View, androidx.core.view.ScrollingView
    public int computeHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }

    @Override // android.view.View, androidx.core.view.ScrollingView
    public int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mScroller.isFinished() == false) goto L5;
        return;
    L5:
        this.mScroller.computeScrollOffset();
        int r10 = this.mScroller.getCurrY();
        int r2 = consumeFlingInVerticalStretch(r10 - this.mLastScrollerY);
        this.mLastScrollerY = r10;
        boolean r12 = false;
        this.mScrollConsumed[1] = 0;
        dispatchNestedPreScroll(0, r2, this.mScrollConsumed, null, 1);
        int r3 = r2 - this.mScrollConsumed[1];
        int r6 = getScrollRange();
        if (r3 == 0) goto L9;
        int r4 = getScrollY();
        overScrollByCompat(0, r3, getScrollX(), r4, 0, r6, 0, 0, false);
        int r1 = getScrollY() - r4;
        int r5 = r3 - r1;
        this.mScrollConsumed[1] = 0;
        dispatchNestedScroll(0, r1, 0, r5, this.mScrollOffset, 1, this.mScrollConsumed);
        r3 = r5 - this.mScrollConsumed[1];
    L9:
        if (r3 == 0) goto L25;
        int r7 = getOverScrollMode();
        if (r7 == 0) goto L14;
        if (r7 != 1) goto L15;
        if (r6 > 0) goto L14;
    L15:
        if (r12 == false) goto L23;
        if (r3 >= 0) goto L21;
        if (this.mEdgeGlowTop.isFinished() == false) goto L23;
        this.mEdgeGlowTop.onAbsorb((int) this.mScroller.getCurrVelocity());
        goto L23
    L21:
        if (this.mEdgeGlowBottom.isFinished() == false) goto L23;
        this.mEdgeGlowBottom.onAbsorb((int) this.mScroller.getCurrVelocity());
    L23:
        abortAnimatedScroll();
    L14:
        r12 = true;
    L25:
        if (this.mScroller.isFinished() == true) goto L27;
        ViewCompat.postInvalidateOnAnimation(this);
        return;
    L27:
        stopNestedScroll(1);
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect r11) {
        if (getChildCount() != 0) goto L5;
        return 0;
    L5:
        int r0 = getHeight();
        int r2 = getScrollY();
        int r3 = r2 + r0;
        int r5 = getVerticalFadingEdgeLength();
        if (r11.top <= 0) goto L8;
        r2 = r2 + r5;
    L8:
        View r1 = getChildAt(0);
        FrameLayout.LayoutParams r6 = (FrameLayout.LayoutParams) r1.getLayoutParams();
        if (r11.bottom >= ((r1.getHeight() + r6.topMargin) + r6.bottomMargin)) goto L12;
        r3 = r3 - r5;
    L12:
        if (r11.bottom <= r3) goto L22;
        if (r11.top <= r2) goto L22;
        if (r11.height() <= r0) goto L18;
        int r7 = 0 + (r11.top - r2);
    L31:
        return Math.min(r7, (r1.getBottom() + r6.bottomMargin) - r3);
    L18:
        r7 = 0 + (r11.bottom - r3);
    L22:
        if (r11.top < r2) goto L24;
        return 0;
    L24:
        if (r11.bottom < r3) goto L26;
        return 0;
    L26:
        if (r11.height() <= r0) goto L28;
        int r8 = 0 - (r3 - r11.bottom);
    L30:
        return Math.max(r8, -getScrollY());
    L28:
        r8 = 0 - (r2 - r11.top);
        goto L30
    }

    @Override // android.view.View, androidx.core.view.ScrollingView
    public int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @Override // android.view.View, androidx.core.view.ScrollingView
    public int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    @Override // android.view.View, androidx.core.view.ScrollingView
    public int computeVerticalScrollRange() {
        int r0 = getChildCount();
        int r1 = (getHeight() - getPaddingBottom()) - getPaddingTop();
        if (r0 != 0) goto L5;
        return r1;
    L5:
        View r3 = getChildAt(0);
        FrameLayout.LayoutParams r4 = (FrameLayout.LayoutParams) r3.getLayoutParams();
        int r5 = r3.getBottom() + r4.bottomMargin;
        int r6 = getScrollY();
        int r2 = Math.max(0, r5 - r1);
        if (r6 < 0) goto L11;
        if (r6 > r2) goto L10;
        return r5;
    L10:
        return r5 + (r6 - r2);
    L11:
        return r5 - r6;
    }

    int consumeFlingInVerticalStretch(int r6) {
        int r0 = getHeight();
        if (r6 > 0) goto L5;
    L11:
        if (r6 < 0) goto L13;
    L19:
        return r6;
    L13:
        if (EdgeEffectCompat.getDistance(this.mEdgeGlowBottom) == 0.0f) goto L19;
        int r1 = Math.round((r0 / FLING_DESTRETCH_FACTOR) * EdgeEffectCompat.onPullDistance(this.mEdgeGlowBottom, (r6 * FLING_DESTRETCH_FACTOR) / r0, 0.5f));
        if (r1 == r6) goto L18;
        this.mEdgeGlowBottom.finish();
    L18:
        return r6 - r1;
    L5:
        if (EdgeEffectCompat.getDistance(this.mEdgeGlowTop) == 0.0f) goto L11;
        int r2 = Math.round(((-r0) / FLING_DESTRETCH_FACTOR) * EdgeEffectCompat.onPullDistance(this.mEdgeGlowTop, ((-r6) * FLING_DESTRETCH_FACTOR) / r0, 0.5f));
        if (r2 == r6) goto L10;
        this.mEdgeGlowTop.finish();
    L10:
        return r6 - r2;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent r2) {
        if (super.dispatchKeyEvent(r2) == false) goto L5;
    L8:
        return true;
    L5:
        if (executeKeyEvent(r2) == true) goto L8;
        return false;
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean dispatchNestedFling(float r2, float r3, boolean r4) {
        return this.mChildHelper.dispatchNestedFling(r2, r3, r4);
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean dispatchNestedPreFling(float r2, float r3) {
        return this.mChildHelper.dispatchNestedPreFling(r2, r3);
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean dispatchNestedPreScroll(int r7, int r8, int[] r9, int[] r10) {
        return dispatchNestedPreScroll(r7, r8, r9, r10, 0);
    }

    @Override // androidx.core.view.NestedScrollingChild2
    public boolean dispatchNestedPreScroll(int r7, int r8, int[] r9, int[] r10, int r11) {
        return this.mChildHelper.dispatchNestedPreScroll(r7, r8, r9, r10, r11);
    }

    @Override // androidx.core.view.NestedScrollingChild3
    public void dispatchNestedScroll(int r9, int r10, int r11, int r12, int[] r13, int r14, int[] r15) {
        this.mChildHelper.dispatchNestedScroll(r9, r10, r11, r12, r13, r14, r15);
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean dispatchNestedScroll(int r7, int r8, int r9, int r10, int[] r11) {
        return this.mChildHelper.dispatchNestedScroll(r7, r8, r9, r10, r11);
    }

    @Override // androidx.core.view.NestedScrollingChild2
    public boolean dispatchNestedScroll(int r8, int r9, int r10, int r11, int[] r12, int r13) {
        return this.mChildHelper.dispatchNestedScroll(r8, r9, r10, r11, r12, r13);
    }

    @Override // android.view.View
    public void draw(Canvas r10) {
        super.draw(r10);
        int r0 = getScrollY();
        if (this.mEdgeGlowTop.isFinished() == true) goto L15;
        int r1 = r10.save();
        int r2 = getWidth();
        int r3 = getHeight();
        int r4 = 0;
        int r5 = Math.min(0, r0);
        if (Api21Impl.getClipToPadding(this) == false) goto L8;
        r2 = r2 - (getPaddingLeft() + getPaddingRight());
        r4 = 0 + getPaddingLeft();
    L8:
        if (Api21Impl.getClipToPadding(this) == false) goto L10;
        r3 = r3 - (getPaddingTop() + getPaddingBottom());
        r5 = r5 + getPaddingTop();
    L10:
        r10.translate(r4, r5);
        this.mEdgeGlowTop.setSize(r2, r3);
        if (this.mEdgeGlowTop.draw(r10) == false) goto L13;
        ViewCompat.postInvalidateOnAnimation(this);
    L13:
        r10.restoreToCount(r1);
    L15:
        if (this.mEdgeGlowBottom.isFinished() == true) goto L27;
        int r6 = r10.save();
        int r7 = getWidth();
        int r8 = getHeight();
        int r9 = 0;
        int r11 = Math.max(getScrollRange(), r0) + r8;
        if (Api21Impl.getClipToPadding(this) == false) goto L20;
        r7 = r7 - (getPaddingLeft() + getPaddingRight());
        r9 = 0 + getPaddingLeft();
    L20:
        if (Api21Impl.getClipToPadding(this) == false) goto L22;
        r8 = r8 - (getPaddingTop() + getPaddingBottom());
        r11 = r11 - getPaddingBottom();
    L22:
        r10.translate(r9 - r7, r11);
        r10.rotate(180.0f, r7, 0.0f);
        this.mEdgeGlowBottom.setSize(r7, r8);
        if (this.mEdgeGlowBottom.draw(r10) == false) goto L25;
        ViewCompat.postInvalidateOnAnimation(this);
    L25:
        r10.restoreToCount(r6);
        return;
    }

    public boolean executeKeyEvent(KeyEvent r5) {
        this.mTempRect.setEmpty();
        boolean r0 = canScroll();
        int r1 = TsExtractor.TS_STREAM_TYPE_HDMV_DTS;
        if (r0 == true) goto L21;
        if (isFocused() == true) goto L7;
    L19:
        return false;
    L7:
        if (r5.getKeyCode() == 4) goto L19;
        View r2 = findFocus();
        if (r2 != this) goto L11;
        r2 = null;
    L11:
        View r3 = FocusFinder.getInstance().findNextFocus(this, r2, TsExtractor.TS_STREAM_TYPE_HDMV_DTS);
        if (r3 == null) goto L38;
        if (r3 != this) goto L15;
        return false;
    L15:
        if (r3.requestFocus(TsExtractor.TS_STREAM_TYPE_HDMV_DTS) == false) goto L40;
        return true;
    L40:
        return false;
    L38:
        return false;
    L21:
        if (r5.getAction() == 0) goto L23;
        return false;
    L23:
        switch(r5.getKeyCode()) {
            case 19: goto L34;
            case 20: goto L30;
            case 62: goto L26;
            default: goto L46;
        };
    L46:
        return false;
    L26:
        if (r5.isShiftPressed() == false) goto L28;
        r1 = 33;
    L28:
        pageScroll(r1);
        return false;
    L30:
        if (r5.isAltPressed() == true) goto L44;
        return arrowScroll(TsExtractor.TS_STREAM_TYPE_HDMV_DTS);
    L44:
        return fullScroll(TsExtractor.TS_STREAM_TYPE_HDMV_DTS);
    L34:
        if (r5.isAltPressed() == true) goto L37;
        return arrowScroll(33);
    L37:
        return fullScroll(33);
    }

    public void fling(int r13) {
        if (getChildCount() <= 0) goto L6;
        this.mScroller.fling(getScrollX(), getScrollY(), 0, r13, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        runAnimatedScroll(true);
        return;
    }

    public boolean fullScroll(int r9) {
        if (r9 != 130) goto L5;
        boolean r0 = true;
    L6:
        int r2 = getHeight();
        this.mTempRect.top = 0;
        this.mTempRect.bottom = r2;
        if (r0 == false) goto L12;
        int r1 = getChildCount();
        if (r1 <= 0) goto L12;
        View r3 = getChildAt(r1 - 1);
        FrameLayout.LayoutParams r4 = (FrameLayout.LayoutParams) r3.getLayoutParams();
        this.mTempRect.bottom = (r3.getBottom() + r4.bottomMargin) + getPaddingBottom();
        this.mTempRect.top = this.mTempRect.bottom - r2;
    L12:
        return scrollAndFocus(r9, this.mTempRect.top, this.mTempRect.bottom);
    L5:
        r0 = false;
        goto L6
    }

    @Override // android.view.View
    protected float getBottomFadingEdgeStrength() {
        if (getChildCount() != 0) goto L6;
        return 0.0f;
    L6:
        View r0 = getChildAt(0);
        FrameLayout.LayoutParams r1 = (FrameLayout.LayoutParams) r0.getLayoutParams();
        int r2 = getVerticalFadingEdgeLength();
        int r4 = ((r0.getBottom() + r1.bottomMargin) - getScrollY()) - (getHeight() - getPaddingBottom());
        if (r4 < r2) goto L9;
        return 1.0f;
    L9:
        return r4 / r2;
    }

    public int getMaxScrollAmount() {
        return (int) (getHeight() * 0.5f);
    }

    @Override // android.view.ViewGroup, androidx.core.view.NestedScrollingParent
    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }

    int getScrollRange() {
        if (getChildCount() <= 0) goto L6;
        View r2 = getChildAt(0);
        FrameLayout.LayoutParams r3 = (FrameLayout.LayoutParams) r2.getLayoutParams();
        return Math.max(0, ((r2.getHeight() + r3.topMargin) + r3.bottomMargin) - ((getHeight() - getPaddingTop()) - getPaddingBottom()));
    L6:
        return 0;
    }

    @Override // android.view.View
    protected float getTopFadingEdgeStrength() {
        if (getChildCount() != 0) goto L6;
        return 0.0f;
    L6:
        int r0 = getVerticalFadingEdgeLength();
        int r1 = getScrollY();
        if (r1 < r0) goto L9;
        return 1.0f;
    L9:
        return r1 / r0;
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean hasNestedScrollingParent() {
        return hasNestedScrollingParent(0);
    }

    @Override // androidx.core.view.NestedScrollingChild2
    public boolean hasNestedScrollingParent(int r2) {
        return this.mChildHelper.hasNestedScrollingParent(r2);
    }

    public boolean isFillViewport() {
        return this.mFillViewport;
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean isNestedScrollingEnabled() {
        return this.mChildHelper.isNestedScrollingEnabled();
    }

    public boolean isSmoothScrollingEnabled() {
        return this.mSmoothScrollingEnabled;
    }

    @Override // android.view.ViewGroup
    protected void measureChild(View r4, int r5, int r6) {
        ViewGroup.LayoutParams r0 = r4.getLayoutParams();
        r4.measure(getChildMeasureSpec(r5, getPaddingLeft() + getPaddingRight(), r0.width), View.MeasureSpec.makeMeasureSpec(0, 0));
    }

    @Override // android.view.ViewGroup
    protected void measureChildWithMargins(View r5, int r6, int r7, int r8, int r9) {
        ViewGroup.MarginLayoutParams r0 = (ViewGroup.MarginLayoutParams) r5.getLayoutParams();
        r5.measure(getChildMeasureSpec(r6, (((getPaddingLeft() + getPaddingRight()) + r0.leftMargin) + r0.rightMargin) + r7, r0.width), View.MeasureSpec.makeMeasureSpec(r0.topMargin + r0.bottomMargin, 0));
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mIsLaidOut = false;
    }

    @Override // android.view.View
    public boolean onGenericMotionEvent(MotionEvent r13) {
        boolean r2 = false;
        if (r13.getAction() == 8) goto L5;
    L41:
        return false;
    L5:
        if (this.mIsBeingDragged == true) goto L41;
        if (MotionEventCompat.isFromSource(r13, 2) == false) goto L10;
        float r0 = r13.getAxisValue(9);
    L14:
        if (r0 == 0.0f) goto L41;
        int r1 = (int) (getVerticalScrollFactorCompat() * r0);
        int r3 = getScrollRange();
        int r4 = getScrollY();
        int r5 = r4 - r1;
        boolean r6 = false;
        if (r5 < 0) goto L18;
        if (r5 > r3) goto L28;
    L37:
        if (r5 == r4) goto L40;
        super.scrollTo(getScrollX(), r5);
        return true;
    L40:
        return r6;
    L28:
        if (canOverScroll() == true) goto L30;
    L33:
        if (r2 == false) goto L35;
        EdgeEffectCompat.onPullDistance(this.mEdgeGlowBottom, (r5 - r3) / getHeight(), 0.5f);
        this.mEdgeGlowBottom.onRelease();
        invalidate();
        r6 = true;
    L35:
        r5 = r3;
        goto L37
    L30:
        if (MotionEventCompat.isFromSource(r13, InputDeviceCompat.SOURCE_MOUSE) == true) goto L33;
        r2 = true;
        goto L33
    L18:
        if (canOverScroll() == true) goto L20;
    L23:
        if (r2 == false) goto L25;
        EdgeEffectCompat.onPullDistance(this.mEdgeGlowTop, (-r5) / getHeight(), 0.5f);
        this.mEdgeGlowTop.onRelease();
        invalidate();
        r6 = true;
    L25:
        r5 = 0;
        goto L37
    L20:
        if (MotionEventCompat.isFromSource(r13, InputDeviceCompat.SOURCE_MOUSE) == true) goto L23;
        r2 = true;
        goto L23
    L10:
        if (MotionEventCompat.isFromSource(r13, 4194304) == false) goto L12;
        r0 = r13.getAxisValue(26);
        goto L14
    L12:
        r0 = 0.0f;
        goto L14
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent r18) {
        int r2 = r18.getAction();
        boolean r3 = true;
        if (r2 != 2) goto L8;
        if (this.mIsBeingDragged == false) goto L8;
        return true;
    L8:
        switch((r2 & 255)) {
            case 0: goto L29;
            case 1: goto L25;
            case 2: goto L11;
            case 3: goto L25;
            case 4: goto L46;
            case 5: goto L46;
            case 6: goto L10;
            default: goto L46;
        };
    L10:
        onSecondaryPointerUp(r18);
        goto L46
    L11:
        int r5 = this.mActivePointerId;
        if (r5 == (-1)) goto L46;
        int r8 = r18.findPointerIndex(r5);
        if (r8 != (-1)) goto L17;
        Log.e(TAG, "Invalid pointerId=" + r5 + " in onInterceptTouchEvent");
        goto L46
    L17:
        int r6 = (int) r18.getY(r8);
        if (Math.abs(r6 - this.mLastMotionY) <= this.mTouchSlop) goto L46;
        if ((2 & getNestedScrollAxes()) != 0) goto L46;
        this.mIsBeingDragged = true;
        this.mLastMotionY = r6;
        initVelocityTrackerIfNotExists();
        this.mVelocityTracker.addMovement(r18);
        this.mNestedYOffset = 0;
        ViewParent r4 = getParent();
        if (r4 == null) goto L46;
        r4.requestDisallowInterceptTouchEvent(true);
        goto L46
    L25:
        this.mIsBeingDragged = false;
        this.mActivePointerId = -1;
        recycleVelocityTracker();
        if (this.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange()) == false) goto L28;
        ViewCompat.postInvalidateOnAnimation(this);
    L28:
        stopNestedScroll(0);
        goto L46
    L29:
        int r7 = (int) r18.getY();
        if (inChild((int) r18.getX(), r7) == false) goto L32;
        this.mLastMotionY = r7;
        this.mActivePointerId = r18.getPointerId(0);
        initOrResetVelocityTracker();
        this.mVelocityTracker.addMovement(r18);
        this.mScroller.computeScrollOffset();
        if (stopGlowAnimations(r18) == false) goto L41;
    L44:
        this.mIsBeingDragged = r3;
        startNestedScroll(2, 0);
        goto L46
    L41:
        if (this.mScroller.isFinished() == false) goto L44;
        r3 = false;
        goto L44
    L32:
        if (stopGlowAnimations(r18) == false) goto L34;
    L37:
        this.mIsBeingDragged = r3;
        recycleVelocityTracker();
        goto L46
    L34:
        if (this.mScroller.isFinished() == false) goto L37;
        r3 = false;
    L46:
        return this.mIsBeingDragged;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean r7, int r8, int r9, int r10, int r11) {
        super.onLayout(r7, r8, r9, r10, r11);
        this.mIsLayoutDirty = false;
        if (this.mChildToScrollTo != null) goto L5;
    L7:
        this.mChildToScrollTo = null;
        if (this.mIsLaidOut == false) goto L10;
    L18:
        scrollTo(getScrollX(), getScrollY());
        this.mIsLaidOut = true;
        return;
    L10:
        if (this.mSavedState == null) goto L12;
        scrollTo(getScrollX(), this.mSavedState.scrollPosition);
        this.mSavedState = null;
    L12:
        int r2 = 0;
        if (getChildCount() <= 0) goto L15;
        View r1 = getChildAt(0);
        FrameLayout.LayoutParams r3 = (FrameLayout.LayoutParams) r1.getLayoutParams();
        r2 = (r1.getMeasuredHeight() + r3.topMargin) + r3.bottomMargin;
    L15:
        int r4 = ((r11 - r9) - getPaddingTop()) - getPaddingBottom();
        int r5 = getScrollY();
        int r6 = clamp(r5, r4, r2);
        if (r6 == r5) goto L18;
        scrollTo(getScrollX(), r6);
        goto L18
    L5:
        if (isViewDescendantOf(this.mChildToScrollTo, this) == false) goto L7;
        scrollToChild(this.mChildToScrollTo);
        goto L7
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int r8, int r9) {
        super.onMeasure(r8, r9);
        if (this.mFillViewport == true) goto L6;
        return;
    L6:
        if (View.MeasureSpec.getMode(r9) != 0) goto L9;
        return;
    L9:
        if (getChildCount() <= 0) goto L14;
        View r1 = getChildAt(0);
        FrameLayout.LayoutParams r2 = (FrameLayout.LayoutParams) r1.getLayoutParams();
        int r3 = r1.getMeasuredHeight();
        int r4 = (((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom()) - r2.topMargin) - r2.bottomMargin;
        if (r3 >= r4) goto L15;
        r1.measure(getChildMeasureSpec(r8, ((getPaddingLeft() + getPaddingRight()) + r2.leftMargin) + r2.rightMargin, r2.width), View.MeasureSpec.makeMeasureSpec(r4, 1073741824));
        return;
    L15:
        return;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public boolean onNestedFling(View r3, float r4, float r5, boolean r6) {
        if (r6 == true) goto L5;
        dispatchNestedFling(0.0f, r5, true);
        fling((int) r5);
        return true;
    L5:
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public boolean onNestedPreFling(View r2, float r3, float r4) {
        return dispatchNestedPreFling(r3, r4);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public void onNestedPreScroll(View r7, int r8, int r9, int[] r10) {
        onNestedPreScroll(r7, r8, r9, r10, 0);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedPreScroll(View r7, int r8, int r9, int[] r10, int r11) {
        dispatchNestedPreScroll(r8, r9, r10, null, r11);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public void onNestedScroll(View r3, int r4, int r5, int r6, int r7) {
        onNestedScrollInternal(r7, 0, null);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedScroll(View r2, int r3, int r4, int r5, int r6, int r7) {
        onNestedScrollInternal(r6, r7, null);
    }

    @Override // androidx.core.view.NestedScrollingParent3
    public void onNestedScroll(View r1, int r2, int r3, int r4, int r5, int r6, int[] r7) {
        onNestedScrollInternal(r5, r6, r7);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public void onNestedScrollAccepted(View r2, View r3, int r4) {
        onNestedScrollAccepted(r2, r3, r4, 0);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedScrollAccepted(View r2, View r3, int r4, int r5) {
        this.mParentHelper.onNestedScrollAccepted(r2, r3, r4, r5);
        startNestedScroll(2, r5);
    }

    @Override // android.view.View
    protected void onOverScrolled(int r1, int r2, boolean r3, boolean r4) {
        super.scrollTo(r1, r2);
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int r4, Rect r5) {
        if (r4 != 2) goto L6;
        r4 = TsExtractor.TS_STREAM_TYPE_HDMV_DTS;
    L8:
        if (r5 != null) goto L10;
        View r0 = FocusFinder.getInstance().findNextFocus(this, null, r4);
    L12:
        if (r0 != null) goto L15;
        return false;
    L15:
        if (isOffScreen(r0) == false) goto L18;
        return false;
    L18:
        return r0.requestFocus(r4, r5);
    L10:
        r0 = FocusFinder.getInstance().findNextFocusFromRect(this, r5, r4);
        goto L12
    L6:
        if (r4 != 1) goto L8;
        r4 = 33;
        goto L8
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable r3) {
        if ((r3 instanceof SavedState) == true) goto L6;
        super.onRestoreInstanceState(r3);
        return;
    L6:
        SavedState r0 = (SavedState) r3;
        super.onRestoreInstanceState(r0.getSuperState());
        this.mSavedState = r0;
        requestLayout();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState r1 = new SavedState(super.onSaveInstanceState());
        r1.scrollPosition = getScrollY();
        return r1;
    }

    @Override // android.view.View
    protected void onScrollChanged(int r8, int r9, int r10, int r11) {
        super.onScrollChanged(r8, r9, r10, r11);
        if (this.mOnScrollChangeListener == null) goto L6;
        this.mOnScrollChangeListener.onScrollChange(this, r8, r9, r10, r11);
        return;
    }

    @Override // android.view.View
    protected void onSizeChanged(int r3, int r4, int r5, int r6) {
        super.onSizeChanged(r3, r4, r5, r6);
        View r0 = findFocus();
        if (r0 == null) goto L10;
        if (this != r0) goto L7;
        return;
    L7:
        if (isWithinDeltaOfScreen(r0, 0, r6) == false) goto L11;
        r0.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(r0, this.mTempRect);
        doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
        return;
    L11:
        return;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public boolean onStartNestedScroll(View r2, View r3, int r4) {
        return onStartNestedScroll(r2, r3, r4, 0);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public boolean onStartNestedScroll(View r2, View r3, int r4, int r5) {
        if ((r4 & 2) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public void onStopNestedScroll(View r2) {
        onStopNestedScroll(r2, 0);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onStopNestedScroll(View r2, int r3) {
        this.mParentHelper.onStopNestedScroll(r2, r3);
        stopNestedScroll(r3);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent r23) {
        initVelocityTrackerIfNotExists();
        int r11 = r23.getActionMasked();
        if (r11 != 0) goto L5;
        this.mNestedYOffset = 0;
    L5:
        MotionEvent r13 = MotionEvent.obtain(r23);
        r13.offsetLocation(0.0f, this.mNestedYOffset);
        switch(r11) {
            case 0: goto L84;
            case 1: goto L72;
            case 2: goto L18;
            case 3: goto L11;
            case 4: goto L96;
            case 5: goto L9;
            case 6: goto L8;
            default: goto L96;
        };
    L8:
        onSecondaryPointerUp(r23);
        this.mLastMotionY = (int) r23.getY(r23.findPointerIndex(this.mActivePointerId));
        goto L96
    L9:
        int r1 = r23.getActionIndex();
        this.mLastMotionY = (int) r23.getY(r1);
        this.mActivePointerId = r23.getPointerId(r1);
        goto L96
    L18:
        int r15 = r23.findPointerIndex(this.mActivePointerId);
        if (r15 != (-1)) goto L21;
        Log.e(TAG, "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent");
        goto L96
    L21:
        int r6 = (int) r23.getY(r15);
        int r2 = this.mLastMotionY - r6;
        int r3 = r2 - releaseVerticalGlow(r2, r23.getX(r15));
        if (this.mIsBeingDragged == false) goto L24;
    L32:
        int r4 = r3;
    L34:
        if (this.mIsBeingDragged == false) goto L96;
        if (dispatchNestedPreScroll(0, r4, this.mScrollConsumed, this.mScrollOffset, 0) == false) goto L38;
        r4 = r4 - this.mScrollConsumed[1];
        this.mNestedYOffset += this.mScrollOffset[1];
    L38:
        this.mLastMotionY = r6 - this.mScrollOffset[1];
        int r16 = getScrollY();
        int r7 = getScrollRange();
        int r5 = getOverScrollMode();
        if (r5 == 0) goto L44;
        if (r5 != 1) goto L43;
        if (r7 > 0) goto L44;
    L43:
        boolean r8 = false;
    L45:
        boolean r17 = r8;
        int r9 = r4;
        if (overScrollByCompat(0, r4, 0, getScrollY(), 0, r7, 0, 0, true) == true) goto L48;
    L50:
        boolean r10 = false;
    L51:
        boolean r20 = r10;
        int r12 = getScrollY() - r16;
        this.mScrollConsumed[1] = 0;
        dispatchNestedScroll(0, r12, 0, r9 - r12, this.mScrollOffset, 0, this.mScrollConsumed);
        this.mLastMotionY -= this.mScrollOffset[1];
        this.mNestedYOffset += this.mScrollOffset[1];
        if (r17 == false) goto L68;
        int r14 = r9 - this.mScrollConsumed[1];
        int r18 = r16 + r14;
        if (r18 >= 0) goto L58;
        EdgeEffectCompat.onPullDistance(this.mEdgeGlowTop, (-r14) / getHeight(), r23.getX(r15) / getWidth());
        if (this.mEdgeGlowBottom.isFinished() == true) goto L63;
        this.mEdgeGlowBottom.onRelease();
    L63:
        if (this.mEdgeGlowTop.isFinished() == true) goto L65;
    L66:
        ViewCompat.postInvalidateOnAnimation(this);
        r20 = false;
        goto L68
    L65:
        if (this.mEdgeGlowBottom.isFinished() == true) goto L68;
    L58:
        if (r18 <= r7) goto L63;
        EdgeEffectCompat.onPullDistance(this.mEdgeGlowBottom, r14 / getHeight(), 1.0f - (r23.getX(r15) / getWidth()));
        if (this.mEdgeGlowTop.isFinished() == true) goto L63;
        this.mEdgeGlowTop.onRelease();
    L68:
        if (r20 == false) goto L96;
        this.mVelocityTracker.clear();
        goto L96
    L48:
        if (hasNestedScrollingParent(0) == true) goto L50;
        r10 = true;
    L44:
        r8 = true;
        goto L45
    L24:
        if (Math.abs(r3) <= this.mTouchSlop) goto L32;
        ViewParent r19 = getParent();
        if (r19 == null) goto L28;
        r19.requestDisallowInterceptTouchEvent(true);
    L28:
        this.mIsBeingDragged = true;
        if (r3 <= 0) goto L31;
        r4 = r3 - this.mTouchSlop;
        goto L34
    L31:
        r4 = r3 + this.mTouchSlop;
        goto L34
    L72:
        VelocityTracker r21 = this.mVelocityTracker;
        r21.computeCurrentVelocity(1000, this.mMaximumVelocity);
        int r22 = (int) r21.getYVelocity(this.mActivePointerId);
        if (Math.abs(r22) < this.mMinimumVelocity) goto L80;
        if (edgeEffectFling(r22) == false) goto L77;
    L82:
        this.mActivePointerId = -1;
        endDrag();
        goto L96
    L77:
        if (dispatchNestedPreFling(0.0f, -r22) == true) goto L82;
        dispatchNestedFling(0.0f, -r22, true);
        fling(-r22);
        goto L82
    L80:
        if (this.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange()) == false) goto L82;
        ViewCompat.postInvalidateOnAnimation(this);
        goto L82
    L11:
        if (this.mIsBeingDragged == true) goto L13;
    L17:
        this.mActivePointerId = -1;
        endDrag();
        goto L96
    L13:
        if (getChildCount() <= 0) goto L17;
        if (this.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange()) == false) goto L17;
        ViewCompat.postInvalidateOnAnimation(this);
        goto L17
    L84:
        if (getChildCount() != 0) goto L87;
        return false;
    L87:
        if (this.mIsBeingDragged == false) goto L92;
        ViewParent r24 = getParent();
        if (r24 == null) goto L92;
        r24.requestDisallowInterceptTouchEvent(true);
    L92:
        if (this.mScroller.isFinished() == true) goto L94;
        abortAnimatedScroll();
    L94:
        this.mLastMotionY = (int) r23.getY();
        this.mActivePointerId = r23.getPointerId(0);
        startNestedScroll(2, 0);
    L96:
        if (this.mVelocityTracker == null) goto L98;
        this.mVelocityTracker.addMovement(r13);
    L98:
        r13.recycle();
        return true;
    }

    boolean overScrollByCompat(int r25, int r26, int r27, int r28, int r29, int r30, int r31, int r32, boolean r33) {
        int r1 = getOverScrollMode();
        if (computeHorizontalScrollRange() <= computeHorizontalScrollExtent()) goto L5;
        boolean r2 = true;
    L7:
        if (computeVerticalScrollRange() <= computeVerticalScrollExtent()) goto L9;
        boolean r3 = true;
    L10:
        if (r1 == 0) goto L15;
        if (r1 != 1) goto L14;
        if (r2 == true) goto L15;
    L14:
        boolean r6 = false;
    L16:
        if (r1 == 0) goto L21;
        if (r1 != 1) goto L20;
        if (r3 == true) goto L21;
    L20:
        boolean r7 = false;
    L22:
        int r8 = r27 + r25;
        if (r6 == true) goto L25;
        int r9 = 0;
    L26:
        int r10 = r28 + r26;
        if (r7 == true) goto L29;
        int r11 = 0;
    L30:
        int r12 = -r9;
        int r13 = r9 + r29;
        int r14 = -r11;
        int r15 = r11 + r30;
        if (r8 <= r13) goto L33;
        int r18 = r13;
        boolean r16 = true;
    L37:
        if (r10 <= r15) goto L39;
        int r19 = r15;
        boolean r17 = true;
    L42:
        if (r17 == false) goto L47;
        if (hasNestedScrollingParent(1) == true) goto L47;
        this.mScroller.springBack(r18, r19, 0, 0, 0, getScrollRange());
    L47:
        onOverScrolled(r18, r19, r16, r17);
        if (r16 == true) goto L52;
        if (r17 == true) goto L52;
        return false;
    L52:
        return true;
    L39:
        if (r10 >= r14) goto L41;
        r19 = r14;
        r17 = true;
        goto L42
    L41:
        r19 = r10;
        r17 = false;
        goto L42
    L33:
        if (r8 >= r12) goto L35;
        r18 = r12;
        r16 = true;
        goto L37
    L35:
        r18 = r8;
        r16 = false;
        goto L37
    L29:
        r11 = r32;
        goto L30
    L25:
        r9 = r31;
    L21:
        r7 = true;
    L15:
        r6 = true;
        goto L16
    L9:
        r3 = false;
        goto L10
    L5:
        r2 = false;
        goto L7
    }

    public boolean pageScroll(int r9) {
        if (r9 != 130) goto L5;
        boolean r0 = true;
    L6:
        int r2 = getHeight();
        if (r0 == false) goto L14;
        this.mTempRect.top = getScrollY() + r2;
        int r1 = getChildCount();
        if (r1 <= 0) goto L17;
        View r3 = getChildAt(r1 - 1);
        int r5 = (r3.getBottom() + ((FrameLayout.LayoutParams) r3.getLayoutParams()).bottomMargin) + getPaddingBottom();
        if ((this.mTempRect.top + r2) <= r5) goto L17;
        this.mTempRect.top = r5 - r2;
    L17:
        this.mTempRect.bottom = this.mTempRect.top + r2;
        return scrollAndFocus(r9, this.mTempRect.top, this.mTempRect.bottom);
    L14:
        this.mTempRect.top = getScrollY() - r2;
        if (this.mTempRect.top >= 0) goto L17;
        this.mTempRect.top = 0;
        goto L17
    L5:
        r0 = false;
        goto L6
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View r2, View r3) {
        if (this.mIsLayoutDirty == true) goto L5;
        scrollToChild(r3);
    L6:
        super.requestChildFocus(r2, r3);
        return;
    L5:
        this.mChildToScrollTo = r3;
        goto L6
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View r4, Rect r5, boolean r6) {
        r5.offset(r4.getLeft() - r4.getScrollX(), r4.getTop() - r4.getScrollY());
        return scrollToChildRect(r5, r6);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean r1) {
        if (r1 == false) goto L4;
        recycleVelocityTracker();
    L4:
        super.requestDisallowInterceptTouchEvent(r1);
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        this.mIsLayoutDirty = true;
        super.requestLayout();
    }

    @Override // android.view.View
    public void scrollTo(int r8, int r9) {
        if (getChildCount() <= 0) goto L10;
        View r0 = getChildAt(0);
        FrameLayout.LayoutParams r1 = (FrameLayout.LayoutParams) r0.getLayoutParams();
        int r2 = (getWidth() - getPaddingLeft()) - getPaddingRight();
        int r3 = (r0.getWidth() + r1.leftMargin) + r1.rightMargin;
        int r4 = (getHeight() - getPaddingTop()) - getPaddingBottom();
        int r5 = (r0.getHeight() + r1.topMargin) + r1.bottomMargin;
        int r10 = clamp(r8, r2, r3);
        int r11 = clamp(r9, r4, r5);
        if (r10 == getScrollX()) goto L7;
    L8:
        super.scrollTo(r10, r11);
        return;
    L7:
        if (r11 != getScrollY()) goto L8;
        return;
    }

    public void setFillViewport(boolean r2) {
        if (r2 == this.mFillViewport) goto L6;
        this.mFillViewport = r2;
        requestLayout();
        return;
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public void setNestedScrollingEnabled(boolean r2) {
        this.mChildHelper.setNestedScrollingEnabled(r2);
    }

    public void setOnScrollChangeListener(OnScrollChangeListener r1) {
        this.mOnScrollChangeListener = r1;
    }

    public void setSmoothScrollingEnabled(boolean r1) {
        this.mSmoothScrollingEnabled = r1;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    public final void smoothScrollBy(int r3, int r4) {
        smoothScrollBy(r3, r4, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, false);
    }

    public final void smoothScrollBy(int r2, int r3, int r4) {
        smoothScrollBy(r2, r3, r4, false);
    }

    public final void smoothScrollTo(int r3, int r4) {
        smoothScrollTo(r3, r4, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, false);
    }

    public final void smoothScrollTo(int r2, int r3, int r4) {
        smoothScrollTo(r2, r3, r4, false);
    }

    void smoothScrollTo(int r3, int r4, int r5, boolean r6) {
        smoothScrollBy(r3 - getScrollX(), r4 - getScrollY(), r5, r6);
    }

    void smoothScrollTo(int r2, int r3, boolean r4) {
        smoothScrollTo(r2, r3, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, r4);
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean startNestedScroll(int r2) {
        return startNestedScroll(r2, 0);
    }

    @Override // androidx.core.view.NestedScrollingChild2
    public boolean startNestedScroll(int r2, int r3) {
        return this.mChildHelper.startNestedScroll(r2, r3);
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public void stopNestedScroll() {
        stopNestedScroll(0);
    }

    @Override // androidx.core.view.NestedScrollingChild2
    public void stopNestedScroll(int r2) {
        this.mChildHelper.stopNestedScroll(r2);
    }
}
