package com.google.common.primitives;

import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Longs {
    public static final int BYTES = 8;
    public static final long MAX_POWER_OF_TWO = 4611686018427387904L;

    static final class AsciiDigits {
        private static final byte[] asciiDigits = null;

        static {
            byte[] r0 = new byte[128];
            Arrays.fill(r0, (byte) -1);
            int r1 = 0;
        L4:
            if (r1 >= 10) goto L6;
            r0[r1 + 48] = (byte) r1;
            r1 = r1 + 1;
            goto L4
        L6:
            int r2 = 0;
        L8:
            if (r2 >= 26) goto L10;
            r0[r2 + 65] = (byte) (r2 + 10);
            r0[r2 + 97] = (byte) (r2 + 10);
            r2 = r2 + 1;
            goto L8
        L10:
            asciiDigits = r0;
        }

        private AsciiDigits() {
        }

        static int digit(char r1) {
            if (r1 < 128) goto L7;
            return -1;
        L7:
            return asciiDigits[r1];
        }
    }

    private static final enum LexicographicalComparator extends Enum<LexicographicalComparator> implements Comparator<long[]> {
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
            if (r1 >= r0) goto L9;
            int r2 = Long.compare(r7[r1], r8[r1]);
            if (r2 != 0) goto L6;
            r1 = r1 + 1;
            goto L3
        L6:
            return r2;
        L9:
            return r7.length - r8.length;
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Longs.lexicographicalComparator()";
        }
    }

    private static class LongArrayAsList extends AbstractList<Long> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final long[] array;
        final int end;
        final int start;

        LongArrayAsList(long[] r3) {
            this(r3, 0, r3.length);
        }

        LongArrayAsList(long[] r1, int r2, int r3) {
            this.array = r1;
            this.start = r2;
            this.end = r3;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean contains(@CheckForNull Object r6) {
            if ((r6 instanceof Long) == true) goto L5;
        L7:
            return false;
        L5:
            if (Longs.access$000(this.array, ((Long) r6).longValue(), this.start, this.end) == (-1)) goto L7;
            return true;
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public boolean equals(@CheckForNull Object r11) {
            if (r11 != this) goto L6;
            return true;
        L6:
            if ((r11 instanceof LongArrayAsList) == false) goto L18;
            LongArrayAsList r1 = (LongArrayAsList) r11;
            int r2 = size();
            if (r1.size() == r2) goto L10;
            return false;
        L10:
            int r3 = 0;
        L11:
            if (r3 >= r2) goto L16;
            if (this.array[this.start + r3] != r1.array[r1.start + r3]) goto L14;
            r3 = r3 + 1;
            goto L11
        L14:
            return false;
        L16:
            return true;
        L18:
            return super.equals(r11);
        }

        @Override // java.util.AbstractList, java.util.List
        public Long get(int r4) {
            Preconditions.checkElementIndex(r4, size());
            return Long.valueOf(this.array[this.start + r4]);
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public int hashCode() {
            int r0 = 1;
            int r1 = this.start;
        L4:
            if (r1 >= this.end) goto L6;
            r0 = (r0 * 31) + Longs.hashCode(this.array[r1]);
            r1 = r1 + 1;
            goto L4
        L6:
            return r0;
        }

        @Override // java.util.AbstractList, java.util.List
        public int indexOf(@CheckForNull Object r6) {
            if ((r6 instanceof Long) == false) goto L8;
            int r0 = Longs.access$000(this.array, ((Long) r6).longValue(), this.start, this.end);
            if (r0 >= 0) goto L7;
            return -1;
        L7:
            return r0 - this.start;
        L8:
            return -1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.AbstractList, java.util.List
        public int lastIndexOf(@CheckForNull Object r6) {
            if ((r6 instanceof Long) == false) goto L8;
            int r0 = Longs.access$100(this.array, ((Long) r6).longValue(), this.start, this.end);
            if (r0 >= 0) goto L7;
            return -1;
        L7:
            return r0 - this.start;
        L8:
            return -1;
        }

        public Long set(int r7, Long r8) {
            Preconditions.checkElementIndex(r7, size());
            long r1 = this.array[this.start + r7];
            this.array[this.start + r7] = ((Long) Preconditions.checkNotNull(r8)).longValue();
            return Long.valueOf(r1);
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object set(int r1, Object r2) {
            return set(r1, (Long) r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.end - this.start;
        }

        @Override // java.util.AbstractList, java.util.List
        public List<Long> subList(int r6, int r7) {
            Preconditions.checkPositionIndexes(r6, r7, size());
            if (r6 != r7) goto L7;
            return Collections.emptyList();
        L7:
            return new LongArrayAsList(this.array, this.start + r6, this.start + r7);
        }

        long[] toLongArray() {
            return Arrays.copyOfRange(this.array, this.start, this.end);
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            StringBuilder r0 = new StringBuilder(size() * 10);
            r0.append('[').append(this.array[this.start]);
            int r1 = this.start;
        L3:
            r1 = r1 + 1;
            if (r1 >= this.end) goto L7;
            r0.append(", ").append(this.array[r1]);
            goto L3
        L7:
            return r0.append(']').toString();
        }
    }

    private static final class LongConverter extends Converter<String, Long> implements Serializable {
        static final Converter<String, Long> INSTANCE = null;
        private static final long serialVersionUID = 1;

        static {
            INSTANCE = new LongConverter();
        }

        private LongConverter() {
        }

        private Object readResolve() {
            return INSTANCE;
        }

        @Override // com.google.common.base.Converter
        protected /* bridge */ /* synthetic */ String doBackward(Long r1) {
            return doBackward2(r1);
        }

        /* JADX INFO: renamed from: doBackward, reason: avoid collision after fix types in other method */
        protected String doBackward2(Long r2) {
            return r2.toString();
        }

        /* JADX INFO: renamed from: doForward, reason: avoid collision after fix types in other method */
        protected Long doForward2(String r2) {
            return Long.decode(r2);
        }

        @Override // com.google.common.base.Converter
        protected /* bridge */ /* synthetic */ Long doForward(String r1) {
            return doForward2(r1);
        }

        public String toString() {
            return "Longs.stringConverter()";
        }
    }

    private Longs() {
    }

    static /* synthetic */ int access$000(long[] r1, long r2, int r4, int r5) {
        return indexOf(r1, r2, r4, r5);
    }

    static /* synthetic */ int access$100(long[] r1, long r2, int r4, int r5) {
        return lastIndexOf(r1, r2, r4, r5);
    }

    public static List<Long> asList(long... r1) {
        if (r1.length != 0) goto L7;
        return Collections.emptyList();
    L7:
        return new LongArrayAsList(r1);
    }

    private static int checkNoOverflow(long r2) {
        if (r2 != ((int) r2)) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "the total number of elements (%s) in the arrays must fit in an int", r2);
        return (int) r2;
    L5:
        r0 = false;
        goto L6
    }

    public static int compare(long r1, long r3) {
        return Long.compare(r1, r3);
    }

    public static long[] concat(long[]... r9) {
        long r0 = 0;
        int r2 = r9.length;
        int r4 = 0;
    L3:
        if (r4 >= r2) goto L5;
        r0 = r0 + ((long) r9[r4].length);
        r4 = r4 + 1;
        goto L3
    L5:
        long[] r3 = new long[checkNoOverflow(r0)];
        int r5 = 0;
        int r6 = r9.length;
        int r7 = 0;
    L6:
        if (r7 >= r6) goto L8;
        long[] r8 = r9[r7];
        System.arraycopy(r8, 0, r3, r5, r8.length);
        r5 = r5 + r8.length;
        r7 = r7 + 1;
        goto L6
    L8:
        return r3;
    }

    public static long constrainToRange(long r7, long r9, long r11) {
        if (r9 > r11) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "min (%s) must be less than or equal to max (%s)", r9, r11);
        return Math.min(Math.max(r7, r9), r11);
    L5:
        r0 = false;
        goto L6
    }

    public static boolean contains(long[] r6, long r7) {
        int r0 = r6.length;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L9;
        if (r6[r2] == r7) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    public static long[] ensureCapacity(long[] r4, int r5, int r6) {
        boolean r0 = true;
        if (r5 < 0) goto L5;
        boolean r2 = true;
    L6:
        Preconditions.checkArgument(r2, "Invalid minLength: %s", r5);
        if (r6 >= 0) goto L10;
        r0 = false;
    L10:
        Preconditions.checkArgument(r0, "Invalid padding: %s", r6);
        if (r4.length >= r5) goto L14;
        return Arrays.copyOf(r4, r5 + r6);
    L14:
        return r4;
    L5:
        r2 = false;
        goto L6
    }

    public static long fromByteArray(byte[] r14) {
        if (r14.length < 8) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "array too small: %s < %s", r14.length, 8);
        return fromBytes(r14[0], r14[1], r14[2], r14[3], r14[4], r14[5], r14[6], r14[7]);
    L5:
        r0 = false;
        goto L6
    }

    public static long fromBytes(byte r7, byte r8, byte r9, byte r10, byte r11, byte r12, byte r13, byte r14) {
        return ((((((((((long) r7) & 255) << 56) | ((((long) r8) & 255) << 48)) | ((((long) r9) & 255) << 40)) | ((((long) r10) & 255) << 32)) | ((((long) r11) & 255) << 24)) | ((((long) r12) & 255) << 16)) | ((((long) r13) & 255) << 8)) | (255 & ((long) r14));
    }

    public static int hashCode(long r2) {
        return (int) ((r2 >>> 32) ^ r2);
    }

    public static int indexOf(long[] r2, long r3) {
        return indexOf(r2, r3, 0, r2.length);
    }

    private static int indexOf(long[] r3, long r4, int r6, int r7) {
        int r0 = r6;
    L3:
        if (r0 >= r7) goto L8;
        if (r3[r0] == r4) goto L6;
        r0 = r0 + 1;
        goto L3
    L6:
        return r0;
    L8:
        return -1;
    }

    public static int indexOf(long[] r6, long[] r7) {
        Preconditions.checkNotNull(r6, "array");
        Preconditions.checkNotNull(r7, "target");
        if (r7.length != 0) goto L6;
        return 0;
    L6:
        int r0 = 0;
    L8:
        if (r0 >= ((r6.length - r7.length) + 1)) goto L17;
        int r1 = 0;
    L11:
        if (r1 >= r7.length) goto L16;
        if (r6[r0 + r1] != r7[r1]) goto L14;
        r1 = r1 + 1;
        goto L11
    L14:
        r0 = r0 + 1;
        goto L8
    L16:
        return r0;
    L17:
        return -1;
    }

    public static String join(String r5, long... r6) {
        Preconditions.checkNotNull(r5);
        if (r6.length != 0) goto L6;
        return "";
    L6:
        StringBuilder r0 = new StringBuilder(r6.length * 10);
        r0.append(r6[0]);
        int r1 = 1;
    L8:
        if (r1 >= r6.length) goto L11;
        r0.append(r5).append(r6[r1]);
        r1 = r1 + 1;
        goto L8
    L11:
        return r0.toString();
    }

    public static int lastIndexOf(long[] r2, long r3) {
        return lastIndexOf(r2, r3, 0, r2.length);
    }

    private static int lastIndexOf(long[] r3, long r4, int r6, int r7) {
        int r0 = r7 - 1;
    L3:
        if (r0 < r6) goto L8;
        if (r3[r0] == r4) goto L6;
        r0 = r0 - 1;
        goto L3
    L6:
        return r0;
    L8:
        return -1;
    }

    public static Comparator<long[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static long max(long... r5) {
        if (r5.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        long r1 = r5[0];
        int r2 = 1;
    L8:
        if (r2 >= r5.length) goto L13;
        if (r5[r2] <= r1) goto L12;
        r1 = r5[r2];
    L12:
        r2 = r2 + 1;
        goto L8
    L13:
        return r1;
    L5:
        r0 = false;
        goto L6
    }

    public static long min(long... r5) {
        if (r5.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        long r1 = r5[0];
        int r2 = 1;
    L8:
        if (r2 >= r5.length) goto L13;
        if (r5[r2] >= r1) goto L12;
        r1 = r5[r2];
    L12:
        r2 = r2 + 1;
        goto L8
    L13:
        return r1;
    L5:
        r0 = false;
        goto L6
    }

    public static void reverse(long[] r2) {
        Preconditions.checkNotNull(r2);
        reverse(r2, 0, r2.length);
    }

    public static void reverse(long[] r6, int r7, int r8) {
        Preconditions.checkNotNull(r6);
        Preconditions.checkPositionIndexes(r7, r8, r6.length);
        int r0 = r7;
        int r1 = r8 - 1;
    L3:
        if (r0 >= r1) goto L5;
        long r2 = r6[r0];
        r6[r0] = r6[r1];
        r6[r1] = r2;
        r0 = r0 + 1;
        r1 = r1 - 1;
        goto L3
    }

    public static void rotate(long[] r2, int r3) {
        rotate(r2, r3, 0, r2.length);
    }

    public static void rotate(long[] r3, int r4, int r5, int r6) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkPositionIndexes(r5, r6, r3.length);
        if (r3.length > 1) goto L5;
        return;
    L5:
        int r0 = r6 - r5;
        int r1 = (-r4) % r0;
        if (r1 >= 0) goto L8;
        int r2 = r1 + r0;
    L9:
        int r7 = r2 + r5;
        if (r7 != r5) goto L12;
        return;
    L12:
        reverse(r3, r5, r7);
        reverse(r3, r7, r6);
        reverse(r3, r5, r6);
        return;
    L8:
        r2 = r1;
        goto L9
    }

    public static void sortDescending(long[] r2) {
        Preconditions.checkNotNull(r2);
        sortDescending(r2, 0, r2.length);
    }

    public static void sortDescending(long[] r1, int r2, int r3) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkPositionIndexes(r2, r3, r1.length);
        Arrays.sort(r1, r2, r3);
        reverse(r1, r2, r3);
    }

    public static Converter<String, Long> stringConverter() {
        return LongConverter.INSTANCE;
    }

    public static long[] toArray(Collection<? extends Number> r6) {
        if ((r6 instanceof LongArrayAsList) == true) goto L5;
        Object[] r0 = r6.toArray();
        int r1 = r0.length;
        long[] r2 = new long[r1];
        int r3 = 0;
    L7:
        if (r3 >= r1) goto L9;
        r2[r3] = ((Number) Preconditions.checkNotNull(r0[r3])).longValue();
        r3 = r3 + 1;
        goto L7
    L9:
        return r2;
    L5:
        return ((LongArrayAsList) r6).toLongArray();
    }

    public static byte[] toByteArray(long r5) {
        byte[] r1 = new byte[8];
        int r2 = 7;
    L3:
        if (r2 < 0) goto L5;
        r1[r2] = (byte) (255 & r5);
        r5 = r5 >> 8;
        r2 = r2 - 1;
        goto L3
    L5:
        return r1;
    }

    @CheckForNull
    public static Long tryParse(String r1) {
        return tryParse(r1, 10);
    }

    @CheckForNull
    public static Long tryParse(String r13, int r14) {
        if (((String) Preconditions.checkNotNull(r13)).isEmpty() == false) goto L6;
        return null;
    L6:
        if (r14 < 2) goto L46;
        if (r14 > 36) goto L46;
        int r0 = 0;
        if (r13.charAt(0) != '-') goto L12;
        boolean r2 = true;
    L13:
        if (r2 == false) goto L16;
        r0 = 1;
    L16:
        if (r0 != r13.length()) goto L18;
        return null;
    L18:
        int r3 = r0 + 1;
        int r1 = AsciiDigits.digit(r13.charAt(r0));
        if (r1 < 0) goto L44;
        if (r1 >= r14) goto L44;
        long r4 = -r1;
        long r6 = Long.MIN_VALUE / ((long) r14);
    L24:
        if (r3 >= r13.length()) goto L36;
        int r10 = r3 + 1;
        int r5 = AsciiDigits.digit(r13.charAt(r3));
        if (r5 < 0) goto L35;
        if (r5 >= r14) goto L35;
        if (r4 < r6) goto L35;
        long r7 = r4 * ((long) r14);
        if (r7 < (((long) r5) - Long.MIN_VALUE)) goto L33;
        r4 = r7 - ((long) r5);
        r3 = r10;
        goto L24
    L33:
        return null;
    L35:
        return null;
    L36:
        if (r2 == false) goto L40;
        return Long.valueOf(r4);
    L40:
        if (r4 != Long.MIN_VALUE) goto L43;
        return null;
    L43:
        return Long.valueOf(-r4);
    L44:
        return null;
    L12:
        r2 = false;
    L46:
        throw new IllegalArgumentException("radix must be between MIN_RADIX and MAX_RADIX but was " + r14);
    }
}
