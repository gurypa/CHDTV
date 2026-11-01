package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class HashingOutputStream extends FilterOutputStream {
    private final Hasher hasher;

    public HashingOutputStream(HashFunction r2, OutputStream r3) {
        super((OutputStream) Preconditions.checkNotNull(r3));
        this.hasher = (Hasher) Preconditions.checkNotNull(r2.newHasher());
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.out.close();
    }

    public HashCode hash() {
        return this.hasher.hash();
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int r3) throws IOException {
        this.hasher.putByte((byte) r3);
        this.out.write(r3);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] r2, int r3, int r4) throws IOException {
        this.hasher.putBytes(r2, r3, r4);
        this.out.write(r2, r3, r4);
    }
}
