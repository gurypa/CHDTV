package es.antonio.m3u;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.util.LruCache;
import androidx.media3.common.PlaybackException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.text.Typography;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class TmdbService {
    private final Context app;
    private final ThreadPoolExecutor artworkPool;
    private volatile boolean automaticPaused;
    volatile SharedPreferences cache;
    private volatile boolean closed;
    private final Handler main;
    private final LruCache<String, JSONObject> memory;
    private final Map<String, List<Runnable>> pending;
    private final ThreadPoolExecutor pool;
    private final ThreadPoolExecutor previewPool;
    private final SingleFlight<JSONObject> requestFlight;
    private final Map<String, Long> retry;
    private final SingleFlight<List<JSONObject>> searchFlight;
    private final LruCache<String, List<JSONObject>> searches;
    final SharedPreferences settings;
    private final LruCache<Entry, String> titleKeys;
    private final Transport transport;
    private final AtomicBoolean warming;

    private static final class ArtworkJob implements Runnable {
        final String key;
        final Runnable task;

        ArtworkJob(String r1, Runnable r2) {
            this.key = r1;
            this.task = r2;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.task.run();
        }
    }

    interface RelatedResults {
        void done(List<RelatedTitles.Result> r1, String r2);
    }

    interface Results {
        void done(List<JSONObject> r1, String r2);
    }

    interface Transport {
        JSONObject get(String r1, Map<String, String> r2) throws Exception;
    }

    public static /* synthetic */ void $r8$lambda$5bOaeXFln0iqUbCx8EWIfImLNMM(TmdbService r0) {
        r0.lambda$warmCache$4();
    }

    public static /* synthetic */ void $r8$lambda$8rAOuiI1lBWl1RJBfSq62zuZsi0(TmdbService r0, Results r1, Exception r2) {
        r0.lambda$previewFind$13(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$AtFQYJJ_lKCcD7Q-xNAOCGCzozE, reason: not valid java name */
    public static /* synthetic */ void m803$r8$lambda$AtFQYJJ_lKCcD7QxNAOCGCzozE(TmdbService r0, Results r1, Exception r2) {
        r0.lambda$find$10(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$G7DY85Q_Evv3Q_HFpDkWtX-kllg, reason: not valid java name */
    public static /* synthetic */ void m804$r8$lambda$G7DY85Q_Evv3Q_HFpDkWtXkllg(TmdbService r0, Results r1, Exception r2) {
        r0.lambda$season$19(r1, r2);
    }

    public static /* synthetic */ JSONObject $r8$lambda$J6bu1c__WA3vBG1DUnz4UiBMF3Y(TmdbService r0, String r1, Map r2) {
        return r0.http(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$VXOJhXHWKADRmgXk8tEE-NfHRNc, reason: not valid java name */
    public static /* synthetic */ void m805$r8$lambda$VXOJhXHWKADRmgXk8tEENfHRNc(TmdbService r0, Entry r1, JSONObject r2, Results r3) {
        r0.lambda$detailsOn$17(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$_ssW5bKTigy3gkzn4q7IimM1tPc(TmdbService r0, Results r1, JSONObject r2, Exception r3) {
        r0.lambda$detailsOn$16(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$eK85STnrkEAVh64CCLTHMj5DmW4(TmdbService r0, Entry r1, Results r2) {
        r0.lambda$find$11(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$hCVwE1TXVSq4lK8o-RDomQjYXJk, reason: not valid java name */
    public static /* synthetic */ void m806$r8$lambda$hCVwE1TXVSq4lK8oRDomQjYXJk(TmdbService r0, List r1) {
        r0.lambda$ensure$7(r1);
    }

    public static /* synthetic */ void $r8$lambda$pU3jNevqizxcVTtOxAutLcp7_vM(TmdbService r0, Entry r1, Results r2) {
        r0.lambda$previewFind$14(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$rGMXhl9VutYUwecn12QTDz5fDDE(TmdbService r0, Entry r1, String r2) {
        r0.lambda$ensure$8(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$tNESwT0BoIczfi-R2ClG7tVWrzM, reason: not valid java name */
    public static /* synthetic */ List m807$r8$lambda$tNESwT0BoIczfiR2ClG7tVWrzM(TmdbService r0, Entry r1) {
        return r0.lambda$search$6(r1);
    }

    public static /* synthetic */ void $r8$lambda$wGrZhkwD6Z5H2JBPvPZ8OaCGsug(TmdbService r0, Results r1, List r2) {
        r0.lambda$previewFind$12(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$ybPeczP41TON05bFAYWr4-MJn8s, reason: not valid java name */
    public static /* synthetic */ void m808$r8$lambda$ybPeczP41TON05bFAYWr4MJn8s(TmdbService r0, Entry r1, int r2, Results r3) {
        r0.lambda$season$20(r1, r2, r3);
    }

    public static /* synthetic */ JSONObject $r8$lambda$ydu4bga5tlPDXPjQd9G8RTVZquw(TmdbService r0, Transport r1, String r2, Map r3) {
        return r0.lambda$new$3(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$zPPpwsA0chlSOBY8TRkX7HwRyXs(TmdbService r0, List r1, Entry r2, JSONArray r3, RelatedResults r4) {
        r0.lambda$related$23(r1, r2, r3, r4);
    }

    TmdbService(Context r2) {
        this(r2, null);
    }

    TmdbService(Context r2, Transport r3) {
        this(r2, r3, false);
    }

    TmdbService(Context r14, Transport r15, boolean r16) {
        this.requestFlight = new SingleFlight();
        this.searchFlight = new SingleFlight();
        this.titleKeys = new LruCache(512);
        this.warming = new AtomicBoolean();
        this.main = new Handler(Looper.getMainLooper());
        this.pool = new ThreadPoolExecutor(2, 2, 30, TimeUnit.SECONDS, new ArrayBlockingQueue(24), backgroundThreads(), new ThreadPoolExecutor.AbortPolicy());
        this.artworkPool = new ThreadPoolExecutor(2, 2, 30, TimeUnit.SECONDS, new ArrayBlockingQueue(24), backgroundThreads(), new ThreadPoolExecutor.AbortPolicy());
        this.previewPool = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, new ArrayBlockingQueue(1), backgroundThreads(), new ThreadPoolExecutor.DiscardOldestPolicy());
        this.pending = new HashMap();
        this.memory = new LruCache(500);
        this.searches = new LruCache(100);
        this.retry = Collections.synchronizedMap(new HashMap());
        this.app = r14.getApplicationContext();
        this.settings = r14.getSharedPreferences("tmdb_settings", 0);
        if (BuildConfig.TMDB_API_KEY.isEmpty() == false) goto L5;
    L7:
        if (r16 == true) goto L9;
        preferences();
        warmCache();
    L9:
        if (r15 != null) goto L11;
        final Transport r0 = new TmdbService$$ExternalSyntheticLambda20(this);
    L12:
        this.transport = new TmdbService$$ExternalSyntheticLambda21(this, r0);
        return;
    L11:
        r0 = r15;
        goto L12
    L5:
        if (BuildConfig.TMDB_API_KEY.equals(this.settings.getString("api-key", "")) == true) goto L7;
        this.settings.edit().putString("api-key", BuildConfig.TMDB_API_KEY).apply();
        goto L7
    }

    private static ThreadFactory backgroundThreads() {
        return new TmdbService$$ExternalSyntheticLambda14();
    }

    private String cacheKey(Entry r3) {
        String r0 = this.titleKeys.get(r3);
        if (r0 != null) goto L6;
        String r1 = TmdbTitles.key(r3);
        this.titleKeys.put(r3, r1);
        return r1;
    L6:
        return r0;
    }

    private static List<JSONObject> copyResults(List<JSONObject> r5) throws JSONException {
        ArrayList r0 = new ArrayList();
        Iterator<JSONObject> r1 = r5.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0.add(new JSONObject(r1.next().toString()));
        goto L4
    L6:
        return r0;
    }

    private void detailsOn(Executor r8, Entry r9, JSONObject r10, Results r11) {
        r8.execute(new DemandTmdbDetails(this, r8, r9, r10, r11));     // Catch: RejectedExecutionException -> L4
        return;
    L5:
        r11.done(Arrays.asList(new JSONObject[]{r10}), null);
    }

    static String episodeImage(JSONObject r3) {
        String r0 = r3.optString("still_path", "");
        if (r0.matches("/[A-Za-z0-9._/-]+") == true) goto L5;
        return "";
    L5:
        return "https://image.tmdb.org/t/p/w300" + r0;
    }

    static JSONObject exact(Entry r10, List<JSONObject> r11) {
        JSONObject r0 = null;
        String r1 = TmdbTitles.normalize(TmdbTitles.query(r10));
        Iterator<JSONObject> r2 = r11.iterator();
    L4:
        if (r2.hasNext() == false) goto L28;
        JSONObject r3 = r2.next();
        if ("tv".equals(TmdbTitles.type(r10)) == false) goto L8;
        String r4 = "first_air_date";
    L9:
        String r5 = r3.optString(r4);
        if (TmdbTitles.year(r10).isEmpty() == true) goto L14;
        if (r5.startsWith(TmdbTitles.year(r10)) == false) goto L4;
    L14:
        boolean r6 = false;
        String[] r7 = {"title", "name", "original_title", "original_name"};
        int r8 = 0;
    L16:
        if (r8 >= 4) goto L21;
        if (r1.equals(TmdbTitles.normalize(r3.optString(r7[r8], ""))) == false) goto L20;
        r6 = true;
    L20:
        r8 = r8 + 1;
        goto L16
    L21:
        if (r6 == false) goto L4;
        if (r0 != null) goto L24;
    L27:
        r0 = r3;
        goto L4
    L24:
        if (r0.optInt("id") == r3.optInt("id")) goto L27;
        return null;
    L8:
        r4 = "release_date";
        goto L9
    L28:
        return r0;
    }

    private JSONObject http(String r12, Map<String, String> r13) throws Exception {
        StringBuilder r0 = new StringBuilder("https://api.themoviedb.org/3/").append(r12).append("?language=").append(URLEncoder.encode(r13.getOrDefault("language", "es-ES"), "UTF-8")).append("&api_key=").append(URLEncoder.encode(apiKey(), "UTF-8"));
        Iterator<Map.Entry<String, String>> r1 = r13.entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L8;
        Map.Entry<String, String> r4 = r1.next();
        if ("language".equals(r4.getKey()) == true) goto L4;
        r0.append(Typography.amp).append(r4.getKey()).append('=').append(URLEncoder.encode(r4.getValue(), "UTF-8"));
        goto L4
    L8:
        HttpURLConnection r2 = (HttpURLConnection) new URL(r0.toString()).openConnection();
        r2.setConnectTimeout(PlaybackException.ERROR_CODE_DRM_UNSPECIFIED);
        r2.setReadTimeout(PlaybackException.ERROR_CODE_DRM_UNSPECIFIED);
        r2.setInstanceFollowRedirects(false);
        int r3 = r2.getResponseCode();     // Catch: Throwable -> L50 IOException -> L52
        if (r3 != 200) goto L12;
        InputStream r5 = r2.getInputStream();     // Catch: Throwable -> L50 IOException -> L52
        ByteArrayOutputStream r6 = new ByteArrayOutputStream();     // Catch: Throwable -> L43
        byte[] r7 = new byte[8192];     // Catch: Throwable -> L37
    L24:
        int r8 = r5.read(r7);     // Catch: Throwable -> L37
        if (r8 == (-1)) goto L31;
        if ((r6.size() + r8) > 2097152) goto L30;
        r6.write(r7, 0, r8);     // Catch: Throwable -> L37
        goto L24
    L30:
        throw new IOException("Respuesta TMDB demasiado grande");     // Catch: Throwable -> L37
    L31:
        JSONObject r9 = new JSONObject(new String(r6.toByteArray(), StandardCharsets.UTF_8));     // Catch: Throwable -> L37
        r6.close();     // Catch: Throwable -> L43
        if (r5 == null) goto L35;
        r5.close();     // Catch: Throwable -> L50 IOException -> L52
    L35:
        r2.disconnect();
        return r9;
    L37:
        th = move-exception;
        r6.close();     // Catch: Throwable -> L40
    L42:
        throw th;     // Catch: Throwable -> L43
    L40:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L43
    L43:
        th = move-exception;
        if (r5 != null) goto L66;
    L49:
        throw th;     // Catch: Throwable -> L50 IOException -> L52
    L66:
        r5.close();     // Catch: Throwable -> L47
    L47:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L50 IOException -> L52
        goto L49
    L12:
        if (r3 != 401) goto L14;
        String r10 = "Clave TMDB no válida";
    L19:
        throw new IOException(r10);     // Catch: Throwable -> L50 IOException -> L52
    L14:
        if (r3 != 429) goto L16;
        r10 = "TMDB pide esperar antes de consultar";
        goto L19
    L16:
        r10 = "TMDB no disponible (HTTP " + r3 + ")";     // Catch: Throwable -> L50 IOException -> L52
    L52:
        e = move-exception;
        if (e.getMessage() == null) goto L58;
        if (e.getMessage().startsWith("TMDB") == false) goto L58;
    L61:
        throw e;     // Catch: Throwable -> L50
    L58:
        if (e.getMessage() == null) goto L63;
        if (e.getMessage().startsWith("Clave TMDB") == true) goto L61;
    L63:
        throw new IOException("No hay conexión con TMDB");     // Catch: Throwable -> L50
    L50:
        th = move-exception;
        r2.disconnect();
        throw th;
    }

    static /* synthetic */ void lambda$backgroundThreads$0(Runnable r1) {
        Process.setThreadPriority(10);
        r1.run();
    }

    static /* synthetic */ Thread lambda$backgroundThreads$1(final Runnable r3) {
        return new Thread(new TmdbService$$ExternalSyntheticLambda7(r3), "CHDTVMetadata");
    }

    static /* synthetic */ void lambda$detailsOn$15(Results r2, JSONObject r3) {
        r2.done(Arrays.asList(new JSONObject[]{r3}), null);
    }

    private /* synthetic */ void lambda$detailsOn$16(Results r3, JSONObject r4, Exception r5) {
        r3.done(Arrays.asList(new JSONObject[]{r4}), message(r5));
    }

    private /* synthetic */ void lambda$detailsOn$17(Entry r5, final JSONObject r6, final Results r7) {
        final JSONObject r0 = this.transport.get(TmdbTitles.type(r5) + "/" + r6.getInt("id"), Collections.singletonMap("append_to_response", "credits,recommendations,similar,translations"));     // Catch: Exception -> L4
        applyOverviewFallback(r0);     // Catch: Exception -> L4
        remember(r5, r0);     // Catch: Exception -> L4
        this.main.post(new TmdbService$$ExternalSyntheticLambda2(r7, r0));     // Catch: Exception -> L4
        return;
    L4:
        e = move-exception;
        this.main.post(new TmdbService$$ExternalSyntheticLambda3(this, r7, r6, e));
    }

    private /* synthetic */ void lambda$ensure$7(List r3) {
        if (this.closed == false) goto L5;
        return;
    L5:
        if (this.automaticPaused == true) goto L12;
        Iterator r0 = r3.iterator();
    L8:
        if (r0.hasNext() == false) goto L13;
        ((Runnable) r0.next()).run();
        goto L8
    L13:
        return;
    }

    private /* synthetic */ void lambda$ensure$8(Entry r9, String r10) {
        boolean r0 = false;
        JSONObject r1 = cached(r9);     // Catch: Throwable -> L28 Exception -> L30
        if (r1 != null) goto L6;
    L7:
        r1 = exact(r9, search(r9));     // Catch: Throwable -> L28 Exception -> L30
    L8:
        if (r1 == null) goto L11;
        remember(r9, r1);     // Catch: Throwable -> L28 Exception -> L30
        r0 = true;
    L10:
        boolean r2 = r0;
        Map<String, List<Runnable>> r3 = this.pending;
        monitor-enter(r3);
        final List<Runnable> r4 = this.pending.remove(r10);     // Catch: Throwable -> L25
        monitor-exit(r3);     // Catch: Throwable -> L25
        if (r2 == false) goto L69;
        if (r4 == null) goto L69;
        if (this.closed == true) goto L69;
        if (this.automaticPaused == true) goto L69;
        this.main.post(new TmdbService$$ExternalSyntheticLambda10(this, r4));
    L69:
        return;
    L25:
        th = move-exception;
        throw th;
    L11:
        this.retry.put(r10, Long.valueOf(System.currentTimeMillis() + 86400000));     // Catch: Throwable -> L28 Exception -> L30
        goto L10
    L6:
        if (r1.optLong("saved") > (System.currentTimeMillis() - 1209600000)) goto L8;
    L28:
        th = move-exception;
        monitor-enter(this.pending);
        final List<Runnable> r5 = this.pending.remove(r10);     // Catch: Throwable -> L59
        if (0 == 0) goto L58;
        if (r5 == null) goto L58;
        if (this.closed == true) goto L58;
        if (this.automaticPaused == true) goto L58;
        this.main.post(new TmdbService$$ExternalSyntheticLambda10(this, r5));
    L58:
        throw th;
    L59:
        th = move-exception;
        throw th;
    L31:
        this.retry.put(r10, Long.valueOf(System.currentTimeMillis() + 300000));     // Catch: Throwable -> L28
        monitor-enter(this.pending);
        final List<Runnable> r6 = this.pending.remove(r10);     // Catch: Throwable -> L44
        if (0 == 0) goto L70;
        if (r6 != null) goto L39;
        return;
    L39:
        if (this.closed == false) goto L41;
        return;
    L41:
        if (this.automaticPaused == true) goto L73;
        this.main.post(new TmdbService$$ExternalSyntheticLambda10(this, r6));
        return;
    L73:
        return;
    L70:
        return;
    L44:
        th = move-exception;
        throw th;
    }

    private /* synthetic */ void lambda$find$10(Results r3, Exception r4) {
        r3.done(Collections.emptyList(), message(r4));
    }

    private /* synthetic */ void lambda$find$11(Entry r4, final Results r5) {
        final List<JSONObject> r0 = search(r4);     // Catch: Exception -> L4
        this.main.post(new TmdbService$$ExternalSyntheticLambda11(r5, r0));     // Catch: Exception -> L4
        return;
    L4:
        e = move-exception;
        this.main.post(new TmdbService$$ExternalSyntheticLambda17(this, r5, e));
    }

    static /* synthetic */ void lambda$find$9(Results r1, List r2) {
        r1.done(r2, null);
    }

    static /* synthetic */ JSONObject lambda$new$2(Transport r7, String r8, Map r9) throws Exception {
        long r0 = SystemClock.elapsedRealtime();
        JSONObject r2 = r7.get(r8, r9);     // Catch: Exception -> L5
        BlackBox.event("TMDB_RESPONSE", "duration_ms=" + (SystemClock.elapsedRealtime() - r0));     // Catch: Exception -> L5
        return r2;
    L5:
        e = move-exception;
        BlackBox.failure("TMDB_ERROR", e);
        throw e;
    }

    private /* synthetic */ JSONObject lambda$new$3(final Transport r5, final String r6, final Map r7) throws Exception {
        return new JSONObject(this.requestFlight.get(r6 + "\n" + new TreeMap(r7).toString(), new TmdbService$$ExternalSyntheticLambda6(r5, r6, r7)).toString());
    }

    private /* synthetic */ void lambda$previewFind$12(Results r2, List r3) {
        if (this.closed == false) goto L5;
        return;
    L5:
        if (this.automaticPaused == true) goto L9;
        r2.done(r3, null);
        return;
    }

    private /* synthetic */ void lambda$previewFind$13(Results r3, Exception r4) {
        if (this.closed == false) goto L5;
        return;
    L5:
        if (this.automaticPaused == true) goto L9;
        r3.done(Collections.emptyList(), message(r4));
        return;
    }

    private /* synthetic */ void lambda$previewFind$14(Entry r5, final Results r6) {
        JSONObject r0 = cached(r5);     // Catch: Exception -> L8
        if (r0 != null) goto L5;
        final List<JSONObject> r1 = search(r5);     // Catch: Exception -> L8
    L6:
        this.main.post(new TmdbService$$ExternalSyntheticLambda12(this, r6, r1));     // Catch: Exception -> L8
        return;
    L5:
        r1 = Arrays.asList(new JSONObject[]{r0});     // Catch: Exception -> L8
    L8:
        e = move-exception;
        this.main.post(new TmdbService$$ExternalSyntheticLambda13(this, r6, e));
    }

    static /* synthetic */ void lambda$related$21(RelatedResults r1, List r2) {
        r1.done(r2, null);
    }

    static /* synthetic */ void lambda$related$22(RelatedResults r2) {
        r2.done(Collections.emptyList(), "No se pudieron cargar los relacionados");
    }

    private /* synthetic */ void lambda$related$23(List r4, Entry r5, JSONArray r6, final RelatedResults r7) {
        final List<RelatedTitles.Result> r0 = new RelatedTitles(r4, r5, this).resolve(r6);     // Catch: Exception -> L4
        this.main.post(new TmdbService$$ExternalSyntheticLambda15(r7, r0));     // Catch: Exception -> L4
        return;
    L5:
        this.main.post(new TmdbService$$ExternalSyntheticLambda16(r7));
    }

    static /* synthetic */ long lambda$remember$5(Map r2, Map.Entry r3) {
        return ((Long) r2.get(r3.getKey())).longValue();
    }

    private /* synthetic */ List lambda$search$6(Entry r2) throws Exception {
        return searchUnshared(r2);
    }

    static /* synthetic */ void lambda$season$18(Results r1, List r2) {
        r1.done(r2, null);
    }

    private /* synthetic */ void lambda$season$19(Results r3, Exception r4) {
        r3.done(Collections.emptyList(), message(r4));
    }

    private /* synthetic */ void lambda$season$20(Entry r18, int r19, final Results r20) {
        JSONObject r0 = cached(r18);     // Catch: Exception -> L45
        if (r0 != null) goto L10;
        r0 = exact(r18, search(r18));     // Catch: Exception -> L45
        if (r0 == null) goto L9;
        remember(r18, r0);     // Catch: Exception -> L45
        goto L10
    L9:
        throw new IOException("No se encontró una serie inequívoca");     // Catch: Exception -> L45
    L10:
        int r1 = r0.getInt("id");     // Catch: Exception -> L45
        String r2 = "season:" + r1 + ":" + r19;     // Catch: Exception -> L45
        JSONObject r10 = null;
        JSONObject r3 = new JSONObject(preferences().getString(r2, "{}"));     // Catch: Exception -> L17
        if (r3.optLong("saved") <= (System.currentTimeMillis() - 1209600000)) goto L19;
        if (r3.has("episodes") == false) goto L19;
        r10 = r3;
    L19:
        if (r10 != null) goto L25;
        r10 = this.transport.get("tv/" + r1 + "/season/" + r19, Collections.emptyMap());     // Catch: Exception -> L45
        r10.put("saved", System.currentTimeMillis());     // Catch: Exception -> L45
        if (preferences().getAll().size() >= 500) goto L23;
    L24:
        preferences().edit().putString(r2, r10.toString()).apply();     // Catch: Exception -> L45
        goto L25
    L23:
        if (preferences().contains(r2) == true) goto L24;
    L25:
        seasonOverviewFallback(r10, r1, r19);     // Catch: Exception -> L45
        if (preferences().getAll().size() >= 500) goto L28;
    L29:
        preferences().edit().putString(r2, r10.toString()).apply();     // Catch: Exception -> L45
    L30:
        final ArrayList r4 = new ArrayList();     // Catch: Exception -> L45
        JSONArray r5 = r10.optJSONArray("episodes");     // Catch: Exception -> L45
        if (r5 == null) goto L43;
        int r6 = 0;
    L34:
        if (r6 >= r5.length()) goto L43;
        JSONObject r11 = r5.optJSONObject(r6);     // Catch: Exception -> L45
        if (r11 == null) goto L42;
        if (r11.optInt("season_number", -1) != r19) goto L42;
        if (r11.optInt("episode_number", -1) < 0) goto L42;
        r4.add(r11);     // Catch: Exception -> L45
    L42:
        r6 = r6 + 1;     // Catch: Exception -> L45
    L43:
        this.main.post(new TmdbService$$ExternalSyntheticLambda18(r20, r4));     // Catch: Exception -> L45
        return;
    L28:
        if (preferences().contains(r2) == false) goto L30;
    L45:
        e = move-exception;
        this.main.post(new TmdbService$$ExternalSyntheticLambda19(this, r20, e));
    }

    private /* synthetic */ void lambda$warmCache$4() {
        if (this.closed == false) goto L5;
        return;
    L5:
        long r0 = SystemClock.elapsedRealtime();
        BlackBox.event("TMDB_CACHE_WARM_START", "");
        int r2 = 0;
        Iterator<Map.Entry<String, ?>> r3 = preferences().getAll().entrySet().iterator();
    L7:
        if (r3.hasNext() == false) goto L19;
        Map.Entry<String, ?> r4 = r3.next();
        if (this.closed == true) goto L10;
        if ((r4.getValue() instanceof String) == false) goto L7;
        JSONObject r5 = new JSONObject((String) r4.getValue());     // Catch: Exception -> L17
        if (this.memory.get(r4.getKey()) != null) goto L16;
        this.memory.put(r4.getKey(), r5);     // Catch: Exception -> L17
    L16:
        r2 = r2 + 1;
        goto L7
    L10:
        return;
    L19:
        BlackBox.event("TMDB_CACHE_WARM_END", "duration_ms=" + (SystemClock.elapsedRealtime() - r0) + " items=" + r2);
    }

    private String message(Exception r2) {
        if ((r2 instanceof IOException) == true) goto L7;
        return "No se pudo consultar TMDB";
    L7:
        return r2.getMessage();
    }

    static String poster(JSONObject r3) {
        String r0 = r3.optString("poster_path", "");
        if (r0.matches("/[A-Za-z0-9._/-]+") == true) goto L5;
        return "";
    L5:
        return "https://image.tmdb.org/t/p/w342" + r0;
    }

    private SharedPreferences preferences() {
        SharedPreferences r0 = this.cache;
        if (r0 != null) goto L13;
        monitor-enter(this);
        r0 = this.cache;     // Catch: Throwable -> L10
        if (r0 != null) goto L8;
        SharedPreferences r1 = this.app.getSharedPreferences("tmdb_cache", 0);     // Catch: Throwable -> L10
        r0 = r1;
        this.cache = r1;     // Catch: Throwable -> L10
    L8:
        monitor-exit(this);     // Catch: Throwable -> L10
    L10:
        th = move-exception;
        throw th;
    L13:
        return r0;
    }

    private List<JSONObject> searchUnshared(Entry r10) throws Exception {
        String r0 = cacheKey(r10);
        List<JSONObject> r1 = this.searches.get(r0);
        if (r1 != null) goto L5;
        LinkedHashMap r2 = new LinkedHashMap();
        r2.put("query", TmdbTitles.query(r10));
        r2.put("include_adult", "false");
        String r3 = TmdbTitles.year(r10);
        if (r3.isEmpty() == false) goto L9;
    L13:
        JSONArray r4 = this.transport.get("search/" + TmdbTitles.type(r10), r2).optJSONArray("results");
        ArrayList r5 = new ArrayList();
        if (r4 == null) goto L19;
        int r6 = 0;
    L17:
        if (r6 >= Math.min(r4.length(), 20)) goto L19;
        r5.add(r4.getJSONObject(r6));
        r6 = r6 + 1;
    L19:
        this.searches.put(r0, copyResults(r5));
        return r5;
    L9:
        if ("tv".equals(TmdbTitles.type(r10)) == false) goto L11;
        String r7 = "first_air_date_year";
    L12:
        r2.put(r7, r3);
        goto L13
    L11:
        r7 = "primary_release_year";
        goto L12
    L5:
        return copyResults(r1);
    }

    private void seasonOverviewFallback(JSONObject r19, int r20, int r21) throws Exception {
        int r2 = r21;
        if (r19.optBoolean("overview_fallback_checked") == false) goto L5;
        return;
    L5:
        JSONArray r4 = r19.optJSONArray("episodes");
        boolean r5 = false;
        int r8 = -1;
        if (r4 == null) goto L19;
        int r9 = 0;
    L9:
        if (r9 >= r4.length()) goto L19;
        JSONObject r10 = r4.optJSONObject(r9);
        if (r10 == null) goto L17;
        if (r10.optInt("season_number", -1) != r2) goto L17;
        if (r10.optString("overview").trim().isEmpty() == false) goto L17;
        r5 = true;
    L17:
        r9 = r9 + 1;
    L19:
        if (englishFallback() == false) goto L62;
        if (r5 == false) goto L62;
        JSONArray r0 = this.transport.get("tv/" + r20 + "/season/" + r2, Collections.singletonMap("language", "en-US")).optJSONArray("episodes");     // Catch: Exception -> L56
        HashMap r11 = new HashMap();     // Catch: Exception -> L56
        if (r0 == null) goto L36;
        int r13 = 0;
    L29:
        if (r13 >= r0.length()) goto L36;
        JSONObject r14 = r0.optJSONObject(r13);     // Catch: Exception -> L56
        if (r14 == null) goto L35;
        if (r14.optInt("season_number", -1) != r2) goto L35;
        r11.put(Integer.valueOf(r14.optInt("episode_number", -1)), r14);     // Catch: Exception -> L56
    L35:
        r13 = r13 + 1;     // Catch: Exception -> L56
    L36:
        int r15 = 0;
    L37:
        if (r15 >= r4.length()) goto L62;
        JSONObject r16 = r4.optJSONObject(r15);     // Catch: Exception -> L56
        if (r16 == null) goto L54;
        if (r16.optInt("season_number", r8) != r2) goto L54;
        if (r16.optString("overview").trim().isEmpty() == false) goto L54;
        JSONObject r17 = (JSONObject) r11.get(Integer.valueOf(r16.optInt("episode_number", r8)));     // Catch: Exception -> L56
        if (r17 == null) goto L54;
        if (r17.optString("overview").trim().isEmpty() == true) goto L54;
        r16.put("overview", r17.optString("overview")).put("overview_language", "en");     // Catch: Exception -> L56
    L54:
        r15 = r15 + 1;
        r2 = r21;
        r0 = r0;
        r8 = -1;
    L62:
        r19.put("overview_fallback_checked", true);
    }

    static String synopsis(JSONObject r3) {
        String r0 = r3.optString("overview").trim();
        if (r0.isEmpty() == false) goto L6;
        return "Sin sinopsis disponible";
    L6:
        if ("en".equals(r3.optString("overview_language")) == false) goto L9;
        return "Sinopsis en inglés (no disponible en castellano)\n\n" + r0;
    L9:
        return r0;
    }

    String apiKey() {
        if (BuildConfig.TMDB_API_KEY.isEmpty() == true) goto L5;
        return BuildConfig.TMDB_API_KEY;
    L5:
        return this.settings.getString("api-key", "");
    }

    void applyOverviewFallback(JSONObject r14) throws JSONException {
        r14.put("overview_fallback_checked", true);
        r14.put("overview_language", "es");
        if (englishFallback() == true) goto L5;
        return;
    L5:
        if (r14.optString("overview").trim().isEmpty() == false) goto L49;
        JSONObject r3 = r14.optJSONObject("translations");
        if (r3 != null) goto L10;
        JSONArray r2 = null;
    L11:
        String r4 = "";
        if (r2 == null) goto L38;
        int r6 = 0;
    L15:
        if (r6 >= r2.length()) goto L38;
        JSONObject r7 = r2.optJSONObject(r6);
        if (r7 == null) goto L36;
        if ("en".equals(r7.optString("iso_639_1")) == false) goto L36;
        JSONObject r8 = r7.optJSONObject("data");
        if (r8 != null) goto L24;
        String r9 = "";
    L26:
        if (r9.isEmpty() == true) goto L36;
        if (r4.isEmpty() == false) goto L31;
    L32:
        r4 = r9;
    L34:
        if ("US".equals(r7.optString("iso_3166_1")) == false) goto L36;
    L31:
        if ("US".equals(r7.optString("iso_3166_1")) == false) goto L34;
    L24:
        r9 = r8.optString("overview").trim();
    L36:
        r6 = r6 + 1;
    L38:
        if (r4.isEmpty() == true) goto L48;
        r14.put("overview", r4).put("overview_language", "en");
        return;
    L48:
        return;
    L10:
        r2 = r3.optJSONArray("translations");
        goto L11
    }

    JSONObject cached(Entry r4) {
        String r5 = cacheKey(r4);     // Catch: Exception -> L15
        JSONObject r1 = this.memory.get(r5);     // Catch: Exception -> L15
        if (r1 != null) goto L6;
        JSONObject r2 = DemandMetadata.get(this.app, r5);     // Catch: Exception -> L15
        if (r2 != null) goto L13;
        String r3 = preferences().getString(r5, null);     // Catch: Exception -> L15
        if (r3 != null) goto L12;
        return null;
    L12:
        JSONObject r6 = new JSONObject(r3);     // Catch: Exception -> L15
        DemandMetadata.put(this.app, r5, r6);     // Catch: Exception -> L15
        r2 = r6;
    L13:
        this.memory.put(r5, r2);     // Catch: Exception -> L15
        return new JSONObject(r2.toString());
    L6:
        return new JSONObject(r1.toString());
    L16:
        return null;
    }

    void cancelArtwork() {
        ArrayList r0 = new ArrayList();
        Map<String, List<Runnable>> r1 = this.pending;
        monitor-enter(r1);
        this.artworkPool.getQueue().drainTo(r0);     // Catch: Throwable -> L12
        Iterator r2 = r0.iterator();     // Catch: Throwable -> L12
    L6:
        if (r2.hasNext() == false) goto L10;
        Runnable r3 = (Runnable) r2.next();     // Catch: Throwable -> L12
        if ((r3 instanceof ArtworkJob) == false) goto L6;
        this.pending.remove(((ArtworkJob) r3).key);     // Catch: Throwable -> L12
        goto L6
    L10:
        monitor-exit(r1);     // Catch: Throwable -> L12
        return;
    L12:
        th = move-exception;
        throw th;
    }

    void cancelPreview() {
        this.previewPool.getQueue().clear();
    }

    void clearCache() {
        this.memory.evictAll();
        this.searches.evictAll();
        this.retry.clear();
        DemandMetadata.clear(this.app);
        preferences().edit().clear().apply();
    }

    void close() {
        this.closed = true;
        this.previewPool.shutdownNow();
        this.artworkPool.shutdownNow();
        Map<String, List<Runnable>> r0 = this.pending;
        monitor-enter(r0);
        this.pending.clear();     // Catch: Throwable -> L8
        monitor-exit(r0);     // Catch: Throwable -> L8
        this.pool.shutdownNow();
        this.main.removeCallbacksAndMessages(null);
        return;
    L8:
        th = move-exception;
        throw th;
    }

    boolean detailCanLoad() {
        return !this.closed;
    }

    void detailFind(Entry r3, Results r4) {
        DemandDetailCache.request(this.pool, this.main, this, r3, r4);
    }

    void details(Entry r2, JSONObject r3, Results r4) {
        detailsOn(this.pool, r2, r3, r4);
    }

    boolean detailsActive() {
        return !this.closed;
    }

    void detailsOriginal(Executor r7, final Entry r8, final JSONObject r9, final Results r10) {
        JSONObject r0 = cached(r8);
        if (r0 != null) goto L5;
    L19:
        r7.execute(new TmdbService$$ExternalSyntheticLambda24(this, r8, r9, r10));     // Catch: RejectedExecutionException -> L16
        return;
    L17:
        r10.done(Arrays.asList(new JSONObject[]{r9}), null);
        return;
    L5:
        if (r0.optInt("id") != r9.optInt("id")) goto L19;
        if (r0.has("credits") == false) goto L19;
        if (r0.has("recommendations") == false) goto L19;
        if (r0.optBoolean("overview_fallback_checked") == false) goto L19;
        r10.done(Arrays.asList(new JSONObject[]{r0}), null);
    }

    boolean enabled() {
        if (this.settings.getBoolean("enabled", true) == true) goto L5;
    L7:
        return false;
    L5:
        if (apiKey().isEmpty() == true) goto L7;
        return true;
    }

    boolean englishFallback() {
        return this.settings.getBoolean("english-overview-fallback", true);
    }

    void ensure(final Entry r10, Runnable r11) {
        if (this.closed == false) goto L5;
        return;
    L5:
        if (this.automaticPaused == false) goto L7;
        return;
    L7:
        if (enabled() == true) goto L9;
        return;
    L9:
        if ("Canales".equals(r10.kind) == true) goto L57;
        JSONObject r0 = peek(r10);
        if (r0 != null) goto L14;
    L16:
        final String r1 = cacheKey(r10);
        Long r2 = this.retry.get(r1);
        if (r2 != null) goto L19;
    L21:
        Map<String, List<Runnable>> r3 = this.pending;
        monitor-enter(r3);
        List<Runnable> r4 = this.pending.get(r1);     // Catch: Throwable -> L45
        if (r4 != null) goto L26;
        this.pending.put(r1, new ArrayList(Arrays.asList(new Runnable[]{r11})));     // Catch: Throwable -> L45
        monitor-exit(r3);     // Catch: Throwable -> L45
        this.artworkPool.execute(new ArtworkJob(r1, new TmdbService$$ExternalSyntheticLambda1(this, r10, r1)));     // Catch: RejectedExecutionException -> L36
        return;
    L38:
        monitor-enter(this.pending);
        this.pending.remove(r1);     // Catch: Throwable -> L42
        return;
    L42:
        th = move-exception;
        throw th;
    L26:
        if (r4.size() < 4) goto L28;
    L30:
        monitor-exit(r3);     // Catch: Throwable -> L45
        return;
    L28:
        if (r4.contains(r11) == true) goto L30;
        r4.add(r11);     // Catch: Throwable -> L45
    L45:
        th = move-exception;
        throw th;
    L19:
        if (r2.longValue() <= System.currentTimeMillis()) goto L21;
        return;
    L14:
        if (r0.optLong("saved") <= (System.currentTimeMillis() - 1209600000)) goto L16;
        return;
    }

    void find(final Entry r4, final Results r5) {
        if (enabled() == true) goto L11;
        r5.done(Collections.emptyList(), "Activa TMDB y configura la clave API");
        return;
    L11:
        this.pool.execute(new TmdbService$$ExternalSyntheticLambda22(this, r4, r5));     // Catch: RejectedExecutionException -> L8
        return;
    L9:
        r5.done(Collections.emptyList(), "TMDB está ocupado. Vuelve a intentarlo");
    }

    void pauseAutomatic() {
        this.automaticPaused = true;
        cancelArtwork();
        cancelPreview();
    }

    JSONObject peek(Entry r3) {
        return this.memory.get(cacheKey(r3));
    }

    String poster(Entry r3) {
        JSONObject r0 = this.memory.get(cacheKey(r3));
        if (r0 != null) goto L5;
        return "";
    L5:
        return poster(r0);
    }

    void previewDetails(Entry r2, JSONObject r3, Results r4) {
        detailsOn(this.previewPool, r2, r3, r4);
    }

    void previewFind(final Entry r3, final Results r4) {
        if (this.closed == false) goto L5;
        return;
    L5:
        if (this.automaticPaused == false) goto L7;
        return;
    L7:
        if (enabled() == false) goto L13;
        this.previewPool.execute(new TmdbService$$ExternalSyntheticLambda23(this, r3, r4));
        return;
    }

    void related(List<Entry> r8, Entry r9, JSONArray r10, RelatedResults r11) {
        DemandRelated.request(this.pool, this.main, this, r8, r9, r10, r11);
    }

    void remember(Entry r6, JSONObject r7) {
        String r0 = cacheKey(r6);
        Object r1 = DemandMetadata.lock(r0);
        monitor-enter(r1);
        JSONObject r2 = new JSONObject(r7.toString());     // Catch: Throwable -> L19 Exception -> L21
        JSONObject r8 = cached(r6);     // Catch: Throwable -> L19 Exception -> L21
        if (r8 != null) goto L7;
    L17:
        r2.put("saved", System.currentTimeMillis());     // Catch: Throwable -> L19 Exception -> L21
        this.memory.put(r0, r2);     // Catch: Throwable -> L19 Exception -> L21
        DemandMetadata.put(this.app, r0, r2);     // Catch: Throwable -> L19 Exception -> L21
        goto L22
    L7:
        if (r8.optInt("id") != r2.optInt("id")) goto L17;
        if (r8.has("credits") == false) goto L17;
        if (r2.has("credits") == true) goto L17;
        Iterator<String> r9 = r2.keys();     // Catch: Throwable -> L19 Exception -> L21
    L14:
        if (r9.hasNext() == false) goto L15;
        String r3 = r9.next();     // Catch: Throwable -> L19 Exception -> L21
        r8.put(r3, r2.get(r3));     // Catch: Throwable -> L19 Exception -> L21
        goto L14
    L15:
        r2 = r8;
    L19:
        th = move-exception;
        throw th;
    L22:
        monitor-exit(r1);     // Catch: Throwable -> L19
    }

    void resumeAutomatic() {
        this.automaticPaused = false;
    }

    List<JSONObject> search(final Entry r4) throws Exception {
        return copyResults(this.searchFlight.get(cacheKey(r4), new TmdbService$$ExternalSyntheticLambda0(this, r4)));
    }

    void season(final Entry r4, final int r5, final Results r6) {
        if (enabled() == true) goto L11;
        r6.done(Collections.emptyList(), "TMDB no está configurado");
        return;
    L11:
        this.pool.execute(new TmdbService$$ExternalSyntheticLambda8(this, r4, r5, r6));     // Catch: RejectedExecutionException -> L8
        return;
    L9:
        r6.done(Collections.emptyList(), "TMDB está ocupado");
    }

    void warmCache() {
        BlackBox.event("TMDB_CACHE_DEFERRED", "per_title=true persistent_limit=20000");
    }
}
