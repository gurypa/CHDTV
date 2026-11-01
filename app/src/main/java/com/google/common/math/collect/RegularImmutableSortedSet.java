package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class RegularImmutableSortedSet<E> extends ImmutableSortedSet<E> {
    static final RegularImmutableSortedSet<Comparable> NATURAL_EMPTY_SET = null;
    final transient ImmutableList<E> elements;

    static {
        NATURAL_EMPTY_SET = new RegularImmutableSortedSet(ImmutableList.m212of(), Ordering.natural());
    }

    RegularImmutableSortedSet(ImmutableList<E> r1, Comparator<? super E> r2) {
        super(r2);
        this.elements = r1;
    }

    private int unsafeBinarySearch(Object r3) throws ClassCastException {
        return Collections.binarySearch(this.elements, r3, unsafeComparator());
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    public ImmutableList<E> asList() {
        return this.elements;
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    @CheckForNull
    public E ceiling(E r3) {
        int r0 = tailIndex(r3, true);
        if (r0 != size()) goto L6;
        return null;
    L6:
        return this.elements.get(r0);
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@CheckForNull Object r3) {
        if (r3 != null) goto L10;
        return false;
    L10:
        if (unsafeBinarySearch(r3) < 0) goto L12;
        return true;
    L12:
        return false;
    L8:
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean containsAll(Collection<?> r9) {
        if ((r9 instanceof Multiset) == false) goto L6;
        r9 = ((Multiset) r9).elementSet();
    L6:
        if (SortedIterables.hasSameComparator(comparator(), r9) == false) goto L34;
        if (r9.size() <= 1) goto L34;
        UnmodifiableIterator<E> r0 = iterator();
        Iterator<?> r2 = r9.iterator();
        if (r0.hasNext() == true) goto L13;
        return false;
    L13:
        Object r3 = r2.next();
        E r5 = r0.next();
    L35:
        int r6 = unsafeCompare(r5, r3);     // Catch: ClassCastException -> L29 Throwable -> L31
        if (r6 < 0) goto L17;
        if (r6 == 0) goto L22;
        if (r6 <= 0) goto L35;
        return false;
    L22:
        if (r2.hasNext() == false) goto L23;
        r3 = r2.next();     // Catch: ClassCastException -> L29 Throwable -> L31
        goto L35
    L23:
        return true;
    L17:
        if (r0.hasNext() == false) goto L18;
        r5 = r0.next();     // Catch: ClassCastException -> L29 Throwable -> L31
        goto L35
    L18:
        return false;
    L32:
        return false;
    L34:
        return super.containsAll(r9);
    }

    @Override // com.google.common.collect.ImmutableCollection
    int copyIntoArray(Object[] r2, int r3) {
        return this.elements.copyIntoArray(r2, r3);
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<E> createDescendingSet() {
        Comparator r0 = Collections.reverseOrder(this.comparator);
        if (isEmpty() == false) goto L6;
        return emptySet(r0);
    L6:
        return new RegularImmutableSortedSet(this.elements.reverse(), r0);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public UnmodifiableIterator<E> descendingIterator() {
        return this.elements.reverse().iterator();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public /* bridge */ /* synthetic */ Iterator descendingIterator() {
        return descendingIterator();
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public boolean equals(@CheckForNull Object r9) {
        if (r9 != this) goto L6;
        return true;
    L6:
        if ((r9 instanceof Set) == true) goto L8;
        return false;
    L8:
        Set r1 = (Set) r9;
        if (size() == r1.size()) goto L12;
        return false;
    L12:
        if (isEmpty() == false) goto L15;
        return true;
    L15:
        if (SortedIterables.hasSameComparator(this.comparator, r1) == false) goto L33;
        Iterator<E> r3 = r1.iterator();
        UnmodifiableIterator<E> r4 = iterator();     // Catch: NoSuchElementException -> L28 ClassCastException -> L30
    L19:
        if (r4.hasNext() == false) goto L27;
        E r5 = r4.next();     // Catch: NoSuchElementException -> L28 ClassCastException -> L30
        E r6 = r3.next();     // Catch: NoSuchElementException -> L28 ClassCastException -> L30
        if (r6 == null) goto L26;
        if (unsafeCompare(r5, r6) == 0) goto L19;
    L26:
        return false;
    L27:
        return true;
    L31:
        return false;
    L29:
        return false;
    L33:
        return containsAll(r1);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.SortedSet
    public E first() {
        if (isEmpty() == true) goto L7;
        return this.elements.get(0);
    L7:
        throw new NoSuchElementException();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    @CheckForNull
    public E floor(E r3) {
        int r1 = headIndex(r3, true) - 1;
        if (r1 != (-1)) goto L6;
        return null;
    L6:
        return this.elements.get(r1);
    }

    RegularImmutableSortedSet<E> getSubSet(int r4, int r5) {
        if (r4 == 0) goto L4;
    L6:
        if (r4 >= r5) goto L10;
        return new RegularImmutableSortedSet(this.elements.subList(r4, r5), this.comparator);
    L10:
        return emptySet(this.comparator);
    L4:
        if (r5 != size()) goto L6;
        return this;
    }

    int headIndex(E r4, boolean r5) {
        int r0 = Collections.binarySearch(this.elements, Preconditions.checkNotNull(r4), comparator());
        if (r0 < 0) goto L9;
        if (r5 == false) goto L7;
        return r0 + 1;
    L7:
        return r0;
    L9:
        return ~r0;
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<E> headSetImpl(E r3, boolean r4) {
        return getSubSet(0, headIndex(r3, r4));
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    @CheckForNull
    public E higher(E r3) {
        int r0 = tailIndex(r3, false);
        if (r0 != size()) goto L6;
        return null;
    L6:
        return this.elements.get(r0);
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    int indexOf(@CheckForNull Object r4) {
        if (r4 != null) goto L11;
        return -1;
    L11:
        int r1 = Collections.binarySearch(this.elements, r4, unsafeComparator());     // Catch: ClassCastException -> L9
        if (r1 >= 0) goto L8;
        return -1;
    L8:
        return r1;
    L10:
        return -1;
    }

    @Override // com.google.common.collect.ImmutableCollection
    @CheckForNull
    Object[] internalArray() {
        return this.elements.internalArray();
    }

    @Override // com.google.common.collect.ImmutableCollection
    int internalArrayEnd() {
        return this.elements.internalArrayEnd();
    }

    @Override // com.google.common.collect.ImmutableCollection
    int internalArrayStart() {
        return this.elements.internalArrayStart();
    }

    @Override // com.google.common.collect.ImmutableCollection
    boolean isPartialView() {
        return this.elements.isPartialView();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public UnmodifiableIterator<E> iterator() {
        return this.elements.iterator();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return iterator();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.SortedSet
    public E last() {
        if (isEmpty() == true) goto L7;
        return this.elements.get(size() - 1);
    L7:
        throw new NoSuchElementException();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    @CheckForNull
    public E lower(E r3) {
        int r0 = headIndex(r3, false) - 1;
        if (r0 != (-1)) goto L6;
        return null;
    L6:
        return this.elements.get(r0);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.elements.size();
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<E> subSetImpl(E r2, boolean r3, E r4, boolean r5) {
        return tailSetImpl(r2, r3).headSetImpl(r4, r5);
    }

    int tailIndex(E r4, boolean r5) {
        int r0 = Collections.binarySearch(this.elements, Preconditions.checkNotNull(r4), comparator());
        if (r0 < 0) goto L9;
        if (r5 == false) goto L7;
        return r0;
    L7:
        return r0 + 1;
    L9:
        return ~r0;
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<E> tailSetImpl(E r3, boolean r4) {
        return getSubSet(tailIndex(r3, r4), size());
    }

    Comparator<Object> unsafeComparator() {
        return this.comparator;
    }

    @Override // com.google.common.collect.ImmutableSortedSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return super.writeReplace();
    }
}
