package kotlin.p005io;

import androidx.exifinterface.media.ExifInterface;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: ReadWrite.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000X\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u001c\u0010\u0007\u001a\u00020\b*\u00020\u00022\u0006\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\u001e\u0010\n\u001a\u00020\u000b*\u00020\u00022\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000b0\r\u001a\u0010\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0010*\u00020\u0001\u001a\n\u0010\u0011\u001a\u00020\u0012*\u00020\u0013\u001a\u0010\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0015*\u00020\u0002\u001a\n\u0010\u0016\u001a\u00020\u000e*\u00020\u0002\u001a\u0017\u0010\u0016\u001a\u00020\u000e*\u00020\u00132\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0087\b\u001a\r\u0010\u0019\u001a\u00020\u001a*\u00020\u000eH\u0087\b\u001a5\u0010\u001b\u001a\u0002H\u001c\"\u0004\b\u0000\u0010\u001c*\u00020\u00022\u0018\u0010\u001d\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u0010\u0012\u0004\u0012\u0002H\u001c0\rH\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u001e\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u001f"}, m421d2 = {"buffered", "Ljava/io/BufferedReader;", "Ljava/io/Reader;", "bufferSize", "", "Ljava/io/BufferedWriter;", "Ljava/io/Writer;", "copyTo", "", "out", "forEachLine", "", "action", "Lkotlin/Function1;", "", "lineSequence", "Lkotlin/sequences/Sequence;", "readBytes", "", "Ljava/net/URL;", "readLines", "", "readText", "charset", "Ljava/nio/charset/Charset;", "reader", "Ljava/io/StringReader;", "useLines", ExifInterface.GPS_DIRECTION_TRUE, "block", "(Ljava/io/Reader;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class TextStreamsKt {
    private static final BufferedReader buffered(Reader r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if ((r1 instanceof BufferedReader) == false) goto L6;
        return (BufferedReader) r1;
    L6:
        return new BufferedReader(r1, r2);
    }

    private static final BufferedWriter buffered(Writer r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if ((r1 instanceof BufferedWriter) == false) goto L6;
        return (BufferedWriter) r1;
    L6:
        return new BufferedWriter(r1, r2);
    }

    static /* synthetic */ BufferedReader buffered$default(Reader r0, int r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = 8192;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        if ((r0 instanceof BufferedReader) == false) goto L9;
        return (BufferedReader) r0;
    L9:
        return new BufferedReader(r0, r1);
    }

    static /* synthetic */ BufferedWriter buffered$default(Writer r0, int r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = 8192;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        if ((r0 instanceof BufferedWriter) == false) goto L9;
        return (BufferedWriter) r0;
    L9:
        return new BufferedWriter(r0, r1);
    }

    public static final long copyTo(Reader r6, Writer r7, int r8) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "out");
        long r0 = 0;
        char[] r2 = new char[r8];
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

    public static /* synthetic */ long copyTo$default(Reader r0, Writer r1, int r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = 8192;
    L6:
        return copyTo(r0, r1, r2);
    }

    public static final void forEachLine(Reader r11, Function1<? super String, Unit> r12) {
        Intrinsics.checkNotNullParameter(r11, "<this>");
        Intrinsics.checkNotNullParameter(r12, "action");
        if ((r11 instanceof BufferedReader) == false) goto L5;
        BufferedReader r2 = (BufferedReader) r11;
    L6:
        BufferedReader r3 = r2;
        Iterator<String> r9 = lineSequence(r3).iterator();     // Catch: Throwable -> L14
    L9:
        if (r9.hasNext() == false) goto L11;
        r12.invoke(r9.next());     // Catch: Throwable -> L14
        goto L9
    L11:
        Unit r5 = Unit.INSTANCE;     // Catch: Throwable -> L14
        CloseableKt.closeFinally(r3, null);
        return;
    L14:
        th = move-exception;
        throw th;     // Catch: Throwable -> L16
    L16:
        th = move-exception;
        CloseableKt.closeFinally(r3, th);
        throw th;
    L5:
        r2 = new BufferedReader(r11, 8192);
        goto L6
    }

    public static final Sequence<String> lineSequence(BufferedReader r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return SequencesKt.constrainOnce(new LinesSequence(r1));
    }

    public static final byte[] readBytes(URL r4) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        InputStream r0 = r4.openStream();
        InputStream r1 = r0;     // Catch: Throwable -> L6
        Intrinsics.checkNotNull(r1);     // Catch: Throwable -> L6
        byte[] r3 = ByteStreamsKt.readBytes(r1);     // Catch: Throwable -> L6
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

    public static final List<String> readLines(Reader r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        final ArrayList r0 = new ArrayList();
        forEachLine(r2, new C10981(r0));
        return r0;
    }

    public static final String readText(Reader r5) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        StringWriter r0 = new StringWriter();
        copyTo$default(r5, r0, 0, 2, null);
        String r1 = r0.toString();
        Intrinsics.checkNotNullExpressionValue(r1, "toString(...)");
        return r1;
    }

    private static final String readText(URL r2, Charset r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "charset");
        return new String(readBytes(r2), r3);
    }

    static /* synthetic */ String readText$default(URL r0, Charset r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "charset");
        return new String(readBytes(r0), r1);
    }

    private static final StringReader reader(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new StringReader(r1);
    }

    public static final <T> T useLines(Reader r7, Function1<? super Sequence<String>, ? extends T> r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "block");
        if ((r7 instanceof BufferedReader) == false) goto L5;
        BufferedReader r1 = (BufferedReader) r7;
    L6:
        BufferedReader r2 = r1;
        T r6 = r8.invoke(lineSequence(r2));     // Catch: Throwable -> L13
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L11;
        CloseableKt.closeFinally(r2, null);
    L12:
        return r6;
    L11:
        r2.close();
    L13:
        th = move-exception;
        throw th;     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L24;
        CloseableKt.closeFinally(r2, th);
    L23:
        throw th;
    L24:
        r2.close();     // Catch: Throwable -> L20
        goto L23
    L5:
        r1 = new BufferedReader(r7, 8192);
        goto L6
    }
}
