package androidx.media3.extractor.text.ssa;

import android.graphics.Color;
import android.graphics.PointF;
import android.text.TextUtils;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.google.common.base.Ascii;
import com.google.common.primitives.Ints;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
final class SsaStyle {
    public static final int SSA_ALIGNMENT_BOTTOM_CENTER = 2;
    public static final int SSA_ALIGNMENT_BOTTOM_LEFT = 1;
    public static final int SSA_ALIGNMENT_BOTTOM_RIGHT = 3;
    public static final int SSA_ALIGNMENT_MIDDLE_CENTER = 5;
    public static final int SSA_ALIGNMENT_MIDDLE_LEFT = 4;
    public static final int SSA_ALIGNMENT_MIDDLE_RIGHT = 6;
    public static final int SSA_ALIGNMENT_TOP_CENTER = 8;
    public static final int SSA_ALIGNMENT_TOP_LEFT = 7;
    public static final int SSA_ALIGNMENT_TOP_RIGHT = 9;
    public static final int SSA_ALIGNMENT_UNKNOWN = -1;
    public static final int SSA_BORDER_STYLE_BOX = 3;
    public static final int SSA_BORDER_STYLE_OUTLINE = 1;
    public static final int SSA_BORDER_STYLE_UNKNOWN = -1;
    private static final String TAG = "SsaStyle";
    public final int alignment;
    public final boolean bold;
    public final int borderStyle;
    public final float fontSize;
    public final boolean italic;
    public final String name;
    public final Integer outlineColor;
    public final Integer primaryColor;
    public final boolean strikeout;
    public final boolean underline;

    static final class Format {
        public final int alignmentIndex;
        public final int boldIndex;
        public final int borderStyleIndex;
        public final int fontSizeIndex;
        public final int italicIndex;
        public final int length;
        public final int nameIndex;
        public final int outlineColorIndex;
        public final int primaryColorIndex;
        public final int strikeoutIndex;
        public final int underlineIndex;

        private Format(int r1, int r2, int r3, int r4, int r5, int r6, int r7, int r8, int r9, int r10, int r11) {
            this.nameIndex = r1;
            this.alignmentIndex = r2;
            this.primaryColorIndex = r3;
            this.outlineColorIndex = r4;
            this.fontSizeIndex = r5;
            this.boldIndex = r6;
            this.italicIndex = r7;
            this.underlineIndex = r8;
            this.strikeoutIndex = r9;
            this.borderStyleIndex = r10;
            this.length = r11;
        }

        public static Format fromFormatLine(String r25) {
            String[] r10 = TextUtils.split(r25.substring("Format:".length()), ",");
            int r12 = 0;
            int r14 = -1;
            int r15 = -1;
            int r16 = -1;
            int r17 = -1;
            int r18 = -1;
            int r19 = -1;
            int r20 = -1;
            int r21 = -1;
            int r22 = -1;
            int r23 = -1;
        L3:
            byte r1 = -1;
            if (r12 >= r10.length) goto L51;
            String r0 = Ascii.toLowerCase(r10[r12].trim());
            switch(r0.hashCode()) {
                case -1178781136: goto L36;
                case -1026963764: goto L33;
                case -192095652: goto L30;
                case -70925746: goto L27;
                case 3029637: goto L24;
                case 3373707: goto L21;
                case 366554320: goto L18;
                case 767321349: goto L15;
                case 1767875043: goto L12;
                case 1988365454: goto L9;
                default: goto L38;
            };
        L38:
            switch(r1) {
                case 0: goto L49;
                case 1: goto L48;
                case 2: goto L47;
                case 3: goto L46;
                case 4: goto L45;
                case 5: goto L44;
                case 6: goto L43;
                case 7: goto L42;
                case 8: goto L41;
                case 9: goto L40;
                default: goto L50;
            };
        L40:
            r23 = r12;
            goto L50
        L41:
            r22 = r12;
            goto L50
        L42:
            r21 = r12;
            goto L50
        L43:
            r20 = r12;
            goto L50
        L44:
            r19 = r12;
            goto L50
        L45:
            r18 = r12;
            goto L50
        L46:
            r17 = r12;
            goto L50
        L47:
            r16 = r12;
            goto L50
        L48:
            r15 = r12;
            goto L50
        L49:
            r14 = r12;
        L50:
            r12 = r12 + 1;
            goto L3
        L9:
            if (r0.equals("outlinecolour") == false) goto L38;
            r1 = 3;
            goto L38
        L12:
            if (r0.equals("alignment") == false) goto L38;
            r1 = 1;
            goto L38
        L15:
            if (r0.equals("borderstyle") == false) goto L38;
            r1 = 9;
            goto L38
        L18:
            if (r0.equals("fontsize") == false) goto L38;
            r1 = 4;
            goto L38
        L21:
            if (r0.equals("name") == false) goto L38;
            r1 = 0;
            goto L38
        L24:
            if (r0.equals(TtmlNode.BOLD) == false) goto L38;
            r1 = 5;
            goto L38
        L27:
            if (r0.equals("primarycolour") == false) goto L38;
            r1 = 2;
            goto L38
        L30:
            if (r0.equals("strikeout") == false) goto L38;
            r1 = 8;
            goto L38
        L33:
            if (r0.equals(TtmlNode.UNDERLINE) == false) goto L38;
            r1 = 7;
            goto L38
        L36:
            if (r0.equals(TtmlNode.ITALIC) == false) goto L38;
            r1 = 6;
            goto L38
        L51:
            if (r14 != (-1)) goto L67;
            return null;
        L67:
            return new Format(r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r10.length);
        }
    }

    static final class Overrides {
        private static final Pattern ALIGNMENT_OVERRIDE_PATTERN = null;
        private static final Pattern BRACES_PATTERN = null;
        private static final Pattern MOVE_PATTERN = null;
        private static final String PADDED_DECIMAL_PATTERN = "\\s*\\d+(?:\\.\\d+)?\\s*";
        private static final Pattern POSITION_PATTERN = null;
        private static final String TAG = "SsaStyle.Overrides";
        public final int alignment;
        public final PointF position;

        static {
            BRACES_PATTERN = Pattern.compile("\\{([^}]*)\\}");
            POSITION_PATTERN = Pattern.compile(Util.formatInvariant("\\\\pos\\((%1$s),(%1$s)\\)", new Object[]{PADDED_DECIMAL_PATTERN}));
            MOVE_PATTERN = Pattern.compile(Util.formatInvariant("\\\\move\\(%1$s,%1$s,(%1$s),(%1$s)(?:,%1$s,%1$s)?\\)", new Object[]{PADDED_DECIMAL_PATTERN}));
            ALIGNMENT_OVERRIDE_PATTERN = Pattern.compile("\\\\an(\\d+)");
        }

        private Overrides(int r1, PointF r2) {
            this.alignment = r1;
            this.position = r2;
        }

        private static int parseAlignmentOverride(String r2) {
            Matcher r0 = ALIGNMENT_OVERRIDE_PATTERN.matcher(r2);
            if (r0.find() == true) goto L7;
            return -1;
        L7:
            return SsaStyle.access$000((String) Assertions.checkNotNull(r0.group(1)));
        }

        public static Overrides parseFromDialogue(String r6) {
            int r0 = -1;
            PointF r1 = null;
            Matcher r2 = BRACES_PATTERN.matcher(r6);
        L4:
            if (r2.find() == false) goto L19;
            String r3 = (String) Assertions.checkNotNull(r2.group(1));
            PointF r4 = parsePosition(r3);     // Catch: RuntimeException -> L10
            if (r4 == null) goto L22;
            r1 = r4;
        L22:
            int r5 = parseAlignmentOverride(r3);     // Catch: RuntimeException -> L16
            if (r5 == (-1)) goto L4;
            r0 = r5;
            goto L4
        L19:
            return new Overrides(r0, r1);
        }

        private static PointF parsePosition(String r9) {
            Matcher r0 = POSITION_PATTERN.matcher(r9);
            Matcher r1 = MOVE_PATTERN.matcher(r9);
            boolean r2 = r0.find();
            boolean r3 = r1.find();
            if (r2 == false) goto L7;
            if (r3 == false) goto L6;
            Log.m46i(TAG, "Override has both \\pos(x,y) and \\move(x1,y1,x2,y2); using \\pos values. override='" + r9 + "'");
        L6:
            String r5 = r0.group(1);
            String r4 = r0.group(2);
        L10:
            return new PointF(Float.parseFloat(((String) Assertions.checkNotNull(r5)).trim()), Float.parseFloat(((String) Assertions.checkNotNull(r4)).trim()));
        L7:
            if (r3 == false) goto L11;
            r5 = r1.group(1);
            r4 = r1.group(2);
            goto L10
        L11:
            return null;
        }

        public static String stripStyleOverrides(String r2) {
            return BRACES_PATTERN.matcher(r2).replaceAll("");
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface SsaAlignment {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface SsaBorderStyle {
    }

    private SsaStyle(String r1, int r2, Integer r3, Integer r4, float r5, boolean r6, boolean r7, boolean r8, boolean r9, int r10) {
        this.name = r1;
        this.alignment = r2;
        this.primaryColor = r3;
        this.outlineColor = r4;
        this.fontSize = r5;
        this.bold = r6;
        this.italic = r7;
        this.underline = r8;
        this.strikeout = r9;
        this.borderStyle = r10;
    }

    static /* synthetic */ int access$000(String r1) {
        return parseAlignment(r1);
    }

    public static SsaStyle fromStyleLine(String r19, Format r20) {
        Assertions.checkArgument(r19.startsWith("Style:"));
        String[] r3 = TextUtils.split(r19.substring("Style:".length()), ",");
        if (r3.length == r20.length) goto L62;
        Log.m48w(TAG, Util.formatInvariant("Skipping malformed 'Style:' line (expected %s values, found %s): '%s'", new Object[]{Integer.valueOf(r20.length), Integer.valueOf(r3.length), r19}));
        return null;
    L62:
        String r4 = r3[r20.nameIndex].trim();     // Catch: RuntimeException -> L56
        if (r20.alignmentIndex == (-1)) goto L10;
        int r9 = parseAlignment(r3[r20.alignmentIndex].trim());     // Catch: RuntimeException -> L56
    L12:
        if (r20.primaryColorIndex == (-1)) goto L14;
        Integer r11 = parseColor(r3[r20.primaryColorIndex].trim());     // Catch: RuntimeException -> L56
    L16:
        if (r20.outlineColorIndex == (-1)) goto L18;
        Integer r12 = parseColor(r3[r20.outlineColorIndex].trim());     // Catch: RuntimeException -> L56
    L20:
        if (r20.fontSizeIndex == (-1)) goto L22;
        float r13 = parseFontSize(r3[r20.fontSizeIndex].trim());     // Catch: RuntimeException -> L56
    L24:
        if (r20.boldIndex != (-1)) goto L26;
    L28:
        boolean r14 = false;
    L30:
        if (r20.italicIndex != (-1)) goto L32;
    L34:
        boolean r15 = false;
    L36:
        if (r20.underlineIndex == (-1)) goto L40;
        if (parseBooleanValue(r3[r20.underlineIndex].trim()) == false) goto L40;
        boolean r16 = true;
    L60:
    L54:
        e = e;
    L58:
        Log.m49w(TAG, "Skipping malformed 'Style:' line: '" + r19 + "'", e);
        return 0;
    L43:
        if (r20.strikeoutIndex != (-1)) goto L45;
    L47:
        boolean r17 = false;
    L49:
        if (r20.borderStyleIndex == (-1)) goto L51;
        int r18 = parseBorderStyle(r3[r20.borderStyleIndex].trim());     // Catch: RuntimeException -> L54
    L52:
        return new SsaStyle(r4, r9, r11, r12, r13, r14, r15, r16, r17, r18);
    L51:
        r18 = -1;
        goto L52
    L45:
        if (parseBooleanValue(r3[r20.strikeoutIndex].trim()) == false) goto L47;
        r17 = true;
    L40:
        r16 = false;
        goto L60
    L32:
        if (parseBooleanValue(r3[r20.italicIndex].trim()) == false) goto L34;
        r15 = true;
        goto L36
    L26:
        if (parseBooleanValue(r3[r20.boldIndex].trim()) == false) goto L28;
        r14 = true;
        goto L30
    L22:
        r13 = -3.4028235E38f;
        goto L24
    L18:
        r12 = null;
        goto L20
    L14:
        r11 = null;
        goto L16
    L10:
        r9 = -1;
    L56:
        e = e;
        goto L58
    }

    private static boolean isValidAlignment(int r1) {
        switch(r1) {
            case 1: goto L5;
            case 2: goto L5;
            case 3: goto L5;
            case 4: goto L5;
            case 5: goto L5;
            case 6: goto L5;
            case 7: goto L5;
            case 8: goto L5;
            case 9: goto L5;
            default: goto L3;
        };
    L3:
        return false;
    L5:
        return true;
    }

    private static boolean isValidBorderStyle(int r1) {
        switch(r1) {
            case 1: goto L5;
            case 2: goto L3;
            case 3: goto L5;
            default: goto L3;
        };
    L3:
        return false;
    L5:
        return true;
    }

    private static int parseAlignment(String r2) {
        int r0 = Integer.parseInt(r2.trim());     // Catch: NumberFormatException -> L6
        if (isValidAlignment(r0) == false) goto L7;
        return r0;
    L7:
        Log.m48w(TAG, "Ignoring unknown alignment: " + r2);
        return -1;
    }

    private static boolean parseBooleanValue(String r4) {
        int r1 = Integer.parseInt(r4);     // Catch: NumberFormatException -> L10
        if (r1 == 1) goto L9;
        if (r1 == (-1)) goto L9;
        return false;
    L9:
        return true;
    L10:
        e = move-exception;
        Log.m49w(TAG, "Failed to parse boolean value: '" + r4 + "'", e);
        return false;
    }

    private static int parseBorderStyle(String r2) {
        int r0 = Integer.parseInt(r2.trim());     // Catch: NumberFormatException -> L6
        if (isValidBorderStyle(r0) == false) goto L7;
        return r0;
    L7:
        Log.m48w(TAG, "Ignoring unknown BorderStyle: " + r2);
        return -1;
    }

    public static Integer parseColor(String r8) {
    L13:
        e = move-exception;
        Log.m49w(TAG, "Failed to parse color expression: '" + r8 + "'", e);
        return null;
    L3:
        if (r8.startsWith("&H") == false) goto L5;
        long r2 = Long.parseLong(r8.substring(2), 16);     // Catch: IllegalArgumentException -> L13
    L7:
        if (r2 > 4294967295L) goto L9;
        boolean r0 = true;
    L10:
        Assertions.checkArgument(r0);     // Catch: IllegalArgumentException -> L13
        int r1 = Ints.checkedCast(((r2 >> 24) & 255) ^ 255);
        int r3 = Ints.checkedCast((r2 >> 16) & 255);
        return Integer.valueOf(Color.argb(r1, Ints.checkedCast(255 & r2), Ints.checkedCast((r2 >> 8) & 255), r3));
    L9:
        r0 = false;
        goto L10
    L5:
        r2 = Long.parseLong(r8);     // Catch: IllegalArgumentException -> L13
        goto L7
    }

    private static float parseFontSize(String r3) {
        return Float.parseFloat(r3);
    L4:
        e = move-exception;
        Log.m49w(TAG, "Failed to parse font size: '" + r3 + "'", e);
        return -3.4028235E38f;
    }
}
