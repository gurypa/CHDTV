package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E> {
    private static final int CUTOFF = 751619276;
    private static final double DESIRED_LOAD_FACTOR = 0.7d;
    static final int MAX_TABLE_SIZE = 1073741824;
    private static final long serialVersionUID = 912559;

    @CheckForNull
    @LazyInit
    private transient ImmutableList<E> asList;

    public static class Builder<E> extends ImmutableCollection.ArrayBasedBuilder<E> {
        private int hashCode;

        @CheckForNull
        Object[] hashTable;

        public Builder() {
            super(4);
        }

        Builder(int r2, boolean r3) {
            super(r2);
            if (r3 == false) goto L6;
            this.hashTable = new Object[ImmutableSet.chooseTableSize(r2)];
            return;
        }

        private void addDeduping(E r6) {
            Objects.requireNonNull(this.hashTable);
            int r0 = this.hashTable.length - 1;
            int r1 = r6.hashCode();
            int r2 = Hashing.smear(r1);
        L3:
            int r3 = r2 & r0;
            Object r4 = this.hashTable[r3];
            if (r4 == null) goto L5;
            if (r4.equals(r6) == true) goto L9;
            r2 = r3 + 1;
            goto L3
        L9:
            return;
        L5:
            this.hashTable[r3] = r6;
            this.hashCode += r1;
            super.add(r6);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.ArrayBasedBuilder add(Object r1) {
            return add(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder add(Object r1) {
            return add(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder add(Object[] r1) {
            return add(r1);
        }

        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E r3) {
            Preconditions.checkNotNull(r3);
            if (this.hashTable != null) goto L5;
        L8:
            this.hashTable = null;
            super.add(r3);
            return this;
        L5:
            if (ImmutableSet.chooseTableSize(this.size) > this.hashTable.length) goto L8;
            addDeduping(r3);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E... r4) {
            if (this.hashTable == null) goto L7;
            int r0 = r4.length;
            int r1 = 0;
        L5:
            if (r1 >= r0) goto L8;
            add(r4[r1]);
            r1 = r1 + 1;
        L8:
            return this;
        L7:
            super.add(r4);
            goto L8
        }

        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder addAll(Iterable r1) {
            return addAll(r1);
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder addAll(Iterator r1) {
            return addAll(r1);
        }

        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterable<? extends E> r3) {
            Preconditions.checkNotNull(r3);
            if (this.hashTable == null) goto L9;
            Iterator<? extends E> r0 = r3.iterator();
        L6:
            if (r0.hasNext() == false) goto L10;
            add(r0.next());
        L10:
            return this;
        L9:
            super.addAll(r3);
            goto L10
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterator<? extends E> r2) {
            Preconditions.checkNotNull(r2);
        L4:
            if (r2.hasNext() == false) goto L6;
            add(r2.next());
            goto L4
        L6:
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection build() {
            return build();
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public ImmutableSet<E> build() {
            switch(this.size) {
                case 0: goto L14;
                case 1: goto L12;
                default: goto L5;
            };
        L5:
            if (this.hashTable != null) goto L7;
        L17:
            ImmutableSet<E> r2 = ImmutableSet.access$100(this.size, this.contents);
            this.size = r2.size();
        L18:
            this.forceCopy = true;
            this.hashTable = null;
            return r2;
        L7:
            if (ImmutableSet.chooseTableSize(this.size) != this.hashTable.length) goto L17;
            if (ImmutableSet.access$000(this.size, this.contents.length) == false) goto L15;
            Object[] r0 = Arrays.copyOf(this.contents, this.size);
        L16:
            r2 = new RegularImmutableSet(r0, this.hashCode, this.hashTable, this.hashTable.length - 1, this.size);
            goto L18
        L15:
            r0 = this.contents;
            goto L16
        L12:
            return ImmutableSet.m260of(Objects.requireNonNull(this.contents[0]));
        L14:
            return ImmutableSet.m259of();
        }

        /* JADX WARN: Multi-variable type inference failed */
        Builder<E> combine(Builder<E> r3) {
            if (this.hashTable == null) goto L9;
            int r0 = 0;
        L6:
            if (r0 >= r3.size) goto L10;
            add(Objects.requireNonNull(r3.contents[r0]));
            r0 = r0 + 1;
        L10:
            return this;
        L9:
            addAll(r3.contents, r3.size);
            goto L10
        }
    }

    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final Object[] elements;

        SerializedForm(Object[] r1) {
            this.elements = r1;
        }

        Object readResolve() {
            return ImmutableSet.copyOf(this.elements);
        }
    }

    ImmutableSet() {
    }

    static /* synthetic */ boolean access$000(int r1, int r2) {
        return shouldTrim(r1, r2);
    }

    static /* synthetic */ ImmutableSet access$100(int r1, Object[] r2) {
        return construct(r1, r2);
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }

    public static <E> Builder<E> builderWithExpectedSize(int r2) {
        CollectPreconditions.checkNonnegative(r2, "expectedSize");
        return new Builder(r2, true);
    }

    static int chooseTableSize(int r5) {
        int r6 = Math.max(r5, 2);
        boolean r1 = true;
        if (r6 >= CUTOFF) goto L10;
        int r0 = Integer.highestOneBit(r6 - 1) << 1;
    L6:
        if ((((double) r0) * DESIRED_LOAD_FACTOR) >= r6) goto L8;
        r0 = r0 << 1;
        goto L6
    L8:
        return r0;
    L10:
        if (r6 < 1073741824) goto L13;
        r1 = false;
    L13:
        Preconditions.checkArgument(r1, "collection too large");
        return 1073741824;
    }

    private static <E> ImmutableSet<E> construct(int r13, Object... r14) {
        switch(r13) {
            case 0: goto L8;
            case 1: goto L6;
            default: goto L4;
        };
    L4:
        int r1 = chooseTableSize(r13);
        Object[] r5 = new Object[r1];
        int r6 = r1 - 1;
        int r4 = 0;
        int r2 = 0;
        int r7 = 0;
    L9:
        if (r2 >= r13) goto L18;
        Object r3 = ObjectArrays.checkElementNotNull(r14[r2], r2);
        int r8 = r3.hashCode();
        int r9 = Hashing.smear(r8);
    L11:
        int r10 = r9 & r6;
        Object r11 = r5[r10];
        if (r11 == null) goto L13;
        if (r11.equals(r3) == true) goto L16;
        r9 = r9 + 1;
    L16:
        r2 = r2 + 1;
        goto L9
    L13:
        r14[r7] = r3;
        r5[r10] = r3;
        r4 = r4 + r8;
        r7 = r7 + 1;
        goto L16
    L18:
        Arrays.fill(r14, r7, r13, null);
        if (r7 != 1) goto L23;
        return new SingletonImmutableSet(Objects.requireNonNull(r14[0]));
    L23:
        if (chooseTableSize(r7) >= (r1 / 2)) goto L27;
        return construct(r7, r14);
    L27:
        if (shouldTrim(r7, r14.length) == false) goto L29;
        Object[] r12 = Arrays.copyOf(r14, r7);
    L31:
        return new RegularImmutableSet(r12, r4, r5, r6, r7);
    L29:
        r12 = r14;
        goto L31
    L6:
        return m260of(Objects.requireNonNull(r14[0]));
    L8:
        return m259of();
    }

    public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> r1) {
        if ((r1 instanceof Collection) == false) goto L6;
        return copyOf((Collection) r1);
    L6:
        return copyOf(r1.iterator());
    }

    public static <E> ImmutableSet<E> copyOf(Collection<? extends E> r2) {
        if ((r2 instanceof ImmutableSet) == true) goto L5;
    L9:
        Object[] r0 = r2.toArray();
        return construct(r0.length, r0);
    L5:
        if ((r2 instanceof SortedSet) == true) goto L9;
        ImmutableSet<E> r1 = (ImmutableSet) r2;
        if (r1.isPartialView() == true) goto L9;
        return r1;
    }

    public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> r2) {
        if (r2.hasNext() == false) goto L5;
        E r0 = r2.next();
        if (r2.hasNext() == true) goto L11;
        return m260of(r0);
    L11:
        return new Builder().add(r0).addAll(r2).build();
    L5:
        return m259of();
    }

    public static <E> ImmutableSet<E> copyOf(E[] r2) {
        switch(r2.length) {
            case 0: goto L9;
            case 1: goto L7;
            default: goto L5;
        };
    L5:
        return construct(r2.length, (Object[]) r2.clone());
    L7:
        return m260of(r2[0]);
    L9:
        return m259of();
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSet<E> m259of() {
        return RegularImmutableSet.EMPTY;
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSet<E> m260of(E r1) {
        return new SingletonImmutableSet(r1);
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSet<E> m261of(E r3, E r4) {
        return construct(2, new Object[]{r3, r4});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSet<E> m262of(E r3, E r4, E r5) {
        return construct(3, new Object[]{r3, r4, r5});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSet<E> m263of(E r3, E r4, E r5, E r6) {
        return construct(4, new Object[]{r3, r4, r5, r6});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSet<E> m264of(E r3, E r4, E r5, E r6, E r7) {
        return construct(5, new Object[]{r3, r4, r5, r6, r7});
    }

    @SafeVarargs
    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSet<E> m265of(E r5, E r6, E r7, E r8, E r9, E r10, E... r11) {
        if (r11.length > 2147483641) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "the total number of elements must fit in an int");
        Object[] r1 = new Object[r11.length + 6];
        r1[0] = r5;
        r1[1] = r6;
        r1[2] = r7;
        r1[3] = r8;
        r1[4] = r9;
        r1[5] = r10;
        System.arraycopy(r11, 0, r1, 6, r11.length);
        return construct(r1.length, r1);
    L5:
        r0 = false;
        goto L6
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    private static boolean shouldTrim(int r2, int r3) {
        if (r2 >= ((r3 >> 1) + (r3 >> 2))) goto L5;
        return true;
    L5:
        return false;
    }

    public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
        return CollectCollectors.toImmutableSet();
    }

    @Override // com.google.common.collect.ImmutableCollection
    public ImmutableList<E> asList() {
        ImmutableList<E> r0 = this.asList;
        if (r0 != null) goto L6;
        ImmutableList<E> r1 = createAsList();
        this.asList = r1;
        return r1;
    L6:
        return r0;
    }

    ImmutableList<E> createAsList() {
        return ImmutableList.asImmutableList(toArray());
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(@CheckForNull Object r3) {
        if (r3 != this) goto L6;
        return true;
    L6:
        if ((r3 instanceof ImmutableSet) == false) goto L16;
        if (isHashCodeFast() == false) goto L16;
        if (((ImmutableSet) r3).isHashCodeFast() == false) goto L16;
        if (hashCode() == r3.hashCode()) goto L16;
        return false;
    L16:
        return Sets.equalsImpl(this, r3);
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        return Sets.hashCodeImpl(this);
    }

    boolean isHashCodeFast() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public abstract UnmodifiableIterator<E> iterator();

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return iterator();
    }

    @Override // com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(toArray());
    }
}
