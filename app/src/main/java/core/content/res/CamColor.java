package androidx.core.content.res;

import androidx.core.graphics.ColorUtils;

/* JADX INFO: loaded from: classes.dex */
class CamColor {
    private static final float CHROMA_SEARCH_ENDPOINT = 0.4f;
    private static final float DE_MAX = 1.0f;
    private static final float DL_MAX = 0.2f;
    private static final float LIGHTNESS_SEARCH_ENDPOINT = 0.01f;
    private final float mAstar;
    private final float mBstar;
    private final float mChroma;
    private final float mHue;

    /* JADX INFO: renamed from: mJ */
    private final float f8mJ;
    private final float mJstar;

    /* JADX INFO: renamed from: mM */
    private final float f9mM;

    /* JADX INFO: renamed from: mQ */
    private final float f10mQ;

    /* JADX INFO: renamed from: mS */
    private final float f11mS;

    CamColor(float r1, float r2, float r3, float r4, float r5, float r6, float r7, float r8, float r9) {
        this.mHue = r1;
        this.mChroma = r2;
        this.f8mJ = r3;
        this.f10mQ = r4;
        this.f9mM = r5;
        this.f11mS = r6;
        this.mJstar = r7;
        this.mAstar = r8;
        this.mBstar = r9;
    }

    private static CamColor findCamByJ(float r13, float r14, float r15) {
        float r0 = 0.0f;
        float r1 = 100.0f;
        float r3 = 1000.0f;
        float r4 = 1000.0f;
        CamColor r5 = null;
    L4:
        if (Math.abs(r0 - r1) <= LIGHTNESS_SEARCH_ENDPOINT) goto L20;
        float r2 = r0 + ((r1 - r0) / 2.0f);
        int r7 = fromJch(r2, r14, r13).viewedInSrgb();
        float r8 = CamUtils.lStarFromInt(r7);
        float r9 = Math.abs(r15 - r8);
        if (r9 >= 0.2f) goto L11;
        CamColor r10 = fromColor(r7);
        float r11 = r10.distance(fromJch(r10.getJ(), r10.getChroma(), r13));
        if (r11 > 1.0f) goto L11;
        r3 = r9;
        r4 = r11;
        r5 = r10;
    L11:
        if (r3 != 0.0f) goto L16;
        if (r4 == 0.0f) goto L20;
    L16:
        if (r8 < r15) goto L17;
        r1 = r2;
        goto L4
    L17:
        r0 = r2;
    L20:
        return r5;
    }

    static CamColor fromColor(int r1) {
        return fromColorInViewingConditions(r1, ViewingConditions.DEFAULT);
    }

    static CamColor fromColorInViewingConditions(int r43, ViewingConditions r44) {
        float[] r0 = CamUtils.xyzFromInt(r43);
        float[][] r1 = CamUtils.XYZ_TO_CAM16RGB;
        float r3 = ((r0[0] * r1[0][0]) + (r0[1] * r1[0][1])) + (r0[2] * r1[0][2]);
        float r6 = ((r0[0] * r1[1][0]) + (r0[1] * r1[1][1])) + (r0[2] * r1[1][2]);
        float r7 = ((r0[0] * r1[2][0]) + (r0[1] * r1[2][1])) + (r0[2] * r1[2][2]);
        float r2 = r44.getRgbD()[0] * r3;
        float r4 = r44.getRgbD()[1] * r6;
        float r5 = r44.getRgbD()[2] * r7;
        float r8 = (float) Math.pow(((double) (r44.getFl() * Math.abs(r2))) / 100.0d, 0.42d);
        float r9 = (float) Math.pow(((double) (r44.getFl() * Math.abs(r4))) / 100.0d, 0.42d);
        float r12 = (float) Math.pow(((double) (r44.getFl() * Math.abs(r5))) / 100.0d, 0.42d);
        float r13 = ((Math.signum(r2) * 400.0f) * r8) / (r8 + 27.13f);
        float r10 = ((Math.signum(r4) * 400.0f) * r9) / (r9 + 27.13f);
        float r11 = ((Math.signum(r5) * 400.0f) * r12) / (27.13f + r12);
        float r14 = ((float) (((((double) r13) * 11.0d) + (((double) r10) * (-12.0d))) + ((double) r11))) / 11.0f;
        float r15 = ((float) (((double) (r13 + r10)) - (((double) r11) * 2.0d))) / 9.0f;
        float r16 = (((r13 * 20.0f) + (r10 * 20.0f)) + (21.0f * r11)) / 20.0f;
        float r17 = (((40.0f * r13) + (r10 * 20.0f)) + r11) / 20.0f;
        float r18 = (((float) Math.atan2(r15, r14)) * 180.0f) / 3.1415927f;
        if (r18 >= 0.0f) goto L6;
        float r26 = r18 + 360.0f;
    L9:
        float r29 = r26;
        float r19 = (3.1415927f * r29) / 180.0f;
        float r20 = ((float) Math.pow((r44.getNbb() * r17) / r44.getAw(), r44.getC() * r44.getZ())) * 100.0f;
        float r32 = (((4.0f / r44.getC()) * ((float) Math.sqrt(r20 / 100.0f))) * (r44.getAw() + 4.0f)) * r44.getFlRoot();
        if (r29 >= 20.14d) goto L12;
        float r210 = r29 + 360.0f;
    L13:
        float r21 = ((float) Math.pow(1.64d - Math.pow(0.29d, r44.getN()), 0.73d)) * ((float) Math.pow((((float) Math.sqrt((r14 * r14) + (r15 * r15))) * (((3846.1538f * (((float) (Math.cos(((((double) r210) * 3.141592653589793d) / 180.0d) + 2.0d) + 3.8d)) * 0.25f)) * r44.getNc()) * r44.getNcb())) / (0.305f + r16), 0.9d));
        float r30 = r21 * ((float) Math.sqrt(((double) r20) / 100.0d));
        float r33 = r30 * r44.getFlRoot();
        float r34 = ((float) Math.sqrt((r44.getC() * r21) / (r44.getAw() + 4.0f))) * 50.0f;
        float r35 = (1.7f * r20) / ((0.007f * r20) + 1.0f);
        float r22 = ((float) Math.log((0.0228f * r33) + 1.0f)) * 43.85965f;
        return new CamColor(r29, r30, r20, r32, r33, r34, r35, r22 * ((float) Math.cos(r19)), r22 * ((float) Math.sin(r19)));
    L12:
        r210 = r29;
        goto L13
    L6:
        if (r18 < 360.0f) goto L8;
        r26 = r18 - 360.0f;
        goto L9
    L8:
        r26 = r18;
        goto L9
    }

    private static CamColor fromJch(float r1, float r2, float r3) {
        return fromJchInFrame(r1, r2, r3, ViewingConditions.DEFAULT);
    }

    private static CamColor fromJchInFrame(float r13, float r14, float r15, ViewingConditions r16) {
        float r4 = (((4.0f / r16.getC()) * ((float) Math.sqrt(((double) r13) / 100.0d))) * (r16.getAw() + 4.0f)) * r16.getFlRoot();
        float r5 = r14 * r16.getFlRoot();
        float r6 = ((float) Math.sqrt((r16.getC() * (r14 / ((float) Math.sqrt(((double) r13) / 100.0d)))) / (r16.getAw() + 4.0f))) * 50.0f;
        float r11 = (3.1415927f * r15) / 180.0f;
        float r7 = (1.7f * r13) / ((0.007f * r13) + 1.0f);
        float r12 = ((float) Math.log((((double) r5) * 0.0228d) + 1.0d)) * 43.85965f;
        return new CamColor(r15, r14, r13, r4, r5, r6, r7, r12 * ((float) Math.cos(r11)), r12 * ((float) Math.sin(r11)));
    }

    static int toColor(float r1, float r2, float r3) {
        return toColor(r1, r2, r3, ViewingConditions.DEFAULT);
    }

    static int toColor(float r8, float r9, float r10, ViewingConditions r11) {
        if (r9 < 1.0d) goto L32;
        if (Math.round(r10) <= 0.0d) goto L32;
        if (Math.round(r10) >= 100.0d) goto L32;
        float r0 = 0.0f;
        if (r8 < 0.0f) goto L13;
        r0 = Math.min(360.0f, r8);
    L13:
        float r12 = r9;
        float r1 = r9;
        float r2 = 0.0f;
        boolean r3 = true;
        CamColor r4 = null;
    L15:
        if (Math.abs(r2 - r12) < CHROMA_SEARCH_ENDPOINT) goto L26;
        CamColor r5 = findCamByJ(r0, r1, r10);
        if (r3 == true) goto L18;
        if (r5 != null) goto L24;
        r12 = r1;
    L25:
        r1 = r2 + ((r12 - r2) / 2.0f);
        goto L15
    L24:
        r4 = r5;
        r2 = r1;
        goto L25
    L18:
        if (r5 != null) goto L20;
        r3 = false;
        r1 = r2 + ((r12 - r2) / 2.0f);
        goto L15
    L20:
        return r5.viewed(r11);
    L26:
        if (r4 != null) goto L30;
        return CamUtils.intFromLStar(r10);
    L30:
        return r4.viewed(r11);
    L32:
        return CamUtils.intFromLStar(r10);
    }

    float distance(CamColor r10) {
        float r0 = getJStar() - r10.getJStar();
        float r1 = getAStar() - r10.getAStar();
        float r2 = getBStar() - r10.getBStar();
        return (float) (Math.pow(Math.sqrt(((r0 * r0) + (r1 * r1)) + (r2 * r2)), 0.63d) * 1.41d);
    }

    float getAStar() {
        return this.mAstar;
    }

    float getBStar() {
        return this.mBstar;
    }

    float getChroma() {
        return this.mChroma;
    }

    float getHue() {
        return this.mHue;
    }

    float getJ() {
        return this.f8mJ;
    }

    float getJStar() {
        return this.mJstar;
    }

    float getM() {
        return this.f9mM;
    }

    float getQ() {
        return this.f10mQ;
    }

    float getS() {
        return this.f11mS;
    }

    int viewed(ViewingConditions r39) {
        if (getChroma() != 0.0d) goto L5;
    L8:
        float r0 = 0.0f;
    L9:
        float r1 = (float) Math.pow(((double) r0) / Math.pow(1.64d - Math.pow(0.29d, r39.getN()), 0.73d), 1.1111111111111112d);
        float r6 = (getHue() * 3.1415927f) / 180.0f;
        float r7 = ((float) (Math.cos(((double) r6) + 2.0d) + 3.8d)) * 0.25f;
        float r8 = r39.getAw() * ((float) Math.pow(((double) getJ()) / 100.0d, (1.0d / ((double) r39.getC())) / ((double) r39.getZ())));
        float r4 = ((3846.1538f * r7) * r39.getNc()) * r39.getNcb();
        float r5 = r8 / r39.getNbb();
        float r9 = (float) Math.sin(r6);
        float r10 = (float) Math.cos(r6);
        float r11 = (((0.305f + r5) * 23.0f) * r1) / (((23.0f * r4) + ((11.0f * r1) * r10)) + ((108.0f * r1) * r9));
        float r12 = r11 * r10;
        float r13 = r11 * r9;
        float r15 = (((r5 * 460.0f) + (451.0f * r12)) + (288.0f * r13)) / 1403.0f;
        float r17 = (((r5 * 460.0f) - (891.0f * r12)) - (261.0f * r13)) / 1403.0f;
        float r14 = (((460.0f * r5) - (220.0f * r12)) - (6300.0f * r13)) / 1403.0f;
        float r2 = (float) Math.max(0.0d, (((double) Math.abs(r15)) * 27.13d) / (400.0d - ((double) Math.abs(r15))));
        float r3 = ((float) Math.pow(r2, 2.380952380952381d)) * (Math.signum(r15) * (100.0f / r39.getFl()));
        float r16 = (Math.signum(r17) * (100.0f / r39.getFl())) * ((float) Math.pow((float) Math.max(0.0d, (((double) Math.abs(r17)) * 27.13d) / (400.0d - ((double) Math.abs(r17)))), 2.380952380952381d));
        float r18 = (Math.signum(r14) * (100.0f / r39.getFl())) * ((float) Math.pow((float) Math.max(0.0d, (((double) Math.abs(r14)) * 27.13d) / (400.0d - ((double) Math.abs(r14)))), 2.380952380952381d));
        float r19 = r3 / r39.getRgbD()[0];
        float r20 = r16 / r39.getRgbD()[1];
        float r21 = r18 / r39.getRgbD()[2];
        float[][] r24 = CamUtils.CAM16RGB_TO_XYZ;
        return ColorUtils.XYZToColor(((r24[0][0] * r19) + (r24[0][1] * r20)) + (r24[0][2] * r21), ((r24[1][0] * r19) + (r24[1][1] * r20)) + (r24[1][2] * r21), ((r24[2][0] * r19) + (r24[2][1] * r20)) + (r24[2][2] * r21));
    L5:
        if (getJ() == 0.0d) goto L8;
        r0 = getChroma() / ((float) Math.sqrt(((double) getJ()) / 100.0d));
        goto L9
    }

    int viewedInSrgb() {
        return viewed(ViewingConditions.DEFAULT);
    }
}
