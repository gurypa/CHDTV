package androidx.core.view;

import android.graphics.Rect;
import android.view.Gravity;

/* JADX INFO: loaded from: classes.dex */
public final class GravityCompat {
    public static final int END = 8388613;
    public static final int RELATIVE_HORIZONTAL_GRAVITY_MASK = 8388615;
    public static final int RELATIVE_LAYOUT_DIRECTION = 8388608;
    public static final int START = 8388611;

    static class Api17Impl {
        private Api17Impl() {
        }

        static void apply(int r0, int r1, int r2, Rect r3, int r4, int r5, Rect r6, int r7) {
            Gravity.apply(r0, r1, r2, r3, r4, r5, r6, r7);
        }

        static void apply(int r0, int r1, int r2, Rect r3, Rect r4, int r5) {
            Gravity.apply(r0, r1, r2, r3, r4, r5);
        }

        static void applyDisplay(int r0, Rect r1, Rect r2, int r3) {
            Gravity.applyDisplay(r0, r1, r2, r3);
        }
    }

    private GravityCompat() {
    }

    public static void apply(int r0, int r1, int r2, Rect r3, int r4, int r5, Rect r6, int r7) {
        Api17Impl.apply(r0, r1, r2, r3, r4, r5, r6, r7);
    }

    public static void apply(int r0, int r1, int r2, Rect r3, Rect r4, int r5) {
        Api17Impl.apply(r0, r1, r2, r3, r4, r5);
    }

    public static void applyDisplay(int r0, Rect r1, Rect r2, int r3) {
        Api17Impl.applyDisplay(r0, r1, r2, r3);
    }

    public static int getAbsoluteGravity(int r1, int r2) {
        return Gravity.getAbsoluteGravity(r1, r2);
    }
}
