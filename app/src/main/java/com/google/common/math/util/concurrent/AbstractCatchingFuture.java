package com.google.common.util.concurrent;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import com.google.common.util.concurrent.internal.InternalFutures;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.lang.Throwable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractCatchingFuture<V, X extends Throwable, F, T> extends FluentFuture.TrustedFuture<V> implements Runnable {

    @CheckForNull
    @LazyInit
    Class<X> exceptionType;

    @CheckForNull
    @LazyInit
    F fallback;

    @CheckForNull
    @LazyInit
    ListenableFuture<? extends V> inputFuture;

    private static final class AsyncCatchingFuture<V, X extends Throwable> extends AbstractCatchingFuture<V, X, AsyncFunction<? super X, ? extends V>, ListenableFuture<? extends V>> {
        AsyncCatchingFuture(ListenableFuture<? extends V> r1, Class<X> r2, AsyncFunction<? super X, ? extends V> r3) {
            super(r1, r2, r3);
        }

        /* JADX WARN: Multi-variable type inference failed */
        ListenableFuture<? extends V> doFallback(AsyncFunction<? super X, ? extends V> r3, X r4) throws Exception {
            ListenableFuture<? extends V> r0 = r3.apply(r4);
            Preconditions.checkNotNull(r0, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", r3);
            return r0;
        }

        @Override // com.google.common.util.concurrent.AbstractCatchingFuture
        /* bridge */ /* synthetic */ Object doFallback(Object r1, Throwable r2) throws Exception {
            return doFallback((AsyncFunction) r1, r2);
        }

        void setResult(ListenableFuture<? extends V> r1) {
            setFuture(r1);
        }

        @Override // com.google.common.util.concurrent.AbstractCatchingFuture
        /* bridge */ /* synthetic */ void setResult(Object r1) {
            setResult((ListenableFuture) r1);
        }
    }

    private static final class CatchingFuture<V, X extends Throwable> extends AbstractCatchingFuture<V, X, Function<? super X, ? extends V>, V> {
        CatchingFuture(ListenableFuture<? extends V> r1, Class<X> r2, Function<? super X, ? extends V> r3) {
            super(r1, r2, r3);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @ParametricNullness
        V doFallback(Function<? super X, ? extends V> r2, X r3) throws Exception {
            return r2.apply(r3);
        }

        @Override // com.google.common.util.concurrent.AbstractCatchingFuture
        @ParametricNullness
        /* bridge */ /* synthetic */ Object doFallback(Object r1, Throwable r2) throws Exception {
            return doFallback((Function) r1, r2);
        }

        @Override // com.google.common.util.concurrent.AbstractCatchingFuture
        void setResult(@ParametricNullness V r1) {
            set(r1);
        }
    }

    AbstractCatchingFuture(ListenableFuture<? extends V> r2, Class<X> r3, F r4) {
        this.inputFuture = (ListenableFuture) Preconditions.checkNotNull(r2);
        this.exceptionType = (Class) Preconditions.checkNotNull(r3);
        this.fallback = (F) Preconditions.checkNotNull(r4);
    }

    static <V, X extends Throwable> ListenableFuture<V> create(ListenableFuture<? extends V> r2, Class<X> r3, Function<? super X, ? extends V> r4, Executor r5) {
        CatchingFuture r0 = new CatchingFuture(r2, r3, r4);
        r2.addListener(r0, MoreExecutors.rejectionPropagatingExecutor(r5, r0));
        return r0;
    }

    static <X extends Throwable, V> ListenableFuture<V> createAsync(ListenableFuture<? extends V> r2, Class<X> r3, AsyncFunction<? super X, ? extends V> r4, Executor r5) {
        AsyncCatchingFuture r0 = new AsyncCatchingFuture(r2, r3, r4);
        r2.addListener(r0, MoreExecutors.rejectionPropagatingExecutor(r5, r0));
        return r0;
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    protected final void afterDone() {
        maybePropagateCancellationTo(this.inputFuture);
        this.inputFuture = null;
        this.exceptionType = null;
        this.fallback = null;
    }

    @ParametricNullness
    abstract T doFallback(F r1, X r2) throws Exception;

    @Override // com.google.common.util.concurrent.AbstractFuture
    @CheckForNull
    protected String pendingToString() {
        ListenableFuture<? extends V> r0 = this.inputFuture;
        Class<X> r1 = this.exceptionType;
        F r2 = this.fallback;
        String r3 = super.pendingToString();
        String r4 = "";
        if (r0 == null) goto L5;
        r4 = "inputFuture=[" + r0 + "], ";
    L5:
        if (r1 == null) goto L9;
        if (r2 == null) goto L9;
        return r4 + "exceptionType=[" + r1 + "], fallback=[" + r2 + "]";
    L9:
        if (r3 != null) goto L11;
        return null;
    L11:
        return r4 + r3;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.Runnable
    public final void run() {
        ListenableFuture<? extends V> r0 = this.inputFuture;
        Class<X> r1 = this.exceptionType;
        F r2 = this.fallback;
        boolean r3 = true;
        if (r0 != 0) goto L5;
        boolean r5 = true;
    L6:
        if (r1 != null) goto L8;
        boolean r6 = true;
    L9:
        boolean r7 = r5 | r6;
        if (r2 == null) goto L14;
        r3 = false;
    L14:
        if ((r3 | r7) == false) goto L16;
        return;
    L16:
        if (isCancelled() == true) goto L57;
        this.inputFuture = null;
        Object r4 = null;
        Throwable r8 = null;
    L27:
        e = move-exception;
        r8 = e.getCause();
        if (r8 != null) goto L32;
        r8 = new NullPointerException("Future type " + r0.getClass() + " threw " + e.getClass() + " without a cause");
    L32:
        if (r8 != null) goto L36;
        set(NullnessCasts.uncheckedCastNullableTToT(r4));
        return;
    L36:
        if (Platform.isInstanceOfThrowableClass(r8, r1) == true) goto L53;
        setFuture(r0);
        return;
    L53:
        Object r9 = doFallback(r2, r8);     // Catch: Throwable -> L43
        this.exceptionType = null;
        this.fallback = null;
        setResult(r9);
        return;
    L43:
        th = move-exception;
        Platform.restoreInterruptIfIsInterruptedException(th);     // Catch: Throwable -> L47
        setException(th);     // Catch: Throwable -> L47
        this.exceptionType = null;
        this.fallback = null;
        return;
    L47:
        th = move-exception;
        this.exceptionType = null;
        this.fallback = null;
        throw th;
    L25:
        th = move-exception;
        r8 = th;
        goto L32
    L20:
        if ((r0 instanceof InternalFutureFailureAccess) == false) goto L22;
        r8 = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess) r0);     // Catch: Throwable -> L25 ExecutionException -> L27
    L22:
        if (r8 != null) goto L32;
        r4 = Futures.getDone(r0);     // Catch: Throwable -> L25 ExecutionException -> L27
        goto L32
    L57:
        return;
    L8:
        r6 = false;
        goto L9
    L5:
        r5 = false;
        goto L6
    }

    abstract void setResult(@ParametricNullness T r1);
}
