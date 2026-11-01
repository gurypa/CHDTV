package androidx.media3.datasource.cache;

import android.os.ConditionVariable;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.database.DatabaseIOException;
import androidx.media3.database.DatabaseProvider;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes.dex */
public final class SimpleCache implements Cache {
    private static final int SUBDIRECTORY_COUNT = 10;
    private static final String TAG = "SimpleCache";
    private static final String UID_FILE_SUFFIX = ".uid";
    private static final HashSet<File> lockedCacheDirs = null;
    private final File cacheDir;
    private final CachedContentIndex contentIndex;
    private final CacheEvictor evictor;
    private final CacheFileMetadataIndex fileIndex;
    private Cache.CacheException initializationException;
    private final HashMap<String, ArrayList<Cache.Listener>> listeners;
    private final Random random;
    private boolean released;
    private long totalSpace;
    private final boolean touchCacheSpans;
    private long uid;

    static {
        lockedCacheDirs = new HashSet();
    }

    @Deprecated
    public SimpleCache(File r8, CacheEvictor r9) {
        this(r8, r9, null, null, false, true);
    }

    public SimpleCache(File r8, CacheEvictor r9, DatabaseProvider r10) {
        this(r8, r9, r10, null, false, false);
    }

    public SimpleCache(File r7, CacheEvictor r8, DatabaseProvider r9, byte[] r10, boolean r11, boolean r12) {
        CachedContentIndex r0 = new CachedContentIndex(r9, r7, r10, r11, r12);
        if (r9 == null) goto L6;
        if (r12 == true) goto L6;
        CacheFileMetadataIndex r13 = new CacheFileMetadataIndex(r9);
    L7:
        this(r7, r8, r0, r13);
        return;
    L6:
        r13 = null;
        goto L7
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [androidx.media3.datasource.cache.SimpleCache$1] */
    SimpleCache(File r4, CacheEvictor r5, CachedContentIndex r6, CacheFileMetadataIndex r7) {
        if (lockFolder(r4) == false) goto L7;
        this.cacheDir = r4;
        this.evictor = r5;
        this.contentIndex = r6;
        this.fileIndex = r7;
        this.listeners = new HashMap();
        this.random = new Random();
        this.touchCacheSpans = r5.requiresCacheSpanTouches();
        this.uid = -1;
        final ConditionVariable r0 = new ConditionVariable();
        final String r2 = "ExoPlayer:SimpleCacheInit";
        new C02181(this, r2, r0).start();
        r0.block();
        return;
    L7:
        throw new IllegalStateException("Another SimpleCache instance uses the folder: " + r4);
    }

    static /* synthetic */ void access$000(SimpleCache r0) {
        r0.initialize();
    }

    static /* synthetic */ CacheEvictor access$100(SimpleCache r1) {
        return r1.evictor;
    }

    private void addSpan(SimpleCacheSpan r5) {
        this.contentIndex.getOrAdd(r5.key).addSpan(r5);
        this.totalSpace += r5.length;
        notifySpanAdded(r5);
    }

    private static void createCacheDirectories(File r2) throws Cache.CacheException {
        if (r2.mkdirs() == false) goto L5;
        return;
    L5:
        if (r2.isDirectory() == true) goto L10;
        String r0 = "Failed to create cache directory: " + r2;
        Log.m44e(TAG, r0);
        throw new Cache.CacheException(r0);
    }

    private static long createUid(File r7) throws IOException {
        long r0 = new SecureRandom().nextLong();
        if (r0 != Long.MIN_VALUE) goto L5;
        long r2 = 0;
    L6:
        File r1 = new File(r7, Long.toString(r2, 16) + UID_FILE_SUFFIX);
        if (r1.createNewFile() == false) goto L10;
        return r2;
    L10:
        throw new IOException("Failed to create UID file: " + r1);
    L5:
        r2 = Math.abs(r0);
        goto L6
    }

    public static void delete(File r7, DatabaseProvider r8) {
        if (r7.exists() == true) goto L5;
        return;
    L5:
        File[] r2 = r7.listFiles();
        if (r2 != null) goto L9;
        r7.delete();
        return;
    L9:
        if (r8 == null) goto L20;
        long r3 = loadUid(r2);
        if (r3 == (-1)) goto L20;
        CacheFileMetadataIndex.delete(r8, r3);     // Catch: DatabaseIOException -> L14
    L22:
        CachedContentIndex.delete(r8, r3);     // Catch: DatabaseIOException -> L18
    L19:
        Log.m48w(TAG, "Failed to delete file metadata: " + r3);
    L15:
        Log.m48w(TAG, "Failed to delete file metadata: " + r3);
    L20:
        Util.recursiveDelete(r7);
    }

    private SimpleCacheSpan getSpan(String r7, long r8, long r10) {
        CachedContent r0 = this.contentIndex.get(r7);
        if (r0 == null) goto L5;
    L6:
        SimpleCacheSpan r1 = r0.getSpan(r8, r10);
        if (r1.isCached == false) goto L11;
        if (((File) Assertions.checkNotNull(r1.file)).length() == r1.length) goto L11;
        removeStaleSpans();
    L11:
        return r1;
    L5:
        return SimpleCacheSpan.createHole(r7, r8, r10);
    }

    private void initialize() {
        if (this.cacheDir.exists() == false) goto L36;
    L9:
        File[] r0 = this.cacheDir.listFiles();
        if (r0 != null) goto L13;
        String r2 = "Failed to list cache directory files: " + this.cacheDir;
        Log.m44e(TAG, r2);
        this.initializationException = new Cache.CacheException(r2);
        return;
    L13:
        this.uid = loadUid(r0);
        if (this.uid == (-1)) goto L34;
    L33:
        this.contentIndex.initialize(this.uid);     // Catch: IOException -> L30
        if (this.fileIndex == null) goto L23;
        this.fileIndex.initialize(this.uid);     // Catch: IOException -> L30
        Map<String, CacheFileMetadata> r3 = this.fileIndex.getAll();     // Catch: IOException -> L30
        loadDirectory(this.cacheDir, true, r0, r3);     // Catch: IOException -> L30
        this.fileIndex.removeAll(r3.keySet());     // Catch: IOException -> L30
    L24:
        this.contentIndex.removeEmpty();
        this.contentIndex.store();     // Catch: IOException -> L27
        return;
    L27:
        e = move-exception;
        Log.m45e(TAG, "Storing index file failed", e);
        return;
    L23:
        loadDirectory(this.cacheDir, true, r0, null);     // Catch: IOException -> L30
    L30:
        e = move-exception;
        String r4 = "Failed to initialize cache indices: " + this.cacheDir;
        Log.m45e(TAG, r4, e);
        this.initializationException = new Cache.CacheException(r4, e);
        return;
    L34:
        this.uid = createUid(this.cacheDir);     // Catch: IOException -> L17
    L17:
        e = move-exception;
        String r5 = "Failed to create cache UID: " + this.cacheDir;
        Log.m45e(TAG, r5, e);
        this.initializationException = new Cache.CacheException(r5, e);
        return;
    L36:
        createCacheDirectories(this.cacheDir);     // Catch: Cache.CacheException -> L6
    L6:
        e = move-exception;
        this.initializationException = e;
    }

    public static synchronized boolean isCacheFolderLocked(File r3) {
        monitor-enter(SimpleCache.class);
        boolean r1 = lockedCacheDirs.contains(r3.getAbsoluteFile());     // Catch: Throwable -> L7
        monitor-exit(SimpleCache.class);
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    private void loadDirectory(File r12, boolean r13, File[] r14, Map<String, CacheFileMetadata> r15) {
        if (r14 != null) goto L4;
    L32:
        if (r13 == true) goto L41;
        r12.delete();
        return;
    L41:
        return;
    L4:
        if (r14.length == 0) goto L32;
        int r0 = r14.length;
        int r2 = 0;
    L7:
        if (r2 >= r0) goto L31;
        File r3 = r14[r2];
        String r9 = r3.getName();
        if (r13 == true) goto L11;
    L13:
        if (r13 == true) goto L15;
    L19:
        long r4 = -1;
        long r6 = C0180C.TIME_UNSET;
        if (r15 == null) goto L22;
        CacheFileMetadata r8 = r15.remove(r9);
    L23:
        CacheFileMetadata r10 = r8;
        if (r10 == null) goto L26;
        r4 = r10.length;
        r6 = r10.lastTouchTimestamp;
    L26:
        SimpleCacheSpan r11 = SimpleCacheSpan.createCacheEntry(r3, r4, r6, this.contentIndex);
        if (r11 == null) goto L29;
        addSpan(r11);
    L30:
        r2 = r2 + 1;
        goto L7
    L29:
        r3.delete();
        goto L30
    L22:
        r8 = null;
        goto L23
    L15:
        if (CachedContentIndex.isIndexFile(r9) == true) goto L30;
        if (r9.endsWith(UID_FILE_SUFFIX) == false) goto L19;
    L11:
        if (r9.indexOf(46) != (-1)) goto L13;
        loadDirectory(r3, false, r3.listFiles(), r15);
        goto L30
    }

    private static long loadUid(File[] r7) {
        int r0 = r7.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L11;
        File r2 = r7[r1];
        String r3 = r2.getName();
        if (r3.endsWith(UID_FILE_SUFFIX) == false) goto L10;
        return parseUid(r3);
    L9:
        Log.m44e(TAG, "Malformed UID file: " + r2);
        r2.delete();
    L10:
        r1 = r1 + 1;
        goto L3
    L11:
        return -1;
    }

    private static synchronized boolean lockFolder(File r3) {
        monitor-enter(SimpleCache.class);
        boolean r1 = lockedCacheDirs.add(r3.getAbsoluteFile());     // Catch: Throwable -> L7
        monitor-exit(SimpleCache.class);
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    private void notifySpanAdded(SimpleCacheSpan r4) {
        ArrayList<Cache.Listener> r0 = this.listeners.get(r4.key);
        if (r0 == null) goto L7;
        int r1 = r0.size() - 1;
    L5:
        if (r1 < 0) goto L7;
        r0.get(r1).onSpanAdded(this, r4);
        r1 = r1 - 1;
    L7:
        this.evictor.onSpanAdded(this, r4);
    }

    private void notifySpanRemoved(CacheSpan r4) {
        ArrayList<Cache.Listener> r0 = this.listeners.get(r4.key);
        if (r0 == null) goto L7;
        int r1 = r0.size() - 1;
    L5:
        if (r1 < 0) goto L7;
        r0.get(r1).onSpanRemoved(this, r4);
        r1 = r1 - 1;
    L7:
        this.evictor.onSpanRemoved(this, r4);
    }

    private void notifySpanTouched(SimpleCacheSpan r4, CacheSpan r5) {
        ArrayList<Cache.Listener> r0 = this.listeners.get(r4.key);
        if (r0 == null) goto L7;
        int r1 = r0.size() - 1;
    L5:
        if (r1 < 0) goto L7;
        r0.get(r1).onSpanTouched(this, r4, r5);
        r1 = r1 - 1;
    L7:
        this.evictor.onSpanTouched(this, r4, r5);
    }

    private static long parseUid(String r2) {
        return Long.parseLong(r2.substring(0, r2.indexOf(46)), 16);
    }

    private void removeSpanInternal(CacheSpan r6) {
        CachedContent r0 = this.contentIndex.get(r6.key);
        if (r0 != null) goto L5;
        return;
    L5:
        if (r0.removeSpan(r6) == false) goto L19;
        this.totalSpace -= r6.length;
        if (this.fileIndex == null) goto L14;
        String r1 = ((File) Assertions.checkNotNull(r6.file)).getName();
        this.fileIndex.remove(r1);     // Catch: IOException -> L12
    L13:
        Log.m48w(TAG, "Failed to remove file index entry for: " + r1);
    L14:
        this.contentIndex.maybeRemove(r0.key);
        notifySpanRemoved(r6);
        return;
    }

    private void removeStaleSpans() {
        ArrayList r0 = new ArrayList();
        Iterator<CachedContent> r1 = this.contentIndex.getAll().iterator();
    L4:
        if (r1.hasNext() == false) goto L13;
        Iterator<SimpleCacheSpan> r3 = r1.next().getSpans().iterator();
    L7:
        if (r3.hasNext() == false) goto L4;
        SimpleCacheSpan r4 = r3.next();
        if (((File) Assertions.checkNotNull(r4.file)).length() == r4.length) goto L7;
        r0.add(r4);
        goto L7
    L13:
        int r2 = 0;
    L15:
        if (r2 >= r0.size()) goto L17;
        removeSpanInternal((CacheSpan) r0.get(r2));
        r2 = r2 + 1;
        goto L15
    }

    private SimpleCacheSpan touchSpan(String r10, SimpleCacheSpan r11) {
        if (this.touchCacheSpans == true) goto L5;
        return r11;
    L5:
        String r2 = ((File) Assertions.checkNotNull(r11.file)).getName();
        long r3 = r11.length;
        long r5 = System.currentTimeMillis();
        boolean r7 = false;
        if (this.fileIndex != null) goto L15;
        r7 = true;
    L13:
        SimpleCacheSpan r0 = ((CachedContent) Assertions.checkNotNull(this.contentIndex.get(r10))).setLastTouchTimestamp(r11, r5, r7);
        notifySpanTouched(r11, r0);
        return r0;
    L15:
        this.fileIndex.set(r2, r3, r5);     // Catch: IOException -> L9
    L10:
        Log.m48w(TAG, "Failed to update index with new touch timestamp.");
        goto L13
    }

    private static synchronized void unlockFolder(File r3) {
        monitor-enter(SimpleCache.class);
        lockedCacheDirs.remove(r3.getAbsoluteFile());     // Catch: Throwable -> L7
        monitor-exit(SimpleCache.class);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized NavigableSet<CacheSpan> addListener(String r3, Cache.Listener r4) {
        monitor-enter(this);
    L13:
        th = move-exception;
        throw th;
    L4:
        if (this.released == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L13
        Assertions.checkNotNull(r3);     // Catch: Throwable -> L13
        Assertions.checkNotNull(r4);     // Catch: Throwable -> L13
        ArrayList<Cache.Listener> r1 = this.listeners.get(r3);     // Catch: Throwable -> L13
        if (r1 != null) goto L10;
        r1 = new ArrayList();     // Catch: Throwable -> L13
        this.listeners.put(r3, r1);     // Catch: Throwable -> L13
    L10:
        r1.add(r4);     // Catch: Throwable -> L13
        NavigableSet<CacheSpan> r2 = getCachedSpans(r3);     // Catch: Throwable -> L13
        monitor-exit(this);
        return r2;
    L6:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized void applyContentMetadataMutations(String r3, ContentMetadataMutations r4) throws Cache.CacheException {
        monitor-enter(this);
    L14:
        th = move-exception;
        throw th;
    L4:
        if (this.released == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L14
        checkInitialization();     // Catch: Throwable -> L14
        this.contentIndex.applyContentMetadataMutations(r3, r4);     // Catch: Throwable -> L14
        this.contentIndex.store();     // Catch: IOException -> L11 Throwable -> L14
        monitor-exit(this);
        return;
    L11:
        e = move-exception;
        throw new Cache.CacheException(e);     // Catch: Throwable -> L14
    L6:
        r0 = false;
        goto L7
    }

    public synchronized void checkInitialization() throws Cache.CacheException {
        monitor-enter(this);
    L9:
        th = move-exception;
        throw th;
    L4:
        if (this.initializationException != null) goto L8;
        monitor-exit(this);
        return;
    L8:
        throw this.initializationException;     // Catch: Throwable -> L9
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized void commitFile(File r18, long r19) throws Cache.CacheException {
        monitor-enter(this);
        boolean r4 = true;
        if (this.released == true) goto L7;
        boolean r0 = true;
    L8:
        Assertions.checkState(r0);     // Catch: Throwable -> L41
        if (r18.exists() == true) goto L13;
        monitor-exit(this);
        return;
    L13:
        if (r19 != 0) goto L17;
        r18.delete();     // Catch: Throwable -> L41
        monitor-exit(this);
        return;
    L17:
        SimpleCacheSpan r1 = (SimpleCacheSpan) Assertions.checkNotNull(SimpleCacheSpan.createCacheEntry(r18, r19, this.contentIndex));     // Catch: Throwable -> L41
        CachedContent r2 = (CachedContent) Assertions.checkNotNull(this.contentIndex.get(r1.key));     // Catch: Throwable -> L41
        Assertions.checkState(r2.isFullyLocked(r1.position, r1.length));     // Catch: Throwable -> L41
        long r9 = ContentMetadata.CC.getContentLength(r2.getMetadata());     // Catch: Throwable -> L41
        if (r9 == (-1)) goto L25;
        if ((r1.position + r1.length) <= r9) goto L23;
        r4 = false;
    L23:
        Assertions.checkState(r4);     // Catch: Throwable -> L41
    L25:
        if (this.fileIndex != null) goto L47;
    L33:
        addSpan(r1);     // Catch: Throwable -> L41
        this.contentIndex.store();     // Catch: IOException -> L38 Throwable -> L41
        notifyAll();     // Catch: Throwable -> L41
        monitor-exit(this);
        return;
    L38:
        e = move-exception;
        throw new Cache.CacheException(e);     // Catch: Throwable -> L41
    L47:
        this.fileIndex.set(r18.getName(), r1.length, r1.lastTouchTimestamp);     // Catch: IOException -> L30 Throwable -> L41
    L30:
        e = move-exception;
        throw new Cache.CacheException(e);     // Catch: Throwable -> L41
    L7:
        r0 = false;
    L41:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized long getCacheSpace() {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (this.released == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L10
        long r1 = this.totalSpace;     // Catch: Throwable -> L10
        monitor-exit(this);
        return r1;
    L6:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized long getCachedBytes(String r15, long r16, long r18) {
        monitor-enter(this);
        if (r18 != (-1)) goto L6;
        long r0 = Long.MAX_VALUE;
    L8:
        if (r0 >= 0) goto L10;
        r0 = Long.MAX_VALUE;
    L10:
        long r6 = 0;
        long r10 = r16;
    L12:
        if (r10 >= r0) goto L23;
        long r4 = getCachedLength(r15, r10, r0 - r10);     // Catch: Throwable -> L19
        if (r4 <= 0) goto L17;
        r6 = r6 + r4;     // Catch: Throwable -> L19
    L18:
        r10 = r10 + r4;     // Catch: Throwable -> L19
        goto L12
    L17:
        r4 = -r4;
    L19:
        th = move-exception;
        throw th;
    L23:
        monitor-exit(this);
        return r6;
    L6:
        r0 = r16 + r18;
        goto L8
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized long getCachedLength(String r4, long r5, long r7) {
        monitor-enter(this);
    L17:
        th = move-exception;
        throw th;
    L4:
        if (this.released == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L17
        if (r7 != (-1)) goto L10;
        r7 = Long.MAX_VALUE;
    L10:
        CachedContent r1 = this.contentIndex.get(r4);     // Catch: Throwable -> L17
        if (r1 == null) goto L14;
        long r2 = r1.getCachedBytesLength(r5, r7);     // Catch: Throwable -> L17
    L15:
        monitor-exit(this);
        return r2;
    L14:
        r2 = -r7;
        goto L15
    L6:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized NavigableSet<CacheSpan> getCachedSpans(String r4) {
        monitor-enter(this);
    L16:
        th = move-exception;
        throw th;
    L4:
        if (this.released == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L16
        CachedContent r1 = this.contentIndex.get(r4);     // Catch: Throwable -> L16
        if (r1 != null) goto L10;
    L13:
        TreeSet r2 = new TreeSet();     // Catch: Throwable -> L16
    L14:
        monitor-exit(this);
        return r2;
    L10:
        if (r1.isEmpty() == true) goto L13;
        r2 = new TreeSet(r1.getSpans());     // Catch: Throwable -> L16
        goto L14
    L6:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized ContentMetadata getContentMetadata(String r2) {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (this.released == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L10
        ContentMetadata r1 = this.contentIndex.getContentMetadata(r2);     // Catch: Throwable -> L10
        monitor-exit(this);
        return r1;
    L6:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized Set<String> getKeys() {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (this.released == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L10
        HashSet r1 = new HashSet(this.contentIndex.getKeys());     // Catch: Throwable -> L10
        monitor-exit(this);
        return r1;
    L6:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized long getUid() {
        monitor-enter(this);
        long r0 = this.uid;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized boolean isCached(String r6, long r7, long r9) {
        monitor-enter(this);
        boolean r1 = true;
        if (this.released == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L16
        CachedContent r2 = this.contentIndex.get(r6);     // Catch: Throwable -> L16
        if (r2 == null) goto L13;
        if (r2.getCachedBytesLength(r7, r9) < r9) goto L13;
    L14:
        monitor-exit(this);
        return r1;
    L13:
        r1 = false;
        goto L14
    L6:
        r0 = false;
    L16:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized void release() {
        monitor-enter(this);
    L21:
        th = move-exception;
        throw th;
    L4:
        if (this.released == false) goto L7;
        monitor-exit(this);
        return;
    L7:
        this.listeners.clear();     // Catch: Throwable -> L21
        removeStaleSpans();     // Catch: Throwable -> L21
        this.contentIndex.store();     // Catch: Throwable -> L12 IOException -> L14
        unlockFolder(this.cacheDir);     // Catch: Throwable -> L21
        this.released = true;     // Catch: Throwable -> L21
    L17:
        monitor-exit(this);
        return;
    L14:
        e = move-exception;
        Log.m45e(TAG, "Storing index file failed", e);     // Catch: Throwable -> L12
        unlockFolder(this.cacheDir);     // Catch: Throwable -> L21
        this.released = true;     // Catch: Throwable -> L21
        goto L17
    L12:
        th = move-exception;
        unlockFolder(this.cacheDir);     // Catch: Throwable -> L21
        this.released = true;     // Catch: Throwable -> L21
        throw th;     // Catch: Throwable -> L21
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized void releaseHoleSpan(CacheSpan r4) {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (this.released == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L10
        CachedContent r1 = (CachedContent) Assertions.checkNotNull(this.contentIndex.get(r4.key));     // Catch: Throwable -> L10
        r1.unlockRange(r4.position);     // Catch: Throwable -> L10
        this.contentIndex.maybeRemove(r1.key);     // Catch: Throwable -> L10
        notifyAll();     // Catch: Throwable -> L10
        monitor-exit(this);
        return;
    L6:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized void removeListener(String r3, Cache.Listener r4) {
        monitor-enter(this);
    L14:
        th = move-exception;
        throw th;
    L4:
        if (this.released == false) goto L7;
        monitor-exit(this);
        return;
    L7:
        ArrayList<Cache.Listener> r0 = this.listeners.get(r3);     // Catch: Throwable -> L14
        if (r0 == null) goto L12;
        r0.remove(r4);     // Catch: Throwable -> L14
        if (r0.isEmpty() == false) goto L12;
        this.listeners.remove(r3);     // Catch: Throwable -> L14
    L12:
        monitor-exit(this);
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized void removeResource(String r3) {
        monitor-enter(this);
    L14:
        th = move-exception;
        throw th;
    L4:
        if (this.released == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L14
        Iterator<CacheSpan> r1 = getCachedSpans(r3).iterator();     // Catch: Throwable -> L14
    L9:
        if (r1.hasNext() == false) goto L12;
        removeSpanInternal(r1.next());     // Catch: Throwable -> L14
        goto L9
    L12:
        monitor-exit(this);
        return;
    L6:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized void removeSpan(CacheSpan r2) {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (this.released == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L10
        removeSpanInternal(r2);     // Catch: Throwable -> L10
        monitor-exit(this);
        return;
    L6:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized File startFile(String r11, long r12, long r14) throws Cache.CacheException {
        monitor-enter(this);
    L22:
        th = th;
    L24:
        Throwable r13 = th;
    L25:
        monitor-exit(this);     // Catch: Throwable -> L27
        throw r13;
    L4:
        if (this.released == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L22
        checkInitialization();     // Catch: Throwable -> L22
        CachedContent r1 = this.contentIndex.get(r11);     // Catch: Throwable -> L22
        Assertions.checkNotNull(r1);     // Catch: Throwable -> L22
        Assertions.checkState(r1.isFullyLocked(r12, r14));     // Catch: Throwable -> L22
        if (this.cacheDir.exists() == false) goto L32;
    L13:
        this.evictor.onStartFile(this, r11, r12, r14);     // Catch: Throwable -> L27
        File r15 = new File(this.cacheDir, Integer.toString(this.random.nextInt(10)));     // Catch: Throwable -> L27
        if (r15.exists() == true) goto L18;
        createCacheDirectories(r15);     // Catch: Throwable -> L27
    L18:
        long r8 = System.currentTimeMillis();     // Catch: Throwable -> L27
        File r16 = SimpleCacheSpan.getCacheFile(r15, r1.f33id, r12, r8);     // Catch: Throwable -> L27
        monitor-exit(this);
        return r16;
    L32:
        createCacheDirectories(this.cacheDir);     // Catch: Throwable -> L11
        removeStaleSpans();     // Catch: Throwable -> L11
    L11:
        th = move-exception;
        r13 = th;
        goto L25
    L6:
        r0 = false;
    L27:
        th = th;
        goto L24
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:? -> B:19:0x0026). Please report as a decompilation issue!!! */
    @Override // androidx.media3.datasource.cache.Cache
    public synchronized CacheSpan startReadWrite(String r4, long r5, long r7) throws InterruptedException, Cache.CacheException {
        monitor-enter(this);
    L14:
        th = th;
    L16:
        Throwable r6 = th;
        monitor-exit(this);     // Catch: Throwable -> L19
        throw r6;
    L19:
        th = th;
        goto L16
    L4:
        if (this.released == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L14
        checkInitialization();     // Catch: Throwable -> L14
    L8:
        CacheSpan r1 = startReadWriteNonBlocking(r4, r5, r7);     // Catch: Throwable -> L14
        long r2 = r7;
        long r8 = r5;
        String r9 = r4;
        if (r1 != null) goto L11;
        wait();     // Catch: Throwable -> L19
        r4 = r9;
        r5 = r8;
        r7 = r2;
        goto L8
    L11:
        monitor-exit(this);
        return r1;
    L6:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.datasource.cache.Cache
    public synchronized CacheSpan startReadWriteNonBlocking(String r6, long r7, long r9) throws Cache.CacheException {
        monitor-enter(this);
    L21:
        th = th;
    L23:
        Throwable r8 = th;
        monitor-exit(this);     // Catch: Throwable -> L26
        throw r8;
    L4:
        if (this.released == true) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L21
        checkInitialization();     // Catch: Throwable -> L21
        SimpleCacheSpan r1 = getSpan(r6, r7, r9);     // Catch: Throwable -> L21
        if (r1.isCached == false) goto L15;
        SimpleCacheSpan r10 = touchSpan(r6, r1);     // Catch: Throwable -> L26
        monitor-exit(this);
        return r10;
    L15:
        if (this.contentIndex.getOrAdd(r6).lockRange(r7, r1.length) == false) goto L18;
        monitor-exit(this);
        return r1;
    L18:
        monitor-exit(this);
        return null;
    L6:
        r0 = false;
    L26:
        th = th;
        goto L23
    }
}
