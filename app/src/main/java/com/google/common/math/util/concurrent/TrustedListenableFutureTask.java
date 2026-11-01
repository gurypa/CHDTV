package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class TrustedListenableFutureTask<V> extends FluentFuture.TrustedFuture<V> implements RunnableFuture<V> {

    @CheckForNull
    private volatile InterruptibleTask<?> task;

    private final class TrustedFutureInterruptibleAsyncTask extends InterruptibleTask<ListenableFuture<V>> {
        private final AsyncCallable<V> callable;
        final /* synthetic */ TrustedListenableFutureTask this$0;

        TrustedFutureInterruptibleAsyncTask(TrustedListenableFutureTask r1, AsyncCallable<V> r2) {
            this.this$0 = r1;
            this.callable = (AsyncCallable) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        void afterRanInterruptiblyFailure(Throwable r2) {
            this.this$0.setException(r2);
        }

        void afterRanInterruptiblySuccess(ListenableFuture<V> r2) {
            this.this$0.setFuture(r2);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        /* bridge */ /* synthetic */ void afterRanInterruptiblySuccess(Object r1) {
            afterRanInterruptiblySuccess((ListenableFuture) r1);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        final boolean isDone() {
            return this.this$0.isDone();
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        ListenableFuture<V> runInterruptibly() throws Exception {
            return (ListenableFuture) Preconditions.checkNotNull(this.callable.call(), "AsyncCallable.call returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", this.callable);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        /* bridge */ /* synthetic */ Object runInterruptibly() throws Exception {
            return runInterruptibly();
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        String toPendingString() {
            return this.callable.toString();
        }
    }

    private final class TrustedFutureInterruptibleTask extends InterruptibleTask<V> {
        private final Callable<V> callable;
        final /* synthetic */ TrustedListenableFutureTask this$0;

        TrustedFutureInterruptibleTask(TrustedListenableFutureTask r1, Callable<V> r2) {
            this.this$0 = r1;
            this.callable = (Callable) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        void afterRanInterruptiblyFailure(Throwable r2) {
            this.this$0.setException(r2);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        void afterRanInterruptiblySuccess(@ParametricNullness V r2) {
            this.this$0.set(r2);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        final boolean isDone() {
            return this.this$0.isDone();
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        @ParametricNullness
        V runInterruptibly() throws Exception {
            return this.callable.call();
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        String toPendingString() {
            return this.callable.toString();
        }
    }

    TrustedListenableFutureTask(AsyncCallable<V> r2) {
        this.task = new TrustedFutureInterruptibleAsyncTask(this, r2);
    }

    TrustedListenableFutureTask(Callable<V> r2) {
        this.task = new TrustedFutureInterruptibleTask(this, r2);
    }

    static <V> TrustedListenableFutureTask<V> create(AsyncCallable<V> r1) {
        return new TrustedListenableFutureTask(r1);
    }

    static <V> TrustedListenableFutureTask<V> create(Runnable r2, @ParametricNullness V r3) {
        return new TrustedListenableFutureTask(Executors.callable(r2, r3));
    }

    static <V> TrustedListenableFutureTask<V> create(Callable<V> r1) {
        return new TrustedListenableFutureTask(r1);
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    protected void afterDone() {
        super.afterDone();
        if (wasInterrupted() == false) goto L7;
        InterruptibleTask<?> r0 = this.task;
        if (r0 == null) goto L7;
        r0.interruptTask();
    L7:
        this.task = null;
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    @CheckForNull
    protected String pendingToString() {
        InterruptibleTask<?> r0 = this.task;
        if (r0 == null) goto L7;
        return "task=[" + r0 + "]";
    L7:
        return super.pendingToString();
    }

    @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
    public void run() {
        InterruptibleTask<?> r0 = this.task;
        if (r0 == null) goto L5;
        r0.run();
    L5:
        this.task = null;
    }
}
