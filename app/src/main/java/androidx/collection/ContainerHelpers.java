package androidx.collection;

/* JADX INFO: loaded from: classes.dex */
class ContainerHelpers {
    static final int[] EMPTY_INTS = null;
    static final long[] EMPTY_LONGS = null;
    static final Object[] EMPTY_OBJECTS = null;

    static {
        EMPTY_INTS = new int[0];
        EMPTY_LONGS = new long[0];
        EMPTY_OBJECTS = new Object[0];
    }

    private ContainerHelpers() {
    }

    static int binarySearch(int[] r4, int r5, int r6) {
        int r0 = 0;
        int r1 = r5 - 1;
    L3:
        if (r0 > r1) goto L12;
        int r2 = (r0 + r1) >>> 1;
        int r3 = r4[r2];
        if (r3 < r6) goto L6;
        if (r3 <= r6) goto L10;
        r1 = r2 - 1;
        goto L3
    L10:
        return r2;
    L6:
        r0 = r2 + 1;
        goto L3
    L12:
        return ~r0;
    }

    static int binarySearch(long[] r6, int r7, long r8) {
        int r0 = 0;
        int r1 = r7 - 1;
    L3:
        if (r0 > r1) goto L13;
        int r2 = (r0 + r1) >>> 1;
        long r3 = r6[r2];
        if (r3 < r8) goto L6;
        if (r3 <= r8) goto L11;
        r1 = r2 - 1;
        goto L3
    L11:
        return r2;
    L6:
        r0 = r2 + 1;
        goto L3
    L13:
        return ~r0;
    }

    public static boolean equal(Object r1, Object r2) {
        if (r1 == r2) goto L8;
        if (r1 != null) goto L5;
    L7:
        return false;
    L5:
        if (r1.equals(r2) == false) goto L7;
    L8:
        return true;
    }

    public static int idealByteArraySize(int r3) {
        int r0 = 4;
    L4:
        if (r0 >= 32) goto L10;
        if (r3 <= ((1 << r0) - 12)) goto L8;
        r0 = r0 + 1;
        goto L4
    L8:
        return (1 << r0) - 12;
    L10:
        return r3;
    }

    public static int idealIntArraySize(int r1) {
        return idealByteArraySize(r1 * 4) / 4;
    }

    public static int idealLongArraySize(int r1) {
        return idealByteArraySize(r1 * 8) / 8;
    }
}
