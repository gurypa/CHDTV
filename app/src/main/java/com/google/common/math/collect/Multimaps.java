package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collector;
import java.util.stream.Stream;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Multimaps {

    static final class AsMap<K, V> extends Maps.ViewCachingAbstractMap<K, Collection<V>> {
        private final Multimap<K, V> multimap;

        class EntrySet extends Maps.EntrySet<K, Collection<V>> {
            final /* synthetic */ AsMap this$0;

            EntrySet(AsMap r1) {
                this.this$0 = r1;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return Maps.asMapEntryIterator(AsMap.access$200(this.this$0).keySet(), new Multimaps$AsMap$EntrySet$$ExternalSyntheticLambda0(this));
            }

            /* JADX INFO: renamed from: lambda$iterator$0$com-google-common-collect-Multimaps$AsMap$EntrySet */
            /* synthetic */ Collection m322xda360ac8(Object r2) {
                return AsMap.access$200(this.this$0).get(r2);
            }

            @Override // com.google.common.collect.Maps.EntrySet
            Map<K, Collection<V>> map() {
                return this.this$0;
            }

            @Override // com.google.common.collect.Maps.EntrySet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(@CheckForNull Object r4) {
                if (contains(r4) == true) goto L6;
                return false;
            L6:
                Map.Entry r0 = (Map.Entry) Objects.requireNonNull((Map.Entry) r4);
                this.this$0.removeValuesForKey(r0.getKey());
                return true;
            }
        }

        AsMap(Multimap<K, V> r2) {
            this.multimap = (Multimap) Preconditions.checkNotNull(r2);
        }

        static /* synthetic */ Multimap access$200(AsMap r1) {
            return r1.multimap;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            this.multimap.clear();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            return this.multimap.containsKey(r2);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
            return new EntrySet(this);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object get(@CheckForNull Object r1) {
            return get(r1);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public Collection<V> get(@CheckForNull Object r2) {
            if (containsKey(r2) == true) goto L7;
            return null;
        L7:
            return this.multimap.get(r2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            return this.multimap.isEmpty();
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return this.multimap.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object remove(@CheckForNull Object r1) {
            return remove(r1);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public Collection<V> remove(@CheckForNull Object r2) {
            if (containsKey(r2) == true) goto L7;
            return null;
        L7:
            return this.multimap.removeAll(r2);
        }

        void removeValuesForKey(@CheckForNull Object r2) {
            this.multimap.keySet().remove(r2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return this.multimap.keySet().size();
        }
    }

    private static class CustomListMultimap<K, V> extends AbstractListMultimap<K, V> {
        private static final long serialVersionUID = 0;
        transient Supplier<? extends List<V>> factory;

        CustomListMultimap(Map<K, Collection<V>> r2, Supplier<? extends List<V>> r3) {
            super(r2);
            this.factory = (Supplier) Preconditions.checkNotNull(r3);
        }

        private void readObject(ObjectInputStream r2) throws IOException, ClassNotFoundException {
            r2.defaultReadObject();
            this.factory = (Supplier) Objects.requireNonNull(r2.readObject());
            setMap((Map) Objects.requireNonNull(r2.readObject()));
        }

        private void writeObject(ObjectOutputStream r2) throws IOException {
            r2.defaultWriteObject();
            r2.writeObject(this.factory);
            r2.writeObject(backingMap());
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Map<K, Collection<V>> createAsMap() {
            return createMaybeNavigableAsMap();
        }

        @Override // com.google.common.collect.AbstractListMultimap, com.google.common.collect.AbstractMapBasedMultimap
        protected /* bridge */ /* synthetic */ Collection createCollection() {
            return createCollection();
        }

        @Override // com.google.common.collect.AbstractListMultimap, com.google.common.collect.AbstractMapBasedMultimap
        protected List<V> createCollection() {
            return this.factory.get();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Set<K> createKeySet() {
            return createMaybeNavigableKeySet();
        }
    }

    private static class CustomMultimap<K, V> extends AbstractMapBasedMultimap<K, V> {
        private static final long serialVersionUID = 0;
        transient Supplier<? extends Collection<V>> factory;

        CustomMultimap(Map<K, Collection<V>> r2, Supplier<? extends Collection<V>> r3) {
            super(r2);
            this.factory = (Supplier) Preconditions.checkNotNull(r3);
        }

        private void readObject(ObjectInputStream r2) throws IOException, ClassNotFoundException {
            r2.defaultReadObject();
            this.factory = (Supplier) Objects.requireNonNull(r2.readObject());
            setMap((Map) Objects.requireNonNull(r2.readObject()));
        }

        private void writeObject(ObjectOutputStream r2) throws IOException {
            r2.defaultWriteObject();
            r2.writeObject(this.factory);
            r2.writeObject(backingMap());
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Map<K, Collection<V>> createAsMap() {
            return createMaybeNavigableAsMap();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap
        protected Collection<V> createCollection() {
            return this.factory.get();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Set<K> createKeySet() {
            return createMaybeNavigableKeySet();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap
        <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> r2) {
            if ((r2 instanceof NavigableSet) == false) goto L7;
            return Sets.unmodifiableNavigableSet((NavigableSet) r2);
        L7:
            if ((r2 instanceof SortedSet) == false) goto L11;
            return Collections.unmodifiableSortedSet((SortedSet) r2);
        L11:
            if ((r2 instanceof Set) == false) goto L15;
            return Collections.unmodifiableSet((Set) r2);
        L15:
            if ((r2 instanceof List) == false) goto L19;
            return Collections.unmodifiableList((List) r2);
        L19:
            return Collections.unmodifiableCollection(r2);
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap
        Collection<V> wrapCollection(@ParametricNullness K r4, Collection<V> r5) {
            if ((r5 instanceof List) == false) goto L7;
            return wrapList(r4, (List) r5, null);
        L7:
            if ((r5 instanceof NavigableSet) == false) goto L11;
            return new AbstractMapBasedMultimap.WrappedNavigableSet(this, r4, (NavigableSet) r5, null);
        L11:
            if ((r5 instanceof SortedSet) == false) goto L15;
            return new AbstractMapBasedMultimap.WrappedSortedSet(this, r4, (SortedSet) r5, null);
        L15:
            if ((r5 instanceof Set) == false) goto L19;
            return new AbstractMapBasedMultimap.WrappedSet(this, r4, (Set) r5);
        L19:
            return new AbstractMapBasedMultimap.WrappedCollection(this, r4, r5, null);
        }
    }

    private static class CustomSetMultimap<K, V> extends AbstractSetMultimap<K, V> {
        private static final long serialVersionUID = 0;
        transient Supplier<? extends Set<V>> factory;

        CustomSetMultimap(Map<K, Collection<V>> r2, Supplier<? extends Set<V>> r3) {
            super(r2);
            this.factory = (Supplier) Preconditions.checkNotNull(r3);
        }

        private void readObject(ObjectInputStream r2) throws IOException, ClassNotFoundException {
            r2.defaultReadObject();
            this.factory = (Supplier) Objects.requireNonNull(r2.readObject());
            setMap((Map) Objects.requireNonNull(r2.readObject()));
        }

        private void writeObject(ObjectOutputStream r2) throws IOException {
            r2.defaultWriteObject();
            r2.writeObject(this.factory);
            r2.writeObject(backingMap());
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Map<K, Collection<V>> createAsMap() {
            return createMaybeNavigableAsMap();
        }

        @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
        protected /* bridge */ /* synthetic */ Collection createCollection() {
            return createCollection();
        }

        @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
        protected Set<V> createCollection() {
            return this.factory.get();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Set<K> createKeySet() {
            return createMaybeNavigableKeySet();
        }

        @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
        <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> r2) {
            if ((r2 instanceof NavigableSet) == false) goto L7;
            return Sets.unmodifiableNavigableSet((NavigableSet) r2);
        L7:
            if ((r2 instanceof SortedSet) == false) goto L11;
            return Collections.unmodifiableSortedSet((SortedSet) r2);
        L11:
            return Collections.unmodifiableSet((Set) r2);
        }

        @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
        Collection<V> wrapCollection(@ParametricNullness K r4, Collection<V> r5) {
            if ((r5 instanceof NavigableSet) == false) goto L7;
            return new AbstractMapBasedMultimap.WrappedNavigableSet(this, r4, (NavigableSet) r5, null);
        L7:
            if ((r5 instanceof SortedSet) == false) goto L11;
            return new AbstractMapBasedMultimap.WrappedSortedSet(this, r4, (SortedSet) r5, null);
        L11:
            return new AbstractMapBasedMultimap.WrappedSet(this, r4, (Set) r5);
        }
    }

    private static class CustomSortedSetMultimap<K, V> extends AbstractSortedSetMultimap<K, V> {
        private static final long serialVersionUID = 0;
        transient Supplier<? extends SortedSet<V>> factory;

        @CheckForNull
        transient Comparator<? super V> valueComparator;

        CustomSortedSetMultimap(Map<K, Collection<V>> r2, Supplier<? extends SortedSet<V>> r3) {
            super(r2);
            this.factory = (Supplier) Preconditions.checkNotNull(r3);
            this.valueComparator = r3.get().comparator();
        }

        private void readObject(ObjectInputStream r2) throws IOException, ClassNotFoundException {
            r2.defaultReadObject();
            this.factory = (Supplier) Objects.requireNonNull(r2.readObject());
            this.valueComparator = this.factory.get().comparator();
            setMap((Map) Objects.requireNonNull(r2.readObject()));
        }

        private void writeObject(ObjectOutputStream r2) throws IOException {
            r2.defaultWriteObject();
            r2.writeObject(this.factory);
            r2.writeObject(backingMap());
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Map<K, Collection<V>> createAsMap() {
            return createMaybeNavigableAsMap();
        }

        @Override // com.google.common.collect.AbstractSortedSetMultimap, com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
        protected /* bridge */ /* synthetic */ Collection createCollection() {
            return createCollection();
        }

        @Override // com.google.common.collect.AbstractSortedSetMultimap, com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
        protected /* bridge */ /* synthetic */ Set createCollection() {
            return createCollection();
        }

        @Override // com.google.common.collect.AbstractSortedSetMultimap, com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
        protected SortedSet<V> createCollection() {
            return this.factory.get();
        }

        @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
        Set<K> createKeySet() {
            return createMaybeNavigableKeySet();
        }

        @Override // com.google.common.collect.SortedSetMultimap
        @CheckForNull
        public Comparator<? super V> valueComparator() {
            return this.valueComparator;
        }
    }

    static abstract class Entries<K, V> extends AbstractCollection<Map.Entry<K, V>> {
        Entries() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            multimap().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(@CheckForNull Object r5) {
            if ((r5 instanceof Map.Entry) == false) goto L6;
            Map.Entry r0 = (Map.Entry) r5;
            return multimap().containsEntry(r0.getKey(), r0.getValue());
        L6:
            return false;
        }

        abstract Multimap<K, V> multimap();

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(@CheckForNull Object r5) {
            if ((r5 instanceof Map.Entry) == false) goto L6;
            Map.Entry r0 = (Map.Entry) r5;
            return multimap().remove(r0.getKey(), r0.getValue());
        L6:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return multimap().size();
        }
    }

    static class Keys<K, V> extends AbstractMultiset<K> {
        final Multimap<K, V> multimap;

        Keys(Multimap<K, V> r1) {
            this.multimap = r1;
        }

        @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
        public void clear() {
            this.multimap.clear();
        }

        @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
        public boolean contains(@CheckForNull Object r2) {
            return this.multimap.containsKey(r2);
        }

        @Override // com.google.common.collect.Multiset
        public int count(@CheckForNull Object r3) {
            Collection r0 = (Collection) Maps.safeGet(this.multimap.asMap(), r3);
            if (r0 != null) goto L6;
            return 0;
        L6:
            return r0.size();
        }

        @Override // com.google.common.collect.AbstractMultiset
        int distinctElements() {
            return this.multimap.asMap().size();
        }

        @Override // com.google.common.collect.AbstractMultiset
        Iterator<K> elementIterator() {
            throw new AssertionError("should never be called");
        }

        @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
        public Set<K> elementSet() {
            return this.multimap.keySet();
        }

        @Override // com.google.common.collect.AbstractMultiset
        Iterator<Multiset.Entry<K>> entryIterator() {
            final Iterator<Map.Entry<K, Collection<V>>> r1 = this.multimap.asMap().entrySet().iterator();
            return new C07061(this, r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
        public Iterator<K> iterator() {
            return Maps.keyIterator(this.multimap.entries().iterator());
        }

        @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
        public int remove(@CheckForNull Object r5, int r6) {
            CollectPreconditions.checkNonnegative(r6, "occurrences");
            if (r6 == 0) goto L5;
            Collection r0 = (Collection) Maps.safeGet(this.multimap.asMap(), r5);
            if (r0 != null) goto L10;
            return 0;
        L10:
            int r1 = r0.size();
            if (r6 < r1) goto L13;
            r0.clear();
        L16:
            return r1;
        L13:
            Iterator r2 = r0.iterator();
            int r3 = 0;
        L14:
            if (r3 >= r6) goto L16;
            r2.next();
            r2.remove();
            r3 = r3 + 1;
            goto L14
        L5:
            return count(r5);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
        public int size() {
            return this.multimap.size();
        }
    }

    private static class MapMultimap<K, V> extends AbstractMultimap<K, V> implements SetMultimap<K, V>, Serializable {
        private static final long serialVersionUID = 7845222491160860175L;
        final Map<K, V> map;

        MapMultimap(Map<K, V> r2) {
            this.map = (Map) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.collect.Multimap
        public void clear() {
            this.map.clear();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean containsEntry(@CheckForNull Object r3, @CheckForNull Object r4) {
            return this.map.entrySet().contains(Maps.immutableEntry(r3, r4));
        }

        @Override // com.google.common.collect.Multimap
        public boolean containsKey(@CheckForNull Object r2) {
            return this.map.containsKey(r2);
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean containsValue(@CheckForNull Object r2) {
            return this.map.containsValue(r2);
        }

        @Override // com.google.common.collect.AbstractMultimap
        Map<K, Collection<V>> createAsMap() {
            return new AsMap(this);
        }

        @Override // com.google.common.collect.AbstractMultimap
        Collection<Map.Entry<K, V>> createEntries() {
            throw new AssertionError("unreachable");
        }

        @Override // com.google.common.collect.AbstractMultimap
        Set<K> createKeySet() {
            return this.map.keySet();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Multiset<K> createKeys() {
            return new Keys(this);
        }

        @Override // com.google.common.collect.AbstractMultimap
        Collection<V> createValues() {
            return this.map.values();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection entries() {
            return entries();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public Set<Map.Entry<K, V>> entries() {
            return this.map.entrySet();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Iterator<Map.Entry<K, V>> entryIterator() {
            return this.map.entrySet().iterator();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection get(@ParametricNullness Object r1) {
            return get(r1);
        }

        @Override // com.google.common.collect.Multimap
        public Set<V> get(@ParametricNullness final K r2) {
            return new C07071(this, r2);
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public int hashCode() {
            return this.map.hashCode();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean put(@ParametricNullness K r2, @ParametricNullness V r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean putAll(Multimap<? extends K, ? extends V> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean putAll(@ParametricNullness K r2, Iterable<? extends V> r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean remove(@CheckForNull Object r3, @CheckForNull Object r4) {
            return this.map.entrySet().remove(Maps.immutableEntry(r3, r4));
        }

        @Override // com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection removeAll(@CheckForNull Object r1) {
            return removeAll(r1);
        }

        @Override // com.google.common.collect.Multimap
        public Set<V> removeAll(@CheckForNull Object r3) {
            HashSet r0 = new HashSet(2);
            if (this.map.containsKey(r3) == true) goto L5;
            return r0;
        L5:
            r0.add(this.map.remove(r3));
            return r0;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection replaceValues(@ParametricNullness Object r1, Iterable r2) {
            return replaceValues(r1, r2);
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public Set<V> replaceValues(@ParametricNullness K r2, Iterable<? extends V> r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.Multimap
        public int size() {
            return this.map.size();
        }
    }

    private static final class TransformedEntriesListMultimap<K, V1, V2> extends TransformedEntriesMultimap<K, V1, V2> implements ListMultimap<K, V2> {
        TransformedEntriesListMultimap(ListMultimap<K, V1> r1, Maps.EntryTransformer<? super K, ? super V1, V2> r2) {
            super(r1, r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimaps.TransformedEntriesMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection get(@ParametricNullness Object r1) {
            return get(r1);
        }

        @Override // com.google.common.collect.Multimaps.TransformedEntriesMultimap, com.google.common.collect.Multimap
        public List<V2> get(@ParametricNullness K r2) {
            return transform(r2, this.fromMultimap.get(r2));
        }

        @Override // com.google.common.collect.Multimaps.TransformedEntriesMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection removeAll(@CheckForNull Object r1) {
            return removeAll(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimaps.TransformedEntriesMultimap, com.google.common.collect.Multimap
        public List<V2> removeAll(@CheckForNull Object r2) {
            return transform(r2, this.fromMultimap.removeAll(r2));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimaps.TransformedEntriesMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection replaceValues(@ParametricNullness Object r1, Iterable r2) {
            return replaceValues(r1, r2);
        }

        @Override // com.google.common.collect.Multimaps.TransformedEntriesMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public List<V2> replaceValues(@ParametricNullness K r2, Iterable<? extends V2> r3) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimaps.TransformedEntriesMultimap
        /* bridge */ /* synthetic */ Collection transform(@ParametricNullness Object r1, Collection r2) {
            return transform(r1, r2);
        }

        @Override // com.google.common.collect.Multimaps.TransformedEntriesMultimap
        List<V2> transform(@ParametricNullness K r3, Collection<V1> r4) {
            return Lists.transform((List) r4, Maps.asValueToValueFunction(this.transformer, r3));
        }
    }

    private static class TransformedEntriesMultimap<K, V1, V2> extends AbstractMultimap<K, V2> {
        final Multimap<K, V1> fromMultimap;
        final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;

        TransformedEntriesMultimap(Multimap<K, V1> r2, Maps.EntryTransformer<? super K, ? super V1, V2> r3) {
            this.fromMultimap = (Multimap) Preconditions.checkNotNull(r2);
            this.transformer = (Maps.EntryTransformer) Preconditions.checkNotNull(r3);
        }

        @Override // com.google.common.collect.Multimap
        public void clear() {
            this.fromMultimap.clear();
        }

        @Override // com.google.common.collect.Multimap
        public boolean containsKey(@CheckForNull Object r2) {
            return this.fromMultimap.containsKey(r2);
        }

        @Override // com.google.common.collect.AbstractMultimap
        Map<K, Collection<V2>> createAsMap() {
            return Maps.transformEntries(this.fromMultimap.asMap(), new Multimaps$TransformedEntriesMultimap$$ExternalSyntheticLambda0(this));
        }

        @Override // com.google.common.collect.AbstractMultimap
        Collection<Map.Entry<K, V2>> createEntries() {
            return new AbstractMultimap.Entries(this);
        }

        @Override // com.google.common.collect.AbstractMultimap
        Set<K> createKeySet() {
            return this.fromMultimap.keySet();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Multiset<K> createKeys() {
            return this.fromMultimap.keys();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Collection<V2> createValues() {
            return Collections2.transform(this.fromMultimap.entries(), Maps.asEntryToValueFunction(this.transformer));
        }

        @Override // com.google.common.collect.AbstractMultimap
        Iterator<Map.Entry<K, V2>> entryIterator() {
            return Iterators.transform(this.fromMultimap.entries().iterator(), Maps.asEntryToEntryFunction(this.transformer));
        }

        @Override // com.google.common.collect.Multimap
        public Collection<V2> get(@ParametricNullness K r2) {
            return transform(r2, this.fromMultimap.get(r2));
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean isEmpty() {
            return this.fromMultimap.isEmpty();
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX INFO: renamed from: lambda$createAsMap$0$com-google-common-collect-Multimaps$TransformedEntriesMultimap */
        /* synthetic */ Collection m323x893217c0(Object r2, Collection r3) {
            return transform(r2, r3);
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean put(@ParametricNullness K r2, @ParametricNullness V2 r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean putAll(Multimap<? extends K, ? extends V2> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean putAll(@ParametricNullness K r2, Iterable<? extends V2> r3) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public boolean remove(@CheckForNull Object r2, @CheckForNull Object r3) {
            return get(r2).remove(r3);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimap
        public Collection<V2> removeAll(@CheckForNull Object r2) {
            return transform(r2, this.fromMultimap.removeAll(r2));
        }

        @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
        public Collection<V2> replaceValues(@ParametricNullness K r2, Iterable<? extends V2> r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.Multimap
        public int size() {
            return this.fromMultimap.size();
        }

        Collection<V2> transform(@ParametricNullness K r3, Collection<V1> r4) {
            Function r0 = Maps.asValueToValueFunction(this.transformer, r3);
            if ((r4 instanceof List) == false) goto L7;
            return Lists.transform((List) r4, r0);
        L7:
            return Collections2.transform(r4, r0);
        }
    }

    private static class UnmodifiableListMultimap<K, V> extends UnmodifiableMultimap<K, V> implements ListMultimap<K, V> {
        private static final long serialVersionUID = 0;

        UnmodifiableListMultimap(ListMultimap<K, V> r1) {
            super(r1);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public ListMultimap<K, V> delegate() {
            return (ListMultimap) super.delegate();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public /* bridge */ /* synthetic */ Multimap delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection get(@ParametricNullness Object r1) {
            return get(r1);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public List<V> get(@ParametricNullness K r2) {
            return Collections.unmodifiableList(delegate().get(r2));
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection removeAll(@CheckForNull Object r1) {
            return removeAll(r1);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public List<V> removeAll(@CheckForNull Object r2) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection replaceValues(@ParametricNullness Object r1, Iterable r2) {
            return replaceValues(r1, r2);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public List<V> replaceValues(@ParametricNullness K r2, Iterable<? extends V> r3) {
            throw new UnsupportedOperationException();
        }
    }

    private static class UnmodifiableMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable {
        private static final long serialVersionUID = 0;
        final Multimap<K, V> delegate;

        @CheckForNull
        @LazyInit
        transient Collection<Map.Entry<K, V>> entries;

        @CheckForNull
        @LazyInit
        transient Set<K> keySet;

        @CheckForNull
        @LazyInit
        transient Multiset<K> keys;

        @CheckForNull
        @LazyInit
        transient Map<K, Collection<V>> map;

        @CheckForNull
        @LazyInit
        transient Collection<V> values;

        UnmodifiableMultimap(Multimap<K, V> r2) {
            this.delegate = (Multimap) Preconditions.checkNotNull(r2);
        }

        static /* synthetic */ Collection lambda$asMap$0(Collection r1) {
            return Multimaps.access$100(r1);
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Map<K, Collection<V>> asMap() {
            Map<K, Collection<V>> r0 = this.map;
            if (r0 != null) goto L6;
            Map<K, Collection<V>> r1 = Collections.unmodifiableMap(Maps.transformValues(this.delegate.asMap(), new Multimaps$UnmodifiableMultimap$$ExternalSyntheticLambda0()));
            this.map = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        protected Multimap<K, V> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Collection<Map.Entry<K, V>> entries() {
            Collection<Map.Entry<K, V>> r0 = this.entries;
            if (r0 != null) goto L6;
            Collection<Map.Entry<K, V>> r1 = Multimaps.access$000(this.delegate.entries());
            this.entries = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Collection<V> get(@ParametricNullness K r2) {
            return Multimaps.access$100(this.delegate.get(r2));
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Set<K> keySet() {
            Set<K> r0 = this.keySet;
            if (r0 != null) goto L6;
            Set<K> r1 = Collections.unmodifiableSet(this.delegate.keySet());
            this.keySet = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Multiset<K> keys() {
            Multiset<K> r0 = this.keys;
            if (r0 != null) goto L6;
            Multiset<K> r1 = Multisets.unmodifiableMultiset(this.delegate.keys());
            this.keys = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public boolean put(@ParametricNullness K r2, @ParametricNullness V r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public boolean putAll(Multimap<? extends K, ? extends V> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public boolean putAll(@ParametricNullness K r2, Iterable<? extends V> r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public boolean remove(@CheckForNull Object r2, @CheckForNull Object r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Collection<V> removeAll(@CheckForNull Object r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Collection<V> replaceValues(@ParametricNullness K r2, Iterable<? extends V> r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Collection<V> values() {
            Collection<V> r0 = this.values;
            if (r0 != null) goto L6;
            Collection<V> r1 = Collections.unmodifiableCollection(this.delegate.values());
            this.values = r1;
            return r1;
        L6:
            return r0;
        }
    }

    private static class UnmodifiableSetMultimap<K, V> extends UnmodifiableMultimap<K, V> implements SetMultimap<K, V> {
        private static final long serialVersionUID = 0;

        UnmodifiableSetMultimap(SetMultimap<K, V> r1) {
            super(r1);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public /* bridge */ /* synthetic */ Multimap delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public SetMultimap<K, V> delegate() {
            return (SetMultimap) super.delegate();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection entries() {
            return entries();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Set<Map.Entry<K, V>> entries() {
            return Maps.unmodifiableEntrySet(delegate().entries());
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection get(@ParametricNullness Object r1) {
            return get(r1);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Set<V> get(@ParametricNullness K r2) {
            return Collections.unmodifiableSet(delegate().get(r2));
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection removeAll(@CheckForNull Object r1) {
            return removeAll(r1);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Set<V> removeAll(@CheckForNull Object r2) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection replaceValues(@ParametricNullness Object r1, Iterable r2) {
            return replaceValues(r1, r2);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Set<V> replaceValues(@ParametricNullness K r2, Iterable<? extends V> r3) {
            throw new UnsupportedOperationException();
        }
    }

    private static class UnmodifiableSortedSetMultimap<K, V> extends UnmodifiableSetMultimap<K, V> implements SortedSetMultimap<K, V> {
        private static final long serialVersionUID = 0;

        UnmodifiableSortedSetMultimap(SortedSetMultimap<K, V> r1) {
            super(r1);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public /* bridge */ /* synthetic */ Multimap delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public /* bridge */ /* synthetic */ SetMultimap delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public SortedSetMultimap<K, V> delegate() {
            return (SortedSetMultimap) super.delegate();
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection get(@ParametricNullness Object r1) {
            return get(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Set get(@ParametricNullness Object r1) {
            return get(r1);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public SortedSet<V> get(@ParametricNullness K r2) {
            return Collections.unmodifiableSortedSet(delegate().get(r2));
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection removeAll(@CheckForNull Object r1) {
            return removeAll(r1);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Set removeAll(@CheckForNull Object r1) {
            return removeAll(r1);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public SortedSet<V> removeAll(@CheckForNull Object r2) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Collection replaceValues(@ParametricNullness Object r1, Iterable r2) {
            return replaceValues(r1, r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public /* bridge */ /* synthetic */ Set replaceValues(@ParametricNullness Object r1, Iterable r2) {
            return replaceValues(r1, r2);
        }

        @Override // com.google.common.collect.Multimaps.UnmodifiableSetMultimap, com.google.common.collect.Multimaps.UnmodifiableMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public SortedSet<V> replaceValues(@ParametricNullness K r2, Iterable<? extends V> r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.SortedSetMultimap
        @CheckForNull
        public Comparator<? super V> valueComparator() {
            return delegate().valueComparator();
        }
    }

    private Multimaps() {
    }

    static /* synthetic */ Collection access$000(Collection r1) {
        return unmodifiableEntries(r1);
    }

    static /* synthetic */ Collection access$100(Collection r1) {
        return unmodifiableValueCollection(r1);
    }

    public static <K, V> Map<K, List<V>> asMap(ListMultimap<K, V> r1) {
        return r1.asMap();
    }

    public static <K, V> Map<K, Collection<V>> asMap(Multimap<K, V> r1) {
        return r1.asMap();
    }

    public static <K, V> Map<K, Set<V>> asMap(SetMultimap<K, V> r1) {
        return r1.asMap();
    }

    public static <K, V> Map<K, SortedSet<V>> asMap(SortedSetMultimap<K, V> r1) {
        return r1.asMap();
    }

    static boolean equalsImpl(Multimap<?, ?> r3, @CheckForNull Object r4) {
        if (r4 != r3) goto L6;
        return true;
    L6:
        if ((r4 instanceof Multimap) == true) goto L8;
        return false;
    L8:
        return r3.asMap().equals(((Multimap) r4).asMap());
    }

    public static <K, V> Multimap<K, V> filterEntries(Multimap<K, V> r2, Predicate<? super Map.Entry<K, V>> r3) {
        Preconditions.checkNotNull(r3);
        if ((r2 instanceof SetMultimap) == false) goto L7;
        return filterEntries((SetMultimap) r2, r3);
    L7:
        if ((r2 instanceof FilteredMultimap) == false) goto L10;
        return filterFiltered((FilteredMultimap) r2, r3);
    L10:
        return new FilteredEntryMultimap((Multimap) Preconditions.checkNotNull(r2), r3);
    }

    public static <K, V> SetMultimap<K, V> filterEntries(SetMultimap<K, V> r2, Predicate<? super Map.Entry<K, V>> r3) {
        Preconditions.checkNotNull(r3);
        if ((r2 instanceof FilteredSetMultimap) == false) goto L6;
        return filterFiltered((FilteredSetMultimap) r2, r3);
    L6:
        return new FilteredEntrySetMultimap((SetMultimap) Preconditions.checkNotNull(r2), r3);
    }

    private static <K, V> Multimap<K, V> filterFiltered(FilteredMultimap<K, V> r3, Predicate<? super Map.Entry<K, V>> r4) {
        Predicate r0 = Predicates.and(r3.entryPredicate(), r4);
        return new FilteredEntryMultimap(r3.unfiltered(), r0);
    }

    private static <K, V> SetMultimap<K, V> filterFiltered(FilteredSetMultimap<K, V> r3, Predicate<? super Map.Entry<K, V>> r4) {
        Predicate r0 = Predicates.and(r3.entryPredicate(), r4);
        return new FilteredEntrySetMultimap(r3.unfiltered(), r0);
    }

    public static <K, V> ListMultimap<K, V> filterKeys(ListMultimap<K, V> r4, Predicate<? super K> r5) {
        if ((r4 instanceof FilteredKeyListMultimap) == false) goto L7;
        FilteredKeyListMultimap r0 = (FilteredKeyListMultimap) r4;
        return new FilteredKeyListMultimap(r0.unfiltered(), Predicates.and(r0.keyPredicate, r5));
    L7:
        return new FilteredKeyListMultimap(r4, r5);
    }

    public static <K, V> Multimap<K, V> filterKeys(Multimap<K, V> r4, Predicate<? super K> r5) {
        if ((r4 instanceof SetMultimap) == false) goto L7;
        return filterKeys((SetMultimap) r4, r5);
    L7:
        if ((r4 instanceof ListMultimap) == false) goto L11;
        return filterKeys((ListMultimap) r4, r5);
    L11:
        if ((r4 instanceof FilteredKeyMultimap) == false) goto L15;
        FilteredKeyMultimap r0 = (FilteredKeyMultimap) r4;
        return new FilteredKeyMultimap(r0.unfiltered, Predicates.and(r0.keyPredicate, r5));
    L15:
        if ((r4 instanceof FilteredMultimap) == false) goto L19;
        return filterFiltered((FilteredMultimap) r4, Maps.keyPredicateOnEntries(r5));
    L19:
        return new FilteredKeyMultimap(r4, r5);
    }

    public static <K, V> SetMultimap<K, V> filterKeys(SetMultimap<K, V> r4, Predicate<? super K> r5) {
        if ((r4 instanceof FilteredKeySetMultimap) == false) goto L7;
        FilteredKeySetMultimap r0 = (FilteredKeySetMultimap) r4;
        return new FilteredKeySetMultimap(r0.unfiltered(), Predicates.and(r0.keyPredicate, r5));
    L7:
        if ((r4 instanceof FilteredSetMultimap) == false) goto L11;
        return filterFiltered((FilteredSetMultimap) r4, Maps.keyPredicateOnEntries(r5));
    L11:
        return new FilteredKeySetMultimap(r4, r5);
    }

    public static <K, V> Multimap<K, V> filterValues(Multimap<K, V> r1, Predicate<? super V> r2) {
        return filterEntries(r1, Maps.valuePredicateOnEntries(r2));
    }

    public static <K, V> SetMultimap<K, V> filterValues(SetMultimap<K, V> r1, Predicate<? super V> r2) {
        return filterEntries(r1, Maps.valuePredicateOnEntries(r2));
    }

    public static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> flatteningToMultimap(java.util.function.Function<? super T, ? extends K> r1, java.util.function.Function<? super T, ? extends Stream<? extends V>> r2, java.util.function.Supplier<M> r3) {
        return CollectCollectors.flatteningToMultimap(r1, r2, r3);
    }

    public static <K, V> SetMultimap<K, V> forMap(Map<K, V> r1) {
        return new MapMultimap(r1);
    }

    public static <K, V> ImmutableListMultimap<K, V> index(Iterable<V> r1, Function<? super V, K> r2) {
        return index(r1.iterator(), r2);
    }

    public static <K, V> ImmutableListMultimap<K, V> index(Iterator<V> r3, Function<? super V, K> r4) {
        Preconditions.checkNotNull(r4);
        ImmutableListMultimap.Builder r0 = ImmutableListMultimap.builder();
    L4:
        if (r3.hasNext() == false) goto L7;
        V r1 = r3.next();
        Preconditions.checkNotNull(r1, r3);
        r0.put(r4.apply(r1), r1);
        goto L4
    L7:
        return r0.build();
    }

    public static <K, V, M extends Multimap<K, V>> M invertFrom(Multimap<? extends V, ? extends K> r4, M r5) {
        Preconditions.checkNotNull(r5);
        Iterator<Map.Entry<? extends V, ? extends K>> r0 = r4.entries().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Map.Entry<? extends V, ? extends K> r1 = r0.next();
        r5.put(r1.getValue(), r1.getKey());
        goto L4
    L6:
        return r5;
    }

    public static <K, V> ListMultimap<K, V> newListMultimap(Map<K, Collection<V>> r1, Supplier<? extends List<V>> r2) {
        return new CustomListMultimap(r1, r2);
    }

    public static <K, V> Multimap<K, V> newMultimap(Map<K, Collection<V>> r1, Supplier<? extends Collection<V>> r2) {
        return new CustomMultimap(r1, r2);
    }

    public static <K, V> SetMultimap<K, V> newSetMultimap(Map<K, Collection<V>> r1, Supplier<? extends Set<V>> r2) {
        return new CustomSetMultimap(r1, r2);
    }

    public static <K, V> SortedSetMultimap<K, V> newSortedSetMultimap(Map<K, Collection<V>> r1, Supplier<? extends SortedSet<V>> r2) {
        return new CustomSortedSetMultimap(r1, r2);
    }

    public static <K, V> ListMultimap<K, V> synchronizedListMultimap(ListMultimap<K, V> r1) {
        return Synchronized.listMultimap(r1, null);
    }

    public static <K, V> Multimap<K, V> synchronizedMultimap(Multimap<K, V> r1) {
        return Synchronized.multimap(r1, null);
    }

    public static <K, V> SetMultimap<K, V> synchronizedSetMultimap(SetMultimap<K, V> r1) {
        return Synchronized.setMultimap(r1, null);
    }

    public static <K, V> SortedSetMultimap<K, V> synchronizedSortedSetMultimap(SortedSetMultimap<K, V> r1) {
        return Synchronized.sortedSetMultimap(r1, null);
    }

    public static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> toMultimap(java.util.function.Function<? super T, ? extends K> r1, java.util.function.Function<? super T, ? extends V> r2, java.util.function.Supplier<M> r3) {
        return CollectCollectors.toMultimap(r1, r2, r3);
    }

    public static <K, V1, V2> ListMultimap<K, V2> transformEntries(ListMultimap<K, V1> r1, Maps.EntryTransformer<? super K, ? super V1, V2> r2) {
        return new TransformedEntriesListMultimap(r1, r2);
    }

    public static <K, V1, V2> Multimap<K, V2> transformEntries(Multimap<K, V1> r1, Maps.EntryTransformer<? super K, ? super V1, V2> r2) {
        return new TransformedEntriesMultimap(r1, r2);
    }

    public static <K, V1, V2> ListMultimap<K, V2> transformValues(ListMultimap<K, V1> r2, Function<? super V1, V2> r3) {
        Preconditions.checkNotNull(r3);
        return transformEntries(r2, Maps.asEntryTransformer(r3));
    }

    public static <K, V1, V2> Multimap<K, V2> transformValues(Multimap<K, V1> r2, Function<? super V1, V2> r3) {
        Preconditions.checkNotNull(r3);
        return transformEntries(r2, Maps.asEntryTransformer(r3));
    }

    private static <K, V> Collection<Map.Entry<K, V>> unmodifiableEntries(Collection<Map.Entry<K, V>> r2) {
        if ((r2 instanceof Set) == false) goto L7;
        return Maps.unmodifiableEntrySet((Set) r2);
    L7:
        return new Maps.UnmodifiableEntries(Collections.unmodifiableCollection(r2));
    }

    @Deprecated
    public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ImmutableListMultimap<K, V> r1) {
        return (ListMultimap) Preconditions.checkNotNull(r1);
    }

    public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ListMultimap<K, V> r1) {
        if ((r1 instanceof UnmodifiableListMultimap) == false) goto L5;
    L9:
        return r1;
    L5:
        if ((r1 instanceof ImmutableListMultimap) == true) goto L9;
        return new UnmodifiableListMultimap(r1);
    }

    @Deprecated
    public static <K, V> Multimap<K, V> unmodifiableMultimap(ImmutableMultimap<K, V> r1) {
        return (Multimap) Preconditions.checkNotNull(r1);
    }

    public static <K, V> Multimap<K, V> unmodifiableMultimap(Multimap<K, V> r1) {
        if ((r1 instanceof UnmodifiableMultimap) == false) goto L5;
    L9:
        return r1;
    L5:
        if ((r1 instanceof ImmutableMultimap) == true) goto L9;
        return new UnmodifiableMultimap(r1);
    }

    @Deprecated
    public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(ImmutableSetMultimap<K, V> r1) {
        return (SetMultimap) Preconditions.checkNotNull(r1);
    }

    public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(SetMultimap<K, V> r1) {
        if ((r1 instanceof UnmodifiableSetMultimap) == false) goto L5;
    L9:
        return r1;
    L5:
        if ((r1 instanceof ImmutableSetMultimap) == true) goto L9;
        return new UnmodifiableSetMultimap(r1);
    }

    public static <K, V> SortedSetMultimap<K, V> unmodifiableSortedSetMultimap(SortedSetMultimap<K, V> r1) {
        if ((r1 instanceof UnmodifiableSortedSetMultimap) == false) goto L6;
        return r1;
    L6:
        return new UnmodifiableSortedSetMultimap(r1);
    }

    private static <V> Collection<V> unmodifiableValueCollection(Collection<V> r1) {
        if ((r1 instanceof SortedSet) == false) goto L7;
        return Collections.unmodifiableSortedSet((SortedSet) r1);
    L7:
        if ((r1 instanceof Set) == false) goto L11;
        return Collections.unmodifiableSet((Set) r1);
    L11:
        if ((r1 instanceof List) == false) goto L15;
        return Collections.unmodifiableList((List) r1);
    L15:
        return Collections.unmodifiableCollection(r1);
    }
}
