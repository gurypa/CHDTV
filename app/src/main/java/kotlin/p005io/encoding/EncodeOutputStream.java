package kotlin.p005io.encoding;

import java.io.IOException;
import java.io.OutputStream;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Base64IOStream.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\r\b\u0003\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u000fH\u0016J \u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\tH\u0002J\b\u0010\u0015\u001a\u00020\u000fH\u0002J \u0010\u0016\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\tH\u0002J\b\u0010\u0017\u001a\u00020\u000fH\u0016J \u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\tH\u0016J\u0010\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\tH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"}, m421d2 = {"Lkotlin/io/encoding/EncodeOutputStream;", "Ljava/io/OutputStream;", "output", "base64", "Lkotlin/io/encoding/Base64;", "(Ljava/io/OutputStream;Lkotlin/io/encoding/Base64;)V", "byteBuffer", "", "byteBufferLength", "", "isClosed", "", "lineLength", "symbolBuffer", "checkOpen", "", "close", "copyIntoByteBuffer", "source", "startIndex", "endIndex", "encodeByteBufferIntoOutput", "encodeIntoOutput", "flush", "write", "offset", "length", "b", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class EncodeOutputStream extends OutputStream {
    private final Base64 base64;
    private final byte[] byteBuffer;
    private int byteBufferLength;
    private boolean isClosed;
    private int lineLength;
    private final OutputStream output;
    private final byte[] symbolBuffer;

    public EncodeOutputStream(OutputStream r2, Base64 r3) {
        Intrinsics.checkNotNullParameter(r2, "output");
        Intrinsics.checkNotNullParameter(r3, "base64");
        this.output = r2;
        this.base64 = r3;
        if (this.base64.isMimeScheme$kotlin_stdlib() == false) goto L5;
        int r0 = 76;
    L6:
        this.lineLength = r0;
        this.symbolBuffer = new byte[1024];
        this.byteBuffer = new byte[3];
        return;
    L5:
        r0 = -1;
        goto L6
    }

    private final void checkOpen() {
        if (this.isClosed == true) goto L6;
        return;
    L6:
        throw new IOException("The output stream is closed.");
    }

    private final int copyIntoByteBuffer(byte[] r6, int r7, int r8) {
        int r0 = Math.min(3 - this.byteBufferLength, r8 - r7);
        ArraysKt.copyInto(r6, this.byteBuffer, this.byteBufferLength, r7, r7 + r0);
        this.byteBufferLength += r0;
        if (this.byteBufferLength != 3) goto L5;
        encodeByteBufferIntoOutput();
    L5:
        return r0;
    }

    private final void encodeByteBufferIntoOutput() {
        if (encodeIntoOutput(this.byteBuffer, 0, this.byteBufferLength) != 4) goto L5;
        boolean r1 = true;
    L6:
        if (r1 == false) goto L10;
        this.byteBufferLength = 0;
        return;
    L10:
        throw new IllegalStateException("Check failed.".toString());
    L5:
        r1 = false;
        goto L6
    }

    private final int encodeIntoOutput(byte[] r7, int r8, int r9) {
        int r10 = this.base64.encodeIntoByteArray(r7, this.symbolBuffer, 0, r8, r9);
        if (this.lineLength != 0) goto L12;
        this.output.write(Base64.Default.getMimeLineSeparatorSymbols$kotlin_stdlib());
        this.lineLength = 76;
        if (r10 > 76) goto L7;
        boolean r11 = true;
    L8:
        if (r11 == true) goto L12;
        throw new IllegalStateException("Check failed.".toString());
    L7:
        r11 = false;
    L12:
        this.output.write(this.symbolBuffer, 0, r10);
        this.lineLength -= r10;
        return r10;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.isClosed == true) goto L9;
        this.isClosed = true;
        if (this.byteBufferLength == 0) goto L7;
        encodeByteBufferIntoOutput();
    L7:
        this.output.close();
        return;
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() {
        checkOpen();
        this.output.flush();
    }

    @Override // java.io.OutputStream
    public void write(int r4) {
        checkOpen();
        byte[] r0 = this.byteBuffer;
        int r1 = this.byteBufferLength;
        this.byteBufferLength = r1 + 1;
        r0[r1] = (byte) r4;
        if (this.byteBufferLength != 3) goto L6;
        encodeByteBufferIntoOutput();
        return;
    }

    @Override // java.io.OutputStream
    public void write(byte[] r12, int r13, int r14) {
        Intrinsics.checkNotNullParameter(r12, "source");
        checkOpen();
        if (r13 < 0) goto L39;
        if (r14 < 0) goto L39;
        if ((r13 + r14) > r12.length) goto L39;
        if (r14 != 0) goto L10;
        return;
    L10:
        if (this.byteBufferLength >= 3) goto L12;
        boolean r0 = true;
    L14:
        if (r0 == false) goto L37;
        int r1 = r13;
        int r5 = r1 + r14;
        if (this.byteBufferLength == 0) goto L21;
        r1 = r1 + copyIntoByteBuffer(r12, r1, r5);
        if (this.byteBufferLength == 0) goto L21;
        return;
    L21:
        if ((r1 + 3) > r5) goto L34;
        if (this.base64.isMimeScheme$kotlin_stdlib() == false) goto L25;
        int r6 = this.lineLength;
    L26:
        int r7 = Math.min(r6 / 4, (r5 - r1) / 3);
        int r8 = r7 * 3;
        if (encodeIntoOutput(r12, r1, r1 + r8) != (r7 * 4)) goto L29;
        boolean r10 = true;
    L30:
        if (r10 == false) goto L33;
        r1 = r1 + r8;
        goto L21
    L33:
        throw new IllegalStateException("Check failed.".toString());
    L29:
        r10 = false;
        goto L30
    L25:
        r6 = this.symbolBuffer.length;
        goto L26
    L34:
        ArraysKt.copyInto(r12, this.byteBuffer, 0, r1, r5);
        this.byteBufferLength = r5 - r1;
        return;
    L37:
        throw new IllegalStateException("Check failed.".toString());
    L12:
        r0 = false;
    L39:
        throw new IndexOutOfBoundsException("offset: " + r13 + ", length: " + r14 + ", source size: " + r12.length);
    }
}
