package androidx.media3.extractor;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class FlacSeekTableSeekMap implements SeekMap {
    private final long firstFrameOffset;
    private final FlacStreamMetadata flacStreamMetadata;

    public FlacSeekTableSeekMap(FlacStreamMetadata r1, long r2) {
        this.flacStreamMetadata = r1;
        this.firstFrameOffset = r2;
    }

    private SeekPoint getSeekPoint(long r6, long r8) {
        return new SeekPoint((1000000 * r6) / ((long) this.flacStreamMetadata.sampleRate), this.firstFrameOffset + r8);
    }

    @Override // androidx.media3.extractor.SeekMap
    public long getDurationUs() {
        return this.flacStreamMetadata.getDurationUs();
    }

    @Override // androidx.media3.extractor.SeekMap
    public SeekMap.SeekPoints getSeekPoints(long r17) {
        Assertions.checkStateNotNull(this.flacStreamMetadata.seekTable);
        long[] r3 = this.flacStreamMetadata.seekTable.pointSampleNumbers;
        long[] r4 = this.flacStreamMetadata.seekTable.pointOffsets;
        int r8 = Util.binarySearchFloor(r3, this.flacStreamMetadata.getSampleNumber(r17), true, false);
        long r9 = 0;
        if (r8 != (-1)) goto L5;
        long r12 = 0;
    L6:
        if (r8 == (-1)) goto L9;
        r9 = r4[r8];
    L9:
        SeekPoint r11 = getSeekPoint(r12, r9);
        if (r11.timeUs == r17) goto L17;
        if (r8 == (r3.length - 1)) goto L17;
        return new SeekMap.SeekPoints(r11, getSeekPoint(r3[r8 + 1], r4[r8 + 1]));
    L17:
        return new SeekMap.SeekPoints(r11);
    L5:
        r12 = r3[r8];
        goto L6
    }

    @Override // androidx.media3.extractor.SeekMap
    public boolean isSeekable() {
        return true;
    }
}
