package androidx.core.view.animation;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.animation.Interpolator;

/* JADX INFO: loaded from: classes.dex */
class PathInterpolatorApi14 implements Interpolator {
    private static final float PRECISION = 0.002f;

    /* JADX INFO: renamed from: mX */
    private final float[] f20mX;

    /* JADX INFO: renamed from: mY */
    private final float[] f21mY;

    PathInterpolatorApi14(float r2, float r3) {
        this(createQuad(r2, r3));
    }

    PathInterpolatorApi14(float r2, float r3, float r4, float r5) {
        this(createCubic(r2, r3, r4, r5));
    }

    PathInterpolatorApi14(Path r11) {
        PathMeasure r0 = new PathMeasure(r11, false);
        float r2 = r0.getLength();
        int r3 = ((int) (r2 / PRECISION)) + 1;
        this.f20mX = new float[r3];
        this.f21mY = new float[r3];
        float[] r5 = new float[2];
        int r6 = 0;
    L3:
        if (r6 >= r3) goto L5;
        r0.getPosTan((r6 * r2) / (r3 - 1), r5, null);
        this.f20mX[r6] = r5[0];
        this.f21mY[r6] = r5[1];
        r6 = r6 + 1;
        goto L3
    }

    private static Path createCubic(float r8, float r9, float r10, float r11) {
        Path r0 = new Path();
        r0.moveTo(0.0f, 0.0f);
        r0.cubicTo(r8, r9, r10, r11, 1.0f, 1.0f);
        return r0;
    }

    private static Path createQuad(float r2, float r3) {
        Path r0 = new Path();
        r0.moveTo(0.0f, 0.0f);
        r0.quadTo(r2, r3, 1.0f, 1.0f);
        return r0;
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float r9) {
        if (r9 > 0.0f) goto L6;
        return 0.0f;
    L6:
        if (r9 < 1.0f) goto L8;
        return 1.0f;
    L8:
        int r1 = 0;
        int r2 = this.f20mX.length - 1;
    L10:
        if ((r2 - r1) <= 1) goto L16;
        int r4 = (r1 + r2) / 2;
        if (r9 < this.f20mX[r4]) goto L13;
        r1 = r4;
        goto L10
    L13:
        r2 = r4;
        goto L10
    L16:
        float r3 = this.f20mX[r2] - this.f20mX[r1];
        if (r3 == 0.0f) goto L19;
        float r5 = (r9 - this.f20mX[r1]) / r3;
        float r6 = this.f21mY[r1];
        return ((this.f21mY[r2] - r6) * r5) + r6;
    L19:
        return this.f21mY[r1];
    }
}
