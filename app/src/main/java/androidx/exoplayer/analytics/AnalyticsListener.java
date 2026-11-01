package androidx.media3.exoplayer.analytics;

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
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.DecoderReuseEvaluation;
import androidx.media3.exoplayer.audio.AudioSink;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MediaLoadData;
import androidx.media3.exoplayer.source.MediaSource;
import com.google.common.base.Objects;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface AnalyticsListener {
    public static final int EVENT_AUDIO_ATTRIBUTES_CHANGED = 20;
    public static final int EVENT_AUDIO_CODEC_ERROR = 1029;
    public static final int EVENT_AUDIO_DECODER_INITIALIZED = 1008;
    public static final int EVENT_AUDIO_DECODER_RELEASED = 1012;
    public static final int EVENT_AUDIO_DISABLED = 1013;
    public static final int EVENT_AUDIO_ENABLED = 1007;
    public static final int EVENT_AUDIO_INPUT_FORMAT_CHANGED = 1009;
    public static final int EVENT_AUDIO_POSITION_ADVANCING = 1010;
    public static final int EVENT_AUDIO_SESSION_ID = 21;
    public static final int EVENT_AUDIO_SINK_ERROR = 1014;
    public static final int EVENT_AUDIO_TRACK_INITIALIZED = 1031;
    public static final int EVENT_AUDIO_TRACK_RELEASED = 1032;
    public static final int EVENT_AUDIO_UNDERRUN = 1011;
    public static final int EVENT_AVAILABLE_COMMANDS_CHANGED = 13;
    public static final int EVENT_BANDWIDTH_ESTIMATE = 1006;
    public static final int EVENT_CUES = 27;
    public static final int EVENT_DEVICE_INFO_CHANGED = 29;
    public static final int EVENT_DEVICE_VOLUME_CHANGED = 30;
    public static final int EVENT_DOWNSTREAM_FORMAT_CHANGED = 1004;
    public static final int EVENT_DRM_KEYS_LOADED = 1023;
    public static final int EVENT_DRM_KEYS_REMOVED = 1026;
    public static final int EVENT_DRM_KEYS_RESTORED = 1025;
    public static final int EVENT_DRM_SESSION_ACQUIRED = 1022;
    public static final int EVENT_DRM_SESSION_MANAGER_ERROR = 1024;
    public static final int EVENT_DRM_SESSION_RELEASED = 1027;
    public static final int EVENT_DROPPED_VIDEO_FRAMES = 1018;
    public static final int EVENT_IS_LOADING_CHANGED = 3;
    public static final int EVENT_IS_PLAYING_CHANGED = 7;
    public static final int EVENT_LOAD_CANCELED = 1002;
    public static final int EVENT_LOAD_COMPLETED = 1001;
    public static final int EVENT_LOAD_ERROR = 1003;
    public static final int EVENT_LOAD_STARTED = 1000;
    public static final int EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED = 18;
    public static final int EVENT_MEDIA_ITEM_TRANSITION = 1;
    public static final int EVENT_MEDIA_METADATA_CHANGED = 14;
    public static final int EVENT_METADATA = 28;
    public static final int EVENT_PLAYBACK_PARAMETERS_CHANGED = 12;
    public static final int EVENT_PLAYBACK_STATE_CHANGED = 4;
    public static final int EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED = 6;
    public static final int EVENT_PLAYER_ERROR = 10;
    public static final int EVENT_PLAYER_RELEASED = 1028;
    public static final int EVENT_PLAYLIST_METADATA_CHANGED = 15;
    public static final int EVENT_PLAY_WHEN_READY_CHANGED = 5;
    public static final int EVENT_POSITION_DISCONTINUITY = 11;
    public static final int EVENT_RENDERED_FIRST_FRAME = 26;
    public static final int EVENT_RENDERER_READY_CHANGED = 1033;
    public static final int EVENT_REPEAT_MODE_CHANGED = 8;
    public static final int EVENT_SEEK_BACK_INCREMENT_CHANGED = 16;
    public static final int EVENT_SEEK_FORWARD_INCREMENT_CHANGED = 17;
    public static final int EVENT_SHUFFLE_MODE_ENABLED_CHANGED = 9;
    public static final int EVENT_SKIP_SILENCE_ENABLED_CHANGED = 23;
    public static final int EVENT_SURFACE_SIZE_CHANGED = 24;
    public static final int EVENT_TIMELINE_CHANGED = 0;
    public static final int EVENT_TRACKS_CHANGED = 2;
    public static final int EVENT_TRACK_SELECTION_PARAMETERS_CHANGED = 19;
    public static final int EVENT_UPSTREAM_DISCARDED = 1005;
    public static final int EVENT_VIDEO_CODEC_ERROR = 1030;
    public static final int EVENT_VIDEO_DECODER_INITIALIZED = 1016;
    public static final int EVENT_VIDEO_DECODER_RELEASED = 1019;
    public static final int EVENT_VIDEO_DISABLED = 1020;
    public static final int EVENT_VIDEO_ENABLED = 1015;
    public static final int EVENT_VIDEO_FRAME_PROCESSING_OFFSET = 1021;
    public static final int EVENT_VIDEO_INPUT_FORMAT_CHANGED = 1017;
    public static final int EVENT_VIDEO_SIZE_CHANGED = 25;
    public static final int EVENT_VOLUME_CHANGED = 22;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.analytics.AnalyticsListener$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onAudioAttributesChanged(AnalyticsListener r0, EventTime r1, AudioAttributes r2) {
        }

        public static void $default$onAudioCodecError(AnalyticsListener r0, EventTime r1, Exception r2) {
        }

        @Deprecated
        public static void $default$onAudioDecoderInitialized(AnalyticsListener r0, EventTime r1, String r2, long r3) {
        }

        public static void $default$onAudioDecoderInitialized(AnalyticsListener r0, EventTime r1, String r2, long r3, long r5) {
        }

        public static void $default$onAudioDecoderReleased(AnalyticsListener r0, EventTime r1, String r2) {
        }

        public static void $default$onAudioDisabled(AnalyticsListener r0, EventTime r1, DecoderCounters r2) {
        }

        public static void $default$onAudioEnabled(AnalyticsListener r0, EventTime r1, DecoderCounters r2) {
        }

        public static void $default$onAudioInputFormatChanged(AnalyticsListener r0, EventTime r1, Format r2, DecoderReuseEvaluation r3) {
        }

        public static void $default$onAudioPositionAdvancing(AnalyticsListener r0, EventTime r1, long r2) {
        }

        public static void $default$onAudioSessionIdChanged(AnalyticsListener r0, EventTime r1, int r2) {
        }

        public static void $default$onAudioSinkError(AnalyticsListener r0, EventTime r1, Exception r2) {
        }

        public static void $default$onAudioTrackInitialized(AnalyticsListener r0, EventTime r1, AudioSink.AudioTrackConfig r2) {
        }

        public static void $default$onAudioTrackReleased(AnalyticsListener r0, EventTime r1, AudioSink.AudioTrackConfig r2) {
        }

        public static void $default$onAudioUnderrun(AnalyticsListener r0, EventTime r1, int r2, long r3, long r5) {
        }

        public static void $default$onAvailableCommandsChanged(AnalyticsListener r0, EventTime r1, Player.Commands r2) {
        }

        public static void $default$onBandwidthEstimate(AnalyticsListener r0, EventTime r1, int r2, long r3, long r5) {
        }

        public static void $default$onCues(AnalyticsListener r0, EventTime r1, CueGroup r2) {
        }

        @Deprecated
        public static void $default$onCues(AnalyticsListener r0, EventTime r1, List r2) {
        }

        public static void $default$onDeviceInfoChanged(AnalyticsListener r0, EventTime r1, DeviceInfo r2) {
        }

        public static void $default$onDeviceVolumeChanged(AnalyticsListener r0, EventTime r1, int r2, boolean r3) {
        }

        public static void $default$onDownstreamFormatChanged(AnalyticsListener r0, EventTime r1, MediaLoadData r2) {
        }

        public static void $default$onDrmKeysLoaded(AnalyticsListener r0, EventTime r1) {
        }

        public static void $default$onDrmKeysRemoved(AnalyticsListener r0, EventTime r1) {
        }

        public static void $default$onDrmKeysRestored(AnalyticsListener r0, EventTime r1) {
        }

        @Deprecated
        public static void $default$onDrmSessionAcquired(AnalyticsListener r0, EventTime r1) {
        }

        public static void $default$onDrmSessionAcquired(AnalyticsListener r0, EventTime r1, int r2) {
        }

        public static void $default$onDrmSessionManagerError(AnalyticsListener r0, EventTime r1, Exception r2) {
        }

        public static void $default$onDrmSessionReleased(AnalyticsListener r0, EventTime r1) {
        }

        public static void $default$onDroppedVideoFrames(AnalyticsListener r0, EventTime r1, int r2, long r3) {
        }

        public static void $default$onEvents(AnalyticsListener r0, Player r1, Events r2) {
        }

        public static void $default$onIsLoadingChanged(AnalyticsListener r0, EventTime r1, boolean r2) {
        }

        public static void $default$onIsPlayingChanged(AnalyticsListener r0, EventTime r1, boolean r2) {
        }

        public static void $default$onLoadCanceled(AnalyticsListener r0, EventTime r1, LoadEventInfo r2, MediaLoadData r3) {
        }

        public static void $default$onLoadCompleted(AnalyticsListener r0, EventTime r1, LoadEventInfo r2, MediaLoadData r3) {
        }

        public static void $default$onLoadError(AnalyticsListener r0, EventTime r1, LoadEventInfo r2, MediaLoadData r3, IOException r4, boolean r5) {
        }

        public static void $default$onLoadStarted(AnalyticsListener r0, EventTime r1, LoadEventInfo r2, MediaLoadData r3) {
        }

        @Deprecated
        public static void $default$onLoadingChanged(AnalyticsListener r0, EventTime r1, boolean r2) {
        }

        public static void $default$onMaxSeekToPreviousPositionChanged(AnalyticsListener r0, EventTime r1, long r2) {
        }

        public static void $default$onMediaItemTransition(AnalyticsListener r0, EventTime r1, MediaItem r2, int r3) {
        }

        public static void $default$onMediaMetadataChanged(AnalyticsListener r0, EventTime r1, MediaMetadata r2) {
        }

        public static void $default$onMetadata(AnalyticsListener r0, EventTime r1, Metadata r2) {
        }

        public static void $default$onPlayWhenReadyChanged(AnalyticsListener r0, EventTime r1, boolean r2, int r3) {
        }

        public static void $default$onPlaybackParametersChanged(AnalyticsListener r0, EventTime r1, PlaybackParameters r2) {
        }

        public static void $default$onPlaybackStateChanged(AnalyticsListener r0, EventTime r1, int r2) {
        }

        public static void $default$onPlaybackSuppressionReasonChanged(AnalyticsListener r0, EventTime r1, int r2) {
        }

        public static void $default$onPlayerError(AnalyticsListener r0, EventTime r1, PlaybackException r2) {
        }

        public static void $default$onPlayerErrorChanged(AnalyticsListener r0, EventTime r1, PlaybackException r2) {
        }

        public static void $default$onPlayerReleased(AnalyticsListener r0, EventTime r1) {
        }

        @Deprecated
        public static void $default$onPlayerStateChanged(AnalyticsListener r0, EventTime r1, boolean r2, int r3) {
        }

        public static void $default$onPlaylistMetadataChanged(AnalyticsListener r0, EventTime r1, MediaMetadata r2) {
        }

        @Deprecated
        public static void $default$onPositionDiscontinuity(AnalyticsListener r0, EventTime r1, int r2) {
        }

        public static void $default$onPositionDiscontinuity(AnalyticsListener r0, EventTime r1, Player.PositionInfo r2, Player.PositionInfo r3, int r4) {
        }

        public static void $default$onRenderedFirstFrame(AnalyticsListener r0, EventTime r1, Object r2, long r3) {
        }

        public static void $default$onRendererReadyChanged(AnalyticsListener r0, EventTime r1, int r2, int r3, boolean r4) {
        }

        public static void $default$onRepeatModeChanged(AnalyticsListener r0, EventTime r1, int r2) {
        }

        public static void $default$onSeekBackIncrementChanged(AnalyticsListener r0, EventTime r1, long r2) {
        }

        public static void $default$onSeekForwardIncrementChanged(AnalyticsListener r0, EventTime r1, long r2) {
        }

        @Deprecated
        public static void $default$onSeekStarted(AnalyticsListener r0, EventTime r1) {
        }

        public static void $default$onShuffleModeChanged(AnalyticsListener r0, EventTime r1, boolean r2) {
        }

        public static void $default$onSkipSilenceEnabledChanged(AnalyticsListener r0, EventTime r1, boolean r2) {
        }

        public static void $default$onSurfaceSizeChanged(AnalyticsListener r0, EventTime r1, int r2, int r3) {
        }

        public static void $default$onTimelineChanged(AnalyticsListener r0, EventTime r1, int r2) {
        }

        public static void $default$onTrackSelectionParametersChanged(AnalyticsListener r0, EventTime r1, TrackSelectionParameters r2) {
        }

        public static void $default$onTracksChanged(AnalyticsListener r0, EventTime r1, Tracks r2) {
        }

        public static void $default$onUpstreamDiscarded(AnalyticsListener r0, EventTime r1, MediaLoadData r2) {
        }

        public static void $default$onVideoCodecError(AnalyticsListener r0, EventTime r1, Exception r2) {
        }

        @Deprecated
        public static void $default$onVideoDecoderInitialized(AnalyticsListener r0, EventTime r1, String r2, long r3) {
        }

        public static void $default$onVideoDecoderInitialized(AnalyticsListener r0, EventTime r1, String r2, long r3, long r5) {
        }

        public static void $default$onVideoDecoderReleased(AnalyticsListener r0, EventTime r1, String r2) {
        }

        public static void $default$onVideoDisabled(AnalyticsListener r0, EventTime r1, DecoderCounters r2) {
        }

        public static void $default$onVideoEnabled(AnalyticsListener r0, EventTime r1, DecoderCounters r2) {
        }

        public static void $default$onVideoFrameProcessingOffset(AnalyticsListener r0, EventTime r1, long r2, int r4) {
        }

        public static void $default$onVideoInputFormatChanged(AnalyticsListener r0, EventTime r1, Format r2, DecoderReuseEvaluation r3) {
        }

        @Deprecated
        public static void $default$onVideoSizeChanged(AnalyticsListener r0, EventTime r1, int r2, int r3, int r4, float r5) {
        }

        public static void $default$onVideoSizeChanged(AnalyticsListener r0, EventTime r1, VideoSize r2) {
        }

        public static void $default$onVolumeChanged(AnalyticsListener r0, EventTime r1, float r2) {
        }
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventFlags {
    }

    public static final class EventTime {
        public final MediaSource.MediaPeriodId currentMediaPeriodId;
        public final long currentPlaybackPositionMs;
        public final Timeline currentTimeline;
        public final int currentWindowIndex;
        public final long eventPlaybackPositionMs;
        public final MediaSource.MediaPeriodId mediaPeriodId;
        public final long realtimeMs;
        public final Timeline timeline;
        public final long totalBufferedDurationMs;
        public final int windowIndex;

        public EventTime(long r1, Timeline r3, int r4, MediaSource.MediaPeriodId r5, long r6, Timeline r8, int r9, MediaSource.MediaPeriodId r10, long r11, long r13) {
            this.realtimeMs = r1;
            this.timeline = r3;
            this.windowIndex = r4;
            this.mediaPeriodId = r5;
            this.eventPlaybackPositionMs = r6;
            this.currentTimeline = r8;
            this.currentWindowIndex = r9;
            this.currentMediaPeriodId = r10;
            this.currentPlaybackPositionMs = r11;
            this.totalBufferedDurationMs = r13;
        }

        public boolean equals(Object r8) {
            if (this != r8) goto L6;
            return true;
        L6:
            if (r8 != null) goto L8;
        L33:
            return false;
        L8:
            if (getClass() != r8.getClass()) goto L33;
            EventTime r2 = (EventTime) r8;
            if (this.realtimeMs != r2.realtimeMs) goto L32;
            if (this.windowIndex != r2.windowIndex) goto L32;
            if (this.eventPlaybackPositionMs != r2.eventPlaybackPositionMs) goto L32;
            if (this.currentWindowIndex != r2.currentWindowIndex) goto L32;
            if (this.currentPlaybackPositionMs != r2.currentPlaybackPositionMs) goto L32;
            if (this.totalBufferedDurationMs != r2.totalBufferedDurationMs) goto L32;
            if (Objects.equal(this.timeline, r2.timeline) == false) goto L32;
            if (Objects.equal(this.mediaPeriodId, r2.mediaPeriodId) == false) goto L32;
            if (Objects.equal(this.currentTimeline, r2.currentTimeline) == false) goto L32;
            if (Objects.equal(this.currentMediaPeriodId, r2.currentMediaPeriodId) == false) goto L32;
            return true;
        L32:
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{Long.valueOf(this.realtimeMs), this.timeline, Integer.valueOf(this.windowIndex), this.mediaPeriodId, Long.valueOf(this.eventPlaybackPositionMs), this.currentTimeline, Integer.valueOf(this.currentWindowIndex), this.currentMediaPeriodId, Long.valueOf(this.currentPlaybackPositionMs), Long.valueOf(this.totalBufferedDurationMs)});
        }
    }

    public static final class Events {
        private final SparseArray<EventTime> eventTimes;
        private final FlagSet flags;

        public Events(FlagSet r5, SparseArray<EventTime> r6) {
            this.flags = r5;
            SparseArray<EventTime> r0 = new SparseArray(r5.size());
            int r1 = 0;
        L4:
            if (r1 >= r5.size()) goto L6;
            int r2 = r5.get(r1);
            r0.append(r2, (EventTime) Assertions.checkNotNull(r6.get(r2)));
            r1 = r1 + 1;
            goto L4
        L6:
            this.eventTimes = r0;
        }

        public boolean contains(int r2) {
            return this.flags.contains(r2);
        }

        public boolean containsAny(int... r2) {
            return this.flags.containsAny(r2);
        }

        public int get(int r2) {
            return this.flags.get(r2);
        }

        public EventTime getEventTime(int r2) {
            return (EventTime) Assertions.checkNotNull(this.eventTimes.get(r2));
        }

        public int size() {
            return this.flags.size();
        }
    }

    void onAudioAttributesChanged(EventTime r1, AudioAttributes r2);

    void onAudioCodecError(EventTime r1, Exception r2);

    @Deprecated
    void onAudioDecoderInitialized(EventTime r1, String r2, long r3);

    void onAudioDecoderInitialized(EventTime r1, String r2, long r3, long r5);

    void onAudioDecoderReleased(EventTime r1, String r2);

    void onAudioDisabled(EventTime r1, DecoderCounters r2);

    void onAudioEnabled(EventTime r1, DecoderCounters r2);

    void onAudioInputFormatChanged(EventTime r1, Format r2, DecoderReuseEvaluation r3);

    void onAudioPositionAdvancing(EventTime r1, long r2);

    void onAudioSessionIdChanged(EventTime r1, int r2);

    void onAudioSinkError(EventTime r1, Exception r2);

    void onAudioTrackInitialized(EventTime r1, AudioSink.AudioTrackConfig r2);

    void onAudioTrackReleased(EventTime r1, AudioSink.AudioTrackConfig r2);

    void onAudioUnderrun(EventTime r1, int r2, long r3, long r5);

    void onAvailableCommandsChanged(EventTime r1, Player.Commands r2);

    void onBandwidthEstimate(EventTime r1, int r2, long r3, long r5);

    void onCues(EventTime r1, CueGroup r2);

    @Deprecated
    void onCues(EventTime r1, List<Cue> r2);

    void onDeviceInfoChanged(EventTime r1, DeviceInfo r2);

    void onDeviceVolumeChanged(EventTime r1, int r2, boolean r3);

    void onDownstreamFormatChanged(EventTime r1, MediaLoadData r2);

    void onDrmKeysLoaded(EventTime r1);

    void onDrmKeysRemoved(EventTime r1);

    void onDrmKeysRestored(EventTime r1);

    @Deprecated
    void onDrmSessionAcquired(EventTime r1);

    void onDrmSessionAcquired(EventTime r1, int r2);

    void onDrmSessionManagerError(EventTime r1, Exception r2);

    void onDrmSessionReleased(EventTime r1);

    void onDroppedVideoFrames(EventTime r1, int r2, long r3);

    void onEvents(Player r1, Events r2);

    void onIsLoadingChanged(EventTime r1, boolean r2);

    void onIsPlayingChanged(EventTime r1, boolean r2);

    void onLoadCanceled(EventTime r1, LoadEventInfo r2, MediaLoadData r3);

    void onLoadCompleted(EventTime r1, LoadEventInfo r2, MediaLoadData r3);

    void onLoadError(EventTime r1, LoadEventInfo r2, MediaLoadData r3, IOException r4, boolean r5);

    void onLoadStarted(EventTime r1, LoadEventInfo r2, MediaLoadData r3);

    @Deprecated
    void onLoadingChanged(EventTime r1, boolean r2);

    void onMaxSeekToPreviousPositionChanged(EventTime r1, long r2);

    void onMediaItemTransition(EventTime r1, MediaItem r2, int r3);

    void onMediaMetadataChanged(EventTime r1, MediaMetadata r2);

    void onMetadata(EventTime r1, Metadata r2);

    void onPlayWhenReadyChanged(EventTime r1, boolean r2, int r3);

    void onPlaybackParametersChanged(EventTime r1, PlaybackParameters r2);

    void onPlaybackStateChanged(EventTime r1, int r2);

    void onPlaybackSuppressionReasonChanged(EventTime r1, int r2);

    void onPlayerError(EventTime r1, PlaybackException r2);

    void onPlayerErrorChanged(EventTime r1, PlaybackException r2);

    void onPlayerReleased(EventTime r1);

    @Deprecated
    void onPlayerStateChanged(EventTime r1, boolean r2, int r3);

    void onPlaylistMetadataChanged(EventTime r1, MediaMetadata r2);

    @Deprecated
    void onPositionDiscontinuity(EventTime r1, int r2);

    void onPositionDiscontinuity(EventTime r1, Player.PositionInfo r2, Player.PositionInfo r3, int r4);

    void onRenderedFirstFrame(EventTime r1, Object r2, long r3);

    void onRendererReadyChanged(EventTime r1, int r2, int r3, boolean r4);

    void onRepeatModeChanged(EventTime r1, int r2);

    void onSeekBackIncrementChanged(EventTime r1, long r2);

    void onSeekForwardIncrementChanged(EventTime r1, long r2);

    @Deprecated
    void onSeekStarted(EventTime r1);

    void onShuffleModeChanged(EventTime r1, boolean r2);

    void onSkipSilenceEnabledChanged(EventTime r1, boolean r2);

    void onSurfaceSizeChanged(EventTime r1, int r2, int r3);

    void onTimelineChanged(EventTime r1, int r2);

    void onTrackSelectionParametersChanged(EventTime r1, TrackSelectionParameters r2);

    void onTracksChanged(EventTime r1, Tracks r2);

    void onUpstreamDiscarded(EventTime r1, MediaLoadData r2);

    void onVideoCodecError(EventTime r1, Exception r2);

    @Deprecated
    void onVideoDecoderInitialized(EventTime r1, String r2, long r3);

    void onVideoDecoderInitialized(EventTime r1, String r2, long r3, long r5);

    void onVideoDecoderReleased(EventTime r1, String r2);

    void onVideoDisabled(EventTime r1, DecoderCounters r2);

    void onVideoEnabled(EventTime r1, DecoderCounters r2);

    void onVideoFrameProcessingOffset(EventTime r1, long r2, int r4);

    void onVideoInputFormatChanged(EventTime r1, Format r2, DecoderReuseEvaluation r3);

    @Deprecated
    void onVideoSizeChanged(EventTime r1, int r2, int r3, int r4, float r5);

    void onVideoSizeChanged(EventTime r1, VideoSize r2);

    void onVolumeChanged(EventTime r1, float r2);
}
