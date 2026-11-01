package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class SequentialExecutor implements Executor {
    private static final LazyLogger log = null;
    private final Executor executor;
    private final Deque<Runnable> queue;
    private final QueueWorker worker;
    private long workerRunCount;

    @LazyInit
    private WorkerRunningState workerRunningState;

    private final class QueueWorker implements Runnable {

        @CheckForNull
        Runnable task;
        final /* synthetic */ SequentialExecutor this$0;

        private QueueWorker(SequentialExecutor r1) {
            this.this$0 = r1;
        }

        /* synthetic */ QueueWorker(SequentialExecutor r1, RunnableC09571 r2) {
            this(r1);
        }

        private void workOnQueue() {
            boolean r0 = false;
            boolean r1 = false;
        L43:
            Deque r2 = SequentialExecutor.access$100(this.this$0);     // Catch: Throwable -> L36
            monitor-enter(r2);     // Catch: Throwable -> L36
            if (r1 == false) goto L42;
        L13:
            this.task = (Runnable) SequentialExecutor.access$100(this.this$0).poll();     // Catch: Throwable -> L33
            if (this.task == null) goto L15;
            monitor-exit(r2);     // Catch: Throwable -> L33
            r0 = r0 | Thread.interrupted();
            this.task.run();     // Catch: Throwable -> L26 Exception -> L28
        L24:
            this.task = null;     // Catch: Throwable -> L36
        L26:
            th = move-exception;
            this.task = null;     // Catch: Throwable -> L36
            throw th;     // Catch: Throwable -> L36
        L28:
            e = move-exception;
            SequentialExecutor.access$400().get().log(Level.SEVERE, "Exception while executing runnable " + this.task, e);     // Catch: Throwable -> L26
            this.task = null;     // Catch: Throwable -> L36
            goto L43
        L15:
            SequentialExecutor.access$202(this.this$0, WorkerRunningState.IDLE);     // Catch: Throwable -> L33
            monitor-exit(r2);     // Catch: Throwable -> L33
            if (r0 == false) goto L51;
            Thread.currentThread().interrupt();
            return;
        L51:
            return;
        L42:
        L33:
            th = move-exception;
            throw th;     // Catch: Throwable -> L36
        L7:
            if (SequentialExecutor.access$200(this.this$0) == WorkerRunningState.RUNNING) goto L8;
            SequentialExecutor.access$308(this.this$0);     // Catch: Throwable -> L33
            SequentialExecutor.access$202(this.this$0, WorkerRunningState.RUNNING);     // Catch: Throwable -> L33
            r1 = true;
            goto L13
        L8:
            monitor-exit(r2);     // Catch: Throwable -> L33
            if (r0 == false) goto L50;
            Thread.currentThread().interrupt();
            return;
        L50:
            return;
        L36:
            th = move-exception;
            if (r0 == false) goto L39;
            Thread.currentThread().interrupt();
        L39:
            throw th;
        }

        @Override // java.lang.Runnable
        public void run() {
            workOnQueue();     // Catch: Error -> L4
            return;
        L4:
            e = move-exception;
            monitor-enter(SequentialExecutor.access$100(this.this$0));
            SequentialExecutor.access$202(this.this$0, WorkerRunningState.IDLE);     // Catch: Throwable -> L10
            throw e;
        L10:
            th = move-exception;
            throw th;
        }

        public String toString() {
            Runnable r0 = this.task;
            if (r0 == null) goto L7;
            return "SequentialExecutorWorker{running=" + r0 + "}";
        L7:
            return "SequentialExecutorWorker{state=" + SequentialExecutor.access$200(this.this$0) + "}";
        }
    }

    static final enum WorkerRunningState extends Enum<WorkerRunningState> {
        private static final /* synthetic */ WorkerRunningState[] $VALUES = null;
        public static final WorkerRunningState IDLE = null;
        public static final WorkerRunningState QUEUED = null;
        public static final WorkerRunningState QUEUING = null;
        public static final WorkerRunningState RUNNING = null;

        private static /* synthetic */ WorkerRunningState[] $values() {
            return new WorkerRunningState[]{IDLE, QUEUING, QUEUED, RUNNING};
        }

        static {
            IDLE = new WorkerRunningState("IDLE", 0);
            QUEUING = new WorkerRunningState("QUEUING", 1);
            QUEUED = new WorkerRunningState("QUEUED", 2);
            RUNNING = new WorkerRunningState("RUNNING", 3);
            $VALUES = $values();
        }

        private WorkerRunningState(String r1, int r2) {
            super(r1, r2);
        }

        public static WorkerRunningState valueOf(String r1) {
            return (WorkerRunningState) Enum.valueOf(WorkerRunningState.class, r1);
        }

        public static WorkerRunningState[] values() {
            return (WorkerRunningState[]) $VALUES.clone();
        }
    }

    static {
        log = new LazyLogger(SequentialExecutor.class);
    }

    SequentialExecutor(Executor r3) {
        this.queue = new ArrayDeque();
        this.workerRunningState = WorkerRunningState.IDLE;
        this.workerRunCount = 0;
        this.worker = new QueueWorker(this, null);
        this.executor = (Executor) Preconditions.checkNotNull(r3);
    }

    static /* synthetic */ Deque access$100(SequentialExecutor r1) {
        return r1.queue;
    }

    static /* synthetic */ WorkerRunningState access$200(SequentialExecutor r1) {
        return r1.workerRunningState;
    }

    static /* synthetic */ WorkerRunningState access$202(SequentialExecutor r0, WorkerRunningState r1) {
        r0.workerRunningState = r1;
        return r1;
    }

    static /* synthetic */ long access$308(SequentialExecutor r4) {
        long r0 = r4.workerRunCount;
        r4.workerRunCount = 1 + r0;
        return r0;
    }

    static /* synthetic */ LazyLogger access$400() {
        return log;
    }

    @Override // java.util.concurrent.Executor
    public void execute(final Runnable r10) {
        Preconditions.checkNotNull(r10);
        Deque<Runnable> r0 = this.queue;
        monitor-enter(r0);
    L56:
        th = move-exception;
        throw th;
    L5:
        if (this.workerRunningState != WorkerRunningState.RUNNING) goto L7;
    L53:
        this.queue.add(r10);     // Catch: Throwable -> L56
        monitor-exit(r0);     // Catch: Throwable -> L56
        return;
    L7:
        if (this.workerRunningState == WorkerRunningState.QUEUED) goto L53;
        long r1 = this.workerRunCount;     // Catch: Throwable -> L56
        Runnable r3 = new RunnableC09571(this, r10);     // Catch: Throwable -> L56
        this.queue.add(r3);     // Catch: Throwable -> L56
        this.workerRunningState = WorkerRunningState.QUEUING;     // Catch: Throwable -> L56
        monitor-exit(r0);     // Catch: Throwable -> L56
        boolean r2 = true;
        this.executor.execute(this.worker);     // Catch: Throwable -> L32
        if (this.workerRunningState != WorkerRunningState.QUEUING) goto L18;
        r2 = false;
    L18:
        if (r2 == false) goto L20;
        return;
    L20:
        Deque<Runnable> r6 = this.queue;
        monitor-enter(r6);
    L29:
        th = move-exception;
        throw th;
    L23:
        if (this.workerRunCount == r1) goto L25;
    L27:
        monitor-exit(r6);     // Catch: Throwable -> L29
        return;
    L25:
        if (this.workerRunningState != WorkerRunningState.QUEUING) goto L27;
        this.workerRunningState = WorkerRunningState.QUEUED;     // Catch: Throwable -> L29
    L32:
        th = move-exception;
        monitor-enter(this.queue);
    L50:
        th = move-exception;
        throw th;
    L36:
        if (this.workerRunningState == WorkerRunningState.IDLE) goto L40;
        if (this.workerRunningState == WorkerRunningState.QUEUING) goto L40;
    L42:
        r2 = false;
    L44:
        if ((th instanceof RejectedExecutionException) == false) goto L49;
        if (r2 == true) goto L49;
        return;
    L49:
        throw th;     // Catch: Throwable -> L50
    L40:
        if (this.queue.removeLastOccurrence(r3) == false) goto L42;
        goto L42
    }

    public String toString() {
        return "SequentialExecutor@" + System.identityHashCode(this) + "{" + this.executor + "}";
    }
}
