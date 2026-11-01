package com.google.common.cache;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class AbstractLoadingCache<K, V> extends AbstractCache<K, V> implements LoadingCache<K, V> {
    protected AbstractLoadingCache() {
    }

    @Override // com.google.common.cache.LoadingCache, com.google.common.base.Function
    public final V apply(K r2) {
        return getUnchecked(r2);
    }

    @Override // com.google.common.cache.LoadingCache
    public ImmutableMap<K, V> getAll(Iterable<? extends K> r5) throws ExecutionException {
        LinkedHashMap r0 = Maps.newLinkedHashMap();
        Iterator<? extends K> r1 = r5.iterator();
    L4:
        if (r1.hasNext() == false) goto L10;
        K r2 = r1.next();
        if (r0.containsKey(r2) == true) goto L4;
        r0.put(r2, get(r2));
        goto L4
    L10:
        return ImmutableMap.copyOf(r0);
    }

    @Override // com.google.common.cache.LoadingCache
    public V getUnchecked(K r4) {
        return get(r4);
    L4:
        e = move-exception;
        throw new UncheckedExecutionException(e.getCause());
    }

    @Override // com.google.common.cache.LoadingCache
    public void refresh(K r2) {
        throw new UnsupportedOperationException();
    }
}
