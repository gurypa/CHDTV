package es.antonio.m3u;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes7.dex */
final class DemandNavigation {

    static final class Bucket {
        Ref first;
        final Set<String> keys;
        String provider;
        final Set<Integer> seasons;

        Bucket() {
            this.keys = new HashSet();
            this.seasons = new HashSet();
        }
    }

    static final class Ref {
        final int row;
        final DemandCatalog.Rows rows;

        Ref(DemandCatalog.Rows r1, int r2) {
            this.rows = r1;
            this.row = r2;
        }

        Entry entry() {
            return this.rows.get(this.row);
        }
    }

    private DemandNavigation() {
    }

    static MainActivity.CatalogPlan menu(DemandCatalog r20, MainActivity.CatalogRequest r21) {
        DemandCatalog.Directory r2 = r20.directory;
        TreeMap r3 = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        TreeSet r4 = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        if (r21.provider != null) goto L5;
        boolean r5 = true;
    L6:
        Iterator<Catalog.Source> r6 = r2.catalog.sources.iterator();
    L7:
        String r8 = "Series";
        if (r6.hasNext() == false) goto L78;
        Catalog.Source r7 = r6.next();
        DemandCatalog.Rows r9 = (DemandCatalog.Rows) r7.entries;
        if (r7.kind != null) goto L12;
    L110:
        DemandFile r10 = new DemandFile(r9.directory.file, "r");     // Catch: IOException -> L75
        int r15 = 0;
    L108:
    L66:
        th = th;
        DemandFile r11 = r10;
    L68:
        Throwable r1 = th;
        r11.close();     // Catch: Throwable -> L71
    L127:
        throw r1;     // Catch: IOException -> L75
    L71:
        th = move-exception;
        r1.addSuppressed(th);     // Catch: IOException -> L75
        throw r1;     // Catch: IOException -> L75
    L19:
        if (r15 >= r9.count) goto L63;
        int r14 = Math.min(1024, r9.count - r15);     // Catch: Throwable -> L66
        byte[] r12 = new byte[r14 * 72];     // Catch: Throwable -> L66
        r11 = r10;
        r11.seek(r9.records + (((long) r15) * 72));     // Catch: Throwable -> L61
        r11.readFully(r12);     // Catch: Throwable -> L61
        IntBuffer r13 = ByteBuffer.wrap(r12).asIntBuffer();     // Catch: Throwable -> L61
        int r16 = 0;
    L23:
        if (r16 >= r14) goto L60;
        int r17 = r16 * 18;     // Catch: Throwable -> L61
        Iterator<Catalog.Source> r18 = r6;
        if (r21.kind.equals(r9.directory.value(r11, r13.get(r17 + 4))) == true) goto L27;
        r4 = r4;
        int r19 = r16;
        int r110 = r14;
        int r0 = r15;
    L59:
        r16 = r19 + 1;
        r15 = r0;
        r6 = r18;
        r8 = r8;
        r14 = r110;
        r4 = r4;
        goto L23
    L27:
        r19 = r16;
        r110 = r14;
        r0 = r15;
        String r111 = TvProviderFormats220.record(r9.directory.value(r11, r13.get(r17 + 17)), r21.kind, r9.directory, r11, r13, r17);     // Catch: Throwable -> L61
        if (r21.provider != null) goto L30;
    L32:
        int r112 = r17 + 1;     // Catch: Throwable -> L61
        String r113 = r9.directory.value(r11, r13.get(r112));     // Catch: Throwable -> L61
        r4.add(r113);     // Catch: Throwable -> L61
        String r114 = r9.directory.value(r11, r13.get(r17 + 5));     // Catch: Throwable -> L61
        if (r114 != null) goto L35;
    L36:
        r13.get(r112);     // Catch: Throwable -> L61
        r114 = r113;
    L37:
        if (r5 == false) goto L39;
        String r115 = r111;
    L40:
        Bucket r116 = (Bucket) r3.get(r115);     // Catch: Throwable -> L61
        if (r116 != null) goto L46;
        r116 = new Bucket();     // Catch: Throwable -> L61
        r116.first = new Ref(r9, r0 + r19);     // Catch: Throwable -> L61
        r116.provider = r111;     // Catch: Throwable -> L61
        r3.put(r115, r116);     // Catch: Throwable -> L61
    L46:
        if (r5 == false) goto L52;
        Set<String> r117 = r116.keys;     // Catch: Throwable -> L61
        if (r8.equals(r21.kind) == false) goto L50;
        String r118 = r114;
    L51:
        r117.add(r118);     // Catch: Throwable -> L61
        goto L59
    L50:
        r118 = r113 + "\n" + r9.directory.value(r11, r13.get(r17));     // Catch: Throwable -> L61
        goto L51
    L52:
        int r119 = r13.get(r17 + 13);     // Catch: Throwable -> L61
        int r120 = r13.get(r17 + 14);     // Catch: Throwable -> L61
        r116.seasons.add(Integer.valueOf(r119));     // Catch: Throwable -> L61
        if (r119 < 0) goto L57;
        if (r120 < 0) goto L57;
        StringBuilder r22 = new StringBuilder().append(r119).append(":").append(r120);     // Catch: Throwable -> L61
    L58:
        r116.keys.add(r22.toString());     // Catch: Throwable -> L61
    L57:
        r22 = new StringBuilder().append(r113).append("\n").append(r9.directory.value(r11, r13.get(r17)));     // Catch: Throwable -> L61
        goto L58
    L39:
        r115 = r114;
        goto L40
    L35:
        if (r114.isEmpty() == false) goto L37;
    L30:
        if (r21.provider.equals(r111) == true) goto L32;
        r4 = r4;
        goto L59
    L60:
        r15 = r15 + 1024;
        r10 = r11;
    L61:
        th = th;
        goto L68
    L63:
        Iterator<Catalog.Source> r121 = r6;
        TreeSet r122 = r4;
        r10.close();     // Catch: IOException -> L75
        r6 = r121;
        r4 = r122;
    L75:
        e = move-exception;
        throw new IllegalStateException("No se pudo leer el índice de la sección", e);
    L12:
        if (r7.kind.equals("Mixta") == true) goto L110;
        if (r7.kind.equals(r21.kind) == true) goto L110;
    L78:
        String r123 = "Series";
        ArrayList r23 = new ArrayList();
        r23.add("Todas las secciones");
        r23.addAll(r4);
        if (r23.contains(r21.section) == false) goto L81;
        String r24 = r21.section;
    L83:
        if (r24.equals("Todas las secciones") == true) goto L86;
        return null;
    L86:
        ArrayList r25 = new ArrayList();
        Iterator r26 = r3.entrySet().iterator();
    L88:
        if (r26.hasNext() == false) goto L104;
        Map.Entry r27 = (Map.Entry) r26.next();
        Bucket r28 = (Bucket) r27.getValue();
        Entry r124 = r28.first.entry();
        String r125 = (String) r27.getKey();
        if (r5 == true) goto L92;
        String r126 = r123;
        r25.add(new MainActivity.Row(r125, r28.seasons.size() + " temporadas · " + r28.keys.size() + " episodios", (String) r27.getKey(), r124, 1));
    L103:
        r123 = r126;
        goto L88
    L92:
        if (Providers.base(r125).equals(r125) == false) goto L94;
        r125 = r125 + " · " + StreamFormat.label(r124);
    L94:
        StringBuilder r29 = new StringBuilder().append(r28.keys.size());
        r126 = r123;
        if (r126.equals(r21.kind) == false) goto L98;
        String r30 = " series";
    L101:
        r25.add(new MainActivity.Row(r125, r29.append(r30).toString(), (String) r27.getKey(), r124, 3));
        goto L103
    L98:
        if ("Canales".equals(r21.kind) == false) goto L100;
        r30 = " canales";
        goto L101
    L100:
        r30 = " películas";
        goto L101
    L104:
        BlackBox.event("CATALOG_DEMAND_MENU", "kind=" + r21.kind + " rows=" + r25.size() + " created_entries=" + r20.materialized());
        MainActivity.CatalogPlan r31 = new MainActivity.CatalogPlan(r25, r23, r24);
        TvProviders.save(r20, r21, r31);
        DemandMenus.save(r20, r21, r31);
        return r31;
    L81:
        r24 = (String) r23.get(0);
        goto L83
    L5:
        r5 = false;
        goto L6
    }

    static MainActivity.CatalogPlan prepare(MainActivity.CatalogRequest r4) {
        Catalog r0 = r4.index.sourceCatalog();
        if (r0.demand != null) goto L5;
        return null;
    L5:
        DemandCatalog.sectionOpened = true;
        MainActivity.CatalogPlan r1 = TvProviders.load(r0.demand, r4, true);
        if (r1 == null) goto L8;
        return r1;
    L8:
        MainActivity.CatalogPlan r2 = DemandSeriesMenu.prepare(r0.demand, r4);
        if (r2 == null) goto L11;
        return r2;
    L11:
        MainActivity.CatalogPlan r3 = DemandMovies.prepare(r0.demand, r4);
        if (r3 == null) goto L14;
        return r3;
    L14:
        MainActivity.CatalogPlan r5 = DemandMenus.load(r0.demand, r4);
        if (r5 == null) goto L18;
        return r5;
    L18:
        if ("Series".equals(r4.kind) == false) goto L22;
        MainActivity.CatalogPlan r6 = DemandSeries.prepare(r0.demand, r4);
        if (r6 == null) goto L22;
        return r6;
    L22:
        r0.demand.resolve();
        if (r0.demand.directory != null) goto L25;
    L37:
        return null;
    L25:
        if (r4.favorites == true) goto L37;
        if (r4.filter.isEmpty() == false) goto L37;
        if (r4.series != null) goto L37;
        if (r4.provider == null) goto L36;
        if ("Series".equals(r4.kind) == false) goto L37;
    L36:
        return menu(r0.demand, r4);
    }

    static List<Entry> related(DemandCatalog r18, Entry r19) {
        r18.resolve();
        if (r18.directory == null) goto L5;
        if (r19 != null) goto L8;
        String r1 = "Películas";
    L9:
        Throwable r5 = null;
        if ("Series".equals(r1) == false) goto L12;
        DemandCatalog.Directory r0 = r18.directory;
        LinkedHashMap r2 = new LinkedHashMap();
        Iterator<Catalog.Source> r3 = r0.catalog.sources.iterator();
    L15:
        if (r3.hasNext() == false) goto L16;
        Catalog.Source r4 = r3.next();
        DemandCatalog.Rows r6 = (DemandCatalog.Rows) r4.entries;
        if (r4.kind != null) goto L24;
    L63:
        DemandFile r7 = new DemandFile(r6.directory.file, "r");     // Catch: Throwable -> L51
        int r9 = 0;
    L67:
        if (r9 >= r6.count) goto L61;
        int r10 = Math.min(1024, r6.count - r9);     // Catch: Throwable -> L45
        byte[] r11 = new byte[r10 * 72];     // Catch: Throwable -> L45
        r7.seek(r6.records + (((long) r9) * 72));     // Catch: Throwable -> L45
        r7.readFully(r11);     // Catch: Throwable -> L45
        IntBuffer r12 = ByteBuffer.wrap(r11).asIntBuffer();     // Catch: Throwable -> L45
        int r13 = 0;
    L35:
        if (r13 >= r10) goto L36;
        int r14 = r13 * 18;     // Catch: Throwable -> L45
        if ("Series".equals(r6.directory.value(r7, r12.get(r14 + 4))) == false) goto L44;
        String r8 = String.valueOf(r4.f177id) + "\n" + r6.directory.value(r7, r12.get(r14 + 17)) + "\n" + r6.series(r7, r12, r14);     // Catch: Throwable -> L45
        if (r2.containsKey(r8) == true) goto L44;
        r2.put(r8, new Ref(r6, r9 + r13));     // Catch: Throwable -> L45
    L44:
        r13 = r13 + 1;
        r5 = null;
        goto L35
    L36:
        r9 = r9 + 1024;     // Catch: Throwable -> L45
        goto L67
    L61:
        r7.close();     // Catch: Throwable -> L49
        goto L15
    L45:
        th = move-exception;
        r5 = th;
        r7.close();     // Catch: Throwable -> L49
        throw r5;     // Catch: Throwable -> L49
    L49:
        th = th;
    L53:
        if (r5 == null) goto L57;
        if (r5 == th) goto L75;
        r5.addSuppressed(th);     // Catch: IOException -> L58
        throw r5;     // Catch: IOException -> L58
    L58:
        e = move-exception;
        throw new IllegalStateException(e);
    L75:
        throw r5;     // Catch: IOException -> L58
    L57:
        throw th;     // Catch: IOException -> L58
    L51:
        th = th;
        r5 = null;
        goto L53
    L24:
        if (r4.kind.equals("Mixta") == true) goto L63;
        if (r4.kind.equals("Series") == true) goto L63;
    L16:
        ArrayList r15 = new ArrayList();
        Iterator r16 = r2.values().iterator();
    L18:
        if (r16.hasNext() == false) goto L19;
        r15.add(((Ref) r16.next()).entry());
        goto L18
    L19:
        return r15;
    L12:
        return r18.select(r1, null, null, null);
    L8:
        r1 = r19.kind;
        goto L9
    L5:
        return SectionStore.related(r18.resolved.entries(), r19);
    }
}
