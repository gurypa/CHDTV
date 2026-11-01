package es.antonio.m3u;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Process;
import android.os.SystemClock;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.exifinterface.media.ExifInterface;
import com.google.common.net.HttpHeaders;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes3.dex */
public class MainActivity extends ChdtvScreen {

    /* JADX INFO: renamed from: BG */
    private static final int f185BG = -16182497;
    private static final int CARD = -15457483;
    private static final int CYAN = -12986907;
    private static final int GREEN = -9181523;
    private static final int MUTED = -7232068;
    private static final int ORANGE = -18852;
    private static final int RED = -36480;
    private static final int WHITE = -1182209;
    private Cards adapter;
    private final Runnable artworkRefresh;
    private Button back;
    private boolean busy;
    private Catalog catalog;
    private int catalogGeneration;
    private CatalogIndex catalogIndex;
    private boolean catalogScrolling;
    private EditText catalogSearch;
    private boolean catalogVisible;
    private final ThreadPoolExecutor catalogWorker;
    private CineHero cineHero;
    private LinearLayout content;
    private boolean continueMode;
    private CatalogPlan deferredPlan;
    private CatalogRequest deferredRequest;
    private List<Entry> entries;
    private EpgData epg;
    private boolean epgRefreshing;
    private final ExecutorService epgWorker;
    private Button favoriteButton;
    private boolean favorites;
    private String filter;
    private boolean guide;
    private final Runnable guideRefresh;
    private TextView heading;
    private List<String> homeDetails;
    private String importKind;
    private String kind;
    private AbsListView list;
    private LogoLoader logos;
    private String page;
    private Parcelable pendingListState;
    private boolean playbackLaunching;
    private long playbackRequestedAt;
    private final PreviewActivation previewActivation;
    private String provider;
    private Map<Entry, String> providers;
    private final Runnable refresh;
    private LinearLayout root;
    private final List<Row> rows;
    private boolean savedEpgReady;
    private boolean savedEpgRequested;
    private final Runnable searchRender;
    private Integer season;
    private String section;
    private List<String> sectionChoices;
    private boolean sectionUpdating;
    private Spinner sections;
    private String series;
    private int settingsSection;
    private View settingsSidebar;
    private Button settingsToggle;
    private final Runnable startupUpdate;
    private boolean startupUpdatePending;
    private TextView status;
    private AppStorage storage;
    private TextView summary;
    private final Handler timer;
    private TmdbService tmdb;
    private TmdbUi tmdbUi;
    private final ExecutorService worker;

    private final class Cards extends BaseAdapter {
        final boolean grid;
        final /* synthetic */ MainActivity this$0;

        public static /* synthetic */ void $r8$lambda$2n8iBkgrpRhBqecSnf_qdszKRV0(Cards r0) {
            r0.lambda$getView$0();
        }

        public static /* synthetic */ void $r8$lambda$EKisBU8x2O1noyLTDCtvBjO3riw(Cards r0) {
            r0.lambda$getView$1();
        }

        Cards(MainActivity r1, boolean r2) {
            this.this$0 = r1;
            this.grid = r2;
        }

        private /* synthetic */ void lambda$getView$0() {
            if (this.this$0.isDestroyed() == false) goto L5;
            return;
        L5:
            if (MainActivity.m609$$Nest$fgetadapter(this.this$0) == null) goto L9;
            MainActivity.m628$$Nest$fgettimer(this.this$0).removeCallbacks(MainActivity.m610$$Nest$fgetartworkRefresh(this.this$0));
            MainActivity.m628$$Nest$fgettimer(this.this$0).postDelayed(MainActivity.m610$$Nest$fgetartworkRefresh(this.this$0), 120);
            return;
        }

        private /* synthetic */ void lambda$getView$1() {
            if (this.this$0.isDestroyed() == false) goto L5;
            return;
        L5:
            if (MainActivity.m609$$Nest$fgetadapter(this.this$0) == null) goto L9;
            MainActivity.m628$$Nest$fgettimer(this.this$0).removeCallbacks(MainActivity.m610$$Nest$fgetartworkRefresh(this.this$0));
            MainActivity.m628$$Nest$fgettimer(this.this$0).postDelayed(MainActivity.m610$$Nest$fgetartworkRefresh(this.this$0), 120);
            return;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return MainActivity.m622$$Nest$fgetrows(this.this$0).size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int r2) {
            return MainActivity.m622$$Nest$fgetrows(this.this$0).get(r2);
        }

        @Override // android.widget.Adapter
        public long getItemId(int r3) {
            return r3;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r24v1, types: [android.view.View] */
        /* JADX WARN: Type inference failed for: r24v2, types: [android.view.View] */
        /* JADX WARN: Type inference failed for: r24v3, types: [android.view.View] */
        @Override // android.widget.Adapter
        public View getView(int r23, View r24, ViewGroup r25) {
            if (r23 >= 0) goto L5;
        L175:
            Log.w("CHDTV_SCROLL", "Invalid catalog position " + r23 + " count " + MainActivity.m622$$Nest$fgetrows(this.this$0).size());
            View r3 = new View(this.this$0);
            r3.setLayoutParams(new AbsListView.LayoutParams(-1, 1));
            return r3;
        L5:
            if (r23 >= MainActivity.m622$$Nest$fgetrows(this.this$0).size()) goto L175;
            String r7 = "  ›";
            String r8 = "★ ";
            if (this.grid == true) goto L10;
            if (r24 != null) goto L112;
            Holder r10 = new Holder(null);
            LinearLayout r12 = new LinearLayout(this.this$0);
            r12.setOrientation(this.grid ? 1 : 0);
            r12.setGravity(16);
            r12.setPadding(MainActivity.m633$$Nest$mdp(this.this$0, 10), MainActivity.m633$$Nest$mdp(this.this$0, 10), MainActivity.m633$$Nest$mdp(this.this$0, 10), MainActivity.m633$$Nest$mdp(this.this$0, 10));
            r12.setBackground(MainActivity.m635$$Nest$mfocus(this.this$0, -1207419803));
            r12.setForeground(PosterTile.selectionOverlay(this.this$0));
            r12.setDescendantFocusability(393216);
            r10.image = new ImageView(this.this$0);
            ImageView r5 = r10.image;
            if (this.grid == false) goto L87;
            ImageView.ScaleType r6 = ImageView.ScaleType.CENTER_CROP;
        L88:
            r5.setScaleType(r6);
            r10.image.setBackground(MainActivity.m640$$Nest$mshape(this.this$0, -16050393, 0));
            ImageView r9 = r10.image;
            if (this.grid == false) goto L91;
            int r11 = -1;
        L93:
            if (this.grid == false) goto L95;
            int r13 = MainActivity.m633$$Nest$mdp(this.this$0, 140);
        L96:
            r12.addView(r9, new LinearLayout.LayoutParams(r11, r13));
            LinearLayout r14 = new LinearLayout(this.this$0);
            r14.setOrientation(1);
            if (this.grid == false) goto L99;
            int r15 = -1;
        L101:
            if (this.grid == false) goto L103;
            float r16 = 0.0f;
        L104:
            LinearLayout.LayoutParams r17 = new LinearLayout.LayoutParams(r15, -2, r16);
            if (this.grid == true) goto L107;
            r17.setMargins(MainActivity.m633$$Nest$mdp(this.this$0, 12), 0, 0, 0);
        L107:
            r12.addView(r14, r17);
            MainActivity r18 = this.this$0;
            if (this.grid == false) goto L110;
            int r19 = 16;
        L111:
            r10.title = MainActivity.m641$$Nest$mtext(r18, "", r19, MainActivity.WHITE);
            r10.title.setTypeface(null, 1);
            r10.title.setMaxLines(2);
            r10.title.setEllipsize(TextUtils.TruncateAt.END);
            r14.addView(r10.title);
            r10.detail = MainActivity.m641$$Nest$mtext(this.this$0, "", 11, MainActivity.MUTED);
            r10.detail.setMaxLines(1);
            r10.detail.setEllipsize(TextUtils.TruncateAt.END);
            r10.detail.setPadding(0, MainActivity.m633$$Nest$mdp(this.this$0, 5), 0, MainActivity.m633$$Nest$mdp(this.this$0, 5));
            r14.addView(r10.detail);
            r10.now = MainActivity.m641$$Nest$mtext(this.this$0, "", 13, MainActivity.GREEN);
            r10.now.setMaxLines(1);
            r10.now.setEllipsize(TextUtils.TruncateAt.END);
            r14.addView(r10.now);
            r10.progress = new ProgressBar(this.this$0, null, android.R.attr.progressBarStyleHorizontal);
            r10.progress.setMax(100);
            r10.progress.setProgressTintList(ColorStateList.valueOf(MainActivity.CYAN));
            r14.addView(r10.progress, new LinearLayout.LayoutParams(-1, MainActivity.m633$$Nest$mdp(this.this$0, 5)));
            r10.next = MainActivity.m641$$Nest$mtext(this.this$0, "", 11, MainActivity.MUTED);
            r10.next.setMaxLines(1);
            r10.next.setEllipsize(TextUtils.TruncateAt.END);
            r10.next.setPadding(0, MainActivity.m633$$Nest$mdp(this.this$0, 5), 0, 0);
            r14.addView(r10.next);
            r12.setTag(r10);
            Object r110 = r12;
        L113:
            Row r2 = (Row) MainActivity.m622$$Nest$fgetrows(this.this$0).get(r23);
            TextView r4 = r10.title;
            StringBuilder r20 = new StringBuilder();
            if (r2.level == 0) goto L116;
        L118:
            r8 = "";
        L119:
            r4.setText(r20.append(r8).append(r2.title).toString());
            TextView r21 = r10.detail;
            StringBuilder r22 = new StringBuilder().append(r2.detail);
            if (r2.level > 0) goto L123;
            r7 = "";
        L123:
            r21.setText(r22.append(r7).toString());
            if (r2.level != 3) goto L126;
            r10.image.setTag(null);
            r10.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            r10.image.setPadding(MainActivity.m633$$Nest$mdp(this.this$0, 12), MainActivity.m633$$Nest$mdp(this.this$0, 12), MainActivity.m633$$Nest$mdp(this.this$0, 12), MainActivity.m633$$Nest$mdp(this.this$0, 12));
            ProviderArtwork.show(r10.image, r2.title);
        L144:
            if (r2.level == 0) goto L146;
        L148:
            boolean r26 = false;
        L149:
            TextView r27 = r10.now;
            if (r26 == false) goto L152;
            int r28 = 0;
        L153:
            r27.setVisibility(r28);
            TextView r29 = r10.next;
            if (r26 == false) goto L156;
            int r30 = 0;
        L157:
            r29.setVisibility(r30);
            r10.progress.setVisibility(8);
            if (r26 == false) goto L174;
            long r31 = System.currentTimeMillis();
            EpgData.Programme[] r32 = MainActivity.m616$$Nest$fgetepg(this.this$0).nowNext(r2.entry, r31);
            TextView r33 = r10.now;
            if (r32[0] != null) goto L162;
            String r34 = "Sin programa actual";
        L163:
            r33.setText(r34);
            TextView r35 = r10.next;
            if (r32[1] != null) goto L166;
            String r36 = "Sin programa siguiente";
        L167:
            r35.setText(r36);
            if (r32[0] == null) goto L178;
            if (r32[0].stop <= r32[0].start) goto L178;
            r10.progress.setVisibility(0);
            ?? r210 = r110;
            r10.progress.setProgress((int) Math.max(0, Math.min(100, ((r31 - r32[0].start) * 100) / (r32[0].stop - r32[0].start))));
            return r210;
        L178:
            return r110;
        L166:
            r36 = MainActivity.m642$$Nest$mtime(this.this$0, r32[1].start) + " · " + r32[1].title;
            goto L167
        L162:
            r34 = "AHORA · " + r32[0].title;
            goto L163
        L174:
            return r110;
        L156:
            r30 = 8;
            goto L157
        L152:
            r28 = 8;
            goto L153
        L146:
            if (r2.entry.kind.equals("Canales") == false) goto L148;
            r26 = true;
            goto L149
        L126:
            r10.image.setPadding(0, 0, 0, 0);
            ImageView r37 = r10.image;
            if (this.grid == true) goto L129;
        L131:
            ImageView.ScaleType r38 = ImageView.ScaleType.FIT_CENTER;
        L132:
            r37.setScaleType(r38);
            String r39 = MainActivity.m629$$Nest$fgettmdb(this.this$0).poster(r2.entry);
            if ("Canales".equals(r2.entry.kind) == false) goto L135;
            MainActivity.m620$$Nest$fgetlogos(this.this$0).show(r10.image, MainActivity.m611$$Nest$fgetcatalogIndex(this.this$0).logos.candidates(r2.entry));
        L137:
            if (MainActivity.m619$$Nest$fgetkind(this.this$0).equals("Canales") == true) goto L144;
            if (MainActivity.m613$$Nest$fgetcatalogVisible(this.this$0) == false) goto L144;
            if (MainActivity.m612$$Nest$fgetcatalogScrolling(this.this$0) == true) goto L144;
            MainActivity.m629$$Nest$fgettmdb(this.this$0).ensure(r2.entry, new MainActivity$Cards$$ExternalSyntheticLambda1(this));
            goto L144
        L135:
            MainActivity.m620$$Nest$fgetlogos(this.this$0).show(r10.image, r39, MainActivity.m611$$Nest$fgetcatalogIndex(this.this$0).logos.resolve(r2.entry));
            goto L137
        L129:
            if ("Canales".equals(r2.entry.kind) == true) goto L131;
            r38 = ImageView.ScaleType.CENTER_CROP;
            goto L132
        L116:
            if (MainActivity.m634$$Nest$mfavorite(this.this$0, r2.entry) == false) goto L118;
        L110:
            r19 = 17;
            goto L111
        L103:
            r16 = 1.0f;
            goto L104
        L99:
            r15 = 0;
            goto L101
        L95:
            r13 = MainActivity.m633$$Nest$mdp(this.this$0, 62);
            goto L96
        L91:
            r11 = MainActivity.m633$$Nest$mdp(this.this$0, 62);
            goto L93
        L87:
            r6 = ImageView.ScaleType.FIT_CENTER;
            goto L88
        L112:
            r10 = (Holder) r24.getTag();
            r110 = r24;
            goto L113
        L10:
            if ((r24 instanceof PosterTile) == false) goto L12;
            PosterTile r40 = (PosterTile) r24;
        L13:
            Row r111 = (Row) MainActivity.m622$$Nest$fgetrows(this.this$0).get(r23);
            r40.channelLayout("Canales".equals(MainActivity.m619$$Nest$fgetkind(this.this$0)));
            if (r111.level != 4) goto L17;
            String r41 = "TU PROGRESO";
        L33:
            if (r111.level == 0) goto L35;
        L46:
            StringBuilder r42 = new StringBuilder();
            if (r111.level == 0) goto L49;
        L51:
            r8 = "";
        L52:
            String r43 = r42.append(r8).append(r111.title).toString();
            StringBuilder r44 = new StringBuilder().append(r111.detail);
            if (r111.level > 0) goto L56;
            r7 = "";
        L56:
            r40.bind(r43, r44.append(r7).toString(), r41);
            if (MainActivity.m615$$Nest$fgetcontinueMode(this.this$0) == false) goto L64;
            WatchHistory.Item r45 = WatchHistory.get(this.this$0, r111.entry);
            if (r45 == null) goto L64;
            if (r45.duration <= 0) goto L64;
            r40.progress.setVisibility(0);
            r40.progress.setProgress(r45.percent());
        L64:
            if (r111.level != 4) goto L67;
            r40.continueArtwork();
        L82:
            return r40;
        L67:
            if (r111.level != 3) goto L69;
            r40.image.setTag(null);
            r40.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            r40.image.setPadding(MainActivity.m633$$Nest$mdp(this.this$0, 20), MainActivity.m633$$Nest$mdp(this.this$0, 20), MainActivity.m633$$Nest$mdp(this.this$0, 20), MainActivity.m633$$Nest$mdp(this.this$0, 20));
            ProviderArtwork.show(r40.image, r111.title);
            goto L82
        L69:
            r40.image.setPadding(0, 0, 0, 0);
            ImageView r46 = r40.image;
            if ("Canales".equals(r111.entry.kind) == false) goto L72;
            ImageView.ScaleType r47 = ImageView.ScaleType.FIT_CENTER;
        L73:
            r46.setScaleType(r47);
            String r48 = MainActivity.m629$$Nest$fgettmdb(this.this$0).poster(r111.entry);
            if ("Canales".equals(r111.entry.kind) == false) goto L76;
            MainActivity.m620$$Nest$fgetlogos(this.this$0).show(r40.image, MainActivity.m611$$Nest$fgetcatalogIndex(this.this$0).logos.candidates(r111.entry));
        L78:
            if (MainActivity.m613$$Nest$fgetcatalogVisible(this.this$0) == false) goto L82;
            if (MainActivity.m612$$Nest$fgetcatalogScrolling(this.this$0) == true) goto L82;
            MainActivity.m629$$Nest$fgettmdb(this.this$0).ensure(r111.entry, new MainActivity$Cards$$ExternalSyntheticLambda0(this));
            goto L82
        L76:
            MainActivity.m620$$Nest$fgetlogos(this.this$0).show(r40.image, r48, MainActivity.m611$$Nest$fgetcatalogIndex(this.this$0).logos.resolve(r111.entry));
            goto L78
        L72:
            r47 = ImageView.ScaleType.CENTER_CROP;
            goto L73
        L49:
            if (MainActivity.m634$$Nest$mfavorite(this.this$0, r111.entry) == false) goto L51;
        L35:
            if (MainActivity.m619$$Nest$fgetkind(this.this$0).equals("Canales") == false) goto L46;
            EpgData.Programme[] r49 = MainActivity.m616$$Nest$fgetepg(this.this$0).nowNext(r111.entry, System.currentTimeMillis());
            String r50 = r111.title;
            if (r49[0] != null) goto L39;
            String r51 = "Sin EPG";
        L40:
            r40.bind(r50, r51, StreamFormat.label(r111.entry));
            r40.image.setPadding(MainActivity.m633$$Nest$mdp(this.this$0, 16), MainActivity.m633$$Nest$mdp(this.this$0, 16), MainActivity.m633$$Nest$mdp(this.this$0, 16), MainActivity.m633$$Nest$mdp(this.this$0, 16));
            r40.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            MainActivity.m620$$Nest$fgetlogos(this.this$0).show(r40.image, MainActivity.m611$$Nest$fgetcatalogIndex(this.this$0).logos.candidates(r111.entry));
            if (r49[0] != null) goto L43;
        L45:
            return r40;
        L43:
            if (r49[0].stop <= r49[0].start) goto L45;
            r40.progress.setVisibility(0);
            r40.progress.setProgress((int) Math.max(0, Math.min(100, ((System.currentTimeMillis() - r49[0].start) * 100) / (r49[0].stop - r49[0].start))));
            goto L45
        L39:
            r51 = r49[0].title;
            goto L40
        L17:
            if (r111.level != 3) goto L20;
            r41 = "PROVEEDOR";
            goto L33
        L20:
            if (r111.level != 1) goto L23;
            r41 = "SERIE";
            goto L33
        L23:
            if (r111.level != 2) goto L26;
            r41 = "TEMPORADA";
            goto L33
        L26:
            if (MainActivity.m619$$Nest$fgetkind(this.this$0).equals("Películas") == false) goto L29;
            r41 = "PELÍCULA";
            goto L33
        L29:
            if (MainActivity.m619$$Nest$fgetkind(this.this$0).equals("Canales") == false) goto L31;
            r41 = StreamFormat.label(r111.entry);
            goto L33
        L31:
            r41 = "EPISODIO";
            goto L33
        L12:
            r40 = new PosterTile(this.this$0);
            goto L13
        }
    }

    private static final class CatalogPlan {
        final List<String> choices;
        final List<Row> rows;
        final String section;

        CatalogPlan(List<Row> r1, List<String> r2, String r3) {
            this.rows = r1;
            this.choices = r2;
            this.section = r3;
        }
    }

    private static final class CatalogRequest {
        final boolean continuing;
        final boolean favorites;
        final String filter;
        final CatalogIndex index;
        final String kind;
        final SharedPreferences preferences;
        Map<String, ?> prefs;
        final String provider;
        final Integer season;
        final String section;
        final String series;

        CatalogRequest(MainActivity r2) {
            this.prefs = Collections.emptyMap();
            this.index = MainActivity.m611$$Nest$fgetcatalogIndex(r2);
            this.kind = MainActivity.m619$$Nest$fgetkind(r2);
            this.provider = MainActivity.m621$$Nest$fgetprovider(r2);
            this.series = MainActivity.m627$$Nest$fgetseries(r2);
            this.section = MainActivity.m625$$Nest$fgetsection(r2);
            this.filter = MainActivity.m618$$Nest$fgetfilter(r2);
            this.season = MainActivity.m624$$Nest$fgetseason(r2);
            this.favorites = MainActivity.m617$$Nest$fgetfavorites(r2);
            this.continuing = MainActivity.m615$$Nest$fgetcontinueMode(r2);
            this.preferences = r2.getPreferences(0);
        }

        Object key() {
            return Arrays.asList(new Object[]{this.kind, this.provider, this.series, this.section, this.filter, this.season, Boolean.valueOf(this.favorites), this.prefs});
        }

        void loadPreferences() {
            Map<String, ?> r0 = this.preferences.getAll();
            if (this.favorites == false) goto L6;
            this.prefs = r0;
            return;
        }

        boolean matches(MainActivity r3) {
            if (this.index == MainActivity.m611$$Nest$fgetcatalogIndex(r3)) goto L5;
        L21:
            return false;
        L5:
            if (Objects.equals(this.kind, MainActivity.m619$$Nest$fgetkind(r3)) == false) goto L21;
            if (Objects.equals(this.provider, MainActivity.m621$$Nest$fgetprovider(r3)) == false) goto L21;
            if (Objects.equals(this.series, MainActivity.m627$$Nest$fgetseries(r3)) == false) goto L21;
            if (Objects.equals(this.section, MainActivity.m625$$Nest$fgetsection(r3)) == false) goto L21;
            if (Objects.equals(this.filter, MainActivity.m618$$Nest$fgetfilter(r3)) == false) goto L21;
            if (Objects.equals(this.season, MainActivity.m624$$Nest$fgetseason(r3)) == false) goto L21;
            if (this.favorites != MainActivity.m617$$Nest$fgetfavorites(r3)) goto L21;
            if (this.continuing != MainActivity.m615$$Nest$fgetcontinueMode(r3)) goto L21;
            return true;
        }
    }

    private static final class Holder {
        TextView detail;
        ImageView image;
        TextView next;
        TextView now;
        ProgressBar progress;
        TextView title;

        private Holder() {
        }

        /* synthetic */ Holder(MainActivityIA r1) {
            this();
        }
    }

    static class LimitedInputStream extends FilterInputStream {
        long count;
        final long limit;

        LimitedInputStream(InputStream r3, long r4) {
            super(r3);
            this.count = 0;
            this.limit = r4;
        }

        private void add(int r7) throws IOException {
            if (r7 <= 0) goto L8;
            long r0 = this.count + ((long) r7);
            this.count = r0;
            if (r0 > this.limit) goto L7;
            return;
        L7:
            throw new IOException("El archivo supera el tamaño máximo permitido (" + ((this.limit / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + " MiB)");
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            int r0 = this.in.read();
            if (r0 >= 0) goto L5;
            int r1 = 0;
        L6:
            add(r1);
            return r0;
        L5:
            r1 = 1;
            goto L6
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] r2, int r3, int r4) throws IOException {
            int r0 = this.in.read(r2, r3, r4);
            add(r0);
            return r0;
        }
    }

    private static final class Row {
        String detail;
        Entry entry;
        int level;
        String target;
        String title;

        Row(String r1, String r2, String r3, Entry r4, int r5) {
            this.title = r1;
            this.detail = r2;
            this.target = r3;
            this.entry = r4;
            this.level = r5;
        }
    }

    public static /* synthetic */ void $r8$lambda$0hPNWxNwqgZJIKLbUsdl0x4YNDQ(MainActivity r0, DialogInterface r1, int r2) {
        r0.lambda$importMenu$48(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$0qiFQiYSz0L-HfNa-wiI-7znxLo, reason: not valid java name */
    public static /* synthetic */ void m584$r8$lambda$0qiFQiYSz0LHfNawiI7znxLo(MainActivity r0, String r1, boolean r2) {
        r0.lambda$openSection$13(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$1wvkNxC5e1GtXSZJHQui02AlWIk(MainActivity r0, Row r1) {
        r0.lambda$open$35(r1);
    }

    public static /* synthetic */ void $r8$lambda$2UGbLmhwWBBmljmGiwEzSBPckiw(MainActivity r0) {
        r0.lambda$refreshVisibleArtwork$4();
    }

    /* JADX INFO: renamed from: $r8$lambda$3J-q8hcHnGvesLO_fQpUdgA2O-w, reason: not valid java name */
    public static /* synthetic */ void m585$r8$lambda$3Jq8hcHnGvesLO_fQpUdgA2Ow(MainActivity r0) {
        r0.lambda$refreshSavedEpg$11();
    }

    /* JADX INFO: renamed from: $r8$lambda$3N4Z0Ysc-1FBwKSkiEHJ5s4bqD8, reason: not valid java name */
    public static /* synthetic */ void m586$r8$lambda$3N4Z0Ysc1FBwKSkiEHJ5s4bqD8(MainActivity r0, Entry r1, DialogInterface r2, int r3) {
        r0.lambda$applyContinue$44(r1, r2, r3);
    }

    /* JADX INFO: renamed from: $r8$lambda$7WKhY5R1eXs9AD-v8JhrDVqx4TM, reason: not valid java name */
    public static /* synthetic */ void m587$r8$lambda$7WKhY5R1eXs9ADv8JhrDVqx4TM(MainActivity r0, Entry r1) {
        r0.lambda$launch$40(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$APwpVWNmj-RnhM0B2x0D1aOcGFA, reason: not valid java name */
    public static /* synthetic */ void m588$r8$lambda$APwpVWNmjRnhM0B2x0D1aOcGFA(MainActivity r0, long r1) {
        r0.lambda$onCreate$7(r1);
    }

    public static /* synthetic */ void $r8$lambda$Ap6Ez07jqeAA8lCnqCEL5M2KMLA(MainActivity r0) {
        r0.lambda$showHome$15();
    }

    public static /* synthetic */ void $r8$lambda$BAJxeOB8aV3jOPRBFdKUHDjsBSs(MainActivity r0) {
        r0.lambda$openChromeRoute$14();
    }

    /* JADX INFO: renamed from: $r8$lambda$C4u83lJ6ZXjs0iJ2Req4vijW-b4, reason: not valid java name */
    public static /* synthetic */ void m589$r8$lambda$C4u83lJ6ZXjs0iJ2Req4vijWb4(MainActivity r0, Entry r1, DialogInterface r2, int r3) {
        r0.lambda$entryMenu$46(r1, r2, r3);
    }

    /* JADX INFO: renamed from: $r8$lambda$CxAonI9jhsGR-bx8inCU7BjzxSI, reason: not valid java name */
    public static /* synthetic */ void m590$r8$lambda$CxAonI9jhsGRbx8inCU7BjzxSI(MainActivity r0, AdapterView r1, View r2, int r3, long r4) {
        r0.lambda$applyContinue$43(r1, r2, r3, r4);
    }

    /* JADX INFO: renamed from: $r8$lambda$D4eH6Qxs0JRxTw5A-7B61x9gvHo, reason: not valid java name */
    public static /* synthetic */ void m591$r8$lambda$D4eH6Qxs0JRxTw5A7B61x9gvHo(MainActivity r0, int r1, CatalogRequest r2, CatalogPlan r3) {
        r0.lambda$renderAsync$30(r1, r2, r3);
    }

    /* JADX INFO: renamed from: $r8$lambda$DbWU3d3gU-NyvP3E9z4JdPxSHlI, reason: not valid java name */
    public static /* synthetic */ void m592$r8$lambda$DbWU3d3gUNyvP3E9z4JdPxSHlI(MainActivity r0) {
        r0.lambda$buildUi$25();
    }

    /* JADX INFO: renamed from: $r8$lambda$E-s5DvL2X_hBWBUjLjSF6C57-AM, reason: not valid java name */
    public static /* synthetic */ void m593$r8$lambda$Es5DvL2X_hBWBUjLjSF6C57AM(MainActivity r0, Catalog r1, DialogInterface r2, int r3) {
        r0.lambda$manageLists$60(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$EqgEVRyOqZJk1DGJzMDw6lYSUNo(MainActivity r0, AdapterView r1, View r2, int r3, long r4) {
        r0.lambda$applyCatalog$33(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$G1HRCEVFetaf4b6MeCbQy0FoQo0(MainActivity r0, AdapterView r1, View r2, int r3, long r4) {
        r0.lambda$showHome$16(r1, r2, r3, r4);
    }

    /* JADX INFO: renamed from: $r8$lambda$G23FzTgn1-VwCqnBj086BUBliQg, reason: not valid java name */
    public static /* synthetic */ void m594$r8$lambda$G23FzTgn1VwCqnBj086BUBliQg(MainActivity r0, String r1, Uri r2, List r3) {
        r0.lambda$loadEpg$70(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$GKZm91AfVyGbwJLhyCta5MCu0rY(MainActivity r0, DialogInterface r1, int r2) {
        r0.lambda$epgMenu$67(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$GQ63gkDiPcg4WQlsEPujhsSPKmY(MainActivity r0) {
        r0.epgMenu();
    }

    public static /* synthetic */ void $r8$lambda$GyQ0CvEVxM0Lb67Hz6FKnyGllss(MainActivity r0) {
        r0.lambda$onCreate$5();
    }

    /* JADX INFO: renamed from: $r8$lambda$HKNyFmAEfHpW-Y5Kdj_3vJQ-0Jg, reason: not valid java name */
    public static /* synthetic */ void m595$r8$lambda$HKNyFmAEfHpWY5Kdj_3vJQ0Jg(MainActivity r0, Entry r1) {
        r0.lambda$play$38(r1);
    }

    public static /* synthetic */ void $r8$lambda$ITYLJkzHEH1h3P2t9cZuuspT1r0(MainActivity r0, Row r1) {
        r0.lambda$openUnlocked$36(r1);
    }

    public static /* synthetic */ void $r8$lambda$JGiroXfz_WgD38K2sf7v8U8vXtM(MainActivity r0, boolean r1, DialogInterface r2, int r3) {
        r0.lambda$choose$49(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$Jpb3ELjyKhjmIJWFSKk0Nt3lUVQ(MainActivity r0, Catalog r1, String r2) {
        r0.lambda$commit$59(r1, r2);
    }

    public static /* synthetic */ boolean $r8$lambda$JzaNs_xLhW0e3NDZ8WiujNoJOks(MainActivity r0, AdapterView r1, View r2, int r3, long r4) {
        return r0.lambda$applyContinue$45(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$LGyAjP0DT46GxaqTNzsTKSd7Paw(MainActivity r0) {
        r0.lambda$showSettingsUnlocked$19();
    }

    public static /* synthetic */ void $r8$lambda$MMoSDsJJ5ahlhkJ92oRiXqnSxDI(MainActivity r0, BatchPlaylist.Result r1, CatalogIndex r2) {
        r0.lambda$loadBatch$56(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$MU5sICFoxgnAAsS5cE1c-Fvlxm0, reason: not valid java name */
    public static /* synthetic */ void m596$r8$lambda$MU5sICFoxgnAAsS5cE1cFvlxm0(MainActivity r0) {
        r0.importMenu();
    }

    public static /* synthetic */ void $r8$lambda$O5pWFCEFEU9T98N4KIh2faSd2cE(MainActivity r0, EditText r1, View r2) {
        r0.lambda$buildUi$26(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$PQ1VsPoHwPZRdv1-EK3cRS1_QTQ, reason: not valid java name */
    public static /* synthetic */ void m597$r8$lambda$PQ1VsPoHwPZRdv1EK3cRS1_QTQ(MainActivity r0, BatchPlaylist.Row r1, int r2, int r3) {
        r0.lambda$loadBatch$55(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$Q7G2ZiKKGmcvJqWzIhwTYPOk5D0(MainActivity r0, String r1) {
        r0.lambda$failure$71(r1);
    }

    public static /* synthetic */ void $r8$lambda$Qr8Mz5pkKMcYbYbEej1kJ7osxf8(MainActivity r0, Entry r1) {
        r0.relatedInfo(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$SOXqQ0H2f5pD-YtWPMwlkX07vZs, reason: not valid java name */
    public static /* synthetic */ void m598$r8$lambda$SOXqQ0H2f5pDYtWPMwlkX07vZs(MainActivity r0) {
        r0.lambda$showSettingsUnlocked$17();
    }

    public static /* synthetic */ void $r8$lambda$Su4LTI1JouhP7iO2zdLmhzhgIrA(MainActivity r0) {
        r0.lambda$showSettingsUnlocked$18();
    }

    public static /* synthetic */ void $r8$lambda$TbshPGpAaYmeTr2FM7lecLiTbMk(MainActivity r0, Catalog.Source r1, DialogInterface r2, int r3) {
        r0.lambda$sourceMenu$64(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$WXO3_uIQGCFsOt8iuYlyjpXYXes(MainActivity r0) {
        r0.lambda$loadSavedEpg$9();
    }

    public static /* synthetic */ void $r8$lambda$X2IBzdGLlYC6yFm7dsf6vAboPGg(MainActivity r0, int r1, int r2, BatchPlaylist.Row r3) {
        r0.lambda$loadBatch$54(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$XEZdDXmzIa2rKbCzmkAyeVpAcTg(MainActivity r0) {
        r0.refreshVisibleArtwork();
    }

    public static /* synthetic */ void $r8$lambda$_M7v2OTnebjwGrONw8CWGbH9rws(MainActivity r0, String[] r1, Catalog r2) {
        r0.lambda$manageLists$61(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$_iYPqxUM1iXf_MOwJ4S5zBtJNNg(MainActivity r0) {
        r0.showSettingsUnlocked();
    }

    public static /* synthetic */ void $r8$lambda$_xykhT6UU80KrtkmSS0YsFLcWR0(MainActivity r0, Entry r1) {
        r0.lambda$play$39(r1);
    }

    public static /* synthetic */ void $r8$lambda$bYD4CKzGJ_2N7WXFFwontTKSWpM(MainActivity r0, Entry r1) {
        r0.play(r1);
    }

    public static /* synthetic */ void $r8$lambda$br7NexeM4iaWQQZYQtzM1gfE48A(MainActivity r0) {
        r0.lambda$showSettingsUnlocked$20();
    }

    /* JADX INFO: renamed from: $r8$lambda$d-DA3HEGLLKp9GptwU8HRxcILXw, reason: not valid java name */
    public static /* synthetic */ List m599$r8$lambda$dDA3HEGLLKp9GptwU8HRxcILXw(MainActivity r0, BatchPlaylist.Row r1) {
        return r0.lambda$loadBatch$53(r1);
    }

    public static /* synthetic */ void $r8$lambda$d1RHvhSjbmpSYJt2M1uRwoCRq2Q(MainActivity r0, Entry r1) {
        r0.lambda$relatedInfo$42(r1);
    }

    public static /* synthetic */ void $r8$lambda$dC1btv_5wTqyRtRfAxhqB_Xe3lA(MainActivity r0) {
        r0.lambda$new$3();
    }

    public static /* synthetic */ void $r8$lambda$dEbeNvWRVs6c2wMJaky8jEwLlak(MainActivity r0, View r1) {
        r0.lambda$buildUi$27(r1);
    }

    public static /* synthetic */ View $r8$lambda$dRcIqPVI3zSoCShW6bQ7HS3UDg8(MainActivity r0) {
        return r0.lambda$buildUi$24();
    }

    public static /* synthetic */ void $r8$lambda$dcJdxO3MR2GymNUKaR9LWi4asyM(MainActivity r0, Uri r1, Catalog r2) {
        r0.lambda$loadBatch$57(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$fV4pFTaK92JplbqsurhbiVXKqFU(MainActivity r0) {
        r0.manageLists();
    }

    public static /* synthetic */ void $r8$lambda$iR77wknENv8q7dIm1fNh9Nx476o(MainActivity r0, Catalog r1, CatalogIndex r2, long r3, long r5, long r7, long r9, String r11) {
        r0.lambda$onCreate$6(r1, r2, r3, r5, r7, r9, r11);
    }

    public static /* synthetic */ InputStream $r8$lambda$iZzBv0ewLu_EBPNFyoUcf7cnEZg(MainActivity r0, String r1, String r2) {
        return r0.lambda$loadEpg$68(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$jjRYgPbdp0td_YbvZJMAtjezWWE(MainActivity r0) {
        r0.lambda$onResume$72();
    }

    /* JADX INFO: renamed from: $r8$lambda$lF8Tbf-k2EWI5GsucOCE-lSPFoI, reason: not valid java name */
    public static /* synthetic */ WindowInsets m600$r8$lambda$lF8Tbfk2EWI5GsucOCElSPFoI(MainActivity r0, View r1, WindowInsets r2) {
        return r0.lambda$landingShell$12(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$nYU3_eXPcM_IU8nD_ScaGQl3rO0(MainActivity r0, Catalog r1, CatalogIndex r2, String r3, Catalog.Source r4, List r5, M3uParser.Result r6) {
        r0.lambda$loadList$51(r1, r2, r3, r4, r5, r6);
    }

    public static /* synthetic */ void $r8$lambda$nbHyaHMHBeurPET7Lx40dQHH30g(MainActivity r0, Entry r1) {
        r0.lambda$relatedInfo$41(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$nfImp7G2V_6_R-MF3W5a2969Z7k, reason: not valid java name */
    public static /* synthetic */ void m601$r8$lambda$nfImp7G2V_6_RMF3W5a2969Z7k(MainActivity r0, DialogInterface r1, int r2) {
        r0.lambda$epgMenu$66(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$o6Htme48c90gHXDYpQse4jTBMTM(MainActivity r0, DialogInterface r1, int r2) {
        r0.lambda$importMenu$47(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$oQ7ehAx9AO62cJqU11X0_abVkUA(MainActivity r0, EditText r1, Catalog.Source r2, DialogInterface r3, int r4) {
        r0.lambda$sourceMenu$63(r1, r2, r3, r4);
    }

    /* JADX INFO: renamed from: $r8$lambda$pvfJKGWCj-ah-xPDAw20fk08MC8, reason: not valid java name */
    public static /* synthetic */ void m602$r8$lambda$pvfJKGWCjahxPDAw20fk08MC8(MainActivity r0, String r1, String r2, String r3, Uri r4, Catalog.Source r5, Catalog r6) {
        r0.lambda$loadList$52(r1, r2, r3, r4, r5, r6);
    }

    /* JADX INFO: renamed from: $r8$lambda$rQibVcjJbe7poP7fL06iU-ILnpE, reason: not valid java name */
    public static /* synthetic */ void m603$r8$lambda$rQibVcjJbe7poP7fL06iUILnpE(MainActivity r0, EpgData r1) {
        r0.lambda$refreshSavedEpg$10(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$s758D8hp0OcF-EmMmgWSbdulmuo, reason: not valid java name */
    public static /* synthetic */ void m604$r8$lambda$s758D8hp0OcFEmMmgWSbdulmuo(MainActivity r0, Catalog r1, CatalogIndex r2, String r3) {
        r0.lambda$commit$58(r1, r2, r3);
    }

    /* JADX INFO: renamed from: $r8$lambda$sMyj8qGro6DFfBGIa-_fBQfpc_w, reason: not valid java name */
    public static /* synthetic */ void m605$r8$lambda$sMyj8qGro6DFfBGIa_fBQfpc_w(MainActivity r0, Row r1) {
        r0.lambda$openUnlocked$37(r1);
    }

    public static /* synthetic */ boolean $r8$lambda$tGl6_dG88VwmXlOD8NSRPkbQ6OE(MainActivity r0, AdapterView r1, View r2, int r3, long r4) {
        return r0.lambda$applyCatalog$34(r1, r2, r3, r4);
    }

    /* JADX INFO: renamed from: $r8$lambda$tymb0HpbO-uN6TzRKKUQdTtGin0, reason: not valid java name */
    public static /* synthetic */ void m606$r8$lambda$tymb0HpbOuN6TzRKKUQdTtGin0(MainActivity r0, Catalog r1) {
        r0.lambda$manageLists$62(r1);
    }

    public static /* synthetic */ void $r8$lambda$u7elZmI_sPQqg7oDmVdfXeqOQYA(MainActivity r0, View r1) {
        r0.lambda$buildUi$23(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$uoGvS_ET77XAI7-WmWPdDSVbh_A, reason: not valid java name */
    public static /* synthetic */ void m607$r8$lambda$uoGvS_ET77XAI7WmWPdDSVbh_A(MainActivity r0, EpgData r1) {
        r0.lambda$loadSavedEpg$8(r1);
    }

    public static /* synthetic */ void $r8$lambda$vKVN6JlZgNAcgXyEZJp011aBysY(MainActivity r0) {
        r0.refreshSavedEpg();
    }

    public static /* synthetic */ void $r8$lambda$xGnsN5EiTZDyNdkC85mwoQRyh1w(MainActivity r0, EditText r1, boolean r2, DialogInterface r3, int r4) {
        r0.lambda$urlDialog$50(r1, r2, r3, r4);
    }

    /* JADX INFO: renamed from: $r8$lambda$xrFW4j45zkkXrpztmWVSMw-wPG4, reason: not valid java name */
    public static /* synthetic */ void m608$r8$lambda$xrFW4j45zkkXrpztmWVSMwwPG4(MainActivity r0, CatalogRequest r1, int r2) {
        r0.lambda$renderAsync$31(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$yQsVrNWcB32zFLKUxoQZKJ72fzU(MainActivity r0, EpgData r1, String r2, Uri r3, List r4) {
        r0.lambda$loadEpg$69(r1, r2, r3, r4);
    }

    public static /* synthetic */ WindowInsets $r8$lambda$yezR_FraSQXVMrea8rUm7719CBA(MainActivity r0, View r1, WindowInsets r2) {
        return r0.lambda$buildUi$22(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$zJPzHMgLzaFHlGVBKh0VlMv7bLo(MainActivity r0, boolean r1, Catalog.Source r2, DialogInterface r3, int r4) {
        r0.lambda$sourceMenu$65(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$zkaSCtr0b8t5hrD63NQB8HZLaWY(MainActivity r0) {
        r0.lambda$new$2();
    }

    /* JADX INFO: renamed from: -$$Nest$fgetadapter, reason: not valid java name */
    static /* bridge */ /* synthetic */ Cards m609$$Nest$fgetadapter(MainActivity r0) {
        return r0.adapter;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetartworkRefresh, reason: not valid java name */
    static /* bridge */ /* synthetic */ Runnable m610$$Nest$fgetartworkRefresh(MainActivity r0) {
        return r0.artworkRefresh;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetcatalogIndex, reason: not valid java name */
    static /* bridge */ /* synthetic */ CatalogIndex m611$$Nest$fgetcatalogIndex(MainActivity r0) {
        return r0.catalogIndex;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetcatalogScrolling, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m612$$Nest$fgetcatalogScrolling(MainActivity r0) {
        return r0.catalogScrolling;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetcatalogVisible, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m613$$Nest$fgetcatalogVisible(MainActivity r0) {
        return r0.catalogVisible;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetcineHero, reason: not valid java name */
    static /* bridge */ /* synthetic */ CineHero m614$$Nest$fgetcineHero(MainActivity r0) {
        return r0.cineHero;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetcontinueMode, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m615$$Nest$fgetcontinueMode(MainActivity r0) {
        return r0.continueMode;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetepg, reason: not valid java name */
    static /* bridge */ /* synthetic */ EpgData m616$$Nest$fgetepg(MainActivity r0) {
        return r0.epg;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetfavorites, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m617$$Nest$fgetfavorites(MainActivity r0) {
        return r0.favorites;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetfilter, reason: not valid java name */
    static /* bridge */ /* synthetic */ String m618$$Nest$fgetfilter(MainActivity r0) {
        return r0.filter;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetkind, reason: not valid java name */
    static /* bridge */ /* synthetic */ String m619$$Nest$fgetkind(MainActivity r0) {
        return r0.kind;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetlogos, reason: not valid java name */
    static /* bridge */ /* synthetic */ LogoLoader m620$$Nest$fgetlogos(MainActivity r0) {
        return r0.logos;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetprovider, reason: not valid java name */
    static /* bridge */ /* synthetic */ String m621$$Nest$fgetprovider(MainActivity r0) {
        return r0.provider;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetrows, reason: not valid java name */
    static /* bridge */ /* synthetic */ List m622$$Nest$fgetrows(MainActivity r0) {
        return r0.rows;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetsearchRender, reason: not valid java name */
    static /* bridge */ /* synthetic */ Runnable m623$$Nest$fgetsearchRender(MainActivity r0) {
        return r0.searchRender;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetseason, reason: not valid java name */
    static /* bridge */ /* synthetic */ Integer m624$$Nest$fgetseason(MainActivity r0) {
        return r0.season;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetsection, reason: not valid java name */
    static /* bridge */ /* synthetic */ String m625$$Nest$fgetsection(MainActivity r0) {
        return r0.section;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetsectionUpdating, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m626$$Nest$fgetsectionUpdating(MainActivity r0) {
        return r0.sectionUpdating;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetseries, reason: not valid java name */
    static /* bridge */ /* synthetic */ String m627$$Nest$fgetseries(MainActivity r0) {
        return r0.series;
    }

    /* JADX INFO: renamed from: -$$Nest$fgettimer, reason: not valid java name */
    static /* bridge */ /* synthetic */ Handler m628$$Nest$fgettimer(MainActivity r0) {
        return r0.timer;
    }

    /* JADX INFO: renamed from: -$$Nest$fgettmdb, reason: not valid java name */
    static /* bridge */ /* synthetic */ TmdbService m629$$Nest$fgettmdb(MainActivity r0) {
        return r0.tmdb;
    }

    /* JADX INFO: renamed from: -$$Nest$fputcatalogScrolling, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m630$$Nest$fputcatalogScrolling(MainActivity r0, boolean r1) {
        r0.catalogScrolling = r1;
    }

    /* JADX INFO: renamed from: -$$Nest$fputfilter, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m631$$Nest$fputfilter(MainActivity r0, String r1) {
        r0.filter = r1;
    }

    /* JADX INFO: renamed from: -$$Nest$fputsection, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m632$$Nest$fputsection(MainActivity r0, String r1) {
        r0.section = r1;
    }

    /* JADX INFO: renamed from: -$$Nest$mdp, reason: not valid java name */
    static /* bridge */ /* synthetic */ int m633$$Nest$mdp(MainActivity r0, int r1) {
        return r0.m402dp(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$mfavorite, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m634$$Nest$mfavorite(MainActivity r0, Entry r1) {
        return r0.favorite(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$mfocus, reason: not valid java name */
    static /* bridge */ /* synthetic */ Drawable m635$$Nest$mfocus(MainActivity r0, int r1) {
        return r0.focus(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$mpreviewRow, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m636$$Nest$mpreviewRow(MainActivity r0, int r1) {
        r0.previewRow(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$mrefreshSavedEpg, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m637$$Nest$mrefreshSavedEpg(MainActivity r0) {
        r0.refreshSavedEpg();
    }

    /* JADX INFO: renamed from: -$$Nest$mrefreshVisibleEpg, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m638$$Nest$mrefreshVisibleEpg(MainActivity r0) {
        r0.refreshVisibleEpg();
    }

    /* JADX INFO: renamed from: -$$Nest$mrenderAsync, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m639$$Nest$mrenderAsync(MainActivity r0) {
        r0.renderAsync();
    }

    /* JADX INFO: renamed from: -$$Nest$mshape, reason: not valid java name */
    static /* bridge */ /* synthetic */ GradientDrawable m640$$Nest$mshape(MainActivity r0, int r1, int r2) {
        return r0.shape(r1, r2);
    }

    /* JADX INFO: renamed from: -$$Nest$mtext, reason: not valid java name */
    static /* bridge */ /* synthetic */ TextView m641$$Nest$mtext(MainActivity r0, String r1, int r2, int r3) {
        return r0.text(r1, r2, r3);
    }

    /* JADX INFO: renamed from: -$$Nest$mtime, reason: not valid java name */
    static /* bridge */ /* synthetic */ String m642$$Nest$mtime(MainActivity r0, long r1) {
        return r0.time(r1);
    }

    public MainActivity() {
        this.worker = Executors.newSingleThreadExecutor(backgroundThreads("CHDTVCatalogIO"));
        this.timer = new Handler(Looper.getMainLooper());
        this.catalog = new Catalog();
        this.catalogIndex = new CatalogIndex(this.catalog);
        this.entries = new ArrayList();
        this.epg = new EpgData();
        this.previewActivation = new PreviewActivation();
        this.continueMode = false;
        this.catalogVisible = true;
        this.catalogScrolling = false;
        this.playbackLaunching = false;
        this.epgWorker = Executors.newSingleThreadExecutor(backgroundThreads("CHDTVGuide"));
        this.page = "home";
        this.provider = null;
        this.providers = new IdentityHashMap();
        this.kind = "Canales";
        this.filter = "";
        this.series = null;
        this.section = "Todas las secciones";
        this.importKind = "Canales";
        this.season = null;
        this.favorites = false;
        this.busy = true;
        this.sectionUpdating = false;
        this.guide = false;
        this.rows = new DemandRows();
        this.searchRender = new MainActivity$$ExternalSyntheticLambda19(this);
        this.startupUpdate = new MainActivity$$ExternalSyntheticLambda20(this);
        this.artworkRefresh = new MainActivity$$ExternalSyntheticLambda22(this);
        this.refresh = new RunnableC10001(this);
        this.guideRefresh = new MainActivity$$ExternalSyntheticLambda23(this);
        this.settingsSection = 8;
        this.catalogWorker = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, new ArrayBlockingQueue(1), new MainActivity$$ExternalSyntheticLambda24(), new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    private void addButton(LinearLayout r5, Button r6) {
        LinearLayout.LayoutParams r0 = new LinearLayout.LayoutParams(0, m402dp(44), 1.0f);
        r0.setMargins(m402dp(3), 0, m402dp(3), 0);
        r5.addView(r6, r0);
    }

    private void applyCatalog(CatalogPlan r13) {
        requestGuideForSection();
        this.previewActivation.reset();
        this.rows.clear();
        this.rows.addAll(r13.rows);
        final List<String> r0 = r13.choices;
        this.section = r13.section;
        int r1 = 1;
        this.sectionUpdating = true;
        if (r0.equals(this.sectionChoices) == true) goto L6;
        this.sectionChoices = new ArrayList(r0);
        final int r3 = android.R.layout.simple_spinner_item;
        ArrayAdapter<String> r2 = new C10067(this, this, r3, r0);
        r2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.sections.setAdapter(r2);
    L6:
        if (this.sections.getSelectedItemPosition() == r0.indexOf(this.section)) goto L8;
        this.sections.setSelection(r0.indexOf(this.section));
    L8:
        this.sectionUpdating = false;
        Spinner r4 = this.sections;
        if (this.provider != null) goto L11;
        int r5 = 8;
    L12:
        r4.setVisibility(r5);
        TextView r6 = this.heading;
        String r7 = "";
        if (this.series == null) goto L15;
        StringBuilder r8 = new StringBuilder().append(this.series);
        if (this.season != null) goto L24;
        String r9 = "";
    L25:
        String r10 = r8.append(r9).toString();
    L26:
        r6.setText(r10);
        StringBuilder r11 = new StringBuilder().append(this.kind);
        if (this.provider != null) goto L29;
        String r12 = "";
    L30:
        StringBuilder r14 = r11.append(r12);
        if (this.series != null) goto L33;
        String r15 = "";
    L34:
        setChromeTitle(r14.append(r15).toString());
        this.heading.setMaxLines(1);
        this.heading.setEllipsize(TextUtils.TruncateAt.END);
        this.back.setVisibility(0);
        Button r16 = this.favoriteButton;
        if (this.favorites == false) goto L37;
        String r17 = "★";
    L38:
        r16.setText(r17);
        Button r18 = this.favoriteButton;
        if (this.favorites == false) goto L41;
        int r19 = ORANGE;
    L42:
        r18.setTextColor(r19);
        Button r20 = this.favoriteButton;
        if (this.favorites == false) goto L45;
        String r21 = "Mostrar todo";
    L46:
        r20.setContentDescription(r21);
        StringBuilder r22 = new StringBuilder();
        int r23 = this.rows.size();
        int r110 = 4;
        if (this.rows.isEmpty() == false) goto L49;
    L52:
        int r24 = 0;
    L53:
        StringBuilder r25 = r22.append(r23 - r24).append(" ");
        if (this.provider != null) goto L57;
        String r26 = "proveedores";
    L70:
        StringBuilder r27 = r25.append(r26);
        if (this.favorites == false) goto L73;
        String r28 = " · favoritos";
    L74:
        StringBuilder r29 = r27.append(r28);
        if (this.kind.equals("Canales") == false) goto L81;
        StringBuilder r30 = new StringBuilder().append(" · ");
        if (this.epg.count() != 0) goto L79;
        String r31 = "Añade tu EPG";
    L80:
        r7 = r30.append(r31).toString();
        goto L81
    L79:
        r31 = "EPG cargada";
    L81:
        message(r29.append(r7).toString(), MUTED);
        if (AppTheme.cine(this) == true) goto L84;
    L86:
        boolean r32 = false;
    L88:
        if (this.cineHero == null) goto L90;
        boolean r33 = true;
    L91:
        if (r32 == r33) goto L93;
    L109:
        boolean r34 = false;
    L110:
        if (r34 == true) goto L112;
    L113:
        this.content.removeAllViews();
    L115:
        if (this.rows.isEmpty() == false) goto L127;
        LinearLayout r35 = new LinearLayout(this);
        r35.setGravity(17);
        r35.setOrientation(1);
        TextView r36 = text("▷", 62, CYAN);
        r36.setGravity(17);
        r35.addView(r36);
        if (this.entries.isEmpty() == false) goto L119;
        String r37 = "Tu próxima sesión empieza aquí";
    L120:
        TextView r38 = text(r37, 20, WHITE);
        r38.setGravity(17);
        r35.addView(r38);
        if (this.entries.isEmpty() == false) goto L123;
        String r39 = "Añade un M3U desde archivo o URL";
    L124:
        TextView r40 = text(r39, 13, MUTED);
        r40.setGravity(17);
        r35.addView(r40);
        this.content.addView(r35, new LinearLayout.LayoutParams(-1, -1));
        return;
    L123:
        r39 = "Prueba otra búsqueda o sección";
        goto L124
    L119:
        r37 = "Sin resultados";
        goto L120
    L127:
        if (AppTheme.cine(this) == false) goto L129;
    L132:
        boolean r41 = true;
    L133:
        if (r34 == false) goto L141;
        this.adapter.notifyDataSetChanged();
        if (this.cineHero != null) goto L137;
        return;
    L137:
        if (this.rows.isEmpty() == true) goto L160;
        previewRow(Math.max(0, Math.min(this.rows.size() - 1, this.list.getSelectedItemPosition())));
        return;
    L160:
        return;
    L141:
        if (r41 == false) goto L150;
        ResponsiveCatalogGrid r42 = new ResponsiveCatalogGrid(this);
        r42.setChannelCatalog("Canales".equals(this.kind));
        float r43 = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density;
        if (r43 < 1000.0f) goto L146;
        r110 = 6;
    L149:
        r42.setNumColumns(r110);
        r42.setColumnWidth(m402dp(170));
        r42.setStretchMode(2);
        r42.setHorizontalSpacing(m402dp(10));
        r42.setVerticalSpacing(m402dp(10));
        this.list = r42;
    L152:
        if (AppTheme.cineStage(this) == false) goto L154;
        this.list.setChoiceMode(1);
    L154:
        this.list.setClipToPadding(false);
        this.list.setPadding(0, m402dp(2), 0, m402dp(8));
        this.adapter = new Cards(this, r41);
        this.list.setAdapter(this.adapter);
        this.list.setOnItemClickListener(new MainActivity$$ExternalSyntheticLambda34(this));
        this.list.setOnItemLongClickListener(new MainActivity$$ExternalSyntheticLambda35(this));
        attachCatalogList(this.list);
        AbsListView r44 = this.list;
        if (r41 == false) goto L157;
        r1 = ((GridView) this.list).getNumColumns();
    L157:
        CatalogNavigation.bind(r44, r1);
        return;
    L146:
        if (r43 >= 600.0f) goto L149;
        r110 = 2;
        goto L149
    L150:
        ListView r45 = new ListView(this);
        r45.setDivider(null);
        r45.setDividerHeight(m402dp(8));
        this.list = r45;
        goto L152
    L129:
        if (this.kind.equals("Canales") == false) goto L132;
        r41 = false;
        goto L133
    L112:
        if (this.rows.isEmpty() == false) goto L115;
    L93:
        if (this.list == null) goto L109;
        if (this.list.getParent() != this.content) goto L109;
        if (this.adapter == null) goto L109;
        boolean r46 = this.adapter.grid;
        if (AppTheme.cine(this) == false) goto L101;
    L104:
        boolean r47 = true;
    L105:
        if (r46 != r47) goto L109;
        if ("continue".equals(this.list.getTag()) == true) goto L109;
        r34 = true;
        goto L110
    L101:
        if (this.kind.equals("Canales") == false) goto L104;
        r47 = false;
        goto L105
    L90:
        r33 = false;
        goto L91
    L84:
        if (this.provider == null) goto L86;
        r32 = true;
        goto L88
    L73:
        r28 = "";
        goto L74
    L57:
        if (this.series != null) goto L62;
        if (this.kind.equals("Series") == false) goto L62;
        r26 = "series";
    L62:
        if (this.kind.equals("Series") == false) goto L67;
        if (this.season != null) goto L67;
        r26 = "temporadas";
    L67:
        if (this.kind.equals("Series") == false) goto L69;
        r26 = "episodios";
        goto L70
    L69:
        r26 = "elementos";
        goto L70
    L49:
        if (this.rows.get(0).level != 4) goto L52;
        r24 = 1;
        goto L53
    L45:
        r21 = "Mostrar favoritos";
        goto L46
    L41:
        r19 = WHITE;
        goto L42
    L37:
        r17 = "☆";
        goto L38
    L33:
        r15 = " · " + this.series;
        goto L34
    L29:
        r12 = " · " + Providers.base(this.provider);
        goto L30
    L24:
        r9 = " · S" + this.season;
        goto L25
    L15:
        if (this.provider == null) goto L18;
        r10 = this.provider;
        goto L26
    L18:
        if (this.guide == false) goto L20;
        r10 = "Guía de televisión";
        goto L26
    L20:
        r10 = this.kind;
        goto L26
    L11:
        r5 = 0;
        goto L12
    }

    private void applyContinue(CatalogPlan r6) {
        this.previewActivation.reset();
        this.rows.clear();
        this.rows.addAll(r6.rows);
        this.sections.setVisibility(8);
        this.heading.setText(this.kind + " · Seguir viendo");
        setChromeTitle(this.kind + " · " + Providers.base(this.provider) + " · Seguir viendo");
        Button r0 = this.favoriteButton;
        if (this.favorites == false) goto L5;
        String r1 = "★";
    L6:
        r0.setText(r1);
        message(this.rows.size() + " títulos pendientes", MUTED);
        if (this.rows.isEmpty() == false) goto L9;
    L18:
        this.content.removeAllViews();
        if (this.rows.isEmpty() == false) goto L22;
        TextView r2 = text("Aún no tienes contenido pendiente de terminar", 17, MUTED);
        r2.setPadding(m402dp(16), m402dp(24), m402dp(16), m402dp(24));
        this.content.addView(r2);
        return;
    L22:
        ResponsiveCatalogGrid r3 = new ResponsiveCatalogGrid(this);
        float r4 = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density;
        if (r4 < 1000.0f) goto L26;
        int r5 = 6;
    L29:
        r3.setNumColumns(r5);
        r3.setHorizontalSpacing(m402dp(10));
        r3.setVerticalSpacing(m402dp(10));
        this.adapter = new Cards(this, true);
        r3.setAdapter(this.adapter);
        if (AppTheme.cineStage(this) == false) goto L32;
        r3.setChoiceMode(1);
    L32:
        r3.setOnItemClickListener(new MainActivity$$ExternalSyntheticLambda21(this));
        r3.setOnItemLongClickListener(new MainActivity$$ExternalSyntheticLambda32(this));
        r3.setTag("continue");
        this.list = r3;
        attachCatalogList(r3);
        CatalogNavigation.bind(r3, r3.getNumColumns());
        return;
    L26:
        if (r4 < 600.0f) goto L28;
        r5 = 4;
        goto L29
    L28:
        r5 = 2;
        goto L29
    L9:
        if (this.list == null) goto L18;
        if ("continue".equals(this.list.getTag()) == false) goto L18;
        if (this.list.getParent() != this.content) goto L18;
        if (this.adapter == null) goto L18;
        Parcelable r7 = this.list.onSaveInstanceState();
        this.adapter.notifyDataSetChanged();
        this.list.onRestoreInstanceState(r7);
        return;
    L5:
        r1 = "☆";
        goto L6
    }

    private void attachCatalogList(AbsListView r6) {
        this.catalogScrolling = false;
        r6.setOnScrollListener(new C10012(this));
        this.cineHero = null;
        if (AppTheme.cine(this) == true) goto L5;
    L9:
        this.content.addView(r6, new LinearLayout.LayoutParams(-1, -1));
        return;
    L5:
        if (this.provider == null) goto L9;
        if (this.rows.isEmpty() == true) goto L9;
        this.cineHero = new CineHero(this, this.tmdb, this.logos);
        this.content.addView(this.cineHero, CineHero.previewLayout(this));
        this.content.addView(r6, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        previewRow(0);
        r6.setOnItemSelectedListener(new C10023(this));
    }

    private static ThreadFactory backgroundThreads(final String r1) {
        return new MainActivity$$ExternalSyntheticLambda74(r1);
    }

    private void buildUi() {
        this.sectionChoices = null;
        this.root = new LinearLayout(this);
        this.root.setOrientation(1);
        this.root.setBackground(new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{0, 0}));
        this.root.setPadding(m402dp(14), m402dp(12), m402dp(14), m402dp(8));
        setContentView(this.root);
        this.root.setOnApplyWindowInsetsListener(new MainActivity$$ExternalSyntheticLambda8(this));
        LinearLayout r1 = horizontal();
        r1.setGravity(16);
        this.back = button("‹", new MainActivity$$ExternalSyntheticLambda9(this));
        this.back.setTextSize(28.0f);
        this.back.setContentDescription("Volver");
        this.heading = text(this.kind, 18, WHITE);
        this.heading.setTypeface(null, 1);
        this.heading.setPadding(m402dp(10), 0, m402dp(8), 0);
        r1.addView(this.heading, new LinearLayout.LayoutParams(0, m402dp(48), 1.0f));
        final EditText r0 = new EditText(this);
        this.catalogSearch = r0;
        SearchKeyboard.bind(this, r0, new MainActivity$$ExternalSyntheticLambda11(this), new MainActivity$$ExternalSyntheticLambda12(this));
        r0.setSingleLine();
        r0.setTextSize(14.0f);
        r0.setTextColor(WHITE);
        r0.setHintTextColor(MUTED);
        r0.setHint("Buscar canal, película o serie");
        r0.setPadding(m402dp(14), 0, m402dp(14), 0);
        r0.setBackground(shape(-15720913, -14206127));
        r0.setText(this.filter);
        if (this.filter.isEmpty() == false) goto L5;
        int r3 = 8;
    L6:
        r0.setVisibility(r3);
        Button r4 = button("⌕", new MainActivity$$ExternalSyntheticLambda13(this, r0));
        r4.setTextSize(26.0f);
        r4.setContentDescription("Buscar");
        this.favoriteButton = button("☆", new MainActivity$$ExternalSyntheticLambda14(this));
        this.favoriteButton.setTextSize(24.0f);
        this.favoriteButton.setContentDescription("Mostrar favoritos");
        setChromeActions(this.back, this.favoriteButton, r4);
        this.root.addView(r0, new LinearLayout.LayoutParams(-1, m402dp(44)));
        r0.addTextChangedListener(new C10045(this));
        this.sections = new Spinner(this);
        this.sections.setBackground(shape(CARD, -14206127));
        this.sections.setVisibility(8);
        this.sections.setOnItemSelectedListener(new C10056(this));
        this.status = text("Cargando tu catálogo…", 12, MUTED);
        this.status.setPadding(m402dp(2), m402dp(8), 0, m402dp(8));
        this.root.addView(this.status);
        this.content = new LinearLayout(this);
        this.content.setOrientation(1);
        this.root.addView(this.content, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        return;
    L5:
        r3 = 0;
        goto L6
    }

    private Button button(String r6, View.OnClickListener r7) {
        Button r0 = new Button(this);
        r0.setText(r6);
        r0.setTextSize(12.0f);
        r0.setAllCaps(false);
        r0.setTextColor(WHITE);
        r0.setMinHeight(0);
        r0.setMinimumHeight(0);
        r0.setPadding(m402dp(6), m402dp(6), m402dp(6), m402dp(6));
        r0.setBackground(focus(CARD));
        r0.setOnClickListener(r7);
        return r0;
    }

    private void choose(final boolean r5) {
        new AlertDialog.Builder(this).setTitle("Tipo de lista").setItems(new String[]{"Canales", "Películas", "Series"}, new MainActivity$$ExternalSyntheticLambda69(this, r5)).show();
    }

    private void commit(final Catalog r3, final String r4) {
        if (this.busy == false) goto L5;
        return;
    L5:
        this.busy = true;
        message("Guardando cambios…", ORANGE);
        this.worker.execute(new MainActivity$$ExternalSyntheticLambda77(this, r3, r4));
    }

    /* JADX INFO: renamed from: dp */
    private int m402dp(int r3) {
        return Math.round(r3 * getResources().getDisplayMetrics().density);
    }

    private void entryMenu(final Entry r6) {
        String r2 = "Quitar favorito";
        if (r6.kind.equals("Canales") == false) goto L10;
        if (favorite(r6) == true) goto L8;
        r2 = "Añadir favorito";
    L8:
        String r0 = "Programación";
    L14:
        String[] r1 = {"Reproducir", r2, r0, "Grabar / programar"};
        new AlertDialog.Builder(this).setTitle(r6.title).setItems(r1, new MainActivity$$ExternalSyntheticLambda54(this, r6)).show();
        return;
    L10:
        if (favorite(r6) == true) goto L13;
        r2 = "Añadir favorito";
    L13:
        r0 = "Información TMDB";
        goto L14
    }

    private void epgMenu() {
        if (this.busy == false) goto L5;
        return;
    L5:
        new AlertDialog.Builder(this).setTitle("Guía de televisión · " + this.epg.count() + " programas").setItems((CharSequence[]) new ArrayList(Arrays.asList(new String[]{"Cargar archivo XML / XML.GZ", "Cargar URL XMLTV", "Ver guía de canales", "Actualizar EPG", "Eliminar EPG"})).toArray(new String[0]), new MainActivity$$ExternalSyntheticLambda66(this)).show();
    }

    private void failure(String r4, Exception r5) {
        String r0 = r5.getMessage();
        if (r0 != null) goto L5;
    L10:
        r0 = "revisa el archivo o la conexión";
    L11:
        final String r1 = r4 + ": " + r0;
        runOnUiThread(new MainActivity$$ExternalSyntheticLambda53(this, r1));
        return;
    L5:
        if (r0.length() > 140) goto L10;
        if (r0.contains("http") == true) goto L10;
        if (r0.contains("content:") == false) goto L11;
        goto L10
    }

    private boolean favorite(Entry r4) {
        return getPreferences(0).getBoolean(r4.identity(), false);
    }

    private void filePicker(int r3) {
        Intent r0 = new Intent("android.intent.action.OPEN_DOCUMENT");
        r0.setType("*/*");
        r0.addCategory("android.intent.category.OPENABLE");
        r0.addFlags(65);
        startActivityForResult(r0, r3);
    }

    private Drawable focus(int r6) {
        StateListDrawable r0 = new StateListDrawable();
        r0.addState(new int[]{android.R.attr.state_pressed}, shape(-14334631, CYAN));
        r0.addState(new int[]{android.R.attr.state_focused}, shape(-14334631, CYAN));
        r0.addState(new int[]{android.R.attr.state_selected}, shape(-14334631, CYAN));
        r0.addState(new int[0], shape(r6, -14534835));
        return r0;
    }

    private boolean guideSectionVisible() {
        if (this.catalogVisible == true) goto L5;
    L9:
        return false;
    L5:
        if (this.page.equals("catalog") == false) goto L9;
        if (this.kind.equals("Canales") == false) goto L9;
        return true;
    }

    private Drawable homeCard(int r7) {
        GradientDrawable r0 = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{AppTheme.color(this, -15444094), AppTheme.color(this, -16245971)});
        r0.setCornerRadius(m402dp(18));
        r0.setStroke(m402dp(1), AppTheme.color(this, -13460261));
        StateListDrawable r1 = new StateListDrawable();
        r1.addState(new int[]{android.R.attr.state_focused}, shape(-15378059, CYAN));
        r1.addState(new int[]{android.R.attr.state_pressed}, shape(-15378059, CYAN));
        r1.addState(new int[0], r0);
        return r1;
    }

    private LinearLayout horizontal() {
        LinearLayout r0 = new LinearLayout(this);
        r0.setOrientation(0);
        return r0;
    }

    private void importMenu() {
        if (this.busy == false) goto L6;
        message("Espera a que termine la operación actual", ORANGE);
        return;
    L6:
        new AlertDialog.Builder(this).setTitle("Añadir listas").setItems(new String[]{"Desde archivo", "Desde URL", "Desde TXT · varias listas"}, new MainActivity$$ExternalSyntheticLambda30(this)).show();
    }

    private /* synthetic */ void lambda$applyCatalog$33(AdapterView r2, View r3, int r4, long r5) {
        if (r4 >= 0) goto L4;
        return;
    L4:
        if (r4 < this.rows.size()) goto L6;
        return;
    L6:
        if (previewBeforeOpen(r4) == true) goto L12;
        open(this.rows.get(r4));
        return;
    }

    private /* synthetic */ boolean lambda$applyCatalog$34(AdapterView r4, View r5, int r6, long r7) {
        if (r6 >= 0) goto L4;
        return false;
    L4:
        if (r6 >= this.rows.size()) goto L25;
        Row r0 = this.rows.get(r6);
        if (r0.level != 4) goto L10;
        open(r0);
        return true;
    L10:
        if (r0.level != 0) goto L13;
        entryMenu(r0.entry);
        return true;
    L13:
        if (r0.level != 3) goto L15;
        return true;
    L15:
        if (this.kind.equals("Series") == false) goto L24;
        this.tmdbUi.info(r0.entry);
        return true;
    L24:
        return true;
    L25:
        return false;
    }

    private /* synthetic */ void lambda$applyContinue$43(AdapterView r2, View r3, int r4, long r5) {
        if (r4 >= 0) goto L4;
        return;
    L4:
        if (r4 < this.rows.size()) goto L6;
        return;
    L6:
        if (previewBeforeOpen(r4) == true) goto L12;
        play(this.rows.get(r4).entry);
        return;
    }

    private /* synthetic */ void lambda$applyContinue$44(Entry r2, DialogInterface r3, int r4) {
        if (r4 != 0) goto L4;
    L3:
        launch(r2);
        return;
    L4:
        WatchHistory.remove(this, r2);
        if (r4 == 1) goto L3;
        renderAsync();
    }

    private /* synthetic */ boolean lambda$applyContinue$45(AdapterView r6, View r7, int r8, long r9) {
        if (r8 >= 0) goto L4;
        return false;
    L4:
        if (r8 >= this.rows.size()) goto L10;
        final Entry r0 = this.rows.get(r8).entry;
        new AlertDialog.Builder(this).setTitle(r0.title).setItems(new String[]{"Reanudar", "Desde el principio", "Quitar de Seguir viendo"}, new MainActivity$$ExternalSyntheticLambda3(this, r0)).show();
        return true;
    L10:
        return false;
    }

    static /* synthetic */ void lambda$backgroundThreads$0(String r1, Runnable r2) {
        if ("CHDTVCatalogIO".equals(r1) == true) goto L5;
        Process.setThreadPriority(10);
    L5:
        r2.run();
    }

    static /* synthetic */ Thread lambda$backgroundThreads$1(final String r2, final Runnable r3) {
        return new Thread(new MainActivity$$ExternalSyntheticLambda64(r2, r3), r2);
    }

    private /* synthetic */ WindowInsets lambda$buildUi$22(View r6, WindowInsets r7) {
        r6.setPadding(m402dp(14) + r7.getSystemWindowInsetLeft(), m402dp(12) + r7.getSystemWindowInsetTop(), m402dp(14) + r7.getSystemWindowInsetRight(), m402dp(8) + r7.getSystemWindowInsetBottom());
        return r7;
    }

    private /* synthetic */ void lambda$buildUi$23(View r1) {
        navigateBack();
    }

    private /* synthetic */ View lambda$buildUi$24() {
        return this.list;
    }

    private /* synthetic */ void lambda$buildUi$25() {
        this.timer.removeCallbacks(this.searchRender);
        renderAsync();
    }

    private /* synthetic */ void lambda$buildUi$26(EditText r5, View r6) {
        if (r5.getVisibility() == 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L8;
        int r3 = 0;
    L9:
        r5.setVisibility(r3);
        if (r0 == false) goto L12;
        r5.requestFocus();
        ((InputMethodManager) getSystemService("input_method")).showSoftInput(r5, 1);
        return;
    L12:
        r5.setText("");
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(r5.getWindowToken(), 0);
        return;
    L8:
        r3 = 8;
        goto L9
    L5:
        r0 = false;
        goto L6
    }

    private /* synthetic */ void lambda$buildUi$27(View r2) {
        this.favorites = !this.favorites;
        renderAsync();
    }

    private /* synthetic */ void lambda$choose$49(boolean r4, DialogInterface r5, int r6) {
        this.importKind = new String[]{"Canales", "Películas", "Series"}[r6];
        if (r4 == false) goto L5;
        urlDialog(false);
        return;
    L5:
        filePicker(10);
    }

    private /* synthetic */ void lambda$commit$58(Catalog r2, CatalogIndex r3, String r4) {
        this.catalog = r2;
        this.catalogIndex = r3;
        this.entries = r3.entries;
        this.providers = r3.providers;
        this.busy = false;
        this.series = null;
        this.season = null;
        renderAsync();
        message(r4, GREEN);
    }

    private /* synthetic */ void lambda$commit$59(final Catalog r3, final String r4) {
        this.storage.write("catalog.dat", r3);     // Catch: Exception -> L4
        CdnTokens.reconcile(getApplicationContext(), r3);     // Catch: Exception -> L4
        final CatalogIndex r0 = AppStorage.indexFor(r3);     // Catch: Exception -> L4
        runOnUiThread(new MainActivity$$ExternalSyntheticLambda56(this, r3, r0, r4));     // Catch: Exception -> L4
        return;
    L4:
        e = move-exception;
        failure("No se pudieron guardar los cambios", e);
    }

    private /* synthetic */ void lambda$entryMenu$46(Entry r5, DialogInterface r6, int r7) {
        if (r7 != 3) goto L5;
        RecordingUi.open(this, r5);
        return;
    L5:
        if (r7 != 0) goto L8;
        play(r5);
        return;
    L8:
        if (r7 != 1) goto L11;
        getPreferences(0).edit().putBoolean(r5.identity(), true ^ favorite(r5)).apply();
        renderAsync();
        return;
    L11:
        if (r5.kind.equals("Canales") == false) goto L13;
        schedule(r5);
        return;
    L13:
        this.tmdbUi.info(r5);
    }

    private /* synthetic */ void lambda$epgMenu$66(DialogInterface r3, int r4) {
        new EpgRepository(this).remove();
        this.epg = new EpgData();
        getPreferences(0).edit().remove("epg-url").remove("epg-file").apply();
        renderAsync();
    }

    private /* synthetic */ void lambda$epgMenu$67(DialogInterface r6, int r7) {
        if (r7 != 0) goto L5;
        filePicker(11);
        return;
    L5:
        if (r7 != 1) goto L8;
        urlDialog(true);
        return;
    L8:
        if (r7 != 2) goto L11;
        startActivity(new Intent(this, EpgActivity.class));
        return;
    L11:
        if (r7 != 3) goto L19;
        EpgRepository r0 = new EpgRepository(this);
        String r2 = r0.url();
        String r3 = r0.file();
        if (r2.isEmpty() == true) goto L16;
        loadEpg(r2, null);
        return;
    L16:
        if (r3.isEmpty() == true) goto L18;
        loadEpg(null, Uri.parse(r3));
        return;
    L18:
        message("Primero carga una EPG desde archivo o URL", ORANGE);
        return;
    L19:
        new AlertDialog.Builder(this).setTitle("Eliminar la guía EPG?").setPositiveButton("Eliminar", new MainActivity$$ExternalSyntheticLambda0(this)).setNegativeButton("Cancelar", null).show();
    }

    private /* synthetic */ void lambda$failure$71(String r2) {
        this.busy = false;
        message(r2, RED);
    }

    private /* synthetic */ void lambda$importMenu$47(DialogInterface r2, int r3) {
        filePicker(12);
    }

    private /* synthetic */ void lambda$importMenu$48(DialogInterface r4, int r5) {
        if (r5 != 2) goto L5;
        new AlertDialog.Builder(this).setTitle("Importar listas desde TXT").setMessage("Una lista por línea:\nCanales | Movistar | https://…\nSeries | Amazon Prime | https://…\nPelículas | Disney+ | https://…\n\nLas líneas vacías y las que empiezan por # se omiten.").setPositiveButton("Elegir TXT", new MainActivity$$ExternalSyntheticLambda62(this)).setNegativeButton("Cancelar", null).show();
        return;
    L5:
        boolean r0 = true;
        if (r5 == 1) goto L9;
        r0 = false;
    L9:
        choose(r0);
    }

    private /* synthetic */ WindowInsets lambda$landingShell$12(View r6, WindowInsets r7) {
        r6.setPadding(m402dp(16) + r7.getSystemWindowInsetLeft(), m402dp(16) + r7.getSystemWindowInsetTop(), m402dp(16) + r7.getSystemWindowInsetRight(), m402dp(10) + r7.getSystemWindowInsetBottom());
        return r7;
    }

    private /* synthetic */ void lambda$launch$40(Entry r1) {
        launchUnlocked(r1);
    }

    private /* synthetic */ List lambda$loadBatch$53(BatchPlaylist.Row r5) throws Exception {
        InputStream r0 = open(r5.url, null, Long.MAX_VALUE);
        Reader r1 = PlaylistInput.reader(r0);     // Catch: Throwable -> L17
        List<Entry> r2 = JsonPlaylist.read(r1, r5.kind).entries;     // Catch: Throwable -> L10
        if (r1 == null) goto L7;
        r1.close();     // Catch: Throwable -> L17
    L7:
        if (r0 == null) goto L9;
        r0.close();
    L9:
        return r2;
    L10:
        th = move-exception;
        if (r1 != null) goto L28;
    L16:
        throw th;     // Catch: Throwable -> L17
    L28:
        r1.close();     // Catch: Throwable -> L14
    L14:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L17
    L17:
        th = move-exception;
        if (r0 != null) goto L26;
    L23:
        throw th;
    L26:
        r0.close();     // Catch: Throwable -> L21
    L21:
        th = move-exception;
        th.addSuppressed(th);
        goto L23
    }

    private /* synthetic */ void lambda$loadBatch$54(int r4, int r5, BatchPlaylist.Row r6) {
        if (isDestroyed() == true) goto L6;
        message("Importando " + r4 + "/" + r5 + " · " + r6.kind + " · " + r6.provider, ORANGE);
        return;
    }

    private /* synthetic */ void lambda$loadBatch$55(final BatchPlaylist.Row r2, final int r3, final int r4) {
        runOnUiThread(new MainActivity$$ExternalSyntheticLambda79(this, r3, r4, r2));
    }

    private /* synthetic */ void lambda$loadBatch$56(BatchPlaylist.Result r5, CatalogIndex r6) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.catalog = r5.catalog;
        this.catalogIndex = r6;
        this.entries = r6.entries;
        this.providers = r6.providers;
        this.busy = false;
        if (this.page.equals("home") == false) goto L8;
        updateHomeContent();
    L9:
        String r0 = r5.imported + " listas importadas · " + r5.entries + " entradas";
        if (r5.imported != 0) goto L15;
        if (r5.errors.isEmpty() == false) goto L15;
        r0 = "El TXT no contiene listas";
    L15:
        if (r5.errors.isEmpty() == true) goto L17;
        r0 = r0 + "\n\n" + DetailExtras$$ExternalSyntheticBackport0.m398m("\n", r5.errors);
    L17:
        new AlertDialog.Builder(this).setTitle("Importación TXT terminada").setMessage(r0).setPositiveButton("Aceptar", null).show();
        refreshSavedEpg();
        return;
    L8:
        renderAsync();
        goto L9
    }

    private /* synthetic */ void lambda$loadBatch$57(Uri r6, Catalog r7) {
        InputStream r0 = open(null, r6, Long.MAX_VALUE);     // Catch: Exception -> L31
        Reader r1 = PlaylistInput.reader(r0);     // Catch: Throwable -> L24
        BatchPlaylist.Document r2 = BatchPlaylist.parse(r1);     // Catch: Throwable -> L17
        if (r1 == null) goto L8;
        r1.close();     // Catch: Throwable -> L24
    L8:
        if (r0 == null) goto L10;
        r0.close();     // Catch: Exception -> L31
    L10:
        final BatchPlaylist.Result r3 = BatchPlaylist.importLists(r2, r7, new MainActivity$$ExternalSyntheticLambda71(this), new MainActivity$$ExternalSyntheticLambda72(this));     // Catch: Exception -> L31
        if (r3.imported <= 0) goto L13;
        this.storage.write("catalog.dat", r3.catalog);     // Catch: Exception -> L31
        CdnTokens.reconcile(getApplicationContext(), r3.catalog);     // Catch: Exception -> L31
    L13:
        final CatalogIndex r4 = AppStorage.indexFor(r3.catalog);     // Catch: Exception -> L31
        runOnUiThread(new MainActivity$$ExternalSyntheticLambda73(this, r3, r4));     // Catch: Exception -> L31
        if (r3.imported <= 0) goto L43;
        this.storage.warmCatalogCache(r3.catalog);     // Catch: Exception -> L31
        return;
    L43:
        return;
    L17:
        th = move-exception;
        if (r1 != null) goto L34;
    L23:
        throw th;     // Catch: Throwable -> L24
    L34:
        r1.close();     // Catch: Throwable -> L21
    L21:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L24
    L24:
        th = move-exception;
        if (r0 != null) goto L39;
    L30:
        throw th;     // Catch: Exception -> L31
    L39:
        r0.close();     // Catch: Throwable -> L28
    L28:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Exception -> L31
    L31:
        e = move-exception;
        failure("No se pudo importar el TXT", e);
    }

    private /* synthetic */ InputStream lambda$loadEpg$68(String r4, String r5) throws Exception {
        if (r5.isEmpty() == false) goto L5;
        Uri r0 = null;
    L7:
        return open(r4, r0, 536870912);
    L5:
        r0 = Uri.parse(r5);
        goto L7
    }

    private /* synthetic */ void lambda$loadEpg$69(EpgData r5, String r6, Uri r7, List r8) {
        this.epg = r5;
        this.busy = false;
        SharedPreferences.Editor r0 = getPreferences(0).edit();
        String r1 = "";
        if (r6 != null) goto L5;
        String r2 = "";
    L6:
        SharedPreferences.Editor r3 = r0.putString("epg-url", r2);
        if (r7 == null) goto L10;
        r1 = r7.toString();
    L10:
        r3.putString("epg-file", r1).apply();
        renderAsync();
        int r4 = 0;
        Iterator r9 = r8.iterator();
    L12:
        if (r9.hasNext() == false) goto L16;
        if (this.epg.schedule((Entry) r9.next()).isEmpty() == true) goto L12;
        r4 = r4 + 1;
        goto L12
    L16:
        if (r4 != 0) goto L18;
        String r10 = "EPG cargada sin enlaces: reimporta el M3U para leer su tvg-id";
    L19:
        if (r4 != 0) goto L21;
        int r11 = ORANGE;
    L22:
        message(r10, r11);
        return;
    L21:
        r11 = GREEN;
        goto L22
    L18:
        r10 = "✓ EPG cargada · " + r4 + " canales enlazados";
        goto L19
    L5:
        r2 = r6;
        goto L6
    }

    private /* synthetic */ void lambda$loadEpg$70(final String r9, final Uri r10, final List r11) {
        EpgRepository r0 = new EpgRepository(this);     // Catch: Exception -> L12
        if (r10 != null) goto L5;
        String r1 = null;
    L6:
        r0.configure(r9, r1);     // Catch: Exception -> L12
        final EpgData r2 = r0.refresh(true, System.currentTimeMillis(), new MainActivity$$ExternalSyntheticLambda36(this));     // Catch: Exception -> L12
        runOnUiThread(new MainActivity$$ExternalSyntheticLambda37(this, r2, r9, r10, r11));     // Catch: Exception -> L10
        return;
    L10:
        e = move-exception;
        Exception r12 = e;
    L14:
        failure("No se pudo cargar la EPG", r12);
        return;
    L5:
        r1 = r10.toString();     // Catch: Exception -> L12
    L12:
        e = move-exception;
        r12 = e;
        goto L14
    }

    private /* synthetic */ void lambda$loadList$51(Catalog r4, CatalogIndex r5, String r6, Catalog.Source r7, List r8, M3uParser.Result r9) {
        this.catalog = r4;
        this.catalogIndex = r5;
        this.entries = r5.entries;
        this.providers = r5.providers;
        this.busy = false;
        openSection(r6, false);
        StringBuilder r0 = new StringBuilder().append("✓ ").append(r7.name).append(" · ").append(r8.size()).append(" entradas");
        if (r9.skipped != 0) goto L5;
        String r1 = "";
    L6:
        String r2 = r0.append(r1).toString();
        if (r9.skipped != 0) goto L9;
        int r3 = GREEN;
    L10:
        message(r2, r3);
        return;
    L9:
        r3 = ORANGE;
        goto L10
    L5:
        r1 = " · " + r9.skipped + " líneas omitidas";
        goto L6
    }

    private /* synthetic */ void lambda$loadList$52(String r15, final String r16, String r17, Uri r18, Catalog.Source r19, Catalog r20) {
        String r0 = PlaylistNames.resolve(getApplicationContext(), r15);     // Catch: Exception -> L60
        if (r0.isEmpty() == false) goto L7;
        String r1 = "Lista " + r16;     // Catch: Exception -> L60
    L8:
        String r10 = r1;
        InputStream r2 = open(r17, r18, Long.MAX_VALUE);     // Catch: Exception -> L58
        Reader r3 = PlaylistInput.reader(r2);     // Catch: Throwable -> L50
        final M3uParser.Result r7 = JsonPlaylist.read(r3, r16);     // Catch: Throwable -> L42
        if (r3 == null) goto L16;
        r3.close();     // Catch: Throwable -> L50
    L16:
        if (r2 == null) goto L18;
        r2.close();     // Catch: Exception -> L58
    L18:
        final List<Entry> r5 = r7.entries;     // Catch: Exception -> L58
        if (r5.isEmpty() == true) goto L41;
        if (r19 != null) goto L22;
        Catalog.Source r4 = r20.findOrigin(r15, r16);     // Catch: Exception -> L58
    L23:
        Catalog.Source r13 = r4;
        if (r13 != null) goto L26;
        String r6 = UUID.randomUUID().toString();     // Catch: Exception -> L58
    L27:
        if (r13 != null) goto L29;
        String r8 = r10;
    L30:
        final Catalog.Source r9 = new Catalog.Source(r6, r8, r15, r16, r5);     // Catch: Exception -> L58
        if (r0.isEmpty() == false) goto L34;
        if (r13 == null) goto L34;
        String r11 = r13.originalFilename;     // Catch: Exception -> L58
    L35:
        r9.originalFilename = r11;     // Catch: Exception -> L58
        if (r13 != null) goto L38;
        String r12 = null;
    L39:
        r9.provider = r12;     // Catch: Exception -> L58
        final Catalog r14 = r20.replace(r9);     // Catch: Exception -> L58
        this.storage.write("catalog.dat", r14);     // Catch: Exception -> L58
        CdnTokens.reconcile(getApplicationContext(), r14);     // Catch: Exception -> L58
        final CatalogIndex r21 = AppStorage.indexFor(r14);     // Catch: Exception -> L58
        runOnUiThread(new MainActivity$$ExternalSyntheticLambda65(this, r14, r21, r16, r9, r5, r7));     // Catch: Exception -> L58
        return;
    L38:
        r12 = r13.provider;     // Catch: Exception -> L58
    L34:
        r11 = r0;
        goto L35
    L29:
        r8 = PlaylistNames.title(r13, r0);     // Catch: Exception -> L58
        goto L30
    L26:
        r6 = r13.f177id;     // Catch: Exception -> L58
        goto L27
    L22:
        r4 = r19;
        goto L23
    L41:
        throw new IOException("La lista no contiene entradas HTTP/HTTPS válidas");     // Catch: Exception -> L58
    L42:
        th = move-exception;
        if (r3 == null) goto L74;
        r3.close();     // Catch: Throwable -> L47
    L75:
        throw th;     // Catch: Throwable -> L50
    L47:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L50
        throw th;     // Catch: Throwable -> L50
    L74:
        throw th;     // Catch: Throwable -> L50
    L50:
        th = move-exception;
        if (r2 == null) goto L76;
        r2.close();     // Catch: Throwable -> L55
    L77:
        throw th;     // Catch: Exception -> L58
    L55:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Exception -> L58
        throw th;     // Catch: Exception -> L58
    L76:
        throw th;     // Catch: Exception -> L58
    L58:
        e = e;
    L62:
        failure("No se pudo importar la lista", e);
        return;
    L7:
        r1 = r0;
    L60:
        e = e;
        goto L62
    }

    private /* synthetic */ void lambda$loadSavedEpg$8(EpgData r3) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.epg = r3;
        this.savedEpgReady = true;
        if (this.catalogVisible == true) goto L9;
        return;
    L9:
        if (this.page.equals("home") == false) goto L11;
        updateHomeContent();
    L12:
        requestGuideForSection();
        return;
    L11:
        refreshVisibleEpg();
        goto L12
    }

    private /* synthetic */ void lambda$loadSavedEpg$9() {
        EpgData r0 = new EpgData();
        Object r1 = this.storage.read("epg.dat");     // Catch: Exception -> L7
        if ((r1 instanceof EpgData) == false) goto L8;
        r0 = (EpgData) r1;     // Catch: Exception -> L7
        r0.ensureIndexed();     // Catch: Exception -> L7
    L8:
        final EpgData r2 = r0;
        runOnUiThread(new MainActivity$$ExternalSyntheticLambda33(this, r2));
    }

    private /* synthetic */ void lambda$manageLists$60(Catalog r2, DialogInterface r3, int r4) {
        sourceMenu(r2.sources.get(r4));
    }

    private /* synthetic */ void lambda$manageLists$61(String[] r4, final Catalog r5) {
        this.busy = false;
        if (isFinishing() == false) goto L5;
        return;
    L5:
        if (isDestroyed() == true) goto L10;
        new AlertDialog.Builder(this).setTitle("Tus listas M3U").setItems(r4, new MainActivity$$ExternalSyntheticLambda40(this, r5)).setNegativeButton("Cerrar", null).show();
        return;
    }

    private /* synthetic */ void lambda$manageLists$62(final Catalog r7) {
        final String[] r0 = new String[r7.sources.size()];
        int r1 = 0;
    L4:
        if (r1 >= r0.length) goto L11;
        Catalog.Source r2 = r7.sources.get(r1);
        String r3 = r2.originalFilename;
        if (r3 != null) goto L8;
    L9:
        r3 = PlaylistNames.cached(getApplicationContext(), r2.origin);
    L10:
        r2.originalFilename = r3;
        r0[r1] = PlaylistNames.label(r2, r3);
        r1 = r1 + 1;
        goto L4
    L8:
        if (r3.isEmpty() == false) goto L10;
    L11:
        runOnUiThread(new MainActivity$$ExternalSyntheticLambda78(this, r0, r7));
    }

    private /* synthetic */ void lambda$new$2() {
        renderAsync();
    }

    static /* synthetic */ void lambda$new$28(Runnable r1) {
        Process.setThreadPriority(10);
        r1.run();
    }

    static /* synthetic */ Thread lambda$new$29(final Runnable r3) {
        return new Thread(new MainActivity$$ExternalSyntheticLambda28(r3), "CHDTVCatalog");
    }

    private /* synthetic */ void lambda$new$3() {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        if (this.catalogVisible == true) goto L7;
        return;
    L7:
        if (this.startupUpdatePending == true) goto L9;
        return;
    L9:
        if (this.busy == true) goto L15;
        this.startupUpdatePending = false;
        AppUpdates.startup(this);
        return;
    }

    private /* synthetic */ void lambda$onCreate$5() {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        if (this.adapter == null) goto L9;
        this.timer.removeCallbacks(this.artworkRefresh);
        this.timer.postDelayed(this.artworkRefresh, 120);
        return;
    }

    private /* synthetic */ void lambda$onCreate$6(Catalog r5, CatalogIndex r6, long r7, long r9, long r11, long r13, String r15) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.catalog = r5;
        this.catalogIndex = r6;
        this.entries = r6.entries;
        this.providers = r6.providers;
        this.busy = false;
        if ((getApplication() instanceof ChdtvApplication) == false) goto L8;
        ((ChdtvApplication) getApplication()).catalogReady(this);
    L8:
        this.tmdb.warmCache();
        if (this.page.equals("home") == false) goto L11;
        updateHomeContent();
    L12:
        restoreScroll();
        Log.i("CHDTVStartup", "catalog_ms=" + (r7 - r9) + " index_ms=" + (r11 - r7) + " content_visible_ms=" + (SystemClock.elapsedRealtime() - r13));
        BlackBox.event("CATALOG_READY", "read_ms=" + (r7 - r9) + " index_ms=" + (r11 - r7) + " entries=" + this.entries.size() + " created_entries=" + r5.materializedSize());
        requestGuideForSection();
        queueStartupUpdate();
        if (getIntent().hasExtra("chromeRoute") == false) goto L16;
        String r0 = getIntent().getStringExtra("chromeRoute");
        getIntent().removeExtra("chromeRoute");
        openChromeRoute(r0);
    L16:
        if (getIntent().hasExtra("chromeTool") == false) goto L18;
        String r1 = getIntent().getStringExtra("chromeTool");
        getIntent().removeExtra("chromeTool");
        openSettingsTool(r1);
    L18:
        if (r15 == null) goto L21;
        message(r15, RED);
        return;
    L21:
        return;
    L11:
        renderAsync();
        goto L12
    }

    private /* synthetic */ void lambda$onCreate$7(final long r16) {
        Process.setThreadPriority(0);
        final long r7 = SystemClock.elapsedRealtime();
        Catalog r0 = new Catalog();
        Catalog r1 = this.storage.catalog();     // Catch: Exception -> L5 FileNotFoundException -> L7
        String r14 = null;
    L9:
        final Catalog r3 = r1;
        final long r5 = SystemClock.elapsedRealtime();
        final CatalogIndex r4 = AppStorage.indexFor(r3);
        final long r9 = SystemClock.elapsedRealtime();
        final String r13 = r14;
        runOnUiThread(new MainActivity$$ExternalSyntheticLambda75(this, r3, r4, r5, r7, r9, r16, r13));
        Process.setThreadPriority(10);
        this.storage.warmCatalogCache(r3);
        return;
    L8:
        r1 = r0;
        r14 = null;
    L6:
        r1 = r0;
        r14 = "No se pudo abrir el catálogo guardado";
        goto L9
    }

    private /* synthetic */ void lambda$onResume$72() {
        if (this.page.equals("catalog") == true) goto L5;
        return;
    L5:
        if (this.continueMode == true) goto L7;
        refreshVisibleEpg();
        refreshVisibleArtwork();
        if (this.cineHero != null) goto L18;
        return;
    L18:
        if (this.list != null) goto L20;
        return;
    L20:
        if (this.rows.isEmpty() == true) goto L29;
        previewRow(Math.max(0, Math.min(this.rows.size() - 1, this.list.getSelectedItemPosition())));
        return;
    L29:
        return;
    L7:
        if (this.list != null) goto L9;
        Parcelable r0 = null;
    L10:
        renderAsync();
        if (r0 != null) goto L13;
        return;
    L13:
        if (this.list == null) goto L25;
        this.list.onRestoreInstanceState(r0);
        return;
    L25:
        return;
    L9:
        r0 = this.list.onSaveInstanceState();
        goto L10
    }

    private /* synthetic */ void lambda$open$35(Row r1) {
        openUnlocked(r1);
    }

    private /* synthetic */ void lambda$openChromeRoute$14() {
        if (this.page.equals("home") == false) goto L6;
        showHome();
        return;
    L6:
        if (this.page.equals("settings") == false) goto L8;
        showSettingsUnlocked();
        return;
    L8:
        buildUi();
        renderAsync();
    }

    private /* synthetic */ void lambda$openSection$13(String r1, boolean r2) {
        openSectionUnlocked(r1, r2);
    }

    private /* synthetic */ void lambda$openUnlocked$36(Row r2) {
        play(r2.entry);
    }

    private /* synthetic */ void lambda$openUnlocked$37(Row r2) {
        play(r2.entry);
    }

    private /* synthetic */ void lambda$play$38(Entry r1) {
        launch(r1);
    }

    private /* synthetic */ void lambda$play$39(Entry r1) {
        WatchHistory.remove(this, r1);
        launch(r1);
    }

    static /* synthetic */ int lambda$prepareCatalog$32(Entry r4, Entry r5) {
        int r0 = String.CASE_INSENSITIVE_ORDER.compare(r4.group, r5.group);
        if (r0 != 0) goto L5;
        r0 = Integer.compare(r4.season, r5.season);
    L5:
        if (r0 != 0) goto L7;
        r0 = Integer.compare(r4.episode, r5.episode);
    L7:
        if (r0 == 0) goto L9;
        return r0;
    L9:
        return String.CASE_INSENSITIVE_ORDER.compare(r4.title, r5.title);
    }

    private /* synthetic */ void lambda$refreshSavedEpg$10(EpgData r3) {
        this.epgRefreshing = false;
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.epg = r3;
        if (this.catalogVisible == true) goto L9;
        return;
    L9:
        if (this.page.equals("home") == false) goto L11;
        updateHomeContent();
    L13:
        if (this.cineHero != null) goto L15;
        return;
    L15:
        if (this.list != null) goto L17;
        return;
    L17:
        if (this.kind.equals("Canales") == false) goto L28;
        int r0 = this.list.getSelectedItemPosition();
        if (r0 >= 0) goto L21;
        return;
    L21:
        if (r0 < this.rows.size()) goto L23;
        return;
    L23:
        if (this.rows.get(r0).level != 0) goto L31;
        previewRow(r0);
        return;
    L31:
        return;
    L28:
        return;
    L11:
        refreshVisibleEpg();
        goto L13
    }

    private /* synthetic */ void lambda$refreshSavedEpg$11() {
        final EpgData r0 = new EpgRepository(getApplicationContext()).refreshIfNeeded();
        runOnUiThread(new MainActivity$$ExternalSyntheticLambda1(this, r0));
    }

    private /* synthetic */ void lambda$refreshVisibleArtwork$4() {
        this.timer.removeCallbacks(this.artworkRefresh);
        this.timer.postDelayed(this.artworkRefresh, 120);
    }

    private /* synthetic */ void lambda$relatedInfo$41(Entry r1) {
        play(r1);
    }

    private /* synthetic */ void lambda$relatedInfo$42(final Entry r10) {
        TmdbService r2 = this.tmdb;
        LogoLoader r3 = this.logos;
        Runnable r4 = new MainActivity$$ExternalSyntheticLambda63(this, r10);
        if (r10.kind.equals("Series") == false) goto L5;
        List<Entry> r0 = relatedEpisodes(r10);
    L6:
        CinematicDetail.show(this, r10, r2, r3, r4, r0, new MainActivity$$ExternalSyntheticLambda58(this), this.entries, new MainActivity$$ExternalSyntheticLambda59(this));
        return;
    L5:
        r0 = null;
        goto L6
    }

    private /* synthetic */ void lambda$renderAsync$30(int r3, CatalogRequest r4, CatalogPlan r5) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        if (r3 == this.catalogGeneration) goto L7;
        return;
    L7:
        if (this.page.equals("catalog") == true) goto L9;
        return;
    L9:
        if (r4.matches(this) == true) goto L12;
        return;
    L12:
        if (this.catalogVisible == true) goto L16;
        this.deferredPlan = r5;
        this.deferredRequest = r4;
        return;
    L16:
        if (r4.continuing == false) goto L18;
        applyContinue(r5);
    L19:
        restoreScroll();
        return;
    L18:
        applyCatalog(r5);
        goto L19
    }

    private /* synthetic */ void lambda$renderAsync$31(final CatalogRequest r9, final int r10) {
        long r0 = SystemClock.elapsedRealtime();
        r9.loadPreferences();
        if (r9.continuing == false) goto L5;
        CatalogPlan r2 = prepareContinue(r9);
    L10:
        final CatalogPlan r3 = r2;
        BlackBox.event("CATALOG_FILTER_READY", "duration_ms=" + (SystemClock.elapsedRealtime() - r0) + " rows=" + r2.rows.size());
        Log.i("CHDTVCatalog", "prepare_ms=" + (SystemClock.elapsedRealtime() - r0) + " rows=" + r2.rows.size());
        runOnUiThread(new MainActivity$$ExternalSyntheticLambda17(this, r10, r9, r3));
        return;
    L5:
        Object r4 = r9.key();
        CatalogPlan r5 = (CatalogPlan) r9.index.views.get(r4);
        if (r5 != null) goto L8;
        r5 = prepareCatalog(r9);
        r9.index.views.put(r4, r5, r5.rows.size());
    L9:
        r2 = withContinue(r5, r9);
        goto L10
    L8:
        BlackBox.event("CATALOG_VIEW_CACHE_HIT", "rows=" + r5.rows.size());
        goto L9
    }

    static /* synthetic */ void lambda$settingAction$21(Runnable r0, View r1) {
        r0.run();
    }

    private /* synthetic */ void lambda$showHome$15() {
        startActivity(new Intent(this, PlexActivity.class));
    }

    private /* synthetic */ void lambda$showHome$16(AdapterView r4, View r5, int r6, long r7) {
        if (r6 >= 3) goto L5;
        openSection(new String[]{"Canales", "Películas", "Series"}[r6], false);
        return;
    L5:
        if (r6 != 3) goto L7;
        startActivity(new Intent(this, EpgActivity.class));
        return;
    L7:
        ParentalControl.requireKey(this, "kind:Plex", new MainActivity$$ExternalSyntheticLambda61(this));
    }

    private /* synthetic */ void lambda$showSettingsUnlocked$17() {
        this.tmdbUi.settings();
    }

    private /* synthetic */ void lambda$showSettingsUnlocked$18() {
        ParentalControl.settings(this);
    }

    private /* synthetic */ void lambda$showSettingsUnlocked$19() {
        startActivity(new Intent(this, CdnTokenActivity.class).putExtra("navigationOpen", navigationOpen()));
    }

    private /* synthetic */ void lambda$showSettingsUnlocked$20() {
        startActivity(new Intent(this, DaznTokenActivity.class).putExtra("navigationOpen", navigationOpen()));
    }

    private /* synthetic */ void lambda$sourceMenu$63(EditText r4, Catalog.Source r5, DialogInterface r6, int r7) {
        String r0 = r4.getText().toString().trim();
        if (r0.isEmpty() == true) goto L6;
        commit(this.catalog.rename(r5.f177id, r0), "Nombre actualizado");
        return;
    }

    private /* synthetic */ void lambda$sourceMenu$64(Catalog.Source r3, DialogInterface r4, int r5) {
        commit(this.catalog.remove(r3.f177id), "Lista eliminada");
    }

    private /* synthetic */ void lambda$sourceMenu$65(boolean r7, final Catalog.Source r8, DialogInterface r9, int r10) {
        String r0 = null;
        if (r7 == true) goto L13;
        if (r10 != 0) goto L13;
        if (r8.origin.startsWith("content:") == false) goto L8;
        Uri r1 = Uri.parse(r8.origin);
    L9:
        if (r1 != null) goto L11;
        r0 = r8.origin;
    L11:
        loadList(r0, r1, r8.kind, r8);
        return;
    L8:
        r1 = null;
    L13:
        if (r10 != (!r7)) goto L15;
        final EditText r2 = new EditText(this);
        r2.setSingleLine();
        r2.setText(PlaylistNames.title(r8, r8.originalFilename));
        new AlertDialog.Builder(this).setTitle("Nombre de la lista").setView(r2).setPositiveButton("Guardar", new MainActivity$$ExternalSyntheticLambda25(this, r2, r8)).setNegativeButton("Cancelar", null).show();
        return;
    L15:
        AlertDialog.Builder r3 = new AlertDialog.Builder(this).setTitle("Eliminar " + PlaylistNames.title(r8, r8.originalFilename) + "?");
        StringBuilder r4 = new StringBuilder().append(r8.entries.size()).append(" entradas se quitarán de la app. El archivo M3U original se conserva.");
        if (r7 == false) goto L18;
        String r5 = "\n\nLa versión anterior mezclaba las listas. Este bloque contiene todas las entradas antiguas que aún no has reimportado.";
    L19:
        r3.setMessage(r4.append(r5).toString()).setPositiveButton("Eliminar", new MainActivity$$ExternalSyntheticLambda26(this, r8)).setNegativeButton("Cancelar", null).show();
        return;
    L18:
        r5 = "";
        goto L19
    }

    private /* synthetic */ void lambda$urlDialog$50(EditText r4, boolean r5, DialogInterface r6, int r7) {
        String r0 = r4.getText().toString().trim();
        if (r5 == false) goto L5;
        loadEpg(r0, null);
        return;
    L5:
        loadList(r0, null, this.importKind, null);
    }

    private void landingShell(String r7) {
        this.adapter = null;
        this.content = null;
        this.root = new LinearLayout(this);
        this.root.setOrientation(1);
        this.root.setBackground(new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{0, 0}));
        this.root.setPadding(m402dp(16), m402dp(16), m402dp(16), m402dp(10));
        setContentView(this.root);
        this.root.setOnApplyWindowInsetsListener(new MainActivity$$ExternalSyntheticLambda18(this));
        this.summary = text(r7, 13, MUTED);
        this.summary.setPadding(0, m402dp(6), 0, m402dp(18));
        this.root.addView(this.summary);
        this.status = text("", 12, MUTED);
    }

    private void launch(final Entry r5) {
        if (this.playbackRequestedAt > 0) goto L5;
        this.playbackRequestedAt = SystemClock.elapsedRealtime();
    L5:
        ParentalControl.requireEntry(this, r5, new MainActivity$$ExternalSyntheticLambda70(this, r5));
    }

    private void launchUnlocked(Entry r6) {
        BlackBox.event("PLAY_REQUEST", BlackBox.stream(r6));
        SearchKeyboard.hide(this, this.catalogSearch, this.list);
        if (this.playbackLaunching == false) goto L5;
        return;
    L5:
        this.playbackLaunching = true;
        Intent r0 = new Intent(this, PlayerActivity.class);
        if (this.playbackRequestedAt <= 0) goto L8;
        long r1 = this.playbackRequestedAt;
    L9:
        startActivity(r0.putExtra("playbackRequestedAt", r1).putExtra("entry", r6));
        overridePendingTransition(0, 0);
        return;
    L8:
        r1 = SystemClock.elapsedRealtime();
        goto L9
    }

    private void loadBatch(Uri r4) {
        if (this.busy == false) goto L5;
        return;
    L5:
        this.busy = true;
        message("Leyendo TXT de listas…", ORANGE);
        this.worker.execute(new BatchImport(this, r4, this.catalog));     // Catch: RejectedExecutionException -> L8
        return;
    L9:
        this.busy = false;
        message("No se pudo iniciar la importación", RED);
    }

    private void loadEpg(final String r5, final Uri r6) {
        if (this.busy == false) goto L5;
        return;
    L5:
        final ArrayList r0 = new ArrayList();
        r0.addAll(this.catalogIndex.select("Canales", null, null, null));
        if (r0.isEmpty() == false) goto L9;
        message("Importa primero una lista de canales", ORANGE);
        return;
    L9:
        this.busy = true;
        message("⏳ Cargando programación XMLTV…", ORANGE);
        this.worker.execute(new MainActivity$$ExternalSyntheticLambda80(this, r5, r6, r0));
    }

    private void loadList(final String r11, final Uri r12, final String r13, final Catalog.Source r14) {
        if (this.busy == false) goto L5;
        return;
    L5:
        this.busy = true;
        message("⏳ Importando lista…", ORANGE);
        final Catalog r9 = this.catalog;
        if (r11 != null) goto L8;
        final String r4 = r12.toString();
    L9:
        this.worker.execute(new MainActivity$$ExternalSyntheticLambda55(this, r4, r13, r11, r12, r14, r9));
        return;
    L8:
        r4 = r11;
        goto L9
    }

    private void loadSavedEpg() {
        if (guideSectionVisible() == true) goto L5;
        return;
    L5:
        if (isDestroyed() == false) goto L7;
        return;
    L7:
        if (isFinishing() == true) goto L12;
        EpgChannelFlow.request(this);
        return;
    }

    private void manageLists() {
        if (this.busy == false) goto L6;
        return;
    L6:
        if (this.catalog.sources.isEmpty() == false) goto L9;
        new AlertDialog.Builder(this).setTitle("Tus listas").setMessage("No tienes listas importadas. Usa + Lista para añadir una.").setPositiveButton("Cerrar", null).show();
        return;
    L9:
        final Catalog r0 = this.catalog;
        this.busy = true;
        this.worker.execute(new MainActivity$$ExternalSyntheticLambda31(this, r0));
    }

    private void message(String r3, int r4) {
        this.status.setTextColor(AppTheme.color(this, r4));
        this.status.setText(r3);
    }

    private void navigateBack() {
        if (this.continueMode == false) goto L10;
        if (this.page.equals("catalog") == false) goto L10;
        this.continueMode = false;
    L7:
        renderAsync();
        return;
    L10:
        if (this.page.equals("catalog") == true) goto L14;
        showHome();
        return;
    L14:
        if (this.season == null) goto L17;
        this.season = null;
        goto L7
    L17:
        if (this.series == null) goto L20;
        this.series = null;
        goto L7
    L20:
        if (this.provider == null) goto L22;
        this.provider = null;
        this.section = "Todas las secciones";
        goto L7
    L22:
        showHome();
    }

    private InputStream open(String r10, Uri r11, long r12) throws IOException {
        if (r11 == null) goto L9;
        InputStream r0 = getContentResolver().openInputStream(r11);
        if (r0 == null) goto L8;
        return new LimitedInputStream(r0, r12);
    L8:
        throw new IOException("No se pudo abrir el archivo");
    L9:
        int r2 = 0;
        URL r1 = new URL(DownloadLinks.direct(r10));
    L11:
        if (r2 > 6) goto L62;
        if (r1.getProtocol().equals("http") == false) goto L15;
    L19:
        final HttpURLConnection r8 = (HttpURLConnection) r1.openConnection();
        r8.setInstanceFollowRedirects(false);
        r8.setConnectTimeout(15000);
        r8.setReadTimeout(AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH);
        r8.setRequestProperty(HttpHeaders.USER_AGENT, "MiM3U/0.16");
        int r3 = r8.getResponseCode();     // Catch: IOException -> L57
        if (r3 == 301) goto L67;
        if (r3 == 302) goto L67;
        if (r3 == 303) goto L67;
        if (r3 == 307) goto L67;
        if (r3 == 308) goto L67;
        if (r3 != 200) goto L63;
        final InputStream r5 = r8.getInputStream();     // Catch: IOException -> L57
        final long r6 = r12;
        return new C10078(this, r5, r6, r8);
    L63:
    L43:
        e = e;
    L59:
        r8.disconnect();
        throw e;
    L35:
        if (r3 != 401) goto L37;
    L40:
        String r4 = "El enlace necesita permiso de descarga o ha caducado";
    L42:
        throw new IOException(r4);     // Catch: IOException -> L43
    L37:
        if (r3 == 403) goto L40;
        r4 = "Servidor HTTP " + r3;     // Catch: IOException -> L43
    L67:
        String r13 = r8.getHeaderField(HttpHeaders.LOCATION);     // Catch: IOException -> L55
        if (r13 == null) goto L54;
        r1 = new URL(r1, r13);     // Catch: IOException -> L55
        r8.disconnect();     // Catch: IOException -> L55
        r2 = r2 + 1;     // Catch: IOException -> L55
        r12 = r12;
        goto L11
    L54:
        throw new IOException("La descarga no devuelve una dirección válida");     // Catch: IOException -> L55
    L55:
        e = e;
    L57:
        e = e;
        goto L59
    L15:
        if (r1.getProtocol().equals("https") == true) goto L19;
        throw new IOException("Usa HTTP o HTTPS");
    L62:
        throw new IOException("La descarga tiene demasiadas redirecciones");
    }

    private void open(final Row r4) {
        if (r4.level != 0) goto L10;
        if (r4.entry == null) goto L10;
        if ("Canales".equals(r4.entry.kind) == false) goto L10;
        this.playbackRequestedAt = SystemClock.elapsedRealtime();
    L10:
        if (r4.level != 4) goto L13;
        this.continueMode = true;
        renderAsync();
        return;
    L13:
        Runnable r0 = new MainActivity$$ExternalSyntheticLambda27(this, r4);
        if (r4.level != 3) goto L16;
        ParentalControl.requireKey(this, "provider:" + Providers.base(r4.target), r0);
        return;
    L16:
        ParentalControl.requireEntry(this, r4.entry, r0);
    }

    private void openSection(final String r3, final boolean r4) {
        ParentalControl.requireKey(this, "kind:" + r3, new MainActivity$$ExternalSyntheticLambda76(this, r3, r4));
    }

    private void openSectionUnlocked(String r3, boolean r4) {
        this.page = "catalog";
        this.continueMode = false;
        this.kind = r3;
        this.provider = null;
        this.series = null;
        this.season = null;
        this.section = "Todas las secciones";
        this.filter = "";
        this.favorites = false;
        this.guide = r4;
        buildUi();
        renderAsync();
        requestGuideForSection();
    }

    private void openUnlocked(final Row r10) {
        if (r10.level != 3) goto L7;
        this.provider = r10.target;
        this.section = "Todas las secciones";
    L5:
        renderAsync();
        return;
    L7:
        if (r10.level != 1) goto L11;
        Entry r1 = r10.entry;
        TmdbService r2 = this.tmdb;
        LogoLoader r3 = this.logos;
        Runnable r4 = new MainActivity$$ExternalSyntheticLambda57(this, r10);
        List<Entry> r5 = seriesEpisodes(r10.target);
        MainActivity$$ExternalSyntheticLambda58 r6 = new MainActivity$$ExternalSyntheticLambda58(this);
        List<Entry> r7 = this.entries;
        MainActivity$$ExternalSyntheticLambda59 r8 = new MainActivity$$ExternalSyntheticLambda59(this);
    L9:
        CinematicDetail.show(this, r1, r2, r3, r4, r5, r6, r7, r8);
        return;
    L11:
        if (r10.level != 2) goto L14;
        this.season = Integer.valueOf(Integer.parseInt(r10.target.substring("Temporada ".length())));
        goto L5
    L14:
        if (this.guide == false) goto L19;
        if (this.kind.equals("Canales") == false) goto L19;
        schedule(r10.entry);
        return;
    L19:
        if ("Canales".equals(r10.entry.kind) == true) goto L25;
        r1 = r10.entry;
        r2 = this.tmdb;
        r3 = this.logos;
        r4 = new MainActivity$$ExternalSyntheticLambda60(this, r10);
        if (r10.entry.kind.equals("Series") == false) goto L23;
        r5 = seriesEpisodes(seriesName(r10.entry));
    L24:
        r6 = new MainActivity$$ExternalSyntheticLambda58(this);
        r7 = this.entries;
        r8 = new MainActivity$$ExternalSyntheticLambda59(this);
        goto L9
    L23:
        r5 = null;
        goto L24
    L25:
        play(r10.entry);
    }

    private void play(final Entry r10) {
        this.playbackRequestedAt = SystemClock.elapsedRealtime();
        long r4 = WatchHistory.resume(this, r10);
        if (r4 <= 0) goto L5;
        ChdtvDialogs.resume(this, r10, r4, this.logos, new MainActivity$$ExternalSyntheticLambda38(this, r10), new MainActivity$$ExternalSyntheticLambda39(this, r10));
        return;
    L5:
        launch(r10);
    }

    private CatalogPlan prepareCatalog(CatalogRequest r32) {
        CatalogPlan r2 = SectionNavigation.prepare(r32);
        if (r2 == null) goto L5;
        return r2;
    L5:
        CatalogIndex r3 = r32.index;
        String r4 = r32.kind;
        String r5 = r32.provider;
        String r6 = r32.series;
        String r7 = r32.section;
        String r8 = r32.filter;
        Integer r9 = r32.season;
        boolean r10 = r32.favorites;
        Map<String, ?> r11 = r32.prefs;
        Map<Entry, String> r12 = r3.providers;
        ArrayList r13 = new ArrayList();
        Catalog r14 = r3.sourceCatalog();
        if ("Canales".equals(r4) == true) goto L11;
        if (r5 == null) goto L11;
        if (r14.demand == null) goto L11;
        r14.demand.materialize(r4, r5, r6, r9);
    L11:
        List<Entry> r15 = r3.select(r4, r5, r6, r9);
        TreeSet r0 = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        Catalog r16 = r3.sourceCatalog();
        String r17 = r6;
        if (r16.demand != null) goto L14;
        Set<String> r18 = null;
    L15:
        if (r18 == null) goto L17;
        r0.addAll(r18);
    L20:
        ArrayList r19 = new ArrayList();
        String r20 = "Todas las secciones";
        r19.add("Todas las secciones");
        r19.addAll(r0);
        if (r19.contains(r7) == true) goto L23;
        r7 = (String) r19.get(0);
    L23:
        String r1 = r7;
        TreeMap r21 = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        ArrayList r22 = new ArrayList();
        HashSet r23 = new HashSet();
        Iterator<Entry> r110 = r15.iterator();
    L24:
        HashSet r24 = r23;
        if (r110.hasNext() == false) goto L30;
        String r25 = r20;
        Entry r26 = r110.next();
        if (r5 == null) goto L30;
        r19 = r19;
        if (r26.kind.equals(r4) == false) goto L140;
        if (r5 != null) goto L82;
    L85:
        if (r10 == false) goto L90;
        String r111 = r5;
        if (Boolean.TRUE.equals(r11.get(r26.identity())) == false) goto L89;
    L92:
        if (r1.equals(r25) == true) goto L97;
        if (r26.group.equals(r1) == true) goto L97;
        r20 = r25;
        r5 = r111;
        r23 = r24;
    L97:
        if (r8.isEmpty() == true) goto L101;
        String r27 = r1;
        if ((String.valueOf(r26.title) + " " + r26.group + " " + r12.get(r26)).toLowerCase(Locale.ROOT).contains(r8) == false) goto L100;
    L103:
        if (r4.equals("Series") == false) goto L126;
        if (r17 == null) goto L108;
        if (r9 != null) goto L126;
    L108:
        if (r17 == null) goto L112;
        MainActivity r28 = this;
        r17 = r17;
        if (r28.seriesName(r26).equals(r17) == true) goto L113;
        r20 = r25;
        r17 = r17;
        r5 = r111;
        r23 = r24;
        r1 = r27;
    L113:
        if (r17 != null) goto L115;
        String r29 = r28.seriesName(r26);
    L120:
        List r30 = (List) r21.get(r29);
        if (r30 != null) goto L123;
        r30 = new ArrayList();
        r21.put(r29, r30);
    L123:
        r30.add(r26);
    L131:
        r20 = r2;
        r5 = r111;
        r1 = r27;
        r8 = r8;
        r17 = r17;
        r23 = r24;
        goto L24
    L115:
        StringBuilder r31 = new StringBuilder("Temporada ");
        if (r26.season >= 10) goto L118;
        String r33 = "0";
    L119:
        r29 = r31.append(r33).append(Math.max(0, r26.season)).toString();
        goto L120
    L118:
        r33 = "";
        goto L119
    L112:
        r28 = this;
        r17 = r17;
    L126:
        if (r4.equals("Series") == false) goto L133;
        if (seriesName(r26).equals(r17) == false) goto L131;
        if (Math.max(0, r26.season) != r9.intValue()) goto L131;
    L133:
        if (r4.equals("Series") == false) goto L137;
        HashSet r34 = r24;
        if (r34.add(CatalogCounts.episodeKey(r26)) == true) goto L138;
    L139:
        r20 = r2;
        r5 = r111;
        r1 = r27;
        r8 = r8;
        r17 = r17;
        r23 = r34;
    L138:
        r22.add(r26);
        goto L139
    L137:
        r34 = r24;
        goto L138
    L100:
        r20 = r25;
        r5 = r111;
        r23 = r24;
        r1 = r27;
        goto L24
    L101:
        r27 = r1;
        goto L103
    L89:
        r5 = r111;
    L84:
        r23 = r24;
        r20 = r25;
        goto L24
    L90:
        r111 = r5;
        goto L92
    L82:
        if (r5.equals(r12.get(r26)) == false) goto L84;
    L140:
        r20 = r25;
        r5 = r5;
        r17 = r17;
        r23 = r24;
    L30:
        Iterator r112 = r21.entrySet().iterator();
    L32:
        if (r112.hasNext() == false) goto L34;
        ArrayList r210 = r19;
        Map.Entry r35 = (Map.Entry) r112.next();
        List r36 = (List) r35.getValue();
        int r113 = 1;
        if (r17 != null) goto L72;
        boolean r37 = true;
    L73:
        String r211 = CatalogCounts.detail(r36, r37);
        String r212 = (String) r35.getKey();
        String r213 = (String) r35.getKey();
        Entry r214 = (Entry) ((List) r35.getValue()).get(0);
        if (r17 == null) goto L77;
        r113 = 2;
    L77:
        r13.add(new Row(r212, r211, r213, r214, r113));
        r19 = r210;
        goto L32
    L72:
        r37 = false;
        goto L73
    L34:
        if (r4.equals("Canales") == true) goto L36;
        SectionNavigation.sortSelected(r22);
    L36:
        Iterator r38 = r22.iterator();
    L38:
        if (r38.hasNext() == false) goto L39;
        Entry r39 = (Entry) r38.next();
        r13.add(new Row(r39.title, r39.group, null, r39, 0));
        r38 = r38;
        r19 = r19;
        goto L38
    L39:
        if (r5 != null) goto L64;
        TreeMap r40 = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        Iterator<Entry> r41 = r15.iterator();
    L42:
        if (r41.hasNext() == false) goto L43;
        Entry r42 = r41.next();
        if (r10 == false) goto L57;
        if (Boolean.TRUE.equals(r11.get(r42.identity())) == false) goto L42;
    L57:
        if (r8.isEmpty() == true) goto L60;
        if ((String.valueOf(r42.title) + " " + r42.group + " " + r12.get(r42)).toLowerCase(Locale.ROOT).contains(r8) == false) goto L42;
    L60:
        String r43 = r12.get(r42);
        List r114 = (List) r40.get(r43);
        if (r114 != null) goto L63;
        r114 = new ArrayList();
        r40.put(r43, r114);
    L63:
        r114.add(r42);
        goto L42
    L43:
        Iterator r44 = r40.entrySet().iterator();
    L45:
        if (r44.hasNext() == false) goto L64;
        Map.Entry r45 = (Map.Entry) r44.next();
        if (((String) r45.getKey()).equals(Providers.base((String) r45.getKey())) == false) goto L50;
        String r46 = String.valueOf((String) r45.getKey()) + " · " + StreamFormat.summary((List) r45.getValue());
    L51:
        r13.add(new Row(r46, CatalogCounts.provider((List) r45.getValue(), r4), (String) r45.getKey(), (Entry) ((List) r45.getValue()).get(0), 3));
        goto L45
    L50:
        r46 = (String) r45.getKey();
    L64:
        CatalogPlan r47 = new CatalogPlan(r13, r19, r1);
        if (r14.demand == null) goto L67;
        DemandMenus.save(r14.demand, r32, r47);
    L67:
        return r47;
    L17:
        Iterator<Entry> r48 = r3.select(r4, r5, null, null).iterator();
    L19:
        if (r48.hasNext() == false) goto L20;
        r0.add(r48.next().group);
        r5 = r5;
        goto L19
    L14:
        r18 = r16.demand.categories(r4, r5);
        goto L15
    }

    private CatalogPlan prepareContinue(CatalogRequest r14) {
        CatalogIndex r0 = r14.index;
        String r1 = r14.kind;
        String r2 = r14.provider;
        String r3 = r14.filter;
        boolean r4 = r14.favorites;
        Map<String, ?> r5 = r14.prefs;
        ArrayList r6 = new ArrayList();
        Iterator<WatchHistory.Item> r7 = r0.history(this, r1, r2).iterator();
    L4:
        if (r7.hasNext() == false) goto L6;
        WatchHistory.Item r8 = r7.next();
        Entry r11 = r8.entry;
        if (r4 == false) goto L12;
        if (Boolean.TRUE.equals(r5.get(r11.identity())) == false) goto L4;
    L12:
        if (r3.isEmpty() == true) goto L17;
        if ((String.valueOf(r11.title) + " " + r11.series + " " + r11.group).toLowerCase(Locale.ROOT).contains(r3) == false) goto L4;
    L17:
        if (r11.kind.equals("Series") == true) goto L19;
    L21:
        String r9 = r11.title;
    L22:
        r6.add(new Row(r9, r8.label(), null, r11, 0));
        goto L4
    L19:
        if (r11.series.isEmpty() == true) goto L21;
        r9 = String.valueOf(r11.series) + " · S" + Math.max(0, r11.season) + ExifInterface.LONGITUDE_EAST + Math.max(0, r11.episode) + " · " + r11.title;
        goto L22
    L6:
        return new CatalogPlan(r6, Collections.emptyList(), r14.section);
    }

    private boolean previewBeforeOpen(int r5) {
        if (AppTheme.cine(this) == true) goto L5;
    L24:
        return false;
    L5:
        if (this.cineHero == null) goto L24;
        if (r5 < 0) goto L24;
        if (r5 >= this.rows.size()) goto L24;
        if (this.rows.get(r5).level >= 3) goto L24;
        Row r0 = this.rows.get(r5);
        if (r0.level == 0) goto L15;
    L19:
        boolean r1 = this.previewActivation.activate(previewKey(r0));
        previewRow(r5);
        this.list.setItemChecked(r5, true);
        if (this.list.isInTouchMode() == true) goto L23;
        this.list.setSelection(r5);
    L23:
        return !r1;
    L15:
        if (r0.entry == null) goto L19;
        if ("Canales".equals(r0.entry.kind) == false) goto L19;
        return false;
    }

    private String previewKey(Row r4) {
        return r4.level + "|" + r4.target + "|" + r4.entry.identity() + "|" + Entry.details(r4.entry).url;
    }

    private void previewRow(int r6) {
        if (this.cineHero == null) goto L13;
        if (r6 >= 0) goto L6;
        return;
    L6:
        if (r6 >= this.rows.size()) goto L16;
        Row r0 = this.rows.get(r6);
        this.previewActivation.select(previewKey(r0));
        if (r0.entry.kind.equals("Canales") == false) goto L11;
        this.cineHero.bindChannel(r0.entry, this.epg);
        this.logos.show(this.cineHero.artwork, this.catalogIndex.logos.candidates(r0.entry));
        return;
    L11:
        this.cineHero.bind(r0.entry);
        return;
    L16:
        return;
    }

    private void queueStartupUpdate() {
        this.timer.removeCallbacks(this.startupUpdate);
        if (this.startupUpdatePending == true) goto L5;
        return;
    L5:
        if (this.catalogVisible == true) goto L7;
        return;
    L7:
        if (this.busy == true) goto L12;
        this.timer.postDelayed(this.startupUpdate, 300);
        return;
    }

    private void refreshSavedEpg() {
        loadSavedEpg();
    }

    private void refreshVisibleArtwork() {
        if (this.catalogVisible == true) goto L5;
        return;
    L5:
        if (this.catalogScrolling == false) goto L7;
        return;
    L7:
        if (isDestroyed() == false) goto L9;
        return;
    L9:
        if (this.list != null) goto L11;
        return;
    L11:
        if (this.adapter == null) goto L55;
        int r0 = this.list.getFirstVisiblePosition();
        int r1 = 0;
    L15:
        if (r1 >= this.list.getChildCount()) goto L56;
        int r2 = r0 + r1;
        if (r2 < 0) goto L41;
        if (r2 >= this.rows.size()) goto L41;
        Row r3 = this.rows.get(r2);
        if (r3.level >= 3) goto L41;
        View r4 = this.list.getChildAt(r1);
        if ((r4 instanceof PosterTile) == false) goto L28;
        ImageView r5 = ((PosterTile) r4).image;
    L31:
        if (r5 == null) goto L41;
        if ("Canales".equals(r3.entry.kind) == false) goto L36;
        this.logos.show(r5, this.catalogIndex.logos.candidates(r3.entry));
        goto L41
    L36:
        this.logos.show(r5, this.tmdb.poster(r3.entry), this.catalogIndex.logos.resolve(r3.entry));
        if (this.catalogVisible == false) goto L41;
        if (this.catalogScrolling == true) goto L41;
        this.tmdb.ensure(r3.entry, new MainActivity$$ExternalSyntheticLambda2(this));
        goto L41
    L28:
        if ((r4.getTag() instanceof Holder) == false) goto L30;
        r5 = ((Holder) r4.getTag()).image;
        goto L31
    L30:
        r5 = null;
    L41:
        r1 = r1 + 1;
        goto L15
    L56:
        return;
    }

    private void refreshVisibleEpg() {
        if (this.catalogVisible == true) goto L5;
        return;
    L5:
        if (isDestroyed() == false) goto L7;
        return;
    L7:
        if (this.list != null) goto L9;
        return;
    L9:
        if (this.adapter != null) goto L11;
        return;
    L11:
        if (this.page.equals("catalog") == true) goto L13;
        return;
    L13:
        if (this.kind.equals("Canales") == false) goto L36;
        int r0 = this.list.getFirstVisiblePosition();
        int r1 = 0;
    L17:
        if (r1 >= this.list.getChildCount()) goto L37;
        int r2 = r0 + r1;
        if (r2 < 0) goto L25;
        if (r2 >= this.rows.size()) goto L25;
        if (this.rows.get(r2).level != 0) goto L25;
        this.adapter.getView(r2, this.list.getChildAt(r1), this.list);
    L25:
        r1 = r1 + 1;
        goto L17
    L37:
        return;
    }

    private List<Entry> relatedEpisodes(Entry r8) {
        ArrayList r0 = new ArrayList();
        HashSet r1 = new HashSet();
        Iterator<Entry> r2 = this.catalogIndex.select("Series", this.providers.get(r8), seriesName(r8), null).iterator();
    L4:
        if (r2.hasNext() == false) goto L5;
        Entry r3 = r2.next();
        if (seriesName(r3).equals(seriesName(r8)) == false) goto L4;
        if (r1.add(CatalogCounts.episodeKey(r3)) == false) goto L4;
        r0.add(r3);
        goto L4
    L5:
        return r0;
    }

    private void relatedInfo(final Entry r2) {
        ParentalControl.requireEntry(this, r2, new MainActivity$$ExternalSyntheticLambda4(this, r2));
    }

    private void render() {
        this.previewActivation.reset();
        if (this.page.equals("home") == false) goto L7;
        showHome();
        return;
    L7:
        if (this.page.equals("settings") == false) goto L11;
        showSettings();
        return;
    L11:
        if (this.content != null) goto L14;
        return;
    L14:
        if (this.busy == false) goto L17;
        message("Cargando tu catálogo…", MUTED);
        return;
    L17:
        renderAsync();
    }

    private void renderAsync() {
        if (this.page.equals("catalog") == true) goto L5;
    L11:
        render();
        return;
    L5:
        if (this.busy == true) goto L11;
        if (this.content == null) goto L11;
        final CatalogRequest r0 = new CatalogRequest(this);
        final int r1 = this.catalogGeneration + 1;
        this.catalogGeneration = r1;
        this.catalogWorker.execute(new MainActivity$$ExternalSyntheticLambda52(this, r0, r1));
    }

    private void renderContinue() {
        renderAsync();
    }

    private void requestGuideForSection() {
        if (guideSectionVisible() == true) goto L5;
        return;
    L5:
        loadSavedEpg();
        if (this.savedEpgReady == false) goto L9;
        this.timer.removeCallbacks(this.guideRefresh);
        this.timer.postDelayed(this.guideRefresh, 1200);
        return;
    }

    private void restoreRoute(Bundle r3) {
        this.page = r3.getString("catalogPage", "home");
        this.kind = r3.getString("catalogKind", "Canales");
        this.provider = r3.getString("catalogProvider");
        this.series = r3.getString("catalogSeries");
        if (r3.containsKey("catalogSeason") == false) goto L5;
        Integer r0 = Integer.valueOf(r3.getInt("catalogSeason"));
    L6:
        this.season = r0;
        this.section = r3.getString("catalogSection", "Todas las secciones");
        this.filter = r3.getString("catalogFilter", "");
        this.favorites = r3.getBoolean("catalogFavorites");
        this.guide = r3.getBoolean("catalogGuide");
        this.continueMode = r3.getBoolean("catalogContinue");
        this.settingsSection = r3.getInt("catalogSettings", 0);
        this.pendingListState = r3.getParcelable("catalogScroll");
        return;
    L5:
        r0 = null;
        goto L6
    }

    private void restoreScroll() {
        if (this.pendingListState != null) goto L5;
        return;
    L5:
        if (this.list == null) goto L9;
        this.list.onRestoreInstanceState(this.pendingListState);
        this.pendingListState = null;
        return;
    }

    private void schedule(Entry r3) {
        startActivity(new Intent(this, EpgActivity.class).putExtra("entry", r3));
    }

    private List<Entry> seriesEpisodes(String r3) {
        return DemandEpisodes.load(this.catalogIndex, this.provider, r3);
    }

    private String seriesName(Entry r2) {
        if (r2.series == null) goto L9;
        if (r2.series.isEmpty() == true) goto L9;
        return r2.series;
    L9:
        return r2.group;
    }

    private void settingAction(String r2, final Runnable r3) {
        addCategoryAction(button(r2, new MainActivity$$ExternalSyntheticLambda42(r3)));
    }

    private GradientDrawable shape(int r4, int r5) {
        GradientDrawable r0 = new GradientDrawable();
        r0.setColor(AppTheme.color(this, r4));
        r0.setCornerRadius(m402dp(16));
        if (r5 == 0) goto L5;
        r0.setStroke(m402dp(1), AppTheme.color(this, r5));
    L5:
        return r0;
    }

    private void showHome() {
        this.page = "home";
        if (this.busy == false) goto L5;
        String r0 = "Cargando tu contenido…";
    L6:
        landingShell(r0);
        final String[] r1 = {"TV", "Películas", "Series", "EPG", "Plex"};
        final int[] r2 = {C1028R.drawable.home_tv3d95, C1028R.drawable.home_movie3d95, C1028R.drawable.home_series3d95, C1028R.drawable.home_epg3d95, C1028R.drawable.home_plex3d95};
        final ArrayList r3 = new ArrayList();
        this.homeDetails = r3;
        String[] r4 = {"Canales", "Películas", "Series"};
        int r7 = 0;
    L7:
        int r8 = 3;
        if (r7 >= 3) goto L18;
        String r9 = r4[r7];
        StringBuilder r10 = new StringBuilder().append(this.catalogIndex.count(r9));
        if (r9.equals("Canales") == false) goto L13;
        String r11 = " canales";
    L16:
        r3.add(r10.append(r11).toString());
        r7 = r7 + 1;
        goto L7
    L13:
        if (r9.equals("Series") == false) goto L15;
        r11 = " series";
        goto L16
    L15:
        r11 = " películas";
        goto L16
    L18:
        if (this.epg.count() != 0) goto L20;
        String r5 = "Guía de tus canales";
    L21:
        r3.add(r5);
        r3.add("Tus servidores y bibliotecas");
        ResponsiveHomeGrid r6 = new ResponsiveHomeGrid(this);
        if ((getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density) >= 600.0f) goto L25;
        r8 = 2;
    L25:
        r6.setNumColumns(r8);
        r6.setHorizontalSpacing(m402dp(12));
        r6.setVerticalSpacing(m402dp(12));
        r6.setClipToPadding(false);
        r6.setPadding(0, m402dp(4), 0, m402dp(12));
        r6.setAdapter(new C10034(this, r1, r3, r2));
        r6.setOnItemClickListener(new MainActivity$$ExternalSyntheticLambda41(this));
        CatalogNavigation.bind(r6, r6.getNumColumns());
        this.list = r6;
        this.root.addView(r6, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        this.root.addView(this.status);
        return;
    L20:
        r5 = this.epg.count() + " programas en tu guía";
        goto L21
    L5:
        r0 = "Tu entretenimiento, a un clic";
        goto L6
    }

    private void showSettings() {
        ParentalControl.authenticate(this, new MainActivity$$ExternalSyntheticLambda68(this));
    }

    private void showSettingsUnlocked() {
        this.page = "settings";
        landingShell("Ajustes");
        this.root.removeView(this.summary);
        ScrollView r1 = new ScrollView(this);
        LinearLayout r2 = new LinearLayout(this);
        r2.setOrientation(1);
        r2.setPadding(m402dp(16), m402dp(12), m402dp(16), m402dp(12));
        r1.addView(r2);
        this.root.addView(r1, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        String r5 = chromeSelection();
        TextView r6 = text(r5, 26, CYAN);
        r6.setTypeface(null, 1);
        r2.addView(r6);
        if (r5.equals("Ajustes") == false) goto L6;
        String r0 = "Configura CHDTV desde las categorías del panel lateral.";
    L9:
        TextView r3 = text(r0, 15, MUTED);
        r3.setPadding(0, m402dp(16), 0, m402dp(20));
        r2.addView(r3);
        clearCategoryActions();
        if (this.settingsSection != 0) goto L13;
        settingAction("Configurar TMDB", new MainActivity$$ExternalSyntheticLambda45(this));
        settingAction("Control parental", new MainActivity$$ExternalSyntheticLambda46(this));
        settingAction("Cargar lista", new MainActivity$$ExternalSyntheticLambda47(this));
        settingAction("Gestionar listas", new MainActivity$$ExternalSyntheticLambda48(this));
        settingAction("Token Movistar", new MainActivity$$ExternalSyntheticLambda49(this));
        settingAction("Token DAZN", new MainActivity$$ExternalSyntheticLambda50(this));
        r2.addView(text(this.catalog.sources.size() + " listas · " + this.catalog.size() + " títulos", 17, WHITE));
    L16:
        this.root.addView(this.status);
        return;
    L13:
        if (this.settingsSection != 3) goto L15;
        settingAction("Importar / configurar EPG", new MainActivity$$ExternalSyntheticLambda51(this));
        r2.addView(text(this.epg.count() + " programas en tu guía", 17, WHITE));
        goto L16
    L15:
        showNavigation();
        r2.addView(text("Selecciona Reproducción, EPG, Plex o Copias para ver sus herramientas aquí.", 17, WHITE));
        goto L16
    L6:
        if (r5.equals("Reproducción") == false) goto L8;
        r0 = "Importa y gestiona tus listas y la renovación de tus cuentas.";
        goto L9
    L8:
        r0 = "Gestiona la guía de programación y sus fuentes.";
        goto L9
    }

    private void sourceMenu(final Catalog.Source r8) {
        final boolean r0 = r8.origin.isEmpty();
        if (r0 == false) goto L5;
        String[] r1 = {"Renombrar", "Eliminar del catálogo"};
    L6:
        new AlertDialog.Builder(this).setTitle(PlaylistNames.title(r8, r8.originalFilename)).setItems(r1, new MainActivity$$ExternalSyntheticLambda44(this, r0, r8)).show();
        return;
    L5:
        r1 = new String[]{"Actualizar lista", "Renombrar", "Eliminar lista"};
        goto L6
    }

    private TextView text(String r3, int r4, int r5) {
        TextView r0 = new TextView(this);
        r0.setText(r3);
        r0.setTextSize(r4);
        r0.setTextColor(AppTheme.color(this, r5));
        r0.setFontFeatureSettings("kern");
        return r0;
    }

    private String time(long r4) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(r4));
    }

    private void updateHomeContent() {
        if (this.page.equals("home") == true) goto L5;
        return;
    L5:
        if (this.homeDetails != null) goto L7;
        return;
    L7:
        if ((this.list instanceof ResponsiveHomeGrid) == false) goto L66;
        this.homeDetails.clear();
        String[] r3 = {"Canales", "Películas", "Series"};
        int r4 = 0;
        int r5 = 0;
    L11:
        if (r5 >= 3) goto L12;
        String r6 = r3[r5];
        List<String> r7 = this.homeDetails;
        if (this.catalog.demand != null) goto L46;
    L48:
        StringBuilder r8 = new StringBuilder(String.valueOf(this.catalogIndex.count(r6)));
        if (r6.equals("Canales") == false) goto L52;
        String r9 = " canales";
    L55:
        String r10 = r8.append(r9).toString();
    L56:
        r7.add(r10);
        r5 = r5 + 1;
        goto L11
    L52:
        if (r6.equals("Series") == false) goto L54;
        r9 = " series";
        goto L55
    L54:
        r9 = " películas";
        goto L55
    L46:
        if (this.catalog.demand.knownCounts == true) goto L48;
        r10 = "Se carga al entrar";
        goto L56
    L12:
        List<String> r0 = this.homeDetails;
        if (this.epg.count() != 0) goto L15;
        String r11 = "Guía de tus canales";
    L16:
        r0.add(r11);
        this.homeDetails.add("Tus servidores y bibliotecas");
        if (this.summary == null) goto L23;
        TextView r1 = this.summary;
        if (this.busy == false) goto L21;
        String r12 = "Cargando tu contenido…";
    L22:
        r1.setText(r12);
        goto L23
    L21:
        r12 = "Tu entretenimiento, a un clic";
    L23:
        String[] r13 = {"TV", "Películas", "Series", "EPG", "Plex"};
    L25:
        if (r4 >= this.list.getChildCount()) goto L26;
        int r2 = this.list.getFirstVisiblePosition() + r4;
        if (r2 < 0) goto L42;
        if (r2 >= this.homeDetails.size()) goto L42;
        View r14 = this.list.getChildAt(r4);
        if ((r14 instanceof CineHomeTile) == false) goto L36;
        TextView r15 = ((CineHomeTile) r14).detail;
    L39:
        if (r15 == null) goto L41;
        r15.setText(this.homeDetails.get(r2));
    L41:
        r14.setContentDescription(String.valueOf(r13[r2]) + " · " + this.homeDetails.get(r2));
        goto L42
    L36:
        if ((r14 instanceof HomeTile95) == false) goto L38;
        r15 = ((HomeTile95) r14).detail;
        goto L39
    L38:
        r15 = null;
    L42:
        r4 = r4 + 1;
        goto L25
    L26:
        return;
    L15:
        r11 = String.valueOf(this.epg.count()) + " programas en tu guía";
        goto L16
    }

    private void urlDialog(final boolean r5) {
        final EditText r0 = new EditText(this);
        r0.setSingleLine();
        r0.setInputType(17);
        if (r5 == false) goto L5;
        String r1 = "URL XMLTV o enlace de Dropbox";
    L6:
        r0.setHint(r1);
        if (r5 == false) goto L9;
        String r2 = new EpgRepository(this).url();
    L10:
        r0.setText(r2);
        AlertDialog.Builder r3 = new AlertDialog.Builder(this);
        if (r5 == false) goto L13;
        String r4 = "URL de la EPG XMLTV";
    L14:
        r3.setTitle(r4).setView(r0).setPositiveButton("Cargar", new MainActivity$$ExternalSyntheticLambda5(this, r0, r5)).setNegativeButton("Cancelar", null).show();
        return;
    L13:
        r4 = "URL de la lista M3U";
        goto L14
    L9:
        r2 = "";
        goto L10
    L5:
        r1 = "URL M3U o enlace compartido de Dropbox";
        goto L6
    }

    private CatalogPlan withContinue(CatalogPlan r9, CatalogRequest r10) {
        if (r10.provider != null) goto L5;
    L14:
        return r9;
    L5:
        if (r10.series != null) goto L14;
        if ("Canales".equals(r10.kind) == true) goto L14;
        List<WatchHistory.Item> r11 = r10.index.history(this, r10.kind, r10.provider);
        if (r11.isEmpty() == false) goto L13;
        return r9;
    L13:
        return new CatalogPlan(DemandRows.prepend(new Row("Seguir viendo", String.valueOf(r11.size()) + " títulos pendientes", null, r11.get(0).entry, 4), r9.rows), r9.choices, r9.section);
    }

    void batchFailed(Exception r2) {
        failure("No se pudo importar el TXT", r2);
    }

    void batchFinished(Catalog r2, String r3) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.catalog = r2;
        this.catalogIndex = AppStorage.indexFor(r2);
        this.entries = this.catalogIndex.entries;
        this.providers = this.catalogIndex.providers;
        this.busy = false;
        if (this.page.equals("home") == false) goto L8;
        updateHomeContent();
    L9:
        new AlertDialog.Builder(this).setTitle("Importación TXT terminada").setMessage(r3).setPositiveButton("Aceptar", null).show();
        refreshSavedEpg();
        return;
    L8:
        renderAsync();
        goto L9
    }

    InputStream batchOpen(String r1, Uri r2, long r3) throws IOException {
        return open(r1, r2, r3);
    }

    void batchProgress(String r2) {
        if (isDestroyed() == true) goto L6;
        message(r2, ORANGE);
        return;
    }

    @Override // es.antonio.m3u.ChdtvScreen
    protected String chromeSelection() {
        if (this.page.equals("catalog") == false) goto L10;
        if (this.kind.equals("Canales") == false) goto L8;
        return "TV";
    L8:
        return this.kind;
    L10:
        if (this.page.equals("settings") == true) goto L12;
        return "Inicio";
    L12:
        return new String[]{"Reproducción", "Reproducción", "Reproducción", "Listas y EPG", "Reproducción", "Copia de seguridad", "Reproducción", "Reproducción", "Ajustes"}[this.settingsSection];
    }

    void epg216Apply(EpgData r2) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        if (isFinishing() == true) goto L35;
        this.epg = r2;
        this.savedEpgReady = true;
        if (this.catalogVisible == true) goto L11;
        return;
    L11:
        if ("home".equals(this.page) == false) goto L13;
        updateHomeContent();
    L15:
        if (this.cineHero != null) goto L17;
        return;
    L17:
        if (this.list != null) goto L19;
        return;
    L19:
        if ("Canales".equals(this.kind) == false) goto L31;
        int r3 = this.list.getSelectedItemPosition();
        if (r3 >= 0) goto L23;
        return;
    L23:
        if (r3 < this.rows.size()) goto L25;
        return;
    L25:
        if (this.rows.get(r3).level != 0) goto L34;
        previewRow(r3);
        return;
    L34:
        return;
    L31:
        return;
    L13:
        refreshVisibleEpg();
        goto L15
    }

    boolean isSettingsPage() {
        return this.page.equals("settings");
    }

    @Override // android.app.Activity
    protected void onActivityResult(int r4, int r5, Intent r6) {
        super.onActivityResult(r4, r5, r6);
        if (r5 != (-1)) goto L24;
        if (r6 != null) goto L6;
        return;
    L6:
        if (r6.getData() == null) goto L29;
        Uri r0 = r6.getData();
        getContentResolver().takePersistableUriPermission(r0, 1);     // Catch: SecurityException -> L11
    L13:
        if (r4 != 10) goto L16;
        loadList(null, r0, this.importKind, null);
        return;
    L16:
        if (r4 != 11) goto L19;
        loadEpg(null, r0);
        return;
    L19:
        if (r4 != 12) goto L28;
        loadBatch(r0);
        return;
    L28:
        return;
    L29:
        return;
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    public void onBackPressed() {
        if (this.catalogSearch == null) goto L9;
        if (this.catalogSearch.hasFocus() == false) goto L9;
        SearchKeyboard.hide(this, this.catalogSearch, this.list);
        return;
    L9:
        if (closeNavigation() == false) goto L12;
        return;
    L12:
        if (this.page.equals("settings") == false) goto L20;
        if (this.settingsSidebar == null) goto L20;
        if (this.settingsSidebar.getVisibility() != 0) goto L20;
        this.settingsSidebar.setVisibility(8);
        this.settingsToggle.setContentDescription("Mostrar menú");
        this.settingsToggle.requestFocus();
        return;
    L20:
        if (this.page.equals("home") == true) goto L22;
        navigateBack();
        return;
    L22:
        super.onBackPressed();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration r3) {
        super.onConfigurationChanged(r3);
        if (this.cineHero == null) goto L6;
        ViewGroup.LayoutParams r0 = this.cineHero.getLayoutParams();
        r0.height = CineHero.previewLayout(this).height;
        this.cineHero.setLayoutParams(r0);
    L6:
        if (this.root == null) goto L9;
        this.root.requestLayout();
        return;
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    public void onCreate(Bundle r5) {
        super.onCreate(r5);
        if ((getApplication() instanceof ChdtvApplication) == false) goto L6;
        ((ChdtvApplication) getApplication()).catalogLoading(this);
    L6:
        if (r5 == null) goto L9;
        boolean r2 = false;
        if (r5.getBoolean("startupUpdatePending", false) == true) goto L9;
    L10:
        this.startupUpdatePending = r2;
        queueStartupUpdate();
        this.storage = new AppStorage(this);
        this.logos = new LogoLoader(this);
        this.tmdb = new TmdbService(this, null, true);
        this.tmdbUi = new TmdbUi(this, this.tmdb, this.logos, new MainActivity$$ExternalSyntheticLambda6(this));
        if (r5 != null) goto L13;
    L20:
        showHome();
    L21:
        if (r5 == null) goto L23;
    L25:
        if (r5 == null) goto L27;
    L29:
        final long r0 = SystemClock.elapsedRealtime();
        BlackBox.event("CATALOG_LOAD_START", "");
        this.worker.execute(new MainActivity$$ExternalSyntheticLambda7(this, r0));
        return;
    L27:
        if (getIntent().hasExtra("chromeRoute") == false) goto L29;
        openChromeRoute(getIntent().getStringExtra("chromeRoute"));
        goto L29
    L23:
        if (getIntent().hasExtra("cineSection") == false) goto L25;
        openCineSection(getIntent().getStringExtra("cineSection"));
        goto L25
    L13:
        if (r5.containsKey("catalogPage") == false) goto L20;
        restoreRoute(r5);
        if (this.page.equals("catalog") == false) goto L18;
        buildUi();
        renderAsync();
        goto L21
    L18:
        if (this.page.equals("settings") == false) goto L20;
        showSettingsUnlocked();
    L9:
        r2 = true;
        goto L10
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        DialogHandoff.close(this);
        this.catalogGeneration++;
        this.catalogWorker.getQueue().clear();
        this.catalogWorker.shutdownNow();
        this.epgWorker.shutdownNow();
        this.worker.shutdownNow();
        this.logos.close();
        this.tmdb.close();
        this.timer.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent r3) {
        super.onNewIntent(r3);
        setIntent(r3);
        if (r3.hasExtra("chromeRoute") == false) goto L6;
        String r1 = r3.getStringExtra("chromeRoute");
        r3.removeExtra("chromeRoute");
        openChromeRoute(r1);
    L6:
        if (r3.hasExtra("chromeTool") == false) goto L9;
        String r2 = r3.getStringExtra("chromeTool");
        r3.removeExtra("chromeTool");
        openSettingsTool(r2);
    L9:
        if (r3.hasExtra("cineSection") == false) goto L12;
        openCineSection(r3.getStringExtra("cineSection"));
        r3.removeExtra("cineSection");
        return;
    }

    @Override // android.app.Activity
    protected void onPause() {
        this.catalogVisible = false;
        this.tmdb.pauseAutomatic();
        this.logos.pause();
        if (this.cineHero == null) goto L5;
        this.cineHero.suspend();
    L5:
        this.timer.removeCallbacks(this.guideRefresh);
        this.timer.removeCallbacks(this.startupUpdate);
        this.timer.removeCallbacks(this.artworkRefresh);
        super.onPause();
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.catalogVisible = true;
        this.tmdb.resumeAutomatic();
        this.logos.resume();
        requestGuideForSection();
        queueStartupUpdate();
        this.playbackLaunching = false;
        this.playbackRequestedAt = 0;
        if (this.deferredPlan == null) goto L13;
        CatalogPlan r1 = this.deferredPlan;
        CatalogRequest r2 = this.deferredRequest;
        this.deferredPlan = null;
        this.deferredRequest = null;
        if (this.page.equals("catalog") == false) goto L13;
        if (r2.matches(this) == false) goto L13;
        if (r2.continuing == false) goto L11;
        applyContinue(r1);
    L12:
        restoreScroll();
        goto L13
    L11:
        applyCatalog(r1);
    L13:
        Runnable r3 = new MainActivity$$ExternalSyntheticLambda16(this);
        if (this.page.equals("settings") == false) goto L16;
        ParentalControl.authenticate(this, r3, true);
        return;
    L16:
        ParentalControl.require(this, r3);
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle r3) {
        super.onSaveInstanceState(r3);
        r3.putBoolean("startupUpdatePending", this.startupUpdatePending);
        r3.putString("catalogPage", this.page);
        r3.putString("catalogKind", this.kind);
        r3.putString("catalogProvider", this.provider);
        r3.putString("catalogSeries", this.series);
        if (this.season == null) goto L5;
        r3.putInt("catalogSeason", this.season.intValue());
    L5:
        r3.putString("catalogSection", this.section);
        r3.putString("catalogFilter", this.filter);
        r3.putBoolean("catalogFavorites", this.favorites);
        r3.putBoolean("catalogGuide", this.guide);
        r3.putBoolean("catalogContinue", this.continueMode);
        r3.putInt("catalogSettings", this.settingsSection);
        if (this.list == null) goto L9;
        r3.putParcelable("catalogScroll", this.list.onSaveInstanceState());
        return;
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        this.timer.postDelayed(this.refresh, 60000);
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    protected void onStop() {
        this.timer.removeCallbacks(this.refresh);
        super.onStop();
    }

    void openChromeRoute(String r2) {
        if (r2.equals("Temas") == false) goto L7;
        AppTheme.chooser(this, new MainActivity$$ExternalSyntheticLambda10(this));
        return;
    L7:
        if (r2.equals("Inicio") == false) goto L10;
        showHome();
        return;
    L10:
        showNavigationCategory(r2);
    }

    void openCineSection(String r2) {
        openSection(r2, false);
    }

    void openSettingsTool(String r2) {
        if (r2.equals("Ventana flotante al salir") == false) goto L6;
        PipPlayback.settings(this);
        return;
    L6:
        if (r2.equals("Configurar TMDB") == false) goto L9;
        this.tmdbUi.settings();
        return;
    L9:
        if (r2.equals("Control parental") == false) goto L12;
        ParentalControl.settings(this);
        return;
    L12:
        if (r2.equals("Cargar lista") == false) goto L15;
        importMenu();
        return;
    L15:
        if (r2.equals("Gestionar listas") == false) goto L18;
        manageLists();
        return;
    L18:
        if (r2.equals("Importar / configurar EPG") == false) goto L26;
        epgMenu();
        return;
    }
}
