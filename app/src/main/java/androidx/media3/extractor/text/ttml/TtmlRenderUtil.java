package androidx.media3.extractor.text.ttml;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import androidx.media3.common.text.HorizontalTextInVerticalContextSpan;
import androidx.media3.common.text.RubySpan;
import androidx.media3.common.text.SpanUtil;
import androidx.media3.common.text.TextEmphasisSpan;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import java.util.ArrayDeque;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
final class TtmlRenderUtil {
    private static final String TAG = "TtmlRenderUtil";

    private TtmlRenderUtil() {
    }

    public static void applyStylesToSpan(Spannable r9, int r10, int r11, TtmlStyle r12, TtmlNode r13, Map<String, TtmlStyle> r14, int r15) {
        if (r12.getStyle() == (-1)) goto L6;
        r9.setSpan(new StyleSpan(r12.getStyle()), r10, r11, 33);
    L6:
        if (r12.isLinethrough() == false) goto L9;
        r9.setSpan(new StrikethroughSpan(), r10, r11, 33);
    L9:
        if (r12.isUnderline() == false) goto L12;
        r9.setSpan(new UnderlineSpan(), r10, r11, 33);
    L12:
        if (r12.hasFontColor() == false) goto L15;
        SpanUtil.addOrReplaceSpan(r9, new ForegroundColorSpan(r12.getFontColor()), r10, r11, 33);
    L15:
        if (r12.hasBackgroundColor() == false) goto L18;
        SpanUtil.addOrReplaceSpan(r9, new BackgroundColorSpan(r12.getBackgroundColor()), r10, r11, 33);
    L18:
        if (r12.getFontFamily() == null) goto L21;
        SpanUtil.addOrReplaceSpan(r9, new TypefaceSpan(r12.getFontFamily()), r10, r11, 33);
    L21:
        if (r12.getTextEmphasis() == null) goto L38;
        TextEmphasis r0 = (TextEmphasis) Assertions.checkNotNull(r12.getTextEmphasis());
        if (r0.markShape == (-1)) goto L25;
        int r4 = r0.markShape;
        int r5 = r0.markFill;
    L33:
        if (r0.position != (-2)) goto L35;
        int r6 = 1;
    L36:
        SpanUtil.addOrReplaceSpan(r9, new TextEmphasisSpan(r4, r5, r6), r10, r11, 33);
        goto L38
    L35:
        r6 = r0.position;
        goto L36
    L25:
        if (r15 == 2) goto L29;
        if (r15 == 1) goto L29;
        r4 = 1;
    L30:
        r5 = 1;
    L29:
        r4 = 3;
    L38:
        switch(r12.getRubyType()) {
            case 2: goto L41;
            case 3: goto L40;
            case 4: goto L40;
            default: goto L64;
        };
    L40:
        r9.setSpan(new DeleteTextSpan(), r10, r11, 33);
        goto L64
    L41:
        TtmlNode r1 = findRubyContainerNode(r13, r14);
        if (r1 == null) goto L64;
        TtmlNode r7 = findRubyTextNode(r1, r14);
        if (r7 == null) goto L64;
        if (r7.getChildCount() == 1) goto L50;
    L62:
        Log.m46i(TAG, "Skipping rubyText node without exactly one text child.");
        goto L64
    L50:
        if (r7.getChild(0).text == null) goto L62;
        String r8 = (String) Util.castNonNull(r7.getChild(0).text);
        TtmlStyle r16 = resolveStyle(r7.style, r7.getStyleIds(), r14);
        if (r16 == null) goto L54;
        int r17 = r16.getRubyPosition();
    L55:
        if (r17 != (-1)) goto L61;
        TtmlStyle r2 = resolveStyle(r1.style, r1.getStyleIds(), r14);
        if (r2 == null) goto L59;
        int r18 = r2.getRubyPosition();
    L60:
        r17 = r18;
        goto L61
    L59:
        r18 = r17;
    L61:
        r9.setSpan(new RubySpan(r8, r17), r10, r11, 33);
        goto L64
    L54:
        r17 = -1;
    L64:
        if (r12.getTextCombine() == false) goto L67;
        SpanUtil.addOrReplaceSpan(r9, new HorizontalTextInVerticalContextSpan(), r10, r11, 33);
    L67:
        switch(r12.getFontSizeUnit()) {
            case 1: goto L71;
            case 2: goto L70;
            case 3: goto L69;
            default: goto L75;
        };
    L69:
        SpanUtil.addInheritedRelativeSizeSpan(r9, r12.getFontSize() / 100.0f, r10, r11, 33);
        return;
    L70:
        SpanUtil.addOrReplaceSpan(r9, new RelativeSizeSpan(r12.getFontSize()), r10, r11, 33);
        return;
    L71:
        SpanUtil.addOrReplaceSpan(r9, new AbsoluteSizeSpan((int) r12.getFontSize(), true), r10, r11, 33);
        return;
    }

    static String applyTextElementSpacePolicy(String r3) {
        return r3.replaceAll("\r\n", "\n").replaceAll(" *\n *", "\n").replaceAll("\n", " ").replaceAll("[ \t\\x0B\f\r]+", " ");
    }

    static void endParagraph(SpannableStringBuilder r3) {
        int r0 = r3.length() - 1;
    L3:
        if (r0 < 0) goto L7;
        if (r3.charAt(r0) != ' ') goto L7;
        r0 = r0 - 1;
    L7:
        if (r0 >= 0) goto L9;
        return;
    L9:
        if (r3.charAt(r0) == '\n') goto L15;
        r3.append('\n');
        return;
    }

    private static TtmlNode findRubyContainerNode(TtmlNode r3, Map<String, TtmlStyle> r4) {
    L2:
        if (r3 == null) goto L9;
        TtmlStyle r0 = resolveStyle(r3.style, r3.getStyleIds(), r4);
        if (r0 == null) goto L8;
        if (r0.getRubyType() != 1) goto L8;
        return r3;
    L8:
        r3 = r3.parent;
        goto L2
    L9:
        return null;
    }

    private static TtmlNode findRubyTextNode(TtmlNode r5, Map<String, TtmlStyle> r6) {
        ArrayDeque r0 = new ArrayDeque();
        r0.push(r5);
    L4:
        if (r0.isEmpty() == true) goto L14;
        TtmlNode r1 = (TtmlNode) r0.pop();
        TtmlStyle r2 = resolveStyle(r1.style, r1.getStyleIds(), r6);
        if (r2 != null) goto L8;
    L10:
        int r3 = r1.getChildCount() - 1;
    L11:
        if (r3 < 0) goto L4;
        r0.push(r1.getChild(r3));
        r3 = r3 - 1;
        goto L11
    L8:
        if (r2.getRubyType() != 3) goto L10;
        return r1;
    L14:
        return null;
    }

    public static TtmlStyle resolveStyle(TtmlStyle r5, String[] r6, Map<String, TtmlStyle> r7) {
        int r0 = 0;
        if (r5 != null) goto L17;
        if (r6 != null) goto L8;
        return null;
    L8:
        if (r6.length != 1) goto L12;
        return r7.get(r6[0]);
    L12:
        if (r6.length <= 1) goto L29;
        TtmlStyle r1 = new TtmlStyle();
        int r2 = r6.length;
    L14:
        if (r0 >= r2) goto L16;
        r1.chain(r7.get(r6[r0]));
        r0 = r0 + 1;
        goto L14
    L16:
        return r1;
    L29:
        return r5;
    L17:
        if (r6 != null) goto L19;
    L22:
        if (r6 == null) goto L29;
        if (r6.length <= 1) goto L29;
        int r3 = r6.length;
    L26:
        if (r0 >= r3) goto L28;
        r5.chain(r7.get(r6[r0]));
        r0 = r0 + 1;
        goto L26
    L28:
        return r5;
    L19:
        if (r6.length != 1) goto L22;
        return r5.chain(r7.get(r6[0]));
    }
}
