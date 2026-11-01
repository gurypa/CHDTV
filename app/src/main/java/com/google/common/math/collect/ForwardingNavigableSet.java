package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E> {

    protected class StandardDescendingSet extends Sets.DescendingSet<E> {
        final /* synthetic */ ForwardingNavigableSet this$0;

        public StandardDescendingSet(ForwardingNavigableSet r1) {
            this.this$0 = r1;
            super(r1);
        }
    }

    protected ForwardingNavigableSet() {
    }

    @Override // java.util.NavigableSet
    @CheckForNull
    public E ceiling(@ParametricNullness E r2) {
        return delegate().ceiling(r2);
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
    protected abstract NavigableSet<E> delegate();

    @Override // com.google.common.collect.ForwardingSortedSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Set delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingSortedSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ SortedSet delegate() {
        return delegate();
    }

    @Override // java.util.NavigableSet
    public Iterator<E> descendingIterator() {
        return delegate().descendingIterator();
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> descendingSet() {
        return delegate().descendingSet();
    }

    @Override // java.util.NavigableSet
    @CheckForNull
    public E floor(@ParametricNullness E r2) {
        return delegate().floor(r2);
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> headSet(@ParametricNullness E r2, boolean r3) {
        return delegate().headSet(r2, r3);
    }

    @Override // java.util.NavigableSet
    @CheckForNull
    public E higher(@ParametricNullness E r2) {
        return delegate().higher(r2);
    }

    @Override // java.util.NavigableSet
    @CheckForNull
    public E lower(@ParametricNullness E r2) {
        return delegate().lower(r2);
    }

    @Override // java.util.NavigableSet
    @CheckForNull
    public E pollFirst() {
        return delegate().pollFirst();
    }

    @Override // java.util.NavigableSet
    @CheckForNull
    public E pollLast() {
        return delegate().pollLast();
    }

    @CheckForNull
    protected E standardCeiling(@ParametricNullness E r3) {
        return (E) Iterators.getNext(tailSet(r3, true).iterator(), null);
    }

    @ParametricNullness
    protected E standardFirst() {
        return iterator().next();
    }

    @CheckForNull
    protected E standardFloor(@ParametricNullness E r3) {
        return (E) Iterators.getNext(headSet(r3, true).descendingIterator(), null);
    }

    protected SortedSet<E> standardHeadSet(@ParametricNullness E r2) {
        return headSet(r2, false);
    }

    @CheckForNull
    protected E standardHigher(@ParametricNullness E r3) {
        return (E) Iterators.getNext(tailSet(r3, false).iterator(), null);
    }

    @ParametricNullness
    protected E standardLast() {
        return descendingIterator().next();
    }

    @CheckForNull
    protected E standardLower(@ParametricNullness E r3) {
        return (E) Iterators.getNext(headSet(r3, false).descendingIterator(), null);
    }

    @CheckForNull
    protected E standardPollFirst() {
        return (E) Iterators.pollNext(iterator());
    }

    @CheckForNull
    protected E standardPollLast() {
        return (E) Iterators.pollNext(descendingIterator());
    }

    protected NavigableSet<E> standardSubSet(@ParametricNullness E r2, boolean r3, @ParametricNullness E r4, boolean r5) {
        return tailSet(r2, r3).headSet(r4, r5);
    }

    @Override // com.google.common.collect.ForwardingSortedSet
    protected SortedSet<E> standardSubSet(@ParametricNullness E r3, @ParametricNullness E r4) {
        return subSet(r3, true, r4, false);
    }

    protected SortedSet<E> standardTailSet(@ParametricNullness E r2) {
        return tailSet(r2, true);
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> subSet(@ParametricNullness E r2, boolean r3, @ParametricNullness E r4, boolean r5) {
        return delegate().subSet(r2, r3, r4, r5);
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> tailSet(@ParametricNullness E r2, boolean r3) {
        return delegate().tailSet(r2, r3);
    }
}
