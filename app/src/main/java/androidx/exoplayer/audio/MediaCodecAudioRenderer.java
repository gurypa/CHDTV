package androidx.media3.exoplayer.audio;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.media3.common.util.MediaFormatUtil;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.DecoderReuseEvaluation;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.MediaClock;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.media3.exoplayer.mediacodec.LoudnessCodecController;
import androidx.media3.exoplayer.mediacodec.MediaCodecAdapter;
import androidx.media3.exoplayer.mediacodec.MediaCodecInfo;
import androidx.media3.exoplayer.mediacodec.MediaCodecRenderer;
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector;
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil;
import androidx.media3.extractor.VorbisUtil;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class MediaCodecAudioRenderer extends MediaCodecRenderer implements MediaClock {
    private static final String TAG = "MediaCodecAudioRenderer";
    private static final String VIVO_BITS_PER_SAMPLE_KEY = "v-bits-per-sample";
    private boolean allowPositionDiscontinuity;
    private final AudioSink audioSink;
    private boolean audioSinkNeedsReset;
    private int codecMaxInputSize;
    private boolean codecNeedsDiscardChannelsWorkaround;
    private boolean codecNeedsVorbisToAndroidChannelMappingWorkaround;
    private final Context context;
    private long currentPositionUs;
    private Format decryptOnlyCodecFormat;
    private final AudioRendererEventListener.EventDispatcher eventDispatcher;
    private boolean hasPendingReportedSkippedSilence;
    private Format inputFormat;
    private boolean isStarted;
    private final LoudnessCodecController loudnessCodecController;
    private long nextBufferToWritePresentationTimeUs;
    private int rendererPriority;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.audio.MediaCodecAudioRenderer$1 */
    static /* synthetic */ class C02731 {
    }

    private static final class Api23 {
        private Api23() {
        }

        public static void setAudioSinkPreferredDevice(AudioSink r1, Object r2) {
            r1.setPreferredDevice((AudioDeviceInfo) r2);
        }
    }

    private final class AudioSinkListener implements AudioSink.Listener {
        final /* synthetic */ MediaCodecAudioRenderer this$0;

        private AudioSinkListener(MediaCodecAudioRenderer r1) {
            this.this$0 = r1;
        }

        /* synthetic */ AudioSinkListener(MediaCodecAudioRenderer r1, C02731 r2) {
            this(r1);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onAudioCapabilitiesChanged() {
            MediaCodecAudioRenderer.access$500(this.this$0);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onAudioSinkError(Exception r3) {
            Log.m45e(MediaCodecAudioRenderer.TAG, "Audio sink error", r3);
            MediaCodecAudioRenderer.access$200(this.this$0).audioSinkError(r3);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onAudioTrackInitialized(AudioSink.AudioTrackConfig r2) {
            MediaCodecAudioRenderer.access$200(this.this$0).audioTrackInitialized(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onAudioTrackReleased(AudioSink.AudioTrackConfig r2) {
            MediaCodecAudioRenderer.access$200(this.this$0).audioTrackReleased(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onOffloadBufferEmptying() {
            Renderer.WakeupListener r0 = MediaCodecAudioRenderer.access$300(this.this$0);
            if (r0 == null) goto L6;
            r0.onWakeup();
            return;
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onOffloadBufferFull() {
            Renderer.WakeupListener r0 = MediaCodecAudioRenderer.access$400(this.this$0);
            if (r0 == null) goto L6;
            r0.onSleep();
            return;
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onPositionAdvancing(long r2) {
            MediaCodecAudioRenderer.access$200(this.this$0).positionAdvancing(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onPositionDiscontinuity() {
            this.this$0.onPositionDiscontinuity();
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onSilenceSkipped() {
            MediaCodecAudioRenderer.access$102(this.this$0, true);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onSkipSilenceEnabledChanged(boolean r2) {
            MediaCodecAudioRenderer.access$200(this.this$0).skipSilenceEnabledChanged(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioSink.Listener
        public void onUnderrun(int r8, long r9, long r11) {
            MediaCodecAudioRenderer.access$200(this.this$0).underrun(r8, r9, r11);
        }
    }

    public MediaCodecAudioRenderer(Context r11, MediaCodecAdapter.Factory r12, MediaCodecSelector r13, boolean r14, Handler r15, AudioRendererEventListener r16, AudioSink r17) {
        if (Util.SDK_INT < 35) goto L5;
        LoudnessCodecController r0 = new LoudnessCodecController();
    L6:
        this(r11, r12, r13, r14, r15, r16, r17, r0);
        return;
    L5:
        r0 = null;
        goto L6
    }

    public MediaCodecAudioRenderer(Context r7, MediaCodecAdapter.Factory r8, MediaCodecSelector r9, boolean r10, Handler r11, AudioRendererEventListener r12, AudioSink r13, LoudnessCodecController r14) {
        super(1, r8, r9, r10, 44100.0f);
        this.context = r7.getApplicationContext();
        this.audioSink = r13;
        this.loudnessCodecController = r14;
        this.rendererPriority = -1000;
        this.eventDispatcher = new AudioRendererEventListener.EventDispatcher(r11, r12);
        this.nextBufferToWritePresentationTimeUs = C0180C.TIME_UNSET;
        r13.setListener(new AudioSinkListener(this, null));
    }

    public MediaCodecAudioRenderer(Context r2, MediaCodecSelector r3) {
        this(r2, r3, null, null);
    }

    public MediaCodecAudioRenderer(Context r8, MediaCodecSelector r9, Handler r10, AudioRendererEventListener r11) {
        this(r8, r9, r10, r11, new DefaultAudioSink.Builder(r8).build());
    }

    @Deprecated
    public MediaCodecAudioRenderer(Context r8, MediaCodecSelector r9, Handler r10, AudioRendererEventListener r11, AudioCapabilities r12, AudioProcessor... r13) {
        this(r8, r9, r10, r11, new DefaultAudioSink.Builder().setAudioCapabilities((AudioCapabilities) MoreObjects.firstNonNull(r12, AudioCapabilities.DEFAULT_AUDIO_CAPABILITIES)).setAudioProcessors(r13).build());
    }

    public MediaCodecAudioRenderer(Context r9, MediaCodecSelector r10, Handler r11, AudioRendererEventListener r12, AudioSink r13) {
        this(r9, MediaCodecAdapter.Factory.CC.getDefault(r9), r10, false, r11, r12, r13);
    }

    public MediaCodecAudioRenderer(Context r9, MediaCodecSelector r10, boolean r11, Handler r12, AudioRendererEventListener r13, AudioSink r14) {
        this(r9, MediaCodecAdapter.Factory.CC.getDefault(r9), r10, r11, r12, r13, r14);
    }

    static /* synthetic */ boolean access$102(MediaCodecAudioRenderer r0, boolean r1) {
        r0.hasPendingReportedSkippedSilence = r1;
        return r1;
    }

    static /* synthetic */ AudioRendererEventListener.EventDispatcher access$200(MediaCodecAudioRenderer r1) {
        return r1.eventDispatcher;
    }

    static /* synthetic */ Renderer.WakeupListener access$300(MediaCodecAudioRenderer r1) {
        return r1.getWakeupListener();
    }

    static /* synthetic */ Renderer.WakeupListener access$400(MediaCodecAudioRenderer r1) {
        return r1.getWakeupListener();
    }

    static /* synthetic */ void access$500(MediaCodecAudioRenderer r0) {
        r0.onRendererCapabilitiesChanged();
    }

    private static boolean codecNeedsDiscardChannelsWorkaround(String r2) {
        if (Util.SDK_INT < 24) goto L5;
    L15:
        return false;
    L5:
        if ("OMX.SEC.aac.dec".equals(r2) == false) goto L15;
        if ("samsung".equals(Util.MANUFACTURER) == false) goto L15;
        if (Util.DEVICE.startsWith("zeroflte") == false) goto L11;
    L14:
        return true;
    L11:
        if (Util.DEVICE.startsWith("herolte") == true) goto L14;
        if (Util.DEVICE.startsWith("heroqlte") == false) goto L15;
        goto L14
    }

    private static boolean codecNeedsVorbisToAndroidChannelMappingWorkaround(String r1) {
        if (r1.equals("OMX.google.opus.decoder") == false) goto L5;
    L12:
        return true;
    L5:
        if (r1.equals("c2.android.opus.decoder") == true) goto L12;
        if (r1.equals("OMX.google.vorbis.decoder") == true) goto L12;
        if (r1.equals("c2.android.vorbis.decoder") == true) goto L12;
        return false;
    }

    private static boolean deviceDoesntSupportOperatingRate() {
        if (Util.SDK_INT == 23) goto L5;
    L9:
        return false;
    L5:
        if ("ZTE B2017G".equals(Util.MODEL) == false) goto L7;
    L8:
        return true;
    L7:
        if ("AXON 7 mini".equals(Util.MODEL) == false) goto L9;
        goto L8
    }

    private int getAudioOffloadSupport(Format r4) {
        AudioOffloadSupport r0 = this.audioSink.getFormatOffloadSupport(r4);
        if (r0.isFormatSupported == true) goto L6;
        return 0;
    L6:
        int r1 = 512;
        if (r0.isGaplessSupported == false) goto L10;
        r1 = 512 | 1024;
    L10:
        if (r0.isSpeedChangeSupported == true) goto L12;
        return r1;
    L12:
        return r1 | 2048;
    }

    private int getCodecMaxInputSize(MediaCodecInfo r3, Format r4) {
        if ("OMX.google.raw.decoder".equals(r3.name) == false) goto L13;
        if (Util.SDK_INT >= 24) goto L13;
        if (Util.SDK_INT == 23) goto L9;
        return -1;
    L9:
        if (Util.isTv(this.context) == true) goto L13;
        return -1;
    L13:
        return r4.maxInputSize;
    }

    private static List<MediaCodecInfo> getDecoderInfos(MediaCodecSelector r2, Format r3, boolean r4, AudioSink r5) throws MediaCodecUtil.DecoderQueryException {
        if (r3.sampleMimeType != null) goto L7;
        return ImmutableList.m212of();
    L7:
        if (r5.supportsFormat(r3) == false) goto L13;
        MediaCodecInfo r0 = MediaCodecUtil.getDecryptOnlyDecoderInfo();
        if (r0 == null) goto L13;
        return ImmutableList.m213of(r0);
    L13:
        return MediaCodecUtil.getDecoderInfosSoftMatch(r2, r3, r4, false);
    }

    private void setAudioSessionId(int r3) {
        this.audioSink.setAudioSessionId(r3);
        if (Util.SDK_INT >= 35) goto L5;
        return;
    L5:
        if (this.loudnessCodecController == null) goto L9;
        this.loudnessCodecController.setAudioSessionId(r3);
        return;
    }

    private void updateCodecImportance() {
        MediaCodecAdapter r0 = getCodec();
        if (r0 != null) goto L6;
        return;
    L6:
        if (Util.SDK_INT < 35) goto L9;
        Bundle r1 = new Bundle();
        r1.putInt("importance", Math.max(0, -this.rendererPriority));
        r0.setParameters(r1);
        return;
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

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected DecoderReuseEvaluation canReuseCodec(MediaCodecInfo r9, Format r10, Format r11) {
        DecoderReuseEvaluation r0 = r9.canReuseCodec(r10, r11);
        int r1 = r0.discardReasons;
        if (isBypassPossible(r11) == false) goto L6;
        r1 = r1 | 32768;
    L6:
        if (getCodecMaxInputSize(r9, r11) <= this.codecMaxInputSize) goto L8;
        int r7 = r1 | 64;
    L9:
        String r3 = r9.name;
        if (r7 == 0) goto L12;
        int r2 = 0;
    L14:
        return new DecoderReuseEvaluation(r3, r10, r11, r2, r7);
    L12:
        r2 = r0.result;
        goto L14
    L8:
        r7 = r1;
        goto L9
    }

    protected int getCodecMaxInputSize(MediaCodecInfo r6, Format r7, Format[] r8) {
        int r0 = getCodecMaxInputSize(r6, r7);
        if (r8.length != 1) goto L5;
        return r0;
    L5:
        int r1 = r8.length;
        int r2 = 0;
    L6:
        if (r2 >= r1) goto L11;
        Format r3 = r8[r2];
        if (r6.canReuseCodec(r7, r3).result == 0) goto L10;
        r0 = Math.max(r0, getCodecMaxInputSize(r6, r3));
    L10:
        r2 = r2 + 1;
        goto L6
    L11:
        return r0;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected float getCodecOperatingRateV23(float r7, Format r8, Format[] r9) {
        int r0 = -1;
        int r1 = r9.length;
        int r2 = 0;
    L4:
        if (r2 >= r1) goto L9;
        int r5 = r9[r2].sampleRate;
        if (r5 == (-1)) goto L8;
        r0 = Math.max(r0, r5);
    L8:
        r2 = r2 + 1;
        goto L4
    L9:
        if (r0 != (-1)) goto L12;
        return -1.0f;
    L12:
        return r0 * r7;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected List<MediaCodecInfo> getDecoderInfos(MediaCodecSelector r2, Format r3, boolean r4) throws MediaCodecUtil.DecoderQueryException {
        return MediaCodecUtil.getDecoderInfosSortedByFormatSupport(getDecoderInfos(r2, r3, r4, this.audioSink), r3);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    public long getDurationToProgressUs(boolean r5, long r6, long r8) {
        if (this.nextBufferToWritePresentationTimeUs == C0180C.TIME_UNSET) goto L14;
        float r0 = this.nextBufferToWritePresentationTimeUs - r6;
        if (getPlaybackParameters() == null) goto L7;
        float r1 = getPlaybackParameters().speed;
    L8:
        long r2 = (long) ((r0 / r1) / 2.0f);
        if (this.isStarted == false) goto L12;
        r2 = r2 - (Util.msToUs(getClock().elapsedRealtime()) - r8);
    L12:
        return Math.max(Renderer.DEFAULT_DURATION_TO_PROGRESS_US, r2);
    L7:
        r1 = 1.0f;
        goto L8
    L14:
        return super.getDurationToProgressUs(r5, r6, r8);
    }

    @Override // androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.Renderer
    public MediaClock getMediaClock() {
        return this;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected MediaCodecAdapter.Configuration getMediaCodecConfiguration(MediaCodecInfo r4, Format r5, MediaCrypto r6, float r7) {
        this.codecMaxInputSize = getCodecMaxInputSize(r4, r5, getStreamFormats());
        this.codecNeedsDiscardChannelsWorkaround = codecNeedsDiscardChannelsWorkaround(r4.name);
        this.codecNeedsVorbisToAndroidChannelMappingWorkaround = codecNeedsVorbisToAndroidChannelMappingWorkaround(r4.name);
        MediaFormat r0 = getMediaFormat(r5, r4.codecMimeType, this.codecMaxInputSize, r7);
        if (MimeTypes.AUDIO_RAW.equals(r4.mimeType) == true) goto L5;
    L7:
        boolean r1 = false;
    L8:
        if (r1 == false) goto L10;
        Format r2 = r5;
    L11:
        this.decryptOnlyCodecFormat = r2;
        return MediaCodecAdapter.Configuration.createForAudioDecoding(r4, r0, r5, r6, this.loudnessCodecController);
    L10:
        r2 = null;
        goto L11
    L5:
        if (MimeTypes.AUDIO_RAW.equals(r5.sampleMimeType) == true) goto L7;
        r1 = true;
        goto L8
    }

    protected MediaFormat getMediaFormat(Format r7, String r8, int r9, float r10) {
        MediaFormat r0 = new MediaFormat();
        r0.setString("mime", r8);
        r0.setInteger("channel-count", r7.channelCount);
        r0.setInteger("sample-rate", r7.sampleRate);
        MediaFormatUtil.setCsdBuffers(r0, r7.initializationData);
        MediaFormatUtil.maybeSetInteger(r0, "max-input-size", r9);
        if (Util.SDK_INT < 23) goto L10;
        r0.setInteger("priority", 0);
        if (r10 == (-1.0f)) goto L10;
        if (deviceDoesntSupportOperatingRate() == true) goto L10;
        r0.setFloat("operating-rate", r10);
    L10:
        if (Util.SDK_INT > 28) goto L15;
        if (MimeTypes.AUDIO_AC4.equals(r7.sampleMimeType) == false) goto L15;
        r0.setInteger("ac4-is-sync", 1);
    L15:
        if (Util.SDK_INT < 24) goto L20;
        if (this.audioSink.getFormatSupport(Util.getPcmFormat(4, r7.channelCount, r7.sampleRate)) != 2) goto L20;
        r0.setInteger("pcm-encoding", 4);
    L20:
        if (Util.SDK_INT < 32) goto L23;
        r0.setInteger("max-output-channel-count", 99);
    L23:
        if (Util.SDK_INT < 35) goto L25;
        r0.setInteger("importance", Math.max(0, -this.rendererPriority));
    L25:
        return r0;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public String getName() {
        return TAG;
    }

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

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void handleInputBufferSupplementalData(DecoderInputBuffer r7) {
        if (Util.SDK_INT >= 29) goto L5;
        return;
    L5:
        if (r7.format != null) goto L7;
        return;
    L7:
        if (Objects.equals(r7.format.sampleMimeType, MimeTypes.AUDIO_OPUS) == true) goto L9;
        return;
    L9:
        if (isBypassEnabled() == false) goto L17;
        ByteBuffer r0 = (ByteBuffer) Assertions.checkNotNull(r7.supplementalData);
        int r1 = ((Format) Assertions.checkNotNull(r7.format)).encoderDelay;
        if (r0.remaining() != 8) goto L18;
        int r2 = (int) ((r0.order(ByteOrder.LITTLE_ENDIAN).getLong() * 48000) / C0180C.NANOS_PER_SECOND);
        this.audioSink.setOffloadDelayPadding(r1, r2);
        return;
    L18:
        return;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer, androidx.media3.exoplayer.PlayerMessage.Target
    public void handleMessage(int r4, Object r5) throws ExoPlaybackException {
        switch(r4) {
            case 2: goto L12;
            case 3: goto L11;
            case 6: goto L10;
            case 9: goto L9;
            case 10: goto L8;
            case 12: goto L6;
            case 16: goto L4;
            default: goto L3;
        };
    L3:
        super.handleMessage(r4, r5);
        return;
    L4:
        this.rendererPriority = ((Integer) Assertions.checkNotNull(r5)).intValue();
        updateCodecImportance();
        return;
    L8:
        setAudioSessionId(((Integer) Assertions.checkNotNull(r5)).intValue());
        return;
    L9:
        this.audioSink.setSkipSilenceEnabled(((Boolean) Assertions.checkNotNull(r5)).booleanValue());
        return;
    L10:
        this.audioSink.setAuxEffectInfo((AuxEffectInfo) Assertions.checkNotNull((AuxEffectInfo) r5));
        return;
    L11:
        this.audioSink.setAudioAttributes((AudioAttributes) Assertions.checkNotNull((AudioAttributes) r5));
        return;
    L12:
        this.audioSink.setVolume(((Float) Assertions.checkNotNull(r5)).floatValue());
        return;
    L6:
        if (Util.SDK_INT < 23) goto L16;
        Api23.setAudioSinkPreferredDevice(this.audioSink, r5);
        return;
    }

    @Override // androidx.media3.exoplayer.MediaClock
    public boolean hasSkippedSilenceSinceLastCall() {
        boolean r0 = this.hasPendingReportedSkippedSilence;
        this.hasPendingReportedSkippedSilence = false;
        return r0;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.Renderer
    public boolean isEnded() {
        if (super.isEnded() == true) goto L5;
    L7:
        return false;
    L5:
        if (this.audioSink.isEnded() == false) goto L7;
        return true;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.Renderer
    public boolean isReady() {
        if (this.audioSink.hasPendingData() == false) goto L5;
    L8:
        return true;
    L5:
        if (super.isReady() == true) goto L8;
        return false;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void onCodecError(Exception r3) {
        Log.m45e(TAG, "Audio codec error", r3);
        this.eventDispatcher.audioCodecError(r3);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void onCodecInitialized(String r7, MediaCodecAdapter.Configuration r8, long r9, long r11) {
        this.eventDispatcher.decoderInitialized(r7, r9, r11);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void onCodecReleased(String r2) {
        this.eventDispatcher.decoderReleased(r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer
    protected void onDisabled() {
        this.audioSinkNeedsReset = true;
        this.inputFormat = null;
        this.audioSink.flush();     // Catch: Throwable -> L10
        super.onDisabled();     // Catch: Throwable -> L7
        this.eventDispatcher.disabled(this.decoderCounters);
        return;
    L7:
        th = move-exception;
        this.eventDispatcher.disabled(this.decoderCounters);
        throw th;
    L10:
        th = move-exception;
        super.onDisabled();     // Catch: Throwable -> L14
        this.eventDispatcher.disabled(this.decoderCounters);
        throw th;
    L14:
        th = move-exception;
        this.eventDispatcher.disabled(this.decoderCounters);
        throw th;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer
    protected void onEnabled(boolean r3, boolean r4) throws ExoPlaybackException {
        super.onEnabled(r3, r4);
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

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected DecoderReuseEvaluation onInputFormatChanged(FormatHolder r4) throws ExoPlaybackException {
        Format r0 = (Format) Assertions.checkNotNull(r4.format);
        this.inputFormat = r0;
        DecoderReuseEvaluation r1 = super.onInputFormatChanged(r4);
        this.eventDispatcher.inputFormatChanged(r0, r1);
        return r1;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void onOutputFormatChanged(Format r6, MediaFormat r7) throws ExoPlaybackException {
        int[] r0 = null;
        if (this.decryptOnlyCodecFormat == null) goto L6;
        Format r1 = this.decryptOnlyCodecFormat;
    L48:
    L45:
        e = move-exception;
        throw createRendererException(e, e.format, PlaybackException.ERROR_CODE_AUDIO_TRACK_INIT_FAILED);
    L36:
        if (Util.SDK_INT >= 29) goto L38;
    L43:
        this.audioSink.configure(r1, 0, r0);     // Catch: AudioSink.ConfigurationException -> L45
        return;
    L38:
        if (isBypassEnabled() == true) goto L40;
    L42:
        this.audioSink.setOffloadMode(0);     // Catch: AudioSink.ConfigurationException -> L45
        goto L43
    L40:
        if (getConfiguration().offloadModePreferred == 0) goto L42;
        this.audioSink.setOffloadMode(getConfiguration().offloadModePreferred);     // Catch: AudioSink.ConfigurationException -> L45
        goto L43
    L6:
        if (getCodec() != null) goto L8;
        r1 = r6;
        goto L48
    L8:
        Assertions.checkNotNull(r7);
        if (MimeTypes.AUDIO_RAW.equals(r6.sampleMimeType) == false) goto L12;
        int r2 = r6.pcmEncoding;
    L20:
        Format r3 = new Format.Builder().setSampleMimeType(MimeTypes.AUDIO_RAW).setPcmEncoding(r2).setEncoderDelay(r6.encoderDelay).setEncoderPadding(r6.encoderPadding).setMetadata(r6.metadata).setCustomData(r6.customData).setId(r6.f26id).setLabel(r6.label).setLabels(r6.labels).setLanguage(r6.language).setSelectionFlags(r6.selectionFlags).setRoleFlags(r6.roleFlags).setChannelCount(r7.getInteger("channel-count")).setSampleRate(r7.getInteger("sample-rate")).build();
        if (this.codecNeedsDiscardChannelsWorkaround == false) goto L32;
        if (r3.channelCount != 6) goto L32;
        if (r6.channelCount >= 6) goto L32;
        r0 = new int[r6.channelCount];
        int r4 = 0;
    L28:
        if (r4 >= r6.channelCount) goto L30;
        r0[r4] = r4;
        r4 = r4 + 1;
        goto L28
    L30:
        r1 = r3;
    L32:
        if (this.codecNeedsVorbisToAndroidChannelMappingWorkaround == false) goto L34;
        r0 = VorbisUtil.getVorbisToAndroidChannelLayoutMapping(r3.channelCount);
        r1 = r3;
        goto L48
    L34:
        r1 = r3;
        goto L48
    L12:
        if (Util.SDK_INT < 24) goto L17;
        if (r7.containsKey("pcm-encoding") == false) goto L17;
        r2 = r7.getInteger("pcm-encoding");
    L17:
        if (r7.containsKey(VIVO_BITS_PER_SAMPLE_KEY) == false) goto L19;
        r2 = Util.getPcmEncoding(r7.getInteger(VIVO_BITS_PER_SAMPLE_KEY));
        goto L20
    L19:
        r2 = 2;
        goto L20
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void onOutputStreamOffsetUsChanged(long r2) {
        this.audioSink.setOutputStreamOffsetUs(r2);
    }

    protected void onPositionDiscontinuity() {
        this.allowPositionDiscontinuity = true;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer
    protected void onPositionReset(long r2, boolean r4) throws ExoPlaybackException {
        super.onPositionReset(r2, r4);
        this.audioSink.flush();
        this.currentPositionUs = r2;
        this.hasPendingReportedSkippedSilence = false;
        this.allowPositionDiscontinuity = true;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void onProcessedStreamChange() {
        super.onProcessedStreamChange();
        this.audioSink.handleDiscontinuity();
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onRelease() {
        this.audioSink.release();
        if (Util.SDK_INT >= 35) goto L5;
        return;
    L5:
        if (this.loudnessCodecController == null) goto L9;
        this.loudnessCodecController.release();
        return;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer
    protected void onReset() {
        this.hasPendingReportedSkippedSilence = false;
        super.onReset();     // Catch: Throwable -> L8
        if (this.audioSinkNeedsReset == false) goto L15;
        this.audioSinkNeedsReset = false;
        this.audioSink.reset();
        return;
    L15:
        return;
    L8:
        th = move-exception;
        if (this.audioSinkNeedsReset == false) goto L12;
        this.audioSinkNeedsReset = false;
        this.audioSink.reset();
    L12:
        throw th;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer
    protected void onStarted() {
        super.onStarted();
        this.audioSink.play();
        this.isStarted = true;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer, androidx.media3.exoplayer.BaseRenderer
    protected void onStopped() {
        updateCurrentPosition();
        this.isStarted = false;
        this.audioSink.pause();
        super.onStopped();
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected boolean processOutputBuffer(long r12, long r14, MediaCodecAdapter r16, ByteBuffer r17, int r18, int r19, int r20, long r21, boolean r23, boolean r24, Format r25) throws ExoPlaybackException {
        Assertions.checkNotNull(r17);
        this.nextBufferToWritePresentationTimeUs = C0180C.TIME_UNSET;
        if (this.decryptOnlyCodecFormat != null) goto L5;
    L8:
        if (r23 == false) goto L49;
        if (r16 == null) goto L11;
        r16.releaseOutputBuffer(r18, false);
    L11:
        this.decoderCounters.skippedOutputBufferCount += r20;
        this.audioSink.handleDiscontinuity();
        return true;
    L49:
    L47:
        if (this.audioSink.handleBuffer(r17, r21, r20) == false) goto L21;
        if (r16 == null) goto L19;
        r16.releaseOutputBuffer(r18, false);
    L19:
        this.decoderCounters.renderedOutputBufferCount += r20;
        return true;
    L21:
        this.nextBufferToWritePresentationTimeUs = r21;
        return false;
    L25:
        e = e;
    L39:
        Format r6 = this.inputFormat;
        boolean r7 = e.isRecoverable;
        if (isBypassEnabled() == true) goto L42;
    L44:
        int r10 = PlaybackException.ERROR_CODE_AUDIO_TRACK_INIT_FAILED;
    L46:
        throw createRendererException(e, r6, r7, r10);
    L42:
        if (getConfiguration().offloadModePreferred == 0) goto L44;
        r10 = PlaybackException.ERROR_CODE_AUDIO_TRACK_OFFLOAD_INIT_FAILED;
    L23:
        e = e;
    L29:
        boolean r8 = e.isRecoverable;
        if (isBypassEnabled() == true) goto L32;
    L34:
        int r9 = PlaybackException.ERROR_CODE_AUDIO_TRACK_WRITE_FAILED;
    L36:
        throw createRendererException(e, r25, r8, r9);
    L32:
        if (getConfiguration().offloadModePreferred == 0) goto L34;
        r9 = PlaybackException.ERROR_CODE_AUDIO_TRACK_OFFLOAD_WRITE_FAILED;
    L37:
        e = e;
    L27:
        e = e;
        goto L29
    L5:
        if ((r19 & 2) == 0) goto L8;
        ((MediaCodecAdapter) Assertions.checkNotNull(r16)).releaseOutputBuffer(r18, false);
        return true;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected void renderToEndOfStream() throws ExoPlaybackException {
        this.audioSink.playToEndOfStream();     // Catch: AudioSink.WriteException -> L7
        if (getLastBufferInStreamPresentationTimeUs() == C0180C.TIME_UNSET) goto L16;
        this.nextBufferToWritePresentationTimeUs = getLastBufferInStreamPresentationTimeUs();     // Catch: AudioSink.WriteException -> L7
        return;
    L16:
        return;
    L7:
        e = move-exception;
        Format r1 = e.format;
        boolean r2 = e.isRecoverable;
        if (isBypassEnabled() == false) goto L11;
        int r3 = PlaybackException.ERROR_CODE_AUDIO_TRACK_OFFLOAD_WRITE_FAILED;
    L13:
        throw createRendererException(e, r1, r2, r3);
    L11:
        r3 = PlaybackException.ERROR_CODE_AUDIO_TRACK_WRITE_FAILED;
        goto L13
    }

    @Override // androidx.media3.exoplayer.MediaClock
    public void setPlaybackParameters(PlaybackParameters r2) {
        this.audioSink.setPlaybackParameters(r2);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected boolean shouldUseBypass(Format r4) {
        if (getConfiguration().offloadModePreferred == 0) goto L17;
        int r0 = getAudioOffloadSupport(r4);
        if ((r0 & 512) == 0) goto L17;
        if (getConfiguration().offloadModePreferred != 2) goto L9;
        return true;
    L9:
        if ((r0 & 1024) == 0) goto L11;
        return true;
    L11:
        if (r4.encoderDelay != 0) goto L17;
        if (r4.encoderPadding != 0) goto L17;
        return true;
    L17:
        return this.audioSink.supportsFormat(r4);
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
    protected int supportsFormat(MediaCodecSelector r18, Format r19) throws MediaCodecUtil.DecoderQueryException {
        int r3 = 0;
        if (MimeTypes.isAudio(r19.sampleMimeType) == true) goto L7;
        return RendererCapabilities.CC.create(0);
    L7:
        if (r19.cryptoType == 0) goto L9;
        boolean r2 = true;
    L10:
        boolean r5 = supportsFormatDrm(r19);
        int r7 = 8;
        int r8 = 4;
        if (r5 == false) goto L20;
        if (r2 == true) goto L14;
    L15:
        int r6 = getAudioOffloadSupport(r19);
        if (this.audioSink.supportsFormat(r19) == true) goto L18;
        int r12 = r6;
    L22:
        if (MimeTypes.AUDIO_RAW.equals(r19.sampleMimeType) == false) goto L28;
        if (this.audioSink.supportsFormat(r19) == true) goto L28;
        return RendererCapabilities.CC.create(1);
    L28:
        if (this.audioSink.supportsFormat(Util.getPcmFormat(2, r19.channelCount, r19.sampleRate)) == false) goto L30;
        List<MediaCodecInfo> r9 = getDecoderInfos(r18, r19, false, this.audioSink);
        if (r9.isEmpty() == true) goto L34;
        if (r5 == false) goto L37;
        MediaCodecInfo r4 = r9.get(0);
        boolean r10 = r4.isFormatSupported(r19);
        if (r10 == true) goto L47;
        int r11 = 1;
    L42:
        if (r11 >= r9.size()) goto L47;
        MediaCodecInfo r14 = r9.get(r11);
        if (r14.isFormatSupported(r19) == true) goto L45;
        r11 = r11 + 1;
        goto L42
    L45:
        r4 = r14;
        boolean r15 = true;
        boolean r16 = false;
    L48:
        if (r15 == true) goto L51;
        r8 = 3;
    L51:
        if (r15 == false) goto L57;
        if (r4.isSeamlessAdaptationSupported(r19) == false) goto L57;
        r7 = 16;
    L57:
        if (r4.hardwareAccelerated == false) goto L59;
        int r13 = 64;
    L61:
        if (r16 == false) goto L64;
        r3 = 128;
    L64:
        return RendererCapabilities.CC.create(r8, r7, 32, r13, r3, r12);
    L59:
        r13 = 0;
    L47:
        r15 = r10;
        r16 = true;
        goto L48
    L37:
        return RendererCapabilities.CC.create(2);
    L34:
        return RendererCapabilities.CC.create(1);
    L30:
        return RendererCapabilities.CC.create(1);
    L18:
        return RendererCapabilities.CC.create(4, 8, 32, r6);
    L14:
        if (MediaCodecUtil.getDecryptOnlyDecoderInfo() != null) goto L15;
    L20:
        r12 = 0;
        goto L22
    L9:
        r2 = false;
        goto L10
    }
}
