package androidx.core.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;
import androidx.core.graphics.BitmapCompat;
import androidx.core.view.GravityCompat;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public final class RoundedBitmapDrawableFactory {
    private static final String TAG = "RoundedBitmapDrawableFa";

    private static class DefaultRoundedBitmapDrawable extends RoundedBitmapDrawable {
        DefaultRoundedBitmapDrawable(Resources r1, Bitmap r2) {
            super(r1, r2);
        }

        @Override // androidx.core.graphics.drawable.RoundedBitmapDrawable
        void gravityCompatApply(int r7, int r8, int r9, Rect r10, Rect r11) {
            GravityCompat.apply(r7, r8, r9, r10, r11, 0);
        }

        @Override // androidx.core.graphics.drawable.RoundedBitmapDrawable
        public boolean hasMipMap() {
            if (this.mBitmap != null) goto L5;
        L7:
            return false;
        L5:
            if (BitmapCompat.hasMipMap(this.mBitmap) == false) goto L7;
            return true;
        }

        @Override // androidx.core.graphics.drawable.RoundedBitmapDrawable
        public void setMipMap(boolean r2) {
            if (this.mBitmap == null) goto L6;
            BitmapCompat.setHasMipMap(this.mBitmap, r2);
            invalidateSelf();
            return;
        }
    }

    private RoundedBitmapDrawableFactory() {
    }

    public static RoundedBitmapDrawable create(Resources r1, Bitmap r2) {
        return new RoundedBitmapDrawable21(r1, r2);
    }

    public static RoundedBitmapDrawable create(Resources r3, InputStream r4) {
        RoundedBitmapDrawable r0 = create(r3, BitmapFactory.decodeStream(r4));
        if (r0.getBitmap() != null) goto L5;
        Log.w(TAG, "RoundedBitmapDrawable cannot decode " + r4);
    L5:
        return r0;
    }

    public static RoundedBitmapDrawable create(Resources r3, String r4) {
        RoundedBitmapDrawable r0 = create(r3, BitmapFactory.decodeFile(r4));
        if (r0.getBitmap() != null) goto L5;
        Log.w(TAG, "RoundedBitmapDrawable cannot decode " + r4);
    L5:
        return r0;
    }
}
