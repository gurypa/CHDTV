package es.antonio.m3u;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface;
import androidx.media3.extractor.p001ts.PsExtractor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class CinematicDetail {

    /* JADX INFO: renamed from: es.antonio.m3u.CinematicDetail$1 */
    class C09751 implements AdapterView.OnItemSelectedListener {
        final /* synthetic */ Activity val$a;
        final /* synthetic */ Spinner val$chooser;
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ IntUnaryOperator val$dp;
        final /* synthetic */ Entry val$e;
        final /* synthetic */ Consumer val$episodePlay;
        final /* synthetic */ List val$keys;
        final /* synthetic */ LogoLoader val$logos;
        final /* synthetic */ LinearLayout val$row;
        final /* synthetic */ TreeMap val$seasons;
        final /* synthetic */ TmdbService val$tmdb;

        C09751(LinearLayout r1, TreeMap r2, List r3, Activity r4, IntUnaryOperator r5, LogoLoader r6, Consumer r7, TmdbService r8, Entry r9, Dialog r10, Spinner r11) {
            this.val$row = r1;
            this.val$seasons = r2;
            this.val$keys = r3;
            this.val$a = r4;
            this.val$dp = r5;
            this.val$logos = r6;
            this.val$episodePlay = r7;
            this.val$tmdb = r8;
            this.val$e = r9;
            this.val$dialog = r10;
            this.val$chooser = r11;
        }

        static /* synthetic */ void lambda$onItemSelected$0(Activity r3, TextView r4, TextView r5, View r6) {
            new AlertDialog.Builder(r3).setTitle(r4.getText()).setMessage(r5.getText()).setPositiveButton("Cerrar", null).show();
        }

        static /* synthetic */ void lambda$onItemSelected$1(Consumer r0, Entry r1) {
            r0.accept(r1);
        }

        static /* synthetic */ void lambda$onItemSelected$2(Dialog r18, Activity r19, Spinner r20, int r21, LinearLayout r22, LogoLoader r23, List r24, String r25) {
            if (r18.isShowing() == false) goto L42;
            if (r19.isDestroyed() == true) goto L42;
            if (r20.getSelectedItemPosition() != r21) goto L47;
            HashMap r0 = new HashMap();
            Iterator r2 = r24.iterator();
        L11:
            if (r2.hasNext() == false) goto L13;
            JSONObject r3 = (JSONObject) r2.next();
            r0.put(Integer.valueOf(r3.optInt("episode_number", -1)), r3);
            goto L11
        L13:
            int r4 = 0;
        L15:
            if (r4 >= r22.getChildCount()) goto L40;
            LinearLayout r5 = (LinearLayout) r22.getChildAt(r4);
            Entry r6 = (Entry) r5.getTag();
            JSONObject r7 = (JSONObject) r0.get(Integer.valueOf(r6.episode));
            TextView r8 = (TextView) r5.getChildAt(1);
            TextView r9 = (TextView) r5.getChildAt(2);
            if (r7 != null) goto L22;
            if (r25 != null) goto L20;
            String r10 = "Sin metadatos de este capítulo";
        L21:
            r9.setText(r10);
        L38:
            r4 = r4 + 1;
            goto L15
        L20:
            r10 = r25;
            goto L21
        L22:
            r8.setText(ExifInterface.LONGITUDE_EAST + r6.episode + " · " + r7.optString("name", r6.title));
            String r13 = "";
            StringBuilder r11 = new StringBuilder().append(r7.optString("air_date", ""));
            if (r7.optInt("runtime") <= 0) goto L25;
            String r12 = " · " + r7.optInt("runtime") + " min";
        L26:
            StringBuilder r14 = r11.append(r12);
            if (r7.optDouble("vote_average") <= 0.0d) goto L29;
            r13 = " · ★ " + String.format(Locale.getDefault(), "%.1f", new Object[]{Double.valueOf(r7.optDouble("vote_average"))});
        L29:
            String r15 = r14.append(r13).toString();
            String r16 = TmdbService.synopsis(r7);
            StringBuilder r17 = new StringBuilder().append(r15).append("\n");
            if (r16.isEmpty() == false) goto L32;
            String r110 = "Sin sinopsis de este capítulo";
        L33:
            r9.setText(r17.append(r110).toString());
            ImageView r111 = (ImageView) r5.getChildAt(0);
            String r112 = TmdbService.episodeImage(r7);
            if (r112.isEmpty() == false) goto L36;
            String r113 = "Sin imagen disponible del episodio";
        L37:
            r111.setContentDescription(r113);
            r23.show(r111, r112);
            goto L38
        L36:
            r113 = "Imagen del episodio " + r6.episode;
            goto L37
        L32:
            r110 = r16;
            goto L33
        L25:
            r12 = "";
            goto L26
        L40:
            return;
        L47:
            return;
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> r15, View r16, final int r17, long r18) {
            if ((r16 instanceof TextView) == false) goto L5;
            ((TextView) r16).setTextColor(-1);
        L5:
            this.val$row.removeAllViews();
            Iterator r1 = ((List) this.val$seasons.get(this.val$keys.get(r17))).iterator();
        L7:
            if (r1.hasNext() == false) goto L9;
            final Entry r3 = (Entry) r1.next();
            LinearLayout r4 = new LinearLayout(this.val$a);
            r4.setOrientation(1);
            r4.setPadding(this.val$dp.applyAsInt(6), this.val$dp.applyAsInt(8), this.val$dp.applyAsInt(6), this.val$dp.applyAsInt(8));
            ImageView r6 = new ImageView(this.val$a);
            r6.setScaleType(ImageView.ScaleType.CENTER_CROP);
            r4.addView(r6, new LinearLayout.LayoutParams(this.val$dp.applyAsInt(228), this.val$dp.applyAsInt(128)));
            r6.setBackgroundColor(AppTheme.color(this.val$a, -15981256));
            r6.setContentDescription("Sin imagen disponible del episodio");
            this.val$logos.show(r6, "");
            final TextView r7 = CinematicDetail.m551$$Nest$smlabel(this.val$a, ExifInterface.LONGITUDE_EAST + r3.episode + " · " + r3.title, 14);
            r7.setMaxLines(2);
            r4.addView(r7);
            final TextView r8 = CinematicDetail.m551$$Nest$smlabel(this.val$a, "Sin metadatos del capítulo", 12);
            r8.setMaxLines(5);
            final Activity r9 = this.val$a;
            r8.setOnClickListener(new CinematicDetail$1$$ExternalSyntheticLambda0(r9, r7, r8));
            r4.addView(r8);
            Activity r10 = this.val$a;
            final Consumer r11 = this.val$episodePlay;
            r4.addView(CinematicDetail.m550$$Nest$smaction(r10, "▶ Ver episodio", new CinematicDetail$1$$ExternalSyntheticLambda1(r11, r3)), new LinearLayout.LayoutParams(-1, this.val$dp.applyAsInt(48)));
            r4.setTag(r3);
            this.val$row.addView(r4, new LinearLayout.LayoutParams(this.val$dp.applyAsInt(PsExtractor.VIDEO_STREAM_MASK), -2));
            goto L7
        L9:
            int r12 = ((Integer) this.val$keys.get(r17)).intValue();
            TmdbService r13 = this.val$tmdb;
            Entry r14 = this.val$e;
            final Dialog r2 = this.val$dialog;
            final Activity r5 = this.val$a;
            final Spinner r19 = this.val$chooser;
            final LinearLayout r20 = this.val$row;
            final LogoLoader r21 = this.val$logos;
            r13.season(r14, r12, new CinematicDetail$1$$ExternalSyntheticLambda2(r2, r5, r19, r17, r20, r21));
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> r1) {
        }
    }

    /* JADX INFO: renamed from: -$$Nest$smaction, reason: not valid java name */
    static /* bridge */ /* synthetic */ Button m550$$Nest$smaction(Activity r0, String r1, Runnable r2) {
        return action(r0, r1, r2);
    }

    /* JADX INFO: renamed from: -$$Nest$smlabel, reason: not valid java name */
    static /* bridge */ /* synthetic */ TextView m551$$Nest$smlabel(Activity r0, String r1, int r2) {
        return label(r0, r1, r2);
    }

    CinematicDetail() {
    }

    private static Button action(Activity r2, String r3, final Runnable r4) {
        Button r0 = new Button(r2);
        r0.setText(r3);
        SettingsSidePanel.style(r0);
        r0.setOnClickListener(new CinematicDetail$$ExternalSyntheticLambda0(r4));
        return r0;
    }

    private static TextView label(Activity r2, String r3, int r4) {
        TextView r0 = new TextView(r2);
        r0.setText(r3);
        r0.setTextSize(r4);
        r0.setTextColor(AppTheme.color(r2, -1182209));
        return r0;
    }

    static /* synthetic */ void lambda$action$15(Runnable r0, View r1) {
        r0.run();
    }

    static /* synthetic */ void lambda$show$0(Entry r0) {
    }

    static /* synthetic */ int lambda$show$1(float r1, int r2) {
        return Math.round(r2 * r1);
    }

    static /* synthetic */ void lambda$show$10(TmdbService r8, Entry r9, Consumer r10, Spinner[] r11) {
        JSONObject r0 = r8.cached(r9);
        if (r0 == null) goto L10;
        r10.accept(r0);
        Spinner r3 = r11[0];
        if (r3 != null) goto L7;
        return;
    L7:
        if (r3.getOnItemSelectedListener() == null) goto L12;
        r3.getOnItemSelectedListener().onItemSelected(r3, r3.getSelectedView(), r3.getSelectedItemPosition(), r3.getSelectedItemId());
        return;
    L12:
        return;
    }

    static /* synthetic */ void lambda$show$11(Activity r2, final TmdbService r3, LogoLoader r4, final Entry r5, final Consumer r6, final Spinner[] r7) {
        new TmdbUi(r2, r3, r4, new CinematicDetail$$ExternalSyntheticLambda8(r3, r5, r6, r7)).changeFicha(r5);
    }

    static /* synthetic */ void lambda$show$12(Consumer r1, List r2, String r3) {
        if (r2.isEmpty() == true) goto L6;
        r1.accept((JSONObject) r2.get(0));
        return;
    }

    static /* synthetic */ void lambda$show$13(Consumer r1, List r2, String r3) {
        if (r2.isEmpty() == true) goto L6;
        r1.accept((JSONObject) r2.get(0));
        return;
    }

    static /* synthetic */ void lambda$show$14(Entry r2, final Consumer r3, TmdbService r4, JSONObject r5, Dialog r6, TextView r7, List r8, String r9) {
        JSONObject r0 = DemandDetailCache.exact(r2, r8);
        if (r0 == null) goto L5;
        r3.accept(r0);
        r4.details(r2, r0, new CinematicDetail$$ExternalSyntheticLambda7(r3));
        return;
    L5:
        if (r5 == null) goto L7;
        return;
    L7:
        if (r6.isShowing() == false) goto L15;
        if (r9 != null) goto L10;
        String r1 = "Sin ficha inequívoca disponible";
    L11:
        r7.setText(r1);
        return;
    L10:
        r1 = r9;
        goto L11
    }

    static /* synthetic */ void lambda$show$2(Dialog r0) {
        r0.dismiss();
    }

    static /* synthetic */ void lambda$show$3(Runnable r0) {
        r0.run();
    }

    static /* synthetic */ void lambda$show$4(Activity r0, Entry r1) {
        RecordingUi.open(r0, r1);
    }

    static /* synthetic */ void lambda$show$5(float r2, LinearLayout r3, View r4, int r5, int r6, int r7, int r8, int r9, int r10, int r11, int r12) {
        if (((r7 - r5) / r2) >= 640.0f) goto L5;
        int r0 = 1;
    L7:
        if (r3.getOrientation() == r0) goto L10;
        r3.setOrientation(r0);
        return;
    L10:
        return;
    L5:
        r0 = 0;
        goto L7
    }

    static /* synthetic */ List lambda$show$6(Integer r1) {
        return new ArrayList();
    }

    static /* synthetic */ int lambda$show$7(Entry r1) {
        return r1.episode;
    }

    static /* synthetic */ void lambda$show$8(Dialog r0, Consumer r1, Entry r2) {
        r0.dismiss();
        r1.accept(r2);
    }

    static /* synthetic */ void lambda$show$9(Activity r16, final Dialog r17, LinearLayout r18, LogoLoader r19, List r20, Entry r21, TmdbService r22, final Consumer r23, TextView r24, TextView r25, TextView r26, ImageView r27, JSONObject r28) {
        if (r16.isDestroyed() == true) goto L49;
        if (r17.isShowing() == false) goto L61;
        DetailExtras.render(r16, r18, r28, r19, r20, r21, r22, new CinematicDetail$$ExternalSyntheticLambda6(r17, r23));
        r24.setText(r28.optString("title", r28.optString("name", TmdbTitles.query(r21))));
        r25.setText(TmdbService.synopsis(r28));
        String r5 = "";
        String r6 = r28.optString("release_date", r28.optString("first_air_date", ""));
        String r0 = "";
        JSONArray r7 = r28.optJSONArray("genres");
        if (r7 == null) goto L18;
        int r11 = 0;
    L11:
        if (r11 >= r7.length()) goto L17;
        StringBuilder r12 = new StringBuilder().append(r0);
        if (r11 != 0) goto L15;
        String r13 = "";
    L16:
        r0 = r12.append(r13).append(r7.optJSONObject(r11).optString("name")).toString();
        r11 = r11 + 1;
        goto L11
    L15:
        r13 = " · ";
        goto L16
    L17:
        String r1 = r0;
    L19:
        StringBuilder r2 = new StringBuilder().append(r6);
        if (r1.isEmpty() == false) goto L22;
        String r14 = "";
    L23:
        StringBuilder r3 = r2.append(r14);
        if (r28.optDouble("vote_average") <= 0.0d) goto L26;
        String r15 = " · ★ " + String.format(Locale.getDefault(), "%.1f", new Object[]{Double.valueOf(r28.optDouble("vote_average"))}) + "/10";
    L27:
        StringBuilder r4 = r3.append(r15);
        if (r28.optInt("runtime") <= 0) goto L30;
        r5 = " · " + r28.optInt("runtime") + " min";
    L30:
        r26.setText(r4.append(r5).toString());
        JSONObject r8 = new JSONObject(r28.toString());     // Catch: Exception -> L44
        String r9 = r28.optString("backdrop_path");     // Catch: Exception -> L44
        if (r9.isEmpty() == true) goto L34;
        r8.put("poster_path", r9);     // Catch: Exception -> L44
    L34:
        r19.show(r27, TmdbService.poster(r8), r21.logo);     // Catch: Exception -> L40
        return;
    L60:
        return;
    L47:
        return;
    L26:
        r15 = "";
        goto L27
    L22:
        r14 = " · " + r1;
        goto L23
    L18:
        r1 = "";
        goto L19
    L61:
        return;
    }

    static Dialog show(Activity r9, Entry r10, TmdbService r11, LogoLoader r12, Runnable r13, List<Entry> r14, Consumer<Entry> r15) {
        return show(r9, r10, r11, r12, r13, r14, r15, Collections.emptyList(), new CinematicDetail$$ExternalSyntheticLambda9());
    }

    static Dialog show(Activity r10, Entry r11, TmdbService r12, LogoLoader r13, Runnable r14, List<Entry> r15, Consumer<Entry> r16, List<Entry> r17, Consumer<Entry> r18) {
        return show(r10, r11, r12, r13, r14, r15, r16, r17, r18, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v21 */
    /* JADX WARN: Type inference failed for: r0v3, types: [android.widget.FrameLayout] */
    /* JADX WARN: Type inference failed for: r0v32 */
    /* JADX WARN: Type inference failed for: r0v33 */
    /* JADX WARN: Type inference failed for: r10v19 */
    /* JADX WARN: Type inference failed for: r10v4 */
    /* JADX WARN: Type inference failed for: r10v5, types: [android.app.Dialog] */
    /* JADX WARN: Type inference failed for: r14v1 */
    /* JADX WARN: Type inference failed for: r14v2, types: [android.view.View] */
    /* JADX WARN: Type inference failed for: r14v3 */
    /* JADX WARN: Type inference failed for: r29v0 */
    /* JADX WARN: Type inference failed for: r29v2 */
    /* JADX WARN: Type inference failed for: r2v4, types: [android.view.View, android.widget.ScrollView] */
    /* JADX WARN: Type inference failed for: r2v5, types: [android.view.View, android.widget.LinearLayout] */
    /* JADX WARN: Type inference failed for: r2v7, types: [android.app.Dialog] */
    static Dialog show(final Activity r33, final Entry r34, final TmdbService r35, final LogoLoader r36, final Runnable r37, List<Entry> r38, Consumer<Entry> r39, final List<Entry> r40, final Consumer<Entry> r41, JSONObject r42) {
        final Activity r1 = r33;
        final Spinner[] r14 = new Spinner[1];
        final float r15 = r1.getResources().getDisplayMetrics().density;
        IntUnaryOperator r5 = new CinematicDetail$$ExternalSyntheticLambda10(r15);
        final Dialog r10 = new Dialog(r1, android.R.style.Theme.Material.NoActionBar);
        ?? r0 = new FrameLayout(r1);
        r0.setBackgroundColor(AppTheme.color(r1, -16511464));
        final ImageView r3 = new ImageView(r1);
        r3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        r0.addView(r3, new FrameLayout.LayoutParams(-1, -1));
        View r4 = new View(r1);
        r4.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{AppTheme.color(r1, 1711541784), AppTheme.color(r1, -335278568), AppTheme.color(r1, -16511464)}));
        r0.addView(r4, new FrameLayout.LayoutParams(-1, -1));
        ?? r2 = new ScrollView(r1);
        ?? r6 = new LinearLayout(r1);
        r6.setOrientation(1);
        r6.setPadding(r5.applyAsInt(24), r5.applyAsInt(76), r5.applyAsInt(24), r5.applyAsInt(24));
        r2.addView(r6);
        r0.addView(r2, new FrameLayout.LayoutParams(-1, -1));
        Button r7 = action(r1, "‹ Atrás", new CinematicDetail$$ExternalSyntheticLambda12(r10));
        FrameLayout.LayoutParams r9 = new FrameLayout.LayoutParams(r5.applyAsInt(112), r5.applyAsInt(48), 8388661);
        r9.setMargins(r5.applyAsInt(16), r5.applyAsInt(16), r5.applyAsInt(16), 0);
        r0.addView(r7, r9);
        r7.setContentDescription("Atrás");
        final TextView r8 = label(r1, TmdbTitles.query(r34), 30);
        r8.setTypeface(null, 1);
        r8.setPadding(0, r5.applyAsInt(40), 0, r5.applyAsInt(12));
        r6.addView(r8);
        final TextView r11 = label(r1, r34.group, 15);
        r6.addView(r11);
        if (r35.enabled() == false) goto L5;
        String r21 = "Cargando sinopsis…";
    L6:
        TextView r12 = label(r1, r21, 16);
        r12.setPadding(0, r5.applyAsInt(16), 0, r5.applyAsInt(20));
        r6.addView(r12);
        final LinearLayout r13 = new LinearLayout(r1);
        if ((r1.getResources().getDisplayMetrics().widthPixels / r1.getResources().getDisplayMetrics().density) >= 640.0f) goto L10;
        r13.setOrientation(1);
    L10:
        Button r16 = action(r1, "▶ Reproducir", new CinematicDetail$$ExternalSyntheticLambda13(r37));
        r16.setActivated(true);
        r13.addView(r16, new LinearLayout.LayoutParams(r5.applyAsInt(180), r5.applyAsInt(52)));
        if (r34.kind.equals("Series") == false) goto L13;
        String r17 = "● Grabar este episodio";
    L14:
        r13.addView(action(r1, r17, new CinematicDetail$$ExternalSyntheticLambda14(r1, r34)), new LinearLayout.LayoutParams(r5.applyAsInt(180), r5.applyAsInt(52)));
        r6.addView(r13);
        r0.addOnLayoutChangeListener(new CinematicDetail$$ExternalSyntheticLambda15(r15, r13));
        if (r38 != null) goto L17;
    L31:
        LinearLayout r31 = r13;
        TextView r22 = r12;
        ?? r18 = r10;
        int r19 = 15;
        ?? r110 = r0;
    L32:
        final LinearLayout r20 = new LinearLayout(r1);
        r20.setOrientation(1);
        r6.addView(r20);
        if (r35.enabled() == false) goto L35;
        String r23 = "Cargando reparto y títulos relacionados…";
    L36:
        r20.addView(label(r1, r23, r19));
        r6.addView(label(r1, "Información e imágenes de TMDB cuando están disponibles", 11));
        r18.setContentView(r110);
        DialogHandoff.track(r1, r18);
        r18.show();
        r18.getWindow().setLayout(-1, -1);
        r7.requestFocus();
        final ?? r24 = r18;
        final TextView r111 = r22;
        final Consumer r25 = new CinematicDetail$$ExternalSyntheticLambda3(r1, r24, r20, r36, r40, r34, r35, r41, r8, r111, r11, r3);
        r31.addView(action(r33, "Cambiar ficha TMDB", new CinematicDetail$$ExternalSyntheticLambda4(r33, r35, r36, r34, r25, r14)), new LinearLayout.LayoutParams(r5.applyAsInt(200), r5.applyAsInt(52)));
        r36.show(r3, r34.logo);
        if (r42 == null) goto L39;
        final JSONObject r26 = r42;
        r25.accept(r26);
    L40:
        JSONObject r27 = r35.peek(r34);
        if (r27 == null) goto L53;
        r25.accept(r27);
        if (r35.enabled() == false) goto L60;
        if (r27.has("credits") == true) goto L47;
    L50:
        r35.details(r34, r27, new CinematicDetail$$ExternalSyntheticLambda5(r25));
        goto L60
    L47:
        if (r27.has("recommendations") == false) goto L50;
        if (r27.optBoolean("overview_fallback_checked") == false) goto L50;
    L60:
        return r24;
    L53:
        if (r35.detailCanLoad() == false) goto L56;
        final TextView r28 = r22;
        r35.detailFind(r34, new CinematicDetail$$ExternalSyntheticLambda11(r34, r25, r35, r26, r24, r28));
        return r24;
    L56:
        return r24;
    L39:
        r26 = r42;
        goto L40
    L35:
        r23 = "Configura TMDB en Reproducción para ver reparto, títulos relacionados e información ampliada.";
        goto L36
    L17:
        if (r38.isEmpty() == true) goto L31;
        TreeMap r29 = new TreeMap();
        Iterator<Entry> r30 = r38.iterator();
        ?? r32 = r0;
    L20:
        if (r30.hasNext() == false) goto L22;
        Entry r112 = r30.next();
        ((List) r29.computeIfAbsent(Integer.valueOf(Math.max(0, r112.season)), new CinematicDetail$$ExternalSyntheticLambda1())).add(r112);
        r32 = r32;
        goto L20
    L22:
        ?? r210 = r32;
        Iterator r43 = r29.values().iterator();
    L24:
        if (r43.hasNext() == false) goto L26;
        ((List) r43.next()).sort(Comparator.comparingInt(new CinematicDetail$$ExternalSyntheticLambda2()));
        goto L24
    L26:
        r6.addView(label(r1, "Episodios", 23));
        Spinner r44 = new Spinner(r1);
        r14[0] = r44;
        ArrayList r45 = new ArrayList(r29.keySet());
        ArrayList r46 = new ArrayList();
        Iterator r113 = r45.iterator();
    L28:
        if (r113.hasNext() == false) goto L30;
        LinearLayout r310 = r13;
        r46.add("Temporada " + ((Integer) r113.next()).intValue());
        r29 = r29;
        r13 = r310;
        goto L28
    L30:
        LinearLayout r311 = r13;
        TreeMap r211 = r29;
        r44.setAdapter(new ArrayAdapter(r1, android.R.layout.simple_spinner_dropdown_item, r46));
        r6.addView(r44, new LinearLayout.LayoutParams(r5.applyAsInt(210), r5.applyAsInt(52)));
        HorizontalScrollView r47 = new HorizontalScrollView(r1);
        LinearLayout r48 = new LinearLayout(r1);
        r47.addView(r48);
        r6.addView(r47);
        r22 = r12;
        Dialog r114 = r10;
        r110 = r210;
        r31 = r311;
        r19 = 15;
        C09751 r49 = new C09751(r48, r211, r45, r1, r5, r36, r39, r35, r34, r114, r44);
        r1 = r1;
        r44.setOnItemSelectedListener(r49);
        r18 = r114;
        goto L32
    L13:
        r17 = "● Grabar";
        goto L14
    L5:
        r21 = "Sin sinopsis disponible. Puedes configurar TMDB en Reproducción.";
        goto L6
    }
}
