package kotlin.p005io.path;

import androidx.exifinterface.media.ExifInterface;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.p005io.CloseableKt;
import kotlin.p005io.TextStreamsKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: PathReadWrite.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0082\u0001\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a%\u0010\u0005\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a%\u0010\u0005\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u000b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u001e\u0010\f\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a:\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u0010\u0015\u001a:\u0010\u0016\u001a\u00020\u0017*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u0010\u0018\u001a=\u0010\u0019\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2!\u0010\u001a\u001a\u001d\u0012\u0013\u0012\u00110\u001c¢\u0006\f\b\u001d\u0012\b\b\u001e\u0012\u0004\b\b(\u001f\u0012\u0004\u0012\u00020\u00010\u001bH\u0087\bø\u0001\u0000\u001a&\u0010 \u001a\u00020!*\u00020\u00022\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u0010\"\u001a&\u0010#\u001a\u00020$*\u00020\u00022\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u0010%\u001a\r\u0010&\u001a\u00020\u0004*\u00020\u0002H\u0087\b\u001a\u001d\u0010'\u001a\b\u0012\u0004\u0012\u00020\u001c0(*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0016\u0010)\u001a\u00020\u001c*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a0\u0010*\u001a\u00020+*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u0010,\u001a?\u0010-\u001a\u0002H.\"\u0004\b\u0000\u0010.*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\u0018\u0010/\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0\u000b\u0012\u0004\u0012\u0002H.0\u001bH\u0087\bø\u0001\u0000¢\u0006\u0002\u00100\u001a.\u00101\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u00102\u001a>\u00103\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u00104\u001a>\u00103\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u000b2\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u00105\u001a7\u00106\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0007¢\u0006\u0002\u00107\u001a0\u00108\u001a\u000209*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u0010:\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006;"}, m421d2 = {"appendBytes", "", "Ljava/nio/file/Path;", "array", "", "appendLines", "lines", "", "", "charset", "Ljava/nio/charset/Charset;", "Lkotlin/sequences/Sequence;", "appendText", "text", "bufferedReader", "Ljava/io/BufferedReader;", "bufferSize", "", "options", "", "Ljava/nio/file/OpenOption;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;I[Ljava/nio/file/OpenOption;)Ljava/io/BufferedReader;", "bufferedWriter", "Ljava/io/BufferedWriter;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;I[Ljava/nio/file/OpenOption;)Ljava/io/BufferedWriter;", "forEachLine", "action", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "line", "inputStream", "Ljava/io/InputStream;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Ljava/io/InputStream;", "outputStream", "Ljava/io/OutputStream;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Ljava/io/OutputStream;", "readBytes", "readLines", "", "readText", "reader", "Ljava/io/InputStreamReader;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/io/InputStreamReader;", "useLines", ExifInterface.GPS_DIRECTION_TRUE, "block", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "writeBytes", "(Ljava/nio/file/Path;[B[Ljava/nio/file/OpenOption;)V", "writeLines", "(Ljava/nio/file/Path;Ljava/lang/Iterable;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/nio/file/Path;", "(Ljava/nio/file/Path;Lkotlin/sequences/Sequence;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/nio/file/Path;", "writeText", "(Ljava/nio/file/Path;Ljava/lang/CharSequence;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)V", "writer", "Ljava/io/OutputStreamWriter;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/io/OutputStreamWriter;", "kotlin-stdlib-jdk7"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/io/path/PathsKt")
class PathsKt__PathReadWriteKt {
    public PathsKt__PathReadWriteKt() {
    }

    private static final void appendBytes(Path r3, byte[] r4) throws IOException {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "array");
        Files.write(r3, r4, new OpenOption[]{StandardOpenOption.APPEND});
    }

    private static final Path appendLines(Path r3, Iterable<? extends CharSequence> r4, Charset r5) throws IOException {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "lines");
        Intrinsics.checkNotNullParameter(r5, "charset");
        Path r0 = Files.write(r3, r4, r5, new OpenOption[]{StandardOpenOption.APPEND});
        Intrinsics.checkNotNullExpressionValue(r0, "write(...)");
        return r0;
    }

    private static final Path appendLines(Path r4, Sequence<? extends CharSequence> r5, Charset r6) throws IOException {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "lines");
        Intrinsics.checkNotNullParameter(r6, "charset");
        Path r0 = Files.write(r4, SequencesKt.asIterable(r5), r6, new OpenOption[]{StandardOpenOption.APPEND});
        Intrinsics.checkNotNullExpressionValue(r0, "write(...)");
        return r0;
    }

    static /* synthetic */ Path appendLines$default(Path r1, Iterable r2, Charset r3, int r4, Object r5) throws IOException {
        if ((r4 & 2) == 0) goto L5;
        r3 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "lines");
        Intrinsics.checkNotNullParameter(r3, "charset");
        Path r6 = Files.write(r1, r2, r3, new OpenOption[]{StandardOpenOption.APPEND});
        Intrinsics.checkNotNullExpressionValue(r6, "write(...)");
        return r6;
    }

    static /* synthetic */ Path appendLines$default(Path r2, Sequence r3, Charset r4, int r5, Object r6) throws IOException {
        if ((r5 & 2) == 0) goto L5;
        r4 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "lines");
        Intrinsics.checkNotNullParameter(r4, "charset");
        Path r7 = Files.write(r2, SequencesKt.asIterable(r3), r4, new OpenOption[]{StandardOpenOption.APPEND});
        Intrinsics.checkNotNullExpressionValue(r7, "write(...)");
        return r7;
    }

    public static final void appendText(Path r3, CharSequence r4, Charset r5) throws IOException {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "text");
        Intrinsics.checkNotNullParameter(r5, "charset");
        OutputStream r0 = Files.newOutputStream(r3, new OpenOption[]{StandardOpenOption.APPEND});
        Intrinsics.checkNotNullExpressionValue(r0, "newOutputStream(...)");
        OutputStreamWriter r1 = new OutputStreamWriter(r0, r5);
        r1.append(r4);     // Catch: Throwable -> L6
        CloseableKt.closeFinally(r1, null);
        return;
    L6:
        th = move-exception;
        throw th;     // Catch: Throwable -> L8
    L8:
        th = move-exception;
        CloseableKt.closeFinally(r1, th);
        throw th;
    }

    public static /* synthetic */ void appendText$default(Path r0, CharSequence r1, Charset r2, int r3, Object r4) throws IOException {
        if ((r3 & 2) == 0) goto L5;
        r2 = Charsets.UTF_8;
    L5:
        PathsKt.appendText(r0, r1, r2);
    }

    private static final BufferedReader bufferedReader(Path r3, Charset r4, int r5, OpenOption... r6) throws IOException {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "charset");
        Intrinsics.checkNotNullParameter(r6, "options");
        return new BufferedReader(new InputStreamReader(Files.newInputStream(r3, (OpenOption[]) Arrays.copyOf(r6, r6.length)), r4), r5);
    }

    static /* synthetic */ BufferedReader bufferedReader$default(Path r1, Charset r2, int r3, OpenOption[] r4, int r5, Object r6) throws IOException {
        if ((r5 & 1) == 0) goto L6;
        r2 = Charsets.UTF_8;
    L6:
        if ((r5 & 2) == 0) goto L8;
        r3 = 8192;
    L8:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "charset");
        Intrinsics.checkNotNullParameter(r4, "options");
        return new BufferedReader(new InputStreamReader(Files.newInputStream(r1, (OpenOption[]) Arrays.copyOf(r4, r4.length)), r2), r3);
    }

    private static final BufferedWriter bufferedWriter(Path r3, Charset r4, int r5, OpenOption... r6) throws IOException {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "charset");
        Intrinsics.checkNotNullParameter(r6, "options");
        return new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(r3, (OpenOption[]) Arrays.copyOf(r6, r6.length)), r4), r5);
    }

    static /* synthetic */ BufferedWriter bufferedWriter$default(Path r1, Charset r2, int r3, OpenOption[] r4, int r5, Object r6) throws IOException {
        if ((r5 & 1) == 0) goto L6;
        r2 = Charsets.UTF_8;
    L6:
        if ((r5 & 2) == 0) goto L8;
        r3 = 8192;
    L8:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "charset");
        Intrinsics.checkNotNullParameter(r4, "options");
        return new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(r1, (OpenOption[]) Arrays.copyOf(r4, r4.length)), r2), r3);
    }

    private static final void forEachLine(Path r13, Charset r14, Function1<? super String, Unit> r15) throws IOException {
        Intrinsics.checkNotNullParameter(r13, "<this>");
        Intrinsics.checkNotNullParameter(r14, "charset");
        Intrinsics.checkNotNullParameter(r15, "action");
        BufferedReader r0 = Files.newBufferedReader(r13, r14);
        Intrinsics.checkNotNullExpressionValue(r0, "newBufferedReader(...)");
        BufferedReader r2 = r0;
        Iterator<String> r11 = TextStreamsKt.lineSequence(r2).iterator();     // Catch: Throwable -> L13
    L5:
        if (r11.hasNext() == false) goto L7;
        r15.invoke(r11.next());     // Catch: Throwable -> L13
        goto L5
    L7:
        Unit r7 = Unit.INSTANCE;     // Catch: Throwable -> L13
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L11;
        CloseableKt.closeFinally(r2, null);
        return;
    L11:
        r2.close();
        return;
    L13:
        th = move-exception;
        throw th;     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L28;
        CloseableKt.closeFinally(r2, th);
    L23:
        throw th;
    L28:
        r2.close();     // Catch: Throwable -> L20
        goto L23
    }

    static /* synthetic */ void forEachLine$default(Path r11, Charset r12, Function1 r13, int r14, Object r15) throws IOException {
        if ((r14 & 1) == 0) goto L5;
        r12 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r11, "<this>");
        Intrinsics.checkNotNullParameter(r12, "charset");
        Intrinsics.checkNotNullParameter(r13, "action");
        BufferedReader r16 = Files.newBufferedReader(r11, r12);
        Intrinsics.checkNotNullExpressionValue(r16, "newBufferedReader(...)");
        BufferedReader r1 = r16;
        Iterator<String> r9 = TextStreamsKt.lineSequence(r1).iterator();     // Catch: Throwable -> L16
    L8:
        if (r9.hasNext() == false) goto L10;
        r13.invoke(r9.next());     // Catch: Throwable -> L16
        goto L8
    L10:
        Unit r5 = Unit.INSTANCE;     // Catch: Throwable -> L16
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L14;
        CloseableKt.closeFinally(r1, null);
        return;
    L14:
        r1.close();
        return;
    L16:
        th = move-exception;
        throw th;     // Catch: Throwable -> L18
    L18:
        th = move-exception;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L27;
        CloseableKt.closeFinally(r1, th);
    L26:
        throw th;
    L27:
        r1.close();     // Catch: Throwable -> L23
        goto L26
    }

    private static final InputStream inputStream(Path r2, OpenOption... r3) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "options");
        InputStream r0 = Files.newInputStream(r2, (OpenOption[]) Arrays.copyOf(r3, r3.length));
        Intrinsics.checkNotNullExpressionValue(r0, "newInputStream(...)");
        return r0;
    }

    private static final OutputStream outputStream(Path r2, OpenOption... r3) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "options");
        OutputStream r0 = Files.newOutputStream(r2, (OpenOption[]) Arrays.copyOf(r3, r3.length));
        Intrinsics.checkNotNullExpressionValue(r0, "newOutputStream(...)");
        return r0;
    }

    private static final byte[] readBytes(Path r2) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        byte[] r0 = Files.readAllBytes(r2);
        Intrinsics.checkNotNullExpressionValue(r0, "readAllBytes(...)");
        return r0;
    }

    private static final List<String> readLines(Path r2, Charset r3) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "charset");
        List<String> r0 = Files.readAllLines(r2, r3);
        Intrinsics.checkNotNullExpressionValue(r0, "readAllLines(...)");
        return r0;
    }

    static /* synthetic */ List readLines$default(Path r0, Charset r1, int r2, Object r3) throws IOException {
        if ((r2 & 1) == 0) goto L5;
        r1 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "charset");
        List<String> r4 = Files.readAllLines(r0, r1);
        Intrinsics.checkNotNullExpressionValue(r4, "readAllLines(...)");
        return r4;
    }

    public static final String readText(Path r4, Charset r5) throws IOException {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "charset");
        InputStreamReader r2 = new InputStreamReader(Files.newInputStream(r4, (OpenOption[]) Arrays.copyOf(new OpenOption[0], 0)), r5);
        String r3 = TextStreamsKt.readText(r2);     // Catch: Throwable -> L6
        CloseableKt.closeFinally(r2, null);
        return r3;
    L6:
        th = move-exception;
        throw th;     // Catch: Throwable -> L8
    L8:
        th = move-exception;
        CloseableKt.closeFinally(r2, th);
        throw th;
    }

    public static /* synthetic */ String readText$default(Path r0, Charset r1, int r2, Object r3) throws IOException {
        if ((r2 & 1) == 0) goto L6;
        r1 = Charsets.UTF_8;
    L6:
        return PathsKt.readText(r0, r1);
    }

    private static final InputStreamReader reader(Path r2, Charset r3, OpenOption... r4) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "charset");
        Intrinsics.checkNotNullParameter(r4, "options");
        return new InputStreamReader(Files.newInputStream(r2, (OpenOption[]) Arrays.copyOf(r4, r4.length)), r3);
    }

    static /* synthetic */ InputStreamReader reader$default(Path r0, Charset r1, OpenOption[] r2, int r3, Object r4) throws IOException {
        if ((r3 & 1) == 0) goto L5;
        r1 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "charset");
        Intrinsics.checkNotNullParameter(r2, "options");
        return new InputStreamReader(Files.newInputStream(r0, (OpenOption[]) Arrays.copyOf(r2, r2.length)), r1);
    }

    private static final <T> T useLines(Path r6, Charset r7, Function1<? super Sequence<String>, ? extends T> r8) throws IOException {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "charset");
        Intrinsics.checkNotNullParameter(r8, "block");
        BufferedReader r0 = Files.newBufferedReader(r6, r7);
        BufferedReader r3 = r0;     // Catch: Throwable -> L10
        Intrinsics.checkNotNull(r3);     // Catch: Throwable -> L10
        T r5 = r8.invoke(TextStreamsKt.lineSequence(r3));     // Catch: Throwable -> L10
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L7;
        CloseableKt.closeFinally(r0, null);
    L9:
        return r5;
    L7:
        if (r0 == null) goto L9;
        r0.close();
    L10:
        th = move-exception;
        throw th;     // Catch: Throwable -> L12
    L12:
        th = move-exception;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == true) goto L20;
        if (r0 != null) goto L22;
    L21:
        throw th;
    L22:
        r0.close();     // Catch: Throwable -> L18
        goto L21
    L20:
        CloseableKt.closeFinally(r0, th);
        goto L21
    }

    static /* synthetic */ Object useLines$default(Path r4, Charset r5, Function1 r6, int r7, Object r8) throws IOException {
        if ((r7 & 1) == 0) goto L5;
        r5 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "charset");
        Intrinsics.checkNotNullParameter(r6, "block");
        BufferedReader r9 = Files.newBufferedReader(r4, r5);
        BufferedReader r1 = r9;     // Catch: Throwable -> L13
        Intrinsics.checkNotNull(r1);     // Catch: Throwable -> L13
        Object r3 = r6.invoke(TextStreamsKt.lineSequence(r1));     // Catch: Throwable -> L13
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L10;
        CloseableKt.closeFinally(r9, null);
    L12:
        return r3;
    L10:
        if (r9 == null) goto L12;
        r9.close();
    L13:
        th = move-exception;
        throw th;     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == true) goto L23;
        if (r9 != null) goto L25;
    L24:
        throw th;
    L25:
        r9.close();     // Catch: Throwable -> L21
        goto L24
    L23:
        CloseableKt.closeFinally(r9, th);
        goto L24
    }

    private static final void writeBytes(Path r1, byte[] r2, OpenOption... r3) throws IOException {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "array");
        Intrinsics.checkNotNullParameter(r3, "options");
        Files.write(r1, r2, (OpenOption[]) Arrays.copyOf(r3, r3.length));
    }

    private static final Path writeLines(Path r2, Iterable<? extends CharSequence> r3, Charset r4, OpenOption... r5) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "lines");
        Intrinsics.checkNotNullParameter(r4, "charset");
        Intrinsics.checkNotNullParameter(r5, "options");
        Path r0 = Files.write(r2, r3, r4, (OpenOption[]) Arrays.copyOf(r5, r5.length));
        Intrinsics.checkNotNullExpressionValue(r0, "write(...)");
        return r0;
    }

    private static final Path writeLines(Path r2, Sequence<? extends CharSequence> r3, Charset r4, OpenOption... r5) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "lines");
        Intrinsics.checkNotNullParameter(r4, "charset");
        Intrinsics.checkNotNullParameter(r5, "options");
        Path r0 = Files.write(r2, SequencesKt.asIterable(r3), r4, (OpenOption[]) Arrays.copyOf(r5, r5.length));
        Intrinsics.checkNotNullExpressionValue(r0, "write(...)");
        return r0;
    }

    static /* synthetic */ Path writeLines$default(Path r0, Iterable r1, Charset r2, OpenOption[] r3, int r4, Object r5) throws IOException {
        if ((r4 & 2) == 0) goto L5;
        r2 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "lines");
        Intrinsics.checkNotNullParameter(r2, "charset");
        Intrinsics.checkNotNullParameter(r3, "options");
        Path r6 = Files.write(r0, r1, r2, (OpenOption[]) Arrays.copyOf(r3, r3.length));
        Intrinsics.checkNotNullExpressionValue(r6, "write(...)");
        return r6;
    }

    static /* synthetic */ Path writeLines$default(Path r0, Sequence r1, Charset r2, OpenOption[] r3, int r4, Object r5) throws IOException {
        if ((r4 & 2) == 0) goto L5;
        r2 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "lines");
        Intrinsics.checkNotNullParameter(r2, "charset");
        Intrinsics.checkNotNullParameter(r3, "options");
        Path r6 = Files.write(r0, SequencesKt.asIterable(r1), r2, (OpenOption[]) Arrays.copyOf(r3, r3.length));
        Intrinsics.checkNotNullExpressionValue(r6, "write(...)");
        return r6;
    }

    public static final void writeText(Path r3, CharSequence r4, Charset r5, OpenOption... r6) throws IOException {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "text");
        Intrinsics.checkNotNullParameter(r5, "charset");
        Intrinsics.checkNotNullParameter(r6, "options");
        OutputStream r0 = Files.newOutputStream(r3, (OpenOption[]) Arrays.copyOf(r6, r6.length));
        Intrinsics.checkNotNullExpressionValue(r0, "newOutputStream(...)");
        OutputStreamWriter r1 = new OutputStreamWriter(r0, r5);
        r1.append(r4);     // Catch: Throwable -> L6
        CloseableKt.closeFinally(r1, null);
        return;
    L6:
        th = move-exception;
        throw th;     // Catch: Throwable -> L8
    L8:
        th = move-exception;
        CloseableKt.closeFinally(r1, th);
        throw th;
    }

    public static /* synthetic */ void writeText$default(Path r0, CharSequence r1, Charset r2, OpenOption[] r3, int r4, Object r5) throws IOException {
        if ((r4 & 2) == 0) goto L5;
        r2 = Charsets.UTF_8;
    L5:
        PathsKt.writeText(r0, r1, r2, r3);
    }

    private static final OutputStreamWriter writer(Path r2, Charset r3, OpenOption... r4) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "charset");
        Intrinsics.checkNotNullParameter(r4, "options");
        return new OutputStreamWriter(Files.newOutputStream(r2, (OpenOption[]) Arrays.copyOf(r4, r4.length)), r3);
    }

    static /* synthetic */ OutputStreamWriter writer$default(Path r0, Charset r1, OpenOption[] r2, int r3, Object r4) throws IOException {
        if ((r3 & 1) == 0) goto L5;
        r1 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "charset");
        Intrinsics.checkNotNullParameter(r2, "options");
        return new OutputStreamWriter(Files.newOutputStream(r0, (OpenOption[]) Arrays.copyOf(r2, r2.length)), r1);
    }
}
