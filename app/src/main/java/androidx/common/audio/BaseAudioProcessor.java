package androidx.media3.common.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseAudioProcessor implements AudioProcessor {
    private ByteBuffer buffer;
    protected AudioProcessor.AudioFormat inputAudioFormat;
    private boolean inputEnded;
    protected AudioProcessor.AudioFormat outputAudioFormat;
    private ByteBuffer outputBuffer;
    private AudioProcessor.AudioFormat pendingInputAudioFormat;
    private AudioProcessor.AudioFormat pendingOutputAudioFormat;

    public BaseAudioProcessor() {
        this.buffer = EMPTY_BUFFER;
        this.outputBuffer = EMPTY_BUFFER;
        this.pendingInputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.pendingOutputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.inputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.outputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final AudioProcessor.AudioFormat configure(AudioProcessor.AudioFormat r2) throws AudioProcessor.UnhandledAudioFormatException {
        this.pendingInputAudioFormat = r2;
        this.pendingOutputAudioFormat = onConfigure(r2);
        if (isActive() == false) goto L6;
        return this.pendingOutputAudioFormat;
    L6:
        return AudioProcessor.AudioFormat.NOT_SET;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final void flush() {
        this.outputBuffer = EMPTY_BUFFER;
        this.inputEnded = false;
        this.inputAudioFormat = this.pendingInputAudioFormat;
        this.outputAudioFormat = this.pendingOutputAudioFormat;
        onFlush();
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public /* synthetic */ long getDurationAfterProcessorApplied(long r1) {
        return AudioProcessor.CC.$default$getDurationAfterProcessorApplied(this, r1);
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public ByteBuffer getOutput() {
        ByteBuffer r0 = this.outputBuffer;
        this.outputBuffer = EMPTY_BUFFER;
        return r0;
    }

    protected final boolean hasPendingOutput() {
        return this.outputBuffer.hasRemaining();
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public boolean isActive() {
        if (this.pendingOutputAudioFormat == AudioProcessor.AudioFormat.NOT_SET) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public boolean isEnded() {
        if (this.inputEnded == true) goto L5;
    L7:
        return false;
    L5:
        if (this.outputBuffer != EMPTY_BUFFER) goto L7;
        return true;
    }

    protected AudioProcessor.AudioFormat onConfigure(AudioProcessor.AudioFormat r2) throws AudioProcessor.UnhandledAudioFormatException {
        return AudioProcessor.AudioFormat.NOT_SET;
    }

    protected void onFlush() {
    }

    protected void onQueueEndOfStream() {
    }

    protected void onReset() {
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final void queueEndOfStream() {
        this.inputEnded = true;
        onQueueEndOfStream();
    }

    protected final ByteBuffer replaceOutputBuffer(int r3) {
        if (this.buffer.capacity() >= r3) goto L5;
        this.buffer = ByteBuffer.allocateDirect(r3).order(ByteOrder.nativeOrder());
    L6:
        this.outputBuffer = this.buffer;
        return this.buffer;
    L5:
        this.buffer.clear();
        goto L6
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final void reset() {
        flush();
        this.buffer = EMPTY_BUFFER;
        this.pendingInputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.pendingOutputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.inputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.outputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        onReset();
    }
}
