package androidx.media3.exoplayer.offline;

import androidx.media3.common.MediaItem;
import androidx.media3.common.PriorityTaskManager;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.RunnableFutureTask;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.datasource.cache.CacheWriter;
import androidx.media3.exoplayer.dash.offline.DashDownloader$$ExternalSyntheticLambda0;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public final class ProgressiveDownloader implements Downloader {
    private final CacheWriter cacheWriter;
    private final CacheDataSource dataSource;
    private final DataSpec dataSpec;
    private volatile RunnableFutureTask<Void, IOException> downloadRunnable;
    private final Executor executor;
    private volatile boolean isCanceled;
    private final PriorityTaskManager priorityTaskManager;
    private Downloader.ProgressListener progressListener;

    public static /* synthetic */ void $r8$lambda$VTq9YeX0OoDzOHG6MNnGui6uRfA(ProgressiveDownloader r0, long r1, long r3, long r5) {
        r0.onProgress(r1, r3, r5);
    }

    public ProgressiveDownloader(MediaItem r2, CacheDataSource.Factory r3) {
        this(r2, r3, new DashDownloader$$ExternalSyntheticLambda0());
    }

    public ProgressiveDownloader(MediaItem r6, CacheDataSource.Factory r7, Executor r8) {
        this.executor = (Executor) Assertions.checkNotNull(r8);
        Assertions.checkNotNull(r6.localConfiguration);
        this.dataSpec = new DataSpec.Builder().setUri(r6.localConfiguration.uri).setKey(r6.localConfiguration.customCacheKey).setFlags(4).build();
        this.dataSource = r7.createDataSourceForDownloading();
        CacheWriter.ProgressListener r0 = new ProgressiveDownloader$$ExternalSyntheticLambda0(this);
        this.cacheWriter = new CacheWriter(this.dataSource, this.dataSpec, null, r0);
        this.priorityTaskManager = r7.getUpstreamPriorityTaskManager();
    }

    static /* synthetic */ CacheWriter access$000(ProgressiveDownloader r1) {
        return r1.cacheWriter;
    }

    private void onProgress(long r8, long r10, long r12) {
        if (this.progressListener != null) goto L6;
        return;
    L6:
        if (r8 != (-1)) goto L8;
    L11:
        float r0 = -1.0f;
    L12:
        this.progressListener.onProgress(r8, r10, r0);
        return;
    L8:
        if (r8 == 0) goto L11;
        r0 = (r10 * 100.0f) / r8;
        goto L12
    }

    @Override // androidx.media3.exoplayer.offline.Downloader
    public void cancel() {
        this.isCanceled = true;
        RunnableFutureTask<Void, IOException> r1 = this.downloadRunnable;
        if (r1 == null) goto L6;
        r1.cancel(true);
        return;
    }

    @Override // androidx.media3.exoplayer.offline.Downloader
    public void download(Downloader.ProgressListener r6) throws IOException, InterruptedException {
        this.progressListener = r6;
        if (this.priorityTaskManager == null) goto L5;
        this.priorityTaskManager.add(-4000);
    L5:
        boolean r0 = false;
    L6:
        if (r0 == true) goto L30;
    L25:
        th = move-exception;
        ((RunnableFutureTask) Assertions.checkNotNull(this.downloadRunnable)).blockUntilFinished();
        if (this.priorityTaskManager == null) goto L29;
        this.priorityTaskManager.remove(-4000);
    L29:
        throw th;
    L8:
        if (this.isCanceled == true) goto L30;
        this.downloadRunnable = new C03201(this);     // Catch: Throwable -> L25
        if (this.priorityTaskManager == null) goto L12;
        this.priorityTaskManager.proceed(-4000);     // Catch: Throwable -> L25
    L12:
        this.executor.execute(this.downloadRunnable);     // Catch: Throwable -> L25
        this.downloadRunnable.get();     // Catch: ExecutionException -> L16 Throwable -> L25
    L14:
        r0 = true;
    L16:
        e = move-exception;
        Throwable r3 = (Throwable) Assertions.checkNotNull(e.getCause());     // Catch: Throwable -> L25
        if ((r3 instanceof PriorityTaskManager.PriorityTooLowException) == true) goto L6;
        if ((r3 instanceof IOException) == true) goto L24;
        Util.sneakyThrow(r3);     // Catch: Throwable -> L25
        goto L6
    L24:
        throw ((IOException) r3);     // Catch: Throwable -> L25
    L30:
        ((RunnableFutureTask) Assertions.checkNotNull(this.downloadRunnable)).blockUntilFinished();
        if (this.priorityTaskManager == null) goto L48;
        this.priorityTaskManager.remove(-4000);
        return;
    }

    @Override // androidx.media3.exoplayer.offline.Downloader
    public void remove() {
        this.dataSource.getCache().removeResource(this.dataSource.getCacheKeyFactory().buildCacheKey(this.dataSpec));
    }
}
