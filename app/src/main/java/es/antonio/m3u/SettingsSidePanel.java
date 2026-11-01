package es.antonio.m3u;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/* JADX INFO: loaded from: classes3.dex */
final class SettingsSidePanel extends FrameLayout {
    private final LinearLayout actions;
    private final Button menu;
    private final Activity owner;
    private ChdtvScreen screen;
    private final View shade;
    private final ScrollView side;
    private final boolean wide;

    SettingsSidePanel(Activity r3, LinearLayout r4, ScrollView r5, Button r6) {
        super(r3);
        this.screen = (ChdtvScreen) r3;
        this.owner = r3;
        this.side = null;
        this.actions = null;
        this.shade = null;
        this.menu = null;
        this.wide = false;
        if (r5.getParent() == null) goto L6;
        ((ViewGroup) r5.getParent()).removeView(r5);
    L6:
        if (r6.getParent() == null) goto L8;
        ((ViewGroup) r6.getParent()).removeView(r6);
    L8:
        addView(r5, new FrameLayout.LayoutParams(-1, -1));
    }

    /* JADX INFO: renamed from: dp */
    private int m408dp(int r3) {
        return Math.round(r3 * getResources().getDisplayMetrics().density);
    }

    private static GradientDrawable shape(float r3, int r4, int r5) {
        GradientDrawable r0 = new GradientDrawable();
        r0.setColor(r4);
        r0.setCornerRadius(12.0f * r3);
        r0.setStroke(Math.max(1, Math.round(r3)), r5);
        return r0;
    }

    static void style(Button r13) {
        r13.setFocusable(true);
        r13.setFocusableInTouchMode(false);
        float r2 = r13.getResources().getDisplayMetrics().density;
        r13.setAllCaps(false);
        r13.setTextSize(14.0f);
        if (AppTheme.selected(r13.getContext()) != 2) goto L5;
    L10:
        boolean r4 = true;
    L11:
        r13.setBackgroundTintList(null);
        r13.setTextColor(new ColorStateList(new int[][]{new int[]{android.R.attr.state_focused}, new int[]{android.R.attr.state_pressed}, new int[]{-16842910}, new int[]{android.R.attr.state_activated}, new int[0]}, new int[]{-657931, -657931, -6710887, -15724528, -657931}));
        r13.setMinimumWidth(0);
        r13.setMinWidth(0);
        r13.setMinimumHeight(Math.round(48.0f * r2));
        r13.setMaxLines(2);
        r13.setPadding(Math.round(r2 * 14.0f), Math.round(r2 * 8.0f), Math.round(14.0f * r2), Math.round(8.0f * r2));
        StateListDrawable r3 = new StateListDrawable();
        int[] r5 = {android.R.attr.state_focused};
        int r6 = -13421773;
        if (r4 == false) goto L14;
        int r8 = -13421773;
    L15:
        r3.addState(r5, shape(r2, r8, AppTheme.color(r13.getContext(), -12986907)));
        int[] r7 = {android.R.attr.state_pressed};
        if (r4 == true) goto L19;
        r6 = AppTheme.color(r13.getContext(), -15579298);
    L19:
        r3.addState(r7, shape(r2, r6, AppTheme.color(r13.getContext(), -12986907)));
        int[] r0 = {android.R.attr.state_activated};
        if (r4 == false) goto L22;
        int r9 = -1644826;
    L23:
        r3.addState(r0, shape(r2, r9, AppTheme.color(r13.getContext(), -16721939)));
        int[] r1 = new int[0];
        if (r4 == false) goto L26;
        int r10 = -15132391;
    L27:
        r3.addState(r1, shape(r2, r10, AppTheme.color(r13.getContext(), -14400679)));
        r13.setBackground(r3);
        return;
    L26:
        r10 = AppTheme.color(r13.getContext(), -16047048);
        goto L27
    L22:
        r9 = AppTheme.color(r13.getContext(), -16728876);
        goto L23
    L14:
        r8 = AppTheme.color(r13.getContext(), -15579298);
        goto L15
    L5:
        if (AppTheme.cine(r13.getContext()) == true) goto L7;
    L9:
        r4 = false;
        goto L11
    L7:
        if (AppTheme.cinePalette(r13.getContext()) != 2) goto L9;
        goto L9
    }

    void addAction(Button r2) {
        this.screen.addCategoryAction(r2);
    }

    boolean closeIfOpen() {
        return this.screen.closeNavigation();
    }

    void hide() {
        this.screen.closeNavigation();
    }

    boolean isOpen() {
        return this.screen.navigationOpen();
    }

    void show() {
        this.screen.showNavigation();
    }
}
