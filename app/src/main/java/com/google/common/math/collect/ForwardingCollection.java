package com.google.common.collect;

import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingCollection<E> extends ForwardingObject implements Collection<E> {
    protected ForwardingCollection() {
    }

    public boolean add(@ParametricNullness E r2) {
        return delegate().add(r2);
    }

    public boolean addAll(Collection<? extends E> r2) {
        return delegate().addAll(r2);
    }

    public void clear() {
        delegate().clear();
    }

    public boolean contains(@CheckForNull Object r2) {
        return delegate().contains(r2);
    }

    public boolean containsAll(Collection<?> r2) {
        return delegate().containsAll(r2);
    }

    @Override // com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingObject
    protected abstract Collection<E> delegate();

    @Override // java.util.Collection
    public boolean isEmpty() {
        return delegate().isEmpty();
    }

    public Iterator<E> iterator() {
        return delegate().iterator();
    }

    public boolean remove(@CheckForNull Object r2) {
        return delegate().remove(r2);
    }

    public boolean removeAll(Collection<?> r2) {
        return delegate().removeAll(r2);
    }

    public boolean retainAll(Collection<?> r2) {
        return delegate().retainAll(r2);
    }

    @Override // java.util.Collection
    public int size() {
        return delegate().size();
    }

    protected boolean standardAddAll(Collection<? extends E> r2) {
        return Iterators.addAll(this, r2.iterator());
    }

    protected void standardClear() {
        Iterators.clear(iterator());
    }

    protected boolean standardContains(@CheckForNull Object r2) {
        return Iterators.contains(iterator(), r2);
    }

    protected boolean standardContainsAll(Collection<?> r2) {
        return Collections2.containsAllImpl(this, r2);
    }

    protected boolean standardIsEmpty() {
        return !iterator().hasNext();
    }

    protected boolean standardRemove(@CheckForNull Object r3) {
        Iterator<E> r0 = iterator();
    L4:
        if (r0.hasNext() == false) goto L9;
        if (Objects.equal(r0.next(), r3) == false) goto L4;
        r0.remove();
        return true;
    L9:
        return false;
    }

    protected boolean standardRemoveAll(Collection<?> r2) {
        return Iterators.removeAll(iterator(), r2);
    }

    protected boolean standardRetainAll(Collection<?> r2) {
        return Iterators.retainAll(iterator(), r2);
    }

    protected Object[] standardToArray() {
        return toArray(new Object[size()]);
    }

    protected <T> T[] standardToArray(T[] r2) {
        return (T[]) ObjectArrays.toArrayImpl(this, r2);
    }

    protected String standardToString() {
        return Collections2.toStringImpl(this);
    }

    public Object[] toArray() {
        return delegate().toArray();
    }

    public <T> T[] toArray(T[] r2) {
        return (T[]) delegate().toArray(r2);
    }
}
