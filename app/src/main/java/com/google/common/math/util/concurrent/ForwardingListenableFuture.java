package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingListenableFuture<V> extends ForwardingFuture<V> implements ListenableFuture<V> {

    public static abstract class SimpleForwardingListenableFuture<V> extends ForwardingListenableFuture<V> {
        private final ListenableFuture<V> delegate;

        protected SimpleForwardingListenableFuture(ListenableFuture<V> r2) {
            this.delegate = (ListenableFuture) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.util.concurrent.ForwardingListenableFuture, com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
        protected final ListenableFuture<V> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.util.concurrent.ForwardingListenableFuture, com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.util.concurrent.ForwardingListenableFuture, com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Future delegate() {
            return delegate();
        }
    }

    protected ForwardingListenableFuture() {
    }

    @Override // com.google.common.util.concurrent.ListenableFuture
    public void addListener(Runnable r2, Executor r3) {
        delegate().addListener(r2, r3);
    }

    @Override // com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
    protected abstract ListenableFuture<? extends V> delegate();

    @Override // com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Future delegate() {
        return delegate();
    }
}
