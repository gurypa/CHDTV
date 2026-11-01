package es.antonio.m3u;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes3.dex */
final class BackupOutput extends FilterOutputStream {
    BackupOutput(OutputStream r1) {
        super(r1);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        flush();
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] r2) throws IOException {
        this.out.write(r2);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] r2, int r3, int r4) throws IOException {
        this.out.write(r2, r3, r4);
    }
}
