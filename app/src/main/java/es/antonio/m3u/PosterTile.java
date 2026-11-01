package es.antonio.m3u;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.media3.extractor.p001ts.PsExtractor;

/* JADX INFO: loaded from: classes3.dex */
final class PosterTile extends LinearLayout {
    final TextView badge;
    final TextView detail;
    final ImageView image;
    final ProgressBar progress;
    final TextView title;

    PosterTile(Context r13) {
        super(r13);
        setOrientation(1);
        setDescendantFocusability(393216);
        setBackground(background(r13));
        setForeground(selectionOverlay(r13));
        setElevation(m407dp(3));
        setClipToOutline(true);
        if (AppTheme.cineStage(r13) == false) goto L5;
        int r2 = 262;
    L6:
        setLayoutParams(new AbsListView.LayoutParams(-1, m407dp(r2)));
        FrameLayout r1 = new FrameLayout(r13);
        if (AppTheme.cineStage(r13) == false) goto L9;
        int r4 = 190;
    L10:
        addView(r1, new LinearLayout.LayoutParams(-1, m407dp(r4)));
        this.image = new ImageView(r13);
        this.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.image.setBackgroundColor(AppTheme.color(r13, -16050393));
        r1.addView(this.image, new FrameLayout.LayoutParams(-1, -1));
        View r3 = new View(r13);
        r3.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{AppTheme.color(r13, 464937), AppTheme.color(r13, -804841431)}));
        r1.addView(r3, new FrameLayout.LayoutParams(-1, m407dp(68), 80));
        this.progress = new ProgressBar(r13, null, android.R.attr.progressBarStyleHorizontal);
        this.progress.setMax(100);
        this.progress.setProgressTintList(ColorStateList.valueOf(AppTheme.color(r13, -12986907)));
        this.progress.setVisibility(8);
        r1.addView(this.progress, new FrameLayout.LayoutParams(-1, m407dp(5), 80));
        this.badge = label(r13, 10, AppTheme.color(r13, -3934721));
        this.badge.setTypeface(null, 1);
        this.badge.setPadding(m407dp(8), m407dp(4), m407dp(8), m407dp(4));
        this.badge.setBackground(shape(AppTheme.color(r13, -636338597), AppTheme.color(r13, 1080088831), m407dp(8)));
        FrameLayout.LayoutParams r5 = new FrameLayout.LayoutParams(-2, -2, 51);
        r5.setMargins(m407dp(9), m407dp(9), m407dp(9), 0);
        r1.addView(this.badge, r5);
        LinearLayout r6 = new LinearLayout(r13);
        r6.setOrientation(1);
        r6.setPadding(m407dp(12), m407dp(8), m407dp(12), m407dp(8));
        addView(r6, new LinearLayout.LayoutParams(-1, -1));
        this.title = label(r13, 15, AppTheme.color(r13, -1181697));
        this.title.setTypeface(null, 1);
        this.title.setMaxLines(2);
        this.title.setEllipsize(TextUtils.TruncateAt.END);
        this.title.setMinLines(2);
        r6.addView(this.title, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        this.detail = label(r13, 11, AppTheme.color(r13, -6244403));
        this.detail.setMaxLines(1);
        this.detail.setEllipsize(TextUtils.TruncateAt.END);
        r6.addView(this.detail);
        return;
    L9:
        r4 = PsExtractor.VIDEO_STREAM_MASK;
        goto L10
    L5:
        r2 = 325;
        goto L6
    }

    static StateListDrawable background(Context r7) {
        int r0 = Math.round(r7.getResources().getDisplayMetrics().density * 16.0f);
        StateListDrawable r1 = new StateListDrawable();
        r1.addState(new int[]{android.R.attr.state_activated}, shape(AppTheme.color(r7, -15186584), AppTheme.color(r7, -12331281), r0));
        r1.addState(new int[]{android.R.attr.state_pressed}, shape(AppTheme.color(r7, -15186584), AppTheme.color(r7, -12331281), r0));
        r1.addState(new int[]{android.R.attr.state_focused}, shape(AppTheme.color(r7, -15186584), AppTheme.color(r7, -12331281), r0));
        r1.addState(new int[]{android.R.attr.state_selected}, shape(AppTheme.color(r7, -15186584), AppTheme.color(r7, -12331281), r0));
        r1.addState(new int[0], shape(AppTheme.color(r7, -15652803), AppTheme.color(r7, -14400418), r0));
        return r1;
    }

    /* JADX INFO: renamed from: dp */
    private int m407dp(int r3) {
        return Math.round(r3 * getResources().getDisplayMetrics().density);
    }

    private TextView label(Context r3, int r4, int r5) {
        TextView r0 = new TextView(r3);
        r0.setTextSize(r4);
        r0.setTextColor(r5);
        return r0;
    }

    static StateListDrawable selectionOverlay(Context r9) {
        int r0 = Math.max(1, Math.round(r9.getResources().getDisplayMetrics().density));
        StateListDrawable r1 = new StateListDrawable();
        int[] r2 = {android.R.attr.state_activated, android.R.attr.state_pressed, android.R.attr.state_focused, android.R.attr.state_selected};
        int r4 = 0;
    L4:
        if (r4 >= 4) goto L10;
        int r5 = r2[r4];
        GradientDrawable r6 = new GradientDrawable();
        r6.setColor(0);
        r6.setCornerRadius(r0 * 16);
        int r7 = r0 * 3;
        if (AppTheme.cine(r9) == false) goto L8;
        int r8 = AppTheme.cineAccent(r9);
    L9:
        r6.setStroke(r7, r8);
        r1.addState(new int[]{r5}, new InsetDrawable(r6, r0));
        r4 = r4 + 1;
        goto L4
    L8:
        r8 = AppTheme.color(r9, -12331281);
        goto L9
    L10:
        r1.addState(new int[0], new ColorDrawable(0));
        return r1;
    }

    static GradientDrawable shape(int r2, int r3, int r4) {
        GradientDrawable r0 = new GradientDrawable();
        r0.setColor(r2);
        r0.setCornerRadius(r4);
        r0.setStroke(2, r3);
        return r0;
    }

    void bind(String r4, String r5, String r6) {
        this.progress.setVisibility(8);
        this.image.clearColorFilter();
        this.title.setText(r4);
        this.detail.setText(r5);
        this.badge.setText(r6);
        StringBuilder r0 = new StringBuilder().append(r4);
        if (r5.isEmpty() == false) goto L5;
        String r1 = "";
    L6:
        setContentDescription(r0.append(r1).toString());
        return;
    L5:
        r1 = " · " + r5;
        goto L6
    }

    void channelLayout(boolean r5) {
        ViewGroup.LayoutParams r0 = getLayoutParams();
        if (r5 == false) goto L6;
        int r1 = 150;
    L9:
        r0.height = m407dp(r1);
        setLayoutParams(r0);
        ViewGroup.LayoutParams r2 = getChildAt(0).getLayoutParams();
        if (r5 == false) goto L13;
        int r3 = 90;
    L16:
        r2.height = m407dp(r3);
        getChildAt(0).setLayoutParams(r2);
        TextView r4 = this.title;
        if (r5 == false) goto L19;
        float r6 = 13.0f;
    L20:
        r4.setTextSize(r6);
        return;
    L19:
        r6 = 15.0f;
        goto L20
    L13:
        if (AppTheme.cineStage(getContext()) == false) goto L15;
        r3 = 190;
        goto L16
    L15:
        r3 = PsExtractor.VIDEO_STREAM_MASK;
        goto L16
    L6:
        if (AppTheme.cineStage(getContext()) == false) goto L8;
        r1 = 262;
        goto L9
    L8:
        r1 = 325;
        goto L9
    }

    void continueArtwork() {
        this.image.setTag(null);
        this.image.setPadding(m407dp(36), m407dp(36), m407dp(36), m407dp(36));
        this.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.image.setImageResource(C1028R.drawable.continue_art);
        if (AppTheme.selected(getContext()) != 2) goto L5;
        ColorMatrix r0 = new ColorMatrix();
        r0.setSaturation(0.0f);
        this.image.setColorFilter(new ColorMatrixColorFilter(r0));
        return;
    L5:
        this.image.setColorFilter(AppTheme.artworkFilter(getContext()));
    }
}
