package com.google.common.collect;

import android.R;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ImmutableSortedSet<E> extends ImmutableSet<E> implements NavigableSet<E>, SortedIterable<E> {
    private static final long serialVersionUID = 912559;
    final transient Comparator<? super E> comparator;

    @CheckForNull
    @LazyInit
    transient ImmutableSortedSet<E> descendingSet;

    public static final class Builder<E> extends ImmutableSet.Builder<E> {
        private final Comparator<? super E> comparator;

        public Builder(Comparator<? super E> r2) {
            this.comparator = (Comparator) Preconditions.checkNotNull(r2);
        }

        Builder(Comparator<? super E> r2, int r3) {
            super(r3, false);
            this.comparator = (Comparator) Preconditions.checkNotNull(r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.ArrayBasedBuilder add(Object r1) {
            return add(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder add(Object r1) {
            return add(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder add(Object[] r1) {
            return add(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableSet.Builder add(Object r1) {
            return add(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableSet.Builder add(Object[] r1) {
            return add(r1);
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E r1) {
            super.add(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E... r1) {
            super.add(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder addAll(Iterable r1) {
            return addAll(r1);
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder addAll(Iterator r1) {
            return addAll(r1);
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableSet.Builder addAll(Iterable r1) {
            return addAll(r1);
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableSet.Builder addAll(Iterator r1) {
            return addAll(r1);
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterable<? extends E> r1) {
            super.addAll(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterator<? extends E> r1) {
            super.addAll(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection build() {
            return build();
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableSet build() {
            return build();
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public ImmutableSortedSet<E> build() {
            Object[] r0 = this.contents;
            ImmutableSortedSet<E> r1 = ImmutableSortedSet.construct(this.comparator, this.size, r0);
            this.size = r1.size();
            this.forceCopy = true;
            return r1;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder
        /* bridge */ /* synthetic */ ImmutableSet.Builder combine(ImmutableSet.Builder r1) {
            return combine(r1);
        }

        @Override // com.google.common.collect.ImmutableSet.Builder
        Builder<E> combine(ImmutableSet.Builder<E> r1) {
            super.combine(r1);
            return this;
        }
    }

    private static class SerializedForm<E> implements Serializable {
        private static final long serialVersionUID = 0;
        final Comparator<? super E> comparator;
        final Object[] elements;

        public SerializedForm(Comparator<? super E> r1, Object[] r2) {
            this.comparator = r1;
            this.elements = r2;
        }

        /* JADX WARN: Multi-variable type inference failed */
        Object readResolve() {
            return new Builder(this.comparator).add(this.elements).build();
        }
    }

    ImmutableSortedSet(Comparator<? super E> r1) {
        this.comparator = r1;
    }

    @Deprecated
    public static <E> Builder<E> builder() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public static <E> Builder<E> builderWithExpectedSize(int r1) {
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <E> ImmutableSortedSet<E> construct(Comparator<? super E> r5, int r6, E... r7) {
        if (r6 == 0) goto L4;
        ObjectArrays.checkElementsNotNull(r7, r6);
        Arrays.sort(r7, 0, r6, r5);
        int r0 = 1;
        int r1 = 1;
    L6:
        if (r1 >= r6) goto L11;
        R.color r2 = (Object) r7[r1];
        if (r5.compare(r2, (Object) r7[r0 - 1]) == 0) goto L10;
        r7[r0] = r2;
        r0 = r0 + 1;
    L10:
        r1 = r1 + 1;
        goto L6
    L11:
        Arrays.fill(r7, r0, r6, null);
        if (r0 >= (r7.length / 2)) goto L15;
        r7 = (E[]) Arrays.copyOf(r7, r0);
    L15:
        return new RegularImmutableSortedSet(ImmutableList.asImmutableList(r7, r0), r5);
    L4:
        return emptySet(r5);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> r2) {
        return copyOf(Ordering.natural(), r2);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> r2) {
        return copyOf(Ordering.natural(), r2);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> r3, Iterable<? extends E> r4) {
        Preconditions.checkNotNull(r3);
        if (SortedIterables.hasSameComparator(r3, r4) == true) goto L5;
    L9:
        Object[] r1 = Iterables.toArray(r4);
        return construct(r3, r1.length, r1);
    L5:
        if ((r4 instanceof ImmutableSortedSet) == false) goto L9;
        ImmutableSortedSet<E> r2 = (ImmutableSortedSet) r4;
        if (r2.isPartialView() == true) goto L9;
        return r2;
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> r1, Collection<? extends E> r2) {
        return copyOf(r1, r2);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> r1, Iterator<? extends E> r2) {
        return new Builder(r1).addAll(r2).build();
    }

    public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> r2) {
        return copyOf(Ordering.natural(), r2);
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>([TE;)Lcom/google/common/collect/ImmutableSortedSet<TE;>; */
    public static ImmutableSortedSet copyOf(Comparable[] r3) {
        return construct(Ordering.natural(), r3.length, (Comparable[]) r3.clone());
    }

    @Deprecated
    public static <Z> ImmutableSortedSet<Z> copyOf(Z[] r1) {
        throw new UnsupportedOperationException();
    }

    public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> r3) {
        Comparator r0 = SortedIterables.comparator(r3);
        ImmutableList r1 = ImmutableList.copyOf(r3);
        if (r1.isEmpty() == false) goto L7;
        return emptySet(r0);
    L7:
        return new RegularImmutableSortedSet(r1, r0);
    }

    static <E> RegularImmutableSortedSet<E> emptySet(Comparator<? super E> r2) {
        if (Ordering.natural().equals(r2) == false) goto L7;
        return (RegularImmutableSortedSet<E>) RegularImmutableSortedSet.NATURAL_EMPTY_SET;
    L7:
        return new RegularImmutableSortedSet(ImmutableList.m212of(), r2);
    }

    public static <E extends Comparable<?>> Builder<E> naturalOrder() {
        return new Builder(Ordering.natural());
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSortedSet<E> m307of() {
        return RegularImmutableSortedSet.NATURAL_EMPTY_SET;
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;)Lcom/google/common/collect/ImmutableSortedSet<TE;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedSet m308of(Comparable r3) {
        return new RegularImmutableSortedSet(ImmutableList.m213of(r3), Ordering.natural());
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;TE;)Lcom/google/common/collect/ImmutableSortedSet<TE;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedSet m309of(Comparable r4, Comparable r5) {
        return construct(Ordering.natural(), 2, new Comparable[]{r4, r5});
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;TE;TE;)Lcom/google/common/collect/ImmutableSortedSet<TE;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedSet m310of(Comparable r4, Comparable r5, Comparable r6) {
        return construct(Ordering.natural(), 3, new Comparable[]{r4, r5, r6});
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;TE;TE;TE;)Lcom/google/common/collect/ImmutableSortedSet<TE;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedSet m311of(Comparable r4, Comparable r5, Comparable r6, Comparable r7) {
        return construct(Ordering.natural(), 4, new Comparable[]{r4, r5, r6, r7});
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;TE;TE;TE;TE;)Lcom/google/common/collect/ImmutableSortedSet<TE;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedSet m312of(Comparable r4, Comparable r5, Comparable r6, Comparable r7, Comparable r8) {
        return construct(Ordering.natural(), 5, new Comparable[]{r4, r5, r6, r7, r8});
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;TE;TE;TE;TE;TE;[TE;)Lcom/google/common/collect/ImmutableSortedSet<TE;>; */
    /* JADX INFO: renamed from: of */
    public static ImmutableSortedSet m313of(Comparable r4, Comparable r5, Comparable r6, Comparable r7, Comparable r8, Comparable r9, Comparable... r10) {
        Comparable[] r0 = new Comparable[r10.length + 6];
        r0[0] = r4;
        r0[1] = r5;
        r0[2] = r6;
        r0[3] = r7;
        r0[4] = r8;
        r0[5] = r9;
        System.arraycopy(r10, 0, r0, 6, r10.length);
        return construct(Ordering.natural(), r0.length, r0);
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSortedSet<E> m314of(E r1) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSortedSet<E> m315of(E r1, E r2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSortedSet<E> m316of(E r1, E r2, E r3) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSortedSet<E> m317of(E r1, E r2, E r3, E r4) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSortedSet<E> m318of(E r1, E r2, E r3, E r4, E r5) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    /* JADX INFO: renamed from: of */
    public static <E> ImmutableSortedSet<E> m319of(E r1, E r2, E r3, E r4, E r5, E r6, E... r7) {
        throw new UnsupportedOperationException();
    }

    public static <E> Builder<E> orderedBy(Comparator<E> r1) {
        return new Builder(r1);
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    public static <E extends Comparable<?>> Builder<E> reverseOrder() {
        return new Builder(Collections.reverseOrder());
    }

    @Deprecated
    public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
        throw new UnsupportedOperationException();
    }

    public static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(Comparator<? super E> r1) {
        return CollectCollectors.toImmutableSortedSet(r1);
    }

    static int unsafeCompare(Comparator<?> r2, Object r3, @CheckForNull Object r4) {
        return r2.compare(r3, r4);
    }

    @CheckForNull
    public E ceiling(E r3) {
        return (E) Iterables.getFirst(tailSet(r3, true), null);
    }

    @Override // java.util.SortedSet, com.google.common.collect.SortedIterable
    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    abstract ImmutableSortedSet<E> createDescendingSet();

    public abstract UnmodifiableIterator<E> descendingIterator();

    public /* bridge */ /* synthetic */ Iterator descendingIterator() {
        return descendingIterator();
    }

    public ImmutableSortedSet<E> descendingSet() {
        ImmutableSortedSet<E> r0 = this.descendingSet;
        if (r0 != null) goto L6;
        ImmutableSortedSet<E> r1 = createDescendingSet();
        this.descendingSet = r1;
        r1.descendingSet = this;
        return r1;
    L6:
        return r0;
    }

    public /* bridge */ /* synthetic */ NavigableSet descendingSet() {
        return descendingSet();
    }

    public E first() {
        return iterator().next();
    }

    @CheckForNull
    public E floor(E r3) {
        return (E) Iterators.getNext(headSet(r3, true).descendingIterator(), null);
    }

    public ImmutableSortedSet<E> headSet(E r2) {
        return headSet(r2, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ImmutableSortedSet<E> headSet(E r2, boolean r3) {
        return headSetImpl(Preconditions.checkNotNull(r2), r3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* bridge */ /* synthetic */ NavigableSet headSet(Object r1, boolean r2) {
        return headSet(r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* bridge */ /* synthetic */ SortedSet headSet(Object r1) {
        return headSet(r1);
    }

    abstract ImmutableSortedSet<E> headSetImpl(E r1, boolean r2);

    @CheckForNull
    public E higher(E r3) {
        return (E) Iterables.getFirst(tailSet(r3, false), null);
    }

    abstract int indexOf(@CheckForNull Object r1);

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public abstract UnmodifiableIterator<E> iterator();

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return iterator();
    }

    public E last() {
        return descendingIterator().next();
    }

    @CheckForNull
    public E lower(E r3) {
        return (E) Iterators.getNext(headSet(r3, false).descendingIterator(), null);
    }

    @Override // java.util.NavigableSet
    @CheckForNull
    @Deprecated
    public final E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableSet
    @CheckForNull
    @Deprecated
    public final E pollLast() {
        throw new UnsupportedOperationException();
    }

    public ImmutableSortedSet<E> subSet(E r3, E r4) {
        return subSet(r3, true, r4, false);
    }

    public ImmutableSortedSet<E> subSet(E r2, boolean r3, E r4, boolean r5) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r4);
        if (this.comparator.compare(r2, r4) > 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        return subSetImpl(r2, r3, r4, r5);
    L5:
        r0 = false;
        goto L6
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* bridge */ /* synthetic */ NavigableSet subSet(Object r1, boolean r2, Object r3, boolean r4) {
        return subSet(r1, r2, r3, r4);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* bridge */ /* synthetic */ SortedSet subSet(Object r1, Object r2) {
        return subSet(r1, r2);
    }

    abstract ImmutableSortedSet<E> subSetImpl(E r1, boolean r2, E r3, boolean r4);

    public ImmutableSortedSet<E> tailSet(E r2) {
        return tailSet(r2, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ImmutableSortedSet<E> tailSet(E r2, boolean r3) {
        return tailSetImpl(Preconditions.checkNotNull(r2), r3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* bridge */ /* synthetic */ NavigableSet tailSet(Object r1, boolean r2) {
        return tailSet(r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* bridge */ /* synthetic */ SortedSet tailSet(Object r1) {
        return tailSet(r1);
    }

    abstract ImmutableSortedSet<E> tailSetImpl(E r1, boolean r2);

    int unsafeCompare(Object r2, @CheckForNull Object r3) {
        return unsafeCompare(this.comparator, r2, r3);
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(this.comparator, toArray());
    }
}
