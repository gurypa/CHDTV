package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class RegularImmutableList<E> extends ImmutableList<E> {
    static final ImmutableList<Object> EMPTY = null;
    final transient Object[] array;
    private final transient int size;

    static {
        EMPTY = new RegularImmutableList(new Object[0], 0);
    }

    RegularImmutableList(Object[] r1, int r2) {
        this.array = r1;
        this.size = r2;
    }

    @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
    int copyIntoArray(Object[] r4, int r5) {
        System.arraycopy(this.array, 0, r4, r5, this.size);
        return this.size + r5;
    }

    @Override // java.util.List
    public E get(int r2) {
        Preconditions.checkElementIndex(r2, this.size);
        return (E) Objects.requireNonNull(this.array[r2]);
    }

    @Override // com.google.common.collect.ImmutableCollection
    Object[] internalArray() {
        return this.array;
    }

    @Override // com.google.common.collect.ImmutableCollection
    int internalArrayEnd() {
        return this.size;
    }

    @Override // com.google.common.collect.ImmutableCollection
    int internalArrayStart() {
        return 0;
    }

    @Override // com.google.common.collect.ImmutableCollection
    boolean isPartialView() {
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.size;
    }

    @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return super.writeReplace();
    }
}
