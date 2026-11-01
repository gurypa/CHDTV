package es.antonio.m3u;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import androidx.media3.common.C0180C;
import com.google.common.net.HttpHeaders;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.net.ssl.SSLException;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class AppUpdates {
    private static final long INTERVAL = 21600000;

    /* JADX INFO: renamed from: IO */
    private static final ExecutorService f171IO = null;
    private static final long MAX_APK = 157286400;
    private static final AtomicBoolean checking = null;
    private static final AtomicBoolean downloading = null;

    static {
        f171IO = Executors.newSingleThreadExecutor();
        checking = new AtomicBoolean();
        downloading = new AtomicBoolean();
    }

    AppUpdates() {
    }

    private static boolean alive(Activity r1) {
        if (r1.isFinishing() == false) goto L5;
    L7:
        return false;
    L5:
        if (r1.isDestroyed() == true) goto L7;
        return true;
    }

    static void check(Activity r4, final boolean r5) {
        if (checking.compareAndSet(false, true) == true) goto L7;
        if (r5 == false) goto L12;
        Toast.makeText(r4, "Buscando actualizaciones…", 0).show();
        return;
    L12:
        return;
    L7:
        final Context r0 = r4.getApplicationContext();
        final WeakReference r3 = new WeakReference(r4);
        if (r5 == false) goto L10;
        Toast.makeText(r4, "Buscando actualizaciones…", 0).show();
    L10:
        f171IO.execute(new AppUpdates$$ExternalSyntheticLambda12(r0, r3, r5));
    }

    static String checkError(Exception r3) {
        if ((r3 instanceof UnknownHostException) == false) goto L7;
        return "No se pudo comprobar la actualización. No se pudo resolver el servidor. Revisa la conexión o el DNS.";
    L7:
        if ((r3 instanceof SocketTimeoutException) == false) goto L11;
        return "No se pudo comprobar la actualización. El servidor tardó demasiado en responder. Vuelve a intentarlo.";
    L11:
        if ((r3 instanceof SSLException) == false) goto L15;
        return "No se pudo comprobar la actualización. No se pudo establecer la conexión segura. Revisa la fecha del dispositivo.";
    L15:
        if ((r3.getCause() instanceof JSONException) == false) goto L19;
        return "No se pudo comprobar la actualización. El enlace no devuelve un archivo de versiones válido.";
    L19:
        return "No se pudo comprobar la actualización. Revisa el enlace y la conexión. Código: " + r3.getClass().getSimpleName();
    }

    static long contentLength(URLConnection r5) {
        String r2 = r5.getHeaderField(HttpHeaders.CONTENT_LENGTH);     // Catch: NumberFormatException -> L8
        if (r2 != null) goto L6;
        return -1;
    L6:
        return Math.max(-1, Long.parseLong(r2));
    L9:
        return -1;
    }

    private static void download(Activity r12, final UpdateManifest r13) {
        if (downloading.compareAndSet(false, true) == true) goto L6;
        tell(r12, "Ya hay una descarga en curso.");
        return;
    L6:
        final ProgressBar r0 = new ProgressBar(r12, null, android.R.attr.progressBarStyleHorizontal);
        r0.setMax(100);
        r0.setIndeterminate(true);
        final AtomicBoolean r8 = new AtomicBoolean();
        final AlertDialog r10 = new AlertDialog.Builder(r12).setTitle("Descargando actualización…").setView(r0).setNegativeButton("Cancelar", new AppUpdates$$ExternalSyntheticLambda5(r8)).create();
        r10.setCanceledOnTouchOutside(false);
        r10.setOnCancelListener(new AppUpdates$$ExternalSyntheticLambda6(r8));
        r10.show();
        final Context r6 = r12.getApplicationContext();
        final WeakReference r9 = new WeakReference(r12);
        f171IO.execute(new AppUpdates$$ExternalSyntheticLambda7(r6, r13, r8, r9, r10, r0));
    }

    static String hex(byte[] r8) {
        StringBuilder r0 = new StringBuilder();
        int r1 = r8.length;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L6;
        r0.append(String.format(Locale.ROOT, "%02x", new Object[]{Integer.valueOf(r8[r3] & 255)}));
        r3 = r3 + 1;
        goto L3
    L6:
        return r0.toString();
    }

    static void install(final Activity r4) {
        final File r0 = new File(r4.getCacheDir(), "updates/CHDTV-update.apk");
        final String r1 = prefs(r4).getString("pending", "");
        if (r0.isFile() == true) goto L5;
    L9:
        tell(r4, "No hay una actualización descargada.");
        return;
    L5:
        if (r1.isEmpty() == true) goto L9;
        f171IO.execute(new AppUpdates$$ExternalSyntheticLambda4(r1, r4, r0));
    }

    static /* synthetic */ void lambda$check$1(Activity r1) {
        tell(r1, "Todavía no hay un enlace de actualizaciones configurado.");
    }

    static /* synthetic */ void lambda$check$2(Activity r1, UpdateManifest r2, long r3, boolean r5, boolean r6) {
        if (alive(r1) == true) goto L6;
        return;
    L6:
        if (r2.newerThan(r3) == false) goto L10;
        if (r5 == true) goto L9;
        if (r6 == true) goto L10;
    L9:
        offer(r1, r2);
        return;
    L10:
        if (r5 == false) goto L14;
        tell(r1, "Ya tienes la última versión disponible.");
        return;
    }

    static /* synthetic */ void lambda$check$3(Activity r1, Exception r2) {
        tell(r1, checkError(r2));
    }

    static /* synthetic */ void lambda$check$4(Context r13, WeakReference r14, boolean r15) {
        SharedPreferences r0 = prefs(r13);     // Catch: Throwable -> L43 Exception -> L45
        String r2 = r0.getString("feed", BuildConfig.UPDATE_FEED_URL);     // Catch: Throwable -> L43 Exception -> L45
        if (r2.isEmpty() == false) goto L16;
        final Activity r3 = (Activity) r14.get();     // Catch: Throwable -> L11 Exception -> L13
        if (r15 == false) goto L9;
        if (r3 == null) goto L9;
        r3.runOnUiThread(new AppUpdates$$ExternalSyntheticLambda13(r3));     // Catch: Throwable -> L11 Exception -> L13
    L9:
        checking.set(false);
        return;
    L16:
        if (r15 == false) goto L18;
    L21:
        final UpdateManifest r4 = UpdateManifest.parse(new String(read(r2, 65536), StandardCharsets.UTF_8), r13.getPackageName(), Build.VERSION.SDK_INT);     // Catch: Throwable -> L43 Exception -> L45
        PackageInfo r5 = r13.getPackageManager().getPackageInfo(r13.getPackageName(), 0);     // Catch: Throwable -> L43 Exception -> L45
        if (Build.VERSION.SDK_INT < 28) goto L26;
        long r6 = r5.getLongVersionCode();     // Catch: Throwable -> L11 Exception -> L13
    L27:
        final long r8 = r6;
        r0.edit().putLong("lastCheck", System.currentTimeMillis()).apply();     // Catch: Throwable -> L43 Exception -> L45
        if (r0.getInt("deferred", 0) == r4.versionCode) goto L31;
    L33:
        final boolean r11 = false;
    L34:
        final Activity r7 = (Activity) r14.get();     // Catch: Throwable -> L43 Exception -> L45
        if (r7 == null) goto L51;
        final boolean r10 = r15;
        r7.runOnUiThread(new AppUpdates$$ExternalSyntheticLambda14(r7, r4, r8, r10, r11));     // Catch: Exception -> L40 Throwable -> L53
    L40:
        e = e;
    L47:
        final Activity r16 = (Activity) r14.get();     // Catch: Throwable -> L53
        if (r10 == false) goto L51;
        if (r16 == null) goto L51;
        r16.runOnUiThread(new AppUpdates$$ExternalSyntheticLambda15(r16, e));     // Catch: Throwable -> L53
    L53:
        th = th;
    L54:
        checking.set(false);
        throw th;
    L51:
        checking.set(false);
        return;
    L31:
        if (System.currentTimeMillis() >= r0.getLong("deferredUntil", 0)) goto L33;
        r11 = true;
        goto L34
    L26:
        r6 = r5.versionCode;     // Catch: Throwable -> L43 Exception -> L45
        goto L27
    L18:
        if (r0.getBoolean("automatic", true) == true) goto L21;
        checking.set(false);
        return;
    L13:
        e = e;
        r10 = r15;
    L11:
        th = th;
    L45:
        e = e;
        r10 = r15;
    L43:
        th = th;
        goto L54
    }

    static /* synthetic */ void lambda$download$10(Activity r1, AlertDialog r2) {
        if (alive(r1) == false) goto L6;
        r2.dismiss();
        install(r1);
        return;
    }

    static /* synthetic */ void lambda$download$11(Activity r1, AlertDialog r2, AtomicBoolean r3) {
        if (alive(r1) == false) goto L8;
        r2.dismiss();
        if (r3.get() == true) goto L9;
        tell(r1, "No se pudo descargar o validar la actualización. Puedes volver a intentarlo desde Ajustes.");
        return;
    L9:
        return;
    }

    static /* synthetic */ void lambda$download$12(Context r24, UpdateManifest r25, final AtomicBoolean r26, WeakReference r27, final AlertDialog r28, final ProgressBar r29) {
        File r0 = new File(r24.getCacheDir(), "updates");
        File r1 = new File(r0, "download.part");
        HttpURLConnection r5 = null;
    L138:
        th = th;
    L158:
        if (r5 == null) goto L160;
        r5.disconnect();
    L160:
        downloading.set(false);
        throw th;
    L4:
        if (r0.isDirectory() == false) goto L181;
    L12:
        r5 = open(r25.apkUrl);     // Catch: Throwable -> L138 Exception -> L141
        long r7 = contentLength(r5);     // Catch: Throwable -> L134 Exception -> L136
        long r9 = MAX_APK;
        if (r7 > MAX_APK) goto L129;
        MessageDigest r2 = MessageDigest.getInstance("SHA-256");     // Catch: Throwable -> L134 Exception -> L136
        long r13 = 0;
        int r15 = -1;
        InputStream r16 = r5.getInputStream();     // Catch: Throwable -> L134 Exception -> L136
        FileOutputStream r17 = new FileOutputStream(r1);     // Catch: Throwable -> L120
        byte[] r3 = new byte[65536];     // Catch: Throwable -> L111
    L22:
        long r18 = r9;
        InputStream r10 = r16;
        int r11 = r10.read(r3);     // Catch: Throwable -> L109
        if (r11 == (-1)) goto L65;
        if (r26.get() == true) goto L203;
        long r22 = r7;
        r13 = r13 + ((long) r11);
        if (r13 > r18) goto L59;
        r17 = r17;
        r17.write(r3, 0, r11);     // Catch: Throwable -> L55
        r2.update(r3, 0, r11);     // Catch: Throwable -> L55
        if (r22 > 0) goto L174;
        r17 = r17;
        int r6 = -1;
    L43:
        if (r6 == r15) goto L54;
        r15 = r6;
        final int r8 = r6;
        final Activity r12 = (Activity) r27.get();     // Catch: Throwable -> L51
        if (r12 == null) goto L54;
        r12.runOnUiThread(new AppUpdates$$ExternalSyntheticLambda9(r12, r28, r29, r8));     // Catch: Throwable -> L62
    L62:
        Throwable th = th;
    L64:
        Throwable r14 = th;
    L170:
        r17.close();     // Catch: Throwable -> L115
    L205:
        throw r14;     // Catch: Throwable -> L118
    L115:
        th = move-exception;
        r14.addSuppressed(th);     // Catch: Throwable -> L118
        throw r14;     // Catch: Throwable -> L118
    L118:
        Throwable th2 = th;
    L108:
        Throwable r19 = th2;
    L122:
        if (r10 == null) goto L206;
        r10.close();     // Catch: Throwable -> L125
        throw r19;     // Catch: Throwable -> L130 Exception -> L132
    L125:
        th = move-exception;
        r19.addSuppressed(th);     // Catch: Throwable -> L130 Exception -> L132
        throw r19;     // Catch: Throwable -> L130 Exception -> L132
    L206:
        throw r19;     // Catch: Throwable -> L130 Exception -> L132
    L51:
        th = th;
    L54:
        r5 = r5;
        r7 = r22;
        r16 = r10;
        r9 = r18;
        goto L22
    L174:
        r17 = r17;
        r6 = (int) Math.min(100, (r13 * 100) / r22);
    L38:
        th = th;
    L40:
        th = th;
    L57:
        r14 = th;
    L55:
        th = th;
        goto L57
    L59:
        throw new IOException("APK demasiado grande");     // Catch: Throwable -> L62
    L203:
        throw new InterruptedIOException();     // Catch: Throwable -> L62
    L65:
        long r23 = r7;
        HttpURLConnection r110 = r5;
        r17.close();     // Catch: Throwable -> L106
        if (r10 == null) goto L74;
        r10.close();     // Catch: Throwable -> L69 Exception -> L71
    L69:
        th = th;
    L131:
        r5 = r110;
    L74:
        if (r23 < 0) goto L81;
        if (r13 == r23) goto L81;
        throw new IOException("Descarga incompleta");     // Catch: Throwable -> L69 Exception -> L71
    L81:
        if (r25.sha256.equals(hex(r2.digest())) == false) goto L105;
        validate(r24, r1, r25);     // Catch: Throwable -> L130 Exception -> L132
        if (r26.get() == true) goto L103;
        File r4 = new File(r0, "CHDTV-update.apk");     // Catch: Throwable -> L130 Exception -> L132
        if (r4.exists() == false) goto L94;
        if (r4.delete() == true) goto L94;
        throw new IOException("No se pudo guardar la actualización");     // Catch: Throwable -> L130 Exception -> L132
    L94:
        if (r1.renameTo(r4) == false) goto L204;
        prefs(r24).edit().putString("pending", releaseJson(r25)).apply();     // Catch: Throwable -> L130 Exception -> L132
        final Activity r20 = (Activity) r27.get();     // Catch: Throwable -> L130 Exception -> L132
        if (r20 == null) goto L98;
        r20.runOnUiThread(new AppUpdates$$ExternalSyntheticLambda10(r20, r28));     // Catch: Throwable -> L130 Exception -> L132
    L98:
        if (r110 == null) goto L100;
        r110.disconnect();
    L100:
        downloading.set(false);
        return;
    L204:
        throw new IOException("No se pudo guardar la actualización");     // Catch: Throwable -> L130 Exception -> L132
    L103:
        throw new InterruptedIOException();     // Catch: Throwable -> L130 Exception -> L132
    L105:
        throw new IOException("La descarga no supera la comprobación de integridad");     // Catch: Throwable -> L130 Exception -> L132
    L106:
        th2 = th;
    L109:
        th = move-exception;
        r14 = th;
    L111:
        th = move-exception;
        r10 = r16;
        r14 = th;
    L120:
        th = move-exception;
        r10 = r16;
        r19 = th;
        goto L122
        goto L164
    L129:
        throw new IOException("APK demasiado grande");     // Catch: Throwable -> L130 Exception -> L132
    L130:
        th = th;
    L133:
        r5 = r110;
    L134:
        th = th;
        goto L158
    L181:
    L10:
        th = th;
        goto L158
    L6:
        if (r0.mkdirs() == true) goto L12;
        throw new IOException("Sin espacio para descargar");     // Catch: Throwable -> L10 Exception -> L141
    L164:
        r1.delete();     // Catch: Throwable -> L156
        final Activity r21 = (Activity) r27.get();     // Catch: Throwable -> L156
        if (r21 == null) goto L152;
        r21.runOnUiThread(new AppUpdates$$ExternalSyntheticLambda11(r21, r28, r26));     // Catch: Throwable -> L149
    L149:
        th = th;
    L152:
        if (r5 == null) goto L154;
        r5.disconnect();
    L154:
        downloading.set(false);
        return;
    L156:
        th = th;
        goto L158
    }

    static /* synthetic */ void lambda$download$7(AtomicBoolean r1, DialogInterface r2, int r3) {
        r1.set(true);
    }

    static /* synthetic */ void lambda$download$8(AtomicBoolean r1, DialogInterface r2) {
        r1.set(true);
    }

    static /* synthetic */ void lambda$download$9(Activity r1, AlertDialog r2, ProgressBar r3, int r4) {
        if (alive(r1) == true) goto L5;
        return;
    L5:
        if (r2.isShowing() == false) goto L14;
        if (r4 >= 0) goto L8;
        boolean r0 = true;
    L9:
        r3.setIndeterminate(r0);
        if (r4 < 0) goto L15;
        r3.setProgress(r4);
        return;
    L15:
        return;
    L8:
        r0 = false;
        goto L9
    }

    static /* synthetic */ void lambda$install$13(Activity r4, DialogInterface r5, int r6) {
        r4.startActivity(new Intent("android.settings.MANAGE_UNKNOWN_APP_SOURCES", Uri.parse("package:" + r4.getPackageName())));     // Catch: ActivityNotFoundException -> L4
        return;
    L5:
        tell(r4, "Este dispositivo no ofrece el permiso de instalación.");
    }

    static /* synthetic */ void lambda$install$14(final Activity r3, File r4) {
        if (alive(r3) == true) goto L16;
        return;
    L16:
        if (Build.VERSION.SDK_INT >= 26) goto L8;
    L11:
        Uri r0 = FileProvider.getUriForFile(r3, r3.getPackageName() + ".backups", r4);     // Catch: Exception -> L13
        Intent r1 = new Intent("android.intent.action.VIEW").setDataAndType(r0, "application/vnd.android.package-archive").addFlags(1);     // Catch: Exception -> L13
        r1.setClipData(ClipData.newRawUri("CHDTV", r0));     // Catch: Exception -> L13
        r3.startActivity(r1);     // Catch: Exception -> L13
        return;
    L8:
        if (r3.getPackageManager().canRequestPackageInstalls() == true) goto L11;
        new AlertDialog.Builder(r3).setTitle("Permitir actualización").setMessage("Activa el permiso para instalar desde CHDTV. Después vuelve a Actualizaciones y pulsa Instalar descarga.").setNegativeButton("Más tarde", null).setPositiveButton("Abrir permiso", new AppUpdates$$ExternalSyntheticLambda1(r3)).show();     // Catch: Exception -> L13
        return;
    L14:
        tell(r3, "Este dispositivo no ha podido abrir el instalador.");
    }

    static /* synthetic */ void lambda$install$15(Activity r1) {
        tell(r1, "La APK guardada no es una actualización válida. Descárgala de nuevo.");
    }

    static /* synthetic */ void lambda$install$16(String r7, final Activity r8, final File r9) {
        UpdateManifest r0 = UpdateManifest.parse(r7, r8.getPackageName(), Build.VERSION.SDK_INT);     // Catch: Exception -> L20
        MessageDigest r1 = MessageDigest.getInstance("SHA-256");     // Catch: Exception -> L20
        FileInputStream r2 = new FileInputStream(r9);     // Catch: Exception -> L20
        byte[] r3 = new byte[65536];     // Catch: Throwable -> L14
    L5:
        int r4 = r2.read(r3);     // Catch: Throwable -> L14
        if (r4 == (-1)) goto L9;
        r1.update(r3, 0, r4);     // Catch: Throwable -> L14
        goto L21
    L9:
        r2.close();     // Catch: Exception -> L20
        if (r0.sha256.equals(hex(r1.digest())) == false) goto L13;
        validate(r8, r9, r0);     // Catch: Exception -> L20
        r8.runOnUiThread(new AppUpdates$$ExternalSyntheticLambda2(r8, r9));     // Catch: Exception -> L20
        return;
    L13:
        throw new IOException();     // Catch: Exception -> L20
    L14:
        th = move-exception;
        r2.close();     // Catch: Throwable -> L17
    L19:
        throw th;     // Catch: Exception -> L20
    L17:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Exception -> L20
    L21:
        r8.runOnUiThread(new AppUpdates$$ExternalSyntheticLambda3(r8));
    }

    static /* synthetic */ void lambda$offer$5(Activity r0, UpdateManifest r1, DialogInterface r2, int r3) {
        download(r0, r1);
    }

    static /* synthetic */ void lambda$offer$6(Activity r5, UpdateManifest r6, DialogInterface r7, int r8) {
        prefs(r5).edit().putInt("deferred", r6.versionCode).putLong("deferredUntil", System.currentTimeMillis() + 86400000).apply();
    }

    static /* synthetic */ void lambda$settings$0(EditText r4, Activity r5, CheckBox r6, AlertDialog r7, View r8) {
        String r0 = r4.getText().toString().trim();
    L8:
        e = move-exception;
        r4.setError(e.getMessage());
        return;
    L4:
        if (r0.isEmpty() == true) goto L6;
        UpdateManifest.https(r0);     // Catch: IOException -> L8
    L6:
        prefs(r5).edit().putString("feed", r0).putBoolean("automatic", r6.isChecked()).remove("lastCheck").apply();     // Catch: IOException -> L8
        r7.dismiss();     // Catch: IOException -> L8
    }

    private static void offer(final Activity r4, final UpdateManifest r5) {
        AlertDialog.Builder r0 = new AlertDialog.Builder(r4).setTitle("CHDTV " + r5.versionName);
        StringBuilder r1 = new StringBuilder().append("Hay una actualización disponible.");
        if (r5.notes.isEmpty() == false) goto L5;
        String r2 = "";
    L6:
        r0.setMessage(r1.append(r2).toString()).setPositiveButton("Actualizar", new AppUpdates$$ExternalSyntheticLambda0(r4, r5)).setNegativeButton("Más tarde", new AppUpdates$$ExternalSyntheticLambda8(r4, r5)).show();
        return;
    L5:
        r2 = "\n\n" + r5.notes;
        goto L6
    }

    static HttpURLConnection open(String r7) throws IOException {
        String r0 = UpdateManifest.downloadUrl(r7);
        int r1 = 0;
    L4:
        if (r1 > 5) goto L32;
        HttpURLConnection r2 = (HttpURLConnection) new URL(r0).openConnection();
        r2.setInstanceFollowRedirects(false);
        r2.setConnectTimeout(8000);
        r2.setReadTimeout(15000);
        r2.setUseCaches(false);
        r2.setRequestProperty(HttpHeaders.CACHE_CONTROL, "no-cache");
        r2.setRequestProperty(HttpHeaders.USER_AGENT, "CHDTV-update/169");
        r2.setRequestProperty(HttpHeaders.ACCEPT_ENCODING, "identity");
        int r3 = r2.getResponseCode();     // Catch: IOException -> L28
        if (r3 != 301) goto L10;
    L23:
        String r4 = r2.getHeaderField(HttpHeaders.LOCATION);
        r2.disconnect();
        if (r4 == null) goto L27;
        r0 = UpdateManifest.https(new URL(new URL(r0), r4).toString());
        r1 = r1 + 1;
        goto L4
    L27:
        throw new IOException("Redirección sin enlace");
    L10:
        if (r3 == 302) goto L23;
        if (r3 == 303) goto L23;
        if (r3 == 307) goto L23;
        if (r3 == 308) goto L23;
        if (r3 != 200) goto L21;
        return r2;
    L21:
        r2.disconnect();
        throw new IOException("No se pudo descargar la actualización");
    L28:
        e = move-exception;
        r2.disconnect();
        throw e;
    L32:
        throw new IOException("Demasiadas redirecciones");
    }

    private static SharedPreferences prefs(Context r2) {
        return r2.getSharedPreferences("app-updates", 0);
    }

    static byte[] read(String r8, int r9) throws IOException {
        HttpURLConnection r0 = open(r8);
        InputStream r1 = r0.getInputStream();     // Catch: Throwable -> L38
        ByteArrayOutputStream r2 = new ByteArrayOutputStream();     // Catch: Throwable -> L31
    L25:
        th = move-exception;
        r2.close();     // Catch: Throwable -> L28
    L30:
        throw th;     // Catch: Throwable -> L31
    L28:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L31
        goto L30
    L7:
        if (contentLength(r0) > r9) goto L24;
        byte[] r3 = new byte[8192];     // Catch: Throwable -> L25
    L10:
        int r5 = r1.read(r3);     // Catch: Throwable -> L25
        if (r5 == (-1)) goto L17;
        if ((r2.size() + r5) > r9) goto L16;
        r2.write(r3, 0, r5);     // Catch: Throwable -> L25
        goto L10
    L16:
        throw new IOException("Información demasiado grande");     // Catch: Throwable -> L25
    L17:
        byte[] r4 = r2.toByteArray();     // Catch: Throwable -> L25
        r2.close();     // Catch: Throwable -> L31
        if (r1 == null) goto L21;
        r1.close();     // Catch: Throwable -> L38
    L21:
        r0.disconnect();
        return r4;
    L24:
        throw new IOException("Información demasiado grande");     // Catch: Throwable -> L25
    L31:
        th = move-exception;
        if (r1 != null) goto L46;
    L37:
        throw th;     // Catch: Throwable -> L38
    L46:
        r1.close();     // Catch: Throwable -> L35
    L35:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L38
    L38:
        th = move-exception;
        r0.disconnect();
        throw th;
    }

    private static String releaseJson(UpdateManifest r3) throws JSONException {
        return new JSONObject().put("versionCode", r3.versionCode).put("versionName", r3.versionName).put("packageName", r3.packageName).put("apkUrl", r3.apkUrl).put("sha256", r3.sha256).toString();
    }

    static boolean sameSigners(Signature[] r3, Signature[] r4) {
        if (r3 == null) goto L11;
        if (r4 != null) goto L5;
        return false;
    L5:
        if (r3.length != 0) goto L7;
        return false;
    L7:
        if (r3.length == r4.length) goto L10;
        return false;
    L10:
        return new HashSet(Arrays.asList(r3)).equals(new HashSet(Arrays.asList(r4)));
    L11:
        return false;
    }

    static void settings(final Activity r6) {
        final EditText r0 = new EditText(r6);
        r0.setSingleLine(true);
        r0.setInputType(17);
        r0.setText(prefs(r6).getString("feed", BuildConfig.UPDATE_FEED_URL));
        r0.setHint("Enlace de actualizaciones");
        final CheckBox r2 = new CheckBox(r6);
        r2.setText("Buscar actualizaciones al iniciar");
        r2.setChecked(prefs(r6).getBoolean("automatic", true));
        LinearLayout r3 = new LinearLayout(r6);
        r3.setOrientation(1);
        r3.setPadding(24, 16, 24, 16);
        r3.addView(r0);
        r3.addView(r2);
        final AlertDialog r1 = new AlertDialog.Builder(r6).setTitle("Actualizaciones").setView(r3).setNegativeButton("Cancelar", null).setPositiveButton("Guardar", null).create();
        r1.show();
        r1.getButton(-1).setOnClickListener(new AppUpdates$$ExternalSyntheticLambda16(r0, r6, r2, r1));
    }

    static void startup(Activity r1) {
        check(r1, false);
    }

    private static void tell(Activity r3, String r4) {
        if (alive(r3) == false) goto L6;
        new AlertDialog.Builder(r3).setTitle("Actualizaciones CHDTV").setMessage(r4).setPositiveButton("Aceptar", null).show();
        return;
    }

    static void validate(Context r12, File r13, UpdateManifest r14) throws Exception {
        PackageManager r0 = r12.getPackageManager();
        if (Build.VERSION.SDK_INT < 28) goto L5;
        int r1 = C0180C.BUFFER_FLAG_FIRST_SAMPLE;
    L6:
        PackageInfo r3 = r0.getPackageArchiveInfo(r13.getAbsolutePath(), r1);
        PackageInfo r4 = r0.getPackageInfo(r12.getPackageName(), r1);
        if (r3 == null) goto L42;
        if (r12.getPackageName().equals(r3.packageName) == false) goto L42;
        if (Build.VERSION.SDK_INT < 28) goto L13;
        long r5 = r3.getLongVersionCode();
    L15:
        if (Build.VERSION.SDK_INT < 28) goto L17;
        long r7 = r4.getLongVersionCode();
    L19:
        if (r5 != r14.versionCode) goto L40;
        if (r5 <= r7) goto L40;
        if (Build.VERSION.SDK_INT >= 28) goto L25;
    L27:
        Signature[] r9 = r3.signatures;
    L29:
        if (Build.VERSION.SDK_INT >= 28) goto L31;
    L33:
        Signature[] r2 = r4.signatures;
    L35:
        if (sameSigners(r2, r9) == false) goto L38;
        return;
    L38:
        throw new IOException("La firma no corresponde a CHDTV");
    L31:
        if (r4.signingInfo == null) goto L33;
        r2 = r4.signingInfo.getApkContentsSigners();
        goto L35
    L25:
        if (r3.signingInfo == null) goto L27;
        r9 = r3.signingInfo.getApkContentsSigners();
    L40:
        throw new IOException("Versión de APK incorrecta");
    L17:
        r7 = r4.versionCode;
        goto L19
    L13:
        r5 = r3.versionCode;
    L42:
        throw new IOException("APK de otra aplicación");
    L5:
        r1 = 64;
        goto L6
    }
}
