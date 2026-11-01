package androidx.recyclerview.widget;

import android.graphics.PointF;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public class LinearSnapHelper extends SnapHelper {
    private static final float INVALID_DISTANCE = 1.0f;
    private OrientationHelper mHorizontalHelper;
    private OrientationHelper mVerticalHelper;

    public LinearSnapHelper() {
    }

    private float computeDistancePerChild(RecyclerView.LayoutManager r11, OrientationHelper r12) {
        View r0 = null;
        View r1 = null;
        int r2 = Integer.MAX_VALUE;
        int r3 = Integer.MIN_VALUE;
        int r4 = r11.getChildCount();
        if (r4 != 0) goto L5;
        return 1.0f;
    L5:
        int r6 = 0;
    L6:
        if (r6 >= r4) goto L15;
        View r7 = r11.getChildAt(r6);
        int r8 = r11.getPosition(r7);
        if (r8 == (-1)) goto L14;
        if (r8 >= r2) goto L12;
        r2 = r8;
        r0 = r7;
    L12:
        if (r8 <= r3) goto L14;
        r3 = r8;
        r1 = r7;
    L14:
        r6 = r6 + 1;
        goto L6
    L15:
        if (r0 == null) goto L23;
        if (r1 == null) goto L23;
        int r9 = Math.max(r12.getDecoratedEnd(r0), r12.getDecoratedEnd(r1)) - Math.min(r12.getDecoratedStart(r0), r12.getDecoratedStart(r1));
        if (r9 != 0) goto L22;
        return 1.0f;
    L22:
        return (r9 * 1.0f) / ((r3 - r2) + 1);
    L23:
        return 1.0f;
    }

    private int distanceToCenter(View r4, OrientationHelper r5) {
        return (r5.getDecoratedStart(r4) + (r5.getDecoratedMeasurement(r4) / 2)) - (r5.getStartAfterPadding() + (r5.getTotalSpace() / 2));
    }

    private int estimateNextPositionDiffForFling(RecyclerView.LayoutManager r7, OrientationHelper r8, int r9, int r10) {
        int[] r0 = calculateScrollDistance(r9, r10);
        float r1 = computeDistancePerChild(r7, r8);
        if (r1 > 0.0f) goto L6;
        return 0;
    L6:
        if (Math.abs(r0[0]) <= Math.abs(r0[1])) goto L8;
        int r2 = r0[0];
    L10:
        return Math.round(r2 / r1);
    L8:
        r2 = r0[1];
        goto L10
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
    public View findSnapView(RecyclerView.LayoutManager r2) {
        if (r2.canScrollVertically() == false) goto L7;
        return findCenterView(r2, getVerticalHelper(r2));
    L7:
        if (r2.canScrollHorizontally() == true) goto L9;
        return null;
    L9:
        return findCenterView(r2, getHorizontalHelper(r2));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.recyclerview.widget.SnapHelper
    public int findTargetSnapPosition(RecyclerView.LayoutManager r11, int r12, int r13) {
        if ((r11 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) == true) goto L5;
        return -1;
    L5:
        int r0 = r11.getItemCount();
        if (r0 != 0) goto L8;
        return -1;
    L8:
        View r2 = findSnapView(r11);
        if (r2 != null) goto L11;
        return -1;
    L11:
        int r3 = r11.getPosition(r2);
        if (r3 != (-1)) goto L14;
        return -1;
    L14:
        PointF r5 = ((RecyclerView.SmoothScroller.ScrollVectorProvider) r11).computeScrollVectorForPosition(r0 - 1);
        if (r5 != null) goto L18;
        return -1;
    L18:
        if (r11.canScrollHorizontally() == false) goto L22;
        int r6 = estimateNextPositionDiffForFling(r11, getHorizontalHelper(r11), r12, 0);
        if (r5.x >= 0.0f) goto L24;
        r6 = -r6;
    L24:
        if (r11.canScrollVertically() == false) goto L28;
        int r8 = estimateNextPositionDiffForFling(r11, getVerticalHelper(r11), 0, r13);
        if (r5.y >= 0.0f) goto L30;
        r8 = -r8;
    L30:
        if (r11.canScrollVertically() == false) goto L32;
        int r7 = r8;
    L33:
        if (r7 != 0) goto L35;
        return -1;
    L35:
        int r1 = r3 + r7;
        if (r1 >= 0) goto L38;
        r1 = 0;
    L38:
        if (r1 >= r0) goto L40;
        return r1;
    L40:
        return r0 - 1;
    L32:
        r7 = r6;
        goto L33
    L28:
        r8 = 0;
        goto L30
    L22:
        r6 = 0;
        goto L24
    }
}
