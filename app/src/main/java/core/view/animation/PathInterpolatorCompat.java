package androidx.core.view.animation;

import android.graphics.Path;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

/* JADX INFO: loaded from: classes.dex */
public final class PathInterpolatorCompat {

    static class Api21Impl {
        private Api21Impl() {
        }

        static PathInterpolator createPathInterpolator(float r1, float r2) {
            return new PathInterpolator(r1, r2);
        }

        static PathInterpolator createPathInterpolator(float r1, float r2, float r3, float r4) {
            return new PathInterpolator(r1, r2, r3, r4);
        }

        static PathInterpolator createPathInterpolator(Path r1) {
            return new PathInterpolator(r1);
        }
    }

    private PathInterpolatorCompat() {
    }

    public static Interpolator create(float r1, float r2) {
        return Api21Impl.createPathInterpolator(r1, r2);
    }

    public static Interpolator create(float r1, float r2, float r3, float r4) {
        return Api21Impl.createPathInterpolator(r1, r2, r3, r4);
    }

    public static Interpolator create(Path r1) {
        return Api21Impl.createPathInterpolator(r1);
    }
}
