package kotlin.concurrent;

import androidx.exifinterface.media.ExifInterface;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Locks.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001a\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a6\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\u0005\u001a6\u0010\u0006\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00072\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\b\u001a6\u0010\t\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\u0005\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\n"}, m421d2 = {"read", ExifInterface.GPS_DIRECTION_TRUE, "Ljava/util/concurrent/locks/ReentrantReadWriteLock;", "action", "Lkotlin/Function0;", "(Ljava/util/concurrent/locks/ReentrantReadWriteLock;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "withLock", "Ljava/util/concurrent/locks/Lock;", "(Ljava/util/concurrent/locks/Lock;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "write", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class LocksKt {
    private static final <T> T read(ReentrantReadWriteLock r2, Function0<? extends T> r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "action");
        ReentrantReadWriteLock.ReadLock r0 = r2.readLock();
        r0.lock();
        T r1 = r3.invoke();     // Catch: Throwable -> L6
        r0.unlock();
        return r1;
    L6:
        th = move-exception;
        r0.unlock();
        throw th;
    }

    private static final <T> T withLock(Lock r1, Function0<? extends T> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "action");
        r1.lock();
        T r0 = r2.invoke();     // Catch: Throwable -> L6
        r1.unlock();
        return r0;
    L6:
        th = move-exception;
        r1.unlock();
        throw th;
    }

    private static final <T> T write(ReentrantReadWriteLock r7, Function0<? extends T> r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "action");
        ReentrantReadWriteLock.ReadLock r0 = r7.readLock();
        int r2 = 0;
        if (r7.getWriteHoldCount() != 0) goto L5;
        int r1 = r7.getReadHoldCount();
    L6:
        int r3 = 0;
    L7:
        if (r3 >= r1) goto L9;
        r0.unlock();
        r3 = r3 + 1;
        goto L7
    L9:
        ReentrantReadWriteLock.WriteLock r4 = r7.writeLock();
        r4.lock();
        T r5 = r8.invoke();     // Catch: Throwable -> L15
    L11:
        if (r2 >= r1) goto L13;
        r0.lock();
        r2 = r2 + 1;
        goto L11
    L13:
        r4.unlock();
        return r5;
    L15:
        th = move-exception;
    L16:
        if (r2 >= r1) goto L18;
        r0.lock();
        r2 = r2 + 1;
        goto L16
    L18:
        r4.unlock();
        throw th;
    L5:
        r1 = 0;
        goto L6
    }
}
