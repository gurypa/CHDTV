package androidx.media3.datasource.cache;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSink;
import androidx.media3.datasource.DataSpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
public final class CacheDataSink implements DataSink {
    public static final int DEFAULT_BUFFER_SIZE = 20480;
    public static final long DEFAULT_FRAGMENT_SIZE = 5242880;
    private static final long MIN_RECOMMENDED_FRAGMENT_SIZE = 2097152;
    private static final String TAG = "CacheDataSink";
    private final int bufferSize;
    private ReusableBufferedOutputStream bufferedOutputStream;
    private final Cache cache;
    private DataSpec dataSpec;
    private long dataSpecBytesWritten;
    private long dataSpecFragmentSize;
    private File file;
    private final long fragmentSize;
    private OutputStream outputStream;
    private long outputStreamBytesWritten;

    public static final class CacheDataSinkException extends Cache.CacheException {
        public CacheDataSinkException(IOException r1) {
            super(r1);
        }
    }

    public static final class Factory implements DataSink.Factory {
        private int bufferSize;
        private Cache cache;
        private long fragmentSize;

        public Factory() {
            this.fragmentSize = CacheDataSink.DEFAULT_FRAGMENT_SIZE;
            this.bufferSize = CacheDataSink.DEFAULT_BUFFER_SIZE;
        }

        @Override // androidx.media3.datasource.DataSink.Factory
        public DataSink createDataSink() {
            return new CacheDataSink((Cache) Assertions.checkNotNull(this.cache), this.fragmentSize, this.bufferSize);
        }

        public Factory setBufferSize(int r1) {
            this.bufferSize = r1;
            return this;
        }

        public Factory setCache(Cache r1) {
            this.cache = r1;
            return this;
        }

        public Factory setFragmentSize(long r1) {
            this.fragmentSize = r1;
            return this;
        }
    }

    public CacheDataSink(Cache r2, long r3) {
        this(r2, r3, DEFAULT_BUFFER_SIZE);
    }

    public CacheDataSink(Cache r6, long r7, int r9) {
        if (r7 <= 0) goto L5;
    L8:
        boolean r0 = true;
    L9:
        Assertions.checkState(r0, "fragmentSize must be positive or C.LENGTH_UNSET.");
        if (r7 != (-1)) goto L12;
    L14:
        this.cache = (Cache) Assertions.checkNotNull(r6);
        if (r7 != (-1)) goto L17;
        long r1 = Long.MAX_VALUE;
    L18:
        this.fragmentSize = r1;
        this.bufferSize = r9;
        return;
    L17:
        r1 = r7;
        goto L18
    L12:
        if (r7 >= 2097152) goto L14;
        Log.m48w(TAG, "fragmentSize is below the minimum recommended value of 2097152. This may cause poor cache performance.");
        goto L14
    L5:
        if (r7 == (-1)) goto L8;
        r0 = false;
        goto L9
    }

    private void closeCurrentOutputStream() throws IOException {
        if (this.outputStream != null) goto L19;
        return;
    L19:
        this.outputStream.flush();     // Catch: Throwable -> L13
        Util.closeQuietly(this.outputStream);
        this.outputStream = null;
        File r2 = (File) Util.castNonNull(this.file);
        this.file = null;
        if (1 == 0) goto L10;
        this.cache.commitFile(r2, this.outputStreamBytesWritten);
        return;
    L10:
        r2.delete();
        return;
    L13:
        th = move-exception;
        Util.closeQuietly(this.outputStream);
        this.outputStream = null;
        File r3 = (File) Util.castNonNull(this.file);
        this.file = null;
        if (0 == 0) goto L17;
        this.cache.commitFile(r3, this.outputStreamBytesWritten);
    L18:
        throw th;
    L17:
        r3.delete();
        goto L18
    }

    private void openNextOutputStream(DataSpec r11) throws IOException {
        long r2 = -1;
        if (r11.length == (-1)) goto L6;
        r2 = Math.min(r11.length - this.dataSpecBytesWritten, this.dataSpecFragmentSize);
    L6:
        long r8 = r2;
        this.file = this.cache.startFile((String) Util.castNonNull(r11.key), r11.position + this.dataSpecBytesWritten, r8);
        FileOutputStream r0 = new FileOutputStream(this.file);
        if (this.bufferSize > 0) goto L9;
        this.outputStream = r0;
    L14:
        this.outputStreamBytesWritten = 0;
        return;
    L9:
        if (this.bufferedOutputStream != null) goto L11;
        this.bufferedOutputStream = new ReusableBufferedOutputStream(r0, this.bufferSize);
    L12:
        this.outputStream = this.bufferedOutputStream;
        goto L14
    L11:
        this.bufferedOutputStream.reset(r0);
        goto L12
    }

    @Override // androidx.media3.datasource.DataSink
    public void close() throws CacheDataSinkException {
        if (this.dataSpec != null) goto L10;
        return;
    L10:
        closeCurrentOutputStream();     // Catch: IOException -> L7
        return;
    L7:
        e = move-exception;
        throw new CacheDataSinkException(e);
    }

    @Override // androidx.media3.datasource.DataSink
    public void open(DataSpec r5) throws CacheDataSinkException {
        Assertions.checkNotNull(r5.key);
        if (r5.length == (-1)) goto L5;
    L8:
        this.dataSpec = r5;
        if (r5.isFlagSet(4) == false) goto L11;
        long r0 = this.fragmentSize;
    L12:
        this.dataSpecFragmentSize = r0;
        this.dataSpecBytesWritten = 0;
        openNextOutputStream(r5);     // Catch: IOException -> L15
        return;
    L15:
        e = move-exception;
        throw new CacheDataSinkException(e);
    L11:
        r0 = Long.MAX_VALUE;
        goto L12
    L5:
        if (r5.isFlagSet(2) == false) goto L8;
        this.dataSpec = null;
    }

    @Override // androidx.media3.datasource.DataSink
    public void write(byte[] r9, int r10, int r11) throws CacheDataSinkException {
        DataSpec r0 = this.dataSpec;
        if (r0 != null) goto L5;
        return;
    L5:
        int r1 = 0;
    L6:
        if (r1 >= r11) goto L16;
        if (this.outputStreamBytesWritten != this.dataSpecFragmentSize) goto L10;
        closeCurrentOutputStream();     // Catch: IOException -> L12
        openNextOutputStream(r0);     // Catch: IOException -> L12
    L10:
        int r2 = (int) Math.min(r11 - r1, this.dataSpecFragmentSize - this.outputStreamBytesWritten);     // Catch: IOException -> L12
        ((OutputStream) Util.castNonNull(this.outputStream)).write(r9, r10 + r1, r2);     // Catch: IOException -> L12
        r1 = r1 + r2;     // Catch: IOException -> L12
        this.outputStreamBytesWritten += (long) r2;     // Catch: IOException -> L12
        this.dataSpecBytesWritten += (long) r2;     // Catch: IOException -> L12
    L12:
        e = move-exception;
        throw new CacheDataSinkException(e);
    }
}
