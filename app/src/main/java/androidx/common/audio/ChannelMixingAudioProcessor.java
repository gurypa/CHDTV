package androidx.media3.common.audio;

import android.util.SparseArray;
import androidx.media3.common.util.Assertions;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public final class ChannelMixingAudioProcessor extends BaseAudioProcessor {
    private final SparseArray<ChannelMixingMatrix> matrixByInputChannelCount;

    public ChannelMixingAudioProcessor() {
        this.matrixByInputChannelCount = new SparseArray();
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    protected AudioProcessor.AudioFormat onConfigure(AudioProcessor.AudioFormat r6) throws AudioProcessor.UnhandledAudioFormatException {
        if (r6.encoding != 2) goto L15;
        ChannelMixingMatrix r0 = this.matrixByInputChannelCount.get(r6.channelCount);
        if (r0 == null) goto L13;
        if (r0.isIdentity() == false) goto L11;
        return AudioProcessor.AudioFormat.NOT_SET;
    L11:
        return new AudioProcessor.AudioFormat(r6.sampleRate, r0.getOutputChannelCount(), 2);
    L13:
        throw new AudioProcessor.UnhandledAudioFormatException("No mixing matrix for input channel count", r6);
    L15:
        throw new AudioProcessor.UnhandledAudioFormatException(r6);
    }

    public void putChannelMixingMatrix(ChannelMixingMatrix r3) {
        int r0 = r3.getInputChannelCount();
        this.matrixByInputChannelCount.put(r0, r3);
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public void queueInput(ByteBuffer r10) {
        ChannelMixingMatrix r5 = (ChannelMixingMatrix) Assertions.checkStateNotNull(this.matrixByInputChannelCount.get(this.inputAudioFormat.channelCount));
        int r6 = r10.remaining() / this.inputAudioFormat.bytesPerFrame;
        ByteBuffer r3 = replaceOutputBuffer(this.outputAudioFormat.bytesPerFrame * r6);
        AudioMixingUtil.mix(r10, this.inputAudioFormat, r3, this.outputAudioFormat, r5, r6, false, true);
        r3.flip();
    }
}
