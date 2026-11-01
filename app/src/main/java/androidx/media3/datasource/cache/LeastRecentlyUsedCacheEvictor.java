package androidx.media3.datasource.cache;

import java.util.TreeSet;

/* JADX INFO: loaded from: classes.dex */
public final class LeastRecentlyUsedCacheEvictor implements CacheEvictor {
    private long currentSize;
    private final TreeSet<CacheSpan> leastRecentlyUsed;
    private final long maxBytes;

    public static /* synthetic */ int $r8$lambda$MbH9hjkgCvcQdZEuEdG20KWrdO4(CacheSpan r0, CacheSpan r1) {
        return compare(r0, r1);
    }

    public LeastRecentlyUsedCacheEvictor(long r3) {
        this.maxBytes = r3;
        this.leastRecentlyUsed = new TreeSet(new LeastRecentlyUsedCacheEvictor$$ExternalSyntheticLambda0());
    }

    private static int compare(CacheSpan r6, CacheSpan r7) {
        if ((r6.lastTouchTimestamp - r7.lastTouchTimestamp) != 0) goto L7;
        return r6.compareTo2(r7);
    L7:
        if (r6.lastTouchTimestamp >= r7.lastTouchTimestamp) goto L9;
        return -1;
    L9:
        return 1;
    }

    private void evictCache(Cache r5, long r6) {
    L3:
        if ((this.currentSize + r6) <= this.maxBytes) goto L7;
        if (this.leastRecentlyUsed.isEmpty() == true) goto L10;
        r5.removeSpan(this.leastRecentlyUsed.first());
        goto L3
    L10:
        return;
    }

    @Override // androidx.media3.datasource.cache.CacheEvictor
    public void onCacheInitialized() {
    }

    @Override // androidx.media3.datasource.cache.Cache.Listener
    public void onSpanAdded(Cache r5, CacheSpan r6) {
        this.leastRecentlyUsed.add(r6);
        this.currentSize += r6.length;
        evictCache(r5, 0);
    }

    @Override // androidx.media3.datasource.cache.Cache.Listener
    public void onSpanRemoved(Cache r5, CacheSpan r6) {
        this.leastRecentlyUsed.remove(r6);
        this.currentSize -= r6.length;
    }

    @Override // androidx.media3.datasource.cache.Cache.Listener
    public void onSpanTouched(Cache r1, CacheSpan r2, CacheSpan r3) {
        onSpanRemoved(r1, r2);
        onSpanAdded(r1, r3);
    }

    @Override // androidx.media3.datasource.cache.CacheEvictor
    public void onStartFile(Cache r3, String r4, long r5, long r7) {
        if (r7 == (-1)) goto L6;
        evictCache(r3, r7);
        return;
    }

    @Override // androidx.media3.datasource.cache.CacheEvictor
    public boolean requiresCacheSpanTouches() {
        return true;
    }
}
