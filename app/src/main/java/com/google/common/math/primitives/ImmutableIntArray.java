package com.google.common.primitives;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
@Immutable
public final class ImmutableIntArray implements Serializable {
    private static final ImmutableIntArray EMPTY = null;
    private final int[] array;
    private final int end;
    private final transient int start;

    /* JADX INFO: renamed from: com.google.common.primitives.ImmutableIntArray$1 */
    static /* synthetic */ class C08691 {
    }

    static class AsList extends AbstractList<Integer> implements RandomAccess, Serializable {
        private final ImmutableIntArray parent;

        private AsList(ImmutableIntArray r1) {
            this.parent = r1;
        }

        /* synthetic */ AsList(ImmutableIntArray r1, C08691 r2) {
            this(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean contains(@CheckForNull Object r2) {
            if (indexOf(r2) < 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public boolean equals(@CheckForNull Object r8) {
            if ((r8 instanceof AsList) == false) goto L7;
            return this.parent.equals(((AsList) r8).parent);
        L7:
            if ((r8 instanceof List) == true) goto L9;
            return false;
        L9:
            List r0 = (List) r8;
            if (size() == r0.size()) goto L12;
            return false;
        L12:
            int r2 = ImmutableIntArray.access$100(this.parent);
            Iterator r3 = r0.iterator();
        L14:
            if (r3.hasNext() == false) goto L22;
            Object r4 = r3.next();
            if ((r4 instanceof Integer) == false) goto L21;
            int r6 = r2 + 1;
            if (ImmutableIntArray.access$000(this.parent)[r2] != ((Integer) r4).intValue()) goto L21;
            r2 = r6;
        L21:
            return false;
        L22:
            return true;
        }

        @Override // java.util.AbstractList, java.util.List
        public Integer get(int r2) {
            return Integer.valueOf(this.parent.get(r2));
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public int hashCode() {
            return this.parent.hashCode();
        }

        @Override // java.util.AbstractList, java.util.List
        public int indexOf(@CheckForNull Object r3) {
            if ((r3 instanceof Integer) == true) goto L7;
            return -1;
        L7:
            return this.parent.indexOf(((Integer) r3).intValue());
        }

        @Override // java.util.AbstractList, java.util.List
        public int lastIndexOf(@CheckForNull Object r3) {
            if ((r3 instanceof Integer) == true) goto L7;
            return -1;
        L7:
            return this.parent.lastIndexOf(((Integer) r3).intValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.parent.length();
        }

        @Override // java.util.AbstractList, java.util.List
        public List<Integer> subList(int r2, int r3) {
            return this.parent.subArray(r2, r3).asList();
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            return this.parent.toString();
        }
    }

    public static final class Builder {
        private int[] array;
        private int count;

        Builder(int r2) {
            this.count = 0;
            this.array = new int[r2];
        }

        private void ensureRoomFor(int r4) {
            int r0 = this.count + r4;
            if (r0 <= this.array.length) goto L6;
            this.array = Arrays.copyOf(this.array, expandedCapacity(this.array.length, r0));
            return;
        }

        private static int expandedCapacity(int r2, int r3) {
            if (r3 < 0) goto L10;
            int r0 = ((r2 >> 1) + r2) + 1;
            if (r0 >= r3) goto L6;
            r0 = Integer.highestOneBit(r3 - 1) << 1;
        L6:
            if (r0 >= 0) goto L11;
            return Integer.MAX_VALUE;
        L11:
            return r0;
        L10:
            throw new AssertionError("cannot store more than MAX_VALUE elements");
        }

        public Builder add(int r4) {
            ensureRoomFor(1);
            this.array[this.count] = r4;
            this.count++;
            return this;
        }

        public Builder addAll(ImmutableIntArray r6) {
            ensureRoomFor(r6.length());
            System.arraycopy(ImmutableIntArray.access$000(r6), ImmutableIntArray.access$100(r6), this.array, this.count, r6.length());
            this.count += r6.length();
            return this;
        }

        public Builder addAll(Iterable<Integer> r4) {
            if ((r4 instanceof Collection) == true) goto L5;
            Iterator<Integer> r0 = r4.iterator();
        L8:
            if (r0.hasNext() == false) goto L10;
            add(r0.next().intValue());
            goto L8
        L10:
            return this;
        L5:
            return addAll((Collection) r4);
        }

        public Builder addAll(Collection<Integer> r6) {
            ensureRoomFor(r6.size());
            Iterator<Integer> r0 = r6.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            Integer r1 = r0.next();
            int[] r2 = this.array;
            int r3 = this.count;
            this.count = r3 + 1;
            r2[r3] = r1.intValue();
            goto L4
        L6:
            return this;
        }

        public Builder addAll(int[] r5) {
            ensureRoomFor(r5.length);
            System.arraycopy(r5, 0, this.array, this.count, r5.length);
            this.count += r5.length;
            return this;
        }

        public ImmutableIntArray build() {
            if (this.count == 0) goto L7;
            int r4 = 0;
            return new ImmutableIntArray(this.array, r4, this.count, null);
        L7:
            return ImmutableIntArray.access$200();
        }
    }

    static {
        EMPTY = new ImmutableIntArray(new int[0]);
    }

    private ImmutableIntArray(int[] r3) {
        this(r3, 0, r3.length);
    }

    private ImmutableIntArray(int[] r1, int r2, int r3) {
        this.array = r1;
        this.start = r2;
        this.end = r3;
    }

    /* synthetic */ ImmutableIntArray(int[] r1, int r2, int r3, C08691 r4) {
        this(r1, r2, r3);
    }

    static /* synthetic */ int[] access$000(ImmutableIntArray r1) {
        return r1.array;
    }

    static /* synthetic */ int access$100(ImmutableIntArray r1) {
        return r1.start;
    }

    static /* synthetic */ ImmutableIntArray access$200() {
        return EMPTY;
    }

    public static Builder builder() {
        return new Builder(10);
    }

    public static Builder builder(int r2) {
        if (r2 < 0) goto L4;
        boolean r0 = true;
    L5:
        Preconditions.checkArgument(r0, "Invalid initialCapacity: %s", r2);
        return new Builder(r2);
    L4:
        r0 = false;
        goto L5
    }

    public static ImmutableIntArray copyOf(Iterable<Integer> r1) {
        if ((r1 instanceof Collection) == false) goto L7;
        return copyOf((Collection) r1);
    L7:
        return builder().addAll(r1).build();
    }

    public static ImmutableIntArray copyOf(Collection<Integer> r2) {
        if (r2.isEmpty() == false) goto L6;
        return EMPTY;
    L6:
        return new ImmutableIntArray(Ints.toArray(r2));
    }

    public static ImmutableIntArray copyOf(int[] r2) {
        if (r2.length != 0) goto L6;
        return EMPTY;
    L6:
        return new ImmutableIntArray(Arrays.copyOf(r2, r2.length));
    }

    private boolean isPartialView() {
        if (this.start <= 0) goto L5;
    L8:
        return true;
    L5:
        if (this.end < this.array.length) goto L8;
        return false;
    }

    /* JADX INFO: renamed from: of */
    public static ImmutableIntArray m355of() {
        return EMPTY;
    }

    /* JADX INFO: renamed from: of */
    public static ImmutableIntArray m356of(int r2) {
        return new ImmutableIntArray(new int[]{r2});
    }

    /* JADX INFO: renamed from: of */
    public static ImmutableIntArray m357of(int r2, int r3) {
        return new ImmutableIntArray(new int[]{r2, r3});
    }

    /* JADX INFO: renamed from: of */
    public static ImmutableIntArray m358of(int r2, int r3, int r4) {
        return new ImmutableIntArray(new int[]{r2, r3, r4});
    }

    /* JADX INFO: renamed from: of */
    public static ImmutableIntArray m359of(int r2, int r3, int r4, int r5) {
        return new ImmutableIntArray(new int[]{r2, r3, r4, r5});
    }

    /* JADX INFO: renamed from: of */
    public static ImmutableIntArray m360of(int r2, int r3, int r4, int r5, int r6) {
        return new ImmutableIntArray(new int[]{r2, r3, r4, r5, r6});
    }

    /* JADX INFO: renamed from: of */
    public static ImmutableIntArray m361of(int r2, int r3, int r4, int r5, int r6, int r7) {
        return new ImmutableIntArray(new int[]{r2, r3, r4, r5, r6, r7});
    }

    /* JADX INFO: renamed from: of */
    public static ImmutableIntArray m362of(int r4, int... r5) {
        if (r5.length > 2147483646) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "the total number of elements must fit in an int");
        int[] r1 = new int[r5.length + 1];
        r1[0] = r4;
        System.arraycopy(r5, 0, r1, 1, r5.length);
        return new ImmutableIntArray(r1);
    L5:
        r0 = false;
        goto L6
    }

    public List<Integer> asList() {
        return new AsList(this, null);
    }

    public boolean contains(int r2) {
        if (indexOf(r2) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean equals(@CheckForNull Object r7) {
        if (r7 != this) goto L6;
        return true;
    L6:
        if ((r7 instanceof ImmutableIntArray) == true) goto L8;
        return false;
    L8:
        ImmutableIntArray r1 = (ImmutableIntArray) r7;
        if (length() == r1.length()) goto L11;
        return false;
    L11:
        int r3 = 0;
    L13:
        if (r3 >= length()) goto L18;
        if (get(r3) != r1.get(r3)) goto L16;
        r3 = r3 + 1;
        goto L13
    L16:
        return false;
    L18:
        return true;
    }

    public int get(int r3) {
        Preconditions.checkElementIndex(r3, length());
        return this.array[this.start + r3];
    }

    public int hashCode() {
        int r0 = 1;
        int r1 = this.start;
    L4:
        if (r1 >= this.end) goto L6;
        r0 = (r0 * 31) + Ints.hashCode(this.array[r1]);
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    public int indexOf(int r3) {
        int r0 = this.start;
    L4:
        if (r0 >= this.end) goto L10;
        if (this.array[r0] == r3) goto L8;
        r0 = r0 + 1;
        goto L4
    L8:
        return r0 - this.start;
    L10:
        return -1;
    }

    public boolean isEmpty() {
        if (this.end != this.start) goto L5;
        return true;
    L5:
        return false;
    }

    public int lastIndexOf(int r3) {
        int r0 = this.end;
    L3:
        r0 = r0 - 1;
        if (r0 < this.start) goto L10;
        if (this.array[r0] != r3) goto L3;
        return r0 - this.start;
    L10:
        return -1;
    }

    public int length() {
        return this.end - this.start;
    }

    Object readResolve() {
        if (isEmpty() == false) goto L6;
        return EMPTY;
    L6:
        return this;
    }

    public ImmutableIntArray subArray(int r5, int r6) {
        Preconditions.checkPositionIndexes(r5, r6, length());
        if (r5 != r6) goto L6;
        return EMPTY;
    L6:
        return new ImmutableIntArray(this.array, this.start + r5, this.start + r6);
    }

    public int[] toArray() {
        return Arrays.copyOfRange(this.array, this.start, this.end);
    }

    public String toString() {
        if (isEmpty() == false) goto L6;
        return "[]";
    L6:
        StringBuilder r0 = new StringBuilder(length() * 5);
        r0.append('[').append(this.array[this.start]);
        int r1 = this.start;
    L7:
        r1 = r1 + 1;
        if (r1 >= this.end) goto L10;
        r0.append(", ").append(this.array[r1]);
        goto L7
    L10:
        r0.append(']');
        return r0.toString();
    }

    public ImmutableIntArray trimmed() {
        if (isPartialView() == false) goto L6;
        return new ImmutableIntArray(toArray());
    L6:
        return this;
    }

    Object writeReplace() {
        return trimmed();
    }
}
