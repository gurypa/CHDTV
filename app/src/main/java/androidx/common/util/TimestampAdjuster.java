package androidx.media3.common.util;

import androidx.media3.common.C0180C;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
public final class TimestampAdjuster {
    private static final long MAX_PTS_PLUS_ONE = 8589934592L;
    public static final long MODE_NO_OFFSET = Long.MAX_VALUE;
    public static final long MODE_SHARED = 9223372036854775806L;
    private long firstSampleTimestampUs;
    private long lastUnadjustedTimestampUs;
    private final ThreadLocal<Long> nextSampleTimestampUs;
    private long timestampOffsetUs;

    public TimestampAdjuster(long r2) {
        this.nextSampleTimestampUs = new ThreadLocal();
        reset(r2);
    }

    public static long ptsToUs(long r6) {
        return Util.scaleLargeTimestamp(r6, 1000000, 90000);
    }

    public static long usToNonWrappedPts(long r6) {
        return Util.scaleLargeTimestamp(r6, 90000, 1000000);
    }

    public static long usToWrappedPts(long r4) {
        return usToNonWrappedPts(r4) % MAX_PTS_PLUS_ONE;
    }

    public synchronized long adjustSampleTimestamp(long r5) {
        monitor-enter(this);
        if (r5 != C0180C.TIME_UNSET) goto L21;
        monitor-exit(this);
        return C0180C.TIME_UNSET;
    L21:
    L18:
        th = move-exception;
        throw th;
    L8:
        if (isInitialized() == false) goto L10;
    L14:
        this.lastUnadjustedTimestampUs = r5;     // Catch: Throwable -> L18
        long r0 = this.timestampOffsetUs + r5;
        monitor-exit(this);
        return r0;
    L10:
        if (this.firstSampleTimestampUs != MODE_SHARED) goto L12;
        long r1 = ((Long) Assertions.checkNotNull(this.nextSampleTimestampUs.get())).longValue();     // Catch: Throwable -> L18
    L13:
        this.timestampOffsetUs = r1 - r5;     // Catch: Throwable -> L18
        notifyAll();     // Catch: Throwable -> L18
        goto L14
    L12:
        r1 = this.firstSampleTimestampUs;     // Catch: Throwable -> L18
        goto L13
    }

    public synchronized long adjustTsTimestamp(long r13) {
        monitor-enter(this);
        if (r13 != C0180C.TIME_UNSET) goto L20;
        monitor-exit(this);
        return C0180C.TIME_UNSET;
    L20:
    L17:
        th = move-exception;
        throw th;
    L8:
        if (this.lastUnadjustedTimestampUs == C0180C.TIME_UNSET) goto L14;
        long r0 = usToNonWrappedPts(this.lastUnadjustedTimestampUs);     // Catch: Throwable -> L17
        long r2 = (4294967296L + r0) / MAX_PTS_PLUS_ONE;     // Catch: Throwable -> L17
        long r6 = ((r2 - 1) * MAX_PTS_PLUS_ONE) + r13;     // Catch: Throwable -> L17
        long r4 = (MAX_PTS_PLUS_ONE * r2) + r13;     // Catch: Throwable -> L17
        if (Math.abs(r6 - r0) >= Math.abs(r4 - r0)) goto L12;
        long r8 = r6;
    L13:
        r13 = r8;
        goto L14
    L12:
        r8 = r4;
    L14:
        long r1 = adjustSampleTimestamp(ptsToUs(r13));     // Catch: Throwable -> L17
        monitor-exit(this);
        return r1;
    }

    public synchronized long adjustTsTimestampGreaterThanPreviousTimestamp(long r11) {
        monitor-enter(this);
        if (r11 != C0180C.TIME_UNSET) goto L21;
        monitor-exit(this);
        return C0180C.TIME_UNSET;
    L21:
    L18:
        th = move-exception;
        throw th;
    L8:
        if (this.lastUnadjustedTimestampUs == C0180C.TIME_UNSET) goto L15;
        long r0 = usToNonWrappedPts(this.lastUnadjustedTimestampUs);     // Catch: Throwable -> L18
        long r4 = r0 / MAX_PTS_PLUS_ONE;     // Catch: Throwable -> L18
        Long.signum(r4);
        long r6 = (r4 * MAX_PTS_PLUS_ONE) + r11;
        long r8 = ((1 + r4) * MAX_PTS_PLUS_ONE) + r11;
        if (r6 < r0) goto L13;
        long r2 = r6;
    L14:
        r11 = r2;
        goto L15
    L13:
        r2 = r8;
    L15:
        long r1 = adjustSampleTimestamp(ptsToUs(r11));     // Catch: Throwable -> L18
        monitor-exit(this);
        return r1;
    }

    public synchronized long getFirstSampleTimestampUs() {
        monitor-enter(this);
    L13:
        th = move-exception;
        throw th;
    L4:
        if (this.firstSampleTimestampUs != Long.MAX_VALUE) goto L6;
    L10:
        long r0 = C0180C.TIME_UNSET;
    L11:
        monitor-exit(this);
        return r0;
    L6:
        if (this.firstSampleTimestampUs == MODE_SHARED) goto L10;
        r0 = this.firstSampleTimestampUs;     // Catch: Throwable -> L13
        goto L11
    }

    public synchronized long getLastAdjustedTimestampUs() {
        monitor-enter(this);
    L9:
        th = move-exception;
        throw th;
    L4:
        if (this.lastUnadjustedTimestampUs == C0180C.TIME_UNSET) goto L6;
        long r0 = this.lastUnadjustedTimestampUs + this.timestampOffsetUs;     // Catch: Throwable -> L9
    L7:
        monitor-exit(this);
        return r0;
    L6:
        r0 = getFirstSampleTimestampUs();     // Catch: Throwable -> L9
        goto L7
    }

    public synchronized long getTimestampOffsetUs() {
        monitor-enter(this);
        long r0 = this.timestampOffsetUs;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized boolean isInitialized() {
        monitor-enter(this);
        if (this.timestampOffsetUs == C0180C.TIME_UNSET) goto L7;
        boolean r0 = true;
    L8:
        monitor-exit(this);
        return r0;
    L7:
        r0 = false;
    L10:
        th = move-exception;
        throw th;
    }

    public synchronized void reset(long r6) {
        monitor-enter(this);
        this.firstSampleTimestampUs = r6;     // Catch: Throwable -> L10
        if (r6 != Long.MAX_VALUE) goto L6;
        long r3 = 0;
    L7:
        this.timestampOffsetUs = r3;     // Catch: Throwable -> L10
        this.lastUnadjustedTimestampUs = C0180C.TIME_UNSET;     // Catch: Throwable -> L10
        monitor-exit(this);
        return;
    L6:
        r3 = -9223372036854775807L;
    L10:
        th = move-exception;
        throw th;
    }

    public synchronized void sharedInitializeOrWait(boolean r14, long r15, long r17) throws InterruptedException, TimeoutException {
        monitor-enter(this);
    L34:
        th = move-exception;
        throw th;
    L5:
        if (this.firstSampleTimestampUs != MODE_SHARED) goto L7;
        boolean r2 = true;
    L8:
        Assertions.checkState(r2);     // Catch: Throwable -> L34
        if (isInitialized() == false) goto L12;
        monitor-exit(this);
        return;
    L12:
        if (r14 == false) goto L14;
        this.nextSampleTimestampUs.set(Long.valueOf(r15));     // Catch: Throwable -> L34
    L32:
        monitor-exit(this);
        return;
    L14:
        long r5 = 0;
        long r7 = r17;
    L16:
        if (isInitialized() == true) goto L32;
        if (r17 == 0) goto L19;
        if (r7 <= 0) goto L23;
        boolean r3 = true;
    L24:
        Assertions.checkState(r3);     // Catch: Throwable -> L34
        long r9 = android.os.SystemClock.elapsedRealtime();     // Catch: Throwable -> L34
        wait(r7);     // Catch: Throwable -> L34
        r5 = r5 + (android.os.SystemClock.elapsedRealtime() - r9);     // Catch: Throwable -> L34
        if (r5 >= r17) goto L27;
    L31:
        r7 = r17 - r5;
        goto L16
    L27:
        if (isInitialized() == true) goto L31;
        throw new TimeoutException("TimestampAdjuster failed to initialize in " + r17 + " milliseconds");     // Catch: Throwable -> L34
    L23:
        r3 = false;
        goto L24
    L19:
        wait();     // Catch: Throwable -> L34
        goto L16
    L7:
        r2 = false;
        goto L8
    }
}
