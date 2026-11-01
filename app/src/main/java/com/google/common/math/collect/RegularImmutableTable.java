package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {

    /* JADX INFO: renamed from: com.google.common.collect.RegularImmutableTable$1 */
    static /* synthetic */ class C07221 {
    }

    private final class CellSet extends IndexedImmutableSet<Table.Cell<R, C, V>> {
        final /* synthetic */ RegularImmutableTable this$0;

        private CellSet(RegularImmutableTable r1) {
            this.this$0 = r1;
        }

        /* synthetic */ CellSet(RegularImmutableTable r1, C07221 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r6) {
            if ((r6 instanceof Table.Cell) == false) goto L10;
            Table.Cell r0 = (Table.Cell) r6;
            Object r2 = this.this$0.get(r0.getRowKey(), r0.getColumnKey());
            if (r2 != null) goto L7;
            return false;
        L7:
            if (r2.equals(r0.getValue()) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        @Override // com.google.common.collect.IndexedImmutableSet
        Table.Cell<R, C, V> get(int r2) {
            return this.this$0.getCell(r2);
        }

        @Override // com.google.common.collect.IndexedImmutableSet
        /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.size();
        }

        @Override // com.google.common.collect.IndexedImmutableSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    private final class Values extends ImmutableList<V> {
        final /* synthetic */ RegularImmutableTable this$0;

        private Values(RegularImmutableTable r1) {
            this.this$0 = r1;
        }

        /* synthetic */ Values(RegularImmutableTable r1, C07221 r2) {
            this(r1);
        }

        @Override // java.util.List
        public V get(int r2) {
            return (V) this.this$0.getValue(r2);
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.this$0.size();
        }

        @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    RegularImmutableTable() {
    }

    static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> r1) {
        return forCellsInternal(r1, null, null);
    }

    static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> r1, @CheckForNull final Comparator<? super R> r2, @CheckForNull final Comparator<? super C> r3) {
        Preconditions.checkNotNull(r1);
        if (r2 != null) goto L5;
        if (r3 != null) goto L5;
    L7:
        return forCellsInternal(r1, r2, r3);
    L5:
        Collections.sort(r1, new RegularImmutableTable$$ExternalSyntheticLambda0(r2, r3));
        goto L7
    }

    private static <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> r6, @CheckForNull Comparator<? super R> r7, @CheckForNull Comparator<? super C> r8) {
        LinkedHashSet r0 = new LinkedHashSet();
        LinkedHashSet r1 = new LinkedHashSet();
        ImmutableList r2 = ImmutableList.copyOf(r6);
        Iterator<Table.Cell<R, C, V>> r3 = r6.iterator();
    L4:
        if (r3.hasNext() == false) goto L6;
        Table.Cell<R, C, V> r4 = r3.next();
        r0.add(r4.getRowKey());
        r1.add(r4.getColumnKey());
        goto L4
    L6:
        if (r7 != null) goto L8;
        ImmutableSet r5 = ImmutableSet.copyOf(r0);
    L10:
        if (r8 != null) goto L12;
        ImmutableSet r9 = ImmutableSet.copyOf(r1);
    L14:
        return forOrderedComponents(r2, r5, r9);
    L12:
        r9 = ImmutableSet.copyOf(ImmutableList.sortedCopyOf(r8, r1));
        goto L14
    L8:
        r5 = ImmutableSet.copyOf(ImmutableList.sortedCopyOf(r7, r0));
        goto L10
    }

    static <R, C, V> RegularImmutableTable<R, C, V> forOrderedComponents(ImmutableList<Table.Cell<R, C, V>> r6, ImmutableSet<R> r7, ImmutableSet<C> r8) {
        if (r6.size() <= ((((long) r7.size()) * ((long) r8.size())) / 2)) goto L6;
        return new DenseImmutableTable(r6, r7, r8);
    L6:
        return new SparseImmutableTable(r6, r7, r8);
    }

    static /* synthetic */ int lambda$forCells$0(Comparator r3, Comparator r4, Table.Cell r5, Table.Cell r6) {
        if (r3 != null) goto L5;
        int r1 = 0;
    L7:
        if (r1 == 0) goto L9;
        return r1;
    L9:
        if (r4 != null) goto L12;
        return 0;
    L12:
        return r4.compare(r5.getColumnKey(), r6.getColumnKey());
    L5:
        r1 = r3.compare(r5.getRowKey(), r6.getRowKey());
        goto L7
    }

    final void checkNoDuplicate(R r8, C r9, @CheckForNull V r10, V r11) {
        if (r10 != null) goto L4;
        boolean r0 = true;
    L5:
        Preconditions.checkArgument(r0, "Duplicate key: (row=%s, column=%s), values: [%s, %s].", r8, r9, r11, r10);
        return;
    L4:
        r0 = false;
        goto L5
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.AbstractTable
    final ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
        if (isEmpty() == false) goto L6;
        return ImmutableSet.m259of();
    L6:
        return new CellSet(this, null);
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.AbstractTable
    /* bridge */ /* synthetic */ Set createCellSet() {
        return createCellSet();
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.AbstractTable
    final ImmutableCollection<V> createValues() {
        if (isEmpty() == false) goto L6;
        return ImmutableList.m212of();
    L6:
        return new Values(this, null);
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.AbstractTable
    /* bridge */ /* synthetic */ Collection createValues() {
        return createValues();
    }

    abstract Table.Cell<R, C, V> getCell(int r1);

    abstract V getValue(int r1);

    @Override // com.google.common.collect.ImmutableTable
    abstract Object writeReplace();
}
