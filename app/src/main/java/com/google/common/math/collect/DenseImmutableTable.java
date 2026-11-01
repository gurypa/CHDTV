package com.google.common.collect;

import androidx.exifinterface.media.ExifInterface;
import com.google.errorprone.annotations.Immutable;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Objects;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@Immutable(containerOf = {"R", "C", ExifInterface.GPS_MEASUREMENT_INTERRUPTED})
@ElementTypesAreNonnullByDefault
final class DenseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
    private final int[] cellColumnIndices;
    private final int[] cellRowIndices;
    private final int[] columnCounts;
    private final ImmutableMap<C, Integer> columnKeyToIndex;
    private final ImmutableMap<C, ImmutableMap<R, V>> columnMap;
    private final int[] rowCounts;
    private final ImmutableMap<R, Integer> rowKeyToIndex;
    private final ImmutableMap<R, ImmutableMap<C, V>> rowMap;
    private final V[][] values;

    /* JADX INFO: renamed from: com.google.common.collect.DenseImmutableTable$1 */
    static /* synthetic */ class C06141 {
    }

    private final class Column extends ImmutableArrayMap<R, V> {
        private final int columnIndex;
        final /* synthetic */ DenseImmutableTable this$0;

        Column(DenseImmutableTable r1, int r2) {
            this.this$0 = r1;
            super(DenseImmutableTable.access$500(r1)[r2]);
            this.columnIndex = r2;
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        @CheckForNull
        V getValue(int r3) {
            return (V) DenseImmutableTable.access$400(this.this$0)[r3][this.columnIndex];
        }

        @Override // com.google.common.collect.ImmutableMap
        boolean isPartialView() {
            return true;
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        ImmutableMap<R, Integer> keyToIndex() {
            return DenseImmutableTable.access$600(this.this$0);
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap, com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap, com.google.common.collect.ImmutableMap
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    private final class ColumnMap extends ImmutableArrayMap<C, ImmutableMap<R, V>> {
        final /* synthetic */ DenseImmutableTable this$0;

        private ColumnMap(DenseImmutableTable r1) {
            this.this$0 = r1;
            super(DenseImmutableTable.access$500(r1).length);
        }

        /* synthetic */ ColumnMap(DenseImmutableTable r1, C06141 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        ImmutableMap<R, V> getValue(int r3) {
            return new Column(this.this$0, r3);
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        /* bridge */ /* synthetic */ Object getValue(int r1) {
            return getValue(r1);
        }

        @Override // com.google.common.collect.ImmutableMap
        boolean isPartialView() {
            return false;
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        ImmutableMap<C, Integer> keyToIndex() {
            return DenseImmutableTable.access$300(this.this$0);
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap, com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap, com.google.common.collect.ImmutableMap
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    private static abstract class ImmutableArrayMap<K, V> extends ImmutableMap.IteratorBasedImmutableMap<K, V> {
        private final int size;

        ImmutableArrayMap(int r1) {
            this.size = r1;
        }

        private boolean isFull() {
            if (this.size != keyToIndex().size()) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap, com.google.common.collect.ImmutableMap
        ImmutableSet<K> createKeySet() {
            if (isFull() == false) goto L6;
            return keyToIndex().keySet();
        L6:
            return super.createKeySet();
        }

        @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap
        UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
            return new C06151(this);
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        @CheckForNull
        public V get(@CheckForNull Object r3) {
            Integer r0 = keyToIndex().get(r3);
            if (r0 != null) goto L6;
            return null;
        L6:
            return getValue(r0.intValue());
        }

        K getKey(int r2) {
            return keyToIndex().keySet().asList().get(r2);
        }

        @CheckForNull
        abstract V getValue(int r1);

        abstract ImmutableMap<K, Integer> keyToIndex();

        @Override // java.util.Map
        public int size() {
            return this.size;
        }

        @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap, com.google.common.collect.ImmutableMap
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    private final class Row extends ImmutableArrayMap<C, V> {
        private final int rowIndex;
        final /* synthetic */ DenseImmutableTable this$0;

        Row(DenseImmutableTable r1, int r2) {
            this.this$0 = r1;
            super(DenseImmutableTable.access$200(r1)[r2]);
            this.rowIndex = r2;
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        @CheckForNull
        V getValue(int r3) {
            return (V) DenseImmutableTable.access$400(this.this$0)[this.rowIndex][r3];
        }

        @Override // com.google.common.collect.ImmutableMap
        boolean isPartialView() {
            return true;
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        ImmutableMap<C, Integer> keyToIndex() {
            return DenseImmutableTable.access$300(this.this$0);
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap, com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap, com.google.common.collect.ImmutableMap
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    private final class RowMap extends ImmutableArrayMap<R, ImmutableMap<C, V>> {
        final /* synthetic */ DenseImmutableTable this$0;

        private RowMap(DenseImmutableTable r1) {
            this.this$0 = r1;
            super(DenseImmutableTable.access$200(r1).length);
        }

        /* synthetic */ RowMap(DenseImmutableTable r1, C06141 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        ImmutableMap<C, V> getValue(int r3) {
            return new Row(this.this$0, r3);
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        /* bridge */ /* synthetic */ Object getValue(int r1) {
            return getValue(r1);
        }

        @Override // com.google.common.collect.ImmutableMap
        boolean isPartialView() {
            return false;
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        ImmutableMap<R, Integer> keyToIndex() {
            return DenseImmutableTable.access$600(this.this$0);
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap, com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap, com.google.common.collect.ImmutableMap
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    DenseImmutableTable(ImmutableList<Table.Cell<R, C, V>> r14, ImmutableSet<R> r15, ImmutableSet<C> r16) {
        this.values = (V[][]) ((Object[][]) Array.newInstance(Object.class, new int[]{r15.size(), r16.size()}));
        this.rowKeyToIndex = Maps.indexMap(r15);
        this.columnKeyToIndex = Maps.indexMap(r16);
        this.rowCounts = new int[this.rowKeyToIndex.size()];
        this.columnCounts = new int[this.columnKeyToIndex.size()];
        int[] r1 = new int[r14.size()];
        int[] r2 = new int[r14.size()];
        int r4 = 0;
    L4:
        if (r4 >= r14.size()) goto L6;
        Table.Cell<R, C, V> r5 = r14.get(r4);
        R r6 = r5.getRowKey();
        C r7 = r5.getColumnKey();
        int r8 = ((Integer) Objects.requireNonNull(this.rowKeyToIndex.get(r6))).intValue();
        int r9 = ((Integer) Objects.requireNonNull(this.columnKeyToIndex.get(r7))).intValue();
        checkNoDuplicate(r6, r7, this.values[r8][r9], r5.getValue());
        this.values[r8][r9] = r5.getValue();
        int[] r11 = this.rowCounts;
        r11[r8] = r11[r8] + 1;
        int[] r12 = this.columnCounts;
        r12[r9] = r12[r9] + 1;
        r1[r4] = r8;
        r2[r4] = r9;
        r4 = r4 + 1;
        goto L4
    L6:
        this.cellRowIndices = r1;
        this.cellColumnIndices = r2;
        C06141 r10 = null;
        this.rowMap = new RowMap(this, r10);
        this.columnMap = new ColumnMap(this, r10);
    }

    static /* synthetic */ int[] access$200(DenseImmutableTable r1) {
        return r1.rowCounts;
    }

    static /* synthetic */ ImmutableMap access$300(DenseImmutableTable r1) {
        return r1.columnKeyToIndex;
    }

    static /* synthetic */ Object[][] access$400(DenseImmutableTable r1) {
        return r1.values;
    }

    static /* synthetic */ int[] access$500(DenseImmutableTable r1) {
        return r1.columnCounts;
    }

    static /* synthetic */ ImmutableMap access$600(DenseImmutableTable r1) {
        return r1.rowKeyToIndex;
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public ImmutableMap<C, Map<R, V>> columnMap() {
        return ImmutableMap.copyOf(this.columnMap);
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ Map columnMap() {
        return columnMap();
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.AbstractTable, com.google.common.collect.Table
    @CheckForNull
    public V get(@CheckForNull Object r5, @CheckForNull Object r6) {
        Integer r0 = this.rowKeyToIndex.get(r5);
        Integer r1 = this.columnKeyToIndex.get(r6);
        if (r0 == null) goto L7;
        if (r1 == null) goto L7;
        return this.values[r0.intValue()][r1.intValue()];
    L7:
        return null;
    }

    @Override // com.google.common.collect.RegularImmutableTable
    Table.Cell<R, C, V> getCell(int r7) {
        int r0 = this.cellRowIndices[r7];
        int r1 = this.cellColumnIndices[r7];
        return cellOf(rowKeySet().asList().get(r0), columnKeySet().asList().get(r1), Objects.requireNonNull(this.values[r0][r1]));
    }

    @Override // com.google.common.collect.RegularImmutableTable
    V getValue(int r3) {
        return (V) Objects.requireNonNull(this.values[this.cellRowIndices[r3]][this.cellColumnIndices[r3]]);
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
        return ImmutableTable.SerializedForm.create(this, this.cellRowIndices, this.cellColumnIndices);
    }
}
