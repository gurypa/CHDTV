package com.google.common.collect;

import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class IndexedImmutableSet<E> extends ImmutableSet<E> {
    IndexedImmutableSet() {
    }

    @Override // com.google.common.collect.ImmutableCollection
    int copyIntoArray(Object[] r2, int r3) {
        return asList().copyIntoArray(r2, r3);
    }

    @Override // com.google.common.collect.ImmutableSet
    ImmutableList<E> createAsList() {
        return new C06431(this);
    }

    abstract E get(int r1);

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public UnmodifiableIterator<E> iterator() {
        return asList().iterator();
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return iterator();
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return super.writeReplace();
    }
}
