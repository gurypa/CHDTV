package com.google.common.math;

import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class BigIntegerMath {
    private static final double LN_10 = 0.0d;
    private static final double LN_2 = 0.0d;
    static final BigInteger SQRT2_PRECOMPUTED_BITS = null;
    static final int SQRT2_PRECOMPUTE_THRESHOLD = 256;

    /* JADX INFO: renamed from: com.google.common.math.BigIntegerMath$1 */
    static /* synthetic */ class C08581 {
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

    private static class BigIntegerToDoubleRounder extends ToDoubleRounder<BigInteger> {
        static final BigIntegerToDoubleRounder INSTANCE = null;

        static {
            INSTANCE = new BigIntegerToDoubleRounder();
        }

        private BigIntegerToDoubleRounder() {
        }

        @Override // com.google.common.math.ToDoubleRounder
        /* bridge */ /* synthetic */ Number minus(Number r1, Number r2) {
            return minus((BigInteger) r1, (BigInteger) r2);
        }

        BigInteger minus(BigInteger r2, BigInteger r3) {
            return r2.subtract(r3);
        }

        @Override // com.google.common.math.ToDoubleRounder
        /* bridge */ /* synthetic */ double roundToDoubleArbitrarily(Number r3) {
            return roundToDoubleArbitrarily((BigInteger) r3);
        }

        double roundToDoubleArbitrarily(BigInteger r3) {
            return DoubleUtils.bigToDouble(r3);
        }

        @Override // com.google.common.math.ToDoubleRounder
        /* bridge */ /* synthetic */ int sign(Number r1) {
            return sign((BigInteger) r1);
        }

        int sign(BigInteger r2) {
            return r2.signum();
        }

        @Override // com.google.common.math.ToDoubleRounder
        /* bridge */ /* synthetic */ Number toX(double r1, RoundingMode r3) {
            return toX(r1, r3);
        }

        @Override // com.google.common.math.ToDoubleRounder
        BigInteger toX(double r2, RoundingMode r4) {
            return DoubleMath.roundToBigInteger(r2, r4);
        }
    }

    static {
        SQRT2_PRECOMPUTED_BITS = new BigInteger("16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a", 16);
        LN_10 = Math.log(10.0d);
        LN_2 = Math.log(2.0d);
    }

    private BigIntegerMath() {
    }

    public static BigInteger binomial(int r12, int r13) {
        MathPreconditions.checkNonNegative("n", r12);
        MathPreconditions.checkNonNegative("k", r13);
        if (r13 > r12) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "k (%s) > n (%s)", r13, r12);
        if (r13 <= (r12 >> 1)) goto L10;
        r13 = r12 - r13;
    L10:
        if (r13 < LongMath.biggestBinomials.length) goto L12;
    L15:
        BigInteger r1 = BigInteger.ONE;
        long r2 = r12;
        long r3 = 1;
        int r5 = LongMath.log2(r12, RoundingMode.CEILING);
        int r6 = r5;
        int r7 = 1;
    L16:
        if (r7 >= r13) goto L23;
        int r8 = r12 - r7;
        int r9 = r7 + 1;
        if ((r6 + r5) < 63) goto L20;
        r1 = r1.multiply(BigInteger.valueOf(r2)).divide(BigInteger.valueOf(r3));
        r2 = r8;
        r3 = r9;
        r6 = r5;
    L21:
        r7 = r7 + 1;
        goto L16
    L20:
        r2 = r2 * ((long) r8);
        r3 = r3 * ((long) r9);
        r6 = r6 + r5;
        goto L21
    L23:
        return r1.multiply(BigInteger.valueOf(r2)).divide(BigInteger.valueOf(r3));
    L12:
        if (r12 > LongMath.biggestBinomials[r13]) goto L15;
        return BigInteger.valueOf(LongMath.binomial(r12, r13));
    L5:
        r0 = false;
        goto L6
    }

    public static BigInteger ceilingPowerOfTwo(BigInteger r2) {
        return BigInteger.ZERO.setBit(log2(r2, RoundingMode.CEILING));
    }

    public static BigInteger divide(BigInteger r3, BigInteger r4, RoundingMode r5) {
        return new BigDecimal(r3).divide(new BigDecimal(r4), 0, r5).toBigIntegerExact();
    }

    public static BigInteger factorial(int r21) {
        MathPreconditions.checkNonNegative("n", r21);
        if (r21 < LongMath.factorials.length) goto L5;
        ArrayList r2 = new ArrayList(IntMath.divide(IntMath.log2(r21, RoundingMode.CEILING) * r21, 64, RoundingMode.CEILING));
        int r4 = LongMath.factorials.length;
        long r6 = LongMath.factorials[r4 - 1];
        int r5 = Long.numberOfTrailingZeros(r6);
        long r7 = r6 >> r5;
        boolean r9 = true;
        int r8 = LongMath.log2(r7, RoundingMode.FLOOR) + 1;
        int r10 = LongMath.log2(r4, RoundingMode.FLOOR) + 1;
        int r11 = 1 << (r10 - 1);
        long r12 = r4;
    L8:
        if (r12 > r21) goto L18;
        boolean r14 = r9;
        int r17 = r10;
        if ((((long) r11) & r12) == 0) goto L12;
        r10 = r17 + 1;
        r11 = r11 << 1;
    L13:
        int r13 = Long.numberOfTrailingZeros(r12);
        long r18 = r12 >> r13;
        r5 = r5 + r13;
        if (((r10 - r13) + r8) < 64) goto L16;
        r2.add(BigInteger.valueOf(r7));
        r7 = 1;
    L16:
        r7 = r7 * r18;
        r8 = LongMath.log2(r7, RoundingMode.FLOOR) + 1;
        r12 = r12 + 1;
        r9 = r14;
        goto L8
    L12:
        r10 = r17;
        goto L13
    L18:
        if (r7 <= 1) goto L21;
        r2.add(BigInteger.valueOf(r7));
    L21:
        return listProduct(r2).shiftLeft(r5);
    L5:
        return BigInteger.valueOf(LongMath.factorials[r21]);
    }

    static boolean fitsInLong(BigInteger r2) {
        if (r2.bitLength() > 63) goto L5;
        return true;
    L5:
        return false;
    }

    public static BigInteger floorPowerOfTwo(BigInteger r2) {
        return BigInteger.ZERO.setBit(log2(r2, RoundingMode.FLOOR));
    }

    public static boolean isPowerOfTwo(BigInteger r3) {
        Preconditions.checkNotNull(r3);
        if (r3.signum() > 0) goto L5;
    L7:
        return false;
    L5:
        if (r3.getLowestSetBit() != (r3.bitLength() - 1)) goto L7;
        return true;
    }

    static BigInteger listProduct(List<BigInteger> r2) {
        return listProduct(r2, 0, r2.size());
    }

    static BigInteger listProduct(List<BigInteger> r3, int r4, int r5) {
        switch((r5 - r4)) {
            case 0: goto L13;
            case 1: goto L11;
            case 2: goto L9;
            case 3: goto L7;
            default: goto L4;
        };
    L4:
        int r0 = (r5 + r4) >>> 1;
        return listProduct(r3, r4, r0).multiply(listProduct(r3, r0, r5));
    L7:
        return r3.get(r4).multiply(r3.get(r4 + 1)).multiply(r3.get(r4 + 2));
    L9:
        return r3.get(r4).multiply(r3.get(r4 + 1));
    L11:
        return r3.get(r4);
    L13:
        return BigInteger.ONE;
    }

    public static int log10(BigInteger r9, RoundingMode r10) {
        MathPreconditions.checkPositive("x", r9);
        if (fitsInLong(r9) == true) goto L5;
        int r0 = (int) ((((double) log2(r9, RoundingMode.FLOOR)) * LN_2) / LN_10);
        BigInteger r1 = BigInteger.TEN.pow(r0);
        int r2 = r1.compareTo(r9);
        if (r2 <= 0) goto L11;
    L8:
        r0 = r0 - 1;
        r1 = r1.divide(BigInteger.TEN);
        r2 = r1.compareTo(r9);
        if (r2 > 0) goto L8;
    L14:
        int r3 = r0;
        BigInteger r4 = r1;
        int r5 = r2;
        switch(C08581.$SwitchMap$java$math$RoundingMode[r10.ordinal()]) {
            case 1: goto L28;
            case 2: goto L32;
            case 3: goto L32;
            case 4: goto L24;
            case 5: goto L24;
            case 6: goto L19;
            case 7: goto L19;
            case 8: goto L19;
            default: goto L17;
        };
    L28:
        if (r5 != 0) goto L30;
        boolean r6 = true;
    L31:
        MathPreconditions.checkRoundingUnnecessary(r6);
        goto L32
    L30:
        r6 = false;
    L32:
        return r3;
    L17:
        throw new AssertionError();
    L19:
        if (r9.pow(2).compareTo(r4.pow(2).multiply(BigInteger.TEN)) > 0) goto L22;
        return r3;
    L22:
        return r3 + 1;
    L24:
        if (r4.equals(r9) == false) goto L27;
        return r3;
    L27:
        return r3 + 1;
    L11:
        BigInteger r7 = BigInteger.TEN.multiply(r1);
        int r8 = r7.compareTo(r9);
    L12:
        if (r8 > 0) goto L14;
        r0 = r0 + 1;
        r1 = r7;
        r2 = r8;
        r7 = BigInteger.TEN.multiply(r1);
        r8 = r7.compareTo(r9);
        goto L12
    L5:
        return LongMath.log10(r9.longValue(), r10);
    }

    public static int log2(BigInteger r4, RoundingMode r5) {
        MathPreconditions.checkPositive("x", (BigInteger) Preconditions.checkNotNull(r4));
        int r0 = r4.bitLength() - 1;
        switch(C08581.$SwitchMap$java$math$RoundingMode[r5.ordinal()]) {
            case 1: goto L23;
            case 2: goto L24;
            case 3: goto L24;
            case 4: goto L19;
            case 5: goto L19;
            case 6: goto L7;
            case 7: goto L7;
            case 8: goto L7;
            default: goto L5;
        };
    L23:
        MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(r4));
    L24:
        return r0;
    L5:
        throw new AssertionError();
    L7:
        if (r0 >= 256) goto L14;
        if (r4.compareTo(SQRT2_PRECOMPUTED_BITS.shiftRight(256 - r0)) > 0) goto L12;
        return r0;
    L12:
        return r0 + 1;
    L14:
        if ((r4.pow(2).bitLength() - 1) >= ((r0 * 2) + 1)) goto L17;
        return r0;
    L17:
        return r0 + 1;
    L19:
        if (isPowerOfTwo(r4) == false) goto L22;
        return r0;
    L22:
        return r0 + 1;
    }

    public static double roundToDouble(BigInteger r2, RoundingMode r3) {
        return BigIntegerToDoubleRounder.INSTANCE.roundToDouble(r2, r3);
    }

    public static BigInteger sqrt(BigInteger r5, RoundingMode r6) {
        MathPreconditions.checkNonNegative("x", r5);
        if (fitsInLong(r5) == true) goto L5;
        BigInteger r0 = sqrtFloor(r5);
        switch(C08581.$SwitchMap$java$math$RoundingMode[r6.ordinal()]) {
            case 1: goto L25;
            case 2: goto L26;
            case 3: goto L26;
            case 4: goto L15;
            case 5: goto L15;
            case 6: goto L11;
            case 7: goto L11;
            case 8: goto L11;
            default: goto L9;
        };
    L15:
        int r1 = r0.intValue();
        if ((r1 * r1) == r5.intValue()) goto L18;
    L20:
        boolean r2 = false;
    L21:
        if (r2 == false) goto L24;
        return r0;
    L24:
        return r0.add(BigInteger.ONE);
    L18:
        if (r0.pow(2).equals(r5) == false) goto L20;
        r2 = true;
        goto L21
    L25:
        MathPreconditions.checkRoundingUnnecessary(r0.pow(2).equals(r5));
    L26:
        return r0;
    L9:
        throw new AssertionError();
    L11:
        if (r0.pow(2).add(r0).compareTo(r5) < 0) goto L14;
        return r0;
    L14:
        return r0.add(BigInteger.ONE);
    L5:
        return BigInteger.valueOf(LongMath.sqrt(r5.longValue(), r6));
    }

    private static BigInteger sqrtApproxWithDoubles(BigInteger r3) {
        return DoubleMath.roundToBigInteger(Math.sqrt(DoubleUtils.bigToDouble(r3)), RoundingMode.HALF_EVEN);
    }

    private static BigInteger sqrtFloor(BigInteger r5) {
        int r0 = log2(r5, RoundingMode.FLOOR);
        if (r0 >= 1023) goto L5;
        BigInteger r1 = sqrtApproxWithDoubles(r5);
    L6:
        BigInteger r2 = r1.add(r5.divide(r1)).shiftRight(1);
        if (r1.equals(r2) == false) goto L9;
        return r1;
    L9:
        BigInteger r3 = r2;
        r2 = r3.add(r5.divide(r3)).shiftRight(1);
        if (r2.compareTo(r3) < 0) goto L9;
        return r3;
    L5:
        int r4 = (r0 - 52) & (-2);
        r1 = sqrtApproxWithDoubles(r5.shiftRight(r4)).shiftLeft(r4 >> 1);
        goto L6
    }
}
