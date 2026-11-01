package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractByteHasher extends AbstractHasher {
    private final ByteBuffer scratch;

    AbstractByteHasher() {
        this.scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
    }

    private Hasher update(int r3) {
        update(this.scratch.array(), 0, r3);     // Catch: Throwable -> L5
        Java8Compatibility.clear(this.scratch);
        return this;
    L5:
        th = move-exception;
        Java8Compatibility.clear(this.scratch);
        throw th;
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putByte(byte r1) {
        update(r1);
        return this;
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putByte(byte r1) {
        return putByte(r1);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putBytes(ByteBuffer r1) {
        update(r1);
        return this;
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putBytes(byte[] r1) {
        Preconditions.checkNotNull(r1);
        update(r1);
        return this;
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putBytes(byte[] r3, int r4, int r5) {
        Preconditions.checkPositionIndexes(r4, r4 + r5, r3.length);
        update(r3, r4, r5);
        return this;
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putBytes(ByteBuffer r1) {
        return putBytes(r1);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putBytes(byte[] r1) {
        return putBytes(r1);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putBytes(byte[] r1, int r2, int r3) {
        return putBytes(r1, r2, r3);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putChar(char r2) {
        this.scratch.putChar(r2);
        return update(2);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putChar(char r1) {
        return putChar(r1);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putInt(int r2) {
        this.scratch.putInt(r2);
        return update(4);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putInt(int r1) {
        return putInt(r1);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putLong(long r2) {
        this.scratch.putLong(r2);
        return update(8);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putLong(long r1) {
        return putLong(r1);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putShort(short r2) {
        this.scratch.putShort(r2);
        return update(2);
    }

    @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putShort(short r1) {
        return putShort(r1);
    }

    protected abstract void update(byte r1);

    protected void update(ByteBuffer r4) {
        if (r4.hasArray() == false) goto L5;
        update(r4.array(), r4.arrayOffset() + r4.position(), r4.remaining());
        Java8Compatibility.position(r4, r4.limit());
        return;
    L5:
        int r0 = r4.remaining();
    L6:
        if (r0 <= 0) goto L10;
        update(r4.get());
        r0 = r0 - 1;
        goto L6
    }

    protected void update(byte[] r3) {
        update(r3, 0, r3.length);
    }

    protected void update(byte[] r3, int r4, int r5) {
        int r0 = r4;
    L4:
        if (r0 >= (r4 + r5)) goto L6;
        update(r3[r0]);
        r0 = r0 + 1;
        goto L4
    }
}
