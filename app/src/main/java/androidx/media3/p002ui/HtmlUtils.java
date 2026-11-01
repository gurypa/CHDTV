package androidx.media3.p002ui;

import android.graphics.Color;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
final class HtmlUtils {
    private HtmlUtils() {
    }

    public static String cssAllClassDescendantsSelector(String r2) {
        return "." + r2 + ",." + r2 + " *";
    }

    public static String toCssRgba(int r7) {
        return Util.formatInvariant("rgba(%d,%d,%d,%.3f)", new Object[]{Integer.valueOf(Color.red(r7)), Integer.valueOf(Color.green(r7)), Integer.valueOf(Color.blue(r7)), Double.valueOf(((double) Color.alpha(r7)) / 255.0d)});
    }
}
