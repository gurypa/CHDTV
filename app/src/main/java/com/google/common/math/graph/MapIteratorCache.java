package com.google.common.graph;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class MapIteratorCache<K, V> {
    private final Map<K, V> backingMap;

    @CheckForNull
    private volatile transient Map.Entry<K, V> cacheEntry;

    MapIteratorCache(Map<K, V> r2) {
        this.backingMap = (Map) Preconditions.checkNotNull(r2);
    }

    static /* synthetic */ Map access$000(MapIteratorCache r1) {
        return r1.backingMap;
    }

    static /* synthetic */ Map.Entry access$102(MapIteratorCache r0, Map.Entry r1) {
        r0.cacheEntry = r1;
        return r1;
    }

    final void clear() {
        clearCache();
        this.backingMap.clear();
    }

    void clearCache() {
        this.cacheEntry = null;
    }

    final boolean containsKey(@CheckForNull Object r2) {
        if (getIfCached(r2) == null) goto L5;
    L8:
        return true;
    L5:
        if (this.backingMap.containsKey(r2) == true) goto L8;
        return false;
    }

    @CheckForNull
    V get(Object r3) {
        Preconditions.checkNotNull(r3);
        V r0 = getIfCached(r3);
        if (r0 == null) goto L5;
        return r0;
    L5:
        return getWithoutCaching(r3);
    }

    @CheckForNull
    V getIfCached(@CheckForNull Object r3) {
        Map.Entry<K, V> r0 = this.cacheEntry;
        if (r0 != null) goto L5;
        return null;
    L5:
        if (r0.getKey() == r3) goto L7;
        return null;
    L7:
        return r0.getValue();
    }

    @CheckForNull
    final V getWithoutCaching(Object r2) {
        Preconditions.checkNotNull(r2);
        return this.backingMap.get(r2);
    }

    @CheckForNull
    final V put(K r2, V r3) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        clearCache();
        return this.backingMap.put(r2, r3);
    }

    @CheckForNull
    final V remove(Object r2) {
        Preconditions.checkNotNull(r2);
        clearCache();
        return this.backingMap.remove(r2);
    }

    final Set<K> unmodifiableKeySet() {
        return new C08051(this);
    }
}
