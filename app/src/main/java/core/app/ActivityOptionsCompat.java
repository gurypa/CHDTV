package androidx.core.app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public class ActivityOptionsCompat {
    public static final String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";
    public static final String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";

    private static class ActivityOptionsCompatImpl extends ActivityOptionsCompat {
        private final ActivityOptions mActivityOptions;

        ActivityOptionsCompatImpl(ActivityOptions r1) {
            this.mActivityOptions = r1;
        }

        @Override // androidx.core.app.ActivityOptionsCompat
        public Rect getLaunchBounds() {
            if (Build.VERSION.SDK_INT >= 24) goto L7;
            return null;
        L7:
            return Api24Impl.getLaunchBounds(this.mActivityOptions);
        }

        @Override // androidx.core.app.ActivityOptionsCompat
        public void requestUsageTimeReport(PendingIntent r2) {
            Api23Impl.requestUsageTimeReport(this.mActivityOptions, r2);
        }

        @Override // androidx.core.app.ActivityOptionsCompat
        public ActivityOptionsCompat setLaunchBounds(Rect r3) {
            if (Build.VERSION.SDK_INT >= 24) goto L6;
            return this;
        L6:
            return new ActivityOptionsCompatImpl(Api24Impl.setLaunchBounds(this.mActivityOptions, r3));
        }

        @Override // androidx.core.app.ActivityOptionsCompat
        public Bundle toBundle() {
            return this.mActivityOptions.toBundle();
        }

        @Override // androidx.core.app.ActivityOptionsCompat
        public void update(ActivityOptionsCompat r4) {
            if ((r4 instanceof ActivityOptionsCompatImpl) == false) goto L6;
            this.mActivityOptions.update(((ActivityOptionsCompatImpl) r4).mActivityOptions);
            return;
        }
    }

    static class Api16Impl {
        private Api16Impl() {
        }

        static ActivityOptions makeCustomAnimation(Context r1, int r2, int r3) {
            return ActivityOptions.makeCustomAnimation(r1, r2, r3);
        }

        static ActivityOptions makeScaleUpAnimation(View r1, int r2, int r3, int r4, int r5) {
            return ActivityOptions.makeScaleUpAnimation(r1, r2, r3, r4, r5);
        }

        static ActivityOptions makeThumbnailScaleUpAnimation(View r1, Bitmap r2, int r3, int r4) {
            return ActivityOptions.makeThumbnailScaleUpAnimation(r1, r2, r3, r4);
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static ActivityOptions makeSceneTransitionAnimation(Activity r1, View r2, String r3) {
            return ActivityOptions.makeSceneTransitionAnimation(r1, r2, r3);
        }

        @SafeVarargs
        static ActivityOptions makeSceneTransitionAnimation(Activity r1, Pair<View, String>... r2) {
            return ActivityOptions.makeSceneTransitionAnimation(r1, r2);
        }

        static ActivityOptions makeTaskLaunchBehind() {
            return ActivityOptions.makeTaskLaunchBehind();
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static ActivityOptions makeBasic() {
            return ActivityOptions.makeBasic();
        }

        static ActivityOptions makeClipRevealAnimation(View r1, int r2, int r3, int r4, int r5) {
            return ActivityOptions.makeClipRevealAnimation(r1, r2, r3, r4, r5);
        }

        static void requestUsageTimeReport(ActivityOptions r0, PendingIntent r1) {
            r0.requestUsageTimeReport(r1);
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static Rect getLaunchBounds(ActivityOptions r1) {
            return r1.getLaunchBounds();
        }

        static ActivityOptions setLaunchBounds(ActivityOptions r1, Rect r2) {
            return r1.setLaunchBounds(r2);
        }
    }

    protected ActivityOptionsCompat() {
    }

    public static ActivityOptionsCompat makeBasic() {
        return new ActivityOptionsCompatImpl(Api23Impl.makeBasic());
    }

    public static ActivityOptionsCompat makeClipRevealAnimation(View r2, int r3, int r4, int r5, int r6) {
        return new ActivityOptionsCompatImpl(Api23Impl.makeClipRevealAnimation(r2, r3, r4, r5, r6));
    }

    public static ActivityOptionsCompat makeCustomAnimation(Context r2, int r3, int r4) {
        return new ActivityOptionsCompatImpl(Api16Impl.makeCustomAnimation(r2, r3, r4));
    }

    public static ActivityOptionsCompat makeScaleUpAnimation(View r2, int r3, int r4, int r5, int r6) {
        return new ActivityOptionsCompatImpl(Api16Impl.makeScaleUpAnimation(r2, r3, r4, r5, r6));
    }

    public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity r2, View r3, String r4) {
        return new ActivityOptionsCompatImpl(Api21Impl.makeSceneTransitionAnimation(r2, r3, r4));
    }

    public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity r4, androidx.core.util.Pair<View, String>... r5) {
        Pair[] r0 = null;
        if (r5 == null) goto L9;
        r0 = new Pair[r5.length];
        int r1 = 0;
    L6:
        if (r1 >= r5.length) goto L9;
        r0[r1] = Pair.create(r5[r1].first, r5[r1].second);
        r1 = r1 + 1;
    L9:
        return new ActivityOptionsCompatImpl(Api21Impl.makeSceneTransitionAnimation(r4, r0));
    }

    public static ActivityOptionsCompat makeTaskLaunchBehind() {
        return new ActivityOptionsCompatImpl(Api21Impl.makeTaskLaunchBehind());
    }

    public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View r2, Bitmap r3, int r4, int r5) {
        return new ActivityOptionsCompatImpl(Api16Impl.makeThumbnailScaleUpAnimation(r2, r3, r4, r5));
    }

    public Rect getLaunchBounds() {
        return null;
    }

    public void requestUsageTimeReport(PendingIntent r1) {
    }

    public ActivityOptionsCompat setLaunchBounds(Rect r1) {
        return this;
    }

    public Bundle toBundle() {
        return null;
    }

    public void update(ActivityOptionsCompat r1) {
    }
}
