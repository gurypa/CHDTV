package androidx.media3.exoplayer.audio;

import android.media.AudioTimestamp;
import android.media.AudioTrack;
import androidx.media3.common.C0180C;
import androidx.media3.exoplayer.Renderer;

/* JADX INFO: loaded from: classes.dex */
final class AudioTimestampPoller {
    private static final int ERROR_POLL_INTERVAL_US = 500000;
    private static final int FAST_POLL_INTERVAL_US = 10000;
    private static final int INITIALIZING_DURATION_US = 500000;
    private static final int SLOW_POLL_INTERVAL_US = 10000000;
    private static final int STATE_ERROR = 4;
    private static final int STATE_INITIALIZING = 0;
    private static final int STATE_NO_TIMESTAMP = 3;
    private static final int STATE_TIMESTAMP = 1;
    private static final int STATE_TIMESTAMP_ADVANCING = 2;
    private final AudioTimestampWrapper audioTimestamp;
    private long initialTimestampPositionFrames;
    private long initializeSystemTimeUs;
    private long lastTimestampSampleTimeUs;
    private long sampleIntervalUs;
    private int state;

    private static final class AudioTimestampWrapper {
        private long accumulatedRawTimestampFramePosition;
        private final AudioTimestamp audioTimestamp;
        private final AudioTrack audioTrack;
        private boolean expectTimestampFramePositionReset;
        private long lastTimestampPositionFrames;
        private long lastTimestampRawPositionFrames;
        private long rawTimestampFramePositionWrapCount;

        public AudioTimestampWrapper(AudioTrack r2) {
            this.audioTrack = r2;
            this.audioTimestamp = new AudioTimestamp();
        }

        public void expectTimestampFramePositionReset() {
            this.expectTimestampFramePositionReset = true;
        }

        public long getTimestampPositionFrames() {
            return this.lastTimestampPositionFrames;
        }

        public long getTimestampSystemTimeUs() {
            return this.audioTimestamp.nanoTime / 1000;
        }

        public boolean maybeUpdateTimestamp() {
            boolean r0 = this.audioTrack.getTimestamp(this.audioTimestamp);
            if (r0 == false) goto L11;
            long r1 = this.audioTimestamp.framePosition;
            if (this.lastTimestampRawPositionFrames > r1) goto L7;
        L10:
            this.lastTimestampRawPositionFrames = r1;
            this.lastTimestampPositionFrames = (this.accumulatedRawTimestampFramePosition + r1) + (this.rawTimestampFramePositionWrapCount << 32);
            goto L11
        L7:
            if (this.expectTimestampFramePositionReset == false) goto L9;
            this.accumulatedRawTimestampFramePosition += this.lastTimestampRawPositionFrames;
            this.expectTimestampFramePositionReset = false;
            goto L10
        L9:
            this.rawTimestampFramePositionWrapCount++;
        L11:
            return r0;
        }
    }

    public AudioTimestampPoller(AudioTrack r2) {
        this.audioTimestamp = new AudioTimestampWrapper(r2);
        reset();
    }

    private void updateState(int r7) {
        this.state = r7;
        switch(r7) {
            case 0: goto L9;
            case 1: goto L8;
            case 2: goto L7;
            case 3: goto L7;
            case 4: goto L6;
            default: goto L5;
        };
    L6:
        this.sampleIntervalUs = 500000;
        return;
    L7:
        this.sampleIntervalUs = 10000000;
        return;
    L8:
        this.sampleIntervalUs = Renderer.DEFAULT_DURATION_TO_PROGRESS_US;
        return;
    L9:
        this.lastTimestampSampleTimeUs = 0;
        this.initialTimestampPositionFrames = -1;
        this.initializeSystemTimeUs = System.nanoTime() / 1000;
        this.sampleIntervalUs = Renderer.DEFAULT_DURATION_TO_PROGRESS_US;
        return;
    L5:
        throw new IllegalStateException();
    }

    public void acceptTimestamp() {
        if (this.state != 4) goto L6;
        reset();
        return;
    }

    public void expectTimestampFramePositionReset() {
        if (this.audioTimestamp == null) goto L6;
        this.audioTimestamp.expectTimestampFramePositionReset();
        return;
    }

    public long getTimestampPositionFrames() {
        if (this.audioTimestamp != null) goto L7;
        return -1;
    L7:
        return this.audioTimestamp.getTimestampPositionFrames();
    }

    public long getTimestampSystemTimeUs() {
        if (this.audioTimestamp != null) goto L7;
        return C0180C.TIME_UNSET;
    L7:
        return this.audioTimestamp.getTimestampSystemTimeUs();
    }

    public boolean hasAdvancingTimestamp() {
        if (this.state != 2) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean hasTimestamp() {
        if (this.state != 1) goto L5;
        return true;
    L5:
        if (this.state == 2) goto L10;
        return false;
    L10:
        return true;
    }

    public boolean maybePollTimestamp(long r6) {
        if (this.audioTimestamp != null) goto L5;
        return false;
    L5:
        if ((r6 - this.lastTimestampSampleTimeUs) < this.sampleIntervalUs) goto L44;
        this.lastTimestampSampleTimeUs = r6;
        boolean r0 = this.audioTimestamp.maybeUpdateTimestamp();
        switch(this.state) {
            case 0: goto L22;
            case 1: goto L16;
            case 2: goto L14;
            case 3: goto L12;
            case 4: goto L41;
            default: goto L10;
        };
    L12:
        if (r0 == false) goto L33;
        reset();
        return r0;
    L33:
        return r0;
    L14:
        if (r0 == true) goto L35;
        reset();
        return r0;
    L35:
        return r0;
    L16:
        if (r0 == true) goto L18;
        reset();
        return r0;
    L18:
        if (this.audioTimestamp.getTimestampPositionFrames() <= this.initialTimestampPositionFrames) goto L43;
        updateState(2);
        return r0;
    L43:
        return r0;
    L22:
        if (r0 == false) goto L28;
        if (this.audioTimestamp.getTimestampSystemTimeUs() < this.initializeSystemTimeUs) goto L26;
        this.initialTimestampPositionFrames = this.audioTimestamp.getTimestampPositionFrames();
        updateState(1);
        return r0;
    L26:
        return false;
    L28:
        if ((r6 - this.initializeSystemTimeUs) <= 500000) goto L40;
        updateState(3);
        return r0;
    L40:
        return r0;
    L41:
        return r0;
    L10:
        throw new IllegalStateException();
    L44:
        return false;
    }

    public void rejectTimestamp() {
        updateState(4);
    }

    public void reset() {
        if (this.audioTimestamp == null) goto L6;
        updateState(0);
        return;
    }
}
