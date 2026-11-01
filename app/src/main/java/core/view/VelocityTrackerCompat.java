package androidx.core.view;

import android.view.VelocityTracker;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class VelocityTrackerCompat {
    private VelocityTrackerCompat() {
    }

    @Deprecated
    public static float getXVelocity(VelocityTracker r1, int r2) {
        return r1.getXVelocity(r2);
    }

    @Deprecated
    public static float getYVelocity(VelocityTracker r1, int r2) {
        return r1.getYVelocity(r2);
    }
}
