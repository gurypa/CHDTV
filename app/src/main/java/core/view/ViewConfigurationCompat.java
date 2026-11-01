package androidx.core.view;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewConfiguration;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public final class ViewConfigurationCompat {
    private static final String TAG = "ViewConfigCompat";
    private static Method sGetScaledScrollFactorMethod;

    static class Api26Impl {
        private Api26Impl() {
        }

        static float getScaledHorizontalScrollFactor(ViewConfiguration r1) {
            return r1.getScaledHorizontalScrollFactor();
        }

        static float getScaledVerticalScrollFactor(ViewConfiguration r1) {
            return r1.getScaledVerticalScrollFactor();
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static int getScaledHoverSlop(ViewConfiguration r1) {
            return r1.getScaledHoverSlop();
        }

        static boolean shouldShowMenuShortcutsWhenKeyboardPresent(ViewConfiguration r1) {
            return r1.shouldShowMenuShortcutsWhenKeyboardPresent();
        }
    }

    static {
        if (Build.VERSION.SDK_INT != 25) goto L11;
        sGetScaledScrollFactorMethod = ViewConfiguration.class.getDeclaredMethod("getScaledScrollFactor", new Class[0]);     // Catch: Exception -> L6
        return;
    L7:
        Log.i(TAG, "Could not find method getScaledScrollFactor() on ViewConfiguration");
        return;
    }

    private ViewConfigurationCompat() {
    }

    private static float getLegacyScrollFactor(ViewConfiguration r4, Context r5) {
        if (Build.VERSION.SDK_INT >= 25) goto L5;
    L11:
        TypedValue r0 = new TypedValue();
        if (r5.getTheme().resolveAttribute(R.attr.listPreferredItemHeight, r0, true) == true) goto L14;
        return 0.0f;
    L14:
        return r0.getDimension(r5.getResources().getDisplayMetrics());
    L5:
        if (sGetScaledScrollFactorMethod == null) goto L11;
        return ((Integer) sGetScaledScrollFactorMethod.invoke(r4, new Object[0])).intValue();
    L10:
        Log.i(TAG, "Could not find method getScaledScrollFactor() on ViewConfiguration");
        goto L11
    }

    public static float getScaledHorizontalScrollFactor(ViewConfiguration r2, Context r3) {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return Api26Impl.getScaledHorizontalScrollFactor(r2);
    L7:
        return getLegacyScrollFactor(r2, r3);
    }

    public static int getScaledHoverSlop(ViewConfiguration r2) {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return Api28Impl.getScaledHoverSlop(r2);
    L7:
        return r2.getScaledTouchSlop() / 2;
    }

    @Deprecated
    public static int getScaledPagingTouchSlop(ViewConfiguration r1) {
        return r1.getScaledPagingTouchSlop();
    }

    public static float getScaledVerticalScrollFactor(ViewConfiguration r2, Context r3) {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return Api26Impl.getScaledVerticalScrollFactor(r2);
    L7:
        return getLegacyScrollFactor(r2, r3);
    }

    @Deprecated
    public static boolean hasPermanentMenuKey(ViewConfiguration r1) {
        return r1.hasPermanentMenuKey();
    }

    public static boolean shouldShowMenuShortcutsWhenKeyboardPresent(ViewConfiguration r4, Context r5) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        Resources r0 = r5.getResources();
        int r1 = r0.getIdentifier("config_showMenuShortcutsWhenKeyboardPresent", "bool", "android");
        if (r1 != 0) goto L9;
    L11:
        return false;
    L9:
        if (r0.getBoolean(r1) == false) goto L11;
        return true;
    L5:
        return Api28Impl.shouldShowMenuShortcutsWhenKeyboardPresent(r4);
    }
}
