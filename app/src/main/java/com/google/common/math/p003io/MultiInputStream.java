package com.google.common.p003io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class MultiInputStream extends InputStream {

    /* JADX INFO: renamed from: in */
    @CheckForNull
    private InputStream f160in;

    /* JADX INFO: renamed from: it */
    private Iterator<? extends ByteSource> f161it;

    public MultiInputStream(Iterator<? extends ByteSource> r2) throws IOException {
        this.f161it = (Iterator) Preconditions.checkNotNull(r2);
        advance();
    }

    private void advance() throws IOException {
        close();
        if (this.f161it.hasNext() == false) goto L6;
        this.f160in = this.f161it.next().openStream();
        return;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.f160in != null) goto L7;
        return 0;
    L7:
        return this.f160in.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.f160in != null) goto L11;
        return;
    L11:
        this.f160in.close();     // Catch: Throwable -> L7
        this.f160in = null;
        return;
    L7:
        th = move-exception;
        this.f160in = null;
        throw th;
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
    L3:
        if (this.f160in == null) goto L8;
        int r0 = this.f160in.read();
        if (r0 != (-1)) goto L6;
        advance();
        goto L3
    L6:
        return r0;
    L8:
        return -1;
    }

    @Override // java.io.InputStream
    public int read(byte[] r3, int r4, int r5) throws IOException {
        Preconditions.checkNotNull(r3);
    L4:
        if (this.f160in == null) goto L9;
        int r0 = this.f160in.read(r3, r4, r5);
        if (r0 != (-1)) goto L7;
        advance();
        goto L4
    L7:
        return r0;
    L9:
        return -1;
    }

    @Override // java.io.InputStream
    public long skip(long r8) throws IOException {
        if (this.f160in != null) goto L5;
    L15:
        return 0;
    L5:
        if (r8 <= 0) goto L15;
        long r3 = this.f160in.skip(r8);
        if (r3 == 0) goto L11;
        return r3;
    L11:
        if (read() != (-1)) goto L14;
        return 0;
    L14:
        return this.f160in.skip(r8 - 1) + 1;
    }
}
