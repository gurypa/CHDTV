package es.antonio.m3u;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CancellationException;

/* JADX INFO: loaded from: classes3.dex */
final class ChannelFallback {
    final Entry anchor;
    final Set<String> tried;

    static final class Choice {
        final Entry entry;
        final String list;

        Choice(Entry r1, String r2) {
            this.entry = r1;
            this.list = r2;
        }
    }

    ChannelFallback(Entry r2) {
        this.tried = new LinkedHashSet();
        this.anchor = r2;
    }

    static List<Entry> channels(Catalog.Source r3) {
        if (r3.kind == null) goto L11;
        if ("Canales".equals(r3.kind) == true) goto L11;
        if ("Mixta".equals(r3.kind) == true) goto L11;
        return Collections.emptyList();
    L11:
        if ((r3.entries instanceof DemandCatalog.Rows) == false) goto L15;
        return ((DemandCatalog.Rows) r3.entries).select("Canales", null, null, null);
    L15:
        if ((r3.entries instanceof SectionStore.DiskEntries) == false) goto L19;
        return ((SectionStore.DiskEntries) r3.entries).select("Canales", null, null, null);
    L19:
        if ((r3.entries instanceof CatalogCache.LazyEntries) == false) goto L23;
        return ((CatalogCache.LazyEntries) r3.entries).select("Canales", null);
    L23:
        return r3.entries;
    }

    static String name(String r4) {
        if (r4 != null) goto L5;
        String r1 = "";
    L6:
        String r0 = Normalizer.normalize(r1, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT).trim().replaceAll("(?:\\s+|(?<=\\d))(?:full\\s*hd|fhd|uhd|hd|sd|4k|1080p|720p)\\s*$", "").replaceFirst("^(?:movistar\\s*plus|movistar|m\\s*\\+)\\s*", "mplus").replaceAll("[^\\p{L}\\p{N}]", "");
        if (r0.startsWith("mplus") == false) goto L12;
        if (r0.length() <= 5) goto L12;
        return r0.substring(5);
    L12:
        return r0;
    L5:
        r1 = r4;
        goto L6
    }

    static int rank(Entry r4, Catalog.Source r5, Entry r6, Catalog.Source r7) {
        if ("Canales".equals(r4.kind) == true) goto L5;
        return -1;
    L5:
        if ("Canales".equals(r6.kind) == false) goto L35;
        String r1 = "";
        if (r4.tvgId != null) goto L10;
        String r0 = "";
    L12:
        if (r6.tvgId == null) goto L16;
        r1 = r6.tvgId.trim();
    L16:
        if (r0.isEmpty() == true) goto L30;
        if (r0.equals(r1) == false) goto L30;
        if (uniqueId(r5, r0) == false) goto L30;
        if (uniqueId(r7, r0) == false) goto L30;
        if (r0.matches("[0-9]+") == true) goto L26;
        return 0;
    L26:
        if (sameName(r4, r6) == false) goto L30;
        return 0;
    L30:
        if (sameName(r4, r6) == false) goto L34;
        return 1;
    L34:
        return -1;
    L10:
        r0 = r4.tvgId.trim();
        goto L12
    L35:
        return -1;
    }

    static String route(Entry r14) {
        MessageDigest r0 = MessageDigest.getInstance("SHA-256");     // Catch: Exception -> L27
        StringBuilder r1 = new StringBuilder();     // Catch: Exception -> L27
        Iterator r2 = Arrays.asList(new String[]{Entry.details(r14).url, r14.manifest, r14.licenseType, Entry.details(r14).license}).iterator();     // Catch: Exception -> L27
    L4:
        if (r2.hasNext() == false) goto L6;
        String r3 = (String) r2.next();     // Catch: Exception -> L27
        r1.append(r3.length()).append(':').append(r3);     // Catch: Exception -> L27
        goto L4
    L6:
        TreeMap r4 = new TreeMap();     // Catch: Exception -> L27
        Iterator<Map.Entry<String, String>> r5 = r14.headers.entrySet().iterator();     // Catch: Exception -> L27
    L8:
        if (r5.hasNext() == false) goto L10;
        Map.Entry<String, String> r6 = r5.next();     // Catch: Exception -> L27
        r4.put(r6.getKey().toLowerCase(Locale.ROOT), r6.getValue());     // Catch: Exception -> L27
        goto L8
    L10:
        Iterator r7 = r4.entrySet().iterator();     // Catch: Exception -> L27
    L12:
        if (r7.hasNext() == false) goto L14;
        Map.Entry r8 = (Map.Entry) r7.next();     // Catch: Exception -> L27
        r1.append(((String) r8.getKey()).length()).append(':').append((String) r8.getKey()).append(((String) r8.getValue()).length()).append(':').append((String) r8.getValue());     // Catch: Exception -> L27
        goto L12
    L14:
        TreeMap r9 = new TreeMap();     // Catch: Exception -> L27
        Iterator<Map.Entry<String, String>> r10 = r14.licenseRequestHeaders().entrySet().iterator();     // Catch: Exception -> L27
    L16:
        if (r10.hasNext() == false) goto L18;
        Map.Entry<String, String> r11 = r10.next();     // Catch: Exception -> L27
        r9.put(r11.getKey().toLowerCase(Locale.ROOT), r11.getValue());     // Catch: Exception -> L27
        goto L16
    L18:
        r1.append("licenseHeaders:");     // Catch: Exception -> L27
        Iterator r12 = r9.entrySet().iterator();     // Catch: Exception -> L27
    L20:
        if (r12.hasNext() == false) goto L22;
        Map.Entry r13 = (Map.Entry) r12.next();     // Catch: Exception -> L27
        r1.append(((String) r13.getKey()).length()).append(':').append((String) r13.getKey()).append(((String) r13.getValue()).length()).append(':').append((String) r13.getValue());     // Catch: Exception -> L27
        goto L20
    L22:
        byte[] r15 = r0.digest(r1.toString().getBytes(StandardCharsets.UTF_8));     // Catch: Exception -> L27
        StringBuilder r16 = new StringBuilder();     // Catch: Exception -> L27
        int r17 = r15.length;     // Catch: Exception -> L27
        int r18 = 0;
    L23:
        if (r18 >= r17) goto L25;
        r16.append(String.format(Locale.ROOT, "%02x", new Object[]{Integer.valueOf(r15[r18] & 255)}));     // Catch: Exception -> L27
        r18 = r18 + 1;     // Catch: Exception -> L27
        goto L23
    L25:
        return r16.toString();
    L27:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    static boolean sameFormat(Entry r0, Entry r1) {
        return Objects.equals(StreamFormat.mime(Entry.details(r0)), StreamFormat.mime(Entry.details(r1)));
    }

    static boolean sameName(Entry r2, Entry r3) {
        String r0 = name(r2.title);
        if (r0.isEmpty() == false) goto L5;
    L7:
        return false;
    L5:
        if (r0.equals(name(r3.title)) == false) goto L7;
        return true;
    }

    static Catalog.Source source(Catalog r6, Entry r7) {
        String r0 = route(Entry.details(r7));
        Iterator<Catalog.Source> r8 = r6.sources.iterator();
    L4:
        if (r8.hasNext() == false) goto L22;
        Catalog.Source r1 = r8.next();
        Iterator<Entry> r2 = channels(r1).iterator();
    L7:
        if (r2.hasNext() == false) goto L4;
        Entry r3 = r2.next();
        if (Thread.currentThread().isInterrupted() == true) goto L21;
        if ("Canales".equals(r3.kind) == false) goto L7;
        if (r3 == r7) goto L19;
        if (r3.title.equals(r7.title) == false) goto L7;
        if (sameFormat(r7, r3) == false) goto L7;
        if (route(Entry.details(r3)).equals(r0) == false) goto L7;
    L19:
        return r1;
    L21:
        throw new CancellationException();
    L22:
        return null;
    }

    static boolean uniqueId(Catalog.Source r5, String r6) {
        if (r5 != null) goto L5;
        return false;
    L5:
        if (r6.isEmpty() == true) goto L37;
        Iterator<Entry> r7 = channels(r5).iterator();
        int r1 = 0;
    L9:
        if (r7.hasNext() == false) goto L24;
        Entry r2 = r7.next();
        if (Thread.currentThread().isInterrupted() == true) goto L22;
        if ("Canales".equals(r2.kind) == false) goto L9;
        if (r2.tvgId != null) goto L17;
        String r3 = "";
    L19:
        if (r6.equals(r3) == false) goto L9;
        r1 = r1 + 1;
        goto L9
    L17:
        r3 = r2.tvgId.trim();
        goto L19
    L22:
        throw new CancellationException();
    L24:
        if (r1 == 1) goto L26;
        return false;
    L26:
        return true;
    L37:
        return false;
    }

    Choice next(Catalog r11, Entry r12) {
        Choice r2 = null;
        if ("Canales".equals(this.anchor.kind) == true) goto L5;
        return null;
    L5:
        this.tried.add(route(Entry.details(r12)));
        Catalog.Source r0 = source(r11, this.anchor);
        Catalog.Source r13 = source(r11, r12);
        Iterator<Catalog.Source> r14 = r11.sources.iterator();
        int r3 = 2;
    L7:
        if (r14.hasNext() == false) goto L33;
        Catalog.Source r4 = r14.next();
        if (r13 == null) goto L13;
        if (r4.f177id.equals(r13.f177id) == true) goto L7;
    L13:
        Iterator<Entry> r5 = channels(r4).iterator();
    L15:
        if (r5.hasNext() == false) goto L7;
        Entry r6 = r5.next();
        if (Thread.currentThread().isInterrupted() == true) goto L31;
        if ("Canales".equals(r6.kind) == false) goto L15;
        sameFormat(this.anchor, r6);
        if (1 == 0) goto L15;
        int r7 = rank(this.anchor, r0, r6, r4);
        if (r7 < 0) goto L15;
        if (r7 >= r3) goto L15;
        if (this.tried.contains(route(Entry.details(r6))) == true) goto L15;
        r2 = new Choice(r6, r4.name);
        r3 = r7;
        goto L15
    L31:
        throw new CancellationException();
    L33:
        if (r2 == null) goto L35;
        this.tried.add(route(Entry.details(r2.entry)));
    L35:
        return r2;
    }
}
