package es.antonio.m3u;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
public final class Catalog implements Serializable {
    private static final long serialVersionUID = 1;
    transient String changedId;
    transient String changedName;
    transient Source changedSource;
    transient DemandCatalog demand;
    transient String mutation;
    transient Object sectionState;
    public final List<Source> sources;
    transient int startupCacheVersion;
    transient long startupConstructMs;
    transient long startupDictionaryMs;
    transient Object startupIndex;
    transient long startupRecordsMs;
    transient long startupResolveMs;

    static final class AllEntries extends AbstractList<Entry> {
        final Catalog owner;
        final List<Entry> values;

        AllEntries(Catalog r1, List<Entry> r2) {
            this.owner = r1;
            this.values = r2;
        }

        @Override // java.util.AbstractList, java.util.List
        public Entry get(int r2) {
            return this.values.get(r2);
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<Entry> iterator() {
            return this.values.iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.values.size();
        }
    }

    public static final class Source implements Serializable {
        private static final long serialVersionUID = 1;
        public List<Entry> entries;

        /* JADX INFO: renamed from: id */
        public String f177id;
        public String kind;
        public String name;
        public String origin;
        public String originalFilename;
        public String provider;

        public Source(String r2, String r3, String r4, String r5, List<Entry> r6) {
            this.entries = new ArrayList();
            this.f177id = r2;
            this.name = r3;
            this.origin = r4;
            this.kind = r5;
            this.entries = new ArrayList(r6);
        }
    }

    public Catalog() {
        this.sources = new ArrayList();
    }

    Catalog(DemandCatalog r3) {
        this.demand = r3;
        this.sources = r3.sources();
        this.startupIndex = new CatalogCache.Prepared(r3.providers, r3.counts);
    }

    static List<Entry> join(List<List<Entry>> r5) {
        final ArrayList r0 = new ArrayList(r5);
        int r6 = r0.size();
        final int[] r1 = new int[r6];
        int r2 = 0;
        final int r3 = 0;
    L3:
        if (r2 >= r6) goto L5;
        r3 = r3 + ((List) r0.get(r2)).size();
        r1[r2] = r3;
        r2 = r2 + 1;
        goto L3
    L5:
        return new C09701(r3, r1, r0);
    }

    public static Catalog migrate(List<Entry> r9) {
        Catalog r0 = new Catalog();
        if (r9.isEmpty() == true) goto L5;
        r0.sources.add(new Source("legacy", "Catálogo anterior", "", "Mixta", r9));
    L5:
        return r0;
    }

    private Object writeReplace() {
        if (this.demand != null) goto L6;
        return this;
    L6:
        return this.demand.resolve();
    }

    public List<Entry> entries() {
        if (this.demand != null) goto L5;
        ArrayList r0 = new ArrayList();
        Iterator<Source> r1 = this.sources.iterator();
    L8:
        if (r1.hasNext() == false) goto L10;
        r0.add(r1.next().entries);
        goto L8
    L10:
        return new AllEntries(this, join(r0));
    L5:
        return new AllEntries(this, this.demand.allEntries());
    }

    List<Entry> entriesFor(String r7, String r8, Map<Entry, String> r9) {
        if (this.demand == null) goto L7;
        return this.demand.select(r7, r8, null, null);
    L7:
        if (SectionStore.state(this) != null) goto L9;
        ArrayList r0 = new ArrayList();
        Iterator<Source> r1 = this.sources.iterator();
    L12:
        if (r1.hasNext() == false) goto L14;
        Source r2 = r1.next();
        if ((r2.entries instanceof CatalogCache.LazyEntries) == false) goto L20;
        if (r9 != ((CatalogCache.LazyEntries) r2.entries).providers) goto L20;
        r0.add(((CatalogCache.LazyEntries) r2.entries).select(r7, r8));
    L20:
        ArrayList r3 = new ArrayList();
        Iterator<Entry> r4 = r2.entries.iterator();
    L22:
        if (r4.hasNext() == false) goto L23;
        Entry r5 = r4.next();
        if (r7.equals(r5.kind) == false) goto L22;
        if (r8 == null) goto L29;
        if (r8.equals(r9.get(r5)) == false) goto L22;
    L29:
        r3.add(r5);
        goto L22
    L23:
        r0.add(r3);
        goto L12
    L14:
        return join(r0);
    L9:
        return SectionStore.select(this, r7, r8, null, null);
    }

    public Source findOrigin(String r4, String r5) {
        Iterator<Source> r0 = this.sources.iterator();
    L4:
        if (r0.hasNext() == false) goto L5;
        Source r1 = r0.next();
        if (r1.origin.equals(r4) == false) goto L4;
        if (r1.kind.equals(r5) == false) goto L4;
        return r1;
    L5:
        return null;
    }

    int materializedSize() {
        if (this.demand != null) goto L5;
        Iterator<Source> r0 = this.sources.iterator();
        int r1 = 0;
    L8:
        if (r0.hasNext() == false) goto L9;
        Source r2 = r0.next();
        if ((r2.entries instanceof DemandCatalog.Rows) == false) goto L14;
        int r3 = ((DemandCatalog.Rows) r2.entries).materialized();
    L20:
        r1 = r1 + r3;
        goto L8
    L14:
        if ((r2.entries instanceof SectionStore.DiskEntries) == false) goto L16;
        r3 = ((SectionStore.DiskEntries) r2.entries).materialized();
        goto L20
    L16:
        boolean r4 = r2.entries instanceof CatalogCache.LazyEntries;
        List<Entry> r5 = r2.entries;
        if (r4 == false) goto L19;
        r3 = ((CatalogCache.LazyEntries) r5).materialized;
        goto L20
    L19:
        r3 = r5.size();
        goto L20
    L9:
        return r1;
    L5:
        return this.demand.materialized();
    }

    public Catalog remove(String r5) {
        Catalog r0 = new Catalog();
        Iterator<Source> r1 = this.sources.iterator();
    L4:
        if (r1.hasNext() == false) goto L5;
        Source r2 = r1.next();
        if (r2.f177id.equals(r5) == true) goto L4;
        r0.sources.add(r2);
        goto L4
    L5:
        r0.mutation = "remove";
        r0.changedId = r5;
        return r0;
    }

    public Catalog rename(String r11, String r12) {
        Catalog r0 = new Catalog();
        Iterator<Source> r1 = this.sources.iterator();
    L4:
        if (r1.hasNext() == false) goto L5;
        Source r2 = r1.next();
        if (r2.f177id.equals(r11) == true) goto L9;
        r0.sources.add(r2);
        goto L4
    L9:
        Source r3 = new Source(r2.f177id, r12, r2.origin, r2.kind, Collections.emptyList());
        r3.entries = r2.entries;
        r3.originalFilename = r2.originalFilename;
        r3.provider = r2.provider;
        r0.sources.add(r3);
        goto L4
    L5:
        r0.mutation = "rename";
        r0.changedId = r11;
        r0.changedName = r12;
        return r0;
    }

    public Catalog replace(Source r14) {
        Catalog r0 = new Catalog();
        Iterator<Source> r1 = this.sources.iterator();
        HashSet r2 = null;
        boolean r3 = false;
    L4:
        if (r1.hasNext() == false) goto L5;
        Source r4 = r1.next();
        if (r4.f177id.equals(r14.f177id) == true) goto L11;
        if (r4.f177id.equals("legacy") == true) goto L14;
        r0.sources.add(r4);
        goto L4
    L14:
        if (r2 != null) goto L20;
        HashSet r5 = new HashSet();
        Iterator<Entry> r6 = r14.entries.iterator();
    L17:
        if (r6.hasNext() == false) goto L21;
        r5.add(r6.next().identity());
    L21:
        ArrayList r11 = new ArrayList();
        Iterator<Entry> r7 = r4.entries.iterator();
    L23:
        if (r7.hasNext() == false) goto L25;
        Entry r8 = r7.next();
        if (r5.contains(r8.identity()) == true) goto L23;
        r11.add(r8);
        goto L23
    L25:
        if (r11.isEmpty() == true) goto L27;
        r0.sources.add(new Source(r4.f177id, r4.name, r4.origin, r4.kind, r11));
    L27:
        r2 = r5;
        goto L4
    L20:
        r5 = r2;
        goto L21
    L11:
        r0.sources.add(r14);
        r3 = true;
        goto L4
    L5:
        if (r3 == true) goto L7;
        r0.sources.add(r14);
    L7:
        r0.mutation = "replace";
        r0.changedSource = r14;
        return r0;
    }

    public int size() {
        if (this.demand != null) goto L5;
        Iterator<Source> r0 = this.sources.iterator();
        int r1 = 0;
    L8:
        if (r0.hasNext() == false) goto L9;
        r1 = r1 + r0.next().entries.size();
        goto L8
    L9:
        return r1;
    L5:
        return this.demand.total;
    }
}
