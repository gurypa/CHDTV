package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class FilteredKeyMultimap<K, V> extends AbstractMultimap<K, V> implements FilteredMultimap<K, V> {
    final Predicate<? super K> keyPredicate;
    final Multimap<K, V> unfiltered;

    static class AddRejectingList<K, V> extends ForwardingList<V> {

        @ParametricNullness
        final K key;

        AddRejectingList(@ParametricNullness K r1) {
            this.key = r1;
        }

        @Override // com.google.common.collect.ForwardingList, java.util.List
        public void add(int r4, @ParametricNullness V r5) {
            Preconditions.checkPositionIndex(r4, 0);
            throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Queue
        public boolean add(@ParametricNullness V r2) {
            add(0, r2);
            return true;
        }

        @Override // com.google.common.collect.ForwardingList, java.util.List
        public boolean addAll(int r4, Collection<? extends V> r5) {
            Preconditions.checkNotNull(r5);
            Preconditions.checkPositionIndex(r4, 0);
            throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean addAll(Collection<? extends V> r2) {
            addAll(0, r2);
            return true;
        }

        @Override // com.google.common.collect.ForwardingList, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingList, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Collection delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingList, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected List<V> delegate() {
            return Collections.emptyList();
        }
    }

    static class AddRejectingSet<K, V> extends ForwardingSet<V> {

        @ParametricNullness
        final K key;

        AddRejectingSet(@ParametricNullness K r1) {
            this.key = r1;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Queue
        public boolean add(@ParametricNullness V r4) {
            throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean addAll(Collection<? extends V> r4) {
            Preconditions.checkNotNull(r4);
            throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
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
        protected Set<V> delegate() {
            return Collections.emptySet();
        }
    }

    class Entries extends ForwardingCollection<Map.Entry<K, V>> {
        final /* synthetic */ FilteredKeyMultimap this$0;

        Entries(FilteredKeyMultimap r1) {
            this.this$0 = r1;
        }

        @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected Collection<Map.Entry<K, V>> delegate() {
            return Collections2.filter(this.this$0.unfiltered.entries(), this.this$0.entryPredicate());
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean remove(@CheckForNull Object r5) {
            if ((r5 instanceof Map.Entry) == false) goto L10;
            Map.Entry r0 = (Map.Entry) r5;
            if (this.this$0.unfiltered.containsKey(r0.getKey()) == true) goto L7;
            return false;
        L7:
            if (this.this$0.keyPredicate.apply((Object) r0.getKey()) == true) goto L9;
            return false;
        L9:
            return this.this$0.unfiltered.remove(r0.getKey(), r0.getValue());
        L10:
            return false;
        }
    }

    FilteredKeyMultimap(Multimap<K, V> r2, Predicate<? super K> r3) {
        this.unfiltered = (Multimap) Preconditions.checkNotNull(r2);
        this.keyPredicate = (Predicate) Preconditions.checkNotNull(r3);
    }

    @Override // com.google.common.collect.Multimap
    public void clear() {
        keySet().clear();
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsKey(@CheckForNull Object r3) {
        if (this.unfiltered.containsKey(r3) == true) goto L5;
        return false;
    L5:
        return this.keyPredicate.apply(r3);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Map<K, Collection<V>> createAsMap() {
        return Maps.filterKeys(this.unfiltered.asMap(), this.keyPredicate);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Collection<Map.Entry<K, V>> createEntries() {
        return new Entries(this);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Set<K> createKeySet() {
        return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
    }

    @Override // com.google.common.collect.AbstractMultimap
    Multiset<K> createKeys() {
        return Multisets.filter(this.unfiltered.keys(), this.keyPredicate);
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
        return Maps.keyPredicateOnEntries(this.keyPredicate);
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> get(@ParametricNullness K r2) {
        if (this.keyPredicate.apply(r2) == false) goto L7;
        return this.unfiltered.get(r2);
    L7:
        if ((this.unfiltered instanceof SetMultimap) == false) goto L11;
        return new AddRejectingSet(r2);
    L11:
        return new AddRejectingList(r2);
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> removeAll(@CheckForNull Object r2) {
        if (containsKey(r2) == false) goto L6;
        return this.unfiltered.removeAll(r2);
    L6:
        return unmodifiableEmptyCollection();
    }

    @Override // com.google.common.collect.Multimap
    public int size() {
        int r0 = 0;
        Iterator<Collection<V>> r1 = asMap().values().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0 = r0 + r1.next().size();
        goto L4
    L6:
        return r0;
    }

    public Multimap<K, V> unfiltered() {
        return this.unfiltered;
    }

    Collection<V> unmodifiableEmptyCollection() {
        if ((this.unfiltered instanceof SetMultimap) == false) goto L7;
        return Collections.emptySet();
    L7:
        return Collections.emptyList();
    }
}
