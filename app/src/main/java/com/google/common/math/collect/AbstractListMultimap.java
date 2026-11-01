package com.google.common.collect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractListMultimap<K, V> extends AbstractMapBasedMultimap<K, V> implements ListMultimap<K, V> {
    private static final long serialVersionUID = 6588350623831699109L;

    protected AbstractListMultimap(Map<K, Collection<V>> r1) {
        super(r1);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public Map<K, Collection<V>> asMap() {
        return super.asMap();
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap
    /* bridge */ /* synthetic */ Collection createCollection() {
        return createCollection();
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap
    abstract List<V> createCollection();

    @Override // com.google.common.collect.AbstractMapBasedMultimap
    /* bridge */ /* synthetic */ Collection createUnmodifiableEmptyCollection() {
        return createUnmodifiableEmptyCollection();
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap
    List<V> createUnmodifiableEmptyCollection() {
        return Collections.emptyList();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public boolean equals(@CheckForNull Object r2) {
        return super.equals(r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection get(@ParametricNullness Object r1) {
        return get(r1);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap
    public List<V> get(@ParametricNullness K r2) {
        return (List) super.get(r2);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public boolean put(@ParametricNullness K r2, @ParametricNullness V r3) {
        return super.put(r2, r3);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection removeAll(@CheckForNull Object r1) {
        return removeAll(r1);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap
    public List<V> removeAll(@CheckForNull Object r2) {
        return (List) super.removeAll(r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection replaceValues(@ParametricNullness Object r1, Iterable r2) {
        return replaceValues(r1, r2);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public List<V> replaceValues(@ParametricNullness K r2, Iterable<? extends V> r3) {
        return (List) super.replaceValues(r2, r3);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap
    <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> r2) {
        return Collections.unmodifiableList((List) r2);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap
    Collection<V> wrapCollection(@ParametricNullness K r3, Collection<V> r4) {
        return wrapList(r3, (List) r4, null);
    }
}
