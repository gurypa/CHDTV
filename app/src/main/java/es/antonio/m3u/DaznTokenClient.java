package es.antonio.m3u;

import android.util.Base64;
import androidx.core.os.EnvironmentCompat;
import androidx.exifinterface.media.ExifInterface;
import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.text.Typography;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class DaznTokenClient {
    static final String DEFAULT_REFRESH_DEVICE = "002a3fd9f9";
    static final long INTERVAL = 86100000;
    static final String REFRESH = "https://ott-authz-bff-prod.ar.indazn.com/v5/RefreshAccessToken";
    static final String SCRIPT_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36";
    final MovistarTokenClient.Transport transport;

    DaznTokenClient(MovistarTokenClient.Transport r1) {
        this.transport = r1;
    }

    static long expiry(String r10) {
        String r2 = URLDecoder.decode(r10, "UTF-8");     // Catch: Exception -> L16
        ArrayList r3 = new ArrayList();     // Catch: Exception -> L16
        r3.add(r10);     // Catch: Exception -> L16
        r3.add(r2);     // Catch: Exception -> L16
        r3.addAll(Arrays.asList(r2.split("\\.")));     // Catch: Exception -> L16
        Iterator r4 = r3.iterator();     // Catch: Exception -> L16
    L5:
        if (r4.hasNext() == false) goto L17;
        long r6 = new JSONObject(new String(Base64.decode((String) r4.next(), 10), StandardCharsets.UTF_8)).optLong("exp", 0);     // Catch: Exception -> L13
        if (r6 <= 0) goto L5;
        return 1000 * r6;
    L17:
        return 0;
    }

    static Map<String, String> headers(String r3) throws Exception {
        LinkedHashMap r0 = new LinkedHashMap();
        r0.put(HttpHeaders.AUTHORIZATION, "Bearer " + r3);
        r0.put("X-BRAND", "dazn");
        r0.put(HttpHeaders.ORIGIN, "https://www.dazn.com");
        r0.put(HttpHeaders.REFERER, "https://www.dazn.com/");
        r0.put(HttpHeaders.USER_AGENT, SCRIPT_UA);
        r0.put(HttpHeaders.ACCEPT, "*/*");
        r0.put(HttpHeaders.ACCEPT_LANGUAGE, "es-ES,es;q=0.9");
        r0.put(HttpHeaders.CONTENT_TYPE, "application/json");
        String r1 = payload(r3).optString("user");
        if (r1.isEmpty() == true) goto L5;
        r0.put("X-DAZNID", r1);
    L5:
        return r0;
    }

    static long next(JSONObject r13) {
        long r3 = r13.optLong("renewed", 0);
        if (r3 != 0) goto L5;
        return 0;
    L5:
        long r5 = expiry(r13.optString("token"));
        long r7 = r13.optLong("expires", 0);
        if (r7 > 0) goto L8;
    L12:
        long r11 = r3 + INTERVAL;
        if (r5 <= 0) goto L15;
        long r0 = r5 - 300000;
    L17:
        return Math.min(r11, r0);
    L15:
        r0 = r3 + INTERVAL;
        goto L17
    L8:
        if (r5 <= 0) goto L10;
        long r9 = Math.min(r5, r7);
    L11:
        r5 = r9;
        goto L12
    L10:
        r9 = r7;
        goto L11
    }

    static JSONObject payload(String r5) throws Exception {
        String[] r0 = r5.split("\\.");
        if (r0.length < 2) goto L7;
        return new JSONObject(new String(Base64.decode(r0[1], 10), StandardCharsets.UTF_8));
    L7:
        throw new IOException("Sesión DAZN no válida");
    }

    static Map<String, String> playbackHeaders(String r6) throws Exception {
        LinkedHashMap r0 = new LinkedHashMap();
        Map<String, String> r1 = headers(r6);
        Iterator r2 = Arrays.asList(new String[]{HttpHeaders.ORIGIN, HttpHeaders.REFERER, HttpHeaders.USER_AGENT, HttpHeaders.AUTHORIZATION}).iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        String r3 = (String) r2.next();
        r0.put(r3, r1.get(r3));
        goto L4
    L6:
        r0.put("x-dazn-device", payload(r6).getString("deviceId"));
        r0.put(HttpHeaders.ACCEPT, "*/*");
        r0.put(HttpHeaders.ACCEPT_LANGUAGE, "es-ES,es;q=0.9");
        return r0;
    }

    JSONObject playback(String r28, String r29) throws Exception {
        if (r29.isEmpty() == true) goto L34;
        Map<String, String> r0 = playbackHeaders(r28);
        Map<String, String> r1 = MovistarTokenClient.map(new String[]{"AppVersion", "0.60.0", "DrmType", "WIDEVINE", "Format", "MPEG-DASH", "PlayerId", "@dazn/peng-html5-core/web/web", "Platform", "web", "LanguageCode", "es", ExifInterface.TAG_MODEL, EnvironmentCompat.MEDIA_UNKNOWN, "Secure", "true", "Manufacturer", "microsoft", "PlayReadyInitiator", "false", "Capabilities", "mta", "AssetId", r29, "MtaLanguageCode", ""});
        StringBuilder r2 = new StringBuilder("https://api.playback.indazn.com/v5/Playback?");
        Iterator<Map.Entry<String, String>> r3 = r1.entrySet().iterator();
    L6:
        if (r3.hasNext() == false) goto L8;
        Map.Entry<String, String> r4 = r3.next();
        r2.append(MovistarTokenClient.encode(r4.getKey())).append('=').append(MovistarTokenClient.encode(r4.getValue())).append(Typography.amp);
        goto L6
    L8:
        r2.setLength(r2.length() - 1);
        JSONObject r5 = new JSONObject(this.transport.call("GET", r2.toString(), r0, null));
        String r6 = r5.optString("_chdtvBrowserUserAgent", r0.get(HttpHeaders.USER_AGENT));
        JSONObject r7 = r5.getJSONArray("PlaybackDetails").getJSONObject(0);
        String r8 = r7.getString("ManifestUrl");
        String r9 = r7.getJSONObject("CdnToken").getString("Value");
        String r10 = r7.getJSONObject("CdnToken").getString("Name");
        URI r11 = new URI(r8);
        if ("https".equalsIgnoreCase(r11.getScheme()) == false) goto L32;
        if (r11.getHost() == null) goto L32;
        if (r11.getUserInfo() != null) goto L32;
        if (r10.equalsIgnoreCase("dazn-token") == false) goto L32;
        if (r9.isEmpty() == true) goto L32;
        if (r9.indexOf(13) >= 0) goto L32;
        if (r9.indexOf(10) >= 0) goto L32;
        long r12 = expiry(r9);
        if (r12 <= 0) goto L30;
        if (r12 > (System.currentTimeMillis() + 30000)) goto L30;
        throw new IOException("DAZN devolvió un token CDN caducado");
    L30:
        return new JSONObject().put("url", r8).put("token", r9).put("userAgent", r6).put("renewed", System.currentTimeMillis());
    L32:
        throw new IOException("Respuesta CDN de DAZN no compatible");
    L34:
        throw new IOException("Falta AssetId");
    }

    String refresh(String r2) throws Exception {
        return refresh(r2, DEFAULT_REFRESH_DEVICE);
    }

    String refresh(String r9, String r10) throws Exception {
        payload(r9);
        if (r10.isEmpty() == true) goto L10;
        String r1 = new JSONObject(this.transport.call("POST", REFRESH, headers(r9), new JSONObject().put("DeviceId", r10).toString())).getJSONObject("AuthToken").getString("Token");
        if ((payload(r1).optLong("exp", 0) * 1000) <= (System.currentTimeMillis() + 30000)) goto L8;
        return r1;
    L8:
        throw new IOException("DAZN devolvió una sesión caducada");
    L10:
        throw new IOException("La sesión DAZN no incluye DeviceId");
    }
}
