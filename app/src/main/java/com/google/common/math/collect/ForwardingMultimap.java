package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingMultimap<K, V> extends ForwardingObject implements Multimap<K, V> {
    protected ForwardingMultimap() {
    }

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public Map<K, Collection<V>> asMap() {
        return delegate().asMap();
    }

    @Override // com.google.common.collect.Multimap
    public void clear() {
        delegate().clear();
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsEntry(@CheckForNull Object r2, @CheckForNull Object r3) {
        return delegate().containsEntry(r2, r3);
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsKey(@CheckForNull Object r2) {
        return delegate().containsKey(r2);
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsValue(@CheckForNull Object r2) {
        return delegate().containsValue(r2);
    }

    @Override // com.google.common.collect.ForwardingObject
    protected abstract Multimap<K, V> delegate();

    @Override // com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.Multimap
    public Collection<Map.Entry<K, V>> entries() {
        return delegate().entries();
    }

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public boolean equals(@CheckForNull Object r2) {
        if (r2 != this) goto L4;
    L7:
        return true;
    L4:
        if (delegate().equals(r2) == true) goto L7;
        return false;
    }

    public Collection<V> get(@ParametricNullness K r2) {
        return delegate().get(r2);
    }

    @Override // com.google.common.collect.Multimap
    public int hashCode() {
        return delegate().hashCode();
    }

    @Override // com.google.common.collect.Multimap
    public boolean isEmpty() {
        return delegate().isEmpty();
    }

    @Override // com.google.common.collect.Multimap
    public Set<K> keySet() {
        return delegate().keySet();
    }

    @Override // com.google.common.collect.Multimap
    public Multiset<K> keys() {
        return delegate().keys();
    }

    @Override // com.google.common.collect.Multimap
    public boolean put(@ParametricNullness K r2, @ParametricNullness V r3) {
        return delegate().put(r2, r3);
    }

    @Override // com.google.common.collect.Multimap
    public boolean putAll(Multimap<? extends K, ? extends V> r2) {
        return delegate().putAll(r2);
    }

    @Override // com.google.common.collect.Multimap
    public boolean putAll(@ParametricNullness K r2, Iterable<? extends V> r3) {
        return delegate().putAll(r2, r3);
    }

    @Override // com.google.common.collect.Multimap
    public boolean remove(@CheckForNull Object r2, @CheckForNull Object r3) {
        return delegate().remove(r2, r3);
    }

    public Collection<V> removeAll(@CheckForNull Object r2) {
        return delegate().removeAll(r2);
    }

    public Collection<V> replaceValues(@ParametricNullness K r2, Iterable<? extends V> r3) {
        return delegate().replaceValues(r2, r3);
    }

    @Override // com.google.common.collect.Multimap
    public int size() {
        return delegate().size();
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> values() {
        return delegate().values();
    }
}
