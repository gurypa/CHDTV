package com.google.common.math;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class StatsAccumulator {
    private long count;
    private double max;
    private double mean;
    private double min;
    private double sumOfSquaresOfDeltas;

    public StatsAccumulator() {
        this.count = 0;
        this.mean = 0.0d;
        this.sumOfSquaresOfDeltas = 0.0d;
        this.min = Double.NaN;
        this.max = Double.NaN;
    }

    static double calculateNewMeanNonFinite(double r2, double r4) {
        if (Doubles.isFinite(r2) == false) goto L6;
        return r4;
    L6:
        if (Doubles.isFinite(r4) == false) goto L8;
    L12:
        return r2;
    L8:
        if (r2 == r4) goto L12;
        return Double.NaN;
    }

    private void merge(long r18, double r20, double r22, double r24, double r26) {
        if (this.count != 0) goto L5;
        this.count = r18;
        this.mean = r20;
        this.sumOfSquaresOfDeltas = r22;
        this.min = r24;
        this.max = r26;
        return;
    L5:
        this.count += r18;
        if (Doubles.isFinite(this.mean) == true) goto L8;
    L10:
        this.mean = calculateNewMeanNonFinite(this.mean, r20);
        this.sumOfSquaresOfDeltas = Double.NaN;
    L11:
        this.min = Math.min(this.min, r24);
        this.max = Math.max(this.max, r26);
        return;
    L8:
        if (Doubles.isFinite(r20) == false) goto L10;
        double r11 = r20 - this.mean;
        this.mean += (r18 * r11) / this.count;
        this.sumOfSquaresOfDeltas = (r22 + (((r20 - this.mean) * r11) * r18)) + this.sumOfSquaresOfDeltas;
        goto L11
    }

    public void add(double r8) {
        if (this.count != 0) goto L7;
        this.count = 1;
        this.mean = r8;
        this.min = r8;
        this.max = r8;
        if (Doubles.isFinite(r8) == true) goto L15;
        this.sumOfSquaresOfDeltas = Double.NaN;
        return;
    L15:
        return;
    L7:
        this.count++;
        if (Doubles.isFinite(r8) == true) goto L10;
    L12:
        this.mean = calculateNewMeanNonFinite(this.mean, r8);
        this.sumOfSquaresOfDeltas = Double.NaN;
    L13:
        this.min = Math.min(this.min, r8);
        this.max = Math.max(this.max, r8);
        return;
    L10:
        if (Doubles.isFinite(this.mean) == false) goto L12;
        double r0 = r8 - this.mean;
        this.mean += r0 / this.count;
        this.sumOfSquaresOfDeltas += (r8 - this.mean) * r0;
        goto L13
    }

    public void addAll(Stats r13) {
        if (r13.count() != 0) goto L5;
        return;
    L5:
        merge(r13.count(), r13.mean(), r13.sumOfSquaresOfDeltas(), r13.min(), r13.max());
    }

    public void addAll(StatsAccumulator r13) {
        if (r13.count() != 0) goto L5;
        return;
    L5:
        merge(r13.count(), r13.mean(), r13.sumOfSquaresOfDeltas(), r13.min(), r13.max());
    }

    public void addAll(Iterable<? extends Number> r5) {
        Iterator<? extends Number> r0 = r5.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        add(r0.next().doubleValue());
        goto L4
    }

    public void addAll(Iterator<? extends Number> r3) {
    L3:
        if (r3.hasNext() == false) goto L5;
        add(r3.next().doubleValue());
        goto L3
    }

    public void addAll(double... r5) {
        int r0 = r5.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        add(r5[r1]);
        r1 = r1 + 1;
        goto L3
    }

    public void addAll(int... r6) {
        int r0 = r6.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        add(r6[r1]);
        r1 = r1 + 1;
        goto L3
    }

    public void addAll(long... r7) {
        int r0 = r7.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        add(r7[r1]);
        r1 = r1 + 1;
        goto L3
    }

    public long count() {
        return this.count;
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

    public final double populationStandardDeviation() {
        return Math.sqrt(populationVariance());
    }

    public final double populationVariance() {
        if (this.count == 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        if (Double.isNaN(this.sumOfSquaresOfDeltas) == false) goto L11;
        return Double.NaN;
    L11:
        if (this.count != 1) goto L15;
        return 0.0d;
    L15:
        return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / this.count;
    L5:
        r0 = false;
        goto L6
    }

    public final double sampleStandardDeviation() {
        return Math.sqrt(sampleVariance());
    }

    public final double sampleVariance() {
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

    public Stats snapshot() {
        return new Stats(this.count, this.mean, this.sumOfSquaresOfDeltas, this.min, this.max);
    }

    public final double sum() {
        return this.mean * this.count;
    }

    double sumOfSquaresOfDeltas() {
        return this.sumOfSquaresOfDeltas;
    }
}
