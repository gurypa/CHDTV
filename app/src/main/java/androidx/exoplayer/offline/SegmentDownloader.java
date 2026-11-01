package androidx.media3.exoplayer.offline;

import android.net.Uri;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PriorityTaskManager;
import androidx.media3.common.StreamKey;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.RunnableFutureTask;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.cache.Cache;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.datasource.cache.CacheKeyFactory;
import androidx.media3.datasource.cache.CacheWriter;
import androidx.media3.datasource.cache.ContentMetadata;
import androidx.media3.exoplayer.offline.FilterableManifest;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public abstract class SegmentDownloader<M extends FilterableManifest<M>> implements Downloader {
    private static final int BUFFER_SIZE_BYTES = 131072;
    public static final long DEFAULT_MAX_MERGED_SEGMENT_START_TIME_DIFF_MS = 20000;
    private final ArrayList<RunnableFutureTask<?, ?>> activeRunnables;
    private final Cache cache;
    private final CacheDataSource.Factory cacheDataSourceFactory;
    private final CacheKeyFactory cacheKeyFactory;
    private final Executor executor;
    private volatile boolean isCanceled;
    private final DataSpec manifestDataSpec;
    private final ParsingLoadable.Parser<M> manifestParser;
    private final long maxMergedSegmentStartTimeDiffUs;
    private final PriorityTaskManager priorityTaskManager;
    private final ArrayList<StreamKey> streamKeys;

    private static final class ProgressNotifier implements CacheWriter.ProgressListener {
        private long bytesDownloaded;
        private final long contentLength;
        private final Downloader.ProgressListener progressListener;
        private int segmentsDownloaded;
        private final int totalSegments;

        public ProgressNotifier(Downloader.ProgressListener r1, long r2, int r4, long r5, int r7) {
            this.progressListener = r1;
            this.contentLength = r2;
            this.totalSegments = r4;
            this.bytesDownloaded = r5;
            this.segmentsDownloaded = r7;
        }

        private float getPercentDownloaded() {
            if (this.contentLength == (-1)) goto L9;
            if (this.contentLength == 0) goto L9;
            return (this.bytesDownloaded * 100.0f) / this.contentLength;
        L9:
            if (this.totalSegments != 0) goto L11;
            return -1.0f;
        L11:
            return (this.segmentsDownloaded * 100.0f) / this.totalSegments;
        }

        @Override // androidx.media3.datasource.cache.CacheWriter.ProgressListener
        public void onProgress(long r9, long r11, long r13) {
            this.bytesDownloaded += r13;
            this.progressListener.onProgress(this.contentLength, this.bytesDownloaded, getPercentDownloaded());
        }

        public void onSegmentDownloaded() {
            this.segmentsDownloaded++;
            this.progressListener.onProgress(this.contentLength, this.bytesDownloaded, getPercentDownloaded());
        }
    }

    protected static class Segment implements Comparable<Segment> {
        public final DataSpec dataSpec;
        public final long startTimeUs;

        public Segment(long r1, DataSpec r3) {
            this.startTimeUs = r1;
            this.dataSpec = r3;
        }

        /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
        public int compareTo2(Segment r5) {
            return Util.compareLong(this.startTimeUs, r5.startTimeUs);
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(Segment r1) {
            return compareTo2(r1);
        }
    }

    private static final class SegmentDownloadRunnable extends RunnableFutureTask<Void, IOException> {
        private final CacheWriter cacheWriter;
        public final CacheDataSource dataSource;
        private final ProgressNotifier progressNotifier;
        public final Segment segment;
        public final byte[] temporaryBuffer;

        public SegmentDownloadRunnable(Segment r3, CacheDataSource r4, ProgressNotifier r5, byte[] r6) {
            this.segment = r3;
            this.dataSource = r4;
            this.progressNotifier = r5;
            this.temporaryBuffer = r6;
            this.cacheWriter = new CacheWriter(r4, r3.dataSpec, r6, r5);
        }

        @Override // androidx.media3.common.util.RunnableFutureTask
        protected void cancelWork() {
            this.cacheWriter.cancel();
        }

        @Override // androidx.media3.common.util.RunnableFutureTask
        protected /* bridge */ /* synthetic */ Void doWork() throws Exception {
            return doWork2();
        }

        @Override // androidx.media3.common.util.RunnableFutureTask
        /* JADX INFO: renamed from: doWork, reason: avoid collision after fix types in other method */
        protected Void doWork2() throws IOException {
            this.cacheWriter.cache();
            if (this.progressNotifier == null) goto L7;
            this.progressNotifier.onSegmentDownloaded();
            return null;
        L7:
            return null;
        }
    }

    @Deprecated
    public SegmentDownloader(MediaItem r8, ParsingLoadable.Parser<M> r9, CacheDataSource.Factory r10, Executor r11) {
        this(r8, r9, r10, r11, 20000);
    }

    public SegmentDownloader(MediaItem r3, ParsingLoadable.Parser<M> r4, CacheDataSource.Factory r5, Executor r6, long r7) {
        Assertions.checkNotNull(r3.localConfiguration);
        this.manifestDataSpec = getCompressibleDataSpec(r3.localConfiguration.uri);
        this.manifestParser = r4;
        this.streamKeys = new ArrayList(r3.localConfiguration.streamKeys);
        this.cacheDataSourceFactory = r5;
        this.executor = r6;
        this.cache = (Cache) Assertions.checkNotNull(r5.getCache());
        this.cacheKeyFactory = r5.getCacheKeyFactory();
        this.priorityTaskManager = r5.getUpstreamPriorityTaskManager();
        this.activeRunnables = new ArrayList();
        this.maxMergedSegmentStartTimeDiffUs = Util.msToUs(r7);
    }

    static /* synthetic */ ParsingLoadable.Parser access$000(SegmentDownloader r1) {
        return r1.manifestParser;
    }

    private <T> void addActiveRunnable(RunnableFutureTask<T, ?> r3) throws InterruptedException {
        ArrayList<RunnableFutureTask<?, ?>> r0 = this.activeRunnables;
        monitor-enter(r0);
    L11:
        th = move-exception;
        throw th;
    L5:
        if (this.isCanceled == true) goto L10;
        this.activeRunnables.add(r3);     // Catch: Throwable -> L11
        monitor-exit(r0);     // Catch: Throwable -> L11
        return;
    L10:
        throw new InterruptedException();     // Catch: Throwable -> L11
    }

    private static boolean canMergeSegments(DataSpec r4, DataSpec r5) {
        if (r4.uri.equals(r5.uri) == true) goto L5;
    L17:
        return false;
    L5:
        if (r4.length == (-1)) goto L17;
        if ((r4.position + r4.length) != r5.position) goto L17;
        if (Util.areEqual(r4.key, r5.key) == false) goto L17;
        if (r4.flags != r5.flags) goto L17;
        if (r4.httpMethod != r5.httpMethod) goto L17;
        if (r4.httpRequestHeaders.equals(r5.httpRequestHeaders) == false) goto L17;
        return true;
    }

    protected static DataSpec getCompressibleDataSpec(Uri r2) {
        return new DataSpec.Builder().setUri(r2).setFlags(1).build();
    }

    private static void mergeSegments(List<Segment> r16, CacheKeyFactory r17, long r18) {
        HashMap r1 = new HashMap();
        int r2 = 0;
        int r3 = 0;
    L4:
        if (r3 >= r16.size()) goto L22;
        Segment r4 = r16.get(r3);
        String r5 = r17.buildCacheKey(r4.dataSpec);
        Integer r7 = (Integer) r1.get(r5);
        if (r7 != null) goto L8;
        Segment r8 = null;
    L9:
        if (r8 != null) goto L11;
    L20:
        r1.put(r5, Integer.valueOf(r2));
        r16.set(r2, r4);
        r2 = r2 + 1;
    L21:
        r3 = r3 + 1;
        goto L4
    L11:
        if (r4.startTimeUs > (r8.startTimeUs + r18)) goto L20;
        if (canMergeSegments(r8.dataSpec, r4.dataSpec) == false) goto L20;
        long r11 = -1;
        if (r4.dataSpec.length == (-1)) goto L19;
        long r9 = r8.dataSpec.length;
        r11 = r4.dataSpec.length + r9;
    L19:
        DataSpec r10 = r8.dataSpec.subrange(0, r11);
        r16.set(((Integer) Assertions.checkNotNull(r7)).intValue(), new Segment(r8.startTimeUs, r10));
        goto L21
    L8:
        r8 = r16.get(r7.intValue());
        goto L9
    L22:
        Util.removeRange(r16, r2, r16.size());
    }

    private void removeActiveRunnable(int r3) {
        ArrayList<RunnableFutureTask<?, ?>> r0 = this.activeRunnables;
        monitor-enter(r0);
        this.activeRunnables.remove(r3);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    private void removeActiveRunnable(RunnableFutureTask<?, ?> r3) {
        ArrayList<RunnableFutureTask<?, ?>> r0 = this.activeRunnables;
        monitor-enter(r0);
        this.activeRunnables.remove(r3);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.offline.Downloader
    public void cancel() {
        ArrayList<RunnableFutureTask<?, ?>> r0 = this.activeRunnables;
        monitor-enter(r0);
        this.isCanceled = true;     // Catch: Throwable -> L11
        int r2 = 0;
    L7:
        if (r2 >= this.activeRunnables.size()) goto L9;
        this.activeRunnables.get(r2).cancel(true);     // Catch: Throwable -> L11
        r2 = r2 + 1;     // Catch: Throwable -> L11
        goto L7
    L9:
        monitor-exit(r0);     // Catch: Throwable -> L11
        return;
    L11:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.offline.Downloader
    public final void download(Downloader.ProgressListener r28) throws IOException, InterruptedException {
        ArrayDeque r0 = new ArrayDeque();
        ArrayDeque r1 = new ArrayDeque();
        if (this.priorityTaskManager == null) goto L5;
        this.priorityTaskManager.add(-4000);
    L5:
        boolean r5 = true;
        CacheDataSource r6 = this.cacheDataSourceFactory.createDataSourceForDownloading();     // Catch: Throwable -> L89
        FilterableManifest r2 = getManifest(r6, this.manifestDataSpec, false);     // Catch: Throwable -> L89
        if (this.streamKeys.isEmpty() == true) goto L9;
        FilterableManifest r8 = (FilterableManifest) r2.copy(this.streamKeys);     // Catch: Throwable -> L89
    L10:
        List<Segment> r3 = getSegments(r6, r8, false);     // Catch: Throwable -> L89
        Collections.sort(r3);     // Catch: Throwable -> L89
        mergeSegments(r3, this.cacheKeyFactory, this.maxMergedSegmentStartTimeDiffUs);     // Catch: Throwable -> L89
        int r4 = r3.size();     // Catch: Throwable -> L89
        int r16 = 0;
        int r7 = r3.size() - 1;
        long r14 = 0;
        long r11 = 0;
    L12:
        if (r7 < 0) goto L35;
        DataSpec r9 = r3.get(r7).dataSpec;     // Catch: Throwable -> L33
        String r10 = this.cacheKeyFactory.buildCacheKey(r9);     // Catch: Throwable -> L33
        boolean r23 = r5;
        CacheDataSource r24 = r6;
        long r12 = r9.length;     // Catch: Throwable -> L89
        if (r12 != (-1)) goto L22;
        long r17 = ContentMetadata.CC.getContentLength(this.cache.getContentMetadata(r10));     // Catch: Throwable -> L89
        if (r17 == (-1)) goto L22;
        long r21 = r17 - r9.position;     // Catch: Throwable -> L89
    L23:
        long r13 = this.cache.getCachedBytes(r10, r9.position, r21);     // Catch: Throwable -> L89
        r14 = r14 + r13;     // Catch: Throwable -> L89
        if (r21 != (-1)) goto L26;
        r11 = -1;
    L32:
        r7 = r7 - 1;     // Catch: Throwable -> L89
        r5 = r23;
        r6 = r24;
        goto L12
    L26:
        if (r21 != r13) goto L29;
        r16 = r16 + 1;     // Catch: Throwable -> L89
        r3.remove(r7);     // Catch: Throwable -> L89
    L29:
        if (r11 == (-1)) goto L32;
        r11 = r11 + r21;     // Catch: Throwable -> L89
    L22:
        r21 = r12;
    L33:
        th = th;
    L90:
        int r15 = 0;
    L92:
        if (r15 >= this.activeRunnables.size()) goto L94;
        this.activeRunnables.get(r15).cancel(true);
        r15 = r15 + 1;
        goto L92
    L94:
        int r18 = this.activeRunnables.size() - 1;
    L95:
        if (r18 < 0) goto L98;
        this.activeRunnables.get(r18).blockUntilFinished();
        removeActiveRunnable(r18);
        r18 = r18 - 1;
        goto L95
    L98:
        if (this.priorityTaskManager == null) goto L100;
        this.priorityTaskManager.remove(-4000);
    L100:
        throw th;
    L35:
        boolean r25 = r5;
        if (r28 == null) goto L38;
        ProgressNotifier r19 = new ProgressNotifier(r28, r11, r4, r14, r16);     // Catch: Throwable -> L89
    L39:
        r0.addAll(r3);     // Catch: Throwable -> L89
    L41:
        if (this.isCanceled == true) goto L78;
        if (r0.isEmpty() == true) goto L78;
        if (this.priorityTaskManager == null) goto L48;
        this.priorityTaskManager.proceed(-4000);     // Catch: Throwable -> L89
    L48:
        if (r1.isEmpty() == true) goto L50;
        SegmentDownloadRunnable r20 = (SegmentDownloadRunnable) r1.removeFirst();     // Catch: Throwable -> L89
        CacheDataSource r22 = r20.dataSource;     // Catch: Throwable -> L89
        byte[] r26 = r20.temporaryBuffer;     // Catch: Throwable -> L89
    L51:
        Segment r27 = (Segment) r0.removeFirst();     // Catch: Throwable -> L89
        SegmentDownloadRunnable r29 = new SegmentDownloadRunnable(r27, r22, r19, r26);     // Catch: Throwable -> L89
        addActiveRunnable(r29);     // Catch: Throwable -> L89
        this.executor.execute(r29);     // Catch: Throwable -> L89
        int r30 = this.activeRunnables.size() - 1;     // Catch: Throwable -> L89
    L52:
        if (r30 < 0) goto L76;
        SegmentDownloadRunnable r31 = (SegmentDownloadRunnable) this.activeRunnables.get(r30);     // Catch: Throwable -> L89
        if (r0.isEmpty() == false) goto L56;
    L107:
        r31.get();     // Catch: ExecutionException -> L65 Throwable -> L89
        removeActiveRunnable(r30);     // Catch: ExecutionException -> L65 Throwable -> L89
        byte[] r110 = r26;
        SegmentDownloadRunnable r32 = r31;
        r1.addLast(r32);     // Catch: ExecutionException -> L63 Throwable -> L89
        Segment r210 = r27;
    L73:
        r30 = r30 - 1;     // Catch: Throwable -> L89
        r26 = r110;
        r27 = r210;
    L63:
        e = e;
    L67:
        Throwable r111 = (Throwable) Assertions.checkNotNull(e.getCause());     // Catch: Throwable -> L89
        r210 = r27;
        if ((r111 instanceof PriorityTaskManager.PriorityTooLowException) == false) goto L71;
        r0.addFirst(r32.segment);     // Catch: Throwable -> L89
        removeActiveRunnable(r30);     // Catch: Throwable -> L89
        r1.addLast(r32);     // Catch: Throwable -> L89
        goto L73
    L71:
        if ((r111 instanceof IOException) == true) goto L75;
        Util.sneakyThrow(r111);     // Catch: Throwable -> L89
        goto L73
    L75:
        throw ((IOException) r111);     // Catch: Throwable -> L89
    L65:
        e = e;
        r110 = r26;
        r32 = r31;
        goto L67
    L56:
        if (r31.isDone() == true) goto L107;
        r110 = r26;
        r210 = r27;
        goto L73
    L76:
        r29.blockUntilStarted();     // Catch: Throwable -> L89
        goto L41
    L50:
        r22 = this.cacheDataSourceFactory.createDataSourceForDownloading();     // Catch: Throwable -> L89
        r26 = new byte[131072];     // Catch: Throwable -> L89
    L78:
        int r33 = 0;
    L80:
        if (r33 >= this.activeRunnables.size()) goto L82;
        this.activeRunnables.get(r33).cancel(r25);
        r33 = r33 + 1;
        goto L80
    L82:
        int r34 = this.activeRunnables.size() - (r25 ? 1 : 0);
    L83:
        if (r34 < 0) goto L86;
        this.activeRunnables.get(r34).blockUntilFinished();
        removeActiveRunnable(r34);
        r34 = r34 - 1;
        goto L83
    L86:
        if (this.priorityTaskManager == null) goto L125;
        this.priorityTaskManager.remove(-4000);
        return;
    L125:
        return;
    L38:
        r19 = null;
        goto L39
    L9:
        r8 = r2;
    L89:
        th = th;
        goto L90
    }

    protected final <T> T execute(RunnableFutureTask<T, ?> r4, boolean r5) throws InterruptedException, IOException {
        if (r5 == false) goto L13;
        r4.run();
        return r4.get();
    L6:
        e = move-exception;
        Throwable r1 = (Throwable) Assertions.checkNotNull(e.getCause());
        if ((r1 instanceof IOException) == true) goto L11;
        Util.sneakyThrow(e);
        goto L13
    L11:
        throw ((IOException) r1);
    L13:
        if (this.isCanceled == true) goto L36;
        if (this.priorityTaskManager == null) goto L17;
        this.priorityTaskManager.proceed(-4000);
    L17:
        addActiveRunnable(r4);
        this.executor.execute(r4);
        T r0 = r4.get();     // Catch: Throwable -> L21 ExecutionException -> L23
    L19:
        r4.blockUntilFinished();
        removeActiveRunnable(r4);
        return r0;
    L23:
        e = move-exception;
        Throwable r2 = (Throwable) Assertions.checkNotNull(e.getCause());     // Catch: Throwable -> L21
        if ((r2 instanceof PriorityTaskManager.PriorityTooLowException) == false) goto L28;
    L30:
        r4.blockUntilFinished();
        removeActiveRunnable(r4);
        goto L13
    L28:
        if ((r2 instanceof IOException) == true) goto L32;
        Util.sneakyThrow(e);     // Catch: Throwable -> L21
        goto L30
    L32:
        throw ((IOException) r2);     // Catch: Throwable -> L21
    L21:
        th = move-exception;
        r4.blockUntilFinished();
        removeActiveRunnable(r4);
        throw th;
    L36:
        throw new InterruptedException();
    }

    protected final M getManifest(final DataSource r2, final DataSpec r3, boolean r4) throws InterruptedException, IOException {
        return (M) execute(new C03211(this, r2, r3), r4);
    }

    protected abstract List<Segment> getSegments(DataSource r1, M r2, boolean r3) throws IOException, InterruptedException;

    @Override // androidx.media3.exoplayer.offline.Downloader
    public final void remove() {
        CacheDataSource r0 = this.cacheDataSourceFactory.createDataSourceForRemovingDownload();
        List<Segment> r2 = getSegments(r0, getManifest(r0, this.manifestDataSpec, true), true);     // Catch: Throwable -> L9 Exception -> L11 InterruptedException -> L13
        int r3 = 0;
    L5:
        if (r3 >= r2.size()) goto L12;
        this.cache.removeResource(this.cacheKeyFactory.buildCacheKey(r2.get(r3).dataSpec));     // Catch: Throwable -> L9 Exception -> L11 InterruptedException -> L13
        r3 = r3 + 1;
        goto L5
    L9:
        th = move-exception;
        this.cache.removeResource(this.cacheKeyFactory.buildCacheKey(this.manifestDataSpec));
        throw th;
    L14:
        Thread.currentThread().interrupt();     // Catch: Throwable -> L9
    L12:
        this.cache.removeResource(this.cacheKeyFactory.buildCacheKey(this.manifestDataSpec));
    }
}
