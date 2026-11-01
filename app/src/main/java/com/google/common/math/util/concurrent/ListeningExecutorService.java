package com.google.common.util.concurrent;

import com.google.errorprone.annotations.DoNotMock;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use TestingExecutors.sameThreadScheduledExecutor, or wrap a real Executor from java.util.concurrent.Executors with MoreExecutors.listeningDecorator")
@ElementTypesAreNonnullByDefault
public interface ListeningExecutorService extends ExecutorService {

    /* JADX INFO: renamed from: com.google.common.util.concurrent.ListeningExecutorService$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ Future $default$submit(ListeningExecutorService r0, Runnable r1) {
            return r0.submit(r1);
        }

        public static /* bridge */ /* synthetic */ Future $default$submit(ListeningExecutorService r0, Runnable r1, @ParametricNullness Object r2) {
            return r0.submit(r1, r2);
        }

        public static /* bridge */ /* synthetic */ Future $default$submit(ListeningExecutorService r0, Callable r1) {
            return r0.submit(r1);
        }
    }

    @Override // java.util.concurrent.ExecutorService
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> r1) throws InterruptedException;

    @Override // java.util.concurrent.ExecutorService
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> r1, long r2, TimeUnit r4) throws InterruptedException;

    ListenableFuture<?> submit(Runnable r1);

    <T> ListenableFuture<T> submit(Runnable r1, @ParametricNullness T r2);

    <T> ListenableFuture<T> submit(Callable<T> r1);
}
