package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractMultimap<K, V> implements Multimap<K, V> {

    @CheckForNull
    @LazyInit
    private transient Map<K, Collection<V>> asMap;

    @CheckForNull
    @LazyInit
    private transient Collection<Map.Entry<K, V>> entries;

    @CheckForNull
    @LazyInit
    private transient Set<K> keySet;

    @CheckForNull
    @LazyInit
    private transient Multiset<K> keys;

    @CheckForNull
    @LazyInit
    private transient Collection<V> values;

    class Entries extends Multimaps.Entries<K, V> {
        final /* synthetic */ AbstractMultimap this$0;

        Entries(AbstractMultimap r1) {
            this.this$0 = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<Map.Entry<K, V>> iterator() {
            return this.this$0.entryIterator();
        }

        @Override // com.google.common.collect.Multimaps.Entries
        Multimap<K, V> multimap() {
            return this.this$0;
        }
    }

    class EntrySet extends AbstractMultimap<K, V>.Entries implements Set<Map.Entry<K, V>> {
        final /* synthetic */ AbstractMultimap this$0;

        EntrySet(AbstractMultimap r1) {
            this.this$0 = r1;
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

    class Values extends AbstractCollection<V> {
        final /* synthetic */ AbstractMultimap this$0;

        Values(AbstractMultimap r1) {
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
            return this.this$0.valueIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.this$0.size();
        }
    }

    AbstractMultimap() {
    }

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public Map<K, Collection<V>> asMap() {
        Map<K, Collection<V>> r0 = this.asMap;
        if (r0 != null) goto L6;
        Map<K, Collection<V>> r1 = createAsMap();
        this.asMap = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsEntry(@CheckForNull Object r3, @CheckForNull Object r4) {
        Collection<V> r0 = asMap().get(r3);
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (r0.contains(r4) == false) goto L7;
        return true;
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsValue(@CheckForNull Object r4) {
        Iterator<Collection<V>> r0 = asMap().values().iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (r0.next().contains(r4) == false) goto L4;
        return true;
    L10:
        return false;
    }

    abstract Map<K, Collection<V>> createAsMap();

    abstract Collection<Map.Entry<K, V>> createEntries();

    abstract Set<K> createKeySet();

    abstract Multiset<K> createKeys();

    abstract Collection<V> createValues();

    @Override // com.google.common.collect.Multimap
    public Collection<Map.Entry<K, V>> entries() {
        Collection<Map.Entry<K, V>> r0 = this.entries;
        if (r0 != null) goto L6;
        Collection<Map.Entry<K, V>> r1 = createEntries();
        this.entries = r1;
        return r1;
    L6:
        return r0;
    }

    abstract Iterator<Map.Entry<K, V>> entryIterator();

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public boolean equals(@CheckForNull Object r2) {
        return Multimaps.equalsImpl(this, r2);
    }

    @Override // com.google.common.collect.Multimap
    public int hashCode() {
        return asMap().hashCode();
    }

    @Override // com.google.common.collect.Multimap
    public boolean isEmpty() {
        if (size() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // com.google.common.collect.Multimap
    public Set<K> keySet() {
        Set<K> r0 = this.keySet;
        if (r0 != null) goto L6;
        Set<K> r1 = createKeySet();
        this.keySet = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.Multimap
    public Multiset<K> keys() {
        Multiset<K> r0 = this.keys;
        if (r0 != null) goto L6;
        Multiset<K> r1 = createKeys();
        this.keys = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.Multimap
    public boolean put(@ParametricNullness K r2, @ParametricNullness V r3) {
        return get(r2).add(r3);
    }

    @Override // com.google.common.collect.Multimap
    public boolean putAll(Multimap<? extends K, ? extends V> r6) {
        boolean r0 = false;
        Iterator<Map.Entry<? extends K, ? extends V>> r1 = r6.entries().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        Map.Entry<? extends K, ? extends V> r2 = r1.next();
        r0 = r0 | put(r2.getKey(), r2.getValue());
        goto L4
    L6:
        return r0;
    }

    @Override // com.google.common.collect.Multimap
    public boolean putAll(@ParametricNullness K r5, Iterable<? extends V> r6) {
        Preconditions.checkNotNull(r6);
        if ((r6 instanceof Collection) == false) goto L11;
        Collection<? extends V> r0 = (Collection) r6;
        if (r0.isEmpty() == true) goto L10;
        if (get(r5).addAll(r0) == false) goto L10;
        return true;
    L10:
        return false;
    L11:
        Iterator<? extends V> r1 = r6.iterator();
        if (r1.hasNext() == false) goto L17;
        if (Iterators.addAll(get(r5), r1) == false) goto L17;
        return true;
    L17:
        return false;
    }

    @Override // com.google.common.collect.Multimap
    public boolean remove(@CheckForNull Object r3, @CheckForNull Object r4) {
        Collection<V> r0 = asMap().get(r3);
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (r0.remove(r4) == false) goto L7;
        return true;
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> replaceValues(@ParametricNullness K r2, Iterable<? extends V> r3) {
        Preconditions.checkNotNull(r3);
        Collection<V> r0 = removeAll(r2);
        putAll(r2, r3);
        return r0;
    }

    public String toString() {
        return asMap().toString();
    }

    Iterator<V> valueIterator() {
        return Maps.valueIterator(entries().iterator());
    }

    @Override // com.google.common.collect.Multimap
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
