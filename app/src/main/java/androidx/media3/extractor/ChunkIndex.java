package androidx.media3.extractor;

import androidx.media3.common.util.Util;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class ChunkIndex implements SeekMap {
    private final long durationUs;
    public final long[] durationsUs;
    public final int length;
    public final long[] offsets;
    public final int[] sizes;
    public final long[] timesUs;

    public ChunkIndex(int[] r5, long[] r6, long[] r7, long[] r8) {
        this.sizes = r5;
        this.offsets = r6;
        this.durationsUs = r7;
        this.timesUs = r8;
        this.length = r5.length;
        if (this.length <= 0) goto L5;
        this.durationUs = r7[this.length - 1] + r8[this.length - 1];
        return;
    L5:
        this.durationUs = 0;
    }

    public int getChunkIndex(long r3) {
        return Util.binarySearchFloor(this.timesUs, r3, true, true);
    }

    @Override // androidx.media3.extractor.SeekMap
    public long getDurationUs() {
        return this.durationUs;
    }

    @Override // androidx.media3.extractor.SeekMap
    public SeekMap.SeekPoints getSeekPoints(long r9) {
        int r0 = getChunkIndex(r9);
        SeekPoint r1 = new SeekPoint(this.timesUs[r0], this.offsets[r0]);
        if (r1.timeUs >= r9) goto L10;
        if (r0 == (this.length - 1)) goto L10;
        return new SeekMap.SeekPoints(r1, new SeekPoint(this.timesUs[r0 + 1], this.offsets[r0 + 1]));
    L10:
        return new SeekMap.SeekPoints(r1);
    }

    @Override // androidx.media3.extractor.SeekMap
    public boolean isSeekable() {
        return true;
    }

    public String toString() {
        return "ChunkIndex(length=" + this.length + ", sizes=" + Arrays.toString(this.sizes) + ", offsets=" + Arrays.toString(this.offsets) + ", timeUs=" + Arrays.toString(this.timesUs) + ", durationsUs=" + Arrays.toString(this.durationsUs) + ")";
    }
}
