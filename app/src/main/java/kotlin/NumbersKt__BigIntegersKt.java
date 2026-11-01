package kotlin;

import androidx.media3.extractor.text.ttml.TtmlNode;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: BigIntegers.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0004\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\r\u0010\u0006\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u000b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0087\f\u001a\u0015\u0010\u000e\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0087\f\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0010\u001a\u00020\u0011*\u00020\u0001H\u0087\b\u001a!\u0010\u0010\u001a\u00020\u0011*\u00020\u00012\b\b\u0002\u0010\u0012\u001a\u00020\r2\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\rH\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\u0016H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0018\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f¨\u0006\u0019"}, m421d2 = {"and", "Ljava/math/BigInteger;", "other", "dec", TtmlNode.TAG_DIV, "inc", "inv", "minus", "or", "plus", "rem", "shl", "n", "", "shr", "times", "toBigDecimal", "Ljava/math/BigDecimal;", "scale", "mathContext", "Ljava/math/MathContext;", "toBigInteger", "", "unaryMinus", "xor", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/NumbersKt")
class NumbersKt__BigIntegersKt extends NumbersKt__BigDecimalsKt {
    public NumbersKt__BigIntegersKt() {
    }

    private static final BigInteger and(BigInteger r2, BigInteger r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        BigInteger r0 = r2.and(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "and(...)");
        return r0;
    }

    private static final BigInteger dec(BigInteger r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        BigInteger r0 = r2.subtract(BigInteger.ONE);
        Intrinsics.checkNotNullExpressionValue(r0, "subtract(...)");
        return r0;
    }

    private static final BigInteger div(BigInteger r2, BigInteger r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        BigInteger r0 = r2.divide(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "divide(...)");
        return r0;
    }

    private static final BigInteger inc(BigInteger r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        BigInteger r0 = r2.add(BigInteger.ONE);
        Intrinsics.checkNotNullExpressionValue(r0, "add(...)");
        return r0;
    }

    private static final BigInteger inv(BigInteger r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        BigInteger r0 = r2.not();
        Intrinsics.checkNotNullExpressionValue(r0, "not(...)");
        return r0;
    }

    private static final BigInteger minus(BigInteger r2, BigInteger r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        BigInteger r0 = r2.subtract(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "subtract(...)");
        return r0;
    }

    /* JADX INFO: renamed from: or */
    private static final BigInteger m427or(BigInteger r2, BigInteger r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        BigInteger r0 = r2.or(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "or(...)");
        return r0;
    }

    private static final BigInteger plus(BigInteger r2, BigInteger r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        BigInteger r0 = r2.add(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "add(...)");
        return r0;
    }

    private static final BigInteger rem(BigInteger r2, BigInteger r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        BigInteger r0 = r2.remainder(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "remainder(...)");
        return r0;
    }

    private static final BigInteger shl(BigInteger r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        BigInteger r0 = r2.shiftLeft(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "shiftLeft(...)");
        return r0;
    }

    private static final BigInteger shr(BigInteger r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        BigInteger r0 = r2.shiftRight(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "shiftRight(...)");
        return r0;
    }

    private static final BigInteger times(BigInteger r2, BigInteger r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        BigInteger r0 = r2.multiply(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "multiply(...)");
        return r0;
    }

    private static final BigDecimal toBigDecimal(BigInteger r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new BigDecimal(r1);
    }

    private static final BigDecimal toBigDecimal(BigInteger r1, int r2, MathContext r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r3, "mathContext");
        return new BigDecimal(r1, r2, r3);
    }

    static /* synthetic */ BigDecimal toBigDecimal$default(BigInteger r0, int r1, MathContext r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L6;
        r1 = 0;
    L6:
        if ((r3 & 2) == 0) goto L8;
        MathContext r5 = MathContext.UNLIMITED;
        Intrinsics.checkNotNullExpressionValue(r5, "UNLIMITED");
        r2 = r5;
    L8:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r2, "mathContext");
        return new BigDecimal(r0, r1, r2);
    }

    private static final BigInteger toBigInteger(int r2) {
        BigInteger r0 = BigInteger.valueOf(r2);
        Intrinsics.checkNotNullExpressionValue(r0, "valueOf(...)");
        return r0;
    }

    private static final BigInteger toBigInteger(long r2) {
        BigInteger r0 = BigInteger.valueOf(r2);
        Intrinsics.checkNotNullExpressionValue(r0, "valueOf(...)");
        return r0;
    }

    private static final BigInteger unaryMinus(BigInteger r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        BigInteger r0 = r2.negate();
        Intrinsics.checkNotNullExpressionValue(r0, "negate(...)");
        return r0;
    }

    private static final BigInteger xor(BigInteger r2, BigInteger r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        BigInteger r0 = r2.xor(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "xor(...)");
        return r0;
    }
}
