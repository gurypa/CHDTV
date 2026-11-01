package es.antonio.m3u;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.media3.extractor.p001ts.TsExtractor;

/* JADX INFO: loaded from: classes3.dex */
final class ChdtvDialogs {
    ChdtvDialogs() {
    }

    static Button action(Activity r6, String r7, boolean r8) {
        Button r0 = new Button(r6);
        r0.setText(r7);
        r0.setAllCaps(false);
        r0.setTextSize(15.0f);
        if (r8 == false) goto L5;
        int r2 = -16178379;
    L6:
        r0.setTextColor(AppTheme.color(r6, r2));
        if (r8 == false) goto L9;
        int r3 = -11149841;
    L10:
        GradientDrawable r4 = shape(AppTheme.color(r6, r3), AppTheme.color(r6, -2140219923), m393dp(r6, 14));
        if (r8 == false) goto L13;
        int r5 = -7671558;
    L14:
        GradientDrawable r9 = shape(AppTheme.color(r6, r5), AppTheme.color(r6, -4130817), m393dp(r6, 14));
        StateListDrawable r10 = new StateListDrawable();
        r10.addState(new int[]{android.R.attr.state_focused}, r9);
        r10.addState(new int[]{android.R.attr.state_pressed}, r9);
        r10.addState(new int[0], r4);
        r0.setBackground(r10);
        return r0;
    L13:
        r5 = -14453349;
        goto L14
    L9:
        r3 = -2144316303;
        goto L10
    L5:
        r2 = -1575169;
        goto L6
    }

    /* JADX INFO: renamed from: dp */
    static int m393dp(Activity r2, int r3) {
        return Math.round(r3 * r2.getResources().getDisplayMetrics().density);
    }

    static /* synthetic */ void lambda$resume$0(Runnable r0, View r1) {
        r0.run();
    }

    static /* synthetic */ void lambda$resume$1(Runnable r0, View r1) {
        r0.run();
    }

    static /* synthetic */ void lambda$resume$2(AlertDialog r0, View r1) {
        r0.dismiss();
    }

    static AlertDialog resume(Activity r21, Entry r22, long r23, LogoLoader r25, final Runnable r26, final Runnable r27) {
        LinearLayout r2 = new LinearLayout(r21);
        r2.setOrientation(1);
        r2.setPadding(m393dp(r21, 18), m393dp(r21, 18), m393dp(r21, 18), m393dp(r21, 14));
        LinearLayout r5 = new LinearLayout(r21);
        r5.setGravity(16);
        ImageView r7 = new ImageView(r21);
        r7.setScaleType(ImageView.ScaleType.CENTER_CROP);
        r7.setBackground(shape(AppTheme.color(r21, -16049108), AppTheme.color(r21, 1079369199), m393dp(r21, 12)));
        r7.setClipToOutline(true);
        r5.addView(r7, new LinearLayout.LayoutParams(m393dp(r21, 92), m393dp(r21, TsExtractor.TS_STREAM_TYPE_DTS)));
        r25.show(r7, r22.logo);
        LinearLayout r8 = new LinearLayout(r21);
        r8.setOrientation(1);
        r8.setPadding(m393dp(r21, 18), 0, 0, 0);
        r5.addView(r8, new LinearLayout.LayoutParams(0, -2, 1.0f));
        r8.addView(text(r21, "SEGUIR VIENDO", 12, AppTheme.color(r21, -11149841)));
        TextView r4 = text(r21, r22.title, 21, AppTheme.color(r21, -1115905));
        r4.setTypeface(null, 1);
        int r10 = 3;
        r4.setMaxLines(3);
        r4.setPadding(0, m393dp(r21, 8), 0, m393dp(r21, 10));
        r8.addView(r4);
        r8.addView(text(r21, "Lo dejaste en " + WatchHistory.clock(r23), 15, AppTheme.color(r21, -5322788)));
        r2.addView(r5);
        Button r13 = action(r21, "▶  Reanudar", true);
        Button r14 = action(r21, "Empezar desde el principio", false);
        Button r15 = action(r21, "Volver", false);
        View[] r3 = {r13, r14, r15};
        int r6 = 0;
    L3:
        if (r6 >= r10) goto L5;
        View r11 = r3[r6];
        LinearLayout.LayoutParams r12 = new LinearLayout.LayoutParams(-1, m393dp(r21, 46));
        r12.setMargins(0, m393dp(r21, 9), 0, 0);
        r2.addView(r11, r12);
        r6 = r6 + 1;
        r10 = 3;
        goto L3
    L5:
        ScrollView r1 = new ScrollView(r21);
        r1.addView(r2);
        final AlertDialog r9 = new AlertDialog.Builder(r21).setView(r1).create();
        r13.setOnClickListener(new ChdtvDialogs$$ExternalSyntheticLambda0(r26));
        r14.setOnClickListener(new ChdtvDialogs$$ExternalSyntheticLambda1(r27));
        r15.setOnClickListener(new ChdtvDialogs$$ExternalSyntheticLambda2(r9));
        DialogHandoff.track(r21, r9);
        r9.show();
        Window r16 = r9.getWindow();
        int r17 = r21.getResources().getDisplayMetrics().widthPixels;
        int r18 = r21.getResources().getDisplayMetrics().heightPixels;
        r16.setGravity(21);
        WindowManager.LayoutParams r19 = r16.getAttributes();
        r19.x = m393dp(r21, 16);
        r16.setAttributes(r19);
        r16.setLayout(Math.min(m393dp(r21, 520), r17 - m393dp(r21, 32)), Math.min(m393dp(r21, 365), r18 - m393dp(r21, 48)));
        r13.requestFocus();
        return r9;
    }

    static GradientDrawable shape(int r2, int r3, int r4) {
        GradientDrawable r0 = new GradientDrawable();
        r0.setColor(r2);
        r0.setCornerRadius(r4);
        r0.setStroke(2, r3);
        return r0;
    }

    static TextView text(Activity r2, String r3, int r4, int r5) {
        TextView r0 = new TextView(r2);
        r0.setText(r3);
        r0.setTextSize(r4);
        r0.setTextColor(AppTheme.color(r2, r5));
        return r0;
    }
}
