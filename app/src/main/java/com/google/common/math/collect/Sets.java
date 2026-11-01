package com.google.common.collect;

import com.arthenica.ffmpegkit.MediaInformation;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.math.IntMath;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Sets {

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* JADX INFO: renamed from: com.google.common.collect.Sets$5 */
    class C07285<E> extends AbstractSet<Set<E>> {
        final /* synthetic */ ImmutableMap val$index;
        final /* synthetic */ int val$size;

        /* JADX INFO: renamed from: com.google.common.collect.Sets$5$1, reason: invalid class name */
        class AnonymousClass1 extends AbstractIterator<Set<E>> {
            final BitSet bits;
            final /* synthetic */ C07285 this$0;

            AnonymousClass1(C07285 r3) {
                this.this$0 = r3;
                this.bits = new BitSet(this.this$0.val$index.size());
            }

            @Override // com.google.common.collect.AbstractIterator
            @CheckForNull
            protected /* bridge */ /* synthetic */ Object computeNext() {
                return computeNext();
            }

            @Override // com.google.common.collect.AbstractIterator
            @CheckForNull
            protected Set<E> computeNext() {
                if (this.bits.isEmpty() == false) goto L5;
                this.bits.set(0, this.this$0.val$size);
            L10:
                final BitSet r0 = (BitSet) this.bits.clone();
                return new C11691(this, r0);
            L5:
                int r1 = this.bits.nextSetBit(0);
                int r2 = this.bits.nextClearBit(r1);
                if (r2 == this.this$0.val$index.size()) goto L8;
                this.bits.set(0, (r2 - r1) - 1);
                this.bits.clear((r2 - r1) - 1, r2);
                this.bits.set(r2);
                goto L10
            L8:
                return endOfData();
            }
        }

        C07285(int r1, ImmutableMap r2) {
            this.val$size = r1;
            this.val$index = r2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r5) {
            if ((r5 instanceof Set) == false) goto L10;
            Set r0 = (Set) r5;
            if (r0.size() == this.val$size) goto L7;
            return false;
        L7:
            if (this.val$index.keySet().containsAll(r0) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Set<E>> iterator() {
            return new AnonymousClass1(this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return IntMath.binomial(this.val$index.size(), this.val$size);
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            return "Sets.combinations(" + this.val$index.keySet() + ", " + this.val$size + ")";
        }
    }

    private static final class CartesianSet<E> extends ForwardingCollection<List<E>> implements Set<List<E>> {
        private final transient ImmutableList<ImmutableSet<E>> axes;
        private final transient CartesianList<E> delegate;

        private CartesianSet(ImmutableList<ImmutableSet<E>> r1, CartesianList<E> r2) {
            this.axes = r1;
            this.delegate = r2;
        }

        static <E> Set<List<E>> create(List<? extends Set<? extends E>> r5) {
            ImmutableList.Builder r0 = new ImmutableList.Builder(r5.size());
            Iterator<? extends Set<? extends E>> r1 = r5.iterator();
        L4:
            if (r1.hasNext() == false) goto L10;
            ImmutableSet r3 = ImmutableSet.copyOf(r1.next());
            if (r3.isEmpty() == true) goto L8;
            r0.add(r3);
            goto L4
        L8:
            return ImmutableSet.m259of();
        L10:
            final ImmutableList<E> r2 = r0.build();
            return new CartesianSet(r2, new CartesianList(new C07291(r2)));
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r7) {
            if ((r7 instanceof List) == true) goto L5;
            return false;
        L5:
            List r0 = (List) r7;
            if (r0.size() == this.axes.size()) goto L8;
            return false;
        L8:
            int r2 = 0;
            Iterator<E> r3 = r0.iterator();
        L10:
            if (r3.hasNext() == false) goto L15;
            E r4 = r3.next();
            if (this.axes.get(r2).contains(r4) == false) goto L13;
            r2 = r2 + 1;
            goto L10
        L13:
            return false;
        L15:
            return true;
        }

        @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected Collection<List<E>> delegate() {
            return this.delegate;
        }

        @Override // java.util.Collection, java.util.Set
        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof CartesianSet) == false) goto L7;
            return this.axes.equals(((CartesianSet) r5).axes);
        L7:
            if ((r5 instanceof Set) == false) goto L14;
            Set r0 = (Set) r5;
            if (size() == r0.size()) goto L11;
            return false;
        L11:
            if (containsAll(r0) == false) goto L16;
            return true;
        L16:
            return false;
        L14:
            return false;
        }

        @Override // java.util.Collection, java.util.Set
        public int hashCode() {
            int r0 = size() - 1;
            int r1 = 0;
        L4:
            if (r1 >= this.axes.size()) goto L6;
            r0 = ~(~(r0 * 31));
            r1 = r1 + 1;
            goto L4
        L6:
            int r2 = 1;
            UnmodifiableIterator<ImmutableSet<E>> r3 = this.axes.iterator();
        L8:
            if (r3.hasNext() == false) goto L11;
            ImmutableSet<E> r4 = r3.next();
            r2 = ~(~((r2 * 31) + ((size() / r4.size()) * r4.hashCode())));
            goto L8
        L11:
            return ~(~(r2 + r0));
        }
    }

    static class DescendingSet<E> extends ForwardingNavigableSet<E> {
        private final NavigableSet<E> forward;

        DescendingSet(NavigableSet<E> r1) {
            this.forward = r1;
        }

        private static <T> Ordering<T> reverse(Comparator<T> r1) {
            return Ordering.from(r1).reverse();
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, java.util.NavigableSet
        @CheckForNull
        public E ceiling(@ParametricNullness E r2) {
            return this.forward.floor(r2);
        }

        @Override // com.google.common.collect.ForwardingSortedSet, java.util.SortedSet
        public Comparator<? super E> comparator() {
            Comparator<? super E> r0 = this.forward.comparator();
            if (r0 != null) goto L7;
            return Ordering.natural().reverse();
        L7:
            return reverse(r0);
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, com.google.common.collect.ForwardingSortedSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, com.google.common.collect.ForwardingSortedSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Collection delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, com.google.common.collect.ForwardingSortedSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected NavigableSet<E> delegate() {
            return this.forward;
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, com.google.common.collect.ForwardingSortedSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Set delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, com.google.common.collect.ForwardingSortedSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ SortedSet delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, java.util.NavigableSet
        public Iterator<E> descendingIterator() {
            return this.forward.iterator();
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, java.util.NavigableSet
        public NavigableSet<E> descendingSet() {
            return this.forward;
        }

        @Override // com.google.common.collect.ForwardingSortedSet, java.util.SortedSet
        @ParametricNullness
        public E first() {
            return this.forward.last();
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, java.util.NavigableSet
        @CheckForNull
        public E floor(@ParametricNullness E r2) {
            return this.forward.ceiling(r2);
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, java.util.NavigableSet
        public NavigableSet<E> headSet(@ParametricNullness E r2, boolean r3) {
            return this.forward.tailSet(r2, r3).descendingSet();
        }

        @Override // com.google.common.collect.ForwardingSortedSet, java.util.SortedSet
        public SortedSet<E> headSet(@ParametricNullness E r2) {
            return standardHeadSet(r2);
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, java.util.NavigableSet
        @CheckForNull
        public E higher(@ParametricNullness E r2) {
            return this.forward.lower(r2);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<E> iterator() {
            return this.forward.descendingIterator();
        }

        @Override // com.google.common.collect.ForwardingSortedSet, java.util.SortedSet
        @ParametricNullness
        public E last() {
            return this.forward.first();
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, java.util.NavigableSet
        @CheckForNull
        public E lower(@ParametricNullness E r2) {
            return this.forward.higher(r2);
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, java.util.NavigableSet
        @CheckForNull
        public E pollFirst() {
            return this.forward.pollLast();
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, java.util.NavigableSet
        @CheckForNull
        public E pollLast() {
            return this.forward.pollFirst();
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, java.util.NavigableSet
        public NavigableSet<E> subSet(@ParametricNullness E r2, boolean r3, @ParametricNullness E r4, boolean r5) {
            return this.forward.subSet(r4, r5, r2, r3).descendingSet();
        }

        @Override // com.google.common.collect.ForwardingSortedSet, java.util.SortedSet
        public SortedSet<E> subSet(@ParametricNullness E r2, @ParametricNullness E r3) {
            return standardSubSet(r2, r3);
        }

        @Override // com.google.common.collect.ForwardingNavigableSet, java.util.NavigableSet
        public NavigableSet<E> tailSet(@ParametricNullness E r2, boolean r3) {
            return this.forward.headSet(r2, r3).descendingSet();
        }

        @Override // com.google.common.collect.ForwardingSortedSet, java.util.SortedSet
        public SortedSet<E> tailSet(@ParametricNullness E r2) {
            return standardTailSet(r2);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return standardToArray();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] r2) {
            return (T[]) standardToArray(r2);
        }

        @Override // com.google.common.collect.ForwardingObject
        public String toString() {
            return standardToString();
        }
    }

    private static class FilteredNavigableSet<E> extends FilteredSortedSet<E> implements NavigableSet<E> {
        FilteredNavigableSet(NavigableSet<E> r1, Predicate<? super E> r2) {
            super(r1, r2);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public E ceiling(@ParametricNullness E r4) {
            return (E) Iterables.find(unfiltered().tailSet(r4, true), this.predicate, null);
        }

        @Override // java.util.NavigableSet
        public Iterator<E> descendingIterator() {
            return Iterators.filter(unfiltered().descendingIterator(), this.predicate);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> descendingSet() {
            return Sets.filter(unfiltered().descendingSet(), this.predicate);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public E floor(@ParametricNullness E r4) {
            return (E) Iterators.find(unfiltered().headSet(r4, true).descendingIterator(), this.predicate, null);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> headSet(@ParametricNullness E r3, boolean r4) {
            return Sets.filter(unfiltered().headSet(r3, r4), this.predicate);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public E higher(@ParametricNullness E r4) {
            return (E) Iterables.find(unfiltered().tailSet(r4, false), this.predicate, null);
        }

        @Override // com.google.common.collect.Sets.FilteredSortedSet, java.util.SortedSet
        @ParametricNullness
        public E last() {
            return (E) Iterators.find(unfiltered().descendingIterator(), this.predicate);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public E lower(@ParametricNullness E r4) {
            return (E) Iterators.find(unfiltered().headSet(r4, false).descendingIterator(), this.predicate, null);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public E pollFirst() {
            return (E) Iterables.removeFirstMatching(unfiltered(), this.predicate);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public E pollLast() {
            return (E) Iterables.removeFirstMatching(unfiltered().descendingSet(), this.predicate);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> subSet(@ParametricNullness E r3, boolean r4, @ParametricNullness E r5, boolean r6) {
            return Sets.filter(unfiltered().subSet(r3, r4, r5, r6), this.predicate);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> tailSet(@ParametricNullness E r3, boolean r4) {
            return Sets.filter(unfiltered().tailSet(r3, r4), this.predicate);
        }

        NavigableSet<E> unfiltered() {
            return (NavigableSet) this.unfiltered;
        }
    }

    private static class FilteredSet<E> extends Collections2.FilteredCollection<E> implements Set<E> {
        FilteredSet(Set<E> r1, Predicate<? super E> r2) {
            super(r1, r2);
        }

        @Override // java.util.Collection, java.util.Set
        public boolean equals(@CheckForNull Object r2) {
            return Sets.equalsImpl(this, r2);
        }

        @Override // java.util.Collection, java.util.Set
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    private static class FilteredSortedSet<E> extends FilteredSet<E> implements SortedSet<E> {
        FilteredSortedSet(SortedSet<E> r1, Predicate<? super E> r2) {
            super(r1, r2);
        }

        @Override // java.util.SortedSet
        @CheckForNull
        public Comparator<? super E> comparator() {
            return ((SortedSet) this.unfiltered).comparator();
        }

        @Override // java.util.SortedSet
        @ParametricNullness
        public E first() {
            return (E) Iterators.find(this.unfiltered.iterator(), this.predicate);
        }

        @Override // java.util.SortedSet
        public SortedSet<E> headSet(@ParametricNullness E r4) {
            return new FilteredSortedSet(((SortedSet) this.unfiltered).headSet(r4), this.predicate);
        }

        @ParametricNullness
        public E last() {
            SortedSet r0 = (SortedSet) this.unfiltered;
        L3:
            E r1 = (Object) r0.last();
            if (this.predicate.apply(r1) == true) goto L5;
            r0 = r0.headSet(r1);
            goto L3
        L5:
            return r1;
        }

        @Override // java.util.SortedSet
        public SortedSet<E> subSet(@ParametricNullness E r4, @ParametricNullness E r5) {
            return new FilteredSortedSet(((SortedSet) this.unfiltered).subSet(r4, r5), this.predicate);
        }

        @Override // java.util.SortedSet
        public SortedSet<E> tailSet(@ParametricNullness E r4) {
            return new FilteredSortedSet(((SortedSet) this.unfiltered).tailSet(r4), this.predicate);
        }
    }

    static abstract class ImprovedAbstractSet<E> extends AbstractSet<E> {
        ImprovedAbstractSet() {
        }

        @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> r2) {
            return Sets.removeAllImpl(this, r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> r2) {
            return super.retainAll((Collection) Preconditions.checkNotNull(r2));
        }
    }

    private static final class PowerSet<E> extends AbstractSet<Set<E>> {
        final ImmutableMap<E, Integer> inputSet;

        PowerSet(Set<E> r4) {
            if (r4.size() > 30) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "Too many elements to create power set: %s > 30", r4.size());
            this.inputSet = Maps.indexMap(r4);
            return;
        L5:
            r0 = false;
            goto L6
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r3) {
            if ((r3 instanceof Set) == false) goto L6;
            ImmutableSet<E> r1 = this.inputSet.keySet();
            return r1.containsAll((Set) r3);
        L6:
            return false;
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof PowerSet) == false) goto L7;
            return this.inputSet.keySet().equals(((PowerSet) r4).inputSet.keySet());
        L7:
            return super.equals(r4);
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
        public int hashCode() {
            return this.inputSet.keySet().hashCode() << (this.inputSet.size() - 1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Set<E>> iterator() {
            final int r1 = size();
            return new C07301(this, r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return 1 << this.inputSet.size();
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            return "powerSet(" + this.inputSet + ")";
        }
    }

    public static abstract class SetView<E> extends AbstractSet<E> {
        private SetView() {
        }

        /* synthetic */ SetView(C07241 r1) {
            this();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        @Deprecated
        public final boolean add(@ParametricNullness E r2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        @Deprecated
        public final boolean addAll(Collection<? extends E> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        @Deprecated
        public final void clear() {
            throw new UnsupportedOperationException();
        }

        public <S extends Set<E>> S copyInto(S r1) {
            r1.addAll(this);
            return r1;
        }

        public ImmutableSet<E> immutableCopy() {
            return ImmutableSet.copyOf(this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public abstract UnmodifiableIterator<E> iterator();

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        @Deprecated
        public final boolean remove(@CheckForNull Object r2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        @Deprecated
        public final boolean removeAll(Collection<?> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        @Deprecated
        public final boolean retainAll(Collection<?> r2) {
            throw new UnsupportedOperationException();
        }
    }

    private static final class SubSet<E> extends AbstractSet<E> {
        private final ImmutableMap<E, Integer> inputSet;
        private final int mask;

        SubSet(ImmutableMap<E, Integer> r1, int r2) {
            this.inputSet = r1;
            this.mask = r2;
        }

        static /* synthetic */ ImmutableMap access$100(SubSet r1) {
            return r1.inputSet;
        }

        static /* synthetic */ int access$200(SubSet r1) {
            return r1.mask;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r5) {
            Integer r0 = this.inputSet.get(r5);
            if (r0 != null) goto L5;
        L7:
            return false;
        L5:
            if ((this.mask & (1 << r0.intValue())) == 0) goto L7;
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<E> iterator() {
            return new C07311(this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Integer.bitCount(this.mask);
        }
    }

    static final class UnmodifiableNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>, Serializable {
        private static final long serialVersionUID = 0;
        private final NavigableSet<E> delegate;

        @CheckForNull
        @LazyInit
        private transient UnmodifiableNavigableSet<E> descendingSet;
        private final SortedSet<E> unmodifiableDelegate;

        UnmodifiableNavigableSet(NavigableSet<E> r2) {
            this.delegate = (NavigableSet) Preconditions.checkNotNull(r2);
            this.unmodifiableDelegate = Collections.unmodifiableSortedSet(r2);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public E ceiling(@ParametricNullness E r2) {
            return this.delegate.ceiling(r2);
        }

        @Override // com.google.common.collect.ForwardingSortedSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingSortedSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Collection delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingSortedSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Set delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingSortedSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected SortedSet<E> delegate() {
            return this.unmodifiableDelegate;
        }

        @Override // java.util.NavigableSet
        public Iterator<E> descendingIterator() {
            return Iterators.unmodifiableIterator(this.delegate.descendingIterator());
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> descendingSet() {
            UnmodifiableNavigableSet<E> r0 = this.descendingSet;
            if (r0 != null) goto L6;
            UnmodifiableNavigableSet<E> r1 = new UnmodifiableNavigableSet(this.delegate.descendingSet());
            this.descendingSet = r1;
            r1.descendingSet = this;
            return r1;
        L6:
            return r0;
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public E floor(@ParametricNullness E r2) {
            return this.delegate.floor(r2);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> headSet(@ParametricNullness E r2, boolean r3) {
            return Sets.unmodifiableNavigableSet(this.delegate.headSet(r2, r3));
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public E higher(@ParametricNullness E r2) {
            return this.delegate.higher(r2);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public E lower(@ParametricNullness E r2) {
            return this.delegate.lower(r2);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public E pollFirst() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public E pollLast() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> subSet(@ParametricNullness E r2, boolean r3, @ParametricNullness E r4, boolean r5) {
            return Sets.unmodifiableNavigableSet(this.delegate.subSet(r2, r3, r4, r5));
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> tailSet(@ParametricNullness E r2, boolean r3) {
            return Sets.unmodifiableNavigableSet(this.delegate.tailSet(r2, r3));
        }
    }

    private Sets() {
    }

    public static <B> Set<List<B>> cartesianProduct(List<? extends Set<? extends B>> r1) {
        return CartesianSet.create(r1);
    }

    @SafeVarargs
    public static <B> Set<List<B>> cartesianProduct(Set<? extends B>... r1) {
        return cartesianProduct(Arrays.asList(r1));
    }

    public static <E> Set<Set<E>> combinations(Set<E> r4, int r5) {
        ImmutableMap r0 = Maps.indexMap(r4);
        CollectPreconditions.checkNonnegative(r5, MediaInformation.KEY_SIZE);
        if (r5 > r0.size()) goto L5;
        boolean r1 = true;
    L6:
        Preconditions.checkArgument(r1, "size (%s) must be <= set.size() (%s)", r5, r0.size());
        if (r5 != 0) goto L11;
        return ImmutableSet.m260of(ImmutableSet.m259of());
    L11:
        if (r5 != r0.size()) goto L15;
        return ImmutableSet.m260of(r0.keySet());
    L15:
        return new C07285(r5, r0);
    L5:
        r1 = false;
        goto L6
    }

    public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> r2) {
        if ((r2 instanceof EnumSet) == true) goto L5;
        Preconditions.checkArgument(!r2.isEmpty(), "collection is empty; use the other version of this method");
        return makeComplementByHand(r2, r2.iterator().next().getDeclaringClass());
    L5:
        return EnumSet.complementOf((EnumSet) r2);
    }

    public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> r1, Class<E> r2) {
        Preconditions.checkNotNull(r1);
        if ((r1 instanceof EnumSet) == false) goto L6;
        return EnumSet.complementOf((EnumSet) r1);
    L6:
        return makeComplementByHand(r1, r2);
    }

    public static <E> SetView<E> difference(final Set<E> r1, final Set<?> r2) {
        Preconditions.checkNotNull(r1, "set1");
        Preconditions.checkNotNull(r2, "set2");
        return new C07263(r1, r2);
    }

    static boolean equalsImpl(Set<?> r5, @CheckForNull Object r6) {
        if (r5 != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof Set) == false) goto L19;
        Set r1 = (Set) r6;
        if (r5.size() != r1.size()) goto L14;
        if (r5.containsAll(r1) == false) goto L14;
        return true;
    L14:
        return false;
    L18:
        return false;
    L19:
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <E> NavigableSet<E> filter(NavigableSet<E> r4, Predicate<? super E> r5) {
        if ((r4 instanceof FilteredSet) == false) goto L7;
        FilteredSet r0 = (FilteredSet) r4;
        Predicate r1 = Predicates.and(r0.predicate, r5);
        return new FilteredNavigableSet((NavigableSet) r0.unfiltered, r1);
    L7:
        return new FilteredNavigableSet((NavigableSet) Preconditions.checkNotNull(r4), (Predicate) Preconditions.checkNotNull(r5));
    }

    public static <E> Set<E> filter(Set<E> r4, Predicate<? super E> r5) {
        if ((r4 instanceof SortedSet) == false) goto L7;
        return filter((SortedSet) r4, r5);
    L7:
        if ((r4 instanceof FilteredSet) == false) goto L11;
        FilteredSet r0 = (FilteredSet) r4;
        Predicate r1 = Predicates.and(r0.predicate, r5);
        return new FilteredSet((Set) r0.unfiltered, r1);
    L11:
        return new FilteredSet((Set) Preconditions.checkNotNull(r4), (Predicate) Preconditions.checkNotNull(r5));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <E> SortedSet<E> filter(SortedSet<E> r4, Predicate<? super E> r5) {
        if ((r4 instanceof FilteredSet) == false) goto L7;
        FilteredSet r0 = (FilteredSet) r4;
        Predicate r1 = Predicates.and(r0.predicate, r5);
        return new FilteredSortedSet((SortedSet) r0.unfiltered, r1);
    L7:
        return new FilteredSortedSet((SortedSet) Preconditions.checkNotNull(r4), (Predicate) Preconditions.checkNotNull(r5));
    }

    static int hashCodeImpl(Set<?> r4) {
        int r0 = 0;
        Iterator<?> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L10;
        Object r2 = r1.next();
        if (r2 == null) goto L8;
        int r3 = r2.hashCode();
    L9:
        r0 = ~(~(r0 + r3));
        goto L4
    L8:
        r3 = 0;
        goto L9
    L10:
        return r0;
    }

    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(E r1, E... r2) {
        return ImmutableEnumSet.asImmutable(EnumSet.of(r1, r2));
    }

    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> r3) {
        if ((r3 instanceof ImmutableEnumSet) == false) goto L7;
        return (ImmutableEnumSet) r3;
    L7:
        if ((r3 instanceof Collection) == false) goto L14;
        Collection r0 = (Collection) r3;
        if (r0.isEmpty() == false) goto L13;
        return ImmutableSet.m259of();
    L13:
        return ImmutableEnumSet.asImmutable(EnumSet.copyOf(r0));
    L14:
        Iterator<E> r1 = r3.iterator();
        if (r1.hasNext() == false) goto L19;
        EnumSet r2 = EnumSet.of(r1.next());
        Iterators.addAll(r2, r1);
        return ImmutableEnumSet.asImmutable(r2);
    L19:
        return ImmutableSet.m259of();
    }

    public static <E> SetView<E> intersection(final Set<E> r1, final Set<?> r2) {
        Preconditions.checkNotNull(r1, "set1");
        Preconditions.checkNotNull(r2, "set2");
        return new C07252(r1, r2);
    }

    private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(Collection<E> r1, Class<E> r2) {
        EnumSet<E> r0 = EnumSet.allOf(r2);
        r0.removeAll(r1);
        return r0;
    }

    public static <E> Set<E> newConcurrentHashSet() {
        return Collections.newSetFromMap(new ConcurrentHashMap());
    }

    public static <E> Set<E> newConcurrentHashSet(Iterable<? extends E> r1) {
        Set<E> r0 = newConcurrentHashSet();
        Iterables.addAll(r0, r1);
        return r0;
    }

    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
        return new CopyOnWriteArraySet();
    }

    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<? extends E> r2) {
        if ((r2 instanceof Collection) == false) goto L5;
        Collection r0 = (Collection) r2;
    L7:
        return new CopyOnWriteArraySet(r0);
    L5:
        r0 = Lists.newArrayList(r2);
        goto L7
    }

    public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> r1, Class<E> r2) {
        EnumSet<E> r0 = EnumSet.noneOf(r2);
        Iterables.addAll(r0, r1);
        return r0;
    }

    public static <E> HashSet<E> newHashSet() {
        return new HashSet();
    }

    public static <E> HashSet<E> newHashSet(Iterable<? extends E> r2) {
        if ((r2 instanceof Collection) == false) goto L6;
        return new HashSet((Collection) r2);
    L6:
        return newHashSet(r2.iterator());
    }

    public static <E> HashSet<E> newHashSet(Iterator<? extends E> r1) {
        HashSet<E> r0 = newHashSet();
        Iterators.addAll(r0, r1);
        return r0;
    }

    public static <E> HashSet<E> newHashSet(E... r1) {
        HashSet<E> r0 = newHashSetWithExpectedSize(r1.length);
        Collections.addAll(r0, r1);
        return r0;
    }

    public static <E> HashSet<E> newHashSetWithExpectedSize(int r2) {
        return new HashSet(Maps.capacity(r2));
    }

    public static <E> Set<E> newIdentityHashSet() {
        return Collections.newSetFromMap(Maps.newIdentityHashMap());
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet();
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> r2) {
        if ((r2 instanceof Collection) == true) goto L5;
        LinkedHashSet<E> r0 = newLinkedHashSet();
        Iterables.addAll(r0, r2);
        return r0;
    L5:
        return new LinkedHashSet((Collection) r2);
    }

    public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int r2) {
        return new LinkedHashSet(Maps.capacity(r2));
    }

    @Deprecated
    public static <E> Set<E> newSetFromMap(Map<E, Boolean> r1) {
        return Collections.newSetFromMap(r1);
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet() {
        return new TreeSet();
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> r1) {
        TreeSet<E> r0 = newTreeSet();
        Iterables.addAll(r0, r1);
        return r0;
    }

    public static <E> TreeSet<E> newTreeSet(Comparator<? super E> r2) {
        return new TreeSet((Comparator) Preconditions.checkNotNull(r2));
    }

    public static <E> Set<Set<E>> powerSet(Set<E> r1) {
        return new PowerSet(r1);
    }

    static boolean removeAllImpl(Set<?> r2, Collection<?> r3) {
        Preconditions.checkNotNull(r3);
        if ((r3 instanceof Multiset) == false) goto L6;
        r3 = ((Multiset) r3).elementSet();
    L6:
        if ((r3 instanceof Set) == false) goto L12;
        if (r3.size() <= r2.size()) goto L12;
        return Iterators.removeAll(r2.iterator(), r3);
    L12:
        return removeAllImpl(r2, r3.iterator());
    }

    static boolean removeAllImpl(Set<?> r2, Iterator<?> r3) {
        boolean r0 = false;
    L4:
        if (r3.hasNext() == false) goto L6;
        r0 = r0 | r2.remove(r3.next());
        goto L4
    L6:
        return r0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <K extends Comparable<? super K>> NavigableSet<K> subSet(NavigableSet<K> r7, Range<K> r8) {
        boolean r1 = true;
        if (r7.comparator() == null) goto L16;
        if (r7.comparator() == Ordering.natural()) goto L16;
        if (r8.hasLowerBound() == false) goto L16;
        if (r8.hasUpperBound() == false) goto L16;
        if (r7.comparator().compare(r8.lowerEndpoint(), r8.upperEndpoint()) > 0) goto L13;
        boolean r0 = true;
    L14:
        Preconditions.checkArgument(r0, "set is using a custom comparator which is inconsistent with the natural ordering.");
        goto L16
    L13:
        r0 = false;
    L16:
        if (r8.hasLowerBound() == false) goto L30;
        if (r8.hasUpperBound() == false) goto L30;
        Comparable r2 = r8.lowerEndpoint();
        if (r8.lowerBoundType() != BoundType.CLOSED) goto L22;
        boolean r3 = true;
    L23:
        Comparable r4 = r8.upperEndpoint();
        if (r8.upperBoundType() == BoundType.CLOSED) goto L28;
        r1 = false;
    L28:
        return r7.subSet(r2, r3, r4, r1);
    L22:
        r3 = false;
    L30:
        if (r8.hasLowerBound() == false) goto L38;
        Comparable r5 = r8.lowerEndpoint();
        if (r8.lowerBoundType() == BoundType.CLOSED) goto L36;
        r1 = false;
    L36:
        return r7.tailSet(r5, r1);
    L38:
        if (r8.hasUpperBound() == false) goto L46;
        Comparable r6 = r8.upperEndpoint();
        if (r8.upperBoundType() == BoundType.CLOSED) goto L44;
        r1 = false;
    L44:
        return r7.headSet(r6, r1);
    L46:
        return (NavigableSet) Preconditions.checkNotNull(r7);
    }

    public static <E> SetView<E> symmetricDifference(final Set<? extends E> r1, final Set<? extends E> r2) {
        Preconditions.checkNotNull(r1, "set1");
        Preconditions.checkNotNull(r2, "set2");
        return new C07274(r1, r2);
    }

    public static <E> NavigableSet<E> synchronizedNavigableSet(NavigableSet<E> r1) {
        return Synchronized.navigableSet(r1);
    }

    public static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
        return CollectCollectors.toImmutableEnumSet();
    }

    public static <E> SetView<E> union(final Set<? extends E> r1, final Set<? extends E> r2) {
        Preconditions.checkNotNull(r1, "set1");
        Preconditions.checkNotNull(r2, "set2");
        return new C07241(r1, r2);
    }

    public static <E> NavigableSet<E> unmodifiableNavigableSet(NavigableSet<E> r1) {
        if ((r1 instanceof ImmutableCollection) == false) goto L5;
    L9:
        return r1;
    L5:
        if ((r1 instanceof UnmodifiableNavigableSet) == true) goto L9;
        return new UnmodifiableNavigableSet(r1);
    }
}
