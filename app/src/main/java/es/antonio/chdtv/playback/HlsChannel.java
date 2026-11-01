package es.antonio.chdtv.playback;

import android.net.Uri;
import androidx.media3.exoplayer.offline.DownloadService;
import java.util.Locale;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes4.dex */
public final class HlsChannel {
    public final String casId;
    public final String contentId;
    public final int dial;
    public final String grupo;

    /* JADX INFO: renamed from: iv */
    public final String f168iv;
    public final String kid;
    public final String nombre;
    public final String pssh;
    public final String serviceUid;
    public final String skd;
    public final String urlPrincipal;
    public final String urlRedireccion;

    private HlsChannel(String r1, String r2, int r3, String r4, String r5, String r6, String r7, String r8, String r9, String r10, String r11, String r12) {
        this.nombre = r1;
        this.grupo = r2;
        this.dial = r3;
        this.casId = r4;
        this.serviceUid = r5;
        this.urlPrincipal = r6;
        this.urlRedireccion = r7;
        this.skd = r8;
        this.kid = r9;
        this.contentId = r10;
        this.f168iv = r11;
        this.pssh = r12;
    }

    public static HlsChannel fromJson(String r15) {
        JSONObject r1 = new JSONObject(r15);     // Catch: Exception -> L5
        return new HlsChannel(r1.optString("nombre", ""), r1.optString("grupo", ""), r1.optInt("dial", -1), r1.optString("cas_id", ""), r1.optString("service_uid", ""), r1.optString("url_principal", ""), r1.optString("url_redireccion", ""), r1.optString("skd", ""), r1.optString("kid", ""), r1.optString(DownloadService.KEY_CONTENT_ID, ""), r1.optString("iv", ""), r1.optString("pssh", ""));
    L5:
        e = move-exception;
        throw new IllegalArgumentException("JSON HLS no válido", e);
    }

    private static boolean isHttpM3u8(String r6) {
        if (r6 != null) goto L5;
    L25:
        return false;
    L5:
        if (r6.trim().isEmpty() == true) goto L25;
        Uri r1 = Uri.parse(r6.trim());     // Catch: Exception -> L23
        String r2 = r1.getScheme();     // Catch: Exception -> L23
        String r3 = r1.getPath();     // Catch: Exception -> L23
        if ("http".equalsIgnoreCase(r2) == true) goto L12;
        if ("https".equalsIgnoreCase(r2) == true) goto L12;
        return false;
    L12:
        if (r1.getHost() != null) goto L14;
        return false;
    L14:
        if (r1.getHost().isEmpty() == false) goto L16;
        return false;
    L16:
        if (r1.getUserInfo() != null) goto L31;
        if (r3 == null) goto L32;
        if (r3.toLowerCase(Locale.ROOT).endsWith(".m3u8") == false) goto L33;
        return true;
    L33:
        return false;
    L32:
        return false;
    L31:
        return false;
    L24:
        return false;
    }

    public String playbackUrl() {
        if (isHttpM3u8(this.urlPrincipal) == false) goto L7;
        return this.urlPrincipal.trim();
    L7:
        if (isHttpM3u8(this.urlRedireccion) == false) goto L11;
        return this.urlRedireccion.trim();
    L11:
        throw new IllegalArgumentException("No hay una URL HLS HTTP/HTTPS válida");
    }
}
