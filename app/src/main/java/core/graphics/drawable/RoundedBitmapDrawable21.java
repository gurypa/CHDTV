package androidx.core.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.Rect;
import android.view.Gravity;

/* JADX INFO: loaded from: classes.dex */
class RoundedBitmapDrawable21 extends RoundedBitmapDrawable {
    protected RoundedBitmapDrawable21(Resources r1, Bitmap r2) {
        super(r1, r2);
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline r3) {
        updateDstRect();
        r3.setRoundRect(this.mDstRect, getCornerRadius());
    }

    @Override // androidx.core.graphics.drawable.RoundedBitmapDrawable
    void gravityCompatApply(int r7, int r8, int r9, Rect r10, Rect r11) {
        Gravity.apply(r7, r8, r9, r10, r11, 0);
    }

    @Override // androidx.core.graphics.drawable.RoundedBitmapDrawable
    public boolean hasMipMap() {
        if (this.mBitmap != null) goto L5;
    L7:
        return false;
    L5:
        if (this.mBitmap.hasMipMap() == false) goto L7;
        return true;
    }

    @Override // androidx.core.graphics.drawable.RoundedBitmapDrawable
    public void setMipMap(boolean r2) {
        if (this.mBitmap == null) goto L6;
        this.mBitmap.setHasMipMap(r2);
        invalidateSelf();
        return;
    }
}
