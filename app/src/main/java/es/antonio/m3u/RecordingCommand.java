package es.antonio.m3u;

import android.util.Base64;
import androidx.media3.common.MimeTypes;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes7.dex */
final class RecordingCommand {
    RecordingCommand() {
    }

    static String[] arguments(RecordingTask r20, RecordingProxy r21, File r22, long r23) throws Exception {
        if (r20.entry.licenseType != null) goto L5;
    L7:
        boolean r4 = false;
    L8:
        if (r4 == false) goto L35;
        if (MimeTypes.APPLICATION_MPD.equals(StreamFormat.mime(r20.entry)) == false) goto L35;
        if (r23 <= 0) goto L33;
        List<DashRecordingPlan.Track> r5 = r21.prepareDash(keys(r20.entry.license));
        ArrayList r7 = new ArrayList(Arrays.asList(new String[]{"-hide_banner", "-loglevel", "warning", "-nostdin", "-y"}));
        int r8 = 0;
    L16:
        if (r8 >= r5.size()) goto L17;
        dashClock(r7, r8);
        r7.addAll(Arrays.asList(new String[]{"-protocol_whitelist", "http,tcp,crypto", "-rw_timeout", "15000000", "-cenc_decryption_key", r5.get(r8).key, "-f", "dash", "-i", r21.trackUrl(r8)}));
        r8 = r8 + 1;
        goto L16
    L17:
        int r1 = 0;
    L19:
        if (r1 >= r5.size()) goto L20;
        StringBuilder r9 = new StringBuilder(String.valueOf(r1));
        if (r5.get(r1).kind.equals(MimeTypes.BASE_TYPE_VIDEO) == false) goto L29;
        String r10 = ":v:0";
    L30:
        r7.addAll(Arrays.asList(new String[]{"-map", r9.append(r10).toString()}));
        r1 = r1 + 1;
        goto L19
    L29:
        r10 = ":a:0";
        goto L30
    L20:
        r7.addAll(Arrays.asList(new String[]{"-c", "copy", "-t", String.format(Locale.ROOT, "%.3f", new Object[]{Double.valueOf(r23 / 1000.0d)})}));
        if (r20.format.equals("mp4") == false) goto L23;
        List r0 = Arrays.asList(new String[]{"-movflags", mp4Flags(), "-f", "mp4"});
    L24:
        r7.addAll(r0);
        r7.add(r22.getAbsolutePath());
        return (String[]) r7.toArray(new String[0]);
    L23:
        r0 = Arrays.asList(new String[]{"-f", "matroska"});
        goto L24
    L33:
        throw new IllegalArgumentException("La duración ya ha terminado");
    L35:
        return arguments(r20, r21.url(r20.entry.url), r22, r23);
    L5:
        if (r20.entry.licenseType.toLowerCase(Locale.ROOT).contains("clearkey") == false) goto L7;
        r4 = true;
        goto L8
    }

    static String[] arguments(RecordingTask r16, String r17, File r18, long r19) throws Exception {
        if (r19 <= 0) goto L23;
        ArrayList r3 = new ArrayList(Arrays.asList(new String[]{"-hide_banner", "-loglevel", "warning", "-nostdin", "-y", "-protocol_whitelist", "http,tcp,crypto", "-rw_timeout", "15000000"}));
        if (r16.entry.licenseType == null) goto L14;
        if (r16.entry.licenseType.toLowerCase(Locale.ROOT).contains("clearkey") == false) goto L14;
        Map<String, String> r4 = keys(r16.entry.license);
        if (r4.size() != 1) goto L12;
        r3.add("-decryption_key");
        r3.add(r4.values().iterator().next());
        goto L14
    L12:
        throw new IllegalArgumentException("Varias claves no admitidas");
    L14:
        if (MimeTypes.APPLICATION_M3U8.equals(StreamFormat.mime(r16.entry)) == false) goto L16;
        r3.addAll(Arrays.asList(new String[]{"-allowed_extensions", "ALL"}));
    L16:
        r3.addAll(Arrays.asList(new String[]{"-i", r17, "-map", "0:v:0?", "-map", "0:a?", "-c", "copy", "-t", String.format(Locale.ROOT, "%.3f", new Object[]{Double.valueOf(r19 / 1000.0d)})}));
        if (r16.format.equals("mp4") == false) goto L19;
        List r0 = Arrays.asList(new String[]{"-movflags", mp4Flags(), "-f", "mp4"});
    L20:
        r3.addAll(r0);
        r3.add(r18.getAbsolutePath());
        return (String[]) r3.toArray(new String[0]);
    L19:
        r0 = Arrays.asList(new String[]{"-f", "matroska"});
        goto L20
    L23:
        throw new IllegalArgumentException("La duración ya ha terminado");
    }

    static void dashClock(List<String> r1, int r2) {
        if (r2 != 0) goto L4;
        List r3 = Arrays.asList(new String[]{"-copyts", "-start_at_zero"});
    L5:
        r1.addAll(r3);
        return;
    L4:
        r3 = Arrays.asList(new String[]{"-isync", "0"});
        goto L5
    }

    private static String hex(byte[] r7) {
        if (r7.length != 16) goto L10;
        StringBuilder r0 = new StringBuilder();
        int r1 = r7.length;
        int r3 = 0;
    L5:
        if (r3 >= r1) goto L7;
        r0.append(String.format(Locale.ROOT, "%02x", new Object[]{Integer.valueOf(r7[r3] & 255)}));
        r3 = r3 + 1;
        goto L5
    L7:
        return r0.toString();
    L10:
        throw new IllegalArgumentException("Clave inválida");
    }

    static Map<String, String> keys(String r6) throws Exception {
        if (r6 == null) goto L20;
        if (r6.trim().startsWith("{") == false) goto L20;
        if (r6.contains("\"keys\"") == false) goto L20;
        LinkedHashMap r0 = new LinkedHashMap();
        JSONArray r1 = new JSONObject(r6).getJSONArray("keys");
        int r7 = 0;
    L9:
        if (r7 >= r1.length()) goto L10;
        JSONObject r2 = r1.getJSONObject(r7);
        String r3 = hex(Base64.decode(r2.getString("kid"), 8));
        String r4 = hex(Base64.decode(r2.getString("k"), 8));
        String r5 = (String) r0.put(r3, r4);
        if (r5 == null) goto L18;
        if (r5.equals(r4) == true) goto L18;
        throw new IllegalArgumentException("Claves incompatibles para un KID");
    L18:
        r7 = r7 + 1;
        goto L9
    L10:
        return r0;
    L20:
        return ClearKeyPairs.parse(r6);
    }

    static String limitation(Entry r5) {
        String r0 = URI.create(r5.url).getScheme();     // Catch: Exception -> L38
        if ("http".equalsIgnoreCase(r0) == true) goto L9;
        if ("https".equalsIgnoreCase(r0) == true) goto L9;
        return "La grabación necesita una fuente HTTP o HTTPS";
    L9:
        if (r5.licenseType != null) goto L11;
        String r1 = "";
    L13:
        if (r1.isEmpty() == true) goto L19;
        if (r1.contains("clearkey") == true) goto L19;
        return "Este motor todavía no graba contenido con licencias DRM. La reproducción sigue disponible.";
    L19:
        if (MimeTypes.APPLICATION_SS.equals(StreamFormat.mime(r5)) == false) goto L23;
        return "La grabación de ISM todavía no está implementada. La reproducción sigue disponible.";
    L23:
        if (r1.contains("clearkey") == true) goto L43;
    L37:
        return "";
    L43:
        Map<String, String> r2 = keys(r5.license);     // Catch: Exception -> L34
        if (r2.isEmpty() == false) goto L29;
        return "Faltan claves ClearKey";
    L29:
        if (r2.size() <= 1) goto L37;
        if (MimeTypes.APPLICATION_MPD.equals(StreamFormat.mime(r5)) == true) goto L37;
        return "ClearKey con varias claves necesita una fuente DASH";
    L35:
        return "No se pudo interpretar la clave ClearKey de esta fuente";
    L11:
        r1 = r5.licenseType.toLowerCase(Locale.ROOT);
    L39:
        return "La dirección del contenido no es válida";
    }

    static String mp4Flags() {
        return "+frag_keyframe+empty_moov+default_base_moof+delay_moov";
    }
}
