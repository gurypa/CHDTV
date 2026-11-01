package com.google.common.primitives;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Bytes {

    private static class ByteArrayAsList extends AbstractList<Byte> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final byte[] array;
        final int end;
        final int start;

        ByteArrayAsList(byte[] r3) {
            this(r3, 0, r3.length);
        }

        ByteArrayAsList(byte[] r1, int r2, int r3) {
            this.array = r1;
            this.start = r2;
            this.end = r3;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean contains(@CheckForNull Object r5) {
            if ((r5 instanceof Byte) == true) goto L5;
        L7:
            return false;
        L5:
            if (Bytes.access$000(this.array, ((Byte) r5).byteValue(), this.start, this.end) == (-1)) goto L7;
            return true;
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public boolean equals(@CheckForNull Object r9) {
            if (r9 != this) goto L6;
            return true;
        L6:
            if ((r9 instanceof ByteArrayAsList) == false) goto L18;
            ByteArrayAsList r1 = (ByteArrayAsList) r9;
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
            return super.equals(r9);
        }

        @Override // java.util.AbstractList, java.util.List
        public Byte get(int r3) {
            Preconditions.checkElementIndex(r3, size());
            return Byte.valueOf(this.array[this.start + r3]);
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
            r0 = (r0 * 31) + Bytes.hashCode(this.array[r1]);
            r1 = r1 + 1;
            goto L4
        L6:
            return r0;
        }

        @Override // java.util.AbstractList, java.util.List
        public int indexOf(@CheckForNull Object r5) {
            if ((r5 instanceof Byte) == false) goto L8;
            int r0 = Bytes.access$000(this.array, ((Byte) r5).byteValue(), this.start, this.end);
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
        public int lastIndexOf(@CheckForNull Object r5) {
            if ((r5 instanceof Byte) == false) goto L8;
            int r0 = Bytes.access$100(this.array, ((Byte) r5).byteValue(), this.start, this.end);
            if (r0 >= 0) goto L7;
            return -1;
        L7:
            return r0 - this.start;
        L8:
            return -1;
        }

        public Byte set(int r5, Byte r6) {
            Preconditions.checkElementIndex(r5, size());
            byte r0 = this.array[this.start + r5];
            this.array[this.start + r5] = ((Byte) Preconditions.checkNotNull(r6)).byteValue();
            return Byte.valueOf(r0);
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object set(int r1, Object r2) {
            return set(r1, (Byte) r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.end - this.start;
        }

        @Override // java.util.AbstractList, java.util.List
        public List<Byte> subList(int r6, int r7) {
            Preconditions.checkPositionIndexes(r6, r7, size());
            if (r6 != r7) goto L7;
            return Collections.emptyList();
        L7:
            return new ByteArrayAsList(this.array, this.start + r6, this.start + r7);
        }

        byte[] toByteArray() {
            return Arrays.copyOfRange(this.array, this.start, this.end);
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            StringBuilder r0 = new StringBuilder(size() * 5);
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

    private Bytes() {
    }

    static /* synthetic */ int access$000(byte[] r1, byte r2, int r3, int r4) {
        return indexOf(r1, r2, r3, r4);
    }

    static /* synthetic */ int access$100(byte[] r1, byte r2, int r3, int r4) {
        return lastIndexOf(r1, r2, r3, r4);
    }

    public static List<Byte> asList(byte... r1) {
        if (r1.length != 0) goto L7;
        return Collections.emptyList();
    L7:
        return new ByteArrayAsList(r1);
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

    public static byte[] concat(byte[]... r9) {
        long r0 = 0;
        int r2 = r9.length;
        int r4 = 0;
    L3:
        if (r4 >= r2) goto L5;
        r0 = r0 + ((long) r9[r4].length);
        r4 = r4 + 1;
        goto L3
    L5:
        byte[] r3 = new byte[checkNoOverflow(r0)];
        int r5 = 0;
        int r6 = r9.length;
        int r7 = 0;
    L6:
        if (r7 >= r6) goto L8;
        byte[] r8 = r9[r7];
        System.arraycopy(r8, 0, r3, r5, r8.length);
        r5 = r5 + r8.length;
        r7 = r7 + 1;
        goto L6
    L8:
        return r3;
    }

    public static boolean contains(byte[] r4, byte r5) {
        int r0 = r4.length;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L9;
        if (r4[r2] == r5) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    public static byte[] ensureCapacity(byte[] r4, int r5, int r6) {
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

    public static int hashCode(byte r0) {
        return r0;
    }

    public static int indexOf(byte[] r2, byte r3) {
        return indexOf(r2, r3, 0, r2.length);
    }

    private static int indexOf(byte[] r2, byte r3, int r4, int r5) {
        int r0 = r4;
    L3:
        if (r0 >= r5) goto L8;
        if (r2[r0] == r3) goto L6;
        r0 = r0 + 1;
        goto L3
    L6:
        return r0;
    L8:
        return -1;
    }

    public static int indexOf(byte[] r4, byte[] r5) {
        Preconditions.checkNotNull(r4, "array");
        Preconditions.checkNotNull(r5, "target");
        if (r5.length != 0) goto L6;
        return 0;
    L6:
        int r0 = 0;
    L8:
        if (r0 >= ((r4.length - r5.length) + 1)) goto L17;
        int r1 = 0;
    L11:
        if (r1 >= r5.length) goto L16;
        if (r4[r0 + r1] != r5[r1]) goto L14;
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

    public static int lastIndexOf(byte[] r2, byte r3) {
        return lastIndexOf(r2, r3, 0, r2.length);
    }

    private static int lastIndexOf(byte[] r2, byte r3, int r4, int r5) {
        int r0 = r5 - 1;
    L3:
        if (r0 < r4) goto L8;
        if (r2[r0] == r3) goto L6;
        r0 = r0 - 1;
        goto L3
    L6:
        return r0;
    L8:
        return -1;
    }

    public static void reverse(byte[] r2) {
        Preconditions.checkNotNull(r2);
        reverse(r2, 0, r2.length);
    }

    public static void reverse(byte[] r4, int r5, int r6) {
        Preconditions.checkNotNull(r4);
        Preconditions.checkPositionIndexes(r5, r6, r4.length);
        int r0 = r5;
        int r1 = r6 - 1;
    L3:
        if (r0 >= r1) goto L5;
        byte r2 = r4[r0];
        r4[r0] = r4[r1];
        r4[r1] = r2;
        r0 = r0 + 1;
        r1 = r1 - 1;
        goto L3
    }

    public static void rotate(byte[] r2, int r3) {
        rotate(r2, r3, 0, r2.length);
    }

    public static void rotate(byte[] r3, int r4, int r5, int r6) {
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

    public static byte[] toArray(Collection<? extends Number> r5) {
        if ((r5 instanceof ByteArrayAsList) == true) goto L5;
        Object[] r0 = r5.toArray();
        int r1 = r0.length;
        byte[] r2 = new byte[r1];
        int r3 = 0;
    L7:
        if (r3 >= r1) goto L9;
        r2[r3] = ((Number) Preconditions.checkNotNull(r0[r3])).byteValue();
        r3 = r3 + 1;
        goto L7
    L9:
        return r2;
    L5:
        return ((ByteArrayAsList) r5).toByteArray();
    }
}
