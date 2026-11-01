package androidx.media3.exoplayer.mediacodec;

import android.media.MediaCodec;
import android.os.Bundle;
import androidx.media3.decoder.CryptoInfo;

/* JADX INFO: loaded from: classes.dex */
class SynchronousMediaCodecBufferEnqueuer implements MediaCodecBufferEnqueuer {
    private final MediaCodec codec;

    public SynchronousMediaCodecBufferEnqueuer(MediaCodec r1) {
        this.codec = r1;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void flush() {
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void maybeThrowException() {
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void queueInputBuffer(int r8, int r9, int r10, long r11, int r13) {
        this.codec.queueInputBuffer(r8, r9, r10, r11, r13);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void queueSecureInputBuffer(int r8, int r9, CryptoInfo r10, long r11, int r13) {
        this.codec.queueSecureInputBuffer(r8, r9, r10.getFrameworkCryptoInfo(), r11, r13);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void setParameters(Bundle r2) {
        this.codec.setParameters(r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void shutdown() {
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void start() {
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void waitUntilQueueingComplete() {
    }
}
