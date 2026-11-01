package androidx.media3.exoplayer.offline;

import android.util.SparseArray;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.exoplayer.dash.offline.DashDownloader$$ExternalSyntheticLambda0;
import java.lang.reflect.Constructor;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class DefaultDownloaderFactory implements DownloaderFactory {
    private static final SparseArray<Constructor<? extends Downloader>> CONSTRUCTORS = null;
    private final CacheDataSource.Factory cacheDataSourceFactory;
    private final Executor executor;

    static {
        CONSTRUCTORS = createDownloaderConstructors();
    }

    @Deprecated
    public DefaultDownloaderFactory(CacheDataSource.Factory r2) {
        this(r2, new DashDownloader$$ExternalSyntheticLambda0());
    }

    public DefaultDownloaderFactory(CacheDataSource.Factory r2, Executor r3) {
        this.cacheDataSourceFactory = (CacheDataSource.Factory) Assertions.checkNotNull(r2);
        this.executor = (Executor) Assertions.checkNotNull(r3);
    }

    private Downloader createDownloader(DownloadRequest r7, int r8) {
        Constructor<? extends Downloader> r0 = CONSTRUCTORS.get(r8);
        if (r0 == null) goto L11;
        return r0.newInstance(new Object[]{new MediaItem.Builder().setUri(r7.uri).setStreamKeys(r7.streamKeys).setCustomCacheKey(r7.customCacheKey).build(), this.cacheDataSourceFactory, this.executor});
    L7:
        e = move-exception;
        throw new IllegalStateException("Failed to instantiate downloader for content type " + r8, e);
    L11:
        throw new IllegalStateException("Module missing for content type " + r8);
    }

    private static SparseArray<Constructor<? extends Downloader>> createDownloaderConstructors() {
        SparseArray<Constructor<? extends Downloader>> r0 = new SparseArray();
        r0.put(0, getDownloaderConstructor(Class.forName("androidx.media3.exoplayer.dash.offline.DashDownloader")));     // Catch: ClassNotFoundException -> L5
    L15:
        r0.put(2, getDownloaderConstructor(Class.forName("androidx.media3.exoplayer.hls.offline.HlsDownloader")));     // Catch: ClassNotFoundException -> L8
    L13:
        r0.put(1, getDownloaderConstructor(Class.forName("androidx.media3.exoplayer.smoothstreaming.offline.SsDownloader")));     // Catch: ClassNotFoundException -> L11
    L12:
        return r0;
    }

    private static Constructor<? extends Downloader> getDownloaderConstructor(Class<?> r4) {
        return r4.asSubclass(Downloader.class).getConstructor(new Class[]{MediaItem.class, CacheDataSource.Factory.class, Executor.class});
    L4:
        e = move-exception;
        throw new IllegalStateException("Downloader constructor missing", e);
    }

    @Override // androidx.media3.exoplayer.offline.DownloaderFactory
    public Downloader createDownloader(DownloadRequest r6) {
        int r0 = Util.inferContentTypeForUriAndMimeType(r6.uri, r6.mimeType);
        switch(r0) {
            case 0: goto L9;
            case 1: goto L9;
            case 2: goto L9;
            case 3: goto L5;
            case 4: goto L7;
            default: goto L5;
        };
    L5:
        throw new IllegalArgumentException("Unsupported type: " + r0);
    L7:
        return new ProgressiveDownloader(new MediaItem.Builder().setUri(r6.uri).setCustomCacheKey(r6.customCacheKey).build(), this.cacheDataSourceFactory, this.executor);
    L9:
        return createDownloader(r6, r0);
    }
}
