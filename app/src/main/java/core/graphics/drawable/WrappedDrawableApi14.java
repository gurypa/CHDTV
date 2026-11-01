package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes.dex */
class WrappedDrawableApi14 extends Drawable implements Drawable.Callback, WrappedDrawable, TintAwareDrawable {
    static final PorterDuff.Mode DEFAULT_TINT_MODE = null;
    private boolean mColorFilterSet;
    private int mCurrentColor;
    private PorterDuff.Mode mCurrentMode;
    Drawable mDrawable;
    private boolean mMutated;
    WrappedDrawableState mState;

    static {
        DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
    }

    WrappedDrawableApi14(Drawable r2) {
        this.mState = mutateConstantState();
        setWrappedDrawable(r2);
    }

    WrappedDrawableApi14(WrappedDrawableState r1, Resources r2) {
        this.mState = r1;
        updateLocalState(r2);
    }

    private WrappedDrawableState mutateConstantState() {
        return new WrappedDrawableState(this.mState);
    }

    private void updateLocalState(Resources r2) {
        if (this.mState != null) goto L5;
        return;
    L5:
        if (this.mState.mDrawableState == null) goto L9;
        setWrappedDrawable(this.mState.mDrawableState.newDrawable(r2));
        return;
    }

    private boolean updateTint(int[] r6) {
        if (isCompatTintEnabled() == true) goto L5;
        return false;
    L5:
        ColorStateList r0 = this.mState.mTint;
        PorterDuff.Mode r2 = this.mState.mTintMode;
        if (r0 == null) goto L18;
        if (r2 == null) goto L18;
        int r3 = r0.getColorForState(r6, r0.getDefaultColor());
        if (this.mColorFilterSet == true) goto L11;
    L16:
        setColorFilter(r3, r2);
        this.mCurrentColor = r3;
        this.mCurrentMode = r2;
        this.mColorFilterSet = true;
        return true;
    L11:
        if (r3 != this.mCurrentColor) goto L16;
        if (r2 != this.mCurrentMode) goto L16;
    L19:
        return false;
    L18:
        this.mColorFilterSet = false;
        clearColorFilter();
        goto L19
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas r2) {
        this.mDrawable.draw(r2);
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        int r0 = super.getChangingConfigurations();
        if (this.mState == null) goto L5;
        int r1 = this.mState.getChangingConfigurations();
    L7:
        return (r0 | r1) | this.mDrawable.getChangingConfigurations();
    L5:
        r1 = 0;
        goto L7
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        if (this.mState != null) goto L5;
        return null;
    L5:
        if (this.mState.canConstantState() == false) goto L10;
        WrappedDrawableState r0 = this.mState;
        r0.mChangingConfigurations = getChangingConfigurations();
        return this.mState;
    L10:
        return null;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable getCurrent() {
        return this.mDrawable.getCurrent();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mDrawable.getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mDrawable.getIntrinsicWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getLayoutDirection() {
        return DrawableCompat.getLayoutDirection(this.mDrawable);
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumHeight() {
        return this.mDrawable.getMinimumHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumWidth() {
        return this.mDrawable.getMinimumWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return this.mDrawable.getOpacity();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect r2) {
        return this.mDrawable.getPadding(r2);
    }

    @Override // android.graphics.drawable.Drawable
    public int[] getState() {
        return this.mDrawable.getState();
    }

    @Override // android.graphics.drawable.Drawable
    public Region getTransparentRegion() {
        return this.mDrawable.getTransparentRegion();
    }

    @Override // androidx.core.graphics.drawable.WrappedDrawable
    public final Drawable getWrappedDrawable() {
        return this.mDrawable;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable r1) {
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isAutoMirrored() {
        return DrawableCompat.isAutoMirrored(this.mDrawable);
    }

    protected boolean isCompatTintEnabled() {
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        if (isCompatTintEnabled() == true) goto L5;
    L7:
        ColorStateList r0 = null;
    L9:
        if (r0 == null) goto L13;
        if (r0.isStateful() == false) goto L13;
    L14:
        return true;
    L13:
        if (this.mDrawable.isStateful() == true) goto L14;
        return false;
    L5:
        if (this.mState == null) goto L7;
        r0 = this.mState.mTint;
        goto L9
    }

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        this.mDrawable.jumpToCurrentState();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (this.mMutated == false) goto L5;
    L17:
        return this;
    L5:
        if (super.mutate() != this) goto L17;
        this.mState = mutateConstantState();
        if (this.mDrawable == null) goto L10;
        this.mDrawable.mutate();
    L10:
        if (this.mState == null) goto L16;
        WrappedDrawableState r0 = this.mState;
        if (this.mDrawable == null) goto L14;
        Drawable.ConstantState r1 = this.mDrawable.getConstantState();
    L15:
        r0.mDrawableState = r1;
        goto L16
    L14:
        r1 = null;
    L16:
        this.mMutated = true;
        goto L17
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect r2) {
        if (this.mDrawable == null) goto L6;
        this.mDrawable.setBounds(r2);
        return;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onLayoutDirectionChanged(int r2) {
        return DrawableCompat.setLayoutDirection(this.mDrawable, r2);
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int r2) {
        return this.mDrawable.setLevel(r2);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable r1, Runnable r2, long r3) {
        scheduleSelf(r2, r3);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int r2) {
        this.mDrawable.setAlpha(r2);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean r2) {
        DrawableCompat.setAutoMirrored(this.mDrawable, r2);
    }

    @Override // android.graphics.drawable.Drawable
    public void setChangingConfigurations(int r2) {
        this.mDrawable.setChangingConfigurations(r2);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter r2) {
        this.mDrawable.setColorFilter(r2);
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean r2) {
        this.mDrawable.setDither(r2);
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean r2) {
        this.mDrawable.setFilterBitmap(r2);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setState(int[] r3) {
        boolean r0 = this.mDrawable.setState(r3);
        if (updateTint(r3) == true) goto L7;
        if (r0 == true) goto L7;
        return false;
    L7:
        return true;
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.TintAwareDrawable
    public void setTint(int r2) {
        setTintList(ColorStateList.valueOf(r2));
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.TintAwareDrawable
    public void setTintList(ColorStateList r2) {
        this.mState.mTint = r2;
        updateTint(getState());
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.TintAwareDrawable
    public void setTintMode(PorterDuff.Mode r2) {
        this.mState.mTintMode = r2;
        updateTint(getState());
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean r2, boolean r3) {
        if (super.setVisible(r2, r3) == false) goto L5;
    L8:
        return true;
    L5:
        if (this.mDrawable.setVisible(r2, r3) == true) goto L8;
        return false;
    }

    @Override // androidx.core.graphics.drawable.WrappedDrawable
    public final void setWrappedDrawable(Drawable r3) {
        if (this.mDrawable == null) goto L5;
        this.mDrawable.setCallback(null);
    L5:
        this.mDrawable = r3;
        if (r3 == null) goto L10;
        r3.setCallback(this);
        setVisible(r3.isVisible(), true);
        setState(r3.getState());
        setLevel(r3.getLevel());
        setBounds(r3.getBounds());
        if (this.mState == null) goto L10;
        WrappedDrawableState r0 = this.mState;
        r0.mDrawableState = r3.getConstantState();
    L10:
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable r1, Runnable r2) {
        unscheduleSelf(r2);
    }
}
