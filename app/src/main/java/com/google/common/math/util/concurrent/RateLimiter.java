package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class RateLimiter {

    @CheckForNull
    private volatile Object mutexDoNotUseDirectly;
    private final SleepingStopwatch stopwatch;

    static abstract class SleepingStopwatch {
        protected SleepingStopwatch() {
        }

        public static SleepingStopwatch createFromSystemTimer() {
            return new C09551();
        }

        protected abstract long readMicros();

        protected abstract void sleepMicrosUninterruptibly(long r1);
    }

    RateLimiter(SleepingStopwatch r2) {
        this.stopwatch = (SleepingStopwatch) Preconditions.checkNotNull(r2);
    }

    private boolean canAcquire(long r3, long r5) {
        if ((queryEarliestAvailable(r3) - r5) > r3) goto L5;
        return true;
    L5:
        return false;
    }

    private static void checkPermits(int r2) {
        if (r2 <= 0) goto L4;
        boolean r0 = true;
    L5:
        Preconditions.checkArgument(r0, "Requested permits (%s) must be positive", r2);
        return;
    L4:
        r0 = false;
        goto L5
    }

    public static RateLimiter create(double r1) {
        return create(r1, SleepingStopwatch.createFromSystemTimer());
    }

    public static RateLimiter create(double r10, long r12, TimeUnit r14) {
        if (r12 < 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "warmupPeriod must not be negative: %s", r12);
        return create(r10, r12, r14, 3.0d, SleepingStopwatch.createFromSystemTimer());
    L5:
        r0 = false;
        goto L6
    }

    static RateLimiter create(double r7, long r9, TimeUnit r11, double r12, SleepingStopwatch r14) {
        SmoothRateLimiter.SmoothWarmingUp r0 = new SmoothRateLimiter.SmoothWarmingUp(r14, r9, r11, r12);
        r0.setRate(r7);
        return r0;
    }

    static RateLimiter create(double r3, SleepingStopwatch r5) {
        SmoothRateLimiter.SmoothBursty r0 = new SmoothRateLimiter.SmoothBursty(r5, 1.0d);
        r0.setRate(r3);
        return r0;
    }

    private Object mutex() {
        Object r0 = this.mutexDoNotUseDirectly;
        if (r0 != null) goto L13;
        monitor-enter(this);
        r0 = this.mutexDoNotUseDirectly;     // Catch: Throwable -> L10
        if (r0 != null) goto L8;
        Object r1 = new Object();     // Catch: Throwable -> L10
        r0 = r1;
        this.mutexDoNotUseDirectly = r1;     // Catch: Throwable -> L10
    L8:
        monitor-exit(this);     // Catch: Throwable -> L10
    L10:
        th = move-exception;
        throw th;
    L13:
        return r0;
    }

    public double acquire() {
        return acquire(1);
    }

    public double acquire(int r9) {
        long r0 = reserve(r9);
        this.stopwatch.sleepMicrosUninterruptibly(r0);
        return (r0 * 1.0d) / TimeUnit.SECONDS.toMicros(1);
    }

    abstract double doGetRate();

    abstract void doSetRate(double r1, long r3);

    public final double getRate() {
        Object r0 = mutex();
        monitor-enter(r0);
        double r1 = doGetRate();     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    abstract long queryEarliestAvailable(long r1);

    final long reserve(int r4) {
        checkPermits(r4);
        Object r0 = mutex();
        monitor-enter(r0);
        long r1 = reserveAndGetWaitLength(r4, this.stopwatch.readMicros());     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    final long reserveAndGetWaitLength(int r7, long r8) {
        return Math.max(reserveEarliestAvailable(r7, r8) - r8, 0);
    }

    abstract long reserveEarliestAvailable(int r1, long r2);

    public final void setRate(double r4) {
        if (r4 <= 0.0d) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "rate must be positive");
        Object r1 = mutex();
        monitor-enter(r1);
        doSetRate(r4, this.stopwatch.readMicros());     // Catch: Throwable -> L11
        monitor-exit(r1);     // Catch: Throwable -> L11
        return;
    L11:
        th = move-exception;
        throw th;
    L5:
        r0 = false;
        goto L6
    }

    public String toString() {
        return String.format(Locale.ROOT, "RateLimiter[stableRate=%3.1fqps]", new Object[]{Double.valueOf(getRate())});
    }

    public boolean tryAcquire() {
        return tryAcquire(1, 0, TimeUnit.MICROSECONDS);
    }

    public boolean tryAcquire(int r4) {
        return tryAcquire(r4, 0, TimeUnit.MICROSECONDS);
    }

    public boolean tryAcquire(int r8, long r9, TimeUnit r11) {
        long r0 = Math.max(r11.toMicros(r9), 0);
        checkPermits(r8);
        Object r2 = mutex();
        monitor-enter(r2);
        long r3 = this.stopwatch.readMicros();     // Catch: Throwable -> L13
        if (canAcquire(r3, r0) == true) goto L9;
        monitor-exit(r2);     // Catch: Throwable -> L13
        return false;
    L9:
        long r5 = reserveAndGetWaitLength(r8, r3);     // Catch: Throwable -> L13
        monitor-exit(r2);     // Catch: Throwable -> L13
        this.stopwatch.sleepMicrosUninterruptibly(r5);
        return true;
    L13:
        th = move-exception;
        throw th;
    }

    public boolean tryAcquire(long r2, TimeUnit r4) {
        return tryAcquire(1, r2, r4);
    }
}
