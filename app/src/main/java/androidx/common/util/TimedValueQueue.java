package androidx.media3.common.util;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class TimedValueQueue<V> {
    private static final int INITIAL_BUFFER_SIZE = 10;
    private int first;
    private int size;
    private long[] timestamps;
    private V[] values;

    public TimedValueQueue() {
        this(10);
    }

    public TimedValueQueue(int r2) {
        this.timestamps = new long[r2];
        this.values = (V[]) newArray(r2);
    }

    private void addUnchecked(long r3, V r5) {
        int r0 = (this.first + this.size) % this.values.length;
        this.timestamps[r0] = r3;
        this.values[r0] = r5;
        this.size++;
    }

    private void clearBufferOnTimeDiscontinuity(long r5) {
        if (this.size > 0) goto L5;
        return;
    L5:
        if (r5 > this.timestamps[((this.first + this.size) - 1) % this.values.length]) goto L9;
        clear();
        return;
    }

    private void doubleCapacityIfFull() {
        int r0 = this.values.length;
        if (this.size >= r0) goto L5;
        return;
    L5:
        int r1 = r0 * 2;
        long[] r2 = new long[r1];
        V[] r3 = (V[]) newArray(r1);
        int r4 = r0 - this.first;
        System.arraycopy(this.timestamps, this.first, r2, 0, r4);
        System.arraycopy(this.values, this.first, r3, 0, r4);
        if (this.first <= 0) goto L8;
        System.arraycopy(this.timestamps, 0, r2, r4, this.first);
        System.arraycopy(this.values, 0, r3, r4, this.first);
    L8:
        this.timestamps = r2;
        this.values = r3;
        this.first = 0;
    }

    private static <V> V[] newArray(int r1) {
        return (V[]) new Object[r1];
    }

    private V poll(long r9, boolean r11) {
        V r0 = null;
        long r1 = Long.MAX_VALUE;
    L4:
        if (this.size <= 0) goto L12;
        long r4 = r9 - this.timestamps[this.first];
        if (r4 >= 0) goto L11;
        if (r11 == true) goto L12;
        if ((-r4) >= r1) goto L12;
    L11:
        r1 = r4;
        r0 = popFirst();
    L12:
        return r0;
    }

    private V popFirst() {
        if (this.size <= 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        V r1 = this.values[this.first];
        this.values[this.first] = null;
        this.first = (this.first + 1) % this.values.length;
        this.size--;
        return r1;
    L5:
        r0 = false;
        goto L6
    }

    public synchronized void add(long r1, V r3) {
        monitor-enter(this);
        clearBufferOnTimeDiscontinuity(r1);     // Catch: Throwable -> L6
        doubleCapacityIfFull();     // Catch: Throwable -> L6
        addUnchecked(r1, r3);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized void clear() {
        monitor-enter(this);
        this.first = 0;     // Catch: Throwable -> L7
        this.size = 0;     // Catch: Throwable -> L7
        Arrays.fill(this.values, null);     // Catch: Throwable -> L7
        monitor-exit(this);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public synchronized V poll(long r2) {
        monitor-enter(this);
        V r0 = poll(r2, false);     // Catch: Throwable -> L7
        monitor-exit(this);
        return r0;
    L7:
        th = move-exception;
        throw th;
    }

    public synchronized V pollFirst() {
        monitor-enter(this);
    L9:
        th = move-exception;
        throw th;
    L4:
        if (this.size != 0) goto L6;
        V r0 = null;
    L7:
        monitor-exit(this);
        return r0;
    L6:
        r0 = popFirst();     // Catch: Throwable -> L9
        goto L7
    }

    public synchronized V pollFloor(long r2) {
        monitor-enter(this);
        V r0 = poll(r2, true);     // Catch: Throwable -> L7
        monitor-exit(this);
        return r0;
    L7:
        th = move-exception;
        throw th;
    }

    public synchronized int size() {
        monitor-enter(this);
        int r0 = this.size;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }
}
