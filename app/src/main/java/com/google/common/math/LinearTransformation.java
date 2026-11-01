package com.google.common.math;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class LinearTransformation {

    /* JADX INFO: renamed from: com.google.common.math.LinearTransformation$1 */
    static /* synthetic */ class C08611 {
    }

    public static final class LinearTransformationBuilder {

        /* JADX INFO: renamed from: x1 */
        private final double f163x1;

        /* JADX INFO: renamed from: y1 */
        private final double f164y1;

        private LinearTransformationBuilder(double r1, double r3) {
            this.f163x1 = r1;
            this.f164y1 = r3;
        }

        /* synthetic */ LinearTransformationBuilder(double r1, double r3, C08611 r5) {
            this(r1, r3);
        }

        public LinearTransformation and(double r6, double r8) {
            boolean r1 = true;
            if (DoubleUtils.isFinite(r6) == true) goto L5;
        L7:
            boolean r0 = false;
        L8:
            Preconditions.checkArgument(r0);
            if (r6 != this.f163x1) goto L17;
            if (r8 != this.f164y1) goto L14;
            r1 = false;
        L14:
            Preconditions.checkArgument(r1);
            return new VerticalLinearTransformation(this.f163x1);
        L17:
            return withSlope((r8 - this.f164y1) / (r6 - this.f163x1));
        L5:
            if (DoubleUtils.isFinite(r8) == false) goto L7;
            r0 = true;
            goto L8
        }

        public LinearTransformation withSlope(double r5) {
            Preconditions.checkArgument(!Double.isNaN(r5));
            if (DoubleUtils.isFinite(r5) == false) goto L7;
            return new RegularLinearTransformation(r5, this.f164y1 - (this.f163x1 * r5));
        L7:
            return new VerticalLinearTransformation(this.f163x1);
        }
    }

    private static final class NaNLinearTransformation extends LinearTransformation {
        static final NaNLinearTransformation INSTANCE = null;

        static {
            INSTANCE = new NaNLinearTransformation();
        }

        private NaNLinearTransformation() {
        }

        @Override // com.google.common.math.LinearTransformation
        public LinearTransformation inverse() {
            return this;
        }

        @Override // com.google.common.math.LinearTransformation
        public boolean isHorizontal() {
            return false;
        }

        @Override // com.google.common.math.LinearTransformation
        public boolean isVertical() {
            return false;
        }

        @Override // com.google.common.math.LinearTransformation
        public double slope() {
            return Double.NaN;
        }

        public String toString() {
            return "NaN";
        }

        @Override // com.google.common.math.LinearTransformation
        public double transform(double r3) {
            return Double.NaN;
        }
    }

    private static final class RegularLinearTransformation extends LinearTransformation {

        @CheckForNull
        @LazyInit
        LinearTransformation inverse;
        final double slope;
        final double yIntercept;

        RegularLinearTransformation(double r2, double r4) {
            this.slope = r2;
            this.yIntercept = r4;
            this.inverse = null;
        }

        RegularLinearTransformation(double r1, double r3, LinearTransformation r5) {
            this.slope = r1;
            this.yIntercept = r3;
            this.inverse = r5;
        }

        private LinearTransformation createInverse() {
            if (this.slope == 0.0d) goto L7;
            return new RegularLinearTransformation(1.0d / this.slope, (this.yIntercept * (-1.0d)) / this.slope, this);
        L7:
            return new VerticalLinearTransformation(this.yIntercept, this);
        }

        @Override // com.google.common.math.LinearTransformation
        public LinearTransformation inverse() {
            LinearTransformation r0 = this.inverse;
            if (r0 != null) goto L6;
            LinearTransformation r1 = createInverse();
            this.inverse = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // com.google.common.math.LinearTransformation
        public boolean isHorizontal() {
            if (this.slope != 0.0d) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.math.LinearTransformation
        public boolean isVertical() {
            return false;
        }

        @Override // com.google.common.math.LinearTransformation
        public double slope() {
            return this.slope;
        }

        public String toString() {
            return String.format("y = %g * x + %g", new Object[]{Double.valueOf(this.slope), Double.valueOf(this.yIntercept)});
        }

        @Override // com.google.common.math.LinearTransformation
        public double transform(double r5) {
            return (this.slope * r5) + this.yIntercept;
        }
    }

    private static final class VerticalLinearTransformation extends LinearTransformation {

        @CheckForNull
        @LazyInit
        LinearTransformation inverse;

        /* JADX INFO: renamed from: x */
        final double f165x;

        VerticalLinearTransformation(double r2) {
            this.f165x = r2;
            this.inverse = null;
        }

        VerticalLinearTransformation(double r1, LinearTransformation r3) {
            this.f165x = r1;
            this.inverse = r3;
        }

        private LinearTransformation createInverse() {
            return new RegularLinearTransformation(0.0d, this.f165x, this);
        }

        @Override // com.google.common.math.LinearTransformation
        public LinearTransformation inverse() {
            LinearTransformation r0 = this.inverse;
            if (r0 != null) goto L6;
            LinearTransformation r1 = createInverse();
            this.inverse = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // com.google.common.math.LinearTransformation
        public boolean isHorizontal() {
            return false;
        }

        @Override // com.google.common.math.LinearTransformation
        public boolean isVertical() {
            return true;
        }

        @Override // com.google.common.math.LinearTransformation
        public double slope() {
            throw new IllegalStateException();
        }

        public String toString() {
            return String.format("x = %g", new Object[]{Double.valueOf(this.f165x)});
        }

        @Override // com.google.common.math.LinearTransformation
        public double transform(double r2) {
            throw new IllegalStateException();
        }
    }

    public LinearTransformation() {
    }

    public static LinearTransformation forNaN() {
        return NaNLinearTransformation.INSTANCE;
    }

    public static LinearTransformation horizontal(double r3) {
        Preconditions.checkArgument(DoubleUtils.isFinite(r3));
        return new RegularLinearTransformation(0.0d, r3);
    }

    public static LinearTransformationBuilder mapping(double r7, double r9) {
        if (DoubleUtils.isFinite(r7) == true) goto L5;
    L7:
        boolean r0 = false;
    L8:
        Preconditions.checkArgument(r0);
        return new LinearTransformationBuilder(r7, r9, null);
    L5:
        if (DoubleUtils.isFinite(r9) == false) goto L7;
        r0 = true;
        goto L8
    }

    public static LinearTransformation vertical(double r1) {
        Preconditions.checkArgument(DoubleUtils.isFinite(r1));
        return new VerticalLinearTransformation(r1);
    }

    public abstract LinearTransformation inverse();

    public abstract boolean isHorizontal();

    public abstract boolean isVertical();

    public abstract double slope();

    public abstract double transform(double r1);
}
