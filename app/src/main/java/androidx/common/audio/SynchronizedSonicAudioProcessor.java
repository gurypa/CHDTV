package androidx.media3.common.audio;

import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
class SynchronizedSonicAudioProcessor implements AudioProcessor {
    private final Object lock;
    private final SonicAudioProcessor sonicAudioProcessor;

    public SynchronizedSonicAudioProcessor(Object r2) {
        this.lock = r2;
        this.sonicAudioProcessor = new SonicAudioProcessor();
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final AudioProcessor.AudioFormat configure(AudioProcessor.AudioFormat r3) throws AudioProcessor.UnhandledAudioFormatException {
        Object r0 = this.lock;
        monitor-enter(r0);
        AudioProcessor.AudioFormat r1 = this.sonicAudioProcessor.configure(r3);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final void flush() {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.sonicAudioProcessor.flush();     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public long getDurationAfterProcessorApplied(long r3) {
        return getPlayoutDuration(r3);
    }

    public final long getMediaDuration(long r4) {
        Object r0 = this.lock;
        monitor-enter(r0);
        long r1 = this.sonicAudioProcessor.getMediaDuration(r4);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final ByteBuffer getOutput() {
        Object r0 = this.lock;
        monitor-enter(r0);
        ByteBuffer r1 = this.sonicAudioProcessor.getOutput();     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    public final long getPlayoutDuration(long r4) {
        Object r0 = this.lock;
        monitor-enter(r0);
        long r1 = this.sonicAudioProcessor.getPlayoutDuration(r4);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    public final long getProcessedInputBytes() {
        Object r0 = this.lock;
        monitor-enter(r0);
        long r1 = this.sonicAudioProcessor.getProcessedInputBytes();     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final boolean isActive() {
        Object r0 = this.lock;
        monitor-enter(r0);
        boolean r1 = this.sonicAudioProcessor.isActive();     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final boolean isEnded() {
        Object r0 = this.lock;
        monitor-enter(r0);
        boolean r1 = this.sonicAudioProcessor.isEnded();     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final void queueEndOfStream() {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.sonicAudioProcessor.queueEndOfStream();     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final void queueInput(ByteBuffer r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.sonicAudioProcessor.queueInput(r3);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.common.audio.AudioProcessor
    public final void reset() {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.sonicAudioProcessor.reset();     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public final void setOutputSampleRateHz(int r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.sonicAudioProcessor.setOutputSampleRateHz(r3);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public final void setPitch(float r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.sonicAudioProcessor.setPitch(r3);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public final void setSpeed(float r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.sonicAudioProcessor.setSpeed(r3);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }
}
