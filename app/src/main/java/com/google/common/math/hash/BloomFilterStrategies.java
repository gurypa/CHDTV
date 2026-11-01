package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLongArray;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract enum BloomFilterStrategies extends Enum<BloomFilterStrategies> implements BloomFilter.Strategy {
    private static final /* synthetic */ BloomFilterStrategies[] $VALUES = null;
    public static final BloomFilterStrategies MURMUR128_MITZ_32 = null;
    public static final BloomFilterStrategies MURMUR128_MITZ_64 = null;

    static final class LockFreeBitArray {
        private static final int LONG_ADDRESSABLE_BITS = 6;
        private final LongAddable bitCount;
        final AtomicLongArray data;

        LockFreeBitArray(long r5) {
            if (r5 <= 0) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "data length is zero!");
            this.data = new AtomicLongArray(Ints.checkedCast(LongMath.divide(r5, 64, RoundingMode.CEILING)));
            this.bitCount = LongAddables.create();
            return;
        L5:
            r0 = false;
            goto L6
        }

        LockFreeBitArray(long[] r9) {
            int r1 = 0;
            if (r9.length <= 0) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "data length is zero!");
            this.data = new AtomicLongArray(r9);
            this.bitCount = LongAddables.create();
            long r2 = 0;
            int r3 = r9.length;
        L7:
            if (r1 >= r3) goto L9;
            r2 = r2 + ((long) Long.bitCount(r9[r1]));
            r1 = r1 + 1;
            goto L7
        L9:
            this.bitCount.add(r2);
            return;
        L5:
            r0 = false;
            goto L6
        }

        public static long[] toPlainArray(AtomicLongArray r4) {
            long[] r0 = new long[r4.length()];
            int r1 = 0;
        L4:
            if (r1 >= r0.length) goto L6;
            r0[r1] = r4.get(r1);
            r1 = r1 + 1;
            goto L4
        L6:
            return r0;
        }

        long bitCount() {
            return this.bitCount.sum();
        }

        long bitSize() {
            return ((long) this.data.length()) * 64;
        }

        LockFreeBitArray copy() {
            return new LockFreeBitArray(toPlainArray(this.data));
        }

        int dataLength() {
            return this.data.length();
        }

        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof LockFreeBitArray) == true) goto L5;
            return false;
        L5:
            return Arrays.equals(toPlainArray(this.data), toPlainArray(((LockFreeBitArray) r4).data));
        }

        boolean get(long r6) {
            if ((this.data.get((int) (r6 >>> 6)) & (1 << ((int) r6))) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        public int hashCode() {
            return Arrays.hashCode(toPlainArray(this.data));
        }

        void putAll(LockFreeBitArray r5) {
            if (this.data.length() != r5.data.length()) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "BitArrays must be of equal length (%s != %s)", this.data.length(), r5.data.length());
            int r1 = 0;
        L8:
            if (r1 >= this.data.length()) goto L10;
            putData(r1, r5.data.get(r1));
            r1 = r1 + 1;
            goto L8
        L10:
            return;
        L5:
            r0 = false;
            goto L6
        }

        void putData(int r11, long r12) {
            boolean r0 = true;
        L3:
            long r4 = this.data.get(r11);
            long r6 = r4 | r12;
            if (r4 == r6) goto L5;
            int r3 = r11;
            if (this.data.compareAndSet(r3, r4, r6) == true) goto L8;
            r11 = r3;
        L8:
            if (r0 == false) goto L14;
            this.bitCount.add(Long.bitCount(r6) - Long.bitCount(r4));
            return;
        L14:
            return;
        L5:
            r0 = false;
            goto L8
        }

        boolean set(long r11) {
            if (get(r11) == false) goto L5;
            return false;
        L5:
            int r5 = (int) (r11 >>> 6);
            long r2 = 1 << ((int) r11);
        L6:
            long r6 = this.data.get(r5);
            long r8 = r6 | r2;
            if (r6 == r8) goto L8;
            if (this.data.compareAndSet(r5, r6, r8) == false) goto L6;
            this.bitCount.increment();
            return true;
        L8:
            return false;
        }
    }

    private static /* synthetic */ BloomFilterStrategies[] $values() {
        return new BloomFilterStrategies[]{MURMUR128_MITZ_32, MURMUR128_MITZ_64};
    }

    static {
        final String r1 = "MURMUR128_MITZ_32";
        final int r2 = 0;
        MURMUR128_MITZ_32 = new C08231(r1, r2);
        final String r3 = "MURMUR128_MITZ_64";
        final int r4 = 1;
        MURMUR128_MITZ_64 = new C08242(r3, r4);
        $VALUES = $values();
    }

    private BloomFilterStrategies(String r1, int r2) {
        super(r1, r2);
    }

    /* synthetic */ BloomFilterStrategies(String r1, int r2, C08231 r3) {
        this(r1, r2);
    }

    public static BloomFilterStrategies valueOf(String r1) {
        return (BloomFilterStrategies) Enum.valueOf(BloomFilterStrategies.class, r1);
    }

    public static BloomFilterStrategies[] values() {
        return (BloomFilterStrategies[]) $VALUES.clone();
    }
}
