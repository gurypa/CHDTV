package androidx.media3.extractor.text.webvtt;

import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import androidx.media3.common.text.Cue;
import androidx.media3.common.text.HorizontalTextInVerticalContextSpan;
import androidx.media3.common.text.RubySpan;
import androidx.media3.common.text.SpanUtil;
import androidx.media3.common.text.VoiceSpan;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.arthenica.ffmpegkit.MediaInformation;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class WebvttCueParser {
    private static final char CHAR_AMPERSAND = '&';
    private static final char CHAR_GREATER_THAN = '>';
    private static final char CHAR_LESS_THAN = '<';
    private static final char CHAR_SEMI_COLON = ';';
    private static final char CHAR_SLASH = '/';
    private static final char CHAR_SPACE = ' ';
    public static final Pattern CUE_HEADER_PATTERN = null;
    private static final Pattern CUE_SETTING_PATTERN = null;
    private static final Map<String, Integer> DEFAULT_BACKGROUND_COLORS = null;
    static final float DEFAULT_POSITION = 0.5f;
    private static final Map<String, Integer> DEFAULT_TEXT_COLORS = null;
    private static final String ENTITY_AMPERSAND = "amp";
    private static final String ENTITY_GREATER_THAN = "gt";
    private static final String ENTITY_LESS_THAN = "lt";
    private static final String ENTITY_NON_BREAK_SPACE = "nbsp";
    private static final int STYLE_BOLD = 1;
    private static final int STYLE_ITALIC = 2;
    private static final String TAG = "WebvttCueParser";
    private static final String TAG_BOLD = "b";
    private static final String TAG_CLASS = "c";
    private static final String TAG_ITALIC = "i";
    private static final String TAG_LANG = "lang";
    private static final String TAG_RUBY = "ruby";
    private static final String TAG_RUBY_TEXT = "rt";
    private static final String TAG_UNDERLINE = "u";
    private static final String TAG_VOICE = "v";
    private static final int TEXT_ALIGNMENT_CENTER = 2;
    private static final int TEXT_ALIGNMENT_END = 3;
    private static final int TEXT_ALIGNMENT_LEFT = 4;
    private static final int TEXT_ALIGNMENT_RIGHT = 5;
    private static final int TEXT_ALIGNMENT_START = 1;

    /* JADX INFO: renamed from: androidx.media3.extractor.text.webvtt.WebvttCueParser$1 */
    static /* synthetic */ class C04461 {
    }

    private static class Element {
        private static final Comparator<Element> BY_START_POSITION_ASC = null;
        private final int endPosition;
        private final StartTag startTag;

        static {
            BY_START_POSITION_ASC = new WebvttCueParser$Element$$ExternalSyntheticLambda0();
        }

        private Element(StartTag r1, int r2) {
            this.startTag = r1;
            this.endPosition = r2;
        }

        /* synthetic */ Element(StartTag r1, int r2, C04461 r3) {
            this(r1, r2);
        }

        static /* synthetic */ Comparator access$100() {
            return BY_START_POSITION_ASC;
        }

        static /* synthetic */ StartTag access$200(Element r1) {
            return r1.startTag;
        }

        static /* synthetic */ int access$300(Element r1) {
            return r1.endPosition;
        }

        static /* synthetic */ int lambda$static$0(Element r2, Element r3) {
            return Integer.compare(r2.startTag.position, r3.startTag.position);
        }
    }

    private static final class StartTag {
        public final Set<String> classes;
        public final String name;
        public final int position;
        public final String voice;

        private StartTag(String r1, int r2, String r3, Set<String> r4) {
            this.position = r2;
            this.name = r1;
            this.voice = r3;
            this.classes = r4;
        }

        public static StartTag buildStartTag(String r7, int r8) {
            String r9 = r7.trim();
            Assertions.checkArgument(!r9.isEmpty());
            int r0 = r9.indexOf(" ");
            if (r0 != (-1)) goto L5;
            String r1 = "";
        L6:
            String[] r3 = Util.split(r9, "\\.");
            String r2 = r3[0];
            HashSet r4 = new HashSet();
            int r5 = 1;
        L8:
            if (r5 >= r3.length) goto L11;
            r4.add(r3[r5]);
            r5 = r5 + 1;
            goto L8
        L11:
            return new StartTag(r2, r8, r1, r4);
        L5:
            r1 = r9.substring(r0).trim();
            r9 = r9.substring(0, r0);
            goto L6
        }

        public static StartTag buildWholeCueVirtualTag() {
            return new StartTag("", 0, "", Collections.emptySet());
        }
    }

    private static final class StyleMatch implements Comparable<StyleMatch> {
        public final int score;
        public final WebvttCssStyle style;

        public StyleMatch(int r1, WebvttCssStyle r2) {
            this.score = r1;
            this.style = r2;
        }

        /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
        public int compareTo2(StyleMatch r3) {
            return Integer.compare(this.score, r3.score);
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(StyleMatch r1) {
            return compareTo2(r1);
        }
    }

    private static final class WebvttCueInfoBuilder {
        public long endTimeUs;
        public float line;
        public int lineAnchor;
        public int lineType;
        public float position;
        public int positionAnchor;
        public float size;
        public long startTimeUs;
        public CharSequence text;
        public int textAlignment;
        public int verticalType;

        public WebvttCueInfoBuilder() {
            this.startTimeUs = 0;
            this.endTimeUs = 0;
            this.textAlignment = 2;
            this.line = -3.4028235E38f;
            this.lineType = 1;
            this.lineAnchor = 0;
            this.position = -3.4028235E38f;
            this.positionAnchor = Integer.MIN_VALUE;
            this.size = 1.0f;
            this.verticalType = Integer.MIN_VALUE;
        }

        private static float computeLine(float r3, int r4) {
            if (r3 == (-3.4028235E38f)) goto L11;
            if (r4 != 0) goto L11;
            if (r3 >= 0.0f) goto L8;
        L9:
            return 1.0f;
        L8:
            if (r3 > 1.0f) goto L9;
        L11:
            if (r3 == (-3.4028235E38f)) goto L13;
            return r3;
        L13:
            if (r4 != 0) goto L15;
            return 1.0f;
        L15:
            return -3.4028235E38f;
        }

        private static Layout.Alignment convertTextAlignment(int r2) {
            switch(r2) {
                case 1: goto L10;
                case 2: goto L8;
                case 3: goto L6;
                case 4: goto L10;
                case 5: goto L6;
                default: goto L3;
            };
        L3:
            Log.m48w(WebvttCueParser.TAG, "Unknown textAlignment: " + r2);
            return null;
        L6:
            return Layout.Alignment.ALIGN_OPPOSITE;
        L8:
            return Layout.Alignment.ALIGN_CENTER;
        L10:
            return Layout.Alignment.ALIGN_NORMAL;
        }

        private static float deriveMaxSize(int r3, float r4) {
            switch(r3) {
                case 0: goto L14;
                case 1: goto L8;
                case 2: goto L6;
                default: goto L5;
            };
        L6:
            return r4;
        L5:
            throw new IllegalStateException(String.valueOf(r3));
        L8:
            if (r4 > 0.5f) goto L12;
            return 2.0f * r4;
        L12:
            return (1.0f - r4) * 2.0f;
        L14:
            return 1.0f - r4;
        }

        private static float derivePosition(int r1) {
            switch(r1) {
                case 4: goto L7;
                case 5: goto L5;
                default: goto L3;
            };
        L3:
            return 0.5f;
        L5:
            return 1.0f;
        L7:
            return 0.0f;
        }

        private static int derivePositionAnchor(int r1) {
            switch(r1) {
                case 1: goto L7;
                case 2: goto L3;
                case 3: goto L5;
                case 4: goto L7;
                case 5: goto L5;
                default: goto L3;
            };
        L3:
            return 1;
        L5:
            return 2;
        L7:
            return 0;
        }

        public WebvttCueInfo build() {
            return new WebvttCueInfo(toCueBuilder().build(), this.startTimeUs, this.endTimeUs);
        }

        public Cue.Builder toCueBuilder() {
            if (this.position == (-3.4028235E38f)) goto L5;
            float r0 = this.position;
        L7:
            if (this.positionAnchor == Integer.MIN_VALUE) goto L9;
            int r1 = this.positionAnchor;
        L10:
            Cue.Builder r2 = new Cue.Builder().setTextAlignment(convertTextAlignment(this.textAlignment)).setLine(computeLine(this.line, this.lineType), this.lineType).setLineAnchor(this.lineAnchor).setPosition(r0).setPositionAnchor(r1).setSize(Math.min(this.size, deriveMaxSize(r1, r0))).setVerticalType(this.verticalType);
            if (this.text == null) goto L13;
            r2.setText(this.text);
        L13:
            return r2;
        L9:
            r1 = derivePositionAnchor(this.textAlignment);
            goto L10
        L5:
            r0 = derivePosition(this.textAlignment);
            goto L7
        }
    }

    static {
        CUE_HEADER_PATTERN = Pattern.compile("^(\\S+)\\s+-->\\s+(\\S+)(.*)?$");
        CUE_SETTING_PATTERN = Pattern.compile("(\\S+?):(\\S+)");
        HashMap r0 = new HashMap();
        r0.put("white", Integer.valueOf(Color.rgb(255, 255, 255)));
        r0.put("lime", Integer.valueOf(Color.rgb(0, 255, 0)));
        r0.put("cyan", Integer.valueOf(Color.rgb(0, 255, 255)));
        r0.put("red", Integer.valueOf(Color.rgb(255, 0, 0)));
        r0.put("yellow", Integer.valueOf(Color.rgb(255, 255, 0)));
        r0.put("magenta", Integer.valueOf(Color.rgb(255, 0, 255)));
        r0.put("blue", Integer.valueOf(Color.rgb(0, 0, 255)));
        r0.put("black", Integer.valueOf(Color.rgb(0, 0, 0)));
        DEFAULT_TEXT_COLORS = Collections.unmodifiableMap(r0);
        HashMap r1 = new HashMap();
        r1.put("bg_white", Integer.valueOf(Color.rgb(255, 255, 255)));
        r1.put("bg_lime", Integer.valueOf(Color.rgb(0, 255, 0)));
        r1.put("bg_cyan", Integer.valueOf(Color.rgb(0, 255, 255)));
        r1.put("bg_red", Integer.valueOf(Color.rgb(255, 0, 0)));
        r1.put("bg_yellow", Integer.valueOf(Color.rgb(255, 255, 0)));
        r1.put("bg_magenta", Integer.valueOf(Color.rgb(255, 0, 255)));
        r1.put("bg_blue", Integer.valueOf(Color.rgb(0, 0, 255)));
        r1.put("bg_black", Integer.valueOf(Color.rgb(0, 0, 0)));
        DEFAULT_BACKGROUND_COLORS = Collections.unmodifiableMap(r1);
    }

    public WebvttCueParser() {
    }

    private static void applyDefaultColors(SpannableStringBuilder r5, Set<String> r6, int r7, int r8) {
        Iterator<String> r0 = r6.iterator();
    L4:
        if (r0.hasNext() == false) goto L13;
        String r1 = r0.next();
        if (DEFAULT_TEXT_COLORS.containsKey(r1) == true) goto L7;
        if (DEFAULT_BACKGROUND_COLORS.containsKey(r1) == false) goto L4;
        r5.setSpan(new BackgroundColorSpan(DEFAULT_BACKGROUND_COLORS.get(r1).intValue()), r7, r8, 33);
        goto L4
    L7:
        r5.setSpan(new ForegroundColorSpan(DEFAULT_TEXT_COLORS.get(r1).intValue()), r7, r8, 33);
        goto L4
    }

    private static void applyEntity(String r2, SpannableStringBuilder r3) {
        switch(r2.hashCode()) {
            case 3309: goto L15;
            case 3464: goto L12;
            case 96708: goto L9;
            case 3374865: goto L6;
            default: goto L17;
        };
    L17:
        byte r0 = -1;
    L18:
        switch(r0) {
            case 0: goto L23;
            case 1: goto L22;
            case 2: goto L21;
            case 3: goto L20;
            default: goto L19;
        };
    L19:
        Log.m48w(TAG, "ignoring unsupported entity: '&" + r2 + ";'");
        return;
    L20:
        r3.append('&');
        return;
    L21:
        r3.append(CHAR_SPACE);
        return;
    L22:
        r3.append('>');
        return;
    L23:
        r3.append('<');
        return;
    L6:
        if (r2.equals(ENTITY_NON_BREAK_SPACE) == false) goto L17;
        r0 = 2;
        goto L18
    L9:
        if (r2.equals(ENTITY_AMPERSAND) == false) goto L17;
        r0 = 3;
        goto L18
    L12:
        if (r2.equals(ENTITY_LESS_THAN) == false) goto L17;
        r0 = 0;
        goto L18
    L15:
        if (r2.equals(ENTITY_GREATER_THAN) == false) goto L17;
        r0 = 1;
        goto L18
    }

    private static void applyRubySpans(SpannableStringBuilder r16, String r17, StartTag r18, List<Element> r19, List<WebvttCssStyle> r20) {
        String r1 = r17;
        int r4 = getRubyPosition(r20, r1, r18);
        ArrayList r5 = new ArrayList(r19.size());
        r5.addAll(r19);
        Collections.sort(r5, Element.access$100());
        int r7 = 0;
        int r8 = r18.position;
        int r9 = 0;
    L4:
        if (r9 >= r5.size()) goto L10;
        if (TAG_RUBY_TEXT.equals(Element.access$200((Element) r5.get(r9)).name) == false) goto L9;
        Element r10 = (Element) r5.get(r9);
        int r11 = firstKnownRubyPosition(getRubyPosition(r20, r1, Element.access$200(r10)), r4, 1);
        int r12 = Element.access$200(r10).position - r7;
        int r13 = Element.access$300(r10) - r7;
        CharSequence r14 = r16.subSequence(r12, r13);
        r16.delete(r12, r13);
        r16.setSpan(new RubySpan(r14.toString(), r11), r8, r12, 33);
        r7 = r7 + r14.length();
        r8 = r12;
    L9:
        r9 = r9 + 1;
        r1 = r17;
        goto L4
    }

    private static void applySpansForTag(String r6, StartTag r7, List<Element> r8, SpannableStringBuilder r9, List<WebvttCssStyle> r10) {
        int r0 = r7.position;
        int r1 = r9.length();
        String r2 = r7.name;
        switch(r2.hashCode()) {
            case 0: goto L27;
            case 98: goto L24;
            case 99: goto L21;
            case 105: goto L18;
            case 117: goto L15;
            case 118: goto L12;
            case 3314158: goto L9;
            case 3511770: goto L6;
            default: goto L29;
        };
    L29:
        byte r3 = -1;
    L31:
        switch(r3) {
            case 0: goto L39;
            case 1: goto L38;
            case 2: goto L37;
            case 3: goto L36;
            case 4: goto L35;
            case 5: goto L34;
            case 6: goto L40;
            case 7: goto L40;
            default: goto L32;
        };
    L32:
        return;
    L34:
        applyVoiceSpan(r9, r7.voice, r0, r1);
        goto L40
    L35:
        applyDefaultColors(r9, r7.classes, r0, r1);
        goto L40
    L36:
        r9.setSpan(new UnderlineSpan(), r0, r1, 33);
        goto L40
    L37:
        applyRubySpans(r9, r6, r7, r8, r10);
        goto L40
    L38:
        r9.setSpan(new StyleSpan(2), r0, r1, 33);
        goto L40
    L39:
        r9.setSpan(new StyleSpan(1), r0, r1, 33);
    L40:
        List<StyleMatch> r4 = getApplicableStyles(r10, r6, r7);
        int r5 = 0;
    L42:
        if (r5 >= r4.size()) goto L44;
        applyStyleToText(r9, r4.get(r5).style, r0, r1);
        r5 = r5 + 1;
        goto L42
    L44:
        return;
    L6:
        if (r2.equals("ruby") == false) goto L29;
        r3 = 2;
        goto L31
    L9:
        if (r2.equals(TAG_LANG) == false) goto L29;
        r3 = 6;
        goto L31
    L12:
        if (r2.equals("v") == false) goto L29;
        r3 = 5;
        goto L31
    L15:
        if (r2.equals(TAG_UNDERLINE) == false) goto L29;
        r3 = 3;
        goto L31
    L18:
        if (r2.equals("i") == false) goto L29;
        r3 = 1;
        goto L31
    L21:
        if (r2.equals(TAG_CLASS) == false) goto L29;
        r3 = 4;
        goto L31
    L24:
        if (r2.equals(TAG_BOLD) == false) goto L29;
        r3 = 0;
        goto L31
    L27:
        if (r2.equals("") == false) goto L29;
        r3 = 7;
        goto L31
    }

    private static void applyStyleToText(SpannableStringBuilder r4, WebvttCssStyle r5, int r6, int r7) {
        if (r5 != null) goto L5;
        return;
    L5:
        if (r5.getStyle() == (-1)) goto L8;
        SpanUtil.addOrReplaceSpan(r4, new StyleSpan(r5.getStyle()), r6, r7, 33);
    L8:
        if (r5.isLinethrough() == false) goto L11;
        r4.setSpan(new StrikethroughSpan(), r6, r7, 33);
    L11:
        if (r5.isUnderline() == false) goto L14;
        r4.setSpan(new UnderlineSpan(), r6, r7, 33);
    L14:
        if (r5.hasFontColor() == false) goto L17;
        SpanUtil.addOrReplaceSpan(r4, new ForegroundColorSpan(r5.getFontColor()), r6, r7, 33);
    L17:
        if (r5.hasBackgroundColor() == false) goto L20;
        SpanUtil.addOrReplaceSpan(r4, new BackgroundColorSpan(r5.getBackgroundColor()), r6, r7, 33);
    L20:
        if (r5.getFontFamily() == null) goto L23;
        SpanUtil.addOrReplaceSpan(r4, new TypefaceSpan(r5.getFontFamily()), r6, r7, 33);
    L23:
        switch(r5.getFontSizeUnit()) {
            case 1: goto L27;
            case 2: goto L26;
            case 3: goto L25;
            default: goto L29;
        };
    L25:
        SpanUtil.addOrReplaceSpan(r4, new RelativeSizeSpan(r5.getFontSize() / 100.0f), r6, r7, 33);
        goto L29
    L26:
        SpanUtil.addOrReplaceSpan(r4, new RelativeSizeSpan(r5.getFontSize()), r6, r7, 33);
        goto L29
    L27:
        SpanUtil.addOrReplaceSpan(r4, new AbsoluteSizeSpan((int) r5.getFontSize(), true), r6, r7, 33);
    L29:
        if (r5.getCombineUpright() == false) goto L32;
        r4.setSpan(new HorizontalTextInVerticalContextSpan(), r6, r7, 33);
        return;
    }

    private static void applyVoiceSpan(SpannableStringBuilder r2, String r3, int r4, int r5) {
        r2.setSpan(new VoiceSpan(r3), r4, r5, 33);
    }

    private static int findEndOfTag(String r2, int r3) {
        int r0 = r2.indexOf(62, r3);
        if (r0 != (-1)) goto L6;
        return r2.length();
    L6:
        return r0 + 1;
    }

    private static int firstKnownRubyPosition(int r1, int r2, int r3) {
        if (r1 == (-1)) goto L5;
        return r1;
    L5:
        if (r2 == (-1)) goto L7;
        return r2;
    L7:
        if (r3 == (-1)) goto L10;
        return r3;
    L10:
        throw new IllegalArgumentException();
    }

    private static List<StyleMatch> getApplicableStyles(List<WebvttCssStyle> r6, String r7, StartTag r8) {
        ArrayList r0 = new ArrayList();
        int r1 = 0;
    L4:
        if (r1 >= r6.size()) goto L9;
        WebvttCssStyle r2 = r6.get(r1);
        int r3 = r2.getSpecificityScore(r7, r8.name, r8.classes, r8.voice);
        if (r3 <= 0) goto L8;
        r0.add(new StyleMatch(r3, r2));
    L8:
        r1 = r1 + 1;
        goto L4
    L9:
        Collections.sort(r0);
        return r0;
    }

    private static int getRubyPosition(List<WebvttCssStyle> r5, String r6, StartTag r7) {
        List<StyleMatch> r0 = getApplicableStyles(r5, r6, r7);
        int r1 = 0;
    L4:
        if (r1 >= r0.size()) goto L10;
        WebvttCssStyle r2 = r0.get(r1).style;
        if (r2.getRubyPosition() != (-1)) goto L8;
        r1 = r1 + 1;
        goto L4
    L8:
        return r2.getRubyPosition();
    L10:
        return -1;
    }

    private static String getTagName(String r2) {
        String r3 = r2.trim();
        Assertions.checkArgument(!r3.isEmpty());
        return Util.splitAtFirst(r3, "[ \\.]")[0];
    }

    private static boolean isSupportedTag(String r3) {
        switch(r3.hashCode()) {
            case 98: goto L27;
            case 99: goto L24;
            case 105: goto L21;
            case 117: goto L18;
            case 118: goto L15;
            case 3650: goto L12;
            case 3314158: goto L9;
            case 3511770: goto L6;
            default: goto L29;
        };
    L29:
        byte r0 = -1;
    L30:
        switch(r0) {
            case 0: goto L32;
            case 1: goto L32;
            case 2: goto L32;
            case 3: goto L32;
            case 4: goto L32;
            case 5: goto L32;
            case 6: goto L32;
            case 7: goto L32;
            default: goto L31;
        };
    L31:
        return false;
    L32:
        return true;
    L6:
        if (r3.equals("ruby") == false) goto L29;
        r0 = 4;
        goto L30
    L9:
        if (r3.equals(TAG_LANG) == false) goto L29;
        r0 = 3;
        goto L30
    L12:
        if (r3.equals(TAG_RUBY_TEXT) == false) goto L29;
        r0 = 5;
        goto L30
    L15:
        if (r3.equals("v") == false) goto L29;
        r0 = 7;
        goto L30
    L18:
        if (r3.equals(TAG_UNDERLINE) == false) goto L29;
        r0 = 6;
        goto L30
    L21:
        if (r3.equals("i") == false) goto L29;
        r0 = 2;
        goto L30
    L24:
        if (r3.equals(TAG_CLASS) == false) goto L29;
        r0 = 1;
        goto L30
    L27:
        if (r3.equals(TAG_BOLD) == false) goto L29;
        r0 = 0;
        goto L30
    }

    public static Cue newCueForText(CharSequence r2) {
        WebvttCueInfoBuilder r0 = new WebvttCueInfoBuilder();
        r0.text = r2;
        return r0.toCueBuilder().build();
    }

    public static WebvttCueInfo parseCue(ParsableByteArray r5, List<WebvttCssStyle> r6) {
        String r0 = r5.readLine();
        if (r0 != null) goto L5;
        return null;
    L5:
        Matcher r2 = CUE_HEADER_PATTERN.matcher(r0);
        if (r2.matches() == true) goto L8;
        String r3 = r5.readLine();
        if (r3 != null) goto L12;
        return null;
    L12:
        Matcher r4 = CUE_HEADER_PATTERN.matcher(r3);
        if (r4.matches() == true) goto L15;
        return null;
    L15:
        return parseCue(r0.trim(), r4, r5, r6);
    L8:
        return parseCue(null, r2, r5, r6);
    }

    private static WebvttCueInfo parseCue(String r4, Matcher r5, ParsableByteArray r6, List<WebvttCssStyle> r7) {
        WebvttCueInfoBuilder r0 = new WebvttCueInfoBuilder();
        r0.startTimeUs = WebvttParserUtil.parseTimestampUs((String) Assertions.checkNotNull(r5.group(1)));     // Catch: NumberFormatException -> L13
        r0.endTimeUs = WebvttParserUtil.parseTimestampUs((String) Assertions.checkNotNull(r5.group(2)));     // Catch: NumberFormatException -> L13
        parseCueSettingsList((String) Assertions.checkNotNull(r5.group(3)), r0);
        StringBuilder r1 = new StringBuilder();
        String r2 = r6.readLine();
    L6:
        if (TextUtils.isEmpty(r2) == true) goto L11;
        if (r1.length() <= 0) goto L10;
        r1.append("\n");
    L10:
        r1.append(r2.trim());
        r2 = r6.readLine();
        goto L6
    L11:
        r0.text = parseCueText(r4, r1.toString(), r7);
        return r0.build();
    L14:
        Log.m48w(TAG, "Skipping cue with bad header: " + r5.group());
        return null;
    }

    static Cue.Builder parseCueSettingsList(String r2) {
        WebvttCueInfoBuilder r0 = new WebvttCueInfoBuilder();
        parseCueSettingsList(r2, r0);
        return r0.toCueBuilder();
    }

    private static void parseCueSettingsList(String r7, WebvttCueInfoBuilder r8) {
        Matcher r1 = CUE_SETTING_PATTERN.matcher(r7);
    L4:
        if (r1.find() == false) goto L26;
        String r2 = (String) Assertions.checkNotNull(r1.group(1));
        String r3 = (String) Assertions.checkNotNull(r1.group(2));
        if ("line".equals(r2) == true) goto L8;
        if ("align".equals(r2) == true) goto L11;
        if ("position".equals(r2) == true) goto L14;
        if (MediaInformation.KEY_SIZE.equals(r2) == true) goto L17;
        if ("vertical".equals(r2) == true) goto L20;
        Log.m48w(TAG, "Unknown cue setting " + r2 + ":" + r3);     // Catch: NumberFormatException -> L23
        goto L4
    L20:
        r8.verticalType = parseVerticalAttribute(r3);     // Catch: NumberFormatException -> L23
        goto L4
    L17:
        r8.size = WebvttParserUtil.parsePercentage(r3);     // Catch: NumberFormatException -> L23
        goto L4
    L14:
        parsePositionAttribute(r3, r8);     // Catch: NumberFormatException -> L23
        goto L4
    L11:
        r8.textAlignment = parseTextAlignment(r3);     // Catch: NumberFormatException -> L23
        goto L4
    L8:
        parseLineAttribute(r3, r8);     // Catch: NumberFormatException -> L23
    L24:
        Log.m48w(TAG, "Skipping bad cue setting: " + r1.group());
        goto L4
    }

    static SpannedString parseCueText(String r17, String r18, List<WebvttCssStyle> r19) {
        SpannableStringBuilder r3 = new SpannableStringBuilder();
        ArrayDeque r4 = new ArrayDeque();
        int r5 = 0;
        ArrayList r6 = new ArrayList();
    L4:
        if (r5 >= r18.length()) goto L63;
        char r7 = r18.charAt(r5);
        switch(r7) {
            case 38: goto L48;
            case 60: goto L9;
            default: goto L7;
        };
    L9:
        if ((r5 + 1) >= r18.length()) goto L10;
        int r8 = r5;
        boolean r10 = false;
        int r12 = 1;
        if (r18.charAt(r8 + 1) != '/') goto L14;
        boolean r9 = true;
    L15:
        r5 = findEndOfTag(r18, r8 + 1);
        if (r18.charAt(r5 - 2) != '/') goto L19;
        r10 = true;
    L19:
        if (r9 == false) goto L21;
        r12 = 2;
    L21:
        int r13 = r12 + r8;
        if (r10 == false) goto L24;
        int r11 = r5 - 2;
    L25:
        String r14 = r18.substring(r13, r11);
        if (r14.trim().isEmpty() == true) goto L4;
        String r15 = getTagName(r14);
        if (isSupportedTag(r15) == false) goto L4;
        if (r9 == true) goto L33;
        r5 = r5;
        if (r10 == true) goto L47;
        r4.push(StartTag.buildStartTag(r14, r3.length()));
    L47:
        r5 = r5;
    L33:
        if (r4.isEmpty() == true) goto L34;
        StartTag r16 = (StartTag) r4.pop();
        applySpansForTag(r17, r16, r6, r3, r19);
        if (r4.isEmpty() == true) goto L38;
        r6.add(new Element(r16, r3.length(), null));
    L40:
        if (r16.name.equals(r15) == true) goto L47;
        r5 = r5;
        goto L33
    L38:
        r6.clear();
        goto L40
    L34:
        r5 = r5;
        goto L47
    L24:
        r11 = r5 - 1;
        goto L25
    L14:
        r9 = false;
        goto L15
    L10:
        r5 = r5 + 1;
        goto L4
    L48:
        int r20 = r18.indexOf(59, r5 + 1);
        int r21 = r18.indexOf(32, r5 + 1);
        if (r20 != (-1)) goto L51;
        int r110 = r21;
    L55:
        if (r110 != (-1)) goto L56;
        r3.append(r7);
        r5 = r5 + 1;
        goto L4
    L56:
        applyEntity(r18.substring(r5 + 1, r110), r3);
        if (r110 != r21) goto L59;
        r3.append(" ");
    L59:
        r5 = r110 + 1;
        goto L4
    L51:
        if (r21 != (-1)) goto L53;
        r110 = r20;
        goto L55
    L53:
        r110 = Math.min(r20, r21);
        goto L55
    L7:
        r3.append(r7);
        r5 = r5 + 1;
    L63:
        if (r4.isEmpty() == true) goto L65;
        applySpansForTag(r17, (StartTag) r4.pop(), r6, r3, r19);
        goto L63
    L65:
        applySpansForTag(r17, StartTag.buildWholeCueVirtualTag(), Collections.emptyList(), r3, r19);
        return SpannedString.valueOf(r3);
    }

    private static int parseLineAnchor(String r4) {
        switch(r4.hashCode()) {
            case -1364013995: goto L15;
            case -1074341483: goto L12;
            case 100571: goto L9;
            case 109757538: goto L6;
            default: goto L17;
        };
    L17:
        byte r0 = -1;
    L18:
        switch(r0) {
            case 0: goto L23;
            case 1: goto L22;
            case 2: goto L22;
            case 3: goto L21;
            default: goto L19;
        };
    L19:
        Log.m48w(TAG, "Invalid anchor value: " + r4);
        return Integer.MIN_VALUE;
    L21:
        return 2;
    L22:
        return 1;
    L23:
        return 0;
    L6:
        if (r4.equals("start") == false) goto L17;
        r0 = 0;
        goto L18
    L9:
        if (r4.equals("end") == false) goto L17;
        r0 = 3;
        goto L18
    L12:
        if (r4.equals("middle") == false) goto L17;
        r0 = 2;
        goto L18
    L15:
        if (r4.equals(TtmlNode.CENTER) == false) goto L17;
        r0 = 1;
        goto L18
    }

    private static void parseLineAttribute(String r3, WebvttCueInfoBuilder r4) {
        int r0 = r3.indexOf(44);
        if (r0 == (-1)) goto L6;
        r4.lineAnchor = parseLineAnchor(r3.substring(r0 + 1));
        r3 = r3.substring(0, r0);
    L6:
        if (r3.endsWith("%") == false) goto L8;
        r4.line = WebvttParserUtil.parsePercentage(r3);
        r4.lineType = 0;
        return;
    L8:
        r4.line = Integer.parseInt(r3);
        r4.lineType = 1;
    }

    private static int parsePositionAnchor(String r4) {
        switch(r4.hashCode()) {
            case -1842484672: goto L21;
            case -1364013995: goto L18;
            case -1276788989: goto L15;
            case -1074341483: goto L12;
            case 100571: goto L9;
            case 109757538: goto L6;
            default: goto L23;
        };
    L23:
        byte r0 = -1;
    L24:
        switch(r0) {
            case 0: goto L29;
            case 1: goto L29;
            case 2: goto L28;
            case 3: goto L28;
            case 4: goto L27;
            case 5: goto L27;
            default: goto L25;
        };
    L25:
        Log.m48w(TAG, "Invalid anchor value: " + r4);
        return Integer.MIN_VALUE;
    L27:
        return 2;
    L28:
        return 1;
    L29:
        return 0;
    L6:
        if (r4.equals("start") == false) goto L23;
        r0 = 1;
        goto L24
    L9:
        if (r4.equals("end") == false) goto L23;
        r0 = 5;
        goto L24
    L12:
        if (r4.equals("middle") == false) goto L23;
        r0 = 3;
        goto L24
    L15:
        if (r4.equals("line-right") == false) goto L23;
        r0 = 4;
        goto L24
    L18:
        if (r4.equals(TtmlNode.CENTER) == false) goto L23;
        r0 = 2;
        goto L24
    L21:
        if (r4.equals("line-left") == false) goto L23;
        r0 = 0;
        goto L24
    }

    private static void parsePositionAttribute(String r2, WebvttCueInfoBuilder r3) {
        int r0 = r2.indexOf(44);
        if (r0 == (-1)) goto L5;
        r3.positionAnchor = parsePositionAnchor(r2.substring(r0 + 1));
        r2 = r2.substring(0, r0);
    L5:
        r3.position = WebvttParserUtil.parsePercentage(r2);
    }

    private static int parseTextAlignment(String r6) {
        switch(r6.hashCode()) {
            case -1364013995: goto L21;
            case -1074341483: goto L18;
            case 100571: goto L15;
            case 3317767: goto L12;
            case 108511772: goto L9;
            case 109757538: goto L6;
            default: goto L23;
        };
    L23:
        byte r0 = -1;
    L24:
        switch(r0) {
            case 0: goto L31;
            case 1: goto L30;
            case 2: goto L29;
            case 3: goto L29;
            case 4: goto L28;
            case 5: goto L27;
            default: goto L25;
        };
    L25:
        Log.m48w(TAG, "Invalid alignment value: " + r6);
        return 2;
    L27:
        return 5;
    L28:
        return 3;
    L29:
        return 2;
    L30:
        return 4;
    L31:
        return 1;
    L6:
        if (r6.equals("start") == false) goto L23;
        r0 = 0;
        goto L24
    L9:
        if (r6.equals(TtmlNode.RIGHT) == false) goto L23;
        r0 = 5;
        goto L24
    L12:
        if (r6.equals(TtmlNode.LEFT) == false) goto L23;
        r0 = 1;
        goto L24
    L15:
        if (r6.equals("end") == false) goto L23;
        r0 = 4;
        goto L24
    L18:
        if (r6.equals("middle") == false) goto L23;
        r0 = 3;
        goto L24
    L21:
        if (r6.equals(TtmlNode.CENTER) == false) goto L23;
        r0 = 2;
        goto L24
    }

    private static int parseVerticalAttribute(String r2) {
        switch(r2.hashCode()) {
            case 3462: goto L9;
            case 3642: goto L6;
            default: goto L11;
        };
    L11:
        byte r0 = -1;
    L12:
        switch(r0) {
            case 0: goto L17;
            case 1: goto L15;
            default: goto L13;
        };
    L13:
        Log.m48w(TAG, "Invalid 'vertical' value: " + r2);
        return Integer.MIN_VALUE;
    L15:
        return 2;
    L17:
        return 1;
    L6:
        if (r2.equals("rl") == false) goto L11;
        r0 = 0;
        goto L12
    L9:
        if (r2.equals("lr") == false) goto L11;
        r0 = 1;
        goto L12
    }
}
