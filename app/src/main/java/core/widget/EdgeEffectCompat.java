package androidx.core.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EdgeEffect;

/* JADX INFO: loaded from: classes.dex */
public final class EdgeEffectCompat {
    private final EdgeEffect mEdgeEffect;

    static class Api21Impl {
        private Api21Impl() {
        }

        static void onPull(EdgeEffect r0, float r1, float r2) {
            r0.onPull(r1, r2);
        }
    }

    private static class Api31Impl {
        private Api31Impl() {
        }

        public static EdgeEffect create(Context r2, AttributeSet r3) {
            return new EdgeEffect(r2, r3);
        L6:
            return new EdgeEffect(r2);
        }

        public static float getDistance(EdgeEffect r2) {
            return r2.getDistance();
        L5:
            return 0.0f;
        }

        public static float onPullDistance(EdgeEffect r2, float r3, float r4) {
            return r2.onPullDistance(r3, r4);
        L5:
            r2.onPull(r3, r4);
            return 0.0f;
        }
    }

    @Deprecated
    public EdgeEffectCompat(Context r2) {
        this.mEdgeEffect = new EdgeEffect(r2);
    }

    public static EdgeEffect create(Context r2, AttributeSet r3) {
        if (Build.VERSION.SDK_INT < 31) goto L7;
        return Api31Impl.create(r2, r3);
    L7:
        return new EdgeEffect(r2);
    }

    public static float getDistance(EdgeEffect r2) {
        if (Build.VERSION.SDK_INT >= 31) goto L5;
        return 0.0f;
    L5:
        return Api31Impl.getDistance(r2);
    }

    public static void onPull(EdgeEffect r0, float r1, float r2) {
        Api21Impl.onPull(r0, r1, r2);
    }

    public static float onPullDistance(EdgeEffect r2, float r3, float r4) {
        if (Build.VERSION.SDK_INT >= 31) goto L5;
        onPull(r2, r3, r4);
        return r3;
    L5:
        return Api31Impl.onPullDistance(r2, r3, r4);
    }

    @Deprecated
    public boolean draw(Canvas r2) {
        return this.mEdgeEffect.draw(r2);
    }

    @Deprecated
    public void finish() {
        this.mEdgeEffect.finish();
    }

    @Deprecated
    public boolean isFinished() {
        return this.mEdgeEffect.isFinished();
    }

    @Deprecated
    public boolean onAbsorb(int r2) {
        this.mEdgeEffect.onAbsorb(r2);
        return true;
    }

    @Deprecated
    public boolean onPull(float r2) {
        this.mEdgeEffect.onPull(r2);
        return true;
    }

    @Deprecated
    public boolean onPull(float r2, float r3) {
        onPull(this.mEdgeEffect, r2, r3);
        return true;
    }

    @Deprecated
    public boolean onRelease() {
        this.mEdgeEffect.onRelease();
        return this.mEdgeEffect.isFinished();
    }

    @Deprecated
    public void setSize(int r2, int r3) {
        this.mEdgeEffect.setSize(r2, r3);
    }
}
