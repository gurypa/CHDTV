package androidx.core.view;

import android.os.Build;
import android.view.View;
import android.view.Window;

/* JADX INFO: loaded from: classes.dex */
public final class WindowCompat {
    public static final int FEATURE_ACTION_BAR = 8;
    public static final int FEATURE_ACTION_BAR_OVERLAY = 9;
    public static final int FEATURE_ACTION_MODE_OVERLAY = 10;

    static class Api16Impl {
        private Api16Impl() {
        }

        static void setDecorFitsSystemWindows(Window r4, boolean r5) {
            View r1 = r4.getDecorView();
            int r2 = r1.getSystemUiVisibility();
            if (r5 == false) goto L5;
            int r3 = r2 & (-1793);
        L6:
            r1.setSystemUiVisibility(r3);
            return;
        L5:
            r3 = r2 | 1792;
            goto L6
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static <T> T requireViewById(Window r1, int r2) {
            return (T) r1.requireViewById(r2);
        }
    }

    static class Api30Impl {
        private Api30Impl() {
        }

        static void setDecorFitsSystemWindows(Window r0, boolean r1) {
            r0.setDecorFitsSystemWindows(r1);
        }
    }

    private WindowCompat() {
    }

    public static WindowInsetsControllerCompat getInsetsController(Window r1, View r2) {
        return new WindowInsetsControllerCompat(r1, r2);
    }

    public static <T extends View> T requireViewById(Window r3, int r4) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        T r0 = (T) r3.findViewById(r4);
        if (r0 == null) goto L10;
        return r0;
    L10:
        throw new IllegalArgumentException("ID does not reference a View inside this Window");
    L5:
        return (T) Api28Impl.requireViewById(r3, r4);
    }

    public static void setDecorFitsSystemWindows(Window r2, boolean r3) {
        if (Build.VERSION.SDK_INT < 30) goto L5;
        Api30Impl.setDecorFitsSystemWindows(r2, r3);
        return;
    L5:
        Api16Impl.setDecorFitsSystemWindows(r2, r3);
    }
}
