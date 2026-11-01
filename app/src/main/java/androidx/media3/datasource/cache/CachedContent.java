package androidx.media3.datasource.cache;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes.dex */
final class CachedContent {
    private static final String TAG = "CachedContent";
    private final TreeSet<SimpleCacheSpan> cachedSpans;

    /* JADX INFO: renamed from: id */
    public final int f33id;
    public final String key;
    private final ArrayList<Range> lockedRanges;
    private DefaultContentMetadata metadata;

    private static final class Range {
        public final long length;
        public final long position;

        public Range(long r1, long r3) {
            this.position = r1;
            this.length = r3;
        }

        public boolean contains(long r10, long r12) {
            if (this.length != (-1)) goto L10;
            if (r10 < this.position) goto L8;
            return true;
        L8:
            return false;
        L10:
            if (r12 != (-1)) goto L13;
            return false;
        L13:
            if (this.position > r10) goto L18;
            if ((r10 + r12) > (this.position + this.length)) goto L18;
            return true;
        L18:
            return false;
        }

        public boolean intersects(long r8, long r10) {
            if (this.position > r8) goto L11;
            if (this.length == (-1)) goto L9;
            if ((this.position + this.length) > r8) goto L9;
            return false;
        L9:
            return true;
        L11:
            if (r10 == (-1)) goto L15;
            if ((r8 + r10) > this.position) goto L15;
            return false;
        L15:
            return true;
        }
    }

    public CachedContent(int r2, String r3) {
        this(r2, r3, DefaultContentMetadata.EMPTY);
    }

    public CachedContent(int r2, String r3, DefaultContentMetadata r4) {
        this.f33id = r2;
        this.key = r3;
        this.metadata = r4;
        this.cachedSpans = new TreeSet();
        this.lockedRanges = new ArrayList();
    }

    public void addSpan(SimpleCacheSpan r2) {
        this.cachedSpans.add(r2);
    }

    public boolean applyMetadataMutations(ContentMetadataMutations r3) {
        DefaultContentMetadata r0 = this.metadata;
        this.metadata = this.metadata.copyWithMutationsApplied(r3);
        return !this.metadata.equals(r0);
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L21:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L21;
        CachedContent r2 = (CachedContent) r6;
        if (this.f33id != r2.f33id) goto L20;
        if (this.key.equals(r2.key) == false) goto L20;
        if (this.cachedSpans.equals(r2.cachedSpans) == false) goto L20;
        if (this.metadata.equals(r2.metadata) == false) goto L20;
        return true;
    L20:
        return false;
    }

    public long getCachedBytesLength(long r12, long r14) {
        boolean r3 = true;
        if (r12 < 0) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkArgument(r2);
        if (r14 >= 0) goto L10;
        r3 = false;
    L10:
        Assertions.checkArgument(r3);
        SimpleCacheSpan r4 = getSpan(r12, r14);
        if (r4.isHoleSpan() == true) goto L13;
        long r5 = r12 + r14;
        if (r5 >= 0) goto L21;
        r5 = Long.MAX_VALUE;
    L21:
        long r0 = r4.position + r4.length;
        if (r0 >= r5) goto L34;
        Iterator<SimpleCacheSpan> r6 = this.cachedSpans.tailSet(r4, false).iterator();
    L25:
        if (r6.hasNext() == false) goto L34;
        SimpleCacheSpan r7 = r6.next();
        if (r7.position > r0) goto L34;
        r0 = Math.max(r0, r7.position + r7.length);
        if (r0 < r5) goto L25;
    L34:
        return Math.min(r0 - r12, r14);
    L13:
        if (r4.isOpenEnded() == false) goto L15;
        long r1 = Long.MAX_VALUE;
    L17:
        return -Math.min(r1, r14);
    L15:
        r1 = r4.length;
        goto L17
    L5:
        r2 = false;
        goto L6
    }

    public DefaultContentMetadata getMetadata() {
        return this.metadata;
    }

    public SimpleCacheSpan getSpan(long r8, long r10) {
        SimpleCacheSpan r0 = SimpleCacheSpan.createLookup(this.key, r8);
        SimpleCacheSpan r1 = this.cachedSpans.floor(r0);
        if (r1 != null) goto L5;
    L7:
        SimpleCacheSpan r2 = this.cachedSpans.ceiling(r0);
        if (r2 == null) goto L15;
        long r3 = r2.position - r8;
        if (r10 != (-1)) goto L12;
        long r5 = r3;
    L13:
        r10 = r5;
        goto L15
    L12:
        r5 = Math.min(r3, r10);
    L15:
        return SimpleCacheSpan.createHole(this.key, r8, r10);
    L5:
        if ((r1.position + r1.length) <= r8) goto L7;
        return r1;
    }

    public TreeSet<SimpleCacheSpan> getSpans() {
        return this.cachedSpans;
    }

    public int hashCode() {
        return (((this.f33id * 31) + this.key.hashCode()) * 31) + this.metadata.hashCode();
    }

    public boolean isEmpty() {
        return this.cachedSpans.isEmpty();
    }

    public boolean isFullyLocked(long r3, long r5) {
        int r0 = 0;
    L4:
        if (r0 >= this.lockedRanges.size()) goto L10;
        if (this.lockedRanges.get(r0).contains(r3, r5) == true) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return true;
    L10:
        return false;
    }

    public boolean isFullyUnlocked() {
        return this.lockedRanges.isEmpty();
    }

    public boolean lockRange(long r3, long r5) {
        int r0 = 0;
    L4:
        if (r0 >= this.lockedRanges.size()) goto L10;
        if (this.lockedRanges.get(r0).intersects(r3, r5) == true) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return false;
    L10:
        this.lockedRanges.add(new Range(r3, r5));
        return true;
    }

    public boolean removeSpan(CacheSpan r2) {
        if (this.cachedSpans.remove(r2) == true) goto L5;
        return false;
    L5:
        if (r2.file == null) goto L11;
        r2.file.delete();
        return true;
    L11:
        return true;
    }

    public SimpleCacheSpan setLastTouchTimestamp(SimpleCacheSpan r9, long r10, boolean r12) {
        Assertions.checkState(this.cachedSpans.remove(r9));
        File r0 = (File) Assertions.checkNotNull(r9.file);
        if (r12 == false) goto L8;
        long r6 = r10;
        File r11 = SimpleCacheSpan.getCacheFile((File) Assertions.checkNotNull(r0.getParentFile()), this.f33id, r9.position, r6);
        if (r0.renameTo(r11) == false) goto L7;
        r0 = r11;
    L9:
        SimpleCacheSpan r13 = r9.copyWithFileAndLastTouchTimestamp(r0, r6);
        this.cachedSpans.add(r13);
        return r13;
    L7:
        Log.m48w(TAG, "Failed to rename " + r0 + " to " + r11);
        goto L9
    L8:
        r6 = r10;
        goto L9
    }

    public void unlockRange(long r4) {
        int r0 = 0;
    L4:
        if (r0 >= this.lockedRanges.size()) goto L11;
        if (this.lockedRanges.get(r0).position == r4) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        this.lockedRanges.remove(r0);
        return;
    L11:
        throw new IllegalStateException();
    }
}
