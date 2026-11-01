package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class ExecutionSequencer {

    @LazyInit
    private ThreadConfinedTaskQueue latestTaskQueue;
    private final AtomicReference<ListenableFuture<Void>> ref;

    static final enum RunningState extends Enum<RunningState> {
        private static final /* synthetic */ RunningState[] $VALUES = null;
        public static final RunningState CANCELLED = null;
        public static final RunningState NOT_RUN = null;
        public static final RunningState STARTED = null;

        private static /* synthetic */ RunningState[] $values() {
            return new RunningState[]{NOT_RUN, CANCELLED, STARTED};
        }

        static {
            NOT_RUN = new RunningState("NOT_RUN", 0);
            CANCELLED = new RunningState("CANCELLED", 1);
            STARTED = new RunningState("STARTED", 2);
            $VALUES = $values();
        }

        private RunningState(String r1, int r2) {
            super(r1, r2);
        }

        public static RunningState valueOf(String r1) {
            return (RunningState) Enum.valueOf(RunningState.class, r1);
        }

        public static RunningState[] values() {
            return (RunningState[]) $VALUES.clone();
        }
    }

    private static final class TaskNonReentrantExecutor extends AtomicReference<RunningState> implements Executor, Runnable {

        @CheckForNull
        Executor delegate;

        @CheckForNull
        ExecutionSequencer sequencer;

        @CheckForNull
        @LazyInit
        Thread submitting;

        @CheckForNull
        Runnable task;

        private TaskNonReentrantExecutor(Executor r2, ExecutionSequencer r3) {
            super(RunningState.NOT_RUN);
            this.delegate = r2;
            this.sequencer = r3;
        }

        /* synthetic */ TaskNonReentrantExecutor(Executor r1, ExecutionSequencer r2, C09431 r3) {
            this(r1, r2);
        }

        static /* synthetic */ boolean access$200(TaskNonReentrantExecutor r1) {
            return r1.trySetStarted();
        }

        static /* synthetic */ boolean access$400(TaskNonReentrantExecutor r1) {
            return r1.trySetCancelled();
        }

        private boolean trySetCancelled() {
            return compareAndSet(RunningState.NOT_RUN, RunningState.CANCELLED);
        }

        private boolean trySetStarted() {
            return compareAndSet(RunningState.NOT_RUN, RunningState.STARTED);
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable r5) {
            if (get() != RunningState.CANCELLED) goto L6;
            this.delegate = null;
            this.sequencer = null;
            return;
        L6:
            this.submitting = Thread.currentThread();
            ThreadConfinedTaskQueue r0 = ExecutionSequencer.access$300((ExecutionSequencer) Objects.requireNonNull(this.sequencer));     // Catch: Throwable -> L17
            if (r0.thread != this.submitting) goto L14;
            this.sequencer = null;     // Catch: Throwable -> L17
            if (r0.nextTask != null) goto L12;
            boolean r1 = true;
        L13:
            Preconditions.checkState(r1);     // Catch: Throwable -> L17
            r0.nextTask = r5;     // Catch: Throwable -> L17
            r0.nextExecutor = (Executor) Objects.requireNonNull(this.delegate);     // Catch: Throwable -> L17
            this.delegate = null;     // Catch: Throwable -> L17
        L15:
            this.submitting = null;
            return;
        L12:
            r1 = false;
            goto L13
        L14:
            Executor r2 = (Executor) Objects.requireNonNull(this.delegate);     // Catch: Throwable -> L17
            this.delegate = null;     // Catch: Throwable -> L17
            this.task = r5;     // Catch: Throwable -> L17
            r2.execute(this);     // Catch: Throwable -> L17
        L17:
            th = move-exception;
            this.submitting = null;
            throw th;
        }

        @Override // java.lang.Runnable
        public void run() {
            Thread r0 = Thread.currentThread();
            C09431 r2 = null;
            if (r0 == this.submitting) goto L6;
            Runnable r1 = (Runnable) Objects.requireNonNull(this.task);
            this.task = null;
            r1.run();
            return;
        L6:
            ThreadConfinedTaskQueue r3 = new ThreadConfinedTaskQueue(r2);
            r3.thread = r0;
            ExecutionSequencer.access$302((ExecutionSequencer) Objects.requireNonNull(this.sequencer), r3);
            this.sequencer = null;
            Runnable r4 = (Runnable) Objects.requireNonNull(this.task);     // Catch: Throwable -> L16
            this.task = null;     // Catch: Throwable -> L16
            r4.run();     // Catch: Throwable -> L16
        L8:
            Runnable r5 = r3.nextTask;     // Catch: Throwable -> L16
            if (r5 == null) goto L14;
            Executor r6 = r3.nextExecutor;     // Catch: Throwable -> L16
            if (r6 == null) goto L14;
            r3.nextTask = null;     // Catch: Throwable -> L16
            r3.nextExecutor = null;     // Catch: Throwable -> L16
            r6.execute(r5);     // Catch: Throwable -> L16
        L14:
            r3.thread = null;
            return;
        L16:
            th = move-exception;
            r3.thread = null;
            throw th;
        }
    }

    private static final class ThreadConfinedTaskQueue {

        @CheckForNull
        Executor nextExecutor;

        @CheckForNull
        Runnable nextTask;

        @CheckForNull
        @LazyInit
        Thread thread;

        private ThreadConfinedTaskQueue() {
        }

        /* synthetic */ ThreadConfinedTaskQueue(C09431 r1) {
            this();
        }
    }

    private ExecutionSequencer() {
        this.ref = new AtomicReference(Futures.immediateVoidFuture());
        this.latestTaskQueue = new ThreadConfinedTaskQueue(null);
    }

    static /* synthetic */ ThreadConfinedTaskQueue access$300(ExecutionSequencer r1) {
        return r1.latestTaskQueue;
    }

    static /* synthetic */ ThreadConfinedTaskQueue access$302(ExecutionSequencer r0, ThreadConfinedTaskQueue r1) {
        r0.latestTaskQueue = r1;
        return r1;
    }

    public static ExecutionSequencer create() {
        return new ExecutionSequencer();
    }

    static /* synthetic */ void lambda$submitAsync$0(TrustedListenableFutureTask r1, SettableFuture r2, ListenableFuture r3, ListenableFuture r4, TaskNonReentrantExecutor r5) {
        if (r1.isDone() == false) goto L6;
        r2.setFuture(r3);
        return;
    L6:
        if (r4.isCancelled() == true) goto L8;
        return;
    L8:
        if (TaskNonReentrantExecutor.access$400(r5) == false) goto L13;
        r1.cancel(false);
        return;
    }

    public <T> ListenableFuture<T> submit(final Callable<T> r2, Executor r3) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        return submitAsync(new C09431(this, r2), r3);
    }

    public <T> ListenableFuture<T> submitAsync(final AsyncCallable<T> r9, Executor r10) {
        Preconditions.checkNotNull(r9);
        Preconditions.checkNotNull(r10);
        final TaskNonReentrantExecutor r0 = new TaskNonReentrantExecutor(r10, this, null);
        AsyncCallable<T> r1 = new C09442(this, r0, r9);
        final SettableFuture r4 = SettableFuture.create();
        final ListenableFuture<Void> r5 = this.ref.getAndSet(r4);
        final TrustedListenableFutureTask r3 = TrustedListenableFutureTask.create(r1);
        r5.addListener(r3, r0);
        final ListenableFuture<T> r6 = Futures.nonCancellationPropagating(r3);
        Runnable r2 = new ExecutionSequencer$$ExternalSyntheticLambda0(r3, r4, r5, r6, r0);
        r6.addListener(r2, MoreExecutors.directExecutor());
        r3.addListener(r2, MoreExecutors.directExecutor());
        return r6;
    }
}
