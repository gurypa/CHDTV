package androidx.core.view;

import android.view.ViewGroup;

/* JADX INFO: loaded from: classes.dex */
public final class MarginLayoutParamsCompat {

    static class Api17Impl {
        private Api17Impl() {
        }

        static int getLayoutDirection(ViewGroup.MarginLayoutParams r1) {
            return r1.getLayoutDirection();
        }

        static int getMarginEnd(ViewGroup.MarginLayoutParams r1) {
            return r1.getMarginEnd();
        }

        static int getMarginStart(ViewGroup.MarginLayoutParams r1) {
            return r1.getMarginStart();
        }

        static boolean isMarginRelative(ViewGroup.MarginLayoutParams r1) {
            return r1.isMarginRelative();
        }

        static void resolveLayoutDirection(ViewGroup.MarginLayoutParams r0, int r1) {
            r0.resolveLayoutDirection(r1);
        }

        static void setLayoutDirection(ViewGroup.MarginLayoutParams r0, int r1) {
            r0.setLayoutDirection(r1);
        }

        static void setMarginEnd(ViewGroup.MarginLayoutParams r0, int r1) {
            r0.setMarginEnd(r1);
        }

        static void setMarginStart(ViewGroup.MarginLayoutParams r0, int r1) {
            r0.setMarginStart(r1);
        }
    }

    private MarginLayoutParamsCompat() {
    }

    public static int getLayoutDirection(ViewGroup.MarginLayoutParams r2) {
        int r0 = Api17Impl.getLayoutDirection(r2);
        if (r0 != 0) goto L5;
        return r0;
    L5:
        if (r0 == 1) goto L9;
        return 0;
    L9:
        return r0;
    }

    public static int getMarginEnd(ViewGroup.MarginLayoutParams r1) {
        return Api17Impl.getMarginEnd(r1);
    }

    public static int getMarginStart(ViewGroup.MarginLayoutParams r1) {
        return Api17Impl.getMarginStart(r1);
    }

    public static boolean isMarginRelative(ViewGroup.MarginLayoutParams r1) {
        return Api17Impl.isMarginRelative(r1);
    }

    public static void resolveLayoutDirection(ViewGroup.MarginLayoutParams r0, int r1) {
        Api17Impl.resolveLayoutDirection(r0, r1);
    }

    public static void setLayoutDirection(ViewGroup.MarginLayoutParams r0, int r1) {
        Api17Impl.setLayoutDirection(r0, r1);
    }

    public static void setMarginEnd(ViewGroup.MarginLayoutParams r0, int r1) {
        Api17Impl.setMarginEnd(r0, r1);
    }

    public static void setMarginStart(ViewGroup.MarginLayoutParams r0, int r1) {
        Api17Impl.setMarginStart(r0, r1);
    }
}
