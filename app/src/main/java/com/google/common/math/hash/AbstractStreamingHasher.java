package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractStreamingHasher extends AbstractHasher {
    private final ByteBuffer buffer;
    private final int bufferSize;
    private final int chunkSize;

    protected AbstractStreamingHasher(int r1) {
        this(r1, r1);
    }

    protected AbstractStreamingHasher(int r3, int r4) {
        if ((r4 % r3) != 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        this.buffer = ByteBuffer.allocate(r4 + 7).order(ByteOrder.LITTLE_ENDIAN);
        this.bufferSize = r4;
        this.chunkSize = r3;
        return;
    L5:
        r0 = false;
        goto L6
    }

    private void munch() {
        Java8Compatibility.flip(this.buffer);
    L4:
        if (this.buffer.remaining() < this.chunkSize) goto L6;
        process(this.buffer);
        goto L4
    L6:
        this.buffer.compact();
    }

    private void munchIfFull() {
        if (this.buffer.remaining() >= 8) goto L6;
        munch();
        return;
    }

    private Hasher putBytesInternal(ByteBuffer r5) {
        if (r5.remaining() > this.buffer.remaining()) goto L6;
        this.buffer.put(r5);
        munchIfFull();
        return this;
    L6:
        int r0 = this.bufferSize - this.buffer.position();
        int r1 = 0;
    L7:
        if (r1 >= r0) goto L9;
        this.buffer.put(r5.get());
        r1 = r1 + 1;
        goto L7
    L9:
        munch();
    L11:
        if (r5.remaining() < this.chunkSize) goto L13;
        process(r5);
        goto L11
    L13:
        this.buffer.put(r5);
        return this;
    }

    @Override // com.google.common.hash.Hasher
    public final HashCode hash() {
        munch();
        Java8Compatibility.flip(this.buffer);
        if (this.buffer.remaining() <= 0) goto L6;
        processRemaining(this.buffer);
        Java8Compatibility.position(this.buffer, this.buffer.limit());
    L6:
        return makeHash();
    }

    protected abstract HashCode makeHash();

    protected abstract void process(ByteBuffer r1);

    protected void processRemaining(ByteBuffer r3) {
        Java8Compatibility.position(r3, r3.limit());
        Java8Compatibility.limit(r3, this.chunkSize + 7);
    L4:
        if (r3.position() >= this.chunkSize) goto L6;
        r3.putLong(0);
        goto L4
    L6:
        Java8Compatibility.limit(r3, this.chunkSize);
        Java8Compatibility.flip(r3);
        process(r3);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public final Hasher putByte(byte r2) {
        this.buffer.put(r2);
        munchIfFull();
        return this;
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putByte(byte r1) {
        return putByte(r1);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public final Hasher putBytes(ByteBuffer r3) {
        ByteOrder r0 = r3.order();
        r3.order(ByteOrder.LITTLE_ENDIAN);     // Catch: Throwable -> L6
        Hasher r1 = putBytesInternal(r3);     // Catch: Throwable -> L6
        r3.order(r0);
        return r1;
    L6:
        th = move-exception;
        r3.order(r0);
        throw th;
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public final Hasher putBytes(byte[] r3, int r4, int r5) {
        return putBytesInternal(ByteBuffer.wrap(r3, r4, r5).order(ByteOrder.LITTLE_ENDIAN));
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putBytes(ByteBuffer r1) {
        return putBytes(r1);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putBytes(byte[] r1, int r2, int r3) {
        return putBytes(r1, r2, r3);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public final Hasher putChar(char r2) {
        this.buffer.putChar(r2);
        munchIfFull();
        return this;
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putChar(char r1) {
        return putChar(r1);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public final Hasher putInt(int r2) {
        this.buffer.putInt(r2);
        munchIfFull();
        return this;
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putInt(int r1) {
        return putInt(r1);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public final Hasher putLong(long r2) {
        this.buffer.putLong(r2);
        munchIfFull();
        return this;
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putLong(long r1) {
        return putLong(r1);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public final Hasher putShort(short r2) {
        this.buffer.putShort(r2);
        munchIfFull();
        return this;
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putShort(short r1) {
        return putShort(r1);
    }
}
