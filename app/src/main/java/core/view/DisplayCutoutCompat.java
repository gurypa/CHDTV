package androidx.core.view;

import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Build;
import android.view.DisplayCutout;
import androidx.core.util.ObjectsCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class DisplayCutoutCompat {
    private final DisplayCutout mDisplayCutout;

    static class Api28Impl {
        private Api28Impl() {
        }

        static DisplayCutout createDisplayCutout(Rect r1, List<Rect> r2) {
            return new DisplayCutout(r1, r2);
        }

        static List<Rect> getBoundingRects(DisplayCutout r1) {
            return r1.getBoundingRects();
        }

        static int getSafeInsetBottom(DisplayCutout r1) {
            return r1.getSafeInsetBottom();
        }

        static int getSafeInsetLeft(DisplayCutout r1) {
            return r1.getSafeInsetLeft();
        }

        static int getSafeInsetRight(DisplayCutout r1) {
            return r1.getSafeInsetRight();
        }

        static int getSafeInsetTop(DisplayCutout r1) {
            return r1.getSafeInsetTop();
        }
    }

    static class Api29Impl {
        private Api29Impl() {
        }

        static DisplayCutout createDisplayCutout(Insets r6, Rect r7, Rect r8, Rect r9, Rect r10) {
            return new DisplayCutout(r6, r7, r8, r9, r10);
        }
    }

    static class Api30Impl {
        private Api30Impl() {
        }

        static DisplayCutout createDisplayCutout(Insets r7, Rect r8, Rect r9, Rect r10, Rect r11, Insets r12) {
            return new DisplayCutout(r7, r8, r9, r10, r11, r12);
        }

        static Insets getWaterfallInsets(DisplayCutout r1) {
            return r1.getWaterfallInsets();
        }
    }

    public DisplayCutoutCompat(Rect r3, List<Rect> r4) {
        if (Build.VERSION.SDK_INT < 28) goto L5;
        DisplayCutout r0 = Api28Impl.createDisplayCutout(r3, r4);
    L6:
        this(r0);
        return;
    L5:
        r0 = null;
        goto L6
    }

    private DisplayCutoutCompat(DisplayCutout r1) {
        this.mDisplayCutout = r1;
    }

    public DisplayCutoutCompat(androidx.core.graphics.Insets r2, Rect r3, Rect r4, Rect r5, Rect r6, androidx.core.graphics.Insets r7) {
        this(constructDisplayCutout(r2, r3, r4, r5, r6, r7));
    }

    private static DisplayCutout constructDisplayCutout(androidx.core.graphics.Insets r8, Rect r9, Rect r10, Rect r11, Rect r12, androidx.core.graphics.Insets r13) {
        if (Build.VERSION.SDK_INT < 30) goto L7;
        return Api30Impl.createDisplayCutout(r8.toPlatformInsets(), r9, r10, r11, r12, r13.toPlatformInsets());
    L7:
        if (Build.VERSION.SDK_INT < 29) goto L11;
        return Api29Impl.createDisplayCutout(r8.toPlatformInsets(), r9, r10, r11, r12);
    L11:
        if (Build.VERSION.SDK_INT < 28) goto L23;
        Rect r14 = new Rect(r8.left, r8.top, r8.right, r8.bottom);
        ArrayList r15 = new ArrayList();
        if (r9 == null) goto L15;
        r15.add(r9);
    L15:
        if (r10 == null) goto L17;
        r15.add(r10);
    L17:
        if (r11 == null) goto L19;
        r15.add(r11);
    L19:
        if (r12 == null) goto L22;
        r15.add(r12);
    L22:
        return Api28Impl.createDisplayCutout(r14, r15);
    L23:
        return null;
    }

    static DisplayCutoutCompat wrap(DisplayCutout r1) {
        if (r1 != null) goto L5;
        return null;
    L5:
        return new DisplayCutoutCompat(r1);
    }

    public boolean equals(Object r4) {
        if (this != r4) goto L5;
        return true;
    L5:
        if (r4 != null) goto L7;
        return false;
    L7:
        if (getClass() == r4.getClass()) goto L10;
        return false;
    L10:
        return ObjectsCompat.equals(this.mDisplayCutout, ((DisplayCutoutCompat) r4).mDisplayCutout);
    }

    public List<Rect> getBoundingRects() {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return Api28Impl.getBoundingRects(this.mDisplayCutout);
    L7:
        return Collections.emptyList();
    }

    public int getSafeInsetBottom() {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        return 0;
    L5:
        return Api28Impl.getSafeInsetBottom(this.mDisplayCutout);
    }

    public int getSafeInsetLeft() {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        return 0;
    L5:
        return Api28Impl.getSafeInsetLeft(this.mDisplayCutout);
    }

    public int getSafeInsetRight() {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        return 0;
    L5:
        return Api28Impl.getSafeInsetRight(this.mDisplayCutout);
    }

    public int getSafeInsetTop() {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        return 0;
    L5:
        return Api28Impl.getSafeInsetTop(this.mDisplayCutout);
    }

    public androidx.core.graphics.Insets getWaterfallInsets() {
        if (Build.VERSION.SDK_INT < 30) goto L7;
        return androidx.core.graphics.Insets.toCompatInsets(Api30Impl.getWaterfallInsets(this.mDisplayCutout));
    L7:
        return androidx.core.graphics.Insets.NONE;
    }

    public int hashCode() {
        if (this.mDisplayCutout != null) goto L6;
        return 0;
    L6:
        return this.mDisplayCutout.hashCode();
    }

    public String toString() {
        return "DisplayCutoutCompat{" + this.mDisplayCutout + "}";
    }

    DisplayCutout unwrap() {
        return this.mDisplayCutout;
    }
}
