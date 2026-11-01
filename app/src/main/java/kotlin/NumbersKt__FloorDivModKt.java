package kotlin;

/* JADX INFO: compiled from: FloorDivMod.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000 \n\u0000\n\u0002\u0010\b\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\t\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0000\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b¨\u0006\t"}, m421d2 = {"floorDiv", "", "", "other", "", "", "mod", "", "", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/NumbersKt")
class NumbersKt__FloorDivModKt extends NumbersKt__BigIntegersKt {
    public NumbersKt__FloorDivModKt() {
    }

    private static final int floorDiv(byte r2, byte r3) {
        int r0 = r2 / r3;
        if ((r2 ^ r3) < 0) goto L5;
        return r0;
    L5:
        if ((r0 * r3) != r2) goto L7;
        return r0;
    L7:
        return r0 - 1;
    }

    private static final int floorDiv(byte r2, int r3) {
        int r0 = r2 / r3;
        if ((r2 ^ r3) < 0) goto L5;
        return r0;
    L5:
        if ((r0 * r3) != r2) goto L7;
        return r0;
    L7:
        return r0 - 1;
    }

    private static final int floorDiv(byte r2, short r3) {
        int r0 = r2 / r3;
        if ((r2 ^ r3) < 0) goto L5;
        return r0;
    L5:
        if ((r0 * r3) != r2) goto L7;
        return r0;
    L7:
        return r0 - 1;
    }

    private static final int floorDiv(int r2, byte r3) {
        int r0 = r2 / r3;
        if ((r2 ^ r3) < 0) goto L5;
        return r0;
    L5:
        if ((r0 * r3) != r2) goto L7;
        return r0;
    L7:
        return r0 - 1;
    }

    private static final int floorDiv(int r2, int r3) {
        int r0 = r2 / r3;
        if ((r2 ^ r3) < 0) goto L5;
        return r0;
    L5:
        if ((r0 * r3) != r2) goto L7;
        return r0;
    L7:
        return r0 - 1;
    }

    private static final int floorDiv(int r2, short r3) {
        int r0 = r2 / r3;
        if ((r2 ^ r3) < 0) goto L5;
        return r0;
    L5:
        if ((r0 * r3) != r2) goto L7;
        return r0;
    L7:
        return r0 - 1;
    }

    private static final int floorDiv(short r2, byte r3) {
        int r0 = r2 / r3;
        if ((r2 ^ r3) < 0) goto L5;
        return r0;
    L5:
        if ((r0 * r3) != r2) goto L7;
        return r0;
    L7:
        return r0 - 1;
    }

    private static final int floorDiv(short r2, int r3) {
        int r0 = r2 / r3;
        if ((r2 ^ r3) < 0) goto L5;
        return r0;
    L5:
        if ((r0 * r3) != r2) goto L7;
        return r0;
    L7:
        return r0 - 1;
    }

    private static final int floorDiv(short r2, short r3) {
        int r0 = r2 / r3;
        if ((r2 ^ r3) < 0) goto L5;
        return r0;
    L5:
        if ((r0 * r3) != r2) goto L7;
        return r0;
    L7:
        return r0 - 1;
    }

    private static final long floorDiv(byte r8, long r9) {
        long r0 = r8;
        long r2 = r0 / r9;
        if ((r0 ^ r9) < 0) goto L5;
        return r2;
    L5:
        if ((r2 * r9) != r0) goto L7;
        return r2;
    L7:
        return r2 - 1;
    }

    private static final long floorDiv(int r8, long r9) {
        long r0 = r8;
        long r2 = r0 / r9;
        if ((r0 ^ r9) < 0) goto L5;
        return r2;
    L5:
        if ((r2 * r9) != r0) goto L7;
        return r2;
    L7:
        return r2 - 1;
    }

    private static final long floorDiv(long r8, byte r10) {
        long r0 = r10;
        long r2 = r8 / r0;
        if ((r8 ^ r0) < 0) goto L5;
        return r2;
    L5:
        if ((r0 * r2) != r8) goto L7;
        return r2;
    L7:
        return r2 - 1;
    }

    private static final long floorDiv(long r8, int r10) {
        long r0 = r10;
        long r2 = r8 / r0;
        if ((r8 ^ r0) < 0) goto L5;
        return r2;
    L5:
        if ((r0 * r2) != r8) goto L7;
        return r2;
    L7:
        return r2 - 1;
    }

    private static final long floorDiv(long r6, long r8) {
        long r0 = r6 / r8;
        if ((r6 ^ r8) < 0) goto L5;
        return r0;
    L5:
        if ((r0 * r8) != r6) goto L7;
        return r0;
    L7:
        return r0 - 1;
    }

    private static final long floorDiv(long r8, short r10) {
        long r0 = r10;
        long r2 = r8 / r0;
        if ((r8 ^ r0) < 0) goto L5;
        return r2;
    L5:
        if ((r0 * r2) != r8) goto L7;
        return r2;
    L7:
        return r2 - 1;
    }

    private static final long floorDiv(short r8, long r9) {
        long r0 = r8;
        long r2 = r0 / r9;
        if ((r0 ^ r9) < 0) goto L5;
        return r2;
    L5:
        if ((r2 * r9) != r0) goto L7;
        return r2;
    L7:
        return r2 - 1;
    }

    private static final byte mod(byte r3, byte r4) {
        int r0 = r3 % r4;
        return (byte) (r0 + ((((r0 ^ r4) & ((-r0) | r0)) >> 31) & r4));
    }

    private static final byte mod(int r3, byte r4) {
        int r0 = r3 % r4;
        return (byte) (r0 + ((((r0 ^ r4) & ((-r0) | r0)) >> 31) & r4));
    }

    private static final byte mod(long r8, byte r10) {
        long r0 = r10;
        long r2 = r8 % r0;
        return (byte) (r2 + (r0 & (((r2 ^ r0) & ((-r2) | r2)) >> 63)));
    }

    private static final byte mod(short r3, byte r4) {
        int r0 = r3 % r4;
        return (byte) (r0 + ((((r0 ^ r4) & ((-r0) | r0)) >> 31) & r4));
    }

    private static final double mod(double r9, double r11) {
        double r0 = r9 % r11;
        boolean r3 = true;
        if (r0 != 0.0d) goto L5;
        boolean r2 = true;
    L6:
        if (r2 == true) goto L14;
        if (Math.signum(r0) == Math.signum(r11)) goto L11;
        r3 = false;
    L11:
        if (r3 == true) goto L14;
        return r0 + r11;
    L14:
        return r0;
    L5:
        r2 = false;
        goto L6
    }

    private static final double mod(double r11, float r13) {
        double r0 = r13;
        double r2 = r11 % r0;
        boolean r5 = true;
        if (r2 != 0.0d) goto L5;
        boolean r4 = true;
    L6:
        if (r4 == false) goto L8;
        return r2;
    L8:
        if (Math.signum(r2) == Math.signum(r0)) goto L11;
        r5 = false;
    L11:
        if (r5 == false) goto L13;
        return r2;
    L13:
        return r2 + r0;
    L5:
        r4 = false;
        goto L6
    }

    private static final double mod(float r9, double r10) {
        double r0 = ((double) r9) % r10;
        boolean r3 = true;
        if (r0 != 0.0d) goto L5;
        boolean r2 = true;
    L6:
        if (r2 == false) goto L8;
        return r0;
    L8:
        if (Math.signum(r0) == Math.signum(r10)) goto L11;
        r3 = false;
    L11:
        if (r3 == false) goto L13;
        return r0;
    L13:
        return r0 + r10;
    L5:
        r2 = false;
        goto L6
    }

    private static final float mod(float r5, float r6) {
        float r0 = r5 % r6;
        boolean r2 = true;
        if (r0 != 0.0f) goto L5;
        boolean r1 = true;
    L6:
        if (r1 == true) goto L14;
        if (Math.signum(r0) == Math.signum(r6)) goto L11;
        r2 = false;
    L11:
        if (r2 == true) goto L14;
        return r0 + r6;
    L14:
        return r0;
    L5:
        r1 = false;
        goto L6
    }

    private static final int mod(byte r3, int r4) {
        int r0 = r3 % r4;
        return r0 + ((((r0 ^ r4) & ((-r0) | r0)) >> 31) & r4);
    }

    private static final int mod(int r3, int r4) {
        int r0 = r3 % r4;
        return ((((r0 ^ r4) & ((-r0) | r0)) >> 31) & r4) + r0;
    }

    private static final int mod(long r8, int r10) {
        long r0 = r10;
        long r2 = r8 % r0;
        return (int) (r2 + (r0 & (((r2 ^ r0) & ((-r2) | r2)) >> 63)));
    }

    private static final int mod(short r3, int r4) {
        int r0 = r3 % r4;
        return r0 + ((((r0 ^ r4) & ((-r0) | r0)) >> 31) & r4);
    }

    private static final long mod(byte r6, long r7) {
        long r0 = ((long) r6) % r7;
        return r0 + ((((r0 ^ r7) & ((-r0) | r0)) >> 63) & r7);
    }

    private static final long mod(int r6, long r7) {
        long r0 = ((long) r6) % r7;
        return r0 + ((((r0 ^ r7) & ((-r0) | r0)) >> 63) & r7);
    }

    private static final long mod(long r6, long r8) {
        long r0 = r6 % r8;
        return ((((r0 ^ r8) & ((-r0) | r0)) >> 63) & r8) + r0;
    }

    private static final long mod(short r6, long r7) {
        long r0 = ((long) r6) % r7;
        return r0 + ((((r0 ^ r7) & ((-r0) | r0)) >> 63) & r7);
    }

    private static final short mod(byte r3, short r4) {
        int r0 = r3 % r4;
        return (short) (r0 + ((((r0 ^ r4) & ((-r0) | r0)) >> 31) & r4));
    }

    private static final short mod(int r3, short r4) {
        int r0 = r3 % r4;
        return (short) (r0 + ((((r0 ^ r4) & ((-r0) | r0)) >> 31) & r4));
    }

    private static final short mod(long r8, short r10) {
        long r0 = r10;
        long r2 = r8 % r0;
        return (short) (r2 + (r0 & (((r2 ^ r0) & ((-r2) | r2)) >> 63)));
    }

    private static final short mod(short r3, short r4) {
        int r0 = r3 % r4;
        return (short) (r0 + ((((r0 ^ r4) & ((-r0) | r0)) >> 31) & r4));
    }
}
