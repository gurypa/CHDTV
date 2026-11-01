package androidx.media3.datasource.cache;

import androidx.media3.datasource.DataSourceUtil;
import androidx.media3.datasource.DataSpec;
import java.io.IOException;
import java.io.InterruptedIOException;

/* JADX INFO: loaded from: classes.dex */
public final class CacheWriter {
    public static final int DEFAULT_BUFFER_SIZE_BYTES = 131072;
    private long bytesCached;
    private final Cache cache;
    private final String cacheKey;
    private final CacheDataSource dataSource;
    private final DataSpec dataSpec;
    private long endPosition;
    private volatile boolean isCanceled;
    private long nextPosition;
    private final ProgressListener progressListener;
    private final byte[] temporaryBuffer;

    public interface ProgressListener {
        void onProgress(long r1, long r3, long r5);
    }

    public CacheWriter(CacheDataSource r3, DataSpec r4, byte[] r5, ProgressListener r6) {
        this.dataSource = r3;
        this.cache = r3.getCache();
        this.dataSpec = r4;
        if (r5 != null) goto L5;
        byte[] r0 = new byte[131072];
    L6:
        this.temporaryBuffer = r0;
        this.progressListener = r6;
        this.cacheKey = r3.getCacheKeyFactory().buildCacheKey(r4);
        this.nextPosition = r4.position;
        return;
    L5:
        r0 = r5;
        goto L6
    }

    private long getLength() {
        if (this.endPosition != (-1)) goto L6;
        return -1;
    L6:
        return this.endPosition - this.dataSpec.position;
    }

    private void onNewBytesCached(long r9) {
        this.bytesCached += r9;
        if (this.progressListener == null) goto L6;
        this.progressListener.onProgress(getLength(), this.bytesCached, r9);
        return;
    }

    private void onRequestEndPosition(long r9) {
        if (this.endPosition != r9) goto L5;
        return;
    L5:
        this.endPosition = r9;
        if (this.progressListener == null) goto L9;
        this.progressListener.onProgress(getLength(), this.bytesCached, 0);
        return;
    }

    private long readBlockToCache(long r12, long r14) throws IOException {
        if ((r12 + r14) != this.endPosition) goto L5;
    L8:
        boolean r0 = true;
    L9:
        long r4 = -1;
        boolean r6 = false;
        if (r14 != (-1)) goto L46;
    L16:
        if (r6 == true) goto L23;
        throwIfCanceled();
        r4 = this.dataSource.open(this.dataSpec.buildUpon().setPosition(r12).setLength(-1).build());     // Catch: IOException -> L20
    L20:
        e = move-exception;
        DataSourceUtil.closeQuietly(this.dataSource);
        throw e;
    L23:
        int r7 = 0;
        if (r0 == true) goto L26;
    L31:
        int r2 = 0;
    L33:
        if (r2 == (-1)) goto L37;
        throwIfCanceled();     // Catch: IOException -> L29
        r2 = this.dataSource.read(this.temporaryBuffer, 0, this.temporaryBuffer.length);     // Catch: IOException -> L29
        if (r2 == (-1)) goto L33;
        onNewBytesCached(r2);     // Catch: IOException -> L29
        r7 = r7 + r2;     // Catch: IOException -> L29
        goto L33
    L37:
        if (r0 == false) goto L42;
        onRequestEndPosition(((long) r7) + r12);     // Catch: IOException -> L29
    L29:
        e = move-exception;
        DataSourceUtil.closeQuietly(this.dataSource);
        throw e;
    L42:
        this.dataSource.close();
        return r7;
    L26:
        if (r4 == (-1)) goto L31;
        onRequestEndPosition(r12 + r4);     // Catch: IOException -> L29
        goto L31
    L46:
        r4 = this.dataSource.open(this.dataSpec.buildUpon().setPosition(r12).setLength(r14).build());     // Catch: IOException -> L14
        r6 = true;
    L15:
        DataSourceUtil.closeQuietly(this.dataSource);
        goto L16
    L5:
        if (r14 == (-1)) goto L8;
        r0 = false;
        goto L9
    }

    private void throwIfCanceled() throws InterruptedIOException {
        if (this.isCanceled == true) goto L6;
        return;
    L6:
        throw new InterruptedIOException();
    }

    public void cache() throws IOException {
        throwIfCanceled();
        this.bytesCached = this.cache.getCachedBytes(this.cacheKey, this.dataSpec.position, this.dataSpec.length);
        if (this.dataSpec.length == (-1)) goto L5;
        this.endPosition = this.dataSpec.position + this.dataSpec.length;
    L11:
        if (this.progressListener == null) goto L14;
        this.progressListener.onProgress(getLength(), this.bytesCached, 0);
    L14:
        if (this.endPosition != (-1)) goto L16;
    L19:
        throwIfCanceled();
        if (this.endPosition != (-1)) goto L22;
        long r0 = Long.MAX_VALUE;
    L23:
        long r1 = this.cache.getCachedLength(this.cacheKey, this.nextPosition, r0);
        if (r1 > 0) goto L25;
        long r2 = -r1;
        if (r2 != Long.MAX_VALUE) goto L29;
        long r4 = -1;
    L30:
        this.nextPosition += readBlockToCache(this.nextPosition, r4);
        goto L14
    L29:
        r4 = r2;
        goto L30
    L25:
        this.nextPosition += r1;
        goto L14
    L22:
        r0 = this.endPosition - this.nextPosition;
        goto L23
    L16:
        if (this.nextPosition < this.endPosition) goto L19;
        return;
    L5:
        long r3 = ContentMetadata.CC.getContentLength(this.cache.getContentMetadata(this.cacheKey));
        if (r3 != (-1)) goto L8;
        long r5 = -1;
    L9:
        this.endPosition = r5;
        goto L11
    L8:
        r5 = r3;
        goto L9
    }

    public void cancel() {
        this.isCanceled = true;
    }
}
