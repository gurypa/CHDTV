package androidx.core.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

/* JADX INFO: loaded from: classes.dex */
public abstract class RoundedBitmapDrawable extends Drawable {
    private static final int DEFAULT_PAINT_FLAGS = 3;
    private boolean mApplyGravity;
    final Bitmap mBitmap;
    private int mBitmapHeight;
    private final BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private float mCornerRadius;
    final Rect mDstRect;
    private final RectF mDstRectF;
    private int mGravity;
    private boolean mIsCircular;
    private final Paint mPaint;
    private final Matrix mShaderMatrix;
    private int mTargetDensity;

    RoundedBitmapDrawable(Resources r5, Bitmap r6) {
        this.mTargetDensity = 160;
        this.mGravity = 119;
        this.mPaint = new Paint(3);
        this.mShaderMatrix = new Matrix();
        this.mDstRect = new Rect();
        this.mDstRectF = new RectF();
        this.mApplyGravity = true;
        if (r5 == null) goto L5;
        this.mTargetDensity = r5.getDisplayMetrics().densityDpi;
    L5:
        this.mBitmap = r6;
        if (this.mBitmap == null) goto L8;
        computeBitmapSize();
        this.mBitmapShader = new BitmapShader(this.mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        return;
    L8:
        this.mBitmapHeight = -1;
        this.mBitmapWidth = -1;
        this.mBitmapShader = null;
    }

    private void computeBitmapSize() {
        this.mBitmapWidth = this.mBitmap.getScaledWidth(this.mTargetDensity);
        this.mBitmapHeight = this.mBitmap.getScaledHeight(this.mTargetDensity);
    }

    private static boolean isGreaterThanZero(float r1) {
        if (r1 <= 0.05f) goto L5;
        return true;
    L5:
        return false;
    }

    private void updateCircularCornerRadius() {
        this.mCornerRadius = Math.min(this.mBitmapHeight, this.mBitmapWidth) / 2;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas r6) {
        Bitmap r0 = this.mBitmap;
        if (r0 != null) goto L5;
        return;
    L5:
        updateDstRect();
        if (this.mPaint.getShader() != null) goto L8;
        r6.drawBitmap(r0, null, this.mDstRect, this.mPaint);
        return;
    L8:
        r6.drawRoundRect(this.mDstRectF, this.mCornerRadius, this.mCornerRadius, this.mPaint);
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mPaint.getAlpha();
    }

    public final Bitmap getBitmap() {
        return this.mBitmap;
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        return this.mPaint.getColorFilter();
    }

    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    public int getGravity() {
        return this.mGravity;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        if (this.mGravity == 119) goto L5;
    L19:
        return -3;
    L5:
        if (this.mIsCircular == true) goto L19;
        Bitmap r0 = this.mBitmap;
        if (r0 != null) goto L10;
        return -3;
    L10:
        if (r0.hasAlpha() == false) goto L12;
        return -3;
    L12:
        if (this.mPaint.getAlpha() >= 255) goto L14;
        return -3;
    L14:
        if (isGreaterThanZero(this.mCornerRadius) == true) goto L23;
        return -1;
    L23:
        return -3;
    }

    public final Paint getPaint() {
        return this.mPaint;
    }

    void gravityCompatApply(int r2, int r3, int r4, Rect r5, Rect r6) {
        throw new UnsupportedOperationException();
    }

    public boolean hasAntiAlias() {
        return this.mPaint.isAntiAlias();
    }

    public boolean hasMipMap() {
        throw new UnsupportedOperationException();
    }

    public boolean isCircular() {
        return this.mIsCircular;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect r2) {
        super.onBoundsChange(r2);
        if (this.mIsCircular == false) goto L5;
        updateCircularCornerRadius();
    L5:
        this.mApplyGravity = true;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int r3) {
        if (r3 == this.mPaint.getAlpha()) goto L6;
        this.mPaint.setAlpha(r3);
        invalidateSelf();
        return;
    }

    public void setAntiAlias(boolean r2) {
        this.mPaint.setAntiAlias(r2);
        invalidateSelf();
    }

    public void setCircular(boolean r3) {
        this.mIsCircular = r3;
        this.mApplyGravity = true;
        if (r3 == false) goto L5;
        updateCircularCornerRadius();
        this.mPaint.setShader(this.mBitmapShader);
        invalidateSelf();
        return;
    L5:
        setCornerRadius(0.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter r2) {
        this.mPaint.setColorFilter(r2);
        invalidateSelf();
    }

    public void setCornerRadius(float r3) {
        if (this.mCornerRadius != r3) goto L5;
        return;
    L5:
        this.mIsCircular = false;
        if (isGreaterThanZero(r3) == false) goto L8;
        this.mPaint.setShader(this.mBitmapShader);
    L9:
        this.mCornerRadius = r3;
        invalidateSelf();
        return;
    L8:
        this.mPaint.setShader(null);
        goto L9
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean r2) {
        this.mPaint.setDither(r2);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean r2) {
        this.mPaint.setFilterBitmap(r2);
        invalidateSelf();
    }

    public void setGravity(int r2) {
        if (this.mGravity == r2) goto L6;
        this.mGravity = r2;
        this.mApplyGravity = true;
        invalidateSelf();
        return;
    }

    public void setMipMap(boolean r2) {
        throw new UnsupportedOperationException();
    }

    public void setTargetDensity(int r2) {
        if (this.mTargetDensity == r2) goto L12;
        if (r2 != 0) goto L6;
        int r0 = 160;
    L7:
        this.mTargetDensity = r0;
        if (this.mBitmap == null) goto L10;
        computeBitmapSize();
    L10:
        invalidateSelf();
        return;
    L6:
        r0 = r2;
        goto L7
    }

    public void setTargetDensity(Canvas r2) {
        setTargetDensity(r2.getDensity());
    }

    public void setTargetDensity(DisplayMetrics r2) {
        setTargetDensity(r2.densityDpi);
    }

    void updateDstRect() {
        if (this.mApplyGravity == false) goto L13;
        if (this.mIsCircular == false) goto L7;
        int r5 = Math.min(this.mBitmapWidth, this.mBitmapHeight);
        RoundedBitmapDrawable r3 = this;
        r3.gravityCompatApply(this.mGravity, r5, r5, getBounds(), this.mDstRect);
        int r0 = Math.min(r3.mDstRect.width(), r3.mDstRect.height());
        r3.mDstRect.inset(Math.max(0, (r3.mDstRect.width() - r0) / 2), Math.max(0, (r3.mDstRect.height() - r0) / 2));
        r3.mCornerRadius = r0 * 0.5f;
    L8:
        r3.mDstRectF.set(r3.mDstRect);
        if (r3.mBitmapShader == null) goto L11;
        r3.mShaderMatrix.setTranslate(r3.mDstRectF.left, r3.mDstRectF.top);
        r3.mShaderMatrix.preScale(r3.mDstRectF.width() / r3.mBitmap.getWidth(), r3.mDstRectF.height() / r3.mBitmap.getHeight());
        r3.mBitmapShader.setLocalMatrix(r3.mShaderMatrix);
        r3.mPaint.setShader(r3.mBitmapShader);
    L11:
        r3.mApplyGravity = false;
        return;
    L7:
        r3 = this;
        r3.gravityCompatApply(r3.mGravity, r3.mBitmapWidth, r3.mBitmapHeight, getBounds(), r3.mDstRect);
        goto L8
    }
}
