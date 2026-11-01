package es.antonio.m3u;

import android.util.JsonReader;
import android.util.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;

/* JADX INFO: loaded from: classes3.dex */
final class PlexImport {

    static final class Server implements Serializable {
        private static final long serialVersionUID = 1;
        final String base;
        final String name;
        final String token;

        Server(String r1, String r2, String r3) {
            this.name = r1;
            this.base = r2;
            this.token = r3;
        }
    }

    PlexImport() {
    }

    static ArrayList<Server> read(Reader r19) throws IOException {
        String r2 = "";
        JsonReader r0 = new JsonReader(r19);
        LinkedHashMap r1 = new LinkedHashMap();
        r0.beginObject();
    L4:
        if (r0.hasNext() == false) goto L100;
        if (Arrays.asList(new String[]{"movies", "series", "artists", "photos", "conexiones_locales"}).contains(r0.nextName()) == true) goto L8;
    L98:
        String r16 = r2;
        r0.skipValue();
    L99:
        r2 = r16;
        goto L4
    L8:
        if (r0.peek() != JsonToken.BEGIN_ARRAY) goto L98;
        r0.beginArray();
    L11:
        if (r0.hasNext() == false) goto L97;
        if (r0.peek() != JsonToken.BEGIN_OBJECT) goto L14;
        String r7 = "";
        String r8 = "";
        String r9 = "";
        String r10 = "";
        r0.beginObject();
        String r14 = "";
    L16:
        byte r11 = -1;
        if (r0.hasNext() == false) goto L47;
        String r3 = r0.nextName();
        switch(r3.hashCode()) {
            case -1826110354: goto L37;
            case -1039904823: goto L34;
            case -977328275: goto L31;
            case -599309097: goto L28;
            case 3367: goto L25;
            case 110541305: goto L22;
            default: goto L39;
        };
    L39:
        switch(r11) {
            case 0: goto L45;
            case 1: goto L44;
            case 2: goto L43;
            case 3: goto L42;
            case 4: goto L41;
            case 5: goto L41;
            default: goto L40;
        };
    L41:
        r10 = value(r0);
        goto L16
    L42:
        r9 = value(r0);
        goto L16
    L43:
        r8 = value(r0);
        goto L16
    L44:
        r7 = value(r0);
        goto L16
    L45:
        r14 = value(r0);
        goto L16
    L40:
        r0.skipValue();
        goto L16
    L22:
        if (r3.equals("token") == false) goto L39;
        r11 = 3;
        goto L39
    L25:
        if (r3.equals("ip") == false) goto L39;
        r11 = 0;
        goto L39
    L28:
        if (r3.equals("protocolo") == false) goto L39;
        r11 = 2;
        goto L39
    L31:
        if (r3.equals("puerto") == false) goto L39;
        r11 = 1;
        goto L39
    L34:
        if (r3.equals("nombre") == false) goto L39;
        r11 = 5;
        goto L39
    L37:
        if (r3.equals("serverName") == false) goto L39;
        r11 = 4;
        goto L39
    L47:
        r0.endObject();
        String r12 = r8.replace("://", r2).replace(":", r2).toLowerCase(Locale.ROOT);     // Catch: Exception -> L89 IOException -> L91
        if (r12.isEmpty() == true) goto L51;
    L52:
        String r13 = r12;
        if (r7.isEmpty() == false) goto L56;
        int r15 = -1;
    L57:
        if (r15 < (-1)) goto L82;
        if (r15 == 0) goto L82;
        if (r15 > 65535) goto L82;
        if (r9.trim().isEmpty() == true) goto L82;
        String r4 = new URI(r13, null, r14, r15, null, null, null).toString();     // Catch: Exception -> L83 IOException -> L86
        PlexClient.endpoint(r4, "/library/sections");     // Catch: Exception -> L83 IOException -> L86
        String r17 = r4 + "\n" + r9;     // Catch: Exception -> L83 IOException -> L86
        if (r1.containsKey(r17) == false) goto L67;
        r2 = r2;
    L73:
        if (r1.size() > 1000) goto L76;
    L95:
        r2 = r2;
        goto L11
    L76:
        throw new IOException("Demasiados servidores Plex");     // Catch: Exception -> L77 IOException -> L79
    L79:
        e = e;
    L94:
        if (e.getMessage().equals("Demasiados servidores Plex") == false) goto L95;
        throw e;
    L67:
        if (r10.isEmpty() == false) goto L69;
        String r5 = r14;
    L110:
        r1.put(r17, new Server(r5, r4, r9));     // Catch: Exception -> L77 IOException -> L79
        goto L73
    L69:
        r5 = r10;
    L82:
        r2 = r2;
        goto L11
    L56:
        r15 = Integer.parseInt(r7);     // Catch: Exception -> L83 IOException -> L86
        goto L57
    L86:
        e = e;
        r2 = r2;
    L84:
        r2 = r2;
        goto L95
    L51:
        r12 = "http";
        goto L52
    L91:
        e = e;
        r2 = r2;
    L90:
        r2 = r2;
        goto L95
    L14:
        r0.skipValue();
        goto L11
    L97:
        r16 = r2;
        r0.endArray();
        goto L99
    L100:
        r0.endObject();
        if (r0.peek() != JsonToken.END_DOCUMENT) goto L109;
        if (r1.isEmpty() == true) goto L107;
        return new ArrayList(r1.values());
    L107:
        throw new IOException("JSON sin conexiones Plex compatibles");
    L109:
        throw new IOException("Contenido adicional tras el JSON");
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
