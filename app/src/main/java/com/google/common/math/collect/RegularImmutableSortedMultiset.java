package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class RegularImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E> {
    static final ImmutableSortedMultiset<?> NATURAL_EMPTY_MULTISET = null;
    private static final long[] ZERO_CUMULATIVE_COUNTS = null;
    private final transient long[] cumulativeCounts;
    final transient RegularImmutableSortedSet<E> elementSet;
    private final transient int length;
    private final transient int offset;

    static {
        ZERO_CUMULATIVE_COUNTS = new long[]{0};
        NATURAL_EMPTY_MULTISET = new RegularImmutableSortedMultiset(Ordering.natural());
    }

    RegularImmutableSortedMultiset(RegularImmutableSortedSet<E> r1, long[] r2, int r3, int r4) {
        this.elementSet = r1;
        this.cumulativeCounts = r2;
        this.offset = r3;
        this.length = r4;
    }

    RegularImmutableSortedMultiset(Comparator<? super E> r2) {
        this.elementSet = ImmutableSortedSet.emptySet(r2);
        this.cumulativeCounts = ZERO_CUMULATIVE_COUNTS;
        this.offset = 0;
        this.length = 0;
    }

    private int getCount(int r6) {
        return (int) (this.cumulativeCounts[(this.offset + r6) + 1] - this.cumulativeCounts[this.offset + r6]);
    }

    @Override // com.google.common.collect.Multiset
    public int count(@CheckForNull Object r3) {
        int r0 = this.elementSet.indexOf(r3);
        if (r0 >= 0) goto L7;
        return 0;
    L7:
        return getCount(r0);
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ ImmutableSet elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
    public ImmutableSortedSet<E> elementSet() {
        return this.elementSet;
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ NavigableSet elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ SortedSet elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    public Multiset.Entry<E> firstEntry() {
        if (isEmpty() == false) goto L6;
        return null;
    L6:
        return getEntry(0);
    }

    @Override // com.google.common.collect.ImmutableMultiset
    Multiset.Entry<E> getEntry(int r3) {
        return Multisets.immutableEntry(this.elementSet.asList().get(r3), getCount(r3));
    }

    ImmutableSortedMultiset<E> getSubMultiset(int r6, int r7) {
        Preconditions.checkPositionIndexes(r6, r7, this.length);
        if (r6 == r7) goto L5;
        if (r6 != 0) goto L11;
        if (r7 != this.length) goto L11;
        return this;
    L11:
        return new RegularImmutableSortedMultiset(this.elementSet.getSubSet(r6, r7), this.cumulativeCounts, this.offset + r6, r7 - r6);
    L5:
        return emptyMultiset(comparator());
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.SortedMultiset
    public ImmutableSortedMultiset<E> headMultiset(E r5, BoundType r6) {
        RegularImmutableSortedSet<E> r0 = this.elementSet;
        if (Preconditions.checkNotNull(r6) != BoundType.CLOSED) goto L5;
        boolean r1 = true;
    L7:
        return getSubMultiset(0, r0.headIndex(r5, r1));
    L5:
        r1 = false;
        goto L7
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.SortedMultiset
    public /* bridge */ /* synthetic */ SortedMultiset headMultiset(Object r1, BoundType r2) {
        return headMultiset(r1, r2);
    }

    @Override // com.google.common.collect.ImmutableCollection
    boolean isPartialView() {
        if (this.offset <= 0) goto L5;
        return true;
    L5:
        if (this.length < (this.cumulativeCounts.length - 1)) goto L10;
        return false;
    L10:
        return true;
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    public Multiset.Entry<E> lastEntry() {
        if (isEmpty() == false) goto L6;
        return null;
    L6:
        return getEntry(this.length - 1);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public int size() {
        return Ints.saturatedCast(this.cumulativeCounts[this.offset + this.length] - this.cumulativeCounts[this.offset]);
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.SortedMultiset
    public ImmutableSortedMultiset<E> tailMultiset(E r4, BoundType r5) {
        RegularImmutableSortedSet<E> r0 = this.elementSet;
        if (Preconditions.checkNotNull(r5) != BoundType.CLOSED) goto L5;
        boolean r1 = true;
    L7:
        return getSubMultiset(r0.tailIndex(r4, r1), this.length);
    L5:
        r1 = false;
        goto L7
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.SortedMultiset
    public /* bridge */ /* synthetic */ SortedMultiset tailMultiset(Object r1, BoundType r2) {
        return tailMultiset(r1, r2);
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.ImmutableMultiset, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return super.writeReplace();
    }
}
