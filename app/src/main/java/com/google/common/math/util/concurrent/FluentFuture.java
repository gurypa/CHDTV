package com.google.common.util.concurrent;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.DoNotMock;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use FluentFuture.from(Futures.immediate*Future) or SettableFuture")
@ElementTypesAreNonnullByDefault
public abstract class FluentFuture<V> extends GwtFluentFutureCatchingSpecialization<V> {

    static abstract class TrustedFuture<V> extends FluentFuture<V> implements AbstractFuture.Trusted<V> {
        TrustedFuture() {
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, com.google.common.util.concurrent.ListenableFuture
        public final void addListener(Runnable r1, Executor r2) {
            super.addListener(r1, r2);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean cancel(boolean r2) {
            return super.cancel(r2);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        @ParametricNullness
        public final V get() throws InterruptedException, ExecutionException {
            return (V) super.get();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        @ParametricNullness
        public final V get(long r2, TimeUnit r4) throws InterruptedException, ExecutionException, TimeoutException {
            return (V) super.get(r2, r4);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isCancelled() {
            return super.isCancelled();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isDone() {
            return super.isDone();
        }
    }

    FluentFuture() {
    }

    @Deprecated
    public static <V> FluentFuture<V> from(FluentFuture<V> r1) {
        return (FluentFuture) Preconditions.checkNotNull(r1);
    }

    public static <V> FluentFuture<V> from(ListenableFuture<V> r1) {
        if ((r1 instanceof FluentFuture) == false) goto L6;
        return (FluentFuture) r1;
    L6:
        return new ForwardingFluentFuture(r1);
    }

    public final void addCallback(FutureCallback<? super V> r1, Executor r2) {
        Futures.addCallback(this, r1, r2);
    }

    public final <X extends Throwable> FluentFuture<V> catching(Class<X> r2, Function<? super X, ? extends V> r3, Executor r4) {
        return (FluentFuture) Futures.catching(this, r2, r3, r4);
    }

    public final <X extends Throwable> FluentFuture<V> catchingAsync(Class<X> r2, AsyncFunction<? super X, ? extends V> r3, Executor r4) {
        return (FluentFuture) Futures.catchingAsync(this, r2, r3, r4);
    }

    public final <T> FluentFuture<T> transform(Function<? super V, T> r2, Executor r3) {
        return (FluentFuture) Futures.transform(this, r2, r3);
    }

    public final <T> FluentFuture<T> transformAsync(AsyncFunction<? super V, T> r2, Executor r3) {
        return (FluentFuture) Futures.transformAsync(this, r2, r3);
    }

    public final FluentFuture<V> withTimeout(long r2, TimeUnit r4, ScheduledExecutorService r5) {
        return (FluentFuture) Futures.withTimeout(this, r2, r4, r5);
    }
}
