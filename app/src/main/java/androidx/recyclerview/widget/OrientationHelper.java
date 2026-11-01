package androidx.recyclerview.widget;

import android.graphics.Rect;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public abstract class OrientationHelper {
    public static final int HORIZONTAL = 0;
    private static final int INVALID_SIZE = Integer.MIN_VALUE;
    public static final int VERTICAL = 1;
    private int mLastTotalSpace;
    protected final RecyclerView.LayoutManager mLayoutManager;
    final Rect mTmpRect;

    private OrientationHelper(RecyclerView.LayoutManager r2) {
        this.mLastTotalSpace = Integer.MIN_VALUE;
        this.mTmpRect = new Rect();
        this.mLayoutManager = r2;
    }

    /* synthetic */ OrientationHelper(RecyclerView.LayoutManager r1, C05011 r2) {
        this(r1);
    }

    public static OrientationHelper createHorizontalHelper(final RecyclerView.LayoutManager r1) {
        return new C05011(r1);
    }

    public static OrientationHelper createOrientationHelper(RecyclerView.LayoutManager r2, int r3) {
        switch(r3) {
            case 0: goto L8;
            case 1: goto L6;
            default: goto L4;
        };
    L4:
        throw new IllegalArgumentException("invalid orientation");
    L6:
        return createVerticalHelper(r2);
    L8:
        return createHorizontalHelper(r2);
    }

    public static OrientationHelper createVerticalHelper(final RecyclerView.LayoutManager r1) {
        return new C05022(r1);
    }

    public abstract int getDecoratedEnd(View r1);

    public abstract int getDecoratedMeasurement(View r1);

    public abstract int getDecoratedMeasurementInOther(View r1);

    public abstract int getDecoratedStart(View r1);

    public abstract int getEnd();

    public abstract int getEndAfterPadding();

    public abstract int getEndPadding();

    public RecyclerView.LayoutManager getLayoutManager() {
        return this.mLayoutManager;
    }

    public abstract int getMode();

    public abstract int getModeInOther();

    public abstract int getStartAfterPadding();

    public abstract int getTotalSpace();

    public int getTotalSpaceChange() {
        if (Integer.MIN_VALUE != this.mLastTotalSpace) goto L6;
        return 0;
    L6:
        return getTotalSpace() - this.mLastTotalSpace;
    }

    public abstract int getTransformedEndWithDecoration(View r1);

    public abstract int getTransformedStartWithDecoration(View r1);

    public abstract void offsetChild(View r1, int r2);

    public abstract void offsetChildren(int r1);

    public void onLayoutComplete() {
        this.mLastTotalSpace = getTotalSpace();
    }
}
