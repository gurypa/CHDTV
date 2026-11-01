package kotlin.p005io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.ByteIterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: IOStreams.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000Z\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0007\u001a\u00020\b*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\u000b\u001a\u00020\f*\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\r\u001a\u00020\u000e*\u00020\u000f2\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u001c\u0010\u0010\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\r\u0010\u0013\u001a\u00020\u000e*\u00020\u0014H\u0087\b\u001a\u001d\u0010\u0013\u001a\u00020\u000e*\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0004H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u0018*\u00020\u0001H\u0086\u0002\u001a\f\u0010\u0019\u001a\u00020\u0014*\u00020\u0002H\u0007\u001a\u0016\u0010\u0019\u001a\u00020\u0014*\u00020\u00022\b\b\u0002\u0010\u001a\u001a\u00020\u0004H\u0007\u001a\u0017\u0010\u001b\u001a\u00020\u001c*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\u001d\u001a\u00020\u001e*\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b¨\u0006\u001f"}, m421d2 = {"buffered", "Ljava/io/BufferedInputStream;", "Ljava/io/InputStream;", "bufferSize", "", "Ljava/io/BufferedOutputStream;", "Ljava/io/OutputStream;", "bufferedReader", "Ljava/io/BufferedReader;", "charset", "Ljava/nio/charset/Charset;", "bufferedWriter", "Ljava/io/BufferedWriter;", "byteInputStream", "Ljava/io/ByteArrayInputStream;", "", "copyTo", "", "out", "inputStream", "", "offset", "length", "iterator", "Lkotlin/collections/ByteIterator;", "readBytes", "estimatedSize", "reader", "Ljava/io/InputStreamReader;", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class ByteStreamsKt {
    private static final BufferedInputStream buffered(InputStream r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if ((r1 instanceof BufferedInputStream) == false) goto L6;
        return (BufferedInputStream) r1;
    L6:
        return new BufferedInputStream(r1, r2);
    }

    private static final BufferedOutputStream buffered(OutputStream r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if ((r1 instanceof BufferedOutputStream) == false) goto L6;
        return (BufferedOutputStream) r1;
    L6:
        return new BufferedOutputStream(r1, r2);
    }

    static /* synthetic */ BufferedInputStream buffered$default(InputStream r0, int r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = 8192;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        if ((r0 instanceof BufferedInputStream) == false) goto L9;
        return (BufferedInputStream) r0;
    L9:
        return new BufferedInputStream(r0, r1);
    }

    static /* synthetic */ BufferedOutputStream buffered$default(OutputStream r0, int r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = 8192;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        if ((r0 instanceof BufferedOutputStream) == false) goto L9;
        return (BufferedOutputStream) r0;
    L9:
        return new BufferedOutputStream(r0, r1);
    }

    private static final BufferedReader bufferedReader(InputStream r3, Charset r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "charset");
        Reader r0 = new InputStreamReader(r3, r4);
        if ((r0 instanceof BufferedReader) == false) goto L6;
        return (BufferedReader) r0;
    L6:
        return new BufferedReader(r0, 8192);
    }

    static /* synthetic */ BufferedReader bufferedReader$default(InputStream r1, Charset r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L5;
        r2 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "charset");
        Reader r5 = new InputStreamReader(r1, r2);
        if ((r5 instanceof BufferedReader) == false) goto L9;
        return (BufferedReader) r5;
    L9:
        return new BufferedReader(r5, 8192);
    }

    private static final BufferedWriter bufferedWriter(OutputStream r3, Charset r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "charset");
        Writer r0 = new OutputStreamWriter(r3, r4);
        if ((r0 instanceof BufferedWriter) == false) goto L6;
        return (BufferedWriter) r0;
    L6:
        return new BufferedWriter(r0, 8192);
    }

    static /* synthetic */ BufferedWriter bufferedWriter$default(OutputStream r1, Charset r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L5;
        r2 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "charset");
        Writer r5 = new OutputStreamWriter(r1, r2);
        if ((r5 instanceof BufferedWriter) == false) goto L9;
        return (BufferedWriter) r5;
    L9:
        return new BufferedWriter(r5, 8192);
    }

    private static final ByteArrayInputStream byteInputStream(String r3, Charset r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "charset");
        byte[] r1 = r3.getBytes(r4);
        Intrinsics.checkNotNullExpressionValue(r1, "getBytes(...)");
        return new ByteArrayInputStream(r1);
    }

    static /* synthetic */ ByteArrayInputStream byteInputStream$default(String r1, Charset r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L5;
        r2 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "charset");
        byte[] r5 = r1.getBytes(r2);
        Intrinsics.checkNotNullExpressionValue(r5, "getBytes(...)");
        return new ByteArrayInputStream(r5);
    }

    public static final long copyTo(InputStream r6, OutputStream r7, int r8) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "out");
        long r0 = 0;
        byte[] r2 = new byte[r8];
        int r3 = r6.read(r2);
    L3:
        if (r3 < 0) goto L5;
        r7.write(r2, 0, r3);
        r0 = r0 + ((long) r3);
        r3 = r6.read(r2);
        goto L3
    L5:
        return r0;
    }

    public static /* synthetic */ long copyTo$default(InputStream r0, OutputStream r1, int r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = 8192;
    L6:
        return copyTo(r0, r1, r2);
    }

    private static final ByteArrayInputStream inputStream(byte[] r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new ByteArrayInputStream(r1);
    }

    private static final ByteArrayInputStream inputStream(byte[] r1, int r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new ByteArrayInputStream(r1, r2, r3);
    }

    public static final ByteIterator iterator(final BufferedInputStream r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new C10931(r1);
    }

    public static final byte[] readBytes(InputStream r5) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        ByteArrayOutputStream r0 = new ByteArrayOutputStream(Math.max(8192, r5.available()));
        copyTo$default(r5, r0, 0, 2, null);
        byte[] r1 = r0.toByteArray();
        Intrinsics.checkNotNullExpressionValue(r1, "toByteArray(...)");
        return r1;
    }

    @Deprecated(message = "Use readBytes() overload without estimatedSize parameter", replaceWith = @ReplaceWith(expression = "readBytes()", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", warningSince = "1.3")
    public static final byte[] readBytes(InputStream r5, int r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        ByteArrayOutputStream r0 = new ByteArrayOutputStream(Math.max(r6, r5.available()));
        copyTo$default(r5, r0, 0, 2, null);
        byte[] r1 = r0.toByteArray();
        Intrinsics.checkNotNullExpressionValue(r1, "toByteArray(...)");
        return r1;
    }

    public static /* synthetic */ byte[] readBytes$default(InputStream r0, int r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = 8192;
    L6:
        return readBytes(r0, r1);
    }

    private static final InputStreamReader reader(InputStream r1, Charset r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "charset");
        return new InputStreamReader(r1, r2);
    }

    static /* synthetic */ InputStreamReader reader$default(InputStream r0, Charset r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "charset");
        return new InputStreamReader(r0, r1);
    }

    private static final OutputStreamWriter writer(OutputStream r1, Charset r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "charset");
        return new OutputStreamWriter(r1, r2);
    }

    static /* synthetic */ OutputStreamWriter writer$default(OutputStream r0, Charset r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "charset");
        return new OutputStreamWriter(r0, r1);
    }
}
