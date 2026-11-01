package androidx.media3.p002ui;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import androidx.media3.common.text.Cue;
import androidx.media3.common.text.LanguageFeatureSpan;
import androidx.media3.common.util.Assertions;
import com.google.common.base.Predicate;

/* JADX INFO: loaded from: classes.dex */
final class SubtitleViewUtils {
    private SubtitleViewUtils() {
    }

    static /* synthetic */ boolean lambda$removeAllEmbeddedStyling$0(Object r1) {
        return !(r1 instanceof LanguageFeatureSpan);
    }

    static /* synthetic */ boolean lambda$removeEmbeddedFontSizes$1(Object r1) {
        if ((r1 instanceof AbsoluteSizeSpan) == false) goto L5;
    L8:
        return true;
    L5:
        if ((r1 instanceof RelativeSizeSpan) == true) goto L8;
        return false;
    }

    public static void removeAllEmbeddedStyling(Cue.Builder r2) {
        r2.clearWindowColor();
        if ((r2.getText() instanceof Spanned) == true) goto L5;
    L8:
        removeEmbeddedFontSizes(r2);
        return;
    L5:
        if ((r2.getText() instanceof Spannable) == true) goto L7;
        r2.setText(SpannableString.valueOf(r2.getText()));
    L7:
        removeSpansIf((Spannable) Assertions.checkNotNull(r2.getText()), new SubtitleViewUtils$$ExternalSyntheticLambda0());
        goto L8
    }

    public static void removeEmbeddedFontSizes(Cue.Builder r2) {
        r2.setTextSize(-3.4028235E38f, Integer.MIN_VALUE);
        if ((r2.getText() instanceof Spanned) == true) goto L5;
        return;
    L5:
        if ((r2.getText() instanceof Spannable) == true) goto L7;
        r2.setText(SpannableString.valueOf(r2.getText()));
    L7:
        removeSpansIf((Spannable) Assertions.checkNotNull(r2.getText()), new SubtitleViewUtils$$ExternalSyntheticLambda1());
    }

    private static void removeSpansIf(Spannable r5, Predicate<Object> r6) {
        int r2 = 0;
        Object[] r0 = r5.getSpans(0, r5.length(), Object.class);
        int r1 = r0.length;
    L3:
        if (r2 >= r1) goto L8;
        Object r3 = r0[r2];
        if (r6.apply(r3) == false) goto L7;
        r5.removeSpan(r3);
    L7:
        r2 = r2 + 1;
        goto L3
    }

    public static float resolveTextSize(int r2, float r3, int r4, int r5) {
        if (r3 != (-3.4028235E38f)) goto L5;
        return -3.4028235E38f;
    L5:
        switch(r2) {
            case 0: goto L11;
            case 1: goto L9;
            case 2: goto L7;
            default: goto L6;
        };
    L6:
        return -3.4028235E38f;
    L7:
        return r3;
    L9:
        return r4 * r3;
    L11:
        return r5 * r3;
    }
}
