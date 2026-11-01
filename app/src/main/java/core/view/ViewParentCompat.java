package androidx.core.view;

import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;

/* JADX INFO: loaded from: classes.dex */
public final class ViewParentCompat {
    private static final String TAG = "ViewParentCompat";
    private static int[] sTempNestedScrollConsumed;

    static class Api19Impl {
        private Api19Impl() {
        }

        static void notifySubtreeAccessibilityStateChanged(ViewParent r0, View r1, View r2, int r3) {
            r0.notifySubtreeAccessibilityStateChanged(r1, r2, r3);
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static boolean onNestedFling(ViewParent r1, View r2, float r3, float r4, boolean r5) {
            return r1.onNestedFling(r2, r3, r4, r5);
        }

        static boolean onNestedPreFling(ViewParent r1, View r2, float r3, float r4) {
            return r1.onNestedPreFling(r2, r3, r4);
        }

        static void onNestedPreScroll(ViewParent r0, View r1, int r2, int r3, int[] r4) {
            r0.onNestedPreScroll(r1, r2, r3, r4);
        }

        static void onNestedScroll(ViewParent r0, View r1, int r2, int r3, int r4, int r5) {
            r0.onNestedScroll(r1, r2, r3, r4, r5);
        }

        static void onNestedScrollAccepted(ViewParent r0, View r1, View r2, int r3) {
            r0.onNestedScrollAccepted(r1, r2, r3);
        }

        static boolean onStartNestedScroll(ViewParent r1, View r2, View r3, int r4) {
            return r1.onStartNestedScroll(r2, r3, r4);
        }

        static void onStopNestedScroll(ViewParent r0, View r1) {
            r0.onStopNestedScroll(r1);
        }
    }

    private ViewParentCompat() {
    }

    private static int[] getTempNestedScrollConsumed() {
        if (sTempNestedScrollConsumed != null) goto L5;
        sTempNestedScrollConsumed = new int[2];
    L7:
        return sTempNestedScrollConsumed;
    L5:
        sTempNestedScrollConsumed[0] = 0;
        sTempNestedScrollConsumed[1] = 0;
        goto L7
    }

    public static void notifySubtreeAccessibilityStateChanged(ViewParent r0, View r1, View r2, int r3) {
        Api19Impl.notifySubtreeAccessibilityStateChanged(r0, r1, r2, r3);
    }

    public static boolean onNestedFling(ViewParent r3, View r4, float r5, float r6, boolean r7) {
        return Api21Impl.onNestedFling(r3, r4, r5, r6, r7);
    L4:
        e = move-exception;
        Log.e(TAG, "ViewParent " + r3 + " does not implement interface method onNestedFling", e);
        return false;
    }

    public static boolean onNestedPreFling(ViewParent r3, View r4, float r5, float r6) {
        return Api21Impl.onNestedPreFling(r3, r4, r5, r6);
    L4:
        e = move-exception;
        Log.e(TAG, "ViewParent " + r3 + " does not implement interface method onNestedPreFling", e);
        return false;
    }

    public static void onNestedPreScroll(ViewParent r6, View r7, int r8, int r9, int[] r10) {
        onNestedPreScroll(r6, r7, r8, r9, r10, 0);
    }

    public static void onNestedPreScroll(ViewParent r7, View r8, int r9, int r10, int[] r11, int r12) {
        if ((r7 instanceof NestedScrollingParent2) == false) goto L6;
        ((NestedScrollingParent2) r7).onNestedPreScroll(r8, r9, r10, r11, r12);
        return;
    L6:
        if (r12 != 0) goto L14;
        Api21Impl.onNestedPreScroll(r7, r8, r9, r10, r11);     // Catch: AbstractMethodError -> L9
        return;
    L9:
        e = move-exception;
        Log.e(TAG, "ViewParent " + r7 + " does not implement interface method onNestedPreScroll", e);
        return;
    }

    public static void onNestedScroll(ViewParent r8, View r9, int r10, int r11, int r12, int r13) {
        onNestedScroll(r8, r9, r10, r11, r12, r13, 0, getTempNestedScrollConsumed());
    }

    public static void onNestedScroll(ViewParent r8, View r9, int r10, int r11, int r12, int r13, int r14) {
        onNestedScroll(r8, r9, r10, r11, r12, r13, r14, getTempNestedScrollConsumed());
    }

    public static void onNestedScroll(ViewParent r9, View r10, int r11, int r12, int r13, int r14, int r15, int[] r16) {
        if ((r9 instanceof NestedScrollingParent3) == false) goto L5;
        ((NestedScrollingParent3) r9).onNestedScroll(r10, r11, r12, r13, r14, r15, r16);
        return;
    L5:
        r16[0] = r16[0] + r13;
        r16[1] = r16[1] + r14;
        if ((r9 instanceof NestedScrollingParent2) == false) goto L8;
        ((NestedScrollingParent2) r9).onNestedScroll(r10, r11, r12, r13, r14, r15);
        return;
    L8:
        if (r15 != 0) goto L17;
        Api21Impl.onNestedScroll(r9, r10, r11, r12, r13, r14);     // Catch: AbstractMethodError -> L11
        return;
    L11:
        e = move-exception;
        Log.e(TAG, "ViewParent " + r9 + " does not implement interface method onNestedScroll", e);
        return;
    }

    public static void onNestedScrollAccepted(ViewParent r1, View r2, View r3, int r4) {
        onNestedScrollAccepted(r1, r2, r3, r4, 0);
    }

    public static void onNestedScrollAccepted(ViewParent r3, View r4, View r5, int r6, int r7) {
        if ((r3 instanceof NestedScrollingParent2) == false) goto L5;
        ((NestedScrollingParent2) r3).onNestedScrollAccepted(r4, r5, r6, r7);
        return;
    L5:
        if (r7 != 0) goto L13;
        Api21Impl.onNestedScrollAccepted(r3, r4, r5, r6);     // Catch: AbstractMethodError -> L8
        return;
    L8:
        e = move-exception;
        Log.e(TAG, "ViewParent " + r3 + " does not implement interface method onNestedScrollAccepted", e);
        return;
    }

    public static boolean onStartNestedScroll(ViewParent r1, View r2, View r3, int r4) {
        return onStartNestedScroll(r1, r2, r3, r4, 0);
    }

    public static boolean onStartNestedScroll(ViewParent r3, View r4, View r5, int r6, int r7) {
        if ((r3 instanceof NestedScrollingParent2) == true) goto L5;
        if (r7 != 0) goto L15;
        return Api21Impl.onStartNestedScroll(r3, r4, r5, r6);
    L9:
        e = move-exception;
        Log.e(TAG, "ViewParent " + r3 + " does not implement interface method onStartNestedScroll", e);
        return false;
    L15:
        return false;
    L5:
        return ((NestedScrollingParent2) r3).onStartNestedScroll(r4, r5, r6, r7);
    }

    public static void onStopNestedScroll(ViewParent r1, View r2) {
        onStopNestedScroll(r1, r2, 0);
    }

    public static void onStopNestedScroll(ViewParent r3, View r4, int r5) {
        if ((r3 instanceof NestedScrollingParent2) == false) goto L5;
        ((NestedScrollingParent2) r3).onStopNestedScroll(r4, r5);
        return;
    L5:
        if (r5 != 0) goto L13;
        Api21Impl.onStopNestedScroll(r3, r4);     // Catch: AbstractMethodError -> L8
        return;
    L8:
        e = move-exception;
        Log.e(TAG, "ViewParent " + r3 + " does not implement interface method onStopNestedScroll", e);
        return;
    }

    @Deprecated
    public static boolean requestSendAccessibilityEvent(ViewParent r1, View r2, AccessibilityEvent r3) {
        return r1.requestSendAccessibilityEvent(r2, r3);
    }
}
