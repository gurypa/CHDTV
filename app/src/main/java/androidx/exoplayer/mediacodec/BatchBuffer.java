package androidx.media3.exoplayer.mediacodec;

import androidx.media3.common.util.Assertions;
import androidx.media3.decoder.DecoderInputBuffer;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
final class BatchBuffer extends DecoderInputBuffer {
    public static final int DEFAULT_MAX_SAMPLE_COUNT = 32;
    static final int MAX_SIZE_BYTES = 3072000;
    private long lastSampleTimeUs;
    private int maxSampleCount;
    private int sampleCount;

    public BatchBuffer() {
        super(2);
        this.maxSampleCount = 32;
    }

    private boolean canAppendSampleBuffer(DecoderInputBuffer r6) {
        if (hasSamples() == true) goto L6;
        return true;
    L6:
        if (this.sampleCount < this.maxSampleCount) goto L8;
        return false;
    L8:
        ByteBuffer r0 = r6.data;
        if (r0 != null) goto L11;
    L15:
        return true;
    L11:
        if (this.data == null) goto L15;
        if ((this.data.position() + r0.remaining()) <= MAX_SIZE_BYTES) goto L15;
        return false;
    }

    public boolean append(DecoderInputBuffer r5) {
        Assertions.checkArgument(!r5.isEncrypted());
        Assertions.checkArgument(!r5.hasSupplementalData());
        Assertions.checkArgument(!r5.isEndOfStream());
        if (canAppendSampleBuffer(r5) == true) goto L6;
        return false;
    L6:
        int r0 = this.sampleCount;
        this.sampleCount = r0 + 1;
        if (r0 != 0) goto L11;
        this.timeUs = r5.timeUs;
        if (r5.isKeyFrame() == false) goto L11;
        setFlags(1);
    L11:
        ByteBuffer r1 = r5.data;
        if (r1 == null) goto L14;
        ensureSpaceForWrite(r1.remaining());
        this.data.put(r1);
    L14:
        this.lastSampleTimeUs = r5.timeUs;
        return true;
    }

    @Override // androidx.media3.decoder.DecoderInputBuffer, androidx.media3.decoder.Buffer
    public void clear() {
        super.clear();
        this.sampleCount = 0;
    }

    public long getFirstSampleTimeUs() {
        return this.timeUs;
    }

    public long getLastSampleTimeUs() {
        return this.lastSampleTimeUs;
    }

    public int getSampleCount() {
        return this.sampleCount;
    }

    public boolean hasSamples() {
        if (this.sampleCount <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    public void setMaxSampleCount(int r2) {
        if (r2 <= 0) goto L4;
        boolean r0 = true;
    L5:
        Assertions.checkArgument(r0);
        this.maxSampleCount = r2;
        return;
    L4:
        r0 = false;
        goto L5
    }
}
