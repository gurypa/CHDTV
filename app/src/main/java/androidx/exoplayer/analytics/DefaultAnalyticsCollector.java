package androidx.media3.exoplayer.analytics;

import android.os.Looper;
import android.util.SparseArray;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.DeviceInfo;
import androidx.media3.common.FlagSet;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.Tracks;
import androidx.media3.common.VideoSize;
import androidx.media3.common.text.Cue;
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.HandlerWrapper;
import androidx.media3.common.util.ListenerSet;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.DecoderReuseEvaluation;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.audio.AudioSink;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MediaLoadData;
import androidx.media3.exoplayer.source.MediaSource;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.util.List;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public class DefaultAnalyticsCollector implements AnalyticsCollector {
    private final Clock clock;
    private final SparseArray<AnalyticsListener.EventTime> eventTimes;
    private HandlerWrapper handler;
    private boolean isSeeking;
    private ListenerSet<AnalyticsListener> listeners;
    private final MediaPeriodQueueTracker mediaPeriodQueueTracker;
    private final Timeline.Period period;
    private Player player;
    private final Timeline.Window window;

    private static final class MediaPeriodQueueTracker {
        private MediaSource.MediaPeriodId currentPlayerMediaPeriod;
        private ImmutableList<MediaSource.MediaPeriodId> mediaPeriodQueue;
        private ImmutableMap<MediaSource.MediaPeriodId, Timeline> mediaPeriodTimelines;
        private final Timeline.Period period;
        private MediaSource.MediaPeriodId playingMediaPeriod;
        private MediaSource.MediaPeriodId readingMediaPeriod;

        public MediaPeriodQueueTracker(Timeline.Period r2) {
            this.period = r2;
            this.mediaPeriodQueue = ImmutableList.m212of();
            this.mediaPeriodTimelines = ImmutableMap.m231of();
        }

        static /* synthetic */ ImmutableList access$000(MediaPeriodQueueTracker r1) {
            return r1.mediaPeriodQueue;
        }

        private void addTimelineForMediaPeriodId(ImmutableMap.Builder<MediaSource.MediaPeriodId, Timeline> r3, MediaSource.MediaPeriodId r4, Timeline r5) {
            if (r4 != null) goto L5;
            return;
        L5:
            if (r5.getIndexOfPeriod(r4.periodUid) == (-1)) goto L7;
            r3.put(r4, r5);
            return;
        L7:
            Timeline r0 = this.mediaPeriodTimelines.get(r4);
            if (r0 == null) goto L12;
            r3.put(r4, r0);
            return;
        }

        private static MediaSource.MediaPeriodId findCurrentPlayerMediaPeriodInQueue(Player r10, ImmutableList<MediaSource.MediaPeriodId> r11, MediaSource.MediaPeriodId r12, Timeline.Period r13) {
            Timeline r0 = r10.getCurrentTimeline();
            int r1 = r10.getCurrentPeriodIndex();
            if (r0.isEmpty() == false) goto L5;
            Object r2 = null;
        L6:
            Object r5 = r2;
            if (r10.isPlayingAd() == false) goto L9;
        L12:
            int r9 = -1;
        L13:
            int r3 = 0;
        L15:
            if (r3 >= r11.size()) goto L21;
            MediaSource.MediaPeriodId r4 = r11.get(r3);
            if (isMatchingMediaPeriod(r4, r5, r10.isPlayingAd(), r10.getCurrentAdGroupIndex(), r10.getCurrentAdIndexInAdGroup(), r9) == true) goto L18;
            r3 = r3 + 1;
            goto L15
        L18:
            return r4;
        L21:
            if (r11.isEmpty() == false) goto L27;
            if (r12 == null) goto L27;
            if (isMatchingMediaPeriod(r12, r5, r10.isPlayingAd(), r10.getCurrentAdGroupIndex(), r10.getCurrentAdIndexInAdGroup(), r9) == false) goto L27;
            return r12;
        L27:
            return null;
        L9:
            if (r0.isEmpty() == true) goto L12;
            r9 = r0.getPeriod(r1, r13).getAdGroupIndexAfterPositionUs(Util.msToUs(r10.getCurrentPosition()) - r13.getPositionInWindowUs());
            goto L13
        L5:
            r2 = r0.getUidOfPeriod(r1);
            goto L6
        }

        private static boolean isMatchingMediaPeriod(MediaSource.MediaPeriodId r3, Object r4, boolean r5, int r6, int r7, int r8) {
            if (r3.periodUid.equals(r4) == true) goto L5;
            return false;
        L5:
            if (r5 == true) goto L7;
        L10:
            if (r5 == false) goto L12;
            return false;
        L12:
            if (r3.adGroupIndex == (-1)) goto L14;
            return false;
        L14:
            if (r3.nextAdGroupIndex != r8) goto L19;
        L15:
            return true;
        L19:
            return false;
        L7:
            if (r3.adGroupIndex != r6) goto L10;
            if (r3.adIndexInAdGroup == r7) goto L15;
            goto L10
        }

        private void updateMediaPeriodTimelines(Timeline r4) {
            ImmutableMap.Builder<MediaSource.MediaPeriodId, Timeline> r0 = ImmutableMap.builder();
            if (this.mediaPeriodQueue.isEmpty() == false) goto L12;
            addTimelineForMediaPeriodId(r0, this.playingMediaPeriod, r4);
            if (Objects.equal(this.readingMediaPeriod, this.playingMediaPeriod) == true) goto L8;
            addTimelineForMediaPeriodId(r0, this.readingMediaPeriod, r4);
        L8:
            if (Objects.equal(this.currentPlayerMediaPeriod, this.playingMediaPeriod) == false) goto L10;
        L19:
            this.mediaPeriodTimelines = r0.buildOrThrow();
            return;
        L10:
            if (Objects.equal(this.currentPlayerMediaPeriod, this.readingMediaPeriod) == true) goto L19;
            addTimelineForMediaPeriodId(r0, this.currentPlayerMediaPeriod, r4);
            goto L19
        L12:
            int r1 = 0;
        L14:
            if (r1 >= this.mediaPeriodQueue.size()) goto L17;
            addTimelineForMediaPeriodId(r0, this.mediaPeriodQueue.get(r1), r4);
            r1 = r1 + 1;
            goto L14
        L17:
            if (this.mediaPeriodQueue.contains(this.currentPlayerMediaPeriod) == true) goto L19;
            addTimelineForMediaPeriodId(r0, this.currentPlayerMediaPeriod, r4);
            goto L19
        }

        public MediaSource.MediaPeriodId getCurrentPlayerMediaPeriod() {
            return this.currentPlayerMediaPeriod;
        }

        public MediaSource.MediaPeriodId getLoadingMediaPeriod() {
            if (this.mediaPeriodQueue.isEmpty() == false) goto L6;
            return null;
        L6:
            return (MediaSource.MediaPeriodId) Iterables.getLast(this.mediaPeriodQueue);
        }

        public Timeline getMediaPeriodIdTimeline(MediaSource.MediaPeriodId r2) {
            return this.mediaPeriodTimelines.get(r2);
        }

        public MediaSource.MediaPeriodId getPlayingMediaPeriod() {
            return this.playingMediaPeriod;
        }

        public MediaSource.MediaPeriodId getReadingMediaPeriod() {
            return this.readingMediaPeriod;
        }

        public void onPositionDiscontinuity(Player r4) {
            this.currentPlayerMediaPeriod = findCurrentPlayerMediaPeriodInQueue(r4, this.mediaPeriodQueue, this.playingMediaPeriod, this.period);
        }

        public void onQueueUpdated(List<MediaSource.MediaPeriodId> r4, MediaSource.MediaPeriodId r5, Player r6) {
            this.mediaPeriodQueue = ImmutableList.copyOf(r4);
            if (r4.isEmpty() == true) goto L6;
            this.playingMediaPeriod = r4.get(0);
            this.readingMediaPeriod = (MediaSource.MediaPeriodId) Assertions.checkNotNull(r5);
        L6:
            if (this.currentPlayerMediaPeriod != null) goto L8;
            this.currentPlayerMediaPeriod = findCurrentPlayerMediaPeriodInQueue(r6, this.mediaPeriodQueue, this.playingMediaPeriod, this.period);
        L8:
            updateMediaPeriodTimelines(r6.getCurrentTimeline());
        }

        public void onTimelineChanged(Player r4) {
            this.currentPlayerMediaPeriod = findCurrentPlayerMediaPeriodInQueue(r4, this.mediaPeriodQueue, this.playingMediaPeriod, this.period);
            updateMediaPeriodTimelines(r4.getCurrentTimeline());
        }
    }

    public static /* synthetic */ void $r8$lambda$beemuUlrcRdYmQiMx95bEndNv9Q(DefaultAnalyticsCollector r0) {
        r0.releaseInternal();
    }

    public DefaultAnalyticsCollector(Clock r4) {
        this.clock = (Clock) Assertions.checkNotNull(r4);
        this.listeners = new ListenerSet(Util.getCurrentOrMainLooper(), r4, new DefaultAnalyticsCollector$$ExternalSyntheticLambda32());
        this.period = new Timeline.Period();
        this.window = new Timeline.Window();
        this.mediaPeriodQueueTracker = new MediaPeriodQueueTracker(this.period);
        this.eventTimes = new SparseArray();
    }

    private AnalyticsListener.EventTime generateEventTime(MediaSource.MediaPeriodId r7) {
        Assertions.checkNotNull(this.player);
        if (r7 != null) goto L5;
        Timeline r1 = null;
    L7:
        if (r7 == null) goto L12;
        if (r1 == null) goto L12;
        return generateEventTime(r1, r1.getPeriodByUid(r7.periodUid, this.period).windowIndex, r7);
    L12:
        int r2 = this.player.getCurrentMediaItemIndex();
        Timeline r3 = this.player.getCurrentTimeline();
        if (r2 >= r3.getWindowCount()) goto L15;
        boolean r4 = true;
    L17:
        if (r4 == false) goto L19;
        Timeline r5 = r3;
    L21:
        return generateEventTime(r5, r2, null);
    L19:
        r5 = Timeline.EMPTY;
        goto L21
    L15:
        r4 = false;
        goto L17
    L5:
        r1 = this.mediaPeriodQueueTracker.getMediaPeriodIdTimeline(r7);
        goto L7
    }

    private AnalyticsListener.EventTime generateLoadingMediaPeriodEventTime() {
        return generateEventTime(this.mediaPeriodQueueTracker.getLoadingMediaPeriod());
    }

    private AnalyticsListener.EventTime generateMediaPeriodEventTime(int r5, MediaSource.MediaPeriodId r6) {
        Assertions.checkNotNull(this.player);
        boolean r0 = true;
        if (r6 != null) goto L5;
        Timeline r2 = this.player.getCurrentTimeline();
        if (r5 < r2.getWindowCount()) goto L17;
        r0 = false;
    L17:
        if (r0 == false) goto L19;
        Timeline r1 = r2;
    L21:
        return generateEventTime(r1, r5, null);
    L19:
        r1 = Timeline.EMPTY;
        goto L21
    L5:
        if (this.mediaPeriodQueueTracker.getMediaPeriodIdTimeline(r6) != null) goto L8;
        r0 = false;
    L8:
        if (r0 == false) goto L11;
        return generateEventTime(r6);
    L11:
        return generateEventTime(Timeline.EMPTY, r5, r6);
    }

    private AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime() {
        return generateEventTime(this.mediaPeriodQueueTracker.getPlayingMediaPeriod());
    }

    private AnalyticsListener.EventTime generateReadingMediaPeriodEventTime() {
        return generateEventTime(this.mediaPeriodQueueTracker.getReadingMediaPeriod());
    }

    private AnalyticsListener.EventTime getEventTimeForErrorEvent(PlaybackException r3) {
        if ((r3 instanceof ExoPlaybackException) == false) goto L9;
        ExoPlaybackException r0 = (ExoPlaybackException) r3;
        if (r0.mediaPeriodId == null) goto L9;
        return generateEventTime(r0.mediaPeriodId);
    L9:
        return generateCurrentPlayerMediaPeriodEventTime();
    }

    static /* synthetic */ void lambda$new$0(AnalyticsListener r0, FlagSet r1) {
    }

    static /* synthetic */ void lambda$notifySeekStarted$2(AnalyticsListener.EventTime r0, AnalyticsListener r1) {
        r1.onSeekStarted(r0);
    }

    static /* synthetic */ void lambda$onAudioAttributesChanged$58(AnalyticsListener.EventTime r0, AudioAttributes r1, AnalyticsListener r2) {
        r2.onAudioAttributesChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onAudioCodecError$12(AnalyticsListener.EventTime r0, Exception r1, AnalyticsListener r2) {
        r2.onAudioCodecError(r0, r1);
    }

    static /* synthetic */ void lambda$onAudioDecoderInitialized$5(AnalyticsListener.EventTime r3, String r4, long r5, long r7, AnalyticsListener r9) {
        r9.onAudioDecoderInitialized(r3, r4, r5);
        r9.onAudioDecoderInitialized(r3, r4, r7, r5);
    }

    static /* synthetic */ void lambda$onAudioDecoderReleased$9(AnalyticsListener.EventTime r0, String r1, AnalyticsListener r2) {
        r2.onAudioDecoderReleased(r0, r1);
    }

    static /* synthetic */ void lambda$onAudioDisabled$10(AnalyticsListener.EventTime r0, DecoderCounters r1, AnalyticsListener r2) {
        r2.onAudioDisabled(r0, r1);
    }

    static /* synthetic */ void lambda$onAudioEnabled$4(AnalyticsListener.EventTime r0, DecoderCounters r1, AnalyticsListener r2) {
        r2.onAudioEnabled(r0, r1);
    }

    static /* synthetic */ void lambda$onAudioInputFormatChanged$6(AnalyticsListener.EventTime r0, Format r1, DecoderReuseEvaluation r2, AnalyticsListener r3) {
        r3.onAudioInputFormatChanged(r0, r1, r2);
    }

    static /* synthetic */ void lambda$onAudioPositionAdvancing$7(AnalyticsListener.EventTime r0, long r1, AnalyticsListener r3) {
        r3.onAudioPositionAdvancing(r0, r1);
    }

    static /* synthetic */ void lambda$onAudioSessionIdChanged$57(AnalyticsListener.EventTime r0, int r1, AnalyticsListener r2) {
        r2.onAudioSessionIdChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onAudioSinkError$11(AnalyticsListener.EventTime r0, Exception r1, AnalyticsListener r2) {
        r2.onAudioSinkError(r0, r1);
    }

    static /* synthetic */ void lambda$onAudioTrackInitialized$13(AnalyticsListener.EventTime r0, AudioSink.AudioTrackConfig r1, AnalyticsListener r2) {
        r2.onAudioTrackInitialized(r0, r1);
    }

    static /* synthetic */ void lambda$onAudioTrackReleased$14(AnalyticsListener.EventTime r0, AudioSink.AudioTrackConfig r1, AnalyticsListener r2) {
        r2.onAudioTrackReleased(r0, r1);
    }

    static /* synthetic */ void lambda$onAudioUnderrun$8(AnalyticsListener.EventTime r1, int r2, long r3, long r5, AnalyticsListener r7) {
        r7.onAudioUnderrun(r1, r2, r3, r5);
    }

    static /* synthetic */ void lambda$onAvailableCommandsChanged$36(AnalyticsListener.EventTime r0, Player.Commands r1, AnalyticsListener r2) {
        r2.onAvailableCommandsChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onBandwidthSample$63(AnalyticsListener.EventTime r1, int r2, long r3, long r5, AnalyticsListener r7) {
        r7.onBandwidthEstimate(r1, r2, r3, r5);
    }

    static /* synthetic */ void lambda$onCues$54(AnalyticsListener.EventTime r0, List r1, AnalyticsListener r2) {
        r2.onCues(r0, r1);
    }

    static /* synthetic */ void lambda$onCues$55(AnalyticsListener.EventTime r0, CueGroup r1, AnalyticsListener r2) {
        r2.onCues(r0, r1);
    }

    static /* synthetic */ void lambda$onDeviceInfoChanged$61(AnalyticsListener.EventTime r0, DeviceInfo r1, AnalyticsListener r2) {
        r2.onDeviceInfoChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onDeviceVolumeChanged$62(AnalyticsListener.EventTime r0, int r1, boolean r2, AnalyticsListener r3) {
        r3.onDeviceVolumeChanged(r0, r1, r2);
    }

    static /* synthetic */ void lambda$onDownstreamFormatChanged$31(AnalyticsListener.EventTime r0, MediaLoadData r1, AnalyticsListener r2) {
        r2.onDownstreamFormatChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onDrmKeysLoaded$65(AnalyticsListener.EventTime r0, AnalyticsListener r1) {
        r1.onDrmKeysLoaded(r0);
    }

    static /* synthetic */ void lambda$onDrmKeysRemoved$68(AnalyticsListener.EventTime r0, AnalyticsListener r1) {
        r1.onDrmKeysRemoved(r0);
    }

    static /* synthetic */ void lambda$onDrmKeysRestored$67(AnalyticsListener.EventTime r0, AnalyticsListener r1) {
        r1.onDrmKeysRestored(r0);
    }

    static /* synthetic */ void lambda$onDrmSessionAcquired$64(AnalyticsListener.EventTime r0, int r1, AnalyticsListener r2) {
        r2.onDrmSessionAcquired(r0);
        r2.onDrmSessionAcquired(r0, r1);
    }

    static /* synthetic */ void lambda$onDrmSessionManagerError$66(AnalyticsListener.EventTime r0, Exception r1, AnalyticsListener r2) {
        r2.onDrmSessionManagerError(r0, r1);
    }

    static /* synthetic */ void lambda$onDrmSessionReleased$69(AnalyticsListener.EventTime r0, AnalyticsListener r1) {
        r1.onDrmSessionReleased(r0);
    }

    static /* synthetic */ void lambda$onDroppedFrames$19(AnalyticsListener.EventTime r0, int r1, long r2, AnalyticsListener r4) {
        r4.onDroppedVideoFrames(r0, r1, r2);
    }

    static /* synthetic */ void lambda$onIsLoadingChanged$35(AnalyticsListener.EventTime r0, boolean r1, AnalyticsListener r2) {
        r2.onLoadingChanged(r0, r1);
        r2.onIsLoadingChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onIsPlayingChanged$41(AnalyticsListener.EventTime r0, boolean r1, AnalyticsListener r2) {
        r2.onIsPlayingChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onLoadCanceled$28(AnalyticsListener.EventTime r0, LoadEventInfo r1, MediaLoadData r2, AnalyticsListener r3) {
        r3.onLoadCanceled(r0, r1, r2);
    }

    static /* synthetic */ void lambda$onLoadCompleted$27(AnalyticsListener.EventTime r0, LoadEventInfo r1, MediaLoadData r2, AnalyticsListener r3) {
        r3.onLoadCompleted(r0, r1, r2);
    }

    static /* synthetic */ void lambda$onLoadError$29(AnalyticsListener.EventTime r1, LoadEventInfo r2, MediaLoadData r3, IOException r4, boolean r5, AnalyticsListener r6) {
        r6.onLoadError(r1, r2, r3, r4, r5);
    }

    static /* synthetic */ void lambda$onLoadStarted$26(AnalyticsListener.EventTime r0, LoadEventInfo r1, MediaLoadData r2, AnalyticsListener r3) {
        r3.onLoadStarted(r0, r1, r2);
    }

    static /* synthetic */ void lambda$onMaxSeekToPreviousPositionChanged$50(AnalyticsListener.EventTime r0, long r1, AnalyticsListener r3) {
        r3.onMaxSeekToPreviousPositionChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onMediaItemTransition$33(AnalyticsListener.EventTime r0, MediaItem r1, int r2, AnalyticsListener r3) {
        r3.onMediaItemTransition(r0, r1, r2);
    }

    static /* synthetic */ void lambda$onMediaMetadataChanged$51(AnalyticsListener.EventTime r0, MediaMetadata r1, AnalyticsListener r2) {
        r2.onMediaMetadataChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onMetadata$53(AnalyticsListener.EventTime r0, Metadata r1, AnalyticsListener r2) {
        r2.onMetadata(r0, r1);
    }

    static /* synthetic */ void lambda$onPlayWhenReadyChanged$39(AnalyticsListener.EventTime r0, boolean r1, int r2, AnalyticsListener r3) {
        r3.onPlayWhenReadyChanged(r0, r1, r2);
    }

    static /* synthetic */ void lambda$onPlaybackParametersChanged$47(AnalyticsListener.EventTime r0, PlaybackParameters r1, AnalyticsListener r2) {
        r2.onPlaybackParametersChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onPlaybackStateChanged$38(AnalyticsListener.EventTime r0, int r1, AnalyticsListener r2) {
        r2.onPlaybackStateChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onPlaybackSuppressionReasonChanged$40(AnalyticsListener.EventTime r0, int r1, AnalyticsListener r2) {
        r2.onPlaybackSuppressionReasonChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onPlayerError$44(AnalyticsListener.EventTime r0, PlaybackException r1, AnalyticsListener r2) {
        r2.onPlayerError(r0, r1);
    }

    static /* synthetic */ void lambda$onPlayerErrorChanged$45(AnalyticsListener.EventTime r0, PlaybackException r1, AnalyticsListener r2) {
        r2.onPlayerErrorChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onPlayerStateChanged$37(AnalyticsListener.EventTime r0, boolean r1, int r2, AnalyticsListener r3) {
        r3.onPlayerStateChanged(r0, r1, r2);
    }

    static /* synthetic */ void lambda$onPlaylistMetadataChanged$52(AnalyticsListener.EventTime r0, MediaMetadata r1, AnalyticsListener r2) {
        r2.onPlaylistMetadataChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onPositionDiscontinuity$46(AnalyticsListener.EventTime r0, int r1, Player.PositionInfo r2, Player.PositionInfo r3, AnalyticsListener r4) {
        r4.onPositionDiscontinuity(r0, r1);
        r4.onPositionDiscontinuity(r0, r2, r3, r1);
    }

    static /* synthetic */ void lambda$onRenderedFirstFrame$22(AnalyticsListener.EventTime r0, Object r1, long r2, AnalyticsListener r4) {
        r4.onRenderedFirstFrame(r0, r1, r2);
    }

    static /* synthetic */ void lambda$onRendererReadyChanged$3(AnalyticsListener.EventTime r0, int r1, int r2, boolean r3, AnalyticsListener r4) {
        r4.onRendererReadyChanged(r0, r1, r2, r3);
    }

    static /* synthetic */ void lambda$onRepeatModeChanged$42(AnalyticsListener.EventTime r0, int r1, AnalyticsListener r2) {
        r2.onRepeatModeChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onSeekBackIncrementChanged$48(AnalyticsListener.EventTime r0, long r1, AnalyticsListener r3) {
        r3.onSeekBackIncrementChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onSeekForwardIncrementChanged$49(AnalyticsListener.EventTime r0, long r1, AnalyticsListener r3) {
        r3.onSeekForwardIncrementChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onShuffleModeEnabledChanged$43(AnalyticsListener.EventTime r0, boolean r1, AnalyticsListener r2) {
        r2.onShuffleModeChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onSkipSilenceEnabledChanged$56(AnalyticsListener.EventTime r0, boolean r1, AnalyticsListener r2) {
        r2.onSkipSilenceEnabledChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onSurfaceSizeChanged$25(AnalyticsListener.EventTime r0, int r1, int r2, AnalyticsListener r3) {
        r3.onSurfaceSizeChanged(r0, r1, r2);
    }

    static /* synthetic */ void lambda$onTimelineChanged$32(AnalyticsListener.EventTime r0, int r1, AnalyticsListener r2) {
        r2.onTimelineChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onTrackSelectionParametersChanged$60(AnalyticsListener.EventTime r0, TrackSelectionParameters r1, AnalyticsListener r2) {
        r2.onTrackSelectionParametersChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onTracksChanged$34(AnalyticsListener.EventTime r0, Tracks r1, AnalyticsListener r2) {
        r2.onTracksChanged(r0, r1);
    }

    static /* synthetic */ void lambda$onUpstreamDiscarded$30(AnalyticsListener.EventTime r0, MediaLoadData r1, AnalyticsListener r2) {
        r2.onUpstreamDiscarded(r0, r1);
    }

    static /* synthetic */ void lambda$onVideoCodecError$24(AnalyticsListener.EventTime r0, Exception r1, AnalyticsListener r2) {
        r2.onVideoCodecError(r0, r1);
    }

    static /* synthetic */ void lambda$onVideoDecoderInitialized$17(AnalyticsListener.EventTime r3, String r4, long r5, long r7, AnalyticsListener r9) {
        r9.onVideoDecoderInitialized(r3, r4, r5);
        r9.onVideoDecoderInitialized(r3, r4, r7, r5);
    }

    static /* synthetic */ void lambda$onVideoDecoderReleased$20(AnalyticsListener.EventTime r0, String r1, AnalyticsListener r2) {
        r2.onVideoDecoderReleased(r0, r1);
    }

    static /* synthetic */ void lambda$onVideoDisabled$21(AnalyticsListener.EventTime r0, DecoderCounters r1, AnalyticsListener r2) {
        r2.onVideoDisabled(r0, r1);
    }

    static /* synthetic */ void lambda$onVideoEnabled$16(AnalyticsListener.EventTime r0, DecoderCounters r1, AnalyticsListener r2) {
        r2.onVideoEnabled(r0, r1);
    }

    static /* synthetic */ void lambda$onVideoFrameProcessingOffset$23(AnalyticsListener.EventTime r0, long r1, int r3, AnalyticsListener r4) {
        r4.onVideoFrameProcessingOffset(r0, r1, r3);
    }

    static /* synthetic */ void lambda$onVideoInputFormatChanged$18(AnalyticsListener.EventTime r0, Format r1, DecoderReuseEvaluation r2, AnalyticsListener r3) {
        r3.onVideoInputFormatChanged(r0, r1, r2);
    }

    static /* synthetic */ void lambda$onVideoSizeChanged$59(AnalyticsListener.EventTime r6, VideoSize r7, AnalyticsListener r8) {
        r8.onVideoSizeChanged(r6, r7);
        r8.onVideoSizeChanged(r6, r7.width, r7.height, 0, r7.pixelWidthHeightRatio);
    }

    static /* synthetic */ void lambda$onVolumeChanged$15(AnalyticsListener.EventTime r0, float r1, AnalyticsListener r2) {
        r2.onVolumeChanged(r0, r1);
    }

    static /* synthetic */ void lambda$releaseInternal$70(AnalyticsListener.EventTime r0, AnalyticsListener r1) {
        r1.onPlayerReleased(r0);
    }

    private void releaseInternal() {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, AnalyticsListener.EVENT_PLAYER_RELEASED, new DefaultAnalyticsCollector$$ExternalSyntheticLambda1(r0));
        this.listeners.release();
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public void addListener(AnalyticsListener r2) {
        Assertions.checkNotNull(r2);
        this.listeners.add(r2);
    }

    protected final AnalyticsListener.EventTime generateCurrentPlayerMediaPeriodEventTime() {
        return generateEventTime(this.mediaPeriodQueueTracker.getCurrentPlayerMediaPeriod());
    }

    @RequiresNonNull({"player"})
    protected final AnalyticsListener.EventTime generateEventTime(Timeline r18, int r19, MediaSource.MediaPeriodId r20) {
        if (r18.isEmpty() == false) goto L5;
        MediaSource.MediaPeriodId r6 = null;
    L6:
        long r2 = this.clock.elapsedRealtime();
        boolean r7 = true;
        if (r18.equals(this.player.getCurrentTimeline()) == true) goto L9;
    L11:
        boolean r1 = false;
    L12:
        boolean r16 = r1;
        long r9 = 0;
        if (r6 != null) goto L15;
    L26:
        if (r16 == false) goto L29;
        long r8 = this.player.getContentPosition();
    L34:
        return new AnalyticsListener.EventTime(r2, r18, r19, r6, r8, this.player.getCurrentTimeline(), this.player.getCurrentMediaItemIndex(), this.mediaPeriodQueueTracker.getCurrentPlayerMediaPeriod(), this.player.getCurrentPosition(), this.player.getTotalBufferedDuration());
    L29:
        if (r18.isEmpty() == true) goto L32;
        r9 = r18.getWindow(r19, this.window).getDefaultPositionMs();
    L32:
        r8 = r9;
        goto L34
    L15:
        if (r6.isAd() == false) goto L26;
        if (r16 == true) goto L18;
    L22:
        r7 = false;
    L23:
        if (r7 == false) goto L25;
        r9 = this.player.getCurrentPosition();
    L25:
        r8 = r9;
        goto L34
    L18:
        if (this.player.getCurrentAdGroupIndex() != r6.adGroupIndex) goto L22;
        if (this.player.getCurrentAdIndexInAdGroup() != r6.adIndexInAdGroup) goto L22;
    L9:
        if (r19 != this.player.getCurrentMediaItemIndex()) goto L11;
        r1 = true;
        goto L12
    L5:
        r6 = r20;
        goto L6
    }

    /* JADX INFO: renamed from: lambda$setPlayer$1$androidx-media3-exoplayer-analytics-DefaultAnalyticsCollector */
    /* synthetic */ void m78xfeaa50a6(Player r3, AnalyticsListener r4, FlagSet r5) {
        r4.onEvents(r3, new AnalyticsListener.Events(r5, this.eventTimes));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void notifySeekStarted() {
        if (this.isSeeking == true) goto L6;
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        this.isSeeking = true;
        sendEvent(r0, -1, new DefaultAnalyticsCollector$$ExternalSyntheticLambda51(r0));
        return;
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onAudioAttributesChanged(final AudioAttributes r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 20, new DefaultAnalyticsCollector$$ExternalSyntheticLambda18(r0, r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onAudioCodecError(final Exception r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, AnalyticsListener.EVENT_AUDIO_CODEC_ERROR, new DefaultAnalyticsCollector$$ExternalSyntheticLambda60(r0, r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onAudioDecoderInitialized(final String r8, final long r9, final long r11) {
        final AnalyticsListener.EventTime r1 = generateReadingMediaPeriodEventTime();
        sendEvent(r1, 1008, new DefaultAnalyticsCollector$$ExternalSyntheticLambda23(r1, r8, r11, r9));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onAudioDecoderReleased(final String r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 1012, new DefaultAnalyticsCollector$$ExternalSyntheticLambda5(r0, r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onAudioDisabled(final DecoderCounters r4) {
        final AnalyticsListener.EventTime r0 = generatePlayingMediaPeriodEventTime();
        sendEvent(r0, 1013, new DefaultAnalyticsCollector$$ExternalSyntheticLambda37(r0, r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onAudioEnabled(final DecoderCounters r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 1007, new DefaultAnalyticsCollector$$ExternalSyntheticLambda2(r0, r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onAudioInputFormatChanged(final Format r4, final DecoderReuseEvaluation r5) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 1009, new DefaultAnalyticsCollector$$ExternalSyntheticLambda52(r0, r4, r5));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onAudioPositionAdvancing(final long r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 1010, new DefaultAnalyticsCollector$$ExternalSyntheticLambda20(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onAudioSessionIdChanged(final int r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 21, new DefaultAnalyticsCollector$$ExternalSyntheticLambda48(r0, r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onAudioSinkError(final Exception r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 1014, new DefaultAnalyticsCollector$$ExternalSyntheticLambda65(r0, r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public void onAudioTrackInitialized(final AudioSink.AudioTrackConfig r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, AnalyticsListener.EVENT_AUDIO_TRACK_INITIALIZED, new DefaultAnalyticsCollector$$ExternalSyntheticLambda46(r0, r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public void onAudioTrackReleased(final AudioSink.AudioTrackConfig r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, AnalyticsListener.EVENT_AUDIO_TRACK_RELEASED, new DefaultAnalyticsCollector$$ExternalSyntheticLambda63(r0, r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onAudioUnderrun(final int r8, final long r9, final long r11) {
        final AnalyticsListener.EventTime r1 = generateReadingMediaPeriodEventTime();
        sendEvent(r1, 1011, new DefaultAnalyticsCollector$$ExternalSyntheticLambda14(r1, r8, r9, r11));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onAvailableCommandsChanged(final Player.Commands r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 13, new DefaultAnalyticsCollector$$ExternalSyntheticLambda7(r0, r4));
    }

    @Override // androidx.media3.exoplayer.upstream.BandwidthMeter.EventListener
    public final void onBandwidthSample(final int r8, final long r9, final long r11) {
        final AnalyticsListener.EventTime r1 = generateLoadingMediaPeriodEventTime();
        sendEvent(r1, 1006, new DefaultAnalyticsCollector$$ExternalSyntheticLambda11(r1, r8, r9, r11));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onCues(final CueGroup r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 27, new DefaultAnalyticsCollector$$ExternalSyntheticLambda55(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onCues(final List<Cue> r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 27, new DefaultAnalyticsCollector$$ExternalSyntheticLambda31(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onDeviceInfoChanged(final DeviceInfo r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 29, new DefaultAnalyticsCollector$$ExternalSyntheticLambda40(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onDeviceVolumeChanged(final int r4, final boolean r5) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 30, new DefaultAnalyticsCollector$$ExternalSyntheticLambda28(r0, r4, r5));
    }

    @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
    public final void onDownstreamFormatChanged(int r4, MediaSource.MediaPeriodId r5, final MediaLoadData r6) {
        final AnalyticsListener.EventTime r0 = generateMediaPeriodEventTime(r4, r5);
        sendEvent(r0, 1004, new DefaultAnalyticsCollector$$ExternalSyntheticLambda33(r0, r6));
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
    public final void onDrmKeysLoaded(int r4, MediaSource.MediaPeriodId r5) {
        final AnalyticsListener.EventTime r0 = generateMediaPeriodEventTime(r4, r5);
        sendEvent(r0, AnalyticsListener.EVENT_DRM_KEYS_LOADED, new DefaultAnalyticsCollector$$ExternalSyntheticLambda62(r0));
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
    public final void onDrmKeysRemoved(int r4, MediaSource.MediaPeriodId r5) {
        final AnalyticsListener.EventTime r0 = generateMediaPeriodEventTime(r4, r5);
        sendEvent(r0, AnalyticsListener.EVENT_DRM_KEYS_REMOVED, new DefaultAnalyticsCollector$$ExternalSyntheticLambda41(r0));
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
    public final void onDrmKeysRestored(int r4, MediaSource.MediaPeriodId r5) {
        final AnalyticsListener.EventTime r0 = generateMediaPeriodEventTime(r4, r5);
        sendEvent(r0, 1025, new DefaultAnalyticsCollector$$ExternalSyntheticLambda47(r0));
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
    public final void onDrmSessionAcquired(int r4, MediaSource.MediaPeriodId r5, final int r6) {
        final AnalyticsListener.EventTime r0 = generateMediaPeriodEventTime(r4, r5);
        sendEvent(r0, AnalyticsListener.EVENT_DRM_SESSION_ACQUIRED, new DefaultAnalyticsCollector$$ExternalSyntheticLambda39(r0, r6));
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
    public final void onDrmSessionManagerError(int r4, MediaSource.MediaPeriodId r5, final Exception r6) {
        final AnalyticsListener.EventTime r0 = generateMediaPeriodEventTime(r4, r5);
        sendEvent(r0, 1024, new DefaultAnalyticsCollector$$ExternalSyntheticLambda42(r0, r6));
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionEventListener
    public final void onDrmSessionReleased(int r4, MediaSource.MediaPeriodId r5) {
        final AnalyticsListener.EventTime r0 = generateMediaPeriodEventTime(r4, r5);
        sendEvent(r0, AnalyticsListener.EVENT_DRM_SESSION_RELEASED, new DefaultAnalyticsCollector$$ExternalSyntheticLambda64(r0));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onDroppedFrames(final int r4, final long r5) {
        final AnalyticsListener.EventTime r0 = generatePlayingMediaPeriodEventTime();
        sendEvent(r0, 1018, new DefaultAnalyticsCollector$$ExternalSyntheticLambda26(r0, r4, r5));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onEvents(Player r1, Player.Events r2) {
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onIsLoadingChanged(final boolean r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 3, new DefaultAnalyticsCollector$$ExternalSyntheticLambda4(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onIsPlayingChanged(final boolean r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 7, new DefaultAnalyticsCollector$$ExternalSyntheticLambda21(r0, r4));
    }

    @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
    public final void onLoadCanceled(int r4, MediaSource.MediaPeriodId r5, final LoadEventInfo r6, final MediaLoadData r7) {
        final AnalyticsListener.EventTime r0 = generateMediaPeriodEventTime(r4, r5);
        sendEvent(r0, 1002, new DefaultAnalyticsCollector$$ExternalSyntheticLambda35(r0, r6, r7));
    }

    @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
    public final void onLoadCompleted(int r4, MediaSource.MediaPeriodId r5, final LoadEventInfo r6, final MediaLoadData r7) {
        final AnalyticsListener.EventTime r0 = generateMediaPeriodEventTime(r4, r5);
        sendEvent(r0, 1001, new DefaultAnalyticsCollector$$ExternalSyntheticLambda43(r0, r6, r7));
    }

    @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
    public final void onLoadError(int r7, MediaSource.MediaPeriodId r8, final LoadEventInfo r9, final MediaLoadData r10, final IOException r11, final boolean r12) {
        final AnalyticsListener.EventTime r1 = generateMediaPeriodEventTime(r7, r8);
        sendEvent(r1, 1003, new DefaultAnalyticsCollector$$ExternalSyntheticLambda15(r1, r9, r10, r11, r12));
    }

    @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
    public final void onLoadStarted(int r4, MediaSource.MediaPeriodId r5, final LoadEventInfo r6, final MediaLoadData r7) {
        final AnalyticsListener.EventTime r0 = generateMediaPeriodEventTime(r4, r5);
        sendEvent(r0, 1000, new DefaultAnalyticsCollector$$ExternalSyntheticLambda6(r0, r6, r7));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onLoadingChanged(boolean r1) {
    }

    @Override // androidx.media3.common.Player.Listener
    public void onMaxSeekToPreviousPositionChanged(final long r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 18, new DefaultAnalyticsCollector$$ExternalSyntheticLambda70(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onMediaItemTransition(final MediaItem r4, final int r5) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 1, new DefaultAnalyticsCollector$$ExternalSyntheticLambda9(r0, r4, r5));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onMediaMetadataChanged(final MediaMetadata r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 14, new DefaultAnalyticsCollector$$ExternalSyntheticLambda66(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onMetadata(final Metadata r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 28, new DefaultAnalyticsCollector$$ExternalSyntheticLambda19(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onPlayWhenReadyChanged(final boolean r4, final int r5) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 5, new DefaultAnalyticsCollector$$ExternalSyntheticLambda29(r0, r4, r5));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onPlaybackParametersChanged(final PlaybackParameters r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 12, new DefaultAnalyticsCollector$$ExternalSyntheticLambda0(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onPlaybackStateChanged(final int r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 4, new DefaultAnalyticsCollector$$ExternalSyntheticLambda38(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onPlaybackSuppressionReasonChanged(final int r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 6, new DefaultAnalyticsCollector$$ExternalSyntheticLambda24(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onPlayerError(final PlaybackException r4) {
        final AnalyticsListener.EventTime r0 = getEventTimeForErrorEvent(r4);
        sendEvent(r0, 10, new DefaultAnalyticsCollector$$ExternalSyntheticLambda36(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onPlayerErrorChanged(final PlaybackException r4) {
        final AnalyticsListener.EventTime r0 = getEventTimeForErrorEvent(r4);
        sendEvent(r0, 10, new DefaultAnalyticsCollector$$ExternalSyntheticLambda27(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onPlayerStateChanged(final boolean r4, final int r5) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, -1, new DefaultAnalyticsCollector$$ExternalSyntheticLambda17(r0, r4, r5));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onPlaylistMetadataChanged(final MediaMetadata r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 15, new DefaultAnalyticsCollector$$ExternalSyntheticLambda49(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onPositionDiscontinuity(int r1) {
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onPositionDiscontinuity(final Player.PositionInfo r4, final Player.PositionInfo r5, final int r6) {
        if (r6 != 1) goto L5;
        this.isSeeking = false;
    L5:
        this.mediaPeriodQueueTracker.onPositionDiscontinuity((Player) Assertions.checkNotNull(this.player));
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 11, new DefaultAnalyticsCollector$$ExternalSyntheticLambda53(r0, r6, r4, r5));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onRenderedFirstFrame() {
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onRenderedFirstFrame(final Object r4, final long r5) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 26, new DefaultAnalyticsCollector$$ExternalSyntheticLambda58(r0, r4, r5));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public void onRendererReadyChanged(final int r4, final int r5, final boolean r6) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, AnalyticsListener.EVENT_RENDERER_READY_CHANGED, new DefaultAnalyticsCollector$$ExternalSyntheticLambda30(r0, r4, r5, r6));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onRepeatModeChanged(final int r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 8, new DefaultAnalyticsCollector$$ExternalSyntheticLambda57(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onSeekBackIncrementChanged(final long r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 16, new DefaultAnalyticsCollector$$ExternalSyntheticLambda69(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onSeekForwardIncrementChanged(final long r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 17, new DefaultAnalyticsCollector$$ExternalSyntheticLambda3(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onShuffleModeEnabledChanged(final boolean r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 9, new DefaultAnalyticsCollector$$ExternalSyntheticLambda67(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onSkipSilenceEnabledChanged(final boolean r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 23, new DefaultAnalyticsCollector$$ExternalSyntheticLambda10(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onSurfaceSizeChanged(final int r4, final int r5) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 24, new DefaultAnalyticsCollector$$ExternalSyntheticLambda68(r0, r4, r5));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onTimelineChanged(Timeline r4, final int r5) {
        this.mediaPeriodQueueTracker.onTimelineChanged((Player) Assertions.checkNotNull(this.player));
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 0, new DefaultAnalyticsCollector$$ExternalSyntheticLambda8(r0, r5));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onTrackSelectionParametersChanged(final TrackSelectionParameters r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 19, new DefaultAnalyticsCollector$$ExternalSyntheticLambda71(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public void onTracksChanged(final Tracks r4) {
        final AnalyticsListener.EventTime r0 = generateCurrentPlayerMediaPeriodEventTime();
        sendEvent(r0, 2, new DefaultAnalyticsCollector$$ExternalSyntheticLambda22(r0, r4));
    }

    @Override // androidx.media3.exoplayer.source.MediaSourceEventListener
    public final void onUpstreamDiscarded(int r4, MediaSource.MediaPeriodId r5, final MediaLoadData r6) {
        final AnalyticsListener.EventTime r0 = generateMediaPeriodEventTime(r4, r5);
        sendEvent(r0, AnalyticsListener.EVENT_UPSTREAM_DISCARDED, new DefaultAnalyticsCollector$$ExternalSyntheticLambda59(r0, r6));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onVideoCodecError(final Exception r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, AnalyticsListener.EVENT_VIDEO_CODEC_ERROR, new DefaultAnalyticsCollector$$ExternalSyntheticLambda13(r0, r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onVideoDecoderInitialized(final String r8, final long r9, final long r11) {
        final AnalyticsListener.EventTime r1 = generateReadingMediaPeriodEventTime();
        sendEvent(r1, 1016, new DefaultAnalyticsCollector$$ExternalSyntheticLambda61(r1, r8, r11, r9));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onVideoDecoderReleased(final String r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 1019, new DefaultAnalyticsCollector$$ExternalSyntheticLambda25(r0, r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onVideoDisabled(final DecoderCounters r4) {
        final AnalyticsListener.EventTime r0 = generatePlayingMediaPeriodEventTime();
        sendEvent(r0, 1020, new DefaultAnalyticsCollector$$ExternalSyntheticLambda44(r0, r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onVideoEnabled(final DecoderCounters r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 1015, new DefaultAnalyticsCollector$$ExternalSyntheticLambda54(r0, r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onVideoFrameProcessingOffset(final long r4, final int r6) {
        final AnalyticsListener.EventTime r0 = generatePlayingMediaPeriodEventTime();
        sendEvent(r0, 1021, new DefaultAnalyticsCollector$$ExternalSyntheticLambda34(r0, r4, r6));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void onVideoInputFormatChanged(final Format r4, final DecoderReuseEvaluation r5) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 1017, new DefaultAnalyticsCollector$$ExternalSyntheticLambda45(r0, r4, r5));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onVideoSizeChanged(final VideoSize r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 25, new DefaultAnalyticsCollector$$ExternalSyntheticLambda50(r0, r4));
    }

    @Override // androidx.media3.common.Player.Listener
    public final void onVolumeChanged(final float r4) {
        final AnalyticsListener.EventTime r0 = generateReadingMediaPeriodEventTime();
        sendEvent(r0, 22, new DefaultAnalyticsCollector$$ExternalSyntheticLambda12(r0, r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public void release() {
        ((HandlerWrapper) Assertions.checkStateNotNull(this.handler)).post(new DefaultAnalyticsCollector$$ExternalSyntheticLambda56(this));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public void removeListener(AnalyticsListener r2) {
        this.listeners.remove(r2);
    }

    protected final void sendEvent(AnalyticsListener.EventTime r2, int r3, ListenerSet.Event<AnalyticsListener> r4) {
        this.eventTimes.put(r3, r2);
        this.listeners.sendEvent(r3, r4);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public void setPlayer(final Player r3, Looper r4) {
        if (this.player != null) goto L5;
    L8:
        boolean r0 = true;
    L9:
        Assertions.checkState(r0);
        this.player = (Player) Assertions.checkNotNull(r3);
        this.handler = this.clock.createHandler(r4, null);
        this.listeners = this.listeners.copy(r4, new DefaultAnalyticsCollector$$ExternalSyntheticLambda16(this, r3));
        return;
    L5:
        if (MediaPeriodQueueTracker.access$000(this.mediaPeriodQueueTracker).isEmpty() == true) goto L8;
        r0 = false;
        goto L9
    }

    @Deprecated
    public void setThrowsWhenUsingWrongThread(boolean r2) {
        this.listeners.setThrowsWhenUsingWrongThread(r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsCollector
    public final void updateMediaPeriodQueueInfo(List<MediaSource.MediaPeriodId> r3, MediaSource.MediaPeriodId r4) {
        this.mediaPeriodQueueTracker.onQueueUpdated(r3, r4, (Player) Assertions.checkNotNull(this.player));
    }
}
