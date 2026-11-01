package androidx.media3.exoplayer.mediacodec;

import android.media.MediaCodec;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ConditionVariable;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.CryptoInfo;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes.dex */
class AsynchronousMediaCodecBufferEnqueuer implements MediaCodecBufferEnqueuer {
    private static final ArrayDeque<MessageParams> MESSAGE_PARAMS_INSTANCE_POOL = null;
    private static final int MSG_OPEN_CV = 3;
    private static final int MSG_QUEUE_INPUT_BUFFER = 1;
    private static final int MSG_QUEUE_SECURE_INPUT_BUFFER = 2;
    private static final int MSG_SET_PARAMETERS = 4;
    private static final Object QUEUE_SECURE_LOCK = null;
    private final MediaCodec codec;
    private final ConditionVariable conditionVariable;
    private Handler handler;
    private final HandlerThread handlerThread;
    private final AtomicReference<RuntimeException> pendingRuntimeException;
    private boolean started;

    private static class MessageParams {
        public final MediaCodec.CryptoInfo cryptoInfo;
        public int flags;
        public int index;
        public int offset;
        public long presentationTimeUs;
        public int size;

        MessageParams() {
            this.cryptoInfo = new MediaCodec.CryptoInfo();
        }

        public void setQueueParams(int r1, int r2, int r3, long r4, int r6) {
            this.index = r1;
            this.offset = r2;
            this.size = r3;
            this.presentationTimeUs = r4;
            this.flags = r6;
        }
    }

    static {
        MESSAGE_PARAMS_INSTANCE_POOL = new ArrayDeque();
        QUEUE_SECURE_LOCK = new Object();
    }

    public AsynchronousMediaCodecBufferEnqueuer(MediaCodec r2, HandlerThread r3) {
        this(r2, r3, new ConditionVariable());
    }

    AsynchronousMediaCodecBufferEnqueuer(MediaCodec r2, HandlerThread r3, ConditionVariable r4) {
        this.codec = r2;
        this.handlerThread = r3;
        this.conditionVariable = r4;
        this.pendingRuntimeException = new AtomicReference();
    }

    static /* synthetic */ void access$000(AsynchronousMediaCodecBufferEnqueuer r0, Message r1) {
        r0.doHandleMessage(r1);
    }

    private void blockUntilHandlerThreadIsIdle() throws InterruptedException {
        this.conditionVariable.close();
        ((Handler) Assertions.checkNotNull(this.handler)).obtainMessage(3).sendToTarget();
        this.conditionVariable.block();
    }

    private static void copy(CryptoInfo r3, MediaCodec.CryptoInfo r4) {
        r4.numSubSamples = r3.numSubSamples;
        r4.numBytesOfClearData = copy(r3.numBytesOfClearData, r4.numBytesOfClearData);
        r4.numBytesOfEncryptedData = copy(r3.numBytesOfEncryptedData, r4.numBytesOfEncryptedData);
        r4.key = (byte[]) Assertions.checkNotNull(copy(r3.key, r4.key));
        r4.iv = (byte[]) Assertions.checkNotNull(copy(r3.f34iv, r4.iv));
        r4.mode = r3.mode;
        if (Util.SDK_INT < 24) goto L6;
        r4.setPattern(new MediaCodec.CryptoInfo.Pattern(r3.encryptedBlocks, r3.clearBlocks));
        return;
    }

    private static byte[] copy(byte[] r2, byte[] r3) {
        if (r2 != null) goto L4;
        return r3;
    L4:
        if (r3 == null) goto L11;
        if (r3.length < r2.length) goto L11;
        System.arraycopy(r2, 0, r3, 0, r2.length);
        return r3;
    L11:
        return Arrays.copyOf(r2, r2.length);
    }

    private static int[] copy(int[] r2, int[] r3) {
        if (r2 != null) goto L4;
        return r3;
    L4:
        if (r3 == null) goto L11;
        if (r3.length < r2.length) goto L11;
        System.arraycopy(r2, 0, r3, 0, r2.length);
        return r3;
    L11:
        return Arrays.copyOf(r2, r2.length);
    }

    private void doHandleMessage(Message r9) {
        MessageParams r1 = null;
        switch(r9.what) {
            case 1: goto L8;
            case 2: goto L7;
            case 3: goto L6;
            case 4: goto L5;
            default: goto L4;
        };
    L4:
        C0305xa830b2f.m111m(this.pendingRuntimeException, null, new IllegalStateException(String.valueOf(r9.what)));
    L9:
        if (r1 == null) goto L12;
        recycleMessageParams(r1);
        return;
    L12:
        return;
    L5:
        doSetParameters((Bundle) r9.obj);
        goto L9
    L6:
        this.conditionVariable.open();
        goto L9
    L7:
        MessageParams r7 = (MessageParams) r9.obj;
        doQueueSecureInputBuffer(r7.index, r7.offset, r7.cryptoInfo, r7.presentationTimeUs, r7.flags);
        r1 = r7;
        goto L9
    L8:
        MessageParams r8 = (MessageParams) r9.obj;
        doQueueInputBuffer(r8.index, r8.offset, r8.size, r8.presentationTimeUs, r8.flags);
        r1 = r8;
        goto L9
    }

    private void doQueueInputBuffer(int r8, int r9, int r10, long r11, int r13) {
        this.codec.queueInputBuffer(r8, r9, r10, r11, r13);     // Catch: RuntimeException -> L6
        return;
    L6:
        e = move-exception;
        RuntimeException r12 = e;
    L10:
        C0305xa830b2f.m111m(this.pendingRuntimeException, null, r12);
        return;
    L8:
        e = move-exception;
        r12 = e;
        goto L10
    }

    private void doQueueSecureInputBuffer(int r10, int r11, MediaCodec.CryptoInfo r12, long r13, int r15) {
        Object r1 = QUEUE_SECURE_LOCK;     // Catch: RuntimeException -> L17
        monitor-enter(r1);     // Catch: RuntimeException -> L17
        this.codec.queueSecureInputBuffer(r10, r11, r12, r13, r15);     // Catch: Throwable -> L9
        monitor-exit(r1);     // Catch: Throwable -> L9
        return;
    L11:
        th = move-exception;
        Throwable r14 = th;
    L13:
        monitor-exit(r1);     // Catch: Throwable -> L9
        throw r14;     // Catch: RuntimeException -> L15
    L15:
        e = move-exception;
        RuntimeException r16 = e;
    L19:
        C0305xa830b2f.m111m(this.pendingRuntimeException, null, r16);
        return;
    L9:
        th = move-exception;
        r14 = th;
    L17:
        e = move-exception;
        r16 = e;
        goto L19
    }

    private void doSetParameters(Bundle r4) {
        this.codec.setParameters(r4);     // Catch: RuntimeException -> L4
        return;
    L4:
        e = move-exception;
        C0305xa830b2f.m111m(this.pendingRuntimeException, null, e);
    }

    private void flushHandlerThread() throws InterruptedException {
        ((Handler) Assertions.checkNotNull(this.handler)).removeCallbacksAndMessages(null);
        blockUntilHandlerThreadIsIdle();
    }

    private static MessageParams getMessageParams() {
        ArrayDeque<MessageParams> r0 = MESSAGE_PARAMS_INSTANCE_POOL;
        monitor-enter(r0);
    L12:
        th = move-exception;
        throw th;
    L5:
        if (MESSAGE_PARAMS_INSTANCE_POOL.isEmpty() == false) goto L9;
        MessageParams r1 = new MessageParams();     // Catch: Throwable -> L12
        monitor-exit(r0);     // Catch: Throwable -> L12
        return r1;
    L9:
        MessageParams r2 = MESSAGE_PARAMS_INSTANCE_POOL.removeFirst();     // Catch: Throwable -> L12
        monitor-exit(r0);     // Catch: Throwable -> L12
        return r2;
    }

    private static void recycleMessageParams(MessageParams r2) {
        ArrayDeque<MessageParams> r0 = MESSAGE_PARAMS_INSTANCE_POOL;
        monitor-enter(r0);
        MESSAGE_PARAMS_INSTANCE_POOL.add(r2);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void flush() {
        if (this.started == true) goto L10;
        return;
    L10:
        flushHandlerThread();     // Catch: InterruptedException -> L6
        return;
    L6:
        e = move-exception;
        Thread.currentThread().interrupt();
        throw new IllegalStateException(e);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void maybeThrowException() {
        RuntimeException r0 = this.pendingRuntimeException.getAndSet(null);
        if (r0 != null) goto L5;
        return;
    L5:
        throw r0;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void queueInputBuffer(int r8, int r9, int r10, long r11, int r13) {
        maybeThrowException();
        MessageParams r0 = getMessageParams();
        r0.setQueueParams(r8, r9, r10, r11, r13);
        ((Handler) Util.castNonNull(this.handler)).obtainMessage(1, r0).sendToTarget();
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void queueSecureInputBuffer(int r8, int r9, CryptoInfo r10, long r11, int r13) {
        maybeThrowException();
        MessageParams r0 = getMessageParams();
        r0.setQueueParams(r8, r9, 0, r11, r13);
        copy(r10, r0.cryptoInfo);
        ((Handler) Util.castNonNull(this.handler)).obtainMessage(2, r0).sendToTarget();
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void setParameters(Bundle r3) {
        maybeThrowException();
        ((Handler) Util.castNonNull(this.handler)).obtainMessage(4, r3).sendToTarget();
    }

    void setPendingRuntimeException(RuntimeException r2) {
        this.pendingRuntimeException.set(r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void shutdown() {
        if (this.started == false) goto L5;
        flush();
        this.handlerThread.quit();
    L5:
        this.started = false;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void start() {
        if (this.started == true) goto L6;
        this.handlerThread.start();
        final Looper r1 = this.handlerThread.getLooper();
        this.handler = new HandlerC03061(this, r1);
        this.started = true;
        return;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecBufferEnqueuer
    public void waitUntilQueueingComplete() throws InterruptedException {
        blockUntilHandlerThreadIsIdle();
    }
}
