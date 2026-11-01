package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.Comparable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class RegularContiguousSet<C extends Comparable> extends ContiguousSet<C> {
    private static final long serialVersionUID = 0;
    private final Range<C> range;

    private static final class SerializedForm<C extends Comparable> implements Serializable {
        final DiscreteDomain<C> domain;
        final Range<C> range;

        private SerializedForm(Range<C> r1, DiscreteDomain<C> r2) {
            this.range = r1;
            this.domain = r2;
        }

        /* synthetic */ SerializedForm(Range r1, DiscreteDomain r2, C07171 r3) {
            this(r1, r2);
        }

        private Object readResolve() {
            return new RegularContiguousSet(this.range, this.domain);
        }
    }

    RegularContiguousSet(Range<C> r1, DiscreteDomain<C> r2) {
        super(r2);
        this.range = r1;
    }

    static /* synthetic */ boolean access$000(Comparable r1, Comparable r2) {
        return equalsOrThrow(r1, r2);
    }

    private static boolean equalsOrThrow(Comparable<?> r1, @CheckForNull Comparable<?> r2) {
        if (r2 != null) goto L4;
    L6:
        return false;
    L4:
        if (Range.compareOrThrow(r1, r2) != 0) goto L6;
        return true;
    }

    private ContiguousSet<C> intersectionInCurrentDomain(Range<C> r3) {
        if (this.range.isConnected(r3) == false) goto L6;
        return ContiguousSet.create(this.range.intersection(r3), this.domain);
    L6:
        return new EmptyContiguousSet(this.domain);
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@CheckForNull Object r4) {
        if (r4 != null) goto L9;
        return false;
    L9:
        Range<C> r2 = this.range;     // Catch: ClassCastException -> L7
        return r2.contains((Comparable) r4);
    L8:
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean containsAll(Collection<?> r2) {
        return Collections2.containsAllImpl(this, r2);
    }

    @Override // com.google.common.collect.ImmutableSet
    ImmutableList<C> createAsList() {
        if (this.domain.supportsFastOffset == false) goto L7;
        return new C07193(this);
    L7:
        return super.createAsList();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public UnmodifiableIterator<C> descendingIterator() {
        final Comparable r1 = last();
        return new C07182(this, r1);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public /* bridge */ /* synthetic */ Iterator descendingIterator() {
        return descendingIterator();
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public boolean equals(@CheckForNull Object r5) {
        if (r5 != this) goto L6;
        return true;
    L6:
        if ((r5 instanceof RegularContiguousSet) == false) goto L17;
        RegularContiguousSet r1 = (RegularContiguousSet) r5;
        if (this.domain.equals(r1.domain) == false) goto L17;
        if (first().equals(r1.first()) == true) goto L12;
    L14:
        return false;
    L12:
        if (last().equals(r1.last()) == false) goto L14;
        return true;
    L17:
        return super.equals(r5);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.SortedSet
    public C first() {
        return (C) Objects.requireNonNull(this.range.lowerBound.leastValueAbove(this.domain));
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.SortedSet
    public /* bridge */ /* synthetic */ Object first() {
        return first();
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public int hashCode() {
        return Sets.hashCodeImpl(this);
    }

    @Override // com.google.common.collect.ContiguousSet
    ContiguousSet<C> headSetImpl(C r2, boolean r3) {
        return intersectionInCurrentDomain(Range.upTo(r2, BoundType.forBoolean(r3)));
    }

    @Override // com.google.common.collect.ContiguousSet, com.google.common.collect.ImmutableSortedSet
    /* bridge */ /* synthetic */ ImmutableSortedSet headSetImpl(Object r1, boolean r2) {
        return headSetImpl((Comparable) r1, r2);
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    int indexOf(@CheckForNull Object r4) {
        if (contains(r4) == true) goto L6;
        return -1;
    L6:
        Comparable r0 = (Comparable) Objects.requireNonNull(r4);
        return (int) this.domain.distance(first(), r0);
    }

    @Override // com.google.common.collect.ContiguousSet
    public ContiguousSet<C> intersection(ContiguousSet<C> r5) {
        Preconditions.checkNotNull(r5);
        Preconditions.checkArgument(this.domain.equals(r5.domain));
        if (r5.isEmpty() == false) goto L5;
        return r5;
    L5:
        Comparable r0 = (Comparable) Ordering.natural().max(first(), r5.first());
        Comparable r1 = (Comparable) Ordering.natural().min(last(), r5.last());
        if (r0.compareTo(r1) > 0) goto L9;
        return ContiguousSet.create(Range.closed(r0, r1), this.domain);
    L9:
        return new EmptyContiguousSet(this.domain);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableCollection
    boolean isPartialView() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableSortedSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public UnmodifiableIterator<C> iterator() {
        final Comparable r1 = first();
        return new C07171(this, r1);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return iterator();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.SortedSet
    public C last() {
        return (C) Objects.requireNonNull(this.range.upperBound.greatestValueBelow(this.domain));
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.SortedSet
    public /* bridge */ /* synthetic */ Object last() {
        return last();
    }

    @Override // com.google.common.collect.ContiguousSet
    public Range<C> range() {
        return range(BoundType.CLOSED, BoundType.CLOSED);
    }

    @Override // com.google.common.collect.ContiguousSet
    public Range<C> range(BoundType r4, BoundType r5) {
        return Range.create(this.range.lowerBound.withLowerBoundType(r4, this.domain), this.range.upperBound.withUpperBoundType(r5, this.domain));
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        long r0 = this.domain.distance(first(), last());
        if (r0 < 2147483647L) goto L6;
        return Integer.MAX_VALUE;
    L6:
        return ((int) r0) + 1;
    }

    @Override // com.google.common.collect.ContiguousSet
    ContiguousSet<C> subSetImpl(C r3, boolean r4, C r5, boolean r6) {
        if (r3.compareTo(r5) != 0) goto L9;
        if (r4 == true) goto L9;
        if (r6 == true) goto L9;
        return new EmptyContiguousSet(this.domain);
    L9:
        return intersectionInCurrentDomain(Range.range(r3, BoundType.forBoolean(r4), r5, BoundType.forBoolean(r6)));
    }

    @Override // com.google.common.collect.ContiguousSet, com.google.common.collect.ImmutableSortedSet
    /* bridge */ /* synthetic */ ImmutableSortedSet subSetImpl(Object r1, boolean r2, Object r3, boolean r4) {
        return subSetImpl((Comparable) r1, r2, (Comparable) r3, r4);
    }

    @Override // com.google.common.collect.ContiguousSet
    ContiguousSet<C> tailSetImpl(C r2, boolean r3) {
        return intersectionInCurrentDomain(Range.downTo(r2, BoundType.forBoolean(r3)));
    }

    @Override // com.google.common.collect.ContiguousSet, com.google.common.collect.ImmutableSortedSet
    /* bridge */ /* synthetic */ ImmutableSortedSet tailSetImpl(Object r1, boolean r2) {
        return tailSetImpl((Comparable) r1, r2);
    }

    @Override // com.google.common.collect.ContiguousSet, com.google.common.collect.ImmutableSortedSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(this.range, this.domain, null);
    }
}
