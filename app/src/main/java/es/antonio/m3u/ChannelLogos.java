package es.antonio.m3u;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes3.dex */
final class ChannelLogos {
    private static final Pattern RX0 = null;
    private static final Pattern RX1 = null;
    private Supplier<List<Entry>> deferred;
    private final Map<String, List<String>> ids;
    private final Map<String, List<String>> names;

    static {
        RX0 = Pattern.compile("(?i)^(?:movistar\\s*\\+?|m\\s*\\+)\\s*");
        RX1 = Pattern.compile("(?i)(?:[ \\t._-]+(?:hls|dash|ism|isml|hevc|h265|50fps|60fps|sd|hd|fhd|uhd|4k|720p?|1080p?))+$");
    }

    ChannelLogos(List<Entry> r2) {
        this.ids = new HashMap();
        this.names = new HashMap();
        index(r2);
    }

    ChannelLogos(Supplier<List<Entry>> r2) {
        this.ids = new HashMap();
        this.names = new HashMap();
        this.deferred = r2;
    }

    private static void add(Set<String> r0, List<String> r1) {
        if (r1 == null) goto L5;
        r0.addAll(r1);
        return;
    }

    private synchronized void ensure() {
        monitor-enter(this);
    L8:
        th = move-exception;
        throw th;
    L4:
        if (this.deferred == null) goto L6;
        List<Entry> r0 = this.deferred.get();     // Catch: Throwable -> L8
        this.deferred = null;     // Catch: Throwable -> L8
        index(r0);     // Catch: Throwable -> L8
    L6:
        monitor-exit(this);
    }

    private void index(List<Entry> r8) {
        Iterator<Entry> r0 = r8.iterator();
    L4:
        if (r0.hasNext() == false) goto L14;
        Entry r1 = r0.next();
        if ("Canales".equals(r1.kind) == false) goto L4;
        if (valid(r1.logo) == false) goto L4;
        put(this.ids, EpgData.normalize(r1.tvgId), r1.logo);
        Iterator r2 = Arrays.asList(new String[]{r1.tvgName, r1.title}).iterator();
    L11:
        if (r2.hasNext() == false) goto L4;
        String r3 = (String) r2.next();
        put(this.names, key(r3), r1.logo);
        goto L11
    }

    static String key(String r4) {
        if (r4 != null) goto L5;
        String r1 = "";
    L6:
        String r2 = RX0.matcher(r1).replaceFirst("MPLUS ");
        return EpgData.baseName(RX1.matcher(r2).replaceAll(""));
    L5:
        r1 = r4.trim();
        goto L6
    }

    static /* synthetic */ List lambda$put$0(String r1) {
        return new ArrayList();
    }

    private static void put(Map<String, List<String>> r3, String r4, String r5) {
        if (r4.isEmpty() == false) goto L5;
        return;
    L5:
        List<String> r0 = r3.computeIfAbsent(r4, new ChannelLogos$$ExternalSyntheticLambda0());
        String r6 = r5.trim();
        if (r0.size() < 8) goto L8;
        return;
    L8:
        if (r0.contains(r6) == true) goto L12;
        r0.add(r6);
        return;
    }

    private static boolean valid(String r4) {
        if (r4 != null) goto L15;
        return false;
    L15:
        URI r1 = new URI(r4.trim());     // Catch: Exception -> L13
        if ("https".equalsIgnoreCase(r1.getScheme()) == false) goto L8;
    L9:
        if (r1.getHost() == null) goto L18;
        return true;
    L18:
        return false;
    L8:
        if ("http".equalsIgnoreCase(r1.getScheme()) == true) goto L9;
        return false;
    L14:
        return false;
    }

    List<String> candidates(Entry r6) {
        if ("Canales".equals(r6.kind) == false) goto L5;
        ensure();
    L5:
        LinkedHashSet r0 = new LinkedHashSet();
        if (valid(r6.logo) == false) goto L9;
        r0.add(r6.logo.trim());
    L9:
        if ("Canales".equals(r6.kind) == false) goto L15;
        add(r0, this.ids.get(EpgData.normalize(r6.tvgId)));
        Iterator r1 = Arrays.asList(new String[]{r6.tvgName, r6.title}).iterator();
    L12:
        if (r1.hasNext() == false) goto L15;
        String r2 = (String) r1.next();
        add(r0, this.names.get(key(r2)));
    L15:
        return new ArrayList(r0);
    }

    String resolve(Entry r3) {
        List<String> r0 = candidates(r3);
        if (r0.isEmpty() == false) goto L9;
        if (r3.logo != null) goto L11;
        return "";
    L11:
        return r3.logo;
    L9:
        return r0.get(0);
    }
}
