package androidx.media3.exoplayer.mediacodec;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.Surface;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.TraceUtil;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.CryptoInfo;
import java.io.IOException;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public final class SynchronousMediaCodecAdapter implements MediaCodecAdapter {
    private final MediaCodec codec;
    private final LoudnessCodecController loudnessCodecController;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.mediacodec.SynchronousMediaCodecAdapter$1 */
    static /* synthetic */ class C03111 {
    }

    public static class Factory implements MediaCodecAdapter.Factory {
        public Factory() {
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter.Factory
        public MediaCodecAdapter createAdapter(MediaCodecAdapter.Configuration r6) throws IOException {
            MediaCodec r0 = null;
            r0 = createCodec(r6);     // Catch: RuntimeException -> L12 Throwable -> L14
            TraceUtil.beginSection("configureCodec");     // Catch: RuntimeException -> L12 Throwable -> L14
            int r1 = 0;
            if (r6.surface == null) goto L6;
        L10:
            r0.configure(r6.mediaFormat, r6.surface, r6.crypto, r1);     // Catch: RuntimeException -> L12 Throwable -> L14
            TraceUtil.endSection();     // Catch: RuntimeException -> L12 Throwable -> L14
            TraceUtil.beginSection("startCodec");     // Catch: RuntimeException -> L12 Throwable -> L14
            r0.start();     // Catch: RuntimeException -> L12 Throwable -> L14
            TraceUtil.endSection();     // Catch: RuntimeException -> L12 Throwable -> L14
            return new SynchronousMediaCodecAdapter(r0, r6.loudnessCodecController, null);
        L6:
            if (r6.codecInfo.detachedSurfaceSupported == false) goto L10;
            if (Util.SDK_INT < 35) goto L10;
            r1 = 0 | 8;     // Catch: RuntimeException -> L12 Throwable -> L14
        L14:
            e = move-exception;
            if (r0 == null) goto L17;
            r0.release();
        L17:
            throw e;
        }

        protected MediaCodec createCodec(MediaCodecAdapter.Configuration r4) throws IOException {
            Assertions.checkNotNull(r4.codecInfo);
            String r0 = r4.codecInfo.name;
            TraceUtil.beginSection("createCodec:" + r0);
            MediaCodec r1 = MediaCodec.createByCodecName(r0);
            TraceUtil.endSection();
            return r1;
        }
    }

    private SynchronousMediaCodecAdapter(MediaCodec r3, LoudnessCodecController r4) {
        this.codec = r3;
        this.loudnessCodecController = r4;
        if (Util.SDK_INT < 35) goto L7;
        if (r4 == null) goto L8;
        r4.addMediaCodec(this.codec);
        return;
    L8:
        return;
    }

    /* synthetic */ SynchronousMediaCodecAdapter(MediaCodec r1, LoudnessCodecController r2, C03111 r3) {
        this(r1, r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public int dequeueInputBufferIndex() {
        return this.codec.dequeueInputBuffer(0);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public int dequeueOutputBufferIndex(MediaCodec.BufferInfo r4) {
    L2:
        int r0 = this.codec.dequeueOutputBuffer(r4, 0);
        if (r0 == (-3)) goto L2;
        return r0;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void detachOutputSurface() {
        this.codec.detachOutputSurface();
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void flush() {
        this.codec.flush();
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public ByteBuffer getInputBuffer(int r2) {
        return this.codec.getInputBuffer(r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public PersistableBundle getMetrics() {
        return this.codec.getMetrics();
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public ByteBuffer getOutputBuffer(int r2) {
        return this.codec.getOutputBuffer(r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public MediaFormat getOutputFormat() {
        return this.codec.getOutputFormat();
    }

    /* JADX INFO: renamed from: lambda$setOnFrameRenderedListener$0$androidx-media3-exoplayer-mediacodec-SynchronousMediaCodecAdapter */
    /* synthetic */ void m112xe3d0a01f(MediaCodecAdapter.OnFrameRenderedListener r7, MediaCodec r8, long r9, long r11) {
        r7.onFrameRendered(this, r9, r11);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public boolean needsReconfiguration() {
        return false;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void queueInputBuffer(int r8, int r9, int r10, long r11, int r13) {
        this.codec.queueInputBuffer(r8, r9, r10, r11, r13);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void queueSecureInputBuffer(int r8, int r9, CryptoInfo r10, long r11, int r13) {
        this.codec.queueSecureInputBuffer(r8, r9, r10.getFrameworkCryptoInfo(), r11, r13);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public /* synthetic */ boolean registerOnBufferAvailableListener(MediaCodecAdapter.OnBufferAvailableListener r1) {
        return MediaCodecAdapter.CC.$default$registerOnBufferAvailableListener(this, r1);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void release() {
    L15:
        th = move-exception;
        if (Util.SDK_INT >= 35) goto L19;
    L21:
        this.codec.release();
        throw th;
    L19:
        if (this.loudnessCodecController == null) goto L21;
        this.loudnessCodecController.removeMediaCodec(this.codec);
        goto L21
    L4:
        if (Util.SDK_INT < 30) goto L9;
        if (Util.SDK_INT >= 33) goto L9;
        this.codec.stop();     // Catch: Throwable -> L15
    L9:
        if (Util.SDK_INT >= 35) goto L11;
    L13:
        this.codec.release();
        return;
    L11:
        if (this.loudnessCodecController == null) goto L13;
        this.loudnessCodecController.removeMediaCodec(this.codec);
        goto L13
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void releaseOutputBuffer(int r2, long r3) {
        this.codec.releaseOutputBuffer(r2, r3);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void releaseOutputBuffer(int r2, boolean r3) {
        this.codec.releaseOutputBuffer(r2, r3);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void setOnFrameRenderedListener(final MediaCodecAdapter.OnFrameRenderedListener r3, Handler r4) {
        this.codec.setOnFrameRenderedListener(new SynchronousMediaCodecAdapter$$ExternalSyntheticLambda0(this, r3), r4);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void setOutputSurface(Surface r2) {
        this.codec.setOutputSurface(r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void setParameters(Bundle r2) {
        this.codec.setParameters(r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void setVideoScalingMode(int r2) {
        this.codec.setVideoScalingMode(r2);
    }
}
