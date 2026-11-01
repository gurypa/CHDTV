package es.antonio.m3u;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.common.net.HttpHeaders;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes7.dex */
final class LocalPanel {
    private static volatile String address;
    private static Context app;
    private static ScheduledFuture<?> burst;
    private static volatile boolean enabled;
    private static volatile int epoch;
    private static PanelJournal journal;
    private static final AtomicLong journalErrors = null;
    private static volatile long lastOk;
    private static boolean ownsCapture;
    private static String session;
    private static volatile boolean snapshotBusy;
    private static volatile String snapshotDone;
    private static volatile PanelJournal.Batch snapshotEnd;
    private static volatile String snapshotError;
    private static volatile String status;
    private static volatile String token;
    private static ScheduledExecutorService worker;

    private static final class Status extends IOException {
        final int code;

        Status(int r1) {
            this.code = r1;
        }
    }

    public static /* synthetic */ void $r8$lambda$wJVp90tJgnxEw9wpVFqtxBFrES8() {
        flush();
    }

    static {
        address = "";
        token = "";
        status = "Desactivado";
        snapshotDone = "";
        snapshotError = "";
        journalErrors = new AtomicLong();
    }

    private LocalPanel() {
    }

    static void attach(final Activity r4) {
        ViewGroup r0 = findBody(r4.findViewById(android.R.id.content));
        if (r0 != null) goto L5;
        return;
    L5:
        final Button r1 = new Button(r4);
        r1.setAllCaps(false);
        StringBuilder r2 = new StringBuilder("Panel web local · ");
        if (enabled == false) goto L8;
        String r3 = "Activado";
    L9:
        r1.setText(r2.append(r3).toString());
        r1.setOnClickListener(new LocalPanel$$ExternalSyntheticLambda8(r4, r1));
        r0.addView(r1, new ViewGroup.LayoutParams(-1, -2));
        return;
    L8:
        r3 = "Desactivado";
        goto L9
    }

    private static void configure(final Activity r11, final Button r12) {
        LinearLayout r0 = new LinearLayout(r11);
        r0.setOrientation(1);
        int r1 = Math.round(r11.getResources().getDisplayMetrics().density * 20.0f);
        r0.setPadding(r1, r1, r1, r1);
        TextView r2 = new TextView(r11);
        StringBuilder r3 = new StringBuilder("Estado: ").append(status);
        if (lastOk <= 0) goto L5;
        String r4 = "\nÚltima respuesta hace " + Math.max(0, (System.currentTimeMillis() - lastOk) / 1000) + " s";
    L6:
        r2.setText(r3.append(r4).append("\n\nConecta el ordenador y este dispositivo a la misma Wi‑Fi. Abre el panel en el ordenador y escribe su dirección IP y el código de 8 cifras.\n\nLa caja negra se guarda y envía por dispositivo. Si el panel se desconecta, se reintenta desde un búfer de hasta 32 MB. El panel puede pedir el registro retenido. No se envían listas, URLs, claves ni tokens de proveedores. Mientras el panel esté activado se mantiene la captura; desactívalo aquí para detener el envío.").toString());
        r0.addView(r2);
        final EditText r5 = new EditText(r11);
        r5.setSingleLine();
        r5.setHint("http://192.168.1.50:8765");
        r5.setText(address);
        r5.setInputType(17);
        r0.addView(r5);
        final EditText r6 = new EditText(r11);
        r6.setSingleLine();
        r6.setHint("Código de 8 cifras");
        r6.setInputType(2);
        r0.addView(r6);
        ScrollView r7 = new ScrollView(r11);
        r7.addView(r0);
        final AlertDialog r8 = new AlertDialog.Builder(r11).setTitle("CHDTV · Panel web local").setView(r7).setPositiveButton("Vincular", null).setNeutralButton("Desactivar", new LocalPanel$$ExternalSyntheticLambda5(r12)).setNegativeButton("Cerrar", null).create();
        r8.setOnShowListener(new LocalPanel$$ExternalSyntheticLambda6(r8, r5, r6, r11, r12));
        r8.show();
        return;
    L5:
        r4 = "";
        goto L6
    }

    private static JSONObject event(String r7, long r8, long r10) throws Exception {
        String r9 = safeRow(r7);
        int r0 = r9.indexOf(32);
        if (r0 >= 0) goto L5;
        String r1 = r9;
    L7:
        if (r1.matches("[A-Z0-9_]{1,80}") == true) goto L9;
        r1 = "BLACKBOX_LINE";
        r0 = -1;
    L9:
        String r2 = r9.replaceFirst(" dropped_events=[0-9]+$", "");
        JSONObject r3 = new JSONObject();
        r3.put("id", "log:" + SourceStore.digest(String.valueOf(r8) + "|" + r10 + "|" + r2));
        r3.put("op", r1);
        StringBuilder r11 = new StringBuilder("uptime_ms=").append(r10).append(" ");
        if (r0 < 0) goto L13;
        r9 = r9.substring(r0 + 1);
    L13:
        r3.put("row", r11.append(r9).toString());
        r3.put("time", r8);
        return r3;
    L5:
        r1 = r9.substring(0, r0);
        goto L7
    }

    private static synchronized ScheduledExecutorService executor() {
        monitor-enter(LocalPanel.class);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (worker != null) goto L7;
        worker = Executors.newSingleThreadScheduledExecutor(new LocalPanel$$ExternalSyntheticLambda12());     // Catch: Throwable -> L10
        worker.scheduleWithFixedDelay(new LocalPanel$$ExternalSyntheticLambda10(), 3, 3, TimeUnit.SECONDS);     // Catch: Throwable -> L10
    L7:
        ScheduledExecutorService r1 = worker;     // Catch: Throwable -> L10
        monitor-exit(LocalPanel.class);
        return r1;
    }

    private static ViewGroup findBody(View r2) {
        if ((r2 instanceof Button) == false) goto L11;
        if ("Iniciar registro".contentEquals(((Button) r2).getText()) == false) goto L11;
        if ((r2.getParent() instanceof ViewGroup) == false) goto L11;
        return (ViewGroup) r2.getParent();
    L11:
        if ((r2 instanceof ViewGroup) == false) goto L20;
        ViewGroup r3 = (ViewGroup) r2;
        int r0 = 0;
    L14:
        if (r0 >= r3.getChildCount()) goto L24;
        ViewGroup r1 = findBody(r3.getChildAt(r0));
        if (r1 != null) goto L18;
        r0 = r0 + 1;
        goto L14
    L18:
        return r1;
    L24:
        return null;
    L20:
        return null;
    }

    private static void flush() {
        if (burst == null) goto L6;
        burst.cancel(false);
        burst = null;
    L6:
        if (enabled == true) goto L8;
        return;
    L8:
        if (token.isEmpty() == false) goto L10;
        return;
    L10:
        if (BackgroundWork.busy() == true) goto L69;
        int r0 = epoch;
        String r2 = address;
        String r3 = token;
    L44:
        e = move-exception;
        if (r0 == epoch) goto L49;
        return;
    L49:
        if ((e instanceof Status) == true) goto L51;
    L53:
        status = "Sin respuesta · registro guardado para reintentar";
        return;
    L51:
        if (((Status) e).code != 403) goto L53;
        enabled = false;
        status = "Vínculo revocado · vuelve a vincular";
        prefs().edit().putBoolean("enabled", false).putString("token", "").apply();
        return;
    L14:
        if (BlackBox.active() == true) goto L16;
        new Handler(Looper.getMainLooper()).post(new LocalPanel$$ExternalSyntheticLambda9());     // Catch: Exception -> L44
    L16:
        PanelJournal.Batch r4 = journal().read(50);     // Catch: Exception -> L44
        JSONArray r5 = new JSONArray();     // Catch: Exception -> L44
        Iterator<String> r6 = r4.rows.iterator();     // Catch: Exception -> L44
    L18:
        if (r6.hasNext() == false) goto L19;
        r5.put(new JSONObject(r6.next()));     // Catch: JSONException -> L41 Exception -> L44
    L42:
        journalErrors.incrementAndGet();     // Catch: Exception -> L44
        goto L18
    L19:
        JSONObject r7 = new JSONObject();     // Catch: Exception -> L44
        r7.put("events", r5);     // Catch: Exception -> L44
        r7.put("heartbeat", heartbeat(r4));     // Catch: Exception -> L44
        JSONObject r8 = request(r2, "/api/events", r3, r7);     // Catch: Exception -> L44
        if (r8.optBoolean("ok") == false) goto L37;
        if (r0 == epoch) goto L24;
        return;
    L24:
        if (r4.rows.isEmpty() == true) goto L26;
        journal().acknowledge(r4);     // Catch: Exception -> L44
    L26:
        lastOk = System.currentTimeMillis();     // Catch: Exception -> L44
        status = "Conectado";     // Catch: Exception -> L44
        JSONObject r9 = r8.optJSONObject("request");     // Catch: Exception -> L44
        if (r9 == null) goto L32;
        if ("snapshot".equals(r9.optString("type")) == false) goto L32;
        snapshot(r9.optString("id"));     // Catch: Exception -> L44
    L32:
        if (enabled == true) goto L34;
        return;
    L34:
        if (journal().pendingBytes() <= 0) goto L66;
        burst = executor().schedule(new LocalPanel$$ExternalSyntheticLambda10(), 500, TimeUnit.MILLISECONDS);     // Catch: Exception -> L44
        return;
    L66:
        return;
    L37:
        throw new IOException();     // Catch: Exception -> L44
    }

    private static JSONObject heartbeat(PanelJournal.Batch r6) throws Exception {
        JSONObject r0 = new JSONObject();
        Runtime r1 = Runtime.getRuntime();
        r0.put("heap_bytes", r1.totalMemory() - r1.freeMemory());
        r0.put("native_bytes", Debug.getNativeHeapAllocatedSize());
        int r2 = 0;
        r0.put("queue_dropped", 0);
        r0.put("log_protocol", 2);
        r0.put("journal_pending_bytes", journal().pendingBytes());
        r0.put("journal_lost_bytes", journal().lostBytes());
        r0.put("journal_errors", journalErrors.get());
        PanelJournal.Batch r3 = snapshotEnd;
        if (r3 != null) goto L5;
    L7:
        String r7 = "";
    L8:
        r0.put("snapshot_done", r7);
        r0.put("snapshot_error", snapshotError);
        r0.put("session", session);
        r0.put("version", app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName);
        r0.put("capture_active", BlackBox.active());
        Catalog r8 = AppStorage.cachedCatalog();
        if (r8 != null) goto L11;
    L19:
        return r0;
    L11:
        if (r8.demand == null) goto L19;
        if (r8.demand.knownCounts == false) goto L19;
        JSONObject r4 = new JSONObject();
        String[] r5 = {"Canales", "Películas", "Series"};
    L16:
        if (r2 >= 3) goto L17;
        String r9 = r5[r2];
        r4.put(r9, r8.demand.count(r9));
        r2 = r2 + 1;
        goto L16
    L17:
        r0.put("counts", r4);
        goto L19
    L5:
        if (PanelJournal.covers(r6, r3) == false) goto L7;
        r7 = snapshotDone;
        goto L8
    }

    private static void importSnapshot(File r8) throws Exception {
        Throwable r0 = null;
        BufferedReader r1 = new BufferedReader(new InputStreamReader(new FileInputStream(r8), StandardCharsets.UTF_8));     // Catch: Throwable -> L35
        SimpleDateFormat r9 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ROOT);     // Catch: Throwable -> L31
    L5:
        String r2 = r1.readLine();     // Catch: Throwable -> L31
        if (r2 == null) goto L7;
        if (enabled == false) goto L11;
        if (r2.length() < 26) goto L5;
        long r3 = r9.parse(r2.substring(0, 23)).getTime();     // Catch: NumberFormatException -> L27 ParseException -> L29 Throwable -> L31
        String r4 = r2.substring(24);     // Catch: NumberFormatException -> L27 ParseException -> L29 Throwable -> L31
        goto L5
    L19:
        if (r4.startsWith("uptime_ms=") == false) goto L24;
        int r5 = r4.indexOf(32);     // Catch: NumberFormatException -> L27 ParseException -> L29 Throwable -> L31
        if (r5 < 0) goto L5;
        long r6 = Long.parseLong(r4.substring(10, r5));     // Catch: NumberFormatException -> L27 ParseException -> L29 Throwable -> L31
        r4 = r4.substring(r5 + 1);     // Catch: NumberFormatException -> L27 ParseException -> L29 Throwable -> L31
    L25:
        journal().append(event(r4, r3, r6).put("archive", true).toString());     // Catch: NumberFormatException -> L27 ParseException -> L29 Throwable -> L31
        goto L5
    L24:
        r6 = 0;
        goto L25
    L11:
        r1.close();     // Catch: Throwable -> L35
        return;
    L7:
        r1.close();     // Catch: Throwable -> L35
        return;
    L31:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L35
        throw th;     // Catch: Throwable -> L35
    L35:
        th = move-exception;
        if (0 == 0) goto L59;
        if (null == th) goto L40;
        r0.addSuppressed(th);
    L40:
        throw null;
    }

    static void init(Context r2) {
        app = r2.getApplicationContext();
        SharedPreferences r3 = prefs();
        address = r3.getString("address", "");
        token = r3.getString("token", "");
        ownsCapture = r3.getBoolean("owns_capture", false);
        if (r3.getBoolean("enabled", false) == true) goto L6;
    L10:
        boolean r4 = false;
    L11:
        enabled = r4;     // Catch: Exception -> L13
    L16:
        if (enabled == false) goto L21;
        if (session != null) goto L21;
        session = UUID.randomUUID().toString();
    L21:
        if (enabled == false) goto L23;
        String r5 = "Esperando conexión";
    L24:
        status = r5;
        return;
    L23:
        r5 = "Desactivado";
        goto L24
    L6:
        if (normalize(address).equals(address) == false) goto L10;
        if (token.matches("[A-Za-z0-9_-]{30,128}") == false) goto L10;
        r4 = true;
    L14:
        enabled = false;
        goto L16
    }

    private static synchronized PanelJournal journal() throws IOException {
        monitor-enter(LocalPanel.class);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (journal != null) goto L7;
        journal = new PanelJournal(new File(app.getFilesDir(), "panel-journal-v205"));     // Catch: Throwable -> L10
    L7:
        PanelJournal r1 = journal;     // Catch: Throwable -> L10
        monitor-exit(LocalPanel.class);
        return r1;
    }

    static /* synthetic */ Thread lambda$0(final Runnable r2) {
        Thread r0 = new Thread(new LocalPanel$$ExternalSyntheticLambda13(r2), "CHDTVLocalPanel");
        r0.setDaemon(true);
        return r0;
    }

    static /* synthetic */ void lambda$1(Runnable r0) {
        BackgroundWork.lowPriority();
        r0.run();
    }

    static /* synthetic */ void lambda$10(EditText r7, EditText r8, final Activity r9, final AlertDialog r10, final Button r11, View r12) {
        final String r3 = normalize(r7.getText().toString());     // Catch: Exception -> L9
        if (r8.getText().toString().matches("[0-9]{8}") == false) goto L8;
        final String r1 = r8.getText().toString();
        final int r4 = epoch + 1;
        epoch = r4;
        r10.getButton(-1).setEnabled(false);
        executor().execute(new LocalPanel$$ExternalSyntheticLambda7(r1, r9, r3, r4, r11, r10));
        return;
    L8:
        throw new IOException();     // Catch: Exception -> L9
    L10:
        Toast.makeText(r9, "Usa una IP privada y el código de 8 cifras del panel.", 1).show();
    }

    static /* synthetic */ void lambda$11(String r8, final Activity r9, final String r10, final int r11, final Button r12, final AlertDialog r13) {
        String r2 = prefs().getString("device_id", "");     // Catch: Exception -> L11
        if (r2.isEmpty() == false) goto L6;
        r2 = UUID.randomUUID().toString();     // Catch: Exception -> L11
        prefs().edit().putString("device_id", r2).apply();     // Catch: Exception -> L11
    L6:
        JSONObject r3 = new JSONObject();     // Catch: Exception -> L11
        r3.put("code", r8);     // Catch: Exception -> L11
        r3.put("device_id", r2);     // Catch: Exception -> L11
        r3.put("name", BlackBox.clean(Build.MODEL));     // Catch: Exception -> L11
        r3.put("model", BlackBox.clean(String.valueOf(Build.MANUFACTURER) + " " + Build.MODEL));     // Catch: Exception -> L11
        r3.put("version", r9.getPackageManager().getPackageInfo(r9.getPackageName(), 0).versionName);     // Catch: Exception -> L11
        r3.put("sdk", Build.VERSION.SDK_INT);     // Catch: Exception -> L11
        final String r5 = request(r10, "/api/pair", "", r3).getString("token");     // Catch: Exception -> L11
        if (r5.matches("[A-Za-z0-9_-]{30,128}") == false) goto L10;
        r9.runOnUiThread(new LocalPanel$$ExternalSyntheticLambda3(r11, r9, r10, r5, r12, r13));     // Catch: Exception -> L11
        return;
    L10:
        throw new IOException();     // Catch: Exception -> L11
    L12:
        r9.runOnUiThread(new LocalPanel$$ExternalSyntheticLambda4(r11, r9, r13));
    }

    static /* synthetic */ void lambda$12(int r2, Activity r3, String r4, String r5, Button r6, AlertDialog r7) {
        if (r2 == epoch) goto L5;
        return;
    L5:
        if (r3.isDestroyed() == true) goto L10;
        address = r4;
        token = r5;
        enabled = true;
        status = "Conectado";
        lastOk = System.currentTimeMillis();
        prefs().edit().putString("address", r4).putString("token", r5).putBoolean("enabled", true).apply();
        startCapture();
        executor().execute(new LocalPanel$$ExternalSyntheticLambda11());
        r6.setText("Panel web local · Activado");
        r7.dismiss();
        Toast.makeText(r3, "Panel vinculado. Ya recibe el diagnóstico.", 1).show();
        return;
    }

    static /* synthetic */ void lambda$13() {
        journal().rewind();     // Catch: IOException -> L5
        snapshotDone = "";     // Catch: IOException -> L5
        snapshotError = "";     // Catch: IOException -> L5
        snapshot("initial");     // Catch: IOException -> L5
        return;
    L6:
        journalErrors.incrementAndGet();
    }

    static /* synthetic */ void lambda$14(int r1, Activity r2, AlertDialog r3) {
        if (r1 == epoch) goto L5;
        return;
    L5:
        if (r2.isDestroyed() == true) goto L10;
        r3.getButton(-1).setEnabled(true);
        Toast.makeText(r2, "No se pudo vincular. Revisa IP, código, Wi‑Fi y servidor abierto.", 1).show();
        return;
    }

    static /* synthetic */ void lambda$3() {
        if (enabled == true) goto L5;
        return;
    L5:
        if (BlackBox.active() == true) goto L9;
        startCapture();
        return;
    }

    static /* synthetic */ void lambda$5(final String r2, final File r3, final Exception r4) {
        executor().execute(new LocalPanel$$ExternalSyntheticLambda0(r4, r3, r2));
    }

    static /* synthetic */ void lambda$6(Exception r2, File r3, String r4) {
        if (r2 != null) goto L9;
        if (r3 == null) goto L9;
        importSnapshot(r3);     // Catch: Throwable -> L10 Exception -> L12
        snapshotEnd = journal().mark();     // Catch: Throwable -> L10 Exception -> L12
        snapshotDone = r4;     // Catch: Throwable -> L10 Exception -> L12
        snapshotError = "";     // Catch: Throwable -> L10 Exception -> L12
        if ("initial".equals(r4) == false) goto L14;
        prefs().edit().putBoolean("history_imported_v205", true).apply();     // Catch: Throwable -> L10 Exception -> L12
    L14:
        snapshotBusy = false;
        return;
    L9:
        throw new IOException();     // Catch: Throwable -> L10 Exception -> L12
    L10:
        th = move-exception;
        snapshotBusy = false;
        throw th;
    L13:
        snapshotError = r4;     // Catch: Throwable -> L10
        journalErrors.incrementAndGet();     // Catch: Throwable -> L10
        goto L14
    }

    static /* synthetic */ void lambda$7(Activity r0, Button r1, View r2) {
        configure(r0, r1);
    }

    static /* synthetic */ void lambda$8(Button r2, DialogInterface r3, int r4) {
        epoch++;
        enabled = false;
        token = "";
        status = "Desactivado";
        prefs().edit().putBoolean("enabled", false).putString("token", "").putBoolean("owns_capture", false).apply();
        if (ownsCapture == true) goto L5;
    L7:
        ownsCapture = false;
        r2.setText("Panel web local · Desactivado");
        return;
    L5:
        if (BlackBox.active() == false) goto L7;
        BlackBox.stop();
        goto L7
    }

    static /* synthetic */ void lambda$9(final AlertDialog r7, final EditText r8, final EditText r9, final Activity r10, final Button r11, DialogInterface r12) {
        r7.getButton(-1).setOnClickListener(new LocalPanel$$ExternalSyntheticLambda2(r8, r9, r10, r7, r11));
    }

    static String normalize(String r9) throws Exception {
        String r10 = r9.trim();
        if (r10.startsWith("http://") == false) goto L60;
        URI r1 = new URI(r10);
        String r11 = r1.getHost();
        if (r11 == null) goto L58;
        if (r11.matches("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+") == false) goto L58;
        if (r1.getRawUserInfo() != null) goto L58;
        if (r1.getRawQuery() != null) goto L58;
        if (r1.getRawFragment() != null) goto L58;
        if (r1.getRawPath() != null) goto L17;
    L20:
        String[] r2 = r11.split("\\.");
        int[] r4 = new int[4];
        int r6 = 0;
    L21:
        if (r6 >= 4) goto L23;
        r4[r6] = Integer.parseInt(r2[r6]);
        if (r4[r6] > 255) goto L56;
        if (String.valueOf(r4[r6]).equals(r2[r6]) == false) goto L56;
        r6 = r6 + 1;
    L56:
        throw new IOException("IP no válida");
    L23:
        if (r4[0] == 10) goto L40;
        if (r4[0] != 192) goto L29;
        if (r4[1] == 168) goto L40;
    L29:
        if (r4[0] != 172) goto L35;
        if (r4[1] < 16) goto L35;
        if (r4[1] <= 31) goto L40;
    L35:
        if (r4[0] == 127) goto L40;
        throw new IOException("Se requiere IP privada");
    L40:
        if (r1.getPort() != (-1)) goto L42;
        int r3 = 8765;
    L43:
        if (r3 < 1) goto L49;
        if (r3 > 65535) goto L49;
        return "http://" + r11 + ":" + r3;
    L49:
        throw new IOException("Puerto no válido");
    L42:
        r3 = r1.getPort();
        goto L43
    L17:
        if (r1.getRawPath().isEmpty() == true) goto L20;
        if (r1.getRawPath().equals("/") == true) goto L20;
    L58:
        throw new IOException("Dirección local no válida");
    L60:
        throw new IOException("Solo HTTP local");
    }

    static void offer(String r1, long r2, long r4) {
        if (enabled == true) goto L5;
        return;
    L5:
        if (app == null) goto L15;
        journal().append(event(r1, r2, r4).toString());     // Catch: Exception -> L9
        return;
    L10:
        journalErrors.incrementAndGet();
        return;
    }

    private static SharedPreferences prefs() {
        return app.getSharedPreferences("local-panel-v194", 0);
    }

    static void ready() {
        if (enabled == false) goto L8;
        startCapture();
        if (prefs().getBoolean("history_imported_v205", false) == true) goto L9;
        snapshot("initial");
        return;
    L9:
        return;
    }

    private static JSONObject request(String r5, String r6, String r7, JSONObject r8) throws Exception {
        HttpURLConnection r9 = (HttpURLConnection) new URL(String.valueOf(normalize(r5)) + r6).openConnection();
        r9.setConnectTimeout(2200);     // Catch: Throwable -> L53
        r9.setReadTimeout(2200);     // Catch: Throwable -> L53
        r9.setInstanceFollowRedirects(false);     // Catch: Throwable -> L53
        r9.setRequestMethod("POST");     // Catch: Throwable -> L53
        r9.setDoOutput(true);     // Catch: Throwable -> L53
        r9.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/json");     // Catch: Throwable -> L53
        if (r7.isEmpty() == true) goto L6;
        r9.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + r7);     // Catch: Throwable -> L53
    L6:
        byte[] r10 = r8.toString().getBytes(StandardCharsets.UTF_8);     // Catch: Throwable -> L53
        r9.setFixedLengthStreamingMode(r10.length);     // Catch: Throwable -> L53
        Throwable r11 = null;
        OutputStream r0 = r9.getOutputStream();     // Catch: Throwable -> L48
        r0.write(r10);     // Catch: Throwable -> L44
        if (r0 == null) goto L12;
        r0.close();     // Catch: Throwable -> L48
    L12:
        int r12 = r9.getResponseCode();     // Catch: Throwable -> L53
        if (r12 != 200) goto L69;
        ByteArrayOutputStream r13 = new ByteArrayOutputStream();     // Catch: Throwable -> L53
        InputStream r1 = r9.getInputStream();     // Catch: Throwable -> L36
        byte[] r2 = new byte[4096];     // Catch: Throwable -> L31
    L18:
        int r3 = r1.read(r2);     // Catch: Throwable -> L31
        if (r3 == (-1)) goto L21;
        if ((r13.size() + r3) > 65536) goto L30;
        r13.write(r2, 0, r3);     // Catch: Throwable -> L31
        goto L18
    L30:
        throw new IOException();     // Catch: Throwable -> L31
    L21:
        if (r1 == null) goto L23;
        r1.close();     // Catch: Throwable -> L36
    L23:
        JSONObject r14 = new JSONObject(r13.toString("UTF-8"));     // Catch: Throwable -> L53
        r9.disconnect();
        return r14;
    L31:
        th = move-exception;
        if (r1 == null) goto L65;
        r1.close();     // Catch: Throwable -> L36
        throw th;     // Catch: Throwable -> L36
    L65:
        throw th;     // Catch: Throwable -> L36
    L36:
        th = move-exception;
        if (0 == 0) goto L66;
        if (null == th) goto L41;
        r11.addSuppressed(th);     // Catch: Throwable -> L53
    L41:
        throw null;     // Catch: Throwable -> L53
    L66:
        throw th;     // Catch: Throwable -> L53
    L69:
        throw new Status(r12);     // Catch: Throwable -> L53
    L44:
        th = move-exception;
        if (r0 == null) goto L47;
        r0.close();     // Catch: Throwable -> L48
    L47:
        throw th;     // Catch: Throwable -> L48
    L48:
        th = move-exception;
        if (0 == 0) goto L67;
        if (null == th) goto L68;
        r11.addSuppressed(th);     // Catch: Throwable -> L53
    L68:
        throw null;     // Catch: Throwable -> L53
    L67:
        throw th;     // Catch: Throwable -> L53
    L53:
        th = move-exception;
        r9.disconnect();
        throw th;
    }

    private static String safeRow(String r2) {
        if (r2 != null) goto L4;
        String r3 = "";
    L6:
        return r3.substring(0, Math.min(r3.length(), 16384));
    L4:
        r3 = r2.replaceAll("(?i)https?://[^\\s]+", "[URL]").replaceAll("(?i)(?:bearer\\s+|basic\\s+|(?:token|password|secret|authorization|cookie|license|key)\\s*[:=]\\s*(?:Bearer\\s+|Basic\\s+)?)[^\\s,;]+", "[SECRETO]").replaceAll("[A-Za-z0-9_-]{40,}", "[SECRETO]").replace('\n', ' ').replace('\r', ' ');
        goto L6
    }

    private static void snapshot(final String r1) {
        if (r1.isEmpty() == false) goto L5;
        return;
    L5:
        if (snapshotBusy == false) goto L7;
        return;
    L7:
        if (r1.equals(snapshotDone) == false) goto L9;
        return;
    L9:
        if (r1.equals(snapshotError) == true) goto L16;
        snapshotBusy = true;
        BlackBox.snapshot(new LocalPanel$$ExternalSyntheticLambda1(r1));
        return;
    }

    private static void startCapture() {
        if (session != null) goto L6;
        session = UUID.randomUUID().toString();
    L6:
        if (BlackBox.active() == true) goto L8;
        ownsCapture = true;
        prefs().edit().putBoolean("owns_capture", true).apply();
        BlackBox.start();
    L8:
        executor();
    }
}
