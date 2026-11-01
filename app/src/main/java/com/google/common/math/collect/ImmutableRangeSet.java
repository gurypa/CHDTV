package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.Comparable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class ImmutableRangeSet<C extends Comparable> extends AbstractRangeSet<C> implements Serializable {
    private static final ImmutableRangeSet<Comparable<?>> ALL = null;
    private static final ImmutableRangeSet<Comparable<?>> EMPTY = null;

    @CheckForNull
    @LazyInit
    private transient ImmutableRangeSet<C> complement;
    private final transient ImmutableList<Range<C>> ranges;

    private final class AsSet extends ImmutableSortedSet<C> {
        private final DiscreteDomain<C> domain;

        @CheckForNull
        @LazyInit
        private transient Integer size;
        final /* synthetic */ ImmutableRangeSet this$0;

        AsSet(ImmutableRangeSet r1, DiscreteDomain<C> r2) {
            this.this$0 = r1;
            super(Ordering.natural());
            this.domain = r2;
        }

        static /* synthetic */ DiscreteDomain access$100(AsSet r1) {
            return r1.domain;
        }

        private void readObject(ObjectInputStream r3) throws InvalidObjectException {
            throw new InvalidObjectException("Use SerializedForm");
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r4) {
            if (r4 != null) goto L9;
            return false;
        L9:
            ImmutableRangeSet r2 = this.this$0;     // Catch: ClassCastException -> L7
            return r2.contains((Comparable) r4);
        L8:
            return false;
        }

        @Override // com.google.common.collect.ImmutableSortedSet
        ImmutableSortedSet<C> createDescendingSet() {
            return new DescendingImmutableSortedSet(this);
        }

        @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
        public UnmodifiableIterator<C> descendingIterator() {
            return new C06412(this);
        }

        @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
        public /* bridge */ /* synthetic */ Iterator descendingIterator() {
            return descendingIterator();
        }

        ImmutableSortedSet<C> headSetImpl(C r2, boolean r3) {
            return subSet(Range.upTo(r2, BoundType.forBoolean(r3)));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableSortedSet
        /* bridge */ /* synthetic */ ImmutableSortedSet headSetImpl(Object r1, boolean r2) {
            return headSetImpl((Comparable) r1, r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableSortedSet
        int indexOf(@CheckForNull Object r8) {
            if (contains(r8) == false) goto L14;
            Comparable r0 = (Comparable) Objects.requireNonNull(r8);
            long r1 = 0;
            UnmodifiableIterator r3 = ImmutableRangeSet.access$000(this.this$0).iterator();
        L6:
            if (r3.hasNext() == false) goto L13;
            Range r4 = (Range) r3.next();
            if (r4.contains(r0) == true) goto L10;
            r1 = r1 + ((long) ContiguousSet.create(r4, this.domain).size());
            goto L6
        L10:
            return Ints.saturatedCast(((long) ContiguousSet.create(r4, this.domain).indexOf(r0)) + r1);
        L13:
            throw new AssertionError("impossible");
        L14:
            return -1;
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return ImmutableRangeSet.access$000(this.this$0).isPartialView();
        }

        @Override // com.google.common.collect.ImmutableSortedSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public UnmodifiableIterator<C> iterator() {
            return new C06401(this);
        }

        @Override // com.google.common.collect.ImmutableSortedSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return iterator();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            Integer r0 = this.size;
            if (r0 != null) goto L13;
            long r1 = 0;
            UnmodifiableIterator r3 = ImmutableRangeSet.access$000(this.this$0).iterator();
        L6:
            if (r3.hasNext() == false) goto L11;
            r1 = r1 + ((long) ContiguousSet.create((Range) r3.next(), this.domain).size());
            if (r1 < 2147483647L) goto L6;
        L11:
            Integer r4 = Integer.valueOf(Ints.saturatedCast(r1));
            this.size = r4;
            r0 = r4;
        L13:
            return r0.intValue();
        }

        ImmutableSortedSet<C> subSet(Range<C> r3) {
            return this.this$0.subRangeSet(r3).asSet(this.domain);
        }

        ImmutableSortedSet<C> subSetImpl(C r3, boolean r4, C r5, boolean r6) {
            if (r4 == true) goto L9;
            if (r6 == true) goto L9;
            if (Range.compareOrThrow(r3, r5) != 0) goto L9;
            return ImmutableSortedSet.m307of();
        L9:
            return subSet(Range.range(r3, BoundType.forBoolean(r4), r5, BoundType.forBoolean(r6)));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableSortedSet
        /* bridge */ /* synthetic */ ImmutableSortedSet subSetImpl(Object r1, boolean r2, Object r3, boolean r4) {
            return subSetImpl((Comparable) r1, r2, (Comparable) r3, r4);
        }

        ImmutableSortedSet<C> tailSetImpl(C r2, boolean r3) {
            return subSet(Range.downTo(r2, BoundType.forBoolean(r3)));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableSortedSet
        /* bridge */ /* synthetic */ ImmutableSortedSet tailSetImpl(Object r1, boolean r2) {
            return tailSetImpl((Comparable) r1, r2);
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            return ImmutableRangeSet.access$000(this.this$0).toString();
        }

        @Override // com.google.common.collect.ImmutableSortedSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return new AsSetSerializedForm(ImmutableRangeSet.access$000(this.this$0), this.domain);
        }
    }

    private static class AsSetSerializedForm<C extends Comparable> implements Serializable {
        private final DiscreteDomain<C> domain;
        private final ImmutableList<Range<C>> ranges;

        AsSetSerializedForm(ImmutableList<Range<C>> r1, DiscreteDomain<C> r2) {
            this.ranges = r1;
            this.domain = r2;
        }

        Object readResolve() {
            return new ImmutableRangeSet(this.ranges).asSet(this.domain);
        }
    }

    public static class Builder<C extends Comparable<?>> {
        private final List<Range<C>> ranges;

        public Builder() {
            this.ranges = Lists.newArrayList();
        }

        public Builder<C> add(Range<C> r3) {
            Preconditions.checkArgument(!r3.isEmpty(), "range must not be empty, but was %s", r3);
            this.ranges.add(r3);
            return this;
        }

        public Builder<C> addAll(RangeSet<C> r2) {
            return addAll(r2.asRanges());
        }

        public Builder<C> addAll(Iterable<Range<C>> r3) {
            Iterator<Range<C>> r0 = r3.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            add(r0.next());
            goto L4
        L6:
            return this;
        }

        public ImmutableRangeSet<C> build() {
            ImmutableList.Builder r0 = new ImmutableList.Builder(this.ranges.size());
            Collections.sort(this.ranges, Range.rangeLexOrdering());
            PeekingIterator r1 = Iterators.peekingIterator(this.ranges.iterator());
        L4:
            if (r1.hasNext() == false) goto L12;
            Range r2 = (Range) r1.next();
        L7:
            if (r1.hasNext() == false) goto L11;
            Range<C> r3 = (Range) r1.peek();
            if (r2.isConnected(r3) == false) goto L11;
            Preconditions.checkArgument(r2.intersection(r3).isEmpty(), "Overlapping ranges not permitted but found %s overlapping %s", r2, r3);
            r2 = r2.span((Range) r1.next());
        L11:
            r0.add(r2);
            goto L4
        L12:
            ImmutableList r4 = r0.build();
            if (r4.isEmpty() == false) goto L17;
            return ImmutableRangeSet.m257of();
        L17:
            if (r4.size() != 1) goto L23;
            if (((Range) Iterables.getOnlyElement(r4)).equals(Range.all()) == false) goto L23;
            return ImmutableRangeSet.all();
        L23:
            return new ImmutableRangeSet(r4);
        }

        Builder<C> combine(Builder<C> r2) {
            addAll(r2.ranges);
            return this;
        }
    }

    private final class ComplementRanges extends ImmutableList<Range<C>> {
        private final boolean positiveBoundedAbove;
        private final boolean positiveBoundedBelow;
        private final int size;
        final /* synthetic */ ImmutableRangeSet this$0;

        /* JADX WARN: Multi-variable type inference failed */
        ComplementRanges(ImmutableRangeSet r3) {
            this.this$0 = r3;
            this.positiveBoundedBelow = ((Range) ImmutableRangeSet.access$000(r3).get(0)).hasLowerBound();
            this.positiveBoundedAbove = ((Range) Iterables.getLast(ImmutableRangeSet.access$000(r3))).hasUpperBound();
            int r4 = ImmutableRangeSet.access$000(r3).size() - 1;
            if (this.positiveBoundedBelow == false) goto L6;
            r4 = r4 + 1;
        L6:
            if (this.positiveBoundedAbove == false) goto L8;
            r4 = r4 + 1;
        L8:
            this.size = r4;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.List
        public Range<C> get(int r4) {
            Preconditions.checkElementIndex(r4, this.size);
            if (this.positiveBoundedBelow == false) goto L8;
            if (r4 != 0) goto L6;
            Cut<C> r0 = Cut.belowAll();
        L10:
            if (this.positiveBoundedAbove == true) goto L12;
        L14:
            Cut<C> r1 = ((Range) ImmutableRangeSet.access$000(this.this$0).get((!this.positiveBoundedBelow ? 1 : 0) + r4)).lowerBound;
        L16:
            return Range.create(r0, r1);
        L12:
            if (r4 != (this.size - 1)) goto L14;
            r1 = Cut.aboveAll();
            goto L16
        L6:
            r0 = ((Range) ImmutableRangeSet.access$000(this.this$0).get(r4 - 1)).upperBound;
            goto L10
        L8:
            r0 = ((Range) ImmutableRangeSet.access$000(this.this$0).get(r4)).upperBound;
            goto L10
        }

        @Override // java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.size;
        }

        @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    private static final class SerializedForm<C extends Comparable> implements Serializable {
        private final ImmutableList<Range<C>> ranges;

        SerializedForm(ImmutableList<Range<C>> r1) {
            this.ranges = r1;
        }

        Object readResolve() {
            if (this.ranges.isEmpty() == false) goto L7;
            return ImmutableRangeSet.m257of();
        L7:
            if (this.ranges.equals(ImmutableList.m213of(Range.all())) == false) goto L11;
            return ImmutableRangeSet.all();
        L11:
            return new ImmutableRangeSet(this.ranges);
        }
    }

    static {
        EMPTY = new ImmutableRangeSet(ImmutableList.m212of());
        ALL = new ImmutableRangeSet(ImmutableList.m213of(Range.all()));
    }

    ImmutableRangeSet(ImmutableList<Range<C>> r1) {
        this.ranges = r1;
    }

    private ImmutableRangeSet(ImmutableList<Range<C>> r1, ImmutableRangeSet<C> r2) {
        this.ranges = r1;
        this.complement = r2;
    }

    static /* synthetic */ ImmutableList access$000(ImmutableRangeSet r1) {
        return r1.ranges;
    }

    static <C extends Comparable> ImmutableRangeSet<C> all() {
        return ALL;
    }

    public static <C extends Comparable<?>> Builder<C> builder() {
        return new Builder();
    }

    public static <C extends Comparable> ImmutableRangeSet<C> copyOf(RangeSet<C> r2) {
        Preconditions.checkNotNull(r2);
        if (r2.isEmpty() == false) goto L7;
        return m257of();
    L7:
        if (r2.encloses(Range.all()) == false) goto L11;
        return all();
    L11:
        if ((r2 instanceof ImmutableRangeSet) == false) goto L16;
        ImmutableRangeSet<C> r0 = (ImmutableRangeSet) r2;
        if (r0.isPartialView() == true) goto L16;
        return r0;
    L16:
        return new ImmutableRangeSet(ImmutableList.copyOf(r2.asRanges()));
    }

    public static <C extends Comparable<?>> ImmutableRangeSet<C> copyOf(Iterable<Range<C>> r1) {
        return new Builder().addAll(r1).build();
    }

    private ImmutableList<Range<C>> intersectRanges(final Range<C> r7) {
        if (this.ranges.isEmpty() == true) goto L26;
        if (r7.isEmpty() == true) goto L26;
        if (r7.encloses(span()) == false) goto L12;
        return this.ranges;
    L12:
        if (r7.hasLowerBound() == false) goto L14;
        final int r0 = SortedLists.binarySearch(this.ranges, new ImmutableRangeMap$$ExternalSyntheticLambda0(), r7.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
    L16:
        if (r7.hasUpperBound() == false) goto L18;
        int r1 = SortedLists.binarySearch(this.ranges, new ImmutableRangeMap$$ExternalSyntheticLambda1(), r7.upperBound, SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
    L19:
        final int r2 = r1 - r0;
        if (r2 != 0) goto L24;
        return ImmutableList.m212of();
    L24:
        return (ImmutableList<Range<C>>) new C06391(this, r2, r0, r7);
    L18:
        r1 = this.ranges.size();
        goto L19
    L14:
        r0 = 0;
    L26:
        return ImmutableList.m212of();
    }

    /* JADX INFO: renamed from: of */
    public static <C extends Comparable> ImmutableRangeSet<C> m257of() {
        return EMPTY;
    }

    /* JADX INFO: renamed from: of */
    public static <C extends Comparable> ImmutableRangeSet<C> m258of(Range<C> r2) {
        Preconditions.checkNotNull(r2);
        if (r2.isEmpty() == false) goto L7;
        return m257of();
    L7:
        if (r2.equals(Range.all()) == false) goto L11;
        return all();
    L11:
        return new ImmutableRangeSet(ImmutableList.m213of(r2));
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    public static <E extends Comparable<? super E>> Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet() {
        return CollectCollectors.toImmutableRangeSet();
    }

    public static <C extends Comparable<?>> ImmutableRangeSet<C> unionOf(Iterable<Range<C>> r1) {
        return copyOf(TreeRangeSet.create(r1));
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    @Deprecated
    public void add(Range<C> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    @Deprecated
    public void addAll(RangeSet<C> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    @Deprecated
    public void addAll(Iterable<Range<C>> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.RangeSet
    public ImmutableSet<Range<C>> asDescendingSetOfRanges() {
        if (this.ranges.isEmpty() == false) goto L7;
        return ImmutableSet.m259of();
    L7:
        return new RegularImmutableSortedSet(this.ranges.reverse(), Range.rangeLexOrdering().reverse());
    }

    @Override // com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ Set asDescendingSetOfRanges() {
        return asDescendingSetOfRanges();
    }

    @Override // com.google.common.collect.RangeSet
    public ImmutableSet<Range<C>> asRanges() {
        if (this.ranges.isEmpty() == false) goto L7;
        return ImmutableSet.m259of();
    L7:
        return new RegularImmutableSortedSet(this.ranges, Range.rangeLexOrdering());
    }

    @Override // com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ Set asRanges() {
        return asRanges();
    }

    public ImmutableSortedSet<C> asSet(DiscreteDomain<C> r5) {
        Preconditions.checkNotNull(r5);
        if (isEmpty() == true) goto L5;
        Range<C> r0 = span().canonical(r5);
        if (r0.hasLowerBound() == false) goto L18;
        if (r0.hasUpperBound() == true) goto L16;
        r5.maxValue();     // Catch: NoSuchElementException -> L12
    L14:
        throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded above");
    L16:
        return new AsSet(this, r5);
    L18:
        throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded below");
    L5:
        return ImmutableSortedSet.m307of();
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    @Override // com.google.common.collect.RangeSet
    public ImmutableRangeSet<C> complement() {
        ImmutableRangeSet<C> r0 = this.complement;
        if (r0 == null) goto L6;
        return r0;
    L6:
        if (this.ranges.isEmpty() == false) goto L10;
        ImmutableRangeSet<C> r1 = all();
        this.complement = r1;
        return r1;
    L10:
        if (this.ranges.size() == 1) goto L12;
    L15:
        ImmutableRangeSet<C> r2 = new ImmutableRangeSet(new ComplementRanges(this), this);
        this.complement = r2;
        return r2;
    L12:
        if (this.ranges.get(0).equals(Range.all()) == false) goto L15;
        ImmutableRangeSet<C> r3 = m257of();
        this.complement = r3;
        return r3;
    }

    @Override // com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ RangeSet complement() {
        return complement();
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ boolean contains(Comparable r1) {
        return super.contains(r1);
    }

    public ImmutableRangeSet<C> difference(RangeSet<C> r3) {
        TreeRangeSet r0 = TreeRangeSet.create(this);
        r0.removeAll(r3);
        return copyOf(r0);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public boolean encloses(Range<C> r7) {
        int r0 = SortedLists.binarySearch(this.ranges, new ImmutableRangeMap$$ExternalSyntheticLambda1(), r7.lowerBound, Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
        if (r0 != (-1)) goto L5;
    L7:
        return false;
    L5:
        if (this.ranges.get(r0).encloses(r7) == false) goto L7;
        return true;
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ boolean enclosesAll(RangeSet r1) {
        return super.enclosesAll(r1);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ boolean enclosesAll(Iterable r1) {
        return super.enclosesAll(r1);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ boolean equals(@CheckForNull Object r1) {
        return super.equals(r1);
    }

    public ImmutableRangeSet<C> intersection(RangeSet<C> r3) {
        TreeRangeSet r0 = TreeRangeSet.create(this);
        r0.removeAll(r3.complement());
        return copyOf(r0);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public boolean intersects(Range<C> r7) {
        int r0 = SortedLists.binarySearch(this.ranges, new ImmutableRangeMap$$ExternalSyntheticLambda1(), r7.lowerBound, Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        if (r0 < this.ranges.size()) goto L5;
    L9:
        if (r0 > 0) goto L11;
    L15:
        return false;
    L11:
        if (this.ranges.get(r0 - 1).isConnected(r7) == false) goto L15;
        if (this.ranges.get(r0 - 1).intersection(r7).isEmpty() == true) goto L15;
        return true;
    L5:
        if (this.ranges.get(r0).isConnected(r7) == false) goto L9;
        if (this.ranges.get(r0).intersection(r7).isEmpty() == true) goto L9;
        return true;
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public boolean isEmpty() {
        return this.ranges.isEmpty();
    }

    boolean isPartialView() {
        return this.ranges.isPartialView();
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    @CheckForNull
    public Range<C> rangeContaining(C r7) {
        int r0 = SortedLists.binarySearch(this.ranges, new ImmutableRangeMap$$ExternalSyntheticLambda1(), Cut.belowValue(r7), Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
        if (r0 == (-1)) goto L8;
        Range<C> r1 = this.ranges.get(r0);
        if (r1.contains(r7) == true) goto L7;
        return null;
    L7:
        return r1;
    L8:
        return null;
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    @Deprecated
    public void remove(Range<C> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    @Deprecated
    public void removeAll(RangeSet<C> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    @Deprecated
    public void removeAll(Iterable<Range<C>> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.RangeSet
    public Range<C> span() {
        if (this.ranges.isEmpty() == true) goto L7;
        return Range.create(this.ranges.get(0).lowerBound, this.ranges.get(this.ranges.size() - 1).upperBound);
    L7:
        throw new NoSuchElementException();
    }

    @Override // com.google.common.collect.RangeSet
    public ImmutableRangeSet<C> subRangeSet(Range<C> r4) {
        if (isEmpty() == true) goto L12;
        Range<C> r0 = span();
        if (r4.encloses(r0) == false) goto L8;
        return this;
    L8:
        if (r4.isConnected(r0) == false) goto L12;
        return new ImmutableRangeSet(intersectRanges(r4));
    L12:
        return m257of();
    }

    @Override // com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ RangeSet subRangeSet(Range r1) {
        return subRangeSet(r1);
    }

    public ImmutableRangeSet<C> union(RangeSet<C> r3) {
        return unionOf(Iterables.concat(asRanges(), r3.asRanges()));
    }

    Object writeReplace() {
        return new SerializedForm(this.ranges);
    }
}
