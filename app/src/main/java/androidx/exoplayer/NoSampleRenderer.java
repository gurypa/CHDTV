package androidx.media3.exoplayer;

import androidx.media3.common.Format;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.SampleStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public abstract class NoSampleRenderer implements Renderer, RendererCapabilities {
    private RendererConfiguration configuration;
    private int index;
    private int state;
    private SampleStream stream;
    private boolean streamIsFinal;

    public NoSampleRenderer() {
    }

    @Override // androidx.media3.exoplayer.RendererCapabilities
    public /* synthetic */ void clearListener() {
        RendererCapabilities.CC.$default$clearListener(this);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void disable() {
        boolean r2 = true;
        if (this.state == 1) goto L6;
        r2 = false;
    L6:
        Assertions.checkState(r2);
        this.state = 0;
        this.stream = null;
        this.streamIsFinal = false;
        onDisabled();
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void enable(RendererConfiguration r10, Format[] r11, SampleStream r12, long r13, boolean r15, boolean r16, long r17, long r19, MediaSource.MediaPeriodId r21) throws ExoPlaybackException {
        if (this.state != 0) goto L5;
        boolean r1 = true;
    L6:
        Assertions.checkState(r1);
        this.configuration = r10;
        this.state = 1;
        onEnabled(r15);
        replaceStream(r11, r12, r17, r19, r21);
        onPositionReset(r13, r15);
        return;
    L5:
        r1 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.Renderer
    public /* synthetic */ void enableMayRenderStartOfStream() {
        Renderer.CC.$default$enableMayRenderStartOfStream(this);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final RendererCapabilities getCapabilities() {
        return this;
    }

    protected final RendererConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public /* synthetic */ long getDurationToProgressUs(long r1, long r3) {
        return Renderer.CC.$default$getDurationToProgressUs(this, r1, r3);
    }

    protected final int getIndex() {
        return this.index;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public MediaClock getMediaClock() {
        return null;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public long getReadingPositionUs() {
        return Long.MIN_VALUE;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final int getState() {
        return this.state;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final SampleStream getStream() {
        return this.stream;
    }

    @Override // androidx.media3.exoplayer.Renderer, androidx.media3.exoplayer.RendererCapabilities
    public final int getTrackType() {
        return -2;
    }

    @Override // androidx.media3.exoplayer.PlayerMessage.Target
    public void handleMessage(int r1, Object r2) throws ExoPlaybackException {
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final boolean hasReadStreamToEnd() {
        return true;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void init(int r1, PlayerId r2, Clock r3) {
        this.index = r1;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final boolean isCurrentStreamFinal() {
        return this.streamIsFinal;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public boolean isEnded() {
        return true;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public boolean isReady() {
        return true;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void maybeThrowStreamError() throws IOException {
    }

    protected void onDisabled() {
    }

    protected void onEnabled(boolean r1) throws ExoPlaybackException {
    }

    protected void onPositionReset(long r1, boolean r3) throws ExoPlaybackException {
    }

    protected void onRendererOffsetChanged(long r1) throws ExoPlaybackException {
    }

    protected void onReset() {
    }

    protected void onStarted() throws ExoPlaybackException {
    }

    protected void onStopped() {
    }

    @Override // androidx.media3.exoplayer.Renderer
    public /* synthetic */ void release() {
        Renderer.CC.$default$release(this);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void replaceStream(Format[] r2, SampleStream r3, long r4, long r6, MediaSource.MediaPeriodId r8) throws ExoPlaybackException {
        Assertions.checkState(!this.streamIsFinal);
        this.stream = r3;
        onRendererOffsetChanged(r6);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void reset() {
        if (this.state != 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        onReset();
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void resetPosition(long r2) throws ExoPlaybackException {
        this.streamIsFinal = false;
        onPositionReset(r2, false);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void setCurrentStreamFinal() {
        this.streamIsFinal = true;
    }

    @Override // androidx.media3.exoplayer.RendererCapabilities
    public /* synthetic */ void setListener(RendererCapabilities.Listener r1) {
        RendererCapabilities.CC.$default$setListener(this, r1);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public /* synthetic */ void setPlaybackSpeed(float r1, float r2) {
        Renderer.CC.$default$setPlaybackSpeed(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public void setTimeline(Timeline r1) {
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void start() throws ExoPlaybackException {
        boolean r1 = true;
        if (this.state == 1) goto L6;
        r1 = false;
    L6:
        Assertions.checkState(r1);
        this.state = 2;
        onStarted();
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void stop() {
        if (this.state != 2) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        this.state = 1;
        onStopped();
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.RendererCapabilities
    public int supportsFormat(Format r2) throws ExoPlaybackException {
        return RendererCapabilities.CC.create(0);
    }

    @Override // androidx.media3.exoplayer.RendererCapabilities
    public int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException {
        return 0;
    }
}
