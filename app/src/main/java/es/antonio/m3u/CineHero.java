package es.antonio.m3u;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.media3.common.C0180C;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class CineHero extends FrameLayout {
    private final HorizontalScrollView actors;
    final ImageView artwork;
    final LinearLayout cast;
    final LinearLayout copy;
    private int detailsGeneration;
    private int generation;
    private final Handler handler;
    private String key;
    private final LogoLoader logos;
    final TextView metadata;
    final TextView synopsis;
    final TextView title;
    private final TmdbService tmdb;

    public static /* synthetic */ void $r8$lambda$5y7FLg5mymbqZTaa7TFfh4zx2hE(CineHero r0, int r1, Entry r2, List r3, String r4) {
        r0.lambda$details$3(r1, r2, r3, r4);
    }

    /* JADX INFO: renamed from: $r8$lambda$9_ZqCyqq--6by_thRv405QOAUHQ, reason: not valid java name */
    public static /* synthetic */ void m548$r8$lambda$9_ZqCyqq6by_thRv405QOAUHQ(CineHero r0, int r1, Entry r2) {
        r0.lambda$bind$1(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$JPTD8_0XFAG4bROQBIJ73MAurqQ(CineHero r0, int r1, Entry r2, List r3, String r4) {
        r0.lambda$bind$0(r1, r2, r3, r4);
    }

    /* JADX INFO: renamed from: $r8$lambda$eHXr4ahrKsYFpgLhFSZ3L--N3Xg, reason: not valid java name */
    public static /* synthetic */ void m549$r8$lambda$eHXr4ahrKsYFpgLhFSZ3LN3Xg(CineHero r0, int r1, Entry r2, JSONObject r3) {
        r0.lambda$bindLocal$2(r1, r2, r3);
    }

    CineHero(Context r10, TmdbService r11, LogoLoader r12) {
        super(r10);
        this.handler = new Handler(Looper.getMainLooper());
        this.key = "";
        this.detailsGeneration = -1;
        this.tmdb = r11;
        this.logos = r12;
        setBackgroundColor(AppTheme.cineBackground(r10));
        setFocusable(false);
        this.artwork = new ImageView(r10);
        this.artwork.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(this.artwork, new FrameLayout.LayoutParams(-1, -1));
        View r2 = new View(r10);
        r2.setBackground(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{AppTheme.cineBackground(r10), (AppTheme.cineBackground(r10) & ViewCompat.MEASURED_SIZE_MASK) | (-436207616), (16777215 & AppTheme.cineBackground(r10)) | C0180C.ENCODING_PCM_24BIT_BIG_ENDIAN}));
        addView(r2, new FrameLayout.LayoutParams(-1, -1));
        this.copy = new LinearLayout(r10);
        this.copy.setOrientation(1);
        this.copy.setPadding(m394dp(12), m394dp(4), m394dp(12), m394dp(4));
        addView(this.copy, new FrameLayout.LayoutParams(-1, -1));
        this.title = label(26, -723219);
        this.title.setTypeface(null, 1);
        this.title.setMaxLines(1);
        this.copy.addView(this.title);
        this.metadata = label(11, AppTheme.cineAccent(r10));
        this.metadata.setMaxLines(1);
        this.copy.addView(this.metadata);
        this.synopsis = label(11, -3945541);
        this.synopsis.setMaxLines(3);
        this.synopsis.setPadding(0, m394dp(2), 0, m394dp(2));
        this.copy.addView(this.synopsis);
        this.actors = new HorizontalScrollView(r10);
        this.actors.setHorizontalScrollBarEnabled(false);
        this.cast = new LinearLayout(r10);
        this.actors.addView(this.cast);
        this.copy.addView(this.actors, new LinearLayout.LayoutParams(-1, m394dp(70)));
    }

    private void details(final Entry r3, JSONObject r4, final int r5) {
        render(r3, r4);
        if (r4.optString("overview").trim().isEmpty() == true) goto L5;
        return;
    L5:
        if (this.tmdb.enabled() == true) goto L7;
        return;
    L7:
        if (this.detailsGeneration == r5) goto L13;
        this.detailsGeneration = r5;
        this.tmdb.previewDetails(r3, r4, new CineHero$$ExternalSyntheticLambda2(this, r5, r3));
        return;
    }

    /* JADX INFO: renamed from: dp */
    private int m394dp(int r3) {
        return Math.round(r3 * getResources().getDisplayMetrics().density);
    }

    private TextView label(int r3, int r4) {
        TextView r0 = new TextView(getContext());
        r0.setTextSize(r3);
        r0.setTextColor(r4);
        r0.setEllipsize(TextUtils.TruncateAt.END);
        return r0;
    }

    private /* synthetic */ void lambda$bind$0(int r3, Entry r4, List r5, String r6) {
        if (r3 == this.generation) goto L5;
        return;
    L5:
        if (isAttachedToWindow() == false) goto L13;
        JSONObject r0 = TmdbService.exact(r4, r5);
        if (r0 == null) goto L12;
        this.tmdb.remember(r4, r0);
        render(r4, r0);
        details(r4, r0, r3);
        return;
    L12:
        return;
    }

    private /* synthetic */ void lambda$bind$1(final int r4, final Entry r5) {
        if (r4 == this.generation) goto L5;
        return;
    L5:
        if (isAttachedToWindow() == false) goto L14;
        JSONObject r0 = this.tmdb.peek(r5);
        if (r0 == null) goto L11;
        details(r5, r0, r4);
        return;
    L11:
        this.tmdb.previewFind(r5, new CineHero$$ExternalSyntheticLambda3(this, r4, r5));
        return;
    }

    private /* synthetic */ void lambda$bindLocal$2(int r2, Entry r3, JSONObject r4) {
        if (r2 == this.generation) goto L5;
        return;
    L5:
        if (isAttachedToWindow() == false) goto L9;
        render(r3, r4);
        return;
    }

    private /* synthetic */ void lambda$details$3(int r2, Entry r3, List r4, String r5) {
        if (r2 == this.generation) goto L5;
        return;
    L5:
        if (isAttachedToWindow() == true) goto L7;
        return;
    L7:
        if (r4.isEmpty() == true) goto L12;
        render(r3, (JSONObject) r4.get(0));
        return;
    }

    static LinearLayout.LayoutParams previewLayout(Context r4) {
        if (r4.getResources().getConfiguration().screenHeightDp >= 480) goto L5;
        int r0 = 64;
    L7:
        return new LinearLayout.LayoutParams(-1, Math.round(r0 * r4.getResources().getDisplayMetrics().density));
    L5:
        r0 = 96;
        goto L7
    }

    private void render(Entry r2, JSONObject r3) {
        render(r2, r3, false);
    }

    private void render(Entry r26, JSONObject r27, boolean r28) {
        TextView r3 = this.title;
        String r4 = "name";
        String r5 = TmdbTitles.query(r26);
        if (r27 == null) goto L6;
        r5 = r27.optString("title", r27.optString("name", r5));
    L6:
        r3.setText(r5);
        String r6 = r26.kind;
        String r7 = r26.logo;
        this.synopsis.setText("Sin sinopsis disponible");
        this.cast.removeAllViews();
        if (r27 == null) goto L47;
        String r8 = r27.optString("release_date", r27.optString("first_air_date", ""));
        if (r8.length() < 4) goto L11;
        r6 = r6 + " · " + r8.substring(0, 4);
    L11:
        JSONArray r9 = r27.optJSONArray("genres");
        if (r9 == null) goto L19;
        if (r9.length() <= 0) goto L19;
        if (r9.optJSONObject(0) == null) goto L19;
        r6 = r6 + " · " + r9.optJSONObject(0).optString("name");
    L19:
        if (r27.optDouble("vote_average") <= 0.0d) goto L21;
        r6 = r6 + " · ★ " + String.format(Locale.getDefault(), "%.1f", new Object[]{Double.valueOf(r27.optDouble("vote_average"))});
    L21:
        this.synopsis.setText(TmdbService.synopsis(r27));
        String r10 = TmdbService.poster(r27);
        if (r10.isEmpty() == true) goto L24;
        r7 = r10;
    L24:
        String r11 = r27.optString("backdrop_path");
        if (r11.matches("/[A-Za-z0-9._/-]+") == false) goto L27;
        r7 = "https://image.tmdb.org/t/p/w780" + r11;
    L27:
        JSONObject r12 = r27.optJSONObject("credits");
        if (r12 != null) goto L30;
        JSONArray r15 = null;
    L31:
        int r13 = 0;
    L32:
        if (r15 == null) goto L46;
        if (r28 == false) goto L35;
        int r2 = Math.min(6, r15.length());
    L36:
        if (r13 >= r2) goto L46;
        JSONObject r14 = r15.optJSONObject(r13);
        if (r14 == null) goto L45;
        LinearLayout r16 = new LinearLayout(getContext());
        r16.setOrientation(1);
        r16.setPadding(m394dp(3), 0, m394dp(3), 0);
        ImageView r17 = new ImageView(getContext());
        r17.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GradientDrawable r18 = new GradientDrawable();
        r18.setColor(AppTheme.cineSurface(getContext()));
        r18.setCornerRadius(m394dp(50));
        r17.setBackground(r18);
        r17.setClipToOutline(true);
        r17.setContentDescription(r14.optString(r4));
        r16.addView(r17, new LinearLayout.LayoutParams(m394dp(36), m394dp(36)));
        TextView r19 = label(9, -3945541);
        r19.setText(r14.optString(r4));
        r19.setMaxLines(2);
        r16.addView(r19, new LinearLayout.LayoutParams(m394dp(62), m394dp(28)));
        String r20 = r14.optString("profile_path");
        LogoLoader r21 = this.logos;
        if (r20.matches("/[A-Za-z0-9._/-]+") == false) goto L43;
        String r22 = "https://image.tmdb.org/t/p/w92" + r20;
    L44:
        r21.show(r17, r22);
        this.cast.addView(r16);
        goto L45
    L43:
        r22 = "";
    L45:
        r13 = r13 + 1;
        r6 = r6;
        r7 = r7;
        r12 = r12;
        r8 = r8;
        r4 = r4;
        goto L32
    L35:
        r2 = 0;
    L46:
        r6 = r6;
        r7 = r7;
        goto L47
    L30:
        r15 = r12.optJSONArray("cast");
    L47:
        this.metadata.setText(r6);
        if (r7.isEmpty() == false) goto L50;
        this.artwork.setTag("");
        this.artwork.setImageDrawable(null);
        return;
    L50:
        this.logos.show(this.artwork, r7, r26.logo);
    }

    void bind(final Entry r7) {
        this.artwork.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String r0 = TmdbTitles.key(r7) + "\n" + r7.logo;
        if (r0.equals(this.key) == false) goto L5;
        return;
    L5:
        this.key = r0;
        final int r1 = this.generation + 1;
        this.generation = r1;
        this.handler.removeCallbacksAndMessages(null);
        render(r7, this.tmdb.peek(r7), false);
        this.handler.postDelayed(new CineHero$$ExternalSyntheticLambda0(this, r1, r7), 350);
    }

    void bindChannel(Entry r6, EpgData r7) {
        this.key = "";
        this.generation++;
        this.handler.removeCallbacksAndMessages(null);
        render(r6, null);
        this.title.setText(r6.title);
        this.metadata.setText(StreamFormat.label(r6) + " · TV en directo");
        this.artwork.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.cast.removeAllViews();
        EpgData.Programme[] r0 = r7.nowNext(r6, System.currentTimeMillis());
        if (r0[0] != null) goto L5;
        String r2 = "Sin programa actual en la EPG";
    L7:
        if (r0[1] == null) goto L9;
        r2 = r2 + "\nDESPUÉS · " + r0[1].title;
    L9:
        this.synopsis.setText(r2);
        return;
    L5:
        r2 = "AHORA · " + r0[0].title + "\n" + r0[0].description;
        goto L7
    }

    void bindLocal(final Entry r6, final JSONObject r7) {
        this.key = "";
        final int r0 = this.generation + 1;
        this.generation = r0;
        this.handler.removeCallbacksAndMessages(null);
        render(r6, r7, false);
        this.handler.postDelayed(new CineHero$$ExternalSyntheticLambda1(this, r0, r6, r7), 350);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        this.generation++;
        this.key = "";
        this.handler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onSizeChanged(int r4, int r5, int r6, int r7) {
        super.onSizeChanged(r4, r5, r6, r7);
        this.actors.setVisibility(8);
        TextView r0 = this.synopsis;
        if (r5 >= m394dp(100)) goto L5;
        int r2 = 1;
    L6:
        r0.setMaxLines(r2);
        TextView r1 = this.title;
        if (r5 >= m394dp(100)) goto L9;
        float r3 = 18.0f;
    L10:
        r1.setTextSize(r3);
        FrameLayout.LayoutParams r8 = (FrameLayout.LayoutParams) this.copy.getLayoutParams();
        if (r4 < m394dp(600)) goto L13;
        int r9 = (int) (r4 * 0.68f);
    L14:
        r8.width = r9;
        this.copy.setLayoutParams(r8);
        return;
    L13:
        r9 = -1;
        goto L14
    L9:
        r3 = 20.0f;
        goto L10
    L5:
        r2 = 2;
        goto L6
    }

    void suspend() {
        this.generation++;
        this.key = "";
        this.handler.removeCallbacksAndMessages(null);
    }
}
