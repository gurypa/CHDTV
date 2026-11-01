package com.google.common.math;

import com.google.common.base.Preconditions;
import java.math.BigInteger;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class DoubleUtils {
    static final int EXPONENT_BIAS = 1023;
    static final long EXPONENT_MASK = 9218868437227405312L;
    static final long IMPLICIT_BIT = 4503599627370496L;
    static final long ONE_BITS = 4607182418800017408L;
    static final int SIGNIFICAND_BITS = 52;
    static final long SIGNIFICAND_MASK = 4503599627370495L;
    static final long SIGN_MASK = Long.MIN_VALUE;

    private DoubleUtils() {
    }

    static double bigToDouble(BigInteger r16) {
        BigInteger r0 = r16.abs();
        boolean r2 = true;
        int r1 = r0.bitLength() - 1;
        if (r1 >= 63) goto L7;
        return r16.longValue();
    L7:
        if (r1 > 1023) goto L9;
        int r3 = (r1 - 52) - 1;
        long r4 = r0.shiftRight(r3).longValue();
        long r6 = (r4 >> 1) & SIGNIFICAND_MASK;
        if ((r4 & 1) != 0) goto L13;
    L17:
        r2 = false;
    L18:
        if (r2 == false) goto L20;
        long r8 = 1 + r6;
    L22:
        return Double.longBitsToDouble(((((long) (r1 + 1023)) << 52) + r8) | (((long) r16.signum()) & Long.MIN_VALUE));
    L20:
        r8 = r6;
        goto L22
    L13:
        if ((r6 & 1) != 0) goto L18;
        if (r0.getLowestSetBit() >= r3) goto L17;
    L9:
        return ((double) r16.signum()) * Double.POSITIVE_INFINITY;
    }

    static double ensureNonNegative(double r2) {
        Preconditions.checkArgument(!Double.isNaN(r2));
        return Math.max(r2, 0.0d);
    }

    static long getSignificand(double r5) {
        Preconditions.checkArgument(isFinite(r5), "not a normal value");
        int r0 = Math.getExponent(r5);
        long r1 = Double.doubleToRawLongBits(r5) & SIGNIFICAND_MASK;
        if (r0 != (-1023)) goto L6;
        return r1 << 1;
    L6:
        return IMPLICIT_BIT | r1;
    }

    static boolean isFinite(double r2) {
        if (Math.getExponent(r2) > 1023) goto L5;
        return true;
    L5:
        return false;
    }

    static boolean isNormal(double r2) {
        if (Math.getExponent(r2) < (-1022)) goto L5;
        return true;
    L5:
        return false;
    }

    static double nextDown(double r2) {
        return -Math.nextUp(-r2);
    }

    static double scaleNormalize(double r4) {
        return Double.longBitsToDouble(ONE_BITS | (Double.doubleToRawLongBits(r4) & SIGNIFICAND_MASK));
    }
}
