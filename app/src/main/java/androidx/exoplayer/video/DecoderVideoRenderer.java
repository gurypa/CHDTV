package androidx.media3.exoplayer.video;

import android.os.Handler;
import android.os.SystemClock;
import android.view.Surface;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.VideoSize;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.TimedValueQueue;
import androidx.media3.common.util.TraceUtil;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.CryptoConfig;
import androidx.media3.decoder.Decoder;
import androidx.media3.decoder.DecoderException;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.decoder.VideoDecoderOutputBuffer;
import androidx.media3.exoplayer.BaseRenderer;
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.DecoderReuseEvaluation;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.audio.SilenceSkippingAudioProcessor;
import androidx.media3.exoplayer.drm.DrmSession;
import androidx.media3.exoplayer.source.MediaSource;

/* JADX INFO: loaded from: classes.dex */
public abstract class DecoderVideoRenderer extends BaseRenderer {
    private static final int REINITIALIZATION_STATE_NONE = 0;
    private static final int REINITIALIZATION_STATE_SIGNAL_END_OF_STREAM = 1;
    private static final int REINITIALIZATION_STATE_WAIT_END_OF_STREAM = 2;
    private static final String TAG = "DecoderVideoRenderer";
    private final long allowedJoiningTimeMs;
    private int buffersInCodecCount;
    private int consecutiveDroppedFrameCount;
    private Decoder<DecoderInputBuffer, ? extends VideoDecoderOutputBuffer, ? extends DecoderException> decoder;
    protected DecoderCounters decoderCounters;
    private DrmSession decoderDrmSession;
    private boolean decoderReceivedBuffers;
    private int decoderReinitializationState;
    private long droppedFrameAccumulationStartTimeMs;
    private int droppedFrames;
    private final VideoRendererEventListener.EventDispatcher eventDispatcher;
    private int firstFrameState;
    private final DecoderInputBuffer flagsOnlyBuffer;
    private final TimedValueQueue<Format> formatQueue;
    private VideoFrameMetadataListener frameMetadataListener;
    private long initialPositionUs;
    private DecoderInputBuffer inputBuffer;
    private Format inputFormat;
    private boolean inputStreamEnded;
    private long joiningDeadlineMs;
    private long lastRenderTimeUs;
    private final int maxDroppedFramesToNotify;
    private Object output;
    private VideoDecoderOutputBuffer outputBuffer;
    private VideoDecoderOutputBufferRenderer outputBufferRenderer;
    private Format outputFormat;
    private int outputMode;
    private boolean outputStreamEnded;
    private Surface outputSurface;
    private VideoSize reportedVideoSize;
    private DrmSession sourceDrmSession;
    private boolean waitingForFirstSampleInFormat;

    protected DecoderVideoRenderer(long r3, Handler r5, VideoRendererEventListener r6, int r7) {
        super(2);
        this.allowedJoiningTimeMs = r3;
        this.maxDroppedFramesToNotify = r7;
        this.joiningDeadlineMs = C0180C.TIME_UNSET;
        this.formatQueue = new TimedValueQueue();
        this.flagsOnlyBuffer = DecoderInputBuffer.newNoDataInstance();
        this.eventDispatcher = new VideoRendererEventListener.EventDispatcher(r5, r6);
        this.decoderReinitializationState = 0;
        this.outputMode = -1;
        this.firstFrameState = 0;
        this.decoderCounters = new DecoderCounters();
    }

    private boolean drainOutputBuffer(long r6, long r8) throws ExoPlaybackException, DecoderException {
        if (this.outputBuffer != null) goto L9;
        this.outputBuffer = (VideoDecoderOutputBuffer) ((Decoder) Assertions.checkNotNull(this.decoder)).dequeueOutputBuffer();
        if (this.outputBuffer != null) goto L7;
        return false;
    L7:
        this.decoderCounters.skippedOutputBufferCount += this.outputBuffer.skippedOutputBufferCount;
        this.buffersInCodecCount -= this.outputBuffer.skippedOutputBufferCount;
    L9:
        if (this.outputBuffer.isEndOfStream() == true) goto L11;
        boolean r0 = processOutputBuffer(r6, r8);
        if (r0 == false) goto L18;
        onProcessedOutputBuffer(((VideoDecoderOutputBuffer) Assertions.checkNotNull(this.outputBuffer)).timeUs);
        this.outputBuffer = null;
    L18:
        return r0;
    L11:
        if (this.decoderReinitializationState != 2) goto L13;
        releaseDecoder();
        maybeInitDecoder();
    L14:
        return false;
    L13:
        this.outputBuffer.release();
        this.outputBuffer = null;
        this.outputStreamEnded = true;
        goto L14
    }

    private boolean feedInputBuffer() throws DecoderException, ExoPlaybackException {
        if (this.decoder != null) goto L5;
    L34:
        return false;
    L5:
        if (this.decoderReinitializationState == 2) goto L34;
        if (this.inputStreamEnded == true) goto L34;
        if (this.inputBuffer != null) goto L14;
        this.inputBuffer = this.decoder.dequeueInputBuffer();
        if (this.inputBuffer != null) goto L14;
        return false;
    L14:
        DecoderInputBuffer r0 = (DecoderInputBuffer) Assertions.checkNotNull(this.inputBuffer);
        if (this.decoderReinitializationState != 1) goto L18;
        r0.setFlags(4);
        ((Decoder) Assertions.checkNotNull(this.decoder)).queueInputBuffer(r0);
        this.inputBuffer = null;
        this.decoderReinitializationState = 2;
        return false;
    L18:
        FormatHolder r2 = getFormatHolder();
        switch(readSource(r2, r0, 0)) {
            case -5: goto L32;
            case -4: goto L24;
            case -3: goto L22;
            default: goto L21;
        };
    L22:
        return false;
    L32:
        onInputFormatChanged(r2);
        return true;
    L21:
        throw new IllegalStateException();
    L24:
        if (r0.isEndOfStream() == false) goto L28;
        this.inputStreamEnded = true;
        ((Decoder) Assertions.checkNotNull(this.decoder)).queueInputBuffer(r0);
        this.inputBuffer = null;
        return false;
    L28:
        if (this.waitingForFirstSampleInFormat == false) goto L30;
        this.formatQueue.add(r0.timeUs, (Format) Assertions.checkNotNull(this.inputFormat));
        this.waitingForFirstSampleInFormat = false;
    L30:
        r0.flip();
        r0.format = this.inputFormat;
        onQueueInputBuffer(r0);
        ((Decoder) Assertions.checkNotNull(this.decoder)).queueInputBuffer(r0);
        this.buffersInCodecCount++;
        this.decoderReceivedBuffers = true;
        this.decoderCounters.queuedInputBufferCount++;
        this.inputBuffer = null;
        return true;
    }

    private boolean hasOutput() {
        if (this.outputMode == (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    private static boolean isBufferLate(long r2) {
        if (r2 >= (-30000)) goto L5;
        return true;
    L5:
        return false;
    }

    private static boolean isBufferVeryLate(long r2) {
        if (r2 >= (-500000)) goto L5;
        return true;
    L5:
        return false;
    }

    private void lowerFirstFrameState(int r2) {
        this.firstFrameState = Math.min(this.firstFrameState, r2);
    }

    private void maybeInitDecoder() throws ExoPlaybackException {
        if (this.decoder == null) goto L5;
        return;
    L5:
        setDecoderDrmSession(this.sourceDrmSession);
        if (this.decoderDrmSession == null) goto L14;
        CryptoConfig r0 = this.decoderDrmSession.getCryptoConfig();
        if (r0 == null) goto L10;
    L13:
        CryptoConfig r1 = r0;
    L24:
        long r3 = SystemClock.elapsedRealtime();     // Catch: OutOfMemoryError -> L18 DecoderException -> L21
        this.decoder = createDecoder((Format) Assertions.checkNotNull(this.inputFormat), r1);     // Catch: OutOfMemoryError -> L18 DecoderException -> L21
        this.decoder.setOutputStartTimeUs(getLastResetPositionUs());     // Catch: OutOfMemoryError -> L18 DecoderException -> L21
        setDecoderOutputMode(this.outputMode);     // Catch: OutOfMemoryError -> L18 DecoderException -> L21
        long r5 = SystemClock.elapsedRealtime();     // Catch: OutOfMemoryError -> L18 DecoderException -> L21
        this.eventDispatcher.decoderInitialized(((Decoder) Assertions.checkNotNull(this.decoder)).getName(), r5, r5 - r3);     // Catch: OutOfMemoryError -> L18 DecoderException -> L21
        this.decoderCounters.decoderInitCount++;     // Catch: OutOfMemoryError -> L18 DecoderException -> L21
        return;
    L21:
        e = move-exception;
        Log.m45e(TAG, "Video codec error", e);
        this.eventDispatcher.videoCodecError(e);
        throw createRendererException(e, this.inputFormat, PlaybackException.ERROR_CODE_DECODER_INIT_FAILED);
    L18:
        e = move-exception;
        throw createRendererException(e, this.inputFormat, PlaybackException.ERROR_CODE_DECODER_INIT_FAILED);
    L10:
        if (this.decoderDrmSession.getError() != null) goto L13;
        return;
    L14:
        r1 = null;
        goto L24
    }

    private void maybeNotifyDroppedFrames() {
        if (this.droppedFrames <= 0) goto L6;
        long r0 = SystemClock.elapsedRealtime();
        long r2 = r0 - this.droppedFrameAccumulationStartTimeMs;
        this.eventDispatcher.droppedFrames(this.droppedFrames, r2);
        this.droppedFrames = 0;
        this.droppedFrameAccumulationStartTimeMs = r0;
        return;
    }

    private void maybeNotifyRenderedFirstFrame() {
        if (this.firstFrameState == 3) goto L8;
        this.firstFrameState = 3;
        if (this.output == null) goto L9;
        this.eventDispatcher.renderedFirstFrame(this.output);
        return;
    L9:
        return;
    }

    private void maybeNotifyVideoSizeChanged(int r3, int r4) {
        if (this.reportedVideoSize != null) goto L5;
    L8:
        this.reportedVideoSize = new VideoSize(r3, r4);
        this.eventDispatcher.videoSizeChanged(this.reportedVideoSize);
        return;
    L5:
        if (this.reportedVideoSize.width != r3) goto L8;
        if (this.reportedVideoSize.height != r4) goto L8;
    }

    private void maybeRenotifyRenderedFirstFrame() {
        if (this.firstFrameState == 3) goto L5;
        return;
    L5:
        if (this.output == null) goto L9;
        this.eventDispatcher.renderedFirstFrame(this.output);
        return;
    }

    private void maybeRenotifyVideoSizeChanged() {
        if (this.reportedVideoSize == null) goto L6;
        this.eventDispatcher.videoSizeChanged(this.reportedVideoSize);
        return;
    }

    private void onOutputChanged() {
        maybeRenotifyVideoSizeChanged();
        lowerFirstFrameState(1);
        if (getState() != 2) goto L6;
        setJoiningDeadlineMs();
        return;
    }

    private void onOutputRemoved() {
        this.reportedVideoSize = null;
        lowerFirstFrameState(1);
    }

    private void onOutputReset() {
        maybeRenotifyVideoSizeChanged();
        maybeRenotifyRenderedFirstFrame();
    }

    private boolean processOutputBuffer(long r20, long r22) throws ExoPlaybackException, DecoderException {
        if (this.initialPositionUs != C0180C.TIME_UNSET) goto L5;
        this.initialPositionUs = r20;
    L5:
        VideoDecoderOutputBuffer r5 = (VideoDecoderOutputBuffer) Assertions.checkNotNull(this.outputBuffer);
        long r6 = r5.timeUs;
        long r8 = r6 - r20;
        if (hasOutput() == false) goto L8;
        Format r10 = this.formatQueue.pollFloor(r6);
        if (r10 == null) goto L16;
        this.outputFormat = r10;
    L18:
        long r13 = r6 - getStreamOffsetUs();
        if (shouldForceRender(r8) == false) goto L23;
        renderOutputBuffer(r5, r13, (Format) Assertions.checkNotNull(this.outputFormat));
        return true;
    L23:
        if (getState() != 2) goto L25;
        boolean r11 = true;
    L26:
        if (r11 == false) goto L45;
        if (r20 != this.initialPositionUs) goto L31;
        return false;
    L31:
        if (shouldDropBuffersToKeyframe(r8, r22) == false) goto L36;
        if (maybeDropBuffersToKeyframe(r20) == false) goto L36;
        return false;
    L36:
        if (shouldDropOutputBuffer(r8, r22) == false) goto L40;
        dropOutputBuffer(r5);
        return true;
    L40:
        if (r8 >= 30000) goto L43;
        renderOutputBuffer(r5, r13, (Format) Assertions.checkNotNull(this.outputFormat));
        return true;
    L43:
        return false;
    L45:
        return false;
    L25:
        r11 = false;
        goto L26
    L16:
        if (this.outputFormat != null) goto L18;
        this.outputFormat = this.formatQueue.pollFirst();
        goto L18
    L8:
        if (isBufferLate(r8) == false) goto L11;
        skipOutputBuffer(r5);
        return true;
    L11:
        return false;
    }

    private void setDecoderDrmSession(DrmSession r2) {
        DrmSession.CC.replaceSession(this.decoderDrmSession, r2);
        this.decoderDrmSession = r2;
    }

    private void setJoiningDeadlineMs() {
        if (this.allowedJoiningTimeMs <= 0) goto L5;
        long r0 = SystemClock.elapsedRealtime() + this.allowedJoiningTimeMs;
    L6:
        this.joiningDeadlineMs = r0;
        return;
    L5:
        r0 = C0180C.TIME_UNSET;
        goto L6
    }

    private void setSourceDrmSession(DrmSession r2) {
        DrmSession.CC.replaceSession(this.sourceDrmSession, r2);
        this.sourceDrmSession = r2;
    }

    private boolean shouldForceRender(long r9) {
        if (getState() != 2) goto L5;
        boolean r0 = true;
    L7:
        switch(this.firstFrameState) {
            case 0: goto L17;
            case 1: goto L16;
            case 2: goto L9;
            case 3: goto L10;
            default: goto L9;
        };
    L10:
        long r4 = Util.msToUs(SystemClock.elapsedRealtime()) - this.lastRenderTimeUs;
        if (r0 == true) goto L13;
        return false;
    L13:
        if (shouldForceRenderOutputBuffer(r9, r4) == true) goto L15;
        return false;
    L15:
        return true;
    L16:
        return true;
    L17:
        return r0;
    L9:
        throw new IllegalStateException();
    L5:
        r0 = false;
        goto L7
    }

    protected DecoderReuseEvaluation canReuseDecoder(String r7, Format r8, Format r9) {
        return new DecoderReuseEvaluation(r7, r8, r9, 0, 1);
    }

    protected abstract Decoder<DecoderInputBuffer, ? extends VideoDecoderOutputBuffer, ? extends DecoderException> createDecoder(Format r1, CryptoConfig r2) throws DecoderException;

    protected void dropOutputBuffer(VideoDecoderOutputBuffer r3) {
        updateDroppedBufferCounters(0, 1);
        r3.release();
    }

    @Override // androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.Renderer
    public void enableMayRenderStartOfStream() {
        if (this.firstFrameState != 0) goto L6;
        this.firstFrameState = 1;
        return;
    }

    protected void flushDecoder() throws ExoPlaybackException {
        this.buffersInCodecCount = 0;
        if (this.decoderReinitializationState == 0) goto L5;
        releaseDecoder();
        maybeInitDecoder();
        return;
    L5:
        this.inputBuffer = null;
        if (this.outputBuffer == null) goto L8;
        this.outputBuffer.release();
        this.outputBuffer = null;
    L8:
        Decoder r1 = (Decoder) Assertions.checkNotNull(this.decoder);
        r1.flush();
        r1.setOutputStartTimeUs(getLastResetPositionUs());
        this.decoderReceivedBuffers = false;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.PlayerMessage.Target
    public void handleMessage(int r2, Object r3) throws ExoPlaybackException {
        if (r2 != 1) goto L6;
        setOutput(r3);
        return;
    L6:
        if (r2 != 7) goto L8;
        this.frameMetadataListener = (VideoFrameMetadataListener) r3;
        return;
    L8:
        super.handleMessage(r2, r3);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public boolean isEnded() {
        return this.outputStreamEnded;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public boolean isReady() {
        if (this.inputFormat == null) goto L15;
        if (isSourceReady() == true) goto L9;
        if (this.outputBuffer == null) goto L15;
    L9:
        if (this.firstFrameState != 3) goto L11;
    L12:
        this.joiningDeadlineMs = C0180C.TIME_UNSET;
        return true;
    L11:
        if (hasOutput() == false) goto L12;
    L15:
        if (this.joiningDeadlineMs != C0180C.TIME_UNSET) goto L18;
        return false;
    L18:
        if (SystemClock.elapsedRealtime() >= this.joiningDeadlineMs) goto L20;
        return true;
    L20:
        this.joiningDeadlineMs = C0180C.TIME_UNSET;
        return false;
    }

    protected boolean maybeDropBuffersToKeyframe(long r5) throws ExoPlaybackException {
        int r0 = skipSource(r5);
        if (r0 != 0) goto L6;
        return false;
    L6:
        this.decoderCounters.droppedToKeyframeCount++;
        updateDroppedBufferCounters(r0, this.buffersInCodecCount);
        flushDecoder();
        return true;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onDisabled() {
        this.inputFormat = null;
        this.reportedVideoSize = null;
        lowerFirstFrameState(0);
        setSourceDrmSession(null);     // Catch: Throwable -> L6
        releaseDecoder();     // Catch: Throwable -> L6
        this.eventDispatcher.disabled(this.decoderCounters);
        return;
    L6:
        th = move-exception;
        this.eventDispatcher.disabled(this.decoderCounters);
        throw th;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onEnabled(boolean r3, boolean r4) throws ExoPlaybackException {
        this.decoderCounters = new DecoderCounters();
        this.eventDispatcher.enabled(this.decoderCounters);
        if (r4 == false) goto L5;
        int r0 = 1;
    L6:
        this.firstFrameState = r0;
        return;
    L5:
        r0 = 0;
        goto L6
    }

    protected void onInputFormatChanged(FormatHolder r9) throws ExoPlaybackException {
        this.waitingForFirstSampleInFormat = true;
        Format r5 = (Format) Assertions.checkNotNull(r9.format);
        setSourceDrmSession(r9.drmSession);
        Format r1 = this.inputFormat;
        this.inputFormat = r5;
        if (this.decoder != null) goto L7;
        maybeInitDecoder();
        this.eventDispatcher.inputFormatChanged((Format) Assertions.checkNotNull(this.inputFormat), null);
        return;
    L7:
        if (this.sourceDrmSession == this.decoderDrmSession) goto L9;
        DecoderReuseEvaluation r2 = new DecoderReuseEvaluation(this.decoder.getName(), (Format) Assertions.checkNotNull(r1), r5, 0, 128);
    L11:
        if (r2.result == 0) goto L13;
    L16:
        this.eventDispatcher.inputFormatChanged((Format) Assertions.checkNotNull(this.inputFormat), r2);
        return;
    L13:
        if (this.decoderReceivedBuffers == false) goto L15;
        this.decoderReinitializationState = 1;
        goto L16
    L15:
        releaseDecoder();
        maybeInitDecoder();
        goto L16
    L9:
        r2 = canReuseDecoder(this.decoder.getName(), (Format) Assertions.checkNotNull(r1), r5);
        goto L11
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onPositionReset(long r4, boolean r6) throws ExoPlaybackException {
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        lowerFirstFrameState(1);
        this.initialPositionUs = C0180C.TIME_UNSET;
        this.consecutiveDroppedFrameCount = 0;
        if (this.decoder == null) goto L5;
        flushDecoder();
    L5:
        if (r6 == false) goto L7;
        setJoiningDeadlineMs();
    L8:
        this.formatQueue.clear();
        return;
    L7:
        this.joiningDeadlineMs = C0180C.TIME_UNSET;
        goto L8
    }

    protected void onProcessedOutputBuffer(long r2) {
        this.buffersInCodecCount--;
    }

    protected void onQueueInputBuffer(DecoderInputBuffer r1) {
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onStarted() {
        this.droppedFrames = 0;
        this.droppedFrameAccumulationStartTimeMs = SystemClock.elapsedRealtime();
        this.lastRenderTimeUs = Util.msToUs(SystemClock.elapsedRealtime());
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onStopped() {
        this.joiningDeadlineMs = C0180C.TIME_UNSET;
        maybeNotifyDroppedFrames();
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onStreamChanged(Format[] r1, long r2, long r4, MediaSource.MediaPeriodId r6) throws ExoPlaybackException {
        super.onStreamChanged(r1, r2, r4, r6);
    }

    protected void releaseDecoder() {
        this.inputBuffer = null;
        this.outputBuffer = null;
        this.decoderReinitializationState = 0;
        this.decoderReceivedBuffers = false;
        this.buffersInCodecCount = 0;
        if (this.decoder == null) goto L5;
        this.decoderCounters.decoderReleaseCount++;
        this.decoder.release();
        this.eventDispatcher.decoderReleased(this.decoder.getName());
        this.decoder = null;
    L5:
        setDecoderDrmSession(null);
    }

    @Override // androidx.media3.exoplayer.Renderer
    public void render(long r4, long r6) throws ExoPlaybackException {
        if (this.outputStreamEnded == false) goto L6;
        return;
    L6:
        if (this.inputFormat != null) goto L15;
        FormatHolder r0 = getFormatHolder();
        this.flagsOnlyBuffer.clear();
        int r1 = readSource(r0, this.flagsOnlyBuffer, 2);
        if (r1 != (-5)) goto L11;
        onInputFormatChanged(r0);
        goto L15
    L11:
        if (r1 != (-4)) goto L14;
        Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
        this.inputStreamEnded = true;
        this.outputStreamEnded = true;
        return;
    L14:
        return;
    L15:
        maybeInitDecoder();
        if (this.decoder != null) goto L30;
        return;
    L30:
        TraceUtil.beginSection("drainAndFeed");     // Catch: DecoderException -> L26
    L19:
        if (drainOutputBuffer(r4, r6) == true) goto L19;
    L22:
        if (feedInputBuffer() == true) goto L22;
        TraceUtil.endSection();     // Catch: DecoderException -> L26
        this.decoderCounters.ensureUpdated();
        return;
    L26:
        e = move-exception;
        Log.m45e(TAG, "Video codec error", e);
        this.eventDispatcher.videoCodecError(e);
        throw createRendererException(e, this.inputFormat, PlaybackException.ERROR_CODE_DECODING_FAILED);
    }

    protected void renderOutputBuffer(VideoDecoderOutputBuffer r9, long r10, Format r12) throws DecoderException {
        if (this.frameMetadataListener == null) goto L6;
        this.frameMetadataListener.onVideoFrameAboutToBeRendered(r10, getClock().nanoTime(), r12, null);
    L6:
        this.lastRenderTimeUs = Util.msToUs(SystemClock.elapsedRealtime());
        int r11 = r9.mode;
        if (r11 == 1) goto L9;
    L11:
        boolean r0 = false;
    L12:
        if (r11 == 0) goto L14;
    L16:
        boolean r1 = false;
    L17:
        if (r1 == true) goto L20;
        if (r0 == true) goto L20;
        dropOutputBuffer(r9);
        return;
    L20:
        maybeNotifyVideoSizeChanged(r9.width, r9.height);
        if (r1 == false) goto L23;
        ((VideoDecoderOutputBufferRenderer) Assertions.checkNotNull(this.outputBufferRenderer)).setOutputBuffer(r9);
    L24:
        this.consecutiveDroppedFrameCount = 0;
        this.decoderCounters.renderedOutputBufferCount++;
        maybeNotifyRenderedFirstFrame();
        return;
    L23:
        renderOutputBufferToSurface(r9, (Surface) Assertions.checkNotNull(this.outputSurface));
        goto L24
    L14:
        if (this.outputBufferRenderer == null) goto L16;
        r1 = true;
        goto L17
    L9:
        if (this.outputSurface == null) goto L11;
        r0 = true;
        goto L12
    }

    protected abstract void renderOutputBufferToSurface(VideoDecoderOutputBuffer r1, Surface r2) throws DecoderException;

    protected abstract void setDecoderOutputMode(int r1);

    protected final void setOutput(Object r3) {
        if ((r3 instanceof Surface) == false) goto L6;
        this.outputSurface = (Surface) r3;
        this.outputBufferRenderer = null;
        this.outputMode = 1;
    L10:
        if (this.output == r3) goto L18;
        this.output = r3;
        if (r3 != null) goto L14;
        onOutputRemoved();
        return;
    L14:
        if (this.decoder == null) goto L16;
        setDecoderOutputMode(this.outputMode);
    L16:
        onOutputChanged();
        return;
    L18:
        if (r3 == null) goto L23;
        onOutputReset();
        return;
    L23:
        return;
    L6:
        if ((r3 instanceof VideoDecoderOutputBufferRenderer) == false) goto L8;
        this.outputSurface = null;
        this.outputBufferRenderer = (VideoDecoderOutputBufferRenderer) r3;
        this.outputMode = 0;
        goto L10
    L8:
        r3 = null;
        this.outputSurface = null;
        this.outputBufferRenderer = null;
        this.outputMode = -1;
        goto L10
    }

    protected boolean shouldDropBuffersToKeyframe(long r2, long r4) {
        return isBufferVeryLate(r2);
    }

    protected boolean shouldDropOutputBuffer(long r2, long r4) {
        return isBufferLate(r2);
    }

    protected boolean shouldForceRenderOutputBuffer(long r3, long r5) {
        if (isBufferLate(r3) == true) goto L5;
    L7:
        return false;
    L5:
        if (r5 <= SilenceSkippingAudioProcessor.DEFAULT_MINIMUM_SILENCE_DURATION_US) goto L7;
        return true;
    }

    protected void skipOutputBuffer(VideoDecoderOutputBuffer r3) {
        this.decoderCounters.skippedOutputBufferCount++;
        r3.release();
    }

    protected void updateDroppedBufferCounters(int r5, int r6) {
        this.decoderCounters.droppedInputBufferCount += r5;
        int r0 = r5 + r6;
        this.decoderCounters.droppedBufferCount += r0;
        this.droppedFrames += r0;
        this.consecutiveDroppedFrameCount += r0;
        this.decoderCounters.maxConsecutiveDroppedBufferCount = Math.max(this.consecutiveDroppedFrameCount, this.decoderCounters.maxConsecutiveDroppedBufferCount);
        if (this.maxDroppedFramesToNotify > 0) goto L5;
        return;
    L5:
        if (this.droppedFrames < this.maxDroppedFramesToNotify) goto L9;
        maybeNotifyDroppedFrames();
        return;
    }
}
