package androidx.core.graphics;

import android.graphics.BlendMode;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import androidx.core.util.Pair;

/* JADX INFO: loaded from: classes.dex */
public final class PaintCompat {
    private static final String EM_STRING = "m";
    private static final String TOFU_STRING = "\udfffd";
    private static final ThreadLocal<Pair<Rect, Rect>> sRectThreadLocal = null;

    static class Api23Impl {
        private Api23Impl() {
        }

        static boolean hasGlyph(Paint r1, String r2) {
            return r1.hasGlyph(r2);
        }
    }

    static class Api29Impl {
        private Api29Impl() {
        }

        static void setBlendMode(Paint r1, Object r2) {
            r1.setBlendMode((BlendMode) r2);
        }
    }

    static {
        sRectThreadLocal = new ThreadLocal();
    }

    private PaintCompat() {
    }

    public static boolean hasGlyph(Paint r1, String r2) {
        return Api23Impl.hasGlyph(r1, r2);
    }

    private static Pair<Rect, Rect> obtainEmptyRects() {
        Pair<Rect, Rect> r0 = sRectThreadLocal.get();
        if (r0 != null) goto L5;
        Pair<Rect, Rect> r1 = new Pair(new Rect(), new Rect());
        sRectThreadLocal.set(r1);
        return r1;
    L5:
        r0.first.setEmpty();
        r0.second.setEmpty();
        return r0;
    }

    public static boolean setBlendMode(Paint r4, BlendModeCompat r5) {
        PorterDuffXfermode r3 = null;
        Object r6 = null;
        if (Build.VERSION.SDK_INT < 29) goto L9;
        if (r5 == null) goto L7;
        r6 = BlendModeUtils.Api29Impl.obtainBlendModeFromCompat(r5);
    L7:
        Api29Impl.setBlendMode(r4, r6);
        return true;
    L9:
        if (r5 == null) goto L18;
        PorterDuff.Mode r0 = BlendModeUtils.obtainPorterDuffFromCompat(r5);
        if (r0 == null) goto L13;
        r3 = new PorterDuffXfermode(r0);
    L13:
        r4.setXfermode(r3);
        if (r0 != null) goto L20;
        return false;
    L20:
        return true;
    L18:
        r4.setXfermode(null);
        return true;
    }
}
