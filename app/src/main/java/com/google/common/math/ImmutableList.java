package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, RandomAccess {
    private static final UnmodifiableListIterator<Object> EMPTY_ITR = null;
    private static final long serialVersionUID = -889275714;

    public static final class Builder<E> extends ImmutableCollection.ArrayBasedBuilder<E> {
        public Builder() {
            this(4);
        }

        Builder(int r1) {
            super(r1);
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
        public Builder<E> add(E r1) {
            super.add(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E... r1) {
            super.add(r1);
            return this;
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
        public Builder<E> addAll(Iterable<? extends E> r1) {
            super.addAll(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterator<? extends E> r1) {
            super.addAll(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection build() {
            return build();
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public ImmutableList<E> build() {
            this.forceCopy = true;
            return ImmutableList.asImmutableList(this.contents, this.size);
        }

        ImmutableList<E> buildSorted(Comparator<? super E> r4) {
            this.forceCopy = true;
            Arrays.sort(this.contents, 0, this.size, r4);
            return ImmutableList.asImmutableList(this.contents, this.size);
        }

        Builder<E> combine(Builder<E> r3) {
            addAll(r3.contents, r3.size);
            return this;
        }
    }

    static class Itr<E> extends AbstractIndexedListIterator<E> {
        private final ImmutableList<E> list;

        Itr(ImmutableList<E> r2, int r3) {
            super(r2.size(), r3);
            this.list = r2;
        }

        @Override // com.google.common.collect.AbstractIndexedListIterator
        protected E get(int r2) {
            return this.list.get(r2);
        }
    }

    private static class ReverseImmutableList<E> extends ImmutableList<E> {
        private final transient ImmutableList<E> forwardList;

        ReverseImmutableList(ImmutableList<E> r1) {
            this.forwardList = r1;
        }

        private int reverseIndex(int r2) {
            return (size() - 1) - r2;
        }

        private int reversePosition(int r2) {
            return size() - r2;
        }

        @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r2) {
            return this.forwardList.contains(r2);
        }

        @Override // java.util.List
        public E get(int r3) {
            Preconditions.checkElementIndex(r3, size());
            return this.forwardList.get(reverseIndex(r3));
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public int indexOf(@CheckForNull Object r3) {
            int r0 = this.forwardList.lastIndexOf(r3);
            if (r0 >= 0) goto L7;
            return -1;
        L7:
            return reverseIndex(r0);
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return this.forwardList.isPartialView();
        }

        @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public int lastIndexOf(@CheckForNull Object r3) {
            int r0 = this.forwardList.indexOf(r3);
            if (r0 >= 0) goto L7;
            return -1;
        L7:
            return reverseIndex(r0);
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public /* bridge */ /* synthetic */ ListIterator listIterator() {
            return super.listIterator();
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public /* bridge */ /* synthetic */ ListIterator listIterator(int r1) {
            return super.listIterator(r1);
        }

        @Override // com.google.common.collect.ImmutableList
        public ImmutableList<E> reverse() {
            return this.forwardList;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.forwardList.size();
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public ImmutableList<E> subList(int r4, int r5) {
            Preconditions.checkPositionIndexes(r4, r5, size());
            return this.forwardList.subList(reversePosition(r5), reversePosition(r4)).reverse();
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public /* bridge */ /* synthetic */ List subList(int r1, int r2) {
            return subList(r1, r2);
        }

        @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final Object[] elements;

        SerializedForm(Object[] r1) {
            this.elements = r1;
        }

        Object readResolve() {
            return ImmutableList.copyOf(this.elements);
        }
    }

    class SubList extends ImmutableList<E> {
        final transient int length;
        final transient int offset;
        final /* synthetic */ ImmutableList this$0;

        SubList(ImmutableList r1, int r2, int r3) {
            this.this$0 = r1;
            this.offset = r2;
            this.length = r3;
        }

        @Override // java.util.List
        public E get(int r3) {
            Preconditions.checkElementIndex(r3, this.length);
            return this.this$0.get(this.offset + r3);
        }

        @Override // com.google.common.collect.ImmutableCollection
        @CheckForNull
        Object[] internalArray() {
            return this.this$0.internalArray();
        }

        @Override // com.google.common.collect.ImmutableCollection
        int internalArrayEnd() {
            return (this.this$0.internalArrayStart() + this.offset) + this.length;
        }

        @Override // com.google.common.collect.ImmutableCollection
        int internalArrayStart() {
            return this.this$0.internalArrayStart() + this.offset;
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return true;
        }

        @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public /* bridge */ /* synthetic */ ListIterator listIterator() {
            return super.listIterator();
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public /* bridge */ /* synthetic */ ListIterator listIterator(int r1) {
            return super.listIterator(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.length;
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public ImmutableList<E> subList(int r4, int r5) {
            Preconditions.checkPositionIndexes(r4, r5, this.length);
            return this.this$0.subList(this.offset + r4, this.offset + r5);
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public /* bridge */ /* synthetic */ List subList(int r1, int r2) {
            return subList(r1, r2);
        }

        @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    static {
        EMPTY_ITR = new Itr(RegularImmutableList.EMPTY, 0);
    }

    ImmutableList() {
    }

    static <E> ImmutableList<E> asImmutableList(Object[] r1) {
        return asImmutableList(r1, r1.length);
    }

    static <E> ImmutableList<E> asImmutableList(Object[] r1, int r2) {
        if (r2 != 0) goto L6;
        return m212of();
    L6:
        return new RegularImmutableList(r1, r2);
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }

    public static <E> Builder<E> builderWithExpectedSize(int r1) {
        CollectPreconditions.checkNonnegative(r1, "expectedSize");
        return new Builder(r1);
    }

    private static <E> ImmutableList<E> construct(Object... r1) {
        return asImmutableList(ObjectArrays.checkElementsNotNull(r1));
    }

    public static <E> ImmutableList<E> copyOf(Iterable<? extends E> r1) {
        Preconditions.checkNotNull(r1);
        if ((r1 instanceof Collection) == false) goto L6;
        return copyOf((Collection) r1);
    L6:
        return copyOf(r1.iterator());
    }

    public static <E> ImmutableList<E> copyOf(Collection<? extends E> r2) {
        if ((r2 instanceof ImmutableCollection) == false) goto L10;
        ImmutableList<E> r0 = ((ImmutableCollection) r2).asList();
        if (r0.isPartialView() == false) goto L8;
        return asImmutableList(r0.toArray());
    L8:
        return r0;
    L10:
        return construct(r2.toArray());
    }

    public static <E> ImmutableList<E> copyOf(Iterator<? extends E> r2) {
        if (r2.hasNext() == false) goto L5;
        E r0 = r2.next();
        if (r2.hasNext() == true) goto L11;
        return m213of(r0);
    L11:
        return new Builder().add(r0).addAll(r2).build();
    L5:
        return m212of();
    }

    public static <E> ImmutableList<E> copyOf(E[] r1) {
        if (r1.length != 0) goto L6;
        return m212of();
    L6:
        return construct((Object[]) r1.clone());
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableList<E> m212of() {
        return (ImmutableList<E>) RegularImmutableList.EMPTY;
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableList<E> m213of(E r2) {
        return construct(new Object[]{r2});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableList<E> m214of(E r2, E r3) {
        return construct(new Object[]{r2, r3});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableList<E> m215of(E r2, E r3, E r4) {
        return construct(new Object[]{r2, r3, r4});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableList<E> m216of(E r2, E r3, E r4, E r5) {
        return construct(new Object[]{r2, r3, r4, r5});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableList<E> m217of(E r2, E r3, E r4, E r5, E r6) {
        return construct(new Object[]{r2, r3, r4, r5, r6});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableList<E> m218of(E r2, E r3, E r4, E r5, E r6, E r7) {
        return construct(new Object[]{r2, r3, r4, r5, r6, r7});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableList<E> m219of(E r2, E r3, E r4, E r5, E r6, E r7, E r8) {
        return construct(new Object[]{r2, r3, r4, r5, r6, r7, r8});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableList<E> m220of(E r2, E r3, E r4, E r5, E r6, E r7, E r8, E r9) {
        return construct(new Object[]{r2, r3, r4, r5, r6, r7, r8, r9});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableList<E> m221of(E r2, E r3, E r4, E r5, E r6, E r7, E r8, E r9, E r10) {
        return construct(new Object[]{r2, r3, r4, r5, r6, r7, r8, r9, r10});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableList<E> m222of(E r2, E r3, E r4, E r5, E r6, E r7, E r8, E r9, E r10, E r11) {
        return construct(new Object[]{r2, r3, r4, r5, r6, r7, r8, r9, r10, r11});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableList<E> m223of(E r2, E r3, E r4, E r5, E r6, E r7, E r8, E r9, E r10, E r11, E r12) {
        return construct(new Object[]{r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12});
    }

    @SafeVarargs
    /* JADX INFO: renamed from: of */
    public static <E> ImmutableList<E> m224of(E r5, E r6, E r7, E r8, E r9, E r10, E r11, E r12, E r13, E r14, E r15, E r16, E... r17) {
        if (r17.length > 2147483635) goto L5;
        boolean r1 = true;
    L6:
        Preconditions.checkArgument(r1, "the total number of elements must fit in an int");
        Object[] r2 = new Object[r17.length + 12];
        r2[0] = r5;
        r2[1] = r6;
        r2[2] = r7;
        r2[3] = r8;
        r2[4] = r9;
        r2[5] = r10;
        r2[6] = r11;
        r2[7] = r12;
        r2[8] = r13;
        r2[9] = r14;
        r2[10] = r15;
        r2[11] = r16;
        System.arraycopy(r17, 0, r2, 12, r17.length);
        return construct(r2);
    L5:
        r1 = false;
        goto L6
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    public static <E extends Comparable<? super E>> ImmutableList<E> sortedCopyOf(Iterable<? extends E> r2) {
        Comparable[] r0 = (Comparable[]) Iterables.toArray(r2, new Comparable[0]);
        ObjectArrays.checkElementsNotNull(r0);
        Arrays.sort(r0);
        return asImmutableList(r0);
    }

    public static <E> ImmutableList<E> sortedCopyOf(Comparator<? super E> r2, Iterable<? extends E> r3) {
        Preconditions.checkNotNull(r2);
        Object[] r0 = Iterables.toArray(r3);
        ObjectArrays.checkElementsNotNull(r0);
        Arrays.sort(r0, r2);
        return asImmutableList(r0);
    }

    public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
        return CollectCollectors.toImmutableList();
    }

    @Override // java.util.List
    @Deprecated
    public final void add(int r2, E r3) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    @Deprecated
    public final boolean addAll(int r2, Collection<? extends E> r3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.ImmutableCollection
    @Deprecated
    public final ImmutableList<E> asList() {
        return this;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@CheckForNull Object r2) {
        if (indexOf(r2) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // com.google.common.collect.ImmutableCollection
    int copyIntoArray(Object[] r5, int r6) {
        int r0 = size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L6;
        r5[r6 + r1] = get(r1);
        r1 = r1 + 1;
        goto L3
    L6:
        return r6 + r0;
    }

    @Override // java.util.Collection, java.util.List
    public boolean equals(@CheckForNull Object r2) {
        return Lists.equalsImpl(this, r2);
    }

    @Override // java.util.Collection, java.util.List
    public int hashCode() {
        int r0 = 1;
        int r1 = size();
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = ~(~((r0 * 31) + get(r2).hashCode()));
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    @Override // java.util.List
    public int indexOf(@CheckForNull Object r2) {
        if (r2 != null) goto L5;
        return -1;
    L5:
        return Lists.indexOfImpl(this, r2);
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public UnmodifiableIterator<E> iterator() {
        return listIterator();
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return iterator();
    }

    @Override // java.util.List
    public int lastIndexOf(@CheckForNull Object r2) {
        if (r2 != null) goto L5;
        return -1;
    L5:
        return Lists.lastIndexOfImpl(this, r2);
    }

    @Override // java.util.List
    public UnmodifiableListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override // java.util.List
    public UnmodifiableListIterator<E> listIterator(int r2) {
        Preconditions.checkPositionIndex(r2, size());
        if (isEmpty() == false) goto L7;
        return (UnmodifiableListIterator<E>) EMPTY_ITR;
    L7:
        return new Itr(this, r2);
    }

    @Override // java.util.List
    public /* bridge */ /* synthetic */ ListIterator listIterator() {
        return listIterator();
    }

    @Override // java.util.List
    public /* bridge */ /* synthetic */ ListIterator listIterator(int r1) {
        return listIterator(r1);
    }

    @Override // java.util.List
    @Deprecated
    public final E remove(int r2) {
        throw new UnsupportedOperationException();
    }

    public ImmutableList<E> reverse() {
        if (size() > 1) goto L6;
        return this;
    L6:
        return new ReverseImmutableList(this);
    }

    @Override // java.util.List
    @Deprecated
    public final E set(int r2, E r3) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public ImmutableList<E> subList(int r3, int r4) {
        Preconditions.checkPositionIndexes(r3, r4, size());
        int r0 = r4 - r3;
        if (r0 != size()) goto L5;
        return this;
    L5:
        if (r0 != 0) goto L9;
        return m212of();
    L9:
        return subListUnchecked(r3, r4);
    }

    @Override // java.util.List
    public /* bridge */ /* synthetic */ List subList(int r1, int r2) {
        return subList(r1, r2);
    }

    ImmutableList<E> subListUnchecked(int r3, int r4) {
        return new SubList(this, r3, r4 - r3);
    }

    @Override // com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(toArray());
    }
}
