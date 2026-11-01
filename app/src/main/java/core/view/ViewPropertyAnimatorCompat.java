package androidx.core.view;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public final class ViewPropertyAnimatorCompat {
    static final int LISTENER_TAG_ID = 2113929216;
    Runnable mEndAction;
    int mOldLayerType;
    Runnable mStartAction;
    private final WeakReference<View> mView;

    static class Api16Impl {
        private Api16Impl() {
        }

        static ViewPropertyAnimator withEndAction(ViewPropertyAnimator r1, Runnable r2) {
            return r1.withEndAction(r2);
        }

        static ViewPropertyAnimator withLayer(ViewPropertyAnimator r1) {
            return r1.withLayer();
        }

        static ViewPropertyAnimator withStartAction(ViewPropertyAnimator r1, Runnable r2) {
            return r1.withStartAction(r2);
        }
    }

    static class Api18Impl {
        private Api18Impl() {
        }

        static Interpolator getInterpolator(ViewPropertyAnimator r1) {
            return (Interpolator) r1.getInterpolator();
        }
    }

    static class Api19Impl {
        private Api19Impl() {
        }

        static ViewPropertyAnimator setUpdateListener(ViewPropertyAnimator r1, ValueAnimator.AnimatorUpdateListener r2) {
            return r1.setUpdateListener(r2);
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static ViewPropertyAnimator translationZ(ViewPropertyAnimator r1, float r2) {
            return r1.translationZ(r2);
        }

        static ViewPropertyAnimator translationZBy(ViewPropertyAnimator r1, float r2) {
            return r1.translationZBy(r2);
        }

        /* JADX INFO: renamed from: z */
        static ViewPropertyAnimator m36z(ViewPropertyAnimator r1, float r2) {
            return r1.z(r2);
        }

        static ViewPropertyAnimator zBy(ViewPropertyAnimator r1, float r2) {
            return r1.zBy(r2);
        }
    }

    static class ViewPropertyAnimatorListenerApi14 implements ViewPropertyAnimatorListener {
        boolean mAnimEndCalled;
        ViewPropertyAnimatorCompat mVpa;

        ViewPropertyAnimatorListenerApi14(ViewPropertyAnimatorCompat r1) {
            this.mVpa = r1;
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListener
        public void onAnimationCancel(View r4) {
            Object r0 = r4.getTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
            ViewPropertyAnimatorListener r1 = null;
            if ((r0 instanceof ViewPropertyAnimatorListener) == false) goto L5;
            r1 = (ViewPropertyAnimatorListener) r0;
        L5:
            if (r1 == null) goto L8;
            r1.onAnimationCancel(r4);
            return;
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListener
        public void onAnimationEnd(View r4) {
            if (this.mVpa.mOldLayerType <= (-1)) goto L6;
            r4.setLayerType(this.mVpa.mOldLayerType, null);
            this.mVpa.mOldLayerType = -1;
        L6:
            if (this.mVpa.mEndAction == null) goto L8;
            Runnable r0 = this.mVpa.mEndAction;
            this.mVpa.mEndAction = null;
            r0.run();
        L8:
            Object r1 = r4.getTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
            ViewPropertyAnimatorListener r2 = null;
            if ((r1 instanceof ViewPropertyAnimatorListener) == false) goto L11;
            r2 = (ViewPropertyAnimatorListener) r1;
        L11:
            if (r2 == null) goto L13;
            r2.onAnimationEnd(r4);
        L13:
            this.mAnimEndCalled = true;
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListener
        public void onAnimationStart(View r4) {
            this.mAnimEndCalled = false;
            if (this.mVpa.mOldLayerType <= (-1)) goto L6;
            r4.setLayerType(2, null);
        L6:
            if (this.mVpa.mStartAction == null) goto L8;
            Runnable r0 = this.mVpa.mStartAction;
            this.mVpa.mStartAction = null;
            r0.run();
        L8:
            Object r1 = r4.getTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
            ViewPropertyAnimatorListener r2 = null;
            if ((r1 instanceof ViewPropertyAnimatorListener) == false) goto L11;
            r2 = (ViewPropertyAnimatorListener) r1;
        L11:
            if (r2 == null) goto L14;
            r2.onAnimationStart(r4);
            return;
        }
    }

    ViewPropertyAnimatorCompat(View r2) {
        this.mStartAction = null;
        this.mEndAction = null;
        this.mOldLayerType = -1;
        this.mView = new WeakReference(r2);
    }

    static /* synthetic */ void lambda$setUpdateListener$0(ViewPropertyAnimatorUpdateListener r0, View r1, ValueAnimator r2) {
        r0.onAnimationUpdate(r1);
    }

    private void setListenerInternal(final View r3, final ViewPropertyAnimatorListener r4) {
        if (r4 == null) goto L4;
        r3.animate().setListener(new C01241(this, r4, r3));
        return;
    L4:
        r3.animate().setListener(null);
    }

    public ViewPropertyAnimatorCompat alpha(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().alpha(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat alphaBy(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().alphaBy(r3);
    L5:
        return this;
    }

    public void cancel() {
        View r0 = this.mView.get();
        if (r0 == null) goto L6;
        r0.animate().cancel();
        return;
    }

    public long getDuration() {
        View r0 = this.mView.get();
        if (r0 != null) goto L5;
        return 0;
    L5:
        return r0.animate().getDuration();
    }

    public Interpolator getInterpolator() {
        View r0 = this.mView.get();
        if (r0 != null) goto L5;
        return null;
    L5:
        return Api18Impl.getInterpolator(r0.animate());
    }

    public long getStartDelay() {
        View r0 = this.mView.get();
        if (r0 != null) goto L5;
        return 0;
    L5:
        return r0.animate().getStartDelay();
    }

    public ViewPropertyAnimatorCompat rotation(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().rotation(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat rotationBy(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().rotationBy(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat rotationX(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().rotationX(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat rotationXBy(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().rotationXBy(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat rotationY(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().rotationY(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat rotationYBy(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().rotationYBy(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat scaleX(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().scaleX(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat scaleXBy(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().scaleXBy(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat scaleY(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().scaleY(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat scaleYBy(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().scaleYBy(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat setDuration(long r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().setDuration(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat setInterpolator(Interpolator r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().setInterpolator(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat setListener(ViewPropertyAnimatorListener r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        setListenerInternal(r0, r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat setStartDelay(long r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().setStartDelay(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat setUpdateListener(final ViewPropertyAnimatorUpdateListener r4) {
        final View r0 = this.mView.get();
        if (r0 == null) goto L8;
        ValueAnimator.AnimatorUpdateListener r1 = null;
        if (r4 == null) goto L7;
        r1 = new ViewPropertyAnimatorCompat$$ExternalSyntheticLambda0(r4, r0);
    L7:
        Api19Impl.setUpdateListener(r0.animate(), r1);
    L8:
        return this;
    }

    public void start() {
        View r0 = this.mView.get();
        if (r0 == null) goto L6;
        r0.animate().start();
        return;
    }

    public ViewPropertyAnimatorCompat translationX(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().translationX(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat translationXBy(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().translationXBy(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat translationY(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().translationY(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat translationYBy(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().translationYBy(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat translationZ(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        Api21Impl.translationZ(r0.animate(), r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat translationZBy(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        Api21Impl.translationZBy(r0.animate(), r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat withEndAction(Runnable r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        Api16Impl.withEndAction(r0.animate(), r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat withLayer() {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        Api16Impl.withLayer(r0.animate());
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat withStartAction(Runnable r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        Api16Impl.withStartAction(r0.animate(), r3);
    L5:
        return this;
    }

    /* JADX INFO: renamed from: x */
    public ViewPropertyAnimatorCompat m33x(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().x(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat xBy(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().xBy(r3);
    L5:
        return this;
    }

    /* JADX INFO: renamed from: y */
    public ViewPropertyAnimatorCompat m34y(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().y(r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat yBy(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        r0.animate().yBy(r3);
    L5:
        return this;
    }

    /* JADX INFO: renamed from: z */
    public ViewPropertyAnimatorCompat m35z(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        Api21Impl.m36z(r0.animate(), r3);
    L5:
        return this;
    }

    public ViewPropertyAnimatorCompat zBy(float r3) {
        View r0 = this.mView.get();
        if (r0 == null) goto L5;
        Api21Impl.zBy(r0.animate(), r3);
    L5:
        return this;
    }
}
