package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingListeningExecutorService extends ForwardingExecutorService implements ListeningExecutorService {
    protected ForwardingListeningExecutorService() {
    }

    @Override // com.google.common.util.concurrent.ForwardingExecutorService, com.google.common.collect.ForwardingObject
    protected abstract ListeningExecutorService delegate();

    @Override // com.google.common.util.concurrent.ForwardingExecutorService, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.util.concurrent.ForwardingExecutorService, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ ExecutorService delegate() {
        return delegate();
    }

    @Override // com.google.common.util.concurrent.ForwardingExecutorService, java.util.concurrent.ExecutorService
    public ListenableFuture<?> submit(Runnable r2) {
        return delegate().submit(r2);
    }

    @Override // com.google.common.util.concurrent.ForwardingExecutorService, java.util.concurrent.ExecutorService
    public <T> ListenableFuture<T> submit(Runnable r2, @ParametricNullness T r3) {
        return delegate().submit(r2, r3);
    }

    @Override // com.google.common.util.concurrent.ForwardingExecutorService, java.util.concurrent.ExecutorService
    public <T> ListenableFuture<T> submit(Callable<T> r2) {
        return delegate().submit(r2);
    }

    @Override // com.google.common.util.concurrent.ForwardingExecutorService, java.util.concurrent.ExecutorService
    public /* bridge */ /* synthetic */ Future submit(Runnable r1) {
        return submit(r1);
    }

    @Override // com.google.common.util.concurrent.ForwardingExecutorService, java.util.concurrent.ExecutorService
    public /* bridge */ /* synthetic */ Future submit(Runnable r1, @ParametricNullness Object r2) {
        return submit(r1, r2);
    }

    @Override // com.google.common.util.concurrent.ForwardingExecutorService, java.util.concurrent.ExecutorService
    public /* bridge */ /* synthetic */ Future submit(Callable r1) {
        return submit(r1);
    }
}
