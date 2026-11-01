package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class WrappingExecutorService implements ExecutorService {
    private final ExecutorService delegate;

    protected WrappingExecutorService(ExecutorService r2) {
        this.delegate = (ExecutorService) Preconditions.checkNotNull(r2);
    }

    static /* synthetic */ void lambda$wrapTask$0(Callable r2) {
        r2.call();     // Catch: Exception -> L4
        return;
    L4:
        e = move-exception;
        Platform.restoreInterruptIfIsInterruptedException(e);
        Throwables.throwIfUnchecked(e);
        throw new RuntimeException(e);
    }

    private <T> ImmutableList<Callable<T>> wrapTasks(Collection<? extends Callable<T>> r5) {
        ImmutableList.Builder r0 = ImmutableList.builder();
        Iterator<? extends Callable<T>> r1 = r5.iterator();
    L4:
        if (r1.hasNext() == false) goto L7;
        r0.add(wrapTask(r1.next()));
        goto L4
    L7:
        return r0.build();
    }

    @Override // java.util.concurrent.ExecutorService
    public final boolean awaitTermination(long r2, TimeUnit r4) throws InterruptedException {
        return this.delegate.awaitTermination(r2, r4);
    }

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable r3) {
        this.delegate.execute(wrapTask(r3));
    }

    @Override // java.util.concurrent.ExecutorService
    public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> r3) throws InterruptedException {
        return this.delegate.invokeAll(wrapTasks(r3));
    }

    @Override // java.util.concurrent.ExecutorService
    public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> r3, long r4, TimeUnit r6) throws InterruptedException {
        return this.delegate.invokeAll(wrapTasks(r3), r4, r6);
    }

    @Override // java.util.concurrent.ExecutorService
    public final <T> T invokeAny(Collection<? extends Callable<T>> r3) throws InterruptedException, ExecutionException {
        return (T) this.delegate.invokeAny(wrapTasks(r3));
    }

    @Override // java.util.concurrent.ExecutorService
    public final <T> T invokeAny(Collection<? extends Callable<T>> r3, long r4, TimeUnit r6) throws InterruptedException, ExecutionException, TimeoutException {
        return (T) this.delegate.invokeAny(wrapTasks(r3), r4, r6);
    }

    @Override // java.util.concurrent.ExecutorService
    public final boolean isShutdown() {
        return this.delegate.isShutdown();
    }

    @Override // java.util.concurrent.ExecutorService
    public final boolean isTerminated() {
        return this.delegate.isTerminated();
    }

    @Override // java.util.concurrent.ExecutorService
    public final void shutdown() {
        this.delegate.shutdown();
    }

    @Override // java.util.concurrent.ExecutorService
    public final List<Runnable> shutdownNow() {
        return this.delegate.shutdownNow();
    }

    @Override // java.util.concurrent.ExecutorService
    public final Future<?> submit(Runnable r3) {
        return this.delegate.submit(wrapTask(r3));
    }

    @Override // java.util.concurrent.ExecutorService
    public final <T> Future<T> submit(Runnable r3, @ParametricNullness T r4) {
        return this.delegate.submit(wrapTask(r3), r4);
    }

    @Override // java.util.concurrent.ExecutorService
    public final <T> Future<T> submit(Callable<T> r3) {
        return this.delegate.submit(wrapTask((Callable) Preconditions.checkNotNull(r3)));
    }

    protected Runnable wrapTask(Runnable r3) {
        final Callable r0 = wrapTask(Executors.callable(r3, null));
        return new WrappingExecutorService$$ExternalSyntheticLambda0(r0);
    }

    protected abstract <T> Callable<T> wrapTask(Callable<T> r1);
}
