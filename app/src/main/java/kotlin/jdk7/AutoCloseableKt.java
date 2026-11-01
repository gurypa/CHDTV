package kotlin.jdk7;

import androidx.exifinterface.media.ExifInterface;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AutoCloseableJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0018\u0010\u0000\u001a\u00020\u0001*\u0004\u0018\u00010\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0001\u001aH\u0010\u0005\u001a\u0002H\u0006\"\n\b\u0000\u0010\u0007*\u0004\u0018\u00010\u0002\"\u0004\b\u0001\u0010\u0006*\u0002H\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u0002H\u00060\tH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\n\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u000b"}, m421d2 = {"closeFinally", "", "Ljava/lang/AutoCloseable;", "cause", "", "use", "R", ExifInterface.GPS_DIRECTION_TRUE, "block", "Lkotlin/Function1;", "(Ljava/lang/AutoCloseable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlin-stdlib-jdk7"}, m422k = 2, m423mv = {1, 9, 0}, m424pn = "kotlin", m425xi = 48)
public final class AutoCloseableKt {
    public static final void closeFinally(AutoCloseable r1, Throwable r2) {
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

    private static final <T extends AutoCloseable, R> R use(T r2, Function1<? super T, ? extends R> r3) {
        Intrinsics.checkNotNullParameter(r3, "block");
        R r1 = r3.invoke(r2);     // Catch: Throwable -> L6
        closeFinally(r2, null);
        return r1;
    L6:
        th = move-exception;
        throw th;     // Catch: Throwable -> L9
    L9:
        th = move-exception;
        closeFinally(r2, th);
        throw th;
    }
}
