package com.google.common.collect;

import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingMultiset<E> extends ForwardingCollection<E> implements Multiset<E> {

    protected class StandardElementSet extends Multisets.ElementSet<E> {
        final /* synthetic */ ForwardingMultiset this$0;

        public StandardElementSet(ForwardingMultiset r1) {
            this.this$0 = r1;
        }

        @Override // com.google.common.collect.Multisets.ElementSet, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<E> iterator() {
            return Multisets.elementIterator(multiset().entrySet().iterator());
        }

        @Override // com.google.common.collect.Multisets.ElementSet
        Multiset<E> multiset() {
            return this.this$0;
        }
    }

    protected ForwardingMultiset() {
    }

    @Override // com.google.common.collect.Multiset
    public int add(@ParametricNullness E r2, int r3) {
        return delegate().add(r2, r3);
    }

    @Override // com.google.common.collect.Multiset
    public int count(@CheckForNull Object r2) {
        return delegate().count(r2);
    }

    @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected abstract Multiset<E> delegate();

    @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Collection delegate() {
        return delegate();
    }

    public Set<E> elementSet() {
        return delegate().elementSet();
    }

    public Set<Multiset.Entry<E>> entrySet() {
        return delegate().entrySet();
    }

    @Override // java.util.Collection, com.google.common.collect.Multiset
    public boolean equals(@CheckForNull Object r2) {
        if (r2 != this) goto L4;
    L7:
        return true;
    L4:
        if (delegate().equals(r2) == true) goto L7;
        return false;
    }

    @Override // java.util.Collection, com.google.common.collect.Multiset
    public int hashCode() {
        return delegate().hashCode();
    }

    @Override // com.google.common.collect.Multiset
    public int remove(@CheckForNull Object r2, int r3) {
        return delegate().remove(r2, r3);
    }

    @Override // com.google.common.collect.Multiset
    public int setCount(@ParametricNullness E r2, int r3) {
        return delegate().setCount(r2, r3);
    }

    @Override // com.google.common.collect.Multiset
    public boolean setCount(@ParametricNullness E r2, int r3, int r4) {
        return delegate().setCount(r2, r3, r4);
    }

    protected boolean standardAdd(@ParametricNullness E r2) {
        add(r2, 1);
        return true;
    }

    @Override // com.google.common.collect.ForwardingCollection
    protected boolean standardAddAll(Collection<? extends E> r2) {
        return Multisets.addAllImpl(this, r2);
    }

    @Override // com.google.common.collect.ForwardingCollection
    protected void standardClear() {
        Iterators.clear(entrySet().iterator());
    }

    @Override // com.google.common.collect.ForwardingCollection
    protected boolean standardContains(@CheckForNull Object r2) {
        if (count(r2) <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    protected int standardCount(@CheckForNull Object r4) {
        Iterator<Multiset.Entry<E>> r0 = entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        Multiset.Entry<E> r1 = r0.next();
        if (Objects.equal(r1.getElement(), r4) == false) goto L4;
        return r1.getCount();
    L10:
        return 0;
    }

    protected boolean standardEquals(@CheckForNull Object r2) {
        return Multisets.equalsImpl(this, r2);
    }

    protected int standardHashCode() {
        return entrySet().hashCode();
    }

    protected Iterator<E> standardIterator() {
        return Multisets.iteratorImpl(this);
    }

    @Override // com.google.common.collect.ForwardingCollection
    protected boolean standardRemove(@CheckForNull Object r3) {
        if (remove(r3, 1) > 0) goto L7;
        return false;
    L7:
        return true;
    }

    @Override // com.google.common.collect.ForwardingCollection
    protected boolean standardRemoveAll(Collection<?> r2) {
        return Multisets.removeAllImpl(this, r2);
    }

    @Override // com.google.common.collect.ForwardingCollection
    protected boolean standardRetainAll(Collection<?> r2) {
        return Multisets.retainAllImpl(this, r2);
    }

    protected int standardSetCount(@ParametricNullness E r2, int r3) {
        return Multisets.setCountImpl(this, r2, r3);
    }

    protected boolean standardSetCount(@ParametricNullness E r2, int r3, int r4) {
        return Multisets.setCountImpl(this, r2, r3, r4);
    }

    protected int standardSize() {
        return Multisets.linearTimeSizeImpl(this);
    }

    @Override // com.google.common.collect.ForwardingCollection
    protected String standardToString() {
        return entrySet().toString();
    }
}
