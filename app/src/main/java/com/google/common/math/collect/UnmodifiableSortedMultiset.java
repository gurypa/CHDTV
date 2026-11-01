package com.google.common.collect;

import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class UnmodifiableSortedMultiset<E> extends Multisets.UnmodifiableMultiset<E> implements SortedMultiset<E> {
    private static final long serialVersionUID = 0;

    @CheckForNull
    @LazyInit
    private transient UnmodifiableSortedMultiset<E> descendingMultiset;

    UnmodifiableSortedMultiset(SortedMultiset<E> r1) {
        super(r1);
    }

    @Override // com.google.common.collect.SortedMultiset, com.google.common.collect.SortedIterable
    public Comparator<? super E> comparator() {
        return delegate().comparator();
    }

    @Override // com.google.common.collect.Multisets.UnmodifiableMultiset
    NavigableSet<E> createElementSet() {
        return Sets.unmodifiableNavigableSet(delegate().elementSet());
    }

    @Override // com.google.common.collect.Multisets.UnmodifiableMultiset
    /* bridge */ /* synthetic */ Set createElementSet() {
        return createElementSet();
    }

    @Override // com.google.common.collect.Multisets.UnmodifiableMultiset, com.google.common.collect.ForwardingMultiset, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Multiset delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.Multisets.UnmodifiableMultiset, com.google.common.collect.ForwardingMultiset, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected SortedMultiset<E> delegate() {
        return (SortedMultiset) super.delegate();
    }

    @Override // com.google.common.collect.Multisets.UnmodifiableMultiset, com.google.common.collect.ForwardingMultiset, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.Multisets.UnmodifiableMultiset, com.google.common.collect.ForwardingMultiset, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Collection delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> descendingMultiset() {
        UnmodifiableSortedMultiset<E> r0 = this.descendingMultiset;
        if (r0 != null) goto L6;
        UnmodifiableSortedMultiset<E> r1 = new UnmodifiableSortedMultiset(delegate().descendingMultiset());
        r1.descendingMultiset = this;
        this.descendingMultiset = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.Multisets.UnmodifiableMultiset, com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
    public NavigableSet<E> elementSet() {
        return (NavigableSet) super.elementSet();
    }

    @Override // com.google.common.collect.Multisets.UnmodifiableMultiset, com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.Multisets.UnmodifiableMultiset, com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ SortedSet elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    public Multiset.Entry<E> firstEntry() {
        return delegate().firstEntry();
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> headMultiset(@ParametricNullness E r2, BoundType r3) {
        return Multisets.unmodifiableSortedMultiset(delegate().headMultiset(r2, r3));
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    public Multiset.Entry<E> lastEntry() {
        return delegate().lastEntry();
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    public Multiset.Entry<E> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    public Multiset.Entry<E> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> subMultiset(@ParametricNullness E r2, BoundType r3, @ParametricNullness E r4, BoundType r5) {
        return Multisets.unmodifiableSortedMultiset(delegate().subMultiset(r2, r3, r4, r5));
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> tailMultiset(@ParametricNullness E r2, BoundType r3) {
        return Multisets.unmodifiableSortedMultiset(delegate().tailMultiset(r2, r3));
    }
}
