package com.google.common.collect;

import android.R;
import com.google.common.base.Converter;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Maps {

    private static abstract class AbstractFilteredMap<K, V> extends ViewCachingAbstractMap<K, V> {
        final Predicate<? super Map.Entry<K, V>> predicate;
        final Map<K, V> unfiltered;

        AbstractFilteredMap(Map<K, V> r1, Predicate<? super Map.Entry<K, V>> r2) {
            this.unfiltered = r1;
            this.predicate = r2;
        }

        boolean apply(@CheckForNull Object r4, @ParametricNullness V r5) {
            return this.predicate.apply(Maps.immutableEntry(r4, r5));
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            if (this.unfiltered.containsKey(r2) == true) goto L5;
        L7:
            return false;
        L5:
            if (apply(r2, this.unfiltered.get(r2)) == false) goto L7;
            return true;
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        Collection<V> createValues() {
            return new FilteredMapValues(this, this.unfiltered, this.predicate);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V get(@CheckForNull Object r3) {
            V r0 = this.unfiltered.get(r3);
            if (r0 != null) goto L5;
        L7:
            return null;
        L5:
            if (apply(r3, r0) == false) goto L7;
            return r0;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            return entrySet().isEmpty();
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V put(@ParametricNullness K r2, @ParametricNullness V r3) {
            Preconditions.checkArgument(apply(r2, r3));
            return this.unfiltered.put(r2, r3);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void putAll(Map<? extends K, ? extends V> r5) {
            Iterator<Map.Entry<? extends K, ? extends V>> r0 = r5.entrySet().iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            Map.Entry<? extends K, ? extends V> r1 = r0.next();
            Preconditions.checkArgument(apply(r1.getKey(), r1.getValue()));
            goto L4
        L6:
            this.unfiltered.putAll(r5);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V remove(@CheckForNull Object r2) {
            if (containsKey(r2) == true) goto L7;
            return null;
        L7:
            return this.unfiltered.remove(r2);
        }
    }

    private static class AsMapView<K, V> extends ViewCachingAbstractMap<K, V> {
        final Function<? super K, V> function;
        private final Set<K> set;

        AsMapView(Set<K> r2, Function<? super K, V> r3) {
            this.set = (Set) Preconditions.checkNotNull(r2);
            this.function = (Function) Preconditions.checkNotNull(r3);
        }

        Set<K> backingSet() {
            return this.set;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            backingSet().clear();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            return backingSet().contains(r2);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        protected Set<Map.Entry<K, V>> createEntrySet() {
            return new C1EntrySetImpl(this);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        public Set<K> createKeySet() {
            return Maps.access$200(backingSet());
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        Collection<V> createValues() {
            return Collections2.transform(this.set, this.function);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V get(@CheckForNull Object r3) {
            if (Collections2.safeContains(backingSet(), r3) == true) goto L5;
            return null;
        L5:
            return this.function.apply(r3);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V remove(@CheckForNull Object r3) {
            if (backingSet().remove(r3) == true) goto L5;
            return null;
        L5:
            return this.function.apply(r3);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return backingSet().size();
        }
    }

    private static final class BiMapConverter<A, B> extends Converter<A, B> implements Serializable {
        private static final long serialVersionUID = 0;
        private final BiMap<A, B> bimap;

        BiMapConverter(BiMap<A, B> r2) {
            this.bimap = (BiMap) Preconditions.checkNotNull(r2);
        }

        private static <X, Y> Y convert(BiMap<X, Y> r3, X r4) {
            Y r0 = r3.get(r4);
            if (r0 == null) goto L5;
            boolean r1 = true;
        L6:
            Preconditions.checkArgument(r1, "No non-null mapping present for input: %s", r4);
            return r0;
        L5:
            r1 = false;
            goto L6
        }

        @Override // com.google.common.base.Converter
        protected A doBackward(B r2) {
            return (A) convert(this.bimap.inverse(), r2);
        }

        @Override // com.google.common.base.Converter
        protected B doForward(A r2) {
            return (B) convert(this.bimap, r2);
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof BiMapConverter) == true) goto L5;
            return false;
        L5:
            return this.bimap.equals(((BiMapConverter) r4).bimap);
        }

        public int hashCode() {
            return this.bimap.hashCode();
        }

        public String toString() {
            return "Maps.asConverter(" + this.bimap + ")";
        }
    }

    static abstract class DescendingMap<K, V> extends ForwardingMap<K, V> implements NavigableMap<K, V> {

        @CheckForNull
        @LazyInit
        private transient Comparator<? super K> comparator;

        @CheckForNull
        @LazyInit
        private transient Set<Map.Entry<K, V>> entrySet;

        @CheckForNull
        @LazyInit
        private transient NavigableSet<K> navigableKeySet;

        DescendingMap() {
        }

        private static <T> Ordering<T> reverse(Comparator<T> r1) {
            return Ordering.from(r1).reverse();
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> ceilingEntry(@ParametricNullness K r2) {
            return forward().floorEntry(r2);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K ceilingKey(@ParametricNullness K r2) {
            return forward().floorKey(r2);
        }

        @Override // java.util.SortedMap
        public Comparator<? super K> comparator() {
            Comparator<? super K> r0 = this.comparator;
            if (r0 != null) goto L9;
            Comparator<? super K> r1 = forward().comparator();
            if (r1 != null) goto L7;
            r1 = Ordering.natural();
        L7:
            Ordering r2 = reverse(r1);
            this.comparator = r2;
            return r2;
        L9:
            return r0;
        }

        Set<Map.Entry<K, V>> createEntrySet() {
            return new C1EntrySetImpl(this);
        }

        @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        protected final Map<K, V> delegate() {
            return forward();
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> descendingKeySet() {
            return forward().navigableKeySet();
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> descendingMap() {
            return forward();
        }

        abstract Iterator<Map.Entry<K, V>> entryIterator();

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> r0 = this.entrySet;
            if (r0 != null) goto L6;
            Set<Map.Entry<K, V>> r1 = createEntrySet();
            this.entrySet = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> firstEntry() {
            return forward().lastEntry();
        }

        @Override // java.util.SortedMap
        @ParametricNullness
        public K firstKey() {
            return forward().lastKey();
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> floorEntry(@ParametricNullness K r2) {
            return forward().ceilingEntry(r2);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K floorKey(@ParametricNullness K r2) {
            return forward().ceilingKey(r2);
        }

        abstract NavigableMap<K, V> forward();

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> headMap(@ParametricNullness K r2, boolean r3) {
            return forward().tailMap(r2, r3).descendingMap();
        }

        @Override // java.util.NavigableMap, java.util.SortedMap
        public SortedMap<K, V> headMap(@ParametricNullness K r2) {
            return headMap(r2, false);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> higherEntry(@ParametricNullness K r2) {
            return forward().lowerEntry(r2);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K higherKey(@ParametricNullness K r2) {
            return forward().lowerKey(r2);
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public Set<K> keySet() {
            return navigableKeySet();
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> lastEntry() {
            return forward().firstEntry();
        }

        @Override // java.util.SortedMap
        @ParametricNullness
        public K lastKey() {
            return forward().firstKey();
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> lowerEntry(@ParametricNullness K r2) {
            return forward().higherEntry(r2);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K lowerKey(@ParametricNullness K r2) {
            return forward().higherKey(r2);
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> navigableKeySet() {
            NavigableSet<K> r0 = this.navigableKeySet;
            if (r0 != null) goto L6;
            NavigableKeySet r1 = new NavigableKeySet(this);
            this.navigableKeySet = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> pollFirstEntry() {
            return forward().pollLastEntry();
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> pollLastEntry() {
            return forward().pollFirstEntry();
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> subMap(@ParametricNullness K r2, boolean r3, @ParametricNullness K r4, boolean r5) {
            return forward().subMap(r4, r5, r2, r3).descendingMap();
        }

        @Override // java.util.NavigableMap, java.util.SortedMap
        public SortedMap<K, V> subMap(@ParametricNullness K r3, @ParametricNullness K r4) {
            return subMap(r3, true, r4, false);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> tailMap(@ParametricNullness K r2, boolean r3) {
            return forward().headMap(r2, r3).descendingMap();
        }

        @Override // java.util.NavigableMap, java.util.SortedMap
        public SortedMap<K, V> tailMap(@ParametricNullness K r2) {
            return tailMap(r2, true);
        }

        @Override // com.google.common.collect.ForwardingObject
        public String toString() {
            return standardToString();
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public Collection<V> values() {
            return new Values(this);
        }
    }

    private static abstract enum EntryFunction extends Enum<EntryFunction> implements Function<Map.Entry<?, ?>, Object> {
        private static final /* synthetic */ EntryFunction[] $VALUES = null;
        public static final EntryFunction KEY = null;
        public static final EntryFunction VALUE = null;

        private static /* synthetic */ EntryFunction[] $values() {
            return new EntryFunction[]{KEY, VALUE};
        }

        static {
            final String r1 = "KEY";
            final int r2 = 0;
            KEY = new C06891(r1, r2);
            final String r3 = "VALUE";
            final int r4 = 1;
            VALUE = new C06902(r3, r4);
            $VALUES = $values();
        }

        private EntryFunction(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ EntryFunction(String r1, int r2, C06761 r3) {
            this(r1, r2);
        }

        public static EntryFunction valueOf(String r1) {
            return (EntryFunction) Enum.valueOf(EntryFunction.class, r1);
        }

        public static EntryFunction[] values() {
            return (EntryFunction[]) $VALUES.clone();
        }
    }

    static abstract class EntrySet<K, V> extends Sets.ImprovedAbstractSet<Map.Entry<K, V>> {
        EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            map().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r6) {
            if ((r6 instanceof Map.Entry) == false) goto L11;
            Map.Entry r0 = (Map.Entry) r6;
            Object r2 = r0.getKey();
            Object r3 = Maps.safeGet(map(), r2);
            if (Objects.equal(r3, r0.getValue()) == false) goto L12;
            if (r3 == null) goto L8;
        L9:
            return true;
        L8:
            if (map().containsKey(r2) == true) goto L9;
            return false;
        L12:
            return false;
        L11:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return map().isEmpty();
        }

        abstract Map<K, V> map();

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r4) {
            if (contains(r4) == true) goto L5;
            return false;
        L5:
            if ((r4 instanceof Map.Entry) == true) goto L7;
            return false;
        L7:
            return map().keySet().remove(((Map.Entry) r4).getKey());
        }

        @Override // com.google.common.collect.Sets.ImprovedAbstractSet, java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> r3) {
            return super.removeAll((Collection) Preconditions.checkNotNull(r3));
        L6:
            return Sets.removeAllImpl(this, r3.iterator());
        }

        @Override // com.google.common.collect.Sets.ImprovedAbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> r7) {
            return super.retainAll((Collection) Preconditions.checkNotNull(r7));
        L5:
            HashSet r1 = Sets.newHashSetWithExpectedSize(r7.size());
            Iterator<?> r2 = r7.iterator();
        L7:
            if (r2.hasNext() == false) goto L15;
            Object r3 = r2.next();
            if (contains(r3) == false) goto L7;
            if ((r3 instanceof Map.Entry) == false) goto L7;
            r1.add(((Map.Entry) r3).getKey());
            goto L7
        L15:
            return map().keySet().retainAll(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return map().size();
        }
    }

    public interface EntryTransformer<K, V1, V2> {
        @ParametricNullness
        V2 transformEntry(@ParametricNullness K r1, @ParametricNullness V1 r2);
    }

    static final class FilteredEntryBiMap<K, V> extends FilteredEntryMap<K, V> implements BiMap<K, V> {
        private final BiMap<V, K> inverse;

        FilteredEntryBiMap(BiMap<K, V> r4, Predicate<? super Map.Entry<K, V>> r5) {
            super(r4, r5);
            this.inverse = new FilteredEntryBiMap(r4.inverse(), inversePredicate(r5), this);
        }

        private FilteredEntryBiMap(BiMap<K, V> r1, Predicate<? super Map.Entry<K, V>> r2, BiMap<V, K> r3) {
            super(r1, r2);
            this.inverse = r3;
        }

        private static <K, V> Predicate<Map.Entry<V, K>> inversePredicate(final Predicate<? super Map.Entry<K, V>> r1) {
            return new C06911(r1);
        }

        @Override // com.google.common.collect.BiMap
        @CheckForNull
        public V forcePut(@ParametricNullness K r2, @ParametricNullness V r3) {
            Preconditions.checkArgument(apply(r2, r3));
            return unfiltered().forcePut(r2, r3);
        }

        @Override // com.google.common.collect.BiMap
        public BiMap<V, K> inverse() {
            return this.inverse;
        }

        BiMap<K, V> unfiltered() {
            return (BiMap) this.unfiltered;
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public /* bridge */ /* synthetic */ Collection values() {
            return values();
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public Set<V> values() {
            return this.inverse.keySet();
        }
    }

    static class FilteredEntryMap<K, V> extends AbstractFilteredMap<K, V> {
        final Set<Map.Entry<K, V>> filteredEntrySet;

        private class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
            final /* synthetic */ FilteredEntryMap this$0;

            private EntrySet(FilteredEntryMap r1) {
                this.this$0 = r1;
            }

            /* synthetic */ EntrySet(FilteredEntryMap r1, C06761 r2) {
                this(r1);
            }

            @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
            protected /* bridge */ /* synthetic */ Object delegate() {
                return delegate();
            }

            @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
            protected /* bridge */ /* synthetic */ Collection delegate() {
                return delegate();
            }

            @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
            protected Set<Map.Entry<K, V>> delegate() {
                return this.this$0.filteredEntrySet;
            }

            @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<K, V>> iterator() {
                final Iterator<Map.Entry<K, V>> r1 = this.this$0.filteredEntrySet.iterator();
                return new C06921(this, r1);
            }
        }

        class KeySet extends KeySet<K, V> {
            final /* synthetic */ FilteredEntryMap this$0;

            KeySet(FilteredEntryMap r1) {
                this.this$0 = r1;
                super(r1);
            }

            @Override // com.google.common.collect.Maps.KeySet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(@CheckForNull Object r2) {
                if (this.this$0.containsKey(r2) == false) goto L6;
                this.this$0.unfiltered.remove(r2);
                return true;
            L6:
                return false;
            }

            @Override // com.google.common.collect.Sets.ImprovedAbstractSet, java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean removeAll(Collection<?> r3) {
                return FilteredEntryMap.removeAllKeys(this.this$0.unfiltered, this.this$0.predicate, r3);
            }

            @Override // com.google.common.collect.Sets.ImprovedAbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean retainAll(Collection<?> r3) {
                return FilteredEntryMap.retainAllKeys(this.this$0.unfiltered, this.this$0.predicate, r3);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public Object[] toArray() {
                return Lists.newArrayList(iterator()).toArray();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public <T> T[] toArray(T[] r2) {
                return (T[]) Lists.newArrayList(iterator()).toArray(r2);
            }
        }

        FilteredEntryMap(Map<K, V> r3, Predicate<? super Map.Entry<K, V>> r4) {
            super(r3, r4);
            this.filteredEntrySet = Sets.filter(r3.entrySet(), this.predicate);
        }

        static <K, V> boolean removeAllKeys(Map<K, V> r4, Predicate<? super Map.Entry<K, V>> r5, Collection<?> r6) {
            Iterator<Map.Entry<K, V>> r0 = r4.entrySet().iterator();
            boolean r1 = false;
        L4:
            if (r0.hasNext() == false) goto L11;
            Map.Entry<K, V> r2 = r0.next();
            if (r5.apply(r2) == false) goto L4;
            if (r6.contains(r2.getKey()) == false) goto L4;
            r0.remove();
            r1 = true;
            goto L4
        L11:
            return r1;
        }

        static <K, V> boolean retainAllKeys(Map<K, V> r4, Predicate<? super Map.Entry<K, V>> r5, Collection<?> r6) {
            Iterator<Map.Entry<K, V>> r0 = r4.entrySet().iterator();
            boolean r1 = false;
        L4:
            if (r0.hasNext() == false) goto L11;
            Map.Entry<K, V> r2 = r0.next();
            if (r5.apply(r2) == false) goto L4;
            if (r6.contains(r2.getKey()) == true) goto L4;
            r0.remove();
            r1 = true;
            goto L4
        L11:
            return r1;
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        protected Set<Map.Entry<K, V>> createEntrySet() {
            return new EntrySet(this, null);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        Set<K> createKeySet() {
            return new KeySet(this);
        }
    }

    private static class FilteredEntryNavigableMap<K, V> extends AbstractNavigableMap<K, V> {
        private final Predicate<? super Map.Entry<K, V>> entryPredicate;
        private final Map<K, V> filteredDelegate;
        private final NavigableMap<K, V> unfiltered;

        FilteredEntryNavigableMap(NavigableMap<K, V> r2, Predicate<? super Map.Entry<K, V>> r3) {
            this.unfiltered = (NavigableMap) Preconditions.checkNotNull(r2);
            this.entryPredicate = r3;
            this.filteredDelegate = new FilteredEntryMap(r2, r3);
        }

        static /* synthetic */ Predicate access$500(FilteredEntryNavigableMap r1) {
            return r1.entryPredicate;
        }

        static /* synthetic */ NavigableMap access$600(FilteredEntryNavigableMap r1) {
            return r1.unfiltered;
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public void clear() {
            this.filteredDelegate.clear();
        }

        @Override // java.util.SortedMap
        @CheckForNull
        public Comparator<? super K> comparator() {
            return this.unfiltered.comparator();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            return this.filteredDelegate.containsKey(r2);
        }

        @Override // com.google.common.collect.AbstractNavigableMap
        Iterator<Map.Entry<K, V>> descendingEntryIterator() {
            return Iterators.filter(this.unfiltered.descendingMap().entrySet().iterator(), this.entryPredicate);
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.NavigableMap
        public NavigableMap<K, V> descendingMap() {
            return Maps.filterEntries(this.unfiltered.descendingMap(), this.entryPredicate);
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap
        Iterator<Map.Entry<K, V>> entryIterator() {
            return Iterators.filter(this.unfiltered.entrySet().iterator(), this.entryPredicate);
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map, java.util.SortedMap
        public Set<Map.Entry<K, V>> entrySet() {
            return this.filteredDelegate.entrySet();
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V get(@CheckForNull Object r2) {
            return this.filteredDelegate.get(r2);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> headMap(@ParametricNullness K r3, boolean r4) {
            return Maps.filterEntries(this.unfiltered.headMap(r3, r4), this.entryPredicate);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            return !Iterables.any(this.unfiltered.entrySet(), this.entryPredicate);
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.NavigableMap
        public NavigableSet<K> navigableKeySet() {
            return new C06931(this, this);
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> pollFirstEntry() {
            return (Map.Entry) Iterables.removeFirstMatching(this.unfiltered.entrySet(), this.entryPredicate);
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> pollLastEntry() {
            return (Map.Entry) Iterables.removeFirstMatching(this.unfiltered.descendingMap().entrySet(), this.entryPredicate);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V put(@ParametricNullness K r2, @ParametricNullness V r3) {
            return this.filteredDelegate.put(r2, r3);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void putAll(Map<? extends K, ? extends V> r2) {
            this.filteredDelegate.putAll(r2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V remove(@CheckForNull Object r2) {
            return this.filteredDelegate.remove(r2);
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public int size() {
            return this.filteredDelegate.size();
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> subMap(@ParametricNullness K r3, boolean r4, @ParametricNullness K r5, boolean r6) {
            return Maps.filterEntries(this.unfiltered.subMap(r3, r4, r5, r6), this.entryPredicate);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> tailMap(@ParametricNullness K r3, boolean r4) {
            return Maps.filterEntries(this.unfiltered.tailMap(r3, r4), this.entryPredicate);
        }

        @Override // java.util.AbstractMap, java.util.Map, java.util.SortedMap
        public Collection<V> values() {
            return new FilteredMapValues(this, this.unfiltered, this.entryPredicate);
        }
    }

    private static class FilteredEntrySortedMap<K, V> extends FilteredEntryMap<K, V> implements SortedMap<K, V> {

        class SortedKeySet extends FilteredEntryMap<K, V>.KeySet implements SortedSet<K> {
            final /* synthetic */ FilteredEntrySortedMap this$0;

            SortedKeySet(FilteredEntrySortedMap r1) {
                this.this$0 = r1;
                super(r1);
            }

            @Override // java.util.SortedSet
            @CheckForNull
            public Comparator<? super K> comparator() {
                return this.this$0.sortedMap().comparator();
            }

            @Override // java.util.SortedSet
            @ParametricNullness
            public K first() {
                return (K) this.this$0.firstKey();
            }

            @Override // java.util.SortedSet
            public SortedSet<K> headSet(@ParametricNullness K r2) {
                return (SortedSet) this.this$0.headMap(r2).keySet();
            }

            @Override // java.util.SortedSet
            @ParametricNullness
            public K last() {
                return (K) this.this$0.lastKey();
            }

            @Override // java.util.SortedSet
            public SortedSet<K> subSet(@ParametricNullness K r2, @ParametricNullness K r3) {
                return (SortedSet) this.this$0.subMap(r2, r3).keySet();
            }

            @Override // java.util.SortedSet
            public SortedSet<K> tailSet(@ParametricNullness K r2) {
                return (SortedSet) this.this$0.tailMap(r2).keySet();
            }
        }

        FilteredEntrySortedMap(SortedMap<K, V> r1, Predicate<? super Map.Entry<K, V>> r2) {
            super(r1, r2);
        }

        @Override // java.util.SortedMap
        @CheckForNull
        public Comparator<? super K> comparator() {
            return sortedMap().comparator();
        }

        @Override // com.google.common.collect.Maps.FilteredEntryMap, com.google.common.collect.Maps.ViewCachingAbstractMap
        /* bridge */ /* synthetic */ Set createKeySet() {
            return createKeySet();
        }

        @Override // com.google.common.collect.Maps.FilteredEntryMap, com.google.common.collect.Maps.ViewCachingAbstractMap
        SortedSet<K> createKeySet() {
            return new SortedKeySet(this);
        }

        @Override // java.util.SortedMap
        @ParametricNullness
        public K firstKey() {
            return keySet().iterator().next();
        }

        @Override // java.util.SortedMap
        public SortedMap<K, V> headMap(@ParametricNullness K r4) {
            return new FilteredEntrySortedMap(sortedMap().headMap(r4), this.predicate);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public /* bridge */ /* synthetic */ Set keySet() {
            return keySet();
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public SortedSet<K> keySet() {
            return (SortedSet) super.keySet();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.SortedMap
        @ParametricNullness
        public K lastKey() {
            SortedMap<K, V> r0 = sortedMap();
        L3:
            K r1 = r0.lastKey();
            if (apply(r1, NullnessCasts.uncheckedCastNullableTToT(this.unfiltered.get(r1))) == true) goto L5;
            r0 = sortedMap().headMap(r1);
            goto L3
        L5:
            return r1;
        }

        SortedMap<K, V> sortedMap() {
            return (SortedMap) this.unfiltered;
        }

        @Override // java.util.SortedMap
        public SortedMap<K, V> subMap(@ParametricNullness K r4, @ParametricNullness K r5) {
            return new FilteredEntrySortedMap(sortedMap().subMap(r4, r5), this.predicate);
        }

        @Override // java.util.SortedMap
        public SortedMap<K, V> tailMap(@ParametricNullness K r4) {
            return new FilteredEntrySortedMap(sortedMap().tailMap(r4), this.predicate);
        }
    }

    private static class FilteredKeyMap<K, V> extends AbstractFilteredMap<K, V> {
        final Predicate<? super K> keyPredicate;

        FilteredKeyMap(Map<K, V> r1, Predicate<? super K> r2, Predicate<? super Map.Entry<K, V>> r3) {
            super(r1, r3);
            this.keyPredicate = r2;
        }

        @Override // com.google.common.collect.Maps.AbstractFilteredMap, java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            if (this.unfiltered.containsKey(r2) == true) goto L5;
        L7:
            return false;
        L5:
            if (this.keyPredicate.apply(r2) == false) goto L7;
            return true;
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        protected Set<Map.Entry<K, V>> createEntrySet() {
            return Sets.filter(this.unfiltered.entrySet(), this.predicate);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        Set<K> createKeySet() {
            return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
        }
    }

    private static final class FilteredMapValues<K, V> extends Values<K, V> {
        final Predicate<? super Map.Entry<K, V>> predicate;
        final Map<K, V> unfiltered;

        FilteredMapValues(Map<K, V> r1, Map<K, V> r2, Predicate<? super Map.Entry<K, V>> r3) {
            super(r1);
            this.unfiltered = r2;
            this.predicate = r3;
        }

        @Override // com.google.common.collect.Maps.Values, java.util.AbstractCollection, java.util.Collection
        public boolean remove(@CheckForNull Object r4) {
            Iterator<Map.Entry<K, V>> r0 = this.unfiltered.entrySet().iterator();
        L4:
            if (r0.hasNext() == false) goto L12;
            Map.Entry<K, V> r1 = r0.next();
            if (this.predicate.apply(r1) == false) goto L4;
            if (Objects.equal(r1.getValue(), r4) == false) goto L4;
            r0.remove();
            return true;
        L12:
            return false;
        }

        @Override // com.google.common.collect.Maps.Values, java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> r5) {
            Iterator<Map.Entry<K, V>> r0 = this.unfiltered.entrySet().iterator();
            boolean r1 = false;
        L4:
            if (r0.hasNext() == false) goto L11;
            Map.Entry<K, V> r2 = r0.next();
            if (this.predicate.apply(r2) == false) goto L4;
            if (r5.contains(r2.getValue()) == false) goto L4;
            r0.remove();
            r1 = true;
            goto L4
        L11:
            return r1;
        }

        @Override // com.google.common.collect.Maps.Values, java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> r5) {
            Iterator<Map.Entry<K, V>> r0 = this.unfiltered.entrySet().iterator();
            boolean r1 = false;
        L4:
            if (r0.hasNext() == false) goto L11;
            Map.Entry<K, V> r2 = r0.next();
            if (this.predicate.apply(r2) == false) goto L4;
            if (r5.contains(r2.getValue()) == true) goto L4;
            r0.remove();
            r1 = true;
            goto L4
        L11:
            return r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public Object[] toArray() {
            return Lists.newArrayList(iterator()).toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] r2) {
            return (T[]) Lists.newArrayList(iterator()).toArray(r2);
        }
    }

    static abstract class IteratorBasedAbstractMap<K, V> extends AbstractMap<K, V> {
        IteratorBasedAbstractMap() {
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            Iterators.clear(entryIterator());
        }

        abstract Iterator<Map.Entry<K, V>> entryIterator();

        @Override // java.util.AbstractMap, java.util.Map, java.util.SortedMap
        public Set<Map.Entry<K, V>> entrySet() {
            return new C06941(this);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public abstract int size();
    }

    static class KeySet<K, V> extends Sets.ImprovedAbstractSet<K> {
        final Map<K, V> map;

        KeySet(Map<K, V> r2) {
            this.map = (Map) Preconditions.checkNotNull(r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            map().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r2) {
            return map().containsKey(r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return map().isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return Maps.keyIterator(map().entrySet().iterator());
        }

        Map<K, V> map() {
            return this.map;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r2) {
            if (contains(r2) == false) goto L6;
            map().remove(r2);
            return true;
        L6:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return map().size();
        }
    }

    static class MapDifferenceImpl<K, V> implements MapDifference<K, V> {
        final Map<K, MapDifference.ValueDifference<V>> differences;
        final Map<K, V> onBoth;
        final Map<K, V> onlyOnLeft;
        final Map<K, V> onlyOnRight;

        MapDifferenceImpl(Map<K, V> r2, Map<K, V> r3, Map<K, V> r4, Map<K, MapDifference.ValueDifference<V>> r5) {
            this.onlyOnLeft = Maps.access$100(r2);
            this.onlyOnRight = Maps.access$100(r3);
            this.onBoth = Maps.access$100(r4);
            this.differences = Maps.access$100(r5);
        }

        @Override // com.google.common.collect.MapDifference
        public boolean areEqual() {
            if (this.onlyOnLeft.isEmpty() == true) goto L5;
        L9:
            return false;
        L5:
            if (this.onlyOnRight.isEmpty() == false) goto L9;
            if (this.differences.isEmpty() == false) goto L9;
            return true;
        }

        @Override // com.google.common.collect.MapDifference
        public Map<K, MapDifference.ValueDifference<V>> entriesDiffering() {
            return this.differences;
        }

        @Override // com.google.common.collect.MapDifference
        public Map<K, V> entriesInCommon() {
            return this.onBoth;
        }

        @Override // com.google.common.collect.MapDifference
        public Map<K, V> entriesOnlyOnLeft() {
            return this.onlyOnLeft;
        }

        @Override // com.google.common.collect.MapDifference
        public Map<K, V> entriesOnlyOnRight() {
            return this.onlyOnRight;
        }

        @Override // com.google.common.collect.MapDifference
        public boolean equals(@CheckForNull Object r6) {
            if (r6 != this) goto L6;
            return true;
        L6:
            if ((r6 instanceof MapDifference) == false) goto L18;
            MapDifference r1 = (MapDifference) r6;
            if (entriesOnlyOnLeft().equals(r1.entriesOnlyOnLeft()) == false) goto L17;
            if (entriesOnlyOnRight().equals(r1.entriesOnlyOnRight()) == false) goto L17;
            if (entriesInCommon().equals(r1.entriesInCommon()) == false) goto L17;
            if (entriesDiffering().equals(r1.entriesDiffering()) == false) goto L17;
            return true;
        L17:
            return false;
        L18:
            return false;
        }

        @Override // com.google.common.collect.MapDifference
        public int hashCode() {
            return Objects.hashCode(new Object[]{entriesOnlyOnLeft(), entriesOnlyOnRight(), entriesInCommon(), entriesDiffering()});
        }

        public String toString() {
            if (areEqual() == false) goto L6;
            return "equal";
        L6:
            StringBuilder r0 = new StringBuilder("not equal");
            if (this.onlyOnLeft.isEmpty() == true) goto L10;
            r0.append(": only on left=").append(this.onlyOnLeft);
        L10:
            if (this.onlyOnRight.isEmpty() == true) goto L13;
            r0.append(": only on right=").append(this.onlyOnRight);
        L13:
            if (this.differences.isEmpty() == true) goto L16;
            r0.append(": value differences=").append(this.differences);
        L16:
            return r0.toString();
        }
    }

    private static final class NavigableAsMapView<K, V> extends AbstractNavigableMap<K, V> {
        private final Function<? super K, V> function;
        private final NavigableSet<K> set;

        NavigableAsMapView(NavigableSet<K> r2, Function<? super K, V> r3) {
            this.set = (NavigableSet) Preconditions.checkNotNull(r2);
            this.function = (Function) Preconditions.checkNotNull(r3);
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public void clear() {
            this.set.clear();
        }

        @Override // java.util.SortedMap
        @CheckForNull
        public Comparator<? super K> comparator() {
            return this.set.comparator();
        }

        @Override // com.google.common.collect.AbstractNavigableMap
        Iterator<Map.Entry<K, V>> descendingEntryIterator() {
            return descendingMap().entrySet().iterator();
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.NavigableMap
        public NavigableMap<K, V> descendingMap() {
            return Maps.asMap(this.set.descendingSet(), this.function);
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap
        Iterator<Map.Entry<K, V>> entryIterator() {
            return Maps.asMapEntryIterator(this.set, this.function);
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V get(@CheckForNull Object r3) {
            if (Collections2.safeContains(this.set, r3) == true) goto L5;
            return null;
        L5:
            return this.function.apply(r3);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> headMap(@ParametricNullness K r3, boolean r4) {
            return Maps.asMap(this.set.headSet(r3, r4), this.function);
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.NavigableMap
        public NavigableSet<K> navigableKeySet() {
            return Maps.access$400(this.set);
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public int size() {
            return this.set.size();
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> subMap(@ParametricNullness K r3, boolean r4, @ParametricNullness K r5, boolean r6) {
            return Maps.asMap(this.set.subSet(r3, r4, r5, r6), this.function);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> tailMap(@ParametricNullness K r3, boolean r4) {
            return Maps.asMap(this.set.tailSet(r3, r4), this.function);
        }
    }

    static class NavigableKeySet<K, V> extends SortedKeySet<K, V> implements NavigableSet<K> {
        NavigableKeySet(NavigableMap<K, V> r1) {
            super(r1);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public K ceiling(@ParametricNullness K r2) {
            return map().ceilingKey(r2);
        }

        @Override // java.util.NavigableSet
        public Iterator<K> descendingIterator() {
            return descendingSet().iterator();
        }

        @Override // java.util.NavigableSet
        public NavigableSet<K> descendingSet() {
            return map().descendingKeySet();
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public K floor(@ParametricNullness K r2) {
            return map().floorKey(r2);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<K> headSet(@ParametricNullness K r2, boolean r3) {
            return map().headMap(r2, r3).navigableKeySet();
        }

        @Override // com.google.common.collect.Maps.SortedKeySet, java.util.SortedSet, java.util.NavigableSet
        public SortedSet<K> headSet(@ParametricNullness K r2) {
            return headSet(r2, false);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public K higher(@ParametricNullness K r2) {
            return map().higherKey(r2);
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public K lower(@ParametricNullness K r2) {
            return map().lowerKey(r2);
        }

        @Override // com.google.common.collect.Maps.SortedKeySet, com.google.common.collect.Maps.KeySet
        /* bridge */ /* synthetic */ Map map() {
            return map();
        }

        @Override // com.google.common.collect.Maps.SortedKeySet, com.google.common.collect.Maps.KeySet
        NavigableMap<K, V> map() {
            return (NavigableMap) this.map;
        }

        @Override // com.google.common.collect.Maps.SortedKeySet, com.google.common.collect.Maps.KeySet
        /* bridge */ /* synthetic */ SortedMap map() {
            return map();
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public K pollFirst() {
            return (K) Maps.keyOrNull(map().pollFirstEntry());
        }

        @Override // java.util.NavigableSet
        @CheckForNull
        public K pollLast() {
            return (K) Maps.keyOrNull(map().pollLastEntry());
        }

        @Override // java.util.NavigableSet
        public NavigableSet<K> subSet(@ParametricNullness K r2, boolean r3, @ParametricNullness K r4, boolean r5) {
            return map().subMap(r2, r3, r4, r5).navigableKeySet();
        }

        @Override // com.google.common.collect.Maps.SortedKeySet, java.util.SortedSet, java.util.NavigableSet
        public SortedSet<K> subSet(@ParametricNullness K r3, @ParametricNullness K r4) {
            return subSet(r3, true, r4, false);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<K> tailSet(@ParametricNullness K r2, boolean r3) {
            return map().tailMap(r2, r3).navigableKeySet();
        }

        @Override // com.google.common.collect.Maps.SortedKeySet, java.util.SortedSet, java.util.NavigableSet
        public SortedSet<K> tailSet(@ParametricNullness K r2) {
            return tailSet(r2, true);
        }
    }

    private static class SortedAsMapView<K, V> extends AsMapView<K, V> implements SortedMap<K, V> {
        SortedAsMapView(SortedSet<K> r1, Function<? super K, V> r2) {
            super(r1, r2);
        }

        @Override // com.google.common.collect.Maps.AsMapView
        /* bridge */ /* synthetic */ Set backingSet() {
            return backingSet();
        }

        @Override // com.google.common.collect.Maps.AsMapView
        SortedSet<K> backingSet() {
            return (SortedSet) super.backingSet();
        }

        @Override // java.util.SortedMap
        @CheckForNull
        public Comparator<? super K> comparator() {
            return backingSet().comparator();
        }

        @Override // java.util.SortedMap
        @ParametricNullness
        public K firstKey() {
            return backingSet().first();
        }

        @Override // java.util.SortedMap
        public SortedMap<K, V> headMap(@ParametricNullness K r3) {
            return Maps.asMap(backingSet().headSet(r3), this.function);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap, java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return Maps.access$300(backingSet());
        }

        @Override // java.util.SortedMap
        @ParametricNullness
        public K lastKey() {
            return backingSet().last();
        }

        @Override // java.util.SortedMap
        public SortedMap<K, V> subMap(@ParametricNullness K r3, @ParametricNullness K r4) {
            return Maps.asMap(backingSet().subSet(r3, r4), this.function);
        }

        @Override // java.util.SortedMap
        public SortedMap<K, V> tailMap(@ParametricNullness K r3) {
            return Maps.asMap(backingSet().tailSet(r3), this.function);
        }
    }

    static class SortedKeySet<K, V> extends KeySet<K, V> implements SortedSet<K> {
        SortedKeySet(SortedMap<K, V> r1) {
            super(r1);
        }

        @Override // java.util.SortedSet
        @CheckForNull
        public Comparator<? super K> comparator() {
            return map().comparator();
        }

        @Override // java.util.SortedSet
        @ParametricNullness
        public K first() {
            return map().firstKey();
        }

        public SortedSet<K> headSet(@ParametricNullness K r3) {
            return new SortedKeySet(map().headMap(r3));
        }

        @Override // java.util.SortedSet
        @ParametricNullness
        public K last() {
            return map().lastKey();
        }

        @Override // com.google.common.collect.Maps.KeySet
        /* bridge */ /* synthetic */ Map map() {
            return map();
        }

        @Override // com.google.common.collect.Maps.KeySet
        SortedMap<K, V> map() {
            return (SortedMap) super.map();
        }

        public SortedSet<K> subSet(@ParametricNullness K r3, @ParametricNullness K r4) {
            return new SortedKeySet(map().subMap(r3, r4));
        }

        public SortedSet<K> tailSet(@ParametricNullness K r3) {
            return new SortedKeySet(map().tailMap(r3));
        }
    }

    static class SortedMapDifferenceImpl<K, V> extends MapDifferenceImpl<K, V> implements SortedMapDifference<K, V> {
        SortedMapDifferenceImpl(SortedMap<K, V> r1, SortedMap<K, V> r2, SortedMap<K, V> r3, SortedMap<K, MapDifference.ValueDifference<V>> r4) {
            super(r1, r2, r3, r4);
        }

        @Override // com.google.common.collect.Maps.MapDifferenceImpl, com.google.common.collect.MapDifference
        public /* bridge */ /* synthetic */ Map entriesDiffering() {
            return entriesDiffering();
        }

        @Override // com.google.common.collect.Maps.MapDifferenceImpl, com.google.common.collect.MapDifference
        public SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering() {
            return (SortedMap) super.entriesDiffering();
        }

        @Override // com.google.common.collect.Maps.MapDifferenceImpl, com.google.common.collect.MapDifference
        public /* bridge */ /* synthetic */ Map entriesInCommon() {
            return entriesInCommon();
        }

        @Override // com.google.common.collect.Maps.MapDifferenceImpl, com.google.common.collect.MapDifference
        public SortedMap<K, V> entriesInCommon() {
            return (SortedMap) super.entriesInCommon();
        }

        @Override // com.google.common.collect.Maps.MapDifferenceImpl, com.google.common.collect.MapDifference
        public /* bridge */ /* synthetic */ Map entriesOnlyOnLeft() {
            return entriesOnlyOnLeft();
        }

        @Override // com.google.common.collect.Maps.MapDifferenceImpl, com.google.common.collect.MapDifference
        public SortedMap<K, V> entriesOnlyOnLeft() {
            return (SortedMap) super.entriesOnlyOnLeft();
        }

        @Override // com.google.common.collect.Maps.MapDifferenceImpl, com.google.common.collect.MapDifference
        public /* bridge */ /* synthetic */ Map entriesOnlyOnRight() {
            return entriesOnlyOnRight();
        }

        @Override // com.google.common.collect.Maps.MapDifferenceImpl, com.google.common.collect.MapDifference
        public SortedMap<K, V> entriesOnlyOnRight() {
            return (SortedMap) super.entriesOnlyOnRight();
        }
    }

    static class TransformedEntriesMap<K, V1, V2> extends IteratorBasedAbstractMap<K, V2> {
        final Map<K, V1> fromMap;
        final EntryTransformer<? super K, ? super V1, V2> transformer;

        TransformedEntriesMap(Map<K, V1> r2, EntryTransformer<? super K, ? super V1, V2> r3) {
            this.fromMap = (Map) Preconditions.checkNotNull(r2);
            this.transformer = (EntryTransformer) Preconditions.checkNotNull(r3);
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public void clear() {
            this.fromMap.clear();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            return this.fromMap.containsKey(r2);
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap
        Iterator<Map.Entry<K, V2>> entryIterator() {
            return Iterators.transform(this.fromMap.entrySet().iterator(), Maps.asEntryToEntryFunction(this.transformer));
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V2 get(@CheckForNull Object r4) {
            V1 r0 = this.fromMap.get(r4);
            if (r0 != null) goto L10;
            if (this.fromMap.containsKey(r4) == true) goto L10;
            return null;
        L10:
            return this.transformer.transformEntry(r4, (Object) NullnessCasts.uncheckedCastNullableTToT(r0));
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return this.fromMap.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V2 remove(@CheckForNull Object r3) {
            if (this.fromMap.containsKey(r3) == true) goto L7;
            return null;
        L7:
            return this.transformer.transformEntry(r3, (Object) NullnessCasts.uncheckedCastNullableTToT(this.fromMap.remove(r3)));
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public int size() {
            return this.fromMap.size();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V2> values() {
            return new Values(this);
        }
    }

    private static class TransformedEntriesNavigableMap<K, V1, V2> extends TransformedEntriesSortedMap<K, V1, V2> implements NavigableMap<K, V2> {
        TransformedEntriesNavigableMap(NavigableMap<K, V1> r1, EntryTransformer<? super K, ? super V1, V2> r2) {
            super(r1, r2);
        }

        @CheckForNull
        private Map.Entry<K, V2> transformEntry(@CheckForNull Map.Entry<K, V1> r2) {
            if (r2 != null) goto L5;
            return null;
        L5:
            return Maps.transformEntry(this.transformer, r2);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V2> ceilingEntry(@ParametricNullness K r2) {
            return transformEntry(fromMap().ceilingEntry(r2));
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K ceilingKey(@ParametricNullness K r2) {
            return fromMap().ceilingKey(r2);
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> descendingKeySet() {
            return fromMap().descendingKeySet();
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V2> descendingMap() {
            return Maps.transformEntries(fromMap().descendingMap(), this.transformer);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V2> firstEntry() {
            return transformEntry(fromMap().firstEntry());
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V2> floorEntry(@ParametricNullness K r2) {
            return transformEntry(fromMap().floorEntry(r2));
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K floorKey(@ParametricNullness K r2) {
            return fromMap().floorKey(r2);
        }

        @Override // com.google.common.collect.Maps.TransformedEntriesSortedMap
        protected NavigableMap<K, V1> fromMap() {
            return (NavigableMap) super.fromMap();
        }

        @Override // com.google.common.collect.Maps.TransformedEntriesSortedMap
        protected /* bridge */ /* synthetic */ SortedMap fromMap() {
            return fromMap();
        }

        @Override // com.google.common.collect.Maps.TransformedEntriesSortedMap, java.util.SortedMap, java.util.NavigableMap
        public NavigableMap<K, V2> headMap(@ParametricNullness K r2) {
            return headMap(r2, false);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V2> headMap(@ParametricNullness K r3, boolean r4) {
            return Maps.transformEntries(fromMap().headMap(r3, r4), this.transformer);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Maps.TransformedEntriesSortedMap, java.util.SortedMap, java.util.NavigableMap
        public /* bridge */ /* synthetic */ SortedMap headMap(@ParametricNullness Object r1) {
            return headMap(r1);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V2> higherEntry(@ParametricNullness K r2) {
            return transformEntry(fromMap().higherEntry(r2));
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K higherKey(@ParametricNullness K r2) {
            return fromMap().higherKey(r2);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V2> lastEntry() {
            return transformEntry(fromMap().lastEntry());
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V2> lowerEntry(@ParametricNullness K r2) {
            return transformEntry(fromMap().lowerEntry(r2));
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K lowerKey(@ParametricNullness K r2) {
            return fromMap().lowerKey(r2);
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> navigableKeySet() {
            return fromMap().navigableKeySet();
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V2> pollFirstEntry() {
            return transformEntry(fromMap().pollFirstEntry());
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V2> pollLastEntry() {
            return transformEntry(fromMap().pollLastEntry());
        }

        @Override // com.google.common.collect.Maps.TransformedEntriesSortedMap, java.util.SortedMap, java.util.NavigableMap
        public NavigableMap<K, V2> subMap(@ParametricNullness K r3, @ParametricNullness K r4) {
            return subMap(r3, true, r4, false);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V2> subMap(@ParametricNullness K r3, boolean r4, @ParametricNullness K r5, boolean r6) {
            return Maps.transformEntries(fromMap().subMap(r3, r4, r5, r6), this.transformer);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Maps.TransformedEntriesSortedMap, java.util.SortedMap, java.util.NavigableMap
        public /* bridge */ /* synthetic */ SortedMap subMap(@ParametricNullness Object r1, @ParametricNullness Object r2) {
            return subMap(r1, r2);
        }

        @Override // com.google.common.collect.Maps.TransformedEntriesSortedMap, java.util.SortedMap, java.util.NavigableMap
        public NavigableMap<K, V2> tailMap(@ParametricNullness K r2) {
            return tailMap(r2, true);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V2> tailMap(@ParametricNullness K r3, boolean r4) {
            return Maps.transformEntries(fromMap().tailMap(r3, r4), this.transformer);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.Maps.TransformedEntriesSortedMap, java.util.SortedMap, java.util.NavigableMap
        public /* bridge */ /* synthetic */ SortedMap tailMap(@ParametricNullness Object r1) {
            return tailMap(r1);
        }
    }

    static class TransformedEntriesSortedMap<K, V1, V2> extends TransformedEntriesMap<K, V1, V2> implements SortedMap<K, V2> {
        TransformedEntriesSortedMap(SortedMap<K, V1> r1, EntryTransformer<? super K, ? super V1, V2> r2) {
            super(r1, r2);
        }

        @Override // java.util.SortedMap
        @CheckForNull
        public Comparator<? super K> comparator() {
            return fromMap().comparator();
        }

        @Override // java.util.SortedMap
        @ParametricNullness
        public K firstKey() {
            return fromMap().firstKey();
        }

        protected SortedMap<K, V1> fromMap() {
            return (SortedMap) this.fromMap;
        }

        public SortedMap<K, V2> headMap(@ParametricNullness K r3) {
            return Maps.transformEntries(fromMap().headMap(r3), this.transformer);
        }

        @Override // java.util.SortedMap
        @ParametricNullness
        public K lastKey() {
            return fromMap().lastKey();
        }

        public SortedMap<K, V2> subMap(@ParametricNullness K r3, @ParametricNullness K r4) {
            return Maps.transformEntries(fromMap().subMap(r3, r4), this.transformer);
        }

        public SortedMap<K, V2> tailMap(@ParametricNullness K r3) {
            return Maps.transformEntries(fromMap().tailMap(r3), this.transformer);
        }
    }

    private static class UnmodifiableBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        final BiMap<? extends K, ? extends V> delegate;

        @CheckForNull
        @LazyInit
        BiMap<V, K> inverse;
        final Map<K, V> unmodifiableMap;

        @CheckForNull
        @LazyInit
        transient Set<V> values;

        UnmodifiableBiMap(BiMap<? extends K, ? extends V> r2, @CheckForNull BiMap<V, K> r3) {
            this.unmodifiableMap = Collections.unmodifiableMap(r2);
            this.delegate = r2;
            this.inverse = r3;
        }

        @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        protected Map<K, V> delegate() {
            return this.unmodifiableMap;
        }

        @Override // com.google.common.collect.BiMap
        @CheckForNull
        public V forcePut(@ParametricNullness K r2, @ParametricNullness V r3) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.BiMap
        public BiMap<V, K> inverse() {
            BiMap<V, K> r0 = this.inverse;
            if (r0 != null) goto L6;
            UnmodifiableBiMap r1 = new UnmodifiableBiMap(this.delegate.inverse(), this);
            this.inverse = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public /* bridge */ /* synthetic */ Collection values() {
            return values();
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public Set<V> values() {
            Set<V> r0 = this.values;
            if (r0 != null) goto L6;
            Set<V> r1 = Collections.unmodifiableSet(this.delegate.values());
            this.values = r1;
            return r1;
        L6:
            return r0;
        }
    }

    static class UnmodifiableEntries<K, V> extends ForwardingCollection<Map.Entry<K, V>> {
        private final Collection<Map.Entry<K, V>> entries;

        UnmodifiableEntries(Collection<Map.Entry<K, V>> r1) {
            this.entries = r1;
        }

        @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected Collection<Map.Entry<K, V>> delegate() {
            return this.entries;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return Maps.unmodifiableEntryIterator(this.entries.iterator());
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return standardToArray();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] r2) {
            return (T[]) standardToArray(r2);
        }
    }

    static class UnmodifiableEntrySet<K, V> extends UnmodifiableEntries<K, V> implements Set<Map.Entry<K, V>> {
        UnmodifiableEntrySet(Set<Map.Entry<K, V>> r1) {
            super(r1);
        }

        @Override // java.util.Collection, java.util.Set
        public boolean equals(@CheckForNull Object r2) {
            return Sets.equalsImpl(this, r2);
        }

        @Override // java.util.Collection, java.util.Set
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    static class UnmodifiableNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V>, Serializable {
        private final NavigableMap<K, ? extends V> delegate;

        @CheckForNull
        @LazyInit
        private transient UnmodifiableNavigableMap<K, V> descendingMap;

        UnmodifiableNavigableMap(NavigableMap<K, ? extends V> r1) {
            this.delegate = r1;
        }

        UnmodifiableNavigableMap(NavigableMap<K, ? extends V> r1, UnmodifiableNavigableMap<K, V> r2) {
            this.delegate = r1;
            this.descendingMap = r2;
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> ceilingEntry(@ParametricNullness K r2) {
            return Maps.access$800(this.delegate.ceilingEntry(r2));
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K ceilingKey(@ParametricNullness K r2) {
            return this.delegate.ceilingKey(r2);
        }

        @Override // com.google.common.collect.ForwardingSortedMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingSortedMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Map delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingSortedMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        protected SortedMap<K, V> delegate() {
            return Collections.unmodifiableSortedMap(this.delegate);
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> descendingKeySet() {
            return Sets.unmodifiableNavigableSet(this.delegate.descendingKeySet());
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> descendingMap() {
            UnmodifiableNavigableMap<K, V> r0 = this.descendingMap;
            if (r0 != null) goto L6;
            UnmodifiableNavigableMap<K, V> r1 = new UnmodifiableNavigableMap(this.delegate.descendingMap(), this);
            this.descendingMap = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> firstEntry() {
            return Maps.access$800(this.delegate.firstEntry());
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> floorEntry(@ParametricNullness K r2) {
            return Maps.access$800(this.delegate.floorEntry(r2));
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K floorKey(@ParametricNullness K r2) {
            return this.delegate.floorKey(r2);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> headMap(@ParametricNullness K r2, boolean r3) {
            return Maps.unmodifiableNavigableMap(this.delegate.headMap(r2, r3));
        }

        @Override // com.google.common.collect.ForwardingSortedMap, java.util.SortedMap
        public SortedMap<K, V> headMap(@ParametricNullness K r2) {
            return headMap(r2, false);
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> higherEntry(@ParametricNullness K r2) {
            return Maps.access$800(this.delegate.higherEntry(r2));
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K higherKey(@ParametricNullness K r2) {
            return this.delegate.higherKey(r2);
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public Set<K> keySet() {
            return navigableKeySet();
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> lastEntry() {
            return Maps.access$800(this.delegate.lastEntry());
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public Map.Entry<K, V> lowerEntry(@ParametricNullness K r2) {
            return Maps.access$800(this.delegate.lowerEntry(r2));
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public K lowerKey(@ParametricNullness K r2) {
            return this.delegate.lowerKey(r2);
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> navigableKeySet() {
            return Sets.unmodifiableNavigableSet(this.delegate.navigableKeySet());
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public final Map.Entry<K, V> pollFirstEntry() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.NavigableMap
        @CheckForNull
        public final Map.Entry<K, V> pollLastEntry() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> subMap(@ParametricNullness K r2, boolean r3, @ParametricNullness K r4, boolean r5) {
            return Maps.unmodifiableNavigableMap(this.delegate.subMap(r2, r3, r4, r5));
        }

        @Override // com.google.common.collect.ForwardingSortedMap, java.util.SortedMap
        public SortedMap<K, V> subMap(@ParametricNullness K r3, @ParametricNullness K r4) {
            return subMap(r3, true, r4, false);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> tailMap(@ParametricNullness K r2, boolean r3) {
            return Maps.unmodifiableNavigableMap(this.delegate.tailMap(r2, r3));
        }

        @Override // com.google.common.collect.ForwardingSortedMap, java.util.SortedMap
        public SortedMap<K, V> tailMap(@ParametricNullness K r2) {
            return tailMap(r2, true);
        }
    }

    static class ValueDifferenceImpl<V> implements MapDifference.ValueDifference<V> {

        @ParametricNullness
        private final V left;

        @ParametricNullness
        private final V right;

        private ValueDifferenceImpl(@ParametricNullness V r1, @ParametricNullness V r2) {
            this.left = r1;
            this.right = r2;
        }

        static <V> MapDifference.ValueDifference<V> create(@ParametricNullness V r1, @ParametricNullness V r2) {
            return new ValueDifferenceImpl(r1, r2);
        }

        @Override // com.google.common.collect.MapDifference.ValueDifference
        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof MapDifference.ValueDifference) == false) goto L11;
            MapDifference.ValueDifference r0 = (MapDifference.ValueDifference) r5;
            if (Objects.equal(this.left, r0.leftValue()) == true) goto L7;
            return false;
        L7:
            if (Objects.equal(this.right, r0.rightValue()) == false) goto L13;
            return true;
        L13:
            return false;
        L11:
            return false;
        }

        @Override // com.google.common.collect.MapDifference.ValueDifference
        public int hashCode() {
            return Objects.hashCode(new Object[]{this.left, this.right});
        }

        @Override // com.google.common.collect.MapDifference.ValueDifference
        @ParametricNullness
        public V leftValue() {
            return this.left;
        }

        @Override // com.google.common.collect.MapDifference.ValueDifference
        @ParametricNullness
        public V rightValue() {
            return this.right;
        }

        public String toString() {
            return "(" + this.left + ", " + this.right + ")";
        }
    }

    static class Values<K, V> extends AbstractCollection<V> {
        final Map<K, V> map;

        Values(Map<K, V> r2) {
            this.map = (Map) Preconditions.checkNotNull(r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            map().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(@CheckForNull Object r2) {
            return map().containsValue(r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return map().isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return Maps.valueIterator(map().entrySet().iterator());
        }

        final Map<K, V> map() {
            return this.map;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(@CheckForNull Object r5) {
            return super.remove(r5);
        L5:
            Iterator<Map.Entry<K, V>> r1 = map().entrySet().iterator();
        L7:
            if (r1.hasNext() == false) goto L13;
            Map.Entry<K, V> r2 = r1.next();
            if (Objects.equal(r5, r2.getValue()) == false) goto L7;
            map().remove(r2.getKey());
            return true;
        L13:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> r6) {
            return super.removeAll((Collection) Preconditions.checkNotNull(r6));
        L5:
            HashSet r1 = Sets.newHashSet();
            Iterator<Map.Entry<K, V>> r2 = map().entrySet().iterator();
        L7:
            if (r2.hasNext() == false) goto L13;
            Map.Entry<K, V> r3 = r2.next();
            if (r6.contains(r3.getValue()) == false) goto L7;
            r1.add(r3.getKey());
            goto L7
        L13:
            return map().keySet().removeAll(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> r6) {
            return super.retainAll((Collection) Preconditions.checkNotNull(r6));
        L5:
            HashSet r1 = Sets.newHashSet();
            Iterator<Map.Entry<K, V>> r2 = map().entrySet().iterator();
        L7:
            if (r2.hasNext() == false) goto L13;
            Map.Entry<K, V> r3 = r2.next();
            if (r6.contains(r3.getValue()) == false) goto L7;
            r1.add(r3.getKey());
            goto L7
        L13:
            return map().keySet().retainAll(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return map().size();
        }
    }

    static abstract class ViewCachingAbstractMap<K, V> extends AbstractMap<K, V> {

        @CheckForNull
        @LazyInit
        private transient Set<Map.Entry<K, V>> entrySet;

        @CheckForNull
        @LazyInit
        private transient Set<K> keySet;

        @CheckForNull
        @LazyInit
        private transient Collection<V> values;

        ViewCachingAbstractMap() {
        }

        abstract Set<Map.Entry<K, V>> createEntrySet();

        Set<K> createKeySet() {
            return new KeySet(this);
        }

        Collection<V> createValues() {
            return new Values(this);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> r0 = this.entrySet;
            if (r0 != null) goto L6;
            Set<Map.Entry<K, V>> r1 = createEntrySet();
            this.entrySet = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            Set<K> r0 = this.keySet;
            if (r0 != null) goto L6;
            Set<K> r1 = createKeySet();
            this.keySet = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> values() {
            Collection<V> r0 = this.values;
            if (r0 != null) goto L6;
            Collection<V> r1 = createValues();
            this.values = r1;
            return r1;
        L6:
            return r0;
        }
    }

    private Maps() {
    }

    static /* synthetic */ Map access$100(Map r1) {
        return unmodifiableMap(r1);
    }

    static /* synthetic */ Set access$200(Set r1) {
        return removeOnlySet(r1);
    }

    static /* synthetic */ SortedSet access$300(SortedSet r1) {
        return removeOnlySortedSet(r1);
    }

    static /* synthetic */ NavigableSet access$400(NavigableSet r1) {
        return removeOnlyNavigableSet(r1);
    }

    static /* synthetic */ Map.Entry access$800(Map.Entry r1) {
        return unmodifiableOrNull(r1);
    }

    public static <A, B> Converter<A, B> asConverter(BiMap<A, B> r1) {
        return new BiMapConverter(r1);
    }

    static <K, V1, V2> Function<Map.Entry<K, V1>, Map.Entry<K, V2>> asEntryToEntryFunction(final EntryTransformer<? super K, ? super V1, V2> r1) {
        Preconditions.checkNotNull(r1);
        return new C068013(r1);
    }

    static <K, V1, V2> Function<Map.Entry<K, V1>, V2> asEntryToValueFunction(final EntryTransformer<? super K, ? super V1, V2> r1) {
        Preconditions.checkNotNull(r1);
        return new C067811(r1);
    }

    static <K, V1, V2> EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> r1) {
        Preconditions.checkNotNull(r1);
        return new C06889(r1);
    }

    public static <K, V> Map<K, V> asMap(Set<K> r1, Function<? super K, V> r2) {
        return new AsMapView(r1, r2);
    }

    public static <K, V> NavigableMap<K, V> asMap(NavigableSet<K> r1, Function<? super K, V> r2) {
        return new NavigableAsMapView(r1, r2);
    }

    public static <K, V> SortedMap<K, V> asMap(SortedSet<K> r1, Function<? super K, V> r2) {
        return new SortedAsMapView(r1, r2);
    }

    static <K, V> Iterator<Map.Entry<K, V>> asMapEntryIterator(Set<K> r2, final Function<? super K, V> r3) {
        final Iterator<K> r1 = r2.iterator();
        return new C06823(r1, r3);
    }

    static <K, V1, V2> Function<V1, V2> asValueToValueFunction(final EntryTransformer<? super K, V1, V2> r1, @ParametricNullness final K r2) {
        Preconditions.checkNotNull(r1);
        return new C067710(r1, r2);
    }

    static int capacity(int r4) {
        if (r4 >= 3) goto L7;
        CollectPreconditions.checkNonnegative(r4, "expectedSize");
        return r4 + 1;
    L7:
        if (r4 < 1073741824) goto L9;
        return Integer.MAX_VALUE;
    L9:
        return (int) Math.ceil(((double) r4) / 0.75d);
    }

    static <K, V> boolean containsEntryImpl(Collection<Map.Entry<K, V>> r1, @CheckForNull Object r2) {
        if ((r2 instanceof Map.Entry) == true) goto L7;
        return false;
    L7:
        return r1.contains(unmodifiableEntry((Map.Entry) r2));
    }

    static boolean containsKeyImpl(Map<?, ?> r1, @CheckForNull Object r2) {
        return Iterators.contains(keyIterator(r1.entrySet().iterator()), r2);
    }

    static boolean containsValueImpl(Map<?, ?> r1, @CheckForNull Object r2) {
        return Iterators.contains(valueIterator(r1.entrySet().iterator()), r2);
    }

    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> r2, Map<? extends K, ? extends V> r3) {
        if ((r2 instanceof SortedMap) == false) goto L7;
        return difference((SortedMap) r2, r3);
    L7:
        return difference(r2, r3, Equivalence.equals());
    }

    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> r7, Map<? extends K, ? extends V> r8, Equivalence<? super V> r9) {
        Preconditions.checkNotNull(r9);
        LinkedHashMap r3 = newLinkedHashMap();
        LinkedHashMap r4 = new LinkedHashMap(r8);
        LinkedHashMap r5 = newLinkedHashMap();
        LinkedHashMap r6 = newLinkedHashMap();
        doDifference(r7, r8, r9, r3, r4, r5, r6);
        return new MapDifferenceImpl(r3, r4, r5, r6);
    }

    public static <K, V> SortedMapDifference<K, V> difference(SortedMap<K, ? extends V> r8, Map<? extends K, ? extends V> r9) {
        Preconditions.checkNotNull(r8);
        Preconditions.checkNotNull(r9);
        Comparator r0 = orNaturalOrder(r8.comparator());
        TreeMap r4 = newTreeMap(r0);
        TreeMap r5 = newTreeMap(r0);
        r5.putAll(r9);
        TreeMap r6 = newTreeMap(r0);
        TreeMap r7 = newTreeMap(r0);
        doDifference(r8, r9, Equivalence.equals(), r4, r5, r6, r7);
        return new SortedMapDifferenceImpl(r4, r5, r6, r7);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <K, V> void doDifference(Map<? extends K, ? extends V> r6, Map<? extends K, ? extends V> r7, Equivalence<? super V> r8, Map<K, V> r9, Map<K, V> r10, Map<K, V> r11, Map<K, MapDifference.ValueDifference<V>> r12) {
        Iterator<Map.Entry<? extends K, ? extends V>> r0 = r6.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L14;
        Map.Entry<? extends K, ? extends V> r1 = r0.next();
        K r2 = r1.getKey();
        V r3 = r1.getValue();
        if (r7.containsKey(r2) == true) goto L7;
        r9.put(r2, r3);
        goto L4
    L7:
        R.bool r4 = (Object) NullnessCasts.uncheckedCastNullableTToT(r10.remove(r2));
        if (r8.equivalent(r3, r4) == true) goto L9;
        r12.put(r2, ValueDifferenceImpl.create(r3, r4));
        goto L4
    L9:
        r11.put(r2, r3);
        goto L4
    }

    static boolean equalsImpl(Map<?, ?> r3, @CheckForNull Object r4) {
        if (r3 != r4) goto L6;
        return true;
    L6:
        if ((r4 instanceof Map) == true) goto L8;
        return false;
    L8:
        return r3.entrySet().equals(((Map) r4).entrySet());
    }

    public static <K, V> BiMap<K, V> filterEntries(BiMap<K, V> r1, Predicate<? super Map.Entry<K, V>> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        if ((r1 instanceof FilteredEntryBiMap) == false) goto L6;
        return filterFiltered((FilteredEntryBiMap) r1, r2);
    L6:
        return new FilteredEntryBiMap(r1, r2);
    }

    public static <K, V> Map<K, V> filterEntries(Map<K, V> r2, Predicate<? super Map.Entry<K, V>> r3) {
        Preconditions.checkNotNull(r3);
        if ((r2 instanceof AbstractFilteredMap) == false) goto L6;
        return filterFiltered((AbstractFilteredMap) r2, r3);
    L6:
        return new FilteredEntryMap((Map) Preconditions.checkNotNull(r2), r3);
    }

    public static <K, V> NavigableMap<K, V> filterEntries(NavigableMap<K, V> r2, Predicate<? super Map.Entry<K, V>> r3) {
        Preconditions.checkNotNull(r3);
        if ((r2 instanceof FilteredEntryNavigableMap) == false) goto L6;
        return filterFiltered((FilteredEntryNavigableMap) r2, r3);
    L6:
        return new FilteredEntryNavigableMap((NavigableMap) Preconditions.checkNotNull(r2), r3);
    }

    public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> r2, Predicate<? super Map.Entry<K, V>> r3) {
        Preconditions.checkNotNull(r3);
        if ((r2 instanceof FilteredEntrySortedMap) == false) goto L6;
        return filterFiltered((FilteredEntrySortedMap) r2, r3);
    L6:
        return new FilteredEntrySortedMap((SortedMap) Preconditions.checkNotNull(r2), r3);
    }

    private static <K, V> BiMap<K, V> filterFiltered(FilteredEntryBiMap<K, V> r3, Predicate<? super Map.Entry<K, V>> r4) {
        Predicate r0 = Predicates.and(r3.predicate, r4);
        return new FilteredEntryBiMap(r3.unfiltered(), r0);
    }

    private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> r3, Predicate<? super Map.Entry<K, V>> r4) {
        return new FilteredEntryMap(r3.unfiltered, Predicates.and(r3.predicate, r4));
    }

    private static <K, V> NavigableMap<K, V> filterFiltered(FilteredEntryNavigableMap<K, V> r3, Predicate<? super Map.Entry<K, V>> r4) {
        Predicate r0 = Predicates.and(FilteredEntryNavigableMap.access$500(r3), r4);
        return new FilteredEntryNavigableMap(FilteredEntryNavigableMap.access$600(r3), r0);
    }

    private static <K, V> SortedMap<K, V> filterFiltered(FilteredEntrySortedMap<K, V> r3, Predicate<? super Map.Entry<K, V>> r4) {
        Predicate r0 = Predicates.and(r3.predicate, r4);
        return new FilteredEntrySortedMap(r3.sortedMap(), r0);
    }

    public static <K, V> BiMap<K, V> filterKeys(BiMap<K, V> r1, Predicate<? super K> r2) {
        Preconditions.checkNotNull(r2);
        return filterEntries(r1, keyPredicateOnEntries(r2));
    }

    public static <K, V> Map<K, V> filterKeys(Map<K, V> r3, Predicate<? super K> r4) {
        Preconditions.checkNotNull(r4);
        Predicate r0 = keyPredicateOnEntries(r4);
        if ((r3 instanceof AbstractFilteredMap) == false) goto L6;
        return filterFiltered((AbstractFilteredMap) r3, r0);
    L6:
        return new FilteredKeyMap((Map) Preconditions.checkNotNull(r3), r4, r0);
    }

    public static <K, V> NavigableMap<K, V> filterKeys(NavigableMap<K, V> r1, Predicate<? super K> r2) {
        return filterEntries(r1, keyPredicateOnEntries(r2));
    }

    public static <K, V> SortedMap<K, V> filterKeys(SortedMap<K, V> r1, Predicate<? super K> r2) {
        return filterEntries(r1, keyPredicateOnEntries(r2));
    }

    public static <K, V> BiMap<K, V> filterValues(BiMap<K, V> r1, Predicate<? super V> r2) {
        return filterEntries(r1, valuePredicateOnEntries(r2));
    }

    public static <K, V> Map<K, V> filterValues(Map<K, V> r1, Predicate<? super V> r2) {
        return filterEntries(r1, valuePredicateOnEntries(r2));
    }

    public static <K, V> NavigableMap<K, V> filterValues(NavigableMap<K, V> r1, Predicate<? super V> r2) {
        return filterEntries(r1, valuePredicateOnEntries(r2));
    }

    public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> r1, Predicate<? super V> r2) {
        return filterEntries(r1, valuePredicateOnEntries(r2));
    }

    public static ImmutableMap<String, String> fromProperties(Properties r4) {
        ImmutableMap.Builder r0 = ImmutableMap.builder();
        Enumeration<?> r1 = r4.propertyNames();
    L4:
        if (r1.hasMoreElements() == false) goto L7;
        String r2 = (String) java.util.Objects.requireNonNull(r1.nextElement());
        r0.put(r2, (String) java.util.Objects.requireNonNull(r4.getProperty(r2)));
        goto L4
    L7:
        return r0.buildOrThrow();
    }

    public static <K, V> Map.Entry<K, V> immutableEntry(@ParametricNullness K r1, @ParametricNullness V r2) {
        return new ImmutableEntry(r1, r2);
    }

    public static <K extends Enum<K>, V> ImmutableMap<K, V> immutableEnumMap(Map<K, ? extends V> r8) {
        if ((r8 instanceof ImmutableEnumMap) == true) goto L5;
        Iterator<Map.Entry<K, ? extends V>> r0 = r8.entrySet().iterator();
        if (r0.hasNext() == false) goto L9;
        Map.Entry<K, ? extends V> r1 = r0.next();
        K r2 = r1.getKey();
        V r3 = r1.getValue();
        CollectPreconditions.checkEntryNotNull(r2, r3);
        EnumMap r4 = new EnumMap(Collections.singletonMap(r2, r3));
    L12:
        if (r0.hasNext() == false) goto L15;
        Map.Entry<K, ? extends V> r5 = r0.next();
        K r6 = r5.getKey();
        V r7 = r5.getValue();
        CollectPreconditions.checkEntryNotNull(r6, r7);
        r4.put(r6, r7);
        goto L12
    L15:
        return ImmutableEnumMap.asImmutable(r4);
    L9:
        return ImmutableMap.m231of();
    L5:
        return (ImmutableEnumMap) r8;
    }

    static <E> ImmutableMap<E, Integer> indexMap(Collection<E> r5) {
        ImmutableMap.Builder r0 = new ImmutableMap.Builder(r5.size());
        int r1 = 0;
        Iterator<E> r2 = r5.iterator();
    L4:
        if (r2.hasNext() == false) goto L7;
        int r4 = r1 + 1;
        r0.put(r2.next(), Integer.valueOf(r1));
        r1 = r4;
        goto L4
    L7:
        return r0.buildOrThrow();
    }

    static <K> Function<Map.Entry<K, ?>, K> keyFunction() {
        return EntryFunction.KEY;
    }

    static <K, V> Iterator<K> keyIterator(final Iterator<Map.Entry<K, V>> r1) {
        return new C06761(r1);
    }

    @CheckForNull
    static <K> K keyOrNull(@CheckForNull Map.Entry<K, ?> r1) {
        if (r1 != null) goto L5;
        return null;
    L5:
        return r1.getKey();
    }

    static <K> Predicate<Map.Entry<K, ?>> keyPredicateOnEntries(Predicate<? super K> r1) {
        return Predicates.compose(r1, keyFunction());
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
        return new ConcurrentHashMap();
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> r2) {
        return new EnumMap((Class) Preconditions.checkNotNull(r2));
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> r1) {
        return new EnumMap(r1);
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap();
    }

    public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> r1) {
        return new HashMap(r1);
    }

    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int r2) {
        return new HashMap(capacity(r2));
    }

    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return new IdentityHashMap();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> r1) {
        return new LinkedHashMap(r1);
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMapWithExpectedSize(int r2) {
        return new LinkedHashMap(capacity(r2));
    }

    public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap();
    }

    public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@CheckForNull Comparator<C> r1) {
        return new TreeMap(r1);
    }

    public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> r1) {
        return new TreeMap(r1);
    }

    static <E> Comparator<? super E> orNaturalOrder(@CheckForNull Comparator<? super E> r1) {
        if (r1 == null) goto L5;
        return r1;
    L5:
        return Ordering.natural();
    }

    static <K, V> void putAllImpl(Map<K, V> r4, Map<? extends K, ? extends V> r5) {
        Iterator<Map.Entry<? extends K, ? extends V>> r0 = r5.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Map.Entry<? extends K, ? extends V> r1 = r0.next();
        r4.put(r1.getKey(), r1.getValue());
        goto L4
    }

    static <K, V> boolean removeEntryImpl(Collection<Map.Entry<K, V>> r1, @CheckForNull Object r2) {
        if ((r2 instanceof Map.Entry) == true) goto L7;
        return false;
    L7:
        return r1.remove(unmodifiableEntry((Map.Entry) r2));
    }

    private static <E> NavigableSet<E> removeOnlyNavigableSet(final NavigableSet<E> r1) {
        return new C06856(r1);
    }

    private static <E> Set<E> removeOnlySet(final Set<E> r1) {
        return new C06834(r1);
    }

    private static <E> SortedSet<E> removeOnlySortedSet(final SortedSet<E> r1) {
        return new C06845(r1);
    }

    static boolean safeContainsKey(Map<?, ?> r2, @CheckForNull Object r3) {
        Preconditions.checkNotNull(r2);
        return r2.containsKey(r3);
    L8:
        return false;
    }

    @CheckForNull
    static <V> V safeGet(Map<?, V> r2, @CheckForNull Object r3) {
        Preconditions.checkNotNull(r2);
        return r2.get(r3);
    L8:
        return null;
    }

    @CheckForNull
    static <V> V safeRemove(Map<?, V> r2, @CheckForNull Object r3) {
        Preconditions.checkNotNull(r2);
        return r2.remove(r3);
    L8:
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <K extends Comparable<? super K>, V> NavigableMap<K, V> subMap(NavigableMap<K, V> r7, Range<K> r8) {
        boolean r1 = true;
        if (r7.comparator() == null) goto L16;
        if (r7.comparator() == Ordering.natural()) goto L16;
        if (r8.hasLowerBound() == false) goto L16;
        if (r8.hasUpperBound() == false) goto L16;
        if (r7.comparator().compare(r8.lowerEndpoint(), r8.upperEndpoint()) > 0) goto L13;
        boolean r0 = true;
    L14:
        Preconditions.checkArgument(r0, "map is using a custom comparator which is inconsistent with the natural ordering.");
        goto L16
    L13:
        r0 = false;
    L16:
        if (r8.hasLowerBound() == false) goto L30;
        if (r8.hasUpperBound() == false) goto L30;
        Comparable r2 = r8.lowerEndpoint();
        if (r8.lowerBoundType() != BoundType.CLOSED) goto L22;
        boolean r3 = true;
    L23:
        Comparable r4 = r8.upperEndpoint();
        if (r8.upperBoundType() == BoundType.CLOSED) goto L28;
        r1 = false;
    L28:
        return r7.subMap(r2, r3, r4, r1);
    L22:
        r3 = false;
    L30:
        if (r8.hasLowerBound() == false) goto L38;
        Comparable r5 = r8.lowerEndpoint();
        if (r8.lowerBoundType() == BoundType.CLOSED) goto L36;
        r1 = false;
    L36:
        return r7.tailMap(r5, r1);
    L38:
        if (r8.hasUpperBound() == false) goto L46;
        Comparable r6 = r8.upperEndpoint();
        if (r8.upperBoundType() == BoundType.CLOSED) goto L44;
        r1 = false;
    L44:
        return r7.headMap(r6, r1);
    L46:
        return (NavigableMap) Preconditions.checkNotNull(r7);
    }

    public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> r1) {
        return Synchronized.biMap(r1, null);
    }

    public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> r1) {
        return Synchronized.navigableMap(r1);
    }

    public static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(java.util.function.Function<? super T, ? extends K> r1, java.util.function.Function<? super T, ? extends V> r2) {
        return CollectCollectors.toImmutableEnumMap(r1, r2);
    }

    public static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(java.util.function.Function<? super T, ? extends K> r1, java.util.function.Function<? super T, ? extends V> r2, BinaryOperator<V> r3) {
        return CollectCollectors.toImmutableEnumMap(r1, r2, r3);
    }

    public static <K, V> ImmutableMap<K, V> toMap(Iterable<K> r1, Function<? super K, V> r2) {
        return toMap(r1.iterator(), r2);
    }

    public static <K, V> ImmutableMap<K, V> toMap(Iterator<K> r3, Function<? super K, V> r4) {
        Preconditions.checkNotNull(r4);
        ImmutableMap.Builder r0 = ImmutableMap.builder();
    L4:
        if (r3.hasNext() == false) goto L7;
        K r1 = r3.next();
        r0.put(r1, r4.apply(r1));
        goto L4
    L7:
        return r0.buildKeepingLast();
    }

    static String toStringImpl(Map<?, ?> r6) {
        StringBuilder r0 = Collections2.newStringBuilderForCollection(r6.size()).append('{');
        boolean r1 = true;
        Iterator<Map.Entry<?, ?>> r2 = r6.entrySet().iterator();
    L4:
        if (r2.hasNext() == false) goto L10;
        Map.Entry<?, ?> r3 = r2.next();
        if (r1 == true) goto L8;
        r0.append(", ");
    L8:
        r1 = false;
        r0.append(r3.getKey()).append('=').append(r3.getValue());
        goto L4
    L10:
        return r0.append('}').toString();
    }

    public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> r1, EntryTransformer<? super K, ? super V1, V2> r2) {
        return new TransformedEntriesMap(r1, r2);
    }

    public static <K, V1, V2> NavigableMap<K, V2> transformEntries(NavigableMap<K, V1> r1, EntryTransformer<? super K, ? super V1, V2> r2) {
        return new TransformedEntriesNavigableMap(r1, r2);
    }

    public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> r1, EntryTransformer<? super K, ? super V1, V2> r2) {
        return new TransformedEntriesSortedMap(r1, r2);
    }

    static <V2, K, V1> Map.Entry<K, V2> transformEntry(final EntryTransformer<? super K, ? super V1, V2> r1, final Map.Entry<K, V1> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new C067912(r2, r1);
    }

    public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> r1, Function<? super V1, V2> r2) {
        return transformEntries(r1, asEntryTransformer(r2));
    }

    public static <K, V1, V2> NavigableMap<K, V2> transformValues(NavigableMap<K, V1> r1, Function<? super V1, V2> r2) {
        return transformEntries(r1, asEntryTransformer(r2));
    }

    public static <K, V1, V2> SortedMap<K, V2> transformValues(SortedMap<K, V1> r1, Function<? super V1, V2> r2) {
        return transformEntries(r1, asEntryTransformer(r2));
    }

    public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> r2, Function<? super V, K> r3) {
        if ((r2 instanceof Collection) == false) goto L7;
        return uniqueIndex(r2.iterator(), r3, ImmutableMap.builderWithExpectedSize(((Collection) r2).size()));
    L7:
        return uniqueIndex(r2.iterator(), r3);
    }

    public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> r1, Function<? super V, K> r2) {
        return uniqueIndex(r1, r2, ImmutableMap.builder());
    }

    private static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> r4, Function<? super V, K> r5, ImmutableMap.Builder<K, V> r6) {
        Preconditions.checkNotNull(r5);
    L4:
        if (r4.hasNext() == false) goto L11;
        V r0 = r4.next();
        r6.put(r5.apply(r0), r0);
        goto L4
    L11:
        return r6.buildOrThrow();
    L8:
        e = move-exception;
        throw new IllegalArgumentException(e.getMessage() + ". To index multiple values under a key, use Multimaps.index.");
    }

    public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> r2) {
        return new UnmodifiableBiMap(r2, null);
    }

    static <K, V> Map.Entry<K, V> unmodifiableEntry(final Map.Entry<? extends K, ? extends V> r1) {
        Preconditions.checkNotNull(r1);
        return new C06867(r1);
    }

    static <K, V> UnmodifiableIterator<Map.Entry<K, V>> unmodifiableEntryIterator(final Iterator<Map.Entry<K, V>> r1) {
        return new C06878(r1);
    }

    static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(Set<Map.Entry<K, V>> r2) {
        return new UnmodifiableEntrySet(Collections.unmodifiableSet(r2));
    }

    private static <K, V> Map<K, V> unmodifiableMap(Map<K, ? extends V> r1) {
        if ((r1 instanceof SortedMap) == false) goto L7;
        return Collections.unmodifiableSortedMap((SortedMap) r1);
    L7:
        return Collections.unmodifiableMap(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, ? extends V> r1) {
        Preconditions.checkNotNull(r1);
        if ((r1 instanceof UnmodifiableNavigableMap) == false) goto L7;
        return r1;
    L7:
        return new UnmodifiableNavigableMap(r1);
    }

    @CheckForNull
    private static <K, V> Map.Entry<K, V> unmodifiableOrNull(@CheckForNull Map.Entry<K, ? extends V> r1) {
        if (r1 != null) goto L5;
        return null;
    L5:
        return unmodifiableEntry(r1);
    }

    static <V> Function<Map.Entry<?, V>, V> valueFunction() {
        return EntryFunction.VALUE;
    }

    static <K, V> Iterator<V> valueIterator(final Iterator<Map.Entry<K, V>> r1) {
        return new C06812(r1);
    }

    @CheckForNull
    static <V> V valueOrNull(@CheckForNull Map.Entry<?, V> r1) {
        if (r1 != null) goto L5;
        return null;
    L5:
        return r1.getValue();
    }

    static <V> Predicate<Map.Entry<?, V>> valuePredicateOnEntries(Predicate<? super V> r1) {
        return Predicates.compose(r1, valueFunction());
    }
}
