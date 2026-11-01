package es.antonio.m3u;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/* JADX INFO: loaded from: classes7.dex */
public final class TvHlsUrls {
    private static final ThreadLocal<long[]> COUNTS = null;
    private static final ThreadLocal<Base> LAST = null;

    private static final class Base {
        final String directory;
        final String origin;
        final String originSlash;
        final URI uri;
        final String value;

        Base(String r7) throws URISyntaxException {
            this.value = r7;
            this.uri = new URI(r7);
            String r8 = this.uri.getScheme();
            String r0 = this.uri.getRawAuthority();
            String r3 = null;
            if ("http".equalsIgnoreCase(r8) == true) goto L7;
            if ("https".equalsIgnoreCase(r8) == true) goto L7;
        L15:
            String r9 = null;
            String r1 = null;
        L16:
            this.origin = r9;
            if (r9 == null) goto L20;
            r3 = String.valueOf(r9) + "/";
        L20:
            this.originSlash = r3;
            this.directory = r1;
            return;
        L7:
            if (this.uri.isOpaque() == true) goto L15;
            if (r0 == null) goto L15;
            r9 = String.valueOf(r8) + "://" + r0;
            String r2 = this.uri.resolve("chdtv_uri_base_marker").toString();
            if (r2.startsWith(String.valueOf(r9) + "/") == true) goto L12;
        L14:
            r1 = null;
            goto L16
        L12:
            if (r2.endsWith("chdtv_uri_base_marker") == false) goto L14;
            r1 = r2.substring(0, r2.length() - "chdtv_uri_base_marker".length());
            goto L16
        }
    }

    static {
        LAST = new ThreadLocal();
        COUNTS = new C10371();
    }

    public TvHlsUrls() {
    }

    private static boolean hex(char r1) {
        if (r1 < '0') goto L7;
        if (r1 > '9') goto L7;
        return true;
    L7:
        if (r1 < 'a') goto L11;
        if (r1 > 'f') goto L11;
        return true;
    L11:
        if (r1 >= 'A') goto L13;
        return false;
    L13:
        if (r1 > 'F') goto L21;
        return true;
    L21:
        return false;
    }

    public static long[] mark() {
        return (long[]) COUNTS.get().clone();
    }

    private static boolean plainPath(String r7, int r8) {
        if (r8 != 0) goto L5;
    L39:
        return false;
    L5:
        if (r7.startsWith("//") == true) goto L39;
        int r1 = r7.indexOf(58);
        int r3 = r7.indexOf(47);
        if (r1 < 0) goto L13;
        if (r1 >= r8) goto L13;
        if (r3 < 0) goto L12;
        if (r1 >= r3) goto L13;
    L12:
        return false;
    L13:
        int r2 = 0;
    L15:
        if (r2 >= r8) goto L16;
        int r4 = r7.indexOf(47, r2);
        if (r4 < 0) goto L20;
        if (r4 > r8) goto L20;
    L21:
        int r5 = r4 - r2;
        if (r5 != 1) goto L26;
        if (r7.charAt(r2) != '.') goto L26;
    L31:
        return false;
    L26:
        if (r5 == 2) goto L28;
    L32:
        if (r4 >= r8) goto L38;
        int r6 = r4 + 1;
        if (r6 >= r8) goto L38;
        if (r7.charAt(r6) != '/') goto L38;
        return false;
    L38:
        r2 = r4 + 1;
        goto L15
    L28:
        if (r7.charAt(r2) != '.') goto L32;
        if (r7.charAt(r2 + 1) != '.') goto L32;
    L20:
        r4 = r8;
        goto L21
    L16:
        return true;
    }

    public static String report(long[] r8) {
        long[] r0 = COUNTS.get();
        long r2 = r0[0] - r8[0];
        long r4 = r0[1] - r8[1];
        if ((r2 + r4) != 0) goto L6;
        return null;
    L6:
        return "fast=" + r2 + " original=" + r4 + " full_metadata=true";
    }

    public static String resolve(String r6, String r7) throws IOException {
        if (r7.contains("{$") == true) goto L48;
        Base r0 = LAST.get();     // Catch: URISyntaxException -> L44
        if (r0 != null) goto L7;
    L8:
        r0 = new Base(r6);     // Catch: URISyntaxException -> L44
        LAST.set(r0);     // Catch: URISyntaxException -> L44
    L10:
        if (r0.origin == null) goto L17;
        if (r7.startsWith(r0.originSlash) == false) goto L17;
        if (safeAscii(r7, r0.origin.length()) == false) goto L17;
        String r8 = r7;
    L32:
        if (r8 == null) goto L35;
        long[] r9 = COUNTS.get();     // Catch: URISyntaxException -> L44
        r9[0] = r9[0] + 1;     // Catch: URISyntaxException -> L44
        return r8;
    L35:
        long[] r10 = COUNTS.get();     // Catch: URISyntaxException -> L44
        r10[1] = r10[1] + 1;     // Catch: URISyntaxException -> L44
        URI r11 = r0.uri.resolve(new URI(r7));     // Catch: URISyntaxException -> L44
        if ("http".equalsIgnoreCase(r11.getScheme()) == true) goto L43;
        if ("https".equalsIgnoreCase(r11.getScheme()) == true) goto L43;
        throw new IOException("Sample-AES: se necesita una URL HTTP/HTTPS");     // Catch: URISyntaxException -> L44
    L43:
        return r11.toString();
    L17:
        if (r0.directory != null) goto L19;
    L30:
        r8 = null;
        goto L32
    L19:
        if (safeAscii(r7, 0) == false) goto L30;
        int r12 = r7.indexOf(63);     // Catch: URISyntaxException -> L44
        if (r12 >= 0) goto L24;
        r12 = r7.length();     // Catch: URISyntaxException -> L44
    L24:
        if (plainPath(r7, r12) == false) goto L30;
        if (r7.charAt(0) != '/') goto L28;
        StringBuilder r13 = new StringBuilder(String.valueOf(r0.origin)).append(r7);     // Catch: URISyntaxException -> L44
    L29:
        r8 = r13.toString();     // Catch: URISyntaxException -> L44
        goto L32
    L28:
        r13 = new StringBuilder(String.valueOf(r0.directory)).append(r7);     // Catch: URISyntaxException -> L44
        goto L29
    L7:
        if (r0.value.equals(r6) == true) goto L10;
    L46:
        throw new IOException("Sample-AES: URL inválida");
    L48:
        throw new IOException("Sample-AES: variables de URL no compatibles");
    }

    private static boolean safeAscii(String r3, int r4) {
    L3:
        if (r4 >= r3.length()) goto L4;
        char r0 = r3.charAt(r4);
        if (r0 < 'a') goto L10;
        if (r0 > 'z') goto L10;
    L29:
        r4 = r4 + 1;
    L10:
        if (r0 < 'A') goto L14;
        if (r0 <= 'Z') goto L29;
    L14:
        if (r0 < '0') goto L18;
        if (r0 <= '9') goto L29;
    L18:
        if ("-._~!$&'()*+,;=:@/?".indexOf(r0) >= 0) goto L29;
        if (r0 != '%') goto L30;
        int r1 = r4 + 2;
        if (r1 >= r3.length()) goto L42;
        if (hex(r3.charAt(r4 + 1)) == false) goto L43;
        if (hex(r3.charAt(r1)) == false) goto L44;
        r4 = r1;
        goto L29
    L44:
        return false;
    L43:
        return false;
    L42:
        return false;
    L30:
        return false;
    L4:
        return true;
    }
}
