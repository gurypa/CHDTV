package com.google.common.p003io;

import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedBytes;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class ReaderInputStream extends InputStream {
    private ByteBuffer byteBuffer;
    private CharBuffer charBuffer;
    private boolean doneFlushing;
    private boolean draining;
    private final CharsetEncoder encoder;
    private boolean endOfInput;
    private final Reader reader;
    private final byte[] singleByte;

    ReaderInputStream(Reader r3, Charset r4, int r5) {
        this(r3, r4.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE), r5);
    }

    ReaderInputStream(Reader r3, CharsetEncoder r4, int r5) {
        boolean r0 = true;
        this.singleByte = new byte[1];
        this.reader = (Reader) Preconditions.checkNotNull(r3);
        this.encoder = (CharsetEncoder) Preconditions.checkNotNull(r4);
        if (r5 > 0) goto L6;
        r0 = false;
    L6:
        Preconditions.checkArgument(r0, "bufferSize must be positive: %s", r5);
        r4.reset();
        this.charBuffer = CharBuffer.allocate(r5);
        Java8Compatibility.flip(this.charBuffer);
        this.byteBuffer = ByteBuffer.allocate(r5);
    }

    private static int availableCapacity(Buffer r2) {
        return r2.capacity() - r2.limit();
    }

    private int drain(byte[] r3, int r4, int r5) {
        int r0 = Math.min(r5, this.byteBuffer.remaining());
        this.byteBuffer.get(r3, r4, r0);
        return r0;
    }

    private static CharBuffer grow(CharBuffer r3) {
        CharBuffer r1 = CharBuffer.wrap(Arrays.copyOf(r3.array(), r3.capacity() * 2));
        Java8Compatibility.position(r1, r3.position());
        Java8Compatibility.limit(r1, r3.limit());
        return r1;
    }

    private void readMoreChars() throws IOException {
        if (availableCapacity(this.charBuffer) == 0) goto L5;
    L8:
        int r0 = this.charBuffer.limit();
        int r1 = this.reader.read(this.charBuffer.array(), r0, availableCapacity(this.charBuffer));
        if (r1 != (-1)) goto L11;
        this.endOfInput = true;
        return;
    L11:
        Java8Compatibility.limit(this.charBuffer, r0 + r1);
        return;
    L5:
        if (this.charBuffer.position() <= 0) goto L7;
        Java8Compatibility.flip(this.charBuffer.compact());
        goto L8
    L7:
        this.charBuffer = grow(this.charBuffer);
        goto L8
    }

    private void startDraining(boolean r2) {
        Java8Compatibility.flip(this.byteBuffer);
        if (r2 == true) goto L5;
    L7:
        this.draining = true;
        return;
    L5:
        if (this.byteBuffer.remaining() != 0) goto L7;
        this.byteBuffer = ByteBuffer.allocate(this.byteBuffer.capacity() * 2);
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.reader.close();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (read(this.singleByte) == 1) goto L7;
        return -1;
    L7:
        return UnsignedBytes.toInt(this.singleByte[0]);
    }

    @Override // java.io.InputStream
    public int read(byte[] r8, int r9, int r10) throws IOException {
        Preconditions.checkPositionIndexes(r9, r9 + r10, r8.length);
        if (r10 != 0) goto L5;
        return 0;
    L5:
        int r1 = 0;
        boolean r2 = this.endOfInput;
    L7:
        if (this.draining == false) goto L19;
        r1 = r1 + drain(r8, r9 + r1, r10 - r1);
        if (r1 == r10) goto L14;
        if (this.doneFlushing == true) goto L14;
        this.draining = false;
        Java8Compatibility.clear(this.byteBuffer);
    L14:
        if (r1 > 0) goto L57;
        return -1;
    L57:
        return r1;
    L19:
        if (this.doneFlushing == false) goto L21;
        CoderResult r3 = CoderResult.UNDERFLOW;
    L25:
        if (r3.isOverflow() == true) goto L26;
        if (r3.isUnderflow() == true) goto L29;
        if (r3.isError() == false) goto L19;
        r3.throwException();
        return 0;
    L29:
        if (r2 == true) goto L30;
        if (this.endOfInput == true) goto L33;
        readMoreChars();
        goto L19
    L33:
        r2 = true;
        goto L19
    L30:
        this.doneFlushing = true;
        startDraining(false);
        goto L7
    L26:
        startDraining(true);
        goto L7
    L21:
        if (r2 == false) goto L23;
        r3 = this.encoder.flush(this.byteBuffer);
        goto L25
    L23:
        r3 = this.encoder.encode(this.charBuffer, this.byteBuffer, this.endOfInput);
        goto L25
    }
}
