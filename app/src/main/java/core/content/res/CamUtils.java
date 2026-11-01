package androidx.core.content.res;

import android.graphics.Color;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;

/* JADX INFO: loaded from: classes.dex */
final class CamUtils {
    static final float[][] CAM16RGB_TO_XYZ = null;
    static final float[][] SRGB_TO_XYZ = null;
    static final float[] WHITE_POINT_D65 = null;
    static final float[][] XYZ_TO_CAM16RGB = null;

    static {
        XYZ_TO_CAM16RGB = new float[][]{new float[]{0.401288f, 0.650173f, -0.051461f}, new float[]{-0.250268f, 1.204414f, 0.045854f}, new float[]{-0.002079f, 0.048952f, 0.953127f}};
        CAM16RGB_TO_XYZ = new float[][]{new float[]{1.8620678f, -1.0112547f, 0.14918678f}, new float[]{0.38752654f, 0.62144744f, -0.00897398f}, new float[]{-0.0158415f, -0.03412294f, 1.0499644f}};
        WHITE_POINT_D65 = new float[]{95.047f, 100.0f, 108.883f};
        SRGB_TO_XYZ = new float[][]{new float[]{0.41233894f, 0.35762063f, 0.18051042f}, new float[]{0.2126f, 0.7152f, 0.0722f}, new float[]{0.01932141f, 0.11916382f, 0.9503448f}};
    }

    private CamUtils() {
    }

    static int intFromLStar(float r21) {
        if (r21 >= 1.0f) goto L7;
        return ViewCompat.MEASURED_STATE_MASK;
    L7:
        if (r21 <= 99.0f) goto L10;
        return -1;
    L10:
        float r1 = (r21 + 16.0f) / 116.0f;
        if (r21 <= 8.0f) goto L13;
        boolean r7 = true;
    L14:
        if (r7 == false) goto L16;
        float r10 = (r1 * r1) * r1;
    L18:
        if (((r1 * r1) * r1) <= 0.008856452f) goto L20;
        boolean r11 = true;
    L21:
        if (r11 == false) goto L23;
        float r12 = (r1 * r1) * r1;
    L24:
        if (r11 == false) goto L26;
        float r0 = (r1 * r1) * r1;
    L28:
        return ColorUtils.XYZToColor(WHITE_POINT_D65[0] * r12, WHITE_POINT_D65[1] * r10, WHITE_POINT_D65[2] * r0);
    L26:
        r0 = ((116.0f * r1) - 16.0f) / 903.2963f;
        goto L28
    L23:
        r12 = ((r1 * 116.0f) - 16.0f) / 903.2963f;
        goto L24
    L20:
        r11 = false;
        goto L21
    L16:
        r10 = r21 / 903.2963f;
        goto L18
    L13:
        r7 = false;
        goto L14
    }

    static float lStarFromInt(int r1) {
        return lStarFromY(yFromInt(r1));
    }

    static float lStarFromY(float r4) {
        float r5 = r4 / 100.0f;
        if (r5 > 0.008856452f) goto L7;
        return 903.2963f * r5;
    L7:
        return (116.0f * ((float) Math.cbrt(r5))) - 16.0f;
    }

    static float lerp(float r1, float r2, float r3) {
        return ((r2 - r1) * r3) + r1;
    }

    static float linearized(int r7) {
        float r0 = r7 / 255.0f;
        if (r0 > 0.04045f) goto L7;
        return (r0 / 12.92f) * 100.0f;
    L7:
        return ((float) Math.pow((0.055f + r0) / 1.055f, 2.4000000953674316d)) * 100.0f;
    }

    static float[] xyzFromInt(int r11) {
        float r0 = linearized(Color.red(r11));
        float r1 = linearized(Color.green(r11));
        float r2 = linearized(Color.blue(r11));
        float[][] r3 = SRGB_TO_XYZ;
        return new float[]{((r3[0][0] * r0) + (r3[0][1] * r1)) + (r3[0][2] * r2), ((r3[1][0] * r0) + (r3[1][1] * r1)) + (r3[1][2] * r2), ((r3[2][0] * r0) + (r3[2][1] * r1)) + (r3[2][2] * r2)};
    }

    static float yFromInt(int r7) {
        float r0 = linearized(Color.red(r7));
        float r1 = linearized(Color.green(r7));
        float r2 = linearized(Color.blue(r7));
        float[][] r3 = SRGB_TO_XYZ;
        return ((r3[1][0] * r0) + (r3[1][1] * r1)) + (r3[1][2] * r2);
    }

    static float yFromLStar(float r7) {
        if (r7 <= 8.0f) goto L7;
        return ((float) Math.pow((((double) r7) + 16.0d) / 116.0d, 3.0d)) * 100.0f;
    L7:
        return (r7 / 903.2963f) * 100.0f;
    }
}
