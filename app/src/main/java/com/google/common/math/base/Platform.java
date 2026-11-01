package com.google.common.base;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class Platform {
    private static final PatternCompiler patternCompiler = null;

    /* JADX INFO: renamed from: com.google.common.base.Platform$1 */
    static /* synthetic */ class C05441 {
    }

    private static final class JdkPatternCompiler implements PatternCompiler {
        private JdkPatternCompiler() {
        }

        /* synthetic */ JdkPatternCompiler(C05441 r1) {
            this();
        }

        @Override // com.google.common.base.PatternCompiler
        public CommonPattern compile(String r3) {
            return new JdkPattern(Pattern.compile(r3));
        }

        @Override // com.google.common.base.PatternCompiler
        public boolean isPcreLike() {
            return true;
        }
    }

    static {
        patternCompiler = loadPatternCompiler();
    }

    private Platform() {
    }

    static CommonPattern compilePattern(String r1) {
        Preconditions.checkNotNull(r1);
        return patternCompiler.compile(r1);
    }

    @CheckForNull
    static String emptyToNull(@CheckForNull String r1) {
        if (stringIsNullOrEmpty(r1) == false) goto L6;
        return null;
    L6:
        return r1;
    }

    static String formatCompact4Digits(double r4) {
        return String.format(Locale.ROOT, "%.4g", new Object[]{Double.valueOf(r4)});
    }

    static <T extends Enum<T>> Optional<T> getEnumIfPresent(Class<T> r2, String r3) {
        WeakReference<? extends Enum<?>> r0 = Enums.getEnumConstants(r2).get(r3);
        if (r0 != null) goto L6;
        return Optional.absent();
    L6:
        return Optional.fromNullable(r2.cast(r0.get()));
    }

    private static PatternCompiler loadPatternCompiler() {
        return new JdkPatternCompiler(null);
    }

    static String nullToEmpty(@CheckForNull String r1) {
        if (r1 != null) goto L5;
        return "";
    L5:
        return r1;
    }

    static boolean patternCompilerIsPcreLike() {
        return patternCompiler.isPcreLike();
    }

    static CharMatcher precomputeCharMatcher(CharMatcher r1) {
        return r1.precomputedInternal();
    }

    static boolean stringIsNullOrEmpty(@CheckForNull String r1) {
        if (r1 != null) goto L4;
    L7:
        return true;
    L4:
        if (r1.isEmpty() == true) goto L7;
        return false;
    }
}
