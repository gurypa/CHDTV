package com.google.common.primitives;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class UnsignedInts {
    static final long INT_MASK = 4294967295L;

    static final enum LexicographicalComparator extends Enum<LexicographicalComparator> implements Comparator<int[]> {
        private static final /* synthetic */ LexicographicalComparator[] $VALUES = null;
        public static final LexicographicalComparator INSTANCE = null;

        private static /* synthetic */ LexicographicalComparator[] $values() {
            return new LexicographicalComparator[]{INSTANCE};
        }

        static {
            INSTANCE = new LexicographicalComparator("INSTANCE", 0);
            $VALUES = $values();
        }

        private LexicographicalComparator(String r1, int r2) {
            super(r1, r2);
        }

        public static LexicographicalComparator valueOf(String r1) {
            return (LexicographicalComparator) Enum.valueOf(LexicographicalComparator.class, r1);
        }

        public static LexicographicalComparator[] values() {
            return (LexicographicalComparator[]) $VALUES.clone();
        }

        @Override // java.util.Comparator
        public /* bridge */ /* synthetic */ int compare(int[] r1, int[] r2) {
            return compare2(r1, r2);
        }

        /* JADX INFO: renamed from: compare, reason: avoid collision after fix types in other method */
        public int compare2(int[] r5, int[] r6) {
            int r0 = Math.min(r5.length, r6.length);
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L10;
            if (r5[r1] != r6[r1]) goto L7;
            r1 = r1 + 1;
            goto L3
        L7:
            return UnsignedInts.compare(r5[r1], r6[r1]);
        L10:
            return r5.length - r6.length;
        }

        @Override // java.lang.Enum
        public String toString() {
            return "UnsignedInts.lexicographicalComparator()";
        }
    }

    private UnsignedInts() {
    }

    public static int checkedCast(long r4) {
        if ((r4 >> 32) != 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "out of range: %s", r4);
        return (int) r4;
    L5:
        r0 = false;
        goto L6
    }

    public static int compare(int r2, int r3) {
        return Ints.compare(flip(r2), flip(r3));
    }

    public static int decode(String r5) {
        ParseRequest r0 = ParseRequest.fromString(r5);
        return parseUnsignedInt(r0.rawValue, r0.radix);
    L5:
        e = move-exception;
        NumberFormatException r2 = new NumberFormatException("Error parsing value: " + r5);
        r2.initCause(e);
        throw r2;
    }

    public static int divide(int r4, int r5) {
        return (int) (toLong(r4) / toLong(r5));
    }

    static int flip(int r1) {
        return Integer.MIN_VALUE ^ r1;
    }

    public static String join(String r4, int... r5) {
        Preconditions.checkNotNull(r4);
        if (r5.length != 0) goto L6;
        return "";
    L6:
        StringBuilder r0 = new StringBuilder(r5.length * 5);
        r0.append(toString(r5[0]));
        int r1 = 1;
    L8:
        if (r1 >= r5.length) goto L11;
        r0.append(r4).append(toString(r5[r1]));
        r1 = r1 + 1;
        goto L8
    L11:
        return r0.toString();
    }

    public static Comparator<int[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static int max(int... r3) {
        if (r3.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        int r1 = flip(r3[0]);
        int r2 = 1;
    L8:
        if (r2 >= r3.length) goto L14;
        int r4 = flip(r3[r2]);
        if (r4 <= r1) goto L12;
        r1 = r4;
    L12:
        r2 = r2 + 1;
        goto L8
    L14:
        return flip(r1);
    L5:
        r0 = false;
        goto L6
    }

    public static int min(int... r3) {
        if (r3.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        int r1 = flip(r3[0]);
        int r2 = 1;
    L8:
        if (r2 >= r3.length) goto L14;
        int r4 = flip(r3[r2]);
        if (r4 >= r1) goto L12;
        r1 = r4;
    L12:
        r2 = r2 + 1;
        goto L8
    L14:
        return flip(r1);
    L5:
        r0 = false;
        goto L6
    }

    public static int parseUnsignedInt(String r1) {
        return parseUnsignedInt(r1, 10);
    }

    public static int parseUnsignedInt(String r5, int r6) {
        Preconditions.checkNotNull(r5);
        long r0 = Long.parseLong(r5, r6);
        if ((INT_MASK & r0) != r0) goto L7;
        return (int) r0;
    L7:
        throw new NumberFormatException("Input " + r5 + " in base " + r6 + " is not in the range of an unsigned integer");
    }

    public static int remainder(int r4, int r5) {
        return (int) (toLong(r4) % toLong(r5));
    }

    public static int saturatedCast(long r2) {
        if (r2 > 0) goto L7;
        return 0;
    L7:
        if (r2 < 4294967296L) goto L11;
        return -1;
    L11:
        return (int) r2;
    }

    public static void sort(int[] r2) {
        Preconditions.checkNotNull(r2);
        sort(r2, 0, r2.length);
    }

    public static void sort(int[] r2, int r3, int r4) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkPositionIndexes(r3, r4, r2.length);
        int r0 = r3;
    L3:
        if (r0 >= r4) goto L5;
        r2[r0] = flip(r2[r0]);
        r0 = r0 + 1;
        goto L3
    L5:
        Arrays.sort(r2, r3, r4);
        int r1 = r3;
    L6:
        if (r1 >= r4) goto L8;
        r2[r1] = flip(r2[r1]);
        r1 = r1 + 1;
        goto L6
    }

    public static void sortDescending(int[] r2) {
        Preconditions.checkNotNull(r2);
        sortDescending(r2, 0, r2.length);
    }

    public static void sortDescending(int[] r3, int r4, int r5) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkPositionIndexes(r4, r5, r3.length);
        int r0 = r4;
    L4:
        if (r0 >= r5) goto L6;
        r3[r0] = Integer.MAX_VALUE ^ r3[r0];
        r0 = r0 + 1;
        goto L4
    L6:
        Arrays.sort(r3, r4, r5);
        int r1 = r4;
    L7:
        if (r1 >= r5) goto L9;
        r3[r1] = r3[r1] ^ Integer.MAX_VALUE;
        r1 = r1 + 1;
        goto L7
    }

    public static long toLong(int r4) {
        return ((long) r4) & INT_MASK;
    }

    public static String toString(int r1) {
        return toString(r1, 10);
    }

    public static String toString(int r4, int r5) {
        return Long.toString(((long) r4) & INT_MASK, r5);
    }
}
