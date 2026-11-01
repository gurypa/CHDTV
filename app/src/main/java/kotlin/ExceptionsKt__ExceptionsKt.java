package kotlin;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Exceptions.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00004\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\u0014\u0010\r\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0003H\u0007\u001a\r\u0010\u0010\u001a\u00020\u000e*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u0010\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u0012H\u0087\b\u001a\u0015\u0010\u0010\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0014H\u0087\b\u001a\f\u0010\u0015\u001a\u00020\u0016*\u00020\u0003H\u0007\"!\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038F¢\u0006\f\u0012\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0006\u0010\u0007\"$\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t*\u00020\u00038FX\u0087\u0004¢\u0006\f\u0012\u0004\b\n\u0010\u0005\u001a\u0004\b\u000b\u0010\f¨\u0006\u0017"}, m421d2 = {"stackTrace", "", "Ljava/lang/StackTraceElement;", "", "getStackTrace$annotations", "(Ljava/lang/Throwable;)V", "getStackTrace", "(Ljava/lang/Throwable;)[Ljava/lang/StackTraceElement;", "suppressedExceptions", "", "getSuppressedExceptions$annotations", "getSuppressedExceptions", "(Ljava/lang/Throwable;)Ljava/util/List;", "addSuppressed", "", "exception", "printStackTrace", "stream", "Ljava/io/PrintStream;", "writer", "Ljava/io/PrintWriter;", "stackTraceToString", "", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/ExceptionsKt")
class ExceptionsKt__ExceptionsKt {
    public ExceptionsKt__ExceptionsKt() {
    }

    public static final void addSuppressed(Throwable r1, Throwable r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "exception");
        if (r1 == r2) goto L6;
        PlatformImplementationsKt.IMPLEMENTATIONS.addSuppressed(r1, r2);
        return;
    }

    public static final StackTraceElement[] getStackTrace(Throwable r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        StackTraceElement[] r0 = r1.getStackTrace();
        Intrinsics.checkNotNull(r0);
        return r0;
    }

    public static /* synthetic */ void getStackTrace$annotations(Throwable r0) {
    }

    public static final List<Throwable> getSuppressedExceptions(Throwable r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return PlatformImplementationsKt.IMPLEMENTATIONS.getSuppressed(r1);
    }

    public static /* synthetic */ void getSuppressedExceptions$annotations(Throwable r0) {
    }

    private static final void printStackTrace(Throwable r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1.printStackTrace();
    }

    private static final void printStackTrace(Throwable r1, PrintStream r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "stream");
        r1.printStackTrace(r2);
    }

    private static final void printStackTrace(Throwable r1, PrintWriter r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "writer");
        r1.printStackTrace(r2);
    }

    public static final String stackTraceToString(Throwable r4) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        StringWriter r0 = new StringWriter();
        PrintWriter r1 = new PrintWriter(r0);
        r4.printStackTrace(r1);
        r1.flush();
        String r2 = r0.toString();
        Intrinsics.checkNotNullExpressionValue(r2, "toString(...)");
        return r2;
    }
}
