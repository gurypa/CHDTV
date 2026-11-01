package com.google.common.cache;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingLoadingCache<K, V> extends ForwardingCache<K, V> implements LoadingCache<K, V> {

    public static abstract class SimpleForwardingLoadingCache<K, V> extends ForwardingLoadingCache<K, V> {
        private final LoadingCache<K, V> delegate;

        protected SimpleForwardingLoadingCache(LoadingCache<K, V> r2) {
            this.delegate = (LoadingCache) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.cache.ForwardingLoadingCache, com.google.common.cache.ForwardingCache, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Cache delegate() {
            return delegate();
        }

        @Override // com.google.common.cache.ForwardingLoadingCache, com.google.common.cache.ForwardingCache, com.google.common.collect.ForwardingObject
        protected final LoadingCache<K, V> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.cache.ForwardingLoadingCache, com.google.common.cache.ForwardingCache, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }
    }

    protected ForwardingLoadingCache() {
    }

    @Override // com.google.common.cache.LoadingCache, com.google.common.base.Function
    public V apply(K r2) {
        return delegate().apply(r2);
    }

    @Override // com.google.common.cache.ForwardingCache, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Cache delegate() {
        return delegate();
    }

    @Override // com.google.common.cache.ForwardingCache, com.google.common.collect.ForwardingObject
    protected abstract LoadingCache<K, V> delegate();

    @Override // com.google.common.cache.ForwardingCache, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.cache.LoadingCache
    public V get(K r2) throws ExecutionException {
        return delegate().get(r2);
    }

    @Override // com.google.common.cache.LoadingCache
    public ImmutableMap<K, V> getAll(Iterable<? extends K> r2) throws ExecutionException {
        return delegate().getAll(r2);
    }

    @Override // com.google.common.cache.LoadingCache
    public V getUnchecked(K r2) {
        return delegate().getUnchecked(r2);
    }

    @Override // com.google.common.cache.LoadingCache
    public void refresh(K r2) {
        delegate().refresh(r2);
    }
}
