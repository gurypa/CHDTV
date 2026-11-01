package com.google.common.primitives;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class SignedBytes {
    public static final byte MAX_POWER_OF_TWO = 64;

    private static final enum LexicographicalComparator extends Enum<LexicographicalComparator> implements Comparator<byte[]> {
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
        public /* bridge */ /* synthetic */ int compare(byte[] r1, byte[] r2) {
            return compare2(r1, r2);
        }

        /* JADX INFO: renamed from: compare, reason: avoid collision after fix types in other method */
        public int compare2(byte[] r5, byte[] r6) {
            int r0 = Math.min(r5.length, r6.length);
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L9;
            int r2 = Byte.compare(r5[r1], r6[r1]);
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
            return "SignedBytes.lexicographicalComparator()";
        }
    }

    private SignedBytes() {
    }

    public static byte checkedCast(long r3) {
        byte r0 = (byte) r3;
        if (r0 != r3) goto L5;
        boolean r1 = true;
    L6:
        Preconditions.checkArgument(r1, "Out of range: %s", r3);
        return r0;
    L5:
        r1 = false;
        goto L6
    }

    public static int compare(byte r1, byte r2) {
        return Byte.compare(r1, r2);
    }

    public static String join(String r4, byte... r5) {
        Preconditions.checkNotNull(r4);
        if (r5.length != 0) goto L6;
        return "";
    L6:
        StringBuilder r0 = new StringBuilder(r5.length * 5);
        r0.append(r5[0]);
        int r1 = 1;
    L8:
        if (r1 >= r5.length) goto L11;
        r0.append(r4).append(r5[r1]);
        r1 = r1 + 1;
        goto L8
    L11:
        return r0.toString();
    }

    public static Comparator<byte[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static byte max(byte... r3) {
        if (r3.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        byte r1 = r3[0];
        int r2 = 1;
    L8:
        if (r2 >= r3.length) goto L13;
        if (r3[r2] <= r1) goto L12;
        r1 = r3[r2];
    L12:
        r2 = r2 + 1;
        goto L8
    L13:
        return r1;
    L5:
        r0 = false;
        goto L6
    }

    public static byte min(byte... r3) {
        if (r3.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        byte r1 = r3[0];
        int r2 = 1;
    L8:
        if (r2 >= r3.length) goto L13;
        if (r3[r2] >= r1) goto L12;
        r1 = r3[r2];
    L12:
        r2 = r2 + 1;
        goto L8
    L13:
        return r1;
    L5:
        r0 = false;
        goto L6
    }

    public static byte saturatedCast(long r2) {
        if (r2 <= 127) goto L7;
        return 127;
    L7:
        if (r2 >= (-128)) goto L11;
        return -128;
    L11:
        return (byte) r2;
    }

    public static void sortDescending(byte[] r2) {
        Preconditions.checkNotNull(r2);
        sortDescending(r2, 0, r2.length);
    }

    public static void sortDescending(byte[] r1, int r2, int r3) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkPositionIndexes(r2, r3, r1.length);
        Arrays.sort(r1, r2, r3);
        Bytes.reverse(r1, r2, r3);
    }
}
