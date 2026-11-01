package androidx.core.view;

import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes.dex */
public class NestedScrollingParentHelper {
    private int mNestedScrollAxesNonTouch;
    private int mNestedScrollAxesTouch;

    public NestedScrollingParentHelper(ViewGroup r1) {
    }

    public int getNestedScrollAxes() {
        return this.mNestedScrollAxesTouch | this.mNestedScrollAxesNonTouch;
    }

    public void onNestedScrollAccepted(View r2, View r3, int r4) {
        onNestedScrollAccepted(r2, r3, r4, 0);
    }

    public void onNestedScrollAccepted(View r2, View r3, int r4, int r5) {
        if (r5 != 1) goto L5;
        this.mNestedScrollAxesNonTouch = r4;
        return;
    L5:
        this.mNestedScrollAxesTouch = r4;
    }

    public void onStopNestedScroll(View r2) {
        onStopNestedScroll(r2, 0);
    }

    public void onStopNestedScroll(View r3, int r4) {
        if (r4 != 1) goto L5;
        this.mNestedScrollAxesNonTouch = 0;
        return;
    L5:
        this.mNestedScrollAxesTouch = 0;
    }
}
