package es.antonio.m3u;

import com.arthenica.ffmpegkit.MediaInformation;
import com.google.common.net.HttpHeaders;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class DropboxBackupClient {
    static final int CHUNK = 8388608;
    final JSONObject account;
    final Transport http;
    String token;

    interface Progress {
        void update(long r1, long r3);
    }

    static final class Response {
        final String body;
        final int code;

        Response(int r1, String r2) {
            this.code = r1;
            this.body = r2;
        }
    }

    static final class Result {
        String link;
        String path;
        String warning;

        Result() {
            this.link = "";
            this.warning = "";
        }
    }

    interface Transport {
        Response post(String r1, Map<String, String> r2, byte[] r3) throws Exception;
    }

    DropboxBackupClient(JSONObject r2) {
        this(r2, new DropboxBackupClient$$ExternalSyntheticLambda0());
    }

    DropboxBackupClient(JSONObject r1, Transport r2) {
        this.account = r1;
        this.http = r2;
    }

    static String ascii(String r9) {
        StringBuilder r0 = new StringBuilder();
        char[] r1 = r9.toCharArray();
        int r2 = r1.length;
        int r4 = 0;
    L3:
        if (r4 >= r2) goto L10;
        char r5 = r1[r4];
        if (r5 <= 127) goto L7;
        r0.append(String.format(Locale.ROOT, "\\u%04x", new Object[]{Integer.valueOf(r5)}));
    L8:
        r4 = r4 + 1;
        goto L3
    L7:
        r0.append(r5);
        goto L8
    L10:
        return r0.toString();
    }

    static String downloadLink(String r4) throws Exception {
        URI r0 = new URI(r4);
        if ("https".equals(r0.getScheme()) == false) goto L20;
        if (r0.getHost() == null) goto L20;
        if (r0.getHost().equals("dropbox.com") == true) goto L11;
        if (r0.getHost().endsWith(".dropbox.com") == false) goto L20;
    L11:
        if (r4.matches(".*[?&]dl=[^&#]*.*") == true) goto L13;
        StringBuilder r1 = new StringBuilder().append(r4);
        String r2 = "?";
        if (r4.contains("?") == false) goto L18;
        r2 = "&";
    L18:
        return r1.append(r2).append("dl=1").toString();
    L13:
        return r4.replaceAll("([?&])dl=[^&#]*", "$1dl=1");
    L20:
        throw new IOException("Enlace Dropbox no válido");
    }

    static String encode(String r1) throws Exception {
        return URLEncoder.encode(r1, "UTF-8");
    }

    static String path(String r6) throws IOException {
        if (r6 == null) goto L29;
        if (r6.startsWith("/") == false) goto L29;
        if (r6.endsWith("/") == true) goto L29;
        if (r6.length() > 1024) goto L29;
        if (r6.indexOf(10) >= 0) goto L29;
        if (r6.indexOf(13) >= 0) goto L29;
        int r2 = 0;
        if (r6.indexOf(0) >= 0) goto L29;
        String[] r1 = r6.substring(1).split("/", -1);
        int r3 = r1.length;
    L17:
        if (r2 >= r3) goto L27;
        String r4 = r1[r2];
        if (r4.isEmpty() == true) goto L26;
        if (r4.equals(".") == true) goto L26;
        if (r4.equals("..") == true) goto L26;
        r2 = r2 + 1;
    L26:
        throw new IOException("Ruta Dropbox no válida");
    L27:
        return r6;
    L29:
        throw new IOException("Ruta Dropbox no válida");
    }

    static Response post(String r8, Map<String, String> r9, byte[] r10) throws Exception {
        HttpURLConnection r0 = (HttpURLConnection) new URL(r8).openConnection();
        r0.setInstanceFollowRedirects(false);
        r0.setConnectTimeout(15000);
        r0.setReadTimeout(60000);
        r0.setRequestMethod("POST");
        r0.setDoOutput(true);
        r0.setFixedLengthStreamingMode(r10.length);
        Iterator<Map.Entry<String, String>> r1 = r9.entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L49;
        Map.Entry<String, String> r2 = r1.next();
        r0.setRequestProperty(r2.getKey(), r2.getValue());
        goto L4
    L49:
        OutputStream r3 = r0.getOutputStream();     // Catch: Throwable -> L45
        r3.write(r10);     // Catch: Throwable -> L38
        if (r3 == null) goto L10;
        r3.close();     // Catch: Throwable -> L45
    L10:
        int r4 = r0.getResponseCode();     // Catch: Throwable -> L45
        if (r4 >= 200) goto L13;
    L15:
        InputStream r5 = r0.getErrorStream();     // Catch: Throwable -> L45
    L16:
        String r6 = "";
        if (r5 == null) goto L35;
        InputStream r7 = r5;
        byte[] r11 = readChunk(r7, 1048577);     // Catch: Throwable -> L28
        if (r11.length > 1048576) goto L27;
        r6 = new String(r11, StandardCharsets.UTF_8);     // Catch: Throwable -> L28
        if (r7 == null) goto L35;
        r7.close();     // Catch: Throwable -> L45
        goto L35
    L27:
        throw new IOException("Respuesta Dropbox demasiado grande");     // Catch: Throwable -> L28
    L28:
        th = move-exception;
        if (r7 != null) goto L52;
    L34:
        throw th;     // Catch: Throwable -> L45
    L52:
        r7.close();     // Catch: Throwable -> L32
    L32:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L45
    L35:
        Response r12 = new Response(r4, r6);     // Catch: Throwable -> L45
        r0.disconnect();
        return r12;
    L13:
        if (r4 >= 300) goto L15;
        r5 = r0.getInputStream();     // Catch: Throwable -> L45
        goto L16
    L38:
        th = move-exception;
        if (r3 != null) goto L54;
    L44:
        throw th;     // Catch: Throwable -> L45
    L54:
        r3.close();     // Catch: Throwable -> L42
    L42:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L45
    L45:
        th = move-exception;
        r0.disconnect();
        throw th;
    }

    static byte[] readChunk(InputStream r4, int r5) throws IOException {
        ByteArrayOutputStream r0 = new ByteArrayOutputStream();
        byte[] r1 = new byte[65536];
    L4:
        if (r0.size() >= r5) goto L10;
        int r2 = r4.read(r1, 0, Math.min(r1.length, r5 - r0.size()));
        if (r2 < 0) goto L10;
        r0.write(r1, 0, r2);
    L10:
        return r0.toByteArray();
    }

    static JSONObject success(Response r3) throws Exception {
        if (r3.code != 200) goto L10;
        if (r3.body.trim().equals("null") == false) goto L8;
        return new JSONObject();
    L8:
        return new JSONObject(r3.body);
    L10:
        throw new IOException("Dropbox: operación fallida (HTTP " + r3.code + "). Comprueba permisos, conexión y espacio.");
    }

    Response call(String r7, JSONObject r8, byte[] r9, boolean r10) throws Exception {
        if (this.token != null) goto L5;
        refresh();
    L5:
        int r0 = 0;
    L7:
        if (r0 >= 2) goto L29;
        HashMap r1 = new HashMap();
        r1.put(HttpHeaders.AUTHORIZATION, "Bearer " + this.token);
        if (r10 == false) goto L11;
        String r2 = "application/octet-stream";
    L12:
        r1.put(HttpHeaders.CONTENT_TYPE, r2);
        if (r10 == false) goto L15;
        r1.put("Dropbox-API-Arg", ascii(r8.toString()));
    L15:
        Transport r3 = this.http;
        StringBuilder r4 = new StringBuilder();
        if (r10 == false) goto L18;
        String r5 = "https://content.dropboxapi.com/2/";
    L19:
        String r6 = r4.append(r5).append(r7).toString();
        if (r10 == false) goto L22;
        byte[] r11 = r9;
    L23:
        Response r12 = r3.post(r6, r1, r11);
        if (r12.code != 401) goto L27;
        if (r0 != 0) goto L27;
        refresh();
        r0 = r0 + 1;
    L27:
        return r12;
    L22:
        r11 = r8.toString().getBytes(StandardCharsets.UTF_8);
        goto L23
    L18:
        r5 = "https://api.dropboxapi.com/2/";
        goto L19
    L11:
        r2 = "application/json";
        goto L12
    L29:
        throw new IOException("Dropbox: autorización caducada");
    }

    String link(String r8) throws Exception {
        Response r0 = call("sharing/create_shared_link_with_settings", new JSONObject().put("path", r8).put("settings", new JSONObject().put("requested_visibility", "public")), null, false);
        if (r0.code != 200) goto L7;
        return downloadLink(new JSONObject(r0.body).getString("url"));
    L7:
        if (r0.code != 409) goto L19;
        if (r0.body.contains("shared_link_already_exists") == false) goto L19;
        JSONArray r2 = success(call("sharing/list_shared_links", new JSONObject().put("path", r8).put("direct_only", true), null, false)).getJSONArray("links");
        int r3 = 0;
    L12:
        if (r3 >= r2.length()) goto L19;
        JSONObject r4 = r2.getJSONObject(r3);
        if (r4.optString("path_lower", r8).equalsIgnoreCase(r8) == true) goto L16;
        r3 = r3 + 1;
        goto L12
    L16:
        return downloadLink(r4.getString("url"));
    L19:
        throw new IOException("Dropbox no devolvió enlace");
    }

    void refresh() throws Exception {
        String r0 = "grant_type=refresh_token&refresh_token=" + encode(this.account.getString("refresh")) + "&client_id=" + encode(this.account.getString("key"));
        String r1 = this.account.optString("secret");
        if (r1.isEmpty() == true) goto L5;
        r0 = r0 + "&client_secret=" + encode(r1);
    L5:
        HashMap r2 = new HashMap();
        r2.put(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        Response r3 = this.http.post("https://api.dropboxapi.com/oauth2/token", r2, r0.getBytes(StandardCharsets.UTF_8));
        if (r3.code != 200) goto L13;
        this.token = new JSONObject(r3.body).getString("access_token");
        if (this.token.isEmpty() == true) goto L11;
        return;
    L11:
        throw new IOException("Dropbox no devolvió autorización");
    L13:
        throw new IOException("Dropbox: no se pudo renovar la autorización (HTTP " + r3.code + ")");
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 5 */
    Result upload(File r26, boolean r27, Progress r28) throws Exception {
        Progress r2 = r28;
        String r0 = "session_id";
        String r4 = path(this.account.getString("path"));
        JSONObject r5 = new JSONObject().put("path", r4).put("mode", "overwrite").put("autorename", false).put("mute", true);
        Result r6 = new Result();
        r6.path = r4;
        long r9 = r26.length();
        JSONObject r13 = null;
        FileInputStream r14 = new FileInputStream(r26);
        if (r9 > 8388608) goto L99;
        byte[] r1 = readChunk(r14, 8388608);     // Catch: Throwable -> L6
        JSONObject r15 = success(call("files/upload", r5, r1, true));     // Catch: Throwable -> L6
        long r11 = r1.length;     // Catch: Throwable -> L6
        r2.update(r11, r9);     // Catch: Throwable -> L6
    L58:
        r14.close();
        if (r15 == null) goto L71;
        if (r11 != r9) goto L71;
        if (r15.getLong(MediaInformation.KEY_SIZE) != r9) goto L71;
        if (r27 == true) goto L97;
    L69:
        return r6;
    L97:
        r6.link = link(r4);     // Catch: Exception -> L67
    L68:
        r6.warning = "Copia subida; no se pudo crear el enlace. Comprueba permisos de compartir.";
    L71:
        throw new IOException("Dropbox no confirmó el tamaño de la copia");
    L6:
        th = move-exception;
        Throwable r3 = th;
    L87:
        r14.close();     // Catch: Throwable -> L82
        throw r3;
    L82:
        th = move-exception;
        r3.addSuppressed(th);
        throw r3;
    L99:
        String r7 = success(call("files/upload_session/start", new JSONObject().put("close", false), new byte[0], true)).getString("session_id");     // Catch: Throwable -> L76
        long r12 = 0;
    L13:
        if (r12 >= r9) goto L55;
        byte[] r16 = readChunk(r14, 8388608);     // Catch: Throwable -> L53
        JSONObject r18 = r13;
    L51:
        th = move-exception;
        r3 = th;
        goto L87
    L18:
        if (r16.length == 0) goto L50;
        String r20 = r0;
        JSONObject r8 = new JSONObject().put(r0, r7).put("offset", r12);     // Catch: Throwable -> L51
        long r21 = r12;
        if ((r21 + ((long) r16.length)) != r9) goto L23;
        boolean r17 = true;
    L103:
        JSONObject r19 = new JSONObject().put("cursor", r8);     // Catch: Throwable -> L74
        if (r17 == false) goto L27;
        r19.put("commit", r5);     // Catch: Throwable -> L74
    L28:
        if (r17 == false) goto L30;
        String r110 = "files/upload_session/finish";
    L31:
        JSONObject r111 = success(call(r110, r19, r16, true));     // Catch: Throwable -> L74
        String r23 = r7;
        long r10 = r21 + ((long) r16.length);
        r2.update(r10, r9);     // Catch: Throwable -> L46
        if (r17 == true) goto L89;
        r13 = r18;
    L45:
        r2 = r28;
        r12 = r10;
        r0 = r20;
        r7 = r23;
        goto L13
    L89:
        r6.path = r111.optString("path_display", r4);     // Catch: Throwable -> L40
        r13 = r111;
    L40:
        th = move-exception;
        r3 = th;
    L46:
        th = move-exception;
        r3 = th;
        goto L87
    L30:
        r110 = "files/upload_session/append_v2";
        goto L31
    L27:
        r19.put("close", false);     // Catch: Throwable -> L74
    L74:
        th = move-exception;
        r3 = th;
        goto L87
    L23:
        r17 = false;
        goto L103
    L50:
        throw new IOException("La copia cambió durante la subida");     // Catch: Throwable -> L74
    L53:
        th = move-exception;
        r3 = th;
        goto L87
    L55:
        long r22 = r12;
        JSONObject r112 = r13;
        if (r22 != r9) goto L73;
        r15 = r112;
        r11 = r22;
        goto L58
    L73:
        throw new IOException("Subida incompleta");     // Catch: Throwable -> L74
    L76:
        th = move-exception;
        r3 = th;
    L78:
        th = move-exception;
        r3 = th;
        goto L87
    }
}
