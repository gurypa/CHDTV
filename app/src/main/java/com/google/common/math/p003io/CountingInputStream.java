package com.google.common.p003io;

import com.google.common.base.Preconditions;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class CountingInputStream extends FilterInputStream {
    private long count;
    private long mark;

    public CountingInputStream(InputStream r3) {
        super((InputStream) Preconditions.checkNotNull(r3));
        this.mark = -1;
    }

    public long getCount() {
        return this.count;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void mark(int r3) {
        monitor-enter(this);
        this.in.mark(r3);     // Catch: Throwable -> L6
        this.mark = this.count;     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int r0 = this.in.read();
        if (r0 == (-1)) goto L5;
        this.count++;
    L5:
        return r0;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] r6, int r7, int r8) throws IOException {
        int r0 = this.in.read(r6, r7, r8);
        if (r0 == (-1)) goto L5;
        this.count += (long) r0;
    L5:
        return r0;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        monitor-enter(this);
    L14:
        th = move-exception;
        throw th;
    L4:
        if (this.in.markSupported() == false) goto L13;
        if (this.mark == (-1)) goto L11;
        this.in.reset();     // Catch: Throwable -> L14
        this.count = this.mark;     // Catch: Throwable -> L14
        monitor-exit(this);
        return;
    L11:
        throw new IOException("Mark not set");     // Catch: Throwable -> L14
    L13:
        throw new IOException("Mark not supported");     // Catch: Throwable -> L14
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long r5) throws IOException {
        long r0 = this.in.skip(r5);
        this.count += r0;
        return r0;
    }
}
