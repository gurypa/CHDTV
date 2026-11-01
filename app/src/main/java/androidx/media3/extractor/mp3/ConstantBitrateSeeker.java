package androidx.media3.extractor.mp3;

import androidx.media3.extractor.ConstantBitrateSeekMap;
import androidx.media3.extractor.MpegAudioUtil;

/* JADX INFO: loaded from: classes.dex */
final class ConstantBitrateSeeker extends ConstantBitrateSeekMap implements Seeker {
    private final boolean allowSeeksIfLengthUnknown;
    private final int bitrate;
    private final long dataEndPosition;
    private final long firstFramePosition;
    private final int frameSize;

    public ConstantBitrateSeeker(long r5, long r7, int r9, int r10, boolean r11) {
        super(r5, r7, r9, r10, r11);
        this.firstFramePosition = r7;
        this.bitrate = r9;
        this.frameSize = r10;
        this.allowSeeksIfLengthUnknown = r11;
        long r1 = -1;
        if (r5 == (-1)) goto L5;
        r1 = r5;
    L5:
        this.dataEndPosition = r1;
    }

    public ConstantBitrateSeeker(long r9, long r11, MpegAudioUtil.Header r13, boolean r14) {
        this(r9, r11, r13.bitrate, r13.frameSize, r14);
    }

    public ConstantBitrateSeeker copyWithNewDataEndPosition(long r9) {
        return new ConstantBitrateSeeker(r9, this.firstFramePosition, this.bitrate, this.frameSize, this.allowSeeksIfLengthUnknown);
    }

    @Override // androidx.media3.extractor.mp3.Seeker
    public int getAverageBitrate() {
        return this.bitrate;
    }

    @Override // androidx.media3.extractor.mp3.Seeker
    public long getDataEndPosition() {
        return this.dataEndPosition;
    }

    @Override // androidx.media3.extractor.mp3.Seeker
    public long getTimeUs(long r3) {
        return getTimeUsAtPosition(r3);
    }
}
