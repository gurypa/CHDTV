package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractMapBasedMultimap<K, V> extends AbstractMultimap<K, V> implements Serializable {
    private static final long serialVersionUID = 2447537837011683357L;
    private transient Map<K, Collection<V>> map;
    private transient int totalSize;

    private class AsMap extends Maps.ViewCachingAbstractMap<K, Collection<V>> {
        final transient Map<K, Collection<V>> submap;
        final /* synthetic */ AbstractMapBasedMultimap this$0;

        class AsMapEntries extends Maps.EntrySet<K, Collection<V>> {
            final /* synthetic */ AsMap this$1;

            AsMapEntries(AsMap r1) {
                this.this$1 = r1;
            }

            @Override // com.google.common.collect.Maps.EntrySet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@CheckForNull Object r2) {
                return Collections2.safeContains(this.this$1.submap.entrySet(), r2);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return new AsMapIterator(this.this$1);
            }

            @Override // com.google.common.collect.Maps.EntrySet
            Map<K, Collection<V>> map() {
                return this.this$1;
            }

            @Override // com.google.common.collect.Maps.EntrySet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(@CheckForNull Object r4) {
                if (contains(r4) == true) goto L6;
                return false;
            L6:
                Map.Entry r0 = (Map.Entry) Objects.requireNonNull((Map.Entry) r4);
                AbstractMapBasedMultimap.access$300(this.this$1.this$0, r0.getKey());
                return true;
            }
        }

        class AsMapIterator implements Iterator<Map.Entry<K, Collection<V>>> {

            @CheckForNull
            Collection<V> collection;
            final Iterator<Map.Entry<K, Collection<V>>> delegateIterator;
            final /* synthetic */ AsMap this$1;

            AsMapIterator(AsMap r2) {
                this.this$1 = r2;
                this.delegateIterator = this.this$1.submap.entrySet().iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.delegateIterator.hasNext();
            }

            @Override // java.util.Iterator
            public /* bridge */ /* synthetic */ Object next() {
                return next();
            }

            @Override // java.util.Iterator
            public Map.Entry<K, Collection<V>> next() {
                Map.Entry<K, Collection<V>> r0 = this.delegateIterator.next();
                this.collection = r0.getValue();
                return this.this$1.wrapEntry(r0);
            }

            @Override // java.util.Iterator
            public void remove() {
                if (this.collection == null) goto L5;
                boolean r0 = true;
            L6:
                Preconditions.checkState(r0, "no calls to next() since the last call to remove()");
                this.delegateIterator.remove();
                AbstractMapBasedMultimap.access$220(this.this$1.this$0, this.collection.size());
                this.collection.clear();
                this.collection = null;
                return;
            L5:
                r0 = false;
                goto L6
            }
        }

        AsMap(AbstractMapBasedMultimap r1, Map<K, Collection<V>> r2) {
            this.this$0 = r1;
            this.submap = r2;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            if (this.submap != AbstractMapBasedMultimap.access$000(this.this$0)) goto L5;
            this.this$0.clear();
            return;
        L5:
            Iterators.clear(new AsMapIterator(this));
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            return Maps.safeContainsKey(this.submap, r2);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
            return new AsMapEntries(this);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean equals(@CheckForNull Object r2) {
            if (this != r2) goto L4;
        L7:
            return true;
        L4:
            if (this.submap.equals(r2) == true) goto L7;
            return false;
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object get(@CheckForNull Object r1) {
            return get(r1);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public Collection<V> get(@CheckForNull Object r4) {
            Collection<V> r0 = (Collection) Maps.safeGet(this.submap, r4);
            if (r0 != null) goto L7;
            return null;
        L7:
            return this.this$0.wrapCollection(r4, r0);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int hashCode() {
            return this.submap.hashCode();
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return this.this$0.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object remove(@CheckForNull Object r1) {
            return remove(r1);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public Collection<V> remove(@CheckForNull Object r5) {
            Collection<V> r0 = this.submap.remove(r5);
            if (r0 != null) goto L6;
            return null;
        L6:
            Collection<V> r1 = this.this$0.createCollection();
            r1.addAll(r0);
            AbstractMapBasedMultimap.access$220(this.this$0, r0.size());
            r0.clear();
            return r1;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return this.submap.size();
        }

        @Override // java.util.AbstractMap
        public String toString() {
            return this.submap.toString();
        }

        Map.Entry<K, Collection<V>> wrapEntry(Map.Entry<K, Collection<V>> r4) {
            K r0 = r4.getKey();
            return Maps.immutableEntry(r0, this.this$0.wrapCollection(r0, r4.getValue()));
        }
    }

    private abstract class Itr<T> implements Iterator<T> {

        @CheckForNull
        Collection<V> collection;

        @CheckForNull
        K key;
        final Iterator<Map.Entry<K, Collection<V>>> keyIterator;
        final /* synthetic */ AbstractMapBasedMultimap this$0;
        Iterator<V> valueIterator;

        Itr(AbstractMapBasedMultimap r1) {
            this.this$0 = r1;
            this.keyIterator = AbstractMapBasedMultimap.access$000(r1).entrySet().iterator();
            this.key = null;
            this.collection = null;
            this.valueIterator = Iterators.emptyModifiableIterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.keyIterator.hasNext() == false) goto L5;
        L8:
            return true;
        L5:
            if (this.valueIterator.hasNext() == true) goto L8;
            return false;
        }

        @Override // java.util.Iterator
        @ParametricNullness
        public T next() {
            if (this.valueIterator.hasNext() == true) goto L6;
            Map.Entry<K, Collection<V>> r0 = this.keyIterator.next();
            this.key = r0.getKey();
            this.collection = r0.getValue();
            this.valueIterator = this.collection.iterator();
        L6:
            return output(NullnessCasts.uncheckedCastNullableTToT(this.key), this.valueIterator.next());
        }

        abstract T output(@ParametricNullness K r1, @ParametricNullness V r2);

        @Override // java.util.Iterator
        public void remove() {
            this.valueIterator.remove();
            if (((Collection) Objects.requireNonNull(this.collection)).isEmpty() == false) goto L5;
            this.keyIterator.remove();
        L5:
            AbstractMapBasedMultimap.access$210(this.this$0);
        }
    }

    private class KeySet extends Maps.KeySet<K, Collection<V>> {
        final /* synthetic */ AbstractMapBasedMultimap this$0;

        KeySet(AbstractMapBasedMultimap r1, Map<K, Collection<V>> r2) {
            this.this$0 = r1;
            super(r2);
        }

        @Override // com.google.common.collect.Maps.KeySet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Iterators.clear(iterator());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> r2) {
            return map().keySet().containsAll(r2);
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
        public boolean equals(@CheckForNull Object r2) {
            if (this != r2) goto L4;
        L7:
            return true;
        L4:
            if (map().keySet().equals(r2) == true) goto L7;
            return false;
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
        public int hashCode() {
            return map().keySet().hashCode();
        }

        @Override // com.google.common.collect.Maps.KeySet, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            final Iterator<Map.Entry<K, Collection<V>>> r0 = map().entrySet().iterator();
            return new C05931(this, r0);
        }

        @Override // com.google.common.collect.Maps.KeySet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r4) {
            int r0 = 0;
            Collection<V> r1 = map().remove(r4);
            if (r1 == null) goto L5;
            r0 = r1.size();
            r1.clear();
            AbstractMapBasedMultimap.access$220(this.this$0, r0);
        L5:
            if (r0 <= 0) goto L7;
            return true;
        L7:
            return false;
        }
    }

    private final class NavigableAsMap extends AbstractMapBasedMultimap<K, V>.SortedAsMap implements NavigableMap<K, Collection<V>> {
        final /* synthetic */ AbstractMapBasedMultimap this$0;

        NavigableAsMap(AbstractMapBasedMultimap r1, NavigableMap<K, Collection<V>> r2) {
            this.this$0 = r1;
            super(r1, r2);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, Collection<V>> ceilingEntry(@ParametricNullness K r3) {
            Map.Entry<K, Collection<V>> r0 = sortedMap().ceilingEntry(r3);
            if (r0 != null) goto L6;
            return null;
        L6:
            return wrapEntry(r0);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K ceilingKey(@ParametricNullness K r2) {
            return sortedMap().ceilingKey(r2);
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedAsMap, com.google.common.collect.Maps.ViewCachingAbstractMap
        NavigableSet<K> createKeySet() {
            return new NavigableKeySet(this.this$0, sortedMap());
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedAsMap, com.google.common.collect.Maps.ViewCachingAbstractMap
        /* bridge */ /* synthetic */ Set createKeySet() {
            return createKeySet();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedAsMap, com.google.common.collect.Maps.ViewCachingAbstractMap
        /* bridge */ /* synthetic */ SortedSet createKeySet() {
            return createKeySet();
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> descendingKeySet() {
            return descendingMap().navigableKeySet();
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, Collection<V>> descendingMap() {
            return new NavigableAsMap(this.this$0, sortedMap().descendingMap());
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, Collection<V>> firstEntry() {
            Map.Entry<K, Collection<V>> r0 = sortedMap().firstEntry();
            if (r0 != null) goto L6;
            return null;
        L6:
            return wrapEntry(r0);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, Collection<V>> floorEntry(@ParametricNullness K r3) {
            Map.Entry<K, Collection<V>> r0 = sortedMap().floorEntry(r3);
            if (r0 != null) goto L6;
            return null;
        L6:
            return wrapEntry(r0);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K floorKey(@ParametricNullness K r2) {
            return sortedMap().floorKey(r2);
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedAsMap, java.util.SortedMap, java.util.NavigableMap
        public NavigableMap<K, Collection<V>> headMap(@ParametricNullness K r2) {
            return headMap(r2, false);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, Collection<V>> headMap(@ParametricNullness K r4, boolean r5) {
            return new NavigableAsMap(this.this$0, sortedMap().headMap(r4, r5));
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedAsMap, java.util.SortedMap, java.util.NavigableMap
        public /* bridge */ /* synthetic */ SortedMap headMap(@ParametricNullness Object r1) {
            return headMap(r1);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, Collection<V>> higherEntry(@ParametricNullness K r3) {
            Map.Entry<K, Collection<V>> r0 = sortedMap().higherEntry(r3);
            if (r0 != null) goto L6;
            return null;
        L6:
            return wrapEntry(r0);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K higherKey(@ParametricNullness K r2) {
            return sortedMap().higherKey(r2);
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedAsMap, com.google.common.collect.AbstractMapBasedMultimap.AsMap, com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public NavigableSet<K> keySet() {
            return (NavigableSet) super.keySet();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedAsMap, com.google.common.collect.AbstractMapBasedMultimap.AsMap, com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public /* bridge */ /* synthetic */ Set keySet() {
            return keySet();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedAsMap, com.google.common.collect.AbstractMapBasedMultimap.AsMap, com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public /* bridge */ /* synthetic */ SortedSet keySet() {
            return keySet();
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, Collection<V>> lastEntry() {
            Map.Entry<K, Collection<V>> r0 = sortedMap().lastEntry();
            if (r0 != null) goto L6;
            return null;
        L6:
            return wrapEntry(r0);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, Collection<V>> lowerEntry(@ParametricNullness K r3) {
            Map.Entry<K, Collection<V>> r0 = sortedMap().lowerEntry(r3);
            if (r0 != null) goto L6;
            return null;
        L6:
            return wrapEntry(r0);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K lowerKey(@ParametricNullness K r2) {
            return sortedMap().lowerKey(r2);
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> navigableKeySet() {
            return keySet();
        }

        @CheckForNull
        Map.Entry<K, Collection<V>> pollAsMapEntry(Iterator<Map.Entry<K, Collection<V>>> r5) {
            if (r5.hasNext() == true) goto L6;
            return null;
        L6:
            Map.Entry<K, Collection<V>> r0 = r5.next();
            Collection<V> r1 = this.this$0.createCollection();
            r1.addAll(r0.getValue());
            r5.remove();
            return Maps.immutableEntry(r0.getKey(), this.this$0.unmodifiableCollectionSubclass(r1));
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, Collection<V>> pollFirstEntry() {
            return pollAsMapEntry(entrySet().iterator());
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, Collection<V>> pollLastEntry() {
            return pollAsMapEntry(descendingMap().entrySet().iterator());
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedAsMap
        NavigableMap<K, Collection<V>> sortedMap() {
            return (NavigableMap) super.sortedMap();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedAsMap
        /* bridge */ /* synthetic */ SortedMap sortedMap() {
            return sortedMap();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedAsMap, java.util.SortedMap, java.util.NavigableMap
        public NavigableMap<K, Collection<V>> subMap(@ParametricNullness K r3, @ParametricNullness K r4) {
            return subMap(r3, true, r4, false);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, Collection<V>> subMap(@ParametricNullness K r4, boolean r5, @ParametricNullness K r6, boolean r7) {
            return new NavigableAsMap(this.this$0, sortedMap().subMap(r4, r5, r6, r7));
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedAsMap, java.util.SortedMap, java.util.NavigableMap
        public /* bridge */ /* synthetic */ SortedMap subMap(@ParametricNullness Object r1, @ParametricNullness Object r2) {
            return subMap(r1, r2);
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedAsMap, java.util.SortedMap, java.util.NavigableMap
        public NavigableMap<K, Collection<V>> tailMap(@ParametricNullness K r2) {
            return tailMap(r2, true);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, Collection<V>> tailMap(@ParametricNullness K r4, boolean r5) {
            return new NavigableAsMap(this.this$0, sortedMap().tailMap(r4, r5));
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedAsMap, java.util.SortedMap, java.util.NavigableMap
        public /* bridge */ /* synthetic */ SortedMap tailMap(@ParametricNullness Object r1) {
            return tailMap(r1);
        }
    }

    private final class NavigableKeySet extends AbstractMapBasedMultimap<K, V>.SortedKeySet implements NavigableSet<K> {
        final /* synthetic */ AbstractMapBasedMultimap this$0;

        NavigableKeySet(AbstractMapBasedMultimap r1, NavigableMap<K, Collection<V>> r2) {
            this.this$0 = r1;
            super(r1, r2);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public K ceiling(@ParametricNullness K r2) {
            return sortedMap().ceilingKey(r2);
        }

        @Override // java.util.NavigableSet
        public Iterator<K> descendingIterator() {
            return descendingSet().iterator();
        }

        @Override // java.util.NavigableSet
        public NavigableSet<K> descendingSet() {
            return new NavigableKeySet(this.this$0, sortedMap().descendingMap());
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public K floor(@ParametricNullness K r2) {
            return sortedMap().floorKey(r2);
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedKeySet, java.util.SortedSet, java.util.NavigableSet
        public NavigableSet<K> headSet(@ParametricNullness K r2) {
            return headSet(r2, false);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<K> headSet(@ParametricNullness K r4, boolean r5) {
            return new NavigableKeySet(this.this$0, sortedMap().headMap(r4, r5));
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedKeySet, java.util.SortedSet, java.util.NavigableSet
        public /* bridge */ /* synthetic */ SortedSet headSet(@ParametricNullness Object r1) {
            return headSet(r1);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public K higher(@ParametricNullness K r2) {
            return sortedMap().higherKey(r2);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public K lower(@ParametricNullness K r2) {
            return sortedMap().lowerKey(r2);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public K pollFirst() {
            return (K) Iterators.pollNext(iterator());
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public K pollLast() {
            return (K) Iterators.pollNext(descendingIterator());
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedKeySet
        NavigableMap<K, Collection<V>> sortedMap() {
            return (NavigableMap) super.sortedMap();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedKeySet
        /* bridge */ /* synthetic */ SortedMap sortedMap() {
            return sortedMap();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedKeySet, java.util.SortedSet, java.util.NavigableSet
        public NavigableSet<K> subSet(@ParametricNullness K r3, @ParametricNullness K r4) {
            return subSet(r3, true, r4, false);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<K> subSet(@ParametricNullness K r4, boolean r5, @ParametricNullness K r6, boolean r7) {
            return new NavigableKeySet(this.this$0, sortedMap().subMap(r4, r5, r6, r7));
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedKeySet, java.util.SortedSet, java.util.NavigableSet
        public /* bridge */ /* synthetic */ SortedSet subSet(@ParametricNullness Object r1, @ParametricNullness Object r2) {
            return subSet(r1, r2);
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedKeySet, java.util.SortedSet, java.util.NavigableSet
        public NavigableSet<K> tailSet(@ParametricNullness K r2) {
            return tailSet(r2, true);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<K> tailSet(@ParametricNullness K r4, boolean r5) {
            return new NavigableKeySet(this.this$0, sortedMap().tailMap(r4, r5));
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.SortedKeySet, java.util.SortedSet, java.util.NavigableSet
        public /* bridge */ /* synthetic */ SortedSet tailSet(@ParametricNullness Object r1) {
            return tailSet(r1);
        }
    }

    private class RandomAccessWrappedList extends AbstractMapBasedMultimap<K, V>.WrappedList implements RandomAccess {
        final /* synthetic */ AbstractMapBasedMultimap this$0;

        RandomAccessWrappedList(@ParametricNullness AbstractMapBasedMultimap r1, K r2, @CheckForNull List<V> r3, AbstractMapBasedMultimap<K, V>.WrappedCollection r4) {
            this.this$0 = r1;
            super(r1, r2, r3, r4);
        }
    }

    private class SortedAsMap extends AbstractMapBasedMultimap<K, V>.AsMap implements SortedMap<K, Collection<V>> {

        @CheckForNull
        SortedSet<K> sortedKeySet;
        final /* synthetic */ AbstractMapBasedMultimap this$0;

        SortedAsMap(AbstractMapBasedMultimap r1, SortedMap<K, Collection<V>> r2) {
            this.this$0 = r1;
            super(r1, r2);
        }

        @Override // java.util.SortedMap
        @CheckForNull
        public Comparator<? super K> comparator() {
            return sortedMap().comparator();
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        /* bridge */ /* synthetic */ Set createKeySet() {
            return createKeySet();
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        SortedSet<K> createKeySet() {
            return new SortedKeySet(this.this$0, sortedMap());
        }

        @Override // java.util.SortedMap
        @ParametricNullness
        public K firstKey() {
            return sortedMap().firstKey();
        }

        public SortedMap<K, Collection<V>> headMap(@ParametricNullness K r4) {
            return new SortedAsMap(this.this$0, sortedMap().headMap(r4));
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.AsMap, com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public /* bridge */ /* synthetic */ Set keySet() {
            return keySet();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.AsMap, com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public SortedSet<K> keySet() {
            SortedSet<K> r0 = this.sortedKeySet;
            if (r0 != null) goto L6;
            SortedSet<K> r1 = createKeySet();
            this.sortedKeySet = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // java.util.SortedMap
        @ParametricNullness
        public K lastKey() {
            return sortedMap().lastKey();
        }

        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap) this.submap;
        }

        public SortedMap<K, Collection<V>> subMap(@ParametricNullness K r4, @ParametricNullness K r5) {
            return new SortedAsMap(this.this$0, sortedMap().subMap(r4, r5));
        }

        public SortedMap<K, Collection<V>> tailMap(@ParametricNullness K r4) {
            return new SortedAsMap(this.this$0, sortedMap().tailMap(r4));
        }
    }

    private class SortedKeySet extends AbstractMapBasedMultimap<K, V>.KeySet implements SortedSet<K> {
        final /* synthetic */ AbstractMapBasedMultimap this$0;

        SortedKeySet(AbstractMapBasedMultimap r1, SortedMap<K, Collection<V>> r2) {
            this.this$0 = r1;
            super(r1, r2);
        }

        @Override // java.util.SortedSet
        @CheckForNull
        public Comparator<? super K> comparator() {
            return sortedMap().comparator();
        }

        @Override // java.util.SortedSet
        @ParametricNullness
        public K first() {
            return sortedMap().firstKey();
        }

        public SortedSet<K> headSet(@ParametricNullness K r4) {
            return new SortedKeySet(this.this$0, sortedMap().headMap(r4));
        }

        @Override // java.util.SortedSet
        @ParametricNullness
        public K last() {
            return sortedMap().lastKey();
        }

        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap) super.map();
        }

        public SortedSet<K> subSet(@ParametricNullness K r4, @ParametricNullness K r5) {
            return new SortedKeySet(this.this$0, sortedMap().subMap(r4, r5));
        }

        public SortedSet<K> tailSet(@ParametricNullness K r4) {
            return new SortedKeySet(this.this$0, sortedMap().tailMap(r4));
        }
    }

    class WrappedCollection extends AbstractCollection<V> {

        @CheckForNull
        final AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor;

        @CheckForNull
        final Collection<V> ancestorDelegate;
        Collection<V> delegate;

        @ParametricNullness
        final K key;
        final /* synthetic */ AbstractMapBasedMultimap this$0;

        class WrappedIterator implements Iterator<V> {
            final Iterator<V> delegateIterator;
            final Collection<V> originalDelegate;
            final /* synthetic */ WrappedCollection this$1;

            WrappedIterator(WrappedCollection r2) {
                this.this$1 = r2;
                this.originalDelegate = this.this$1.delegate;
                this.delegateIterator = AbstractMapBasedMultimap.access$100(r2.delegate);
            }

            WrappedIterator(WrappedCollection r2, Iterator<V> r3) {
                this.this$1 = r2;
                this.originalDelegate = this.this$1.delegate;
                this.delegateIterator = r3;
            }

            Iterator<V> getDelegateIterator() {
                validateIterator();
                return this.delegateIterator;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                validateIterator();
                return this.delegateIterator.hasNext();
            }

            @Override // java.util.Iterator
            @ParametricNullness
            public V next() {
                validateIterator();
                return this.delegateIterator.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                this.delegateIterator.remove();
                AbstractMapBasedMultimap.access$210(this.this$1.this$0);
                this.this$1.removeIfEmpty();
            }

            void validateIterator() {
                this.this$1.refreshIfEmpty();
                if (this.this$1.delegate != this.originalDelegate) goto L6;
                return;
            L6:
                throw new ConcurrentModificationException();
            }
        }

        WrappedCollection(@ParametricNullness AbstractMapBasedMultimap r2, K r3, @CheckForNull Collection<V> r4, AbstractMapBasedMultimap<K, V>.WrappedCollection r5) {
            this.this$0 = r2;
            this.key = r3;
            this.delegate = r4;
            this.ancestor = r5;
            if (r5 != null) goto L5;
            Collection<V> r0 = null;
        L6:
            this.ancestorDelegate = r0;
            return;
        L5:
            r0 = r5.getDelegate();
            goto L6
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean add(@ParametricNullness V r4) {
            refreshIfEmpty();
            boolean r0 = this.delegate.isEmpty();
            boolean r1 = this.delegate.add(r4);
            if (r1 == false) goto L7;
            AbstractMapBasedMultimap.access$208(this.this$0);
            if (r0 == false) goto L7;
            addToMap();
        L7:
            return r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends V> r6) {
            if (r6.isEmpty() == false) goto L6;
            return false;
        L6:
            int r0 = size();
            boolean r1 = this.delegate.addAll(r6);
            if (r1 == false) goto L11;
            int r2 = this.delegate.size();
            AbstractMapBasedMultimap.access$212(this.this$0, r2 - r0);
            if (r0 != 0) goto L11;
            addToMap();
        L11:
            return r1;
        }

        void addToMap() {
            if (this.ancestor == null) goto L5;
            this.ancestor.addToMap();
            return;
        L5:
            AbstractMapBasedMultimap.access$000(this.this$0).put(this.key, this.delegate);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            int r0 = size();
            if (r0 != 0) goto L5;
            return;
        L5:
            this.delegate.clear();
            AbstractMapBasedMultimap.access$220(this.this$0, r0);
            removeIfEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(@CheckForNull Object r2) {
            refreshIfEmpty();
            return this.delegate.contains(r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean containsAll(Collection<?> r2) {
            refreshIfEmpty();
            return this.delegate.containsAll(r2);
        }

        @Override // java.util.Collection
        public boolean equals(@CheckForNull Object r2) {
            if (r2 != this) goto L5;
            return true;
        L5:
            refreshIfEmpty();
            return this.delegate.equals(r2);
        }

        @CheckForNull
        AbstractMapBasedMultimap<K, V>.WrappedCollection getAncestor() {
            return this.ancestor;
        }

        Collection<V> getDelegate() {
            return this.delegate;
        }

        @ParametricNullness
        K getKey() {
            return this.key;
        }

        @Override // java.util.Collection
        public int hashCode() {
            refreshIfEmpty();
            return this.delegate.hashCode();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            refreshIfEmpty();
            return new WrappedIterator(this);
        }

        void refreshIfEmpty() {
            if (this.ancestor == null) goto L10;
            this.ancestor.refreshIfEmpty();
            if (this.ancestor.getDelegate() != this.ancestorDelegate) goto L8;
            return;
        L8:
            throw new ConcurrentModificationException();
        L10:
            if (this.delegate.isEmpty() == false) goto L15;
            Collection<V> r0 = (Collection) AbstractMapBasedMultimap.access$000(this.this$0).get(this.key);
            if (r0 == null) goto L16;
            this.delegate = r0;
            return;
        L16:
            return;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(@CheckForNull Object r3) {
            refreshIfEmpty();
            boolean r0 = this.delegate.remove(r3);
            if (r0 == false) goto L5;
            AbstractMapBasedMultimap.access$210(this.this$0);
            removeIfEmpty();
        L5:
            return r0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> r6) {
            if (r6.isEmpty() == false) goto L6;
            return false;
        L6:
            int r0 = size();
            boolean r1 = this.delegate.removeAll(r6);
            if (r1 == false) goto L9;
            int r2 = this.delegate.size();
            AbstractMapBasedMultimap.access$212(this.this$0, r2 - r0);
            removeIfEmpty();
        L9:
            return r1;
        }

        void removeIfEmpty() {
            if (this.ancestor == null) goto L6;
            this.ancestor.removeIfEmpty();
            return;
        L6:
            if (this.delegate.isEmpty() == false) goto L10;
            AbstractMapBasedMultimap.access$000(this.this$0).remove(this.key);
            return;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> r6) {
            Preconditions.checkNotNull(r6);
            int r0 = size();
            boolean r1 = this.delegate.retainAll(r6);
            if (r1 == false) goto L5;
            int r2 = this.delegate.size();
            AbstractMapBasedMultimap.access$212(this.this$0, r2 - r0);
            removeIfEmpty();
        L5:
            return r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            refreshIfEmpty();
            return this.delegate.size();
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            refreshIfEmpty();
            return this.delegate.toString();
        }
    }

    class WrappedList extends AbstractMapBasedMultimap<K, V>.WrappedCollection implements List<V> {
        final /* synthetic */ AbstractMapBasedMultimap this$0;

        private class WrappedListIterator extends AbstractMapBasedMultimap<K, V>.WrappedCollection.WrappedIterator implements ListIterator<V> {
            final /* synthetic */ WrappedList this$1;

            WrappedListIterator(WrappedList r1) {
                this.this$1 = r1;
                super(r1);
            }

            public WrappedListIterator(WrappedList r2, int r3) {
                this.this$1 = r2;
                super(r2, r2.getListDelegate().listIterator(r3));
            }

            private ListIterator<V> getDelegateListIterator() {
                return (ListIterator) getDelegateIterator();
            }

            @Override // java.util.ListIterator
            public void add(@ParametricNullness V r3) {
                boolean r0 = this.this$1.isEmpty();
                getDelegateListIterator().add(r3);
                AbstractMapBasedMultimap.access$208(this.this$1.this$0);
                if (r0 == false) goto L6;
                this.this$1.addToMap();
                return;
            }

            @Override // java.util.ListIterator
            public boolean hasPrevious() {
                return getDelegateListIterator().hasPrevious();
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return getDelegateListIterator().nextIndex();
            }

            @Override // java.util.ListIterator
            @ParametricNullness
            public V previous() {
                return getDelegateListIterator().previous();
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return getDelegateListIterator().previousIndex();
            }

            @Override // java.util.ListIterator
            public void set(@ParametricNullness V r2) {
                getDelegateListIterator().set(r2);
            }
        }

        WrappedList(@ParametricNullness AbstractMapBasedMultimap r1, K r2, @CheckForNull List<V> r3, AbstractMapBasedMultimap<K, V>.WrappedCollection r4) {
            this.this$0 = r1;
            super(r1, r2, r3, r4);
        }

        @Override // java.util.List
        public void add(int r3, @ParametricNullness V r4) {
            refreshIfEmpty();
            boolean r0 = getDelegate().isEmpty();
            getListDelegate().add(r3, r4);
            AbstractMapBasedMultimap.access$208(this.this$0);
            if (r0 == false) goto L6;
            addToMap();
            return;
        }

        @Override // java.util.List
        public boolean addAll(int r6, Collection<? extends V> r7) {
            if (r7.isEmpty() == false) goto L6;
            return false;
        L6:
            int r0 = size();
            boolean r1 = getListDelegate().addAll(r6, r7);
            if (r1 == false) goto L11;
            int r2 = getDelegate().size();
            AbstractMapBasedMultimap.access$212(this.this$0, r2 - r0);
            if (r0 != 0) goto L11;
            addToMap();
        L11:
            return r1;
        }

        @Override // java.util.List
        @ParametricNullness
        public V get(int r2) {
            refreshIfEmpty();
            return getListDelegate().get(r2);
        }

        List<V> getListDelegate() {
            return (List) getDelegate();
        }

        @Override // java.util.List
        public int indexOf(@CheckForNull Object r2) {
            refreshIfEmpty();
            return getListDelegate().indexOf(r2);
        }

        @Override // java.util.List
        public int lastIndexOf(@CheckForNull Object r2) {
            refreshIfEmpty();
            return getListDelegate().lastIndexOf(r2);
        }

        @Override // java.util.List
        public ListIterator<V> listIterator() {
            refreshIfEmpty();
            return new WrappedListIterator(this);
        }

        @Override // java.util.List
        public ListIterator<V> listIterator(int r2) {
            refreshIfEmpty();
            return new WrappedListIterator(this, r2);
        }

        @Override // java.util.List
        @ParametricNullness
        public V remove(int r3) {
            refreshIfEmpty();
            V r0 = getListDelegate().remove(r3);
            AbstractMapBasedMultimap.access$210(this.this$0);
            removeIfEmpty();
            return r0;
        }

        @Override // java.util.List
        @ParametricNullness
        public V set(int r2, @ParametricNullness V r3) {
            refreshIfEmpty();
            return getListDelegate().set(r2, r3);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.List
        public List<V> subList(int r5, int r6) {
            refreshIfEmpty();
            AbstractMapBasedMultimap r0 = this.this$0;
            Object r1 = getKey();
            List<V> r2 = getListDelegate().subList(r5, r6);
            if (getAncestor() != null) goto L5;
            WrappedCollection r3 = this;
        L7:
            return r0.wrapList(r1, r2, r3);
        L5:
            r3 = getAncestor();
            goto L7
        }
    }

    class WrappedNavigableSet extends AbstractMapBasedMultimap<K, V>.WrappedSortedSet implements NavigableSet<V> {
        final /* synthetic */ AbstractMapBasedMultimap this$0;

        WrappedNavigableSet(@ParametricNullness AbstractMapBasedMultimap r1, K r2, @CheckForNull NavigableSet<V> r3, AbstractMapBasedMultimap<K, V>.WrappedCollection r4) {
            this.this$0 = r1;
            super(r1, r2, r3, r4);
        }

        private NavigableSet<V> wrap(NavigableSet<V> r5) {
            AbstractMapBasedMultimap r1 = this.this$0;
            K r2 = this.key;
            if (getAncestor() != null) goto L5;
            WrappedCollection r3 = this;
        L7:
            return new WrappedNavigableSet(r1, r2, r5, r3);
        L5:
            r3 = getAncestor();
            goto L7
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public V ceiling(@ParametricNullness V r2) {
            return getSortedSetDelegate().ceiling(r2);
        }

        @Override // java.util.NavigableSet
        public Iterator<V> descendingIterator() {
            return new WrappedCollection.WrappedIterator(this, getSortedSetDelegate().descendingIterator());
        }

        @Override // java.util.NavigableSet
        public NavigableSet<V> descendingSet() {
            return wrap(getSortedSetDelegate().descendingSet());
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public V floor(@ParametricNullness V r2) {
            return getSortedSetDelegate().floor(r2);
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.WrappedSortedSet
        NavigableSet<V> getSortedSetDelegate() {
            return (NavigableSet) super.getSortedSetDelegate();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.WrappedSortedSet
        /* bridge */ /* synthetic */ SortedSet getSortedSetDelegate() {
            return getSortedSetDelegate();
        }

        @Override // java.util.NavigableSet
        public NavigableSet<V> headSet(@ParametricNullness V r2, boolean r3) {
            return wrap(getSortedSetDelegate().headSet(r2, r3));
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public V higher(@ParametricNullness V r2) {
            return getSortedSetDelegate().higher(r2);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public V lower(@ParametricNullness V r2) {
            return getSortedSetDelegate().lower(r2);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public V pollFirst() {
            return (V) Iterators.pollNext(iterator());
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public V pollLast() {
            return (V) Iterators.pollNext(descendingIterator());
        }

        @Override // java.util.NavigableSet
        public NavigableSet<V> subSet(@ParametricNullness V r2, boolean r3, @ParametricNullness V r4, boolean r5) {
            return wrap(getSortedSetDelegate().subSet(r2, r3, r4, r5));
        }

        @Override // java.util.NavigableSet
        public NavigableSet<V> tailSet(@ParametricNullness V r2, boolean r3) {
            return wrap(getSortedSetDelegate().tailSet(r2, r3));
        }
    }

    class WrappedSet extends AbstractMapBasedMultimap<K, V>.WrappedCollection implements Set<V> {
        final /* synthetic */ AbstractMapBasedMultimap this$0;

        WrappedSet(@ParametricNullness AbstractMapBasedMultimap r2, K r3, Set<V> r4) {
            this.this$0 = r2;
            super(r2, r3, r4, null);
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap.WrappedCollection, java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> r6) {
            if (r6.isEmpty() == false) goto L6;
            return false;
        L6:
            int r0 = size();
            boolean r1 = Sets.removeAllImpl((Set) this.delegate, r6);
            if (r1 == false) goto L9;
            int r2 = this.delegate.size();
            AbstractMapBasedMultimap.access$212(this.this$0, r2 - r0);
            removeIfEmpty();
        L9:
            return r1;
        }
    }

    class WrappedSortedSet extends AbstractMapBasedMultimap<K, V>.WrappedCollection implements SortedSet<V> {
        final /* synthetic */ AbstractMapBasedMultimap this$0;

        WrappedSortedSet(@ParametricNullness AbstractMapBasedMultimap r1, K r2, @CheckForNull SortedSet<V> r3, AbstractMapBasedMultimap<K, V>.WrappedCollection r4) {
            this.this$0 = r1;
            super(r1, r2, r3, r4);
        }

        @Override // java.util.SortedSet
        @CheckForNull
        public Comparator<? super V> comparator() {
            return getSortedSetDelegate().comparator();
        }

        @Override // java.util.SortedSet
        @ParametricNullness
        public V first() {
            refreshIfEmpty();
            return getSortedSetDelegate().first();
        }

        SortedSet<V> getSortedSetDelegate() {
            return (SortedSet) getDelegate();
        }

        @Override // java.util.SortedSet
        public SortedSet<V> headSet(@ParametricNullness V r6) {
            refreshIfEmpty();
            AbstractMapBasedMultimap r1 = this.this$0;
            Object r2 = getKey();
            SortedSet<V> r3 = getSortedSetDelegate().headSet(r6);
            if (getAncestor() != null) goto L5;
            WrappedCollection r4 = this;
        L7:
            return new WrappedSortedSet(r1, r2, r3, r4);
        L5:
            r4 = getAncestor();
            goto L7
        }

        @Override // java.util.SortedSet
        @ParametricNullness
        public V last() {
            refreshIfEmpty();
            return getSortedSetDelegate().last();
        }

        @Override // java.util.SortedSet
        public SortedSet<V> subSet(@ParametricNullness V r6, @ParametricNullness V r7) {
            refreshIfEmpty();
            AbstractMapBasedMultimap r1 = this.this$0;
            Object r2 = getKey();
            SortedSet<V> r3 = getSortedSetDelegate().subSet(r6, r7);
            if (getAncestor() != null) goto L5;
            WrappedCollection r4 = this;
        L7:
            return new WrappedSortedSet(r1, r2, r3, r4);
        L5:
            r4 = getAncestor();
            goto L7
        }

        @Override // java.util.SortedSet
        public SortedSet<V> tailSet(@ParametricNullness V r6) {
            refreshIfEmpty();
            AbstractMapBasedMultimap r1 = this.this$0;
            Object r2 = getKey();
            SortedSet<V> r3 = getSortedSetDelegate().tailSet(r6);
            if (getAncestor() != null) goto L5;
            WrappedCollection r4 = this;
        L7:
            return new WrappedSortedSet(r1, r2, r3, r4);
        L5:
            r4 = getAncestor();
            goto L7
        }
    }

    protected AbstractMapBasedMultimap(Map<K, Collection<V>> r2) {
        Preconditions.checkArgument(r2.isEmpty());
        this.map = r2;
    }

    static /* synthetic */ Map access$000(AbstractMapBasedMultimap r1) {
        return r1.map;
    }

    static /* synthetic */ Iterator access$100(Collection r1) {
        return iteratorOrListIterator(r1);
    }

    static /* synthetic */ int access$208(AbstractMapBasedMultimap r2) {
        int r0 = r2.totalSize;
        r2.totalSize = r0 + 1;
        return r0;
    }

    static /* synthetic */ int access$210(AbstractMapBasedMultimap r2) {
        int r0 = r2.totalSize;
        r2.totalSize = r0 - 1;
        return r0;
    }

    static /* synthetic */ int access$212(AbstractMapBasedMultimap r1, int r2) {
        int r0 = r1.totalSize + r2;
        r1.totalSize = r0;
        return r0;
    }

    static /* synthetic */ int access$220(AbstractMapBasedMultimap r1, int r2) {
        int r0 = r1.totalSize - r2;
        r1.totalSize = r0;
        return r0;
    }

    static /* synthetic */ void access$300(AbstractMapBasedMultimap r0, Object r1) {
        r0.removeValuesForKey(r1);
    }

    private Collection<V> getOrCreateCollection(@ParametricNullness K r3) {
        Collection<V> r0 = this.map.get(r3);
        if (r0 != null) goto L6;
        Collection<V> r1 = createCollection(r3);
        this.map.put(r3, r1);
        return r1;
    L6:
        return r0;
    }

    private static <E> Iterator<E> iteratorOrListIterator(Collection<E> r1) {
        if ((r1 instanceof List) == false) goto L6;
        return ((List) r1).listIterator();
    L6:
        return r1.iterator();
    }

    private void removeValuesForKey(@CheckForNull Object r4) {
        Collection r0 = (Collection) Maps.safeRemove(this.map, r4);
        if (r0 == null) goto L6;
        int r1 = r0.size();
        r0.clear();
        this.totalSize -= r1;
        return;
    }

    Map<K, Collection<V>> backingMap() {
        return this.map;
    }

    @Override // com.google.common.collect.Multimap
    public void clear() {
        Iterator<Collection<V>> r0 = this.map.values().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().clear();
        goto L4
    L6:
        this.map.clear();
        this.totalSize = 0;
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsKey(@CheckForNull Object r2) {
        return this.map.containsKey(r2);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Map<K, Collection<V>> createAsMap() {
        return new AsMap(this, this.map);
    }

    abstract Collection<V> createCollection();

    Collection<V> createCollection(@ParametricNullness K r2) {
        return createCollection();
    }

    @Override // com.google.common.collect.AbstractMultimap
    Collection<Map.Entry<K, V>> createEntries() {
        if ((this instanceof SetMultimap) == false) goto L7;
        return new AbstractMultimap.EntrySet(this);
    L7:
        return new AbstractMultimap.Entries(this);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Set<K> createKeySet() {
        return new KeySet(this, this.map);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Multiset<K> createKeys() {
        return new Multimaps.Keys(this);
    }

    final Map<K, Collection<V>> createMaybeNavigableAsMap() {
        if ((this.map instanceof NavigableMap) == false) goto L7;
        return new NavigableAsMap(this, (NavigableMap) this.map);
    L7:
        if ((this.map instanceof SortedMap) == false) goto L11;
        return new SortedAsMap(this, (SortedMap) this.map);
    L11:
        return new AsMap(this, this.map);
    }

    final Set<K> createMaybeNavigableKeySet() {
        if ((this.map instanceof NavigableMap) == false) goto L7;
        return new NavigableKeySet(this, (NavigableMap) this.map);
    L7:
        if ((this.map instanceof SortedMap) == false) goto L11;
        return new SortedKeySet(this, (SortedMap) this.map);
    L11:
        return new KeySet(this, this.map);
    }

    Collection<V> createUnmodifiableEmptyCollection() {
        return (Collection<V>) unmodifiableCollectionSubclass(createCollection());
    }

    @Override // com.google.common.collect.AbstractMultimap
    Collection<V> createValues() {
        return new AbstractMultimap.Values(this);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public Collection<Map.Entry<K, V>> entries() {
        return super.entries();
    }

    @Override // com.google.common.collect.AbstractMultimap
    Iterator<Map.Entry<K, V>> entryIterator() {
        return new C05922(this);
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> get(@ParametricNullness K r3) {
        Collection<V> r0 = this.map.get(r3);
        if (r0 != null) goto L6;
        r0 = createCollection(r3);
    L6:
        return wrapCollection(r3, r0);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public boolean put(@ParametricNullness K r4, @ParametricNullness V r5) {
        Collection<V> r0 = this.map.get(r4);
        if (r0 != null) goto L11;
        Collection<V> r1 = createCollection(r4);
        if (r1.add(r5) == false) goto L9;
        this.totalSize++;
        this.map.put(r4, r1);
        return true;
    L9:
        throw new AssertionError("New Collection violated the Collection spec");
    L11:
        if (r0.add(r5) == false) goto L14;
        this.totalSize++;
        return true;
    L14:
        return false;
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> removeAll(@CheckForNull Object r5) {
        Collection<V> r0 = this.map.remove(r5);
        if (r0 == null) goto L5;
        Collection r1 = createCollection();
        r1.addAll(r0);
        this.totalSize -= r0.size();
        r0.clear();
        return (Collection<V>) unmodifiableCollectionSubclass(r1);
    L5:
        return createUnmodifiableEmptyCollection();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public Collection<V> replaceValues(@ParametricNullness K r6, Iterable<? extends V> r7) {
        Iterator<? extends V> r0 = r7.iterator();
        if (r0.hasNext() == false) goto L5;
        Collection<V> r1 = getOrCreateCollection(r6);
        Collection<V> r2 = createCollection();
        r2.addAll(r1);
        this.totalSize -= r1.size();
        r1.clear();
    L8:
        if (r0.hasNext() == false) goto L13;
        if (r1.add(r0.next()) == false) goto L8;
        this.totalSize++;
        goto L8
    L13:
        return (Collection<V>) unmodifiableCollectionSubclass(r2);
    L5:
        return removeAll(r6);
    }

    final void setMap(Map<K, Collection<V>> r5) {
        this.map = r5;
        this.totalSize = 0;
        Iterator<Collection<V>> r0 = r5.values().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Collection<V> r1 = r0.next();
        Preconditions.checkArgument(!r1.isEmpty());
        this.totalSize += r1.size();
        goto L4
    }

    @Override // com.google.common.collect.Multimap
    public int size() {
        return this.totalSize;
    }

    <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> r2) {
        return Collections.unmodifiableCollection(r2);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Iterator<V> valueIterator() {
        return new C05911(this);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public Collection<V> values() {
        return super.values();
    }

    Collection<V> wrapCollection(@ParametricNullness K r3, Collection<V> r4) {
        return new WrappedCollection(this, r3, r4, null);
    }

    final List<V> wrapList(@ParametricNullness K r2, List<V> r3, @CheckForNull AbstractMapBasedMultimap<K, V>.WrappedCollection r4) {
        if ((r3 instanceof RandomAccess) == false) goto L6;
        return new RandomAccessWrappedList(this, r2, r3, r4);
    L6:
        return new WrappedList(this, r2, r3, r4);
    }
}
