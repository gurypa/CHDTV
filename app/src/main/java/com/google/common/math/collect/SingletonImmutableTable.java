package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class SingletonImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {
    final C singleColumnKey;
    final R singleRowKey;
    final V singleValue;

    SingletonImmutableTable(Table.Cell<R, C, V> r4) {
        this(r4.getRowKey(), r4.getColumnKey(), r4.getValue());
    }

    SingletonImmutableTable(R r2, C r3, V r4) {
        this.singleRowKey = (R) Preconditions.checkNotNull(r2);
        this.singleColumnKey = (C) Preconditions.checkNotNull(r3);
        this.singleValue = (V) Preconditions.checkNotNull(r4);
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public ImmutableMap<R, V> column(C r3) {
        Preconditions.checkNotNull(r3);
        if (containsColumn(r3) == false) goto L6;
        return ImmutableMap.m232of(this.singleRowKey, this.singleValue);
    L6:
        return ImmutableMap.m231of();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ Map column(Object r1) {
        return column(r1);
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public ImmutableMap<C, Map<R, V>> columnMap() {
        return ImmutableMap.m232of(this.singleColumnKey, ImmutableMap.m232of(this.singleRowKey, this.singleValue));
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ Map columnMap() {
        return columnMap();
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.AbstractTable
    ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
        return ImmutableSet.m260of(cellOf(this.singleRowKey, this.singleColumnKey, this.singleValue));
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.AbstractTable
    /* bridge */ /* synthetic */ Set createCellSet() {
        return createCellSet();
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.AbstractTable
    ImmutableCollection<V> createValues() {
        return ImmutableSet.m260of(this.singleValue);
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.AbstractTable
    /* bridge */ /* synthetic */ Collection createValues() {
        return createValues();
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public ImmutableMap<R, Map<C, V>> rowMap() {
        return ImmutableMap.m232of(this.singleRowKey, ImmutableMap.m232of(this.singleColumnKey, this.singleValue));
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ Map rowMap() {
        return rowMap();
    }

    @Override // com.google.common.collect.Table
    public int size() {
        return 1;
    }

    @Override // com.google.common.collect.ImmutableTable
    Object writeReplace() {
        return ImmutableTable.SerializedForm.create(this, new int[]{0}, new int[]{0});
    }
}
