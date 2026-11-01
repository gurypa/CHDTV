package androidx.media3.exoplayer.util;

import android.os.Looper;
import android.widget.TextView;
import androidx.core.os.EnvironmentCompat;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.ColorInfo;
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
import androidx.media3.common.util.Assertions;
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.ExoPlayer;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class DebugTextViewHelper {
    private static final int REFRESH_INTERVAL_MS = 1000;
    private final ExoPlayer player;
    private boolean started;
    private final TextView textView;
    private final Updater updater;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.util.DebugTextViewHelper$1 */
    static /* synthetic */ class C03781 {
    }

    private final class Updater implements Player.Listener, Runnable {
        final /* synthetic */ DebugTextViewHelper this$0;

        private Updater(DebugTextViewHelper r1) {
            this.this$0 = r1;
        }

        /* synthetic */ Updater(DebugTextViewHelper r1, C03781 r2) {
            this(r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onAudioAttributesChanged(AudioAttributes r1) {
            Player.Listener.CC.$default$onAudioAttributesChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onAudioSessionIdChanged(int r1) {
            Player.Listener.CC.$default$onAudioSessionIdChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onAvailableCommandsChanged(Player.Commands r1) {
            Player.Listener.CC.$default$onAvailableCommandsChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onCues(CueGroup r1) {
            Player.Listener.CC.$default$onCues(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onCues(List r1) {
            Player.Listener.CC.$default$onCues(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onDeviceInfoChanged(DeviceInfo r1) {
            Player.Listener.CC.$default$onDeviceInfoChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onDeviceVolumeChanged(int r1, boolean r2) {
            Player.Listener.CC.$default$onDeviceVolumeChanged(this, r1, r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onEvents(Player r1, Player.Events r2) {
            Player.Listener.CC.$default$onEvents(this, r1, r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onIsLoadingChanged(boolean r1) {
            Player.Listener.CC.$default$onIsLoadingChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onIsPlayingChanged(boolean r1) {
            Player.Listener.CC.$default$onIsPlayingChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onLoadingChanged(boolean r1) {
            Player.Listener.CC.$default$onLoadingChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onMaxSeekToPreviousPositionChanged(long r1) {
            Player.Listener.CC.$default$onMaxSeekToPreviousPositionChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onMediaItemTransition(MediaItem r1, int r2) {
            Player.Listener.CC.$default$onMediaItemTransition(this, r1, r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onMediaMetadataChanged(MediaMetadata r1) {
            Player.Listener.CC.$default$onMediaMetadataChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onMetadata(Metadata r1) {
            Player.Listener.CC.$default$onMetadata(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlayWhenReadyChanged(boolean r2, int r3) {
            this.this$0.updateAndPost();
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onPlaybackParametersChanged(PlaybackParameters r1) {
            Player.Listener.CC.$default$onPlaybackParametersChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlaybackStateChanged(int r2) {
            this.this$0.updateAndPost();
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onPlaybackSuppressionReasonChanged(int r1) {
            Player.Listener.CC.$default$onPlaybackSuppressionReasonChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onPlayerError(PlaybackException r1) {
            Player.Listener.CC.$default$onPlayerError(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onPlayerErrorChanged(PlaybackException r1) {
            Player.Listener.CC.$default$onPlayerErrorChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onPlayerStateChanged(boolean r1, int r2) {
            Player.Listener.CC.$default$onPlayerStateChanged(this, r1, r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onPlaylistMetadataChanged(MediaMetadata r1) {
            Player.Listener.CC.$default$onPlaylistMetadataChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onPositionDiscontinuity(int r1) {
            Player.Listener.CC.$default$onPositionDiscontinuity(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPositionDiscontinuity(Player.PositionInfo r2, Player.PositionInfo r3, int r4) {
            this.this$0.updateAndPost();
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onRenderedFirstFrame() {
            Player.Listener.CC.$default$onRenderedFirstFrame(this);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onRepeatModeChanged(int r1) {
            Player.Listener.CC.$default$onRepeatModeChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onSeekBackIncrementChanged(long r1) {
            Player.Listener.CC.$default$onSeekBackIncrementChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onSeekForwardIncrementChanged(long r1) {
            Player.Listener.CC.$default$onSeekForwardIncrementChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onShuffleModeEnabledChanged(boolean r1) {
            Player.Listener.CC.$default$onShuffleModeEnabledChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onSkipSilenceEnabledChanged(boolean r1) {
            Player.Listener.CC.$default$onSkipSilenceEnabledChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onSurfaceSizeChanged(int r1, int r2) {
            Player.Listener.CC.$default$onSurfaceSizeChanged(this, r1, r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onTimelineChanged(Timeline r1, int r2) {
            Player.Listener.CC.$default$onTimelineChanged(this, r1, r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onTrackSelectionParametersChanged(TrackSelectionParameters r1) {
            Player.Listener.CC.$default$onTrackSelectionParametersChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onTracksChanged(Tracks r1) {
            Player.Listener.CC.$default$onTracksChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onVideoSizeChanged(VideoSize r1) {
            Player.Listener.CC.$default$onVideoSizeChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onVolumeChanged(float r1) {
            Player.Listener.CC.$default$onVolumeChanged(this, r1);
        }

        @Override // java.lang.Runnable
        public void run() {
            this.this$0.updateAndPost();
        }
    }

    public DebugTextViewHelper(ExoPlayer r3, TextView r4) {
        if (r3.getApplicationLooper() != Looper.getMainLooper()) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        this.player = r3;
        this.textView = r4;
        this.updater = new Updater(this, null);
        return;
    L5:
        r0 = false;
        goto L6
    }

    private static String getColorInfoString(ColorInfo r2) {
        if (r2 != null) goto L4;
    L6:
        return "";
    L4:
        if (r2.isValid() == false) goto L6;
        return " colr:" + r2.toLogString();
    }

    private static String getDecoderCountersBufferCountString(DecoderCounters r2) {
        if (r2 != null) goto L5;
        return "";
    L5:
        r2.ensureUpdated();
        return " sib:" + r2.skippedInputBufferCount + " sb:" + r2.skippedOutputBufferCount + " rb:" + r2.renderedOutputBufferCount + " db:" + r2.droppedBufferCount + " mcdb:" + r2.maxConsecutiveDroppedBufferCount + " dk:" + r2.droppedToKeyframeCount;
    }

    private static String getPixelAspectRatioString(float r5) {
        if (r5 != (-1.0f)) goto L5;
    L8:
        return "";
    L5:
        if (r5 == 1.0f) goto L8;
        return " par:" + String.format(Locale.US, "%.02f", new Object[]{Float.valueOf(r5)});
    }

    private static String getVideoFrameProcessingOffsetAverageString(long r4, int r6) {
        if (r6 != 0) goto L6;
        return "N/A";
    L6:
        return String.valueOf((long) (r4 / ((double) r6)));
    }

    protected String getAudioString() {
        Format r0 = this.player.getAudioFormat();
        DecoderCounters r1 = this.player.getAudioDecoderCounters();
        if (r0 == null) goto L8;
        if (r1 != null) goto L7;
        return "";
    L7:
        return "\n" + r0.sampleMimeType + "(id:" + r0.f26id + " hz:" + r0.sampleRate + " ch:" + r0.channelCount + getDecoderCountersBufferCountString(r1) + ")";
    L8:
        return "";
    }

    protected String getDebugString() {
        return getPlayerStateString() + getVideoString() + getAudioString();
    }

    protected String getPlayerStateString() {
        switch(this.player.getPlaybackState()) {
            case 1: goto L8;
            case 2: goto L7;
            case 3: goto L6;
            case 4: goto L5;
            default: goto L4;
        };
    L4:
        String r0 = EnvironmentCompat.MEDIA_UNKNOWN;
    L10:
        return String.format("playWhenReady:%s playbackState:%s item:%s", new Object[]{Boolean.valueOf(this.player.getPlayWhenReady()), r0, Integer.valueOf(this.player.getCurrentMediaItemIndex())});
    L5:
        r0 = "ended";
        goto L10
    L6:
        r0 = "ready";
        goto L10
    L7:
        r0 = "buffering";
        goto L10
    L8:
        r0 = "idle";
        goto L10
    }

    protected String getVideoString() {
        Format r0 = this.player.getVideoFormat();
        VideoSize r1 = this.player.getVideoSize();
        DecoderCounters r2 = this.player.getVideoDecoderCounters();
        if (r0 == null) goto L8;
        if (r2 != null) goto L7;
        return "";
    L7:
        return "\n" + r0.sampleMimeType + "(id:" + r0.f26id + " r:" + r1.width + "x" + r1.height + getColorInfoString(r0.colorInfo) + getPixelAspectRatioString(r1.pixelWidthHeightRatio) + getDecoderCountersBufferCountString(r2) + " vfpo: " + getVideoFrameProcessingOffsetAverageString(r2.totalVideoFrameProcessingOffsetUs, r2.videoFrameProcessingOffsetCount) + ")";
    L8:
        return "";
    }

    public final void start() {
        if (this.started == false) goto L5;
        return;
    L5:
        this.started = true;
        this.player.addListener(this.updater);
        updateAndPost();
    }

    public final void stop() {
        if (this.started == true) goto L5;
        return;
    L5:
        this.started = false;
        this.player.removeListener(this.updater);
        this.textView.removeCallbacks(this.updater);
    }

    protected final void updateAndPost() {
        this.textView.setText(getDebugString());
        this.textView.removeCallbacks(this.updater);
        this.textView.postDelayed(this.updater, 1000);
    }
}
