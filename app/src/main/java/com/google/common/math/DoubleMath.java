package com.google.common.math;

import com.google.common.base.Preconditions;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class DoubleMath {
    private static final double LN_2 = 0.0d;
    static final int MAX_FACTORIAL = 170;
    private static final double MAX_INT_AS_DOUBLE = 2.147483647E9d;
    private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18d;
    private static final double MIN_INT_AS_DOUBLE = -2.147483648E9d;
    private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18d;
    static final double[] everySixteenthFactorial = null;

    /* JADX INFO: renamed from: com.google.common.math.DoubleMath$1 */
    static /* synthetic */ class C08591 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = null;

        static {
            $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];
            $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L40:
            $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
        L30:
            $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 3;     // Catch: NoSuchFieldError -> L11
        L28:
            $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 4;     // Catch: NoSuchFieldError -> L14
        L32:
            $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 5;     // Catch: NoSuchFieldError -> L17
        L36:
            $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 6;     // Catch: NoSuchFieldError -> L20
        L42:
            $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;     // Catch: NoSuchFieldError -> L23
        L38:
            $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 8;     // Catch: NoSuchFieldError -> L26
            return;
        }
    }

    static {
        LN_2 = Math.log(2.0d);
        everySixteenthFactorial = new double[]{1.0d, 2.0922789888E13d, 2.631308369336935E35d, 1.2413915592536073E61d, 1.2688693218588417E89d, 7.156945704626381E118d, 9.916779348709496E149d, 1.974506857221074E182d, 3.856204823625804E215d, 5.5502938327393044E249d, 4.7147236359920616E284d};
    }

    private DoubleMath() {
    }

    private static double checkFinite(double r1) {
        Preconditions.checkArgument(DoubleUtils.isFinite(r1));
        return r1;
    }

    public static double factorial(int r5) {
        MathPreconditions.checkNonNegative("n", r5);
        if (r5 <= MAX_FACTORIAL) goto L6;
        return Double.POSITIVE_INFINITY;
    L6:
        double r0 = 1.0d;
        int r2 = (r5 & (-16)) + 1;
    L7:
        if (r2 > r5) goto L10;
        r0 = r0 * ((double) r2);
        r2 = r2 + 1;
        goto L7
    L10:
        return everySixteenthFactorial[r5 >> 4] * r0;
    }

    public static int fuzzyCompare(double r2, double r4, double r6) {
        if (fuzzyEquals(r2, r4, r6) == false) goto L7;
        return 0;
    L7:
        if (r2 >= r4) goto L11;
        return -1;
    L11:
        if (r2 <= r4) goto L15;
        return 1;
    L15:
        return Boolean.compare(Double.isNaN(r2), Double.isNaN(r4));
    }

    public static boolean fuzzyEquals(double r4, double r6, double r8) {
        MathPreconditions.checkNonNegative("tolerance", r8);
        if (Math.copySign(r4 - r6, 1.0d) > r8) goto L5;
    L12:
        return true;
    L5:
        if (r4 == r6) goto L12;
        if (Double.isNaN(r4) == true) goto L9;
    L11:
        return false;
    L9:
        if (Double.isNaN(r6) == false) goto L11;
        goto L11
    }

    public static boolean isMathematicalInteger(double r2) {
        if (DoubleUtils.isFinite(r2) == true) goto L5;
    L9:
        return false;
    L5:
        if (r2 != 0.0d) goto L7;
    L8:
        return true;
    L7:
        if ((52 - Long.numberOfTrailingZeros(DoubleUtils.getSignificand(r2))) > Math.getExponent(r2)) goto L9;
        goto L8
    }

    public static boolean isPowerOfTwo(double r8) {
        if (r8 > 0.0d) goto L5;
    L10:
        return false;
    L5:
        if (DoubleUtils.isFinite(r8) == false) goto L10;
        long r2 = DoubleUtils.getSignificand(r8);
        if (((r2 - 1) & r2) != 0) goto L11;
        return true;
    L11:
        return false;
    }

    public static double log2(double r4) {
        return Math.log(r4) / LN_2;
    }

    public static int log2(double r9, RoundingMode r11) {
        boolean r1 = false;
        if (r9 > 0.0d) goto L5;
    L7:
        boolean r0 = false;
    L8:
        Preconditions.checkArgument(r0, "x must be positive and finite");
        int r2 = Math.getExponent(r9);
        if (DoubleUtils.isNormal(r9) == true) goto L13;
        return log2(4.503599627370496E15d * r9, r11) - 52;
    L13:
        switch(C08591.$SwitchMap$java$math$RoundingMode[r11.ordinal()]) {
            case 1: goto L27;
            case 2: goto L28;
            case 3: goto L26;
            case 4: goto L23;
            case 5: goto L20;
            case 6: goto L16;
            case 7: goto L16;
            case 8: goto L16;
            default: goto L15;
        };
    L16:
        double r3 = DoubleUtils.scaleNormalize(r9);
        if ((r3 * r3) <= 2.0d) goto L29;
        r1 = true;
    L29:
        if (r1 == false) goto L32;
        return r2 + 1;
    L32:
        return r2;
    L20:
        if (r2 < 0) goto L22;
        r1 = true;
    L22:
        r1 = r1 & (true ^ isPowerOfTwo(r9));
        goto L29
    L23:
        if (r2 >= 0) goto L25;
        r1 = true;
    L25:
        r1 = r1 & (true ^ isPowerOfTwo(r9));
        goto L29
    L26:
        r1 = !isPowerOfTwo(r9);
        goto L29
    L27:
        MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(r9));
    L28:
        r1 = false;
        goto L29
    L15:
        throw new AssertionError();
    L5:
        if (DoubleUtils.isFinite(r9) == false) goto L7;
        r0 = true;
        goto L8
    }

    @Deprecated
    public static double mean(Iterable<? extends Number> r2) {
        return mean(r2.iterator());
    }

    @Deprecated
    public static double mean(Iterator<? extends Number> r10) {
        Preconditions.checkArgument(r10.hasNext(), "Cannot take mean of 0 values");
        long r0 = 1;
        double r2 = checkFinite(r10.next().doubleValue());
    L4:
        if (r10.hasNext() == false) goto L6;
        r0 = r0 + 1;
        r2 = r2 + ((checkFinite(r10.next().doubleValue()) - r2) / r0);
        goto L4
    L6:
        return r2;
    }

    @Deprecated
    public static double mean(double... r9) {
        if (r9.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "Cannot take mean of 0 values");
        long r2 = 1;
        double r1 = checkFinite(r9[0]);
        int r4 = 1;
    L8:
        if (r4 >= r9.length) goto L10;
        checkFinite(r9[r4]);
        r2 = r2 + 1;
        r1 = r1 + ((r9[r4] - r1) / r2);
        r4 = r4 + 1;
        goto L8
    L10:
        return r1;
    L5:
        r0 = false;
        goto L6
    }

    @Deprecated
    public static double mean(int... r6) {
        if (r6.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "Cannot take mean of 0 values");
        long r1 = 0;
        int r2 = 0;
    L8:
        if (r2 >= r6.length) goto L11;
        r1 = r1 + ((long) r6[r2]);
        r2 = r2 + 1;
        goto L8
    L11:
        return r1 / ((double) r6.length);
    L5:
        r0 = false;
        goto L6
    }

    @Deprecated
    public static double mean(long... r9) {
        if (r9.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "Cannot take mean of 0 values");
        long r2 = 1;
        double r1 = r9[0];
        int r4 = 1;
    L8:
        if (r4 >= r9.length) goto L10;
        r2 = r2 + 1;
        r1 = r1 + ((r9[r4] - r1) / r2);
        r4 = r4 + 1;
        goto L8
    L10:
        return r1;
    L5:
        r0 = false;
        goto L6
    }

    static double roundIntermediate(double r7, RoundingMode r9) {
        if (DoubleUtils.isFinite(r7) == false) goto L48;
        switch(C08591.$SwitchMap$java$math$RoundingMode[r9.ordinal()]) {
            case 1: goto L45;
            case 2: goto L38;
            case 3: goto L30;
            case 4: goto L28;
            case 5: goto L20;
            case 6: goto L18;
            case 7: goto L12;
            case 8: goto L8;
            default: goto L7;
        };
    L8:
        double r0 = Math.rint(r7);
        if (Math.abs(r7 - r0) != 0.5d) goto L11;
        return r7;
    L11:
        return r0;
    L12:
        double r1 = Math.rint(r7);
        if (Math.abs(r7 - r1) == 0.5d) goto L15;
        return r1;
    L15:
        return Math.copySign(0.5d, r7) + r7;
    L28:
        return r7;
    L45:
        MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(r7));
        return r7;
    L7:
        throw new AssertionError();
    L18:
        return Math.rint(r7);
    L20:
        if (isMathematicalInteger(r7) == false) goto L22;
        return r7;
    L22:
        long r2 = (long) r7;
        if (r7 <= 0.0d) goto L25;
        int r3 = 1;
    L27:
        return r2 + ((long) r3);
    L25:
        r3 = -1;
        goto L27
    L30:
        if (r7 > 0.0d) goto L32;
    L36:
        return r7;
    L32:
        if (isMathematicalInteger(r7) == true) goto L36;
        return ((long) r7) + 1;
    L38:
        if (r7 < 0.0d) goto L40;
    L44:
        return r7;
    L40:
        if (isMathematicalInteger(r7) == true) goto L44;
        return ((long) r7) - 1;
    L48:
        throw new ArithmeticException("input is infinite or NaN");
    }

    public static BigInteger roundToBigInteger(double r6, RoundingMode r8) {
        double r7 = roundIntermediate(r6, r8);
        boolean r1 = true;
        if ((MIN_LONG_AS_DOUBLE - r7) >= 1.0d) goto L5;
        boolean r0 = true;
    L7:
        if (r7 < MAX_LONG_AS_DOUBLE_PLUS_ONE) goto L11;
        r1 = false;
    L11:
        if ((r0 & r1) == true) goto L13;
        BigInteger r3 = BigInteger.valueOf(DoubleUtils.getSignificand(r7)).shiftLeft(Math.getExponent(r7) - 52);
        if (r7 >= 0.0d) goto L18;
        return r3.negate();
    L18:
        return r3;
    L13:
        return BigInteger.valueOf((long) r7);
    L5:
        r0 = false;
        goto L7
    }

    public static int roundToInt(double r7, RoundingMode r9) {
        double r0 = roundIntermediate(r7, r9);
        boolean r3 = true;
        if (r0 <= (-2.147483649E9d)) goto L5;
        boolean r2 = true;
    L7:
        if (r0 < 2.147483648E9d) goto L10;
        r3 = false;
    L10:
        MathPreconditions.checkInRangeForRoundingInputs(r2 & r3, r7, r9);
        return (int) r0;
    L5:
        r2 = false;
        goto L7
    }

    public static long roundToLong(double r7, RoundingMode r9) {
        double r0 = roundIntermediate(r7, r9);
        boolean r3 = true;
        if ((MIN_LONG_AS_DOUBLE - r0) >= 1.0d) goto L5;
        boolean r2 = true;
    L7:
        if (r0 < MAX_LONG_AS_DOUBLE_PLUS_ONE) goto L10;
        r3 = false;
    L10:
        MathPreconditions.checkInRangeForRoundingInputs(r2 & r3, r7, r9);
        return (long) r0;
    L5:
        r2 = false;
        goto L7
    }
}
