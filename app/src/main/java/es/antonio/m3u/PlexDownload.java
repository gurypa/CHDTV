package es.antonio.m3u;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/* JADX INFO: loaded from: classes3.dex */
final class PlexDownload {
    private PlexDownload() {
    }

    static InputStream open(String r7) throws IOException {
        URL r0 = new URL(DownloadLinks.direct(r7));
        int r1 = 0;
    L4:
        if (r1 >= 6) goto L50;
        if (r0.getProtocol().equals("https") == true) goto L13;
        if (r0.getProtocol().equals("http") == true) goto L13;
        throw new IOException("Usa una URL HTTP o HTTPS");
    L13:
        if (r0.getUserInfo() != null) goto L48;
        final HttpURLConnection r2 = (HttpURLConnection) r0.openConnection();
        r2.setInstanceFollowRedirects(false);
        r2.setConnectTimeout(15000);
        r2.setReadTimeout(AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH);
        r2.setRequestProperty(HttpHeaders.USER_AGENT, "Chdtv/0.34");
        int r3 = r2.getResponseCode();     // Catch: IOException -> L44
        if (r3 != 301) goto L18;
    L39:
        String r4 = r2.getHeaderField(HttpHeaders.LOCATION);     // Catch: IOException -> L44
        if (r4 == null) goto L43;
        r0 = new URL(r0, r4);     // Catch: IOException -> L44
        r2.disconnect();     // Catch: IOException -> L44
        r1 = r1 + 1;     // Catch: IOException -> L44
        goto L4
    L43:
        throw new IOException("Redirección sin destino");     // Catch: IOException -> L44
    L18:
        if (r3 == 302) goto L39;
        if (r3 == 303) goto L39;
        if (r3 == 307) goto L39;
        if (r3 == 308) goto L39;
        if (r3 != 200) goto L29;
        final InputStream r5 = r2.getInputStream();     // Catch: IOException -> L44
        return new C10271(r5, r2);
    L29:
        if (r3 != 401) goto L31;
    L34:
        String r6 = "El enlace necesita permiso o ha caducado";
    L36:
        throw new IOException(r6);     // Catch: IOException -> L44
    L31:
        if (r3 == 403) goto L34;
        r6 = "Descarga HTTP " + r3;     // Catch: IOException -> L44
    L44:
        e = move-exception;
        r2.disconnect();
        throw e;
    L48:
        throw new IOException("URL con credenciales no admitida");
    L50:
        throw new IOException("Demasiadas redirecciones");
    }
}
