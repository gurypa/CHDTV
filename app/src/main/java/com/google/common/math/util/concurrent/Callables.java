package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Callables {
    private Callables() {
    }

    public static <T> AsyncCallable<T> asAsyncCallable(final Callable<T> r1, final ListeningExecutorService r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new Callables$$ExternalSyntheticLambda1(r2, r1);
    }

    static /* synthetic */ ListenableFuture lambda$asAsyncCallable$1(ListeningExecutorService r1, Callable r2) throws Exception {
        return r1.submit(r2);
    }

    static /* synthetic */ Object lambda$returning$0(Object r0) throws Exception {
        return r0;
    }

    static /* synthetic */ Object lambda$threadRenaming$2(Supplier r4, Callable r5) throws Exception {
        Thread r0 = Thread.currentThread();
        String r1 = r0.getName();
        boolean r2 = trySetName((String) r4.get(), r0);
        Object r3 = r5.call();     // Catch: Throwable -> L7
        if (r2 == false) goto L6;
        trySetName(r1, r0);
    L6:
        return r3;
    L7:
        th = move-exception;
        if (r2 == false) goto L10;
        trySetName(r1, r0);
    L10:
        throw th;
    }

    static /* synthetic */ void lambda$threadRenaming$3(Supplier r4, Runnable r5) {
        Thread r0 = Thread.currentThread();
        String r1 = r0.getName();
        boolean r2 = trySetName((String) r4.get(), r0);
        r5.run();     // Catch: Throwable -> L7
        if (r2 == false) goto L13;
        trySetName(r1, r0);
        return;
    L13:
        return;
    L7:
        th = move-exception;
        if (r2 == false) goto L10;
        trySetName(r1, r0);
    L10:
        throw th;
    }

    public static <T> Callable<T> returning(@ParametricNullness final T r1) {
        return new Callables$$ExternalSyntheticLambda2(r1);
    }

    static Runnable threadRenaming(final Runnable r1, final Supplier<String> r2) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r1);
        return new Callables$$ExternalSyntheticLambda3(r2, r1);
    }

    static <T> Callable<T> threadRenaming(final Callable<T> r1, final Supplier<String> r2) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r1);
        return new Callables$$ExternalSyntheticLambda0(r2, r1);
    }

    private static boolean trySetName(String r2, Thread r3) {
        r3.setName(r2);     // Catch: SecurityException -> L5
        return true;
    L6:
        return false;
    }
}
