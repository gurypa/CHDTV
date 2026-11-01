package androidx.media3.extractor.text.webvtt;

import android.text.TextUtils;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ColorParser;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import com.google.common.base.Ascii;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
final class WebvttCssParser {
    private static final Pattern FONT_SIZE_PATTERN = null;
    private static final String PROPERTY_BGCOLOR = "background-color";
    private static final String PROPERTY_COLOR = "color";
    private static final String PROPERTY_FONT_FAMILY = "font-family";
    private static final String PROPERTY_FONT_SIZE = "font-size";
    private static final String PROPERTY_FONT_STYLE = "font-style";
    private static final String PROPERTY_FONT_WEIGHT = "font-weight";
    private static final String PROPERTY_RUBY_POSITION = "ruby-position";
    private static final String PROPERTY_TEXT_COMBINE_UPRIGHT = "text-combine-upright";
    private static final String PROPERTY_TEXT_DECORATION = "text-decoration";
    private static final String RULE_END = "}";
    private static final String RULE_START = "{";
    private static final String TAG = "WebvttCssParser";
    private static final String VALUE_ALL = "all";
    private static final String VALUE_BOLD = "bold";
    private static final String VALUE_DIGITS = "digits";
    private static final String VALUE_ITALIC = "italic";
    private static final String VALUE_OVER = "over";
    private static final String VALUE_UNDER = "under";
    private static final String VALUE_UNDERLINE = "underline";
    private static final Pattern VOICE_NAME_PATTERN = null;
    private final StringBuilder stringBuilder;
    private final ParsableByteArray styleInput;

    static {
        VOICE_NAME_PATTERN = Pattern.compile("\\[voice=\"([^\"]*)\"\\]");
        FONT_SIZE_PATTERN = Pattern.compile("^((?:[0-9]*\\.)?[0-9]+)(px|em|%)$");
    }

    public WebvttCssParser() {
        this.styleInput = new ParsableByteArray();
        this.stringBuilder = new StringBuilder();
    }

    private void applySelectorToStyle(WebvttCssStyle r8, String r9) {
        if ("".equals(r9) == false) goto L5;
        return;
    L5:
        int r0 = r9.indexOf(91);
        if (r0 == (-1)) goto L11;
        Matcher r4 = VOICE_NAME_PATTERN.matcher(r9.substring(r0));
        if (r4.matches() == false) goto L10;
        r8.setTargetVoice((String) Assertions.checkNotNull(r4.group(1)));
    L10:
        r9 = r9.substring(0, r0);
    L11:
        String[] r5 = Util.split(r9, "\\.");
        String r6 = r5[0];
        int r7 = r6.indexOf(35);
        if (r7 == (-1)) goto L14;
        r8.setTargetTagName(r6.substring(0, r7));
        r8.setTargetId(r6.substring(r7 + 1));
    L16:
        if (r5.length <= 1) goto L19;
        r8.setTargetClasses((String[]) Util.nullSafeArrayCopyOfRange(r5, 1, r5.length));
        return;
    L19:
        return;
    L14:
        r8.setTargetTagName(r6);
        goto L16
    }

    private static boolean maybeSkipComment(ParsableByteArray r7) {
        int r0 = r7.getPosition();
        int r1 = r7.limit();
        byte[] r2 = r7.getData();
        if ((r0 + 2) > r1) goto L29;
        int r3 = r0 + 1;
        if (r2[r0] != 47) goto L20;
        int r4 = r3 + 1;
        if (r2[r3] == 42) goto L9;
        return false;
    L9:
        if ((r4 + 1) >= r1) goto L17;
        int r5 = r4 + 1;
        if (((char) r2[r4]) != '*') goto L15;
        if (((char) r2[r5]) != '/') goto L15;
        int r6 = r5 + 1;
        r1 = r6;
        r4 = r6;
    L15:
        r4 = r5;
        goto L9
    L17:
        r7.skipBytes(r1 - r7.getPosition());
        return true;
    L20:
        return false;
    L29:
        return false;
    }

    private static boolean maybeSkipWhitespace(ParsableByteArray r1) {
        switch(peekCharAtPosition(r1, r1.getPosition())) {
            case 9: goto L6;
            case 10: goto L6;
            case 12: goto L6;
            case 13: goto L6;
            case 32: goto L6;
            default: goto L4;
        };
    L4:
        return false;
    L6:
        r1.skipBytes(1);
        return true;
    }

    private static void parseFontSize(String r5, WebvttCssStyle r6) {
        Matcher r0 = FONT_SIZE_PATTERN.matcher(Ascii.toLowerCase(r5));
        if (r0.matches() == true) goto L6;
        Log.m48w(TAG, "Invalid font-size: '" + r5 + "'.");
        return;
    L6:
        String r2 = (String) Assertions.checkNotNull(r0.group(2));
        switch(r2.hashCode()) {
            case 37: goto L16;
            case 3240: goto L13;
            case 3592: goto L10;
            default: goto L18;
        };
    L18:
        byte r3 = -1;
    L19:
        switch(r3) {
            case 0: goto L24;
            case 1: goto L23;
            case 2: goto L22;
            default: goto L21;
        };
    L22:
        r6.setFontSizeUnit(3);
    L25:
        r6.setFontSize(Float.parseFloat((String) Assertions.checkNotNull(r0.group(1))));
        return;
    L23:
        r6.setFontSizeUnit(2);
        goto L25
    L24:
        r6.setFontSizeUnit(1);
        goto L25
    L21:
        throw new IllegalStateException();
    L10:
        if (r2.equals("px") == false) goto L18;
        r3 = 0;
        goto L19
    L13:
        if (r2.equals("em") == false) goto L18;
        r3 = 1;
        goto L19
    L16:
        if (r2.equals("%") == false) goto L18;
        r3 = 2;
        goto L19
    }

    private static String parseIdentifier(ParsableByteArray r5, StringBuilder r6) {
        r6.setLength(0);
        int r0 = r5.getPosition();
        int r1 = r5.limit();
        boolean r2 = false;
    L3:
        if (r0 >= r1) goto L29;
        if (r2 == true) goto L29;
        char r3 = (char) r5.getData()[r0];
        if (r3 < 'A') goto L10;
        if (r3 > 'Z') goto L10;
    L27:
        r0 = r0 + 1;
        r6.append(r3);
    L10:
        if (r3 < 'a') goto L14;
        if (r3 <= 'z') goto L27;
    L14:
        if (r3 < '0') goto L18;
        if (r3 <= '9') goto L27;
    L18:
        if (r3 == '#') goto L27;
        if (r3 == '-') goto L27;
        if (r3 == '.') goto L27;
        if (r3 == '_') goto L27;
        r2 = true;
    L29:
        r5.skipBytes(r0 - r5.getPosition());
        return r6.toString();
    }

    static String parseNextToken(ParsableByteArray r3, StringBuilder r4) {
        skipWhitespaceAndComments(r3);
        if (r3.bytesLeft() != 0) goto L6;
        return null;
    L6:
        String r0 = parseIdentifier(r3, r4);
        if ("".equals(r0) == true) goto L10;
        return r0;
    L10:
        return "" + ((char) r3.readUnsignedByte());
    }

    private static String parsePropertyValue(ParsableByteArray r5, StringBuilder r6) {
        StringBuilder r0 = new StringBuilder();
        boolean r1 = false;
    L3:
        if (r1 == true) goto L16;
        int r2 = r5.getPosition();
        String r3 = parseNextToken(r5, r6);
        if (r3 == null) goto L6;
        if (RULE_END.equals(r3) == true) goto L14;
        if (";".equals(r3) == true) goto L14;
        r0.append(r3);
    L14:
        r5.setPosition(r2);
        r1 = true;
        goto L3
    L6:
        return null;
    L16:
        return r0.toString();
    }

    private static String parseSelector(ParsableByteArray r6, StringBuilder r7) {
        skipWhitespaceAndComments(r6);
        if (r6.bytesLeft() >= 5) goto L6;
        return null;
    L6:
        if ("::cue".equals(r6.readString(5)) == true) goto L8;
        return null;
    L8:
        int r2 = r6.getPosition();
        String r3 = parseNextToken(r6, r7);
        if (r3 != null) goto L12;
        return null;
    L12:
        if (RULE_START.equals(r3) == false) goto L15;
        r6.setPosition(r2);
        return "";
    L15:
        String r4 = null;
        if ("(".equals(r3) == false) goto L19;
        r4 = readCueTarget(r6);
    L19:
        if (")".equals(parseNextToken(r6, r7)) == true) goto L21;
        return null;
    L21:
        return r4;
    }

    private static void parseStyleDeclaration(ParsableByteArray r6, WebvttCssStyle r7, StringBuilder r8) {
        skipWhitespaceAndComments(r6);
        String r0 = parseIdentifier(r6, r8);
        if ("".equals(r0) == false) goto L6;
        return;
    L6:
        if (":".equals(parseNextToken(r6, r8)) == true) goto L8;
        return;
    L8:
        skipWhitespaceAndComments(r6);
        String r2 = parsePropertyValue(r6, r8);
        if (r2 != null) goto L11;
        return;
    L11:
        if ("".equals(r2) == true) goto L80;
        int r1 = r6.getPosition();
        String r3 = parseNextToken(r6, r8);
        if (";".equals(r3) == true) goto L20;
        if (RULE_END.equals(r3) == false) goto L64;
        r6.setPosition(r1);
        goto L20
    L64:
        return;
    L20:
        if ("color".equals(r0) == false) goto L23;
        r7.setFontColor(ColorParser.parseCssColor(r2));
        return;
    L23:
        if (PROPERTY_BGCOLOR.equals(r0) == false) goto L25;
        r7.setBackgroundColor(ColorParser.parseCssColor(r2));
        return;
    L25:
        boolean r5 = true;
        if (PROPERTY_RUBY_POSITION.equals(r0) == false) goto L34;
        if (VALUE_OVER.equals(r2) == false) goto L31;
        r7.setRubyPosition(1);
        return;
    L31:
        if (VALUE_UNDER.equals(r2) == false) goto L69;
        r7.setRubyPosition(2);
        return;
    L69:
        return;
    L34:
        if (PROPERTY_TEXT_COMBINE_UPRIGHT.equals(r0) == false) goto L43;
        if ("all".equals(r2) == false) goto L38;
    L41:
        r7.setCombineUpright(r5);
        return;
    L38:
        if (r2.startsWith(VALUE_DIGITS) == true) goto L41;
        r5 = false;
        goto L41
    L43:
        if (PROPERTY_TEXT_DECORATION.equals(r0) == false) goto L48;
        if ("underline".equals(r2) == false) goto L72;
        r7.setUnderline(true);
        return;
    L72:
        return;
    L48:
        if (PROPERTY_FONT_FAMILY.equals(r0) == false) goto L51;
        r7.setFontFamily(r2);
        return;
    L51:
        if (PROPERTY_FONT_WEIGHT.equals(r0) == false) goto L56;
        if ("bold".equals(r2) == false) goto L75;
        r7.setBold(true);
        return;
    L75:
        return;
    L56:
        if (PROPERTY_FONT_STYLE.equals(r0) == false) goto L61;
        if ("italic".equals(r2) == false) goto L77;
        r7.setItalic(true);
        return;
    L77:
        return;
    L61:
        if (PROPERTY_FONT_SIZE.equals(r0) == false) goto L79;
        parseFontSize(r2, r7);
        return;
    L79:
        return;
    }

    private static char peekCharAtPosition(ParsableByteArray r1, int r2) {
        return (char) r1.getData()[r2];
    }

    private static String readCueTarget(ParsableByteArray r5) {
        int r0 = r5.getPosition();
        int r1 = r5.limit();
        boolean r2 = false;
    L3:
        if (r0 >= r1) goto L11;
        if (r2 == true) goto L11;
        int r4 = r0 + 1;
        if (((char) r5.getData()[r0]) != ')') goto L8;
        boolean r3 = true;
    L9:
        r2 = r3;
        r0 = r4;
        goto L3
    L8:
        r3 = false;
    L11:
        return r5.readString((r0 - 1) - r5.getPosition()).trim();
    }

    static void skipStyleBlock(ParsableByteArray r2) {
    L3:
        if (TextUtils.isEmpty(r2.readLine()) == false) goto L3;
    }

    static void skipWhitespaceAndComments(ParsableByteArray r2) {
        boolean r0 = true;
    L4:
        if (r2.bytesLeft() <= 0) goto L14;
        if (r0 == false) goto L19;
        if (maybeSkipWhitespace(r2) == false) goto L9;
    L12:
        boolean r1 = true;
    L13:
        r0 = r1;
        goto L4
    L9:
        if (maybeSkipComment(r2) == true) goto L12;
        r1 = false;
        goto L13
    L19:
        return;
    }

    public List<WebvttCssStyle> parseBlock(ParsableByteArray r12) {
        this.stringBuilder.setLength(0);
        int r0 = r12.getPosition();
        skipStyleBlock(r12);
        this.styleInput.reset(r12.getData(), r12.getPosition());
        this.styleInput.setPosition(r0);
        ArrayList r2 = new ArrayList();
    L3:
        String r3 = parseSelector(this.styleInput, this.stringBuilder);
        if (r3 == null) goto L26;
        if (RULE_START.equals(parseNextToken(this.styleInput, this.stringBuilder)) == false) goto L7;
        WebvttCssStyle r4 = new WebvttCssStyle();
        applySelectorToStyle(r4, r3);
        String r5 = null;
        boolean r6 = false;
    L10:
        if (r6 == true) goto L23;
        int r8 = this.styleInput.getPosition();
        r5 = parseNextToken(this.styleInput, this.stringBuilder);
        if (r5 != null) goto L14;
    L17:
        boolean r7 = true;
    L18:
        r6 = r7;
        if (r6 == true) goto L10;
        this.styleInput.setPosition(r8);
        parseStyleDeclaration(this.styleInput, r4, this.stringBuilder);
        goto L10
    L14:
        if (RULE_END.equals(r5) == true) goto L17;
        r7 = false;
        goto L18
    L23:
        if (RULE_END.equals(r5) == false) goto L3;
        r2.add(r4);
        goto L3
    L7:
        return r2;
    L26:
        return r2;
    }
}
