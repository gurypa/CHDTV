package es.antonio.m3u;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.media3.common.C0180C;

/* JADX INFO: loaded from: classes3.dex */
final class ChdtvBrand {

    static final class Wordmark extends Drawable {
        private final Context context;

        /* JADX INFO: renamed from: p */
        private final Paint f178p;

        Wordmark(Context r3) {
            this.f178p = new Paint(1);
            this.context = r3;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas r20) {
            Rect r2 = getBounds();
            if (r2.isEmpty() == false) goto L5;
            return;
        L5:
            this.f178p.setTypeface(Typeface.create(C0180C.SANS_SERIF_NAME, 3));
            this.f178p.setTextSize(48.0f);
            float r3 = this.f178p.measureText("CHD");
            Rect r6 = new Rect();
            Rect r7 = new Rect();
            this.f178p.getTextBounds("CHD", 0, 3, r6);
            this.f178p.getTextBounds("TV", 0, 2, r7);
            float r5 = Math.min(0, r6.left);
            float r8 = Math.max(r6.right, r7.right + r3);
            float r9 = Math.min(r6.top, r7.top);
            float r11 = Math.max(r6.bottom, r7.bottom);
            float r13 = Math.min(r2.width() / ((r8 - r5) + (2.0f * 2.0f)), r2.height() / ((r11 - r9) + (2.0f * 2.0f)));
            int r14 = r20.save();
            r20.translate(r2.exactCenterX() - (((r5 + r8) * r13) / 2.0f), r2.exactCenterY() - (((r9 + r11) * r13) / 2.0f));
            r20.scale(r13, r13);
            this.f178p.setColor(-723219);
            r20.drawText("CHD", 0.0f, 0.0f, this.f178p);
            this.f178p.setColor(AppTheme.cineAccent(this.context));
            r20.drawText("TV", r3, 0.0f, this.f178p);
            r20.restoreToCount(r14);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int r2) {
            this.f178p.setAlpha(r2);
            invalidateSelf();
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter r2) {
            this.f178p.setColorFilter(r2);
            invalidateSelf();
        }
    }

    ChdtvBrand() {
    }

    static void show(ImageView r2) {
        if (AppTheme.cine(r2.getContext()) == true) goto L6;
        r2.setImageResource(C1028R.drawable.brand_transparent);
        return;
    L6:
        r2.setContentDescription("CHDTV");
        r2.setImageDrawable(new Wordmark(r2.getContext()));
    }
}
