package es.antonio.m3u;

import android.content.Context;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.media3.common.MimeTypes;
import com.google.common.net.HttpHeaders;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* JADX INFO: loaded from: classes3.dex */
final class RecordingProxy implements AutoCloseable {
    private String cachedDash;
    private long cachedDashAt;
    private final ExecutorService clients;
    private volatile boolean closed;
    private final Set<HttpURLConnection> connections;
    private final Context context;
    private final Map<String, DashRecordingPlan.Track> dashTracks;
    private final Entry entry;
    volatile int lastStatus;
    volatile String manifestFailure;
    private final String prefix;
    private final boolean scoped;
    private final String secret;
    private final ServerSocket server;
    private final Set<Socket> sockets;

    public static /* synthetic */ void $r8$lambda$0SuHO_5qk339BmZVOmVPaJImxso(RecordingProxy r0) {
        r0.lambda$new$1();
    }

    public static /* synthetic */ void $r8$lambda$ykvt8ffMY9BvE3rXkYdTl07EiWw(RecordingProxy r0, Socket r1) {
        r0.lambda$new$0(r1);
    }

    RecordingProxy(Context r5, Entry r6) throws IOException {
        this.secret = UUID.randomUUID().toString();
        this.dashTracks = new ConcurrentHashMap();
        this.clients = Executors.newFixedThreadPool(4);
        this.sockets = ConcurrentHashMap.newKeySet();
        this.connections = ConcurrentHashMap.newKeySet();
        this.manifestFailure = "";
        this.context = r5.getApplicationContext();
        this.entry = r6;
        this.scoped = CdnTokens.scoped(this.context, this.entry);
        this.server = new ServerSocket(0, 16, InetAddress.getByName("127.0.0.1"));
        this.prefix = "http://127.0.0.1:" + this.server.getLocalPort() + "/" + this.secret + "/";
        Thread r0 = new Thread(new RecordingProxy$$ExternalSyntheticLambda1(this), "CHDTV-record-network");
        r0.setDaemon(true);
        r0.start();
    }

    private HttpURLConnection connect(URI r12, String r13, String r14) throws IOException {
        URI r0 = r12;
        int r1 = 0;
    L4:
        if (r1 >= 8) goto L69;
        String r2 = r0.toString();
        if (this.scoped == true) goto L8;
    L10:
        String r3 = "";
    L12:
        if (r3.isEmpty() == true) goto L15;
        r2 = MovistarStreams.withToken(r2, r3);
    L15:
        if (this.entry.headers != null) goto L17;
        Map<String, String> r5 = Collections.emptyMap();
    L18:
        Map r4 = new LinkedHashMap(r5);
        if (Objects.equals(r12.getHost(), r0.getHost()) == true) goto L22;
        r4.entrySet().removeIf(new RecordingProxy$$ExternalSyntheticLambda2());
    L22:
        if (r3.isEmpty() == true) goto L24;
        r4 = CdnTokens.headers(r4, r3);
    L24:
        HttpURLConnection r6 = (HttpURLConnection) new URL(r2).openConnection();
        this.connections.add(r6);
        r6.setConnectTimeout(15000);
        r6.setReadTimeout(15000);
        r6.setRequestMethod(r14);
        r6.setInstanceFollowRedirects(false);
        Iterator r7 = r4.entrySet().iterator();
    L26:
        if (r7.hasNext() == false) goto L42;
        Map.Entry r8 = (Map.Entry) r7.next();
        if (r8.getKey() == null) goto L26;
        if (r8.getValue() == null) goto L26;
        if (((String) r8.getKey()).matches("(?i)host|content-length|range|connection") == true) goto L26;
        if (((String) r8.getKey()).contains("\n") == true) goto L26;
        if (((String) r8.getKey()).contains("\r") == true) goto L26;
        if (((String) r8.getValue()).contains("\n") == true) goto L26;
        if (((String) r8.getValue()).contains("\r") == true) goto L26;
        r6.setRequestProperty((String) r8.getKey(), (String) r8.getValue());
        goto L26
    L42:
        r6.setRequestProperty(HttpHeaders.ACCEPT_ENCODING, "identity");
        if (r13 == null) goto L45;
        r6.setRequestProperty(HttpHeaders.RANGE, r13);
    L45:
        int r9 = r6.getResponseCode();
        if (r9 != 301) goto L48;
    L56:
        String r10 = r6.getHeaderField(HttpHeaders.LOCATION);
        this.connections.remove(r6);
        r6.disconnect();
        if (r10 == null) goto L67;
        r0 = r0.resolve(r10);
        if ("http".equalsIgnoreCase(r0.getScheme()) == true) goto L65;
        if ("https".equalsIgnoreCase(r0.getScheme()) == true) goto L65;
        throw new IOException("Protocolo de redirección no admitido");
    L65:
        r1 = r1 + 1;
        goto L4
    L67:
        throw new IOException("Redirección sin destino");
    L48:
        if (r9 == 302) goto L56;
        if (r9 == 303) goto L56;
        if (r9 == 307) goto L56;
        if (r9 == 308) goto L56;
        return r6;
    L17:
        r5 = this.entry.headers;
        goto L18
    L8:
        if (CdnTokens.allowedHost(r2) == false) goto L10;
        r3 = CdnTokens.current(this.context, this.entry);
        goto L12
    L69:
        throw new IOException("Demasiadas redirecciones");
    }

    private synchronized String currentDash() throws Exception {
        monitor-enter(this);
    L38:
        th = move-exception;
        throw th;
    L4:
        if (this.closed == true) goto L37;
        if (this.cachedDash == null) goto L13;
        if ((System.currentTimeMillis() - this.cachedDashAt) >= 1000) goto L13;
        String r0 = this.cachedDash;     // Catch: Throwable -> L38
        monitor-exit(this);
        return r0;
    L13:
        renew(false);     // Catch: Throwable -> L38
        URI r1 = URI.create(Entry.details(this.entry).url);     // Catch: Throwable -> L38
        HttpURLConnection r2 = connect(r1, null, "GET");     // Catch: Throwable -> L38
        int r3 = r2.getResponseCode();     // Catch: Throwable -> L33
        if (this.scoped == true) goto L17;
    L25:
        this.lastStatus = r3;     // Catch: Throwable -> L33
        if (r3 != 200) goto L32;
        this.cachedDash = dash(new String(limited(r2.getInputStream(), 4194304), StandardCharsets.UTF_8), r2.getURL().toURI());     // Catch: Throwable -> L33
        this.cachedDashAt = System.currentTimeMillis();     // Catch: Throwable -> L33
        String r4 = this.cachedDash;     // Catch: Throwable -> L33
        this.connections.remove(r2);     // Catch: Throwable -> L38
        r2.disconnect();     // Catch: Throwable -> L38
        monitor-exit(this);
        return r4;
    L32:
        throw new IOException("El proveedor respondió HTTP " + r3);     // Catch: Throwable -> L33
    L17:
        if (CdnTokens.allowedHost(r1.toString()) == false) goto L25;
        if (r3 != 401) goto L21;
    L24:
        this.connections.remove(r2);     // Catch: Throwable -> L33
        r2.disconnect();     // Catch: Throwable -> L33
        renew(true);     // Catch: Throwable -> L33
        r2 = connect(r1, null, "GET");     // Catch: Throwable -> L33
        r3 = r2.getResponseCode();     // Catch: Throwable -> L33
        goto L25
    L21:
        if (r3 == 403) goto L24;
        if (r3 != 418) goto L25;
    L33:
        th = move-exception;
        this.connections.remove(r2);     // Catch: Throwable -> L38
        r2.disconnect();     // Catch: Throwable -> L38
        throw th;     // Catch: Throwable -> L38
    L37:
        throw new IOException("Grabación detenida");     // Catch: Throwable -> L38
    }

    static /* synthetic */ boolean lambda$connect$3(Map.Entry r2) {
        if (r2.getKey() != null) goto L5;
    L9:
        return false;
    L5:
        if (((String) r2.getKey()).equalsIgnoreCase(HttpHeaders.AUTHORIZATION) == false) goto L7;
    L8:
        return true;
    L7:
        if (((String) r2.getKey()).equalsIgnoreCase(HttpHeaders.COOKIE) == false) goto L9;
        goto L8
    }

    private /* synthetic */ void lambda$new$0(Socket r1) {
        serve(r1);
    }

    private /* synthetic */ void lambda$new$1() {
    L3:
        if (this.closed == true) goto L13;
        final Socket r0 = this.server.accept();     // Catch: Throwable -> L6 IOException -> L8
        this.sockets.add(r0);     // Catch: Throwable -> L6 IOException -> L8
        this.clients.execute(new RecordingProxy$$ExternalSyntheticLambda3(this, r0));     // Catch: Throwable -> L6 IOException -> L8
    L10:
        if (this.closed == false) goto L3;
        return;
    }

    static /* synthetic */ void lambda$renew$2(CountDownLatch r0, Boolean r1, Throwable r2) {
        r0.countDown();
    }

    private static byte[] limited(InputStream r5, int r6) throws IOException {
        ByteArrayOutputStream r0 = new ByteArrayOutputStream();     // Catch: Throwable -> L23
        byte[] r1 = new byte[8192];     // Catch: Throwable -> L17
    L5:
        int r2 = r5.read(r1);     // Catch: Throwable -> L17
        if (r2 == (-1)) goto L12;
        if ((r0.size() + r2) > r6) goto L11;
        r0.write(r1, 0, r2);     // Catch: Throwable -> L17
        goto L5
    L11:
        throw new IOException("Manifest demasiado grande");     // Catch: Throwable -> L17
    L12:
        byte[] r3 = r0.toByteArray();     // Catch: Throwable -> L17
        r0.close();     // Catch: Throwable -> L23
        if (r5 == null) goto L16;
        r5.close();
    L16:
        return r3;
    L17:
        th = move-exception;
        r0.close();     // Catch: Throwable -> L20
    L22:
        throw th;     // Catch: Throwable -> L23
    L20:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L23
    L23:
        th = move-exception;
        if (r5 != null) goto L31;
    L29:
        throw th;
    L31:
        r5.close();     // Catch: Throwable -> L27
    L27:
        th = move-exception;
        th.addSuppressed(th);
        goto L29
    }

    private void renew(boolean r6) throws InterruptedException {
        if (this.scoped == true) goto L5;
        return;
    L5:
        final CountDownLatch r0 = new CountDownLatch(1);
        if (r6 == false) goto L8;
        CdnTokens.Pending r1 = CdnTokens.renew(this.context, true);
    L9:
        r1.whenComplete(new RecordingProxy$$ExternalSyntheticLambda0(r0));
        r0.await(30, TimeUnit.SECONDS);
        return;
    L8:
        r1 = CdnTokens.forPlayback(this.context);
        goto L9
    }

    private String resolve(URI r4, String r5) {
        URI r0 = r4.resolve(r5);
        if ("http".equalsIgnoreCase(r0.getScheme()) == true) goto L9;
        if ("https".equalsIgnoreCase(r0.getScheme()) == true) goto L9;
        return r5;
    L9:
        return url(r0.toString());
    }

    private void rewriteDash(Element r8, URI r9) {
        URI r0 = r9;
        Node r1 = r8.getFirstChild();
    L4:
        if (r1 == null) goto L12;
        if ((r1 instanceof Element) == false) goto L10;
        if ("BaseURL".equals(r1.getLocalName()) == false) goto L10;
        String r3 = r1.getTextContent().trim();
        URI r4 = r9.resolve(r3);
        r1.setTextContent(resolve(r9, r3));
        r0 = r4;
    L10:
        r1 = r1.getNextSibling();
    L12:
        if ("UTCTiming".equals(r8.getLocalName()) == false) goto L21;
        if (r8.hasAttribute("value") == false) goto L21;
        if (r8.getAttribute("value").startsWith("http://") == false) goto L18;
    L19:
        r8.setAttribute("value", resolve(r0, r8.getAttribute("value")));
        goto L21
    L18:
        if (r8.getAttribute("value").startsWith("https://") == true) goto L19;
    L21:
        if (HttpHeaders.LOCATION.equals(r8.getLocalName()) == false) goto L23;
        r8.setTextContent(resolve(r0, r8.getTextContent().trim()));
    L23:
        String[] r2 = {"media", "initialization", "sourceURL", "href"};
        int r5 = 0;
    L25:
        if (r5 >= 4) goto L32;
        String r6 = r2[r5];
        if (r8.hasAttribute(r6) == false) goto L31;
        String r7 = r8.getAttribute(r6);
        if (r7.isEmpty() == true) goto L31;
        r8.setAttribute(r6, resolve(r0, r7));
    L31:
        r5 = r5 + 1;
        goto L25
    L32:
        Node r10 = r8.getFirstChild();
    L33:
        if (r10 == null) goto L40;
        if ((r10 instanceof Element) == false) goto L39;
        if ("BaseURL".equals(r10.getLocalName()) == true) goto L39;
        rewriteDash((Element) r10, r0);
    L39:
        r10 = r10.getNextSibling();
        goto L33
    }

    /* JADX WARN: Not initialized variable reg: 14, insn: 0x02f8: MOVE (r3 I:??[OBJECT, ARRAY]) = (r14 I:??[OBJECT, ARRAY]), block:B:183:0x02f8 */
    /* JADX WARN: Not initialized variable reg: 14, insn: 0x02fb: MOVE (r3 I:??[OBJECT, ARRAY]) = (r14 I:??[OBJECT, ARRAY]), block:B:185:0x02fb */
    private void serve(Socket r30) {
        HttpURLConnection r3 = null;
        r30.setSoTimeout(AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH);     // Catch: Throwable -> L174
        BufferedReader r5 = new BufferedReader(new InputStreamReader(r30.getInputStream(), StandardCharsets.US_ASCII));     // Catch: Throwable -> L174
        String r6 = r5.readLine();     // Catch: Throwable -> L174
        if (r6 != null) goto L14;
        if (r30 == null) goto L10;
        r30.close();     // Catch: Exception -> L8 Throwable -> L194
    L10:
        if (0 == 0) goto L12;
        this.connections.remove(null);
        r3.disconnect();
    L12:
        this.sockets.remove(r30);
        return;
    L14:
        String[] r7 = r6.split(" ", 3);     // Catch: Throwable -> L174
        if (r7.length != 3) goto L204;
        if (r7[0].equals("GET") == false) goto L203;
    L25:
        String r9 = null;
        int r11 = r6.length();     // Catch: Throwable -> L174
    L27:
        String r12 = r5.readLine();     // Catch: Throwable -> L174
        if (r12 == null) goto L40;
        if (r12.isEmpty() == true) goto L40;
        r11 = r11 + r12.length();     // Catch: Throwable -> L23
        if (r11 > 16384) goto L39;
        if (r12.regionMatches(true, 0, "Range:", 0, 6) == false) goto L27;
        r9 = r12.substring(6).trim();     // Catch: Throwable -> L23
        goto L27
    L39:
        throw new IOException("Cabeceras demasiado grandes");     // Catch: Throwable -> L23
    L40:
        DashRecordingPlan.Track r8 = this.dashTracks.get(r7[1]);     // Catch: Throwable -> L174
        if (r8 != null) goto L208;
        String r21 = r9;
        URI r10 = original(r7[1]);     // Catch: Throwable -> L174
        renew(false);     // Catch: Throwable -> L174
        HttpURLConnection r14 = connect(r10, r21, r7[0]);     // Catch: Throwable -> L174
        int r4 = r14.getResponseCode();     // Catch: Throwable -> L167
        if (this.scoped == true) goto L231;
    L76:
        this.lastStatus = r4;     // Catch: Throwable -> L167
        OutputStream r15 = r30.getOutputStream();     // Catch: Throwable -> L167
        String r17 = r14.getContentType();     // Catch: Throwable -> L167
        if (r7[0].equals("GET") == false) goto L88;
        if (r4 != 200) goto L88;
    L165:
        th = move-exception;
        Throwable r13 = th;
    L176:
        if (r30 == null) goto L243;
        r30.close();     // Catch: Throwable -> L179
        throw r13;     // Catch: Throwable -> L182 Exception -> L184
    L179:
        th = move-exception;
        r13.addSuppressed(th);     // Catch: Throwable -> L182 Exception -> L184
        throw r13;     // Catch: Throwable -> L182 Exception -> L184
    L243:
        throw r13;     // Catch: Throwable -> L182 Exception -> L184
    L81:
        if (r10.getPath().toLowerCase(Locale.ROOT).endsWith(".m3u8") == true) goto L86;
        if (r17 == null) goto L88;
        String r16 = r17;
        if (r16.toLowerCase(Locale.ROOT).contains("mpegurl") == true) goto L87;
    L89:
        boolean r18 = false;
    L91:
        if (r7[0].equals("GET") == true) goto L93;
    L100:
        boolean r19 = false;
    L101:
        boolean r20 = r19;
        if (r18 == true) goto L149;
        if (r20 == true) goto L149;
        writeHeaders(r15, r4, r16, r14.getContentLengthLong(), r14.getHeaderField(HttpHeaders.CONTENT_RANGE));     // Catch: Throwable -> L165
    L146:
        th = move-exception;
        r13 = th;
        goto L176
    L108:
        if (r7[0].equals("HEAD") == true) goto L157;
        if (r4 < 400) goto L112;
        InputStream r0 = r14.getErrorStream();     // Catch: Throwable -> L146
    L113:
        InputStream r110 = r0;
        if (r110 == null) goto L157;
        byte[] r1 = new byte[65536];     // Catch: Throwable -> L135
    L117:
        int r22 = r4;
    L133:
        th = move-exception;
        Throwable r23 = th;
    L137:
        if (r110 == null) goto L241;
        r110.close();     // Catch: Throwable -> L140
    L242:
        throw r23;     // Catch: Throwable -> L146
    L140:
        th = move-exception;
        r23.addSuppressed(th);     // Catch: Throwable -> L146
        throw r23;     // Catch: Throwable -> L146
    L241:
        throw r23;     // Catch: Throwable -> L146
    L119:
        if (this.closed == true) goto L130;
        int r24 = r110.read(r1);     // Catch: Throwable -> L133
        boolean r25 = r18;
        if (r24 == (-1)) goto L130;
        r15.write(r1, 0, r24);     // Catch: Throwable -> L126
        r18 = r25;
        r4 = r22;
    L126:
        th = move-exception;
        r23 = th;
    L130:
        if (r110 == null) goto L157;
        r110.close();     // Catch: Throwable -> L146
    L135:
        th = move-exception;
        r23 = th;
        goto L137
    L112:
        r0 = r14.getInputStream();     // Catch: Throwable -> L146
    L157:
        r15.flush();     // Catch: Throwable -> L74
        if (r30 == null) goto L160;
        r30.close();     // Catch: Throwable -> L182 Exception -> L184
    L160:
        if (r14 == null) goto L162;
        this.connections.remove(r14);
        r14.disconnect();
    L162:
        this.sockets.remove(r30);
        return;
    L74:
        th = move-exception;
        r13 = th;
    L149:
        byte[] r2 = limited(r14.getInputStream(), 4194304);     // Catch: Throwable -> L165
        URI r26 = r14.getURL().toURI();     // Catch: Throwable -> L165
        String r27 = new String(r2, StandardCharsets.UTF_8);     // Catch: Throwable -> L165
        if (r18 == false) goto L152;
        String r111 = hls(r27, r26);     // Catch: Throwable -> L165
    L153:
        byte[] r28 = r111.getBytes(StandardCharsets.UTF_8);     // Catch: Throwable -> L165
        writeHeaders(r15, r4, r16, r28.length, null);     // Catch: Throwable -> L165
        r15.write(r28);     // Catch: Throwable -> L163
    L163:
        th = move-exception;
        r13 = th;
        goto L176
    L152:
        r111 = dash(r27, r26);     // Catch: Throwable -> L165
        goto L153
    L93:
        if (r4 != 200) goto L100;
        if (r10.getPath().toLowerCase(Locale.ROOT).endsWith(".mpd") == true) goto L99;
        if (r16 == null) goto L100;
        if (r16.contains("dash+xml") == false) goto L100;
    L99:
        r19 = true;
    L87:
        r18 = true;
        goto L91
    L86:
        r16 = r17;
    L88:
        r16 = r17;
        goto L89
    L231:
        if (CdnTokens.allowedHost(r10.toString()) == false) goto L76;
        if (r4 != 401) goto L66;
    L69:
        this.connections.remove(r14);     // Catch: Throwable -> L74
        r14.disconnect();     // Catch: Throwable -> L74
        renew(true);     // Catch: Throwable -> L74
        HttpURLConnection r112 = connect(r10, r21, r7[0]);     // Catch: Throwable -> L74
        r4 = r112.getResponseCode();     // Catch: Throwable -> L72
        r14 = r112;
    L72:
        th = move-exception;
        r13 = th;
        goto L176
    L66:
        if (r4 == 403) goto L69;
        if (r4 != 418) goto L76;
    L167:
        th = move-exception;
        r13 = th;
        goto L176
    L208:
        byte[] r29 = r8.manifest(currentDash()).getBytes(StandardCharsets.UTF_8);     // Catch: Throwable -> L23 IOException -> L53
        OutputStream r113 = r30.getOutputStream();     // Catch: Throwable -> L23
        writeHeaders(r113, 200, MimeTypes.APPLICATION_MPD, r29.length, null);     // Catch: Throwable -> L23
        if (r7[0].equals("HEAD") == true) goto L46;
        r113.write(r29);     // Catch: Throwable -> L23
    L46:
        r113.flush();     // Catch: Throwable -> L23
        if (r30 == null) goto L49;
        r30.close();     // Catch: Exception -> L8 Throwable -> L194
    L49:
        if (0 == 0) goto L51;
        this.connections.remove(null);
        r3.disconnect();
    L51:
        this.sockets.remove(r30);
        return;
    L53:
        e = move-exception;
        this.manifestFailure = e.getMessage();     // Catch: Throwable -> L23
        throw e;     // Catch: Throwable -> L23
    L23:
        th = move-exception;
        r13 = th;
        goto L176
    L203:
        if (r7[0].equals("HEAD") == true) goto L25;
    L204:
    L172:
        th = move-exception;
        r13 = th;
        goto L176
    L171:
        throw new IOException("Solicitud no válida");     // Catch: Throwable -> L172
    L8:
        e = e;
    L187:
        if (0 == 0) goto L189;
    L199:
        if (r3 == null) goto L201;
        this.connections.remove(r3);
        r3.disconnect();
    L201:
        this.sockets.remove(r30);
        return;
    L189:
        if (r30.isClosed() == true) goto L199;
        writeHeaders(r30.getOutputStream(), 502, "text/plain", 0, null);     // Catch: Exception -> L192 Throwable -> L194
    L194:
        th = th;
    L195:
        if (r3 == null) goto L197;
        this.connections.remove(r3);
        r3.disconnect();
    L197:
        this.sockets.remove(r30);
        throw th;
    L174:
        th = move-exception;
        r13 = th;
    L184:
        e = e;
        r3 = r14;
    L182:
        th = th;
        r3 = r14;
        goto L195
    }

    private static void writeHeaders(OutputStream r6, int r7, String r8, long r9, String r11) throws IOException {
        StringBuilder r0 = new StringBuilder("HTTP/1.1 " + r7 + " Response\r\nConnection: close\r\n");
        if (r8 == null) goto L10;
        if (r8.contains("\r") == true) goto L10;
        if (r8.contains("\n") == true) goto L10;
        r0.append("Content-Type: ").append(r8).append("\r\n");
    L10:
        if (r9 < 0) goto L12;
        r0.append("Content-Length: ").append(r9).append("\r\n");
    L12:
        if (r11 != null) goto L14;
    L18:
        r0.append("\r\n");
        r6.write(r0.toString().getBytes(StandardCharsets.US_ASCII));
        return;
    L14:
        if (r11.contains("\r") == true) goto L18;
        if (r11.contains("\n") == true) goto L18;
        r0.append("Content-Range: ").append(r11).append("\r\n");
        goto L18
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.closed = true;
        this.server.close();     // Catch: IOException -> L5
    L6:
        Iterator<HttpURLConnection> r0 = this.connections.iterator();
    L8:
        if (r0.hasNext() == false) goto L10;
        r0.next().disconnect();
        goto L8
    L10:
        Iterator<Socket> r1 = this.sockets.iterator();
    L12:
        if (r1.hasNext() == false) goto L18;
        r1.next().close();     // Catch: IOException -> L16
        goto L12
    L18:
        this.clients.shutdownNow();
    }

    String dash(String r10, URI r11) throws Exception {
        if (Pattern.compile("<!\\s*(DOCTYPE|ENTITY)", 2).matcher(r10).find() == true) goto L18;
        DocumentBuilderFactory r0 = DocumentBuilderFactory.newInstance();
        r0.setNamespaceAware(true);
        r0.setExpandEntityReferences(false);
        Document r1 = r0.newDocumentBuilder().parse(new ByteArrayInputStream(r10.getBytes(StandardCharsets.UTF_8)));
        rewriteDash(r1.getDocumentElement(), r11);
        Element r2 = r1.getDocumentElement();
        boolean r3 = false;
        Node r4 = r2.getFirstChild();
    L6:
        if (r4 == null) goto L13;
        if ((r4 instanceof Element) == false) goto L12;
        if ("BaseURL".equals(r4.getLocalName()) == false) goto L12;
        r3 = true;
    L12:
        r4 = r4.getNextSibling();
        goto L6
    L13:
        if (r3 == true) goto L15;
        Element r5 = r1.createElementNS(r2.getNamespaceURI(), "BaseURL");
        r5.setTextContent(url(r11.resolve(".").toString()));
        r2.insertBefore(r5, r2.getFirstChild());
    L15:
        TransformerFactory r6 = TransformerFactory.newInstance();
        ByteArrayOutputStream r7 = new ByteArrayOutputStream();
        r6.newTransformer().transform(new DOMSource(r1), new StreamResult(r7));
        return r7.toString("UTF-8");
    L18:
        throw new IOException("Declaraciones XML externas no admitidas");
    }

    String hls(String r11, URI r12) {
        StringBuilder r0 = new StringBuilder();
        Pattern r1 = Pattern.compile("URI=\"([^\"]*)\"");
        String[] r2 = r11.split("\\r?\\n");
        int r3 = r2.length;
        int r4 = 0;
    L3:
        if (r4 >= r3) goto L16;
        String r5 = r2[r4];
        if (r5.isEmpty() == false) goto L7;
    L9:
        Matcher r6 = r1.matcher(r5);
        StringBuffer r7 = new StringBuffer();
    L11:
        if (r6.find() == false) goto L13;
        r6.appendReplacement(r7, Matcher.quoteReplacement("URI=\"" + resolve(r12, r6.group(1)) + "\""));
        goto L11
    L13:
        r6.appendTail(r7);
        String r8 = r7.toString();
    L14:
        r0.append(r8).append('\n');
        r4 = r4 + 1;
        goto L3
    L7:
        if (r5.startsWith("#") == true) goto L9;
        r8 = resolve(r12, r5);
        goto L14
    L16:
        return r0.toString();
    }

    URI original(String r7) throws IOException {
        String r0 = "/" + this.secret + "/";
        if (r7.startsWith(r0) == false) goto L18;
        String r1 = r7.substring(r0.length());
        int r3 = r1.indexOf(47);
        if (r3 < 0) goto L16;
        URI r2 = URI.create(r1.substring(0, r3) + "://" + r1.substring(r3 + 1));
        if ("http".equals(r2.getScheme()) == true) goto L11;
        if ("https".equals(r2.getScheme()) == true) goto L11;
    L14:
        throw new IOException("Protocolo no válido");
    L11:
        if (r2.getHost() == null) goto L14;
        return r2;
    L16:
        throw new IOException("Solicitud local no válida");
    L18:
        throw new IOException("Solicitud local no válida");
    }

    List<DashRecordingPlan.Track> prepareDash(Map<String, String> r6) throws Exception {
        List<DashRecordingPlan.Track> r0 = DashRecordingPlan.tracks(currentDash(), r6);
        int r1 = 0;
    L4:
        if (r1 >= r0.size()) goto L6;
        this.dashTracks.put("/" + this.secret + "/dash/" + r1 + ".mpd", r0.get(r1));
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    String trackUrl(int r3) {
        return this.prefix + "dash/" + r3 + ".mpd";
    }

    String url(String r5) {
        URI r0 = URI.create(r5);
        if ("http".equalsIgnoreCase(r0.getScheme()) == false) goto L5;
    L9:
        String r2 = "/";
        StringBuilder r1 = new StringBuilder().append(this.prefix).append(r0.getScheme()).append("/").append(r0.getRawAuthority());
        if (r0.getRawPath() != null) goto L12;
    L15:
        StringBuilder r3 = r1.append(r2);
        if (r0.getRawQuery() != null) goto L18;
        String r4 = "";
    L20:
        return r3.append(r4).toString();
    L18:
        r4 = "?" + r0.getRawQuery();
        goto L20
    L12:
        if (r0.getRawPath().isEmpty() == true) goto L15;
        r2 = r0.getRawPath();
        goto L15
    L5:
        if ("https".equalsIgnoreCase(r0.getScheme()) == true) goto L9;
        throw new IllegalArgumentException("Protocolo no válido");
    }
}
