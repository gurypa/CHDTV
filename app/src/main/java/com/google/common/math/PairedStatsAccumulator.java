package com.google.common.math;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class PairedStatsAccumulator {
    private double sumOfProductsOfDeltas;
    private final StatsAccumulator xStats;
    private final StatsAccumulator yStats;

    public PairedStatsAccumulator() {
        this.xStats = new StatsAccumulator();
        this.yStats = new StatsAccumulator();
        this.sumOfProductsOfDeltas = 0.0d;
    }

    private static double ensureInUnitRange(double r6) {
        return Doubles.constrainToRange(r6, -1.0d, 1.0d);
    }

    private double ensurePositive(double r3) {
        if (r3 <= 0.0d) goto L5;
        return r3;
    L5:
        return Double.MIN_VALUE;
    }

    public void add(double r7, double r9) {
        this.xStats.add(r7);
        if (Doubles.isFinite(r7) == true) goto L5;
    L9:
        this.sumOfProductsOfDeltas = Double.NaN;
    L10:
        this.yStats.add(r9);
        return;
    L5:
        if (Doubles.isFinite(r9) == false) goto L9;
        if (this.xStats.count() <= 1) goto L10;
        this.sumOfProductsOfDeltas += (r7 - this.xStats.mean()) * (r9 - this.yStats.mean());
        goto L10
    }

    public void addAll(PairedStats r11) {
        if (r11.count() != 0) goto L5;
        return;
    L5:
        this.xStats.addAll(r11.xStats());
        if (this.yStats.count() != 0) goto L8;
        this.sumOfProductsOfDeltas = r11.sumOfProductsOfDeltas();
    L9:
        this.yStats.addAll(r11.yStats());
        return;
    L8:
        this.sumOfProductsOfDeltas += r11.sumOfProductsOfDeltas() + (((r11.xStats().mean() - this.xStats.mean()) * (r11.yStats().mean() - this.yStats.mean())) * r11.count());
        goto L9
    }

    public long count() {
        return this.xStats.count();
    }

    public final LinearTransformation leastSquaresFit() {
        boolean r1 = true;
        if (count() <= 1) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        if (Double.isNaN(this.sumOfProductsOfDeltas) == true) goto L9;
        double r3 = this.xStats.sumOfSquaresOfDeltas();
        if (r3 <= 0.0d) goto L19;
        if (this.yStats.sumOfSquaresOfDeltas() <= 0.0d) goto L17;
        return LinearTransformation.mapping(this.xStats.mean(), this.yStats.mean()).withSlope(this.sumOfProductsOfDeltas / r3);
    L17:
        return LinearTransformation.horizontal(this.yStats.mean());
    L19:
        if (this.yStats.sumOfSquaresOfDeltas() > 0.0d) goto L22;
        r1 = false;
    L22:
        Preconditions.checkState(r1);
        return LinearTransformation.vertical(this.xStats.mean());
    L9:
        return LinearTransformation.forNaN();
    L5:
        r0 = false;
        goto L6
    }

    public final double pearsonsCorrelationCoefficient() {
        boolean r1 = true;
        if (count() <= 1) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        if (Double.isNaN(this.sumOfProductsOfDeltas) == false) goto L10;
        return Double.NaN;
    L10:
        double r3 = this.xStats.sumOfSquaresOfDeltas();
        double r5 = this.yStats.sumOfSquaresOfDeltas();
        if (r3 <= 0.0d) goto L13;
        boolean r2 = true;
    L14:
        Preconditions.checkState(r2);
        if (r5 > 0.0d) goto L18;
        r1 = false;
    L18:
        Preconditions.checkState(r1);
        return ensureInUnitRange(this.sumOfProductsOfDeltas / Math.sqrt(ensurePositive(r3 * r5)));
    L13:
        r2 = false;
        goto L14
    L5:
        r0 = false;
        goto L6
    }

    public double populationCovariance() {
        if (count() == 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        return this.sumOfProductsOfDeltas / count();
    L5:
        r0 = false;
        goto L6
    }

    public final double sampleCovariance() {
        if (count() <= 1) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        return this.sumOfProductsOfDeltas / (count() - 1);
    L5:
        r0 = false;
        goto L6
    }

    public PairedStats snapshot() {
        return new PairedStats(this.xStats.snapshot(), this.yStats.snapshot(), this.sumOfProductsOfDeltas);
    }

    public Stats xStats() {
        return this.xStats.snapshot();
    }

    public Stats yStats() {
        return this.yStats.snapshot();
    }
}
