package androidx.media3.exoplayer.audio;

import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public final class SilenceSkippingAudioProcessor extends androidx.media3.common.audio.BaseAudioProcessor {
    private static final int AVOID_TRUNCATION_FACTOR = 1000;
    public static final long DEFAULT_MAX_SILENCE_TO_KEEP_DURATION_US = 2000000;
    public static final long DEFAULT_MINIMUM_SILENCE_DURATION_US = 100000;
    public static final int DEFAULT_MIN_VOLUME_TO_KEEP_PERCENTAGE = 10;

    @Deprecated
    public static final long DEFAULT_PADDING_SILENCE_US = 20000;
    public static final float DEFAULT_SILENCE_RETENTION_RATIO = 0.2f;
    public static final short DEFAULT_SILENCE_THRESHOLD_LEVEL = 1024;
    private static final int DO_NOT_CHANGE_VOLUME = 3;
    private static final int FADE_IN = 2;
    private static final int FADE_OUT = 0;
    private static final int MUTE = 1;
    private static final int STATE_NOISY = 0;
    private static final int STATE_SHORTENING_SILENCE = 1;
    private int bytesPerFrame;
    private byte[] contiguousOutputBuffer;
    private boolean enabled;
    private final long maxSilenceToKeepDurationUs;
    private byte[] maybeSilenceBuffer;
    private int maybeSilenceBufferContentsSize;
    private int maybeSilenceBufferStartIndex;
    private final int minVolumeToKeepPercentageWhenMuting;
    private final long minimumSilenceDurationUs;
    private int outputSilenceFramesSinceNoise;
    private final float silenceRetentionRatio;
    private final short silenceThresholdLevel;
    private long skippedFrames;
    private int state;

    public SilenceSkippingAudioProcessor() {
        this(DEFAULT_MINIMUM_SILENCE_DURATION_US, 0.2f, DEFAULT_MAX_SILENCE_TO_KEEP_DURATION_US, 10, DEFAULT_SILENCE_THRESHOLD_LEVEL);
    }

    public SilenceSkippingAudioProcessor(long r3, float r5, long r6, int r8, short r9) {
        boolean r0 = false;
        this.outputSilenceFramesSinceNoise = 0;
        this.maybeSilenceBufferStartIndex = 0;
        this.maybeSilenceBufferContentsSize = 0;
        if (r5 >= 0.0f) goto L5;
    L7:
        Assertions.checkArgument(r0);
        this.minimumSilenceDurationUs = r3;
        this.silenceRetentionRatio = r5;
        this.maxSilenceToKeepDurationUs = r6;
        this.minVolumeToKeepPercentageWhenMuting = r8;
        this.silenceThresholdLevel = r9;
        this.maybeSilenceBuffer = Util.EMPTY_BYTE_ARRAY;
        this.contiguousOutputBuffer = Util.EMPTY_BYTE_ARRAY;
        return;
    L5:
        if (r5 > 1.0f) goto L7;
        r0 = true;
        goto L7
    }

    @Deprecated
    public SilenceSkippingAudioProcessor(long r11, long r13, short r15) {
        this(r11, r13 / r11, r11, 0, r15);
    }

    private int alignToBytePerFrameBoundary(float r2) {
        return alignToBytePerFrameBoundary((int) r2);
    }

    private int alignToBytePerFrameBoundary(int r3) {
        return (r3 / this.bytesPerFrame) * this.bytesPerFrame;
    }

    private int calculateFadeInPercentage(int r4, int r5) {
        return this.minVolumeToKeepPercentageWhenMuting + ((((100 - this.minVolumeToKeepPercentageWhenMuting) * (r4 * 1000)) / r5) / 1000);
    }

    private int calculateFadeOutPercentage(int r3, int r4) {
        return (((this.minVolumeToKeepPercentageWhenMuting - 100) * ((r3 * 1000) / r4)) / 1000) + 100;
    }

    private int calculateShortenedSilenceLength(int r4) {
        int r0 = ((durationUsToFrames(this.maxSilenceToKeepDurationUs) - this.outputSilenceFramesSinceNoise) * this.bytesPerFrame) - (this.maybeSilenceBuffer.length / 2);
        if (r0 < 0) goto L5;
        boolean r1 = true;
    L6:
        Assertions.checkState(r1);
        return alignToBytePerFrameBoundary(Math.min((r4 * this.silenceRetentionRatio) + 0.5f, r0));
    L5:
        r1 = false;
        goto L6
    }

    private int durationUsToFrames(long r5) {
        return (int) ((((long) this.inputAudioFormat.sampleRate) * r5) / 1000000);
    }

    private int findNoiseLimit(ByteBuffer r4) {
        int r0 = r4.limit() - 1;
    L4:
        if (r0 < r4.position()) goto L11;
        if (isNoise(r4.get(r0), r4.get(r0 - 1)) == true) goto L8;
        r0 = r0 - 2;
        goto L4
    L8:
        return (this.bytesPerFrame * (r0 / this.bytesPerFrame)) + this.bytesPerFrame;
    L11:
        return r4.position();
    }

    private int findNoisePosition(ByteBuffer r4) {
        int r0 = r4.position() + 1;
    L4:
        if (r0 >= r4.limit()) goto L11;
        if (isNoise(r4.get(r0), r4.get(r0 - 1)) == true) goto L8;
        r0 = r0 + 2;
        goto L4
    L8:
        return this.bytesPerFrame * (r0 / this.bytesPerFrame);
    L11:
        return r4.limit();
    }

    private boolean isNoise(byte r3, byte r4) {
        if (Math.abs(twoByteSampleToInt(r3, r4)) <= this.silenceThresholdLevel) goto L5;
        return true;
    L5:
        return false;
    }

    private void modifyVolume(byte[] r7, int r8, int r9) {
        if (r9 != 3) goto L5;
        return;
    L5:
        int r0 = 0;
    L6:
        if (r0 >= r8) goto L15;
        int r3 = twoByteSampleToInt(r7[r0 + 1], r7[r0]);
        if (r9 != 0) goto L11;
        int r4 = calculateFadeOutPercentage(r0, r8 - 1);
    L14:
        sampleIntToTwoBigEndianBytes(r7, r0, (r3 * r4) / 100);
        r0 = r0 + 2;
        goto L6
    L11:
        if (r9 != 2) goto L13;
        r4 = calculateFadeInPercentage(r0, r8 - 1);
        goto L14
    L13:
        r4 = this.minVolumeToKeepPercentageWhenMuting;
        goto L14
    }

    private void output(ByteBuffer r2) {
        replaceOutputBuffer(r2.remaining()).put(r2).flip();
    }

    private void outputRange(byte[] r5, int r6, int r7) {
        if ((r6 % this.bytesPerFrame) != 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0, "byteOutput size is not aligned to frame size " + r6);
        modifyVolume(r5, r6, r7);
        replaceOutputBuffer(r6).put(r5, 0, r6).flip();
        return;
    L5:
        r0 = false;
        goto L6
    }

    private void outputShortenedSilenceBuffer(boolean r10) {
        int r0 = this.maybeSilenceBufferContentsSize;
        if (this.maybeSilenceBufferContentsSize == this.maybeSilenceBuffer.length) goto L5;
        if (r10 == true) goto L5;
        return;
    L5:
        boolean r2 = true;
        if (this.outputSilenceFramesSinceNoise != 0) goto L14;
        if (r10 == false) goto L10;
        int r4 = this.maybeSilenceBufferContentsSize;
        outputSilence(r4, 3);
        int r5 = r4;
    L18:
        if ((r5 % this.bytesPerFrame) != 0) goto L20;
        boolean r6 = true;
    L21:
        Assertions.checkState(r6, "bytesConsumed is not aligned to frame size: %s" + r5);
        if (r0 >= r4) goto L25;
        r2 = false;
    L25:
        Assertions.checkState(r2);
        this.maybeSilenceBufferContentsSize -= r5;
        this.maybeSilenceBufferStartIndex += r5;
        this.maybeSilenceBufferStartIndex %= this.maybeSilenceBuffer.length;
        this.outputSilenceFramesSinceNoise += r4 / this.bytesPerFrame;
        this.skippedFrames += (long) ((r5 - r4) / this.bytesPerFrame);
        return;
    L20:
        r6 = false;
        goto L21
    L10:
        if (this.maybeSilenceBufferContentsSize < (this.maybeSilenceBuffer.length / 2)) goto L12;
        boolean r1 = true;
    L13:
        Assertions.checkState(r1);
        r4 = this.maybeSilenceBuffer.length / 2;
        outputSilence(r4, 0);
        r5 = r4;
        goto L18
    L12:
        r1 = false;
        goto L13
    L14:
        if (r10 == false) goto L16;
        int r7 = this.maybeSilenceBufferContentsSize - (this.maybeSilenceBuffer.length / 2);
        r5 = (this.maybeSilenceBuffer.length / 2) + r7;
        int r8 = (this.maybeSilenceBuffer.length / 2) + calculateShortenedSilenceLength(r7);
        outputSilence(r8, 2);
        r4 = r8;
        goto L18
    L16:
        r5 = this.maybeSilenceBufferContentsSize - (this.maybeSilenceBuffer.length / 2);
        r4 = calculateShortenedSilenceLength(r5);
        outputSilence(r4, 1);
        goto L18
    }

    private void outputSilence(int r9, int r10) {
        if (r9 != 0) goto L4;
        return;
    L4:
        boolean r1 = true;
        if (this.maybeSilenceBufferContentsSize < r9) goto L7;
        boolean r0 = true;
    L8:
        Assertions.checkArgument(r0);
        if (r10 != 2) goto L19;
        if ((this.maybeSilenceBufferStartIndex + this.maybeSilenceBufferContentsSize) > this.maybeSilenceBuffer.length) goto L13;
        System.arraycopy(this.maybeSilenceBuffer, (this.maybeSilenceBufferStartIndex + this.maybeSilenceBufferContentsSize) - r9, this.contiguousOutputBuffer, 0, r9);
    L23:
        if ((r9 % this.bytesPerFrame) != 0) goto L25;
        boolean r2 = true;
    L26:
        Assertions.checkArgument(r2, "sizeToOutput is not aligned to frame size: " + r9);
        if (this.maybeSilenceBufferStartIndex < this.maybeSilenceBuffer.length) goto L30;
        r1 = false;
    L30:
        Assertions.checkState(r1);
        outputRange(this.contiguousOutputBuffer, r9, r10);
        return;
    L25:
        r2 = false;
        goto L26
    L13:
        int r3 = this.maybeSilenceBufferContentsSize - (this.maybeSilenceBuffer.length - this.maybeSilenceBufferStartIndex);
        if (r3 < r9) goto L16;
        System.arraycopy(this.maybeSilenceBuffer, r3 - r9, this.contiguousOutputBuffer, 0, r9);
        goto L23
    L16:
        int r4 = r9 - r3;
        System.arraycopy(this.maybeSilenceBuffer, this.maybeSilenceBuffer.length - r4, this.contiguousOutputBuffer, 0, r4);
        System.arraycopy(this.maybeSilenceBuffer, 0, this.contiguousOutputBuffer, r4, r3);
        goto L23
    L19:
        if ((this.maybeSilenceBufferStartIndex + r9) > this.maybeSilenceBuffer.length) goto L21;
        System.arraycopy(this.maybeSilenceBuffer, this.maybeSilenceBufferStartIndex, this.contiguousOutputBuffer, 0, r9);
        goto L23
    L21:
        int r5 = this.maybeSilenceBuffer.length - this.maybeSilenceBufferStartIndex;
        System.arraycopy(this.maybeSilenceBuffer, this.maybeSilenceBufferStartIndex, this.contiguousOutputBuffer, 0, r5);
        System.arraycopy(this.maybeSilenceBuffer, 0, this.contiguousOutputBuffer, r5, r9 - r5);
        goto L23
    L7:
        r0 = false;
        goto L8
    }

    private void processNoisy(ByteBuffer r4) {
        int r0 = r4.limit();
        r4.limit(Math.min(r0, r4.position() + this.maybeSilenceBuffer.length));
        int r1 = findNoiseLimit(r4);
        if (r1 != r4.position()) goto L5;
        this.state = 1;
    L6:
        r4.limit(r0);
        return;
    L5:
        r4.limit(Math.min(r1, r4.capacity()));
        output(r4);
        goto L6
    }

    private static void sampleIntToTwoBigEndianBytes(byte[] r2, int r3, int r4) {
        if (r4 < 32767) goto L6;
        r2[r3] = -1;
        r2[r3 + 1] = 127;
        return;
    L6:
        if (r4 > (-32768)) goto L8;
        r2[r3] = 0;
        r2[r3 + 1] = -128;
        return;
    L8:
        r2[r3] = (byte) (r4 & 255);
        r2[r3 + 1] = (byte) (r4 >> 8);
    }

    private void shortenSilenceSilenceUntilNoise(ByteBuffer r12) {
        boolean r2 = true;
        if (this.maybeSilenceBufferStartIndex >= this.maybeSilenceBuffer.length) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        int r1 = r12.limit();
        int r3 = findNoisePosition(r12);
        int r4 = r3 - r12.position();
        if ((this.maybeSilenceBufferStartIndex + this.maybeSilenceBufferContentsSize) >= this.maybeSilenceBuffer.length) goto L9;
        int r5 = this.maybeSilenceBuffer.length - (this.maybeSilenceBufferContentsSize + this.maybeSilenceBufferStartIndex);
        int r6 = this.maybeSilenceBufferStartIndex + this.maybeSilenceBufferContentsSize;
    L10:
        if (r3 >= r1) goto L12;
        boolean r7 = true;
    L13:
        int r8 = Math.min(r4, r5);
        r12.limit(r12.position() + r8);
        r12.get(this.maybeSilenceBuffer, r6, r8);
        this.maybeSilenceBufferContentsSize += r8;
        if (this.maybeSilenceBufferContentsSize > this.maybeSilenceBuffer.length) goto L16;
        boolean r9 = true;
    L17:
        Assertions.checkState(r9);
        if (r7 == false) goto L21;
        if (r4 >= r5) goto L21;
    L22:
        outputShortenedSilenceBuffer(r2);
        if (r2 == false) goto L25;
        this.state = 0;
        this.outputSilenceFramesSinceNoise = 0;
    L25:
        r12.limit(r1);
        return;
    L21:
        r2 = false;
        goto L22
    L16:
        r9 = false;
        goto L17
    L12:
        r7 = false;
        goto L13
    L9:
        r6 = this.maybeSilenceBufferContentsSize - (this.maybeSilenceBuffer.length - this.maybeSilenceBufferStartIndex);
        r5 = this.maybeSilenceBufferStartIndex - r6;
        goto L10
    L5:
        r0 = false;
        goto L6
    }

    private static int twoByteSampleToInt(byte r2, byte r3) {
        return (r3 & 255) | (r2 << 8);
    }

    public long getSkippedFrames() {
        return this.skippedFrames;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor, androidx.media3.common.audio.AudioProcessor
    public boolean isActive() {
        if (super.isActive() == true) goto L5;
    L7:
        return false;
    L5:
        if (this.enabled == false) goto L7;
        return true;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    protected AudioProcessor.AudioFormat onConfigure(AudioProcessor.AudioFormat r3) throws AudioProcessor.UnhandledAudioFormatException {
        if (r3.encoding != 2) goto L10;
        if (r3.sampleRate == (-1)) goto L7;
        return r3;
    L7:
        return AudioProcessor.AudioFormat.NOT_SET;
    L10:
        throw new AudioProcessor.UnhandledAudioFormatException(r3);
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    public void onFlush() {
        if (isActive() == false) goto L7;
        this.bytesPerFrame = this.inputAudioFormat.channelCount * 2;
        int r0 = alignToBytePerFrameBoundary(durationUsToFrames(this.minimumSilenceDurationUs) / 2) * 2;
        if (this.maybeSilenceBuffer.length == r0) goto L7;
        this.maybeSilenceBuffer = new byte[r0];
        this.contiguousOutputBuffer = new byte[r0];
    L7:
        this.state = 0;
        this.skippedFrames = 0;
        this.outputSilenceFramesSinceNoise = 0;
        this.maybeSilenceBufferStartIndex = 0;
        this.maybeSilenceBufferContentsSize = 0;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    public void onQueueEndOfStream() {
        if (this.maybeSilenceBufferContentsSize <= 0) goto L6;
        outputShortenedSilenceBuffer(true);
        this.outputSilenceFramesSinceNoise = 0;
        return;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    public void onReset() {
        this.enabled = false;
        this.maybeSilenceBuffer = Util.EMPTY_BYTE_ARRAY;
        this.contiguousOutputBuffer = Util.EMPTY_BYTE_ARRAY;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public void queueInput(ByteBuffer r2) {
    L3:
        if (r2.hasRemaining() == false) goto L12;
        if (hasPendingOutput() == true) goto L21;
        switch(this.state) {
            case 0: goto L11;
            case 1: goto L10;
            default: goto L9;
        };
    L9:
        throw new IllegalStateException();
    L11:
        processNoisy(r2);
        goto L3
    L10:
        shortenSilenceSilenceUntilNoise(r2);
        goto L3
    L21:
        return;
    }

    public void setEnabled(boolean r1) {
        this.enabled = r1;
    }
}
