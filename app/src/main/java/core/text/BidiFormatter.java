package androidx.core.text;

import android.text.SpannableStringBuilder;
import com.google.common.base.Ascii;
import java.util.Locale;
import kotlin.text.Typography;

/* JADX INFO: loaded from: classes.dex */
public final class BidiFormatter {
    private static final int DEFAULT_FLAGS = 2;
    static final BidiFormatter DEFAULT_LTR_INSTANCE = null;
    static final BidiFormatter DEFAULT_RTL_INSTANCE = null;
    static final TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC = null;
    private static final int DIR_LTR = -1;
    private static final int DIR_RTL = 1;
    private static final int DIR_UNKNOWN = 0;
    private static final String EMPTY_STRING = "";
    private static final int FLAG_STEREO_RESET = 2;
    private static final char LRE = 8234;
    private static final char LRM = 8206;
    private static final String LRM_STRING = null;
    private static final char PDF = 8236;
    private static final char RLE = 8235;
    private static final char RLM = 8207;
    private static final String RLM_STRING = null;
    private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
    private final int mFlags;
    private final boolean mIsRtlContext;

    public static final class Builder {
        private int mFlags;
        private boolean mIsRtlContext;
        private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;

        public Builder() {
            initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
        }

        public Builder(Locale r2) {
            initialize(BidiFormatter.isRtlLocale(r2));
        }

        public Builder(boolean r1) {
            initialize(r1);
        }

        private static BidiFormatter getDefaultInstanceFromContext(boolean r1) {
            if (r1 == false) goto L5;
            return BidiFormatter.DEFAULT_RTL_INSTANCE;
        L5:
            return BidiFormatter.DEFAULT_LTR_INSTANCE;
        }

        private void initialize(boolean r2) {
            this.mIsRtlContext = r2;
            this.mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
            this.mFlags = 2;
        }

        public BidiFormatter build() {
            if (this.mFlags != 2) goto L9;
            if (this.mTextDirectionHeuristicCompat != BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC) goto L9;
            return getDefaultInstanceFromContext(this.mIsRtlContext);
        L9:
            return new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat);
        }

        public Builder setTextDirectionHeuristic(TextDirectionHeuristicCompat r1) {
            this.mTextDirectionHeuristicCompat = r1;
            return this;
        }

        public Builder stereoReset(boolean r2) {
            if (r2 == false) goto L4;
            this.mFlags |= 2;
        L5:
            return this;
        L4:
            this.mFlags &= -3;
            goto L5
        }
    }

    private static class DirectionalityEstimator {
        private static final byte[] DIR_TYPE_CACHE = null;
        private static final int DIR_TYPE_CACHE_SIZE = 1792;
        private int charIndex;
        private final boolean isHtml;
        private char lastChar;
        private final int length;
        private final CharSequence text;

        static {
            DIR_TYPE_CACHE = new byte[DIR_TYPE_CACHE_SIZE];
            int r1 = 0;
        L3:
            if (r1 >= DIR_TYPE_CACHE_SIZE) goto L5;
            DIR_TYPE_CACHE[r1] = Character.getDirectionality(r1);
            r1 = r1 + 1;
            goto L3
        }

        DirectionalityEstimator(CharSequence r2, boolean r3) {
            this.text = r2;
            this.isHtml = r3;
            this.length = r2.length();
        }

        private static byte getCachedDirectionality(char r1) {
            if (r1 >= DIR_TYPE_CACHE_SIZE) goto L6;
            return DIR_TYPE_CACHE[r1];
        L6:
            return Character.getDirectionality(r1);
        }

        private byte skipEntityBackward() {
            int r0 = this.charIndex;
        L4:
            if (this.charIndex <= 0) goto L11;
            CharSequence r1 = this.text;
            int r3 = this.charIndex - 1;
            this.charIndex = r3;
            this.lastChar = r1.charAt(r3);
            if (this.lastChar == '&') goto L7;
            if (this.lastChar != ';') goto L4;
        L7:
            return Ascii.f81FF;
        L11:
            this.charIndex = r0;
            this.lastChar = ';';
            return Ascii.f79CR;
        }

        private byte skipEntityForward() {
        L3:
            if (this.charIndex >= this.length) goto L7;
            CharSequence r0 = this.text;
            int r1 = this.charIndex;
            this.charIndex = r1 + 1;
            char r2 = r0.charAt(r1);
            this.lastChar = r2;
            if (r2 != ';') goto L3;
            return Ascii.f81FF;
        L7:
            return Ascii.f81FF;
        }

        private byte skipTagBackward() {
            int r0 = this.charIndex;
        L4:
            if (this.charIndex <= 0) goto L23;
            CharSequence r1 = this.text;
            int r3 = this.charIndex - 1;
            this.charIndex = r3;
            this.lastChar = r1.charAt(r3);
            if (this.lastChar == '<') goto L7;
            if (this.lastChar == '>') goto L23;
            if (this.lastChar == '\"') goto L16;
            if (this.lastChar != '\'') goto L4;
        L16:
            char r2 = this.lastChar;
        L18:
            if (this.charIndex <= 0) goto L4;
            CharSequence r4 = this.text;
            int r5 = this.charIndex - 1;
            this.charIndex = r5;
            char r6 = r4.charAt(r5);
            this.lastChar = r6;
            if (r6 == r2) goto L4;
        L7:
            return Ascii.f81FF;
        L23:
            this.charIndex = r0;
            this.lastChar = Typography.greater;
            return Ascii.f79CR;
        }

        private byte skipTagForward() {
            int r0 = this.charIndex;
        L4:
            if (this.charIndex >= this.length) goto L20;
            CharSequence r1 = this.text;
            int r2 = this.charIndex;
            this.charIndex = r2 + 1;
            this.lastChar = r1.charAt(r2);
            if (this.lastChar == '>') goto L7;
            if (this.lastChar == '\"') goto L13;
            if (this.lastChar != '\'') goto L4;
        L13:
            char r3 = this.lastChar;
        L15:
            if (this.charIndex >= this.length) goto L4;
            CharSequence r4 = this.text;
            int r5 = this.charIndex;
            this.charIndex = r5 + 1;
            char r6 = r4.charAt(r5);
            this.lastChar = r6;
            if (r6 == r3) goto L4;
        L7:
            return Ascii.f81FF;
        L20:
            this.charIndex = r0;
            this.lastChar = Typography.less;
            return Ascii.f79CR;
        }

        byte dirTypeBackward() {
            this.lastChar = this.text.charAt(this.charIndex - 1);
            if (Character.isLowSurrogate(this.lastChar) == false) goto L6;
            int r0 = Character.codePointBefore(this.text, this.charIndex);
            this.charIndex -= Character.charCount(r0);
            return Character.getDirectionality(r0);
        L6:
            this.charIndex--;
            byte r1 = getCachedDirectionality(this.lastChar);
            if (this.isHtml == true) goto L9;
            return r1;
        L9:
            if (this.lastChar != '>') goto L12;
            return skipTagBackward();
        L12:
            if (this.lastChar == ';') goto L14;
            return r1;
        L14:
            return skipEntityBackward();
        }

        byte dirTypeForward() {
            this.lastChar = this.text.charAt(this.charIndex);
            if (Character.isHighSurrogate(this.lastChar) == false) goto L6;
            int r0 = Character.codePointAt(this.text, this.charIndex);
            this.charIndex += Character.charCount(r0);
            return Character.getDirectionality(r0);
        L6:
            this.charIndex++;
            byte r1 = getCachedDirectionality(this.lastChar);
            if (this.isHtml == true) goto L9;
            return r1;
        L9:
            if (this.lastChar != '<') goto L12;
            return skipTagForward();
        L12:
            if (this.lastChar == '&') goto L14;
            return r1;
        L14:
            return skipEntityForward();
        }

        int getEntryDir() {
            this.charIndex = 0;
            int r1 = 0;
            int r2 = 0;
            int r3 = 0;
        L4:
            if (this.charIndex >= this.length) goto L19;
            if (r3 != 0) goto L19;
            switch(dirTypeForward()) {
                case 0: goto L16;
                case 1: goto L13;
                case 2: goto L13;
                case 9: goto L4;
                case 14: goto L11;
                case 15: goto L11;
                case 16: goto L10;
                case 17: goto L10;
                case 18: goto L9;
                default: goto L8;
            };
        L8:
            r3 = r1;
            goto L4
        L9:
            r1 = r1 - 1;
            r2 = 0;
            goto L4
        L10:
            r1 = r1 + 1;
            r2 = 1;
            goto L4
        L11:
            r1 = r1 + 1;
            r2 = -1;
            goto L4
        L13:
            if (r1 == 0) goto L14;
            r3 = r1;
            goto L4
        L14:
            return 1;
        L16:
            if (r1 == 0) goto L17;
            r3 = r1;
            goto L4
        L17:
            return -1;
        L19:
            if (r3 != 0) goto L21;
            return 0;
        L21:
            if (r2 == 0) goto L24;
            return r2;
        L24:
            if (this.charIndex <= 0) goto L36;
            switch(dirTypeBackward()) {
                case 14: goto L32;
                case 15: goto L32;
                case 16: goto L29;
                case 17: goto L29;
                case 18: goto L28;
                default: goto L24;
            };
        L28:
            r1 = r1 + 1;
            goto L24
        L29:
            if (r3 == r1) goto L30;
            r1 = r1 - 1;
            goto L24
        L30:
            return 1;
        L32:
            if (r3 == r1) goto L33;
            r1 = r1 - 1;
            goto L24
        L33:
            return -1;
        L36:
            return 0;
        }

        int getExitDir() {
            this.charIndex = this.length;
            int r0 = 0;
            int r1 = 0;
        L4:
            if (this.charIndex <= 0) goto L25;
            switch(dirTypeBackward()) {
                case 0: goto L21;
                case 1: goto L17;
                case 2: goto L17;
                case 9: goto L4;
                case 14: goto L13;
                case 15: goto L13;
                case 16: goto L10;
                case 17: goto L10;
                case 18: goto L9;
                default: goto L7;
            };
        L7:
            if (r1 != 0) goto L4;
            r1 = r0;
            goto L4
        L9:
            r0 = r0 + 1;
            goto L4
        L10:
            if (r1 == r0) goto L11;
            r0 = r0 - 1;
            goto L4
        L11:
            return 1;
        L13:
            if (r1 == r0) goto L14;
            r0 = r0 - 1;
            goto L4
        L14:
            return -1;
        L17:
            if (r0 == 0) goto L18;
            if (r1 != 0) goto L4;
            r1 = r0;
            goto L4
        L18:
            return 1;
        L21:
            if (r0 == 0) goto L22;
            if (r1 != 0) goto L4;
            r1 = r0;
            goto L4
        L22:
            return -1;
        L25:
            return 0;
        }
    }

    static {
        DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        LRM_STRING = Character.toString(LRM);
        RLM_STRING = Character.toString(RLM);
        DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
        DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
    }

    BidiFormatter(boolean r1, int r2, TextDirectionHeuristicCompat r3) {
        this.mIsRtlContext = r1;
        this.mFlags = r2;
        this.mDefaultTextDirectionHeuristicCompat = r3;
    }

    private static int getEntryDir(CharSequence r2) {
        return new DirectionalityEstimator(r2, false).getEntryDir();
    }

    private static int getExitDir(CharSequence r2) {
        return new DirectionalityEstimator(r2, false).getExitDir();
    }

    public static BidiFormatter getInstance() {
        return new Builder().build();
    }

    public static BidiFormatter getInstance(Locale r1) {
        return new Builder(r1).build();
    }

    public static BidiFormatter getInstance(boolean r1) {
        return new Builder(r1).build();
    }

    static boolean isRtlLocale(Locale r2) {
        if (TextUtilsCompat.getLayoutDirectionFromLocale(r2) == 1) goto L7;
        return false;
    L7:
        return true;
    }

    private String markAfter(CharSequence r4, TextDirectionHeuristicCompat r5) {
        boolean r0 = r5.isRtl(r4, 0, r4.length());
        if (this.mIsRtlContext == true) goto L10;
        if (r0 == true) goto L8;
        if (getExitDir(r4) != 1) goto L10;
    L8:
        return LRM_STRING;
    L10:
        if (this.mIsRtlContext == false) goto L16;
        if (r0 == false) goto L15;
        if (getExitDir(r4) == (-1)) goto L15;
        return "";
    L15:
        return RLM_STRING;
    L16:
        return "";
    }

    private String markBefore(CharSequence r4, TextDirectionHeuristicCompat r5) {
        boolean r0 = r5.isRtl(r4, 0, r4.length());
        if (this.mIsRtlContext == true) goto L10;
        if (r0 == true) goto L8;
        if (getEntryDir(r4) != 1) goto L10;
    L8:
        return LRM_STRING;
    L10:
        if (this.mIsRtlContext == false) goto L16;
        if (r0 == false) goto L15;
        if (getEntryDir(r4) == (-1)) goto L15;
        return "";
    L15:
        return RLM_STRING;
    L16:
        return "";
    }

    public boolean getStereoReset() {
        if ((this.mFlags & 2) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isRtl(CharSequence r4) {
        return this.mDefaultTextDirectionHeuristicCompat.isRtl(r4, 0, r4.length());
    }

    public boolean isRtl(String r2) {
        return isRtl(r2);
    }

    public boolean isRtlContext() {
        return this.mIsRtlContext;
    }

    public CharSequence unicodeWrap(CharSequence r3) {
        return unicodeWrap(r3, this.mDefaultTextDirectionHeuristicCompat, true);
    }

    public CharSequence unicodeWrap(CharSequence r2, TextDirectionHeuristicCompat r3) {
        return unicodeWrap(r2, r3, true);
    }

    public CharSequence unicodeWrap(CharSequence r4, TextDirectionHeuristicCompat r5, boolean r6) {
        if (r4 != null) goto L5;
        return null;
    L5:
        boolean r0 = r5.isRtl(r4, 0, r4.length());
        SpannableStringBuilder r1 = new SpannableStringBuilder();
        if (getStereoReset() == false) goto L13;
        if (r6 == false) goto L13;
        if (r0 == false) goto L10;
        TextDirectionHeuristicCompat r2 = TextDirectionHeuristicsCompat.RTL;
    L11:
        r1.append(markBefore(r4, r2));
        goto L13
    L10:
        r2 = TextDirectionHeuristicsCompat.LTR;
    L13:
        if (r0 == this.mIsRtlContext) goto L18;
        if (r0 == false) goto L16;
        char r3 = RLE;
    L17:
        r1.append(r3);
        r1.append(r4);
        r1.append(PDF);
    L19:
        if (r6 == false) goto L24;
        if (r0 == false) goto L22;
        TextDirectionHeuristicCompat r7 = TextDirectionHeuristicsCompat.RTL;
    L23:
        r1.append(markAfter(r4, r7));
        goto L24
    L22:
        r7 = TextDirectionHeuristicsCompat.LTR;
    L24:
        return r1;
    L16:
        r3 = LRE;
        goto L17
    L18:
        r1.append(r4);
        goto L19
    }

    public CharSequence unicodeWrap(CharSequence r2, boolean r3) {
        return unicodeWrap(r2, this.mDefaultTextDirectionHeuristicCompat, r3);
    }

    public String unicodeWrap(String r3) {
        return unicodeWrap(r3, this.mDefaultTextDirectionHeuristicCompat, true);
    }

    public String unicodeWrap(String r2, TextDirectionHeuristicCompat r3) {
        return unicodeWrap(r2, r3, true);
    }

    public String unicodeWrap(String r2, TextDirectionHeuristicCompat r3, boolean r4) {
        if (r2 != null) goto L6;
        return null;
    L6:
        return unicodeWrap(r2, r3, r4).toString();
    }

    public String unicodeWrap(String r2, boolean r3) {
        return unicodeWrap(r2, this.mDefaultTextDirectionHeuristicCompat, r3);
    }
}
