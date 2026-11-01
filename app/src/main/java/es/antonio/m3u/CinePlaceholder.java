package es.antonio.m3u;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes3.dex */
final class CinePlaceholder extends Drawable {
    private final Context context;

    /* JADX INFO: renamed from: p */
    private final Paint f180p;

    CinePlaceholder(Context r3) {
        this.f180p = new Paint(1);
        this.context = r3;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas r10) {
        Rect r0 = getBounds();
        this.f180p.setColor(AppTheme.cineSurface(this.context));
        r10.drawRect(r0, this.f180p);
        float r1 = Math.min(r0.width(), r0.height()) * 0.18f;
        float r2 = r0.exactCenterX();
        float r3 = r0.exactCenterY();
        this.f180p.setColor(AppTheme.cineAccent(this.context));
        this.f180p.setStyle(Paint.Style.STROKE);
        this.f180p.setStrokeWidth(Math.max(1.0f, 0.04f * r1));
        r10.drawCircle(r2, r3, r1, this.f180p);
        this.f180p.setStyle(Paint.Style.FILL);
        Path r4 = new Path();
        r4.moveTo(r2 - (r1 * 0.25f), r3 - (r1 * 0.5f));
        r4.lineTo((r1 * 0.5f) + r2, r3);
        r4.lineTo(r2 - (0.25f * r1), (0.5f * r1) + r3);
        r4.close();
        r10.drawPath(r4, this.f180p);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -1;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int r1) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter r1) {
    }
}
