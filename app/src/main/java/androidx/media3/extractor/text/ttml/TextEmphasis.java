package androidx.media3.extractor.text.ttml;

import android.text.TextUtils;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
final class TextEmphasis {
    private static final ImmutableSet<String> MARK_FILL_VALUES = null;
    public static final int MARK_SHAPE_AUTO = -1;
    private static final ImmutableSet<String> MARK_SHAPE_VALUES = null;
    public static final int POSITION_OUTSIDE = -2;
    private static final ImmutableSet<String> POSITION_VALUES = null;
    private static final ImmutableSet<String> SINGLE_STYLE_VALUES = null;
    private static final Pattern WHITESPACE_PATTERN = null;
    public final int markFill;
    public final int markShape;
    public final int position;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Position {
    }

    static {
        WHITESPACE_PATTERN = Pattern.compile("\\s+");
        SINGLE_STYLE_VALUES = ImmutableSet.m261of(TtmlNode.TEXT_EMPHASIS_AUTO, "none");
        MARK_SHAPE_VALUES = ImmutableSet.m262of(TtmlNode.TEXT_EMPHASIS_MARK_DOT, TtmlNode.TEXT_EMPHASIS_MARK_SESAME, TtmlNode.TEXT_EMPHASIS_MARK_CIRCLE);
        MARK_FILL_VALUES = ImmutableSet.m261of(TtmlNode.TEXT_EMPHASIS_MARK_FILLED, TtmlNode.TEXT_EMPHASIS_MARK_OPEN);
        POSITION_VALUES = ImmutableSet.m262of(TtmlNode.ANNOTATION_POSITION_AFTER, TtmlNode.ANNOTATION_POSITION_BEFORE, TtmlNode.ANNOTATION_POSITION_OUTSIDE);
    }

    private TextEmphasis(int r1, int r2, int r3) {
        this.markShape = r1;
        this.markFill = r2;
        this.position = r3;
    }

    public static TextEmphasis parse(String r3) {
        if (r3 != null) goto L5;
        return null;
    L5:
        String r1 = Ascii.toLowerCase(r3.trim());
        if (r1.isEmpty() == false) goto L9;
        return null;
    L9:
        return parseWords(ImmutableSet.copyOf(TextUtils.split(r1, WHITESPACE_PATTERN)));
    }

    private static TextEmphasis parseWords(ImmutableSet<String> r13) {
        String r2 = (String) Iterables.getFirst(Sets.intersection(POSITION_VALUES, r13), TtmlNode.ANNOTATION_POSITION_OUTSIDE);
        byte r4 = 2;
        byte r5 = 1;
        switch(r2.hashCode()) {
            case -1392885889: goto L12;
            case -1106037339: goto L9;
            case 92734940: goto L6;
            default: goto L14;
        };
    L14:
        byte r1 = -1;
    L15:
        switch(r1) {
            case 0: goto L18;
            case 1: goto L17;
            default: goto L16;
        };
    L16:
        int r3 = 1;
    L19:
        Sets.SetView r6 = Sets.intersection(SINGLE_STYLE_VALUES, r13);
        if (r6.isEmpty() == true) goto L36;
        String r7 = (String) r6.iterator().next();
        switch(r7.hashCode()) {
            case 3005871: goto L28;
            case 3387192: goto L25;
            default: goto L30;
        };
    L30:
        r5 = -1;
    L31:
        switch(r5) {
            case 0: goto L33;
            default: goto L32;
        };
    L32:
        int r8 = -1;
    L35:
        return new TextEmphasis(r8, 0, r3);
    L33:
        r8 = 0;
        goto L35
    L25:
        if (r7.equals("none") == false) goto L74;
        r5 = 0;
        goto L31
    L74:
        r5 = -1;
        goto L31
    L28:
        if (r7.equals(TtmlNode.TEXT_EMPHASIS_AUTO) == true) goto L31;
        r5 = -1;
        goto L31
    L36:
        Sets.SetView r9 = Sets.intersection(MARK_FILL_VALUES, r13);
        Sets.SetView r10 = Sets.intersection(MARK_SHAPE_VALUES, r13);
        if (r9.isEmpty() == true) goto L39;
    L42:
        String r11 = (String) Iterables.getFirst(r9, TtmlNode.TEXT_EMPHASIS_MARK_FILLED);
        switch(r11.hashCode()) {
            case -1274499742: goto L49;
            case 3417674: goto L46;
            default: goto L51;
        };
    L51:
        byte r12 = -1;
    L52:
        switch(r12) {
            case 0: goto L54;
            default: goto L53;
        };
    L53:
        int r14 = 1;
    L55:
        String r15 = (String) Iterables.getFirst(r10, TtmlNode.TEXT_EMPHASIS_MARK_CIRCLE);
        switch(r15.hashCode()) {
            case -1360216880: goto L65;
            case -905816648: goto L62;
            case 99657: goto L59;
            default: goto L67;
        };
    L67:
        r4 = -1;
    L68:
        switch(r4) {
            case 0: goto L71;
            case 1: goto L70;
            default: goto L69;
        };
    L69:
        int r16 = 1;
    L73:
        return new TextEmphasis(r16, r14, r3);
    L70:
        r16 = 3;
        goto L73
    L71:
        r16 = 2;
        goto L73
    L59:
        if (r15.equals(TtmlNode.TEXT_EMPHASIS_MARK_DOT) == false) goto L76;
        r4 = 0;
        goto L68
    L76:
        r4 = -1;
        goto L68
    L62:
        if (r15.equals(TtmlNode.TEXT_EMPHASIS_MARK_SESAME) == false) goto L77;
        r4 = 1;
        goto L68
    L77:
        r4 = -1;
        goto L68
    L65:
        if (r15.equals(TtmlNode.TEXT_EMPHASIS_MARK_CIRCLE) == true) goto L68;
        r4 = -1;
        goto L68
    L54:
        r14 = 2;
        goto L55
    L46:
        if (r11.equals(TtmlNode.TEXT_EMPHASIS_MARK_OPEN) == false) goto L79;
        r12 = 0;
        goto L52
    L79:
        r12 = -1;
        goto L52
    L49:
        if (r11.equals(TtmlNode.TEXT_EMPHASIS_MARK_FILLED) == false) goto L80;
        r12 = 1;
        goto L52
    L80:
        r12 = -1;
        goto L52
    L39:
        if (r10.isEmpty() == false) goto L42;
        return new TextEmphasis(-1, 0, r3);
    L17:
        r3 = -2;
        goto L19
    L18:
        r3 = 2;
        goto L19
    L6:
        if (r2.equals(TtmlNode.ANNOTATION_POSITION_AFTER) == false) goto L81;
        r1 = 0;
        goto L15
    L81:
        r1 = -1;
        goto L15
    L9:
        if (r2.equals(TtmlNode.ANNOTATION_POSITION_OUTSIDE) == false) goto L82;
        r1 = 1;
        goto L15
    L82:
        r1 = -1;
        goto L15
    L12:
        if (r2.equals(TtmlNode.ANNOTATION_POSITION_BEFORE) == false) goto L83;
        r1 = 2;
        goto L15
    L83:
        r1 = -1;
        goto L15
    }
}
