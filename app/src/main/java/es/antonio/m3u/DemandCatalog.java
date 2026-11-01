package es.antonio.m3u;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes7.dex */
final class DemandCatalog {
    static final int BOOT_MAGIC = 1128809585;
    static volatile boolean sectionOpened;
    final File authoritative;
    final File cache;
    final Map<String, Integer> counts;
    volatile Directory directory;
    final File files;
    boolean knownCounts;
    final File legacyCache;
    final long modified;
    final Map<String, DemandMovies.Snapshot> movieViews;
    final Map<Entry, String> providers;
    final Map<Entry, String> remembered;
    volatile Catalog resolved;
    volatile CatalogIndex resolvedIndex;
    final long size;
    volatile Object sourceManifest;
    int total;

    static final class Directory {
        final Catalog catalog;
        final Map<String, Integer> counts;
        int created;
        final File file;
        final int[] lengths;
        final List<int[]> maps;
        final long[] offsets;
        final DemandCatalog owner;
        Map<Entry, String> providers;
        final Map<Integer, String> text;

        Directory(DemandCatalog r2) throws IOException {
            this(r2, r2.cache);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r8v19 */
        /* JADX WARN: Type inference failed for: r8v4 */
        /* JADX WARN: Type inference failed for: r8v7 */
        Directory(DemandCatalog r23, File r24) throws IOException {
            final int r2 = 128;
            final float r3 = 0.75f;
            boolean r8 = true;
            final boolean r9 = true ? 1 : 0;
            this.text = new C09841(this, r2, r3, r9);
            this.maps = new ArrayList();
            this.catalog = new Catalog();
            this.providers = Collections.synchronizedMap(new WeakHashMap());
            this.counts = new HashMap();
            this.owner = r23;
            this.file = r24;
            DemandFile r10 = new DemandFile(this.file, "r");     // Catch: Throwable -> L64
        L58:
            th = move-exception;
            Throwable r11 = th;
            r10.close();     // Catch: Throwable -> L62
            throw r11;     // Catch: Throwable -> L62
        L62:
            th = th;
        L66:
            if (r11 == null) goto L70;
            if (r11 == th) goto L87;
            r11.addSuppressed(th);
            throw r11;
        L87:
            throw r11;
        L70:
            throw th;
        L5:
            if (r10.readInt() != 1128809523) goto L57;
            int r12 = 6;
            if (r10.readInt() != 6) goto L57;
            if (r10.readBoolean() == false) goto L57;
            if (r10.readLong() != r23.modified) goto L57;
            if (r10.readLong() != r23.size) goto L57;
            int r13 = 10000000;
            int r0 = bounded(r10.readInt(), 10000000);     // Catch: Throwable -> L58
            this.offsets = new long[r0];     // Catch: Throwable -> L58
            this.lengths = new int[r0];     // Catch: Throwable -> L58
            int r14 = 0;
            int r1 = 0;
        L15:
            if (r1 >= r0) goto L16;
            int r4 = Math.min(1024, r0 - r1);     // Catch: Throwable -> L58
            int r5 = bounded(r10.readInt(), Integer.MAX_VALUE);     // Catch: Throwable -> L58
            byte[] r6 = new byte[r4 * 4];     // Catch: Throwable -> L58
            r10.readFully(r6);     // Catch: Throwable -> L58
            IntBuffer r7 = ByteBuffer.wrap(r6).asIntBuffer();     // Catch: Throwable -> L58
            long r15 = r10.getFilePointer();     // Catch: Throwable -> L58
            long r16 = 0;
            int r17 = 0;
        L47:
            if (r17 >= r4) goto L48;
            int r18 = bounded(r7.get(), 16777216);     // Catch: Throwable -> L58
            int r19 = r1 + r17;     // Catch: Throwable -> L58
            this.offsets[r19] = r15 + r16;     // Catch: Throwable -> L58
            this.lengths[r19] = r18;     // Catch: Throwable -> L58
            r16 = r16 + ((long) r18);     // Catch: Throwable -> L58
            r17 = r17 + 1;     // Catch: Throwable -> L58
            goto L47
        L48:
            long r20 = r5;
            if (r16 != r20) goto L54;
            if (r15 > (r10.length() - r20)) goto L54;
            r10.seek(r15 + r20);     // Catch: Throwable -> L58
            r1 = r1 + 1024;     // Catch: Throwable -> L58
            r8 = true;
            r12 = 6;
            r13 = 10000000;
        L54:
            throw new IOException("Invalid dictionary block");     // Catch: Throwable -> L58
        L16:
            int r21 = bounded(r10.readInt(), 1000000);     // Catch: Throwable -> L58
            int r22 = 0;
            boolean r25 = r8;
        L18:
            if (r22 >= r21) goto L19;
            int r26 = bounded(r10.readInt(), 10000) * 2;     // Catch: Throwable -> L58
            int[] r27 = new int[r26];     // Catch: Throwable -> L58
            int r28 = 0;
        L43:
            if (r28 >= r26) goto L44;
            r27[r28] = r10.readInt();     // Catch: Throwable -> L58
            r28 = r28 + 1;     // Catch: Throwable -> L58
            goto L43
        L44:
            this.maps.add(r27);     // Catch: Throwable -> L58
            r22 = r22 + 1;     // Catch: Throwable -> L58
            r25 = true;
            r12 = 6;
            r13 = 10000000;
            goto L18
        L19:
            int r29 = bounded(r10.readInt(), 1000000);     // Catch: Throwable -> L58
            int r110 = 0;
            boolean r30 = r25;
        L20:
            if (r110 >= r29) goto L21;
            int[] r31 = new int[r12];     // Catch: Throwable -> L58
            int r32 = 0;
            ?? r33 = r30;
        L35:
            if (r32 >= r12) goto L36;
            r31[r32] = r10.readInt();     // Catch: Throwable -> L58
            r32 = r32 + 1;     // Catch: Throwable -> L58
            r33 = 1;
            r12 = 6;
            r13 = 10000000;
            goto L35
        L36:
            int r34 = bounded(r10.readInt(), r13);     // Catch: Throwable -> L58
            long r35 = r10.getFilePointer();     // Catch: Throwable -> L58
            long r111 = (((long) r34) * 72) + r35;     // Catch: Throwable -> L58
            if (r111 > r10.length()) goto L40;
            Catalog.Source r36 = new Catalog.Source(value(r10, r31[0]), value(r10, r31[r33]), value(r10, r31[2]), value(r10, r31[3]), Collections.emptyList());     // Catch: Throwable -> L58
            r36.originalFilename = value(r10, r31[4]);     // Catch: Throwable -> L58
            r36.provider = value(r10, r31[5]);     // Catch: Throwable -> L58
            r36.entries = new Rows(this, r35, r34, r36.kind);     // Catch: Throwable -> L58
            this.catalog.sources.add(r36);     // Catch: Throwable -> L58
            r10.seek(r111);     // Catch: Throwable -> L58
            r110 = r110 + 1;     // Catch: Throwable -> L58
            r30 = true;
            r12 = 6;
            r13 = 10000000;
            goto L20
        L40:
            throw new IOException("Invalid records");     // Catch: Throwable -> L58
        L21:
            int r37 = bounded(r10.readInt(), 20);     // Catch: Throwable -> L58
            int[] r38 = new int[r37];     // Catch: Throwable -> L58
            int[] r39 = new int[r37];     // Catch: Throwable -> L58
            int r40 = 0;
        L22:
            if (r40 >= r37) goto L25;
            r38[r40] = r10.readInt();     // Catch: Throwable -> L58
            r39[r40] = bounded(r10.readInt(), r13);     // Catch: Throwable -> L58
            r40 = r40 + 1;     // Catch: Throwable -> L58
            goto L22
        L25:
            if (r10.getFilePointer() != r10.length()) goto L32;
        L26:
            if (r14 >= r37) goto L27;
            this.counts.put(value(r10, r38[r14]), Integer.valueOf(r39[r14]));     // Catch: Throwable -> L58
            r14 = r14 + 1;     // Catch: Throwable -> L58
            goto L26
        L27:
            r10.close();     // Catch: Throwable -> L64
            this.catalog.startupIndex = new CatalogCache.Prepared(this.providers, this.counts);
            this.catalog.startupCacheVersion = 171;
            BlackBox.event("CATALOG_DIRECTORY_SEEK", "entries=" + this.catalog.size() + " created_entries=0 dictionary_payload_skipped=true");
            SourceStore.note(this);
            return;
        L32:
            throw new IOException("Trailing cache data");     // Catch: Throwable -> L58
        L57:
            throw new IOException("Cache v6 unavailable");     // Catch: Throwable -> L58
        L64:
            th = th;
            r11 = null;
            goto L66
        }

        Directory(DemandCatalog r5, List<Catalog.Source> r6, Map<String, Integer> r7) {
            final float r1 = 0.75f;
            final boolean r2 = true;
            final int r3 = 128;
            this.text = new C09841(this, r3, r1, r2);
            this.maps = new ArrayList();
            this.catalog = new Catalog();
            this.providers = Collections.synchronizedMap(new WeakHashMap());
            this.counts = new HashMap();
            this.owner = r5;
            this.file = r5.authoritative;
            this.offsets = new long[0];
            this.lengths = new int[0];
            this.catalog.sources.addAll(r6);
            this.counts.putAll(r7);
            this.providers = new C09852(this);
            Catalog r8 = this.catalog;
            r8.startupIndex = new CatalogCache.Prepared(this.providers, this.counts);
            this.catalog.startupCacheVersion = 171;
            SourceStore.note(this);
        }

        static int bounded(int r0, int r1) throws IOException {
            if (r0 < 0) goto L6;
            if (r0 > r1) goto L6;
            return r0;
        L6:
            throw new IOException("Invalid size");
        }

        Map<String, String> headers(DemandFile r5, int r6) throws IOException {
            if (r6 != (-1)) goto L6;
            return null;
        L6:
            if (r6 < 0) goto L15;
            if (r6 >= this.maps.size()) goto L15;
            LinkedHashMap r0 = new LinkedHashMap();
            int[] r7 = this.maps.get(r6);
            int r1 = 0;
        L11:
            if (r1 >= r7.length) goto L12;
            r0.put(value(r5, r7[r1]), value(r5, r7[r1 + 1]));
            r1 = r1 + 2;
            goto L11
        L12:
            return r0;
        L15:
            throw new IOException("Invalid headers");
        }

        int[] record(DemandFile r6, long r7, int r9) throws IOException {
            byte[] r0 = new byte[72];
            r6.seek(r7 + (((long) r9) * 72));
            r6.readFully(r0);
            int[] r8 = new int[18];
            ByteBuffer.wrap(r0).asIntBuffer().get(r8);
            return r8;
        }

        synchronized String value(DemandFile r5, int r6) throws IOException {
            monitor-enter(this);
            if (r6 != (-1)) goto L8;
            monitor-exit(this);
            return null;
        L8:
            if (r6 < 0) goto L19;
        L20:
            th = move-exception;
            throw th;
        L10:
            if (r6 >= this.lengths.length) goto L19;
            String r0 = this.text.get(Integer.valueOf(r6));     // Catch: Throwable -> L20
            if (r0 == null) goto L15;
            monitor-exit(this);
            return r0;
        L15:
            byte[] r1 = new byte[this.lengths[r6]];     // Catch: Throwable -> L20
            r5.seek(this.offsets[r6]);     // Catch: Throwable -> L20
            r5.readFully(r1);     // Catch: Throwable -> L20
            String r7 = new String(r1, StandardCharsets.UTF_8);     // Catch: Throwable -> L20
            this.text.put(Integer.valueOf(r6), r7);     // Catch: Throwable -> L20
            monitor-exit(this);
            return r7;
        L19:
            throw new IOException("Invalid text reference");     // Catch: Throwable -> L20
        }
    }

    static final class Ints {
        int[] data;
        int[] frozen;
        int size;

        Ints() {
            this.data = new int[8];
        }

        void add(int r4) {
            if (this.size != this.data.length) goto L5;
            this.data = Arrays.copyOf(this.data, this.data.length * 2);
        L5:
            int[] r0 = this.data;
            int r1 = this.size;
            this.size = r1 + 1;
            r0[r1] = r4;
        }

        int[] values() {
            if (this.frozen != null) goto L6;
            this.frozen = Arrays.copyOf(this.data, this.size);
        L6:
            return this.frozen;
        }
    }

    static final class KindIndex {
        final Ints all;
        final Map<String, Map<String, Ints>> both;
        final Map<String, Set<String>> categories;
        final Map<String, Map<Long, Ints>> episodeRows;
        final int[] episodes;
        final Map<List<String>, Ints> movieNames;
        final Map<String, Ints> providers;
        final int[] seasons;
        final Map<String, Ints> series;

        KindIndex(int r2) {
            this.all = new Ints();
            this.providers = new HashMap();
            this.series = new HashMap();
            this.both = new HashMap();
            this.categories = new HashMap();
            this.movieNames = new HashMap();
            this.episodeRows = new HashMap();
            this.seasons = new int[r2];
            this.episodes = new int[r2];
        }

        static long episodeKey(int r4, int r5) {
            long r0 = ((long) r4) << 32;
            return (((long) r5) & 4294967295L) | r0;
        }

        static /* synthetic */ Ints lambda$0(String r0) {
            return new Ints();
        }

        static /* synthetic */ Ints lambda$1(String r0) {
            return new Ints();
        }

        static /* synthetic */ Map lambda$2(String r0) {
            return new HashMap();
        }

        static /* synthetic */ Ints lambda$3(String r0) {
            return new Ints();
        }

        void add(String r3, String r4, int r5, int r6, int r7) {
            this.all.add(r5);
            this.providers.computeIfAbsent(r3, new DemandCatalog$KindIndex$$ExternalSyntheticLambda0()).add(r5);
            this.series.computeIfAbsent(r4, new DemandCatalog$KindIndex$$ExternalSyntheticLambda1()).add(r5);
            this.both.computeIfAbsent(r3, new DemandCatalog$KindIndex$$ExternalSyntheticLambda2()).computeIfAbsent(r4, new DemandCatalog$KindIndex$$ExternalSyntheticLambda3()).add(r5);
            this.seasons[r5] = r6;
            this.episodes[r5] = r7;
        }

        int[] history(Entry r4) {
            if ("Películas".equals(r4.kind) == false) goto L6;
            Ints r5 = this.movieNames.get(Arrays.asList(new String[]{r4.title, r4.group}));
        L12:
            if (r5 != null) goto L15;
            return new int[0];
        L15:
            return r5.values();
        L6:
            if ("Series".equals(r4.kind) == false) goto L11;
            Map<Long, Ints> r0 = this.episodeRows.get(CatalogCounts.series(r4));
            if (r0 != null) goto L10;
            r5 = null;
            goto L12
        L10:
            r5 = r0.get(Long.valueOf(episodeKey(r4.season, r4.episode)));
            goto L12
        L11:
            r5 = this.all;
            goto L12
        }

        int[] select(String r2, String r3) {
            if (r2 != null) goto L7;
            if (r3 != null) goto L5;
            Ints r4 = this.all;
        L12:
            if (r4 != null) goto L15;
            return new int[0];
        L15:
            return r4.values();
        L5:
            Map<String, Ints> r5 = this.series;
        L6:
            r4 = r5.get(r3);
            goto L12
        L7:
            if (r3 != null) goto L9;
            r4 = this.providers.get(r2);
            goto L12
        L9:
            r5 = this.both.get(r2);
            if (r5 != null) goto L6;
            r4 = null;
            goto L12
        }
    }

    static final class Rows extends AbstractList<Entry> implements Serializable {
        private static final long serialVersionUID = 1;
        int batchOpens;
        final int count;
        final transient Directory directory;
        final Map<String, int[]> historyMatches;
        boolean historyMatchesDirty;
        boolean historyMatchesLoaded;
        long historyRecordReads;
        final transient Map<Integer, Entry> loaded;
        final long records;
        int selectionBuilds;
        final transient Map<String, KindIndex> selectionIndexes;
        final String sourceKind;

        Rows(Directory r5, long r6, int r8, String r9) {
            final float r1 = 0.75f;
            final boolean r2 = true;
            final int r3 = 64;
            this.loaded = new C09861(this, r3, r1, r2);
            this.selectionIndexes = new HashMap();
            this.historyMatches = new LinkedHashMap();
            this.directory = r5;
            this.records = r6;
            this.count = r8;
            this.sourceKind = r9;
        }

        static /* synthetic */ Set lambda$0(String r0) {
            return new HashSet();
        }

        static /* synthetic */ Map lambda$1(String r0) {
            return new HashMap();
        }

        static /* synthetic */ Ints lambda$2(Long r0) {
            return new Ints();
        }

        static /* synthetic */ Ints lambda$3(List r0) {
            return new Ints();
        }

        private Object writeReplace() {
            return new ArrayList(this);
        }

        Entry find(Entry r2, String r3) {
            return find(r2, r3, false);
        }

        Entry find(Entry r17, String r18, boolean r19) {
            Entry r4 = null;
            if ("Mixta".equals(this.sourceKind) == false) goto L5;
        L9:
            String r3 = WatchHistory.m418id(r17);
            if ("Películas".equals(r17.kind) == false) goto L12;
        L15:
            boolean r5 = true;
        L16:
            if (r5 == false) goto L18;
            int[] r8 = DemandMatches.get(this, r3);
        L19:
            if (r8 != null) goto L62;
            r8 = index(r17.kind).history(r17);
            if (r5 == false) goto L62;
            DemandMatches.put(this, r3, r8);
        L62:
            DemandFile r6 = new DemandFile(this.directory.file, "r");     // Catch: IOException -> L59
            int r9 = r8.length;     // Catch: Throwable -> L51
            int r10 = 0;
        L25:
            if (r10 >= r9) goto L49;
            int r11 = r8[r10];     // Catch: Throwable -> L51
            this.historyRecordReads++;     // Catch: Throwable -> L51
            int[] r12 = this.directory.record(r6, this.records, r11);     // Catch: Throwable -> L51
            if (r12[13] != r17.season) goto L48;
            if (r12[14] != r17.episode) goto L48;
            if (r18 == null) goto L36;
            if (r18.equals(TvProviderFormats220.saved(this.directory.value(r6, r12[17]), r17.kind, this.directory, r6, r12)) == false) goto L48;
        L36:
            if (Objects.equals(r17.title, this.directory.value(r6, r12[0])) == false) goto L48;
            if (Objects.equals(r17.group, this.directory.value(r6, r12[1])) == false) goto L48;
            Entry r13 = get(r11);     // Catch: Throwable -> L51
            if (r3.equals(WatchHistory.m418id(r13)) == false) goto L48;
            if (r19 == true) goto L44;
        L47:
            r4 = r13;
            goto L48
        L44:
            if (Entry.details(r13).url.equals(Entry.details(r17).url) == false) goto L48;
            if (Objects.equals(r13.headers, r17.headers) == true) goto L47;
        L48:
            r10 = r10 + 1;
            goto L25
        L49:
            r6.close();     // Catch: IOException -> L59
            return r4;
        L51:
            th = move-exception;
            r6.close();     // Catch: Throwable -> L55
        L77:
            throw th;     // Catch: IOException -> L59
        L55:
            th = move-exception;
            th.addSuppressed(th);     // Catch: IOException -> L59
            throw th;     // Catch: IOException -> L59
        L59:
            e = move-exception;
            throw new IllegalStateException(e);
        L18:
            r8 = null;
            goto L19
        L12:
            if ("Series".equals(r17.kind) == true) goto L15;
            r5 = false;
            goto L16
        L5:
            if (this.sourceKind == null) goto L9;
            if (this.sourceKind.equals(r17.kind) == true) goto L9;
            return null;
        }

        @Override // java.util.AbstractList, java.util.List
        public synchronized Entry get(int r5) {
            monitor-enter(this);
            if (r5 < 0) goto L29;
        L30:
            th = move-exception;
            throw th;
        L5:
            if (r5 >= this.count) goto L29;
            Entry r0 = this.loaded.get(Integer.valueOf(r5));     // Catch: Throwable -> L30
            if (r0 == null) goto L10;
            monitor-exit(this);
            return r0;
        L10:
            Throwable r1 = null;
            DemandFile r2 = new DemandFile(this.directory.file, "r");     // Catch: Throwable -> L19
            Entry r6 = readEntry(r2, r5);     // Catch: Throwable -> L16
            r2.close();     // Catch: Throwable -> L19
            monitor-exit(this);
            return r6;
        L16:
            th = move-exception;
            r2.close();     // Catch: Throwable -> L19
            throw th;     // Catch: Throwable -> L19
        L19:
            th = move-exception;
            if (0 == 0) goto L39;
            if (null == th) goto L24;
            r1.addSuppressed(th);     // Catch: IOException -> L25 Throwable -> L30
        L25:
            e = move-exception;
            throw new IllegalStateException("No se pudo leer el contenido solicitado", e);     // Catch: Throwable -> L30
        L24:
            throw null;     // Catch: IOException -> L25 Throwable -> L30
        L39:
            throw th;     // Catch: IOException -> L25 Throwable -> L30
        L29:
            throw new IndexOutOfBoundsException();     // Catch: Throwable -> L30
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        synchronized KindIndex index(String r23) {
            monitor-enter(this);
            KindIndex r2 = this.selectionIndexes.get(r23);     // Catch: Throwable -> L43
            if (r2 == null) goto L8;
            monitor-exit(this);
            return r2;
        L8:
            KindIndex r9 = new KindIndex(this.count);     // Catch: Throwable -> L43
            long r10 = System.nanoTime();     // Catch: Throwable -> L43
            DemandFile r12 = new DemandFile(this.directory.file, "r");     // Catch: IOException -> L40 Throwable -> L43
            int r14 = 0;
        L46:
            if (r14 >= this.count) goto L27;
            int r15 = Math.min(1024, this.count - r14);     // Catch: Throwable -> L32
            byte[] r3 = new byte[r15 * 72];     // Catch: Throwable -> L32
            r12.seek(this.records + (((long) r14) * 72));     // Catch: Throwable -> L32
            r12.readFully(r3);     // Catch: Throwable -> L32
            IntBuffer r8 = ByteBuffer.wrap(r3).asIntBuffer();     // Catch: Throwable -> L32
            int r7 = 0;
        L14:
            if (r7 >= r15) goto L26;
            int r6 = r7 * 18;     // Catch: Throwable -> L32
            if (r23.equals(this.directory.value(r12, r8.get(r6 + 4))) == false) goto L24;
            int r5 = r6 + 17;     // Catch: Throwable -> L32
            int r17 = r7;
            String r4 = TvProviderFormats220.record(this.directory.value(r12, r8.get(r5)), r23, this.directory, r12, r8, r6);     // Catch: Throwable -> L32
            String r11 = series(r12, r8, r6);     // Catch: Throwable -> L32
            int r13 = r14 + r17;     // Catch: Throwable -> L32
            int r16 = r6 + 13;     // Catch: Throwable -> L32
            int r18 = r6 + 14;     // Catch: Throwable -> L32
            int r19 = r15;
            long r20 = r10;
            int r110 = r14;
            IntBuffer r111 = r8;
            r9.add(r4, r11, r13, r8.get(r16), r8.get(r18));     // Catch: Throwable -> L32
            Set<String> r21 = r9.categories.computeIfAbsent(TvProviderFormats220.record(this.directory.value(r12, r111.get(r5)), r23, this.directory, r12, r111, r6), new DemandCatalog$Rows$$ExternalSyntheticLambda0());     // Catch: Throwable -> L32
            int r22 = r6 + 1;     // Catch: Throwable -> L32
            r21.add(this.directory.value(r12, r111.get(r22)));     // Catch: Throwable -> L32
            if ("Series".equals(r23) == false) goto L21;
            r9.episodeRows.computeIfAbsent(series(r12, r111, r6), new DemandCatalog$Rows$$ExternalSyntheticLambda1()).computeIfAbsent(Long.valueOf(KindIndex.episodeKey(r111.get(r16), r111.get(r18))), new DemandCatalog$Rows$$ExternalSyntheticLambda2()).add(r13);     // Catch: Throwable -> L32
        L21:
            if ("Películas".equals(r23) == false) goto L25;
            r9.movieNames.computeIfAbsent(Arrays.asList(new String[]{this.directory.value(r12, r111.get(r6)), this.directory.value(r12, r111.get(r22))}), new DemandCatalog$Rows$$ExternalSyntheticLambda3()).add(r13);     // Catch: Throwable -> L32
        L25:
            r7 = r17 + 1;
            r8 = r111;
            r14 = r110;
            r15 = r19;
            r10 = r20;
            goto L14
        L24:
            r17 = r7;
            r20 = r10;
            r110 = r14;
            r19 = r15;
            r111 = r8;
            goto L25
        L26:
            r14 = r14 + 1024;
            goto L46
        L27:
            long r24 = r10;
            r12.close();     // Catch: IOException -> L40 Throwable -> L43
            this.selectionIndexes.put(r23, r9);     // Catch: Throwable -> L43
            this.selectionBuilds++;     // Catch: Throwable -> L43
            BlackBox.event("CATALOG_SELECTION_INDEX", "kind=" + r23 + " entries=" + r9.all.size + " duration_ms=" + ((System.nanoTime() - r24) / 1000000));     // Catch: Throwable -> L43
            monitor-exit(this);
            return r9;
        L32:
            th = move-exception;
            r12.close();     // Catch: Throwable -> L36
            throw th;     // Catch: IOException -> L40 Throwable -> L43
        L36:
            th = move-exception;
            th.addSuppressed(th);     // Catch: IOException -> L40 Throwable -> L43
            throw th;     // Catch: IOException -> L40 Throwable -> L43
        L40:
            e = move-exception;
            throw new IllegalStateException(e);     // Catch: Throwable -> L43
        L43:
            th = move-exception;
            throw th;
        }

        synchronized void materialize(String r8, String r9, String r10, Integer r11) {
            monitor-enter(this);
        L38:
            th = move-exception;
            throw th;
        L4:
            if (this.sourceKind != null) goto L6;
        L11:
            KindIndex r12 = index(r8);     // Catch: Throwable -> L38
            int[] r13 = r12.select(r9, r10);     // Catch: Throwable -> L38
            Throwable r14 = null;
            DemandFile r0 = new DemandFile(this.directory.file, "r");     // Catch: Throwable -> L29
            this.batchOpens++;     // Catch: Throwable -> L25
            int r1 = r13.length;     // Catch: Throwable -> L25
            int r3 = 0;
        L16:
            if (r3 >= r1) goto L17;
            int r4 = r13[r3];     // Catch: Throwable -> L25
            if (r11 != null) goto L22;
        L23:
            readEntry(r0, r4);     // Catch: Throwable -> L25
        L24:
            r3 = r3 + 1;
            goto L16
        L22:
            if (Math.max(0, r12.seasons[r4]) != r11.intValue()) goto L24;
        L17:
            r0.close();     // Catch: Throwable -> L29
        L9:
            monitor-exit(this);
            return;
        L25:
            th = move-exception;
            r0.close();     // Catch: Throwable -> L29
            throw th;     // Catch: Throwable -> L29
        L29:
            th = move-exception;
            if (0 == 0) goto L50;
            if (null == th) goto L34;
            r14.addSuppressed(th);     // Catch: IOException -> L35 Throwable -> L38
        L35:
            e = move-exception;
            throw new IllegalStateException(e);     // Catch: Throwable -> L38
        L34:
            throw null;     // Catch: IOException -> L35 Throwable -> L38
        L50:
            throw th;     // Catch: IOException -> L35 Throwable -> L38
        L6:
            if ("Mixta".equals(this.sourceKind) == true) goto L11;
            if (r8.equals(this.sourceKind) == true) goto L11;
            goto L9
        }

        synchronized int materialized() {
            monitor-enter(this);
            int r0 = this.loaded.size();     // Catch: Throwable -> L6
            monitor-exit(this);
            return r0;
        L6:
            th = move-exception;
            throw th;
        }

        Entry readEntry(DemandFile r8, int r9) throws IOException {
            Entry r0 = this.loaded.get(Integer.valueOf(r9));
            if (r0 == null) goto L5;
            return r0;
        L5:
            int[] r1 = this.directory.record(r8, this.records, r9);
            String[] r2 = new String[13];
            int r4 = 0;
        L6:
            if (r4 >= 13) goto L8;
            r2[r4] = this.directory.value(r8, r1[r4]);
            r4 = r4 + 1;
            goto L6
        L8:
            Entry r5 = new Entry(false);
            r5.title = r2[0];
            r5.group = r2[1];
            r5.logo = r2[2];
            r5.url = r2[3];
            r5.kind = r2[4];
            r5.series = r2[5];
            r5.manifest = r2[6];
            r5.licenseType = r2[7];
            r5.license = r2[8];
            r5.plexBase = r2[9];
            r5.plexKey = r2[10];
            r5.tvgId = r2[11];
            r5.tvgName = r2[12];
            r5.season = r1[13];
            r5.episode = r1[14];
            r5.headers = this.directory.headers(r8, r1[15]);
            r5.licenseHeaders = this.directory.headers(r8, r1[16]);
            this.directory.providers.put(r5, TvProviderFormats220.label(this.directory.value(r8, r1[17]), r5));
            this.loaded.put(Integer.valueOf(r9), r5);
            this.directory.created++;
            return r5;
        }

        List<Entry> select(String r7, String r8, String r9, Integer r10) {
            if ("Mixta".equals(this.sourceKind) == true) goto L11;
            if (this.sourceKind == null) goto L11;
            if (this.sourceKind.equals(r7) == true) goto L11;
            return Collections.emptyList();
        L11:
            if (r7.equals(this.sourceKind) == false) goto L16;
            if (r8 != null) goto L16;
            if (r9 != null) goto L16;
            if (r10 != null) goto L16;
            return this;
        L16:
            KindIndex r11 = index(r7);
            final int[] r12 = r11.select(r8, r9);
            if (r10 == null) goto L23;
            Ints r13 = new Ints();
            int r0 = r12.length;
            int r2 = 0;
        L20:
            if (r2 >= r0) goto L21;
            int r3 = r12[r2];
            if (Math.max(0, r11.seasons[r3]) != r10.intValue()) goto L27;
            r13.add(r3);
        L27:
            r2 = r2 + 1;
            goto L20
        L21:
            r12 = r13.values();
        L23:
            return new C09872(this, r12);
        }

        String series(DemandFile r3, IntBuffer r4, int r5) throws IOException {
            String r0 = this.directory.value(r3, r4.get(r5 + 5));
            if (r0 == null) goto L7;
            if (r0.isEmpty() == true) goto L7;
            return r0;
        L7:
            return this.directory.value(r3, r4.get(r5 + 1));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.count;
        }
    }

    private DemandCatalog() {
        final float r1 = 0.75f;
        final boolean r2 = true;
        final int r3 = 4;
        this.movieViews = new C09801(this, r3, r1, r2);
        this.counts = new HashMap();
        this.remembered = Collections.synchronizedMap(new WeakHashMap());
        this.providers = new C09812(this);
        this.legacyCache = null;
        this.authoritative = null;
        this.cache = null;
        this.files = null;
        this.size = 0;
        this.modified = 0;
    }

    DemandCatalog(File r2, File r3, File r4) {
        this(r2, r3, r4, null);
    }

    DemandCatalog(File r5, File r6, File r7, long r8, long r10) {
        final float r1 = 0.75f;
        final boolean r2 = true;
        final int r3 = 4;
        this.movieViews = new C09801(this, r3, r1, r2);
        this.counts = new HashMap();
        this.remembered = Collections.synchronizedMap(new WeakHashMap());
        this.providers = new C09812(this);
        this.files = r5;
        this.cache = r6;
        this.authoritative = r7;
        this.legacyCache = null;
        this.modified = r8;
        this.size = r10;
    }

    DemandCatalog(File r5, File r6, File r7, File r8) {
        final float r1 = 0.75f;
        final boolean r2 = true;
        final int r3 = 4;
        this.movieViews = new C09801(this, r3, r1, r2);
        this.counts = new HashMap();
        this.remembered = Collections.synchronizedMap(new WeakHashMap());
        this.providers = new C09812(this);
        this.files = r5;
        this.cache = r6;
        this.legacyCache = r8;
        this.authoritative = r7;
        this.modified = r7.lastModified();
        this.size = r7.length();
        readBoot();
    }

    private File bootFile() {
        return new File(this.files, "catalog-home-v171.bin");
    }

    private void readBoot() {
        Throwable r0 = null;
        DataInputStream r1 = new DataInputStream(new FileInputStream(bootFile()));     // Catch: Throwable -> L43
    L40:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L43
        throw th;     // Catch: Throwable -> L43
    L5:
        if (r1.readInt() == BOOT_MAGIC) goto L7;
    L38:
        r1.close();     // Catch: Throwable -> L43
        return;
    L7:
        if (r1.readLong() != this.modified) goto L38;
        if (r1.readLong() != this.size) goto L38;
        int r2 = r1.readInt();     // Catch: Throwable -> L40
        if (r2 >= 0) goto L14;
    L36:
        r1.close();     // Catch: Throwable -> L43
        return;
    L14:
        if (r2 > 10000000) goto L36;
        HashMap r4 = new HashMap();     // Catch: Throwable -> L40
        int r5 = r1.readInt();     // Catch: Throwable -> L40
        if (r5 >= 0) goto L19;
    L34:
        r1.close();     // Catch: Throwable -> L43
        return;
    L19:
        if (r5 > 20) goto L34;
        int r6 = 0;
    L22:
        if (r6 >= r5) goto L23;
        String r7 = r1.readUTF();     // Catch: Throwable -> L40
        int r8 = r1.readInt();     // Catch: Throwable -> L40
        if (r8 < 0) goto L32;
        if (r8 > 10000000) goto L32;
        r4.put(r7, Integer.valueOf(r8));     // Catch: Throwable -> L40
        r6 = r6 + 1;
    L32:
        r1.close();     // Catch: Throwable -> L43
        return;
    L23:
        this.counts.putAll(r4);     // Catch: Throwable -> L40
        this.total = r2;     // Catch: Throwable -> L40
        this.knownCounts = true;     // Catch: Throwable -> L40
        r1.close();     // Catch: Throwable -> L43
        return;
    L43:
        th = move-exception;
        if (0 == 0) goto L48;
        if (null == th) goto L59;
        r0.addSuppressed(th);     // Catch: IOException -> L49
        throw null;     // Catch: IOException -> L49
    L50:
        return;
    L59:
        throw null;     // Catch: IOException -> L49
    L48:
        throw th;     // Catch: IOException -> L49
        goto L50
    }

    private void saveBoot() {
        File r0 = new File(this.files, "catalog-home-v171.tmp");
        Throwable r1 = null;
        FileOutputStream r2 = new FileOutputStream(r0);     // Catch: Throwable -> L27
        DataOutputStream r3 = new DataOutputStream(r2);     // Catch: Throwable -> L20
        r3.writeInt(BOOT_MAGIC);     // Catch: Throwable -> L17
        r3.writeLong(this.modified);     // Catch: Throwable -> L17
        r3.writeLong(this.size);     // Catch: Throwable -> L17
        r3.writeInt(this.total);     // Catch: Throwable -> L17
        r3.writeInt(this.counts.size());     // Catch: Throwable -> L17
        Iterator<Map.Entry<String, Integer>> r4 = this.counts.entrySet().iterator();     // Catch: Throwable -> L17
    L8:
        if (r4.hasNext() == false) goto L9;
        Map.Entry<String, Integer> r5 = r4.next();     // Catch: Throwable -> L17
        r3.writeUTF(r5.getKey());     // Catch: Throwable -> L17
        r3.writeInt(r5.getValue().intValue());     // Catch: Throwable -> L17
        goto L8
    L9:
        r3.flush();     // Catch: Throwable -> L17
        r2.getFD().sync();     // Catch: Throwable -> L17
        if (r0.renameTo(bootFile()) == true) goto L12;
        r0.delete();     // Catch: Throwable -> L17
    L12:
        r3.close();     // Catch: Throwable -> L20
        r2.close();     // Catch: Throwable -> L27
        return;
    L17:
        th = move-exception;
        r3.close();     // Catch: Throwable -> L20
        throw th;     // Catch: Throwable -> L20
    L20:
        th = move-exception;
        if (0 == 0) goto L24;
        if (null == th) goto L25;
        r1.addSuppressed(th);     // Catch: Throwable -> L27
    L25:
        r2.close();     // Catch: Throwable -> L27
        throw r1;     // Catch: Throwable -> L27
    L24:
        r1 = th;
    L27:
        th = move-exception;
        if (0 == 0) goto L32;
        if (null == th) goto L43;
        r1.addSuppressed(th);     // Catch: IOException -> L33
        throw null;     // Catch: IOException -> L33
    L34:
        r0.delete();
        return;
    L43:
        throw null;     // Catch: IOException -> L33
    L32:
        throw th;     // Catch: IOException -> L33
        goto L34
    }

    static void saveHome(File r1, File r2, File r3, Map<String, Integer> r4, int r5) {
        DemandCatalog r0 = new DemandCatalog(r1, r2, r3);
        r0.counts.clear();
        r0.counts.putAll(r4);
        r0.total = r5;
        r0.knownCounts = true;
        r0.saveBoot();
    }

    List<Entry> allEntries() {
        return new C09823(this);
    }

    Set<String> categories(String r7, String r8) {
        resolve();
        if (this.directory != null) goto L6;
        return null;
    L6:
        TreeSet r0 = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        Iterator<Catalog.Source> r1 = this.directory.catalog.sources.iterator();
    L8:
        if (r1.hasNext() == false) goto L9;
        Catalog.Source r2 = r1.next();
        Rows r3 = (Rows) r2.entries;
        if (r2.kind == null) goto L17;
        if ("Mixta".equals(r2.kind) == true) goto L17;
        if (r7.equals(r2.kind) == false) goto L8;
    L17:
        Map<String, Set<String>> r4 = r3.index(r7).categories;
        if (r8 == null) goto L19;
        Set<String> r5 = r4.get(r8);
        if (r5 == null) goto L8;
        r0.addAll(r5);
        goto L8
    L19:
        Iterator<Set<String>> r6 = r4.values().iterator();
    L21:
        if (r6.hasNext() == false) goto L8;
        r0.addAll(r6.next());
        goto L21
    L9:
        return r0;
    }

    int count(String r2) {
        Integer r3 = this.counts.get(r2);
        if (r3 != null) goto L6;
        return 0;
    L6:
        return r3.intValue();
    }

    int created() {
        if (this.directory == null) goto L5;
        Set r0 = Collections.newSetFromMap(new IdentityHashMap());
        Iterator<Catalog.Source> r1 = this.directory.catalog.sources.iterator();
        int r2 = 0;
    L8:
        if (r1.hasNext() == false) goto L9;
        Directory r3 = ((Rows) r1.next().entries).directory;
        if (r0.add(r3) == false) goto L8;
        r2 = r2 + r3.created;
        goto L8
    L9:
        return r2;
    L5:
        return materialized();
    }

    List<Entry> historyCandidates(List<WatchHistory.Item> r8, String r9, String r10) {
        resolve();
        if (this.directory != null) goto L5;
        return null;
    L5:
        if (r10 != null) goto L7;
    L13:
        ArrayList r0 = new ArrayList();
        Iterator<WatchHistory.Item> r11 = r8.iterator();
    L15:
        if (r11.hasNext() == false) goto L16;
        WatchHistory.Item r2 = r11.next();
        if (r9.equals(r2.entry.kind) == false) goto L15;
        Iterator<Catalog.Source> r3 = this.directory.catalog.sources.iterator();
        Entry r4 = null;
    L26:
        if (r3.hasNext() == false) goto L27;
        Entry r5 = ((Rows) r3.next().entries).find(r2.entry, r10);
        if (r5 == null) goto L26;
        r4 = r5;
        goto L26
    L27:
        if (r4 == null) goto L15;
        r0.add(r4);
        goto L15
    L16:
        Iterator<Catalog.Source> r6 = this.directory.catalog.sources.iterator();
    L18:
        if (r6.hasNext() == false) goto L19;
        DemandMatches.save((Rows) r6.next().entries);
        goto L18
    L19:
        return r0;
    L7:
        if ("Películas".equals(r9) == false) goto L9;
    L10:
        List<Entry> r1 = providerHistory(r8, r9, r10);
        if (r1 == null) goto L13;
        return r1;
    L9:
        if ("Series".equals(r9) == false) goto L13;
        goto L10
    }

    void materialize(String r3, String r4, String r5, Integer r6) {
        resolve();
        if (this.directory == null) goto L9;
        Iterator<Catalog.Source> r0 = this.directory.catalog.sources.iterator();
    L6:
        if (r0.hasNext() == false) goto L11;
        ((Rows) r0.next().entries).materialize(r3, r4, r5, r6);
        goto L6
    L11:
        return;
    }

    int materialized() {
        if (this.resolved != null) goto L6;
        return 0;
    L6:
        return this.resolved.materializedSize();
    }

    List<Entry> providerHistory(List<WatchHistory.Item> r20, String r21, String r22) {
        ArrayList r3 = new ArrayList();
        Iterator<WatchHistory.Item> r4 = r20.iterator();
    L4:
        if (r4.hasNext() == false) goto L6;
        WatchHistory.Item r1 = r4.next();
        if (r21.equals(r1.entry.kind) == false) goto L4;
        r3.add(r1);
        goto L4
    L6:
        if (r3.isEmpty() == true) goto L8;
        int r5 = r3.size();
        Entry[] r6 = new Entry[r5];
        long[] r7 = new long[r3.size()];
        Arrays.fill(r7, -1);
        IdentityHashMap r8 = new IdentityHashMap();
        Iterator<Catalog.Source> r9 = this.directory.catalog.sources.iterator();
        int r10 = 0;
    L11:
        if (r9.hasNext() == false) goto L136;
        int r11 = r5;
        r8.put((Rows) r9.next().entries, Integer.valueOf(r10));
        DemandCatalog this = this;
        r10 = r10 + 1;
        r5 = r11;
        goto L11
    L136:
    L120:
        e = move-exception;
        BlackBox.event("PROVIDER_HISTORY_FALLBACK", "type=" + e.getClass().getSimpleName());
        return null;
    L14:
        if ("Películas".equals(r21) == false) goto L51;
        DemandMovies.Snapshot r0 = DemandMovies.snapshot(this, r22);     // Catch: Throwable -> L120 IOException -> L122
        if (r0 != null) goto L18;
        return null;
    L18:
        HashMap r2 = new HashMap();     // Catch: Throwable -> L120 IOException -> L122
        int r12 = 0;
    L20:
        if (r12 >= r3.size()) goto L21;
        int r16 = r5;
        String r13 = ((WatchHistory.Item) r3.get(r12)).entry.title;     // Catch: Throwable -> L120 IOException -> L122
        List r14 = (List) r2.get(r13);     // Catch: Throwable -> L120 IOException -> L122
        if (r14 != null) goto L50;
        r14 = new ArrayList();     // Catch: Throwable -> L120 IOException -> L122
        r2.put(r13, r14);     // Catch: Throwable -> L120 IOException -> L122
    L50:
        r14.add(Integer.valueOf(r12));     // Catch: Throwable -> L120 IOException -> L122
        r12 = r12 + 1;     // Catch: Throwable -> L120 IOException -> L122
        r5 = r16;
        goto L20
    L21:
        Iterator<DemandMovies.Ref> r15 = r0.refs.iterator();     // Catch: Throwable -> L120 IOException -> L122
    L23:
        if (r15.hasNext() == false) goto L24;
        DemandMovies.Ref r17 = r15.next();     // Catch: Throwable -> L120 IOException -> L122
        List r18 = (List) r2.get(r17.title);     // Catch: Throwable -> L120 IOException -> L122
        if (r18 == null) goto L23;
        Iterator r19 = r18.iterator();     // Catch: Throwable -> L120 IOException -> L122
    L30:
        if (r19.hasNext() == false) goto L23;
        int r110 = ((Integer) r19.next()).intValue();     // Catch: Throwable -> L120 IOException -> L122
        Entry r111 = ((WatchHistory.Item) r3.get(r110)).entry;     // Catch: Throwable -> L120 IOException -> L122
        if (r111.season != r17.season) goto L30;
        if (r111.episode != r17.episode) goto L30;
        if (Objects.equals(r111.group, r17.group) == false) goto L30;
        Entry r112 = r17.entry();     // Catch: Throwable -> L120 IOException -> L122
        Iterator r23 = r19;
        int r113 = r5;
        long r114 = (((long) ((Integer) r8.get(r17.rows)).intValue()) << 32) + (((long) r17.row) & 4294967295L);     // Catch: Throwable -> L120 IOException -> L122
        if (r114 > r7[r110]) goto L42;
    L44:
        r19 = r23;
        r5 = r113;
        goto L30
    L42:
        if (WatchHistory.m418id(r111).equals(WatchHistory.m418id(r112)) == false) goto L44;
        r6[r110] = r112;     // Catch: Throwable -> L120 IOException -> L122
        r7[r110] = r114;     // Catch: Throwable -> L120 IOException -> L122
        goto L44
    L24:
        int r115 = r5;
    L58:
        ArrayList r24 = new ArrayList();     // Catch: Throwable -> L120 IOException -> L122
        int r116 = 0;
    L59:
        int r117 = r115;
        if (r116 >= r117) goto L61;
        Entry r25 = r6[r116];     // Catch: Throwable -> L120 IOException -> L122
        if (r25 == null) goto L65;
        r24.add(r25);     // Catch: Throwable -> L120 IOException -> L122
    L65:
        r116 = r116 + 1;     // Catch: Throwable -> L120 IOException -> L122
        r115 = r117;
        goto L59
    L61:
        return r24;
    L51:
        r115 = r5;
        DemandSeriesIndex.Index r26 = DemandSeriesIndex.get(this, r22);     // Catch: Throwable -> L120 IOException -> L122
        if (r26 != null) goto L55;
        return null;
    L55:
        HashMap r27 = new HashMap();     // Catch: Throwable -> L120 IOException -> L122
        int r28 = 0;
    L57:
        if (r28 >= r3.size()) goto L58;
        int r118 = r115;
        Entry r29 = ((WatchHistory.Item) r3.get(r28)).entry;     // Catch: Throwable -> L120 IOException -> L122
        String r30 = r29.series;     // Catch: Throwable -> L120 IOException -> L122
        if (r30 == null) goto L177;
        if (r30.isEmpty() == true) goto L118;
        Map<Rows, int[]> r31 = (Map) r27.get(r30);     // Catch: Throwable -> L120 IOException -> L122
        if (r31 != null) goto L74;
        r31 = DemandSeriesIndex.select(this, r26, r30);     // Catch: Throwable -> L120 IOException -> L122
        r27.put(r30, r31);     // Catch: Throwable -> L120 IOException -> L122
    L74:
        Iterator<Catalog.Source> r32 = this.directory.catalog.sources.iterator();     // Catch: Throwable -> L120 IOException -> L122
    L76:
        if (r32.hasNext() == false) goto L77;
        Rows r119 = (Rows) r32.next().entries;     // Catch: Throwable -> L120 IOException -> L122
        int[] r120 = r31.get(r119);     // Catch: Throwable -> L120 IOException -> L122
        if (r120 == null) goto L76;
        DemandFile r121 = new DemandFile(r119.directory.file, "r");     // Catch: Throwable -> L111
        int r122 = r120.length;     // Catch: Throwable -> L105
        int r123 = 0;
    L84:
        if (r123 >= r122) goto L85;
        DemandSeriesIndex.Index r210 = r26;
        int r33 = r120[r123];     // Catch: Throwable -> L105
        Iterator<Catalog.Source> r124 = r32;
        Map<Rows, int[]> r125 = r31;
        int[] r34 = r119.directory.record(r121, r119.records, r33);     // Catch: Throwable -> L105
        if (r34[13] == r29.season) goto L91;
    L103:
        HashMap r211 = r27;
    L104:
        r123 = r123 + 1;
        this = this;
        r27 = r211;
        r26 = r210;
        r31 = r125;
        r32 = r124;
        goto L84
    L91:
        if (r34[14] != r29.episode) goto L103;
        r211 = r27;
        if (Objects.equals(r29.title, r119.directory.value(r121, r34[0])) == false) goto L104;
        if (Objects.equals(r29.group, r119.directory.value(r121, r34[1])) == false) goto L104;
        if (r22.equals(r119.directory.value(r121, r34[17])) == false) goto L104;
        Entry r35 = r119.get(r33);     // Catch: Throwable -> L105
        if (WatchHistory.m418id(r29).equals(WatchHistory.m418id(r35)) == false) goto L104;
        r6[r28] = r35;     // Catch: Throwable -> L105
        goto L104
    L85:
        r121.close();     // Catch: Throwable -> L111
    L105:
        th = move-exception;
        Throwable r36 = th;
        r121.close();     // Catch: Throwable -> L109
        throw r36;     // Catch: Throwable -> L109
    L109:
        th = th;
    L113:
        if (r36 == null) goto L117;
        if (r36 == th) goto L175;
        r36.addSuppressed(th);     // Catch: Throwable -> L120 IOException -> L122
        throw r36;     // Catch: Throwable -> L120 IOException -> L122
    L175:
        throw r36;     // Catch: Throwable -> L120 IOException -> L122
    L117:
        throw th;     // Catch: Throwable -> L120 IOException -> L122
    L111:
        th = th;
        r36 = null;
        goto L113
    L77:
        r28 = r28 + 1;     // Catch: Throwable -> L120 IOException -> L122
        r115 = r118;
        goto L57
    L118:
        return null;
    L177:
        return null;
    L8:
        return new ArrayList();
    }

    synchronized Catalog resolve() {
        monitor-enter(this);
    L60:
        th = move-exception;
        throw th;
    L63:
        if (this.resolved == null) goto L69;
        Catalog r0 = this.resolved;     // Catch: Throwable -> L60
        monitor-exit(this);
        return r0;
    L69:
    L16:
        e = move-exception;
        if (this.legacyCache == null) goto L21;
        this.directory = new Directory(this, this.legacyCache);     // Catch: Exception -> L14 IOException -> L22 Throwable -> L60
    L20:
        Catalog r1 = this.directory.catalog;     // Catch: Exception -> L14 IOException -> L22 Throwable -> L60
    L38:
        if (this.directory != null) goto L40;
        BlackBox.event("CATALOG_SECTION_FALLBACK", "directory_missing=true rebuild_on_navigation=false");     // Catch: Exception -> L14 Throwable -> L60
    L40:
        CatalogCache.Prepared r2 = CatalogCache.prepared(r1);     // Catch: Exception -> L14 Throwable -> L60
        if (r2 != null) goto L43;
        CatalogIndex r3 = new CatalogIndex(r1);     // Catch: Exception -> L14 Throwable -> L60
    L44:
        this.counts.clear();     // Catch: Exception -> L14 Throwable -> L60
        this.counts.putAll(r3.counts());     // Catch: Exception -> L14 Throwable -> L60
        this.total = r1.size();     // Catch: Exception -> L14 Throwable -> L60
        this.knownCounts = true;     // Catch: Exception -> L14 Throwable -> L60
        this.resolvedIndex = r3;     // Catch: Exception -> L14 Throwable -> L60
        this.resolved = r1;     // Catch: Exception -> L14 Throwable -> L60
        saveBoot();     // Catch: Exception -> L14 Throwable -> L60
        BlackBox.event("CATALOG_DIRECTORY_READY", "entries=" + this.total + " created_entries=" + r1.materializedSize() + " deferred_until_section=true");     // Catch: Exception -> L14 Throwable -> L60
        monitor-exit(this);
        return r1;
    L43:
        r3 = new CatalogIndex(r1, r2.providers, r2.counts);     // Catch: Exception -> L14 Throwable -> L60
        goto L44
    L21:
        throw e;     // Catch: Exception -> L14 IOException -> L22 Throwable -> L60
    L67:
        r1 = SectionStore.open(this.files, this.modified, this.size);     // Catch: Exception -> L14 IOException -> L25 Throwable -> L60
    L25:
        e = move-exception;
        BlackBox.event("CATALOG_LEGACY_FALLBACK", "requested=true reason=" + e.getClass().getSimpleName());     // Catch: Exception -> L14 Throwable -> L60
        Throwable r4 = null;
        ObjectInputStream r5 = new ObjectInputStream(new BufferedInputStream(new FileInputStream(this.authoritative), 65536));     // Catch: Throwable -> L52
        Object r6 = r5.readObject();     // Catch: Throwable -> L49
        if ((r6 instanceof Catalog) == false) goto L33;
        Catalog r7 = (Catalog) r6;     // Catch: Throwable -> L49
    L35:
        r5.close();     // Catch: Throwable -> L52
        r1 = r7;
        goto L38
    L33:
        if ((r6 instanceof List) == false) goto L48;
        r7 = Catalog.migrate((List) r6);     // Catch: Throwable -> L49
        goto L35
    L48:
        throw new IOException("Catálogo incompatible");     // Catch: Throwable -> L49
    L49:
        th = move-exception;
        r5.close();     // Catch: Throwable -> L52
        throw th;     // Catch: Throwable -> L52
    L52:
        th = move-exception;
        if (0 == 0) goto L57;
        if (null == th) goto L72;
        r4.addSuppressed(th);     // Catch: Exception -> L14 Throwable -> L60
        throw null;     // Catch: Exception -> L14 Throwable -> L60
    L72:
        throw null;     // Catch: Exception -> L14 Throwable -> L60
    L57:
        throw th;     // Catch: Exception -> L14 Throwable -> L60
    L9:
        if (SourceStore.isManifest(this.authoritative) == false) goto L11;
        Directory r8 = SourceStore.open(this);     // Catch: Exception -> L14 IOException -> L16 Throwable -> L60
    L12:
        this.directory = r8;     // Catch: Exception -> L14 IOException -> L16 Throwable -> L60
        goto L20
    L11:
        r8 = new Directory(this);     // Catch: Exception -> L14 IOException -> L16 Throwable -> L60
    L14:
        e = move-exception;
        throw new IllegalStateException("No se pudo abrir la sección del catálogo guardado", e);     // Catch: Throwable -> L60
    }

    List<Entry> select(String r2, String r3, String r4, Integer r5) {
        sectionOpened = true;
        resolve();
        return this.resolvedIndex.select(r2, r3, r4, r5);
    }

    List<Catalog.Source> sources() {
        return new C09834(this);
    }
}
