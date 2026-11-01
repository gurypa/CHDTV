package es.antonio.chdtv.playback.sampleaes;

import android.net.Uri;
import android.util.Base64;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import com.google.common.base.Ascii;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes5.dex */
public final class SampleAesKeys implements SampleAesManifest.KeyResolver {
    private static final Pattern UUID = null;
    private final Map<String, byte[]> keys;
    private final DataSource.Factory network;

    static {
        UUID = Pattern.compile("(?i)(?<![0-9a-f])(?:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}|[0-9a-f]{32})(?![0-9a-f])");
    }

    public SampleAesKeys(byte[] r5, DataSource.Factory r6) throws IOException {
        this.keys = new LinkedHashMap();
        this.network = r6;
        if (r5 != null) goto L37;
        return;
    L37:
        JSONArray r7 = new JSONObject(new String(r5, StandardCharsets.UTF_8)).getJSONArray("keys");     // Catch: Exception -> L34
        if (r7.length() == 0) goto L33;
        if (r7.length() > 64) goto L33;
        int r8 = 0;
    L11:
        if (r8 >= r7.length()) goto L31;
        JSONObject r0 = r7.getJSONObject(r8);     // Catch: Exception -> L34
        if ("oct".equals(r0.getString("kty")) == false) goto L29;
        byte[] r1 = decode(r0.getString("kid"));     // Catch: Exception -> L34
        byte[] r2 = decode(r0.getString("k"));     // Catch: Exception -> L34
        if (r1.length != 16) goto L27;
        if (r2.length != 16) goto L27;
        byte[] r3 = this.keys.put(hex(r1), r2);     // Catch: Exception -> L34
        if (r3 == null) goto L25;
        if (Arrays.equals(r3, r2) == true) goto L25;
        throw new IllegalArgumentException();     // Catch: Exception -> L34
    L25:
        r8 = r8 + 1;     // Catch: Exception -> L34
    L27:
        throw new IllegalArgumentException();     // Catch: Exception -> L34
    L29:
        throw new IllegalArgumentException();     // Catch: Exception -> L34
    L31:
        return;
    L33:
        throw new IllegalArgumentException();     // Catch: Exception -> L34
    L36:
        throw new IOException("Sample-AES: configuración de claves locales inválida");
    }

    private static byte[] decode(String r2) {
        return Base64.decode(r2.replace('-', '+').replace('_', '/'), 0);
    }

    private static String hex(byte[] r6) {
        char[] r0 = "0123456789abcdef".toCharArray();
        StringBuilder r1 = new StringBuilder(r6.length * 2);
        int r2 = r6.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L6;
        byte r4 = r6[r3];
        r1.append(r0[(r4 & 255) >>> 4]);
        r1.append(r0[r4 & Ascii.f88SI]);
        r3 = r3 + 1;
        goto L3
    L6:
        return r1.toString();
    }

    @Override // es.antonio.chdtv.playback.sampleaes.SampleAesManifest.KeyResolver
    public SampleAesManifest.KeyMaterial resolve(String r11, Map<String, String> r12) throws IOException {
        if (r12.containsKey("KEYFORMAT") == false) goto L5;
        String r1 = r12.get("KEYFORMAT");
    L6:
        String r2 = r12.get("URI");
        if (r2 == null) goto L74;
        if (r2.length() > 8192) goto L74;
        String r13 = r12.get("KEYID");
        if ("com.apple.streamingkeydelivery".equals(r1) == false) goto L33;
        if (r2.startsWith("skd://") == false) goto L31;
        r13 = Uri.decode(r2.substring(6));
        if (UUID.matcher(r13).matches() == false) goto L76;
        byte[] r0 = null;
    L36:
        if (r13 != null) goto L42;
        Matcher r8 = UUID.matcher(r2);
        if (r8.find() == false) goto L42;
        String r9 = r8.group().replace("-", "").toLowerCase(Locale.ROOT);
        if (this.keys.containsKey(r9) == false) goto L42;
        r13 = r9;
    L42:
        if (r13 == null) goto L50;
        byte[] r14 = this.keys.get(r13.replace("-", "").replaceFirst("(?i)^0x", "").toLowerCase(Locale.ROOT));
        if (r14 == null) goto L48;
        return new SampleAesManifest.KeyMaterial(r14, r0);
    L48:
        throw new IOException("Sample-AES: no hay una clave local para el KID del manifiesto");
    L50:
        if ("identity".equals(r1) == false) goto L70;
        String r15 = SampleAesManifest.resolveUrl(r11, r2);
        DataSource r16 = this.network.createDataSource();
        r16.open(new DataSpec.Builder().setUri(r15).build());     // Catch: Throwable -> L66
        ByteArrayOutputStream r17 = new ByteArrayOutputStream(16);     // Catch: Throwable -> L66
        byte[] r3 = new byte[17];     // Catch: Throwable -> L66
    L53:
        int r4 = r16.read(r3, 0, 17);     // Catch: Throwable -> L66
        if (r4 == (-1)) goto L63;
        if (r4 == 0) goto L62;
        if ((r17.size() + r4) > 16) goto L60;
        r17.write(r3, 0, r4);     // Catch: Throwable -> L66
        goto L53
    L60:
        throw new IOException("Sample-AES: clave HTTP inválida");     // Catch: Throwable -> L66
    L62:
        throw new IOException("Sample-AES: descarga de clave sin progreso");     // Catch: Throwable -> L66
    L63:
        SampleAesManifest.KeyMaterial r5 = new SampleAesManifest.KeyMaterial(r17.toByteArray(), null);     // Catch: Throwable -> L66
        r16.close();
        return r5;
    L66:
        th = move-exception;
        r16.close();
        throw th;
    L70:
        throw new IOException("Sample-AES: falta el KID del manifiesto");
    L76:
        JSONObject r6 = new JSONObject(new String(decode(r13), StandardCharsets.UTF_8));     // Catch: Exception -> L27
        r13 = r6.getString("KeyId");     // Catch: Exception -> L27
        if (r6.has("IV") == false) goto L24;
        r0 = decode(r6.getString("IV"));     // Catch: Exception -> L27
        if (r0.length == 16) goto L36;
        throw new IllegalArgumentException();     // Catch: Exception -> L27
    L24:
        r0 = null;
    L29:
        throw new IOException("Sample-AES: metadatos SKD inválidos");
    L31:
        throw new IOException("Sample-AES: referencia SKD inválida");
    L33:
        if ("identity".equals(r1) == false) goto L72;
        r0 = null;
        goto L36
    L72:
        throw new IOException("Sample-AES: KEYFORMAT no compatible con el motor local");
    L74:
        throw new IOException("Sample-AES: falta la referencia de clave");
    L5:
        r1 = "identity";
        goto L6
    }
}
