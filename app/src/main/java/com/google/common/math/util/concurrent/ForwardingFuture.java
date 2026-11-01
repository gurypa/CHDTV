package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingObject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingFuture<V> extends ForwardingObject implements Future<V> {

    public static abstract class SimpleForwardingFuture<V> extends ForwardingFuture<V> {
        private final Future<V> delegate;

        protected SimpleForwardingFuture(Future<V> r2) {
            this.delegate = (Future) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
        protected final Future<V> delegate() {
            return this.delegate;
        }
    }

    protected ForwardingFuture() {
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean r2) {
        return delegate().cancel(r2);
    }

    @Override // com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingObject
    protected abstract Future<? extends V> delegate();

    @Override // java.util.concurrent.Future
    @ParametricNullness
    public V get() throws InterruptedException, ExecutionException {
        return delegate().get();
    }

    @Override // java.util.concurrent.Future
    @ParametricNullness
    public V get(long r2, TimeUnit r4) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate().get(r2, r4);
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return delegate().isCancelled();
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return delegate().isDone();
    }
}
