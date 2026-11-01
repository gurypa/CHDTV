package androidx.core.text;

import android.text.Spannable;
import android.text.SpannableString;
import androidx.media3.extractor.text.ttml.TtmlNode;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

/* JADX INFO: compiled from: SpannableString.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000(\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a%\u0010\u0003\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bH\u0086\n\u001a\u001d\u0010\u0003\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\bH\u0086\n\u001a\r\u0010\u000b\u001a\u00020\u0002*\u00020\fH\u0086\b¨\u0006\r"}, m421d2 = {"clearSpans", "", "Landroid/text/Spannable;", "set", "start", "", "end", TtmlNode.TAG_SPAN, "", "range", "Lkotlin/ranges/IntRange;", "toSpannable", "", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class SpannableStringKt {
    public static final void clearSpans(Spannable r8) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        Spannable r1 = r8;
        Object[] r5 = r1.getSpans(0, r1.length(), Object.class);
        Intrinsics.checkNotNullExpressionValue(r5, "getSpans(start, end, T::class.java)");
        int r2 = r5.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L6;
        r8.removeSpan(r5[r3]);
        r3 = r3 + 1;
        goto L3
    }

    public static final void set(Spannable r2, int r3, int r4, Object r5) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r5, TtmlNode.TAG_SPAN);
        r2.setSpan(r5, r3, r4, 17);
    }

    public static final void set(Spannable r4, IntRange r5, Object r6) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "range");
        Intrinsics.checkNotNullParameter(r6, TtmlNode.TAG_SPAN);
        r4.setSpan(r6, r5.getStart().intValue(), r5.getEndInclusive().intValue(), 17);
    }

    public static final Spannable toSpannable(CharSequence r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        SpannableString r1 = SpannableString.valueOf(r3);
        Intrinsics.checkNotNullExpressionValue(r1, "valueOf(this)");
        return r1;
    }
}
