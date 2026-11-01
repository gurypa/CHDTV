package kotlin.p005io;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Console.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0014\u001a\u00020\u0004H\u0002J\u0010\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0010H\u0002J\u0018\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u0004H\u0002J\u0018\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fJ\b\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020!H\u0002J\u0010\u0010#\u001a\u00020!2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0011\u001a\u00060\u0012j\u0002`\u0013X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, m421d2 = {"Lkotlin/io/LineReader;", "", "()V", "BUFFER_SIZE", "", "byteBuf", "Ljava/nio/ByteBuffer;", "bytes", "", "charBuf", "Ljava/nio/CharBuffer;", "chars", "", "decoder", "Ljava/nio/charset/CharsetDecoder;", "directEOL", "", "sb", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "compactBytes", "decode", "endOfInput", "decodeEndOfInput", "nBytes", "nChars", "readLine", "", "inputStream", "Ljava/io/InputStream;", "charset", "Ljava/nio/charset/Charset;", "resetAll", "", "trimStringBuilder", "updateCharset", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class LineReader {
    private static final int BUFFER_SIZE = 32;
    public static final LineReader INSTANCE = null;
    private static final ByteBuffer byteBuf = null;
    private static final byte[] bytes = null;
    private static final CharBuffer charBuf = null;
    private static final char[] chars = null;
    private static CharsetDecoder decoder;
    private static boolean directEOL;

    /* JADX INFO: renamed from: sb */
    private static final StringBuilder f193sb = null;

    static {
        INSTANCE = new LineReader();
        bytes = new byte[32];
        chars = new char[32];
        ByteBuffer r0 = ByteBuffer.wrap(bytes);
        Intrinsics.checkNotNullExpressionValue(r0, "wrap(...)");
        byteBuf = r0;
        CharBuffer r1 = CharBuffer.wrap(chars);
        Intrinsics.checkNotNullExpressionValue(r1, "wrap(...)");
        charBuf = r1;
        f193sb = new StringBuilder();
    }

    private LineReader() {
    }

    private final int compactBytes() {
        ByteBuffer r0 = byteBuf;
        r0.compact();
        int r2 = r0.position();
        r0.position(0);
        return r2;
    }

    private final int decode(boolean r7) {
    L2:
        CharsetDecoder r0 = decoder;
        if (r0 != null) goto L5;
        Intrinsics.throwUninitializedPropertyAccessException("decoder");
        r0 = null;
    L5:
        CoderResult r1 = r0.decode(byteBuf, charBuf, r7);
        Intrinsics.checkNotNullExpressionValue(r1, "decode(...)");
        if (r1.isError() == false) goto L8;
        resetAll();
        r1.throwException();
    L8:
        int r2 = charBuf.position();
        if (r1.isOverflow() == false) goto L10;
        f193sb.append(chars, 0, r2 - 1);
        charBuf.position(0);
        charBuf.limit(32);
        charBuf.put(chars[r2 - 1]);
        goto L2
    L10:
        return r2;
    }

    private final int decodeEndOfInput(int r6, int r7) {
        byteBuf.limit(r6);
        charBuf.position(r7);
        int r0 = decode(true);
        CharsetDecoder r3 = decoder;
        if (r3 != null) goto L5;
        Intrinsics.throwUninitializedPropertyAccessException("decoder");
        r3 = null;
    L5:
        r3.reset();
        byteBuf.position(0);
        return r0;
    }

    private final void resetAll() {
        CharsetDecoder r0 = decoder;
        if (r0 != null) goto L5;
        Intrinsics.throwUninitializedPropertyAccessException("decoder");
        r0 = null;
    L5:
        r0.reset();
        byteBuf.position(0);
        f193sb.setLength(0);
    }

    private final void trimStringBuilder() {
        f193sb.setLength(32);
        f193sb.trimToSize();
    }

    private final void updateCharset(Charset r6) {
        CharsetDecoder r0 = r6.newDecoder();
        Intrinsics.checkNotNullExpressionValue(r0, "newDecoder(...)");
        decoder = r0;
        byteBuf.clear();
        charBuf.clear();
        byteBuf.put((byte) 10);
        byteBuf.flip();
        CharsetDecoder r1 = decoder;
        if (r1 != null) goto L5;
        Intrinsics.throwUninitializedPropertyAccessException("decoder");
        r1 = null;
    L5:
        boolean r4 = false;
        r1.decode(byteBuf, charBuf, false);
        if (charBuf.position() == 1) goto L8;
    L10:
        directEOL = r4;
        resetAll();
        return;
    L8:
        if (charBuf.get(0) != '\n') goto L10;
        r4 = true;
        goto L10
    }

    public final synchronized String readLine(InputStream r12, Charset r13) {
        monitor-enter(this);
        Intrinsics.checkNotNullParameter(r12, "inputStream");     // Catch: Throwable -> L59
        Intrinsics.checkNotNullParameter(r13, "charset");     // Catch: Throwable -> L59
        if (decoder == null) goto L10;
        CharsetDecoder r0 = decoder;     // Catch: Throwable -> L59
        if (r0 != null) goto L9;
        Intrinsics.throwUninitializedPropertyAccessException("decoder");     // Catch: Throwable -> L59
        r0 = null;
    L9:
        if (Intrinsics.areEqual(r0.charset(), r13) == false) goto L10;
    L11:
        int r1 = 0;
        int r2 = 0;
    L12:
        int r3 = r12.read();     // Catch: Throwable -> L59
        boolean r7 = true;
        if (r3 == (-1)) goto L15;
        int r9 = r1 + 1;     // Catch: Throwable -> L59
        bytes[r1] = (byte) r3;     // Catch: Throwable -> L59
        if (r3 == 10) goto L31;
        if (r9 == 32) goto L31;
        if (directEOL == false) goto L31;
        r1 = r9;
    L31:
        byteBuf.limit(r9);     // Catch: Throwable -> L59
        charBuf.position(r2);     // Catch: Throwable -> L59
        r2 = decode(false);     // Catch: Throwable -> L59
        if (r2 > 0) goto L34;
    L58:
        r1 = compactBytes();     // Catch: Throwable -> L59
        goto L12
    L34:
        if (chars[r2 - 1] != '\n') goto L58;
        byteBuf.position(0);     // Catch: Throwable -> L59
        int r4 = r2;
    L36:
        if (r4 <= 0) goto L45;
        if (chars[r4 - 1] != '\n') goto L45;
        r4 = r4 - 1;     // Catch: Throwable -> L59
        if (r4 <= 0) goto L45;
        if (chars[r4 - 1] != '\r') goto L45;
        r4 = r4 - 1;     // Catch: Throwable -> L59
    L45:
        if (f193sb.length() == 0) goto L48;
        r7 = false;
    L48:
        if (r7 == false) goto L52;
        String r5 = new String(chars, 0, r4);     // Catch: Throwable -> L59
        monitor-exit(this);
        return r5;
    L52:
        f193sb.append(chars, 0, r4);     // Catch: Throwable -> L59
        String r6 = f193sb.toString();     // Catch: Throwable -> L59
        Intrinsics.checkNotNullExpressionValue(r6, "toString(...)");     // Catch: Throwable -> L59
        if (f193sb.length() <= 32) goto L55;
        trimStringBuilder();     // Catch: Throwable -> L59
    L55:
        f193sb.setLength(0);     // Catch: Throwable -> L59
        monitor-exit(this);
        return r6;
    L15:
        if (f193sb.length() != 0) goto L17;
        boolean r8 = true;
    L18:
        if (r8 == false) goto L23;
        if (r1 != 0) goto L23;
        if (r2 != 0) goto L23;
        monitor-exit(this);
        return null;
    L23:
        r4 = decodeEndOfInput(r1, r2);     // Catch: Throwable -> L59
        goto L36
    L17:
        r8 = false;
    L10:
        updateCharset(r13);     // Catch: Throwable -> L59
    L59:
        th = move-exception;
        throw th;
    }
}
