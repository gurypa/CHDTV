package com.google.common.base;

import java.io.Serializable;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract enum CaseFormat extends Enum<CaseFormat> {
    private static final /* synthetic */ CaseFormat[] $VALUES = null;
    public static final CaseFormat LOWER_CAMEL = null;
    public static final CaseFormat LOWER_HYPHEN = null;
    public static final CaseFormat LOWER_UNDERSCORE = null;
    public static final CaseFormat UPPER_CAMEL = null;
    public static final CaseFormat UPPER_UNDERSCORE = null;
    private final CharMatcher wordBoundary;
    private final String wordSeparator;

    private static final class StringConverter extends Converter<String, String> implements Serializable {
        private static final long serialVersionUID = 0;
        private final CaseFormat sourceFormat;
        private final CaseFormat targetFormat;

        StringConverter(CaseFormat r2, CaseFormat r3) {
            this.sourceFormat = (CaseFormat) Preconditions.checkNotNull(r2);
            this.targetFormat = (CaseFormat) Preconditions.checkNotNull(r3);
        }

        @Override // com.google.common.base.Converter
        protected /* bridge */ /* synthetic */ String doBackward(String r1) {
            return doBackward2(r1);
        }

        /* JADX INFO: renamed from: doBackward, reason: avoid collision after fix types in other method */
        protected String doBackward2(String r3) {
            return this.targetFormat.m180to(this.sourceFormat, r3);
        }

        @Override // com.google.common.base.Converter
        protected /* bridge */ /* synthetic */ String doForward(String r1) {
            return doForward2(r1);
        }

        /* JADX INFO: renamed from: doForward, reason: avoid collision after fix types in other method */
        protected String doForward2(String r3) {
            return this.sourceFormat.m180to(this.targetFormat, r3);
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function
        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof StringConverter) == false) goto L10;
            StringConverter r0 = (StringConverter) r5;
            if (this.sourceFormat.equals(r0.sourceFormat) == true) goto L7;
            return false;
        L7:
            if (this.targetFormat.equals(r0.targetFormat) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        public int hashCode() {
            return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode();
        }

        public String toString() {
            return this.sourceFormat + ".converterTo(" + this.targetFormat + ")";
        }
    }

    private static /* synthetic */ CaseFormat[] $values() {
        return new CaseFormat[]{LOWER_HYPHEN, LOWER_UNDERSCORE, LOWER_CAMEL, UPPER_CAMEL, UPPER_UNDERSCORE};
    }

    static {
        final CharMatcher r1 = CharMatcher.m181is('-');
        final String r2 = "-";
        final String r3 = "LOWER_HYPHEN";
        final int r4 = 0;
        LOWER_HYPHEN = new C05281(r3, r4, r1, r2);
        final CharMatcher r5 = CharMatcher.m181is('_');
        final String r6 = "LOWER_UNDERSCORE";
        final int r7 = 1;
        final String r8 = "_";
        LOWER_UNDERSCORE = new C05292(r6, r7, r5, r8);
        final CharMatcher r9 = CharMatcher.inRange('A', 'Z');
        final String r10 = "LOWER_CAMEL";
        final int r11 = 2;
        final String r12 = "";
        LOWER_CAMEL = new C05303(r10, r11, r9, r12);
        final int r13 = 3;
        final CharMatcher r14 = CharMatcher.inRange('A', 'Z');
        final String r15 = "UPPER_CAMEL";
        UPPER_CAMEL = new C05314(r15, r13, r14, r12);
        final int r16 = 4;
        final CharMatcher r17 = CharMatcher.m181is('_');
        final String r18 = "UPPER_UNDERSCORE";
        UPPER_UNDERSCORE = new C05325(r18, r16, r17, r8);
        $VALUES = $values();
    }

    private CaseFormat(String r1, int r2, CharMatcher r3, String r4) {
        super(r1, r2);
        this.wordBoundary = r3;
        this.wordSeparator = r4;
    }

    /* synthetic */ CaseFormat(String r1, int r2, CharMatcher r3, String r4, C05281 r5) {
        this(r1, r2, r3, r4);
    }

    static /* synthetic */ String access$100(String r1) {
        return firstCharOnlyToUpper(r1);
    }

    private static String firstCharOnlyToUpper(String r2) {
        if (r2.isEmpty() == false) goto L6;
        return r2;
    L6:
        return Ascii.toUpperCase(r2.charAt(0)) + Ascii.toLowerCase(r2.substring(1));
    }

    public static CaseFormat valueOf(String r1) {
        return (CaseFormat) Enum.valueOf(CaseFormat.class, r1);
    }

    public static CaseFormat[] values() {
        return (CaseFormat[]) $VALUES.clone();
    }

    String convert(CaseFormat r7, String r8) {
        StringBuilder r0 = null;
        int r1 = 0;
        int r2 = -1;
    L3:
        int r3 = this.wordBoundary.indexIn(r8, r2 + 1);
        r2 = r3;
        if (r3 == (-1)) goto L9;
        if (r1 != 0) goto L7;
        StringBuilder r4 = new StringBuilder(r8.length() + (r7.wordSeparator.length() * 4));
        r4.append(r7.normalizeFirstWord(r8.substring(r1, r2)));
        r0 = r4;
    L8:
        r0.append(r7.wordSeparator);
        r1 = r2 + this.wordSeparator.length();
        goto L3
    L7:
        ((StringBuilder) java.util.Objects.requireNonNull(r0)).append(r7.normalizeWord(r8.substring(r1, r2)));
        goto L8
    L9:
        if (r1 != 0) goto L12;
        return r7.normalizeFirstWord(r8);
    L12:
        return ((StringBuilder) java.util.Objects.requireNonNull(r0)).append(r7.normalizeWord(r8.substring(r1))).toString();
    }

    public Converter<String, String> converterTo(CaseFormat r2) {
        return new StringConverter(this, r2);
    }

    String normalizeFirstWord(String r2) {
        return normalizeWord(r2);
    }

    abstract String normalizeWord(String r1);

    /* JADX INFO: renamed from: to */
    public final String m180to(CaseFormat r2, String r3) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        if (r2 != this) goto L6;
        return r3;
    L6:
        return convert(r2, r3);
    }
}
