package androidx.media3.exoplayer.hls;

import androidx.media3.common.util.Assertions;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.source.SampleStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
final class HlsSampleStream implements SampleStream {
    private int sampleQueueIndex;
    private final HlsSampleStreamWrapper sampleStreamWrapper;
    private final int trackGroupIndex;

    public HlsSampleStream(HlsSampleStreamWrapper r2, int r3) {
        this.sampleStreamWrapper = r2;
        this.trackGroupIndex = r3;
        this.sampleQueueIndex = -1;
    }

    private boolean hasValidSampleQueueIndex() {
        if (this.sampleQueueIndex != (-1)) goto L5;
    L9:
        return false;
    L5:
        if (this.sampleQueueIndex == (-3)) goto L9;
        if (this.sampleQueueIndex == (-2)) goto L9;
        return true;
    }

    public void bindSampleQueue() {
        if (this.sampleQueueIndex != (-1)) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        this.sampleQueueIndex = this.sampleStreamWrapper.bindSampleQueueToSampleStream(this.trackGroupIndex);
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.source.SampleStream
    public boolean isReady() {
        if (this.sampleQueueIndex != (-3)) goto L5;
    L10:
        return true;
    L5:
        if (hasValidSampleQueueIndex() == true) goto L7;
    L9:
        return false;
    L7:
        if (this.sampleStreamWrapper.isReady(this.sampleQueueIndex) == false) goto L9;
        goto L9
    }

    @Override // androidx.media3.exoplayer.source.SampleStream
    public void maybeThrowError() throws IOException {
        if (this.sampleQueueIndex == (-2)) goto L12;
        if (this.sampleQueueIndex != (-1)) goto L8;
        this.sampleStreamWrapper.maybeThrowError();
        return;
    L8:
        if (this.sampleQueueIndex == (-3)) goto L14;
        this.sampleStreamWrapper.maybeThrowError(this.sampleQueueIndex);
        return;
    L14:
        return;
    L12:
        throw new SampleQueueMappingException(this.sampleStreamWrapper.getTrackGroups().get(this.trackGroupIndex).getFormat(0).sampleMimeType);
    }

    @Override // androidx.media3.exoplayer.source.SampleStream
    public int readData(FormatHolder r3, DecoderInputBuffer r4, int r5) {
        if (this.sampleQueueIndex != (-3)) goto L7;
        r4.addFlag(4);
        return -4;
    L7:
        if (hasValidSampleQueueIndex() == true) goto L10;
        return -3;
    L10:
        return this.sampleStreamWrapper.readData(this.sampleQueueIndex, r3, r4, r5);
    }

    @Override // androidx.media3.exoplayer.source.SampleStream
    public int skipData(long r3) {
        if (hasValidSampleQueueIndex() == true) goto L7;
        return 0;
    L7:
        return this.sampleStreamWrapper.skipData(this.sampleQueueIndex, r3);
    }

    public void unbindSampleQueue() {
        if (this.sampleQueueIndex == (-1)) goto L6;
        this.sampleStreamWrapper.unbindSampleQueue(this.trackGroupIndex);
        this.sampleQueueIndex = -1;
        return;
    }
}
