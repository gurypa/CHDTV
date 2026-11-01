package androidx.media3.common.audio;

import androidx.media3.common.util.Assertions;
import com.google.common.collect.ImmutableList;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class AudioProcessingPipeline {
    private final List<AudioProcessor> activeAudioProcessors;
    private final ImmutableList<AudioProcessor> audioProcessors;
    private boolean inputEnded;
    private AudioProcessor.AudioFormat outputAudioFormat;
    private ByteBuffer[] outputBuffers;
    private AudioProcessor.AudioFormat pendingOutputAudioFormat;

    public AudioProcessingPipeline(ImmutableList<AudioProcessor> r3) {
        this.audioProcessors = r3;
        this.activeAudioProcessors = new ArrayList();
        this.outputBuffers = new ByteBuffer[0];
        this.outputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.pendingOutputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.inputEnded = false;
    }

    private int getFinalOutputBufferIndex() {
        return this.outputBuffers.length - 1;
    }

    private void processData(ByteBuffer r11) {
        boolean r0 = true;
    L3:
        if (r0 == false) goto L33;
        r0 = false;
        int r1 = 0;
    L6:
        if (r1 > getFinalOutputBufferIndex()) goto L3;
        if (this.outputBuffers[r1].hasRemaining() == true) goto L31;
        AudioProcessor r2 = this.activeAudioProcessors.get(r1);
        if (r2.isEnded() == true) goto L13;
        if (r1 <= 0) goto L20;
        ByteBuffer r3 = this.outputBuffers[r1 - 1];
    L23:
        long r4 = r3.remaining();
        r2.queueInput(r3);
        this.outputBuffers[r1] = r2.getOutput();
        if ((r4 - ((long) r3.remaining())) <= 0) goto L26;
    L29:
        boolean r6 = true;
    L30:
        r0 = r0 | r6;
        goto L31
    L26:
        if (this.outputBuffers[r1].hasRemaining() == true) goto L29;
        r6 = false;
        goto L30
    L20:
        if (r11.hasRemaining() == false) goto L22;
        r3 = r11;
        goto L23
    L22:
        r3 = AudioProcessor.EMPTY_BUFFER;
        goto L23
    L13:
        if (this.outputBuffers[r1].hasRemaining() == true) goto L31;
        if (r1 >= getFinalOutputBufferIndex()) goto L31;
        this.activeAudioProcessors.get(r1 + 1).queueEndOfStream();
    L31:
        r1 = r1 + 1;
        goto L6
    }

    public AudioProcessor.AudioFormat configure(AudioProcessor.AudioFormat r6) throws AudioProcessor.UnhandledAudioFormatException {
        if (r6.equals(AudioProcessor.AudioFormat.NOT_SET) == true) goto L14;
        AudioProcessor.AudioFormat r0 = r6;
        int r1 = 0;
    L6:
        if (r1 >= this.audioProcessors.size()) goto L11;
        AudioProcessor r2 = this.audioProcessors.get(r1);
        AudioProcessor.AudioFormat r3 = r2.configure(r0);
        if (r2.isActive() == false) goto L10;
        Assertions.checkState(!r3.equals(AudioProcessor.AudioFormat.NOT_SET));
        r0 = r3;
    L10:
        r1 = r1 + 1;
        goto L6
    L11:
        this.pendingOutputAudioFormat = r0;
        return r0;
    L14:
        throw new AudioProcessor.UnhandledAudioFormatException(r6);
    }

    public boolean equals(Object r7) {
        if (this != r7) goto L6;
        return true;
    L6:
        if ((r7 instanceof AudioProcessingPipeline) == true) goto L8;
        return false;
    L8:
        AudioProcessingPipeline r1 = (AudioProcessingPipeline) r7;
        if (this.audioProcessors.size() == r1.audioProcessors.size()) goto L11;
        return false;
    L11:
        int r3 = 0;
    L13:
        if (r3 >= this.audioProcessors.size()) goto L18;
        if (this.audioProcessors.get(r3) != r1.audioProcessors.get(r3)) goto L16;
        r3 = r3 + 1;
        goto L13
    L16:
        return false;
    L18:
        return true;
    }

    public void flush() {
        this.activeAudioProcessors.clear();
        this.outputAudioFormat = this.pendingOutputAudioFormat;
        this.inputEnded = false;
        int r0 = 0;
    L4:
        if (r0 >= this.audioProcessors.size()) goto L9;
        AudioProcessor r1 = this.audioProcessors.get(r0);
        r1.flush();
        if (r1.isActive() == false) goto L8;
        this.activeAudioProcessors.add(r1);
    L8:
        r0 = r0 + 1;
        goto L4
    L9:
        this.outputBuffers = new ByteBuffer[this.activeAudioProcessors.size()];
        int r2 = 0;
    L11:
        if (r2 > getFinalOutputBufferIndex()) goto L13;
        this.outputBuffers[r2] = this.activeAudioProcessors.get(r2).getOutput();
        r2 = r2 + 1;
        goto L11
    }

    public ByteBuffer getOutput() {
        if (isOperational() == false) goto L5;
        ByteBuffer r0 = this.outputBuffers[getFinalOutputBufferIndex()];
        if (r0.hasRemaining() == false) goto L9;
        return r0;
    L9:
        processData(AudioProcessor.EMPTY_BUFFER);
        return this.outputBuffers[getFinalOutputBufferIndex()];
    L5:
        return AudioProcessor.EMPTY_BUFFER;
    }

    public AudioProcessor.AudioFormat getOutputAudioFormat() {
        return this.outputAudioFormat;
    }

    public int hashCode() {
        return this.audioProcessors.hashCode();
    }

    public boolean isEnded() {
        if (this.inputEnded == true) goto L5;
    L9:
        return false;
    L5:
        if (this.activeAudioProcessors.get(getFinalOutputBufferIndex()).isEnded() == false) goto L9;
        if (this.outputBuffers[getFinalOutputBufferIndex()].hasRemaining() == true) goto L9;
        return true;
    }

    public boolean isOperational() {
        return !this.activeAudioProcessors.isEmpty();
    }

    public void queueEndOfStream() {
        if (isOperational() == true) goto L5;
        return;
    L5:
        if (this.inputEnded == true) goto L10;
        this.inputEnded = true;
        this.activeAudioProcessors.get(0).queueEndOfStream();
        return;
    }

    public void queueInput(ByteBuffer r2) {
        if (isOperational() == true) goto L5;
        return;
    L5:
        if (this.inputEnded == true) goto L10;
        processData(r2);
        return;
    }

    public void reset() {
        int r0 = 0;
    L4:
        if (r0 >= this.audioProcessors.size()) goto L6;
        AudioProcessor r1 = this.audioProcessors.get(r0);
        r1.flush();
        r1.reset();
        r0 = r0 + 1;
        goto L4
    L6:
        this.outputBuffers = new ByteBuffer[0];
        this.outputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.pendingOutputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.inputEnded = false;
    }
}
