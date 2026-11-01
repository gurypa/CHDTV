package com.google.common.cache;

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingObject;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingCache<K, V> extends ForwardingObject implements Cache<K, V> {

    public static abstract class SimpleForwardingCache<K, V> extends ForwardingCache<K, V> {
        private final Cache<K, V> delegate;

        protected SimpleForwardingCache(Cache<K, V> r2) {
            this.delegate = (Cache) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.cache.ForwardingCache, com.google.common.collect.ForwardingObject
        protected final Cache<K, V> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.cache.ForwardingCache, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }
    }

    protected ForwardingCache() {
    }

    @Override // com.google.common.cache.Cache
    public ConcurrentMap<K, V> asMap() {
        return delegate().asMap();
    }

    @Override // com.google.common.cache.Cache
    public void cleanUp() {
        delegate().cleanUp();
    }

    @Override // com.google.common.collect.ForwardingObject
    protected abstract Cache<K, V> delegate();

    @Override // com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.cache.Cache
    public V get(K r2, Callable<? extends V> r3) throws ExecutionException {
        return delegate().get(r2, r3);
    }

    @Override // com.google.common.cache.Cache
    public ImmutableMap<K, V> getAllPresent(Iterable<? extends Object> r2) {
        return delegate().getAllPresent(r2);
    }

    @Override // com.google.common.cache.Cache
    @CheckForNull
    public V getIfPresent(Object r2) {
        return delegate().getIfPresent(r2);
    }

    @Override // com.google.common.cache.Cache
    public void invalidate(Object r2) {
        delegate().invalidate(r2);
    }

    @Override // com.google.common.cache.Cache
    public void invalidateAll() {
        delegate().invalidateAll();
    }

    @Override // com.google.common.cache.Cache
    public void invalidateAll(Iterable<? extends Object> r2) {
        delegate().invalidateAll(r2);
    }

    @Override // com.google.common.cache.Cache
    public void put(K r2, V r3) {
        delegate().put(r2, r3);
    }

    @Override // com.google.common.cache.Cache
    public void putAll(Map<? extends K, ? extends V> r2) {
        delegate().putAll(r2);
    }

    @Override // com.google.common.cache.Cache
    public long size() {
        return delegate().size();
    }

    @Override // com.google.common.cache.Cache
    public CacheStats stats() {
        return delegate().stats();
    }
}
