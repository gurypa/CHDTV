package com.google.common.math;

import java.math.BigInteger;
import java.math.RoundingMode;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class MathPreconditions {
    private MathPreconditions() {
    }

    static void checkInRangeForRoundingInputs(boolean r3, double r4, RoundingMode r6) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new ArithmeticException("rounded value is out of range for input " + r4 + " and rounding mode " + r6);
    }

    static void checkNoOverflow(boolean r3, String r4, int r5, int r6) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new ArithmeticException("overflow: " + r4 + "(" + r5 + ", " + r6 + ")");
    }

    static void checkNoOverflow(boolean r3, String r4, long r5, long r7) {
        if (r3 == false) goto L5;
        return;
    L5:
        throw new ArithmeticException("overflow: " + r4 + "(" + r5 + ", " + r7 + ")");
    }

    static double checkNonNegative(String r3, double r4) {
        if (r4 < 0.0d) goto L6;
        return r4;
    L6:
        throw new IllegalArgumentException(r3 + " (" + r4 + ") must be >= 0");
    }

    static int checkNonNegative(String r3, int r4) {
        if (r4 < 0) goto L5;
        return r4;
    L5:
        throw new IllegalArgumentException(r3 + " (" + r4 + ") must be >= 0");
    }

    static long checkNonNegative(String r3, long r4) {
        if (r4 < 0) goto L6;
        return r4;
    L6:
        throw new IllegalArgumentException(r3 + " (" + r4 + ") must be >= 0");
    }

    static BigInteger checkNonNegative(String r3, BigInteger r4) {
        if (r4.signum() < 0) goto L6;
        return r4;
    L6:
        throw new IllegalArgumentException(r3 + " (" + r4 + ") must be >= 0");
    }

    static int checkPositive(String r3, int r4) {
        if (r4 <= 0) goto L5;
        return r4;
    L5:
        throw new IllegalArgumentException(r3 + " (" + r4 + ") must be > 0");
    }

    static long checkPositive(String r3, long r4) {
        if (r4 <= 0) goto L6;
        return r4;
    L6:
        throw new IllegalArgumentException(r3 + " (" + r4 + ") must be > 0");
    }

    static BigInteger checkPositive(String r3, BigInteger r4) {
        if (r4.signum() <= 0) goto L6;
        return r4;
    L6:
        throw new IllegalArgumentException(r3 + " (" + r4 + ") must be > 0");
    }

    static void checkRoundingUnnecessary(boolean r2) {
        if (r2 == false) goto L5;
        return;
    L5:
        throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
    }
}
