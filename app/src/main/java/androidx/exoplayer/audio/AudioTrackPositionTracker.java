package androidx.media3.exoplayer.audio;

import android.media.AudioTrack;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.dash.DashMediaSource;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
final class AudioTrackPositionTracker {
    private static final long FORCE_RESET_WORKAROUND_TIMEOUT_MS = 200;
    private static final long MAX_AUDIO_TIMESTAMP_OFFSET_US = 5000000;
    private static final long MAX_LATENCY_US = 5000000;
    private static final int MAX_PLAYHEAD_OFFSET_COUNT = 10;
    private static final int MIN_LATENCY_SAMPLE_INTERVAL_US = 500000;
    private static final int MIN_PLAYHEAD_OFFSET_SAMPLE_INTERVAL_US = 30000;
    private static final long MODE_SWITCH_SMOOTHING_DURATION_US = 1000000;
    private static final int PLAYSTATE_PAUSED = 2;
    private static final int PLAYSTATE_PLAYING = 3;
    private static final int PLAYSTATE_STOPPED = 1;
    private static final long RAW_PLAYBACK_HEAD_POSITION_UPDATE_INTERVAL_MS = 5;
    private AudioTimestampPoller audioTimestampPoller;
    private AudioTrack audioTrack;
    private float audioTrackPlaybackSpeed;
    private int bufferSize;
    private long bufferSizeUs;
    private Clock clock;
    private long endPlaybackHeadPosition;
    private boolean expectRawPlaybackHeadReset;
    private long forceResetWorkaroundTimeMs;
    private Method getLatencyMethod;
    private boolean hasData;
    private boolean isOutputPcm;
    private long lastLatencySampleTimeUs;
    private long lastPlayheadSampleTimeUs;
    private long lastPositionUs;
    private long lastRawPlaybackHeadPositionSampleTimeMs;
    private boolean lastSampleUsedGetTimestampMode;
    private long lastSystemTimeUs;
    private long latencyUs;
    private final Listener listener;
    private boolean needsPassthroughWorkarounds;
    private int nextPlayheadOffsetIndex;
    private boolean notifiedPositionIncreasing;
    private int outputPcmFrameSize;
    private int outputSampleRate;
    private long passthroughWorkaroundPauseOffset;
    private int playheadOffsetCount;
    private final long[] playheadOffsets;
    private long previousModePositionUs;
    private long previousModeSystemTimeUs;
    private long rawPlaybackHeadPosition;
    private long rawPlaybackHeadWrapCount;
    private long smoothedPlayheadOffsetUs;
    private long stopPlaybackHeadPosition;
    private long stopTimestampUs;
    private long sumRawPlaybackHeadPosition;

    public interface Listener {
        void onInvalidLatency(long r1);

        void onPositionAdvancing(long r1);

        void onPositionFramesMismatch(long r1, long r3, long r5, long r7);

        void onSystemTimeUsMismatch(long r1, long r3, long r5, long r7);

        void onUnderrun(int r1, long r2);
    }

    public AudioTrackPositionTracker(Listener r5) {
        this.listener = (Listener) Assertions.checkNotNull(r5);
        Class[] r3 = (Class[]) null;     // Catch: NoSuchMethodException -> L5
        this.getLatencyMethod = AudioTrack.class.getMethod("getLatency", null);     // Catch: NoSuchMethodException -> L5
    L6:
        this.playheadOffsets = new long[10];
        this.clock = Clock.DEFAULT;
    }

    private boolean forceHasPendingData() {
        if (this.needsPassthroughWorkarounds == true) goto L5;
    L9:
        return false;
    L5:
        if (((AudioTrack) Assertions.checkNotNull(this.audioTrack)).getPlayState() != 2) goto L9;
        if (getPlaybackHeadPosition() != 0) goto L9;
        return true;
    }

    private long getPlaybackHeadPosition() {
        long r0 = this.clock.elapsedRealtime();
        if (this.stopTimestampUs == C0180C.TIME_UNSET) goto L11;
        if (((AudioTrack) Assertions.checkNotNull(this.audioTrack)).getPlayState() == 2) goto L7;
        long r6 = Util.durationUsToSampleCount(Util.getMediaDurationForPlayoutDuration(Util.msToUs(r0) - this.stopTimestampUs, this.audioTrackPlaybackSpeed), this.outputSampleRate);
        return Math.min(this.endPlaybackHeadPosition, this.stopPlaybackHeadPosition + r6);
    L7:
        return this.stopPlaybackHeadPosition;
    L11:
        if ((r0 - this.lastRawPlaybackHeadPositionSampleTimeMs) < 5) goto L14;
        updateRawPlaybackHeadPosition(r0);
        this.lastRawPlaybackHeadPositionSampleTimeMs = r0;
    L14:
        return (this.rawPlaybackHeadPosition + this.sumRawPlaybackHeadPosition) + (this.rawPlaybackHeadWrapCount << 32);
    }

    private long getPlaybackHeadPositionUs() {
        return Util.sampleCountToDurationUs(getPlaybackHeadPosition(), this.outputSampleRate);
    }

    private void maybePollAndCheckTimestamp(long r12) {
        AudioTimestampPoller r0 = (AudioTimestampPoller) Assertions.checkNotNull(this.audioTimestampPoller);
        if (r0.maybePollTimestamp(r12) == true) goto L5;
        return;
    L5:
        long r5 = r0.getTimestampSystemTimeUs();
        long r3 = r0.getTimestampPositionFrames();
        long r9 = getPlaybackHeadPositionUs();
        if (Math.abs(r5 - r12) <= DashMediaSource.MIN_LIVE_DEFAULT_START_POSITION_US) goto L9;
        this.listener.onSystemTimeUsMismatch(r3, r5, r12, r9);
        r0.rejectTimestamp();
        return;
    L9:
        if (Math.abs(Util.sampleCountToDurationUs(r3, this.outputSampleRate) - r9) <= DashMediaSource.MIN_LIVE_DEFAULT_START_POSITION_US) goto L11;
        this.listener.onPositionFramesMismatch(r3, r5, r12, r9);
        r0.rejectTimestamp();
        return;
    L11:
        r0.acceptTimestamp();
    }

    private void maybeSampleSyncParams() {
        long r0 = this.clock.nanoTime() / 1000;
        if ((r0 - this.lastPlayheadSampleTimeUs) < 30000) goto L15;
        long r2 = getPlaybackHeadPositionUs();
        if (r2 != 0) goto L7;
        return;
    L7:
        this.playheadOffsets[this.nextPlayheadOffsetIndex] = Util.getPlayoutDurationForMediaDuration(r2, this.audioTrackPlaybackSpeed) - r0;
        this.nextPlayheadOffsetIndex = (this.nextPlayheadOffsetIndex + 1) % 10;
        if (this.playheadOffsetCount >= 10) goto L10;
        this.playheadOffsetCount++;
    L10:
        this.lastPlayheadSampleTimeUs = r0;
        this.smoothedPlayheadOffsetUs = 0;
        int r4 = 0;
    L12:
        if (r4 >= this.playheadOffsetCount) goto L15;
        this.smoothedPlayheadOffsetUs += this.playheadOffsets[r4] / ((long) this.playheadOffsetCount);
        r4 = r4 + 1;
    L15:
        if (this.needsPassthroughWorkarounds == false) goto L17;
        return;
    L17:
        maybePollAndCheckTimestamp(r0);
        maybeUpdateLatency(r0);
    }

    private void maybeUpdateLatency(long r7) {
        if (this.isOutputPcm == true) goto L5;
        return;
    L5:
        if (this.getLatencyMethod != null) goto L7;
        return;
    L7:
        if ((r7 - this.lastLatencySampleTimeUs) < 500000) goto L20;
        this.latencyUs = (((long) ((Integer) Util.castNonNull((Integer) this.getLatencyMethod.invoke(Assertions.checkNotNull(this.audioTrack), new Object[0]))).intValue()) * 1000) - this.bufferSizeUs;     // Catch: Exception -> L12
        this.latencyUs = Math.max(this.latencyUs, 0);     // Catch: Exception -> L12
        if (this.latencyUs <= DashMediaSource.MIN_LIVE_DEFAULT_START_POSITION_US) goto L14;
        this.listener.onInvalidLatency(this.latencyUs);     // Catch: Exception -> L12
        this.latencyUs = 0;     // Catch: Exception -> L12
    L14:
        this.lastLatencySampleTimeUs = r7;
        return;
    L13:
        this.getLatencyMethod = null;
        goto L14
    }

    private static boolean needsPassthroughWorkarounds(int r2) {
        if (Util.SDK_INT < 23) goto L5;
    L9:
        return false;
    L5:
        if (r2 != 5) goto L7;
    L8:
        return true;
    L7:
        if (r2 != 6) goto L9;
        goto L8
    }

    private void resetSyncParams() {
        this.smoothedPlayheadOffsetUs = 0;
        this.playheadOffsetCount = 0;
        this.nextPlayheadOffsetIndex = 0;
        this.lastPlayheadSampleTimeUs = 0;
        this.lastSystemTimeUs = 0;
        this.previousModeSystemTimeUs = 0;
        this.notifiedPositionIncreasing = false;
    }

    private void updateRawPlaybackHeadPosition(long r12) {
        AudioTrack r0 = (AudioTrack) Assertions.checkNotNull(this.audioTrack);
        int r1 = r0.getPlayState();
        if (r1 != 1) goto L5;
        return;
    L5:
        long r2 = ((long) r0.getPlaybackHeadPosition()) & 4294967295L;
        if (this.needsPassthroughWorkarounds == false) goto L14;
        if (r1 == 2) goto L10;
    L12:
        r2 = r2 + this.passthroughWorkaroundPauseOffset;
        goto L14
    L10:
        if (r2 != 0) goto L12;
        this.passthroughWorkaroundPauseOffset = this.rawPlaybackHeadPosition;
    L14:
        if (Util.SDK_INT > 29) goto L27;
        if (r2 == 0) goto L18;
    L25:
        this.forceResetWorkaroundTimeMs = C0180C.TIME_UNSET;
        goto L27
    L18:
        if (this.rawPlaybackHeadPosition <= 0) goto L25;
        if (r1 != 3) goto L25;
        if (this.forceResetWorkaroundTimeMs != C0180C.TIME_UNSET) goto L34;
        this.forceResetWorkaroundTimeMs = r12;
        return;
    L34:
        return;
    L27:
        if (this.rawPlaybackHeadPosition > r2) goto L29;
    L32:
        this.rawPlaybackHeadPosition = r2;
        return;
    L29:
        if (this.expectRawPlaybackHeadReset == false) goto L31;
        this.sumRawPlaybackHeadPosition += this.rawPlaybackHeadPosition;
        this.expectRawPlaybackHeadReset = false;
        goto L32
    L31:
        this.rawPlaybackHeadWrapCount++;
        goto L32
    }

    public void expectRawPlaybackHeadReset() {
        this.expectRawPlaybackHeadReset = true;
        if (this.audioTimestampPoller == null) goto L6;
        this.audioTimestampPoller.expectTimestampFramePositionReset();
        return;
    }

    public int getAvailableBufferSize(long r5) {
        int r0 = (int) (r5 - (getPlaybackHeadPosition() * ((long) this.outputPcmFrameSize)));
        return this.bufferSize - r0;
    }

    public long getCurrentPositionUs(boolean r18) {
        if (((AudioTrack) Assertions.checkNotNull(this.audioTrack)).getPlayState() != 3) goto L5;
        maybeSampleSyncParams();
    L5:
        long r1 = this.clock.nanoTime() / 1000;
        AudioTimestampPoller r5 = (AudioTimestampPoller) Assertions.checkNotNull(this.audioTimestampPoller);
        boolean r6 = r5.hasAdvancingTimestamp();
        if (r6 == false) goto L9;
        long r9 = Util.sampleCountToDurationUs(r5.getTimestampPositionFrames(), this.outputSampleRate) + Util.getMediaDurationForPlayoutDuration(r1 - r5.getTimestampSystemTimeUs(), this.audioTrackPlaybackSpeed);
    L15:
        if (this.lastSampleUsedGetTimestampMode == r6) goto L17;
        this.previousModeSystemTimeUs = this.lastSystemTimeUs;
        this.previousModePositionUs = this.lastPositionUs;
    L17:
        long r7 = r1 - this.previousModeSystemTimeUs;
        if (r7 >= 1000000) goto L21;
        long r13 = this.previousModePositionUs + Util.getMediaDurationForPlayoutDuration(r7, this.audioTrackPlaybackSpeed);
        long r15 = (r7 * 1000) / 1000000;
        r9 = ((r9 * r15) + ((1000 - r15) * r13)) / 1000;
    L21:
        if (this.notifiedPositionIncreasing == false) goto L23;
    L25:
        this.lastSystemTimeUs = r1;
        this.lastPositionUs = r9;
        this.lastSampleUsedGetTimestampMode = r6;
        return r9;
    L23:
        if (r9 <= this.lastPositionUs) goto L25;
        this.notifiedPositionIncreasing = true;
        long r11 = Util.getPlayoutDurationForMediaDuration(Util.usToMs(r9 - this.lastPositionUs), this.audioTrackPlaybackSpeed);
        long r14 = this.clock.currentTimeMillis() - Util.usToMs(r11);
        this.listener.onPositionAdvancing(r14);
        goto L25
    L9:
        if (this.playheadOffsetCount != 0) goto L11;
        r9 = getPlaybackHeadPositionUs();
    L12:
        if (r18 == true) goto L15;
        r9 = Math.max(0, r9 - this.latencyUs);
        goto L15
    L11:
        r9 = Util.getMediaDurationForPlayoutDuration(this.smoothedPlayheadOffsetUs + r1, this.audioTrackPlaybackSpeed);
        goto L12
    }

    public void handleEndOfStream(long r3) {
        this.stopPlaybackHeadPosition = getPlaybackHeadPosition();
        this.stopTimestampUs = Util.msToUs(this.clock.elapsedRealtime());
        this.endPlaybackHeadPosition = r3;
    }

    public boolean hasPendingData(long r6) {
        if (r6 <= Util.durationUsToSampleCount(getCurrentPositionUs(false), this.outputSampleRate)) goto L5;
    L6:
        return true;
    L5:
        if (forceHasPendingData() == true) goto L6;
        return false;
    }

    public boolean isPlaying() {
        if (((AudioTrack) Assertions.checkNotNull(this.audioTrack)).getPlayState() != 3) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isStalled(long r5) {
        if (this.forceResetWorkaroundTimeMs != C0180C.TIME_UNSET) goto L5;
    L9:
        return false;
    L5:
        if (r5 <= 0) goto L9;
        if ((this.clock.elapsedRealtime() - this.forceResetWorkaroundTimeMs) < FORCE_RESET_WORKAROUND_TIMEOUT_MS) goto L9;
        return true;
    }

    public boolean mayHandleBuffer(long r9) {
        int r0 = ((AudioTrack) Assertions.checkNotNull(this.audioTrack)).getPlayState();
        if (this.needsPassthroughWorkarounds == true) goto L5;
    L12:
        boolean r1 = this.hasData;
        this.hasData = hasPendingData(r9);
        if (r1 == true) goto L15;
    L18:
        return true;
    L15:
        if (this.hasData == true) goto L18;
        if (r0 == 1) goto L18;
        this.listener.onUnderrun(this.bufferSize, Util.usToMs(this.bufferSizeUs));
        goto L18
    L5:
        if (r0 != 2) goto L8;
        this.hasData = false;
        return false;
    L8:
        if (r0 != 1) goto L12;
        if (getPlaybackHeadPosition() != 0) goto L12;
        return false;
    }

    public boolean pause() {
        resetSyncParams();
        if (this.stopTimestampUs != C0180C.TIME_UNSET) goto L6;
        ((AudioTimestampPoller) Assertions.checkNotNull(this.audioTimestampPoller)).reset();
        return true;
    L6:
        this.stopPlaybackHeadPosition = getPlaybackHeadPosition();
        return false;
    }

    public void reset() {
        resetSyncParams();
        this.audioTrack = null;
        this.audioTimestampPoller = null;
    }

    public void setAudioTrack(AudioTrack r7, boolean r8, int r9, int r10, int r11) {
        this.audioTrack = r7;
        this.outputPcmFrameSize = r10;
        this.bufferSize = r11;
        this.audioTimestampPoller = new AudioTimestampPoller(r7);
        this.outputSampleRate = r7.getSampleRate();
        if (r8 == true) goto L5;
    L7:
        boolean r1 = false;
    L8:
        this.needsPassthroughWorkarounds = r1;
        this.isOutputPcm = Util.isEncodingLinearPcm(r9);
        if (this.isOutputPcm == false) goto L11;
        long r4 = Util.sampleCountToDurationUs(r11 / r10, this.outputSampleRate);
    L12:
        this.bufferSizeUs = r4;
        this.rawPlaybackHeadPosition = 0;
        this.rawPlaybackHeadWrapCount = 0;
        this.expectRawPlaybackHeadReset = false;
        this.sumRawPlaybackHeadPosition = 0;
        this.passthroughWorkaroundPauseOffset = 0;
        this.hasData = false;
        this.stopTimestampUs = C0180C.TIME_UNSET;
        this.forceResetWorkaroundTimeMs = C0180C.TIME_UNSET;
        this.lastLatencySampleTimeUs = 0;
        this.latencyUs = 0;
        this.audioTrackPlaybackSpeed = 1.0f;
        return;
    L11:
        r4 = -9223372036854775807L;
        goto L12
    L5:
        if (needsPassthroughWorkarounds(r9) == false) goto L7;
        r1 = true;
        goto L8
    }

    public void setAudioTrackPlaybackSpeed(float r2) {
        this.audioTrackPlaybackSpeed = r2;
        if (this.audioTimestampPoller == null) goto L5;
        this.audioTimestampPoller.reset();
    L5:
        resetSyncParams();
    }

    public void setClock(Clock r1) {
        this.clock = r1;
    }

    public void start() {
        if (this.stopTimestampUs == C0180C.TIME_UNSET) goto L5;
        this.stopTimestampUs = Util.msToUs(this.clock.elapsedRealtime());
    L5:
        ((AudioTimestampPoller) Assertions.checkNotNull(this.audioTimestampPoller)).reset();
    }
}
