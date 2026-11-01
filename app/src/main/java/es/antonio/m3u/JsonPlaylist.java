package es.antonio.m3u;

import android.util.JsonReader;
import android.util.JsonToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/* JADX INFO: loaded from: classes3.dex */
final class JsonPlaylist {
    JsonPlaylist() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v1 */
    /* JADX WARN: Type inference failed for: r13v10 */
    /* JADX WARN: Type inference failed for: r13v13 */
    /* JADX WARN: Type inference failed for: r13v16 */
    /* JADX WARN: Type inference failed for: r13v19 */
    /* JADX WARN: Type inference failed for: r13v22 */
    /* JADX WARN: Type inference failed for: r13v25 */
    /* JADX WARN: Type inference failed for: r13v27 */
    /* JADX WARN: Type inference failed for: r13v29 */
    /* JADX WARN: Type inference failed for: r13v32 */
    /* JADX WARN: Type inference failed for: r13v35 */
    /* JADX WARN: Type inference failed for: r13v38 */
    /* JADX WARN: Type inference failed for: r13v4 */
    /* JADX WARN: Type inference failed for: r13v41 */
    /* JADX WARN: Type inference failed for: r13v44 */
    /* JADX WARN: Type inference failed for: r13v47 */
    /* JADX WARN: Type inference failed for: r13v50 */
    /* JADX WARN: Type inference failed for: r13v53 */
    /* JADX WARN: Type inference failed for: r13v56 */
    /* JADX WARN: Type inference failed for: r13v59 */
    /* JADX WARN: Type inference failed for: r13v62 */
    /* JADX WARN: Type inference failed for: r13v63 */
    /* JADX WARN: Type inference failed for: r13v7 */
    /* JADX WARN: Type inference failed for: r13v72 */
    /* JADX WARN: Type inference failed for: r17v21 */
    /* JADX WARN: Type inference failed for: r17v22 */
    private static void array(JsonReader r21, String r22, List<Entry> r23, Set<String> r24, int[] r25) throws IOException {
        r21.beginArray();
    L4:
        if (r21.hasNext() == false) goto L176;
        boolean r2 = true;
        boolean r3 = false;
        if (r21.peek() != JsonToken.BEGIN_OBJECT) goto L7;
        Entry r0 = new Entry();
        r0.kind = r22;
        ArrayList r1 = new ArrayList();
        r21.beginObject();
        String r9 = "";
        String r8 = "";
        String r7 = "";
        String r6 = "";
    L10:
        if (r21.hasNext() == false) goto L132;
        String r4 = r21.nextName();
        switch(r4.hashCode()) {
            case -1784152691: goto L78;
            case -1367579195: goto L75;
            case -1357934708: goto L72;
            case -1039904823: goto L69;
            case -861895229: goto L66;
            case -861847179: goto L63;
            case -855009709: goto L60;
            case -328855158: goto L57;
            case 106079: goto L54;
            case 106182: goto L51;
            case 116079: goto L48;
            case 3288564: goto L45;
            case 3327403: goto L42;
            case 3373707: goto L39;
            case 98629247: goto L36;
            case 98634857: goto L33;
            case 110371416: goto L30;
            case 110745120: goto L27;
            case 647476307: goto L24;
            case 647522611: goto L21;
            case 915665765: goto L18;
            case 2058907998: goto L15;
            default: goto L80;
        };
    L80:
        ?? r13 = -1;
    L81:
        switch(r13) {
            case 0: goto L130;
            case 1: goto L130;
            case 2: goto L130;
            case 3: goto L129;
            case 4: goto L129;
            case 5: goto L129;
            case 6: goto L128;
            case 7: goto L127;
            case 8: goto L124;
            case 9: goto L123;
            case 10: goto L123;
            case 11: goto L122;
            case 12: goto L122;
            case 13: goto L122;
            case 14: goto L120;
            case 15: goto L120;
            case 16: goto L120;
            case 17: goto L119;
            case 18: goto L118;
            case 19: goto L117;
            case 20: goto L84;
            case 21: goto L84;
            default: goto L82;
        };
    L82:
        boolean r16 = r2;
        boolean r17 = r3;
    L83:
        r21.skipValue();
    L131:
        r2 = r16;
        r3 = r17;
        goto L10
    L84:
        r16 = r2;
        if (r21.peek() != JsonToken.BEGIN_ARRAY) goto L116;
        r21.beginArray();
    L88:
        if (r21.hasNext() == false) goto L115;
        if (r21.peek() != JsonToken.BEGIN_OBJECT) goto L113;
        String r5 = "";
        String r14 = "";
        r21.beginObject();
        boolean r18 = r3;
        String r10 = "CONTENT";
    L93:
        if (r21.hasNext() == false) goto L105;
        String r19 = r4;
        String r11 = r21.nextName();
        if (r11.equals("kid") == false) goto L98;
        r5 = value(r21);
    L104:
        r4 = r19;
        goto L93
    L98:
        if (r11.equals("key") == false) goto L101;
        r14 = value(r21);
        goto L104
    L101:
        if (r11.equals("tipo") == false) goto L103;
        r10 = value(r21);
        goto L104
    L103:
        r21.skipValue();
        goto L104
    L105:
        String r110 = r4;
        r21.endObject();
        if (r10.equalsIgnoreCase("CONTENT") == true) goto L108;
    L112:
        r3 = r18;
    L114:
        r4 = r110;
        goto L88
    L108:
        if (r5.isEmpty() == true) goto L112;
        if (r14.isEmpty() == true) goto L112;
        r1.add(r5.replace("-", "") + ":" + r14);
        goto L112
    L113:
        r110 = r4;
        r1.add(value(r21));
        goto L114
    L115:
        r17 = r3;
        r21.endArray();
        goto L131
    L116:
        r17 = r3;
        r1.add(value(r21));
        goto L131
    L117:
        r16 = r2;
        r17 = r3;
        r9 = value(r21);
        goto L131
    L118:
        r16 = r2;
        r17 = r3;
        r8 = value(r21);
        goto L131
    L119:
        r16 = r2;
        r17 = r3;
        r0.tvgName = value(r21);
        goto L131
    L120:
        r16 = r2;
        r17 = r3;
    L121:
        r0.tvgId = value(r21);
        goto L131
    L122:
        r16 = r2;
        r17 = r3;
        r0.group = value(r21);
        goto L131
    L123:
        r16 = r2;
        r17 = r3;
        r0.logo = value(r21);
        goto L131
    L124:
        r16 = r2;
        r17 = r3;
        if (r0.tvgId.isEmpty() == false) goto L83;
    L127:
        r16 = r2;
        r17 = r3;
        r7 = value(r21);
        goto L131
    L128:
        r16 = r2;
        r17 = r3;
        r6 = value(r21);
        goto L131
    L129:
        r16 = r2;
        r17 = r3;
        Entry.details(r0).url = value(r21);
        goto L131
    L130:
        r16 = r2;
        r17 = r3;
        r0.title = value(r21);
        goto L131
    L15:
        if (r4.equals("url_principal") == false) goto L80;
        r13 = 5;
        goto L81
    L18:
        if (r4.equals("estado_licencia") == false) goto L80;
        r13 = 7;
        goto L81
    L21:
        if (r4.equals("tvg-name") == false) goto L80;
        r13 = 17;
        goto L81
    L24:
        if (r4.equals("tvg-logo") == false) goto L80;
        r13 = 10;
        goto L81
    L27:
        if (r4.equals("tvgId") == false) goto L80;
        r13 = 16;
        goto L81
    L30:
        if (r4.equals("title") == false) goto L80;
        r13 = 2;
        goto L81
    L33:
        if (r4.equals("grupo") == false) goto L80;
        r13 = 12;
        goto L81
    L36:
        if (r4.equals("group") == false) goto L80;
        r13 = 11;
        goto L81
    L39:
        if (r4.equals("name") == false) goto L80;
        r13 = r2;
        goto L81
    L42:
        if (r4.equals("logo") == false) goto L80;
        r13 = 9;
        goto L81
    L45:
        if (r4.equals("keys") == false) goto L80;
        r13 = 21;
        goto L81
    L48:
        if (r4.equals("url") == false) goto L80;
        r13 = 4;
        goto L81
    L51:
        if (r4.equals("kid") == false) goto L80;
        r13 = 18;
        goto L81
    L54:
        if (r4.equals("key") == false) goto L80;
        r13 = 19;
        goto L81
    L57:
        if (r4.equals("group-title") == false) goto L80;
        r13 = 13;
        goto L81
    L60:
        if (r4.equals("fileUrl") == false) goto L80;
        r13 = 3;
        goto L81
    L63:
        if (r4.equals("tvg_id") == false) goto L80;
        r13 = 14;
        goto L81
    L66:
        if (r4.equals("tvg-id") == false) goto L80;
        r13 = 15;
        goto L81
    L69:
        if (r4.equals("nombre") == false) goto L80;
        r13 = r3;
        goto L81
    L72:
        if (r4.equals("claves") == false) goto L80;
        r13 = 20;
        goto L81
    L75:
        if (r4.equals("cas_id") == false) goto L80;
        r13 = 8;
        goto L81
    L78:
        if (r4.equals("url_redireccion") == false) goto L80;
        r13 = 6;
        goto L81
    L132:
        boolean r111 = r3;
        r21.endObject();
        if (r8.isEmpty() == true) goto L139;
        if (r9.isEmpty() == true) goto L139;
        r1.add(r8.replace("-", "") + ":" + r9);     // Catch: Exception -> L173
    L139:
        if (Entry.details(r0).url.isEmpty() == false) goto L142;
        Entry.details(r0).url = r6;     // Catch: Exception -> L173
    L142:
        if (r7.equalsIgnoreCase("error") == true) goto L144;
    L148:
        URI r12 = new URI(Entry.details(r0).url);     // Catch: Exception -> L173
        if (r0.title.trim().isEmpty() == true) goto L170;
        if (r12.getHost() == null) goto L170;
        if (Arrays.asList(new String[]{"http", "https"}).contains(r12.getScheme()) == false) goto L170;
        if (r1.isEmpty() == true) goto L158;
        Entry.details(r0).license = DetailExtras$$ExternalSyntheticBackport0.m398m(",", r1);     // Catch: Exception -> L173
        ClearKeyPairs.parse(Entry.details(r0).license);     // Catch: Exception -> L173
        r0.licenseType = "clearkey";     // Catch: Exception -> L173
    L158:
        if (Entry.details(r0).url.toLowerCase(Locale.ROOT).contains(".mpd") == false) goto L178;
        r0.manifest = "mpd";     // Catch: Exception -> L173
    L178:
        if (r24.add(r0.identity()) == false) goto L4;
        r23.add(r0);     // Catch: Exception -> L171
    L170:
        throw new IllegalArgumentException();     // Catch: Exception -> L171
    L144:
        if (r1.isEmpty() == false) goto L148;
        throw new IllegalArgumentException();     // Catch: Exception -> L173
    L175:
        r25[r111 ? 1 : 0] = r25[r111 ? 1 : 0] + 1;
        goto L4
    L7:
        r21.skipValue();
        r25[0] = r25[0] + 1;
        goto L4
    L176:
        r21.endArray();
    }

    private static void categories(JsonReader r5, String r6, List<Entry> r7, Set<String> r8, int[] r9) throws IOException {
        r5.beginArray();
    L4:
        if (r5.hasNext() == false) goto L28;
        if (r5.peek() != JsonToken.BEGIN_OBJECT) goto L7;
        String r0 = "";
        int r1 = r7.size();
        r5.beginObject();
    L10:
        if (r5.hasNext() == false) goto L20;
        String r2 = r5.nextName();
        if (r2.equals("name") == true) goto L13;
        if (r2.equals("channels") == false) goto L19;
        if (r5.peek() != JsonToken.BEGIN_ARRAY) goto L19;
        array(r5, r6, r7, r8, r9);
    L19:
        r5.skipValue();
        goto L10
    L13:
        r0 = value(r5);
        goto L10
    L20:
        r5.endObject();
        int r3 = r1;
    L22:
        if (r3 >= r7.size()) goto L4;
        if (r7.get(r3).group.equals("Sin sección") == false) goto L26;
        r7.get(r3).group = r0;
    L26:
        r3 = r3 + 1;
        goto L22
    L7:
        r5.skipValue();
        goto L4
    L28:
        r5.endArray();
    }

    static M3uParser.Result read(Reader r13, String r14) throws IOException {
        BufferedReader r0 = new BufferedReader(r13);
        r0.mark(4096);
    L3:
        int r1 = r0.read();
        if (r1 < 0) goto L9;
        if (Character.isWhitespace(r1) == true) goto L3;
        if (r1 == 65279) goto L3;
    L9:
        r0.reset();
        if (r1 != 91) goto L12;
    L15:
        JsonReader r2 = new JsonReader(r0);
        ArrayList r3 = new ArrayList();
        HashSet r4 = new HashSet();
        int[] r6 = {0};
        if (r2.peek() != JsonToken.BEGIN_ARRAY) goto L18;
        array(r2, r14, r3, r4, r6);
    L43:
        if (r2.peek() != JsonToken.END_DOCUMENT) goto L47;
        return new M3uParser.Result(r3, r6[0]);
    L47:
        throw new IOException("Contenido adicional tras el JSON");
    L18:
        r2.beginObject();
        boolean r7 = false;
    L20:
        if (r2.hasNext() == false) goto L40;
        String r8 = r2.nextName();
        if (r2.peek() == JsonToken.BEGIN_ARRAY) goto L23;
        r2.skipValue();
        goto L20
    L23:
        int r9 = r3.size();
        if (r8.equals("categories") == false) goto L26;
        categories(r2, r14, r3, r4, r6);
    L28:
        if (Arrays.asList(new String[]{"channels", "canales", "entries"}).contains(r8) == true) goto L38;
        int r10 = r9;
    L31:
        if (r10 >= r3.size()) goto L38;
        if (((Entry) r3.get(r10)).group.isEmpty() == false) goto L35;
    L36:
        ((Entry) r3.get(r10)).group = r8;
    L37:
        r10 = r10 + 1;
        goto L31
    L35:
        if (((Entry) r3.get(r10)).group.equals("Sin sección") == false) goto L37;
    L38:
        r7 = true;
        goto L20
    L26:
        array(r2, r14, r3, r4, r6);
        goto L28
    L40:
        r2.endObject();
        if (r7 == true) goto L43;
        throw new IOException("JSON sin lista de canales compatible");
    L12:
        if (r1 == 123) goto L15;
        return M3uParser.read(r0, r14);
    }

    private static String value(JsonReader r2) throws IOException {
        if (r2.peek() == JsonToken.STRING) goto L10;
        if (r2.peek() == JsonToken.NUMBER) goto L10;
        r2.skipValue();
        return "";
    L10:
        return r2.nextString();
    }
}
