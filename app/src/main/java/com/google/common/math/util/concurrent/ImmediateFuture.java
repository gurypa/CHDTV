package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class ImmediateFuture<V> implements ListenableFuture<V> {
    static final ListenableFuture<?> NULL = null;
    private static final LazyLogger log = null;

    @ParametricNullness
    private final V value;

    static final class ImmediateCancelledFuture<V> extends AbstractFuture.TrustedFuture<V> {

        @CheckForNull
        static final ImmediateCancelledFuture<Object> INSTANCE = null;

        static {
            if (AbstractFuture.GENERATE_CANCELLATION_CAUSES == false) goto L5;
            ImmediateCancelledFuture<Object> r0 = null;
        L6:
            INSTANCE = r0;
            return;
        L5:
            r0 = new ImmediateCancelledFuture();
            goto L6
        }

        ImmediateCancelledFuture() {
            cancel(false);
        }
    }

    static final class ImmediateFailedFuture<V> extends AbstractFuture.TrustedFuture<V> {
        ImmediateFailedFuture(Throwable r1) {
            setException(r1);
        }
    }

    static {
        NULL = new ImmediateFuture(null);
        log = new LazyLogger(ImmediateFuture.class);
    }

    ImmediateFuture(@ParametricNullness V r1) {
        this.value = r1;
    }

    @Override // com.google.common.util.concurrent.ListenableFuture
    public void addListener(Runnable r6, Executor r7) {
        Preconditions.checkNotNull(r6, "Runnable was null.");
        Preconditions.checkNotNull(r7, "Executor was null.");
        r7.execute(r6);     // Catch: Exception -> L5
        return;
    L5:
        e = move-exception;
        log.get().log(Level.SEVERE, "RuntimeException while executing runnable " + r6 + " with executor " + r7, e);
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean r2) {
        return false;
    }

    @Override // java.util.concurrent.Future
    @ParametricNullness
    public V get() {
        return this.value;
    }

    @Override // java.util.concurrent.Future
    @ParametricNullness
    public V get(long r2, TimeUnit r4) throws ExecutionException {
        Preconditions.checkNotNull(r4);
        return get();
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return true;
    }

    public String toString() {
        return super.toString() + "[status=SUCCESS, result=[" + this.value + "]]";
    }
}
