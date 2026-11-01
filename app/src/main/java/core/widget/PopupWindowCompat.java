package androidx.core.widget;

import android.view.View;
import android.widget.PopupWindow;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public final class PopupWindowCompat {
    private static final String TAG = "PopupWindowCompatApi21";
    private static Method sGetWindowLayoutTypeMethod;
    private static boolean sGetWindowLayoutTypeMethodAttempted;
    private static Field sOverlapAnchorField;
    private static boolean sOverlapAnchorFieldAttempted;
    private static Method sSetWindowLayoutTypeMethod;
    private static boolean sSetWindowLayoutTypeMethodAttempted;

    static class Api19Impl {
        private Api19Impl() {
        }

        static void showAsDropDown(PopupWindow r0, View r1, int r2, int r3, int r4) {
            r0.showAsDropDown(r1, r2, r3, r4);
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static boolean getOverlapAnchor(PopupWindow r1) {
            return r1.getOverlapAnchor();
        }

        static int getWindowLayoutType(PopupWindow r1) {
            return r1.getWindowLayoutType();
        }

        static void setOverlapAnchor(PopupWindow r0, boolean r1) {
            r0.setOverlapAnchor(r1);
        }

        static void setWindowLayoutType(PopupWindow r0, int r1) {
            r0.setWindowLayoutType(r1);
        }
    }

    private PopupWindowCompat() {
    }

    public static boolean getOverlapAnchor(PopupWindow r1) {
        return Api23Impl.getOverlapAnchor(r1);
    }

    public static int getWindowLayoutType(PopupWindow r1) {
        return Api23Impl.getWindowLayoutType(r1);
    }

    public static void setOverlapAnchor(PopupWindow r0, boolean r1) {
        Api23Impl.setOverlapAnchor(r0, r1);
    }

    public static void setWindowLayoutType(PopupWindow r0, int r1) {
        Api23Impl.setWindowLayoutType(r0, r1);
    }

    public static void showAsDropDown(PopupWindow r0, View r1, int r2, int r3, int r4) {
        Api19Impl.showAsDropDown(r0, r1, r2, r3, r4);
    }
}
