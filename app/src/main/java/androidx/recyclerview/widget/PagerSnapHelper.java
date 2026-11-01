package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public class PagerSnapHelper extends SnapHelper {
    private static final int MAX_SCROLL_ON_FLING_DURATION = 100;
    private OrientationHelper mHorizontalHelper;
    private OrientationHelper mVerticalHelper;

    public PagerSnapHelper() {
    }

    private int distanceToCenter(View r4, OrientationHelper r5) {
        return (r5.getDecoratedStart(r4) + (r5.getDecoratedMeasurement(r4) / 2)) - (r5.getStartAfterPadding() + (r5.getTotalSpace() / 2));
    }

    private View findCenterView(RecyclerView.LayoutManager r9, OrientationHelper r10) {
        int r0 = r9.getChildCount();
        if (r0 != 0) goto L6;
        return null;
    L6:
        View r1 = null;
        int r2 = r10.getStartAfterPadding() + (r10.getTotalSpace() / 2);
        int r3 = Integer.MAX_VALUE;
        int r4 = 0;
    L7:
        if (r4 >= r0) goto L12;
        View r5 = r9.getChildAt(r4);
        int r7 = Math.abs((r10.getDecoratedStart(r5) + (r10.getDecoratedMeasurement(r5) / 2)) - r2);
        if (r7 >= r3) goto L11;
        r3 = r7;
        r1 = r5;
    L11:
        r4 = r4 + 1;
        goto L7
    L12:
        return r1;
    }

    private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager r2) {
        if (this.mHorizontalHelper != null) goto L5;
    L6:
        this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(r2);
    L8:
        return this.mHorizontalHelper;
    L5:
        if (this.mHorizontalHelper.mLayoutManager == r2) goto L8;
        goto L6
    }

    private OrientationHelper getOrientationHelper(RecyclerView.LayoutManager r2) {
        if (r2.canScrollVertically() == false) goto L7;
        return getVerticalHelper(r2);
    L7:
        if (r2.canScrollHorizontally() == true) goto L9;
        return null;
    L9:
        return getHorizontalHelper(r2);
    }

    private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager r2) {
        if (this.mVerticalHelper != null) goto L5;
    L6:
        this.mVerticalHelper = OrientationHelper.createVerticalHelper(r2);
    L8:
        return this.mVerticalHelper;
    L5:
        if (this.mVerticalHelper.mLayoutManager == r2) goto L8;
        goto L6
    }

    private boolean isForwardFling(RecyclerView.LayoutManager r4, int r5, int r6) {
        if (r4.canScrollHorizontally() == false) goto L8;
        if (r5 <= 0) goto L7;
        return true;
    L7:
        return false;
    L8:
        if (r6 <= 0) goto L11;
        return true;
    L11:
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean isReverseLayout(RecyclerView.LayoutManager r7) {
        int r0 = r7.getItemCount();
        if ((r7 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) == false) goto L12;
        PointF r3 = ((RecyclerView.SmoothScroller.ScrollVectorProvider) r7).computeScrollVectorForPosition(r0 - 1);
        if (r3 == null) goto L12;
        if (r3.x >= 0.0f) goto L9;
    L10:
        return true;
    L9:
        if (r3.y < 0.0f) goto L10;
        return false;
    L12:
        return false;
    }

    @Override // androidx.recyclerview.widget.SnapHelper
    public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager r5, View r6) {
        int[] r0 = new int[2];
        if (r5.canScrollHorizontally() == false) goto L5;
        r0[0] = distanceToCenter(r6, getHorizontalHelper(r5));
    L7:
        if (r5.canScrollVertically() == false) goto L9;
        r0[1] = distanceToCenter(r6, getVerticalHelper(r5));
    L10:
        return r0;
    L9:
        r0[1] = 0;
        goto L10
    L5:
        r0[0] = 0;
        goto L7
    }

    @Override // androidx.recyclerview.widget.SnapHelper
    protected RecyclerView.SmoothScroller createScroller(RecyclerView.LayoutManager r3) {
        if ((r3 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) == true) goto L6;
        return null;
    L6:
        final Context r1 = this.mRecyclerView.getContext();
        return new C05031(this, r1);
    }

    @Override // androidx.recyclerview.widget.SnapHelper
    public View findSnapView(RecyclerView.LayoutManager r2) {
        if (r2.canScrollVertically() == false) goto L7;
        return findCenterView(r2, getVerticalHelper(r2));
    L7:
        if (r2.canScrollHorizontally() == true) goto L9;
        return null;
    L9:
        return findCenterView(r2, getHorizontalHelper(r2));
    }

    @Override // androidx.recyclerview.widget.SnapHelper
    public int findTargetSnapPosition(RecyclerView.LayoutManager r13, int r14, int r15) {
        int r0 = r13.getItemCount();
        if (r0 != 0) goto L5;
        return -1;
    L5:
        OrientationHelper r2 = getOrientationHelper(r13);
        if (r2 != null) goto L8;
        return -1;
    L8:
        View r3 = null;
        int r4 = Integer.MIN_VALUE;
        View r5 = null;
        int r6 = Integer.MAX_VALUE;
        int r7 = r13.getChildCount();
        int r8 = 0;
    L9:
        if (r8 >= r7) goto L21;
        View r9 = r13.getChildAt(r8);
        if (r9 == null) goto L20;
        int r10 = distanceToCenter(r9, r2);
        if (r10 > 0) goto L17;
        if (r10 <= r4) goto L17;
        r4 = r10;
        r3 = r9;
    L17:
        if (r10 < 0) goto L20;
        if (r10 >= r6) goto L20;
        r6 = r10;
        r5 = r9;
    L20:
        r8 = r8 + 1;
        goto L9
    L21:
        boolean r11 = isForwardFling(r13, r14, r15);
        if (r11 == false) goto L26;
        if (r5 == null) goto L26;
        return r13.getPosition(r5);
    L26:
        if (r11 == true) goto L30;
        if (r3 == null) goto L30;
        return r13.getPosition(r3);
    L30:
        if (r11 == false) goto L32;
        View r12 = r3;
    L33:
        if (r12 != null) goto L35;
        return -1;
    L35:
        int r16 = r13.getPosition(r12);
        if (isReverseLayout(r13) != r11) goto L38;
        int r17 = -1;
    L39:
        int r18 = r17 + r16;
        if (r18 < 0) goto L44;
        if (r18 >= r0) goto L44;
        return r18;
    L44:
        return -1;
    L38:
        r17 = 1;
        goto L39
    L32:
        r12 = r5;
        goto L33
    }
}
