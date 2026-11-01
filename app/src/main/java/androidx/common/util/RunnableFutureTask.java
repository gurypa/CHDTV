package androidx.media3.common.util;

import java.lang.Exception;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
public abstract class RunnableFutureTask<R, E extends Exception> implements RunnableFuture<R> {
    private final Object cancelLock;
    private boolean canceled;
    private Exception exception;
    private final ConditionVariable finished;
    private R result;
    private final ConditionVariable started;
    private Thread workThread;

    protected RunnableFutureTask() {
        this.started = new ConditionVariable();
        this.finished = new ConditionVariable();
        this.cancelLock = new Object();
    }

    private R getResult() throws ExecutionException {
        if (this.canceled == true) goto L11;
        if (this.exception != null) goto L9;
        return this.result;
    L9:
        throw new ExecutionException(this.exception);
    L11:
        throw new CancellationException();
    }

    public final void blockUntilFinished() {
        this.finished.blockUninterruptible();
    }

    public final void blockUntilStarted() {
        this.started.blockUninterruptible();
    }

    @Override // java.util.concurrent.Future
    public final boolean cancel(boolean r5) {
        Object r0 = this.cancelLock;
        monitor-enter(r0);
    L19:
        th = move-exception;
        throw th;
    L5:
        if (this.canceled == false) goto L7;
    L16:
        monitor-exit(r0);     // Catch: Throwable -> L19
        return false;
    L7:
        if (this.finished.isOpen() == true) goto L16;
        this.canceled = true;     // Catch: Throwable -> L19
        cancelWork();     // Catch: Throwable -> L19
        Thread r2 = this.workThread;     // Catch: Throwable -> L19
        if (r2 == null) goto L13;
        if (r5 == false) goto L14;
        r2.interrupt();     // Catch: Throwable -> L19
    L14:
        monitor-exit(r0);     // Catch: Throwable -> L19
        return true;
    L13:
        this.started.open();     // Catch: Throwable -> L19
        this.finished.open();     // Catch: Throwable -> L19
        goto L14
    }

    protected void cancelWork() {
    }

    protected abstract R doWork() throws Exception;

    @Override // java.util.concurrent.Future
    public final R get() throws ExecutionException, InterruptedException {
        this.finished.block();
        return getResult();
    }

    @Override // java.util.concurrent.Future
    public final R get(long r4, TimeUnit r6) throws ExecutionException, InterruptedException, TimeoutException {
        long r0 = TimeUnit.MILLISECONDS.convert(r4, r6);
        if (this.finished.block(r0) == false) goto L7;
        return getResult();
    L7:
        throw new TimeoutException();
    }

    @Override // java.util.concurrent.Future
    public final boolean isCancelled() {
        return this.canceled;
    }

    @Override // java.util.concurrent.Future
    public final boolean isDone() {
        return this.finished.isOpen();
    }

    @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
    public final void run() {
        Object r0 = this.cancelLock;
        monitor-enter(r0);
    L41:
        th = move-exception;
        throw th;
    L5:
        if (this.canceled == false) goto L8;
        monitor-exit(r0);     // Catch: Throwable -> L41
        return;
    L8:
        this.workThread = Thread.currentThread();     // Catch: Throwable -> L41
        monitor-exit(r0);     // Catch: Throwable -> L41
        this.started.open();
        this.result = doWork();     // Catch: Throwable -> L20 Exception -> L22
        Object r1 = this.cancelLock;
        monitor-enter(r1);
        this.finished.open();     // Catch: Throwable -> L17
        this.workThread = null;     // Catch: Throwable -> L17
        Thread.interrupted();     // Catch: Throwable -> L17
        monitor-exit(r1);     // Catch: Throwable -> L17
    L29:
        return;
    L17:
        th = move-exception;
        throw th;
    L22:
        e = move-exception;
        this.exception = e;     // Catch: Throwable -> L20
        monitor-enter(this.cancelLock);
        this.finished.open();     // Catch: Throwable -> L30
        this.workThread = null;     // Catch: Throwable -> L30
        Thread.interrupted();     // Catch: Throwable -> L30
    L30:
        th = move-exception;
        throw th;
    L20:
        th = move-exception;
        monitor-enter(this.cancelLock);
        this.finished.open();     // Catch: Throwable -> L38
        this.workThread = null;     // Catch: Throwable -> L38
        Thread.interrupted();     // Catch: Throwable -> L38
        throw th;
    L38:
        th = move-exception;
        throw th;
    }
}
