package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingList<E> extends ForwardingCollection<E> implements List<E> {
    protected ForwardingList() {
    }

    public void add(int r2, @ParametricNullness E r3) {
        delegate().add(r2, r3);
    }

    public boolean addAll(int r2, Collection<? extends E> r3) {
        return delegate().addAll(r2, r3);
    }

    @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Collection delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected abstract List<E> delegate();

    @Override // java.util.Collection, java.util.List
    public boolean equals(@CheckForNull Object r2) {
        if (r2 != this) goto L4;
    L7:
        return true;
    L4:
        if (delegate().equals(r2) == true) goto L7;
        return false;
    }

    @Override // java.util.List
    @ParametricNullness
    public E get(int r2) {
        return delegate().get(r2);
    }

    @Override // java.util.Collection, java.util.List
    public int hashCode() {
        return delegate().hashCode();
    }

    @Override // java.util.List
    public int indexOf(@CheckForNull Object r2) {
        return delegate().indexOf(r2);
    }

    @Override // java.util.List
    public int lastIndexOf(@CheckForNull Object r2) {
        return delegate().lastIndexOf(r2);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator() {
        return delegate().listIterator();
    }

    @Override // java.util.List
    public ListIterator<E> listIterator(int r2) {
        return delegate().listIterator(r2);
    }

    @Override // java.util.List
    @ParametricNullness
    public E remove(int r2) {
        return delegate().remove(r2);
    }

    @Override // java.util.List
    @ParametricNullness
    public E set(int r2, @ParametricNullness E r3) {
        return delegate().set(r2, r3);
    }

    protected boolean standardAdd(@ParametricNullness E r2) {
        add(size(), r2);
        return true;
    }

    protected boolean standardAddAll(int r2, Iterable<? extends E> r3) {
        return Lists.addAllImpl(this, r2, r3);
    }

    protected boolean standardEquals(@CheckForNull Object r2) {
        return Lists.equalsImpl(this, r2);
    }

    protected int standardHashCode() {
        return Lists.hashCodeImpl(this);
    }

    protected int standardIndexOf(@CheckForNull Object r2) {
        return Lists.indexOfImpl(this, r2);
    }

    protected Iterator<E> standardIterator() {
        return listIterator();
    }

    protected int standardLastIndexOf(@CheckForNull Object r2) {
        return Lists.lastIndexOfImpl(this, r2);
    }

    protected ListIterator<E> standardListIterator() {
        return listIterator(0);
    }

    protected ListIterator<E> standardListIterator(int r2) {
        return Lists.listIteratorImpl(this, r2);
    }

    protected List<E> standardSubList(int r2, int r3) {
        return Lists.subListImpl(this, r2, r3);
    }

    @Override // java.util.List
    public List<E> subList(int r2, int r3) {
        return delegate().subList(r2, r3);
    }
}
