package es.antonio.m3u;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Process;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;
import androidx.media3.datasource.HttpDataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.net.ssl.SSLException;

/* JADX INFO: loaded from: classes7.dex */
public final class HlsDiagnostics225 {
    private static final int MAX_SAMPLE = 8388608;
    private static volatile Context app;
    private static volatile boolean capture;
    private static volatile boolean detailed;
    private static final Object files = null;
    private static long goodRun;
    private static String lastFailed;
    private static final AtomicLong serial = null;
    private static final Set<Long> summarized = null;

    static {
        detailed = true;
        serial = new AtomicLong();
        files = new Object();
        goodRun = -1;
        lastFailed = "";
        summarized = new HashSet();
    }

    private HlsDiagnostics225() {
    }

    public static boolean active() {
        return enabled();
    }

    private static String category(Throwable r4) {
        Set r0 = Collections.newSetFromMap(new IdentityHashMap());
        Throwable r1 = r4;
    L3:
        if (r1 == null) goto L26;
        if (r0.size() >= 16) goto L26;
        if (r0.add(r1) == false) goto L26;
        if ((r1 instanceof HttpDataSource.InvalidResponseCodeException) == true) goto L11;
        if ((r1 instanceof SocketTimeoutException) == true) goto L14;
        if ((r1 instanceof UnknownHostException) == true) goto L18;
        if ((r1 instanceof SSLException) == true) goto L22;
        r1 = r1.getCause();
        goto L3
    L22:
        return "TLS";
    L18:
        return "DNS";
    L14:
        return "TIMEOUT";
    L11:
        return "HTTP_" + ((HttpDataSource.InvalidResponseCodeException) r1).responseCode;
    L26:
        if ((r4 instanceof LinkageError) == false) goto L30;
        return "NATIVE_LINK";
    L30:
        if ((r4 instanceof InterruptedIOException) == false) goto L34;
        return "CANCELLED";
    L34:
        if ((r4 instanceof IOException) == false) goto L37;
        return "IO";
    L37:
        return "RUNTIME";
    }

    private static void clearSamples() {
        Object r0 = files;
        monitor-enter(r0);
    L22:
        th = move-exception;
        throw th;
    L5:
        if (app != null) goto L8;
        monitor-exit(r0);     // Catch: Throwable -> L22
        return;
    L8:
        File[] r1 = folder().listFiles();     // Catch: Throwable -> L22
        int r2 = 0;
        if (r1 == null) goto L13;
        int r3 = r1.length;     // Catch: Throwable -> L22
        int r4 = 0;
    L11:
        if (r4 >= r3) goto L13;
        r1[r4].delete();     // Catch: Throwable -> L22
        r4 = r4 + 1;     // Catch: Throwable -> L22
    L13:
        File[] r5 = new File(app.getCacheDir(), "shared-backups").listFiles(new HlsDiagnostics225$$ExternalSyntheticLambda7());     // Catch: Throwable -> L22
        if (r5 == null) goto L18;
        int r6 = r5.length;     // Catch: Throwable -> L22
    L16:
        if (r2 >= r6) goto L18;
        r5[r2].delete();     // Catch: Throwable -> L22
        r2 = r2 + 1;     // Catch: Throwable -> L22
    L18:
        goodRun = -1;     // Catch: Throwable -> L22
        lastFailed = "";     // Catch: Throwable -> L22
        monitor-exit(r0);     // Catch: Throwable -> L22
        event("HLS225_SAMPLE", "status=cleared");
    }

    private static boolean enabled() {
        if (app != null) goto L5;
    L9:
        return false;
    L5:
        if (detailed == false) goto L9;
        if (BlackBox.active() == false) goto L9;
        return true;
    }

    private static void event(String r0, String r1) {
        BlackBox.event(r0, r1);
    }

    private static void exportSamples(final Activity r2, final Button r3) {
        r3.setEnabled(false);
        new Thread(new HlsDiagnostics225$$ExternalSyntheticLambda2(r2, r3), "CHDTVHlsExport").start();
    }

    private static LinearLayout findBody(View r5) {
        int r1 = 0;
        if ((r5 instanceof LinearLayout) == false) goto L16;
        LinearLayout r0 = (LinearLayout) r5;
        int r2 = 0;
    L6:
        if (r2 >= r0.getChildCount()) goto L16;
        View r3 = r0.getChildAt(r2);
        if ((r3 instanceof TextView) == false) goto L14;
        String r4 = ((TextView) r3).getText().toString();
        if (r4.equals("Caja negra · diagnóstico") == true) goto L13;
        if (r4.equals("Guardar TXT") == false) goto L14;
    L13:
        return r0;
    L14:
        r2 = r2 + 1;
    L16:
        if ((r5 instanceof ViewGroup) == false) goto L24;
        ViewGroup r6 = (ViewGroup) r5;
    L19:
        if (r1 >= r6.getChildCount()) goto L33;
        LinearLayout r7 = findBody(r6.getChildAt(r1));
        if (r7 != null) goto L22;
        r1 = r1 + 1;
        goto L19
    L22:
        return r7;
    L33:
        return null;
    L24:
        return null;
    }

    private static File folder() {
        return new File(app.getFilesDir(), "hls-diagnostics-225");
    }

    public static void init(Context r3) {
        app = r3.getApplicationContext();     // Catch: RuntimeException -> L5
        SharedPreferences r4 = app.getSharedPreferences("hls-diagnostics-225", 0);     // Catch: RuntimeException -> L5
        detailed = r4.getBoolean("detailed", true);     // Catch: RuntimeException -> L5
        capture = r4.getBoolean("capture", false);     // Catch: RuntimeException -> L5
        return;
    L6:
        detailed = true;
        capture = false;
    }

    public static void install(Activity r1) {
        installInternal(r1);     // Catch: RuntimeException -> L4
        return;
    L5:
        event("HLS225_DIAGNOSTIC", "status=controls_unavailable");
    }

    private static void installInternal(final Activity r4) {
        init(r4);
        LinearLayout r0 = findBody(r4.findViewById(android.R.id.content));
        if (r0 != null) goto L5;
        return;
    L5:
        TextView r1 = new TextView(r4);
        r1.setText("Diagnóstico HLS · Rust");
        r1.setTextSize(20.0f);
        r0.addView(r1);
        CheckBox r2 = new CheckBox(r4);
        r2.setText("Añadir diagnóstico HLS detallado al TXT");
        r2.setChecked(detailed);
        r0.addView(r2);
        r2.setOnCheckedChangeListener(new HlsDiagnostics225$$ExternalSyntheticLambda3());
        TextView r3 = new TextView(r4);
        r3.setText("Con el registro activo: causa interna de Rust, fase, paquete/PID, pistas, cabeceras PES, continuidad, tamaños y tiempos. El TXT no incluye URLs, cabeceras de red, claves, IV ni contenido de audio o vídeo.");
        r0.addView(r3);
        CheckBox r5 = new CheckBox(r4);
        r5.setText("Guardar muestras cifradas para analizar el fallo (opcional)");
        r5.setChecked(capture);
        r0.addView(r5);
        r5.setOnCheckedChangeListener(new HlsDiagnostics225$$ExternalSyntheticLambda4());
        TextView r6 = new TextView(r4);
        r6.setText("Guarda localmente una muestra correcta y la última fallida: hasta 8 MB cada una. No añade claves, IV, URLs ni cabeceras de red al ZIP. Las muestras contienen datos del canal. Se comparten solo cuando tú pulses el botón y elijas una aplicación.");
        r0.addView(r6);
        final Button r7 = new Button(r4);
        r7.setAllCaps(false);
        r7.setText("Compartir muestras HLS (ZIP)");
        r0.addView(r7);
        r7.setOnClickListener(new HlsDiagnostics225$$ExternalSyntheticLambda5(r4, r7));
        Button r8 = new Button(r4);
        r8.setAllCaps(false);
        r8.setText("Borrar muestras HLS");
        r0.addView(r8);
        r8.setOnClickListener(new HlsDiagnostics225$$ExternalSyntheticLambda6(r4));
    }

    static /* synthetic */ boolean lambda$clearSamples$4(File r0, String r1) {
        if (r1.startsWith("CHDTV_hls_diagnostico_") == true) goto L5;
    L7:
        return false;
    L5:
        if (r1.endsWith(".zip") == false) goto L7;
        return true;
    }

    static /* synthetic */ boolean lambda$exportSamples$5(File r0, String r1) {
        if (r1.startsWith("CHDTV_hls_diagnostico_") == true) goto L5;
    L7:
        return false;
    L5:
        if (r1.endsWith(".zip") == false) goto L7;
        return true;
    }

    static /* synthetic */ void lambda$exportSamples$6(Activity r1, Button r2, File r3, String r4) {
        if (r1.isDestroyed() == false) goto L5;
        return;
    L5:
        r2.setEnabled(true);
        if (r3 != null) goto L14;
        Toast.makeText(r1, r4, 1).show();
        return;
    L14:
        Uri r5 = FileProvider.getUriForFile(r1, r1.getPackageName() + ".backups", r3);     // Catch: RuntimeException -> L11
        Intent r6 = new Intent("android.intent.action.SEND").setType("application/zip").putExtra("android.intent.extra.STREAM", r5).addFlags(1);     // Catch: RuntimeException -> L11
        r6.setClipData(ClipData.newRawUri("Muestras HLS", r5));     // Catch: RuntimeException -> L11
        r1.startActivity(Intent.createChooser(r6, "Compartir muestras HLS"));     // Catch: RuntimeException -> L11
        return;
    L12:
        Toast.makeText(r1, "No se pudo abrir el selector para compartir.", 1).show();
    }

    static /* synthetic */ void lambda$exportSamples$7(final Activity r14, final Button r15) {
        File r0 = null;
        final String r1 = null;
        r0 = null;
        Object r2 = files;     // Catch: IOException -> L62
        monitor-enter(r2);     // Catch: IOException -> L62
        File r3 = folder();     // Catch: Throwable -> L59
        if (new File(r3, "last-failure.ts").isFile() == false) goto L58;
        File r4 = new File(app.getCacheDir(), "shared-backups");     // Catch: Throwable -> L59
        if (r4.isDirectory() == false) goto L10;
    L14:
        File[] r5 = r4.listFiles(new HlsDiagnostics225$$ExternalSyntheticLambda0());     // Catch: Throwable -> L59
        if (r5 == null) goto L19;
        int r6 = r5.length;     // Catch: Throwable -> L59
        int r7 = 0;
    L17:
        if (r7 >= r6) goto L19;
        r5[r7].delete();     // Catch: Throwable -> L59
        r7 = r7 + 1;     // Catch: Throwable -> L59
    L19:
        final File r8 = new File(r4, "CHDTV_hls_diagnostico_" + System.currentTimeMillis() + ".zip");     // Catch: Throwable -> L59
        ZipOutputStream r9 = new ZipOutputStream(new FileOutputStream(r8));     // Catch: Throwable -> L59
        String[] r10 = {"last-success.ts", "last-success.txt", "last-failure.ts", "last-failure.txt"};     // Catch: Throwable -> L51
        int r11 = 0;
    L22:
        if (r11 >= 4) goto L45;
        String r12 = r10[r11];     // Catch: Throwable -> L51
        File r13 = new File(r3, r12);     // Catch: Throwable -> L51
        if (r13.isFile() == true) goto L27;
    L37:
        r11 = r11 + 1;
        goto L22
    L27:
        if (r13.length() > 8388608) goto L87;
        r9.putNextEntry(new ZipEntry(r12));     // Catch: Throwable -> L51
        FileInputStream r16 = new FileInputStream(r13);     // Catch: Throwable -> L51
        byte[] r17 = new byte[32768];     // Catch: Throwable -> L38
    L31:
        int r18 = r16.read(r17);     // Catch: Throwable -> L38
        if (r18 == (-1)) goto L35;
        r9.write(r17, 0, r18);     // Catch: Throwable -> L38
    L35:
        r16.close();     // Catch: Throwable -> L51
        r9.closeEntry();     // Catch: Throwable -> L51
    L38:
        th = move-exception;
        r16.close();     // Catch: Throwable -> L41
    L88:
        throw th;     // Catch: Throwable -> L51
    L41:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L51
        throw th;     // Catch: Throwable -> L51
    L87:
        throw new IOException();     // Catch: Throwable -> L51
    L45:
        r9.putNextEntry(new ZipEntry("LEEME.txt"));     // Catch: Throwable -> L51
        r9.write("Diagnostico HLS local. Muestras de entrada cifradas y metadatos estructurales. No se adjuntan claves, IV, URLs ni cabeceras HTTP. No se ha enviado automaticamente. El TXT de caja negra se exporta con sus botones habituales.\n".getBytes(StandardCharsets.UTF_8));     // Catch: Throwable -> L51
        r9.closeEntry();     // Catch: Throwable -> L51
        r9.close();     // Catch: Throwable -> L59
        monitor-exit(r2);     // Catch: Throwable -> L49
    L68:
        r14.runOnUiThread(new HlsDiagnostics225$$ExternalSyntheticLambda1(r14, r15, r8, r1));
        return;
    L49:
        th = th;
        r0 = r8;
    L60:
        monitor-exit(r2);     // Catch: Throwable -> L59
        throw th;     // Catch: IOException -> L62
    L51:
        th = move-exception;
        r9.close();     // Catch: Throwable -> L54
    L56:
        throw th;     // Catch: Throwable -> L59
    L54:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L59
        goto L56
    L10:
        if (r4.mkdirs() == true) goto L14;
        throw new IOException();     // Catch: Throwable -> L59
    L58:
        throw new IOException("NO_SAMPLE");     // Catch: Throwable -> L59
    L59:
        th = th;
    L62:
        e = move-exception;
        if ("NO_SAMPLE".equals(e.getMessage()) == false) goto L66;
        String r19 = "Activa las muestras, inicia el registro y reproduce el fallo antes de exportar.";
    L67:
        r8 = r0;
        r1 = r19;
        goto L68
    L66:
        r19 = "No se pudieron preparar las muestras HLS.";
        goto L67
    }

    static /* synthetic */ void lambda$installInternal$0(CompoundButton r2, boolean r3) {
        detailed = r3;
        app.getSharedPreferences("hls-diagnostics-225", 0).edit().putBoolean("detailed", r3).apply();
        event("HLS225_OPTION", "detailed=" + r3);
    }

    static /* synthetic */ void lambda$installInternal$1(CompoundButton r2, boolean r3) {
        capture = r3;
        app.getSharedPreferences("hls-diagnostics-225", 0).edit().putBoolean("capture", r3).apply();
        event("HLS225_OPTION", "capture=" + r3);
    }

    static /* synthetic */ void lambda$installInternal$2(Activity r0, Button r1, View r2) {
        exportSamples(r0, r1);
    }

    static /* synthetic */ void lambda$installInternal$3(Activity r1, View r2) {
        clearSamples();
        Toast.makeText(r1, "Muestras HLS borradas", 0).show();
    }

    public static long nextRun() {
        return serial.incrementAndGet();
    }

    public static void result(long r18, long r20, String r22, int r23, long r24, long r26, int r28, String r29, byte[] r30, long r31, long r33, long r35, long r37, long r39, long r41, long r43, int r45, int r46, Throwable r47) {
        if (enabled() == true) goto L5;
        return;
    L5:
        if (r30 != null) goto L85;
        String r13 = "none";
    L8:
        if (r47 != null) goto L10;
        String r14 = "native=ok";
    L11:
        String r16 = r14;
        String r1 = "run=" + r18 + " segment=" + r20 + " track=" + r22 + " sample=" + r13;     // Catch: RuntimeException -> L82
        if (r47 != null) goto L14;
        String r2 = "ok";
    L15:
        if (r47 != null) goto L17;
        String r15 = "none";
    L18:
        String r21 = r1;
        StringBuilder r3 = new StringBuilder().append(r1).append(" media_sequence=").append(r24).append(" discontinuity_sequence=").append(r26).append(" iv_source=");     // Catch: RuntimeException -> L82
        if (r28 != 1) goto L23;
        String r17 = "manifest";
    L29:
        StringBuilder r4 = r3.append(r17).append(" native_build=226 process_64=").append(Process.is64Bit()).append(" stage=").append(r29).append(" outcome=").append(r2).append(" bytes=");     // Catch: RuntimeException -> L82
        if (r30 != null) goto L32;
        int r19 = 0;
    L33:
        StringBuilder r5 = r4.append(r19).append(" range_offset=").append(r31).append(" range_length=").append(r33).append(" requested_position=").append(r35).append(" requested_length=").append(r37).append(" announced=").append(r39).append(" download_ms=").append(r41).append(" decrypt_ms=").append(r43).append(" key_length=").append(r45).append(" iv_length=").append(r46).append(" crypto_first=");     // Catch: RuntimeException -> L82
        if ((r23 & 1) == 0) goto L36;
        boolean r11 = true;
    L37:
        StringBuilder r6 = r5.append(r11).append(" key_changed=");     // Catch: RuntimeException -> L82
        if ((r23 & 2) == 0) goto L40;
        boolean r12 = true;
    L41:
        StringBuilder r7 = r6.append(r12).append(" iv_changed=");     // Catch: RuntimeException -> L82
        if ((r23 & 4) == 0) goto L44;
        boolean r110 = true;
    L45:
        StringBuilder r8 = r7.append(r110).append(" error_category=").append(r15).append(" ");     // Catch: RuntimeException -> L82
        String r111 = r16;
        event("HLS225_SEGMENT", r8.append(r111).toString());     // Catch: RuntimeException -> L82
        if (r30 != null) goto L48;
        List<String> r9 = Collections.emptyList();     // Catch: RuntimeException -> L82
    L49:
        if (r47 == null) goto L51;
        boolean r112 = true;
    L52:
        Set<Long> r113 = summarized;     // Catch: RuntimeException -> L82
        monitor-enter(r113);     // Catch: RuntimeException -> L82
    L79:
        th = move-exception;
        throw th;     // Catch: RuntimeException -> L82
    L55:
        if (summarized.size() <= 128) goto L58;
        summarized.clear();     // Catch: Throwable -> L79
    L58:
        if (summarized.add(Long.valueOf(r18)) == false) goto L60;
        r112 = true;
    L60:
        monitor-exit(r113);     // Catch: Throwable -> L79
        if (r112 == false) goto L66;
        int r114 = 0;
    L64:
        if (r114 >= r9.size()) goto L66;
        String r115 = r21;
        event("HLS225_STRUCTURE", r115 + " row=" + r114 + " " + r9.get(r114));     // Catch: RuntimeException -> L82
        r114 = r114 + 1;     // Catch: RuntimeException -> L82
        r21 = r115;
        r111 = r111;
    L66:
        String r116 = r21;
        String r117 = r111;
        if (capture == false) goto L90;
        if (r30 == null) goto L91;
        if (r47 != null) goto L71;
    L74:
        if (r47 != null) goto L76;
        boolean r118 = true;
    L77:
        saveSample(r18, r118, r30, r116 + " media_sequence=" + r24 + " discontinuity_sequence=" + r26 + " iv_source=" + r28 + " range_offset=" + r31 + " range_length=" + r33 + " outcome=" + r2 + " stage=" + r29 + " " + r117, r9);     // Catch: RuntimeException -> L82
        return;
    L76:
        r118 = false;
        goto L77
    L71:
        if (r29.equals("DECRYPT") == true) goto L74;
        if (r29.equals("VALIDATE") == true) goto L74;
        return;
    L91:
        return;
    L90:
        return;
    L83:
        event("HLS225_DIAGNOSTIC", "run=" + r18 + " status=unavailable");
        return;
    L51:
        r112 = false;
        goto L52
    L48:
        r9 = HlsTsReport225.rows(r30, r111);     // Catch: RuntimeException -> L82
        goto L49
    L44:
        r110 = false;
        goto L45
    L40:
        r12 = false;
        goto L41
    L36:
        r11 = false;
        goto L37
    L32:
        r19 = r30.length;     // Catch: RuntimeException -> L82
        goto L33
    L23:
        if (r28 != 2) goto L26;
        r17 = "provider";
        goto L29
    L26:
        if (r28 != 3) goto L28;
        r17 = "sequence";
        goto L29
    L28:
        r17 = EnvironmentCompat.MEDIA_UNKNOWN;
        goto L29
    L17:
        r15 = category(r47);     // Catch: RuntimeException -> L82
        goto L18
    L14:
        r2 = "failed";
        goto L15
    L10:
        r14 = safeCause(r47);     // Catch: RuntimeException -> L82
        goto L11
    L85:
        r13 = HlsTsReport225.fingerprint(r30);     // Catch: RuntimeException -> L82
        goto L8
    }

    public static String safeCause(Throwable r0) {
        return HlsTsReport225.cause(r0);
    }

    private static void saveSample(long r10, boolean r12, byte[] r13, String r14, List<String> r15) {
        Object r0 = files;
        monitor-enter(r0);
        String r1 = HlsTsReport225.fingerprint(r13);     // Catch: Throwable -> L75
        if (r12 == true) goto L7;
    L8:
        if (r12 == true) goto L14;
        if (lastFailed.equals(r1) == false) goto L14;
    L11:
        monitor-exit(r0);     // Catch: Throwable -> L75
        return;
    L14:
        if (r13.length <= 8388608) goto L18;
        event("HLS225_SAMPLE", "run=" + r10 + " status=omitted_size_limit bytes=" + r13.length);     // Catch: Throwable -> L75
        monitor-exit(r0);     // Catch: Throwable -> L75
        return;
    L18:
        File r2 = folder();     // Catch: Throwable -> L75
        if (r2.isDirectory() == false) goto L21;
    L25:
        if (r12 == false) goto L27;
        String r3 = "last-success";
    L28:
        File r4 = new File(r2, r3 + ".ts");     // Catch: Throwable -> L75
        File r5 = new File(r2, r3 + ".tmp");     // Catch: Throwable -> L75
        File r6 = new File(r2, r3 + ".txt");     // Catch: Throwable -> L75
    L66:
        th = move-exception;
        r5.delete();     // Catch: Throwable -> L75
        throw th;     // Catch: Throwable -> L75
    L30:
        if (r4.exists() == true) goto L32;
    L36:
        FileOutputStream r7 = new FileOutputStream(r5);     // Catch: Throwable -> L66 IOException -> L68
        r7.write(r13);     // Catch: Throwable -> L60
        r7.close();     // Catch: Throwable -> L66 IOException -> L68
        if (r5.renameTo(r4) == false) goto L59;
        OutputStreamWriter r8 = new OutputStreamWriter(new FileOutputStream(r6), StandardCharsets.UTF_8);     // Catch: Throwable -> L66 IOException -> L68
        r8.write("native_build=226 captured_wall_ms=" + System.currentTimeMillis() + "\n" + r14 + "\n");     // Catch: Throwable -> L52
        Iterator<String> r16 = r15.iterator();     // Catch: Throwable -> L52
    L43:
        if (r16.hasNext() == false) goto L46;
        r8.write(r16.next() + "\n");     // Catch: Throwable -> L52
        goto L43
    L46:
        r8.close();     // Catch: Throwable -> L66 IOException -> L68
        if (r12 == false) goto L49;
        goodRun = r10;     // Catch: Throwable -> L66 IOException -> L68
    L50:
        event("HLS225_SAMPLE", "run=" + r10 + " sample=" + r1 + " role=" + r3 + " status=saved bytes=" + r13.length);     // Catch: Throwable -> L66 IOException -> L68
    L70:
        r5.delete();     // Catch: Throwable -> L75
        monitor-exit(r0);     // Catch: Throwable -> L75
        return;
    L49:
        lastFailed = r1;     // Catch: Throwable -> L66 IOException -> L68
    L52:
        th = move-exception;
        r8.close();     // Catch: Throwable -> L55
    L57:
        throw th;     // Catch: Throwable -> L66 IOException -> L68
    L55:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L66 IOException -> L68
        goto L57
    L59:
        throw new IOException();     // Catch: Throwable -> L66 IOException -> L68
    L60:
        th = move-exception;
        r7.close();     // Catch: Throwable -> L63
    L65:
        throw th;     // Catch: Throwable -> L66 IOException -> L68
    L63:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L66 IOException -> L68
        goto L65
    L32:
        if (r4.delete() == true) goto L36;
        throw new IOException();     // Catch: Throwable -> L66 IOException -> L68
    L69:
        r4.delete();     // Catch: Throwable -> L66
        r6.delete();     // Catch: Throwable -> L66
        event("HLS225_SAMPLE", "run=" + r10 + " role=" + r3 + " status=storage_unavailable");     // Catch: Throwable -> L66
        goto L70
    L27:
        r3 = "last-failure";
        goto L28
    L21:
        if (r2.mkdirs() == true) goto L25;
        event("HLS225_SAMPLE", "status=storage_unavailable");     // Catch: Throwable -> L75
        monitor-exit(r0);     // Catch: Throwable -> L75
        return;
    L7:
        if (goodRun == r10) goto L11;
    L75:
        th = move-exception;
        throw th;
    }

    public static String track(String r4) {
        URI r0 = new URI(r4);     // Catch: Exception -> L12
        String r5 = r0.getPath();     // Catch: Exception -> L12
        String r1 = "";
        if (r5 != null) goto L7;
        r5 = "";
    L7:
        int r2 = r5.lastIndexOf(47);     // Catch: Exception -> L12
        StringBuilder r3 = new StringBuilder().append(r0.getHost()).append("/");     // Catch: Exception -> L12
        if (r2 < 0) goto L10;
        r1 = r5.substring(0, r2);     // Catch: Exception -> L12
    L10:
        return HlsTsReport225.fingerprint(r3.append(r1).toString().getBytes(StandardCharsets.UTF_8));
    L13:
        return EnvironmentCompat.MEDIA_UNKNOWN;
    }
}
