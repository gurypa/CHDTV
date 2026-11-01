package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.DoNotMock;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use ImmutableList.of or another implementation")
@ElementTypesAreNonnullByDefault
public abstract class ImmutableCollection<E> extends AbstractCollection<E> implements Serializable {
    private static final Object[] EMPTY_ARRAY = null;
    static final int SPLITERATOR_CHARACTERISTICS = 1296;
    private static final long serialVersionUID = 912559;

    static abstract class ArrayBasedBuilder<E> extends Builder<E> {
        Object[] contents;
        boolean forceCopy;
        int size;

        ArrayBasedBuilder(int r2) {
            CollectPreconditions.checkNonnegative(r2, "initialCapacity");
            this.contents = new Object[r2];
            this.size = 0;
        }

        private void ensureRoomFor(int r4) {
            Object[] r0 = this.contents;
            int r1 = expandedCapacity(r0.length, this.size + r4);
            if (r1 <= r0.length) goto L5;
        L6:
            this.contents = Arrays.copyOf(this.contents, r1);
            this.forceCopy = false;
            return;
        L5:
            if (this.forceCopy == true) goto L6;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public ArrayBasedBuilder<E> add(E r4) {
            Preconditions.checkNotNull(r4);
            ensureRoomFor(1);
            Object[] r0 = this.contents;
            int r1 = this.size;
            this.size = r1 + 1;
            r0[r1] = r4;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ Builder add(Object r1) {
            return add(r1);
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E... r2) {
            addAll(r2, r2.length);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterable<? extends E> r5) {
            if ((r5 instanceof Collection) == false) goto L8;
            Collection r0 = (Collection) r5;
            ensureRoomFor(r0.size());
            if ((r0 instanceof ImmutableCollection) == false) goto L8;
            this.size = ((ImmutableCollection) r0).copyIntoArray(this.contents, this.size);
            return this;
        L8:
            super.addAll(r5);
            return this;
        }

        final void addAll(Object[] r4, int r5) {
            ObjectArrays.checkElementsNotNull(r4, r5);
            ensureRoomFor(r5);
            System.arraycopy(r4, 0, this.contents, this.size, r5);
            this.size += r5;
        }
    }

    @DoNotMock
    public static abstract class Builder<E> {
        static final int DEFAULT_INITIAL_CAPACITY = 4;

        Builder() {
        }

        static int expandedCapacity(int r2, int r3) {
            if (r3 < 0) goto L12;
            if (r3 > r2) goto L5;
            return r2;
        L5:
            int r0 = ((r2 >> 1) + r2) + 1;
            if (r0 >= r3) goto L8;
            r0 = Integer.highestOneBit(r3 - 1) << 1;
        L8:
            if (r0 >= 0) goto L13;
            return Integer.MAX_VALUE;
        L13:
            return r0;
        L12:
            throw new IllegalArgumentException("cannot store more than MAX_VALUE elements");
        }

        public abstract Builder<E> add(E r1);

        public Builder<E> add(E... r4) {
            int r0 = r4.length;
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L5;
            add(r4[r1]);
            r1 = r1 + 1;
            goto L3
        L5:
            return this;
        }

        public Builder<E> addAll(Iterable<? extends E> r3) {
            Iterator<? extends E> r0 = r3.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            add(r0.next());
            goto L4
        L6:
            return this;
        }

        public Builder<E> addAll(Iterator<? extends E> r2) {
        L3:
            if (r2.hasNext() == false) goto L5;
            add(r2.next());
            goto L3
        L5:
            return this;
        }

        public abstract ImmutableCollection<E> build();
    }

    static {
        EMPTY_ARRAY = new Object[0];
    }

    ImmutableCollection() {
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    @Deprecated
    public final boolean add(E r2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    @Deprecated
    public final boolean addAll(Collection<? extends E> r2) {
        throw new UnsupportedOperationException();
    }

    public ImmutableList<E> asList() {
        if (isEmpty() == false) goto L6;
        return ImmutableList.m212of();
    L6:
        return ImmutableList.asImmutableList(toArray());
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public abstract boolean contains(@CheckForNull Object r1);

    int copyIntoArray(Object[] r4, int r5) {
        UnmodifiableIterator<E> r0 = iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r4[r5] = r0.next();
        r5 = r5 + 1;
        goto L4
    L6:
        return r5;
    }

    @CheckForNull
    Object[] internalArray() {
        return null;
    }

    int internalArrayEnd() {
        throw new UnsupportedOperationException();
    }

    int internalArrayStart() {
        throw new UnsupportedOperationException();
    }

    abstract boolean isPartialView();

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public abstract UnmodifiableIterator<E> iterator();

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    @Deprecated
    public final boolean remove(@CheckForNull Object r2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    @Deprecated
    public final boolean removeAll(Collection<?> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    @Deprecated
    public final boolean retainAll(Collection<?> r2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, SPLITERATOR_CHARACTERISTICS);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final Object[] toArray() {
        return toArray(EMPTY_ARRAY);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final <T> T[] toArray(T[] r5) {
        Preconditions.checkNotNull(r5);
        int r0 = size();
        if (r5.length >= r0) goto L11;
        Object[] r1 = internalArray();
        if (r1 != null) goto L7;
        r5 = (T[]) ObjectArrays.newArray(r5, r0);
    L13:
        copyIntoArray(r5, 0);
        return r5;
    L7:
        return (T[]) Platform.copy(r1, internalArrayStart(), internalArrayEnd(), r5);
    L11:
        if (r5.length <= r0) goto L13;
        r5[r0] = null;
        goto L13
    }

    Object writeReplace() {
        return new ImmutableList.SerializedForm(toArray());
    }
}
