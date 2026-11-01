package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class HashingInputStream extends FilterInputStream {
    private final Hasher hasher;

    public HashingInputStream(HashFunction r2, InputStream r3) {
        super((InputStream) Preconditions.checkNotNull(r3));
        this.hasher = (Hasher) Preconditions.checkNotNull(r2.newHasher());
    }

    public HashCode hash() {
        return this.hasher.hash();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void mark(int r1) {
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int r0 = this.in.read();
        if (r0 == (-1)) goto L5;
        this.hasher.putByte((byte) r0);
    L5:
        return r0;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] r3, int r4, int r5) throws IOException {
        int r0 = this.in.read(r3, r4, r5);
        if (r0 == (-1)) goto L5;
        this.hasher.putBytes(r3, r4, r0);
    L5:
        return r0;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void reset() throws IOException {
        throw new IOException("reset not supported");
    }
}
