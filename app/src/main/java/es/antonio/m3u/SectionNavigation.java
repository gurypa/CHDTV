package es.antonio.m3u;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes7.dex */
final class SectionNavigation {
    SectionNavigation() {
    }

    static /* synthetic */ int lambda$0(Entry r3, Entry r4) {
        int r0 = String.CASE_INSENSITIVE_ORDER.compare(r3.group, r4.group);
        if (r0 != 0) goto L5;
        r0 = Integer.compare(r3.season, r4.season);
    L5:
        if (r0 != 0) goto L7;
        r0 = Integer.compare(r3.episode, r4.episode);
    L7:
        if (r0 == 0) goto L9;
        return r0;
    L9:
        return String.CASE_INSENSITIVE_ORDER.compare(r3.title, r4.title);
    }

    static /* synthetic */ List lambda$1(String r0) {
        return new ArrayList();
    }

    static MainActivity.CatalogPlan prepare(MainActivity.CatalogRequest r17) {
        MainActivity.CatalogPlan r1 = DemandNavigation.prepare(r17);
        if (r1 == null) goto L5;
        return r1;
    L5:
        Catalog r2 = r17.index.sourceCatalog();
        if (r2.demand == null) goto L8;
        r2 = r2.demand.resolve();
    L8:
        SectionStore.State r3 = SectionStore.state(r2);
        if (r3 != null) goto L11;
    L85:
        return null;
    L11:
        if ("Series".equals(r17.kind) == false) goto L85;
        if (r17.series != null) goto L85;
        if (r17.favorites == true) goto L85;
        if (r17.filter.isEmpty() == false) goto L85;
        TreeSet r4 = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        Iterator<SectionStore.SourceMeta> r5 = r3.meta.sources.iterator();
    L21:
        if (r5.hasNext() == false) goto L22;
        Iterator<SectionStore.Part> r6 = r5.next().parts.iterator();
    L76:
        if (r6.hasNext() == false) goto L21;
        SectionStore.Part r7 = r6.next();
        if ("Series".equals(r7.kind) == false) goto L76;
        if (r17.provider == null) goto L84;
        if (r17.provider.equals(r7.provider) == false) goto L76;
    L84:
        r4.addAll(r7.groups);
        goto L76
    L22:
        ArrayList r8 = new ArrayList();
        r8.add("Todas las secciones");
        r8.addAll(r4);
        if (r8.contains(r17.section) == false) goto L25;
        String r9 = r17.section;
    L26:
        String r10 = r9;
        if ("Todas las secciones".equals(r10) == true) goto L29;
        return null;
    L29:
        TreeMap r11 = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        Iterator<SectionStore.SourceMeta> r12 = r3.meta.sources.iterator();
    L31:
        if (r12.hasNext() == false) goto L32;
        Iterator<SectionStore.Part> r13 = r12.next().parts.iterator();
    L61:
        if (r13.hasNext() == false) goto L31;
        SectionStore.Part r14 = r13.next();
        if ("Series".equals(r14.kind) == false) goto L61;
        if (r17.provider == null) goto L70;
        if (r17.provider.equals(r14.provider) == false) goto L61;
    L70:
        if (r17.provider != null) goto L72;
        String r15 = r14.provider;
    L73:
        ((List) r11.computeIfAbsent(r15, new SectionNavigation$$ExternalSyntheticLambda0())).add(r14);
        goto L61
    L72:
        r15 = r14.series;
        goto L73
    L32:
        ArrayList r16 = new ArrayList();
        Iterator r18 = r11.entrySet().iterator();
    L34:
        if (r18.hasNext() == false) goto L35;
        Map.Entry r19 = (Map.Entry) r18.next();
        List r20 = (List) r19.getValue();
        SectionStore.Part r21 = (SectionStore.Part) r20.get(0);
        Entry r110 = r3.read(r21.representative, r21.representativeLength).get(0);     // Catch: IOException -> L56
        r3.providers.values.put(r110, r21.provider);
        if (r17.provider == null) goto L41;
        HashSet r22 = new HashSet();
        HashSet r111 = new HashSet();
        Iterator r23 = r20.iterator();
    L48:
        if (r23.hasNext() == false) goto L49;
        SectionStore.Part r112 = (SectionStore.Part) r23.next();
        r22.addAll(r3.episodeKeys(r112));     // Catch: IOException -> L53
        r111.addAll(r112.seasons);
    L53:
        e = move-exception;
        SectionStore.index(r3.data.getParentFile()).delete();
        throw new IllegalStateException(e);
    L49:
        r16.add(new MainActivity.Row((String) r19.getKey(), String.valueOf(r111.size()) + " temporadas · " + r22.size() + " episodios", (String) r19.getKey(), r110, 1));
        goto L34
    L41:
        HashSet r24 = new HashSet();
        Iterator r113 = r20.iterator();
    L43:
        if (r113.hasNext() == false) goto L44;
        r24.add(((SectionStore.Part) r113.next()).series);
        goto L43
    L44:
        r16.add(new MainActivity.Row((String) r19.getKey(), String.valueOf(r24.size()) + " series", (String) r19.getKey(), r110, 3));
    L56:
        e = move-exception;
        SectionStore.index(r3.data.getParentFile()).delete();
        throw new IllegalStateException(e);
    L35:
        BlackBox.event("CATALOG_SERIES_INDEX", "rows=" + r16.size() + " episode_entries=" + r3.catalog.materializedSize());
        return new MainActivity.CatalogPlan(r16, r8, r10);
    L25:
        r9 = (String) r8.get(0);
        goto L26
    }

    static void sortSelected(List<Entry> r1) {
        Collections.sort(r1, new SectionNavigation$$ExternalSyntheticLambda1());
    }
}
