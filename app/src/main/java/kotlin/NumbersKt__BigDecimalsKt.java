package kotlin;

import androidx.media3.extractor.text.ttml.TtmlNode;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: BigDecimals.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0002\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\nH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\rH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\r2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\u000eH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\u000fH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\u0010\u001a\u00020\u0001*\u00020\u0001H\u0087\n¨\u0006\u0011"}, m421d2 = {"dec", "Ljava/math/BigDecimal;", TtmlNode.TAG_DIV, "other", "inc", "minus", "plus", "rem", "times", "toBigDecimal", "", "mathContext", "Ljava/math/MathContext;", "", "", "", "unaryMinus", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/NumbersKt")
class NumbersKt__BigDecimalsKt {
    public NumbersKt__BigDecimalsKt() {
    }

    private static final BigDecimal dec(BigDecimal r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        BigDecimal r0 = r2.subtract(BigDecimal.ONE);
        Intrinsics.checkNotNullExpressionValue(r0, "subtract(...)");
        return r0;
    }

    private static final BigDecimal div(BigDecimal r2, BigDecimal r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        BigDecimal r0 = r2.divide(r3, RoundingMode.HALF_EVEN);
        Intrinsics.checkNotNullExpressionValue(r0, "divide(...)");
        return r0;
    }

    private static final BigDecimal inc(BigDecimal r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        BigDecimal r0 = r2.add(BigDecimal.ONE);
        Intrinsics.checkNotNullExpressionValue(r0, "add(...)");
        return r0;
    }

    private static final BigDecimal minus(BigDecimal r2, BigDecimal r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        BigDecimal r0 = r2.subtract(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "subtract(...)");
        return r0;
    }

    private static final BigDecimal plus(BigDecimal r2, BigDecimal r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        BigDecimal r0 = r2.add(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "add(...)");
        return r0;
    }

    private static final BigDecimal rem(BigDecimal r2, BigDecimal r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        BigDecimal r0 = r2.remainder(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "remainder(...)");
        return r0;
    }

    private static final BigDecimal times(BigDecimal r2, BigDecimal r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        BigDecimal r0 = r2.multiply(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "multiply(...)");
        return r0;
    }

    private static final BigDecimal toBigDecimal(double r2) {
        return new BigDecimal(String.valueOf(r2));
    }

    private static final BigDecimal toBigDecimal(double r2, MathContext r4) {
        Intrinsics.checkNotNullParameter(r4, "mathContext");
        return new BigDecimal(String.valueOf(r2), r4);
    }

    private static final BigDecimal toBigDecimal(float r2) {
        return new BigDecimal(String.valueOf(r2));
    }

    private static final BigDecimal toBigDecimal(float r2, MathContext r3) {
        Intrinsics.checkNotNullParameter(r3, "mathContext");
        return new BigDecimal(String.valueOf(r2), r3);
    }

    private static final BigDecimal toBigDecimal(int r2) {
        BigDecimal r0 = BigDecimal.valueOf(r2);
        Intrinsics.checkNotNullExpressionValue(r0, "valueOf(...)");
        return r0;
    }

    private static final BigDecimal toBigDecimal(int r1, MathContext r2) {
        Intrinsics.checkNotNullParameter(r2, "mathContext");
        return new BigDecimal(r1, r2);
    }

    private static final BigDecimal toBigDecimal(long r2) {
        BigDecimal r0 = BigDecimal.valueOf(r2);
        Intrinsics.checkNotNullExpressionValue(r0, "valueOf(...)");
        return r0;
    }

    private static final BigDecimal toBigDecimal(long r1, MathContext r3) {
        Intrinsics.checkNotNullParameter(r3, "mathContext");
        return new BigDecimal(r1, r3);
    }

    private static final BigDecimal unaryMinus(BigDecimal r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        BigDecimal r0 = r2.negate();
        Intrinsics.checkNotNullExpressionValue(r0, "negate(...)");
        return r0;
    }
}
