package es.antonio.m3u;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/* JADX INFO: loaded from: classes3.dex */
final class PlayerControlBar extends HorizontalScrollView {
    final LinearLayout row;

    PlayerControlBar(Context r5) {
        super(r5);
        setHorizontalScrollBarEnabled(false);
        setFillViewport(false);
        this.row = new LinearLayout(r5);
        this.row.setGravity(16);
        addView(this.row, new FrameLayout.LayoutParams(-2, -1));
        setContentDescription("Controles de reproducción");
    }

    /* JADX INFO: renamed from: dp */
    private int m405dp(int r3) {
        return Math.round(r3 * getResources().getDisplayMetrics().density);
    }

    void addControl(View r7) {
        LinearLayout.LayoutParams r0 = new LinearLayout.LayoutParams(m405dp(64), m405dp(48));
        r0.setMargins(m405dp(2), 0, m405dp(2), 0);
        if ((r7 instanceof TextView) == false) goto L7;
        TextView r2 = (TextView) r7;
        r2.setMaxLines(2);
        if (Build.VERSION.SDK_INT < 26) goto L7;
        r2.setAutoSizeTextTypeUniformWithConfiguration(10, 12, 1, 2);
    L7:
        this.row.addView(r7, r0);
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int r8, int r9) {
        int r0 = Math.max(1, this.row.getChildCount());
        int r1 = (View.MeasureSpec.getSize(r8) - getPaddingLeft()) - getPaddingRight();
        int r2 = Math.max(m405dp(48), Math.min(m405dp(96), (r1 - (m405dp(4) * r0)) / r0));
        int r3 = 0;
    L4:
        if (r3 >= this.row.getChildCount()) goto L9;
        View r4 = this.row.getChildAt(r3);
        LinearLayout.LayoutParams r5 = (LinearLayout.LayoutParams) r4.getLayoutParams();
        if (r5.width == r2) goto L8;
        r5.width = r2;
        r4.setLayoutParams(r5);
    L8:
        r3 = r3 + 1;
        goto L4
    L9:
        super.onMeasure(r8, r9);
    }
}
