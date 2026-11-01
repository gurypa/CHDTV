package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class JdkFutureAdapters {

    private static class ListenableFutureAdapter<V> extends ForwardingFuture<V> implements ListenableFuture<V> {
        private static final Executor defaultAdapterExecutor = null;
        private static final ThreadFactory threadFactory = null;
        private final Executor adapterExecutor;
        private final Future<V> delegate;
        private final ExecutionList executionList;
        private final AtomicBoolean hasListeners;

        static {
            threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ListenableFutureAdapter-thread-%d").build();
            defaultAdapterExecutor = Executors.newCachedThreadPool(threadFactory);
        }

        ListenableFutureAdapter(Future<V> r2) {
            this(r2, defaultAdapterExecutor);
        }

        ListenableFutureAdapter(Future<V> r3, Executor r4) {
            this.executionList = new ExecutionList();
            this.hasListeners = new AtomicBoolean(false);
            this.delegate = (Future) Preconditions.checkNotNull(r3);
            this.adapterExecutor = (Executor) Preconditions.checkNotNull(r4);
        }

        @Override // com.google.common.util.concurrent.ListenableFuture
        public void addListener(Runnable r4, Executor r5) {
            this.executionList.add(r4, r5);
            if (this.hasListeners.compareAndSet(false, true) == true) goto L5;
            return;
        L5:
            if (this.delegate.isDone() == false) goto L8;
            this.executionList.execute();
            return;
        L8:
            this.adapterExecutor.execute(new RunnableC0948x8da53ce(this));
        }

        @Override // com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
        protected Future<V> delegate() {
            return this.delegate;
        }

        /* JADX INFO: renamed from: lambda$addListener$0$com-google-common-util-concurrent-JdkFutureAdapters$ListenableFutureAdapter */
        /* synthetic */ void m387x6e801c7a() {
            Uninterruptibles.getUninterruptibly(this.delegate);     // Catch: Throwable -> L4
        L5:
            this.executionList.execute();
        }
    }

    private JdkFutureAdapters() {
    }

    public static <V> ListenableFuture<V> listenInPoolThread(Future<V> r1) {
        if ((r1 instanceof ListenableFuture) == false) goto L7;
        return (ListenableFuture) r1;
    L7:
        return new ListenableFutureAdapter(r1);
    }

    public static <V> ListenableFuture<V> listenInPoolThread(Future<V> r1, Executor r2) {
        Preconditions.checkNotNull(r2);
        if ((r1 instanceof ListenableFuture) == false) goto L7;
        return (ListenableFuture) r1;
    L7:
        return new ListenableFutureAdapter(r1, r2);
    }
}
