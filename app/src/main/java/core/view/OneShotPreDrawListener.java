package androidx.core.view;

import android.view.View;
import android.view.ViewTreeObserver;

/* JADX INFO: loaded from: classes.dex */
public final class OneShotPreDrawListener implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {
    private final Runnable mRunnable;
    private final View mView;
    private ViewTreeObserver mViewTreeObserver;

    private OneShotPreDrawListener(View r2, Runnable r3) {
        this.mView = r2;
        this.mViewTreeObserver = r2.getViewTreeObserver();
        this.mRunnable = r3;
    }

    public static OneShotPreDrawListener add(View r2, Runnable r3) {
        if (r2 == null) goto L9;
        if (r3 == null) goto L7;
        OneShotPreDrawListener r0 = new OneShotPreDrawListener(r2, r3);
        r2.getViewTreeObserver().addOnPreDrawListener(r0);
        r2.addOnAttachStateChangeListener(r0);
        return r0;
    L7:
        throw new NullPointerException("runnable == null");
    L9:
        throw new NullPointerException("view == null");
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        removeListener();
        this.mRunnable.run();
        return true;
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View r2) {
        this.mViewTreeObserver = r2.getViewTreeObserver();
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View r1) {
        removeListener();
    }

    public void removeListener() {
        if (this.mViewTreeObserver.isAlive() == false) goto L5;
        this.mViewTreeObserver.removeOnPreDrawListener(this);
    L6:
        this.mView.removeOnAttachStateChangeListener(this);
        return;
    L5:
        this.mView.getViewTreeObserver().removeOnPreDrawListener(this);
        goto L6
    }
}
