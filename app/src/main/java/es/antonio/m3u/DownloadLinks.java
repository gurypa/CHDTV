package es.antonio.m3u;

import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.text.Typography;

/* JADX INFO: loaded from: classes3.dex */
public final class DownloadLinks {
    private DownloadLinks() {
    }

    public static String direct(String r11) throws IOException {
        URI r0 = new URI(r11.trim());     // Catch: IllegalArgumentException -> L32 Throwable -> L34
        String r1 = r0.getHost();     // Catch: IllegalArgumentException -> L32 Throwable -> L34
        if (r1 == null) goto L31;
        if (r0.getScheme().matches("(?i)https?") == false) goto L31;
        if (r1.equalsIgnoreCase("dropbox.com") == false) goto L9;
    L12:
        String r2 = r0.getRawPath();     // Catch: IllegalArgumentException -> L32 Throwable -> L34
        if (r2.startsWith("/sh/") == true) goto L29;
        if (r2.startsWith("/scl/fo/") == true) goto L29;
        ArrayList r3 = new ArrayList();     // Catch: IllegalArgumentException -> L32 Throwable -> L34
        if (r0.getRawQuery() == null) goto L26;
        String[] r4 = r0.getRawQuery().split("&");     // Catch: IllegalArgumentException -> L32 Throwable -> L34
        int r5 = r4.length;     // Catch: IllegalArgumentException -> L32 Throwable -> L34
        int r7 = 0;
    L19:
        if (r7 >= r5) goto L26;
        String r8 = r4[r7];     // Catch: IllegalArgumentException -> L32 Throwable -> L34
        String r10 = URLDecoder.decode(r8.split("=", 2)[0], "UTF-8");     // Catch: IllegalArgumentException -> L32 Throwable -> L34
        if (r10.equalsIgnoreCase(CmcdConfiguration.KEY_DEADLINE) == true) goto L25;
        if (r10.equalsIgnoreCase("raw") == true) goto L25;
        r3.add(r8);     // Catch: IllegalArgumentException -> L32 Throwable -> L34
    L25:
        r7 = r7 + 1;     // Catch: IllegalArgumentException -> L32 Throwable -> L34
    L26:
        r3.add("dl=1");     // Catch: IllegalArgumentException -> L32 Throwable -> L34
        return (r0.getScheme() + "://" + r0.getRawAuthority() + r2) + "?" + join(r3);
    L29:
        throw new IOException("Comparte el archivo M3U de Dropbox, no su carpeta");     // Catch: IllegalArgumentException -> L32 Throwable -> L34
    L9:
        if (r1.equalsIgnoreCase("www.dropbox.com") == true) goto L12;
        return r11.trim();
    L31:
        throw new IOException("Usa un enlace HTTP o HTTPS válido");     // Catch: IllegalArgumentException -> L32 Throwable -> L34
    L36:
        throw new IOException("El enlace no tiene un formato válido");
    }

    private static String join(List<String> r4) {
        StringBuilder r0 = new StringBuilder();
        Iterator<String> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L10;
        String r2 = r1.next();
        if (r0.length() <= 0) goto L8;
        r0.append(Typography.amp);
    L8:
        r0.append(r2);
        goto L4
    L10:
        return r0.toString();
    }
}
