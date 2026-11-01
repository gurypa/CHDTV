package es.antonio.m3u;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.security.keystore.KeyGenParameterSpec;
import android.util.AtomicFile;
import android.util.Base64;
import androidx.media3.datasource.HttpDataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class CdnTokens {
    static final long INTERVAL = 42900000;
    static final int JOB = 48115;
    static final String PREF = "cdn_token_state";
    private static Context app;
    static final Runnable due = null;
    private static final Map<Catalog.Source, Boolean> eligibleCache = null;
    static final ExecutorService executor = null;
    private static volatile boolean foreground;
    private static int generation;
    private static Pending inFlight;
    static final Handler main = null;

    interface Callback {
        void done(Boolean r1, Throwable r2);
    }

    static final class Pending {
        private final List<Callback> callbacks;
        private boolean finished;
        private boolean value;

        Pending() {
            this.callbacks = new ArrayList();
        }

        static Pending completed(boolean r1) {
            Pending r0 = new Pending();
            r0.complete(r1);
            return r0;
        }

        void complete(boolean r6) {
            monitor-enter(this);
        L14:
            th = move-exception;
            throw th;
        L4:
            if (this.finished == false) goto L7;
            monitor-exit(this);     // Catch: Throwable -> L14
            return;
        L7:
            this.finished = true;     // Catch: Throwable -> L14
            this.value = r6;     // Catch: Throwable -> L14
            ArrayList r0 = new ArrayList(this.callbacks);     // Catch: Throwable -> L14
            this.callbacks.clear();     // Catch: Throwable -> L14
            monitor-exit(this);     // Catch: Throwable -> L14
            Iterator r1 = r0.iterator();
        L11:
            if (r1.hasNext() == false) goto L13;
            ((Callback) r1.next()).done(Boolean.valueOf(r6), null);
            goto L11
        }

        synchronized void whenComplete(Callback r3) {
            monitor-enter(this);
        L9:
            th = move-exception;
            throw th;
        L4:
            if (this.finished == false) goto L6;
            r3.done(Boolean.valueOf(this.value), null);     // Catch: Throwable -> L9
        L7:
            monitor-exit(this);
            return;
        L6:
            this.callbacks.add(r3);     // Catch: Throwable -> L9
            goto L7
        }
    }

    static {
        executor = Executors.newSingleThreadExecutor(new CdnTokens$$ExternalSyntheticLambda4());
        main = new Handler(Looper.getMainLooper());
        due = new CdnTokens$$ExternalSyntheticLambda5();
        eligibleCache = Collections.synchronizedMap(new WeakHashMap());
    }

    CdnTokens() {
    }

    static JSONObject account(Context r7) throws Exception {
        byte[] r0 = new AtomicFile(credentials(r7)).readFully();
        if (r0.length < 28) goto L9;
        if (r0.length > 65536) goto L9;
        Cipher r1 = Cipher.getInstance("AES/GCM/NoPadding");
        r1.init(2, key(), new GCMParameterSpec(128, Arrays.copyOfRange(r0, 0, 12)));
        return new JSONObject(new String(r1.doFinal(Arrays.copyOfRange(r0, 12, r0.length)), StandardCharsets.UTF_8));
    L9:
        throw new IOException("Cuenta guardada no válida");
    }

    static boolean allowedHost(String r1) {
        return MovistarStreams.allowedHost(r1);
    }

    static void arm(Context r6) {
        main.removeCallbacks(due);
        if (foreground == true) goto L5;
        return;
    L5:
        if (configured(r6) == false) goto L14;
        long r0 = Math.max(0, next(r6) - System.currentTimeMillis());
        if (prefs(r6).getString("error", "").isEmpty() == false) goto L9;
    L11:
        main.postDelayed(due, Math.max(1000, r0));
        return;
    L9:
        if (r0 >= 300000) goto L11;
        r0 = 300000;
        goto L11
    }

    static void background() {
        foreground = false;
        main.removeCallbacks(due);
    }

    static synchronized void configure(Context r11, String r12, String r13, Set<String> r14) throws Exception {
        monitor-enter(CdnTokens.class);
        Cipher r1 = Cipher.getInstance("AES/GCM/NoPadding");     // Catch: Throwable -> L23
        r1.init(1, key());     // Catch: Throwable -> L23
        byte[] r2 = r1.doFinal(new JSONObject().put("user", r12).put("password", r13).toString().getBytes(StandardCharsets.UTF_8));     // Catch: Throwable -> L23
        AtomicFile r4 = new AtomicFile(credentials(r11));     // Catch: Throwable -> L23
        FileOutputStream r5 = r4.startWrite();     // Catch: Throwable -> L23
        r5.write(r1.getIV());     // Catch: Exception -> L20 Throwable -> L23
        r5.write(r2);     // Catch: Exception -> L20 Throwable -> L23
        r4.finishWrite(r5);     // Catch: Exception -> L20 Throwable -> L23
        boolean r6 = prefs(r11).getBoolean("auto_new_sources", true);     // Catch: Throwable -> L23
        HashSet r7 = new HashSet();     // Catch: Throwable -> L23
        Iterator<Catalog.Source> r8 = new AppStorage(r11).catalog().sources.iterator();     // Catch: Exception -> L16 Throwable -> L23
    L9:
        if (r8.hasNext() == false) goto L17;
        Catalog.Source r9 = r8.next();     // Catch: Exception -> L16 Throwable -> L23
        if (eligible(r9) == false) goto L9;
        if (r14.contains(r9.f177id) == true) goto L9;
        r7.add(r9.f177id);     // Catch: Exception -> L16 Throwable -> L23
    L17:
        generation++;     // Catch: Throwable -> L23
        prefs(r11).edit().clear().putStringSet("sources", new HashSet(r14)).putStringSet("excluded_sources", r7).putBoolean("auto_new_sources", r6).commit();     // Catch: Throwable -> L23
        schedule(r11, 0);     // Catch: Throwable -> L23
        arm(r11);     // Catch: Throwable -> L23
        monitor-exit(CdnTokens.class);
        return;
    L20:
        e = move-exception;
        r4.failWrite(r5);     // Catch: Throwable -> L23
        throw e;     // Catch: Throwable -> L23
    L23:
        th = move-exception;
        throw th;
    }

    static boolean configured(Context r3) {
        if (hasAccount(r3) == true) goto L5;
    L7:
        return false;
    L5:
        if (prefs(r3).getStringSet("sources", Collections.emptySet()).isEmpty() == true) goto L7;
        return true;
    }

    static File credentials(Context r3) {
        return new File(r3.getNoBackupFilesDir(), "movistar-account.enc");
    }

    static String current(Context r5, Entry r6) {
        String r0 = prefs(r5).getString("token", "");
        if (valid(r0, System.currentTimeMillis()) == false) goto L5;
        return r0;
    L5:
        return "";
    }

    static synchronized void disable(Context r3) {
        monitor-enter(CdnTokens.class);
        generation++;     // Catch: Throwable -> L7
        new AtomicFile(credentials(r3)).delete();     // Catch: Throwable -> L7
        prefs(r3).edit().clear().commit();     // Catch: Throwable -> L7
        ((JobScheduler) r3.getSystemService("jobscheduler")).cancel(JOB);     // Catch: Throwable -> L7
        main.removeCallbacks(due);     // Catch: Throwable -> L7
        monitor-exit(CdnTokens.class);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    static boolean eligible(Catalog.Source r4) {
        Boolean r0 = eligibleCache.get(r4);
        if (r0 != null) goto L5;
        boolean r1 = MovistarStreams.eligible(r4);
        eligibleCache.put(r4, Boolean.valueOf(r1));
        return r1;
    L5:
        return r0.booleanValue();
    }

    static long expiry(String r7) {
        return new JSONObject(new String(Base64.decode(r7.split("\\.")[1], 11), StandardCharsets.UTF_8)).optLong("exp", 0) * 1000;
    L7:
        return 0;
    }

    static Pending forPlayback(Context r4) {
        Pending r0 = renew(r4, false);
        monitor-enter(CdnTokens.class);
    L14:
        th = move-exception;
        throw th;
    L5:
        if (inFlight == null) goto L8;
        monitor-exit(CdnTokens.class);     // Catch: Throwable -> L14
        return r0;
    L8:
        monitor-exit(CdnTokens.class);     // Catch: Throwable -> L14
        if (valid(prefs(r4).getString("token", ""), System.currentTimeMillis()) == false) goto L13;
        return Pending.completed(true);
    L13:
        return r0;
    }

    static void foreground(Context r3) {
        app = r3.getApplicationContext();
        if (foreground == false) goto L5;
        return;
    L5:
        foreground = true;
        final Context r0 = app;
        executor.execute(new CdnTokens$$ExternalSyntheticLambda0(r0));
    }

    static boolean hasAccount(Context r1) {
        return credentials(r1).isFile();
    }

    static boolean hasToken(Map<String, String> r3) {
        Iterator<String> r0 = r3.keySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (r0.next().equalsIgnoreCase("x-tcdn-token") == false) goto L4;
        return true;
    L10:
        return false;
    }

    static Map<String, String> headers(Map<String, String> r4, String r5) {
        LinkedHashMap r0 = new LinkedHashMap(r4);
        Iterator r1 = r0.keySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L8;
        if (((String) r1.next()).equalsIgnoreCase("x-tcdn-token") == false) goto L4;
        r1.remove();
        goto L4
    L8:
        r0.put("x-tcdn-token", r5);
        return r0;
    }

    static boolean keepAccount(boolean r1, String r2, String r3, String r4) {
        if (r1 == true) goto L4;
    L12:
        return false;
    L4:
        if (r2.isEmpty() == true) goto L12;
        if (r4.isEmpty() == false) goto L12;
        if (r3.isEmpty() == false) goto L10;
    L11:
        return true;
    L10:
        if (r3.equals(r2) == false) goto L12;
        goto L11
    }

    static SecretKey key() throws Exception {
        KeyStore r1 = KeyStore.getInstance("AndroidKeyStore");
        r1.load(null);
        if (r1.containsAlias("chdtv_movistar_account") == true) goto L6;
        KeyGenerator r0 = KeyGenerator.getInstance("AES", "AndroidKeyStore");
        r0.init(new KeyGenParameterSpec.Builder("chdtv_movistar_account", 3).setBlockModes(new String[]{"GCM"}).setEncryptionPaddings(new String[]{"NoPadding"}).setKeySize(256).build());
        r0.generateKey();
    L6:
        return (SecretKey) r1.getKey("chdtv_movistar_account", null);
    }

    static /* synthetic */ void lambda$foreground$5(Context r1) {
        if (foreground == false) goto L6;
        arm(r1);
        return;
    }

    static /* synthetic */ void lambda$foreground$6(final Context r2) {
        if (foreground == true) goto L5;
        return;
    L5:
        renew(r2, true);
        main.post(new CdnTokens$$ExternalSyntheticLambda6(r2));
    }

    static /* synthetic */ void lambda$renew$3(Context r0) {
        arm(r0);
    }

    static /* synthetic */ void lambda$renew$4(final Context r11, int r12, Pending r13) {
        boolean r0 = false;
        JSONObject r1 = account(r11);     // Catch: Exception -> L19
        long r2 = SystemClock.elapsedRealtime();     // Catch: Exception -> L19
        BlackBox.event("MOVISTAR_RENEW_START", "");     // Catch: Exception -> L19
        MovistarTokenClient.Result r4 = new MovistarTokenClient(new MovistarTokenClient.Http()).renew(r1.getString("user"), r1.getString("password"));     // Catch: Exception -> L19
        BlackBox.event("MOVISTAR_RENEW_RESPONSE", "duration_ms=" + (SystemClock.elapsedRealtime() - r2));     // Catch: Exception -> L19
        long r5 = System.currentTimeMillis();     // Catch: Exception -> L19
        if (valid(r4.token, r5) == false) goto L18;
        monitor-enter(CdnTokens.class);     // Catch: Exception -> L19
    L14:
        th = move-exception;
        throw th;     // Catch: Exception -> L19
    L8:
        if (r12 == generation) goto L10;
    L12:
        monitor-exit(CdnTokens.class);     // Catch: Throwable -> L14
        boolean r3 = r0;
    L36:
        monitor-enter(CdnTokens.class);
    L49:
        th = move-exception;
        throw th;
    L38:
        if (r12 == generation) goto L40;
    L45:
        inFlight = null;     // Catch: Throwable -> L49
        monitor-exit(CdnTokens.class);     // Catch: Throwable -> L49
        r13.complete(r3);
        main.post(new CdnTokens$$ExternalSyntheticLambda2(r11));
        return;
    L40:
        if (configured(r11) == false) goto L45;
        if (r3 == false) goto L43;
        long r6 = Math.max(0, next(r11) - System.currentTimeMillis());     // Catch: Throwable -> L49
    L44:
        schedule(r11, r6);     // Catch: Throwable -> L49
        goto L45
    L43:
        r6 = 300000;
        goto L44
    L10:
        if (configured(r11) == false) goto L12;
        r0 = prefs(r11).edit().putString("token", r4.token).putLong("renewed", r5).putString("error", r4.warning).commit();     // Catch: Throwable -> L14
        goto L12
    L18:
        throw new IOException("El servidor devolvió un token caducado o sin fecha válida");     // Catch: Exception -> L19
    L19:
        e = move-exception;
        monitor-enter(CdnTokens.class);
    L52:
        th = move-exception;
        throw th;
    L23:
        if (r12 == generation) goto L25;
    L33:
        r3 = r0;
        goto L36
    L25:
        if ((e instanceof IOException) == false) goto L27;
        String r7 = e.getMessage();     // Catch: Throwable -> L52
    L28:
        SharedPreferences.Editor r8 = prefs(r11).edit();     // Catch: Throwable -> L52
        if (r7 != null) goto L31;
        String r9 = "No se pudo renovar el token";
    L32:
        r8.putString("error", r9).apply();     // Catch: Throwable -> L52
        goto L33
    L31:
        r9 = r7;
        goto L32
    L27:
        r7 = "No se pudo renovar: revisa la cuenta o la configuración del proveedor";
        goto L28
    }

    static /* synthetic */ void lambda$static$0(Runnable r1) {
        Process.setThreadPriority(10);
        r1.run();
    }

    static /* synthetic */ Thread lambda$static$1(final Runnable r3) {
        return new Thread(new CdnTokens$$ExternalSyntheticLambda1(r3), "CHDTVMovistar");
    }

    static /* synthetic */ void lambda$static$2() {
        if (app != null) goto L5;
        return;
    L5:
        if (foreground == false) goto L9;
        renew(app, false);
        arm(app);
        return;
    }

    static long next(Context r10) {
        long r0 = prefs(r10).getLong("renewed", 0);
        if (r0 != 0) goto L5;
        return 0;
    L5:
        long r4 = expiry(prefs(r10).getString("token", ""));
        long r8 = r0 + INTERVAL;
        if (r4 <= 0) goto L8;
        long r2 = r4 - 300000;
    L10:
        return Math.min(r8, r2);
    L8:
        r2 = r0 + INTERVAL;
        goto L10
    }

    static String originalToken(Entry r4) {
        Iterator<Map.Entry<String, String>> r0 = r4.headers.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L11;
        Map.Entry<String, String> r1 = r0.next();
        if (r1.getKey().equalsIgnoreCase("x-tcdn-token") == false) goto L4;
        return r1.getValue();
    L11:
        return MovistarStreams.queryToken(Entry.details(r4).url);
    }

    static SharedPreferences prefs(Context r2) {
        return r2.getSharedPreferences(PREF, 0);
    }

    static synchronized void reconcile(Context r7, Catalog r8) {
        monitor-enter(CdnTokens.class);
    L25:
        th = move-exception;
        throw th;
    L5:
        if (hasAccount(r7) == true) goto L7;
    L23:
        monitor-exit(CdnTokens.class);
        return;
    L7:
        if (prefs(r7).getBoolean("auto_new_sources", true) == false) goto L23;
        HashSet r1 = new HashSet(prefs(r7).getStringSet("sources", Collections.emptySet()));     // Catch: Throwable -> L25
        Set<String> r2 = prefs(r7).getStringSet("excluded_sources", Collections.emptySet());     // Catch: Throwable -> L25
        boolean r3 = false;
        Iterator<Catalog.Source> r4 = r8.sources.iterator();     // Catch: Throwable -> L25
    L11:
        if (r4.hasNext() == false) goto L19;
        Catalog.Source r5 = r4.next();     // Catch: Throwable -> L25
        if (eligible(r5) == false) goto L11;
        if (r2.contains(r5.f177id) == true) goto L11;
        if (r1.add(r5.f177id) == false) goto L11;
        r3 = true;
        goto L11
    L19:
        if (r3 == false) goto L21;
        prefs(r7).edit().putStringSet("sources", r1).commit();     // Catch: Throwable -> L25
    L21:
        monitor-exit(CdnTokens.class);
    }

    static boolean rejected(Throwable r4) {
        Throwable r0 = r4;
    L4:
        if (r0 == null) goto L18;
        if ((r0 instanceof HttpDataSource.InvalidResponseCodeException) == true) goto L7;
        r0 = r0.getCause();
        goto L4
    L7:
        int r2 = ((HttpDataSource.InvalidResponseCodeException) r0).responseCode;
        if (r2 == 401) goto L14;
        if (r2 == 403) goto L14;
        if (r2 == 418) goto L14;
        return false;
    L14:
        if (allowedHost(((HttpDataSource.InvalidResponseCodeException) r0).dataSpec.uri.toString()) == false) goto L22;
        return true;
    L22:
        return false;
    L18:
        return false;
    }

    static synchronized Pending renew(Context r8, boolean r9) {
        monitor-enter(CdnTokens.class);
        final Context r1 = r8.getApplicationContext();     // Catch: Throwable -> L24
        if (configured(r1) == true) goto L10;
        Pending r2 = Pending.completed(false);     // Catch: Throwable -> L24
    L7:
        monitor-exit(CdnTokens.class);
        return r2;
    L10:
        if (inFlight == null) goto L12;
        r2 = inFlight;     // Catch: Throwable -> L24
        goto L7
    L12:
        long r3 = System.currentTimeMillis();     // Catch: Throwable -> L24
        if (r9 == false) goto L15;
    L21:
        final int r4 = generation;     // Catch: Throwable -> L24
        final Pending r5 = new Pending();     // Catch: Throwable -> L24
        inFlight = r5;     // Catch: Throwable -> L24
        executor.execute(new CdnTokens$$ExternalSyntheticLambda3(r1, r4, r5));     // Catch: Throwable -> L24
        monitor-exit(CdnTokens.class);
        return r5;
    L15:
        if (r3 >= next(r1)) goto L21;
        if (valid(prefs(r1).getString("token", ""), r3) == false) goto L21;
        Pending r6 = Pending.completed(true);     // Catch: Throwable -> L24
        monitor-exit(CdnTokens.class);
        return r6;
    L24:
        th = move-exception;
        throw th;
    }

    static void schedule(Context r4, long r5) {
        if (configured(r4) == true) goto L5;
        return;
    L5:
        JobInfo r0 = new JobInfo.Builder(JOB, new ComponentName(r4, CdnTokenJob.class)).setRequiredNetworkType(1).setMinimumLatency(Math.max(0, r5)).setPersisted(true).setBackoffCriteria(300000, 1).build();
        ((JobScheduler) r4.getSystemService("jobscheduler")).schedule(r0);
    }

    static boolean scoped(Context r11, Entry r12) {
        if (allowedHost(Entry.details(r12).url) == true) goto L5;
    L38:
        return false;
    L5:
        if (hasAccount(r11) == false) goto L38;
        Catalog r0 = AppStorage.cachedCatalog();     // Catch: Exception -> L37
        if (r0 != null) goto L10;
        r0 = new AppStorage(r11).catalog();     // Catch: Exception -> L37
    L10:
        Set<String> r2 = AppStorage.indexFor(r0).matchingSources(r12);     // Catch: Exception -> L37
        monitor-enter(CdnTokens.class);     // Catch: Exception -> L37
        HashSet r4 = new HashSet(prefs(r11).getStringSet("sources", Collections.emptySet()));     // Catch: Throwable -> L34
        boolean r5 = false;
        if (prefs(r11).getBoolean("auto_new_sources", true) == false) goto L22;
        Set<String> r6 = prefs(r11).getStringSet("excluded_sources", Collections.emptySet());     // Catch: Throwable -> L34
        Iterator<String> r7 = r2.iterator();     // Catch: Throwable -> L34
    L16:
        if (r7.hasNext() == false) goto L22;
        String r9 = r7.next();     // Catch: Throwable -> L34
        if (r6.contains(r9) == true) goto L16;
        if (r4.add(r9) == false) goto L16;
        r5 = true;
    L22:
        if (r5 == false) goto L24;
        prefs(r11).edit().putStringSet("sources", r4).apply();     // Catch: Throwable -> L34
    L24:
        Iterator<String> r8 = r2.iterator();     // Catch: Throwable -> L34
    L26:
        if (r8.hasNext() == false) goto L32;
        if (r4.contains(r8.next()) == false) goto L26;
        monitor-exit(CdnTokens.class);     // Catch: Throwable -> L34
        return true;
    L32:
        monitor-exit(CdnTokens.class);     // Catch: Throwable -> L34
    L34:
        th = move-exception;
        throw th;     // Catch: Exception -> L37
    }

    static synchronized void selectSources(Context r5, Set<String> r6, Set<String> r7, boolean r8) {
        monitor-enter(CdnTokens.class);
        HashSet r1 = new HashSet(prefs(r5).getStringSet("excluded_sources", Collections.emptySet()));     // Catch: Throwable -> L14
        Iterator<String> r2 = r7.iterator();     // Catch: Throwable -> L14
    L6:
        if (r2.hasNext() == false) goto L11;
        String r3 = r2.next();     // Catch: Throwable -> L14
        if (r6.contains(r3) == true) goto L9;
        r1.add(r3);     // Catch: Throwable -> L14
        goto L6
    L9:
        r1.remove(r3);     // Catch: Throwable -> L14
        goto L6
    L11:
        prefs(r5).edit().putStringSet("sources", new HashSet(r6)).putStringSet("excluded_sources", r1).putBoolean("auto_new_sources", r8).commit();     // Catch: Throwable -> L14
        monitor-exit(CdnTokens.class);
        return;
    L14:
        th = move-exception;
        throw th;
    }

    static String status(Context r15) {
        if (configured(r15) == true) goto L6;
        return "Renovación desactivada";
    L6:
        long r0 = System.currentTimeMillis();
        long r2 = prefs(r15).getLong("renewed", 0);
        long r6 = Math.max(0, next(r15) - r0);
        if (r2 != 0) goto L9;
        String r4 = "Todavía no se ha renovado";
    L10:
        String r9 = "";
        String r5 = prefs(r15).getString("error", "");
        StringBuilder r8 = new StringBuilder().append(r4);
        if (r5.isEmpty() == true) goto L15;
        r9 = "\n" + r5;
    L15:
        return r8.append(r9).toString();
    L9:
        r4 = "Próxima renovación · " + String.format(Locale.ROOT, "%02d:%02d:%02d", new Object[]{Long.valueOf(r6 / 3600000), Long.valueOf((r6 / 60000) % 60), Long.valueOf((r6 / 1000) % 60)});
        goto L10
    }

    static boolean valid(String r4, long r5) {
        if (expiry(r4) <= (30000 + r5)) goto L5;
        return true;
    L5:
        return false;
    }

    static Map<String, String> withoutToken(Map<String, String> r4) {
        LinkedHashMap r0 = new LinkedHashMap(r4);
        Iterator r1 = r0.keySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L8;
        if (((String) r1.next()).equalsIgnoreCase("x-tcdn-token") == false) goto L4;
        r1.remove();
        goto L4
    L8:
        return r0;
    }
}
