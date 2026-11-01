package kotlin.p005io;

import androidx.exifinterface.media.ExifInterface;
import java.io.Closeable;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Closeable.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0018\u0010\u0000\u001a\u00020\u0001*\u0004\u0018\u00010\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0001\u001aH\u0010\u0005\u001a\u0002H\u0006\"\n\b\u0000\u0010\u0007*\u0004\u0018\u00010\u0002\"\u0004\b\u0001\u0010\u0006*\u0002H\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u0002H\u00060\tH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\n\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u000b"}, m421d2 = {"closeFinally", "", "Ljava/io/Closeable;", "cause", "", "use", "R", ExifInterface.GPS_DIRECTION_TRUE, "block", "Lkotlin/Function1;", "(Ljava/io/Closeable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class CloseableKt {
    public static final void closeFinally(Closeable r1, Throwable r2) {
        if (r1 == null) goto L13;
        if (r2 != null) goto L11;
        r1.close();
        return;
    L11:
        r1.close();     // Catch: Throwable -> L8
        return;
    L8:
        th = move-exception;
        ExceptionsKt.addSuppressed(r2, th);
        return;
    }

    private static final <T extends Closeable, R> R use(T r4, Function1<? super T, ? extends R> r5) {
        Intrinsics.checkNotNullParameter(r5, "block");
        R r3 = r5.invoke(r4);     // Catch: Throwable -> L11
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L7;
        closeFinally(r4, null);
    L10:
        return r3;
    L7:
        if (r4 == null) goto L10;
        r4.close();
    L11:
        th = move-exception;
        throw th;     // Catch: Throwable -> L14
    L14:
        th = move-exception;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == true) goto L22;
        if (r4 != null) goto L26;
    L23:
        throw th;
    L26:
        r4.close();     // Catch: Throwable -> L20
        goto L23
    L22:
        closeFinally(r4, th);
        goto L23
    }
}
