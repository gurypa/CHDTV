package androidx.core.math;

/* JADX INFO: loaded from: classes.dex */
public class MathUtils {
    private MathUtils() {
    }

    public static int addExact(int r3, int r4) {
        int r0 = r3 + r4;
        if (((r3 ^ r0) & (r4 ^ r0)) < 0) goto L6;
        return r0;
    L6:
        throw new ArithmeticException("integer overflow");
    }

    public static long addExact(long r6, long r8) {
        long r0 = r6 + r8;
        if (((r6 ^ r0) & (r8 ^ r0)) < 0) goto L6;
        return r0;
    L6:
        throw new ArithmeticException("long overflow");
    }

    public static double clamp(double r1, double r3, double r5) {
        if (r1 >= r3) goto L6;
        return r3;
    L6:
        if (r1 <= r5) goto L8;
        return r5;
    L8:
        return r1;
    }

    public static float clamp(float r1, float r2, float r3) {
        if (r1 >= r2) goto L6;
        return r2;
    L6:
        if (r1 <= r3) goto L8;
        return r3;
    L8:
        return r1;
    }

    public static int clamp(int r0, int r1, int r2) {
        if (r0 >= r1) goto L4;
        return r1;
    L4:
        if (r0 <= r2) goto L6;
        return r2;
    L6:
        return r0;
    }

    public static long clamp(long r1, long r3, long r5) {
        if (r1 >= r3) goto L6;
        return r3;
    L6:
        if (r1 <= r5) goto L8;
        return r5;
    L8:
        return r1;
    }

    public static int decrementExact(int r2) {
        if (r2 == Integer.MIN_VALUE) goto L7;
        return r2 - 1;
    L7:
        throw new ArithmeticException("integer overflow");
    }

    public static long decrementExact(long r2) {
        if (r2 == Long.MIN_VALUE) goto L7;
        return r2 - 1;
    L7:
        throw new ArithmeticException("long overflow");
    }

    public static int incrementExact(int r2) {
        if (r2 == Integer.MAX_VALUE) goto L7;
        return r2 + 1;
    L7:
        throw new ArithmeticException("integer overflow");
    }

    public static long incrementExact(long r2) {
        if (r2 == Long.MAX_VALUE) goto L7;
        return 1 + r2;
    L7:
        throw new ArithmeticException("long overflow");
    }

    public static int multiplyExact(int r4, int r5) {
        long r0 = ((long) r4) * ((long) r5);
        if (((int) r0) != r0) goto L7;
        return (int) r0;
    L7:
        throw new ArithmeticException("integer overflow");
    }

    public static long multiplyExact(long r10, long r12) {
        long r0 = r10 * r12;
        if (((Math.abs(r10) | Math.abs(r12)) >>> 31) != 0) goto L5;
    L15:
        return r0;
    L5:
        if (r12 == 0) goto L9;
        if ((r0 / r12) == r10) goto L9;
    L14:
        throw new ArithmeticException("long overflow");
    L9:
        if (r10 != Long.MIN_VALUE) goto L15;
        if (r12 == (-1)) goto L14;
        goto L15
    }

    public static int negateExact(int r2) {
        if (r2 == Integer.MIN_VALUE) goto L7;
        return -r2;
    L7:
        throw new ArithmeticException("integer overflow");
    }

    public static long negateExact(long r2) {
        if (r2 == Long.MIN_VALUE) goto L7;
        return -r2;
    L7:
        throw new ArithmeticException("long overflow");
    }

    public static int subtractExact(int r3, int r4) {
        int r0 = r3 - r4;
        if (((r3 ^ r4) & (r3 ^ r0)) < 0) goto L6;
        return r0;
    L6:
        throw new ArithmeticException("integer overflow");
    }

    public static long subtractExact(long r6, long r8) {
        long r0 = r6 - r8;
        if (((r6 ^ r8) & (r6 ^ r0)) < 0) goto L6;
        return r0;
    L6:
        throw new ArithmeticException("long overflow");
    }

    public static int toIntExact(long r2) {
        if (((int) r2) != r2) goto L7;
        return (int) r2;
    L7:
        throw new ArithmeticException("integer overflow");
    }
}
