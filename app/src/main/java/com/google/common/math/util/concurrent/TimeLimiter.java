package com.google.common.util.concurrent;

import com.google.errorprone.annotations.DoNotMock;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use FakeTimeLimiter")
@ElementTypesAreNonnullByDefault
public interface TimeLimiter {
    @ParametricNullness
    <T> T callUninterruptiblyWithTimeout(Callable<T> r1, long r2, TimeUnit r4) throws TimeoutException, ExecutionException;

    @ParametricNullness
    <T> T callWithTimeout(Callable<T> r1, long r2, TimeUnit r4) throws TimeoutException, InterruptedException, ExecutionException;

    <T> T newProxy(T r1, Class<T> r2, long r3, TimeUnit r5);

    void runUninterruptiblyWithTimeout(Runnable r1, long r2, TimeUnit r4) throws TimeoutException;

    void runWithTimeout(Runnable r1, long r2, TimeUnit r4) throws TimeoutException, InterruptedException;
}
