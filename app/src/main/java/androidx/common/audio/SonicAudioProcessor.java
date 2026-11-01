package androidx.media3.common.audio;

import android.support.v4.media.session.PlaybackStateCompat;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/* JADX INFO: loaded from: classes.dex */
public class SonicAudioProcessor implements AudioProcessor {
    private static final float CLOSE_THRESHOLD = 1.0E-4f;
    private static final int MIN_BYTES_FOR_DURATION_SCALING_CALCULATION = 1024;
    public static final int SAMPLE_RATE_NO_CHANGE = -1;
    private ByteBuffer buffer;
    private AudioProcessor.AudioFormat inputAudioFormat;
    private long inputBytes;
    private boolean inputEnded;
    private AudioProcessor.AudioFormat outputAudioFormat;
    private ByteBuffer outputBuffer;
    private long outputBytes;
    private AudioProcessor.AudioFormat pendingInputAudioFormat;
    private AudioProcessor.AudioFormat pendingOutputAudioFormat;
    private int pendingOutputSampleRate;
    private boolean pendingSonicRecreation;
    private float pitch;
    private ShortBuffer shortBuffer;
    private Sonic sonic;
    private float speed;

    public SonicAudioProcessor() {
        this.speed = 1.0f;
        this.pitch = 1.0f;
        this.pendingInputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.pendingOutputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.inputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.outputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.buffer = EMPTY_BUFFER;
        this.shortBuffer = this.buffer.asShortBuffer();
        this.outputBuffer = EMPTY_BUFFER;
        this.pendingOutputSampleRate = -1;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final AudioProcessor.AudioFormat configure(AudioProcessor.AudioFormat r5) throws AudioProcessor.UnhandledAudioFormatException {
        if (r5.encoding != 2) goto L11;
        if (this.pendingOutputSampleRate != (-1)) goto L7;
        int r0 = r5.sampleRate;
    L8:
        this.pendingInputAudioFormat = r5;
        this.pendingOutputAudioFormat = new AudioProcessor.AudioFormat(r0, r5.channelCount, 2);
        this.pendingSonicRecreation = true;
        return this.pendingOutputAudioFormat;
    L7:
        r0 = this.pendingOutputSampleRate;
        goto L8
    L11:
        throw new AudioProcessor.UnhandledAudioFormatException(r5);
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final void flush() {
        if (isActive() == false) goto L10;
        this.inputAudioFormat = this.pendingInputAudioFormat;
        this.outputAudioFormat = this.pendingOutputAudioFormat;
        if (this.pendingSonicRecreation == false) goto L8;
        this.sonic = new Sonic(this.inputAudioFormat.sampleRate, this.inputAudioFormat.channelCount, this.speed, this.pitch, this.outputAudioFormat.sampleRate);
        goto L10
    L8:
        if (this.sonic == null) goto L10;
        this.sonic.flush();
    L10:
        this.outputBuffer = EMPTY_BUFFER;
        this.inputBytes = 0;
        this.outputBytes = 0;
        this.inputEnded = false;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public long getDurationAfterProcessorApplied(long r3) {
        return getPlayoutDuration(r3);
    }

    public final long getMediaDuration(long r11) {
        if (this.outputBytes < PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) goto L10;
        long r6 = this.inputBytes - ((long) ((Sonic) Assertions.checkNotNull(this.sonic)).getPendingInputBytes());
        if (this.outputAudioFormat.sampleRate != this.inputAudioFormat.sampleRate) goto L8;
        return Util.scaleLargeTimestamp(r11, r6, this.outputBytes);
    L8:
        return Util.scaleLargeTimestamp(r11, r6 * ((long) this.outputAudioFormat.sampleRate), ((long) this.inputAudioFormat.sampleRate) * this.outputBytes);
    L10:
        return (long) (((double) this.speed) * r11);
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final ByteBuffer getOutput() {
        Sonic r0 = this.sonic;
        if (r0 == null) goto L11;
        int r1 = r0.getOutputSize();
        if (r1 <= 0) goto L11;
        if (this.buffer.capacity() >= r1) goto L9;
        this.buffer = ByteBuffer.allocateDirect(r1).order(ByteOrder.nativeOrder());
        this.shortBuffer = this.buffer.asShortBuffer();
    L10:
        r0.getOutput(this.shortBuffer);
        this.outputBytes += (long) r1;
        this.buffer.limit(r1);
        this.outputBuffer = this.buffer;
        goto L11
    L9:
        this.buffer.clear();
        this.shortBuffer.clear();
    L11:
        ByteBuffer r2 = this.outputBuffer;
        this.outputBuffer = EMPTY_BUFFER;
        return r2;
    }

    public final long getPlayoutDuration(long r11) {
        if (this.outputBytes < PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) goto L10;
        long r8 = this.inputBytes - ((long) ((Sonic) Assertions.checkNotNull(this.sonic)).getPendingInputBytes());
        if (this.outputAudioFormat.sampleRate != this.inputAudioFormat.sampleRate) goto L8;
        return Util.scaleLargeTimestamp(r11, this.outputBytes, r8);
    L8:
        return Util.scaleLargeTimestamp(r11, ((long) this.inputAudioFormat.sampleRate) * this.outputBytes, r8 * ((long) this.outputAudioFormat.sampleRate));
    L10:
        return (long) (r11 / ((double) this.speed));
    }

    public final long getProcessedInputBytes() {
        return this.inputBytes - ((long) ((Sonic) Assertions.checkNotNull(this.sonic)).getPendingInputBytes());
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final boolean isActive() {
        if (this.pendingOutputAudioFormat.sampleRate != (-1)) goto L5;
    L11:
        return false;
    L5:
        if (Math.abs(this.speed - 1.0f) < CLOSE_THRESHOLD) goto L7;
    L10:
        return true;
    L7:
        if (Math.abs(this.pitch - 1.0f) >= CLOSE_THRESHOLD) goto L10;
        if (this.pendingOutputAudioFormat.sampleRate == this.pendingInputAudioFormat.sampleRate) goto L11;
        goto L10
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final boolean isEnded() {
        if (this.inputEnded == true) goto L5;
    L9:
        return false;
    L5:
        if (this.sonic != null) goto L7;
    L8:
        return true;
    L7:
        if (this.sonic.getOutputSize() != 0) goto L9;
        goto L8
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final void queueEndOfStream() {
        if (this.sonic == null) goto L5;
        this.sonic.queueEndOfStream();
    L5:
        this.inputEnded = true;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final void queueInput(ByteBuffer r8) {
        if (r8.hasRemaining() == true) goto L5;
        return;
    L5:
        Sonic r0 = (Sonic) Assertions.checkNotNull(this.sonic);
        ShortBuffer r1 = r8.asShortBuffer();
        int r2 = r8.remaining();
        this.inputBytes += (long) r2;
        r0.queueInput(r1);
        r8.position(r8.position() + r2);
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final void reset() {
        this.speed = 1.0f;
        this.pitch = 1.0f;
        this.pendingInputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.pendingOutputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.inputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.outputAudioFormat = AudioProcessor.AudioFormat.NOT_SET;
        this.buffer = EMPTY_BUFFER;
        this.shortBuffer = this.buffer.asShortBuffer();
        this.outputBuffer = EMPTY_BUFFER;
        this.pendingOutputSampleRate = -1;
        this.pendingSonicRecreation = false;
        this.sonic = null;
        this.inputBytes = 0;
        this.outputBytes = 0;
        this.inputEnded = false;
    }

    public final void setOutputSampleRateHz(int r1) {
        this.pendingOutputSampleRate = r1;
    }

    public final void setPitch(float r2) {
        if (this.pitch == r2) goto L6;
        this.pitch = r2;
        this.pendingSonicRecreation = true;
        return;
    }

    public final void setSpeed(float r2) {
        if (this.speed == r2) goto L6;
        this.speed = r2;
        this.pendingSonicRecreation = true;
        return;
    }
}
