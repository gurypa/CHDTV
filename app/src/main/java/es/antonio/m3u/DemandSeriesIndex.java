package es.antonio.m3u;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes7.dex */
final class DemandSeriesIndex {
    static final int MAGIC = 1128813367;
    static final int MAX = 200000;
    static final Map<DemandCatalog, LinkedHashMap<String, Index>> hot = null;

    static final class Index {
        List<String> choices;
        int count;
        long data;
        File file;
        String provider;
        long size;
        List<String> sources;
        Map<String, Span> spans;
        long stamp;
        long time;
        long weight;

        Index() {
            this.sources = new ArrayList();
            this.choices = new ArrayList();
            this.spans = new HashMap();
        }

        boolean valid(DemandCatalog r6) {
            if (this.stamp == SourceStore.stamp(r6, "Series")) goto L5;
            return false;
        L5:
            if (this.weight == SourceStore.weight(r6, "Series")) goto L7;
            return false;
        L7:
            if (this.size == SourceStore.payloadSize(r6)) goto L9;
            return false;
        L9:
            if (this.time == SourceStore.payloadTime(r6)) goto L11;
            return false;
        L11:
            if (this.file.isFile() == false) goto L19;
            return true;
        L19:
            return false;
        }
    }

    static final class Ints {
        int[] data;
        int size;

        Ints() {
            this.data = new int[16];
        }

        void add(int r4) {
            if (this.size != this.data.length) goto L5;
            this.data = Arrays.copyOf(this.data, this.size * 2);
        L5:
            int[] r0 = this.data;
            int r1 = this.size;
            this.size = r1 + 1;
            r0[r1] = r4;
        }
    }

    static final class Span {
        final int count;
        final int first;

        Span(int r1, int r2) {
            this.first = r1;
            this.count = r2;
        }
    }

    static {
        hot = new WeakHashMap();
    }

    DemandSeriesIndex() {
    }

    static byte[] block(DemandCatalog.Rows r8, DemandFile r9, int r10, int r11, int[] r12) throws IOException {
        byte[] r0 = new byte[r11 * 72];
        if (r12 != null) goto L5;
    L11:
        if (r12 == null) goto L14;
        r10 = r12[r10];
    L14:
        r9.seek(r8.records + (((long) r10) * 72));
        r9.readFully(r0);
    L15:
        return r0;
    L5:
        if ((r12[(r10 + r11) - 1] - r12[r10]) == (r11 - 1)) goto L11;
        int r3 = 0;
    L8:
        if (r3 >= r11) goto L15;
        r9.seek(r8.records + (((long) r12[r10 + r3]) * 72));
        r9.readFully(r0, r3 * 72, 72);
        r3 = r3 + 1;
        goto L8
    }

    static int bounded(int r0, int r1) throws IOException {
        if (r0 < 0) goto L6;
        if (r0 > r1) goto L6;
        return r0;
    L6:
        throw new IOException("Series index bounds");
    }

    static void build(DemandCatalog r25, String r26) throws IOException {
        DemandCatalog r0 = r25;
        String r1 = r26;
        ArrayList r2 = new ArrayList();
        TreeSet r3 = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        TreeMap r4 = new TreeMap();
        DemandSeriesMenu.Builder r13 = new DemandSeriesMenu.Builder();
        Iterator<Catalog.Source> r14 = r0.directory.catalog.sources.iterator();
        int r5 = 0;
    L3:
        Throwable r16 = null;
        String r12 = "Series";
        if (r14.hasNext() == false) goto L5;
        Catalog.Source r6 = r14.next();
        DemandCatalog.Rows r11 = (DemandCatalog.Rows) r6.entries;
        int r10 = r2.size();
        r2.add(key(r11));
        if (r6.kind != null) goto L59;
    L123:
        DemandFile r9 = new DemandFile(r11.directory.file, "r");     // Catch: Throwable -> L106
        int r8 = 0;
    L117:
        if (r8 >= r11.count) goto L67;
        int r7 = Math.min(1024, r11.count - r8);     // Catch: Throwable -> L101
        byte[] r15 = new byte[r7 * 72];     // Catch: Throwable -> L101
        Iterator<Catalog.Source> r17 = r14;
        TreeMap r18 = r4;
        r9.seek(r11.records + (((long) r8) * 72));     // Catch: Throwable -> L101
        r9.readFully(r15);     // Catch: Throwable -> L101
        IntBuffer r19 = ByteBuffer.wrap(r15).asIntBuffer();     // Catch: Throwable -> L101
        r5 = r5;
        int r110 = 0;
    L70:
        if (r110 >= r7) goto L71;
        int r20 = r110 * 18;     // Catch: Throwable -> L101
        if (r12.equals(r11.directory.value(r9, r19.get(r20 + 4))) == true) goto L75;
        DemandFile r24 = r9;
        TreeMap r111 = r18;
    L100:
        r110 = r110 + 1;
        r10 = r10;
        r11 = r11;
        r8 = r8;
        r2 = r2;
        r12 = r12;
        r3 = r3;
        r7 = r7;
        r9 = r24;
        r0 = r25;
        r1 = r26;
        r18 = r111;
        goto L70
    L75:
        if (r1.equals(r11.directory.value(r9, r19.get(r20 + 17))) == true) goto L77;
        r24 = r9;
        r111 = r18;
        goto L100
    L77:
        int r21 = r5 + 1;     // Catch: Throwable -> L101
        if (r21 > MAX) goto L95;
        int r112 = r20 + 1;     // Catch: Throwable -> L101
        r3.add(r11.directory.value(r9, r19.get(r112)));     // Catch: Throwable -> L101
        String r22 = r11.series(r9, r19, r20);     // Catch: Throwable -> L101
        TreeMap r23 = r18;
        Ints r113 = (Ints) r23.get(r22);     // Catch: Throwable -> L101
        if (r113 == null) goto L113;
        Ints r27 = r113;
    L86:
        r27.add(r10);     // Catch: Throwable -> L101
        int r28 = r8 + r110;     // Catch: Throwable -> L101
        r27.add(r28);     // Catch: Throwable -> L101
        String r29 = r11.directory.value(r9, r19.get(r112));     // Catch: Throwable -> L101
        int r114 = r20 + 13;     // Catch: Throwable -> L101
        int r210 = r19.get(r114);     // Catch: Throwable -> L101
        int r30 = r20 + 14;     // Catch: Throwable -> L101
        int r211 = r19.get(r30);     // Catch: Throwable -> L101
        if (r19.get(r114) >= 0) goto L89;
    L92:
        String r31 = r11.directory.value(r9, r19.get(r20));     // Catch: Throwable -> L101
    L93:
        r111 = r23;
        r24 = r9;
        r13.add(r11, r28, r22, r29, r210, r211, r31);     // Catch: Throwable -> L97
        r5 = r21;
    L97:
        th = th;
    L103:
        r16 = th;
    L104:
        r24.close();     // Catch: Throwable -> L106
        throw r16;     // Catch: Throwable -> L106
    L89:
        if (r19.get(r30) < 0) goto L92;
        r31 = null;
        goto L93
    L113:
        r27 = new Ints();     // Catch: Throwable -> L83
        r23.put(r22, r27);     // Catch: Throwable -> L83
    L83:
        th = move-exception;
        r16 = th;
        r24 = r9;
        goto L104
    L95:
        r24 = r9;
        throw new IOException("Series rows limit");     // Catch: Throwable -> L97
    L71:
        r8 = r8 + 1024;     // Catch: Throwable -> L101
        r14 = r17;
        r4 = r18;
        goto L117
    L67:
        r9.close();     // Catch: Throwable -> L106
    L101:
        th = th;
        r24 = r9;
    L106:
        th = move-exception;
        Throwable r32 = r16;
        if (r32 == null) goto L112;
        if (r32 == th) goto L144;
        r32.addSuppressed(th);
        throw r32;
    L144:
        throw r32;
    L112:
        throw th;
    L59:
        if ("Mixta".equals(r6.kind) == true) goto L123;
        if ("Series".equals(r6.kind) == true) goto L123;
    L5:
        File r33 = file(r25, r26);
        File r34 = new File(String.valueOf(r33.getPath()) + ".tmp");
        DataOutputStream r35 = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(r34)));     // Catch: Throwable -> L44
        r35.writeInt(MAGIC);     // Catch: Throwable -> L40
        r35.writeLong(SourceStore.stamp(r0, "Series"));     // Catch: Throwable -> L40
        r35.writeLong(SourceStore.weight(r0, "Series"));     // Catch: Throwable -> L40
        r35.writeLong(SourceStore.payloadSize(r25));     // Catch: Throwable -> L40
        r35.writeLong(SourceStore.payloadTime(r25));     // Catch: Throwable -> L40
        r35.writeUTF(r1);     // Catch: Throwable -> L40
        r35.writeInt(r2.size());     // Catch: Throwable -> L40
        Iterator r36 = r2.iterator();     // Catch: Throwable -> L40
    L10:
        if (r36.hasNext() == false) goto L11;
        r35.writeUTF((String) r36.next());     // Catch: Throwable -> L40
        goto L10
    L11:
        r35.writeInt(r3.size() + 1);     // Catch: Throwable -> L40
        r35.writeUTF("Todas las secciones");     // Catch: Throwable -> L40
        Iterator r37 = r3.iterator();     // Catch: Throwable -> L40
    L13:
        if (r37.hasNext() == false) goto L14;
        r35.writeUTF((String) r37.next());     // Catch: Throwable -> L40
        goto L13
    L14:
        r35.writeInt(r4.size());     // Catch: Throwable -> L40
        Iterator r38 = r4.entrySet().iterator();     // Catch: Throwable -> L40
    L16:
        if (r38.hasNext() == false) goto L17;
        Map.Entry r39 = (Map.Entry) r38.next();     // Catch: Throwable -> L40
        r35.writeUTF((String) r39.getKey());     // Catch: Throwable -> L40
        r35.writeInt(((Ints) r39.getValue()).size / 2);     // Catch: Throwable -> L40
        goto L16
    L17:
        Iterator r40 = r4.values().iterator();     // Catch: Throwable -> L40
    L18:
        if (r40.hasNext() == false) goto L20;
        Ints r41 = (Ints) r40.next();     // Catch: Throwable -> L40
        int r42 = 0;
    L33:
        if (r42 >= r41.size) goto L18;
        r35.writeInt(r41.data[r42]);     // Catch: Throwable -> L40
        r42 = r42 + 1;     // Catch: Throwable -> L40
        goto L33
    L20:
        r35.close();     // Catch: Throwable -> L44
        if (r34.length() > 33554432) goto L143;
        if (r34.renameTo(r33) == false) goto L143;
        DemandSeriesMenu.save(r0, r1, r13.view(new ArrayList(r3)));     // Catch: Throwable -> L27
        return;
    L27:
        th = th;
        boolean r115 = true;
    L53:
        if (r115 == true) goto L55;
        r34.delete();
    L55:
        throw th;
    L143:
        throw new IOException("Series index commit");     // Catch: Throwable -> L51
    L40:
        th = move-exception;
        r35.close();     // Catch: Throwable -> L44
        throw th;     // Catch: Throwable -> L44
    L51:
        th = th;
        r115 = false;
    L44:
        th = move-exception;
        if (0 == 0) goto L50;
        if (null == th) goto L141;
        r16.addSuppressed(th);     // Catch: Throwable -> L51
        throw null;     // Catch: Throwable -> L51
    L141:
        throw null;     // Catch: Throwable -> L51
    L50:
        throw th;     // Catch: Throwable -> L51
    }

    static File file(DemandCatalog r7, String r8) {
        return new File(r7.files, "series-locators-v208-" + DemandDiskCache.hash(String.valueOf(r8) + "|" + SourceStore.stamp(r7, "Series") + "|" + SourceStore.weight(r7, "Series")) + ".bin");
    }

    static synchronized Index get(DemandCatalog r9, String r10) {
        monitor-enter(DemandSeriesIndex.class);
        if (r10 != null) goto L40;
    L6:
        monitor-exit(DemandSeriesIndex.class);
        return null;
    L40:
        LinkedHashMap<String, Index> r2 = hot.get(r9);     // Catch: Throwable -> L35
        if (r2 != null) goto L11;
        r2 = new LinkedHashMap(4, 0.75f, true);     // Catch: Throwable -> L35
        hot.put(r9, r2);     // Catch: Throwable -> L35
    L11:
        Index r4 = r2.get(r10);     // Catch: Throwable -> L35
        if (r4 != null) goto L14;
    L17:
        long r5 = System.nanoTime();     // Catch: Throwable -> L35
        String r6 = "disk";
        Index r11 = read(r9, r10);     // Catch: RuntimeException -> L20 IOException -> L22 Throwable -> L35
    L24:
        r2.put(r10, r11);     // Catch: RuntimeException -> L20 IOException -> L32 Throwable -> L35
    L26:
        if (r2.size() <= 4) goto L27;
        r2.remove(r2.keySet().iterator().next());     // Catch: RuntimeException -> L20 IOException -> L32 Throwable -> L35
        goto L26
    L27:
        BlackBox.event("SERIES_LOCATORS_READY", "source=" + r6 + " series=" + r11.spans.size() + " rows=" + r11.count + " duration_ms=" + ((System.nanoTime() - r5) / 1000000) + " created_entries=0");     // Catch: RuntimeException -> L20 IOException -> L32 Throwable -> L35
        monitor-exit(DemandSeriesIndex.class);
        return r11;
    L32:
        e = e;
    L33:
        r2.remove(r10);     // Catch: Throwable -> L35
        BlackBox.event("SERIES_LOCATORS_FALLBACK", "type=" + e.getClass().getSimpleName());     // Catch: Throwable -> L35
    L20:
        e = e;
    L41:
        r6 = "build";
        build(r9, r10);     // Catch: RuntimeException -> L20 IOException -> L32 Throwable -> L35
        r11 = read(r9, r10);     // Catch: RuntimeException -> L20 IOException -> L32 Throwable -> L35
        goto L24
    L14:
        if (r4.valid(r9) == false) goto L17;
        monitor-exit(DemandSeriesIndex.class);
        return r4;
    L35:
        th = move-exception;
        throw th;
    }

    static String key(DemandCatalog.Rows r3) {
        return String.valueOf(r3.directory.file.getName()) + ":" + r3.records;
    }

    static Index read(DemandCatalog r9, String r10) throws IOException {
        Index r0 = new Index();
        r0.file = file(r9, r10);
        if (r0.file.length() > 33554432) goto L52;
        Throwable r1 = null;
        DemandFile r2 = new DemandFile(r0.file, "r");     // Catch: Throwable -> L45
    L41:
        th = move-exception;
        r2.close();     // Catch: Throwable -> L45
        throw th;     // Catch: Throwable -> L45
    L7:
        if (r2.readInt() != MAGIC) goto L64;
        r0.stamp = r2.readLong();     // Catch: Throwable -> L41
        r0.weight = r2.readLong();     // Catch: Throwable -> L41
        r0.size = r2.readLong();     // Catch: Throwable -> L41
        r0.time = r2.readLong();     // Catch: Throwable -> L41
        r0.provider = r2.readUTF();     // Catch: Throwable -> L41
        if (r10.equals(r0.provider) == false) goto L63;
        if (r0.valid(r9) == false) goto L63;
        int r11 = bounded(r2.readInt(), 4096);     // Catch: Throwable -> L41
        int r12 = 0;
        int r3 = 0;
    L13:
        if (r3 >= r11) goto L14;
        r0.sources.add(r2.readUTF());     // Catch: Throwable -> L41
        r3 = r3 + 1;     // Catch: Throwable -> L41
        goto L13
    L14:
        int r13 = bounded(r2.readInt(), MAX);     // Catch: Throwable -> L41
        int r4 = 0;
    L15:
        if (r4 >= r13) goto L17;
        r0.choices.add(r2.readUTF());     // Catch: Throwable -> L41
        r4 = r4 + 1;     // Catch: Throwable -> L41
        goto L15
    L17:
        if (r0.choices.isEmpty() == true) goto L62;
        if ("Todas las secciones".equals(r0.choices.get(0)) == false) goto L62;
        int r14 = bounded(r2.readInt(), MAX);     // Catch: Throwable -> L41
    L21:
        if (r12 >= r14) goto L22;
        String r5 = r2.readUTF();     // Catch: Throwable -> L41
        int r6 = bounded(r2.readInt(), MAX - r0.count);     // Catch: Throwable -> L41
        if (r6 == 0) goto L61;
        if (r0.spans.put(r5, new Span(r0.count, r6)) != null) goto L61;
        r0.count += r6;     // Catch: Throwable -> L41
        r12 = r12 + 1;     // Catch: Throwable -> L41
    L61:
        throw new IOException("Series span");     // Catch: Throwable -> L41
    L22:
        r0.data = r2.getFilePointer();     // Catch: Throwable -> L41
        if (r2.length() != (r0.data + (((long) r0.count) * 8))) goto L28;
        r2.close();     // Catch: Throwable -> L45
        return r0;
    L28:
        throw new IOException("Series index length");     // Catch: Throwable -> L41
    L62:
        throw new IOException("Series categories");     // Catch: Throwable -> L41
    L63:
        throw new IOException("Series index fingerprint");     // Catch: Throwable -> L41
    L64:
        throw new IOException("Series index version");     // Catch: Throwable -> L41
    L45:
        th = move-exception;
        if (0 == 0) goto L65;
        if (null == th) goto L50;
        r1.addSuppressed(th);
    L50:
        throw null;
    L65:
        throw th;
    L52:
        throw new IOException("Series index size");
    }

    static Map<DemandCatalog.Rows, int[]> select(DemandCatalog r10, Index r11, String r12) throws IOException {
        IdentityHashMap r0 = new IdentityHashMap();
        HashMap r1 = new HashMap();
        Iterator<Catalog.Source> r13 = r10.directory.catalog.sources.iterator();
    L4:
        if (r13.hasNext() == false) goto L5;
        DemandCatalog.Rows r2 = (DemandCatalog.Rows) r13.next().entries;
        r1.put(key(r2), r2);
        goto L4
    L5:
        Span r14 = r11.spans.get(r12);
        if (r14 != null) goto L8;
        return r0;
    L8:
        IdentityHashMap r15 = new IdentityHashMap();
        Throwable r3 = null;
        DemandFile r4 = new DemandFile(r11.file, "r");     // Catch: Throwable -> L41
        r4.seek(r11.data + (((long) r14.first) * 8));     // Catch: Throwable -> L37
        int r5 = 0;
    L12:
        if (r5 >= r14.count) goto L13;
        int r6 = r4.readInt();     // Catch: Throwable -> L37
        int r7 = r4.readInt();     // Catch: Throwable -> L37
        if (r6 < 0) goto L61;
        if (r6 >= r11.sources.size()) goto L61;
        DemandCatalog.Rows r8 = (DemandCatalog.Rows) r1.get(r11.sources.get(r6));     // Catch: Throwable -> L37
        if (r8 == null) goto L34;
        if (r7 < 0) goto L34;
        if (r7 >= r8.count) goto L34;
        Ints r9 = (Ints) r15.get(r8);     // Catch: Throwable -> L37
        if (r9 != null) goto L32;
        r9 = new Ints();     // Catch: Throwable -> L37
        r15.put(r8, r9);     // Catch: Throwable -> L37
    L32:
        r9.add(r7);     // Catch: Throwable -> L37
        r5 = r5 + 1;     // Catch: Throwable -> L37
    L34:
        throw new IOException("Series row");     // Catch: Throwable -> L37
    L61:
        throw new IOException("Series source");     // Catch: Throwable -> L37
    L13:
        r4.close();     // Catch: Throwable -> L41
        Iterator r16 = r15.entrySet().iterator();
    L16:
        if (r16.hasNext() == false) goto L17;
        Map.Entry r17 = (Map.Entry) r16.next();
        int[] r18 = Arrays.copyOf(((Ints) r17.getValue()).data, ((Ints) r17.getValue()).size);
        Arrays.sort(r18);
        r0.put((DemandCatalog.Rows) r17.getKey(), r18);
        goto L16
    L17:
        BlackBox.event("SERIES_LOCATORS_SELECT", "rows=" + r14.count + " catalogue_rows_scanned=0");
        return r0;
    L37:
        th = move-exception;
        r4.close();     // Catch: Throwable -> L41
        throw th;     // Catch: Throwable -> L41
    L41:
        th = move-exception;
        if (0 == 0) goto L62;
        if (null == th) goto L46;
        r3.addSuppressed(th);
    L46:
        throw null;
    L62:
        throw th;
    }
}
