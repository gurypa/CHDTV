package com.google.common.util.concurrent;

import com.google.common.collect.ForwardingObject;
import com.google.errorprone.annotations.CheckReturnValue;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingExecutorService extends ForwardingObject implements ExecutorService {
    protected ForwardingExecutorService() {
    }

    @Override // java.util.concurrent.ExecutorService
    @CheckReturnValue
    public boolean awaitTermination(long r2, TimeUnit r4) throws InterruptedException {
        return delegate().awaitTermination(r2, r4);
    }

    @Override // com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingObject
    protected abstract ExecutorService delegate();

    @Override // java.util.concurrent.Executor
    public void execute(Runnable r2) {
        delegate().execute(r2);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> r2) throws InterruptedException {
        return delegate().invokeAll(r2);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> r2, long r3, TimeUnit r5) throws InterruptedException {
        return delegate().invokeAll(r2, r3, r5);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> T invokeAny(Collection<? extends Callable<T>> r2) throws InterruptedException, ExecutionException {
        return (T) delegate().invokeAny(r2);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> T invokeAny(Collection<? extends Callable<T>> r2, long r3, TimeUnit r5) throws InterruptedException, ExecutionException, TimeoutException {
        return (T) delegate().invokeAny(r2, r3, r5);
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return delegate().isShutdown();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return delegate().isTerminated();
    }

    @Override // java.util.concurrent.ExecutorService
    public void shutdown() {
        delegate().shutdown();
    }

    @Override // java.util.concurrent.ExecutorService
    public List<Runnable> shutdownNow() {
        return delegate().shutdownNow();
    }

    @Override // java.util.concurrent.ExecutorService
    public Future<?> submit(Runnable r2) {
        return delegate().submit(r2);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Runnable r2, @ParametricNullness T r3) {
        return delegate().submit(r2, r3);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Callable<T> r2) {
        return delegate().submit(r2);
    }
}
