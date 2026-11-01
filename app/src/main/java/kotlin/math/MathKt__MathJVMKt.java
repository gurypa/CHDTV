package kotlin.math;

import androidx.media3.exoplayer.upstream.CmcdData;
import androidx.media3.extractor.text.ttml.TtmlNode;
import kotlin.Metadata;

/* JADX INFO: compiled from: MathJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b8\u001a\u0011\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\tH\u0087\b\u001a\u0011\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\fH\u0087\b\u001a\u0011\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0011\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u001b\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0011\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u001d\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0019\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0019\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u0010 \u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0011\u0010 \u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010!\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010!\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\"\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\"\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010#\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010#\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010$\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010$\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010%\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010%\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010&\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010&\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010'\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010'\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0019\u0010(\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u0001H\u0087\b\u001a\u0019\u0010(\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010)\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010)\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010*\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010*\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0018\u0010+\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u00012\u0006\u0010,\u001a\u00020\u0001H\u0007\u001a\u0018\u0010+\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00062\u0006\u0010,\u001a\u00020\u0006H\u0007\u001a\u0011\u0010-\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010-\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u0010.\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0010\u0010.\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0007\u001a\u0019\u0010/\u001a\u00020\u00012\u0006\u00100\u001a\u00020\u00012\u0006\u00101\u001a\u00020\u0001H\u0087\b\u001a\u0019\u0010/\u001a\u00020\u00062\u0006\u00100\u001a\u00020\u00062\u0006\u00101\u001a\u00020\u0006H\u0087\b\u001a\u0019\u0010/\u001a\u00020\t2\u0006\u00100\u001a\u00020\t2\u0006\u00101\u001a\u00020\tH\u0087\b\u001a\u0019\u0010/\u001a\u00020\f2\u0006\u00100\u001a\u00020\f2\u0006\u00101\u001a\u00020\fH\u0087\b\u001a\u0019\u00102\u001a\u00020\u00012\u0006\u00100\u001a\u00020\u00012\u0006\u00101\u001a\u00020\u0001H\u0087\b\u001a\u0019\u00102\u001a\u00020\u00062\u0006\u00100\u001a\u00020\u00062\u0006\u00101\u001a\u00020\u0006H\u0087\b\u001a\u0019\u00102\u001a\u00020\t2\u0006\u00100\u001a\u00020\t2\u0006\u00101\u001a\u00020\tH\u0087\b\u001a\u0019\u00102\u001a\u00020\f2\u0006\u00100\u001a\u00020\f2\u0006\u00101\u001a\u00020\fH\u0087\b\u001a\u0011\u00103\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00103\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00104\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00104\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00105\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00105\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00106\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00106\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00107\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00107\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00108\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00108\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u00109\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0010\u00109\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0007\u001a\u0015\u0010:\u001a\u00020\u0001*\u00020\u00012\u0006\u0010;\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010:\u001a\u00020\u0006*\u00020\u00062\u0006\u0010;\u001a\u00020\u0006H\u0087\b\u001a\r\u0010<\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010<\u001a\u00020\u0006*\u00020\u0006H\u0087\b\u001a\u0015\u0010=\u001a\u00020\u0001*\u00020\u00012\u0006\u0010>\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010=\u001a\u00020\u0006*\u00020\u00062\u0006\u0010>\u001a\u00020\u0006H\u0087\b\u001a\r\u0010?\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010?\u001a\u00020\u0006*\u00020\u0006H\u0087\b\u001a\u0015\u0010@\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010@\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0018\u001a\u00020\tH\u0087\b\u001a\u0015\u0010@\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0015\u0010@\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0018\u001a\u00020\tH\u0087\b\u001a\f\u0010A\u001a\u00020\t*\u00020\u0001H\u0007\u001a\f\u0010A\u001a\u00020\t*\u00020\u0006H\u0007\u001a\f\u0010B\u001a\u00020\f*\u00020\u0001H\u0007\u001a\f\u0010B\u001a\u00020\f*\u00020\u0006H\u0007\u001a\u0015\u0010C\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010C\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u000f\u001a\u00020\tH\u0087\b\u001a\u0015\u0010C\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0006H\u0087\b\u001a\u0015\u0010C\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u000f\u001a\u00020\tH\u0087\b\"\u001f\u0010\u0000\u001a\u00020\u0001*\u00020\u00018Æ\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005\"\u001f\u0010\u0000\u001a\u00020\u0006*\u00020\u00068Æ\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0002\u0010\u0007\u001a\u0004\b\u0004\u0010\b\"\u001f\u0010\u0000\u001a\u00020\t*\u00020\t8Æ\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0002\u0010\n\u001a\u0004\b\u0004\u0010\u000b\"\u001f\u0010\u0000\u001a\u00020\f*\u00020\f8Æ\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0002\u0010\r\u001a\u0004\b\u0004\u0010\u000e\"\u001f\u0010\u000f\u001a\u00020\u0001*\u00020\u00018Æ\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0010\u0010\u0003\u001a\u0004\b\u0011\u0010\u0005\"\u001f\u0010\u000f\u001a\u00020\u0006*\u00020\u00068Æ\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0010\u0010\u0007\u001a\u0004\b\u0011\u0010\b\"\u001e\u0010\u000f\u001a\u00020\t*\u00020\t8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0010\u0010\n\u001a\u0004\b\u0011\u0010\u000b\"\u001e\u0010\u000f\u001a\u00020\t*\u00020\f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0010\u0010\r\u001a\u0004\b\u0011\u0010\u0012\"\u001f\u0010\u0013\u001a\u00020\u0001*\u00020\u00018Æ\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0014\u0010\u0003\u001a\u0004\b\u0015\u0010\u0005\"\u001f\u0010\u0013\u001a\u00020\u0006*\u00020\u00068Æ\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0014\u0010\u0007\u001a\u0004\b\u0015\u0010\b¨\u0006D"}, m421d2 = {"absoluteValue", "", "getAbsoluteValue$annotations", "(D)V", "getAbsoluteValue", "(D)D", "", "(F)V", "(F)F", "", "(I)V", "(I)I", "", "(J)V", "(J)J", "sign", "getSign$annotations", "getSign", "(J)I", "ulp", "getUlp$annotations", "getUlp", "abs", "x", "n", "acos", "acosh", "asin", "asinh", "atan", "atan2", "y", "atanh", "cbrt", "ceil", "cos", "cosh", "exp", "expm1", "floor", "hypot", "ln", "ln1p", "log", TtmlNode.RUBY_BASE, "log10", "log2", "max", CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY, "b", "min", "round", "sin", "sinh", "sqrt", "tan", "tanh", "truncate", "IEEErem", "divisor", "nextDown", "nextTowards", "to", "nextUp", "pow", "roundToInt", "roundToLong", "withSign", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/math/MathKt")
class MathKt__MathJVMKt extends MathKt__MathHKt {
    public MathKt__MathJVMKt() {
    }

    private static final double IEEErem(double r2, double r4) {
        return Math.IEEEremainder(r2, r4);
    }

    private static final float IEEErem(float r4, float r5) {
        return (float) Math.IEEEremainder(r4, r5);
    }

    private static final double abs(double r2) {
        return Math.abs(r2);
    }

    private static final float abs(float r1) {
        return Math.abs(r1);
    }

    private static final int abs(int r1) {
        return Math.abs(r1);
    }

    private static final long abs(long r2) {
        return Math.abs(r2);
    }

    private static final double acos(double r2) {
        return Math.acos(r2);
    }

    private static final float acos(float r2) {
        return (float) Math.acos(r2);
    }

    public static final double acosh(double r8) {
        if (r8 >= 1.0d) goto L6;
        return Double.NaN;
    L6:
        if (r8 > Constants.upper_taylor_2_bound) goto L17;
        double r0 = 1;
        if ((r8 - r0) >= Constants.taylor_n_bound) goto L18;
        double r1 = Math.sqrt(r8 - r0);
        double r2 = r1;
        if (r1 < Constants.taylor_2_bound) goto L15;
        r2 = r2 - (((r1 * r1) * r1) / ((double) 12));
    L15:
        return Math.sqrt(2.0d) * r2;
    L18:
        return Math.log(Math.sqrt((r8 * r8) - r0) + r8);
    L17:
        return Math.log(r8) + Constants.LN2;
    }

    private static final float acosh(float r2) {
        return (float) MathKt.acosh(r2);
    }

    private static final double asin(double r2) {
        return Math.asin(r2);
    }

    private static final float asin(float r2) {
        return (float) Math.asin(r2);
    }

    public static final double asinh(double r6) {
        if (r6 < Constants.taylor_n_bound) goto L12;
        if (r6 <= Constants.upper_taylor_n_bound) goto L20;
        if (r6 > Constants.upper_taylor_2_bound) goto L18;
        double r2 = 2;
        return Math.log((r6 * r2) + (((double) 1) / (r2 * r6)));
    L18:
        return Math.log(r6) + Constants.LN2;
    L20:
        return Math.log(Math.sqrt((r6 * r6) + ((double) 1)) + r6);
    L12:
        if (r6 > (-Constants.taylor_n_bound)) goto L15;
        return -MathKt.asinh(-r6);
    L15:
        if (Math.abs(r6) >= Constants.taylor_2_bound) goto L22;
        return r6;
    L22:
        return r6 - (((r6 * r6) * r6) / ((double) 6));
    }

    private static final float asinh(float r2) {
        return (float) MathKt.asinh(r2);
    }

    private static final double atan(double r2) {
        return Math.atan(r2);
    }

    private static final float atan(float r2) {
        return (float) Math.atan(r2);
    }

    private static final double atan2(double r2, double r4) {
        return Math.atan2(r2, r4);
    }

    private static final float atan2(float r4, float r5) {
        return (float) Math.atan2(r4, r5);
    }

    public static final double atanh(double r6) {
        if (Math.abs(r6) < Constants.taylor_n_bound) goto L5;
        double r0 = 1;
        return Math.log((r0 + r6) / (r0 - r6)) / ((double) 2);
    L5:
        if (Math.abs(r6) > Constants.taylor_2_bound) goto L7;
        return r6;
    L7:
        return r6 + (((r6 * r6) * r6) / ((double) 3));
    }

    private static final float atanh(float r2) {
        return (float) MathKt.atanh(r2);
    }

    private static final double cbrt(double r2) {
        return Math.cbrt(r2);
    }

    private static final float cbrt(float r2) {
        return (float) Math.cbrt(r2);
    }

    private static final double ceil(double r2) {
        return Math.ceil(r2);
    }

    private static final float ceil(float r2) {
        return (float) Math.ceil(r2);
    }

    private static final double cos(double r2) {
        return Math.cos(r2);
    }

    private static final float cos(float r2) {
        return (float) Math.cos(r2);
    }

    private static final double cosh(double r2) {
        return Math.cosh(r2);
    }

    private static final float cosh(float r2) {
        return (float) Math.cosh(r2);
    }

    private static final double exp(double r2) {
        return Math.exp(r2);
    }

    private static final float exp(float r2) {
        return (float) Math.exp(r2);
    }

    private static final double expm1(double r2) {
        return Math.expm1(r2);
    }

    private static final float expm1(float r2) {
        return (float) Math.expm1(r2);
    }

    private static final double floor(double r2) {
        return Math.floor(r2);
    }

    private static final float floor(float r2) {
        return (float) Math.floor(r2);
    }

    private static final double getAbsoluteValue(double r2) {
        return Math.abs(r2);
    }

    private static final float getAbsoluteValue(float r1) {
        return Math.abs(r1);
    }

    private static final int getAbsoluteValue(int r1) {
        return Math.abs(r1);
    }

    private static final long getAbsoluteValue(long r2) {
        return Math.abs(r2);
    }

    public static /* synthetic */ void getAbsoluteValue$annotations(double r0) {
    }

    public static /* synthetic */ void getAbsoluteValue$annotations(float r0) {
    }

    public static /* synthetic */ void getAbsoluteValue$annotations(int r0) {
    }

    public static /* synthetic */ void getAbsoluteValue$annotations(long r0) {
    }

    private static final double getSign(double r2) {
        return Math.signum(r2);
    }

    private static final float getSign(float r1) {
        return Math.signum(r1);
    }

    public static final int getSign(int r1) {
        if (r1 >= 0) goto L4;
        return -1;
    L4:
        if (r1 <= 0) goto L6;
        return 1;
    L6:
        return 0;
    }

    public static final int getSign(long r3) {
        if (r3 >= 0) goto L6;
        return -1;
    L6:
        if (r3 <= 0) goto L8;
        return 1;
    L8:
        return 0;
    }

    public static /* synthetic */ void getSign$annotations(double r0) {
    }

    public static /* synthetic */ void getSign$annotations(float r0) {
    }

    public static /* synthetic */ void getSign$annotations(int r0) {
    }

    public static /* synthetic */ void getSign$annotations(long r0) {
    }

    private static final double getUlp(double r2) {
        return Math.ulp(r2);
    }

    private static final float getUlp(float r1) {
        return Math.ulp(r1);
    }

    public static /* synthetic */ void getUlp$annotations(double r0) {
    }

    public static /* synthetic */ void getUlp$annotations(float r0) {
    }

    private static final double hypot(double r2, double r4) {
        return Math.hypot(r2, r4);
    }

    private static final float hypot(float r4, float r5) {
        return (float) Math.hypot(r4, r5);
    }

    /* JADX INFO: renamed from: ln */
    private static final double m447ln(double r2) {
        return Math.log(r2);
    }

    /* JADX INFO: renamed from: ln */
    private static final float m448ln(float r2) {
        return (float) Math.log(r2);
    }

    private static final double ln1p(double r2) {
        return Math.log1p(r2);
    }

    private static final float ln1p(float r2) {
        return (float) Math.log1p(r2);
    }

    public static final double log(double r4, double r6) {
        if (r6 > 0.0d) goto L5;
        return Double.NaN;
    L5:
        if (r6 != 1.0d) goto L7;
        boolean r0 = true;
    L8:
        if (r0 == false) goto L11;
        return Double.NaN;
    L11:
        return Math.log(r4) / Math.log(r6);
    L7:
        r0 = false;
        goto L8
    }

    public static final float log(float r4, float r5) {
        if (r5 > 0.0f) goto L5;
        return Float.NaN;
    L5:
        if (r5 != 1.0f) goto L7;
        boolean r0 = true;
    L8:
        if (r0 == false) goto L11;
        return Float.NaN;
    L11:
        return (float) (Math.log(r4) / Math.log(r5));
    L7:
        r0 = false;
        goto L8
    }

    private static final double log10(double r2) {
        return Math.log10(r2);
    }

    private static final float log10(float r2) {
        return (float) Math.log10(r2);
    }

    public static final double log2(double r4) {
        return Math.log(r4) / Constants.LN2;
    }

    public static final float log2(float r4) {
        return (float) (Math.log(r4) / Constants.LN2);
    }

    private static final double max(double r2, double r4) {
        return Math.max(r2, r4);
    }

    private static final float max(float r1, float r2) {
        return Math.max(r1, r2);
    }

    private static final int max(int r1, int r2) {
        return Math.max(r1, r2);
    }

    private static final long max(long r2, long r4) {
        return Math.max(r2, r4);
    }

    private static final double min(double r2, double r4) {
        return Math.min(r2, r4);
    }

    private static final float min(float r1, float r2) {
        return Math.min(r1, r2);
    }

    private static final int min(int r1, int r2) {
        return Math.min(r1, r2);
    }

    private static final long min(long r2, long r4) {
        return Math.min(r2, r4);
    }

    private static final double nextDown(double r2) {
        return Math.nextAfter(r2, Double.NEGATIVE_INFINITY);
    }

    private static final float nextDown(float r2) {
        return Math.nextAfter(r2, Double.NEGATIVE_INFINITY);
    }

    private static final double nextTowards(double r2, double r4) {
        return Math.nextAfter(r2, r4);
    }

    private static final float nextTowards(float r2, float r3) {
        return Math.nextAfter(r2, r3);
    }

    private static final double nextUp(double r2) {
        return Math.nextUp(r2);
    }

    private static final float nextUp(float r1) {
        return Math.nextUp(r1);
    }

    private static final double pow(double r2, double r4) {
        return Math.pow(r2, r4);
    }

    private static final double pow(double r2, int r4) {
        return Math.pow(r2, r4);
    }

    private static final float pow(float r4, float r5) {
        return (float) Math.pow(r4, r5);
    }

    private static final float pow(float r4, int r5) {
        return (float) Math.pow(r4, r5);
    }

    private static final double round(double r2) {
        return Math.rint(r2);
    }

    private static final float round(float r2) {
        return (float) Math.rint(r2);
    }

    public static final int roundToInt(double r2) {
        if (Double.isNaN(r2) == true) goto L13;
        if (r2 <= 2.147483647E9d) goto L8;
        return Integer.MAX_VALUE;
    L8:
        if (r2 >= (-2.147483648E9d)) goto L11;
        return Integer.MIN_VALUE;
    L11:
        return (int) Math.round(r2);
    L13:
        throw new IllegalArgumentException("Cannot round NaN value.");
    }

    public static final int roundToInt(float r2) {
        if (Float.isNaN(r2) == true) goto L7;
        return Math.round(r2);
    L7:
        throw new IllegalArgumentException("Cannot round NaN value.");
    }

    public static final long roundToLong(double r2) {
        if (Double.isNaN(r2) == true) goto L7;
        return Math.round(r2);
    L7:
        throw new IllegalArgumentException("Cannot round NaN value.");
    }

    public static final long roundToLong(float r2) {
        return MathKt.roundToLong(r2);
    }

    private static final double sign(double r2) {
        return Math.signum(r2);
    }

    private static final float sign(float r1) {
        return Math.signum(r1);
    }

    private static final double sin(double r2) {
        return Math.sin(r2);
    }

    private static final float sin(float r2) {
        return (float) Math.sin(r2);
    }

    private static final double sinh(double r2) {
        return Math.sinh(r2);
    }

    private static final float sinh(float r2) {
        return (float) Math.sinh(r2);
    }

    private static final double sqrt(double r2) {
        return Math.sqrt(r2);
    }

    private static final float sqrt(float r2) {
        return (float) Math.sqrt(r2);
    }

    private static final double tan(double r2) {
        return Math.tan(r2);
    }

    private static final float tan(float r2) {
        return (float) Math.tan(r2);
    }

    private static final double tanh(double r2) {
        return Math.tanh(r2);
    }

    private static final float tanh(float r2) {
        return (float) Math.tanh(r2);
    }

    public static final double truncate(double r2) {
        if (Double.isNaN(r2) == true) goto L12;
        if (Double.isInfinite(r2) == true) goto L12;
        if (r2 <= 0.0d) goto L14;
        return Math.floor(r2);
    L14:
        return Math.ceil(r2);
    L12:
        return r2;
    }

    public static final float truncate(float r2) {
        if (Float.isNaN(r2) == true) goto L12;
        if (Float.isInfinite(r2) == true) goto L12;
        if (r2 <= 0.0f) goto L14;
        return (float) Math.floor(r2);
    L14:
        return (float) Math.ceil(r2);
    L12:
        return r2;
    }

    private static final double withSign(double r2, double r4) {
        return Math.copySign(r2, r4);
    }

    private static final double withSign(double r2, int r4) {
        return Math.copySign(r2, r4);
    }

    private static final float withSign(float r1, float r2) {
        return Math.copySign(r1, r2);
    }

    private static final float withSign(float r1, int r2) {
        return Math.copySign(r1, r2);
    }
}
