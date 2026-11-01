package androidx.media3.datasource;

import androidx.media3.common.util.Assertions;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public final class DataSourceInputStream extends InputStream {
    private boolean closed;
    private final DataSource dataSource;
    private final DataSpec dataSpec;
    private boolean opened;
    private final byte[] singleByteArray;
    private long totalBytesRead;

    public DataSourceInputStream(DataSource r2, DataSpec r3) {
        this.opened = false;
        this.closed = false;
        this.dataSource = r2;
        this.dataSpec = r3;
        this.singleByteArray = new byte[1];
    }

    private void checkOpened() throws IOException {
        if (this.opened == true) goto L6;
        this.dataSource.open(this.dataSpec);
        this.opened = true;
        return;
    }

    public long bytesRead() {
        return this.totalBytesRead;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closed == true) goto L6;
        this.dataSource.close();
        this.closed = true;
        return;
    }

    public void open() throws IOException {
        checkOpened();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (read(this.singleByteArray) != (-1)) goto L6;
        return -1;
    L6:
        return this.singleByteArray[0] & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] r3) throws IOException {
        return read(r3, 0, r3.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] r6, int r7, int r8) throws IOException {
        Assertions.checkState(!this.closed);
        checkOpened();
        int r0 = this.dataSource.read(r6, r7, r8);
        if (r0 != (-1)) goto L5;
        return -1;
    L5:
        this.totalBytesRead += (long) r0;
        return r0;
    }
}
