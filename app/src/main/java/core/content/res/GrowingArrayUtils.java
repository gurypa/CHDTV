package androidx.core.content.res;

import java.lang.reflect.Array;

/* JADX INFO: loaded from: classes.dex */
final class GrowingArrayUtils {
    private GrowingArrayUtils() {
    }

    public static int[] append(int[] r2, int r3, int r4) {
        if ((r3 + 1) <= r2.length) goto L5;
        int[] r0 = new int[growSize(r3)];
        System.arraycopy(r2, 0, r0, 0, r3);
        r2 = r0;
    L5:
        r2[r3] = r4;
        return r2;
    }

    public static long[] append(long[] r2, int r3, long r4) {
        if ((r3 + 1) <= r2.length) goto L5;
        long[] r0 = new long[growSize(r3)];
        System.arraycopy(r2, 0, r0, 0, r3);
        r2 = r0;
    L5:
        r2[r3] = r4;
        return r2;
    }

    public static <T> T[] append(T[] r2, int r3, T r4) {
        if ((r3 + 1) <= r2.length) goto L5;
        Object[] r0 = (Object[]) Array.newInstance(r2.getClass().getComponentType(), growSize(r3));
        System.arraycopy(r2, 0, r0, 0, r3);
        r2 = (T[]) r0;
    L5:
        r2[r3] = r4;
        return r2;
    }

    public static boolean[] append(boolean[] r2, int r3, boolean r4) {
        if ((r3 + 1) <= r2.length) goto L5;
        boolean[] r0 = new boolean[growSize(r3)];
        System.arraycopy(r2, 0, r0, 0, r3);
        r2 = r0;
    L5:
        r2[r3] = r4;
        return r2;
    }

    public static int growSize(int r1) {
        if (r1 > 4) goto L6;
        return 8;
    L6:
        return r1 * 2;
    }

    public static int[] insert(int[] r3, int r4, int r5, int r6) {
        if ((r4 + 1) > r3.length) goto L6;
        System.arraycopy(r3, r5, r3, r5 + 1, r4 - r5);
        r3[r5] = r6;
        return r3;
    L6:
        int[] r0 = new int[growSize(r4)];
        System.arraycopy(r3, 0, r0, 0, r5);
        r0[r5] = r6;
        System.arraycopy(r3, r5, r0, r5 + 1, r3.length - r5);
        return r0;
    }

    public static long[] insert(long[] r3, int r4, int r5, long r6) {
        if ((r4 + 1) > r3.length) goto L6;
        System.arraycopy(r3, r5, r3, r5 + 1, r4 - r5);
        r3[r5] = r6;
        return r3;
    L6:
        long[] r0 = new long[growSize(r4)];
        System.arraycopy(r3, 0, r0, 0, r5);
        r0[r5] = r6;
        System.arraycopy(r3, r5, r0, r5 + 1, r3.length - r5);
        return r0;
    }

    public static <T> T[] insert(T[] r3, int r4, int r5, T r6) {
        if ((r4 + 1) > r3.length) goto L6;
        System.arraycopy(r3, r5, r3, r5 + 1, r4 - r5);
        r3[r5] = r6;
        return r3;
    L6:
        T[] r0 = (T[]) ((Object[]) Array.newInstance(r3.getClass().getComponentType(), growSize(r4)));
        System.arraycopy(r3, 0, r0, 0, r5);
        r0[r5] = r6;
        System.arraycopy(r3, r5, r0, r5 + 1, r3.length - r5);
        return r0;
    }

    public static boolean[] insert(boolean[] r3, int r4, int r5, boolean r6) {
        if ((r4 + 1) > r3.length) goto L6;
        System.arraycopy(r3, r5, r3, r5 + 1, r4 - r5);
        r3[r5] = r6;
        return r3;
    L6:
        boolean[] r0 = new boolean[growSize(r4)];
        System.arraycopy(r3, 0, r0, 0, r5);
        r0[r5] = r6;
        System.arraycopy(r3, r5, r0, r5 + 1, r3.length - r5);
        return r0;
    }
}
