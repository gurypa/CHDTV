package androidx.media3.datasource.cache;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
final class ReusableBufferedOutputStream extends BufferedOutputStream {
    private boolean closed;

    public ReusableBufferedOutputStream(OutputStream r1) {
        super(r1);
    }

    public ReusableBufferedOutputStream(OutputStream r1, int r2) {
        super(r1, r2);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.closed = true;
        Throwable r0 = null;
        flush();     // Catch: Throwable -> L5
    L15:
        this.out.close();     // Catch: Throwable -> L9
    L12:
        if (r0 == null) goto L19;
        Util.sneakyThrow(r0);
        return;
    L19:
        return;
    L9:
        th = move-exception;
        if (r0 != null) goto L12;
        r0 = th;
    L5:
        th = move-exception;
        r0 = th;
        goto L15
    }

    public void reset(OutputStream r2) {
        Assertions.checkState(this.closed);
        this.out = r2;
        this.count = 0;
        this.closed = false;
    }
}
