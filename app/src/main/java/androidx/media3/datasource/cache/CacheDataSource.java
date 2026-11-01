package androidx.media3.datasource.cache;

import android.net.Uri;
import androidx.media3.common.PriorityTaskManager;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSink;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSourceException;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.FileDataSource;
import androidx.media3.datasource.PlaceholderDataSource;
import androidx.media3.datasource.PriorityDataSource;
import androidx.media3.datasource.TeeDataSource;
import androidx.media3.datasource.TransferListener;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class CacheDataSource implements DataSource {
    public static final int CACHE_IGNORED_REASON_ERROR = 0;
    public static final int CACHE_IGNORED_REASON_UNSET_LENGTH = 1;
    private static final int CACHE_NOT_IGNORED = -1;
    public static final int FLAG_BLOCK_ON_CACHE = 1;
    public static final int FLAG_IGNORE_CACHE_FOR_UNSET_LENGTH_REQUESTS = 4;
    public static final int FLAG_IGNORE_CACHE_ON_ERROR = 2;
    private static final long MIN_READ_BEFORE_CHECKING_CACHE = 102400;
    private Uri actualUri;
    private final boolean blockOnCache;
    private long bytesRemaining;
    private final Cache cache;
    private final CacheKeyFactory cacheKeyFactory;
    private final DataSource cacheReadDataSource;
    private final DataSource cacheWriteDataSource;
    private long checkCachePosition;
    private DataSource currentDataSource;
    private long currentDataSourceBytesRead;
    private DataSpec currentDataSpec;
    private CacheSpan currentHoleSpan;
    private boolean currentRequestIgnoresCache;
    private final EventListener eventListener;
    private final boolean ignoreCacheForUnsetLengthRequests;
    private final boolean ignoreCacheOnError;
    private long readPosition;
    private DataSpec requestDataSpec;
    private boolean seenCacheError;
    private long totalCachedBytesRead;
    private final DataSource upstreamDataSource;

    /* JADX INFO: renamed from: androidx.media3.datasource.cache.CacheDataSource$1 */
    static /* synthetic */ class C02171 {
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface CacheIgnoredReason {
    }

    public interface EventListener {
        void onCacheIgnored(int r1);

        void onCachedBytesRead(long r1, long r3);
    }

    public static final class Factory implements DataSource.Factory {
        private Cache cache;
        private boolean cacheIsReadOnly;
        private CacheKeyFactory cacheKeyFactory;
        private DataSource.Factory cacheReadDataSourceFactory;
        private DataSink.Factory cacheWriteDataSinkFactory;
        private EventListener eventListener;
        private int flags;
        private DataSource.Factory upstreamDataSourceFactory;
        private int upstreamPriority;
        private PriorityTaskManager upstreamPriorityTaskManager;

        public Factory() {
            this.cacheReadDataSourceFactory = new FileDataSource.Factory();
            this.cacheKeyFactory = CacheKeyFactory.DEFAULT;
        }

        private CacheDataSource createDataSourceInternal(DataSource r13, int r14, int r15) {
            Cache r2 = (Cache) Assertions.checkNotNull(this.cache);
            if (this.cacheIsReadOnly == true) goto L10;
            if (r13 == null) goto L10;
            if (this.cacheWriteDataSinkFactory == null) goto L9;
            DataSink r5 = this.cacheWriteDataSinkFactory.createDataSink();
        L12:
            return new CacheDataSource(r2, r13, this.cacheReadDataSourceFactory.createDataSource(), r5, this.cacheKeyFactory, r14, this.upstreamPriorityTaskManager, r15, this.eventListener, null);
        L9:
            r5 = new CacheDataSink.Factory().setCache(r2).createDataSink();
        L10:
            r5 = null;
            goto L12
        }

        @Override // androidx.media3.datasource.DataSource.Factory
        public /* bridge */ /* synthetic */ DataSource createDataSource() {
            return createDataSource();
        }

        @Override // androidx.media3.datasource.DataSource.Factory
        public CacheDataSource createDataSource() {
            if (this.upstreamDataSourceFactory == null) goto L5;
            DataSource r0 = this.upstreamDataSourceFactory.createDataSource();
        L7:
            return createDataSourceInternal(r0, this.flags, this.upstreamPriority);
        L5:
            r0 = null;
            goto L7
        }

        public CacheDataSource createDataSourceForDownloading() {
            if (this.upstreamDataSourceFactory == null) goto L5;
            DataSource r0 = this.upstreamDataSourceFactory.createDataSource();
        L7:
            return createDataSourceInternal(r0, this.flags | 1, -4000);
        L5:
            r0 = null;
            goto L7
        }

        public CacheDataSource createDataSourceForRemovingDownload() {
            return createDataSourceInternal(null, this.flags | 1, -4000);
        }

        public Cache getCache() {
            return this.cache;
        }

        public CacheKeyFactory getCacheKeyFactory() {
            return this.cacheKeyFactory;
        }

        public PriorityTaskManager getUpstreamPriorityTaskManager() {
            return this.upstreamPriorityTaskManager;
        }

        public Factory setCache(Cache r1) {
            this.cache = r1;
            return this;
        }

        public Factory setCacheKeyFactory(CacheKeyFactory r1) {
            this.cacheKeyFactory = r1;
            return this;
        }

        public Factory setCacheReadDataSourceFactory(DataSource.Factory r1) {
            this.cacheReadDataSourceFactory = r1;
            return this;
        }

        public Factory setCacheWriteDataSinkFactory(DataSink.Factory r2) {
            this.cacheWriteDataSinkFactory = r2;
            if (r2 != null) goto L5;
            boolean r0 = true;
        L6:
            this.cacheIsReadOnly = r0;
            return this;
        L5:
            r0 = false;
            goto L6
        }

        public Factory setEventListener(EventListener r1) {
            this.eventListener = r1;
            return this;
        }

        public Factory setFlags(int r1) {
            this.flags = r1;
            return this;
        }

        public Factory setUpstreamDataSourceFactory(DataSource.Factory r1) {
            this.upstreamDataSourceFactory = r1;
            return this;
        }

        public Factory setUpstreamPriority(int r1) {
            this.upstreamPriority = r1;
            return this;
        }

        public Factory setUpstreamPriorityTaskManager(PriorityTaskManager r1) {
            this.upstreamPriorityTaskManager = r1;
            return this;
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    public CacheDataSource(Cache r2, DataSource r3) {
        this(r2, r3, 0);
    }

    public CacheDataSource(Cache r8, DataSource r9, int r10) {
        this(r8, r9, new FileDataSource(), new CacheDataSink(r8, CacheDataSink.DEFAULT_FRAGMENT_SIZE), r10, null);
    }

    public CacheDataSource(Cache r9, DataSource r10, DataSource r11, DataSink r12, int r13, EventListener r14) {
        this(r9, r10, r11, r12, r13, r14, null);
    }

    public CacheDataSource(Cache r11, DataSource r12, DataSource r13, DataSink r14, int r15, EventListener r16, CacheKeyFactory r17) {
        this(r11, r12, r13, r14, r17, r15, null, -1000, r16);
    }

    private CacheDataSource(Cache r4, DataSource r5, DataSource r6, DataSink r7, CacheKeyFactory r8, int r9, PriorityTaskManager r10, int r11, EventListener r12) {
        this.cache = r4;
        this.cacheReadDataSource = r6;
        if (r8 == null) goto L5;
        CacheKeyFactory r0 = r8;
    L6:
        this.cacheKeyFactory = r0;
        boolean r1 = false;
        if ((r9 & 1) == 0) goto L9;
        boolean r2 = true;
    L10:
        this.blockOnCache = r2;
        if ((r9 & 2) == 0) goto L13;
        boolean r3 = true;
    L14:
        this.ignoreCacheOnError = r3;
        if ((r9 & 4) == 0) goto L17;
        r1 = true;
    L17:
        this.ignoreCacheForUnsetLengthRequests = r1;
        TeeDataSource r13 = null;
        if (r5 == null) goto L26;
        if (r10 == null) goto L21;
        r5 = new PriorityDataSource(r5, r10, r11);
    L21:
        this.upstreamDataSource = r5;
        if (r7 == null) goto L25;
        r13 = new TeeDataSource(r5, r7);
    L25:
        this.cacheWriteDataSource = r13;
    L27:
        this.eventListener = r12;
        return;
    L26:
        this.upstreamDataSource = PlaceholderDataSource.INSTANCE;
        this.cacheWriteDataSource = null;
        goto L27
    L13:
        r3 = false;
        goto L14
    L9:
        r2 = false;
        goto L10
    L5:
        r0 = CacheKeyFactory.DEFAULT;
        goto L6
    }

    /* synthetic */ CacheDataSource(Cache r1, DataSource r2, DataSource r3, DataSink r4, CacheKeyFactory r5, int r6, PriorityTaskManager r7, int r8, EventListener r9, C02171 r10) {
        this(r1, r2, r3, r4, r5, r6, r7, r8, r9);
    }

    private void closeCurrentSource() throws IOException {
        if (this.currentDataSource != null) goto L16;
        return;
    L16:
        this.currentDataSource.close();     // Catch: Throwable -> L11
        this.currentDataSpec = null;
        this.currentDataSource = null;
        if (this.currentHoleSpan == null) goto L18;
        this.cache.releaseHoleSpan(this.currentHoleSpan);
        this.currentHoleSpan = null;
        return;
    L18:
        return;
    L11:
        th = move-exception;
        this.currentDataSpec = null;
        this.currentDataSource = null;
        if (this.currentHoleSpan == null) goto L15;
        this.cache.releaseHoleSpan(this.currentHoleSpan);
        this.currentHoleSpan = null;
    L15:
        throw th;
    }

    private static Uri getRedirectedUriOrDefault(Cache r2, String r3, Uri r4) {
        Uri r0 = ContentMetadata.CC.getRedirectedUri(r2.getContentMetadata(r3));
        if (r0 == null) goto L6;
        return r0;
    L6:
        return r4;
    }

    private void handleBeforeThrow(Throwable r2) {
        if (isReadingFromCache() == false) goto L5;
    L6:
        this.seenCacheError = true;
        return;
    L5:
        if ((r2 instanceof Cache.CacheException) == true) goto L6;
    }

    private boolean isBypassingCache() {
        if (this.currentDataSource != this.upstreamDataSource) goto L5;
        return true;
    L5:
        return false;
    }

    private boolean isReadingFromCache() {
        if (this.currentDataSource != this.cacheReadDataSource) goto L5;
        return true;
    L5:
        return false;
    }

    private boolean isReadingFromUpstream() {
        return !isReadingFromCache();
    }

    private boolean isWritingToCache() {
        if (this.currentDataSource != this.cacheWriteDataSource) goto L5;
        return true;
    L5:
        return false;
    }

    private void notifyBytesRead() {
        if (this.eventListener != null) goto L5;
        return;
    L5:
        if (this.totalCachedBytesRead <= 0) goto L9;
        this.eventListener.onCachedBytesRead(this.cache.getCacheSpace(), this.totalCachedBytesRead);
        this.totalCachedBytesRead = 0;
        return;
    }

    private void notifyCacheIgnored(int r2) {
        if (this.eventListener == null) goto L6;
        this.eventListener.onCacheIgnored(r2);
        return;
    }

    private void openNextSource(DataSpec r14, boolean r15) throws IOException {
        String r2 = (String) Util.castNonNull(r14.key);
        if (this.currentRequestIgnoresCache == false) goto L6;
        CacheSpan r0 = null;
    L14:
        if (r0 != null) goto L17;
        DataSource r1 = this.upstreamDataSource;
        DataSpec r5 = r14.buildUpon().setPosition(this.readPosition).setLength(this.bytesRemaining).build();
        DataSource r7 = r1;
        CacheSpan r3 = r0;
    L33:
        if (this.currentRequestIgnoresCache == false) goto L35;
    L37:
        long r8 = Long.MAX_VALUE;
    L38:
        this.checkCachePosition = r8;
        if (r15 == false) goto L50;
        Assertions.checkState(isBypassingCache());
        if (r7 != this.upstreamDataSource) goto L72;
        return;
    L72:
        closeCurrentSource();     // Catch: Throwable -> L45
    L45:
        th = move-exception;
        if (((CacheSpan) Util.castNonNull(r3)).isHoleSpan() == false) goto L49;
        this.cache.releaseHoleSpan(r3);
    L49:
        throw th;
    L50:
        if (r3 != null) goto L52;
    L54:
        this.currentDataSource = r7;
        this.currentDataSpec = r5;
        this.currentDataSourceBytesRead = 0;
        long r9 = r7.open(r5);
        ContentMetadataMutations r4 = new ContentMetadataMutations();
        if (r5.length != (-1)) goto L60;
        if (r9 == (-1)) goto L60;
        this.bytesRemaining = r9;
        ContentMetadataMutations.setContentLength(r4, this.readPosition + this.bytesRemaining);
    L60:
        if (isReadingFromUpstream() == false) goto L67;
        this.actualUri = r7.getUri();
        if ((!r14.uri.equals(this.actualUri)) == false) goto L64;
        Uri r6 = this.actualUri;
    L65:
        ContentMetadataMutations.setRedirectedUri(r4, r6);
        goto L67
    L64:
        r6 = null;
    L67:
        if (isWritingToCache() == false) goto L74;
        this.cache.applyContentMetadataMutations(r2, r4);
        return;
    L74:
        return;
    L52:
        if (r3.isHoleSpan() == false) goto L54;
        this.currentHoleSpan = r3;
        goto L54
    L35:
        if (r7 != this.upstreamDataSource) goto L37;
        r8 = this.readPosition + MIN_READ_BEFORE_CHECKING_CACHE;
        goto L38
    L17:
        if (r0.isCached == false) goto L23;
        Uri r10 = Uri.fromFile((File) Util.castNonNull(r0.file));
        long r11 = r0.position;
        long r12 = this.readPosition - r11;
        long r13 = r0.length - r12;
        if (this.bytesRemaining == (-1)) goto L21;
        r13 = Math.min(r13, this.bytesRemaining);
    L21:
        DataSpec r16 = r14.buildUpon().setUri(r10).setUriPositionOffset(r11).setPosition(r12).setLength(r13).build();
        r7 = this.cacheReadDataSource;
        r5 = r16;
        r3 = r0;
        goto L33
    L23:
        if (r0.isOpenEnded() == false) goto L25;
        long r17 = this.bytesRemaining;
    L28:
        DataSpec r18 = r14.buildUpon().setPosition(this.readPosition).setLength(r17).build();
        if (this.cacheWriteDataSource == null) goto L31;
        r7 = this.cacheWriteDataSource;
        r5 = r18;
        r3 = r0;
        goto L33
    L31:
        r7 = this.upstreamDataSource;
        this.cache.releaseHoleSpan(r0);
        r5 = r18;
        r3 = null;
        goto L33
    L25:
        r17 = r0.length;
        if (this.bytesRemaining == (-1)) goto L28;
        r17 = Math.min(r17, this.bytesRemaining);
        goto L28
    L6:
        if (this.blockOnCache == true) goto L70;
        r0 = this.cache.startReadWriteNonBlocking(r2, this.readPosition, this.bytesRemaining);
        goto L14
    L70:
        r0 = this.cache.startReadWrite(r2, this.readPosition, this.bytesRemaining);     // Catch: InterruptedException -> L9
    L10:
        Thread.currentThread().interrupt();
        throw new InterruptedIOException();
    }

    private void setNoBytesRemainingAndMaybeStoreLength(String r4) throws IOException {
        this.bytesRemaining = 0;
        if (isWritingToCache() == false) goto L6;
        ContentMetadataMutations r0 = new ContentMetadataMutations();
        ContentMetadataMutations.setContentLength(r0, this.readPosition);
        this.cache.applyContentMetadataMutations(r4, r0);
        return;
    }

    private int shouldIgnoreCacheForRequest(DataSpec r5) {
        if (this.ignoreCacheOnError == false) goto L9;
        if (this.seenCacheError == false) goto L9;
        return 0;
    L9:
        if (this.ignoreCacheForUnsetLengthRequests == true) goto L11;
        return -1;
    L11:
        if (r5.length != (-1)) goto L16;
        return 1;
    L16:
        return -1;
    }

    @Override // androidx.media3.datasource.DataSource
    public void addTransferListener(TransferListener r2) {
        Assertions.checkNotNull(r2);
        this.cacheReadDataSource.addTransferListener(r2);
        this.upstreamDataSource.addTransferListener(r2);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws IOException {
        this.requestDataSpec = null;
        this.actualUri = null;
        this.readPosition = 0;
        notifyBytesRead();
        closeCurrentSource();     // Catch: Throwable -> L5
        return;
    L5:
        th = move-exception;
        handleBeforeThrow(th);
        throw th;
    }

    public Cache getCache() {
        return this.cache;
    }

    public CacheKeyFactory getCacheKeyFactory() {
        return this.cacheKeyFactory;
    }

    @Override // androidx.media3.datasource.DataSource
    public Map<String, List<String>> getResponseHeaders() {
        if (isReadingFromUpstream() == false) goto L6;
        return this.upstreamDataSource.getResponseHeaders();
    L6:
        return Collections.emptyMap();
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        return this.actualUri;
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r14) throws IOException {
        String r0 = this.cacheKeyFactory.buildCacheKey(r14);     // Catch: Throwable -> L36
        DataSpec r1 = r14.buildUpon().setKey(r0).build();     // Catch: Throwable -> L36
        this.requestDataSpec = r1;     // Catch: Throwable -> L36
        this.actualUri = getRedirectedUriOrDefault(this.cache, r0, r1.uri);     // Catch: Throwable -> L36
        this.readPosition = r14.position;     // Catch: Throwable -> L36
        int r2 = shouldIgnoreCacheForRequest(r14);     // Catch: Throwable -> L36
        if (r2 == (-1)) goto L5;
        boolean r3 = true;
    L6:
        this.currentRequestIgnoresCache = r3;     // Catch: Throwable -> L36
        if (this.currentRequestIgnoresCache == false) goto L10;
        notifyCacheIgnored(r2);     // Catch: Throwable -> L36
    L10:
        if (this.currentRequestIgnoresCache == false) goto L12;
        this.bytesRemaining = -1;     // Catch: Throwable -> L36
    L20:
        if (r14.length == (-1)) goto L27;
        if (this.bytesRemaining != (-1)) goto L24;
        long r9 = r14.length;     // Catch: Throwable -> L36
    L25:
        this.bytesRemaining = r9;     // Catch: Throwable -> L36
        goto L27
    L24:
        r9 = Math.min(this.bytesRemaining, r14.length);     // Catch: Throwable -> L36
    L27:
        if (this.bytesRemaining <= 0) goto L29;
    L30:
        openNextSource(r1, false);     // Catch: Throwable -> L36
    L32:
        if (r14.length == (-1)) goto L35;
        return r14.length;
    L35:
        return this.bytesRemaining;
    L29:
        if (this.bytesRemaining != (-1)) goto L32;
    L12:
        this.bytesRemaining = ContentMetadata.CC.getContentLength(this.cache.getContentMetadata(r0));     // Catch: Throwable -> L36
        if (this.bytesRemaining == (-1)) goto L20;
        this.bytesRemaining -= r14.position;     // Catch: Throwable -> L36
        if (this.bytesRemaining >= 0) goto L20;
        throw new DataSourceException(2008);     // Catch: Throwable -> L36
    L5:
        r3 = false;
    L36:
        th = move-exception;
        handleBeforeThrow(th);
        throw th;
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r18, int r19, int r20) throws IOException {
        if (r20 != 0) goto L6;
        return 0;
    L6:
        if (this.bytesRemaining != 0) goto L8;
        return -1;
    L8:
        DataSpec r3 = (DataSpec) Assertions.checkNotNull(this.requestDataSpec);
        DataSpec r7 = (DataSpec) Assertions.checkNotNull(this.currentDataSpec);
    L40:
        th = th;
    L42:
        handleBeforeThrow(th);
        throw th;
    L10:
        if (this.readPosition < this.checkCachePosition) goto L12;
        openNextSource(r3, true);     // Catch: Throwable -> L40
    L12:
        int r8 = ((DataSource) Assertions.checkNotNull(this.currentDataSource)).read(r18, r19, r20);     // Catch: Throwable -> L38
        if (r8 == (-1)) goto L23;
        if (isReadingFromCache() == false) goto L19;
        this.totalCachedBytesRead += (long) r8;     // Catch: Throwable -> L38
    L19:
        this.readPosition += (long) r8;     // Catch: Throwable -> L38
        this.currentDataSourceBytesRead += (long) r8;     // Catch: Throwable -> L38
        if (this.bytesRemaining == (-1)) goto L35;
        this.bytesRemaining -= (long) r8;     // Catch: Throwable -> L38
    L35:
        return r8;
    L23:
        if (isReadingFromUpstream() == false) goto L31;
        if (r7.length != (-1)) goto L27;
    L28:
        setNoBytesRemainingAndMaybeStoreLength((String) Util.castNonNull(r3.key));     // Catch: Throwable -> L38
        goto L35
    L27:
        if (this.currentDataSourceBytesRead < r7.length) goto L28;
    L31:
        if (this.bytesRemaining <= 0) goto L33;
    L36:
        closeCurrentSource();     // Catch: Throwable -> L38
        openNextSource(r3, false);     // Catch: Throwable -> L38
        return read(r18, r19, r20);
    L33:
        if (this.bytesRemaining != (-1)) goto L35;
    L38:
        th = th;
        goto L42
    }
}
