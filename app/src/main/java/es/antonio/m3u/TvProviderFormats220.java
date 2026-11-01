package es.antonio.m3u;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
final class TvProviderFormats220 {
    private TvProviderFormats220() {
    }

    static String fields(String r2, String r3, String r4, String r5) {
        if ("Canales".equals(r3) == true) goto L5;
        return r2;
    L5:
        Entry r0 = new Entry(false);
        r0.kind = r3;
        r0.manifest = r4;
        r0.url = r5;
        return label(r2, r0);
    }

    static String label(String r2, Entry r3) {
        if ("Canales".equals(r3.kind) == false) goto L8;
        if (r2 == null) goto L8;
        return Providers.base(r2) + " · " + StreamFormat.label(r3);
    L8:
        return r2;
    }

    static Map<Entry, String> normalize(Map<Entry, String> r4) {
        Iterator<Map.Entry<Entry, String>> r0 = r4.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L8;
        Map.Entry<Entry, String> r1 = r0.next();
        if ("Canales".equals(r1.getKey().kind) == false) goto L4;
        r1.setValue(label(r1.getValue(), r1.getKey()));
        goto L4
    L8:
        return r4;
    }

    static String record(String r1, String r2, DemandCatalog.Directory r3, DemandFile r4, IntBuffer r5, int r6) throws IOException {
        if ("Canales".equals(r2) == true) goto L6;
        return r1;
    L6:
        return fields(r1, r2, r3.value(r4, r5.get(r6 + 6)), r3.value(r4, r5.get(r6 + 3)));
    }

    static String saved(String r2, String r3, DemandCatalog.Directory r4, DemandFile r5, int[] r6) throws IOException {
        if ("Canales".equals(r3) == true) goto L6;
        return r2;
    L6:
        return fields(r2, r3, r4.value(r5, r6[6]), r4.value(r5, r6[3]));
    }
}
