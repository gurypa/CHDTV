package es.antonio.m3u;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/* JADX INFO: loaded from: classes3.dex */
final class SingleFlight<T> {
    private final ConcurrentHashMap<String, FutureTask<T>> tasks;

    SingleFlight() {
        this.tasks = new ConcurrentHashMap();
    }

    T get(String r7, Callable<T> r8) throws Exception {
        FutureTask<T> r0 = new FutureTask(r8);
        FutureTask<T> r1 = this.tasks.putIfAbsent(r7, r0);
        if (r1 != null) goto L5;
        boolean r2 = true;
    L6:
        if (r2 == false) goto L27;
        r1 = r0;
        r1.run();
    L27:
        T r3 = r1.get();     // Catch: Throwable -> L12 ExecutionException -> L14
        if (r2 == false) goto L11;
        this.tasks.remove(r7, r1);
    L11:
        return r3;
    L14:
        e = move-exception;
        Throwable r4 = e.getCause();     // Catch: Throwable -> L12
        if ((r4 instanceof Exception) == true) goto L31;
        if ((r4 instanceof Error) == false) goto L30;
        throw ((Error) r4);     // Catch: Throwable -> L12
    L30:
        throw new RuntimeException(r4);     // Catch: Throwable -> L12
    L31:
        throw ((Exception) r4);     // Catch: Throwable -> L12
    L12:
        th = move-exception;
        if (r2 == false) goto L26;
        this.tasks.remove(r7, r1);
    L26:
        throw th;
    L5:
        r2 = false;
        goto L6
    }
}
