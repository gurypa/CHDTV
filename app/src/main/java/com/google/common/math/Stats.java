package com.google.common.math;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Stats implements Serializable {
    static final int BYTES = 40;
    private static final long serialVersionUID = 0;
    private final long count;
    private final double max;
    private final double mean;
    private final double min;
    private final double sumOfSquaresOfDeltas;

    Stats(long r1, double r3, double r5, double r7, double r9) {
        this.count = r1;
        this.mean = r3;
        this.sumOfSquaresOfDeltas = r5;
        this.min = r7;
        this.max = r9;
    }

    public static Stats fromByteArray(byte[] r4) {
        Preconditions.checkNotNull(r4);
        if (r4.length != 40) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "Expected Stats.BYTES = %s remaining , got %s", 40, r4.length);
        return readFrom(ByteBuffer.wrap(r4).order(ByteOrder.LITTLE_ENDIAN));
    L5:
        r0 = false;
        goto L6
    }

    public static double meanOf(Iterable<? extends Number> r2) {
        return meanOf(r2.iterator());
    }

    public static double meanOf(Iterator<? extends Number> r10) {
        Preconditions.checkArgument(r10.hasNext());
        long r0 = 1;
        double r2 = r10.next().doubleValue();
    L4:
        if (r10.hasNext() == false) goto L12;
        double r4 = r10.next().doubleValue();
        r0 = r0 + 1;
        if (Doubles.isFinite(r4) == false) goto L10;
        if (Doubles.isFinite(r2) == false) goto L10;
        r2 = r2 + ((r4 - r2) / r0);
    L10:
        r2 = StatsAccumulator.calculateNewMeanNonFinite(r2, r4);
        goto L4
    L12:
        return r2;
    }

    public static double meanOf(double... r9) {
        if (r9.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        double r1 = r9[0];
        int r2 = 1;
    L8:
        if (r2 >= r9.length) goto L16;
        double r3 = r9[r2];
        if (Doubles.isFinite(r3) == true) goto L12;
    L14:
        r1 = StatsAccumulator.calculateNewMeanNonFinite(r1, r3);
    L15:
        r2 = r2 + 1;
        goto L8
    L12:
        if (Doubles.isFinite(r1) == false) goto L14;
        r1 = r1 + ((r3 - r1) / ((double) (r2 + 1)));
        goto L15
    L16:
        return r1;
    L5:
        r0 = false;
        goto L6
    }

    public static double meanOf(int... r9) {
        if (r9.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        double r1 = r9[0];
        int r2 = 1;
    L8:
        if (r2 >= r9.length) goto L16;
        double r3 = r9[r2];
        if (Doubles.isFinite(r3) == true) goto L12;
    L14:
        r1 = StatsAccumulator.calculateNewMeanNonFinite(r1, r3);
    L15:
        r2 = r2 + 1;
        goto L8
    L12:
        if (Doubles.isFinite(r1) == false) goto L14;
        r1 = r1 + ((r3 - r1) / ((double) (r2 + 1)));
        goto L15
    L16:
        return r1;
    L5:
        r0 = false;
        goto L6
    }

    public static double meanOf(long... r9) {
        if (r9.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        double r1 = r9[0];
        int r2 = 1;
    L8:
        if (r2 >= r9.length) goto L16;
        double r3 = r9[r2];
        if (Doubles.isFinite(r3) == true) goto L12;
    L14:
        r1 = StatsAccumulator.calculateNewMeanNonFinite(r1, r3);
    L15:
        r2 = r2 + 1;
        goto L8
    L12:
        if (Doubles.isFinite(r1) == false) goto L14;
        r1 = r1 + ((r3 - r1) / ((double) (r2 + 1)));
        goto L15
    L16:
        return r1;
    L5:
        r0 = false;
        goto L6
    }

    /* JADX INFO: renamed from: of */
    public static Stats m341of(Iterable<? extends Number> r2) {
        StatsAccumulator r0 = new StatsAccumulator();
        r0.addAll(r2);
        return r0.snapshot();
    }

    /* JADX INFO: renamed from: of */
    public static Stats m342of(Iterator<? extends Number> r2) {
        StatsAccumulator r0 = new StatsAccumulator();
        r0.addAll(r2);
        return r0.snapshot();
    }

    /* JADX INFO: renamed from: of */
    public static Stats m343of(double... r2) {
        StatsAccumulator r0 = new StatsAccumulator();
        r0.addAll(r2);
        return r0.snapshot();
    }

    /* JADX INFO: renamed from: of */
    public static Stats m344of(int... r2) {
        StatsAccumulator r0 = new StatsAccumulator();
        r0.addAll(r2);
        return r0.snapshot();
    }

    /* JADX INFO: renamed from: of */
    public static Stats m345of(long... r2) {
        StatsAccumulator r0 = new StatsAccumulator();
        r0.addAll(r2);
        return r0.snapshot();
    }

    static Stats readFrom(ByteBuffer r15) {
        Preconditions.checkNotNull(r15);
        if (r15.remaining() < 40) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "Expected at least Stats.BYTES = %s remaining , got %s", 40, r15.remaining());
        return new Stats(r15.getLong(), r15.getDouble(), r15.getDouble(), r15.getDouble(), r15.getDouble());
    L5:
        r0 = false;
        goto L6
    }

    public long count() {
        return this.count;
    }

    public boolean equals(@CheckForNull Object r7) {
        if (r7 != null) goto L6;
        return false;
    L6:
        if (getClass() == r7.getClass()) goto L8;
        return false;
    L8:
        Stats r1 = (Stats) r7;
        if (this.count == r1.count) goto L11;
        return false;
    L11:
        if (Double.doubleToLongBits(this.mean) == Double.doubleToLongBits(r1.mean)) goto L13;
        return false;
    L13:
        if (Double.doubleToLongBits(this.sumOfSquaresOfDeltas) == Double.doubleToLongBits(r1.sumOfSquaresOfDeltas)) goto L15;
        return false;
    L15:
        if (Double.doubleToLongBits(this.min) == Double.doubleToLongBits(r1.min)) goto L17;
        return false;
    L17:
        if (Double.doubleToLongBits(this.max) != Double.doubleToLongBits(r1.max)) goto L25;
        return true;
    L25:
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{Long.valueOf(this.count), Double.valueOf(this.mean), Double.valueOf(this.sumOfSquaresOfDeltas), Double.valueOf(this.min), Double.valueOf(this.max)});
    }

    public double max() {
        if (this.count == 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        return this.max;
    L5:
        r0 = false;
        goto L6
    }

    public double mean() {
        if (this.count == 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        return this.mean;
    L5:
        r0 = false;
        goto L6
    }

    public double min() {
        if (this.count == 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        return this.min;
    L5:
        r0 = false;
        goto L6
    }

    public double populationStandardDeviation() {
        return Math.sqrt(populationVariance());
    }

    public double populationVariance() {
        if (this.count <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        if (Double.isNaN(this.sumOfSquaresOfDeltas) == false) goto L11;
        return Double.NaN;
    L11:
        if (this.count != 1) goto L15;
        return 0.0d;
    L15:
        return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / count();
    L5:
        r0 = false;
        goto L6
    }

    public double sampleStandardDeviation() {
        return Math.sqrt(sampleVariance());
    }

    public double sampleVariance() {
        if (this.count <= 1) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        if (Double.isNaN(this.sumOfSquaresOfDeltas) == false) goto L11;
        return Double.NaN;
    L11:
        return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (this.count - 1);
    L5:
        r0 = false;
        goto L6
    }

    public double sum() {
        return this.mean * this.count;
    }

    double sumOfSquaresOfDeltas() {
        return this.sumOfSquaresOfDeltas;
    }

    public byte[] toByteArray() {
        ByteBuffer r0 = ByteBuffer.allocate(40).order(ByteOrder.LITTLE_ENDIAN);
        writeTo(r0);
        return r0.array();
    }

    public String toString() {
        if (count() <= 0) goto L7;
        return MoreObjects.toStringHelper(this).add("count", this.count).add("mean", this.mean).add("populationStandardDeviation", populationStandardDeviation()).add("min", this.min).add("max", this.max).toString();
    L7:
        return MoreObjects.toStringHelper(this).add("count", this.count).toString();
    }

    void writeTo(ByteBuffer r5) {
        Preconditions.checkNotNull(r5);
        if (r5.remaining() < 40) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "Expected at least Stats.BYTES = %s remaining , got %s", 40, r5.remaining());
        r5.putLong(this.count).putDouble(this.mean).putDouble(this.sumOfSquaresOfDeltas).putDouble(this.min).putDouble(this.max);
        return;
    L5:
        r0 = false;
        goto L6
    }
}
