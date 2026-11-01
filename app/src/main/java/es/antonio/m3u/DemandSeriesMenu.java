package es.antonio.m3u;

import androidx.media3.extractor.AacUtil;
import com.arthenica.ffmpegkit.StreamInformation;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.CRC32;

/* JADX INFO: loaded from: classes7.dex */
final class DemandSeriesMenu {
    static final String ALL = "Todas las secciones";
    static final int MAGIC = 1128813332;

    static final class Builder {
        final TreeMap<String, Group> groups;

        Builder() {
            this.groups = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        }

        void add(DemandCatalog.Rows r2, int r3, String r4, String r5, int r6, int r7, String r8) {
            Group r0 = this.groups.get(r4);
            if (r0 != null) goto L5;
            r0 = new Group();
            r0.name = r4;
            r0.rows = r2;
            r0.row = r3;
            this.groups.put(r4, r0);
        L5:
            r0.seasons.add(Integer.valueOf(r6));
            Set<String> r9 = r0.episodes;
            if (r6 < 0) goto L9;
            if (r7 < 0) goto L9;
            StringBuilder r10 = new StringBuilder(String.valueOf(r4)).append("\n").append(r6).append("\n").append(r7);
        L10:
            r9.add(r10.toString());
            return;
        L9:
            r10 = new StringBuilder("Series\n").append(r5).append("\n").append(r8);
            goto L10
        }

        View view(List<String> r11) {
            ArrayList r0 = new ArrayList(this.groups.size());
            Iterator<Group> r1 = this.groups.values().iterator();
        L4:
            if (r1.hasNext() == false) goto L5;
            Group r2 = r1.next();
            r0.add(new Meta(r2.name, r2.rows, r2.row, r2.seasons.size(), r2.episodes.size()));
            goto L4
        L5:
            ArrayList r3 = new ArrayList();
            r3.add(DemandSeriesMenu.ALL);
            r3.addAll(r11);
            return new View(r0, r3);
        }
    }

    static final class Group {
        final Set<String> episodes;
        String name;
        int row;
        DemandCatalog.Rows rows;
        final Set<Integer> seasons;

        Group() {
            this.seasons = new HashSet();
            this.episodes = new HashSet();
        }
    }

    static final class Meta {
        final int episodes;
        final String name;
        final int row;
        final DemandCatalog.Rows rows;
        final int seasons;

        Meta(String r1, DemandCatalog.Rows r2, int r3, int r4, int r5) {
            this.name = r1;
            this.rows = r2;
            this.row = r3;
            this.seasons = r4;
            this.episodes = r5;
        }
    }

    static final class View extends AbstractList<MainActivity.Row> implements DemandRows.View, RandomAccess {
        final List<String> choices;
        final List<Meta> meta;
        final Map<Integer, MainActivity.Row> ready;

        View(List<Meta> r5, List<String> r6) {
            final float r1 = 0.75f;
            final boolean r2 = true;
            final int r3 = 24;
            this.ready = new C09891(this, r3, r1, r2);
            this.meta = r5;
            this.choices = r6;
        }

        @Override // java.util.AbstractList, java.util.List
        public synchronized MainActivity.Row get(int r9) {
            monitor-enter(this);
            MainActivity.Row r0 = this.ready.get(Integer.valueOf(r9));     // Catch: Throwable -> L10
            if (r0 == null) goto L7;
            monitor-exit(this);
            return r0;
        L7:
            Meta r1 = this.meta.get(r9);     // Catch: Throwable -> L10
            MainActivity.Row r7 = new MainActivity.Row(r1.name, String.valueOf(r1.seasons) + " temporadas · " + r1.episodes + " episodios", r1.name, r1.rows.get(r1.row), 1);     // Catch: Throwable -> L10
            this.ready.put(Integer.valueOf(r9), r7);     // Catch: Throwable -> L10
            monitor-exit(this);
            return r7;
        L10:
            th = move-exception;
            throw th;
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.meta.size();
        }
    }

    DemandSeriesMenu() {
    }

    static View build(DemandCatalog r23, String r24) throws IOException {
        Builder r10 = new Builder();
        TreeSet r11 = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        Iterator<Catalog.Source> r12 = r23.directory.catalog.sources.iterator();
    L4:
        if (r12.hasNext() == false) goto L5;
        Catalog.Source r2 = r12.next();
        if (r2.kind == null) goto L14;
        if ("Mixta".equals(r2.kind) == true) goto L14;
        if ("Series".equals(r2.kind) == false) goto L4;
    L14:
        DemandCatalog.Rows r14 = (DemandCatalog.Rows) r2.entries;
        Throwable r15 = null;
        DemandFile r9 = new DemandFile(r14.directory.file, "r");     // Catch: Throwable -> L48
        int r8 = 0;
    L54:
        if (r8 >= r14.count) goto L19;
        int r7 = Math.min(1024, r14.count - r8);     // Catch: Throwable -> L43
        byte[] r3 = new byte[r7 * 72];     // Catch: Throwable -> L43
        r9.seek(r14.records + (((long) r8) * 72));     // Catch: Throwable -> L43
        r9.readFully(r3);     // Catch: Throwable -> L43
        IntBuffer r6 = ByteBuffer.wrap(r3).asIntBuffer();     // Catch: Throwable -> L43
        int r5 = 0;
    L22:
        if (r5 >= r7) goto L23;
        int r4 = r5 * 18;     // Catch: Throwable -> L43
        if ("Series".equals(r14.directory.value(r9, r6.get(r4 + 4))) == true) goto L27;
        DemandFile r19 = r9;
    L42:
        r5 = r5 + 1;
        r6 = r6;
        r8 = r8;
        r9 = r19;
        r7 = r7;
        goto L22
    L27:
        if (r24.equals(r14.directory.value(r9, r6.get(r4 + 17))) == true) goto L29;
        r19 = r9;
        goto L42
    L29:
        String r13 = r14.directory.value(r9, r6.get(r4 + 1));     // Catch: Throwable -> L43
        String r17 = r14.series(r9, r6, r4);     // Catch: Throwable -> L43
        r11.add(r13);     // Catch: Throwable -> L43
        int r18 = r6.get(r4 + 13);     // Catch: Throwable -> L43
        int r110 = r6.get(r4 + 14);     // Catch: Throwable -> L43
        int r20 = r8 + r5;     // Catch: Throwable -> L43
        if (r18 < 0) goto L35;
        if (r110 < 0) goto L35;
        String r21 = null;
    L36:
        r19 = r9;
        r10.add(r14, r20, r17, r13, r18, r110, r21);     // Catch: Throwable -> L39
    L39:
        th = th;
    L45:
        r15 = th;
        r19.close();     // Catch: Throwable -> L48
        throw r15;     // Catch: Throwable -> L48
    L35:
        r21 = r14.directory.value(r9, r6.get(r4));     // Catch: Throwable -> L43
        goto L36
    L23:
        r8 = r8 + 1024;     // Catch: Throwable -> L43
        goto L54
    L19:
        r9.close();     // Catch: Throwable -> L48
    L43:
        th = th;
        r19 = r9;
    L48:
        th = move-exception;
        if (r15 == null) goto L53;
        if (r15 == th) goto L72;
        r15.addSuppressed(th);
        throw r15;
    L72:
        throw r15;
    L53:
        throw th;
    L5:
        View r16 = r10.view(new ArrayList(r11));
        save(r23, r24, r16);
        return r16;
    }

    static boolean eligible(MainActivity.CatalogRequest r2) {
        if ("Series".equals(r2.kind) == true) goto L5;
        return false;
    L5:
        if (r2.provider != null) goto L7;
        return false;
    L7:
        if (r2.series == null) goto L9;
        return false;
    L9:
        if (r2.season == null) goto L11;
        return false;
    L11:
        if (r2.favorites == false) goto L13;
        return false;
    L13:
        if (r2.filter == null) goto L17;
        if (r2.filter.isEmpty() == true) goto L17;
        return false;
    L17:
        if (r2.section != null) goto L19;
        return true;
    L19:
        if (ALL.equals(r2.section) == false) goto L30;
        return true;
    L30:
        return false;
    }

    static File file(DemandCatalog r7, String r8) {
        return new File(folder(r7), String.valueOf(DemandDiskCache.hash(String.valueOf(r8) + "|" + SourceStore.stamp(r7, "Series") + "|" + SourceStore.weight(r7, "Series"))) + ".json");
    }

    static File folder(DemandCatalog r2) {
        return new File(r2.files, "series-menus-v214");
    }

    static /* synthetic */ void lambda$0(byte[] r0, CRC32 r1, OutputStream r2) throws IOException {
        r2.write(r0);
        new DataOutputStream(r2).writeLong(r1.getValue());
    }

    static MainActivity.CatalogPlan prepare(DemandCatalog r8, MainActivity.CatalogRequest r9) {
        if (eligible(r9) == true) goto L5;
        return null;
    L5:
        long r2 = System.nanoTime();
        r8.resolve();
        if (r8.directory != null) goto L29;
        return null;
    L29:
        View r10 = read(r8, r9.provider);     // Catch: IOException -> L10
        String r0 = "disk";
    L16:
        int r4 = r8.created();
        int r1 = 0;
    L18:
        if (r1 >= Math.min(24, r10.size())) goto L19;
        r10.get(r1);
        r1 = r1 + 1;
        goto L18
    L19:
        BlackBox.event("SERIES_PROVIDER_LIGHT", "rows=" + r10.size() + " primed=" + (r8.created() - r4) + " source=" + r0 + " duration_ms=" + ((System.nanoTime() - r2) / 1000000));
        return new MainActivity.CatalogPlan(r10, r10.choices, ALL);
    L11:
        DemandSeriesIndex.get(r8, r9.provider);
        View r3 = read(r8, r9.provider);     // Catch: IOException -> L14
        r0 = StreamInformation.KEY_INDEX;
        r10 = r3;
    L27:
        r10 = build(r8, r9.provider);     // Catch: IOException -> L22
        r0 = "build";
    L22:
        e = move-exception;
        BlackBox.event("SERIES_PROVIDER_FALLBACK", e.getClass().getSimpleName());
        return null;
    }

    static View read(DemandCatalog r14, String r15) throws IOException {
        byte[] r1 = DemandDiskCache.read(file(r14, r15), 16777224);
        if (r1.length < 8) goto L67;
        CRC32 r2 = new CRC32();
        int r5 = 0;
        r2.update(r1, 0, r1.length - 8);
        if (r2.getValue() != ByteBuffer.wrap(r1, r1.length - 8, 8).getLong()) goto L65;
        Throwable r3 = null;
        DataInputStream r4 = new DataInputStream(new ByteArrayInputStream(r1, 0, r1.length - 8));     // Catch: Throwable -> L58
    L54:
        th = move-exception;
        r4.close();     // Catch: Throwable -> L58
        throw th;     // Catch: Throwable -> L58
    L9:
        if (r4.readInt() != MAGIC) goto L83;
        if (r4.readLong() != SourceStore.stamp(r14, "Series")) goto L83;
        if (r4.readLong() != SourceStore.weight(r14, "Series")) goto L83;
        if (r4.readLong() != SourceStore.payloadSize(r14)) goto L83;
        if (r4.readLong() != SourceStore.payloadTime(r14)) goto L83;
        if (r15.equals(r4.readUTF()) == false) goto L83;
        int r16 = DemandCatalog.Directory.bounded(r4.readInt(), AacUtil.AAC_LC_MAX_RATE_BYTES_PER_SECOND);     // Catch: Throwable -> L54
        ArrayList r0 = new ArrayList();     // Catch: Throwable -> L54
        int r6 = 0;
    L21:
        if (r6 >= r16) goto L23;
        r0.add(r4.readUTF());     // Catch: Throwable -> L54
        r6 = r6 + 1;     // Catch: Throwable -> L54
        goto L21
    L23:
        if (r0.isEmpty() == true) goto L82;
        if (ALL.equals(r0.get(0)) == false) goto L82;
        HashMap r17 = new HashMap();     // Catch: Throwable -> L54
        Iterator<Catalog.Source> r18 = r14.directory.catalog.sources.iterator();     // Catch: Throwable -> L54
    L28:
        if (r18.hasNext() == false) goto L29;
        DemandCatalog.Rows r7 = (DemandCatalog.Rows) r18.next().entries;     // Catch: Throwable -> L54
        String r8 = DemandSeriesIndex.key(r7);     // Catch: Throwable -> L54
        if (r17.containsKey(r8) == true) goto L28;
        r17.put(r8, r7);     // Catch: Throwable -> L54
        goto L28
    L29:
        int r19 = DemandCatalog.Directory.bounded(r4.readInt(), 25000);     // Catch: Throwable -> L54
        ArrayList r9 = new ArrayList(r19);     // Catch: Throwable -> L54
        TreeSet r10 = new TreeSet(String.CASE_INSENSITIVE_ORDER);     // Catch: Throwable -> L54
    L30:
        if (r5 >= r19) goto L32;
        String r11 = r4.readUTF();     // Catch: Throwable -> L54
        DemandCatalog.Rows r12 = (DemandCatalog.Rows) r17.get(r4.readUTF());     // Catch: Throwable -> L54
        int r13 = r4.readInt();     // Catch: Throwable -> L54
        int r110 = DemandCatalog.Directory.bounded(r4.readInt(), 10000000);     // Catch: Throwable -> L54
        int r111 = DemandCatalog.Directory.bounded(r4.readInt(), 10000000);     // Catch: Throwable -> L54
        if (r12 == null) goto L81;
        if (r13 < 0) goto L81;
        if (r13 >= r12.count) goto L81;
        if (r10.add(r11) == false) goto L81;
        r9.add(new Meta(r11, r12, r13, r110, r111));     // Catch: Throwable -> L54
        r5 = r5 + 1;     // Catch: Throwable -> L54
    L81:
        throw new IOException("Series menu row");     // Catch: Throwable -> L54
    L32:
        if (r4.read() != (-1)) goto L37;
        View r112 = new View(r9, r0);     // Catch: Throwable -> L54
        r4.close();     // Catch: Throwable -> L58
        return r112;
    L37:
        throw new IOException("Series menu trailing data");     // Catch: Throwable -> L54
    L82:
        throw new IOException("Series menu choices");     // Catch: Throwable -> L54
    L83:
        throw new IOException("Series menu generation");     // Catch: Throwable -> L54
    L58:
        th = move-exception;
        if (0 == 0) goto L84;
        if (null == th) goto L63;
        r3.addSuppressed(th);
    L63:
        throw null;
    L84:
        throw th;
    L65:
        throw new IOException("Series menu checksum");
    L67:
        throw new IOException("Series menu length");
    }

    static void save(DemandCatalog r7, String r8, View r9) {
        if (r9.size() <= 25000) goto L20;
        return;
    L20:
        ByteArrayOutputStream r1 = new ByteArrayOutputStream();     // Catch: IOException -> L18
        DataOutputStream r2 = new DataOutputStream(r1);     // Catch: IOException -> L18
        r2.writeInt(MAGIC);     // Catch: IOException -> L18
        r2.writeLong(SourceStore.stamp(r7, "Series"));     // Catch: IOException -> L18
        r2.writeLong(SourceStore.weight(r7, "Series"));     // Catch: IOException -> L18
        r2.writeLong(SourceStore.payloadSize(r7));     // Catch: IOException -> L18
        r2.writeLong(SourceStore.payloadTime(r7));     // Catch: IOException -> L18
        r2.writeUTF(r8);     // Catch: IOException -> L18
        r2.writeInt(r9.choices.size());     // Catch: IOException -> L18
        Iterator<String> r0 = r9.choices.iterator();     // Catch: IOException -> L18
    L7:
        if (r0.hasNext() == false) goto L8;
        r2.writeUTF(r0.next());     // Catch: IOException -> L18
        goto L7
    L8:
        r2.writeInt(r9.size());     // Catch: IOException -> L18
        Iterator<Meta> r10 = r9.meta.iterator();     // Catch: IOException -> L18
    L10:
        if (r10.hasNext() == false) goto L11;
        Meta r3 = r10.next();     // Catch: IOException -> L18
        r2.writeUTF(r3.name);     // Catch: IOException -> L18
        r2.writeUTF(DemandSeriesIndex.key(r3.rows));     // Catch: IOException -> L18
        r2.writeInt(r3.row);     // Catch: IOException -> L18
        r2.writeInt(r3.seasons);     // Catch: IOException -> L18
        r2.writeInt(r3.episodes);     // Catch: IOException -> L18
        goto L10
    L11:
        r2.flush();     // Catch: IOException -> L18
        final byte[] r11 = r1.toByteArray();     // Catch: IOException -> L18
        if (r11.length <= 16777216) goto L14;
        return;
    L14:
        final CRC32 r4 = new CRC32();     // Catch: IOException -> L18
        r4.update(r11);     // Catch: IOException -> L18
        File r5 = file(r7, r8);     // Catch: IOException -> L18
        DemandDiskCache.put(folder(r7), r5, new DemandSeriesMenu$$ExternalSyntheticLambda0(r11, r4), 67108864, 128);     // Catch: IOException -> L18
        return;
    }
}
