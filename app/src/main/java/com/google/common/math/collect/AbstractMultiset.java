package com.google.common.collect;

import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractMultiset<E> extends AbstractCollection<E> implements Multiset<E> {

    @CheckForNull
    @LazyInit
    private transient Set<E> elementSet;

    @CheckForNull
    @LazyInit
    private transient Set<Multiset.Entry<E>> entrySet;

    class ElementSet extends Multisets.ElementSet<E> {
        final /* synthetic */ AbstractMultiset this$0;

        ElementSet(AbstractMultiset r1) {
            this.this$0 = r1;
        }

        @Override // com.google.common.collect.Multisets.ElementSet, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<E> iterator() {
            return this.this$0.elementIterator();
        }

        @Override // com.google.common.collect.Multisets.ElementSet
        Multiset<E> multiset() {
            return this.this$0;
        }
    }

    class EntrySet extends Multisets.EntrySet<E> {
        final /* synthetic */ AbstractMultiset this$0;

        EntrySet(AbstractMultiset r1) {
            this.this$0 = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Multiset.Entry<E>> iterator() {
            return this.this$0.entryIterator();
        }

        @Override // com.google.common.collect.Multisets.EntrySet
        Multiset<E> multiset() {
            return this.this$0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.distinctElements();
        }
    }

    AbstractMultiset() {
    }

    public int add(@ParametricNullness E r2, int r3) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public final boolean add(@ParametricNullness E r2) {
        add(r2, 1);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean addAll(Collection<? extends E> r2) {
        return Multisets.addAllImpl(this, r2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public abstract void clear();

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public boolean contains(@CheckForNull Object r2) {
        if (count(r2) <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    Set<E> createElementSet() {
        return new ElementSet(this);
    }

    Set<Multiset.Entry<E>> createEntrySet() {
        return new EntrySet(this);
    }

    abstract int distinctElements();

    abstract Iterator<E> elementIterator();

    @Override // com.google.common.collect.Multiset
    public Set<E> elementSet() {
        Set<E> r0 = this.elementSet;
        if (r0 != null) goto L6;
        Set<E> r1 = createElementSet();
        this.elementSet = r1;
        return r1;
    L6:
        return r0;
    }

    abstract Iterator<Multiset.Entry<E>> entryIterator();

    @Override // com.google.common.collect.Multiset
    public Set<Multiset.Entry<E>> entrySet() {
        Set<Multiset.Entry<E>> r0 = this.entrySet;
        if (r0 != null) goto L6;
        Set<Multiset.Entry<E>> r1 = createEntrySet();
        this.entrySet = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // java.util.Collection, com.google.common.collect.Multiset
    public final boolean equals(@CheckForNull Object r2) {
        return Multisets.equalsImpl(this, r2);
    }

    @Override // java.util.Collection, com.google.common.collect.Multiset
    public final int hashCode() {
        return entrySet().hashCode();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return entrySet().isEmpty();
    }

    public int remove(@CheckForNull Object r2, int r3) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public final boolean remove(@CheckForNull Object r3) {
        if (remove(r3, 1) > 0) goto L7;
        return false;
    L7:
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public final boolean removeAll(Collection<?> r2) {
        return Multisets.removeAllImpl(this, r2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public final boolean retainAll(Collection<?> r2) {
        return Multisets.retainAllImpl(this, r2);
    }

    public int setCount(@ParametricNullness E r2, int r3) {
        return Multisets.setCountImpl(this, r2, r3);
    }

    public boolean setCount(@ParametricNullness E r2, int r3, int r4) {
        return Multisets.setCountImpl(this, r2, r3, r4);
    }

    @Override // java.util.AbstractCollection, com.google.common.collect.Multiset
    public final String toString() {
        return entrySet().toString();
    }
}
