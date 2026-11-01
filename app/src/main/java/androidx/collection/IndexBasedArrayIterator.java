package androidx.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes.dex */
abstract class IndexBasedArrayIterator<T> implements Iterator<T> {
    private boolean mCanRemove;
    private int mIndex;
    private int mSize;

    IndexBasedArrayIterator(int r1) {
        this.mSize = r1;
    }

    protected abstract T elementAt(int r1);

    @Override // java.util.Iterator
    public final boolean hasNext() {
        if (this.mIndex >= this.mSize) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.Iterator
    public T next() {
        if (hasNext() == false) goto L7;
        T r0 = elementAt(this.mIndex);
        this.mIndex++;
        this.mCanRemove = true;
        return r0;
    L7:
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator
    public void remove() {
        if (this.mCanRemove == false) goto L7;
        int r0 = this.mIndex - 1;
        this.mIndex = r0;
        removeAt(r0);
        this.mSize--;
        this.mCanRemove = false;
        return;
    L7:
        throw new IllegalStateException();
    }

    protected abstract void removeAt(int r1);
}
