package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Multisets {

    static abstract class AbstractEntry<E> implements Multiset.Entry<E> {
        AbstractEntry() {
        }

        @Override // com.google.common.collect.Multiset.Entry
        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof Multiset.Entry) == false) goto L11;
            Multiset.Entry r0 = (Multiset.Entry) r5;
            if (getCount() == r0.getCount()) goto L7;
            return false;
        L7:
            if (Objects.equal(getElement(), r0.getElement()) == false) goto L13;
            return true;
        L13:
            return false;
        L11:
            return false;
        }

        @Override // com.google.common.collect.Multiset.Entry
        public int hashCode() {
            E r0 = getElement();
            if (r0 != null) goto L5;
            int r1 = 0;
        L7:
            return r1 ^ getCount();
        L5:
            r1 = r0.hashCode();
            goto L7
        }

        @Override // com.google.common.collect.Multiset.Entry
        public String toString() {
            String r0 = String.valueOf(getElement());
            int r1 = getCount();
            if (r1 != 1) goto L6;
            return r0;
        L6:
            return r0 + " x " + r1;
        }
    }

    private static final class DecreasingCount implements Comparator<Multiset.Entry<?>> {
        static final Comparator<Multiset.Entry<?>> INSTANCE = null;

        static {
            INSTANCE = new DecreasingCount();
        }

        private DecreasingCount() {
        }

        /* JADX INFO: renamed from: compare, reason: avoid collision after fix types in other method */
        public int compare2(Multiset.Entry<?> r3, Multiset.Entry<?> r4) {
            return r4.getCount() - r3.getCount();
        }

        @Override // java.util.Comparator
        public /* bridge */ /* synthetic */ int compare(Multiset.Entry<?> r1, Multiset.Entry<?> r2) {
            return compare2(r1, r2);
        }
    }

    static abstract class ElementSet<E> extends Sets.ImprovedAbstractSet<E> {
        ElementSet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            multiset().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r2) {
            return multiset().contains(r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> r2) {
            return multiset().containsAll(r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return multiset().isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public abstract Iterator<E> iterator();

        abstract Multiset<E> multiset();

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r3) {
            if (multiset().remove(r3, Integer.MAX_VALUE) <= 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return multiset().entrySet().size();
        }
    }

    static abstract class EntrySet<E> extends Sets.ImprovedAbstractSet<Multiset.Entry<E>> {
        EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            multiset().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r5) {
            if ((r5 instanceof Multiset.Entry) == false) goto L11;
            Multiset.Entry r0 = (Multiset.Entry) r5;
            if (r0.getCount() > 0) goto L8;
            return false;
        L8:
            if (multiset().count(r0.getElement()) != r0.getCount()) goto L12;
            return true;
        L12:
            return false;
        L11:
            return false;
        }

        abstract Multiset<E> multiset();

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r6) {
            if ((r6 instanceof Multiset.Entry) == false) goto L8;
            Multiset.Entry r0 = (Multiset.Entry) r6;
            Object r2 = r0.getElement();
            int r3 = r0.getCount();
            if (r3 == 0) goto L8;
            return multiset().setCount(r2, r3, 0);
        L8:
            return false;
        }
    }

    private static final class FilteredMultiset<E> extends ViewMultiset<E> {
        final Predicate<? super E> predicate;
        final Multiset<E> unfiltered;

        FilteredMultiset(Multiset<E> r2, Predicate<? super E> r3) {
            super(null);
            this.unfiltered = (Multiset) Preconditions.checkNotNull(r2);
            this.predicate = (Predicate) Preconditions.checkNotNull(r3);
        }

        @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
        public int add(@ParametricNullness E r4, int r5) {
            Preconditions.checkArgument(this.predicate.apply(r4), "Element %s does not match predicate %s", r4, this.predicate);
            return this.unfiltered.add(r4, r5);
        }

        @Override // com.google.common.collect.Multiset
        public int count(@CheckForNull Object r5) {
            int r0 = this.unfiltered.count(r5);
            if (r0 > 0) goto L5;
            return 0;
        L5:
            if (this.predicate.apply(r5) == true) goto L7;
            return 0;
        L7:
            return r0;
        }

        @Override // com.google.common.collect.AbstractMultiset
        Set<E> createElementSet() {
            return Sets.filter(this.unfiltered.elementSet(), this.predicate);
        }

        @Override // com.google.common.collect.AbstractMultiset
        Set<Multiset.Entry<E>> createEntrySet() {
            return Sets.filter(this.unfiltered.entrySet(), new C07131(this));
        }

        @Override // com.google.common.collect.AbstractMultiset
        Iterator<E> elementIterator() {
            throw new AssertionError("should never be called");
        }

        @Override // com.google.common.collect.AbstractMultiset
        Iterator<Multiset.Entry<E>> entryIterator() {
            throw new AssertionError("should never be called");
        }

        @Override // com.google.common.collect.Multisets.ViewMultiset, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
        public UnmodifiableIterator<E> iterator() {
            return Iterators.filter(this.unfiltered.iterator(), this.predicate);
        }

        @Override // com.google.common.collect.Multisets.ViewMultiset, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return iterator();
        }

        @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
        public int remove(@CheckForNull Object r2, int r3) {
            CollectPreconditions.checkNonnegative(r3, "occurrences");
            if (r3 != 0) goto L7;
            return count(r2);
        L7:
            if (contains(r2) == true) goto L11;
            return 0;
        L11:
            return this.unfiltered.remove(r2, r3);
        }
    }

    static class ImmutableEntry<E> extends AbstractEntry<E> implements Serializable {
        private static final long serialVersionUID = 0;
        private final int count;

        @ParametricNullness
        private final E element;

        ImmutableEntry(@ParametricNullness E r2, int r3) {
            this.element = r2;
            this.count = r3;
            CollectPreconditions.checkNonnegative(r3, "count");
        }

        @Override // com.google.common.collect.Multiset.Entry
        public final int getCount() {
            return this.count;
        }

        @Override // com.google.common.collect.Multiset.Entry
        @ParametricNullness
        public final E getElement() {
            return this.element;
        }

        @CheckForNull
        public ImmutableEntry<E> nextInBucket() {
            return null;
        }
    }

    static final class MultisetIteratorImpl<E> implements Iterator<E> {
        private boolean canRemove;

        @CheckForNull
        private Multiset.Entry<E> currentEntry;
        private final Iterator<Multiset.Entry<E>> entryIterator;
        private int laterCount;
        private final Multiset<E> multiset;
        private int totalCount;

        MultisetIteratorImpl(Multiset<E> r1, Iterator<Multiset.Entry<E>> r2) {
            this.multiset = r1;
            this.entryIterator = r2;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.laterCount <= 0) goto L5;
        L8:
            return true;
        L5:
            if (this.entryIterator.hasNext() == true) goto L8;
            return false;
        }

        @Override // java.util.Iterator
        @ParametricNullness
        public E next() {
            if (hasNext() == false) goto L10;
            if (this.laterCount != 0) goto L7;
            this.currentEntry = this.entryIterator.next();
            int r0 = this.currentEntry.getCount();
            this.laterCount = r0;
            this.totalCount = r0;
        L7:
            this.laterCount--;
            this.canRemove = true;
            return (E) ((Multiset.Entry) java.util.Objects.requireNonNull(this.currentEntry)).getElement();
        L10:
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            CollectPreconditions.checkRemove(this.canRemove);
            if (this.totalCount != 1) goto L5;
            this.entryIterator.remove();
        L6:
            this.totalCount--;
            this.canRemove = false;
            return;
        L5:
            this.multiset.remove(((Multiset.Entry) java.util.Objects.requireNonNull(this.currentEntry)).getElement());
            goto L6
        }
    }

    static class UnmodifiableMultiset<E> extends ForwardingMultiset<E> implements Serializable {
        private static final long serialVersionUID = 0;
        final Multiset<? extends E> delegate;

        @CheckForNull
        @LazyInit
        transient Set<E> elementSet;

        @CheckForNull
        @LazyInit
        transient Set<Multiset.Entry<E>> entrySet;

        UnmodifiableMultiset(Multiset<? extends E> r1) {
            this.delegate = r1;
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
        public int add(@ParametricNullness E r2, int r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Queue
        public boolean add(@ParametricNullness E r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean addAll(Collection<? extends E> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public void clear() {
            throw new UnsupportedOperationException();
        }

        Set<E> createElementSet() {
            return Collections.unmodifiableSet(this.delegate.elementSet());
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected Multiset<E> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Collection delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
        public Set<E> elementSet() {
            Set<E> r0 = this.elementSet;
            if (r0 != null) goto L6;
            Set<E> r1 = createElementSet();
            this.elementSet = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
        public Set<Multiset.Entry<E>> entrySet() {
            Set<Multiset.Entry<E>> r0 = this.entrySet;
            if (r0 != null) goto L6;
            Set<Multiset.Entry<E>> r1 = Collections.unmodifiableSet(this.delegate.entrySet());
            this.entrySet = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<E> iterator() {
            return Iterators.unmodifiableIterator(this.delegate.iterator());
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
        public int remove(@CheckForNull Object r2, int r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
        public int setCount(@ParametricNullness E r2, int r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
        public boolean setCount(@ParametricNullness E r2, int r3, int r4) {
            throw new UnsupportedOperationException();
        }
    }

    private static abstract class ViewMultiset<E> extends AbstractMultiset<E> {
        private ViewMultiset() {
        }

        /* synthetic */ ViewMultiset(C07081 r1) {
            this();
        }

        @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
        public void clear() {
            elementSet().clear();
        }

        @Override // com.google.common.collect.AbstractMultiset
        int distinctElements() {
            return elementSet().size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
        public Iterator<E> iterator() {
            return Multisets.iteratorImpl(this);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
        public int size() {
            return Multisets.linearTimeSizeImpl(this);
        }
    }

    private Multisets() {
    }

    private static <E> boolean addAllImpl(Multiset<E> r1, AbstractMapBasedMultiset<? extends E> r2) {
        if (r2.isEmpty() == false) goto L6;
        return false;
    L6:
        r2.addTo(r1);
        return true;
    }

    private static <E> boolean addAllImpl(Multiset<E> r4, Multiset<? extends E> r5) {
        if ((r5 instanceof AbstractMapBasedMultiset) == false) goto L7;
        return addAllImpl(r4, (AbstractMapBasedMultiset) r5);
    L7:
        if (r5.isEmpty() == false) goto L10;
        return false;
    L10:
        Iterator<Multiset.Entry<? extends E>> r0 = r5.entrySet().iterator();
    L12:
        if (r0.hasNext() == false) goto L14;
        Multiset.Entry<? extends E> r1 = r0.next();
        r4.add(r1.getElement(), r1.getCount());
        goto L12
    L14:
        return true;
    }

    static <E> boolean addAllImpl(Multiset<E> r1, Collection<? extends E> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        if ((r2 instanceof Multiset) == false) goto L7;
        return addAllImpl(r1, cast(r2));
    L7:
        if (r2.isEmpty() == false) goto L11;
        return false;
    L11:
        return Iterators.addAll(r1, r2.iterator());
    }

    static <T> Multiset<T> cast(Iterable<T> r1) {
        return (Multiset) r1;
    }

    public static boolean containsOccurrences(Multiset<?> r4, Multiset<?> r5) {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        Iterator<Multiset.Entry<?>> r0 = r5.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        Multiset.Entry<?> r1 = r0.next();
        if (r4.count(r1.getElement()) >= r1.getCount()) goto L4;
        return false;
    L10:
        return true;
    }

    public static <E> ImmutableMultiset<E> copyHighestCountFirst(Multiset<E> r2) {
        Multiset.Entry[] r0 = (Multiset.Entry[]) r2.entrySet().toArray(new Multiset.Entry[0]);
        Arrays.sort(r0, DecreasingCount.INSTANCE);
        return ImmutableMultiset.copyFromEntries(Arrays.asList(r0));
    }

    public static <E> Multiset<E> difference(final Multiset<E> r1, final Multiset<?> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new C07114(r1, r2);
    }

    static <E> Iterator<E> elementIterator(final Iterator<Multiset.Entry<E>> r1) {
        return new C07125(r1);
    }

    static boolean equalsImpl(Multiset<?> r7, @CheckForNull Object r8) {
        if (r8 != r7) goto L6;
        return true;
    L6:
        if ((r8 instanceof Multiset) == false) goto L21;
        Multiset r1 = (Multiset) r8;
        if (r7.size() == r1.size()) goto L10;
    L20:
        return false;
    L10:
        if (r7.entrySet().size() != r1.entrySet().size()) goto L20;
        Iterator r3 = r1.entrySet().iterator();
    L14:
        if (r3.hasNext() == false) goto L19;
        Multiset.Entry r4 = (Multiset.Entry) r3.next();
        if (r7.count(r4.getElement()) == r4.getCount()) goto L14;
        return false;
    L19:
        return true;
    L21:
        return false;
    }

    public static <E> Multiset<E> filter(Multiset<E> r4, Predicate<? super E> r5) {
        if ((r4 instanceof FilteredMultiset) == false) goto L7;
        FilteredMultiset r0 = (FilteredMultiset) r4;
        Predicate r1 = Predicates.and(r0.predicate, r5);
        return new FilteredMultiset(r0.unfiltered, r1);
    L7:
        return new FilteredMultiset(r4, r5);
    }

    public static <E> Multiset.Entry<E> immutableEntry(@ParametricNullness E r1, int r2) {
        return new ImmutableEntry(r1, r2);
    }

    static int inferDistinctElements(Iterable<?> r1) {
        if ((r1 instanceof Multiset) == true) goto L5;
        return 11;
    L5:
        return ((Multiset) r1).elementSet().size();
    }

    public static <E> Multiset<E> intersection(final Multiset<E> r1, final Multiset<?> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new C07092(r1, r2);
    }

    static <E> Iterator<E> iteratorImpl(Multiset<E> r2) {
        return new MultisetIteratorImpl(r2, r2.entrySet().iterator());
    }

    static int linearTimeSizeImpl(Multiset<?> r6) {
        long r0 = 0;
        Iterator<Multiset.Entry<?>> r2 = r6.entrySet().iterator();
    L4:
        if (r2.hasNext() == false) goto L7;
        r0 = r0 + ((long) r2.next().getCount());
        goto L4
    L7:
        return Ints.saturatedCast(r0);
    }

    static boolean removeAllImpl(Multiset<?> r2, Collection<?> r3) {
        if ((r3 instanceof Multiset) == false) goto L5;
        Collection<?> r0 = ((Multiset) r3).elementSet();
    L7:
        return r2.elementSet().removeAll(r0);
    L5:
        r0 = r3;
        goto L7
    }

    public static boolean removeOccurrences(Multiset<?> r5, Multiset<?> r6) {
        Preconditions.checkNotNull(r5);
        Preconditions.checkNotNull(r6);
        boolean r0 = false;
        Iterator<Multiset.Entry<?>> r1 = r5.entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L11;
        Multiset.Entry<?> r2 = r1.next();
        int r3 = r6.count(r2.getElement());
        if (r3 >= r2.getCount()) goto L7;
        if (r3 <= 0) goto L4;
        r5.remove(r2.getElement(), r3);
        r0 = true;
        goto L4
    L7:
        r1.remove();
        r0 = true;
        goto L4
    L11:
        return r0;
    }

    public static boolean removeOccurrences(Multiset<?> r4, Iterable<?> r5) {
        if ((r5 instanceof Multiset) == true) goto L5;
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        boolean r0 = false;
        Iterator<?> r1 = r5.iterator();
    L8:
        if (r1.hasNext() == false) goto L10;
        r0 = r0 | r4.remove(r1.next());
        goto L8
    L10:
        return r0;
    L5:
        return removeOccurrences(r4, (Multiset) r5);
    }

    static boolean retainAllImpl(Multiset<?> r2, Collection<?> r3) {
        Preconditions.checkNotNull(r3);
        if ((r3 instanceof Multiset) == false) goto L5;
        Collection<?> r0 = ((Multiset) r3).elementSet();
    L7:
        return r2.elementSet().retainAll(r0);
    L5:
        r0 = r3;
        goto L7
    }

    public static boolean retainOccurrences(Multiset<?> r1, Multiset<?> r2) {
        return retainOccurrencesImpl(r1, r2);
    }

    private static <E> boolean retainOccurrencesImpl(Multiset<E> r5, Multiset<?> r6) {
        Preconditions.checkNotNull(r5);
        Preconditions.checkNotNull(r6);
        Iterator<Multiset.Entry<E>> r0 = r5.entrySet().iterator();
        boolean r1 = false;
    L4:
        if (r0.hasNext() == false) goto L12;
        Multiset.Entry<E> r2 = r0.next();
        int r3 = r6.count(r2.getElement());
        if (r3 == 0) goto L7;
        if (r3 >= r2.getCount()) goto L4;
        r5.setCount(r2.getElement(), r3);
        r1 = true;
        goto L4
    L7:
        r0.remove();
        r1 = true;
        goto L4
    L12:
        return r1;
    }

    static <E> int setCountImpl(Multiset<E> r3, @ParametricNullness E r4, int r5) {
        CollectPreconditions.checkNonnegative(r5, "count");
        int r0 = r3.count(r4);
        int r1 = r5 - r0;
        if (r1 <= 0) goto L5;
        r3.add(r4, r1);
    L7:
        return r0;
    L5:
        if (r1 >= 0) goto L7;
        r3.remove(r4, -r1);
        goto L7
    }

    static <E> boolean setCountImpl(Multiset<E> r1, @ParametricNullness E r2, int r3, int r4) {
        CollectPreconditions.checkNonnegative(r3, "oldCount");
        CollectPreconditions.checkNonnegative(r4, "newCount");
        if (r1.count(r2) != r3) goto L6;
        r1.setCount(r2, r4);
        return true;
    L6:
        return false;
    }

    public static <E> Multiset<E> sum(final Multiset<? extends E> r1, final Multiset<? extends E> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new C07103(r1, r2);
    }

    public static <T, E, M extends Multiset<E>> Collector<T, ?, M> toMultiset(Function<? super T, E> r1, ToIntFunction<? super T> r2, Supplier<M> r3) {
        return CollectCollectors.toMultiset(r1, r2, r3);
    }

    public static <E> Multiset<E> union(final Multiset<? extends E> r1, final Multiset<? extends E> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new C07081(r1, r2);
    }

    @Deprecated
    public static <E> Multiset<E> unmodifiableMultiset(ImmutableMultiset<E> r1) {
        return (Multiset) Preconditions.checkNotNull(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> r2) {
        if ((r2 instanceof UnmodifiableMultiset) == true) goto L10;
        if ((r2 instanceof ImmutableMultiset) == true) goto L10;
        return new UnmodifiableMultiset((Multiset) Preconditions.checkNotNull(r2));
    L10:
        return r2;
    }

    public static <E> SortedMultiset<E> unmodifiableSortedMultiset(SortedMultiset<E> r2) {
        return new UnmodifiableSortedMultiset((SortedMultiset) Preconditions.checkNotNull(r2));
    }
}
