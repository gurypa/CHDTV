package androidx.media3.datasource.cache;

/* JADX INFO: loaded from: classes.dex */
public final class NoOpCacheEvictor implements CacheEvictor {
    public NoOpCacheEvictor() {
    }

    @Override // androidx.media3.datasource.cache.CacheEvictor
    public void onCacheInitialized() {
    }

    @Override // androidx.media3.datasource.cache.Cache.Listener
    public void onSpanAdded(Cache r1, CacheSpan r2) {
    }

    @Override // androidx.media3.datasource.cache.Cache.Listener
    public void onSpanRemoved(Cache r1, CacheSpan r2) {
    }

    @Override // androidx.media3.datasource.cache.Cache.Listener
    public void onSpanTouched(Cache r1, CacheSpan r2, CacheSpan r3) {
    }

    @Override // androidx.media3.datasource.cache.CacheEvictor
    public void onStartFile(Cache r1, String r2, long r3, long r5) {
    }

    @Override // androidx.media3.datasource.cache.CacheEvictor
    public boolean requiresCacheSpanTouches() {
        return false;
    }
}
