package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes.dex */
final class WrappedDrawableState extends Drawable.ConstantState {
    int mChangingConfigurations;
    Drawable.ConstantState mDrawableState;
    ColorStateList mTint;
    PorterDuff.Mode mTintMode;

    WrappedDrawableState(WrappedDrawableState r2) {
        this.mTint = null;
        this.mTintMode = WrappedDrawableApi14.DEFAULT_TINT_MODE;
        if (r2 == null) goto L6;
        this.mChangingConfigurations = r2.mChangingConfigurations;
        this.mDrawableState = r2.mDrawableState;
        this.mTint = r2.mTint;
        this.mTintMode = r2.mTintMode;
        return;
    }

    boolean canConstantState() {
        if (this.mDrawableState == null) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // android.graphics.drawable.Drawable.ConstantState
    public int getChangingConfigurations() {
        int r0 = this.mChangingConfigurations;
        if (this.mDrawableState == null) goto L5;
        int r1 = this.mDrawableState.getChangingConfigurations();
    L7:
        return r0 | r1;
    L5:
        r1 = 0;
        goto L7
    }

    @Override // android.graphics.drawable.Drawable.ConstantState
    public Drawable newDrawable() {
        return newDrawable(null);
    }

    @Override // android.graphics.drawable.Drawable.ConstantState
    public Drawable newDrawable(Resources r2) {
        return new WrappedDrawableApi21(this, r2);
    }
}
