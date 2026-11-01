package es.antonio.m3u;

import com.google.common.net.HttpHeaders;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes3.dex */
public final class M3uParser {
    private static final Pattern ATTR = null;

    /* JADX INFO: renamed from: EP */
    private static final Pattern f184EP = null;
    public static final int MAX_LINE = 1048576;

    public static final class Result {
        public final List<Entry> entries;
        public final int skipped;

        Result(List<Entry> r1, int r2) {
            this.entries = r1;
            this.skipped = r2;
        }
    }

    static {
        ATTR = Pattern.compile("([\\w-]+)\\s*=\\s*\"([^\"]*)\"");
        f184EP = Pattern.compile("(?i)^(.*?)\\s*[·|\\-]?\\s*S(\\d{1,3})\\s*E(\\d{1,4})(?:\\s*[·|\\-].*)?$");
    }

    public M3uParser() {
    }

    private static String boundedLine(Reader r5) throws IOException {
        StringBuilder r0 = new StringBuilder();
        boolean r1 = false;
    L3:
        int r2 = r5.read();
        if (r2 == (-1)) goto L14;
        if (r2 == 10) goto L14;
        if (r2 == 13) goto L3;
        if (r0.length() < 1048576) goto L12;
        r1 = true;
        goto L3
    L12:
        r0.append((char) r2);
    L14:
        if (r1 == false) goto L16;
        return "\u0000";
    L16:
        if (r2 != (-1)) goto L21;
        if (r0.length() != 0) goto L21;
        return null;
    L21:
        return r0.toString();
    }

    private static int delimiter(String r4) {
        boolean r0 = false;
        int r1 = 0;
    L4:
        if (r1 >= r4.length()) goto L16;
        char r2 = r4.charAt(r1);
        if (r2 != '\"') goto L12;
        if (r0 == true) goto L9;
        boolean r3 = true;
    L10:
        r0 = r3;
    L15:
        r1 = r1 + 1;
        goto L4
    L9:
        r3 = false;
        goto L10
    L12:
        if (r2 != ',') goto L15;
        if (r0 == true) goto L15;
        return r1;
    L16:
        return -1;
    }

    public static List<Entry> parse(Reader r1, String r2) throws IOException {
        return read(r1, r2).entries;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r18v0 */
    /* JADX WARN: Type inference failed for: r18v1 */
    /* JADX WARN: Type inference failed for: r18v2 */
    /* JADX WARN: Type inference failed for: r18v3 */
    /* JADX WARN: Type inference failed for: r18v4 */
    /* JADX WARN: Type inference failed for: r18v5 */
    public static Result read(Reader r26, String r27) throws IOException {
        int r0 = 0;
        ArrayList r1 = new ArrayList();
        HashSet r2 = new HashSet();
        Entry r3 = null;
        BufferedReader r4 = new BufferedReader(r26);
        r4.mark(1024);
        StringBuilder r6 = new StringBuilder();
        int r7 = 0;
    L4:
        if (r7 >= 512) goto L9;
        int r8 = r4.read();
        if (r8 < 0) goto L9;
        r6.append((char) r8);
        r7 = r7 + 1;
    L9:
        r4.reset();
        if (r6.toString().replace("\ufeff", "").trim().matches("(?is)^(?:<!doctype\\s+html\\b|<html\\b|<head\\b|<body\\b).*") == true) goto L123;
        BoundedLines r9 = new BoundedLines(r4, 1048576);
    L12:
        String r10 = r9.next();
        if (r10 == null) goto L121;
        if (r10.equals("\u0000") == true) goto L16;
        String r11 = r10.replace("\ufeff", "").trim();
        boolean r12 = false;
        if (r11.startsWith("#EXTINF:") == true) goto L19;
        BufferedReader r19 = r4;
        if (r3 != null) goto L68;
    L91:
        if (r3 != null) goto L93;
    L102:
        if (r3 == null) goto L119;
        if (r11.isEmpty() == true) goto L119;
        if (r11.startsWith("#") == true) goto L119;
        String[] r5 = r11.split("\\|", 2);
        Entry.details(r3).url = r5[0];
        if (r5.length != 2) goto L111;
        readHeaders(r5[1], r3.headers);
    L111:
        if (Entry.details(r3).url.regionMatches(true, 0, "https://", 0, 8) == true) goto L115;
        if (Entry.details(r3).url.regionMatches(true, 0, "http://", 0, 7) == true) goto L115;
    L117:
        r3 = null;
        r4 = r19;
    L115:
        if (r2.add(r3.identity() + "\n" + Entry.details(r3).url) == false) goto L117;
        r1.add(r3);
    L119:
        r4 = r19;
        goto L12
    L93:
        if (r11.startsWith("#EXTVLCOPT:") == false) goto L102;
        int r13 = r11.indexOf(61);
        if (r13 <= 0) goto L119;
        String r14 = r11.substring(11, r13);
        if (r14.equals("http-user-agent") == false) goto L100;
        r3.headers.put(HttpHeaders.USER_AGENT, r11.substring(r13 + 1));
    L100:
        if (r14.equals("http-referrer") == false) goto L119;
        r3.headers.put(HttpHeaders.REFERER, r11.substring(r13 + 1));
        goto L119
    L68:
        if (r11.startsWith("#KODIPROP:") == false) goto L91;
        int r15 = r11.indexOf(61);
        if (r15 <= 0) goto L119;
        String r16 = r11.substring(10, r15);
        String r17 = r11.substring(r15 + 1);
        if (r16.endsWith("stream_headers") == false) goto L74;
    L88:
        Map<String, String> r18 = r3.headers;
    L89:
        readHeaders(r17, r18);
        goto L119
    L74:
        if (r16.endsWith("manifest_headers") == true) goto L88;
        if (r16.endsWith("manifest_type") == false) goto L80;
        r3.manifest = r17;
        goto L119
    L80:
        if (r16.endsWith("license_headers") == false) goto L83;
        r18 = r3.licenseHeaders;
        goto L89
    L83:
        if (r16.endsWith("license_type") == false) goto L86;
        r3.licenseType = r17;
        goto L119
    L86:
        if (r16.endsWith("license_key") == false) goto L119;
        Entry.details(r3).license = r17;
        goto L119
    L19:
        r3 = new Entry();
        r3.kind = r27;
        int r110 = delimiter(r11);
        if (r110 < 0) goto L21;
        r3.title = r11.substring(r110 + 1).trim();
        Matcher r111 = ATTR.matcher(r11.substring(0, r110));
    L23:
        boolean r112 = r12;
        if (r111.find() == false) goto L48;
        String r113 = r111.group(1).toLowerCase(Locale.ROOT);
        switch(r113.hashCode()) {
            case -861895229: goto L38;
            case -328855158: goto L35;
            case 647476307: goto L32;
            case 647522611: goto L29;
            default: goto L40;
        };
    L40:
        ?? r114 = -1;
    L41:
        switch(r114) {
            case 0: goto L46;
            case 1: goto L45;
            case 2: goto L44;
            case 3: goto L43;
            default: goto L47;
        };
    L43:
        r3.logo = r111.group(2);
        goto L47
    L44:
        r3.tvgName = r111.group(2);
        goto L47
    L45:
        r3.tvgId = r111.group(2);
        goto L47
    L46:
        r3.group = r111.group(2);
    L47:
        r12 = r112;
        goto L23
    L29:
        if (r113.equals("tvg-name") == false) goto L40;
        r114 = 2;
        goto L41
    L32:
        if (r113.equals("tvg-logo") == false) goto L40;
        r114 = 3;
        goto L41
    L35:
        if (r113.equals("group-title") == false) goto L40;
        r114 = r112;
        goto L41
    L38:
        if (r113.equals("tvg-id") == false) goto L40;
        r114 = 1;
        goto L41
    L48:
        Matcher r115 = f184EP.matcher(r3.title);
        if (r115.matches() == false) goto L64;
        r3.kind = "Series";
        r3.series = r115.group(1).replaceAll("[\\s·|\\-]+$", "");
        if (r3.series.isEmpty() == true) goto L53;
    L62:
        r3.season = Integer.parseInt(r115.group(2));
        r3.episode = Integer.parseInt(r115.group(3));
        goto L64
    L53:
        if (r3.group.equals("Sin sección") == true) goto L58;
        if (r3.group.isEmpty() == true) goto L58;
        String r20 = r3.group;
    L61:
        r3.series = r20;
    L58:
        if (r3.tvgName.isEmpty() == true) goto L60;
        r20 = r3.tvgName;
        goto L61
    L60:
        r20 = r3.tvgId;
    L64:
        r4 = r4;
        goto L12
    L21:
        r0 = r0 + 1;
        r3 = null;
        goto L12
    L16:
        r0 = r0 + 1;
        r3 = null;
        goto L12
    L121:
        return new Result(r1, r0);
    L123:
        throw new IOException("El enlace devuelve una página web. Comparte el archivo M3U con permiso de descarga");
    }

    private static void readHeaders(String r11, Map<String, String> r12) throws UnsupportedEncodingException {
        String[] r1 = r11.split("&");
        int r2 = r1.length;
        int r4 = 0;
    L3:
        if (r4 >= r2) goto L27;
        String r5 = r1[r4];
        int r6 = r5.indexOf(61);
        if (r6 <= 0) goto L26;
        String r7 = URLDecoder.decode(r5.substring(0, r6), "UTF-8").trim();     // Catch: IllegalArgumentException -> L24
        String r8 = URLDecoder.decode(r5.substring(r6 + 1), "UTF-8");     // Catch: IllegalArgumentException -> L24
        if (r7.matches("[A-Za-z0-9!#$%&'*+.^_`|~-]+") == false) goto L26;
        if (r8.indexOf(13) >= 0) goto L26;
        if (r8.indexOf(10) >= 0) goto L26;
        if (r8.indexOf(0) >= 0) goto L26;
        Iterator<String> r9 = r12.keySet().iterator();     // Catch: IllegalArgumentException -> L24
    L18:
        if (r9.hasNext() == false) goto L22;
        if (r9.next().equalsIgnoreCase(r7) == false) goto L18;
        r9.remove();     // Catch: IllegalArgumentException -> L24
        goto L18
    L22:
        r12.put(r7, r8);     // Catch: IllegalArgumentException -> L24
    L26:
        r4 = r4 + 1;
        goto L3
    }
}
