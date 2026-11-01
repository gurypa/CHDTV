package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.lang.Comparable;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedSet;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ContiguousSet<C extends Comparable> extends ImmutableSortedSet<C> {
    final DiscreteDomain<C> domain;

    ContiguousSet(DiscreteDomain<C> r2) {
        super(Ordering.natural());
        this.domain = r2;
    }

    @Deprecated
    public static <E> ImmutableSortedSet.Builder<E> builder() {
        throw new UnsupportedOperationException();
    }

    public static ContiguousSet<Integer> closed(int r2, int r3) {
        return create(Range.closed(Integer.valueOf(r2), Integer.valueOf(r3)), DiscreteDomain.integers());
    }

    public static ContiguousSet<Long> closed(long r2, long r4) {
        return create(Range.closed(Long.valueOf(r2), Long.valueOf(r4)), DiscreteDomain.longs());
    }

    public static ContiguousSet<Integer> closedOpen(int r2, int r3) {
        return create(Range.closedOpen(Integer.valueOf(r2), Integer.valueOf(r3)), DiscreteDomain.integers());
    }

    public static ContiguousSet<Long> closedOpen(long r2, long r4) {
        return create(Range.closedOpen(Long.valueOf(r2), Long.valueOf(r4)), DiscreteDomain.longs());
    }

    public static <C extends Comparable> ContiguousSet<C> create(Range<C> r4, DiscreteDomain<C> r5) {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        Range<C> r0 = r4;
    L22:
        e = move-exception;
        throw new IllegalArgumentException(e);
    L4:
        if (r4.hasLowerBound() == true) goto L7;
        r0 = r0.intersection(Range.atLeast(r5.minValue()));     // Catch: NoSuchElementException -> L22
    L7:
        if (r4.hasUpperBound() == true) goto L11;
        r0 = r0.intersection(Range.atMost(r5.maxValue()));     // Catch: NoSuchElementException -> L22
    L11:
        if (r0.isEmpty() == false) goto L14;
        boolean r1 = true;
    L18:
        if (r1 == false) goto L21;
        return new EmptyContiguousSet(r5);
    L21:
        return new RegularContiguousSet(r0, r5);
    L14:
        if (Range.compareOrThrow((Comparable) Objects.requireNonNull(r4.lowerBound.leastValueAbove(r5)), (Comparable) Objects.requireNonNull(r4.upperBound.greatestValueBelow(r5))) <= 0) goto L16;
        boolean r3 = true;
    L17:
        r1 = r3;
        goto L18
    L16:
        r3 = false;
        goto L17
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<C> createDescendingSet() {
        return new DescendingImmutableSortedSet(this);
    }

    public ContiguousSet<C> headSet(C r3) {
        return headSetImpl((Comparable) Preconditions.checkNotNull(r3), false);
    }

    public ContiguousSet<C> headSet(C r2, boolean r3) {
        return headSetImpl((Comparable) Preconditions.checkNotNull(r2), r3);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet, java.util.SortedSet
    public /* bridge */ /* synthetic */ ImmutableSortedSet headSet(Object r1) {
        return headSet((Comparable) r1);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public /* bridge */ /* synthetic */ ImmutableSortedSet headSet(Object r1, boolean r2) {
        return headSet((Comparable) r1, r2);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public /* bridge */ /* synthetic */ NavigableSet headSet(Object r1, boolean r2) {
        return headSet((Comparable) r1, r2);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet, java.util.SortedSet
    public /* bridge */ /* synthetic */ SortedSet headSet(Object r1) {
        return headSet((Comparable) r1);
    }

    abstract ContiguousSet<C> headSetImpl(C r1, boolean r2);

    @Override // com.google.common.collect.ImmutableSortedSet
    /* bridge */ /* synthetic */ ImmutableSortedSet headSetImpl(Object r1, boolean r2) {
        return headSetImpl((Comparable) r1, r2);
    }

    public abstract ContiguousSet<C> intersection(ContiguousSet<C> r1);

    public abstract Range<C> range();

    public abstract Range<C> range(BoundType r1, BoundType r2);

    public ContiguousSet<C> subSet(C r4, C r5) {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        if (comparator().compare(r4, r5) > 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        return subSetImpl(r4, true, r5, false);
    L5:
        r0 = false;
        goto L6
    }

    public ContiguousSet<C> subSet(C r2, boolean r3, C r4, boolean r5) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r4);
        if (comparator().compare(r2, r4) > 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        return subSetImpl(r2, r3, r4, r5);
    L5:
        r0 = false;
        goto L6
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet, java.util.SortedSet
    public /* bridge */ /* synthetic */ ImmutableSortedSet subSet(Object r1, Object r2) {
        return subSet((Comparable) r1, (Comparable) r2);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public /* bridge */ /* synthetic */ ImmutableSortedSet subSet(Object r1, boolean r2, Object r3, boolean r4) {
        return subSet((Comparable) r1, r2, (Comparable) r3, r4);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public /* bridge */ /* synthetic */ NavigableSet subSet(Object r1, boolean r2, Object r3, boolean r4) {
        return subSet((Comparable) r1, r2, (Comparable) r3, r4);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet, java.util.SortedSet
    public /* bridge */ /* synthetic */ SortedSet subSet(Object r1, Object r2) {
        return subSet((Comparable) r1, (Comparable) r2);
    }

    abstract ContiguousSet<C> subSetImpl(C r1, boolean r2, C r3, boolean r4);

    @Override // com.google.common.collect.ImmutableSortedSet
    /* bridge */ /* synthetic */ ImmutableSortedSet subSetImpl(Object r1, boolean r2, Object r3, boolean r4) {
        return subSetImpl((Comparable) r1, r2, (Comparable) r3, r4);
    }

    public ContiguousSet<C> tailSet(C r3) {
        return tailSetImpl((Comparable) Preconditions.checkNotNull(r3), true);
    }

    public ContiguousSet<C> tailSet(C r2, boolean r3) {
        return tailSetImpl((Comparable) Preconditions.checkNotNull(r2), r3);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet, java.util.SortedSet
    public /* bridge */ /* synthetic */ ImmutableSortedSet tailSet(Object r1) {
        return tailSet((Comparable) r1);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public /* bridge */ /* synthetic */ ImmutableSortedSet tailSet(Object r1, boolean r2) {
        return tailSet((Comparable) r1, r2);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public /* bridge */ /* synthetic */ NavigableSet tailSet(Object r1, boolean r2) {
        return tailSet((Comparable) r1, r2);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet, java.util.SortedSet
    public /* bridge */ /* synthetic */ SortedSet tailSet(Object r1) {
        return tailSet((Comparable) r1);
    }

    abstract ContiguousSet<C> tailSetImpl(C r1, boolean r2);

    @Override // com.google.common.collect.ImmutableSortedSet
    /* bridge */ /* synthetic */ ImmutableSortedSet tailSetImpl(Object r1, boolean r2) {
        return tailSetImpl((Comparable) r1, r2);
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        return range().toString();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return super.writeReplace();
    }
}
