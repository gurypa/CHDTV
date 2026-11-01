package androidx.core.view;

import android.animation.ValueAnimator;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.core.C0039R;
import androidx.core.graphics.Insets;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class WindowInsetsAnimationCompat {
    private static final boolean DEBUG = false;
    private static final String TAG = "WindowInsetsAnimCompat";
    private Impl mImpl;

    public static final class BoundsCompat {
        private final Insets mLowerBound;
        private final Insets mUpperBound;

        private BoundsCompat(WindowInsetsAnimation.Bounds r2) {
            this.mLowerBound = Impl30.getLowerBounds(r2);
            this.mUpperBound = Impl30.getHigherBounds(r2);
        }

        public BoundsCompat(Insets r1, Insets r2) {
            this.mLowerBound = r1;
            this.mUpperBound = r2;
        }

        public static BoundsCompat toBoundsCompat(WindowInsetsAnimation.Bounds r1) {
            return new BoundsCompat(r1);
        }

        public Insets getLowerBound() {
            return this.mLowerBound;
        }

        public Insets getUpperBound() {
            return this.mUpperBound;
        }

        public BoundsCompat inset(Insets r8) {
            return new BoundsCompat(WindowInsetsCompat.insetInsets(this.mLowerBound, r8.left, r8.top, r8.right, r8.bottom), WindowInsetsCompat.insetInsets(this.mUpperBound, r8.left, r8.top, r8.right, r8.bottom));
        }

        public WindowInsetsAnimation.Bounds toBounds() {
            return Impl30.createPlatformBounds(this);
        }

        public String toString() {
            return "Bounds{lower=" + this.mLowerBound + " upper=" + this.mUpperBound + "}";
        }
    }

    public static abstract class Callback {
        public static final int DISPATCH_MODE_CONTINUE_ON_SUBTREE = 1;
        public static final int DISPATCH_MODE_STOP = 0;
        WindowInsets mDispachedInsets;
        private final int mDispatchMode;

        @Retention(RetentionPolicy.SOURCE)
        public @interface DispatchMode {
        }

        public Callback(int r1) {
            this.mDispatchMode = r1;
        }

        public final int getDispatchMode() {
            return this.mDispatchMode;
        }

        public void onEnd(WindowInsetsAnimationCompat r1) {
        }

        public void onPrepare(WindowInsetsAnimationCompat r1) {
        }

        public abstract WindowInsetsCompat onProgress(WindowInsetsCompat r1, List<WindowInsetsAnimationCompat> r2);

        public BoundsCompat onStart(WindowInsetsAnimationCompat r1, BoundsCompat r2) {
            return r2;
        }
    }

    private static class Impl {
        private float mAlpha;
        private final long mDurationMillis;
        private float mFraction;
        private final Interpolator mInterpolator;
        private final int mTypeMask;

        Impl(int r1, Interpolator r2, long r3) {
            this.mTypeMask = r1;
            this.mInterpolator = r2;
            this.mDurationMillis = r3;
        }

        public float getAlpha() {
            return this.mAlpha;
        }

        public long getDurationMillis() {
            return this.mDurationMillis;
        }

        public float getFraction() {
            return this.mFraction;
        }

        public float getInterpolatedFraction() {
            if (this.mInterpolator == null) goto L7;
            return this.mInterpolator.getInterpolation(this.mFraction);
        L7:
            return this.mFraction;
        }

        public Interpolator getInterpolator() {
            return this.mInterpolator;
        }

        public int getTypeMask() {
            return this.mTypeMask;
        }

        public void setAlpha(float r1) {
            this.mAlpha = r1;
        }

        public void setFraction(float r1) {
            this.mFraction = r1;
        }
    }

    private static class Impl21 extends Impl {

        private static class Impl21OnApplyWindowInsetsListener implements View.OnApplyWindowInsetsListener {
            private static final int COMPAT_ANIMATION_DURATION = 160;
            final Callback mCallback;
            private WindowInsetsCompat mLastInsets;

            Impl21OnApplyWindowInsetsListener(View r3, Callback r4) {
                this.mCallback = r4;
                WindowInsetsCompat r0 = ViewCompat.getRootWindowInsets(r3);
                if (r0 == null) goto L5;
                WindowInsetsCompat r1 = new WindowInsetsCompat.Builder(r0).build();
            L6:
                this.mLastInsets = r1;
                return;
            L5:
                r1 = null;
                goto L6
            }

            @Override // android.view.View.OnApplyWindowInsetsListener
            public WindowInsets onApplyWindowInsets(final View r13, WindowInsets r14) {
                if (r13.isLaidOut() == true) goto L6;
                this.mLastInsets = WindowInsetsCompat.toWindowInsetsCompat(r14, r13);
                return Impl21.forwardToViewIfNeeded(r13, r14);
            L6:
                final WindowInsetsCompat r3 = WindowInsetsCompat.toWindowInsetsCompat(r14, r13);
                if (this.mLastInsets != null) goto L10;
                this.mLastInsets = ViewCompat.getRootWindowInsets(r13);
            L10:
                if (this.mLastInsets != null) goto L13;
                this.mLastInsets = r3;
                return Impl21.forwardToViewIfNeeded(r13, r14);
            L13:
                Callback r7 = Impl21.getCallback(r13);
                if (r7 != null) goto L16;
            L19:
                final int r5 = Impl21.buildAnimationMask(r3, this.mLastInsets);
                if (r5 == 0) goto L22;
                final WindowInsetsCompat r4 = this.mLastInsets;
                final WindowInsetsAnimationCompat r0 = new WindowInsetsAnimationCompat(r5, new DecelerateInterpolator(), 160);
                r0.setFraction(0.0f);
                final ValueAnimator r8 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f}).setDuration(r0.getDurationMillis());
                final BoundsCompat r9 = Impl21.computeAnimationBounds(r3, r4, r5);
                Impl21.dispatchOnPrepare(r13, r0, r14, false);
                r8.addUpdateListener(new C01251(this, r0, r3, r4, r5, r13));
                r8.addListener(new C01262(this, r0, r13));
                OneShotPreDrawListener.add(r13, new RunnableC01273(this, r13, r0, r9, r8));
                this.mLastInsets = r3;
                return Impl21.forwardToViewIfNeeded(r13, r14);
            L22:
                return Impl21.forwardToViewIfNeeded(r13, r14);
            L16:
                if (Objects.equals(r7.mDispachedInsets, r14) == false) goto L19;
                return Impl21.forwardToViewIfNeeded(r13, r14);
            }
        }

        Impl21(int r1, Interpolator r2, long r3) {
            super(r1, r2, r3);
        }

        static int buildAnimationMask(WindowInsetsCompat r4, WindowInsetsCompat r5) {
            int r0 = 0;
            int r1 = 1;
        L4:
            if (r1 > 256) goto L9;
            if (r4.getInsets(r1).equals(r5.getInsets(r1)) == true) goto L8;
            r0 = r0 | r1;
        L8:
            r1 = r1 << 1;
            goto L4
        L9:
            return r0;
        }

        static BoundsCompat computeAnimationBounds(WindowInsetsCompat r8, WindowInsetsCompat r9, int r10) {
            Insets r0 = r8.getInsets(r10);
            Insets r1 = r9.getInsets(r10);
            return new BoundsCompat(Insets.m8of(Math.min(r0.left, r1.left), Math.min(r0.top, r1.top), Math.min(r0.right, r1.right), Math.min(r0.bottom, r1.bottom)), Insets.m8of(Math.max(r0.left, r1.left), Math.max(r0.top, r1.top), Math.max(r0.right, r1.right), Math.max(r0.bottom, r1.bottom)));
        }

        private static View.OnApplyWindowInsetsListener createProxyListener(View r1, Callback r2) {
            return new Impl21OnApplyWindowInsetsListener(r1, r2);
        }

        static void dispatchOnEnd(View r4, WindowInsetsAnimationCompat r5) {
            Callback r0 = getCallback(r4);
            if (r0 == null) goto L8;
            r0.onEnd(r5);
            if (r0.getDispatchMode() != 0) goto L8;
            return;
        L8:
            if ((r4 instanceof ViewGroup) == false) goto L13;
            ViewGroup r1 = (ViewGroup) r4;
            int r2 = 0;
        L11:
            if (r2 >= r1.getChildCount()) goto L15;
            dispatchOnEnd(r1.getChildAt(r2), r5);
            r2 = r2 + 1;
            goto L11
        L15:
            return;
        }

        static void dispatchOnPrepare(View r4, WindowInsetsAnimationCompat r5, WindowInsets r6, boolean r7) {
            Callback r0 = getCallback(r4);
            if (r0 == null) goto L12;
            r0.mDispachedInsets = r6;
            if (r7 == true) goto L12;
            r0.onPrepare(r5);
            if (r0.getDispatchMode() != 0) goto L9;
            boolean r1 = true;
        L10:
            r7 = r1;
            goto L12
        L9:
            r1 = false;
        L12:
            if ((r4 instanceof ViewGroup) == false) goto L17;
            ViewGroup r2 = (ViewGroup) r4;
            int r3 = 0;
        L15:
            if (r3 >= r2.getChildCount()) goto L19;
            dispatchOnPrepare(r2.getChildAt(r3), r5, r6, r7);
            r3 = r3 + 1;
            goto L15
        L19:
            return;
        }

        static void dispatchOnProgress(View r5, WindowInsetsCompat r6, List<WindowInsetsAnimationCompat> r7) {
            Callback r0 = getCallback(r5);
            WindowInsetsCompat r1 = r6;
            if (r0 == null) goto L8;
            r1 = r0.onProgress(r1, r7);
            if (r0.getDispatchMode() != 0) goto L8;
            return;
        L8:
            if ((r5 instanceof ViewGroup) == false) goto L13;
            ViewGroup r2 = (ViewGroup) r5;
            int r3 = 0;
        L11:
            if (r3 >= r2.getChildCount()) goto L15;
            dispatchOnProgress(r2.getChildAt(r3), r1, r7);
            r3 = r3 + 1;
            goto L11
        L15:
            return;
        }

        static void dispatchOnStart(View r4, WindowInsetsAnimationCompat r5, BoundsCompat r6) {
            Callback r0 = getCallback(r4);
            if (r0 == null) goto L8;
            r0.onStart(r5, r6);
            if (r0.getDispatchMode() != 0) goto L8;
            return;
        L8:
            if ((r4 instanceof ViewGroup) == false) goto L13;
            ViewGroup r1 = (ViewGroup) r4;
            int r2 = 0;
        L11:
            if (r2 >= r1.getChildCount()) goto L15;
            dispatchOnStart(r1.getChildAt(r2), r5, r6);
            r2 = r2 + 1;
            goto L11
        L15:
            return;
        }

        static WindowInsets forwardToViewIfNeeded(View r1, WindowInsets r2) {
            if (r1.getTag(C0039R.id.tag_on_apply_window_listener) == null) goto L6;
            return r2;
        L6:
            return r1.onApplyWindowInsets(r2);
        }

        static Callback getCallback(View r3) {
            Object r0 = r3.getTag(C0039R.id.tag_window_insets_animation_callback);
            if ((r0 instanceof Impl21OnApplyWindowInsetsListener) == true) goto L5;
            return null;
        L5:
            return ((Impl21OnApplyWindowInsetsListener) r0).mCallback;
        }

        static WindowInsetsCompat interpolateInsets(WindowInsetsCompat r12, WindowInsetsCompat r13, float r14, int r15) {
            WindowInsetsCompat.Builder r0 = new WindowInsetsCompat.Builder(r12);
            int r1 = 1;
        L4:
            if (r1 > 256) goto L11;
            if ((r15 & r1) != 0) goto L8;
            r0.setInsets(r1, r12.getInsets(r1));
        L9:
            r1 = r1 << 1;
            goto L4
        L8:
            Insets r2 = r12.getInsets(r1);
            Insets r3 = r13.getInsets(r1);
            r0.setInsets(r1, WindowInsetsCompat.insetInsets(r2, (int) (((double) ((r2.left - r3.left) * (1.0f - r14))) + 0.5d), (int) (((double) ((r2.top - r3.top) * (1.0f - r14))) + 0.5d), (int) (((double) ((r2.right - r3.right) * (1.0f - r14))) + 0.5d), (int) (((double) ((r2.bottom - r3.bottom) * (1.0f - r14))) + 0.5d)));
            goto L9
        L11:
            return r0.build();
        }

        static void setCallback(View r3, Callback r4) {
            Object r0 = r3.getTag(C0039R.id.tag_on_apply_window_listener);
            if (r4 != null) goto L7;
            r3.setTag(C0039R.id.tag_window_insets_animation_callback, null);
            if (r0 != null) goto L11;
            r3.setOnApplyWindowInsetsListener(null);
            return;
        L11:
            return;
        L7:
            View.OnApplyWindowInsetsListener r1 = createProxyListener(r3, r4);
            r3.setTag(C0039R.id.tag_window_insets_animation_callback, r1);
            if (r0 != null) goto L13;
            r3.setOnApplyWindowInsetsListener(r1);
            return;
        }
    }

    private static class Impl30 extends Impl {
        private final WindowInsetsAnimation mWrapped;

        private static class ProxyCallback extends WindowInsetsAnimation.Callback {
            private final HashMap<WindowInsetsAnimation, WindowInsetsAnimationCompat> mAnimations;
            private final Callback mCompat;
            private List<WindowInsetsAnimationCompat> mRORunningAnimations;
            private ArrayList<WindowInsetsAnimationCompat> mTmpRunningAnimations;

            ProxyCallback(Callback r2) {
                super(r2.getDispatchMode());
                this.mAnimations = new HashMap();
                this.mCompat = r2;
            }

            private WindowInsetsAnimationCompat getWindowInsetsAnimationCompat(WindowInsetsAnimation r3) {
                WindowInsetsAnimationCompat r0 = this.mAnimations.get(r3);
                if (r0 != null) goto L6;
                WindowInsetsAnimationCompat r1 = WindowInsetsAnimationCompat.toWindowInsetsAnimationCompat(r3);
                this.mAnimations.put(r3, r1);
                return r1;
            L6:
                return r0;
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public void onEnd(WindowInsetsAnimation r3) {
                this.mCompat.onEnd(getWindowInsetsAnimationCompat(r3));
                this.mAnimations.remove(r3);
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public void onPrepare(WindowInsetsAnimation r3) {
                this.mCompat.onPrepare(getWindowInsetsAnimationCompat(r3));
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public WindowInsets onProgress(WindowInsets r5, List<WindowInsetsAnimation> r6) {
                if (this.mTmpRunningAnimations != null) goto L5;
                this.mTmpRunningAnimations = new ArrayList(r6.size());
                this.mRORunningAnimations = Collections.unmodifiableList(this.mTmpRunningAnimations);
            L6:
                int r0 = r6.size() - 1;
            L7:
                if (r0 < 0) goto L10;
                WindowInsetsAnimation r1 = r6.get(r0);
                WindowInsetsAnimationCompat r2 = getWindowInsetsAnimationCompat(r1);
                r2.setFraction(r1.getFraction());
                this.mTmpRunningAnimations.add(r2);
                r0 = r0 - 1;
                goto L7
            L10:
                return this.mCompat.onProgress(WindowInsetsCompat.toWindowInsetsCompat(r5), this.mRORunningAnimations).toWindowInsets();
            L5:
                this.mTmpRunningAnimations.clear();
                goto L6
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public WindowInsetsAnimation.Bounds onStart(WindowInsetsAnimation r4, WindowInsetsAnimation.Bounds r5) {
                return this.mCompat.onStart(getWindowInsetsAnimationCompat(r4), BoundsCompat.toBoundsCompat(r5)).toBounds();
            }
        }

        Impl30(int r2, Interpolator r3, long r4) {
            this(new WindowInsetsAnimation(r2, r3, r4));
        }

        Impl30(WindowInsetsAnimation r5) {
            super(0, null, 0);
            this.mWrapped = r5;
        }

        public static WindowInsetsAnimation.Bounds createPlatformBounds(BoundsCompat r3) {
            return new WindowInsetsAnimation.Bounds(r3.getLowerBound().toPlatformInsets(), r3.getUpperBound().toPlatformInsets());
        }

        public static Insets getHigherBounds(WindowInsetsAnimation.Bounds r1) {
            return Insets.toCompatInsets(r1.getUpperBound());
        }

        public static Insets getLowerBounds(WindowInsetsAnimation.Bounds r1) {
            return Insets.toCompatInsets(r1.getLowerBound());
        }

        public static void setCallback(View r1, Callback r2) {
            if (r2 == null) goto L4;
            ProxyCallback r0 = new ProxyCallback(r2);
        L5:
            r1.setWindowInsetsAnimationCallback(r0);
            return;
        L4:
            r0 = null;
            goto L5
        }

        @Override // androidx.core.view.WindowInsetsAnimationCompat.Impl
        public long getDurationMillis() {
            return this.mWrapped.getDurationMillis();
        }

        @Override // androidx.core.view.WindowInsetsAnimationCompat.Impl
        public float getFraction() {
            return this.mWrapped.getFraction();
        }

        @Override // androidx.core.view.WindowInsetsAnimationCompat.Impl
        public float getInterpolatedFraction() {
            return this.mWrapped.getInterpolatedFraction();
        }

        @Override // androidx.core.view.WindowInsetsAnimationCompat.Impl
        public Interpolator getInterpolator() {
            return this.mWrapped.getInterpolator();
        }

        @Override // androidx.core.view.WindowInsetsAnimationCompat.Impl
        public int getTypeMask() {
            return this.mWrapped.getTypeMask();
        }

        @Override // androidx.core.view.WindowInsetsAnimationCompat.Impl
        public void setFraction(float r2) {
            this.mWrapped.setFraction(r2);
        }
    }

    public WindowInsetsAnimationCompat(int r3, Interpolator r4, long r5) {
        if (Build.VERSION.SDK_INT < 30) goto L5;
        this.mImpl = new Impl30(r3, r4, r5);
        return;
    L5:
        this.mImpl = new Impl21(r3, r4, r5);
    }

    private WindowInsetsAnimationCompat(WindowInsetsAnimation r5) {
        this(0, null, 0);
        if (Build.VERSION.SDK_INT < 30) goto L6;
        this.mImpl = new Impl30(r5);
        return;
    }

    static void setCallback(View r2, Callback r3) {
        if (Build.VERSION.SDK_INT < 30) goto L5;
        Impl30.setCallback(r2, r3);
        return;
    L5:
        Impl21.setCallback(r2, r3);
    }

    static WindowInsetsAnimationCompat toWindowInsetsAnimationCompat(WindowInsetsAnimation r1) {
        return new WindowInsetsAnimationCompat(r1);
    }

    public float getAlpha() {
        return this.mImpl.getAlpha();
    }

    public long getDurationMillis() {
        return this.mImpl.getDurationMillis();
    }

    public float getFraction() {
        return this.mImpl.getFraction();
    }

    public float getInterpolatedFraction() {
        return this.mImpl.getInterpolatedFraction();
    }

    public Interpolator getInterpolator() {
        return this.mImpl.getInterpolator();
    }

    public int getTypeMask() {
        return this.mImpl.getTypeMask();
    }

    public void setAlpha(float r2) {
        this.mImpl.setAlpha(r2);
    }

    public void setFraction(float r2) {
        this.mImpl.setFraction(r2);
    }
}
