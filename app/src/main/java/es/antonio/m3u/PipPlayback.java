package es.antonio.m3u;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Rational;

/* JADX INFO: loaded from: classes3.dex */
final class PipPlayback {
    static final String PREF = "playback-pip";

    private PipPlayback() {
    }

    static Rational aspect(int r6, int r7, float r8) {
        if (r6 <= 0) goto L7;
        if (r7 <= 0) goto L7;
        if (r8 <= 0.0f) goto L7;
        double r0 = (((double) r6) * ((double) r8)) / ((double) r7);
    L9:
        if (Double.isNaN(r0) == false) goto L11;
    L12:
        r0 = 1.7777777777777777d;
    L14:
        return new Rational((int) Math.round(10000.0d * Math.max(0.41841004184100417d, Math.min(2.39d, r0))), 10000);
    L11:
        if (Double.isInfinite(r0) == false) goto L14;
    L7:
        r0 = 1.7777777777777777d;
        goto L9
    }

    static boolean enabled(Context r4) {
        if (supported(r4) == true) goto L5;
        return false;
    L5:
        if (r4.getSharedPreferences(PREF, 0).getBoolean("automatic", true) == true) goto L7;
        return false;
    L7:
        return true;
    }

    static /* synthetic */ void lambda$settings$0(Activity r3, DialogInterface r4, int r5) {
        boolean r1 = false;
        SharedPreferences.Editor r0 = r3.getSharedPreferences(PREF, 0).edit();
        if (r5 != 0) goto L5;
        r1 = true;
    L5:
        r0.putBoolean("automatic", r1).apply();
        if ((r3 instanceof PlayerActivity) == false) goto L8;
        ((PlayerActivity) r3).updateFloatingParams();
    L8:
        r4.dismiss();
    }

    static void settings(final Activity r5) {
        if (supported(r5) == true) goto L6;
        new AlertDialog.Builder(r5).setTitle("Ventana flotante").setMessage("Este dispositivo no ofrece ventana flotante de vídeo.").setPositiveButton("Aceptar", null).show();
        return;
    L6:
        new AlertDialog.Builder(r5).setTitle("Ventana flotante al salir").setSingleChoiceItems(new String[]{"Activada · continúa al pulsar Inicio", "Desactivada · reproducción sólo dentro de CHDTV"}, !enabled(r5) ? 1 : 0, new PipPlayback$$ExternalSyntheticLambda0(r5)).setNegativeButton("Cerrar", null).show();
    }

    static boolean supported(Context r4) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return false;
    L5:
        UiModeManager r0 = (UiModeManager) r4.getSystemService("uimode");
        if (r0 == null) goto L10;
        if (r0.getCurrentModeType() != 4) goto L10;
        return false;
    L10:
        if (r4.getPackageManager().hasSystemFeature("android.software.leanback") == false) goto L12;
        return false;
    L12:
        if (r4.getPackageManager().hasSystemFeature("android.software.picture_in_picture") == false) goto L18;
        return true;
    L18:
        return false;
    }
}
