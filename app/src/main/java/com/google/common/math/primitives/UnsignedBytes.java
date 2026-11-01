package com.google.common.primitives;

import com.google.common.base.Preconditions;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import sun.misc.Unsafe;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class UnsignedBytes {
    public static final byte MAX_POWER_OF_TWO = -128;
    public static final byte MAX_VALUE = -1;
    private static final int UNSIGNED_MASK = 255;

    static class LexicographicalComparatorHolder {
        static final Comparator<byte[]> BEST_COMPARATOR = null;
        static final String UNSAFE_COMPARATOR_NAME = null;

        static final enum PureJavaComparator extends Enum<PureJavaComparator> implements Comparator<byte[]> {
            private static final /* synthetic */ PureJavaComparator[] $VALUES = null;
            public static final PureJavaComparator INSTANCE = null;

            private static /* synthetic */ PureJavaComparator[] $values() {
                return new PureJavaComparator[]{INSTANCE};
            }

            static {
                INSTANCE = new PureJavaComparator("INSTANCE", 0);
                $VALUES = $values();
            }

            private PureJavaComparator(String r1, int r2) {
                super(r1, r2);
            }

            public static PureJavaComparator valueOf(String r1) {
                return (PureJavaComparator) Enum.valueOf(PureJavaComparator.class, r1);
            }

            public static PureJavaComparator[] values() {
                return (PureJavaComparator[]) $VALUES.clone();
            }

            @Override // java.util.Comparator
            public /* bridge */ /* synthetic */ int compare(byte[] r1, byte[] r2) {
                return compare2(r1, r2);
            }

            /* JADX INFO: renamed from: compare, reason: avoid collision after fix types in other method */
            public int compare2(byte[] r5, byte[] r6) {
                int r0 = Math.min(r5.length, r6.length);
                int r1 = 0;
            L3:
                if (r1 >= r0) goto L9;
                int r2 = UnsignedBytes.compare(r5[r1], r6[r1]);
                if (r2 != 0) goto L6;
                r1 = r1 + 1;
                goto L3
            L6:
                return r2;
            L9:
                return r5.length - r6.length;
            }

            @Override // java.lang.Enum
            public String toString() {
                return "UnsignedBytes.lexicographicalComparator() (pure Java version)";
            }
        }

        static final enum UnsafeComparator extends Enum<UnsafeComparator> implements Comparator<byte[]> {
            private static final /* synthetic */ UnsafeComparator[] $VALUES = null;
            static final boolean BIG_ENDIAN = false;
            static final int BYTE_ARRAY_BASE_OFFSET = 0;
            public static final UnsafeComparator INSTANCE = null;
            static final Unsafe theUnsafe = null;

            private static /* synthetic */ UnsafeComparator[] $values() {
                return new UnsafeComparator[]{INSTANCE};
            }

            static {
                INSTANCE = new UnsafeComparator("INSTANCE", 0);
                $VALUES = $values();
                BIG_ENDIAN = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
                theUnsafe = getUnsafe();
                BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
                if ("64".equals(System.getProperty("sun.arch.data.model")) == false) goto L10;
                if ((BYTE_ARRAY_BASE_OFFSET % 8) != 0) goto L10;
                if (theUnsafe.arrayIndexScale(byte[].class) != 1) goto L10;
                return;
            L10:
                throw new Error();
            }

            private UnsafeComparator(String r1, int r2) {
                super(r1, r2);
            }

            private static Unsafe getUnsafe() {
                return Unsafe.getUnsafe();
            L12:
                return (Unsafe) AccessController.doPrivileged(new C08721());
            L7:
                e = move-exception;
                throw new RuntimeException("Could not initialize intrinsics", e.getCause());
            }

            public static UnsafeComparator valueOf(String r1) {
                return (UnsafeComparator) Enum.valueOf(UnsafeComparator.class, r1);
            }

            public static UnsafeComparator[] values() {
                return (UnsafeComparator[]) $VALUES.clone();
            }

            @Override // java.util.Comparator
            public /* bridge */ /* synthetic */ int compare(byte[] r1, byte[] r2) {
                return compare2(r1, r2);
            }

            /* JADX INFO: renamed from: compare, reason: avoid collision after fix types in other method */
            public int compare2(byte[] r18, byte[] r19) {
                int r3 = Math.min(r18.length, r19.length);
                int r4 = (~(8 - 1)) & r3;
                int r5 = 0;
            L3:
                if (r5 >= r4) goto L13;
                long r6 = theUnsafe.getLong(r18, ((long) BYTE_ARRAY_BASE_OFFSET) + ((long) r5));
                long r8 = theUnsafe.getLong(r19, ((long) BYTE_ARRAY_BASE_OFFSET) + ((long) r5));
                if (r6 != r8) goto L7;
                r5 = r5 + 8;
                goto L3
            L7:
                if (BIG_ENDIAN == true) goto L9;
                int r10 = Long.numberOfTrailingZeros(r6 ^ r8) & (-8);
                return ((int) ((r6 >>> r10) & 255)) - ((int) (255 & (r8 >>> r10)));
            L9:
                return C0871x7f2744bd.m371m(r6, r8);
            L13:
                if (r5 >= r3) goto L19;
                int r7 = UnsignedBytes.compare(r18[r5], r19[r5]);
                if (r7 != 0) goto L16;
                r5 = r5 + 1;
                goto L13
            L16:
                return r7;
            L19:
                return r18.length - r19.length;
            }

            @Override // java.lang.Enum
            public String toString() {
                return "UnsignedBytes.lexicographicalComparator() (sun.misc.Unsafe version)";
            }
        }

        static {
            UNSAFE_COMPARATOR_NAME = LexicographicalComparatorHolder.class.getName() + "$UnsafeComparator";
            BEST_COMPARATOR = getBestComparator();
        }

        LexicographicalComparatorHolder() {
        }

        static Comparator<byte[]> getBestComparator() {
            return (Comparator) ((Object[]) Objects.requireNonNull(Class.forName(UNSAFE_COMPARATOR_NAME).getEnumConstants()))[0];
        L6:
            return UnsignedBytes.lexicographicalComparatorJavaImpl();
        }
    }

    private UnsignedBytes() {
    }

    public static byte checkedCast(long r4) {
        if ((r4 >> 8) != 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "out of range: %s", r4);
        return (byte) r4;
    L5:
        r0 = false;
        goto L6
    }

    public static int compare(byte r2, byte r3) {
        return toInt(r2) - toInt(r3);
    }

    private static byte flip(byte r1) {
        return (byte) (r1 ^ 128);
    }

    public static String join(String r4, byte... r5) {
        Preconditions.checkNotNull(r4);
        if (r5.length != 0) goto L6;
        return "";
    L6:
        StringBuilder r0 = new StringBuilder(r5.length * (r4.length() + 3));
        r0.append(toInt(r5[0]));
        int r1 = 1;
    L8:
        if (r1 >= r5.length) goto L11;
        r0.append(r4).append(toString(r5[r1]));
        r1 = r1 + 1;
        goto L8
    L11:
        return r0.toString();
    }

    public static Comparator<byte[]> lexicographicalComparator() {
        return LexicographicalComparatorHolder.BEST_COMPARATOR;
    }

    static Comparator<byte[]> lexicographicalComparatorJavaImpl() {
        return LexicographicalComparatorHolder.PureJavaComparator.INSTANCE;
    }

    public static byte max(byte... r3) {
        if (r3.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        int r1 = toInt(r3[0]);
        int r2 = 1;
    L8:
        if (r2 >= r3.length) goto L14;
        int r4 = toInt(r3[r2]);
        if (r4 <= r1) goto L12;
        r1 = r4;
    L12:
        r2 = r2 + 1;
        goto L8
    L14:
        return (byte) r1;
    L5:
        r0 = false;
        goto L6
    }

    public static byte min(byte... r3) {
        if (r3.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        int r1 = toInt(r3[0]);
        int r2 = 1;
    L8:
        if (r2 >= r3.length) goto L14;
        int r4 = toInt(r3[r2]);
        if (r4 >= r1) goto L12;
        r1 = r4;
    L12:
        r2 = r2 + 1;
        goto L8
    L14:
        return (byte) r1;
    L5:
        r0 = false;
        goto L6
    }

    public static byte parseUnsignedByte(String r1) {
        return parseUnsignedByte(r1, 10);
    }

    public static byte parseUnsignedByte(String r4, int r5) {
        int r0 = Integer.parseInt((String) Preconditions.checkNotNull(r4), r5);
        if ((r0 >> 8) != 0) goto L7;
        return (byte) r0;
    L7:
        throw new NumberFormatException("out of range: " + r0);
    }

    public static byte saturatedCast(long r3) {
        if (r3 <= toInt((byte) -1)) goto L6;
        return -1;
    L6:
        if (r3 >= 0) goto L10;
        return 0;
    L10:
        return (byte) r3;
    }

    public static void sort(byte[] r2) {
        Preconditions.checkNotNull(r2);
        sort(r2, 0, r2.length);
    }

    public static void sort(byte[] r2, int r3, int r4) {
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

    public static void sortDescending(byte[] r2) {
        Preconditions.checkNotNull(r2);
        sortDescending(r2, 0, r2.length);
    }

    public static void sortDescending(byte[] r2, int r3, int r4) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkPositionIndexes(r3, r4, r2.length);
        int r0 = r3;
    L3:
        if (r0 >= r4) goto L5;
        r2[r0] = (byte) (r2[r0] ^ 127);
        r0 = r0 + 1;
        goto L3
    L5:
        Arrays.sort(r2, r3, r4);
        int r1 = r3;
    L6:
        if (r1 >= r4) goto L8;
        r2[r1] = (byte) (r2[r1] ^ 127);
        r1 = r1 + 1;
        goto L6
    }

    public static int toInt(byte r1) {
        return r1 & 255;
    }

    public static String toString(byte r1) {
        return toString(r1, 10);
    }

    public static String toString(byte r2, int r3) {
        if (r3 >= 2) goto L5;
    L7:
        boolean r0 = false;
    L8:
        Preconditions.checkArgument(r0, "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", r3);
        return Integer.toString(toInt(r2), r3);
    L5:
        if (r3 > 36) goto L7;
        r0 = true;
        goto L8
    }
}
