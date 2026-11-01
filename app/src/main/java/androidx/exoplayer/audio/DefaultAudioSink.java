package androidx.media3.exoplayer.audio;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioRouting;
import android.media.AudioTrack;
import android.media.PlaybackParams;
import android.media.metrics.LogSessionId;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Pair;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.AuxEffectInfo;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.audio.AudioProcessingPipeline;
import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.common.audio.ToInt16PcmAudioProcessor;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.extractor.Ac3Util;
import androidx.media3.extractor.Ac4Util;
import androidx.media3.extractor.DtsUtil;
import androidx.media3.extractor.MpegAudioUtil;
import androidx.media3.extractor.OpusUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultAudioSink implements AudioSink {
    private static final int AUDIO_TRACK_SMALLER_BUFFER_RETRY_SIZE = 1000000;
    private static final int AUDIO_TRACK_VOLUME_RAMP_TIME_MS = 20;
    public static final float DEFAULT_PLAYBACK_SPEED = 1.0f;
    private static final boolean DEFAULT_SKIP_SILENCE = false;
    private static final int ERROR_NATIVE_DEAD_OBJECT = -32;
    public static final float MAX_PITCH = 8.0f;
    public static final float MAX_PLAYBACK_SPEED = 8.0f;
    private static final int MINIMUM_REPORT_SKIPPED_SILENCE_DURATION_US = 300000;
    public static final float MIN_PITCH = 0.1f;
    public static final float MIN_PLAYBACK_SPEED = 0.1f;
    public static final int OUTPUT_MODE_OFFLOAD = 1;
    public static final int OUTPUT_MODE_PASSTHROUGH = 2;
    public static final int OUTPUT_MODE_PCM = 0;
    private static final int REPORT_SKIPPED_SILENCE_DELAY_MS = 100;
    private static final String TAG = "DefaultAudioSink";
    public static boolean failOnSpuriousAudioTimestamp;
    private static int pendingReleaseCount;
    private static ScheduledExecutorService releaseExecutor;
    private static final Object releaseExecutorLock = null;
    private long accumulatedSkippedSilenceDurationUs;
    private MediaPositionParameters afterDrainParameters;
    private AudioAttributes audioAttributes;
    private AudioCapabilities audioCapabilities;
    private AudioCapabilitiesReceiver audioCapabilitiesReceiver;
    private final ExoPlayer.AudioOffloadListener audioOffloadListener;
    private final AudioOffloadSupportProvider audioOffloadSupportProvider;
    private AudioProcessingPipeline audioProcessingPipeline;
    private final androidx.media3.common.audio.AudioProcessorChain audioProcessorChain;
    private int audioSessionId;
    private AudioTrack audioTrack;
    private final AudioTrackBufferSizeProvider audioTrackBufferSizeProvider;
    private final AudioTrackPositionTracker audioTrackPositionTracker;
    private final AudioTrackProvider audioTrackProvider;
    private AuxEffectInfo auxEffectInfo;
    private ByteBuffer avSyncHeader;
    private int bytesUntilNextAvSync;
    private final ChannelMappingAudioProcessor channelMappingAudioProcessor;
    private Configuration configuration;
    private final Context context;
    private final boolean enableFloatOutput;
    private boolean externalAudioSessionIdProvided;
    private int framesPerEncodedSample;
    private boolean handledEndOfStream;
    private boolean handledOffloadOnPresentationEnded;
    private final PendingExceptionHolder<AudioSink.InitializationException> initializationExceptionPendingExceptionHolder;
    private ByteBuffer inputBuffer;
    private int inputBufferAccessUnitCount;
    private boolean isWaitingForOffloadEndOfStreamHandled;
    private long lastFeedElapsedRealtimeMs;
    private long lastTunnelingAvSyncPresentationTimeUs;
    private AudioSink.Listener listener;
    private MediaPositionParameters mediaPositionParameters;
    private final ArrayDeque<MediaPositionParameters> mediaPositionParametersCheckpoints;
    private boolean offloadDisabledUntilNextConfiguration;
    private int offloadMode;
    private StreamEventCallbackV29 offloadStreamEventCallbackV29;
    private OnRoutingChangedListenerApi24 onRoutingChangedListener;
    private ByteBuffer outputBuffer;
    private Configuration pendingConfiguration;
    private Looper playbackLooper;
    private PlaybackParameters playbackParameters;
    private PlayerId playerId;
    private boolean playing;
    private final boolean preferAudioTrackPlaybackParams;
    private AudioDeviceInfoApi23 preferredDevice;
    private Handler reportSkippedSilenceHandler;
    private boolean skipSilenceEnabled;
    private long skippedOutputFrameCountAtLastPosition;
    private long startMediaTimeUs;
    private boolean startMediaTimeUsNeedsInit;
    private boolean startMediaTimeUsNeedsSync;
    private boolean stoppedAudioTrack;
    private long submittedEncodedFrames;
    private long submittedPcmBytes;
    private final ImmutableList<AudioProcessor> toFloatPcmAvailableAudioProcessors;
    private final ImmutableList<AudioProcessor> toIntPcmAvailableAudioProcessors;
    private final TrimmingAudioProcessor trimmingAudioProcessor;
    private boolean tunneling;
    private float volume;
    private final PendingExceptionHolder<AudioSink.WriteException> writeExceptionPendingExceptionHolder;
    private long writtenEncodedFrames;
    private long writtenPcmBytes;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.audio.DefaultAudioSink$1 */
    static /* synthetic */ class C02691 {
    }

    private static final class Api23 {
        private Api23() {
        }

        public static void setPreferredDeviceOnAudioTrack(AudioTrack r1, AudioDeviceInfoApi23 r2) {
            if (r2 != null) goto L4;
            AudioDeviceInfo r0 = null;
        L5:
            r1.setPreferredDevice(r0);
            return;
        L4:
            r0 = r2.audioDeviceInfo;
            goto L5
        }
    }

    private static final class Api31 {
        private Api31() {
        }

        public static void setLogSessionIdOnAudioTrack(AudioTrack r2, PlayerId r3) {
            LogSessionId r0 = r3.getLogSessionId();
            if (r0.equals(LogSessionId.LOG_SESSION_ID_NONE) == true) goto L6;
            r2.setLogSessionId(r0);
            return;
        }
    }

    public interface AudioOffloadSupportProvider {
        AudioOffloadSupport getAudioOffloadSupport(Format r1, AudioAttributes r2);
    }

    @Deprecated
    public interface AudioProcessorChain extends androidx.media3.common.audio.AudioProcessorChain {
    }

    public interface AudioTrackBufferSizeProvider {
        public static final AudioTrackBufferSizeProvider DEFAULT = null;

        static {
            DEFAULT = new DefaultAudioTrackBufferSizeProvider.Builder().build();
        }

        int getBufferSizeInBytes(int r1, int r2, int r3, int r4, int r5, int r6, double r7);
    }

    public interface AudioTrackProvider {
        public static final AudioTrackProvider DEFAULT = null;

        static {
            DEFAULT = new DefaultAudioTrackProvider();
        }

        AudioTrack getAudioTrack(AudioSink.AudioTrackConfig r1, AudioAttributes r2, int r3);
    }

    public static final class Builder {
        private AudioCapabilities audioCapabilities;
        private ExoPlayer.AudioOffloadListener audioOffloadListener;
        private AudioOffloadSupportProvider audioOffloadSupportProvider;
        private androidx.media3.common.audio.AudioProcessorChain audioProcessorChain;
        private AudioTrackBufferSizeProvider audioTrackBufferSizeProvider;
        private AudioTrackProvider audioTrackProvider;
        private boolean buildCalled;
        private final Context context;
        private boolean enableAudioTrackPlaybackParams;
        private boolean enableFloatOutput;

        @Deprecated
        public Builder() {
            this.context = null;
            this.audioCapabilities = AudioCapabilities.DEFAULT_AUDIO_CAPABILITIES;
            this.audioTrackBufferSizeProvider = AudioTrackBufferSizeProvider.DEFAULT;
            this.audioTrackProvider = AudioTrackProvider.DEFAULT;
        }

        public Builder(Context r2) {
            this.context = r2;
            this.audioCapabilities = AudioCapabilities.DEFAULT_AUDIO_CAPABILITIES;
            this.audioTrackBufferSizeProvider = AudioTrackBufferSizeProvider.DEFAULT;
            this.audioTrackProvider = AudioTrackProvider.DEFAULT;
        }

        static /* synthetic */ Context access$100(Builder r1) {
            return r1.context;
        }

        static /* synthetic */ ExoPlayer.AudioOffloadListener access$1000(Builder r1) {
            return r1.audioOffloadListener;
        }

        static /* synthetic */ AudioTrackProvider access$1100(Builder r1) {
            return r1.audioTrackProvider;
        }

        static /* synthetic */ AudioCapabilities access$200(Builder r1) {
            return r1.audioCapabilities;
        }

        static /* synthetic */ androidx.media3.common.audio.AudioProcessorChain access$300(Builder r1) {
            return r1.audioProcessorChain;
        }

        static /* synthetic */ boolean access$400(Builder r1) {
            return r1.enableFloatOutput;
        }

        static /* synthetic */ boolean access$500(Builder r1) {
            return r1.enableAudioTrackPlaybackParams;
        }

        static /* synthetic */ AudioTrackBufferSizeProvider access$600(Builder r1) {
            return r1.audioTrackBufferSizeProvider;
        }

        static /* synthetic */ AudioOffloadSupportProvider access$700(Builder r1) {
            return r1.audioOffloadSupportProvider;
        }

        public DefaultAudioSink build() {
            Assertions.checkState(!this.buildCalled);
            this.buildCalled = true;
            if (this.audioProcessorChain != null) goto L6;
            this.audioProcessorChain = new DefaultAudioProcessorChain(new AudioProcessor[0]);
        L6:
            if (this.audioOffloadSupportProvider != null) goto L9;
            this.audioOffloadSupportProvider = new DefaultAudioOffloadSupportProvider(this.context);
        L9:
            return new DefaultAudioSink(this, null);
        }

        @Deprecated
        public Builder setAudioCapabilities(AudioCapabilities r1) {
            Assertions.checkNotNull(r1);
            this.audioCapabilities = r1;
            return this;
        }

        public Builder setAudioOffloadSupportProvider(AudioOffloadSupportProvider r1) {
            this.audioOffloadSupportProvider = r1;
            return this;
        }

        public Builder setAudioProcessorChain(androidx.media3.common.audio.AudioProcessorChain r1) {
            Assertions.checkNotNull(r1);
            this.audioProcessorChain = r1;
            return this;
        }

        public Builder setAudioProcessors(AudioProcessor[] r2) {
            Assertions.checkNotNull(r2);
            return setAudioProcessorChain(new DefaultAudioProcessorChain(r2));
        }

        public Builder setAudioTrackBufferSizeProvider(AudioTrackBufferSizeProvider r1) {
            this.audioTrackBufferSizeProvider = r1;
            return this;
        }

        public Builder setAudioTrackProvider(AudioTrackProvider r1) {
            this.audioTrackProvider = r1;
            return this;
        }

        public Builder setEnableAudioTrackPlaybackParams(boolean r1) {
            this.enableAudioTrackPlaybackParams = r1;
            return this;
        }

        public Builder setEnableFloatOutput(boolean r1) {
            this.enableFloatOutput = r1;
            return this;
        }

        public Builder setExperimentalAudioOffloadListener(ExoPlayer.AudioOffloadListener r1) {
            this.audioOffloadListener = r1;
            return this;
        }
    }

    private static final class Configuration {
        public final AudioProcessingPipeline audioProcessingPipeline;
        public final int bufferSize;
        public final boolean enableAudioTrackPlaybackParams;
        public final boolean enableOffloadGapless;
        public final Format inputFormat;
        public final int inputPcmFrameSize;
        public final int outputChannelConfig;
        public final int outputEncoding;
        public final int outputMode;
        public final int outputPcmFrameSize;
        public final int outputSampleRate;
        public final boolean tunneling;

        public Configuration(Format r1, int r2, int r3, int r4, int r5, int r6, int r7, int r8, AudioProcessingPipeline r9, boolean r10, boolean r11, boolean r12) {
            this.inputFormat = r1;
            this.inputPcmFrameSize = r2;
            this.outputMode = r3;
            this.outputPcmFrameSize = r4;
            this.outputSampleRate = r5;
            this.outputChannelConfig = r6;
            this.outputEncoding = r7;
            this.bufferSize = r8;
            this.audioProcessingPipeline = r9;
            this.enableAudioTrackPlaybackParams = r10;
            this.enableOffloadGapless = r11;
            this.tunneling = r12;
        }

        public AudioSink.AudioTrackConfig buildAudioTrackConfig() {
            int r1 = this.outputEncoding;
            int r2 = this.outputSampleRate;
            int r3 = this.outputChannelConfig;
            boolean r4 = this.tunneling;
            boolean r6 = true;
            if (this.outputMode == 1) goto L7;
            r6 = false;
        L7:
            return new AudioSink.AudioTrackConfig(r1, r2, r3, r4, r6, this.bufferSize);
        }

        public boolean canReuseAudioTrack(Configuration r3) {
            if (r3.outputMode == this.outputMode) goto L5;
        L17:
            return false;
        L5:
            if (r3.outputEncoding != this.outputEncoding) goto L17;
            if (r3.outputSampleRate != this.outputSampleRate) goto L17;
            if (r3.outputChannelConfig != this.outputChannelConfig) goto L17;
            if (r3.outputPcmFrameSize != this.outputPcmFrameSize) goto L17;
            if (r3.enableAudioTrackPlaybackParams != this.enableAudioTrackPlaybackParams) goto L17;
            if (r3.enableOffloadGapless != this.enableOffloadGapless) goto L17;
            return true;
        }

        public Configuration copyWithBufferSize(int r14) {
            return new Configuration(this.inputFormat, this.inputPcmFrameSize, this.outputMode, this.outputPcmFrameSize, this.outputSampleRate, this.outputChannelConfig, this.outputEncoding, r14, this.audioProcessingPipeline, this.enableAudioTrackPlaybackParams, this.enableOffloadGapless, this.tunneling);
        }

        public long framesToDurationUs(long r3) {
            return Util.sampleCountToDurationUs(r3, this.outputSampleRate);
        }

        public long inputFramesToDurationUs(long r3) {
            return Util.sampleCountToDurationUs(r3, this.inputFormat.sampleRate);
        }

        public boolean outputModeIsOffload() {
            if (this.outputMode == 1) goto L7;
            return false;
        L7:
            return true;
        }
    }

    public static class DefaultAudioProcessorChain implements AudioProcessorChain {
        private final AudioProcessor[] audioProcessors;
        private final SilenceSkippingAudioProcessor silenceSkippingAudioProcessor;
        private final androidx.media3.common.audio.SonicAudioProcessor sonicAudioProcessor;

        public DefaultAudioProcessorChain(AudioProcessor... r3) {
            this(r3, new SilenceSkippingAudioProcessor(), new androidx.media3.common.audio.SonicAudioProcessor());
        }

        public DefaultAudioProcessorChain(AudioProcessor[] r4, SilenceSkippingAudioProcessor r5, androidx.media3.common.audio.SonicAudioProcessor r6) {
            this.audioProcessors = new AudioProcessor[r4.length + 2];
            System.arraycopy(r4, 0, this.audioProcessors, 0, r4.length);
            this.silenceSkippingAudioProcessor = r5;
            this.sonicAudioProcessor = r6;
            this.audioProcessors[r4.length] = r5;
            this.audioProcessors[r4.length + 1] = r6;
        }

        @Override // androidx.media3.common.audio.AudioProcessorChain
        public PlaybackParameters applyPlaybackParameters(PlaybackParameters r3) {
            this.sonicAudioProcessor.setSpeed(r3.speed);
            this.sonicAudioProcessor.setPitch(r3.pitch);
            return r3;
        }

        @Override // androidx.media3.common.audio.AudioProcessorChain
        public boolean applySkipSilenceEnabled(boolean r2) {
            this.silenceSkippingAudioProcessor.setEnabled(r2);
            return r2;
        }

        @Override // androidx.media3.common.audio.AudioProcessorChain
        public AudioProcessor[] getAudioProcessors() {
            return this.audioProcessors;
        }

        @Override // androidx.media3.common.audio.AudioProcessorChain
        public long getMediaDuration(long r3) {
            if (this.sonicAudioProcessor.isActive() == false) goto L6;
            return this.sonicAudioProcessor.getMediaDuration(r3);
        L6:
            return r3;
        }

        @Override // androidx.media3.common.audio.AudioProcessorChain
        public long getSkippedOutputFrameCount() {
            return this.silenceSkippingAudioProcessor.getSkippedFrames();
        }
    }

    public static final class InvalidAudioTrackTimestampException extends RuntimeException {
        private InvalidAudioTrackTimestampException(String r1) {
            super(r1);
        }

        /* synthetic */ InvalidAudioTrackTimestampException(String r1, C02691 r2) {
            this(r1);
        }
    }

    private static final class MediaPositionParameters {
        public final long audioTrackPositionUs;
        public long mediaPositionDriftUs;
        public final long mediaTimeUs;
        public final PlaybackParameters playbackParameters;

        private MediaPositionParameters(PlaybackParameters r1, long r2, long r4) {
            this.playbackParameters = r1;
            this.mediaTimeUs = r2;
            this.audioTrackPositionUs = r4;
        }

        /* synthetic */ MediaPositionParameters(PlaybackParameters r1, long r2, long r4, C02691 r6) {
            this(r1, r2, r4);
        }
    }

    private static final class OnRoutingChangedListenerApi24 {
        private final AudioTrack audioTrack;
        private final AudioCapabilitiesReceiver capabilitiesReceiver;
        private AudioRouting.OnRoutingChangedListener listener;

        /* JADX INFO: renamed from: $r8$lambda$iF2hPfQ_z_eejaC3PvZcN-i9wBM, reason: not valid java name */
        public static /* synthetic */ void m471$r8$lambda$iF2hPfQ_z_eejaC3PvZcNi9wBM(OnRoutingChangedListenerApi24 r0, AudioRouting r1) {
            r0.onRoutingChanged(r1);
        }

        public OnRoutingChangedListenerApi24(AudioTrack r3, AudioCapabilitiesReceiver r4) {
            this.audioTrack = r3;
            this.capabilitiesReceiver = r4;
            this.listener = new C0270x609a926e(this);
            Handler r0 = new Handler(Looper.myLooper());
            r3.addOnRoutingChangedListener(this.listener, r0);
        }

        private void onRoutingChanged(AudioRouting r4) {
            if (this.listener != null) goto L6;
            return;
        L6:
            if (r4.getRoutedDevice() == null) goto L9;
            this.capabilitiesReceiver.setRoutedDevice(r4.getRoutedDevice());
            return;
        }

        public void release() {
            this.audioTrack.removeOnRoutingChangedListener((AudioRouting.OnRoutingChangedListener) Assertions.checkNotNull(this.listener));
            this.listener = null;
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface OutputMode {
    }

    private static final class PendingExceptionHolder<T extends Exception> {
        private static final int RETRY_DELAY_MS = 50;
        private static final int RETRY_DURATION_MS = 200;
        private long earliestNextRetryTimeMs;
        private T pendingException;
        private long throwDeadlineMs;

        public PendingExceptionHolder() {
            this.throwDeadlineMs = C0180C.TIME_UNSET;
            this.earliestNextRetryTimeMs = C0180C.TIME_UNSET;
        }

        public void clear() {
            this.pendingException = null;
            this.throwDeadlineMs = C0180C.TIME_UNSET;
            this.earliestNextRetryTimeMs = C0180C.TIME_UNSET;
        }

        public boolean shouldWaitBeforeRetry() {
            if (this.pendingException != null) goto L6;
            return false;
        L6:
            if (DefaultAudioSink.access$2000() == false) goto L9;
            return true;
        L9:
            if (SystemClock.elapsedRealtime() < this.earliestNextRetryTimeMs) goto L11;
            return false;
        L11:
            return true;
        }

        public void throwExceptionIfDeadlineIsReached(T r7) throws Exception {
            long r0 = SystemClock.elapsedRealtime();
            if (this.pendingException != null) goto L6;
            this.pendingException = r7;
        L6:
            if (this.throwDeadlineMs != C0180C.TIME_UNSET) goto L11;
            if (DefaultAudioSink.access$2000() == true) goto L11;
            this.throwDeadlineMs = 200 + r0;
        L11:
            if (this.throwDeadlineMs != C0180C.TIME_UNSET) goto L13;
        L19:
            this.earliestNextRetryTimeMs = 50 + r0;
            return;
        L13:
            if (r0 < this.throwDeadlineMs) goto L19;
            if (this.pendingException == r7) goto L17;
            this.pendingException.addSuppressed(r7);
        L17:
            T r2 = this.pendingException;
            clear();
            throw r2;
        }
    }

    private final class PositionTrackerListener implements AudioTrackPositionTracker.Listener {
        final /* synthetic */ DefaultAudioSink this$0;

        private PositionTrackerListener(DefaultAudioSink r1) {
            this.this$0 = r1;
        }

        /* synthetic */ PositionTrackerListener(DefaultAudioSink r1, C02691 r2) {
            this(r1);
        }

        @Override // androidx.media3.exoplayer.audio.AudioTrackPositionTracker.Listener
        public void onInvalidLatency(long r3) {
            Log.m48w(DefaultAudioSink.TAG, "Ignoring impossibly large audio latency: " + r3);
        }

        @Override // androidx.media3.exoplayer.audio.AudioTrackPositionTracker.Listener
        public void onPositionAdvancing(long r2) {
            if (DefaultAudioSink.access$1300(this.this$0) == null) goto L6;
            DefaultAudioSink.access$1300(this.this$0).onPositionAdvancing(r2);
            return;
        }

        @Override // androidx.media3.exoplayer.audio.AudioTrackPositionTracker.Listener
        public void onPositionFramesMismatch(long r5, long r7, long r9, long r11) {
            String r0 = "Spurious audio timestamp (frame position mismatch): " + r5 + ", " + r7 + ", " + r9 + ", " + r11 + ", " + DefaultAudioSink.access$1600(this.this$0) + ", " + DefaultAudioSink.access$1700(this.this$0);
            if (DefaultAudioSink.failOnSpuriousAudioTimestamp == true) goto L7;
            Log.m48w(DefaultAudioSink.TAG, r0);
            return;
        L7:
            throw new InvalidAudioTrackTimestampException(r0, null);
        }

        @Override // androidx.media3.exoplayer.audio.AudioTrackPositionTracker.Listener
        public void onSystemTimeUsMismatch(long r5, long r7, long r9, long r11) {
            String r0 = "Spurious audio timestamp (system clock mismatch): " + r5 + ", " + r7 + ", " + r9 + ", " + r11 + ", " + DefaultAudioSink.access$1600(this.this$0) + ", " + DefaultAudioSink.access$1700(this.this$0);
            if (DefaultAudioSink.failOnSpuriousAudioTimestamp == true) goto L7;
            Log.m48w(DefaultAudioSink.TAG, r0);
            return;
        L7:
            throw new InvalidAudioTrackTimestampException(r0, null);
        }

        @Override // androidx.media3.exoplayer.audio.AudioTrackPositionTracker.Listener
        public void onUnderrun(int r11, long r12) {
            if (DefaultAudioSink.access$1300(this.this$0) == null) goto L6;
            long r8 = SystemClock.elapsedRealtime() - DefaultAudioSink.access$1900(this.this$0);
            DefaultAudioSink.access$1300(this.this$0).onUnderrun(r11, r12, r8);
            return;
        }
    }

    private final class StreamEventCallbackV29 {
        private final AudioTrack.StreamEventCallback callback;
        private final Handler handler;
        final /* synthetic */ DefaultAudioSink this$0;

        public StreamEventCallbackV29(final DefaultAudioSink r3) {
            this.this$0 = r3;
            this.handler = new Handler(Looper.myLooper());
            this.callback = new C02721(this, r3);
        }

        public void register(AudioTrack r3) {
            Handler r0 = this.handler;
            Objects.requireNonNull(r0);
            r3.registerStreamEventCallback(new ExecutorC0271x59586989(r0), this.callback);
        }

        public void unregister(AudioTrack r3) {
            r3.unregisterStreamEventCallback(this.callback);
            this.handler.removeCallbacksAndMessages(null);
        }
    }

    public static /* synthetic */ void $r8$lambda$eQpBSvvM7qMitmX5MdwoTJRm8SQ(DefaultAudioSink r0) {
        r0.maybeReportSkippedSilence();
    }

    static {
        failOnSpuriousAudioTimestamp = false;
        releaseExecutorLock = new Object();
    }

    @RequiresNonNull({"#1.audioProcessorChain"})
    private DefaultAudioSink(Builder r12) {
        this.context = Builder.access$100(r12);
        this.audioAttributes = AudioAttributes.DEFAULT;
        C02691 r1 = null;
        if (this.context == null) goto L5;
        AudioCapabilities r0 = AudioCapabilities.getCapabilities(this.context, this.audioAttributes, null);
    L6:
        this.audioCapabilities = r0;
        this.audioProcessorChain = Builder.access$300(r12);
        this.enableFloatOutput = Builder.access$400(r12);
        if (Util.SDK_INT >= 23) goto L9;
    L11:
        boolean r2 = false;
    L12:
        this.preferAudioTrackPlaybackParams = r2;
        this.offloadMode = 0;
        this.audioTrackBufferSizeProvider = Builder.access$600(r12);
        this.audioOffloadSupportProvider = (AudioOffloadSupportProvider) Assertions.checkNotNull(Builder.access$700(r12));
        this.audioTrackPositionTracker = new AudioTrackPositionTracker(new PositionTrackerListener(this, r1));
        this.channelMappingAudioProcessor = new ChannelMappingAudioProcessor();
        this.trimmingAudioProcessor = new TrimmingAudioProcessor();
        this.toIntPcmAvailableAudioProcessors = ImmutableList.m215of(new ToInt16PcmAudioProcessor(), this.channelMappingAudioProcessor, this.trimmingAudioProcessor);
        this.toFloatPcmAvailableAudioProcessors = ImmutableList.m213of(new ToFloatPcmAudioProcessor());
        this.volume = 1.0f;
        this.audioSessionId = 0;
        this.auxEffectInfo = new AuxEffectInfo(0, 0.0f);
        this.mediaPositionParameters = new MediaPositionParameters(PlaybackParameters.DEFAULT, 0, 0, null);
        this.playbackParameters = PlaybackParameters.DEFAULT;
        this.skipSilenceEnabled = false;
        this.mediaPositionParametersCheckpoints = new ArrayDeque();
        this.initializationExceptionPendingExceptionHolder = new PendingExceptionHolder();
        this.writeExceptionPendingExceptionHolder = new PendingExceptionHolder();
        this.audioOffloadListener = Builder.access$1000(r12);
        this.audioTrackProvider = Builder.access$1100(r12);
        return;
    L9:
        if (Builder.access$500(r12) == false) goto L11;
        r2 = true;
        goto L12
    L5:
        r0 = Builder.access$200(r12);
        goto L6
    }

    /* synthetic */ DefaultAudioSink(Builder r1, C02691 r2) {
        this(r1);
    }

    static /* synthetic */ AudioTrack access$1200(DefaultAudioSink r1) {
        return r1.audioTrack;
    }

    static /* synthetic */ AudioSink.Listener access$1300(DefaultAudioSink r1) {
        return r1.listener;
    }

    static /* synthetic */ boolean access$1400(DefaultAudioSink r1) {
        return r1.playing;
    }

    static /* synthetic */ boolean access$1502(DefaultAudioSink r0, boolean r1) {
        r0.handledOffloadOnPresentationEnded = r1;
        return r1;
    }

    static /* synthetic */ long access$1600(DefaultAudioSink r2) {
        return r2.getSubmittedFrames();
    }

    static /* synthetic */ long access$1700(DefaultAudioSink r2) {
        return r2.getWrittenFrames();
    }

    static /* synthetic */ long access$1900(DefaultAudioSink r2) {
        return r2.lastFeedElapsedRealtimeMs;
    }

    static /* synthetic */ boolean access$2000() {
        return hasPendingAudioTrackReleases();
    }

    private void applyAudioProcessorPlaybackParametersAndSkipSilence(long r9) {
        if (useAudioTrackPlaybackParams() == false) goto L5;
        PlaybackParameters r2 = PlaybackParameters.DEFAULT;
    L11:
        if (shouldApplyAudioProcessorPlaybackParameters() == false) goto L13;
        boolean r0 = this.audioProcessorChain.applySkipSilenceEnabled(this.skipSilenceEnabled);
    L14:
        this.skipSilenceEnabled = r0;
        this.mediaPositionParametersCheckpoints.add(new MediaPositionParameters(r2, Math.max(0, r9), this.configuration.framesToDurationUs(getWrittenFrames()), null));
        setupAudioProcessors();
        if (this.listener == null) goto L18;
        this.listener.onSkipSilenceEnabledChanged(this.skipSilenceEnabled);
        return;
    L18:
        return;
    L13:
        r0 = false;
        goto L14
    L5:
        if (shouldApplyAudioProcessorPlaybackParameters() == false) goto L7;
        PlaybackParameters r1 = this.audioProcessorChain.applyPlaybackParameters(this.playbackParameters);
    L8:
        this.playbackParameters = r1;
        r2 = this.playbackParameters;
        goto L11
    L7:
        r1 = PlaybackParameters.DEFAULT;
        goto L8
    }

    private long applyMediaPositionParameters(long r12) {
    L3:
        if (this.mediaPositionParametersCheckpoints.isEmpty() == true) goto L7;
        if (r12 < this.mediaPositionParametersCheckpoints.getFirst().audioTrackPositionUs) goto L7;
        this.mediaPositionParameters = this.mediaPositionParametersCheckpoints.remove();
    L7:
        long r0 = r12 - this.mediaPositionParameters.audioTrackPositionUs;
        long r2 = Util.getMediaDurationForPlayoutDuration(r0, this.mediaPositionParameters.playbackParameters.speed);
        if (this.mediaPositionParametersCheckpoints.isEmpty() == false) goto L12;
        long r4 = this.audioProcessorChain.getMediaDuration(r0);
        long r6 = this.mediaPositionParameters.mediaTimeUs + r4;
        long r9 = r4 - r2;
        this.mediaPositionParameters.mediaPositionDriftUs = r9;
        return r6;
    L12:
        return (this.mediaPositionParameters.mediaTimeUs + r2) + this.mediaPositionParameters.mediaPositionDriftUs;
    }

    private long applySkipping(long r8) {
        long r0 = this.audioProcessorChain.getSkippedOutputFrameCount();
        long r2 = this.configuration.framesToDurationUs(r0) + r8;
        if (r0 <= this.skippedOutputFrameCountAtLastPosition) goto L5;
        long r4 = this.configuration.framesToDurationUs(r0 - this.skippedOutputFrameCountAtLastPosition);
        this.skippedOutputFrameCountAtLastPosition = r0;
        handleSkippedSilence(r4);
    L5:
        return r2;
    }

    private AudioTrack buildAudioTrack(AudioSink.AudioTrackConfig r23, AudioAttributes r24, int r25, Format r26) throws AudioSink.InitializationException {
        AudioTrack r0 = this.audioTrackProvider.getAudioTrack(r23, r24, r25);     // Catch: IllegalArgumentException -> L14 UnsupportedOperationException -> L16
        int r7 = r0.getState();
        if (r7 != 1) goto L24;
        return r0;
    L24:
        r0.release();     // Catch: Exception -> L11
    L13:
        throw new AudioSink.InitializationException(r7, r23.sampleRate, r23.channelConfig, r23.encoding, r26, r23.offload, null);
    L14:
        e = e;
    L22:
        Exception r21 = e;
        throw new AudioSink.InitializationException(0, r23.sampleRate, r23.channelConfig, r23.encoding, r26, r23.offload, r21);
    L16:
        e = e;
    L18:
        e = e;
        goto L22
    }

    private AudioTrack buildAudioTrack(Configuration r5) throws AudioSink.InitializationException {
        AudioTrack r0 = buildAudioTrack(r5.buildAudioTrackConfig(), this.audioAttributes, this.audioSessionId, r5.inputFormat);     // Catch: AudioSink.InitializationException -> L6
        if (this.audioOffloadListener == null) goto L5;
        this.audioOffloadListener.onOffloadedPlayback(isOffloadedPlayback(r0));     // Catch: AudioSink.InitializationException -> L6
    L5:
        return r0;
    L6:
        e = move-exception;
        if (this.listener == null) goto L10;
        this.listener.onAudioSinkError(e);
    L10:
        throw e;
    }

    private AudioTrack buildAudioTrackWithRetry() throws AudioSink.InitializationException {
        return buildAudioTrack((Configuration) Assertions.checkNotNull(this.configuration));
    L4:
        e = move-exception;
        if (this.configuration.bufferSize <= 1000000) goto L12;
        Configuration r1 = this.configuration.copyWithBufferSize(1000000);
        AudioTrack r2 = buildAudioTrack(r1);     // Catch: AudioSink.InitializationException -> L10
        this.configuration = r1;     // Catch: AudioSink.InitializationException -> L10
        return r2;
    L10:
        e = move-exception;
        e.addSuppressed(e);
    L12:
        maybeDisableOffload();
        throw e;
    }

    private void drainOutputBuffer(long r12) throws AudioSink.WriteException {
        if (this.outputBuffer != null) goto L6;
        return;
    L6:
        if (this.writeExceptionPendingExceptionHolder.shouldWaitBeforeRetry() == false) goto L8;
        return;
    L8:
        int r4 = this.outputBuffer.remaining();
        boolean r7 = true;
        if (this.tunneling == true) goto L11;
        DefaultAudioSink r1 = this;
        int r13 = writeNonBlocking(r1.audioTrack, r1.outputBuffer, r4);
    L20:
        r1.lastFeedElapsedRealtimeMs = SystemClock.elapsedRealtime();
        if (r13 >= 0) goto L39;
        int r14 = r13;
        boolean r0 = false;
        if (isAudioTrackDeadObject(r14) == true) goto L25;
    L30:
        AudioSink.WriteException r2 = new AudioSink.WriteException(r14, r1.configuration.inputFormat, r0);
        if (r1.listener == null) goto L34;
        r1.listener.onAudioSinkError(r2);
    L34:
        if (r2.isRecoverable == true) goto L37;
        r1.writeExceptionPendingExceptionHolder.throwExceptionIfDeadlineIsReached(r2);
        return;
    L37:
        r1.audioCapabilities = AudioCapabilities.DEFAULT_AUDIO_CAPABILITIES;
        throw r2;
    L25:
        if (getWrittenFrames() <= 0) goto L28;
        r0 = true;
        goto L30
    L28:
        if (isOffloadedPlayback(r1.audioTrack) == false) goto L30;
        maybeDisableOffload();
        r0 = true;
        goto L30
    L39:
        r1.writeExceptionPendingExceptionHolder.clear();
        int r15 = r13;
        if (isOffloadedPlayback(r1.audioTrack) == false) goto L53;
        if (r1.writtenEncodedFrames <= 0) goto L45;
        r1.isWaitingForOffloadEndOfStreamHandled = false;
    L45:
        if (r1.playing == false) goto L53;
        if (r1.listener == null) goto L53;
        if (r15 >= r4) goto L53;
        if (r1.isWaitingForOffloadEndOfStreamHandled == true) goto L53;
        r1.listener.onOffloadBufferFull();
    L53:
        if (r1.configuration.outputMode != 0) goto L55;
        r1.writtenPcmBytes += (long) r15;
    L55:
        if (r15 == r4) goto L57;
        return;
    L57:
        if (r1.configuration.outputMode != 0) goto L59;
    L63:
        r1.outputBuffer = null;
        return;
    L59:
        if (r1.outputBuffer == r1.inputBuffer) goto L62;
        r7 = false;
    L62:
        Assertions.checkState(r7);
        r1.writtenEncodedFrames += ((long) r1.framesPerEncodedSample) * ((long) r1.inputBufferAccessUnitCount);
        goto L63
    L11:
        if (r12 == C0180C.TIME_UNSET) goto L13;
        boolean r3 = true;
    L14:
        Assertions.checkState(r3);
        if (r12 != Long.MIN_VALUE) goto L17;
        long r5 = this.lastTunnelingAvSyncPresentationTimeUs;
    L18:
        r1 = this;
        r13 = r1.writeNonBlockingWithAvSync(this.audioTrack, this.outputBuffer, r4, r5);
        goto L20
    L17:
        this.lastTunnelingAvSyncPresentationTimeUs = r12;
        r5 = r12;
        goto L18
    L13:
        r3 = false;
        goto L14
    }

    private boolean drainToEndOfStream() throws AudioSink.WriteException {
        if (this.audioProcessingPipeline.isOperational() == true) goto L9;
        drainOutputBuffer(Long.MIN_VALUE);
        if (this.outputBuffer != null) goto L8;
        return true;
    L8:
        return false;
    L9:
        this.audioProcessingPipeline.queueEndOfStream();
        processBuffers(Long.MIN_VALUE);
        if (this.audioProcessingPipeline.isEnded() == false) goto L17;
        if (this.outputBuffer != null) goto L14;
        return true;
    L14:
        if (this.outputBuffer.hasRemaining() == true) goto L17;
        return true;
    L17:
        return false;
    }

    private static int getAudioTrackMinBufferSize(int r2, int r3, int r4) {
        int r0 = AudioTrack.getMinBufferSize(r2, r3, r4);
        if (r0 == (-2)) goto L5;
        boolean r1 = true;
    L6:
        Assertions.checkState(r1);
        return r0;
    L5:
        r1 = false;
        goto L6
    }

    private static int getFramesPerEncodedSample(int r3, ByteBuffer r4) {
        switch(r3) {
            case 5: goto L29;
            case 6: goto L29;
            case 7: goto L27;
            case 8: goto L27;
            case 9: goto L21;
            case 10: goto L20;
            case 11: goto L18;
            case 12: goto L18;
            case 13: goto L5;
            case 14: goto L13;
            case 15: goto L11;
            case 16: goto L10;
            case 17: goto L9;
            case 18: goto L29;
            case 19: goto L5;
            case 20: goto L7;
            case 21: goto L5;
            case 22: goto L5;
            case 23: goto L5;
            case 24: goto L5;
            case 25: goto L5;
            case 26: goto L5;
            case 27: goto L5;
            case 28: goto L5;
            case 29: goto L5;
            case 30: goto L27;
            default: goto L5;
        };
    L10:
        return 1024;
    L11:
        return 512;
    L13:
        int r0 = Ac3Util.findTrueHdSyncframeOffset(r4);
        if (r0 != (-1)) goto L17;
        return 0;
    L17:
        return Ac3Util.parseTrueHdSyncframeAudioSampleCount(r4, r0) * 16;
    L18:
        return 2048;
    L20:
        return 1024;
    L21:
        int r2 = MpegAudioUtil.parseMpegAudioFrameSampleCount(Util.getBigEndianInt(r4, r4.position()));
        if (r2 == (-1)) goto L25;
        return r2;
    L25:
        throw new IllegalArgumentException();
    L5:
        throw new IllegalStateException("Unexpected audio encoding: " + r3);
    L7:
        return OpusUtil.parseOggPacketAudioSampleCount(r4);
    L9:
        return Ac4Util.parseAc4SyncframeAudioSampleCount(r4);
    L27:
        return DtsUtil.parseDtsAudioSampleCount(r4);
    L29:
        return Ac3Util.parseAc3SyncframeAudioSampleCount(r4);
    }

    private long getSubmittedFrames() {
        if (this.configuration.outputMode != 0) goto L6;
        return this.submittedPcmBytes / ((long) this.configuration.inputPcmFrameSize);
    L6:
        return this.submittedEncodedFrames;
    }

    private long getWrittenFrames() {
        if (this.configuration.outputMode != 0) goto L6;
        return Util.ceilDivide(this.writtenPcmBytes, this.configuration.outputPcmFrameSize);
    L6:
        return this.writtenEncodedFrames;
    }

    private void handleSkippedSilence(long r5) {
        this.accumulatedSkippedSilenceDurationUs += r5;
        if (this.reportSkippedSilenceHandler != null) goto L5;
        this.reportSkippedSilenceHandler = new Handler(Looper.myLooper());
    L5:
        this.reportSkippedSilenceHandler.removeCallbacksAndMessages(null);
        this.reportSkippedSilenceHandler.postDelayed(new DefaultAudioSink$$ExternalSyntheticLambda1(this), 100);
    }

    private static boolean hasPendingAudioTrackReleases() {
        Object r0 = releaseExecutorLock;
        monitor-enter(r0);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (pendingReleaseCount <= 0) goto L7;
        boolean r1 = true;
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L10
        return r1;
    L7:
        r1 = false;
        goto L8
    }

    private boolean initializeAudioTrack() throws AudioSink.InitializationException {
        if (this.initializationExceptionPendingExceptionHolder.shouldWaitBeforeRetry() == false) goto L5;
        return false;
    L5:
        this.audioTrack = buildAudioTrackWithRetry();
        if (isOffloadedPlayback(this.audioTrack) == false) goto L11;
        registerStreamEventCallbackV29(this.audioTrack);
        if (this.configuration.enableOffloadGapless == false) goto L11;
        this.audioTrack.setOffloadDelayPadding(this.configuration.inputFormat.encoderDelay, this.configuration.inputFormat.encoderPadding);
    L11:
        if (Util.SDK_INT >= 31) goto L13;
    L15:
        this.audioSessionId = this.audioTrack.getAudioSessionId();
        AudioTrackPositionTracker r2 = this.audioTrackPositionTracker;
        AudioTrack r3 = this.audioTrack;
        if (this.configuration.outputMode != 2) goto L18;
        boolean r4 = true;
    L19:
        r2.setAudioTrack(r3, r4, this.configuration.outputEncoding, this.configuration.outputPcmFrameSize, this.configuration.bufferSize);
        setVolumeInternal();
        if (this.auxEffectInfo.effectId == 0) goto L23;
        this.audioTrack.attachAuxEffect(this.auxEffectInfo.effectId);
        this.audioTrack.setAuxEffectSendLevel(this.auxEffectInfo.sendLevel);
    L23:
        if (this.preferredDevice == null) goto L30;
        if (Util.SDK_INT < 23) goto L30;
        Api23.setPreferredDeviceOnAudioTrack(this.audioTrack, this.preferredDevice);
        if (this.audioCapabilitiesReceiver == null) goto L30;
        this.audioCapabilitiesReceiver.setRoutedDevice(this.preferredDevice.audioDeviceInfo);
    L30:
        if (Util.SDK_INT >= 24) goto L32;
    L34:
        this.startMediaTimeUsNeedsInit = true;
        if (this.listener == null) goto L37;
        this.listener.onAudioTrackInitialized(this.configuration.buildAudioTrackConfig());
    L37:
        return true;
    L32:
        if (this.audioCapabilitiesReceiver == null) goto L34;
        this.onRoutingChangedListener = new OnRoutingChangedListenerApi24(this.audioTrack, this.audioCapabilitiesReceiver);
        goto L34
    L18:
        r4 = false;
        goto L19
    L13:
        if (this.playerId == null) goto L15;
        Api31.setLogSessionIdOnAudioTrack(this.audioTrack, this.playerId);
        goto L15
    }

    private static boolean isAudioTrackDeadObject(int r2) {
        if (Util.SDK_INT < 24) goto L7;
        if (r2 != (-6)) goto L7;
    L8:
        return true;
    L7:
        if (r2 == ERROR_NATIVE_DEAD_OBJECT) goto L8;
        return false;
    }

    private boolean isAudioTrackInitialized() {
        if (this.audioTrack == null) goto L5;
        return true;
    L5:
        return false;
    }

    private static boolean isOffloadedPlayback(AudioTrack r2) {
        if (Util.SDK_INT >= 29) goto L5;
    L7:
        return false;
    L5:
        if (r2.isOffloadedPlayback() == false) goto L7;
        return true;
    }

    static /* synthetic */ void lambda$releaseAudioTrackAsync$0(AudioSink.Listener r0, AudioSink.AudioTrackConfig r1) {
        r0.onAudioTrackReleased(r1);
    }

    static /* synthetic */ void lambda$releaseAudioTrackAsync$1(AudioTrack r4, final AudioSink.Listener r5, Handler r6, final AudioSink.AudioTrackConfig r7) {
        r4.flush();     // Catch: Throwable -> L18
        r4.release();     // Catch: Throwable -> L18
        if (r5 != null) goto L6;
    L8:
        Object r1 = releaseExecutorLock;
        monitor-enter(r1);
        pendingReleaseCount--;     // Catch: Throwable -> L15
        if (pendingReleaseCount != 0) goto L13;
        releaseExecutor.shutdown();     // Catch: Throwable -> L15
        releaseExecutor = null;     // Catch: Throwable -> L15
    L13:
        monitor-exit(r1);     // Catch: Throwable -> L15
        return;
    L15:
        th = move-exception;
        throw th;
    L6:
        if (r6.getLooper().getThread().isAlive() == false) goto L8;
        r6.post(new DefaultAudioSink$$ExternalSyntheticLambda3(r5, r7));
    L18:
        th = move-exception;
        if (r5 == null) goto L24;
        if (r6.getLooper().getThread().isAlive() == false) goto L24;
        r6.post(new DefaultAudioSink$$ExternalSyntheticLambda3(r5, r7));
    L24:
        monitor-enter(releaseExecutorLock);
        pendingReleaseCount--;     // Catch: Throwable -> L30
        if (pendingReleaseCount != 0) goto L28;
        releaseExecutor.shutdown();     // Catch: Throwable -> L30
        releaseExecutor = null;     // Catch: Throwable -> L30
    L28:
        throw th;
    L30:
        th = move-exception;
        throw th;
    }

    private void maybeDisableOffload() {
        if (this.configuration.outputModeIsOffload() == true) goto L5;
        return;
    L5:
        this.offloadDisabledUntilNextConfiguration = true;
    }

    private ByteBuffer maybeRampUpVolume(ByteBuffer r9) {
        if (this.configuration.outputMode == 0) goto L5;
        return r9;
    L5:
        int r2 = (int) Util.durationUsToSampleCount(Util.msToUs(20), this.configuration.outputSampleRate);
        long r3 = getWrittenFrames();
        if (r3 < r2) goto L9;
        return r9;
    L9:
        return PcmAudioUtil.rampUpVolume(r9, this.configuration.outputEncoding, this.configuration.outputPcmFrameSize, (int) r3, r2);
    }

    private void maybeReportSkippedSilence() {
        if (this.accumulatedSkippedSilenceDurationUs < 300000) goto L6;
        this.listener.onSilenceSkipped();
        this.accumulatedSkippedSilenceDurationUs = 0;
        return;
    }

    private void maybeStartAudioCapabilitiesReceiver() {
        if (this.audioCapabilitiesReceiver == null) goto L5;
        return;
    L5:
        if (this.context == null) goto L9;
        this.playbackLooper = Looper.myLooper();
        this.audioCapabilitiesReceiver = new AudioCapabilitiesReceiver(this.context, new DefaultAudioSink$$ExternalSyntheticLambda2(this), this.audioAttributes, this.preferredDevice);
        this.audioCapabilities = this.audioCapabilitiesReceiver.register();
        return;
    }

    private void playPendingData() {
        if (this.stoppedAudioTrack == true) goto L9;
        this.stoppedAudioTrack = true;
        this.audioTrackPositionTracker.handleEndOfStream(getWrittenFrames());
        if (isOffloadedPlayback(this.audioTrack) == false) goto L7;
        this.handledOffloadOnPresentationEnded = false;
    L7:
        this.audioTrack.stop();
        this.bytesUntilNextAvSync = 0;
        return;
    }

    private void processBuffers(long r4) throws AudioSink.WriteException {
        drainOutputBuffer(r4);
        if (this.outputBuffer == null) goto L6;
        return;
    L6:
        if (this.audioProcessingPipeline.isOperational() == true) goto L12;
        if (this.inputBuffer == null) goto L32;
        setOutputBuffer(this.inputBuffer);
        drainOutputBuffer(r4);
        return;
    L32:
        return;
    L12:
        if (this.audioProcessingPipeline.isEnded() == true) goto L25;
    L13:
        ByteBuffer r0 = this.audioProcessingPipeline.getOutput();
        if (r0.hasRemaining() == false) goto L19;
        setOutputBuffer(r0);
        drainOutputBuffer(r4);
        if (this.outputBuffer == null) goto L13;
        return;
    L19:
        if (this.inputBuffer == null) goto L24;
        if (this.inputBuffer.hasRemaining() == false) goto L33;
        this.audioProcessingPipeline.queueInput(this.inputBuffer);
        goto L12
    L33:
        return;
    L24:
        return;
    }

    private void registerStreamEventCallbackV29(AudioTrack r2) {
        if (this.offloadStreamEventCallbackV29 != null) goto L5;
        this.offloadStreamEventCallbackV29 = new StreamEventCallbackV29(this);
    L5:
        this.offloadStreamEventCallbackV29.register(r2);
    }

    private static void releaseAudioTrackAsync(final AudioTrack r7, final AudioSink.Listener r8, final AudioSink.AudioTrackConfig r9) {
        final Handler r0 = new Handler(Looper.myLooper());
        Object r1 = releaseExecutorLock;
        monitor-enter(r1);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (releaseExecutor != null) goto L7;
        releaseExecutor = Util.newSingleThreadScheduledExecutor("ExoPlayer:AudioTrackReleaseThread");     // Catch: Throwable -> L10
    L7:
        pendingReleaseCount++;     // Catch: Throwable -> L10
        releaseExecutor.schedule(new DefaultAudioSink$$ExternalSyntheticLambda0(r7, r8, r0, r9), 20, TimeUnit.MILLISECONDS);     // Catch: Throwable -> L10
        monitor-exit(r1);     // Catch: Throwable -> L10
    }

    private void resetSinkStateForFlush() {
        this.submittedPcmBytes = 0;
        this.submittedEncodedFrames = 0;
        this.writtenPcmBytes = 0;
        this.writtenEncodedFrames = 0;
        this.isWaitingForOffloadEndOfStreamHandled = false;
        this.framesPerEncodedSample = 0;
        long r5 = 0;
        this.mediaPositionParameters = new MediaPositionParameters(this.playbackParameters, r5, 0, null);
        this.startMediaTimeUs = 0;
        this.afterDrainParameters = null;
        this.mediaPositionParametersCheckpoints.clear();
        this.inputBuffer = null;
        this.inputBufferAccessUnitCount = 0;
        this.outputBuffer = null;
        this.stoppedAudioTrack = false;
        this.handledEndOfStream = false;
        this.handledOffloadOnPresentationEnded = false;
        this.avSyncHeader = null;
        this.bytesUntilNextAvSync = 0;
        this.trimmingAudioProcessor.resetTrimmedFrameCount();
        setupAudioProcessors();
    }

    private void setAudioProcessorPlaybackParameters(PlaybackParameters r8) {
        long r2 = C0180C.TIME_UNSET;
        MediaPositionParameters r0 = new MediaPositionParameters(r8, r2, C0180C.TIME_UNSET, null);
        if (isAudioTrackInitialized() == false) goto L5;
        this.afterDrainParameters = r0;
        return;
    L5:
        this.mediaPositionParameters = r0;
    }

    private void setAudioTrackPlaybackParametersV23() {
        if (isAudioTrackInitialized() == false) goto L13;
        PlaybackParams r0 = new PlaybackParams().allowDefaults().setSpeed(this.playbackParameters.speed).setPitch(this.playbackParameters.pitch).setAudioFallbackMode(2);
        this.audioTrack.setPlaybackParams(r0);     // Catch: IllegalArgumentException -> L7
    L9:
        this.playbackParameters = new PlaybackParameters(this.audioTrack.getPlaybackParams().getSpeed(), this.audioTrack.getPlaybackParams().getPitch());
        this.audioTrackPositionTracker.setAudioTrackPlaybackSpeed(this.playbackParameters.speed);
        return;
    L7:
        e = move-exception;
        Log.m49w(TAG, "Failed to set playback params", e);
        goto L9
    }

    private void setOutputBuffer(ByteBuffer r2) {
        if (this.outputBuffer != null) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        if (r2.hasRemaining() == true) goto L9;
        return;
    L9:
        this.outputBuffer = maybeRampUpVolume(r2);
        return;
    L5:
        r0 = false;
        goto L6
    }

    private void setVolumeInternal() {
        if (isAudioTrackInitialized() == false) goto L6;
        this.audioTrack.setVolume(this.volume);
        return;
    }

    private void setupAudioProcessors() {
        this.audioProcessingPipeline = this.configuration.audioProcessingPipeline;
        this.audioProcessingPipeline.flush();
    }

    private boolean shouldApplyAudioProcessorPlaybackParameters() {
        if (this.tunneling == false) goto L5;
    L9:
        return false;
    L5:
        if (this.configuration.outputMode != 0) goto L9;
        if (shouldUseFloatOutput(this.configuration.inputFormat.pcmEncoding) == true) goto L9;
        return true;
    }

    private boolean shouldUseFloatOutput(int r2) {
        if (this.enableFloatOutput == true) goto L5;
    L7:
        return false;
    L5:
        if (Util.isEncodingHighResolutionPcm(r2) == false) goto L7;
        return true;
    }

    private boolean useAudioTrackPlaybackParams() {
        if (this.configuration != null) goto L5;
    L9:
        return false;
    L5:
        if (this.configuration.enableAudioTrackPlaybackParams == false) goto L9;
        if (Util.SDK_INT < 23) goto L9;
        return true;
    }

    private static int writeNonBlocking(AudioTrack r1, ByteBuffer r2, int r3) {
        return r1.write(r2, r3, 1);
    }

    private int writeNonBlockingWithAvSync(AudioTrack r11, ByteBuffer r12, int r13, long r14) {
        if (Util.SDK_INT < 26) goto L7;
        return r11.write(r12, r13, 1, r14 * 1000);
    L7:
        if (this.avSyncHeader != null) goto L10;
        this.avSyncHeader = ByteBuffer.allocate(16);
        this.avSyncHeader.order(ByteOrder.BIG_ENDIAN);
        this.avSyncHeader.putInt(1431633921);
    L10:
        if (this.bytesUntilNextAvSync != 0) goto L12;
        this.avSyncHeader.putInt(4, r13);
        this.avSyncHeader.putLong(8, 1000 * r14);
        this.avSyncHeader.position(0);
        this.bytesUntilNextAvSync = r13;
    L12:
        int r15 = this.avSyncHeader.remaining();
        if (r15 <= 0) goto L20;
        int r16 = r11.write(this.avSyncHeader, r15, 1);
        if (r16 >= 0) goto L18;
        this.bytesUntilNextAvSync = 0;
        return r16;
    L18:
        if (r16 >= r15) goto L20;
        return 0;
    L20:
        int r17 = writeNonBlocking(r11, r12, r13);
        if (r17 >= 0) goto L24;
        this.bytesUntilNextAvSync = 0;
        return r17;
    L24:
        this.bytesUntilNextAvSync -= r17;
        return r17;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void configure(Format r26, int r27, int[] r28) throws AudioSink.ConfigurationException {
        maybeStartAudioCapabilitiesReceiver();
        if (MimeTypes.AUDIO_RAW.equals(r26.sampleMimeType) == false) goto L18;
        Assertions.checkArgument(Util.isEncodingLinearPcm(r26.pcmEncoding));
        int r4 = Util.getPcmFrameSize(r26.pcmEncoding, r26.channelCount);
        ImmutableList.Builder r0 = new ImmutableList.Builder();
        if (shouldUseFloatOutput(r26.pcmEncoding) == false) goto L7;
        r0.addAll(this.toFloatPcmAvailableAudioProcessors);
    L8:
        AudioProcessingPipeline r1 = new AudioProcessingPipeline(r0.build());
        if (r1.equals(this.audioProcessingPipeline) == false) goto L11;
        AudioProcessingPipeline r6 = this.audioProcessingPipeline;
    L12:
        this.trimmingAudioProcessor.setTrimFrameCount(r26.encoderDelay, r26.encoderPadding);
        this.channelMappingAudioProcessor.setChannelMap(r28);
        AudioProcessor.AudioFormat r2 = r6.configure(new AudioProcessor.AudioFormat(r26));     // Catch: AudioProcessor.UnhandledAudioFormatException -> L15
        int r8 = r2.encoding;
        int r9 = r2.sampleRate;
        int r10 = Util.getAudioTrackChannelConfig(r2.channelCount);
        AudioProcessingPipeline r11 = r6;
        int r7 = Util.getPcmFrameSize(r8, r2.channelCount);
        boolean r12 = this.preferAudioTrackPlaybackParams;
        boolean r13 = false;
        int r19 = 0;
        int r14 = r9;
        int r15 = r8;
        int r16 = r10;
    L31:
        if (r15 == 0) goto L56;
        if (r16 == 0) goto L54;
        int r3 = r26.bitrate;
        if (MimeTypes.AUDIO_DTS_EXPRESS.equals(r26.sampleMimeType) == false) goto L37;
        if (r3 != (-1)) goto L37;
        int r22 = 768000;
    L38:
        if (r27 == 0) goto L40;
        int r17 = r27;
        int r21 = r14;
        int r18 = r15;
    L48:
        this.offloadDisabledUntilNextConfiguration = false;
        int r5 = r19;
        int r20 = r21;
        Configuration r23 = new Configuration(r26, r4, r5, r7, r20, r16, r18, r17, r11, r12, r13, this.tunneling);
        if (isAudioTrackInitialized() == false) goto L51;
        this.pendingConfiguration = r23;
        return;
    L51:
        this.configuration = r23;
        return;
    L40:
        AudioTrackBufferSizeProvider r24 = this.audioTrackBufferSizeProvider;
        int r110 = getAudioTrackMinBufferSize(r14, r16, r15);
        if (r7 == (-1)) goto L43;
        int r25 = r7;
    L44:
        if (r12 == false) goto L46;
        double r29 = 8.0d;
    L47:
        r21 = r14;
        r18 = r15;
        r17 = r24.getBufferSizeInBytes(r110, r18, r19, r25, r21, r22, r29);
        goto L48
    L46:
        r29 = 1.0d;
        goto L47
    L43:
        r25 = 1;
    L37:
        r22 = r3;
        goto L38
    L54:
        throw new AudioSink.ConfigurationException(new StringBuilder().append("Invalid output channel config (mode=").append(r19).append(") for: ").append(r26).toString(), r26);
    L56:
        throw new AudioSink.ConfigurationException(new StringBuilder().append("Invalid output encoding (mode=").append(r19).append(") for: ").append(r26).toString(), r26);
    L15:
        e = move-exception;
        throw new AudioSink.ConfigurationException(e, r26);
    L11:
        r6 = r1;
        goto L12
    L7:
        r0.addAll(this.toIntPcmAvailableAudioProcessors);
        r0.add(this.audioProcessorChain.getAudioProcessors());
        goto L8
    L18:
        AudioProcessingPipeline r30 = new AudioProcessingPipeline(ImmutableList.m212of());
        r4 = -1;
        int r31 = r26.sampleRate;
        if (this.offloadMode == 0) goto L21;
        AudioOffloadSupport r32 = getFormatOffloadSupport(r26);
    L23:
        if (this.offloadMode != 0) goto L25;
    L27:
        Pair<Integer, Integer> r33 = this.audioCapabilities.getEncodingAndChannelConfigForPassthrough(r26, this.audioAttributes);
        if (r33 == null) goto L58;
        int r34 = ((Integer) r33.first).intValue();
        int r111 = ((Integer) r33.second).intValue();
        r12 = this.preferAudioTrackPlaybackParams;
        r11 = r30;
        r7 = -1;
        r13 = false;
        r19 = 2;
        r14 = r31;
        r15 = r34;
        r16 = r111;
        goto L31
    L58:
        throw new AudioSink.ConfigurationException("Unable to configure passthrough for: " + r26, r26);
    L25:
        if (r32.isFormatSupported == false) goto L27;
        int r35 = MimeTypes.getEncoding((String) Assertions.checkNotNull(r26.sampleMimeType), r26.codecs);
        int r112 = Util.getAudioTrackChannelConfig(r26.channelCount);
        r11 = r30;
        r7 = -1;
        r13 = r32.isGaplessSupported;
        r12 = true;
        r19 = 1;
        r14 = r31;
        r15 = r35;
        r16 = r112;
        goto L31
    L21:
        r32 = AudioOffloadSupport.DEFAULT_UNSUPPORTED;
        goto L23
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void disableTunneling() {
        if (this.tunneling == false) goto L6;
        this.tunneling = false;
        flush();
        return;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void enableTunnelingV21() {
        Assertions.checkState(this.externalAudioSessionIdProvided);
        if (this.tunneling == true) goto L6;
        this.tunneling = true;
        flush();
        return;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void flush() {
        if (isAudioTrackInitialized() == false) goto L19;
        resetSinkStateForFlush();
        if (this.audioTrackPositionTracker.isPlaying() == false) goto L8;
        this.audioTrack.pause();
    L8:
        if (isOffloadedPlayback(this.audioTrack) == false) goto L10;
        ((StreamEventCallbackV29) Assertions.checkNotNull(this.offloadStreamEventCallbackV29)).unregister(this.audioTrack);
    L10:
        AudioSink.AudioTrackConfig r0 = this.configuration.buildAudioTrackConfig();
        if (this.pendingConfiguration == null) goto L13;
        this.configuration = this.pendingConfiguration;
        this.pendingConfiguration = null;
    L13:
        this.audioTrackPositionTracker.reset();
        if (Util.SDK_INT >= 24) goto L16;
    L18:
        releaseAudioTrackAsync(this.audioTrack, this.listener, r0);
        this.audioTrack = null;
        goto L19
    L16:
        if (this.onRoutingChangedListener == null) goto L18;
        this.onRoutingChangedListener.release();
        this.onRoutingChangedListener = null;
    L19:
        this.writeExceptionPendingExceptionHolder.clear();
        this.initializationExceptionPendingExceptionHolder.clear();
        this.skippedOutputFrameCountAtLastPosition = 0;
        this.accumulatedSkippedSilenceDurationUs = 0;
        if (this.reportSkippedSilenceHandler == null) goto L23;
        ((Handler) Assertions.checkNotNull(this.reportSkippedSilenceHandler)).removeCallbacksAndMessages(null);
        return;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public AudioAttributes getAudioAttributes() {
        return this.audioAttributes;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public long getCurrentPositionUs(boolean r6) {
        if (isAudioTrackInitialized() == true) goto L5;
        return Long.MIN_VALUE;
    L5:
        if (this.startMediaTimeUsNeedsInit == false) goto L8;
        return Long.MIN_VALUE;
    L8:
        return applySkipping(applyMediaPositionParameters(Math.min(this.audioTrackPositionTracker.getCurrentPositionUs(r6), this.configuration.framesToDurationUs(getWrittenFrames()))));
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public AudioOffloadSupport getFormatOffloadSupport(Format r3) {
        if (this.offloadDisabledUntilNextConfiguration == false) goto L7;
        return AudioOffloadSupport.DEFAULT_UNSUPPORTED;
    L7:
        return this.audioOffloadSupportProvider.getAudioOffloadSupport(r3, this.audioAttributes);
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public int getFormatSupport(Format r5) {
        maybeStartAudioCapabilitiesReceiver();
        if (MimeTypes.AUDIO_RAW.equals(r5.sampleMimeType) == false) goto L19;
        if (Util.isEncodingLinearPcm(r5.pcmEncoding) == true) goto L9;
        Log.m48w(TAG, "Invalid PCM encoding: " + r5.pcmEncoding);
        return 0;
    L9:
        if (r5.pcmEncoding != 2) goto L11;
    L17:
        return 2;
    L11:
        if (this.enableFloatOutput == true) goto L13;
        return 1;
    L13:
        if (r5.pcmEncoding == 4) goto L17;
        return 1;
    L19:
        if (this.audioCapabilities.isPassthroughPlaybackSupported(r5, this.audioAttributes) == false) goto L21;
        return 2;
    L21:
        return 0;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public PlaybackParameters getPlaybackParameters() {
        return this.playbackParameters;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public boolean getSkipSilenceEnabled() {
        return this.skipSilenceEnabled;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public boolean handleBuffer(ByteBuffer r18, long r19, int r21) throws AudioSink.InitializationException, AudioSink.WriteException {
        boolean r6 = true;
        if (this.inputBuffer != null) goto L5;
    L8:
        boolean r0 = true;
    L9:
        Assertions.checkArgument(r0);
        if (this.pendingConfiguration == null) goto L32;
        if (drainToEndOfStream() == true) goto L15;
        return false;
    L15:
        if (this.pendingConfiguration.canReuseAudioTrack(this.configuration) == true) goto L20;
        playPendingData();
        if (hasPendingData() == false) goto L19;
        return false;
    L19:
        flush();
    L30:
        applyAudioProcessorPlaybackParametersAndSkipSilence(r19);
        goto L32
    L20:
        this.configuration = this.pendingConfiguration;
        this.pendingConfiguration = null;
        if (this.audioTrack == null) goto L30;
        if (isOffloadedPlayback(this.audioTrack) == false) goto L30;
        if (this.configuration.enableOffloadGapless == false) goto L30;
        if (this.audioTrack.getPlayState() != 3) goto L29;
        this.audioTrack.setOffloadEndOfStream();
        this.audioTrackPositionTracker.expectRawPlaybackHeadReset();
    L29:
        this.audioTrack.setOffloadDelayPadding(this.configuration.inputFormat.encoderDelay, this.configuration.inputFormat.encoderPadding);
        this.isWaitingForOffloadEndOfStreamHandled = true;
    L32:
        if (isAudioTrackInitialized() == false) goto L110;
    L43:
        this.initializationExceptionPendingExceptionHolder.clear();
        if (this.startMediaTimeUsNeedsInit == false) goto L52;
        this.startMediaTimeUs = Math.max(0, r19);
        this.startMediaTimeUsNeedsSync = false;
        this.startMediaTimeUsNeedsInit = false;
        if (useAudioTrackPlaybackParams() == false) goto L48;
        setAudioTrackPlaybackParametersV23();
    L48:
        applyAudioProcessorPlaybackParametersAndSkipSilence(r19);
        if (this.playing == false) goto L52;
        play();
    L52:
        if (this.audioTrackPositionTracker.mayHandleBuffer(getWrittenFrames()) == true) goto L55;
        return false;
    L55:
        if (this.inputBuffer == null) goto L57;
        r6 = true;
    L100:
        processBuffers(r19);
        if (this.inputBuffer.hasRemaining() == true) goto L105;
        this.inputBuffer = null;
        this.inputBufferAccessUnitCount = 0;
        return r6;
    L105:
        if (this.audioTrackPositionTracker.isStalled(getWrittenFrames()) == false) goto L108;
        Log.m48w(TAG, "Resetting stalled audio track");
        flush();
        return r6;
    L108:
        return false;
    L57:
        if (r18.order() != ByteOrder.LITTLE_ENDIAN) goto L59;
        boolean r1 = true;
    L60:
        Assertions.checkArgument(r1);
        if (r18.hasRemaining() == true) goto L64;
        return true;
    L64:
        if (this.configuration.outputMode == 0) goto L71;
        if (this.framesPerEncodedSample != 0) goto L71;
        this.framesPerEncodedSample = getFramesPerEncodedSample(this.configuration.outputEncoding, r18);
        if (this.framesPerEncodedSample != 0) goto L71;
        return true;
    L71:
        if (this.afterDrainParameters != null) goto L73;
    L76:
        long r11 = this.startMediaTimeUs + this.configuration.inputFramesToDurationUs(getSubmittedFrames() - this.trimmingAudioProcessor.getTrimmedFrameCount());
        if (this.startMediaTimeUsNeedsSync == true) goto L85;
        if (Math.abs(r11 - r19) <= 200000) goto L85;
        if (this.listener == null) goto L83;
        this.listener.onAudioSinkError(new AudioSink.UnexpectedDiscontinuityException(r19, r11));
    L83:
        this.startMediaTimeUsNeedsSync = true;
    L85:
        if (this.startMediaTimeUsNeedsSync == false) goto L95;
        if (drainToEndOfStream() == true) goto L89;
        return false;
    L89:
        long r13 = r19 - r11;
        this.startMediaTimeUs += r13;
        this.startMediaTimeUsNeedsSync = false;
        applyAudioProcessorPlaybackParametersAndSkipSilence(r19);
        if (this.listener == null) goto L95;
        if (r13 == 0) goto L95;
        this.listener.onPositionDiscontinuity();
    L95:
        if (this.configuration.outputMode != 0) goto L97;
        this.submittedPcmBytes += (long) r18.remaining();
    L98:
        this.inputBuffer = r18;
        this.inputBufferAccessUnitCount = r21;
        goto L100
    L97:
        this.submittedEncodedFrames += ((long) this.framesPerEncodedSample) * ((long) r21);
        goto L98
    L73:
        if (drainToEndOfStream() == true) goto L75;
        return false;
    L75:
        applyAudioProcessorPlaybackParametersAndSkipSilence(r19);
        this.afterDrainParameters = null;
        goto L76
    L59:
        r1 = false;
        goto L60
    L110:
        if (initializeAudioTrack() == true) goto L43;
        return false;
    L37:
        e = move-exception;
        if (e.isRecoverable == true) goto L42;
        this.initializationExceptionPendingExceptionHolder.throwExceptionIfDeadlineIsReached(e);
        return false;
    L42:
        throw e;
    L5:
        if (r18 == this.inputBuffer) goto L8;
        r0 = false;
        goto L9
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void handleDiscontinuity() {
        this.startMediaTimeUsNeedsSync = true;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public boolean hasPendingData() {
        if (isAudioTrackInitialized() == true) goto L5;
    L13:
        return false;
    L5:
        if (Util.SDK_INT < 29) goto L11;
        if (this.audioTrack.isOffloadedPlayback() == false) goto L11;
        if (this.handledOffloadOnPresentationEnded == true) goto L13;
    L11:
        if (this.audioTrackPositionTracker.hasPendingData(getWrittenFrames()) == false) goto L13;
        return true;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public boolean isEnded() {
        if (isAudioTrackInitialized() == true) goto L5;
    L10:
        return true;
    L5:
        if (this.handledEndOfStream == true) goto L7;
    L9:
        return false;
    L7:
        if (hasPendingData() == true) goto L9;
        goto L9
    }

    public void onAudioCapabilitiesChanged(AudioCapabilities r7) {
        Looper r0 = Looper.myLooper();
        if (this.playbackLooper == r0) goto L14;
        String r2 = "null";
        if (this.playbackLooper != null) goto L7;
        String r1 = "null";
    L8:
        if (r0 == null) goto L12;
        r2 = r0.getThread().getName();
    L12:
        throw new IllegalStateException("Current looper (" + r2 + ") is not the playback looper (" + r1 + ")");
    L7:
        r1 = this.playbackLooper.getThread().getName();
        goto L8
    L14:
        if (r7.equals(this.audioCapabilities) == true) goto L19;
        this.audioCapabilities = r7;
        if (this.listener == null) goto L20;
        this.listener.onAudioCapabilitiesChanged();
        return;
    L20:
        return;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void pause() {
        this.playing = false;
        if (isAudioTrackInitialized() == true) goto L5;
        return;
    L5:
        if (this.audioTrackPositionTracker.pause() == false) goto L7;
    L8:
        this.audioTrack.pause();
        return;
    L7:
        if (isOffloadedPlayback(this.audioTrack) == true) goto L8;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void play() {
        this.playing = true;
        if (isAudioTrackInitialized() == false) goto L6;
        this.audioTrackPositionTracker.start();
        this.audioTrack.play();
        return;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void playToEndOfStream() throws AudioSink.WriteException {
        if (this.handledEndOfStream == false) goto L5;
        return;
    L5:
        if (isAudioTrackInitialized() == true) goto L7;
        return;
    L7:
        if (drainToEndOfStream() == false) goto L12;
        playPendingData();
        this.handledEndOfStream = true;
        return;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void release() {
        if (this.audioCapabilitiesReceiver == null) goto L6;
        this.audioCapabilitiesReceiver.unregister();
        return;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void reset() {
        flush();
        UnmodifiableIterator<AudioProcessor> r0 = this.toIntPcmAvailableAudioProcessors.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().reset();
        goto L4
    L6:
        UnmodifiableIterator<AudioProcessor> r1 = this.toFloatPcmAvailableAudioProcessors.iterator();
    L8:
        if (r1.hasNext() == false) goto L11;
        r1.next().reset();
        goto L8
    L11:
        if (this.audioProcessingPipeline == null) goto L13;
        this.audioProcessingPipeline.reset();
    L13:
        this.playing = false;
        this.offloadDisabledUntilNextConfiguration = false;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void setAudioAttributes(AudioAttributes r2) {
        if (this.audioAttributes.equals(r2) == false) goto L5;
        return;
    L5:
        this.audioAttributes = r2;
        if (this.tunneling == false) goto L9;
        return;
    L9:
        if (this.audioCapabilitiesReceiver == null) goto L11;
        this.audioCapabilitiesReceiver.setAudioAttributes(r2);
    L11:
        flush();
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void setAudioSessionId(int r2) {
        if (this.audioSessionId == r2) goto L10;
        this.audioSessionId = r2;
        if (r2 == 0) goto L7;
        boolean r0 = true;
    L8:
        this.externalAudioSessionIdProvided = r0;
        flush();
        return;
    L7:
        r0 = false;
        goto L8
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void setAuxEffectInfo(AuxEffectInfo r4) {
        if (this.auxEffectInfo.equals(r4) == false) goto L5;
        return;
    L5:
        int r0 = r4.effectId;
        float r1 = r4.sendLevel;
        if (this.audioTrack != null) goto L8;
    L12:
        this.auxEffectInfo = r4;
        return;
    L8:
        if (this.auxEffectInfo.effectId == r0) goto L10;
        this.audioTrack.attachAuxEffect(r0);
    L10:
        if (r0 == 0) goto L12;
        this.audioTrack.setAuxEffectSendLevel(r1);
        goto L12
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void setClock(Clock r2) {
        this.audioTrackPositionTracker.setClock(r2);
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void setListener(AudioSink.Listener r1) {
        this.listener = r1;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void setOffloadDelayPadding(int r2, int r3) {
        if (this.audioTrack != null) goto L5;
        return;
    L5:
        if (isOffloadedPlayback(this.audioTrack) == true) goto L7;
        return;
    L7:
        if (this.configuration != null) goto L9;
        return;
    L9:
        if (this.configuration.enableOffloadGapless == false) goto L15;
        this.audioTrack.setOffloadDelayPadding(r2, r3);
        return;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void setOffloadMode(int r3) {
        if (Util.SDK_INT < 29) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        this.offloadMode = r3;
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public /* synthetic */ void setOutputStreamOffsetUs(long r1) {
        AudioSink.CC.$default$setOutputStreamOffsetUs(this, r1);
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void setPlaybackParameters(PlaybackParameters r6) {
        this.playbackParameters = new PlaybackParameters(Util.constrainValue(r6.speed, 0.1f, 8.0f), Util.constrainValue(r6.pitch, 0.1f, 8.0f));
        if (useAudioTrackPlaybackParams() == false) goto L5;
        setAudioTrackPlaybackParametersV23();
        return;
    L5:
        setAudioProcessorPlaybackParameters(r6);
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void setPlayerId(PlayerId r1) {
        this.playerId = r1;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void setPreferredDevice(AudioDeviceInfo r3) {
        if (r3 != null) goto L4;
        AudioDeviceInfoApi23 r0 = null;
    L5:
        this.preferredDevice = r0;
        if (this.audioCapabilitiesReceiver == null) goto L9;
        this.audioCapabilitiesReceiver.setRoutedDevice(r3);
    L9:
        if (this.audioTrack == null) goto L12;
        Api23.setPreferredDeviceOnAudioTrack(this.audioTrack, this.preferredDevice);
        return;
    L12:
        return;
    L4:
        r0 = new AudioDeviceInfoApi23(r3);
        goto L5
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void setSkipSilenceEnabled(boolean r2) {
        this.skipSilenceEnabled = r2;
        if (useAudioTrackPlaybackParams() == false) goto L5;
        PlaybackParameters r0 = PlaybackParameters.DEFAULT;
    L6:
        setAudioProcessorPlaybackParameters(r0);
        return;
    L5:
        r0 = this.playbackParameters;
        goto L6
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public void setVolume(float r2) {
        if (this.volume == r2) goto L6;
        this.volume = r2;
        setVolumeInternal();
        return;
    }

    @Override // androidx.media3.exoplayer.audio.AudioSink
    public boolean supportsFormat(Format r2) {
        if (getFormatSupport(r2) == 0) goto L5;
        return true;
    L5:
        return false;
    }
}
