package com.google.common.collect;

import java.util.ListIterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class RegularImmutableAsList<E> extends ImmutableAsList<E> {
    private final ImmutableCollection<E> delegate;
    private final ImmutableList<? extends E> delegateList;

    RegularImmutableAsList(ImmutableCollection<E> r1, ImmutableList<? extends E> r2) {
        this.delegate = r1;
        this.delegateList = r2;
    }

    RegularImmutableAsList(ImmutableCollection<E> r2, Object[] r3) {
        this(r2, ImmutableList.asImmutableList(r3));
    }

    RegularImmutableAsList(ImmutableCollection<E> r2, Object[] r3, int r4) {
        this(r2, ImmutableList.asImmutableList(r3, r4));
    }

    @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
    int copyIntoArray(Object[] r2, int r3) {
        return this.delegateList.copyIntoArray(r2, r3);
    }

    @Override // com.google.common.collect.ImmutableAsList
    ImmutableCollection<E> delegateCollection() {
        return this.delegate;
    }

    ImmutableList<? extends E> delegateList() {
        return this.delegateList;
    }

    @Override // java.util.List
    public E get(int r2) {
        return this.delegateList.get(r2);
    }

    @Override // com.google.common.collect.ImmutableCollection
    @CheckForNull
    Object[] internalArray() {
        return this.delegateList.internalArray();
    }

    @Override // com.google.common.collect.ImmutableCollection
    int internalArrayEnd() {
        return this.delegateList.internalArrayEnd();
    }

    @Override // com.google.common.collect.ImmutableCollection
    int internalArrayStart() {
        return this.delegateList.internalArrayStart();
    }

    @Override // com.google.common.collect.ImmutableList, java.util.List
    public UnmodifiableListIterator<E> listIterator(int r2) {
        return this.delegateList.listIterator(r2);
    }

    @Override // com.google.common.collect.ImmutableList, java.util.List
    public /* bridge */ /* synthetic */ ListIterator listIterator(int r1) {
        return listIterator(r1);
    }

    @Override // com.google.common.collect.ImmutableAsList, com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return super.writeReplace();
    }
}
