package com.google.common.graph;

import com.google.common.base.Preconditions;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class MapRetrievalCache<K, V> extends MapIteratorCache<K, V> {

    @CheckForNull
    private volatile transient CacheEntry<K, V> cacheEntry1;

    @CheckForNull
    private volatile transient CacheEntry<K, V> cacheEntry2;

    private static final class CacheEntry<K, V> {
        final K key;
        final V value;

        CacheEntry(K r1, V r2) {
            this.key = r1;
            this.value = r2;
        }
    }

    MapRetrievalCache(Map<K, V> r1) {
        super(r1);
    }

    private void addToCache(CacheEntry<K, V> r2) {
        this.cacheEntry2 = this.cacheEntry1;
        this.cacheEntry1 = r2;
    }

    private void addToCache(K r2, V r3) {
        addToCache(new CacheEntry(r2, r3));
    }

    @Override // com.google.common.graph.MapIteratorCache
    void clearCache() {
        super.clearCache();
        this.cacheEntry1 = null;
        this.cacheEntry2 = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.MapIteratorCache
    @CheckForNull
    V get(Object r2) {
        Preconditions.checkNotNull(r2);
        V r0 = getIfCached(r2);
        if (r0 == null) goto L5;
        return r0;
    L5:
        V r1 = getWithoutCaching(r2);
        if (r1 == null) goto L8;
        addToCache(r2, r1);
    L8:
        return r1;
    }

    @Override // com.google.common.graph.MapIteratorCache
    @CheckForNull
    V getIfCached(@CheckForNull Object r4) {
        V r0 = (V) super.getIfCached(r4);
        if (r0 == null) goto L5;
        return r0;
    L5:
        CacheEntry<K, V> r1 = this.cacheEntry1;
        if (r1 != null) goto L8;
    L11:
        CacheEntry<K, V> r2 = this.cacheEntry2;
        if (r2 != null) goto L14;
        return null;
    L14:
        if (r2.key != r4) goto L19;
        addToCache(r2);
        return r2.value;
    L19:
        return null;
    L8:
        if (r1.key != r4) goto L11;
        return r1.value;
    }
}
