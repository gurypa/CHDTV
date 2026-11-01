package androidx.media3.exoplayer.audio;

import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.common.util.Util;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
final class TrimmingAudioProcessor extends androidx.media3.common.audio.BaseAudioProcessor {
    private static final int OUTPUT_ENCODING = 2;
    private byte[] endBuffer;
    private int endBufferSize;
    private int pendingTrimStartBytes;
    private boolean reconfigurationPending;
    private int trimEndFrames;
    private int trimStartFrames;
    private long trimmedFrameCount;

    public TrimmingAudioProcessor() {
        this.endBuffer = Util.EMPTY_BYTE_ARRAY;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor, androidx.media3.common.audio.AudioProcessor
    public long getDurationAfterProcessorApplied(long r4) {
        return r4 - Util.sampleCountToDurationUs(this.trimEndFrames + this.trimStartFrames, this.inputAudioFormat.sampleRate);
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor, androidx.media3.common.audio.AudioProcessor
    public ByteBuffer getOutput() {
        if (super.isEnded() == false) goto L8;
        if (this.endBufferSize <= 0) goto L8;
        replaceOutputBuffer(this.endBufferSize).put(this.endBuffer, 0, this.endBufferSize).flip();
        this.endBufferSize = 0;
    L8:
        return super.getOutput();
    }

    public long getTrimmedFrameCount() {
        return this.trimmedFrameCount;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor, androidx.media3.common.audio.AudioProcessor
    public boolean isEnded() {
        if (super.isEnded() == true) goto L5;
    L7:
        return false;
    L5:
        if (this.endBufferSize != 0) goto L7;
        return true;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    public AudioProcessor.AudioFormat onConfigure(AudioProcessor.AudioFormat r3) throws AudioProcessor.UnhandledAudioFormatException {
        if (r3.encoding != 2) goto L13;
        this.reconfigurationPending = true;
        if (this.trimStartFrames != 0) goto L11;
        if (this.trimEndFrames != 0) goto L11;
        return AudioProcessor.AudioFormat.NOT_SET;
    L11:
        return r3;
    L13:
        throw new AudioProcessor.UnhandledAudioFormatException(r3);
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    protected void onFlush() {
        if (this.reconfigurationPending == false) goto L5;
        this.reconfigurationPending = false;
        this.endBuffer = new byte[this.trimEndFrames * this.inputAudioFormat.bytesPerFrame];
        this.pendingTrimStartBytes = this.trimStartFrames * this.inputAudioFormat.bytesPerFrame;
    L5:
        this.endBufferSize = 0;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    protected void onQueueEndOfStream() {
        if (this.reconfigurationPending == true) goto L5;
        return;
    L5:
        if (this.endBufferSize <= 0) goto L7;
        this.trimmedFrameCount += (long) (this.endBufferSize / this.inputAudioFormat.bytesPerFrame);
    L7:
        this.endBufferSize = 0;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    protected void onReset() {
        this.endBuffer = Util.EMPTY_BYTE_ARRAY;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public void queueInput(ByteBuffer r13) {
        int r0 = r13.position();
        int r1 = r13.limit();
        int r2 = r1 - r0;
        if (r2 != 0) goto L5;
        return;
    L5:
        int r3 = Math.min(r2, this.pendingTrimStartBytes);
        this.trimmedFrameCount += (long) (r3 / this.inputAudioFormat.bytesPerFrame);
        this.pendingTrimStartBytes -= r3;
        r13.position(r0 + r3);
        if (this.pendingTrimStartBytes <= 0) goto L8;
        return;
    L8:
        int r4 = r2 - r3;
        int r5 = (this.endBufferSize + r4) - this.endBuffer.length;
        ByteBuffer r6 = replaceOutputBuffer(r5);
        int r7 = Util.constrainValue(r5, 0, this.endBufferSize);
        r6.put(this.endBuffer, 0, r7);
        int r8 = Util.constrainValue(r5 - r7, 0, r4);
        r13.limit(r13.position() + r8);
        r6.put(r13);
        r13.limit(r1);
        int r9 = r4 - r8;
        this.endBufferSize -= r7;
        System.arraycopy(this.endBuffer, r7, this.endBuffer, 0, this.endBufferSize);
        r13.get(this.endBuffer, this.endBufferSize, r9);
        this.endBufferSize += r9;
        r6.flip();
    }

    public void resetTrimmedFrameCount() {
        this.trimmedFrameCount = 0;
    }

    public void setTrimFrameCount(int r1, int r2) {
        this.trimStartFrames = r1;
        this.trimEndFrames = r2;
    }
}
