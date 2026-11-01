package es.antonio.m3u;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes7.dex */
final class CatalogIndex {
    private final Map<String, List<Entry>> buckets;
    private final Catalog catalog;
    final List<Entry> entries;
    final ChannelLogos logos;
    final Map<Entry, String> providers;
    private final Map<String, Integer> totals;
    final CatalogPlanCache views;

    CatalogIndex(final Catalog r6) {
        this.views = new CatalogPlanCache();
        this.buckets = new HashMap();
        this.totals = new HashMap();
        this.catalog = r6;
        this.entries = r6.entries();
        this.providers = Providers.index(r6);
        this.logos = new ChannelLogos(new CatalogIndex$$ExternalSyntheticLambda1(this, r6));
        HashMap r7 = new HashMap();
        HashMap r0 = new HashMap();
        Iterator<Entry> r1 = this.entries.iterator();
    L4:
        if (r1.hasNext() == false) goto L5;
        Entry r2 = r1.next();
        if ("Series".equals(r2.kind) == false) goto L18;
        Set r3 = (Set) r0.computeIfAbsent(this.providers.get(r2), new CatalogIndex$$ExternalSyntheticLambda2());
        String r4 = CatalogCounts.series(r2);
    L19:
        r3.add(r4);
        goto L4
    L18:
        r3 = (Set) r7.computeIfAbsent(r2.kind, new CatalogIndex$$ExternalSyntheticLambda3());
        r4 = r2.identity();
        goto L19
    L5:
        Iterator r5 = r7.entrySet().iterator();
    L7:
        if (r5.hasNext() == false) goto L8;
        Map.Entry r8 = (Map.Entry) r5.next();
        this.totals.put((String) r8.getKey(), Integer.valueOf(((Set) r8.getValue()).size()));
        goto L7
    L8:
        Iterator r9 = r0.values().iterator();
        int r10 = 0;
    L10:
        if (r9.hasNext() == false) goto L11;
        r10 = r10 + ((Set) r9.next()).size();
        goto L10
    L11:
        this.totals.put("Series", Integer.valueOf(r10));
    }

    CatalogIndex(final Catalog r2, Map<Entry, String> r3, Map<String, Integer> r4) {
        this.views = new CatalogPlanCache();
        this.buckets = new HashMap();
        this.totals = new HashMap();
        this.catalog = r2;
        this.entries = r2.entries();
        this.providers = r3;
        this.logos = new ChannelLogos(new CatalogIndex$$ExternalSyntheticLambda0(this, r2));
        this.totals.putAll(r4);
    }

    private String key(String r2, String r3, String r4, Integer r5) {
        return String.valueOf(r2) + "\u0000" + r3 + "\u0000" + r4 + "\u0000" + r5;
    }

    static /* synthetic */ Set lambda$1(String r0) {
        return new HashSet();
    }

    static /* synthetic */ Set lambda$2(String r0) {
        return new HashSet();
    }

    boolean belongsTo(Entry r2, Set<String> r3) {
        Iterator<String> r4 = matchingSources(r2).iterator();
    L4:
        if (r4.hasNext() == false) goto L5;
        if (r3.contains(r4.next()) == false) goto L4;
        return true;
    L5:
        return false;
    }

    int count(String r2) {
        if (this.catalog.demand != null) goto L5;
        Integer r3 = this.totals.get(r2);
        if (r3 != null) goto L10;
        return 0;
    L10:
        return r3.intValue();
    L5:
        return this.catalog.demand.count(r2);
    }

    Map<String, Integer> counts() {
        if (this.catalog.demand != null) goto L5;
        Map<String, Integer> r0 = this.totals;
    L7:
        return Collections.unmodifiableMap(r0);
    L5:
        r0 = this.catalog.demand.counts;
        goto L7
    }

    List<WatchHistory.Item> history(Context r3, String r4, String r5) {
        if (this.catalog.demand != null) goto L11;
        if (this.catalog.startupCacheVersion == 171) goto L11;
        if (SectionStore.state(this.catalog) != null) goto L11;
        return WatchHistory.inCatalog(r3, select(r4, r5, null, null), r4);
    L11:
        return SectionStore.history(r3, this.catalog, r4, r5);
    }

    /* JADX INFO: renamed from: lambda$0$es-antonio-m3u-CatalogIndex, reason: not valid java name */
    /* synthetic */ List m530lambda$0$esantoniom3uCatalogIndex(Catalog r4) {
        return r4.entriesFor("Canales", null, this.providers);
    }

    /* JADX INFO: renamed from: lambda$3$es-antonio-m3u-CatalogIndex, reason: not valid java name */
    /* synthetic */ List m531lambda$3$esantoniom3uCatalogIndex(Catalog r4) {
        return r4.entriesFor("Canales", null, this.providers);
    }

    Set<String> matchingSources(Entry r10) {
        HashSet r0 = new HashSet();
        String r1 = WatchHistory.m418id(r10);
        Iterator<Catalog.Source> r2 = this.catalog.sources.iterator();
    L4:
        if (r2.hasNext() == false) goto L5;
        Catalog.Source r3 = r2.next();
        if ((r3.entries instanceof DemandCatalog.Rows) == true) goto L9;
        if ((r3.entries instanceof DemandCatalog.Rows) == false) goto L19;
        DemandCatalog.Rows r4 = (DemandCatalog.Rows) r3.entries;
        String r7 = r10.kind;
        if ("Series".equals(r10.kind) == false) goto L16;
        String r6 = CatalogCounts.series(r10);
    L17:
        List<Entry> r5 = r4.select(r7, null, r6, null);
    L29:
        Iterator<Entry> r8 = r5.iterator();
    L31:
        if (r8.hasNext() == false) goto L4;
        Entry r9 = r8.next();
        if (Entry.details(r9).url.equals(Entry.details(r10).url) == false) goto L31;
        if (r9.headers.equals(r10.headers) == false) goto L31;
        if (WatchHistory.m418id(r9).equals(r1) == false) goto L31;
    L10:
        r0.add(r3.f177id);
        goto L4
    L16:
        r6 = null;
        goto L17
    L19:
        if ((r3.entries instanceof SectionStore.DiskEntries) == false) goto L26;
        SectionStore.DiskEntries r11 = (SectionStore.DiskEntries) r3.entries;
        String r12 = r10.kind;
        if ("Series".equals(r10.kind) == false) goto L23;
        String r13 = CatalogCounts.series(r10);
    L24:
        r5 = r11.select(r12, null, r13, null);
        goto L29
    L23:
        r13 = null;
        goto L24
    L26:
        if ((r3.entries instanceof CatalogCache.LazyEntries) == false) goto L28;
        r5 = ((CatalogCache.LazyEntries) r3.entries).select(r10.kind, null);
        goto L29
    L28:
        r5 = r3.entries;
        goto L29
    L9:
        if (((DemandCatalog.Rows) r3.entries).find(r10, null, true) == null) goto L4;
    L5:
        return r0;
    }

    List<Entry> select(String r5, String r6, String r7, Integer r8) {
        if (this.catalog.demand == null) goto L7;
        return this.catalog.demand.select(r5, r6, r7, r8);
    L7:
        if (this.catalog.startupCacheVersion != 171) goto L17;
        ArrayList r0 = new ArrayList();
        Iterator<Catalog.Source> r1 = this.catalog.sources.iterator();
    L10:
        if (r1.hasNext() == false) goto L12;
        Catalog.Source r2 = r1.next();
        if ((r2.entries instanceof DemandCatalog.Rows) == false) goto L10;
        r0.add(((DemandCatalog.Rows) r2.entries).select(r5, r6, r7, r8));
        goto L10
    L12:
        return Catalog.join(r0);
    L17:
        if (SectionStore.state(this.catalog) != null) goto L19;
        String r3 = key(r5, r6, null, null);
        Map<String, List<Entry>> r4 = this.buckets;
        monitor-enter(r4);
        List<Entry> r9 = this.buckets.get(r3);     // Catch: Throwable -> L41
        if (r9 != null) goto L25;
        r9 = this.catalog.entriesFor(r5, r6, this.providers);     // Catch: Throwable -> L41
        this.buckets.put(r3, r9);     // Catch: Throwable -> L41
    L25:
        monitor-exit(r4);     // Catch: Throwable -> L41
        if (r7 != null) goto L29;
        if (r8 != null) goto L29;
        return r9;
    L29:
        ArrayList r10 = new ArrayList();
        Iterator<Entry> r11 = r9.iterator();
    L31:
        if (r11.hasNext() == false) goto L32;
        Entry r12 = r11.next();
        if (r7 == null) goto L37;
        if (r7.equals(CatalogCounts.series(r12)) == false) goto L31;
    L37:
        if (r8 == null) goto L40;
        if (Math.max(0, r12.season) != r8.intValue()) goto L31;
    L40:
        r10.add(r12);
        goto L31
    L32:
        return r10;
    L41:
        th = move-exception;
        throw th;
    L19:
        return SectionStore.select(this.catalog, r5, r6, r7, r8);
    }

    Catalog sourceCatalog() {
        return this.catalog;
    }
}
