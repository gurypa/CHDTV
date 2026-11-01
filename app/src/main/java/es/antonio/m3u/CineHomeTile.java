package es.antonio.m3u;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/* JADX INFO: loaded from: classes3.dex */
final class CineHomeTile extends LinearLayout {
    final TextView detail;
    final ImageView icon;
    final TextView title;

    private static final class CategoryIcon extends Drawable {
        final String category;
        final Context context;

        /* JADX INFO: renamed from: p */
        final Paint f179p;

        CategoryIcon(Context r3, String r4) {
            this.f179p = new Paint(1);
            this.context = r3;
            this.category = r4;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas r12) {
            Rect r8 = getBounds();
            int r9 = r12.save();
            r12.translate(r8.left, r8.top);
            r12.scale(r8.width() / 48.0f, r8.height() / 48.0f);
            this.f179p.setColor(AppTheme.cineAccent(this.context));
            this.f179p.setStyle(Paint.Style.STROKE);
            this.f179p.setStrokeWidth(2.0f);
            this.f179p.setStrokeCap(Paint.Cap.ROUND);
            this.f179p.setStrokeJoin(Paint.Join.ROUND);
            if (this.category.equals("TV") == false) goto L6;
            r12.drawRoundRect(4.0f, 7.0f, 44.0f, 35.0f, 4.0f, 4.0f, this.f179p);
            r12.drawLine(24.0f, 35.0f, 24.0f, 41.0f, this.f179p);
            r12.drawLine(15.0f, 41.0f, 33.0f, 41.0f, this.f179p);
        L29:
            r12.restoreToCount(r9);
            return;
        L6:
            if (this.category.equals("EPG") == false) goto L17;
            r12.drawRoundRect(7.0f, 9.0f, 41.0f, 42.0f, 4.0f, 4.0f, this.f179p);
            r12.drawLine(7.0f, 18.0f, 41.0f, 18.0f, this.f179p);
            r12.drawLine(16.0f, 5.0f, 16.0f, 13.0f, this.f179p);
            r12.drawLine(32.0f, 5.0f, 32.0f, 13.0f, this.f179p);
            int r1 = 24;
        L9:
            if (r1 > 34) goto L29;
            int r2 = 15;
        L12:
            if (r2 > 33) goto L14;
            r12.drawPoint(r2, r1, this.f179p);
            r2 = r2 + 9;
            goto L12
        L14:
            r1 = r1 + 10;
            goto L9
        L17:
            if (this.category.equals("Plex") == false) goto L20;
            Path r3 = new Path();
            r3.moveTo(17.0f, 5.0f);
            r3.lineTo(31.0f, 24.0f);
            r3.lineTo(17.0f, 43.0f);
            r12.drawPath(r3, this.f179p);
            goto L29
        L20:
            if (this.category.equals("Series") == false) goto L22;
            r12.drawRoundRect(12.0f, 5.0f, 43.0f, 33.0f, 3.0f, 3.0f, this.f179p);
            r12.drawRoundRect(7.0f, 10.0f, 38.0f, 38.0f, 3.0f, 3.0f, this.f179p);
        L22:
            r12.drawRoundRect(3.0f, 15.0f, 34.0f, 43.0f, 3.0f, 3.0f, this.f179p);
            if (this.category.equals("Películas") == false) goto L28;
            r12.drawLine(3.0f, 23.0f, 34.0f, 23.0f, this.f179p);
            int r6 = 7;
        L26:
            if (r6 >= 32) goto L28;
            r12.drawLine(r6, 15.0f, r6 - 4, 23.0f, this.f179p);
            r6 = r6 + 8;
        L28:
            Path r4 = new Path();
            r4.moveTo(14.0f, 28.0f);
            r4.lineTo(24.0f, 33.0f);
            r4.lineTo(14.0f, 38.0f);
            r4.close();
            r12.drawPath(r4, this.f179p);
            goto L29
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int r1) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter r1) {
        }
    }

    CineHomeTile(Context r9, String r10, String r11, int r12) {
        super(r9);
        setOrientation(1);
        setGravity(16);
        setPadding(m395dp(18), m395dp(12), m395dp(18), m395dp(12));
        setDescendantFocusability(393216);
        setBackground(PosterTile.background(r9));
        setLayoutParams(new AbsListView.LayoutParams(-1, r12));
        this.icon = new ImageView(r9);
        this.icon.setImageDrawable(new CategoryIcon(r9, r10));
        this.icon.setContentDescription(r10);
        ImageView r1 = this.icon;
        int r5 = 36;
        if (r12 >= m395dp(150)) goto L5;
        int r4 = 36;
    L6:
        int r6 = m395dp(r4);
        if (r12 < m395dp(150)) goto L10;
        r5 = 48;
    L10:
        addView(r1, new LinearLayout.LayoutParams(r6, m395dp(r5)));
        this.title = new TextView(r9);
        this.title.setText(r10);
        this.title.setTextColor(-1182209);
        this.title.setTypeface(null, 1);
        TextView r0 = this.title;
        if (r12 >= m395dp(150)) goto L13;
        float r2 = 18.0f;
    L14:
        r0.setTextSize(r2);
        this.title.setPadding(0, m395dp(10), 0, m395dp(4));
        this.title.setTag("keepColor");
        addView(this.title);
        this.detail = new TextView(r9);
        this.detail.setText(r11);
        this.detail.setTextColor(AppTheme.color(r9, -6244403));
        this.detail.setTextSize(12.0f);
        this.detail.setMaxLines(2);
        this.detail.setEllipsize(TextUtils.TruncateAt.END);
        addView(this.detail);
        setContentDescription(r10 + " · " + r11);
        return;
    L13:
        r2 = 22.0f;
        goto L14
    L5:
        r4 = 48;
        goto L6
    }

    /* JADX INFO: renamed from: dp */
    private int m395dp(int r3) {
        return Math.round(r3 * getResources().getDisplayMetrics().density);
    }
}
