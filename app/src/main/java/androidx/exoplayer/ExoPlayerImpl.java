package androidx.media3.exoplayer;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MediaFormat;
import android.media.metrics.LogSessionId;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.AuxEffectInfo;
import androidx.media3.common.BasePlayer;
import androidx.media3.common.C0180C;
import androidx.media3.common.DeviceInfo;
import androidx.media3.common.Effect;
import androidx.media3.common.FlagSet;
import androidx.media3.common.Format;
import androidx.media3.common.IllegalSeekPositionException;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaLibraryInfo;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.PriorityTaskManager;
import androidx.media3.common.SimpleBasePlayer$$ExternalSyntheticLambda10;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.Tracks;
import androidx.media3.common.VideoFrameProcessor;
import androidx.media3.common.VideoSize;
import androidx.media3.common.text.Cue;
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.ConditionVariable;
import androidx.media3.common.util.HandlerWrapper;
import androidx.media3.common.util.ListenerSet;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Size;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.analytics.AnalyticsCollector;
import androidx.media3.exoplayer.analytics.AnalyticsListener;
import androidx.media3.exoplayer.analytics.DefaultAnalyticsCollector;
import androidx.media3.exoplayer.analytics.MediaMetricsListener;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.audio.AudioRendererEventListener;
import androidx.media3.exoplayer.audio.AudioSink;
import androidx.media3.exoplayer.image.ImageOutput;
import androidx.media3.exoplayer.metadata.MetadataOutput;
import androidx.media3.exoplayer.source.MaskingMediaSource;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ShuffleOrder;
import androidx.media3.exoplayer.source.TimelineWithUpdatedMediaItem;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.text.TextOutput;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.trackselection.TrackSelectionArray;
import androidx.media3.exoplayer.trackselection.TrackSelector;
import androidx.media3.exoplayer.trackselection.TrackSelectorResult;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import androidx.media3.exoplayer.video.VideoDecoderOutputBufferRenderer;
import androidx.media3.exoplayer.video.VideoFrameMetadataListener;
import androidx.media3.exoplayer.video.VideoRendererEventListener;
import androidx.media3.exoplayer.video.spherical.CameraMotionListener;
import androidx.media3.exoplayer.video.spherical.SphericalGLSurfaceView;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
final class ExoPlayerImpl extends BasePlayer implements ExoPlayer, ExoPlayer.AudioComponent, ExoPlayer.VideoComponent, ExoPlayer.TextComponent, ExoPlayer.DeviceComponent {
    private static final String TAG = "ExoPlayerImpl";
    private final AnalyticsCollector analyticsCollector;
    private final Context applicationContext;
    private final Looper applicationLooper;
    private AudioAttributes audioAttributes;
    private final AudioBecomingNoisyManager audioBecomingNoisyManager;
    private DecoderCounters audioDecoderCounters;
    private final AudioFocusManager audioFocusManager;
    private Format audioFormat;
    private AudioManager audioManager;
    private final CopyOnWriteArraySet<ExoPlayer.AudioOffloadListener> audioOffloadListeners;
    private int audioSessionId;
    private Player.Commands availableCommands;
    private final BandwidthMeter bandwidthMeter;
    private CameraMotionListener cameraMotionListener;
    private final Clock clock;
    private final ComponentListener componentListener;
    private final ConditionVariable constructorFinished;
    private CueGroup currentCueGroup;
    private final long detachSurfaceTimeoutMs;
    private DeviceInfo deviceInfo;
    final TrackSelectorResult emptyTrackSelectorResult;
    private boolean foregroundMode;
    private final FrameMetadataListener frameMetadataListener;
    private boolean hasNotifiedFullWrongThreadWarning;
    private final ExoPlayerImplInternal internalPlayer;
    private boolean isPriorityTaskManagerRegistered;
    private final ListenerSet<Player.Listener> listeners;
    private int maskingPeriodIndex;
    private int maskingWindowIndex;
    private long maskingWindowPositionMs;
    private final long maxSeekToPreviousPositionMs;
    private MediaMetadata mediaMetadata;
    private final MediaSource.Factory mediaSourceFactory;
    private final List<MediaSourceHolderSnapshot> mediaSourceHolderSnapshots;
    private Surface ownedSurface;
    private boolean pauseAtEndOfMediaItems;
    private boolean pendingDiscontinuity;
    private int pendingDiscontinuityReason;
    private int pendingOperationAcks;
    private final Timeline.Period period;
    final Player.Commands permanentAvailableCommands;
    private PlaybackInfo playbackInfo;
    private final HandlerWrapper playbackInfoUpdateHandler;
    private final ExoPlayerImplInternal.PlaybackInfoUpdateListener playbackInfoUpdateListener;
    private boolean playerReleased;
    private MediaMetadata playlistMetadata;
    private ExoPlayer.PreloadConfiguration preloadConfiguration;
    private int priority;
    private PriorityTaskManager priorityTaskManager;
    private final Renderer[] renderers;
    private int repeatMode;
    private final long seekBackIncrementMs;
    private final long seekForwardIncrementMs;
    private SeekParameters seekParameters;
    private boolean shuffleModeEnabled;
    private ShuffleOrder shuffleOrder;
    private boolean skipSilenceEnabled;
    private SphericalGLSurfaceView sphericalGLSurfaceView;
    private MediaMetadata staticAndDynamicMediaMetadata;
    private final StreamVolumeManager streamVolumeManager;
    private final SuitableOutputChecker suitableOutputChecker;
    private final boolean suppressPlaybackOnUnsuitableOutput;
    private SurfaceHolder surfaceHolder;
    private boolean surfaceHolderSurfaceIsVideoOutput;
    private Size surfaceSize;
    private TextureView textureView;
    private boolean throwsWhenUsingWrongThread;
    private final TrackSelector trackSelector;
    private final boolean useLazyPreparation;
    private int videoChangeFrameRateStrategy;
    private DecoderCounters videoDecoderCounters;
    private Format videoFormat;
    private VideoFrameMetadataListener videoFrameMetadataListener;
    private Object videoOutput;
    private int videoScalingMode;
    private VideoSize videoSize;
    private float volume;
    private final WakeLockManager wakeLockManager;
    private final WifiLockManager wifiLockManager;
    private final Player wrappingPlayer;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.ExoPlayerImpl$1 */
    static /* synthetic */ class C02321 {
    }

    private static final class Api23 {
        private Api23() {
        }

        public static boolean isSuitableExternalAudioOutputPresentInAudioDeviceInfoList(Context r8, AudioDeviceInfo[] r9) {
            if (Util.isWear(r8) == true) goto L5;
            return true;
        L5:
            int r0 = r9.length;
            int r3 = 0;
        L6:
            if (r3 >= r0) goto L44;
            AudioDeviceInfo r4 = r9[r3];
            if (r4.getType() == 8) goto L43;
            if (r4.getType() == 5) goto L43;
            if (r4.getType() == 6) goto L43;
            if (r4.getType() == 11) goto L43;
            if (r4.getType() == 4) goto L43;
            if (r4.getType() == 3) goto L43;
            if (Util.SDK_INT < 26) goto L26;
            if (r4.getType() != 22) goto L26;
            return true;
        L26:
            if (Util.SDK_INT < 28) goto L31;
            if (r4.getType() != 23) goto L31;
            return true;
        L31:
            if (Util.SDK_INT < 31) goto L38;
            if (r4.getType() == 26) goto L36;
            if (r4.getType() != 27) goto L38;
        L36:
            return true;
        L38:
            if (Util.SDK_INT < 33) goto L42;
            if (r4.getType() != 30) goto L42;
            return true;
        L42:
            r3 = r3 + 1;
        L43:
            return true;
        L44:
            return false;
        }

        public static void registerAudioDeviceCallback(AudioManager r0, AudioDeviceCallback r1, Handler r2) {
            r0.registerAudioDeviceCallback(r1, r2);
        }
    }

    private static final class Api31 {
        private Api31() {
        }

        public static PlayerId registerMediaMetricsListener(Context r3, ExoPlayerImpl r4, boolean r5, String r6) {
            MediaMetricsListener r0 = MediaMetricsListener.create(r3);
            if (r0 != null) goto L6;
            Log.m48w(ExoPlayerImpl.TAG, "MediaMetricsService unavailable.");
            return new PlayerId(LogSessionId.LOG_SESSION_ID_NONE, r6);
        L6:
            if (r5 == false) goto L9;
            r4.addAnalyticsListener(r0);
        L9:
            return new PlayerId(r0.getLogSessionId(), r6);
        }
    }

    private final class ComponentListener implements VideoRendererEventListener, AudioRendererEventListener, TextOutput, MetadataOutput, SurfaceHolder.Callback, TextureView.SurfaceTextureListener, SphericalGLSurfaceView.VideoSurfaceListener, AudioFocusManager.PlayerControl, AudioBecomingNoisyManager.EventListener, StreamVolumeManager.Listener, ExoPlayer.AudioOffloadListener {
        final /* synthetic */ ExoPlayerImpl this$0;

        private ComponentListener(ExoPlayerImpl r1) {
            this.this$0 = r1;
        }

        /* synthetic */ ComponentListener(ExoPlayerImpl r1, C02321 r2) {
            this(r1);
        }

        static /* synthetic */ void lambda$onCues$2(List r0, Player.Listener r1) {
            r1.onCues(r0);
        }

        static /* synthetic */ void lambda$onCues$3(CueGroup r0, Player.Listener r1) {
            r1.onCues(r0);
        }

        static /* synthetic */ void lambda$onMetadata$5(Metadata r0, Player.Listener r1) {
            r1.onMetadata(r0);
        }

        static /* synthetic */ void lambda$onSkipSilenceEnabledChanged$1(boolean r0, Player.Listener r1) {
            r1.onSkipSilenceEnabledChanged(r0);
        }

        static /* synthetic */ void lambda$onStreamTypeChanged$6(DeviceInfo r0, Player.Listener r1) {
            r1.onDeviceInfoChanged(r0);
        }

        static /* synthetic */ void lambda$onStreamVolumeChanged$7(int r0, boolean r1, Player.Listener r2) {
            r2.onDeviceVolumeChanged(r0, r1);
        }

        static /* synthetic */ void lambda$onVideoSizeChanged$0(VideoSize r0, Player.Listener r1) {
            r1.onVideoSizeChanged(r0);
        }

        @Override // androidx.media3.exoplayer.AudioFocusManager.PlayerControl
        public void executePlayerCommand(int r4) {
            boolean r0 = this.this$0.getPlayWhenReady();
            ExoPlayerImpl.access$2300(this.this$0, r0, r4, ExoPlayerImpl.access$2200(r4));
        }

        /* JADX INFO: renamed from: lambda$onMetadata$4$androidx-media3-exoplayer-ExoPlayerImpl$ComponentListener */
        /* synthetic */ void m61xb185137(Player.Listener r2) {
            r2.onMediaMetadataChanged(ExoPlayerImpl.access$1600(this.this$0));
        }

        @Override // androidx.media3.exoplayer.AudioBecomingNoisyManager.EventListener
        public void onAudioBecomingNoisy() {
            ExoPlayerImpl.access$2300(this.this$0, false, -1, 3);
        }

        @Override // androidx.media3.exoplayer.audio.AudioRendererEventListener
        public void onAudioCodecError(Exception r2) {
            ExoPlayerImpl.access$500(this.this$0).onAudioCodecError(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioRendererEventListener
        public void onAudioDecoderInitialized(String r8, long r9, long r11) {
            ExoPlayerImpl.access$500(this.this$0).onAudioDecoderInitialized(r8, r9, r11);
        }

        @Override // androidx.media3.exoplayer.audio.AudioRendererEventListener
        public void onAudioDecoderReleased(String r2) {
            ExoPlayerImpl.access$500(this.this$0).onAudioDecoderReleased(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioRendererEventListener
        public void onAudioDisabled(DecoderCounters r3) {
            ExoPlayerImpl.access$500(this.this$0).onAudioDisabled(r3);
            ExoPlayerImpl.access$1102(this.this$0, null);
            ExoPlayerImpl.access$1002(this.this$0, null);
        }

        @Override // androidx.media3.exoplayer.audio.AudioRendererEventListener
        public void onAudioEnabled(DecoderCounters r2) {
            ExoPlayerImpl.access$1002(this.this$0, r2);
            ExoPlayerImpl.access$500(this.this$0).onAudioEnabled(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioRendererEventListener
        public void onAudioInputFormatChanged(Format r2, DecoderReuseEvaluation r3) {
            ExoPlayerImpl.access$1102(this.this$0, r2);
            ExoPlayerImpl.access$500(this.this$0).onAudioInputFormatChanged(r2, r3);
        }

        @Override // androidx.media3.exoplayer.audio.AudioRendererEventListener
        public void onAudioPositionAdvancing(long r2) {
            ExoPlayerImpl.access$500(this.this$0).onAudioPositionAdvancing(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioRendererEventListener
        public void onAudioSinkError(Exception r2) {
            ExoPlayerImpl.access$500(this.this$0).onAudioSinkError(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioRendererEventListener
        public void onAudioTrackInitialized(AudioSink.AudioTrackConfig r2) {
            ExoPlayerImpl.access$500(this.this$0).onAudioTrackInitialized(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioRendererEventListener
        public void onAudioTrackReleased(AudioSink.AudioTrackConfig r2) {
            ExoPlayerImpl.access$500(this.this$0).onAudioTrackReleased(r2);
        }

        @Override // androidx.media3.exoplayer.audio.AudioRendererEventListener
        public void onAudioUnderrun(int r8, long r9, long r11) {
            ExoPlayerImpl.access$500(this.this$0).onAudioUnderrun(r8, r9, r11);
        }

        @Override // androidx.media3.exoplayer.text.TextOutput
        public void onCues(final CueGroup r4) {
            ExoPlayerImpl.access$1302(this.this$0, r4);
            ExoPlayerImpl.access$800(this.this$0).sendEvent(27, new ExoPlayerImpl$ComponentListener$$ExternalSyntheticLambda0(r4));
        }

        @Override // androidx.media3.exoplayer.text.TextOutput
        public void onCues(final List<Cue> r4) {
            ExoPlayerImpl.access$800(this.this$0).sendEvent(27, new ExoPlayerImpl$ComponentListener$$ExternalSyntheticLambda4(r4));
        }

        @Override // androidx.media3.exoplayer.video.VideoRendererEventListener
        public void onDroppedFrames(int r2, long r3) {
            ExoPlayerImpl.access$500(this.this$0).onDroppedFrames(r2, r3);
        }

        @Override // androidx.media3.exoplayer.metadata.MetadataOutput
        public void onMetadata(final Metadata r5) {
            ExoPlayerImpl.access$1402(this.this$0, ExoPlayerImpl.access$1400(this.this$0).buildUpon().populateFromMetadata(r5).build());
            MediaMetadata r0 = ExoPlayerImpl.access$1500(this.this$0);
            if (r0.equals(ExoPlayerImpl.access$1600(this.this$0)) == true) goto L5;
            ExoPlayerImpl.access$1602(this.this$0, r0);
            ExoPlayerImpl.access$800(this.this$0).queueEvent(14, new ExoPlayerImpl$ComponentListener$$ExternalSyntheticLambda1(this));
        L5:
            ExoPlayerImpl.access$800(this.this$0).queueEvent(28, new ExoPlayerImpl$ComponentListener$$ExternalSyntheticLambda2(r5));
            ExoPlayerImpl.access$800(this.this$0).flushEvents();
        }

        @Override // androidx.media3.exoplayer.ExoPlayer.AudioOffloadListener
        public /* synthetic */ void onOffloadedPlayback(boolean r1) {
            ExoPlayer.AudioOffloadListener.CC.$default$onOffloadedPlayback(this, r1);
        }

        @Override // androidx.media3.exoplayer.video.VideoRendererEventListener
        public void onRenderedFirstFrame(Object r4, long r5) {
            ExoPlayerImpl.access$500(this.this$0).onRenderedFirstFrame(r4, r5);
            if (ExoPlayerImpl.access$900(this.this$0) != r4) goto L6;
            ExoPlayerImpl.access$800(this.this$0).sendEvent(26, new SimpleBasePlayer$$ExternalSyntheticLambda10());
            return;
        }

        @Override // androidx.media3.exoplayer.audio.AudioRendererEventListener
        public void onSkipSilenceEnabledChanged(final boolean r4) {
            if (ExoPlayerImpl.access$1200(this.this$0) != r4) goto L5;
            return;
        L5:
            ExoPlayerImpl.access$1202(this.this$0, r4);
            ExoPlayerImpl.access$800(this.this$0).sendEvent(23, new ExoPlayerImpl$ComponentListener$$ExternalSyntheticLambda3(r4));
        }

        @Override // androidx.media3.exoplayer.ExoPlayer.AudioOffloadListener
        public void onSleepingForOffloadChanged(boolean r2) {
            ExoPlayerImpl.access$2700(this.this$0);
        }

        @Override // androidx.media3.exoplayer.StreamVolumeManager.Listener
        public void onStreamTypeChanged(int r5) {
            final DeviceInfo r0 = ExoPlayerImpl.access$2500(ExoPlayerImpl.access$2400(this.this$0));
            if (r0.equals(ExoPlayerImpl.access$2600(this.this$0)) == true) goto L6;
            ExoPlayerImpl.access$2602(this.this$0, r0);
            ExoPlayerImpl.access$800(this.this$0).sendEvent(29, new ExoPlayerImpl$ComponentListener$$ExternalSyntheticLambda6(r0));
            return;
        }

        @Override // androidx.media3.exoplayer.StreamVolumeManager.Listener
        public void onStreamVolumeChanged(final int r4, final boolean r5) {
            ExoPlayerImpl.access$800(this.this$0).sendEvent(30, new ExoPlayerImpl$ComponentListener$$ExternalSyntheticLambda7(r4, r5));
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureAvailable(SurfaceTexture r2, int r3, int r4) {
            ExoPlayerImpl.access$2000(this.this$0, r2);
            ExoPlayerImpl.access$1900(this.this$0, r3, r4);
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public boolean onSurfaceTextureDestroyed(SurfaceTexture r3) {
            ExoPlayerImpl.access$1800(this.this$0, null);
            ExoPlayerImpl.access$1900(this.this$0, 0, 0);
            return true;
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureSizeChanged(SurfaceTexture r2, int r3, int r4) {
            ExoPlayerImpl.access$1900(this.this$0, r3, r4);
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureUpdated(SurfaceTexture r1) {
        }

        @Override // androidx.media3.exoplayer.video.VideoRendererEventListener
        public void onVideoCodecError(Exception r2) {
            ExoPlayerImpl.access$500(this.this$0).onVideoCodecError(r2);
        }

        @Override // androidx.media3.exoplayer.video.VideoRendererEventListener
        public void onVideoDecoderInitialized(String r8, long r9, long r11) {
            ExoPlayerImpl.access$500(this.this$0).onVideoDecoderInitialized(r8, r9, r11);
        }

        @Override // androidx.media3.exoplayer.video.VideoRendererEventListener
        public void onVideoDecoderReleased(String r2) {
            ExoPlayerImpl.access$500(this.this$0).onVideoDecoderReleased(r2);
        }

        @Override // androidx.media3.exoplayer.video.VideoRendererEventListener
        public void onVideoDisabled(DecoderCounters r3) {
            ExoPlayerImpl.access$500(this.this$0).onVideoDisabled(r3);
            ExoPlayerImpl.access$602(this.this$0, null);
            ExoPlayerImpl.access$402(this.this$0, null);
        }

        @Override // androidx.media3.exoplayer.video.VideoRendererEventListener
        public void onVideoEnabled(DecoderCounters r2) {
            ExoPlayerImpl.access$402(this.this$0, r2);
            ExoPlayerImpl.access$500(this.this$0).onVideoEnabled(r2);
        }

        @Override // androidx.media3.exoplayer.video.VideoRendererEventListener
        public void onVideoFrameProcessingOffset(long r2, int r4) {
            ExoPlayerImpl.access$500(this.this$0).onVideoFrameProcessingOffset(r2, r4);
        }

        @Override // androidx.media3.exoplayer.video.VideoRendererEventListener
        public void onVideoInputFormatChanged(Format r2, DecoderReuseEvaluation r3) {
            ExoPlayerImpl.access$602(this.this$0, r2);
            ExoPlayerImpl.access$500(this.this$0).onVideoInputFormatChanged(r2, r3);
        }

        @Override // androidx.media3.exoplayer.video.VideoRendererEventListener
        public void onVideoSizeChanged(final VideoSize r4) {
            ExoPlayerImpl.access$702(this.this$0, r4);
            ExoPlayerImpl.access$800(this.this$0).sendEvent(25, new ExoPlayerImpl$ComponentListener$$ExternalSyntheticLambda5(r4));
        }

        @Override // androidx.media3.exoplayer.video.spherical.SphericalGLSurfaceView.VideoSurfaceListener
        public void onVideoSurfaceCreated(Surface r2) {
            ExoPlayerImpl.access$1800(this.this$0, r2);
        }

        @Override // androidx.media3.exoplayer.video.spherical.SphericalGLSurfaceView.VideoSurfaceListener
        public void onVideoSurfaceDestroyed(Surface r3) {
            ExoPlayerImpl.access$1800(this.this$0, null);
        }

        @Override // androidx.media3.exoplayer.AudioFocusManager.PlayerControl
        public void setVolumeMultiplier(float r2) {
            ExoPlayerImpl.access$2100(this.this$0);
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceChanged(SurfaceHolder r2, int r3, int r4, int r5) {
            ExoPlayerImpl.access$1900(this.this$0, r4, r5);
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceCreated(SurfaceHolder r3) {
            if (ExoPlayerImpl.access$1700(this.this$0) == false) goto L6;
            ExoPlayerImpl.access$1800(this.this$0, r3.getSurface());
            return;
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceDestroyed(SurfaceHolder r3) {
            if (ExoPlayerImpl.access$1700(this.this$0) == false) goto L5;
            ExoPlayerImpl.access$1800(this.this$0, null);
        L5:
            ExoPlayerImpl.access$1900(this.this$0, 0, 0);
        }
    }

    private static final class FrameMetadataListener implements VideoFrameMetadataListener, CameraMotionListener, PlayerMessage.Target {
        public static final int MSG_SET_CAMERA_MOTION_LISTENER = 8;
        public static final int MSG_SET_SPHERICAL_SURFACE_VIEW = 10000;
        public static final int MSG_SET_VIDEO_FRAME_METADATA_LISTENER = 7;
        private CameraMotionListener cameraMotionListener;
        private CameraMotionListener internalCameraMotionListener;
        private VideoFrameMetadataListener internalVideoFrameMetadataListener;
        private VideoFrameMetadataListener videoFrameMetadataListener;

        private FrameMetadataListener() {
        }

        /* synthetic */ FrameMetadataListener(C02321 r1) {
            this();
        }

        @Override // androidx.media3.exoplayer.PlayerMessage.Target
        public void handleMessage(int r3, Object r4) {
            switch(r3) {
                case 7: goto L9;
                case 8: goto L8;
                case 10000: goto L4;
                default: goto L14;
            };
        L4:
            SphericalGLSurfaceView r0 = (SphericalGLSurfaceView) r4;
            if (r0 != null) goto L7;
            this.internalVideoFrameMetadataListener = null;
            this.internalCameraMotionListener = null;
            return;
        L7:
            this.internalVideoFrameMetadataListener = r0.getVideoFrameMetadataListener();
            this.internalCameraMotionListener = r0.getCameraMotionListener();
            return;
        L8:
            this.cameraMotionListener = (CameraMotionListener) r4;
            return;
        L9:
            this.videoFrameMetadataListener = (VideoFrameMetadataListener) r4;
            return;
        }

        @Override // androidx.media3.exoplayer.video.spherical.CameraMotionListener
        public void onCameraMotion(long r2, float[] r4) {
            if (this.internalCameraMotionListener == null) goto L6;
            this.internalCameraMotionListener.onCameraMotion(r2, r4);
        L6:
            if (this.cameraMotionListener == null) goto L9;
            this.cameraMotionListener.onCameraMotion(r2, r4);
            return;
        }

        @Override // androidx.media3.exoplayer.video.spherical.CameraMotionListener
        public void onCameraMotionReset() {
            if (this.internalCameraMotionListener == null) goto L6;
            this.internalCameraMotionListener.onCameraMotionReset();
        L6:
            if (this.cameraMotionListener == null) goto L9;
            this.cameraMotionListener.onCameraMotionReset();
            return;
        }

        @Override // androidx.media3.exoplayer.video.VideoFrameMetadataListener
        public void onVideoFrameAboutToBeRendered(long r10, long r12, Format r14, MediaFormat r15) {
            if (this.internalVideoFrameMetadataListener == null) goto L5;
            long r2 = r10;
            long r4 = r12;
            Format r6 = r14;
            MediaFormat r7 = r15;
            this.internalVideoFrameMetadataListener.onVideoFrameAboutToBeRendered(r2, r4, r6, r7);
        L7:
            if (this.videoFrameMetadataListener == null) goto L10;
            MediaFormat r8 = r7;
            long r5 = r4;
            long r3 = r2;
            VideoFrameMetadataListener r9 = this.videoFrameMetadataListener;
            r9.onVideoFrameAboutToBeRendered(r3, r5, r6, r8);
            return;
        L10:
            return;
        L5:
            r2 = r10;
            r4 = r12;
            r6 = r14;
            r7 = r15;
            goto L7
        }
    }

    private static final class MediaSourceHolderSnapshot implements MediaSourceInfoHolder {
        private final MediaSource mediaSource;
        private Timeline timeline;
        private final Object uid;

        public MediaSourceHolderSnapshot(Object r2, MaskingMediaSource r3) {
            this.uid = r2;
            this.mediaSource = r3;
            this.timeline = r3.getTimeline();
        }

        static /* synthetic */ MediaSource access$300(MediaSourceHolderSnapshot r1) {
            return r1.mediaSource;
        }

        @Override // androidx.media3.exoplayer.MediaSourceInfoHolder
        public Timeline getTimeline() {
            return this.timeline;
        }

        @Override // androidx.media3.exoplayer.MediaSourceInfoHolder
        public Object getUid() {
            return this.uid;
        }

        public void updateTimeline(Timeline r1) {
            this.timeline = r1;
        }
    }

    private final class NoSuitableOutputPlaybackSuppressionAudioDeviceCallback extends AudioDeviceCallback {
        final /* synthetic */ ExoPlayerImpl this$0;

        private NoSuitableOutputPlaybackSuppressionAudioDeviceCallback(ExoPlayerImpl r1) {
            this.this$0 = r1;
        }

        /* synthetic */ NoSuitableOutputPlaybackSuppressionAudioDeviceCallback(ExoPlayerImpl r1, C02321 r2) {
            this(r1);
        }

        @Override // android.media.AudioDeviceCallback
        public void onAudioDevicesAdded(AudioDeviceInfo[] r5) {
            if (ExoPlayerImpl.access$2800(this.this$0) == true) goto L5;
            return;
        L5:
            if (ExoPlayerImpl.access$2900(this.this$0).playbackSuppressionReason != 3) goto L9;
            ExoPlayerImpl.access$3000(this.this$0, ExoPlayerImpl.access$2900(this.this$0).playWhenReady, 1, 0);
            return;
        }

        @Override // android.media.AudioDeviceCallback
        public void onAudioDevicesRemoved(AudioDeviceInfo[] r5) {
            if (ExoPlayerImpl.access$2800(this.this$0) == true) goto L6;
            ExoPlayerImpl.access$3000(this.this$0, ExoPlayerImpl.access$2900(this.this$0).playWhenReady, 1, 3);
            return;
        }
    }

    public static /* synthetic */ void $r8$lambda$kuBgAkV8YlLt9T_pxBD5vBMCsCg(ExoPlayerImpl r0, boolean r1) {
        r0.onSelectedOutputSuitabilityChanged(r1);
    }

    static {
        MediaLibraryInfo.registerModule("media3.exoplayer");
    }

    public ExoPlayerImpl(ExoPlayer.Builder r33, Player r34) {
        this.constructorFinished = new ConditionVariable();
        Log.m46i(TAG, "Init " + Integer.toHexString(System.identityHashCode(this)) + " [" + MediaLibraryInfo.VERSION_SLASHY + "] [" + Util.DEVICE_DEBUG_INFO + "]");     // Catch: Throwable -> L47
        this.applicationContext = r33.context.getApplicationContext();     // Catch: Throwable -> L47
        this.analyticsCollector = r33.analyticsCollectorFunction.apply(r33.clock);     // Catch: Throwable -> L47
        this.priority = r33.priority;     // Catch: Throwable -> L47
        this.priorityTaskManager = r33.priorityTaskManager;     // Catch: Throwable -> L47
        this.audioAttributes = r33.audioAttributes;     // Catch: Throwable -> L47
        this.videoScalingMode = r33.videoScalingMode;     // Catch: Throwable -> L47
        this.videoChangeFrameRateStrategy = r33.videoChangeFrameRateStrategy;     // Catch: Throwable -> L47
        this.skipSilenceEnabled = r33.skipSilenceEnabled;     // Catch: Throwable -> L47
        this.detachSurfaceTimeoutMs = r33.detachSurfaceTimeoutMs;     // Catch: Throwable -> L47
        C02321 r3 = null;
        this.componentListener = new ComponentListener(this, r3);     // Catch: Throwable -> L47
        this.frameMetadataListener = new FrameMetadataListener(r3);     // Catch: Throwable -> L47
        Handler r0 = new Handler(r33.looper);     // Catch: Throwable -> L47
        this.renderers = r33.renderersFactorySupplier.get().createRenderers(r0, this.componentListener, this.componentListener, this.componentListener, this.componentListener);     // Catch: Throwable -> L47
        if (this.renderers.length <= 0) goto L6;
        boolean r1 = true;
    L7:
        Assertions.checkState(r1);     // Catch: Throwable -> L47
        this.trackSelector = r33.trackSelectorSupplier.get();     // Catch: Throwable -> L47
        this.mediaSourceFactory = r33.mediaSourceFactorySupplier.get();     // Catch: Throwable -> L47
        this.bandwidthMeter = r33.bandwidthMeterSupplier.get();     // Catch: Throwable -> L47
        this.useLazyPreparation = r33.useLazyPreparation;     // Catch: Throwable -> L47
        this.seekParameters = r33.seekParameters;     // Catch: Throwable -> L47
        this.seekBackIncrementMs = r33.seekBackIncrementMs;     // Catch: Throwable -> L47
        this.seekForwardIncrementMs = r33.seekForwardIncrementMs;     // Catch: Throwable -> L47
        this.maxSeekToPreviousPositionMs = r33.maxSeekToPreviousPositionMs;     // Catch: Throwable -> L47
        this.pauseAtEndOfMediaItems = r33.pauseAtEndOfMediaItems;     // Catch: Throwable -> L47
        this.applicationLooper = r33.looper;     // Catch: Throwable -> L47
        this.clock = r33.clock;     // Catch: Throwable -> L47
        if (r34 != null) goto L10;
        Player r2 = this;
    L11:
        this.wrappingPlayer = r2;     // Catch: Throwable -> L47
        this.suppressPlaybackOnUnsuitableOutput = r33.suppressPlaybackOnUnsuitableOutput;     // Catch: Throwable -> L47
        this.listeners = new ListenerSet(this.applicationLooper, this.clock, new ExoPlayerImpl$$ExternalSyntheticLambda15(this));     // Catch: Throwable -> L47
        this.audioOffloadListeners = new CopyOnWriteArraySet();     // Catch: Throwable -> L47
        this.mediaSourceHolderSnapshots = new ArrayList();     // Catch: Throwable -> L47
        this.shuffleOrder = new ShuffleOrder.DefaultShuffleOrder(0);     // Catch: Throwable -> L47
        this.preloadConfiguration = ExoPlayer.PreloadConfiguration.DEFAULT;     // Catch: Throwable -> L47
        this.emptyTrackSelectorResult = new TrackSelectorResult(new RendererConfiguration[this.renderers.length], new ExoTrackSelection[this.renderers.length], Tracks.EMPTY, null);     // Catch: Throwable -> L47
        this.period = new Timeline.Period();     // Catch: Throwable -> L47
        this.permanentAvailableCommands = new Player.Commands.Builder().addAll(new int[]{1, 2, 3, 13, 14, 15, 16, 17, 18, 19, 31, 20, 30, 21, 35, 22, 24, 27, 28, 32}).addIf(29, this.trackSelector.isSetParametersSupported()).addIf(23, r33.deviceVolumeControlEnabled).addIf(25, r33.deviceVolumeControlEnabled).addIf(33, r33.deviceVolumeControlEnabled).addIf(26, r33.deviceVolumeControlEnabled).addIf(34, r33.deviceVolumeControlEnabled).build();     // Catch: Throwable -> L47
        this.availableCommands = new Player.Commands.Builder().addAll(this.permanentAvailableCommands).add(4).add(10).build();     // Catch: Throwable -> L47
        this.playbackInfoUpdateHandler = this.clock.createHandler(this.applicationLooper, null);     // Catch: Throwable -> L47
        this.playbackInfoUpdateListener = new ExoPlayerImpl$$ExternalSyntheticLambda16(this);     // Catch: Throwable -> L47
        this.playbackInfo = PlaybackInfo.createDummy(this.emptyTrackSelectorResult);     // Catch: Throwable -> L47
        this.analyticsCollector.setPlayer(this.wrappingPlayer, this.applicationLooper);     // Catch: Throwable -> L47
        if (Util.SDK_INT >= 31) goto L14;
        PlayerId r4 = new PlayerId(r33.playerName);     // Catch: Throwable -> L47
    L15:
        this.internalPlayer = new ExoPlayerImplInternal(this.renderers, this.trackSelector, this.emptyTrackSelectorResult, r33.loadControlSupplier.get(), this.bandwidthMeter, this.repeatMode, this.shuffleModeEnabled, this.analyticsCollector, this.seekParameters, r33.livePlaybackSpeedControl, r33.releaseTimeoutMs, this.pauseAtEndOfMediaItems, r33.dynamicSchedulingEnabled, this.applicationLooper, this.clock, this.playbackInfoUpdateListener, r4, r33.playbackLooperProvider, this.preloadConfiguration);     // Catch: Throwable -> L47
        this.volume = 1.0f;     // Catch: Throwable -> L47
        this.repeatMode = 0;     // Catch: Throwable -> L47
        this.mediaMetadata = MediaMetadata.EMPTY;     // Catch: Throwable -> L47
        this.playlistMetadata = MediaMetadata.EMPTY;     // Catch: Throwable -> L47
        this.staticAndDynamicMediaMetadata = MediaMetadata.EMPTY;     // Catch: Throwable -> L47
        this.maskingWindowIndex = -1;     // Catch: Throwable -> L47
        this.audioSessionId = Util.generateAudioSessionIdV21(this.applicationContext);     // Catch: Throwable -> L47
        this.currentCueGroup = CueGroup.EMPTY_TIME_ZERO;     // Catch: Throwable -> L47
        this.throwsWhenUsingWrongThread = true;     // Catch: Throwable -> L47
        addListener(this.analyticsCollector);     // Catch: Throwable -> L47
        this.bandwidthMeter.addEventListener(new Handler(this.applicationLooper), this.analyticsCollector);     // Catch: Throwable -> L47
        addAudioOffloadListener(this.componentListener);     // Catch: Throwable -> L47
        if (r33.foregroundModeTimeoutMs <= 0) goto L18;
        this.internalPlayer.experimentalSetForegroundModeTimeoutMs(r33.foregroundModeTimeoutMs);     // Catch: Throwable -> L47
    L18:
        this.audioBecomingNoisyManager = new AudioBecomingNoisyManager(r33.context, r0, this.componentListener);     // Catch: Throwable -> L47
        this.audioBecomingNoisyManager.setEnabled(r33.handleAudioBecomingNoisy);     // Catch: Throwable -> L47
        this.audioFocusManager = new AudioFocusManager(r33.context, r0, this.componentListener);     // Catch: Throwable -> L47
        AudioFocusManager r5 = this.audioFocusManager;     // Catch: Throwable -> L47
        if (r33.handleAudioFocus == false) goto L21;
        AudioAttributes r6 = this.audioAttributes;     // Catch: Throwable -> L47
    L22:
        r5.setAudioAttributes(r6);     // Catch: Throwable -> L47
        this.suitableOutputChecker = r33.suitableOutputChecker;     // Catch: Throwable -> L47
        if (this.suitableOutputChecker == null) goto L28;
        if (Util.SDK_INT < 35) goto L28;
        this.suitableOutputChecker.enable(new ExoPlayerImpl$$ExternalSyntheticLambda17(this));     // Catch: Throwable -> L47
    L33:
        if (r33.deviceVolumeControlEnabled == false) goto L35;
        this.streamVolumeManager = new StreamVolumeManager(r33.context, r0, this.componentListener, Util.getStreamTypeForAudioUsage(this.audioAttributes.usage));     // Catch: Throwable -> L47
    L36:
        this.wakeLockManager = new WakeLockManager(r33.context);     // Catch: Throwable -> L47
        WakeLockManager r7 = this.wakeLockManager;     // Catch: Throwable -> L47
        if (r33.wakeMode == 0) goto L39;
        boolean r8 = true;
    L40:
        r7.setEnabled(r8);     // Catch: Throwable -> L47
        this.wifiLockManager = new WifiLockManager(r33.context);     // Catch: Throwable -> L47
        WifiLockManager r9 = this.wifiLockManager;     // Catch: Throwable -> L47
        if (r33.wakeMode != 2) goto L43;
        boolean r10 = true;
    L44:
        r9.setEnabled(r10);     // Catch: Throwable -> L47
        this.deviceInfo = createDeviceInfo(this.streamVolumeManager);     // Catch: Throwable -> L47
        this.videoSize = VideoSize.UNKNOWN;     // Catch: Throwable -> L47
        this.surfaceSize = Size.UNKNOWN;     // Catch: Throwable -> L47
        this.trackSelector.setAudioAttributes(this.audioAttributes);     // Catch: Throwable -> L47
        sendRendererMessage(1, 10, Integer.valueOf(this.audioSessionId));     // Catch: Throwable -> L47
        sendRendererMessage(2, 10, Integer.valueOf(this.audioSessionId));     // Catch: Throwable -> L47
        sendRendererMessage(1, 3, this.audioAttributes);     // Catch: Throwable -> L47
        sendRendererMessage(2, 4, Integer.valueOf(this.videoScalingMode));     // Catch: Throwable -> L47
        sendRendererMessage(2, 5, Integer.valueOf(this.videoChangeFrameRateStrategy));     // Catch: Throwable -> L47
        sendRendererMessage(1, 9, Boolean.valueOf(this.skipSilenceEnabled));     // Catch: Throwable -> L47
        sendRendererMessage(2, 7, this.frameMetadataListener);     // Catch: Throwable -> L47
        sendRendererMessage(6, 8, this.frameMetadataListener);     // Catch: Throwable -> L47
        sendRendererMessage(16, Integer.valueOf(this.priority));     // Catch: Throwable -> L47
        this.constructorFinished.open();
        return;
    L43:
        r10 = false;
        goto L44
    L39:
        r8 = false;
        goto L40
    L35:
        this.streamVolumeManager = null;     // Catch: Throwable -> L47
    L28:
        if (this.suppressPlaybackOnUnsuitableOutput == false) goto L33;
        if (Util.SDK_INT < 23) goto L33;
        this.audioManager = (AudioManager) this.applicationContext.getSystemService(MimeTypes.BASE_TYPE_AUDIO);     // Catch: Throwable -> L47
        Api23.registerAudioDeviceCallback(this.audioManager, new NoSuitableOutputPlaybackSuppressionAudioDeviceCallback(this, null), new Handler(this.applicationLooper));     // Catch: Throwable -> L47
        goto L33
    L21:
        r6 = null;
        goto L22
    L14:
        r4 = Api31.registerMediaMetricsListener(this.applicationContext, this, r33.usePlatformDiagnostics, r33.playerName);     // Catch: Throwable -> L47
        goto L15
    L10:
        r2 = r34;
        goto L11
    L6:
        r1 = false;
    L47:
        th = move-exception;
        this.constructorFinished.open();
        throw th;
    }

    static /* synthetic */ DecoderCounters access$1002(ExoPlayerImpl r0, DecoderCounters r1) {
        r0.audioDecoderCounters = r1;
        return r1;
    }

    static /* synthetic */ Format access$1102(ExoPlayerImpl r0, Format r1) {
        r0.audioFormat = r1;
        return r1;
    }

    static /* synthetic */ boolean access$1200(ExoPlayerImpl r1) {
        return r1.skipSilenceEnabled;
    }

    static /* synthetic */ boolean access$1202(ExoPlayerImpl r0, boolean r1) {
        r0.skipSilenceEnabled = r1;
        return r1;
    }

    static /* synthetic */ CueGroup access$1302(ExoPlayerImpl r0, CueGroup r1) {
        r0.currentCueGroup = r1;
        return r1;
    }

    static /* synthetic */ MediaMetadata access$1400(ExoPlayerImpl r1) {
        return r1.staticAndDynamicMediaMetadata;
    }

    static /* synthetic */ MediaMetadata access$1402(ExoPlayerImpl r0, MediaMetadata r1) {
        r0.staticAndDynamicMediaMetadata = r1;
        return r1;
    }

    static /* synthetic */ MediaMetadata access$1500(ExoPlayerImpl r1) {
        return r1.buildUpdatedMediaMetadata();
    }

    static /* synthetic */ MediaMetadata access$1600(ExoPlayerImpl r1) {
        return r1.mediaMetadata;
    }

    static /* synthetic */ MediaMetadata access$1602(ExoPlayerImpl r0, MediaMetadata r1) {
        r0.mediaMetadata = r1;
        return r1;
    }

    static /* synthetic */ boolean access$1700(ExoPlayerImpl r1) {
        return r1.surfaceHolderSurfaceIsVideoOutput;
    }

    static /* synthetic */ void access$1800(ExoPlayerImpl r0, Object r1) {
        r0.setVideoOutputInternal(r1);
    }

    static /* synthetic */ void access$1900(ExoPlayerImpl r0, int r1, int r2) {
        r0.maybeNotifySurfaceSizeChanged(r1, r2);
    }

    static /* synthetic */ void access$2000(ExoPlayerImpl r0, SurfaceTexture r1) {
        r0.setSurfaceTextureInternal(r1);
    }

    static /* synthetic */ void access$2100(ExoPlayerImpl r0) {
        r0.sendVolumeToRenderers();
    }

    static /* synthetic */ int access$2200(int r1) {
        return getPlayWhenReadyChangeReason(r1);
    }

    static /* synthetic */ void access$2300(ExoPlayerImpl r0, boolean r1, int r2, int r3) {
        r0.updatePlayWhenReady(r1, r2, r3);
    }

    static /* synthetic */ StreamVolumeManager access$2400(ExoPlayerImpl r1) {
        return r1.streamVolumeManager;
    }

    static /* synthetic */ DeviceInfo access$2500(StreamVolumeManager r1) {
        return createDeviceInfo(r1);
    }

    static /* synthetic */ DeviceInfo access$2600(ExoPlayerImpl r1) {
        return r1.deviceInfo;
    }

    static /* synthetic */ DeviceInfo access$2602(ExoPlayerImpl r0, DeviceInfo r1) {
        r0.deviceInfo = r1;
        return r1;
    }

    static /* synthetic */ void access$2700(ExoPlayerImpl r0) {
        r0.updateWakeAndWifiLock();
    }

    static /* synthetic */ boolean access$2800(ExoPlayerImpl r1) {
        return r1.hasSupportedAudioOutput();
    }

    static /* synthetic */ PlaybackInfo access$2900(ExoPlayerImpl r1) {
        return r1.playbackInfo;
    }

    static /* synthetic */ void access$3000(ExoPlayerImpl r0, boolean r1, int r2, int r3) {
        r0.updatePlaybackInfoForPlayWhenReadyAndSuppressionReasonStates(r1, r2, r3);
    }

    static /* synthetic */ DecoderCounters access$402(ExoPlayerImpl r0, DecoderCounters r1) {
        r0.videoDecoderCounters = r1;
        return r1;
    }

    static /* synthetic */ AnalyticsCollector access$500(ExoPlayerImpl r1) {
        return r1.analyticsCollector;
    }

    static /* synthetic */ Format access$602(ExoPlayerImpl r0, Format r1) {
        r0.videoFormat = r1;
        return r1;
    }

    static /* synthetic */ VideoSize access$702(ExoPlayerImpl r0, VideoSize r1) {
        r0.videoSize = r1;
        return r1;
    }

    static /* synthetic */ ListenerSet access$800(ExoPlayerImpl r1) {
        return r1.listeners;
    }

    static /* synthetic */ Object access$900(ExoPlayerImpl r1) {
        return r1.videoOutput;
    }

    private List<MediaSourceList.MediaSourceHolder> addMediaSourceHolders(int r9, List<MediaSource> r10) {
        ArrayList r0 = new ArrayList();
        int r1 = 0;
    L4:
        if (r1 >= r10.size()) goto L6;
        MediaSourceList.MediaSourceHolder r2 = new MediaSourceList.MediaSourceHolder(r10.get(r1), this.useLazyPreparation);
        r0.add(r2);
        this.mediaSourceHolderSnapshots.add(r1 + r9, new MediaSourceHolderSnapshot(r2.uid, r2.mediaSource));
        r1 = r1 + 1;
        goto L4
    L6:
        this.shuffleOrder = this.shuffleOrder.cloneAndInsert(r9, r0.size());
        return r0;
    }

    private PlaybackInfo addMediaSourcesInternal(PlaybackInfo r8, int r9, List<MediaSource> r10) {
        Timeline r1 = r8.timeline;
        this.pendingOperationAcks++;
        List<MediaSourceList.MediaSourceHolder> r6 = addMediaSourceHolders(r9, r10);
        Timeline r2 = createMaskingTimeline();
        PlaybackInfo r3 = maskTimelineAndPosition(r8, r2, getPeriodPositionUsAfterTimelineChanged(r1, r2, getCurrentWindowIndexInternal(r8), getContentPositionInternal(r8)));
        this.internalPlayer.addMediaSources(r9, r6, this.shuffleOrder);
        return r3;
    }

    private MediaMetadata buildUpdatedMediaMetadata() {
        Timeline r0 = getCurrentTimeline();
        if (r0.isEmpty() == true) goto L5;
        MediaItem r1 = r0.getWindow(getCurrentMediaItemIndex(), this.window).mediaItem;
        return this.staticAndDynamicMediaMetadata.buildUpon().populate(r1.mediaMetadata).build();
    L5:
        return this.staticAndDynamicMediaMetadata;
    }

    private boolean canUpdateMediaSourcesWithMediaItems(int r5, int r6, List<MediaItem> r7) {
        if ((r6 - r5) == r7.size()) goto L5;
        return false;
    L5:
        int r0 = r5;
    L6:
        if (r0 >= r6) goto L11;
        if (MediaSourceHolderSnapshot.access$300(this.mediaSourceHolderSnapshots.get(r0)).canUpdateMediaItem(r7.get(r0 - r5)) == false) goto L9;
        r0 = r0 + 1;
        goto L6
    L9:
        return false;
    L11:
        return true;
    }

    private int computePlaybackSuppressionReason(boolean r3, int r4) {
        if (r4 != 0) goto L6;
        return 1;
    L6:
        if (this.suppressPlaybackOnUnsuitableOutput == true) goto L8;
        return 0;
    L8:
        if (r3 == true) goto L10;
    L12:
        if (r3 == false) goto L14;
        return 0;
    L14:
        if (this.playbackInfo.playbackSuppressionReason != 3) goto L19;
        return 3;
    L19:
        return 0;
    L10:
        if (hasSupportedAudioOutput() == true) goto L12;
        return 3;
    }

    private static DeviceInfo createDeviceInfo(StreamVolumeManager r3) {
        int r1 = 0;
        DeviceInfo.Builder r0 = new DeviceInfo.Builder(0);
        if (r3 == null) goto L5;
        int r2 = r3.getMinVolume();
    L6:
        DeviceInfo.Builder r4 = r0.setMinVolume(r2);
        if (r3 == null) goto L10;
        r1 = r3.getMaxVolume();
    L10:
        return r4.setMaxVolume(r1).build();
    L5:
        r2 = 0;
        goto L6
    }

    private Timeline createMaskingTimeline() {
        return new PlaylistTimeline(this.mediaSourceHolderSnapshots, this.shuffleOrder);
    }

    private List<MediaSource> createMediaSources(List<MediaItem> r5) {
        ArrayList r0 = new ArrayList();
        int r1 = 0;
    L4:
        if (r1 >= r5.size()) goto L6;
        r0.add(this.mediaSourceFactory.createMediaSource(r5.get(r1)));
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    private PlayerMessage createMessageInternal(PlayerMessage.Target r9) {
        int r0 = getCurrentWindowIndexInternal(this.playbackInfo);
        ExoPlayerImplInternal r2 = this.internalPlayer;
        Timeline r4 = this.playbackInfo.timeline;
        if (r0 != (-1)) goto L5;
        int r5 = 0;
    L7:
        return new PlayerMessage(r2, r9, r4, r5, this.clock, this.internalPlayer.getPlaybackLooper());
    L5:
        r5 = r0;
        goto L7
    }

    private Pair<Boolean, Integer> evaluateMediaItemTransitionReason(PlaybackInfo r20, PlaybackInfo r21, boolean r22, int r23, boolean r24, boolean r25) {
        Timeline r4 = r21.timeline;
        Timeline r5 = r20.timeline;
        if (r5.isEmpty() == false) goto L9;
        if (r4.isEmpty() == false) goto L9;
        return new Pair(false, -1);
    L9:
        if (r5.isEmpty() == r4.isEmpty()) goto L13;
        return new Pair(true, 3);
    L13:
        if (r4.getWindow(r4.getPeriodByUid(r21.periodId.periodUid, this.period).windowIndex, this.window).uid.equals(r5.getWindow(r5.getPeriodByUid(r20.periodId.periodUid, this.period).windowIndex, this.window).uid) == true) goto L26;
        if (r22 == false) goto L17;
        if (r23 != 0) goto L17;
        int r7 = 1;
    L23:
        return new Pair(true, Integer.valueOf(r7));
    L17:
        if (r22 == false) goto L20;
        if (r23 != 1) goto L20;
        r7 = 2;
    L20:
        if (r24 == false) goto L25;
        r7 = 3;
        goto L23
    L25:
        throw new IllegalStateException();
    L26:
        if (r22 == false) goto L32;
        if (r23 != 0) goto L32;
        boolean r18 = true;
        if (r21.periodId.windowSequenceNumber < r20.periodId.windowSequenceNumber) goto L31;
    L33:
        if (r22 == false) goto L40;
        boolean r8 = r18;
        if (r23 != r8) goto L40;
        if (r25 == false) goto L40;
        return new Pair(Boolean.valueOf(r8), 2);
    L40:
        return new Pair(false, -1);
    L31:
        return new Pair(true, 0);
    L32:
        r18 = true;
        goto L33
    }

    private long getContentPositionInternal(PlaybackInfo r5) {
        if (r5.periodId.isAd() == false) goto L10;
        r5.timeline.getPeriodByUid(r5.periodId.periodUid, this.period);
        if (r5.requestedContentPositionUs != C0180C.TIME_UNSET) goto L8;
        return r5.timeline.getWindow(getCurrentWindowIndexInternal(r5), this.window).getDefaultPositionMs();
    L8:
        return this.period.getPositionInWindowMs() + Util.usToMs(r5.requestedContentPositionUs);
    L10:
        return Util.usToMs(getCurrentPositionUsInternal(r5));
    }

    private long getCurrentPositionUsInternal(PlaybackInfo r5) {
        if (r5.timeline.isEmpty() == false) goto L7;
        return Util.msToUs(this.maskingWindowPositionMs);
    L7:
        if (r5.sleepingForOffload == false) goto L9;
        long r0 = r5.getEstimatedPositionUs();
    L11:
        if (r5.periodId.isAd() == false) goto L14;
        return r0;
    L14:
        return periodPositionUsToWindowPositionUs(r5.timeline, r5.periodId, r0);
    L9:
        r0 = r5.positionUs;
        goto L11
    }

    private int getCurrentWindowIndexInternal(PlaybackInfo r4) {
        if (r4.timeline.isEmpty() == false) goto L7;
        return this.maskingWindowIndex;
    L7:
        return r4.timeline.getPeriodByUid(r4.periodId.periodUid, this.period).windowIndex;
    }

    private Pair<Object, Long> getPeriodPositionUsAfterTimelineChanged(Timeline r18, Timeline r19, int r20, long r21) {
        boolean r1 = r18.isEmpty();
        long r8 = C0180C.TIME_UNSET;
        int r10 = -1;
        if (r1 == true) goto L17;
        if (r19.isEmpty() == true) goto L17;
        Pair<Object, Long> r12 = r18.getPeriodPositionUs(this.window, this.period, r20, Util.msToUs(r21));
        Object r5 = ((Pair) Util.castNonNull(r12)).first;
        if (r19.getIndexOfPeriod(r5) == (-1)) goto L10;
        return r12;
    L10:
        int r2 = ExoPlayerImplInternal.resolveSubsequentPeriod(this.window, this.period, this.repeatMode, this.shuffleModeEnabled, r5, r18, r19);
        if (r2 == (-1)) goto L15;
        return maskWindowPositionMsOrGetPeriodPositionUs(r19, r2, r19.getWindow(r2, this.window).getDefaultPositionMs());
    L15:
        return maskWindowPositionMsOrGetPeriodPositionUs(r19, -1, C0180C.TIME_UNSET);
    L17:
        if (r18.isEmpty() == false) goto L19;
    L21:
        boolean r3 = false;
    L23:
        if (r3 == true) goto L26;
        r10 = r20;
    L26:
        if (r3 == true) goto L30;
        r8 = r21;
    L30:
        return maskWindowPositionMsOrGetPeriodPositionUs(r19, r10, r8);
    L19:
        if (r19.isEmpty() == false) goto L21;
        r3 = true;
        goto L23
    }

    private static int getPlayWhenReadyChangeReason(int r1) {
        if (r1 != (-1)) goto L5;
        return 2;
    L5:
        return 1;
    }

    private Player.PositionInfo getPositionInfo(long r15) {
        int r4 = getCurrentMediaItemIndex();
        if (this.playbackInfo.timeline.isEmpty() == true) goto L5;
        Object r1 = this.playbackInfo.periodId.periodUid;
        this.playbackInfo.timeline.getPeriodByUid(r1, this.period);
        int r2 = this.playbackInfo.timeline.getIndexOfPeriod(r1);
        Object r0 = this.playbackInfo.timeline.getWindow(r4, this.window).uid;
        Object r6 = r1;
        int r7 = r2;
        MediaItem r5 = this.window.mediaItem;
        Object r3 = r0;
    L6:
        long r8 = Util.usToMs(r15);
        if (this.playbackInfo.periodId.isAd() == false) goto L9;
        long r10 = Util.usToMs(getRequestedContentPositionUs(this.playbackInfo));
    L11:
        return new Player.PositionInfo(r3, r4, r5, r6, r7, r8, r10, this.playbackInfo.periodId.adGroupIndex, this.playbackInfo.periodId.adIndexInAdGroup);
    L9:
        r10 = r8;
        goto L11
    L5:
        r6 = null;
        r7 = -1;
        r5 = null;
        r3 = null;
        goto L6
    }

    private Player.PositionInfo getPreviousPositionInfo(int r21, PlaybackInfo r22, int r23) {
        Timeline.Period r7 = new Timeline.Period();
        if (r22.timeline.isEmpty() == true) goto L5;
        Object r3 = r22.periodId.periodUid;
        r22.timeline.getPeriodByUid(r3, r7);
        int r4 = r7.windowIndex;
        int r5 = r22.timeline.getIndexOfPeriod(r3);
        Object r9 = r22.timeline.getWindow(r4, this.window).uid;
        Object r12 = r3;
        int r10 = r4;
        int r13 = r5;
        MediaItem r11 = this.window.mediaItem;
    L6:
        if (r21 != 0) goto L15;
        if (r22.periodId.isAd() == false) goto L11;
        long r2 = r7.getAdDurationUs(r22.periodId.adGroupIndex, r22.periodId.adIndexInAdGroup);
        long r6 = getRequestedContentPositionUs(r22);
    L19:
        return new Player.PositionInfo(r9, r10, r11, r12, r13, Util.usToMs(r2), Util.usToMs(r6), r22.periodId.adGroupIndex, r22.periodId.adIndexInAdGroup);
    L11:
        if (r22.periodId.nextAdGroupIndex == (-1)) goto L13;
        r2 = getRequestedContentPositionUs(this.playbackInfo);
        r6 = r2;
        goto L19
    L13:
        r2 = r7.positionInWindowUs + r7.durationUs;
        r6 = r2;
        goto L19
    L15:
        if (r22.periodId.isAd() == false) goto L17;
        r2 = r22.positionUs;
        r6 = getRequestedContentPositionUs(r22);
        goto L19
    L17:
        r2 = r7.positionInWindowUs + r22.positionUs;
        r6 = r2;
        goto L19
    L5:
        r9 = null;
        r12 = null;
        r10 = r23;
        r13 = -1;
        r11 = null;
        goto L6
    }

    private static long getRequestedContentPositionUs(PlaybackInfo r6) {
        Timeline.Window r0 = new Timeline.Window();
        Timeline.Period r1 = new Timeline.Period();
        r6.timeline.getPeriodByUid(r6.periodId.periodUid, r1);
        if (r6.requestedContentPositionUs != C0180C.TIME_UNSET) goto L6;
        return r6.timeline.getWindow(r1.windowIndex, r0).getDefaultPositionUs();
    L6:
        return r1.getPositionInWindowUs() + r6.requestedContentPositionUs;
    }

    private void handlePlaybackInfo(ExoPlayerImplInternal.PlaybackInfoUpdate r13) {
        this.pendingOperationAcks -= r13.operationAcks;
        boolean r2 = true;
        if (r13.positionDiscontinuity == false) goto L6;
        this.pendingDiscontinuityReason = r13.discontinuityReason;
        this.pendingDiscontinuity = true;
    L6:
        if (this.pendingOperationAcks != 0) goto L45;
        Timeline r9 = r13.playbackInfo.timeline;
        if (this.playbackInfo.timeline.isEmpty() == true) goto L13;
        if (r9.isEmpty() == false) goto L13;
        this.maskingWindowIndex = -1;
        this.maskingWindowPositionMs = 0;
        this.maskingPeriodIndex = 0;
    L13:
        if (r9.isEmpty() == true) goto L22;
        List<Timeline> r1 = ((PlaylistTimeline) r9).getChildTimelines();
        if (r1.size() != this.mediaSourceHolderSnapshots.size()) goto L17;
        boolean r4 = true;
    L18:
        Assertions.checkState(r4);
        int r5 = 0;
    L20:
        if (r5 >= r1.size()) goto L22;
        this.mediaSourceHolderSnapshots.get(r5).updateTimeline(r1.get(r5));
        r5 = r5 + 1;
        goto L20
    L17:
        r4 = false;
    L22:
        boolean r3 = false;
        if (this.pendingDiscontinuity == true) goto L25;
        long r6 = -9223372036854775807L;
    L42:
        this.pendingDiscontinuity = false;
        updatePlaybackInfo(r13.playbackInfo, 1, r3, this.pendingDiscontinuityReason, r6, -1, false);
        return;
    L25:
        if (r13.playbackInfo.periodId.equals(this.playbackInfo.periodId) == true) goto L27;
    L30:
        r3 = r2;
        if (r3 == true) goto L33;
        r6 = -9223372036854775807L;
        goto L42
    L33:
        if (r9.isEmpty() == false) goto L35;
    L38:
        long r7 = r13.playbackInfo.discontinuityStartPositionUs;
    L39:
        r6 = r7;
        goto L42
    L35:
        if (r13.playbackInfo.periodId.isAd() == true) goto L38;
        r7 = periodPositionUsToWindowPositionUs(r9, r13.playbackInfo.periodId, r13.playbackInfo.discontinuityStartPositionUs);
        goto L39
    L27:
        if (r13.playbackInfo.discontinuityStartPositionUs != this.playbackInfo.positionUs) goto L30;
        r2 = false;
        goto L30
    }

    private boolean hasSupportedAudioOutput() {
        if (Util.SDK_INT < 35) goto L9;
        if (this.suitableOutputChecker == null) goto L9;
        return this.suitableOutputChecker.isSelectedOutputSuitableForPlayback();
    L9:
        if (Util.SDK_INT >= 23) goto L11;
        return true;
    L11:
        if (this.audioManager != null) goto L13;
        return true;
    L13:
        return Api23.isSuitableExternalAudioOutputPresentInAudioDeviceInfoList(this.applicationContext, this.audioManager.getDevices(2));
    }

    static /* synthetic */ void lambda$maybeNotifySurfaceSizeChanged$27(int r0, int r1, Player.Listener r2) {
        r2.onSurfaceSizeChanged(r0, r1);
    }

    static /* synthetic */ void lambda$release$5(Player.Listener r2) {
        r2.onPlayerError(ExoPlaybackException.createForUnexpected(new ExoTimeoutException(1), 1003));
    }

    static /* synthetic */ void lambda$setAudioAttributes$8(AudioAttributes r0, Player.Listener r1) {
        r1.onAudioAttributesChanged(r0);
    }

    static /* synthetic */ void lambda$setAudioSessionId$9(int r0, Player.Listener r1) {
        r1.onAudioSessionIdChanged(r0);
    }

    static /* synthetic */ void lambda$setRepeatMode$3(int r0, Player.Listener r1) {
        r1.onRepeatModeChanged(r0);
    }

    static /* synthetic */ void lambda$setShuffleModeEnabled$4(boolean r0, Player.Listener r1) {
        r1.onShuffleModeEnabledChanged(r0);
    }

    static /* synthetic */ void lambda$setSkipSilenceEnabled$11(boolean r0, Player.Listener r1) {
        r1.onSkipSilenceEnabledChanged(r0);
    }

    static /* synthetic */ void lambda$setTrackSelectionParameters$6(TrackSelectionParameters r0, Player.Listener r1) {
        r1.onTrackSelectionParametersChanged(r0);
    }

    static /* synthetic */ void lambda$setVolume$10(float r0, Player.Listener r1) {
        r1.onVolumeChanged(r0);
    }

    static /* synthetic */ void lambda$updatePlaybackInfo$12(PlaybackInfo r1, int r2, Player.Listener r3) {
        r3.onTimelineChanged(r1.timeline, r2);
    }

    static /* synthetic */ void lambda$updatePlaybackInfo$13(int r0, Player.PositionInfo r1, Player.PositionInfo r2, Player.Listener r3) {
        r3.onPositionDiscontinuity(r0);
        r3.onPositionDiscontinuity(r1, r2, r0);
    }

    static /* synthetic */ void lambda$updatePlaybackInfo$14(MediaItem r0, int r1, Player.Listener r2) {
        r2.onMediaItemTransition(r0, r1);
    }

    static /* synthetic */ void lambda$updatePlaybackInfo$15(PlaybackInfo r1, Player.Listener r2) {
        r2.onPlayerErrorChanged(r1.playbackError);
    }

    static /* synthetic */ void lambda$updatePlaybackInfo$16(PlaybackInfo r1, Player.Listener r2) {
        r2.onPlayerError(r1.playbackError);
    }

    static /* synthetic */ void lambda$updatePlaybackInfo$17(PlaybackInfo r1, Player.Listener r2) {
        r2.onTracksChanged(r1.trackSelectorResult.tracks);
    }

    static /* synthetic */ void lambda$updatePlaybackInfo$18(MediaMetadata r0, Player.Listener r1) {
        r1.onMediaMetadataChanged(r0);
    }

    static /* synthetic */ void lambda$updatePlaybackInfo$19(PlaybackInfo r1, Player.Listener r2) {
        r2.onLoadingChanged(r1.isLoading);
        r2.onIsLoadingChanged(r1.isLoading);
    }

    static /* synthetic */ void lambda$updatePlaybackInfo$20(PlaybackInfo r2, Player.Listener r3) {
        r3.onPlayerStateChanged(r2.playWhenReady, r2.playbackState);
    }

    static /* synthetic */ void lambda$updatePlaybackInfo$21(PlaybackInfo r1, Player.Listener r2) {
        r2.onPlaybackStateChanged(r1.playbackState);
    }

    static /* synthetic */ void lambda$updatePlaybackInfo$22(PlaybackInfo r2, Player.Listener r3) {
        r3.onPlayWhenReadyChanged(r2.playWhenReady, r2.playWhenReadyChangeReason);
    }

    static /* synthetic */ void lambda$updatePlaybackInfo$23(PlaybackInfo r1, Player.Listener r2) {
        r2.onPlaybackSuppressionReasonChanged(r1.playbackSuppressionReason);
    }

    static /* synthetic */ void lambda$updatePlaybackInfo$24(PlaybackInfo r1, Player.Listener r2) {
        r2.onIsPlayingChanged(r1.isPlaying());
    }

    static /* synthetic */ void lambda$updatePlaybackInfo$25(PlaybackInfo r1, Player.Listener r2) {
        r2.onPlaybackParametersChanged(r1.playbackParameters);
    }

    private PlaybackInfo maskTimelineAndPosition(PlaybackInfo r30, Timeline r31, Pair<Object, Long> r32) {
        if (r31.isEmpty() == true) goto L7;
        if (r32 != null) goto L7;
        boolean r3 = false;
    L8:
        Assertions.checkArgument(r3);
        Timeline r5 = r30.timeline;
        long r6 = getContentPositionInternal(r30);
        PlaybackInfo r8 = r30.copyWithTimeline(r31);
        if (r31.isEmpty() == false) goto L12;
        MediaSource.MediaPeriodId r9 = PlaybackInfo.getDummyPeriodForEmptyTimeline();
        long r10 = Util.msToUs(this.maskingWindowPositionMs);
        PlaybackInfo r4 = r8.copyWithNewPosition(r9, r10, r10, r10, 0, TrackGroupArray.EMPTY, this.emptyTrackSelectorResult, ImmutableList.m212of()).copyWithLoadingMediaPeriodId(r9);
        r4.bufferedPositionUs = r4.positionUs;
        return r4;
    L12:
        Object r7 = r8.periodId.periodUid;
        boolean r11 = !r7.equals(((Pair) Util.castNonNull(r32)).first);
        if (r11 == false) goto L15;
        MediaSource.MediaPeriodId r12 = new MediaSource.MediaPeriodId(r32.first);
    L16:
        long r22 = ((Long) r32.second).longValue();
        long r13 = Util.msToUs(r6);
        if (r5.isEmpty() == true) goto L19;
        long r24 = r13 - r5.getPeriodByUid(r7, this.period).getPositionInWindowUs();
    L20:
        if (r11 == true) goto L41;
        if (r22 < r24) goto L41;
        if (r22 != r24) goto L36;
        int r14 = r31.getIndexOfPeriod(r8.loadingMediaPeriodId.periodUid);
        if (r14 != (-1)) goto L29;
    L30:
        r31.getPeriodByUid(r12.periodUid, this.period);
        if (r12.isAd() == false) goto L33;
        long r15 = this.period.getAdDurationUs(r12.adGroupIndex, r12.adIndexInAdGroup);
    L34:
        long r16 = r15;
        r8 = r8.copyWithNewPosition(r12, r8.positionUs, r8.positionUs, r8.discontinuityStartPositionUs, r16 - r8.positionUs, r8.trackGroups, r8.trackSelectorResult, r8.staticMetadata).copyWithLoadingMediaPeriodId(r12);
        r8.bufferedPositionUs = r16;
    L55:
        return r8;
    L33:
        r15 = this.period.durationUs;
        goto L34
    L29:
        if (r31.getPeriod(r14, this.period).windowIndex == r31.getPeriodByUid(r12.periodUid, this.period).windowIndex) goto L55;
    L36:
        Assertions.checkState(!r12.isAd());
        long r17 = Math.max(0, r8.totalBufferedDurationUs - (r22 - r24));
        long r1 = r8.bufferedPositionUs;
        if (r8.loadingMediaPeriodId.equals(r8.periodId) == false) goto L39;
        r1 = r22 + r17;
    L39:
        PlaybackInfo r18 = r8.copyWithNewPosition(r12, r22, r22, r22, r17, r8.trackGroups, r8.trackSelectorResult, r8.staticMetadata);
        r18.bufferedPositionUs = r1;
        return r18;
    L41:
        Assertions.checkState(!r12.isAd());
        if (r11 == false) goto L44;
        TrackGroupArray r2 = TrackGroupArray.EMPTY;
    L45:
        TrackGroupArray r19 = r2;
        if (r11 == false) goto L48;
        TrackSelectorResult r20 = this.emptyTrackSelectorResult;
    L49:
        TrackSelectorResult r110 = r20;
        if (r11 == false) goto L52;
        List<Metadata> r21 = ImmutableList.m212of();
    L53:
        PlaybackInfo r23 = r8.copyWithNewPosition(r12, r22, r22, r22, 0, r19, r110, r21).copyWithLoadingMediaPeriodId(r12);
        r23.bufferedPositionUs = r22;
        return r23;
    L52:
        r21 = r8.staticMetadata;
        goto L53
    L48:
        r20 = r8.trackSelectorResult;
        goto L49
    L44:
        r2 = r8.trackGroups;
        goto L45
    L19:
        r24 = r13;
        goto L20
    L15:
        r12 = r8.periodId;
    L7:
        r3 = true;
        goto L8
    }

    private Pair<Object, Long> maskWindowPositionMsOrGetPeriodPositionUs(Timeline r7, int r8, long r9) {
        if (r7.isEmpty() == false) goto L11;
        this.maskingWindowIndex = r8;
        if (r9 != C0180C.TIME_UNSET) goto L7;
        long r0 = 0;
    L8:
        this.maskingWindowPositionMs = r0;
        this.maskingPeriodIndex = 0;
        return null;
    L7:
        r0 = r9;
        goto L8
    L11:
        if (r8 != (-1)) goto L13;
    L16:
        int r10 = r7.getFirstWindowIndex(this.shuffleModeEnabled);
        r9 = r7.getWindow(r10, this.window).getDefaultPositionMs();
        int r3 = r10;
    L18:
        return r7.getPeriodPositionUs(this.window, this.period, r3, Util.msToUs(r9));
    L13:
        if (r8 >= r7.getWindowCount()) goto L16;
        r3 = r8;
        goto L18
    }

    private void maybeNotifySurfaceSizeChanged(final int r4, final int r5) {
        if (r4 == this.surfaceSize.getWidth()) goto L5;
    L6:
        this.surfaceSize = new Size(r4, r5);
        this.listeners.sendEvent(24, new ExoPlayerImpl$$ExternalSyntheticLambda7(r4, r5));
        sendRendererMessage(2, 14, new Size(r4, r5));
        return;
    L5:
        if (r5 != this.surfaceSize.getHeight()) goto L6;
    }

    private void onSelectedOutputSuitabilityChanged(boolean r4) {
        if (r4 == true) goto L5;
        updatePlaybackInfoForPlayWhenReadyAndSuppressionReasonStates(this.playbackInfo.playWhenReady, 1, 3);
        return;
    L5:
        if (this.playbackInfo.playbackSuppressionReason != 3) goto L9;
        updatePlaybackInfoForPlayWhenReadyAndSuppressionReasonStates(this.playbackInfo.playWhenReady, 1, 0);
        return;
    }

    private long periodPositionUsToWindowPositionUs(Timeline r3, MediaSource.MediaPeriodId r4, long r5) {
        r3.getPeriodByUid(r4.periodUid, this.period);
        return r5 + this.period.getPositionInWindowUs();
    }

    private PlaybackInfo removeMediaItemsInternal(PlaybackInfo r12, int r13, int r14) {
        int r3 = getCurrentWindowIndexInternal(r12);
        long r4 = getContentPositionInternal(r12);
        Timeline r1 = r12.timeline;
        int r6 = this.mediaSourceHolderSnapshots.size();
        boolean r7 = true;
        this.pendingOperationAcks++;
        removeMediaSourceHolders(r13, r14);
        Timeline r2 = createMaskingTimeline();
        PlaybackInfo r8 = maskTimelineAndPosition(r12, r2, getPeriodPositionUsAfterTimelineChanged(r1, r2, r3, r4));
        if (r8.playbackState != 1) goto L5;
    L11:
        r7 = false;
    L12:
        if (r7 == false) goto L14;
        r8 = r8.copyWithPlaybackState(4);
    L14:
        this.internalPlayer.removeMediaSources(r13, r14, this.shuffleOrder);
        return r8;
    L5:
        if (r8.playbackState == 4) goto L11;
        if (r13 >= r14) goto L11;
        if (r14 != r6) goto L11;
        if (r3 < r8.timeline.getWindowCount()) goto L11;
        goto L11
    }

    private void removeMediaSourceHolders(int r3, int r4) {
        int r0 = r4 - 1;
    L3:
        if (r0 < r3) goto L5;
        this.mediaSourceHolderSnapshots.remove(r0);
        r0 = r0 - 1;
        goto L3
    L5:
        this.shuffleOrder = this.shuffleOrder.cloneAndRemove(r3, r4);
    }

    private void removeSurfaceCallbacks() {
        if (this.sphericalGLSurfaceView == null) goto L6;
        createMessageInternal(this.frameMetadataListener).setType(10000).setPayload(null).send();
        this.sphericalGLSurfaceView.removeVideoSurfaceListener(this.componentListener);
        this.sphericalGLSurfaceView = null;
    L6:
        if (this.textureView == null) goto L13;
        if (this.textureView.getSurfaceTextureListener() == this.componentListener) goto L10;
        Log.m48w(TAG, "SurfaceTextureListener already unset or replaced.");
    L11:
        this.textureView = null;
        goto L13
    L10:
        this.textureView.setSurfaceTextureListener(null);
    L13:
        if (this.surfaceHolder == null) goto L16;
        this.surfaceHolder.removeCallback(this.componentListener);
        this.surfaceHolder = null;
        return;
    }

    private void sendRendererMessage(int r6, int r7, Object r8) {
        Renderer[] r0 = this.renderers;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L10;
        Renderer r3 = r0[r2];
        if (r6 != (-1)) goto L7;
    L8:
        createMessageInternal(r3).setType(r7).setPayload(r8).send();
    L9:
        r2 = r2 + 1;
        goto L3
    L7:
        if (r3.getTrackType() != r6) goto L9;
    }

    private void sendRendererMessage(int r2, Object r3) {
        sendRendererMessage(-1, r2, r3);
    }

    private void sendVolumeToRenderers() {
        sendRendererMessage(1, 2, Float.valueOf(this.volume * this.audioFocusManager.getVolumeMultiplier()));
    }

    private void setMediaSourcesInternal(List<MediaSource> r20, int r21, long r22, boolean r24) {
        int r9 = getCurrentWindowIndexInternal(this.playbackInfo);
        long r10 = getCurrentPosition();
        boolean r3 = true;
        this.pendingOperationAcks++;
        if (this.mediaSourceHolderSnapshots.isEmpty() == true) goto L5;
        removeMediaSourceHolders(0, this.mediaSourceHolderSnapshots.size());
    L5:
        List<MediaSourceList.MediaSourceHolder> r14 = addMediaSourceHolders(0, r20);
        Timeline r2 = createMaskingTimeline();
        if (r2.isEmpty() == true) goto L14;
        if (r21 < r2.getWindowCount()) goto L14;
        throw new IllegalSeekPositionException(r2, r21, r22);
    L14:
        if (r24 == false) goto L16;
        int r1 = r2.getFirstWindowIndex(this.shuffleModeEnabled);
        r22 = C0180C.TIME_UNSET;
        int r15 = r1;
    L19:
        PlaybackInfo r4 = maskTimelineAndPosition(this.playbackInfo, r2, maskWindowPositionMsOrGetPeriodPositionUs(r2, r15, r22));
        int r8 = r4.playbackState;
        if (r15 != (-1)) goto L22;
    L30:
        int r7 = r8;
    L31:
        PlaybackInfo r5 = r4.copyWithPlaybackState(r7);
        this.internalPlayer.setMediaSources(r14, r15, Util.msToUs(r22), this.shuffleOrder);
        if (this.playbackInfo.periodId.periodUid.equals(r5.periodId.periodUid) == false) goto L34;
    L36:
        r3 = false;
    L37:
        updatePlaybackInfo(r5, 0, r3, 4, getCurrentPositionUsInternal(r5), -1, false);
        return;
    L34:
        if (this.playbackInfo.timeline.isEmpty() == true) goto L36;
    L22:
        if (r4.playbackState == 1) goto L30;
        if (r2.isEmpty() == false) goto L26;
    L29:
        r7 = 4;
        goto L31
    L26:
        if (r15 >= r2.getWindowCount()) goto L29;
        r7 = 2;
        goto L31
    L16:
        if (r21 != (-1)) goto L18;
        r22 = r10;
        r15 = r9;
        goto L19
    L18:
        r15 = r21;
        goto L19
    }

    private void setNonVideoOutputSurfaceHolderInternal(SurfaceHolder r5) {
        this.surfaceHolderSurfaceIsVideoOutput = false;
        this.surfaceHolder = r5;
        this.surfaceHolder.addCallback(this.componentListener);
        Surface r1 = this.surfaceHolder.getSurface();
        if (r1 != null) goto L5;
    L7:
        maybeNotifySurfaceSizeChanged(0, 0);
        return;
    L5:
        if (r1.isValid() == false) goto L7;
        Rect r0 = this.surfaceHolder.getSurfaceFrame();
        maybeNotifySurfaceSizeChanged(r0.width(), r0.height());
    }

    private void setSurfaceTextureInternal(SurfaceTexture r2) {
        Surface r0 = new Surface(r2);
        setVideoOutputInternal(r0);
        this.ownedSurface = r0;
    }

    private void setVideoOutputInternal(Object r8) {
        ArrayList r0 = new ArrayList();
        Renderer[] r1 = this.renderers;
        int r2 = r1.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L8;
        Renderer r4 = r1[r3];
        if (r4.getTrackType() != 2) goto L7;
        r0.add(createMessageInternal(r4).setType(1).setPayload(r8).send());
    L7:
        r3 = r3 + 1;
        goto L3
    L8:
        boolean r5 = false;
        if (this.videoOutput != null) goto L11;
    L25:
        this.videoOutput = r8;
        if (r5 == false) goto L36;
        stopInternal(ExoPlaybackException.createForUnexpected(new ExoTimeoutException(3), 1003));
        return;
    L36:
        return;
    L11:
        if (this.videoOutput == r8) goto L25;
        Iterator r6 = r0.iterator();     // Catch: TimeoutException -> L17 InterruptedException -> L19
    L14:
        if (r6.hasNext() == false) goto L23;
        ((PlayerMessage) r6.next()).blockUntilDelivered(this.detachSurfaceTimeoutMs);     // Catch: TimeoutException -> L17 InterruptedException -> L19
    L23:
        if (this.videoOutput != this.ownedSurface) goto L25;
        this.ownedSurface.release();
        this.ownedSurface = null;
    L20:
        Thread.currentThread().interrupt();
    L18:
        r5 = true;
        goto L23
    }

    private void stopInternal(ExoPlaybackException r12) {
        PlaybackInfo r0 = this.playbackInfo.copyWithLoadingMediaPeriodId(this.playbackInfo.periodId);
        r0.bufferedPositionUs = r0.positionUs;
        r0.totalBufferedDurationUs = 0;
        PlaybackInfo r1 = r0.copyWithPlaybackState(1);
        if (r12 == null) goto L5;
        PlaybackInfo r3 = r1.copyWithPlaybackError(r12);
    L6:
        this.pendingOperationAcks++;
        this.internalPlayer.stop();
        updatePlaybackInfo(r3, 0, false, 5, C0180C.TIME_UNSET, -1, false);
        return;
    L5:
        r3 = r1;
        goto L6
    }

    private void updateAvailableCommands() {
        Player.Commands r0 = this.availableCommands;
        this.availableCommands = Util.getAvailableCommands(this.wrappingPlayer, this.permanentAvailableCommands);
        if (this.availableCommands.equals(r0) == true) goto L6;
        this.listeners.queueEvent(13, new ExoPlayerImpl$$ExternalSyntheticLambda20(this));
        return;
    }

    private void updateMediaSourcesWithMediaItems(int r12, int r13, List<MediaItem> r14) {
        this.pendingOperationAcks++;
        this.internalPlayer.updateMediaSourcesWithMediaItems(r12, r13, r14);
        int r0 = r12;
    L3:
        if (r0 >= r13) goto L5;
        MediaSourceHolderSnapshot r1 = this.mediaSourceHolderSnapshots.get(r0);
        r1.updateTimeline(new TimelineWithUpdatedMediaItem(r1.getTimeline(), r14.get(r0 - r12)));
        r0 = r0 + 1;
        goto L3
    L5:
        updatePlaybackInfo(this.playbackInfo.copyWithTimeline(createMaskingTimeline()), 0, false, 4, C0180C.TIME_UNSET, -1, false);
    }

    private void updatePlayWhenReady(boolean r3, int r4, int r5) {
        if (r3 == true) goto L4;
    L6:
        boolean r0 = false;
    L7:
        int r6 = computePlaybackSuppressionReason(r0, r4);
        if (this.playbackInfo.playWhenReady == r0) goto L10;
    L14:
        updatePlaybackInfoForPlayWhenReadyAndSuppressionReasonStates(r0, r5, r6);
        return;
    L10:
        if (this.playbackInfo.playbackSuppressionReason != r6) goto L14;
        if (this.playbackInfo.playWhenReadyChangeReason != r5) goto L14;
        return;
    L4:
        if (r4 == (-1)) goto L6;
        r0 = true;
        goto L7
    }

    private void updatePlaybackInfo(final PlaybackInfo r20, final int r21, boolean r22, final int r23, long r24, int r26, boolean r27) {
        PlaybackInfo r2 = this.playbackInfo;
        this.playbackInfo = r20;
        boolean r5 = !r2.timeline.equals(r20.timeline);
        Pair<Boolean, Integer> r9 = evaluateMediaItemTransitionReason(r20, r2, r22, r23, r5, r27);
        boolean r3 = ((Boolean) r9.first).booleanValue();
        final int r6 = ((Integer) r9.second).intValue();
        MediaItem r10 = null;
        if (r3 == true) goto L5;
    L8:
        if (r3 == false) goto L10;
    L11:
        this.staticAndDynamicMediaMetadata = this.staticAndDynamicMediaMetadata.buildUpon().populateFromMetadata(r20.staticMetadata).build();
    L12:
        MediaMetadata r11 = buildUpdatedMediaMetadata();
        boolean r12 = !r11.equals(this.mediaMetadata);
        this.mediaMetadata = r11;
        if (r2.playWhenReady == r20.playWhenReady) goto L15;
        boolean r13 = true;
    L17:
        if (r2.playbackState == r20.playbackState) goto L19;
        boolean r8 = true;
    L20:
        if (r8 == true) goto L22;
        if (r13 == true) goto L22;
    L24:
        if (r2.isLoading == r20.isLoading) goto L26;
        boolean r14 = true;
    L27:
        if (r14 == false) goto L29;
        updatePriorityTaskManagerForIsLoadingChange(r20.isLoading);
    L29:
        if (r5 == false) goto L32;
        this.listeners.queueEvent(0, new ExoPlayerImpl$$ExternalSyntheticLambda0(r20, r21));
    L32:
        if (r22 == false) goto L35;
        final Player.PositionInfo r7 = getPreviousPositionInfo(r23, r2, r26);
        final Player.PositionInfo r4 = getPositionInfo(r24);
        this.listeners.queueEvent(11, new ExoPlayerImpl$$ExternalSyntheticLambda24(r23, r7, r4));
    L35:
        if (r3 == false) goto L38;
        final MediaItem r15 = r10;
        this.listeners.queueEvent(1, new ExoPlayerImpl$$ExternalSyntheticLambda25(r15, r6));
    L38:
        if (r2.playbackError == r20.playbackError) goto L43;
        this.listeners.queueEvent(10, new ExoPlayerImpl$$ExternalSyntheticLambda26(r20));
        if (r20.playbackError == null) goto L43;
        this.listeners.queueEvent(10, new ExoPlayerImpl$$ExternalSyntheticLambda27(r20));
    L43:
        if (r2.trackSelectorResult == r20.trackSelectorResult) goto L45;
        this.trackSelector.onSelectionActivated(r20.trackSelectorResult.info);
        this.listeners.queueEvent(2, new ExoPlayerImpl$$ExternalSyntheticLambda28(r20));
    L45:
        if (r12 == false) goto L47;
        final MediaMetadata r16 = this.mediaMetadata;
        this.listeners.queueEvent(14, new ExoPlayerImpl$$ExternalSyntheticLambda1(r16));
    L47:
        if (r14 == false) goto L49;
        this.listeners.queueEvent(3, new ExoPlayerImpl$$ExternalSyntheticLambda2(r20));
    L49:
        if (r8 != 0) goto L51;
        if (r13 == true) goto L51;
    L52:
        if (r8 == false) goto L54;
        this.listeners.queueEvent(4, new ExoPlayerImpl$$ExternalSyntheticLambda4(r20));
    L54:
        if (r13 == false) goto L56;
    L57:
        this.listeners.queueEvent(5, new ExoPlayerImpl$$ExternalSyntheticLambda11(r20));
    L59:
        if (r2.playbackSuppressionReason == r20.playbackSuppressionReason) goto L62;
        this.listeners.queueEvent(6, new ExoPlayerImpl$$ExternalSyntheticLambda21(r20));
    L62:
        if (r2.isPlaying() == r20.isPlaying()) goto L65;
        this.listeners.queueEvent(7, new ExoPlayerImpl$$ExternalSyntheticLambda22(r20));
    L65:
        if (r2.playbackParameters.equals(r20.playbackParameters) == true) goto L67;
        this.listeners.queueEvent(12, new ExoPlayerImpl$$ExternalSyntheticLambda23(r20));
    L67:
        updateAvailableCommands();
        this.listeners.flushEvents();
        if (r2.sleepingForOffload == r20.sleepingForOffload) goto L73;
        Iterator<ExoPlayer.AudioOffloadListener> r17 = this.audioOffloadListeners.iterator();
    L71:
        if (r17.hasNext() == false) goto L75;
        r17.next().onSleepingForOffloadChanged(r20.sleepingForOffload);
        goto L71
    L75:
        return;
    L73:
        return;
    L56:
        if (r2.playWhenReadyChangeReason == r20.playWhenReadyChangeReason) goto L59;
    L51:
        this.listeners.queueEvent(-1, new ExoPlayerImpl$$ExternalSyntheticLambda3(r20));
        goto L52
    L26:
        r14 = false;
    L22:
        updateWakeAndWifiLock();
        goto L24
    L19:
        r8 = false;
        goto L20
    L15:
        r13 = false;
        goto L17
    L10:
        if (r2.staticMetadata.equals(r20.staticMetadata) == true) goto L12;
    L5:
        if (r20.timeline.isEmpty() == true) goto L7;
        r10 = r20.timeline.getWindow(r20.timeline.getPeriodByUid(r20.periodId.periodUid, this.period).windowIndex, this.window).mediaItem;
    L7:
        this.staticAndDynamicMediaMetadata = MediaMetadata.EMPTY;
        goto L8
    }

    private void updatePlaybackInfoForPlayWhenReadyAndSuppressionReasonStates(boolean r11, int r12, int r13) {
        this.pendingOperationAcks++;
        if (this.playbackInfo.sleepingForOffload == false) goto L5;
        PlaybackInfo r0 = this.playbackInfo.copyWithEstimatedPosition();
    L6:
        PlaybackInfo r2 = r0.copyWithPlayWhenReady(r11, r12, r13);
        this.internalPlayer.setPlayWhenReady(r11, r12, r13);
        updatePlaybackInfo(r2, 0, false, 5, C0180C.TIME_UNSET, -1, false);
        return;
    L5:
        r0 = this.playbackInfo;
        goto L6
    }

    private void updatePriorityTaskManagerForIsLoadingChange(boolean r3) {
        if (this.priorityTaskManager == null) goto L13;
        if (r3 == true) goto L6;
    L8:
        if (r3 == false) goto L10;
        return;
    L10:
        if (this.isPriorityTaskManagerRegistered == false) goto L16;
        this.priorityTaskManager.remove(this.priority);
        this.isPriorityTaskManagerRegistered = false;
        return;
    L16:
        return;
    L6:
        if (this.isPriorityTaskManagerRegistered == true) goto L8;
        this.priorityTaskManager.add(this.priority);
        this.isPriorityTaskManagerRegistered = true;
        return;
    }

    private void updateWakeAndWifiLock() {
        boolean r1 = false;
        switch(getPlaybackState()) {
            case 1: goto L11;
            case 2: goto L6;
            case 3: goto L6;
            case 4: goto L11;
            default: goto L5;
        };
    L6:
        boolean r2 = isSleepingForOffload();
        WakeLockManager r3 = this.wakeLockManager;
        if (getPlayWhenReady() == false) goto L10;
        if (r2 == true) goto L10;
        r1 = true;
    L10:
        r3.setStayAwake(r1);
        this.wifiLockManager.setStayAwake(getPlayWhenReady());
        return;
    L11:
        this.wakeLockManager.setStayAwake(false);
        this.wifiLockManager.setStayAwake(false);
        return;
    L5:
        throw new IllegalStateException();
    }

    private void verifyApplicationThread() {
        this.constructorFinished.blockUninterruptible();
        if (Thread.currentThread() == getApplicationLooper().getThread()) goto L13;
        String r1 = Util.formatInvariant("Player is accessed on the wrong thread.\nCurrent thread: '%s'\nExpected thread: '%s'\nSee https://developer.android.com/guide/topics/media/issues/player-accessed-on-wrong-thread", new Object[]{Thread.currentThread().getName(), getApplicationLooper().getThread().getName()});
        if (this.throwsWhenUsingWrongThread == true) goto L12;
        if (this.hasNotifiedFullWrongThreadWarning == false) goto L9;
        IllegalStateException r2 = null;
    L10:
        Log.m49w(TAG, r1, r2);
        this.hasNotifiedFullWrongThreadWarning = true;
        return;
    L9:
        r2 = new IllegalStateException();
        goto L10
    L12:
        throw new IllegalStateException(r1);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void addAnalyticsListener(AnalyticsListener r3) {
        this.analyticsCollector.addListener((AnalyticsListener) Assertions.checkNotNull(r3));
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void addAudioOffloadListener(ExoPlayer.AudioOffloadListener r2) {
        this.audioOffloadListeners.add(r2);
    }

    @Override // androidx.media3.common.Player
    public void addListener(Player.Listener r3) {
        this.listeners.add((Player.Listener) Assertions.checkNotNull(r3));
    }

    @Override // androidx.media3.common.Player
    public void addMediaItems(int r2, List<MediaItem> r3) {
        verifyApplicationThread();
        addMediaSources(r2, createMediaSources(r3));
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void addMediaSource(int r2, MediaSource r3) {
        verifyApplicationThread();
        addMediaSources(r2, Collections.singletonList(r3));
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void addMediaSource(MediaSource r2) {
        verifyApplicationThread();
        addMediaSources(Collections.singletonList(r2));
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void addMediaSources(int r11, List<MediaSource> r12) {
        verifyApplicationThread();
        boolean r0 = true;
        if (r11 < 0) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkArgument(r2);
        int r13 = Math.min(r11, this.mediaSourceHolderSnapshots.size());
        if (this.mediaSourceHolderSnapshots.isEmpty() == true) goto L9;
        updatePlaybackInfo(addMediaSourcesInternal(this.playbackInfo, r13, r12), 0, false, 5, C0180C.TIME_UNSET, -1, false);
        return;
    L9:
        if (this.maskingWindowIndex == (-1)) goto L12;
        r0 = false;
    L12:
        setMediaSources(r12, r0);
        return;
    L5:
        r2 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void addMediaSources(List<MediaSource> r2) {
        verifyApplicationThread();
        addMediaSources(this.mediaSourceHolderSnapshots.size(), r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.AudioComponent
    public void clearAuxEffectInfo() {
        verifyApplicationThread();
        setAuxEffectInfo(new AuxEffectInfo(0, 0.0f));
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public void clearCameraMotionListener(CameraMotionListener r3) {
        verifyApplicationThread();
        if (this.cameraMotionListener == r3) goto L5;
        return;
    L5:
        createMessageInternal(this.frameMetadataListener).setType(8).setPayload(null).send();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public void clearVideoFrameMetadataListener(VideoFrameMetadataListener r3) {
        verifyApplicationThread();
        if (this.videoFrameMetadataListener == r3) goto L5;
        return;
    L5:
        createMessageInternal(this.frameMetadataListener).setType(7).setPayload(null).send();
    }

    @Override // androidx.media3.common.Player
    public void clearVideoSurface() {
        verifyApplicationThread();
        removeSurfaceCallbacks();
        setVideoOutputInternal(null);
        maybeNotifySurfaceSizeChanged(0, 0);
    }

    @Override // androidx.media3.common.Player
    public void clearVideoSurface(Surface r2) {
        verifyApplicationThread();
        if (r2 != null) goto L5;
        return;
    L5:
        if (r2 != this.videoOutput) goto L9;
        clearVideoSurface();
        return;
    }

    @Override // androidx.media3.common.Player
    public void clearVideoSurfaceHolder(SurfaceHolder r2) {
        verifyApplicationThread();
        if (r2 != null) goto L5;
        return;
    L5:
        if (r2 != this.surfaceHolder) goto L9;
        clearVideoSurface();
        return;
    }

    @Override // androidx.media3.common.Player
    public void clearVideoSurfaceView(SurfaceView r2) {
        verifyApplicationThread();
        if (r2 != null) goto L5;
        SurfaceHolder r0 = null;
    L6:
        clearVideoSurfaceHolder(r0);
        return;
    L5:
        r0 = r2.getHolder();
        goto L6
    }

    @Override // androidx.media3.common.Player
    public void clearVideoTextureView(TextureView r2) {
        verifyApplicationThread();
        if (r2 != null) goto L5;
        return;
    L5:
        if (r2 != this.textureView) goto L9;
        clearVideoSurface();
        return;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public PlayerMessage createMessage(PlayerMessage.Target r2) {
        verifyApplicationThread();
        return createMessageInternal(r2);
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void decreaseDeviceVolume() {
        verifyApplicationThread();
        if (this.streamVolumeManager == null) goto L6;
        this.streamVolumeManager.decreaseVolume(1);
        return;
    }

    @Override // androidx.media3.common.Player
    public void decreaseDeviceVolume(int r2) {
        verifyApplicationThread();
        if (this.streamVolumeManager == null) goto L6;
        this.streamVolumeManager.decreaseVolume(r2);
        return;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public AnalyticsCollector getAnalyticsCollector() {
        verifyApplicationThread();
        return this.analyticsCollector;
    }

    @Override // androidx.media3.common.Player
    public Looper getApplicationLooper() {
        return this.applicationLooper;
    }

    @Override // androidx.media3.common.Player
    public AudioAttributes getAudioAttributes() {
        verifyApplicationThread();
        return this.audioAttributes;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    @Deprecated
    public ExoPlayer.AudioComponent getAudioComponent() {
        verifyApplicationThread();
        return this;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public DecoderCounters getAudioDecoderCounters() {
        verifyApplicationThread();
        return this.audioDecoderCounters;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public Format getAudioFormat() {
        verifyApplicationThread();
        return this.audioFormat;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.AudioComponent
    public int getAudioSessionId() {
        verifyApplicationThread();
        return this.audioSessionId;
    }

    @Override // androidx.media3.common.Player
    public Player.Commands getAvailableCommands() {
        verifyApplicationThread();
        return this.availableCommands;
    }

    @Override // androidx.media3.common.Player
    public long getBufferedPosition() {
        verifyApplicationThread();
        if (isPlayingAd() == false) goto L10;
        if (this.playbackInfo.loadingMediaPeriodId.equals(this.playbackInfo.periodId) == false) goto L8;
        return Util.usToMs(this.playbackInfo.bufferedPositionUs);
    L8:
        return getDuration();
    L10:
        return getContentBufferedPosition();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public Clock getClock() {
        return this.clock;
    }

    @Override // androidx.media3.common.Player
    public long getContentBufferedPosition() {
        verifyApplicationThread();
        if (this.playbackInfo.timeline.isEmpty() == false) goto L7;
        return this.maskingWindowPositionMs;
    L7:
        if (this.playbackInfo.loadingMediaPeriodId.windowSequenceNumber != this.playbackInfo.periodId.windowSequenceNumber) goto L9;
        long r0 = this.playbackInfo.bufferedPositionUs;
        if (this.playbackInfo.loadingMediaPeriodId.isAd() == false) goto L16;
        Timeline.Period r2 = this.playbackInfo.timeline.getPeriodByUid(this.playbackInfo.loadingMediaPeriodId.periodUid, this.period);
        r0 = r2.getAdGroupTimeUs(this.playbackInfo.loadingMediaPeriodId.adGroupIndex);
        if (r0 != Long.MIN_VALUE) goto L16;
        r0 = r2.durationUs;
    L16:
        return Util.usToMs(periodPositionUsToWindowPositionUs(this.playbackInfo.timeline, this.playbackInfo.loadingMediaPeriodId, r0));
    L9:
        return this.playbackInfo.timeline.getWindow(getCurrentMediaItemIndex(), this.window).getDurationMs();
    }

    @Override // androidx.media3.common.Player
    public long getContentPosition() {
        verifyApplicationThread();
        return getContentPositionInternal(this.playbackInfo);
    }

    @Override // androidx.media3.common.Player
    public int getCurrentAdGroupIndex() {
        verifyApplicationThread();
        if (isPlayingAd() == true) goto L7;
        return -1;
    L7:
        return this.playbackInfo.periodId.adGroupIndex;
    }

    @Override // androidx.media3.common.Player
    public int getCurrentAdIndexInAdGroup() {
        verifyApplicationThread();
        if (isPlayingAd() == true) goto L7;
        return -1;
    L7:
        return this.playbackInfo.periodId.adIndexInAdGroup;
    }

    @Override // androidx.media3.common.Player
    public CueGroup getCurrentCues() {
        verifyApplicationThread();
        return this.currentCueGroup;
    }

    @Override // androidx.media3.common.Player
    public int getCurrentMediaItemIndex() {
        verifyApplicationThread();
        int r0 = getCurrentWindowIndexInternal(this.playbackInfo);
        if (r0 != (-1)) goto L6;
        return 0;
    L6:
        return r0;
    }

    @Override // androidx.media3.common.Player
    public int getCurrentPeriodIndex() {
        verifyApplicationThread();
        if (this.playbackInfo.timeline.isEmpty() == false) goto L7;
        return this.maskingPeriodIndex;
    L7:
        return this.playbackInfo.timeline.getIndexOfPeriod(this.playbackInfo.periodId.periodUid);
    }

    @Override // androidx.media3.common.Player
    public long getCurrentPosition() {
        verifyApplicationThread();
        return Util.usToMs(getCurrentPositionUsInternal(this.playbackInfo));
    }

    @Override // androidx.media3.common.Player
    public Timeline getCurrentTimeline() {
        verifyApplicationThread();
        return this.playbackInfo.timeline;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public TrackGroupArray getCurrentTrackGroups() {
        verifyApplicationThread();
        return this.playbackInfo.trackGroups;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public TrackSelectionArray getCurrentTrackSelections() {
        verifyApplicationThread();
        return new TrackSelectionArray(this.playbackInfo.trackSelectorResult.selections);
    }

    @Override // androidx.media3.common.Player
    public Tracks getCurrentTracks() {
        verifyApplicationThread();
        return this.playbackInfo.trackSelectorResult.tracks;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    @Deprecated
    public ExoPlayer.DeviceComponent getDeviceComponent() {
        verifyApplicationThread();
        return this;
    }

    @Override // androidx.media3.common.Player
    public DeviceInfo getDeviceInfo() {
        verifyApplicationThread();
        return this.deviceInfo;
    }

    @Override // androidx.media3.common.Player
    public int getDeviceVolume() {
        verifyApplicationThread();
        if (this.streamVolumeManager != null) goto L5;
        return 0;
    L5:
        return this.streamVolumeManager.getVolume();
    }

    @Override // androidx.media3.common.Player
    public long getDuration() {
        verifyApplicationThread();
        if (isPlayingAd() == false) goto L7;
        MediaSource.MediaPeriodId r0 = this.playbackInfo.periodId;
        this.playbackInfo.timeline.getPeriodByUid(r0.periodUid, this.period);
        return Util.usToMs(this.period.getAdDurationUs(r0.adGroupIndex, r0.adIndexInAdGroup));
    L7:
        return getContentDuration();
    }

    @Override // androidx.media3.common.Player
    public long getMaxSeekToPreviousPosition() {
        verifyApplicationThread();
        return this.maxSeekToPreviousPositionMs;
    }

    @Override // androidx.media3.common.Player
    public MediaMetadata getMediaMetadata() {
        verifyApplicationThread();
        return this.mediaMetadata;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public boolean getPauseAtEndOfMediaItems() {
        verifyApplicationThread();
        return this.pauseAtEndOfMediaItems;
    }

    @Override // androidx.media3.common.Player
    public boolean getPlayWhenReady() {
        verifyApplicationThread();
        return this.playbackInfo.playWhenReady;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public Looper getPlaybackLooper() {
        return this.internalPlayer.getPlaybackLooper();
    }

    @Override // androidx.media3.common.Player
    public PlaybackParameters getPlaybackParameters() {
        verifyApplicationThread();
        return this.playbackInfo.playbackParameters;
    }

    @Override // androidx.media3.common.Player
    public int getPlaybackState() {
        verifyApplicationThread();
        return this.playbackInfo.playbackState;
    }

    @Override // androidx.media3.common.Player
    public int getPlaybackSuppressionReason() {
        verifyApplicationThread();
        return this.playbackInfo.playbackSuppressionReason;
    }

    @Override // androidx.media3.common.Player
    public /* bridge */ /* synthetic */ PlaybackException getPlayerError() {
        return getPlayerError();
    }

    @Override // androidx.media3.common.Player
    public ExoPlaybackException getPlayerError() {
        verifyApplicationThread();
        return this.playbackInfo.playbackError;
    }

    @Override // androidx.media3.common.Player
    public MediaMetadata getPlaylistMetadata() {
        verifyApplicationThread();
        return this.playlistMetadata;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public ExoPlayer.PreloadConfiguration getPreloadConfiguration() {
        return this.preloadConfiguration;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public Renderer getRenderer(int r2) {
        verifyApplicationThread();
        return this.renderers[r2];
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public int getRendererCount() {
        verifyApplicationThread();
        return this.renderers.length;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public int getRendererType(int r2) {
        verifyApplicationThread();
        return this.renderers[r2].getTrackType();
    }

    @Override // androidx.media3.common.Player
    public int getRepeatMode() {
        verifyApplicationThread();
        return this.repeatMode;
    }

    @Override // androidx.media3.common.Player
    public long getSeekBackIncrement() {
        verifyApplicationThread();
        return this.seekBackIncrementMs;
    }

    @Override // androidx.media3.common.Player
    public long getSeekForwardIncrement() {
        verifyApplicationThread();
        return this.seekForwardIncrementMs;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public SeekParameters getSeekParameters() {
        verifyApplicationThread();
        return this.seekParameters;
    }

    @Override // androidx.media3.common.Player
    public boolean getShuffleModeEnabled() {
        verifyApplicationThread();
        return this.shuffleModeEnabled;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.AudioComponent
    public boolean getSkipSilenceEnabled() {
        verifyApplicationThread();
        return this.skipSilenceEnabled;
    }

    @Override // androidx.media3.common.Player
    public Size getSurfaceSize() {
        verifyApplicationThread();
        return this.surfaceSize;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    @Deprecated
    public ExoPlayer.TextComponent getTextComponent() {
        verifyApplicationThread();
        return this;
    }

    @Override // androidx.media3.common.Player
    public long getTotalBufferedDuration() {
        verifyApplicationThread();
        return Util.usToMs(this.playbackInfo.totalBufferedDurationUs);
    }

    @Override // androidx.media3.common.Player
    public TrackSelectionParameters getTrackSelectionParameters() {
        verifyApplicationThread();
        return this.trackSelector.getParameters();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public TrackSelector getTrackSelector() {
        verifyApplicationThread();
        return this.trackSelector;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public int getVideoChangeFrameRateStrategy() {
        verifyApplicationThread();
        return this.videoChangeFrameRateStrategy;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    @Deprecated
    public ExoPlayer.VideoComponent getVideoComponent() {
        verifyApplicationThread();
        return this;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public DecoderCounters getVideoDecoderCounters() {
        verifyApplicationThread();
        return this.videoDecoderCounters;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public Format getVideoFormat() {
        verifyApplicationThread();
        return this.videoFormat;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public int getVideoScalingMode() {
        verifyApplicationThread();
        return this.videoScalingMode;
    }

    @Override // androidx.media3.common.Player
    public VideoSize getVideoSize() {
        verifyApplicationThread();
        return this.videoSize;
    }

    @Override // androidx.media3.common.Player
    public float getVolume() {
        verifyApplicationThread();
        return this.volume;
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void increaseDeviceVolume() {
        verifyApplicationThread();
        if (this.streamVolumeManager == null) goto L6;
        this.streamVolumeManager.increaseVolume(1);
        return;
    }

    @Override // androidx.media3.common.Player
    public void increaseDeviceVolume(int r2) {
        verifyApplicationThread();
        if (this.streamVolumeManager == null) goto L6;
        this.streamVolumeManager.increaseVolume(r2);
        return;
    }

    @Override // androidx.media3.common.Player
    public boolean isDeviceMuted() {
        verifyApplicationThread();
        if (this.streamVolumeManager != null) goto L5;
        return false;
    L5:
        return this.streamVolumeManager.isMuted();
    }

    @Override // androidx.media3.common.Player
    public boolean isLoading() {
        verifyApplicationThread();
        return this.playbackInfo.isLoading;
    }

    @Override // androidx.media3.common.Player
    public boolean isPlayingAd() {
        verifyApplicationThread();
        return this.playbackInfo.periodId.isAd();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public boolean isReleased() {
        verifyApplicationThread();
        return this.playerReleased;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public boolean isSleepingForOffload() {
        verifyApplicationThread();
        return this.playbackInfo.sleepingForOffload;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public boolean isTunnelingEnabled() {
        verifyApplicationThread();
        RendererConfiguration[] r0 = this.playbackInfo.trackSelectorResult.rendererConfigurations;
        int r1 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L11;
        RendererConfiguration r4 = r0[r3];
        if (r4 == null) goto L10;
        if (r4.tunneling == false) goto L10;
        return true;
    L10:
        r3 = r3 + 1;
        goto L3
    L11:
        return false;
    }

    /* JADX INFO: renamed from: lambda$new$0$androidx-media3-exoplayer-ExoPlayerImpl, reason: not valid java name */
    /* synthetic */ void m467lambda$new$0$androidxmedia3exoplayerExoPlayerImpl(Player.Listener r3, FlagSet r4) {
        r3.onEvents(this.wrappingPlayer, new Player.Events(r4));
    }

    /* JADX INFO: renamed from: lambda$new$1$androidx-media3-exoplayer-ExoPlayerImpl, reason: not valid java name */
    /* synthetic */ void m468lambda$new$1$androidxmedia3exoplayerExoPlayerImpl(ExoPlayerImplInternal.PlaybackInfoUpdate r1) {
        handlePlaybackInfo(r1);
    }

    /* JADX INFO: renamed from: lambda$new$2$androidx-media3-exoplayer-ExoPlayerImpl, reason: not valid java name */
    /* synthetic */ void m469lambda$new$2$androidxmedia3exoplayerExoPlayerImpl(final ExoPlayerImplInternal.PlaybackInfoUpdate r3) {
        this.playbackInfoUpdateHandler.post(new ExoPlayerImpl$$ExternalSyntheticLambda10(this, r3));
    }

    /* JADX INFO: renamed from: lambda$setPlaylistMetadata$7$androidx-media3-exoplayer-ExoPlayerImpl */
    /* synthetic */ void m59x47ee3208(Player.Listener r2) {
        r2.onPlaylistMetadataChanged(this.playlistMetadata);
    }

    /* JADX INFO: renamed from: lambda$updateAvailableCommands$26$androidx-media3-exoplayer-ExoPlayerImpl */
    /* synthetic */ void m60x9a87546c(Player.Listener r2) {
        r2.onAvailableCommandsChanged(this.availableCommands);
    }

    @Override // androidx.media3.common.Player
    public void moveMediaItems(int r16, int r17, int r18) {
        verifyApplicationThread();
        if (r16 < 0) goto L7;
        if (r16 > r17) goto L7;
        if (r18 < 0) goto L7;
        boolean r4 = true;
    L8:
        Assertions.checkArgument(r4);
        int r10 = this.mediaSourceHolderSnapshots.size();
        int r11 = Math.min(r17, r10);
        int r12 = Math.min(r18, r10 - (r11 - r16));
        if (r16 >= r10) goto L15;
        if (r16 == r11) goto L16;
        if (r16 == r12) goto L17;
        Timeline r1 = getCurrentTimeline();
        this.pendingOperationAcks++;
        Util.moveItems(this.mediaSourceHolderSnapshots, r16, r11, r12);
        Timeline r2 = createMaskingTimeline();
        PlaybackInfo r3 = maskTimelineAndPosition(this.playbackInfo, r2, getPeriodPositionUsAfterTimelineChanged(r1, r2, getCurrentWindowIndexInternal(this.playbackInfo), getContentPositionInternal(this.playbackInfo)));
        this.internalPlayer.moveMediaSources(r16, r11, r12, this.shuffleOrder);
        updatePlaybackInfo(r3, 0, false, 5, C0180C.TIME_UNSET, -1, false);
        return;
    L17:
        return;
    L16:
        return;
    L15:
        return;
    L7:
        r4 = false;
        goto L8
    }

    @Override // androidx.media3.common.Player
    public void prepare() {
        verifyApplicationThread();
        boolean r0 = getPlayWhenReady();
        int r2 = 2;
        int r1 = this.audioFocusManager.updateAudioFocus(r0, 2);
        updatePlayWhenReady(r0, r1, getPlayWhenReadyChangeReason(r1));
        if (this.playbackInfo.playbackState == 1) goto L5;
        return;
    L5:
        PlaybackInfo r3 = this.playbackInfo.copyWithPlaybackError(null);
        if (r3.timeline.isEmpty() == false) goto L8;
        r2 = 4;
    L8:
        PlaybackInfo r6 = r3.copyWithPlaybackState(r2);
        this.pendingOperationAcks++;
        this.internalPlayer.prepare();
        updatePlaybackInfo(r6, 1, false, 5, C0180C.TIME_UNSET, -1, false);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    @Deprecated
    public void prepare(MediaSource r1) {
        verifyApplicationThread();
        setMediaSource(r1);
        prepare();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    @Deprecated
    public void prepare(MediaSource r1, boolean r2, boolean r3) {
        verifyApplicationThread();
        setMediaSource(r1, r2);
        prepare();
    }

    @Override // androidx.media3.common.Player
    public void release() {
        Log.m46i(TAG, "Release " + Integer.toHexString(System.identityHashCode(this)) + " [" + MediaLibraryInfo.VERSION_SLASHY + "] [" + Util.DEVICE_DEBUG_INFO + "] [" + MediaLibraryInfo.registeredModules() + "]");
        verifyApplicationThread();
        this.audioBecomingNoisyManager.setEnabled(false);
        if (this.streamVolumeManager == null) goto L5;
        this.streamVolumeManager.release();
    L5:
        this.wakeLockManager.setStayAwake(false);
        this.wifiLockManager.setStayAwake(false);
        this.audioFocusManager.release();
        if (this.internalPlayer.release() == true) goto L8;
        this.listeners.sendEvent(10, new ExoPlayerImpl$$ExternalSyntheticLambda9());
    L8:
        this.listeners.release();
        this.playbackInfoUpdateHandler.removeCallbacksAndMessages(null);
        this.bandwidthMeter.removeEventListener(this.analyticsCollector);
        if (this.playbackInfo.sleepingForOffload == false) goto L12;
        this.playbackInfo = this.playbackInfo.copyWithEstimatedPosition();
    L12:
        if (this.suitableOutputChecker != null) goto L14;
    L16:
        this.playbackInfo = this.playbackInfo.copyWithPlaybackState(1);
        this.playbackInfo = this.playbackInfo.copyWithLoadingMediaPeriodId(this.playbackInfo.periodId);
        this.playbackInfo.bufferedPositionUs = this.playbackInfo.positionUs;
        this.playbackInfo.totalBufferedDurationUs = 0;
        this.analyticsCollector.release();
        this.trackSelector.release();
        removeSurfaceCallbacks();
        if (this.ownedSurface == null) goto L20;
        this.ownedSurface.release();
        this.ownedSurface = null;
    L20:
        if (this.isPriorityTaskManagerRegistered == false) goto L22;
        ((PriorityTaskManager) Assertions.checkNotNull(this.priorityTaskManager)).remove(this.priority);
        this.isPriorityTaskManagerRegistered = false;
    L22:
        this.currentCueGroup = CueGroup.EMPTY_TIME_ZERO;
        this.playerReleased = true;
        return;
    L14:
        if (Util.SDK_INT < 35) goto L16;
        this.suitableOutputChecker.disable();
        goto L16
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void removeAnalyticsListener(AnalyticsListener r3) {
        verifyApplicationThread();
        this.analyticsCollector.removeListener((AnalyticsListener) Assertions.checkNotNull(r3));
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void removeAudioOffloadListener(ExoPlayer.AudioOffloadListener r2) {
        verifyApplicationThread();
        this.audioOffloadListeners.remove(r2);
    }

    @Override // androidx.media3.common.Player
    public void removeListener(Player.Listener r3) {
        verifyApplicationThread();
        this.listeners.remove((Player.Listener) Assertions.checkNotNull(r3));
    }

    @Override // androidx.media3.common.Player
    public void removeMediaItems(int r13, int r14) {
        verifyApplicationThread();
        if (r13 < 0) goto L6;
        if (r14 < r13) goto L6;
        boolean r1 = true;
    L7:
        Assertions.checkArgument(r1);
        int r2 = this.mediaSourceHolderSnapshots.size();
        int r15 = Math.min(r14, r2);
        if (r13 >= r2) goto L13;
        if (r13 == r15) goto L14;
        PlaybackInfo r4 = removeMediaItemsInternal(this.playbackInfo, r13, r15);
        updatePlaybackInfo(r4, 0, !r4.periodId.periodUid.equals(this.playbackInfo.periodId.periodUid), 4, getCurrentPositionUsInternal(r4), -1, false);
        return;
    L14:
        return;
    L13:
        return;
    L6:
        r1 = false;
        goto L7
    }

    @Override // androidx.media3.common.Player
    public void replaceMediaItems(int r14, int r15, List<MediaItem> r16) {
        verifyApplicationThread();
        boolean r2 = false;
        if (r14 < 0) goto L6;
        if (r15 < r14) goto L6;
        boolean r4 = true;
    L7:
        Assertions.checkArgument(r4);
        int r10 = this.mediaSourceHolderSnapshots.size();
        if (r14 <= r10) goto L10;
        return;
    L10:
        int r11 = Math.min(r15, r10);
        if (canUpdateMediaSourcesWithMediaItems(r14, r11, r16) == false) goto L14;
        updateMediaSourcesWithMediaItems(r14, r11, r16);
        return;
    L14:
        List<MediaSource> r12 = createMediaSources(r16);
        if (this.mediaSourceHolderSnapshots.isEmpty() == true) goto L17;
        PlaybackInfo r1 = removeMediaItemsInternal(addMediaSourcesInternal(this.playbackInfo, r11, r12), r14, r11);
        updatePlaybackInfo(r1, 0, true ^ r1.periodId.periodUid.equals(this.playbackInfo.periodId.periodUid), 4, getCurrentPositionUsInternal(r1), -1, false);
        return;
    L17:
        if (this.maskingWindowIndex != (-1)) goto L19;
        r2 = true;
    L19:
        setMediaSources(r12, r2);
        return;
    L6:
        r4 = false;
        goto L7
    }

    @Override // androidx.media3.common.BasePlayer
    public void seekTo(int r12, long r13, int r15, boolean r16) {
        verifyApplicationThread();
        if (r12 != (-1)) goto L6;
        return;
    L6:
        if (r12 < 0) goto L8;
        boolean r1 = true;
    L9:
        Assertions.checkArgument(r1);
        Timeline r2 = this.playbackInfo.timeline;
        if (r2.isEmpty() == false) goto L12;
    L14:
        this.analyticsCollector.notifySeekStarted();
        this.pendingOperationAcks++;
        if (isPlayingAd() == false) goto L18;
        Log.m48w(TAG, "seekTo ignored because an ad is playing");
        ExoPlayerImplInternal.PlaybackInfoUpdate r3 = new ExoPlayerImplInternal.PlaybackInfoUpdate(this.playbackInfo);
        r3.incrementPendingOperationAcks(1);
        this.playbackInfoUpdateListener.onPlaybackInfoUpdate(r3);
        return;
    L18:
        PlaybackInfo r0 = this.playbackInfo;
        if (this.playbackInfo.playbackState != 3) goto L21;
    L24:
        r0 = this.playbackInfo.copyWithPlaybackState(2);
    L25:
        int r9 = getCurrentMediaItemIndex();
        PlaybackInfo r4 = maskTimelineAndPosition(r0, r2, maskWindowPositionMsOrGetPeriodPositionUs(r2, r12, r13));
        this.internalPlayer.seekTo(r2, r12, Util.msToUs(r13));
        updatePlaybackInfo(r4, 0, true, 1, getCurrentPositionUsInternal(r4), r9, r16);
        return;
    L21:
        if (this.playbackInfo.playbackState != 4) goto L25;
        if (r2.isEmpty() == true) goto L25;
    L12:
        if (r12 < r2.getWindowCount()) goto L14;
        return;
    L8:
        r1 = false;
        goto L9
    }

    @Override // androidx.media3.common.Player
    public void setAudioAttributes(final AudioAttributes r4, boolean r5) {
        verifyApplicationThread();
        if (this.playerReleased == false) goto L6;
        return;
    L6:
        if (Util.areEqual(this.audioAttributes, r4) == true) goto L11;
        this.audioAttributes = r4;
        sendRendererMessage(1, 3, r4);
        if (this.streamVolumeManager == null) goto L10;
        this.streamVolumeManager.setStreamType(Util.getStreamTypeForAudioUsage(r4.usage));
    L10:
        this.listeners.queueEvent(20, new ExoPlayerImpl$$ExternalSyntheticLambda18(r4));
    L11:
        AudioFocusManager r0 = this.audioFocusManager;
        if (r5 == false) goto L14;
        AudioAttributes r1 = r4;
    L15:
        r0.setAudioAttributes(r1);
        this.trackSelector.setAudioAttributes(r4);
        boolean r2 = getPlayWhenReady();
        int r3 = this.audioFocusManager.updateAudioFocus(r2, getPlaybackState());
        updatePlayWhenReady(r2, r3, getPlayWhenReadyChangeReason(r3));
        this.listeners.flushEvents();
        return;
    L14:
        r1 = null;
        goto L15
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.AudioComponent
    public void setAudioSessionId(int r5) {
        verifyApplicationThread();
        if (this.audioSessionId != r5) goto L5;
        return;
    L5:
        if (r5 != 0) goto L7;
        r5 = Util.generateAudioSessionIdV21(this.applicationContext);
    L7:
        this.audioSessionId = r5;
        sendRendererMessage(1, 10, Integer.valueOf(r5));
        sendRendererMessage(2, 10, Integer.valueOf(r5));
        final int r0 = r5;
        this.listeners.sendEvent(21, new ExoPlayerImpl$$ExternalSyntheticLambda8(r0));
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.AudioComponent
    public void setAuxEffectInfo(AuxEffectInfo r3) {
        verifyApplicationThread();
        sendRendererMessage(1, 6, r3);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public void setCameraMotionListener(CameraMotionListener r3) {
        verifyApplicationThread();
        this.cameraMotionListener = r3;
        createMessageInternal(this.frameMetadataListener).setType(8).setPayload(r3).send();
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void setDeviceMuted(boolean r3) {
        verifyApplicationThread();
        if (this.streamVolumeManager == null) goto L6;
        this.streamVolumeManager.setMuted(r3, 1);
        return;
    }

    @Override // androidx.media3.common.Player
    public void setDeviceMuted(boolean r2, int r3) {
        verifyApplicationThread();
        if (this.streamVolumeManager == null) goto L6;
        this.streamVolumeManager.setMuted(r2, r3);
        return;
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public void setDeviceVolume(int r3) {
        verifyApplicationThread();
        if (this.streamVolumeManager == null) goto L6;
        this.streamVolumeManager.setVolume(r3, 1);
        return;
    }

    @Override // androidx.media3.common.Player
    public void setDeviceVolume(int r2, int r3) {
        verifyApplicationThread();
        if (this.streamVolumeManager == null) goto L6;
        this.streamVolumeManager.setVolume(r2, r3);
        return;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setForegroundMode(boolean r3) {
        verifyApplicationThread();
        if (this.foregroundMode == r3) goto L8;
        this.foregroundMode = r3;
        if (this.internalPlayer.setForegroundMode(r3) == true) goto L9;
        stopInternal(ExoPlaybackException.createForUnexpected(new ExoTimeoutException(2), 1003));
        return;
    L9:
        return;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setHandleAudioBecomingNoisy(boolean r2) {
        verifyApplicationThread();
        if (this.playerReleased == false) goto L5;
        return;
    L5:
        this.audioBecomingNoisyManager.setEnabled(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setImageOutput(ImageOutput r3) {
        verifyApplicationThread();
        sendRendererMessage(4, 15, r3);
    }

    @Override // androidx.media3.common.Player
    public void setMediaItems(List<MediaItem> r2, int r3, long r4) {
        verifyApplicationThread();
        setMediaSources(createMediaSources(r2), r3, r4);
    }

    @Override // androidx.media3.common.Player
    public void setMediaItems(List<MediaItem> r2, boolean r3) {
        verifyApplicationThread();
        setMediaSources(createMediaSources(r2), r3);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setMediaSource(MediaSource r2) {
        verifyApplicationThread();
        setMediaSources(Collections.singletonList(r2));
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setMediaSource(MediaSource r3, long r4) {
        verifyApplicationThread();
        setMediaSources(Collections.singletonList(r3), 0, r4);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setMediaSource(MediaSource r2, boolean r3) {
        verifyApplicationThread();
        setMediaSources(Collections.singletonList(r2), r3);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setMediaSources(List<MediaSource> r2) {
        verifyApplicationThread();
        setMediaSources(r2, true);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setMediaSources(List<MediaSource> r7, int r8, long r9) {
        verifyApplicationThread();
        setMediaSourcesInternal(r7, r8, r9, false);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setMediaSources(List<MediaSource> r7, boolean r8) {
        verifyApplicationThread();
        setMediaSourcesInternal(r7, -1, C0180C.TIME_UNSET, r8);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setPauseAtEndOfMediaItems(boolean r2) {
        verifyApplicationThread();
        if (this.pauseAtEndOfMediaItems != r2) goto L5;
        return;
    L5:
        this.pauseAtEndOfMediaItems = r2;
        this.internalPlayer.setPauseAtEndOfWindow(r2);
    }

    @Override // androidx.media3.common.Player
    public void setPlayWhenReady(boolean r3) {
        verifyApplicationThread();
        int r0 = this.audioFocusManager.updateAudioFocus(r3, getPlaybackState());
        updatePlayWhenReady(r3, r0, getPlayWhenReadyChangeReason(r0));
    }

    @Override // androidx.media3.common.Player
    public void setPlaybackParameters(PlaybackParameters r11) {
        verifyApplicationThread();
        if (r11 != null) goto L6;
        r11 = PlaybackParameters.DEFAULT;
    L6:
        if (this.playbackInfo.playbackParameters.equals(r11) == false) goto L8;
        return;
    L8:
        PlaybackInfo r2 = this.playbackInfo.copyWithPlaybackParameters(r11);
        this.pendingOperationAcks++;
        this.internalPlayer.setPlaybackParameters(r11);
        updatePlaybackInfo(r2, 0, false, 5, C0180C.TIME_UNSET, -1, false);
    }

    @Override // androidx.media3.common.Player
    public void setPlaylistMetadata(MediaMetadata r4) {
        verifyApplicationThread();
        Assertions.checkNotNull(r4);
        if (r4.equals(this.playlistMetadata) == false) goto L5;
        return;
    L5:
        this.playlistMetadata = r4;
        this.listeners.sendEvent(15, new ExoPlayerImpl$$ExternalSyntheticLambda13(this));
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setPreferredAudioDevice(AudioDeviceInfo r3) {
        verifyApplicationThread();
        sendRendererMessage(1, 12, r3);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setPreloadConfiguration(ExoPlayer.PreloadConfiguration r2) {
        verifyApplicationThread();
        if (this.preloadConfiguration.equals(r2) == false) goto L5;
        return;
    L5:
        this.preloadConfiguration = r2;
        this.internalPlayer.setPreloadConfiguration(r2);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setPriority(int r3) {
        verifyApplicationThread();
        if (this.priority != r3) goto L6;
        return;
    L6:
        if (this.isPriorityTaskManagerRegistered == false) goto L8;
        PriorityTaskManager r0 = (PriorityTaskManager) Assertions.checkNotNull(this.priorityTaskManager);
        r0.add(r3);
        r0.remove(this.priority);
    L8:
        this.priority = r3;
        sendRendererMessage(16, Integer.valueOf(r3));
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setPriorityTaskManager(PriorityTaskManager r3) {
        verifyApplicationThread();
        if (Util.areEqual(this.priorityTaskManager, r3) == false) goto L6;
        return;
    L6:
        if (this.isPriorityTaskManagerRegistered == false) goto L8;
        ((PriorityTaskManager) Assertions.checkNotNull(this.priorityTaskManager)).remove(this.priority);
    L8:
        if (r3 != null) goto L10;
    L12:
        this.isPriorityTaskManagerRegistered = false;
    L13:
        this.priorityTaskManager = r3;
        return;
    L10:
        if (isLoading() == false) goto L12;
        r3.add(this.priority);
        this.isPriorityTaskManagerRegistered = true;
        goto L13
    }

    @Override // androidx.media3.common.Player
    public void setRepeatMode(final int r4) {
        verifyApplicationThread();
        if (this.repeatMode == r4) goto L6;
        this.repeatMode = r4;
        this.internalPlayer.setRepeatMode(r4);
        this.listeners.queueEvent(8, new ExoPlayerImpl$$ExternalSyntheticLambda12(r4));
        updateAvailableCommands();
        this.listeners.flushEvents();
        return;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setSeekParameters(SeekParameters r2) {
        verifyApplicationThread();
        if (r2 != null) goto L6;
        r2 = SeekParameters.DEFAULT;
    L6:
        if (this.seekParameters.equals(r2) == true) goto L9;
        this.seekParameters = r2;
        this.internalPlayer.setSeekParameters(r2);
        return;
    }

    @Override // androidx.media3.common.Player
    public void setShuffleModeEnabled(final boolean r4) {
        verifyApplicationThread();
        if (this.shuffleModeEnabled == r4) goto L6;
        this.shuffleModeEnabled = r4;
        this.internalPlayer.setShuffleModeEnabled(r4);
        this.listeners.queueEvent(9, new ExoPlayerImpl$$ExternalSyntheticLambda14(r4));
        updateAvailableCommands();
        this.listeners.flushEvents();
        return;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setShuffleOrder(ShuffleOrder r14) {
        verifyApplicationThread();
        if (r14.getLength() != this.mediaSourceHolderSnapshots.size()) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        this.shuffleOrder = r14;
        Timeline r1 = createMaskingTimeline();
        PlaybackInfo r5 = maskTimelineAndPosition(this.playbackInfo, r1, maskWindowPositionMsOrGetPeriodPositionUs(r1, getCurrentMediaItemIndex(), getCurrentPosition()));
        this.pendingOperationAcks++;
        this.internalPlayer.setShuffleOrder(r14);
        updatePlaybackInfo(r5, 0, false, 5, C0180C.TIME_UNSET, -1, false);
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.AudioComponent
    public void setSkipSilenceEnabled(final boolean r4) {
        verifyApplicationThread();
        if (this.skipSilenceEnabled != r4) goto L5;
        return;
    L5:
        this.skipSilenceEnabled = r4;
        sendRendererMessage(1, 9, Boolean.valueOf(r4));
        this.listeners.sendEvent(23, new ExoPlayerImpl$$ExternalSyntheticLambda5(r4));
    }

    void setThrowsWhenUsingWrongThread(boolean r2) {
        this.throwsWhenUsingWrongThread = r2;
        this.listeners.setThrowsWhenUsingWrongThread(r2);
        if ((this.analyticsCollector instanceof DefaultAnalyticsCollector) == false) goto L6;
        ((DefaultAnalyticsCollector) this.analyticsCollector).setThrowsWhenUsingWrongThread(r2);
        return;
    }

    @Override // androidx.media3.common.Player
    public void setTrackSelectionParameters(final TrackSelectionParameters r4) {
        verifyApplicationThread();
        if (this.trackSelector.isSetParametersSupported() == true) goto L5;
        return;
    L5:
        if (r4.equals(this.trackSelector.getParameters()) == true) goto L10;
        this.trackSelector.setParameters(r4);
        this.listeners.sendEvent(19, new ExoPlayerImpl$$ExternalSyntheticLambda19(r4));
        return;
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public void setVideoChangeFrameRateStrategy(int r4) {
        verifyApplicationThread();
        if (this.videoChangeFrameRateStrategy != r4) goto L5;
        return;
    L5:
        this.videoChangeFrameRateStrategy = r4;
        sendRendererMessage(2, 5, Integer.valueOf(r4));
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setVideoEffects(List<Effect> r5) {
        verifyApplicationThread();
        Class.forName("androidx.media3.effect.PreviewingSingleInputVideoGraph$Factory").getConstructor(new Class[]{VideoFrameProcessor.Factory.class});     // Catch: Throwable -> L6 ClassNotFoundException -> L8
        sendRendererMessage(2, 13, r5);
        return;
    L6:
        e = move-exception;
        throw new IllegalStateException("Could not find required lib-effect dependencies.", e);
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public void setVideoFrameMetadataListener(VideoFrameMetadataListener r3) {
        verifyApplicationThread();
        this.videoFrameMetadataListener = r3;
        createMessageInternal(this.frameMetadataListener).setType(7).setPayload(r3).send();
    }

    @Override // androidx.media3.exoplayer.ExoPlayer, androidx.media3.exoplayer.ExoPlayer.VideoComponent
    public void setVideoScalingMode(int r4) {
        verifyApplicationThread();
        this.videoScalingMode = r4;
        sendRendererMessage(2, 4, Integer.valueOf(r4));
    }

    @Override // androidx.media3.common.Player
    public void setVideoSurface(Surface r2) {
        verifyApplicationThread();
        removeSurfaceCallbacks();
        setVideoOutputInternal(r2);
        if (r2 != null) goto L5;
        int r0 = 0;
    L6:
        maybeNotifySurfaceSizeChanged(r0, r0);
        return;
    L5:
        r0 = -1;
        goto L6
    }

    @Override // androidx.media3.common.Player
    public void setVideoSurfaceHolder(SurfaceHolder r5) {
        verifyApplicationThread();
        if (r5 != null) goto L5;
        clearVideoSurface();
        return;
    L5:
        removeSurfaceCallbacks();
        this.surfaceHolderSurfaceIsVideoOutput = true;
        this.surfaceHolder = r5;
        r5.addCallback(this.componentListener);
        Surface r0 = r5.getSurface();
        if (r0 != null) goto L8;
    L10:
        setVideoOutputInternal(null);
        maybeNotifySurfaceSizeChanged(0, 0);
        return;
    L8:
        if (r0.isValid() == false) goto L10;
        setVideoOutputInternal(r0);
        Rect r1 = r5.getSurfaceFrame();
        maybeNotifySurfaceSizeChanged(r1.width(), r1.height());
    }

    @Override // androidx.media3.common.Player
    public void setVideoSurfaceView(SurfaceView r3) {
        verifyApplicationThread();
        if ((r3 instanceof VideoDecoderOutputBufferRenderer) == false) goto L6;
        removeSurfaceCallbacks();
        setVideoOutputInternal(r3);
        setNonVideoOutputSurfaceHolderInternal(r3.getHolder());
        return;
    L6:
        if ((r3 instanceof SphericalGLSurfaceView) == false) goto L8;
        removeSurfaceCallbacks();
        this.sphericalGLSurfaceView = (SphericalGLSurfaceView) r3;
        createMessageInternal(this.frameMetadataListener).setType(10000).setPayload(this.sphericalGLSurfaceView).send();
        this.sphericalGLSurfaceView.addVideoSurfaceListener(this.componentListener);
        setVideoOutputInternal(this.sphericalGLSurfaceView.getVideoSurface());
        setNonVideoOutputSurfaceHolderInternal(r3.getHolder());
        return;
    L8:
        if (r3 != null) goto L10;
        SurfaceHolder r0 = null;
    L11:
        setVideoSurfaceHolder(r0);
        return;
    L10:
        r0 = r3.getHolder();
        goto L11
    }

    @Override // androidx.media3.common.Player
    public void setVideoTextureView(TextureView r4) {
        verifyApplicationThread();
        if (r4 != null) goto L5;
        clearVideoSurface();
        return;
    L5:
        removeSurfaceCallbacks();
        this.textureView = r4;
        if (r4.getSurfaceTextureListener() == null) goto L8;
        Log.m48w(TAG, "Replacing existing SurfaceTextureListener.");
    L8:
        r4.setSurfaceTextureListener(this.componentListener);
        if (r4.isAvailable() == false) goto L11;
        SurfaceTexture r0 = r4.getSurfaceTexture();
    L12:
        if (r0 != null) goto L14;
        setVideoOutputInternal(null);
        maybeNotifySurfaceSizeChanged(0, 0);
        return;
    L14:
        setSurfaceTextureInternal(r0);
        maybeNotifySurfaceSizeChanged(r4.getWidth(), r4.getHeight());
        return;
    L11:
        r0 = null;
        goto L12
    }

    @Override // androidx.media3.common.Player
    public void setVolume(float r5) {
        verifyApplicationThread();
        final float r6 = Util.constrainValue(r5, 0.0f, 1.0f);
        if (this.volume != r6) goto L5;
        return;
    L5:
        this.volume = r6;
        sendVolumeToRenderers();
        this.listeners.sendEvent(22, new ExoPlayerImpl$$ExternalSyntheticLambda6(r6));
    }

    @Override // androidx.media3.exoplayer.ExoPlayer
    public void setWakeMode(int r4) {
        verifyApplicationThread();
        switch(r4) {
            case 0: goto L7;
            case 1: goto L6;
            case 2: goto L5;
            default: goto L11;
        };
    L5:
        this.wakeLockManager.setEnabled(true);
        this.wifiLockManager.setEnabled(true);
        return;
    L6:
        this.wakeLockManager.setEnabled(true);
        this.wifiLockManager.setEnabled(false);
        return;
    L7:
        this.wakeLockManager.setEnabled(false);
        this.wifiLockManager.setEnabled(false);
        return;
    }

    @Override // androidx.media3.common.Player
    public void stop() {
        verifyApplicationThread();
        this.audioFocusManager.updateAudioFocus(getPlayWhenReady(), 1);
        stopInternal(null);
        this.currentCueGroup = new CueGroup(ImmutableList.m212of(), this.playbackInfo.positionUs);
    }
}
