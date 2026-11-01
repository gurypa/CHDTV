package androidx.media3.exoplayer;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Metadata;
import androidx.media3.common.ParserException;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.HandlerWrapper;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.TraceUtil;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSourceException;
import androidx.media3.exoplayer.analytics.AnalyticsCollector;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.drm.DrmSession;
import androidx.media3.exoplayer.metadata.MetadataRenderer;
import androidx.media3.exoplayer.source.BehindLiveWindowException;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.SampleStream;
import androidx.media3.exoplayer.source.SequenceableLoader;
import androidx.media3.exoplayer.source.ShuffleOrder;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.text.TextRenderer;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.trackselection.TrackSelector;
import androidx.media3.exoplayer.trackselection.TrackSelectorResult;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
final class ExoPlayerImplInternal implements Handler.Callback, MediaPeriod.Callback, TrackSelector.InvalidationListener, MediaSourceList.MediaSourceListInfoRefreshListener, DefaultMediaClock.PlaybackParametersListener, PlayerMessage.Sender {
    private static final long BUFFERING_MAXIMUM_INTERVAL_MS = 0;
    private static final int MSG_ADD_MEDIA_SOURCES = 18;
    private static final int MSG_ATTEMPT_RENDERER_ERROR_RECOVERY = 25;
    private static final int MSG_DO_SOME_WORK = 2;
    private static final int MSG_MOVE_MEDIA_SOURCES = 19;
    private static final int MSG_PERIOD_PREPARED = 8;
    private static final int MSG_PLAYBACK_PARAMETERS_CHANGED_INTERNAL = 16;
    private static final int MSG_PLAYLIST_UPDATE_REQUESTED = 22;
    private static final int MSG_PREPARE = 29;
    private static final int MSG_RELEASE = 7;
    private static final int MSG_REMOVE_MEDIA_SOURCES = 20;
    private static final int MSG_RENDERER_CAPABILITIES_CHANGED = 26;
    private static final int MSG_SEEK_TO = 3;
    private static final int MSG_SEND_MESSAGE = 14;
    private static final int MSG_SEND_MESSAGE_TO_TARGET_THREAD = 15;
    private static final int MSG_SET_FOREGROUND_MODE = 13;
    private static final int MSG_SET_MEDIA_SOURCES = 17;
    private static final int MSG_SET_PAUSE_AT_END_OF_WINDOW = 23;
    private static final int MSG_SET_PLAYBACK_PARAMETERS = 4;
    private static final int MSG_SET_PLAY_WHEN_READY = 1;
    private static final int MSG_SET_PRELOAD_CONFIGURATION = 28;
    private static final int MSG_SET_REPEAT_MODE = 11;
    private static final int MSG_SET_SEEK_PARAMETERS = 5;
    private static final int MSG_SET_SHUFFLE_ENABLED = 12;
    private static final int MSG_SET_SHUFFLE_ORDER = 21;
    private static final int MSG_SOURCE_CONTINUE_LOADING_REQUESTED = 9;
    private static final int MSG_STOP = 6;
    private static final int MSG_TRACK_SELECTION_INVALIDATED = 10;
    private static final int MSG_UPDATE_MEDIA_SOURCES_WITH_MEDIA_ITEMS = 27;
    private static final long PLAYBACK_BUFFER_EMPTY_THRESHOLD_US = 500000;
    private static final long PLAYBACK_STUCK_AFTER_MS = 4000;
    private static final long READY_MAXIMUM_INTERVAL_MS = 1000;
    private static final String TAG = "ExoPlayerImplInternal";
    private final AnalyticsCollector analyticsCollector;
    private final HandlerWrapper applicationLooperHandler;
    private final long backBufferDurationUs;
    private final BandwidthMeter bandwidthMeter;
    private final Clock clock;
    private boolean deliverPendingMessageAtStartPositionRequired;
    private final boolean dynamicSchedulingEnabled;
    private final TrackSelectorResult emptyTrackSelectorResult;
    private int enabledRendererCount;
    private boolean foregroundMode;
    private final HandlerWrapper handler;
    private boolean isRebuffering;
    private Timeline lastPreloadPoolInvalidationTimeline;
    private long lastRebufferRealtimeMs;
    private final LivePlaybackSpeedControl livePlaybackSpeedControl;
    private final LoadControl loadControl;
    private final DefaultMediaClock mediaClock;
    private final MediaSourceList mediaSourceList;
    private int nextPendingMessageIndexHint;
    private boolean offloadSchedulingEnabled;
    private boolean pauseAtEndOfWindow;
    private SeekPosition pendingInitialSeekPosition;
    private final ArrayList<PendingMessageInfo> pendingMessages;
    private boolean pendingPauseAtEndOfPeriod;
    private ExoPlaybackException pendingRecoverableRendererError;
    private final Timeline.Period period;
    private PlaybackInfo playbackInfo;
    private PlaybackInfoUpdate playbackInfoUpdate;
    private final PlaybackInfoUpdateListener playbackInfoUpdateListener;
    private final Looper playbackLooper;
    private final PlaybackLooperProvider playbackLooperProvider;
    private long playbackMaybeBecameStuckAtMs;
    private final PlayerId playerId;
    private ExoPlayer.PreloadConfiguration preloadConfiguration;
    private final MediaPeriodQueue queue;
    private final long releaseTimeoutMs;
    private boolean released;
    private final RendererCapabilities[] rendererCapabilities;
    private long rendererPositionElapsedRealtimeUs;
    private long rendererPositionUs;
    private final boolean[] rendererReportedReady;
    private final Renderer[] renderers;
    private final Set<Renderer> renderersToReset;
    private int repeatMode;
    private boolean requestForRendererSleep;
    private final boolean retainBackBufferFromKeyframe;
    private SeekParameters seekParameters;
    private long setForegroundModeTimeoutMs;
    private boolean shouldContinueLoading;
    private boolean shuffleModeEnabled;
    private final TrackSelector trackSelector;
    private final Timeline.Window window;

    private static final class MediaSourceListUpdateMessage {
        private final List<MediaSourceList.MediaSourceHolder> mediaSourceHolders;
        private final long positionUs;
        private final ShuffleOrder shuffleOrder;
        private final int windowIndex;

        private MediaSourceListUpdateMessage(List<MediaSourceList.MediaSourceHolder> r1, ShuffleOrder r2, int r3, long r4) {
            this.mediaSourceHolders = r1;
            this.shuffleOrder = r2;
            this.windowIndex = r3;
            this.positionUs = r4;
        }

        /* synthetic */ MediaSourceListUpdateMessage(List r1, ShuffleOrder r2, int r3, long r4, C02331 r6) {
            this(r1, r2, r3, r4);
        }

        static /* synthetic */ int access$200(MediaSourceListUpdateMessage r1) {
            return r1.windowIndex;
        }

        static /* synthetic */ List access$300(MediaSourceListUpdateMessage r1) {
            return r1.mediaSourceHolders;
        }

        static /* synthetic */ ShuffleOrder access$400(MediaSourceListUpdateMessage r1) {
            return r1.shuffleOrder;
        }

        static /* synthetic */ long access$500(MediaSourceListUpdateMessage r2) {
            return r2.positionUs;
        }
    }

    private static class MoveMediaItemsMessage {
        public final int fromIndex;
        public final int newFromIndex;
        public final ShuffleOrder shuffleOrder;
        public final int toIndex;

        public MoveMediaItemsMessage(int r1, int r2, int r3, ShuffleOrder r4) {
            this.fromIndex = r1;
            this.toIndex = r2;
            this.newFromIndex = r3;
            this.shuffleOrder = r4;
        }
    }

    private static final class PendingMessageInfo implements Comparable<PendingMessageInfo> {
        public final PlayerMessage message;
        public int resolvedPeriodIndex;
        public long resolvedPeriodTimeUs;
        public Object resolvedPeriodUid;

        public PendingMessageInfo(PlayerMessage r1) {
            this.message = r1;
        }

        /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
        public int compareTo2(PendingMessageInfo r6) {
            if (this.resolvedPeriodUid != null) goto L5;
            boolean r0 = true;
        L7:
            if (r6.resolvedPeriodUid != null) goto L9;
            boolean r3 = true;
        L10:
            if (r0 == r3) goto L16;
            if (this.resolvedPeriodUid == null) goto L23;
            return -1;
        L23:
            return 1;
        L16:
            if (this.resolvedPeriodUid != null) goto L18;
            return 0;
        L18:
            int r1 = this.resolvedPeriodIndex - r6.resolvedPeriodIndex;
            if (r1 == 0) goto L22;
            return r1;
        L22:
            return Util.compareLong(this.resolvedPeriodTimeUs, r6.resolvedPeriodTimeUs);
        L9:
            r3 = false;
            goto L10
        L5:
            r0 = false;
            goto L7
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(PendingMessageInfo r1) {
            return compareTo2(r1);
        }

        public void setResolvedPosition(int r1, long r2, Object r4) {
            this.resolvedPeriodIndex = r1;
            this.resolvedPeriodTimeUs = r2;
            this.resolvedPeriodUid = r4;
        }
    }

    public static final class PlaybackInfoUpdate {
        public int discontinuityReason;
        private boolean hasPendingChange;
        public int operationAcks;
        public PlaybackInfo playbackInfo;
        public boolean positionDiscontinuity;

        public PlaybackInfoUpdate(PlaybackInfo r1) {
            this.playbackInfo = r1;
        }

        static /* synthetic */ boolean access$100(PlaybackInfoUpdate r1) {
            return r1.hasPendingChange;
        }

        public void incrementPendingOperationAcks(int r3) {
            boolean r0 = this.hasPendingChange;
            if (r3 <= 0) goto L5;
            boolean r1 = true;
        L6:
            this.hasPendingChange = r0 | r1;
            this.operationAcks += r3;
            return;
        L5:
            r1 = false;
            goto L6
        }

        public void setPlaybackInfo(PlaybackInfo r3) {
            boolean r0 = this.hasPendingChange;
            if (this.playbackInfo == r3) goto L5;
            boolean r1 = true;
        L6:
            this.hasPendingChange = r0 | r1;
            this.playbackInfo = r3;
            return;
        L5:
            r1 = false;
            goto L6
        }

        public void setPositionDiscontinuity(int r4) {
            boolean r1 = true;
            if (this.positionDiscontinuity == true) goto L5;
        L11:
            this.hasPendingChange = true;
            this.positionDiscontinuity = true;
            this.discontinuityReason = r4;
            return;
        L5:
            if (this.discontinuityReason == 5) goto L11;
            if (r4 == 5) goto L9;
            r1 = false;
        L9:
            Assertions.checkArgument(r1);
        }
    }

    public interface PlaybackInfoUpdateListener {
        void onPlaybackInfoUpdate(PlaybackInfoUpdate r1);
    }

    private static final class PositionUpdateForPlaylistChange {
        public final boolean endPlayback;
        public final boolean forceBufferingState;
        public final MediaSource.MediaPeriodId periodId;
        public final long periodPositionUs;
        public final long requestedContentPositionUs;
        public final boolean setTargetLiveOffset;

        public PositionUpdateForPlaylistChange(MediaSource.MediaPeriodId r1, long r2, long r4, boolean r6, boolean r7, boolean r8) {
            this.periodId = r1;
            this.periodPositionUs = r2;
            this.requestedContentPositionUs = r4;
            this.forceBufferingState = r6;
            this.endPlayback = r7;
            this.setTargetLiveOffset = r8;
        }
    }

    private static final class SeekPosition {
        public final Timeline timeline;
        public final int windowIndex;
        public final long windowPositionUs;

        public SeekPosition(Timeline r1, int r2, long r3) {
            this.timeline = r1;
            this.windowIndex = r2;
            this.windowPositionUs = r3;
        }
    }

    public static /* synthetic */ MediaPeriodHolder $r8$lambda$4ByVomVrDZGMrfXELlhTrFd0naQ(ExoPlayerImplInternal r0, MediaPeriodInfo r1, long r2) {
        return r0.createMediaPeriodHolder(r1, r2);
    }

    static {
        BUFFERING_MAXIMUM_INTERVAL_MS = Util.usToMs(Renderer.DEFAULT_DURATION_TO_PROGRESS_US);
    }

    public ExoPlayerImplInternal(Renderer[] r18, TrackSelector r19, TrackSelectorResult r20, LoadControl r21, BandwidthMeter r22, int r23, boolean r24, AnalyticsCollector r25, SeekParameters r26, LivePlaybackSpeedControl r27, long r28, boolean r30, boolean r31, Looper r32, Clock r33, PlaybackInfoUpdateListener r34, PlayerId r35, PlaybackLooperProvider r36, ExoPlayer.PreloadConfiguration r37) {
        this.playbackInfoUpdateListener = r34;
        this.renderers = r18;
        this.trackSelector = r19;
        this.emptyTrackSelectorResult = r20;
        this.loadControl = r21;
        this.bandwidthMeter = r22;
        this.repeatMode = r23;
        this.shuffleModeEnabled = r24;
        this.seekParameters = r26;
        this.livePlaybackSpeedControl = r27;
        this.releaseTimeoutMs = r28;
        this.setForegroundModeTimeoutMs = r28;
        this.pauseAtEndOfWindow = r30;
        this.dynamicSchedulingEnabled = r31;
        this.clock = r33;
        this.playerId = r35;
        this.preloadConfiguration = r37;
        this.analyticsCollector = r25;
        this.playbackMaybeBecameStuckAtMs = C0180C.TIME_UNSET;
        this.lastRebufferRealtimeMs = C0180C.TIME_UNSET;
        this.backBufferDurationUs = r21.getBackBufferDurationUs(r35);
        this.retainBackBufferFromKeyframe = r21.retainBackBufferFromKeyframe(r35);
        this.lastPreloadPoolInvalidationTimeline = Timeline.EMPTY;
        this.playbackInfo = PlaybackInfo.createDummy(r20);
        this.playbackInfoUpdate = new PlaybackInfoUpdate(this.playbackInfo);
        this.rendererCapabilities = new RendererCapabilities[r18.length];
        this.rendererReportedReady = new boolean[r18.length];
        RendererCapabilities.Listener r6 = r19.getRendererCapabilitiesListener();
        int r7 = 0;
    L4:
        if (r7 >= r18.length) goto L9;
        r18[r7].init(r7, r35, r33);
        this.rendererCapabilities[r7] = r18[r7].getCapabilities();
        if (r6 == null) goto L8;
        this.rendererCapabilities[r7].setListener(r6);
    L8:
        r7 = r7 + 1;
        goto L4
    L9:
        this.mediaClock = new DefaultMediaClock(this, r33);
        this.pendingMessages = new ArrayList();
        this.renderersToReset = Sets.newIdentityHashSet();
        this.window = new Timeline.Window();
        this.period = new Timeline.Period();
        r19.init(this, r22);
        this.deliverPendingMessageAtStartPositionRequired = true;
        this.applicationLooperHandler = r33.createHandler(r32, null);
        this.queue = new MediaPeriodQueue(r25, this.applicationLooperHandler, new ExoPlayerImplInternal$$ExternalSyntheticLambda2(this), r37);
        this.mediaSourceList = new MediaSourceList(this, r25, this.applicationLooperHandler, r35);
        if (r36 != null) goto L12;
        PlaybackLooperProvider r1 = new PlaybackLooperProvider();
    L13:
        this.playbackLooperProvider = r1;
        this.playbackLooper = this.playbackLooperProvider.obtainLooper();
        this.handler = r33.createHandler(this.playbackLooper, this);
        return;
    L12:
        r1 = r36;
        goto L13
    }

    static /* synthetic */ boolean access$602(ExoPlayerImplInternal r0, boolean r1) {
        r0.requestForRendererSleep = r1;
        return r1;
    }

    static /* synthetic */ boolean access$700(ExoPlayerImplInternal r1) {
        return r1.dynamicSchedulingEnabled;
    }

    static /* synthetic */ boolean access$800(ExoPlayerImplInternal r1) {
        return r1.offloadSchedulingEnabled;
    }

    static /* synthetic */ HandlerWrapper access$900(ExoPlayerImplInternal r1) {
        return r1.handler;
    }

    private void addMediaItemsInternal(MediaSourceListUpdateMessage r5, int r6) throws ExoPlaybackException {
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        MediaSourceList r0 = this.mediaSourceList;
        if (r6 != (-1)) goto L5;
        int r1 = this.mediaSourceList.getSize();
    L6:
        handleMediaSourceListInfoRefreshed(r0.addMediaSources(r1, MediaSourceListUpdateMessage.access$300(r5), MediaSourceListUpdateMessage.access$400(r5)), false);
        return;
    L5:
        r1 = r6;
        goto L6
    }

    private void allowRenderersToRenderStartOfStreams() {
        TrackSelectorResult r0 = this.queue.getPlayingPeriod().getTrackSelectorResult();
        int r1 = 0;
    L4:
        if (r1 >= this.renderers.length) goto L9;
        if (r0.isRendererEnabled(r1) == false) goto L8;
        this.renderers[r1].enableMayRenderStartOfStream();
    L8:
        r1 = r1 + 1;
        goto L4
    }

    private void attemptRendererErrorRecovery() throws ExoPlaybackException {
        reselectTracksInternalAndSeek();
    }

    private MediaPeriodHolder createMediaPeriodHolder(MediaPeriodInfo r12, long r13) {
        return new MediaPeriodHolder(this.rendererCapabilities, r13, this.trackSelector, this.loadControl.getAllocator(), this.mediaSourceList, r12, this.emptyTrackSelectorResult, this.preloadConfiguration.targetPreloadDurationUs);
    }

    private void deliverMessage(PlayerMessage r5) throws ExoPlaybackException {
        if (r5.isCanceled() == false) goto L12;
        return;
    L12:
        r5.getTarget().handleMessage(r5.getType(), r5.getPayload());     // Catch: Throwable -> L9
        r5.markAsProcessed(true);
        return;
    L9:
        th = move-exception;
        r5.markAsProcessed(true);
        throw th;
    }

    private void disableRenderer(int r3) throws ExoPlaybackException {
        Renderer r0 = this.renderers[r3];
        if (isRendererEnabled(r0) == true) goto L5;
        return;
    L5:
        maybeTriggerOnRendererReadyChanged(r3, false);
        this.mediaClock.onRendererDisabled(r0);
        ensureStopped(r0);
        r0.disable();
        this.enabledRendererCount--;
    }

    private void doSomeWork() throws ExoPlaybackException, IOException {
        long r1 = this.clock.uptimeMillis();
        this.handler.removeMessages(2);
        updatePeriods();
        if (this.playbackInfo.playbackState != 1) goto L5;
        return;
    L5:
        if (this.playbackInfo.playbackState == 4) goto L159;
        MediaPeriodHolder r3 = this.queue.getPlayingPeriod();
        if (r3 != null) goto L11;
        scheduleNextWork(r1);
        return;
    L11:
        TraceUtil.beginSection("doSomeWork");
        updatePlaybackPositions();
        boolean r7 = true;
        boolean r8 = true;
        if (r3.prepared == false) goto L53;
        this.rendererPositionElapsedRealtimeUs = Util.msToUs(this.clock.elapsedRealtime());
        r3.mediaPeriod.discardBuffer(this.playbackInfo.positionUs - this.backBufferDurationUs, this.retainBackBufferFromKeyframe);
        int r9 = 0;
    L15:
        if (r9 >= this.renderers.length) goto L54;
        Renderer r11 = this.renderers[r9];
        if (isRendererEnabled(r11) == true) goto L19;
        maybeTriggerOnRendererReadyChanged(r9, false);
    L51:
        r9 = r9 + 1;
        goto L15
    L19:
        r11.render(this.rendererPositionUs, this.rendererPositionElapsedRealtimeUs);
        if (r7 == true) goto L22;
    L24:
        boolean r12 = false;
    L25:
        r7 = r12;
        if (r3.sampleStreams[r9] == r11.getStream()) goto L28;
        boolean r13 = true;
    L29:
        if (r13 == false) goto L31;
    L33:
        boolean r14 = false;
    L34:
        if (r13 == true) goto L42;
        if (r14 == true) goto L42;
        if (r11.isReady() == true) goto L42;
        if (r11.isEnded() == true) goto L42;
        boolean r15 = false;
    L43:
        maybeTriggerOnRendererReadyChanged(r9, r15);
        if (r8 == false) goto L47;
        if (r15 == false) goto L47;
        boolean r16 = true;
    L48:
        if (r15 == true) goto L50;
        maybeThrowRendererStreamError(r9);
    L50:
        r8 = r16;
    L47:
        r16 = false;
    L42:
        r15 = true;
        goto L43
    L31:
        if (r11.hasReadStreamToEnd() == false) goto L33;
        r14 = true;
        goto L34
    L28:
        r13 = false;
        goto L29
    L22:
        if (r11.isEnded() == false) goto L24;
        r12 = true;
    L54:
        long r17 = r3.info.durationUs;
        if (r7 == true) goto L57;
    L63:
        boolean r10 = false;
    L64:
        if (r10 == false) goto L69;
        if (this.pendingPauseAtEndOfPeriod == false) goto L69;
        this.pendingPauseAtEndOfPeriod = false;
        setPlayWhenReadyInternal(false, this.playbackInfo.playbackSuppressionReason, false, 5);
    L69:
        if (r10 == false) goto L74;
        if (r3.info.isFinal == false) goto L74;
        setState(4);
        stopRenderers();
    L93:
        boolean r18 = false;
        if (this.playbackInfo.playbackState != 2) goto L111;
        int r5 = 0;
    L97:
        if (r5 >= this.renderers.length) goto L105;
        if (isRendererEnabled(this.renderers[r5]) == false) goto L103;
        if (this.renderers[r5].getStream() != r3.sampleStreams[r5]) goto L103;
        maybeThrowRendererStreamError(r5);
    L103:
        r5 = r5 + 1;
        goto L97
    L105:
        if (this.playbackInfo.isLoading == true) goto L111;
        if (this.playbackInfo.totalBufferedDurationUs >= PLAYBACK_BUFFER_EMPTY_THRESHOLD_US) goto L111;
        if (isLoadingPossible(this.queue.getLoadingPeriod()) == false) goto L111;
        r18 = true;
    L111:
        if (r18 == true) goto L114;
        this.playbackMaybeBecameStuckAtMs = C0180C.TIME_UNSET;
    L119:
        if (shouldPlayWhenReady() == true) goto L121;
    L123:
        boolean r4 = false;
    L125:
        if (this.offloadSchedulingEnabled == true) goto L127;
    L130:
        boolean r6 = false;
    L132:
        if (this.playbackInfo.sleepingForOffload == r6) goto L134;
        this.playbackInfo = this.playbackInfo.copyWithSleepingForOffload(r6);
    L134:
        this.requestForRendererSleep = false;
        if (r6 == false) goto L137;
    L147:
        TraceUtil.endSection();
        return;
    L137:
        if (this.playbackInfo.playbackState == 4) goto L147;
        if (r4 == false) goto L141;
    L146:
        scheduleNextWork(r1);
        goto L147
    L141:
        if (this.playbackInfo.playbackState == 2) goto L146;
        if (this.playbackInfo.playbackState != 3) goto L147;
        if (this.enabledRendererCount == 0) goto L147;
    L127:
        if (this.requestForRendererSleep == false) goto L130;
        if (r4 == false) goto L130;
        r6 = true;
        goto L132
    L121:
        if (this.playbackInfo.playbackState != 3) goto L123;
        r4 = true;
        goto L125
    L114:
        if (this.playbackMaybeBecameStuckAtMs != C0180C.TIME_UNSET) goto L117;
        this.playbackMaybeBecameStuckAtMs = this.clock.elapsedRealtime();
        goto L119
    L117:
        if ((this.clock.elapsedRealtime() - this.playbackMaybeBecameStuckAtMs) < PLAYBACK_STUCK_AFTER_MS) goto L119;
        throw new IllegalStateException("Playback stuck buffering and not loading");
    L74:
        if (this.playbackInfo.playbackState != 2) goto L81;
        if (shouldTransitionToReadyState(r8) == false) goto L81;
        setState(3);
        this.pendingRecoverableRendererError = null;
        if (shouldPlayWhenReady() == false) goto L93;
        updateRebufferingState(false, false);
        this.mediaClock.start();
        startRenderers();
    L81:
        if (this.playbackInfo.playbackState != 3) goto L93;
        if (this.enabledRendererCount == 0) goto L85;
        if (r8 == true) goto L93;
    L89:
        updateRebufferingState(shouldPlayWhenReady(), false);
        setState(2);
        if (this.isRebuffering == false) goto L92;
        notifyTrackSelectionRebuffer();
        this.livePlaybackSpeedControl.notifyRebuffer();
    L92:
        stopRenderers();
        goto L93
    L85:
        if (isTimelineReady() == false) goto L89;
    L57:
        if (r3.prepared == false) goto L63;
        if (r17 != C0180C.TIME_UNSET) goto L61;
    L62:
        r10 = true;
        goto L64
    L61:
        if (r17 > this.playbackInfo.positionUs) goto L63;
    L53:
        r3.mediaPeriod.maybeThrowPrepareError();
        goto L54
    }

    private void enableRenderer(int r19, boolean r20, long r21) throws ExoPlaybackException {
        Renderer r2 = this.renderers[r19];
        if (isRendererEnabled(r2) == false) goto L5;
        return;
    L5:
        MediaPeriodHolder r1 = this.queue.getReadingPeriod();
        if (r1 != this.queue.getPlayingPeriod()) goto L8;
        boolean r9 = true;
    L9:
        TrackSelectorResult r15 = r1.getTrackSelectorResult();
        RendererConfiguration r3 = r15.rendererConfigurations[r19];
        Format[] r4 = getFormats(r15.selections[r19]);
        if (shouldPlayWhenReady() == true) goto L12;
    L14:
        boolean r7 = false;
    L15:
        boolean r17 = r7;
        if (r20 == true) goto L19;
        if (r17 == false) goto L19;
        boolean r8 = true;
    L20:
        this.enabledRendererCount++;
        this.renderersToReset.add(r2);
        r2.enable(r3, r4, r1.sampleStreams[r19], this.rendererPositionUs, r8, r9, r21, r1.getRendererOffset(), r1.info.f35id);
        r2.handleMessage(11, new C02331(this));
        this.mediaClock.onRendererEnabled(r2);
        if (r17 == false) goto L25;
        if (r9 == false) goto L26;
        r2.start();
        return;
    L26:
        return;
    L25:
        return;
    L19:
        r8 = false;
        goto L20
    L12:
        if (this.playbackInfo.playbackState != 3) goto L14;
        r7 = true;
        goto L15
    L8:
        r9 = false;
        goto L9
    }

    private void enableRenderers() throws ExoPlaybackException {
        enableRenderers(new boolean[this.renderers.length], this.queue.getReadingPeriod().getStartPositionRendererTime());
    }

    private void enableRenderers(boolean[] r6, long r7) throws ExoPlaybackException {
        MediaPeriodHolder r0 = this.queue.getReadingPeriod();
        TrackSelectorResult r1 = r0.getTrackSelectorResult();
        int r2 = 0;
    L4:
        if (r2 >= this.renderers.length) goto L11;
        if (r1.isRendererEnabled(r2) == true) goto L10;
        if (this.renderersToReset.remove(this.renderers[r2]) == false) goto L10;
        this.renderers[r2].reset();
    L10:
        r2 = r2 + 1;
        goto L4
    L11:
        int r3 = 0;
    L13:
        if (r3 >= this.renderers.length) goto L18;
        if (r1.isRendererEnabled(r3) == false) goto L17;
        enableRenderer(r3, r6[r3], r7);
    L17:
        r3 = r3 + 1;
        goto L13
    L18:
        r0.allRenderersInCorrectState = true;
    }

    private void ensureStopped(Renderer r3) {
        if (r3.getState() != 2) goto L6;
        r3.stop();
        return;
    }

    private ImmutableList<Metadata> extractMetadataFromTrackSelectionArray(ExoTrackSelection[] r10) {
        ImmutableList.Builder r0 = new ImmutableList.Builder();
        boolean r1 = false;
        int r2 = r10.length;
        int r4 = 0;
    L3:
        if (r4 >= r2) goto L11;
        ExoTrackSelection r5 = r10[r4];
        if (r5 == null) goto L10;
        Format r6 = r5.getFormat(0);
        if (r6.metadata != null) goto L9;
        r0.add(new Metadata(new Metadata.Entry[0]));
        goto L10
    L9:
        r0.add(r6.metadata);
        r1 = true;
    L10:
        r4 = r4 + 1;
        goto L3
    L11:
        if (r1 == false) goto L14;
        return r0.build();
    L14:
        return ImmutableList.m212of();
    }

    private long getCurrentLiveOffsetUs() {
        return getLiveOffsetUs(this.playbackInfo.timeline, this.playbackInfo.periodId.periodUid, this.playbackInfo.positionUs);
    }

    private static Format[] getFormats(ExoTrackSelection r4) {
        if (r4 == null) goto L4;
        int r0 = r4.length();
    L5:
        Format[] r1 = new Format[r0];
        int r2 = 0;
    L6:
        if (r2 >= r0) goto L8;
        r1[r2] = r4.getFormat(r2);
        r2 = r2 + 1;
        goto L6
    L8:
        return r1;
    L4:
        r0 = 0;
        goto L5
    }

    private long getLiveOffsetUs(Timeline r6, Object r7, long r8) {
        r6.getWindow(r6.getPeriodByUid(r7, this.period).windowIndex, this.window);
        if (this.window.windowStartTimeMs != C0180C.TIME_UNSET) goto L5;
    L11:
        return C0180C.TIME_UNSET;
    L5:
        if (this.window.isLive() == false) goto L11;
        if (this.window.isDynamic == false) goto L11;
        return Util.msToUs(this.window.getCurrentUnixTimeMs() - this.window.windowStartTimeMs) - (this.period.getPositionInWindowUs() + r8);
    }

    private long getMaxRendererReadPositionUs() {
        MediaPeriodHolder r0 = this.queue.getReadingPeriod();
        if (r0 != null) goto L6;
        return 0;
    L6:
        long r1 = r0.getRendererOffset();
        if (r0.prepared == true) goto L9;
        return r1;
    L9:
        int r3 = 0;
    L11:
        if (r3 >= this.renderers.length) goto L22;
        if (isRendererEnabled(this.renderers[r3]) == false) goto L21;
        if (this.renderers[r3].getStream() != r0.sampleStreams[r3]) goto L21;
        long r4 = this.renderers[r3].getReadingPositionUs();
        if (r4 == Long.MIN_VALUE) goto L19;
        r1 = Math.max(r4, r1);
        goto L21
    L19:
        return Long.MIN_VALUE;
    L21:
        r3 = r3 + 1;
        goto L11
    L22:
        return r1;
    }

    private Pair<MediaSource.MediaPeriodId, Long> getPlaceholderFirstMediaPeriodPositionUs(Timeline r11) {
        long r1 = 0;
        if (r11.isEmpty() == true) goto L5;
        int r6 = r11.getFirstWindowIndex(this.shuffleModeEnabled);
        Pair<Object, Long> r12 = r11.getPeriodPositionUs(this.window, this.period, r6, C0180C.TIME_UNSET);
        MediaSource.MediaPeriodId r0 = this.queue.resolveMediaPeriodIdForAdsAfterPeriodPositionChange(r11, r12.first, 0);
        long r4 = ((Long) r12.second).longValue();
        if (r0.isAd() == false) goto L14;
        r11.getPeriodByUid(r0.periodUid, this.period);
        if (r0.adIndexInAdGroup != this.period.getFirstAdIndexToPlay(r0.adGroupIndex)) goto L12;
        r1 = this.period.getAdResumePositionUs();
    L12:
        r4 = r1;
    L14:
        return Pair.create(r0, Long.valueOf(r4));
    L5:
        return Pair.create(PlaybackInfo.getDummyPeriodForEmptyTimeline(), 0L);
    }

    private long getTotalBufferedDurationUs() {
        return getTotalBufferedDurationUs(this.playbackInfo.bufferedPositionUs);
    }

    private long getTotalBufferedDurationUs(long r6) {
        MediaPeriodHolder r0 = this.queue.getLoadingPeriod();
        if (r0 != null) goto L6;
        return 0;
    L6:
        return Math.max(0, r6 - r0.toPeriodTime(this.rendererPositionUs));
    }

    private void handleContinueLoadingRequested(MediaPeriod r4) {
        if (this.queue.isLoading(r4) == false) goto L6;
        this.queue.reevaluateBuffer(this.rendererPositionUs);
        maybeContinueLoading();
        return;
    L6:
        if (this.queue.isPreloading(r4) == false) goto L10;
        maybeContinuePreloading();
        return;
    }

    private void handleIoException(IOException r5, int r6) {
        ExoPlaybackException r0 = ExoPlaybackException.createForSource(r5, r6);
        MediaPeriodHolder r1 = this.queue.getPlayingPeriod();
        if (r1 == null) goto L5;
        r0 = r0.copyWithMediaPeriodId(r1.info.f35id);
    L5:
        Log.m45e(TAG, "Playback error", r0);
        stopInternal(false, false);
        this.playbackInfo = this.playbackInfo.copyWithPlaybackError(r0);
    }

    private void handleLoadingMediaPeriodChanged(boolean r7) {
        MediaPeriodHolder r0 = this.queue.getLoadingPeriod();
        if (r0 != null) goto L5;
        MediaSource.MediaPeriodId r1 = this.playbackInfo.periodId;
    L6:
        boolean r2 = !this.playbackInfo.loadingMediaPeriodId.equals(r1);
        if (r2 == false) goto L9;
        this.playbackInfo = this.playbackInfo.copyWithLoadingMediaPeriodId(r1);
    L9:
        PlaybackInfo r3 = this.playbackInfo;
        if (r0 != null) goto L12;
        long r4 = this.playbackInfo.positionUs;
    L13:
        r3.bufferedPositionUs = r4;
        this.playbackInfo.totalBufferedDurationUs = getTotalBufferedDurationUs();
        if (r2 == true) goto L16;
        if (r7 == true) goto L16;
        return;
    L16:
        if (r0 != null) goto L18;
        return;
    L18:
        if (r0.prepared == false) goto L23;
        updateLoadControlTrackSelection(r0.info.f35id, r0.getTrackGroups(), r0.getTrackSelectorResult());
        return;
    L23:
        return;
    L12:
        r4 = r0.getBufferedPositionUs();
        goto L13
    L5:
        r1 = r0.info.f35id;
        goto L6
    }

    private void handleLoadingPeriodPrepared(MediaPeriodHolder r12) throws ExoPlaybackException {
        if (r12.prepared == true) goto L5;
        r12.handlePrepared(this.mediaClock.getPlaybackParameters().speed, this.playbackInfo.timeline, this.playbackInfo.playWhenReady);
    L5:
        updateLoadControlTrackSelection(r12.info.f35id, r12.getTrackGroups(), r12.getTrackSelectorResult());
        if (r12 != this.queue.getPlayingPeriod()) goto L9;
        resetRendererPosition(r12.info.startPositionUs);
        enableRenderers();
        this.playbackInfo = handlePositionDiscontinuity(this.playbackInfo.periodId, r12.info.startPositionUs, this.playbackInfo.requestedContentPositionUs, r12.info.startPositionUs, false, 5);
    L9:
        maybeContinueLoading();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v0 */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r17v0 */
    /* JADX WARN: Type inference failed for: r17v1 */
    /* JADX WARN: Type inference failed for: r17v2 */
    /* JADX WARN: Type inference failed for: r17v3 */
    /* JADX WARN: Type inference failed for: r17v4 */
    /* JADX WARN: Type inference failed for: r17v5 */
    /* JADX WARN: Type inference failed for: r21v1 */
    /* JADX WARN: Type inference failed for: r21v10 */
    /* JADX WARN: Type inference failed for: r21v11 */
    /* JADX WARN: Type inference failed for: r21v12 */
    /* JADX WARN: Type inference failed for: r21v14 */
    /* JADX WARN: Type inference failed for: r21v15 */
    /* JADX WARN: Type inference failed for: r21v16 */
    /* JADX WARN: Type inference failed for: r21v17 */
    /* JADX WARN: Type inference failed for: r21v18 */
    /* JADX WARN: Type inference failed for: r21v19 */
    /* JADX WARN: Type inference failed for: r21v20 */
    /* JADX WARN: Type inference failed for: r21v3 */
    /* JADX WARN: Type inference failed for: r21v4 */
    /* JADX WARN: Type inference failed for: r21v5 */
    /* JADX WARN: Type inference failed for: r21v7 */
    /* JADX WARN: Type inference failed for: r21v8, types: [androidx.media3.exoplayer.Renderer] */
    /* JADX WARN: Type inference failed for: r21v9 */
    private void handleMediaSourceListInfoRefreshed(Timeline r26, boolean r27) throws ExoPlaybackException {
        Timeline r2 = r26;
        PositionUpdateForPlaylistChange r11 = resolvePositionForPlaylistChange(r2, this.playbackInfo, this.pendingInitialSeekPosition, this.queue, this.repeatMode, this.shuffleModeEnabled, this.window, this.period);
        MediaSource.MediaPeriodId r8 = r11.periodId;
        long r9 = r11.requestedContentPositionUs;
        boolean r12 = r11.forceBufferingState;
        long r13 = r11.periodPositionUs;
        boolean r15 = true;
        if (this.playbackInfo.periodId.equals(r8) == true) goto L5;
    L8:
        boolean r0 = true;
    L9:
        boolean r16 = r0;
        ?? r17 = 3;
        ?? r18 = 3;
    L91:
        th = th;
        r15 = false;
        char r21 = 4;
    L93:
        ?? r22 = r21;
    L94:
        Timeline r4 = this.playbackInfo.timeline;
        MediaSource.MediaPeriodId r5 = this.playbackInfo.periodId;
        if (r11.setTargetLiveOffset == false) goto L97;
        long r6 = r13;
    L98:
        updatePlaybackSpeedSettingsForNewPeriod(r26, r8, r4, r5, r6, false);
        if (r16 == false) goto L101;
    L104:
        Object r7 = this.playbackInfo.periodId.periodUid;
        Timeline r10 = this.playbackInfo.timeline;
        if (r16 == false) goto L112;
        if (r27 == false) goto L112;
        if (r10.isEmpty() == true) goto L112;
        if (r10.getPeriodByUid(r7, this.period).isPlaceholder == true) goto L112;
        boolean r23 = true;
    L113:
        long r14 = this.playbackInfo.discontinuityStartPositionUs;
        if (r26.getIndexOfPeriod(r7) != (-1)) goto L117;
        r17 = r22;
    L117:
        this.playbackInfo = handlePositionDiscontinuity(r8, r13, r9, r14, r23, r17 == true ? 1 : 0);
    L118:
        resetPendingPauseAtEndOfPeriod();
        resolvePendingMessagePositions(r2, this.playbackInfo.timeline);
        this.playbackInfo = this.playbackInfo.copyWithTimeline(r2);
        if (r26.isEmpty() == true) goto L121;
        this.pendingInitialSeekPosition = null;
    L121:
        handleLoadingMediaPeriodChanged(false);
        this.handler.sendEmptyMessage(2);
        throw th;
    L112:
        r23 = r15;
        goto L113
    L101:
        if (r9 == this.playbackInfo.requestedContentPositionUs) goto L118;
    L97:
        r6 = -9223372036854775807L;
        goto L98
    L11:
        if (r11.endPlayback == true) goto L126;
    L20:
        Renderer[] r1 = this.renderers;     // Catch: Throwable -> L91
        int r3 = r1.length;     // Catch: Throwable -> L89
        int r19 = 0;
    L23:
        if (r19 < r3) goto L134;
        if (r16 == false) goto L130;
        r21 = 4;
        r15 = false;
        ?? r24 = r21;
        if (r2.isEmpty() == true) goto L58;
        MediaPeriodHolder r20 = this.queue.getPlayingPeriod();     // Catch: Throwable -> L56
    L49:
        if (r20 == null) goto L55;
        if (r20.info.f35id.equals(r8) == false) goto L53;
        r20.info = this.queue.getUpdatedMediaPeriodInfo(r2, r20.info);     // Catch: Throwable -> L56
        r20.updateClipping();     // Catch: Throwable -> L56
    L53:
        r20 = r20.getNext();     // Catch: Throwable -> L56
        goto L49
    L55:
        r13 = seekToPeriodPosition(r8, r13, r12);     // Catch: Throwable -> L56
        r24 = r21;
    L58:
        Timeline r25 = this.playbackInfo.timeline;
        MediaSource.MediaPeriodId r28 = this.playbackInfo.periodId;
        if (r11.setTargetLiveOffset == false) goto L61;
        long r29 = r13;
    L62:
        updatePlaybackSpeedSettingsForNewPeriod(r2, r8, r25, r28, r29, false);
        if (r16 == false) goto L65;
    L68:
        Object r30 = this.playbackInfo.periodId.periodUid;
        Timeline r31 = this.playbackInfo.timeline;
        if (r16 == false) goto L76;
        if (r27 == false) goto L76;
        if (r31.isEmpty() == true) goto L76;
        if (r31.getPeriodByUid(r30, this.period).isPlaceholder == true) goto L76;
        boolean r210 = true;
    L77:
        long r32 = this.playbackInfo.discontinuityStartPositionUs;
        if (r2.getIndexOfPeriod(r30) != (-1)) goto L81;
        r18 = r24;
    L81:
        this.playbackInfo = handlePositionDiscontinuity(r8, r13, r9, r32, r210, r18 == true ? 1 : 0);
    L82:
        resetPendingPauseAtEndOfPeriod();
        resolvePendingMessagePositions(r2, this.playbackInfo.timeline);
        this.playbackInfo = this.playbackInfo.copyWithTimeline(r2);
        if (r2.isEmpty() == true) goto L85;
        this.pendingInitialSeekPosition = null;
    L85:
        handleLoadingMediaPeriodChanged(r15);
        this.handler.sendEmptyMessage(2);
        return;
    L76:
        r210 = r15;
        goto L77
    L65:
        if (r9 == this.playbackInfo.requestedContentPositionUs) goto L82;
    L61:
        r29 = -9223372036854775807L;
    L87:
        th = th;
        goto L93
    L130:
        ?? r211 = 4;
        r15 = false;
        r2 = r26;
        r24 = r211;
        if (this.queue.updateQueuedPeriods(r26, this.rendererPositionUs, getMaxRendererReadPositionUs()) == true) goto L58;
        seekToCurrentPosition(false);     // Catch: Throwable -> L56
        r24 = r211;
    L38:
        th = th;
    L19:
        r22 = r211;
    L40:
        th = th;
        r211 = r211;
    L44:
        r15 = false;
    L42:
        th = th;
        r211 = 4;
    L56:
        th = th;
        goto L19
    L134:
        r211 = r1[r19];     // Catch: Throwable -> L26
        r211.setTimeline(r2);     // Catch: Throwable -> L26
        r19 = r19 + 1;
    L26:
        th = th;
        r22 = 4;
        r15 = false;
    L89:
        th = th;
        r22 = 4;
        r15 = false;
        goto L94
    L126:
    L17:
        th = th;
        r15 = false;
        r211 = 4;
        goto L19
    L13:
        if (this.playbackInfo.playbackState == 1) goto L15;
        setState(4);     // Catch: Throwable -> L17
    L15:
        resetInternal(false, false, false, true);     // Catch: Throwable -> L17
        goto L20
    L5:
        if (r13 != this.playbackInfo.positionUs) goto L8;
        r0 = false;
        goto L9
    }

    private void handlePeriodPrepared(MediaPeriod r5) throws ExoPlaybackException {
        if (this.queue.isLoading(r5) == false) goto L5;
        handleLoadingPeriodPrepared((MediaPeriodHolder) Assertions.checkNotNull(this.queue.getLoadingPeriod()));
        return;
    L5:
        MediaPeriodHolder r0 = this.queue.getPreloadHolderByMediaPeriod(r5);
        if (r0 == null) goto L12;
        Assertions.checkState(!r0.prepared);
        r0.handlePrepared(this.mediaClock.getPlaybackParameters().speed, this.playbackInfo.timeline, this.playbackInfo.playWhenReady);
        if (this.queue.isPreloading(r5) == false) goto L13;
        maybeContinuePreloading();
        return;
    L13:
        return;
    }

    private void handlePlaybackParameters(PlaybackParameters r6, float r7, boolean r8, boolean r9) throws ExoPlaybackException {
        if (r8 == false) goto L6;
        if (r9 == false) goto L5;
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
    L5:
        this.playbackInfo = this.playbackInfo.copyWithPlaybackParameters(r6);
    L6:
        updateTrackSelectionPlaybackSpeed(r6.speed);
        Renderer[] r0 = this.renderers;
        int r1 = r0.length;
        int r2 = 0;
    L7:
        if (r2 >= r1) goto L12;
        Renderer r3 = r0[r2];
        if (r3 == null) goto L11;
        r3.setPlaybackSpeed(r7, r6.speed);
    L11:
        r2 = r2 + 1;
        goto L7
    }

    private void handlePlaybackParameters(PlaybackParameters r3, boolean r4) throws ExoPlaybackException {
        handlePlaybackParameters(r3, r3.speed, true, r4);
    }

    private PlaybackInfo handlePositionDiscontinuity(MediaSource.MediaPeriodId r15, long r16, long r18, long r20, boolean r22, int r23) {
        if (this.deliverPendingMessageAtStartPositionRequired == false) goto L5;
    L10:
        boolean r0 = true;
    L11:
        this.deliverPendingMessageAtStartPositionRequired = r0;
        resetPendingPauseAtEndOfPeriod();
        TrackGroupArray r1 = this.playbackInfo.trackGroups;
        TrackSelectorResult r2 = this.playbackInfo.trackSelectorResult;
        List<Metadata> r3 = this.playbackInfo.staticMetadata;
        if (this.mediaSourceList.isPrepared() == false) goto L28;
        MediaPeriodHolder r4 = this.queue.getPlayingPeriod();
        if (r4 != null) goto L16;
        TrackGroupArray r6 = TrackGroupArray.EMPTY;
    L17:
        r1 = r6;
        if (r4 != null) goto L20;
        TrackSelectorResult r7 = this.emptyTrackSelectorResult;
    L21:
        r2 = r7;
        r3 = extractMetadataFromTrackSelectionArray(r2.selections);
        if (r4 != null) goto L24;
    L26:
        maybeUpdateOffloadScheduling();
    L30:
        TrackGroupArray r10 = r1;
        TrackSelectorResult r11 = r2;
        List<Metadata> r12 = r3;
    L31:
        if (r22 == false) goto L35;
        this.playbackInfoUpdate.setPositionDiscontinuity(r23);
    L35:
        return this.playbackInfo.copyWithNewPosition(r15, r16, r18, r20, getTotalBufferedDurationUs(), r10, r11, r12);
    L24:
        if (r4.info.requestedContentPositionUs == r18) goto L26;
        r4.info = r4.info.copyWithRequestedContentPositionUs(r18);
        goto L26
    L20:
        r7 = r4.getTrackSelectorResult();
        goto L21
    L16:
        r6 = r4.getTrackGroups();
        goto L17
    L28:
        if (r15.equals(this.playbackInfo.periodId) == true) goto L30;
        TrackGroupArray r5 = TrackGroupArray.EMPTY;
        TrackSelectorResult r8 = this.emptyTrackSelectorResult;
        r10 = r5;
        r11 = r8;
        r12 = ImmutableList.m212of();
        goto L31
    L5:
        if (r16 != this.playbackInfo.positionUs) goto L10;
        if (r15.equals(this.playbackInfo.periodId) == false) goto L10;
        r0 = false;
        goto L11
    }

    private boolean hasReachedServerSideInsertedAdsTransition(Renderer r6, MediaPeriodHolder r7) {
        MediaPeriodHolder r0 = r7.getNext();
        if (r7.info.isFollowedByTransitionToSameStream == true) goto L5;
    L13:
        return false;
    L5:
        if (r0.prepared == false) goto L13;
        if ((r6 instanceof TextRenderer) == false) goto L9;
    L12:
        return true;
    L9:
        if ((r6 instanceof MetadataRenderer) == true) goto L12;
        if (r6.getReadingPositionUs() < r0.getStartPositionRendererTime()) goto L13;
        goto L12
    }

    private boolean hasReadingPeriodFinishedReading() {
        MediaPeriodHolder r0 = this.queue.getReadingPeriod();
        if (r0.prepared == true) goto L5;
        return false;
    L5:
        int r1 = 0;
    L7:
        if (r1 >= this.renderers.length) goto L18;
        Renderer r3 = this.renderers[r1];
        SampleStream r4 = r0.sampleStreams[r1];
        if (r3.getStream() != r4) goto L17;
        if (r4 == null) goto L16;
        if (r3.hasReadStreamToEnd() == true) goto L16;
        if (hasReachedServerSideInsertedAdsTransition(r3, r0) == false) goto L17;
    L16:
        r1 = r1 + 1;
    L17:
        return false;
    L18:
        return true;
    }

    private static boolean isIgnorableServerSideAdInsertionPeriodChange(boolean r4, MediaSource.MediaPeriodId r5, long r6, MediaSource.MediaPeriodId r8, Timeline.Period r9, long r10) {
        if (r4 == false) goto L5;
    L26:
        return false;
    L5:
        if (r6 != r10) goto L26;
        if (r5.periodUid.equals(r8.periodUid) == false) goto L26;
        if (r5.isAd() == false) goto L21;
        if (r9.isServerSideInsertedAdGroup(r5.adGroupIndex) == false) goto L21;
        if (r9.getAdState(r5.adGroupIndex, r5.adIndexInAdGroup) != 4) goto L16;
        return false;
    L16:
        if (r9.getAdState(r5.adGroupIndex, r5.adIndexInAdGroup) != 2) goto L19;
        return false;
    L19:
        return true;
    L21:
        if (r8.isAd() == true) goto L23;
        return false;
    L23:
        if (r9.isServerSideInsertedAdGroup(r8.adGroupIndex) == true) goto L25;
        return false;
    L25:
        return true;
    }

    private boolean isLoadingPossible(MediaPeriodHolder r5) {
        if (r5 != null) goto L4;
    L8:
        return false;
    L4:
        if (r5.hasLoadingError() == true) goto L8;
        if (r5.getNextLoadPositionUs() == Long.MIN_VALUE) goto L8;
        return true;
    }

    private static boolean isRendererEnabled(Renderer r1) {
        if (r1.getState() == 0) goto L5;
        return true;
    L5:
        return false;
    }

    private boolean isTimelineReady() {
        MediaPeriodHolder r0 = this.queue.getPlayingPeriod();
        long r1 = r0.info.durationUs;
        if (r0.prepared == true) goto L5;
    L11:
        return false;
    L5:
        if (r1 != C0180C.TIME_UNSET) goto L7;
    L10:
        return true;
    L7:
        if (this.playbackInfo.positionUs < r1) goto L10;
        if (shouldPlayWhenReady() == true) goto L11;
        goto L10
    }

    private static boolean isUsingPlaceholderPeriod(PlaybackInfo r3, Timeline.Period r4) {
        MediaSource.MediaPeriodId r0 = r3.periodId;
        Timeline r1 = r3.timeline;
        if (r1.isEmpty() == false) goto L5;
    L8:
        return true;
    L5:
        if (r1.getPeriodByUid(r0.periodUid, r4).isPlaceholder == true) goto L8;
        return false;
    }

    private void maybeContinueLoading() {
        this.shouldContinueLoading = shouldContinueLoading();
        if (this.shouldContinueLoading == false) goto L5;
        MediaPeriodHolder r0 = (MediaPeriodHolder) Assertions.checkNotNull(this.queue.getLoadingPeriod());
        r0.continueLoading(new LoadingInfo.Builder().setPlaybackPositionUs(r0.toPeriodTime(this.rendererPositionUs)).setPlaybackSpeed(this.mediaClock.getPlaybackParameters().speed).setLastRebufferRealtimeMs(this.lastRebufferRealtimeMs).build());
    L5:
        updateIsLoading();
    }

    private void maybeContinuePreloading() {
        this.queue.maybeUpdatePreloadMediaPeriodHolder();
        MediaPeriodHolder r0 = this.queue.getPreloadingPeriod();
        if (r0 != null) goto L5;
        return;
    L5:
        if (r0.prepareCalled == false) goto L9;
        if (r0.prepared == true) goto L9;
        return;
    L9:
        if (r0.mediaPeriod.isLoading() == true) goto L25;
        LoadControl r1 = this.loadControl;
        Timeline r2 = this.playbackInfo.timeline;
        MediaSource.MediaPeriodId r3 = r0.info.f35id;
        if (r0.prepared == false) goto L13;
        long r4 = r0.mediaPeriod.getBufferedPositionUs();
    L15:
        if (r1.shouldContinuePreloading(r2, r3, r4) == true) goto L18;
        return;
    L18:
        if (r0.prepareCalled == true) goto L20;
        r0.prepare(this, r0.info.startPositionUs);
        return;
    L20:
        r0.continueLoading(new LoadingInfo.Builder().setPlaybackPositionUs(r0.toPeriodTime(this.rendererPositionUs)).setPlaybackSpeed(this.mediaClock.getPlaybackParameters().speed).setLastRebufferRealtimeMs(this.lastRebufferRealtimeMs).build());
        return;
    L13:
        r4 = 0;
        goto L15
    }

    private void maybeNotifyPlaybackInfoChanged() {
        this.playbackInfoUpdate.setPlaybackInfo(this.playbackInfo);
        if (PlaybackInfoUpdate.access$100(this.playbackInfoUpdate) == false) goto L6;
        this.playbackInfoUpdateListener.onPlaybackInfoUpdate(this.playbackInfoUpdate);
        this.playbackInfoUpdate = new PlaybackInfoUpdate(this.playbackInfo);
        return;
    }

    private void maybeThrowRendererStreamError(int r9) throws IOException, ExoPlaybackException {
        Renderer r0 = this.renderers[r9];
        r0.maybeThrowStreamError();     // Catch: RuntimeException -> L5 Throwable -> L7
        return;
    L7:
        e = move-exception;
        switch(r0.getTrackType()) {
            case 3: goto L11;
            case 4: goto L10;
            case 5: goto L11;
            default: goto L10;
        };
    L10:
        throw e;
    L11:
        TrackSelectorResult r2 = this.queue.getPlayingPeriod().getTrackSelectorResult();
        Log.m45e(TAG, "Disabling track due to error: " + Format.toLogString(r2.selections[r9].getSelectedFormat()), e);
        TrackSelectorResult r3 = new TrackSelectorResult((RendererConfiguration[]) r2.rendererConfigurations.clone(), (ExoTrackSelection[]) r2.selections.clone(), r2.tracks, r2.info);
        r3.rendererConfigurations[r9] = null;
        r3.selections[r9] = null;
        disableRenderer(r9);
        this.queue.getPlayingPeriod().applyTrackSelection(r3, this.playbackInfo.positionUs, false);
    }

    private void maybeTriggerOnRendererReadyChanged(final int r3, final boolean r4) {
        if (this.rendererReportedReady[r3] == r4) goto L6;
        this.rendererReportedReady[r3] = r4;
        this.applicationLooperHandler.post(new ExoPlayerImplInternal$$ExternalSyntheticLambda1(this, r3, r4));
        return;
    }

    private void maybeTriggerPendingMessages(long r8, long r10) throws ExoPlaybackException {
        if (this.pendingMessages.isEmpty() == false) goto L5;
        return;
    L5:
        if (this.playbackInfo.periodId.isAd() == false) goto L8;
        return;
    L8:
        if (this.deliverPendingMessageAtStartPositionRequired == false) goto L10;
        r8 = r8 - 1;
        this.deliverPendingMessageAtStartPositionRequired = false;
    L10:
        int r0 = this.playbackInfo.timeline.getIndexOfPeriod(this.playbackInfo.periodId.periodUid);
        int r1 = Math.min(this.nextPendingMessageIndexHint, this.pendingMessages.size());
        if (r1 <= 0) goto L13;
        PendingMessageInfo r3 = this.pendingMessages.get(r1 - 1);
    L14:
        if (r3 == null) goto L27;
        if (r3.resolvedPeriodIndex <= r0) goto L18;
    L21:
        r1 = r1 - 1;
        if (r1 <= 0) goto L24;
        PendingMessageInfo r4 = this.pendingMessages.get(r1 - 1);
    L25:
        r3 = r4;
        goto L14
    L24:
        r4 = null;
        goto L25
    L18:
        if (r3.resolvedPeriodIndex != r0) goto L27;
        if (r3.resolvedPeriodTimeUs > r8) goto L21;
    L27:
        if (r1 >= this.pendingMessages.size()) goto L29;
        PendingMessageInfo r5 = this.pendingMessages.get(r1);
    L31:
        if (r5 == null) goto L45;
        if (r5.resolvedPeriodUid == null) goto L45;
        if (r5.resolvedPeriodIndex >= r0) goto L37;
    L40:
        r1 = r1 + 1;
        if (r1 >= this.pendingMessages.size()) goto L43;
        PendingMessageInfo r6 = this.pendingMessages.get(r1);
    L44:
        r5 = r6;
        goto L31
    L43:
        r6 = null;
        goto L44
    L37:
        if (r5.resolvedPeriodIndex != r0) goto L45;
        if (r5.resolvedPeriodTimeUs <= r8) goto L40;
    L45:
        if (r5 == null) goto L76;
        if (r5.resolvedPeriodUid == null) goto L76;
        if (r5.resolvedPeriodIndex != r0) goto L76;
        if (r5.resolvedPeriodTimeUs <= r8) goto L76;
        if (r5.resolvedPeriodTimeUs > r10) goto L76;
        sendMessageToTarget(r5.message);     // Catch: Throwable -> L67
        if (r5.message.getDeleteAfterDelivery() == false) goto L58;
    L61:
        this.pendingMessages.remove(r1);
    L63:
        if (r1 >= this.pendingMessages.size()) goto L65;
        PendingMessageInfo r7 = this.pendingMessages.get(r1);
    L66:
        r5 = r7;
        goto L45
    L65:
        r7 = null;
        goto L66
    L58:
        if (r5.message.isCanceled() == true) goto L61;
        r1 = r1 + 1;
    L67:
        th = move-exception;
        if (r5.message.getDeleteAfterDelivery() == false) goto L71;
    L74:
        this.pendingMessages.remove(r1);
    L75:
        throw th;
    L71:
        if (r5.message.isCanceled() == true) goto L74;
        int r2 = r1 + 1;
    L76:
        this.nextPendingMessageIndexHint = r1;
        return;
    L29:
        r5 = null;
        goto L31
    L13:
        r3 = null;
        goto L14
    }

    private boolean maybeUpdateLoadingPeriod() throws ExoPlaybackException {
        boolean r0 = false;
        this.queue.reevaluateBuffer(this.rendererPositionUs);
        if (this.queue.shouldLoadNextMediaPeriod() == false) goto L17;
        MediaPeriodInfo r1 = this.queue.getNextMediaPeriodInfo(this.rendererPositionUs, this.playbackInfo);
        if (r1 == null) goto L17;
        MediaPeriodHolder r2 = this.queue.enqueueNextMediaPeriodHolder(r1);
        if (r2.prepareCalled == true) goto L10;
        r2.prepare(this, r1.startPositionUs);
    L13:
        if (this.queue.getPlayingPeriod() != r2) goto L15;
        resetRendererPosition(r1.startPositionUs);
    L15:
        handleLoadingMediaPeriodChanged(false);
        r0 = true;
        goto L17
    L10:
        if (r2.prepared == false) goto L13;
        this.handler.obtainMessage(8, r2.mediaPeriod).sendToTarget();
    L17:
        if (this.shouldContinueLoading == false) goto L19;
        this.shouldContinueLoading = isLoadingPossible(this.queue.getLoadingPeriod());
        updateIsLoading();
    L20:
        return r0;
    L19:
        maybeContinueLoading();
        goto L20
    }

    private void maybeUpdateOffloadScheduling() {
        MediaPeriodHolder r0 = this.queue.getPlayingPeriod();
        if (r0 == null) goto L27;
        TrackSelectorResult r1 = r0.getTrackSelectorResult();
        boolean r2 = false;
        boolean r3 = true;
        int r4 = 0;
    L5:
        boolean r6 = true;
        if (r4 >= this.renderers.length) goto L16;
        if (r1.isRendererEnabled(r4) == false) goto L15;
        if (this.renderers[r4].getTrackType() != 1) goto L11;
        if (r1.rendererConfigurations[r4].offloadModePreferred == 0) goto L15;
        r2 = true;
        goto L15
    L11:
        r3 = false;
    L15:
        r4 = r4 + 1;
    L16:
        if (r2 == false) goto L19;
        if (r3 == false) goto L19;
    L20:
        setOffloadSchedulingEnabled(r6);
        return;
    L19:
        r6 = false;
        goto L20
    }

    private void maybeUpdatePlayingPeriod() throws ExoPlaybackException {
        boolean r10 = false;
    L4:
        if (shouldAdvancePlayingPeriod() == false) goto L25;
        if (r10 == false) goto L7;
        maybeNotifyPlaybackInfoChanged();
    L7:
        MediaPeriodHolder r11 = (MediaPeriodHolder) Assertions.checkNotNull(this.queue.advancePlayingPeriod());
        boolean r2 = true;
        if (this.playbackInfo.periodId.periodUid.equals(r11.info.f35id.periodUid) == true) goto L10;
    L16:
        boolean r1 = false;
    L17:
        boolean r12 = r1;
        MediaSource.MediaPeriodId r3 = r11.info.f35id;
        long r4 = r11.info.startPositionUs;
        long r6 = r11.info.requestedContentPositionUs;
        long r8 = r11.info.startPositionUs;
        if (r12 == false) goto L21;
        r2 = false;
    L21:
        this.playbackInfo = handlePositionDiscontinuity(r3, r4, r6, r8, r2, 0);
        resetPendingPauseAtEndOfPeriod();
        updatePlaybackPositions();
        if (this.playbackInfo.playbackState != 3) goto L24;
        startRenderers();
    L24:
        allowRenderersToRenderStartOfStreams();
        r10 = true;
        goto L4
    L10:
        if (this.playbackInfo.periodId.adGroupIndex != (-1)) goto L16;
        if (r11.info.f35id.adGroupIndex != (-1)) goto L16;
        if (this.playbackInfo.periodId.nextAdGroupIndex == r11.info.f35id.nextAdGroupIndex) goto L16;
        r1 = true;
        goto L17
    }

    private void maybeUpdatePreloadPeriods(boolean r5) {
        if (this.preloadConfiguration.targetPreloadDurationUs != C0180C.TIME_UNSET) goto L5;
        return;
    L5:
        if (r5 == false) goto L7;
    L8:
        this.lastPreloadPoolInvalidationTimeline = this.playbackInfo.timeline;
        this.queue.invalidatePreloadPool(this.playbackInfo.timeline);
    L9:
        maybeContinuePreloading();
        return;
    L7:
        if (this.playbackInfo.timeline.equals(this.lastPreloadPoolInvalidationTimeline) == true) goto L9;
        goto L8
    }

    private void maybeUpdateReadingPeriod() throws ExoPlaybackException {
        MediaPeriodHolder r0 = this.queue.getReadingPeriod();
        if (r0 != null) goto L6;
        return;
    L6:
        if (r0.getNext() == null) goto L47;
        if (this.pendingPauseAtEndOfPeriod == true) goto L47;
        if (hasReadingPeriodFinishedReading() == true) goto L14;
        return;
    L14:
        if (r0.getNext().prepared == false) goto L16;
    L18:
        TrackSelectorResult r4 = r0.getTrackSelectorResult();
        MediaPeriodHolder r1 = this.queue.advanceReadingPeriod();
        TrackSelectorResult r5 = r1.getTrackSelectorResult();
        updatePlaybackSpeedSettingsForNewPeriod(this.playbackInfo.timeline, r1.info.f35id, this.playbackInfo.timeline, r0.info.f35id, C0180C.TIME_UNSET, false);
        if (r1.prepared == true) goto L21;
    L26:
        int r2 = 0;
    L28:
        if (r2 >= this.renderers.length) goto L44;
        boolean r3 = r4.isRendererEnabled(r2);
        boolean r6 = r5.isRendererEnabled(r2);
        if (r3 == false) goto L43;
        if (this.renderers[r2].isCurrentStreamFinal() == true) goto L43;
        if (this.rendererCapabilities[r2].getTrackType() != (-2)) goto L36;
        boolean r9 = true;
    L37:
        RendererConfiguration r10 = r4.rendererConfigurations[r2];
        RendererConfiguration r11 = r5.rendererConfigurations[r2];
        if (r6 == true) goto L40;
    L42:
        setCurrentStreamFinal(this.renderers[r2], r1.getStartPositionRendererTime());
        goto L43
    L40:
        if (r11.equals(r10) == false) goto L42;
        if (r9 == false) goto L43;
    L36:
        r9 = false;
    L43:
        r2 = r2 + 1;
        goto L28
    L44:
        return;
    L21:
        if (r1.mediaPeriod.readDiscontinuity() == C0180C.TIME_UNSET) goto L26;
        setAllRendererStreamsFinal(r1.getStartPositionRendererTime());
        if (r1.isFullyBuffered() == true) goto L78;
        this.queue.removeAfter(r1);
        handleLoadingMediaPeriodChanged(false);
        maybeContinueLoading();
        return;
    L78:
        return;
    L16:
        if (this.rendererPositionUs >= r0.getNext().getStartPositionRendererTime()) goto L18;
        return;
    L47:
        if (r0.info.isFinal == false) goto L49;
    L50:
        int r7 = 0;
    L52:
        if (r7 >= this.renderers.length) goto L79;
        Renderer r8 = this.renderers[r7];
        SampleStream r12 = r0.sampleStreams[r7];
        if (r12 == null) goto L66;
        if (r8.getStream() != r12) goto L66;
        if (r8.hasReadStreamToEnd() == false) goto L66;
        if (r0.info.durationUs != C0180C.TIME_UNSET) goto L62;
    L64:
        long r13 = -9223372036854775807L;
    L65:
        setCurrentStreamFinal(r8, r13);
        goto L66
    L62:
        if (r0.info.durationUs == Long.MIN_VALUE) goto L64;
        r13 = r0.getRendererOffset() + r0.info.durationUs;
    L66:
        r7 = r7 + 1;
        goto L52
    L79:
        return;
    L49:
        if (this.pendingPauseAtEndOfPeriod == true) goto L50;
    }

    private void maybeUpdateReadingRenderers() throws ExoPlaybackException {
        MediaPeriodHolder r0 = this.queue.getReadingPeriod();
        if (r0 != null) goto L5;
        return;
    L5:
        if (this.queue.getPlayingPeriod() != r0) goto L7;
        return;
    L7:
        if (r0.allRenderersInCorrectState == false) goto L10;
        return;
    L10:
        if (replaceStreamsOrDisableRendererForTransition() == false) goto L14;
        enableRenderers();
        return;
    }

    private void mediaSourceListUpdateRequestedInternal() throws ExoPlaybackException {
        handleMediaSourceListInfoRefreshed(this.mediaSourceList.createTimeline(), true);
    }

    private void moveMediaItemsInternal(MoveMediaItemsMessage r6) throws ExoPlaybackException {
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        handleMediaSourceListInfoRefreshed(this.mediaSourceList.moveMediaSourceRange(r6.fromIndex, r6.toIndex, r6.newFromIndex, r6.shuffleOrder), false);
    }

    private void notifyTrackSelectionDiscontinuity() {
        MediaPeriodHolder r0 = this.queue.getPlayingPeriod();
    L3:
        if (r0 == null) goto L11;
        ExoTrackSelection[] r1 = r0.getTrackSelectorResult().selections;
        int r2 = r1.length;
        int r3 = 0;
    L5:
        if (r3 >= r2) goto L10;
        ExoTrackSelection r4 = r1[r3];
        if (r4 == null) goto L9;
        r4.onDiscontinuity();
    L9:
        r3 = r3 + 1;
        goto L5
    L10:
        r0 = r0.getNext();
        goto L3
    }

    private void notifyTrackSelectionPlayWhenReadyChanged(boolean r6) {
        MediaPeriodHolder r0 = this.queue.getPlayingPeriod();
    L3:
        if (r0 == null) goto L11;
        ExoTrackSelection[] r1 = r0.getTrackSelectorResult().selections;
        int r2 = r1.length;
        int r3 = 0;
    L5:
        if (r3 >= r2) goto L10;
        ExoTrackSelection r4 = r1[r3];
        if (r4 == null) goto L9;
        r4.onPlayWhenReadyChanged(r6);
    L9:
        r3 = r3 + 1;
        goto L5
    L10:
        r0 = r0.getNext();
        goto L3
    }

    private void notifyTrackSelectionRebuffer() {
        MediaPeriodHolder r0 = this.queue.getPlayingPeriod();
    L3:
        if (r0 == null) goto L11;
        ExoTrackSelection[] r1 = r0.getTrackSelectorResult().selections;
        int r2 = r1.length;
        int r3 = 0;
    L5:
        if (r3 >= r2) goto L10;
        ExoTrackSelection r4 = r1[r3];
        if (r4 == null) goto L9;
        r4.onRebuffer();
    L9:
        r3 = r3 + 1;
        goto L5
    L10:
        r0 = r0.getNext();
        goto L3
    }

    private void prepareInternal() {
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        resetInternal(false, false, false, true);
        this.loadControl.onPrepared(this.playerId);
        if (this.playbackInfo.timeline.isEmpty() == false) goto L5;
        int r0 = 4;
    L6:
        setState(r0);
        this.mediaSourceList.prepare(this.bandwidthMeter.getTransferListener());
        this.handler.sendEmptyMessage(2);
        return;
    L5:
        r0 = 2;
        goto L6
    }

    private void releaseInternal() {
        resetInternal(true, false, true, false);     // Catch: Throwable -> L12
        releaseRenderers();     // Catch: Throwable -> L12
        this.loadControl.onReleased(this.playerId);     // Catch: Throwable -> L12
        setState(1);     // Catch: Throwable -> L12
        this.playbackLooperProvider.releaseLooper();
        monitor-enter(this);
        this.released = true;     // Catch: Throwable -> L9
        notifyAll();     // Catch: Throwable -> L9
        monitor-exit(this);     // Catch: Throwable -> L9
        return;
    L9:
        th = move-exception;
        throw th;
    L12:
        th = move-exception;
        this.playbackLooperProvider.releaseLooper();
        monitor-enter(this);
        this.released = true;     // Catch: Throwable -> L18
        notifyAll();     // Catch: Throwable -> L18
        throw th;
    L18:
        th = move-exception;
        throw th;
    }

    private void releaseRenderers() {
        int r0 = 0;
    L4:
        if (r0 >= this.renderers.length) goto L6;
        this.rendererCapabilities[r0].clearListener();
        this.renderers[r0].release();
        r0 = r0 + 1;
        goto L4
    }

    private void removeMediaItemsInternal(int r3, int r4, ShuffleOrder r5) throws ExoPlaybackException {
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        handleMediaSourceListInfoRefreshed(this.mediaSourceList.removeMediaSourceRange(r3, r4, r5), false);
    }

    private boolean replaceStreamsOrDisableRendererForTransition() throws ExoPlaybackException {
        MediaPeriodHolder r0 = this.queue.getReadingPeriod();
        TrackSelectorResult r1 = r0.getTrackSelectorResult();
        boolean r2 = false;
        int r3 = 0;
    L4:
        if (r3 >= this.renderers.length) goto L28;
        Renderer r5 = this.renderers[r3];
        if (isRendererEnabled(r5) == false) goto L26;
        if (r5.getStream() == r0.sampleStreams[r3]) goto L11;
        boolean r4 = true;
    L13:
        if (r1.isRendererEnabled(r3) == false) goto L17;
        if (r4 == false) goto L26;
    L17:
        if (r5.isCurrentStreamFinal() == true) goto L23;
        r5.replaceStream(getFormats(r1.selections[r3]), r0.sampleStreams[r3], r0.getStartPositionRendererTime(), r0.getRendererOffset(), r0.info.f35id);
        if (this.offloadSchedulingEnabled == false) goto L26;
        setOffloadSchedulingEnabled(false);
        goto L26
    L23:
        if (r5.isEnded() == false) goto L25;
        disableRenderer(r3);
        goto L26
    L25:
        r2 = true;
        goto L26
    L11:
        r4 = false;
    L26:
        r3 = r3 + 1;
        goto L4
    L28:
        return !r2;
    }

    private void reselectTracksInternal() throws ExoPlaybackException {
        float r10 = this.mediaClock.getPlaybackParameters().speed;
        MediaPeriodHolder r1 = this.queue.getPlayingPeriod();
        MediaPeriodHolder r11 = this.queue.getReadingPeriod();
        TrackSelectorResult r3 = null;
        MediaPeriodHolder r12 = r1;
        boolean r13 = true;
    L3:
        if (r12 == null) goto L52;
        if (r12.prepared == false) goto L64;
        TrackSelectorResult r14 = r12.selectTracks(r10, this.playbackInfo.timeline, this.playbackInfo.playWhenReady);
        if (r12 != this.queue.getPlayingPeriod()) goto L10;
        TrackSelectorResult r15 = r14;
    L12:
        if (r14.isEquivalent(r12.getTrackSelectorResult()) == false) goto L14;
        float r19 = r10;
        boolean r16 = r13;
        if (r12 != r11) goto L49;
        r13 = false;
    L50:
        r12 = r12.getNext();
        r3 = r15;
        r10 = r19;
        goto L3
    L49:
        r13 = r16;
        goto L50
    L14:
        if (r13 == false) goto L39;
        MediaPeriodHolder r4 = this.queue.getPlayingPeriod();
        boolean r20 = this.queue.removeAfter(r4);
        boolean[] r5 = new boolean[this.renderers.length];
        long r6 = r4.applyTrackSelection((TrackSelectorResult) Assertions.checkNotNull(r15), this.playbackInfo.positionUs, r20, r5);
        if (this.playbackInfo.playbackState != 4) goto L18;
    L20:
        boolean r8 = false;
    L21:
        this.playbackInfo = handlePositionDiscontinuity(this.playbackInfo.periodId, r6, this.playbackInfo.requestedContentPositionUs, this.playbackInfo.discontinuityStartPositionUs, r8, 5);
        if (r8 == false) goto L24;
        resetRendererPosition(r6);
    L24:
        boolean[] r2 = new boolean[this.renderers.length];
        int r7 = 0;
    L26:
        if (r7 >= this.renderers.length) goto L38;
        Renderer r9 = this.renderers[r7];
        r2[r7] = isRendererEnabled(r9);
        SampleStream r17 = r4.sampleStreams[r7];
        if (r2[r7] == false) goto L37;
        if (r17 == r9.getStream()) goto L33;
        disableRenderer(r7);
        goto L37
    L33:
        if (r5[r7] == false) goto L37;
        r9.resetPosition(this.rendererPositionUs);
    L37:
        r7 = r7 + 1;
        r14 = r14;
        goto L26
    L38:
        enableRenderers(r2, this.rendererPositionUs);
    L42:
        handleLoadingMediaPeriodChanged(true);
        if (this.playbackInfo.playbackState == 4) goto L63;
        maybeContinueLoading();
        updatePlaybackPositions();
        this.handler.sendEmptyMessage(2);
        return;
    L63:
        return;
    L18:
        if (r6 == this.playbackInfo.positionUs) goto L20;
        r8 = true;
        goto L21
    L39:
        this.queue.removeAfter(r12);
        if (r12.prepared == false) goto L42;
        r12.applyTrackSelection(r14, Math.max(r12.info.startPositionUs, r12.toPeriodTime(this.rendererPositionUs)), false);
        goto L42
    L10:
        r15 = r3;
        goto L12
    L64:
        return;
    }

    private void reselectTracksInternalAndSeek() throws ExoPlaybackException {
        reselectTracksInternal();
        seekToCurrentPosition(true);
    }

    private void resetInternal(boolean r33, boolean r34, boolean r35, boolean r36) {
        this.handler.removeMessages(2);
        ExoPlaybackException r2 = null;
        this.pendingRecoverableRendererError = null;
        updateRebufferingState(false, true);
        this.mediaClock.stop();
        this.rendererPositionUs = MediaPeriodQueue.INITIAL_RENDERER_POSITION_OFFSET_US;
        int r4 = 0;
    L4:
        if (r4 >= this.renderers.length) goto L12;
        disableRenderer(r4);     // Catch: RuntimeException -> L7 Throwable -> L9
    L11:
        r4 = r4 + 1;
    L9:
        e = move-exception;
        Log.m45e(TAG, "Disable failed.", e);
        goto L11
    L12:
        if (r33 == false) goto L22;
        Renderer[] r5 = this.renderers;
        int r6 = r5.length;
        int r7 = 0;
    L14:
        if (r7 >= r6) goto L22;
        Renderer r8 = r5[r7];
        if (this.renderersToReset.remove(r8) == false) goto L21;
        r8.reset();     // Catch: RuntimeException -> L19
    L19:
        e = move-exception;
        Log.m45e(TAG, "Reset failed.", e);
    L21:
        r7 = r7 + 1;
    L22:
        this.enabledRendererCount = 0;
        MediaSource.MediaPeriodId r0 = this.playbackInfo.periodId;
        long r9 = this.playbackInfo.positionUs;
        if (this.playbackInfo.periodId.isAd() == false) goto L25;
    L28:
        long r10 = this.playbackInfo.requestedContentPositionUs;
    L30:
        if (r34 == false) goto L35;
        this.pendingInitialSeekPosition = null;
        Pair<MediaSource.MediaPeriodId, Long> r11 = getPlaceholderFirstMediaPeriodPositionUs(this.playbackInfo.timeline);
        r0 = (MediaSource.MediaPeriodId) r11.first;
        long r12 = ((Long) r11.second).longValue();
        if (r0.equals(this.playbackInfo.periodId) == true) goto L34;
        long r13 = r12;
        boolean r31 = true;
        long r14 = -9223372036854775807L;
    L36:
        this.queue.clear();
        this.shouldContinueLoading = false;
        Timeline r3 = this.playbackInfo.timeline;
        if (r35 == true) goto L39;
    L45:
        MediaSource.MediaPeriodId r15 = r0;
        Timeline r16 = r3;
    L46:
        int r17 = this.playbackInfo.playbackState;
        if (r36 == true) goto L50;
        r2 = this.playbackInfo.playbackError;
    L50:
        ExoPlaybackException r18 = r2;
        if (r31 == false) goto L53;
        TrackGroupArray r1 = TrackGroupArray.EMPTY;
    L54:
        TrackGroupArray r19 = r1;
        if (r31 == false) goto L57;
        TrackSelectorResult r20 = this.emptyTrackSelectorResult;
    L58:
        TrackSelectorResult r110 = r20;
        if (r31 == false) goto L61;
        List r21 = ImmutableList.m212of();
    L62:
        this.playbackInfo = new PlaybackInfo(r16, r15, r14, r13, r17, r18, false, r19, r110, r21, r15, this.playbackInfo.playWhenReady, this.playbackInfo.playWhenReadyChangeReason, this.playbackInfo.playbackSuppressionReason, this.playbackInfo.playbackParameters, r13, 0, r13, 0, false);
        if (r35 == false) goto L77;
        this.queue.releasePreloadPool();
        this.mediaSourceList.release();
        return;
    L77:
        return;
    L61:
        r21 = this.playbackInfo.staticMetadata;
        goto L62
    L57:
        r20 = this.playbackInfo.trackSelectorResult;
        goto L58
    L53:
        r1 = this.playbackInfo.trackGroups;
        goto L54
    L39:
        if ((r3 instanceof PlaylistTimeline) == false) goto L45;
        r3 = ((PlaylistTimeline) this.playbackInfo.timeline).copyWithPlaceholderTimeline(this.mediaSourceList.getShuffleOrder());
        if (r0.adGroupIndex == (-1)) goto L45;
        r3.getPeriodByUid(r0.periodUid, this.period);
        if (r3.getWindow(this.period.windowIndex, this.window).isLive() == false) goto L45;
        r15 = new MediaSource.MediaPeriodId(r0.periodUid, r0.windowSequenceNumber);
        r16 = r3;
        goto L46
    L34:
        r13 = r12;
        r31 = false;
        r14 = -9223372036854775807L;
        goto L36
    L35:
        r13 = r9;
        r31 = false;
        r14 = r10;
        goto L36
    L25:
        if (isUsingPlaceholderPeriod(this.playbackInfo, this.period) == true) goto L28;
        r10 = this.playbackInfo.positionUs;
        goto L30
    }

    private void resetPendingPauseAtEndOfPeriod() {
        MediaPeriodHolder r0 = this.queue.getPlayingPeriod();
        if (r0 != null) goto L5;
    L9:
        boolean r1 = false;
    L10:
        this.pendingPauseAtEndOfPeriod = r1;
        return;
    L5:
        if (r0.info.isLastInTimelineWindow == false) goto L9;
        if (this.pauseAtEndOfWindow == false) goto L9;
        r1 = true;
        goto L10
    }

    private void resetRendererPosition(long r8) throws ExoPlaybackException {
        MediaPeriodHolder r0 = this.queue.getPlayingPeriod();
        if (r0 != null) goto L5;
        long r1 = MediaPeriodQueue.INITIAL_RENDERER_POSITION_OFFSET_US + r8;
    L6:
        this.rendererPositionUs = r1;
        this.mediaClock.resetPosition(this.rendererPositionUs);
        Renderer[] r2 = this.renderers;
        int r3 = r2.length;
        int r4 = 0;
    L7:
        if (r4 >= r3) goto L12;
        Renderer r5 = r2[r4];
        if (isRendererEnabled(r5) == false) goto L11;
        r5.resetPosition(this.rendererPositionUs);
    L11:
        r4 = r4 + 1;
        goto L7
    L12:
        notifyTrackSelectionDiscontinuity();
        return;
    L5:
        r1 = r0.toRendererTime(r8);
        goto L6
    }

    private static void resolvePendingMessageEndOfStreamPosition(Timeline r7, PendingMessageInfo r8, Timeline.Window r9, Timeline.Period r10) {
        int r1 = r7.getWindow(r7.getPeriodByUid(r8.resolvedPeriodUid, r10).windowIndex, r9).lastPeriodIndex;
        Object r2 = r7.getPeriod(r1, r10, true).uid;
        if (r10.durationUs == C0180C.TIME_UNSET) goto L5;
        long r3 = r10.durationUs - 1;
    L6:
        r8.setResolvedPosition(r1, r3, r2);
        return;
    L5:
        r3 = Long.MAX_VALUE;
        goto L6
    }

    private static boolean resolvePendingMessagePosition(PendingMessageInfo r19, Timeline r20, Timeline r21, int r22, boolean r23, Timeline.Window r24, Timeline.Period r25) {
        if (r19.resolvedPeriodUid == null) goto L5;
        int r10 = r20.getIndexOfPeriod(r19.resolvedPeriodUid);
        if (r10 != (-1)) goto L19;
        return false;
    L19:
        if (r19.message.getPositionMs() != Long.MIN_VALUE) goto L22;
        resolvePendingMessageEndOfStreamPosition(r20, r19, r24, r25);
        return true;
    L22:
        r19.resolvedPeriodIndex = r10;
        r21.getPeriodByUid(r19.resolvedPeriodUid, r25);
        if (r25.isPlaceholder == true) goto L25;
    L27:
        return true;
    L25:
        if (r21.getWindow(r25.windowIndex, r24).firstPeriodIndex != r21.getIndexOfPeriod(r19.resolvedPeriodUid)) goto L27;
        long r4 = r19.resolvedPeriodTimeUs + r25.getPositionInWindowUs();
        Pair<Object, Long> r9 = r20.getPeriodPositionUs(r24, r25, r20.getPeriodByUid(r19.resolvedPeriodUid, r25).windowIndex, r4);
        r19.setResolvedPosition(r20.getIndexOfPeriod(r9.first), ((Long) r9.second).longValue(), r9.first);
        goto L27
    L5:
        if (r19.message.getPositionMs() != Long.MIN_VALUE) goto L7;
        long r1 = C0180C.TIME_UNSET;
    L8:
        long r13 = r1;
        Pair<Object, Long> r2 = resolveSeekPositionUs(r20, new SeekPosition(r19.message.getTimeline(), r19.message.getMediaItemIndex(), r13), false, r22, r23, r24, r25);
        if (r2 != null) goto L11;
        return false;
    L11:
        r19.setResolvedPosition(r20.getIndexOfPeriod(r2.first), ((Long) r2.second).longValue(), r2.first);
        if (r19.message.getPositionMs() != Long.MIN_VALUE) goto L14;
        resolvePendingMessageEndOfStreamPosition(r20, r19, r24, r25);
    L14:
        return true;
    L7:
        r1 = Util.msToUs(r19.message.getPositionMs());
        goto L8
    }

    private void resolvePendingMessagePositions(Timeline r10, Timeline r11) {
        if (r10.isEmpty() == true) goto L5;
    L7:
        int r0 = this.pendingMessages.size() - 1;
    L8:
        if (r0 < 0) goto L13;
        Timeline r3 = r10;
        Timeline r4 = r11;
        if (resolvePendingMessagePosition(this.pendingMessages.get(r0), r3, r4, this.repeatMode, this.shuffleModeEnabled, this.window, this.period) == true) goto L12;
        this.pendingMessages.get(r0).message.markAsProcessed(false);
        this.pendingMessages.remove(r0);
    L12:
        r0 = r0 - 1;
        r10 = r3;
        r11 = r4;
        goto L8
    L13:
        Collections.sort(this.pendingMessages);
        return;
    L5:
        if (r11.isEmpty() == false) goto L7;
    }

    private static PositionUpdateForPlaylistChange resolvePositionForPlaylistChange(Timeline r35, PlaybackInfo r36, SeekPosition r37, MediaPeriodQueue r38, int r39, boolean r40, Timeline.Window r41, Timeline.Period r42) {
        if (r35.isEmpty() == true) goto L5;
        MediaSource.MediaPeriodId r10 = r36.periodId;
        Object r8 = r10.periodUid;
        boolean r9 = isUsingPlaceholderPeriod(r36, r42);
        if (r36.periodId.isAd() == true) goto L11;
        if (r9 == true) goto L11;
        long r11 = r36.positionUs;
    L12:
        long r13 = r11;
        int r15 = -1;
        boolean r16 = false;
        boolean r17 = false;
        boolean r18 = false;
        boolean r21 = false;
        if (r37 == null) goto L27;
        boolean r23 = r9;
        Pair<Object, Long> r2 = resolveSeekPositionUs(r35, r37, true, r39, r40, r41, r42);
        if (r2 != null) goto L18;
        r17 = true;
        r15 = r35.getFirstWindowIndex(r40);
    L26:
        int r3 = r15;
        boolean r30 = r16;
        boolean r31 = r17;
        boolean r32 = r18;
    L46:
        long r19 = r13;
        if (r3 == (-1)) goto L49;
        Timeline r0 = r35;
        Timeline.Period r4 = r42;
        Pair<Object, Long> r5 = r0.getPeriodPositionUs(r41, r4, r3, C0180C.TIME_UNSET);
        r8 = r5.first;
        long r28 = -9223372036854775807L;
        long r6 = ((Long) r5.second).longValue();
    L50:
        MediaSource.MediaPeriodId r14 = r38.resolveMediaPeriodIdForAdsAfterPeriodPositionChange(r0, r8, r6);
        if (r14.nextAdGroupIndex != (-1)) goto L53;
    L58:
        boolean r12 = true;
    L59:
        boolean r110 = r12;
        if (r10.periodUid.equals(r8) == true) goto L62;
    L68:
        boolean r111 = isIgnorableServerSideAdInsertionPeriodChange(r23, r10, r11, r14, r0.getPeriodByUid(r8, r4), r28);
        if (r21 == true) goto L73;
        if (r111 == true) goto L73;
        MediaSource.MediaPeriodId r25 = r14;
    L74:
        MediaSource.MediaPeriodId r112 = r25;
        long r113 = r6;
        if (r112.isAd() == true) goto L77;
        long r26 = r113;
    L86:
        return new PositionUpdateForPlaylistChange(r112, r26, r28, r30, r31, r32);
    L77:
        if (r112.equals(r10) == false) goto L79;
        r26 = r36.positionUs;
        goto L86
    L79:
        r0.getPeriodByUid(r112.periodUid, r4);
        if (r112.adIndexInAdGroup != r4.getFirstAdIndexToPlay(r112.adGroupIndex)) goto L82;
        long r7 = r4.getAdResumePositionUs();
    L83:
        r26 = r7;
        goto L86
    L82:
        r7 = 0;
    L73:
        r25 = r10;
        goto L74
    L62:
        if (r10.isAd() == true) goto L68;
        if (r14.isAd() == true) goto L68;
        if (r110 == false) goto L68;
        r21 = true;
        goto L68
    L53:
        if (r10.nextAdGroupIndex != (-1)) goto L55;
    L57:
        r12 = false;
        goto L59
    L55:
        if (r14.nextAdGroupIndex < r10.nextAdGroupIndex) goto L57;
    L49:
        r0 = r35;
        r4 = r42;
        r28 = r13;
        r6 = r19;
        goto L50
    L18:
        if (r37.windowPositionUs != C0180C.TIME_UNSET) goto L20;
        r15 = r35.getPeriodByUid(r2.first, r42).windowIndex;
    L22:
        if (r36.playbackState != 4) goto L24;
        boolean r20 = true;
    L25:
        r16 = r20;
        goto L26
    L24:
        r20 = false;
        goto L25
    L20:
        r8 = r2.first;
        r13 = ((Long) r2.second).longValue();
        r18 = true;
        goto L22
    L27:
        r23 = r9;
        if (r36.timeline.isEmpty() == false) goto L31;
        r3 = r35.getFirstWindowIndex(r40);
        r30 = false;
        r31 = false;
        r32 = false;
        goto L46
    L31:
        if (r35.getIndexOfPeriod(r8) != (-1)) goto L38;
        int r22 = resolveSubsequentPeriod(r41, r42, r39, r40, r8, r36.timeline, r35);
        if (r22 != (-1)) goto L35;
        r17 = true;
        int r114 = r35.getFirstWindowIndex(r40);
    L36:
        r3 = r114;
        r30 = false;
        r31 = r17;
        r32 = false;
        goto L46
    L35:
        r114 = r22;
        goto L36
    L38:
        if (r11 != C0180C.TIME_UNSET) goto L40;
        r3 = r35.getPeriodByUid(r8, r42).windowIndex;
        r30 = false;
        r31 = false;
        r32 = false;
        goto L46
    L40:
        if (r23 == false) goto L45;
        r36.timeline.getPeriodByUid(r10.periodUid, r42);
        if (r36.timeline.getWindow(r42.windowIndex, r41).firstPeriodIndex != r36.timeline.getIndexOfPeriod(r10.periodUid)) goto L44;
        Pair<Object, Long> r24 = r35.getPeriodPositionUs(r41, r42, r35.getPeriodByUid(r8, r42).windowIndex, r42.getPositionInWindowUs() + r11);
        r8 = r24.first;
        r13 = ((Long) r24.second).longValue();
    L44:
        r3 = -1;
        r30 = false;
        r31 = false;
        r32 = true;
        goto L46
    L45:
        r3 = -1;
        r30 = false;
        r31 = false;
        r32 = false;
    L11:
        r11 = r36.requestedContentPositionUs;
        goto L12
    L5:
        return new PositionUpdateForPlaylistChange(PlaybackInfo.getDummyPeriodForEmptyTimeline(), 0, C0180C.TIME_UNSET, false, true, false);
    }

    private static Pair<Object, Long> resolveSeekPositionUs(Timeline r11, SeekPosition r12, boolean r13, int r14, boolean r15, Timeline.Window r16, Timeline.Period r17) {
        Timeline r0 = r12.timeline;
        if (r11.isEmpty() == false) goto L6;
        return null;
    L6:
        if (r0.isEmpty() == false) goto L8;
        Timeline r1 = r11;
    L31:
        Pair<Object, Long> r2 = r1.getPeriodPositionUs(r16, r17, r12.windowIndex, r12.windowPositionUs);     // Catch: IndexOutOfBoundsException -> L28
        Timeline r7 = r1;
        if (r11.equals(r7) == false) goto L14;
        return r2;
    L14:
        if (r11.getIndexOfPeriod(r2.first) != (-1)) goto L16;
        if (r13 == false) goto L27;
        int r5 = resolveSubsequentPeriod(r16, r17, r14, r15, r2.first, r7, r11);
        if (r5 == (-1)) goto L27;
        return r11.getPeriodPositionUs(r16, r17, r5, C0180C.TIME_UNSET);
    L27:
        return null;
    L16:
        if (r7.getPeriodByUid(r2.first, r17).isPlaceholder == true) goto L18;
        return r2;
    L18:
        if (r7.getWindow(r17.windowIndex, r16).firstPeriodIndex == r7.getIndexOfPeriod(r2.first)) goto L20;
        return r2;
    L20:
        return r11.getPeriodPositionUs(r16, r17, r11.getPeriodByUid(r2.first, r17).windowIndex, r12.windowPositionUs);
    L30:
        return null;
    L8:
        r1 = r0;
        goto L31
    }

    static int resolveSubsequentPeriod(Timeline.Window r14, Timeline.Period r15, int r16, boolean r17, Object r18, Timeline r19, Timeline r20) {
        Object r9 = r19.getWindow(r19.getPeriodByUid(r18, r15).windowIndex, r14).uid;
        int r2 = 0;
    L4:
        if (r2 >= r20.getWindowCount()) goto L9;
        if (r20.getWindow(r2, r14).uid.equals(r9) == true) goto L7;
        r2 = r2 + 1;
        goto L4
    L7:
        return r2;
    L9:
        int r3 = r19.getIndexOfPeriod(r18);
        int r10 = r19.getPeriodCount();
        int r11 = -1;
        int r12 = 0;
    L11:
        if (r12 >= r10) goto L17;
        if (r11 != (-1)) goto L17;
        r3 = r19.getNextPeriodIndex(r3, r15, r14, r16, r17);
        if (r3 == (-1)) goto L17;
        r11 = r20.getIndexOfPeriod(r19.getUidOfPeriod(r3));
        r12 = r12 + 1;
    L17:
        if (r11 != (-1)) goto L20;
        return -1;
    L20:
        return r20.getPeriod(r11, r15).windowIndex;
    }

    private void scheduleNextWork(long r11) {
        if (this.playbackInfo.playbackState == 3) goto L5;
    L9:
        long r0 = BUFFERING_MAXIMUM_INTERVAL_MS;
    L11:
        if (this.dynamicSchedulingEnabled == true) goto L13;
    L20:
        this.handler.sendEmptyMessageAtTime(2, r11 + r0);
        return;
    L13:
        if (shouldPlayWhenReady() == false) goto L20;
        Renderer[] r2 = this.renderers;
        int r3 = r2.length;
        int r4 = 0;
    L15:
        if (r4 >= r3) goto L20;
        Renderer r5 = r2[r4];
        if (isRendererEnabled(r5) == false) goto L19;
        r0 = Math.min(r0, Util.usToMs(r5.getDurationToProgressUs(this.rendererPositionUs, this.rendererPositionElapsedRealtimeUs)));
    L19:
        r4 = r4 + 1;
        goto L15
    L5:
        if (this.dynamicSchedulingEnabled == false) goto L7;
    L8:
        r0 = 1000;
        goto L11
    L7:
        if (shouldPlayWhenReady() == true) goto L9;
        goto L8
    }

    private void seekToCurrentPosition(boolean r12) throws ExoPlaybackException {
        MediaSource.MediaPeriodId r2 = this.queue.getPlayingPeriod().info.f35id;
        long r3 = seekToPeriodPosition(r2, this.playbackInfo.positionUs, true, false);
        if (r3 == this.playbackInfo.positionUs) goto L6;
        this.playbackInfo = handlePositionDiscontinuity(r2, r3, this.playbackInfo.requestedContentPositionUs, this.playbackInfo.discontinuityStartPositionUs, r12, 5);
        return;
    }

    private void seekToInternal(SeekPosition r20) throws ExoPlaybackException {
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        Pair<Object, Long> r11 = resolveSeekPositionUs(this.playbackInfo.timeline, r20, true, this.repeatMode, this.shuffleModeEnabled, this.window, this.period);
        if (r11 != null) goto L5;
        Pair<MediaSource.MediaPeriodId, Long> r4 = getPlaceholderFirstMediaPeriodPositionUs(this.playbackInfo.timeline);
        MediaSource.MediaPeriodId r5 = (MediaSource.MediaPeriodId) r4.first;
        long r6 = ((Long) r4.second).longValue();
        long r13 = C0180C.TIME_UNSET;
        boolean r8 = !this.playbackInfo.timeline.isEmpty();
        MediaSource.MediaPeriodId r2 = r5;
        long r17 = 0;
    L93:
    L84:
        th = th;
        r8 = r8;
        long r3 = r6;
    L86:
        this.playbackInfo = handlePositionDiscontinuity(r2, r3, r13, r3, r8, 2);
        throw th;
    L92:
        if (this.playbackInfo.timeline.isEmpty() == false) goto L27;
        this.pendingInitialSeekPosition = r20;     // Catch: Throwable -> L24
    L32:
        long r7 = r6;
        r8 = r8;
        long r9 = r13;
    L76:
        this.playbackInfo = handlePositionDiscontinuity(r2, r7, r9, r7, r8, 2);
        return;
    L27:
        if (r11 == null) goto L29;
        long r10 = r6;
        if (r2.equals(this.playbackInfo.periodId) == false) goto L61;
        MediaPeriodHolder r12 = this.queue.getPlayingPeriod();     // Catch: Throwable -> L84
        if (r12 != null) goto L40;
    L45:
        long r18 = r10;
    L47:
        if (Util.usToMs(r18) != Util.usToMs(this.playbackInfo.positionUs)) goto L60;
        if (this.playbackInfo.playbackState != 2) goto L53;
    L56:
        long r14 = this.playbackInfo.positionUs;     // Catch: Throwable -> L84
        this.playbackInfo = handlePositionDiscontinuity(r2, r14, r13, r14, r8, 2);
        return;
    L53:
        if (this.playbackInfo.playbackState == 3) goto L56;
    L60:
        r10 = r18;
    L90:
    L82:
        th = th;
        r3 = r6;
        goto L86
    L63:
        if (this.playbackInfo.playbackState != 4) goto L65;
        boolean r0 = true;
    L66:
        long r19 = seekToPeriodPosition(r2, r10, r0);     // Catch: Throwable -> L82
        if (r6 == r19) goto L70;
        boolean r16 = true;
    L71:
        r8 = r8 | r16;
        MediaSource.MediaPeriodId r15 = r2;
        long r21 = r13;
        updatePlaybackSpeedSettingsForNewPeriod(this.playbackInfo.timeline, r15, this.playbackInfo.timeline, this.playbackInfo.periodId, r21, true);     // Catch: Throwable -> L78
        r2 = r15;
        r9 = r21;
        r7 = r19;
    L78:
        th = th;
        r2 = r15;
        r13 = r21;
        r3 = r19;
    L80:
        th = th;
        r3 = r19;
        goto L86
    L70:
        r16 = false;
        goto L71
    L65:
        r0 = false;
        goto L66
    L40:
        if (r12.prepared == false) goto L45;
        if (r10 == r17) goto L45;
        r18 = r12.mediaPeriod.getAdjustedSeekPositionUs(r10, this.seekParameters);     // Catch: Throwable -> L24
        goto L47
    L61:
        r8 = r8;
        goto L90
    L29:
        if (this.playbackInfo.playbackState == 1) goto L31;
        setState(4);     // Catch: Throwable -> L24
    L31:
        resetInternal(false, true, false, true);     // Catch: Throwable -> L24
    L24:
        th = th;
        r3 = r6;
        r8 = r8;
        goto L86
    L5:
        Object r22 = r11.first;
        long r23 = ((Long) r11.second).longValue();
        if (r20.windowPositionUs != C0180C.TIME_UNSET) goto L8;
        long r24 = -9223372036854775807L;
    L9:
        MediaSource.MediaPeriodId r110 = this.queue.resolveMediaPeriodIdForAdsAfterPeriodPositionChange(this.playbackInfo.timeline, r22, r23);
        if (r110.isAd() == false) goto L16;
        this.playbackInfo.timeline.getPeriodByUid(r110.periodUid, this.period);
        if (this.period.getFirstAdIndexToPlay(r110.adGroupIndex) != r110.adIndexInAdGroup) goto L14;
        long r111 = this.period.getAdResumePositionUs();
    L15:
        long r112 = r111;
        r13 = r24;
        r6 = r112;
        r2 = r110;
        r8 = true;
        r17 = 0;
        goto L93
    L14:
        r111 = 0;
        goto L15
    L16:
        r17 = 0;
        if (r20.windowPositionUs != C0180C.TIME_UNSET) goto L19;
        boolean r25 = true;
    L20:
        r13 = r24;
        r6 = r23;
        r8 = r25;
        r2 = r110;
        goto L93
    L19:
        r25 = false;
        goto L20
    L8:
        r24 = r23;
        goto L9
    }

    private long seekToPeriodPosition(MediaSource.MediaPeriodId r8, long r9, boolean r11) throws ExoPlaybackException {
        if (this.queue.getPlayingPeriod() == this.queue.getReadingPeriod()) goto L5;
        boolean r0 = true;
    L7:
        return seekToPeriodPosition(r8, r9, r0, r11);
    L5:
        r0 = false;
        goto L7
    }

    private long seekToPeriodPosition(MediaSource.MediaPeriodId r9, long r10, boolean r12, boolean r13) throws ExoPlaybackException {
        stopRenderers();
        updateRebufferingState(false, true);
        if (r13 == false) goto L5;
    L6:
        setState(2);
    L7:
        MediaPeriodHolder r2 = this.queue.getPlayingPeriod();
        MediaPeriodHolder r3 = r2;
    L8:
        if (r3 == null) goto L13;
        if (r9.equals(r3.info.f35id) == true) goto L13;
        r3 = r3.getNext();
    L13:
        if (r12 == true) goto L18;
        if (r2 != r3) goto L18;
        if (r3 != null) goto L17;
    L27:
        if (r3 == null) goto L35;
        this.queue.removeAfter(r3);
        if (r3.prepared == true) goto L32;
        r3.info = r3.info.copyWithStartPositionUs(r10);
    L34:
        resetRendererPosition(r10);
        maybeContinueLoading();
    L36:
        handleLoadingMediaPeriodChanged(false);
        this.handler.sendEmptyMessage(2);
        return r10;
    L32:
        if (r3.hasEnabledTracks == false) goto L34;
        r10 = r3.mediaPeriod.seekToUs(r10);
        r3.mediaPeriod.discardBuffer(r10 - this.backBufferDurationUs, this.retainBackBufferFromKeyframe);
        goto L34
    L35:
        this.queue.clear();
        resetRendererPosition(r10);
        goto L36
    L17:
        if (r3.toRendererTime(r10) >= 0) goto L27;
    L18:
        int r4 = 0;
    L20:
        if (r4 >= this.renderers.length) goto L22;
        disableRenderer(r4);
        r4 = r4 + 1;
        goto L20
    L22:
        if (r3 == null) goto L27;
    L24:
        if (this.queue.getPlayingPeriod() == r3) goto L26;
        this.queue.advancePlayingPeriod();
        goto L24
    L26:
        this.queue.removeAfter(r3);
        r3.setRendererOffset(MediaPeriodQueue.INITIAL_RENDERER_POSITION_OFFSET_US);
        enableRenderers();
        goto L27
    L5:
        if (this.playbackInfo.playbackState != 3) goto L7;
        goto L6
    }

    private void sendMessageInternal(PlayerMessage r9) throws ExoPlaybackException {
        if (r9.getPositionMs() != C0180C.TIME_UNSET) goto L6;
        sendMessageToTarget(r9);
        return;
    L6:
        if (this.playbackInfo.timeline.isEmpty() == false) goto L8;
        this.pendingMessages.add(new PendingMessageInfo(r9));
        return;
    L8:
        PendingMessageInfo r0 = new PendingMessageInfo(r9);
        if (resolvePendingMessagePosition(r0, this.playbackInfo.timeline, this.playbackInfo.timeline, this.repeatMode, this.shuffleModeEnabled, this.window, this.period) == false) goto L11;
        this.pendingMessages.add(r0);
        Collections.sort(this.pendingMessages);
        return;
    L11:
        r9.markAsProcessed(false);
    }

    private void sendMessageToTarget(PlayerMessage r4) throws ExoPlaybackException {
        if (r4.getLooper() != this.playbackLooper) goto L9;
        deliverMessage(r4);
        if (this.playbackInfo.playbackState != 3) goto L7;
    L8:
        this.handler.sendEmptyMessage(2);
        return;
    L7:
        if (this.playbackInfo.playbackState == 2) goto L8;
        return;
    L9:
        this.handler.obtainMessage(15, r4).sendToTarget();
    }

    private void sendMessageToTargetThread(final PlayerMessage r4) {
        Looper r0 = r4.getLooper();
        if (r0.getThread().isAlive() == true) goto L6;
        Log.m48w("TAG", "Trying to send message on a dead thread.");
        r4.markAsProcessed(false);
        return;
    L6:
        this.clock.createHandler(r0, null).post(new ExoPlayerImplInternal$$ExternalSyntheticLambda3(this, r4));
    }

    private void setAllRendererStreamsFinal(long r6) {
        Renderer[] r0 = this.renderers;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L8;
        Renderer r3 = r0[r2];
        if (r3.getStream() == null) goto L7;
        setCurrentStreamFinal(r3, r6);
    L7:
        r2 = r2 + 1;
        goto L3
    }

    private void setCurrentStreamFinal(Renderer r2, long r3) {
        r2.setCurrentStreamFinal();
        if ((r2 instanceof TextRenderer) == false) goto L6;
        ((TextRenderer) r2).setFinalStreamEndPositionUs(r3);
        return;
    }

    private void setForegroundModeInternal(boolean r6, AtomicBoolean r7) {
        if (this.foregroundMode == r6) goto L14;
        this.foregroundMode = r6;
        if (r6 == true) goto L14;
        Renderer[] r0 = this.renderers;
        int r1 = r0.length;
        int r2 = 0;
    L7:
        if (r2 >= r1) goto L14;
        Renderer r3 = r0[r2];
        if (isRendererEnabled(r3) == true) goto L13;
        if (this.renderersToReset.remove(r3) == false) goto L13;
        r3.reset();
    L13:
        r2 = r2 + 1;
    L14:
        if (r7 == null) goto L23;
        monitor-enter(this);
        r7.set(true);     // Catch: Throwable -> L20
        notifyAll();     // Catch: Throwable -> L20
        monitor-exit(this);     // Catch: Throwable -> L20
        return;
    L20:
        th = move-exception;
        throw th;
    }

    private void setMediaClockPlaybackParameters(PlaybackParameters r3) {
        this.handler.removeMessages(16);
        this.mediaClock.setPlaybackParameters(r3);
    }

    private void setMediaItemsInternal(MediaSourceListUpdateMessage r6) throws ExoPlaybackException {
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        if (MediaSourceListUpdateMessage.access$200(r6) == (-1)) goto L5;
        this.pendingInitialSeekPosition = new SeekPosition(new PlaylistTimeline(MediaSourceListUpdateMessage.access$300(r6), MediaSourceListUpdateMessage.access$400(r6)), MediaSourceListUpdateMessage.access$200(r6), MediaSourceListUpdateMessage.access$500(r6));
    L5:
        handleMediaSourceListInfoRefreshed(this.mediaSourceList.setMediaSources(MediaSourceListUpdateMessage.access$300(r6), MediaSourceListUpdateMessage.access$400(r6)), false);
    }

    private void setOffloadSchedulingEnabled(boolean r3) {
        if (r3 != this.offloadSchedulingEnabled) goto L5;
        return;
    L5:
        this.offloadSchedulingEnabled = r3;
        if (r3 == false) goto L8;
        return;
    L8:
        if (this.playbackInfo.sleepingForOffload == false) goto L12;
        this.handler.sendEmptyMessage(2);
        return;
    }

    private void setPauseAtEndOfWindowInternal(boolean r3) throws ExoPlaybackException {
        this.pauseAtEndOfWindow = r3;
        resetPendingPauseAtEndOfPeriod();
        if (this.pendingPauseAtEndOfPeriod == true) goto L5;
        return;
    L5:
        if (this.queue.getReadingPeriod() == this.queue.getPlayingPeriod()) goto L9;
        seekToCurrentPosition(true);
        handleLoadingMediaPeriodChanged(false);
        return;
    }

    private void setPlayWhenReadyInternal(boolean r4, int r5, boolean r6, int r7) throws ExoPlaybackException {
        this.playbackInfoUpdate.incrementPendingOperationAcks(r6 ? 1 : 0);
        this.playbackInfo = this.playbackInfo.copyWithPlayWhenReady(r4, r7, r5);
        updateRebufferingState(false, false);
        notifyTrackSelectionPlayWhenReadyChanged(r4);
        if (shouldPlayWhenReady() == true) goto L6;
        stopRenderers();
        updatePlaybackPositions();
        return;
    L6:
        if (this.playbackInfo.playbackState != 3) goto L9;
        this.mediaClock.start();
        startRenderers();
        this.handler.sendEmptyMessage(2);
        return;
    L9:
        if (this.playbackInfo.playbackState != 2) goto L14;
        this.handler.sendEmptyMessage(2);
        return;
    }

    private void setPlaybackParametersInternal(PlaybackParameters r3) throws ExoPlaybackException {
        setMediaClockPlaybackParameters(r3);
        handlePlaybackParameters(this.mediaClock.getPlaybackParameters(), true);
    }

    private void setPreloadConfigurationInternal(ExoPlayer.PreloadConfiguration r3) {
        this.preloadConfiguration = r3;
        this.queue.updatePreloadConfiguration(this.playbackInfo.timeline, r3);
    }

    private void setRepeatModeInternal(int r3) throws ExoPlaybackException {
        this.repeatMode = r3;
        if (this.queue.updateRepeatMode(this.playbackInfo.timeline, r3) == true) goto L5;
        seekToCurrentPosition(true);
    L5:
        handleLoadingMediaPeriodChanged(false);
    }

    private void setSeekParametersInternal(SeekParameters r1) {
        this.seekParameters = r1;
    }

    private void setShuffleModeEnabledInternal(boolean r3) throws ExoPlaybackException {
        this.shuffleModeEnabled = r3;
        if (this.queue.updateShuffleModeEnabled(this.playbackInfo.timeline, r3) == true) goto L5;
        seekToCurrentPosition(true);
    L5:
        handleLoadingMediaPeriodChanged(false);
    }

    private void setShuffleOrderInternal(ShuffleOrder r3) throws ExoPlaybackException {
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        handleMediaSourceListInfoRefreshed(this.mediaSourceList.setShuffleOrder(r3), false);
    }

    private void setState(int r3) {
        if (this.playbackInfo.playbackState != r3) goto L5;
        return;
    L5:
        if (r3 == 2) goto L7;
        this.playbackMaybeBecameStuckAtMs = C0180C.TIME_UNSET;
    L7:
        this.playbackInfo = this.playbackInfo.copyWithPlaybackState(r3);
    }

    private boolean shouldAdvancePlayingPeriod() {
        if (shouldPlayWhenReady() == true) goto L6;
        return false;
    L6:
        if (this.pendingPauseAtEndOfPeriod == false) goto L8;
        return false;
    L8:
        MediaPeriodHolder r0 = this.queue.getPlayingPeriod();
        if (r0 != null) goto L11;
        return false;
    L11:
        MediaPeriodHolder r2 = r0.getNext();
        if (r2 != null) goto L14;
        return false;
    L14:
        if (this.rendererPositionUs >= r2.getStartPositionRendererTime()) goto L16;
        return false;
    L16:
        if (r2.allRenderersInCorrectState == false) goto L22;
        return true;
    L22:
        return false;
    }

    private boolean shouldContinueLoading() {
        if (isLoadingPossible(this.queue.getLoadingPeriod()) == true) goto L5;
        return false;
    L5:
        MediaPeriodHolder r1 = this.queue.getLoadingPeriod();
        long r11 = getTotalBufferedDurationUs(r1.getNextLoadPositionUs());
        if (r1 != this.queue.getPlayingPeriod()) goto L8;
        long r9 = r1.toPeriodTime(this.rendererPositionUs);
    L10:
        if (shouldUseLivePlaybackSpeedControl(this.playbackInfo.timeline, r1.info.f35id) == false) goto L12;
        long r16 = this.livePlaybackSpeedControl.getTargetLiveOffsetUs();
    L13:
        LoadControl.Parameters r5 = new LoadControl.Parameters(this.playerId, this.playbackInfo.timeline, r1.info.f35id, r9, r11, this.mediaClock.getPlaybackParameters().speed, this.playbackInfo.playWhenReady, this.isRebuffering, r16);
        boolean r3 = this.loadControl.shouldContinueLoading(r5);
        MediaPeriodHolder r4 = this.queue.getPlayingPeriod();
        if (r3 == false) goto L16;
        return r3;
    L16:
        if (r4.prepared == true) goto L18;
        return r3;
    L18:
        if (r11 < PLAYBACK_BUFFER_EMPTY_THRESHOLD_US) goto L20;
        return r3;
    L20:
        if (this.backBufferDurationUs <= 0) goto L22;
    L23:
        r4.mediaPeriod.discardBuffer(this.playbackInfo.positionUs, false);
        return this.loadControl.shouldContinueLoading(r5);
    L22:
        if (this.retainBackBufferFromKeyframe == true) goto L23;
        return r3;
    L12:
        r16 = -9223372036854775807L;
        goto L13
    L8:
        r9 = r1.toPeriodTime(this.rendererPositionUs) - r1.info.startPositionUs;
        goto L10
    }

    private boolean shouldPlayWhenReady() {
        if (this.playbackInfo.playWhenReady == true) goto L5;
    L7:
        return false;
    L5:
        if (this.playbackInfo.playbackSuppressionReason != 0) goto L7;
        return true;
    }

    private boolean shouldTransitionToReadyState(boolean r21) {
        if (this.enabledRendererCount == 0) goto L5;
        boolean r1 = false;
        if (r21 == true) goto L10;
        return false;
    L10:
        if (this.playbackInfo.isLoading == true) goto L12;
        return true;
    L12:
        MediaPeriodHolder r2 = this.queue.getPlayingPeriod();
        if (shouldUseLivePlaybackSpeedControl(this.playbackInfo.timeline, r2.info.f35id) == false) goto L15;
        long r4 = this.livePlaybackSpeedControl.getTargetLiveOffsetUs();
    L16:
        long r17 = r4;
        MediaPeriodHolder r5 = this.queue.getLoadingPeriod();
        if (r5.isFullyBuffered() == true) goto L19;
    L21:
        boolean r6 = false;
    L23:
        if (r5.info.f35id.isAd() == true) goto L25;
    L27:
        if (r6 == true) goto L33;
        if (r1 == true) goto L33;
        return this.loadControl.shouldStartPlayback(new LoadControl.Parameters(this.playerId, this.playbackInfo.timeline, r2.info.f35id, r2.toPeriodTime(this.rendererPositionUs), getTotalBufferedDurationUs(r5.getBufferedPositionUs()), this.mediaClock.getPlaybackParameters().speed, this.playbackInfo.playWhenReady, this.isRebuffering, r17));
    L33:
        return true;
    L25:
        if (r5.prepared == true) goto L27;
        r1 = true;
        goto L27
    L19:
        if (r5.info.isFinal == false) goto L21;
        r6 = true;
        goto L23
    L15:
        r4 = C0180C.TIME_UNSET;
        goto L16
    L5:
        return isTimelineReady();
    }

    private boolean shouldUseLivePlaybackSpeedControl(Timeline r7, MediaSource.MediaPeriodId r8) {
        if (r8.isAd() == false) goto L5;
    L15:
        return false;
    L5:
        if (r7.isEmpty() == true) goto L15;
        r7.getWindow(r7.getPeriodByUid(r8.periodUid, this.period).windowIndex, this.window);
        if (this.window.isLive() == true) goto L10;
        return false;
    L10:
        if (this.window.isDynamic == true) goto L12;
        return false;
    L12:
        if (this.window.windowStartTimeMs == C0180C.TIME_UNSET) goto L18;
        return true;
    L18:
        return false;
    }

    private void startRenderers() throws ExoPlaybackException {
        MediaPeriodHolder r0 = this.queue.getPlayingPeriod();
        if (r0 != null) goto L5;
        return;
    L5:
        TrackSelectorResult r1 = r0.getTrackSelectorResult();
        int r2 = 0;
    L7:
        if (r2 >= this.renderers.length) goto L14;
        if (r1.isRendererEnabled(r2) == false) goto L13;
        if (this.renderers[r2].getState() != 1) goto L13;
        this.renderers[r2].start();
    L13:
        r2 = r2 + 1;
        goto L7
    }

    private void stopInternal(boolean r4, boolean r5) {
        if (r4 == false) goto L5;
    L8:
        boolean r2 = true;
    L9:
        resetInternal(r2, false, true, false);
        this.playbackInfoUpdate.incrementPendingOperationAcks(r5 ? 1 : 0);
        this.loadControl.onStopped(this.playerId);
        setState(1);
        return;
    L5:
        if (this.foregroundMode == false) goto L8;
        r2 = false;
        goto L9
    }

    private void stopRenderers() throws ExoPlaybackException {
        this.mediaClock.stop();
        Renderer[] r0 = this.renderers;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L8;
        Renderer r3 = r0[r2];
        if (isRendererEnabled(r3) == false) goto L7;
        ensureStopped(r3);
    L7:
        r2 = r2 + 1;
        goto L3
    }

    private void updateIsLoading() {
        MediaPeriodHolder r0 = this.queue.getLoadingPeriod();
        if (this.shouldContinueLoading == true) goto L9;
        if (r0 != null) goto L6;
    L8:
        boolean r1 = false;
    L11:
        if (r1 == this.playbackInfo.isLoading) goto L14;
        this.playbackInfo = this.playbackInfo.copyWithIsLoading(r1);
        return;
    L14:
        return;
    L6:
        if (r0.mediaPeriod.isLoading() == false) goto L8;
    L9:
        r1 = true;
        goto L11
    }

    private void updateLoadControlTrackSelection(MediaSource.MediaPeriodId r18, TrackGroupArray r19, TrackSelectorResult r20) {
        MediaPeriodHolder r1 = (MediaPeriodHolder) Assertions.checkNotNull(this.queue.getLoadingPeriod());
        if (r1 != this.queue.getPlayingPeriod()) goto L5;
        long r2 = r1.toPeriodTime(this.rendererPositionUs);
    L6:
        long r8 = r2;
        long r10 = getTotalBufferedDurationUs(r1.getBufferedPositionUs());
        if (shouldUseLivePlaybackSpeedControl(this.playbackInfo.timeline, r1.info.f35id) == false) goto L9;
        long r15 = this.livePlaybackSpeedControl.getTargetLiveOffsetUs();
    L10:
        this.loadControl.onTracksSelected(new LoadControl.Parameters(this.playerId, this.playbackInfo.timeline, r18, r8, r10, this.mediaClock.getPlaybackParameters().speed, this.playbackInfo.playWhenReady, this.isRebuffering, r15), r19, r20.selections);
        return;
    L9:
        r15 = -9223372036854775807L;
        goto L10
    L5:
        r2 = r1.toPeriodTime(this.rendererPositionUs) - r1.info.startPositionUs;
        goto L6
    }

    private void updateMediaSourcesWithMediaItemsInternal(int r3, int r4, List<MediaItem> r5) throws ExoPlaybackException {
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        handleMediaSourceListInfoRefreshed(this.mediaSourceList.updateMediaSourcesWithMediaItems(r3, r4, r5), false);
    }

    private void updatePeriods() throws ExoPlaybackException {
        if (this.playbackInfo.timeline.isEmpty() == false) goto L5;
        return;
    L5:
        if (this.mediaSourceList.isPrepared() == false) goto L10;
        boolean r0 = maybeUpdateLoadingPeriod();
        maybeUpdateReadingPeriod();
        maybeUpdateReadingRenderers();
        maybeUpdatePlayingPeriod();
        maybeUpdatePreloadPeriods(r0);
        return;
    }

    private void updatePlaybackPositions() throws ExoPlaybackException {
        MediaPeriodHolder r10 = this.queue.getPlayingPeriod();
        if (r10 != null) goto L6;
        return;
    L6:
        if (r10.prepared == false) goto L8;
        long r4 = r10.mediaPeriod.readDiscontinuity();
    L10:
        if (r4 != C0180C.TIME_UNSET) goto L12;
        DefaultMediaClock r1 = this.mediaClock;
        if (r10 == this.queue.getReadingPeriod()) goto L21;
        boolean r2 = true;
    L22:
        this.rendererPositionUs = r1.syncAndGetPositionUs(r2);
        long r3 = r10.toPeriodTime(this.rendererPositionUs);
        maybeTriggerPendingMessages(this.playbackInfo.positionUs, r3);
        if (this.mediaClock.hasSkippedSilenceSinceLastCall() == false) goto L25;
        this.playbackInfo = handlePositionDiscontinuity(this.playbackInfo.periodId, r3, this.playbackInfo.requestedContentPositionUs, r3, !this.playbackInfoUpdate.positionDiscontinuity, 6);
    L26:
        MediaPeriodHolder r5 = this.queue.getLoadingPeriod();
        this.playbackInfo.bufferedPositionUs = r5.getBufferedPositionUs();
        this.playbackInfo.totalBufferedDurationUs = getTotalBufferedDurationUs();
        if (this.playbackInfo.playWhenReady == true) goto L29;
        return;
    L29:
        if (this.playbackInfo.playbackState == 3) goto L31;
        return;
    L31:
        if (shouldUseLivePlaybackSpeedControl(this.playbackInfo.timeline, this.playbackInfo.periodId) == true) goto L33;
        return;
    L33:
        if (this.playbackInfo.playbackParameters.speed != 1.0f) goto L41;
        float r6 = this.livePlaybackSpeedControl.getAdjustedPlaybackSpeed(getCurrentLiveOffsetUs(), this.playbackInfo.totalBufferedDurationUs);
        if (this.mediaClock.getPlaybackParameters().speed == r6) goto L42;
        setMediaClockPlaybackParameters(this.playbackInfo.playbackParameters.withSpeed(r6));
        handlePlaybackParameters(this.playbackInfo.playbackParameters, this.mediaClock.getPlaybackParameters().speed, false, false);
        return;
    L42:
        return;
    L41:
        return;
    L25:
        this.playbackInfo.updatePositionUs(r3);
        goto L26
    L21:
        r2 = false;
        goto L22
    L12:
        if (r10.isFullyBuffered() == true) goto L14;
        this.queue.removeAfter(r10);
        handleLoadingMediaPeriodChanged(false);
        maybeContinueLoading();
    L14:
        resetRendererPosition(r4);
        if (r4 == this.playbackInfo.positionUs) goto L26;
        this.playbackInfo = handlePositionDiscontinuity(this.playbackInfo.periodId, r4, this.playbackInfo.requestedContentPositionUs, r4, true, 5);
        goto L26
    L8:
        r4 = -9223372036854775807L;
        goto L10
    }

    private void updatePlaybackSpeedSettingsForNewPeriod(Timeline r8, MediaSource.MediaPeriodId r9, Timeline r10, MediaSource.MediaPeriodId r11, long r12, boolean r14) throws ExoPlaybackException {
        if (shouldUseLivePlaybackSpeedControl(r8, r9) == false) goto L5;
        r8.getWindow(r8.getPeriodByUid(r9.periodUid, this.period).windowIndex, this.window);
        this.livePlaybackSpeedControl.setLiveConfiguration((MediaItem.LiveConfiguration) Util.castNonNull(this.window.liveConfiguration));
        if (r12 == C0180C.TIME_UNSET) goto L15;
        this.livePlaybackSpeedControl.setTargetLiveOffsetOverrideUs(getLiveOffsetUs(r8, r9.periodUid, r12));
        return;
    L15:
        Object r3 = this.window.uid;
        Object r4 = null;
        if (r10.isEmpty() == true) goto L19;
        r4 = r10.getWindow(r10.getPeriodByUid(r11.periodUid, this.period).windowIndex, this.window).uid;
    L19:
        if (Util.areEqual(r4, r3) == false) goto L21;
        if (r14 == true) goto L21;
        return;
    L21:
        this.livePlaybackSpeedControl.setTargetLiveOffsetOverrideUs(C0180C.TIME_UNSET);
        return;
    L5:
        if (r9.isAd() == false) goto L7;
        PlaybackParameters r0 = PlaybackParameters.DEFAULT;
    L9:
        if (this.mediaClock.getPlaybackParameters().equals(r0) == true) goto L23;
        setMediaClockPlaybackParameters(r0);
        handlePlaybackParameters(this.playbackInfo.playbackParameters, r0.speed, false, false);
        return;
    L23:
        return;
    L7:
        r0 = this.playbackInfo.playbackParameters;
        goto L9
    }

    private void updateRebufferingState(boolean r3, boolean r4) {
        this.isRebuffering = r3;
        if (r3 == false) goto L6;
        if (r4 == true) goto L6;
        long r0 = this.clock.elapsedRealtime();
    L7:
        this.lastRebufferRealtimeMs = r0;
        return;
    L6:
        r0 = C0180C.TIME_UNSET;
        goto L7
    }

    private void updateTrackSelectionPlaybackSpeed(float r6) {
        MediaPeriodHolder r0 = this.queue.getPlayingPeriod();
    L3:
        if (r0 == null) goto L11;
        ExoTrackSelection[] r1 = r0.getTrackSelectorResult().selections;
        int r2 = r1.length;
        int r3 = 0;
    L5:
        if (r3 >= r2) goto L10;
        ExoTrackSelection r4 = r1[r3];
        if (r4 == null) goto L9;
        r4.onPlaybackSpeed(r6);
    L9:
        r3 = r3 + 1;
        goto L5
    L10:
        r0 = r0.getNext();
        goto L3
    }

    private synchronized void waitUninterruptibly(Supplier<Boolean> r8, long r9) {
        monitor-enter(this);
        long r0 = this.clock.elapsedRealtime() + r9;     // Catch: Throwable -> L17
        long r2 = r9;
        boolean r4 = false;
    L5:
        if (r8.get().booleanValue() == true) goto L13;
        if (r2 <= 0) goto L13;
        this.clock.onThreadBlocked();     // Catch: InterruptedException -> L10 Throwable -> L17
        wait(r2);     // Catch: InterruptedException -> L10 Throwable -> L17
    L12:
        r2 = r0 - this.clock.elapsedRealtime();     // Catch: Throwable -> L17
    L11:
        r4 = true;
    L13:
        if (r4 == false) goto L15;
        Thread.currentThread().interrupt();     // Catch: Throwable -> L17
    L15:
        monitor-exit(this);
        return;
    L17:
        th = move-exception;
        throw th;
    }

    public void addMediaSources(int r9, List<MediaSourceList.MediaSourceHolder> r10, ShuffleOrder r11) {
        int r4 = -1;
        this.handler.obtainMessage(18, r9, 0, new MediaSourceListUpdateMessage(r10, r11, r4, C0180C.TIME_UNSET, null)).sendToTarget();
    }

    public void experimentalSetForegroundModeTimeoutMs(long r1) {
        this.setForegroundModeTimeoutMs = r1;
    }

    public Looper getPlaybackLooper() {
        return this.playbackLooper;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message r16) {
    L67:
        e = move-exception;
        if (e.dataType != 1) goto L76;
        if (e.contentIsMalformed == false) goto L73;
        int r2 = PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED;
    L83:
        handleIoException(e, r2);
    L118:
        maybeNotifyPlaybackInfoChanged();
        return true;
    L73:
        r2 = PlaybackException.ERROR_CODE_PARSING_CONTAINER_UNSUPPORTED;
        goto L83
    L76:
        if (e.dataType == 4) goto L78;
        r2 = 1000;
        goto L83
    L78:
        if (e.contentIsMalformed == false) goto L80;
        r2 = PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED;
        goto L83
    L80:
        r2 = PlaybackException.ERROR_CODE_PARSING_MANIFEST_UNSUPPORTED;
    L65:
        e = move-exception;
        handleIoException(e, e.reason);
    L87:
        e = e;
        if (e.type != 1) goto L94;
        MediaPeriodHolder r4 = this.queue.getReadingPeriod();
        if (r4 == null) goto L94;
        e = e.copyWithMediaPeriodId(r4.info.f35id);
    L94:
        if (e.isRecoverable == false) goto L107;
        if (this.pendingRecoverableRendererError != null) goto L98;
    L101:
        Log.m49w(TAG, "Recoverable renderer error", e);
        if (this.pendingRecoverableRendererError == null) goto L104;
        this.pendingRecoverableRendererError.addSuppressed(e);
        e = this.pendingRecoverableRendererError;
    L105:
        this.handler.sendMessageAtFrontOfQueue(this.handler.obtainMessage(25, e));
        goto L118
    L104:
        this.pendingRecoverableRendererError = e;
        goto L105
    L98:
        if (e.errorCode == 5004) goto L101;
        if (e.errorCode == 5003) goto L101;
    L107:
        if (this.pendingRecoverableRendererError == null) goto L109;
        this.pendingRecoverableRendererError.addSuppressed(e);
        e = this.pendingRecoverableRendererError;
    L109:
        Log.m45e(TAG, "Playback error", e);
        if (e.type == 1) goto L112;
    L117:
        stopInternal(true, false);
        this.playbackInfo = this.playbackInfo.copyWithPlaybackError(e);
        goto L118
    L112:
        if (this.queue.getPlayingPeriod() == this.queue.getReadingPeriod()) goto L117;
    L114:
        if (this.queue.getPlayingPeriod() == this.queue.getReadingPeriod()) goto L116;
        this.queue.advancePlayingPeriod();
        goto L114
    L116:
        MediaPeriodHolder r14 = (MediaPeriodHolder) Assertions.checkNotNull(this.queue.getPlayingPeriod());
        maybeNotifyPlaybackInfoChanged();
        this.playbackInfo = handlePositionDiscontinuity(r14.info.f35id, r14.info.startPositionUs, r14.info.requestedContentPositionUs, r14.info.startPositionUs, true, 0);
    L84:
        e = move-exception;
        handleIoException(e, e.errorCode);
    L63:
        e = move-exception;
        handleIoException(e, 1002);
    L61:
        e = move-exception;
        handleIoException(e, 2000);
    L52:
        e = move-exception;
        if ((e instanceof IllegalStateException) == false) goto L56;
    L59:
        int r5 = 1004;
    L60:
        ExoPlaybackException r6 = ExoPlaybackException.createForUnexpected(e, r5);
        Log.m45e(TAG, "Playback error", r6);
        stopInternal(true, false);
        this.playbackInfo = this.playbackInfo.copyWithPlaybackError(r6);
        goto L118
    L56:
        if ((e instanceof IllegalArgumentException) == true) goto L59;
        r5 = 1000;
        goto L60
    L4:
        switch(r16.what) {
            case 1: goto L47;
            case 2: goto L45;
            case 3: goto L44;
            case 4: goto L43;
            case 5: goto L42;
            case 6: goto L41;
            case 7: goto L39;
            case 8: goto L38;
            case 9: goto L37;
            case 10: goto L36;
            case 11: goto L35;
            case 12: goto L31;
            case 13: goto L26;
            case 14: goto L24;
            case 15: goto L23;
            case 16: goto L22;
            case 17: goto L21;
            case 18: goto L20;
            case 19: goto L19;
            case 20: goto L18;
            case 21: goto L17;
            case 22: goto L16;
            case 23: goto L12;
            case 24: goto L5;
            case 25: goto L10;
            case 26: goto L9;
            case 27: goto L8;
            case 28: goto L7;
            case 29: goto L6;
            default: goto L5;
        };     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
    L5:
        return false;
    L6:
        prepareInternal();     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L7:
        setPreloadConfigurationInternal((ExoPlayer.PreloadConfiguration) r16.obj);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L8:
        updateMediaSourcesWithMediaItemsInternal(r16.arg1, r16.arg2, (List) r16.obj);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L9:
        reselectTracksInternalAndSeek();     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L10:
        attemptRendererErrorRecovery();     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L16:
        mediaSourceListUpdateRequestedInternal();     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L17:
        setShuffleOrderInternal((ShuffleOrder) r16.obj);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L18:
        removeMediaItemsInternal(r16.arg1, r16.arg2, (ShuffleOrder) r16.obj);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L19:
        moveMediaItemsInternal((MoveMediaItemsMessage) r16.obj);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L20:
        addMediaItemsInternal((MediaSourceListUpdateMessage) r16.obj, r16.arg1);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L21:
        setMediaItemsInternal((MediaSourceListUpdateMessage) r16.obj);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L22:
        handlePlaybackParameters((PlaybackParameters) r16.obj, false);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L23:
        sendMessageToTargetThread((PlayerMessage) r16.obj);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L24:
        sendMessageInternal((PlayerMessage) r16.obj);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L35:
        setRepeatModeInternal(r16.arg1);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L36:
        reselectTracksInternal();     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L37:
        handleContinueLoadingRequested((MediaPeriod) r16.obj);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L38:
        handlePeriodPrepared((MediaPeriod) r16.obj);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L39:
        releaseInternal();     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        return true;
    L41:
        stopInternal(false, true);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L42:
        setSeekParametersInternal((SeekParameters) r16.obj);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L43:
        setPlaybackParametersInternal((PlaybackParameters) r16.obj);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L44:
        seekToInternal((SeekPosition) r16.obj);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L45:
        doSomeWork();     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L12:
        if (r16.arg1 == 0) goto L14;
        boolean r0 = true;
    L15:
        setPauseAtEndOfWindowInternal(r0);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L14:
        r0 = false;
        goto L15
    L26:
        if (r16.arg1 == 0) goto L28;
        boolean r1 = true;
    L29:
        setForegroundModeInternal(r1, (AtomicBoolean) r16.obj);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L28:
        r1 = false;
        goto L29
    L31:
        if (r16.arg1 == 0) goto L33;
        boolean r3 = true;
    L34:
        setShuffleModeEnabledInternal(r3);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L33:
        r3 = false;
        goto L34
    L47:
        if (r16.arg1 == 0) goto L49;
        boolean r7 = true;
    L50:
        setPlayWhenReadyInternal(r7, r16.arg2 >> 4, true, r16.arg2 & 15);     // Catch: RuntimeException -> L52 IOException -> L61 BehindLiveWindowException -> L63 DataSourceException -> L65 ParserException -> L67 DrmSession.DrmSessionException -> L84 ExoPlaybackException -> L87
        goto L118
    L49:
        r7 = false;
        goto L50
    }

    /* JADX INFO: renamed from: lambda$maybeTriggerOnRendererReadyChanged$1$androidx-media3-exoplayer-ExoPlayerImplInternal */
    /* synthetic */ void m62x6a39ddd9(int r3, boolean r4) {
        this.analyticsCollector.onRendererReadyChanged(r3, this.renderers[r3].getTrackType(), r4);
    }

    /* JADX INFO: renamed from: lambda$release$0$androidx-media3-exoplayer-ExoPlayerImplInternal, reason: not valid java name */
    /* synthetic */ Boolean m470lambda$release$0$androidxmedia3exoplayerExoPlayerImplInternal() {
        return Boolean.valueOf(this.released);
    }

    /* JADX INFO: renamed from: lambda$sendMessageToTargetThread$2$androidx-media3-exoplayer-ExoPlayerImplInternal */
    /* synthetic */ void m63x7ddc96d2(PlayerMessage r4) {
        deliverMessage(r4);     // Catch: ExoPlaybackException -> L4
        return;
    L4:
        e = move-exception;
        Log.m45e(TAG, "Unexpected error delivering message on external thread.", e);
        throw new RuntimeException(e);
    }

    public void moveMediaSources(int r4, int r5, int r6, ShuffleOrder r7) {
        MoveMediaItemsMessage r0 = new MoveMediaItemsMessage(r4, r5, r6, r7);
        this.handler.obtainMessage(19, r0).sendToTarget();
    }

    public void onContinueLoadingRequested(MediaPeriod r3) {
        this.handler.obtainMessage(9, r3).sendToTarget();
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader.Callback
    public /* bridge */ /* synthetic */ void onContinueLoadingRequested(SequenceableLoader r1) {
        onContinueLoadingRequested((MediaPeriod) r1);
    }

    @Override // androidx.media3.exoplayer.DefaultMediaClock.PlaybackParametersListener
    public void onPlaybackParametersChanged(PlaybackParameters r3) {
        this.handler.obtainMessage(16, r3).sendToTarget();
    }

    @Override // androidx.media3.exoplayer.MediaSourceList.MediaSourceListInfoRefreshListener
    public void onPlaylistUpdateRequested() {
        this.handler.removeMessages(2);
        this.handler.sendEmptyMessage(22);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod.Callback
    public void onPrepared(MediaPeriod r3) {
        this.handler.obtainMessage(8, r3).sendToTarget();
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelector.InvalidationListener
    public void onRendererCapabilitiesChanged(Renderer r3) {
        this.handler.sendEmptyMessage(26);
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelector.InvalidationListener
    public void onTrackSelectionsInvalidated() {
        this.handler.sendEmptyMessage(10);
    }

    public void prepare() {
        this.handler.obtainMessage(29).sendToTarget();
    }

    public synchronized boolean release() {
        monitor-enter(this);
    L14:
        th = move-exception;
        throw th;
    L4:
        if (this.released == false) goto L6;
    L11:
        monitor-exit(this);
        return true;
    L6:
        if (this.playbackLooper.getThread().isAlive() == false) goto L11;
        this.handler.sendEmptyMessage(7);     // Catch: Throwable -> L14
        waitUninterruptibly(new ExoPlayerImplInternal$$ExternalSyntheticLambda0(this), this.releaseTimeoutMs);     // Catch: Throwable -> L14
        boolean r0 = this.released;     // Catch: Throwable -> L14
        monitor-exit(this);
        return r0;
    }

    public void removeMediaSources(int r3, int r4, ShuffleOrder r5) {
        this.handler.obtainMessage(20, r3, r4, r5).sendToTarget();
    }

    public void seekTo(Timeline r4, int r5, long r6) {
        this.handler.obtainMessage(3, new SeekPosition(r4, r5, r6)).sendToTarget();
    }

    @Override // androidx.media3.exoplayer.PlayerMessage.Sender
    public synchronized void sendMessage(PlayerMessage r3) {
        monitor-enter(this);
    L14:
        th = move-exception;
        throw th;
    L4:
        if (this.released == false) goto L6;
    L11:
        Log.m48w(TAG, "Ignoring messages sent after release.");     // Catch: Throwable -> L14
        r3.markAsProcessed(false);     // Catch: Throwable -> L14
        monitor-exit(this);
        return;
    L6:
        if (this.playbackLooper.getThread().isAlive() == false) goto L11;
        this.handler.obtainMessage(14, r3).sendToTarget();     // Catch: Throwable -> L14
        monitor-exit(this);
    }

    public synchronized boolean setForegroundMode(boolean r5) {
        monitor-enter(this);
    L18:
        th = move-exception;
        throw th;
    L4:
        if (this.released == false) goto L6;
    L16:
        monitor-exit(this);
        return true;
    L6:
        if (this.playbackLooper.getThread().isAlive() == false) goto L16;
        if (r5 == false) goto L13;
        this.handler.obtainMessage(13, 1, 0).sendToTarget();     // Catch: Throwable -> L18
        monitor-exit(this);
        return true;
    L13:
        final AtomicBoolean r1 = new AtomicBoolean();     // Catch: Throwable -> L18
        this.handler.obtainMessage(13, 0, 0, r1).sendToTarget();     // Catch: Throwable -> L18
        Objects.requireNonNull(r1);     // Catch: Throwable -> L18
        waitUninterruptibly(new ExoPlayerImplInternal$$ExternalSyntheticLambda4(r1), this.setForegroundModeTimeoutMs);     // Catch: Throwable -> L18
        boolean r0 = r1.get();     // Catch: Throwable -> L18
        monitor-exit(this);
        return r0;
    }

    public void setMediaSources(List<MediaSourceList.MediaSourceHolder> r9, int r10, long r11, ShuffleOrder r13) {
        this.handler.obtainMessage(17, new MediaSourceListUpdateMessage(r9, r13, r10, r11, null)).sendToTarget();
    }

    public void setPauseAtEndOfWindow(boolean r4) {
        this.handler.obtainMessage(23, r4 ? 1 : 0, 0).sendToTarget();
    }

    public void setPlayWhenReady(boolean r4, int r5, int r6) {
        HandlerWrapper r1 = this.handler;
        int r7 = r4 ? 1 : 0;
        r1.obtainMessage(1, r7, (r6 << 4) | r5).sendToTarget();
    }

    public void setPlaybackParameters(PlaybackParameters r3) {
        this.handler.obtainMessage(4, r3).sendToTarget();
    }

    public void setPreloadConfiguration(ExoPlayer.PreloadConfiguration r3) {
        this.handler.obtainMessage(28, r3).sendToTarget();
    }

    public void setRepeatMode(int r4) {
        this.handler.obtainMessage(11, r4, 0).sendToTarget();
    }

    public void setSeekParameters(SeekParameters r3) {
        this.handler.obtainMessage(5, r3).sendToTarget();
    }

    public void setShuffleModeEnabled(boolean r4) {
        this.handler.obtainMessage(12, r4 ? 1 : 0, 0).sendToTarget();
    }

    public void setShuffleOrder(ShuffleOrder r3) {
        this.handler.obtainMessage(21, r3).sendToTarget();
    }

    public void stop() {
        this.handler.obtainMessage(6).sendToTarget();
    }

    public void updateMediaSourcesWithMediaItems(int r3, int r4, List<MediaItem> r5) {
        this.handler.obtainMessage(27, r3, r4, r5).sendToTarget();
    }
}
