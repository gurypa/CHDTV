package es.antonio.m3u;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.view.GravityCompat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes3.dex */
public class ChdtvScreen extends Activity {
    private int appliedTheme;
    private LinearLayout categoryActions;
    private LinearLayout chromeActions;
    private View chromeHeader;
    private ImageView chromeLogo;
    private View chromeRail;
    private TextView chromeTitle;
    private int focusHandoffKey;
    private Button menu;
    private LinearLayout navigation;
    private ScrollView navigationSurface;
    private boolean overlayNavigation;
    private String panelCategory;
    private View screenContent;
    private View shade;
    private final Set<View> styled;

    /* JADX INFO: renamed from: $r8$lambda$3EXFwt6CbBzMYzitUqMsoeep-eU, reason: not valid java name */
    public static /* synthetic */ void m541$r8$lambda$3EXFwt6CbBzMYzitUqMsoeepeU(ChdtvScreen r0) {
        r0.lambda$dispatchKeyEvent$1();
    }

    public static /* synthetic */ void $r8$lambda$3G4NyFUdJ9mfdNzKZEEpGjCHCsk(ChdtvScreen r0, View r1) {
        r0.lambda$setContentView$6(r1);
    }

    public static /* synthetic */ void $r8$lambda$5qWr2abqRAoTdKiAY4y0pv3l2AA(ChdtvScreen r0, String r1, View r2) {
        r0.lambda$setContentView$11(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$8oHrlwCqXVfse0ryks9d3LAuMbk(ChdtvScreen r0, ScrollView r1, View r2) {
        r0.lambda$setContentView$15(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$9EPkDlRJQiDCPj5wH7sxOEYw140(ChdtvScreen r0, String r1, View r2) {
        r0.lambda$setContentView$16(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$AqCjQJRIt56D-HlPTXC5u5xDaZ4, reason: not valid java name */
    public static /* synthetic */ void m542$r8$lambda$AqCjQJRIt56DHlPTXC5u5xDaZ4(ChdtvScreen r0) {
        r0.lambda$route$4();
    }

    public static /* synthetic */ void $r8$lambda$CI9agPFKZmHGL5FLh_zVkLTZ5Oc(ChdtvScreen r0, View r1) {
        r0.lambda$setContentView$18(r1);
    }

    public static /* synthetic */ void $r8$lambda$DN3fWUX5X0cYX1vfaRaqXBnS8gQ(ChdtvScreen r0, View r1) {
        r0.lambda$setContentView$9(r1);
    }

    public static /* synthetic */ WindowInsets $r8$lambda$IU1DiSW04_qJxvXDAxqfIPISU2w(ChdtvScreen r0, View r1, WindowInsets r2) {
        return r0.lambda$setContentView$5(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$ee2untJ9W-vsm0SuEOt4T-Ru1jg, reason: not valid java name */
    public static /* synthetic */ void m543$r8$lambda$ee2untJ9Wvsm0SuEOt4TRu1jg(ChdtvScreen r0, String r1, View r2) {
        r0.lambda$setContentView$7(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$fWgvTRasNk6XdrW-tBr8T7Ej4Nc, reason: not valid java name */
    public static /* synthetic */ void m544$r8$lambda$fWgvTRasNk6XdrWtBr8T7Ej4Nc(ChdtvScreen r0, LinearLayout r1, ScrollView r2, View r3, int r4, int r5, int r6, int r7, int r8, int r9, int r10, int r11) {
        r0.lambda$setContentView$17(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
    }

    public static /* synthetic */ void $r8$lambda$gSR2r17ZC1p95OnCSGnsWucah14(ChdtvScreen r0) {
        r0.lambda$showNavigationCategory$3();
    }

    public static /* synthetic */ void $r8$lambda$hXhRHYTxNQ8YpUtetr31C5umvAY(ChdtvScreen r0, View r1) {
        r0.lambda$setContentView$12(r1);
    }

    public static /* synthetic */ void $r8$lambda$mr6NO6S2JmgLyfmjjFZeV4sVAes(ChdtvScreen r0, View r1) {
        r0.lambda$setContentView$13(r1);
    }

    public static /* synthetic */ void $r8$lambda$nzNIjOKqVOb8x668P8ZUtC8pz6M(ChdtvScreen r0) {
        r0.lambda$showNavigation$0();
    }

    /* JADX INFO: renamed from: $r8$lambda$pyW-dINrOXGNqrk6qUrmcjpk_b8, reason: not valid java name */
    public static /* synthetic */ void m545$r8$lambda$pyWdINrOXGNqrk6qUrmcjpk_b8(ChdtvScreen r0, View r1) {
        r0.lambda$setContentView$14(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$q6-EeGS5zgCqt2ZrKj4Tw_VTulY, reason: not valid java name */
    public static /* synthetic */ void m546$r8$lambda$q6EeGS5zgCqt2ZrKj4Tw_VTulY(ChdtvScreen r0, String r1, View r2) {
        r0.lambda$showNavigationCategory$2(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$w1Bt4TgK6vycj6-cd0h7_MR-rQc, reason: not valid java name */
    public static /* synthetic */ void m547$r8$lambda$w1Bt4TgK6vycj6cd0h7_MRrQc(ChdtvScreen r0, Button r1, boolean r2, View r3, boolean r4) {
        r0.lambda$setContentView$10(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$zrzWVYiTfpiwMuIot1FBK7wry6Y(ChdtvScreen r0, View r1) {
        r0.lambda$setContentView$8(r1);
    }

    public ChdtvScreen() {
        this.appliedTheme = -1;
        this.focusHandoffKey = -1;
        this.panelCategory = "";
        this.styled = Collections.newSetFromMap(new WeakHashMap());
    }

    private GradientDrawable card(int r3, int r4) {
        GradientDrawable r0 = new GradientDrawable();
        r0.setColor(r3);
        r0.setCornerRadius(chromeDp(12));
        if (r4 == 0) goto L5;
        r0.setStroke(chromeDp(1), r4);
    L5:
        return r0;
    }

    private AbsListView catalogIn(View r5) {
        if (r5 != null) goto L5;
    L24:
        return null;
    L5:
        if (r5.getVisibility() != 0) goto L24;
        if (r5.isEnabled() == false) goto L24;
        if ((r5 instanceof AbsListView) == false) goto L16;
        if (((AbsListView) r5).getCount() <= 0) goto L16;
        return (AbsListView) r5;
    L16:
        if ((r5 instanceof ViewGroup) == false) goto L24;
        ViewGroup r1 = (ViewGroup) r5;
        int r2 = 0;
    L19:
        if (r2 >= r1.getChildCount()) goto L24;
        AbsListView r3 = catalogIn(r1.getChildAt(r2));
        if (r3 != null) goto L22;
        r2 = r2 + 1;
        goto L19
    L22:
        return r3;
    }

    private boolean focusCatalog() {
        AbsListView r0 = catalogIn(this.screenContent);
        if (r0 != null) goto L5;
        return false;
    L5:
        if (r0.requestFocus() == true) goto L8;
        return false;
    L8:
        if (r0.getSelectedItemPosition() >= 0) goto L14;
        r0.setSelection(r0.getFirstVisiblePosition());
        return true;
    L14:
        return true;
    }

    private static boolean isWithin(View r3, View r4) {
        View r0 = r3;
    L3:
        if (r0 == null) goto L12;
        if (r0 == r4) goto L5;
        Object r1 = r0.getParent();
        if ((r1 instanceof View) == false) goto L10;
        View r2 = (View) r1;
    L11:
        r0 = r2;
        goto L3
    L10:
        r2 = null;
        goto L11
    L5:
        return true;
    L12:
        return false;
    }

    private /* synthetic */ void lambda$dispatchKeyEvent$1() {
        PanelFocus.move(this.navigation, null, 1);
    }

    private /* synthetic */ void lambda$route$4() {
        recreate();
    }

    private /* synthetic */ void lambda$setContentView$10(Button r4, boolean r5, View r6, boolean r7) {
        if (r7 == true) goto L7;
        if (r5 == true) goto L7;
        int r1 = AppTheme.color(this, -4798502);
    L8:
        r4.setTextColor(r1);
        if (r7 == true) goto L13;
        if (r5 == true) goto L13;
        int r2 = AppTheme.color(this, 0);
    L14:
        if (r7 == true) goto L18;
        if (r5 == true) goto L18;
        int r0 = AppTheme.color(this, 0);
    L19:
        r4.setBackground(card(r2, r0));
        return;
    L18:
        r0 = AppTheme.color(this, -16721939);
    L13:
        r2 = AppTheme.color(this, -16237487);
    L7:
        r1 = AppTheme.color(this, -16721939);
        goto L8
    }

    private /* synthetic */ void lambda$setContentView$11(String r1, View r2) {
        showNavigationCategory(r1);
    }

    private /* synthetic */ void lambda$setContentView$12(View r1) {
        closeNavigation();
    }

    private /* synthetic */ void lambda$setContentView$13(View r2) {
        if (navigationOpen() == false) goto L5;
        closeNavigation();
        return;
    L5:
        showNavigation();
    }

    private /* synthetic */ void lambda$setContentView$14(View r1) {
        backNavigation();
    }

    private /* synthetic */ void lambda$setContentView$15(ScrollView r2, View r3) {
        closeNavigation();
        r2.setVisibility(8);
    }

    private /* synthetic */ void lambda$setContentView$16(String r4, View r5) {
        if (r4.equals("Token Movistar") == false) goto L9;
        if ((this instanceof CdnTokenActivity) == true) goto L16;
        Intent r0 = new Intent(this, CdnTokenActivity.class);
    L7:
        Intent r1 = r0.putExtra("navigationOpen", navigationOpen());
    L14:
        startActivity(r1);
        return;
    L16:
        return;
    L9:
        if (r4.equals("Token DAZN") == true) goto L11;
        r1 = new Intent(this, MainActivity.class).putExtra("chromeRoute", "Reproducción").putExtra("chromeTool", r4);
        goto L14
    L11:
        if ((this instanceof DaznTokenActivity) == true) goto L17;
        r0 = new Intent(this, DaznTokenActivity.class);
        goto L7
    }

    private /* synthetic */ void lambda$setContentView$17(LinearLayout r12, ScrollView r13, View r14, int r15, int r16, int r17, int r18, int r19, int r20, int r21, int r22) {
        int r0 = r17 - r15;
        int r4 = 40;
        int r2 = Math.min(chromeDp(164), Math.max(chromeDp(40), (r0 - chromeDp(164)) - this.chromeActions.getMeasuredWidth()));
        ViewGroup.LayoutParams r3 = this.chromeLogo.getLayoutParams();
        if (r3.width == r2) goto L6;
        r3.width = r2;
        this.chromeLogo.setLayoutParams(r3);
    L6:
        if (getResources().getConfiguration().screenHeightDp >= 480) goto L8;
        boolean r5 = true;
    L9:
        int r8 = 56;
        if (AppTheme.cine(this) == false) goto L13;
        if (r5 == false) goto L16;
        r8 = 48;
    L16:
        int r6 = chromeDp(r8);
        ViewGroup.LayoutParams r9 = r12.getLayoutParams();
        if (r9.height == r6) goto L20;
        r9.height = r6;
        r12.setLayoutParams(r9);
    L20:
        if (AppTheme.cine(this) == false) goto L23;
        if (r5 == false) goto L26;
        r4 = 32;
    L26:
        int r7 = chromeDp(r4);
        if (r3.height == r7) goto L29;
        r3.height = r7;
        this.chromeLogo.setLayoutParams(r3);
    L29:
        int r10 = Math.min(chromeDp(270), (int) (r0 * 0.85f));
        ViewGroup.LayoutParams r11 = r13.getLayoutParams();
        if (r11.width == r10) goto L33;
        r11.width = r10;
        r13.setLayoutParams(r11);
        return;
    L33:
        return;
    L23:
        if (r5 == false) goto L25;
        r4 = 48;
        goto L26
    L25:
        r4 = 72;
        goto L26
    L13:
        if (r5 == true) goto L16;
        r8 = 80;
        goto L16
    L8:
        r5 = false;
        goto L9
    }

    private /* synthetic */ void lambda$setContentView$18(View r1) {
        styleTree(r1);
    }

    private /* synthetic */ WindowInsets lambda$setContentView$5(View r6, WindowInsets r7) {
        r6.setPadding(chromeDp(16) + r7.getSystemWindowInsetLeft(), chromeDp(8) + r7.getSystemWindowInsetTop(), chromeDp(16) + r7.getSystemWindowInsetRight(), chromeDp(8) + r7.getSystemWindowInsetBottom());
        return r7.consumeSystemWindowInsets();
    }

    private /* synthetic */ void lambda$setContentView$6(View r2) {
        if (navigationOpen() == false) goto L5;
        backNavigation();
        return;
    L5:
        onBackPressed();
    }

    private /* synthetic */ void lambda$setContentView$7(String r4, View r5) {
        if (r4.equals("Buscar") == false) goto L8;
        if (this.chromeActions.getChildCount() <= 2) goto L29;
        this.chromeActions.getChildAt(2).performClick();
        return;
    L29:
        return;
    L8:
        if (r4.equals("Ajustes") == false) goto L11;
        showNavigation();
        return;
    L11:
        if (r4.equals("Plex") == false) goto L15;
        Intent r0 = new Intent(this, PlexActivity.class);
    L13:
        startActivity(r0);
        return;
    L15:
        if (r4.equals("EPG") == false) goto L18;
        r0 = new Intent(this, EpgActivity.class);
        goto L13
    L18:
        if (r4.equals("Inicio") == false) goto L21;
        route("Inicio");
        return;
    L21:
        if (r4.equals("TV") == false) goto L23;
        String r1 = "Canales";
    L25:
        if ((this instanceof MainActivity) == false) goto L27;
        ((MainActivity) this).openCineSection(r1);
        return;
    L27:
        startActivity(new Intent(this, MainActivity.class).putExtra("cineSection", r1));
        return;
    L23:
        r1 = r4;
        goto L25
    }

    private /* synthetic */ void lambda$setContentView$8(View r1) {
        backNavigation();
    }

    private /* synthetic */ void lambda$setContentView$9(View r2) {
        closeNavigation();
        route("Inicio");
    }

    private /* synthetic */ void lambda$showNavigation$0() {
        PanelFocus.move(this.navigation, null, 1);
    }

    private /* synthetic */ void lambda$showNavigationCategory$2(String r1, View r2) {
        openPanelTool(r1);
    }

    private /* synthetic */ void lambda$showNavigationCategory$3() {
        if (this.categoryActions == null) goto L11;
        int r0 = 0;
    L6:
        if (r0 >= this.categoryActions.getChildCount()) goto L15;
        if ((this.categoryActions.getChildAt(r0) instanceof Button) == true) goto L9;
        r0 = r0 + 1;
        goto L6
    L9:
        this.categoryActions.getChildAt(r0).requestFocus();
        return;
    L15:
        return;
    }

    private void styleTree(View r7) {
        if (this.styled.add(r7) == false) goto L43;
        Drawable r0 = r7.getBackground();
        if ((r0 instanceof ColorDrawable) == false) goto L8;
        r7.setBackgroundColor(AppTheme.color(this, ((ColorDrawable) r0).getColor()));
    L22:
        if ((r7 instanceof Button) == false) goto L33;
        Button r1 = (Button) r7;
        SettingsSidePanel.style(r1);
        String r2 = r1.getText().toString();
        if (r2.equals("‹") == false) goto L26;
    L31:
        r1.setVisibility(8);
        goto L43
    L26:
        if (r2.equals("Volver") == true) goto L31;
        if (r2.equals("‹ Volver") == true) goto L31;
        if (r2.equals("‹ Volver a DAZN") == false) goto L43;
    L33:
        if ((r7 instanceof EditText) == false) goto L36;
        EditText r3 = (EditText) r7;
        r3.setTextColor(AppTheme.color(this, -1182209));
        r3.setHintTextColor(AppTheme.color(this, -7428161));
        r3.setBackground(card(AppTheme.color(this, -16047048), AppTheme.color(this, -14400679)));
        goto L43
    L36:
        if ((r7 instanceof TextView) == false) goto L43;
        TextView r4 = (TextView) r7;
        r4.setTextColor(AppTheme.color(this, r4.getCurrentTextColor()));
        if ("keepColor".equals(r4.getTag()) == true) goto L43;
        if ((r4.getTextSize() / getResources().getDisplayMetrics().scaledDensity) < 20.0f) goto L43;
        r4.setTextColor(AppTheme.color(this, -16721939));
        r4.setTypeface(null, 1);
        goto L43
    L8:
        if ((r0 instanceof GradientDrawable) == false) goto L22;
        if (Build.VERSION.SDK_INT < 24) goto L22;
        GradientDrawable r5 = (GradientDrawable) r0;
        int[] r6 = r5.getColors();
        if (r6 == null) goto L19;
        int[] r8 = (int[]) r6.clone();
        int r9 = 0;
    L15:
        if (r9 >= r8.length) goto L17;
        r8[r9] = AppTheme.color(this, r8[r9]);
        r9 = r9 + 1;
        goto L15
    L17:
        r5.setColors(r8);
        goto L22
    L19:
        if (r5.getColor() == null) goto L22;
        r5.setColor(AppTheme.color(this, r5.getColor().getDefaultColor()));
    L43:
        if ((r7 instanceof ViewGroup) == false) goto L48;
        ViewGroup r10 = (ViewGroup) r7;
        int r11 = 0;
    L46:
        if (r11 >= r10.getChildCount()) goto L51;
        styleTree(r10.getChildAt(r11));
        r11 = r11 + 1;
        goto L46
    L51:
        return;
    }

    protected void addCategoryAction(Button r7) {
        if (this.categoryActions != null) goto L6;
        return;
    L6:
        if (this.categoryActions.getChildCount() != 0) goto L26;
        if (this.panelCategory.isEmpty() == true) goto L10;
    L13:
        int r0 = 0;
    L15:
        if (r0 >= this.navigation.getChildCount()) goto L20;
        if ((this.navigation.getChildAt(r0) instanceof Button) == false) goto L19;
        this.navigation.getChildAt(r0).setVisibility(8);
    L19:
        r0 = r0 + 1;
        goto L15
    L20:
        TextView r1 = new TextView(this);
        if (this.panelCategory.isEmpty() == false) goto L23;
        String r2 = chromeSelection();
    L24:
        r1.setText(r2);
        r1.setTextColor(AppTheme.color(this, -16721939));
        r1.setTextSize(19.0f);
        r1.setPadding(chromeDp(12), chromeDp(12), 0, chromeDp(8));
        this.categoryActions.addView(r1);
        goto L26
    L23:
        r2 = this.panelCategory;
        goto L24
    L10:
        if (chromeSelection().equals("General") == true) goto L26;
        if (chromeSelection().equals("Inicio") == false) goto L13;
    L26:
        if (r7.getParent() == null) goto L28;
        ((ViewGroup) r7.getParent()).removeView(r7);
    L28:
        SettingsSidePanel.style(r7);
        r7.setGravity(8388627);
        LinearLayout.LayoutParams r3 = new LinearLayout.LayoutParams(-1, -2);
        r3.topMargin = chromeDp(8);
        this.categoryActions.addView(r7, r3);
    }

    protected void backNavigation() {
        if (this.categoryActions != null) goto L5;
    L9:
        boolean r0 = false;
    L10:
        boolean r2 = false;
        int r3 = 0;
    L12:
        if (this.navigation == null) goto L21;
        if (r3 >= this.navigation.getChildCount()) goto L21;
        if ((this.navigation.getChildAt(r3) instanceof Button) == false) goto L20;
        if (this.navigation.getChildAt(r3).getVisibility() != 8) goto L20;
        r2 = true;
    L20:
        r3 = r3 + 1;
    L21:
        if (r0 == false) goto L32;
        if (r2 == false) goto L32;
        int r4 = 0;
    L25:
        if (r4 >= this.navigation.getChildCount()) goto L30;
        if ((this.navigation.getChildAt(r4) instanceof Button) == false) goto L29;
        this.navigation.getChildAt(r4).setVisibility(0);
    L29:
        r4 = r4 + 1;
        goto L25
    L30:
        this.categoryActions.setVisibility(8);
        this.panelCategory = "";
        return;
    L32:
        if (navigationOpen() == false) goto L35;
        closeNavigation();
        return;
    L35:
        if ((this instanceof MainActivity) == false) goto L37;
        route("Inicio");
        return;
    L37:
        finish();
        return;
    L5:
        if (this.categoryActions.getVisibility() != 0) goto L9;
        if (this.categoryActions.getChildCount() <= 0) goto L9;
        r0 = true;
        goto L10
    }

    protected int chromeDp(int r3) {
        return Math.round(r3 * getResources().getDisplayMetrics().density);
    }

    protected String chromeSelection() {
        if ((this instanceof PlexActivity) == false) goto L7;
        return "Plex";
    L7:
        if ((this instanceof BackupActivity) == false) goto L11;
        return "Copia de seguridad";
    L11:
        if ((this instanceof TimedAccessActivity) == false) goto L15;
        return "Acceso temporal";
    L15:
        if ((this instanceof EpgActivity) == false) goto L19;
        return "Listas y EPG";
    L19:
        if ((this instanceof CdnTokenActivity) == false) goto L22;
        return "Reproducción";
    L22:
        if ((this instanceof DaznTokenActivity) == false) goto L25;
        return "Reproducción";
    L25:
        if ((this instanceof DaznLoginActivity) == false) goto L28;
        return "Token DAZN";
    L28:
        return "Inicio";
    }

    protected void clearCategoryActions() {
        if (this.categoryActions == null) goto L6;
        this.categoryActions.removeAllViews();
        this.categoryActions.setVisibility(0);
        return;
    }

    protected boolean closeNavigation() {
        if (this.navigation != null) goto L5;
        return false;
    L5:
        if (this.navigation.getVisibility() != 0) goto L10;
        this.navigation.setVisibility(8);
        this.navigationSurface.setVisibility(8);
        this.shade.setVisibility(8);
        this.menu.requestFocus();
        return true;
    L10:
        return false;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent r10) {
        int r1 = -1;
        if (r10.getAction() != 1) goto L9;
        if (r10.getKeyCode() != this.focusHandoffKey) goto L9;
        this.focusHandoffKey = -1;
        return true;
    L9:
        if (navigationOpen() == false) goto L41;
        int r0 = r10.getKeyCode();
        if (r0 == 19) goto L34;
        if (r0 == 20) goto L34;
        if (r0 == 21) goto L28;
        if (r0 == 4) goto L28;
        if (r0 != 22) goto L41;
        if (r10.getAction() == 0) goto L22;
    L26:
        return true;
    L22:
        if (r10.getRepeatCount() != 0) goto L26;
        View r2 = this.navigation.findFocus();
        if ((r2 instanceof Button) == false) goto L26;
        r2.performClick();
    L28:
        if (r10.getAction() != 1) goto L32;
        backNavigation();
        if (navigationOpen() == false) goto L32;
        this.navigation.post(new ChdtvScreen$$ExternalSyntheticLambda0(this));
    L32:
        return true;
    L34:
        if (r10.getAction() != 0) goto L40;
        LinearLayout r3 = this.navigation;
        View r4 = this.navigation.findFocus();
        if (r0 == 19) goto L39;
        r1 = 1;
    L39:
        PanelFocus.move(r3, r4, r1);
    L40:
        return true;
    L41:
        View r5 = getWindow().getDecorView().findFocus();
        int r7 = r10.getKeyCode();
        if (r5 != null) goto L44;
    L54:
        if (r5 != null) goto L56;
    L75:
        AbsListView r6 = catalogIn(this.screenContent);
        if ((r6 instanceof ResponsiveCatalogGrid) == false) goto L97;
        if (r5 == null) goto L97;
        if (isWithin(r5, r6) == false) goto L97;
        if (r10.getAction() != 0) goto L97;
        int r8 = r6.getSelectedItemPosition();
        int r9 = Math.max(1, ((GridView) r6).getNumColumns());
        if (r7 != 21) goto L91;
        if (r8 < 0) goto L91;
        if ((r8 % r9) != 0) goto L91;
        if (this.chromeRail == null) goto L91;
        PanelFocus.move(this.chromeRail, null, 1);
        this.focusHandoffKey = r7;
        return true;
    L91:
        if (r7 != 19) goto L97;
        if (r8 < 0) goto L97;
        if (r8 >= r9) goto L97;
        this.menu.requestFocus();
        this.focusHandoffKey = r7;
        return true;
    L97:
        return super.dispatchKeyEvent(r10);
    L56:
        if (this.chromeRail == null) goto L75;
        if (isWithin(r5, this.chromeRail) == false) goto L75;
        if (r7 == 22) goto L61;
    L66:
        if (r7 == 20) goto L69;
        if (r7 != 19) goto L75;
    L69:
        if (r10.getAction() != 0) goto L74;
        View r11 = this.chromeRail;
        if (r7 != 20) goto L73;
        r1 = 1;
    L73:
        PanelFocus.move(r11, r5, r1);
    L74:
        return true;
    L61:
        if (r10.getAction() != 0) goto L66;
        if (focusCatalog() == false) goto L66;
        this.focusHandoffKey = r7;
        return true;
    L44:
        if (this.chromeHeader == null) goto L54;
        if (isWithin(r5, this.chromeHeader) == false) goto L54;
        if (r7 != 20) goto L54;
        if (r10.getAction() != 0) goto L54;
        if (focusCatalog() == false) goto L54;
        this.focusHandoffKey = r7;
        return true;
    }

    protected boolean navigationOpen() {
        if (this.navigation != null) goto L5;
    L7:
        return false;
    L5:
        if (this.navigation.getVisibility() != 0) goto L7;
        return true;
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (closeNavigation() == true) goto L6;
        super.onBackPressed();
        return;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle r1) {
        AppTheme.apply(this);
        super.onCreate(r1);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        if (this.appliedTheme >= 0) goto L5;
        return;
    L5:
        if (this.appliedTheme == AppTheme.selected(this)) goto L9;
        recreate();
        return;
    }

    @Override // android.app.Activity
    protected void onStop() {
        DialogHandoff.close(this);
        super.onStop();
    }

    protected void openPanelTool(String r5) {
        closeNavigation();
        BlackBox.event("SETTINGS_ACTION", r5);
        if (r5.equals("Caja negra · diagnóstico") == false) goto L7;
        startActivity(new Intent(this, BlackBoxActivity.class));
        return;
    L7:
        if (r5.equals("Mis grabaciones") == false) goto L11;
        startActivity(new Intent(this, RecordingsActivity.class));
        return;
    L11:
        if (r5.equals("Ventana flotante al salir") == false) goto L15;
        PipPlayback.settings(this);
        return;
    L15:
        if (r5.equals("Buscar actualizaciones") == false) goto L19;
        AppUpdates.check(this, true);
        return;
    L19:
        if (r5.equals("Configurar actualizaciones") == false) goto L23;
        AppUpdates.settings(this);
        return;
    L23:
        if (r5.equals("Instalar descarga") == false) goto L27;
        AppUpdates.install(this);
        return;
    L27:
        if (r5.equals("Elegir tema") == false) goto L31;
        route("Temas");
        return;
    L31:
        if (r5.equals("Token Movistar") == true) goto L84;
        if (r5.equals("Token DAZN") == true) goto L84;
        if (r5.equals("Crear copia cifrada") == true) goto L79;
        if (r5.equals("Copia completa · APK + datos") == true) goto L79;
        if (r5.equals("Compartir copia completa") == true) goto L79;
        if (r5.equals("Restaurar copia") == true) goto L79;
        if (r5.equals("Ver copias guardadas") == true) goto L79;
        if (r5.equals("Configurar Dropbox") == true) goto L79;
        if (r5.equals("Subir copia completa a Dropbox") == true) goto L79;
        if (r5.equals("Generar copia y código con duración") == true) goto L74;
        if (r5.equals("Ver último código generado") == true) goto L74;
        if (r5.equals("Importar copia temporal con código") == true) goto L74;
        if (r5.endsWith("Plex") == false) goto L65;
        if ((this instanceof PlexActivity) == false) goto L62;
        ((PlexActivity) this).openPanelToolAction(r5);
        return;
    L62:
        startActivity(new Intent(this, PlexActivity.class).putExtra("toolsOnly", true).putExtra("panelTool", r5));
        return;
    L65:
        if ((this instanceof MainActivity) == false) goto L67;
        ((MainActivity) this).openSettingsTool(r5);
        return;
    L67:
        Intent r0 = new Intent(this, MainActivity.class);
        if (r5.contains("EPG") == false) goto L70;
        String r1 = "Listas y EPG";
    L71:
        startActivity(r0.putExtra("chromeRoute", r1).putExtra("chromeTool", r5));
        return;
    L70:
        r1 = "Reproducción";
    L74:
        if ((this instanceof TimedAccessActivity) == false) goto L76;
        ((TimedAccessActivity) this).openPanelToolAction(r5);
        return;
    L76:
        startActivity(new Intent(this, TimedAccessActivity.class).putExtra("panelTool", r5));
        return;
    L79:
        if ((this instanceof BackupActivity) == false) goto L81;
        ((BackupActivity) this).openPanelToolAction(r5);
        return;
    L81:
        startActivity(new Intent(this, BackupActivity.class).putExtra("dropbox", r5.contains("Dropbox")).putExtra("panelTool", r5));
        return;
    L84:
        if (r5.equals("Token Movistar") == false) goto L86;
        Class r2 = CdnTokenActivity.class;
    L87:
        startActivity(new Intent(this, r2).putExtra("navigationOpen", true));
        return;
    L86:
        r2 = DaznTokenActivity.class;
        goto L87
    }

    protected void route(String r4) {
        closeNavigation();
        BlackBox.event("ROUTE", r4);
        if (r4.equals("Temas") == false) goto L10;
        if ((this instanceof MainActivity) == false) goto L7;
        ((MainActivity) this).openChromeRoute("Temas");
        return;
    L7:
        AppTheme.chooser(this, new ChdtvScreen$$ExternalSyntheticLambda10(this));
        return;
    L10:
        if ((this instanceof MainActivity) == false) goto L14;
        ((MainActivity) this).openChromeRoute(r4);
        return;
    L14:
        if (r4.equals("Plex") == false) goto L18;
        startActivity(new Intent(this, PlexActivity.class).putExtra("toolsOnly", true));
        return;
    L18:
        if (r4.equals("Copia de seguridad") == false) goto L20;
    L28:
        Intent r0 = new Intent(this, BackupActivity.class);
        r0.putExtra("dropbox", r4.equals("Dropbox"));
        startActivity(r0);
        return;
    L20:
        if (r4.equals("Dropbox") == true) goto L28;
        if (r4.equals("Acceso temporal") == false) goto L26;
        startActivity(new Intent(this, TimedAccessActivity.class));
        return;
    L26:
        startActivity(new Intent(this, MainActivity.class).addFlags(603979776).putExtra("chromeRoute", r4));
        finish();
    }

    protected void setChromeActions(Button r9, Button r10, Button r11) {
        this.chromeActions.removeAllViews();
        int r2 = 0;
        Button[] r1 = {r9, r10, r11};
    L4:
        if (r2 >= 3) goto L16;
        Button r4 = r1[r2];
        if (r4 == null) goto L15;
        if (r4.getParent() == null) goto L10;
        ((ViewGroup) r4.getParent()).removeView(r4);
    L10:
        SettingsSidePanel.style(r4);
        LinearLayout r5 = this.chromeActions;
        if (r4 != r9) goto L13;
        int r7 = 80;
    L14:
        r5.addView(r4, new LinearLayout.LayoutParams(chromeDp(r7), chromeDp(48)));
        goto L15
    L13:
        r7 = 44;
    L15:
        r2 = r2 + 1;
        goto L4
    L16:
        r9.setText("‹ Atrás");
        r9.setTextSize(14.0f);
        r9.setContentDescription("Atrás");
        this.chromeLogo.getLayoutParams().width = chromeDp(Math.min(164, Math.max(48, ((int) (getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density)) - 340)));
        this.chromeLogo.requestLayout();
    }

    protected void setChromeTitle(String r2) {
        if (this.chromeTitle == null) goto L6;
        this.chromeTitle.setText(r2);
        this.chromeTitle.setContentDescription(r2);
        return;
    }

    @Override // android.app.Activity
    public void setContentView(final View r40) {
        this.screenContent = r40;
        this.chromeRail = null;
        this.appliedTheme = AppTheme.selected(this);
        if (r40.getParent() == null) goto L5;
        ((ViewGroup) r40.getParent()).removeView(r40);
    L5:
        LinearLayout r3 = new LinearLayout(this);
        r3.setOrientation(1);
        r3.setPadding(chromeDp(16), chromeDp(8), chromeDp(16), chromeDp(8));
        r3.setOnApplyWindowInsetsListener(new ChdtvScreen$$ExternalSyntheticLambda11(this));
        r3.setBackground(new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{AppTheme.color(this, -16510173), AppTheme.color(this, -16046532), AppTheme.color(this, -16510173)}));
        final LinearLayout r6 = new LinearLayout(this);
        this.chromeHeader = r6;
        r6.setGravity(16);
        this.menu = new Button(this);
        this.menu.setText("☰");
        this.menu.setContentDescription("Mostrar u ocultar navegación");
        SettingsSidePanel.style(this.menu);
        this.menu.setTextSize(24.0f);
        r6.addView(this.menu, new LinearLayout.LayoutParams(chromeDp(48), chromeDp(48)));
        ImageView r8 = new ImageView(this);
        this.chromeLogo = r8;
        ChdtvBrand.show(r8);
        r8.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (AppTheme.cine(this) == false) goto L8;
        String r10 = "CHDTV";
    L9:
        r8.setContentDescription(r10);
        int r11 = (int) (getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density);
        int r13 = chromeDp(Math.min(164, Math.max(80, r11 - 200)));
        if (r11 < 600) goto L12;
        int r16 = 92;
    L13:
        LinearLayout.LayoutParams r12 = new LinearLayout.LayoutParams(r13, chromeDp(r16));
        r12.leftMargin = chromeDp(16);
        r6.addView(r8, r12);
        this.chromeTitle = new TextView(this);
        this.chromeTitle.setTextColor(AppTheme.color(this, -1182209));
        TextView r14 = this.chromeTitle;
        if (r11 < 600) goto L16;
        float r15 = 22.0f;
    L17:
        r14.setTextSize(r15);
        this.chromeTitle.setTypeface(null, 1);
        this.chromeTitle.setGravity(17);
        this.chromeTitle.setMaxLines(2);
        this.chromeTitle.setEllipsize(TextUtils.TruncateAt.END);
        this.chromeTitle.setPadding(chromeDp(8), 0, chromeDp(8), 0);
        r6.addView(this.chromeTitle, new LinearLayout.LayoutParams(0, -2, 1.0f));
        if (chromeSelection().equals("Inicio") == false) goto L20;
        String r2 = "";
    L21:
        setChromeTitle(r2);
        Button r4 = new Button(this);
        SettingsSidePanel.style(r4);
        r4.setText("‹ Atrás");
        r4.setContentDescription("Atrás");
        r4.setOnClickListener(new ChdtvScreen$$ExternalSyntheticLambda16(this));
        this.chromeActions = new LinearLayout(this);
        this.chromeActions.setGravity(16);
        r6.addView(this.chromeActions);
        this.chromeActions.addView(r4, new LinearLayout.LayoutParams(chromeDp(88), chromeDp(48)));
        if (r11 < 600) goto L24;
        int r5 = 96;
    L25:
        r3.addView(r6, new LinearLayout.LayoutParams(-1, chromeDp(r5)));
        if (settingsChrome() == true) goto L28;
    L32:
        boolean r7 = false;
    L33:
        boolean r9 = false;
        if (0 == 0) goto L38;
        if (r7 == false) goto L38;
        boolean r17 = false;
    L39:
        this.overlayNavigation = r17;
        FrameLayout r18 = new FrameLayout(this);
        if (AppTheme.cineStage(this) == false) goto L46;
        LinearLayout r19 = new LinearLayout(this);
        r19.setOrientation(0);
        ScrollView r110 = new ScrollView(this);
        r110.setVerticalScrollBarEnabled(false);
        LinearLayout r20 = new LinearLayout(this);
        this.chromeRail = r20;
        r20.setOrientation(1);
        r20.setPadding(0, chromeDp(8), 0, chromeDp(8));
        r20.setBackground(card(AppTheme.cineSurface(this), AppTheme.color(this, -14400418)));
        r110.addView(r20);
        String[] r21 = {"Inicio", "Buscar", "TV", "EPG", "Películas", "Series", "Plex", "Ajustes"};
        String[] r22 = {"⌂", "⌕", "▣", "▤", "▦", "☷", "❯", "☰"};
        int r111 = 0;
    L42:
        String[] r24 = r22;
        if (r111 >= r21.length) goto L45;
        final String r23 = r21[r111];
        String[] r25 = r21;
        Button r26 = new Button(this);
        SettingsSidePanel.style(r26);
        boolean r27 = r9;
        r26.setText(r24[r111]);
        r26.setTextSize(21.0f);
        r26.setTextColor(AppTheme.cineAccent(this));
        r26.setContentDescription(r23);
        r26.setOnClickListener(new ChdtvScreen$$ExternalSyntheticLambda17(this, r23));
        LinearLayout.LayoutParams r28 = new LinearLayout.LayoutParams(chromeDp(48), chromeDp(48));
        r28.topMargin = chromeDp(4);
        r20.addView(r26, r28);
        r111 = r111 + 1;
        r22 = r24;
        r21 = r25;
        r9 = r27;
        r8 = r8;
        r11 = r11;
        goto L42
    L45:
        boolean r29 = r9;
        r19.addView(r110, new LinearLayout.LayoutParams(chromeDp(52), -1));
        r19.addView(r18, new LinearLayout.LayoutParams(0, -1, 1.0f));
        r3.addView(r19, new LinearLayout.LayoutParams(-1, 0, 1.0f));
    L47:
        this.navigation = new LinearLayout(this);
        this.navigation.setOrientation(1);
        this.navigation.setPadding(chromeDp(8), chromeDp(12), chromeDp(8), chromeDp(12));
        this.navigation.setBackground(card(AppTheme.color(this, -16113355), AppTheme.color(this, -14927022)));
        final ScrollView r30 = new ScrollView(this);
        r30.setFocusable(false);
        r30.setDescendantFocusability(262144);
        this.navigationSurface = r30;
        r30.addView(this.navigation);
        this.navigation.setVisibility(8);
        LinearLayout r31 = new LinearLayout(this);
        r31.setGravity(16);
        TextView r32 = new TextView(this);
        r32.setText("Ajustes");
        r32.setTextColor(AppTheme.color(this, -1182209));
        r32.setTypeface(null, 1);
        r32.setTextSize(20.0f);
        r31.addView(r32, new LinearLayout.LayoutParams(0, chromeDp(48), 1.0f));
        Button r33 = new Button(this);
        SettingsSidePanel.style(r33);
        r33.setText("‹");
        r33.setContentDescription("Volver");
        r33.setOnClickListener(new ChdtvScreen$$ExternalSyntheticLambda18(this));
        r31.addView(r33, new LinearLayout.LayoutParams(chromeDp(40), chromeDp(48)));
        this.navigation.addView(r31);
        LinearLayout r34 = new LinearLayout(this);
        Button r112 = new Button(this);
        SettingsSidePanel.style(r112);
        r112.setText("⌂   Inicio");
        r112.setContentDescription("Ir al inicio");
        r112.setGravity(8388627);
        r112.setOnClickListener(new ChdtvScreen$$ExternalSyntheticLambda1(this));
        r34.addView(r112, new LinearLayout.LayoutParams(-1, chromeDp(48)));
        LinearLayout.LayoutParams r113 = new LinearLayout.LayoutParams(-1, -2);
        r113.bottomMargin = chromeDp(10);
        this.navigation.addView(r34, r113);
        String[] r114 = {"Reproducción", "Listas y EPG", "Plex", "Copia de seguridad", "Acceso temporal", "Dropbox", "Temas", "Actualizaciones"};
        String[] r115 = {"▶", "☷", "❯", "☁", "◷", "◇", "◐", "↻"};
        int r35 = 0;
    L48:
        TextView r116 = r32;
        if (r35 >= r114.length) goto L62;
        final String r36 = r114[r35];
        int r210 = r35;
        final Button r37 = new Button(this);
        SettingsSidePanel.style(r37);
        LinearLayout r211 = r34;
        Button r212 = r112;
        r37.setText(r115[r210] + "   " + r36);
        r37.setTextSize(14.0f);
        r37.setGravity(8388627);
        final boolean r38 = r36.equals(chromeSelection());
        if (r38 == false) goto L53;
        int r117 = AppTheme.color(this, -16721939);
    L54:
        r37.setTextColor(r117);
        if (r38 == false) goto L57;
        int r39 = AppTheme.color(this, -16237487);
    L58:
        if (r38 == false) goto L60;
        int r118 = AppTheme.color(this, -16721939);
    L61:
        r37.setBackground(card(r39, r118));
        r37.setOnFocusChangeListener(new ChdtvScreen$$ExternalSyntheticLambda2(this, r37, r38));
        r37.setOnClickListener(new ChdtvScreen$$ExternalSyntheticLambda3(this, r36));
        LinearLayout.LayoutParams r119 = new LinearLayout.LayoutParams(-1, chromeDp(48));
        r119.topMargin = chromeDp(3);
        this.navigation.addView(r37, r119);
        r35 = r210 + 1;
        r32 = r116;
        r34 = r211;
        r112 = r212;
        r12 = r12;
        goto L48
    L60:
        r118 = AppTheme.color(this, 0);
        goto L61
    L57:
        r39 = AppTheme.color(this, 0);
        goto L58
    L53:
        r117 = AppTheme.color(this, -4798502);
        goto L54
    L62:
        this.categoryActions = new LinearLayout(this);
        this.categoryActions.setOrientation(1);
        this.navigation.addView(this.categoryActions);
        this.shade = new View(this);
        this.shade.setFocusable(false);
        this.shade.setFocusableInTouchMode(false);
        this.shade.setBackgroundColor(AppTheme.color(this, -1728053248));
        this.shade.setVisibility(8);
        this.shade.setOnClickListener(new ChdtvScreen$$ExternalSyntheticLambda4(this));
        if (r29 == false) goto L66;
        if (r7 == false) goto L66;
        LinearLayout r41 = new LinearLayout(this);
        r41.setOrientation(0);
        r41.addView(r30, new LinearLayout.LayoutParams(chromeDp(230), -1));
        r41.addView(r40, new LinearLayout.LayoutParams(0, -1, 1.0f));
        r18.addView(r41);
        this.navigation.setVisibility(0);
        int r42 = 8;
    L67:
        if (r7 != 0) goto L69;
        r30.setVisibility(r42);
    L69:
        this.menu.setText("☰");
        this.menu.setContentDescription("Mostrar u ocultar ajustes");
        this.menu.setOnClickListener(new ChdtvScreen$$ExternalSyntheticLambda5(this));
        r33.setOnClickListener(new ChdtvScreen$$ExternalSyntheticLambda6(this));
        this.shade.setOnClickListener(new ChdtvScreen$$ExternalSyntheticLambda12(this, r30));
        if ((this instanceof CdnTokenActivity) == false) goto L72;
    L73:
        String[] r43 = {"Configurar TMDB", "Control parental", "Cargar lista", "Gestionar listas", "Token Movistar", "Token DAZN", "Mis grabaciones"};
        int r44 = 0;
    L75:
        if (r44 >= 7) goto L78;
        final String r45 = r43[r44];
        Button r46 = new Button(this);
        r46.setText(r45);
        r46.setOnClickListener(new ChdtvScreen$$ExternalSyntheticLambda13(this, r45));
        addCategoryAction(r46);
        r44 = r44 + 1;
        goto L75
    L78:
        if ((this instanceof CdnTokenActivity) == false) goto L80;
        String r47 = "Token Movistar";
    L81:
        setChromeTitle(r47);
        if (getIntent().getBooleanExtra("navigationOpen", false) == false) goto L84;
        showNavigation();
    L84:
        r6.addOnLayoutChangeListener(new ChdtvScreen$$ExternalSyntheticLambda14(this, r6, r30));
        super.setContentView(r3);
        r3.getViewTreeObserver().addOnGlobalLayoutListener(new ChdtvScreen$$ExternalSyntheticLambda15(this, r40));
        closeNavigation();
        RecordingIndicator.attach(this);
        return;
    L80:
        r47 = "Token DAZN";
        goto L81
    L72:
        if ((this instanceof DaznTokenActivity) == false) goto L84;
    L66:
        r18.addView(r40, new FrameLayout.LayoutParams(-1, -1));
        r18.addView(this.shade, new FrameLayout.LayoutParams(-1, -1));
        r18.addView(r30, new FrameLayout.LayoutParams(Math.min(chromeDp(270), (int) (getResources().getDisplayMetrics().widthPixels * 0.85f)), -1, GravityCompat.START));
        r42 = 8;
        r30.setVisibility(8);
        goto L67
    L46:
        r29 = false;
        r3.addView(r18, new LinearLayout.LayoutParams(-1, 0, 1.0f));
    L38:
        r17 = true;
        goto L39
    L28:
        if ((this instanceof EpgActivity) == true) goto L32;
        if ((this instanceof DaznLoginActivity) == true) goto L32;
        r7 = true;
        goto L33
    L24:
        r5 = 80;
        goto L25
    L20:
        r2 = chromeSelection();
        goto L21
    L16:
        r15 = 14.0f;
        goto L17
    L12:
        r16 = 72;
        goto L13
    L8:
        r10 = "CHDTV · pitufo con cerveza";
        goto L9
    }

    protected boolean settingsChrome() {
        if ((this instanceof MainActivity) == true) goto L5;
    L8:
        return true;
    L5:
        if (((MainActivity) this).isSettingsPage() == true) goto L8;
        return false;
    }

    protected void showNavigation() {
        if (this.navigation == null) goto L9;
        this.navigation.setVisibility(0);
        this.navigationSurface.setVisibility(0);
        if (this.overlayNavigation == false) goto L7;
        this.shade.setVisibility(0);
    L7:
        this.navigation.post(new ChdtvScreen$$ExternalSyntheticLambda7(this));
        return;
    }

    protected void showNavigationCategory(String r11) {
        BlackBox.event("SETTINGS_CATEGORY", r11);
        this.panelCategory = r11;
        clearCategoryActions();
        if (r11.equals("Reproducción") == false) goto L6;
        String[] r1 = {"Configurar TMDB", "Control parental", "Cargar lista", "Gestionar listas", "Token Movistar", "Token DAZN", "Mis grabaciones", "Caja negra · diagnóstico"};
    L25:
        if (r11.equals("Reproducción") == true) goto L27;
    L29:
        int r0 = r1.length;
        int r2 = 0;
    L30:
        if (r2 >= r0) goto L32;
        final String r3 = r1[r2];
        Button r4 = new Button(this);
        r4.setText(r3);
        r4.setOnClickListener(new ChdtvScreen$$ExternalSyntheticLambda8(this, r3));
        addCategoryAction(r4);
        r2 = r2 + 1;
        goto L30
    L32:
        showNavigation();
        this.navigation.post(new ChdtvScreen$$ExternalSyntheticLambda9(this));
        return;
    L27:
        if (PipPlayback.supported(this) == false) goto L29;
        String[] r5 = (String[]) Arrays.copyOf(r1, r1.length + 1);
        r5[r1.length] = "Ventana flotante al salir";
        r1 = r5;
        goto L29
    L6:
        if (r11.equals("Listas y EPG") == false) goto L9;
        r1 = new String[]{"Cargar lista", "Gestionar listas", "Importar / configurar EPG", "Mis grabaciones", "Caja negra · diagnóstico"};
        goto L25
    L9:
        if (r11.equals("Plex") == false) goto L12;
        r1 = new String[]{"Conectar cuenta Plex", "Importar JSON Plex", "Servidores Plex"};
        goto L25
    L12:
        if (r11.equals("Copia de seguridad") == false) goto L15;
        r1 = new String[]{"Crear copia cifrada", "Copia completa · APK + datos", "Compartir copia completa", "Restaurar copia", "Ver copias guardadas"};
        goto L25
    L15:
        if (r11.equals("Acceso temporal") == false) goto L18;
        r1 = new String[]{"Generar copia y código con duración", "Ver último código generado", "Importar copia temporal con código"};
        goto L25
    L18:
        if (r11.equals("Dropbox") == false) goto L21;
        r1 = new String[]{"Configurar Dropbox", "Subir copia completa a Dropbox"};
        goto L25
    L21:
        if (r11.equals("Actualizaciones") == false) goto L23;
        r1 = new String[]{"Buscar actualizaciones", "Configurar actualizaciones", "Instalar descarga"};
        goto L25
    L23:
        r1 = new String[]{"Elegir tema"};
        goto L25
    }
}
