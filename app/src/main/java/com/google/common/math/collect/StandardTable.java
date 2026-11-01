package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class StandardTable<R, C, V> extends AbstractTable<R, C, V> implements Serializable {
    private static final long serialVersionUID = 0;

    @GwtTransient
    final Map<R, Map<C, V>> backingMap;

    @CheckForNull
    @LazyInit
    private transient Set<C> columnKeySet;

    @CheckForNull
    @LazyInit
    private transient StandardTable<R, C, V>.ColumnMap columnMap;

    @GwtTransient
    final Supplier<? extends Map<C, V>> factory;

    @CheckForNull
    @LazyInit
    private transient Map<R, Map<C, V>> rowMap;

    /* JADX INFO: renamed from: com.google.common.collect.StandardTable$1 */
    static /* synthetic */ class C07421 {
    }

    private class CellIterator implements Iterator<Table.Cell<R, C, V>> {
        Iterator<Map.Entry<C, V>> columnIterator;

        @CheckForNull
        Map.Entry<R, Map<C, V>> rowEntry;
        final Iterator<Map.Entry<R, Map<C, V>>> rowIterator;
        final /* synthetic */ StandardTable this$0;

        private CellIterator(StandardTable r1) {
            this.this$0 = r1;
            this.rowIterator = this.this$0.backingMap.entrySet().iterator();
            this.columnIterator = Iterators.emptyModifiableIterator();
        }

        /* synthetic */ CellIterator(StandardTable r1, C07421 r2) {
            this(r1);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.rowIterator.hasNext() == false) goto L5;
        L8:
            return true;
        L5:
            if (this.columnIterator.hasNext() == true) goto L8;
            return false;
        }

        @Override // java.util.Iterator
        public Table.Cell<R, C, V> next() {
            if (this.columnIterator.hasNext() == true) goto L5;
            this.rowEntry = this.rowIterator.next();
            this.columnIterator = this.rowEntry.getValue().entrySet().iterator();
        L5:
            Objects.requireNonNull(this.rowEntry);
            Map.Entry<C, V> r0 = this.columnIterator.next();
            return Tables.immutableCell(this.rowEntry.getKey(), r0.getKey(), r0.getValue());
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ Object next() {
            return next();
        }

        @Override // java.util.Iterator
        public void remove() {
            this.columnIterator.remove();
            if (((Map) ((Map.Entry) Objects.requireNonNull(this.rowEntry)).getValue()).isEmpty() == false) goto L6;
            this.rowIterator.remove();
            this.rowEntry = null;
            return;
        }
    }

    private class Column extends Maps.ViewCachingAbstractMap<R, V> {
        final C columnKey;
        final /* synthetic */ StandardTable this$0;

        private class EntrySet extends Sets.ImprovedAbstractSet<Map.Entry<R, V>> {
            final /* synthetic */ Column this$1;

            private EntrySet(Column r1) {
                this.this$1 = r1;
            }

            /* synthetic */ EntrySet(Column r1, C07421 r2) {
                this(r1);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public void clear() {
                this.this$1.removeFromColumnIf(Predicates.alwaysTrue());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@CheckForNull Object r6) {
                if ((r6 instanceof Map.Entry) == false) goto L6;
                Map.Entry r0 = (Map.Entry) r6;
                return StandardTable.access$300(this.this$1.this$0, r0.getKey(), this.this$1.columnKey, r0.getValue());
            L6:
                return false;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean isEmpty() {
                return !this.this$1.this$0.containsColumn(this.this$1.columnKey);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<R, V>> iterator() {
                return new EntrySetIterator(this.this$1, null);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(@CheckForNull Object r6) {
                if ((r6 instanceof Map.Entry) == false) goto L6;
                Map.Entry r0 = (Map.Entry) r6;
                return StandardTable.access$400(this.this$1.this$0, r0.getKey(), this.this$1.columnKey, r0.getValue());
            L6:
                return false;
            }

            @Override // com.google.common.collect.Sets.ImprovedAbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean retainAll(Collection<?> r3) {
                return this.this$1.removeFromColumnIf(Predicates.not(Predicates.m186in(r3)));
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                int r0 = 0;
                Iterator<Map<C, V>> r1 = this.this$1.this$0.backingMap.values().iterator();
            L4:
                if (r1.hasNext() == false) goto L9;
                if (r1.next().containsKey(this.this$1.columnKey) == false) goto L4;
                r0 = r0 + 1;
                goto L4
            L9:
                return r0;
            }
        }

        private class EntrySetIterator extends AbstractIterator<Map.Entry<R, V>> {
            final Iterator<Map.Entry<R, Map<C, V>>> iterator;
            final /* synthetic */ Column this$1;

            private EntrySetIterator(Column r1) {
                this.this$1 = r1;
                this.iterator = this.this$1.this$0.backingMap.entrySet().iterator();
            }

            /* synthetic */ EntrySetIterator(Column r1, C07421 r2) {
                this(r1);
            }

            @Override // com.google.common.collect.AbstractIterator
            @CheckForNull
            protected /* bridge */ /* synthetic */ Object computeNext() {
                return computeNext();
            }

            @Override // com.google.common.collect.AbstractIterator
            @CheckForNull
            protected Map.Entry<R, V> computeNext() {
            L3:
                if (this.iterator.hasNext() == false) goto L10;
                final Map.Entry<R, Map<C, V>> r0 = this.iterator.next();
                if (r0.getValue().containsKey(this.this$1.columnKey) == false) goto L3;
                return new C1EntryImpl(this, r0);
            L10:
                return endOfData();
            }
        }

        private class KeySet extends Maps.KeySet<R, V> {
            final /* synthetic */ Column this$1;

            KeySet(Column r1) {
                this.this$1 = r1;
                super(r1);
            }

            @Override // com.google.common.collect.Maps.KeySet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@CheckForNull Object r3) {
                return this.this$1.this$0.contains(r3, this.this$1.columnKey);
            }

            @Override // com.google.common.collect.Maps.KeySet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(@CheckForNull Object r3) {
                if (this.this$1.this$0.remove(r3, this.this$1.columnKey) == null) goto L5;
                return true;
            L5:
                return false;
            }

            @Override // com.google.common.collect.Sets.ImprovedAbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean retainAll(Collection<?> r3) {
                return this.this$1.removeFromColumnIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.m186in(r3))));
            }
        }

        private class Values extends Maps.Values<R, V> {
            final /* synthetic */ Column this$1;

            Values(Column r1) {
                this.this$1 = r1;
                super(r1);
            }

            @Override // com.google.common.collect.Maps.Values, java.util.AbstractCollection, java.util.Collection
            public boolean remove(@CheckForNull Object r3) {
                if (r3 != null) goto L4;
            L6:
                return false;
            L4:
                if (this.this$1.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.equalTo(r3))) == false) goto L6;
                return true;
            }

            @Override // com.google.common.collect.Maps.Values, java.util.AbstractCollection, java.util.Collection
            public boolean removeAll(Collection<?> r3) {
                return this.this$1.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.m186in(r3)));
            }

            @Override // com.google.common.collect.Maps.Values, java.util.AbstractCollection, java.util.Collection
            public boolean retainAll(Collection<?> r3) {
                return this.this$1.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.m186in(r3))));
            }
        }

        Column(StandardTable r1, C r2) {
            this.this$0 = r1;
            this.columnKey = (C) Preconditions.checkNotNull(r2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r3) {
            return this.this$0.contains(r3, this.columnKey);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        Set<Map.Entry<R, V>> createEntrySet() {
            return new EntrySet(this, null);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        Set<R> createKeySet() {
            return new KeySet(this);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        Collection<V> createValues() {
            return new Values(this);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V get(@CheckForNull Object r3) {
            return (V) this.this$0.get(r3, this.columnKey);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V put(R r3, V r4) {
            return (V) this.this$0.put(r3, this.columnKey, r4);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V remove(@CheckForNull Object r3) {
            return (V) this.this$0.remove(r3, this.columnKey);
        }

        boolean removeFromColumnIf(Predicate<? super Map.Entry<R, V>> r7) {
            boolean r0 = false;
            Iterator<Map.Entry<R, Map<C, V>>> r1 = this.this$0.backingMap.entrySet().iterator();
        L4:
            if (r1.hasNext() == false) goto L13;
            Map.Entry<R, Map<C, V>> r2 = r1.next();
            Map<C, V> r3 = r2.getValue();
            V r4 = r3.get(this.columnKey);
            if (r4 == null) goto L4;
            if (r7.apply(Maps.immutableEntry(r2.getKey(), r4)) == false) goto L4;
            r3.remove(this.columnKey);
            r0 = true;
            if (r3.isEmpty() == false) goto L4;
            r1.remove();
            goto L4
        L13:
            return r0;
        }
    }

    private class ColumnKeyIterator extends AbstractIterator<C> {
        Iterator<Map.Entry<C, V>> entryIterator;
        final Iterator<Map<C, V>> mapIterator;
        final Map<C, V> seen;
        final /* synthetic */ StandardTable this$0;

        private ColumnKeyIterator(StandardTable r1) {
            this.this$0 = r1;
            this.seen = this.this$0.factory.get();
            this.mapIterator = this.this$0.backingMap.values().iterator();
            this.entryIterator = Iterators.emptyIterator();
        }

        /* synthetic */ ColumnKeyIterator(StandardTable r1, C07421 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.AbstractIterator
        @CheckForNull
        protected C computeNext() {
        L3:
            if (this.entryIterator.hasNext() == true) goto L4;
            if (this.mapIterator.hasNext() == false) goto L13;
            this.entryIterator = this.mapIterator.next().entrySet().iterator();
            goto L3
        L13:
            return endOfData();
        L4:
            Map.Entry<C, V> r0 = this.entryIterator.next();
            if (this.seen.containsKey(r0.getKey()) == true) goto L3;
            this.seen.put(r0.getKey(), r0.getValue());
            return r0.getKey();
        }
    }

    private class ColumnKeySet extends StandardTable<R, C, V>.TableSet<C> {
        final /* synthetic */ StandardTable this$0;

        private ColumnKeySet(StandardTable r2) {
            this.this$0 = r2;
            super(r2, null);
        }

        /* synthetic */ ColumnKeySet(StandardTable r1, C07421 r2) {
            this(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r2) {
            return this.this$0.containsColumn(r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<C> iterator() {
            return this.this$0.createColumnKeyIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r5) {
            if (r5 != null) goto L5;
            return false;
        L5:
            boolean r0 = false;
            Iterator<Map<C, V>> r1 = this.this$0.backingMap.values().iterator();
        L7:
            if (r1.hasNext() == false) goto L14;
            Map<C, V> r2 = r1.next();
            if (r2.keySet().remove(r5) == false) goto L7;
            r0 = true;
            if (r2.isEmpty() == false) goto L7;
            r1.remove();
            goto L7
        L14:
            return r0;
        }

        @Override // com.google.common.collect.Sets.ImprovedAbstractSet, java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> r5) {
            Preconditions.checkNotNull(r5);
            boolean r0 = false;
            Iterator<Map<C, V>> r1 = this.this$0.backingMap.values().iterator();
        L4:
            if (r1.hasNext() == false) goto L11;
            Map<C, V> r2 = r1.next();
            if (Iterators.removeAll(r2.keySet().iterator(), r5) == false) goto L4;
            r0 = true;
            if (r2.isEmpty() == false) goto L4;
            r1.remove();
            goto L4
        L11:
            return r0;
        }

        @Override // com.google.common.collect.Sets.ImprovedAbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> r5) {
            Preconditions.checkNotNull(r5);
            boolean r0 = false;
            Iterator<Map<C, V>> r1 = this.this$0.backingMap.values().iterator();
        L4:
            if (r1.hasNext() == false) goto L11;
            Map<C, V> r2 = r1.next();
            if (r2.keySet().retainAll(r5) == false) goto L4;
            r0 = true;
            if (r2.isEmpty() == false) goto L4;
            r1.remove();
            goto L4
        L11:
            return r0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Iterators.size(iterator());
        }
    }

    private class ColumnMap extends Maps.ViewCachingAbstractMap<C, Map<R, V>> {
        final /* synthetic */ StandardTable this$0;

        private final class ColumnMapEntrySet extends StandardTable<R, C, V>.TableSet<Map.Entry<C, Map<R, V>>> {
            final /* synthetic */ ColumnMap this$1;

            private ColumnMapEntrySet(ColumnMap r2) {
                this.this$1 = r2;
                super(r2.this$0, null);
            }

            /* synthetic */ ColumnMapEntrySet(ColumnMap r1, C07421 r2) {
                this(r1);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@CheckForNull Object r4) {
                if ((r4 instanceof Map.Entry) == false) goto L8;
                Map.Entry r0 = (Map.Entry) r4;
                if (this.this$1.this$0.containsColumn(r0.getKey()) == true) goto L7;
                return false;
            L7:
                return ((Map) Objects.requireNonNull(this.this$1.get(r0.getKey()))).equals(r0.getValue());
            L8:
                return false;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<C, Map<R, V>>> iterator() {
                return Maps.asMapEntryIterator(this.this$1.this$0.columnKeySet(), new C07431(this));
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(@CheckForNull Object r4) {
                if (contains(r4) == true) goto L5;
                return false;
            L5:
                if ((r4 instanceof Map.Entry) == false) goto L10;
                StandardTable.access$1000(this.this$1.this$0, ((Map.Entry) r4).getKey());
                return true;
            L10:
                return false;
            }

            @Override // com.google.common.collect.Sets.ImprovedAbstractSet, java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean removeAll(Collection<?> r2) {
                Preconditions.checkNotNull(r2);
                return Sets.removeAllImpl(this, r2.iterator());
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.Sets.ImprovedAbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean retainAll(Collection<?> r5) {
                Preconditions.checkNotNull(r5);
                boolean r0 = false;
                Iterator r1 = Lists.newArrayList(this.this$1.this$0.columnKeySet().iterator()).iterator();
            L4:
                if (r1.hasNext() == false) goto L9;
                Object r2 = r1.next();
                if (r5.contains(Maps.immutableEntry(r2, this.this$1.this$0.column(r2))) == true) goto L4;
                StandardTable.access$1000(this.this$1.this$0, r2);
                r0 = true;
                goto L4
            L9:
                return r0;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return this.this$1.this$0.columnKeySet().size();
            }
        }

        private class ColumnMapValues extends Maps.Values<C, Map<R, V>> {
            final /* synthetic */ ColumnMap this$1;

            ColumnMapValues(ColumnMap r1) {
                this.this$1 = r1;
                super(r1);
            }

            @Override // com.google.common.collect.Maps.Values, java.util.AbstractCollection, java.util.Collection
            public boolean remove(@CheckForNull Object r4) {
                Iterator<Map.Entry<C, Map<R, V>>> r0 = this.this$1.entrySet().iterator();
            L4:
                if (r0.hasNext() == false) goto L10;
                Map.Entry<C, Map<R, V>> r1 = r0.next();
                if (r1.getValue().equals(r4) == false) goto L4;
                StandardTable.access$1000(this.this$1.this$0, r1.getKey());
                return true;
            L10:
                return false;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.Maps.Values, java.util.AbstractCollection, java.util.Collection
            public boolean removeAll(Collection<?> r5) {
                Preconditions.checkNotNull(r5);
                boolean r0 = false;
                Iterator r1 = Lists.newArrayList(this.this$1.this$0.columnKeySet().iterator()).iterator();
            L4:
                if (r1.hasNext() == false) goto L9;
                Object r2 = r1.next();
                if (r5.contains(this.this$1.this$0.column(r2)) == false) goto L4;
                StandardTable.access$1000(this.this$1.this$0, r2);
                r0 = true;
                goto L4
            L9:
                return r0;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.Maps.Values, java.util.AbstractCollection, java.util.Collection
            public boolean retainAll(Collection<?> r5) {
                Preconditions.checkNotNull(r5);
                boolean r0 = false;
                Iterator r1 = Lists.newArrayList(this.this$1.this$0.columnKeySet().iterator()).iterator();
            L4:
                if (r1.hasNext() == false) goto L9;
                Object r2 = r1.next();
                if (r5.contains(this.this$1.this$0.column(r2)) == true) goto L4;
                StandardTable.access$1000(this.this$1.this$0, r2);
                r0 = true;
                goto L4
            L9:
                return r0;
            }
        }

        private ColumnMap(StandardTable r1) {
            this.this$0 = r1;
        }

        /* synthetic */ ColumnMap(StandardTable r1, C07421 r2) {
            this(r1);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            return this.this$0.containsColumn(r2);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        public Set<Map.Entry<C, Map<R, V>>> createEntrySet() {
            return new ColumnMapEntrySet(this, null);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        Collection<Map<R, V>> createValues() {
            return new ColumnMapValues(this);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object get(@CheckForNull Object r1) {
            return get(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public Map<R, V> get(@CheckForNull Object r3) {
            if (this.this$0.containsColumn(r3) == true) goto L7;
            return null;
        L7:
            return this.this$0.column(Objects.requireNonNull(r3));
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public Set<C> keySet() {
            return this.this$0.columnKeySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object remove(@CheckForNull Object r1) {
            return remove(r1);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public Map<R, V> remove(@CheckForNull Object r2) {
            if (this.this$0.containsColumn(r2) == true) goto L7;
            return null;
        L7:
            return StandardTable.access$1000(this.this$0, r2);
        }
    }

    class Row extends Maps.IteratorBasedAbstractMap<C, V> {

        @CheckForNull
        Map<C, V> backingRowMap;
        final R rowKey;
        final /* synthetic */ StandardTable this$0;

        Row(StandardTable r2, R r3) {
            this.this$0 = r2;
            this.rowKey = (R) Preconditions.checkNotNull(r3);
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public void clear() {
            updateBackingRowMapField();
            if (this.backingRowMap == null) goto L5;
            this.backingRowMap.clear();
        L5:
            maintainEmptyInvariant();
        }

        @CheckForNull
        Map<C, V> computeBackingRowMap() {
            return this.this$0.backingMap.get(this.rowKey);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            updateBackingRowMapField();
            if (r2 != null) goto L5;
        L9:
            return false;
        L5:
            if (this.backingRowMap == null) goto L9;
            if (Maps.safeContainsKey(this.backingRowMap, r2) == false) goto L9;
            return true;
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap
        Iterator<Map.Entry<C, V>> entryIterator() {
            updateBackingRowMapField();
            if (this.backingRowMap == null) goto L5;
            final Iterator<Map.Entry<C, V>> r0 = this.backingRowMap.entrySet().iterator();
            return new C07441(this, r0);
        L5:
            return Iterators.emptyModifiableIterator();
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V get(@CheckForNull Object r2) {
            updateBackingRowMapField();
            if (r2 != null) goto L5;
        L7:
            return null;
        L5:
            if (this.backingRowMap == null) goto L7;
            return (V) Maps.safeGet(this.backingRowMap, r2);
        }

        void maintainEmptyInvariant() {
            updateBackingRowMapField();
            if (this.backingRowMap != null) goto L5;
            return;
        L5:
            if (this.backingRowMap.isEmpty() == false) goto L9;
            this.this$0.backingMap.remove(this.rowKey);
            this.backingRowMap = null;
            return;
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V put(C r3, V r4) {
            Preconditions.checkNotNull(r3);
            Preconditions.checkNotNull(r4);
            if (this.backingRowMap == null) goto L9;
            if (this.backingRowMap.isEmpty() == true) goto L9;
            return this.backingRowMap.put(r3, r4);
        L9:
            return (V) this.this$0.put(this.rowKey, r3, r4);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V remove(@CheckForNull Object r2) {
            updateBackingRowMapField();
            if (this.backingRowMap != null) goto L6;
            return null;
        L6:
            V r0 = (V) Maps.safeRemove(this.backingRowMap, r2);
            maintainEmptyInvariant();
            return r0;
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public int size() {
            updateBackingRowMapField();
            if (this.backingRowMap != null) goto L6;
            return 0;
        L6:
            return this.backingRowMap.size();
        }

        final void updateBackingRowMapField() {
            if (this.backingRowMap != null) goto L5;
        L8:
            this.backingRowMap = computeBackingRowMap();
            return;
        L5:
            if (this.backingRowMap.isEmpty() == true) goto L7;
            return;
        L7:
            if (this.this$0.backingMap.containsKey(this.rowKey) == true) goto L8;
        }

        Map.Entry<C, V> wrapEntry(final Map.Entry<C, V> r2) {
            return new C07452(this, r2);
        }
    }

    class RowMap extends Maps.ViewCachingAbstractMap<R, Map<C, V>> {
        final /* synthetic */ StandardTable this$0;

        private final class EntrySet extends StandardTable<R, C, V>.TableSet<Map.Entry<R, Map<C, V>>> {
            final /* synthetic */ RowMap this$1;

            private EntrySet(RowMap r2) {
                this.this$1 = r2;
                super(r2.this$0, null);
            }

            /* synthetic */ EntrySet(RowMap r1, C07421 r2) {
                this(r1);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@CheckForNull Object r4) {
                if ((r4 instanceof Map.Entry) == false) goto L13;
                Map.Entry r0 = (Map.Entry) r4;
                if (r0.getKey() != null) goto L7;
                return false;
            L7:
                if ((r0.getValue() instanceof Map) == true) goto L9;
                return false;
            L9:
                if (Collections2.safeContains(this.this$1.this$0.backingMap.entrySet(), r0) == false) goto L16;
                return true;
            L16:
                return false;
            L13:
                return false;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<R, Map<C, V>>> iterator() {
                return Maps.asMapEntryIterator(this.this$1.this$0.backingMap.keySet(), new C07461(this));
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(@CheckForNull Object r4) {
                if ((r4 instanceof Map.Entry) == false) goto L13;
                Map.Entry r0 = (Map.Entry) r4;
                if (r0.getKey() != null) goto L7;
                return false;
            L7:
                if ((r0.getValue() instanceof Map) == true) goto L9;
                return false;
            L9:
                if (this.this$1.this$0.backingMap.entrySet().remove(r0) == false) goto L16;
                return true;
            L16:
                return false;
            L13:
                return false;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return this.this$1.this$0.backingMap.size();
            }
        }

        RowMap(StandardTable r1) {
            this.this$0 = r1;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            return this.this$0.containsRow(r2);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        protected Set<Map.Entry<R, Map<C, V>>> createEntrySet() {
            return new EntrySet(this, null);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object get(@CheckForNull Object r1) {
            return get(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public Map<C, V> get(@CheckForNull Object r3) {
            if (this.this$0.containsRow(r3) == true) goto L7;
            return null;
        L7:
            return this.this$0.row(Objects.requireNonNull(r3));
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object remove(@CheckForNull Object r1) {
            return remove(r1);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public Map<C, V> remove(@CheckForNull Object r2) {
            if (r2 != null) goto L5;
            return null;
        L5:
            return this.this$0.backingMap.remove(r2);
        }
    }

    private abstract class TableSet<T> extends Sets.ImprovedAbstractSet<T> {
        final /* synthetic */ StandardTable this$0;

        private TableSet(StandardTable r1) {
            this.this$0 = r1;
        }

        /* synthetic */ TableSet(StandardTable r1, C07421 r2) {
            this(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.this$0.backingMap.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return this.this$0.backingMap.isEmpty();
        }
    }

    StandardTable(Map<R, Map<C, V>> r1, Supplier<? extends Map<C, V>> r2) {
        this.backingMap = r1;
        this.factory = r2;
    }

    static /* synthetic */ Map access$1000(StandardTable r1, Object r2) {
        return r1.removeColumn(r2);
    }

    static /* synthetic */ boolean access$300(StandardTable r1, Object r2, Object r3, Object r4) {
        return r1.containsMapping(r2, r3, r4);
    }

    static /* synthetic */ boolean access$400(StandardTable r1, Object r2, Object r3, Object r4) {
        return r1.removeMapping(r2, r3, r4);
    }

    private boolean containsMapping(@CheckForNull Object r2, @CheckForNull Object r3, @CheckForNull Object r4) {
        if (r4 != null) goto L4;
    L6:
        return false;
    L4:
        if (r4.equals(get(r2, r3)) == false) goto L6;
        return true;
    }

    private Map<C, V> getOrCreate(R r3) {
        Map<C, V> r0 = this.backingMap.get(r3);
        if (r0 != null) goto L6;
        Map<C, V> r1 = this.factory.get();
        this.backingMap.put(r3, r1);
        return r1;
    L6:
        return r0;
    }

    private Map<R, V> removeColumn(@CheckForNull Object r6) {
        LinkedHashMap r0 = new LinkedHashMap();
        Iterator<Map.Entry<R, Map<C, V>>> r1 = this.backingMap.entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L11;
        Map.Entry<R, Map<C, V>> r2 = r1.next();
        V r3 = r2.getValue().remove(r6);
        if (r3 == null) goto L4;
        r0.put(r2.getKey(), r3);
        if (r2.getValue().isEmpty() == false) goto L4;
        r1.remove();
        goto L4
    L11:
        return r0;
    }

    private boolean removeMapping(@CheckForNull Object r2, @CheckForNull Object r3, @CheckForNull Object r4) {
        if (containsMapping(r2, r3, r4) == false) goto L6;
        remove(r2, r3);
        return true;
    L6:
        return false;
    }

    @Override // com.google.common.collect.AbstractTable
    Iterator<Table.Cell<R, C, V>> cellIterator() {
        return new CellIterator(this, null);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public Set<Table.Cell<R, C, V>> cellSet() {
        return super.cellSet();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public void clear() {
        this.backingMap.clear();
    }

    @Override // com.google.common.collect.Table
    public Map<R, V> column(C r2) {
        return new Column(this, r2);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public Set<C> columnKeySet() {
        Set<C> r0 = this.columnKeySet;
        if (r0 != null) goto L6;
        ColumnKeySet r1 = new ColumnKeySet(this, null);
        this.columnKeySet = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.Table
    public Map<C, Map<R, V>> columnMap() {
        StandardTable<R, C, V>.ColumnMap r0 = this.columnMap;
        if (r0 != null) goto L6;
        StandardTable<R, C, V>.ColumnMap r1 = new ColumnMap(this, null);
        this.columnMap = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean contains(@CheckForNull Object r2, @CheckForNull Object r3) {
        if (r2 == null) goto L7;
        if (r3 == null) goto L7;
        if (super.contains(r2, r3) == false) goto L7;
        return true;
    L7:
        return false;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean containsColumn(@CheckForNull Object r5) {
        if (r5 != null) goto L5;
        return false;
    L5:
        Iterator<Map<C, V>> r1 = this.backingMap.values().iterator();
    L7:
        if (r1.hasNext() == false) goto L13;
        if (Maps.safeContainsKey(r1.next(), r5) == false) goto L7;
        return true;
    L13:
        return false;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean containsRow(@CheckForNull Object r2) {
        if (r2 != null) goto L4;
    L6:
        return false;
    L4:
        if (Maps.safeContainsKey(this.backingMap, r2) == false) goto L6;
        return true;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean containsValue(@CheckForNull Object r2) {
        if (r2 != null) goto L4;
    L6:
        return false;
    L4:
        if (super.containsValue(r2) == false) goto L6;
        return true;
    }

    Iterator<C> createColumnKeyIterator() {
        return new ColumnKeyIterator(this, null);
    }

    Map<R, Map<C, V>> createRowMap() {
        return new RowMap(this);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    @CheckForNull
    public V get(@CheckForNull Object r2, @CheckForNull Object r3) {
        if (r2 == null) goto L6;
        if (r3 == null) goto L6;
        return (V) super.get(r2, r3);
    L6:
        return null;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean isEmpty() {
        return this.backingMap.isEmpty();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    @CheckForNull
    public V put(R r2, C r3, V r4) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        return getOrCreate(r2).put(r3, r4);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    @CheckForNull
    public V remove(@CheckForNull Object r4, @CheckForNull Object r5) {
        if (r4 == null) goto L13;
        if (r5 == null) goto L13;
        Map r1 = (Map) Maps.safeGet(this.backingMap, r4);
        if (r1 != null) goto L9;
        return null;
    L9:
        V r0 = (V) r1.remove(r5);
        if (r1.isEmpty() == false) goto L12;
        this.backingMap.remove(r4);
    L12:
        return r0;
    L13:
        return null;
    }

    @Override // com.google.common.collect.Table
    public Map<C, V> row(R r2) {
        return new Row(this, r2);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public Set<R> rowKeySet() {
        return rowMap().keySet();
    }

    @Override // com.google.common.collect.Table
    public Map<R, Map<C, V>> rowMap() {
        Map<R, Map<C, V>> r0 = this.rowMap;
        if (r0 != null) goto L6;
        Map<R, Map<C, V>> r1 = createRowMap();
        this.rowMap = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.Table
    public int size() {
        int r0 = 0;
        Iterator<Map<C, V>> r1 = this.backingMap.values().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0 = r0 + r1.next().size();
        goto L4
    L6:
        return r0;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public Collection<V> values() {
        return super.values();
    }
}
