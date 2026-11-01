package com.google.common.math;

import androidx.media3.exoplayer.upstream.CmcdData;
import androidx.media3.extractor.AacUtil;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.math.RoundingMode;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class IntMath {
    static final int FLOOR_SQRT_MAX_INT = 46340;
    static final int MAX_POWER_OF_SQRT2_UNSIGNED = -1257966797;
    static final int MAX_SIGNED_POWER_OF_TWO = 1073741824;
    static int[] biggestBinomials;
    private static final int[] factorials = null;
    static final int[] halfPowersOf10 = null;
    static final byte[] maxLog10ForLeadingZeros = null;
    static final int[] powersOf10 = null;

    /* JADX INFO: renamed from: com.google.common.math.IntMath$1 */
    static /* synthetic */ class C08601 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = null;

        static {
            $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];
            $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L40:
            $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
        L30:
            $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 3;     // Catch: NoSuchFieldError -> L11
        L28:
            $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 4;     // Catch: NoSuchFieldError -> L14
        L32:
            $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 5;     // Catch: NoSuchFieldError -> L17
        L36:
            $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 6;     // Catch: NoSuchFieldError -> L20
        L42:
            $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;     // Catch: NoSuchFieldError -> L23
        L38:
            $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 8;     // Catch: NoSuchFieldError -> L26
            return;
        }
    }

    static {
        maxLog10ForLeadingZeros = new byte[]{9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0, 0};
        powersOf10 = new int[]{1, 10, 100, 1000, 10000, AacUtil.AAC_LC_MAX_RATE_BYTES_PER_SECOND, 1000000, 10000000, 100000000, 1000000000};
        halfPowersOf10 = new int[]{3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, Integer.MAX_VALUE};
        factorials = new int[]{1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600};
        biggestBinomials = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, 65536, 2345, 477, 193, 110, 75, 58, 49, 43, 39, 37, 35, 34, 34, 33};
    }

    private IntMath() {
    }

    public static int binomial(int r5, int r6) {
        MathPreconditions.checkNonNegative("n", r5);
        MathPreconditions.checkNonNegative("k", r6);
        if (r6 > r5) goto L5;
        boolean r1 = true;
    L6:
        Preconditions.checkArgument(r1, "k (%s) > n (%s)", r6, r5);
        if (r6 <= (r5 >> 1)) goto L10;
        r6 = r5 - r6;
    L10:
        if (r6 < biggestBinomials.length) goto L12;
        return Integer.MAX_VALUE;
    L12:
        if (r5 > biggestBinomials[r6]) goto L25;
        switch(r6) {
            case 0: goto L17;
            case 1: goto L16;
            default: goto L15;
        };
    L15:
        long r0 = 1;
        int r2 = 0;
    L18:
        if (r2 >= r6) goto L21;
        r0 = (r0 * ((long) (r5 - r2))) / ((long) (r2 + 1));
        r2 = r2 + 1;
        goto L18
    L21:
        return (int) r0;
    L16:
        return r5;
    L17:
        return 1;
    L25:
        return Integer.MAX_VALUE;
    L5:
        r1 = false;
        goto L6
    }

    public static int ceilingPowerOfTwo(int r3) {
        MathPreconditions.checkPositive("x", r3);
        if (r3 > 1073741824) goto L7;
        return 1 << (-Integer.numberOfLeadingZeros(r3 - 1));
    L7:
        throw new ArithmeticException("ceilingPowerOfTwo(" + r3 + ") not representable as an int");
    }

    public static int checkedAdd(int r4, int r5) {
        long r0 = ((long) r4) + ((long) r5);
        if (r0 != ((int) r0)) goto L5;
        boolean r2 = true;
    L6:
        MathPreconditions.checkNoOverflow(r2, "checkedAdd", r4, r5);
        return (int) r0;
    L5:
        r2 = false;
        goto L6
    }

    public static int checkedMultiply(int r4, int r5) {
        long r0 = ((long) r4) * ((long) r5);
        if (r0 != ((int) r0)) goto L5;
        boolean r2 = true;
    L6:
        MathPreconditions.checkNoOverflow(r2, "checkedMultiply", r4, r5);
        return (int) r0;
    L5:
        r2 = false;
        goto L6
    }

    public static int checkedPow(int r6, int r7) {
        MathPreconditions.checkNonNegative("exponent", r7);
        boolean r2 = false;
        switch(r6) {
            case -2: goto L19;
            case -1: goto L15;
            case 0: goto L11;
            case 1: goto L10;
            case 2: goto L6;
            default: goto L4;
        };
    L4:
        int r0 = 1;
    L26:
        switch(r7) {
            case 0: goto L32;
            case 1: goto L31;
            default: goto L28;
        };
    L28:
        if ((r7 & 1) == 0) goto L33;
        r0 = checkedMultiply(r0, r6);
    L33:
        r7 = r7 >> 1;
        if (r7 <= 0) goto L26;
        if ((-46340) > r6) goto L38;
        boolean r4 = true;
    L40:
        if (r6 > FLOOR_SQRT_MAX_INT) goto L42;
        boolean r5 = true;
    L43:
        MathPreconditions.checkNoOverflow(r4 & r5, "checkedPow", r6, r7);
        r6 = r6 * r6;
        goto L26
    L42:
        r5 = false;
        goto L43
    L38:
        r4 = false;
        goto L40
    L31:
        return checkedMultiply(r0, r6);
    L32:
        return r0;
    L10:
        return 1;
    L11:
        if (r7 == 0) goto L13;
        return 0;
    L13:
        return 1;
    L6:
        if (r7 >= 31) goto L8;
        r2 = true;
    L8:
        MathPreconditions.checkNoOverflow(r2, "checkedPow", r6, r7);
        return 1 << r7;
    L15:
        if ((r7 & 1) == 0) goto L17;
        return -1;
    L17:
        return 1;
    L19:
        if (r7 >= 32) goto L21;
        r2 = true;
    L21:
        MathPreconditions.checkNoOverflow(r2, "checkedPow", r6, r7);
        if ((r7 & 1) != 0) goto L25;
        return 1 << r7;
    L25:
        return (-1) << r7;
    }

    public static int checkedSubtract(int r4, int r5) {
        long r0 = ((long) r4) - ((long) r5);
        if (r0 != ((int) r0)) goto L5;
        boolean r2 = true;
    L6:
        MathPreconditions.checkNoOverflow(r2, "checkedSubtract", r4, r5);
        return (int) r0;
    L5:
        r2 = false;
        goto L6
    }

    public static int divide(int r9, int r10, RoundingMode r11) {
        Preconditions.checkNotNull(r11);
        if (r10 == 0) goto L51;
        int r0 = r9 / r10;
        int r1 = r9 - (r10 * r0);
        if (r1 != 0) goto L7;
        return r0;
    L7:
        boolean r3 = true;
        int r2 = ((r9 ^ r10) >> 31) | 1;
        switch(C08601.$SwitchMap$java$math$RoundingMode[r11.ordinal()]) {
            case 1: goto L41;
            case 2: goto L45;
            case 3: goto L37;
            case 4: goto L36;
            case 5: goto L32;
            case 6: goto L11;
            case 7: goto L11;
            case 8: goto L11;
            default: goto L10;
        };
    L11:
        int r4 = Math.abs(r1);
        int r6 = r4 - (Math.abs(r10) - r4);
        if (r6 == 0) goto L14;
        if (r6 > 0) goto L46;
        r3 = false;
    L46:
        if (r3 == false) goto L49;
        return r0 + r2;
    L49:
        return r0;
    L14:
        if (r11 == RoundingMode.HALF_UP) goto L46;
        if (r11 != RoundingMode.HALF_EVEN) goto L18;
        boolean r7 = true;
    L20:
        if ((r0 & 1) == 0) goto L22;
        boolean r8 = true;
    L24:
        if ((r7 & r8) == true) goto L46;
        r3 = false;
        goto L46
    L22:
        r8 = false;
        goto L24
    L18:
        r7 = false;
        goto L20
    L32:
        if (r2 > 0) goto L46;
        r3 = false;
        goto L46
    L36:
        r3 = true;
        goto L46
    L37:
        if (r2 < 0) goto L46;
        r3 = false;
        goto L46
    L41:
        if (r1 == 0) goto L44;
        r3 = false;
    L44:
        MathPreconditions.checkRoundingUnnecessary(r3);
    L45:
        r3 = false;
        goto L46
    L10:
        throw new AssertionError();
    L51:
        throw new ArithmeticException("/ by zero");
    }

    public static int factorial(int r1) {
        MathPreconditions.checkNonNegative("n", r1);
        if (r1 < factorials.length) goto L7;
        return Integer.MAX_VALUE;
    L7:
        return factorials[r1];
    }

    public static int floorPowerOfTwo(int r1) {
        MathPreconditions.checkPositive("x", r1);
        return Integer.highestOneBit(r1);
    }

    public static int gcd(int r5, int r6) {
        MathPreconditions.checkNonNegative(CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY, r5);
        MathPreconditions.checkNonNegative("b", r6);
        if (r5 != 0) goto L5;
        return r6;
    L5:
        if (r6 != 0) goto L7;
        return r5;
    L7:
        int r0 = Integer.numberOfTrailingZeros(r5);
        int r7 = r5 >> r0;
        int r1 = Integer.numberOfTrailingZeros(r6);
        int r8 = r6 >> r1;
    L8:
        if (r7 == r8) goto L11;
        int r2 = r7 - r8;
        int r3 = (r2 >> 31) & r2;
        int r4 = (r2 - r3) - r3;
        r8 = r8 + r3;
        r7 = r4 >> Integer.numberOfTrailingZeros(r4);
        goto L8
    L11:
        return r7 << Math.min(r0, r1);
    }

    public static boolean isPowerOfTwo(int r4) {
        boolean r0 = false;
        if (r4 <= 0) goto L5;
        boolean r2 = true;
    L7:
        if (((r4 - 1) & r4) != 0) goto L10;
        r0 = true;
    L10:
        return r0 & r2;
    L5:
        r2 = false;
        goto L7
    }

    public static boolean isPrime(int r2) {
        return LongMath.isPrime(r2);
    }

    static int lessThanBranchFree(int r1, int r2) {
        return (~(~(r1 - r2))) >>> 31;
    }

    public static int log10(int r4, RoundingMode r5) {
        MathPreconditions.checkPositive("x", r4);
        int r0 = log10Floor(r4);
        int r1 = powersOf10[r0];
        switch(C08601.$SwitchMap$java$math$RoundingMode[r5.ordinal()]) {
            case 1: goto L10;
            case 2: goto L14;
            case 3: goto L14;
            case 4: goto L9;
            case 5: goto L9;
            case 6: goto L7;
            case 7: goto L7;
            case 8: goto L7;
            default: goto L5;
        };
    L10:
        if (r4 != r1) goto L12;
        boolean r2 = true;
    L13:
        MathPreconditions.checkRoundingUnnecessary(r2);
        goto L14
    L12:
        r2 = false;
    L14:
        return r0;
    L5:
        throw new AssertionError();
    L7:
        return lessThanBranchFree(halfPowersOf10[r0], r4) + r0;
    L9:
        return lessThanBranchFree(r1, r4) + r0;
    }

    private static int log10Floor(int r2) {
        byte r0 = maxLog10ForLeadingZeros[Integer.numberOfLeadingZeros(r2)];
        return r0 - lessThanBranchFree(r2, powersOf10[r0]);
    }

    public static int log2(int r4, RoundingMode r5) {
        MathPreconditions.checkPositive("x", r4);
        switch(C08601.$SwitchMap$java$math$RoundingMode[r5.ordinal()]) {
            case 1: goto L10;
            case 2: goto L12;
            case 3: goto L12;
            case 4: goto L9;
            case 5: goto L9;
            case 6: goto L6;
            case 7: goto L6;
            case 8: goto L6;
            default: goto L5;
        };
    L6:
        int r0 = Integer.numberOfLeadingZeros(r4);
        return lessThanBranchFree(MAX_POWER_OF_SQRT2_UNSIGNED >>> r0, r4) + (31 - r0);
    L10:
        MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(r4));
        goto L12
    L5:
        throw new AssertionError();
    L9:
        return 32 - Integer.numberOfLeadingZeros(r4 - 1);
    L12:
        return 31 - Integer.numberOfLeadingZeros(r4);
    }

    public static int mean(int r2, int r3) {
        return (r2 & r3) + ((r2 ^ r3) >> 1);
    }

    public static int mod(int r3, int r4) {
        if (r4 <= 0) goto L9;
        int r0 = r3 % r4;
        if (r0 < 0) goto L7;
        return r0;
    L7:
        return r0 + r4;
    L9:
        throw new ArithmeticException("Modulus " + r4 + " must be > 0");
    }

    public static int pow(int r3, int r4) {
        MathPreconditions.checkNonNegative("exponent", r4);
        switch(r3) {
            case -2: goto L17;
            case -1: goto L13;
            case 0: goto L9;
            case 1: goto L8;
            case 2: goto L5;
            default: goto L4;
        };
    L4:
        int r0 = 1;
    L24:
        switch(r4) {
            case 0: goto L30;
            case 1: goto L29;
            default: goto L26;
        };
    L26:
        if ((r4 & 1) != 0) goto L31;
        int r1 = 1;
    L32:
        r0 = r0 * r1;
        r3 = r3 * r3;
        r4 = r4 >> 1;
        goto L24
    L31:
        r1 = r3;
        goto L32
    L29:
        return r3 * r0;
    L30:
        return r0;
    L5:
        if (r4 < 32) goto L7;
        return 0;
    L7:
        return 1 << r4;
    L8:
        return 1;
    L9:
        if (r4 == 0) goto L11;
        return 0;
    L11:
        return 1;
    L17:
        if (r4 < 32) goto L19;
        return 0;
    L19:
        if ((r4 & 1) != 0) goto L22;
        return 1 << r4;
    L22:
        return -(1 << r4);
    L13:
        if ((r4 & 1) == 0) goto L39;
        return -1;
    L39:
        return 1;
    }

    public static int saturatedAdd(int r4, int r5) {
        return Ints.saturatedCast(((long) r4) + ((long) r5));
    }

    public static int saturatedMultiply(int r4, int r5) {
        return Ints.saturatedCast(((long) r4) * ((long) r5));
    }

    public static int saturatedPow(int r6, int r7) {
        MathPreconditions.checkNonNegative("exponent", r7);
        switch(r6) {
            case -2: goto L19;
            case -1: goto L15;
            case 0: goto L11;
            case 1: goto L10;
            case 2: goto L6;
            default: goto L4;
        };
    L4:
        int r0 = 1;
        int r4 = ((r6 >>> 31) & (r7 & 1)) + Integer.MAX_VALUE;
    L27:
        switch(r7) {
            case 0: goto L33;
            case 1: goto L32;
            default: goto L29;
        };
    L29:
        if ((r7 & 1) == 0) goto L34;
        r0 = saturatedMultiply(r0, r6);
    L34:
        r7 = r7 >> 1;
        if (r7 <= 0) goto L27;
        if ((-46340) <= r6) goto L39;
        boolean r2 = true;
    L41:
        if (r6 <= FLOOR_SQRT_MAX_INT) goto L43;
        boolean r5 = true;
    L45:
        if ((r2 | r5) == true) goto L46;
        r6 = r6 * r6;
        goto L27
    L46:
        return r4;
    L43:
        r5 = false;
        goto L45
    L39:
        r2 = false;
        goto L41
    L32:
        return saturatedMultiply(r0, r6);
    L33:
        return r0;
    L10:
        return 1;
    L11:
        if (r7 == 0) goto L13;
        return 0;
    L13:
        return 1;
    L6:
        if (r7 < 31) goto L9;
        return Integer.MAX_VALUE;
    L9:
        return 1 << r7;
    L15:
        if ((r7 & 1) == 0) goto L17;
        return -1;
    L17:
        return 1;
    L19:
        if (r7 < 32) goto L23;
        return (r7 & 1) + Integer.MAX_VALUE;
    L23:
        if ((r7 & 1) != 0) goto L26;
        return 1 << r7;
    L26:
        return (-1) << r7;
    }

    public static int saturatedSubtract(int r4, int r5) {
        return Ints.saturatedCast(((long) r4) - ((long) r5));
    }

    public static int sqrt(int r3, RoundingMode r4) {
        MathPreconditions.checkNonNegative("x", r3);
        int r0 = sqrtFloor(r3);
        switch(C08601.$SwitchMap$java$math$RoundingMode[r4.ordinal()]) {
            case 1: goto L11;
            case 2: goto L15;
            case 3: goto L15;
            case 4: goto L9;
            case 5: goto L9;
            case 6: goto L7;
            case 7: goto L7;
            case 8: goto L7;
            default: goto L5;
        };
    L15:
        return r0;
    L5:
        throw new AssertionError();
    L7:
        return lessThanBranchFree((r0 * r0) + r0, r3) + r0;
    L9:
        return lessThanBranchFree(r0 * r0, r3) + r0;
    L11:
        if ((r0 * r0) != r3) goto L13;
        boolean r1 = true;
    L14:
        MathPreconditions.checkRoundingUnnecessary(r1);
        goto L15
    L13:
        r1 = false;
        goto L14
    }

    private static int sqrtFloor(int r2) {
        return (int) Math.sqrt(r2);
    }
}
