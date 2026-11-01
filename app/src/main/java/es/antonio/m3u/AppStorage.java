package es.antonio.m3u;

import android.content.Context;
import android.os.Debug;
import android.os.SystemClock;
import android.util.AtomicFile;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes7.dex */
final class AppStorage {
    private static CatalogIndex cachedIndex;
    private static final ConcurrentHashMap<String, Object> fileLocks = null;
    private static final Map<String, Cached> hot = null;
    private static Catalog indexedCatalog;
    private final Context context;

    private static final class Cached {
        final long modified;
        final long size;
        final Object value;

        Cached(File r3, Object r4) {
            this.value = r4;
            this.modified = r3.lastModified();
            this.size = r3.length();
        }
    }

    private static final class ReadMeter extends FilterInputStream {
        long bytes;
        long nanos;

        ReadMeter(InputStream r1) {
            super(r1);
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            long r0 = System.nanoTime();
            int r2 = this.in.read();     // Catch: Throwable -> L8
            if (r2 < 0) goto L6;
            this.bytes++;     // Catch: Throwable -> L8
        L6:
            this.nanos += System.nanoTime() - r0;
            return r2;
        L8:
            th = move-exception;
            this.nanos += System.nanoTime() - r0;
            throw th;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] r5, int r6, int r7) throws IOException {
            long r0 = System.nanoTime();
            int r8 = this.in.read(r5, r6, r7);     // Catch: Throwable -> L8
            if (r8 <= 0) goto L6;
            this.bytes += (long) r8;     // Catch: Throwable -> L8
        L6:
            this.nanos += System.nanoTime() - r0;
            return r8;
        L8:
            th = move-exception;
            this.nanos += System.nanoTime() - r0;
            throw th;
        }
    }

    static {
        final float r1 = 0.75f;
        final boolean r2 = true;
        final int r3 = 4;
        hot = new C09681(r3, r1, r2);
        fileLocks = new ConcurrentHashMap();
    }

    AppStorage(Context r1) {
        this.context = r1;
    }

    private File cacheFile() {
        return new File(this.context.getFilesDir(), "catalog-directory-v185.bin");
    }

    static synchronized Catalog cachedCatalog() {
        monitor-enter(AppStorage.class);
        Catalog r1 = indexedCatalog;     // Catch: Throwable -> L7
        monitor-exit(AppStorage.class);
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    static Object catalogLock(File r0) {
        return lock(r0);
    }

    private boolean deferredReads() {
        return true;
    }

    private static long gcCount() {
        return Long.parseLong(Debug.getRuntimeStat("art.gc.gc-count"));
    L5:
        return 0;
    }

    static synchronized CatalogIndex indexFor(Catalog r4) {
        monitor-enter(AppStorage.class);
    L14:
        th = move-exception;
        throw th;
    L5:
        if (indexedCatalog == r4) goto L11;
        CatalogCache.Prepared r1 = CatalogCache.prepared(r4);     // Catch: Throwable -> L14
        if (r1 != null) goto L9;
        CatalogIndex r2 = new CatalogIndex(r4);     // Catch: Throwable -> L14
    L10:
        cachedIndex = r2;     // Catch: Throwable -> L14
        indexedCatalog = r4;     // Catch: Throwable -> L14
        goto L11
    L9:
        r2 = new CatalogIndex(r4, r1.providers, r1.counts);     // Catch: Throwable -> L14
    L11:
        CatalogIndex r5 = cachedIndex;     // Catch: Throwable -> L14
        monitor-exit(AppStorage.class);
        return r5;
    }

    private File legacyCacheFile() {
        return new File(this.context.getCacheDir(), "catalog-startup-v3.bin");
    }

    private static Object lock(File r2) {
        String r3 = r2.getAbsolutePath();
        Object r0 = fileLocks.get(r3);
        if (r0 == null) goto L5;
        return r0;
    L5:
        Object r1 = new Object();
        Object r4 = fileLocks.putIfAbsent(r3, r1);
        if (r4 != null) goto L9;
        return r1;
    L9:
        return r4;
    }

    static EpgData peekEpg(Context r6) {
        File r0 = new File(r6.getFilesDir(), "epg.dat");
        Map<String, Cached> r7 = hot;
        monitor-enter(r7);
        Cached r1 = hot.get(r0.getAbsolutePath());     // Catch: Throwable -> L18
        if (r1 != null) goto L7;
    L15:
        EpgData r2 = null;
    L16:
        monitor-exit(r7);     // Catch: Throwable -> L18
        return r2;
    L7:
        if ((r1.value instanceof EpgData) == false) goto L15;
        if (r0.exists() == false) goto L15;
        if (r1.modified != r0.lastModified()) goto L15;
        if (r1.size != r0.length()) goto L15;
        r2 = (EpgData) r1.value;     // Catch: Throwable -> L18
    L18:
        th = move-exception;
        throw th;
    }

    private Catalog prepareSections(Catalog r9, File r10) {
        SectionStore.build(this.context.getFilesDir(), r9, r10.lastModified(), r10.length(), indexFor(r9));     // Catch: IOException -> L4
        return SectionStore.open(this.context.getFilesDir(), r10.lastModified(), r10.length());
    L4:
        e = move-exception;
        BlackBox.event("CATALOG_SECTION_SAVE_ERROR", e.getClass().getSimpleName());
        return r9;
    }

    private boolean reusable(String r2) {
        if (r2.equals("catalog.dat") == false) goto L5;
    L8:
        return true;
    L5:
        if (r2.equals("epg.dat") == true) goto L8;
        return false;
    }

    Catalog catalog() throws Exception {
        Object r1 = read("catalog.dat");
        if ((r1 instanceof Catalog) == false) goto L7;
        return (Catalog) r1;
    L7:
        if ((r1 instanceof List) == false) goto L19;
        ArrayList r2 = new ArrayList();
        Iterator r3 = ((List) r1).iterator();
    L10:
        if (r3.hasNext() == false) goto L16;
        Object r4 = r3.next();
        if ((r4 instanceof Entry) == false) goto L15;
        r2.add((Entry) r4);
        goto L10
    L15:
        throw new IOException("Catálogo incompatible");
    L16:
        Catalog r5 = Catalog.migrate(r2);
        write("catalog.dat", r5);
        return r5;
    L19:
        throw new IOException("Catálogo incompatible");
    }

    Object read(String r19) throws Exception {
        StringBuilder r3 = new StringBuilder().append("type=");
        if (r19.equals("catalog.dat") == false) goto L6;
        String r4 = "catalog";
    L9:
        BlackBox.event("STORAGE_READ", r3.append(r4).toString());
        File r5 = new File(this.context.getFilesDir(), r19);
        if (r19.equals("catalog.dat") == false) goto L12;
        SourceStore.recover(r5);
    L12:
        Map<String, Cached> r6 = hot;
        monitor-enter(r6);
        Cached r0 = hot.get(r5.getAbsolutePath());     // Catch: Throwable -> L179
        if (reusable(r19) == false) goto L26;
        if (r0 == null) goto L26;
        if (r5.exists() == false) goto L26;
        if (r0.modified != r5.lastModified()) goto L26;
        if (r0.size != r5.length()) goto L26;
        Object r1 = r0.value;     // Catch: Throwable -> L179
        monitor-exit(r6);     // Catch: Throwable -> L179
        return r1;
    L26:
        monitor-exit(r6);     // Catch: Throwable -> L179
        Object r15 = lock(r5);
        monitor-enter(r15);
        Map<String, Cached> r7 = hot;     // Catch: Throwable -> L173
        monitor-enter(r7);     // Catch: Throwable -> L173
        Cached r2 = hot.get(r5.getAbsolutePath());     // Catch: Throwable -> L167
        if (reusable(r19) == false) goto L44;
        if (r2 == null) goto L44;
        if (r5.exists() == false) goto L44;
        if (r2.modified != r5.lastModified()) goto L44;
        if (r2.size != r5.length()) goto L44;
        Object r8 = r2.value;     // Catch: Throwable -> L167
        monitor-exit(r7);     // Catch: Throwable -> L167
        monitor-exit(r15);     // Catch: Throwable -> L173
        return r8;
    L44:
        monitor-exit(r7);     // Catch: Throwable -> L167
        if (r19.equals("catalog.dat") == false) goto L62;
        if (r5.exists() == false) goto L62;
        if (deferredReads() == false) goto L62;
        long r9 = SystemClock.elapsedRealtime();     // Catch: Throwable -> L173
        Catalog r10 = new Catalog(new DemandCatalog(this.context.getFilesDir(), cacheFile(), r5, legacyCacheFile()));     // Catch: Throwable -> L173
        Map<String, Cached> r11 = hot;     // Catch: Throwable -> L173
        monitor-enter(r11);     // Catch: Throwable -> L173
        hot.put(r5.getAbsolutePath(), new Cached(r5, r10));     // Catch: Throwable -> L58
        monitor-exit(r11);     // Catch: Throwable -> L58
        BlackBox.event("CATALOG_ENGINES_READY", "duration_ms=" + (SystemClock.elapsedRealtime() - r9) + " catalog_payload_bytes=0 created_entries=0 counts_saved=" + r10.demand.knownCounts);     // Catch: Throwable -> L173
        monitor-exit(r15);     // Catch: Throwable -> L173
        return r10;
    L58:
        th = move-exception;
        throw th;     // Catch: Throwable -> L173
    L62:
        if (r19.equals("catalog.dat") == false) goto L119;
        if (r5.exists() == false) goto L119;
        long r12 = SystemClock.elapsedRealtime();     // Catch: IOException -> L75 Throwable -> L173
        Catalog r13 = SectionStore.open(this.context.getFilesDir(), r5.lastModified(), r5.length());     // Catch: IOException -> L75 Throwable -> L173
        Map<String, Cached> r14 = hot;     // Catch: IOException -> L75 Throwable -> L173
        monitor-enter(r14);     // Catch: IOException -> L75 Throwable -> L173
        hot.put(r5.getAbsolutePath(), new Cached(r5, r13));     // Catch: Throwable -> L72
        monitor-exit(r14);     // Catch: Throwable -> L72
        BlackBox.event("CATALOG_CACHE_DECODE", "duration_ms=" + (SystemClock.elapsedRealtime() - r12) + " cache_version=170 entries=" + r13.size() + " created_entries=" + r13.materializedSize());     // Catch: IOException -> L75 Throwable -> L173
        monitor-exit(r15);     // Catch: Throwable -> L173
        return r13;
    L72:
        th = move-exception;
        throw th;     // Catch: IOException -> L75 Throwable -> L173
    L75:
        e = move-exception;
        BlackBox.event("CATALOG_SECTION_INDEX_MISS", "reason=" + e.getClass().getSimpleName());     // Catch: Throwable -> L173
        BufferedInputStream r16 = new BufferedInputStream(new AtomicFile(cacheFile()).openRead(), 65536);     // Catch: IOException -> L114 Throwable -> L173
        long r17 = SystemClock.elapsedRealtime();     // Catch: Throwable -> L103
        long r18 = Debug.threadCpuTimeNanos();     // Catch: Throwable -> L103
        long r110 = gcCount();     // Catch: Throwable -> L103
        ReadMeter r20 = new ReadMeter(r16);     // Catch: Throwable -> L103
        Catalog r21 = prepareSections(CatalogCache.readStartup(r20, r5.lastModified(), r5.length()), r5);     // Catch: Throwable -> L101
        BlackBox.event("CATALOG_CACHE_DECODE", "duration_ms=" + (SystemClock.elapsedRealtime() - r17) + " cpu_ms=" + ((Debug.threadCpuTimeNanos() - r18) / 1000000) + " file_read_ms=" + (r20.nanos / 1000000) + " bytes=" + r20.bytes + " gc_count=" + (gcCount() - r110) + " entries=" + r21.size());     // Catch: Throwable -> L101
        BlackBox.event("CATALOG_CACHE_HIT", "entries=" + r21.size());     // Catch: Throwable -> L101
        CatalogCache.Prepared r22 = CatalogCache.prepared(r21);     // Catch: Throwable -> L101
        if (r22 == null) goto L92;
        monitor-enter(AppStorage.class);     // Catch: Throwable -> L101
        cachedIndex = new CatalogIndex(r21, r22.providers, r22.counts);     // Catch: Throwable -> L88
        indexedCatalog = r21;     // Catch: Throwable -> L88
        BlackBox.event("CATALOG_INDEX_CACHE_HIT", "");     // Catch: Throwable -> L101
    L88:
        th = move-exception;
        throw th;     // Catch: Throwable -> L101
    L92:
        monitor-enter(hot);     // Catch: Throwable -> L101
        hot.put(r5.getAbsolutePath(), new Cached(r5, r21));     // Catch: Throwable -> L98
        r16.close();     // Catch: IOException -> L112 Throwable -> L177
        return r21;
    L98:
        th = move-exception;
        throw th;     // Catch: Throwable -> L101
    L101:
        th = th;
    L105:
        Throwable r23 = th;
        r16.close();     // Catch: Throwable -> L108
        throw r23;     // Catch: IOException -> L112 Throwable -> L177
    L108:
        th = move-exception;
        r23.addSuppressed(th);     // Catch: IOException -> L112 Throwable -> L177
        throw r23;     // Catch: IOException -> L112 Throwable -> L177
    L103:
        th = th;
    L114:
        e = e;
    L116:
        BlackBox.event("CATALOG_CACHE_MISS", "reason=" + e.getClass().getSimpleName());     // Catch: Throwable -> L177
        new AtomicFile(cacheFile()).delete();     // Catch: Throwable -> L177
    L112:
        e = e;
    L119:
        if (r19.equals("epg.dat") == true) goto L121;
    L137:
        ObjectInputStream r24 = new ObjectInputStream(new BufferedInputStream(new AtomicFile(r5).openRead(), 65536));     // Catch: Throwable -> L177
        Object r25 = r24.readObject();     // Catch: Throwable -> L159
        if (r19.equals("catalog.dat") == false) goto L144;
        if ((r25 instanceof Catalog) == false) goto L144;
        r25 = prepareSections((Catalog) r25, r5);     // Catch: Throwable -> L159
    L144:
        if ((r25 instanceof EpgData) == false) goto L147;
        long r26 = SystemClock.elapsedRealtime();     // Catch: Throwable -> L159
        ((EpgData) r25).ensureIndexed();     // Catch: Throwable -> L159
        EpgFastStore.save(this.context.getCacheDir(), r5, (EpgData) r25);     // Catch: Throwable -> L159
        BlackBox.event("EPG_CACHE_READY", "source=legacy programmes=" + ((EpgData) r25).count() + " index_and_migrate_ms=" + (SystemClock.elapsedRealtime() - r26));     // Catch: Throwable -> L159
    L147:
        if (reusable(r19) == false) goto L156;
        Map<String, Cached> r27 = hot;     // Catch: Throwable -> L159
        monitor-enter(r27);     // Catch: Throwable -> L159
        hot.put(r5.getAbsolutePath(), new Cached(r5, r25));     // Catch: Throwable -> L153
        monitor-exit(r27);     // Catch: Throwable -> L153
    L153:
        th = move-exception;
        throw th;     // Catch: Throwable -> L159
    L156:
        r24.close();     // Catch: Throwable -> L177
        monitor-exit(r15);     // Catch: Throwable -> L177
        return r25;
    L159:
        th = move-exception;
        r24.close();     // Catch: Throwable -> L163
    L216:
        throw th;     // Catch: Throwable -> L177
    L163:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L177
        throw th;     // Catch: Throwable -> L177
    L121:
        if (new File(r5.getPath() + ".bak").exists() == true) goto L137;
        long r28 = SystemClock.elapsedRealtime();     // Catch: Throwable -> L177
        EpgData r29 = EpgFastStore.read(this.context.getCacheDir(), r5);     // Catch: Throwable -> L133 IOException -> L135 Throwable -> L177
        r29.ensureIndexed();     // Catch: Throwable -> L133 IOException -> L135 Throwable -> L177
        Map<String, Cached> r30 = hot;     // Catch: Throwable -> L133 IOException -> L135 Throwable -> L177
        monitor-enter(r30);     // Catch: Throwable -> L133 IOException -> L135 Throwable -> L177
        hot.put(r5.getAbsolutePath(), new Cached(r5, r29));     // Catch: Throwable -> L130
        monitor-exit(r30);     // Catch: Throwable -> L130
        BlackBox.event("EPG_CACHE_READY", "source=binary programmes=" + r29.count() + " duration_ms=" + (SystemClock.elapsedRealtime() - r28));     // Catch: Throwable -> L133 IOException -> L135 Throwable -> L177
        monitor-exit(r15);     // Catch: Throwable -> L177
        return r29;
    L130:
        th = move-exception;
        throw th;     // Catch: Throwable -> L133 IOException -> L135 Throwable -> L177
    L133:
        e = move-exception;
        BlackBox.event("EPG_CACHE_FALLBACK", "type=" + e.getClass().getSimpleName());     // Catch: Throwable -> L177
    L167:
        th = th;
    L210:
        throw th;     // Catch: Throwable -> L177
    L171:
        th = th;
    L177:
        th = th;
    L175:
        monitor-exit(r15);     // Catch: Throwable -> L177
        throw th;
    L173:
        th = th;
        r15 = r15;
    L179:
        th = move-exception;
        throw th;
    L6:
        if (r19.equals("epg.dat") == false) goto L8;
        r4 = "epg";
        goto L9
    L8:
        r4 = "other";
        goto L9
    }

    void remove(String r5) {
        File r0 = new File(this.context.getFilesDir(), r5);
        Object r1 = lock(r0);
        monitor-enter(r1);
    L37:
        th = move-exception;
        throw th;
    L5:
        if (r5.equals("epg.dat") == false) goto L8;
        EpgFastStore.remove(this.context.getCacheDir());     // Catch: Throwable -> L37
        EpgChannelFlow.removed(this.context);     // Catch: Throwable -> L37
    L8:
        if (r5.equals("catalog.dat") == true) goto L10;
    L12:
        boolean r2 = false;
    L13:
        new AtomicFile(r0).delete();     // Catch: Throwable -> L37
        if (r5.equals("catalog.dat") == false) goto L17;
        if (r2 == true) goto L17;
        SectionStore.index(this.context.getFilesDir()).delete();     // Catch: Throwable -> L37
        new AtomicFile(cacheFile()).delete();     // Catch: Throwable -> L37
    L17:
        Map<String, Cached> r3 = hot;     // Catch: Throwable -> L37
        monitor-enter(r3);     // Catch: Throwable -> L37
        hot.remove(r0.getAbsolutePath());     // Catch: Throwable -> L34
        monitor-exit(r3);     // Catch: Throwable -> L34
    L22:
        if (r5.equals("catalog.dat") == true) goto L24;
    L32:
        monitor-exit(r1);     // Catch: Throwable -> L37
        return;
    L24:
        monitor-enter(AppStorage.class);     // Catch: Throwable -> L37
        indexedCatalog = null;     // Catch: Throwable -> L29
        cachedIndex = null;     // Catch: Throwable -> L29
        monitor-exit(AppStorage.class);     // Catch: Throwable -> L29
    L29:
        th = move-exception;
        throw th;     // Catch: Throwable -> L37
    L34:
        th = move-exception;
        throw th;     // Catch: Throwable -> L37
    L10:
        if (SourceStore.isManifest(r0) == false) goto L12;
        r2 = true;
        goto L13
    }

    void warmCatalogCache(Catalog r11) {
        if (r11.demand == null) goto L5;
        return;
    L5:
        if (SourceStore.isManifest(new File(this.context.getFilesDir(), "catalog.dat")) == true) goto L58;
        File r2 = new File(this.context.getFilesDir(), "catalog.dat");
        Object r0 = lock(r2);
        monitor-enter(r0);
        Map<String, Cached> r1 = hot;     // Catch: Throwable -> L46
        monitor-enter(r1);     // Catch: Throwable -> L46
        Cached r3 = hot.get(r2.getAbsolutePath());     // Catch: Throwable -> L43
        if (r3 != null) goto L14;
    L40:
        monitor-exit(r1);     // Catch: Throwable -> L43
        monitor-exit(r0);     // Catch: Throwable -> L46
        return;
    L14:
        if (r3.value != r11) goto L40;
        if (r2.exists() == false) goto L40;
        if (r3.modified != r2.lastModified()) goto L40;
        if (r3.size != r2.length()) goto L40;
        monitor-exit(r1);     // Catch: Throwable -> L43
        if (DemandCache.valid(cacheFile(), r2) == false) goto L50;
        monitor-exit(r0);     // Catch: Throwable -> L46
        return;
    L50:
        SectionStore.open(this.context.getFilesDir(), r2.lastModified(), r2.length());     // Catch: IOException -> L29 Throwable -> L46
    L52:
        CatalogIndex r4 = indexFor(r11);     // Catch: IOException -> L36 Throwable -> L46
        DemandCache.write(cacheFile(), r2, r11, r4.providers, r4.counts(), "warm_after_import");     // Catch: IOException -> L36 Throwable -> L46
    L38:
        monitor-exit(r0);     // Catch: Throwable -> L46
        return;
    L36:
        e = move-exception;
        BlackBox.event("CATALOG_DIRECTORY_SAVE_ERROR", e.getClass().getSimpleName());     // Catch: Throwable -> L46
    L54:
        SectionStore.build(this.context.getFilesDir(), r11, r2.lastModified(), r2.length(), indexFor(r11));     // Catch: IOException -> L32 Throwable -> L46
    L32:
        e = move-exception;
        BlackBox.event("CATALOG_SECTION_SAVE_ERROR", e.getClass().getSimpleName());     // Catch: Throwable -> L46
    L43:
        th = move-exception;
        throw th;     // Catch: Throwable -> L46
    L46:
        th = move-exception;
        throw th;
    }

    void write(String r15, Serializable r16) throws IOException {
        File r9 = new File(this.context.getFilesDir(), r15);
        Object r10 = lock(r9);
        monitor-enter(r10);
        FileOutputStream r4 = null;
        if (r15.equals("catalog.dat") == false) goto L28;
        if ((r16 instanceof Catalog) == false) goto L28;
        Catalog r0 = SourceStore.write(this.context.getFilesDir(), r9, (Catalog) r16);     // Catch: Throwable -> L79
        Catalog r2 = (Catalog) r16;     // Catch: Throwable -> L79
        if (r2.demand != null) goto L11;
        Catalog r3 = r0.demand.resolve();     // Catch: Throwable -> L79
        r2.sources.clear();     // Catch: Throwable -> L79
        r2.sources.addAll(r3.sources);     // Catch: Throwable -> L79
        r2.demand = r0.demand;     // Catch: Throwable -> L79
        r2.startupIndex = new CatalogCache.Prepared(r0.demand.providers, r0.demand.counts);     // Catch: Throwable -> L79
    L11:
        r2.mutation = null;     // Catch: Throwable -> L79
        r2.changedSource = null;     // Catch: Throwable -> L79
        r2.changedId = null;     // Catch: Throwable -> L79
        r2.changedName = null;     // Catch: Throwable -> L79
        monitor-enter(AppStorage.class);     // Catch: Throwable -> L79
        indexedCatalog = null;     // Catch: Throwable -> L24
        cachedIndex = null;     // Catch: Throwable -> L24
        monitor-exit(AppStorage.class);     // Catch: Throwable -> L24
        Map<String, Cached> r5 = hot;     // Catch: Throwable -> L79
        monitor-enter(r5);     // Catch: Throwable -> L79
        hot.put(r9.getAbsolutePath(), new Cached(r9, r0));     // Catch: Throwable -> L21
        monitor-exit(r5);     // Catch: Throwable -> L21
        monitor-exit(r10);     // Catch: Throwable -> L79
        return;
    L21:
        th = move-exception;
        throw th;     // Catch: Throwable -> L79
    L24:
        th = move-exception;
        throw th;     // Catch: Throwable -> L79
    L28:
        if (r15.equals("epg.dat") == false) goto L30;
        EpgFastStore.remove(this.context.getCacheDir());     // Catch: Throwable -> L79
    L30:
        AtomicFile r11 = new AtomicFile(r9);     // Catch: Throwable -> L79
        FileOutputStream r12 = r11.startWrite();     // Catch: IOException -> L75 Throwable -> L79
        ObjectOutputStream r6 = new ObjectOutputStream(new BufferedOutputStream(r12, 65536));     // Catch: IOException -> L73 Throwable -> L79
        r6.writeObject(r16);     // Catch: IOException -> L73 Throwable -> L79
        r6.flush();     // Catch: IOException -> L73 Throwable -> L79
        r11.finishWrite(r12);     // Catch: IOException -> L73 Throwable -> L79
        if (r15.equals("catalog.dat") == false) goto L46;
        SectionStore.index(this.context.getFilesDir()).delete();     // Catch: IOException -> L73 Throwable -> L79
        new AtomicFile(cacheFile()).delete();     // Catch: IOException -> L73 Throwable -> L79
        if ((r16 instanceof Catalog) == false) goto L38;
        ((Catalog) r16).startupIndex = null;     // Catch: IOException -> L73 Throwable -> L79
    L38:
        monitor-enter(AppStorage.class);     // Catch: IOException -> L73 Throwable -> L79
        indexedCatalog = null;     // Catch: Throwable -> L42
        cachedIndex = null;     // Catch: Throwable -> L42
        monitor-exit(AppStorage.class);     // Catch: Throwable -> L42
    L42:
        th = move-exception;
        throw th;     // Catch: IOException -> L73 Throwable -> L79
    L46:
        if ((r16 instanceof EpgData) == false) goto L49;
        ((EpgData) r16).ensureIndexed();     // Catch: IOException -> L73 Throwable -> L79
        EpgData r7 = EpgChannelStore.save(this.context, r9, (EpgData) r16, System.currentTimeMillis());     // Catch: IOException -> L73 Throwable -> L79
        EpgChannelFlow.storedReady(this.context, r7);     // Catch: IOException -> L73 Throwable -> L79
        EpgFastStore.save(this.context.getCacheDir(), r9, (EpgData) r16);     // Catch: IOException -> L73 Throwable -> L79
    L49:
        if (reusable(r15) == false) goto L59;
        Map<String, Cached> r8 = hot;     // Catch: IOException -> L73 Throwable -> L79
        monitor-enter(r8);     // Catch: IOException -> L73 Throwable -> L79
        hot.put(r9.getAbsolutePath(), new Cached(r9, r16));     // Catch: Throwable -> L55
        monitor-exit(r8);     // Catch: Throwable -> L55
    L55:
        th = move-exception;
        throw th;     // Catch: IOException -> L73 Throwable -> L79
    L59:
        if (r15.equals("catalog.dat") == true) goto L61;
    L71:
        monitor-exit(r10);     // Catch: Throwable -> L79
        return;
    L61:
        if ((r16 instanceof Catalog) == false) goto L71;
        CatalogIndex r13 = indexFor((Catalog) r16);     // Catch: IOException -> L69 Throwable -> L79
        DemandCatalog.saveHome(this.context.getFilesDir(), cacheFile(), r9, r13.counts(), ((Catalog) r16).size());     // Catch: IOException -> L69 Throwable -> L79
        DemandCache.write(cacheFile(), r9, (Catalog) r16, r13.providers, r13.counts(), "import");     // Catch: IOException -> L65 Throwable -> L79
    L67:
        SectionStore.build(this.context.getFilesDir(), (Catalog) r16, r9.lastModified(), r9.length(), r13);     // Catch: IOException -> L69 Throwable -> L79
    L65:
        e = move-exception;
        BlackBox.event("CATALOG_DIRECTORY_SAVE_ERROR", e.getClass().getSimpleName());     // Catch: IOException -> L69 Throwable -> L79
    L69:
        e = move-exception;
        BlackBox.event("CATALOG_SECTION_SAVE_ERROR", e.getClass().getSimpleName());     // Catch: IOException -> L73 Throwable -> L79
    L73:
        e = e;
        r4 = r12;
    L76:
        if (r4 == null) goto L78;
        r11.failWrite(r4);     // Catch: Throwable -> L79
    L78:
        throw e;     // Catch: Throwable -> L79
    L75:
        e = e;
    L79:
        th = move-exception;
        throw th;
    }
}
