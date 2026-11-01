package es.antonio.m3u;

import android.os.Build;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class UpdateManifest {
    final String apkUrl;
    final String notes;
    final String packageName;
    final String sha256;
    final int versionCode;
    final String versionName;

    private UpdateManifest(int r1, String r2, String r3, String r4, String r5, String r6) {
        this.versionCode = r1;
        this.versionName = r2;
        this.packageName = r3;
        this.apkUrl = r4;
        this.sha256 = r5;
        this.notes = r6;
    }

    static String downloadUrl(String r4) throws IOException {
        https(r4);
        String r0 = URI.create(r4).getHost();
        if ("dropbox.com".equalsIgnoreCase(r0) == true) goto L9;
        if ("www.dropbox.com".equalsIgnoreCase(r0) == true) goto L9;
        return r4;
    L9:
        if (r4.matches(".*[?&]dl=[^&]*.*") == true) goto L11;
        StringBuilder r1 = new StringBuilder().append(r4);
        String r2 = "?";
        if (r4.contains("?") == false) goto L16;
        r2 = "&";
    L16:
        return r1.append(r2).append("dl=1").toString();
    L11:
        return r4.replaceAll("([?&])dl=[^&]*", "$1dl=1");
    }

    static String https(String r3) throws IOException {
        URI r0 = new URI(r3);     // Catch: URISyntaxException -> L13
        if ("https".equalsIgnoreCase(r0.getScheme()) == false) goto L12;
        if (r0.getHost() == null) goto L12;
        if (r0.getUserInfo() != null) goto L12;
        if (r0.getFragment() != null) goto L12;
        return r3;
    L12:
        throw new IOException("El enlace debe ser HTTPS");     // Catch: URISyntaxException -> L13
    L13:
        e = move-exception;
        throw new IOException("Enlace incorrecto", e);
    }

    static UpdateManifest parse(String r1, String r2, int r3) throws IOException {
        return parse(r1, r2, r3, Build.SUPPORTED_ABIS);
    }

    static UpdateManifest parse(String r12, String r13, int r14, String[] r15) throws IOException {
        JSONObject r0 = new JSONObject(r12);     // Catch: JSONException -> L37
        long r1 = r0.getLong("versionCode");     // Catch: JSONException -> L37
        if (r1 < 1) goto L36;
        if (r1 > 2147483647L) goto L36;
        String r3 = r0.getString("packageName");     // Catch: JSONException -> L37
        if (r13.equals(r3) == false) goto L34;
        if (r0.optInt("minSdk", 23) > r14) goto L32;
        JSONObject r4 = r0;
        JSONObject r5 = r0.optJSONObject("variants");     // Catch: JSONException -> L37
        if (r5 == null) goto L19;
        if (r15 == null) goto L19;
        int r6 = r15.length;     // Catch: JSONException -> L37
        int r7 = 0;
    L14:
        if (r7 >= r6) goto L19;
        String r8 = r15[r7];     // Catch: JSONException -> L37
        if (r5.has(r8) == true) goto L17;
        r7 = r7 + 1;     // Catch: JSONException -> L37
        goto L14
    L17:
        r4 = r5.getJSONObject(r8);     // Catch: JSONException -> L37
    L19:
        String r9 = r4.getString("sha256").toLowerCase(Locale.ROOT);     // Catch: JSONException -> L37
        if (r9.matches("[0-9a-f]{64}") == false) goto L30;
        String r10 = r0.getString("versionName");     // Catch: JSONException -> L37
        String r11 = r0.optString("notes", "");     // Catch: JSONException -> L37
        if (r10.length() > 100) goto L28;
        if (r11.length() > 4000) goto L28;
        return new UpdateManifest((int) r1, r10, r3, downloadUrl(r4.getString("apkUrl")), r9, r11);
    L28:
        throw new IOException("Información de versión demasiado larga");     // Catch: JSONException -> L37
    L30:
        throw new IOException("Falta la comprobación de integridad");     // Catch: JSONException -> L37
    L32:
        throw new IOException("Esta versión necesita un Android más reciente");     // Catch: JSONException -> L37
    L34:
        throw new IOException("La actualización pertenece a otra aplicación");     // Catch: JSONException -> L37
    L36:
        throw new IOException("Versión incorrecta");     // Catch: JSONException -> L37
    L37:
        e = move-exception;
        throw new IOException("No se pudo leer la información de actualización", e);
    }

    boolean newerThan(long r3) {
        if (this.versionCode <= r3) goto L5;
        return true;
    L5:
        return false;
    }
}
