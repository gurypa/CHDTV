package es.antonio.m3u;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes3.dex */
final class CatalogCache {
    private static final int MAGIC = 1128809523;
    private static final int VERSION = 6;

    private static final class DeferredHeaders extends AbstractMap<String, String> implements Serializable {
        private static final long serialVersionUID = 1;
        private boolean owned;
        private Map<String, String> values;

        DeferredHeaders(Map<String, String> r1) {
            this.values = r1;
        }

        private Map<String, String> writable() {
            if (this.owned == true) goto L6;
            this.values = new LinkedHashMap(this.values);
            this.owned = true;
        L6:
            return this.values;
        }

        private Object writeReplace() {
            return new LinkedHashMap(this.values);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            if (this.values.isEmpty() == false) goto L5;
            return;
        L5:
            this.values = new LinkedHashMap();
            this.owned = true;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object r2) {
            return this.values.containsKey(r2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsValue(Object r2) {
            return this.values.containsValue(r2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<String, String>> entrySet() {
            return writable().entrySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public /* bridge */ /* synthetic */ Object get(Object r1) {
            return get(r1);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public String get(Object r2) {
            return this.values.get(r2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            return this.values.isEmpty();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public /* bridge */ /* synthetic */ Object put(Object r1, Object r2) {
            return put((String) r1, (String) r2);
        }

        public String put(String r2, String r3) {
            return writable().put(r2, r3);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void putAll(Map<? extends String, ? extends String> r2) {
            writable().putAll(r2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public /* bridge */ /* synthetic */ Object remove(Object r1) {
            return remove(r1);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public String remove(Object r2) {
            return writable().remove(r2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return this.values.size();
        }
    }

    private static final class Dictionary {
        final Map<Map<String, String>, Integer> maps;
        final Map<String, Integer> strings;

        private Dictionary() {
            this.strings = new LinkedHashMap();
            this.maps = new LinkedHashMap();
        }

        /* synthetic */ Dictionary(CatalogCacheIA r1) {
            this();
        }

        int headers(Map<String, String> r6) {
            if (r6 != null) goto L5;
            return -1;
        L5:
            Integer r0 = this.maps.get(r6);
            if (r0 != null) goto L8;
            Iterator<Map.Entry<String, String>> r1 = r6.entrySet().iterator();
        L11:
            if (r1.hasNext() == false) goto L13;
            Map.Entry<String, String> r2 = r1.next();
            m391id(r2.getKey());
            m391id(r2.getValue());
            goto L11
        L13:
            int r3 = this.maps.size();
            this.maps.put(new LinkedHashMap(r6), Integer.valueOf(r3));
            return r3;
        L8:
            return r0.intValue();
        }

        /* JADX INFO: renamed from: id */
        int m391id(String r5) {
            if (r5 != null) goto L5;
            return -1;
        L5:
            Integer r0 = this.strings.get(r5);
            if (r0 != null) goto L8;
            int r1 = this.strings.size();
            this.strings.put(r5, Integer.valueOf(r1));
            return r1;
        L8:
            return r0.intValue();
        }
    }

    private static final class FastInput extends InputStream {
        final byte[] buffer;
        int limit;
        int position;
        final InputStream source;

        FastInput(InputStream r2) {
            this.buffer = new byte[65536];
            this.source = r2;
        }

        private boolean availableBytes() throws IOException {
            if (this.position >= this.limit) goto L5;
            return true;
        L5:
            this.position = 0;
        L6:
            this.limit = this.source.read(this.buffer);
            if (this.limit == 0) goto L6;
            if (this.limit <= 0) goto L12;
            return true;
        L12:
            return false;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.source.close();
        }

        int number() throws IOException {
            if ((this.limit - this.position) < 4) goto L6;
            int r0 = this.position;
            this.position += 4;
            return ((((this.buffer[r0] & 255) << 24) | ((this.buffer[r0 + 1] & 255) << 16)) | ((this.buffer[r0 + 2] & 255) << 8)) | (this.buffer[r0 + 3] & 255);
        L6:
            int r1 = read();
            int r2 = read();
            int r3 = read();
            int r4 = read();
            if ((((r1 | r2) | r3) | r4) < 0) goto L11;
            return (((r1 << 24) | (r2 << 16)) | (r3 << 8)) | r4;
        L11:
            throw new EOFException();
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (availableBytes() == false) goto L5;
            byte[] r0 = this.buffer;
            int r1 = this.position;
            this.position = r1 + 1;
            return r0[r1] & 255;
        L5:
            return -1;
        }

        @Override // java.io.InputStream
        public int read(byte[] r4, int r5, int r6) throws IOException {
            if (r5 < 0) goto L16;
            if (r6 < 0) goto L16;
            if (r5 > (r4.length - r6)) goto L16;
            if (r6 != 0) goto L10;
            return 0;
        L10:
            if (availableBytes() == true) goto L13;
            return -1;
        L13:
            int r0 = Math.min(r6, this.limit - this.position);
            System.arraycopy(this.buffer, this.position, r4, r5, r0);
            this.position += r0;
            return r0;
        L16:
            throw new IndexOutOfBoundsException();
        }

        String utf8(int r5) throws IOException {
            if (r5 != 0) goto L6;
            return "";
        L6:
            if (r5 > (this.limit - this.position)) goto L9;
            String r0 = new String(this.buffer, this.position, r5, StandardCharsets.UTF_8);
            this.position += r5;
            return r0;
        L9:
            byte[] r1 = new byte[r5];
            int r2 = 0;
        L10:
            if (r2 >= r5) goto L17;
            int r3 = read(r1, r2, r5 - r2);
            if (r3 < 0) goto L15;
            r2 = r2 + r3;
            goto L10
        L15:
            throw new EOFException();
        L17:
            return new String(r1, StandardCharsets.UTF_8);
        }
    }

    static final class LazyEntries extends AbstractList<Entry> implements Serializable, RandomAccess {
        final int[] ids;
        final Entry[] loaded;
        final List<Map<String, String>> maps;
        volatile int materialized;
        final LazyProviders providers;
        final TextTable strings;
        final int width;

        LazyEntries(TextTable r2, List<Map<String, String>> r3, int[] r4, int r5, LazyProviders r6) {
            this.strings = r2;
            this.maps = r3;
            this.ids = r4;
            this.width = r5;
            this.providers = r6;
            this.loaded = new Entry[r4.length / r5];
        }

        private Object writeReplace() {
            return new ArrayList(this);
        }

        @Override // java.util.AbstractList, java.util.List
        public synchronized Entry get(int r7) {
            monitor-enter(this);
            if (r7 < 0) goto L21;
        L22:
            th = move-exception;
            throw th;
        L5:
            if (r7 >= size()) goto L21;
            Entry r0 = this.loaded[r7];     // Catch: Throwable -> L22
            if (r0 == null) goto L10;
            monitor-exit(this);
            return r0;
        L10:
            int r1 = this.width * r7;     // Catch: Throwable -> L22
            Entry r2 = new Entry(false);     // Catch: Throwable -> L22
            r2.title = this.strings.get(this.ids[r1]);     // Catch: Throwable -> L22
            r2.group = this.strings.get(this.ids[r1 + 1]);     // Catch: Throwable -> L22
            r2.logo = this.strings.get(this.ids[r1 + 2]);     // Catch: Throwable -> L22
            r2.kind = this.strings.get(this.ids[r1 + 4]);     // Catch: Throwable -> L22
            r2.series = this.strings.get(this.ids[r1 + 5]);     // Catch: Throwable -> L22
            r2.manifest = this.strings.get(this.ids[r1 + 6]);     // Catch: Throwable -> L22
            r2.licenseType = this.strings.get(this.ids[r1 + 7]);     // Catch: Throwable -> L22
            r2.plexBase = this.strings.get(this.ids[r1 + 9]);     // Catch: Throwable -> L22
            r2.plexKey = this.strings.get(this.ids[r1 + 10]);     // Catch: Throwable -> L22
            r2.tvgId = this.strings.get(this.ids[r1 + 11]);     // Catch: Throwable -> L22
            r2.tvgName = this.strings.get(this.ids[r1 + 12]);     // Catch: Throwable -> L22
            r2.playbackText = this.strings;     // Catch: Throwable -> L22
            r2.playbackUrl = this.ids[r1 + 3];     // Catch: Throwable -> L22
            r2.playbackLicense = this.ids[r1 + 8];     // Catch: Throwable -> L22
            r2.season = this.ids[r1 + 13];     // Catch: Throwable -> L22
            r2.episode = this.ids[r1 + 14];     // Catch: Throwable -> L22
            r2.headers = CatalogCache.m529$$Nest$smheaders(this.ids[r1 + 15], this.maps);     // Catch: IOException -> L17 Throwable -> L22
            r2.licenseHeaders = CatalogCache.m529$$Nest$smheaders(this.ids[r1 + 16], this.maps);     // Catch: IOException -> L17 Throwable -> L22
            this.loaded[r7] = r2;     // Catch: Throwable -> L22
            this.materialized++;     // Catch: Throwable -> L22
            if (this.providers == null) goto L15;
            this.providers.remember(r2, this.strings.get(this.ids[r1 + 17]));     // Catch: Throwable -> L22
        L15:
            monitor-exit(this);
            return r2;
        L17:
            e = move-exception;
            throw new IllegalStateException(e);     // Catch: Throwable -> L22
        L21:
            throw new IndexOutOfBoundsException();     // Catch: Throwable -> L22
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        synchronized boolean hasUrlHost(Predicate<String> r6) {
            monitor-enter(this);
            int r0 = 0;
        L22:
            if (r0 >= size()) goto L16;
            Entry r1 = this.loaded[r0];     // Catch: Throwable -> L19
            if (r1 != null) goto L9;
            String r2 = this.strings.authority(this.ids[(this.width * r0) + 3]);     // Catch: Throwable -> L19
        L11:
            if (r6.test(r2) == true) goto L12;
            r0 = r0 + 1;
            goto L22
        L12:
            monitor-exit(this);
            return true;
        L9:
            r2 = Entry.details(r1).url;     // Catch: Throwable -> L19
            goto L11
        L16:
            monitor-exit(this);
            return false;
        L19:
            th = move-exception;
            throw th;
        }

        List<Entry> select(String r9, String r10) {
            int[] r0 = new int[Math.min(size(), 128)];
            int r1 = 0;
            int r2 = 0;
        L4:
            if (r2 >= size()) goto L19;
            int r3 = this.width * r2;
            if (r9.equals(this.strings.get(this.ids[r3 + 4])) == false) goto L18;
            if (r10 == null) goto L15;
            if (this.providers == null) goto L18;
            if (r10.equals(this.strings.get(this.ids[r3 + 17])) == false) goto L18;
        L15:
            if (r1 != r0.length) goto L17;
            r0 = Arrays.copyOf(r0, Math.max(1, r0.length * 2));
        L17:
            r0[r1] = r2;
            r1 = r1 + 1;
        L18:
            r2 = r2 + 1;
            goto L4
        L19:
            final int[] r4 = Arrays.copyOf(r0, r1);
            return new C09711(this, r4);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.loaded.length;
        }
    }

    private static final class LazyProviders extends AbstractMap<Entry, String> {
        final List<LazyEntries> groups;
        final Map<Entry, String> realized;
        int total;

        private LazyProviders() {
            this.groups = new ArrayList();
            this.realized = new IdentityHashMap();
        }

        /* synthetic */ LazyProviders(CatalogCacheIA r1) {
            this();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public synchronized boolean containsKey(Object r2) {
            monitor-enter(this);
            boolean r0 = this.realized.containsKey(r2);     // Catch: Throwable -> L6
            monitor-exit(this);
            return r0;
        L6:
            th = move-exception;
            throw th;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<Entry, String>> entrySet() {
            Iterator<LazyEntries> r0 = this.groups.iterator();
        L4:
            if (r0.hasNext() == false) goto L10;
            LazyEntries r1 = r0.next();
            int r2 = 0;
        L7:
            if (r2 >= r1.size()) goto L4;
            r1.get(r2);
            r2 = r2 + 1;
            goto L7
        L10:
            monitor-enter(this);
            Set<Map.Entry<Entry, String>> r3 = new IdentityHashMap(this.realized).entrySet();     // Catch: Throwable -> L14
            monitor-exit(this);     // Catch: Throwable -> L14
            return r3;
        L14:
            th = move-exception;
            throw th;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public /* bridge */ /* synthetic */ Object get(Object r1) {
            return get(r1);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public synchronized String get(Object r2) {
            monitor-enter(this);
            String r0 = this.realized.get(r2);     // Catch: Throwable -> L6
            monitor-exit(this);
            return r0;
        L6:
            th = move-exception;
            throw th;
        }

        void register(LazyEntries r3) {
            this.groups.add(r3);
            this.total += r3.size();
        }

        synchronized void remember(Entry r2, String r3) {
            monitor-enter(this);
            this.realized.put(r2, r3);     // Catch: Throwable -> L6
            monitor-exit(this);
            return;
        L6:
            th = move-exception;
            throw th;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return this.total;
        }
    }

    static final class Prepared {
        final Map<String, Integer> counts;
        final Map<Entry, String> providers;

        Prepared(Map<Entry, String> r1, Map<String, Integer> r2) {
            this.providers = r1;
            this.counts = r2;
        }
    }

    private static final class Reader extends DataInputStream {
        final FastInput fast;
        final List<String> strings;

        private Reader(FastInput r2) {
            super(r2);
            this.strings = new ArrayList();
            this.fast = r2;
        }

        Reader(InputStream r2) {
            this(new FastInput(r2));
        }

        int number() throws IOException {
            return this.fast.number();
        }

        long timestamp() throws IOException {
            return (((long) number()) << 32) | (((long) number()) & 4294967295L);
        }
    }

    static final class TextTable {
        final List<byte[]> blocks;
        final String[] decoded;
        final int[] lengths;
        final int[] offsets;

        TextTable(int r2) {
            this.decoded = new String[r2 + 1];
            this.offsets = new int[r2];
            this.lengths = new int[r2];
            this.blocks = new ArrayList();
        }

        TextTable(String[] r2) {
            this.decoded = r2;
            this.lengths = null;
            this.offsets = null;
            this.blocks = null;
        }

        String authority(int r10) {
            if (r10 != (-1)) goto L5;
        L33:
            return "";
        L5:
            if (r10 == size()) goto L33;
            String r0 = this.decoded[r10];
            if (r0 == null) goto L10;
            return r0;
        L10:
            byte[] r2 = this.blocks.get(r10 / 1024);
            int r3 = this.offsets[r10];
            int r4 = this.lengths[r10];
            int r5 = 0;
        L11:
            if (r5 >= r4) goto L16;
            if (r2[r3 + r5] == 58) goto L16;
            r5 = r5 + 1;
        L16:
            if ((r5 + 2) >= r4) goto L33;
            if (r2[(r3 + r5) + 1] != 47) goto L33;
            if (r2[(r3 + r5) + 2] != 47) goto L33;
            int r1 = r5 + 3;
        L23:
            if (r1 >= r4) goto L32;
            if (r2[r3 + r1] == 47) goto L32;
            if (r2[r3 + r1] == 63) goto L32;
            if (r2[r3 + r1] == 35) goto L32;
            r1 = r1 + 1;
        L32:
            return new String(r2, r3, r1, StandardCharsets.UTF_8);
        }

        String get(int r7) {
            if (r7 != size()) goto L5;
            return null;
        L5:
            if (r7 == (-1)) goto L27;
            String r0 = this.decoded[r7];
            if (r0 == null) goto L10;
            return r0;
        L10:
            monitor-enter(this);
            String r1 = this.decoded[r7];     // Catch: Throwable -> L20
            if (r1 != null) goto L18;
            int r2 = this.lengths[r7];     // Catch: Throwable -> L20
            if (r2 != 0) goto L16;
            String r3 = "";
        L17:
            r1 = r3;
            this.decoded[r7] = r1;     // Catch: Throwable -> L20
            goto L18
        L16:
            r3 = new String(this.blocks.get(r7 / 1024), this.offsets[r7], r2, StandardCharsets.UTF_8);     // Catch: Throwable -> L20
        L18:
            monitor-exit(this);     // Catch: Throwable -> L20
            return r1;
        L20:
            th = move-exception;
            throw th;
        L27:
            return null;
        }

        String getForReader(int r7) {
            if (r7 != size()) goto L5;
            return null;
        L5:
            if (r7 == (-1)) goto L18;
            String r0 = this.decoded[r7];
            if (r0 != null) goto L17;
            int r1 = this.lengths[r7];
            if (r1 != 0) goto L12;
            String r2 = "";
        L13:
            String r3 = r2;
            this.decoded[r7] = r3;
            return r3;
        L12:
            r2 = new String(this.blocks.get(r7 / 1024), this.offsets[r7], r1, StandardCharsets.UTF_8);
            goto L13
        L17:
            return r0;
        L18:
            return null;
        }

        int size() {
            return this.decoded.length - 1;
        }
    }

    private static final class Writer extends DataOutputStream {
        final Map<String, Integer> strings;

        Writer(OutputStream r2) {
            super(r2);
            this.strings = new HashMap();
        }
    }

    /* JADX INFO: renamed from: -$$Nest$smheaders, reason: not valid java name */
    static /* bridge */ /* synthetic */ Map m529$$Nest$smheaders(int r0, List r1) {
        return headers(r0, r1);
    }

    private CatalogCache() {
    }

    static boolean complete(File r4) {
        DataInputStream r1 = new DataInputStream(new FileInputStream(r4));     // Catch: IOException -> L20
    L14:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L17
    L19:
        throw th;     // Catch: IOException -> L20
    L17:
        th = move-exception;
        th.addSuppressed(th);     // Catch: IOException -> L20
        goto L19
    L5:
        if (r1.readInt() == MAGIC) goto L7;
    L11:
        boolean r2 = false;
    L12:
        r1.close();     // Catch: IOException -> L20
        return r2;
    L7:
        if (r1.readInt() != 6) goto L11;
        if (r1.readBoolean() == false) goto L11;
        r2 = true;
        goto L12
    L21:
        return false;
    }

    private static int count(Reader r3, int r4) throws IOException {
        int r0 = r3.number();
        if (r0 < 0) goto L7;
        if (r0 > r4) goto L7;
        return r0;
    L7:
        throw new IOException("Invalid cache size");
    }

    private static String[] fields(Entry r13) {
        Entry.details(r13);
        return new String[]{r13.title, r13.group, r13.logo, r13.url, r13.kind, r13.series, r13.manifest, r13.licenseType, r13.license, r13.plexBase, r13.plexKey, r13.tvgId, r13.tvgName};
    }

    private static Map<String, String> headers(int r2, List<Map<String, String>> r3) throws IOException {
        if (r2 != (-1)) goto L6;
        return null;
    L6:
        if (r2 < 0) goto L12;
        if (r2 >= r3.size()) goto L12;
        return new DeferredHeaders(r3.get(r2));
    L12:
        throw new IOException("Invalid header reference");
    }

    private static Map<String, String> headers(Reader r1, List<Map<String, String>> r2) throws IOException {
        return headers(r1.number(), r2);
    }

    private static Map<String, String> map(Reader r5, Map<String, String> r6) throws IOException {
        int r0 = r5.number();
        if (r0 != (-1)) goto L6;
        return null;
    L6:
        if (r0 < 0) goto L17;
        if (r0 > 10000) goto L17;
        if (r6 != null) goto L11;
        Map<String, String> r1 = new LinkedHashMap();
    L12:
        r1.clear();
        int r2 = 0;
    L13:
        if (r2 >= r0) goto L15;
        r1.put(text(r5), text(r5));
        r2 = r2 + 1;
        goto L13
    L15:
        return r1;
    L11:
        r1 = r6;
    L17:
        throw new IOException("Invalid headers");
    }

    private static void map(Writer r3, Map<String, String> r4) throws IOException {
        if (r4 != null) goto L5;
        r3.writeInt(-1);
        return;
    L5:
        r3.writeInt(r4.size());
        Iterator<Map.Entry<String, String>> r0 = r4.entrySet().iterator();
    L7:
        if (r0.hasNext() == false) goto L9;
        Map.Entry<String, String> r1 = r0.next();
        text(r3, r1.getKey());
        text(r3, r1.getValue());
        goto L7
    }

    static Prepared prepared(Catalog r1) {
        if ((r1.startupIndex instanceof Prepared) == true) goto L7;
        return null;
    L7:
        return (Prepared) r1.startupIndex;
    }

    static Catalog read(InputStream r6, long r7, long r9) throws IOException {
        return readInternal(r6, r7, r9, false);
    }

    private static Catalog readDictionary(Reader r50, boolean r51, int r52, boolean r53) throws IOException {
        int r1 = r52;
        long r2 = System.nanoTime();
        int r5 = count(r50, 10000000);
        if (r53 == false) goto L6;
        if (r1 != 6) goto L6;
        TextTable r7 = readPackedStrings(r50, r5);
    L7:
        TextTable r9 = r7;
        long r14 = System.nanoTime() - r2;
        long r16 = System.nanoTime();
        int r8 = count(r50, 1000000);
        ArrayList r10 = new ArrayList(Math.min(r8, 8192));
        int r11 = 0;
    L8:
        if (r11 >= r8) goto L13;
        int r12 = count(r50, 10000);
        LinkedHashMap r13 = new LinkedHashMap();
        int r4 = 0;
    L10:
        if (r4 >= r12) goto L12;
        r13.put(ref(r50, r9), ref(r50, r9));
        r4 = r4 + 1;
        goto L10
    L12:
        r10.add(r13);
        r11 = r11 + 1;
        goto L8
    L13:
        Catalog r6 = new Catalog();
        int r15 = count(r50, 1000000);
        CatalogCacheIA r17 = null;
        if (r51 == false) goto L20;
        if (r53 == true) goto L17;
    L19:
        Map r18 = new IdentityHashMap();
    L21:
        int r19 = 0;
    L22:
        if (r19 >= r15) goto L141;
        String r20 = ref(r50, r9);
        String r21 = ref(r50, r9);
        String r22 = ref(r50, r9);
        String r23 = ref(r50, r9);
        String r24 = ref(r50, r9);
        String r110 = ref(r50, r9);
        long r25 = r2;
        int r3 = count(r50, 10000000);
        ArrayList r26 = new ArrayList(r3);
        int r27 = r15;
        String r28 = "Missing provider";
        int r29 = r8;
        int r210 = 18;
        TextTable r211 = r9;
        if (r53 == true) goto L26;
    L78:
        int r37 = r19;
        long r39 = r14;
        r9 = r211;
        Map r111 = r18;
        String r112 = r110;
        if (r51 == true) goto L81;
    L83:
        Map r113 = r111;
    L84:
        if (r51 == true) goto L87;
        r210 = 17;
    L87:
        if (r53 == false) goto L92;
        if (r1 != 6) goto L92;
        boolean r114 = true;
    L93:
        String[] r115 = r9.decoded;
        byte[] r116 = new byte[(Math.min(r3, 1024) * r210) * 4];
        boolean r33 = r114;
        int[] r117 = new int[r116.length / 4];
        int r34 = r29;
        int r118 = 0;
    L94:
        if (r118 >= r3) goto L139;
        int r35 = r3;
        int r30 = Math.min(1024, r35 - r118);
        int r31 = r30 * r210;
        String r38 = r112;
        String r36 = r24;
        r50.readFully(r116, 0, r31 * 4);
        ByteBuffer.wrap(r116, 0, r31 * 4).asIntBuffer().get(r117, 0, r31);
        long r41 = System.nanoTime();
        int r32 = 0;
    L96:
        if (r32 >= r30) goto L128;
        int r119 = r32 * r210;
        int r43 = r31;
        int r40 = 0;
    L98:
        int r212 = r32;
        if (r40 >= 13) goto L117;
        int r42 = r117[r119 + r40];
        int r310 = r119;
        if (r42 != (-1)) goto L103;
        r42 = r5;
        r117[r310 + r40] = r42;
    L105:
        if (r42 == r5) goto L114;
        if (r33 == false) goto L112;
        if (r40 == 3) goto L114;
        if (r40 == 8) goto L114;
    L112:
        if (r115[r42] != null) goto L114;
        r9.getForReader(r42);
    L114:
        r40 = r40 + 1;
        r32 = r212;
        r119 = r310;
        goto L98
    L103:
        if (r42 < 0) goto L116;
        if (r42 < r5) goto L105;
    L116:
        throw new IOException("Invalid dictionary reference");
    L117:
        int r311 = r119;
        if (r51 == false) goto L127;
        int r44 = r117[r311 + 17];
        if (r44 < 0) goto L126;
        if (r44 >= r5) goto L126;
        if (r115[r44] != null) goto L127;
        r9.getForReader(r44);
    L126:
        throw new IOException(r28);
    L127:
        r32 = r212 + 1;
        r31 = r43;
        goto L96
    L128:
        int r45 = r5;
        String r46 = r28;
        r6.startupResolveMs += System.nanoTime() - r41;
        long r47 = System.nanoTime();
        int r120 = 0;
    L129:
        if (r120 >= r30) goto L138;
        int r213 = r120 * r210;
        int r312 = r45;
        int r48 = r30;
        Entry r49 = new Entry(false);
        r49.title = r115[r117[r213]];
        r49.group = r115[r117[r213 + 1]];
        r49.logo = r115[r117[r213 + 2]];
        if (r33 == false) goto L133;
        r49.playbackText = r9;
        r49.playbackUrl = r117[r213 + 3];
        r49.playbackLicense = r117[r213 + 8];
    L134:
        r49.kind = r115[r117[r213 + 4]];
        r49.series = r115[r117[r213 + 5]];
        r49.manifest = r115[r117[r213 + 6]];
        r49.licenseType = r115[r117[r213 + 7]];
        r49.plexBase = r115[r117[r213 + 9]];
        r49.plexKey = r115[r117[r213 + 10]];
        r49.tvgId = r115[r117[r213 + 11]];
        r49.tvgName = r115[r117[r213 + 12]];
        r49.season = r117[r213 + 13];
        r49.episode = r117[r213 + 14];
        r49.headers = headers(r117[r213 + 15], r10);
        r49.licenseHeaders = headers(r117[r213 + 16], r10);
        r26.add(r49);
        if (r51 == false) goto L137;
        r113.put(r49, r115[r117[r213 + 17]]);
    L137:
        r120 = r120 + 1;
        r45 = r312;
        r30 = r48;
        goto L129
    L133:
        r49.url = r115[r117[r213 + 3]];
        r49.license = r115[r117[r213 + 8]];
        goto L134
    L138:
        r6.startupConstructMs += System.nanoTime() - r47;
        r118 = r118 + 1024;
        r28 = r46;
        r5 = r45;
        r3 = r35;
        r24 = r36;
        r112 = r38;
        goto L94
    L139:
        int r313 = r5;
        Catalog.Source r121 = new Catalog.Source(r20, r21, r22, r23, Collections.emptyList());
        r121.originalFilename = r24;
        r121.provider = r112;
        r121.entries = r26;
        r6.sources.add(r121);
        r18 = r113;
    L140:
        r19 = r37 + 1;
        r1 = r52;
        r2 = r25;
        r15 = r27;
        r5 = r313;
        r8 = r34;
        r14 = r39;
    L92:
        r114 = false;
        goto L93
    L81:
        if (r111.isEmpty() == false) goto L83;
        r113 = new IdentityHashMap(r3);
        goto L84
    L26:
        if (r1 != 6) goto L78;
        if (r51 == true) goto L31;
        r210 = 17;
    L31:
        if (r3 > (Integer.MAX_VALUE / r210)) goto L77;
        int[] r54 = new int[r3 * r210];
        ArrayList r314 = r10;
        byte[] r122 = new byte[(Math.min(r3, 1024) * r210) * 4];
        Map r315 = r18;
        int r123 = 0;
    L33:
        if (r123 >= r3) goto L68;
        int r316 = r19;
        String r317 = r110;
        int r124 = Math.min(1024, r3 - r123);
        int r125 = r124 * r210;
        long r318 = r14;
        r50.readFully(r122, 0, r125 * 4);
        ByteBuffer.wrap(r122, 0, r125 * 4).asIntBuffer().get(r54, r123 * r210, r125);
        int r126 = r123;
    L36:
        if (r126 >= (r123 + r124)) goto L67;
        int r127 = r126 * r210;
        int r128 = 0;
    L38:
        int[] r410 = r54;
        if (r128 >= 13) goto L48;
        int r55 = r410[r127 + r128];
        byte[] r411 = r122;
        if (r55 != (-1)) goto L43;
        r410[r127 + r128] = r5;
    L45:
        r128 = r128 + 1;
        r54 = r410;
        r122 = r411;
        goto L38
    L43:
        if (r55 < 0) goto L47;
        if (r55 < r5) goto L45;
    L47:
        throw new IOException("Invalid dictionary reference");
    L48:
        byte[] r412 = r122;
        int r56 = 15;
    L50:
        if (r56 >= 17) goto L58;
        int r129 = r410[r127 + r56];
        if (r129 < (-1)) goto L57;
        if (r129 >= r314.size()) goto L57;
        r56 = r56 + 1;
    L57:
        throw new IOException("Invalid header reference");
    L58:
        if (r51 == false) goto L66;
        if (r410[r127 + 17] < 0) goto L65;
        if (r410[r127 + 17] < r5) goto L66;
    L65:
        throw new IOException("Missing provider");
    L66:
        r126 = r126 + 1;
        r54 = r410;
        r122 = r412;
        goto L36
    L67:
        r123 = r123 + 1024;
        r19 = r316;
        r110 = r317;
        r14 = r318;
        goto L33
    L68:
        int[] r413 = r54;
        r37 = r19;
        String r319 = r110;
        r39 = r14;
        if (r51 == false) goto L71;
        LazyProviders r130 = (LazyProviders) r315;
    L72:
        r9 = r211;
        r10 = r314;
        LazyEntries r57 = new LazyEntries(r9, r10, r413, r210, r130);
        Catalog.Source r131 = new Catalog.Source(r20, r21, r22, r23, Collections.emptyList());
        r131.originalFilename = r24;
        r131.provider = r319;
        r131.entries = r57;
        r6.sources.add(r131);
        if (r51 == false) goto L75;
        ((LazyProviders) r315).register(r57);
    L75:
        r313 = r5;
        r18 = r315;
        r34 = r29;
        goto L140
    L71:
        r130 = null;
        goto L72
    L77:
        throw new IOException("Too many records");
    L141:
        long r320 = r14;
        Map r132 = r18;
        if (r51 == false) goto L148;
        HashMap r58 = new HashMap();
        int r59 = count(r50, 100);
        int r60 = 0;
    L144:
        if (r60 >= r59) goto L146;
        r58.put(ref(r50, r9), Integer.valueOf(count(r50, r6.size())));
        r60 = r60 + 1;
        goto L144
    L146:
        r6.startupIndex = new Prepared(r132, r58);
    L148:
        if (r50.read() != (-1)) goto L152;
        r6.startupResolveMs /= 1000000;
        r6.startupConstructMs /= 1000000;
        r6.startupCacheVersion = r52;
        r6.startupDictionaryMs = r320 / 1000000;
        r6.startupRecordsMs = (System.nanoTime() - r16) / 1000000;
        return r6;
    L152:
        throw new IOException("Trailing cache data");
    L17:
        if (r1 != 6) goto L19;
        r18 = new LazyProviders(r17);
        goto L21
    L20:
        r18 = null;
    L6:
        r7 = new TextTable(readStrings(r50, r5, r1));
        goto L7
    }

    private static Catalog readInternal(InputStream r19, long r20, long r22, boolean r24) throws IOException {
        Reader r0 = new Reader(r19);
        if (r0.number() != MAGIC) goto L58;
        int r2 = r0.number();
        if (r2 == 3) goto L13;
        if (r2 == 4) goto L13;
        if (r2 == 5) goto L13;
        if (r2 == 6) goto L13;
        throw new IOException("Cache outdated");
    L13:
        if (r2 >= 4) goto L15;
    L17:
        boolean r3 = false;
    L19:
        if (r0.timestamp() != r20) goto L56;
        if (r0.timestamp() != r22) goto L56;
        if (r2 >= 5) goto L24;
        Catalog r4 = new Catalog();
        int r6 = Integer.MAX_VALUE;
        int r7 = count(r0, Integer.MAX_VALUE);
        int r8 = 0;
    L26:
        if (r8 >= r7) goto L31;
        String r10 = text(r0);
        String r11 = text(r0);
        String r12 = text(r0);
        String r13 = text(r0);
        String r15 = text(r0);
        String r9 = text(r0);
        int r14 = count(r0, r6);
        ArrayList r16 = new ArrayList(Math.min(r14, 8192));
        int r1 = 0;
    L28:
        if (r1 >= r14) goto L30;
        Entry r17 = new Entry();
        int r18 = r1;
        r17.title = text(r0);
        r17.group = text(r0);
        r17.logo = text(r0);
        r17.url = text(r0);
        r17.kind = text(r0);
        r17.series = text(r0);
        r17.manifest = text(r0);
        r17.licenseType = text(r0);
        r17.license = text(r0);
        r17.plexBase = text(r0);
        r17.plexKey = text(r0);
        r17.tvgId = text(r0);
        r17.tvgName = text(r0);
        r17.season = r0.number();
        r17.episode = r0.number();
        r17.headers = map(r0, r17.headers);
        r17.licenseHeaders = map(r0, r17.licenseHeaders);
        r16.add(r17);
        r1 = r18 + 1;
        r2 = r2;
        goto L28
    L30:
        Catalog.Source r21 = new Catalog.Source(r10, r11, r12, r13, Collections.emptyList());
        r21.entries = r16;
        r21.originalFilename = r15;
        r21.provider = r9;
        r4.sources.add(r21);
        r8 = r8 + 1;
        r6 = Integer.MAX_VALUE;
        r2 = r2;
        goto L26
    L31:
        Prepared r5 = null;
        if (r3 == false) goto L50;
        IdentityHashMap r23 = new IdentityHashMap(r4.size());
        Iterator<Catalog.Source> r25 = r4.sources.iterator();
    L35:
        if (r25.hasNext() == false) goto L45;
        Iterator<Entry> r26 = r25.next().entries.iterator();
    L38:
        if (r26.hasNext() == false) goto L35;
        Entry r110 = r26.next();
        String r111 = text(r0);
        if (r111 == null) goto L43;
        r23.put(r110, r111);
        goto L38
    L43:
        throw new IOException("Missing provider");
    L45:
        HashMap r27 = new HashMap();
        int r28 = count(r0, 100);
        int r29 = 0;
    L46:
        if (r29 >= r28) goto L48;
        r27.put(text(r0), Integer.valueOf(count(r0, r4.size())));
        r29 = r29 + 1;
        goto L46
    L48:
        r5 = new Prepared(r23, r27);
    L50:
        if (r0.read() != (-1)) goto L54;
        r4.startupIndex = r5;
        return r4;
    L54:
        throw new IOException("Trailing cache data");
    L24:
        return readDictionary(r0, r3, r2, r24);
    L56:
        throw new IOException("Cache outdated");
    L15:
        if (r0.readBoolean() == false) goto L17;
        r3 = true;
        goto L19
    L58:
        throw new IOException("Cache outdated");
    }

    private static TextTable readPackedStrings(Reader r17, int r18) throws IOException {
        TextTable r2 = new TextTable(r18);
        byte[] r4 = new byte[Math.min(r18, 1024) * 4];
        int[] r5 = new int[r4.length / 4];
        int r6 = 0;
    L3:
        if (r6 >= r18) goto L21;
        int r7 = Math.min(1024, r18 - r6);
        int r8 = count(r17, Integer.MAX_VALUE);
        r17.readFully(r4, 0, r7 * 4);
        ByteBuffer.wrap(r4, 0, r7 * 4).asIntBuffer().get(r5, 0, r7);
        long r9 = 0;
        int r11 = 0;
    L5:
        if (r11 >= r7) goto L14;
        int r12 = r5[r11];
        if (r12 < 0) goto L12;
        if (r12 > 16777216) goto L12;
        r9 = r9 + ((long) r12);
        r11 = r11 + 1;
    L12:
        throw new IOException("Invalid cache string");
    L14:
        if (r9 != r8) goto L20;
        byte[] r13 = new byte[r8];
        r17.readFully(r13);
        r2.blocks.add(r13);
        int r14 = 0;
        int r15 = 0;
    L16:
        if (r15 >= r7) goto L18;
        r2.offsets[r6 + r15] = r14;
        r2.lengths[r6 + r15] = r5[r15];
        r14 = r14 + r5[r15];
        r15 = r15 + 1;
        goto L16
    L18:
        r6 = r6 + 1024;
        goto L3
    L20:
        throw new IOException("Invalid dictionary block size");
    L21:
        return r2;
    }

    static Catalog readStartup(InputStream r6, long r7, long r9) throws IOException {
        return readInternal(r6, r7, r9, true);
    }

    private static String[] readStrings(Reader r16, int r17, int r18) throws IOException {
        Reader r0 = r16;
        String[] r2 = new String[r17 + 1];
        int r4 = 16777216;
        if (r18 != 5) goto L8;
        int r3 = 0;
    L5:
        if (r3 >= r17) goto L7;
        r2[r3] = r0.fast.utf8(count(r0, 16777216));
        r3 = r3 + 1;
        goto L5
    L7:
        return r2;
    L8:
        int r5 = 1024;
        byte[] r6 = new byte[Math.min(r17, 1024) * 4];
        int[] r7 = new int[r6.length / 4];
        byte[] r8 = new byte[262144];
        int r9 = 0;
    L9:
        if (r9 >= r17) goto L33;
        int r10 = Math.min(r5, r17 - r9);
        int r11 = count(r0, Integer.MAX_VALUE);
        r0.readFully(r6, 0, r10 * 4);
        ByteBuffer.wrap(r6, 0, r10 * 4).asIntBuffer().get(r7, 0, r10);
        long r14 = 0;
        int r12 = 0;
    L11:
        if (r12 >= r10) goto L19;
        int r13 = r7[r12];
        if (r13 < 0) goto L17;
        if (r13 > r4) goto L17;
        r14 = r14 + ((long) r13);
        r12 = r12 + 1;
        r4 = 16777216;
    L17:
        throw new IOException("Invalid cache string");
    L19:
        if (r14 != r11) goto L32;
        if (r11 <= r8.length) goto L23;
        r8 = new byte[r11];
    L23:
        r0.readFully(r8, 0, r11);
        int r15 = 0;
        int r19 = 0;
    L24:
        if (r19 >= r10) goto L30;
        int r20 = r7[r19];
        int r110 = r9 + r19;
        if (r20 != 0) goto L28;
        String r111 = "";
    L29:
        r2[r110] = r111;
        r15 = r15 + r20;
        r19 = r19 + 1;
        goto L24
    L28:
        r111 = new String(r8, r15, r20, StandardCharsets.UTF_8);
        goto L29
    L30:
        r9 = r9 + 1024;
        r5 = 1024;
        r4 = 16777216;
        r0 = r16;
        goto L9
    L32:
        throw new IOException("Invalid dictionary block size");
    L33:
        return r2;
    }

    private static String ref(Reader r3, TextTable r4) throws IOException {
        int r0 = r3.number();
        if (r0 != (-1)) goto L6;
        return null;
    L6:
        if (r0 < 0) goto L12;
        if (r0 >= r4.size()) goto L12;
        return r4.getForReader(r0);
    L12:
        throw new IOException("Invalid dictionary reference");
    }

    private static String text(Reader r4) throws IOException {
        int r0 = r4.number();
        if (r0 != (-1)) goto L7;
        return null;
    L7:
        if (r0 != (-2)) goto L16;
        int r1 = r4.number();
        if (r1 < 0) goto L15;
        if (r1 >= r4.strings.size()) goto L15;
        return r4.strings.get(r1);
    L15:
        throw new IOException("Invalid cache reference");
    L16:
        if (r0 < 0) goto L22;
        if (r0 > 16777216) goto L22;
        byte[] r2 = new byte[r0];
        r4.readFully(r2);
        String r3 = new String(r2, StandardCharsets.UTF_8);
        r4.strings.add(r3);
        return r3;
    L22:
        throw new IOException("Invalid cache string");
    }

    private static void text(Writer r3, String r4) throws IOException {
        if (r4 != null) goto L5;
        r3.writeInt(-1);
        return;
    L5:
        Integer r0 = r3.strings.get(r4);
        if (r0 == null) goto L9;
        r3.writeInt(-2);
        r3.writeInt(r0.intValue());
        return;
    L9:
        r3.strings.put(r4, Integer.valueOf(r3.strings.size()));
        byte[] r1 = r4.getBytes(StandardCharsets.UTF_8);
        r3.writeInt(r1.length);
        r3.write(r1);
    }

    static void write(OutputStream r8, Catalog r9, long r10, long r12) throws IOException {
        writePrepared(r8, r9, r10, r12, null, null);
    }

    static void writePrepared(OutputStream r26, Catalog r27, long r28, long r30, Map<Entry, String> r32, Map<String, Integer> r33) throws IOException {
        Dictionary r2 = new Dictionary(null);
        Iterator<Catalog.Source> r3 = r27.sources.iterator();
    L4:
        if (r3.hasNext() == false) goto L18;
        Catalog.Source r4 = r3.next();
        String[] r7 = {r4.f177id, r4.name, r4.origin, r4.kind, r4.originalFilename, r4.provider};
        int r8 = 0;
    L6:
        if (r8 >= 6) goto L8;
        r2.m391id(r7[r8]);
        r8 = r8 + 1;
        goto L6
    L8:
        Iterator<Entry> r5 = r4.entries.iterator();
    L10:
        if (r5.hasNext() == false) goto L4;
        Entry r9 = r5.next();
        String[] r10 = fields(r9);
        int r11 = r10.length;
        int r12 = 0;
    L12:
        if (r12 >= r11) goto L14;
        r2.m391id(r10[r12]);
        r12 = r12 + 1;
        goto L12
    L14:
        r2.headers(r9.headers);
        r2.headers(r9.licenseHeaders);
        if (r32 == null) goto L10;
        r2.m391id(r32.get(r9));
        goto L10
    L18:
        if (r33 == null) goto L23;
        Iterator<String> r6 = r33.keySet().iterator();
    L21:
        if (r6.hasNext() == false) goto L23;
        r2.m391id(r6.next());
    L23:
        DataOutputStream r13 = new DataOutputStream(r26);
        if (r32 == null) goto L27;
        if (r33 == null) goto L27;
        boolean r14 = true;
    L28:
        r13.writeInt(MAGIC);
        r13.writeInt(6);
        r13.writeBoolean(r14);
        r13.writeLong(r28);
        r13.writeLong(r30);
        r13.writeInt(r2.strings.size());
        ArrayList r15 = new ArrayList(r2.strings.keySet());
        int r16 = 0;
    L30:
        if (r16 >= r15.size()) goto L45;
        int r17 = Math.min(1024, r15.size() - r16);
        byte[][] r18 = new byte[r17][];
        boolean r19 = r14;
        long r20 = 0;
        int r21 = 0;
    L32:
        if (r21 >= r17) goto L35;
        int r110 = r21;
        r18[r110] = ((String) r15.get(r16 + r21)).getBytes(StandardCharsets.UTF_8);
        r20 = r20 + ((long) r18[r110].length);
        r21 = r110 + 1;
        goto L32
    L35:
        if (r20 > 2147483647L) goto L44;
        r13.writeInt((int) r20);
        int r22 = r18.length;
        int r23 = 0;
    L37:
        if (r23 >= r22) goto L39;
        r13.writeInt(r18[r23].length);
        r23 = r23 + 1;
        r22 = r22;
        goto L37
    L39:
        int r24 = r18.length;
        int r25 = 0;
    L40:
        if (r25 >= r24) goto L42;
        r13.write(r18[r25]);
        r25 = r25 + 1;
        r24 = r24;
        goto L40
    L42:
        r16 = r16 + 1024;
        r14 = r19;
        goto L30
    L44:
        throw new IOException("Dictionary block too large");
    L45:
        boolean r111 = r14;
        r13.writeInt(r2.maps.size());
        Iterator<Map<String, String>> r29 = r2.maps.keySet().iterator();
    L47:
        if (r29.hasNext() == false) goto L53;
        Map<String, String> r31 = r29.next();
        r13.writeInt(r31.size());
        Iterator<Map.Entry<String, String>> r34 = r31.entrySet().iterator();
    L50:
        if (r34.hasNext() == false) goto L47;
        Map.Entry<String, String> r35 = r34.next();
        r13.writeInt(r2.m391id(r35.getKey()));
        r13.writeInt(r2.m391id(r35.getValue()));
        goto L50
    L53:
        r13.writeInt(r27.sources.size());
        Iterator<Catalog.Source> r36 = r27.sources.iterator();
    L55:
        if (r36.hasNext() == false) goto L71;
        Catalog.Source r37 = r36.next();
        String[] r0 = {r37.f177id, r37.name, r37.origin, r37.kind, r37.originalFilename, r37.provider};
        int r38 = 0;
    L58:
        if (r38 >= 6) goto L60;
        r13.writeInt(r2.m391id(r0[r38]));
        r38 = r38 + 1;
        goto L58
    L60:
        r13.writeInt(r37.entries.size());
        Iterator<Entry> r1 = r37.entries.iterator();
    L62:
        if (r1.hasNext() == false) goto L55;
        Entry r39 = r1.next();
        String[] r112 = fields(r39);
        int r113 = r112.length;
        int r114 = 0;
    L64:
        if (r114 >= r113) goto L66;
        r13.writeInt(r2.m391id(r112[r114]));
        r114 = r114 + 1;
        r1 = r1;
        goto L64
    L66:
        Iterator<Entry> r115 = r1;
        r13.writeInt(r39.season);
        r13.writeInt(r39.episode);
        r13.writeInt(r2.headers(r39.headers));
        r13.writeInt(r2.headers(r39.licenseHeaders));
        if (r111 == false) goto L69;
        r13.writeInt(r2.m391id(r32.get(r39)));
    L69:
        r1 = r115;
        goto L62
    L71:
        if (r111 == false) goto L76;
        r13.writeInt(r33.size());
        Iterator<Map.Entry<String, Integer>> r40 = r33.entrySet().iterator();
    L74:
        if (r40.hasNext() == false) goto L76;
        Map.Entry<String, Integer> r41 = r40.next();
        r13.writeInt(r2.m391id(r41.getKey()));
        r13.writeInt(r41.getValue().intValue());
    L76:
        r13.flush();
        return;
    L27:
        r14 = false;
        goto L28
    }
}
