package es.antonio.m3u;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
final class DemandEpisodes {
    DemandEpisodes() {
    }

    static List<Entry> load(CatalogIndex r16, String r17, String r18) {
        DemandCatalog r0 = r16.sourceCatalog().demand;
        if (r0 == null) goto L57;
        if (r17 == null) goto L57;
        r0.resolve();
        if (r0.directory == null) goto L57;
        DemandSeriesIndex.Index r4 = DemandSeriesIndex.get(r0, r17);
        if (r4 == null) goto L57;
        Map<DemandCatalog.Rows, int[]> r5 = DemandSeriesIndex.select(r0, r4, r18);     // Catch: RuntimeException -> L53 Throwable -> L55
        ArrayList r6 = new ArrayList();     // Catch: RuntimeException -> L53 Throwable -> L55
        HashSet r7 = new HashSet();     // Catch: RuntimeException -> L53 Throwable -> L55
        Iterator<Catalog.Source> r1 = r0.directory.catalog.sources.iterator();     // Catch: RuntimeException -> L53 Throwable -> L55
    L11:
        if (r1.hasNext() == false) goto L12;
        DemandCatalog.Rows r8 = (DemandCatalog.Rows) r1.next().entries;     // Catch: RuntimeException -> L53 Throwable -> L55
        int[] r9 = r5.get(r8);     // Catch: RuntimeException -> L53 Throwable -> L55
        if (r9 == null) goto L11;
        monitor-enter(r8);     // Catch: RuntimeException -> L53 Throwable -> L55
        DemandFile r10 = new DemandFile(r8.directory.file, "r");     // Catch: Throwable -> L43
        int r11 = r9.length;     // Catch: Throwable -> L37
        int r12 = 0;
    L21:
        if (r12 >= r11) goto L22;
        Entry r13 = r8.readEntry(r10, r9[r12]);     // Catch: Throwable -> L37
        if ("Series".equals(r13.kind) == false) goto L36;
        if (r18.equals(CatalogCounts.series(r13)) == false) goto L36;
        if (r17.equals(r8.directory.providers.get(r13)) == false) goto L36;
        if (r7.add(CatalogCounts.episodeKey(r13)) == false) goto L34;
        r6.add(r13);     // Catch: Throwable -> L37
    L34:
        r12 = r12 + 1;     // Catch: Throwable -> L37
    L36:
        throw new IOException("Series locator mismatch");     // Catch: Throwable -> L37
    L22:
        r10.close();     // Catch: Throwable -> L43
        monitor-exit(r8);     // Catch: Throwable -> L50
    L50:
        th = move-exception;
        throw th;     // Catch: RuntimeException -> L53 Throwable -> L55
    L37:
        th = move-exception;
        Throwable r14 = th;
        r10.close();     // Catch: Throwable -> L41
        throw r14;     // Catch: Throwable -> L41
    L41:
        th = th;
    L45:
        if (r14 == null) goto L49;
        if (r14 == th) goto L94;
        r14.addSuppressed(th);     // Catch: Throwable -> L50
        throw r14;     // Catch: Throwable -> L50
    L94:
        throw r14;     // Catch: Throwable -> L50
    L49:
        throw th;     // Catch: Throwable -> L50
    L43:
        th = th;
        r14 = null;
        goto L45
    L12:
        BlackBox.event("SERIES_DETAIL_EPISODES", "episodes=" + r6.size() + " full_selection_index=false");     // Catch: RuntimeException -> L53 Throwable -> L55
        return r6;
    L55:
        e = move-exception;
        r4.file.delete();
        BlackBox.event("SERIES_DETAIL_FALLBACK", e.getClass().getSimpleName());
    L57:
        ArrayList r2 = new ArrayList();
        HashSet r15 = new HashSet();
        Iterator<Entry> r3 = r16.select("Series", r17, r18, null).iterator();
    L59:
        if (r3.hasNext() == false) goto L60;
        Entry r19 = r3.next();
        if (r18.equals(CatalogCounts.series(r19)) == false) goto L59;
        if (r15.add(CatalogCounts.episodeKey(r19)) == false) goto L59;
        r2.add(r19);
        goto L59
    L60:
        return r2;
    }
}
