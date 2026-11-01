package androidx.media3.exoplayer.audio;

import android.util.SparseArray;
import androidx.media3.common.audio.AudioMixingUtil;
import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.common.audio.ChannelMixingMatrix;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public class WaveformAudioBufferSink implements TeeAudioProcessor.AudioBufferSink {
    private final int barsPerSecond;
    private ChannelMixingMatrix channelMixingMatrix;
    private AudioProcessor.AudioFormat inputAudioFormat;
    private final Listener listener;
    private AudioProcessor.AudioFormat mixingAudioFormat;
    private final ByteBuffer mixingBuffer;
    private final SparseArray<WaveformBar> outputChannels;
    private int samplesPerBar;

    public interface Listener {
        void onNewWaveformBar(int r1, WaveformBar r2);
    }

    public static class WaveformBar {
        private float maxSampleValue;
        private float minSampleValue;
        private int sampleCount;
        private double squareSum;

        public WaveformBar() {
            this.minSampleValue = 1.0f;
            this.maxSampleValue = -1.0f;
        }

        public void addSample(float r9) {
            if (r9 >= (-1.0f)) goto L5;
        L7:
            boolean r0 = false;
        L8:
            Preconditions.checkArgument(r0);
            this.minSampleValue = Math.min(this.minSampleValue, r9);
            this.maxSampleValue = Math.max(this.maxSampleValue, r9);
            this.squareSum += ((double) r9) * ((double) r9);
            this.sampleCount++;
            return;
        L5:
            if (r9 > 1.0f) goto L7;
            r0 = true;
            goto L8
        }

        public double getMaxSampleValue() {
            return this.maxSampleValue;
        }

        public double getMinSampleValue() {
            return this.minSampleValue;
        }

        public double getRootMeanSquare() {
            return Math.sqrt(this.squareSum / ((double) this.sampleCount));
        }

        public int getSampleCount() {
            return this.sampleCount;
        }
    }

    public WaveformAudioBufferSink(int r4, int r5, Listener r6) {
        this.barsPerSecond = r4;
        this.listener = r6;
        this.mixingBuffer = ByteBuffer.allocate(Util.getPcmFrameSize(4, r5));
        this.outputChannels = new SparseArray(r5);
        int r0 = 0;
    L3:
        if (r0 >= r5) goto L5;
        this.outputChannels.append(r0, new WaveformBar());
        r0 = r0 + 1;
        goto L3
    }

    @Override // androidx.media3.exoplayer.audio.TeeAudioProcessor.AudioBufferSink
    public void flush(int r4, int r5, int r6) {
        this.samplesPerBar = r4 / this.barsPerSecond;
        this.inputAudioFormat = new AudioProcessor.AudioFormat(r4, r5, r6);
        this.mixingAudioFormat = new AudioProcessor.AudioFormat(r4, this.outputChannels.size(), 4);
        this.channelMixingMatrix = ChannelMixingMatrix.create(r5, this.outputChannels.size());
    }

    @Override // androidx.media3.exoplayer.audio.TeeAudioProcessor.AudioBufferSink
    public void handleBuffer(ByteBuffer r10) {
        Assertions.checkStateNotNull(this.inputAudioFormat);
        Assertions.checkStateNotNull(this.mixingAudioFormat);
        Assertions.checkStateNotNull(this.channelMixingMatrix);
    L4:
        if (r10.hasRemaining() == false) goto L13;
        this.mixingBuffer.rewind();
        ByteBuffer r1 = r10;
        AudioMixingUtil.mix(r1, this.inputAudioFormat, this.mixingBuffer, this.mixingAudioFormat, this.channelMixingMatrix, 1, false, true);
        this.mixingBuffer.rewind();
        int r11 = 0;
    L7:
        if (r11 >= this.outputChannels.size()) goto L12;
        WaveformBar r0 = this.outputChannels.get(r11);
        r0.addSample(this.mixingBuffer.getFloat());
        if (r0.getSampleCount() < this.samplesPerBar) goto L11;
        this.listener.onNewWaveformBar(r11, r0);
        this.outputChannels.put(r11, new WaveformBar());
    L11:
        r11 = r11 + 1;
        goto L7
    L12:
        r10 = r1;
        goto L4
    }
}
