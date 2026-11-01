package com.google.common.util.concurrent;

import com.google.common.primitives.ImmutableLongArray;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongArray;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class AtomicDoubleArray implements Serializable {
    private static final long serialVersionUID = 0;
    private transient AtomicLongArray longs;

    public AtomicDoubleArray(int r2) {
        this.longs = new AtomicLongArray(r2);
    }

    public AtomicDoubleArray(double[] r6) {
        int r0 = r6.length;
        long[] r1 = new long[r0];
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L5;
        r1[r2] = Double.doubleToRawLongBits(r6[r2]);
        r2 = r2 + 1;
        goto L3
    L5:
        this.longs = new AtomicLongArray(r1);
    }

    private void readObject(ObjectInputStream r6) throws IOException, ClassNotFoundException {
        r6.defaultReadObject();
        int r0 = r6.readInt();
        ImmutableLongArray.Builder r1 = ImmutableLongArray.builder();
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L5;
        r1.add(Double.doubleToRawLongBits(r6.readDouble()));
        r2 = r2 + 1;
        goto L3
    L5:
        this.longs = new AtomicLongArray(r1.build().toArray());
    }

    private void writeObject(ObjectOutputStream r5) throws IOException {
        r5.defaultWriteObject();
        int r0 = length();
        r5.writeInt(r0);
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        r5.writeDouble(get(r1));
        r1 = r1 + 1;
        goto L3
    }

    public double addAndGet(int r12, double r13) {
    L2:
        long r3 = this.longs.get(r12);
        double r9 = Double.longBitsToDouble(r3) + r13;
        long r5 = Double.doubleToRawLongBits(r9);
        int r2 = r12;
        if (this.longs.compareAndSet(r2, r3, r5) == true) goto L4;
        r12 = r2;
        goto L2
    L4:
        return r9;
    }

    public final boolean compareAndSet(int r7, double r8, double r10) {
        return this.longs.compareAndSet(r7, Double.doubleToRawLongBits(r8), Double.doubleToRawLongBits(r10));
    }

    public final double get(int r3) {
        return Double.longBitsToDouble(this.longs.get(r3));
    }

    public final double getAndAdd(int r12, double r13) {
    L2:
        long r3 = this.longs.get(r12);
        double r7 = Double.longBitsToDouble(r3);
        long r5 = Double.doubleToRawLongBits(r7 + r13);
        int r2 = r12;
        if (this.longs.compareAndSet(r2, r3, r5) == true) goto L4;
        r12 = r2;
        goto L2
    L4:
        return r7;
    }

    public final double getAndSet(int r5, double r6) {
        long r0 = Double.doubleToRawLongBits(r6);
        return Double.longBitsToDouble(this.longs.getAndSet(r5, r0));
    }

    public final void lazySet(int r4, double r5) {
        long r0 = Double.doubleToRawLongBits(r5);
        this.longs.lazySet(r4, r0);
    }

    public final int length() {
        return this.longs.length();
    }

    public final void set(int r4, double r5) {
        long r0 = Double.doubleToRawLongBits(r5);
        this.longs.set(r4, r0);
    }

    public String toString() {
        int r0 = length() - 1;
        if (r0 != (-1)) goto L6;
        return "[]";
    L6:
        StringBuilder r1 = new StringBuilder((r0 + 1) * 19);
        r1.append('[');
        int r2 = 0;
    L7:
        r1.append(Double.longBitsToDouble(this.longs.get(r2)));
        if (r2 == r0) goto L10;
        r1.append(',').append(' ');
        r2 = r2 + 1;
        goto L7
    L10:
        return r1.append(']').toString();
    }

    public final boolean weakCompareAndSet(int r7, double r8, double r10) {
        return this.longs.weakCompareAndSet(r7, Double.doubleToRawLongBits(r8), Double.doubleToRawLongBits(r10));
    }
}
