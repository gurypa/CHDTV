package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractMapBasedMultiset<E> extends AbstractMultiset<E> implements Serializable {
    private static final long serialVersionUID = 0;
    transient ObjectCountHashMap<E> backingMap;
    transient long size;

    abstract class Itr<T> implements Iterator<T> {
        int entryIndex;
        int expectedModCount;
        final /* synthetic */ AbstractMapBasedMultiset this$0;
        int toRemove;

        Itr(AbstractMapBasedMultiset r2) {
            this.this$0 = r2;
            this.entryIndex = this.this$0.backingMap.firstIndex();
            this.toRemove = -1;
            this.expectedModCount = this.this$0.backingMap.modCount;
        }

        private void checkForConcurrentModification() {
            if (this.this$0.backingMap.modCount != this.expectedModCount) goto L6;
            return;
        L6:
            throw new ConcurrentModificationException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            checkForConcurrentModification();
            if (this.entryIndex < 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.Iterator
        @ParametricNullness
        public T next() {
            if (hasNext() == false) goto L7;
            T r0 = result(this.entryIndex);
            this.toRemove = this.entryIndex;
            this.entryIndex = this.this$0.backingMap.nextIndex(this.entryIndex);
            return r0;
        L7:
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            checkForConcurrentModification();
            if (this.toRemove == (-1)) goto L5;
            boolean r0 = true;
        L6:
            CollectPreconditions.checkRemove(r0);
            this.this$0.size -= (long) this.this$0.backingMap.removeEntry(this.toRemove);
            this.entryIndex = this.this$0.backingMap.nextIndexAfterRemove(this.entryIndex, this.toRemove);
            this.toRemove = -1;
            this.expectedModCount = this.this$0.backingMap.modCount;
            return;
        L5:
            r0 = false;
            goto L6
        }

        @ParametricNullness
        abstract T result(int r1);
    }

    AbstractMapBasedMultiset(int r2) {
        this.backingMap = newBackingMap(r2);
    }

    private void readObject(ObjectInputStream r3) throws IOException, ClassNotFoundException {
        r3.defaultReadObject();
        int r0 = Serialization.readCount(r3);
        this.backingMap = newBackingMap(3);
        Serialization.populateMultiset(this, r3, r0);
    }

    private void writeObject(ObjectOutputStream r1) throws IOException {
        r1.defaultWriteObject();
        Serialization.writeMultiset(this, r1);
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public final int add(@ParametricNullness E r9, int r10) {
        if (r10 == 0) goto L4;
        boolean r0 = true;
        if (r10 <= 0) goto L8;
        boolean r2 = true;
    L9:
        Preconditions.checkArgument(r2, "occurrences cannot be negative: %s", r10);
        int r3 = this.backingMap.indexOf(r9);
        if (r3 != (-1)) goto L13;
        this.backingMap.put(r9, r10);
        this.size += (long) r10;
        return 0;
    L13:
        int r4 = this.backingMap.getValue(r3);
        long r5 = ((long) r4) + ((long) r10);
        if (r5 <= 2147483647L) goto L17;
        r0 = false;
    L17:
        Preconditions.checkArgument(r0, "too many occurrences: %s", r5);
        this.backingMap.setValue(r3, (int) r5);
        this.size += (long) r10;
        return r4;
    L8:
        r2 = false;
        goto L9
    L4:
        return count(r9);
    }

    void addTo(Multiset<? super E> r4) {
        Preconditions.checkNotNull(r4);
        int r0 = this.backingMap.firstIndex();
    L3:
        if (r0 < 0) goto L5;
        r4.add(this.backingMap.getKey(r0), this.backingMap.getValue(r0));
        r0 = this.backingMap.nextIndex(r0);
        goto L3
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public final void clear() {
        this.backingMap.clear();
        this.size = 0;
    }

    @Override // com.google.common.collect.Multiset
    public final int count(@CheckForNull Object r2) {
        return this.backingMap.get(r2);
    }

    @Override // com.google.common.collect.AbstractMultiset
    final int distinctElements() {
        return this.backingMap.size();
    }

    @Override // com.google.common.collect.AbstractMultiset
    final Iterator<E> elementIterator() {
        return new C05941(this);
    }

    @Override // com.google.common.collect.AbstractMultiset
    final Iterator<Multiset.Entry<E>> entryIterator() {
        return new C05952(this);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
    public final Iterator<E> iterator() {
        return Multisets.iteratorImpl(this);
    }

    abstract ObjectCountHashMap<E> newBackingMap(int r1);

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public final int remove(@CheckForNull Object r8, int r9) {
        if (r9 != 0) goto L6;
        return count(r8);
    L6:
        if (r9 <= 0) goto L8;
        boolean r1 = true;
    L9:
        Preconditions.checkArgument(r1, "occurrences cannot be negative: %s", r9);
        int r2 = this.backingMap.indexOf(r8);
        if (r2 != (-1)) goto L12;
        return 0;
    L12:
        int r0 = this.backingMap.getValue(r2);
        if (r0 <= r9) goto L15;
        int r3 = r9;
        this.backingMap.setValue(r2, r0 - r9);
    L16:
        this.size -= (long) r3;
        return r0;
    L15:
        r3 = r0;
        this.backingMap.removeEntry(r2);
        goto L16
    L8:
        r1 = false;
        goto L9
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public final int setCount(@ParametricNullness E r6, int r7) {
        CollectPreconditions.checkNonnegative(r7, "count");
        ObjectCountHashMap<E> r0 = this.backingMap;
        if (r7 != 0) goto L5;
        int r1 = r0.remove(r6);
    L6:
        this.size += (long) (r7 - r1);
        return r1;
    L5:
        r1 = r0.put(r6, r7);
        goto L6
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public final boolean setCount(@ParametricNullness E r8, int r9, int r10) {
        CollectPreconditions.checkNonnegative(r9, "oldCount");
        CollectPreconditions.checkNonnegative(r10, "newCount");
        int r0 = this.backingMap.indexOf(r8);
        if (r0 != (-1)) goto L10;
        if (r9 == 0) goto L6;
        return false;
    L6:
        if (r10 <= 0) goto L8;
        this.backingMap.put(r8, r10);
        this.size += (long) r10;
    L8:
        return true;
    L10:
        if (this.backingMap.getValue(r0) == r9) goto L12;
        return false;
    L12:
        if (r10 != 0) goto L14;
        this.backingMap.removeEntry(r0);
        this.size -= (long) r9;
    L15:
        return true;
    L14:
        this.backingMap.setValue(r0, r10);
        this.size += (long) (r10 - r9);
        goto L15
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public final int size() {
        return Ints.saturatedCast(this.size);
    }
}
