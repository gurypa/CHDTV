package androidx.core.text;

import android.text.TextUtils;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class TextUtilsCompat {
    private static final String ARAB_SCRIPT_SUBTAG = "Arab";
    private static final String HEBR_SCRIPT_SUBTAG = "Hebr";
    private static final Locale ROOT = null;

    static class Api17Impl {
        private Api17Impl() {
        }

        static int getLayoutDirectionFromLocale(Locale r1) {
            return TextUtils.getLayoutDirectionFromLocale(r1);
        }
    }

    static {
        ROOT = new Locale("", "");
    }

    private TextUtilsCompat() {
    }

    private static int getLayoutDirectionFromFirstChar(Locale r2) {
        switch(Character.getDirectionality(r2.getDisplayName(r2).charAt(0))) {
            case 1: goto L5;
            case 2: goto L5;
            default: goto L4;
        };
    L4:
        return 0;
    L5:
        return 1;
    }

    public static int getLayoutDirectionFromLocale(Locale r1) {
        return Api17Impl.getLayoutDirectionFromLocale(r1);
    }

    public static String htmlEncode(String r1) {
        return TextUtils.htmlEncode(r1);
    }
}
