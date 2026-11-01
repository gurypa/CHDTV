package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class CombinedFuture<V> extends AggregateFuture<Object, V> {

    @CheckForNull
    @LazyInit
    private CombinedFuture<V>.CombinedFutureInterruptibleTask<?> task;

    private final class AsyncCallableInterruptibleTask extends CombinedFuture<V>.CombinedFutureInterruptibleTask<ListenableFuture<V>> {
        private final AsyncCallable<V> callable;
        final /* synthetic */ CombinedFuture this$0;

        AsyncCallableInterruptibleTask(CombinedFuture r1, AsyncCallable<V> r2, Executor r3) {
            this.this$0 = r1;
            super(r1, r3);
            this.callable = (AsyncCallable) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        ListenableFuture<V> runInterruptibly() throws Exception {
            return (ListenableFuture) Preconditions.checkNotNull(this.callable.call(), "AsyncCallable.call returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", this.callable);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        /* bridge */ /* synthetic */ Object runInterruptibly() throws Exception {
            return runInterruptibly();
        }

        void setValue(ListenableFuture<V> r2) {
            this.this$0.setFuture(r2);
        }

        @Override // com.google.common.util.concurrent.CombinedFuture.CombinedFutureInterruptibleTask
        /* bridge */ /* synthetic */ void setValue(Object r1) {
            setValue((ListenableFuture) r1);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        String toPendingString() {
            return this.callable.toString();
        }
    }

    private final class CallableInterruptibleTask extends CombinedFuture<V>.CombinedFutureInterruptibleTask<V> {
        private final Callable<V> callable;
        final /* synthetic */ CombinedFuture this$0;

        CallableInterruptibleTask(CombinedFuture r1, Callable<V> r2, Executor r3) {
            this.this$0 = r1;
            super(r1, r3);
            this.callable = (Callable) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        @ParametricNullness
        V runInterruptibly() throws Exception {
            return this.callable.call();
        }

        @Override // com.google.common.util.concurrent.CombinedFuture.CombinedFutureInterruptibleTask
        void setValue(@ParametricNullness V r2) {
            this.this$0.set(r2);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        String toPendingString() {
            return this.callable.toString();
        }
    }

    private abstract class CombinedFutureInterruptibleTask<T> extends InterruptibleTask<T> {
        private final Executor listenerExecutor;
        final /* synthetic */ CombinedFuture this$0;

        CombinedFutureInterruptibleTask(CombinedFuture r1, Executor r2) {
            this.this$0 = r1;
            this.listenerExecutor = (Executor) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        final void afterRanInterruptiblyFailure(Throwable r3) {
            CombinedFuture.access$002(this.this$0, null);
            if ((r3 instanceof ExecutionException) == false) goto L6;
            this.this$0.setException(((ExecutionException) r3).getCause());
            return;
        L6:
            if ((r3 instanceof CancellationException) == false) goto L8;
            this.this$0.cancel(false);
            return;
        L8:
            this.this$0.setException(r3);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        final void afterRanInterruptiblySuccess(@ParametricNullness T r3) {
            CombinedFuture.access$002(this.this$0, null);
            setValue(r3);
        }

        final void execute() {
            this.listenerExecutor.execute(this);     // Catch: RejectedExecutionException -> L4
            return;
        L4:
            e = move-exception;
            this.this$0.setException(e);
        }

        @Override // com.google.common.util.concurrent.InterruptibleTask
        final boolean isDone() {
            return this.this$0.isDone();
        }

        abstract void setValue(@ParametricNullness T r1);
    }

    CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> r2, boolean r3, Executor r4, AsyncCallable<V> r5) {
        super(r2, r3, false);
        this.task = new AsyncCallableInterruptibleTask(this, r5, r4);
        init();
    }

    CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> r2, boolean r3, Executor r4, Callable<V> r5) {
        super(r2, r3, false);
        this.task = new CallableInterruptibleTask(this, r5, r4);
        init();
    }

    static /* synthetic */ CombinedFutureInterruptibleTask access$002(CombinedFuture r0, CombinedFutureInterruptibleTask r1) {
        r0.task = r1;
        return r1;
    }

    @Override // com.google.common.util.concurrent.AggregateFuture
    void collectOneValue(int r1, @CheckForNull Object r2) {
    }

    @Override // com.google.common.util.concurrent.AggregateFuture
    void handleAllCompleted() {
        CombinedFuture<V>.CombinedFutureInterruptibleTask<?> r0 = this.task;
        if (r0 == null) goto L6;
        r0.execute();
        return;
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    protected void interruptTask() {
        CombinedFuture<V>.CombinedFutureInterruptibleTask<?> r0 = this.task;
        if (r0 == null) goto L6;
        r0.interruptTask();
        return;
    }

    @Override // com.google.common.util.concurrent.AggregateFuture
    void releaseResources(AggregateFuture.ReleaseResourcesReason r2) {
        super.releaseResources(r2);
        if (r2 != AggregateFuture.ReleaseResourcesReason.OUTPUT_FUTURE_DONE) goto L6;
        this.task = null;
        return;
    }
}
