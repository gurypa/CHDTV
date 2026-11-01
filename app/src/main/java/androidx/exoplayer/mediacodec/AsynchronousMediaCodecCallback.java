package androidx.media3.exoplayer.mediacodec;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.collection.CircularIntArray;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.util.ArrayDeque;

/* JADX INFO: loaded from: classes.dex */
final class AsynchronousMediaCodecCallback extends MediaCodec.Callback {
    private final CircularIntArray availableInputBuffers;
    private final CircularIntArray availableOutputBuffers;
    private final ArrayDeque<MediaCodec.BufferInfo> bufferInfos;
    private final HandlerThread callbackThread;
    private MediaFormat currentFormat;
    private final ArrayDeque<MediaFormat> formats;
    private Handler handler;
    private IllegalStateException internalException;
    private final Object lock;
    private MediaCodec.CryptoException mediaCodecCryptoException;
    private MediaCodec.CodecException mediaCodecException;
    private MediaCodecAdapter.OnBufferAvailableListener onBufferAvailableListener;
    private long pendingFlushCount;
    private MediaFormat pendingOutputFormat;
    private boolean shutDown;

    /* JADX INFO: renamed from: $r8$lambda$Fdco1oD1dm50W-d6ae2eyfideq4, reason: not valid java name */
    public static /* synthetic */ void m473$r8$lambda$Fdco1oD1dm50Wd6ae2eyfideq4(AsynchronousMediaCodecCallback r0) {
        r0.onFlushCompleted();
    }

    AsynchronousMediaCodecCallback(HandlerThread r2) {
        this.lock = new Object();
        this.callbackThread = r2;
        this.availableInputBuffers = new CircularIntArray();
        this.availableOutputBuffers = new CircularIntArray();
        this.bufferInfos = new ArrayDeque();
        this.formats = new ArrayDeque();
    }

    private void addOutputFormat(MediaFormat r3) {
        this.availableOutputBuffers.addLast(-2);
        this.formats.add(r3);
    }

    private void flushInternal() {
        if (this.formats.isEmpty() == true) goto L5;
        this.pendingOutputFormat = this.formats.getLast();
    L5:
        this.availableInputBuffers.clear();
        this.availableOutputBuffers.clear();
        this.bufferInfos.clear();
        this.formats.clear();
    }

    private boolean isFlushingOrShutdown() {
        if (this.pendingFlushCount <= 0) goto L5;
    L8:
        return true;
    L5:
        if (this.shutDown == true) goto L8;
        return false;
    }

    private void maybeThrowException() {
        maybeThrowInternalException();
        maybeThrowMediaCodecException();
        maybeThrowMediaCodecCryptoException();
    }

    private void maybeThrowInternalException() {
        if (this.internalException != null) goto L5;
        return;
    L5:
        IllegalStateException r0 = this.internalException;
        this.internalException = null;
        throw r0;
    }

    private void maybeThrowMediaCodecCryptoException() {
        if (this.mediaCodecCryptoException != null) goto L5;
        return;
    L5:
        MediaCodec.CryptoException r0 = this.mediaCodecCryptoException;
        this.mediaCodecCryptoException = null;
        throw r0;
    }

    private void maybeThrowMediaCodecException() {
        if (this.mediaCodecException != null) goto L5;
        return;
    L5:
        MediaCodec.CodecException r0 = this.mediaCodecException;
        this.mediaCodecException = null;
        throw r0;
    }

    private void onFlushCompleted() {
        Object r0 = this.lock;
        monitor-enter(r0);
    L20:
        th = move-exception;
        throw th;
    L5:
        if (this.shutDown == false) goto L8;
        monitor-exit(r0);     // Catch: Throwable -> L20
        return;
    L8:
        this.pendingFlushCount--;     // Catch: Throwable -> L20
        if (this.pendingFlushCount <= 0) goto L13;
        monitor-exit(r0);     // Catch: Throwable -> L20
        return;
    L13:
        if (this.pendingFlushCount >= 0) goto L17;
        setInternalException(new IllegalStateException());     // Catch: Throwable -> L20
        monitor-exit(r0);     // Catch: Throwable -> L20
        return;
    L17:
        flushInternal();     // Catch: Throwable -> L20
        monitor-exit(r0);     // Catch: Throwable -> L20
    }

    private void setInternalException(IllegalStateException r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.internalException = r3;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public int dequeueInputBufferIndex() {
        Object r0 = this.lock;
        monitor-enter(r0);
        maybeThrowException();     // Catch: Throwable -> L14
        int r2 = -1;
        if (isFlushingOrShutdown() == false) goto L9;
        monitor-exit(r0);     // Catch: Throwable -> L14
        return -1;
    L9:
        if (this.availableInputBuffers.isEmpty() == true) goto L12;
        r2 = this.availableInputBuffers.popFirst();     // Catch: Throwable -> L14
    L12:
        monitor-exit(r0);     // Catch: Throwable -> L14
        return r2;
    L14:
        th = move-exception;
        throw th;
    }

    public int dequeueOutputBufferIndex(MediaCodec.BufferInfo r10) {
        Object r1 = this.lock;
        monitor-enter(r1);
        maybeThrowException();     // Catch: Throwable -> L26
        if (isFlushingOrShutdown() == false) goto L12;
        monitor-exit(r1);     // Catch: Throwable -> L9
        return -1;
    L12:
        if (this.availableOutputBuffers.isEmpty() == false) goto L15;
        monitor-exit(r1);     // Catch: Throwable -> L9
        return -1;
    L15:
        int r0 = this.availableOutputBuffers.popFirst();     // Catch: Throwable -> L26
        if (r0 < 0) goto L21;
        Assertions.checkStateNotNull(this.currentFormat);     // Catch: Throwable -> L26
        MediaCodec.BufferInfo r2 = this.bufferInfos.remove();     // Catch: Throwable -> L26
        r10.set(r2.offset, r2.size, r2.presentationTimeUs, r2.flags);     // Catch: Throwable -> L30
    L24:
        monitor-exit(r1);     // Catch: Throwable -> L30
        return r0;
    L21:
        if (r0 != (-2)) goto L24;
        this.currentFormat = this.formats.remove();     // Catch: Throwable -> L30
    L9:
        th = th;
    L28:
        monitor-exit(r1);     // Catch: Throwable -> L30
        throw th;
    L26:
        th = th;
    L30:
        th = th;
        goto L28
    }

    public void flush() {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.pendingFlushCount++;     // Catch: Throwable -> L7
        ((Handler) Util.castNonNull(this.handler)).post(new AsynchronousMediaCodecCallback$$ExternalSyntheticLambda0(this));     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public MediaFormat getOutputFormat() {
        Object r0 = this.lock;
        monitor-enter(r0);
    L11:
        th = move-exception;
        throw th;
    L5:
        if (this.currentFormat == null) goto L10;
        MediaFormat r1 = this.currentFormat;     // Catch: Throwable -> L11
        monitor-exit(r0);     // Catch: Throwable -> L11
        return r1;
    L10:
        throw new IllegalStateException();     // Catch: Throwable -> L11
    }

    public void initialize(MediaCodec r3) {
        if (this.handler != null) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        this.callbackThread.start();
        Handler r1 = new Handler(this.callbackThread.getLooper());
        r3.setCallback(this, r1);
        this.handler = r1;
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // android.media.MediaCodec.Callback
    public void onCryptoError(MediaCodec r3, MediaCodec.CryptoException r4) {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.mediaCodecCryptoException = r4;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // android.media.MediaCodec.Callback
    public void onError(MediaCodec r3, MediaCodec.CodecException r4) {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.mediaCodecException = r4;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // android.media.MediaCodec.Callback
    public void onInputBufferAvailable(MediaCodec r3, int r4) {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.availableInputBuffers.addLast(r4);     // Catch: Throwable -> L9
        if (this.onBufferAvailableListener == null) goto L7;
        this.onBufferAvailableListener.onInputBufferAvailable();     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
        return;
    L9:
        th = move-exception;
        throw th;
    }

    @Override // android.media.MediaCodec.Callback
    public void onOutputBufferAvailable(MediaCodec r3, int r4, MediaCodec.BufferInfo r5) {
        Object r0 = this.lock;
        monitor-enter(r0);
    L12:
        th = move-exception;
        throw th;
    L5:
        if (this.pendingOutputFormat == null) goto L7;
        addOutputFormat(this.pendingOutputFormat);     // Catch: Throwable -> L12
        this.pendingOutputFormat = null;     // Catch: Throwable -> L12
    L7:
        this.availableOutputBuffers.addLast(r4);     // Catch: Throwable -> L12
        this.bufferInfos.add(r5);     // Catch: Throwable -> L12
        if (this.onBufferAvailableListener == null) goto L10;
        this.onBufferAvailableListener.onOutputBufferAvailable();     // Catch: Throwable -> L12
    L10:
        monitor-exit(r0);     // Catch: Throwable -> L12
    }

    @Override // android.media.MediaCodec.Callback
    public void onOutputFormatChanged(MediaCodec r3, MediaFormat r4) {
        Object r0 = this.lock;
        monitor-enter(r0);
        addOutputFormat(r4);     // Catch: Throwable -> L7
        this.pendingOutputFormat = null;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public void setOnBufferAvailableListener(MediaCodecAdapter.OnBufferAvailableListener r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.onBufferAvailableListener = r3;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public void shutdown() {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.shutDown = true;     // Catch: Throwable -> L8
        this.callbackThread.quit();     // Catch: Throwable -> L8
        flushInternal();     // Catch: Throwable -> L8
        monitor-exit(r0);     // Catch: Throwable -> L8
        return;
    L8:
        th = move-exception;
        throw th;
    }
}
