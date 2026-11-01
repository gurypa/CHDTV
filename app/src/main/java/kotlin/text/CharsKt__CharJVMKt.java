package kotlin.text;

import java.util.Locale;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

/* JADX INFO: compiled from: CharJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00004\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0001\u001a\u0018\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\nH\u0000\u001a\r\u0010\u000e\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0010\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0011\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0012\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0013\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0014\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0016\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0018\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0019\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u001a\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u001b\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\n\u0010\u001c\u001a\u00020\u000f*\u00020\u0002\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0014\u0010\u001d\u001a\u00020\u001e*\u00020\u00022\u0006\u0010\u001f\u001a\u00020 H\u0007\u001a\r\u0010!\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0014\u0010\"\u001a\u00020\u001e*\u00020\u00022\u0006\u0010\u001f\u001a\u00020 H\u0007\u001a\r\u0010#\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010$\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010%\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010&\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010'\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0014\u0010'\u001a\u00020\u001e*\u00020\u00022\u0006\u0010\u001f\u001a\u00020 H\u0007\u001a\r\u0010(\u001a\u00020\u0002*\u00020\u0002H\u0087\b\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006)"}, m421d2 = {"category", "Lkotlin/text/CharCategory;", "", "getCategory", "(C)Lkotlin/text/CharCategory;", "directionality", "Lkotlin/text/CharDirectionality;", "getDirectionality", "(C)Lkotlin/text/CharDirectionality;", "checkRadix", "", "radix", "digitOf", "char", "isDefined", "", "isDigit", "isHighSurrogate", "isISOControl", "isIdentifierIgnorable", "isJavaIdentifierPart", "isJavaIdentifierStart", "isLetter", "isLetterOrDigit", "isLowSurrogate", "isLowerCase", "isTitleCase", "isUpperCase", "isWhitespace", "lowercase", "", "locale", "Ljava/util/Locale;", "lowercaseChar", "titlecase", "titlecaseChar", "toLowerCase", "toTitleCase", "toUpperCase", "uppercase", "uppercaseChar", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/text/CharsKt")
class CharsKt__CharJVMKt {
    public CharsKt__CharJVMKt() {
    }

    public static final int checkRadix(int r5) {
        if (new IntRange(2, 36).contains(r5) == false) goto L6;
        return r5;
    L6:
        throw new IllegalArgumentException("radix " + r5 + " was not in valid range " + new IntRange(2, 36));
    }

    public static final int digitOf(char r1, int r2) {
        return Character.digit(r1, r2);
    }

    public static final CharCategory getCategory(char r2) {
        return CharCategory.Companion.valueOf(Character.getType(r2));
    }

    public static final CharDirectionality getDirectionality(char r2) {
        return CharDirectionality.Companion.valueOf(Character.getDirectionality(r2));
    }

    private static final boolean isDefined(char r1) {
        return Character.isDefined(r1);
    }

    private static final boolean isDigit(char r1) {
        return Character.isDigit(r1);
    }

    private static final boolean isHighSurrogate(char r1) {
        return Character.isHighSurrogate(r1);
    }

    private static final boolean isISOControl(char r1) {
        return Character.isISOControl(r1);
    }

    private static final boolean isIdentifierIgnorable(char r1) {
        return Character.isIdentifierIgnorable(r1);
    }

    private static final boolean isJavaIdentifierPart(char r1) {
        return Character.isJavaIdentifierPart(r1);
    }

    private static final boolean isJavaIdentifierStart(char r1) {
        return Character.isJavaIdentifierStart(r1);
    }

    private static final boolean isLetter(char r1) {
        return Character.isLetter(r1);
    }

    private static final boolean isLetterOrDigit(char r1) {
        return Character.isLetterOrDigit(r1);
    }

    private static final boolean isLowSurrogate(char r1) {
        return Character.isLowSurrogate(r1);
    }

    private static final boolean isLowerCase(char r1) {
        return Character.isLowerCase(r1);
    }

    private static final boolean isTitleCase(char r1) {
        return Character.isTitleCase(r1);
    }

    private static final boolean isUpperCase(char r1) {
        return Character.isUpperCase(r1);
    }

    public static final boolean isWhitespace(char r1) {
        if (Character.isWhitespace(r1) == false) goto L5;
    L8:
        return true;
    L5:
        if (Character.isSpaceChar(r1) == true) goto L8;
        return false;
    }

    private static final String lowercase(char r2) {
        String r0 = String.valueOf(r2);
        Intrinsics.checkNotNull(r0, "null cannot be cast to non-null type java.lang.String");
        String r1 = r0.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(r1, "toLowerCase(...)");
        return r1;
    }

    public static final String lowercase(char r2, Locale r3) {
        Intrinsics.checkNotNullParameter(r3, "locale");
        String r0 = String.valueOf(r2);
        Intrinsics.checkNotNull(r0, "null cannot be cast to non-null type java.lang.String");
        String r1 = r0.toLowerCase(r3);
        Intrinsics.checkNotNullExpressionValue(r1, "toLowerCase(...)");
        return r1;
    }

    private static final char lowercaseChar(char r1) {
        return Character.toLowerCase(r1);
    }

    public static final String titlecase(char r5, Locale r6) {
        Intrinsics.checkNotNullParameter(r6, "locale");
        String r0 = CharsKt.uppercase(r5, r6);
        if (r0.length() > 1) goto L5;
        String r1 = String.valueOf(r5);
        Intrinsics.checkNotNull(r1, "null cannot be cast to non-null type java.lang.String");
        String r2 = r1.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(r2, "toUpperCase(...)");
        if (Intrinsics.areEqual(r0, r2) == true) goto L13;
        return r0;
    L13:
        return String.valueOf(Character.toTitleCase(r5));
    L5:
        if (r5 == 329) goto L14;
        char r3 = r0.charAt(0);
        Intrinsics.checkNotNull(r0, "null cannot be cast to non-null type java.lang.String");
        String r4 = r0.substring(1);
        Intrinsics.checkNotNullExpressionValue(r4, "substring(...)");
        Intrinsics.checkNotNull(r4, "null cannot be cast to non-null type java.lang.String");
        String r7 = r4.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(r7, "toLowerCase(...)");
        return r3 + r7;
    L14:
        return r0;
    }

    private static final char titlecaseChar(char r1) {
        return Character.toTitleCase(r1);
    }

    @Deprecated(message = "Use lowercaseChar() instead.", replaceWith = @ReplaceWith(expression = "lowercaseChar()", imports = {}))
    @DeprecatedSinceKotlin(warningSince = "1.5")
    private static final char toLowerCase(char r1) {
        return Character.toLowerCase(r1);
    }

    @Deprecated(message = "Use titlecaseChar() instead.", replaceWith = @ReplaceWith(expression = "titlecaseChar()", imports = {}))
    @DeprecatedSinceKotlin(warningSince = "1.5")
    private static final char toTitleCase(char r1) {
        return Character.toTitleCase(r1);
    }

    @Deprecated(message = "Use uppercaseChar() instead.", replaceWith = @ReplaceWith(expression = "uppercaseChar()", imports = {}))
    @DeprecatedSinceKotlin(warningSince = "1.5")
    private static final char toUpperCase(char r1) {
        return Character.toUpperCase(r1);
    }

    private static final String uppercase(char r2) {
        String r0 = String.valueOf(r2);
        Intrinsics.checkNotNull(r0, "null cannot be cast to non-null type java.lang.String");
        String r1 = r0.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(r1, "toUpperCase(...)");
        return r1;
    }

    public static final String uppercase(char r2, Locale r3) {
        Intrinsics.checkNotNullParameter(r3, "locale");
        String r0 = String.valueOf(r2);
        Intrinsics.checkNotNull(r0, "null cannot be cast to non-null type java.lang.String");
        String r1 = r0.toUpperCase(r3);
        Intrinsics.checkNotNullExpressionValue(r1, "toUpperCase(...)");
        return r1;
    }

    private static final char uppercaseChar(char r1) {
        return Character.toUpperCase(r1);
    }
}
