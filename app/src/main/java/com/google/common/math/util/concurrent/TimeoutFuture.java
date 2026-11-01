package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class TimeoutFuture<V> extends FluentFuture.TrustedFuture<V> {

    @CheckForNull
    @LazyInit
    private ListenableFuture<V> delegateRef;

    @CheckForNull
    @LazyInit
    private ScheduledFuture<?> timer;

    /* JADX INFO: renamed from: com.google.common.util.concurrent.TimeoutFuture$1 */
    static /* synthetic */ class C09671 {
    }

    private static final class Fire<V> implements Runnable {

        @CheckForNull
        @LazyInit
        TimeoutFuture<V> timeoutFutureRef;

        Fire(TimeoutFuture<V> r1) {
            this.timeoutFutureRef = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            TimeoutFuture<V> r0 = this.timeoutFutureRef;
            if (r0 != null) goto L5;
            return;
        L5:
            ListenableFuture<? extends V> r1 = TimeoutFuture.access$000(r0);
            if (r1 != null) goto L8;
            return;
        L8:
            C09671 r2 = null;
            this.timeoutFutureRef = null;
            if (r1.isDone() == false) goto L27;
            r0.setFuture(r1);
            return;
        L27:
            ScheduledFuture r4 = TimeoutFuture.access$100(r0);     // Catch: Throwable -> L24
            TimeoutFuture.access$102(r0, null);     // Catch: Throwable -> L24
            String r5 = "Timed out";
            if (r4 != null) goto L28;
        L17:
            r0.setException(new TimeoutFutureException(r5 + ": " + r1, r2));     // Catch: Throwable -> L24
            r1.cancel(true);
            return;
        L21:
            th = move-exception;
            r0.setException(new TimeoutFutureException(r5, r2));     // Catch: Throwable -> L24
            throw th;     // Catch: Throwable -> L24
        L28:
            long r6 = Math.abs(r4.getDelay(TimeUnit.MILLISECONDS));     // Catch: Throwable -> L21
            if (r6 <= 10) goto L17;
            r5 = "Timed out (timeout delayed by " + r6 + " ms after scheduled time)";     // Catch: Throwable -> L21
        L24:
            th = move-exception;
            r1.cancel(true);
            throw th;
        }
    }

    private static final class TimeoutFutureException extends TimeoutException {
        private TimeoutFutureException(String r1) {
            super(r1);
        }

        /* synthetic */ TimeoutFutureException(String r1, C09671 r2) {
            this(r1);
        }

        @Override // java.lang.Throwable
        public synchronized Throwable fillInStackTrace() {
            monitor-enter(this);
            setStackTrace(new StackTraceElement[0]);     // Catch: Throwable -> L7
            monitor-exit(this);
            return this;
        L7:
            th = move-exception;
            throw th;
        }
    }

    private TimeoutFuture(ListenableFuture<V> r2) {
        this.delegateRef = (ListenableFuture) Preconditions.checkNotNull(r2);
    }

    static /* synthetic */ ListenableFuture access$000(TimeoutFuture r1) {
        return r1.delegateRef;
    }

    static /* synthetic */ ScheduledFuture access$100(TimeoutFuture r1) {
        return r1.timer;
    }

    static /* synthetic */ ScheduledFuture access$102(TimeoutFuture r0, ScheduledFuture r1) {
        r0.timer = r1;
        return r1;
    }

    static <V> ListenableFuture<V> create(ListenableFuture<V> r3, long r4, TimeUnit r6, ScheduledExecutorService r7) {
        TimeoutFuture r0 = new TimeoutFuture(r3);
        Fire r1 = new Fire(r0);
        r0.timer = r7.schedule(r1, r4, r6);
        r3.addListener(r1, MoreExecutors.directExecutor());
        return r0;
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    protected void afterDone() {
        maybePropagateCancellationTo(this.delegateRef);
        ScheduledFuture<?> r0 = this.timer;
        if (r0 == null) goto L5;
        r0.cancel(false);
    L5:
        this.delegateRef = null;
        this.timer = null;
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    @CheckForNull
    protected String pendingToString() {
        ListenableFuture<V> r0 = this.delegateRef;
        ScheduledFuture<?> r1 = this.timer;
        if (r0 == null) goto L10;
        String r2 = "inputFuture=[" + r0 + "]";
        if (r1 == null) goto L12;
        long r3 = r1.getDelay(TimeUnit.MILLISECONDS);
        if (r3 > 0) goto L9;
        return r2;
    L9:
        return r2 + ", remaining delay=[" + r3 + " ms]";
    L12:
        return r2;
    L10:
        return null;
    }
}
