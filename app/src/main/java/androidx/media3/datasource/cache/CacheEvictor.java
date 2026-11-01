package androidx.media3.datasource.cache;

/* JADX INFO: loaded from: classes.dex */
public interface CacheEvictor extends Cache.Listener {
    void onCacheInitialized();

    void onStartFile(Cache r1, String r2, long r3, long r5);

    boolean requiresCacheSpanTouches();
}
