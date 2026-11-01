package es.antonio.m3u;

import java.net.URI;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes3.dex */
public final class Providers {
    private static final Pattern MARKS = null;
    private static final Pattern PUNCTUATION = null;

    static {
        MARKS = Pattern.compile("\\p{M}");
        PUNCTUATION = Pattern.compile("[^a-z0-9]");
    }

    private Providers() {
    }

    public static String base(String r3) {
        if (r3 != null) goto L5;
        return "Sin proveedor";
    L5:
        String r0 = r3.replaceFirst("\\s*·\\s*(?:(?:DASH|HLS|ISM|ISML|Directo)(?:\\s*/\\s*)?)+$", "").trim();
        String r1 = known(r0);
        if (r1 != null) goto L9;
        return r0;
    L9:
        return r1;
    }

    private static String fallback(Entry r2) {
        String r0 = new URI(Entry.details(r2).url).getHost();     // Catch: Exception -> L30
        if (r0 == null) goto L32;
        String r1 = r0.toLowerCase(Locale.ROOT);     // Catch: Exception -> L30
        if (r1.equals("cdn.avatel.tv") == false) goto L7;
        return "ClicTV";
    L7:
        if (r1.endsWith(".cdn.avatel.tv") == false) goto L9;
        return "ClicTV";
    L9:
        if (r1.equals("clictv.es") == false) goto L11;
        return "ClicTV";
    L11:
        if (r1.endsWith(".clictv.es") == false) goto L14;
        return "ClicTV";
    L14:
        if (r1.equals("media.dssott.com") == false) goto L16;
        return "Disney+";
    L16:
        if (r1.endsWith(".media.dssott.com") == false) goto L19;
        return "Disney+";
    L19:
        if (r1.endsWith(".cdn.telefonica.com") == false) goto L21;
        return "Movistar";
    L21:
        if (r1.endsWith(".emisiondof6.com") == false) goto L32;
        if (r1.contains("movistarplus") == false) goto L32;
        return "Movistar";
    L32:
        if ("Canales".equals(r2.kind) == true) goto L34;
    L40:
        return "Sin proveedor";
    L34:
        if (r2.group == null) goto L40;
        if (r2.group.isEmpty() == true) goto L40;
        if (r2.group.equals("Sin sección") == true) goto L40;
        return r2.group;
    }

    public static Map<Entry, String> index(Catalog r13) {
        IdentityHashMap r0 = new IdentityHashMap(r13.size());
        HashMap r1 = new HashMap();
        HashMap r2 = new HashMap();
        Iterator<Catalog.Source> r3 = r13.sources.iterator();
    L4:
        if (r3.hasNext() == false) goto L52;
        Catalog.Source r4 = r3.next();
        if (r4.provider != null) goto L8;
    L11:
        String r6 = null;
    L12:
        if (r6 == null) goto L16;
        String r7 = known(r6);
        if (r7 == null) goto L16;
        r6 = r7;
    L16:
        Iterator<Entry> r8 = r4.entries.iterator();
    L18:
        if (r8.hasNext() == false) goto L4;
        Entry r9 = r8.next();
        String r10 = r6;
        if (r10 != null) goto L22;
        List r11 = Arrays.asList(new String[]{r4.name, r9.group, r9.tvgId});
    L23:
        Iterator r12 = r11.iterator();
    L25:
        if (r12.hasNext() == false) goto L33;
        String r14 = (String) r12.next();
        if (r2.containsKey(r14) == true) goto L29;
        r2.put(r14, known(r14));
    L29:
        r10 = (String) r2.get(r14);
        if (r10 == null) goto L25;
    L33:
        if (r10 != null) goto L45;
        r10 = fallback(r9);
        if (r10.equals(r9.group) == true) goto L39;
        if (r10.equals("Sin proveedor") == false) goto L45;
    L39:
        if (r4.origin.isEmpty() == true) goto L45;
        if (r4.name == null) goto L45;
        if (r4.name.trim().isEmpty() == true) goto L45;
        r10 = r4.name;
    L45:
        r0.put(r9, r10);
        if ("Canales".equals(r9.kind) == false) goto L18;
        Set r15 = (Set) r1.get(r10);
        if (r15 != null) goto L50;
        r15 = new HashSet();
        r1.put(r10, r15);
    L50:
        r15.add(StreamFormat.label(r9));
        goto L18
    L22:
        r11 = Collections.emptyList();
        goto L23
    L8:
        if (r4.provider.trim().isEmpty() == true) goto L11;
        r6 = r4.provider.trim();
        goto L12
    L52:
        Iterator r5 = r0.entrySet().iterator();
    L54:
        if (r5.hasNext() == false) goto L60;
        Map.Entry r16 = (Map.Entry) r5.next();
        if ("Canales".equals(((Entry) r16.getKey()).kind) == false) goto L54;
        if (((Set) r1.get(r16.getValue())).size() <= 1) goto L54;
        r16.setValue(((String) r16.getValue()) + " · " + StreamFormat.label((Entry) r16.getKey()));
        goto L54
    L60:
        TvProviderFormats220.normalize(r0);
        return r0;
    }

    private static String known(String r5) {
        Pattern r0 = PUNCTUATION;
        Pattern r1 = MARKS;
        if (r5 != null) goto L5;
        String r3 = "";
    L6:
        String r2 = r0.matcher(r1.matcher(Normalizer.normalize(r3, Normalizer.Form.NFD)).replaceAll("").toLowerCase(Locale.ROOT)).replaceAll("");
        if (r2.contains("amazon") == false) goto L9;
        return "Amazon Prime";
    L9:
        if (r2.contains("primevideo") == false) goto L12;
        return "Amazon Prime";
    L12:
        if (r2.contains("hbomax") == false) goto L14;
        return "HBO Max";
    L14:
        if (r2.contains("hbmax") == false) goto L16;
        return "HBO Max";
    L16:
        if (r2.equals("max") == false) goto L18;
        return "HBO Max";
    L18:
        if (r2.equals("hbo") == false) goto L21;
        return "HBO Max";
    L21:
        if (r2.contains("movistar") == false) goto L25;
        return "Movistar";
    L25:
        if (r2.contains("orange") == false) goto L29;
        return "Orange";
    L29:
        if (r2.contains("clictv") == false) goto L33;
        return "ClicTV";
    L33:
        if (r2.contains("netflix") == false) goto L37;
        return "Netflix";
    L37:
        if (r2.contains("disney") == false) goto L41;
        return "Disney+";
    L41:
        if (r2.contains("dazn") == false) goto L45;
        return "DAZN";
    L45:
        if (r2.contains("atresplayer") == false) goto L47;
        return "Atresplayer";
    L47:
        if (r2.contains("a3player") == false) goto L50;
        return "Atresplayer";
    L50:
        if (r2.contains("vodafone") == false) goto L53;
        return "Vodafone";
    L53:
        return null;
    L5:
        r3 = r5;
        goto L6
    }

    public static String name(Entry r3, String r4) {
        Iterator r0 = Arrays.asList(new String[]{r4, r3.group, r3.tvgId}).iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        String r2 = known((String) r0.next());
        if (r2 == null) goto L4;
        return r2;
    L10:
        return fallback(r3);
    }
}
