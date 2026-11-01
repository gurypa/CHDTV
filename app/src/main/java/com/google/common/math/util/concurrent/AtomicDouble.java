package com.google.common.util.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class AtomicDouble extends Number implements Serializable {
    private static final long serialVersionUID = 0;
    private transient AtomicLong value;

    public AtomicDouble() {
        this(0.0d);
    }

    public AtomicDouble(double r4) {
        this.value = new AtomicLong(Double.doubleToRawLongBits(r4));
    }

    private void readObject(ObjectInputStream r3) throws IOException, ClassNotFoundException {
        r3.defaultReadObject();
        this.value = new AtomicLong();
        set(r3.readDouble());
    }

    private void writeObject(ObjectOutputStream r3) throws IOException {
        r3.defaultWriteObject();
        r3.writeDouble(get());
    }

    public final double addAndGet(double r10) {
    L2:
        long r0 = this.value.get();
        double r4 = Double.longBitsToDouble(r0) + r10;
        long r6 = Double.doubleToRawLongBits(r4);
        if (this.value.compareAndSet(r0, r6) == false) goto L2;
        return r4;
    }

    public final boolean compareAndSet(double r6, double r8) {
        return this.value.compareAndSet(Double.doubleToRawLongBits(r6), Double.doubleToRawLongBits(r8));
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return get();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return (float) get();
    }

    public final double get() {
        return Double.longBitsToDouble(this.value.get());
    }

    public final double getAndAdd(double r10) {
    L2:
        long r0 = this.value.get();
        double r2 = Double.longBitsToDouble(r0);
        long r6 = Double.doubleToRawLongBits(r2 + r10);
        if (this.value.compareAndSet(r0, r6) == false) goto L2;
        return r2;
    }

    public final double getAndSet(double r5) {
        long r0 = Double.doubleToRawLongBits(r5);
        return Double.longBitsToDouble(this.value.getAndSet(r0));
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) get();
    }

    public final void lazySet(double r4) {
        long r0 = Double.doubleToRawLongBits(r4);
        this.value.lazySet(r0);
    }

    @Override // java.lang.Number
    public long longValue() {
        return (long) get();
    }

    public final void set(double r4) {
        long r0 = Double.doubleToRawLongBits(r4);
        this.value.set(r0);
    }

    public String toString() {
        return Double.toString(get());
    }

    public final boolean weakCompareAndSet(double r6, double r8) {
        return this.value.weakCompareAndSet(Double.doubleToRawLongBits(r6), Double.doubleToRawLongBits(r8));
    }
}
