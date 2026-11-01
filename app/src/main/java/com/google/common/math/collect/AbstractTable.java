package com.google.common.collect;

import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractTable<R, C, V> implements Table<R, C, V> {

    @CheckForNull
    @LazyInit
    private transient Set<Table.Cell<R, C, V>> cellSet;

    @CheckForNull
    @LazyInit
    private transient Collection<V> values;

    class CellSet extends AbstractSet<Table.Cell<R, C, V>> {
        final /* synthetic */ AbstractTable this$0;

        CellSet(AbstractTable r1) {
            this.this$0 = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r7) {
            if ((r7 instanceof Table.Cell) == false) goto L11;
            Table.Cell r0 = (Table.Cell) r7;
            Map r2 = (Map) Maps.safeGet(this.this$0.rowMap(), r0.getRowKey());
            if (r2 != null) goto L7;
            return false;
        L7:
            if (Collections2.safeContains(r2.entrySet(), Maps.immutableEntry(r0.getColumnKey(), r0.getValue())) == false) goto L13;
            return true;
        L13:
            return false;
        L11:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Table.Cell<R, C, V>> iterator() {
            return this.this$0.cellIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r7) {
            if ((r7 instanceof Table.Cell) == false) goto L11;
            Table.Cell r0 = (Table.Cell) r7;
            Map r2 = (Map) Maps.safeGet(this.this$0.rowMap(), r0.getRowKey());
            if (r2 != null) goto L7;
            return false;
        L7:
            if (Collections2.safeRemove(r2.entrySet(), Maps.immutableEntry(r0.getColumnKey(), r0.getValue())) == false) goto L13;
            return true;
        L13:
            return false;
        L11:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.size();
        }
    }

    class Values extends AbstractCollection<V> {
        final /* synthetic */ AbstractTable this$0;

        Values(AbstractTable r1) {
            this.this$0 = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(@CheckForNull Object r2) {
            return this.this$0.containsValue(r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return this.this$0.valuesIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.this$0.size();
        }
    }

    AbstractTable() {
    }

    abstract Iterator<Table.Cell<R, C, V>> cellIterator();

    @Override // com.google.common.collect.Table
    public Set<Table.Cell<R, C, V>> cellSet() {
        Set<Table.Cell<R, C, V>> r0 = this.cellSet;
        if (r0 != null) goto L6;
        Set<Table.Cell<R, C, V>> r1 = createCellSet();
        this.cellSet = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.Table
    public void clear() {
        Iterators.clear(cellSet().iterator());
    }

    @Override // com.google.common.collect.Table
    public Set<C> columnKeySet() {
        return columnMap().keySet();
    }

    @Override // com.google.common.collect.Table
    public boolean contains(@CheckForNull Object r3, @CheckForNull Object r4) {
        Map r0 = (Map) Maps.safeGet(rowMap(), r3);
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (Maps.safeContainsKey(r0, r4) == false) goto L7;
        return true;
    }

    @Override // com.google.common.collect.Table
    public boolean containsColumn(@CheckForNull Object r2) {
        return Maps.safeContainsKey(columnMap(), r2);
    }

    @Override // com.google.common.collect.Table
    public boolean containsRow(@CheckForNull Object r2) {
        return Maps.safeContainsKey(rowMap(), r2);
    }

    @Override // com.google.common.collect.Table
    public boolean containsValue(@CheckForNull Object r4) {
        Iterator<Map<C, V>> r0 = rowMap().values().iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (r0.next().containsValue(r4) == false) goto L4;
        return true;
    L10:
        return false;
    }

    Set<Table.Cell<R, C, V>> createCellSet() {
        return new CellSet(this);
    }

    Collection<V> createValues() {
        return new Values(this);
    }

    @Override // com.google.common.collect.Table
    public boolean equals(@CheckForNull Object r2) {
        return Tables.equalsImpl(this, r2);
    }

    @Override // com.google.common.collect.Table
    @CheckForNull
    public V get(@CheckForNull Object r3, @CheckForNull Object r4) {
        Map r0 = (Map) Maps.safeGet(rowMap(), r3);
        if (r0 != null) goto L6;
        return null;
    L6:
        return (V) Maps.safeGet(r0, r4);
    }

    @Override // com.google.common.collect.Table
    public int hashCode() {
        return cellSet().hashCode();
    }

    @Override // com.google.common.collect.Table
    public boolean isEmpty() {
        if (size() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // com.google.common.collect.Table
    @CheckForNull
    public V put(@ParametricNullness R r2, @ParametricNullness C r3, @ParametricNullness V r4) {
        return row(r2).put(r3, r4);
    }

    @Override // com.google.common.collect.Table
    public void putAll(Table<? extends R, ? extends C, ? extends V> r6) {
        Iterator<Table.Cell<? extends R, ? extends C, ? extends V>> r0 = r6.cellSet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Table.Cell<? extends R, ? extends C, ? extends V> r1 = r0.next();
        put(r1.getRowKey(), r1.getColumnKey(), r1.getValue());
        goto L4
    }

    @Override // com.google.common.collect.Table
    @CheckForNull
    public V remove(@CheckForNull Object r3, @CheckForNull Object r4) {
        Map r0 = (Map) Maps.safeGet(rowMap(), r3);
        if (r0 != null) goto L6;
        return null;
    L6:
        return (V) Maps.safeRemove(r0, r4);
    }

    @Override // com.google.common.collect.Table
    public Set<R> rowKeySet() {
        return rowMap().keySet();
    }

    public String toString() {
        return rowMap().toString();
    }

    @Override // com.google.common.collect.Table
    public Collection<V> values() {
        Collection<V> r0 = this.values;
        if (r0 != null) goto L6;
        Collection<V> r1 = createValues();
        this.values = r1;
        return r1;
    L6:
        return r0;
    }

    Iterator<V> valuesIterator() {
        final Iterator<Table.Cell<R, C, V>> r1 = cellSet().iterator();
        return new C05971(this, r1);
    }
}
