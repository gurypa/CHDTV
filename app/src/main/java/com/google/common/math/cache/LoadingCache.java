package com.google.common.cache;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface LoadingCache<K, V> extends Cache<K, V>, Function<K, V> {
    @Override // com.google.common.base.Function
    @Deprecated
    V apply(K r1);

    @Override // com.google.common.cache.Cache
    ConcurrentMap<K, V> asMap();

    V get(K r1) throws ExecutionException;

    ImmutableMap<K, V> getAll(Iterable<? extends K> r1) throws ExecutionException;

    V getUnchecked(K r1);

    void refresh(K r1);
}
