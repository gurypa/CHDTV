package es.antonio.m3u;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
final class HomeTile95 extends LinearLayout {
    private static final Map<Integer, Bitmap> ART = null;
    final TextView detail;
    final ImageView image;
    final TextView title;

    private static final class Surface extends Drawable {
        private final int accent;
        private final int dark;
        private final float density;
        private boolean focused;
        private final int light;

        /* JADX INFO: renamed from: p */
        private final Paint f183p;

        Surface(Context r3) {
            this.f183p = new Paint(1);
            this.dark = AppTheme.color(r3, -16245971);
            this.light = AppTheme.color(r3, -15444094);
            this.accent = AppTheme.color(r3, -11477516);
            this.density = r3.getResources().getDisplayMetrics().density;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas r19) {
            Rect r2 = getBounds();
            RectF r3 = new RectF(r2.left + (this.density * 2.0f), r2.top + (this.density * 2.0f), r2.right - (this.density * 2.0f), r2.bottom - (this.density * 2.0f));
            float r5 = this.density * 18.0f;
            this.f183p.setStyle(Paint.Style.FILL);
            this.f183p.setShader(new LinearGradient(r3.left, r3.top, r3.right, r3.bottom, new int[]{this.light, this.dark}, null, Shader.TileMode.CLAMP));
            r19.drawRoundRect(r3, r5, r5, this.f183p);
            int r4 = r19.save();
            Path r6 = new Path();
            r6.addRoundRect(r3, r5, r5, Path.Direction.CW);
            r19.clipPath(r6);
            this.f183p.setShader(new RadialGradient(r3.right - (r3.width() * 0.18f), r3.bottom * 0.85f, r3.width() * 0.52f, new int[]{(this.accent & ViewCompat.MEASURED_SIZE_MASK) | 889192448, 0}, null, Shader.TileMode.CLAMP));
            r19.drawRect(r3, this.f183p);
            this.f183p.setShader(null);
            this.f183p.setColor((this.accent & ViewCompat.MEASURED_SIZE_MASK) | 369098752);
            Path r7 = new Path();
            r7.moveTo(r3.left, r3.bottom * 0.75f);
            r7.cubicTo((r3.width() * 0.35f) + r3.left, r3.top + (r3.height() * 0.3f), r3.right - (r3.width() * 0.35f), r3.bottom, r3.right, (r3.height() * 0.28f) + r3.top);
            r7.lineTo(r3.right, r3.bottom);
            r7.close();
            r19.drawPath(r7, this.f183p);
            r19.restoreToCount(r4);
            Paint r8 = this.f183p;
            if (this.focused == false) goto L5;
            int r9 = this.accent;
        L6:
            r8.setColor(r9);
            Paint r10 = this.f183p;
            if (this.focused == false) goto L9;
            float r11 = 2.5f;
        L10:
            r10.setStrokeWidth(r11 * this.density);
            this.f183p.setStyle(Paint.Style.STROKE);
            r19.drawRoundRect(r3, r5, r5, this.f183p);
            if (this.focused == false) goto L13;
            this.f183p.setColor((this.accent & ViewCompat.MEASURED_SIZE_MASK) | 889192448);
            this.f183p.setStrokeWidth(this.density * 7.0f);
            r19.drawRoundRect(r3, r5, r5, this.f183p);
        L13:
            this.f183p.setStyle(Paint.Style.FILL);
            return;
        L9:
            r11 = 0.7f;
            goto L10
        L5:
            r9 = (this.accent & ViewCompat.MEASURED_SIZE_MASK) | (-2013265920);
            goto L6
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public boolean isStateful() {
            return true;
        }

        @Override // android.graphics.drawable.Drawable
        protected boolean onStateChange(int[] r7) {
            boolean r0 = false;
            int r1 = r7.length;
            boolean r2 = false;
            int r3 = 0;
        L3:
            if (r3 >= r1) goto L15;
            int r4 = r7[r3];
            if (r4 != 16842908) goto L7;
        L12:
            r0 = true;
        L13:
            r3 = r3 + 1;
            goto L3
        L7:
            if (r4 == 16842919) goto L12;
            if (r4 == 16842913) goto L12;
            if (r4 != 16843518) goto L13;
        L15:
            if (r0 == this.focused) goto L17;
            r2 = true;
        L17:
            this.focused = r0;
            if (r2 == false) goto L20;
            invalidateSelf();
        L20:
            return r2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int r2) {
            this.f183p.setAlpha(r2);
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter r2) {
            this.f183p.setColorFilter(r2);
        }
    }

    static {
        ART = new HashMap();
    }

    HomeTile95(Context r8, String r9, String r10, int r11, int r12) {
        super(r8);
        setOrientation(0);
        setGravity(16);
        setPadding(m401dp(12), m401dp(10), m401dp(8), m401dp(10));
        setDescendantFocusability(393216);
        setBackground(new Surface(r8));
        setLayoutParams(new AbsListView.LayoutParams(-1, r12));
        LinearLayout r2 = new LinearLayout(r8);
        r2.setOrientation(1);
        r2.setGravity(16);
        addView(r2, new LinearLayout.LayoutParams(0, -1, 0.48f));
        this.title = new TextView(r8);
        this.title.setText(r9);
        TextView r1 = this.title;
        if ((r8.getResources().getDisplayMetrics().widthPixels / r8.getResources().getDisplayMetrics().density) < 600.0f) goto L5;
        float r5 = 24.0f;
    L6:
        r1.setTextSize(r5);
        this.title.setTextColor(-1182209);
        this.title.setTypeface(null, 1);
        this.title.setTag("keepColor");
        this.title.setMaxLines(2);
        r2.addView(this.title);
        this.detail = new TextView(r8);
        this.detail.setText(r10);
        this.detail.setTextSize(12.0f);
        this.detail.setTextColor(AppTheme.color(r8, -6571311));
        this.detail.setMaxLines(2);
        this.detail.setEllipsize(TextUtils.TruncateAt.END);
        this.detail.setPadding(0, m401dp(6), 0, 0);
        r2.addView(this.detail);
        this.image = new ImageView(r8);
        this.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.image.setContentDescription(r9);
        this.image.setImageBitmap(art(r8, r11));
        this.image.setColorFilter(AppTheme.artworkFilter(r8));
        addView(this.image, new LinearLayout.LayoutParams(0, -1, 0.52f));
        setContentDescription(r9 + " · " + r10);
        return;
    L5:
        r5 = 18.0f;
        goto L6
    }

    private static synchronized Bitmap art(Context r5, int r6) {
        monitor-enter(HomeTile95.class);
        Bitmap r1 = ART.get(Integer.valueOf(r6));     // Catch: Throwable -> L11
        if (r1 != null) goto L9;
        BitmapFactory.Options r2 = new BitmapFactory.Options();     // Catch: Throwable -> L11
        r2.inSampleSize = 2;     // Catch: Throwable -> L11
        r2.inScaled = false;     // Catch: Throwable -> L11
        r1 = BitmapFactory.decodeResource(r5.getResources(), r6, r2);     // Catch: Throwable -> L11
        if (r1 == null) goto L9;
        ART.put(Integer.valueOf(r6), r1);     // Catch: Throwable -> L11
    L9:
        monitor-exit(HomeTile95.class);
        return r1;
    L11:
        th = move-exception;
        throw th;
    }

    /* JADX INFO: renamed from: dp */
    private int m401dp(int r3) {
        return Math.round(r3 * getResources().getDisplayMetrics().density);
    }
}
