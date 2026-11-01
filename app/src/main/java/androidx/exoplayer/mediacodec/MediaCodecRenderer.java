package androidx.media3.exoplayer.mediacodec;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaCryptoException;
import android.media.MediaFormat;
import android.media.metrics.LogSessionId;
import android.os.Bundle;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.TimedValueQueue;
import androidx.media3.common.util.TraceUtil;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.CryptoConfig;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.BaseRenderer;
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.DecoderReuseEvaluation;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.audio.OggOpusAudioPacketizer;
import androidx.media3.exoplayer.drm.DrmSession;
import androidx.media3.exoplayer.drm.FrameworkCryptoConfig;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.extractor.OpusUtil;
import com.arthenica.ffmpegkit.StreamInformation;
import com.google.common.base.Ascii;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public abstract class MediaCodecRenderer extends BaseRenderer {
    private static final byte[] ADAPTATION_WORKAROUND_BUFFER = null;
    private static final int ADAPTATION_WORKAROUND_MODE_ALWAYS = 2;
    private static final int ADAPTATION_WORKAROUND_MODE_NEVER = 0;
    private static final int ADAPTATION_WORKAROUND_MODE_SAME_RESOLUTION = 1;
    private static final int ADAPTATION_WORKAROUND_SLICE_WIDTH_HEIGHT = 32;
    protected static final float CODEC_OPERATING_RATE_UNSET = -1.0f;
    private static final int DRAIN_ACTION_FLUSH = 1;
    private static final int DRAIN_ACTION_FLUSH_AND_UPDATE_DRM_SESSION = 2;
    private static final int DRAIN_ACTION_NONE = 0;
    private static final int DRAIN_ACTION_REINITIALIZE = 3;
    private static final int DRAIN_STATE_NONE = 0;
    private static final int DRAIN_STATE_SIGNAL_END_OF_STREAM = 1;
    private static final int DRAIN_STATE_WAIT_END_OF_STREAM = 2;
    private static final long MAX_CODEC_HOTSWAP_TIME_MS = 1000;
    private static final int RECONFIGURATION_STATE_NONE = 0;
    private static final int RECONFIGURATION_STATE_QUEUE_PENDING = 2;
    private static final int RECONFIGURATION_STATE_WRITE_PENDING = 1;
    private static final String TAG = "MediaCodecRenderer";
    private final float assumedMinimumCodecOperatingRate;
    private ArrayDeque<MediaCodecInfo> availableCodecInfos;
    private final DecoderInputBuffer buffer;
    private final BatchBuffer bypassBatchBuffer;
    private boolean bypassDrainAndReinitialize;
    private boolean bypassEnabled;
    private final DecoderInputBuffer bypassSampleBuffer;
    private boolean bypassSampleBufferPending;
    private MediaCodecAdapter codec;
    private int codecAdaptationWorkaroundMode;
    private final MediaCodecAdapter.Factory codecAdapterFactory;
    private int codecDrainAction;
    private int codecDrainState;
    private DrmSession codecDrmSession;
    private boolean codecHasOutputMediaFormat;
    private long codecHotswapDeadlineMs;
    private MediaCodecInfo codecInfo;
    private Format codecInputFormat;
    private boolean codecNeedsAdaptationWorkaroundBuffer;
    private boolean codecNeedsEosFlushWorkaround;
    private boolean codecNeedsEosOutputExceptionWorkaround;
    private boolean codecNeedsEosPropagation;
    private boolean codecNeedsSosFlushWorkaround;
    private float codecOperatingRate;
    private MediaFormat codecOutputMediaFormat;
    private boolean codecOutputMediaFormatChanged;
    private boolean codecReceivedBuffers;
    private boolean codecReceivedEos;
    private int codecReconfigurationState;
    private boolean codecReconfigured;
    private boolean codecRegisteredOnBufferAvailableListener;
    private float currentPlaybackSpeed;
    protected DecoderCounters decoderCounters;
    private final boolean enableDecoderFallback;
    private boolean experimentalEnableProcessedStreamChangedAtStart;
    private Format inputFormat;
    private int inputIndex;
    private boolean inputStreamEnded;
    private boolean isDecodeOnlyOutputBuffer;
    private boolean isLastOutputBuffer;
    private long largestQueuedPresentationTimeUs;
    private long lastBufferInStreamPresentationTimeUs;
    private long lastOutputBufferProcessedRealtimeMs;
    private long lastProcessedOutputBufferTimeUs;
    private final MediaCodecSelector mediaCodecSelector;
    private MediaCrypto mediaCrypto;
    private boolean needToNotifyOutputFormatChangeAfterStreamChange;
    private final DecoderInputBuffer noDataBuffer;
    private final OggOpusAudioPacketizer oggOpusAudioPacketizer;
    private ByteBuffer outputBuffer;
    private final MediaCodec.BufferInfo outputBufferInfo;
    private Format outputFormat;
    private int outputIndex;
    private boolean outputStreamEnded;
    private OutputStreamInfo outputStreamInfo;
    private boolean pendingOutputEndOfStream;
    private final ArrayDeque<OutputStreamInfo> pendingOutputStreamChanges;
    private ExoPlaybackException pendingPlaybackException;
    private DecoderInitializationException preferredDecoderInitializationException;
    private long renderTimeLimitMs;
    private boolean shouldSkipAdaptationWorkaroundOutputBuffer;
    private DrmSession sourceDrmSession;
    private float targetPlaybackSpeed;
    private boolean waitingForFirstSampleInFormat;
    private Renderer.WakeupListener wakeupListener;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.mediacodec.MediaCodecRenderer$1 */
    static /* synthetic */ class C03091 {
    }

    private static final class Api31 {
        private Api31() {
        }

        public static void setLogSessionIdToMediaCodecFormat(MediaCodecAdapter.Configuration r4, PlayerId r5) {
            LogSessionId r0 = r5.getLogSessionId();
            if (r0.equals(LogSessionId.LOG_SESSION_ID_NONE) == true) goto L6;
            r4.mediaFormat.setString("log-session-id", r0.getStringId());
            return;
        }
    }

    public static class DecoderInitializationException extends Exception {
        private static final int CUSTOM_ERROR_CODE_BASE = -50000;
        private static final int DECODER_QUERY_ERROR = -49998;
        private static final int NO_SUITABLE_DECODER_ERROR = -49999;
        public final MediaCodecInfo codecInfo;
        public final String diagnosticInfo;
        public final DecoderInitializationException fallbackDecoderInitializationException;
        public final String mimeType;
        public final boolean secureDecoderRequired;

        public DecoderInitializationException(Format r10, Throwable r11, boolean r12, int r13) {
            this("Decoder init failed: [" + r13 + "], " + r10, r11, r10.sampleMimeType, r12, null, buildCustomDiagnosticInfo(r13), null);
        }

        public DecoderInitializationException(Format r10, Throwable r11, boolean r12, MediaCodecInfo r13) {
            String r2 = "Decoder init failed: " + r13.name + ", " + r10;
            String r4 = r10.sampleMimeType;
            if ((r11 instanceof MediaCodec.CodecException) == false) goto L5;
            String r0 = ((MediaCodec.CodecException) r11).getDiagnosticInfo();
        L6:
            this(r2, r11, r4, r12, r13, r0, null);
            return;
        L5:
            r0 = null;
            goto L6
        }

        private DecoderInitializationException(String r1, Throwable r2, String r3, boolean r4, MediaCodecInfo r5, String r6, DecoderInitializationException r7) {
            super(r1, r2);
            this.mimeType = r3;
            this.secureDecoderRequired = r4;
            this.codecInfo = r5;
            this.diagnosticInfo = r6;
            this.fallbackDecoderInitializationException = r7;
        }

        static /* synthetic */ DecoderInitializationException access$000(DecoderInitializationException r1, DecoderInitializationException r2) {
            return r1.copyWithFallbackException(r2);
        }

        private static String buildCustomDiagnosticInfo(int r4) {
            if (r4 >= 0) goto L4;
            String r0 = "neg_";
        L6:
            return "androidx.media3.exoplayer.mediacodec.MediaCodecRenderer_" + r0 + Math.abs(r4);
        L4:
            r0 = "";
            goto L6
        }

        private DecoderInitializationException copyWithFallbackException(DecoderInitializationException r9) {
            return new DecoderInitializationException(getMessage(), getCause(), this.mimeType, this.secureDecoderRequired, this.codecInfo, this.diagnosticInfo, r9);
        }
    }

    private final class MediaCodecRendererCodecAdapterListener implements MediaCodecAdapter.OnBufferAvailableListener {
        final /* synthetic */ MediaCodecRenderer this$0;

        private MediaCodecRendererCodecAdapterListener(MediaCodecRenderer r1) {
            this.this$0 = r1;
        }

        /* synthetic */ MediaCodecRendererCodecAdapterListener(MediaCodecRenderer r1, C03091 r2) {
            this(r1);
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter.OnBufferAvailableListener
        public void onInputBufferAvailable() {
            if (MediaCodecRenderer.access$200(this.this$0) == null) goto L6;
            MediaCodecRenderer.access$200(this.this$0).onWakeup();
            return;
        }

        @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter.OnBufferAvailableListener
        public void onOutputBufferAvailable() {
            if (MediaCodecRenderer.access$200(this.this$0) == null) goto L6;
            MediaCodecRenderer.access$200(this.this$0).onWakeup();
            return;
        }
    }

    private static final class OutputStreamInfo {
        public static final OutputStreamInfo UNSET = null;
        public final TimedValueQueue<Format> formatQueue;
        public final long previousStreamLastBufferTimeUs;
        public final long startPositionUs;
        public final long streamOffsetUs;

        static {
            UNSET = new OutputStreamInfo(C0180C.TIME_UNSET, C0180C.TIME_UNSET, C0180C.TIME_UNSET);
        }

        public OutputStreamInfo(long r2, long r4, long r6) {
            this.previousStreamLastBufferTimeUs = r2;
            this.startPositionUs = r4;
            this.streamOffsetUs = r6;
            this.formatQueue = new TimedValueQueue();
        }
    }

    static {
        ADAPTATION_WORKAROUND_BUFFER = new byte[]{0, 0, 1, 103, 66, -64, Ascii.f92VT, -38, 37, -112, 0, 0, 1, 104, -50, Ascii.f88SI, 19, 32, 0, 0, 1, 101, -120, -124, Ascii.f79CR, -50, 113, Ascii.CAN, -96, 0, 47, -65, Ascii.f82FS, 49, -61, 39, 93, 120};
    }

    public MediaCodecRenderer(int r6, MediaCodecAdapter.Factory r7, MediaCodecSelector r8, boolean r9, float r10) {
        super(r6);
        this.codecAdapterFactory = r7;
        this.mediaCodecSelector = (MediaCodecSelector) Assertions.checkNotNull(r8);
        this.enableDecoderFallback = r9;
        this.assumedMinimumCodecOperatingRate = r10;
        this.noDataBuffer = DecoderInputBuffer.newNoDataInstance();
        this.buffer = new DecoderInputBuffer(0);
        this.bypassSampleBuffer = new DecoderInputBuffer(2);
        this.bypassBatchBuffer = new BatchBuffer();
        this.outputBufferInfo = new MediaCodec.BufferInfo();
        this.currentPlaybackSpeed = 1.0f;
        this.targetPlaybackSpeed = 1.0f;
        this.renderTimeLimitMs = C0180C.TIME_UNSET;
        this.pendingOutputStreamChanges = new ArrayDeque();
        this.outputStreamInfo = OutputStreamInfo.UNSET;
        this.bypassBatchBuffer.ensureSpaceForWrite(0);
        this.bypassBatchBuffer.data.order(ByteOrder.nativeOrder());
        this.oggOpusAudioPacketizer = new OggOpusAudioPacketizer();
        this.codecOperatingRate = CODEC_OPERATING_RATE_UNSET;
        this.codecAdaptationWorkaroundMode = 0;
        this.codecReconfigurationState = 0;
        this.inputIndex = -1;
        this.outputIndex = -1;
        this.codecHotswapDeadlineMs = C0180C.TIME_UNSET;
        this.largestQueuedPresentationTimeUs = C0180C.TIME_UNSET;
        this.lastBufferInStreamPresentationTimeUs = C0180C.TIME_UNSET;
        this.lastProcessedOutputBufferTimeUs = C0180C.TIME_UNSET;
        this.lastOutputBufferProcessedRealtimeMs = C0180C.TIME_UNSET;
        this.codecDrainState = 0;
        this.codecDrainAction = 0;
        this.decoderCounters = new DecoderCounters();
    }

    static /* synthetic */ Renderer.WakeupListener access$200(MediaCodecRenderer r1) {
        return r1.wakeupListener;
    }

    private void bypassRead() throws ExoPlaybackException {
        Assertions.checkState(!this.inputStreamEnded);
        FormatHolder r0 = getFormatHolder();
        this.bypassSampleBuffer.clear();
    L3:
        this.bypassSampleBuffer.clear();
        switch(readSource(r0, this.bypassSampleBuffer, 0)) {
            case -5: goto L46;
            case -4: goto L12;
            case -3: goto L8;
            default: goto L6;
        };
    L12:
        if (this.bypassSampleBuffer.isEndOfStream() == true) goto L13;
        this.largestQueuedPresentationTimeUs = Math.max(this.largestQueuedPresentationTimeUs, this.bypassSampleBuffer.timeUs);
        if (hasReadStreamToEnd() == false) goto L18;
    L19:
        this.lastBufferInStreamPresentationTimeUs = this.largestQueuedPresentationTimeUs;
    L21:
        if (this.waitingForFirstSampleInFormat == false) goto L28;
        this.outputFormat = (Format) Assertions.checkNotNull(this.inputFormat);
        if (Objects.equals(this.outputFormat.sampleMimeType, MimeTypes.AUDIO_OPUS) == true) goto L25;
    L27:
        onOutputFormatChanged(this.outputFormat, null);
        this.waitingForFirstSampleInFormat = false;
        goto L28
    L25:
        if (this.outputFormat.initializationData.isEmpty() == true) goto L27;
        this.outputFormat = ((Format) Assertions.checkNotNull(this.outputFormat)).buildUpon().setEncoderDelay(OpusUtil.getPreSkipSamples(this.outputFormat.initializationData.get(0))).build();
    L28:
        this.bypassSampleBuffer.flip();
        if (this.outputFormat == null) goto L39;
        if (Objects.equals(this.outputFormat.sampleMimeType, MimeTypes.AUDIO_OPUS) == false) goto L39;
        if (this.bypassSampleBuffer.hasSupplementalData() == false) goto L36;
        this.bypassSampleBuffer.format = this.outputFormat;
        handleInputBufferSupplementalData(this.bypassSampleBuffer);
    L36:
        if (OpusUtil.needToDecodeOpusFrame(getLastResetPositionUs(), this.bypassSampleBuffer.timeUs) == false) goto L39;
        this.oggOpusAudioPacketizer.packetize(this.bypassSampleBuffer, ((Format) Assertions.checkNotNull(this.outputFormat)).initializationData);
    L39:
        if (haveBypassBatchBufferAndNewSampleSameDecodeOnlyState() == false) goto L44;
        if (this.bypassBatchBuffer.append(this.bypassSampleBuffer) == true) goto L3;
    L44:
        this.bypassSampleBufferPending = true;
        return;
    L18:
        if (this.buffer.isLastSample() == false) goto L21;
    L13:
        this.inputStreamEnded = true;
        this.lastBufferInStreamPresentationTimeUs = this.largestQueuedPresentationTimeUs;
        return;
    L6:
        throw new IllegalStateException();
    L8:
        if (hasReadStreamToEnd() == false) goto L55;
        this.lastBufferInStreamPresentationTimeUs = this.largestQueuedPresentationTimeUs;
        return;
    L55:
        return;
    L46:
        onInputFormatChanged(r0);
    }

    private boolean bypassRender(long r18, long r20) throws ExoPlaybackException {
        Assertions.checkState(!this.outputStreamEnded);
        if (this.bypassBatchBuffer.hasSamples() == false) goto L8;
        boolean r15 = false;
        if (processOutputBuffer(r18, r20, null, this.bypassBatchBuffer.data, this.outputIndex, 0, this.bypassBatchBuffer.getSampleCount(), this.bypassBatchBuffer.getFirstSampleTimeUs(), isDecodeOnly(getLastResetPositionUs(), this.bypassBatchBuffer.getLastSampleTimeUs()), this.bypassBatchBuffer.isEndOfStream(), (Format) Assertions.checkNotNull(this.outputFormat)) == false) goto L7;
        onProcessedOutputBuffer(this.bypassBatchBuffer.getLastSampleTimeUs());
        this.bypassBatchBuffer.clear();
    L10:
        if (this.inputStreamEnded == false) goto L14;
        this.outputStreamEnded = true;
        return r15;
    L14:
        if (this.bypassSampleBufferPending == false) goto L17;
        Assertions.checkState(this.bypassBatchBuffer.append(this.bypassSampleBuffer));
        this.bypassSampleBufferPending = r15;
    L17:
        if (this.bypassDrainAndReinitialize == true) goto L19;
        boolean r16 = true;
    L26:
        bypassRead();
        if (this.bypassBatchBuffer.hasSamples() == false) goto L30;
        this.bypassBatchBuffer.flip();
    L30:
        if (this.bypassBatchBuffer.hasSamples() == true) goto L36;
        if (this.inputStreamEnded == true) goto L36;
        if (this.bypassDrainAndReinitialize == true) goto L36;
        return r15;
    L36:
        return r16;
    L19:
        if (this.bypassBatchBuffer.hasSamples() == false) goto L22;
        return true;
    L22:
        r16 = true;
        disableBypass();
        this.bypassDrainAndReinitialize = r15;
        maybeInitCodecOrBypass();
        if (this.bypassEnabled == true) goto L26;
        return r15;
    L7:
        return false;
    L8:
        r15 = false;
        goto L10
    }

    private int codecAdaptationWorkaroundMode(String r3) {
        if (Util.SDK_INT > 25) goto L17;
        if ("OMX.Exynos.avc.dec.secure".equals(r3) == false) goto L17;
        if (Util.MODEL.startsWith("SM-T585") == false) goto L9;
        return 2;
    L9:
        if (Util.MODEL.startsWith("SM-A510") == false) goto L11;
        return 2;
    L11:
        if (Util.MODEL.startsWith("SM-A520") == false) goto L13;
        return 2;
    L13:
        if (Util.MODEL.startsWith("SM-J700") == false) goto L17;
        return 2;
    L17:
        if (Util.SDK_INT < 24) goto L19;
        return 0;
    L19:
        if ("OMX.Nvidia.h264.decode".equals(r3) == true) goto L23;
        if ("OMX.Nvidia.h264.decode.secure".equals(r3) == true) goto L23;
        return 0;
    L23:
        if ("flounder".equals(Util.DEVICE) == false) goto L25;
        return 1;
    L25:
        if ("flounder_lte".equals(Util.DEVICE) == false) goto L27;
        return 1;
    L27:
        if ("grouper".equals(Util.DEVICE) == false) goto L29;
        return 1;
    L29:
        if ("tilapia".equals(Util.DEVICE) == false) goto L41;
        return 1;
    L41:
        return 0;
    }

    private static boolean codecNeedsEosFlushWorkaround(String r2) {
        if (Util.SDK_INT <= 23) goto L5;
    L7:
        return false;
    L5:
        if ("OMX.google.vorbis.decoder".equals(r2) == false) goto L7;
        return true;
    }

    private static boolean codecNeedsEosOutputExceptionWorkaround(String r2) {
        if (Util.SDK_INT == 21) goto L5;
    L7:
        return false;
    L5:
        if ("OMX.google.aac.decoder".equals(r2) == false) goto L7;
        return true;
    }

    private static boolean codecNeedsEosPropagationWorkaround(MediaCodecInfo r3) {
        String r0 = r3.name;
        if (Util.SDK_INT > 25) goto L7;
        if ("OMX.rk.video_decoder.avc".equals(r0) == false) goto L7;
    L26:
        return true;
    L7:
        if (Util.SDK_INT > 29) goto L21;
        if ("OMX.broadcom.video_decoder.tunnel".equals(r0) == true) goto L26;
        if ("OMX.broadcom.video_decoder.tunnel.secure".equals(r0) == true) goto L26;
        if ("OMX.bcm.vdec.avc.tunnel".equals(r0) == true) goto L26;
        if ("OMX.bcm.vdec.avc.tunnel.secure".equals(r0) == true) goto L26;
        if ("OMX.bcm.vdec.hevc.tunnel".equals(r0) == true) goto L26;
        if ("OMX.bcm.vdec.hevc.tunnel.secure".equals(r0) == true) goto L26;
    L21:
        if ("Amazon".equals(Util.MANUFACTURER) == true) goto L23;
    L27:
        return false;
    L23:
        if ("AFTS".equals(Util.MODEL) == false) goto L27;
        if (r3.secure == false) goto L27;
        goto L26
    }

    private static boolean codecNeedsSosFlushWorkaround(String r2) {
        if (Util.SDK_INT == 29) goto L5;
    L7:
        return false;
    L5:
        if ("c2.android.aac.decoder".equals(r2) == false) goto L7;
        return true;
    }

    private void disableBypass() {
        this.bypassDrainAndReinitialize = false;
        this.bypassBatchBuffer.clear();
        this.bypassSampleBuffer.clear();
        this.bypassSampleBufferPending = false;
        this.bypassEnabled = false;
        this.oggOpusAudioPacketizer.reset();
    }

    private boolean drainAndFlushCodec() {
        if (this.codecReceivedBuffers == false) goto L9;
        this.codecDrainState = 1;
        if (this.codecNeedsEosFlushWorkaround == false) goto L8;
        this.codecDrainAction = 3;
        return false;
    L8:
        this.codecDrainAction = 1;
    L9:
        return true;
    }

    private void drainAndReinitializeCodec() throws ExoPlaybackException {
        if (this.codecReceivedBuffers == false) goto L5;
        this.codecDrainState = 1;
        this.codecDrainAction = 3;
        return;
    L5:
        reinitializeCodec();
    }

    private boolean drainAndUpdateCodecDrmSessionV23() throws ExoPlaybackException {
        if (this.codecReceivedBuffers == false) goto L9;
        this.codecDrainState = 1;
        if (this.codecNeedsEosFlushWorkaround == false) goto L8;
        this.codecDrainAction = 3;
        return false;
    L8:
        this.codecDrainAction = 2;
    L10:
        return true;
    L9:
        updateDrmSessionV23();
        goto L10
    }

    private boolean drainOutputBuffer(long r19, long r21) throws ExoPlaybackException {
        MediaCodecAdapter r6 = (MediaCodecAdapter) Assertions.checkNotNull(this.codec);
        if (hasOutputBuffer() == true) goto L60;
        if (this.codecNeedsEosOutputExceptionWorkaround == true) goto L7;
    L15:
        int r0 = r6.dequeueOutputBufferIndex(this.outputBufferInfo);
    L17:
        if (r0 >= 0) goto L36;
        if (r0 != (-2)) goto L23;
        processOutputMediaFormatChanged();
        return true;
    L23:
        if (this.codecNeedsEosPropagation == false) goto L30;
        if (this.inputStreamEnded == false) goto L27;
    L28:
        processEndOfStream();
        goto L30
    L27:
        if (this.codecDrainState == 2) goto L28;
    L30:
        if (this.lastOutputBufferProcessedRealtimeMs != C0180C.TIME_UNSET) goto L32;
    L34:
        return false;
    L32:
        if ((this.lastOutputBufferProcessedRealtimeMs + 100) >= getClock().currentTimeMillis()) goto L34;
        processEndOfStream();
        goto L34
    L36:
        if (this.shouldSkipAdaptationWorkaroundOutputBuffer == false) goto L40;
        this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
        r6.releaseOutputBuffer(r0, false);
        return true;
    L40:
        if (this.outputBufferInfo.size == 0) goto L42;
    L45:
        this.outputIndex = r0;
        this.outputBuffer = r6.getOutputBuffer(r0);
        if (this.outputBuffer == null) goto L49;
        this.outputBuffer.position(this.outputBufferInfo.offset);
        this.outputBuffer.limit(this.outputBufferInfo.offset + this.outputBufferInfo.size);
    L49:
        if (this.outputBufferInfo.presentationTimeUs >= getLastResetPositionUs()) goto L51;
        boolean r5 = true;
    L52:
        this.isDecodeOnlyOutputBuffer = r5;
        if (this.lastBufferInStreamPresentationTimeUs != C0180C.TIME_UNSET) goto L55;
    L57:
        boolean r3 = false;
    L58:
        this.isLastOutputBuffer = r3;
        updateOutputFormatForTime(this.outputBufferInfo.presentationTimeUs);
        goto L60
    L55:
        if (this.lastBufferInStreamPresentationTimeUs > this.outputBufferInfo.presentationTimeUs) goto L57;
        r3 = true;
        goto L58
    L51:
        r5 = false;
        goto L52
    L42:
        if ((this.outputBufferInfo.flags & 4) == 0) goto L45;
        processEndOfStream();
        return false;
    L7:
        if (this.codecReceivedEos == false) goto L15;
        r0 = r6.dequeueOutputBufferIndex(this.outputBufferInfo);     // Catch: IllegalStateException -> L10
    L11:
        processEndOfStream();
        if (this.outputStreamEnded == false) goto L14;
        releaseCodec();
    L14:
        return false;
    L60:
        if (this.codecNeedsEosOutputExceptionWorkaround == true) goto L62;
    L75:
        boolean r17 = false;
        boolean r1 = processOutputBuffer(r19, r21, r6, this.outputBuffer, this.outputIndex, this.outputBufferInfo.flags, 1, this.outputBufferInfo.presentationTimeUs, this.isDecodeOnlyOutputBuffer, this.isLastOutputBuffer, (Format) Assertions.checkNotNull(this.outputFormat));
    L76:
        if (r1 == false) goto L91;
        onProcessedOutputBuffer(this.outputBufferInfo.presentationTimeUs);
        if ((this.outputBufferInfo.flags & 4) == 0) goto L80;
        boolean r2 = true;
    L81:
        if (r2 == false) goto L83;
    L87:
        resetOutputBuffer();
        if (r2 == true) goto L90;
        return true;
    L90:
        processEndOfStream();
        goto L91
    L83:
        if (this.codecReceivedEos == false) goto L87;
        if (this.isLastOutputBuffer == false) goto L87;
        this.lastOutputBufferProcessedRealtimeMs = getClock().currentTimeMillis();
        goto L87
    L80:
        r2 = r17;
    L91:
        return r17;
    L62:
        if (this.codecReceivedEos == false) goto L75;
        r17 = false;
        r1 = processOutputBuffer(r19, r21, r6, this.outputBuffer, this.outputIndex, this.outputBufferInfo.flags, 1, this.outputBufferInfo.presentationTimeUs, this.isDecodeOnlyOutputBuffer, this.isLastOutputBuffer, (Format) Assertions.checkNotNull(this.outputFormat));     // Catch: IllegalStateException -> L67
    L71:
        processEndOfStream();
        if (this.outputStreamEnded == false) goto L74;
        releaseCodec();
    L74:
        return r17;
    L70:
        r17 = false;
        goto L71
    }

    private boolean drmNeedsCodecReinitialization(MediaCodecInfo r7, Format r8, DrmSession r9, DrmSession r10) throws ExoPlaybackException {
        if (r9 != r10) goto L6;
        return false;
    L6:
        if (r10 == null) goto L46;
        if (r9 == null) goto L46;
        CryptoConfig r2 = r10.getCryptoConfig();
        if (r2 != null) goto L12;
        return true;
    L12:
        CryptoConfig r3 = r9.getCryptoConfig();
        if (r3 != null) goto L15;
    L45:
        return true;
    L15:
        if (r2.getClass().equals(r3.getClass()) == false) goto L45;
        if ((r2 instanceof FrameworkCryptoConfig) == true) goto L21;
        return false;
    L21:
        if (r10.getSchemeUuid().equals(r9.getSchemeUuid()) == true) goto L24;
        return true;
    L24:
        if (Util.SDK_INT >= 23) goto L27;
        return true;
    L27:
        if (C0180C.PLAYREADY_UUID.equals(r9.getSchemeUuid()) == false) goto L29;
    L44:
        return true;
    L29:
        if (C0180C.PLAYREADY_UUID.equals(r10.getSchemeUuid()) == true) goto L44;
        if (r7.secure == false) goto L34;
        return false;
    L34:
        if (r10.getState() == 2) goto L43;
        if (r10.getState() == 3) goto L40;
        if (r10.getState() == 4) goto L40;
        return false;
    L40:
        if (r10.requiresSecureDecoder((String) Assertions.checkNotNull(r8.sampleMimeType)) == true) goto L43;
        return false;
    L43:
        return true;
    L46:
        return true;
    }

    private boolean feedInputBuffer() throws ExoPlaybackException {
        if (this.codec != null) goto L5;
    L107:
        return false;
    L5:
        if (this.codecDrainState == 2) goto L107;
        if (this.inputStreamEnded == true) goto L107;
        if (this.codecDrainState == 0) goto L12;
    L14:
        MediaCodecAdapter r4 = (MediaCodecAdapter) Assertions.checkNotNull(this.codec);
        if (this.inputIndex >= 0) goto L21;
        this.inputIndex = r4.dequeueInputBufferIndex();
        if (this.inputIndex >= 0) goto L19;
        return false;
    L19:
        this.buffer.data = r4.getInputBuffer(this.inputIndex);
        this.buffer.clear();
    L21:
        if (this.codecDrainState != 1) goto L29;
        if (this.codecNeedsEosPropagation == true) goto L26;
        this.codecReceivedEos = true;
        r4.queueInputBuffer(this.inputIndex, 0, 0, 0, 4);
        resetInputBuffer();
    L26:
        this.codecDrainState = 2;
        return false;
    L29:
        if (this.codecNeedsAdaptationWorkaroundBuffer == false) goto L33;
        this.codecNeedsAdaptationWorkaroundBuffer = false;
        ((ByteBuffer) Assertions.checkNotNull(this.buffer.data)).put(ADAPTATION_WORKAROUND_BUFFER);
        r4.queueInputBuffer(this.inputIndex, 0, ADAPTATION_WORKAROUND_BUFFER.length, 0, 0);
        resetInputBuffer();
        this.codecReceivedBuffers = true;
        return true;
    L33:
        if (this.codecReconfigurationState != 1) goto L39;
        int r0 = 0;
    L36:
        if (r0 >= ((Format) Assertions.checkNotNull(this.codecInputFormat)).initializationData.size()) goto L38;
        ((ByteBuffer) Assertions.checkNotNull(this.buffer.data)).put(this.codecInputFormat.initializationData.get(r0));
        r0 = r0 + 1;
        goto L36
    L38:
        this.codecReconfigurationState = 2;
    L39:
        int r12 = ((ByteBuffer) Assertions.checkNotNull(this.buffer.data)).position();
        FormatHolder r13 = getFormatHolder();
        int r1 = readSource(r13, this.buffer, 0);     // Catch: DecoderInputBuffer.InsufficientCapacityException -> L104
        if (r1 != (-3)) goto L48;
        if (hasReadStreamToEnd() == false) goto L46;
        this.lastBufferInStreamPresentationTimeUs = this.largestQueuedPresentationTimeUs;
    L46:
        return false;
    L48:
        if (r1 != (-5)) goto L55;
        if (this.codecReconfigurationState != 2) goto L52;
        this.buffer.clear();
        this.codecReconfigurationState = 1;
    L52:
        onInputFormatChanged(r13);
        return true;
    L55:
        if (this.buffer.isEndOfStream() == false) goto L69;
        this.lastBufferInStreamPresentationTimeUs = this.largestQueuedPresentationTimeUs;
        if (this.codecReconfigurationState != 2) goto L59;
        this.buffer.clear();
        this.codecReconfigurationState = 1;
    L59:
        this.inputStreamEnded = true;
        if (this.codecReceivedBuffers == true) goto L64;
        processEndOfStream();
        return false;
    L64:
        if (this.codecNeedsEosPropagation == true) goto L67;
        this.codecReceivedEos = true;
        r4.queueInputBuffer(this.inputIndex, 0, 0, 0, 4);
        resetInputBuffer();
    L67:
        return false;
    L69:
        if (this.codecReceivedBuffers == true) goto L77;
        if (this.buffer.isKeyFrame() == true) goto L77;
        this.buffer.clear();
        if (this.codecReconfigurationState != 2) goto L75;
        this.codecReconfigurationState = 1;
    L75:
        return true;
    L77:
        if (shouldSkipDecoderInputBuffer(this.buffer) == false) goto L80;
        this.buffer.clear();
        this.decoderCounters.skippedInputBufferCount++;
        return true;
    L80:
        boolean r3 = this.buffer.isEncrypted();
        if (r3 == false) goto L83;
        this.buffer.cryptoInfo.increaseClearDataFirstSubSampleBy(r12);
    L83:
        long r5 = this.buffer.timeUs;
        if (this.waitingForFirstSampleInFormat == true) goto L86;
    L90:
        this.largestQueuedPresentationTimeUs = Math.max(this.largestQueuedPresentationTimeUs, r5);
        if (hasReadStreamToEnd() == false) goto L93;
    L94:
        this.lastBufferInStreamPresentationTimeUs = this.largestQueuedPresentationTimeUs;
    L95:
        this.buffer.flip();
        if (this.buffer.hasSupplementalData() == false) goto L98;
        handleInputBufferSupplementalData(this.buffer);
    L98:
        onQueueInputBuffer(this.buffer);
        int r20 = getCodecBufferFlags(this.buffer);
        if (r3 == false) goto L101;
        ((MediaCodecAdapter) Assertions.checkNotNull(r4)).queueSecureInputBuffer(this.inputIndex, 0, this.buffer.cryptoInfo, r5, r20);
    L102:
        resetInputBuffer();
        this.codecReceivedBuffers = true;
        this.codecReconfigurationState = 0;
        this.decoderCounters.queuedInputBufferCount++;
        return true;
    L101:
        ((MediaCodecAdapter) Assertions.checkNotNull(r4)).queueInputBuffer(this.inputIndex, 0, ((ByteBuffer) Assertions.checkNotNull(this.buffer.data)).limit(), r5, r20);
        goto L102
    L93:
        if (this.buffer.isLastSample() == false) goto L95;
    L86:
        if (this.pendingOutputStreamChanges.isEmpty() == true) goto L88;
        this.pendingOutputStreamChanges.peekLast().formatQueue.add(r5, (Format) Assertions.checkNotNull(this.inputFormat));
    L89:
        this.waitingForFirstSampleInFormat = false;
        goto L90
    L88:
        this.outputStreamInfo.formatQueue.add(r5, (Format) Assertions.checkNotNull(this.inputFormat));
    L104:
        e = move-exception;
        onCodecError(e);
        readSourceOmittingSampleData(0);
        flushCodec();
        return true;
    L12:
        if (shouldReinitCodec() == false) goto L14;
        drainAndReinitializeCodec();
        goto L14
    }

    private void flushCodec() {
        ((MediaCodecAdapter) Assertions.checkStateNotNull(this.codec)).flush();     // Catch: Throwable -> L5
        resetCodecStateForFlush();
        return;
    L5:
        th = move-exception;
        resetCodecStateForFlush();
        throw th;
    }

    private List<MediaCodecInfo> getAvailableCodecInfos(boolean r5) throws MediaCodecUtil.DecoderQueryException {
        Format r0 = (Format) Assertions.checkNotNull(this.inputFormat);
        List<MediaCodecInfo> r1 = getDecoderInfos(this.mediaCodecSelector, r0, r5);
        if (r1.isEmpty() == false) goto L8;
        if (r5 == false) goto L8;
        r1 = getDecoderInfos(this.mediaCodecSelector, r0, false);
        if (r1.isEmpty() == true) goto L8;
        Log.m48w(TAG, "Drm session requires secure decoder for " + r0.sampleMimeType + ", but no secure decoder available. Trying to proceed with " + r1 + ".");
    L8:
        return r1;
    }

    private boolean hasOutputBuffer() {
        if (this.outputIndex < 0) goto L5;
        return true;
    L5:
        return false;
    }

    private boolean haveBypassBatchBufferAndNewSampleSameDecodeOnlyState() {
        if (this.bypassBatchBuffer.hasSamples() == true) goto L5;
        return true;
    L5:
        long r2 = getLastResetPositionUs();
        if (isDecodeOnly(r2, this.bypassBatchBuffer.getLastSampleTimeUs()) == isDecodeOnly(r2, this.bypassSampleBuffer.timeUs)) goto L10;
        return false;
    L10:
        return true;
    }

    private void initBypass(Format r5) {
        disableBypass();
        String r0 = r5.sampleMimeType;
        if (MimeTypes.AUDIO_AAC.equals(r0) == false) goto L5;
    L9:
        this.bypassBatchBuffer.setMaxSampleCount(32);
    L10:
        this.bypassEnabled = true;
        return;
    L5:
        if (MimeTypes.AUDIO_MPEG.equals(r0) == true) goto L9;
        if (MimeTypes.AUDIO_OPUS.equals(r0) == true) goto L9;
        this.bypassBatchBuffer.setMaxSampleCount(1);
        goto L10
    }

    private void initCodec(MediaCodecInfo r18, MediaCrypto r19) throws Exception {
        Format r9 = (Format) Assertions.checkNotNull(this.inputFormat);
        String r2 = r18.name;
        if (Util.SDK_INT >= 23) goto L5;
        float r0 = CODEC_OPERATING_RATE_UNSET;
    L7:
        if (r0 > this.assumedMinimumCodecOperatingRate) goto L9;
        float r10 = -1.0f;
    L10:
        onReadyToInitializeCodec(r9);
        long r11 = getClock().elapsedRealtime();
        MediaCodecAdapter.Configuration r3 = getMediaCodecConfiguration(r18, r9, r19, r10);
        if (Util.SDK_INT < 31) goto L38;
        Api31.setLogSessionIdToMediaCodecFormat(r3, getPlayerId());
    L38:
        TraceUtil.beginSection("createCodec:" + r2);     // Catch: Throwable -> L35
        this.codec = this.codecAdapterFactory.createAdapter(r3);     // Catch: Throwable -> L35
        this.codecRegisteredOnBufferAvailableListener = this.codec.registerOnBufferAvailableListener(new MediaCodecRendererCodecAdapterListener(this, null));     // Catch: Throwable -> L35
        TraceUtil.endSection();
        long r4 = getClock().elapsedRealtime();
        boolean r7 = false;
        if (r18.isFormatSupported(r9) == true) goto L17;
        Log.m48w(TAG, Util.formatInvariant("Format exceeds selected codec's capabilities [%s, %s]", new Object[]{Format.toLogString(r9), r2}));
    L17:
        this.codecInfo = r18;
        this.codecOperatingRate = r10;
        this.codecInputFormat = r9;
        this.codecAdaptationWorkaroundMode = codecAdaptationWorkaroundMode(r2);
        this.codecNeedsSosFlushWorkaround = codecNeedsSosFlushWorkaround(r2);
        this.codecNeedsEosFlushWorkaround = codecNeedsEosFlushWorkaround(r2);
        this.codecNeedsEosOutputExceptionWorkaround = codecNeedsEosOutputExceptionWorkaround(r2);
        if (codecNeedsEosPropagationWorkaround(r18) == false) goto L20;
    L23:
        boolean r1 = true;
    L24:
        this.codecNeedsEosPropagation = r1;
        if (((MediaCodecAdapter) Assertions.checkNotNull(this.codec)).needsReconfiguration() == false) goto L31;
        this.codecReconfigured = true;
        this.codecReconfigurationState = 1;
        if (this.codecAdaptationWorkaroundMode == 0) goto L29;
        r7 = true;
    L29:
        this.codecNeedsAdaptationWorkaroundBuffer = r7;
    L31:
        if (getState() != 2) goto L33;
        this.codecHotswapDeadlineMs = getClock().elapsedRealtime() + 1000;
    L33:
        this.decoderCounters.decoderInitCount++;
        onCodecInitialized(r2, r3, r4, r4 - r11);
        return;
    L20:
        if (getCodecNeedsEosPropagation() == true) goto L23;
        r1 = false;
    L35:
        th = move-exception;
        TraceUtil.endSection();
        throw th;
    L9:
        r10 = r0;
        goto L10
    L5:
        r0 = getCodecOperatingRateV23(this.targetPlaybackSpeed, r9, getStreamFormats());
        goto L7
    }

    @RequiresNonNull({"this.codecDrmSession"})
    private boolean initMediaCryptoIfDrmSessionReady() throws ExoPlaybackException {
        if (this.mediaCrypto != null) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        DrmSession r1 = this.codecDrmSession;
        CryptoConfig r3 = r1.getCryptoConfig();
        if (FrameworkCryptoConfig.WORKAROUND_DEVICE_NEEDS_KEYS_TO_CONFIGURE_CODEC == true) goto L9;
    L17:
        if (r3 != null) goto L23;
        if (r1.getError() == null) goto L21;
        return true;
    L21:
        return false;
    L23:
        if ((r3 instanceof FrameworkCryptoConfig) == false) goto L30;
        FrameworkCryptoConfig r2 = (FrameworkCryptoConfig) r3;
        this.mediaCrypto = new MediaCrypto(r2.uuid, r2.sessionId);     // Catch: MediaCryptoException -> L27
    L27:
        e = move-exception;
        throw createRendererException(e, this.inputFormat, PlaybackException.ERROR_CODE_DRM_SYSTEM_ERROR);
    L30:
        return true;
    L9:
        if ((r3 instanceof FrameworkCryptoConfig) == false) goto L17;
        int r4 = r1.getState();
        if (r4 != 1) goto L13;
        DrmSession.DrmSessionException r5 = (DrmSession.DrmSessionException) Assertions.checkNotNull(r1.getError());
        throw createRendererException(r5, this.inputFormat, r5.errorCode);
    L13:
        if (r4 == 4) goto L17;
        return false;
    L5:
        r0 = false;
        goto L6
    }

    private boolean isDecodeOnly(long r3, long r5) {
        if (r5 < r3) goto L5;
    L11:
        return false;
    L5:
        if (this.outputFormat != null) goto L7;
    L10:
        return true;
    L7:
        if (Objects.equals(this.outputFormat.sampleMimeType, MimeTypes.AUDIO_OPUS) == false) goto L10;
        if (OpusUtil.needToDecodeOpusFrame(r3, r5) == true) goto L11;
        goto L10
    }

    private static boolean isMediaCodecException(IllegalStateException r5) {
        if ((r5 instanceof MediaCodec.CodecException) == false) goto L5;
        return true;
    L5:
        StackTraceElement[] r0 = r5.getStackTrace();
        if (r0.length <= 0) goto L11;
        if (r0[0].getClassName().equals("android.media.MediaCodec") == false) goto L11;
        return true;
    L11:
        return false;
    }

    private void maybeInitCodecWithFallback(MediaCrypto r8, boolean r9) throws DecoderInitializationException {
        Format r0 = (Format) Assertions.checkNotNull(this.inputFormat);
        if (this.availableCodecInfos != null) goto L16;
        List<MediaCodecInfo> r1 = getAvailableCodecInfos(r9);     // Catch: MediaCodecUtil.DecoderQueryException -> L12
        this.availableCodecInfos = new ArrayDeque();     // Catch: MediaCodecUtil.DecoderQueryException -> L12
        if (this.enableDecoderFallback == false) goto L8;
        this.availableCodecInfos.addAll(r1);     // Catch: MediaCodecUtil.DecoderQueryException -> L12
    L10:
        this.preferredDecoderInitializationException = null;     // Catch: MediaCodecUtil.DecoderQueryException -> L12
        goto L16
    L8:
        if (r1.isEmpty() == true) goto L10;
        this.availableCodecInfos.add(r1.get(0));     // Catch: MediaCodecUtil.DecoderQueryException -> L12
    L12:
        e = move-exception;
        throw new DecoderInitializationException(r0, e, r9, -49998);
    L16:
        if (this.availableCodecInfos.isEmpty() == true) goto L38;
        ArrayDeque r2 = (ArrayDeque) Assertions.checkNotNull(this.availableCodecInfos);
    L19:
        if (this.codec != null) goto L35;
        MediaCodecInfo r3 = (MediaCodecInfo) Assertions.checkNotNull((MediaCodecInfo) r2.peekFirst());
        if (shouldInitCodec(r3) == false) goto L22;
        initCodec(r3, r8);     // Catch: Exception -> L25
    L25:
        e = move-exception;
        Log.m49w(TAG, "Failed to initialize decoder: " + r3, e);
        r2.removeFirst();
        DecoderInitializationException r5 = new DecoderInitializationException(r0, e, r9, r3);
        onCodecError(r5);
        if (this.preferredDecoderInitializationException != null) goto L29;
        this.preferredDecoderInitializationException = r5;
    L31:
        if (r2.isEmpty() == false) goto L19;
        throw this.preferredDecoderInitializationException;
    L29:
        this.preferredDecoderInitializationException = DecoderInitializationException.access$000(this.preferredDecoderInitializationException, r5);
        goto L31
    L22:
        return;
    L35:
        this.availableCodecInfos = null;
        return;
    L38:
        throw new DecoderInitializationException(r0, null, r9, -49999);
    }

    private void processEndOfStream() throws ExoPlaybackException {
        switch(this.codecDrainAction) {
            case 1: goto L7;
            case 2: goto L6;
            case 3: goto L5;
            default: goto L4;
        };
    L4:
        this.outputStreamEnded = true;
        renderToEndOfStream();
        return;
    L5:
        reinitializeCodec();
        return;
    L6:
        flushCodec();
        updateDrmSessionV23();
        return;
    L7:
        flushCodec();
    }

    private void processOutputMediaFormatChanged() {
        this.codecHasOutputMediaFormat = true;
        MediaFormat r1 = ((MediaCodecAdapter) Assertions.checkNotNull(this.codec)).getOutputFormat();
        if (this.codecAdaptationWorkaroundMode != 0) goto L5;
    L10:
        this.codecOutputMediaFormat = r1;
        this.codecOutputMediaFormatChanged = true;
        return;
    L5:
        if (r1.getInteger(StreamInformation.KEY_WIDTH) != 32) goto L10;
        if (r1.getInteger(StreamInformation.KEY_HEIGHT) != 32) goto L10;
        this.shouldSkipAdaptationWorkaroundOutputBuffer = true;
    }

    private boolean readSourceOmittingSampleData(int r5) throws ExoPlaybackException {
        FormatHolder r0 = getFormatHolder();
        this.noDataBuffer.clear();
        int r1 = readSource(r0, this.noDataBuffer, r5 | 4);
        if (r1 != (-5)) goto L7;
        onInputFormatChanged(r0);
        return true;
    L7:
        if (r1 == (-4)) goto L9;
        return false;
    L9:
        if (this.noDataBuffer.isEndOfStream() == false) goto L14;
        this.inputStreamEnded = true;
        processEndOfStream();
        return false;
    L14:
        return false;
    }

    private void reinitializeCodec() throws ExoPlaybackException {
        releaseCodec();
        maybeInitCodecOrBypass();
    }

    private void resetInputBuffer() {
        this.inputIndex = -1;
        this.buffer.data = null;
    }

    private void resetOutputBuffer() {
        this.outputIndex = -1;
        this.outputBuffer = null;
    }

    private void setCodecDrmSession(DrmSession r2) {
        DrmSession.CC.replaceSession(this.codecDrmSession, r2);
        this.codecDrmSession = r2;
    }

    private void setOutputStreamInfo(OutputStreamInfo r5) {
        this.outputStreamInfo = r5;
        if (r5.streamOffsetUs == C0180C.TIME_UNSET) goto L6;
        this.needToNotifyOutputFormatChangeAfterStreamChange = true;
        onOutputStreamOffsetUsChanged(r5.streamOffsetUs);
        return;
    }

    private void setSourceDrmSession(DrmSession r2) {
        DrmSession.CC.replaceSession(this.sourceDrmSession, r2);
        this.sourceDrmSession = r2;
    }

    private boolean shouldContinueRendering(long r5) {
        if (this.renderTimeLimitMs != C0180C.TIME_UNSET) goto L5;
    L8:
        return true;
    L5:
        if ((getClock().elapsedRealtime() - r5) < this.renderTimeLimitMs) goto L8;
        return false;
    }

    protected static boolean supportsFormatDrm(Format r2) {
        if (r2.cryptoType != 0) goto L5;
    L8:
        return true;
    L5:
        if (r2.cryptoType == 2) goto L8;
        return false;
    }

    private boolean updateCodecOperatingRate(Format r5) throws ExoPlaybackException {
        if (Util.SDK_INT >= 23) goto L6;
        return true;
    L6:
        if (this.codec != null) goto L8;
    L27:
        return true;
    L8:
        if (this.codecDrainAction == 3) goto L27;
        if (getState() == 0) goto L27;
        float r0 = getCodecOperatingRateV23(this.targetPlaybackSpeed, (Format) Assertions.checkNotNull(r5), getStreamFormats());
        if (this.codecOperatingRate != r0) goto L16;
        return true;
    L16:
        if (r0 != CODEC_OPERATING_RATE_UNSET) goto L20;
        drainAndReinitializeCodec();
        return false;
    L20:
        if (this.codecOperatingRate == CODEC_OPERATING_RATE_UNSET) goto L22;
    L25:
        Bundle r1 = new Bundle();
        r1.putFloat("operating-rate", r0);
        ((MediaCodecAdapter) Assertions.checkNotNull(this.codec)).setParameters(r1);
        this.codecOperatingRate = r0;
        return true;
    L22:
        if (r0 > this.assumedMinimumCodecOperatingRate) goto L25;
        return true;
    }

    private void updateDrmSessionV23() throws ExoPlaybackException {
        CryptoConfig r0 = ((DrmSession) Assertions.checkNotNull(this.sourceDrmSession)).getCryptoConfig();
        if ((r0 instanceof FrameworkCryptoConfig) == true) goto L11;
    L9:
        setCodecDrmSession(this.sourceDrmSession);
        this.codecDrainState = 0;
        this.codecDrainAction = 0;
        return;
    L11:
        ((MediaCrypto) Assertions.checkNotNull(this.mediaCrypto)).setMediaDrmSession(((FrameworkCryptoConfig) r0).sessionId);     // Catch: MediaCryptoException -> L6
    L6:
        e = move-exception;
        throw createRendererException(e, this.inputFormat, PlaybackException.ERROR_CODE_DRM_SYSTEM_ERROR);
    }

    protected DecoderReuseEvaluation canReuseCodec(MediaCodecInfo r7, Format r8, Format r9) {
        return new DecoderReuseEvaluation(r7.name, r8, r9, 0, 1);
    }

    protected MediaCodecDecoderException createDecoderException(Throwable r2, MediaCodecInfo r3) {
        return new MediaCodecDecoderException(r2, r3);
    }

    public void experimentalEnableProcessedStreamChangedAtStart() {
        this.experimentalEnableProcessedStreamChangedAtStart = true;
    }

    protected final boolean flushOrReinitializeCodec() throws ExoPlaybackException {
        boolean r0 = flushOrReleaseCodec();
        if (r0 == false) goto L5;
        maybeInitCodecOrBypass();
    L5:
        return r0;
    }

    protected boolean flushOrReleaseCodec() {
        if (this.codec != null) goto L6;
        return false;
    L6:
        if (this.codecDrainAction != 3) goto L8;
    L31:
        releaseCodec();
        return true;
    L8:
        if (this.codecNeedsSosFlushWorkaround == false) goto L12;
        if (this.codecHasOutputMediaFormat == false) goto L31;
    L12:
        if (this.codecNeedsEosFlushWorkaround == false) goto L17;
        if (this.codecReceivedEos == true) goto L31;
    L17:
        if (this.codecDrainAction == 2) goto L19;
    L29:
        flushCodec();
        return false;
    L19:
        if (Util.SDK_INT < 23) goto L21;
        boolean r0 = true;
    L22:
        Assertions.checkState(r0);
        if (Util.SDK_INT < 23) goto L29;
        updateDrmSessionV23();     // Catch: ExoPlaybackException -> L26
    L26:
        e = move-exception;
        Log.m49w(TAG, "Failed to update the DRM session, releasing the codec instead.", e);
        releaseCodec();
        return true;
    L21:
        r0 = false;
        goto L22
    }

    protected final MediaCodecAdapter getCodec() {
        return this.codec;
    }

    protected int getCodecBufferFlags(DecoderInputBuffer r2) {
        return 0;
    }

    protected final MediaCodecInfo getCodecInfo() {
        return this.codecInfo;
    }

    protected boolean getCodecNeedsEosPropagation() {
        return false;
    }

    protected float getCodecOperatingRate() {
        return this.codecOperatingRate;
    }

    protected float getCodecOperatingRateV23(float r2, Format r3, Format[] r4) {
        return CODEC_OPERATING_RATE_UNSET;
    }

    protected final MediaFormat getCodecOutputMediaFormat() {
        return this.codecOutputMediaFormat;
    }

    protected abstract List<MediaCodecInfo> getDecoderInfos(MediaCodecSelector r1, Format r2, boolean r3) throws MediaCodecUtil.DecoderQueryException;

    @Override // androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.Renderer
    public final long getDurationToProgressUs(long r7, long r9) {
        return getDurationToProgressUs(this.codecRegisteredOnBufferAvailableListener, r7, r9);
    }

    protected long getDurationToProgressUs(boolean r3, long r4, long r6) {
        return super.getDurationToProgressUs(r4, r6);
    }

    protected long getLastBufferInStreamPresentationTimeUs() {
        return this.lastBufferInStreamPresentationTimeUs;
    }

    protected abstract MediaCodecAdapter.Configuration getMediaCodecConfiguration(MediaCodecInfo r1, Format r2, MediaCrypto r3, float r4);

    protected final long getOutputStreamOffsetUs() {
        return this.outputStreamInfo.streamOffsetUs;
    }

    protected final long getOutputStreamStartPositionUs() {
        return this.outputStreamInfo.startPositionUs;
    }

    protected float getPlaybackSpeed() {
        return this.currentPlaybackSpeed;
    }

    protected final Renderer.WakeupListener getWakeupListener() {
        return this.wakeupListener;
    }

    protected void handleInputBufferSupplementalData(DecoderInputBuffer r1) throws ExoPlaybackException {
    }

    @Override // androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.PlayerMessage.Target
    public void handleMessage(int r2, Object r3) throws ExoPlaybackException {
        if (r2 != 11) goto L5;
        this.wakeupListener = (Renderer.WakeupListener) r3;
        return;
    L5:
        super.handleMessage(r2, r3);
    }

    protected final boolean isBypassEnabled() {
        return this.bypassEnabled;
    }

    protected final boolean isBypassPossible(Format r2) {
        if (this.sourceDrmSession == null) goto L5;
    L7:
        return false;
    L5:
        if (shouldUseBypass(r2) == false) goto L7;
        return true;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public boolean isEnded() {
        return this.outputStreamEnded;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public boolean isReady() {
        if (this.inputFormat != null) goto L5;
    L13:
        return false;
    L5:
        if (isSourceReady() == false) goto L7;
    L12:
        return true;
    L7:
        if (hasOutputBuffer() == true) goto L12;
        if (this.codecHotswapDeadlineMs == C0180C.TIME_UNSET) goto L13;
        if (getClock().elapsedRealtime() >= this.codecHotswapDeadlineMs) goto L13;
        goto L12
    }

    protected final void maybeInitCodecOrBypass() throws ExoPlaybackException {
        if (this.codec == null) goto L5;
        return;
    L5:
        if (this.bypassEnabled == false) goto L7;
        return;
    L7:
        if (this.inputFormat == null) goto L43;
        Format r0 = this.inputFormat;
        if (isBypassPossible(r0) == false) goto L13;
        initBypass(r0);
        return;
    L13:
        setCodecDrmSession(this.sourceDrmSession);
        if (this.codecDrmSession != null) goto L16;
    L38:
    L34:
        e = move-exception;
        throw createRendererException(e, r0, PlaybackException.ERROR_CODE_DECODER_INIT_FAILED);
    L18:
        if (this.codecDrmSession != null) goto L20;
    L26:
        boolean r1 = false;
    L27:
        maybeInitCodecWithFallback(this.mediaCrypto, r1);     // Catch: DecoderInitializationException -> L34
    L29:
        if (this.mediaCrypto != null) goto L31;
        return;
    L31:
        if (this.codec != null) goto L41;
        this.mediaCrypto.release();
        this.mediaCrypto = null;
        return;
    L41:
        return;
    L20:
        if (this.codecDrmSession.getState() == 3) goto L24;
        if (this.codecDrmSession.getState() != 4) goto L26;
    L24:
        if (this.codecDrmSession.requiresSecureDecoder((String) Assertions.checkStateNotNull(r0.sampleMimeType)) == false) goto L26;
        r1 = true;
        goto L27
    L16:
        if (initMediaCryptoIfDrmSessionReady() == false) goto L29;
    }

    protected void onCodecError(Exception r1) {
    }

    protected void onCodecInitialized(String r1, MediaCodecAdapter.Configuration r2, long r3, long r5) {
    }

    protected void onCodecReleased(String r1) {
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onDisabled() {
        this.inputFormat = null;
        setOutputStreamInfo(OutputStreamInfo.UNSET);
        this.pendingOutputStreamChanges.clear();
        flushOrReleaseCodec();
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onEnabled(boolean r2, boolean r3) throws ExoPlaybackException {
        this.decoderCounters = new DecoderCounters();
    }

    protected DecoderReuseEvaluation onInputFormatChanged(FormatHolder r12) throws ExoPlaybackException {
        boolean r0 = true;
        this.waitingForFirstSampleInFormat = true;
        Format r1 = (Format) Assertions.checkNotNull(r12.format);
        if (r1.sampleMimeType == null) goto L85;
        if (Objects.equals(r1.sampleMimeType, MimeTypes.VIDEO_AV1) == true) goto L7;
    L9:
        Format r7 = r1;
    L10:
        setSourceDrmSession(r12.drmSession);
        this.inputFormat = r7;
        if (this.bypassEnabled == false) goto L15;
        this.bypassDrainAndReinitialize = true;
        return null;
    L15:
        if (this.codec != null) goto L18;
        this.availableCodecInfos = null;
        maybeInitCodecOrBypass();
        return null;
    L18:
        MediaCodecAdapter r2 = this.codec;
        MediaCodecInfo r3 = (MediaCodecInfo) Assertions.checkNotNull(this.codecInfo);
        Format r6 = (Format) Assertions.checkNotNull(this.codecInputFormat);
        if (drmNeedsCodecReinitialization(r3, r7, this.codecDrmSession, this.sourceDrmSession) == false) goto L23;
        drainAndReinitializeCodec();
        return new DecoderReuseEvaluation(r3.name, r6, r7, 0, 128);
    L23:
        if (this.sourceDrmSession == this.codecDrmSession) goto L25;
        boolean r4 = true;
    L26:
        if (r4 == true) goto L28;
    L31:
        boolean r5 = true;
    L32:
        Assertions.checkState(r5);
        DecoderReuseEvaluation r10 = canReuseCodec(r3, r6, r7);
        switch(r10.result) {
            case 0: goto L73;
            case 1: goto L63;
            case 2: goto L45;
            case 3: goto L37;
            default: goto L35;
        };
    L73:
        drainAndReinitializeCodec();
    L74:
        int r9 = 0;
    L76:
        if (r10.result != 0) goto L78;
    L83:
        return r10;
    L78:
        if (this.codec != r2) goto L82;
        if (this.codecDrainAction != 3) goto L83;
    L82:
        return new DecoderReuseEvaluation(r3.name, r6, r7, 0, r9);
    L35:
        throw new IllegalStateException();
    L37:
        if (updateCodecOperatingRate(r7) == true) goto L39;
        r9 = 0 | 16;
        goto L76
    L39:
        this.codecInputFormat = r7;
        if (r4 == false) goto L74;
        if (drainAndUpdateCodecDrmSessionV23() == true) goto L74;
        r9 = 0 | 2;
        goto L76
    L45:
        if (updateCodecOperatingRate(r7) == true) goto L47;
        r9 = 0 | 16;
        goto L76
    L47:
        this.codecReconfigured = true;
        this.codecReconfigurationState = 1;
        if (this.codecAdaptationWorkaroundMode != 2) goto L50;
    L57:
        this.codecNeedsAdaptationWorkaroundBuffer = r0;
        this.codecInputFormat = r7;
        if (r4 == false) goto L74;
        if (drainAndUpdateCodecDrmSessionV23() == true) goto L74;
        r9 = 0 | 2;
        goto L76
    L50:
        if (this.codecAdaptationWorkaroundMode == 1) goto L52;
    L56:
        r0 = false;
        goto L57
    L52:
        if (r7.width != r6.width) goto L56;
        if (r7.height != r6.height) goto L56;
    L63:
        if (updateCodecOperatingRate(r7) == true) goto L65;
        r9 = 0 | 16;
        goto L76
    L65:
        this.codecInputFormat = r7;
        if (r4 == false) goto L71;
        if (drainAndUpdateCodecDrmSessionV23() == true) goto L74;
        r9 = 0 | 2;
        goto L76
    L71:
        if (drainAndFlushCodec() == true) goto L74;
        r9 = 0 | 2;
        goto L76
    L28:
        if (Util.SDK_INT >= 23) goto L31;
        r5 = false;
        goto L32
    L25:
        r4 = false;
        goto L26
    L7:
        if (r1.initializationData.isEmpty() == true) goto L9;
        r7 = r1.buildUpon().setInitializationData(null).build();
        goto L10
    L85:
        throw createRendererException(new IllegalArgumentException("Sample MIME type is null."), r1, PlaybackException.ERROR_CODE_DECODING_FORMAT_UNSUPPORTED);
    }

    protected void onOutputFormatChanged(Format r1, MediaFormat r2) throws ExoPlaybackException {
    }

    protected void onOutputStreamOffsetUsChanged(long r1) {
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onPositionReset(long r3, boolean r5) throws ExoPlaybackException {
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        this.pendingOutputEndOfStream = false;
        if (this.bypassEnabled == false) goto L5;
        this.bypassBatchBuffer.clear();
        this.bypassSampleBuffer.clear();
        this.bypassSampleBufferPending = false;
        this.oggOpusAudioPacketizer.reset();
    L7:
        if (this.outputStreamInfo.formatQueue.size() <= 0) goto L9;
        this.waitingForFirstSampleInFormat = true;
    L9:
        this.outputStreamInfo.formatQueue.clear();
        this.pendingOutputStreamChanges.clear();
        return;
    L5:
        flushOrReinitializeCodec();
        goto L7
    }

    protected void onProcessedOutputBuffer(long r3) {
        this.lastProcessedOutputBufferTimeUs = r3;
    L4:
        if (this.pendingOutputStreamChanges.isEmpty() == true) goto L8;
        if (r3 < this.pendingOutputStreamChanges.peek().previousStreamLastBufferTimeUs) goto L11;
        setOutputStreamInfo((OutputStreamInfo) Assertions.checkNotNull(this.pendingOutputStreamChanges.poll()));
        onProcessedStreamChange();
        goto L4
    L11:
        return;
    }

    protected void onProcessedStreamChange() {
    }

    protected void onQueueInputBuffer(DecoderInputBuffer r1) throws ExoPlaybackException {
    }

    protected void onReadyToInitializeCodec(Format r1) throws ExoPlaybackException {
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onReset() {
        disableBypass();     // Catch: Throwable -> L6
        releaseCodec();     // Catch: Throwable -> L6
        setSourceDrmSession(null);
        return;
    L6:
        th = move-exception;
        setSourceDrmSession(null);
        throw th;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onStarted() {
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onStopped() {
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onStreamChanged(Format[] r15, long r16, long r18, MediaSource.MediaPeriodId r20) throws ExoPlaybackException {
        if (this.outputStreamInfo.streamOffsetUs != C0180C.TIME_UNSET) goto L8;
        setOutputStreamInfo(new OutputStreamInfo(C0180C.TIME_UNSET, r16, r18));
        if (this.experimentalEnableProcessedStreamChangedAtStart == false) goto L20;
        onProcessedStreamChange();
        return;
    L20:
        return;
    L8:
        if (this.pendingOutputStreamChanges.isEmpty() == true) goto L10;
    L18:
        this.pendingOutputStreamChanges.add(new OutputStreamInfo(this.largestQueuedPresentationTimeUs, r16, r18));
        return;
    L10:
        if (this.largestQueuedPresentationTimeUs != C0180C.TIME_UNSET) goto L12;
    L15:
        setOutputStreamInfo(new OutputStreamInfo(C0180C.TIME_UNSET, r16, r18));
        if (this.outputStreamInfo.streamOffsetUs == C0180C.TIME_UNSET) goto L22;
        onProcessedStreamChange();
        return;
    L22:
        return;
    L12:
        if (this.lastProcessedOutputBufferTimeUs == C0180C.TIME_UNSET) goto L18;
        if (this.lastProcessedOutputBufferTimeUs < this.largestQueuedPresentationTimeUs) goto L18;
        goto L15
    }

    protected abstract boolean processOutputBuffer(long r1, long r3, MediaCodecAdapter r5, ByteBuffer r6, int r7, int r8, int r9, long r10, boolean r12, boolean r13, Format r14) throws ExoPlaybackException;

    protected void releaseCodec() {
    L15:
        th = move-exception;
        this.codec = null;
    L22:
        th = move-exception;
        this.mediaCrypto = null;
        setCodecDrmSession(null);
        resetCodecStateForRelease();
        throw th;
    L18:
        if (this.mediaCrypto == null) goto L20;
        this.mediaCrypto.release();     // Catch: Throwable -> L22
    L20:
        this.mediaCrypto = null;
        setCodecDrmSession(null);
        resetCodecStateForRelease();
        throw th;
    L4:
        if (this.codec == null) goto L6;
        this.codec.release();     // Catch: Throwable -> L15
        this.decoderCounters.decoderReleaseCount++;     // Catch: Throwable -> L15
        onCodecReleased(((MediaCodecInfo) Assertions.checkNotNull(this.codecInfo)).name);     // Catch: Throwable -> L15
    L6:
        this.codec = null;
    L12:
        th = move-exception;
        this.mediaCrypto = null;
        setCodecDrmSession(null);
        resetCodecStateForRelease();
        throw th;
    L8:
        if (this.mediaCrypto == null) goto L10;
        this.mediaCrypto.release();     // Catch: Throwable -> L12
    L10:
        this.mediaCrypto = null;
        setCodecDrmSession(null);
        resetCodecStateForRelease();
    }

    @Override // androidx.media3.exoplayer.Renderer
    public void render(long r6, long r8) throws ExoPlaybackException {
        boolean r1 = false;
        if (this.pendingOutputEndOfStream == false) goto L6;
        this.pendingOutputEndOfStream = false;
        processEndOfStream();
    L6:
        if (this.pendingPlaybackException == null) goto L64;
        ExoPlaybackException r0 = this.pendingPlaybackException;
        this.pendingPlaybackException = null;
        throw r0;
    L64:
    L59:
        e = move-exception;
        throw createRendererException(e, this.inputFormat, Util.getErrorCodeForMediaDrmErrorCode(e.getErrorCode()));
    L41:
        e = move-exception;
        if (isMediaCodecException(e) == false) goto L58;
        onCodecError(e);
        if ((e instanceof MediaCodec.CodecException) == true) goto L47;
    L50:
        if (r1 == false) goto L52;
        releaseCodec();
    L52:
        MediaCodecDecoderException r2 = createDecoderException(e, getCodecInfo());
        if (r2.errorCode != 1101) goto L55;
        int r3 = PlaybackException.ERROR_CODE_DECODING_RESOURCES_RECLAIMED;
    L57:
        throw createRendererException(r2, this.inputFormat, r1, r3);
    L55:
        r3 = PlaybackException.ERROR_CODE_DECODING_FAILED;
        goto L57
    L47:
        if (((MediaCodec.CodecException) e).isRecoverable() == false) goto L50;
        r1 = true;
        goto L50
    L58:
        throw e;
    L9:
        if (this.outputStreamEnded == false) goto L13;
        renderToEndOfStream();     // Catch: IllegalStateException -> L41 MediaCodec.CryptoException -> L59
        return;
    L13:
        if (this.inputFormat == null) goto L15;
    L17:
        maybeInitCodecOrBypass();     // Catch: IllegalStateException -> L41 MediaCodec.CryptoException -> L59
        if (this.bypassEnabled == false) goto L25;
        TraceUtil.beginSection("bypassRender");     // Catch: IllegalStateException -> L41 MediaCodec.CryptoException -> L59
    L21:
        if (bypassRender(r6, r8) == true) goto L21;
        TraceUtil.endSection();     // Catch: IllegalStateException -> L41 MediaCodec.CryptoException -> L59
    L39:
        this.decoderCounters.ensureUpdated();     // Catch: IllegalStateException -> L41 MediaCodec.CryptoException -> L59
        return;
    L25:
        if (this.codec == null) goto L38;
        long r4 = getClock().elapsedRealtime();     // Catch: IllegalStateException -> L41 MediaCodec.CryptoException -> L59
        TraceUtil.beginSection("drainAndFeed");     // Catch: IllegalStateException -> L41 MediaCodec.CryptoException -> L59
    L28:
        if (drainOutputBuffer(r6, r8) == false) goto L33;
        if (shouldContinueRendering(r4) == true) goto L28;
    L33:
        if (feedInputBuffer() == false) goto L37;
        if (shouldContinueRendering(r4) == true) goto L33;
    L37:
        TraceUtil.endSection();     // Catch: IllegalStateException -> L41 MediaCodec.CryptoException -> L59
        goto L39
    L38:
        this.decoderCounters.skippedInputBufferCount += skipSource(r6);     // Catch: IllegalStateException -> L41 MediaCodec.CryptoException -> L59
        readSourceOmittingSampleData(1);     // Catch: IllegalStateException -> L41 MediaCodec.CryptoException -> L59
        goto L39
    L15:
        if (readSourceOmittingSampleData(2) == true) goto L17;
    }

    protected void renderToEndOfStream() throws ExoPlaybackException {
    }

    protected void resetCodecStateForFlush() {
        resetInputBuffer();
        resetOutputBuffer();
        this.codecHotswapDeadlineMs = C0180C.TIME_UNSET;
        this.codecReceivedEos = false;
        this.lastOutputBufferProcessedRealtimeMs = C0180C.TIME_UNSET;
        this.codecReceivedBuffers = false;
        this.codecNeedsAdaptationWorkaroundBuffer = false;
        this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
        this.isDecodeOnlyOutputBuffer = false;
        this.isLastOutputBuffer = false;
        this.largestQueuedPresentationTimeUs = C0180C.TIME_UNSET;
        this.lastBufferInStreamPresentationTimeUs = C0180C.TIME_UNSET;
        this.lastProcessedOutputBufferTimeUs = C0180C.TIME_UNSET;
        this.codecDrainState = 0;
        this.codecDrainAction = 0;
        this.codecReconfigurationState = this.codecReconfigured ? 1 : 0;
    }

    protected void resetCodecStateForRelease() {
        resetCodecStateForFlush();
        this.pendingPlaybackException = null;
        this.availableCodecInfos = null;
        this.codecInfo = null;
        this.codecInputFormat = null;
        this.codecOutputMediaFormat = null;
        this.codecOutputMediaFormatChanged = false;
        this.codecHasOutputMediaFormat = false;
        this.codecOperatingRate = CODEC_OPERATING_RATE_UNSET;
        this.codecAdaptationWorkaroundMode = 0;
        this.codecNeedsSosFlushWorkaround = false;
        this.codecNeedsEosFlushWorkaround = false;
        this.codecNeedsEosOutputExceptionWorkaround = false;
        this.codecNeedsEosPropagation = false;
        this.codecRegisteredOnBufferAvailableListener = false;
        this.codecReconfigured = false;
        this.codecReconfigurationState = 0;
    }

    protected final void setPendingOutputEndOfStream() {
        this.pendingOutputEndOfStream = true;
    }

    protected final void setPendingPlaybackException(ExoPlaybackException r1) {
        this.pendingPlaybackException = r1;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.Renderer
    public void setPlaybackSpeed(float r2, float r3) throws ExoPlaybackException {
        this.currentPlaybackSpeed = r2;
        this.targetPlaybackSpeed = r3;
        updateCodecOperatingRate(this.codecInputFormat);
    }

    public void setRenderTimeLimitMs(long r1) {
        this.renderTimeLimitMs = r1;
    }

    protected boolean shouldInitCodec(MediaCodecInfo r2) {
        return true;
    }

    protected boolean shouldReinitCodec() {
        return false;
    }

    protected boolean shouldSkipDecoderInputBuffer(DecoderInputBuffer r2) {
        return false;
    }

    protected boolean shouldUseBypass(Format r2) {
        return false;
    }

    @Override // androidx.media3.exoplayer.RendererCapabilities
    public final int supportsFormat(Format r3) throws ExoPlaybackException {
        return supportsFormat(this.mediaCodecSelector, r3);
    L4:
        e = move-exception;
        throw createRendererException(e, r3, PlaybackException.ERROR_CODE_DECODER_QUERY_FAILED);
    }

    protected abstract int supportsFormat(MediaCodecSelector r1, Format r2) throws MediaCodecUtil.DecoderQueryException;

    @Override // androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.RendererCapabilities
    public final int supportsMixedMimeTypeAdaptation() {
        return 8;
    }

    protected final boolean updateCodecOperatingRate() throws ExoPlaybackException {
        return updateCodecOperatingRate(this.codecInputFormat);
    }

    protected final void updateOutputFormatForTime(long r5) throws ExoPlaybackException {
        boolean r0 = false;
        Format r1 = this.outputStreamInfo.formatQueue.pollFloor(r5);
        if (r1 == null) goto L5;
    L9:
        if (r1 == null) goto L11;
        this.outputFormat = r1;
        r0 = true;
    L11:
        if (r0 == false) goto L13;
    L16:
        onOutputFormatChanged((Format) Assertions.checkNotNull(this.outputFormat), this.codecOutputMediaFormat);
        this.codecOutputMediaFormatChanged = false;
        this.needToNotifyOutputFormatChangeAfterStreamChange = false;
        return;
    L13:
        if (this.codecOutputMediaFormatChanged == true) goto L15;
        return;
    L15:
        if (this.outputFormat != null) goto L16;
        return;
    L5:
        if (this.needToNotifyOutputFormatChangeAfterStreamChange == false) goto L9;
        if (this.codecOutputMediaFormat == null) goto L9;
        r1 = this.outputStreamInfo.formatQueue.pollFirst();
        goto L9
    }
}
