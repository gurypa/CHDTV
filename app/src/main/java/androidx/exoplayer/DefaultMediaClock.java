package androidx.media3.exoplayer;

import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;

/* JADX INFO: loaded from: classes.dex */
final class DefaultMediaClock implements MediaClock {
    private boolean isUsingStandaloneClock;
    private final PlaybackParametersListener listener;
    private MediaClock rendererClock;
    private Renderer rendererClockSource;
    private final StandaloneMediaClock standaloneClock;
    private boolean standaloneClockIsStarted;

    public interface PlaybackParametersListener {
        void onPlaybackParametersChanged(PlaybackParameters r1);
    }

    public DefaultMediaClock(PlaybackParametersListener r2, Clock r3) {
        this.listener = r2;
        this.standaloneClock = new StandaloneMediaClock(r3);
        this.isUsingStandaloneClock = true;
    }

    private boolean shouldUseStandaloneClock(boolean r3) {
        if (this.rendererClockSource != null) goto L5;
    L16:
        return true;
    L5:
        if (this.rendererClockSource.isEnded() == true) goto L16;
        if (r3 == false) goto L10;
        if (this.rendererClockSource.getState() != 2) goto L16;
    L10:
        if (this.rendererClockSource.isReady() == true) goto L15;
        if (r3 == true) goto L16;
        if (this.rendererClockSource.hasReadStreamToEnd() == true) goto L16;
    L15:
        return false;
    }

    private void syncClocks(boolean r6) {
        if (shouldUseStandaloneClock(r6) == false) goto L8;
        this.isUsingStandaloneClock = true;
        if (this.standaloneClockIsStarted == false) goto L21;
        this.standaloneClock.start();
        return;
    L21:
        return;
    L8:
        MediaClock r0 = (MediaClock) Assertions.checkNotNull(this.rendererClock);
        long r1 = r0.getPositionUs();
        if (this.isUsingStandaloneClock == true) goto L11;
    L17:
        this.standaloneClock.resetPosition(r1);
        PlaybackParameters r3 = r0.getPlaybackParameters();
        if (r3.equals(this.standaloneClock.getPlaybackParameters()) == true) goto L22;
        this.standaloneClock.setPlaybackParameters(r3);
        this.listener.onPlaybackParametersChanged(r3);
        return;
    L22:
        return;
    L11:
        if (r1 >= this.standaloneClock.getPositionUs()) goto L14;
        this.standaloneClock.stop();
        return;
    L14:
        this.isUsingStandaloneClock = false;
        if (this.standaloneClockIsStarted == false) goto L17;
        this.standaloneClock.start();
        goto L17
    }

    @Override // androidx.media3.exoplayer.MediaClock
    public PlaybackParameters getPlaybackParameters() {
        if (this.rendererClock == null) goto L6;
        return this.rendererClock.getPlaybackParameters();
    L6:
        return this.standaloneClock.getPlaybackParameters();
    }

    @Override // androidx.media3.exoplayer.MediaClock
    public long getPositionUs() {
        if (this.isUsingStandaloneClock == false) goto L6;
        return this.standaloneClock.getPositionUs();
    L6:
        return ((MediaClock) Assertions.checkNotNull(this.rendererClock)).getPositionUs();
    }

    @Override // androidx.media3.exoplayer.MediaClock
    public boolean hasSkippedSilenceSinceLastCall() {
        if (this.isUsingStandaloneClock == false) goto L6;
        return this.standaloneClock.hasSkippedSilenceSinceLastCall();
    L6:
        return ((MediaClock) Assertions.checkNotNull(this.rendererClock)).hasSkippedSilenceSinceLastCall();
    }

    public void onRendererDisabled(Renderer r2) {
        if (r2 != this.rendererClockSource) goto L6;
        this.rendererClock = null;
        this.rendererClockSource = null;
        this.isUsingStandaloneClock = true;
        return;
    }

    public void onRendererEnabled(Renderer r4) throws ExoPlaybackException {
        MediaClock r0 = r4.getMediaClock();
        if (r0 != null) goto L5;
        return;
    L5:
        if (r0 != this.rendererClock) goto L7;
        return;
    L7:
        if (this.rendererClock != null) goto L10;
        this.rendererClock = r0;
        this.rendererClockSource = r4;
        this.rendererClock.setPlaybackParameters(this.standaloneClock.getPlaybackParameters());
        return;
    L10:
        throw ExoPlaybackException.createForUnexpected(new IllegalStateException("Multiple renderer media clocks enabled."), 1000);
    }

    public void resetPosition(long r2) {
        this.standaloneClock.resetPosition(r2);
    }

    @Override // androidx.media3.exoplayer.MediaClock
    public void setPlaybackParameters(PlaybackParameters r2) {
        if (this.rendererClock == null) goto L5;
        this.rendererClock.setPlaybackParameters(r2);
        r2 = this.rendererClock.getPlaybackParameters();
    L5:
        this.standaloneClock.setPlaybackParameters(r2);
    }

    public void start() {
        this.standaloneClockIsStarted = true;
        this.standaloneClock.start();
    }

    public void stop() {
        this.standaloneClockIsStarted = false;
        this.standaloneClock.stop();
    }

    public long syncAndGetPositionUs(boolean r3) {
        syncClocks(r3);
        return getPositionUs();
    }
}
