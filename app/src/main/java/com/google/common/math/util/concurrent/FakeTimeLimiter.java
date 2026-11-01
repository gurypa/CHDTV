package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class FakeTimeLimiter implements TimeLimiter {
    public FakeTimeLimiter() {
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    @ParametricNullness
    public <T> T callUninterruptiblyWithTimeout(Callable<T> r2, long r3, TimeUnit r5) throws ExecutionException {
        return (T) callWithTimeout(r2, r3, r5);
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    @ParametricNullness
    public <T> T callWithTimeout(Callable<T> r3, long r4, TimeUnit r6) throws ExecutionException {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r6);
        return r3.call();
    L5:
        e = move-exception;
        throw new ExecutionError(e);
    L11:
        e = move-exception;
        throw new UncheckedExecutionException(e);
    L8:
        e = move-exception;
        Platform.restoreInterruptIfIsInterruptedException(e);
        throw new ExecutionException(e);
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    public <T> T newProxy(T r1, Class<T> r2, long r3, TimeUnit r5) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r5);
        return r1;
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    public void runUninterruptiblyWithTimeout(Runnable r1, long r2, TimeUnit r4) {
        runWithTimeout(r1, r2, r4);
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    public void runWithTimeout(Runnable r3, long r4, TimeUnit r6) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r6);
        r3.run();     // Catch: Error -> L5 Exception -> L8
        return;
    L5:
        e = move-exception;
        throw new ExecutionError(e);
    L8:
        e = move-exception;
        throw new UncheckedExecutionException(e);
    }
}
