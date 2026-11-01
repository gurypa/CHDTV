package com.google.common.util.concurrent;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.concurrent.Executor;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractTransformFuture<I, O, F, T> extends FluentFuture.TrustedFuture<O> implements Runnable {

    @CheckForNull
    @LazyInit
    F function;

    @CheckForNull
    @LazyInit
    ListenableFuture<? extends I> inputFuture;

    private static final class AsyncTransformFuture<I, O> extends AbstractTransformFuture<I, O, AsyncFunction<? super I, ? extends O>, ListenableFuture<? extends O>> {
        AsyncTransformFuture(ListenableFuture<? extends I> r1, AsyncFunction<? super I, ? extends O> r2) {
            super(r1, r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        ListenableFuture<? extends O> doTransform(AsyncFunction<? super I, ? extends O> r3, @ParametricNullness I r4) throws Exception {
            ListenableFuture<? extends O> r0 = r3.apply(r4);
            Preconditions.checkNotNull(r0, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", r3);
            return r0;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.util.concurrent.AbstractTransformFuture
        /* bridge */ /* synthetic */ Object doTransform(Object r1, @ParametricNullness Object r2) throws Exception {
            return doTransform((AsyncFunction) r1, r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        void setResult(ListenableFuture<? extends O> r1) {
            setFuture(r1);
        }

        @Override // com.google.common.util.concurrent.AbstractTransformFuture
        /* bridge */ /* synthetic */ void setResult(Object r1) {
            setResult((ListenableFuture) r1);
        }
    }

    private static final class TransformFuture<I, O> extends AbstractTransformFuture<I, O, Function<? super I, ? extends O>, O> {
        TransformFuture(ListenableFuture<? extends I> r1, Function<? super I, ? extends O> r2) {
            super(r1, r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @ParametricNullness
        O doTransform(Function<? super I, ? extends O> r2, @ParametricNullness I r3) {
            return r2.apply(r3);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.util.concurrent.AbstractTransformFuture
        @ParametricNullness
        /* bridge */ /* synthetic */ Object doTransform(Object r1, @ParametricNullness Object r2) throws Exception {
            return doTransform((Function) r1, r2);
        }

        @Override // com.google.common.util.concurrent.AbstractTransformFuture
        void setResult(@ParametricNullness O r1) {
            set(r1);
        }
    }

    AbstractTransformFuture(ListenableFuture<? extends I> r2, F r3) {
        this.inputFuture = (ListenableFuture) Preconditions.checkNotNull(r2);
        this.function = (F) Preconditions.checkNotNull(r3);
    }

    static <I, O> ListenableFuture<O> create(ListenableFuture<I> r2, Function<? super I, ? extends O> r3, Executor r4) {
        Preconditions.checkNotNull(r3);
        TransformFuture r0 = new TransformFuture(r2, r3);
        r2.addListener(r0, MoreExecutors.rejectionPropagatingExecutor(r4, r0));
        return r0;
    }

    static <I, O> ListenableFuture<O> createAsync(ListenableFuture<I> r2, AsyncFunction<? super I, ? extends O> r3, Executor r4) {
        Preconditions.checkNotNull(r4);
        AsyncTransformFuture r0 = new AsyncTransformFuture(r2, r3);
        r2.addListener(r0, MoreExecutors.rejectionPropagatingExecutor(r4, r0));
        return r0;
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    protected final void afterDone() {
        maybePropagateCancellationTo(this.inputFuture);
        this.inputFuture = null;
        this.function = null;
    }

    @ParametricNullness
    abstract T doTransform(F r1, @ParametricNullness I r2) throws Exception;

    @Override // com.google.common.util.concurrent.AbstractFuture
    @CheckForNull
    protected String pendingToString() {
        ListenableFuture<? extends I> r0 = this.inputFuture;
        F r1 = this.function;
        String r2 = super.pendingToString();
        String r3 = "";
        if (r0 == null) goto L5;
        r3 = "inputFuture=[" + r0 + "], ";
    L5:
        if (r1 != null) goto L7;
        if (r2 != null) goto L10;
        return null;
    L10:
        return r3 + r2;
    L7:
        return r3 + "function=[" + r1 + "]";
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.Runnable
    public final void run() {
        ListenableFuture<? extends I> r0 = this.inputFuture;
        F r1 = this.function;
        boolean r2 = isCancelled();
        boolean r3 = true;
        if (r0 != null) goto L5;
        boolean r5 = true;
    L6:
        boolean r4 = r2 | r5;
        if (r1 == null) goto L11;
        r3 = false;
    L11:
        if ((r4 | r3) == false) goto L13;
        return;
    L13:
        this.inputFuture = null;
        if (r0.isCancelled() == false) goto L44;
        setFuture(r0);
        return;
    L44:
        Object r6 = doTransform(r1, Futures.getDone(r0));     // Catch: Throwable -> L21
        this.function = null;
        setResult(r6);
        return;
    L21:
        th = move-exception;
        Platform.restoreInterruptIfIsInterruptedException(th);     // Catch: Throwable -> L25
        setException(th);     // Catch: Throwable -> L25
        this.function = null;
        return;
    L25:
        th = move-exception;
        this.function = null;
        throw th;
    L28:
        e = move-exception;
        setException(e);
        return;
    L34:
        e = move-exception;
        setException(e.getCause());
        return;
    L31:
        e = move-exception;
        setException(e);
        return;
    L38:
        cancel(false);
        return;
    L5:
        r5 = false;
        goto L6
    }

    abstract void setResult(@ParametricNullness T r1);
}
