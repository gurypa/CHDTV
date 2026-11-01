package es.antonio.m3u;

import android.content.Context;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/* JADX INFO: loaded from: classes7.dex */
final class SectionStore {
    private static final List<WeakReference<State>> LIVE = null;
    static final int VERSION = 170;

    static final class AcceptedHost implements Predicate<String> {
        AcceptedHost() {
        }

        @Override // java.util.function.Predicate
        public /* bridge */ /* synthetic */ boolean test(String r1) {
            return test2(r1);
        }

        /* JADX INFO: renamed from: test, reason: avoid collision after fix types in other method */
        public boolean test2(String r1) {
            return MovistarStreams.allowedHost(r1);
        }
    }

    static final class DiskEntries extends AbstractList<Entry> implements Serializable {
        private static final long serialVersionUID = 1;
        final transient Map<Part, List<Entry>> loaded;
        final transient SourceMeta source;
        final transient State state;

        DiskEntries(State r2, SourceMeta r3) {
            this.loaded = new IdentityHashMap();
            this.state = r2;
            this.source = r3;
        }

        private Object writeReplace() {
            return new ArrayList(this);
        }

        @Override // java.util.AbstractList, java.util.List
        public Entry get(int r4) {
            if (r4 < 0) goto L16;
            if (r4 >= size()) goto L16;
            Iterator<Part> r0 = this.source.parts.iterator();
        L7:
            if (r0.hasNext() == false) goto L14;
            Part r1 = r0.next();
            if (r4 < r1.count) goto L11;
            r4 = r4 - r1.count;
            goto L7
        L11:
            return part(r1).get(r4);
        L14:
            throw new IndexOutOfBoundsException();
        L16:
            throw new IndexOutOfBoundsException();
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        synchronized int materialized() {
            monitor-enter(this);
            Iterator<List<Entry>> r0 = this.loaded.values().iterator();     // Catch: Throwable -> L10
            int r1 = 0;
        L5:
            if (r0.hasNext() == false) goto L6;
            r1 = r1 + r0.next().size();
            goto L5
        L6:
            monitor-exit(this);
            return r1;
        L10:
            th = move-exception;
            throw th;
        }

        synchronized List<Entry> part(Part r8) {
            monitor-enter(this);
            List<Entry> r0 = this.loaded.get(r8);     // Catch: Throwable -> L21
            if (r0 != null) goto L19;
            long r1 = System.nanoTime();     // Catch: Throwable -> L21
            List<Entry> r2 = this.state.read(r8.offset, r8.length);     // Catch: IOException -> L16 Throwable -> L21
            if (r2.size() != r8.count) goto L15;
            Iterator<Entry> r3 = r2.iterator();     // Catch: Throwable -> L21
        L10:
            if (r3.hasNext() == false) goto L12;
            Entry r4 = r3.next();     // Catch: Throwable -> L21
            this.state.providers.values.put(r4, TvProviderFormats220.label(r8.provider, r4));     // Catch: Throwable -> L21
            goto L10
        L12:
            this.loaded.put(r8, r2);     // Catch: Throwable -> L21
            BlackBox.event("CATALOG_SECTION_LOAD", "kind=" + r8.kind + " entries=" + r2.size() + " duration_ms=" + ((System.nanoTime() - r1) / 1000000));     // Catch: Throwable -> L21
            r0 = r2;
            goto L19
        L15:
            throw new IOException("Section count mismatch");     // Catch: IOException -> L16 Throwable -> L21
        L16:
            e = move-exception;
            SectionStore.index(this.state.data.getParentFile()).delete();     // Catch: Throwable -> L21
            throw new IllegalStateException("No se pudo leer la sección. Vuelve a abrir la app para reconstruir el índice.", e);     // Catch: Throwable -> L21
        L19:
            monitor-exit(this);
            return r0;
        L21:
            th = move-exception;
            throw th;
        }

        List<Entry> select(String r9, String r10, String r11, Integer r12) {
            if ("Canales".equals(r9) == false) goto L24;
            ArrayList r0 = new ArrayList();
            Iterator<Part> r2 = this.source.parts.iterator();
        L6:
            if (r2.hasNext() == false) goto L23;
            Part r3 = r2.next();
            if (r9.equals(r3.kind) == false) goto L6;
            if (r11 == null) goto L12;
            if (r11.equals(r3.series) == false) goto L6;
        L12:
            Iterator<Entry> r4 = part(r3).iterator();
        L14:
            if (r4.hasNext() == false) goto L6;
            Entry r5 = r4.next();
            if (r10 == null) goto L19;
            if (r10.equals(TvProviderFormats220.label(r3.provider, r5)) == false) goto L14;
        L19:
            if (r12 == null) goto L22;
            if (Math.max(0, r5.season) != r12.intValue()) goto L14;
        L22:
            r0.add(r5);
            goto L14
        L23:
            return r0;
        L24:
            ArrayList r1 = new ArrayList();
            Iterator<Part> r6 = this.source.parts.iterator();
        L26:
            if (r6.hasNext() == false) goto L36;
            final Part r7 = r6.next();
            if (r9.equals(r7.kind) == false) goto L26;
            if (r10 == null) goto L32;
            if (r10.equals(r7.provider) == false) goto L26;
        L32:
            if (r11 == null) goto L35;
            if (r11.equals(r7.series) == false) goto L26;
        L35:
            r1.add(new C10341(this, r7));
            goto L26
        L36:
            List<Entry> r13 = Catalog.join(r1);
            if (r12 != null) goto L39;
            return r13;
        L39:
            ArrayList r14 = new ArrayList();
            Iterator<Entry> r15 = r13.iterator();
        L41:
            if (r15.hasNext() == false) goto L45;
            Entry r16 = r15.next();
            if (Math.max(0, r16.season) != r12.intValue()) goto L41;
            r14.add(r16);
            goto L41
        L45:
            return r14;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.source.count;
        }
    }

    static final class Meta implements Serializable {
        private static final long serialVersionUID = 1;
        final Map<String, Integer> counts;
        String data;
        long modified;
        long size;
        final List<SourceMeta> sources;
        int version;

        Meta() {
            this.version = SectionStore.VERSION;
            this.sources = new ArrayList();
            this.counts = new HashMap();
        }
    }

    static final class Part implements Serializable {
        private static final long serialVersionUID = 1;
        int count;
        int episodeLength;
        long episodeOffset;
        transient Set<String> episodes;
        final Set<String> groups;
        String kind;
        int length;
        long offset;
        String provider;
        long representative;
        int representativeLength;
        final Set<Integer> seasons;
        String series;

        Part() {
            this.groups = new HashSet();
            this.episodes = new HashSet();
            this.seasons = new HashSet();
        }
    }

    static final class ProviderMap extends AbstractMap<Entry, String> {
        final Map<Entry, String> values;

        ProviderMap() {
            this.values = Collections.synchronizedMap(new IdentityHashMap());
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<Entry, String>> entrySet() {
            Map<Entry, String> r0 = this.values;
            monitor-enter(r0);
            Set<Map.Entry<Entry, String>> r1 = new IdentityHashMap(this.values).entrySet();     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return r1;
        L7:
            th = move-exception;
            throw th;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public /* bridge */ /* synthetic */ Object get(Object r1) {
            return get(r1);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public String get(Object r3) {
            String r0 = this.values.get(r3);
            if ((r3 instanceof Entry) == true) goto L5;
            return r0;
        L5:
            return TvProviderFormats220.label(r0, (Entry) r3);
        }
    }

    static final class SourceMeta implements Serializable {
        private static final long serialVersionUID = 1;
        int count;
        String filename;
        final Set<String> hosts;

        /* JADX INFO: renamed from: id */
        String f190id;
        String kind;
        String name;
        String origin;
        final List<Part> parts;
        String provider;

        SourceMeta() {
            this.parts = new ArrayList();
            this.hosts = new HashSet();
        }
    }

    static final class State {
        final Catalog catalog;
        final File data;
        final Meta meta;
        final ProviderMap providers;

        State(Meta r2, File r3, Catalog r4) {
            this.providers = new ProviderMap();
            this.meta = r2;
            this.data = r3;
            this.catalog = r4;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.Throwable] */
        /* JADX WARN: Type inference failed for: r1v2, types: [java.lang.Throwable] */
        /* JADX WARN: Type inference failed for: r1v5, types: [java.lang.String] */
        /* JADX WARN: Type inference failed for: r1v6 */
        /* JADX WARN: Type inference failed for: r6v6, types: [java.lang.Throwable] */
        Set<String> episodeKeys(Part r6) throws IOException {
            byte[] r0 = new byte[r6.episodeLength];
            ?? r1 = 0;
            r1 = 0;
            RandomAccessFile r2 = new RandomAccessFile(this.data, "r");     // Catch: Throwable -> L38
            r2.seek(r6.episodeOffset);     // Catch: Throwable -> L34
            r2.readFully(r0);     // Catch: Throwable -> L34
            r2.close();     // Catch: Throwable -> L38
            ObjectInputStream r7 = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(r0)));     // Catch: Throwable -> L25
            Object r3 = r7.readObject();     // Catch: Throwable -> L21
            if ((r3 instanceof Set) == false) goto L54;
            Set<String> r4 = (Set) r3;     // Catch: Throwable -> L21
            Iterator<String> r5 = r4.iterator();     // Catch: Throwable -> L21
        L11:
            if (r5.hasNext() == false) goto L12;
            if ((r5.next() instanceof String) == true) goto L11;
            r1 = "Invalid episode key";
            throw new IOException("Invalid episode key");     // Catch: Throwable -> L21
        L12:
            r7.close();     // Catch: Throwable -> L25
            return r4;
        L54:
            throw new IOException("Invalid episode summary");     // Catch: Throwable -> L21
        L21:
            th = move-exception;
            r7.close();     // Catch: Throwable -> L25
            throw th;     // Catch: Throwable -> L25
        L25:
            th = move-exception;
            if (r1 == 0) goto L55;
            if (r1 == th) goto L30;
            r1.addSuppressed(th);     // Catch: ClassNotFoundException -> L31
        L31:
            e = move-exception;
            throw new IOException(e);
        L30:
            throw r1;     // Catch: ClassNotFoundException -> L31
        L55:
            throw th;     // Catch: ClassNotFoundException -> L31
        L34:
            th = move-exception;
            r2.close();     // Catch: Throwable -> L38
            throw th;     // Catch: Throwable -> L38
        L38:
            th = move-exception;
            if (0 == 0) goto L56;
            if (null == th) goto L43;
            r1.addSuppressed(th);
        L43:
            throw null;
        L56:
            throw th;
        }

        List<Entry> read(long r8, int r10) throws IOException {
            byte[] r0 = new byte[r10];
            Throwable r1 = null;
            RandomAccessFile r2 = new RandomAccessFile(this.data, "r");     // Catch: Throwable -> L45
            if (r8 < 0) goto L40;
            if (r10 < 0) goto L40;
        L41:
            th = move-exception;
            r2.close();     // Catch: Throwable -> L45
            throw th;     // Catch: Throwable -> L45
        L8:
            if (r8 > (r2.length() - ((long) r10))) goto L40;
            r2.seek(r8);     // Catch: Throwable -> L41
            r2.readFully(r0);     // Catch: Throwable -> L41
            r2.close();     // Catch: Throwable -> L45
            ObjectInputStream r9 = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(r0)));     // Catch: Throwable -> L30
            Object r11 = r9.readObject();     // Catch: Throwable -> L26
            if ((r11 instanceof List) == false) goto L60;
            List<Entry> r12 = (List) r11;     // Catch: Throwable -> L26
            Iterator<Entry> r13 = r12.iterator();     // Catch: Throwable -> L26
        L16:
            if (r13.hasNext() == false) goto L17;
            if ((r13.next() instanceof Entry) == true) goto L16;
            throw new IOException("Invalid section entry");     // Catch: Throwable -> L26
        L17:
            r9.close();     // Catch: Throwable -> L30
            return r12;
        L60:
            throw new IOException("Invalid section content");     // Catch: Throwable -> L26
        L26:
            th = move-exception;
            r9.close();     // Catch: Throwable -> L30
            throw th;     // Catch: Throwable -> L30
        L30:
            th = move-exception;
            if (0 == 0) goto L61;
            if (null == th) goto L35;
            r1.addSuppressed(th);     // Catch: ClassNotFoundException -> L36
        L36:
            e = move-exception;
            throw new IOException(e);
        L35:
            throw null;     // Catch: ClassNotFoundException -> L36
        L61:
            throw th;     // Catch: ClassNotFoundException -> L36
        L40:
            throw new IOException("Invalid section block");     // Catch: Throwable -> L41
        L45:
            th = move-exception;
            if (0 == 0) goto L62;
            if (null == th) goto L50;
            r1.addSuppressed(th);
        L50:
            throw null;
        L62:
            throw th;
        }
    }

    static {
        LIVE = new ArrayList();
    }

    private SectionStore() {
    }

    static void build(File r17, Catalog r18, long r19, long r21, CatalogIndex r23) throws IOException {
        if (state(r18) == null) goto L5;
        return;
    L5:
        Meta r2 = new Meta();
        r2.modified = r19;
        r2.size = r21;
        r2.counts.putAll(r23.counts());
        r2.data = "catalog-sections-" + UUID.randomUUID() + ".bin";
        File r3 = new File(r17, r2.data);
        File r4 = new File(r17, "catalog-sections-v170.tmp");
        int r6 = 0;
        RandomAccessFile r7 = new RandomAccessFile(r3, "rw");     // Catch: Throwable -> L87
        Iterator<Catalog.Source> r9 = r18.sources.iterator();     // Catch: Throwable -> L81
    L9:
        int r10 = 1;
        if (r9.hasNext() == false) goto L11;
        Catalog.Source r0 = r9.next();     // Catch: Throwable -> L81
        SourceMeta r11 = new SourceMeta();     // Catch: Throwable -> L81
        r11.f190id = r0.f177id;     // Catch: Throwable -> L81
        r11.name = r0.name;     // Catch: Throwable -> L81
        r11.origin = r0.origin;     // Catch: Throwable -> L81
        r11.kind = r0.kind;     // Catch: Throwable -> L81
        r11.filename = r0.originalFilename;     // Catch: Throwable -> L81
        r11.provider = r0.provider;     // Catch: Throwable -> L81
        r11.count = r0.entries.size();     // Catch: Throwable -> L81
        r2.sources.add(r11);     // Catch: Throwable -> L81
        LinkedHashMap r12 = new LinkedHashMap();     // Catch: Throwable -> L81
        Iterator<Entry> r13 = r0.entries.iterator();     // Catch: Throwable -> L81
    L54:
        if (r13.hasNext() == false) goto L55;
        SourceMeta r14 = r11;
        Entry r1 = r13.next();     // Catch: Throwable -> L81
        String r8 = r23.providers.get(r1);     // Catch: Throwable -> L81
        if ("Series".equals(r1.kind) == false) goto L73;
        String r15 = CatalogCounts.series(r1);     // Catch: Throwable -> L81
    L74:
        ((List) r12.computeIfAbsent(Arrays.asList(new String[]{r1.kind, r8, r15}), new SectionStore$$ExternalSyntheticLambda0())).add(r1);     // Catch: Throwable -> L81
        String r5 = new URI(Entry.details(r1).url).getHost();     // Catch: Exception -> L79 Throwable -> L81
        if (r5 == null) goto L80;
        r14.hosts.add(r5.toLowerCase(Locale.ROOT));     // Catch: Exception -> L79 Throwable -> L81
    L80:
        r11 = r14;
        r6 = 0;
        r10 = 1;
        goto L54
    L73:
        r15 = null;
        goto L74
    L55:
        Iterator r16 = r12.entrySet().iterator();     // Catch: Throwable -> L81
    L57:
        if (r16.hasNext() == false) goto L9;
        Map.Entry r110 = (Map.Entry) r16.next();     // Catch: Throwable -> L81
        List r111 = (List) r110.getValue();     // Catch: Throwable -> L81
        Part r112 = new Part();     // Catch: Throwable -> L81
        r112.kind = (String) ((List) r110.getKey()).get(r6);     // Catch: Throwable -> L81
        r112.provider = (String) ((List) r110.getKey()).get(r10);     // Catch: Throwable -> L81
        r112.series = (String) ((List) r110.getKey()).get(2);     // Catch: Throwable -> L81
        r112.count = r111.size();     // Catch: Throwable -> L81
        Iterator r20 = r111.iterator();     // Catch: Throwable -> L81
    L61:
        if (r20.hasNext() == false) goto L62;
        SourceMeta r113 = r11;
        Entry r114 = (Entry) r20.next();     // Catch: Throwable -> L81
        r112.groups.add(r114.group);     // Catch: Throwable -> L81
        if ("Series".equals(r112.kind) == false) goto L69;
        r112.episodes.add(CatalogCounts.episodeKey(r114));     // Catch: Throwable -> L81
        r112.seasons.add(Integer.valueOf(r114.season));     // Catch: Throwable -> L81
    L69:
        r11 = r113;
        r6 = 0;
        goto L61
    L62:
        byte[] r22 = encode(r111);     // Catch: Throwable -> L81
        SourceMeta r24 = r11;
        r112.offset = r7.getFilePointer();     // Catch: Throwable -> L81
        r112.length = r22.length;     // Catch: Throwable -> L81
        r7.write(r22);     // Catch: Throwable -> L81
        if ("Series".equals(r112.kind) == false) goto L65;
        byte[] r25 = encode(Collections.singletonList((Entry) r111.get(r6)));     // Catch: Throwable -> L81
        r112.representative = r7.getFilePointer();     // Catch: Throwable -> L81
        r112.representativeLength = r25.length;     // Catch: Throwable -> L81
        r7.write(r25);     // Catch: Throwable -> L81
        byte[] r26 = encodeObject(r112.episodes);     // Catch: Throwable -> L81
        r112.episodeOffset = r7.getFilePointer();     // Catch: Throwable -> L81
        r112.episodeLength = r26.length;     // Catch: Throwable -> L81
        r7.write(r26);     // Catch: Throwable -> L81
    L65:
        r24.parts.add(r112);     // Catch: Throwable -> L81
        r11 = r24;
        r10 = 1;
        goto L57
    L11:
        r7.getFD().sync();     // Catch: Throwable -> L81
        r7.close();     // Catch: Throwable -> L87
        FileOutputStream r27 = new FileOutputStream(r4);     // Catch: Throwable -> L42
        ObjectOutputStream r28 = new ObjectOutputStream(new BufferedOutputStream(r27));     // Catch: Throwable -> L32
        r28.writeObject(r2);     // Catch: Throwable -> L26
        r28.flush();     // Catch: Throwable -> L26
        r27.getFD().sync();     // Catch: Throwable -> L26
        r28.close();     // Catch: Throwable -> L32
        r27.close();     // Catch: Throwable -> L42
        if (r4.renameTo(index(r17)) == false) goto L25;
        BlackBox.event("CATALOG_SECTIONS_SAVED", "entries=" + r18.size() + " index_bytes=" + index(r17).length() + " data_bytes=" + r3.length());     // Catch: Throwable -> L22
        return;
    L22:
        th = th;
        r6 = 1;
    L96:
        if (r6 != 0) goto L98;
        r3.delete();
        r4.delete();
    L98:
        throw th;
    L25:
        throw new IOException("Cannot commit section index");     // Catch: Throwable -> L49
    L26:
        th = move-exception;
        Throwable r29 = th;
        r28.close();     // Catch: Throwable -> L30
        throw r29;     // Catch: Throwable -> L30
    L30:
        th = th;
    L34:
        if (r29 == null) goto L39;
        if (r29 != th) goto L116;
    L40:
        r27.close();     // Catch: Throwable -> L37
        throw r29;     // Catch: Throwable -> L37
    L37:
        th = th;
    L44:
        if (r29 == null) goto L48;
        if (r29 == th) goto L133;
        r29.addSuppressed(th);     // Catch: Throwable -> L49
        throw r29;     // Catch: Throwable -> L49
    L133:
        throw r29;     // Catch: Throwable -> L49
    L48:
        throw th;     // Catch: Throwable -> L49
    L116:
        r29.addSuppressed(th);     // Catch: Throwable -> L37
        goto L40
    L39:
        r29 = th;
    L32:
        th = th;
        r29 = null;
    L42:
        th = th;
        r29 = null;
    L49:
        th = th;
    L81:
        th = move-exception;
        Throwable r30 = th;
        r7.close();     // Catch: Throwable -> L85
        throw r30;     // Catch: Throwable -> L85
    L85:
        th = th;
    L89:
        if (r30 == null) goto L93;
        if (r30 == th) goto L135;
        r30.addSuppressed(th);     // Catch: Throwable -> L94
        throw r30;     // Catch: Throwable -> L94
    L94:
        th = th;
        r6 = 0;
        goto L96
    L135:
        throw r30;     // Catch: Throwable -> L94
    L93:
        throw th;     // Catch: Throwable -> L94
    L87:
        th = th;
        r30 = null;
        goto L89
    }

    static boolean eligible(Catalog.Source r4, Predicate<String> r5) {
        if ((r4.entries instanceof DiskEntries) == true) goto L5;
        return false;
    L5:
        Iterator<String> r6 = ((DiskEntries) r4.entries).source.hosts.iterator();
    L7:
        if (r6.hasNext() == false) goto L8;
        if (r5.test("https://" + r6.next() + "/") == false) goto L7;
        return true;
    L8:
        return false;
    }

    private static byte[] encode(List<Entry> r1) throws IOException {
        return encodeObject(new ArrayList(r1));
    }

    private static byte[] encodeObject(Object r4) throws IOException {
        ByteArrayOutputStream r0 = new ByteArrayOutputStream();
        Throwable r1 = null;
        ObjectOutputStream r2 = new ObjectOutputStream(new GZIPOutputStream(r0));     // Catch: Throwable -> L11
        r2.writeObject(r4);     // Catch: Throwable -> L8
        r2.close();     // Catch: Throwable -> L11
        return r0.toByteArray();
    L8:
        th = move-exception;
        r2.close();     // Catch: Throwable -> L11
        throw th;     // Catch: Throwable -> L11
    L11:
        th = move-exception;
        if (0 == 0) goto L20;
        if (null == th) goto L16;
        r1.addSuppressed(th);
    L16:
        throw null;
    L20:
        throw th;
    }

    static List<WatchHistory.Item> history(Context r11, Catalog r12, String r13, String r14) {
        List<WatchHistory.Item> r0 = WatchHistory.all(r11);
        ArrayList r1 = new ArrayList();
        HashSet r2 = new HashSet();
        Iterator<WatchHistory.Item> r3 = r0.iterator();
    L4:
        if (r3.hasNext() == false) goto L5;
        WatchHistory.Item r4 = r3.next();
        if (r13.equals(r4.entry.kind) == false) goto L4;
        r2.add(CatalogCounts.series(r4.entry));
        goto L4
    L5:
        Iterator<Map.Entry<String, ?>> r5 = WatchHistory.legacy(r11).getAll().entrySet().iterator();
    L7:
        if (r5.hasNext() == false) goto L9;
        Map.Entry<String, ?> r6 = r5.next();
        if (r6.getKey().startsWith(String.valueOf(r13) + "\n") == false) goto L7;
        if ((r6.getValue() instanceof Long) == false) goto L7;
        if (((Long) r6.getValue()).longValue() <= 0) goto L7;
        BlackBox.event("CATALOG_HISTORY_LEGACY_SCAN", "kind=" + r13);
        return WatchHistory.inCatalog(r11, select(r12, r13, r14, null, null), r13);
    L9:
        if (r12.demand == null) goto L15;
        long r7 = System.nanoTime();
        List<Entry> r8 = r12.demand.historyCandidates(r0, r13, r14);
        if (r8 == null) goto L15;
        List<WatchHistory.Item> r15 = WatchHistory.inCatalog(r11, r8, r13);
        BlackBox.event("CATALOG_HISTORY_LOOKUP", "kind=" + r13 + " saved=" + r0.size() + " matched=" + r8.size() + " duration_ms=" + ((System.nanoTime() - r7) / 1000000));
        return r15;
    L15:
        if ("Series".equals(r13) == false) goto L21;
        Iterator r9 = r2.iterator();
    L18:
        if (r9.hasNext() == false) goto L23;
        r1.add(select(r12, r13, r14, (String) r9.next(), null));
    L23:
        return WatchHistory.inCatalog(r11, Catalog.join(r1), r13);
    L21:
        r1.add(select(r12, r13, r14, null, null));
        goto L23
    }

    static File index(File r2) {
        return new File(r2, "catalog-sections-v170.idx");
    }

    static /* synthetic */ List lambda$0(List r0) {
        return new ArrayList();
    }

    static Catalog open(File r9, long r10, long r12) throws IOException {
        Throwable r0 = null;
        ObjectInputStream r1 = new ObjectInputStream(new BufferedInputStream(new FileInputStream(index(r9))));     // Catch: Throwable -> L35
        Meta r2 = (Meta) r1.readObject();     // Catch: Throwable -> L26 Throwable -> L28 ClassNotFoundException -> L30
        r1.close();     // Catch: Throwable -> L35
        if (r2.version != VERSION) goto L25;
        if (r2.modified != r10) goto L25;
        if (r2.size != r12) goto L25;
        if (r2.data.matches("catalog-sections-[a-f0-9-]+\\.bin") == false) goto L25;
        File r11 = new File(r9, r2.data);
        if (r11.isFile() == false) goto L23;
        Catalog r13 = new Catalog();
        State r14 = new State(r2, r11, r13);
        Iterator<SourceMeta> r15 = r2.sources.iterator();
    L18:
        if (r15.hasNext() == false) goto L19;
        SourceMeta r16 = r15.next();
        Catalog.Source r3 = new Catalog.Source(r16.f190id, r16.name, r16.origin, r16.kind, Collections.emptyList());
        r3.originalFilename = r16.filename;
        r3.provider = r16.provider;
        r3.entries = new DiskEntries(r14, r16);
        r13.sources.add(r3);
        goto L18
    L19:
        r13.startupIndex = new CatalogCache.Prepared(r14.providers, r2.counts);
        r13.startupCacheVersion = VERSION;
        r13.sectionState = r14;
        register(r14);
        BlackBox.event("CATALOG_SECTION_INDEX", "entries=" + r13.size() + " created_entries=0 bytes=" + index(r9).length());
        return r13;
    L23:
        throw new IOException("Section data missing");
    L25:
        throw new IOException("Section index outdated");
    L26:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L35
        throw th;     // Catch: Throwable -> L35
    L28:
        e = move-exception;
        throw new IOException(e);     // Catch: Throwable -> L26
    L35:
        th = move-exception;
        if (0 == 0) goto L45;
        if (null == th) goto L40;
        r0.addSuppressed(th);
    L40:
        throw null;
    L45:
        throw th;
    }

    private static synchronized void register(State r7) {
        monitor-enter(SectionStore.class);
        LIVE.add(new WeakReference(r7));     // Catch: Throwable -> L25
        HashSet r1 = new HashSet();     // Catch: Throwable -> L25
        Iterator<WeakReference<State>> r2 = LIVE.iterator();     // Catch: Throwable -> L25
    L6:
        if (r2.hasNext() == false) goto L7;
        State r3 = r2.next().get();     // Catch: Throwable -> L25
        if (r3 == null) goto L22;
        r1.add(r3.data.getAbsolutePath());     // Catch: Throwable -> L25
        goto L6
    L22:
        r2.remove();     // Catch: Throwable -> L25
        goto L6
    L7:
        File[] r8 = r7.data.getParentFile().listFiles();     // Catch: Throwable -> L25
        if (r8 == null) goto L18;
        int r4 = r8.length;     // Catch: Throwable -> L25
        int r5 = 0;
    L10:
        if (r5 >= r4) goto L18;
        File r6 = r8[r5];     // Catch: Throwable -> L25
        if (r6.getName().matches("catalog-sections-[a-f0-9-]+\\.bin") == false) goto L17;
        if (r1.contains(r6.getAbsolutePath()) == true) goto L17;
        r6.delete();     // Catch: Throwable -> L25
    L17:
        r5 = r5 + 1;
    L18:
        monitor-exit(SectionStore.class);
        return;
    L25:
        th = move-exception;
        throw th;
    }

    static List<Entry> related(List<Entry> r7, Entry r8) {
        if ((r7 instanceof Catalog.AllEntries) == true) goto L5;
        return r7;
    L5:
        Catalog r0 = ((Catalog.AllEntries) r7).owner;
        if (r0.demand != null) goto L8;
        State r1 = state(r0);
        if (r1 != null) goto L12;
        return r7;
    L12:
        if (r8 != null) goto L14;
        String r9 = "Películas";
    L16:
        if ("Series".equals(r9) == false) goto L18;
        ArrayList r10 = new ArrayList();
        Iterator<SourceMeta> r2 = r1.meta.sources.iterator();
    L21:
        if (r2.hasNext() == false) goto L22;
        Iterator<Part> r3 = r2.next().parts.iterator();
    L25:
        if (r3.hasNext() == false) goto L21;
        Part r4 = r3.next();
        if ("Series".equals(r4.kind) == false) goto L25;
        Entry r5 = r1.read(r4.representative, r4.representativeLength).get(0);     // Catch: IOException -> L31
        r1.providers.values.put(r5, r4.provider);     // Catch: IOException -> L31
        r10.add(r5);     // Catch: IOException -> L31
    L31:
        e = move-exception;
        index(r1.data.getParentFile()).delete();
        throw new IllegalStateException(e);
    L22:
        return r10;
    L18:
        return select(r0, r9, null, null, null);
    L14:
        r9 = r8.kind;
        goto L16
    L8:
        return DemandNavigation.related(r0.demand, r8);
    }

    static List<Entry> select(Catalog r3, String r4, String r5, String r6, Integer r7) {
        if (r3.demand == null) goto L7;
        return r3.demand.select(r4, r5, r6, r7);
    L7:
        if (r3.startupCacheVersion != 171) goto L16;
        ArrayList r0 = new ArrayList();
        Iterator<Catalog.Source> r1 = r3.sources.iterator();
    L10:
        if (r1.hasNext() == false) goto L12;
        Catalog.Source r8 = r1.next();
        if ((r8.entries instanceof DemandCatalog.Rows) == false) goto L10;
        r0.add(((DemandCatalog.Rows) r8.entries).select(r4, r5, r6, r7));
        goto L10
    L12:
        return Catalog.join(r0);
    L16:
        ArrayList r2 = new ArrayList();
        Iterator<Catalog.Source> r9 = r3.sources.iterator();
    L18:
        if (r9.hasNext() == false) goto L20;
        Catalog.Source r10 = r9.next();
        if ((r10.entries instanceof DiskEntries) == false) goto L18;
        r2.add(((DiskEntries) r10.entries).select(r4, r5, r6, r7));
        goto L18
    L20:
        return Catalog.join(r2);
    }

    static State state(Catalog r1) {
        if ((r1.sectionState instanceof State) == true) goto L7;
        return null;
    L7:
        return (State) r1.sectionState;
    }
}
