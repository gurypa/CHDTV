package androidx.media3.exoplayer.util;

import android.os.SystemClock;
import android.text.TextUtils;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C0180C;
import androidx.media3.common.DeviceInfo;
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
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.DecoderReuseEvaluation;
import androidx.media3.exoplayer.analytics.AnalyticsListener;
import androidx.media3.exoplayer.audio.AudioSink;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MediaLoadData;
import androidx.media3.exoplayer.trackselection.MappingTrackSelector;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class EventLogger implements AnalyticsListener {
    private static final String DEFAULT_TAG = "EventLogger";
    private static final int MAX_TIMELINE_ITEM_LINES = 3;
    private static final NumberFormat TIME_FORMAT = null;
    private final Timeline.Period period;
    private final long startTimeMs;
    private final String tag;
    private final Timeline.Window window;

    static {
        TIME_FORMAT = NumberFormat.getInstance(Locale.US);
        TIME_FORMAT.setMinimumFractionDigits(2);
        TIME_FORMAT.setMaximumFractionDigits(2);
        TIME_FORMAT.setGroupingUsed(false);
    }

    public EventLogger() {
        this(DEFAULT_TAG);
    }

    @Deprecated
    public EventLogger(MappingTrackSelector r2) {
        this(DEFAULT_TAG);
    }

    @Deprecated
    public EventLogger(MappingTrackSelector r1, String r2) {
        this(r2);
    }

    public EventLogger(String r3) {
        this.tag = r3;
        this.window = new Timeline.Window();
        this.period = new Timeline.Period();
        this.startTimeMs = SystemClock.elapsedRealtime();
    }

    private static String getAudioTrackConfigString(AudioSink.AudioTrackConfig r3) {
        return r3.encoding + "," + r3.channelConfig + "," + r3.sampleRate + "," + r3.tunneling + "," + r3.offload + "," + r3.bufferSize;
    }

    private static String getDiscontinuityReasonString(int r1) {
        switch(r1) {
            case 0: goto L17;
            case 1: goto L15;
            case 2: goto L13;
            case 3: goto L11;
            case 4: goto L9;
            case 5: goto L7;
            case 6: goto L5;
            default: goto L3;
        };
    L3:
        return "?";
    L5:
        return "SILENCE_SKIP";
    L7:
        return "INTERNAL";
    L9:
        return "REMOVE";
    L11:
        return "SKIP";
    L13:
        return "SEEK_ADJUSTMENT";
    L15:
        return "SEEK";
    L17:
        return "AUTO_TRANSITION";
    }

    private String getEventString(AnalyticsListener.EventTime r6, String r7, String r8, Throwable r9) {
        String r0 = r7 + " [" + getEventTimeString(r6);
        if ((r9 instanceof PlaybackException) == false) goto L5;
        r0 = r0 + ", errorCode=" + ((PlaybackException) r9).getErrorCodeName();
    L5:
        if (r8 == null) goto L7;
        r0 = r0 + ", " + r8;
    L7:
        String r1 = Log.getThrowableString(r9);
        if (TextUtils.isEmpty(r1) == true) goto L11;
        r0 = r0 + "\n  " + r1.replace("\n", "\n  ") + '\n';
    L11:
        return r0 + "]";
    }

    private String getEventTimeString(AnalyticsListener.EventTime r7) {
        String r0 = "window=" + r7.windowIndex;
        if (r7.mediaPeriodId == null) goto L8;
        r0 = r0 + ", period=" + r7.timeline.getIndexOfPeriod(r7.mediaPeriodId.periodUid);
        if (r7.mediaPeriodId.isAd() == false) goto L8;
        r0 = (r0 + ", adGroup=" + r7.mediaPeriodId.adGroupIndex) + ", ad=" + r7.mediaPeriodId.adIndexInAdGroup;
    L8:
        return "eventTime=" + getTimeString(r7.realtimeMs - this.startTimeMs) + ", mediaPos=" + getTimeString(r7.eventPlaybackPositionMs) + ", " + r0;
    }

    private static String getMediaItemTransitionReasonString(int r1) {
        switch(r1) {
            case 0: goto L11;
            case 1: goto L9;
            case 2: goto L7;
            case 3: goto L5;
            default: goto L3;
        };
    L3:
        return "?";
    L5:
        return "PLAYLIST_CHANGED";
    L7:
        return "SEEK";
    L9:
        return "AUTO";
    L11:
        return "REPEAT";
    }

    private static String getPlayWhenReadyChangeReasonString(int r1) {
        switch(r1) {
            case 1: goto L13;
            case 2: goto L11;
            case 3: goto L9;
            case 4: goto L7;
            case 5: goto L5;
            default: goto L3;
        };
    L3:
        return "?";
    L5:
        return "END_OF_MEDIA_ITEM";
    L7:
        return "REMOTE";
    L9:
        return "AUDIO_BECOMING_NOISY";
    L11:
        return "AUDIO_FOCUS_LOSS";
    L13:
        return "USER_REQUEST";
    }

    private static String getPlaybackSuppressionReasonString(int r1) {
        switch(r1) {
            case 0: goto L7;
            case 1: goto L5;
            default: goto L3;
        };
    L3:
        return "?";
    L5:
        return "TRANSIENT_AUDIO_FOCUS_LOSS";
    L7:
        return "NONE";
    }

    private static String getRepeatModeString(int r1) {
        switch(r1) {
            case 0: goto L9;
            case 1: goto L7;
            case 2: goto L5;
            default: goto L3;
        };
    L3:
        return "?";
    L5:
        return "ALL";
    L7:
        return "ONE";
    L9:
        return "OFF";
    }

    private static String getStateString(int r1) {
        switch(r1) {
            case 1: goto L11;
            case 2: goto L9;
            case 3: goto L7;
            case 4: goto L5;
            default: goto L3;
        };
    L3:
        return "?";
    L5:
        return "ENDED";
    L7:
        return "READY";
    L9:
        return "BUFFERING";
    L11:
        return "IDLE";
    }

    private static String getTimeString(long r3) {
        if (r3 != C0180C.TIME_UNSET) goto L6;
        return "?";
    L6:
        return TIME_FORMAT.format(r3 / 1000.0f);
    }

    private static String getTimelineChangeReasonString(int r1) {
        switch(r1) {
            case 0: goto L7;
            case 1: goto L5;
            default: goto L3;
        };
    L3:
        return "?";
    L5:
        return "SOURCE_UPDATE";
    L7:
        return "PLAYLIST_CHANGED";
    }

    private static String getTrackStatusString(boolean r1) {
        if (r1 == false) goto L4;
        return "[X]";
    L4:
        return "[ ]";
    }

    private void logd(AnalyticsListener.EventTime r2, String r3) {
        logd(getEventString(r2, r3, null, null));
    }

    private void logd(AnalyticsListener.EventTime r2, String r3, String r4) {
        logd(getEventString(r2, r3, r4, null));
    }

    private void loge(AnalyticsListener.EventTime r2, String r3, String r4, Throwable r5) {
        loge(getEventString(r2, r3, r4, r5));
    }

    private void loge(AnalyticsListener.EventTime r2, String r3, Throwable r4) {
        loge(getEventString(r2, r3, null, r4));
    }

    private void printInternalError(AnalyticsListener.EventTime r2, String r3, Exception r4) {
        loge(r2, "internalError", r3, r4);
    }

    private void printMetadata(Metadata r4, String r5) {
        int r0 = 0;
    L4:
        if (r0 >= r4.length()) goto L6;
        logd(r5 + r4.get(r0));
        r0 = r0 + 1;
        goto L4
    }

    protected void logd(String r2) {
        Log.m42d(this.tag, r2);
    }

    protected void loge(String r2) {
        Log.m44e(this.tag, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onAudioAttributesChanged(AnalyticsListener.EventTime r4, AudioAttributes r5) {
        logd(r4, "audioAttributes", r5.contentType + "," + r5.flags + "," + r5.usage + "," + r5.allowedCapturePolicy);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onAudioCodecError(AnalyticsListener.EventTime r1, Exception r2) {
        AnalyticsListener.CC.$default$onAudioCodecError(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onAudioDecoderInitialized(AnalyticsListener.EventTime r1, String r2, long r3) {
        AnalyticsListener.CC.$default$onAudioDecoderInitialized(this, r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onAudioDecoderInitialized(AnalyticsListener.EventTime r2, String r3, long r4, long r6) {
        logd(r2, "audioDecoderInitialized", r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onAudioDecoderReleased(AnalyticsListener.EventTime r2, String r3) {
        logd(r2, "audioDecoderReleased", r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onAudioDisabled(AnalyticsListener.EventTime r2, DecoderCounters r3) {
        logd(r2, "audioDisabled");
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onAudioEnabled(AnalyticsListener.EventTime r2, DecoderCounters r3) {
        logd(r2, "audioEnabled");
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onAudioInputFormatChanged(AnalyticsListener.EventTime r3, Format r4, DecoderReuseEvaluation r5) {
        logd(r3, "audioInputFormat", Format.toLogString(r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onAudioPositionAdvancing(AnalyticsListener.EventTime r1, long r2) {
        AnalyticsListener.CC.$default$onAudioPositionAdvancing(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onAudioSessionIdChanged(AnalyticsListener.EventTime r3, int r4) {
        logd(r3, "audioSessionId", Integer.toString(r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onAudioSinkError(AnalyticsListener.EventTime r1, Exception r2) {
        AnalyticsListener.CC.$default$onAudioSinkError(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onAudioTrackInitialized(AnalyticsListener.EventTime r3, AudioSink.AudioTrackConfig r4) {
        logd(r3, "audioTrackInit", getAudioTrackConfigString(r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onAudioTrackReleased(AnalyticsListener.EventTime r3, AudioSink.AudioTrackConfig r4) {
        logd(r3, "audioTrackReleased", getAudioTrackConfigString(r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onAudioUnderrun(AnalyticsListener.EventTime r4, int r5, long r6, long r8) {
        loge(r4, "audioTrackUnderrun", r5 + ", " + r6 + ", " + r8, null);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onAvailableCommandsChanged(AnalyticsListener.EventTime r1, Player.Commands r2) {
        AnalyticsListener.CC.$default$onAvailableCommandsChanged(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onBandwidthEstimate(AnalyticsListener.EventTime r1, int r2, long r3, long r5) {
        AnalyticsListener.CC.$default$onBandwidthEstimate(this, r1, r2, r3, r5);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onCues(AnalyticsListener.EventTime r1, CueGroup r2) {
        AnalyticsListener.CC.$default$onCues(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onCues(AnalyticsListener.EventTime r1, List r2) {
        AnalyticsListener.CC.$default$onCues(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onDeviceInfoChanged(AnalyticsListener.EventTime r1, DeviceInfo r2) {
        AnalyticsListener.CC.$default$onDeviceInfoChanged(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onDeviceVolumeChanged(AnalyticsListener.EventTime r1, int r2, boolean r3) {
        AnalyticsListener.CC.$default$onDeviceVolumeChanged(this, r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onDownstreamFormatChanged(AnalyticsListener.EventTime r3, MediaLoadData r4) {
        logd(r3, "downstreamFormat", Format.toLogString(r4.trackFormat));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onDrmKeysLoaded(AnalyticsListener.EventTime r2) {
        logd(r2, "drmKeysLoaded");
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onDrmKeysRemoved(AnalyticsListener.EventTime r2) {
        logd(r2, "drmKeysRemoved");
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onDrmKeysRestored(AnalyticsListener.EventTime r2) {
        logd(r2, "drmKeysRestored");
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onDrmSessionAcquired(AnalyticsListener.EventTime r1) {
        AnalyticsListener.CC.$default$onDrmSessionAcquired(this, r1);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onDrmSessionAcquired(AnalyticsListener.EventTime r3, int r4) {
        logd(r3, "drmSessionAcquired", "state=" + r4);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onDrmSessionManagerError(AnalyticsListener.EventTime r2, Exception r3) {
        printInternalError(r2, "drmSessionManagerError", r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onDrmSessionReleased(AnalyticsListener.EventTime r2) {
        logd(r2, "drmSessionReleased");
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onDroppedVideoFrames(AnalyticsListener.EventTime r3, int r4, long r5) {
        logd(r3, "droppedFrames", Integer.toString(r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onEvents(Player r1, AnalyticsListener.Events r2) {
        AnalyticsListener.CC.$default$onEvents(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onIsLoadingChanged(AnalyticsListener.EventTime r3, boolean r4) {
        logd(r3, "loading", Boolean.toString(r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onIsPlayingChanged(AnalyticsListener.EventTime r3, boolean r4) {
        logd(r3, "isPlaying", Boolean.toString(r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onLoadCanceled(AnalyticsListener.EventTime r1, LoadEventInfo r2, MediaLoadData r3) {
        AnalyticsListener.CC.$default$onLoadCanceled(this, r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onLoadCompleted(AnalyticsListener.EventTime r1, LoadEventInfo r2, MediaLoadData r3) {
        AnalyticsListener.CC.$default$onLoadCompleted(this, r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onLoadError(AnalyticsListener.EventTime r2, LoadEventInfo r3, MediaLoadData r4, IOException r5, boolean r6) {
        printInternalError(r2, "loadError", r5);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onLoadStarted(AnalyticsListener.EventTime r1, LoadEventInfo r2, MediaLoadData r3) {
        AnalyticsListener.CC.$default$onLoadStarted(this, r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onLoadingChanged(AnalyticsListener.EventTime r1, boolean r2) {
        AnalyticsListener.CC.$default$onLoadingChanged(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onMaxSeekToPreviousPositionChanged(AnalyticsListener.EventTime r1, long r2) {
        AnalyticsListener.CC.$default$onMaxSeekToPreviousPositionChanged(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onMediaItemTransition(AnalyticsListener.EventTime r3, MediaItem r4, int r5) {
        logd("mediaItem [" + getEventTimeString(r3) + ", reason=" + getMediaItemTransitionReasonString(r5) + "]");
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onMediaMetadataChanged(AnalyticsListener.EventTime r1, MediaMetadata r2) {
        AnalyticsListener.CC.$default$onMediaMetadataChanged(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onMetadata(AnalyticsListener.EventTime r3, Metadata r4) {
        logd("metadata [" + getEventTimeString(r3));
        printMetadata(r4, "  ");
        logd("]");
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onPlayWhenReadyChanged(AnalyticsListener.EventTime r3, boolean r4, int r5) {
        logd(r3, "playWhenReady", r4 + ", " + getPlayWhenReadyChangeReasonString(r5));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onPlaybackParametersChanged(AnalyticsListener.EventTime r3, PlaybackParameters r4) {
        logd(r3, "playbackParameters", r4.toString());
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onPlaybackStateChanged(AnalyticsListener.EventTime r3, int r4) {
        logd(r3, "state", getStateString(r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onPlaybackSuppressionReasonChanged(AnalyticsListener.EventTime r3, int r4) {
        logd(r3, "playbackSuppressionReason", getPlaybackSuppressionReasonString(r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onPlayerError(AnalyticsListener.EventTime r2, PlaybackException r3) {
        loge(r2, "playerFailed", r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onPlayerErrorChanged(AnalyticsListener.EventTime r1, PlaybackException r2) {
        AnalyticsListener.CC.$default$onPlayerErrorChanged(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onPlayerReleased(AnalyticsListener.EventTime r1) {
        AnalyticsListener.CC.$default$onPlayerReleased(this, r1);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onPlayerStateChanged(AnalyticsListener.EventTime r1, boolean r2, int r3) {
        AnalyticsListener.CC.$default$onPlayerStateChanged(this, r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onPlaylistMetadataChanged(AnalyticsListener.EventTime r1, MediaMetadata r2) {
        AnalyticsListener.CC.$default$onPlaylistMetadataChanged(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onPositionDiscontinuity(AnalyticsListener.EventTime r1, int r2) {
        AnalyticsListener.CC.$default$onPositionDiscontinuity(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onPositionDiscontinuity(AnalyticsListener.EventTime r12, Player.PositionInfo r13, Player.PositionInfo r14, int r15) {
        StringBuilder r0 = new StringBuilder();
        r0.append("reason=").append(getDiscontinuityReasonString(r15)).append(", PositionInfo:old [").append("mediaItem=").append(r13.mediaItemIndex).append(", period=").append(r13.periodIndex).append(", pos=").append(r13.positionMs);
        if (r13.adGroupIndex == (-1)) goto L5;
        r0.append(", contentPos=").append(r13.contentPositionMs).append(", adGroup=").append(r13.adGroupIndex).append(", ad=").append(r13.adIndexInAdGroup);
    L5:
        r0.append("], PositionInfo:new [").append("mediaItem=").append(r14.mediaItemIndex).append(", period=").append(r14.periodIndex).append(", pos=").append(r14.positionMs);
        if (r14.adGroupIndex == (-1)) goto L8;
        r0.append(", contentPos=").append(r14.contentPositionMs).append(", adGroup=").append(r14.adGroupIndex).append(", ad=").append(r14.adIndexInAdGroup);
    L8:
        r0.append("]");
        logd(r12, "positionDiscontinuity", r0.toString());
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onRenderedFirstFrame(AnalyticsListener.EventTime r3, Object r4, long r5) {
        logd(r3, "renderedFirstFrame", String.valueOf(r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onRendererReadyChanged(AnalyticsListener.EventTime r4, int r5, int r6, boolean r7) {
        logd(r4, "rendererReady", "rendererIndex=" + r5 + ", " + Util.getTrackTypeString(r6) + ", " + r7);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onRepeatModeChanged(AnalyticsListener.EventTime r3, int r4) {
        logd(r3, "repeatMode", getRepeatModeString(r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onSeekBackIncrementChanged(AnalyticsListener.EventTime r1, long r2) {
        AnalyticsListener.CC.$default$onSeekBackIncrementChanged(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onSeekForwardIncrementChanged(AnalyticsListener.EventTime r1, long r2) {
        AnalyticsListener.CC.$default$onSeekForwardIncrementChanged(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onSeekStarted(AnalyticsListener.EventTime r1) {
        AnalyticsListener.CC.$default$onSeekStarted(this, r1);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onShuffleModeChanged(AnalyticsListener.EventTime r3, boolean r4) {
        logd(r3, "shuffleModeEnabled", Boolean.toString(r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onSkipSilenceEnabledChanged(AnalyticsListener.EventTime r3, boolean r4) {
        logd(r3, "skipSilenceEnabled", Boolean.toString(r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onSurfaceSizeChanged(AnalyticsListener.EventTime r3, int r4, int r5) {
        logd(r3, "surfaceSize", r4 + ", " + r5);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onTimelineChanged(AnalyticsListener.EventTime r10, int r11) {
        int r0 = r10.timeline.getPeriodCount();
        int r1 = r10.timeline.getWindowCount();
        logd("timeline [" + getEventTimeString(r10) + ", periodCount=" + r0 + ", windowCount=" + r1 + ", reason=" + getTimelineChangeReasonString(r11));
        int r2 = 0;
    L4:
        if (r2 >= Math.min(r0, 3)) goto L7;
        r10.timeline.getPeriod(r2, this.period);
        logd("  period [" + getTimeString(this.period.getDurationMs()) + "]");
        r2 = r2 + 1;
        goto L4
    L7:
        if (r0 <= 3) goto L9;
        logd("  ...");
    L9:
        int r4 = 0;
    L11:
        if (r4 >= Math.min(r1, 3)) goto L13;
        r10.timeline.getWindow(r4, this.window);
        logd("  window [" + getTimeString(this.window.getDurationMs()) + ", seekable=" + this.window.isSeekable + ", dynamic=" + this.window.isDynamic + "]");
        r4 = r4 + 1;
        goto L11
    L13:
        if (r1 <= 3) goto L15;
        logd("  ...");
    L15:
        logd("]");
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onTrackSelectionParametersChanged(AnalyticsListener.EventTime r1, TrackSelectionParameters r2) {
        AnalyticsListener.CC.$default$onTrackSelectionParametersChanged(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onTracksChanged(AnalyticsListener.EventTime r11, Tracks r12) {
        logd("tracks [" + getEventTimeString(r11));
        ImmutableList<Tracks.Group> r0 = r12.getGroups();
        int r1 = 0;
    L4:
        if (r1 >= r0.size()) goto L10;
        Tracks.Group r2 = r0.get(r1);
        logd("  group [");
        int r5 = 0;
    L7:
        if (r5 >= r2.length) goto L9;
        logd("    " + getTrackStatusString(r2.isTrackSelected(r5)) + " Track:" + r5 + ", " + Format.toLogString(r2.getTrackFormat(r5)) + ", supported=" + Util.getFormatSupportString(r2.getTrackSupport(r5)));
        r5 = r5 + 1;
        goto L7
    L9:
        logd("  ]");
        r1 = r1 + 1;
        goto L4
    L10:
        boolean r3 = false;
        int r4 = 0;
    L11:
        if (r3 == true) goto L27;
        if (r4 >= r0.size()) goto L27;
        Tracks.Group r6 = r0.get(r4);
        int r7 = 0;
    L15:
        if (r3 == true) goto L26;
        if (r7 >= r6.length) goto L26;
        if (r6.isTrackSelected(r7) == false) goto L25;
        Metadata r8 = r6.getTrackFormat(r7).metadata;
        if (r8 == null) goto L25;
        if (r8.length() <= 0) goto L25;
        logd("  Metadata [");
        printMetadata(r8, "    ");
        logd("  ]");
        r3 = true;
    L25:
        r7 = r7 + 1;
    L26:
        r4 = r4 + 1;
    L27:
        logd("]");
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onUpstreamDiscarded(AnalyticsListener.EventTime r3, MediaLoadData r4) {
        logd(r3, "upstreamDiscarded", Format.toLogString(r4.trackFormat));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onVideoCodecError(AnalyticsListener.EventTime r1, Exception r2) {
        AnalyticsListener.CC.$default$onVideoCodecError(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onVideoDecoderInitialized(AnalyticsListener.EventTime r1, String r2, long r3) {
        AnalyticsListener.CC.$default$onVideoDecoderInitialized(this, r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onVideoDecoderInitialized(AnalyticsListener.EventTime r2, String r3, long r4, long r6) {
        logd(r2, "videoDecoderInitialized", r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onVideoDecoderReleased(AnalyticsListener.EventTime r2, String r3) {
        logd(r2, "videoDecoderReleased", r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onVideoDisabled(AnalyticsListener.EventTime r2, DecoderCounters r3) {
        logd(r2, "videoDisabled");
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onVideoEnabled(AnalyticsListener.EventTime r2, DecoderCounters r3) {
        logd(r2, "videoEnabled");
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onVideoFrameProcessingOffset(AnalyticsListener.EventTime r1, long r2, int r4) {
        AnalyticsListener.CC.$default$onVideoFrameProcessingOffset(this, r1, r2, r4);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onVideoInputFormatChanged(AnalyticsListener.EventTime r3, Format r4, DecoderReuseEvaluation r5) {
        logd(r3, "videoInputFormat", Format.toLogString(r4));
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onVideoSizeChanged(AnalyticsListener.EventTime r1, int r2, int r3, int r4, float r5) {
        AnalyticsListener.CC.$default$onVideoSizeChanged(this, r1, r2, r3, r4, r5);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onVideoSizeChanged(AnalyticsListener.EventTime r3, VideoSize r4) {
        logd(r3, "videoSize", r4.width + ", " + r4.height);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onVolumeChanged(AnalyticsListener.EventTime r3, float r4) {
        logd(r3, "volume", Float.toString(r4));
    }
}
