package es.antonio.m3u;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.security.keystore.KeyGenParameterSpec;
import android.util.AtomicFile;
import androidx.media3.datasource.DataSpec;
import com.google.common.net.HttpHeaders;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import kotlin.text.Typography;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class DaznTokens {
    static final int JOB = 48116;
    static final String PREF = "dazn_token_state";
    static Context app;
    static JSONObject cache;
    static final Runnable due = null;
    static volatile boolean foreground;
    static int generation;
    static final Handler main = null;
    static CdnTokens.Pending pending;
    static long retryAt;
    static final ExecutorService worker = null;

    static {
        main = new Handler(Looper.getMainLooper());
        worker = Executors.newSingleThreadExecutor(new DaznTokens$$ExternalSyntheticLambda5());
        due = new DaznTokens$$ExternalSyntheticLambda6();
    }

    DaznTokens() {
    }

    static void arm(Context r6) {
        main.removeCallbacks(due);
        if (foreground == true) goto L5;
        return;
    L5:
        if (configured(r6) == false) goto L13;
        Handler r0 = main;
        Runnable r1 = due;
        if (pending == null) goto L9;
        long r2 = 300000;
    L10:
        r0.postDelayed(r1, r2);
        return;
    L9:
        r2 = Math.max(1000, Math.max(next(r6), retryAt) - System.currentTimeMillis());
        goto L10
    }

    static void background() {
        foreground = false;
        main.removeCallbacks(due);
    }

    static synchronized void configure(Context r17, String r18, String r19) throws Exception {
        monitor-enter(DaznTokens.class);
        JSONObject r2 = new JSONObject(r18);     // Catch: Throwable -> L39
        String r4 = r2.getJSONObject("AuthToken").getString("Token");     // Catch: Throwable -> L39
        DaznTokenClient.payload(r4);     // Catch: Throwable -> L39
        JSONArray r5 = new JSONObject(r19).getJSONArray("categories");     // Catch: Throwable -> L39
        JSONArray r7 = new JSONArray();     // Catch: Throwable -> L39
        HashSet r8 = new HashSet();     // Catch: Throwable -> L39
        int r9 = 0;
    L6:
        if (r9 >= r5.length()) goto L25;
        JSONArray r10 = r5.getJSONObject(r9).getJSONArray("channels");     // Catch: Throwable -> L39
        int r11 = 0;
    L9:
        if (r11 >= r10.length()) goto L23;
        JSONObject r12 = r10.getJSONObject(r11);     // Catch: Throwable -> L39
        String r13 = r12.getString("asset_id");     // Catch: Throwable -> L39
        String r14 = r12.getString("fileUrl");     // Catch: Throwable -> L39
        if (r8.add(r13) == false) goto L20;
        URI r15 = new URI(r14);     // Catch: Throwable -> L39
        if ("https".equalsIgnoreCase(r15.getScheme()) == false) goto L22;
        if (r15.getHost() == null) goto L22;
        if (r7.length() >= 500) goto L22;
        r7.put(new JSONObject().put("asset", r13).put("title", r12.getString("name")).put("id", r12.optString("tvg_id")).put("original", r14).put("current", importedCurrent(r12)));     // Catch: Throwable -> L39
    L22:
        throw new IOException("Mapa DAZN inválido o demasiado grande");     // Catch: Throwable -> L39
    L20:
        r11 = r11 + 1;     // Catch: Throwable -> L39
        r5 = r5;
        goto L9
    L23:
        r9 = r9 + 1;     // Catch: Throwable -> L39
        goto L6
    L25:
        if (r7.length() == 0) goto L38;
        if (hasSession(r17) == true) goto L29;
    L33:
        boolean r3 = false;
    L34:
        save(r17, new JSONObject().put("auth", r4).put("refreshDevice", r2.optString("refreshDeviceId", "002a3fd9f9")).put("channels", r7).put("useLoginSession", r3));     // Catch: Throwable -> L39
        generation++;     // Catch: Throwable -> L39
        retryAt = 0;     // Catch: Throwable -> L39
        r17.getSharedPreferences(PREF, 0).edit().clear().apply();     // Catch: Throwable -> L39
        schedule(r17, 0);     // Catch: Throwable -> L39
        arm(r17);     // Catch: Throwable -> L39
        monitor-exit(DaznTokens.class);
        return;
    L29:
        if (state(r17).optBoolean("useLoginSession", false) == false) goto L33;
        if (r4.equals(state(r17).optString("auth")) == false) goto L33;
        r3 = true;
        goto L34
    L38:
        throw new IOException("No hay canales con AssetId");     // Catch: Throwable -> L39
    L39:
        th = move-exception;
        throw th;
    }

    static boolean configured(Context r3) {
        if (hasSession(r3) == false) goto L13;
        if (state(r3).getJSONArray("channels").length() <= 0) goto L14;
        return true;
    L14:
        return false;
    L13:
        return false;
    L10:
        return false;
    }

    static synchronized void disable(Context r3) {
        monitor-enter(DaznTokens.class);
        generation++;     // Catch: Throwable -> L7
        DaznBrowser.reset();     // Catch: Throwable -> L7
        cache = null;     // Catch: Throwable -> L7
        new AtomicFile(file(r3)).delete();     // Catch: Throwable -> L7
        r3.getSharedPreferences(PREF, 0).edit().clear().apply();     // Catch: Throwable -> L7
        ((JobScheduler) r3.getSystemService("jobscheduler")).cancel(JOB);     // Catch: Throwable -> L7
        main.removeCallbacks(due);     // Catch: Throwable -> L7
        monitor-exit(DaznTokens.class);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    static File file(Context r3) {
        return new File(r3.getNoBackupFilesDir(), "dazn-session.enc");
    }

    static void foreground(Context r3) {
        app = r3.getApplicationContext();
        if (foreground == false) goto L5;
        return;
    L5:
        foreground = true;
        final Context r0 = app;
        worker.execute(new DaznTokens$$ExternalSyntheticLambda3(r0));
    }

    static boolean freshLogin(JSONObject r7) {
        if (r7.optBoolean("useLoginSession", false) == false) goto L14;
        if ((DaznTokenClient.payload(r7.getString("auth")).optLong("exp", 0) * 1000) <= (System.currentTimeMillis() + 30000)) goto L15;
        return true;
    L15:
        return false;
    L14:
        return false;
    L11:
        return false;
    }

    static boolean hasSession(Context r3) {
        if (file(r3).isFile() == false) goto L13;
        if (state(r3).optString("auth").isEmpty() == true) goto L14;
        return true;
    L14:
        return false;
    L13:
        return false;
    L10:
        return false;
    }

    static synchronized Map<String, String> headers(Context r10, Entry r11, String r12, Map<String, String> r13) {
        monitor-enter(DaznTokens.class);
        JSONObject r1 = match(r10, r11);     // Catch: Throwable -> L36
        if (r1 != null) goto L39;
        monitor-exit(DaznTokens.class);
        return r13;
    L39:
        JSONObject r2 = r1.getJSONObject("current");     // Catch: Exception -> L32 Throwable -> L36
        String r3 = r2.optString("token");     // Catch: Exception -> L32 Throwable -> L36
        String r4 = new URI(r12).getHost();     // Catch: Exception -> L32 Throwable -> L36
        String r5 = new URI(r2.optString("url", Entry.details(r11).url)).getHost();     // Catch: Exception -> L32 Throwable -> L36
        if (r4 != null) goto L11;
    L29:
        Map<String, String> r6 = withoutToken(r13);     // Catch: Exception -> L32 Throwable -> L36
        monitor-exit(DaznTokens.class);
        return r6;
    L11:
        if (r4.equalsIgnoreCase(r5) == false) goto L29;
        if (r3.isEmpty() == false) goto L21;
        Iterator<Map.Entry<String, String>> r7 = r11.headers.entrySet().iterator();     // Catch: Exception -> L32 Throwable -> L36
    L17:
        if (r7.hasNext() == false) goto L21;
        Map.Entry<String, String> r8 = r7.next();     // Catch: Exception -> L32 Throwable -> L36
        if (r8.getKey().equalsIgnoreCase("dazn-token") == false) goto L17;
        r3 = r8.getValue();     // Catch: Exception -> L32 Throwable -> L36
    L21:
        Map<String, String> r9 = withoutToken(r13);     // Catch: Exception -> L32 Throwable -> L36
        if (r3.isEmpty() == true) goto L24;
        r9.put("dazn-token", r3);     // Catch: Exception -> L32 Throwable -> L36
    L24:
        String r14 = userAgent(r10, r11);     // Catch: Exception -> L32 Throwable -> L36
        if (r14.isEmpty() == true) goto L27;
        r9.put(HttpHeaders.USER_AGENT, r14);     // Catch: Exception -> L32 Throwable -> L36
    L27:
        monitor-exit(DaznTokens.class);
        return r9;
    L35:
        return withoutToken(r13);
    L36:
        th = move-exception;
        throw th;
    }

    static JSONObject importedCurrent(JSONObject r13) throws Exception {
        String r0 = r13.getString("fileUrl");
        String r1 = r13.optString("cdn_token_value");
        if (r1.indexOf(37) < 0) goto L6;
        r1 = URLDecoder.decode(r1, "UTF-8");
    L6:
        if (r1.isEmpty() == false) goto L17;
        String r2 = new URI(r0).getRawQuery();
        if (r2 == null) goto L17;
        String[] r5 = r2.split("&");
        int r6 = r5.length;
        int r8 = 0;
    L10:
        if (r8 >= r6) goto L17;
        String[] r10 = r5[r8].split("=", 2);
        if (r10.length != 2) goto L16;
        if (URLDecoder.decode(r10[0], "UTF-8").equalsIgnoreCase("dazn-token") == false) goto L16;
        r1 = URLDecoder.decode(r10[1], "UTF-8");
    L16:
        r8 = r8 + 1;
    L17:
        long r3 = DaznTokenClient.expiry(r1);
        long r9 = r13.optLong("cdn_expires", 0);
        if (r9 <= 0) goto L28;
        if (r9 >= 100000000000L) goto L23;
        r9 = r9 * 1000;
    L23:
        if (r3 != 0) goto L25;
        long r7 = r9;
    L26:
        r3 = r7;
        goto L28
    L25:
        r7 = Math.min(r3, r9);
    L28:
        if (r1.isEmpty() == true) goto L35;
        if (r3 <= (System.currentTimeMillis() + 30000)) goto L35;
        return new JSONObject().put("url", r0).put("token", r1).put("renewed", System.currentTimeMillis()).put("expires", r3).put("imported", true);
    L35:
        return new JSONObject();
    }

    static SecretKey key() throws Exception {
        KeyStore r1 = KeyStore.getInstance("AndroidKeyStore");
        r1.load(null);
        if (r1.containsAlias("chdtv_dazn_session") == true) goto L6;
        KeyGenerator r0 = KeyGenerator.getInstance("AES", "AndroidKeyStore");
        r0.init(new KeyGenParameterSpec.Builder("chdtv_dazn_session", 3).setBlockModes(new String[]{"GCM"}).setEncryptionPaddings(new String[]{"NoPadding"}).build());
        r0.generateKey();
    L6:
        return (SecretKey) r1.getKey("chdtv_dazn_session", null);
    }

    static /* synthetic */ void lambda$foreground$7(Context r1) {
        if (foreground == false) goto L6;
        arm(r1);
        return;
    }

    static /* synthetic */ void lambda$foreground$8(final Context r2) {
        if (foreground == true) goto L5;
        return;
    L5:
        renew(r2, false, null);
        main.post(new DaznTokens$$ExternalSyntheticLambda1(r2));
    }

    static /* synthetic */ void lambda$renew$2(CdnTokens.Pending r1, Boolean r2, Throwable r3) {
        r1.complete(Boolean.TRUE.equals(r2));
    }

    static /* synthetic */ void lambda$renew$3(Context r2, Entry r3, final CdnTokens.Pending r4, Boolean r5, Throwable r6) {
        renew(r2, true, r3).whenComplete(new DaznTokens$$ExternalSyntheticLambda0(r4));
    }

    static /* synthetic */ void lambda$renew$4(Context r0) {
        arm(r0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v31, types: [org.json.JSONObject] */
    /* JADX WARN: Type inference failed for: r0v9, types: [android.content.SharedPreferences$Editor] */
    /* JADX WARN: Type inference failed for: r10v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r10v1, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r10v3 */
    /* JADX WARN: Type inference failed for: r10v4 */
    /* JADX WARN: Type inference failed for: r10v7 */
    /* JADX WARN: Type inference failed for: r10v8 */
    /* JADX WARN: Type inference failed for: r10v9 */
    /* JADX WARN: Type inference failed for: r12v1 */
    /* JADX WARN: Type inference failed for: r12v12, types: [java.lang.Object, org.json.JSONObject] */
    /* JADX WARN: Type inference failed for: r12v13 */
    /* JADX WARN: Type inference failed for: r12v14, types: [long] */
    /* JADX WARN: Type inference failed for: r12v15 */
    /* JADX WARN: Type inference failed for: r12v16 */
    /* JADX WARN: Type inference failed for: r12v17 */
    /* JADX WARN: Type inference failed for: r12v18 */
    /* JADX WARN: Type inference failed for: r12v19 */
    /* JADX WARN: Type inference failed for: r12v20 */
    /* JADX WARN: Type inference failed for: r12v21 */
    /* JADX WARN: Type inference failed for: r12v22 */
    /* JADX WARN: Type inference failed for: r12v9 */
    /* JADX WARN: Type inference failed for: r5v12 */
    /* JADX WARN: Type inference failed for: r5v6 */
    static /* synthetic */ void lambda$renew$5(final Context r16, int r17, String r18, boolean r19, CdnTokens.Pending r20) {
        monitor-enter(DaznTokens.class);     // Catch: Exception -> L77
        JSONObject r0 = new JSONObject(state(r16).toString());     // Catch: Throwable -> L74
        monitor-exit(DaznTokens.class);     // Catch: Throwable -> L74
        DaznTokenClient r1 = new DaznTokenClient(new DaznPacedTransport(new DaznPlaybackTransport(r16)));     // Catch: Exception -> L77
        if (freshLogin(r0) == false) goto L10;
        String r2 = r0.getString("auth");     // Catch: Exception -> L77
    L11:
        String r8 = r2;
        r0.remove("useLoginSession");     // Catch: Exception -> L77
        r0.put("auth", r8).put("authRenewed", System.currentTimeMillis());     // Catch: Exception -> L77
        monitor-enter(DaznTokens.class);     // Catch: Exception -> L77
    L71:
        th = move-exception;
        throw th;     // Catch: Exception -> L77
    L14:
        if (r17 != generation) goto L16;
        save(r16, r0);     // Catch: Throwable -> L71
    L16:
        monitor-exit(DaznTokens.class);     // Catch: Throwable -> L71
        JSONArray r3 = r0.getJSONArray("channels");     // Catch: Exception -> L77
        ?? r10 = "";
        boolean r5 = true;
        int r4 = 0;
    L109:
        if (r4 >= r3.length()) goto L80;
        monitor-enter(DaznTokens.class);     // Catch: Exception -> L69
    L66:
        th = move-exception;
        throw th;     // Catch: Exception -> L69
    L24:
        if (r17 != generation) goto L25;
        monitor-exit(DaznTokens.class);     // Catch: Throwable -> L66
        ?? r6 = r3.getJSONObject(r4);     // Catch: Exception -> L69
        if (r18 == null) goto L34;
        ?? r12 = r12;
        if (r18.equals(r6.getString("asset")) == true) goto L34;
    L33:
        r10 = r10;
        ?? r13 = r12;
    L115:
        r4 = r4 + 1;     // Catch: Throwable -> L61
    L61:
        th = move-exception;
        throw th;     // Catch: Exception -> L64
    L34:
        if (r19 == true) goto L107;
        r13 = System.currentTimeMillis();     // Catch: Exception -> L69
        r12 = r13;
        if (r13 < DaznTokenClient.next(r6.getJSONObject("current"))) goto L33;
    L107:
        r13 = r1.playback(r8, r6.getString("asset"));     // Catch: Exception -> L41
        r6.put("current", r13);     // Catch: Exception -> L41
        r6.remove("error");     // Catch: Exception -> L41
        r13 = r10;
    L103:
        monitor-enter(DaznTokens.class);     // Catch: Exception -> L64
        if (r17 != generation) goto L58;
        save(r16, r0);     // Catch: Throwable -> L61
    L58:
        monitor-exit(DaznTokens.class);     // Catch: Throwable -> L61
        r10 = r13;
        r13 = r13;
        goto L115
    L41:
        e = move-exception;
        r5 = false;
        StringBuilder r14 = new StringBuilder().append("No se pudo renovar ").append(r6.getString("title")).append(" · ");     // Catch: Exception -> L69
        if ((e instanceof IOException) == false) goto L46;
        String r15 = e.getMessage();     // Catch: Exception -> L69
    L47:
        String r110 = r14.append(r15).append(" · se conserva la fuente anterior").toString();     // Catch: Exception -> L69
        if ((e instanceof IOException) == false) goto L51;
        String r111 = e.getMessage();     // Catch: Exception -> L64
    L52:
        r6.put("error", r111);     // Catch: Exception -> L64
        r13 = r110;
        goto L103
    L51:
        r111 = "respuesta no compatible";
        goto L52
    L46:
        r15 = "respuesta no compatible";
        goto L47
    L25:
        r5 = false;
        monitor-exit(DaznTokens.class);     // Catch: Throwable -> L66
    L80:
        monitor-enter(DaznTokens.class);
    L95:
        th = th;
    L101:
        throw th;
    L99:
        th = th;
        goto L101
    L82:
        if (r17 != generation) goto L91;
        if (r5 == false) goto L85;
        long r7 = 0;
    L86:
        retryAt = r7;     // Catch: Throwable -> L95
        r16.getSharedPreferences(PREF, 0).edit().putString("error", r10).apply();     // Catch: Throwable -> L95
        if (r5 == false) goto L89;
        long r9 = next(r16);     // Catch: Throwable -> L95
    L90:
        schedule(r16, Math.max(1000, r9 - System.currentTimeMillis()));     // Catch: Throwable -> L95
        goto L91
    L89:
        r9 = retryAt;     // Catch: Throwable -> L95
        goto L90
    L85:
        r7 = System.currentTimeMillis() + 300000;     // Catch: Throwable -> L95
    L91:
        pending = null;     // Catch: Throwable -> L95
        monitor-exit(DaznTokens.class);     // Catch: Throwable -> L95
        r20.complete(r5);
        main.post(new DaznTokens$$ExternalSyntheticLambda4(r16));
        return;
        goto L78
    L10:
        r2 = r1.refresh(r0.getString("auth"), r0.optString("refreshDevice", "002a3fd9f9"));     // Catch: Exception -> L77
        goto L11
    L74:
        th = move-exception;
        throw th;     // Catch: Exception -> L77
    L78:
        r10 = "No se pudo renovar DAZN · importa una sesión vigente o comprueba la conexión";
        r5 = false;
        goto L80
    }

    static /* synthetic */ void lambda$static$0(Runnable r1) {
        Process.setThreadPriority(10);
        r1.run();
    }

    static /* synthetic */ Thread lambda$static$1(final Runnable r3) {
        return new Thread(new DaznTokens$$ExternalSyntheticLambda2(r3), "CHDTVDazn");
    }

    static /* synthetic */ void lambda$static$6() {
        if (app != null) goto L5;
        return;
    L5:
        if (foreground == false) goto L9;
        renew(app, false, null);
        arm(app);
        return;
    }

    static boolean mappedHost(String r1, String r2, String r3) {
        if (r1.equalsIgnoreCase(r2) == false) goto L5;
    L10:
        return true;
    L5:
        if (r1.equalsIgnoreCase(r3) == true) goto L10;
        if (mediaHost(r1) == true) goto L10;
        return false;
    }

    static JSONObject match(Context r12, Entry r13) {
        if ("Canales".equals(r13.kind) == true) goto L5;
    L23:
        return null;
    L5:
        if (configured(r12) == false) goto L23;
        String r1 = new URI(Entry.details(r13).url).getHost();     // Catch: Exception -> L22
        if (r1 != null) goto L10;
        return null;
    L10:
        JSONArray r3 = state(r12).getJSONArray("channels");     // Catch: Exception -> L22
        JSONObject r4 = null;
        int r5 = 0;
    L12:
        if (r5 >= r3.length()) goto L21;
        JSONObject r6 = r3.getJSONObject(r5);     // Catch: Exception -> L22
        if (mappedHost(r1, new URI(r6.getString("original")).getHost(), new URI(r6.getJSONObject("current").optString("url", r6.getString("original"))).getHost()) == false) goto L20;
        if (ChannelFallback.name(r13.title).equals(ChannelFallback.name(r6.getString("title"))) == false) goto L20;
        if (r4 != null) goto L18;
        r4 = r6;
        goto L20
    L18:
        return null;
    L20:
        r5 = r5 + 1;
        goto L12
    L21:
        return r4;
    }

    static boolean mediaHost(String r4) {
        if (r4 != null) goto L5;
        return false;
    L5:
        String r1 = r4.toLowerCase(Locale.ROOT);
        if (r1.endsWith(".dazn.com") == false) goto L8;
    L11:
        return true;
    L8:
        if (r1.endsWith(".akamaized.net") == true) goto L10;
        return false;
    L10:
        if (r1.substring(0, r1.length() - ".akamaized.net".length()).matches("[a-z0-9-]*livedazn") == true) goto L11;
        return false;
    }

    static synchronized long next(Context r9) {
        monitor-enter(DaznTokens.class);
        long r1 = Long.MAX_VALUE;
        long r3 = 0;
        JSONArray r5 = state(r9).getJSONArray("channels");     // Catch: Throwable -> L16 Exception -> L19
        int r6 = 0;
    L7:
        if (r6 >= r5.length()) goto L11;
        r1 = Math.min(r1, DaznTokenClient.next(r5.getJSONObject(r6).getJSONObject("current")));     // Catch: Throwable -> L16 Exception -> L19
        r6 = r6 + 1;
        goto L7
        goto L21
    L11:
        if (r1 == Long.MAX_VALUE) goto L14;
        r3 = r1;
    L14:
        monitor-exit(DaznTokens.class);
        return r3;
    L16:
        th = move-exception;
        throw th;
    L21:
        return 0;
    }

    static synchronized CdnTokens.Pending renew(Context r10, final boolean r11, final Entry r12) {
        monitor-enter(DaznTokens.class);
        final Context r0 = r10.getApplicationContext();     // Catch: Throwable -> L38
        if (configured(r0) == true) goto L10;
        CdnTokens.Pending r1 = CdnTokens.Pending.completed(false);     // Catch: Throwable -> L38
        monitor-exit(DaznTokens.class);
        return r1;
    L10:
        if (pending == null) goto L18;
        if (r11 == true) goto L15;
        CdnTokens.Pending r2 = pending;     // Catch: Throwable -> L38
        monitor-exit(DaznTokens.class);
        return r2;
    L15:
        final CdnTokens.Pending r3 = new CdnTokens.Pending();     // Catch: Throwable -> L38
        pending.whenComplete(new DaznTokens$$ExternalSyntheticLambda7(r0, r12, r3));     // Catch: Throwable -> L38
        monitor-exit(DaznTokens.class);
        return r3;
    L18:
        long r4 = System.currentTimeMillis();     // Catch: Throwable -> L38
        if (r11 == false) goto L21;
    L27:
        final CdnTokens.Pending r7 = new CdnTokens.Pending();     // Catch: Throwable -> L38
        pending = r7;     // Catch: Throwable -> L38
        final int r5 = generation;     // Catch: Throwable -> L38
        if (r12 != null) goto L32;
        String r6 = null;
    L30:
        final String r8 = r6;
        worker.execute(new DaznTokens$$ExternalSyntheticLambda8(r0, r5, r8, r11, r7));     // Catch: Throwable -> L38
        monitor-exit(DaznTokens.class);
        return r7;
    L32:
        if (match(r0, r12) != null) goto L34;
        r6 = "";
        goto L30
    L34:
        r6 = match(r0, r12).optString("asset");     // Catch: Throwable -> L38
        goto L30
    L21:
        if (r4 >= next(r0)) goto L23;
    L24:
        CdnTokens.Pending r9 = CdnTokens.Pending.completed(true);     // Catch: Throwable -> L38
        monitor-exit(DaznTokens.class);
        return r9;
    L23:
        if (r4 >= retryAt) goto L27;
    L38:
        th = move-exception;
        throw th;
    }

    static DataSpec resolve(Context r17, Entry r18, DataSpec r19) {
        DataSpec r0 = r19;
        Map<String, String> r1 = headers(r17, r18, r0.uri.toString(), r0.httpRequestHeaders);
        String r5 = r1.get("dazn-token");
        Uri r6 = r0.uri;
        String r7 = r6.getEncodedQuery();
        if (r5 == null) goto L17;
        if (r7 == null) goto L17;
        StringBuilder r8 = new StringBuilder();
        boolean r9 = false;
        String[] r10 = r7.split("&");
        int r11 = r10.length;
        int r13 = 0;
    L6:
        if (r13 >= r11) goto L14;
        String r14 = r10[r13];
        String r15 = r14.split("=", 2)[0];
        if (Uri.decode(r15).equalsIgnoreCase("dazn-token") == false) goto L11;
        r14 = r15 + "=" + Uri.encode(r5);
        r9 = true;
    L11:
        if (r8.length() <= 0) goto L13;
        r8.append(Typography.amp);
    L13:
        r8.append(r14);
        r13 = r13 + 1;
        goto L6
    L14:
        if (r9 == false) goto L17;
        r0 = r0.withUri(r6.buildUpon().encodedQuery(r8.toString()).build());
    L17:
        return r0.withRequestHeaders(r1);
    }

    static synchronized void save(Context r7, JSONObject r8) throws Exception {
        monitor-enter(DaznTokens.class);
        Cipher r1 = Cipher.getInstance("AES/GCM/NoPadding");     // Catch: Throwable -> L11
        r1.init(1, key());     // Catch: Throwable -> L11
        byte[] r2 = r1.doFinal(r8.toString().getBytes(StandardCharsets.UTF_8));     // Catch: Throwable -> L11
        AtomicFile r3 = new AtomicFile(file(r7));     // Catch: Throwable -> L11
        FileOutputStream r4 = r3.startWrite();     // Catch: Throwable -> L11
        r4.write(r1.getIV());     // Catch: Exception -> L8 Throwable -> L11
        r4.write(r2);     // Catch: Exception -> L8 Throwable -> L11
        r3.finishWrite(r4);     // Catch: Exception -> L8 Throwable -> L11
        cache = new JSONObject(r8.toString());     // Catch: Exception -> L8 Throwable -> L11
        monitor-exit(DaznTokens.class);
        return;
    L8:
        e = move-exception;
        r3.failWrite(r4);     // Catch: Throwable -> L11
        throw e;     // Catch: Throwable -> L11
    L11:
        th = move-exception;
        throw th;
    }

    static synchronized void saveLogin(Context r9, String r10) throws Exception {
        monitor-enter(DaznTokens.class);
        JSONObject r2 = new JSONObject(DaznLogin.session(r10));     // Catch: Throwable -> L15
        if (hasSession(r9) == false) goto L7;
        JSONObject r3 = new JSONObject(state(r9).toString());     // Catch: Throwable -> L15
    L8:
        JSONArray r4 = r3.getJSONArray("channels");     // Catch: Throwable -> L15
        int r5 = 0;
    L10:
        if (r5 >= r4.length()) goto L12;
        r4.getJSONObject(r5).put("current", new JSONObject()).remove("error");     // Catch: Throwable -> L15
        r5 = r5 + 1;     // Catch: Throwable -> L15
        goto L10
    L12:
        r3.put("auth", r2.getJSONObject("AuthToken").getString("Token")).put("authRenewed", System.currentTimeMillis()).put("refreshDevice", "002a3fd9f9").put("localLogin", true).put("useLoginSession", true);     // Catch: Throwable -> L15
        save(r9, r3);     // Catch: Throwable -> L15
        generation++;     // Catch: Throwable -> L15
        retryAt = 0;     // Catch: Throwable -> L15
        r9.getSharedPreferences(PREF, 0).edit().clear().apply();     // Catch: Throwable -> L15
        schedule(r9, 0);     // Catch: Throwable -> L15
        arm(r9);     // Catch: Throwable -> L15
        monitor-exit(DaznTokens.class);
        return;
    L7:
        r3 = new JSONObject().put("channels", new JSONArray());     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        throw th;
    }

    static void savePrepared(Context r2, String r3, String r4) throws Exception {
        if (r4 == null) goto L7;
        if (r3 != null) goto L5;
        String r0 = storedSession(r2);
    L6:
        configure(r2, r0, r4);
        return;
    L5:
        r0 = r3;
        goto L6
    L7:
        if (r3 == null) goto L10;
        saveLogin(r2, r3);
        return;
    L10:
        if (configured(r2) == false) goto L13;
        return;
    L13:
        throw new IOException("Importa el mapa de canales");
    }

    static void schedule(Context r5, long r6) {
        if (configured(r5) == false) goto L6;
        ((JobScheduler) r5.getSystemService("jobscheduler")).schedule(new JobInfo.Builder(JOB, new ComponentName(r5, DaznTokenJob.class)).setRequiredNetworkType(1).setMinimumLatency(Math.max(1000, r6)).setPersisted(true).build());
        return;
    }

    static synchronized JSONObject state(Context r8) throws Exception {
        monitor-enter(DaznTokens.class);
    L18:
        th = move-exception;
        throw th;
    L5:
        if (cache == null) goto L9;
        JSONObject r1 = cache;     // Catch: Throwable -> L18
        monitor-exit(DaznTokens.class);
        return r1;
    L9:
        byte[] r2 = new AtomicFile(file(r8)).readFully();     // Catch: Throwable -> L18
        if (r2.length < 28) goto L17;
        if (r2.length > 8388608) goto L17;
        Cipher r3 = Cipher.getInstance("AES/GCM/NoPadding");     // Catch: Throwable -> L18
        r3.init(2, key(), new GCMParameterSpec(128, Arrays.copyOfRange(r2, 0, 12)));     // Catch: Throwable -> L18
        cache = new JSONObject(new String(r3.doFinal(Arrays.copyOfRange(r2, 12, r2.length)), StandardCharsets.UTF_8));     // Catch: Throwable -> L18
        JSONObject r4 = cache;     // Catch: Throwable -> L18
        monitor-exit(DaznTokens.class);
        return r4;
    L17:
        throw new IOException("Configuración DAZN inválida");     // Catch: Throwable -> L18
    }

    static String status(Context r22) {
        if (configured(r22) == false) goto L5;
        StringBuilder r0 = new StringBuilder("Intervalo · 23 h 55 min\n");     // Catch: Exception -> L37
        JSONObject r1 = state(r22);     // Catch: Exception -> L37
        long r3 = 0;
        if (r1.optLong("authRenewed", 0) <= 0) goto L12;
        String r2 = "✓ Sesión renovada y guardada\n";
    L13:
        r0.append(r2);     // Catch: Exception -> L37
        if (pending == null) goto L16;
        r0.append("Actualizando tokens de canales…\n");     // Catch: Exception -> L37
    L16:
        JSONArray r4 = r1.getJSONArray("channels");     // Catch: Exception -> L37
        long r5 = System.currentTimeMillis();     // Catch: Exception -> L37
        int r7 = 0;
    L17:
        String r9 = "";
        if (r7 >= r4.length()) goto L43;
        JSONObject r8 = r4.getJSONObject(r7);     // Catch: Exception -> L37
        long r12 = Math.max(r3, DaznTokenClient.next(r8.getJSONObject("current")) - r5);     // Catch: Exception -> L37
        StringBuilder r14 = r0.append(r8.getString("title")).append(" · ");     // Catch: Exception -> L37
        if (r8.has("error") == false) goto L27;
        StringBuilder r10 = new StringBuilder().append("No actualizado · ").append(r8.optString("error"));     // Catch: Exception -> L37
        if (r12 <= r3) goto L25;
        r9 = " · token anterior disponible";
    L25:
        String r11 = r10.append(r9).toString();     // Catch: Exception -> L37
    L30:
        r14.append(r11).append('\n');     // Catch: Exception -> L37
        r7 = r7 + 1;     // Catch: Exception -> L37
        r3 = 0;
        goto L17
    L27:
        if (r12 != r3) goto L29;
        r11 = "pendiente";
        goto L30
    L29:
        r11 = String.format(Locale.ROOT, "%02d:%02d:%02d", new Object[]{Long.valueOf(r12 / 3600000), Long.valueOf((r12 / 60000) % 60), Long.valueOf((r12 / 1000) % 60)});     // Catch: Exception -> L37
        goto L30
    L43:
        return r0.append(r22.getSharedPreferences(PREF, 0).getString("error", "")).toString();
    L50:
        return "No se puede leer la sesión DAZN · impórtala de nuevo";
    L12:
        r2 = "Sesión · pendiente de renovación\n";
    L39:
        return "No se puede leer la sesión DAZN · impórtala de nuevo";
    L5:
        if (hasSession(r22) == false) goto L7;
        return "✓ Sesión guardada en este Android · importa el mapa de canales";
    L7:
        return "Renovación DAZN desactivada";
    }

    static synchronized String storedSession(Context r7) throws Exception {
        monitor-enter(DaznTokens.class);
        JSONObject r1 = state(r7);     // Catch: Throwable -> L7
        String r2 = new JSONObject().put("AuthToken", new JSONObject().put("Token", r1.getString("auth"))).put("refreshDeviceId", r1.optString("refreshDevice", "002a3fd9f9")).toString();     // Catch: Throwable -> L7
        monitor-exit(DaznTokens.class);
        return r2;
    L7:
        th = move-exception;
        throw th;
    }

    static boolean tokenAgentMatches(String r12, String r13) {
        if (r12.indexOf(37) < 0) goto L6;
        String r0 = URLDecoder.decode(r12, "UTF-8");     // Catch: Exception -> L17
    L7:
        String r2 = DaznTokenClient.payload(r0).optString("ua");     // Catch: Exception -> L17
        byte[] r3 = MessageDigest.getInstance("SHA-1").digest(r13.getBytes(StandardCharsets.UTF_8));     // Catch: Exception -> L17
        StringBuilder r4 = new StringBuilder();     // Catch: Exception -> L17
        int r5 = r3.length;     // Catch: Exception -> L17
        int r6 = 0;
    L9:
        if (r6 >= r5) goto L12;
        r4.append(String.format(Locale.ROOT, "%02x", new Object[]{Integer.valueOf(r3[r6] & 255)}));     // Catch: Exception -> L17
        r6 = r6 + 1;     // Catch: Exception -> L17
        goto L9
    L12:
        if (r2.isEmpty() == true) goto L22;
        if (r2.equalsIgnoreCase(r4.toString()) == true) goto L16;
        return false;
    L16:
        return true;
    L22:
        return false;
    L6:
        r0 = r12;
    L18:
        return false;
    }

    static synchronized String url(Context r5, Entry r6) {
        monitor-enter(DaznTokens.class);
        JSONObject r1 = match(r5, r6);     // Catch: Throwable -> L10
        if (r1 != null) goto L7;
        String r2 = Entry.details(r6).url;     // Catch: Throwable -> L10
    L8:
        monitor-exit(DaznTokens.class);
        return r2;
    L7:
        r2 = r1.optJSONObject("current").optString("url", Entry.details(r6).url);     // Catch: Throwable -> L10
    L10:
        th = move-exception;
        throw th;
    }

    static String userAgent(Context r8, Entry r9) {
        JSONObject r0 = match(r8, r9);
        if (r0 == null) goto L14;
        String r3 = r0.optJSONObject("current").optString("userAgent");
        if (r3.isEmpty() == true) goto L12;
        if (r3.indexOf(13) >= 0) goto L12;
        if (r3.indexOf(10) >= 0) goto L12;
        return r3;
    L12:
        if (tokenAgentMatches(r0.optJSONObject("current").optString("token"), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36") == false) goto L14;
        return "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36";
    L14:
        String r2 = "";
        String r4 = "";
        Iterator<Map.Entry<String, String>> r5 = r9.headers.entrySet().iterator();
    L16:
        if (r5.hasNext() == false) goto L24;
        Map.Entry<String, String> r6 = r5.next();
        if (r6.getKey().equalsIgnoreCase("dazn-token") == false) goto L21;
        r2 = r6.getValue();
    L21:
        if (r6.getKey().equalsIgnoreCase(HttpHeaders.USER_AGENT) == false) goto L16;
        r4 = r6.getValue();
        goto L16
    L24:
        if (r4.isEmpty() == true) goto L27;
        return r4;
    L27:
        if (tokenAgentMatches(r2, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36") == true) goto L36;
        return "";
    L36:
        return "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36";
    }

    static Map<String, String> withoutToken(Map<String, String> r4) {
        LinkedHashMap r0 = new LinkedHashMap(r4);
        Iterator r1 = r0.keySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L8;
        if (((String) r1.next()).equalsIgnoreCase("dazn-token") == false) goto L4;
        r1.remove();
        goto L4
    L8:
        return r0;
    }
}
