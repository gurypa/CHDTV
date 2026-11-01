package com.google.common.math;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Quantiles {

    /* JADX INFO: renamed from: com.google.common.math.Quantiles$1 */
    static /* synthetic */ class C08651 {
    }

    public static final class Scale {
        private final int scale;

        private Scale(int r3) {
            if (r3 <= 0) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "Quantile scale must be positive");
            this.scale = r3;
            return;
        L5:
            r0 = false;
            goto L6
        }

        /* synthetic */ Scale(int r1, C08651 r2) {
            this(r1);
        }

        public ScaleAndIndex index(int r4) {
            return new ScaleAndIndex(this.scale, r4, null);
        }

        public ScaleAndIndexes indexes(Collection<Integer> r5) {
            return new ScaleAndIndexes(this.scale, Ints.toArray(r5), null);
        }

        public ScaleAndIndexes indexes(int... r5) {
            return new ScaleAndIndexes(this.scale, (int[]) r5.clone(), null);
        }
    }

    public static final class ScaleAndIndex {
        private final int index;
        private final int scale;

        private ScaleAndIndex(int r1, int r2) {
            Quantiles.access$300(r2, r1);
            this.scale = r1;
            this.index = r2;
        }

        /* synthetic */ ScaleAndIndex(int r1, int r2, C08651 r3) {
            this(r1, r2);
        }

        public double compute(Collection<? extends Number> r3) {
            return computeInPlace(Doubles.toArray(r3));
        }

        public double compute(double... r3) {
            return computeInPlace((double[]) r3.clone());
        }

        public double compute(int... r3) {
            return computeInPlace(Quantiles.access$500(r3));
        }

        public double compute(long... r3) {
            return computeInPlace(Quantiles.access$400(r3));
        }

        public double computeInPlace(double... r19) {
            if (r19.length <= 0) goto L5;
            boolean r2 = true;
        L6:
            Preconditions.checkArgument(r2, "Cannot calculate quantiles of an empty dataset");
            if (Quantiles.access$600(r19) == false) goto L10;
            return Double.NaN;
        L10:
            long r5 = ((long) this.index) * ((long) (r19.length - 1));
            int r3 = (int) LongMath.divide(r5, this.scale, RoundingMode.DOWN);
            int r7 = (int) (r5 - (((long) r3) * ((long) this.scale)));
            Quantiles.access$700(r3, r19, 0, r19.length - 1);
            if (r7 == 0) goto L13;
            Quantiles.access$700(r3 + 1, r19, r3 + 1, r19.length - 1);
            return Quantiles.access$800(r19[r3], r19[r3 + 1], r7, this.scale);
        L13:
            return r19[r3];
        L5:
            r2 = false;
            goto L6
        }
    }

    public static final class ScaleAndIndexes {
        private final int[] indexes;
        private final int scale;

        private ScaleAndIndexes(int r5, int[] r6) {
            int r0 = r6.length;
            boolean r1 = false;
            int r2 = 0;
        L3:
            if (r2 >= r0) goto L6;
            Quantiles.access$300(r6[r2], r5);
            r2 = r2 + 1;
            goto L3
        L6:
            if (r6.length <= 0) goto L8;
            r1 = true;
        L8:
            Preconditions.checkArgument(r1, "Indexes must be a non empty array");
            this.scale = r5;
            this.indexes = r6;
        }

        /* synthetic */ ScaleAndIndexes(int r1, int[] r2, C08651 r3) {
            this(r1, r2);
        }

        public Map<Integer, Double> compute(Collection<? extends Number> r2) {
            return computeInPlace(Doubles.toArray(r2));
        }

        public Map<Integer, Double> compute(double... r2) {
            return computeInPlace((double[]) r2.clone());
        }

        public Map<Integer, Double> compute(int... r2) {
            return computeInPlace(Quantiles.access$500(r2));
        }

        public Map<Integer, Double> compute(long... r2) {
            return computeInPlace(Quantiles.access$400(r2));
        }

        public Map<Integer, Double> computeInPlace(double... r20) {
            ScaleAndIndexes r0 = this;
            int r2 = 0;
            if (r20.length <= 0) goto L5;
            boolean r1 = true;
        L6:
            Preconditions.checkArgument(r1, "Cannot calculate quantiles of an empty dataset");
            if (Quantiles.access$600(r20) == false) goto L13;
            LinkedHashMap r3 = new LinkedHashMap();
            int[] r4 = r0.indexes;
            int r5 = r4.length;
        L9:
            if (r2 >= r5) goto L12;
            r3.put(Integer.valueOf(r4[r2]), Double.valueOf(Double.NaN));
            r2 = r2 + 1;
            goto L9
        L12:
            return Collections.unmodifiableMap(r3);
        L13:
            int[] r7 = new int[r0.indexes.length];
            int[] r8 = new int[r0.indexes.length];
            int[] r6 = new int[r0.indexes.length * 2];
            int r9 = 0;
            int r10 = 0;
        L15:
            if (r9 >= r0.indexes.length) goto L20;
            long r11 = ((long) r0.indexes[r9]) * ((long) (r20.length - 1));
            int r12 = (int) LongMath.divide(r11, r0.scale, RoundingMode.DOWN);
            int r13 = (int) (r11 - (((long) r12) * ((long) r0.scale)));
            r7[r9] = r12;
            r8[r9] = r13;
            r6[r10] = r12;
            r10 = r10 + 1;
            if (r13 == 0) goto L19;
            r6[r10] = r12 + 1;
            r10 = r10 + 1;
        L19:
            r9 = r9 + 1;
            goto L15
        L20:
            Arrays.sort(r6, 0, r10);
            Quantiles.access$900(r6, 0, r10 - 1, r20, 0, r20.length - 1);
            LinkedHashMap r14 = new LinkedHashMap();
            int r15 = 0;
        L22:
            if (r15 >= r0.indexes.length) goto L29;
            int r16 = r7[r15];
            int r17 = r8[r15];
            if (r17 != 0) goto L26;
            r14.put(Integer.valueOf(r0.indexes[r15]), Double.valueOf(r20[r16]));
        L27:
            r15 = r15 + 1;
            r0 = this;
            r6 = r6;
            goto L22
        L26:
            r14.put(Integer.valueOf(r0.indexes[r15]), Double.valueOf(Quantiles.access$800(r20[r16], r20[r16 + 1], r17, r0.scale)));
            goto L27
        L29:
            return Collections.unmodifiableMap(r14);
        L5:
            r1 = false;
            goto L6
        }
    }

    public Quantiles() {
    }

    static /* synthetic */ void access$300(int r0, int r1) {
        checkIndex(r0, r1);
    }

    static /* synthetic */ double[] access$400(long[] r1) {
        return longsToDoubles(r1);
    }

    static /* synthetic */ double[] access$500(int[] r1) {
        return intsToDoubles(r1);
    }

    static /* synthetic */ boolean access$600(double[] r1) {
        return containsNaN(r1);
    }

    static /* synthetic */ void access$700(int r0, double[] r1, int r2, int r3) {
        selectInPlace(r0, r1, r2, r3);
    }

    static /* synthetic */ double access$800(double r2, double r4, double r6, double r8) {
        return interpolate(r2, r4, r6, r8);
    }

    static /* synthetic */ void access$900(int[] r0, int r1, int r2, double[] r3, int r4, int r5) {
        selectAllInPlace(r0, r1, r2, r3, r4, r5);
    }

    private static void checkIndex(int r3, int r4) {
        if (r3 < 0) goto L6;
        if (r3 > r4) goto L6;
        return;
    L6:
        throw new IllegalArgumentException("Quantile indexes must be between 0 and the scale, which is " + r4);
    }

    private static int chooseNextSelection(int[] r5, int r6, int r7, int r8, int r9) {
        if (r6 != r7) goto L4;
        return r6;
    L4:
        int r0 = (r8 + r9) >>> 1;
        int r1 = r6;
        int r2 = r7;
    L6:
        if (r2 <= (r1 + 1)) goto L16;
        int r3 = (r1 + r2) >>> 1;
        if (r5[r3] > r0) goto L9;
        if (r5[r3] >= r0) goto L14;
        r1 = r3;
        goto L6
    L14:
        return r3;
    L9:
        r2 = r3;
        goto L6
    L16:
        if ((((r8 + r9) - r5[r1]) - r5[r2]) <= 0) goto L18;
        return r2;
    L18:
        return r1;
    }

    private static boolean containsNaN(double... r6) {
        int r0 = r6.length;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L9;
        if (Double.isNaN(r6[r2]) == true) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    private static double interpolate(double r5, double r7, double r9, double r11) {
        if (r5 != Double.NEGATIVE_INFINITY) goto L10;
        if (r7 != Double.POSITIVE_INFINITY) goto L8;
        return Double.NaN;
    L8:
        return Double.NEGATIVE_INFINITY;
    L10:
        if (r7 != Double.POSITIVE_INFINITY) goto L13;
        return Double.POSITIVE_INFINITY;
    L13:
        return (((r7 - r5) * r9) / r11) + r5;
    }

    private static double[] intsToDoubles(int[] r5) {
        int r0 = r5.length;
        double[] r1 = new double[r0];
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L5;
        r1[r2] = r5[r2];
        r2 = r2 + 1;
        goto L3
    L5:
        return r1;
    }

    private static double[] longsToDoubles(long[] r5) {
        int r0 = r5.length;
        double[] r1 = new double[r0];
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L5;
        r1[r2] = r5[r2];
        r2 = r2 + 1;
        goto L3
    L5:
        return r1;
    }

    public static ScaleAndIndex median() {
        return scale(2).index(1);
    }

    private static void movePivotToStartOfSlice(double[] r9, int r10, int r11) {
        boolean r1 = true;
        int r0 = (r10 + r11) >>> 1;
        if (r9[r11] >= r9[r0]) goto L5;
        boolean r2 = true;
    L7:
        if (r9[r0] >= r9[r10]) goto L9;
        boolean r4 = true;
    L11:
        if (r9[r11] < r9[r10]) goto L14;
        r1 = false;
    L14:
        if (r2 != r4) goto L16;
        swap(r9, r0, r10);
        return;
    L16:
        if (r2 == r1) goto L20;
        swap(r9, r10, r11);
        return;
    L20:
        return;
    L9:
        r4 = false;
        goto L11
    L5:
        r2 = false;
        goto L7
    }

    private static int partition(double[] r6, int r7, int r8) {
        movePivotToStartOfSlice(r6, r7, r8);
        double r0 = r6[r7];
        int r2 = r8;
        int r3 = r8;
    L3:
        if (r3 <= r7) goto L8;
        if (r6[r3] <= r0) goto L7;
        swap(r6, r2, r3);
        r2 = r2 - 1;
    L7:
        r3 = r3 - 1;
        goto L3
    L8:
        swap(r6, r7, r2);
        return r2;
    }

    public static Scale percentiles() {
        return scale(100);
    }

    public static Scale quartiles() {
        return scale(4);
    }

    public static Scale scale(int r2) {
        return new Scale(r2, null);
    }

    private static void selectAllInPlace(int[] r9, int r10, int r11, double[] r12, int r13, int r14) {
        int r0 = chooseNextSelection(r9, r10, r11, r13, r14);
        int r1 = r9[r0];
        selectInPlace(r1, r12, r13, r14);
        int r5 = r0 - 1;
    L3:
        if (r5 < r10) goto L7;
        if (r9[r5] != r1) goto L7;
        r5 = r5 - 1;
    L7:
        if (r5 < r10) goto L10;
        selectAllInPlace(r9, r10, r5, r12, r13, r1 - 1);
    L10:
        int r15 = r0 + 1;
    L11:
        if (r15 > r11) goto L15;
        if (r9[r15] != r1) goto L15;
        r15 = r15 + 1;
    L15:
        if (r15 > r11) goto L22;
        selectAllInPlace(r9, r15, r11, r12, r1 + 1, r14);
        return;
    }

    private static void selectInPlace(int r6, double[] r7, int r8, int r9) {
        if (r6 != r8) goto L12;
        int r0 = r8;
        int r1 = r8 + 1;
    L4:
        if (r1 > r9) goto L9;
        if (r7[r0] <= r7[r1]) goto L8;
        r0 = r1;
    L8:
        r1 = r1 + 1;
        goto L4
    L9:
        if (r0 == r8) goto L28;
        swap(r7, r0, r8);
        return;
    L28:
        return;
    L12:
        if (r9 <= r8) goto L19;
        int r2 = partition(r7, r8, r9);
        if (r2 < r6) goto L16;
        r9 = r2 - 1;
    L16:
        if (r2 > r6) goto L12;
        r8 = r2 + 1;
        goto L12
    }

    private static void swap(double[] r4, int r5, int r6) {
        double r0 = r4[r5];
        r4[r5] = r4[r6];
        r4[r6] = r0;
    }
}
