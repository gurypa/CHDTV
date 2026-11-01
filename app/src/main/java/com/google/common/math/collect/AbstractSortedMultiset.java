package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractSortedMultiset<E> extends AbstractMultiset<E> implements SortedMultiset<E> {

    @GwtTransient
    final Comparator<? super E> comparator;

    @CheckForNull
    @LazyInit
    private transient SortedMultiset<E> descendingMultiset;

    AbstractSortedMultiset() {
        this(Ordering.natural());
    }

    AbstractSortedMultiset(Comparator<? super E> r2) {
        this.comparator = (Comparator) Preconditions.checkNotNull(r2);
    }

    @Override // com.google.common.collect.SortedMultiset, com.google.common.collect.SortedIterable
    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    SortedMultiset<E> createDescendingMultiset() {
        return new C1DescendingMultisetImpl(this);
    }

    @Override // com.google.common.collect.AbstractMultiset
    NavigableSet<E> createElementSet() {
        return new SortedMultisets.NavigableElementSet(this);
    }

    @Override // com.google.common.collect.AbstractMultiset
    /* bridge */ /* synthetic */ Set createElementSet() {
        return createElementSet();
    }

    abstract Iterator<Multiset.Entry<E>> descendingEntryIterator();

    Iterator<E> descendingIterator() {
        return Multisets.iteratorImpl(descendingMultiset());
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> descendingMultiset() {
        SortedMultiset<E> r0 = this.descendingMultiset;
        if (r0 != null) goto L6;
        SortedMultiset<E> r1 = createDescendingMultiset();
        this.descendingMultiset = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public NavigableSet<E> elementSet() {
        return (NavigableSet) super.elementSet();
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ SortedSet elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    public Multiset.Entry<E> firstEntry() {
        Iterator<Multiset.Entry<E>> r0 = entryIterator();
        if (r0.hasNext() == true) goto L7;
        return null;
    L7:
        return r0.next();
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    public Multiset.Entry<E> lastEntry() {
        Iterator<Multiset.Entry<E>> r0 = descendingEntryIterator();
        if (r0.hasNext() == true) goto L7;
        return null;
    L7:
        return r0.next();
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    public Multiset.Entry<E> pollFirstEntry() {
        Iterator<Multiset.Entry<E>> r0 = entryIterator();
        if (r0.hasNext() == false) goto L6;
        Multiset.Entry<E> r1 = r0.next();
        Multiset.Entry<E> r2 = Multisets.immutableEntry(r1.getElement(), r1.getCount());
        r0.remove();
        return r2;
    L6:
        return null;
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    public Multiset.Entry<E> pollLastEntry() {
        Iterator<Multiset.Entry<E>> r0 = descendingEntryIterator();
        if (r0.hasNext() == false) goto L6;
        Multiset.Entry<E> r1 = r0.next();
        Multiset.Entry<E> r2 = Multisets.immutableEntry(r1.getElement(), r1.getCount());
        r0.remove();
        return r2;
    L6:
        return null;
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> subMultiset(@ParametricNullness E r2, BoundType r3, @ParametricNullness E r4, BoundType r5) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r5);
        return tailMultiset(r2, r3).headMultiset(r4, r5);
    }
}
