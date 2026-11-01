package androidx.media3.p002ui;

import android.text.Html;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.util.SparseArray;
import androidx.media3.common.text.HorizontalTextInVerticalContextSpan;
import androidx.media3.common.text.RubySpan;
import androidx.media3.common.text.TextEmphasisSpan;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
final class SpannedToHtmlConverter {
    private static final Pattern NEWLINE_PATTERN = null;

    /* JADX INFO: renamed from: androidx.media3.ui.SpannedToHtmlConverter$1 */
    static /* synthetic */ class C04681 {
    }

    public static class HtmlAndCss {
        public final Map<String, String> cssRuleSets;
        public final String html;

        private HtmlAndCss(String r1, Map<String, String> r2) {
            this.html = r1;
            this.cssRuleSets = r2;
        }

        /* synthetic */ HtmlAndCss(String r1, Map r2, C04681 r3) {
            this(r1, r2);
        }
    }

    private static final class SpanInfo {
        private static final Comparator<SpanInfo> FOR_CLOSING_TAGS = null;
        private static final Comparator<SpanInfo> FOR_OPENING_TAGS = null;
        public final String closingTag;
        public final int end;
        public final String openingTag;
        public final int start;

        static {
            FOR_OPENING_TAGS = new SpannedToHtmlConverter$SpanInfo$$ExternalSyntheticLambda0();
            FOR_CLOSING_TAGS = new SpannedToHtmlConverter$SpanInfo$$ExternalSyntheticLambda1();
        }

        private SpanInfo(int r1, int r2, String r3, String r4) {
            this.start = r1;
            this.end = r2;
            this.openingTag = r3;
            this.closingTag = r4;
        }

        /* synthetic */ SpanInfo(int r1, int r2, String r3, String r4, C04681 r5) {
            this(r1, r2, r3, r4);
        }

        static /* synthetic */ Comparator access$200() {
            return FOR_CLOSING_TAGS;
        }

        static /* synthetic */ Comparator access$400() {
            return FOR_OPENING_TAGS;
        }

        static /* synthetic */ int lambda$static$0(SpanInfo r3, SpanInfo r4) {
            int r0 = Integer.compare(r4.end, r3.end);
            if (r0 == 0) goto L5;
            return r0;
        L5:
            int r1 = r3.openingTag.compareTo(r4.openingTag);
            if (r1 == 0) goto L9;
            return r1;
        L9:
            return r3.closingTag.compareTo(r4.closingTag);
        }

        static /* synthetic */ int lambda$static$1(SpanInfo r3, SpanInfo r4) {
            int r0 = Integer.compare(r4.start, r3.start);
            if (r0 == 0) goto L5;
            return r0;
        L5:
            int r1 = r4.openingTag.compareTo(r3.openingTag);
            if (r1 == 0) goto L9;
            return r1;
        L9:
            return r4.closingTag.compareTo(r3.closingTag);
        }
    }

    private static final class Transition {
        private final List<SpanInfo> spansAdded;
        private final List<SpanInfo> spansRemoved;

        public Transition() {
            this.spansAdded = new ArrayList();
            this.spansRemoved = new ArrayList();
        }

        static /* synthetic */ List access$100(Transition r1) {
            return r1.spansRemoved;
        }

        static /* synthetic */ List access$300(Transition r1) {
            return r1.spansAdded;
        }
    }

    static {
        NEWLINE_PATTERN = Pattern.compile("(&#13;)?&#10;");
    }

    private SpannedToHtmlConverter() {
    }

    public static HtmlAndCss convert(CharSequence r13, float r14) {
        C04681 r0 = null;
        if (r13 != null) goto L7;
        return new HtmlAndCss("", ImmutableMap.m231of(), r0);
    L7:
        if ((r13 instanceof Spanned) == false) goto L9;
        Spanned r1 = (Spanned) r13;
        HashSet r2 = new HashSet();
        BackgroundColorSpan[] r3 = (BackgroundColorSpan[]) r1.getSpans(0, r1.length(), BackgroundColorSpan.class);
        int r4 = r3.length;
        int r6 = 0;
    L11:
        if (r6 >= r4) goto L13;
        r2.add(Integer.valueOf(r3[r6].getBackgroundColor()));
        r6 = r6 + 1;
        goto L11
    L13:
        HashMap r5 = new HashMap();
        Iterator r7 = r2.iterator();
    L15:
        if (r7.hasNext() == false) goto L17;
        int r8 = ((Integer) r7.next()).intValue();
        r5.put(HtmlUtils.cssAllClassDescendantsSelector("bg_" + r8), Util.formatInvariant("background-color:%s;", new Object[]{HtmlUtils.toCssRgba(r8)}));
        goto L15
    L17:
        SparseArray<Transition> r9 = findSpanTransitions(r1, r14);
        StringBuilder r10 = new StringBuilder(r1.length());
        int r11 = 0;
        int r12 = 0;
    L19:
        if (r12 >= r9.size()) goto L29;
        int r15 = r9.keyAt(r12);
        r10.append(escapeHtml(r1.subSequence(r11, r15)));
        Transition r16 = r9.get(r15);
        Collections.sort(Transition.access$100(r16), SpanInfo.access$200());
        Iterator r17 = Transition.access$100(r16).iterator();
    L22:
        if (r17.hasNext() == false) goto L24;
        r10.append(((SpanInfo) r17.next()).closingTag);
        goto L22
    L24:
        Collections.sort(Transition.access$300(r16), SpanInfo.access$400());
        Iterator r18 = Transition.access$300(r16).iterator();
    L26:
        if (r18.hasNext() == false) goto L28;
        r10.append(((SpanInfo) r18.next()).openingTag);
        goto L26
    L28:
        r11 = r15;
        r12 = r12 + 1;
        goto L19
    L29:
        r10.append(escapeHtml(r1.subSequence(r11, r1.length())));
        return new HtmlAndCss(r10.toString(), r5, r0);
    L9:
        return new HtmlAndCss(escapeHtml(r13), ImmutableMap.m231of(), r0);
    }

    private static String escapeHtml(CharSequence r3) {
        String r0 = Html.escapeHtml(r3);
        return NEWLINE_PATTERN.matcher(r0).replaceAll("<br>");
    }

    private static SparseArray<Transition> findSpanTransitions(Spanned r11, float r12) {
        SparseArray<Transition> r0 = new SparseArray();
        int r3 = 0;
        Object[] r1 = r11.getSpans(0, r11.length(), Object.class);
        int r2 = r1.length;
    L3:
        if (r3 >= r2) goto L8;
        Object r4 = r1[r3];
        String r8 = getOpeningTag(r4, r12);
        String r9 = getClosingTag(r4);
        int r6 = r11.getSpanStart(r4);
        int r7 = r11.getSpanEnd(r4);
        if (r8 == null) goto L7;
        Assertions.checkNotNull(r9);
        SpanInfo r5 = new SpanInfo(r6, r7, r8, r9, null);
        Transition.access$300(getOrCreate(r0, r6)).add(r5);
        Transition.access$100(getOrCreate(r0, r7)).add(r5);
    L7:
        r3 = r3 + 1;
        goto L3
    L8:
        return r0;
    }

    private static String getClosingTag(Object r3) {
        if ((r3 instanceof StrikethroughSpan) == false) goto L5;
    L44:
        return "</span>";
    L5:
        if ((r3 instanceof ForegroundColorSpan) == true) goto L44;
        if ((r3 instanceof BackgroundColorSpan) == true) goto L44;
        if ((r3 instanceof HorizontalTextInVerticalContextSpan) == true) goto L44;
        if ((r3 instanceof AbsoluteSizeSpan) == true) goto L44;
        if ((r3 instanceof RelativeSizeSpan) == true) goto L44;
        if ((r3 instanceof TextEmphasisSpan) == true) goto L44;
        if ((r3 instanceof TypefaceSpan) == false) goto L25;
        if (((TypefaceSpan) r3).getFamily() == null) goto L23;
        return "</span>";
    L23:
        return null;
    L25:
        if ((r3 instanceof StyleSpan) == false) goto L36;
        switch(((StyleSpan) r3).getStyle()) {
            case 1: goto L33;
            case 2: goto L31;
            case 3: goto L29;
            default: goto L43;
        };
    L29:
        return "</i></b>";
    L31:
        return "</i>";
    L33:
        return "</b>";
    L43:
        return null;
    L36:
        if ((r3 instanceof RubySpan) == false) goto L40;
        return "<rt>" + escapeHtml(((RubySpan) r3).rubyText) + "</rt></ruby>";
    L40:
        if ((r3 instanceof UnderlineSpan) == false) goto L43;
        return "</u>";
    }

    private static String getOpeningTag(Object r6, float r7) {
        if ((r6 instanceof StrikethroughSpan) == false) goto L7;
        return "<span style='text-decoration:line-through;'>";
    L7:
        if ((r6 instanceof ForegroundColorSpan) == false) goto L11;
        return Util.formatInvariant("<span style='color:%s;'>", new Object[]{HtmlUtils.toCssRgba(((ForegroundColorSpan) r6).getForegroundColor())});
    L11:
        if ((r6 instanceof BackgroundColorSpan) == false) goto L15;
        return Util.formatInvariant("<span class='bg_%s'>", new Object[]{Integer.valueOf(((BackgroundColorSpan) r6).getBackgroundColor())});
    L15:
        if ((r6 instanceof HorizontalTextInVerticalContextSpan) == false) goto L19;
        return "<span style='text-combine-upright:all;'>";
    L19:
        if ((r6 instanceof AbsoluteSizeSpan) == false) goto L27;
        AbsoluteSizeSpan r0 = (AbsoluteSizeSpan) r6;
        if (r0.getDip() == false) goto L23;
        float r3 = r0.getSize();
    L25:
        return Util.formatInvariant("<span style='font-size:%.2fpx;'>", new Object[]{Float.valueOf(r3)});
    L23:
        r3 = r0.getSize() / r7;
        goto L25
    L27:
        if ((r6 instanceof RelativeSizeSpan) == false) goto L31;
        return Util.formatInvariant("<span style='font-size:%.2f%%;'>", new Object[]{Float.valueOf(((RelativeSizeSpan) r6).getSizeChange() * 100.0f)});
    L31:
        if ((r6 instanceof TypefaceSpan) == false) goto L38;
        String r1 = ((TypefaceSpan) r6).getFamily();
        if (r1 != null) goto L36;
        return null;
    L36:
        return Util.formatInvariant("<span style='font-family:\"%s\";'>", new Object[]{r1});
    L38:
        if ((r6 instanceof StyleSpan) == false) goto L49;
        switch(((StyleSpan) r6).getStyle()) {
            case 1: goto L46;
            case 2: goto L44;
            case 3: goto L42;
            default: goto L41;
        };
    L41:
        return null;
    L42:
        return "<b><i>";
    L44:
        return "<i>";
    L46:
        return "<b>";
    L49:
        if ((r6 instanceof RubySpan) == false) goto L60;
        switch(((RubySpan) r6).position) {
            case -1: goto L57;
            case 0: goto L52;
            case 1: goto L55;
            case 2: goto L53;
            default: goto L52;
        };
    L52:
        return null;
    L53:
        return "<ruby style='ruby-position:under;'>";
    L55:
        return "<ruby style='ruby-position:over;'>";
    L57:
        return "<ruby style='ruby-position:unset;'>";
    L60:
        if ((r6 instanceof UnderlineSpan) == false) goto L64;
        return "<u>";
    L64:
        if ((r6 instanceof TextEmphasisSpan) == false) goto L67;
        TextEmphasisSpan r2 = (TextEmphasisSpan) r6;
        return Util.formatInvariant("<span style='-webkit-text-emphasis-style:%1$s;text-emphasis-style:%1$s;-webkit-text-emphasis-position:%2$s;text-emphasis-position:%2$s;display:inline-block;'>", new Object[]{getTextEmphasisStyle(r2.markShape, r2.markFill), getTextEmphasisPosition(r2.position)});
    L67:
        return null;
    }

    private static Transition getOrCreate(SparseArray<Transition> r2, int r3) {
        Transition r0 = r2.get(r3);
        if (r0 != null) goto L6;
        Transition r1 = new Transition();
        r2.put(r3, r1);
        return r1;
    L6:
        return r0;
    }

    private static String getTextEmphasisPosition(int r1) {
        switch(r1) {
            case 2: goto L5;
            default: goto L3;
        };
    L3:
        return "over right";
    L5:
        return "under left";
    }

    private static String getTextEmphasisStyle(int r2, int r3) {
        StringBuilder r0 = new StringBuilder();
        switch(r3) {
            case 1: goto L6;
            case 2: goto L5;
            default: goto L7;
        };
    L5:
        r0.append("open ");
        goto L7
    L6:
        r0.append("filled ");
    L7:
        switch(r2) {
            case 0: goto L12;
            case 1: goto L11;
            case 2: goto L10;
            case 3: goto L9;
            default: goto L8;
        };
    L8:
        r0.append("unset");
    L14:
        return r0.toString();
    L9:
        r0.append(TtmlNode.TEXT_EMPHASIS_MARK_SESAME);
        goto L14
    L10:
        r0.append(TtmlNode.TEXT_EMPHASIS_MARK_DOT);
        goto L14
    L11:
        r0.append(TtmlNode.TEXT_EMPHASIS_MARK_CIRCLE);
        goto L14
    L12:
        r0.append("none");
        goto L14
    }
}
