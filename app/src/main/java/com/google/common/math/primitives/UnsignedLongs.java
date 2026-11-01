package com.google.common.primitives;

import com.google.common.base.Preconditions;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class UnsignedLongs {
    public static final long MAX_VALUE = -1;

    static final enum LexicographicalComparator extends Enum<LexicographicalComparator> implements Comparator<long[]> {
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
        public /* bridge */ /* synthetic */ int compare(long[] r1, long[] r2) {
            return compare2(r1, r2);
        }

        /* JADX INFO: renamed from: compare, reason: avoid collision after fix types in other method */
        public int compare2(long[] r7, long[] r8) {
            int r0 = Math.min(r7.length, r8.length);
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L10;
            if (r7[r1] != r8[r1]) goto L7;
            r1 = r1 + 1;
            goto L3
        L7:
            return UnsignedLongs.compare(r7[r1], r8[r1]);
        L10:
            return r7.length - r8.length;
        }

        @Override // java.lang.Enum
        public String toString() {
            return "UnsignedLongs.lexicographicalComparator()";
        }
    }

    private static final class ParseOverflowDetection {
        static final int[] maxSafeDigits = null;
        static final long[] maxValueDivs = null;
        static final int[] maxValueMods = null;

        static {
            maxValueDivs = new long[37];
            maxValueMods = new int[37];
            maxSafeDigits = new int[37];
            BigInteger r0 = new BigInteger("10000000000000000", 16);
            int r1 = 2;
        L4:
            if (r1 > 36) goto L6;
            maxValueDivs[r1] = UnsignedLongs.divide(-1, r1);
            maxValueMods[r1] = (int) UnsignedLongs.remainder(-1, r1);
            maxSafeDigits[r1] = r0.toString(r1).length() - 1;
            r1 = r1 + 1;
            goto L4
        }

        private ParseOverflowDetection() {
        }

        static boolean overflowInParse(long r5, int r7, int r8) {
            if (r5 >= 0) goto L5;
            return true;
        L5:
            if (r5 >= maxValueDivs[r8]) goto L8;
            return false;
        L8:
            if (r5 <= maxValueDivs[r8]) goto L11;
            return true;
        L11:
            if (r7 <= maxValueMods[r8]) goto L14;
            return true;
        L14:
            return false;
        }
    }

    private UnsignedLongs() {
    }

    public static int compare(long r4, long r6) {
        return Longs.compare(flip(r4), flip(r6));
    }

    public static long decode(String r5) {
        ParseRequest r0 = ParseRequest.fromString(r5);
        return parseUnsignedLong(r0.rawValue, r0.radix);
    L5:
        e = move-exception;
        NumberFormatException r2 = new NumberFormatException("Error parsing value: " + r5);
        r2.initCause(e);
        throw r2;
    }

    public static long divide(long r7, long r9) {
        if (r9 >= 0) goto L10;
        if (compare(r7, r9) >= 0) goto L7;
        return 0;
    L7:
        return 1;
    L10:
        if (r7 >= 0) goto L12;
        int r0 = 1;
        long r1 = ((r7 >>> 1) / r9) << 1;
        if (compare(r7 - (r1 * r9), r9) >= 0) goto L18;
        r0 = 0;
    L18:
        return ((long) r0) + r1;
    L12:
        return r7 / r9;
    }

    private static long flip(long r2) {
        return Long.MIN_VALUE ^ r2;
    }

    public static String join(String r5, long... r6) {
        Preconditions.checkNotNull(r5);
        if (r6.length != 0) goto L6;
        return "";
    L6:
        StringBuilder r0 = new StringBuilder(r6.length * 5);
        r0.append(toString(r6[0]));
        int r1 = 1;
    L8:
        if (r1 >= r6.length) goto L11;
        r0.append(r5).append(toString(r6[r1]));
        r1 = r1 + 1;
        goto L8
    L11:
        return r0.toString();
    }

    public static Comparator<long[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static long max(long... r6) {
        if (r6.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        long r1 = flip(r6[0]);
        int r2 = 1;
    L8:
        if (r2 >= r6.length) goto L14;
        long r3 = flip(r6[r2]);
        if (r3 <= r1) goto L12;
        r1 = r3;
    L12:
        r2 = r2 + 1;
        goto L8
    L14:
        return flip(r1);
    L5:
        r0 = false;
        goto L6
    }

    public static long min(long... r6) {
        if (r6.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        long r1 = flip(r6[0]);
        int r2 = 1;
    L8:
        if (r2 >= r6.length) goto L14;
        long r3 = flip(r6[r2]);
        if (r3 >= r1) goto L12;
        r1 = r3;
    L12:
        r2 = r2 + 1;
        goto L8
    L14:
        return flip(r1);
    L5:
        r0 = false;
        goto L6
    }

    public static long parseUnsignedLong(String r2) {
        return parseUnsignedLong(r2, 10);
    }

    public static long parseUnsignedLong(String r9, int r10) {
        Preconditions.checkNotNull(r9);
        if (r9.length() == 0) goto L26;
        if (r10 < 2) goto L24;
        if (r10 > 36) goto L24;
        int r0 = ParseOverflowDetection.maxSafeDigits[r10] - 1;
        long r1 = 0;
        int r3 = 0;
    L10:
        if (r3 >= r9.length()) goto L22;
        int r4 = Character.digit(r9.charAt(r3), r10);
        if (r4 == (-1)) goto L21;
        if (r3 <= r0) goto L19;
        if (ParseOverflowDetection.overflowInParse(r1, r4, r10) == false) goto L19;
        throw new NumberFormatException("Too large for unsigned long: " + r9);
    L19:
        r1 = (((long) r10) * r1) + ((long) r4);
        r3 = r3 + 1;
        goto L10
    L21:
        throw new NumberFormatException(r9);
    L22:
        return r1;
    L24:
        throw new NumberFormatException("illegal radix: " + r10);
    L26:
        throw new NumberFormatException("empty string");
    }

    public static long remainder(long r7, long r9) {
        long r0 = 0;
        if (r9 >= 0) goto L10;
        if (compare(r7, r9) >= 0) goto L8;
        return r7;
    L8:
        return r7 - r9;
    L10:
        if (r7 >= 0) goto L12;
        long r4 = r7 - ((((r7 >>> 1) / r9) << 1) * r9);
        if (compare(r4, r9) < 0) goto L17;
        r0 = r9;
    L17:
        return r4 - r0;
    L12:
        return r7 % r9;
    }

    public static void sort(long[] r2) {
        Preconditions.checkNotNull(r2);
        sort(r2, 0, r2.length);
    }

    public static void sort(long[] r3, int r4, int r5) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkPositionIndexes(r4, r5, r3.length);
        int r0 = r4;
    L3:
        if (r0 >= r5) goto L5;
        r3[r0] = flip(r3[r0]);
        r0 = r0 + 1;
        goto L3
    L5:
        Arrays.sort(r3, r4, r5);
        int r1 = r4;
    L6:
        if (r1 >= r5) goto L8;
        r3[r1] = flip(r3[r1]);
        r1 = r1 + 1;
        goto L6
    }

    public static void sortDescending(long[] r2) {
        Preconditions.checkNotNull(r2);
        sortDescending(r2, 0, r2.length);
    }

    public static void sortDescending(long[] r5, int r6, int r7) {
        Preconditions.checkNotNull(r5);
        Preconditions.checkPositionIndexes(r6, r7, r5.length);
        int r0 = r6;
    L4:
        if (r0 >= r7) goto L6;
        r5[r0] = Long.MAX_VALUE ^ r5[r0];
        r0 = r0 + 1;
        goto L4
    L6:
        Arrays.sort(r5, r6, r7);
        int r1 = r6;
    L7:
        if (r1 >= r7) goto L9;
        r5[r1] = r5[r1] ^ Long.MAX_VALUE;
        r1 = r1 + 1;
        goto L7
    }

    public static String toString(long r1) {
        return toString(r1, 10);
    }

    public static String toString(long r11, int r13) {
        if (r13 >= 2) goto L5;
    L7:
        boolean r0 = false;
    L8:
        Preconditions.checkArgument(r0, "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", r13);
        if (r11 != 0) goto L13;
        return "0";
    L13:
        if (r11 > 0) goto L15;
        char[] r1 = new char[64];
        int r4 = r1.length;
        if (((r13 - 1) & r13) != 0) goto L23;
        int r5 = Integer.numberOfTrailingZeros(r13);
        int r6 = r13 - 1;
    L19:
        r4 = r4 - 1;
        r1[r4] = Character.forDigit(((int) r11) & r6, r13);
        r11 = r11 >>> r5;
        if (r11 != 0) goto L19;
    L31:
        return new String(r1, r4, r1.length - r4);
    L23:
        if ((r13 & 1) != 0) goto L25;
        long r7 = (r11 >>> 1) / ((long) (r13 >>> 1));
    L26:
        r4 = r4 - 1;
        r1[r4] = Character.forDigit((int) (r11 - (((long) r13) * r7)), r13);
        long r12 = r7;
    L28:
        if (r12 <= 0) goto L31;
        r4 = r4 - 1;
        r1[r4] = Character.forDigit((int) (r12 % ((long) r13)), r13);
        r12 = r12 / ((long) r13);
        goto L28
    L25:
        r7 = divide(r11, r13);
        goto L26
    L15:
        return Long.toString(r11, r13);
    L5:
        if (r13 > 36) goto L7;
        r0 = true;
        goto L8
    }
}
