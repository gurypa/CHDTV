package androidx.media3.datasource.cache;

import java.io.File;
import java.io.IOException;
import java.util.NavigableSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public interface Cache {
    public static final long UID_UNSET = -1;

    public static class CacheException extends IOException {
        public CacheException(String r1) {
            super(r1);
        }

        public CacheException(String r1, Throwable r2) {
            super(r1, r2);
        }

        public CacheException(Throwable r1) {
            super(r1);
        }
    }

    public interface Listener {
        void onSpanAdded(Cache r1, CacheSpan r2);

        void onSpanRemoved(Cache r1, CacheSpan r2);

        void onSpanTouched(Cache r1, CacheSpan r2, CacheSpan r3);
    }

    NavigableSet<CacheSpan> addListener(String r1, Listener r2);

    void applyContentMetadataMutations(String r1, ContentMetadataMutations r2) throws CacheException;

    void commitFile(File r1, long r2) throws CacheException;

    long getCacheSpace();

    long getCachedBytes(String r1, long r2, long r4);

    long getCachedLength(String r1, long r2, long r4);

    NavigableSet<CacheSpan> getCachedSpans(String r1);

    ContentMetadata getContentMetadata(String r1);

    Set<String> getKeys();

    long getUid();

    boolean isCached(String r1, long r2, long r4);

    void release();

    void releaseHoleSpan(CacheSpan r1);

    void removeListener(String r1, Listener r2);

    void removeResource(String r1);

    void removeSpan(CacheSpan r1);

    File startFile(String r1, long r2, long r4) throws CacheException;

    CacheSpan startReadWrite(String r1, long r2, long r4) throws InterruptedException, CacheException;

    CacheSpan startReadWriteNonBlocking(String r1, long r2, long r4) throws CacheException;
}
