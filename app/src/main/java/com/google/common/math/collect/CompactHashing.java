package com.google.common.collect;

import com.google.common.base.Objects;
import java.util.Arrays;
import javax.annotation.CheckForNull;
import kotlin.UShort;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class CompactHashing {
    private static final int BYTE_MASK = 255;
    private static final int BYTE_MAX_SIZE = 256;
    static final int DEFAULT_SIZE = 3;
    static final int HASH_TABLE_BITS_MASK = 31;
    private static final int HASH_TABLE_BITS_MAX_BITS = 5;
    static final int MAX_SIZE = 1073741823;
    private static final int MIN_HASH_TABLE_SIZE = 4;
    static final int MODIFICATION_COUNT_INCREMENT = 32;
    private static final int SHORT_MASK = 65535;
    private static final int SHORT_MAX_SIZE = 65536;
    static final byte UNSET = 0;

    private CompactHashing() {
    }

    static Object createTable(int r3) {
        if (r3 < 2) goto L19;
        if (r3 > 1073741824) goto L19;
        if (Integer.highestOneBit(r3) != r3) goto L19;
        if (r3 > 256) goto L13;
        return new byte[r3];
    L13:
        if (r3 > 65536) goto L17;
        return new short[r3];
    L17:
        return new int[r3];
    L19:
        throw new IllegalArgumentException("must be power of 2 between 2^1 and 2^30: " + r3);
    }

    static int getHashPrefix(int r1, int r2) {
        return (~r2) & r1;
    }

    static int getNext(int r1, int r2) {
        return r1 & r2;
    }

    static int maskCombine(int r2, int r3, int r4) {
        return ((~r4) & r2) | (r3 & r4);
    }

    static int newCapacity(int r2) {
        if (r2 >= 32) goto L5;
        int r0 = 4;
    L7:
        return r0 * (r2 + 1);
    L5:
        r0 = 2;
        goto L7
    }

    static int remove(@CheckForNull Object r9, @CheckForNull Object r10, int r11, Object r12, int[] r13, Object[] r14, @CheckForNull Object[] r15) {
        int r0 = Hashing.smearedHash(r9);
        int r1 = r0 & r11;
        int r2 = tableGet(r12, r1);
        if (r2 != 0) goto L5;
        return -1;
    L5:
        int r4 = getHashPrefix(r0, r11);
        int r5 = -1;
    L6:
        int r6 = r2 - 1;
        int r7 = r13[r6];
        if (getHashPrefix(r7, r11) == r4) goto L9;
    L18:
        r5 = r6;
        r2 = getNext(r7, r11);
        if (r2 != 0) goto L6;
        return -1;
    L9:
        if (Objects.equal(r9, r14[r6]) == false) goto L18;
        if (r15 == null) goto L13;
        if (Objects.equal(r10, r15[r6]) == false) goto L18;
    L13:
        int r8 = getNext(r7, r11);
        if (r5 != (-1)) goto L16;
        tableSet(r12, r1, r8);
    L17:
        return r6;
    L16:
        r13[r5] = maskCombine(r13[r5], r8, r11);
        goto L17
    }

    static void tableClear(Object r2) {
        if ((r2 instanceof byte[]) == false) goto L6;
        Arrays.fill((byte[]) r2, (byte) 0);
        return;
    L6:
        if ((r2 instanceof short[]) == false) goto L8;
        Arrays.fill((short[]) r2, 0);
        return;
    L8:
        Arrays.fill((int[]) r2, 0);
    }

    static int tableGet(Object r2, int r3) {
        if ((r2 instanceof byte[]) == false) goto L7;
        return ((byte[]) r2)[r3] & 255;
    L7:
        if ((r2 instanceof short[]) == false) goto L11;
        return ((short[]) r2)[r3] & UShort.MAX_VALUE;
    L11:
        return ((int[]) r2)[r3];
    }

    static void tableSet(Object r2, int r3, int r4) {
        if ((r2 instanceof byte[]) == false) goto L6;
        ((byte[]) r2)[r3] = (byte) r4;
        return;
    L6:
        if ((r2 instanceof short[]) == false) goto L8;
        ((short[]) r2)[r3] = (short) r4;
        return;
    L8:
        ((int[]) r2)[r3] = r4;
    }

    static int tableSize(int r3) {
        return Math.max(4, Hashing.closedTableSize(r3 + 1, 1.0d));
    }
}
