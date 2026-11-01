package com.google.common.cache;

import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.DoNotMock;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use CacheBuilder.newBuilder().build()")
@ElementTypesAreNonnullByDefault
public interface Cache<K, V> {
    ConcurrentMap<K, V> asMap();

    void cleanUp();

    V get(K r1, Callable<? extends V> r2) throws ExecutionException;

    ImmutableMap<K, V> getAllPresent(Iterable<? extends Object> r1);

    @CheckForNull
    V getIfPresent(Object r1);

    void invalidate(Object r1);

    void invalidateAll();

    void invalidateAll(Iterable<? extends Object> r1);

    void put(K r1, V r2);

    void putAll(Map<? extends K, ? extends V> r1);

    long size();

    CacheStats stats();
}
