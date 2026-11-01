package kotlin.p005io;

import java.io.InputStream;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Console.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000<\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0005\n\u0002\u0010\f\n\u0002\u0010\u0019\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\u001a\u0013\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0004H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0005H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0007H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\bH\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\tH\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\nH\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000bH\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\fH\u0087\b\u001a\t\u0010\r\u001a\u00020\u0001H\u0087\b\u001a\u0013\u0010\r\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0004H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0005H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0007H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\bH\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\tH\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\nH\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000bH\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\fH\u0087\b\u001a\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u001a\b\u0010\u0010\u001a\u00020\u000fH\u0007\u001a\n\u0010\u0011\u001a\u0004\u0018\u00010\u000fH\u0007¨\u0006\u0012"}, m421d2 = {"print", "", "message", "", "", "", "", "", "", "", "", "", "", "println", "readLine", "", "readln", "readlnOrNull", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class ConsoleKt {
    private static final void print(byte r2) {
        System.out.print(Byte.valueOf(r2));
    }

    private static final void print(char r1) {
        System.out.print(r1);
    }

    private static final void print(double r1) {
        System.out.print(r1);
    }

    private static final void print(float r1) {
        System.out.print(r1);
    }

    private static final void print(int r1) {
        System.out.print(r1);
    }

    private static final void print(long r1) {
        System.out.print(r1);
    }

    private static final void print(Object r1) {
        System.out.print(r1);
    }

    private static final void print(short r2) {
        System.out.print(Short.valueOf(r2));
    }

    private static final void print(boolean r1) {
        System.out.print(r1);
    }

    private static final void print(char[] r1) {
        Intrinsics.checkNotNullParameter(r1, "message");
        System.out.print(r1);
    }

    private static final void println() {
        System.out.println();
    }

    private static final void println(byte r2) {
        System.out.println(Byte.valueOf(r2));
    }

    private static final void println(char r1) {
        System.out.println(r1);
    }

    private static final void println(double r1) {
        System.out.println(r1);
    }

    private static final void println(float r1) {
        System.out.println(r1);
    }

    private static final void println(int r1) {
        System.out.println(r1);
    }

    private static final void println(long r1) {
        System.out.println(r1);
    }

    private static final void println(Object r1) {
        System.out.println(r1);
    }

    private static final void println(short r2) {
        System.out.println(Short.valueOf(r2));
    }

    private static final void println(boolean r1) {
        System.out.println(r1);
    }

    private static final void println(char[] r1) {
        Intrinsics.checkNotNullParameter(r1, "message");
        System.out.println(r1);
    }

    public static final String readLine() {
        LineReader r0 = LineReader.INSTANCE;
        InputStream r1 = System.in;
        Intrinsics.checkNotNullExpressionValue(r1, "in");
        Charset r2 = Charset.defaultCharset();
        Intrinsics.checkNotNullExpressionValue(r2, "defaultCharset(...)");
        return r0.readLine(r1, r2);
    }

    public static final String readln() {
        String r0 = readlnOrNull();
        if (r0 == null) goto L6;
        return r0;
    L6:
        throw new ReadAfterEOFException("EOF has already been reached");
    }

    public static final String readlnOrNull() {
        return readLine();
    }
}
