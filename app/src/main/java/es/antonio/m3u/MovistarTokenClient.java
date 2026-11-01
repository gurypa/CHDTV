package es.antonio.m3u;

import android.util.Base64;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.common.net.HttpHeaders;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
class MovistarTokenClient {
    static final String BASE = "https://soter-pf.sve.video.telefonicaservices.com/service/";

    /* JADX INFO: renamed from: UA */
    static final String f186UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.0.0 Safari/537.36";
    String consumerKey;
    String consumerSecret;
    private final Transport transport;

    static final class Http implements Transport {
        Http() {
        }

        @Override // es.antonio.m3u.MovistarTokenClient.Transport
        public String call(String r10, String r11, Map<String, String> r12, String r13) throws Exception {
            HttpURLConnection r0 = (HttpURLConnection) new URL(r11).openConnection();
            r0.setConnectTimeout(15000);
            r0.setReadTimeout(AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH);
            r0.setInstanceFollowRedirects(false);
            r0.setRequestMethod(r10);
            Iterator<Map.Entry<String, String>> r2 = r12.entrySet().iterator();
        L4:
            if (r2.hasNext() == false) goto L6;
            Map.Entry<String, String> r3 = r2.next();
            r0.setRequestProperty(r3.getKey(), r3.getValue());
            goto L4
        L6:
            if (r13 != null) goto L62;
        L20:
            int r4 = r0.getResponseCode();     // Catch: Throwable -> L56
            if (r4 < 200) goto L55;
            if (r4 >= 300) goto L55;
            InputStream r5 = r0.getInputStream();     // Catch: Throwable -> L56
            ByteArrayOutputStream r6 = new ByteArrayOutputStream();     // Catch: Throwable -> L47
            byte[] r7 = new byte[8192];     // Catch: Throwable -> L41
        L28:
            int r8 = r5.read(r7);     // Catch: Throwable -> L41
            if (r8 == (-1)) goto L35;
            if ((r6.size() + r8) > 2097152) goto L34;
            r6.write(r7, 0, r8);     // Catch: Throwable -> L41
            goto L28
        L34:
            throw new IOException("Respuesta demasiado grande");     // Catch: Throwable -> L41
        L35:
            String r1 = r6.toString("UTF-8");     // Catch: Throwable -> L41
            r6.close();     // Catch: Throwable -> L47
            if (r5 == null) goto L39;
            r5.close();     // Catch: Throwable -> L56
        L39:
            r0.disconnect();
            return r1;
        L41:
            th = move-exception;
            r6.close();     // Catch: Throwable -> L44
        L46:
            throw th;     // Catch: Throwable -> L47
        L44:
            th = move-exception;
            th.addSuppressed(th);     // Catch: Throwable -> L47
        L47:
            th = move-exception;
            if (r5 != null) goto L60;
        L53:
            throw th;     // Catch: Throwable -> L56
        L60:
            r5.close();     // Catch: Throwable -> L51
        L51:
            th = move-exception;
            th.addSuppressed(th);     // Catch: Throwable -> L56
        L56:
            th = move-exception;
            r0.disconnect();
            throw th;
        L55:
            throw new IOException("HTTP " + r4 + " · " + new URL(r11).getPath());     // Catch: Throwable -> L56
        L62:
            r0.setDoOutput(true);     // Catch: Throwable -> L56
            byte[] r9 = r13.getBytes(StandardCharsets.UTF_8);     // Catch: Throwable -> L56
            r0.setFixedLengthStreamingMode(r9.length);     // Catch: Throwable -> L56
            OutputStream r14 = r0.getOutputStream();     // Catch: Throwable -> L56
            r14.write(r9);     // Catch: Throwable -> L13
            if (r14 == null) goto L20;
            r14.close();     // Catch: Throwable -> L56
            goto L20
        L13:
            th = move-exception;
            if (r14 != null) goto L63;
        L19:
            throw th;     // Catch: Throwable -> L56
        L63:
            r14.close();     // Catch: Throwable -> L17
        L17:
            th = move-exception;
            th.addSuppressed(th);     // Catch: Throwable -> L56
            goto L19
        }
    }

    static final class Result {
        final String token;
        final String warning;

        Result(String r1, String r2) {
            this.token = r1;
            this.warning = r2;
        }
    }

    interface Transport {
        String call(String r1, String r2, Map<String, String> r3, String r4) throws Exception;
    }

    MovistarTokenClient(Transport r1) {
        this.transport = r1;
    }

    static String b64(byte[] r1) {
        return Base64.encodeToString(r1, 2);
    }

    static String decode(String r2) {
        return URLDecoder.decode(r2.replace("+", "%2B"), "UTF-8");
    L4:
        e = move-exception;
        throw new IllegalArgumentException(e);
    }

    static String decrypt(JSONArray r18, String r19) throws Exception {
        StringBuilder r0 = new StringBuilder();
        int r1 = 0;
    L4:
        if (r1 >= r18.length()) goto L31;
        JSONArray r3 = r18.getJSONArray(r1);
        StringBuilder r4 = new StringBuilder();
        boolean r6 = true;
        int r5 = r3.length() - 1;
    L6:
        if (r5 < 0) goto L8;
        r4.append(r3.getString(r5));
        r5 = r5 - 1;
        goto L6
    L8:
        byte[] r7 = new byte[r4.length() / 2];
        int r8 = 0;
    L10:
        if (r8 >= r7.length) goto L12;
        r7[r8] = (byte) Integer.parseInt(r4.substring(r8 * 2, (r8 * 2) + 2), 16);
        r8 = r8 + 1;
        goto L10
    L12:
        byte[] r9 = Base64.decode(r7, 0);
        if (r9.length >= 16) goto L15;
    L17:
        r6 = false;
    L18:
        if (r6 == false) goto L20;
        byte[] r11 = Arrays.copyOfRange(r9, 8, 16);
    L21:
        ByteArrayOutputStream r12 = new ByteArrayOutputStream();
        byte[] r13 = new byte[0];
    L23:
        if (r12.size() >= 48) goto L25;
        MessageDigest r14 = MessageDigest.getInstance("MD5");
        r14.update(r13);
        r14.update(r19.getBytes(StandardCharsets.UTF_8));
        r13 = r14.digest(r11);
        r12.write(r13);
        goto L23
    L25:
        byte[] r15 = r12.toByteArray();
        Cipher r10 = Cipher.getInstance("AES/CBC/PKCS5Padding");
        int r17 = r1;
        r10.init(2, new SecretKeySpec(Arrays.copyOfRange(r15, 0, 32), "AES"), new IvParameterSpec(Arrays.copyOfRange(r15, 32, 48)));
        if (r6 == false) goto L28;
        int r2 = 16;
    L29:
        r0.append(new String(r10.doFinal(Arrays.copyOfRange(r9, r2, r9.length)), StandardCharsets.UTF_8));
        r1 = r17 + 1;
        goto L4
    L28:
        r2 = 0;
        goto L29
    L20:
        r11 = new byte[0];
        goto L21
    L15:
        if (new String(r9, 0, 8, StandardCharsets.US_ASCII).equals("Salted__") == false) goto L17;
    L31:
        return r0.toString();
    }

    static String encode(String r3) {
        return URLEncoder.encode(r3, "UTF-8").replace("+", "%20").replace("%7E", "~").replace("*", "%2A");
    L4:
        e = move-exception;
        throw new IllegalArgumentException(e);
    }

    static Map<String, String> map(String... r4) {
        LinkedHashMap r0 = new LinkedHashMap();
        int r1 = 0;
    L4:
        if (r1 >= r4.length) goto L6;
        r0.put(r4[r1], r4[r1 + 1]);
        r1 = r1 + 2;
        goto L4
    L6:
        return r0;
    }

    String cleanup(String r20, String r21, String r22) {
        if (r22.isEmpty() == false) goto L6;
        return "No se recibió autorización para cerrar el dispositivo";
    L6:
        String r1 = "https://clientservices.dof6.com/movistarplus/accounts/" + encode(r20) + "/devices/" + encode(r21);
        ArrayList r0 = new ArrayList();
        request("DELETE", r1 + "/sessions?qspVersion=ssp", r22, "Bearer", Collections.emptyMap(), null, false);     // Catch: Exception -> L9
    L19:
        request("DELETE", r1 + "?qspVersion=ssp", r22, "Bearer", Collections.emptyMap(), null, false);     // Catch: Exception -> L13
    L16:
        return DetailExtras$$ExternalSyntheticBackport0.m398m(" · ", r0);
    L14:
        r0.add("No se pudo eliminar el dispositivo temporal");
    L10:
        r0.add("No se pudo cerrar la sesión");
        goto L19
    }

    Map<String, String> headers() {
        return map(new String[]{HttpHeaders.USER_AGENT, f186UA, HttpHeaders.ACCEPT, "application/json, text/plain, */*", HttpHeaders.ORIGIN, "https://ver.movistarplus.es", HttpHeaders.REFERER, "https://ver.movistarplus.es/", HttpHeaders.ACCEPT_LANGUAGE, "es-ES,es;q=0.9", "sec-ch-ua-platform", "\"Windows\"", "sec-ch-ua-mobile", "?0", "sec-ch-ua", "\"Google Chrome\";v=\"149\", \"Chromium\";v=\"149\", \"Not)A;Brand\";v=\"24\"", HttpHeaders.SEC_FETCH_SITE, "cross-site", HttpHeaders.SEC_FETCH_MODE, "cors", HttpHeaders.SEC_FETCH_DEST, "empty"});
    }

    void loadKeys() throws Exception {
        Matcher r1 = Pattern.compile("yomvi\\.setupConf\\((.*?)\\)\\s*;", 32).matcher(this.transport.call("GET", "https://ver.movistarplus.es/feed/conf.php?id=all", headers(), null));
        if (r1.find() == false) goto L7;
        JSONObject r2 = new JSONObject(r1.group(1)).getJSONObject("config");
        String r3 = b64((r2.get("version").toString() + r2.getString("name") + r2.get("timestamp").toString() + r2.getString("language")).getBytes(StandardCharsets.UTF_8));
        JSONObject r4 = r2.getJSONObject("ua");
        this.consumerKey = decrypt(new JSONArray(r4.getString("query")), r3);
        this.consumerSecret = decrypt(new JSONArray(r4.getString("param")), r3);
        return;
    L7:
        throw new IOException("Configuración web no compatible");
    }

    Result renew(String r28, String r29) throws Exception {
        loadKeys();
        String r11 = UUID.randomUUID().toString().replace("-", "");
        String r12 = "";
        String r13 = "";
        Map<String, String> r6 = map(new String[]{"grant_type", "password", "username", r28, "password", r29, "scope", "api", "client_id", "491aafabb70d4644954d8fb21e0b93b1", "client_secret", "secret"});     // Catch: Throwable -> L34
        ArrayList r1 = new ArrayList();     // Catch: Throwable -> L34
        Iterator<Map.Entry<String, String>> r2 = r6.entrySet().iterator();     // Catch: Throwable -> L34
    L5:
        if (r2.hasNext() == false) goto L7;
        Map.Entry<String, String> r3 = r2.next();     // Catch: Throwable -> L34
        r1.add(encode(r3.getKey()) + "=" + encode(r3.getValue()));     // Catch: Throwable -> L34
        goto L5
    L7:
        String r4 = b64((r11 + ":webplayer").getBytes(StandardCharsets.UTF_8));     // Catch: Throwable -> L34
        String r7 = DetailExtras$$ExternalSyntheticBackport0.m398m("&", r1);     // Catch: Throwable -> L34
        MovistarTokenClient r5 = this;
    L32:
        th = th;
    L37:
        if (r12.isEmpty() == true) goto L39;
        r5.cleanup(r12, r11, r13);
    L39:
        throw th;
    L11:
        String r8 = new JSONObject(r5.request("POST", "https://soter-pf.sve.video.telefonicaservices.com/service/oidc/connect/token?deviceClass=webplayer", r4, "Basic", r6, r7, true)).getString("access_token");     // Catch: Throwable -> L34
        r12 = new JSONObject(request("GET", "https://soter-pf.sve.video.telefonicaservices.com/service/login/webplayer/accountInfo?legacy=true", r8, "Bearer", Collections.emptyMap(), null, false)).getJSONArray("ofertas").getJSONObject(0).get("accountNumber").toString();     // Catch: Throwable -> L34
        request("PUT", "https://soter-pf.sve.video.telefonicaservices.com/service/login/devices?deviceId=" + encode(r11) + "&deviceType=webplayer&accountId=" + encode(r12), r8, "Bearer", Collections.emptyMap(), null, false);     // Catch: Throwable -> L32
        JSONObject r10 = new JSONObject(request("POST", "https://soter-pf.sve.video.telefonicaservices.com/service/login/initData?legacy=true", r8, "Bearer", Collections.emptyMap(), new JSONObject().put("accountNumber", r12).put("sessionUserProfile", 0).put("isKidProfile", false).put("streamFormat", "DASH").put("streamDRM", "Widevine").put("streamMiscellanea", "HTTPS").put("deviceManufacturerProduct", "Chrome").put("deviceId", r11).put("deviceType", "webplayer").toString(), false));     // Catch: Throwable -> L34
        r13 = r10.optString("legacyAccessToken");     // Catch: Throwable -> L34
        String r0 = new JSONObject(request("POST", "https://soter-pf.sve.video.telefonicaservices.com/service/tcdnidserver/" + encode(r12) + "/devices/webplayer/cdn/token/refresh", r10.getString("accessToken"), "Bearer", Collections.emptyMap(), "{}", false)).getString("access_token");     // Catch: Throwable -> L32
        if (r0.isEmpty() == true) goto L31;
        Result r9 = new Result(r0, cleanup(r12, r11, r13));     // Catch: Throwable -> L32
        if ("".isEmpty() == true) goto L29;
        cleanup("", r11, r13);
    L29:
        return r9;
    L31:
        throw new IOException("Renovación sin token");     // Catch: Throwable -> L32
    L34:
        th = th;
        r5 = this;
        goto L37
    }

    String request(String r16, String r17, String r18, String r19, Map<String, String> r20, String r21, boolean r22) throws Exception {
        Map<String, String> r9 = headers();
        if (r22 == false) goto L5;
        String r0 = "application/x-www-form-urlencoded";
    L6:
        r9.put(HttpHeaders.CONTENT_TYPE, r0);
        if (r17.contains("/login/initData") == false) goto L9;
        int r1 = 10;
    L10:
        int r10 = r1;
        SecureRandom r2 = new SecureRandom();
        StringBuilder r3 = new StringBuilder();
        int r4 = 0;
    L11:
        if (r4 >= r10) goto L13;
        r3.append("abcdefghijklmnopqrstuvwxyz0123456789".charAt(r2.nextInt("abcdefghijklmnopqrstuvwxyz0123456789".length())));
        r4 = r4 + 1;
        goto L11
    L13:
        r3.append(System.currentTimeMillis());
        r9.put(HttpHeaders.AUTHORIZATION, "OPPlus " + r19 + "=" + r18 + ",Signature=" + signature(r16, r17, r18, r19, r20, b64(r3.toString().getBytes(StandardCharsets.UTF_8)), System.currentTimeMillis() / 1000));
        return this.transport.call(r16, r17, r9, r21);
    L9:
        r1 = 11;
        goto L10
    L5:
        r0 = "application/json";
        goto L6
    }

    String signature(String r17, String r18, String r19, String r20, Map<String, String> r21, String r22, long r23) throws Exception {
        URI r2 = URI.create(r18);
        TreeMap r3 = new TreeMap(r21);
        String r5 = r2.getRawQuery();
        if (r5 == null) goto L11;
        String[] r7 = r5.split("&");
        int r8 = r7.length;
        char r9 = 0;
        int r10 = 0;
    L5:
        if (r10 >= r8) goto L11;
        String[] r12 = r7[r10].split("=", 2);
        String r13 = URLDecoder.decode(r12[r9], "UTF-8");
        if (r12.length <= 1) goto L9;
        String r11 = r12[1];
    L10:
        r3.put(r13, URLDecoder.decode(r11, "UTF-8"));
        r10 = r10 + 1;
        r9 = 0;
        goto L5
    L9:
        r11 = "";
    L11:
        r3.put(r20, r19);
        r3.put("consumer_key", this.consumerKey);
        r3.put("nonce", r22);
        r3.put("signature_method", "HMAC-SHA1");
        r3.put("timestamp", Long.toString(r23));
        r3.put("version", "1.0");
        ArrayList r14 = new ArrayList();
        Iterator r15 = r3.entrySet().iterator();
    L13:
        if (r15.hasNext() == false) goto L18;
        Map.Entry r16 = (Map.Entry) r15.next();
        String r110 = decode((String) r16.getValue());
        if (r110.contains("@") == false) goto L17;
        r110 = encode(r110);
    L17:
        r14.add(encode((String) r16.getKey()) + "%3D" + encode(r110));
        goto L13
    L18:
        String r6 = r17.toUpperCase(Locale.ROOT) + "&" + encode(r2.getRawAuthority() + r2.getRawPath()) + "&" + DetailExtras$$ExternalSyntheticBackport0.m398m("%26", r14);
        Mac r111 = Mac.getInstance("HmacSHA1");
        r111.init(new SecretKeySpec(this.consumerSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
        return b64(("consumer_key=\"" + this.consumerKey + "\",nonce=\"" + r22 + "\",signature_method=\"HMAC-SHA1\",signature=\"" + b64(r111.doFinal(r6.getBytes(StandardCharsets.UTF_8))) + "\",timestamp=\"" + r23 + "\",version=\"1.0\"").getBytes(StandardCharsets.UTF_8));
    }
}
