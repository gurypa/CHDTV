package androidx.media3.p002ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C0180C;
import androidx.media3.common.DeviceInfo;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaLibraryInfo;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.TrackSelectionOverride;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.Tracks;
import androidx.media3.common.VideoSize;
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.RepeatModeUtil;
import androidx.media3.common.util.Util;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public class PlayerControlView extends FrameLayout {
    public static final int DEFAULT_REPEAT_TOGGLE_MODES = 0;
    public static final int DEFAULT_SHOW_TIMEOUT_MS = 5000;
    public static final int DEFAULT_TIME_BAR_MIN_UPDATE_INTERVAL_MS = 200;
    private static final int MAX_UPDATE_INTERVAL_MS = 1000;
    public static final int MAX_WINDOWS_FOR_MULTI_WINDOW_TIME_BAR = 100;
    private static final float[] PLAYBACK_SPEEDS = null;
    private static final int SETTINGS_AUDIO_TRACK_SELECTION_POSITION = 1;
    private static final int SETTINGS_PLAYBACK_SPEED_POSITION = 0;
    private long[] adGroupTimesMs;
    private final View audioTrackButton;
    private final AudioTrackSelectionAdapter audioTrackSelectionAdapter;
    private final float buttonAlphaDisabled;
    private final float buttonAlphaEnabled;
    private final ComponentListener componentListener;
    private final PlayerControlViewLayoutManager controlViewLayoutManager;
    private long currentWindowOffset;
    private final TextView durationView;
    private long[] extraAdGroupTimesMs;
    private boolean[] extraPlayedAdGroups;
    private final View fastForwardButton;
    private final TextView fastForwardButtonTextView;
    private final StringBuilder formatBuilder;
    private final Formatter formatter;
    private final ImageView fullscreenButton;
    private final String fullscreenEnterContentDescription;
    private final Drawable fullscreenEnterDrawable;
    private final String fullscreenExitContentDescription;
    private final Drawable fullscreenExitDrawable;
    private boolean isAttachedToWindow;
    private boolean isFullscreen;
    private final ImageView minimalFullscreenButton;
    private boolean multiWindowTimeBar;
    private boolean needToHideBars;
    private final ImageView nextButton;
    private OnFullScreenModeChangedListener onFullScreenModeChangedListener;
    private final Drawable pauseButtonDrawable;
    private final Timeline.Period period;
    private final Drawable playButtonDrawable;
    private final ImageView playPauseButton;
    private final PlaybackSpeedAdapter playbackSpeedAdapter;
    private final View playbackSpeedButton;
    private boolean[] playedAdGroups;
    private Player player;
    private final TextView positionView;
    private final ImageView previousButton;
    private ProgressUpdateListener progressUpdateListener;
    private final String repeatAllButtonContentDescription;
    private final Drawable repeatAllButtonDrawable;
    private final String repeatOffButtonContentDescription;
    private final Drawable repeatOffButtonDrawable;
    private final String repeatOneButtonContentDescription;
    private final Drawable repeatOneButtonDrawable;
    private final ImageView repeatToggleButton;
    private int repeatToggleModes;
    private final Resources resources;
    private final View rewindButton;
    private final TextView rewindButtonTextView;
    private boolean scrubbing;
    private final SettingsAdapter settingsAdapter;
    private final View settingsButton;
    private final RecyclerView settingsView;
    private final PopupWindow settingsWindow;
    private final int settingsWindowMargin;
    private boolean showMultiWindowTimeBar;
    private boolean showPlayButtonIfSuppressed;
    private int showTimeoutMs;
    private final ImageView shuffleButton;
    private final Drawable shuffleOffButtonDrawable;
    private final String shuffleOffContentDescription;
    private final Drawable shuffleOnButtonDrawable;
    private final String shuffleOnContentDescription;
    private final ImageView subtitleButton;
    private final Drawable subtitleOffButtonDrawable;
    private final String subtitleOffContentDescription;
    private final Drawable subtitleOnButtonDrawable;
    private final String subtitleOnContentDescription;
    private final TextTrackSelectionAdapter textTrackSelectionAdapter;
    private final TimeBar timeBar;
    private int timeBarMinUpdateIntervalMs;
    private final TrackNameProvider trackNameProvider;
    private final Runnable updateProgressAction;
    private final CopyOnWriteArrayList<VisibilityListener> visibilityListeners;
    private final ImageView vrButton;
    private final Timeline.Window window;

    /* JADX INFO: renamed from: androidx.media3.ui.PlayerControlView$1 */
    static /* synthetic */ class C04521 {
    }

    private final class AudioTrackSelectionAdapter extends TrackSelectionAdapter {
        final /* synthetic */ PlayerControlView this$0;

        private AudioTrackSelectionAdapter(PlayerControlView r1) {
            this.this$0 = r1;
            super(r1);
        }

        /* synthetic */ AudioTrackSelectionAdapter(PlayerControlView r1, C04521 r2) {
            this(r1);
        }

        private boolean hasSelectionOverride(TrackSelectionParameters r4) {
            int r0 = 0;
        L4:
            if (r0 >= this.tracks.size()) goto L10;
            TrackGroup r1 = this.tracks.get(r0).trackGroup.getMediaTrackGroup();
            if (r4.overrides.containsKey(r1) == true) goto L7;
            r0 = r0 + 1;
            goto L4
        L7:
            return true;
        L10:
            return false;
        }

        @Override // androidx.media3.ui.PlayerControlView.TrackSelectionAdapter
        public void init(List<TrackInformation> r7) {
            this.tracks = r7;
            TrackSelectionParameters r0 = ((Player) Assertions.checkNotNull(PlayerControlView.access$1600(this.this$0))).getTrackSelectionParameters();
            if (r7.isEmpty() == false) goto L6;
            PlayerControlView.access$2900(this.this$0).setSubTextAtPosition(1, this.this$0.getResources().getString(C0467R.string.exo_track_selection_none));
            return;
        L6:
            if (hasSelectionOverride(r0) == true) goto L8;
            PlayerControlView.access$2900(this.this$0).setSubTextAtPosition(1, this.this$0.getResources().getString(C0467R.string.exo_track_selection_auto));
            return;
        L8:
            int r1 = 0;
        L10:
            if (r1 >= r7.size()) goto L20;
            TrackInformation r3 = r7.get(r1);
            if (r3.isSelected() == true) goto L13;
            r1 = r1 + 1;
            goto L10
        L13:
            PlayerControlView.access$2900(this.this$0).setSubTextAtPosition(1, r3.trackName);
            return;
        }

        /* JADX INFO: renamed from: lambda$onBindViewHolderAtZeroPosition$0$androidx-media3-ui-PlayerControlView$AudioTrackSelectionAdapter */
        /* synthetic */ void m170xa84b12b0(View r6) {
            if (PlayerControlView.access$1600(this.this$0) != null) goto L5;
            return;
        L5:
            if (PlayerControlView.access$1600(this.this$0).isCommandAvailable(29) == false) goto L10;
            TrackSelectionParameters r0 = PlayerControlView.access$1600(this.this$0).getTrackSelectionParameters();
            ((Player) Util.castNonNull(PlayerControlView.access$1600(this.this$0))).setTrackSelectionParameters(r0.buildUpon().clearOverridesOfType(1).setTrackTypeDisabled(1, false).build());
            PlayerControlView.access$2900(this.this$0).setSubTextAtPosition(1, this.this$0.getResources().getString(C0467R.string.exo_track_selection_auto));
            PlayerControlView.access$4200(this.this$0).dismiss();
            return;
        }

        @Override // androidx.media3.ui.PlayerControlView.TrackSelectionAdapter
        public void onBindViewHolderAtZeroPosition(SubSettingViewHolder r5) {
            r5.textView.setText(C0467R.string.exo_track_selection_auto);
            boolean r1 = hasSelectionOverride(((Player) Assertions.checkNotNull(PlayerControlView.access$1600(this.this$0))).getTrackSelectionParameters());
            View r2 = r5.checkView;
            if (r1 == false) goto L5;
            int r3 = 4;
        L6:
            r2.setVisibility(r3);
            r5.itemView.setOnClickListener(new ViewOnClickListenerC0453x69dd452a(this));
            return;
        L5:
            r3 = 0;
            goto L6
        }

        @Override // androidx.media3.ui.PlayerControlView.TrackSelectionAdapter
        public void onTrackSelection(String r3) {
            PlayerControlView.access$2900(this.this$0).setSubTextAtPosition(1, r3);
        }
    }

    private final class ComponentListener implements Player.Listener, TimeBar.OnScrubListener, View.OnClickListener, PopupWindow.OnDismissListener {
        final /* synthetic */ PlayerControlView this$0;

        private ComponentListener(PlayerControlView r1) {
            this.this$0 = r1;
        }

        /* synthetic */ ComponentListener(PlayerControlView r1, C04521 r2) {
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

        @Override // android.view.View.OnClickListener
        public void onClick(View r5) {
            Player r0 = PlayerControlView.access$1600(this.this$0);
            if (r0 != null) goto L5;
            return;
        L5:
            PlayerControlView.access$1500(this.this$0).resetHideCallbacks();
            if (PlayerControlView.access$1900(this.this$0) != r5) goto L11;
            if (r0.isCommandAvailable(9) == false) goto L53;
            r0.seekToNext();
            return;
        L53:
            return;
        L11:
            if (PlayerControlView.access$2000(this.this$0) != r5) goto L16;
            if (r0.isCommandAvailable(7) == false) goto L55;
            r0.seekToPrevious();
            return;
        L55:
            return;
        L16:
            if (PlayerControlView.access$2100(this.this$0) != r5) goto L23;
            if (r0.getPlaybackState() != 4) goto L20;
            return;
        L20:
            if (r0.isCommandAvailable(12) == false) goto L58;
            r0.seekForward();
            return;
        L58:
            return;
        L23:
            if (PlayerControlView.access$2200(this.this$0) != r5) goto L28;
            if (r0.isCommandAvailable(11) == false) goto L60;
            r0.seekBack();
            return;
        L60:
            return;
        L28:
            if (PlayerControlView.access$2300(this.this$0) != r5) goto L31;
            Util.handlePlayPauseButtonAction(r0, PlayerControlView.access$2400(this.this$0));
            return;
        L31:
            if (PlayerControlView.access$2500(this.this$0) != r5) goto L36;
            if (r0.isCommandAvailable(15) == false) goto L63;
            r0.setRepeatMode(RepeatModeUtil.getNextRepeatMode(r0.getRepeatMode(), PlayerControlView.access$2600(this.this$0)));
            return;
        L63:
            return;
        L36:
            if (PlayerControlView.access$2700(this.this$0) != r5) goto L41;
            if (r0.isCommandAvailable(14) == false) goto L65;
            r0.setShuffleModeEnabled(!r0.getShuffleModeEnabled());
            return;
        L65:
            return;
        L41:
            if (PlayerControlView.access$2800(this.this$0) != r5) goto L44;
            PlayerControlView.access$1500(this.this$0).removeHideCallbacks();
            PlayerControlView.access$3000(this.this$0, PlayerControlView.access$2900(this.this$0), PlayerControlView.access$2800(this.this$0));
            return;
        L44:
            if (PlayerControlView.access$3100(this.this$0) != r5) goto L47;
            PlayerControlView.access$1500(this.this$0).removeHideCallbacks();
            PlayerControlView.access$3000(this.this$0, PlayerControlView.access$3200(this.this$0), PlayerControlView.access$3100(this.this$0));
            return;
        L47:
            if (PlayerControlView.access$3300(this.this$0) != r5) goto L50;
            PlayerControlView.access$1500(this.this$0).removeHideCallbacks();
            PlayerControlView.access$3000(this.this$0, PlayerControlView.access$3400(this.this$0), PlayerControlView.access$3300(this.this$0));
            return;
        L50:
            if (PlayerControlView.access$3500(this.this$0) != r5) goto L70;
            PlayerControlView.access$1500(this.this$0).removeHideCallbacks();
            PlayerControlView.access$3000(this.this$0, PlayerControlView.access$3600(this.this$0), PlayerControlView.access$3500(this.this$0));
            return;
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

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            if (PlayerControlView.access$1800(this.this$0) == false) goto L6;
            PlayerControlView.access$1500(this.this$0).resetHideCallbacks();
            return;
        }

        @Override // androidx.media3.common.Player.Listener
        public void onEvents(Player r5, Player.Events r6) {
            if (r6.containsAny(new int[]{4, 5, 13}) == false) goto L6;
            PlayerControlView.access$300(this.this$0);
        L6:
            if (r6.containsAny(new int[]{4, 5, 7, 13}) == false) goto L9;
            PlayerControlView.access$400(this.this$0);
        L9:
            if (r6.containsAny(new int[]{8, 13}) == false) goto L12;
            PlayerControlView.access$500(this.this$0);
        L12:
            if (r6.containsAny(new int[]{9, 13}) == false) goto L15;
            PlayerControlView.access$600(this.this$0);
        L15:
            if (r6.containsAny(new int[]{8, 9, 11, 0, 16, 17, 13}) == false) goto L18;
            PlayerControlView.access$700(this.this$0);
        L18:
            if (r6.containsAny(new int[]{11, 0, 13}) == false) goto L21;
            PlayerControlView.access$800(this.this$0);
        L21:
            if (r6.containsAny(new int[]{12, 13}) == false) goto L24;
            PlayerControlView.access$900(this.this$0);
        L24:
            if (r6.containsAny(new int[]{2, 13}) == false) goto L27;
            PlayerControlView.access$1000(this.this$0);
            return;
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
        public /* synthetic */ void onPlayWhenReadyChanged(boolean r1, int r2) {
            Player.Listener.CC.$default$onPlayWhenReadyChanged(this, r1, r2);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onPlaybackParametersChanged(PlaybackParameters r1) {
            Player.Listener.CC.$default$onPlaybackParametersChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onPlaybackStateChanged(int r1) {
            Player.Listener.CC.$default$onPlaybackStateChanged(this, r1);
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
        public /* synthetic */ void onPositionDiscontinuity(Player.PositionInfo r1, Player.PositionInfo r2, int r3) {
            Player.Listener.CC.$default$onPositionDiscontinuity(this, r1, r2, r3);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onRenderedFirstFrame() {
            Player.Listener.CC.$default$onRenderedFirstFrame(this);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onRepeatModeChanged(int r1) {
            Player.Listener.CC.$default$onRepeatModeChanged(this, r1);
        }

        @Override // androidx.media3.ui.TimeBar.OnScrubListener
        public void onScrubMove(TimeBar r4, long r5) {
            if (PlayerControlView.access$1200(this.this$0) == null) goto L6;
            PlayerControlView.access$1200(this.this$0).setText(Util.getStringForTime(PlayerControlView.access$1300(this.this$0), PlayerControlView.access$1400(this.this$0), r5));
            return;
        }

        @Override // androidx.media3.ui.TimeBar.OnScrubListener
        public void onScrubStart(TimeBar r4, long r5) {
            PlayerControlView.access$1102(this.this$0, true);
            if (PlayerControlView.access$1200(this.this$0) == null) goto L5;
            PlayerControlView.access$1200(this.this$0).setText(Util.getStringForTime(PlayerControlView.access$1300(this.this$0), PlayerControlView.access$1400(this.this$0), r5));
        L5:
            PlayerControlView.access$1500(this.this$0).removeHideCallbacks();
        }

        @Override // androidx.media3.ui.TimeBar.OnScrubListener
        public void onScrubStop(TimeBar r3, long r4, boolean r6) {
            PlayerControlView.access$1102(this.this$0, false);
            if (r6 == false) goto L5;
        L7:
            PlayerControlView.access$1500(this.this$0).resetHideCallbacks();
            return;
        L5:
            if (PlayerControlView.access$1600(this.this$0) == null) goto L7;
            PlayerControlView.access$1700(this.this$0, PlayerControlView.access$1600(this.this$0), r4);
            goto L7
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
    }

    @Deprecated
    public interface OnFullScreenModeChangedListener {
        void onFullScreenModeChanged(boolean r1);
    }

    private final class PlaybackSpeedAdapter extends RecyclerView.Adapter<SubSettingViewHolder> {
        private final String[] playbackSpeedTexts;
        private final float[] playbackSpeeds;
        private int selectedIndex;
        final /* synthetic */ PlayerControlView this$0;

        public PlaybackSpeedAdapter(PlayerControlView r1, String[] r2, float[] r3) {
            this.this$0 = r1;
            this.playbackSpeedTexts = r2;
            this.playbackSpeeds = r3;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.playbackSpeedTexts.length;
        }

        public String getSelectedText() {
            return this.playbackSpeedTexts[this.selectedIndex];
        }

        /* JADX INFO: renamed from: lambda$onBindViewHolder$0$androidx-media3-ui-PlayerControlView$PlaybackSpeedAdapter */
        /* synthetic */ void m171x9de2ddb7(int r3, View r4) {
            if (r3 == this.selectedIndex) goto L5;
            PlayerControlView.access$4100(this.this$0, this.playbackSpeeds[r3]);
        L5:
            PlayerControlView.access$4200(this.this$0).dismiss();
        }

        public void onBindViewHolder(SubSettingViewHolder r4, final int r5) {
            if (r5 >= this.playbackSpeedTexts.length) goto L6;
            r4.textView.setText(this.playbackSpeedTexts[r5]);
        L6:
            if (r5 != this.selectedIndex) goto L8;
            r4.itemView.setSelected(true);
            r4.checkView.setVisibility(0);
        L9:
            r4.itemView.setOnClickListener(new PlayerControlView$PlaybackSpeedAdapter$$ExternalSyntheticLambda0(this, r5));
            return;
        L8:
            r4.itemView.setSelected(false);
            r4.checkView.setVisibility(4);
            goto L9
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder r1, int r2) {
            onBindViewHolder((SubSettingViewHolder) r1, r2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public SubSettingViewHolder onCreateViewHolder(ViewGroup r4, int r5) {
            return new SubSettingViewHolder(LayoutInflater.from(this.this$0.getContext()).inflate(C0467R.layout.exo_styled_sub_settings_list_item, r4, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public /* bridge */ /* synthetic */ RecyclerView.ViewHolder onCreateViewHolder(ViewGroup r1, int r2) {
            return onCreateViewHolder(r1, r2);
        }

        public void updateSelectedIndex(float r6) {
            int r0 = 0;
            float r1 = Float.MAX_VALUE;
            int r2 = 0;
        L4:
            if (r2 >= this.playbackSpeeds.length) goto L9;
            float r3 = Math.abs(r6 - this.playbackSpeeds[r2]);
            if (r3 >= r1) goto L8;
            r0 = r2;
            r1 = r3;
        L8:
            r2 = r2 + 1;
            goto L4
        L9:
            this.selectedIndex = r0;
        }
    }

    public interface ProgressUpdateListener {
        void onProgressUpdate(long r1, long r3);
    }

    private final class SettingViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iconView;
        private final TextView mainTextView;
        private final TextView subTextView;
        final /* synthetic */ PlayerControlView this$0;

        public SettingViewHolder(PlayerControlView r2, View r3) {
            this.this$0 = r2;
            super(r3);
            if (Util.SDK_INT >= 26) goto L5;
            r3.setFocusable(true);
        L5:
            this.mainTextView = (TextView) r3.findViewById(C0467R.id.exo_main_text);
            this.subTextView = (TextView) r3.findViewById(C0467R.id.exo_sub_text);
            this.iconView = (ImageView) r3.findViewById(C0467R.id.exo_icon);
            r3.setOnClickListener(new PlayerControlView$SettingViewHolder$$ExternalSyntheticLambda0(this));
        }

        static /* synthetic */ TextView access$3700(SettingViewHolder r1) {
            return r1.mainTextView;
        }

        static /* synthetic */ TextView access$3800(SettingViewHolder r1) {
            return r1.subTextView;
        }

        static /* synthetic */ ImageView access$3900(SettingViewHolder r1) {
            return r1.iconView;
        }

        /* JADX INFO: renamed from: lambda$new$0$androidx-media3-ui-PlayerControlView$SettingViewHolder */
        /* synthetic */ void m172x7eeeb754(View r3) {
            PlayerControlView.access$4000(this.this$0, getBindingAdapterPosition());
        }
    }

    private class SettingsAdapter extends RecyclerView.Adapter<SettingViewHolder> {
        private final Drawable[] iconIds;
        private final String[] mainTexts;
        private final String[] subTexts;
        final /* synthetic */ PlayerControlView this$0;

        public SettingsAdapter(PlayerControlView r1, String[] r2, Drawable[] r3) {
            this.this$0 = r1;
            this.mainTexts = r2;
            this.subTexts = new String[r2.length];
            this.iconIds = r3;
        }

        private boolean shouldShowSetting(int r5) {
            if (PlayerControlView.access$1600(this.this$0) != null) goto L6;
            return false;
        L6:
            switch(r5) {
                case 0: goto L16;
                case 1: goto L9;
                default: goto L7;
            };
        L7:
            return true;
        L9:
            if (PlayerControlView.access$1600(this.this$0).isCommandAvailable(30) == true) goto L11;
            return false;
        L11:
            if (PlayerControlView.access$1600(this.this$0).isCommandAvailable(29) == true) goto L14;
            return false;
        L14:
            return true;
        L16:
            return PlayerControlView.access$1600(this.this$0).isCommandAvailable(13);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mainTexts.length;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int r3) {
            return r3;
        }

        public boolean hasSettingsToShow() {
            if (shouldShowSetting(1) == false) goto L5;
            return true;
        L5:
            if (shouldShowSetting(0) == false) goto L9;
            return true;
        L9:
            return false;
        }

        public void onBindViewHolder(SettingViewHolder r5, int r6) {
            if (shouldShowSetting(r6) == false) goto L5;
            r5.itemView.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
        L6:
            SettingViewHolder.access$3700(r5).setText(this.mainTexts[r6]);
            if (this.subTexts[r6] != null) goto L9;
            SettingViewHolder.access$3800(r5).setVisibility(8);
        L11:
            if (this.iconIds[r6] != null) goto L13;
            SettingViewHolder.access$3900(r5).setVisibility(8);
            return;
        L13:
            SettingViewHolder.access$3900(r5).setImageDrawable(this.iconIds[r6]);
            return;
        L9:
            SettingViewHolder.access$3800(r5).setText(this.subTexts[r6]);
            goto L11
        L5:
            r5.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            goto L6
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder r1, int r2) {
            onBindViewHolder((SettingViewHolder) r1, r2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public SettingViewHolder onCreateViewHolder(ViewGroup r4, int r5) {
            View r0 = LayoutInflater.from(this.this$0.getContext()).inflate(C0467R.layout.exo_styled_settings_list_item, r4, false);
            return new SettingViewHolder(this.this$0, r0);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public /* bridge */ /* synthetic */ RecyclerView.ViewHolder onCreateViewHolder(ViewGroup r1, int r2) {
            return onCreateViewHolder(r1, r2);
        }

        public void setSubTextAtPosition(int r2, String r3) {
            this.subTexts[r2] = r3;
        }
    }

    private static class SubSettingViewHolder extends RecyclerView.ViewHolder {
        public final View checkView;
        public final TextView textView;

        public SubSettingViewHolder(View r3) {
            super(r3);
            if (Util.SDK_INT >= 26) goto L5;
            r3.setFocusable(true);
        L5:
            this.textView = (TextView) r3.findViewById(C0467R.id.exo_text);
            this.checkView = r3.findViewById(C0467R.id.exo_check);
        }
    }

    private final class TextTrackSelectionAdapter extends TrackSelectionAdapter {
        final /* synthetic */ PlayerControlView this$0;

        private TextTrackSelectionAdapter(PlayerControlView r1) {
            this.this$0 = r1;
            super(r1);
        }

        /* synthetic */ TextTrackSelectionAdapter(PlayerControlView r1, C04521 r2) {
            this(r1);
        }

        @Override // androidx.media3.ui.PlayerControlView.TrackSelectionAdapter
        public void init(List<TrackInformation> r4) {
            boolean r0 = false;
            int r1 = 0;
        L4:
            if (r1 >= r4.size()) goto L10;
            if (r4.get(r1).isSelected() == true) goto L7;
            r1 = r1 + 1;
            goto L4
        L7:
            r0 = true;
        L10:
            if (PlayerControlView.access$3500(this.this$0) == null) goto L20;
            ImageView r2 = PlayerControlView.access$3500(this.this$0);
            PlayerControlView r3 = this.this$0;
            if (r0 == false) goto L14;
            Drawable r5 = PlayerControlView.access$4300(r3);
        L15:
            r2.setImageDrawable(r5);
            ImageView r6 = PlayerControlView.access$3500(this.this$0);
            PlayerControlView r7 = this.this$0;
            if (r0 == false) goto L18;
            String r8 = PlayerControlView.access$4500(r7);
        L19:
            r6.setContentDescription(r8);
            goto L20
        L18:
            r8 = PlayerControlView.access$4600(r7);
            goto L19
        L14:
            r5 = PlayerControlView.access$4400(r3);
        L20:
            this.tracks = r4;
        }

        /* JADX INFO: renamed from: lambda$onBindViewHolderAtZeroPosition$0$androidx-media3-ui-PlayerControlView$TextTrackSelectionAdapter */
        /* synthetic */ void m173x7bd5d809(View r5) {
            if (PlayerControlView.access$1600(this.this$0) != null) goto L5;
            return;
        L5:
            if (PlayerControlView.access$1600(this.this$0).isCommandAvailable(29) == false) goto L9;
            TrackSelectionParameters r0 = PlayerControlView.access$1600(this.this$0).getTrackSelectionParameters();
            PlayerControlView.access$1600(this.this$0).setTrackSelectionParameters(r0.buildUpon().clearOverridesOfType(3).setIgnoredTextSelectionFlags(-3).setPreferredTextLanguage(null).setPreferredTextRoleFlags(0).build());
            PlayerControlView.access$4200(this.this$0).dismiss();
            return;
        }

        @Override // androidx.media3.ui.PlayerControlView.TrackSelectionAdapter
        public void onBindViewHolder(SubSettingViewHolder r4, int r5) {
            super.onBindViewHolder(r4, r5);
            if (r5 <= 0) goto L10;
            TrackInformation r0 = this.tracks.get(r5 - 1);
            View r1 = r4.checkView;
            if (r0.isSelected() == false) goto L7;
            int r2 = 0;
        L8:
            r1.setVisibility(r2);
            return;
        L7:
            r2 = 4;
            goto L8
        }

        @Override // androidx.media3.ui.PlayerControlView.TrackSelectionAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder r1, int r2) {
            onBindViewHolder((SubSettingViewHolder) r1, r2);
        }

        @Override // androidx.media3.ui.PlayerControlView.TrackSelectionAdapter
        public void onBindViewHolderAtZeroPosition(SubSettingViewHolder r4) {
            r4.textView.setText(C0467R.string.exo_track_selection_none);
            boolean r0 = true;
            int r1 = 0;
        L4:
            if (r1 >= this.tracks.size()) goto L9;
            if (this.tracks.get(r1).isSelected() == true) goto L7;
            r1 = r1 + 1;
            goto L4
        L7:
            r0 = false;
        L9:
            View r2 = r4.checkView;
            if (r0 == false) goto L12;
            int r3 = 0;
        L13:
            r2.setVisibility(r3);
            r4.itemView.setOnClickListener(new ViewOnClickListenerC0454xa4a308d(this));
            return;
        L12:
            r3 = 4;
            goto L13
        }

        @Override // androidx.media3.ui.PlayerControlView.TrackSelectionAdapter
        public void onTrackSelection(String r1) {
        }
    }

    private static final class TrackInformation {
        public final Tracks.Group trackGroup;
        public final int trackIndex;
        public final String trackName;

        public TrackInformation(Tracks r2, int r3, int r4, String r5) {
            this.trackGroup = r2.getGroups().get(r3);
            this.trackIndex = r4;
            this.trackName = r5;
        }

        public boolean isSelected() {
            return this.trackGroup.isTrackSelected(this.trackIndex);
        }
    }

    private abstract class TrackSelectionAdapter extends RecyclerView.Adapter<SubSettingViewHolder> {
        final /* synthetic */ PlayerControlView this$0;
        protected List<TrackInformation> tracks;

        protected TrackSelectionAdapter(PlayerControlView r1) {
            this.this$0 = r1;
            this.tracks = new ArrayList();
        }

        protected void clear() {
            this.tracks = Collections.emptyList();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (this.tracks.isEmpty() == false) goto L6;
            return 0;
        L6:
            return this.tracks.size() + 1;
        }

        public abstract void init(List<TrackInformation> r1);

        /* JADX INFO: renamed from: lambda$onBindViewHolder$0$androidx-media3-ui-PlayerControlView$TrackSelectionAdapter */
        /* synthetic */ void m174x45c3fb1a(Player r5, TrackGroup r6, TrackInformation r7, View r8) {
            if (r5.isCommandAvailable(29) == true) goto L5;
            return;
        L5:
            r5.setTrackSelectionParameters(r5.getTrackSelectionParameters().buildUpon().setOverrideForType(new TrackSelectionOverride(r6, ImmutableList.m213of(Integer.valueOf(r7.trackIndex)))).setTrackTypeDisabled(r7.trackGroup.getType(), false).build());
            onTrackSelection(r7.trackName);
            PlayerControlView.access$4200(this.this$0).dismiss();
        }

        public void onBindViewHolder(SubSettingViewHolder r9, int r10) {
            final Player r0 = PlayerControlView.access$1600(this.this$0);
            if (r0 != null) goto L5;
            return;
        L5:
            if (r10 != 0) goto L7;
            onBindViewHolderAtZeroPosition(r9);
            return;
        L7:
            final TrackInformation r1 = this.tracks.get(r10 - 1);
            final TrackGroup r2 = r1.trackGroup.getMediaTrackGroup();
            int r5 = 0;
            if (r0.getTrackSelectionParameters().overrides.get(r2) != null) goto L10;
        L12:
            boolean r4 = false;
        L13:
            r9.textView.setText(r1.trackName);
            View r6 = r9.checkView;
            if (r4 == true) goto L17;
            r5 = 4;
        L17:
            r6.setVisibility(r5);
            r9.itemView.setOnClickListener(new ViewOnClickListenerC0455xc7413ffa(this, r0, r2, r1));
            return;
        L10:
            if (r1.isSelected() == false) goto L12;
            r4 = true;
            goto L13
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder r1, int r2) {
            onBindViewHolder((SubSettingViewHolder) r1, r2);
        }

        protected abstract void onBindViewHolderAtZeroPosition(SubSettingViewHolder r1);

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public SubSettingViewHolder onCreateViewHolder(ViewGroup r4, int r5) {
            return new SubSettingViewHolder(LayoutInflater.from(this.this$0.getContext()).inflate(C0467R.layout.exo_styled_sub_settings_list_item, r4, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public /* bridge */ /* synthetic */ RecyclerView.ViewHolder onCreateViewHolder(ViewGroup r1, int r2) {
            return onCreateViewHolder(r1, r2);
        }

        protected abstract void onTrackSelection(String r1);
    }

    @Deprecated
    public interface VisibilityListener {
        void onVisibilityChange(int r1);
    }

    public static /* synthetic */ void $r8$lambda$KhCtrIPwnfekhKTmchWRbwQURM4(PlayerControlView r0, View r1, int r2, int r3, int r4, int r5, int r6, int r7, int r8, int r9) {
        r0.onLayoutChange(r1, r2, r3, r4, r5, r6, r7, r8, r9);
    }

    public static /* synthetic */ void $r8$lambda$fA_GCUtqcqNfkXcjodDFOHMvm0o(PlayerControlView r0) {
        r0.updateProgress();
    }

    public static /* synthetic */ void $r8$lambda$mIBsYqyl1jVkZycUoNSySZ3hOj8(PlayerControlView r0, View r1) {
        r0.onFullscreenButtonClicked(r1);
    }

    static {
        MediaLibraryInfo.registerModule("media3.ui");
        PLAYBACK_SPEEDS = new float[]{0.25f, 0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f};
    }

    public PlayerControlView(Context r2) {
        this(r2, null);
    }

    public PlayerControlView(Context r2, AttributeSet r3) {
        this(r2, r3, 0);
    }

    public PlayerControlView(Context r1, AttributeSet r2, int r3) {
        this(r1, r2, r3, r2);
    }

    public PlayerControlView(Context r45, AttributeSet r46, int r47, AttributeSet r48) {
        super(r45, r46, r47);
        int r2 = C0467R.layout.exo_player_control_view;
        int r3 = C0467R.drawable.exo_styled_controls_play;
        int r4 = C0467R.drawable.exo_styled_controls_pause;
        int r5 = C0467R.drawable.exo_styled_controls_next;
        int r7 = C0467R.drawable.exo_styled_controls_simple_fastforward;
        int r8 = C0467R.drawable.exo_styled_controls_previous;
        int r9 = C0467R.drawable.exo_styled_controls_simple_rewind;
        int r10 = C0467R.drawable.exo_styled_controls_fullscreen_exit;
        int r11 = C0467R.drawable.exo_styled_controls_fullscreen_enter;
        int r12 = C0467R.drawable.exo_styled_controls_repeat_off;
        int r13 = C0467R.drawable.exo_styled_controls_repeat_one;
        int r14 = C0467R.drawable.exo_styled_controls_repeat_all;
        int r15 = C0467R.drawable.exo_styled_controls_shuffle_on;
        int r16 = C0467R.drawable.exo_styled_controls_shuffle_off;
        int r17 = C0467R.drawable.exo_styled_controls_subtitle_on;
        int r18 = C0467R.drawable.exo_styled_controls_subtitle_off;
        int r19 = C0467R.drawable.exo_styled_controls_vr;
        this.showPlayButtonIfSuppressed = true;
        this.showTimeoutMs = 5000;
        this.repeatToggleModes = 0;
        this.timeBarMinUpdateIntervalMs = 200;
        if (r48 == null) goto L83;
        TypedArray r1 = r45.getTheme().obtainStyledAttributes(r48, C0467R.styleable.PlayerControlView, r47, 0);
        r2 = r1.getResourceId(C0467R.styleable.PlayerControlView_controller_layout_id, r2);     // Catch: Throwable -> L79
        r3 = r1.getResourceId(C0467R.styleable.PlayerControlView_play_icon, r3);     // Catch: Throwable -> L79
        r4 = r1.getResourceId(C0467R.styleable.PlayerControlView_pause_icon, r4);     // Catch: Throwable -> L79
        r5 = r1.getResourceId(C0467R.styleable.PlayerControlView_next_icon, r5);     // Catch: Throwable -> L79
        r7 = r1.getResourceId(C0467R.styleable.PlayerControlView_fastforward_icon, r7);     // Catch: Throwable -> L79
        int r0 = r1.getResourceId(C0467R.styleable.PlayerControlView_previous_icon, r8);     // Catch: Throwable -> L79
        int r6 = r1.getResourceId(C0467R.styleable.PlayerControlView_rewind_icon, r9);     // Catch: Throwable -> L79
        int r20 = r1.getResourceId(C0467R.styleable.PlayerControlView_fullscreen_exit_icon, r10);     // Catch: Throwable -> L79
        int r21 = r1.getResourceId(C0467R.styleable.PlayerControlView_fullscreen_enter_icon, r11);     // Catch: Throwable -> L79
        int r22 = r1.getResourceId(C0467R.styleable.PlayerControlView_repeat_off_icon, r12);     // Catch: Throwable -> L79
        int r23 = r1.getResourceId(C0467R.styleable.PlayerControlView_repeat_one_icon, r13);     // Catch: Throwable -> L79
        int r24 = r1.getResourceId(C0467R.styleable.PlayerControlView_repeat_all_icon, r14);     // Catch: Throwable -> L77
        int r25 = r15;
        r25 = r1.getResourceId(C0467R.styleable.PlayerControlView_shuffle_on_icon, r25);     // Catch: Throwable -> L75
        int r26 = r16;
        r26 = r1.getResourceId(C0467R.styleable.PlayerControlView_shuffle_off_icon, r26);     // Catch: Throwable -> L73
        int r27 = r17;
        r27 = r1.getResourceId(C0467R.styleable.PlayerControlView_subtitle_on_icon, r27);     // Catch: Throwable -> L71
        int r28 = r18;
        r28 = r1.getResourceId(C0467R.styleable.PlayerControlView_subtitle_off_icon, r28);     // Catch: Throwable -> L69
        int r29 = r1.getResourceId(C0467R.styleable.PlayerControlView_vr_icon, r19);     // Catch: Throwable -> L67
        int r30 = r4;
        final PlayerControlView r31 = this;
        r31.showTimeoutMs = r1.getInt(C0467R.styleable.PlayerControlView_show_timeout, r31.showTimeoutMs);     // Catch: Throwable -> L63
        r31.repeatToggleModes = getRepeatToggleModes(r1, r31.repeatToggleModes);     // Catch: Throwable -> L63
        boolean r32 = true;
        r32 = r1.getBoolean(C0467R.styleable.PlayerControlView_show_rewind_button, true);     // Catch: Throwable -> L61
        boolean r33 = true;
        r33 = r1.getBoolean(C0467R.styleable.PlayerControlView_show_fastforward_button, true);     // Catch: Throwable -> L59
        boolean r34 = true;
        r34 = r1.getBoolean(C0467R.styleable.PlayerControlView_show_previous_button, true);     // Catch: Throwable -> L57
        boolean r35 = true;
        r35 = r1.getBoolean(C0467R.styleable.PlayerControlView_show_next_button, true);     // Catch: Throwable -> L55
        boolean r36 = false;
        r36 = r1.getBoolean(C0467R.styleable.PlayerControlView_show_shuffle_button, false);     // Catch: Throwable -> L53
        boolean r37 = false;
        r37 = r1.getBoolean(C0467R.styleable.PlayerControlView_show_subtitle_button, false);     // Catch: Throwable -> L51
        boolean r38 = r1.getBoolean(C0467R.styleable.PlayerControlView_show_vr_button, false);     // Catch: Throwable -> L49
        r31.setTimeBarMinUpdateInterval(r1.getInt(C0467R.styleable.PlayerControlView_time_bar_min_update_interval, r31.timeBarMinUpdateIntervalMs));     // Catch: Throwable -> L45
        boolean r39 = r1.getBoolean(C0467R.styleable.PlayerControlView_animation_enabled, true);     // Catch: Throwable -> L43
        r1.recycle();
        boolean r40 = r39;
        int r310 = r23;
        boolean r41 = r32;
        boolean r110 = r38;
        int r311 = r29;
        int r42 = r0;
        int r312 = r6;
        int r313 = r20;
        int r314 = r21;
        int r315 = r22;
        int r316 = r24;
        int r43 = r25;
        int r44 = r26;
        int r49 = r27;
        int r410 = r28;
        boolean r50 = r33;
        boolean r51 = r34;
        boolean r111 = r35;
        boolean r112 = r36;
        boolean r113 = r37;
        int r114 = r2;
    L84:
        int r115 = r42;
        LayoutInflater.from(r45).inflate(r114, r31);
        r31.setDescendantFocusability(262144);
        int r116 = r5;
        r31.componentListener = new ComponentListener(r31, null);
        r31.visibilityListeners = new CopyOnWriteArrayList();
        r31.period = new Timeline.Period();
        r31.window = new Timeline.Window();
        r31.formatBuilder = new StringBuilder();
        int r117 = r3;
        r31.formatter = new Formatter(r31.formatBuilder, Locale.getDefault());
        r31.adGroupTimesMs = new long[0];
        r31.playedAdGroups = new boolean[0];
        r31.extraAdGroupTimesMs = new long[0];
        r31.extraPlayedAdGroups = new boolean[0];
        r31.updateProgressAction = new PlayerControlView$$ExternalSyntheticLambda0(r31);
        r31.durationView = (TextView) r31.findViewById(C0467R.id.exo_duration);
        r31.positionView = (TextView) r31.findViewById(C0467R.id.exo_position);
        r31.subtitleButton = (ImageView) r31.findViewById(C0467R.id.exo_subtitle);
        if (r31.subtitleButton == null) goto L87;
        r31.subtitleButton.setOnClickListener(r31.componentListener);
    L87:
        r31.fullscreenButton = (ImageView) r31.findViewById(C0467R.id.exo_fullscreen);
        initializeFullscreenButton(r31.fullscreenButton, new PlayerControlView$$ExternalSyntheticLambda1(r31));
        r31.minimalFullscreenButton = (ImageView) r31.findViewById(C0467R.id.exo_minimal_fullscreen);
        initializeFullscreenButton(r31.minimalFullscreenButton, new PlayerControlView$$ExternalSyntheticLambda1(r31));
        r31.settingsButton = r31.findViewById(C0467R.id.exo_settings);
        if (r31.settingsButton == null) goto L90;
        r31.settingsButton.setOnClickListener(r31.componentListener);
    L90:
        r31.playbackSpeedButton = r31.findViewById(C0467R.id.exo_playback_speed);
        if (r31.playbackSpeedButton == null) goto L93;
        r31.playbackSpeedButton.setOnClickListener(r31.componentListener);
    L93:
        r31.audioTrackButton = r31.findViewById(C0467R.id.exo_audio_track);
        if (r31.audioTrackButton == null) goto L96;
        r31.audioTrackButton.setOnClickListener(r31.componentListener);
    L96:
        TimeBar r52 = (TimeBar) r31.findViewById(C0467R.id.exo_progress);
        View r53 = r31.findViewById(C0467R.id.exo_progress_placeholder);
        if (r52 == null) goto L99;
        r31.timeBar = r52;
        int r118 = r117;
        int r119 = r116;
        View r54 = r53;
        Context r55 = r45;
        final PlayerControlView r120 = r31;
        int r121 = r7;
    L103:
        if (r120.timeBar == null) goto L105;
        r120.timeBar.addListener(r120.componentListener);
    L105:
        r120.resources = r55.getResources();
        r120.playPauseButton = (ImageView) r120.findViewById(C0467R.id.exo_play_pause);
        if (r120.playPauseButton == null) goto L108;
        r120.playPauseButton.setOnClickListener(r120.componentListener);
    L108:
        r120.previousButton = (ImageView) r120.findViewById(C0467R.id.exo_prev);
        if (r120.previousButton == null) goto L111;
        r120.previousButton.setImageDrawable(Util.getDrawable(r55, r120.resources, r115));
        r120.previousButton.setOnClickListener(r120.componentListener);
    L111:
        r120.nextButton = (ImageView) r120.findViewById(C0467R.id.exo_next);
        if (r120.nextButton == null) goto L114;
        r120.nextButton.setImageDrawable(Util.getDrawable(r55, r120.resources, r119));
        r120.nextButton.setOnClickListener(r120.componentListener);
    L114:
        Typeface r56 = ResourcesCompat.getFont(r55, C0467R.font.roboto_medium_numbers);
        ImageView r57 = (ImageView) r120.findViewById(C0467R.id.exo_rew);
        TextView r58 = (TextView) r120.findViewById(C0467R.id.exo_rew_with_amount);
        if (r57 == null) goto L118;
        r57.setImageDrawable(Util.getDrawable(r55, r120.resources, r312));
        r120.rewindButton = r57;
        r120.rewindButtonTextView = null;
    L122:
        if (r120.rewindButton == null) goto L125;
        r120.rewindButton.setOnClickListener(r120.componentListener);
    L125:
        ImageView r59 = (ImageView) r120.findViewById(C0467R.id.exo_ffwd);
        TextView r60 = (TextView) r120.findViewById(C0467R.id.exo_ffwd_with_amount);
        if (r59 == null) goto L129;
        r59.setImageDrawable(Util.getDrawable(r55, r120.resources, r121));
        r120.fastForwardButton = r59;
        r120.fastForwardButtonTextView = null;
    L133:
        if (r120.fastForwardButton == null) goto L136;
        r120.fastForwardButton.setOnClickListener(r120.componentListener);
    L136:
        r120.repeatToggleButton = (ImageView) r120.findViewById(C0467R.id.exo_repeat_toggle);
        if (r120.repeatToggleButton == null) goto L139;
        r120.repeatToggleButton.setOnClickListener(r120.componentListener);
    L139:
        r120.shuffleButton = (ImageView) r120.findViewById(C0467R.id.exo_shuffle);
        if (r120.shuffleButton == null) goto L142;
        r120.shuffleButton.setOnClickListener(r120.componentListener);
    L142:
        r120.buttonAlphaEnabled = r120.resources.getInteger(C0467R.integer.exo_media_button_opacity_percentage_enabled) / 100.0f;
        r120.buttonAlphaDisabled = r120.resources.getInteger(C0467R.integer.exo_media_button_opacity_percentage_disabled) / 100.0f;
        r120.vrButton = (ImageView) r120.findViewById(C0467R.id.exo_vr);
        if (r120.vrButton == null) goto L145;
        int r61 = r311;
        r120.vrButton.setImageDrawable(Util.getDrawable(r55, r120.resources, r61));
        r120.updateButton(false, r120.vrButton);
    L146:
        r120.controlViewLayoutManager = new PlayerControlViewLayoutManager(r120);
        r120.controlViewLayoutManager.setAnimationEnabled(r40);
        r120.settingsAdapter = new SettingsAdapter(r120, new String[]{r120.resources.getString(C0467R.string.exo_controls_playback_speed), r120.resources.getString(C0467R.string.exo_track_selection_title_audio)}, new Drawable[]{Util.getDrawable(r55, r120.resources, C0467R.drawable.exo_styled_controls_speed), Util.getDrawable(r55, r120.resources, C0467R.drawable.exo_styled_controls_audiotrack)});
        r120.settingsWindowMargin = r120.resources.getDimensionPixelSize(C0467R.dimen.exo_settings_offset);
        r120.settingsView = (RecyclerView) LayoutInflater.from(r55).inflate(C0467R.layout.exo_styled_settings_list, null);
        r120.settingsView.setAdapter(r120.settingsAdapter);
        r120.settingsView.setLayoutManager(new LinearLayoutManager(r120.getContext()));
        r120.settingsWindow = new PopupWindow(r120.settingsView, -2, -2, true);
        if (Util.SDK_INT >= 23) goto L150;
        r120.settingsWindow.setBackgroundDrawable(new ColorDrawable(0));
    L150:
        r120.settingsWindow.setOnDismissListener(r120.componentListener);
        r120.needToHideBars = true;
        r120.trackNameProvider = new DefaultTrackNameProvider(r120.getResources());
        r120.subtitleOnButtonDrawable = Util.getDrawable(r55, r120.resources, r49);
        r120.subtitleOffButtonDrawable = Util.getDrawable(r55, r120.resources, r410);
        r120.subtitleOnContentDescription = r120.resources.getString(C0467R.string.exo_controls_cc_enabled_description);
        r120.subtitleOffContentDescription = r120.resources.getString(C0467R.string.exo_controls_cc_disabled_description);
        C04521 r62 = null;
        r120.textTrackSelectionAdapter = new TextTrackSelectionAdapter(r120, r62);
        r120.audioTrackSelectionAdapter = new AudioTrackSelectionAdapter(r120, r62);
        r120.playbackSpeedAdapter = new PlaybackSpeedAdapter(r120, r120.resources.getStringArray(C0467R.array.exo_controls_playback_speeds), PLAYBACK_SPEEDS);
        r120.playButtonDrawable = Util.getDrawable(r55, r120.resources, r118);
        r120.pauseButtonDrawable = Util.getDrawable(r55, r120.resources, r30);
        r120.fullscreenExitDrawable = Util.getDrawable(r55, r120.resources, r313);
        r120.fullscreenEnterDrawable = Util.getDrawable(r55, r120.resources, r314);
        r120.repeatOffButtonDrawable = Util.getDrawable(r55, r120.resources, r315);
        r120.repeatOneButtonDrawable = Util.getDrawable(r55, r120.resources, r310);
        r120.repeatAllButtonDrawable = Util.getDrawable(r55, r120.resources, r316);
        r120.shuffleOnButtonDrawable = Util.getDrawable(r55, r120.resources, r43);
        r120.shuffleOffButtonDrawable = Util.getDrawable(r55, r120.resources, r44);
        r120.fullscreenExitContentDescription = r120.resources.getString(C0467R.string.exo_controls_fullscreen_exit_description);
        r120.fullscreenEnterContentDescription = r120.resources.getString(C0467R.string.exo_controls_fullscreen_enter_description);
        r120.repeatOffButtonContentDescription = r120.resources.getString(C0467R.string.exo_controls_repeat_off_description);
        r120.repeatOneButtonContentDescription = r120.resources.getString(C0467R.string.exo_controls_repeat_one_description);
        r120.repeatAllButtonContentDescription = r120.resources.getString(C0467R.string.exo_controls_repeat_all_description);
        r120.shuffleOnContentDescription = r120.resources.getString(C0467R.string.exo_controls_shuffle_on_description);
        r120.shuffleOffContentDescription = r120.resources.getString(C0467R.string.exo_controls_shuffle_off_description);
        r120.controlViewLayoutManager.setShowButton((ViewGroup) r120.findViewById(C0467R.id.exo_bottom_bar), true);
        r120.controlViewLayoutManager.setShowButton(r120.fastForwardButton, r50);
        r120.controlViewLayoutManager.setShowButton(r120.rewindButton, r41);
        r120.controlViewLayoutManager.setShowButton(r120.previousButton, r51);
        r120.controlViewLayoutManager.setShowButton(r120.nextButton, r111);
        r120.controlViewLayoutManager.setShowButton(r120.shuffleButton, r112);
        r120.controlViewLayoutManager.setShowButton(r120.subtitleButton, r113);
        r120.controlViewLayoutManager.setShowButton(r120.vrButton, r110);
        PlayerControlViewLayoutManager r63 = r120.controlViewLayoutManager;
        ImageView r64 = r120.repeatToggleButton;
        if (r120.repeatToggleModes == 0) goto L153;
        boolean r65 = true;
    L154:
        r63.setShowButton(r64, r65);
        r120.addOnLayoutChangeListener(new PlayerControlView$$ExternalSyntheticLambda2(r120));
        return;
    L153:
        r65 = false;
        goto L154
    L145:
        r61 = r311;
        goto L146
    L129:
        if (r60 == null) goto L131;
        r60.setTypeface(r56);
        r120.fastForwardButtonTextView = r60;
        r120.fastForwardButton = r120.fastForwardButtonTextView;
        goto L133
    L131:
        r120.fastForwardButtonTextView = null;
        r120.fastForwardButton = null;
        goto L133
    L118:
        if (r58 == null) goto L120;
        r58.setTypeface(r56);
        r120.rewindButtonTextView = r58;
        r120.rewindButton = r120.rewindButtonTextView;
        goto L122
    L120:
        r120.rewindButtonTextView = null;
        r120.rewindButton = null;
        goto L122
    L99:
        if (r53 == null) goto L101;
        r118 = r117;
        r119 = r116;
        r121 = r7;
        r120 = this;
        r55 = r45;
        DefaultTimeBar r66 = new DefaultTimeBar(r55, null, 0, r48, C0467R.style.ExoStyledControls_TimeBar);
        r66.setId(C0467R.id.exo_progress);
        r66.setLayoutParams(r53.getLayoutParams());
        ViewGroup r67 = (ViewGroup) r53.getParent();
        r54 = r53;
        int r68 = r67.indexOfChild(r54);
        r67.removeView(r54);
        r67.addView(r66, r68);
        r120.timeBar = r66;
        goto L103
    L101:
        r118 = r117;
        r119 = r116;
        r54 = r53;
        r55 = r45;
        r120 = r31;
        r121 = r7;
        r120.timeBar = null;
    L43:
        th = th;
    L81:
        r1.recycle();
        throw th;
    L45:
        th = th;
    L47:
        th = th;
    L49:
        th = th;
    L51:
        th = th;
    L53:
        th = th;
    L55:
        th = th;
    L57:
        th = th;
    L59:
        th = th;
    L61:
        th = th;
    L63:
        th = th;
    L65:
        th = th;
    L67:
        th = th;
    L69:
        th = th;
    L71:
        th = th;
    L73:
        th = th;
    L75:
        th = th;
    L77:
        th = th;
    L79:
        th = th;
        goto L81
    L83:
        r30 = r4;
        r31 = this;
        r310 = r13;
        r311 = r19;
        r40 = true;
        r41 = true;
        r110 = false;
        r312 = r9;
        r313 = r10;
        r314 = r11;
        r315 = r12;
        r316 = r14;
        r43 = r15;
        r44 = r16;
        r49 = r17;
        r410 = r18;
        r51 = true;
        r111 = true;
        r112 = false;
        r113 = false;
        r114 = r2;
        r42 = r8;
        r50 = true;
        goto L84
    }

    static /* synthetic */ void access$1000(PlayerControlView r0) {
        r0.updateTrackLists();
    }

    static /* synthetic */ boolean access$1102(PlayerControlView r0, boolean r1) {
        r0.scrubbing = r1;
        return r1;
    }

    static /* synthetic */ TextView access$1200(PlayerControlView r1) {
        return r1.positionView;
    }

    static /* synthetic */ StringBuilder access$1300(PlayerControlView r1) {
        return r1.formatBuilder;
    }

    static /* synthetic */ Formatter access$1400(PlayerControlView r1) {
        return r1.formatter;
    }

    static /* synthetic */ PlayerControlViewLayoutManager access$1500(PlayerControlView r1) {
        return r1.controlViewLayoutManager;
    }

    static /* synthetic */ Player access$1600(PlayerControlView r1) {
        return r1.player;
    }

    static /* synthetic */ void access$1700(PlayerControlView r0, Player r1, long r2) {
        r0.seekToTimeBarPosition(r1, r2);
    }

    static /* synthetic */ boolean access$1800(PlayerControlView r1) {
        return r1.needToHideBars;
    }

    static /* synthetic */ ImageView access$1900(PlayerControlView r1) {
        return r1.nextButton;
    }

    static /* synthetic */ ImageView access$2000(PlayerControlView r1) {
        return r1.previousButton;
    }

    static /* synthetic */ View access$2100(PlayerControlView r1) {
        return r1.fastForwardButton;
    }

    static /* synthetic */ View access$2200(PlayerControlView r1) {
        return r1.rewindButton;
    }

    static /* synthetic */ ImageView access$2300(PlayerControlView r1) {
        return r1.playPauseButton;
    }

    static /* synthetic */ boolean access$2400(PlayerControlView r1) {
        return r1.showPlayButtonIfSuppressed;
    }

    static /* synthetic */ ImageView access$2500(PlayerControlView r1) {
        return r1.repeatToggleButton;
    }

    static /* synthetic */ int access$2600(PlayerControlView r1) {
        return r1.repeatToggleModes;
    }

    static /* synthetic */ ImageView access$2700(PlayerControlView r1) {
        return r1.shuffleButton;
    }

    static /* synthetic */ View access$2800(PlayerControlView r1) {
        return r1.settingsButton;
    }

    static /* synthetic */ SettingsAdapter access$2900(PlayerControlView r1) {
        return r1.settingsAdapter;
    }

    static /* synthetic */ void access$300(PlayerControlView r0) {
        r0.updatePlayPauseButton();
    }

    static /* synthetic */ void access$3000(PlayerControlView r0, RecyclerView.Adapter r1, View r2) {
        r0.displaySettingsWindow(r1, r2);
    }

    static /* synthetic */ View access$3100(PlayerControlView r1) {
        return r1.playbackSpeedButton;
    }

    static /* synthetic */ PlaybackSpeedAdapter access$3200(PlayerControlView r1) {
        return r1.playbackSpeedAdapter;
    }

    static /* synthetic */ View access$3300(PlayerControlView r1) {
        return r1.audioTrackButton;
    }

    static /* synthetic */ AudioTrackSelectionAdapter access$3400(PlayerControlView r1) {
        return r1.audioTrackSelectionAdapter;
    }

    static /* synthetic */ ImageView access$3500(PlayerControlView r1) {
        return r1.subtitleButton;
    }

    static /* synthetic */ TextTrackSelectionAdapter access$3600(PlayerControlView r1) {
        return r1.textTrackSelectionAdapter;
    }

    static /* synthetic */ void access$400(PlayerControlView r0) {
        r0.updateProgress();
    }

    static /* synthetic */ void access$4000(PlayerControlView r0, int r1) {
        r0.onSettingViewClicked(r1);
    }

    static /* synthetic */ void access$4100(PlayerControlView r0, float r1) {
        r0.setPlaybackSpeed(r1);
    }

    static /* synthetic */ PopupWindow access$4200(PlayerControlView r1) {
        return r1.settingsWindow;
    }

    static /* synthetic */ Drawable access$4300(PlayerControlView r1) {
        return r1.subtitleOnButtonDrawable;
    }

    static /* synthetic */ Drawable access$4400(PlayerControlView r1) {
        return r1.subtitleOffButtonDrawable;
    }

    static /* synthetic */ String access$4500(PlayerControlView r1) {
        return r1.subtitleOnContentDescription;
    }

    static /* synthetic */ String access$4600(PlayerControlView r1) {
        return r1.subtitleOffContentDescription;
    }

    static /* synthetic */ void access$500(PlayerControlView r0) {
        r0.updateRepeatModeButton();
    }

    static /* synthetic */ void access$600(PlayerControlView r0) {
        r0.updateShuffleButton();
    }

    static /* synthetic */ void access$700(PlayerControlView r0) {
        r0.updateNavigation();
    }

    static /* synthetic */ void access$800(PlayerControlView r0) {
        r0.updateTimeline();
    }

    static /* synthetic */ void access$900(PlayerControlView r0) {
        r0.updatePlaybackSpeedList();
    }

    private static boolean canShowMultiWindowTimeBar(Player r9, Timeline.Window r10) {
        if (r9.isCommandAvailable(17) == true) goto L5;
        return false;
    L5:
        Timeline r0 = r9.getCurrentTimeline();
        int r2 = r0.getWindowCount();
        if (r2 > 1) goto L8;
    L17:
        return false;
    L8:
        if (r2 > 100) goto L17;
        int r4 = 0;
    L11:
        if (r4 >= r2) goto L16;
        if (r0.getWindow(r4, r10).durationUs == C0180C.TIME_UNSET) goto L14;
        r4 = r4 + 1;
        goto L11
    L14:
        return false;
    L16:
        return true;
    }

    private void displaySettingsWindow(RecyclerView.Adapter<?> r4, View r5) {
        this.settingsView.setAdapter(r4);
        updateSettingsWindowSize();
        this.needToHideBars = false;
        this.settingsWindow.dismiss();
        this.needToHideBars = true;
        int r0 = (getWidth() - this.settingsWindow.getWidth()) - this.settingsWindowMargin;
        int r1 = (-this.settingsWindow.getHeight()) - this.settingsWindowMargin;
        this.settingsWindow.showAsDropDown(r5, r0, r1);
    }

    private ImmutableList<TrackInformation> gatherSupportedTrackInfosOfType(Tracks r9, int r10) {
        ImmutableList.Builder r0 = new ImmutableList.Builder();
        ImmutableList<Tracks.Group> r1 = r9.getGroups();
        int r2 = 0;
    L4:
        if (r2 >= r1.size()) goto L21;
        Tracks.Group r3 = r1.get(r2);
        if (r3.getType() != r10) goto L19;
        int r4 = 0;
    L10:
        if (r4 >= r3.length) goto L19;
        if (r3.isTrackSupported(r4) == false) goto L18;
        Format r5 = r3.getTrackFormat(r4);
        if ((r5.selectionFlags & 2) != 0) goto L18;
        r0.add(new TrackInformation(r9, r2, r4, this.trackNameProvider.getTrackName(r5)));
    L18:
        r4 = r4 + 1;
    L19:
        r2 = r2 + 1;
        goto L4
    L21:
        return r0.build();
    }

    private static int getRepeatToggleModes(TypedArray r1, int r2) {
        return r1.getInt(C0467R.styleable.PlayerControlView_repeat_toggle_modes, r2);
    }

    private void initTrackSelectionAdapter() {
        this.textTrackSelectionAdapter.clear();
        this.audioTrackSelectionAdapter.clear();
        if (this.player != null) goto L5;
        return;
    L5:
        if (this.player.isCommandAvailable(30) == true) goto L7;
        return;
    L7:
        if (this.player.isCommandAvailable(29) == false) goto L17;
        Tracks r0 = this.player.getCurrentTracks();
        this.audioTrackSelectionAdapter.init(gatherSupportedTrackInfosOfType(r0, 1));
        if (this.controlViewLayoutManager.getShowButton(this.subtitleButton) == false) goto L12;
        this.textTrackSelectionAdapter.init(gatherSupportedTrackInfosOfType(r0, 3));
        return;
    L12:
        this.textTrackSelectionAdapter.init(ImmutableList.m212of());
        return;
    }

    private static void initializeFullscreenButton(View r1, View.OnClickListener r2) {
        if (r1 != null) goto L4;
        return;
    L4:
        r1.setVisibility(8);
        r1.setOnClickListener(r2);
    }

    private static boolean isHandledMediaKey(int r1) {
        if (r1 != 90) goto L5;
    L20:
        return true;
    L5:
        if (r1 == 89) goto L20;
        if (r1 == 85) goto L20;
        if (r1 == 79) goto L20;
        if (r1 == 126) goto L20;
        if (r1 == 127) goto L20;
        if (r1 == 87) goto L20;
        if (r1 == 88) goto L20;
        return false;
    }

    private void onFullscreenButtonClicked(View r2) {
        updateIsFullscreen(!this.isFullscreen);
    }

    private void onLayoutChange(View r13, int r14, int r15, int r16, int r17, int r18, int r19, int r20, int r21) {
        int r1 = r17 - r15;
        int r3 = r21 - r19;
        if ((r16 - r14) != (r20 - r18)) goto L6;
        if (r1 != r3) goto L6;
        return;
    L6:
        if (this.settingsWindow.isShowing() == false) goto L10;
        updateSettingsWindowSize();
        int r8 = (getWidth() - this.settingsWindow.getWidth()) - this.settingsWindowMargin;
        int r9 = (-this.settingsWindow.getHeight()) - this.settingsWindowMargin;
        this.settingsWindow.update(r13, r8, r9, -1, -1);
        return;
    }

    private void onSettingViewClicked(int r3) {
        if (r3 != 0) goto L5;
        displaySettingsWindow(this.playbackSpeedAdapter, (View) Assertions.checkNotNull(this.settingsButton));
        return;
    L5:
        if (r3 != 1) goto L7;
        displaySettingsWindow(this.audioTrackSelectionAdapter, (View) Assertions.checkNotNull(this.settingsButton));
        return;
    L7:
        this.settingsWindow.dismiss();
    }

    private void seekToTimeBarPosition(Player r7, long r8) {
        if (this.multiWindowTimeBar == false) goto L18;
        if (r7.isCommandAvailable(17) == true) goto L7;
    L20:
        updateProgress();
        return;
    L7:
        if (r7.isCommandAvailable(10) == false) goto L20;
        Timeline r0 = r7.getCurrentTimeline();
        int r1 = r0.getWindowCount();
        int r2 = 0;
    L9:
        long r3 = r0.getWindow(r2, this.window).getDurationMs();
        if (r8 < r3) goto L15;
        if (r2 == (r1 - 1)) goto L14;
        r8 = r8 - r3;
        r2 = r2 + 1;
        goto L9
    L14:
        r8 = r3;
    L15:
        r7.seekTo(r2, r8);
        goto L20
    L18:
        if (r7.isCommandAvailable(5) == false) goto L20;
        r7.seekTo(r8);
        goto L20
    }

    private void setPlaybackSpeed(float r3) {
        if (this.player != null) goto L5;
        return;
    L5:
        if (this.player.isCommandAvailable(13) == false) goto L10;
        this.player.setPlaybackParameters(this.player.getPlaybackParameters().withSpeed(r3));
        return;
    }

    private boolean shouldEnablePlayPauseButton() {
        if (this.player != null) goto L5;
    L11:
        return false;
    L5:
        if (this.player.isCommandAvailable(1) == false) goto L11;
        if (this.player.isCommandAvailable(17) == true) goto L9;
        return true;
    L9:
        if (this.player.getCurrentTimeline().isEmpty() == true) goto L11;
        return true;
    }

    private void updateButton(boolean r2, View r3) {
        if (r3 != null) goto L4;
        return;
    L4:
        r3.setEnabled(r2);
        if (r2 == false) goto L7;
        float r0 = this.buttonAlphaEnabled;
    L8:
        r3.setAlpha(r0);
        return;
    L7:
        r0 = this.buttonAlphaDisabled;
        goto L8
    }

    private void updateFastForwardButton() {
        if (this.player == null) goto L5;
        long r0 = this.player.getSeekForwardIncrement();
    L6:
        int r2 = (int) (r0 / 1000);
        if (this.fastForwardButtonTextView == null) goto L10;
        this.fastForwardButtonTextView.setText(String.valueOf(r2));
    L10:
        if (this.fastForwardButton == null) goto L13;
        this.fastForwardButton.setContentDescription(this.resources.getQuantityString(C0467R.plurals.exo_controls_fastforward_by_amount_description, r2, new Object[]{Integer.valueOf(r2)}));
        return;
    L13:
        return;
    L5:
        r0 = C0180C.DEFAULT_SEEK_FORWARD_INCREMENT_MS;
        goto L6
    }

    private void updateFullscreenButtonForState(ImageView r2, boolean r3) {
        if (r2 != null) goto L4;
        return;
    L4:
        if (r3 == false) goto L6;
        r2.setImageDrawable(this.fullscreenExitDrawable);
        r2.setContentDescription(this.fullscreenExitContentDescription);
        return;
    L6:
        r2.setImageDrawable(this.fullscreenEnterDrawable);
        r2.setContentDescription(this.fullscreenEnterContentDescription);
    }

    private static void updateFullscreenButtonVisibility(View r1, boolean r2) {
        if (r1 != null) goto L4;
        return;
    L4:
        if (r2 == false) goto L6;
        r1.setVisibility(0);
        return;
    L6:
        r1.setVisibility(8);
    }

    private void updateNavigation() {
        if (isVisible() == true) goto L5;
        return;
    L5:
        if (this.isAttachedToWindow == false) goto L26;
        Player r0 = this.player;
        boolean r1 = false;
        boolean r2 = false;
        boolean r3 = false;
        boolean r4 = false;
        boolean r5 = false;
        if (r0 != null) goto L10;
    L16:
        if (r3 == false) goto L18;
        updateRewindButton();
    L18:
        if (r4 == false) goto L20;
        updateFastForwardButton();
    L20:
        updateButton(r2, this.previousButton);
        updateButton(r3, this.rewindButton);
        updateButton(r4, this.fastForwardButton);
        updateButton(r5, this.nextButton);
        if (this.timeBar == null) goto L25;
        this.timeBar.setEnabled(r1);
        return;
    L25:
        return;
    L10:
        if (this.showMultiWindowTimeBar == true) goto L12;
    L14:
        boolean r6 = r0.isCommandAvailable(5);
    L15:
        r1 = r6;
        r2 = r0.isCommandAvailable(7);
        r3 = r0.isCommandAvailable(11);
        r4 = r0.isCommandAvailable(12);
        r5 = r0.isCommandAvailable(9);
        goto L16
    L12:
        if (canShowMultiWindowTimeBar(r0, this.window) == false) goto L14;
        r6 = r0.isCommandAvailable(10);
        goto L15
    }

    private void updatePlayPauseButton() {
        if (isVisible() == true) goto L5;
        return;
    L5:
        if (this.isAttachedToWindow == true) goto L8;
        return;
    L8:
        if (this.playPauseButton == null) goto L19;
        boolean r0 = Util.shouldShowPlayButton(this.player, this.showPlayButtonIfSuppressed);
        if (r0 == false) goto L12;
        Drawable r1 = this.playButtonDrawable;
    L13:
        if (r0 == false) goto L15;
        int r2 = C0467R.string.exo_controls_play_description;
    L16:
        this.playPauseButton.setImageDrawable(r1);
        this.playPauseButton.setContentDescription(this.resources.getString(r2));
        updateButton(shouldEnablePlayPauseButton(), this.playPauseButton);
        return;
    L15:
        r2 = C0467R.string.exo_controls_pause_description;
        goto L16
    L12:
        r1 = this.pauseButtonDrawable;
        goto L13
    }

    private void updatePlaybackSpeedList() {
        if (this.player != null) goto L5;
        return;
    L5:
        this.playbackSpeedAdapter.updateSelectedIndex(this.player.getPlaybackParameters().speed);
        this.settingsAdapter.setSubTextAtPosition(0, this.playbackSpeedAdapter.getSelectedText());
        updateSettingsButton();
    }

    private void updateProgress() {
        if (isVisible() == true) goto L5;
        return;
    L5:
        if (this.isAttachedToWindow == false) goto L49;
        Player r1 = this.player;
        long r2 = 0;
        long r4 = 0;
        if (r1 == null) goto L13;
        if (r1.isCommandAvailable(16) == false) goto L13;
        r2 = this.currentWindowOffset + r1.getContentPosition();
        r4 = this.currentWindowOffset + r1.getContentBufferedPosition();
    L13:
        if (this.positionView == null) goto L18;
        if (this.scrubbing == true) goto L18;
        this.positionView.setText(Util.getStringForTime(this.formatBuilder, this.formatter, r2));
    L18:
        if (this.timeBar == null) goto L21;
        this.timeBar.setPosition(r2);
        this.timeBar.setBufferedPosition(r4);
    L21:
        if (this.progressUpdateListener == null) goto L23;
        this.progressUpdateListener.onProgressUpdate(r2, r4);
    L23:
        removeCallbacks(this.updateProgressAction);
        if (r1 != null) goto L26;
        int r7 = 1;
    L27:
        long r8 = 1000;
        if (r1 == null) goto L40;
        if (r1.isPlaying() == false) goto L40;
        if (this.timeBar == null) goto L34;
        long r10 = this.timeBar.getPreferredUpdateDelay();
    L35:
        long r11 = Math.min(r10, 1000 - (r2 % 1000));
        float r6 = r1.getPlaybackParameters().speed;
        if (r6 <= 0.0f) goto L38;
        r8 = (long) (r11 / r6);
    L38:
        postDelayed(this.updateProgressAction, Util.constrainValue(r8, this.timeBarMinUpdateIntervalMs, 1000));
        return;
    L34:
        r10 = 1000;
    L40:
        if (r7 == 4) goto L47;
        if (r7 == 1) goto L48;
        postDelayed(this.updateProgressAction, 1000);
        return;
    L48:
        return;
    L47:
        return;
    L26:
        r7 = r1.getPlaybackState();
        goto L27
    }

    private void updateRepeatModeButton() {
        if (isVisible() == true) goto L5;
        return;
    L5:
        if (this.isAttachedToWindow == true) goto L7;
        return;
    L7:
        if (this.repeatToggleButton != null) goto L10;
        return;
    L10:
        if (this.repeatToggleModes != 0) goto L13;
        updateButton(false, this.repeatToggleButton);
        return;
    L13:
        Player r0 = this.player;
        if (r0 != null) goto L16;
    L25:
        updateButton(false, this.repeatToggleButton);
        this.repeatToggleButton.setImageDrawable(this.repeatOffButtonDrawable);
        this.repeatToggleButton.setContentDescription(this.repeatOffButtonContentDescription);
        return;
    L16:
        if (r0.isCommandAvailable(15) == false) goto L25;
        updateButton(true, this.repeatToggleButton);
        switch(r0.getRepeatMode()) {
            case 0: goto L23;
            case 1: goto L22;
            case 2: goto L21;
            default: goto L30;
        };
    L21:
        this.repeatToggleButton.setImageDrawable(this.repeatAllButtonDrawable);
        this.repeatToggleButton.setContentDescription(this.repeatAllButtonContentDescription);
        return;
    L22:
        this.repeatToggleButton.setImageDrawable(this.repeatOneButtonDrawable);
        this.repeatToggleButton.setContentDescription(this.repeatOneButtonContentDescription);
        return;
    L23:
        this.repeatToggleButton.setImageDrawable(this.repeatOffButtonDrawable);
        this.repeatToggleButton.setContentDescription(this.repeatOffButtonContentDescription);
        return;
    }

    private void updateRewindButton() {
        if (this.player == null) goto L5;
        long r0 = this.player.getSeekBackIncrement();
    L6:
        int r2 = (int) (r0 / 1000);
        if (this.rewindButtonTextView == null) goto L10;
        this.rewindButtonTextView.setText(String.valueOf(r2));
    L10:
        if (this.rewindButton == null) goto L13;
        this.rewindButton.setContentDescription(this.resources.getQuantityString(C0467R.plurals.exo_controls_rewind_by_amount_description, r2, new Object[]{Integer.valueOf(r2)}));
        return;
    L13:
        return;
    L5:
        r0 = 5000;
        goto L6
    }

    private void updateSettingsButton() {
        updateButton(this.settingsAdapter.hasSettingsToShow(), this.settingsButton);
    }

    private void updateSettingsWindowSize() {
        this.settingsView.measure(0, 0);
        int r0 = getWidth() - (this.settingsWindowMargin * 2);
        int r2 = Math.min(this.settingsView.getMeasuredWidth(), r0);
        this.settingsWindow.setWidth(r2);
        int r5 = Math.min(getHeight() - (this.settingsWindowMargin * 2), this.settingsView.getMeasuredHeight());
        this.settingsWindow.setHeight(r5);
    }

    private void updateShuffleButton() {
        if (isVisible() == true) goto L5;
        return;
    L5:
        if (this.isAttachedToWindow == true) goto L7;
        return;
    L7:
        if (this.shuffleButton == null) goto L31;
        Player r0 = this.player;
        if (this.controlViewLayoutManager.getShowButton(this.shuffleButton) == true) goto L12;
        updateButton(false, this.shuffleButton);
        return;
    L12:
        if (r0 != null) goto L14;
    L25:
        updateButton(false, this.shuffleButton);
        this.shuffleButton.setImageDrawable(this.shuffleOffButtonDrawable);
        this.shuffleButton.setContentDescription(this.shuffleOffContentDescription);
        return;
    L14:
        if (r0.isCommandAvailable(14) == false) goto L25;
        updateButton(true, this.shuffleButton);
        ImageView r1 = this.shuffleButton;
        if (r0.getShuffleModeEnabled() == false) goto L19;
        Drawable r2 = this.shuffleOnButtonDrawable;
    L20:
        r1.setImageDrawable(r2);
        ImageView r3 = this.shuffleButton;
        if (r0.getShuffleModeEnabled() == false) goto L23;
        String r4 = this.shuffleOnContentDescription;
    L24:
        r3.setContentDescription(r4);
        return;
    L23:
        r4 = this.shuffleOffContentDescription;
        goto L24
    L19:
        r2 = this.shuffleOffButtonDrawable;
        goto L20
    }

    private void updateTimeline() {
        Player r1 = this.player;
        if (r1 != null) goto L5;
        return;
    L5:
        int r4 = 1;
        if (this.showMultiWindowTimeBar == true) goto L8;
    L10:
        boolean r2 = false;
    L11:
        this.multiWindowTimeBar = r2;
        long r5 = 0;
        this.currentWindowOffset = 0;
        long r7 = 0;
        int r3 = 0;
        if (r1.isCommandAvailable(17) == false) goto L14;
        Timeline r9 = r1.getCurrentTimeline();
    L15:
        boolean r10 = r9.isEmpty();
        long r11 = C0180C.TIME_UNSET;
        if (r10 == true) goto L63;
        int r12 = r1.getCurrentMediaItemIndex();
        if (this.multiWindowTimeBar == false) goto L20;
        int r13 = 0;
    L22:
        if (this.multiWindowTimeBar == false) goto L24;
        int r14 = r9.getWindowCount() - 1;
    L25:
        int r15 = r13;
    L26:
        if (r15 > r14) goto L67;
        if (r15 != r12) goto L30;
        this.currentWindowOffset = Util.usToMs(r7);
    L30:
        r9.getWindow(r15, this.window);
        if (this.window.durationUs == r11) goto L32;
        int r6 = this.window.firstPeriodIndex;
    L35:
        if (r6 > this.window.lastPeriodIndex) goto L60;
        r9.getPeriod(r6, this.period);
        int r8 = this.period.getRemovedAdGroupCount();
        int r16 = this.period.getAdGroupCount();
        long r28 = r11;
        int r17 = r8;
    L37:
        if (r17 >= r16) goto L59;
        long r21 = this.period.getAdGroupTimeUs(r17);
        if (r21 != Long.MIN_VALUE) goto L44;
        int r24 = r6;
        if (this.period.durationUs == r28) goto L57;
        r21 = this.period.durationUs;
    L45:
        long r18 = r21 + this.period.getPositionInWindowUs();
        if (r18 < r5) goto L57;
        if (r3 != this.adGroupTimesMs.length) goto L55;
        if (this.adGroupTimesMs.length != 0) goto L52;
        int r19 = r4;
    L53:
        this.adGroupTimesMs = Arrays.copyOf(this.adGroupTimesMs, r19);
        this.playedAdGroups = Arrays.copyOf(this.playedAdGroups, r19);
        goto L55
    L52:
        r19 = this.adGroupTimesMs.length * 2;
    L55:
        this.adGroupTimesMs[r3] = Util.usToMs(r7 + r18);
        this.playedAdGroups[r3] = this.period.hasPlayedAdGroup(r17);
        r3 = r3 + 1;
    L58:
        r17 = r17 + 1;
        r6 = r24;
    L57:
        r3 = r3;
        goto L58
    L44:
        r24 = r6;
        goto L45
    L59:
        r6 = r6 + 1;
        r11 = r28;
        goto L35
    L60:
        r7 = r7 + this.window.durationUs;
        r15 = r15 + 1;
        r4 = r4;
        r5 = r5;
        goto L26
    L32:
        Assertions.checkState(!this.multiWindowTimeBar);
    L67:
        long r20 = Util.usToMs(r7);
        if (this.durationView == null) goto L71;
        this.durationView.setText(Util.getStringForTime(this.formatBuilder, this.formatter, r20));
    L71:
        if (this.timeBar == null) goto L76;
        this.timeBar.setDuration(r20);
        int r22 = this.extraAdGroupTimesMs.length;
        int r23 = r3 + r22;
        if (r23 <= this.adGroupTimesMs.length) goto L75;
        this.adGroupTimesMs = Arrays.copyOf(this.adGroupTimesMs, r23);
        this.playedAdGroups = Arrays.copyOf(this.playedAdGroups, r23);
    L75:
        System.arraycopy(this.extraAdGroupTimesMs, 0, this.adGroupTimesMs, r3, r22);
        System.arraycopy(this.extraPlayedAdGroups, 0, this.playedAdGroups, r3, r22);
        this.timeBar.setAdGroupTimesMs(this.adGroupTimesMs, this.playedAdGroups, r23);
    L76:
        updateProgress();
        return;
    L24:
        r14 = r12;
        goto L25
    L20:
        r13 = r12;
        goto L22
    L63:
        if (r1.isCommandAvailable(16) == false) goto L67;
        long r25 = r1.getContentDuration();
        if (r25 == C0180C.TIME_UNSET) goto L67;
        r7 = Util.msToUs(r25);
        goto L67
    L14:
        r9 = Timeline.EMPTY;
        goto L15
    L8:
        if (canShowMultiWindowTimeBar(r1, this.window) == false) goto L10;
        r2 = true;
        goto L11
    }

    private void updateTrackLists() {
        initTrackSelectionAdapter();
        if (this.textTrackSelectionAdapter.getItemCount() <= 0) goto L5;
        boolean r0 = true;
    L6:
        updateButton(r0, this.subtitleButton);
        updateSettingsButton();
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Deprecated
    public void addVisibilityListener(VisibilityListener r2) {
        Assertions.checkNotNull(r2);
        this.visibilityListeners.add(r2);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent r2) {
        if (dispatchMediaKeyEvent(r2) == false) goto L5;
    L8:
        return true;
    L5:
        if (super.dispatchKeyEvent(r2) == true) goto L8;
        return false;
    }

    public boolean dispatchMediaKeyEvent(KeyEvent r5) {
        int r0 = r5.getKeyCode();
        Player r1 = this.player;
        if (r1 != null) goto L5;
        return false;
    L5:
        if (isHandledMediaKey(r0) == true) goto L8;
        return false;
    L8:
        if (r5.getAction() == 0) goto L10;
        return true;
    L10:
        if (r0 != 90) goto L17;
        if (r1.getPlaybackState() != 4) goto L14;
        return true;
    L14:
        if (r1.isCommandAvailable(12) == false) goto L40;
        r1.seekForward();
        return true;
    L40:
        return true;
    L17:
        if (r0 != 89) goto L22;
        if (r1.isCommandAvailable(11) == false) goto L22;
        r1.seekBack();
        return true;
    L22:
        if (r5.getRepeatCount() != 0) goto L43;
        switch(r0) {
            case 79: goto L33;
            case 85: goto L33;
            case 87: goto L31;
            case 88: goto L28;
            case 126: goto L26;
            case 127: goto L25;
            default: goto L50;
        };
    L25:
        Util.handlePauseButtonAction(r1);
        return true;
    L26:
        Util.handlePlayButtonAction(r1);
        return true;
    L33:
        Util.handlePlayPauseButtonAction(r1, this.showPlayButtonIfSuppressed);
        return true;
    L50:
        return true;
    L28:
        if (r1.isCommandAvailable(7) == false) goto L46;
        r1.seekToPrevious();
        return true;
    L46:
        return true;
    L31:
        if (r1.isCommandAvailable(9) == false) goto L48;
        r1.seekToNext();
        return true;
    L48:
        return true;
    L43:
        return true;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getRepeatToggleModes() {
        return this.repeatToggleModes;
    }

    public boolean getShowShuffleButton() {
        return this.controlViewLayoutManager.getShowButton(this.shuffleButton);
    }

    public boolean getShowSubtitleButton() {
        return this.controlViewLayoutManager.getShowButton(this.subtitleButton);
    }

    public int getShowTimeoutMs() {
        return this.showTimeoutMs;
    }

    public boolean getShowVrButton() {
        return this.controlViewLayoutManager.getShowButton(this.vrButton);
    }

    public void hide() {
        this.controlViewLayoutManager.hide();
    }

    public void hideImmediately() {
        this.controlViewLayoutManager.hideImmediately();
    }

    public boolean isAnimationEnabled() {
        return this.controlViewLayoutManager.isAnimationEnabled();
    }

    public boolean isFullyVisible() {
        return this.controlViewLayoutManager.isFullyVisible();
    }

    public boolean isVisible() {
        if (getVisibility() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    void notifyOnVisibilityChange() {
        Iterator<VisibilityListener> r0 = this.visibilityListeners.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().onVisibilityChange(getVisibility());
        goto L4
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.controlViewLayoutManager.onAttachedToWindow();
        this.isAttachedToWindow = true;
        if (isFullyVisible() == false) goto L5;
        this.controlViewLayoutManager.resetHideCallbacks();
    L5:
        updateAll();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.controlViewLayoutManager.onDetachedFromWindow();
        this.isAttachedToWindow = false;
        removeCallbacks(this.updateProgressAction);
        this.controlViewLayoutManager.removeHideCallbacks();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean r7, int r8, int r9, int r10, int r11) {
        super.onLayout(r7, r8, r9, r10, r11);
        this.controlViewLayoutManager.onLayout(r7, r8, r9, r10, r11);
    }

    @Deprecated
    public void removeVisibilityListener(VisibilityListener r2) {
        this.visibilityListeners.remove(r2);
    }

    void requestPlayPauseFocus() {
        if (this.playPauseButton == null) goto L6;
        this.playPauseButton.requestFocus();
        return;
    }

    public void setAnimationEnabled(boolean r2) {
        this.controlViewLayoutManager.setAnimationEnabled(r2);
    }

    public void setExtraAdGroupMarkers(long[] r4, boolean[] r5) {
        boolean r0 = false;
        if (r4 != null) goto L5;
        this.extraAdGroupTimesMs = new long[0];
        this.extraPlayedAdGroups = new boolean[0];
    L9:
        updateTimeline();
        return;
    L5:
        boolean[] r6 = (boolean[]) Assertions.checkNotNull(r5);
        if (r4.length != r6.length) goto L8;
        r0 = true;
    L8:
        Assertions.checkArgument(r0);
        this.extraAdGroupTimesMs = r4;
        this.extraPlayedAdGroups = r6;
        goto L9
    }

    @Deprecated
    public void setOnFullScreenModeChangedListener(OnFullScreenModeChangedListener r5) {
        this.onFullScreenModeChangedListener = r5;
        ImageView r0 = this.fullscreenButton;
        boolean r1 = true;
        if (r5 == null) goto L5;
        boolean r3 = true;
    L6:
        updateFullscreenButtonVisibility(r0, r3);
        ImageView r2 = this.minimalFullscreenButton;
        if (r5 != null) goto L10;
        r1 = false;
    L10:
        updateFullscreenButtonVisibility(r2, r1);
        return;
    L5:
        r3 = false;
        goto L6
    }

    public void setPlayer(Player r5) {
        boolean r2 = true;
        if (Looper.myLooper() != Looper.getMainLooper()) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        if (r5 != null) goto L9;
    L13:
        Assertions.checkArgument(r2);
        if (this.player != r5) goto L17;
        return;
    L17:
        if (this.player == null) goto L19;
        this.player.removeListener(this.componentListener);
    L19:
        this.player = r5;
        if (r5 == null) goto L22;
        r5.addListener(this.componentListener);
    L22:
        updateAll();
        return;
    L9:
        if (r5.getApplicationLooper() == Looper.getMainLooper()) goto L13;
        r2 = false;
        goto L13
    L5:
        r0 = false;
        goto L6
    }

    public void setProgressUpdateListener(ProgressUpdateListener r1) {
        this.progressUpdateListener = r1;
    }

    public void setRepeatToggleModes(int r6) {
        this.repeatToggleModes = r6;
        boolean r1 = false;
        if (this.player != null) goto L5;
    L17:
        PlayerControlViewLayoutManager r0 = this.controlViewLayoutManager;
        ImageView r3 = this.repeatToggleButton;
        if (r6 == 0) goto L20;
        r1 = true;
    L20:
        r0.setShowButton(r3, r1);
        updateRepeatModeButton();
        return;
    L5:
        if (this.player.isCommandAvailable(15) == false) goto L17;
        int r2 = this.player.getRepeatMode();
        if (r6 != 0) goto L11;
        if (r2 == 0) goto L11;
        this.player.setRepeatMode(0);
    L11:
        if (r6 != 1) goto L14;
        if (r2 != 2) goto L14;
        this.player.setRepeatMode(1);
    L14:
        if (r6 != 2) goto L17;
        if (r2 != 1) goto L17;
        this.player.setRepeatMode(2);
        goto L17
    }

    public void setShowFastForwardButton(boolean r3) {
        this.controlViewLayoutManager.setShowButton(this.fastForwardButton, r3);
        updateNavigation();
    }

    @Deprecated
    public void setShowMultiWindowTimeBar(boolean r1) {
        this.showMultiWindowTimeBar = r1;
        updateTimeline();
    }

    public void setShowNextButton(boolean r3) {
        this.controlViewLayoutManager.setShowButton(this.nextButton, r3);
        updateNavigation();
    }

    public void setShowPlayButtonIfPlaybackIsSuppressed(boolean r1) {
        this.showPlayButtonIfSuppressed = r1;
        updatePlayPauseButton();
    }

    public void setShowPreviousButton(boolean r3) {
        this.controlViewLayoutManager.setShowButton(this.previousButton, r3);
        updateNavigation();
    }

    public void setShowRewindButton(boolean r3) {
        this.controlViewLayoutManager.setShowButton(this.rewindButton, r3);
        updateNavigation();
    }

    public void setShowShuffleButton(boolean r3) {
        this.controlViewLayoutManager.setShowButton(this.shuffleButton, r3);
        updateShuffleButton();
    }

    public void setShowSubtitleButton(boolean r3) {
        this.controlViewLayoutManager.setShowButton(this.subtitleButton, r3);
    }

    public void setShowTimeoutMs(int r2) {
        this.showTimeoutMs = r2;
        if (isFullyVisible() == false) goto L6;
        this.controlViewLayoutManager.resetHideCallbacks();
        return;
    }

    public void setShowVrButton(boolean r3) {
        this.controlViewLayoutManager.setShowButton(this.vrButton, r3);
    }

    public void setTimeBarMinUpdateInterval(int r3) {
        this.timeBarMinUpdateIntervalMs = Util.constrainValue(r3, 16, 1000);
    }

    public void setVrButtonListener(View.OnClickListener r3) {
        if (this.vrButton == null) goto L10;
        this.vrButton.setOnClickListener(r3);
        if (r3 == null) goto L7;
        boolean r0 = true;
    L8:
        updateButton(r0, this.vrButton);
        return;
    L7:
        r0 = false;
        goto L8
    }

    public void show() {
        this.controlViewLayoutManager.show();
    }

    void updateAll() {
        updatePlayPauseButton();
        updateNavigation();
        updateRepeatModeButton();
        updateShuffleButton();
        updateTrackLists();
        updatePlaybackSpeedList();
        updateTimeline();
    }

    public void updateIsFullscreen(boolean r2) {
        if (this.isFullscreen != r2) goto L5;
        return;
    L5:
        this.isFullscreen = r2;
        updateFullscreenButtonForState(this.fullscreenButton, r2);
        updateFullscreenButtonForState(this.minimalFullscreenButton, r2);
        if (this.onFullScreenModeChangedListener == null) goto L9;
        this.onFullScreenModeChangedListener.onFullScreenModeChanged(r2);
        return;
    }
}
