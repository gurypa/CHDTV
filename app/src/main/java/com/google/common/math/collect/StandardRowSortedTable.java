package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class StandardRowSortedTable<R, C, V> extends StandardTable<R, C, V> implements RowSortedTable<R, C, V> {
    private static final long serialVersionUID = 0;

    /* JADX INFO: renamed from: com.google.common.collect.StandardRowSortedTable$1 */
    static /* synthetic */ class C07411 {
    }

    private class RowSortedMap extends StandardTable<R, C, V>.RowMap implements SortedMap<R, Map<C, V>> {
        final /* synthetic */ StandardRowSortedTable this$0;

        private RowSortedMap(StandardRowSortedTable r1) {
            this.this$0 = r1;
            super(r1);
        }

        /* synthetic */ RowSortedMap(StandardRowSortedTable r1, C07411 r2) {
            this(r1);
        }

        @Override // java.util.SortedMap
        @CheckForNull
        public Comparator<? super R> comparator() {
            return StandardRowSortedTable.access$100(this.this$0).comparator();
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        /* bridge */ /* synthetic */ Set createKeySet() {
            return createKeySet();
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        SortedSet<R> createKeySet() {
            return new Maps.SortedKeySet(this);
        }

        @Override // java.util.SortedMap
        public R firstKey() {
            return (R) StandardRowSortedTable.access$100(this.this$0).firstKey();
        }

        @Override // java.util.SortedMap
        public SortedMap<R, Map<C, V>> headMap(R r4) {
            Preconditions.checkNotNull(r4);
            return new StandardRowSortedTable(StandardRowSortedTable.access$100(this.this$0).headMap(r4), this.this$0.factory).rowMap();
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public /* bridge */ /* synthetic */ Set keySet() {
            return keySet();
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public SortedSet<R> keySet() {
            return (SortedSet) super.keySet();
        }

        @Override // java.util.SortedMap
        public R lastKey() {
            return (R) StandardRowSortedTable.access$100(this.this$0).lastKey();
        }

        @Override // java.util.SortedMap
        public SortedMap<R, Map<C, V>> subMap(R r4, R r5) {
            Preconditions.checkNotNull(r4);
            Preconditions.checkNotNull(r5);
            return new StandardRowSortedTable(StandardRowSortedTable.access$100(this.this$0).subMap(r4, r5), this.this$0.factory).rowMap();
        }

        @Override // java.util.SortedMap
        public SortedMap<R, Map<C, V>> tailMap(R r4) {
            Preconditions.checkNotNull(r4);
            return new StandardRowSortedTable(StandardRowSortedTable.access$100(this.this$0).tailMap(r4), this.this$0.factory).rowMap();
        }
    }

    StandardRowSortedTable(SortedMap<R, Map<C, V>> r1, Supplier<? extends Map<C, V>> r2) {
        super(r1, r2);
    }

    static /* synthetic */ SortedMap access$100(StandardRowSortedTable r1) {
        return r1.sortedBackingMap();
    }

    private SortedMap<R, Map<C, V>> sortedBackingMap() {
        return (SortedMap) this.backingMap;
    }

    @Override // com.google.common.collect.StandardTable
    /* bridge */ /* synthetic */ Map createRowMap() {
        return createRowMap();
    }

    @Override // com.google.common.collect.StandardTable
    SortedMap<R, Map<C, V>> createRowMap() {
        return new RowSortedMap(this, null);
    }

    @Override // com.google.common.collect.StandardTable, com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ Set rowKeySet() {
        return rowKeySet();
    }

    @Override // com.google.common.collect.StandardTable, com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public SortedSet<R> rowKeySet() {
        return (SortedSet) rowMap().keySet();
    }

    @Override // com.google.common.collect.StandardTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ Map rowMap() {
        return rowMap();
    }

    @Override // com.google.common.collect.StandardTable, com.google.common.collect.Table
    public SortedMap<R, Map<C, V>> rowMap() {
        return (SortedMap) super.rowMap();
    }
}
