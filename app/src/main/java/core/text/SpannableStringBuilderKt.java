package androidx.core.text;

import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import androidx.media3.extractor.text.ttml.TtmlNode;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: SpannableStringBuilder.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000:\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0005\u001a%\u0010\u0000\u001a\u00020\u00012\u0017\u0010\u0002\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006H\u0086\bø\u0001\u0000\u001a3\u0010\u0007\u001a\u00020\u0004*\u00020\u00042\b\b\u0001\u0010\b\u001a\u00020\t2\u0017\u0010\u0002\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006H\u0086\bø\u0001\u0000\u001a)\u0010\n\u001a\u00020\u0004*\u00020\u00042\u0017\u0010\u0002\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006H\u0086\bø\u0001\u0000\u001a3\u0010\b\u001a\u00020\u0004*\u00020\u00042\b\b\u0001\u0010\b\u001a\u00020\t2\u0017\u0010\u0002\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006H\u0086\bø\u0001\u0000\u001a1\u0010\u000b\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\f\u001a\u00020\r2\u0017\u0010\u0002\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006H\u0086\bø\u0001\u0000\u001aB\u0010\u000b\u001a\u00020\u0004*\u00020\u00042\u0012\u0010\u000e\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0\u000f\"\u00020\r2\u0017\u0010\u0002\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u0010\u001a)\u0010\u0011\u001a\u00020\u0004*\u00020\u00042\u0017\u0010\u0002\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006H\u0086\bø\u0001\u0000\u001a1\u0010\u0012\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00142\u0017\u0010\u0002\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006H\u0086\bø\u0001\u0000\u001a)\u0010\u0015\u001a\u00020\u0004*\u00020\u00042\u0017\u0010\u0002\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006H\u0086\bø\u0001\u0000\u001a)\u0010\u0016\u001a\u00020\u0004*\u00020\u00042\u0017\u0010\u0002\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006H\u0086\bø\u0001\u0000\u001a)\u0010\u0017\u001a\u00020\u0004*\u00020\u00042\u0017\u0010\u0002\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006H\u0086\bø\u0001\u0000\u001a)\u0010\u0018\u001a\u00020\u0004*\u00020\u00042\u0017\u0010\u0002\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006H\u0086\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0019"}, m421d2 = {"buildSpannedString", "Landroid/text/SpannedString;", "builderAction", "Lkotlin/Function1;", "Landroid/text/SpannableStringBuilder;", "", "Lkotlin/ExtensionFunctionType;", TtmlNode.ATTR_TTS_BACKGROUND_COLOR, TtmlNode.ATTR_TTS_COLOR, "", TtmlNode.BOLD, "inSpans", TtmlNode.TAG_SPAN, "", "spans", "", "(Landroid/text/SpannableStringBuilder;[Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Landroid/text/SpannableStringBuilder;", TtmlNode.ITALIC, "scale", "proportion", "", "strikeThrough", "subscript", "superscript", TtmlNode.UNDERLINE, "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class SpannableStringBuilderKt {
    public static final SpannableStringBuilder backgroundColor(SpannableStringBuilder r7, int r8, Function1<? super SpannableStringBuilder, Unit> r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r9, "builderAction");
        BackgroundColorSpan r1 = new BackgroundColorSpan(r8);
        int r4 = r7.length();
        r9.invoke(r7);
        r7.setSpan(r1, r4, r7.length(), 17);
        return r7;
    }

    public static final SpannableStringBuilder bold(SpannableStringBuilder r7, Function1<? super SpannableStringBuilder, Unit> r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "builderAction");
        StyleSpan r1 = new StyleSpan(1);
        int r4 = r7.length();
        r8.invoke(r7);
        r7.setSpan(r1, r4, r7.length(), 17);
        return r7;
    }

    public static final SpannedString buildSpannedString(Function1<? super SpannableStringBuilder, Unit> r4) {
        Intrinsics.checkNotNullParameter(r4, "builderAction");
        SpannableStringBuilder r1 = new SpannableStringBuilder();
        r4.invoke(r1);
        return new SpannedString(r1);
    }

    public static final SpannableStringBuilder color(SpannableStringBuilder r7, int r8, Function1<? super SpannableStringBuilder, Unit> r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r9, "builderAction");
        ForegroundColorSpan r1 = new ForegroundColorSpan(r8);
        int r4 = r7.length();
        r9.invoke(r7);
        r7.setSpan(r1, r4, r7.length(), 17);
        return r7;
    }

    public static final SpannableStringBuilder inSpans(SpannableStringBuilder r4, Object r5, Function1<? super SpannableStringBuilder, Unit> r6) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, TtmlNode.TAG_SPAN);
        Intrinsics.checkNotNullParameter(r6, "builderAction");
        int r1 = r4.length();
        r6.invoke(r4);
        r4.setSpan(r5, r1, r4.length(), 17);
        return r4;
    }

    public static final SpannableStringBuilder inSpans(SpannableStringBuilder r7, Object[] r8, Function1<? super SpannableStringBuilder, Unit> r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "spans");
        Intrinsics.checkNotNullParameter(r9, "builderAction");
        int r1 = r7.length();
        r9.invoke(r7);
        int r2 = r8.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L5;
        r7.setSpan(r8[r3], r1, r7.length(), 17);
        r3 = r3 + 1;
        goto L3
    L5:
        return r7;
    }

    public static final SpannableStringBuilder italic(SpannableStringBuilder r7, Function1<? super SpannableStringBuilder, Unit> r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "builderAction");
        StyleSpan r1 = new StyleSpan(2);
        int r4 = r7.length();
        r8.invoke(r7);
        r7.setSpan(r1, r4, r7.length(), 17);
        return r7;
    }

    public static final SpannableStringBuilder scale(SpannableStringBuilder r7, float r8, Function1<? super SpannableStringBuilder, Unit> r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r9, "builderAction");
        RelativeSizeSpan r1 = new RelativeSizeSpan(r8);
        int r4 = r7.length();
        r9.invoke(r7);
        r7.setSpan(r1, r4, r7.length(), 17);
        return r7;
    }

    public static final SpannableStringBuilder strikeThrough(SpannableStringBuilder r7, Function1<? super SpannableStringBuilder, Unit> r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "builderAction");
        StrikethroughSpan r1 = new StrikethroughSpan();
        int r4 = r7.length();
        r8.invoke(r7);
        r7.setSpan(r1, r4, r7.length(), 17);
        return r7;
    }

    public static final SpannableStringBuilder subscript(SpannableStringBuilder r7, Function1<? super SpannableStringBuilder, Unit> r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "builderAction");
        SubscriptSpan r1 = new SubscriptSpan();
        int r4 = r7.length();
        r8.invoke(r7);
        r7.setSpan(r1, r4, r7.length(), 17);
        return r7;
    }

    public static final SpannableStringBuilder superscript(SpannableStringBuilder r7, Function1<? super SpannableStringBuilder, Unit> r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "builderAction");
        SuperscriptSpan r1 = new SuperscriptSpan();
        int r4 = r7.length();
        r8.invoke(r7);
        r7.setSpan(r1, r4, r7.length(), 17);
        return r7;
    }

    public static final SpannableStringBuilder underline(SpannableStringBuilder r7, Function1<? super SpannableStringBuilder, Unit> r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "builderAction");
        UnderlineSpan r1 = new UnderlineSpan();
        int r4 = r7.length();
        r8.invoke(r7);
        r7.setSpan(r1, r4, r7.length(), 17);
        return r7;
    }
}
