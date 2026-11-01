package es.antonio.m3u;

import android.util.AtomicFile;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/* JADX INFO: loaded from: classes7.dex */
final class SourceStore {
    static final int MAGIC = 1128813369;
    static int blobsWritten;
    private static final Set<String> inFlight = null;
    private static final List<WeakReference<DemandCatalog.Directory>> live = null;
    static long metadataRows;
    static long payloadRows;

    static final class Meta implements Serializable {
        private static final long serialVersionUID = 1;
        List<Ref> refs;
        Map<String, Long> revisions;
        Map<String, Map<String, Integer>> unique;
        int version;

        Meta() {
            this.version = 193;
            this.refs = new ArrayList();
            this.revisions = new HashMap();
            this.unique = new HashMap();
        }

        Map<String, Integer> counts() {
            HashMap r0 = new HashMap();
            Iterator<Map.Entry<String, Map<String, Integer>>> r1 = this.unique.entrySet().iterator();
        L4:
            if (r1.hasNext() == false) goto L5;
            Map.Entry<String, Map<String, Integer>> r2 = r1.next();
            r0.put(r2.getKey(), Integer.valueOf(r2.getValue().size()));
            goto L4
        L5:
            return r0;
        }
    }

    static final class Ref implements Serializable {
        private static final long serialVersionUID = 1;
        int count;
        String file;
        long modified;
        int ordinal;
        long size;
        Catalog.Source source;
        String summary;

        Ref() {
        }

        String key() {
            return String.valueOf(this.file) + ":" + this.ordinal + ":" + this.modified + ":" + this.size;
        }
    }

    static final class Summary implements Serializable {
        private static final long serialVersionUID = 1;
        Map<String, Set<String>> keys;

        Summary() {
            this.keys = new HashMap();
        }

        static /* synthetic */ Set lambda$0(String r0) {
            return new HashSet();
        }

        void add(String r3, String r4) {
            this.keys.computeIfAbsent(r3, new SourceStore$Summary$$ExternalSyntheticLambda0()).add(r4);
        }
    }

    static {
        inFlight = new HashSet();
        live = new ArrayList();
    }

    private SourceStore() {
    }

    static void apply(Meta r4, Summary r5, int r6) {
        Iterator<Map.Entry<String, Set<String>>> r7 = r5.keys.entrySet().iterator();
    L4:
        if (r7.hasNext() == false) goto L5;
        Map.Entry<String, Set<String>> r0 = r7.next();
        Map<String, Integer> r1 = r4.unique.computeIfAbsent(r0.getKey(), new SourceStore$$ExternalSyntheticLambda0());
        Iterator<String> r2 = r0.getValue().iterator();
    L8:
        if (r2.hasNext() == false) goto L4;
        String r3 = r2.next();
        int r8 = r1.getOrDefault(r3, 0).intValue() + r6;
        if (r8 < 0) goto L16;
        if (r8 == 0) goto L13;
        r1.put(r3, Integer.valueOf(r8));
        goto L8
    L13:
        r1.remove(r3);
        goto L8
    L16:
        throw new IllegalStateException("List count underflow");
    }

    static FileInputStream backupInput(File r7) throws IOException {
        Object r0 = AppStorage.catalogLock(r7);
        monitor-enter(r0);
    L39:
        th = move-exception;
        throw th;
    L5:
        if (isManifest(r7) == true) goto L9;
        FileInputStream r1 = new FileInputStream(r7);     // Catch: Throwable -> L39
        monitor-exit(r0);     // Catch: Throwable -> L39
        return r1;
    L9:
        final File r2 = File.createTempFile("catalog-backup-", ".tmp", r7.getParentFile());     // Catch: Throwable -> L39
        DemandCatalog r3 = new DemandCatalog(r7.getParentFile(), new File(r7.getParentFile(), "catalog-directory-v185.bin"), r7);     // Catch: Throwable -> L36
        Throwable r8 = null;
        FileOutputStream r4 = new FileOutputStream(r2);     // Catch: Throwable -> L30
        ObjectOutputStream r5 = new ObjectOutputStream(new BufferedOutputStream(r4));     // Catch: Throwable -> L23
        r5.writeObject(new Catalog(r3));     // Catch: Throwable -> L20
        r5.flush();     // Catch: Throwable -> L20
        r4.getFD().sync();     // Catch: Throwable -> L20
        r5.close();     // Catch: Throwable -> L23
        r4.close();     // Catch: Throwable -> L30
        FileInputStream r9 = new C10351(r2, r2);     // Catch: Throwable -> L36
        monitor-exit(r0);     // Catch: Throwable -> L39
        return r9;
    L20:
        th = move-exception;
        r5.close();     // Catch: Throwable -> L23
        throw th;     // Catch: Throwable -> L23
    L23:
        th = move-exception;
        if (0 == 0) goto L27;
        if (null == th) goto L28;
        r8.addSuppressed(th);     // Catch: Throwable -> L30
    L28:
        r4.close();     // Catch: Throwable -> L30
        throw r8;     // Catch: Throwable -> L30
    L27:
        r8 = th;
    L30:
        th = move-exception;
        if (0 == 0) goto L35;
        if (null == th) goto L48;
        r8.addSuppressed(th);     // Catch: Throwable -> L36
        throw null;     // Catch: Throwable -> L36
    L48:
        throw null;     // Catch: Throwable -> L36
    L35:
        throw th;     // Catch: Throwable -> L36
    L36:
        th = move-exception;
        r2.delete();     // Catch: Throwable -> L39
        throw th;     // Catch: Throwable -> L39
    }

    static Meta baseline(File r6, File r7) throws IOException {
        if (isManifest(r7) == true) goto L5;
        Meta r0 = new Meta();
        File r1 = new File(r6, "catalog-directory-v185.bin");
        if (r7.exists() == true) goto L9;
    L21:
        return r0;
    L9:
        if (DemandCache.valid(r1, r7) == false) goto L21;
        Iterator<Catalog.Source> r8 = new DemandCatalog.Directory(new DemandCatalog(r6, r1, r7), r1).catalog.sources.iterator();
    L13:
        if (r8.hasNext() == false) goto L14;
        Catalog.Source r2 = r8.next();
        DemandCatalog.Rows r3 = (DemandCatalog.Rows) r2.entries;
        Ref r4 = ref(r2, r3);
        r0.refs.add(r4);
        Summary r5 = summary(r6, r4, r3);
        apply(r0, r5, 1);
        Iterator<String> r9 = r5.keys.keySet().iterator();
    L18:
        if (r9.hasNext() == false) goto L13;
        r0.revisions.put(r9.next(), Long.valueOf(UUID.randomUUID().getMostSignificantBits()));
        goto L18
    L14:
        BlackBox.event("CATALOG_LISTS_MIGRATED", "lists=" + r0.refs.size() + " entry_objects_created=0 metadata_only=true");
        return r0;
    L5:
        return read(r7);
    }

    static DemandCatalog.Directory child(DemandCatalog r11, Ref r12, Map<String, DemandCatalog.Directory> r13) throws IOException {
        DemandCatalog.Directory r0 = r13.get(String.valueOf(r12.file) + ":" + r12.modified + ":" + r12.size);
        if (r0 != null) goto L6;
        File r1 = local(r11.files, r12.file);
        DemandCatalog.Directory r14 = new DemandCatalog.Directory(new DemandCatalog(r11.files, r1, r11.authoritative, r12.modified, r12.size), r1);
        r13.put(String.valueOf(r12.file) + ":" + r12.modified + ":" + r12.size, r14);
        r0 = r14;
    L6:
        if (r12.ordinal < 0) goto L15;
        if (r12.ordinal >= r0.catalog.sources.size()) goto L15;
        if (r0.catalog.sources.get(r12.ordinal).entries.size() != r12.count) goto L13;
        return r0;
    L13:
        throw new IOException("List count mismatch");
    L15:
        throw new IOException("List source reference");
    }

    static synchronized void cleanup(File r6, Meta r7) {
        monitor-enter(SourceStore.class);
        HashSet r1 = new HashSet(inFlight);     // Catch: Throwable -> L40
        SourceStoreHeader.Header r2 = SourceStoreHeader.readHeader(new File(r6, "catalog.dat"));     // Catch: IOException -> L14 Throwable -> L40
        if (r2 == null) goto L13;
        Iterator<Ref> r3 = r2.refs.iterator();     // Catch: IOException -> L14 Throwable -> L40
    L9:
        if (r3.hasNext() == false) goto L15;
        Ref r4 = r3.next();     // Catch: IOException -> L14 Throwable -> L40
        r1.add(r4.file);     // Catch: IOException -> L14 Throwable -> L40
        r1.add(r4.summary);     // Catch: IOException -> L14 Throwable -> L40
        goto L9
    L13:
        throw new IOException("Missing manifest");     // Catch: IOException -> L14 Throwable -> L40
    L15:
        Iterator<Ref> r8 = r7.refs.iterator();     // Catch: Throwable -> L40
    L17:
        if (r8.hasNext() == false) goto L18;
        Ref r5 = r8.next();     // Catch: Throwable -> L40
        r1.add(r5.file);     // Catch: Throwable -> L40
        r1.add(r5.summary);     // Catch: Throwable -> L40
        goto L17
    L18:
        Iterator<WeakReference<DemandCatalog.Directory>> r9 = live.iterator();     // Catch: Throwable -> L40
    L20:
        if (r9.hasNext() == false) goto L21;
        DemandCatalog.Directory r10 = r9.next().get();     // Catch: Throwable -> L40
        if (r10 == null) goto L36;
        r1.add(r10.file.getName());     // Catch: Throwable -> L40
        goto L20
    L36:
        r9.remove();     // Catch: Throwable -> L40
        goto L20
    L21:
        File[] r11 = r6.listFiles();     // Catch: Throwable -> L40
        if (r11 == null) goto L32;
        int r12 = r11.length;     // Catch: Throwable -> L40
        int r13 = 0;
    L24:
        if (r13 >= r12) goto L32;
        File r14 = r11[r13];     // Catch: Throwable -> L40
        if (r14.getName().matches("catalog-list-[a-f0-9-]+\\.bin") == false) goto L31;
        if (r1.contains(r14.getName()) == true) goto L31;
        r14.delete();     // Catch: Throwable -> L40
    L31:
        r13 = r13 + 1;
    L32:
        monitor-exit(SourceStore.class);
        return;
    L40:
        th = move-exception;
        throw th;
    }

    static String digest(String r8) throws IOException {
        byte[] r9 = MessageDigest.getInstance("SHA-256").digest(r8.getBytes(StandardCharsets.UTF_8));     // Catch: Exception -> L8
        StringBuilder r0 = new StringBuilder();     // Catch: Exception -> L8
        int r1 = r9.length;     // Catch: Exception -> L8
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L4;
        r0.append(String.format(Locale.ROOT, "%02x", new Object[]{Integer.valueOf(r9[r3] & 255)}));     // Catch: Exception -> L8
        r3 = r3 + 1;
        goto L3
    L4:
        return r0.toString();
    L8:
        e = move-exception;
        throw new IOException(e);
    }

    static boolean isManifest(File r4) {
        Throwable r1 = null;
        DataInputStream r2 = new DataInputStream(new FileInputStream(r4));     // Catch: Throwable -> L14
    L6:
        if (r2.readInt() != MAGIC) goto L8;
        boolean r5 = true;
    L9:
        r2.close();     // Catch: Throwable -> L14
        return r5;
    L8:
        r5 = false;
    L11:
        th = move-exception;
        r2.close();     // Catch: Throwable -> L14
        throw th;     // Catch: Throwable -> L14
    L14:
        th = move-exception;
        if (0 == 0) goto L27;
        if (null == th) goto L19;
        r1.addSuppressed(th);     // Catch: IOException -> L20
    L21:
        return false;
    L19:
        throw null;     // Catch: IOException -> L20
        goto L21
    L27:
        throw th;     // Catch: IOException -> L20
    }

    static /* synthetic */ Map lambda$0(String r0) {
        return new HashMap();
    }

    static File local(File r1, String r2) throws IOException {
        if (r2 == null) goto L10;
        if (r2.equals(new File(r2).getName()) == false) goto L10;
        if (r2.contains("..") == true) goto L10;
        return new File(r1, r2);
    L10:
        throw new IOException("List path invalid");
    }

    static Meta manifest(DemandCatalog r1) throws IOException {
        if ((r1.sourceManifest instanceof Meta) == true) goto L5;
        monitor-enter(r1);
    L15:
        th = move-exception;
        throw th;
    L8:
        if ((r1.sourceManifest instanceof Meta) == false) goto L12;
        Meta r0 = (Meta) r1.sourceManifest;     // Catch: Throwable -> L15
        monitor-exit(r1);     // Catch: Throwable -> L15
        return r0;
    L12:
        Meta r2 = read(r1.authoritative);     // Catch: Throwable -> L15
        r1.sourceManifest = r2;     // Catch: Throwable -> L15
        monitor-exit(r1);     // Catch: Throwable -> L15
        return r2;
    L5:
        return (Meta) r1.sourceManifest;
    }

    static synchronized void note(DemandCatalog.Directory r3) {
        monitor-enter(SourceStore.class);
        live.add(new WeakReference(r3));     // Catch: Throwable -> L7
        monitor-exit(SourceStore.class);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    static DemandCatalog.Directory open(DemandCatalog r17) throws IOException {
        long r1 = System.nanoTime();
        if (r17.knownCounts == false) goto L5;
        List<Ref> r3 = SourceStoreHeader.refs(r17);
        Map<String, Integer> r4 = new HashMap(r17.counts);
    L6:
        if (r3 == null) goto L14;
        ArrayList r5 = new ArrayList();
        HashMap r6 = new HashMap();
        Iterator<Ref> r7 = r3.iterator();
    L9:
        if (r7.hasNext() == false) goto L10;
        Ref r8 = r7.next();
        DemandCatalog.Directory r9 = child(r17, r8, r6);
        Catalog.Source r15 = new Catalog.Source(r8.source.f177id, r8.source.name, r8.source.origin, r8.source.kind, Collections.emptyList());
        r15.originalFilename = r8.source.originalFilename;
        r15.provider = r8.source.provider;
        r15.entries = r9.catalog.sources.get(r8.ordinal).entries;
        r5.add(r15);
        goto L9
    L10:
        DemandCatalog.Directory r10 = new DemandCatalog.Directory(r17, r5, r4);
        BlackBox.event("CATALOG_LISTS_OPEN", "lists=" + r5.size() + " payload_entries_created=0 full_identity_map=" + (r17.sourceManifest instanceof Meta) + " duration_ms=" + ((System.nanoTime() - r1) / 1000000));
        return r10;
    L14:
        throw new IOException("List manifest missing");
    L5:
        Meta r11 = manifest(r17);
        List<Ref> r12 = r11.refs;
        r4 = r11.counts();
        r3 = r12;
        goto L6
    }

    static long payloadSize(DemandCatalog r2) {
        if (SourceStoreHeader.revisions(r2) == null) goto L7;
        return 0;
    L7:
        return r2.directory.file.length();
    }

    static long payloadTime(DemandCatalog r2) {
        if (SourceStoreHeader.revisions(r2) == null) goto L7;
        return 0;
    L7:
        return r2.directory.file.lastModified();
    }

    static Meta read(File r5) throws IOException {
        IOException r0 = null;
        DataInputStream r1 = new DataInputStream(new BufferedInputStream(new AtomicFile(r5).openRead()));     // Catch: Throwable -> L31
    L27:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L31
        throw th;     // Catch: Throwable -> L31
    L6:
        if (r1.readInt() != MAGIC) goto L50;
        ObjectInputStream r6 = new ObjectInputStream(r1);     // Catch: Throwable -> L19
        Meta r2 = (Meta) r6.readObject();     // Catch: Throwable -> L16
        if (r2.version != 193) goto L14;
        r6.close();     // Catch: Throwable -> L19
        r1.close();     // Catch: Throwable -> L31
        return r2;
    L14:
        r0 = new IOException("List manifest version");     // Catch: Throwable -> L16
        throw r0;     // Catch: Throwable -> L16
    L16:
        th = move-exception;
        r6.close();     // Catch: Throwable -> L19
        throw th;     // Catch: Throwable -> L19
    L19:
        th = move-exception;
        if (r0 == null) goto L49;
        if (r0 == th) goto L24;
        r0.addSuppressed(th);     // Catch: Throwable -> L27
    L24:
        throw r0;     // Catch: Throwable -> L27
    L49:
        throw th;     // Catch: Throwable -> L27
    L50:
        throw new IOException("List manifest magic");     // Catch: Throwable -> L27
    L31:
        th = move-exception;
        if (r0 == null) goto L51;
        if (r0 == th) goto L36;
        r0.addSuppressed(th);     // Catch: ClassCastException -> L37 Throwable -> L39
    L39:
        e = move-exception;
        throw new IOException("List manifest invalid", e);
    L36:
        throw r0;     // Catch: ClassCastException -> L37 Throwable -> L39
    L51:
        throw th;     // Catch: ClassCastException -> L37 Throwable -> L39
    }

    static Summary readSummary(File r4, Ref r5) throws IOException {
        Throwable r0 = null;
        ObjectInputStream r1 = new ObjectInputStream(new BufferedInputStream(new FileInputStream(local(r4, r5.summary))));     // Catch: Throwable -> L10
        Summary r6 = (Summary) r1.readObject();     // Catch: Throwable -> L7
        r1.close();     // Catch: Throwable -> L10
        return r6;
    L7:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L10
        throw th;     // Catch: Throwable -> L10
    L10:
        th = move-exception;
        if (0 == 0) goto L26;
        if (null == th) goto L15;
        r0.addSuppressed(th);     // Catch: ClassCastException -> L16 Throwable -> L18
    L18:
        e = move-exception;
        throw new IOException(e);
    L15:
        throw null;     // Catch: ClassCastException -> L16 Throwable -> L18
    L26:
        throw th;     // Catch: ClassCastException -> L16 Throwable -> L18
    }

    static void recover(File r1) {
        FileInputStream r2 = new AtomicFile(r1).openRead();     // Catch: Throwable -> L6
        if (r2 == null) goto L14;
        r2.close();     // Catch: Throwable -> L6
        return;
    L14:
        return;
    L6:
        th = move-exception;
        throw th;     // Catch: IOException -> L8
    }

    static Ref ref(Catalog.Source r7, DemandCatalog.Rows r8) throws IOException {
        Ref r0 = new Ref();
        r0.file = r8.directory.file.getName();
        r0.modified = r8.directory.owner.modified;
        r0.size = r8.directory.owner.size;
        r0.count = r8.count;
        r0.ordinal = -1;
        int r1 = 0;
    L4:
        if (r1 >= r8.directory.catalog.sources.size()) goto L6;
        if (r8.directory.catalog.sources.get(r1).entries != r8) goto L14;
        r0.ordinal = r1;
    L14:
        r1 = r1 + 1;
        goto L4
    L6:
        if (r0.ordinal < 0) goto L10;
        r0.source = new Catalog.Source(r7.f177id, r7.name, r7.origin, r7.kind, Collections.emptyList());
        r0.source.originalFilename = r7.originalFilename;
        r0.source.provider = r7.provider;
        r0.summary = "catalog-summary-" + digest(r0.key()) + ".bin";
        return r0;
    L10:
        throw new IOException("List directory source");
    }

    static synchronized void release(List<File> r3) {
        monitor-enter(SourceStore.class);
        Iterator<File> r4 = r3.iterator();     // Catch: Throwable -> L11
    L6:
        if (r4.hasNext() == false) goto L7;
        File r1 = r4.next();     // Catch: Throwable -> L11
        inFlight.remove(r1.getName());     // Catch: Throwable -> L11
        goto L6
    L7:
        monitor-exit(SourceStore.class);
        return;
    L11:
        th = move-exception;
        throw th;
    }

    static synchronized void reserve(File r2) {
        monitor-enter(SourceStore.class);
        inFlight.add(r2.getName());     // Catch: Throwable -> L7
        monitor-exit(SourceStore.class);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    static boolean sharded(DemandCatalog r4) {
        if ((r4.sourceManifest instanceof Meta) == false) goto L6;
        return true;
    L6:
        if (r4.sourceManifest != Boolean.FALSE) goto L9;
        return false;
    L9:
        if (isManifest(r4.authoritative) == true) goto L17;
        r4.sourceManifest = Boolean.FALSE;
        return false;
    L17:
        manifest(r4);     // Catch: IOException -> L14
        return true;
    L14:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    static long stamp(DemandCatalog r3, String r4) {
        Map<String, Long> r0 = SourceStoreHeader.revisions(r3);
        if (r0 != null) goto L6;
        return r3.modified;
    L6:
        return r0.getOrDefault(r4, 0L).longValue();
    }

    static Summary summary(File r13, Ref r14, DemandCatalog.Rows r15) throws IOException {
        File r16 = local(r13, r14.summary);
        Throwable r17 = null;
        ObjectInputStream r0 = new ObjectInputStream(new BufferedInputStream(new FileInputStream(r16)));     // Catch: Throwable -> L12
        Summary r1 = (Summary) r0.readObject();     // Catch: Throwable -> L7
        r0.close();     // Catch: Throwable -> L12
        return r1;
    L7:
        Throwable th = th;
        r0.close();     // Catch: Throwable -> L10
        throw th;     // Catch: Throwable -> L10
    L10:
        th = th;
    L14:
        if (th == null) goto L71;
        if (th == th) goto L18;
        th.addSuppressed(th);     // Catch: ClassCastException -> L19 Throwable -> L21 IOException -> L23
    L24:
        Summary r2 = new Summary();
        DemandFile r3 = new DemandFile(r15.directory.file, "r");     // Catch: Throwable -> L52
        int r4 = 0;
    L65:
        if (r4 >= r15.count) goto L29;
        int r5 = Math.min(1024, r15.count - r4);     // Catch: Throwable -> L48
        byte[] r6 = new byte[r5 * 72];     // Catch: Throwable -> L48
        r3.seek(r15.records + (((long) r4) * 72));     // Catch: Throwable -> L48
        r3.readFully(r6);     // Catch: Throwable -> L48
        IntBuffer r7 = ByteBuffer.wrap(r6).asIntBuffer();     // Catch: Throwable -> L48
        int r8 = 0;
    L33:
        if (r8 >= r5) goto L34;
        int r9 = r8 * 18;     // Catch: Throwable -> L48
        String r10 = r15.directory.value(r3, r7.get(r9 + 4));     // Catch: Throwable -> L48
        String r11 = r15.directory.value(r3, r7.get(r9 + 1));     // Catch: Throwable -> L48
        if ("Series".equals(r10) == false) goto L45;
        String r12 = r15.directory.value(r3, r7.get(r9 + 5));     // Catch: Throwable -> L48
        if (r12 != null) goto L41;
    L44:
        String r18 = String.valueOf(r15.directory.value(r3, r7.get(r9 + 17))) + "\n" + r11;     // Catch: Throwable -> L48
    L46:
        r2.add(r10, r18);     // Catch: Throwable -> L48
        metadataRows++;     // Catch: Throwable -> L48
        r8 = r8 + 1;
        goto L33
    L41:
        if (r12.isEmpty() == true) goto L44;
        r11 = r12;
        goto L44
    L45:
        r18 = String.valueOf(r10) + "\n" + r11 + "\n" + r15.directory.value(r3, r7.get(r9 + 0));     // Catch: Throwable -> L48
        goto L46
    L34:
        r4 = r4 + 1024;     // Catch: Throwable -> L48
        goto L65
    L29:
        r3.close();     // Catch: Throwable -> L52
        writeObject(r16, r2);
        return r2;
    L48:
        th = move-exception;
        r3.close();     // Catch: Throwable -> L52
        throw th;     // Catch: Throwable -> L52
    L52:
        th = move-exception;
        if (0 == 0) goto L72;
        if (null == th) goto L57;
        r17.addSuppressed(th);
    L57:
        throw null;
    L72:
        throw th;
    L18:
        throw th;     // Catch: ClassCastException -> L19 Throwable -> L21 IOException -> L23
        goto L24
    L71:
        throw th;     // Catch: ClassCastException -> L19 Throwable -> L21 IOException -> L23
    L12:
        th = th;
        th = null;
        goto L14
    }

    static int total(Meta r2) {
        Iterator<Ref> r3 = r2.refs.iterator();
        int r0 = 0;
    L4:
        if (r3.hasNext() == false) goto L5;
        r0 = r0 + r3.next().count;
        goto L4
    L5:
        return r0;
    }

    static long weight(DemandCatalog r0, String r1) {
        if (SourceStoreHeader.revisions(r0) == null) goto L7;
        return 0;
    L7:
        return r0.size;
    }

    static Catalog write(File r30, File r31, Catalog r32) throws IOException {
        File r9 = r31;
        Catalog r1 = r32;
        long r10 = System.nanoTime();
        long r12 = payloadRows;
        long r14 = metadataRows;
        int r16 = blobsWritten;
        Meta r7 = baseline(r30, r31);
        String r8 = "catalog-directory-v185.bin";
        if (r1.mutation != null) goto L5;
    L15:
        HashMap r6 = new HashMap();
        Iterator<Ref> r2 = r7.refs.iterator();
    L17:
        if (r2.hasNext() == false) goto L18;
        HashMap r29 = r6;
        Ref r3 = r2.next();
        r29.put(r3.key(), r3);
        r9 = r31;
        r6 = r29;
        r12 = r12;
        goto L17
    L18:
        ArrayList r5 = new ArrayList();
        HashSet r4 = new HashSet();
        ArrayList r11 = new ArrayList();
        HashSet r13 = new HashSet();
        HashMap r33 = r6;
        Iterator<Catalog.Source> r17 = r1.sources.iterator();     // Catch: Throwable -> L73
    L21:
        if (r17.hasNext() == false) goto L22;
        Catalog.Source r15 = r17.next();     // Catch: Throwable -> L73
        if ((r15.entries instanceof DemandCatalog.Rows) == true) goto L91;
        ArrayList r18 = r5;
        HashMap r19 = r33;
        HashSet r34 = r4;
        Catalog r20 = new Catalog();     // Catch: Throwable -> L73
        r20.sources.add(r15);     // Catch: Throwable -> L73
        CatalogIndex r21 = new CatalogIndex(r20);     // Catch: Throwable -> L73
        HashSet r22 = r13;
        File r23 = new File(r30, "catalog-list-" + UUID.randomUUID() + ".bin");     // Catch: Throwable -> L73
        r11.add(r23);     // Catch: Throwable -> L73
        reserve(r23);     // Catch: Throwable -> L73
        HashSet r110 = r22;
        ArrayList r111 = r11;
        HashSet r24 = r34;
        DemandCache.write(r23, r31, r20, r21.providers, r21.counts(), "list_only");     // Catch: Throwable -> L71
        blobsWritten++;     // Catch: Throwable -> L71
        payloadRows += (long) r15.entries.size();     // Catch: Throwable -> L71
        HashMap r210 = r19;
        ArrayList r112 = r18;
        Meta r113 = r7;
        boolean r25 = false;
        DemandCatalog.Rows r26 = (DemandCatalog.Rows) new DemandCatalog.Directory(new DemandCatalog(r30, r23, r31, r31.lastModified(), r31.length()), r23).catalog.sources.get(0).entries;     // Catch: Throwable -> L69
        Ref r27 = ref(r15, r26);     // Catch: Throwable -> L69
        r111.add(local(r30, r27.summary));     // Catch: Throwable -> L69
        Summary r28 = summary(r30, r27, r26);     // Catch: Throwable -> L69
        apply(r113, r28, 1);     // Catch: Throwable -> L69
        r110.addAll(r28.keys.keySet());     // Catch: Throwable -> L69
    L93:
        ArrayList r35 = r112;
        r35.add(r27);     // Catch: Throwable -> L69
        r9 = r31;
        r5 = r35;
        r13 = r110;
        r11 = r111;
        r7 = r113;
        r8 = r8;
        r4 = r24;
        r10 = r10;
        r12 = r12;
        r14 = r14;
        r33 = r210;
    L69:
        th = th;
    L76:
        boolean r36 = r25;
    L77:
        if (r36 == true) goto L82;
        Iterator r37 = r111.iterator();
    L80:
        if (r37.hasNext() == false) goto L82;
        ((File) r37.next()).delete();
    L82:
        release(r111);
        throw th;
    L71:
        th = th;
    L75:
        r25 = false;
        goto L76
    L91:
        DemandCatalog.Rows r38 = (DemandCatalog.Rows) r15.entries;     // Catch: Throwable -> L48
        r27 = ref(r15, r38);     // Catch: Throwable -> L48
        r112 = r5;
        HashMap r39 = r33;
        if (r39.containsKey(r27.key()) == true) goto L55;
        Summary r40 = summary(r30, r27, r38);     // Catch: Throwable -> L48
        apply(r7, r40, 1);     // Catch: Throwable -> L48
        r13.addAll(r40.keys.keySet());     // Catch: Throwable -> L48
        HashSet r310 = r4;
    L60:
        r24 = r310;
        r210 = r39;
        r110 = r13;
        r111 = r11;
        r113 = r7;
        r25 = false;
        goto L93
    L55:
        r4.add(r27.key());     // Catch: Throwable -> L48
        Ref r41 = (Ref) r39.get(r27.key());     // Catch: Throwable -> L48
        r310 = r4;
        if (Objects.equals(r41.source.name, r27.source.name) == true) goto L58;
    L59:
        r13.addAll(readSummary(r30, r27).keys.keySet());     // Catch: Throwable -> L48
        goto L60
    L58:
        if (Objects.equals(r41.source.provider, r27.source.provider) == true) goto L60;
    L48:
        th = th;
        r111 = r11;
        r36 = false;
        goto L77
    L22:
        Iterator<Ref> r42 = r7.refs.iterator();     // Catch: Throwable -> L48
    L24:
        if (r42.hasNext() == false) goto L25;
        Ref r43 = r42.next();     // Catch: Throwable -> L48
        Iterator<Ref> r311 = r42;
        if (r4.contains(r43.key()) == true) goto L47;
        Summary r44 = readSummary(r30, r43);     // Catch: Throwable -> L48
        apply(r7, r44, -1);     // Catch: Throwable -> L48
        r13.addAll(r44.keys.keySet());     // Catch: Throwable -> L48
    L47:
        r42 = r311;
        goto L24
    L25:
        Iterator r45 = r13.iterator();     // Catch: Throwable -> L48
    L27:
        if (r45.hasNext() == false) goto L28;
        r7.revisions.put((String) r45.next(), Long.valueOf(UUID.randomUUID().getMostSignificantBits()));     // Catch: Throwable -> L48
        goto L27
    L28:
        r7.refs = r5;     // Catch: Throwable -> L48
        AtomicFile r46 = new AtomicFile(r9);     // Catch: Throwable -> L48
        FileOutputStream r47 = r46.startWrite();     // Catch: IOException -> L38 Throwable -> L48
        DataOutputStream r48 = new DataOutputStream(new BufferedOutputStream(r47));     // Catch: IOException -> L36 Throwable -> L48
        r48.writeInt(MAGIC);     // Catch: IOException -> L36 Throwable -> L48
        ObjectOutputStream r49 = new ObjectOutputStream(r48);     // Catch: IOException -> L36 Throwable -> L48
        r49.writeObject(r7);     // Catch: IOException -> L36 Throwable -> L48
        r49.flush();     // Catch: IOException -> L36 Throwable -> L48
        r46.finishWrite(r47);     // Catch: IOException -> L36 Throwable -> L48
        DemandCatalog r50 = new DemandCatalog(r30, new File(r30, r8), r9);     // Catch: Throwable -> L34
        DemandCatalog.saveHome(r30, r50.cache, r9, r7.counts(), total(r7));     // Catch: Throwable -> L34
        Catalog r51 = new Catalog(r50);     // Catch: Throwable -> L34
        BlackBox.event("CATALOG_LISTS_COMMIT", "lists=" + r7.refs.size() + " processed_entries=" + (payloadRows - r12) + " metadata_rows=" + (metadataRows - r14) + " new_payloads=" + (blobsWritten - r16) + " duration_ms=" + ((System.nanoTime() - r10) / 1000000));     // Catch: Throwable -> L34
        cleanup(r30, r7);     // Catch: Throwable -> L34
        release(r11);
        return r51;
    L34:
        th = th;
        r111 = r11;
        r36 = true;
    L36:
        e = e;
    L40:
        if (r47 == null) goto L42;
        r46.failWrite(r47);     // Catch: Throwable -> L48
    L42:
        throw e;     // Catch: Throwable -> L48
    L38:
        e = e;
        r47 = null;
    L73:
        th = th;
        r111 = r11;
        goto L75
    L5:
        if (isManifest(r31) == false) goto L15;
        Catalog r52 = new DemandCatalog(r30, new File(r30, "catalog-directory-v185.bin"), r9).resolve();
        if ("replace".equals(r1.mutation) == false) goto L10;
        r1 = r52.replace(r1.changedSource);
        goto L15
    L10:
        if ("remove".equals(r1.mutation) == false) goto L13;
        r1 = r52.remove(r1.changedId);
        goto L15
    L13:
        if ("rename".equals(r1.mutation) == false) goto L15;
        r1 = r52.rename(r1.changedId, r1.changedName);
        goto L15
    }

    static void writeObject(File r3, Serializable r4) throws IOException {
        AtomicFile r0 = new AtomicFile(r3);
        FileOutputStream r5 = r0.startWrite();     // Catch: IOException -> L8
        ObjectOutputStream r1 = new ObjectOutputStream(new BufferedOutputStream(r5));     // Catch: IOException -> L6
        r1.writeObject(r4);     // Catch: IOException -> L6
        r1.flush();     // Catch: IOException -> L6
        r0.finishWrite(r5);     // Catch: IOException -> L6
        return;
    L6:
        e = e;
    L10:
        if (r5 == null) goto L12;
        r0.failWrite(r5);
    L12:
        throw e;
    L8:
        e = e;
        r5 = null;
        goto L10
    }
}
