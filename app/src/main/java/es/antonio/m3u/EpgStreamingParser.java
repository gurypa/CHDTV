package es.antonio.m3u;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/* JADX INFO: loaded from: classes7.dex */
public final class EpgStreamingParser {
    private EpgStreamingParser() {
    }

    static /* synthetic */ InputSource lambda$parse$0(String r1, String r2) throws SAXException, IOException {
        return new InputSource(new StringReader(""));
    }

    public static EpgData parse(InputStream r1, long r2) throws Exception {
        return parse(r1, r2, null);
    }

    public static EpgData parse(InputStream r12, final long r13, final List<Entry> r15) throws Exception {
        final HashSet r3 = new HashSet();
        final HashSet r7 = new HashSet();
        if (r15 == null) goto L17;
        Iterator<Entry> r0 = r15.iterator();
    L6:
        if (r0.hasNext() == false) goto L17;
        Entry r1 = r0.next();
        if (r1.tvgId != null) goto L10;
    L12:
        Iterator r2 = Arrays.asList(new String[]{r1.tvgId, r1.tvgName, r1.title}).iterator();
    L14:
        if (r2.hasNext() == false) goto L6;
        r7.addAll(EpgData.matchKeys((String) r2.next()));
        goto L14
    L10:
        if (r1.tvgId.trim().isEmpty() == true) goto L12;
        r3.add(r1.tvgId.trim());
    L17:
        SAXParserFactory r4 = SAXParserFactory.newInstance();
        r4.setNamespaceAware(false);
        r4.setFeature("http://xml.org/sax/features/external-general-entities", false);
        r4.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        r4.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);     // Catch: SAXException -> L20 ParserConfigurationException -> L22
    L23:
        XMLReader r8 = r4.newSAXParser().getXMLReader();
        r8.setEntityResolver(new EpgStreamingParser$$ExternalSyntheticLambda0());
        final EpgData r9 = new EpgData();
        r9.loadedAt = r13;
        final EpgChannelFlow.Progress r10 = EpgChannelFlow.progress();
        DefaultHandler r11 = new C09971(r9, r15, r3, r13, r10, r7);
        r8.setContentHandler(r11);
        r8.setErrorHandler(r11);
        r8.parse(new InputSource(r12));
        r9.index();
        if (r10 == null) goto L26;
        r10.flush();
    L26:
        return r9;
    }

    public static long time(String r2) throws ParseException {
        return EpgTime.parse(r2);
    }
}
