package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Longs;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Monitor {

    @CheckForNull
    private Guard activeGuards;
    private final boolean fair;
    private final ReentrantLock lock;

    public static abstract class Guard {
        final Condition condition;
        final Monitor monitor;

        @CheckForNull
        Guard next;
        int waiterCount;

        protected Guard(Monitor r2) {
            this.waiterCount = 0;
            this.monitor = (Monitor) Preconditions.checkNotNull(r2, "monitor");
            this.condition = Monitor.access$000(r2).newCondition();
        }

        public abstract boolean isSatisfied();
    }

    public Monitor() {
        this(false);
    }

    public Monitor(boolean r2) {
        this.activeGuards = null;
        this.fair = r2;
        this.lock = new ReentrantLock(r2);
    }

    static /* synthetic */ ReentrantLock access$000(Monitor r1) {
        return r1.lock;
    }

    private void await(Guard r2, boolean r3) throws InterruptedException {
        if (r3 == false) goto L4;
        signalNextWaiter();
    L4:
        beginWaitingFor(r2);
    L12:
        r2.condition.await();     // Catch: Throwable -> L9
        if (r2.isSatisfied() == false) goto L12;
        endWaitingFor(r2);
        return;
    L9:
        th = move-exception;
        endWaitingFor(r2);
        throw th;
    }

    private boolean awaitNanos(Guard r4, long r5, boolean r7) throws InterruptedException {
        boolean r0 = true;
    L4:
        if (r5 <= 0) goto L5;
        if (r0 == false) goto L13;
        if (r7 == true) goto L23;
    L12:
        beginWaitingFor(r4);     // Catch: Throwable -> L19
        r0 = false;
        goto L13
    L23:
        signalNextWaiter();     // Catch: Throwable -> L19
    L19:
        th = move-exception;
        if (r0 == true) goto L22;
        endWaitingFor(r4);
    L22:
        throw th;
    L13:
        r5 = r4.condition.awaitNanos(r5);     // Catch: Throwable -> L19
        if (r4.isSatisfied() == false) goto L4;
        if (r0 == true) goto L29;
        endWaitingFor(r4);
        return true;
    L29:
        return true;
    L5:
        if (r0 == true) goto L28;
        endWaitingFor(r4);
        return false;
    L28:
        return false;
    }

    private void awaitUninterruptibly(Guard r2, boolean r3) {
        if (r3 == false) goto L4;
        signalNextWaiter();
    L4:
        beginWaitingFor(r2);
    L12:
        r2.condition.awaitUninterruptibly();     // Catch: Throwable -> L9
        if (r2.isSatisfied() == false) goto L12;
        endWaitingFor(r2);
        return;
    L9:
        th = move-exception;
        endWaitingFor(r2);
        throw th;
    }

    private void beginWaitingFor(Guard r3) {
        int r0 = r3.waiterCount;
        r3.waiterCount = r0 + 1;
        if (r0 != 0) goto L6;
        r3.next = this.activeGuards;
        this.activeGuards = r3;
        return;
    }

    private void endWaitingFor(Guard r5) {
        int r0 = r5.waiterCount - 1;
        r5.waiterCount = r0;
        if (r0 != 0) goto L11;
        Guard r1 = this.activeGuards;
        Guard r2 = null;
    L5:
        if (r1 == r5) goto L6;
        r2 = r1;
        r1 = r1.next;
        goto L5
    L6:
        if (r2 != null) goto L8;
        this.activeGuards = r1.next;
    L9:
        r1.next = null;
        return;
    L8:
        r2.next = r1.next;
        goto L9
    }

    private static long initNanoTime(long r4) {
        if (r4 > 0) goto L5;
        return 0;
    L5:
        long r2 = System.nanoTime();
        if (r2 != 0) goto L9;
        return 1;
    L9:
        return r2;
    }

    private boolean isSatisfied(Guard r2) {
        return r2.isSatisfied();
    L4:
        th = move-exception;
        signalAllWaiters();
        throw th;
    }

    private static long remainingNanos(long r3, long r5) {
        if (r5 > 0) goto L6;
        return 0;
    L6:
        return r5 - (System.nanoTime() - r3);
    }

    private void signalAllWaiters() {
        Guard r0 = this.activeGuards;
    L3:
        if (r0 == null) goto L5;
        r0.condition.signalAll();
        r0 = r0.next;
        goto L3
    }

    private void signalNextWaiter() {
        Guard r0 = this.activeGuards;
    L3:
        if (r0 == null) goto L11;
        if (isSatisfied(r0) == true) goto L6;
        r0 = r0.next;
        goto L3
    L6:
        r0.condition.signal();
        return;
    }

    private static long toSafeNanos(long r6, TimeUnit r8) {
        return Longs.constrainToRange(r8.toNanos(r6), 0, 6917529027641081853L);
    }

    public void enter() {
        this.lock.lock();
    }

    public boolean enter(long r12, TimeUnit r14) {
        long r0 = toSafeNanos(r12, r14);
        ReentrantLock r2 = this.lock;
        if (this.fair == false) goto L5;
    L8:
        boolean r3 = Thread.interrupted();
        long r4 = System.nanoTime();     // Catch: Throwable -> L19
        long r6 = r0;
    L25:
        boolean r8 = r2.tryLock(r6, TimeUnit.NANOSECONDS);     // Catch: InterruptedException -> L15 Throwable -> L19
        goto L16
    L12:
        if (r3 == false) goto L14;
        Thread.currentThread().interrupt();
    L14:
        return r8;
    L16:
        r3 = true;
        r6 = remainingNanos(r4, r0);     // Catch: Throwable -> L19
    L19:
        th = move-exception;
        if (r3 == false) goto L22;
        Thread.currentThread().interrupt();
    L22:
        throw th;
    L5:
        if (r2.tryLock() == false) goto L8;
        return true;
    }

    public boolean enterIf(Guard r4) {
        if (r4.monitor != this) goto L15;
        ReentrantLock r0 = this.lock;
        r0.lock();
        boolean r2 = r4.isSatisfied();     // Catch: Throwable -> L10
        if (r2 == true) goto L9;
        r0.unlock();
    L9:
        return r2;
    L10:
        th = move-exception;
        if (0 != 0) goto L13;
        r0.unlock();
    L13:
        throw th;
    L15:
        throw new IllegalMonitorStateException();
    }

    public boolean enterIf(Guard r4, long r5, TimeUnit r7) {
        if (r4.monitor != this) goto L19;
        if (enter(r5, r7) == true) goto L20;
        return false;
    L20:
        boolean r1 = r4.isSatisfied();     // Catch: Throwable -> L14
        if (r1 == true) goto L13;
        this.lock.unlock();
    L13:
        return r1;
    L14:
        th = move-exception;
        if (0 != 0) goto L17;
        this.lock.unlock();
    L17:
        throw th;
    L19:
        throw new IllegalMonitorStateException();
    }

    public boolean enterIfInterruptibly(Guard r4) throws InterruptedException {
        if (r4.monitor != this) goto L15;
        ReentrantLock r0 = this.lock;
        r0.lockInterruptibly();
        boolean r2 = r4.isSatisfied();     // Catch: Throwable -> L10
        if (r2 == true) goto L9;
        r0.unlock();
    L9:
        return r2;
    L10:
        th = move-exception;
        if (0 != 0) goto L13;
        r0.unlock();
    L13:
        throw th;
    L15:
        throw new IllegalMonitorStateException();
    }

    public boolean enterIfInterruptibly(Guard r4, long r5, TimeUnit r7) throws InterruptedException {
        if (r4.monitor != this) goto L19;
        ReentrantLock r0 = this.lock;
        if (r0.tryLock(r5, r7) == true) goto L20;
        return false;
    L20:
        boolean r2 = r4.isSatisfied();     // Catch: Throwable -> L14
        if (r2 == true) goto L13;
        r0.unlock();
    L13:
        return r2;
    L14:
        th = move-exception;
        if (0 != 0) goto L17;
        r0.unlock();
    L17:
        throw th;
    L19:
        throw new IllegalMonitorStateException();
    }

    public void enterInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }

    public boolean enterInterruptibly(long r2, TimeUnit r4) throws InterruptedException {
        return this.lock.tryLock(r2, r4);
    }

    public void enterWhen(Guard r5) throws InterruptedException {
        if (r5.monitor != this) goto L17;
        ReentrantLock r0 = this.lock;
        boolean r1 = r0.isHeldByCurrentThread();
        r0.lockInterruptibly();
    L12:
        th = move-exception;
        if (0 != 0) goto L15;
        leave();
    L15:
        throw th;
    L6:
        if (r5.isSatisfied() == true) goto L9;
        await(r5, r1);     // Catch: Throwable -> L12
    L9:
        if (1 != 0) goto L20;
        leave();
        return;
    L20:
        return;
    L17:
        throw new IllegalMonitorStateException();
    }

    public boolean enterWhen(Guard r12, long r13, TimeUnit r15) throws InterruptedException {
        long r0 = toSafeNanos(r13, r15);
        if (r12.monitor != this) goto L49;
        ReentrantLock r2 = this.lock;
        boolean r3 = r2.isHeldByCurrentThread();
        long r4 = 0;
        boolean r7 = false;
        if (this.fair == false) goto L7;
    L13:
        r4 = initNanoTime(r0);
        if (r2.tryLock(r13, r15) == true) goto L52;
        return false;
    L52:
    L37:
        th = move-exception;
        if (0 != 0) goto L47;
        if (1 == 0) goto L46;
        if (r3 == true) goto L46;
        signalNextWaiter();     // Catch: Throwable -> L43
    L43:
        th = move-exception;
        r2.unlock();
        throw th;
    L46:
        r2.unlock();
    L47:
        throw th;
    L18:
        if (r12.isSatisfied() == false) goto L20;
    L25:
        r7 = true;
    L27:
        if (r7 == true) goto L36;
        if (0 == 0) goto L35;
        if (r3 == true) goto L35;
        signalNextWaiter();     // Catch: Throwable -> L32
    L32:
        th = move-exception;
        r2.unlock();
        throw th;
    L35:
        r2.unlock();
    L36:
        return r7;
    L20:
        if (r4 != 0) goto L22;
        long r9 = r0;
    L23:
        if (awaitNanos(r12, r9, r3) == false) goto L27;
    L22:
        r9 = remainingNanos(r4, r0);     // Catch: Throwable -> L37
        goto L23
    L7:
        if (Thread.interrupted() == true) goto L12;
        if (r2.tryLock() == false) goto L13;
    L12:
        throw new InterruptedException();
    L49:
        throw new IllegalMonitorStateException();
    }

    public void enterWhenUninterruptibly(Guard r5) {
        if (r5.monitor != this) goto L17;
        ReentrantLock r0 = this.lock;
        boolean r1 = r0.isHeldByCurrentThread();
        r0.lock();
    L12:
        th = move-exception;
        if (0 != 0) goto L15;
        leave();
    L15:
        throw th;
    L6:
        if (r5.isSatisfied() == true) goto L9;
        awaitUninterruptibly(r5, r1);     // Catch: Throwable -> L12
    L9:
        if (1 != 0) goto L20;
        leave();
        return;
    L20:
        return;
    L17:
        throw new IllegalMonitorStateException();
    }

    public boolean enterWhenUninterruptibly(Guard r13, long r14, TimeUnit r16) {
        long r1 = toSafeNanos(r14, r16);
        if (r13.monitor != this) goto L49;
        ReentrantLock r3 = this.lock;
        long r4 = 0;
        boolean r6 = r3.isHeldByCurrentThread();
        boolean r7 = Thread.interrupted();
    L44:
        th = move-exception;
        if (r7 == false) goto L47;
        Thread.currentThread().interrupt();
    L47:
        throw th;
    L6:
        if (this.fair == true) goto L10;
        if (r3.tryLock() == false) goto L10;
    L53:
    L28:
        th = move-exception;
        if (0 != 0) goto L32;
        r3.unlock();     // Catch: Throwable -> L44
    L32:
        throw th;     // Catch: Throwable -> L44
    L15:
        if (r13.isSatisfied() == true) goto L16;
        if (r4 != 0) goto L20;
        r4 = initNanoTime(r1);     // Catch: Throwable -> L28 InterruptedException -> L33
        long r8 = r1;
    L21:
        boolean r0 = awaitNanos(r13, r8, r6);     // Catch: Throwable -> L28 InterruptedException -> L33
    L23:
        if (r0 == true) goto L25;
        r3.unlock();     // Catch: Throwable -> L44
    L25:
        if (r7 == false) goto L27;
        Thread.currentThread().interrupt();
    L27:
        return r0;
    L20:
        r8 = remainingNanos(r4, r1);     // Catch: Throwable -> L28 InterruptedException -> L33
        goto L21
    L16:
        r0 = true;
    L34:
        r7 = true;
        r6 = false;
    L10:
        r4 = initNanoTime(r1);     // Catch: Throwable -> L44
        long r9 = r1;
    L50:
        goto L41
    L12:
        if (r3.tryLock(r9, TimeUnit.NANOSECONDS) == true) goto L53;
        if (r7 == false) goto L58;
        Thread.currentThread().interrupt();
        return false;
    L58:
        return false;
    L41:
        r7 = true;
        r9 = remainingNanos(r4, r1);     // Catch: Throwable -> L44
        goto L50
    L49:
        throw new IllegalMonitorStateException();
    }

    public int getOccupiedDepth() {
        return this.lock.getHoldCount();
    }

    public int getQueueLength() {
        return this.lock.getQueueLength();
    }

    public int getWaitQueueLength(Guard r3) {
        if (r3.monitor != this) goto L12;
        this.lock.lock();
        int r0 = r3.waiterCount;     // Catch: Throwable -> L8
        this.lock.unlock();
        return r0;
    L8:
        th = move-exception;
        this.lock.unlock();
        throw th;
    L12:
        throw new IllegalMonitorStateException();
    }

    public boolean hasQueuedThread(Thread r2) {
        return this.lock.hasQueuedThread(r2);
    }

    public boolean hasQueuedThreads() {
        return this.lock.hasQueuedThreads();
    }

    public boolean hasWaiters(Guard r2) {
        if (getWaitQueueLength(r2) <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isFair() {
        return this.fair;
    }

    public boolean isOccupied() {
        return this.lock.isLocked();
    }

    public boolean isOccupiedByCurrentThread() {
        return this.lock.isHeldByCurrentThread();
    }

    public void leave() {
        ReentrantLock r0 = this.lock;
    L8:
        th = move-exception;
        r0.unlock();
        throw th;
    L4:
        if (r0.getHoldCount() != 1) goto L6;
        signalNextWaiter();     // Catch: Throwable -> L8
    L6:
        r0.unlock();
    }

    public boolean tryEnter() {
        return this.lock.tryLock();
    }

    public boolean tryEnterIf(Guard r4) {
        if (r4.monitor != this) goto L19;
        ReentrantLock r0 = this.lock;
        if (r0.tryLock() == true) goto L20;
        return false;
    L20:
        boolean r2 = r4.isSatisfied();     // Catch: Throwable -> L14
        if (r2 == true) goto L13;
        r0.unlock();
    L13:
        return r2;
    L14:
        th = move-exception;
        if (0 != 0) goto L17;
        r0.unlock();
    L17:
        throw th;
    L19:
        throw new IllegalMonitorStateException();
    }

    public void waitFor(Guard r2) throws InterruptedException {
        if (r2.monitor != this) goto L11;
        if (this.lock.isHeldByCurrentThread() == false) goto L11;
        if (r2.isSatisfied() == true) goto L12;
        await(r2, true);
        return;
    L12:
        return;
    L11:
        throw new IllegalMonitorStateException();
    }

    public boolean waitFor(Guard r5, long r6, TimeUnit r8) throws InterruptedException {
        long r0 = toSafeNanos(r6, r8);
        if (r5.monitor != this) goto L16;
        if (this.lock.isHeldByCurrentThread() == false) goto L16;
        if (r5.isSatisfied() == false) goto L10;
        return true;
    L10:
        if (Thread.interrupted() == true) goto L14;
        return awaitNanos(r5, r0, true);
    L14:
        throw new InterruptedException();
    L16:
        throw new IllegalMonitorStateException();
    }

    public void waitForUninterruptibly(Guard r2) {
        if (r2.monitor != this) goto L11;
        if (this.lock.isHeldByCurrentThread() == false) goto L11;
        if (r2.isSatisfied() == true) goto L12;
        awaitUninterruptibly(r2, true);
        return;
    L12:
        return;
    L11:
        throw new IllegalMonitorStateException();
    }

    public boolean waitForUninterruptibly(Guard r13, long r14, TimeUnit r16) {
        long r1 = toSafeNanos(r14, r16);
        if (r13.monitor != this) goto L30;
        if (this.lock.isHeldByCurrentThread() == false) goto L30;
        if (r13.isSatisfied() == false) goto L9;
        return true;
    L9:
        long r4 = initNanoTime(r1);
        long r8 = r1;
        boolean r7 = Thread.interrupted();
        boolean r6 = true;
    L31:
        boolean r0 = awaitNanos(r13, r8, r6);     // Catch: Throwable -> L14 InterruptedException -> L16
        goto L17
    L11:
        if (r7 == false) goto L13;
        Thread.currentThread().interrupt();
    L13:
        return r0;
    L14:
        th = move-exception;
        if (r7 == false) goto L28;
        Thread.currentThread().interrupt();
    L28:
        throw th;
    L17:
        r7 = true;
        if (r13.isSatisfied() == true) goto L20;
        r6 = false;
        r8 = remainingNanos(r4, r1);     // Catch: Throwable -> L14
        goto L31
    L20:
        if (1 == 0) goto L22;
        Thread.currentThread().interrupt();
    L22:
        return true;
    L30:
        throw new IllegalMonitorStateException();
    }
}
