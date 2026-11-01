package com.google.common.p003io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.Objects;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class CharSequenceReader extends Reader {
    private int mark;
    private int pos;

    @CheckForNull
    private CharSequence seq;

    public CharSequenceReader(CharSequence r2) {
        this.seq = (CharSequence) Preconditions.checkNotNull(r2);
    }

    private void checkOpen() throws IOException {
        if (this.seq == null) goto L6;
        return;
    L6:
        throw new IOException("reader closed");
    }

    private boolean hasRemaining() {
        if (remaining() <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    private int remaining() {
        Objects.requireNonNull(this.seq);
        return this.seq.length() - this.pos;
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        monitor-enter(this);
        this.seq = null;     // Catch: Throwable -> L7
        monitor-exit(this);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // java.io.Reader
    public synchronized void mark(int r3) throws IOException {
        monitor-enter(this);
        if (r3 < 0) goto L5;
        boolean r0 = true;
    L12:
        Preconditions.checkArgument(r0, "readAheadLimit (%s) may not be negative", r3);     // Catch: Throwable -> L9
        checkOpen();     // Catch: Throwable -> L9
        this.mark = this.pos;     // Catch: Throwable -> L9
        monitor-exit(this);
        return;
    L9:
        th = move-exception;
        throw th;
    L5:
        r0 = false;
        goto L12
    }

    @Override // java.io.Reader
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.Reader
    public synchronized int read() throws IOException {
        monitor-enter(this);
        checkOpen();     // Catch: Throwable -> L10
        Objects.requireNonNull(this.seq);     // Catch: Throwable -> L10
        if (hasRemaining() == false) goto L7;
        CharSequence r0 = this.seq;     // Catch: Throwable -> L10
        int r1 = this.pos;     // Catch: Throwable -> L10
        this.pos = r1 + 1;     // Catch: Throwable -> L10
        int r2 = r0.charAt(r1);     // Catch: Throwable -> L10
    L8:
        monitor-exit(this);
        return r2;
    L7:
        r2 = -1;
    L10:
        th = move-exception;
        throw th;
    }

    @Override // java.io.Reader, java.lang.Readable
    public synchronized int read(CharBuffer r6) throws IOException {
        monitor-enter(this);
        Preconditions.checkNotNull(r6);     // Catch: Throwable -> L14
        checkOpen();     // Catch: Throwable -> L14
        Objects.requireNonNull(this.seq);     // Catch: Throwable -> L14
        if (hasRemaining() == true) goto L8;
        monitor-exit(this);
        return -1;
    L8:
        int r0 = Math.min(r6.remaining(), remaining());     // Catch: Throwable -> L14
        int r1 = 0;
    L9:
        if (r1 >= r0) goto L12;
        CharSequence r2 = this.seq;     // Catch: Throwable -> L14
        int r3 = this.pos;     // Catch: Throwable -> L14
        this.pos = r3 + 1;     // Catch: Throwable -> L14
        r6.put(r2.charAt(r3));     // Catch: Throwable -> L14
        r1 = r1 + 1;
        goto L9
    L12:
        monitor-exit(this);
        return r0;
    L14:
        th = move-exception;
        throw th;
    }

    @Override // java.io.Reader
    public synchronized int read(char[] r7, int r8, int r9) throws IOException {
        monitor-enter(this);
        int r0 = r8 + r9;
        Preconditions.checkPositionIndexes(r8, r0, r7.length);     // Catch: Throwable -> L15
        checkOpen();     // Catch: Throwable -> L15
        Objects.requireNonNull(this.seq);     // Catch: Throwable -> L15
        if (hasRemaining() == true) goto L9;
        monitor-exit(this);
        return -1;
    L9:
        int r1 = Math.min(r9, remaining());     // Catch: Throwable -> L15
        int r2 = 0;
    L10:
        if (r2 >= r1) goto L13;
        CharSequence r3 = this.seq;     // Catch: Throwable -> L15
        int r4 = this.pos;     // Catch: Throwable -> L15
        this.pos = r4 + 1;     // Catch: Throwable -> L15
        r7[r8 + r2] = r3.charAt(r4);     // Catch: Throwable -> L15
        r2 = r2 + 1;
        goto L10
    L13:
        monitor-exit(this);
        return r1;
    L15:
        th = move-exception;
        throw th;
    }

    @Override // java.io.Reader
    public synchronized boolean ready() throws IOException {
        monitor-enter(this);
        checkOpen();     // Catch: Throwable -> L7
        monitor-exit(this);
        return true;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // java.io.Reader
    public synchronized void reset() throws IOException {
        monitor-enter(this);
        checkOpen();     // Catch: Throwable -> L6
        this.pos = this.mark;     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // java.io.Reader
    public synchronized long skip(long r4) throws IOException {
        monitor-enter(this);
        if (r4 < 0) goto L6;
        boolean r0 = true;
    L14:
        Preconditions.checkArgument(r0, "n (%s) may not be negative", r4);     // Catch: Throwable -> L11
        checkOpen();     // Catch: Throwable -> L11
        int r1 = (int) Math.min(remaining(), r4);     // Catch: Throwable -> L11
        this.pos += r1;     // Catch: Throwable -> L11
        long r2 = r1;
        monitor-exit(this);
        return r2;
    L11:
        th = move-exception;
        throw th;
    L6:
        r0 = false;
        goto L14
    }
}
