package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractHasher implements Hasher {
    AbstractHasher() {
    }

    @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public final Hasher putBoolean(boolean r2) {
        return putByte(r2 ? 1 : 0);
    }

    @Override // com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putBoolean(boolean r1) {
        return putBoolean(r1);
    }

    @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putByte(byte r1) {
        return Hasher.CC.$default$putByte(this, r1);
    }

    @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putBytes(ByteBuffer r4) {
        if (r4.hasArray() == false) goto L5;
        putBytes(r4.array(), r4.arrayOffset() + r4.position(), r4.remaining());
        Java8Compatibility.position(r4, r4.limit());
    L8:
        return this;
    L5:
        int r0 = r4.remaining();
    L6:
        if (r0 <= 0) goto L8;
        putByte(r4.get());
        r0 = r0 - 1;
        goto L6
    }

    @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putBytes(byte[] r3) {
        return putBytes(r3, 0, r3.length);
    }

    @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putBytes(byte[] r3, int r4, int r5) {
        Preconditions.checkPositionIndexes(r4, r4 + r5, r3.length);
        int r0 = 0;
    L3:
        if (r0 >= r5) goto L5;
        putByte(r3[r4 + r0]);
        r0 = r0 + 1;
        goto L3
    L5:
        return this;
    }

    @Override // com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putBytes(ByteBuffer r1) {
        return putBytes(r1);
    }

    @Override // com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putBytes(byte[] r1) {
        return putBytes(r1);
    }

    @Override // com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putBytes(byte[] r1, int r2, int r3) {
        return putBytes(r1, r2, r3);
    }

    @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putChar(char r2) {
        putByte((byte) r2);
        putByte((byte) (r2 >>> '\b'));
        return this;
    }

    @Override // com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putChar(char r1) {
        return putChar(r1);
    }

    @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public final Hasher putDouble(double r3) {
        return putLong(Double.doubleToRawLongBits(r3));
    }

    @Override // com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putDouble(double r1) {
        return putDouble(r1);
    }

    @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public final Hasher putFloat(float r2) {
        return putInt(Float.floatToRawIntBits(r2));
    }

    @Override // com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putFloat(float r1) {
        return putFloat(r1);
    }

    @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putInt(int r2) {
        putByte((byte) r2);
        putByte((byte) (r2 >>> 8));
        putByte((byte) (r2 >>> 16));
        putByte((byte) (r2 >>> 24));
        return this;
    }

    @Override // com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putInt(int r1) {
        return putInt(r1);
    }

    @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putLong(long r4) {
        int r0 = 0;
    L4:
        if (r0 >= 64) goto L6;
        putByte((byte) (r4 >>> r0));
        r0 = r0 + 8;
        goto L4
    L6:
        return this;
    }

    @Override // com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putLong(long r1) {
        return putLong(r1);
    }

    @Override // com.google.common.hash.Hasher
    public <T> Hasher putObject(@ParametricNullness T r1, Funnel<? super T> r2) {
        r2.funnel(r1, this);
        return this;
    }

    @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putShort(short r2) {
        putByte((byte) r2);
        putByte((byte) (r2 >>> 8));
        return this;
    }

    @Override // com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putShort(short r1) {
        return putShort(r1);
    }

    @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putString(CharSequence r2, Charset r3) {
        return putBytes(r2.toString().getBytes(r3));
    }

    @Override // com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putString(CharSequence r1, Charset r2) {
        return putString(r1, r2);
    }

    @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
    public Hasher putUnencodedChars(CharSequence r4) {
        int r0 = 0;
        int r1 = r4.length();
    L3:
        if (r0 >= r1) goto L5;
        putChar(r4.charAt(r0));
        r0 = r0 + 1;
        goto L3
    L5:
        return this;
    }

    @Override // com.google.common.hash.PrimitiveSink
    public /* bridge */ /* synthetic */ PrimitiveSink putUnencodedChars(CharSequence r1) {
        return putUnencodedChars(r1);
    }
}
