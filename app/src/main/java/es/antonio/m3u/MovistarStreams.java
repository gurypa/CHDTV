package es.antonio.m3u;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Locale;

/* JADX INFO: loaded from: classes3.dex */
final class MovistarStreams {
    private MovistarStreams() {
    }

    static boolean allowedHost(String r5) {
        URI r1 = new URI(r5);     // Catch: Exception -> L20
        String r2 = r1.getScheme();     // Catch: Exception -> L20
        String r3 = r1.getHost();     // Catch: Exception -> L20
        if (r3 != null) goto L6;
        return false;
    L6:
        if ("http".equalsIgnoreCase(r2) == false) goto L8;
    L10:
        String r4 = r3.toLowerCase(Locale.ROOT);     // Catch: Exception -> L20
        if (r4.equals("telefonica.com") == false) goto L13;
    L18:
        return true;
    L13:
        if (r4.endsWith(".telefonica.com") == true) goto L18;
        if (r4.equals("emisiondof6.com") == true) goto L18;
        if (r4.endsWith(".emisiondof6.com") == true) goto L18;
        return false;
    L8:
        if ("https".equalsIgnoreCase(r2) == true) goto L10;
        return false;
    L21:
        return false;
    }

    static boolean eligible(Catalog.Source r0) {
        return DemandTokenSources.eligible(r0);
    }

    static String queryToken(String r12) {
        String r2 = new URI(r12).getRawQuery();     // Catch: Exception -> L14
        if (r2 == null) goto L15;
        String[] r3 = r2.split("&", -1);     // Catch: Exception -> L14
        int r4 = r3.length;     // Catch: Exception -> L14
        int r6 = 0;
    L6:
        if (r6 >= r4) goto L15;
        String[] r8 = r3[r6].split("=", 2);     // Catch: Exception -> L14
        if (URLDecoder.decode(r8[0], "UTF-8").equalsIgnoreCase("x-tcdn-token") == true) goto L10;
        r6 = r6 + 1;
        goto L6
    L10:
        if (r8.length != 2) goto L20;
        return URLDecoder.decode(r8[1], "UTF-8");
    L20:
        return "";
    L15:
        return "";
    }

    static String withToken(String r13, String r14) {
        if (r14 != null) goto L5;
    L28:
        return r13;
    L5:
        if (r14.isEmpty() == true) goto L28;
        if (allowedHost(r13) == false) goto L28;
        String r4 = new URI(r13).getRawQuery();     // Catch: Exception -> L27
        if (r4 != null) goto L12;
        return r13;
    L12:
        String[] r5 = r4.split("&", -1);     // Catch: Exception -> L27
        boolean r6 = false;
        int r7 = 0;
    L14:
        if (r7 >= r5.length) goto L19;
        String r8 = r5[r7].split("=", 2)[0];     // Catch: Exception -> L27
        if (URLDecoder.decode(r8, "UTF-8").equalsIgnoreCase("x-tcdn-token") == false) goto L18;
        r5[r7] = r8 + "=" + URLEncoder.encode(r14, "UTF-8").replace("+", "%20");     // Catch: Exception -> L27
        r6 = true;
    L18:
        r7 = r7 + 1;     // Catch: Exception -> L27
        goto L14
    L19:
        if (r6 == true) goto L21;
        return r13;
    L21:
        int r0 = r13.indexOf(63);     // Catch: Exception -> L27
        int r1 = r13.indexOf(35, r0);     // Catch: Exception -> L27
        StringBuilder r2 = new StringBuilder().append(r13.substring(0, r0 + 1)).append(MovistarStreams$$ExternalSyntheticBackport0.m403m("&", r5));     // Catch: Exception -> L27
        if (r1 >= 0) goto L24;
        String r9 = "";
    L25:
        return r2.append(r9).toString();
    L24:
        r9 = r13.substring(r1);     // Catch: Exception -> L27
        goto L25
    }
}
