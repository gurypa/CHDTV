package es.antonio.m3u;

import androidx.media3.common.MimeTypes;
import com.arthenica.ffmpegkit.StreamInformation;
import com.google.common.net.HttpHeaders;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* JADX INFO: loaded from: classes3.dex */
final class DashRecordingPlan {

    static final class Track {
        final int adaptation;
        final String adaptationId;
        final String key;
        final Map<String, String> keys;
        final String kind;
        final int period;
        final String periodId;
        final int representation;
        final String representationId;

        Track(Document r6, int r7, int r8, int r9, String r10, Map<String, String> r11) throws IOException {
            this.period = r7;
            this.adaptation = r8;
            this.representation = r9;
            this.kind = r10;
            this.keys = new LinkedHashMap(r11);
            Element r0 = DashRecordingPlan.children(r6.getDocumentElement(), "Period").get(r7);
            Element r1 = DashRecordingPlan.children(r0, "AdaptationSet").get(r8);
            Element r2 = DashRecordingPlan.children(r1, "Representation").get(r9);
            this.periodId = r0.getAttribute("id");
            this.adaptationId = r1.getAttribute("id");
            this.representationId = r2.getAttribute("id");
            this.key = DashRecordingPlan.m552$$Nest$smkey(r1, r2, r11);
        }

        String manifest(String r14) throws Exception {
            Document r0 = DashRecordingPlan.parse(r14);
            Element r1 = r0.getDocumentElement();
            List<Element> r2 = DashRecordingPlan.children(r1, "Period");
            Element r3 = DashRecordingPlan.m554$$Nest$smselect(r2, this.periodId, this.period);
            if (r3 == null) goto L47;
            List<Element> r4 = DashRecordingPlan.children(r3, "AdaptationSet");
            Element r5 = DashRecordingPlan.m554$$Nest$smselect(r4, this.adaptationId, this.adaptation);
            if (r5 == null) goto L45;
            List<Element> r6 = DashRecordingPlan.children(r5, "Representation");
            Element r7 = DashRecordingPlan.m554$$Nest$smselect(r6, this.representationId, this.representation);
            if (r7 == null) goto L43;
            if (this.kind.equals(DashRecordingPlan.m553$$Nest$smkind(r5, r7)) == false) goto L41;
            if (this.key.equals(DashRecordingPlan.m552$$Nest$smkey(r5, r7, this.keys)) == false) goto L41;
            Iterator<Element> r8 = r2.iterator();
        L14:
            if (r8.hasNext() == false) goto L18;
            Element r9 = r8.next();
            if (r9 == r3) goto L14;
            r1.removeChild(r9);
            goto L14
        L18:
            Iterator<Element> r10 = r4.iterator();
        L20:
            if (r10.hasNext() == false) goto L24;
            Element r11 = r10.next();
            if (r11 == r5) goto L20;
            r3.removeChild(r11);
            goto L20
        L24:
            Iterator<Element> r12 = r6.iterator();
        L26:
            if (r12.hasNext() == false) goto L30;
            Element r13 = r12.next();
            if (r13 == r7) goto L26;
            r5.removeChild(r13);
            goto L26
        L30:
            String[] r15 = {HttpHeaders.LOCATION, "PatchLocation"};
            int r16 = 0;
        L32:
            if (r16 >= 2) goto L38;
            Iterator<Element> r17 = DashRecordingPlan.children(r1, r15[r16]).iterator();
        L35:
            if (r17.hasNext() == false) goto L37;
            r1.removeChild(r17.next());
            goto L35
        L37:
            r16 = r16 + 1;
            goto L32
        L38:
            ByteArrayOutputStream r18 = new ByteArrayOutputStream();
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(r0), new StreamResult(r18));
            return r18.toString("UTF-8");
        L41:
            throw new IOException("La emisión cambió a otra clave durante la grabación");
        L43:
            throw new IOException("Cambió la representación DASH de la grabación");
        L45:
            throw new IOException("Cambió la pista DASH de la grabación");
        L47:
            throw new IOException("Cambió el periodo DASH de la grabación");
        }
    }

    /* JADX INFO: renamed from: -$$Nest$smkey, reason: not valid java name */
    static /* bridge */ /* synthetic */ String m552$$Nest$smkey(Element r0, Element r1, Map r2) {
        return key(r0, r1, r2);
    }

    /* JADX INFO: renamed from: -$$Nest$smkind, reason: not valid java name */
    static /* bridge */ /* synthetic */ String m553$$Nest$smkind(Element r0, Element r1) {
        return kind(r0, r1);
    }

    /* JADX INFO: renamed from: -$$Nest$smselect, reason: not valid java name */
    static /* bridge */ /* synthetic */ Element m554$$Nest$smselect(List r0, String r1, int r2) {
        return select(r0, r1, r2);
    }

    private DashRecordingPlan() {
    }

    private static long bandwidth(Element r3) {
        return Long.parseLong(r3.getAttribute("bandwidth"));
    L5:
        return 0;
    }

    static List<Element> children(Element r3, String r4) {
        ArrayList r0 = new ArrayList();
        Node r1 = r3.getFirstChild();
    L3:
        if (r1 == null) goto L12;
        if ((r1 instanceof Element) == false) goto L11;
        if (r4.equals(r1.getLocalName()) == false) goto L9;
    L10:
        r0.add((Element) r1);
        goto L11
    L9:
        if (r4.equals(r1.getNodeName()) == true) goto L10;
    L11:
        r1 = r1.getNextSibling();
        goto L3
    L12:
        return r0;
    }

    private static String key(Element r6, Element r7, Map<String, String> r8) throws IOException {
        Set<String> r0 = kids(r7);
        if (r0.isEmpty() == false) goto L6;
        r0 = kids(r6);
    L6:
        if (r0.isEmpty() == true) goto L8;
        String r1 = null;
        Iterator<String> r2 = r0.iterator();
    L15:
        if (r2.hasNext() == false) goto L27;
        String r4 = r8.get(r2.next());
        if (r4 == null) goto L26;
        if (r1 == null) goto L24;
        if (r1.equals(r4) == true) goto L24;
        throw new IOException("Esta pista cambia entre varias claves; la rotación dentro de una pista no está implementada");
    L24:
        r1 = r4;
        goto L15
    L26:
        throw new IOException("Falta la clave ClearKey de una pista DASH");
    L27:
        return r1;
    L8:
        if (r8.size() != 1) goto L12;
        return r8.values().iterator().next();
    L12:
        throw new IOException("El manifiesto no indica el KID de la pista; no se puede elegir una clave con seguridad");
    }

    private static Set<String> kids(Element r13) throws IOException {
        LinkedHashSet r0 = new LinkedHashSet();
        Iterator<Element> r2 = children(r13, "ContentProtection").iterator();
    L4:
        if (r2.hasNext() == false) goto L21;
        NamedNodeMap r4 = r2.next().getAttributes();
        int r5 = 0;
    L7:
        if (r5 >= r4.getLength()) goto L4;
        Node r6 = r4.item(r5);
        if ("default_KID".equals(r6.getLocalName()) == false) goto L11;
    L12:
        String[] r7 = r6.getNodeValue().trim().split("\\s+");
        int r8 = r7.length;
        int r9 = 0;
    L13:
        if (r9 >= r8) goto L19;
        String r11 = r7[r9].replace("-", "").toLowerCase(Locale.ROOT);
        if (r11.matches("[0-9a-f]{32}") == false) goto L18;
        r0.add(r11);
        r9 = r9 + 1;
        goto L13
    L18:
        throw new IOException("El KID del manifiesto DASH no es válido");
    L19:
        r5 = r5 + 1;
        goto L7
    L11:
        if ("default_KID".equals(r6.getNodeName()) == false) goto L19;
    L21:
        return r0;
    }

    private static String kind(Element r6, Element r7) {
        String r0 = r6.getAttribute("contentType");
        if (r0.equals(MimeTypes.BASE_TYPE_VIDEO) == false) goto L5;
    L43:
        return r0;
    L5:
        if (r0.equals(MimeTypes.BASE_TYPE_AUDIO) == true) goto L43;
        if (r0.equals("text") == true) goto L43;
        if (r7.hasAttribute("mimeType") == false) goto L12;
        String r3 = r7.getAttribute("mimeType");
    L14:
        if (r3.startsWith("video/") == false) goto L17;
        return MimeTypes.BASE_TYPE_VIDEO;
    L17:
        if (r3.startsWith("audio/") == false) goto L20;
        return MimeTypes.BASE_TYPE_AUDIO;
    L20:
        if (r7.hasAttribute(StreamInformation.KEY_WIDTH) == false) goto L22;
    L42:
        return MimeTypes.BASE_TYPE_VIDEO;
    L22:
        if (r6.hasAttribute(StreamInformation.KEY_WIDTH) == true) goto L42;
        if (children(r6, "AudioChannelConfiguration").isEmpty() == true) goto L27;
    L41:
        return MimeTypes.BASE_TYPE_AUDIO;
    L27:
        if (children(r7, "AudioChannelConfiguration").isEmpty() == false) goto L41;
        if (r7.hasAttribute("codecs") == false) goto L32;
        String r4 = r7.getAttribute("codecs");
    L34:
        if (r4.matches("(?i)(mp4a|ac-3|ec-3|opus|vorbis).*") == false) goto L37;
        return MimeTypes.BASE_TYPE_AUDIO;
    L37:
        if (r4.matches("(?i)(avc|hvc|hev|av01|vp09|mp4v).*") == false) goto L39;
        return MimeTypes.BASE_TYPE_VIDEO;
    L39:
        return "";
    L32:
        r4 = r6.getAttribute("codecs");
        goto L34
    L12:
        r3 = r6.getAttribute("mimeType");
        goto L14
    }

    static Document parse(String r4) throws Exception {
        if (Pattern.compile("<!\\s*(DOCTYPE|ENTITY)", 2).matcher(r4).find() == true) goto L7;
        DocumentBuilderFactory r0 = DocumentBuilderFactory.newInstance();
        r0.setNamespaceAware(true);
        r0.setExpandEntityReferences(false);
        return r0.newDocumentBuilder().parse(new ByteArrayInputStream(r4.getBytes(StandardCharsets.UTF_8)));
    L7:
        throw new IOException("Declaraciones XML externas no admitidas");
    }

    private static Element select(List<Element> r4, String r5, int r6) {
        if (r5.isEmpty() == true) goto L13;
        Iterator<Element> r0 = r4.iterator();
    L6:
        if (r0.hasNext() == false) goto L11;
        Element r2 = r0.next();
        if (r5.equals(r2.getAttribute("id")) == false) goto L6;
        return r2;
    L11:
        return null;
    L13:
        if (r6 < r4.size()) goto L15;
        return null;
    L15:
        return r4.get(r6);
    }

    static List<Track> tracks(String r20, Map<String, String> r21) throws Exception {
        Document r1 = parse(r20);
        List<Element> r7 = children(r1.getDocumentElement(), "Period");
        if (r7.size() != 1) goto L48;
        List<Element> r8 = children(r7.get(0), "AdaptationSet");
        ArrayList r3 = new ArrayList();
        int r4 = -1;
        int r5 = 0;
        boolean r12 = false;
        int r10 = -1;
        int r6 = 0;
        int r11 = 0;
    L6:
        if (r6 >= r8.size()) goto L35;
        Element r9 = r8.get(r6);
        List<Element> r13 = children(r9, "Representation");
        if (r13.isEmpty() == false) goto L10;
        Document r17 = r1;
    L34:
        r6 = r6 + 1;
        r1 = r17;
        goto L6
    L10:
        int r14 = 0;
        int r15 = 1;
    L12:
        if (r15 >= r13.size()) goto L17;
        if (bandwidth(r13.get(r15)) <= bandwidth(r13.get(r14))) goto L16;
        r14 = r15;
    L16:
        r15 = r15 + 1;
        goto L12
    L17:
        String r0 = kind(r9, r13.get(r14));
        String r2 = r9.getAttribute("lang").toLowerCase(Locale.ROOT);
        if (r2.startsWith("es") == false) goto L20;
    L23:
        boolean r16 = true;
    L24:
        r17 = r1;
        if (r0.equals(MimeTypes.BASE_TYPE_VIDEO) == false) goto L29;
        if (r4 >= 0) goto L29;
        r4 = r6;
        r5 = r14;
    L29:
        if (r0.equals(MimeTypes.BASE_TYPE_AUDIO) == false) goto L34;
        if (r10 < 0) goto L33;
        if (r16 == false) goto L34;
        if (r12 == true) goto L34;
    L33:
        r12 = r16;
        r11 = r14;
        r10 = r6;
        goto L34
    L20:
        if (r2.equals("spa") == true) goto L23;
        r16 = false;
        goto L24
    L35:
        Document r18 = r1;
        if (r4 < 0) goto L38;
        Document r19 = r18;
        r3.add(new Track(r19, 0, r4, r5, MimeTypes.BASE_TYPE_VIDEO, r21));
    L39:
        if (r10 < 0) goto L43;
        r3.add(new Track(r19, 0, r10, r11, MimeTypes.BASE_TYPE_AUDIO, r21));
    L43:
        if (r3.isEmpty() == true) goto L46;
        return r3;
    L46:
        throw new IOException("No se encontraron pistas DASH de vídeo o audio");
    L38:
        r19 = r18;
        goto L39
    L48:
        throw new IOException("La grabación ClearKey necesita un único periodo DASH; esta emisión contiene varios");
    }
}
