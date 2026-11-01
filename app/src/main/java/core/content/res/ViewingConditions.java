package androidx.core.content.res;

/* JADX INFO: loaded from: classes.dex */
final class ViewingConditions {
    static final ViewingConditions DEFAULT = null;
    private final float mAw;

    /* JADX INFO: renamed from: mC */
    private final float f12mC;
    private final float mFl;
    private final float mFlRoot;

    /* JADX INFO: renamed from: mN */
    private final float f13mN;
    private final float mNbb;
    private final float mNc;
    private final float mNcb;
    private final float[] mRgbD;

    /* JADX INFO: renamed from: mZ */
    private final float f14mZ;

    static {
        DEFAULT = make(CamUtils.WHITE_POINT_D65, (float) ((((double) CamUtils.yFromLStar(50.0f)) * 63.66197723675813d) / 100.0d), 50.0f, 2.0f, false);
    }

    private ViewingConditions(float r1, float r2, float r3, float r4, float r5, float r6, float[] r7, float r8, float r9, float r10) {
        this.f13mN = r1;
        this.mAw = r2;
        this.mNbb = r3;
        this.mNcb = r4;
        this.f12mC = r5;
        this.mNc = r6;
        this.mRgbD = r7;
        this.mFl = r8;
        this.mFlRoot = r9;
        this.f14mZ = r10;
    }

    static ViewingConditions make(float[] r30, float r31, float r32, float r33, boolean r34) {
        float[][] r1 = CamUtils.XYZ_TO_CAM16RGB;
        float r4 = ((r30[0] * r1[0][0]) + (r30[1] * r1[0][1])) + (r30[2] * r1[0][2]);
        float r7 = ((r30[0] * r1[1][0]) + (r30[1] * r1[1][1])) + (r30[2] * r1[1][2]);
        float r8 = ((r30[0] * r1[2][0]) + (r30[1] * r1[2][1])) + (r30[2] * r1[2][2]);
        float r10 = (r33 / 10.0f) + 0.8f;
        if (r10 < 0.9d) goto L5;
        float r9 = CamUtils.lerp(0.59f, 0.69f, (r10 - 0.9f) * 10.0f);
    L6:
        float r16 = r9;
        if (r34 == false) goto L9;
        float r11 = 1.0f;
    L11:
        if (r11 <= 1.0d) goto L14;
        float r12 = 1.0f;
    L17:
        float r22 = r12;
        float[] r15 = {(((100.0f / r4) * r22) + 1.0f) - r22, (((100.0f / r7) * r22) + 1.0f) - r22, (((100.0f / r8) * r22) + 1.0f) - r22};
        float r23 = 1.0f / ((5.0f * r31) + 1.0f);
        float r24 = ((r23 * r23) * r23) * r23;
        float r13 = 1.0f - r24;
        float r14 = (r24 * r31) + (((0.1f * r13) * r13) * ((float) Math.cbrt(((double) r31) * 5.0d)));
        float r17 = CamUtils.yFromLStar(r32) / r30[1];
        float r21 = ((float) Math.sqrt(r17)) + 1.48f;
        float r5 = 0.725f / ((float) Math.pow(r17, 0.2d));
        float[] r18 = {(float) Math.pow(((double) ((r15[0] * r14) * r4)) / 100.0d, 0.42d), (float) Math.pow(((double) ((r15[1] * r14) * r7)) / 100.0d, 0.42d), (float) Math.pow(((double) ((r15[2] * r14) * r8)) / 100.0d, 0.42d)};
        float[] r6 = {(r18[0] * 400.0f) / (r18[0] + 27.13f), (r18[1] * 400.0f) / (r18[1] + 27.13f), (r18[2] * 400.0f) / (r18[2] + 27.13f)};
        return new ViewingConditions(r17, (((r6[0] * 2.0f) + r6[1]) + (r6[2] * 0.05f)) * r5, r5, r5, r16, r10, r15, r14, (float) Math.pow(r14, 0.25d), r21);
    L14:
        if (r11 >= 0.0d) goto L16;
        r12 = 0.0f;
        goto L17
    L16:
        r12 = r11;
        goto L17
    L9:
        r11 = (1.0f - (((float) Math.exp(((-r31) - 42.0f) / 92.0f)) * 0.2777778f)) * r10;
        goto L11
    L5:
        r9 = CamUtils.lerp(0.525f, 0.59f, (r10 - 0.8f) * 10.0f);
        goto L6
    }

    float getAw() {
        return this.mAw;
    }

    float getC() {
        return this.f12mC;
    }

    float getFl() {
        return this.mFl;
    }

    float getFlRoot() {
        return this.mFlRoot;
    }

    float getN() {
        return this.f13mN;
    }

    float getNbb() {
        return this.mNbb;
    }

    float getNc() {
        return this.mNc;
    }

    float getNcb() {
        return this.mNcb;
    }

    float[] getRgbD() {
        return this.mRgbD;
    }

    float getZ() {
        return this.f14mZ;
    }
}
