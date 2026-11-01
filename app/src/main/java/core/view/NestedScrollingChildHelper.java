package androidx.core.view;

import android.view.View;
import android.view.ViewParent;

/* JADX INFO: loaded from: classes.dex */
public class NestedScrollingChildHelper {
    private boolean mIsNestedScrollingEnabled;
    private ViewParent mNestedScrollingParentNonTouch;
    private ViewParent mNestedScrollingParentTouch;
    private int[] mTempNestedScrollConsumed;
    private final View mView;

    public NestedScrollingChildHelper(View r1) {
        this.mView = r1;
    }

    private boolean dispatchNestedScrollInternal(int r14, int r15, int r16, int r17, int[] r18, int r19, int[] r20) {
        if (isNestedScrollingEnabled() == false) goto L26;
        ViewParent r3 = getNestedScrollingParentForType(r19);
        if (r3 != null) goto L8;
        return false;
    L8:
        if (r14 != 0) goto L16;
        if (r15 != 0) goto L16;
        if (r16 != 0) goto L16;
        if (r17 != 0) goto L16;
        if (r18 == null) goto L26;
        r18[0] = 0;
        r18[1] = 0;
    L16:
        if (r18 == null) goto L18;
        this.mView.getLocationInWindow(r18);
        int r11 = r18[0];
        int r12 = r18[1];
    L19:
        if (r20 != null) goto L21;
        int[] r4 = getTempNestedScrollConsumed();
        r4[0] = 0;
        r4[1] = 0;
        int[] r10 = r4;
    L22:
        ViewParentCompat.onNestedScroll(r3, this.mView, r14, r15, r16, r17, r19, r10);
        if (r18 == null) goto L25;
        this.mView.getLocationInWindow(r18);
        r18[0] = r18[0] - r11;
        r18[1] = r18[1] - r12;
    L25:
        return true;
    L21:
        r10 = r20;
        goto L22
    L18:
        r11 = 0;
        r12 = 0;
    L26:
        return false;
    }

    private ViewParent getNestedScrollingParentForType(int r2) {
        switch(r2) {
            case 0: goto L8;
            case 1: goto L6;
            default: goto L3;
        };
    L3:
        return null;
    L6:
        return this.mNestedScrollingParentNonTouch;
    L8:
        return this.mNestedScrollingParentTouch;
    }

    private int[] getTempNestedScrollConsumed() {
        if (this.mTempNestedScrollConsumed != null) goto L6;
        this.mTempNestedScrollConsumed = new int[2];
    L6:
        return this.mTempNestedScrollConsumed;
    }

    private void setNestedScrollingParentForType(int r1, ViewParent r2) {
        switch(r1) {
            case 0: goto L5;
            case 1: goto L4;
            default: goto L8;
        };
    L4:
        this.mNestedScrollingParentNonTouch = r2;
        return;
    L5:
        this.mNestedScrollingParentTouch = r2;
        return;
    }

    public boolean dispatchNestedFling(float r3, float r4, boolean r5) {
        if (isNestedScrollingEnabled() == false) goto L8;
        ViewParent r0 = getNestedScrollingParentForType(0);
        if (r0 == null) goto L8;
        return ViewParentCompat.onNestedFling(r0, this.mView, r3, r4, r5);
    L8:
        return false;
    }

    public boolean dispatchNestedPreFling(float r3, float r4) {
        if (isNestedScrollingEnabled() == false) goto L8;
        ViewParent r0 = getNestedScrollingParentForType(0);
        if (r0 == null) goto L8;
        return ViewParentCompat.onNestedPreFling(r0, this.mView, r3, r4);
    L8:
        return false;
    }

    public boolean dispatchNestedPreScroll(int r7, int r8, int[] r9, int[] r10) {
        return dispatchNestedPreScroll(r7, r8, r9, r10, 0);
    }

    public boolean dispatchNestedPreScroll(int r11, int r12, int[] r13, int[] r14, int r15) {
        if (isNestedScrollingEnabled() == false) goto L31;
        ViewParent r2 = getNestedScrollingParentForType(r15);
        if (r2 != null) goto L8;
        return false;
    L8:
        if (r11 != 0) goto L15;
        if (r12 != 0) goto L15;
        if (r14 == null) goto L31;
        r14[0] = 0;
        r14[1] = 0;
    L15:
        if (r14 == null) goto L17;
        this.mView.getLocationInWindow(r14);
        int r8 = r14[0];
        int r9 = r14[1];
    L18:
        if (r13 != null) goto L20;
        int[] r6 = getTempNestedScrollConsumed();
    L21:
        r6[0] = 0;
        r6[1] = 0;
        ViewParentCompat.onNestedPreScroll(r2, this.mView, r11, r12, r6, r15);
        if (r14 == null) goto L25;
        this.mView.getLocationInWindow(r14);
        r14[0] = r14[0] - r8;
        r14[1] = r14[1] - r9;
    L25:
        if (r6[0] != 0) goto L29;
        if (r6[1] != 0) goto L29;
        return false;
    L29:
        return true;
    L20:
        r6 = r13;
        goto L21
    L17:
        r8 = 0;
        r9 = 0;
    L31:
        return false;
    }

    public void dispatchNestedScroll(int r1, int r2, int r3, int r4, int[] r5, int r6, int[] r7) {
        dispatchNestedScrollInternal(r1, r2, r3, r4, r5, r6, r7);
    }

    public boolean dispatchNestedScroll(int r9, int r10, int r11, int r12, int[] r13) {
        return dispatchNestedScrollInternal(r9, r10, r11, r12, r13, 0, null);
    }

    public boolean dispatchNestedScroll(int r9, int r10, int r11, int r12, int[] r13, int r14) {
        return dispatchNestedScrollInternal(r9, r10, r11, r12, r13, r14, null);
    }

    public boolean hasNestedScrollingParent() {
        return hasNestedScrollingParent(0);
    }

    public boolean hasNestedScrollingParent(int r2) {
        if (getNestedScrollingParentForType(r2) == null) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isNestedScrollingEnabled() {
        return this.mIsNestedScrollingEnabled;
    }

    public void onDetachedFromWindow() {
        ViewCompat.stopNestedScroll(this.mView);
    }

    public void onStopNestedScroll(View r2) {
        ViewCompat.stopNestedScroll(this.mView);
    }

    public void setNestedScrollingEnabled(boolean r2) {
        if (this.mIsNestedScrollingEnabled == false) goto L5;
        ViewCompat.stopNestedScroll(this.mView);
    L5:
        this.mIsNestedScrollingEnabled = r2;
    }

    public boolean startNestedScroll(int r2) {
        return startNestedScroll(r2, 0);
    }

    public boolean startNestedScroll(int r5, int r6) {
        if (hasNestedScrollingParent(r6) == false) goto L6;
        return true;
    L6:
        if (isNestedScrollingEnabled() == false) goto L17;
        ViewParent r0 = this.mView.getParent();
        View r2 = this.mView;
    L8:
        if (r0 == null) goto L23;
        if (ViewParentCompat.onStartNestedScroll(r0, r2, this.mView, r5, r6) == true) goto L11;
        if ((r0 instanceof View) == false) goto L16;
        r2 = (View) r0;
    L16:
        r0 = r0.getParent();
        goto L8
    L11:
        setNestedScrollingParentForType(r6, r0);
        ViewParentCompat.onNestedScrollAccepted(r0, r2, this.mView, r5, r6);
        return true;
    L23:
        return false;
    L17:
        return false;
    }

    public void stopNestedScroll() {
        stopNestedScroll(0);
    }

    public void stopNestedScroll(int r3) {
        ViewParent r0 = getNestedScrollingParentForType(r3);
        if (r0 == null) goto L6;
        ViewParentCompat.onStopNestedScroll(r0, this.mView, r3);
        setNestedScrollingParentForType(r3, null);
        return;
    }
}
