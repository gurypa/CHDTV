package androidx.media3.exoplayer.upstream;

import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.cache.Cache;
import androidx.media3.datasource.cache.CacheSpan;
import androidx.media3.extractor.ChunkIndex;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes.dex */
public final class CachedRegionTracker implements Cache.Listener {
    public static final int CACHED_TO_END = -2;
    public static final int NOT_CACHED = -1;
    private static final String TAG = "CachedRegionTracker";
    private final Cache cache;
    private final String cacheKey;
    private final ChunkIndex chunkIndex;
    private final Region lookupRegion;
    private final TreeSet<Region> regions;

    private static class Region implements Comparable<Region> {
        public long endOffset;
        public int endOffsetIndex;
        public long startOffset;

        public Region(long r1, long r3) {
            this.startOffset = r1;
            this.endOffset = r3;
        }

        /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
        public int compareTo2(Region r5) {
            return Util.compareLong(this.startOffset, r5.startOffset);
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(Region r1) {
            return compareTo2(r1);
        }
    }

    public CachedRegionTracker(Cache r4, String r5, ChunkIndex r6) {
        this.cache = r4;
        this.cacheKey = r5;
        this.chunkIndex = r6;
        this.regions = new TreeSet();
        this.lookupRegion = new Region(0, 0);
        monitor-enter(this);
        Iterator<CacheSpan> r1 = r4.addListener(r5, this).descendingIterator();     // Catch: Throwable -> L10
    L6:
        if (r1.hasNext() == false) goto L8;
        mergeSpan(r1.next());     // Catch: Throwable -> L10
        goto L6
    L8:
        monitor-exit(this);     // Catch: Throwable -> L10
        return;
    L10:
        th = move-exception;
        throw th;
    }

    private void mergeSpan(CacheSpan r12) {
        Region r0 = new Region(r12.position, r12.position + r12.length);
        Region r1 = this.regions.floor(r0);
        Region r2 = this.regions.ceiling(r0);
        boolean r3 = regionsConnect(r1, r0);
        if (regionsConnect(r0, r2) == false) goto L8;
        if (r3 == false) goto L6;
        r1.endOffset = r2.endOffset;
        r1.endOffsetIndex = r2.endOffsetIndex;
    L7:
        this.regions.remove(r2);
        return;
    L6:
        r0.endOffset = r2.endOffset;
        r0.endOffsetIndex = r2.endOffsetIndex;
        this.regions.add(r0);
        goto L7
    L8:
        if (r3 == false) goto L16;
        r1.endOffset = r0.endOffset;
        int r5 = r1.endOffsetIndex;
    L11:
        if (r5 >= (this.chunkIndex.length - 1)) goto L15;
        if (this.chunkIndex.offsets[r5 + 1] > r1.endOffset) goto L15;
        r5 = r5 + 1;
    L15:
        r1.endOffsetIndex = r5;
        return;
    L16:
        int r6 = Arrays.binarySearch(this.chunkIndex.offsets, r0.endOffset);
        if (r6 >= 0) goto L19;
        int r7 = (-r6) - 2;
    L20:
        r0.endOffsetIndex = r7;
        this.regions.add(r0);
        return;
    L19:
        r7 = r6;
        goto L20
    }

    private boolean regionsConnect(Region r5, Region r6) {
        if (r5 == null) goto L7;
        if (r6 == null) goto L7;
        if (r5.endOffset != r6.startOffset) goto L7;
        return true;
    L7:
        return false;
    }

    public synchronized int getRegionEndTimeMs(long r10) {
        monitor-enter(this);
        this.lookupRegion.startOffset = r10;     // Catch: Throwable -> L24
        Region r0 = this.regions.floor(this.lookupRegion);     // Catch: Throwable -> L24
        if (r0 != null) goto L6;
    L22:
        monitor-exit(this);
        return -1;
    L6:
        if (r10 > r0.endOffset) goto L22;
        if (r0.endOffsetIndex == (-1)) goto L22;
        int r1 = r0.endOffsetIndex;     // Catch: Throwable -> L24
        if (r1 == (this.chunkIndex.length - 1)) goto L14;
    L18:
        int r2 = (int) ((this.chunkIndex.timesUs[r1] + ((this.chunkIndex.durationsUs[r1] * (r0.endOffset - this.chunkIndex.offsets[r1])) / ((long) this.chunkIndex.sizes[r1]))) / 1000);
        monitor-exit(this);
        return r2;
    L14:
        if (r0.endOffset != (this.chunkIndex.offsets[r1] + ((long) this.chunkIndex.sizes[r1]))) goto L18;
        monitor-exit(this);
        return -2;
    L24:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.datasource.cache.Cache.Listener
    public synchronized void onSpanAdded(Cache r1, CacheSpan r2) {
        monitor-enter(this);
        mergeSpan(r2);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.datasource.cache.Cache.Listener
    public synchronized void onSpanRemoved(Cache r8, CacheSpan r9) {
        monitor-enter(this);
        Region r0 = new Region(r9.position, r9.position + r9.length);     // Catch: Throwable -> L20
        Region r1 = this.regions.floor(r0);     // Catch: Throwable -> L20
        if (r1 != null) goto L8;
        Log.m44e(TAG, "Removed a span we were not aware of");     // Catch: Throwable -> L20
        monitor-exit(this);
        return;
    L8:
        this.regions.remove(r1);     // Catch: Throwable -> L20
        if (r1.startOffset >= r0.startOffset) goto L16;
        Region r2 = new Region(r1.startOffset, r0.startOffset);     // Catch: Throwable -> L20
        int r3 = Arrays.binarySearch(this.chunkIndex.offsets, r2.endOffset);     // Catch: Throwable -> L20
        if (r3 >= 0) goto L13;
        int r4 = (-r3) - 2;     // Catch: Throwable -> L20
    L14:
        r2.endOffsetIndex = r4;     // Catch: Throwable -> L20
        this.regions.add(r2);     // Catch: Throwable -> L20
        goto L16
    L13:
        r4 = r3;
    L16:
        if (r1.endOffset <= r0.endOffset) goto L18;
        Region r5 = new Region(r0.endOffset + 1, r1.endOffset);     // Catch: Throwable -> L20
        r5.endOffsetIndex = r1.endOffsetIndex;     // Catch: Throwable -> L20
        this.regions.add(r5);     // Catch: Throwable -> L20
    L18:
        monitor-exit(this);
        return;
    L20:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.datasource.cache.Cache.Listener
    public void onSpanTouched(Cache r1, CacheSpan r2, CacheSpan r3) {
    }

    public void release() {
        this.cache.removeListener(this.cacheKey, this);
    }
}
