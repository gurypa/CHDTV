package androidx.media3.exoplayer;

import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class StandaloneMediaClock implements MediaClock {
    private long baseElapsedMs;
    private long baseUs;
    private final Clock clock;
    private PlaybackParameters playbackParameters;
    private boolean started;

    public StandaloneMediaClock(Clock r2) {
        this.clock = r2;
        this.playbackParameters = PlaybackParameters.DEFAULT;
    }

    @Override // androidx.media3.exoplayer.MediaClock
    public PlaybackParameters getPlaybackParameters() {
        return this.playbackParameters;
    }

    @Override // androidx.media3.exoplayer.MediaClock
    public long getPositionUs() {
        long r0 = this.baseUs;
        if (this.started == false) goto L9;
        long r2 = this.clock.elapsedRealtime() - this.baseElapsedMs;
        if (this.playbackParameters.speed != 1.0f) goto L8;
        return r0 + Util.msToUs(r2);
    L8:
        return r0 + this.playbackParameters.getMediaTimeUsForPlayoutTimeMs(r2);
    L9:
        return r0;
    }

    @Override // androidx.media3.exoplayer.MediaClock
    public /* synthetic */ boolean hasSkippedSilenceSinceLastCall() {
        return MediaClock.CC.$default$hasSkippedSilenceSinceLastCall(this);
    }

    public void resetPosition(long r3) {
        this.baseUs = r3;
        if (this.started == false) goto L6;
        this.baseElapsedMs = this.clock.elapsedRealtime();
        return;
    }

    @Override // androidx.media3.exoplayer.MediaClock
    public void setPlaybackParameters(PlaybackParameters r3) {
        if (this.started == false) goto L5;
        resetPosition(getPositionUs());
    L5:
        this.playbackParameters = r3;
    }

    public void start() {
        if (this.started == true) goto L6;
        this.baseElapsedMs = this.clock.elapsedRealtime();
        this.started = true;
        return;
    }

    public void stop() {
        if (this.started == false) goto L6;
        resetPosition(getPositionUs());
        this.started = false;
        return;
    }
}
