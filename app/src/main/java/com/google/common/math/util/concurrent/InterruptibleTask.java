package com.google.common.util.concurrent;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;
import java.util.concurrent.locks.LockSupport;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class InterruptibleTask<T> extends AtomicReference<Runnable> implements Runnable {
    private static final Runnable DONE = null;
    private static final int MAX_BUSY_WAIT_SPINS = 1000;
    private static final Runnable PARKED = null;

    /* JADX INFO: renamed from: com.google.common.util.concurrent.InterruptibleTask$1 */
    static /* synthetic */ class C09471 {
    }

    static final class Blocker extends AbstractOwnableSynchronizer implements Runnable {
        private final InterruptibleTask<?> task;

        private Blocker(InterruptibleTask<?> r1) {
            this.task = r1;
        }

        /* synthetic */ Blocker(InterruptibleTask r1, C09471 r2) {
            this(r1);
        }

        static /* synthetic */ void access$200(Blocker r0, Thread r1) {
            r0.setOwner(r1);
        }

        private void setOwner(Thread r1) {
            super.setExclusiveOwnerThread(r1);
        }

        @CheckForNull
        Thread getOwner() {
            return super.getExclusiveOwnerThread();
        }

        @Override // java.lang.Runnable
        public void run() {
        }

        public String toString() {
            return this.task.toString();
        }
    }

    private static final class DoNothingRunnable implements Runnable {
        private DoNothingRunnable() {
        }

        /* synthetic */ DoNothingRunnable(C09471 r1) {
            this();
        }

        @Override // java.lang.Runnable
        public void run() {
        }
    }

    static {
        C09471 r1 = null;
        DONE = new DoNothingRunnable(r1);
        PARKED = new DoNothingRunnable(r1);
    }

    InterruptibleTask() {
    }

    private void waitForInterrupt(Thread r6) {
        boolean r0 = false;
        int r1 = 0;
        Runnable r2 = get();
        Blocker r3 = null;
    L4:
        if ((r2 instanceof Blocker) == true) goto L12;
        if (r2 == PARKED) goto L12;
        if (r0 == false) goto L33;
        r6.interrupt();
        return;
    L33:
        return;
    L12:
        if ((r2 instanceof Blocker) == false) goto L14;
        r3 = (Blocker) r2;
    L14:
        r1 = r1 + 1;
        if (r1 > 1000) goto L17;
        Thread.yield();
    L28:
        r2 = get();
        goto L4
    L17:
        if (r2 == PARKED) goto L21;
        if (compareAndSet(r2, PARKED) == false) goto L28;
    L21:
        if (Thread.interrupted() == true) goto L25;
        if (r0 == true) goto L25;
        boolean r4 = false;
    L26:
        LockSupport.park(r3);
        r0 = r4;
    L25:
        r4 = true;
        goto L26
    }

    abstract void afterRanInterruptiblyFailure(Throwable r1);

    abstract void afterRanInterruptiblySuccess(@ParametricNullness T r1);

    final void interruptTask() {
        Runnable r0 = get();
        if ((r0 instanceof Thread) == false) goto L16;
        Blocker r1 = new Blocker(this, null);
        Blocker.access$200(r1, Thread.currentThread());
        if (compareAndSet(r0, r1) == false) goto L19;
        ((Thread) r0).interrupt();     // Catch: Throwable -> L11
        if (getAndSet(DONE) != PARKED) goto L21;
        LockSupport.unpark((Thread) r0);
        return;
    L21:
        return;
    L11:
        th = move-exception;
        if (getAndSet(DONE) != PARKED) goto L15;
        LockSupport.unpark((Thread) r0);
    L15:
        throw th;
    L19:
        return;
    }

    abstract boolean isDone();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.Runnable
    public final void run() {
        Thread r0 = Thread.currentThread();
        if (compareAndSet(null, r0) == true) goto L5;
        return;
    L5:
        boolean r1 = !isDone();
        Object r2 = null;
        Throwable r3 = null;
        if (r1 == false) goto L26;
        r2 = runInterruptibly();     // Catch: Throwable -> L9
    L9:
        th = move-exception;
        Platform.restoreInterruptIfIsInterruptedException(th);     // Catch: Throwable -> L16
        r3 = th;
        if (compareAndSet(r0, DONE) == true) goto L14;
        waitForInterrupt(r0);
    L14:
        if (r1 == false) goto L36;
    L15:
        afterRanInterruptiblyFailure(r3);
        return;
    L36:
        return;
    L16:
        th = move-exception;
        if (compareAndSet(r0, DONE) == true) goto L20;
        waitForInterrupt(r0);
    L20:
        if (r1 == false) goto L24;
        if (0 != 0) goto L23;
        afterRanInterruptiblySuccess(NullnessCasts.uncheckedCastNullableTToT(null));
        goto L24
    L23:
        afterRanInterruptiblyFailure(null);
    L24:
        throw th;
    L26:
        if (compareAndSet(r0, DONE) == true) goto L28;
        waitForInterrupt(r0);
    L28:
        if (r1 == false) goto L38;
        if (0 != 0) goto L15;
        afterRanInterruptiblySuccess(NullnessCasts.uncheckedCastNullableTToT(r2));
        return;
    }

    @ParametricNullness
    abstract T runInterruptibly() throws Exception;

    abstract String toPendingString();

    @Override // java.util.concurrent.atomic.AtomicReference
    public final String toString() {
        Runnable r0 = get();
        if (r0 != DONE) goto L6;
        String r1 = "running=[DONE]";
    L13:
        return r1 + ", " + toPendingString();
    L6:
        if ((r0 instanceof Blocker) == false) goto L9;
        r1 = "running=[INTERRUPTED]";
        goto L13
    L9:
        if ((r0 instanceof Thread) == false) goto L11;
        r1 = "running=[RUNNING ON " + ((Thread) r0).getName() + "]";
        goto L13
    L11:
        r1 = "running=[NOT STARTED YET]";
        goto L13
    }
}
