package com.google.common.collect;

import java.util.Iterator;
import java.util.ListIterator;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingListIterator<E> extends ForwardingIterator<E> implements ListIterator<E> {
    protected ForwardingListIterator() {
    }

    @Override // java.util.ListIterator
    public void add(@ParametricNullness E r2) {
        delegate().add(r2);
    }

    @Override // com.google.common.collect.ForwardingIterator, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingIterator, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Iterator delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingIterator, com.google.common.collect.ForwardingObject
    protected abstract ListIterator<E> delegate();

    @Override // java.util.ListIterator
    public boolean hasPrevious() {
        return delegate().hasPrevious();
    }

    @Override // java.util.ListIterator
    public int nextIndex() {
        return delegate().nextIndex();
    }

    @Override // java.util.ListIterator
    @ParametricNullness
    public E previous() {
        return delegate().previous();
    }

    @Override // java.util.ListIterator
    public int previousIndex() {
        return delegate().previousIndex();
    }

    @Override // java.util.ListIterator
    public void set(@ParametricNullness E r2) {
        delegate().set(r2);
    }
}
