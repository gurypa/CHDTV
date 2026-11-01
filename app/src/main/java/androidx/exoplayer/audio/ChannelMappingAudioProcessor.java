package androidx.media3.exoplayer.audio;

import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.common.util.Assertions;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
final class ChannelMappingAudioProcessor extends androidx.media3.common.audio.BaseAudioProcessor {
    private int[] outputChannels;
    private int[] pendingOutputChannels;

    ChannelMappingAudioProcessor() {
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    public AudioProcessor.AudioFormat onConfigure(AudioProcessor.AudioFormat r9) throws AudioProcessor.UnhandledAudioFormatException {
        int[] r0 = this.pendingOutputChannels;
        if (r0 != null) goto L7;
        return AudioProcessor.AudioFormat.NOT_SET;
    L7:
        if (r9.encoding != 2) goto L28;
        if (r9.channelCount == r0.length) goto L11;
        boolean r1 = true;
    L12:
        int r3 = 0;
    L14:
        if (r3 >= r0.length) goto L23;
        int r6 = r0[r3];
        if (r6 >= r9.channelCount) goto L22;
        if (r6 == r3) goto L19;
        boolean r7 = true;
    L20:
        r1 = r1 | r7;
        r3 = r3 + 1;
        goto L14
    L19:
        r7 = false;
        goto L20
    L22:
        throw new AudioProcessor.UnhandledAudioFormatException(r9);
    L23:
        if (r1 == false) goto L26;
        return new AudioProcessor.AudioFormat(r9.sampleRate, r0.length, 2);
    L26:
        return AudioProcessor.AudioFormat.NOT_SET;
    L11:
        r1 = false;
        goto L12
    L28:
        throw new AudioProcessor.UnhandledAudioFormatException(r9);
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    protected void onFlush() {
        this.outputChannels = this.pendingOutputChannels;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    protected void onReset() {
        this.outputChannels = null;
        this.pendingOutputChannels = null;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public void queueInput(ByteBuffer r11) {
        int[] r0 = (int[]) Assertions.checkNotNull(this.outputChannels);
        int r1 = r11.position();
        int r2 = r11.limit();
        int r3 = (r2 - r1) / this.inputAudioFormat.bytesPerFrame;
        ByteBuffer r5 = replaceOutputBuffer(this.outputAudioFormat.bytesPerFrame * r3);
    L3:
        if (r1 >= r2) goto L8;
        int r6 = r0.length;
        int r7 = 0;
    L5:
        if (r7 >= r6) goto L7;
        r5.putShort(r11.getShort((r0[r7] * 2) + r1));
        r7 = r7 + 1;
        goto L5
    L7:
        r1 = r1 + this.inputAudioFormat.bytesPerFrame;
        goto L3
    L8:
        r11.position(r2);
        r5.flip();
    }

    public void setChannelMap(int[] r1) {
        this.pendingOutputChannels = r1;
    }
}
