package androidx.media3.common.audio;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.LongArray;
import androidx.media3.common.util.LongArrayQueue;
import androidx.media3.common.util.SpeedProviderUtil;
import androidx.media3.common.util.TimestampConsumer;
import androidx.media3.common.util.Util;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class SpeedChangingAudioProcessor extends BaseAudioProcessor {
    private long bytesRead;
    private float currentSpeed;
    private boolean endOfStreamQueuedToSonic;
    private LongArray inputSegmentStartTimesUs;
    private long lastProcessedInputTimeUs;
    private long lastSpeedAdjustedInputTimeUs;
    private long lastSpeedAdjustedOutputTimeUs;
    private final Object lock;
    private LongArray outputSegmentStartTimesUs;
    private final LongArrayQueue pendingCallbackInputTimesUs;
    private final Queue<TimestampConsumer> pendingCallbacks;
    private final SynchronizedSonicAudioProcessor sonicAudioProcessor;
    private long speedAdjustedTimeAsyncInputTimeUs;
    private final SpeedProvider speedProvider;

    public SpeedChangingAudioProcessor(SpeedProvider r3) {
        this.speedProvider = r3;
        this.lock = new Object();
        this.sonicAudioProcessor = new SynchronizedSonicAudioProcessor(this.lock);
        this.pendingCallbackInputTimesUs = new LongArrayQueue();
        this.pendingCallbacks = new ArrayDeque();
        this.speedAdjustedTimeAsyncInputTimeUs = C0180C.TIME_UNSET;
        resetState();
    }

    private long calculateSpeedAdjustedTime(long r12) {
        int r0 = this.inputSegmentStartTimesUs.size() - 1;
    L3:
        if (r0 <= 0) goto L8;
        if (this.inputSegmentStartTimesUs.get(r0) <= r12) goto L8;
        r0 = r0 - 1;
    L8:
        if (r0 == (this.inputSegmentStartTimesUs.size() - 1)) goto L10;
        long r1 = Math.round((r12 - this.lastSpeedAdjustedInputTimeUs) * divide(this.outputSegmentStartTimesUs.get(r0 + 1) - this.outputSegmentStartTimesUs.get(r0), this.inputSegmentStartTimesUs.get(r0 + 1) - this.inputSegmentStartTimesUs.get(r0)));
    L14:
        this.lastSpeedAdjustedInputTimeUs = r12;
        this.lastSpeedAdjustedOutputTimeUs += r1;
        return this.lastSpeedAdjustedOutputTimeUs;
    L10:
        if (this.lastSpeedAdjustedInputTimeUs >= this.inputSegmentStartTimesUs.get(r0)) goto L12;
        this.lastSpeedAdjustedInputTimeUs = this.inputSegmentStartTimesUs.get(r0);
        this.lastSpeedAdjustedOutputTimeUs = this.outputSegmentStartTimesUs.get(r0);
    L12:
        r1 = getPlayoutDurationUsAtCurrentSpeed(r12 - this.lastSpeedAdjustedInputTimeUs);
        goto L14
    }

    private static double divide(long r4, long r6) {
        return r4 / r6;
    }

    private long getMediaDurationUsAtCurrentSpeed(long r3) {
        if (isUsingSonic() == false) goto L6;
        return this.sonicAudioProcessor.getMediaDuration(r3);
    L6:
        return r3;
    }

    private long getPlayoutDurationUsAtCurrentSpeed(long r3) {
        if (isUsingSonic() == false) goto L6;
        return this.sonicAudioProcessor.getPlayoutDuration(r3);
    L6:
        return r3;
    }

    private static long getSampleRateAlignedTimestamp(long r12, int r14) {
        return Util.scaleLargeTimestamp(Util.scaleLargeTimestamp(r12, r14, 1000000), 1000000, r14);
    }

    private boolean isUsingSonic() {
        Object r0 = this.lock;
        monitor-enter(r0);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (this.currentSpeed == 1.0f) goto L7;
        boolean r1 = true;
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L10
        return r1;
    L7:
        r1 = false;
        goto L8
    }

    private void processPendingCallbacks() {
        Object r0 = this.lock;
        monitor-enter(r0);
    L16:
    L13:
        th = move-exception;
        throw th;
    L5:
        if (this.pendingCallbacks.isEmpty() == true) goto L11;
        if (this.pendingCallbackInputTimesUs.element() <= this.lastProcessedInputTimeUs) goto L10;
        if (isEnded() == false) goto L11;
    L10:
        this.pendingCallbacks.remove().onTimestamp(calculateSpeedAdjustedTime(this.pendingCallbackInputTimesUs.remove()));     // Catch: Throwable -> L13
    L11:
        monitor-exit(r0);     // Catch: Throwable -> L13
    }

    @EnsuresNonNull({"inputSegmentStartTimesUs", "outputSegmentStartTimesUs"})
    @RequiresNonNull({"lock"})
    private void resetState() {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.inputSegmentStartTimesUs = new LongArray();     // Catch: Throwable -> L8
        this.outputSegmentStartTimesUs = new LongArray();     // Catch: Throwable -> L8
        this.inputSegmentStartTimesUs.add(0);     // Catch: Throwable -> L8
        this.outputSegmentStartTimesUs.add(0);     // Catch: Throwable -> L8
        this.lastProcessedInputTimeUs = 0;     // Catch: Throwable -> L8
        this.lastSpeedAdjustedInputTimeUs = 0;     // Catch: Throwable -> L8
        this.lastSpeedAdjustedOutputTimeUs = 0;     // Catch: Throwable -> L8
        this.currentSpeed = 1.0f;     // Catch: Throwable -> L8
        monitor-exit(r0);     // Catch: Throwable -> L8
        this.bytesRead = 0;
        this.endOfStreamQueuedToSonic = false;
        return;
    L8:
        th = move-exception;
        throw th;
    }

    private void updateLastProcessedInputTime() {
        Object r1 = this.lock;
        monitor-enter(r1);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (isUsingSonic() == false) goto L7;
        this.lastProcessedInputTimeUs = this.inputSegmentStartTimesUs.get(this.inputSegmentStartTimesUs.size() - 1) + Util.scaleLargeTimestamp(this.sonicAudioProcessor.getProcessedInputBytes(), 1000000, ((long) this.inputAudioFormat.bytesPerFrame) * this.inputAudioFormat.sampleRate);     // Catch: Throwable -> L10
    L8:
        monitor-exit(r1);     // Catch: Throwable -> L10
        return;
    L7:
        this.lastProcessedInputTimeUs = Util.scaleLargeTimestamp(this.bytesRead, 1000000, ((long) this.inputAudioFormat.bytesPerFrame) * this.inputAudioFormat.sampleRate);     // Catch: Throwable -> L10
        goto L8
    }

    private void updateSpeed(float r3, long r4) {
        Object r0 = this.lock;
        monitor-enter(r0);
    L12:
        th = move-exception;
        throw th;
    L5:
        if (r3 == this.currentSpeed) goto L10;
        updateSpeedChangeArrays(r4);     // Catch: Throwable -> L12
        this.currentSpeed = r3;     // Catch: Throwable -> L12
        if (isUsingSonic() == false) goto L9;
        this.sonicAudioProcessor.setSpeed(r3);     // Catch: Throwable -> L12
        this.sonicAudioProcessor.setPitch(r3);     // Catch: Throwable -> L12
    L9:
        this.sonicAudioProcessor.flush();     // Catch: Throwable -> L12
        this.endOfStreamQueuedToSonic = false;     // Catch: Throwable -> L12
        super.getOutput();     // Catch: Throwable -> L12
    L10:
        monitor-exit(r0);     // Catch: Throwable -> L12
    }

    private void updateSpeedChangeArrays(long r10) {
        long r0 = this.outputSegmentStartTimesUs.get(this.outputSegmentStartTimesUs.size() - 1);
        long r4 = r10 - this.inputSegmentStartTimesUs.get(this.inputSegmentStartTimesUs.size() - 1);
        this.inputSegmentStartTimesUs.add(r10);
        this.outputSegmentStartTimesUs.add(getPlayoutDurationUsAtCurrentSpeed(r4) + r0);
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor, androidx.media3.common.audio.AudioProcessor
    public long getDurationAfterProcessorApplied(long r3) {
        return SpeedProviderUtil.getDurationAfterSpeedProviderApplied(this.speedProvider, r3);
    }

    public long getMediaDurationUs(long r13) {
        Object r0 = this.lock;
        monitor-enter(r0);
        int r1 = this.outputSegmentStartTimesUs.size() - 1;     // Catch: Throwable -> L16
    L5:
        if (r1 <= 0) goto L9;
        if (this.outputSegmentStartTimesUs.get(r1) <= r13) goto L9;
        r1 = r1 - 1;     // Catch: Throwable -> L16
    L9:
        long r2 = r13 - this.outputSegmentStartTimesUs.get(r1);     // Catch: Throwable -> L16
        if (r1 != (this.outputSegmentStartTimesUs.size() - 1)) goto L12;
        long r4 = getMediaDurationUsAtCurrentSpeed(r2);     // Catch: Throwable -> L16
    L13:
        long r6 = this.inputSegmentStartTimesUs.get(r1) + r4;     // Catch: Throwable -> L16
        monitor-exit(r0);     // Catch: Throwable -> L16
        return r6;
    L12:
        r4 = Math.round(r2 * divide(this.inputSegmentStartTimesUs.get(r1 + 1) - this.inputSegmentStartTimesUs.get(r1), this.outputSegmentStartTimesUs.get(r1 + 1) - this.outputSegmentStartTimesUs.get(r1)));     // Catch: Throwable -> L16
    L16:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor, androidx.media3.common.audio.AudioProcessor
    public ByteBuffer getOutput() {
        if (isUsingSonic() == false) goto L5;
        ByteBuffer r0 = this.sonicAudioProcessor.getOutput();
    L6:
        processPendingCallbacks();
        return r0;
    L5:
        r0 = super.getOutput();
        goto L6
    }

    public void getSpeedAdjustedTimeAsync(long r4, TimestampConsumer r6) {
        Object r0 = this.lock;
        monitor-enter(r0);
    L20:
        th = move-exception;
        throw th;
    L5:
        if (this.speedAdjustedTimeAsyncInputTimeUs >= r4) goto L7;
        boolean r1 = true;
    L8:
        Assertions.checkArgument(r1);     // Catch: Throwable -> L20
        this.speedAdjustedTimeAsyncInputTimeUs = r4;     // Catch: Throwable -> L20
        if (r4 > this.lastProcessedInputTimeUs) goto L13;
        if (this.pendingCallbackInputTimesUs.isEmpty() == false) goto L13;
    L14:
        r6.onTimestamp(calculateSpeedAdjustedTime(r4));     // Catch: Throwable -> L20
        monitor-exit(r0);     // Catch: Throwable -> L20
        return;
    L13:
        if (isEnded() == true) goto L14;
        this.pendingCallbackInputTimesUs.add(r4);     // Catch: Throwable -> L20
        this.pendingCallbacks.add(r6);     // Catch: Throwable -> L20
        monitor-exit(r0);     // Catch: Throwable -> L20
        return;
    L7:
        r1 = false;
        goto L8
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor, androidx.media3.common.audio.AudioProcessor
    public boolean isEnded() {
        if (super.isEnded() == true) goto L5;
    L7:
        return false;
    L5:
        if (this.sonicAudioProcessor.isEnded() == false) goto L7;
        return true;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    public AudioProcessor.AudioFormat onConfigure(AudioProcessor.AudioFormat r2) throws AudioProcessor.UnhandledAudioFormatException {
        return this.sonicAudioProcessor.configure(r2);
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    protected void onFlush() {
        resetState();
        this.sonicAudioProcessor.flush();
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    protected void onQueueEndOfStream() {
        if (this.endOfStreamQueuedToSonic == true) goto L6;
        this.sonicAudioProcessor.queueEndOfStream();
        this.endOfStreamQueuedToSonic = true;
        return;
    }

    @Override // androidx.media3.common.audio.BaseAudioProcessor
    protected void onReset() {
        resetState();
        this.sonicAudioProcessor.reset();
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public void queueInput(ByteBuffer r20) {
        long r2 = Util.scaleLargeTimestamp(this.bytesRead, 1000000, ((long) this.inputAudioFormat.bytesPerFrame) * this.inputAudioFormat.sampleRate);
        float r4 = this.speedProvider.getSpeed(r2);
        long r5 = this.speedProvider.getNextSpeedChangeTimeUs(r2);
        if (getSampleRateAlignedTimestamp(r5, this.inputAudioFormat.sampleRate) != r2) goto L5;
        long r9 = Util.sampleCountToDurationUs(1, this.inputAudioFormat.sampleRate);
        r4 = this.speedProvider.getSpeed(r2 + r9);
        r5 = this.speedProvider.getNextSpeedChangeTimeUs(r2 + r9);
    L5:
        updateSpeed(r4, r2);
        int r10 = r20.limit();
        if (r5 == C0180C.TIME_UNSET) goto L11;
        int r3 = (int) Util.scaleLargeTimestamp(r5 - r2, ((long) this.inputAudioFormat.sampleRate) * ((long) this.inputAudioFormat.bytesPerFrame), 1000000);
        int r6 = this.inputAudioFormat.bytesPerFrame - (r3 % this.inputAudioFormat.bytesPerFrame);
        if (r6 == this.inputAudioFormat.bytesPerFrame) goto L10;
        r3 = r3 + r6;
    L10:
        r20.limit(Math.min(r10, r20.position() + r3));
    L12:
        long r11 = r20.position();
        if (isUsingSonic() == false) goto L19;
        this.sonicAudioProcessor.queueInput(r20);
        if (r3 != (-1)) goto L17;
    L23:
        this.bytesRead += ((long) r20.position()) - r11;
        updateLastProcessedInputTime();
        r20.limit(r10);
        return;
    L17:
        if ((((long) r20.position()) - r11) != r3) goto L23;
        this.sonicAudioProcessor.queueEndOfStream();
        this.endOfStreamQueuedToSonic = true;
        goto L23
    L19:
        ByteBuffer r7 = replaceOutputBuffer(r20.remaining());
        if (r20.hasRemaining() == false) goto L22;
        r7.put(r20);
    L22:
        r7.flip();
        goto L23
    L11:
        r3 = -1;
        goto L12
    }
}
