package es.antonio.chdtv.playback.sampleaes;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes5.dex */
public final class SampleAesManifest {
    private final Map<String, List<Segment>> byUrl;
    private final KeyResolver resolver;
    private final Map<String, List<List<Segment>>> snapshots;

    public static final class KeyMaterial {

        /* JADX INFO: renamed from: iv */
        final byte[] f169iv;
        final byte[] key;

        public KeyMaterial(byte[] r3, byte[] r4) throws IOException {
            if (r3 == null) goto L16;
            if (r3.length != 16) goto L16;
            if (r4 != null) goto L8;
        L9:
            this.key = (byte[]) r3.clone();
            if (r4 != null) goto L12;
            byte[] r5 = null;
        L13:
            this.f169iv = r5;
            return;
        L12:
            r5 = (byte[]) r4.clone();
            goto L13
        L8:
            if (r4.length == 16) goto L9;
        L16:
            throw new IOException("Sample-AES: clave o IV inválidos");
        }
    }

    public interface KeyResolver {
        KeyMaterial resolve(String r1, Map<String, String> r2) throws IOException;
    }

    public static final class Segment {
        public final long diagnosticDiscontinuity;
        public final int diagnosticIvSource;
        public final long diagnosticSequence;

        /* JADX INFO: renamed from: iv */
        final byte[] f170iv;
        final byte[] key;
        public final long length;
        public final long offset;
        public final String url;

        Segment(String r14, long r15, long r17, KeyMaterial r19, byte[] r20) {
            this(r14, r15, r17, r19, r20, -1, 0, 0);
        }

        Segment(String r1, long r2, long r4, KeyMaterial r6, byte[] r7, long r8, long r10, int r12) {
            this.diagnosticSequence = r8;
            this.diagnosticDiscontinuity = r10;
            this.diagnosticIvSource = r12;
            this.url = r1;
            this.offset = r2;
            this.length = r4;
            byte[] r3 = null;
            if (r6 != null) goto L5;
            byte[] r5 = null;
        L6:
            this.key = r5;
            if (r7 == null) goto L10;
            r3 = (byte[]) r7.clone();
        L10:
            this.f170iv = r3;
            return;
        L5:
            r5 = (byte[]) r6.key.clone();
            goto L6
        }

        boolean contains(long r7, long r9) {
            if (r7 >= this.offset) goto L6;
            return false;
        L6:
            if (this.length != (-1)) goto L8;
            return true;
        L8:
            long r8 = r7 - this.offset;
            if (r8 < this.length) goto L15;
            if (r8 == this.length) goto L13;
            return false;
        L13:
            if (r9 == 0) goto L15;
            return false;
        L15:
            return true;
        }

        public boolean encrypted() {
            if (this.key == null) goto L5;
            return true;
        L5:
            return false;
        }

        boolean sameCrypto(Segment r3) {
            if (Arrays.equals(this.key, r3.key) == true) goto L5;
        L7:
            return false;
        L5:
            if (Arrays.equals(this.f170iv, r3.f170iv) == false) goto L7;
            return true;
        }
    }

    public SampleAesManifest(KeyResolver r2) {
        this.snapshots = new LinkedHashMap();
        this.byUrl = new LinkedHashMap();
        this.resolver = r2;
    }

    public static Map<String, String> attributes(String r7) throws IOException {
        LinkedHashMap r0 = new LinkedHashMap();
        int r1 = 0;
    L4:
        if (r1 >= r7.length()) goto L72;
    L6:
        if (r1 >= r7.length()) goto L10;
        if (Character.isWhitespace(r7.charAt(r1)) == false) goto L10;
        r1 = r1 + 1;
    L10:
        int r2 = r1;
    L12:
        if (r2 >= r7.length()) goto L19;
        if (r7.charAt(r2) == '=') goto L19;
        if (r7.charAt(r2) == ',') goto L19;
        r2 = r2 + 1;
    L19:
        if (r2 == r7.length()) goto L71;
        if (r7.charAt(r2) != '=') goto L71;
        int r3 = r2 + 1;
        String r4 = r7.substring(r1, r2).trim();
    L24:
        if (r3 >= r7.length()) goto L29;
        if (Character.isWhitespace(r7.charAt(r3)) == false) goto L29;
        r3 = r3 + 1;
    L29:
        if (r3 < r7.length()) goto L31;
    L43:
        int r5 = r3;
    L45:
        if (r5 >= r7.length()) goto L49;
        if (r7.charAt(r5) == ',') goto L49;
        r5 = r5 + 1;
    L49:
        String r6 = r7.substring(r3, r5).trim();
    L51:
        if (r4.isEmpty() == true) goto L69;
        if (r0.put(r4, r6) != null) goto L69;
    L55:
        if (r5 >= r7.length()) goto L60;
        if (Character.isWhitespace(r7.charAt(r5)) == false) goto L60;
        r5 = r5 + 1;
    L60:
        if (r5 < r7.length()) goto L61;
        r1 = r5;
        goto L4
    L61:
        r1 = r5 + 1;
        if (r7.charAt(r5) == ',') goto L4;
        throw new IOException("Sample-AES: separador inválido");
    L69:
        throw new IOException("Sample-AES: atributo duplicado");
    L31:
        if (r7.charAt(r3) != '\"') goto L43;
        int r8 = r3 + 1;
        int r9 = r8;
    L34:
        if (r9 >= r7.length()) goto L39;
        if (r7.charAt(r9) == '\"') goto L39;
        r9 = r9 + 1;
    L39:
        if (r9 == r7.length()) goto L42;
        r5 = r9 + 1;
        r6 = r7.substring(r8, r9);
        goto L51
    L42:
        throw new IOException("Sample-AES: comillas sin cerrar");
    L71:
        throw new IOException("Sample-AES: atributos inválidos");
    L72:
        return r0;
    }

    private static long diagnosticNumber(String r2) {
        return Long.parseLong(r2.trim());
    L5:
        return -1;
    }

    private synchronized boolean hasEncryptedSnapshot(String r3) {
        monitor-enter(this);
        List<List<Segment>> r4 = this.snapshots.get(r3);     // Catch: Throwable -> L19
        if (r4 == null) goto L16;
        Iterator<List<Segment>> r5 = r4.iterator();     // Catch: Throwable -> L19
    L7:
        if (r5.hasNext() == false) goto L16;
        Iterator<Segment> r0 = r5.next().iterator();     // Catch: Throwable -> L19
    L10:
        if (r0.hasNext() == false) goto L7;
        if (r0.next().encrypted() == false) goto L10;
        monitor-exit(this);
        return true;
    L16:
        monitor-exit(this);
        return false;
    L19:
        th = move-exception;
        throw th;
    }

    public static byte[] hexIv(String r5) throws IOException {
        if (r5 == null) goto L14;
        if (r5.matches("0[xX][0-9a-fA-F]{1,32}") == false) goto L14;
        String r6 = r5.substring(2);
    L7:
        if (r6.length() >= 32) goto L9;
        r6 = "0" + r6;
        goto L7
    L9:
        byte[] r1 = new byte[16];
        int r2 = 0;
    L10:
        if (r2 >= 16) goto L12;
        int r3 = r2 * 2;
        r1[r2] = (byte) Integer.parseInt(r6.substring(r3, r3 + 2), 16);
        r2 = r2 + 1;
        goto L10
    L12:
        return r1;
    L14:
        throw new IOException("Sample-AES: IV inválido");
    }

    private static long number(String r4) throws IOException {
        long r0 = Long.parseLong(r4.trim());     // Catch: NumberFormatException -> L7
        if (r0 < 0) goto L6;
        return r0;
    L6:
        throw new NumberFormatException();     // Catch: NumberFormatException -> L7
    L9:
        throw new IOException("Sample-AES: número inválido");
    }

    private synchronized void publish(String r12, List<Segment> r13) throws IOException {
        monitor-enter(this);
    L70:
        th = move-exception;
        throw th;
    L4:
        if (this.snapshots.containsKey(r12) == false) goto L6;
    L10:
        Iterator<Segment> r0 = r13.iterator();     // Catch: Throwable -> L70
    L12:
        if (r0.hasNext() == false) goto L25;
        Segment r1 = r0.next();     // Catch: Throwable -> L70
        Segment r2 = find(r1.url, r1.offset, r1.length);     // Catch: Throwable -> L70
        if (r2 == null) goto L12;
        if (r2.offset != r1.offset) goto L12;
        if (r2.length != r1.length) goto L12;
        if (r2.sameCrypto(r1) == true) goto L12;
        throw new IOException("Sample-AES: una URL de segmento se reutilizó con otro cifrado");     // Catch: Throwable -> L70
    L25:
        LinkedHashMap r3 = new LinkedHashMap();     // Catch: Throwable -> L70
        Iterator<Segment> r4 = r13.iterator();     // Catch: Throwable -> L70
    L27:
        if (r4.hasNext() == false) goto L36;
        Segment r5 = r4.next();     // Catch: Throwable -> L70
        Segment r6 = (Segment) r3.put(r5.url + "\n" + r5.offset + ":" + r5.length, r5);     // Catch: Throwable -> L70
        if (r6 == null) goto L27;
        if (r6.sameCrypto(r5) == true) goto L27;
        throw new IOException("Sample-AES: una URL de segmento se reutilizó con otro cifrado");     // Catch: Throwable -> L70
    L36:
        List<List<Segment>> r7 = this.snapshots.get(r12);     // Catch: Throwable -> L70
        if (r7 != null) goto L39;
        r7 = new ArrayList();     // Catch: Throwable -> L70
        this.snapshots.put(r12, r7);     // Catch: Throwable -> L70
    L39:
        r7.add(0, Collections.unmodifiableList(r13));     // Catch: Throwable -> L70
        if (r7.size() <= 3) goto L42;
        r7.remove(3);     // Catch: Throwable -> L70
    L42:
        this.byUrl.clear();     // Catch: Throwable -> L70
        Iterator<List<List<Segment>>> r14 = this.snapshots.values().iterator();     // Catch: Throwable -> L70
    L44:
        if (r14.hasNext() == false) goto L68;
        Iterator<List<Segment>> r8 = r14.next().iterator();     // Catch: Throwable -> L70
    L47:
        if (r8.hasNext() == false) goto L44;
        Iterator<Segment> r9 = r8.next().iterator();     // Catch: Throwable -> L70
    L50:
        if (r9.hasNext() == false) goto L47;
        Segment r10 = r9.next();     // Catch: Throwable -> L70
        List<Segment> r11 = this.byUrl.get(r10.url);     // Catch: Throwable -> L70
        if (r11 != null) goto L54;
        r11 = new ArrayList();     // Catch: Throwable -> L70
        this.byUrl.put(r10.url, r11);     // Catch: Throwable -> L70
    L54:
        Iterator<Segment> r15 = r11.iterator();     // Catch: Throwable -> L70
        boolean r16 = false;
    L56:
        if (r15.hasNext() == false) goto L65;
        Segment r17 = r15.next();     // Catch: Throwable -> L70
        if (r17.offset != r10.offset) goto L56;
        if (r17.length != r10.length) goto L56;
        if (r17.sameCrypto(r10) == false) goto L56;
        r16 = true;
        goto L56
    L65:
        if (r16 == true) goto L50;
        r11.add(r10);     // Catch: Throwable -> L70
        goto L50
    L68:
        monitor-exit(this);
        return;
    L6:
        if (this.snapshots.size() < 64) goto L10;
        throw new IOException("Sample-AES: demasiadas pistas HLS");     // Catch: Throwable -> L70
    }

    public static String resolveUrl(String r1, String r2) throws IOException {
        if (r2.contains("{$") == true) goto L17;
        URI r3 = new URI(r1).resolve(new URI(r2));     // Catch: URISyntaxException -> L13
        if ("http".equalsIgnoreCase(r3.getScheme()) == true) goto L12;
        if ("https".equalsIgnoreCase(r3.getScheme()) == true) goto L12;
        throw new IOException("Sample-AES: se necesita una URL HTTP/HTTPS");     // Catch: URISyntaxException -> L13
    L12:
        return r3.toString();
    L15:
        throw new IOException("Sample-AES: URL inválida");
    L17:
        throw new IOException("Sample-AES: variables de URL no compatibles");
    }

    private static byte[] sequenceIv(long r4) {
        byte[] r0 = new byte[16];
        int r1 = 15;
    L4:
        if (r1 < 8) goto L6;
        r0[r1] = (byte) r4;
        r4 = r4 >>> 8;
        r1 = r1 - 1;
        goto L4
    L6:
        return r0;
    }

    public synchronized Segment find(String r7, long r8, long r10) throws IOException {
        monitor-enter(this);
        List<Segment> r9 = this.byUrl.get(r7);     // Catch: Throwable -> L26
        Segment r0 = null;
        if (r9 != null) goto L8;
        monitor-exit(this);
        return null;
    L8:
        Iterator<Segment> r11 = r9.iterator();     // Catch: Throwable -> L26
    L10:
        if (r11.hasNext() == false) goto L24;
        Segment r1 = r11.next();     // Catch: Throwable -> L26
        if (r1.contains(r8, r10) == false) goto L10;
        if (r0 != null) goto L15;
    L23:
        r0 = r1;
        goto L10
    L15:
        if (r0.offset != r1.offset) goto L22;
        if (r0.length != r1.length) goto L22;
        if (r0.sameCrypto(r1) == true) goto L23;
    L22:
        throw new IOException("Sample-AES: rangos de segmento ambiguos");     // Catch: Throwable -> L26
    L24:
        monitor-exit(this);
        return r0;
    L26:
        th = move-exception;
        throw th;
    }

    public String rewrite(String r59, String r60) throws IOException {
        String r2 = r60;
        if (r60.length() > 2097152) goto L184;
        String[] r3 = r2.split("\r?\n", -1);
        int r5 = r3.length;
        int r7 = 0;
        boolean r8 = false;
        boolean r9 = false;
    L6:
        if (r7 >= r5) goto L16;
        String r15 = r3[r7];
        if (r15.startsWith("#EXTINF:") == false) goto L11;
        r8 = true;
    L11:
        if (r15.startsWith("#EXT-X-KEY:") == false) goto L15;
        if ("SAMPLE-AES".equals(attributes(r15.substring(11)).get("METHOD")) == false) goto L15;
        r9 = true;
    L15:
        r7 = r7 + 1;
        goto L6
    L16:
        if (r8 == true) goto L18;
        return r2;
    L18:
        ArrayList r6 = new ArrayList();
        StringBuilder r10 = new StringBuilder(r60.length());
        LinkedHashMap r11 = new LinkedHashMap();
        int r16 = r3.length;
        long r27 = -1;
        long r32 = -1;
        long r22 = 0;
        long r29 = 0;
        long r36 = 0;
        String r31 = null;
        KeyMaterial r34 = null;
        byte[] r35 = null;
        int r12 = 0;
        boolean r24 = false;
        boolean r25 = false;
        int r26 = 0;
    L19:
        if (r12 >= r16) goto L181;
        String r38 = r3[r12];
        String r4 = r38.trim();
        String[] r39 = r3;
        if (r4.startsWith("#EXT-X-MEDIA-SEQUENCE:") == false) goto L24;
        r36 = number(r4.substring(22));
    L24:
        if (r4.startsWith("#EXT-X-DISCONTINUITY-SEQUENCE:") == false) goto L27;
        r22 = diagnosticNumber(r4.substring("#EXT-X-DISCONTINUITY-SEQUENCE:".length()));
    L32:
        if (r4.startsWith("#EXT-X-SKIP:") == true) goto L33;
        if (r9 == true) goto L41;
    L54:
        boolean r57 = r9;
    L56:
        if (r4.startsWith("#EXT-X-KEY:") == false) goto L85;
        Map<String, String> r13 = attributes(r4.substring(11));
        if ("SAMPLE-AES-CTR".equals(r13.get("METHOD")) == true) goto L62;
        if ("SAMPLE-AES-CENC".equals(r13.get("METHOD")) == true) goto L62;
    L65:
        r25 = "SAMPLE-AES".equals(r13.get("METHOD"));
        if (r25 == false) goto L82;
        if (r24 == true) goto L81;
        Map<String, String> r14 = Collections.unmodifiableMap(new LinkedHashMap(r13));
        KeyMaterial r17 = (KeyMaterial) r11.get(r14);
        if (r17 != null) goto L71;
        r17 = this.resolver.resolve(r59, r14);
        r11.put(r14, r17);
    L71:
        KeyMaterial r37 = r17;
        if (r37 == null) goto L79;
        if (r13.containsKey("IV") == false) goto L76;
        r35 = hexIv(r13.get("IV"));
    L77:
        KeyMaterial r18 = r37;
        String r19 = "#EXT-X-KEY:METHOD=NONE";
    L87:
        if (r4.startsWith("#EXT-X-MAP:") == false) goto L92;
        if (r25 == true) goto L91;
        r24 = true;
        goto L92
    L91:
        throw new IOException("Sample-AES: este motor necesita TS o AAC, no fMP4");
    L92:
        if (r25 == false) goto L101;
        if (r4.startsWith("#EXT-X-PART:") == true) goto L99;
        if (r4.startsWith("#EXT-X-PRELOAD-HINT:") == false) goto L101;
    L99:
        throw new IOException("Sample-AES: las partes LL-HLS requieren un adaptador específico");
    L101:
        if (r4.startsWith("#EXT-X-BYTERANGE:") == false) goto L114;
        LinkedHashMap r310 = r11;
        String[] r20 = r4.substring(17).split("@", -1);
        if (r20.length > 2) goto L113;
        r27 = number(r20[0]);
        if (r27 == 0) goto L111;
        if (r20.length != 2) goto L109;
        int r110 = 1;
        r29 = number(r20[1]);
    L116:
        if (r4.startsWith("#EXTINF:") == false) goto L119;
        r26 = r110;
    L119:
        if (r4.isEmpty() == false) goto L121;
    L179:
        r31 = r31;
    L180:
        r10.append(r19).append('\n');
        r12 = r12 + 1;
        r34 = r18;
        r11 = r310;
        r3 = r39;
        r9 = r57;
        r2 = r60;
        goto L19
    L121:
        if (r4.startsWith("#") == true) goto L179;
        if (r26 == 0) goto L179;
        if (r6.size() >= 16384) goto L178;
        String r21 = resolveUrl(r59, r4);
        if (r27 != (-1)) goto L128;
    L136:
        r32 = r29;
    L138:
        if (r25 == false) goto L146;
        if (r35 == null) goto L142;
        byte[] r23 = r35;
    L145:
        byte[] r47 = r23;
    L148:
        if (r27 != (-1)) goto L150;
        long r42 = 0;
    L151:
        if (r25 == false) goto L153;
        KeyMaterial r46 = r18;
    L154:
        if (r25 == true) goto L156;
        int r52 = 0;
    L162:
        r6.add(new Segment(r21, r42, r27, r46, r47, r36, r22, r52));
        if (r27 != (-1)) goto L165;
        r31 = null;
    L167:
        if (r36 == Long.MAX_VALUE) goto L176;
        if (r27 != (-1)) goto L170;
    L171:
        if (r27 != (-1)) goto L173;
        r32 = -1;
    L174:
        r36 = r36 + 1;
        r27 = -1;
        r29 = 0;
        r26 = 0;
        goto L180
    L173:
        r32 = r32 + r27;
        goto L174
    L170:
        if (r32 <= (Long.MAX_VALUE - r27)) goto L171;
    L176:
        throw new IOException("Sample-AES: contador o rango inválidos");
    L165:
        r31 = r21;
        goto L167
    L156:
        if (r35 == null) goto L159;
    L157:
        r52 = r110;
        goto L162
    L159:
        if (r18.f169iv == null) goto L161;
        r52 = 2;
        goto L162
    L161:
        r110 = 3;
        goto L157
    L153:
        r46 = null;
        goto L154
    L150:
        r42 = r32;
        goto L151
    L142:
        if (r18.f169iv == null) goto L144;
        r23 = r18.f169iv;
        goto L145
    L144:
        r23 = sequenceIv(r36);
        goto L145
    L146:
        r47 = null;
        goto L148
    L128:
        if (r29 != (-1)) goto L136;
        if (r21.equals(r31) == false) goto L135;
        if (r32 >= 0) goto L138;
    L135:
        throw new IOException("Sample-AES: rango implícito sin segmento anterior");
    L178:
        throw new IOException("Sample-AES: demasiados segmentos");
    L109:
        r110 = 1;
        r29 = -1;
        goto L116
    L111:
        throw new IOException("Sample-AES: rango vacío");
    L113:
        throw new IOException("Sample-AES: rango inválido");
    L114:
        r310 = r11;
        r110 = 1;
        goto L116
    L76:
        r35 = null;
        goto L77
    L79:
        throw new IOException("Sample-AES: falta una clave para este segmento");
    L81:
        throw new IOException("Sample-AES: este motor necesita TS o AAC, no fMP4");
    L82:
        r18 = null;
        r35 = null;
        r19 = r38;
    L62:
        if (r13.containsKey("KEYFORMAT") == false) goto L84;
        if ("identity".equals(r13.get("KEYFORMAT")) == false) goto L65;
    L84:
        throw new IOException("Sample-AES: CTR con KEYFORMAT identity requiere otro motor CENC");
    L85:
        r18 = r34;
        r19 = r38;
        goto L87
    L41:
        if (r4.startsWith("#EXT-X-SERVER-CONTROL:") == false) goto L54;
        Map<String, String> r28 = attributes(r4.substring(22));
        r28.remove("CAN-SKIP-UNTIL");
        r28.remove("CAN-SKIP-DATERANGES");
        StringBuilder r30 = new StringBuilder("#EXT-X-SERVER-CONTROL:");
        Iterator<Map.Entry<String, String>> r111 = r28.entrySet().iterator();
    L44:
        if (r111.hasNext() == false) goto L49;
        Map.Entry<String, String> r311 = r111.next();
        boolean r58 = r9;
        Iterator<Map.Entry<String, String>> r41 = r111;
        if (r30.length() <= 22) goto L48;
        r30.append(',');
    L48:
        r30.append(r311.getKey()).append('=').append(r311.getValue());
        r111 = r41;
        r9 = r58;
        goto L44
    L49:
        r57 = r9;
        if (r28.isEmpty() == false) goto L52;
        String r33 = "";
    L53:
        r38 = r33;
        goto L56
    L52:
        r33 = r30.toString();
        goto L53
    L33:
        if (r9 == true) goto L38;
        if (hasEncryptedSnapshot(r59) == true) goto L38;
        return r2;
    L38:
        throw new IOException("Sample-AES: se necesita una playlist completa, sin EXT-X-SKIP");
    L27:
        if (r4.equals("#EXT-X-DISCONTINUITY") == false) goto L32;
        if (r22 >= Long.MAX_VALUE) goto L32;
        r22 = r22 + 1;
        goto L32
    L181:
        publish(r59, r6);
        return r10.toString();
    L184:
        throw new IOException("La playlist HLS supera 2 MB");
    }
}
