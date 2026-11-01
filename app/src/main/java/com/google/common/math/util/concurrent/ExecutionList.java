package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class ExecutionList {
    private static final LazyLogger log = null;
    private boolean executed;

    @CheckForNull
    private RunnableExecutorPair runnables;

    private static final class RunnableExecutorPair {
        final Executor executor;

        @CheckForNull
        RunnableExecutorPair next;
        final Runnable runnable;

        RunnableExecutorPair(Runnable r1, Executor r2, @CheckForNull RunnableExecutorPair r3) {
            this.runnable = r1;
            this.executor = r2;
            this.next = r3;
        }
    }

    static {
        log = new LazyLogger(ExecutionList.class);
    }

    public ExecutionList() {
    }

    private static void executeListener(Runnable r5, Executor r6) {
        r6.execute(r5);     // Catch: Exception -> L4
        return;
    L4:
        e = move-exception;
        log.get().log(Level.SEVERE, "RuntimeException while executing runnable " + r5 + " with executor " + r6, e);
    }

    public void add(Runnable r3, Executor r4) {
        Preconditions.checkNotNull(r3, "Runnable was null.");
        Preconditions.checkNotNull(r4, "Executor was null.");
        monitor-enter(this);
    L12:
        th = move-exception;
        throw th;
    L5:
        if (this.executed == true) goto L9;
        this.runnables = new RunnableExecutorPair(r3, r4, this.runnables);     // Catch: Throwable -> L12
        monitor-exit(this);     // Catch: Throwable -> L12
        return;
    L9:
        monitor-exit(this);     // Catch: Throwable -> L12
        executeListener(r3, r4);
    }

    public void execute() {
        monitor-enter(this);
    L15:
        th = move-exception;
        throw th;
    L4:
        if (this.executed == false) goto L7;
        monitor-exit(this);     // Catch: Throwable -> L15
        return;
    L7:
        this.executed = true;     // Catch: Throwable -> L15
        RunnableExecutorPair r0 = this.runnables;     // Catch: Throwable -> L15
        this.runnables = null;     // Catch: Throwable -> L15
        monitor-exit(this);     // Catch: Throwable -> L15
        RunnableExecutorPair r1 = null;
    L10:
        if (r0 == null) goto L12;
        RunnableExecutorPair r2 = r0;
        r0 = r0.next;
        r2.next = r1;
        r1 = r2;
    L12:
        if (r1 == null) goto L14;
        executeListener(r1.runnable, r1.executor);
        r1 = r1.next;
        goto L12
    }
}
