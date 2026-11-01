package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.Immutable;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
@Immutable
@ElementTypesAreNonnullByDefault
abstract class AbstractNonStreamingHashFunction extends AbstractHashFunction {

    private final class BufferingHasher extends AbstractHasher {
        final ExposedByteArrayOutputStream stream;
        final /* synthetic */ AbstractNonStreamingHashFunction this$0;

        BufferingHasher(AbstractNonStreamingHashFunction r1, int r2) {
            this.this$0 = r1;
            this.stream = new ExposedByteArrayOutputStream(r2);
        }

        @Override // com.google.common.hash.Hasher
        public HashCode hash() {
            return this.this$0.hashBytes(this.stream.byteArray(), 0, this.stream.length());
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putByte(byte r2) {
            this.stream.write(r2);
            return this;
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public /* bridge */ /* synthetic */ PrimitiveSink putByte(byte r1) {
            return putByte(r1);
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putBytes(ByteBuffer r2) {
            this.stream.write(r2);
            return this;
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putBytes(byte[] r2, int r3, int r4) {
            this.stream.write(r2, r3, r4);
            return this;
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
        public /* bridge */ /* synthetic */ PrimitiveSink putBytes(ByteBuffer r1) {
            return putBytes(r1);
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.PrimitiveSink
        public /* bridge */ /* synthetic */ PrimitiveSink putBytes(byte[] r1, int r2, int r3) {
            return putBytes(r1, r2, r3);
        }
    }

    private static final class ExposedByteArrayOutputStream extends ByteArrayOutputStream {
        ExposedByteArrayOutputStream(int r1) {
            super(r1);
        }

        byte[] byteArray() {
            return this.buf;
        }

        int length() {
            return this.count;
        }

        void write(ByteBuffer r4) {
            int r0 = r4.remaining();
            if ((this.count + r0) <= this.buf.length) goto L5;
            this.buf = Arrays.copyOf(this.buf, this.count + r0);
        L5:
            r4.get(this.buf, this.count, r0);
            this.count += r0;
        }
    }

    AbstractNonStreamingHashFunction() {
    }

    @Override // com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public HashCode hashBytes(ByteBuffer r2) {
        return newHasher(r2.remaining()).putBytes(r2).hash();
    }

    @Override // com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public abstract HashCode hashBytes(byte[] r1, int r2, int r3);

    @Override // com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public HashCode hashInt(int r3) {
        return hashBytes(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(r3).array());
    }

    @Override // com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public HashCode hashLong(long r3) {
        return hashBytes(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(r3).array());
    }

    @Override // com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public HashCode hashString(CharSequence r2, Charset r3) {
        return hashBytes(r2.toString().getBytes(r3));
    }

    @Override // com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public HashCode hashUnencodedChars(CharSequence r5) {
        int r0 = r5.length();
        ByteBuffer r1 = ByteBuffer.allocate(r0 * 2).order(ByteOrder.LITTLE_ENDIAN);
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L6;
        r1.putChar(r5.charAt(r2));
        r2 = r2 + 1;
        goto L3
    L6:
        return hashBytes(r1.array());
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        return newHasher(32);
    }

    @Override // com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public Hasher newHasher(int r2) {
        if (r2 < 0) goto L4;
        boolean r0 = true;
    L5:
        Preconditions.checkArgument(r0);
        return new BufferingHasher(this, r2);
    L4:
        r0 = false;
        goto L5
    }
}
