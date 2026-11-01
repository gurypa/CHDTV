package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.Enum;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class EnumMultiset<E extends Enum<E>> extends AbstractMultiset<E> implements Serializable {
    private static final long serialVersionUID = 0;
    private transient int[] counts;
    private transient int distinctElements;
    private transient E[] enumConstants;
    private transient long size;
    private transient Class<E> type;

    abstract class Itr<T> implements Iterator<T> {
        int index;
        final /* synthetic */ EnumMultiset this$0;
        int toRemove;

        Itr(EnumMultiset r2) {
            this.this$0 = r2;
            this.index = 0;
            this.toRemove = -1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
        L3:
            if (this.index >= EnumMultiset.access$000(this.this$0).length) goto L8;
            if (EnumMultiset.access$100(this.this$0)[this.index] > 0) goto L6;
            this.index++;
            goto L3
        L6:
            return true;
        L8:
            return false;
        }

        @Override // java.util.Iterator
        public T next() {
            if (hasNext() == false) goto L7;
            T r0 = output(this.index);
            this.toRemove = this.index;
            this.index++;
            return r0;
        L7:
            throw new NoSuchElementException();
        }

        abstract T output(int r1);

        @Override // java.util.Iterator
        public void remove() {
            if (this.toRemove < 0) goto L5;
            boolean r0 = true;
        L6:
            CollectPreconditions.checkRemove(r0);
            if (EnumMultiset.access$100(this.this$0)[this.toRemove] <= 0) goto L9;
            EnumMultiset.access$210(this.this$0);
            EnumMultiset.access$322(this.this$0, EnumMultiset.access$100(this.this$0)[this.toRemove]);
            EnumMultiset.access$100(this.this$0)[this.toRemove] = 0;
        L9:
            this.toRemove = -1;
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    private EnumMultiset(Class<E> r2) {
        this.type = r2;
        Preconditions.checkArgument(r2.isEnum());
        this.enumConstants = r2.getEnumConstants();
        this.counts = new int[this.enumConstants.length];
    }

    static /* synthetic */ Enum[] access$000(EnumMultiset r1) {
        return r1.enumConstants;
    }

    static /* synthetic */ int[] access$100(EnumMultiset r1) {
        return r1.counts;
    }

    static /* synthetic */ int access$210(EnumMultiset r2) {
        int r0 = r2.distinctElements;
        r2.distinctElements = r0 - 1;
        return r0;
    }

    static /* synthetic */ long access$322(EnumMultiset r2, long r3) {
        long r0 = r2.size - r3;
        r2.size = r0;
        return r0;
    }

    private void checkIsE(Object r4) {
        Preconditions.checkNotNull(r4);
        if (isActuallyE(r4) == false) goto L6;
        return;
    L6:
        throw new ClassCastException("Expected an " + this.type + " but got " + r4);
    }

    public static <E extends Enum<E>> EnumMultiset<E> create(Class<E> r1) {
        return new EnumMultiset(r1);
    }

    public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> r3) {
        Iterator<E> r0 = r3.iterator();
        Preconditions.checkArgument(r0.hasNext(), "EnumMultiset constructor passed empty Iterable");
        EnumMultiset<E> r1 = new EnumMultiset(r0.next().getDeclaringClass());
        Iterables.addAll(r1, r3);
        return r1;
    }

    public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> r1, Class<E> r2) {
        EnumMultiset<E> r0 = create(r2);
        Iterables.addAll(r0, r1);
        return r0;
    }

    private boolean isActuallyE(@CheckForNull Object r5) {
        if ((r5 instanceof Enum) == false) goto L10;
        Enum r0 = (Enum) r5;
        int r2 = r0.ordinal();
        if (r2 < this.enumConstants.length) goto L7;
        return false;
    L7:
        if (this.enumConstants[r2] != r0) goto L12;
        return true;
    L12:
        return false;
    L10:
        return false;
    }

    private void readObject(ObjectInputStream r3) throws IOException, ClassNotFoundException {
        r3.defaultReadObject();
        this.type = (Class) Objects.requireNonNull(r3.readObject());
        this.enumConstants = this.type.getEnumConstants();
        this.counts = new int[this.enumConstants.length];
        Serialization.populateMultiset(this, r3);
    }

    private void writeObject(ObjectOutputStream r2) throws IOException {
        r2.defaultWriteObject();
        r2.writeObject(this.type);
        Serialization.writeMultiset(this, r2);
    }

    public int add(E r9, int r10) {
        checkIsE(r9);
        CollectPreconditions.checkNonnegative(r10, "occurrences");
        if (r10 == 0) goto L5;
        int r0 = r9.ordinal();
        int r1 = this.counts[r0];
        long r2 = ((long) r1) + ((long) r10);
        if (r2 > 2147483647L) goto L9;
        boolean r4 = true;
    L10:
        Preconditions.checkArgument(r4, "too many occurrences: %s", r2);
        this.counts[r0] = (int) r2;
        if (r1 != 0) goto L13;
        this.distinctElements++;
    L13:
        this.size += (long) r10;
        return r1;
    L9:
        r4 = false;
        goto L10
    L5:
        return count(r9);
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ int add(Object r1, int r2) {
        return add((Enum) r1, r2);
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        Arrays.fill(this.counts, 0);
        this.size = 0;
        this.distinctElements = 0;
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ boolean contains(@CheckForNull Object r1) {
        return super.contains(r1);
    }

    @Override // com.google.common.collect.Multiset
    public int count(@CheckForNull Object r4) {
        if (r4 != null) goto L4;
        return 0;
    L4:
        if (isActuallyE(r4) == true) goto L7;
        return 0;
    L7:
        return this.counts[((Enum) r4).ordinal()];
    }

    @Override // com.google.common.collect.AbstractMultiset
    int distinctElements() {
        return this.distinctElements;
    }

    @Override // com.google.common.collect.AbstractMultiset
    Iterator<E> elementIterator() {
        return new C06181(this);
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set elementSet() {
        return super.elementSet();
    }

    @Override // com.google.common.collect.AbstractMultiset
    Iterator<Multiset.Entry<E>> entryIterator() {
        return new C06192(this);
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
    public Iterator<E> iterator() {
        return Multisets.iteratorImpl(this);
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int remove(@CheckForNull Object r9, int r10) {
        if (r9 != null) goto L5;
    L18:
        return 0;
    L5:
        if (isActuallyE(r9) == false) goto L18;
        Enum r1 = (Enum) r9;
        CollectPreconditions.checkNonnegative(r10, "occurrences");
        if (r10 == 0) goto L10;
        int r2 = r1.ordinal();
        int r3 = this.counts[r2];
        if (r3 != 0) goto L14;
        return 0;
    L14:
        if (r3 > r10) goto L16;
        this.counts[r2] = 0;
        this.distinctElements--;
        this.size -= (long) r3;
    L17:
        return r3;
    L16:
        this.counts[r2] = r3 - r10;
        this.size -= (long) r10;
        goto L17
    L10:
        return count(r9);
    }

    public int setCount(E r7, int r8) {
        checkIsE(r7);
        CollectPreconditions.checkNonnegative(r8, "count");
        int r0 = r7.ordinal();
        int r1 = this.counts[r0];
        this.counts[r0] = r8;
        this.size += (long) (r8 - r1);
        if (r1 != 0) goto L6;
        if (r8 <= 0) goto L6;
        this.distinctElements++;
    L9:
        return r1;
    L6:
        if (r1 <= 0) goto L9;
        if (r8 != 0) goto L9;
        this.distinctElements--;
        goto L9
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ int setCount(Object r1, int r2) {
        return setCount((Enum) r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ boolean setCount(@ParametricNullness Object r1, int r2, int r3) {
        return super.setCount(r1, r2, r3);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public int size() {
        return Ints.saturatedCast(this.size);
    }
}
