package es.antonio.m3u;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Process;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.media3.extractor.p001ts.TsExtractor;
import com.arthenica.ffmpegkit.MediaInformation;
import com.google.common.net.HttpHeaders;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/* JADX INFO: loaded from: classes3.dex */
public final class PlexActivity extends ChdtvScreen {
    private BaseAdapter adapter;
    private final List<Element> all;
    private String base;
    private boolean busy;
    private CineHero cineHero;
    private final PlexClient client;
    private boolean compact;
    private String displayedScope;
    private Button favoriteToggle;
    private boolean favoritesOnly;
    private int filterGeneration;
    private String filterSignature;
    private final ThreadPoolExecutor filterWorker;
    private int generation;
    private GridView grid;
    private final ArrayDeque<String> history;
    private ArrayList<PlexImport.Server> imported;
    private LogoLoader logos;
    private Button moreButton;
    private int offset;
    private String path;
    private final Map<String, String> pathTitles;
    private final Map<String, WatchHistory.Item> pending;
    private boolean playbackLaunching;
    private final PreviewActivation previewActivation;
    private LinearLayout root;
    private boolean screenVisible;
    private EditText search;
    private final Runnable searchFilter;
    private final Handler searchHandler;
    private LinearLayout searchRow;
    private Button searchToggle;
    private final List<Element> shown;
    private TextView status;
    private TmdbService tmdb;
    private String token;
    private boolean toolsOnly;
    private boolean watching;
    private final ExecutorService worker;

    private static final class PlexFilterPlan {
        final Map<String, WatchHistory.Item> pending;
        final List<Element> rows;

        private PlexFilterPlan() {
            this.rows = new ArrayList();
            this.pending = new HashMap();
        }

        /* synthetic */ PlexFilterPlan(PlexActivityIA r1) {
            this();
        }
    }

    private static final class PlexFilterRequest {
        final String base;
        final boolean favorites;
        final int offset;
        final String path;
        final List<Element> rows;
        final Set<Element> starred;
        final String term;
        final boolean watching;

        PlexFilterRequest(PlexActivity r4) {
            this.base = PlexActivity.m721$$Nest$fgetbase(r4);
            this.path = PlexActivity.m725$$Nest$fgetpath(r4);
            this.term = PlexActivity.m727$$Nest$fgetsearch(r4).getText().toString().toLowerCase(Locale.ROOT);
            this.offset = PlexActivity.m724$$Nest$fgetoffset(r4);
            this.watching = PlexActivity.m732$$Nest$fgetwatching(r4);
            this.favorites = PlexActivity.m722$$Nest$fgetfavoritesOnly(r4);
            this.rows = new ArrayList(PlexActivity.m720$$Nest$fgetall(r4));
            this.starred = Collections.newSetFromMap(new IdentityHashMap());
            if (this.favorites == false) goto L10;
            Iterator<Element> r0 = this.rows.iterator();
        L6:
            if (r0.hasNext() == false) goto L16;
            Element r1 = r0.next();
            if (PlexActivity.m734$$Nest$misFavorite(r4, r1) == false) goto L6;
            this.starred.add(r1);
            goto L6
        L16:
            return;
        }

        boolean matches(PlexActivity r4) {
            if (this.base.equals(PlexActivity.m721$$Nest$fgetbase(r4)) == true) goto L5;
        L15:
            return false;
        L5:
            if (this.path.equals(PlexActivity.m725$$Nest$fgetpath(r4)) == false) goto L15;
            if (this.term.equals(PlexActivity.m727$$Nest$fgetsearch(r4).getText().toString().toLowerCase(Locale.ROOT)) == false) goto L15;
            if (this.offset != PlexActivity.m724$$Nest$fgetoffset(r4)) goto L15;
            if (this.watching != PlexActivity.m732$$Nest$fgetwatching(r4)) goto L15;
            if (this.favorites != PlexActivity.m722$$Nest$fgetfavoritesOnly(r4)) goto L15;
            return true;
        }

        String scope() {
            return this.base + "\n" + this.path + "\n" + this.offset + "\n" + this.watching;
        }

        String signature() {
            return this.base + "\n" + this.path + "\n" + this.offset + "\n" + this.term + "\n" + this.watching + "\n" + this.favorites;
        }
    }

    private interface Work {
        void run() throws Exception;
    }

    /* JADX INFO: renamed from: $r8$lambda$-ZEjQ_m-dJcCzR8apyKoJ3p7qoQ, reason: not valid java name */
    public static /* synthetic */ void m709$r8$lambda$ZEjQ_mdJcCzR8apyKoJ3p7qoQ(PlexActivity r0, int r1, Exception r2) {
        r0.lambda$task$35(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$-ZuI2CQAhCnw44ukbCUCR0zPLZM, reason: not valid java name */
    public static /* synthetic */ void m710$r8$lambda$ZuI2CQAhCnw44ukbCUCR0zPLZM(PlexActivity r0, View r1) {
        r0.lambda$onCreate$10(r1);
    }

    public static /* synthetic */ void $r8$lambda$1Xv2h7otwLXCs8EjWdJIbQeUGaw(PlexActivity r0, Entry r1) {
        r0.lambda$playEntry$30(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$1fbnw9fGa-NM2ehGjp4C1VrL3Us, reason: not valid java name */
    public static /* synthetic */ void m711$r8$lambda$1fbnw9fGaNM2ehGjp4C1VrL3Us(PlexActivity r0, String r1) {
        r0.lambda$load$40(r1);
    }

    public static /* synthetic */ void $r8$lambda$2aFHrYb3YaM29qOnDnqZDPY2Z8I(PlexActivity r0, int r1, PlexFilterRequest r2, PlexFilterPlan r3) {
        r0.lambda$filterAsync$28(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$2nZPcT_dsWWL576unaxmd7KjW10(PlexActivity r0, DialogInterface r1, int r2) {
        r0.lambda$connect$43(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$9Gojkpy27hfFbT4mgOk0eTIKVSA(PlexActivity r0, String r1) {
        r0.lambda$importMenu$20(r1);
    }

    public static /* synthetic */ void $r8$lambda$AVSukuHmhP51zH2PxHujxB90ldA(PlexActivity r0, List r1) {
        r0.lambda$publish$38(r1);
    }

    public static /* synthetic */ void $r8$lambda$EEj_oZgxEV18OMwy0DA_4a8JNHg(PlexActivity r0, View r1) {
        r0.lambda$onCreate$3(r1);
    }

    public static /* synthetic */ void $r8$lambda$HEENqQdPSAYMR9kWo3rO8SdsMJ4(PlexActivity r0, View r1) {
        r0.lambda$onCreate$6(r1);
    }

    public static /* synthetic */ void $r8$lambda$JfgN8YCC3seOkivUMC92DGYxJwA(PlexActivity r0, Entry r1) {
        r0.lambda$showPlexDetail$48(r1);
    }

    public static /* synthetic */ void $r8$lambda$P9MkyAaN1IJCUZh8gshzLmyKFNY(PlexActivity r0, WatchHistory.Item r1, DialogInterface r2, int r3) {
        r0.lambda$onCreate$13(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$Pe7fY_pmfNPw0NfSPbiLCHTE46E(PlexActivity r0, String r1, Element r2, String r3) {
        r0.lambda$open$47(r1, r2, r3);
    }

    /* JADX INFO: renamed from: $r8$lambda$QDos5ppf-HSPGFEN6-qa9HkO1vU, reason: not valid java name */
    public static /* synthetic */ void m712$r8$lambda$QDos5ppfHSPGFEN6qa9HkO1vU(PlexActivity r0) {
        r0.lambda$onCreate$15();
    }

    public static /* synthetic */ void $r8$lambda$RxgiJbamHfofmwsDykPCpOMMtN4(PlexActivity r0, long r1, Entry r3) {
        r0.lambda$launch$32(r1, r3);
    }

    /* JADX INFO: renamed from: $r8$lambda$SgYjuJKUVK4lp3U_-ZsIi72fdcY, reason: not valid java name */
    public static /* synthetic */ void m713$r8$lambda$SgYjuJKUVK4lp3U_ZsIi72fdcY(PlexActivity r0, Element r1) {
        r0.lambda$open$45(r1);
    }

    public static /* synthetic */ void $r8$lambda$UyH0OC1Gky9bCOfE9myexMaIVfU(PlexActivity r0, Element r1) {
        r0.lambda$open$44(r1);
    }

    public static /* synthetic */ void $r8$lambda$W8xMM04VzDjyiLC_XxpWyaXMtzU(PlexActivity r0) {
        r0.lambda$onCreate$17();
    }

    public static /* synthetic */ void $r8$lambda$ZZoZHuV4xWk1ISDUDNO_mGT5RPw(PlexActivity r0, Entry r1, String r2) {
        r0.lambda$resumeSaved$34(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$_Pg3bA6QOMMHvegMopQ78PEabsc(PlexActivity r0, Entry r1) {
        r0.playEntry(r1);
    }

    public static /* synthetic */ boolean $r8$lambda$_s6bm0paLyJsXVFEgovCxlJzNqs(PlexActivity r0, AdapterView r1, View r2, int r3, long r4) {
        return r0.lambda$onCreate$14(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$_ya81CiHE_9Z51pOp9xR8I2l4mk(PlexActivity r0, PlexFilterRequest r1, long r2, int r4) {
        r0.lambda$filterAsync$29(r1, r2, r4);
    }

    /* JADX INFO: renamed from: $r8$lambda$bUOJ-P8bZO0huFKoUL-D13qbvLM, reason: not valid java name */
    public static /* synthetic */ void m714$r8$lambda$bUOJP8bZO0huFKoULD13qbvLM(PlexActivity r0, EditText r1, DialogInterface r2, int r3) {
        r0.lambda$connect$42(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$bZ7iJgLONEQhXhPVddQ0RODaHQQ(PlexActivity r0, Uri r1) {
        r0.lambda$onActivityResult$23(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$fcx7nkcMCogP82NmLP-lO2VqSRI, reason: not valid java name */
    public static /* synthetic */ void m715$r8$lambda$fcx7nkcMCogP82NmLPlO2VqSRI(PlexActivity r0, Entry r1) {
        r0.lambda$resumeSaved$33(r1);
    }

    public static /* synthetic */ void $r8$lambda$fgSNCMGvptQefRZrZQTcvP6k1Ns(PlexActivity r0) {
        r0.lambda$onCreate$9();
    }

    public static /* synthetic */ void $r8$lambda$h5IgOEPJJfseWeQtiC6J85s0ks0(PlexActivity r0, Entry r1) {
        r0.lambda$playEntry$31(r1);
    }

    public static /* synthetic */ void $r8$lambda$hXbW9lm47TR5E9RepPSpD7XN72M(PlexActivity r0, DialogInterface r1, int r2) {
        r0.lambda$importMenu$22(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$jrhg49_O0CY55wLOH0IftvO-fCM, reason: not valid java name */
    public static /* synthetic */ void m716$r8$lambda$jrhg49_O0CY55wLOH0IftvOfCM(PlexActivity r0) {
        r0.connect();
    }

    public static /* synthetic */ void $r8$lambda$lCISVUr_AkTsJhmwUovz7Mm2RnM(PlexActivity r0, EditText r1, DialogInterface r2, int r3) {
        r0.lambda$importMenu$21(r1, r2, r3);
    }

    /* JADX INFO: renamed from: $r8$lambda$lvpmUyiXQVFpbaE-UTcILs78g0A, reason: not valid java name */
    public static /* synthetic */ void m717$r8$lambda$lvpmUyiXQVFpbaEUTcILs78g0A(PlexActivity r0, ArrayList r1) {
        r0.lambda$importStream$24(r1);
    }

    public static /* synthetic */ void $r8$lambda$nySJj147jG0sBcj7u5OD_He4OiM(PlexActivity r0, View r1) {
        r0.lambda$onCreate$7(r1);
    }

    public static /* synthetic */ void $r8$lambda$oRGFm43m5X9hWzxCRHtm6tyJx_8(PlexActivity r0, Work r1, int r2) {
        r0.lambda$task$37(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$oXPRxD8haI1GBVcOIlvjmxx6-fc, reason: not valid java name */
    public static /* synthetic */ void m718$r8$lambda$oXPRxD8haI1GBVcOIlvjmxx6fc(PlexActivity r0, AdapterView r1, View r2, int r3, long r4) {
        r0.lambda$onCreate$11(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$o_ySmI_j0fSrZbh9xliBYx_5CIU(PlexActivity r0) {
        r0.lambda$onResume$51();
    }

    public static /* synthetic */ void $r8$lambda$q4C4wCAGoTvWaQon4GVmJQS0U6k(PlexActivity r0, int r1) {
        r0.lambda$task$36(r1);
    }

    public static /* synthetic */ void $r8$lambda$qm2H1GmmgKBk81CHoY9Hy0vA9cU(PlexActivity r0, Entry r1, Element r2) {
        r0.lambda$showPlexDetail$50(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$rGundvTHqWptHqFi_6hgdKm6tbs(PlexActivity r0, View r1) {
        r0.lambda$onCreate$2(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$rxJzbFucpARg3N4Lt4T-d8XFaW8, reason: not valid java name */
    public static /* synthetic */ void m719$r8$lambda$rxJzbFucpARg3N4Lt4Td8XFaW8(PlexActivity r0) {
        r0.lambda$discover$39();
    }

    public static /* synthetic */ void $r8$lambda$sOH57VhaRaKoINjsWI8KaBOG2u0(PlexActivity r0) {
        r0.lambda$new$25();
    }

    public static /* synthetic */ void $r8$lambda$saPx6drlnpCGRygNtt2WtolrXvw(PlexActivity r0, String r1) {
        r0.lambda$load$41(r1);
    }

    public static /* synthetic */ void $r8$lambda$u7mFHxQ4VsbP_6oZJyI_1VrQMR0(PlexActivity r0) {
        r0.importMenu();
    }

    public static /* synthetic */ void $r8$lambda$w7LqiPvLVG2XRwfvqq161eqHnng(PlexActivity r0, Entry r1, Element r2) {
        r0.lambda$open$46(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$wB92i9BJsvjWpn4jQ_jMkPuRYTQ(PlexActivity r0, Element r1, DialogInterface r2, int r3) {
        r0.lambda$onCreate$12(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$wLsBervztsTUNQix91FMPd2s9yw(PlexActivity r0, DialogInterface r1, int r2) {
        r0.lambda$onCreate$4(r1, r2);
    }

    public static /* synthetic */ View $r8$lambda$x5iHyR7IXAwoYl1S2ztgCe4J27I(PlexActivity r0) {
        return r0.lambda$onCreate$8();
    }

    public static /* synthetic */ void $r8$lambda$xBxLOY_oFv4XCe_eldpaD4tUW4I(PlexActivity r0, View r1) {
        r0.lambda$onCreate$5(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$fgetall, reason: not valid java name */
    static /* bridge */ /* synthetic */ List m720$$Nest$fgetall(PlexActivity r0) {
        return r0.all;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetbase, reason: not valid java name */
    static /* bridge */ /* synthetic */ String m721$$Nest$fgetbase(PlexActivity r0) {
        return r0.base;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetfavoritesOnly, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m722$$Nest$fgetfavoritesOnly(PlexActivity r0) {
        return r0.favoritesOnly;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetlogos, reason: not valid java name */
    static /* bridge */ /* synthetic */ LogoLoader m723$$Nest$fgetlogos(PlexActivity r0) {
        return r0.logos;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetoffset, reason: not valid java name */
    static /* bridge */ /* synthetic */ int m724$$Nest$fgetoffset(PlexActivity r0) {
        return r0.offset;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetpath, reason: not valid java name */
    static /* bridge */ /* synthetic */ String m725$$Nest$fgetpath(PlexActivity r0) {
        return r0.path;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetpending, reason: not valid java name */
    static /* bridge */ /* synthetic */ Map m726$$Nest$fgetpending(PlexActivity r0) {
        return r0.pending;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetsearch, reason: not valid java name */
    static /* bridge */ /* synthetic */ EditText m727$$Nest$fgetsearch(PlexActivity r0) {
        return r0.search;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetsearchFilter, reason: not valid java name */
    static /* bridge */ /* synthetic */ Runnable m728$$Nest$fgetsearchFilter(PlexActivity r0) {
        return r0.searchFilter;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetsearchHandler, reason: not valid java name */
    static /* bridge */ /* synthetic */ Handler m729$$Nest$fgetsearchHandler(PlexActivity r0) {
        return r0.searchHandler;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetshown, reason: not valid java name */
    static /* bridge */ /* synthetic */ List m730$$Nest$fgetshown(PlexActivity r0) {
        return r0.shown;
    }

    /* JADX INFO: renamed from: -$$Nest$fgettoken, reason: not valid java name */
    static /* bridge */ /* synthetic */ String m731$$Nest$fgettoken(PlexActivity r0) {
        return r0.token;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetwatching, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m732$$Nest$fgetwatching(PlexActivity r0) {
        return r0.watching;
    }

    /* JADX INFO: renamed from: -$$Nest$mdp, reason: not valid java name */
    static /* bridge */ /* synthetic */ int m733$$Nest$mdp(PlexActivity r0, int r1) {
        return r0.m406dp(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$misFavorite, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m734$$Nest$misFavorite(PlexActivity r0, Element r1) {
        return r0.isFavorite(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$mshowCineSelection, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m735$$Nest$mshowCineSelection(PlexActivity r0, int r1) {
        r0.showCineSelection(r1);
    }

    public PlexActivity() {
        this.previewActivation = new PreviewActivation();
        this.worker = Executors.newSingleThreadExecutor(new PlexActivity$$ExternalSyntheticLambda26());
        this.client = new PlexClient();
        this.all = new ArrayList();
        this.shown = new ArrayList();
        this.base = "";
        this.token = "";
        this.path = "";
        this.pathTitles = new HashMap();
        this.history = new ArrayDeque();
        this.watching = false;
        this.pending = new HashMap();
        this.imported = new ArrayList();
        this.searchHandler = new Handler(Looper.getMainLooper());
        this.searchFilter = new PlexActivity$$ExternalSyntheticLambda27(this);
        this.filterWorker = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, new ArrayBlockingQueue(1), new PlexActivity$$ExternalSyntheticLambda28(), new ThreadPoolExecutor.DiscardOldestPolicy());
        this.screenVisible = true;
        this.filterSignature = "";
        this.displayedScope = "";
    }

    private void addTool(String r3, final Runnable r4) {
        Button r0 = new Button(this);
        r0.setText(r3);
        r0.setOnClickListener(new PlexActivity$$ExternalSyntheticLambda43(r4));
        addCategoryAction(r0);
    }

    private void applyFilter(PlexFilterRequest r8, PlexFilterPlan r9) {
        boolean r0 = r8.signature().equals(this.filterSignature);
        if (r0 == true) goto L5;
    L7:
        boolean r3 = false;
    L8:
        if (r0 == false) goto L10;
        Parcelable r4 = this.grid.onSaveInstanceState();
    L11:
        this.previewActivation.reset();
        this.filterSignature = r8.signature();
        this.displayedScope = r8.scope();
        this.grid.setEnabled(true);
        if (this.moreButton == null) goto L18;
        Button r1 = this.moreButton;
        if (r8.watching == false) goto L16;
        int r5 = 8;
    L17:
        r1.setVisibility(r5);
        goto L18
    L16:
        r5 = 0;
    L18:
        this.shown.clear();
        this.shown.addAll(r9.rows);
        this.pending.clear();
        this.pending.putAll(r9.pending);
        if (this.searchToggle == null) goto L22;
        this.searchToggle.setText("⌕");
    L22:
        if (this.favoriteToggle == null) goto L33;
        Button r2 = this.favoriteToggle;
        if (this.favoritesOnly == false) goto L26;
        String r6 = "★";
    L27:
        r2.setText(r6);
        Button r7 = this.favoriteToggle;
        if (this.favoritesOnly == false) goto L30;
        String r10 = "Mostrar todo Plex";
    L31:
        r7.setContentDescription(r10);
        goto L33
    L30:
        r10 = "Mostrar favoritos Plex";
        goto L31
    L26:
        r6 = "☆";
    L33:
        if (this.watching == false) goto L35;
        this.status.setText("Seguir viendo · " + this.shown.size() + " títulos pendientes · Plex");
    L35:
        if (r3 == true) goto L37;
        this.adapter.notifyDataSetChanged();
    L37:
        if (r4 == null) goto L40;
        if (r3 == true) goto L40;
        this.grid.onRestoreInstanceState(r4);
    L42:
        if (r3 == false) goto L45;
        refreshVisibleCards();
    L45:
        if (this.cineHero == null) goto L48;
        showCineSelection(Math.max(0, this.grid.getSelectedItemPosition()));
        return;
    L48:
        return;
    L40:
        if (r0 == true) goto L42;
        this.grid.setSelection(0);
        goto L42
    L10:
        r4 = null;
        goto L11
    L5:
        if (this.shown.equals(r9.rows) == false) goto L7;
        r3 = true;
        goto L8
    }

    static String artwork(Element r5) {
        String[] r0 = {"thumb", "parentThumb", "grandparentThumb", "art", "composite"};
        int r1 = 0;
    L4:
        if (r1 >= 5) goto L9;
        String r3 = r5.getAttribute(r0[r1]);
        if (r3.isEmpty() == false) goto L7;
        r1 = r1 + 1;
        goto L4
    L7:
        return r3;
    L9:
        return "";
    }

    private void connect() {
        final EditText r0 = new EditText(this);
        r0.setSingleLine();
        r0.setInputType(TsExtractor.TS_STREAM_TYPE_AC3);
        r0.setHint("Token de tu cuenta Plex");
        new AlertDialog.Builder(this).setTitle("Conectar Plex").setView(r0).setPositiveButton("Conectar", new PlexActivity$$ExternalSyntheticLambda50(this, r0)).setNeutralButton("Desconectar", new PlexActivity$$ExternalSyntheticLambda51(this)).setNegativeButton("Cancelar", null).show();
    }

    private String currentScope() {
        return this.base + "\n" + this.path + "\n" + this.offset + "\n" + this.watching;
    }

    private void discover() {
        this.watching = false;
        this.history.clear();
        this.base = "";
        this.path = "";
        setChromeTitle("Plex · Servidores");
        task(new PlexActivity$$ExternalSyntheticLambda20(this));
    }

    /* JADX INFO: renamed from: dp */
    private int m406dp(int r3) {
        return Math.round(r3 * getResources().getDisplayMetrics().density);
    }

    private String favoriteKey(Element r5) {
        String r0 = r5.getAttribute("ratingKey");
        if (r0.isEmpty() == false) goto L6;
        r0 = r5.getAttribute("key");
    L6:
        if (r5.getTagName().equals("Device") == false) goto L13;
        NodeList r1 = r5.getElementsByTagName(HttpHeaders.CONNECTION);
        if (r1.getLength() <= 0) goto L10;
        String r2 = ((Element) r1.item(0)).getAttribute("uri");
    L11:
        r0 = r2;
        goto L13
    L10:
        r2 = r5.getAttribute("name");
    L13:
        return this.base + "|" + this.path + "|" + r5.getTagName() + "|" + r0;
    }

    private void filter() {
        PlexFilterRequest r0 = new PlexFilterRequest(this);
        applyFilter(r0, prepareFilter(r0));
    }

    private void filterAsync() {
        final long r3 = SystemClock.elapsedRealtime();
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        if (this.screenVisible == false) goto L13;
        final PlexFilterRequest r2 = new PlexFilterRequest(this);
        if (r2.scope().equals(this.displayedScope) == true) goto L10;
        this.grid.setEnabled(false);
    L10:
        final int r5 = this.filterGeneration + 1;
        this.filterGeneration = r5;
        this.filterWorker.execute(new PlexActivity$$ExternalSyntheticLambda22(this, r2, r3, r5));
        return;
    }

    private void goBack() {
        if (this.searchRow.getVisibility() != 0) goto L9;
        if (this.compact == false) goto L9;
        setSearchExpanded(false, false);
        return;
    L9:
        if (this.busy == false) goto L12;
        return;
    L12:
        if (this.watching == false) goto L16;
        this.watching = false;
        this.search.setText("");
        filterAsync();
        return;
    L16:
        if (this.history.isEmpty() == true) goto L19;
        load(this.history.pop());
        return;
    L19:
        if (this.base.isEmpty() == true) goto L24;
        if (this.imported.isEmpty() == true) goto L24;
        showImported();
        return;
    L24:
        if (this.base.isEmpty() == true) goto L26;
        this.token = getPreferences(0).getString("token", "");
        discover();
        return;
    L26:
        finish();
    }

    private void importMenu() {
        if (this.busy == false) goto L5;
        return;
    L5:
        new AlertDialog.Builder(this).setTitle("Importar Plex JSON").setItems(new String[]{"Archivo del dispositivo", "URL o Dropbox"}, new PlexActivity$$ExternalSyntheticLambda42(this)).show();
    }

    private void importStream(InputStream r4) throws Exception {
        Reader r0 = PlaylistInput.reader(r4);
        final ArrayList<PlexImport.Server> r1 = PlexImport.read(new BufferedReader(r0, 65536));     // Catch: Throwable -> L8
        if (r0 == null) goto L6;
        r0.close();
    L6:
        new AppStorage(this).write("plex-import.dat", r1);
        runOnUiThread(new PlexActivity$$ExternalSyntheticLambda32(this, r1));
        return;
    L8:
        th = move-exception;
        if (r0 != null) goto L17;
    L14:
        throw th;
    L17:
        r0.close();     // Catch: Throwable -> L12
    L12:
        th = move-exception;
        th.addSuppressed(th);
        goto L14
    }

    private boolean isFavorite(Element r4) {
        return getSharedPreferences("plex-favorites", 0).getBoolean(favoriteKey(r4), false);
    }

    static /* synthetic */ void lambda$addTool$18(Runnable r0, View r1) {
        r0.run();
    }

    private /* synthetic */ void lambda$connect$42(EditText r5, DialogInterface r6, int r7) {
        String r0 = r5.getText().toString().trim();
        if (r0.isEmpty() == false) goto L6;
        return;
    L6:
        if (this.busy == false) goto L9;
        this.status.setText("Espera a que termine la carga actual");
        return;
    L9:
        this.token = r0;
        getPreferences(0).edit().putString("token", this.token).apply();
        if (this.toolsOnly == true) goto L12;
        discover();
        return;
    L12:
        this.status.setText("Cuenta guardada");
    }

    private /* synthetic */ void lambda$connect$43(DialogInterface r3, int r4) {
        if (this.busy == false) goto L5;
        return;
    L5:
        this.token = "";
        getPreferences(0).edit().remove("token").apply();
        this.all.clear();
        filterAsync();
        this.status.setText("Cuenta desconectada");
    }

    private /* synthetic */ void lambda$discover$39() throws Exception {
        publish(this.client.get("https://plex.tv/api/resources?includeHttps=1&includeRelay=1", this.token));
    }

    private /* synthetic */ void lambda$filterAsync$28(int r2, PlexFilterRequest r3, PlexFilterPlan r4) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        if (this.screenVisible == true) goto L7;
        return;
    L7:
        if (r2 == this.filterGeneration) goto L9;
        return;
    L9:
        if (r3.matches(this) == false) goto L15;
        applyFilter(r3, r4);
        return;
    }

    private /* synthetic */ void lambda$filterAsync$29(final PlexFilterRequest r5, long r6, final int r8) {
        final PlexFilterPlan r0 = prepareFilter(r5);
        BlackBox.event("PLEX_FILTER_READY", "duration_ms=" + (SystemClock.elapsedRealtime() - r6));
        runOnUiThread(new PlexActivity$$ExternalSyntheticLambda44(this, r8, r5, r0));
    }

    private /* synthetic */ void lambda$importMenu$20(String r4) throws Exception {
        InputStream r0 = PlexDownload.open(r4);
        importStream(r0);     // Catch: Throwable -> L7
        if (r0 == null) goto L18;
        r0.close();
        return;
    L18:
        return;
    L7:
        th = move-exception;
        if (r0 != null) goto L14;
    L13:
        throw th;
    L14:
        r0.close();     // Catch: Throwable -> L11
    L11:
        th = move-exception;
        th.addSuppressed(th);
        goto L13
    }

    private /* synthetic */ void lambda$importMenu$21(EditText r3, DialogInterface r4, int r5) {
        final String r0 = r3.getText().toString().trim();
        if (r0.isEmpty() == true) goto L6;
        task(new PlexActivity$$ExternalSyntheticLambda35(this, r0));
        return;
    }

    private /* synthetic */ void lambda$importMenu$22(DialogInterface r5, int r6) {
        if (r6 != 0) goto L4;
        startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT").setType("*/*").addCategory("android.intent.category.OPENABLE"), 71);
        return;
    L4:
        final EditText r0 = new EditText(this);
        r0.setSingleLine();
        r0.setInputType(17);
        r0.setHint("Enlace del JSON o Dropbox");
        new AlertDialog.Builder(this).setTitle("URL del JSON Plex").setView(r0).setPositiveButton("Importar", new PlexActivity$$ExternalSyntheticLambda33(this, r0)).setNegativeButton("Cancelar", null).show();
    }

    private /* synthetic */ void lambda$importStream$24(ArrayList r2) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.imported = r2;
        showImported();
    }

    private /* synthetic */ void lambda$launch$32(long r3, Entry r5) {
        if (this.playbackLaunching == false) goto L5;
        return;
    L5:
        this.playbackLaunching = true;
        startActivity(new Intent(this, PlayerActivity.class).putExtra("playbackRequestedAt", r3).putExtra("entry", r5));
        overridePendingTransition(0, 0);
    }

    private /* synthetic */ void lambda$load$40(String r4) {
        setChromeTitle("Plex · " + this.pathTitles.getOrDefault(r4, "Biblioteca"));
    }

    private /* synthetic */ void lambda$load$41(final String r6) throws Exception {
        String r0 = PlexClient.endpoint(this.base, r6);
        PlexClient r1 = this.client;
        StringBuilder r2 = new StringBuilder().append(r0);
        String r3 = "?";
        if (r0.contains("?") == false) goto L5;
        r3 = "&";
    L5:
        publish(r1.get(r2.append(r3).append("X-Plex-Container-Start=").append(this.offset).append("&X-Plex-Container-Size=200").toString(), this.token));
        this.path = r6;
        runOnUiThread(new PlexActivity$$ExternalSyntheticLambda47(this, r6));
    }

    static /* synthetic */ void lambda$new$0(Runnable r1) {
        Process.setThreadPriority(10);
        r1.run();
    }

    static /* synthetic */ Thread lambda$new$1(final Runnable r3) {
        return new Thread(new PlexActivity$$ExternalSyntheticLambda37(r3), "CHDTVPlexNetwork");
    }

    private /* synthetic */ void lambda$new$25() {
        filterAsync();
    }

    static /* synthetic */ void lambda$new$26(Runnable r1) {
        Process.setThreadPriority(10);
        r1.run();
    }

    static /* synthetic */ Thread lambda$new$27(final Runnable r3) {
        return new Thread(new PlexActivity$$ExternalSyntheticLambda48(r3), "CHDTVPlexFilter");
    }

    private /* synthetic */ void lambda$onActivityResult$23(Uri r4) throws Exception {
        InputStream r0 = getContentResolver().openInputStream(r4);
        if (r0 == null) goto L11;
        importStream(r0);     // Catch: Throwable -> L8
        if (r0 == null) goto L21;
        r0.close();
        return;
    L21:
        return;
    L11:
        throw new IOException("No se pudo abrir el JSON");     // Catch: Throwable -> L8
    L8:
        th = move-exception;
        if (r0 != null) goto L18;
    L17:
        throw th;
    L18:
        r0.close();     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        th.addSuppressed(th);
        goto L17
    }

    private /* synthetic */ void lambda$onCreate$10(View r2) {
        if (this.busy == false) goto L5;
        return;
    L5:
        if (this.path.isEmpty() == true) goto L9;
        this.offset += 200;
        load(this.path);
        return;
    }

    private /* synthetic */ void lambda$onCreate$11(AdapterView r2, View r3, int r4, long r5) {
        if (r4 >= 0) goto L4;
        return;
    L4:
        if (r4 < this.shown.size()) goto L6;
        return;
    L6:
        if (previewBeforeOpen(r4) == true) goto L12;
        open(this.shown.get(r4));
        return;
    }

    private /* synthetic */ void lambda$onCreate$12(Element r4, DialogInterface r5, int r6) {
        getSharedPreferences("plex-favorites", 0).edit().putBoolean(favoriteKey(r4), !isFavorite(r4)).apply();
        filterAsync();
    }

    private /* synthetic */ void lambda$onCreate$13(WatchHistory.Item r2, DialogInterface r3, int r4) {
        Entry r0 = r2.entry;
        if (r4 != 0) goto L5;
        resumeSaved(r0);
        return;
    L5:
        WatchHistory.remove(this, r0);
        filterAsync();
    }

    private /* synthetic */ boolean lambda$onCreate$14(AdapterView r7, View r8, int r9, long r10) {
        if (r9 >= 0) goto L5;
    L23:
        return false;
    L5:
        if (r9 >= this.shown.size()) goto L23;
        final Element r1 = this.shown.get(r9);
        if (r1.getTagName().equals("Watch") == true) goto L18;
        AlertDialog.Builder r0 = new AlertDialog.Builder(this);
        String r2 = "title";
        if (r1.getAttribute("title").isEmpty() == false) goto L12;
        r2 = "name";
    L12:
        AlertDialog.Builder r3 = r0.setTitle(r1.getAttribute(r2));
        if (isFavorite(r1) == false) goto L15;
        String r4 = "Quitar favorito";
    L16:
        r3.setItems(new String[]{r4}, new PlexActivity$$ExternalSyntheticLambda0(this, r1)).show();
        return true;
    L15:
        r4 = "Añadir favorito";
        goto L16
    L18:
        final WatchHistory.Item r5 = this.pending.get(r1.getAttribute("id"));
        if (r5 != null) goto L21;
        return false;
    L21:
        new AlertDialog.Builder(this).setTitle(r5.entry.title).setItems(new String[]{"Reanudar", "Quitar de Seguir viendo"}, new PlexActivity$$ExternalSyntheticLambda11(this, r5)).show();
        return true;
    }

    private /* synthetic */ void lambda$onCreate$15() {
        openPanelToolAction(getIntent().getStringExtra("panelTool"));
    }

    static /* synthetic */ String lambda$onCreate$16(PlexImport.Server r1) {
        return r1.name;
    }

    private /* synthetic */ void lambda$onCreate$17() {
        AlertDialog.Builder r0 = new AlertDialog.Builder(this).setTitle("Servidores Plex");
        if (this.imported.isEmpty() == false) goto L5;
        String r1 = "No hay servidores importados";
    L6:
        r0.setMessage(r1).setPositiveButton("Cerrar", null).show();
        return;
    L5:
        r1 = TextUtils.join("\n", (Iterable) this.imported.stream().map(new PlexActivity$$ExternalSyntheticLambda17()).collect(Collectors.toList()));
        goto L6
    }

    private /* synthetic */ void lambda$onCreate$2(View r1) {
        goBack();
    }

    private /* synthetic */ void lambda$onCreate$3(View r2) {
        this.favoritesOnly = !this.favoritesOnly;
        filterAsync();
    }

    private /* synthetic */ void lambda$onCreate$4(DialogInterface r2, int r3) {
        if (r3 != 0) goto L5;
        connect();
        return;
    L5:
        if (r3 != 1) goto L8;
        importMenu();
        return;
    L8:
        if (this.busy == true) goto L13;
        showImported();
        return;
    }

    private /* synthetic */ void lambda$onCreate$5(View r5) {
        new AlertDialog.Builder(this).setTitle("Plex").setItems(new String[]{"Conectar cuenta", "Importar JSON", "Servidores"}, new PlexActivity$$ExternalSyntheticLambda21(this)).show();
    }

    private /* synthetic */ void lambda$onCreate$6(View r2) {
        setSearchExpanded(false, false);
    }

    private /* synthetic */ void lambda$onCreate$7(View r3) {
        if (this.searchRow.getVisibility() == 0) goto L5;
        boolean r0 = true;
    L6:
        setSearchExpanded(r0, true);
        return;
    L5:
        r0 = false;
        goto L6
    }

    private /* synthetic */ View lambda$onCreate$8() {
        return this.grid;
    }

    private /* synthetic */ void lambda$onCreate$9() {
        this.searchHandler.removeCallbacks(this.searchFilter);
        filterAsync();
        setSearchExpanded(false, false);
    }

    private /* synthetic */ void lambda$onResume$51() {
        if (this.adapter == null) goto L6;
        filterAsync();
        return;
    }

    private /* synthetic */ void lambda$open$44(Element r3) {
        setChromeTitle("Plex · " + r3.getAttribute("name"));
    }

    private /* synthetic */ void lambda$open$45(final Element r12) throws Exception {
        NodeList r1 = r12.getElementsByTagName(HttpHeaders.CONNECTION);
        String r2 = r12.getAttribute("accessToken");
        if (r2.isEmpty() == false) goto L5;
        String r3 = this.token;
    L6:
        Exception r4 = null;
        int r5 = 0;
    L8:
        if (r5 >= r1.getLength()) goto L14;
        String r6 = ((Element) r1.item(r5)).getAttribute("uri");
        Document r7 = this.client.get(PlexClient.endpoint(r6, "/library/sections"), r3);     // Catch: Exception -> L12
        this.base = r6;     // Catch: Exception -> L12
        this.token = r3;     // Catch: Exception -> L12
        this.path = "/library/sections";     // Catch: Exception -> L12
        this.pathTitles.put(this.path, r12.getAttribute("name"));     // Catch: Exception -> L12
        runOnUiThread(new PlexActivity$$ExternalSyntheticLambda52(this, r12));     // Catch: Exception -> L12
        publish(r7);     // Catch: Exception -> L12
        return;
    L12:
        e = move-exception;
        r4 = e;
        r5 = r5 + 1;
        goto L8
    L14:
        if (r4 != null) goto L17;
        throw new IOException("Servidor sin conexiones disponibles");
    L17:
        throw r4;
    L5:
        r3 = r2;
        goto L6
    }

    private /* synthetic */ void lambda$open$46(Entry r2, Element r3) {
        if (isDestroyed() == true) goto L6;
        showPlexDetail(r2, r3);
        return;
    }

    private /* synthetic */ void lambda$open$47(String r5, Element r6, String r7) throws Exception {
        final Element r1 = (Element) this.client.get(PlexClient.endpoint(r5, "/library/metadata/" + r6.getAttribute("ratingKey")), r7).getElementsByTagName("Video").item(0);
        if (r1 == null) goto L7;
        final Entry r2 = PlexClient.playable(r5, r7, r1);
        runOnUiThread(new PlexActivity$$ExternalSyntheticLambda41(this, r2, r1));
        return;
    L7:
        throw new IOException("Metadatos incompletos");
    }

    static /* synthetic */ String lambda$openPanelToolAction$19(PlexImport.Server r1) {
        return r1.name;
    }

    private /* synthetic */ void lambda$playEntry$30(Entry r1) {
        launch(r1);
    }

    private /* synthetic */ void lambda$playEntry$31(Entry r1) {
        WatchHistory.remove(this, r1);
        launch(r1);
    }

    private /* synthetic */ void lambda$publish$38(List r4) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.all.clear();
        this.all.addAll(r4);
        filterAsync();
        this.status.setText(r4.size() + " elementos · Plex");
    }

    private /* synthetic */ void lambda$resumeSaved$33(Entry r2) {
        if (isDestroyed() == true) goto L6;
        playEntry(r2);
        return;
    }

    private /* synthetic */ void lambda$resumeSaved$34(Entry r5, String r6) throws Exception {
        PlexClient r0 = this.client;
        String r1 = PlexClient.endpoint(r5.plexBase, "/library/metadata/" + r5.plexKey);
        String r2 = "";
        if (r6 != null) goto L5;
        String r3 = "";
    L6:
        Element r4 = (Element) r0.get(r1, r3).getElementsByTagName("Video").item(0);
        if (r4 == null) goto L15;
        String r7 = r5.plexBase;
        if (r6 == null) goto L12;
        r2 = r6;
    L12:
        final Entry r8 = PlexClient.playable(r7, r2, r4);
        runOnUiThread(new PlexActivity$$ExternalSyntheticLambda46(this, r8));
        return;
    L15:
        throw new IOException("El título ya no está disponible en Plex");
    L5:
        r3 = r6;
        goto L6
    }

    private /* synthetic */ void lambda$showPlexDetail$48(Entry r1) {
        playEntry(r1);
    }

    static /* synthetic */ void lambda$showPlexDetail$49(Entry r0) {
    }

    private /* synthetic */ void lambda$showPlexDetail$50(final Entry r12, Element r13) {
        PlexActivity r1 = this;
        CinematicDetail.show(r1, r12, this.tmdb, this.logos, new PlexActivity$$ExternalSyntheticLambda23(this, r12), null, new PlexActivity$$ExternalSyntheticLambda24(this), Collections.emptyList(), new PlexActivity$$ExternalSyntheticLambda25(), plexInfo(r13));     // Catch: Exception -> L6
        return;
    L10:
        r1.status.setText("No se pudo abrir la ficha Plex");
        return;
    L9:
        r1 = this;
        goto L10
    }

    private /* synthetic */ void lambda$task$35(int r3, Exception r4) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        if (r3 != this.generation) goto L13;
        TextView r0 = this.status;
        if (r4.getMessage() != null) goto L9;
        String r1 = "No se pudo conectar con Plex";
    L10:
        r0.setText(r1);
        return;
    L9:
        r1 = r4.getMessage();
        goto L10
    }

    private /* synthetic */ void lambda$task$36(int r2) {
        if (r2 != this.generation) goto L6;
        this.busy = false;
        return;
    }

    private /* synthetic */ void lambda$task$37(Work r3, final int r4) {
        r3.run();     // Catch: Throwable -> L5 Exception -> L7
        Runnable r0 = new PlexActivity$$ExternalSyntheticLambda18(this, r4);
    L4:
        runOnUiThread(r0);
        return;
    L7:
        e = move-exception;
        runOnUiThread(new PlexActivity$$ExternalSyntheticLambda19(this, r4, e));     // Catch: Throwable -> L5
        r0 = new PlexActivity$$ExternalSyntheticLambda18(this, r4);
    L5:
        th = move-exception;
        runOnUiThread(new PlexActivity$$ExternalSyntheticLambda18(this, r4));
        throw th;
    }

    private void launch(final Entry r4) {
        BlackBox.event("PLEX_PLAY_REQUEST", "kind=" + r4.kind);
        final long r0 = SystemClock.elapsedRealtime();
        ParentalControl.requireEntry(this, r4, new PlexActivity$$ExternalSyntheticLambda29(this, r0, r4));
    }

    private void load(final String r2) {
        if (r2.equals(this.path) == true) goto L5;
        this.offset = 0;
    L5:
        task(new PlexActivity$$ExternalSyntheticLambda40(this, r2));
    }

    private void open(final Element r6) {
        if (this.busy == false) goto L5;
        return;
    L5:
        if (currentScope().equals(this.displayedScope) == true) goto L8;
        return;
    L8:
        if (r6.getTagName().equals("Continue") == false) goto L12;
        this.watching = true;
        this.search.setText("");
        filterAsync();
        return;
    L12:
        if (r6.getTagName().equals("Watch") == false) goto L18;
        WatchHistory.Item r0 = this.pending.get(r6.getAttribute("id"));
        if (r0 == null) goto L38;
        resumeSaved(r0.entry);
        return;
    L38:
        return;
    L18:
        if (r6.getTagName().equals("Device") == false) goto L22;
        task(new PlexActivity$$ExternalSyntheticLambda38(this, r6));
        return;
    L22:
        if (r6.getTagName().equals("Video") == false) goto L25;
        final String r1 = this.base;
        final String r2 = this.token;
        task(new PlexActivity$$ExternalSyntheticLambda39(this, r1, r6, r2));
        return;
    L25:
        this.history.push(this.path);
        String r3 = r6.getAttribute("key");
        if (this.path.equals("/library/sections") == false) goto L30;
        StringBuilder r4 = new StringBuilder().append("/library/sections/").append(r3);
        String r5 = "/all";
    L28:
        String r7 = r4.append(r5).toString();
    L33:
        Map<String, String> r8 = this.pathTitles;
        String r9 = "title";
        if (r6.getAttribute("title").isEmpty() == false) goto L36;
        r9 = "name";
    L36:
        r8.put(r7, r6.getAttribute(r9));
        load(r7);
        return;
    L30:
        if (r3.startsWith("/") == false) goto L32;
        r7 = r3;
        goto L33
    L32:
        r4 = new StringBuilder().append("/library/metadata/").append(r3);
        r5 = "/children";
        goto L28
    }

    private void playEntry(final Entry r9) {
        if (WatchHistory.resume(this, r9) <= 0) goto L5;
        ChdtvDialogs.resume(this, r9, WatchHistory.resume(this, r9), this.logos, new PlexActivity$$ExternalSyntheticLambda30(this, r9), new PlexActivity$$ExternalSyntheticLambda31(this, r9));
        return;
    L5:
        launch(r9);
    }

    static JSONObject plexInfo(Element r12) throws Exception {
        JSONObject r0 = new JSONObject();
        r0.put("title", r12.getAttribute("title"));
        r0.put("name", r12.getAttribute("grandparentTitle"));
        r0.put("overview", r12.getAttribute("summary"));
        r0.put("release_date", r12.getAttribute("originallyAvailableAt"));
        r0.put("runtime", Long.parseLong(r12.getAttribute(MediaInformation.KEY_DURATION)) / 60000);     // Catch: Exception -> L5
    L26:
        r0.put("vote_average", Double.parseDouble(r12.getAttribute("rating")));     // Catch: Exception -> L8
    L9:
        JSONArray r1 = new JSONArray();
        JSONArray r3 = new JSONArray();
        JSONArray r4 = new JSONArray();
        NodeList r5 = r12.getChildNodes();
        int r6 = 0;
    L11:
        if (r6 >= r5.getLength()) goto L24;
        if ((r5.item(r6) instanceof Element) == false) goto L23;
        Element r7 = (Element) r5.item(r6);
        if ("Genre".equals(r7.getTagName()) == false) goto L18;
        r1.put(new JSONObject().put("name", r7.getAttribute("tag")));
    L18:
        if ("Role".equals(r7.getTagName()) == false) goto L21;
        r3.put(new JSONObject().put("id", r6).put("name", r7.getAttribute("tag")).put("character", r7.getAttribute("role")));
    L21:
        if ("Director".equals(r7.getTagName()) == false) goto L23;
        r4.put(new JSONObject().put("name", r7.getAttribute("tag")).put("job", "Director"));
    L23:
        r6 = r6 + 1;
        goto L11
    L24:
        r0.put("genres", r1);
        r0.put("credits", new JSONObject().put("cast", r3).put("crew", r4));
        return r0;
    }

    private PlexFilterPlan prepareFilter(PlexFilterRequest r10) {
        PlexFilterPlan r0 = new PlexFilterPlan(null);
        List<WatchHistory.Item> r1 = WatchHistory.plex(this, r10.base);
        Document r2 = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();     // Catch: Exception -> L32
        if (r10.watching == false) goto L13;
        Iterator<WatchHistory.Item> r3 = r1.iterator();     // Catch: Exception -> L32
    L7:
        if (r3.hasNext() == false) goto L33;
        WatchHistory.Item r4 = r3.next();     // Catch: Exception -> L32
        Entry r5 = r4.entry;     // Catch: Exception -> L32
        if ((r5.title + " " + r5.series).toLowerCase(Locale.ROOT).contains(r10.term) == false) goto L7;
        String r6 = WatchHistory.m418id(r5);     // Catch: Exception -> L32
        r0.pending.put(r6, r4);     // Catch: Exception -> L32
        Element r7 = r2.createElement("Watch");     // Catch: Exception -> L32
        r7.setAttribute("id", r6);     // Catch: Exception -> L32
        r0.rows.add(r7);     // Catch: Exception -> L32
        goto L7
    L13:
        if (r10.favorites == false) goto L15;
    L21:
        Iterator<Element> r8 = r10.rows.iterator();     // Catch: Exception -> L32
    L23:
        if (r8.hasNext() == false) goto L33;
        Element r9 = r8.next();     // Catch: Exception -> L32
        if (r10.favorites == false) goto L29;
        if (r10.starred.contains(r9) == false) goto L23;
    L29:
        if ((r9.getAttribute("title") + r9.getAttribute("name")).toLowerCase(Locale.ROOT).contains(r10.term) == false) goto L23;
        r0.rows.add(r9);     // Catch: Exception -> L32
        goto L23
    L15:
        if (r1.isEmpty() == true) goto L21;
        if (r10.term.isEmpty() == false) goto L19;
    L20:
        r0.rows.add(r2.createElement("Continue"));     // Catch: Exception -> L32
        goto L21
    L19:
        if ("seguir viendo".contains(r10.term) == false) goto L21;
    L33:
        return r0;
    }

    private boolean previewBeforeOpen(int r6) {
        if (AppTheme.cine(this) == true) goto L5;
    L22:
        return false;
    L5:
        if (this.cineHero == null) goto L22;
        if (r6 < 0) goto L22;
        if (r6 >= this.shown.size()) goto L22;
        Element r0 = this.shown.get(r6);
        if (r0.getTagName().equals("Video") == false) goto L13;
    L17:
        boolean r1 = this.previewActivation.activate(favoriteKey(r0));
        showCineSelection(r6);
        this.grid.setItemChecked(r6, true);
        if (this.grid.isInTouchMode() == true) goto L21;
        this.grid.setSelection(r6);
    L21:
        return !r1;
    L13:
        if ("show".equals(r0.getAttribute("type")) == true) goto L17;
        if ("season".equals(r0.getAttribute("type")) == true) goto L17;
        return false;
    }

    private void publish(Document r8) {
        final ArrayList r0 = new ArrayList();
        NodeList r1 = r8.getDocumentElement().getChildNodes();
        int r2 = 0;
    L4:
        if (r2 >= r1.getLength()) goto L15;
        if ((r1.item(r2) instanceof Element) == false) goto L14;
        Element r3 = (Element) r1.item(r2);
        if (Arrays.asList(new String[]{"Device", "Directory", "Video"}).contains(r3.getTagName()) == false) goto L14;
        if (r3.getTagName().equals("Device") == true) goto L12;
    L13:
        r0.add(r3);
        goto L14
    L12:
        if (r3.getAttribute("provides").contains("server") == true) goto L13;
    L14:
        r2 = r2 + 1;
        goto L4
    L15:
        runOnUiThread(new PlexActivity$$ExternalSyntheticLambda36(this, r0));
    }

    private void refreshVisibleCards() {
        int r0 = this.grid.getFirstVisiblePosition();
        int r1 = 0;
    L4:
        if (r1 >= this.grid.getChildCount()) goto L11;
        int r2 = r0 + r1;
        if (r2 < 0) goto L10;
        if (r2 >= this.shown.size()) goto L10;
        this.adapter.getView(r2, this.grid.getChildAt(r1), this.grid);
    L10:
        r1 = r1 + 1;
        goto L4
    }

    private void resumeSaved(final Entry r3) {
        if (r3.plexBase != null) goto L5;
    L11:
        playEntry(r3);
        return;
    L5:
        if (r3.plexBase.isEmpty() == true) goto L11;
        if (r3.plexKey == null) goto L11;
        if (r3.plexKey.matches("[0-9]+") == false) goto L11;
        final String r0 = r3.headers.get("X-Plex-Token");
        task(new PlexActivity$$ExternalSyntheticLambda53(this, r3, r0));
    }

    private void setSearchExpanded(boolean r6, boolean r7) {
        LinearLayout r0 = this.searchRow;
        if (r6 == false) goto L5;
        int r2 = 0;
    L6:
        r0.setVisibility(r2);
        this.search.setVisibility(0);
        InputMethodManager r1 = (InputMethodManager) getSystemService("input_method");
        if (r6 == false) goto L13;
        if (r7 == false) goto L13;
        this.search.requestFocus();
        if (this.compact == false) goto L18;
        if (r1 == null) goto L18;
        r1.showSoftInput(this.search, 1);
    L18:
        this.searchToggle.setText("⌕");
        Button r3 = this.searchToggle;
        StringBuilder r4 = new StringBuilder().append("Búsqueda Plex");
        if (this.search.getText().length() <= 0) goto L21;
        String r5 = ": " + this.search.getText();
    L22:
        r3.setContentDescription(r4.append(r5).toString());
        return;
    L21:
        r5 = "";
    L13:
        if (r1 == null) goto L15;
        r1.hideSoftInputFromWindow(this.search.getWindowToken(), 0);
    L15:
        this.search.clearFocus();
        if (this.grid == null) goto L18;
        this.grid.requestFocus();
        goto L18
    L5:
        r2 = 8;
        goto L6
    }

    private void showCineSelection(int r8) {
        String r0 = "?";
        if (this.cineHero != null) goto L6;
        return;
    L6:
        if (r8 >= 0) goto L8;
    L36:
        this.cineHero.setVisibility(8);
        return;
    L8:
        if (r8 >= this.shown.size()) goto L36;
        Element r2 = this.shown.get(r8);
        this.previewActivation.select(favoriteKey(r2));
        if (r2.getTagName().equals("Video") == false) goto L13;
    L38:
        Entry r3 = new Entry();     // Catch: Exception -> L33
        r3.title = r2.getAttribute("title");     // Catch: Exception -> L33
        if ("movie".equals(r2.getAttribute("type")) == false) goto L21;
        String r4 = "Películas";
    L22:
        r3.kind = r4;     // Catch: Exception -> L33
        r3.series = r2.getAttribute("grandparentTitle");     // Catch: Exception -> L33
        String r5 = r2.getAttribute("art");     // Catch: Exception -> L33
        if (r5.isEmpty() == false) goto L26;
        r5 = artwork(r2);     // Catch: Exception -> L33
    L26:
        if (r5.isEmpty() == true) goto L31;
        StringBuilder r6 = new StringBuilder().append(PlexClient.endpoint(this.base, r5));     // Catch: Exception -> L33
        if (r5.contains("?") == false) goto L30;
        r0 = "&";
    L30:
        r3.logo = r6.append(r0).append("X-Plex-Token=").append(URLEncoder.encode(this.token, "UTF-8")).toString();     // Catch: Exception -> L33
    L31:
        this.cineHero.setVisibility(0);     // Catch: Exception -> L33
        this.cineHero.bindLocal(r3, plexInfo(r2));     // Catch: Exception -> L33
        return;
    L21:
        r4 = "Series";
    L34:
        this.cineHero.setVisibility(8);
        return;
    L13:
        if ("show".equals(r2.getAttribute("type")) == true) goto L38;
        if ("season".equals(r2.getAttribute("type")) == true) goto L38;
        this.cineHero.setVisibility(8);
    }

    private void showImported() {
        this.watching = false;
        this.history.clear();
        this.base = "";
        this.path = "";
        setChromeTitle("Plex · Servidores");
        Document r0 = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();     // Catch: Exception -> L11
        Element r1 = r0.createElement("MediaContainer");     // Catch: Exception -> L11
        r0.appendChild(r1);     // Catch: Exception -> L11
        Iterator<PlexImport.Server> r2 = this.imported.iterator();     // Catch: Exception -> L11
    L5:
        if (r2.hasNext() == false) goto L7;
        PlexImport.Server r3 = r2.next();     // Catch: Exception -> L11
        Element r4 = r0.createElement("Device");     // Catch: Exception -> L11
        r4.setAttribute("name", r3.name);     // Catch: Exception -> L11
        r4.setAttribute("provides", "server");     // Catch: Exception -> L11
        r4.setAttribute("accessToken", r3.token);     // Catch: Exception -> L11
        Element r5 = r0.createElement(HttpHeaders.CONNECTION);     // Catch: Exception -> L11
        r5.setAttribute("uri", r3.base);     // Catch: Exception -> L11
        r4.appendChild(r5);     // Catch: Exception -> L11
        r1.appendChild(r4);     // Catch: Exception -> L11
        goto L5
    L7:
        publish(r0);     // Catch: Exception -> L11
        if (this.imported.isEmpty() == false) goto L17;
        this.status.setText("Importa un JSON Plex o conecta tu cuenta");     // Catch: Exception -> L11
        return;
    L17:
        return;
    L12:
        this.status.setText("No se pudieron mostrar los servidores");
    }

    private void showPlexDetail(final Entry r2, final Element r3) {
        ParentalControl.requireEntry(this, r2, new PlexActivity$$ExternalSyntheticLambda16(this, r2, r3));
    }

    private void task(final Work r4) {
        if (this.busy == false) goto L5;
        return;
    L5:
        this.busy = true;
        this.status.setText("Cargando Plex…");
        final int r1 = this.generation + 1;
        this.generation = r1;
        this.worker.execute(new PlexActivity$$ExternalSyntheticLambda54(this, r4, r1));
    }

    private TextView text(String r3, int r4) {
        TextView r0 = new TextView(this);
        r0.setText(r3);
        r0.setTextSize(r4);
        r0.setTextColor(-1);
        return r0;
    }

    @Override // android.app.Activity
    protected void onActivityResult(int r3, int r4, Intent r5) {
        super.onActivityResult(r3, r4, r5);
        if (r3 == 71) goto L5;
        return;
    L5:
        if (r4 != (-1)) goto L13;
        if (r5 != null) goto L8;
        return;
    L8:
        if (r5.getData() == null) goto L15;
        final Uri r0 = r5.getData();
        task(new PlexActivity$$ExternalSyntheticLambda34(this, r0));
        return;
    L15:
        return;
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    public void onBackPressed() {
        if (closeNavigation() == false) goto L5;
        return;
    L5:
        goBack();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration r3) {
        super.onConfigurationChanged(r3);
        if (r3.screenWidthDp >= 600) goto L5;
        boolean r0 = true;
    L6:
        this.compact = r0;
        if (this.cineHero == null) goto L9;
        ViewGroup.LayoutParams r1 = this.cineHero.getLayoutParams();
        r1.height = CineHero.previewLayout(this).height;
        this.cineHero.setLayoutParams(r1);
    L9:
        this.root.requestLayout();
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    public void onCreate(Bundle r18) {
        super.onCreate(r18);
        this.toolsOnly = getIntent().getBooleanExtra("toolsOnly", false);
        this.logos = new LogoLoader(this);
        this.tmdb = new TmdbService(this);
        this.token = getPreferences(0).getString("token", "");
        this.root = new LinearLayout(this);
        this.root.setOrientation(1);
        this.root.setPadding(m406dp(14), m406dp(12), m406dp(14), m406dp(12));
        this.root.setBackgroundColor(AppTheme.color(this, -16180435));
        setContentView(this.root);
        setChromeTitle("Plex · Servidores");
        LinearLayout r0 = new LinearLayout(this);
        Button r1 = new Button(this);
        r1.setText("‹");
        r1.setOnClickListener(new PlexActivity$$ExternalSyntheticLambda1(this));
        TextView r10 = text("CHDTV · Plex", 24);
        r10.setTextColor(AppTheme.color(this, -12986907));
        r0.addView(r10, new LinearLayout.LayoutParams(0, -2, 1.0f));
        if ((getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density) >= 600.0f) goto L5;
        boolean r11 = true;
    L6:
        this.compact = r11;
        r10.setSingleLine(true);
        r10.setEllipsize(TextUtils.TruncateAt.END);
        if (this.compact == false) goto L9;
        float r12 = 18.0f;
    L10:
        r10.setTextSize(r12);
        r1.setText("‹");
        r1.setContentDescription("Volver en Plex");
        SettingsSidePanel.style(r1);
        r1.setLayoutParams(new LinearLayout.LayoutParams(m406dp(48), m406dp(48)));
        this.searchToggle = new Button(this);
        this.searchToggle.setText("⌕");
        this.searchToggle.setContentDescription("Mostrar u ocultar búsqueda Plex");
        SettingsSidePanel.style(this.searchToggle);
        this.favoriteToggle = new Button(this);
        this.favoriteToggle.setText("☆");
        this.favoriteToggle.setContentDescription("Mostrar favoritos Plex");
        this.favoriteToggle.setOnClickListener(new PlexActivity$$ExternalSyntheticLambda6(this));
        setChromeActions(r1, this.favoriteToggle, this.searchToggle);
        Button r2 = new Button(this);
        r2.setText("⋮");
        r2.setContentDescription("Opciones Plex: conectar, importar JSON y servidores");
        SettingsSidePanel.style(r2);
        r2.setOnClickListener(new PlexActivity$$ExternalSyntheticLambda7(this));
        r0.addView(r2, new LinearLayout.LayoutParams(m406dp(48), m406dp(48)));
        this.searchRow = new LinearLayout(this);
        this.searchRow.setGravity(16);
        this.search = new EditText(this);
        this.search.setSingleLine(true);
        this.search.setTextSize(15.0f);
        this.search.setTextColor(-1);
        this.search.setHintTextColor(AppTheme.color(this, -7232068));
        this.search.setHint("Buscar en esta página");
        this.search.setInputType(1);
        this.search.setImeOptions(268435459);
        this.searchRow.addView(this.search, new LinearLayout.LayoutParams(0, m406dp(48), 1.0f));
        Button r3 = new Button(this);
        r3.setText("×");
        r3.setContentDescription("Cerrar búsqueda");
        SettingsSidePanel.style(r3);
        r3.setOnClickListener(new PlexActivity$$ExternalSyntheticLambda8(this));
        this.searchRow.addView(r3, new LinearLayout.LayoutParams(m406dp(48), m406dp(48)));
        this.root.addView(this.searchRow, new LinearLayout.LayoutParams(-1, m406dp(48)));
        this.searchRow.setVisibility(8);
        this.searchToggle.setOnClickListener(new PlexActivity$$ExternalSyntheticLambda9(this));
        SearchKeyboard.bind(this, this.search, new PlexActivity$$ExternalSyntheticLambda10(this), new PlexActivity$$ExternalSyntheticLambda12(this));
        getWindow().setSoftInputMode(19);
        this.status = text("Conecta tu cuenta Plex para ver tus bibliotecas", 13);
        this.root.addView(this.status);
        this.grid = new ResponsiveCatalogGrid(this);
        GridView r4 = this.grid;
        if ((getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density) < 600.0f) goto L13;
        int r13 = 5;
    L14:
        r4.setNumColumns(r13);
        this.grid.setHorizontalSpacing(m406dp(12));
        this.grid.setVerticalSpacing(m406dp(14));
        this.grid.setClipToPadding(false);
        this.grid.setPadding(0, m406dp(10), 0, m406dp(12));
        this.root.addView(this.grid, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        this.adapter = new C10231(this);
        this.grid.setAdapter(this.adapter);
        if (AppTheme.cine(this) == true) goto L17;
    L19:
        Button r5 = new Button(this);
        this.moreButton = r5;
        r5.setText("Página siguiente");
        r5.setOnClickListener(new PlexActivity$$ExternalSyntheticLambda13(this));
        this.root.addView(r5);
        this.grid.setOnItemClickListener(new PlexActivity$$ExternalSyntheticLambda14(this));
        this.grid.setOnItemLongClickListener(new PlexActivity$$ExternalSyntheticLambda15(this));
        CatalogNavigation.bind(this.grid, this.grid.getNumColumns());
        this.search.addTextChangedListener(new C10253(this));
        Object r6 = new AppStorage(this).read("plex-import.dat");     // Catch: Exception -> L29
        if ((r6 instanceof ArrayList) == false) goto L31;
        Iterator r8 = ((ArrayList) r6).iterator();     // Catch: Exception -> L29
    L24:
        if (r8.hasNext() == false) goto L31;
        Object r14 = r8.next();     // Catch: Exception -> L29
        if ((r14 instanceof PlexImport.Server) == false) goto L24;
        this.imported.add((PlexImport.Server) r14);     // Catch: Exception -> L29
    L31:
        if (this.toolsOnly == true) goto L39;
        if (this.imported.isEmpty() == true) goto L36;
        showImported();
        goto L39
    L36:
        if (this.token.isEmpty() == true) goto L39;
        discover();
    L39:
        if (getIntent().hasExtra("panelTool") == false) goto L41;
        new Handler(Looper.getMainLooper()).post(new PlexActivity$$ExternalSyntheticLambda2(this));
    L41:
        if (r18 == null) goto L43;
        this.search.setText(r18.getString("searchQuery", ""));
        setSearchExpanded(r18.getBoolean("searchExpanded", false), false);
    L43:
        this.root.setFocusable(false);
        this.root.setFocusableInTouchMode(false);
        if (this.toolsOnly == false) goto L54;
        this.root.removeAllViews();
        this.root.addView(text("Plex · Herramientas", 26));
        this.root.addView(text("Configura tu cuenta y tus servidores. El contenido se abre desde la tarjeta Plex del inicio.", 15));
        this.root.addView(this.status);
        addTool("Conectar / desconectar cuenta", new PlexActivity$$ExternalSyntheticLambda3(this));
        addTool("Importar JSON Plex", new PlexActivity$$ExternalSyntheticLambda4(this));
        addTool("Ver servidores configurados", new PlexActivity$$ExternalSyntheticLambda5(this));
        return;
    L54:
        return;
    L17:
        if (this.toolsOnly == true) goto L19;
        this.grid.setChoiceMode(1);
        int r7 = this.root.indexOfChild(this.grid);
        this.cineHero = new CineHero(this, this.tmdb, this.logos);
        this.cineHero.setVisibility(8);
        this.root.addView(this.cineHero, r7, CineHero.previewLayout(this));
        ((LinearLayout.LayoutParams) this.grid.getLayoutParams()).weight = 1.0f;
        this.grid.setOnItemSelectedListener(new C10242(this));
        goto L19
    L13:
        r13 = 2;
        goto L14
    L9:
        r12 = 24.0f;
        goto L10
    L5:
        r11 = false;
        goto L6
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.generation++;
        this.filterGeneration++;
        this.filterWorker.shutdownNow();
        this.searchHandler.removeCallbacksAndMessages(null);
        this.worker.shutdownNow();
        if (this.tmdb == null) goto L5;
        this.tmdb.close();
    L5:
        this.logos.close();
        super.onDestroy();
    }

    @Override // android.app.Activity
    protected void onPause() {
        this.screenVisible = false;
        this.filterGeneration++;
        this.filterWorker.getQueue().clear();
        this.searchHandler.removeCallbacks(this.searchFilter);
        this.tmdb.pauseAutomatic();
        this.logos.pause();
        if (this.cineHero == null) goto L5;
        this.cineHero.suspend();
    L5:
        super.onPause();
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.screenVisible = true;
        this.tmdb.resumeAutomatic();
        this.logos.resume();
        this.playbackLaunching = false;
        ParentalControl.requireKey(this, "kind:Plex", new PlexActivity$$ExternalSyntheticLambda45(this));
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle r3) {
        super.onSaveInstanceState(r3);
        r3.putString("searchQuery", this.search.getText().toString());
        if (this.searchRow.getVisibility() != 0) goto L5;
        boolean r0 = true;
    L6:
        r3.putBoolean("searchExpanded", r0);
        return;
    L5:
        r0 = false;
        goto L6
    }

    void openPanelToolAction(String r4) {
        if (r4.equals("Conectar cuenta Plex") == false) goto L6;
        connect();
        return;
    L6:
        if (r4.equals("Importar JSON Plex") == false) goto L8;
        importMenu();
        return;
    L8:
        AlertDialog.Builder r0 = new AlertDialog.Builder(this).setTitle("Servidores Plex");
        if (this.imported.isEmpty() == false) goto L11;
        String r1 = "No hay servidores importados";
    L12:
        r0.setMessage(r1).setPositiveButton("Cerrar", null).show();
        return;
    L11:
        r1 = TextUtils.join("\n", (Iterable) this.imported.stream().map(new PlexActivity$$ExternalSyntheticLambda49()).collect(Collectors.toList()));
        goto L12
    }

    @Override // es.antonio.m3u.ChdtvScreen
    protected boolean settingsChrome() {
        return this.toolsOnly;
    }
}
