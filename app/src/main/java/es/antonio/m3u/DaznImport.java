package es.antonio.m3u;

import android.content.Context;
import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class DaznImport {
    static final int LIMIT = 2097152;

    private DaznImport() {
    }

    static String prepare(String r19, boolean r20) throws Exception {
        String r2 = "cdn_token_value";
        String r3 = "cdn_token_name";
        if (r19 == null) goto L68;
        if (r19.getBytes(StandardCharsets.UTF_8).length > 2097152) goto L68;
        String r5 = r19.trim();
        if (r5.startsWith("\ufeff") == false) goto L10;
        r5 = r5.substring(1).trim();
    L10:
        if (r5.startsWith("<") == true) goto L66;
        String r11 = "asset_id";
        if (r20 == true) goto L69;
    L18:
        if (r20 == false) goto L20;
    L26:
        String r18 = "asset_id";
        JSONObject r0 = new JSONObject(r5);     // Catch: JSONException -> L16
    L27:
        if (r20 == true) goto L29;
        JSONArray r1 = r0.getJSONArray("categories");     // Catch: JSONException -> L16
        int r4 = 0;
        int r6 = 0;
    L35:
        if (r6 >= r1.length()) goto L53;
        JSONArray r7 = r1.getJSONObject(r6).getJSONArray("channels");     // Catch: JSONException -> L16
        int r8 = 0;
    L38:
        if (r8 >= r7.length()) goto L52;
        JSONObject r9 = r7.getJSONObject(r8);     // Catch: JSONException -> L16
        String r10 = r18;
        if (r9.optString(r10).isEmpty() == true) goto L77;
        if (r9.optString("name").isEmpty() == true) goto L77;
        if (r9.optString("fileUrl").isEmpty() == true) goto L77;
        r4 = r4 + 1;     // Catch: JSONException -> L16
        if (r4 > 500) goto L49;
        r8 = r8 + 1;     // Catch: JSONException -> L16
        r18 = r10;
        goto L38
    L49:
        throw new IOException("Máximo 500 canales para renovación");     // Catch: JSONException -> L16
    L77:
        throw new IOException("El JSON necesita name, fileUrl y asset_id en cada canal");     // Catch: JSONException -> L16
    L52:
        r6 = r6 + 1;
        goto L35
    L53:
        if (r4 == 0) goto L58;
    L56:
        return r0.toString();
    L58:
        throw new IOException("El mapa no contiene canales");     // Catch: JSONException -> L16
    L29:
        if (DaznTokenClient.payload(r0.getJSONObject("AuthToken").getString("Token")).optString("deviceId").isEmpty() == false) goto L56;
        throw new IOException("La sesión no incluye DeviceId");     // Catch: JSONException -> L16
    L20:
        if (r5.startsWith("[") == false) goto L26;
        JSONArray r13 = new JSONArray(r5);     // Catch: JSONException -> L16
        JSONArray r14 = new JSONArray();     // Catch: JSONException -> L16
        int r15 = 0;
    L23:
        if (r15 >= r13.length()) goto L25;
        JSONObject r12 = r13.getJSONObject(r15);     // Catch: JSONException -> L16
        JSONArray r16 = r13;
        int r17 = r15;
        String r110 = r11;
        JSONObject r111 = new JSONObject().put("name", r12.getString("name")).put(r11, r12.getString(r11)).put("fileUrl", r12.optString("url_raw", r12.optString("url"))).put("tvg_id", r12.optString("tvg_id")).put(r3, r12.optString(r3)).put(r2, r12.optString(r2));     // Catch: JSONException -> L16
        String r112 = r2;
        r14.put(r111.put("cdn_expires", r12.optLong("cdn_expires", 0)));     // Catch: JSONException -> L16
        r2 = r112;
        r3 = r3;
        r13 = r16;
        r11 = r110;
        r15 = r17 + 1;     // Catch: JSONException -> L16
        goto L23
    L25:
        r18 = r11;
        r0 = new JSONObject().put("categories", new JSONArray().put(new JSONObject().put("name", "DAZN TV").put("channels", r14)));     // Catch: JSONException -> L16
        goto L27
    L69:
        if (r5.startsWith("{") == true) goto L18;
        DaznTokenClient.payload(r5);     // Catch: JSONException -> L16
        r0 = new JSONObject().put("AuthToken", new JSONObject().put("Token", r5));     // Catch: JSONException -> L16
        r18 = "asset_id";
        goto L27
    L60:
        if (r20 == false) goto L62;
        String r21 = "Sesión no válida: usa auto_token.txt o el token de sesión completo";
    L64:
        throw new IOException(r21);
    L62:
        r21 = "Mapa no válido: usa Dazn.json con categories, channels y AssetId";
        goto L64
    L66:
        throw new IOException("El enlace devuelve una página web, no el archivo");
    L68:
        throw new IOException("El contenido supera 2 MB");
    }

    static String prepareMap(Context r17, String r18) throws Exception {
        if (r18 != null) goto L5;
        String r1 = "";
    L7:
        if (r1.startsWith("\ufeff") == false) goto L10;
        r1 = r1.substring(1).trim();
    L10:
        if (r1.startsWith("#EXTM3U") == true) goto L16;
        if (r1.startsWith("#EXTINF:") == true) goto L16;
        return prepare(r18, false);
    L16:
        if (r18.getBytes(StandardCharsets.UTF_8).length > 2097152) goto L50;
        if (DaznTokens.configured(r17) == false) goto L48;
        List<Entry> r2 = M3uParser.parse(new StringReader(r1), "Canales");
        JSONArray r4 = new JSONArray();
        HashSet r5 = new HashSet();
        Iterator<Entry> r6 = r2.iterator();
    L21:
        if (r6.hasNext() == false) goto L42;
        Entry r7 = r6.next();
        JSONObject r10 = DaznTokens.match(r17, r7);
        if (r10 == null) goto L40;
        String r11 = r10.getString("asset");
        if (r5.add(r11) == false) goto L38;
        String r12 = "";
        Iterator<Map.Entry<String, String>> r13 = r7.headers.entrySet().iterator();
    L28:
        if (r13.hasNext() == false) goto L33;
        Map.Entry<String, String> r14 = r13.next();
        if (r14.getKey().equalsIgnoreCase("dazn-token") == false) goto L28;
        r12 = r14.getValue();
        goto L28
    L33:
        JSONObject r3 = new JSONObject().put("name", r7.title).put("asset_id", r11).put("fileUrl", Entry.details(r7).url).put("tvg_id", r7.tvgId);
        if (r12.isEmpty() == true) goto L36;
        r3.put("cdn_token_name", "dazn-token").put("cdn_token_value", Uri.encode(r12));
    L36:
        r4.put(r3);
        goto L21
    L38:
        throw new IOException("El M3U repite un canal del mapa DAZN");
    L40:
        throw new IOException("El M3U contiene canales sin correspondencia única en el mapa DAZN");
    L42:
        if (r4.length() == 0) goto L46;
        return prepare(new JSONObject().put("categories", new JSONArray().put(new JSONObject().put("name", "DAZN TV").put("channels", r4))).toString(), false);
    L46:
        throw new IOException("El mapa no contiene canales");
    L48:
        throw new IOException("El M3U no incluye AssetId: importa primero Dazn.json o Dazn_streams.json para renovar sus canales");
    L50:
        throw new IOException("El contenido supera 2 MB");
    L5:
        r1 = r18.trim();
        goto L7
    }

    static String read(InputStream r6) throws IOException {
        Reader r0 = PlaylistInput.reader(r6);
        StringBuilder r1 = new StringBuilder();     // Catch: Throwable -> L15
        char[] r2 = new char[8192];     // Catch: Throwable -> L15
    L4:
        int r3 = r0.read(r2);     // Catch: Throwable -> L15
        if (r3 == (-1)) goto L11;
        if ((r1.length() + r3) > 2097152) goto L10;
        r1.append(r2, 0, r3);     // Catch: Throwable -> L15
        goto L4
    L10:
        throw new IOException("El contenido supera 2 MB");     // Catch: Throwable -> L15
    L11:
        String r4 = r1.toString();     // Catch: Throwable -> L15
        if (r0 == null) goto L14;
        r0.close();
    L14:
        return r4;
    L15:
        th = move-exception;
        if (r0 != null) goto L23;
    L21:
        throw th;
    L23:
        r0.close();     // Catch: Throwable -> L19
    L19:
        th = move-exception;
        th.addSuppressed(th);
        goto L21
    }
}
