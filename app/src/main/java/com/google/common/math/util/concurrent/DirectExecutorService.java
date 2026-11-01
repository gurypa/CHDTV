package com.google.common.util.concurrent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class DirectExecutorService extends AbstractListeningExecutorService {
    private final Object lock;
    private int runningTasks;
    private boolean shutdown;

    DirectExecutorService() {
        this.lock = new Object();
        this.runningTasks = 0;
        this.shutdown = false;
    }

    private void endTask() {
        Object r0 = this.lock;
        monitor-enter(r0);
        int r1 = this.runningTasks - 1;     // Catch: Throwable -> L9
        this.runningTasks = r1;     // Catch: Throwable -> L9
        if (r1 != 0) goto L7;
        this.lock.notifyAll();     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
        return;
    L9:
        th = move-exception;
        throw th;
    }

    private void startTask() {
        Object r0 = this.lock;
        monitor-enter(r0);
    L11:
        th = move-exception;
        throw th;
    L5:
        if (this.shutdown == true) goto L10;
        this.runningTasks++;     // Catch: Throwable -> L11
        monitor-exit(r0);     // Catch: Throwable -> L11
        return;
    L10:
        throw new RejectedExecutionException("Executor already shutdown");     // Catch: Throwable -> L11
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long r8, TimeUnit r10) throws InterruptedException {
        long r0 = r10.toNanos(r8);
        Object r2 = this.lock;
        monitor-enter(r2);
    L20:
    L17:
        th = move-exception;
        throw th;
    L5:
        if (this.shutdown == false) goto L12;
        if (this.runningTasks != 0) goto L12;
        monitor-exit(r2);     // Catch: Throwable -> L17
        return true;
    L12:
        if (r0 <= 0) goto L13;
        long r3 = System.nanoTime();     // Catch: Throwable -> L17
        TimeUnit.NANOSECONDS.timedWait(this.lock, r0);     // Catch: Throwable -> L17
        r0 = r0 - (System.nanoTime() - r3);     // Catch: Throwable -> L17
        goto L20
    L13:
        monitor-exit(r2);     // Catch: Throwable -> L17
        return false;
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable r2) {
        startTask();
        r2.run();     // Catch: Throwable -> L6
        endTask();
        return;
    L6:
        th = move-exception;
        endTask();
        throw th;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        Object r0 = this.lock;
        monitor-enter(r0);
        boolean r1 = this.shutdown;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        Object r0 = this.lock;
        monitor-enter(r0);
    L12:
        th = move-exception;
        throw th;
    L5:
        if (this.shutdown == true) goto L7;
    L9:
        boolean r1 = false;
    L10:
        monitor-exit(r0);     // Catch: Throwable -> L12
        return r1;
    L7:
        if (this.runningTasks != 0) goto L9;
        r1 = true;
        goto L10
    }

    @Override // java.util.concurrent.ExecutorService
    public void shutdown() {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.shutdown = true;     // Catch: Throwable -> L10
        if (this.runningTasks != 0) goto L8;
        this.lock.notifyAll();     // Catch: Throwable -> L10
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L10
        return;
    L10:
        th = move-exception;
        throw th;
    }

    @Override // java.util.concurrent.ExecutorService
    public List<Runnable> shutdownNow() {
        shutdown();
        return Collections.emptyList();
    }
}
