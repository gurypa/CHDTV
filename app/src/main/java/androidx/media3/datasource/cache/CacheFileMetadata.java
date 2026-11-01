package androidx.media3.datasource.cache;

/* JADX INFO: loaded from: classes.dex */
final class CacheFileMetadata {
    public final long lastTouchTimestamp;
    public final long length;

    public CacheFileMetadata(long r1, long r3) {
        this.length = r1;
        this.lastTouchTimestamp = r3;
    }
}
