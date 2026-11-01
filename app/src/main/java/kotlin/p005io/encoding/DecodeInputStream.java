package kotlin.p005io.encoding;

import java.io.IOException;
import java.io.InputStream;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Base64IOStream.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u000f\b\u0003\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0013\u001a\u00020\u0014H\u0016J \u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\tH\u0002J(\u0010\u0019\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\t2\u0006\u0010\u001b\u001a\u00020\tH\u0002J\u0010\u0010\u001c\u001a\u00020\t2\u0006\u0010\u001b\u001a\u00020\tH\u0002J\b\u0010\u001d\u001a\u00020\tH\u0016J \u0010\u001d\u001a\u00020\t2\u0006\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\tH\u0016J\b\u0010 \u001a\u00020\tH\u0002J\b\u0010!\u001a\u00020\u0014H\u0002J\b\u0010\"\u001a\u00020\u0014H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, m421d2 = {"Lkotlin/io/encoding/DecodeInputStream;", "Ljava/io/InputStream;", "input", "base64", "Lkotlin/io/encoding/Base64;", "(Ljava/io/InputStream;Lkotlin/io/encoding/Base64;)V", "byteBuffer", "", "byteBufferEndIndex", "", "byteBufferLength", "getByteBufferLength", "()I", "byteBufferStartIndex", "isClosed", "", "isEOF", "singleByteBuffer", "symbolBuffer", "close", "", "copyByteBufferInto", "dst", "dstOffset", "length", "decodeSymbolBufferInto", "dstEndIndex", "symbolBufferLength", "handlePaddingSymbol", "read", "destination", "offset", "readNextSymbol", "resetByteBufferIfEmpty", "shiftByteBufferToStartIfNeeded", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class DecodeInputStream extends InputStream {
    private final Base64 base64;
    private final byte[] byteBuffer;
    private int byteBufferEndIndex;
    private int byteBufferStartIndex;
    private final InputStream input;
    private boolean isClosed;
    private boolean isEOF;
    private final byte[] singleByteBuffer;
    private final byte[] symbolBuffer;

    public DecodeInputStream(InputStream r3, Base64 r4) {
        Intrinsics.checkNotNullParameter(r3, "input");
        Intrinsics.checkNotNullParameter(r4, "base64");
        this.input = r3;
        this.base64 = r4;
        this.singleByteBuffer = new byte[1];
        this.symbolBuffer = new byte[1024];
        this.byteBuffer = new byte[1024];
    }

    private final void copyByteBufferInto(byte[] r4, int r5, int r6) {
        ArraysKt.copyInto(this.byteBuffer, r4, r5, this.byteBufferStartIndex, this.byteBufferStartIndex + r6);
        this.byteBufferStartIndex += r6;
        resetByteBufferIfEmpty();
    }

    private final int decodeSymbolBufferInto(byte[] r8, int r9, int r10, int r11) {
        this.byteBufferEndIndex += this.base64.decodeIntoByteArray(this.symbolBuffer, this.byteBuffer, this.byteBufferEndIndex, 0, r11);
        int r12 = Math.min(getByteBufferLength(), r10 - r9);
        copyByteBufferInto(r8, r9, r12);
        shiftByteBufferToStartIfNeeded();
        return r12;
    }

    private final int getByteBufferLength() {
        return this.byteBufferEndIndex - this.byteBufferStartIndex;
    }

    private final int handlePaddingSymbol(int r5) {
        this.symbolBuffer[r5] = Base64.padSymbol;
        if ((r5 & 3) != 2) goto L9;
        int r0 = readNextSymbol();
        if (r0 < 0) goto L10;
        this.symbolBuffer[r5 + 1] = (byte) r0;
    L10:
        return r5 + 2;
    L9:
        return r5 + 1;
    }

    private final int readNextSymbol() {
        if (this.base64.isMimeScheme$kotlin_stdlib() == true) goto L7;
        return this.input.read();
    L7:
        int r0 = this.input.read();
        if (r0 == (-1)) goto L11;
        if (Base64Kt.isInMimeAlphabet(r0) == false) goto L7;
    L11:
        return r0;
    }

    private final void resetByteBufferIfEmpty() {
        if (this.byteBufferStartIndex != this.byteBufferEndIndex) goto L6;
        this.byteBufferStartIndex = 0;
        this.byteBufferEndIndex = 0;
        return;
    }

    private final void shiftByteBufferToStartIfNeeded() {
        if (((this.symbolBuffer.length / 4) * 3) <= (this.byteBuffer.length - this.byteBufferEndIndex)) goto L6;
        ArraysKt.copyInto(this.byteBuffer, this.byteBuffer, 0, this.byteBufferStartIndex, this.byteBufferEndIndex);
        this.byteBufferEndIndex -= this.byteBufferStartIndex;
        this.byteBufferStartIndex = 0;
        return;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.isClosed == true) goto L6;
        this.isClosed = true;
        this.input.close();
        return;
    }

    @Override // java.io.InputStream
    public int read() {
        if (this.byteBufferStartIndex >= this.byteBufferEndIndex) goto L7;
        int r0 = this.byteBuffer[this.byteBufferStartIndex] & 255;
        this.byteBufferStartIndex++;
        resetByteBufferIfEmpty();
        return r0;
    L7:
        switch(read(this.singleByteBuffer, 0, 1)) {
            case -1: goto L11;
            case 0: goto L9;
            case 1: goto L13;
            default: goto L9;
        };
    L11:
        return -1;
    L9:
        throw new IllegalStateException("Unreachable".toString());
    L13:
        return this.singleByteBuffer[0] & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] r13, int r14, int r15) {
        Intrinsics.checkNotNullParameter(r13, "destination");
        if (r14 < 0) goto L51;
        if (r15 < 0) goto L51;
        if ((r14 + r15) > r13.length) goto L51;
        if (this.isClosed == true) goto L49;
        if (this.isEOF == false) goto L13;
        return -1;
    L13:
        if (r15 != 0) goto L16;
        return 0;
    L16:
        if (getByteBufferLength() < r15) goto L19;
        copyByteBufferInto(r13, r14, r15);
        return r15;
    L19:
        int r5 = ((((r15 - getByteBufferLength()) + 3) - 1) / 3) * 4;
        int r6 = r14;
    L21:
        if (this.isEOF == true) goto L42;
        if (r5 <= 0) goto L42;
        int r7 = 0;
        int r8 = Math.min(this.symbolBuffer.length, r5);
    L25:
        if (this.isEOF == true) goto L33;
        if (r7 >= r8) goto L33;
        int r9 = readNextSymbol();
        switch(r9) {
            case -1: goto L31;
            case 61: goto L30;
            default: goto L29;
        };
    L30:
        r7 = handlePaddingSymbol(r7);
        this.isEOF = true;
        goto L25
    L31:
        this.isEOF = true;
        goto L25
    L29:
        this.symbolBuffer[r7] = (byte) r9;
        r7 = r7 + 1;
    L33:
        if (this.isEOF == true) goto L37;
        if (r7 == r8) goto L37;
        boolean r10 = false;
    L38:
        if (r10 == false) goto L41;
        r5 = r5 - r7;
        r6 = r6 + decodeSymbolBufferInto(r13, r6, r15 + r14, r7);
        goto L21
    L41:
        throw new IllegalStateException("Check failed.".toString());
    L37:
        r10 = true;
    L42:
        if (r6 != r14) goto L47;
        if (this.isEOF == false) goto L47;
        return -1;
    L47:
        return r6 - r14;
    L49:
        throw new IOException("The input stream is closed.");
    L51:
        throw new IndexOutOfBoundsException("offset: " + r14 + ", length: " + r15 + ", buffer size: " + r13.length);
    }
}
