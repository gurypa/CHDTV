package androidx.media3.common.audio;

import androidx.media3.common.util.Assertions;

/* JADX INFO: loaded from: classes.dex */
public final class ChannelMixingMatrix {
    private final float[] coefficients;
    private final int inputChannelCount;
    private final boolean isDiagonal;
    private final boolean isIdentity;
    private final boolean isZero;
    private final int outputChannelCount;

    public ChannelMixingMatrix(int r11, int r12, float[] r13) {
        boolean r0 = false;
        if (r11 <= 0) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkArgument(r2, "Input channel count must be positive.");
        if (r12 <= 0) goto L9;
        boolean r3 = true;
    L10:
        Assertions.checkArgument(r3, "Output channel count must be positive.");
        if (r13.length != (r11 * r12)) goto L13;
        boolean r4 = true;
    L14:
        Assertions.checkArgument(r4, "Coefficient array length is invalid.");
        this.inputChannelCount = r11;
        this.outputChannelCount = r12;
        this.coefficients = checkCoefficientsValid(r13);
        boolean r5 = true;
        boolean r6 = true;
        boolean r7 = true;
        int r8 = 0;
    L15:
        if (r8 >= r11) goto L33;
        int r9 = 0;
    L17:
        if (r9 >= r12) goto L32;
        float r10 = getMixingCoefficient(r8, r9);
        if (r8 != r9) goto L21;
        boolean r14 = true;
    L23:
        if (r10 == 1.0f) goto L27;
        if (r14 == false) goto L27;
        r5 = false;
    L27:
        if (r10 == 0.0f) goto L31;
        r6 = false;
        if (r14 == true) goto L31;
        r7 = false;
    L31:
        r9 = r9 + 1;
        goto L17
    L21:
        r14 = false;
        goto L23
    L32:
        r8 = r8 + 1;
        goto L15
    L33:
        this.isZero = r6;
        if (isSquare() == false) goto L37;
        if (r7 == false) goto L37;
        boolean r15 = true;
    L38:
        this.isDiagonal = r15;
        if (this.isDiagonal == false) goto L42;
        if (r5 == false) goto L42;
        r0 = true;
    L42:
        this.isIdentity = r0;
        return;
    L37:
        r15 = false;
        goto L38
    L13:
        r4 = false;
        goto L14
    L9:
        r3 = false;
        goto L10
    L5:
        r2 = false;
        goto L6
    }

    private static float[] checkCoefficientsValid(float[] r4) {
        int r0 = 0;
    L4:
        if (r0 >= r4.length) goto L10;
        if (r4[r0] < 0.0f) goto L9;
        r0 = r0 + 1;
        goto L4
    L9:
        throw new IllegalArgumentException("Coefficient at index " + r0 + " is negative.");
    L10:
        return r4;
    }

    public static ChannelMixingMatrix create(int r2, int r3) {
        return new ChannelMixingMatrix(r2, r3, createMixingCoefficients(r2, r3));
    }

    private static float[] createMixingCoefficients(int r3, int r4) {
        if (r3 != r4) goto L6;
        return initializeIdentityMatrix(r4);
    L6:
        if (r3 != 1) goto L10;
        if (r4 != 2) goto L10;
        return new float[]{1.0f, 1.0f};
    L10:
        if (r3 != 2) goto L15;
        if (r4 != 1) goto L15;
        return new float[]{0.5f, 0.5f};
    L15:
        throw new UnsupportedOperationException("Default channel mixing coefficients for " + r3 + "->" + r4 + " are not yet implemented.");
    }

    private static float[] initializeIdentityMatrix(int r4) {
        float[] r0 = new float[r4 * r4];
        int r1 = 0;
    L3:
        if (r1 >= r4) goto L5;
        r0[(r4 * r1) + r1] = 1.0f;
        r1 = r1 + 1;
        goto L3
    L5:
        return r0;
    }

    public int getInputChannelCount() {
        return this.inputChannelCount;
    }

    public float getMixingCoefficient(int r3, int r4) {
        return this.coefficients[(this.outputChannelCount * r3) + r4];
    }

    public int getOutputChannelCount() {
        return this.outputChannelCount;
    }

    public boolean isDiagonal() {
        return this.isDiagonal;
    }

    public boolean isIdentity() {
        return this.isIdentity;
    }

    public boolean isSquare() {
        if (this.inputChannelCount != this.outputChannelCount) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isZero() {
        return this.isZero;
    }

    public ChannelMixingMatrix scaleBy(float r5) {
        float[] r0 = new float[this.coefficients.length];
        int r1 = 0;
    L4:
        if (r1 >= this.coefficients.length) goto L7;
        r0[r1] = this.coefficients[r1] * r5;
        r1 = r1 + 1;
        goto L4
    L7:
        return new ChannelMixingMatrix(this.inputChannelCount, this.outputChannelCount, r0);
    }
}
