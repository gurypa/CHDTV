package kotlin.text;

import com.arthenica.ffmpegkit.StreamInformation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

/* JADX INFO: compiled from: _StringsJvm.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000B\n\u0000\n\u0002\u0010\f\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0001*\u00020\u0002H\u0007¢\u0006\u0002\u0010\u0006\u001a;\u0010\u0007\u001a\u0004\u0018\u00010\u0001\"\u000e\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\t*\u00020\u00022\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\b0\u000bH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\f\u001a/\u0010\r\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u001a\u0010\u000e\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00010\u000fj\n\u0012\u0006\b\u0000\u0012\u00020\u0001`\u0010H\u0007¢\u0006\u0002\u0010\u0011\u001a\u0013\u0010\u0012\u001a\u0004\u0018\u00010\u0001*\u00020\u0002H\u0007¢\u0006\u0002\u0010\u0006\u001a;\u0010\u0013\u001a\u0004\u0018\u00010\u0001\"\u000e\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\t*\u00020\u00022\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\b0\u000bH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\f\u001a/\u0010\u0014\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u001a\u0010\u000e\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00010\u000fj\n\u0012\u0006\b\u0000\u0012\u00020\u0001`\u0010H\u0007¢\u0006\u0002\u0010\u0011\u001a)\u0010\u0015\u001a\u00020\u0016*\u00020\u00022\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u000bH\u0087\bø\u0001\u0000¢\u0006\u0002\b\u0017\u001a)\u0010\u0015\u001a\u00020\u0018*\u00020\u00022\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00180\u000bH\u0087\bø\u0001\u0000¢\u0006\u0002\b\u0019\u001a\u0010\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\u001b*\u00020\u0002\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u001c"}, m421d2 = {"elementAt", "", "", StreamInformation.KEY_INDEX, "", "max", "(Ljava/lang/CharSequence;)Ljava/lang/Character;", "maxBy", "R", "", "selector", "Lkotlin/Function1;", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/Character;", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/lang/CharSequence;Ljava/util/Comparator;)Ljava/lang/Character;", "min", "minBy", "minWith", "sumOf", "Ljava/math/BigDecimal;", "sumOfBigDecimal", "Ljava/math/BigInteger;", "sumOfBigInteger", "toSortedSet", "Ljava/util/SortedSet;", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/text/StringsKt")
class StringsKt___StringsJvmKt extends StringsKt__StringsKt {
    public StringsKt___StringsJvmKt() {
    }

    private static final char elementAt(CharSequence r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.charAt(r2);
    }

    @Deprecated(message = "Use maxOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ Character max(CharSequence r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return StringsKt.maxOrNull(r1);
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [kotlin.collections.IntIterator] */
    @Deprecated(message = "Use maxByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ <R extends Comparable<? super R>> Character maxBy(CharSequence r11, Function1<? super Character, ? extends R> r12) {
        Intrinsics.checkNotNullParameter(r11, "<this>");
        Intrinsics.checkNotNullParameter(r12, "selector");
        if (r11.length() != 0) goto L5;
        boolean r3 = true;
    L6:
        if (r3 == false) goto L8;
        return null;
    L8:
        char r4 = r11.charAt(0);
        int r5 = StringsKt.getLastIndex(r11);
        if (r5 == 0) goto L25;
        R r6 = r12.invoke(Character.valueOf(r4));
        ?? r7 = new IntRange(1, r5).iterator();
    L13:
        if (r7.hasNext() == false) goto L18;
        char r8 = r11.charAt(r7.nextInt());
        R r9 = r12.invoke(Character.valueOf(r8));
        if (r6.compareTo(r9) >= 0) goto L13;
        r4 = r8;
        r6 = r9;
        goto L13
    L18:
        return Character.valueOf(r4);
    L25:
        return Character.valueOf(r4);
    L5:
        r3 = false;
        goto L6
    }

    @Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ Character maxWith(CharSequence r1, Comparator r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "comparator");
        return StringsKt.maxWithOrNull(r1, r2);
    }

    @Deprecated(message = "Use minOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ Character min(CharSequence r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return StringsKt.minOrNull(r1);
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [kotlin.collections.IntIterator] */
    @Deprecated(message = "Use minByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ <R extends Comparable<? super R>> Character minBy(CharSequence r11, Function1<? super Character, ? extends R> r12) {
        Intrinsics.checkNotNullParameter(r11, "<this>");
        Intrinsics.checkNotNullParameter(r12, "selector");
        if (r11.length() != 0) goto L5;
        boolean r3 = true;
    L6:
        if (r3 == false) goto L8;
        return null;
    L8:
        char r4 = r11.charAt(0);
        int r5 = StringsKt.getLastIndex(r11);
        if (r5 == 0) goto L25;
        R r6 = r12.invoke(Character.valueOf(r4));
        ?? r7 = new IntRange(1, r5).iterator();
    L13:
        if (r7.hasNext() == false) goto L18;
        char r8 = r11.charAt(r7.nextInt());
        R r9 = r12.invoke(Character.valueOf(r8));
        if (r6.compareTo(r9) <= 0) goto L13;
        r4 = r8;
        r6 = r9;
        goto L13
    L18:
        return Character.valueOf(r4);
    L25:
        return Character.valueOf(r4);
    L5:
        r3 = false;
        goto L6
    }

    @Deprecated(message = "Use minWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ Character minWith(CharSequence r1, Comparator r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "comparator");
        return StringsKt.minWithOrNull(r1, r2);
    }

    private static final BigDecimal sumOfBigDecimal(CharSequence r5, Function1<? super Character, ? extends BigDecimal> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "selector");
        BigDecimal r0 = BigDecimal.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(r0, "valueOf(...)");
        int r1 = 0;
    L4:
        if (r1 >= r5.length()) goto L6;
        BigDecimal r3 = r0.add(r6.invoke(Character.valueOf(r5.charAt(r1))));
        Intrinsics.checkNotNullExpressionValue(r3, "add(...)");
        r0 = r3;
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    private static final BigInteger sumOfBigInteger(CharSequence r5, Function1<? super Character, ? extends BigInteger> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "selector");
        BigInteger r0 = BigInteger.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(r0, "valueOf(...)");
        int r1 = 0;
    L4:
        if (r1 >= r5.length()) goto L6;
        BigInteger r3 = r0.add(r6.invoke(Character.valueOf(r5.charAt(r1))));
        Intrinsics.checkNotNullExpressionValue(r3, "add(...)");
        r0 = r3;
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    public static final SortedSet<Character> toSortedSet(CharSequence r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return (SortedSet) StringsKt.toCollection(r1, new TreeSet());
    }
}
