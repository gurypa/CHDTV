package com.google.common.hash;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class LongAdder extends Striped64 implements Serializable, LongAddable {
    private static final long serialVersionUID = 7249069246863182397L;

    public LongAdder() {
    }

    private void readObject(ObjectInputStream r3) throws IOException, ClassNotFoundException {
        r3.defaultReadObject();
        this.busy = 0;
        this.cells = null;
        this.base = r3.readLong();
    }

    private void writeObject(ObjectOutputStream r3) throws IOException {
        r3.defaultWriteObject();
        r3.writeLong(sum());
    }

    @Override // com.google.common.hash.LongAddable
    public void add(long r13) {
        Striped64.Cell[] r0 = this.cells;
        if (r0 != null) goto L6;
        long r2 = this.base;
        if (casBase(r2, r2 + r13) == false) goto L6;
        return;
    L6:
        boolean r1 = true;
        int[] r3 = threadHashCode.get();
        if (r3 == null) goto L15;
        if (r0 == null) goto L15;
        int r4 = r0.length;
        if (r4 < 1) goto L15;
        Striped64.Cell r5 = r0[(r4 - 1) & r3[0]];
        if (r5 == null) goto L15;
        long r6 = r5.value;
        boolean r7 = r5.cas(r6, r6 + r13);
        r1 = r7;
        if (r7 == false) goto L15;
        return;
    L15:
        retryUpdate(r13, r3, r1);
    }

    public void decrement() {
        add(-1);
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return sum();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return sum();
    }

    @Override // com.google.common.hash.Striped64
    /* JADX INFO: renamed from: fn */
    final long mo340fn(long r3, long r5) {
        return r3 + r5;
    }

    @Override // com.google.common.hash.LongAddable
    public void increment() {
        add(1);
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) sum();
    }

    @Override // java.lang.Number
    public long longValue() {
        return sum();
    }

    public void reset() {
        internalReset(0);
    }

    @Override // com.google.common.hash.LongAddable
    public long sum() {
        long r0 = this.base;
        Striped64.Cell[] r2 = this.cells;
        if (r2 == null) goto L10;
        int r3 = r2.length;
        int r4 = 0;
    L5:
        if (r4 >= r3) goto L10;
        Striped64.Cell r5 = r2[r4];
        if (r5 == null) goto L9;
        r0 = r0 + r5.value;
    L9:
        r4 = r4 + 1;
    L10:
        return r0;
    }

    public long sumThenReset() {
        long r0 = this.base;
        Striped64.Cell[] r2 = this.cells;
        this.base = 0;
        if (r2 == null) goto L10;
        int r5 = r2.length;
        int r6 = 0;
    L5:
        if (r6 >= r5) goto L10;
        Striped64.Cell r7 = r2[r6];
        if (r7 == null) goto L9;
        r0 = r0 + r7.value;
        r7.value = 0;
    L9:
        r6 = r6 + 1;
    L10:
        return r0;
    }

    public String toString() {
        return Long.toString(sum());
    }
}
