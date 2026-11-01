package androidx.media3.decoder;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.decoder.DecoderException;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.decoder.DecoderOutputBuffer;
import java.util.ArrayDeque;

/* JADX INFO: loaded from: classes.dex */
public abstract class SimpleDecoder<I extends DecoderInputBuffer, O extends DecoderOutputBuffer, E extends DecoderException> implements Decoder<I, O, E> {
    private int availableInputBufferCount;
    private final I[] availableInputBuffers;
    private int availableOutputBufferCount;
    private final O[] availableOutputBuffers;
    private final Thread decodeThread;
    private I dequeuedInputBuffer;
    private E exception;
    private boolean flushed;
    private final Object lock;
    private long outputStartTimeUs;
    private final ArrayDeque<I> queuedInputBuffers;
    private final ArrayDeque<O> queuedOutputBuffers;
    private boolean released;
    private int skippedOutputBufferCount;

    protected SimpleDecoder(I[] r4, O[] r5) {
        this.lock = new Object();
        this.outputStartTimeUs = C0180C.TIME_UNSET;
        this.queuedInputBuffers = new ArrayDeque();
        this.queuedOutputBuffers = new ArrayDeque();
        this.availableInputBuffers = r4;
        this.availableInputBufferCount = r4.length;
        int r0 = 0;
    L4:
        if (r0 >= this.availableInputBufferCount) goto L6;
        ((I[]) this.availableInputBuffers)[r0] = createInputBuffer();
        r0 = r0 + 1;
        goto L4
    L6:
        this.availableOutputBuffers = r5;
        this.availableOutputBufferCount = r5.length;
        int r1 = 0;
    L8:
        if (r1 >= this.availableOutputBufferCount) goto L10;
        ((O[]) this.availableOutputBuffers)[r1] = createOutputBuffer();
        r1 = r1 + 1;
        goto L8
    L10:
        final String r2 = "ExoPlayer:SimpleDecoder";
        this.decodeThread = new C02211(this, r2);
        this.decodeThread.start();
    }

    static /* synthetic */ void access$000(SimpleDecoder r0) {
        r0.run();
    }

    private boolean canDecodeBuffer() {
        if (this.queuedInputBuffers.isEmpty() == false) goto L5;
    L7:
        return false;
    L5:
        if (this.availableOutputBufferCount <= 0) goto L7;
        return true;
    }

    private boolean decode() throws InterruptedException {
        Object r0 = this.lock;
        monitor-enter(r0);
    L61:
    L54:
        th = move-exception;
        throw th;
    L5:
        if (this.released == true) goto L10;
        if (canDecodeBuffer() == true) goto L10;
        this.lock.wait();     // Catch: Throwable -> L54
    L10:
        if (this.released == false) goto L13;
        monitor-exit(r0);     // Catch: Throwable -> L54
        return false;
    L13:
        I r1 = this.queuedInputBuffers.removeFirst();     // Catch: Throwable -> L54
        O[] r3 = this.availableOutputBuffers;     // Catch: Throwable -> L54
        int r4 = this.availableOutputBufferCount - 1;     // Catch: Throwable -> L54
        this.availableOutputBufferCount = r4;     // Catch: Throwable -> L54
        O r5 = r3[r4];     // Catch: Throwable -> L54
        boolean r6 = this.flushed;     // Catch: Throwable -> L54
        this.flushed = false;     // Catch: Throwable -> L54
        monitor-exit(r0);     // Catch: Throwable -> L54
        if (r1.isEndOfStream() == false) goto L18;
        r5.addFlag(4);
    L39:
        Object r7 = this.lock;
        monitor-enter(r7);
    L51:
        th = move-exception;
        throw th;
    L42:
        if (this.flushed == false) goto L45;
        r5.release();     // Catch: Throwable -> L51
    L48:
        releaseInputBufferInternal(r1);     // Catch: Throwable -> L51
        monitor-exit(r7);     // Catch: Throwable -> L51
        return true;
    L45:
        if (r5.shouldBeSkipped == false) goto L47;
        this.skippedOutputBufferCount++;     // Catch: Throwable -> L51
        r5.release();     // Catch: Throwable -> L51
        goto L48
    L47:
        r5.skippedOutputBufferCount = this.skippedOutputBufferCount;     // Catch: Throwable -> L51
        this.skippedOutputBufferCount = 0;     // Catch: Throwable -> L51
        this.queuedOutputBuffers.addLast(r5);     // Catch: Throwable -> L51
        goto L48
    L18:
        r5.timeUs = r1.timeUs;
        if (r1.isFirstSample() == false) goto L22;
        r5.addFlag(C0180C.BUFFER_FLAG_FIRST_SAMPLE);
    L22:
        if (isAtLeastOutputStartTimeUs(r1.timeUs) == true) goto L62;
        r5.shouldBeSkipped = true;
    L62:
        E r2 = (E) decode(r1, r5, r6);
    L30:
        if (r2 == null) goto L39;
        Object r8 = this.lock;
        monitor-enter(r8);
        this.exception = r2;     // Catch: Throwable -> L36
        monitor-exit(r8);     // Catch: Throwable -> L36
        return false;
    L36:
        th = move-exception;
        throw th;
    L26:
        e = move-exception;
        r2 = (E) createUnexpectedDecodeException(e);
    L28:
        e = move-exception;
        r2 = (E) createUnexpectedDecodeException(e);
        goto L30
    }

    private void maybeNotifyDecodeLoop() {
        if (canDecodeBuffer() == false) goto L6;
        this.lock.notify();
        return;
    }

    private void maybeThrowException() throws DecoderException {
        E r0 = this.exception;
        if (r0 != null) goto L5;
        return;
    L5:
        throw r0;
    }

    private void releaseInputBufferInternal(I r4) {
        r4.clear();
        I[] r0 = this.availableInputBuffers;
        int r1 = this.availableInputBufferCount;
        this.availableInputBufferCount = r1 + 1;
        r0[r1] = r4;
    }

    private void releaseOutputBufferInternal(O r4) {
        r4.clear();
        O[] r0 = this.availableOutputBuffers;
        int r1 = this.availableOutputBufferCount;
        this.availableOutputBufferCount = r1 + 1;
        r0[r1] = r4;
    }

    private void run() {
    L10:
        if (decode() == true) goto L10;
        return;
    L7:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    protected abstract I createInputBuffer();

    protected abstract O createOutputBuffer();

    protected abstract E createUnexpectedDecodeException(Throwable r1);

    protected abstract E decode(I r1, O r2, boolean r3);

    @Override // androidx.media3.decoder.Decoder
    public final I dequeueInputBuffer() throws DecoderException {
        Object r0 = this.lock;
        monitor-enter(r0);
        maybeThrowException();     // Catch: Throwable -> L15
        if (this.dequeuedInputBuffer != null) goto L7;
        boolean r1 = true;
    L8:
        Assertions.checkState(r1);     // Catch: Throwable -> L15
        if (this.availableInputBufferCount != 0) goto L11;
        I r2 = null;
    L12:
        this.dequeuedInputBuffer = r2;     // Catch: Throwable -> L15
        I r3 = this.dequeuedInputBuffer;     // Catch: Throwable -> L15
        monitor-exit(r0);     // Catch: Throwable -> L15
        return r3;
    L11:
        I[] r4 = this.availableInputBuffers;     // Catch: Throwable -> L15
        int r5 = this.availableInputBufferCount - 1;     // Catch: Throwable -> L15
        this.availableInputBufferCount = r5;     // Catch: Throwable -> L15
        r2 = r4[r5];     // Catch: Throwable -> L15
        goto L12
    L7:
        r1 = false;
    L15:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ Object dequeueInputBuffer() throws DecoderException {
        return dequeueInputBuffer();
    }

    @Override // androidx.media3.decoder.Decoder
    public final O dequeueOutputBuffer() throws DecoderException {
        Object r0 = this.lock;
        monitor-enter(r0);
        maybeThrowException();     // Catch: Throwable -> L12
        if (this.queuedOutputBuffers.isEmpty() == false) goto L9;
        monitor-exit(r0);     // Catch: Throwable -> L12
        return null;
    L9:
        O r1 = this.queuedOutputBuffers.removeFirst();     // Catch: Throwable -> L12
        monitor-exit(r0);     // Catch: Throwable -> L12
        return r1;
    L12:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ Object dequeueOutputBuffer() throws DecoderException {
        return dequeueOutputBuffer();
    }

    @Override // androidx.media3.decoder.Decoder
    public final void flush() {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.flushed = true;     // Catch: Throwable -> L16
        this.skippedOutputBufferCount = 0;     // Catch: Throwable -> L16
        if (this.dequeuedInputBuffer == null) goto L9;
        releaseInputBufferInternal(this.dequeuedInputBuffer);     // Catch: Throwable -> L16
        this.dequeuedInputBuffer = null;     // Catch: Throwable -> L16
    L9:
        if (this.queuedInputBuffers.isEmpty() == true) goto L12;
        releaseInputBufferInternal(this.queuedInputBuffers.removeFirst());     // Catch: Throwable -> L16
    L12:
        if (this.queuedOutputBuffers.isEmpty() == true) goto L14;
        this.queuedOutputBuffers.removeFirst().release();     // Catch: Throwable -> L16
        goto L12
    L14:
        monitor-exit(r0);     // Catch: Throwable -> L16
        return;
    L16:
        th = move-exception;
        throw th;
    }

    protected final boolean isAtLeastOutputStartTimeUs(long r6) {
        Object r0 = this.lock;
        monitor-enter(r0);
    L13:
        th = move-exception;
        throw th;
    L5:
        if (this.outputStartTimeUs != C0180C.TIME_UNSET) goto L7;
    L10:
        boolean r1 = true;
    L11:
        monitor-exit(r0);     // Catch: Throwable -> L13
        return r1;
    L7:
        if (r6 >= this.outputStartTimeUs) goto L10;
        r1 = false;
        goto L11
    }

    public final void queueInputBuffer(I r3) throws DecoderException {
        Object r0 = this.lock;
        monitor-enter(r0);
        maybeThrowException();     // Catch: Throwable -> L11
        if (r3 != this.dequeuedInputBuffer) goto L7;
        boolean r1 = true;
    L8:
        Assertions.checkArgument(r1);     // Catch: Throwable -> L11
        this.queuedInputBuffers.addLast(r3);     // Catch: Throwable -> L11
        maybeNotifyDecodeLoop();     // Catch: Throwable -> L11
        this.dequeuedInputBuffer = null;     // Catch: Throwable -> L11
        monitor-exit(r0);     // Catch: Throwable -> L11
        return;
    L7:
        r1 = false;
    L11:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ void queueInputBuffer(Object r1) throws DecoderException {
        queueInputBuffer((DecoderInputBuffer) r1);
    }

    @Override // androidx.media3.decoder.Decoder
    public void release() {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.released = true;     // Catch: Throwable -> L12
        this.lock.notify();     // Catch: Throwable -> L12
        monitor-exit(r0);     // Catch: Throwable -> L12
        this.decodeThread.join();     // Catch: InterruptedException -> L9
        return;
    L10:
        Thread.currentThread().interrupt();
        return;
    L12:
        th = move-exception;
        throw th;
    }

    protected void releaseOutputBuffer(O r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
        releaseOutputBufferInternal(r3);     // Catch: Throwable -> L7
        maybeNotifyDecodeLoop();     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    protected final void setInitialInputBufferSize(int r5) {
        int r2 = 0;
        if (this.availableInputBufferCount != this.availableInputBuffers.length) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        I[] r1 = this.availableInputBuffers;
        int r3 = r1.length;
    L7:
        if (r2 >= r3) goto L9;
        r1[r2].ensureSpaceForWrite(r5);
        r2 = r2 + 1;
        goto L7
    L9:
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.decoder.Decoder
    public final void setOutputStartTimeUs(long r4) {
        Object r0 = this.lock;
        monitor-enter(r0);
    L14:
        th = move-exception;
        throw th;
    L5:
        if (this.availableInputBufferCount != this.availableInputBuffers.length) goto L7;
    L10:
        boolean r1 = true;
    L11:
        Assertions.checkState(r1);     // Catch: Throwable -> L14
        this.outputStartTimeUs = r4;     // Catch: Throwable -> L14
        monitor-exit(r0);     // Catch: Throwable -> L14
        return;
    L7:
        if (this.flushed == true) goto L10;
        r1 = false;
        goto L11
    }
}
