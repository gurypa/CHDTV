package es.antonio.m3u;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

/* JADX INFO: loaded from: classes3.dex */
final class AppTheme {
    static final String[] CINE_COLORS = null;
    static final String[] NAMES = null;

    static {
        NAMES = new String[]{"Azul", "Verde", "Negro", "Rojo", "Estándar", "Cine"};
        CINE_COLORS = new String[]{"Lima", "Azul", "Negro", "Verde", "Rojo", "Estándar"};
    }

    AppTheme() {
    }

    static void apply(Activity r7) {
        SharedPreferences r0 = r7.getSharedPreferences("appearance", 0);
        if (r0.getBoolean("cine-restored-v169", false) == true) goto L5;
        r0.edit().putInt("theme", 5).putBoolean("cine-restored-v169", true).apply();
    L5:
        int r1 = selected(r7);
        if (r1 == 5) goto L8;
        if (r1 != 1) goto L25;
        int r2 = C1028R.style.AppThemeGreen;
    L30:
        r7.setTheme(r2);
        return;
    L25:
        if (r1 != 2) goto L27;
        r2 = C1028R.style.AppThemeBlack;
        goto L30
    L27:
        if (r1 != 3) goto L29;
        r2 = C1028R.style.AppThemeRed;
        goto L30
    L29:
        r2 = C1028R.style.AppTheme;
        goto L30
    L8:
        if (cinePalette(r7) != 2) goto L11;
        r2 = C1028R.style.AppThemeCineBlack;
        goto L30
    L11:
        if (cinePalette(r7) != 3) goto L14;
        r2 = C1028R.style.AppThemeCineGreen;
        goto L30
    L14:
        if (cinePalette(r7) != 4) goto L17;
        r2 = C1028R.style.AppThemeCineRed;
        goto L30
    L17:
        if (cinePalette(r7) != 1) goto L19;
    L22:
        r2 = C1028R.style.AppThemeCineBlue;
        goto L30
    L19:
        if (cinePalette(r7) == 5) goto L22;
        r2 = C1028R.style.AppThemeCine;
        goto L30
    }

    static ColorFilter artworkFilter(Context r27) {
        int r0 = selected(r27);
        if (r0 == 0) goto L25;
        if (r0 == 4) goto L25;
        if (r0 == 5) goto L9;
    L13:
        ColorMatrix r4 = new ColorMatrix();
        if (r0 == 2) goto L21;
        if (r0 == 5) goto L17;
    L19:
        float[] r7 = new float[3];
        Color.colorToHSV(color(r27, -11477516), r7);
        float r10 = (r7[0] - 190.0f) * 0.017453292f;
        float r11 = (float) Math.cos(r10);
        float r12 = (float) Math.sin(r10);
        r4.set(new float[]{(0.213f + ((1.0f - 0.213f) * r11)) + ((-0.213f) * r12), (((-0.715f) * r11) + 0.715f) + ((-0.715f) * r12), (((-0.072f) * r11) + 0.072f) + ((1.0f - 0.072f) * r12), 0.0f, 0.0f, (((-0.213f) * r11) + 0.213f) + (0.143f * r12), (0.715f + ((1.0f - 0.715f) * r11)) + (0.14f * r12), (((-0.072f) * r11) + 0.072f) + ((-0.283f) * r12), 0.0f, 0.0f, (((-0.213f) * r11) + 0.213f) + ((-(1.0f - 0.213f)) * r12), (((-0.715f) * r11) + 0.715f) + (r12 * 0.715f), (0.072f + ((1.0f - 0.072f) * r11)) + (r12 * 0.072f), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
    L23:
        return new ColorMatrixColorFilter(r4);
    L17:
        if (cinePalette(r27) != 2) goto L19;
    L21:
        r4.setSaturation(0.12f);
        goto L23
    L9:
        if (cinePalette(r27) == 1) goto L27;
        if (cinePalette(r27) != 5) goto L13;
    L27:
        return null;
    L25:
        return null;
    }

    static void chooser(final Activity r4, final Runnable r5) {
        new AlertDialog.Builder(r4).setTitle("Temas").setSingleChoiceItems(NAMES, selected(r4), new AppTheme$$ExternalSyntheticLambda1(r4, r5)).setNegativeButton("Cancelar", null).show();
    }

    static boolean cine(Context r2) {
        if (selected(r2) != 5) goto L5;
        return true;
    L5:
        return false;
    }

    static int cineAccent(Context r2) {
        return new int[]{-5317847, -12986907, -1644826, -12982908, -36480, -12986907}[cinePalette(r2)];
    }

    static int cineBackground(Context r1) {
        return color(r1, -16182497);
    }

    static int cinePalette(Context r3) {
        int r0 = r3.getSharedPreferences("appearance", 0).getInt("cine-color", 0);
        if (r0 >= 0) goto L5;
        return 0;
    L5:
        if (r0 < CINE_COLORS.length) goto L7;
        return 0;
    L7:
        return r0;
    }

    static boolean cineStage(Context r3) {
        Configuration r0 = r3.getResources().getConfiguration();
        if (cine(r3) == true) goto L5;
    L9:
        return false;
    L5:
        if ((r0.uiMode & 15) != 4) goto L7;
    L8:
        return true;
    L7:
        if (r0.smallestScreenWidthDp < 600) goto L9;
        goto L8
    }

    static int cineSurface(Context r1) {
        return color(r1, -15652803);
    }

    static int color(Context r16, int r17) {
        int r1 = selected(r16);
        if (r1 != 0) goto L5;
    L59:
        return r17;
    L5:
        if (r1 == 4) goto L59;
        if (Color.alpha(r17) == 0) goto L59;
        float[] r4 = new float[3];
        Color.colorToHSV(r17, r4);
        float r7 = 0.12f;
        if (r4[1] < 0.12f) goto L59;
        if (r4[0] < 160.0f) goto L59;
        if (r4[0] > 260.0f) goto L59;
        float r9 = 146.0f;
        float r11 = 0.9f;
        if (r1 != 5) goto L48;
        int r15 = cinePalette(r16);
        if (r15 == 1) goto L47;
        if (r15 == 5) goto L47;
        if (r15 != 2) goto L28;
        r4[1] = 0.0f;
        if (r4[2] >= 0.55f) goto L26;
        r11 = r4[2] * 0.25f;
    L26:
        r4[2] = r11;
        return Color.HSVToColor(Color.alpha(r17), r4);
    L28:
        if (r15 == 3) goto L42;
        if (r15 == 4) goto L42;
        r4[0] = 78.0f;
        if (r4[2] >= 0.55f) goto L35;
        r7 = 0.16f;
    L38:
        r4[1] = r7;
        if (r4[2] >= 0.55f) goto L41;
        r11 = Math.max(0.035f, r4[2] * 0.45f);
    L41:
        r4[2] = r11;
    L58:
        return Color.HSVToColor(Color.alpha(r17), r4);
    L35:
        if (r4[1] < 0.4f) goto L38;
        r7 = 0.82f;
    L42:
        if (r15 == 3) goto L45;
        r9 = 0.0f;
    L45:
        r4[0] = r9;
        return Color.HSVToColor(Color.alpha(r17), r4);
    L47:
        return r17;
    L48:
        if (r1 != 2) goto L53;
        r4[1] = 0.0f;
        float r2 = r4[2];
        if (r4[2] >= 0.55f) goto L52;
        r11 = r4[2] * 0.25f;
    L52:
        r4[2] = Math.min(r2, r11);
        goto L58
    L53:
        if (r1 == 1) goto L56;
        r9 = 0.0f;
    L56:
        r4[0] = r9;
        goto L58
    }

    static /* synthetic */ void lambda$chooser$0(Activity r1, Runnable r2, DialogInterface r3, int r4) {
        saveCinePalette(r1, r4);
        save(r1, 5);
        r3.dismiss();
        r2.run();
    }

    static /* synthetic */ void lambda$chooser$1(final Activity r4, final Runnable r5, DialogInterface r6, int r7) {
        r6.dismiss();
        if (r7 != 5) goto L5;
        new AlertDialog.Builder(r4).setTitle("Cine · Color").setSingleChoiceItems(CINE_COLORS, cinePalette(r4), new AppTheme$$ExternalSyntheticLambda0(r4, r5)).setNegativeButton("Cancelar", null).show();
        return;
    L5:
        save(r4, r7);
        r5.run();
    }

    static void save(Context r2, int r3) {
        if (r3 < 0) goto L10;
        if (r3 >= NAMES.length) goto L10;
        r2.getSharedPreferences("appearance", 0).edit().putInt("theme", r3).apply();
        if ((r2 instanceof Activity) == false) goto L11;
        apply((Activity) r2);
        return;
    L11:
        return;
    L10:
        throw new IllegalArgumentException("Tema desconocido");
    }

    static void saveCinePalette(Context r2, int r3) {
        if (r3 < 0) goto L8;
        if (r3 >= CINE_COLORS.length) goto L8;
        r2.getSharedPreferences("appearance", 0).edit().putInt("cine-color", r3).apply();
        return;
    L8:
        throw new IllegalArgumentException("Color desconocido");
    }

    static int selected(Context r3) {
        int r0 = r3.getSharedPreferences("appearance", 0).getInt("theme", 4);
        if (r0 >= 0) goto L5;
        return 4;
    L5:
        if (r0 < NAMES.length) goto L7;
        return 4;
    L7:
        return r0;
    }
}
