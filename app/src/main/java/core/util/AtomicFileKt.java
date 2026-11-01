package androidx.core.util;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: AtomicFile.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\u0016\u0010\u0003\u001a\u00020\u0004*\u00020\u00022\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007\u001a3\u0010\u0007\u001a\u00020\b*\u00020\u00022!\u0010\t\u001a\u001d\u0012\u0013\u0012\u00110\u000b¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u00020\b0\nH\u0087\bø\u0001\u0000\u001a\u0014\u0010\u000f\u001a\u00020\b*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u0001H\u0007\u001a\u001e\u0010\u0011\u001a\u00020\b*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0013"}, m421d2 = {"readBytes", "", "Landroid/util/AtomicFile;", "readText", "", "charset", "Ljava/nio/charset/Charset;", "tryWrite", "", "block", "Lkotlin/Function1;", "Ljava/io/FileOutputStream;", "Lkotlin/ParameterName;", "name", "out", "writeBytes", "array", "writeText", "text", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class AtomicFileKt {
    public static final byte[] readBytes(android.util.AtomicFile r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        byte[] r1 = r3.readFully();
        Intrinsics.checkNotNullExpressionValue(r1, "readFully()");
        return r1;
    }

    public static final String readText(android.util.AtomicFile r2, Charset r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "charset");
        byte[] r0 = r2.readFully();
        Intrinsics.checkNotNullExpressionValue(r0, "readFully()");
        return new String(r0, r3);
    }

    public static /* synthetic */ String readText$default(android.util.AtomicFile r0, Charset r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = Charsets.UTF_8;
    L6:
        return readText(r0, r1);
    }

    public static final void tryWrite(android.util.AtomicFile r4, Function1<? super FileOutputStream, Unit> r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "block");
        FileOutputStream r1 = r4.startWrite();
        Intrinsics.checkNotNullExpressionValue(r1, "stream");     // Catch: Throwable -> L6
        r5.invoke(r1);     // Catch: Throwable -> L6
        r4.finishWrite(r1);
        return;
    L6:
        th = move-exception;
        r4.failWrite(r1);
        throw th;
    }

    public static final void writeBytes(android.util.AtomicFile r6, byte[] r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "array");
        FileOutputStream r2 = r6.startWrite();
        Intrinsics.checkNotNullExpressionValue(r2, "stream");     // Catch: Throwable -> L6
        r2.write(r7);     // Catch: Throwable -> L6
        r6.finishWrite(r2);
        return;
    L6:
        th = move-exception;
        r6.failWrite(r2);
        throw th;
    }

    public static final void writeText(android.util.AtomicFile r2, String r3, Charset r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "text");
        Intrinsics.checkNotNullParameter(r4, "charset");
        byte[] r0 = r3.getBytes(r4);
        Intrinsics.checkNotNullExpressionValue(r0, "this as java.lang.String).getBytes(charset)");
        writeBytes(r2, r0);
    }

    public static /* synthetic */ void writeText$default(android.util.AtomicFile r0, String r1, Charset r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L5;
        r2 = Charsets.UTF_8;
    L5:
        writeText(r0, r1, r2);
    }
}
