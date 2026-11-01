package androidx.core.graphics;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;

/* JADX INFO: loaded from: classes.dex */
public class BlendModeColorFilterCompat {

    static class Api29Impl {
        private Api29Impl() {
        }

        static ColorFilter createBlendModeColorFilter(int r2, Object r3) {
            return new BlendModeColorFilter(r2, (BlendMode) r3);
        }
    }

    private BlendModeColorFilterCompat() {
    }

    public static ColorFilter createBlendModeColorFilterCompat(int r3, BlendModeCompat r4) {
        if (Build.VERSION.SDK_INT < 29) goto L9;
        Object r0 = BlendModeUtils.Api29Impl.obtainBlendModeFromCompat(r4);
        if (r0 != null) goto L8;
        return null;
    L8:
        return Api29Impl.createBlendModeColorFilter(r3, r0);
    L9:
        PorterDuff.Mode r1 = BlendModeUtils.obtainPorterDuffFromCompat(r4);
        if (r1 != null) goto L13;
        return null;
    L13:
        return new PorterDuffColorFilter(r3, r1);
    }
}
