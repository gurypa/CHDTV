package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class AbstractScheduledService implements Service {
    private static final LazyLogger logger = null;
    private final AbstractService delegate;

    interface Cancellable {
        void cancel(boolean r1);

        boolean isCancelled();
    }

    public static abstract class CustomScheduler extends Scheduler {

        private final class ReschedulableCallable implements Callable<Void> {

            @CheckForNull
            private SupplantableFuture cancellationDelegate;
            private final ScheduledExecutorService executor;
            private final ReentrantLock lock;
            private final AbstractService service;
            final /* synthetic */ CustomScheduler this$0;
            private final Runnable wrappedRunnable;

            ReschedulableCallable(CustomScheduler r1, AbstractService r2, ScheduledExecutorService r3, Runnable r4) {
                this.this$0 = r1;
                this.lock = new ReentrantLock();
                this.wrappedRunnable = r4;
                this.executor = r3;
                this.service = r2;
            }

            private Cancellable initializeOrUpdateCancellationDelegate(Schedule r4) {
                if (this.cancellationDelegate != null) goto L7;
                SupplantableFuture r0 = new SupplantableFuture(this.lock, submitToExecutor(r4));
                this.cancellationDelegate = r0;
                return r0;
            L7:
                if (SupplantableFuture.access$600(this.cancellationDelegate).isCancelled() == true) goto L10;
                SupplantableFuture.access$602(this.cancellationDelegate, submitToExecutor(r4));
            L10:
                return this.cancellationDelegate;
            }

            private ScheduledFuture<Void> submitToExecutor(Schedule r5) {
                return this.executor.schedule(this, Schedule.access$700(r5), Schedule.access$800(r5));
            }

            @Override // java.util.concurrent.Callable
            @CheckForNull
            public /* bridge */ /* synthetic */ Void call() throws Exception {
                return call2();
            }

            @Override // java.util.concurrent.Callable
            @CheckForNull
            /* JADX INFO: renamed from: call, reason: avoid collision after fix types in other method */
            public Void call2() throws Exception {
                this.wrappedRunnable.run();
                reschedule();
                return null;
            }

            public Cancellable reschedule() {
                Schedule r0 = this.this$0.getNextSchedule();     // Catch: Throwable -> L16
                Throwable r1 = null;
                this.lock.lock();
                Cancellable r2 = initializeOrUpdateCancellationDelegate(r0);     // Catch: Throwable -> L6
            L5:
                this.lock.unlock();
                if (r1 == null) goto L12;
                this.service.notifyFailed(r1);
            L12:
                return r2;
            L6:
                th = move-exception;
                r1 = th;
                r2 = new FutureAsCancellable(Futures.immediateCancelledFuture());     // Catch: Throwable -> L13
            L13:
                th = move-exception;
                this.lock.unlock();
                throw th;
            L16:
                th = move-exception;
                Platform.restoreInterruptIfIsInterruptedException(th);
                this.service.notifyFailed(th);
                return new FutureAsCancellable(Futures.immediateCancelledFuture());
            }
        }

        protected static final class Schedule {
            private final long delay;
            private final TimeUnit unit;

            public Schedule(long r2, TimeUnit r4) {
                this.delay = r2;
                this.unit = (TimeUnit) Preconditions.checkNotNull(r4);
            }

            static /* synthetic */ long access$700(Schedule r2) {
                return r2.delay;
            }

            static /* synthetic */ TimeUnit access$800(Schedule r1) {
                return r1.unit;
            }
        }

        private static final class SupplantableFuture implements Cancellable {
            private Future<Void> currentFuture;
            private final ReentrantLock lock;

            SupplantableFuture(ReentrantLock r1, Future<Void> r2) {
                this.lock = r1;
                this.currentFuture = r2;
            }

            static /* synthetic */ Future access$600(SupplantableFuture r1) {
                return r1.currentFuture;
            }

            static /* synthetic */ Future access$602(SupplantableFuture r0, Future r1) {
                r0.currentFuture = r1;
                return r1;
            }

            @Override // com.google.common.util.concurrent.AbstractScheduledService.Cancellable
            public void cancel(boolean r3) {
                this.lock.lock();
                this.currentFuture.cancel(r3);     // Catch: Throwable -> L6
                this.lock.unlock();
                return;
            L6:
                th = move-exception;
                this.lock.unlock();
                throw th;
            }

            @Override // com.google.common.util.concurrent.AbstractScheduledService.Cancellable
            public boolean isCancelled() {
                this.lock.lock();
                boolean r0 = this.currentFuture.isCancelled();     // Catch: Throwable -> L6
                this.lock.unlock();
                return r0;
            L6:
                th = move-exception;
                this.lock.unlock();
                throw th;
            }
        }

        public CustomScheduler() {
            super(null);
        }

        protected abstract Schedule getNextSchedule() throws Exception;

        @Override // com.google.common.util.concurrent.AbstractScheduledService.Scheduler
        final Cancellable schedule(AbstractService r2, ScheduledExecutorService r3, Runnable r4) {
            return new ReschedulableCallable(this, r2, r3, r4).reschedule();
        }
    }

    private static final class FutureAsCancellable implements Cancellable {
        private final Future<?> delegate;

        FutureAsCancellable(Future<?> r1) {
            this.delegate = r1;
        }

        @Override // com.google.common.util.concurrent.AbstractScheduledService.Cancellable
        public void cancel(boolean r2) {
            this.delegate.cancel(r2);
        }

        @Override // com.google.common.util.concurrent.AbstractScheduledService.Cancellable
        public boolean isCancelled() {
            return this.delegate.isCancelled();
        }
    }

    public static abstract class Scheduler {
        private Scheduler() {
        }

        /* synthetic */ Scheduler(C09051 r1) {
            this();
        }

        public static Scheduler newFixedDelaySchedule(final long r8, final long r10, final TimeUnit r12) {
            Preconditions.checkNotNull(r12);
            if (r10 <= 0) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "delay must be > 0, found %s", r10);
            return new C09061(r8, r10, r12);
        L5:
            r0 = false;
            goto L6
        }

        public static Scheduler newFixedRateSchedule(final long r8, final long r10, final TimeUnit r12) {
            Preconditions.checkNotNull(r12);
            if (r10 <= 0) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "period must be > 0, found %s", r10);
            return new C09072(r8, r10, r12);
        L5:
            r0 = false;
            goto L6
        }

        abstract Cancellable schedule(AbstractService r1, ScheduledExecutorService r2, Runnable r3);
    }

    private final class ServiceDelegate extends AbstractService {

        @CheckForNull
        private volatile ScheduledExecutorService executorService;
        private final ReentrantLock lock;

        @CheckForNull
        private volatile Cancellable runningTask;
        private final Runnable task;
        final /* synthetic */ AbstractScheduledService this$0;

        class Task implements Runnable {
            final /* synthetic */ ServiceDelegate this$1;

            Task(ServiceDelegate r1) {
                this.this$1 = r1;
            }

            @Override // java.lang.Runnable
            public void run() {
                ServiceDelegate.access$200(this.this$1).lock();
            L9:
                th = move-exception;
                Platform.restoreInterruptIfIsInterruptedException(th);     // Catch: Throwable -> L18
                this.this$1.this$0.shutDown();     // Catch: Exception -> L13 Throwable -> L18
            L15:
                this.this$1.notifyFailed(th);     // Catch: Throwable -> L18
                ((Cancellable) Objects.requireNonNull(ServiceDelegate.access$300(this.this$1))).cancel(false);     // Catch: Throwable -> L18
            L8:
                ServiceDelegate.access$200(this.this$1).unlock();
                return;
            L13:
                e = move-exception;
                Platform.restoreInterruptIfIsInterruptedException(e);     // Catch: Throwable -> L18
                AbstractScheduledService.access$400().get().log(Level.WARNING, "Error while attempting to shut down the service after failure.", e);     // Catch: Throwable -> L18
            L18:
                th = move-exception;
                ServiceDelegate.access$200(this.this$1).unlock();
                throw th;
            L4:
                if (((Cancellable) Objects.requireNonNull(ServiceDelegate.access$300(this.this$1))).isCancelled() == false) goto L7;
                ServiceDelegate.access$200(this.this$1).unlock();
                return;
            L7:
                this.this$1.this$0.runOneIteration();     // Catch: Throwable -> L9
                goto L8
            }
        }

        private ServiceDelegate(AbstractScheduledService r1) {
            this.this$0 = r1;
            this.lock = new ReentrantLock();
            this.task = new Task(this);
        }

        /* synthetic */ ServiceDelegate(AbstractScheduledService r1, C09051 r2) {
            this(r1);
        }

        static /* synthetic */ ReentrantLock access$200(ServiceDelegate r1) {
            return r1.lock;
        }

        static /* synthetic */ Cancellable access$300(ServiceDelegate r1) {
            return r1.runningTask;
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected final void doStart() {
            this.executorService = MoreExecutors.renamingDecorator(this.this$0.executor(), new C0908xa831390f(this));
            this.executorService.execute(new RunnableC0909xa8313910(this));
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected final void doStop() {
            Objects.requireNonNull(this.runningTask);
            Objects.requireNonNull(this.executorService);
            this.runningTask.cancel(false);
            this.executorService.execute(new RunnableC0910xa8313911(this));
        }

        /* JADX INFO: renamed from: lambda$doStart$0$com-google-common-util-concurrent-AbstractScheduledService$ServiceDelegate */
        /* synthetic */ String m384xcd8af3c3() {
            return this.this$0.serviceName() + " " + state();
        }

        /* JADX INFO: renamed from: lambda$doStart$1$com-google-common-util-concurrent-AbstractScheduledService$ServiceDelegate */
        /* synthetic */ void m385xfa22122() {
            this.lock.lock();
            this.this$0.startUp();     // Catch: Throwable -> L5
            Objects.requireNonNull(this.executorService);     // Catch: Throwable -> L5
            this.runningTask = this.this$0.scheduler().schedule(AbstractScheduledService.access$500(this.this$0), this.executorService, this.task);     // Catch: Throwable -> L5
            notifyStarted();     // Catch: Throwable -> L5
        L9:
            this.lock.unlock();
            return;
        L5:
            th = move-exception;
            Platform.restoreInterruptIfIsInterruptedException(th);     // Catch: Throwable -> L11
            notifyFailed(th);     // Catch: Throwable -> L11
            if (this.runningTask == null) goto L9;
            this.runningTask.cancel(false);     // Catch: Throwable -> L11
        L11:
            th = move-exception;
            this.lock.unlock();
            throw th;
        }

        /* JADX INFO: renamed from: lambda$doStop$2$com-google-common-util-concurrent-AbstractScheduledService$ServiceDelegate */
        /* synthetic */ void m386x2d03b891() {
            this.lock.lock();     // Catch: Throwable -> L12
        L9:
            th = move-exception;
            this.lock.unlock();     // Catch: Throwable -> L12
            throw th;     // Catch: Throwable -> L12
        L4:
            if (state() == Service.State.STOPPING) goto L7;
            this.lock.unlock();     // Catch: Throwable -> L12
            return;
        L7:
            this.this$0.shutDown();     // Catch: Throwable -> L9
            this.lock.unlock();     // Catch: Throwable -> L12
            notifyStopped();     // Catch: Throwable -> L12
            return;
        L12:
            th = move-exception;
            Platform.restoreInterruptIfIsInterruptedException(th);
            notifyFailed(th);
        }

        @Override // com.google.common.util.concurrent.AbstractService
        public String toString() {
            return this.this$0.toString();
        }
    }

    static {
        logger = new LazyLogger(AbstractScheduledService.class);
    }

    protected AbstractScheduledService() {
        this.delegate = new ServiceDelegate(this, null);
    }

    static /* synthetic */ LazyLogger access$400() {
        return logger;
    }

    static /* synthetic */ AbstractService access$500(AbstractScheduledService r1) {
        return r1.delegate;
    }

    @Override // com.google.common.util.concurrent.Service
    public final void addListener(Service.Listener r2, Executor r3) {
        this.delegate.addListener(r2, r3);
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitRunning() {
        this.delegate.awaitRunning();
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitRunning(long r2, TimeUnit r4) throws TimeoutException {
        this.delegate.awaitRunning(r2, r4);
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitTerminated() {
        this.delegate.awaitTerminated();
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitTerminated(long r2, TimeUnit r4) throws TimeoutException {
        this.delegate.awaitTerminated(r2, r4);
    }

    protected ScheduledExecutorService executor() {
        final ScheduledExecutorService r0 = Executors.newSingleThreadScheduledExecutor(new C1ThreadFactoryImpl(this));
        addListener(new C09051(this, r0), MoreExecutors.directExecutor());
        return r0;
    }

    @Override // com.google.common.util.concurrent.Service
    public final Throwable failureCause() {
        return this.delegate.failureCause();
    }

    @Override // com.google.common.util.concurrent.Service
    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    protected abstract void runOneIteration() throws Exception;

    protected abstract Scheduler scheduler();

    protected String serviceName() {
        return getClass().getSimpleName();
    }

    protected void shutDown() throws Exception {
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service startAsync() {
        this.delegate.startAsync();
        return this;
    }

    protected void startUp() throws Exception {
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service.State state() {
        return this.delegate.state();
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service stopAsync() {
        this.delegate.stopAsync();
        return this;
    }

    public String toString() {
        return serviceName() + " [" + state() + "]";
    }
}
