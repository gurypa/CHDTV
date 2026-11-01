package kotlin.p005io;

import androidx.exifinterface.media.ExifInterface;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: FileReadWrite.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000z\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010\u0005\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a!\u0010\n\u001a\u00020\u000b*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0087\b\u001a!\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0087\b\u001aB\u0010\u0010\u001a\u00020\u0001*\u00020\u000226\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001aJ\u0010\u0010\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\r26\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001a7\u0010\u0018\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u00010\u0019\u001a\r\u0010\u001b\u001a\u00020\u001c*\u00020\u0002H\u0087\b\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0017\u0010\u001f\u001a\u00020 *\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u001a\n\u0010!\u001a\u00020\u0004*\u00020\u0002\u001a\u001a\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00070#*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0014\u0010$\u001a\u00020\u0007*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010%\u001a\u00020&*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u001a?\u0010'\u001a\u0002H(\"\u0004\b\u0000\u0010(*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\u0018\u0010)\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070*\u0012\u0004\u0012\u0002H(0\u0019H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010+\u001a\u0012\u0010,\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010-\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010.\u001a\u00020/*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u00060"}, m421d2 = {"appendBytes", "", "Ljava/io/File;", "array", "", "appendText", "text", "", "charset", "Ljava/nio/charset/Charset;", "bufferedReader", "Ljava/io/BufferedReader;", "bufferSize", "", "bufferedWriter", "Ljava/io/BufferedWriter;", "forEachBlock", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "buffer", "bytesRead", "blockSize", "forEachLine", "Lkotlin/Function1;", "line", "inputStream", "Ljava/io/FileInputStream;", "outputStream", "Ljava/io/FileOutputStream;", "printWriter", "Ljava/io/PrintWriter;", "readBytes", "readLines", "", "readText", "reader", "Ljava/io/InputStreamReader;", "useLines", ExifInterface.GPS_DIRECTION_TRUE, "block", "Lkotlin/sequences/Sequence;", "(Ljava/io/File;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "writeBytes", "writeText", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/io/FilesKt")
class FilesKt__FileReadWriteKt extends FilesKt__FilePathComponentsKt {
    public FilesKt__FileReadWriteKt() {
    }

    public static final void appendBytes(File r3, byte[] r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "array");
        FileOutputStream r0 = new FileOutputStream(r3, true);
        r0.write(r4);     // Catch: Throwable -> L6
        Unit r1 = Unit.INSTANCE;     // Catch: Throwable -> L6
        CloseableKt.closeFinally(r0, null);
        return;
    L6:
        th = move-exception;
        throw th;     // Catch: Throwable -> L8
    L8:
        th = move-exception;
        CloseableKt.closeFinally(r0, th);
        throw th;
    }

    public static final void appendText(File r2, String r3, Charset r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "text");
        Intrinsics.checkNotNullParameter(r4, "charset");
        byte[] r0 = r3.getBytes(r4);
        Intrinsics.checkNotNullExpressionValue(r0, "getBytes(...)");
        FilesKt.appendBytes(r2, r0);
    }

    public static /* synthetic */ void appendText$default(File r0, String r1, Charset r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L5;
        r2 = Charsets.UTF_8;
    L5:
        FilesKt.appendText(r0, r1, r2);
    }

    private static final BufferedReader bufferedReader(File r2, Charset r3, int r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "charset");
        Reader r0 = new InputStreamReader(new FileInputStream(r2), r3);
        if ((r0 instanceof BufferedReader) == false) goto L6;
        return (BufferedReader) r0;
    L6:
        return new BufferedReader(r0, r4);
    }

    static /* synthetic */ BufferedReader bufferedReader$default(File r0, Charset r1, int r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L6;
        r1 = Charsets.UTF_8;
    L6:
        if ((r3 & 2) == 0) goto L8;
        r2 = 8192;
    L8:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "charset");
        Reader r5 = new InputStreamReader(new FileInputStream(r0), r1);
        if ((r5 instanceof BufferedReader) == false) goto L12;
        return (BufferedReader) r5;
    L12:
        return new BufferedReader(r5, r2);
    }

    private static final BufferedWriter bufferedWriter(File r2, Charset r3, int r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "charset");
        Writer r0 = new OutputStreamWriter(new FileOutputStream(r2), r3);
        if ((r0 instanceof BufferedWriter) == false) goto L6;
        return (BufferedWriter) r0;
    L6:
        return new BufferedWriter(r0, r4);
    }

    static /* synthetic */ BufferedWriter bufferedWriter$default(File r0, Charset r1, int r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L6;
        r1 = Charsets.UTF_8;
    L6:
        if ((r3 & 2) == 0) goto L8;
        r2 = 8192;
    L8:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "charset");
        Writer r5 = new OutputStreamWriter(new FileOutputStream(r0), r1);
        if ((r5 instanceof BufferedWriter) == false) goto L12;
        return (BufferedWriter) r5;
    L12:
        return new BufferedWriter(r5, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4, types: [byte[], java.lang.Object] */
    public static final void forEachBlock(File r6, int r7, Function2<? super byte[], ? super Integer, Unit> r8) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r8, "action");
        ?? r0 = new byte[RangesKt.coerceAtLeast(r7, 512)];
        FileInputStream r1 = new FileInputStream(r6);
        FileInputStream r2 = r1;     // Catch: Throwable -> L11
    L4:
        int r4 = r2.read(r0);     // Catch: Throwable -> L11
        if (r4 <= 0) goto L6;
        r8.invoke(r0, Integer.valueOf(r4));     // Catch: Throwable -> L11
        goto L4
    L6:
        Unit r3 = Unit.INSTANCE;     // Catch: Throwable -> L11
        CloseableKt.closeFinally(r1, null);
        return;
    L11:
        th = move-exception;
        throw th;     // Catch: Throwable -> L13
    L13:
        th = move-exception;
        CloseableKt.closeFinally(r1, th);
        throw th;
    }

    public static final void forEachBlock(File r1, Function2<? super byte[], ? super Integer, Unit> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "action");
        FilesKt.forEachBlock(r1, 4096, r2);
    }

    public static final void forEachLine(File r3, Charset r4, Function1<? super String, Unit> r5) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "charset");
        Intrinsics.checkNotNullParameter(r5, "action");
        TextStreamsKt.forEachLine(new BufferedReader(new InputStreamReader(new FileInputStream(r3), r4)), r5);
    }

    public static /* synthetic */ void forEachLine$default(File r0, Charset r1, Function1 r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L5;
        r1 = Charsets.UTF_8;
    L5:
        FilesKt.forEachLine(r0, r1, r2);
    }

    private static final FileInputStream inputStream(File r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new FileInputStream(r1);
    }

    private static final FileOutputStream outputStream(File r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new FileOutputStream(r1);
    }

    private static final PrintWriter printWriter(File r4, Charset r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "charset");
        Writer r1 = new OutputStreamWriter(new FileOutputStream(r4), r5);
        if ((r1 instanceof BufferedWriter) == false) goto L5;
        BufferedWriter r2 = (BufferedWriter) r1;
    L7:
        return new PrintWriter(r2);
    L5:
        r2 = new BufferedWriter(r1, 8192);
        goto L7
    }

    static /* synthetic */ PrintWriter printWriter$default(File r2, Charset r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L5;
        r3 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "charset");
        Writer r6 = new OutputStreamWriter(new FileOutputStream(r2), r3);
        if ((r6 instanceof BufferedWriter) == false) goto L8;
        BufferedWriter r7 = (BufferedWriter) r6;
    L10:
        return new PrintWriter(r7);
    L8:
        r7 = new BufferedWriter(r6, 8192);
        goto L10
    }

    public static final byte[] readBytes(File r15) {
        Intrinsics.checkNotNullParameter(r15, "<this>");
        FileInputStream r0 = new FileInputStream(r15);
        FileInputStream r1 = r0;     // Catch: Throwable -> L28
        int r3 = 0;
        long r4 = r15.length();     // Catch: Throwable -> L28
        if (r4 > 2147483647L) goto L27;
        int r5 = (int) r4;
        byte[] r6 = new byte[r5];     // Catch: Throwable -> L28
    L8:
        if (r5 <= 0) goto L13;
        int r7 = r1.read(r6, r3, r5);     // Catch: Throwable -> L28
        if (r7 < 0) goto L13;
        r5 = r5 - r7;
        r3 = r3 + r7;
    L13:
        if (r5 <= 0) goto L15;
        byte[] r8 = Arrays.copyOf(r6, r3);     // Catch: Throwable -> L28
        Intrinsics.checkNotNullExpressionValue(r8, "copyOf(...)");     // Catch: Throwable -> L28
        r6 = r8;
    L22:
        CloseableKt.closeFinally(r0, null);
        return r6;
    L15:
        int r9 = r1.read();     // Catch: Throwable -> L28
        if (r9 == (-1)) goto L22;
        ExposingBufferByteArrayOutputStream r10 = new ExposingBufferByteArrayOutputStream(8193);     // Catch: Throwable -> L28
        r10.write(r9);     // Catch: Throwable -> L28
        ByteStreamsKt.copyTo$default(r1, r10, 0, 2, null);     // Catch: Throwable -> L28
        int r11 = r6.length + r10.size();     // Catch: Throwable -> L28
        if (r11 < 0) goto L25;
        byte[] r12 = r10.getBuffer();     // Catch: Throwable -> L28
        byte[] r13 = Arrays.copyOf(r6, r11);     // Catch: Throwable -> L28
        Intrinsics.checkNotNullExpressionValue(r13, "copyOf(...)");     // Catch: Throwable -> L28
        r6 = ArraysKt.copyInto(r12, r13, r6.length, 0, r10.size());     // Catch: Throwable -> L28
        goto L22
    L25:
        throw new OutOfMemoryError("File " + r15 + " is too big to fit in memory.");     // Catch: Throwable -> L28
    L27:
        throw new OutOfMemoryError("File " + r15 + " is too big (" + r4 + " bytes) to fit in memory.");     // Catch: Throwable -> L28
    L28:
        th = move-exception;
        throw th;     // Catch: Throwable -> L30
    L30:
        th = move-exception;
        CloseableKt.closeFinally(r0, th);
        throw th;
    }

    public static final List<String> readLines(File r2, Charset r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "charset");
        final ArrayList r0 = new ArrayList();
        FilesKt.forEachLine(r2, r3, new C10941(r0));
        return r0;
    }

    public static /* synthetic */ List readLines$default(File r0, Charset r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = Charsets.UTF_8;
    L6:
        return FilesKt.readLines(r0, r1);
    }

    public static final String readText(File r4, Charset r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "charset");
        InputStreamReader r0 = new InputStreamReader(new FileInputStream(r4), r5);
        String r3 = TextStreamsKt.readText(r0);     // Catch: Throwable -> L6
        CloseableKt.closeFinally(r0, null);
        return r3;
    L6:
        th = move-exception;
        throw th;     // Catch: Throwable -> L8
    L8:
        th = move-exception;
        CloseableKt.closeFinally(r0, th);
        throw th;
    }

    public static /* synthetic */ String readText$default(File r0, Charset r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = Charsets.UTF_8;
    L6:
        return FilesKt.readText(r0, r1);
    }

    private static final InputStreamReader reader(File r2, Charset r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "charset");
        return new InputStreamReader(new FileInputStream(r2), r3);
    }

    static /* synthetic */ InputStreamReader reader$default(File r0, Charset r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "charset");
        return new InputStreamReader(new FileInputStream(r0), r1);
    }

    public static final <T> T useLines(File r7, Charset r8, Function1<? super Sequence<String>, ? extends T> r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "charset");
        Intrinsics.checkNotNullParameter(r9, "block");
        Reader r1 = new InputStreamReader(new FileInputStream(r7), r8);
        if ((r1 instanceof BufferedReader) == false) goto L5;
        BufferedReader r2 = (BufferedReader) r1;
    L6:
        BufferedReader r3 = r2;
        T r6 = r9.invoke(TextStreamsKt.lineSequence(r3));     // Catch: Throwable -> L13
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L11;
        CloseableKt.closeFinally(r3, null);
    L12:
        return r6;
    L11:
        r3.close();
    L13:
        th = move-exception;
        throw th;     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L24;
        CloseableKt.closeFinally(r3, th);
    L23:
        throw th;
    L24:
        r3.close();     // Catch: Throwable -> L20
        goto L23
    L5:
        r2 = new BufferedReader(r1, 8192);
        goto L6
    }

    public static /* synthetic */ Object useLines$default(File r5, Charset r6, Function1 r7, int r8, Object r9) {
        if ((r8 & 1) == 0) goto L5;
        r6 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "charset");
        Intrinsics.checkNotNullParameter(r7, "block");
        Reader r0 = new InputStreamReader(new FileInputStream(r5), r6);
        if ((r0 instanceof BufferedReader) == false) goto L8;
        BufferedReader r1 = (BufferedReader) r0;
    L9:
        BufferedReader r2 = r1;
        Object r4 = r7.invoke(TextStreamsKt.lineSequence(r2));     // Catch: Throwable -> L16
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L14;
        CloseableKt.closeFinally(r2, null);
    L15:
        return r4;
    L14:
        r2.close();
    L16:
        th = move-exception;
        throw th;     // Catch: Throwable -> L18
    L18:
        th = move-exception;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L31;
        CloseableKt.closeFinally(r2, th);
    L26:
        throw th;
    L31:
        r2.close();     // Catch: Throwable -> L23
        goto L26
    L8:
        r1 = new BufferedReader(r0, 8192);
        goto L9
    }

    public static final void writeBytes(File r3, byte[] r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "array");
        FileOutputStream r0 = new FileOutputStream(r3);
        r0.write(r4);     // Catch: Throwable -> L6
        Unit r1 = Unit.INSTANCE;     // Catch: Throwable -> L6
        CloseableKt.closeFinally(r0, null);
        return;
    L6:
        th = move-exception;
        throw th;     // Catch: Throwable -> L8
    L8:
        th = move-exception;
        CloseableKt.closeFinally(r0, th);
        throw th;
    }

    public static final void writeText(File r2, String r3, Charset r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "text");
        Intrinsics.checkNotNullParameter(r4, "charset");
        byte[] r0 = r3.getBytes(r4);
        Intrinsics.checkNotNullExpressionValue(r0, "getBytes(...)");
        FilesKt.writeBytes(r2, r0);
    }

    public static /* synthetic */ void writeText$default(File r0, String r1, Charset r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L5;
        r2 = Charsets.UTF_8;
    L5:
        FilesKt.writeText(r0, r1, r2);
    }

    private static final OutputStreamWriter writer(File r2, Charset r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "charset");
        return new OutputStreamWriter(new FileOutputStream(r2), r3);
    }

    static /* synthetic */ OutputStreamWriter writer$default(File r0, Charset r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "charset");
        return new OutputStreamWriter(new FileOutputStream(r0), r1);
    }
}
