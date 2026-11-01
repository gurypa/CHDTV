package es.antonio.m3u;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.app.NotificationCompat;
import androidx.media3.extractor.p001ts.TsExtractor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class DetailExtras {
    DetailExtras() {
    }

    private static void cards(final Activity r16, LinearLayout r17, JSONArray r18, final boolean r19, LogoLoader r20) {
        JSONArray r2 = r18;
        if (r2 == null) goto L45;
        if (r2.length() == 0) goto L45;
        HorizontalScrollView r5 = new HorizontalScrollView(r16);
        LinearLayout r6 = new LinearLayout(r16);
        r5.addView(r6);
        r17.addView(r5);
        HashSet r7 = new HashSet();
        int r8 = 0;
    L9:
        if (r8 >= Math.min(r2.length(), 20)) goto L43;
        final JSONObject r9 = r2.optJSONObject(r8);
        if (r9 == null) goto L41;
        if (r7.add(Integer.valueOf(r9.optInt("id", (-r8) - 1))) == false) goto L41;
        LinearLayout r10 = new LinearLayout(r16);
        r10.setOrientation(1);
        r10.setPadding(m397dp(r16, 6), 0, m397dp(r16, 6), m397dp(r16, 8));
        ImageView r12 = new ImageView(r16);
        r12.setScaleType(ImageView.ScaleType.CENTER_CROP);
        r12.setBackgroundColor(AppTheme.color(r16, -15981256));
        if (r19 == false) goto L18;
        int r14 = 120;
    L19:
        int r15 = m397dp(r16, r14);
        if (r19 == false) goto L22;
        int r110 = 180;
    L23:
        r10.addView(r12, new LinearLayout.LayoutParams(r15, m397dp(r16, r110)));
        if (r19 == false) goto L26;
        String r13 = "profile_path";
    L27:
        String r111 = r9.optString(r13);
        if (r111.matches("/[A-Za-z0-9._/-]+") == false) goto L30;
        String r11 = "https://image.tmdb.org/t/p/w185" + r111;
    L31:
        r20.show(r12, r11);
        final String r4 = r9.optString("title", r9.optString("name", "Sin título"));
        r12.setContentDescription(r4);
        text(r16, r10, r4, 15).setMaxLines(3);
        if (r19 == false) goto L34;
        String r3 = r9.optString("character");
    L35:
        text(r16, r10, r3, 13).setMaxLines(2);
        r10.setFocusable(true);
        r10.setClickable(true);
        r10.setBackgroundResource(android.R.drawable.list_selector_background);
        r10.setContentDescription(r4);
        r10.setOnClickListener(new DetailExtras$$ExternalSyntheticLambda1(r16, r4, r19, r9));
        if (r19 == false) goto L38;
        int r112 = TsExtractor.TS_STREAM_TYPE_DTS_HD;
    L39:
        r6.addView(r10, new LinearLayout.LayoutParams(m397dp(r16, r112), -2));
        goto L41
    L38:
        r112 = 164;
        goto L39
    L34:
        r3 = r9.optString("release_date", r9.optString("first_air_date", ""));
        goto L35
    L30:
        r11 = "";
        goto L31
    L26:
        r13 = "poster_path";
        goto L27
    L22:
        r110 = 222;
        goto L23
    L18:
        r14 = 148;
    L41:
        r8 = r8 + 1;
        r2 = r18;
        goto L9
    L43:
        return;
    L45:
        if (r19 == false) goto L47;
        String r21 = "Sin reparto disponible";
    L48:
        text(r16, r17, r21, 15);
        return;
    L47:
        r21 = "Sin títulos relacionados disponibles";
        goto L48
    }

    /* JADX INFO: renamed from: dp */
    private static int m397dp(Activity r2, int r3) {
        return Math.round(r3 * r2.getResources().getDisplayMetrics().density);
    }

    private static void field(StringBuilder r2, String r3, String r4) {
        if (r4 != null) goto L4;
        return;
    L4:
        if (r4.trim().isEmpty() == true) goto L8;
        r2.append(r3).append(": ").append(r4).append('\n');
        return;
    }

    private static void heading(Activity r4, LinearLayout r5, String r6) {
        TextView r0 = text(r4, r5, r6, 23);
        r0.setTypeface(null, 1);
        r0.setPadding(0, m397dp(r4, 24), 0, m397dp(r4, 12));
    }

    static String info(JSONObject r8) {
        StringBuilder r0 = new StringBuilder();
        field(r0, "Título original", r8.optString("original_title", r8.optString("original_name", "")));
        field(r0, "Lema", r8.optString("tagline"));
        field(r0, "Estado", r8.optString(NotificationCompat.CATEGORY_STATUS));
        field(r0, "Idioma original", r8.optString("original_language"));
        field(r0, "Productoras", names(r8.optJSONArray("production_companies")));
        field(r0, "Países", names(r8.optJSONArray("production_countries")));
        field(r0, "Idiomas", names(r8.optJSONArray("spoken_languages")));
        field(r0, "Creadores", names(r8.optJSONArray("created_by")));
        if (r8.optInt("number_of_seasons") <= 0) goto L6;
        field(r0, "Temporadas", String.valueOf(r8.optInt("number_of_seasons")));
    L6:
        if (r8.optInt("number_of_episodes") <= 0) goto L8;
        field(r0, "Episodios", String.valueOf(r8.optInt("number_of_episodes")));
    L8:
        JSONObject r1 = r8.optJSONObject("credits");
        if (r1 != null) goto L11;
        JSONArray r2 = null;
    L12:
        LinkedHashSet r3 = new LinkedHashSet();
        if (r2 == null) goto L23;
        int r4 = 0;
    L16:
        if (r4 >= r2.length()) goto L23;
        JSONObject r5 = r2.optJSONObject(r4);
        if (r5 == null) goto L22;
        if ("Director".equals(r5.optString("job")) == false) goto L22;
        r3.add(r5.optString("name"));
    L22:
        r4 = r4 + 1;
    L23:
        field(r0, "Dirección", DetailExtras$$ExternalSyntheticBackport0.m398m(", ", r3));
        if (r0.length() != 0) goto L27;
        return "Sin información ampliada disponible";
    L27:
        return r0.toString().trim();
    L11:
        r2 = r1.optJSONArray("crew");
        goto L12
    }

    static /* synthetic */ void lambda$cards$4(Activity r4, String r5, boolean r6, JSONObject r7, View r8) {
        AlertDialog.Builder r0 = new AlertDialog.Builder(r4).setTitle(r5);
        if (r6 == false) goto L5;
        StringBuilder r1 = new StringBuilder();
        StringBuilder r2 = r1.append(r5).append("\n");
        String r3 = r7.optString("character");
    L6:
        r0.setMessage(r2.append(r3).toString()).setPositiveButton("Cerrar", null).show();
        return;
    L5:
        r1 = new StringBuilder();
        r2 = r1.append(r7.optString("overview", "Sin sinopsis disponible"));
        r3 = "\n\nRecomendación de TMDB; su disponibilidad depende de tus listas.";
        goto L6
    }

    static /* synthetic */ void lambda$localRelated$1(Activity r1, LinearLayout r2, Object r3, LinearLayout r4, LogoLoader r5, Consumer r6, List r7, String r8) {
        if (r1.isDestroyed() == false) goto L5;
        return;
    L5:
        if (r2.getTag() == r3) goto L7;
        return;
    L7:
        if (r2.isAttachedToWindow() == false) goto L17;
        r4.removeAllViews();
        if (r8 == null) goto L12;
        text(r1, r4, r8, 15);
        return;
    L12:
        showRelated(r1, r4, r7, r5, r6);
        return;
    }

    static /* synthetic */ void lambda$render$0(Entry r0) {
    }

    static /* synthetic */ void lambda$showRelated$2(Consumer r1, List r2, DialogInterface r3, int r4) {
        r1.accept((Entry) r2.get(r4));
    }

    static /* synthetic */ void lambda$showRelated$3(List r6, final Consumer r7, Activity r8, View r9) {
        final ArrayList r0 = new ArrayList();
        HashSet r1 = new HashSet();
        Iterator r2 = r6.iterator();
    L4:
        if (r2.hasNext() == false) goto L11;
        Entry r3 = (Entry) r2.next();
        if ("Series".equals(r3.kind) == false) goto L9;
        if (r1.add(r3.group + "|" + TmdbTitles.key(r3)) == false) goto L4;
    L9:
        r0.add(r3);
        goto L4
    L11:
        if (r0.size() != 1) goto L13;
        r7.accept((Entry) r0.get(0));
        return;
    L13:
        String[] r4 = new String[r0.size()];
        int r5 = 0;
    L15:
        if (r5 >= r4.length) goto L17;
        r4[r5] = ((Entry) r0.get(r5)).title + " · " + ((Entry) r0.get(r5)).group;
        r5 = r5 + 1;
        goto L15
    L17:
        new AlertDialog.Builder(r8).setTitle("Elegir título de tus listas").setItems(r4, new DetailExtras$$ExternalSyntheticLambda4(r7, r0)).setNegativeButton("Cancelar", null).show();
    }

    private static void localRelated(final Activity r9, final LinearLayout r10, JSONArray r11, final LogoLoader r12, List<Entry> r13, Entry r14, TmdbService r15, final Consumer<Entry> r16) {
        if (r11 != null) goto L5;
    L14:
        text(r9, r10, "No hay títulos relacionados disponibles en tus listas", 15);
        return;
    L5:
        if (r11.length() == 0) goto L14;
        if (r13.isEmpty() == true) goto L14;
        final LinearLayout r1 = new LinearLayout(r9);
        r1.setOrientation(1);
        r10.addView(r1);
        text(r9, r1, "Cargando títulos de tus listas…", 15);
        final Object r5 = new Object();
        r10.setTag(r5);
        TmdbService.RelatedResults r2 = new DetailExtras$$ExternalSyntheticLambda2(r9, r10, r5, r1, r12, r16);
        if (r15 == null) goto L12;
        r15.related(r13, r14, r11, r2);
        return;
    L12:
        r2.done(new RelatedTitles(r13, r14, null).resolve(r11), null);
    }

    static List<Entry> matches(JSONObject r1, List<Entry> r2, Entry r3, TmdbService r4) {
        return new RelatedTitles(r2, r3, r4).match(r1);
    }

    private static String names(JSONArray r5) {
        ArrayList r0 = new ArrayList();
        if (r5 == null) goto L14;
        int r1 = 0;
    L6:
        if (r1 >= r5.length()) goto L14;
        JSONObject r2 = r5.optJSONObject(r1);
        if (r2 == null) goto L12;
        if (r2.optString("name").isEmpty() == true) goto L12;
        r0.add(r2.optString("name"));
    L12:
        r1 = r1 + 1;
    L14:
        return DetailExtras$$ExternalSyntheticBackport0.m398m(", ", r0);
    }

    static void render(Activity r8, LinearLayout r9, JSONObject r10, LogoLoader r11) {
        render(r8, r9, r10, r11, Collections.emptyList(), null, null, new DetailExtras$$ExternalSyntheticLambda5());
    }

    static void render(Activity r11, LinearLayout r12, JSONObject r13, LogoLoader r14, List<Entry> r15, Entry r16, TmdbService r17, Consumer<Entry> r18) {
        r12.setTag(new Object());
        r12.removeAllViews();
        heading(r11, r12, "Reparto");
        JSONObject r8 = r13.optJSONObject("credits");
        JSONArray r2 = null;
        if (r8 != null) goto L5;
        JSONArray r3 = null;
    L6:
        cards(r11, r12, r3, true, r14);
        heading(r11, r12, "Títulos relacionados");
        JSONObject r4 = r13.optJSONObject("recommendations");
        if (r4 != null) goto L9;
        JSONArray r6 = null;
    L10:
        if (r6 != null) goto L12;
    L15:
        JSONObject r5 = r13.optJSONObject("similar");
        if (r5 == null) goto L20;
        r2 = r5.optJSONArray("results");
    L20:
        localRelated(r11, r12, r2, r14, r15, r16, r17, r18);
        heading(r11, r12, "Información ampliada");
        text(r11, r12, info(r13), 15);
        return;
    L12:
        if (r6.length() == 0) goto L15;
        r2 = r6;
        goto L20
    L9:
        r6 = r4.optJSONArray("results");
        goto L10
    L5:
        r3 = r8.optJSONArray("cast");
        goto L6
    }

    private static void showRelated(final Activity r16, LinearLayout r17, List<RelatedTitles.Result> r18, LogoLoader r19, final Consumer<Entry> r20) {
        HorizontalScrollView r2 = new HorizontalScrollView(r16);
        LinearLayout r3 = new LinearLayout(r16);
        r2.addView(r3);
        Iterator<RelatedTitles.Result> r4 = r18.iterator();
    L4:
        if (r4.hasNext() == false) goto L11;
        RelatedTitles.Result r5 = r4.next();
        JSONObject r7 = r5.metadata;
        final List<Entry> r8 = r5.entries;
        LinearLayout r9 = new LinearLayout(r16);
        r9.setOrientation(1);
        r9.setPadding(m397dp(r16, 6), 0, m397dp(r16, 6), m397dp(r16, 8));
        ImageView r11 = new ImageView(r16);
        r11.setScaleType(ImageView.ScaleType.CENTER_CROP);
        r11.setBackgroundColor(AppTheme.color(r16, -15981256));
        r9.addView(r11, new LinearLayout.LayoutParams(m397dp(r16, 148), m397dp(r16, 222)));
        String r12 = TmdbService.poster(r7);
        if (r12.isEmpty() == false) goto L8;
        String r13 = r8.get(0).logo;
    L9:
        r19.show(r11, r13);
        text(r16, r9, TmdbTitles.query(r8.get(0)), 15).setMaxLines(3);
        text(r16, r9, "Disponible en tus listas", 13);
        r9.setFocusable(true);
        r9.setClickable(true);
        r9.setBackgroundResource(android.R.drawable.list_selector_background);
        r9.setOnClickListener(new DetailExtras$$ExternalSyntheticLambda3(r8, r20, r16));
        r3.addView(r9, new LinearLayout.LayoutParams(m397dp(r16, 164), -2));
        goto L4
    L8:
        r13 = r12;
        goto L9
    L11:
        if (r3.getChildCount() != 0) goto L13;
        text(r16, r17, "No hay títulos relacionados disponibles en tus listas", 15);
        return;
    L13:
        r17.addView(r2);
    }

    private static TextView text(Activity r4, LinearLayout r5, String r6, int r7) {
        TextView r0 = new TextView(r4);
        r0.setText(r6);
        r0.setTextColor(AppTheme.color(r4, -1182209));
        r0.setTextSize(r7);
        r0.setPadding(0, m397dp(r4, 6), 0, m397dp(r4, 8));
        r5.addView(r0);
        return r0;
    }
}
