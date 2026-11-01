package com.google.common.collect;

import androidx.exifinterface.media.ExifInterface;
import com.google.errorprone.annotations.Immutable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
@Immutable(containerOf = {"R", "C", ExifInterface.GPS_MEASUREMENT_INTERRUPTED})
@ElementTypesAreNonnullByDefault
final class SparseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
    static final ImmutableTable<Object, Object, Object> EMPTY = null;
    private final int[] cellColumnInRowIndices;
    private final int[] cellRowIndices;
    private final ImmutableMap<C, ImmutableMap<R, V>> columnMap;
    private final ImmutableMap<R, ImmutableMap<C, V>> rowMap;

    static {
        EMPTY = new SparseImmutableTable(ImmutableList.m212of(), ImmutableSet.m259of(), ImmutableSet.m259of());
    }

    /* JADX WARN: Multi-variable type inference failed */
    SparseImmutableTable(ImmutableList<Table.Cell<R, C, V>> r14, ImmutableSet<R> r15, ImmutableSet<C> r16) {
        ImmutableMap r0 = Maps.indexMap(r15);
        LinkedHashMap r1 = Maps.newLinkedHashMap();
        UnmodifiableIterator<R> r2 = r15.iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        r1.put(r2.next(), new LinkedHashMap());
        goto L4
    L6:
        LinkedHashMap r3 = Maps.newLinkedHashMap();
        UnmodifiableIterator<C> r4 = r16.iterator();
    L8:
        if (r4.hasNext() == false) goto L10;
        r3.put(r4.next(), new LinkedHashMap());
        goto L8
    L10:
        int[] r5 = new int[r14.size()];
        int[] r6 = new int[r14.size()];
        int r7 = 0;
    L12:
        if (r7 >= r14.size()) goto L14;
        Table.Cell<R, C, V> r8 = r14.get(r7);
        R r9 = r8.getRowKey();
        C r10 = r8.getColumnKey();
        V r11 = r8.getValue();
        r5[r7] = ((Integer) Objects.requireNonNull((Integer) r0.get(r9))).intValue();
        Map r12 = (Map) Objects.requireNonNull((Map) r1.get(r9));
        r6[r7] = r12.size();
        checkNoDuplicate(r9, r10, r12.put(r10, r11), r11);
        ((Map) Objects.requireNonNull((Map) r3.get(r10))).put(r9, r11);
        r7 = r7 + 1;
        goto L12
    L14:
        this.cellRowIndices = r5;
        this.cellColumnInRowIndices = r6;
        ImmutableMap.Builder r13 = new ImmutableMap.Builder(r1.size());
        Iterator r17 = r1.entrySet().iterator();
    L16:
        if (r17.hasNext() == false) goto L18;
        Map.Entry r18 = (Map.Entry) r17.next();
        r13.put(r18.getKey(), ImmutableMap.copyOf((Map) r18.getValue()));
        goto L16
    L18:
        this.rowMap = r13.buildOrThrow();
        ImmutableMap.Builder r19 = new ImmutableMap.Builder(r3.size());
        Iterator r20 = r3.entrySet().iterator();
    L20:
        if (r20.hasNext() == false) goto L22;
        Map.Entry r21 = (Map.Entry) r20.next();
        r19.put(r21.getKey(), ImmutableMap.copyOf((Map) r21.getValue()));
        goto L20
    L22:
        this.columnMap = r19.buildOrThrow();
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public ImmutableMap<C, Map<R, V>> columnMap() {
        return ImmutableMap.copyOf(this.columnMap);
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ Map columnMap() {
        return columnMap();
    }

    @Override // com.google.common.collect.RegularImmutableTable
    Table.Cell<R, C, V> getCell(int r9) {
        int r0 = this.cellRowIndices[r9];
        Map.Entry<R, ImmutableMap<C, V>> r1 = this.rowMap.entrySet().asList().get(r0);
        ImmutableMap<C, V> r2 = r1.getValue();
        int r3 = this.cellColumnInRowIndices[r9];
        Map.Entry<C, V> r4 = r2.entrySet().asList().get(r3);
        return cellOf(r1.getKey(), r4.getKey(), r4.getValue());
    }

    @Override // com.google.common.collect.RegularImmutableTable
    V getValue(int r5) {
        int r0 = this.cellRowIndices[r5];
        ImmutableMap<C, V> r1 = this.rowMap.values().asList().get(r0);
        int r2 = this.cellColumnInRowIndices[r5];
        return r1.values().asList().get(r2);
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public ImmutableMap<R, Map<C, V>> rowMap() {
        return ImmutableMap.copyOf(this.rowMap);
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ Map rowMap() {
        return rowMap();
    }

    @Override // com.google.common.collect.Table
    public int size() {
        return this.cellRowIndices.length;
    }

    @Override // com.google.common.collect.RegularImmutableTable, com.google.common.collect.ImmutableTable
    Object writeReplace() {
        ImmutableMap r0 = Maps.indexMap(columnKeySet());
        int[] r1 = new int[cellSet().size()];
        int r2 = 0;
        UnmodifiableIterator<Table.Cell<R, C, V>> r3 = cellSet().iterator();
    L4:
        if (r3.hasNext() == false) goto L7;
        r1[r2] = ((Integer) Objects.requireNonNull((Integer) r0.get(r3.next().getColumnKey()))).intValue();
        r2 = r2 + 1;
        goto L4
    L7:
        return ImmutableTable.SerializedForm.create(this, this.cellRowIndices, r1);
    }
}
