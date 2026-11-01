package com.google.common.collect;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingConcurrentMap<K, V> extends ForwardingMap<K, V> implements ConcurrentMap<K, V> {
    protected ForwardingConcurrentMap() {
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Map delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected abstract ConcurrentMap<K, V> delegate();

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CheckForNull
    public V putIfAbsent(K r2, V r3) {
        return delegate().putIfAbsent(r2, r3);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean remove(@CheckForNull Object r2, @CheckForNull Object r3) {
        return delegate().remove(r2, r3);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CheckForNull
    public V replace(K r2, V r3) {
        return delegate().replace(r2, r3);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean replace(K r2, V r3, V r4) {
        return delegate().replace(r2, r3, r4);
    }
}
