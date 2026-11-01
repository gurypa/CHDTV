package com.google.common.cache;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class AbstractCache<K, V> implements Cache<K, V> {

    public static final class SimpleStatsCounter implements StatsCounter {
        private final LongAddable evictionCount;
        private final LongAddable hitCount;
        private final LongAddable loadExceptionCount;
        private final LongAddable loadSuccessCount;
        private final LongAddable missCount;
        private final LongAddable totalLoadTime;

        public SimpleStatsCounter() {
            this.hitCount = LongAddables.create();
            this.missCount = LongAddables.create();
            this.loadSuccessCount = LongAddables.create();
            this.loadExceptionCount = LongAddables.create();
            this.totalLoadTime = LongAddables.create();
            this.evictionCount = LongAddables.create();
        }

        private static long negativeToMaxValue(long r2) {
            if (r2 >= 0) goto L7;
            return Long.MAX_VALUE;
        L7:
            return r2;
        }

        public void incrementBy(StatsCounter r5) {
            CacheStats r0 = r5.snapshot();
            this.hitCount.add(r0.hitCount());
            this.missCount.add(r0.missCount());
            this.loadSuccessCount.add(r0.loadSuccessCount());
            this.loadExceptionCount.add(r0.loadExceptionCount());
            this.totalLoadTime.add(r0.totalLoadTime());
            this.evictionCount.add(r0.evictionCount());
        }

        @Override // com.google.common.cache.AbstractCache.StatsCounter
        public void recordEviction() {
            this.evictionCount.increment();
        }

        @Override // com.google.common.cache.AbstractCache.StatsCounter
        public void recordHits(int r4) {
            this.hitCount.add(r4);
        }

        @Override // com.google.common.cache.AbstractCache.StatsCounter
        public void recordLoadException(long r2) {
            this.loadExceptionCount.increment();
            this.totalLoadTime.add(r2);
        }

        @Override // com.google.common.cache.AbstractCache.StatsCounter
        public void recordLoadSuccess(long r2) {
            this.loadSuccessCount.increment();
            this.totalLoadTime.add(r2);
        }

        @Override // com.google.common.cache.AbstractCache.StatsCounter
        public void recordMisses(int r4) {
            this.missCount.add(r4);
        }

        @Override // com.google.common.cache.AbstractCache.StatsCounter
        public CacheStats snapshot() {
            return new CacheStats(negativeToMaxValue(this.hitCount.sum()), negativeToMaxValue(this.missCount.sum()), negativeToMaxValue(this.loadSuccessCount.sum()), negativeToMaxValue(this.loadExceptionCount.sum()), negativeToMaxValue(this.totalLoadTime.sum()), negativeToMaxValue(this.evictionCount.sum()));
        }
    }

    public interface StatsCounter {
        void recordEviction();

        void recordHits(int r1);

        void recordLoadException(long r1);

        void recordLoadSuccess(long r1);

        void recordMisses(int r1);

        CacheStats snapshot();
    }

    protected AbstractCache() {
    }

    @Override // com.google.common.cache.Cache
    public ConcurrentMap<K, V> asMap() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.cache.Cache
    public void cleanUp() {
    }

    @Override // com.google.common.cache.Cache
    public V get(K r2, Callable<? extends V> r3) throws ExecutionException {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.cache.Cache
    public ImmutableMap<K, V> getAllPresent(Iterable<? extends Object> r6) {
        LinkedHashMap r0 = Maps.newLinkedHashMap();
        Iterator<? extends Object> r1 = r6.iterator();
    L4:
        if (r1.hasNext() == false) goto L12;
        Object r2 = r1.next();
        if (r0.containsKey(r2) == true) goto L4;
        V r4 = getIfPresent(r2);
        if (r4 == null) goto L4;
        r0.put(r2, r4);
        goto L4
    L12:
        return ImmutableMap.copyOf(r0);
    }

    @Override // com.google.common.cache.Cache
    public void invalidate(Object r2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.cache.Cache
    public void invalidateAll() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.cache.Cache
    public void invalidateAll(Iterable<? extends Object> r3) {
        Iterator<? extends Object> r0 = r3.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        invalidate(r0.next());
        goto L4
    }

    @Override // com.google.common.cache.Cache
    public void put(K r2, V r3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.cache.Cache
    public void putAll(Map<? extends K, ? extends V> r5) {
        Iterator<Map.Entry<? extends K, ? extends V>> r0 = r5.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Map.Entry<? extends K, ? extends V> r1 = r0.next();
        put(r1.getKey(), r1.getValue());
        goto L4
    }

    @Override // com.google.common.cache.Cache
    public long size() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.cache.Cache
    public CacheStats stats() {
        throw new UnsupportedOperationException();
    }
}
