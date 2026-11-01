package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class MoreExecutors {

    static class Application {
        Application() {
        }

        final void addDelayedShutdownHook(final ExecutorService r8, final long r9, final TimeUnit r11) {
            Preconditions.checkNotNull(r8);
            Preconditions.checkNotNull(r11);
            addShutdownHook(MoreExecutors.newThread("DelayedShutdownHook-for-" + r8, new RunnableC09541(this, r8, r9, r11)));
        }

        void addShutdownHook(Thread r2) {
            Runtime.getRuntime().addShutdownHook(r2);
        }

        final ExecutorService getExitingExecutorService(ThreadPoolExecutor r4) {
            return getExitingExecutorService(r4, 120, TimeUnit.SECONDS);
        }

        final ExecutorService getExitingExecutorService(ThreadPoolExecutor r2, long r3, TimeUnit r5) {
            MoreExecutors.access$000(r2);
            ExecutorService r0 = Executors.unconfigurableExecutorService(r2);
            addDelayedShutdownHook(r2, r3, r5);
            return r0;
        }

        final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor r4) {
            return getExitingScheduledExecutorService(r4, 120, TimeUnit.SECONDS);
        }

        final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor r2, long r3, TimeUnit r5) {
            MoreExecutors.access$000(r2);
            ScheduledExecutorService r0 = Executors.unconfigurableScheduledExecutorService(r2);
            addDelayedShutdownHook(r2, r3, r5);
            return r0;
        }
    }

    private static class ListeningDecorator extends AbstractListeningExecutorService {
        private final ExecutorService delegate;

        ListeningDecorator(ExecutorService r2) {
            this.delegate = (ExecutorService) Preconditions.checkNotNull(r2);
        }

        @Override // java.util.concurrent.ExecutorService
        public final boolean awaitTermination(long r2, TimeUnit r4) throws InterruptedException {
            return this.delegate.awaitTermination(r2, r4);
        }

        @Override // java.util.concurrent.Executor
        public final void execute(Runnable r2) {
            this.delegate.execute(r2);
        }

        @Override // java.util.concurrent.ExecutorService
        public final boolean isShutdown() {
            return this.delegate.isShutdown();
        }

        @Override // java.util.concurrent.ExecutorService
        public final boolean isTerminated() {
            return this.delegate.isTerminated();
        }

        @Override // java.util.concurrent.ExecutorService
        public final void shutdown() {
            this.delegate.shutdown();
        }

        @Override // java.util.concurrent.ExecutorService
        public final List<Runnable> shutdownNow() {
            return this.delegate.shutdownNow();
        }

        public final String toString() {
            return super.toString() + "[" + this.delegate + "]";
        }
    }

    private static final class ScheduledListeningDecorator extends ListeningDecorator implements ListeningScheduledExecutorService {
        final ScheduledExecutorService delegate;

        private static final class ListenableScheduledTask<V> extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V> implements ListenableScheduledFuture<V> {
            private final ScheduledFuture<?> scheduledDelegate;

            public ListenableScheduledTask(ListenableFuture<V> r1, ScheduledFuture<?> r2) {
                super(r1);
                this.scheduledDelegate = r2;
            }

            @Override // com.google.common.util.concurrent.ForwardingFuture, java.util.concurrent.Future
            public boolean cancel(boolean r3) {
                boolean r0 = super.cancel(r3);
                if (r0 == false) goto L5;
                this.scheduledDelegate.cancel(r3);
            L5:
                return r0;
            }

            @Override // java.lang.Comparable
            public /* bridge */ /* synthetic */ int compareTo(Delayed r1) {
                return compareTo2(r1);
            }

            /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
            public int compareTo2(Delayed r2) {
                return this.scheduledDelegate.compareTo(r2);
            }

            @Override // java.util.concurrent.Delayed
            public long getDelay(TimeUnit r3) {
                return this.scheduledDelegate.getDelay(r3);
            }
        }

        private static final class NeverSuccessfulListenableFutureTask extends AbstractFuture.TrustedFuture<Void> implements Runnable {
            private final Runnable delegate;

            public NeverSuccessfulListenableFutureTask(Runnable r2) {
                this.delegate = (Runnable) Preconditions.checkNotNull(r2);
            }

            @Override // com.google.common.util.concurrent.AbstractFuture
            protected String pendingToString() {
                return "task=[" + this.delegate + "]";
            }

            @Override // java.lang.Runnable
            public void run() {
                this.delegate.run();     // Catch: Throwable -> L4
                return;
            L4:
                th = move-exception;
                setException(th);
                throw th;
            }
        }

        ScheduledListeningDecorator(ScheduledExecutorService r2) {
            super(r2);
            this.delegate = (ScheduledExecutorService) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.util.concurrent.ListeningScheduledExecutorService, java.util.concurrent.ScheduledExecutorService
        public ListenableScheduledFuture<?> schedule(Runnable r4, long r5, TimeUnit r7) {
            TrustedListenableFutureTask r0 = TrustedListenableFutureTask.create(r4, null);
            return new ListenableScheduledTask(r0, this.delegate.schedule(r0, r5, r7));
        }

        @Override // com.google.common.util.concurrent.ListeningScheduledExecutorService, java.util.concurrent.ScheduledExecutorService
        public <V> ListenableScheduledFuture<V> schedule(Callable<V> r4, long r5, TimeUnit r7) {
            TrustedListenableFutureTask r0 = TrustedListenableFutureTask.create(r4);
            return new ListenableScheduledTask(r0, this.delegate.schedule(r0, r5, r7));
        }

        @Override // java.util.concurrent.ScheduledExecutorService
        public /* bridge */ /* synthetic */ ScheduledFuture schedule(Runnable r1, long r2, TimeUnit r4) {
            return schedule(r1, r2, r4);
        }

        @Override // java.util.concurrent.ScheduledExecutorService
        public /* bridge */ /* synthetic */ ScheduledFuture schedule(Callable r1, long r2, TimeUnit r4) {
            return schedule(r1, r2, r4);
        }

        @Override // com.google.common.util.concurrent.ListeningScheduledExecutorService, java.util.concurrent.ScheduledExecutorService
        public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable r9, long r10, long r12, TimeUnit r14) {
            NeverSuccessfulListenableFutureTask r0 = new NeverSuccessfulListenableFutureTask(r9);
            return new ListenableScheduledTask(r0, this.delegate.scheduleAtFixedRate(r0, r10, r12, r14));
        }

        @Override // java.util.concurrent.ScheduledExecutorService
        public /* bridge */ /* synthetic */ ScheduledFuture scheduleAtFixedRate(Runnable r1, long r2, long r4, TimeUnit r6) {
            return scheduleAtFixedRate(r1, r2, r4, r6);
        }

        @Override // com.google.common.util.concurrent.ListeningScheduledExecutorService, java.util.concurrent.ScheduledExecutorService
        public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable r9, long r10, long r12, TimeUnit r14) {
            NeverSuccessfulListenableFutureTask r0 = new NeverSuccessfulListenableFutureTask(r9);
            return new ListenableScheduledTask(r0, this.delegate.scheduleWithFixedDelay(r0, r10, r12, r14));
        }

        @Override // java.util.concurrent.ScheduledExecutorService
        public /* bridge */ /* synthetic */ ScheduledFuture scheduleWithFixedDelay(Runnable r1, long r2, long r4, TimeUnit r6) {
            return scheduleWithFixedDelay(r1, r2, r4, r6);
        }
    }

    private MoreExecutors() {
    }

    static /* synthetic */ void access$000(ThreadPoolExecutor r0) {
        useDaemonThreadFactory(r0);
    }

    public static void addDelayedShutdownHook(ExecutorService r1, long r2, TimeUnit r4) {
        new Application().addDelayedShutdownHook(r1, r2, r4);
    }

    public static Executor directExecutor() {
        return DirectExecutor.INSTANCE;
    }

    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor r1) {
        return new Application().getExitingExecutorService(r1);
    }

    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor r1, long r2, TimeUnit r4) {
        return new Application().getExitingExecutorService(r1, r2, r4);
    }

    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor r1) {
        return new Application().getExitingScheduledExecutorService(r1);
    }

    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor r1, long r2, TimeUnit r4) {
        return new Application().getExitingScheduledExecutorService(r1, r2, r4);
    }

    @ParametricNullness
    static <T> T invokeAnyImpl(ListeningExecutorService r22, Collection<? extends Callable<T>> r23, boolean r24, long r25, TimeUnit r27) throws InterruptedException, ExecutionException, TimeoutException {
        ListeningExecutorService r1 = r22;
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r27);
        int r2 = r23.size();
        if (r2 <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        ArrayList r4 = Lists.newArrayListWithCapacity(r2);
        LinkedBlockingQueue r5 = Queues.newLinkedBlockingQueue();
        long r9 = r27.toNanos(r25);
        if (r24 == true) goto L55;
        long r11 = 0;
    L12:
        Iterator<? extends Callable<T>> r13 = r23.iterator();     // Catch: Throwable -> L9
        r4.add(submitAndAddQueueListener(r1, r13.next(), r5));     // Catch: Throwable -> L9
        int r3 = r2 - 1;
        ExecutionException r6 = null;
        int r7 = 1;
        long r14 = r11;
        long r10 = r9;
        int r12 = r3;
    L54:
        Future r15 = (Future) r5.poll();     // Catch: Throwable -> L47
        if (r15 != null) goto L31;
        if (r12 <= 0) goto L18;
        r12 = r12 - 1;     // Catch: Throwable -> L47
        r4.add(submitAndAddQueueListener(r1, r13.next(), r5));     // Catch: Throwable -> L47
        r7 = r7 + 1;     // Catch: Throwable -> L47
        goto L31
    L18:
        if (r7 == 0) goto L19;
        if (r24 == false) goto L30;
        r15 = (Future) r5.poll(r10, TimeUnit.NANOSECONDS);     // Catch: Throwable -> L47
        if (r15 == null) goto L28;
        long r17 = System.nanoTime();     // Catch: Throwable -> L47
        r10 = r10 - (r17 - r14);     // Catch: Throwable -> L47
        r14 = r17;
        goto L31
    L28:
        throw new TimeoutException();     // Catch: Throwable -> L47
    L30:
        r15 = (Future) r5.take();     // Catch: Throwable -> L47
        goto L31
    L19:
        if (r6 == null) goto L22;
        throw r6;     // Catch: Throwable -> L47
    L22:
        throw new ExecutionException(null);     // Catch: Throwable -> L47
    L31:
        if (r15 == null) goto L46;
        int r8 = r7 - 1;
        Object r16 = r15.get();     // Catch: Exception -> L40 InterruptedException -> L42 ExecutionException -> L44 Throwable -> L47
    L34:
        Iterator r18 = r4.iterator();
    L36:
        if (r18.hasNext() == false) goto L39;
        ((Future) r18.next()).cancel(true);
        r16 = r16;
        goto L36
    L39:
        return (T) r16;
    L42:
        e = move-exception;
        throw e;     // Catch: Throwable -> L47
    L44:
        e = move-exception;
        r6 = e;
        r7 = r8;
    L40:
        e = move-exception;
        r6 = new ExecutionException(e);     // Catch: Throwable -> L47
        r7 = r8;
    L46:
        r1 = r22;
    L47:
        th = th;
    L49:
        Iterator r19 = r4.iterator();
    L51:
        if (r19.hasNext() == false) goto L53;
        ((Future) r19.next()).cancel(true);
        goto L51
    L53:
        throw th;
    L9:
        th = th;
        goto L49
    L55:
        r11 = System.nanoTime();     // Catch: Throwable -> L9
        goto L12
    L5:
        r0 = false;
        goto L6
    }

    private static boolean isAppEngineWithApiClasses() {
        if (System.getProperty("com.google.appengine.runtime.environment") != null) goto L20;
        return false;
    L20:
        Class.forName("com.google.appengine.api.utils.SystemProperty");     // Catch: ClassNotFoundException -> L18
        if (Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", new Class[0]).invoke(null, new Object[0]) == null) goto L24;
        return true;
    L24:
        return false;
    L17:
        return false;
    L13:
        return false;
    L11:
        return false;
    L15:
        return false;
    L19:
        return false;
    }

    public static ListeningExecutorService listeningDecorator(ExecutorService r2) {
        if ((r2 instanceof ListeningExecutorService) == false) goto L6;
        return (ListeningExecutorService) r2;
    L6:
        if ((r2 instanceof ScheduledExecutorService) == false) goto L9;
        return new ScheduledListeningDecorator((ScheduledExecutorService) r2);
    L9:
        return new ListeningDecorator(r2);
    }

    public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService r1) {
        if ((r1 instanceof ListeningScheduledExecutorService) == false) goto L6;
        return (ListeningScheduledExecutorService) r1;
    L6:
        return new ScheduledListeningDecorator(r1);
    }

    public static ListeningExecutorService newDirectExecutorService() {
        return new DirectExecutorService();
    }

    public static Executor newSequentialExecutor(Executor r1) {
        return new SequentialExecutor(r1);
    }

    static Thread newThread(String r2, Runnable r3) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        Thread r0 = (Thread) Objects.requireNonNull(platformThreadFactory().newThread(r3));
        r0.setName(r2);     // Catch: SecurityException -> L5
    L6:
        return r0;
    }

    public static ThreadFactory platformThreadFactory() {
        if (isAppEngineWithApiClasses() == false) goto L5;
        return (ThreadFactory) Class.forName("com.google.appengine.api.ThreadManager").getMethod("currentRequestThreadFactory", new Class[0]).invoke(null, new Object[0]);
    L11:
        e = move-exception;
        throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
    L8:
        e = move-exception;
        throw Throwables.propagate(e.getCause());
    L5:
        return Executors.defaultThreadFactory();
    }

    static Executor rejectionPropagatingExecutor(final Executor r1, final AbstractFuture<?> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        if (r1 != directExecutor()) goto L6;
        return r1;
    L6:
        return new ExecutorC09535(r1, r2);
    }

    static Executor renamingDecorator(final Executor r1, final Supplier<String> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new ExecutorC09502(r1, r2);
    }

    static ExecutorService renamingDecorator(final ExecutorService r1, final Supplier<String> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new C09513(r1, r2);
    }

    static ScheduledExecutorService renamingDecorator(final ScheduledExecutorService r1, final Supplier<String> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new C09524(r1, r2);
    }

    public static boolean shutdownAndAwaitTermination(ExecutorService r4, long r5, TimeUnit r7) {
        long r0 = r7.toNanos(r5) / 2;
        r4.shutdown();
        if (r4.awaitTermination(r0, TimeUnit.NANOSECONDS) == true) goto L10;
        r4.shutdownNow();     // Catch: InterruptedException -> L7
        r4.awaitTermination(r0, TimeUnit.NANOSECONDS);     // Catch: InterruptedException -> L7
    L10:
        return r4.isTerminated();
    L8:
        Thread.currentThread().interrupt();
        r4.shutdownNow();
        goto L10
    }

    private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService r3, Callable<T> r4, final BlockingQueue<Future<T>> r5) {
        final ListenableFuture<T> r0 = r3.submit(r4);
        r0.addListener(new RunnableC09491(r5, r0), directExecutor());
        return r0;
    }

    private static void useDaemonThreadFactory(ThreadPoolExecutor r2) {
        r2.setThreadFactory(new ThreadFactoryBuilder().setDaemon(true).setThreadFactory(r2.getThreadFactory()).build());
    }
}
