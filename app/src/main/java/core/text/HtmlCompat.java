package androidx.core.text;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/* JADX INFO: loaded from: classes.dex */
public final class HtmlCompat {
    public static final int FROM_HTML_MODE_COMPACT = 63;
    public static final int FROM_HTML_MODE_LEGACY = 0;
    public static final int FROM_HTML_OPTION_USE_CSS_COLORS = 256;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE = 32;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_DIV = 16;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_HEADING = 2;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_LIST = 8;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM = 4;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH = 1;
    public static final int TO_HTML_PARAGRAPH_LINES_CONSECUTIVE = 0;
    public static final int TO_HTML_PARAGRAPH_LINES_INDIVIDUAL = 1;

    static class Api24Impl {
        private Api24Impl() {
        }

        static Spanned fromHtml(String r1, int r2) {
            return Html.fromHtml(r1, r2);
        }

        static Spanned fromHtml(String r1, int r2, Html.ImageGetter r3, Html.TagHandler r4) {
            return Html.fromHtml(r1, r2, r3, r4);
        }

        static String toHtml(Spanned r1, int r2) {
            return Html.toHtml(r1, r2);
        }
    }

    private HtmlCompat() {
    }

    public static Spanned fromHtml(String r2, int r3) {
        if (Build.VERSION.SDK_INT < 24) goto L7;
        return Api24Impl.fromHtml(r2, r3);
    L7:
        return Html.fromHtml(r2);
    }

    public static Spanned fromHtml(String r2, int r3, Html.ImageGetter r4, Html.TagHandler r5) {
        if (Build.VERSION.SDK_INT < 24) goto L7;
        return Api24Impl.fromHtml(r2, r3, r4, r5);
    L7:
        return Html.fromHtml(r2, r4, r5);
    }

    public static String toHtml(Spanned r2, int r3) {
        if (Build.VERSION.SDK_INT < 24) goto L7;
        return Api24Impl.toHtml(r2, r3);
    L7:
        return Html.toHtml(r2);
    }
}
