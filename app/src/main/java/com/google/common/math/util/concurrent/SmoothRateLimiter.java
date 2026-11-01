package com.google.common.util.concurrent;

import com.google.common.math.LongMath;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class SmoothRateLimiter extends RateLimiter {
    double maxPermits;
    private long nextFreeTicketMicros;
    double stableIntervalMicros;
    double storedPermits;

    /* JADX INFO: renamed from: com.google.common.util.concurrent.SmoothRateLimiter$1 */
    static /* synthetic */ class C09631 {
    }

    static final class SmoothBursty extends SmoothRateLimiter {
        final double maxBurstSeconds;

        SmoothBursty(RateLimiter.SleepingStopwatch r2, double r3) {
            super(r2, null);
            this.maxBurstSeconds = r3;
        }

        @Override // com.google.common.util.concurrent.SmoothRateLimiter
        double coolDownIntervalMicros() {
            return this.stableIntervalMicros;
        }

        @Override // com.google.common.util.concurrent.SmoothRateLimiter
        void doSetRate(double r7, double r9) {
            double r0 = this.maxPermits;
            this.maxPermits = this.maxBurstSeconds * r7;
            if (r0 != Double.POSITIVE_INFINITY) goto L5;
            this.storedPermits = this.maxPermits;
            return;
        L5:
            double r2 = 0.0d;
            if (r0 == 0.0d) goto L9;
            r2 = (this.storedPermits * this.maxPermits) / r0;
        L9:
            this.storedPermits = r2;
        }

        @Override // com.google.common.util.concurrent.SmoothRateLimiter
        long storedPermitsToWaitTime(double r3, double r5) {
            return 0;
        }
    }

    static final class SmoothWarmingUp extends SmoothRateLimiter {
        private double coldFactor;
        private double slope;
        private double thresholdPermits;
        private final long warmupPeriodMicros;

        SmoothWarmingUp(RateLimiter.SleepingStopwatch r3, long r4, TimeUnit r6, double r7) {
            super(r3, null);
            this.warmupPeriodMicros = r6.toMicros(r4);
            this.coldFactor = r7;
        }

        private double permitsToTime(double r5) {
            return this.stableIntervalMicros + (this.slope * r5);
        }

        @Override // com.google.common.util.concurrent.SmoothRateLimiter
        double coolDownIntervalMicros() {
            return this.warmupPeriodMicros / this.maxPermits;
        }

        @Override // com.google.common.util.concurrent.SmoothRateLimiter
        void doSetRate(double r11, double r13) {
            double r0 = this.maxPermits;
            double r2 = this.coldFactor * r13;
            this.thresholdPermits = (this.warmupPeriodMicros * 0.5d) / r13;
            this.maxPermits = this.thresholdPermits + ((this.warmupPeriodMicros * 2.0d) / (r13 + r2));
            this.slope = (r2 - r13) / (this.maxPermits - this.thresholdPermits);
            if (r0 != Double.POSITIVE_INFINITY) goto L6;
            this.storedPermits = 0.0d;
            return;
        L6:
            if (r0 != 0.0d) goto L8;
            double r4 = this.maxPermits;
        L9:
            this.storedPermits = r4;
            return;
        L8:
            r4 = (this.storedPermits * this.maxPermits) / r0;
            goto L9
        }

        @Override // com.google.common.util.concurrent.SmoothRateLimiter
        long storedPermitsToWaitTime(double r15, double r17) {
            double r0 = r17;
            double r2 = r15 - this.thresholdPermits;
            long r4 = 0;
            if (r2 <= 0.0d) goto L6;
            double r6 = Math.min(r2, r0);
            r4 = (long) ((r6 * (permitsToTime(r2) + permitsToTime(r2 - r6))) / 2.0d);
            r0 = r0 - r6;
        L6:
            return r4 + ((long) (this.stableIntervalMicros * r0));
        }
    }

    private SmoothRateLimiter(RateLimiter.SleepingStopwatch r3) {
        super(r3);
        this.nextFreeTicketMicros = 0;
    }

    /* synthetic */ SmoothRateLimiter(RateLimiter.SleepingStopwatch r1, C09631 r2) {
        this(r1);
    }

    abstract double coolDownIntervalMicros();

    @Override // com.google.common.util.concurrent.RateLimiter
    final double doGetRate() {
        return TimeUnit.SECONDS.toMicros(1) / this.stableIntervalMicros;
    }

    abstract void doSetRate(double r1, double r3);

    @Override // com.google.common.util.concurrent.RateLimiter
    final void doSetRate(double r4, long r6) {
        resync(r6);
        double r0 = TimeUnit.SECONDS.toMicros(1) / r4;
        this.stableIntervalMicros = r0;
        doSetRate(r4, r0);
    }

    @Override // com.google.common.util.concurrent.RateLimiter
    final long queryEarliestAvailable(long r3) {
        return this.nextFreeTicketMicros;
    }

    @Override // com.google.common.util.concurrent.RateLimiter
    final long reserveEarliestAvailable(int r11, long r12) {
        resync(r12);
        long r0 = this.nextFreeTicketMicros;
        double r2 = Math.min(r11, this.storedPermits);
        this.nextFreeTicketMicros = LongMath.saturatedAdd(this.nextFreeTicketMicros, storedPermitsToWaitTime(this.storedPermits, r2) + ((long) (this.stableIntervalMicros * (((double) r11) - r2))));
        this.storedPermits -= r2;
        return r0;
    }

    void resync(long r7) {
        if (r7 <= this.nextFreeTicketMicros) goto L6;
        this.storedPermits = Math.min(this.maxPermits, this.storedPermits + ((r7 - this.nextFreeTicketMicros) / coolDownIntervalMicros()));
        this.nextFreeTicketMicros = r7;
        return;
    }

    abstract long storedPermitsToWaitTime(double r1, double r3);
}
