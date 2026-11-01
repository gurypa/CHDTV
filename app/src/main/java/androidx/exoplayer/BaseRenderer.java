package androidx.media3.exoplayer;

import androidx.media3.common.Format;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.SampleStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseRenderer implements Renderer, RendererCapabilities {
    private Clock clock;
    private RendererConfiguration configuration;
    private final FormatHolder formatHolder;
    private int index;
    private long lastResetPositionUs;
    private final Object lock;
    private PlayerId playerId;
    private long readingPositionUs;
    private RendererCapabilities.Listener rendererCapabilitiesListener;
    private int state;
    private SampleStream stream;
    private Format[] streamFormats;
    private boolean streamIsFinal;
    private long streamOffsetUs;
    private boolean throwRendererExceptionIsExecuting;
    private Timeline timeline;
    private final int trackType;

    public BaseRenderer(int r3) {
        this.lock = new Object();
        this.trackType = r3;
        this.formatHolder = new FormatHolder();
        this.readingPositionUs = Long.MIN_VALUE;
        this.timeline = Timeline.EMPTY;
    }

    private void resetPosition(long r2, boolean r4) throws ExoPlaybackException {
        this.streamIsFinal = false;
        this.lastResetPositionUs = r2;
        this.readingPositionUs = r2;
        onPositionReset(r2, r4);
    }

    @Override // androidx.media3.exoplayer.RendererCapabilities
    public final void clearListener() {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.rendererCapabilitiesListener = null;     // Catch: Throwable -> L8
        monitor-exit(r0);     // Catch: Throwable -> L8
        return;
    L8:
        th = move-exception;
        throw th;
    }

    protected final ExoPlaybackException createRendererException(Throwable r2, Format r3, int r4) {
        return createRendererException(r2, r3, false, r4);
    }

    protected final ExoPlaybackException createRendererException(Throwable r11, Format r12, boolean r13, int r14) {
        if (r12 != null) goto L5;
    L14:
        int r7 = 4;
    L16:
        return ExoPlaybackException.createForRenderer(r11, getName(), getIndex(), r12, r7, r13, r14);
    L5:
        if (this.throwRendererExceptionIsExecuting == true) goto L14;
        this.throwRendererExceptionIsExecuting = true;
        int r0 = RendererCapabilities.CC.getFormatSupport(supportsFormat(r12));     // Catch: Throwable -> L9 ExoPlaybackException -> L12
        this.throwRendererExceptionIsExecuting = false;
        r7 = r0;
        goto L16
    L9:
        th = move-exception;
        this.throwRendererExceptionIsExecuting = false;
        throw th;
    L13:
        this.throwRendererExceptionIsExecuting = false;
        goto L14
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void disable() {
        boolean r2 = true;
        if (this.state == 1) goto L6;
        r2 = false;
    L6:
        Assertions.checkState(r2);
        this.formatHolder.clear();
        this.state = 0;
        this.stream = null;
        this.streamFormats = null;
        this.streamIsFinal = false;
        onDisabled();
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void enable(RendererConfiguration r11, Format[] r12, SampleStream r13, long r14, boolean r16, boolean r17, long r18, long r20, MediaSource.MediaPeriodId r22) throws ExoPlaybackException {
        if (this.state != 0) goto L5;
        boolean r1 = true;
    L6:
        Assertions.checkState(r1);
        this.configuration = r11;
        this.state = 1;
        onEnabled(r16, r17);
        replaceStream(r12, r13, r18, r20, r22);
        resetPosition(r18, r16);
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

    protected final Clock getClock() {
        return (Clock) Assertions.checkNotNull(this.clock);
    }

    protected final RendererConfiguration getConfiguration() {
        return (RendererConfiguration) Assertions.checkNotNull(this.configuration);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public /* synthetic */ long getDurationToProgressUs(long r1, long r3) {
        return Renderer.CC.$default$getDurationToProgressUs(this, r1, r3);
    }

    protected final FormatHolder getFormatHolder() {
        this.formatHolder.clear();
        return this.formatHolder;
    }

    protected final int getIndex() {
        return this.index;
    }

    protected final long getLastResetPositionUs() {
        return this.lastResetPositionUs;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public MediaClock getMediaClock() {
        return null;
    }

    protected final PlayerId getPlayerId() {
        return (PlayerId) Assertions.checkNotNull(this.playerId);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final long getReadingPositionUs() {
        return this.readingPositionUs;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final int getState() {
        return this.state;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final SampleStream getStream() {
        return this.stream;
    }

    protected final Format[] getStreamFormats() {
        return (Format[]) Assertions.checkNotNull(this.streamFormats);
    }

    protected final long getStreamOffsetUs() {
        return this.streamOffsetUs;
    }

    protected final Timeline getTimeline() {
        return this.timeline;
    }

    @Override // androidx.media3.exoplayer.Renderer, androidx.media3.exoplayer.RendererCapabilities
    public final int getTrackType() {
        return this.trackType;
    }

    @Override // androidx.media3.exoplayer.PlayerMessage.Target
    public void handleMessage(int r1, Object r2) throws ExoPlaybackException {
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final boolean hasReadStreamToEnd() {
        if (this.readingPositionUs != Long.MIN_VALUE) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void init(int r1, PlayerId r2, Clock r3) {
        this.index = r1;
        this.playerId = r2;
        this.clock = r3;
        onInit();
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final boolean isCurrentStreamFinal() {
        return this.streamIsFinal;
    }

    protected final boolean isSourceReady() {
        if (hasReadStreamToEnd() == false) goto L6;
        return this.streamIsFinal;
    L6:
        return ((SampleStream) Assertions.checkNotNull(this.stream)).isReady();
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void maybeThrowStreamError() throws IOException {
        ((SampleStream) Assertions.checkNotNull(this.stream)).maybeThrowError();
    }

    protected void onDisabled() {
    }

    protected void onEnabled(boolean r1, boolean r2) throws ExoPlaybackException {
    }

    protected void onInit() {
    }

    protected void onPositionReset(long r1, boolean r3) throws ExoPlaybackException {
    }

    protected void onRelease() {
    }

    protected final void onRendererCapabilitiesChanged() {
        Object r0 = this.lock;
        monitor-enter(r0);
        RendererCapabilities.Listener r1 = this.rendererCapabilitiesListener;     // Catch: Throwable -> L9
        monitor-exit(r0);     // Catch: Throwable -> L9
        if (r1 == null) goto L14;
        r1.onRendererCapabilitiesChanged(this);
        return;
    L14:
        return;
    L9:
        th = move-exception;
        throw th;
    }

    protected void onReset() {
    }

    protected void onStarted() throws ExoPlaybackException {
    }

    protected void onStopped() {
    }

    protected void onStreamChanged(Format[] r1, long r2, long r4, MediaSource.MediaPeriodId r6) throws ExoPlaybackException {
    }

    protected void onTimelineChanged(Timeline r1) {
    }

    protected final int readSource(FormatHolder r8, DecoderInputBuffer r9, int r10) {
        int r0 = ((SampleStream) Assertions.checkNotNull(this.stream)).readData(r8, r9, r10);
        if (r0 != (-4)) goto L13;
        if (r9.isEndOfStream() == false) goto L11;
        this.readingPositionUs = Long.MIN_VALUE;
        if (this.streamIsFinal == true) goto L18;
        return -3;
    L18:
        return -4;
    L11:
        r9.timeUs += this.streamOffsetUs;
        this.readingPositionUs = Math.max(this.readingPositionUs, r9.timeUs);
    L17:
        return r0;
    L13:
        if (r0 != (-5)) goto L17;
        Format r1 = (Format) Assertions.checkNotNull(r8.format);
        if (r1.subsampleOffsetUs == Long.MAX_VALUE) goto L17;
        r8.format = r1.buildUpon().setSubsampleOffsetUs(r1.subsampleOffsetUs + this.streamOffsetUs).build();
        goto L17
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void release() {
        if (this.state != 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        onRelease();
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void replaceStream(Format[] r9, SampleStream r10, long r11, long r13, MediaSource.MediaPeriodId r15) throws ExoPlaybackException {
        Assertions.checkState(!this.streamIsFinal);
        this.stream = r10;
        if (this.readingPositionUs != Long.MIN_VALUE) goto L5;
        this.readingPositionUs = r11;
    L5:
        this.streamFormats = r9;
        this.streamOffsetUs = r13;
        onStreamChanged(r9, r11, r13, r15);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void reset() {
        if (this.state != 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        this.formatHolder.clear();
        onReset();
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void resetPosition(long r2) throws ExoPlaybackException {
        resetPosition(r2, false);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void setCurrentStreamFinal() {
        this.streamIsFinal = true;
    }

    @Override // androidx.media3.exoplayer.RendererCapabilities
    public final void setListener(RendererCapabilities.Listener r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
        this.rendererCapabilitiesListener = r3;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public /* synthetic */ void setPlaybackSpeed(float r1, float r2) {
        Renderer.CC.$default$setPlaybackSpeed(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public final void setTimeline(Timeline r2) {
        if (Util.areEqual(this.timeline, r2) == true) goto L6;
        this.timeline = r2;
        onTimelineChanged(this.timeline);
        return;
    }

    protected int skipSource(long r4) {
        return ((SampleStream) Assertions.checkNotNull(this.stream)).skipData(r4 - this.streamOffsetUs);
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
    public int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException {
        return 0;
    }
}
