package androidx.recyclerview.widget;

import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/* JADX INFO: loaded from: classes.dex */
public abstract class SnapHelper extends RecyclerView.OnFlingListener {
    static final float MILLISECONDS_PER_INCH = 100.0f;
    private Scroller mGravityScroller;
    RecyclerView mRecyclerView;
    private final RecyclerView.OnScrollListener mScrollListener;

    public SnapHelper() {
        this.mScrollListener = new C05141(this);
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeOnScrollListener(this.mScrollListener);
        this.mRecyclerView.setOnFlingListener(null);
    }

    private void setupCallbacks() throws IllegalStateException {
        if (this.mRecyclerView.getOnFlingListener() != null) goto L7;
        this.mRecyclerView.addOnScrollListener(this.mScrollListener);
        this.mRecyclerView.setOnFlingListener(this);
        return;
    L7:
        throw new IllegalStateException("An instance of OnFlingListener already set.");
    }

    private boolean snapFromFling(RecyclerView.LayoutManager r5, int r6, int r7) {
        if ((r5 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) == true) goto L5;
        return false;
    L5:
        RecyclerView.SmoothScroller r0 = createScroller(r5);
        if (r0 != null) goto L8;
        return false;
    L8:
        int r2 = findTargetSnapPosition(r5, r6, r7);
        if (r2 != (-1)) goto L11;
        return false;
    L11:
        r0.setTargetPosition(r2);
        r5.startSmoothScroll(r0);
        return true;
    }

    public void attachToRecyclerView(RecyclerView r4) throws IllegalStateException {
        if (this.mRecyclerView != r4) goto L6;
        return;
    L6:
        if (this.mRecyclerView == null) goto L8;
        destroyCallbacks();
    L8:
        this.mRecyclerView = r4;
        if (this.mRecyclerView == null) goto L12;
        setupCallbacks();
        this.mGravityScroller = new Scroller(this.mRecyclerView.getContext(), new DecelerateInterpolator());
        snapToTargetExistingView();
        return;
    }

    public abstract int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager r1, View r2);

    public int[] calculateScrollDistance(int r11, int r12) {
        this.mGravityScroller.fling(0, 0, r11, r12, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[]{this.mGravityScroller.getFinalX(), this.mGravityScroller.getFinalY()};
    }

    protected RecyclerView.SmoothScroller createScroller(RecyclerView.LayoutManager r2) {
        return createSnapScroller(r2);
    }

    @Deprecated
    protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager r3) {
        if ((r3 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) == true) goto L6;
        return null;
    L6:
        final Context r1 = this.mRecyclerView.getContext();
        return new C05152(this, r1);
    }

    public abstract View findSnapView(RecyclerView.LayoutManager r1);

    public abstract int findTargetSnapPosition(RecyclerView.LayoutManager r1, int r2, int r3);

    @Override // androidx.recyclerview.widget.RecyclerView.OnFlingListener
    public boolean onFling(int r6, int r7) {
        RecyclerView.LayoutManager r0 = this.mRecyclerView.getLayoutManager();
        if (r0 != null) goto L6;
        return false;
    L6:
        if (this.mRecyclerView.getAdapter() != null) goto L8;
        return false;
    L8:
        int r3 = this.mRecyclerView.getMinFlingVelocity();
        if (Math.abs(r7) > r3) goto L13;
        if (Math.abs(r6) > r3) goto L13;
        return false;
    L13:
        if (snapFromFling(r0, r6, r7) == false) goto L18;
        return true;
    L18:
        return false;
    }

    void snapToTargetExistingView() {
        if (this.mRecyclerView != null) goto L5;
        return;
    L5:
        RecyclerView.LayoutManager r0 = this.mRecyclerView.getLayoutManager();
        if (r0 != null) goto L8;
        return;
    L8:
        View r1 = findSnapView(r0);
        if (r1 != null) goto L11;
        return;
    L11:
        int[] r2 = calculateDistanceToFinalSnap(r0, r1);
        if (r2[0] == 0) goto L14;
    L15:
        this.mRecyclerView.smoothScrollBy(r2[0], r2[1]);
        return;
    L14:
        if (r2[1] != 0) goto L15;
    }
}
