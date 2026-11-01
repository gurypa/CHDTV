package com.google.common.primitives;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.math.BigInteger;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class UnsignedLong extends Number implements Comparable<UnsignedLong>, Serializable {
    public static final UnsignedLong MAX_VALUE = null;
    public static final UnsignedLong ONE = null;
    private static final long UNSIGNED_MASK = Long.MAX_VALUE;
    public static final UnsignedLong ZERO = null;
    private final long value;

    static {
        ZERO = new UnsignedLong(0);
        ONE = new UnsignedLong(1);
        MAX_VALUE = new UnsignedLong(-1);
    }

    private UnsignedLong(long r1) {
        this.value = r1;
    }

    public static UnsignedLong fromLongBits(long r1) {
        return new UnsignedLong(r1);
    }

    public static UnsignedLong valueOf(long r2) {
        if (r2 < 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "value (%s) is outside the range for an unsigned long value", r2);
        return fromLongBits(r2);
    L5:
        r0 = false;
        goto L6
    }

    public static UnsignedLong valueOf(String r1) {
        return valueOf(r1, 10);
    }

    public static UnsignedLong valueOf(String r2, int r3) {
        return fromLongBits(UnsignedLongs.parseUnsignedLong(r2, r3));
    }

    public static UnsignedLong valueOf(BigInteger r2) {
        Preconditions.checkNotNull(r2);
        if (r2.signum() >= 0) goto L5;
    L7:
        boolean r0 = false;
    L8:
        Preconditions.checkArgument(r0, "value (%s) is outside the range for an unsigned long value", r2);
        return fromLongBits(r2.longValue());
    L5:
        if (r2.bitLength() > 64) goto L7;
        r0 = true;
        goto L8
    }

    public BigInteger bigIntegerValue() {
        BigInteger r0 = BigInteger.valueOf(this.value & Long.MAX_VALUE);
        if (this.value < 0) goto L5;
        return r0;
    L5:
        return r0.setBit(63);
    }

    /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
    public int compareTo2(UnsignedLong r5) {
        Preconditions.checkNotNull(r5);
        return UnsignedLongs.compare(this.value, r5.value);
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(UnsignedLong r1) {
        return compareTo2(r1);
    }

    public UnsignedLong dividedBy(UnsignedLong r5) {
        return fromLongBits(UnsignedLongs.divide(this.value, ((UnsignedLong) Preconditions.checkNotNull(r5)).value));
    }

    @Override // java.lang.Number
    public double doubleValue() {
        if (this.value < 0) goto L7;
        return this.value;
    L7:
        return ((this.value >>> 1) | (this.value & 1)) * 2.0d;
    }

    public boolean equals(@CheckForNull Object r7) {
        if ((r7 instanceof UnsignedLong) == true) goto L5;
        return false;
    L5:
        if (this.value != ((UnsignedLong) r7).value) goto L9;
        return true;
    L9:
        return false;
    }

    @Override // java.lang.Number
    public float floatValue() {
        if (this.value < 0) goto L7;
        return this.value;
    L7:
        return ((this.value >>> 1) | (this.value & 1)) * 2.0f;
    }

    public int hashCode() {
        return Longs.hashCode(this.value);
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) this.value;
    }

    @Override // java.lang.Number
    public long longValue() {
        return this.value;
    }

    public UnsignedLong minus(UnsignedLong r5) {
        return fromLongBits(this.value - ((UnsignedLong) Preconditions.checkNotNull(r5)).value);
    }

    public UnsignedLong mod(UnsignedLong r5) {
        return fromLongBits(UnsignedLongs.remainder(this.value, ((UnsignedLong) Preconditions.checkNotNull(r5)).value));
    }

    public UnsignedLong plus(UnsignedLong r5) {
        return fromLongBits(this.value + ((UnsignedLong) Preconditions.checkNotNull(r5)).value);
    }

    public UnsignedLong times(UnsignedLong r5) {
        return fromLongBits(this.value * ((UnsignedLong) Preconditions.checkNotNull(r5)).value);
    }

    public String toString() {
        return UnsignedLongs.toString(this.value);
    }

    public String toString(int r3) {
        return UnsignedLongs.toString(this.value, r3);
    }
}
