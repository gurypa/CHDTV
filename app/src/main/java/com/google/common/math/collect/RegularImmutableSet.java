package com.google.common.collect;

import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class RegularImmutableSet<E> extends ImmutableSet<E> {
    static final RegularImmutableSet<Object> EMPTY = null;
    private static final Object[] EMPTY_ARRAY = null;
    final transient Object[] elements;
    private final transient int hashCode;
    private final transient int mask;
    private final transient int size;
    final transient Object[] table;

    static {
        EMPTY_ARRAY = new Object[0];
        EMPTY = new RegularImmutableSet(EMPTY_ARRAY, 0, EMPTY_ARRAY, 0, 0);
    }

    RegularImmutableSet(Object[] r1, int r2, Object[] r3, int r4, int r5) {
        this.elements = r1;
        this.hashCode = r2;
        this.table = r3;
        this.mask = r4;
        this.size = r5;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@CheckForNull Object r6) {
        Object[] r0 = this.table;
        if (r6 != null) goto L5;
    L16:
        return false;
    L5:
        if (r0.length == 0) goto L16;
        int r2 = Hashing.smearedHash(r6);
    L8:
        int r3 = r2 & this.mask;
        Object r4 = r0[r3];
        if (r4 == null) goto L10;
        if (r4.equals(r6) == true) goto L13;
        r2 = r3 + 1;
        goto L8
    L13:
        return true;
    L10:
        return false;
    }

    @Override // com.google.common.collect.ImmutableCollection
    int copyIntoArray(Object[] r4, int r5) {
        System.arraycopy(this.elements, 0, r4, r5, this.size);
        return this.size + r5;
    }

    @Override // com.google.common.collect.ImmutableSet
    ImmutableList<E> createAsList() {
        return ImmutableList.asImmutableList(this.elements, this.size);
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public int hashCode() {
        return this.hashCode;
    }

    @Override // com.google.common.collect.ImmutableCollection
    Object[] internalArray() {
        return this.elements;
    }

    @Override // com.google.common.collect.ImmutableCollection
    int internalArrayEnd() {
        return this.size;
    }

    @Override // com.google.common.collect.ImmutableCollection
    int internalArrayStart() {
        return 0;
    }

    @Override // com.google.common.collect.ImmutableSet
    boolean isHashCodeFast() {
        return true;
    }

    @Override // com.google.common.collect.ImmutableCollection
    boolean isPartialView() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public UnmodifiableIterator<E> iterator() {
        return asList().iterator();
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return super.writeReplace();
    }
}
