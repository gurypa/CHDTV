package es.antonio.m3u;

import com.arthenica.ffmpegkit.StreamInformation;
import com.google.common.net.HttpHeaders;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/* JADX INFO: loaded from: classes3.dex */
final class PlexClient {
    PlexClient() {
    }

    static String endpoint(String r4, String r5) throws Exception {
        URI r0 = new URI(r4);
        if (Arrays.asList(new String[]{"http", "https"}).contains(r0.getScheme()) == false) goto L19;
        if (r0.getHost() == null) goto L19;
        if (r0.getUserInfo() != null) goto L19;
        URI r1 = r0.resolve(r5);
        if (Objects.equals(r0.getHost(), r1.getHost()) == false) goto L17;
        if (r0.getPort() != r1.getPort()) goto L17;
        if (Objects.equals(r0.getScheme(), r1.getScheme()) == false) goto L17;
        return r1.toString();
    L17:
        throw new IOException("Ruta fuera del servidor");
    L19:
        throw new IOException("Dirección del servidor inválida");
    }

    static /* synthetic */ InputSource lambda$xml$0(String r2, String r3) throws IOException, SAXException {
        throw new SAXException("Entidades externas no admitidas");
    }

    static Entry playable(String r6, String r7, Element r8) throws Exception {
        NodeList r0 = r8.getElementsByTagName("Part");
        if (r0.getLength() == 0) goto L21;
        Entry r1 = new Entry();
        if (r8.getAttribute("type").equals("episode") == false) goto L7;
        String r2 = "Series";
    L8:
        r1.kind = r2;
        r1.title = r8.getAttribute("title");
        r1.group = "Plex";
        r1.plexBase = r6;
        r1.plexKey = r8.getAttribute("ratingKey");
        String r3 = r8.getAttribute("thumb");
        if (r3.isEmpty() == true) goto L14;
        StringBuilder r4 = new StringBuilder().append(endpoint(r6, r3));
        String r5 = "?";
        if (r3.contains("?") == false) goto L13;
        r5 = "&";
    L13:
        r1.logo = r4.append(r5).append("X-Plex-Token=").append(URLEncoder.encode(r7, "UTF-8")).toString();
    L14:
        r1.series = r8.getAttribute("grandparentTitle");
        r1.season = Integer.parseInt(r8.getAttribute("parentIndex"));     // Catch: NumberFormatException -> L17
        r1.episode = Integer.parseInt(r8.getAttribute(StreamInformation.KEY_INDEX));     // Catch: NumberFormatException -> L17
    L18:
        Entry r9 = Entry.details(r1);
        r9.url = endpoint(r6, ((Element) r0.item(0)).getAttribute("key"));
        r1.headers.put("X-Plex-Token", r7);
        return r1;
    L7:
        r2 = "Películas";
        goto L8
    L21:
        throw new IOException("Plex no ofrece un archivo reproducible");
    }

    static Document xml(InputStream r7) throws Exception {
        StringBuilder r0 = new StringBuilder();
        Reader r1 = PlaylistInput.reader(r7);
        char[] r2 = new char[8192];     // Catch: Throwable -> L19
    L4:
        int r3 = r1.read(r2);     // Catch: Throwable -> L19
        if (r3 == (-1)) goto L11;
        if ((r0.length() + r3) > 8388608) goto L10;
        r0.append(r2, 0, r3);     // Catch: Throwable -> L19
        goto L4
    L10:
        throw new IOException("Respuesta Plex demasiado grande");     // Catch: Throwable -> L19
    L11:
        if (r1 == null) goto L14;
        r1.close();
    L14:
        if (Pattern.compile("<!\\s*(?:DOCTYPE|ENTITY)\\b", 2).matcher(r0).find() == true) goto L18;
        DocumentBuilderFactory r4 = DocumentBuilderFactory.newInstance();
        r4.setExpandEntityReferences(false);
        DocumentBuilder r5 = r4.newDocumentBuilder();
        r5.setEntityResolver(new PlexClient$$ExternalSyntheticLambda0());
        return r5.parse(new InputSource(new StringReader(r0.toString())));
    L18:
        throw new IOException("XML Plex con declaraciones externas no admitidas");
    L19:
        th = move-exception;
        if (r1 != null) goto L26;
    L25:
        throw th;
    L26:
        r1.close();     // Catch: Throwable -> L23
    L23:
        th = move-exception;
        th.addSuppressed(th);
        goto L25
    }

    Document get(String r6, String r7) throws Exception {
        HttpURLConnection r0 = (HttpURLConnection) new URL(r6).openConnection();
        r0.setInstanceFollowRedirects(false);
        r0.setConnectTimeout(10000);
        r0.setReadTimeout(15000);
        r0.setRequestProperty("X-Plex-Token", r7);
        r0.setRequestProperty("X-Plex-Product", "Chdtv");
        r0.setRequestProperty("X-Plex-Client-Identifier", "chdtv-android");
        r0.setRequestProperty(HttpHeaders.ACCEPT, "application/xml");
        int r1 = r0.getResponseCode();     // Catch: Throwable -> L25
        if (r1 != 200) goto L6;
        final InputStream r4 = r0.getInputStream();     // Catch: Throwable -> L25
        BufferedInputStream r2 = new BufferedInputStream(new C10261(this, r4));     // Catch: Throwable -> L25
        Document r3 = xml(r2);     // Catch: Throwable -> L19
        r2.close();     // Catch: Throwable -> L25
        r0.disconnect();
        return r3;
    L19:
        th = move-exception;
        r2.close();     // Catch: Throwable -> L22
    L24:
        throw th;     // Catch: Throwable -> L25
    L22:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L25
        goto L24
    L6:
        if (r1 != 401) goto L8;
    L11:
        String r5 = "Acceso rechazado: comprueba el token";
    L13:
        throw new IOException(r5);     // Catch: Throwable -> L25
    L8:
        if (r1 == 403) goto L11;
        r5 = "Plex ha respondido HTTP " + r1;     // Catch: Throwable -> L25
    L25:
        th = move-exception;
        r0.disconnect();
        throw th;
    }
}
