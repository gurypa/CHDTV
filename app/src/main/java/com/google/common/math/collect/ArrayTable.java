package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class ArrayTable<R, C, V> extends AbstractTable<R, C, V> implements Serializable {
    private static final long serialVersionUID = 0;
    private final V[][] array;
    private final ImmutableMap<C, Integer> columnKeyToIndex;
    private final ImmutableList<C> columnList;

    @CheckForNull
    @LazyInit
    private transient ArrayTable<R, C, V>.ColumnMap columnMap;
    private final ImmutableMap<R, Integer> rowKeyToIndex;
    private final ImmutableList<R> rowList;

    @CheckForNull
    @LazyInit
    private transient ArrayTable<R, C, V>.RowMap rowMap;

    private static abstract class ArrayMap<K, V> extends Maps.IteratorBasedAbstractMap<K, V> {
        private final ImmutableMap<K, Integer> keyIndex;

        private ArrayMap(ImmutableMap<K, Integer> r1) {
            this.keyIndex = r1;
        }

        /* synthetic */ ArrayMap(ImmutableMap r1, C05981 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            return this.keyIndex.containsKey(r2);
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap
        Iterator<Map.Entry<K, V>> entryIterator() {
            final int r1 = size();
            return new C06022(this, r1);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V get(@CheckForNull Object r3) {
            Integer r0 = this.keyIndex.get(r3);
            if (r0 != null) goto L7;
            return null;
        L7:
            return getValue(r0.intValue());
        }

        Map.Entry<K, V> getEntry(final int r2) {
            Preconditions.checkElementIndex(r2, size());
            return new C06011(this, r2);
        }

        K getKey(int r2) {
            return this.keyIndex.keySet().asList().get(r2);
        }

        abstract String getKeyRole();

        @ParametricNullness
        abstract V getValue(int r1);

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            return this.keyIndex.isEmpty();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return this.keyIndex.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V put(K r5, @ParametricNullness V r6) {
            Integer r0 = this.keyIndex.get(r5);
            if (r0 == null) goto L7;
            return setValue(r0.intValue(), r6);
        L7:
            throw new IllegalArgumentException(getKeyRole() + " " + r5 + " not in " + this.keyIndex.keySet());
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V remove(@CheckForNull Object r2) {
            throw new UnsupportedOperationException();
        }

        @ParametricNullness
        abstract V setValue(int r1, @ParametricNullness V r2);

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public int size() {
            return this.keyIndex.size();
        }
    }

    private class Column extends ArrayMap<R, V> {
        final int columnIndex;
        final /* synthetic */ ArrayTable this$0;

        Column(ArrayTable r2, int r3) {
            this.this$0 = r2;
            super(ArrayTable.access$300(r2), null);
            this.columnIndex = r3;
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        String getKeyRole() {
            return "Row";
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        @CheckForNull
        V getValue(int r3) {
            return (V) this.this$0.m196at(r3, this.columnIndex);
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        @CheckForNull
        V setValue(int r3, @CheckForNull V r4) {
            return (V) this.this$0.set(r3, this.columnIndex, r4);
        }
    }

    private class ColumnMap extends ArrayMap<C, Map<R, V>> {
        final /* synthetic */ ArrayTable this$0;

        private ColumnMap(ArrayTable r2) {
            this.this$0 = r2;
            super(ArrayTable.access$600(r2), null);
        }

        /* synthetic */ ColumnMap(ArrayTable r1, C05981 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        String getKeyRole() {
            return "Column";
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        /* bridge */ /* synthetic */ Object getValue(int r1) {
            return getValue(r1);
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        Map<R, V> getValue(int r3) {
            return new Column(this.this$0, r3);
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap, java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object put(Object r1, Object r2) {
            return put(r1, (Map) r2);
        }

        @CheckForNull
        public Map<R, V> put(C r2, Map<R, V> r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        /* bridge */ /* synthetic */ Object setValue(int r1, Object r2) {
            return setValue(r1, (Map) r2);
        }

        Map<R, V> setValue(int r2, Map<R, V> r3) {
            throw new UnsupportedOperationException();
        }
    }

    private class Row extends ArrayMap<C, V> {
        final int rowIndex;
        final /* synthetic */ ArrayTable this$0;

        Row(ArrayTable r2, int r3) {
            this.this$0 = r2;
            super(ArrayTable.access$600(r2), null);
            this.rowIndex = r3;
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        String getKeyRole() {
            return "Column";
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        @CheckForNull
        V getValue(int r3) {
            return (V) this.this$0.m196at(this.rowIndex, r3);
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        @CheckForNull
        V setValue(int r3, @CheckForNull V r4) {
            return (V) this.this$0.set(this.rowIndex, r3, r4);
        }
    }

    private class RowMap extends ArrayMap<R, Map<C, V>> {
        final /* synthetic */ ArrayTable this$0;

        private RowMap(ArrayTable r2) {
            this.this$0 = r2;
            super(ArrayTable.access$300(r2), null);
        }

        /* synthetic */ RowMap(ArrayTable r1, C05981 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        String getKeyRole() {
            return "Row";
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        /* bridge */ /* synthetic */ Object getValue(int r1) {
            return getValue(r1);
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        Map<C, V> getValue(int r3) {
            return new Row(this.this$0, r3);
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap, java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object put(Object r1, Object r2) {
            return put(r1, (Map) r2);
        }

        @CheckForNull
        public Map<C, V> put(R r2, Map<C, V> r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        /* bridge */ /* synthetic */ Object setValue(int r1, Object r2) {
            return setValue(r1, (Map) r2);
        }

        Map<C, V> setValue(int r2, Map<C, V> r3) {
            throw new UnsupportedOperationException();
        }
    }

    private ArrayTable(ArrayTable<R, C, V> r7) {
        this.rowList = r7.rowList;
        this.columnList = r7.columnList;
        this.rowKeyToIndex = r7.rowKeyToIndex;
        this.columnKeyToIndex = r7.columnKeyToIndex;
        V[][] r0 = (V[][]) ((Object[][]) Array.newInstance(Object.class, new int[]{this.rowList.size(), this.columnList.size()}));
        this.array = r0;
        int r2 = 0;
    L4:
        if (r2 >= this.rowList.size()) goto L6;
        System.arraycopy(r7.array[r2], 0, r0[r2], 0, r7.array[r2].length);
        r2 = r2 + 1;
        goto L4
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ArrayTable(Table<R, C, ? extends V> r3) {
        this(r3.rowKeySet(), r3.columnKeySet());
        putAll(r3);
    }

    private ArrayTable(Iterable<? extends R> r6, Iterable<? extends C> r7) {
        this.rowList = ImmutableList.copyOf(r6);
        this.columnList = ImmutableList.copyOf(r7);
        if (this.rowList.isEmpty() != this.columnList.isEmpty()) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        this.rowKeyToIndex = Maps.indexMap(this.rowList);
        this.columnKeyToIndex = Maps.indexMap(this.columnList);
        this.array = (V[][]) ((Object[][]) Array.newInstance(Object.class, new int[]{this.rowList.size(), this.columnList.size()}));
        eraseAll();
        return;
    L5:
        r0 = false;
        goto L6
    }

    static /* synthetic */ Table.Cell access$000(ArrayTable r1, int r2) {
        return r1.getCell(r2);
    }

    static /* synthetic */ ImmutableList access$100(ArrayTable r1) {
        return r1.columnList;
    }

    static /* synthetic */ ImmutableList access$200(ArrayTable r1) {
        return r1.rowList;
    }

    static /* synthetic */ ImmutableMap access$300(ArrayTable r1) {
        return r1.rowKeyToIndex;
    }

    static /* synthetic */ ImmutableMap access$600(ArrayTable r1) {
        return r1.columnKeyToIndex;
    }

    static /* synthetic */ Object access$800(ArrayTable r1, int r2) {
        return r1.getValue(r2);
    }

    public static <R, C, V> ArrayTable<R, C, V> create(Table<R, C, ? extends V> r2) {
        if ((r2 instanceof ArrayTable) == false) goto L6;
        return new ArrayTable((ArrayTable) r2);
    L6:
        return new ArrayTable(r2);
    }

    public static <R, C, V> ArrayTable<R, C, V> create(Iterable<? extends R> r1, Iterable<? extends C> r2) {
        return new ArrayTable(r1, r2);
    }

    private Table.Cell<R, C, V> getCell(final int r2) {
        return new C05992(this, r2);
    }

    @CheckForNull
    private V getValue(int r4) {
        return m196at(r4 / this.columnList.size(), r4 % this.columnList.size());
    }

    @CheckForNull
    /* JADX INFO: renamed from: at */
    public V m196at(int r2, int r3) {
        Preconditions.checkElementIndex(r2, this.rowList.size());
        Preconditions.checkElementIndex(r3, this.columnList.size());
        return this.array[r2][r3];
    }

    @Override // com.google.common.collect.AbstractTable
    Iterator<Table.Cell<R, C, V>> cellIterator() {
        final int r1 = size();
        return new C05981(this, r1);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public Set<Table.Cell<R, C, V>> cellSet() {
        return super.cellSet();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Table
    public Map<R, V> column(C r4) {
        Preconditions.checkNotNull(r4);
        Integer r0 = this.columnKeyToIndex.get(r4);
        if (r0 != null) goto L7;
        return Collections.emptyMap();
    L7:
        return new Column(this, r0.intValue());
    }

    public ImmutableList<C> columnKeyList() {
        return this.columnList;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public ImmutableSet<C> columnKeySet() {
        return this.columnKeyToIndex.keySet();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ Set columnKeySet() {
        return columnKeySet();
    }

    @Override // com.google.common.collect.Table
    public Map<C, Map<R, V>> columnMap() {
        ArrayTable<R, C, V>.ColumnMap r0 = this.columnMap;
        if (r0 != null) goto L6;
        ArrayTable<R, C, V>.ColumnMap r1 = new ColumnMap(this, null);
        this.columnMap = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean contains(@CheckForNull Object r2, @CheckForNull Object r3) {
        if (containsRow(r2) == true) goto L5;
    L7:
        return false;
    L5:
        if (containsColumn(r3) == false) goto L7;
        return true;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean containsColumn(@CheckForNull Object r2) {
        return this.columnKeyToIndex.containsKey(r2);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean containsRow(@CheckForNull Object r2) {
        return this.rowKeyToIndex.containsKey(r2);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean containsValue(@CheckForNull Object r10) {
        V[][] r0 = this.array;
        int r1 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L12;
        V[] r4 = r0[r3];
        int r5 = r4.length;
        int r6 = 0;
    L5:
        if (r6 >= r5) goto L11;
        if (Objects.equal(r10, r4[r6]) == true) goto L8;
        r6 = r6 + 1;
        goto L5
    L8:
        return true;
    L11:
        r3 = r3 + 1;
        goto L3
    L12:
        return false;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ boolean equals(@CheckForNull Object r1) {
        return super.equals(r1);
    }

    @CheckForNull
    public V erase(@CheckForNull Object r6, @CheckForNull Object r7) {
        Integer r0 = this.rowKeyToIndex.get(r6);
        Integer r1 = this.columnKeyToIndex.get(r7);
        if (r0 == null) goto L8;
        if (r1 == null) goto L8;
        return set(r0.intValue(), r1.intValue(), null);
    L8:
        return null;
    }

    public void eraseAll() {
        V[][] r0 = this.array;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        Arrays.fill(r0[r2], null);
        r2 = r2 + 1;
        goto L3
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    @CheckForNull
    public V get(@CheckForNull Object r5, @CheckForNull Object r6) {
        Integer r0 = this.rowKeyToIndex.get(r5);
        Integer r1 = this.columnKeyToIndex.get(r6);
        if (r0 == null) goto L7;
        if (r1 == null) goto L7;
        return m196at(r0.intValue(), r1.intValue());
    L7:
        return null;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean isEmpty() {
        if (this.rowList.isEmpty() == false) goto L5;
    L8:
        return true;
    L5:
        if (this.columnList.isEmpty() == true) goto L8;
        return false;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    @CheckForNull
    public V put(R r7, C r8, @CheckForNull V r9) {
        Preconditions.checkNotNull(r7);
        Preconditions.checkNotNull(r8);
        Integer r0 = this.rowKeyToIndex.get(r7);
        boolean r1 = true;
        if (r0 == null) goto L5;
        boolean r3 = true;
    L6:
        Preconditions.checkArgument(r3, "Row %s not in %s", r7, this.rowList);
        Integer r4 = this.columnKeyToIndex.get(r8);
        if (r4 != null) goto L10;
        r1 = false;
    L10:
        Preconditions.checkArgument(r1, "Column %s not in %s", r8, this.columnList);
        return set(r0.intValue(), r4.intValue(), r9);
    L5:
        r3 = false;
        goto L6
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public void putAll(Table<? extends R, ? extends C, ? extends V> r1) {
        super.putAll(r1);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    @CheckForNull
    @Deprecated
    public V remove(@CheckForNull Object r2, @CheckForNull Object r3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Table
    public Map<C, V> row(R r4) {
        Preconditions.checkNotNull(r4);
        Integer r0 = this.rowKeyToIndex.get(r4);
        if (r0 != null) goto L7;
        return Collections.emptyMap();
    L7:
        return new Row(this, r0.intValue());
    }

    public ImmutableList<R> rowKeyList() {
        return this.rowList;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public ImmutableSet<R> rowKeySet() {
        return this.rowKeyToIndex.keySet();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ Set rowKeySet() {
        return rowKeySet();
    }

    @Override // com.google.common.collect.Table
    public Map<R, Map<C, V>> rowMap() {
        ArrayTable<R, C, V>.RowMap r0 = this.rowMap;
        if (r0 != null) goto L6;
        ArrayTable<R, C, V>.RowMap r1 = new RowMap(this, null);
        this.rowMap = r1;
        return r1;
    L6:
        return r0;
    }

    @CheckForNull
    public V set(int r3, int r4, @CheckForNull V r5) {
        Preconditions.checkElementIndex(r3, this.rowList.size());
        Preconditions.checkElementIndex(r4, this.columnList.size());
        V r0 = this.array[r3][r4];
        this.array[r3][r4] = r5;
        return r0;
    }

    @Override // com.google.common.collect.Table
    public int size() {
        return this.rowList.size() * this.columnList.size();
    }

    public V[][] toArray(Class<V> r7) {
        V[][] r0 = (V[][]) ((Object[][]) Array.newInstance(r7, new int[]{this.rowList.size(), this.columnList.size()}));
        int r1 = 0;
    L4:
        if (r1 >= this.rowList.size()) goto L6;
        System.arraycopy(this.array[r1], 0, r0[r1], 0, this.array[r1].length);
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    @Override // com.google.common.collect.AbstractTable
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public Collection<V> values() {
        return super.values();
    }

    @Override // com.google.common.collect.AbstractTable
    Iterator<V> valuesIterator() {
        final int r1 = size();
        return new C06003(this, r1);
    }
}
