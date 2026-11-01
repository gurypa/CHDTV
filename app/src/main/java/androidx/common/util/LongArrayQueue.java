package androidx.media3.common.util;

import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes.dex */
public final class LongArrayQueue {
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    private long[] data;
    private int headIndex;
    private int size;
    private int tailIndex;
    private int wrapAroundMask;

    public LongArrayQueue() {
        this(16);
    }

    public LongArrayQueue(int r4) {
        if (r4 >= 0) goto L5;
    L7:
        boolean r2 = false;
    L8:
        Assertions.checkArgument(r2);
        if (r4 != 0) goto L11;
        int r3 = 1;
    L13:
        if (Integer.bitCount(r3) == 1) goto L15;
        r3 = Integer.highestOneBit(r3 - 1) << 1;
    L15:
        this.headIndex = 0;
        this.tailIndex = -1;
        this.size = 0;
        this.data = new long[r3];
        this.wrapAroundMask = this.data.length - 1;
        return;
    L11:
        r3 = r4;
        goto L13
    L5:
        if (r4 > 1073741824) goto L7;
        r2 = true;
        goto L8
    }

    private void doubleArraySize() {
        int r0 = this.data.length << 1;
        if (r0 < 0) goto L7;
        long[] r1 = new long[r0];
        int r2 = this.data.length - this.headIndex;
        int r3 = this.headIndex;
        System.arraycopy(this.data, this.headIndex, r1, 0, r2);
        System.arraycopy(this.data, 0, r1, r2, r3);
        this.headIndex = 0;
        this.tailIndex = this.size - 1;
        this.data = r1;
        this.wrapAroundMask = this.data.length - 1;
        return;
    L7:
        throw new IllegalStateException();
    }

    public void add(long r3) {
        if (this.size != this.data.length) goto L5;
        doubleArraySize();
    L5:
        this.tailIndex = (this.tailIndex + 1) & this.wrapAroundMask;
        this.data[this.tailIndex] = r3;
        this.size++;
    }

    int capacity() {
        return this.data.length;
    }

    public void clear() {
        this.headIndex = 0;
        this.tailIndex = -1;
        this.size = 0;
    }

    public long element() {
        if (this.size == 0) goto L7;
        return this.data[this.headIndex];
    L7:
        throw new NoSuchElementException();
    }

    public boolean isEmpty() {
        if (this.size != 0) goto L5;
        return true;
    L5:
        return false;
    }

    public long remove() {
        if (this.size == 0) goto L7;
        long r1 = this.data[this.headIndex];
        this.headIndex = (this.headIndex + 1) & this.wrapAroundMask;
        this.size--;
        return r1;
    L7:
        throw new NoSuchElementException();
    }

    public int size() {
        return this.size;
    }
}
