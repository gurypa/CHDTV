package androidx.core.view;

import android.view.ScaleGestureDetector;

/* JADX INFO: loaded from: classes.dex */
public final class ScaleGestureDetectorCompat {

    static class Api19Impl {
        private Api19Impl() {
        }

        static boolean isQuickScaleEnabled(ScaleGestureDetector r1) {
            return r1.isQuickScaleEnabled();
        }

        static void setQuickScaleEnabled(ScaleGestureDetector r0, boolean r1) {
            r0.setQuickScaleEnabled(r1);
        }
    }

    private ScaleGestureDetectorCompat() {
    }

    public static boolean isQuickScaleEnabled(ScaleGestureDetector r1) {
        return Api19Impl.isQuickScaleEnabled(r1);
    }

    @Deprecated
    public static boolean isQuickScaleEnabled(Object r1) {
        return isQuickScaleEnabled((ScaleGestureDetector) r1);
    }

    public static void setQuickScaleEnabled(ScaleGestureDetector r0, boolean r1) {
        Api19Impl.setQuickScaleEnabled(r0, r1);
    }

    @Deprecated
    public static void setQuickScaleEnabled(Object r1, boolean r2) {
        setQuickScaleEnabled((ScaleGestureDetector) r1, r2);
    }
}
