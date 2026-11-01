package androidx.media3.exoplayer.source.chunk;

import androidx.media3.common.util.Log;
import androidx.media3.exoplayer.source.SampleQueue;
import androidx.media3.extractor.DiscardingTrackOutput;
import androidx.media3.extractor.TrackOutput;

/* JADX INFO: loaded from: classes.dex */
public final class BaseMediaChunkOutput implements ChunkExtractor.TrackOutputProvider {
    private static final String TAG = "BaseMediaChunkOutput";
    private final SampleQueue[] sampleQueues;
    private final int[] trackTypes;

    public BaseMediaChunkOutput(int[] r1, SampleQueue[] r2) {
        this.trackTypes = r1;
        this.sampleQueues = r2;
    }

    public int[] getWriteIndices() {
        int[] r0 = new int[this.sampleQueues.length];
        int r1 = 0;
    L4:
        if (r1 >= this.sampleQueues.length) goto L6;
        r0[r1] = this.sampleQueues[r1].getWriteIndex();
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    public void setSampleOffsetUs(long r5) {
        SampleQueue[] r0 = this.sampleQueues;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].setSampleOffsetUs(r5);
        r2 = r2 + 1;
        goto L3
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor.TrackOutputProvider
    public TrackOutput track(int r3, int r4) {
        int r0 = 0;
    L4:
        if (r0 >= this.trackTypes.length) goto L10;
        if (r4 == this.trackTypes[r0]) goto L8;
        r0 = r0 + 1;
        goto L4
    L8:
        return this.sampleQueues[r0];
    L10:
        Log.m44e(TAG, "Unmatched track of type: " + r4);
        return new DiscardingTrackOutput();
    }
}
