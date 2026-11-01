package es.antonio.m3u;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.SystemClock;
import androidx.media3.common.PlaybackException;
import com.google.common.net.HttpHeaders;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

/* JADX INFO: loaded from: classes3.dex */
final class EpgRepository {
    static final String DEFAULT_URL = "https://raw.githubusercontent.com/davidmuma/EPG_dobleM/master/guiaiptv.xml";
    private static final Object DOWNLOAD_LOCK = null;
    private final Context context;
    final SharedPreferences settings;
    private final AppStorage storage;

    interface Source {
        InputStream open(String r1, String r2) throws Exception;
    }

    /* JADX INFO: renamed from: $r8$lambda$opStuCrrU-Hl5zg-xCcffwwTujg, reason: not valid java name */
    public static /* synthetic */ InputStream m582$r8$lambda$opStuCrrUHl5zgxCcffwwTujg(EpgRepository r0, String r1, String r2) {
        return r0.open(r1, r2);
    }

    static {
        DOWNLOAD_LOCK = new Object();
    }

    EpgRepository(Context r8) {
        this.context = r8.getApplicationContext();
        this.storage = new AppStorage(this.context);
        this.settings = this.context.getSharedPreferences("epg_settings", 0);
        SharedPreferences r0 = this.settings;
        monitor-enter(r0);
    L19:
        th = move-exception;
        throw th;
    L5:
        if (this.settings.contains("configured") == true) goto L17;
        SharedPreferences r1 = this.context.getSharedPreferences(MainActivity.class.getName(), 0);     // Catch: Throwable -> L19
        if (r1.contains("epg-url") == false) goto L9;
    L11:
        String r2 = r1.getString("epg-url", "");     // Catch: Throwable -> L19
        String r3 = r1.getString("epg-file", "");     // Catch: Throwable -> L19
        if (r2.isEmpty() == true) goto L14;
    L16:
        this.settings.edit().putBoolean("configured", true).putString("url", r2).putString("file", r3).putBoolean("enabled", true).commit();     // Catch: Throwable -> L19
        goto L17
    L14:
        if (r3.isEmpty() == false) goto L16;
        r2 = DEFAULT_URL;
        goto L16
    L9:
        if (r1.contains("epg-file") == true) goto L11;
        r1 = this.context.getSharedPreferences("MainActivity", 0);     // Catch: Throwable -> L19
    L17:
        monitor-exit(r0);     // Catch: Throwable -> L19
    }

    private InputStream open(String r10, String r11) throws IOException {
        if (r11.isEmpty() == true) goto L10;
        InputStream r0 = this.context.getContentResolver().openInputStream(Uri.parse(r11));
        if (r0 == null) goto L9;
        return new MainActivity.LimitedInputStream(r0, 536870912);
    L9:
        throw new IOException("No se pudo abrir la guía");
    L10:
        int r2 = 0;
        URL r1 = new URL(DownloadLinks.direct(r10));
    L12:
        if (r2 > 6) goto L47;
        if (r1.getProtocol().equals("https") == false) goto L16;
    L20:
        final HttpURLConnection r8 = (HttpURLConnection) r1.openConnection();
        r8.setInstanceFollowRedirects(false);
        r8.setConnectTimeout(PlaybackException.ERROR_CODE_DRM_UNSPECIFIED);
        r8.setReadTimeout(PlaybackException.ERROR_CODE_DRM_UNSPECIFIED);
        int r3 = r8.getResponseCode();     // Catch: IOException -> L43
        if (r3 != 301) goto L24;
    L38:
        String r4 = r8.getHeaderField(HttpHeaders.LOCATION);     // Catch: IOException -> L43
        if (r4 == null) goto L42;
        r1 = new URL(r1, r4);     // Catch: IOException -> L43
        r8.disconnect();     // Catch: IOException -> L43
        r2 = r2 + 1;     // Catch: IOException -> L43
        goto L12
    L42:
        throw new IOException("Redirección sin destino");     // Catch: IOException -> L43
    L24:
        if (r3 == 302) goto L38;
        if (r3 == 303) goto L38;
        if (r3 == 307) goto L38;
        if (r3 == 308) goto L38;
        if (r3 != 200) goto L36;
        final InputStream r5 = r8.getInputStream();     // Catch: IOException -> L43
        final long r6 = 536870912;
        return new C09961(this, r5, r6, r8);
    L36:
        BlackBox.event("EPG_HTTP_ERROR", "status=" + r3);     // Catch: IOException -> L43
        throw new IOException("EPG HTTP " + r3);     // Catch: IOException -> L43
    L43:
        e = move-exception;
        r8.disconnect();
        throw e;
    L16:
        if (r1.getProtocol().equals("http") == true) goto L20;
        throw new IOException("Usa HTTP o HTTPS");
    L47:
        throw new IOException("Demasiadas redirecciones EPG");
    }

    static boolean stale(EpgData r7, long r8) {
        if (r7.count() != 0) goto L5;
    L23:
        return true;
    L5:
        if (r7.loadedAt <= 0) goto L23;
        if ((r8 - r7.loadedAt) >= 21600000) goto L23;
        if (r7.loadedAt > r8) goto L23;
        Iterator<List<EpgData.Programme>> r0 = r7.programmes.values().iterator();
    L13:
        if (r0.hasNext() == false) goto L23;
        Iterator<EpgData.Programme> r3 = r0.next().iterator();
    L16:
        if (r3.hasNext() == false) goto L13;
        if (r3.next().stop <= r8) goto L16;
        return false;
    }

    EpgData cached() {
        Object r0 = this.storage.read("epg.dat");     // Catch: Exception -> L7
        if ((r0 instanceof EpgData) == false) goto L9;
        return (EpgData) r0;
    L9:
        return new EpgData();
    }

    void configure(String r8, String r9) {
        SharedPreferences r0 = this.settings;
        monitor-enter(r0);
        SharedPreferences.Editor r1 = this.settings.edit();     // Catch: Throwable -> L15
        if (r8 != null) goto L7;
        String r3 = "";
    L8:
        SharedPreferences.Editor r2 = r1.putString("url", r3);     // Catch: Throwable -> L15
        if (r9 != null) goto L11;
        String r4 = "";
    L12:
        r2.putString("file", r4).putBoolean("enabled", true).putLong("revision", this.settings.getLong("revision", 0) + 1).remove("attempt-at").commit();     // Catch: Throwable -> L15
        monitor-exit(r0);     // Catch: Throwable -> L15
        return;
    L11:
        r4 = r9;
        goto L12
    L7:
        r3 = r8.trim();     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        throw th;
    }

    String file() {
        return this.settings.getString("file", "");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:114:0x015e -> B:111:0x015b). Please report as a decompilation issue!!! */
    EpgData refresh(boolean r20, long r21, Source r23) throws Exception {
        Object r4 = DOWNLOAD_LOCK;
        monitor-enter(r4);
        EpgData r0 = cached();     // Catch: Throwable -> L115
        SharedPreferences r6 = this.settings;     // Catch: Throwable -> L115
        monitor-enter(r6);     // Catch: Throwable -> L115
    L109:
        th = th;
    L111:
        monitor-exit(r6);     // Catch: Throwable -> L113
        throw th;     // Catch: Throwable -> L119
    L7:
        if (this.settings.getBoolean("enabled", true) == false) goto L106;
        if (r20 == true) goto L16;
    L13:
        th = th;
        goto L111
    L10:
        if (stale(r0, r21) == false) goto L106;
    L16:
        if (r20 == false) goto L18;
    L23:
        String r1 = url();     // Catch: Throwable -> L109
        String r2 = file();     // Catch: Throwable -> L109
        long r12 = this.settings.getLong("revision", 0);     // Catch: Throwable -> L109
        if (r1.isEmpty() == true) goto L26;
    L30:
        this.settings.edit().putLong("attempt-at", r21).commit();     // Catch: Throwable -> L109
        monitor-exit(r6);     // Catch: Throwable -> L109
        InputStream r3 = r23.open(r1, r2);     // Catch: Throwable -> L119
        BufferedInputStream r5 = new BufferedInputStream(r3);     // Catch: Throwable -> L97
        r5.mark(2);     // Catch: Throwable -> L88
        int r7 = r5.read();     // Catch: Throwable -> L88
        int r8 = r5.read();     // Catch: Throwable -> L88
        r5.reset();     // Catch: Throwable -> L88
        if (r7 != 31) goto L47;
        if (r8 != 139) goto L47;
        InputStream r9 = new GZIPInputStream(r5);     // Catch: Throwable -> L44
    L48:
        InputStream r18 = r3;
        MainActivity.LimitedInputStream r10 = new MainActivity.LimitedInputStream(r9, 2147483648L);     // Catch: Throwable -> L86
        EpgData r11 = EpgXmlStage.parse(r10, r21);     // Catch: Throwable -> L79
    L53:
        r10.close();     // Catch: Throwable -> L86
        r5.close();     // Catch: Throwable -> L95
        if (r18 == null) goto L58;
        r18.close();     // Catch: Throwable -> L119
    L58:
        if (r11.count() == 0) goto L146;
        SharedPreferences r13 = this.settings;     // Catch: Throwable -> L119
        monitor-enter(r13);     // Catch: Throwable -> L119
    L74:
        th = move-exception;
        throw th;     // Catch: Throwable -> L119
    L62:
        if (this.settings.getBoolean("enabled", true) == true) goto L64;
    L70:
        EpgData r14 = cached();     // Catch: Throwable -> L74
        monitor-exit(r13);     // Catch: Throwable -> L74
        monitor-exit(r4);     // Catch: Throwable -> L119
        return r14;
    L64:
        if (this.settings.getLong("revision", 0) != r12) goto L70;
        this.storage.write("epg.dat", r11);     // Catch: Throwable -> L74
        monitor-exit(r13);     // Catch: Throwable -> L74
        monitor-exit(r4);     // Catch: Throwable -> L119
        return r11;
    L146:
        throw new IOException("La guía no contiene programas vigentes");     // Catch: Throwable -> L119
    L79:
        th = move-exception;
        r10.close();     // Catch: Throwable -> L83
    L147:
        throw th;     // Catch: Throwable -> L86
    L83:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L86
        throw th;     // Catch: Throwable -> L86
    L86:
        th = move-exception;
        Throwable r15 = th;
    L132:
        r5.close();     // Catch: Throwable -> L92
    L148:
        throw r15;     // Catch: Throwable -> L95
    L92:
        th = move-exception;
        r15.addSuppressed(th);     // Catch: Throwable -> L95
        throw r15;     // Catch: Throwable -> L95
    L44:
        th = move-exception;
        r18 = r3;
        r15 = th;
    L47:
        r9 = r5;
    L88:
        th = move-exception;
        r18 = r3;
        r15 = th;
    L95:
        th = move-exception;
        Throwable r16 = th;
    L99:
        if (r18 == null) goto L149;
        r18.close();     // Catch: Throwable -> L102
    L150:
        throw r16;     // Catch: Throwable -> L119
    L102:
        th = move-exception;
        r16.addSuppressed(th);     // Catch: Throwable -> L119
        throw r16;     // Catch: Throwable -> L119
    L149:
        throw r16;     // Catch: Throwable -> L119
    L97:
        th = move-exception;
        r18 = r3;
        r16 = th;
        goto L99
    L26:
        if (r2.isEmpty() == false) goto L30;
        monitor-exit(r6);     // Catch: Throwable -> L13
        monitor-exit(r4);     // Catch: Throwable -> L115
        return r0;
    L18:
        if (r21 < this.settings.getLong("attempt-at", 0)) goto L23;
        if ((r21 - this.settings.getLong("attempt-at", 0)) >= 900000) goto L23;
    L106:
        monitor-exit(r6);     // Catch: Throwable -> L113
        monitor-exit(r4);     // Catch: Throwable -> L119
        return r0;
    L113:
        th = th;
    L119:
        th = th;
    L117:
        monitor-exit(r4);     // Catch: Throwable -> L119
        throw th;
    L115:
        th = th;
        goto L117
    }

    EpgData refreshBackgroundOriginal() {
        long r0 = SystemClock.elapsedRealtime();
        BlackBox.event("EPG_CHECK_START", "");
        EpgData r2 = refresh(false, System.currentTimeMillis(), new EpgRepository$$ExternalSyntheticLambda0(this));     // Catch: Exception -> L5
        BlackBox.event("EPG_CHECK_END", "duration_ms=" + (SystemClock.elapsedRealtime() - r0) + " programmes=" + r2.count());     // Catch: Exception -> L5
        return r2;
    L5:
        e = move-exception;
        BlackBox.failure("EPG_ERROR", e);
        return cached();
    }

    EpgData refreshIfNeeded() {
        return BackgroundWork.refresh(this);
    }

    void remove() {
        SharedPreferences r0 = this.settings;
        monitor-enter(r0);
        this.settings.edit().putBoolean("enabled", false).putLong("revision", this.settings.getLong("revision", 0) + 1).commit();     // Catch: Throwable -> L7
        this.storage.remove("epg.dat");     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    String url() {
        return this.settings.getString("url", DEFAULT_URL);
    }
}
