package com.google.common.p003io;

import com.google.common.base.Preconditions;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class AppendableWriter extends Writer {
    private boolean closed;
    private final Appendable target;

    AppendableWriter(Appendable r2) {
        this.target = (Appendable) Preconditions.checkNotNull(r2);
    }

    private void checkNotClosed() throws IOException {
        if (this.closed == true) goto L6;
        return;
    L6:
        throw new IOException("Cannot write to a closed writer.");
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(char r2) throws IOException {
        checkNotClosed();
        this.target.append(r2);
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(@CheckForNull CharSequence r2) throws IOException {
        checkNotClosed();
        this.target.append(r2);
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(@CheckForNull CharSequence r2, int r3, int r4) throws IOException {
        checkNotClosed();
        this.target.append(r2, r3, r4);
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public /* bridge */ /* synthetic */ Appendable append(char r1) throws IOException {
        return append(r1);
    }

    @Override // java.io.Writer, java.lang.Appendable
    public /* bridge */ /* synthetic */ Appendable append(@CheckForNull CharSequence r1) throws IOException {
        return append(r1);
    }

    @Override // java.io.Writer, java.lang.Appendable
    public /* bridge */ /* synthetic */ Appendable append(@CheckForNull CharSequence r1, int r2, int r3) throws IOException {
        return append(r1, r2, r3);
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.closed = true;
        if ((this.target instanceof Closeable) == false) goto L6;
        ((Closeable) this.target).close();
        return;
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        checkNotClosed();
        if ((this.target instanceof Flushable) == false) goto L6;
        ((Flushable) this.target).flush();
        return;
    }

    @Override // java.io.Writer
    public void write(int r3) throws IOException {
        checkNotClosed();
        this.target.append((char) r3);
    }

    @Override // java.io.Writer
    public void write(String r2) throws IOException {
        Preconditions.checkNotNull(r2);
        checkNotClosed();
        this.target.append(r2);
    }

    @Override // java.io.Writer
    public void write(String r3, int r4, int r5) throws IOException {
        Preconditions.checkNotNull(r3);
        checkNotClosed();
        this.target.append(r3, r4, r4 + r5);
    }

    @Override // java.io.Writer
    public void write(char[] r3, int r4, int r5) throws IOException {
        checkNotClosed();
        this.target.append(new String(r3, r4, r5));
    }
}
