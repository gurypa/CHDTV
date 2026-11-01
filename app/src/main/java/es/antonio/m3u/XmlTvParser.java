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

/* JADX INFO: loaded from: classes3.dex */
public final class XmlTvParser {
    private XmlTvParser() {
    }

    static /* synthetic */ InputSource lambda$parse$0(String r3, String r4) throws IOException, SAXException {
        return new InputSource(new StringReader(""));
    }

    public static EpgData parse(InputStream r1, long r2) throws Exception {
        return parse(r1, r2, null);
    }

    public static EpgData parse(InputStream r9, final long r10, final List<Entry> r12) throws Exception {
        final HashSet r0 = new HashSet();
        final HashSet r7 = new HashSet();
        if (r12 == null) goto L17;
        Iterator<Entry> r1 = r12.iterator();
    L6:
        if (r1.hasNext() == false) goto L17;
        Entry r2 = r1.next();
        if (r2.tvgId != null) goto L10;
    L12:
        Iterator r3 = Arrays.asList(new String[]{r2.tvgId, r2.tvgName, r2.title}).iterator();
    L14:
        if (r3.hasNext() == false) goto L6;
        r7.addAll(EpgData.matchKeys((String) r3.next()));
        goto L14
    L10:
        if (r2.tvgId.trim().isEmpty() == true) goto L12;
        r0.add(r2.tvgId.trim());
    L17:
        SAXParserFactory r8 = SAXParserFactory.newInstance();
        r8.setNamespaceAware(false);
        r8.setFeature("http://xml.org/sax/features/external-general-entities", false);
        r8.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        r8.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);     // Catch: SAXException -> L20 ParserConfigurationException -> L22
    L23:
        XMLReader r4 = r8.newSAXParser().getXMLReader();
        r4.setEntityResolver(new XmlTvParser$$ExternalSyntheticLambda0());
        final EpgData r5 = new EpgData();
        r5.loadedAt = r10;
        DefaultHandler r6 = new C10381(r5, r12, r0, r10, r7);
        r4.setContentHandler(r6);
        r4.setErrorHandler(r6);
        r4.parse(new InputSource(r9));
        r5.index();
        return r5;
    }

    public static long time(String r2) throws ParseException {
        return EpgTime.parse(r2);
    }
}
