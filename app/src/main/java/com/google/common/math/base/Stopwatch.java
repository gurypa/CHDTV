package com.google.common.base;

import androidx.media3.exoplayer.upstream.CmcdData;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Stopwatch {
    private long elapsedNanos;
    private boolean isRunning;
    private long startTick;
    private final Ticker ticker;

    /* JADX INFO: renamed from: com.google.common.base.Stopwatch$1 */
    static /* synthetic */ class C05551 {
        static final /* synthetic */ int[] $SwitchMap$java$util$concurrent$TimeUnit = null;

        static {
            $SwitchMap$java$util$concurrent$TimeUnit = new int[TimeUnit.values().length];
            $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.NANOSECONDS.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L35:
            $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.MICROSECONDS.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
        L27:
            $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.MILLISECONDS.ordinal()] = 3;     // Catch: NoSuchFieldError -> L11
        L25:
            $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.SECONDS.ordinal()] = 4;     // Catch: NoSuchFieldError -> L14
        L29:
            $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.MINUTES.ordinal()] = 5;     // Catch: NoSuchFieldError -> L17
        L33:
            $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.HOURS.ordinal()] = 6;     // Catch: NoSuchFieldError -> L20
        L37:
            $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.DAYS.ordinal()] = 7;     // Catch: NoSuchFieldError -> L23
            return;
        }
    }

    Stopwatch() {
        this.ticker = Ticker.systemTicker();
    }

    Stopwatch(Ticker r2) {
        this.ticker = (Ticker) Preconditions.checkNotNull(r2, "ticker");
    }

    private static String abbreviate(TimeUnit r2) {
        switch(C05551.$SwitchMap$java$util$concurrent$TimeUnit[r2.ordinal()]) {
            case 1: goto L18;
            case 2: goto L16;
            case 3: goto L14;
            case 4: goto L12;
            case 5: goto L10;
            case 6: goto L8;
            case 7: goto L6;
            default: goto L5;
        };
    L6:
        return "d";
    L8:
        return CmcdData.Factory.STREAMING_FORMAT_HLS;
    L10:
        return "min";
    L12:
        return CmcdData.Factory.STREAMING_FORMAT_SS;
    L14:
        return "ms";
    L16:
        return "μs";
    L18:
        return "ns";
    L5:
        throw new AssertionError();
    }

    private static TimeUnit chooseUnit(long r4) {
        if (TimeUnit.DAYS.convert(r4, TimeUnit.NANOSECONDS) <= 0) goto L7;
        return TimeUnit.DAYS;
    L7:
        if (TimeUnit.HOURS.convert(r4, TimeUnit.NANOSECONDS) <= 0) goto L11;
        return TimeUnit.HOURS;
    L11:
        if (TimeUnit.MINUTES.convert(r4, TimeUnit.NANOSECONDS) <= 0) goto L15;
        return TimeUnit.MINUTES;
    L15:
        if (TimeUnit.SECONDS.convert(r4, TimeUnit.NANOSECONDS) <= 0) goto L19;
        return TimeUnit.SECONDS;
    L19:
        if (TimeUnit.MILLISECONDS.convert(r4, TimeUnit.NANOSECONDS) <= 0) goto L23;
        return TimeUnit.MILLISECONDS;
    L23:
        if (TimeUnit.MICROSECONDS.convert(r4, TimeUnit.NANOSECONDS) <= 0) goto L27;
        return TimeUnit.MICROSECONDS;
    L27:
        return TimeUnit.NANOSECONDS;
    }

    public static Stopwatch createStarted() {
        return new Stopwatch().start();
    }

    public static Stopwatch createStarted(Ticker r1) {
        return new Stopwatch(r1).start();
    }

    public static Stopwatch createUnstarted() {
        return new Stopwatch();
    }

    public static Stopwatch createUnstarted(Ticker r1) {
        return new Stopwatch(r1);
    }

    private long elapsedNanos() {
        if (this.isRunning == false) goto L6;
        return (this.ticker.read() - this.startTick) + this.elapsedNanos;
    L6:
        return this.elapsedNanos;
    }

    public long elapsed(TimeUnit r4) {
        return r4.convert(elapsedNanos(), TimeUnit.NANOSECONDS);
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public Stopwatch reset() {
        this.elapsedNanos = 0;
        this.isRunning = false;
        return this;
    }

    public Stopwatch start() {
        Preconditions.checkState(!this.isRunning, "This stopwatch is already running.");
        this.isRunning = true;
        this.startTick = this.ticker.read();
        return this;
    }

    public Stopwatch stop() {
        long r0 = this.ticker.read();
        Preconditions.checkState(this.isRunning, "This stopwatch is already stopped.");
        this.isRunning = false;
        this.elapsedNanos += r0 - this.startTick;
        return this;
    }

    public String toString() {
        long r0 = elapsedNanos();
        TimeUnit r2 = chooseUnit(r0);
        return Platform.formatCompact4Digits(r0 / TimeUnit.NANOSECONDS.convert(1, r2)) + " " + abbreviate(r2);
    }
}
