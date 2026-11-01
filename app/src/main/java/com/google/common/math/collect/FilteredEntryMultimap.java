package com.google.common.collect;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class FilteredEntryMultimap<K, V> extends AbstractMultimap<K, V> implements FilteredMultimap<K, V> {
    final Predicate<? super Map.Entry<K, V>> predicate;
    final Multimap<K, V> unfiltered;

    class AsMap extends Maps.ViewCachingAbstractMap<K, Collection<V>> {
        final /* synthetic */ FilteredEntryMultimap this$0;

        AsMap(FilteredEntryMultimap r1) {
            this.this$0 = r1;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            if (get(r2) == null) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        Set<Map.Entry<K, Collection<V>>> createEntrySet() {
            return new C1EntrySetImpl(this);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        Set<K> createKeySet() {
            return new C1KeySetImpl(this);
        }

        @Override // com.google.common.collect.Maps.ViewCachingAbstractMap
        Collection<Collection<V>> createValues() {
            return new C1ValuesImpl(this);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object get(@CheckForNull Object r1) {
            return get(r1);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public Collection<V> get(@CheckForNull Object r6) {
            Collection<V> r0 = this.this$0.unfiltered.asMap().get(r6);
            if (r0 != null) goto L5;
            return null;
        L5:
            Collection<V> r1 = FilteredEntryMultimap.filterCollection(r0, new ValuePredicate(this.this$0, r6));
            if (r1.isEmpty() == false) goto L9;
            return null;
        L9:
            return r1;
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object remove(@CheckForNull Object r1) {
            return remove(r1);
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public Collection<V> remove(@CheckForNull Object r8) {
            Collection<V> r0 = this.this$0.unfiltered.asMap().get(r8);
            if (r0 != null) goto L5;
            return null;
        L5:
            ArrayList r3 = Lists.newArrayList();
            Iterator<V> r4 = r0.iterator();
        L7:
            if (r4.hasNext() == false) goto L13;
            V r5 = r4.next();
            if (FilteredEntryMultimap.access$000(this.this$0, r8, r5) == false) goto L7;
            r4.remove();
            r3.add(r5);
            goto L7
        L13:
            if (r3.isEmpty() == false) goto L16;
            return null;
        L16:
            if ((this.this$0.unfiltered instanceof SetMultimap) == false) goto L20;
            return Collections.unmodifiableSet(Sets.newLinkedHashSet(r3));
        L20:
            return Collections.unmodifiableList(r3);
        }
    }

    class Keys extends Multimaps.Keys<K, V> {
        final /* synthetic */ FilteredEntryMultimap this$0;

        /* JADX INFO: renamed from: com.google.common.collect.FilteredEntryMultimap$Keys$1 */
        class C06211 extends Multisets.EntrySet<K> {
            final /* synthetic */ Keys this$1;

            C06211(Keys r1) {
                this.this$1 = r1;
            }

            static /* synthetic */ boolean lambda$removeEntriesIf$0(Predicate r2, Map.Entry r3) {
                return r2.apply(Multisets.immutableEntry(r3.getKey(), ((Collection) r3.getValue()).size()));
            }

            private boolean removeEntriesIf(final Predicate<? super Multiset.Entry<K>> r3) {
                return this.this$1.this$0.removeEntriesIf(new FilteredEntryMultimap$Keys$1$$ExternalSyntheticLambda0(r3));
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Multiset.Entry<K>> iterator() {
                return this.this$1.entryIterator();
            }

            @Override // com.google.common.collect.Multisets.EntrySet
            Multiset<K> multiset() {
                return this.this$1;
            }

            @Override // com.google.common.collect.Sets.ImprovedAbstractSet, java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean removeAll(Collection<?> r2) {
                return removeEntriesIf(Predicates.m186in(r2));
            }

            @Override // com.google.common.collect.Sets.ImprovedAbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean retainAll(Collection<?> r2) {
                return removeEntriesIf(Predicates.not(Predicates.m186in(r2)));
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return this.this$1.this$0.keySet().size();
            }
        }

        Keys(FilteredEntryMultimap r1) {
            this.this$0 = r1;
            super(r1);
        }

        @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
        public Set<Multiset.Entry<K>> entrySet() {
            return new C06211(this);
        }

        @Override // com.google.common.collect.Multimaps.Keys, com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
        public int remove(@CheckForNull Object r7, int r8) {
            CollectPreconditions.checkNonnegative(r8, "occurrences");
            if (r8 == 0) goto L5;
            Collection<V> r0 = this.this$0.unfiltered.asMap().get(r7);
            if (r0 != null) goto L10;
            return 0;
        L10:
            int r2 = 0;
            Iterator<V> r3 = r0.iterator();
        L12:
            if (r3.hasNext() == false) goto L19;
            V r4 = r3.next();
            if (FilteredEntryMultimap.access$000(this.this$0, r7, r4) == false) goto L12;
            r2 = r2 + 1;
            if (r2 > r8) goto L12;
            r3.remove();
            goto L12
        L19:
            return r2;
        L5:
            return count(r7);
        }
    }

    final class ValuePredicate implements Predicate<V> {

        @ParametricNullness
        private final K key;
        final /* synthetic */ FilteredEntryMultimap this$0;

        ValuePredicate(@ParametricNullness FilteredEntryMultimap r1, K r2) {
            this.this$0 = r1;
            this.key = r2;
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@ParametricNullness V r3) {
            return FilteredEntryMultimap.access$000(this.this$0, this.key, r3);
        }
    }

    FilteredEntryMultimap(Multimap<K, V> r2, Predicate<? super Map.Entry<K, V>> r3) {
        this.unfiltered = (Multimap) Preconditions.checkNotNull(r2);
        this.predicate = (Predicate) Preconditions.checkNotNull(r3);
    }

    static /* synthetic */ boolean access$000(FilteredEntryMultimap r1, Object r2, Object r3) {
        return r1.satisfies(r2, r3);
    }

    static <E> Collection<E> filterCollection(Collection<E> r1, Predicate<? super E> r2) {
        if ((r1 instanceof Set) == false) goto L7;
        return Sets.filter((Set) r1, r2);
    L7:
        return Collections2.filter(r1, r2);
    }

    private boolean satisfies(@ParametricNullness K r3, @ParametricNullness V r4) {
        return this.predicate.apply(Maps.immutableEntry(r3, r4));
    }

    @Override // com.google.common.collect.Multimap
    public void clear() {
        entries().clear();
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsKey(@CheckForNull Object r2) {
        if (asMap().get(r2) == null) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // com.google.common.collect.AbstractMultimap
    Map<K, Collection<V>> createAsMap() {
        return new AsMap(this);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Collection<Map.Entry<K, V>> createEntries() {
        return filterCollection(this.unfiltered.entries(), this.predicate);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Set<K> createKeySet() {
        return asMap().keySet();
    }

    @Override // com.google.common.collect.AbstractMultimap
    Multiset<K> createKeys() {
        return new Keys(this);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Collection<V> createValues() {
        return new FilteredMultimapValues(this);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Iterator<Map.Entry<K, V>> entryIterator() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.FilteredMultimap
    public Predicate<? super Map.Entry<K, V>> entryPredicate() {
        return this.predicate;
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> get(@ParametricNullness K r3) {
        return filterCollection(this.unfiltered.get(r3), new ValuePredicate(this, r3));
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> removeAll(@CheckForNull Object r3) {
        return (Collection) MoreObjects.firstNonNull(asMap().remove(r3), unmodifiableEmptyCollection());
    }

    boolean removeEntriesIf(Predicate<? super Map.Entry<K, Collection<V>>> r8) {
        Iterator<Map.Entry<K, Collection<V>>> r0 = this.unfiltered.asMap().entrySet().iterator();
        boolean r1 = false;
    L4:
        if (r0.hasNext() == false) goto L15;
        Map.Entry<K, Collection<V>> r2 = r0.next();
        K r3 = r2.getKey();
        Collection r4 = filterCollection(r2.getValue(), new ValuePredicate(this, r3));
        if (r4.isEmpty() == true) goto L4;
        if (r8.apply(Maps.immutableEntry(r3, r4)) == false) goto L4;
        if (r4.size() != r2.getValue().size()) goto L12;
        r0.remove();
    L13:
        r1 = true;
        goto L4
    L12:
        r4.clear();
        goto L13
    L15:
        return r1;
    }

    @Override // com.google.common.collect.Multimap
    public int size() {
        return entries().size();
    }

    @Override // com.google.common.collect.FilteredMultimap
    public Multimap<K, V> unfiltered() {
        return this.unfiltered;
    }

    Collection<V> unmodifiableEmptyCollection() {
        if ((this.unfiltered instanceof SetMultimap) == false) goto L6;
        return Collections.emptySet();
    L6:
        return Collections.emptyList();
    }
}
