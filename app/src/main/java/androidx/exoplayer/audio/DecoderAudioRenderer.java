package androidx.media3.exoplayer.audio;

import android.media.AudioDeviceInfo;
import android.os.Handler;
import android.os.SystemClock;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.AuxEffectInfo;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.TraceUtil;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.CryptoConfig;
import androidx.media3.decoder.Decoder;
import androidx.media3.decoder.DecoderException;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.decoder.SimpleDecoderOutputBuffer;
import androidx.media3.exoplayer.BaseRenderer;
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.DecoderReuseEvaluation;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.MediaClock;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.media3.exoplayer.drm.DrmSession;
import androidx.media3.exoplayer.source.MediaSource;
import com.google.common.base.MoreObjects;

/* JADX INFO: loaded from: classes.dex */
public abstract class DecoderAudioRenderer<T extends Decoder<DecoderInputBuffer, ? extends SimpleDecoderOutputBuffer, ? extends DecoderException>> extends BaseRenderer implements MediaClock {
    private static final int MAX_PENDING_OUTPUT_STREAM_OFFSET_COUNT = 10;
    private static final int REINITIALIZATION_STATE_NONE = 0;
    private static final int REINITIALIZATION_STATE_SIGNAL_END_OF_STREAM = 1;
    private static final int REINITIALIZATION_STATE_WAIT_END_OF_STREAM = 2;
    private static final String TAG = "DecoderAudioRenderer";
    private boolean allowPositionDiscontinuity;
    private final AudioSink audioSink;
    private boolean audioTrackNeedsConfigure;
    private long currentPositionUs;
    private T decoder;
    private DecoderCounters decoderCounters;
    private DrmSession decoderDrmSession;
    private boolean decoderReceivedBuffers;
    private int decoderReinitializationState;
    private int encoderDelay;
    private int encoderPadding;
    private final AudioRendererEventListener.EventDispatcher eventDispatcher;
    private boolean firstStreamSampleRead;
    private final DecoderInputBuffer flagsOnlyBuffer;
    private boolean hasPendingReportedSkippedSilence;
    private DecoderInputBuffer inputBuffer;
    private Format inputFormat;
    private boolean inputStreamEnded;
    private SimpleDecoderOutputBuffer outputBuffer;
    private boolean outputStreamEnded;
    private long outputStreamOffsetUs;
    private int pendingOutputStreamOffsetCount;
    private final long[] pendingOutputStreamOffsetsUs;
    private DrmSession sourceDrmSession;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.audio.DecoderAudioRenderer$1 */
    static /* synthetic */ class C02681 {
    }

    private static final class Api23 {
        private Api23() {
        }

        public static void setAudioSinkPreferredDevice(AudioSink r1, Object r2) {
            r1.setPreferredDevice((AudioDeviceInfo) r2);
        }
    }

    private final class AudioSinkListener implements AudioSink.Listener {
        final /* synthetic */ DecoderAudioRenderer this$0;

        private AudioSinkListener(DecoderAudioRenderer r1) {
            this.this$0 = r1;
        }

        /* synthetic */ AudioSinkListener(DecoderAudioRenderer r1, C02681 r2) {
            this(r1);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public /* synthetic */ void onAudioCapabilitiesChanged() {
            AudioSink.Listener.CC.$default$onAudioCapabilitiesChanged(this);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onAudioSinkError(Exception r3) {
            Log.m45e(DecoderAudioRenderer.TAG, "Audio sink error", r3);
            DecoderAudioRenderer.access$200(this.this$0).audioSinkError(r3);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onAudioTrackInitialized(AudioSink.AudioTrackConfig r2) {
            DecoderAudioRenderer.access$200(this.this$0).audioTrackInitialized(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onAudioTrackReleased(AudioSink.AudioTrackConfig r2) {
            DecoderAudioRenderer.access$200(this.this$0).audioTrackReleased(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public /* synthetic */ void onOffloadBufferEmptying() {
            AudioSink.Listener.CC.$default$onOffloadBufferEmptying(this);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public /* synthetic */ void onOffloadBufferFull() {
            AudioSink.Listener.CC.$default$onOffloadBufferFull(this);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onPositionAdvancing(long r2) {
            DecoderAudioRenderer.access$200(this.this$0).positionAdvancing(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onPositionDiscontinuity() {
            this.this$0.onPositionDiscontinuity();
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onSilenceSkipped() {
            DecoderAudioRenderer.access$102(this.this$0, true);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onSkipSilenceEnabledChanged(boolean r2) {
            DecoderAudioRenderer.access$200(this.this$0).skipSilenceEnabledChanged(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onUnderrun(int r8, long r9, long r11) {
            DecoderAudioRenderer.access$200(this.this$0).underrun(r8, r9, r11);
        }
    }

    public DecoderAudioRenderer() {
        this(null, null, new AudioProcessor[0]);
    }

    public DecoderAudioRenderer(Handler r3, AudioRendererEventListener r4, AudioCapabilities r5, AudioProcessor... r6) {
        this(r3, r4, new DefaultAudioSink.Builder().setAudioCapabilities((AudioCapabilities) MoreObjects.firstNonNull(r5, AudioCapabilities.DEFAULT_AUDIO_CAPABILITIES)).setAudioProcessors(r6).build());
    }

    public DecoderAudioRenderer(Handler r4, AudioRendererEventListener r5, AudioSink r6) {
        super(1);
        this.eventDispatcher = new AudioRendererEventListener.EventDispatcher(r4, r5);
        this.audioSink = r6;
        r6.setListener(new AudioSinkListener(this, null));
        this.flagsOnlyBuffer = DecoderInputBuffer.newNoDataInstance();
        this.decoderReinitializationState = 0;
        this.audioTrackNeedsConfigure = true;
        setOutputStreamOffsetUs(C0180C.TIME_UNSET);
        this.pendingOutputStreamOffsetsUs = new long[10];
    }

    public DecoderAudioRenderer(Handler r2, AudioRendererEventListener r3, AudioProcessor... r4) {
        this(r2, r3, null, r4);
    }

    static /* synthetic */ boolean access$102(DecoderAudioRenderer r0, boolean r1) {
        r0.hasPendingReportedSkippedSilence = r1;
        return r1;
    }

    static /* synthetic */ AudioRendererEventListener.EventDispatcher access$200(DecoderAudioRenderer r1) {
        return r1.eventDispatcher;
    }

    private boolean drainOutputBuffer() throws ExoPlaybackException, DecoderException, AudioSink.ConfigurationException, AudioSink.InitializationException, AudioSink.WriteException {
        if (this.outputBuffer != null) goto L14;
        this.outputBuffer = (SimpleDecoderOutputBuffer) this.decoder.dequeueOutputBuffer();
        if (this.outputBuffer != null) goto L8;
        return false;
    L8:
        if (this.outputBuffer.skippedOutputBufferCount <= 0) goto L11;
        this.decoderCounters.skippedOutputBufferCount += this.outputBuffer.skippedOutputBufferCount;
        this.audioSink.handleDiscontinuity();
    L11:
        if (this.outputBuffer.isFirstSample() == false) goto L14;
        processFirstSampleOfStream();
    L14:
        if (this.outputBuffer.isEndOfStream() == false) goto L25;
        if (this.decoderReinitializationState != 2) goto L18;
        releaseDecoder();
        maybeInitDecoder();
        this.audioTrackNeedsConfigure = true;
    L20:
        return false;
    L18:
        this.outputBuffer.release();
        this.outputBuffer = null;
        processEndOfStream();     // Catch: AudioSink.WriteException -> L21
    L21:
        e = move-exception;
        throw createRendererException(e, e.format, e.isRecoverable, PlaybackException.ERROR_CODE_AUDIO_TRACK_WRITE_FAILED);
    L25:
        if (this.audioTrackNeedsConfigure == false) goto L28;
        this.audioSink.configure(getOutputFormat(this.decoder).buildUpon().setEncoderDelay(this.encoderDelay).setEncoderPadding(this.encoderPadding).setMetadata(this.inputFormat.metadata).setCustomData(this.inputFormat.customData).setId(this.inputFormat.f26id).setLabel(this.inputFormat.label).setLabels(this.inputFormat.labels).setLanguage(this.inputFormat.language).setSelectionFlags(this.inputFormat.selectionFlags).setRoleFlags(this.inputFormat.roleFlags).build(), 0, getChannelMapping(this.decoder));
        this.audioTrackNeedsConfigure = false;
    L28:
        if (this.audioSink.handleBuffer(this.outputBuffer.data, this.outputBuffer.timeUs, 1) == false) goto L31;
        this.decoderCounters.renderedOutputBufferCount++;
        this.outputBuffer.release();
        this.outputBuffer = null;
        return true;
    L31:
        return false;
    }

    private boolean feedInputBuffer() throws DecoderException, ExoPlaybackException {
        if (this.decoder != null) goto L5;
    L34:
        return false;
    L5:
        if (this.decoderReinitializationState == 2) goto L34;
        if (this.inputStreamEnded == true) goto L34;
        if (this.inputBuffer != null) goto L15;
        this.inputBuffer = (DecoderInputBuffer) this.decoder.dequeueInputBuffer();
        if (this.inputBuffer != null) goto L15;
        return false;
    L15:
        if (this.decoderReinitializationState != 1) goto L18;
        this.inputBuffer.setFlags(4);
        this.decoder.queueInputBuffer(this.inputBuffer);
        this.inputBuffer = null;
        this.decoderReinitializationState = 2;
        return false;
    L18:
        FormatHolder r0 = getFormatHolder();
        switch(readSource(r0, this.inputBuffer, 0)) {
            case -5: goto L32;
            case -4: goto L24;
            case -3: goto L22;
            default: goto L21;
        };
    L22:
        return false;
    L32:
        onInputFormatChanged(r0);
        return true;
    L21:
        throw new IllegalStateException();
    L24:
        if (this.inputBuffer.isEndOfStream() == false) goto L28;
        this.inputStreamEnded = true;
        this.decoder.queueInputBuffer(this.inputBuffer);
        this.inputBuffer = null;
        return false;
    L28:
        if (this.firstStreamSampleRead == true) goto L30;
        this.firstStreamSampleRead = true;
        this.inputBuffer.addFlag(C0180C.BUFFER_FLAG_FIRST_SAMPLE);
    L30:
        this.inputBuffer.flip();
        this.inputBuffer.format = this.inputFormat;
        this.decoder.queueInputBuffer(this.inputBuffer);
        this.decoderReceivedBuffers = true;
        this.decoderCounters.queuedInputBufferCount++;
        this.inputBuffer = null;
        return true;
    }

    private void flushDecoder() throws ExoPlaybackException {
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
        Decoder r0 = (Decoder) Assertions.checkNotNull(this.decoder);
        r0.flush();
        r0.setOutputStartTimeUs(getLastResetPositionUs());
        this.decoderReceivedBuffers = false;
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
        TraceUtil.beginSection("createAudioDecoder");     // Catch: OutOfMemoryError -> L18 DecoderException -> L21
        this.decoder = (T) createDecoder(this.inputFormat, r1);     // Catch: OutOfMemoryError -> L18 DecoderException -> L21
        this.decoder.setOutputStartTimeUs(getLastResetPositionUs());     // Catch: OutOfMemoryError -> L18 DecoderException -> L21
        TraceUtil.endSection();     // Catch: OutOfMemoryError -> L18 DecoderException -> L21
        long r5 = SystemClock.elapsedRealtime();     // Catch: OutOfMemoryError -> L18 DecoderException -> L21
        this.eventDispatcher.decoderInitialized(this.decoder.getName(), r5, r5 - r3);     // Catch: OutOfMemoryError -> L18 DecoderException -> L21
        this.decoderCounters.decoderInitCount++;     // Catch: OutOfMemoryError -> L18 DecoderException -> L21
        return;
    L21:
        e = move-exception;
        Log.m45e(TAG, "Audio codec error", e);
        this.eventDispatcher.audioCodecError(e);
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

    private void onInputFormatChanged(FormatHolder r8) throws ExoPlaybackException {
        Format r4 = (Format) Assertions.checkNotNull(r8.format);
        setSourceDrmSession(r8.drmSession);
        Format r3 = this.inputFormat;
        this.inputFormat = r4;
        this.encoderDelay = r4.encoderDelay;
        this.encoderPadding = r4.encoderPadding;
        if (this.decoder != null) goto L7;
        maybeInitDecoder();
        this.eventDispatcher.inputFormatChanged(this.inputFormat, null);
        return;
    L7:
        if (this.sourceDrmSession == this.decoderDrmSession) goto L9;
        DecoderReuseEvaluation r1 = new DecoderReuseEvaluation(this.decoder.getName(), r3, r4, 0, 128);
    L11:
        if (r1.result == 0) goto L13;
    L16:
        this.eventDispatcher.inputFormatChanged(this.inputFormat, r1);
        return;
    L13:
        if (this.decoderReceivedBuffers == false) goto L15;
        this.decoderReinitializationState = 1;
        goto L16
    L15:
        releaseDecoder();
        maybeInitDecoder();
        this.audioTrackNeedsConfigure = true;
        goto L16
    L9:
        r1 = canReuseDecoder(this.decoder.getName(), r3, r4);
        goto L11
    }

    private void processEndOfStream() throws AudioSink.WriteException {
        this.outputStreamEnded = true;
        this.audioSink.playToEndOfStream();
    }

    private void processFirstSampleOfStream() {
        this.audioSink.handleDiscontinuity();
        if (this.pendingOutputStreamOffsetCount == 0) goto L6;
        setOutputStreamOffsetUs(this.pendingOutputStreamOffsetsUs[0]);
        this.pendingOutputStreamOffsetCount--;
        System.arraycopy(this.pendingOutputStreamOffsetsUs, 1, this.pendingOutputStreamOffsetsUs, 0, this.pendingOutputStreamOffsetCount);
        return;
    }

    private void releaseDecoder() {
        this.inputBuffer = null;
        this.outputBuffer = null;
        this.decoderReinitializationState = 0;
        this.decoderReceivedBuffers = false;
        if (this.decoder == null) goto L5;
        this.decoderCounters.decoderReleaseCount++;
        this.decoder.release();
        this.eventDispatcher.decoderReleased(this.decoder.getName());
        this.decoder = null;
    L5:
        setDecoderDrmSession(null);
    }

    private void setDecoderDrmSession(DrmSession r2) {
        DrmSession.CC.replaceSession(this.decoderDrmSession, r2);
        this.decoderDrmSession = r2;
    }

    private void setOutputStreamOffsetUs(long r3) {
        this.outputStreamOffsetUs = r3;
        if (r3 == C0180C.TIME_UNSET) goto L6;
        this.audioSink.setOutputStreamOffsetUs(r3);
        return;
    }

    private void setSourceDrmSession(DrmSession r2) {
        DrmSession.CC.replaceSession(this.sourceDrmSession, r2);
        this.sourceDrmSession = r2;
    }

    private void updateCurrentPosition() {
        long r0 = this.audioSink.getCurrentPositionUs(isEnded());
        if (r0 != Long.MIN_VALUE) goto L5;
        return;
    L5:
        if (this.allowPositionDiscontinuity == false) goto L7;
        long r2 = r0;
    L8:
        this.currentPositionUs = r2;
        this.allowPositionDiscontinuity = false;
        return;
    L7:
        r2 = Math.max(this.currentPositionUs, r0);
        goto L8
    }

    protected DecoderReuseEvaluation canReuseDecoder(String r7, Format r8, Format r9) {
        return new DecoderReuseEvaluation(r7, r8, r9, 0, 1);
    }

    protected abstract T createDecoder(Format r1, CryptoConfig r2) throws DecoderException;

    protected int[] getChannelMapping(T r2) {
        return null;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.Renderer
    public MediaClock getMediaClock() {
        return this;
    }

    protected abstract Format getOutputFormat(T r1);

    @Override // androidx.media3.exoplayer.MediaClock
    public PlaybackParameters getPlaybackParameters() {
        return this.audioSink.getPlaybackParameters();
    }

    @Override // androidx.media3.exoplayer.MediaClock
    public long getPositionUs() {
        if (getState() != 2) goto L6;
        updateCurrentPosition();
    L6:
        return this.currentPositionUs;
    }

    protected final int getSinkFormatSupport(Format r2) {
        return this.audioSink.getFormatSupport(r2);
    }

    @Override // androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.PlayerMessage.Target
    public void handleMessage(int r3, Object r4) throws ExoPlaybackException {
        switch(r3) {
            case 2: goto L11;
            case 3: goto L10;
            case 4: goto L3;
            case 5: goto L3;
            case 6: goto L9;
            case 7: goto L3;
            case 8: goto L3;
            case 9: goto L8;
            case 10: goto L7;
            case 11: goto L3;
            case 12: goto L5;
            default: goto L3;
        };
    L3:
        super.handleMessage(r3, r4);
        return;
    L7:
        this.audioSink.setAudioSessionId(((Integer) r4).intValue());
        return;
    L8:
        this.audioSink.setSkipSilenceEnabled(((Boolean) r4).booleanValue());
        return;
    L9:
        AudioSink r1 = this.audioSink;
        r1.setAuxEffectInfo((AuxEffectInfo) r4);
        return;
    L10:
        AudioSink r2 = this.audioSink;
        r2.setAudioAttributes((AudioAttributes) r4);
        return;
    L11:
        this.audioSink.setVolume(((Float) r4).floatValue());
        return;
    L5:
        if (Util.SDK_INT < 23) goto L14;
        Api23.setAudioSinkPreferredDevice(this.audioSink, r4);
        return;
    }

    @Override // androidx.media3.exoplayer.MediaClock
    public boolean hasSkippedSilenceSinceLastCall() {
        boolean r0 = this.hasPendingReportedSkippedSilence;
        this.hasPendingReportedSkippedSilence = false;
        return r0;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public boolean isEnded() {
        if (this.outputStreamEnded == true) goto L5;
    L7:
        return false;
    L5:
        if (this.audioSink.isEnded() == false) goto L7;
        return true;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public boolean isReady() {
        if (this.audioSink.hasPendingData() == false) goto L5;
    L12:
        return true;
    L5:
        if (this.inputFormat != null) goto L7;
    L11:
        return false;
    L7:
        if (isSourceReady() == true) goto L12;
        if (this.outputBuffer == null) goto L11;
        goto L11
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onDisabled() {
        this.inputFormat = null;
        this.audioTrackNeedsConfigure = true;
        setOutputStreamOffsetUs(C0180C.TIME_UNSET);
        this.hasPendingReportedSkippedSilence = false;
        setSourceDrmSession(null);     // Catch: Throwable -> L6
        releaseDecoder();     // Catch: Throwable -> L6
        this.audioSink.reset();     // Catch: Throwable -> L6
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
        if (getConfiguration().tunneling == false) goto L5;
        this.audioSink.enableTunnelingV21();
    L6:
        this.audioSink.setPlayerId(getPlayerId());
        this.audioSink.setClock(getClock());
        return;
    L5:
        this.audioSink.disableTunneling();
        goto L6
    }

    protected void onPositionDiscontinuity() {
        this.allowPositionDiscontinuity = true;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onPositionReset(long r3, boolean r5) throws ExoPlaybackException {
        this.audioSink.flush();
        this.currentPositionUs = r3;
        this.hasPendingReportedSkippedSilence = false;
        this.allowPositionDiscontinuity = true;
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        if (this.decoder == null) goto L6;
        flushDecoder();
        return;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onStarted() {
        this.audioSink.play();
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onStopped() {
        updateCurrentPosition();
        this.audioSink.pause();
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onStreamChanged(Format[] r6, long r7, long r9, MediaSource.MediaPeriodId r11) throws ExoPlaybackException {
        super.onStreamChanged(r6, r7, r9, r11);
        this.firstStreamSampleRead = false;
        if (this.outputStreamOffsetUs != C0180C.TIME_UNSET) goto L6;
        setOutputStreamOffsetUs(r9);
        return;
    L6:
        if (this.pendingOutputStreamOffsetCount != this.pendingOutputStreamOffsetsUs.length) goto L8;
        Log.m48w(TAG, "Too many stream changes, so dropping offset: " + this.pendingOutputStreamOffsetsUs[this.pendingOutputStreamOffsetCount - 1]);
    L9:
        this.pendingOutputStreamOffsetsUs[this.pendingOutputStreamOffsetCount - 1] = r9;
        return;
    L8:
        this.pendingOutputStreamOffsetCount++;
        goto L9
    }

    @Override // androidx.media3.exoplayer.Renderer
    public void render(long r6, long r8) throws ExoPlaybackException {
        if (this.outputStreamEnded == false) goto L10;
        this.audioSink.playToEndOfStream();     // Catch: AudioSink.WriteException -> L6
        return;
    L6:
        e = move-exception;
        throw createRendererException(e, e.format, e.isRecoverable, PlaybackException.ERROR_CODE_AUDIO_TRACK_WRITE_FAILED);
    L10:
        if (this.inputFormat != null) goto L23;
        FormatHolder r0 = getFormatHolder();
        this.flagsOnlyBuffer.clear();
        int r2 = readSource(r0, this.flagsOnlyBuffer, 2);
        if (r2 != (-5)) goto L15;
        onInputFormatChanged(r0);
        goto L23
    L15:
        if (r2 != (-4)) goto L22;
        Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
        this.inputStreamEnded = true;
        processEndOfStream();     // Catch: AudioSink.WriteException -> L19
        return;
    L19:
        e = move-exception;
        throw createRendererException(e, null, PlaybackException.ERROR_CODE_AUDIO_TRACK_WRITE_FAILED);
    L22:
        return;
    L23:
        maybeInitDecoder();
        if (this.decoder != null) goto L52;
        return;
    L52:
        TraceUtil.beginSection("drainAndFeed");     // Catch: AudioSink.WriteException -> L35 AudioSink.InitializationException -> L38 AudioSink.ConfigurationException -> L41 DecoderException -> L44
    L28:
        if (drainOutputBuffer() == true) goto L28;
    L31:
        if (feedInputBuffer() == true) goto L31;
        TraceUtil.endSection();     // Catch: AudioSink.WriteException -> L35 AudioSink.InitializationException -> L38 AudioSink.ConfigurationException -> L41 DecoderException -> L44
        this.decoderCounters.ensureUpdated();
        return;
    L44:
        e = move-exception;
        Log.m45e(TAG, "Audio codec error", e);
        this.eventDispatcher.audioCodecError(e);
        throw createRendererException(e, this.inputFormat, PlaybackException.ERROR_CODE_DECODING_FAILED);
    L41:
        e = move-exception;
        throw createRendererException(e, e.format, PlaybackException.ERROR_CODE_AUDIO_TRACK_INIT_FAILED);
    L38:
        e = move-exception;
        throw createRendererException(e, e.format, e.isRecoverable, PlaybackException.ERROR_CODE_AUDIO_TRACK_INIT_FAILED);
    L35:
        e = move-exception;
        throw createRendererException(e, e.format, e.isRecoverable, PlaybackException.ERROR_CODE_AUDIO_TRACK_WRITE_FAILED);
    }

    @Override // androidx.media3.exoplayer.MediaClock
    public void setPlaybackParameters(PlaybackParameters r2) {
        this.audioSink.setPlaybackParameters(r2);
    }

    protected final boolean sinkSupportsFormat(Format r2) {
        return this.audioSink.supportsFormat(r2);
    }

    @Override // androidx.media3.exoplayer.RendererCapabilities
    public final int supportsFormat(Format r4) {
        if (MimeTypes.isAudio(r4.sampleMimeType) == false) goto L5;
        int r0 = supportsFormatInternal(r4);
        if (r0 > 2) goto L11;
        return RendererCapabilities.CC.create(r0);
    L11:
        return RendererCapabilities.CC.create(r0, 8, 32);
    L5:
        return RendererCapabilities.CC.create(0);
    }

    protected abstract int supportsFormatInternal(Format r1);
}
