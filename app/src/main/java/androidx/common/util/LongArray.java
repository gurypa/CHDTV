package androidx.media3.common.util;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class LongArray {
    private static final int DEFAULT_INITIAL_CAPACITY = 32;
    private int size;
    private long[] values;

    public LongArray() {
        this(32);
    }

    public LongArray(int r2) {
        this.values = new long[r2];
    }

    public void add(long r4) {
        if (this.size != this.values.length) goto L5;
        this.values = Arrays.copyOf(this.values, this.size * 2);
    L5:
        long[] r0 = this.values;
        int r1 = this.size;
        this.size = r1 + 1;
        r0[r1] = r4;
    }

    public void addAll(long[] r6) {
        int r0 = this.size + r6.length;
        if (r0 <= this.values.length) goto L5;
        this.values = Arrays.copyOf(this.values, Math.max(this.values.length * 2, r0));
    L5:
        System.arraycopy(r6, 0, this.values, this.size, r6.length);
        this.size = r0;
    }

    public long get(int r4) {
        if (r4 < 0) goto L8;
        if (r4 >= this.size) goto L8;
        return this.values[r4];
    L8:
        throw new IndexOutOfBoundsException("Invalid index " + r4 + ", size is " + this.size);
    }

    public int size() {
        return this.size;
    }

    public long[] toArray() {
        return Arrays.copyOf(this.values, this.size);
    }
}
