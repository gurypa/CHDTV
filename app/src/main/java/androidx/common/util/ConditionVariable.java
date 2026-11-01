package androidx.media3.common.util;

/* JADX INFO: loaded from: classes.dex */
public class ConditionVariable {
    private final Clock clock;
    private boolean isOpen;

    public ConditionVariable() {
        this(Clock.DEFAULT);
    }

    public ConditionVariable(Clock r1) {
        this.clock = r1;
    }

    public synchronized void block() throws InterruptedException {
        monitor-enter(this);
    L12:
    L9:
        th = move-exception;
        throw th;
    L4:
        if (this.isOpen == true) goto L7;
        wait();     // Catch: Throwable -> L9
        goto L12
    L7:
        monitor-exit(this);
    }

    public synchronized boolean block(long r7) throws InterruptedException {
        monitor-enter(this);
        if (r7 > 0) goto L8;
        boolean r0 = this.isOpen;     // Catch: Throwable -> L19
        monitor-exit(this);
        return r0;
    L8:
        long r1 = this.clock.elapsedRealtime();     // Catch: Throwable -> L19
        long r2 = r1 + r7;     // Catch: Throwable -> L19
        if (r2 >= r1) goto L12;
        block();     // Catch: Throwable -> L19
    L16:
        boolean r4 = this.isOpen;     // Catch: Throwable -> L19
        monitor-exit(this);
        return r4;
    L12:
        if (this.isOpen == true) goto L16;
        if (r1 >= r2) goto L16;
        wait(r2 - r1);     // Catch: Throwable -> L19
        r1 = this.clock.elapsedRealtime();     // Catch: Throwable -> L19
    L19:
        th = move-exception;
        throw th;
    }

    public synchronized void blockUninterruptible() {
        monitor-enter(this);
        boolean r0 = false;
    L17:
    L14:
        th = move-exception;
        throw th;
    L5:
        if (this.isOpen == true) goto L10;
        wait();     // Catch: InterruptedException -> L8 Throwable -> L14
    L9:
        r0 = true;
        goto L17
    L10:
        if (r0 == false) goto L12;
        Thread.currentThread().interrupt();     // Catch: Throwable -> L14
    L12:
        monitor-exit(this);
    }

    public synchronized boolean close() {
        monitor-enter(this);
        boolean r0 = this.isOpen;     // Catch: Throwable -> L6
        this.isOpen = false;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized boolean isOpen() {
        monitor-enter(this);
        boolean r0 = this.isOpen;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized boolean open() {
        monitor-enter(this);
    L12:
        th = move-exception;
        throw th;
    L4:
        if (this.isOpen == false) goto L9;
        monitor-exit(this);
        return false;
    L9:
        this.isOpen = true;     // Catch: Throwable -> L12
        notifyAll();     // Catch: Throwable -> L12
        monitor-exit(this);
        return true;
    }
}
