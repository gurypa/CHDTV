package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractIndexedListIterator<E> extends UnmodifiableListIterator<E> {
    private int position;
    private final int size;

    protected AbstractIndexedListIterator(int r2) {
        this(r2, 0);
    }

    protected AbstractIndexedListIterator(int r1, int r2) {
        Preconditions.checkPositionIndex(r2, r1);
        this.size = r1;
        this.position = r2;
    }

    @ParametricNullness
    protected abstract E get(int r1);

    @Override // java.util.Iterator, java.util.ListIterator
    public final boolean hasNext() {
        if (this.position >= this.size) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.ListIterator
    public final boolean hasPrevious() {
        if (this.position <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.Iterator, java.util.ListIterator
    @ParametricNullness
    public final E next() {
        if (hasNext() == false) goto L7;
        int r0 = this.position;
        this.position = r0 + 1;
        return get(r0);
    L7:
        throw new NoSuchElementException();
    }

    @Override // java.util.ListIterator
    public final int nextIndex() {
        return this.position;
    }

    @Override // java.util.ListIterator
    @ParametricNullness
    public final E previous() {
        if (hasPrevious() == false) goto L7;
        int r0 = this.position - 1;
        this.position = r0;
        return get(r0);
    L7:
        throw new NoSuchElementException();
    }

    @Override // java.util.ListIterator
    public final int previousIndex() {
        return this.position - 1;
    }
}
