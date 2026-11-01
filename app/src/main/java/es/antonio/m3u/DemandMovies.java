package es.antonio.m3u;

import androidx.media3.extractor.AacUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes7.dex */
final class DemandMovies {
    static long sourceBindings;

    static final class MovieEntries extends AbstractList<Entry> implements RandomAccess {
        final Snapshot snapshot;

        MovieEntries(Snapshot r1) {
            this.snapshot = r1;
        }

        int find(Entry r5) {
            int r0 = 0;
        L4:
            if (r0 >= size()) goto L5;
            Ref r1 = this.snapshot.refs.get(r0);
            if (Objects.equals(r1.title, r5.title) == false) goto L14;
            if (Objects.equals(r1.group, r5.group) == false) goto L14;
            if (PlaybackSequence.same(r1.entry(), r5) == false) goto L14;
            return r0;
        L14:
            r0 = r0 + 1;
            goto L4
        L5:
            return -1;
        }

        @Override // java.util.AbstractList, java.util.List
        public Entry get(int r2) {
            return this.snapshot.refs.get(r2).entry();
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.snapshot.refs.size();
        }
    }

    static final class MovieRows extends AbstractList<MainActivity.Row> implements DemandRows.View, RandomAccess {
        final Map<Integer, MainActivity.Row> ready;
        final List<Ref> refs;

        MovieRows(List<Ref> r5) {
            final float r1 = 0.75f;
            final boolean r2 = true;
            final int r3 = 24;
            this.ready = new C09881(this, r3, r1, r2);
            this.refs = r5;
        }

        @Override // java.util.AbstractList, java.util.List
        public synchronized MainActivity.Row get(int r9) {
            monitor-enter(this);
            MainActivity.Row r0 = this.ready.get(Integer.valueOf(r9));     // Catch: Throwable -> L9
            if (r0 != null) goto L7;
            Ref r1 = this.refs.get(r9);     // Catch: Throwable -> L9
            MainActivity.Row r7 = new MainActivity.Row(r1.title, r1.group, null, r1.entry(), 0);     // Catch: Throwable -> L9
            this.ready.put(Integer.valueOf(r9), r7);     // Catch: Throwable -> L9
            r0 = r7;
        L7:
            monitor-exit(this);
            return r0;
        L9:
            th = move-exception;
            throw th;
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.refs.size();
        }
    }

    static final class Ref {
        final int episode;
        final String group;
        final int row;
        final DemandCatalog.Rows rows;
        final int season;
        final String title;

        Ref(DemandCatalog.Rows r1, int r2, String r3, String r4, int r5, int r6) {
            this.rows = r1;
            this.row = r2;
            this.title = r3;
            this.group = r4;
            this.season = r5;
            this.episode = r6;
        }

        Entry entry() {
            return this.rows.get(this.row);
        }
    }

    static final class Snapshot {
        final List<String> choices;
        final String provider;
        final List<Ref> refs;

        Snapshot(String r1, List<Ref> r2, List<String> r3) {
            this.provider = r1;
            this.refs = r2;
            this.choices = r3;
        }
    }

    DemandMovies() {
    }

    static Snapshot build(DemandCatalog r20, String r21) {
        ArrayList r1 = new ArrayList();
        TreeSet r2 = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        Iterator<Catalog.Source> r3 = r20.directory.catalog.sources.iterator();
    L4:
        if (r3.hasNext() == false) goto L5;
        Catalog.Source r4 = r3.next();
        if (r4.kind == null) goto L14;
        if ("Mixta".equals(r4.kind) == true) goto L14;
        if ("Películas".equals(r4.kind) == false) goto L4;
    L14:
        DemandCatalog.Rows r5 = (DemandCatalog.Rows) r4.entries;
        DemandFile r14 = new DemandFile(r5.directory.file, "r");     // Catch: Throwable -> L39
        int r13 = 0;
    L55:
        if (r13 >= r5.count) goto L19;
        int r12 = Math.min(1024, r5.count - r13);     // Catch: Throwable -> L33
        byte[] r7 = new byte[r12 * 72];     // Catch: Throwable -> L33
        r14.seek(r5.records + (((long) r13) * 72));     // Catch: Throwable -> L33
        r14.readFully(r7);     // Catch: Throwable -> L33
        IntBuffer r11 = ByteBuffer.wrap(r7).asIntBuffer();     // Catch: Throwable -> L33
        int r10 = 0;
    L22:
        if (r10 >= r12) goto L23;
        int r8 = r10 * 18;     // Catch: Throwable -> L33
        if ("Películas".equals(r5.directory.value(r14, r11.get(r8 + 4))) == false) goto L32;
        if (r21.equals(r5.directory.value(r14, r11.get(r8 + 17))) == false) goto L32;
        String r16 = r5.directory.value(r14, r11.get(r8));     // Catch: Throwable -> L33
        String r9 = r5.directory.value(r14, r11.get(r8 + 1));     // Catch: Throwable -> L33
        r2.add(r9);     // Catch: Throwable -> L33
        r1.add(new Ref(r5, r13 + r10, r16, r9, r11.get(r8 + 13), r11.get(r8 + 14)));     // Catch: Throwable -> L33
    L32:
        r10 = r10 + 1;
        r13 = r13;
        r11 = r11;
        r12 = r12;
        goto L22
    L23:
        r13 = r13 + 1024;     // Catch: Throwable -> L33
        goto L55
    L19:
        r14.close();     // Catch: Throwable -> L39
    L33:
        th = move-exception;
        Throwable r6 = th;
        r14.close();     // Catch: Throwable -> L37
        throw r6;     // Catch: Throwable -> L37
    L37:
        th = th;
    L41:
        if (r6 == null) goto L45;
        if (r6 == th) goto L69;
        r6.addSuppressed(th);     // Catch: IOException -> L46
        throw r6;     // Catch: IOException -> L46
    L46:
        e = move-exception;
        throw new IllegalStateException(e);
    L69:
        throw r6;     // Catch: IOException -> L46
    L45:
        throw th;     // Catch: IOException -> L46
    L39:
        th = th;
        r6 = null;
        goto L41
    L5:
        Collections.sort(r1, new DemandMovies$$ExternalSyntheticLambda0());
        ArrayList r15 = new ArrayList();
        r15.add("Todas las secciones");
        r15.addAll(r2);
        return new Snapshot(r21, r1, r15);
    }

    static File file(DemandCatalog r3, String r4) {
        return new File(r3.files, "movies-index-v184-" + DemandDiskCache.hash(r4) + ".bin");
    }

    static /* synthetic */ int lambda$0(Ref r3, Ref r4) {
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

    static PlaybackSequence playback(DemandCatalog r4, Entry r5) {
        r4.resolve();
        if (r4.directory != null) goto L6;
        return null;
    L6:
        monitor-enter(DemandMovies.class);
        ArrayList r2 = new ArrayList(r4.movieViews.values());     // Catch: Throwable -> L30
        monitor-exit(DemandMovies.class);     // Catch: Throwable -> L30
        Iterator r3 = r2.iterator();
    L11:
        if (r3.hasNext() == false) goto L12;
        MovieEntries r6 = new MovieEntries((Snapshot) r3.next());
        int r0 = r6.find(r5);
        if (r0 < 0) goto L11;
        return PlaybackSequence.fromOrdered(r6, r0);
    L12:
        Iterator<Catalog.Source> r1 = r4.directory.catalog.sources.iterator();
    L14:
        if (r1.hasNext() == false) goto L15;
        Entry r7 = ((DemandCatalog.Rows) r1.next().entries).find(r5, null, true);
        if (r7 == null) goto L14;
        String r8 = r4.directory.providers.get(r7);
        if (r8 == null) goto L14;
        MovieEntries r9 = new MovieEntries(snapshot(r4, r8));
        int r10 = r9.find(r5);
        if (r10 < 0) goto L14;
        return PlaybackSequence.fromOrdered(r9, r10);
    L15:
        return null;
    L30:
        th = move-exception;
        throw th;
    }

    static MainActivity.CatalogPlan prepare(DemandCatalog r11, MainActivity.CatalogRequest r12) {
        if ("Películas".equals(r12.kind) == true) goto L5;
    L41:
        return null;
    L5:
        if (r12.provider == null) goto L41;
        if (r12.series != null) goto L41;
        long r2 = System.nanoTime();
        Snapshot r13 = snapshot(r11, r12.provider);
        if (r13 != null) goto L12;
        return null;
    L12:
        int r4 = 0;
        if (r13.choices.contains(r12.section) == false) goto L15;
        String r0 = r12.section;
    L16:
        ArrayList r5 = new ArrayList();
        Iterator<Ref> r6 = r13.refs.iterator();
    L18:
        if (r6.hasNext() == false) goto L19;
        Ref r1 = r6.next();
        if ("Todas las secciones".equals(r0) == true) goto L31;
        if (r0.equals(r1.group) == false) goto L18;
    L31:
        if (r12.favorites == false) goto L36;
        if (Boolean.TRUE.equals(r12.prefs.get("Películas\n" + r1.group + "\n" + r1.title)) == false) goto L18;
    L36:
        if (r12.filter.isEmpty() == true) goto L40;
        if ((String.valueOf(r1.title) + " " + r1.group + " " + r12.provider).toLowerCase(Locale.ROOT).contains(r12.filter) == false) goto L18;
    L40:
        r5.add(r1);
        goto L18
    L19:
        MovieRows r3 = new MovieRows(r5);
    L21:
        if (r4 >= Math.min(24, r3.size())) goto L22;
        r3.get(r4);
        r4 = r4 + 1;
        goto L21
    L22:
        BlackBox.event("MOVIES_VIEW_READY", "rows=" + r3.size() + " primed_rows=" + Math.min(24, r3.size()) + " duration_ms=" + ((System.nanoTime() - r2) / 1000000));
        return new MainActivity.CatalogPlan(r3, r13.choices, r0);
    L15:
        r0 = r13.choices.get(0);
        goto L16
    }

    static Snapshot read(DemandCatalog r17, String r18) throws IOException {
        File r3 = file(r17, r18);
        if (r3.length() > 16777216) goto L62;
        Throwable r4 = null;
        DataInputStream r5 = new DataInputStream(new BufferedInputStream(new FileInputStream(r3)));     // Catch: Throwable -> L55
    L51:
        th = move-exception;
        r5.close();     // Catch: Throwable -> L55
        throw th;     // Catch: Throwable -> L55
    L7:
        if (r5.readInt() != 1128809591) goto L75;
        if (r5.readLong() != SourceStore.stamp(r17, "Películas")) goto L75;
        if (r5.readLong() != SourceStore.weight(r17, "Películas")) goto L75;
        if (r5.readLong() != SourceStore.payloadSize(r17)) goto L75;
        if (r5.readLong() != SourceStore.payloadTime(r17)) goto L75;
        if (r18.equals(r5.readUTF()) == false) goto L75;
        int r2 = DemandCatalog.Directory.bounded(r5.readInt(), AacUtil.AAC_LC_MAX_RATE_BYTES_PER_SECOND);     // Catch: Throwable -> L51
        ArrayList r6 = new ArrayList(r2);     // Catch: Throwable -> L51
        HashMap r7 = new HashMap();     // Catch: Throwable -> L51
        Iterator<Catalog.Source> r0 = r17.directory.catalog.sources.iterator();     // Catch: Throwable -> L51
    L20:
        if (r0.hasNext() == false) goto L21;
        DemandCatalog.Rows r8 = (DemandCatalog.Rows) r0.next().entries;     // Catch: Throwable -> L51
        String r9 = String.valueOf(r8.directory.file.getName()) + ":" + r8.records;     // Catch: Throwable -> L51
        if (r7.containsKey(r9) == true) goto L48;
        r7.put(r9, r8);     // Catch: Throwable -> L51
    L48:
        sourceBindings++;     // Catch: Throwable -> L51
        goto L20
    L21:
        int r10 = 0;
    L22:
        if (r10 >= r2) goto L23;
        String r11 = r5.readUTF();     // Catch: Throwable -> L51
        int r12 = r5.readInt();     // Catch: Throwable -> L51
        DemandCatalog.Rows r13 = (DemandCatalog.Rows) r7.get(r11);     // Catch: Throwable -> L51
        if (r13 == null) goto L74;
        if (r12 < 0) goto L73;
        if (r12 >= r13.count) goto L73;
        r6.add(new Ref(r13, r12, r5.readUTF(), r5.readUTF(), r5.readInt(), r5.readInt()));     // Catch: Throwable -> L51
        r10 = r10 + 1;     // Catch: Throwable -> L51
    L73:
        throw new IOException("Movie index row");     // Catch: Throwable -> L51
    L74:
        throw new IOException("Movie index source");     // Catch: Throwable -> L51
    L23:
        int r14 = DemandCatalog.Directory.bounded(r5.readInt(), AacUtil.AAC_LC_MAX_RATE_BYTES_PER_SECOND);     // Catch: Throwable -> L51
        ArrayList r15 = new ArrayList();     // Catch: Throwable -> L51
        int r16 = 0;
    L24:
        if (r16 >= r14) goto L26;
        r15.add(r5.readUTF());     // Catch: Throwable -> L51
        r16 = r16 + 1;     // Catch: Throwable -> L51
        goto L24
    L26:
        if (r15.isEmpty() == true) goto L35;
        if ("Todas las secciones".equals(r15.get(0)) == false) goto L35;
        if (r5.read() != (-1)) goto L35;
        Snapshot r1 = new Snapshot(r18, r6, r15);     // Catch: Throwable -> L51
        r5.close();     // Catch: Throwable -> L55
        return r1;
    L35:
        throw new IOException("Movie index choices");     // Catch: Throwable -> L51
    L75:
        throw new IOException("Movie index fingerprint");     // Catch: Throwable -> L51
    L55:
        th = move-exception;
        if (0 == 0) goto L60;
        if (null == th) goto L76;
        r4.addSuppressed(th);
        throw null;
    L76:
        throw null;
    L60:
        throw th;
    L62:
        throw new IOException("Movie index size");
    }

    static synchronized Snapshot snapshot(DemandCatalog r7, String r8) {
        monitor-enter(DemandMovies.class);
        Snapshot r1 = r7.movieViews.get(r8);     // Catch: Throwable -> L21
        if (r1 == null) goto L8;
        monitor-exit(DemandMovies.class);
        return r1;
    L8:
        r7.resolve();     // Catch: Throwable -> L21
        if (r7.directory != null) goto L13;
        monitor-exit(DemandMovies.class);
        return null;
    L13:
        long r2 = System.nanoTime();     // Catch: Throwable -> L21
        String r3 = "disk";
        Snapshot r4 = read(r7, r8);     // Catch: IOException -> L16 Throwable -> L21
    L18:
        r7.movieViews.put(r8, r4);     // Catch: Throwable -> L21
        BlackBox.event("MOVIES_DIRECTORY_READY", "rows=" + r4.refs.size() + " source=" + r3 + " duration_ms=" + ((System.nanoTime() - r2) / 1000000) + " created_entries=0");     // Catch: Throwable -> L21
        monitor-exit(DemandMovies.class);
        return r4;
    L17:
        r3 = "build";
        r4 = build(r7, r8);     // Catch: Throwable -> L21
        write(r7, r4);     // Catch: Throwable -> L21
    L21:
        th = move-exception;
        throw th;
    }

    static void write(DemandCatalog r8, Snapshot r9) {
        File r1 = file(r8, r9.provider);
        File r2 = new File(String.valueOf(r1.getPath()) + ".tmp");
        IdentityHashMap r3 = new IdentityHashMap();
        int r4 = 0;
    L4:
        if (r4 >= r8.directory.catalog.sources.size()) goto L5;
        r3.put((DemandCatalog.Rows) r8.directory.catalog.sources.get(r4).entries, Integer.valueOf(r4));
        r4 = r4 + 1;
        goto L4
    L5:
        Throwable r5 = null;
        DataOutputStream r6 = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(r2)));     // Catch: Throwable -> L28
        r6.writeInt(1128809591);     // Catch: Throwable -> L24
        r6.writeLong(SourceStore.stamp(r8, "Películas"));     // Catch: Throwable -> L24
        r6.writeLong(SourceStore.weight(r8, "Películas"));     // Catch: Throwable -> L24
        r6.writeLong(SourceStore.payloadSize(r8));     // Catch: Throwable -> L24
        r6.writeLong(SourceStore.payloadTime(r8));     // Catch: Throwable -> L24
        r6.writeUTF(r9.provider);     // Catch: Throwable -> L24
        r6.writeInt(r9.refs.size());     // Catch: Throwable -> L24
        Iterator<Ref> r10 = r9.refs.iterator();     // Catch: Throwable -> L24
    L10:
        if (r10.hasNext() == false) goto L11;
        Ref r0 = r10.next();     // Catch: Throwable -> L24
        r6.writeUTF(String.valueOf(r0.rows.directory.file.getName()) + ":" + r0.rows.records);     // Catch: Throwable -> L24
        r6.writeInt(r0.row);     // Catch: Throwable -> L24
        r6.writeUTF(r0.title);     // Catch: Throwable -> L24
        r6.writeUTF(r0.group);     // Catch: Throwable -> L24
        r6.writeInt(r0.season);     // Catch: Throwable -> L24
        r6.writeInt(r0.episode);     // Catch: Throwable -> L24
        goto L10
    L11:
        r6.writeInt(r9.choices.size());     // Catch: Throwable -> L24
        Iterator<String> r11 = r9.choices.iterator();     // Catch: Throwable -> L24
    L12:
        if (r11.hasNext() == false) goto L14;
        r6.writeUTF(r11.next());     // Catch: Throwable -> L24
        goto L12
    L14:
        r6.close();     // Catch: Throwable -> L28
        if (r2.length() <= 16777216) goto L18;
    L19:
        r2.delete();
        return;
    L18:
        if (r2.renameTo(r1) == false) goto L19;
        return;
    L24:
        th = move-exception;
        r6.close();     // Catch: Throwable -> L28
        throw th;     // Catch: Throwable -> L28
    L28:
        th = move-exception;
        if (0 == 0) goto L47;
        if (null == th) goto L33;
        r5.addSuppressed(th);     // Catch: IOException -> L34
    L35:
        r2.delete();
        return;
    L33:
        throw null;     // Catch: IOException -> L34
        goto L35
    }
}
