package com.google.common.util.concurrent;

import com.google.errorprone.annotations.CheckReturnValue;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
@CheckReturnValue
public abstract class AbstractListeningExecutorService extends AbstractExecutorService implements ListeningExecutorService {
    public AbstractListeningExecutorService() {
    }

    @Override // java.util.concurrent.AbstractExecutorService
    protected final <T> RunnableFuture<T> newTaskFor(Runnable r2, @ParametricNullness T r3) {
        return TrustedListenableFutureTask.create(r2, r3);
    }

    @Override // java.util.concurrent.AbstractExecutorService
    protected final <T> RunnableFuture<T> newTaskFor(Callable<T> r2) {
        return TrustedListenableFutureTask.create(r2);
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService, com.google.common.util.concurrent.ListeningExecutorService
    public ListenableFuture<?> submit(Runnable r2) {
        return (ListenableFuture) super.submit(r2);
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService, com.google.common.util.concurrent.ListeningExecutorService
    public <T> ListenableFuture<T> submit(Runnable r2, @ParametricNullness T r3) {
        return (ListenableFuture) super.submit(r2, r3);
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService, com.google.common.util.concurrent.ListeningExecutorService
    public <T> ListenableFuture<T> submit(Callable<T> r2) {
        return (ListenableFuture) super.submit(r2);
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public /* bridge */ /* synthetic */ Future submit(Runnable r1) {
        return submit(r1);
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public /* bridge */ /* synthetic */ Future submit(Runnable r1, @ParametricNullness Object r2) {
        return submit(r1, r2);
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public /* bridge */ /* synthetic */ Future submit(Callable r1) {
        return submit(r1);
    }
}
