package es.antonio.m3u;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.media3.extractor.p001ts.TsExtractor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class TmdbUi {
    private final Activity activity;
    private final Runnable changed;
    private final LogoLoader logos;
    private final TmdbService service;

    private interface Render {
        void accept(JSONObject r1);
    }

    public static /* synthetic */ void $r8$lambda$0A8oSLKQC06sE1wjZqUJwj2kjKE(TmdbUi r0, AlertDialog r1, Entry r2, TextView r3, Render r4, List r5, String r6) {
        r0.lambda$preview$7(r1, r2, r3, r4, r5, r6);
    }

    public static /* synthetic */ void $r8$lambda$1oYNh46DTUMNmYdTJBiXH8bms78(TmdbUi r0, Render r1, Entry r2, AlertDialog r3, JSONObject r4) {
        r0.lambda$preview$6(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$D8XsHIG92mqePbkw2jjyfRTQ1a4(TmdbUi r0, DialogInterface r1, int r2) {
        r0.lambda$find$8(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$L4XXRFvAKdEZeuNyUISY2-IAvZg, reason: not valid java name */
    public static /* synthetic */ void m809$r8$lambda$L4XXRFvAKdEZeuNyUISY2IAvZg(TmdbUi r0, AlertDialog r1, ImageView r2, Entry r3, TextView r4, TextView r5, JSONObject r6) {
        r0.lambda$preview$4(r1, r2, r3, r4, r5, r6);
    }

    public static /* synthetic */ void $r8$lambda$L8KwSQRNcOfHOlY6yIWf15gsRvU(TmdbUi r0, AlertDialog r1, Render r2, List r3, String r4) {
        r0.lambda$preview$5(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$NxrLGoDZ0jZtAfvLGY4k33ZWoz0(TmdbUi r0, Entry r1, JSONObject r2, DialogInterface r3, int r4) {
        r0.lambda$show$12(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$ROtWhdvSZ3u5uwkauaiCUSgUgdw(TmdbUi r0, DialogInterface r1, int r2) {
        r0.lambda$settings$1(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$X7mnIAgsO9tiaMJlmzus8Xy3DRo(TmdbUi r0, EditText r1, CheckBox r2, CheckBox r3, DialogInterface r4, int r5) {
        r0.lambda$settings$0(r1, r2, r3, r4, r5);
    }

    public static /* synthetic */ void $r8$lambda$dThToofThUzLWGjyBYg21fEMiiA(TmdbUi r0, Entry r1, DialogInterface r2, int r3) {
        r0.lambda$show$13(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$rhO5n5YQfIfn6MYSfN8ZeXpeqzk(TmdbUi r0, Entry r1, List r2, String r3) {
        r0.lambda$find$10(r1, r2, r3);
    }

    /* JADX INFO: renamed from: $r8$lambda$tm-7mekNshlccv7jxN_V0IiP73Y, reason: not valid java name */
    public static /* synthetic */ void m810$r8$lambda$tm7mekNshlccv7jxN_V0IiP73Y(TmdbUi r0, ImageView r1, Entry r2, TextView r3, TextView r4, JSONObject r5) {
        r0.lambda$show$11(r1, r2, r3, r4, r5);
    }

    /* JADX INFO: renamed from: $r8$lambda$xKyxzKU-5ucFH_dh5a8rvDefs5M, reason: not valid java name */
    public static /* synthetic */ void m811$r8$lambda$xKyxzKU5ucFH_dh5a8rvDefs5M(TmdbUi r0, AlertDialog r1, Render r2, List r3, String r4) {
        r0.lambda$show$14(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$zKIyID5ReIFWopuZuOtEychUx9E(TmdbUi r0, Entry r1, List r2, DialogInterface r3, int r4) {
        r0.lambda$find$9(r1, r2, r3, r4);
    }

    TmdbUi(Activity r1, TmdbService r2, LogoLoader r3, Runnable r4) {
        this.activity = r1;
        this.service = r2;
        this.logos = r3;
        this.changed = r4;
    }

    private boolean alive() {
        if (this.activity.isFinishing() == false) goto L5;
    L7:
        return false;
    L5:
        if (this.activity.isDestroyed() == true) goto L7;
        return true;
    }

    private void choose(Entry r2, JSONObject r3) {
        this.service.remember(r2, r3);
        this.changed.run();
        show(r2, r3);
    }

    private void credit(LinearLayout r5) {
        ImageView r0 = new ImageView(this.activity);
        r0.setImageResource(C1028R.drawable.tmdb_logo);
        r0.setScaleType(ImageView.ScaleType.FIT_CENTER);
        r5.addView(r0, new LinearLayout.LayoutParams(m411dp(90), m411dp(34)));
        TextView r1 = text("This product uses the TMDB API but is not endorsed or certified by TMDB.", 10);
        r1.setTextColor(AppTheme.color(this.activity, -7232068));
        r5.addView(r1);
    }

    /* JADX INFO: renamed from: dp */
    private int m411dp(int r3) {
        return Math.round(r3 * this.activity.getResources().getDisplayMetrics().density);
    }

    private void find(final Entry r4) {
        Toast.makeText(this.activity, "Buscando información en TMDB…", 0).show();
        this.service.find(r4, new TmdbUi$$ExternalSyntheticLambda5(this, r4));
    }

    private /* synthetic */ void lambda$find$10(final Entry r10, final List r11, String r12) {
        if (alive() == true) goto L6;
        return;
    L6:
        if (r12 == null) goto L10;
        new AlertDialog.Builder(this.activity).setTitle("TMDB").setMessage(r12).setPositiveButton("Configurar", new TmdbUi$$ExternalSyntheticLambda7(this)).setNegativeButton("Cerrar", null).show();
        return;
    L10:
        if (r11.isEmpty() == false) goto L14;
        new AlertDialog.Builder(this.activity).setTitle("TMDB").setMessage("No se encontró " + TmdbTitles.query(r10)).setPositiveButton("Cerrar", null).show();
        return;
    L14:
        if (r11.size() != 1) goto L17;
        choose(r10, (JSONObject) r11.get(0));
        return;
    L17:
        ArrayList r0 = new ArrayList();
        Iterator r3 = r11.iterator();
    L19:
        if (r3.hasNext() == false) goto L21;
        JSONObject r5 = (JSONObject) r3.next();
        r0.add(r5.optString("title", r5.optString("name")) + " · " + r5.optString("release_date", r5.optString("first_air_date")));
        goto L19
    L21:
        AlertDialog r1 = new AlertDialog.Builder(this.activity).setTitle("Elige la ficha correcta").setItems((CharSequence[]) r0.toArray(new String[0]), new TmdbUi$$ExternalSyntheticLambda8(this, r10, r11)).setNegativeButton("Cerrar", null).create();
        r1.show();
        style(r1);
    }

    private /* synthetic */ void lambda$find$8(DialogInterface r1, int r2) {
        settings();
    }

    private /* synthetic */ void lambda$find$9(Entry r2, List r3, DialogInterface r4, int r5) {
        choose(r2, (JSONObject) r3.get(r5));
    }

    static /* synthetic */ void lambda$preview$2(Runnable r0, DialogInterface r1, int r2) {
        r0.run();
    }

    static /* synthetic */ void lambda$preview$3(Runnable r0, DialogInterface r1, int r2) {
        r0.run();
    }

    private /* synthetic */ void lambda$preview$4(AlertDialog r18, ImageView r19, Entry r20, TextView r21, TextView r22, JSONObject r23) {
        if (alive() == false) goto L41;
        if (r18.isShowing() == false) goto L45;
        String r2 = TmdbService.poster(r23);
        if (r2.isEmpty() == true) goto L11;
        this.logos.show(r19, r2);
    L11:
        String r4 = r23.optString("title", r23.optString("name", TmdbTitles.query(r20)));
        String r8 = "";
        String r7 = r23.optString("release_date", r23.optString("first_air_date", ""));
        ArrayList r9 = new ArrayList();
        JSONArray r10 = r23.optJSONArray("genres");
        if (r10 == null) goto L20;
        int r11 = 0;
    L15:
        if (r11 >= r10.length()) goto L20;
        JSONObject r12 = r10.optJSONObject(r11);
        if (r12 == null) goto L19;
        r9.add(r12.optString("name"));
    L19:
        r11 = r11 + 1;
    L20:
        StringBuilder r6 = new StringBuilder().append(r4);
        if (r7.isEmpty() == false) goto L23;
        String r13 = "";
    L24:
        StringBuilder r14 = r6.append(r13);
        if (r23.optDouble("vote_average") <= 0.0d) goto L27;
        String r15 = "\n★ " + String.format(Locale.getDefault(), "%.1f", new Object[]{Double.valueOf(r23.optDouble("vote_average"))}) + "/10";
    L28:
        StringBuilder r16 = r14.append(r15);
        if (r9.isEmpty() == false) goto L31;
        String r17 = "";
    L32:
        StringBuilder r24 = r16.append(r17);
        if (r23.optInt("runtime") <= 0) goto L35;
        r8 = "\n" + r23.optInt("runtime") + " min";
    L35:
        String r25 = r24.append(r8).toString();
        if (r20.kind.equals("Series") == false) goto L38;
        r25 = r25 + "\n" + r20.title;
    L38:
        r21.setText(r25);
        r22.setText(TmdbService.synopsis(r23));
        return;
    L31:
        r17 = "\n" + TextUtils.join(" · ", r9);
        goto L32
    L27:
        r15 = "";
        goto L28
    L23:
        r13 = "\n" + r7;
        goto L24
    L45:
        return;
    }

    private /* synthetic */ void lambda$preview$5(AlertDialog r2, Render r3, List r4, String r5) {
        if (alive() == true) goto L5;
        return;
    L5:
        if (r2.isShowing() == true) goto L7;
        return;
    L7:
        if (r4.isEmpty() == true) goto L12;
        r3.accept((JSONObject) r4.get(0));
        this.changed.run();
        return;
    }

    private /* synthetic */ void lambda$preview$6(final Render r3, Entry r4, final AlertDialog r5, JSONObject r6) {
        r3.accept(r6);
        if (this.service.enabled() == true) goto L5;
        return;
    L5:
        if (r6.has("genres") == true) goto L9;
        this.service.details(r4, r6, new TmdbUi$$ExternalSyntheticLambda4(this, r5, r3));
        return;
    }

    private /* synthetic */ void lambda$preview$7(AlertDialog r3, Entry r4, TextView r5, Render r6, List r7, String r8) {
        if (alive() == true) goto L5;
        return;
    L5:
        if (r3.isShowing() == false) goto L17;
        JSONObject r0 = TmdbService.exact(r4, r7);
        if (r0 != null) goto L14;
        if (r8 == null) goto L11;
        String r1 = r8;
    L12:
        r5.setText(r1);
        return;
    L11:
        r1 = "Sin ficha inequívoca en TMDB. Puedes elegirla manteniendo pulsado el título y abriendo Información TMDB.";
        goto L12
    L14:
        this.service.remember(r4, r0);
        this.changed.run();
        r6.accept(r0);
        return;
    }

    private /* synthetic */ void lambda$settings$0(EditText r5, CheckBox r6, CheckBox r7, DialogInterface r8, int r9) {
        String r0 = r5.getText().toString().trim();
        if (r0.isEmpty() == false) goto L8;
        if (this.service.apiKey().isEmpty() == true) goto L8;
        r0 = this.service.apiKey();
    L8:
        if (r0.isEmpty() == false) goto L10;
    L13:
        this.service.settings.edit().putString("api-key", r0).putBoolean("enabled", r6.isChecked()).putBoolean("english-overview-fallback", r7.isChecked()).apply();
        this.service.clearCache();
        this.changed.run();
        return;
    L10:
        if (r0.matches("[a-fA-F0-9]{32}") == true) goto L13;
        Toast.makeText(this.activity, "La clave API debe tener 32 caracteres hexadecimales", 1).show();
    }

    private /* synthetic */ void lambda$settings$1(DialogInterface r2, int r3) {
        this.service.clearCache();
        this.changed.run();
    }

    private /* synthetic */ void lambda$show$11(ImageView r13, Entry r14, TextView r15, TextView r16, JSONObject r17) {
        this.logos.show(r13, TmdbService.poster(r17));
        String r1 = r17.optString("title", r17.optString("name", TmdbTitles.query(r14)));
        String r4 = "";
        String r3 = r17.optString("release_date", r17.optString("first_air_date", ""));
        JSONArray r5 = r17.optJSONArray("genres");
        ArrayList r6 = new ArrayList();
        if (r5 == null) goto L8;
        int r7 = 0;
    L6:
        if (r7 >= r5.length()) goto L8;
        r6.add(r5.optJSONObject(r7).optString("name"));
        r7 = r7 + 1;
    L8:
        StringBuilder r2 = new StringBuilder().append(r1).append("\n").append(r3).append("\n★ ").append(String.format(Locale.getDefault(), "%.1f", new Object[]{Double.valueOf(r17.optDouble("vote_average"))})).append("/10");
        if (r6.isEmpty() == false) goto L11;
        String r8 = "";
    L12:
        StringBuilder r9 = r2.append(r8);
        if (r17.optInt("runtime") <= 0) goto L15;
        r4 = "\n" + r17.optInt("runtime") + " min";
    L15:
        r15.setText(r9.append(r4).toString());
        r16.setText(TmdbService.synopsis(r17));
        return;
    L11:
        r8 = "\n" + TextUtils.join(" · ", r6);
        goto L12
    }

    private /* synthetic */ void lambda$show$12(Entry r6, JSONObject r7, DialogInterface r8, int r9) {
        this.activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.themoviedb.org/" + TmdbTitles.type(r6) + "/" + r7.optInt("id"))));     // Catch: ActivityNotFoundException -> L4
        return;
    }

    private /* synthetic */ void lambda$show$13(Entry r1, DialogInterface r2, int r3) {
        find(r1);
    }

    private /* synthetic */ void lambda$show$14(AlertDialog r2, Render r3, List r4, String r5) {
        if (alive() == true) goto L5;
        return;
    L5:
        if (r2.isShowing() == true) goto L7;
        return;
    L7:
        if (r4.isEmpty() == true) goto L12;
        r3.accept((JSONObject) r4.get(0));
        this.changed.run();
        return;
    }

    private void show(final Entry r18, final JSONObject r19) {
        LinearLayout r0 = new LinearLayout(this.activity);
        r0.setOrientation(1);
        r0.setPadding(m411dp(12), m411dp(8), m411dp(12), m411dp(8));
        LinearLayout r1 = new LinearLayout(this.activity);
        final ImageView r2 = new ImageView(this.activity);
        r2.setScaleType(ImageView.ScaleType.FIT_CENTER);
        r1.addView(r2, new LinearLayout.LayoutParams(m411dp(105), m411dp(156)));
        final TextView r4 = text("", 14);
        r1.addView(r4, new LinearLayout.LayoutParams(0, -2, 1.0f));
        r0.addView(r1);
        final TextView r5 = text("", 14);
        r0.addView(r5);
        credit(r0);
        ScrollView r3 = new ScrollView(this.activity);
        r3.addView(r0);
        final Render r6 = new TmdbUi$$ExternalSyntheticLambda14(this, r2, r18, r4, r5);
        r6.accept(r19);
        final AlertDialog r10 = new AlertDialog.Builder(this.activity).setTitle("Información · TMDB").setView(r3).setPositiveButton("Ver ficha web", new TmdbUi$$ExternalSyntheticLambda1(this, r18, r19)).setNeutralButton("Cambiar ficha", new TmdbUi$$ExternalSyntheticLambda2(this, r18)).setNegativeButton("Cerrar", null).create();
        r10.show();
        style(r10);
        r10.getWindow().setLayout((int) (((double) this.activity.getResources().getDisplayMetrics().widthPixels) * 0.92d), (int) (((double) this.activity.getResources().getDisplayMetrics().heightPixels) * 0.82d));
        this.service.details(r18, r19, new TmdbUi$$ExternalSyntheticLambda3(this, r10, r6));
    }

    private void style(AlertDialog r9) {
        Window r0 = r9.getWindow();
        GradientDrawable r1 = new GradientDrawable();
        r1.setColor(AppTheme.color(this.activity, -301454517));
        r1.setCornerRadius(m411dp(16));
        r1.setStroke(m411dp(1), AppTheme.color(this.activity, -13460261));
        r0.setBackgroundDrawable(r1);
        int[] r2 = {-1, -2, -3};
        int r3 = 0;
    L4:
        if (r3 >= 3) goto L9;
        int r4 = r2[r3];
        if (r9.getButton(r4) == null) goto L8;
        r9.getButton(r4).setTextColor(AppTheme.color(this.activity, -10299137));
    L8:
        r3 = r3 + 1;
        goto L4
    }

    private TextView text(String r6, int r7) {
        TextView r0 = new TextView(this.activity);
        r0.setText(r6);
        r0.setTextSize(r7);
        r0.setTextColor(AppTheme.color(this.activity, -1182209));
        r0.setPadding(m411dp(8), m411dp(8), m411dp(8), m411dp(8));
        return r0;
    }

    void changeFicha(Entry r1) {
        find(r1);
    }

    void info(Entry r2) {
        JSONObject r0 = this.service.cached(r2);
        if (r0 == null) goto L6;
        show(r2, r0);
        return;
    L6:
        find(r2);
    }

    void preview(final Entry r19, final Runnable r20, final Runnable r21) {
        LinearLayout r0 = new LinearLayout(this.activity);
        r0.setOrientation(1);
        r0.setPadding(m411dp(12), m411dp(8), m411dp(12), m411dp(8));
        LinearLayout r1 = new LinearLayout(this.activity);
        final ImageView r2 = new ImageView(this.activity);
        r2.setScaleType(ImageView.ScaleType.FIT_CENTER);
        r1.addView(r2, new LinearLayout.LayoutParams(m411dp(105), m411dp(156)));
        StringBuilder r3 = new StringBuilder().append(r19.title).append("\n").append(r19.group);
        if (r19.kind.equals("Series") == true) goto L5;
    L9:
        String r4 = "";
    L10:
        final TextView r5 = text(r3.append(r4).toString(), 14);
        r1.addView(r5, new LinearLayout.LayoutParams(0, -2, 1.0f));
        r0.addView(r1);
        this.logos.show(r2, r19.logo);
        if (this.service.enabled() == false) goto L13;
        String r6 = "Cargando sinopsis…";
    L14:
        final TextView r7 = text(r6, 14);
        r0.addView(r7);
        credit(r0);
        ScrollView r8 = new ScrollView(this.activity);
        r8.addView(r0);
        AlertDialog.Builder r12 = new AlertDialog.Builder(this.activity).setTitle(TmdbTitles.query(r19)).setView(r8).setPositiveButton("Reproducir", new TmdbUi$$ExternalSyntheticLambda9(r20)).setNegativeButton("Cerrar", null);
        if (r21 == null) goto L17;
        r12.setNeutralButton("Ver episodios", new TmdbUi$$ExternalSyntheticLambda10(r21));
    L17:
        final AlertDialog r9 = r12.create();
        r9.show();
        style(r9);
        r9.getWindow().setLayout((int) (((double) this.activity.getResources().getDisplayMetrics().widthPixels) * 0.92d), (int) (((double) this.activity.getResources().getDisplayMetrics().heightPixels) * 0.82d));
        final Render r10 = new TmdbUi$$ExternalSyntheticLambda11(this, r9, r2, r19, r5, r7);
        final Render r11 = new TmdbUi$$ExternalSyntheticLambda12(this, r10, r19, r9);
        JSONObject r15 = this.service.cached(r19);
        if (r15 == null) goto L22;
        r11.accept(r15);
        return;
    L22:
        if (this.service.enabled() == true) goto L24;
        return;
    L24:
        this.service.find(r19, new TmdbUi$$ExternalSyntheticLambda13(this, r9, r19, r7, r11));
        return;
    L13:
        r6 = "Sin sinopsis. Puedes activar TMDB desde Ajustes.";
        goto L14
    L5:
        if (r19.season < 0) goto L9;
        if (r19.episode < 0) goto L9;
        r4 = "\nTemporada " + r19.season + " · Episodio " + r19.episode;
        goto L10
    }

    void settings() {
        LinearLayout r0 = new LinearLayout(this.activity);
        r0.setOrientation(1);
        r0.setPadding(m411dp(12), 0, m411dp(12), 0);
        final CheckBox r2 = new CheckBox(this.activity);
        r2.setText("Portadas automáticas de películas y series");
        r2.setTextColor(-1);
        r2.setChecked(this.service.settings.getBoolean("enabled", true));
        r0.addView(r2);
        final CheckBox r1 = new CheckBox(this.activity);
        r1.setText("Usar sinopsis en inglés si falta en castellano");
        r1.setTextColor(-1);
        r1.setChecked(this.service.englishFallback());
        r0.addView(r1);
        final EditText r4 = new EditText(this.activity);
        r4.setSingleLine();
        r4.setTextColor(-1);
        r4.setHint("Clave API de TMDB");
        r4.setInputType(TsExtractor.TS_STREAM_TYPE_AC3);
        r4.setText(this.service.apiKey());
        r4.setEnabled(BuildConfig.TMDB_API_KEY.isEmpty());
        r0.addView(r4);
        r0.addView(text("Mantén pulsada una película o serie para abrir su información. Los datos consultados se guardan en caché.", 13));
        credit(r0);
        AlertDialog r3 = new AlertDialog.Builder(this.activity).setTitle("TMDB · Información y portadas").setView(r0).setPositiveButton("Guardar", new TmdbUi$$ExternalSyntheticLambda0(this, r4, r2, r1)).setNeutralButton("Vaciar caché", new TmdbUi$$ExternalSyntheticLambda6(this)).setNegativeButton("Cerrar", null).create();
        r3.show();
        style(r3);
    }
}
