package es.antonio.m3u;

import android.support.v4.media.session.PlaybackStateCompat;
import android.util.AtomicFile;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes7.dex */
final class SourceStoreHeader {
    static long bytesRead;
    private static final Map<DemandCatalog, State> saved = null;

    static final class Header implements Serializable {
        private static final long serialVersionUID = 1;
        List<SourceStore.Ref> refs;
        Map<String, Long> revisions;
        int version;

        Header() {
        }
    }

    static final class Limited extends FilterInputStream {
        long count;

        Limited(InputStream r1) {
            super(r1);
        }

        private void add(int r5) throws IOException {
            if (r5 <= 0) goto L8;
            this.count += (long) r5;
            if (this.count > PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE) goto L7;
            return;
        L7:
            throw new IOException("Manifest prefix too large");
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            int r0 = this.in.read();
            if (r0 < 0) goto L5;
            add(1);
        L5:
            return r0;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] r2, int r3, int r4) throws IOException {
            int r5 = this.in.read(r2, r3, r4);
            add(r5);
            return r5;
        }
    }

    static final class Prefix extends ObjectInputStream {
        Prefix(InputStream r1) throws IOException {
            super(r1);
        }

        @Override // java.io.ObjectInputStream
        protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
            ObjectStreamClass r0 = super.readClassDescriptor();
            if (r0.getName().equals(SourceStore.Meta.class.getName()) == false) goto L25;
            ObjectStreamField[] r1 = r0.getFields();
            String[] r2 = {"version", "refs", "revisions", "unique"};
            int r6 = 0;
            Class[] r4 = {Integer.TYPE, List.class, Map.class, Map.class};
            if (r0.getSerialVersionUID() != 1) goto L24;
            if (r1.length != 4) goto L24;
        L9:
            if (r6 >= r1.length) goto L11;
            if (r2[r6].equals(r1[r6].getName()) == false) goto L22;
            String r3 = r4[r6].getName();
            if (r1[r6].getTypeString() != null) goto L17;
            String r5 = r1[r6].getType().getName();
        L19:
            if (r3.equals(r5) == false) goto L22;
            r6 = r6 + 1;
            goto L9
        L17:
            r5 = r1[r6].getTypeString().substring(1, r1[r6].getTypeString().length() - 1).replace('/', '.');
        L22:
            throw new IOException("Manifest prefix fields");
        L11:
            return ObjectStreamClass.lookup(Header.class);
        L24:
            throw new IOException("Manifest prefix schema");
        L25:
            return r0;
        }
    }

    static final class State {
        final List<SourceStore.Ref> refs;
        final Map<String, Long> revisions;

        State(Map<String, Long> r1, List<SourceStore.Ref> r2) {
            this.revisions = r1;
            this.refs = r2;
        }
    }

    static {
        saved = new WeakHashMap();
    }

    SourceStoreHeader() {
    }

    static Map<String, Long> read(File r0) throws IOException {
        Header r1 = readHeader(r0);
        if (r1 != null) goto L6;
        return null;
    L6:
        return r1.revisions;
    }

    static Header readHeader(File r9) throws IOException {
        Throwable r0 = null;
        Limited r1 = new Limited(new AtomicFile(r9).openRead());     // Catch: Throwable -> L64
        DataInputStream r10 = new DataInputStream(new BufferedInputStream(r1, 8192));     // Catch: Throwable -> L57
    L54:
        th = move-exception;
        r10.close();     // Catch: Throwable -> L57
        throw th;     // Catch: Throwable -> L57
    L7:
        if (r10.readInt() == 1128813369) goto L76;
        r10.close();     // Catch: Throwable -> L57
        r1.close();     // Catch: Throwable -> L64
        return null;
    L76:
        Prefix r2 = new Prefix(r10);     // Catch: Throwable -> L45
        Object r3 = r2.readObject();     // Catch: Throwable -> L42
        if ((r3 instanceof Header) == false) goto L81;
        Header r4 = (Header) r3;     // Catch: Throwable -> L42
        if (r4.version != 193) goto L80;
        if (r4.refs == null) goto L80;
        if (r4.refs.size() > 100000) goto L80;
        if (r4.revisions == null) goto L80;
        if (r4.revisions.size() > 20) goto L80;
        HashMap r5 = new HashMap();     // Catch: Throwable -> L42
        Iterator<Map.Entry<String, Long>> r6 = r4.revisions.entrySet().iterator();     // Catch: Throwable -> L42
    L26:
        if (r6.hasNext() == false) goto L27;
        Map.Entry<String, Long> r7 = r6.next();     // Catch: Throwable -> L42
        if (r7.getKey() == null) goto L38;
        if (r7.getValue() == null) goto L38;
        r5.put(r7.getKey(), r7.getValue());     // Catch: Throwable -> L42
    L38:
        throw new IOException("Manifest revision");     // Catch: Throwable -> L42
    L27:
        bytesRead += r1.count;     // Catch: Throwable -> L42
        BlackBox.event("CATALOG_MANIFEST_PREFIX", "bytes=" + r1.count + " unique_ids_decoded=0 sources=" + r4.refs.size());     // Catch: Throwable -> L42
        r4.revisions = Collections.unmodifiableMap(r5);     // Catch: Throwable -> L42
        r2.close();     // Catch: Throwable -> L45
        r10.close();     // Catch: Throwable -> L57
        r1.close();     // Catch: Throwable -> L64
        return r4;
    L80:
        throw new IOException("Manifest prefix bounds");     // Catch: Throwable -> L42
    L81:
        throw new IOException("Manifest prefix type");     // Catch: Throwable -> L42
    L42:
        th = move-exception;
        r2.close();     // Catch: Throwable -> L45
        throw th;     // Catch: Throwable -> L45
    L45:
        th = move-exception;
        if (0 == 0) goto L49;
        if (null == th) goto L82;
        r0.addSuppressed(th);     // Catch: ClassNotFoundException -> L51 Throwable -> L54
        throw null;     // Catch: ClassNotFoundException -> L51 Throwable -> L54
    L51:
        e = move-exception;
        throw new IOException(e);     // Catch: Throwable -> L54
    L82:
        throw null;     // Catch: ClassNotFoundException -> L51 Throwable -> L54
    L49:
        r0 = th;
        throw r0;     // Catch: ClassNotFoundException -> L51 Throwable -> L54
    L57:
        th = move-exception;
        if (r0 == null) goto L61;
        if (r0 == th) goto L62;
        r0.addSuppressed(th);     // Catch: Throwable -> L64
    L62:
        r1.close();     // Catch: Throwable -> L64
        throw r0;     // Catch: Throwable -> L64
    L61:
        r0 = th;
    L64:
        th = move-exception;
        if (r0 == null) goto L84;
        if (r0 == th) goto L69;
        r0.addSuppressed(th);
    L69:
        throw r0;
    L84:
        throw th;
    }

    static List<SourceStore.Ref> refs(DemandCatalog r1) {
        if ((r1.sourceManifest instanceof SourceStore.Meta) == false) goto L7;
        return ((SourceStore.Meta) r1.sourceManifest).refs;
    L7:
        return state(r1).refs;
    }

    static Map<String, Long> revisions(DemandCatalog r1) {
        if ((r1.sourceManifest instanceof SourceStore.Meta) == false) goto L7;
        return ((SourceStore.Meta) r1.sourceManifest).revisions;
    L7:
        return state(r1).revisions;
    }

    static State state(DemandCatalog r4) {
        Map<DemandCatalog, State> r0 = saved;
        monitor-enter(r0);
        State r1 = saved.get(r4);     // Catch: Throwable -> L48
        monitor-exit(r0);     // Catch: Throwable -> L48
        if (r1 == null) goto L8;
        return r1;
    L8:
        monitor-enter(r4);
        Map<DemandCatalog, State> r2 = saved;     // Catch: Throwable -> L45
        monitor-enter(r2);     // Catch: Throwable -> L45
        State r3 = saved.get(r4);     // Catch: Throwable -> L42
        monitor-exit(r2);     // Catch: Throwable -> L42
        if (r3 == null) goto L61;
        monitor-exit(r4);     // Catch: Throwable -> L45
        return r3;
    L61:
        Header r5 = readHeader(r4.authoritative);     // Catch: Throwable -> L22 IOException -> L24 Throwable -> L45
        if (r5 != null) goto L21;
        State r6 = new State(null, null);     // Catch: Throwable -> L22 IOException -> L24 Throwable -> L45
    L60:
    L30:
        Map<DemandCatalog, State> r7 = saved;     // Catch: Throwable -> L45
        monitor-enter(r7);     // Catch: Throwable -> L45
        saved.put(r4, r6);     // Catch: Throwable -> L36
        monitor-exit(r7);     // Catch: Throwable -> L36
        monitor-exit(r4);     // Catch: Throwable -> L45
        return r6;
    L36:
        th = move-exception;
        throw th;     // Catch: Throwable -> L45
    L21:
        r6 = new State(r5.revisions, r5.refs);     // Catch: Throwable -> L22 IOException -> L24 Throwable -> L45
    L26:
        if (SourceStore.isManifest(r4.authoritative) == true) goto L58;
        r6 = new State(null, null);     // Catch: Throwable -> L45
        goto L30
    L58:
        SourceStore.Meta r8 = SourceStore.manifest(r4);     // Catch: IOException -> L39 Throwable -> L45
        r6 = new State(r8.revisions, r8.refs);     // Catch: IOException -> L39 Throwable -> L45
        BlackBox.event("CATALOG_PREFIX_FALLBACK", "schema_or_header=true");     // Catch: IOException -> L39 Throwable -> L45
    L39:
        e = move-exception;
        throw new IllegalStateException(e);     // Catch: Throwable -> L45
    L42:
        th = move-exception;
        throw th;     // Catch: Throwable -> L45
    L45:
        th = move-exception;
        throw th;
    L48:
        th = move-exception;
        throw th;
    }
}
