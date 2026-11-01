package com.google.common.math;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class PairedStats implements Serializable {
    private static final int BYTES = 88;
    private static final long serialVersionUID = 0;
    private final double sumOfProductsOfDeltas;
    private final Stats xStats;
    private final Stats yStats;

    PairedStats(Stats r1, Stats r2, double r3) {
        this.xStats = r1;
        this.yStats = r2;
        this.sumOfProductsOfDeltas = r3;
    }

    private static double ensureInUnitRange(double r3) {
        if (r3 < 1.0d) goto L6;
        return 1.0d;
    L6:
        if (r3 > (-1.0d)) goto L8;
        return -1.0d;
    L8:
        return r3;
    }

    private static double ensurePositive(double r2) {
        if (r2 <= 0.0d) goto L5;
        return r2;
    L5:
        return Double.MIN_VALUE;
    }

    public static PairedStats fromByteArray(byte[] r6) {
        Preconditions.checkNotNull(r6);
        if (r6.length != BYTES) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "Expected PairedStats.BYTES = %s, got %s", BYTES, r6.length);
        ByteBuffer r1 = ByteBuffer.wrap(r6).order(ByteOrder.LITTLE_ENDIAN);
        return new PairedStats(Stats.readFrom(r1), Stats.readFrom(r1), r1.getDouble());
    L5:
        r0 = false;
        goto L6
    }

    public long count() {
        return this.xStats.count();
    }

    public boolean equals(@CheckForNull Object r7) {
        if (r7 != null) goto L6;
        return false;
    L6:
        if (getClass() == r7.getClass()) goto L8;
        return false;
    L8:
        PairedStats r1 = (PairedStats) r7;
        if (this.xStats.equals(r1.xStats) == true) goto L11;
        return false;
    L11:
        if (this.yStats.equals(r1.yStats) == true) goto L13;
        return false;
    L13:
        if (Double.doubleToLongBits(this.sumOfProductsOfDeltas) != Double.doubleToLongBits(r1.sumOfProductsOfDeltas)) goto L19;
        return true;
    L19:
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{this.xStats, this.yStats, Double.valueOf(this.sumOfProductsOfDeltas)});
    }

    public LinearTransformation leastSquaresFit() {
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

    public double pearsonsCorrelationCoefficient() {
        boolean r1 = true;
        if (count() <= 1) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        if (Double.isNaN(this.sumOfProductsOfDeltas) == false) goto L10;
        return Double.NaN;
    L10:
        double r3 = xStats().sumOfSquaresOfDeltas();
        double r5 = yStats().sumOfSquaresOfDeltas();
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

    public double sampleCovariance() {
        if (count() <= 1) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        return this.sumOfProductsOfDeltas / (count() - 1);
    L5:
        r0 = false;
        goto L6
    }

    double sumOfProductsOfDeltas() {
        return this.sumOfProductsOfDeltas;
    }

    public byte[] toByteArray() {
        ByteBuffer r0 = ByteBuffer.allocate(BYTES).order(ByteOrder.LITTLE_ENDIAN);
        this.xStats.writeTo(r0);
        this.yStats.writeTo(r0);
        r0.putDouble(this.sumOfProductsOfDeltas);
        return r0.array();
    }

    public String toString() {
        if (count() <= 0) goto L7;
        return MoreObjects.toStringHelper(this).add("xStats", this.xStats).add("yStats", this.yStats).add("populationCovariance", populationCovariance()).toString();
    L7:
        return MoreObjects.toStringHelper(this).add("xStats", this.xStats).add("yStats", this.yStats).toString();
    }

    public Stats xStats() {
        return this.xStats;
    }

    public Stats yStats() {
        return this.yStats;
    }
}
