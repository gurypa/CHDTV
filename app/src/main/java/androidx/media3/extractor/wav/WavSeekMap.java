package androidx.media3.extractor.wav;

import androidx.media3.common.util.Util;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.SeekPoint;

/* JADX INFO: loaded from: classes.dex */
final class WavSeekMap implements SeekMap {
    private final long blockCount;
    private final long durationUs;
    private final long firstBlockPosition;
    private final int framesPerBlock;
    private final WavFormat wavFormat;

    public WavSeekMap(WavFormat r5, int r6, long r7, long r9) {
        this.wavFormat = r5;
        this.framesPerBlock = r6;
        this.firstBlockPosition = r7;
        this.blockCount = (r9 - r7) / ((long) r5.blockSize);
        this.durationUs = blockIndexToTimeUs(this.blockCount);
    }

    private long blockIndexToTimeUs(long r9) {
        return Util.scaleLargeTimestamp(r9 * ((long) this.framesPerBlock), 1000000, this.wavFormat.frameRateHz);
    }

    @Override // androidx.media3.extractor.SeekMap
    public long getDurationUs() {
        return this.durationUs;
    }

    @Override // androidx.media3.extractor.SeekMap
    public SeekMap.SeekPoints getSeekPoints(long r17) {
        long r1 = Util.constrainValue((((long) this.wavFormat.frameRateHz) * r17) / (((long) this.framesPerBlock) * 1000000), 0, this.blockCount - 1);
        long r5 = this.firstBlockPosition + (((long) this.wavFormat.blockSize) * r1);
        long r7 = blockIndexToTimeUs(r1);
        SeekPoint r9 = new SeekPoint(r7, r5);
        if (r7 >= r17) goto L10;
        if (r1 == (this.blockCount - 1)) goto L10;
        long r3 = 1 + r1;
        long r10 = this.firstBlockPosition + (((long) this.wavFormat.blockSize) * r3);
        return new SeekMap.SeekPoints(r9, new SeekPoint(blockIndexToTimeUs(r3), r10));
    L10:
        return new SeekMap.SeekPoints(r9);
    }

    @Override // androidx.media3.extractor.SeekMap
    public boolean isSeekable() {
        return true;
    }
}
