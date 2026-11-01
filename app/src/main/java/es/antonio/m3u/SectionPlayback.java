package es.antonio.m3u;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
final class SectionPlayback {

    static final class Ready implements Runnable {
        final PlayerActivity activity;
        final Catalog catalog;
        final List<Entry> entries;
        final Map<Entry, String> providers;
        final Entry selected;
        final PlaybackSequence sequence;

        Ready(PlayerActivity r1, List<Entry> r2, Catalog r3, Map<Entry, String> r4, Entry r5, PlaybackSequence r6) {
            this.activity = r1;
            this.entries = r2;
            this.catalog = r3;
            this.providers = r4;
            this.selected = r5;
            this.sequence = r6;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.activity.sectionReady(this.entries, this.catalog, this.providers, this.selected, this.sequence);
        }
    }

    SectionPlayback() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r15v0 */
    /* JADX WARN: Type inference failed for: r15v1 */
    /* JADX WARN: Type inference failed for: r15v2 */
    /* JADX WARN: Type inference failed for: r1v0, types: [es.antonio.m3u.Catalog] */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v16 */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Type inference failed for: r4v1, types: [es.antonio.m3u.Catalog] */
    /* JADX WARN: Type inference failed for: r4v6 */
    static void load(PlayerActivity r18, Entry r19) {
        long r11 = System.nanoTime();
        ?? r1 = new Catalog();
        IdentityHashMap r13 = new IdentityHashMap();
        ArrayList r14 = new ArrayList();
        ?? r15 = 1000000(0xf4240, double:4.940656E-318);
        Catalog r7 = new AppStorage(r18.getApplicationContext()).catalog();     // Catch: Exception -> L45
        CatalogIndex r2 = AppStorage.indexFor(r7);     // Catch: Exception -> L42
        if ("Películas".equals(r19.kind) == false) goto L15;
        if (r7.demand == null) goto L15;
        PlaybackSequence r6 = DemandMovies.playback(r7.demand, r19);     // Catch: Exception -> L42
        if (r6 == null) goto L15;
        BlackBox.event("PLAYBACK_MOVIES_LAZY", "entries=" + r6.entries.size() + " created_entries=" + r7.demand.materialized() + " duration_ms=" + ((System.nanoTime() - r11) / 1000000));     // Catch: Exception -> L42
        r18.runOnUiThread(new Ready(r18, r6.entries, r7, r2.providers, r19, r6));     // Catch: Exception -> L40
        return;
    L15:
        if ("Películas".equals(r19.kind) == true) goto L17;
    L27:
        String r0 = null;
    L28:
        String r3 = r19.kind;     // Catch: Exception -> L40
        if ("Series".equals(r19.kind) == false) goto L31;
        String r4 = CatalogCounts.series(r19);     // Catch: Exception -> L40
    L32:
        Iterator<Entry> r5 = r2.select(r3, r0, r4, null).iterator();     // Catch: Exception -> L40
    L34:
        if (r5.hasNext() == false) goto L35;
        Entry r8 = r5.next();     // Catch: Exception -> L40
        r14.add(r8);     // Catch: Exception -> L40
        String r9 = r2.providers.get(r8);     // Catch: Exception -> L40
        if (r9 == null) goto L34;
        r13.put(r8, r9);     // Catch: Exception -> L40
        goto L34
    L35:
        ?? r10 = r7;
    L47:
        PlaybackSequence r12 = new PlaybackSequence(r14, r19, r13);
        BlackBox.event("PLAYBACK_SECTION_READY", "kind=" + r19.kind + " entries=" + r14.size() + " providers=" + r13.size() + " sequence=" + r12.entries.size() + " duration_ms=" + ((System.nanoTime() - r11) / 1000000));
        r18.runOnUiThread(new Ready(r18, r14, r10, r13, r19, r12));
        return;
    L31:
        r4 = null;
        goto L32
    L17:
        if (r7.demand == null) goto L27;
        r7.demand.resolve();     // Catch: Exception -> L40
        if (r7.demand.directory == null) goto L27;
        Iterator<Catalog.Source> r16 = r7.demand.directory.catalog.sources.iterator();     // Catch: Exception -> L40
    L22:
        if (r16.hasNext() == false) goto L27;
        Entry r17 = ((DemandCatalog.Rows) r16.next().entries).find(r19, null, true);     // Catch: Exception -> L40
        if (r17 == null) goto L22;
        r0 = r7.demand.directory.providers.get(r17);     // Catch: Exception -> L40
    L40:
        e = e;
    L44:
        r1 = r15;
    L46:
        BlackBox.event("PLAYBACK_CATALOG_ERROR", e.getClass().getSimpleName());
        r10 = r1;
    L42:
        e = e;
        r15 = r7;
    L45:
        e = e;
        goto L46
    }
}
