package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/* JADX INFO: loaded from: classes.dex */
public class LinearSmoothScroller extends RecyclerView.SmoothScroller {
    private static final boolean DEBUG = false;
    private static final float MILLISECONDS_PER_INCH = 25.0f;
    public static final int SNAP_TO_ANY = 0;
    public static final int SNAP_TO_END = 1;
    public static final int SNAP_TO_START = -1;
    private static final float TARGET_SEEK_EXTRA_SCROLL_RATIO = 1.2f;
    private static final int TARGET_SEEK_SCROLL_DISTANCE_PX = 10000;
    protected final DecelerateInterpolator mDecelerateInterpolator;
    private final DisplayMetrics mDisplayMetrics;
    private boolean mHasCalculatedMillisPerPixel;
    protected int mInterimTargetDx;
    protected int mInterimTargetDy;
    protected final LinearInterpolator mLinearInterpolator;
    private float mMillisPerPixel;
    protected PointF mTargetVector;

    public LinearSmoothScroller(Context r2) {
        this.mLinearInterpolator = new LinearInterpolator();
        this.mDecelerateInterpolator = new DecelerateInterpolator();
        this.mHasCalculatedMillisPerPixel = false;
        this.mInterimTargetDx = 0;
        this.mInterimTargetDy = 0;
        this.mDisplayMetrics = r2.getResources().getDisplayMetrics();
    }

    private int clampApplyScroll(int r3, int r4) {
        int r5 = r3 - r4;
        if ((r3 * r5) > 0) goto L6;
        return 0;
    L6:
        return r5;
    }

    private float getSpeedPerPixel() {
        if (this.mHasCalculatedMillisPerPixel == true) goto L6;
        this.mMillisPerPixel = calculateSpeedPerPixel(this.mDisplayMetrics);
        this.mHasCalculatedMillisPerPixel = true;
    L6:
        return this.mMillisPerPixel;
    }

    public int calculateDtToFit(int r3, int r4, int r5, int r6, int r7) {
        switch(r7) {
            case -1: goto L16;
            case 0: goto L7;
            case 1: goto L6;
            default: goto L4;
        };
    L7:
        int r0 = r5 - r3;
        if (r0 <= 0) goto L10;
        return r0;
    L10:
        int r1 = r6 - r4;
        if (r1 >= 0) goto L13;
        return r1;
    L13:
        return 0;
    L4:
        throw new IllegalArgumentException("snap preference should be one of the constants defined in SmoothScroller, starting with SNAP_");
    L6:
        return r6 - r4;
    L16:
        return r5 - r3;
    }

    public int calculateDxToMakeVisible(View r11, int r12) {
        RecyclerView.LayoutManager r0 = getLayoutManager();
        if (r0 == null) goto L10;
        if (r0.canScrollHorizontally() == false) goto L12;
        RecyclerView.LayoutParams r1 = (RecyclerView.LayoutParams) r11.getLayoutParams();
        return calculateDtToFit(r0.getDecoratedLeft(r11) - r1.leftMargin, r0.getDecoratedRight(r11) + r1.rightMargin, r0.getPaddingLeft(), r0.getWidth() - r0.getPaddingRight(), r12);
    L12:
        return 0;
    L10:
        return 0;
    }

    public int calculateDyToMakeVisible(View r11, int r12) {
        RecyclerView.LayoutManager r0 = getLayoutManager();
        if (r0 == null) goto L10;
        if (r0.canScrollVertically() == false) goto L12;
        RecyclerView.LayoutParams r1 = (RecyclerView.LayoutParams) r11.getLayoutParams();
        return calculateDtToFit(r0.getDecoratedTop(r11) - r1.topMargin, r0.getDecoratedBottom(r11) + r1.bottomMargin, r0.getPaddingTop(), r0.getHeight() - r0.getPaddingBottom(), r12);
    L12:
        return 0;
    L10:
        return 0;
    }

    protected float calculateSpeedPerPixel(DisplayMetrics r3) {
        return MILLISECONDS_PER_INCH / r3.densityDpi;
    }

    protected int calculateTimeForDeceleration(int r5) {
        return (int) Math.ceil(((double) calculateTimeForScrolling(r5)) / 0.3356d);
    }

    protected int calculateTimeForScrolling(int r3) {
        return (int) Math.ceil(Math.abs(r3) * getSpeedPerPixel());
    }

    protected int getHorizontalSnapPreference() {
        if (this.mTargetVector != null) goto L5;
    L11:
        return 0;
    L5:
        if (this.mTargetVector.x == 0.0f) goto L11;
        if (this.mTargetVector.x <= 0.0f) goto L10;
        return 1;
    L10:
        return -1;
    }

    protected int getVerticalSnapPreference() {
        if (this.mTargetVector != null) goto L5;
    L11:
        return 0;
    L5:
        if (this.mTargetVector.y == 0.0f) goto L11;
        if (this.mTargetVector.y <= 0.0f) goto L10;
        return 1;
    L10:
        return -1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
    protected void onSeekTargetStep(int r2, int r3, RecyclerView.State r4, RecyclerView.SmoothScroller.Action r5) {
        if (getChildCount() != 0) goto L6;
        stop();
        return;
    L6:
        this.mInterimTargetDx = clampApplyScroll(this.mInterimTargetDx, r2);
        this.mInterimTargetDy = clampApplyScroll(this.mInterimTargetDy, r3);
        if (this.mInterimTargetDx == 0) goto L9;
        return;
    L9:
        if (this.mInterimTargetDy != 0) goto L13;
        updateActionForInterimTarget(r5);
        return;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
    protected void onStart() {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
    protected void onStop() {
        this.mInterimTargetDy = 0;
        this.mInterimTargetDx = 0;
        this.mTargetVector = null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
    protected void onTargetFound(View r8, RecyclerView.State r9, RecyclerView.SmoothScroller.Action r10) {
        int r0 = calculateDxToMakeVisible(r8, getHorizontalSnapPreference());
        int r1 = calculateDyToMakeVisible(r8, getVerticalSnapPreference());
        int r3 = calculateTimeForDeceleration((int) Math.sqrt((r0 * r0) + (r1 * r1)));
        if (r3 <= 0) goto L6;
        r10.update(-r0, -r1, r3, this.mDecelerateInterpolator);
        return;
    }

    protected void updateActionForInterimTarget(RecyclerView.SmoothScroller.Action r7) {
        PointF r0 = computeScrollVectorForPosition(getTargetPosition());
        if (r0 != null) goto L5;
    L11:
        r7.jumpTo(getTargetPosition());
        stop();
        return;
    L5:
        if (r0.x == 0.0f) goto L7;
    L9:
        normalize(r0);
        this.mTargetVector = r0;
        this.mInterimTargetDx = (int) (r0.x * 10000.0f);
        this.mInterimTargetDy = (int) (r0.y * 10000.0f);
        r7.update((int) (this.mInterimTargetDx * TARGET_SEEK_EXTRA_SCROLL_RATIO), (int) (this.mInterimTargetDy * TARGET_SEEK_EXTRA_SCROLL_RATIO), (int) (calculateTimeForScrolling(10000) * TARGET_SEEK_EXTRA_SCROLL_RATIO), this.mLinearInterpolator);
        return;
    L7:
        if (r0.y != 0.0f) goto L9;
        goto L9
    }
}
