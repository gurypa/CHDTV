package androidx.media3.exoplayer.mediacodec;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PersistableBundle;
import android.view.Surface;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.TraceUtil;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.CryptoInfo;
import com.google.common.base.Supplier;
import java.io.IOException;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
final class AsynchronousMediaCodecAdapter implements MediaCodecAdapter {
    private static final int STATE_CREATED = 0;
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_SHUT_DOWN = 2;
    private final AsynchronousMediaCodecCallback asynchronousMediaCodecCallback;
    private final MediaCodecBufferEnqueuer bufferEnqueuer;
    private final MediaCodec codec;
    private boolean codecReleased;
    private final LoudnessCodecController loudnessCodecController;
    private int state;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.mediacodec.AsynchronousMediaCodecAdapter$1 */
    static /* synthetic */ class C03041 {
    }

    public static final class Factory implements MediaCodecAdapter.Factory {
        private final Supplier<HandlerThread> callbackThreadSupplier;
        private boolean enableSynchronousBufferQueueingWithAsyncCryptoFlag;
        private final Supplier<HandlerThread> queueingThreadSupplier;

        public Factory(final int r3) {
            this(new AsynchronousMediaCodecAdapter$Factory$$ExternalSyntheticLambda0(r3), new AsynchronousMediaCodecAdapter$Factory$$ExternalSyntheticLambda1(r3));
        }

        Factory(Supplier<HandlerThread> r2, Supplier<HandlerThread> r3) {
            this.callbackThreadSupplier = r2;
            this.queueingThreadSupplier = r3;
            this.enableSynchronousBufferQueueingWithAsyncCryptoFlag = false;
        }

        static /* synthetic */ HandlerThread lambda$new$0(int r2) {
            return new HandlerThread(AsynchronousMediaCodecAdapter.access$300(r2));
        }

        static /* synthetic */ HandlerThread lambda$new$1(int r2) {
            return new HandlerThread(AsynchronousMediaCodecAdapter.access$200(r2));
        }

        private static boolean useSynchronousBufferQueueingWithAsyncCryptoFlag(Format r3) {
            if (Util.SDK_INT >= 34) goto L6;
            return false;
        L6:
            if (Util.SDK_INT < 35) goto L8;
        L9:
            return true;
        L8:
            if (MimeTypes.isVideo(r3.sampleMimeType) == true) goto L9;
            return false;
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter.Factory
        public AsynchronousMediaCodecAdapter createAdapter(MediaCodecAdapter.Configuration r11) throws IOException {
            String r1 = r11.codecInfo.name;
            AsynchronousMediaCodecAdapter r2 = null;
            MediaCodec r3 = null;
            TraceUtil.beginSection("createCodec:" + r1);     // Catch: Exception -> L22
            MediaCodec r0 = MediaCodec.createByCodecName(r1);     // Catch: Exception -> L22
            int r4 = 0;
        L20:
            e = e;
            r3 = r0;
        L23:
            if (r2 != null) goto L26;
            if (r3 == null) goto L27;
            r3.release();
        L27:
            throw e;
        L26:
            r2.release();
            goto L27
        L6:
            if (this.enableSynchronousBufferQueueingWithAsyncCryptoFlag == true) goto L8;
        L10:
            MediaCodecBufferEnqueuer r7 = new AsynchronousMediaCodecBufferEnqueuer(r0, this.queueingThreadSupplier.get());     // Catch: Exception -> L20
        L11:
            r2 = new AsynchronousMediaCodecAdapter(r0, this.callbackThreadSupplier.get(), r7, r11.loudnessCodecController, null);     // Catch: Exception -> L20
            TraceUtil.endSection();     // Catch: Exception -> L20
            if (r11.surface == null) goto L14;
        L18:
            AsynchronousMediaCodecAdapter.access$100(r2, r11.mediaFormat, r11.surface, r11.crypto, r4);     // Catch: Exception -> L20
            return r2;
        L14:
            if (r11.codecInfo.detachedSurfaceSupported == false) goto L18;
            if (Util.SDK_INT < 35) goto L18;
            r4 = r4 | 8;     // Catch: Exception -> L20
            goto L18
        L8:
            if (useSynchronousBufferQueueingWithAsyncCryptoFlag(r11.format) == false) goto L10;
            SynchronousMediaCodecBufferEnqueuer r5 = new SynchronousMediaCodecBufferEnqueuer(r0);     // Catch: Exception -> L20
            r4 = 0 | 4;     // Catch: Exception -> L20
            r7 = r5;
        L22:
            e = e;
            goto L23
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter.Factory
        public /* bridge */ /* synthetic */ MediaCodecAdapter createAdapter(MediaCodecAdapter.Configuration r1) throws IOException {
            return createAdapter(r1);
        }

        public void experimentalSetAsyncCryptoFlagEnabled(boolean r1) {
            this.enableSynchronousBufferQueueingWithAsyncCryptoFlag = r1;
        }
    }

    private AsynchronousMediaCodecAdapter(MediaCodec r2, HandlerThread r3, MediaCodecBufferEnqueuer r4, LoudnessCodecController r5) {
        this.codec = r2;
        this.asynchronousMediaCodecCallback = new AsynchronousMediaCodecCallback(r3);
        this.bufferEnqueuer = r4;
        this.loudnessCodecController = r5;
        this.state = 0;
    }

    /* synthetic */ AsynchronousMediaCodecAdapter(MediaCodec r1, HandlerThread r2, MediaCodecBufferEnqueuer r3, LoudnessCodecController r4, C03041 r5) {
        this(r1, r2, r3, r4);
    }

    static /* synthetic */ void access$100(AsynchronousMediaCodecAdapter r0, MediaFormat r1, Surface r2, MediaCrypto r3, int r4) {
        r0.initialize(r1, r2, r3, r4);
    }

    static /* synthetic */ String access$200(int r1) {
        return createQueueingThreadLabel(r1);
    }

    static /* synthetic */ String access$300(int r1) {
        return createCallbackThreadLabel(r1);
    }

    private static String createCallbackThreadLabel(int r1) {
        return createThreadLabel(r1, "ExoPlayer:MediaCodecAsyncAdapter:");
    }

    private static String createQueueingThreadLabel(int r1) {
        return createThreadLabel(r1, "ExoPlayer:MediaCodecQueueingThread:");
    }

    private static String createThreadLabel(int r3, String r4) {
        StringBuilder r0 = new StringBuilder(r4);
        if (r3 != 1) goto L6;
        r0.append("Audio");
    L10:
        return r0.toString();
    L6:
        if (r3 != 2) goto L8;
        r0.append("Video");
        goto L10
    L8:
        r0.append("Unknown(").append(r3).append(")");
        goto L10
    }

    private void initialize(MediaFormat r3, Surface r4, MediaCrypto r5, int r6) {
        this.asynchronousMediaCodecCallback.initialize(this.codec);
        TraceUtil.beginSection("configureCodec");
        this.codec.configure(r3, r4, r5, r6);
        TraceUtil.endSection();
        this.bufferEnqueuer.start();
        TraceUtil.beginSection("startCodec");
        this.codec.start();
        TraceUtil.endSection();
        if (Util.SDK_INT >= 35) goto L5;
    L7:
        this.state = 1;
        return;
    L5:
        if (this.loudnessCodecController == null) goto L7;
        this.loudnessCodecController.addMediaCodec(this.codec);
        goto L7
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public int dequeueInputBufferIndex() {
        this.bufferEnqueuer.maybeThrowException();
        return this.asynchronousMediaCodecCallback.dequeueInputBufferIndex();
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public int dequeueOutputBufferIndex(MediaCodec.BufferInfo r2) {
        this.bufferEnqueuer.maybeThrowException();
        return this.asynchronousMediaCodecCallback.dequeueOutputBufferIndex(r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void detachOutputSurface() {
        this.codec.detachOutputSurface();
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void flush() {
        this.bufferEnqueuer.flush();
        this.codec.flush();
        this.asynchronousMediaCodecCallback.flush();
        this.codec.start();
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
        return this.asynchronousMediaCodecCallback.getOutputFormat();
    }

    /* JADX INFO: renamed from: lambda$setOnFrameRenderedListener$0$androidx-media3-exoplayer-mediacodec-AsynchronousMediaCodecAdapter */
    /* synthetic */ void m110x4a2a5e4a(MediaCodecAdapter.OnFrameRenderedListener r7, MediaCodec r8, long r9, long r11) {
        r7.onFrameRendered(this, r9, r11);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public boolean needsReconfiguration() {
        return false;
    }

    void onError(MediaCodec.CodecException r3) {
        this.asynchronousMediaCodecCallback.onError(this.codec, r3);
    }

    void onOutputFormatChanged(MediaFormat r3) {
        this.asynchronousMediaCodecCallback.onOutputFormatChanged(this.codec, r3);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void queueInputBuffer(int r8, int r9, int r10, long r11, int r13) {
        this.bufferEnqueuer.queueInputBuffer(r8, r9, r10, r11, r13);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void queueSecureInputBuffer(int r8, int r9, CryptoInfo r10, long r11, int r13) {
        this.bufferEnqueuer.queueSecureInputBuffer(r8, r9, r10, r11, r13);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public boolean registerOnBufferAvailableListener(MediaCodecAdapter.OnBufferAvailableListener r2) {
        this.asynchronousMediaCodecCallback.setOnBufferAvailableListener(r2);
        return true;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void release() {
    L29:
        th = move-exception;
        if (this.codecReleased == false) goto L54;
    L51:
        throw th;
    L54:
    L43:
        th = move-exception;
        if (Util.SDK_INT >= 35) goto L47;
    L49:
        this.codec.release();
        this.codecReleased = true;
        throw th;
    L47:
        if (this.loudnessCodecController == null) goto L49;
        this.loudnessCodecController.removeMediaCodec(this.codec);
        goto L49
    L33:
        if (Util.SDK_INT < 30) goto L38;
        if (Util.SDK_INT >= 33) goto L38;
        this.codec.stop();     // Catch: Throwable -> L43
    L38:
        if (Util.SDK_INT >= 35) goto L40;
    L42:
        this.codec.release();
        this.codecReleased = true;
        goto L51
    L40:
        if (this.loudnessCodecController == null) goto L42;
        this.loudnessCodecController.removeMediaCodec(this.codec);
        goto L42
    L4:
        if (this.state != 1) goto L6;
        this.bufferEnqueuer.shutdown();     // Catch: Throwable -> L29
        this.asynchronousMediaCodecCallback.shutdown();     // Catch: Throwable -> L29
    L6:
        this.state = 2;     // Catch: Throwable -> L29
        if (this.codecReleased == false) goto L52;
        return;
    L52:
    L20:
        th = move-exception;
        if (Util.SDK_INT >= 35) goto L24;
    L26:
        this.codec.release();
        this.codecReleased = true;
        throw th;
    L24:
        if (this.loudnessCodecController == null) goto L26;
        this.loudnessCodecController.removeMediaCodec(this.codec);
        goto L26
    L10:
        if (Util.SDK_INT < 30) goto L15;
        if (Util.SDK_INT >= 33) goto L15;
        this.codec.stop();     // Catch: Throwable -> L20
    L15:
        if (Util.SDK_INT >= 35) goto L17;
    L19:
        this.codec.release();
        this.codecReleased = true;
        return;
    L17:
        if (this.loudnessCodecController == null) goto L19;
        this.loudnessCodecController.removeMediaCodec(this.codec);
        goto L19
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
        this.codec.setOnFrameRenderedListener(new AsynchronousMediaCodecAdapter$$ExternalSyntheticLambda0(this, r3), r4);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void setOutputSurface(Surface r2) {
        this.codec.setOutputSurface(r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void setParameters(Bundle r2) {
        this.bufferEnqueuer.setParameters(r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
    public void setVideoScalingMode(int r2) {
        this.codec.setVideoScalingMode(r2);
    }
}
