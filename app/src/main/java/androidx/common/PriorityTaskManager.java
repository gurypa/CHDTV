package androidx.media3.common;

import androidx.media3.common.util.Util;
import java.io.IOException;
import java.util.Collections;
import java.util.PriorityQueue;

/* JADX INFO: loaded from: classes.dex */
public final class PriorityTaskManager {
    private int highestPriority;
    private final Object lock;
    private final PriorityQueue<Integer> queue;

    public static class PriorityTooLowException extends IOException {
        public PriorityTooLowException(int r3, int r4) {
            super("Priority too low [priority=" + r3 + ", highest=" + r4 + "]");
        }
    }

    public PriorityTaskManager() {
        this.lock = new Object();
        this.queue = new PriorityQueue(10, Collections.reverseOrder());
        this.highestPriority = Integer.MIN_VALUE;
    }

    public void add(int r4) {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.queue.add(Integer.valueOf(r4));     // Catch: Throwable -> L7
        this.highestPriority = Math.max(this.highestPriority, r4);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public void proceed(int r3) throws InterruptedException {
        Object r0 = this.lock;
        monitor-enter(r0);
    L12:
    L9:
        th = move-exception;
        throw th;
    L5:
        if (this.highestPriority == r3) goto L7;
        this.lock.wait();     // Catch: Throwable -> L9
        goto L12
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
    }

    public boolean proceedNonBlocking(int r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (this.highestPriority != r3) goto L7;
        boolean r1 = true;
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L10
        return r1;
    L7:
        r1 = false;
        goto L8
    }

    public void proceedOrThrow(int r4) throws PriorityTooLowException {
        Object r0 = this.lock;
        monitor-enter(r0);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (this.highestPriority != r4) goto L9;
        monitor-exit(r0);     // Catch: Throwable -> L10
        return;
    L9:
        throw new PriorityTooLowException(r4, this.highestPriority);     // Catch: Throwable -> L10
    }

    public void remove(int r4) {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.queue.remove(Integer.valueOf(r4));     // Catch: Throwable -> L11
        if (this.queue.isEmpty() == false) goto L7;
        int r1 = Integer.MIN_VALUE;
    L8:
        this.highestPriority = r1;     // Catch: Throwable -> L11
        this.lock.notifyAll();     // Catch: Throwable -> L11
        monitor-exit(r0);     // Catch: Throwable -> L11
        return;
    L7:
        r1 = ((Integer) Util.castNonNull(this.queue.peek())).intValue();     // Catch: Throwable -> L11
    L11:
        th = move-exception;
        throw th;
    }
}
