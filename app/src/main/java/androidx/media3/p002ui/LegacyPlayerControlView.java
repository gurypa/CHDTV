package androidx.media3.p002ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C0180C;
import androidx.media3.common.DeviceInfo;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaLibraryInfo;
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
import androidx.media3.common.util.RepeatModeUtil;
import androidx.media3.common.util.Util;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public class LegacyPlayerControlView extends FrameLayout {
    public static final int DEFAULT_REPEAT_TOGGLE_MODES = 0;
    public static final int DEFAULT_SHOW_TIMEOUT_MS = 5000;
    public static final int DEFAULT_TIME_BAR_MIN_UPDATE_INTERVAL_MS = 200;
    private static final int MAX_UPDATE_INTERVAL_MS = 1000;
    public static final int MAX_WINDOWS_FOR_MULTI_WINDOW_TIME_BAR = 100;
    private long[] adGroupTimesMs;
    private final float buttonAlphaDisabled;
    private final float buttonAlphaEnabled;
    private final ComponentListener componentListener;
    private long currentBufferedPosition;
    private long currentPosition;
    private long currentWindowOffset;
    private final TextView durationView;
    private long[] extraAdGroupTimesMs;
    private boolean[] extraPlayedAdGroups;
    private final View fastForwardButton;
    private final StringBuilder formatBuilder;
    private final Formatter formatter;
    private final Runnable hideAction;
    private long hideAtMs;
    private boolean isAttachedToWindow;
    private boolean multiWindowTimeBar;
    private final View nextButton;
    private final View pauseButton;
    private final Timeline.Period period;
    private final View playButton;
    private boolean[] playedAdGroups;
    private Player player;
    private final TextView positionView;
    private final View previousButton;
    private ProgressUpdateListener progressUpdateListener;
    private final String repeatAllButtonContentDescription;
    private final Drawable repeatAllButtonDrawable;
    private final String repeatOffButtonContentDescription;
    private final Drawable repeatOffButtonDrawable;
    private final String repeatOneButtonContentDescription;
    private final Drawable repeatOneButtonDrawable;
    private final ImageView repeatToggleButton;
    private int repeatToggleModes;
    private final View rewindButton;
    private boolean scrubbing;
    private boolean showFastForwardButton;
    private boolean showMultiWindowTimeBar;
    private boolean showNextButton;
    private boolean showPlayButtonIfSuppressed;
    private boolean showPreviousButton;
    private boolean showRewindButton;
    private boolean showShuffleButton;
    private int showTimeoutMs;
    private final ImageView shuffleButton;
    private final Drawable shuffleOffButtonDrawable;
    private final String shuffleOffContentDescription;
    private final Drawable shuffleOnButtonDrawable;
    private final String shuffleOnContentDescription;
    private final TimeBar timeBar;
    private int timeBarMinUpdateIntervalMs;
    private final Runnable updateProgressAction;
    private final CopyOnWriteArrayList<VisibilityListener> visibilityListeners;
    private final View vrButton;
    private final Timeline.Window window;

    /* JADX INFO: renamed from: androidx.media3.ui.LegacyPlayerControlView$1 */
    static /* synthetic */ class C04511 {
    }

    private final class ComponentListener implements Player.Listener, TimeBar.OnScrubListener, View.OnClickListener {
        final /* synthetic */ LegacyPlayerControlView this$0;

        private ComponentListener(LegacyPlayerControlView r1) {
            this.this$0 = r1;
        }

        /* synthetic */ ComponentListener(LegacyPlayerControlView r1, C04511 r2) {
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
        public void onClick(View r4) {
            Player r0 = LegacyPlayerControlView.access$1100(this.this$0);
            if (r0 != null) goto L6;
            return;
        L6:
            if (LegacyPlayerControlView.access$1300(this.this$0) != r4) goto L9;
            r0.seekToNext();
            return;
        L9:
            if (LegacyPlayerControlView.access$1400(this.this$0) != r4) goto L12;
            r0.seekToPrevious();
            return;
        L12:
            if (LegacyPlayerControlView.access$1500(this.this$0) != r4) goto L17;
            if (r0.getPlaybackState() == 4) goto L34;
            r0.seekForward();
            return;
        L34:
            return;
        L17:
            if (LegacyPlayerControlView.access$1600(this.this$0) != r4) goto L20;
            r0.seekBack();
            return;
        L20:
            if (LegacyPlayerControlView.access$1700(this.this$0) != r4) goto L23;
            Util.handlePlayButtonAction(r0);
            return;
        L23:
            if (LegacyPlayerControlView.access$1800(this.this$0) != r4) goto L26;
            Util.handlePauseButtonAction(r0);
            return;
        L26:
            if (LegacyPlayerControlView.access$1900(this.this$0) != r4) goto L29;
            r0.setRepeatMode(RepeatModeUtil.getNextRepeatMode(r0.getRepeatMode(), LegacyPlayerControlView.access$2000(this.this$0)));
            return;
        L29:
            if (LegacyPlayerControlView.access$2100(this.this$0) != r4) goto L40;
            r0.setShuffleModeEnabled(!r0.getShuffleModeEnabled());
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

        @Override // androidx.media3.common.Player.Listener
        public void onEvents(Player r6, Player.Events r7) {
            if (r7.containsAny(new int[]{4, 5}) == false) goto L6;
            LegacyPlayerControlView.access$100(this.this$0);
        L6:
            if (r7.containsAny(new int[]{4, 5, 7}) == false) goto L9;
            LegacyPlayerControlView.access$200(this.this$0);
        L9:
            if (r7.contains(8) == false) goto L12;
            LegacyPlayerControlView.access$300(this.this$0);
        L12:
            if (r7.contains(9) == false) goto L15;
            LegacyPlayerControlView.access$400(this.this$0);
        L15:
            if (r7.containsAny(new int[]{8, 9, 11, 0, 13}) == false) goto L18;
            LegacyPlayerControlView.access$500(this.this$0);
        L18:
            if (r7.containsAny(new int[]{11, 0}) == false) goto L21;
            LegacyPlayerControlView.access$600(this.this$0);
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
            if (LegacyPlayerControlView.access$800(this.this$0) == null) goto L6;
            LegacyPlayerControlView.access$800(this.this$0).setText(Util.getStringForTime(LegacyPlayerControlView.access$900(this.this$0), LegacyPlayerControlView.access$1000(this.this$0), r5));
            return;
        }

        @Override // androidx.media3.ui.TimeBar.OnScrubListener
        public void onScrubStart(TimeBar r4, long r5) {
            LegacyPlayerControlView.access$702(this.this$0, true);
            if (LegacyPlayerControlView.access$800(this.this$0) == null) goto L6;
            LegacyPlayerControlView.access$800(this.this$0).setText(Util.getStringForTime(LegacyPlayerControlView.access$900(this.this$0), LegacyPlayerControlView.access$1000(this.this$0), r5));
            return;
        }

        @Override // androidx.media3.ui.TimeBar.OnScrubListener
        public void onScrubStop(TimeBar r3, long r4, boolean r6) {
            LegacyPlayerControlView.access$702(this.this$0, false);
            if (r6 == false) goto L5;
            return;
        L5:
            if (LegacyPlayerControlView.access$1100(this.this$0) == null) goto L9;
            LegacyPlayerControlView.access$1200(this.this$0, LegacyPlayerControlView.access$1100(this.this$0), r4);
            return;
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

    public interface ProgressUpdateListener {
        void onProgressUpdate(long r1, long r3);
    }

    public interface VisibilityListener {
        void onVisibilityChange(int r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$0j2aCtVAsZawYrErNZfQHISA-ak, reason: not valid java name */
    public static /* synthetic */ void m486$r8$lambda$0j2aCtVAsZawYrErNZfQHISAak(LegacyPlayerControlView r0) {
        r0.updateProgress();
    }

    static {
        MediaLibraryInfo.registerModule("media3.ui");
    }

    public LegacyPlayerControlView(Context r2) {
        this(r2, null);
    }

    public LegacyPlayerControlView(Context r2, AttributeSet r3) {
        this(r2, r3, 0);
    }

    public LegacyPlayerControlView(Context r1, AttributeSet r2, int r3) {
        this(r1, r2, r3, r2);
    }

    public LegacyPlayerControlView(Context r10, AttributeSet r11, int r12, AttributeSet r13) {
        super(r10, r11, r12);
        int r0 = C0467R.layout.exo_legacy_player_control_view;
        this.showPlayButtonIfSuppressed = true;
        this.showTimeoutMs = 5000;
        this.repeatToggleModes = 0;
        this.timeBarMinUpdateIntervalMs = 200;
        this.hideAtMs = C0180C.TIME_UNSET;
        this.showRewindButton = true;
        this.showFastForwardButton = true;
        this.showPreviousButton = true;
        this.showNextButton = true;
        this.showShuffleButton = false;
        if (r13 == null) goto L10;
        TypedArray r1 = r10.getTheme().obtainStyledAttributes(r13, C0467R.styleable.LegacyPlayerControlView, r12, 0);
        this.showTimeoutMs = r1.getInt(C0467R.styleable.LegacyPlayerControlView_show_timeout, this.showTimeoutMs);     // Catch: Throwable -> L7
        r0 = r1.getResourceId(C0467R.styleable.LegacyPlayerControlView_controller_layout_id, r0);     // Catch: Throwable -> L7
        this.repeatToggleModes = getRepeatToggleModes(r1, this.repeatToggleModes);     // Catch: Throwable -> L7
        this.showRewindButton = r1.getBoolean(C0467R.styleable.LegacyPlayerControlView_show_rewind_button, this.showRewindButton);     // Catch: Throwable -> L7
        this.showFastForwardButton = r1.getBoolean(C0467R.styleable.LegacyPlayerControlView_show_fastforward_button, this.showFastForwardButton);     // Catch: Throwable -> L7
        this.showPreviousButton = r1.getBoolean(C0467R.styleable.LegacyPlayerControlView_show_previous_button, this.showPreviousButton);     // Catch: Throwable -> L7
        this.showNextButton = r1.getBoolean(C0467R.styleable.LegacyPlayerControlView_show_next_button, this.showNextButton);     // Catch: Throwable -> L7
        this.showShuffleButton = r1.getBoolean(C0467R.styleable.LegacyPlayerControlView_show_shuffle_button, this.showShuffleButton);     // Catch: Throwable -> L7
        setTimeBarMinUpdateInterval(r1.getInt(C0467R.styleable.LegacyPlayerControlView_time_bar_min_update_interval, this.timeBarMinUpdateIntervalMs));     // Catch: Throwable -> L7
        r1.recycle();
    L7:
        th = move-exception;
        r1.recycle();
        throw th;
    L10:
        this.visibilityListeners = new CopyOnWriteArrayList();
        this.period = new Timeline.Period();
        this.window = new Timeline.Window();
        this.formatBuilder = new StringBuilder();
        this.formatter = new Formatter(this.formatBuilder, Locale.getDefault());
        this.adGroupTimesMs = new long[0];
        this.playedAdGroups = new boolean[0];
        this.extraAdGroupTimesMs = new long[0];
        this.extraPlayedAdGroups = new boolean[0];
        this.componentListener = new ComponentListener(this, null);
        this.updateProgressAction = new LegacyPlayerControlView$$ExternalSyntheticLambda0(this);
        this.hideAction = new LegacyPlayerControlView$$ExternalSyntheticLambda1(this);
        LayoutInflater.from(r10).inflate(r0, this);
        setDescendantFocusability(262144);
        TimeBar r2 = (TimeBar) findViewById(C0467R.id.exo_progress);
        View r6 = findViewById(C0467R.id.exo_progress_placeholder);
        if (r2 == null) goto L13;
        this.timeBar = r2;
    L16:
        this.durationView = (TextView) findViewById(C0467R.id.exo_duration);
        this.positionView = (TextView) findViewById(C0467R.id.exo_position);
        if (this.timeBar == null) goto L19;
        this.timeBar.addListener(this.componentListener);
    L19:
        this.playButton = findViewById(C0467R.id.exo_play);
        if (this.playButton == null) goto L22;
        this.playButton.setOnClickListener(this.componentListener);
    L22:
        this.pauseButton = findViewById(C0467R.id.exo_pause);
        if (this.pauseButton == null) goto L25;
        this.pauseButton.setOnClickListener(this.componentListener);
    L25:
        this.previousButton = findViewById(C0467R.id.exo_prev);
        if (this.previousButton == null) goto L28;
        this.previousButton.setOnClickListener(this.componentListener);
    L28:
        this.nextButton = findViewById(C0467R.id.exo_next);
        if (this.nextButton == null) goto L31;
        this.nextButton.setOnClickListener(this.componentListener);
    L31:
        this.rewindButton = findViewById(C0467R.id.exo_rew);
        if (this.rewindButton == null) goto L34;
        this.rewindButton.setOnClickListener(this.componentListener);
    L34:
        this.fastForwardButton = findViewById(C0467R.id.exo_ffwd);
        if (this.fastForwardButton == null) goto L37;
        this.fastForwardButton.setOnClickListener(this.componentListener);
    L37:
        this.repeatToggleButton = (ImageView) findViewById(C0467R.id.exo_repeat_toggle);
        if (this.repeatToggleButton == null) goto L40;
        this.repeatToggleButton.setOnClickListener(this.componentListener);
    L40:
        this.shuffleButton = (ImageView) findViewById(C0467R.id.exo_shuffle);
        if (this.shuffleButton == null) goto L43;
        this.shuffleButton.setOnClickListener(this.componentListener);
    L43:
        this.vrButton = findViewById(C0467R.id.exo_vr);
        setShowVrButton(false);
        updateButton(false, false, this.vrButton);
        Resources r3 = r10.getResources();
        this.buttonAlphaEnabled = r3.getInteger(C0467R.integer.exo_media_button_opacity_percentage_enabled) / 100.0f;
        this.buttonAlphaDisabled = r3.getInteger(C0467R.integer.exo_media_button_opacity_percentage_disabled) / 100.0f;
        this.repeatOffButtonDrawable = Util.getDrawable(r10, r3, C0467R.drawable.exo_legacy_controls_repeat_off);
        this.repeatOneButtonDrawable = Util.getDrawable(r10, r3, C0467R.drawable.exo_legacy_controls_repeat_one);
        this.repeatAllButtonDrawable = Util.getDrawable(r10, r3, C0467R.drawable.exo_legacy_controls_repeat_all);
        this.shuffleOnButtonDrawable = Util.getDrawable(r10, r3, C0467R.drawable.exo_legacy_controls_shuffle_on);
        this.shuffleOffButtonDrawable = Util.getDrawable(r10, r3, C0467R.drawable.exo_legacy_controls_shuffle_off);
        this.repeatOffButtonContentDescription = r3.getString(C0467R.string.exo_controls_repeat_off_description);
        this.repeatOneButtonContentDescription = r3.getString(C0467R.string.exo_controls_repeat_one_description);
        this.repeatAllButtonContentDescription = r3.getString(C0467R.string.exo_controls_repeat_all_description);
        this.shuffleOnContentDescription = r3.getString(C0467R.string.exo_controls_shuffle_on_description);
        this.shuffleOffContentDescription = r3.getString(C0467R.string.exo_controls_shuffle_off_description);
        this.currentPosition = C0180C.TIME_UNSET;
        this.currentBufferedPosition = C0180C.TIME_UNSET;
        return;
    L13:
        if (r6 == null) goto L15;
        DefaultTimeBar r7 = new DefaultTimeBar(r10, null, 0, r13);
        r7.setId(C0467R.id.exo_progress);
        r7.setLayoutParams(r6.getLayoutParams());
        ViewGroup r5 = (ViewGroup) r6.getParent();
        int r8 = r5.indexOfChild(r6);
        r5.removeView(r6);
        r5.addView(r7, r8);
        this.timeBar = r7;
        goto L16
    L15:
        this.timeBar = null;
        goto L16
    }

    static /* synthetic */ void access$100(LegacyPlayerControlView r0) {
        r0.updatePlayPauseButton();
    }

    static /* synthetic */ Formatter access$1000(LegacyPlayerControlView r1) {
        return r1.formatter;
    }

    static /* synthetic */ Player access$1100(LegacyPlayerControlView r1) {
        return r1.player;
    }

    static /* synthetic */ void access$1200(LegacyPlayerControlView r0, Player r1, long r2) {
        r0.seekToTimeBarPosition(r1, r2);
    }

    static /* synthetic */ View access$1300(LegacyPlayerControlView r1) {
        return r1.nextButton;
    }

    static /* synthetic */ View access$1400(LegacyPlayerControlView r1) {
        return r1.previousButton;
    }

    static /* synthetic */ View access$1500(LegacyPlayerControlView r1) {
        return r1.fastForwardButton;
    }

    static /* synthetic */ View access$1600(LegacyPlayerControlView r1) {
        return r1.rewindButton;
    }

    static /* synthetic */ View access$1700(LegacyPlayerControlView r1) {
        return r1.playButton;
    }

    static /* synthetic */ View access$1800(LegacyPlayerControlView r1) {
        return r1.pauseButton;
    }

    static /* synthetic */ ImageView access$1900(LegacyPlayerControlView r1) {
        return r1.repeatToggleButton;
    }

    static /* synthetic */ void access$200(LegacyPlayerControlView r0) {
        r0.updateProgress();
    }

    static /* synthetic */ int access$2000(LegacyPlayerControlView r1) {
        return r1.repeatToggleModes;
    }

    static /* synthetic */ ImageView access$2100(LegacyPlayerControlView r1) {
        return r1.shuffleButton;
    }

    static /* synthetic */ void access$300(LegacyPlayerControlView r0) {
        r0.updateRepeatModeButton();
    }

    static /* synthetic */ void access$400(LegacyPlayerControlView r0) {
        r0.updateShuffleButton();
    }

    static /* synthetic */ void access$500(LegacyPlayerControlView r0) {
        r0.updateNavigation();
    }

    static /* synthetic */ void access$600(LegacyPlayerControlView r0) {
        r0.updateTimeline();
    }

    static /* synthetic */ boolean access$702(LegacyPlayerControlView r0, boolean r1) {
        r0.scrubbing = r1;
        return r1;
    }

    static /* synthetic */ TextView access$800(LegacyPlayerControlView r1) {
        return r1.positionView;
    }

    static /* synthetic */ StringBuilder access$900(LegacyPlayerControlView r1) {
        return r1.formatBuilder;
    }

    private static boolean canShowMultiWindowTimeBar(Timeline r7, Timeline.Window r8) {
        if (r7.getWindowCount() <= 100) goto L5;
        return false;
    L5:
        int r0 = r7.getWindowCount();
        int r1 = 0;
    L6:
        if (r1 >= r0) goto L11;
        if (r7.getWindow(r1, r8).durationUs == C0180C.TIME_UNSET) goto L9;
        r1 = r1 + 1;
        goto L6
    L9:
        return false;
    L11:
        return true;
    }

    private static int getRepeatToggleModes(TypedArray r1, int r2) {
        return r1.getInt(C0467R.styleable.LegacyPlayerControlView_repeat_toggle_modes, r2);
    }

    private void hideAfterTimeout() {
        removeCallbacks(this.hideAction);
        if (this.showTimeoutMs <= 0) goto L7;
        this.hideAtMs = SystemClock.uptimeMillis() + ((long) this.showTimeoutMs);
        if (this.isAttachedToWindow == false) goto L9;
        postDelayed(this.hideAction, this.showTimeoutMs);
        return;
    L9:
        return;
    L7:
        this.hideAtMs = C0180C.TIME_UNSET;
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

    private void requestPlayPauseAccessibilityFocus() {
        boolean r0 = Util.shouldShowPlayButton(this.player, this.showPlayButtonIfSuppressed);
        if (r0 == true) goto L5;
    L7:
        if (r0 == false) goto L9;
        return;
    L9:
        if (this.pauseButton == null) goto L14;
        this.pauseButton.sendAccessibilityEvent(8);
        return;
    L14:
        return;
    L5:
        if (this.playButton == null) goto L7;
        this.playButton.sendAccessibilityEvent(8);
    }

    private void requestPlayPauseFocus() {
        boolean r0 = Util.shouldShowPlayButton(this.player, this.showPlayButtonIfSuppressed);
        if (r0 == true) goto L5;
    L7:
        if (r0 == false) goto L9;
        return;
    L9:
        if (this.pauseButton == null) goto L14;
        this.pauseButton.requestFocus();
        return;
    L14:
        return;
    L5:
        if (this.playButton == null) goto L7;
        this.playButton.requestFocus();
    }

    private void seekTo(Player r1, int r2, long r3) {
        r1.seekTo(r2, r3);
    }

    private void seekToTimeBarPosition(Player r7, long r8) {
        Timeline r0 = r7.getCurrentTimeline();
        if (this.multiWindowTimeBar == true) goto L5;
    L15:
        int r2 = r7.getCurrentMediaItemIndex();
    L16:
        seekTo(r7, r2, r8);
        updateProgress();
        return;
    L5:
        if (r0.isEmpty() == true) goto L15;
        int r1 = r0.getWindowCount();
        r2 = 0;
    L7:
        long r3 = r0.getWindow(r2, this.window).getDurationMs();
        if (r8 < r3) goto L16;
        if (r2 == (r1 - 1)) goto L12;
        r8 = r8 - r3;
        r2 = r2 + 1;
        goto L7
    L12:
        r8 = r3;
        goto L16
    }

    private void updateAll() {
        updatePlayPauseButton();
        updateNavigation();
        updateRepeatModeButton();
        updateShuffleButton();
        updateTimeline();
    }

    private void updateButton(boolean r2, boolean r3, View r4) {
        if (r4 != null) goto L4;
        return;
    L4:
        r4.setEnabled(r3);
        if (r3 == false) goto L7;
        float r0 = this.buttonAlphaEnabled;
    L8:
        r4.setAlpha(r0);
        if (r2 == false) goto L11;
        int r1 = 0;
    L12:
        r4.setVisibility(r1);
        return;
    L11:
        r1 = 8;
        goto L12
    L7:
        r0 = this.buttonAlphaDisabled;
        goto L8
    }

    private void updateNavigation() {
        if (isVisible() == true) goto L5;
        return;
    L5:
        if (this.isAttachedToWindow == false) goto L16;
        Player r0 = this.player;
        boolean r1 = false;
        boolean r2 = false;
        boolean r3 = false;
        boolean r4 = false;
        boolean r5 = false;
        if (r0 == null) goto L10;
        r1 = r0.isCommandAvailable(5);
        r2 = r0.isCommandAvailable(7);
        r3 = r0.isCommandAvailable(11);
        r4 = r0.isCommandAvailable(12);
        r5 = r0.isCommandAvailable(9);
    L10:
        updateButton(this.showPreviousButton, r2, this.previousButton);
        updateButton(this.showRewindButton, r3, this.rewindButton);
        updateButton(this.showFastForwardButton, r4, this.fastForwardButton);
        updateButton(this.showNextButton, r5, this.nextButton);
        if (this.timeBar == null) goto L15;
        this.timeBar.setEnabled(r1);
        return;
    L15:
        return;
    }

    private void updatePlayPauseButton() {
        if (isVisible() == true) goto L5;
        return;
    L5:
        if (this.isAttachedToWindow == false) goto L50;
        boolean r0 = false;
        boolean r1 = false;
        boolean r2 = Util.shouldShowPlayButton(this.player, this.showPlayButtonIfSuppressed);
        int r4 = 8;
        boolean r5 = true;
        if (this.playButton == null) goto L26;
        if (r2 == false) goto L11;
    L13:
        boolean r3 = false;
    L14:
        r0 = r3;
        if (r2 == false) goto L17;
    L19:
        boolean r6 = false;
    L20:
        r1 = r6;
        View r7 = this.playButton;
        if (r2 == false) goto L23;
        int r8 = 0;
    L24:
        r7.setVisibility(r8);
        goto L26
    L23:
        r8 = 8;
        goto L24
    L17:
        if (this.playButton.isAccessibilityFocused() == false) goto L19;
        r6 = true;
        goto L20
    L11:
        if (this.playButton.isFocused() == false) goto L13;
        r3 = true;
    L26:
        if (this.pauseButton == null) goto L43;
        if (r2 == true) goto L29;
    L31:
        boolean r9 = false;
    L32:
        r0 = r0 | r9;
        if (r2 == true) goto L35;
    L37:
        r5 = false;
    L38:
        r1 = r1 | r5;
        View r10 = this.pauseButton;
        if (r2 == true) goto L42;
        r4 = 0;
    L42:
        r10.setVisibility(r4);
        goto L43
    L35:
        if (this.pauseButton.isAccessibilityFocused() == false) goto L37;
    L29:
        if (this.pauseButton.isFocused() == false) goto L31;
        r9 = true;
    L43:
        if (r0 == false) goto L45;
        requestPlayPauseFocus();
    L45:
        if (r1 == false) goto L49;
        requestPlayPauseAccessibilityFocus();
        return;
    L49:
        return;
    }

    private void updateProgress() {
        if (isVisible() == true) goto L5;
        return;
    L5:
        if (this.isAttachedToWindow == false) goto L57;
        Player r1 = this.player;
        long r2 = 0;
        long r4 = 0;
        if (r1 == null) goto L10;
        r2 = this.currentWindowOffset + r1.getContentPosition();
        r4 = this.currentWindowOffset + r1.getContentBufferedPosition();
    L10:
        boolean r7 = false;
        if (r2 == this.currentPosition) goto L13;
        boolean r6 = true;
    L15:
        if (r4 == this.currentBufferedPosition) goto L17;
        r7 = true;
    L17:
        this.currentPosition = r2;
        this.currentBufferedPosition = r4;
        if (this.positionView == null) goto L24;
        if (this.scrubbing == true) goto L24;
        if (r6 == false) goto L24;
        this.positionView.setText(Util.getStringForTime(this.formatBuilder, this.formatter, r2));
    L24:
        if (this.timeBar == null) goto L27;
        this.timeBar.setPosition(r2);
        this.timeBar.setBufferedPosition(r4);
    L27:
        if (this.progressUpdateListener == null) goto L31;
        if (r6 == true) goto L30;
        if (r7 == false) goto L31;
    L30:
        this.progressUpdateListener.onProgressUpdate(r2, r4);
    L31:
        removeCallbacks(this.updateProgressAction);
        if (r1 != null) goto L34;
        int r9 = 1;
    L35:
        long r10 = 1000;
        if (r1 == null) goto L48;
        if (r1.isPlaying() == false) goto L48;
        if (this.timeBar == null) goto L42;
        long r12 = this.timeBar.getPreferredUpdateDelay();
    L43:
        long r13 = Math.min(r12, 1000 - (r2 % 1000));
        float r8 = r1.getPlaybackParameters().speed;
        if (r8 <= 0.0f) goto L46;
        r10 = (long) (r13 / r8);
    L46:
        postDelayed(this.updateProgressAction, Util.constrainValue(r10, this.timeBarMinUpdateIntervalMs, 1000));
        return;
    L42:
        r12 = 1000;
    L48:
        if (r9 == 4) goto L55;
        if (r9 == 1) goto L56;
        postDelayed(this.updateProgressAction, 1000);
        return;
    L56:
        return;
    L55:
        return;
    L34:
        r9 = r1.getPlaybackState();
        goto L35
    L13:
        r6 = false;
        goto L15
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
        updateButton(false, false, this.repeatToggleButton);
        return;
    L13:
        Player r0 = this.player;
        if (r0 != null) goto L17;
        updateButton(true, false, this.repeatToggleButton);
        this.repeatToggleButton.setImageDrawable(this.repeatOffButtonDrawable);
        this.repeatToggleButton.setContentDescription(this.repeatOffButtonContentDescription);
        return;
    L17:
        updateButton(true, true, this.repeatToggleButton);
        switch(r0.getRepeatMode()) {
            case 0: goto L22;
            case 1: goto L21;
            case 2: goto L20;
            default: goto L23;
        };
    L20:
        this.repeatToggleButton.setImageDrawable(this.repeatAllButtonDrawable);
        this.repeatToggleButton.setContentDescription(this.repeatAllButtonContentDescription);
        goto L23
    L21:
        this.repeatToggleButton.setImageDrawable(this.repeatOneButtonDrawable);
        this.repeatToggleButton.setContentDescription(this.repeatOneButtonContentDescription);
        goto L23
    L22:
        this.repeatToggleButton.setImageDrawable(this.repeatOffButtonDrawable);
        this.repeatToggleButton.setContentDescription(this.repeatOffButtonContentDescription);
    L23:
        this.repeatToggleButton.setVisibility(0);
    }

    private void updateShuffleButton() {
        if (isVisible() == true) goto L5;
        return;
    L5:
        if (this.isAttachedToWindow == true) goto L7;
        return;
    L7:
        if (this.shuffleButton == null) goto L29;
        Player r0 = this.player;
        if (this.showShuffleButton == true) goto L13;
        updateButton(false, false, this.shuffleButton);
        return;
    L13:
        if (r0 != null) goto L15;
        updateButton(true, false, this.shuffleButton);
        this.shuffleButton.setImageDrawable(this.shuffleOffButtonDrawable);
        this.shuffleButton.setContentDescription(this.shuffleOffContentDescription);
        return;
    L15:
        updateButton(true, true, this.shuffleButton);
        ImageView r1 = this.shuffleButton;
        if (r0.getShuffleModeEnabled() == false) goto L18;
        Drawable r2 = this.shuffleOnButtonDrawable;
    L19:
        r1.setImageDrawable(r2);
        ImageView r3 = this.shuffleButton;
        if (r0.getShuffleModeEnabled() == false) goto L22;
        String r4 = this.shuffleOnContentDescription;
    L23:
        r3.setContentDescription(r4);
        return;
    L22:
        r4 = this.shuffleOffContentDescription;
        goto L23
    L18:
        r2 = this.shuffleOffButtonDrawable;
        goto L19
    }

    private void updateTimeline() {
        Player r1 = this.player;
        if (r1 != null) goto L5;
        return;
    L5:
        boolean r4 = true;
        if (this.showMultiWindowTimeBar == true) goto L8;
    L10:
        boolean r2 = false;
    L11:
        this.multiWindowTimeBar = r2;
        long r5 = 0;
        this.currentWindowOffset = 0;
        long r7 = 0;
        int r3 = 0;
        Timeline r9 = r1.getCurrentTimeline();
        if (r9.isEmpty() == true) goto L57;
        int r10 = r1.getCurrentMediaItemIndex();
        if (this.multiWindowTimeBar == false) goto L16;
        int r11 = 0;
    L18:
        if (this.multiWindowTimeBar == false) goto L20;
        int r12 = r9.getWindowCount() - 1;
    L21:
        int r13 = r11;
    L22:
        if (r13 > r12) goto L57;
        if (r13 != r10) goto L25;
        this.currentWindowOffset = Util.usToMs(r7);
    L25:
        r9.getWindow(r13, this.window);
        if (this.window.durationUs == C0180C.TIME_UNSET) goto L27;
        int r14 = this.window.firstPeriodIndex;
    L30:
        if (r14 > this.window.lastPeriodIndex) goto L54;
        r9.getPeriod(r14, this.period);
        int r15 = this.period.getRemovedAdGroupCount();
        int r6 = this.period.getAdGroupCount();
        long r27 = r5;
        int r8 = r15;
    L32:
        if (r8 >= r6) goto L53;
        long r20 = this.period.getAdGroupTimeUs(r8);
        if (r20 != Long.MIN_VALUE) goto L39;
        int r23 = r6;
        if (this.period.durationUs != C0180C.TIME_UNSET) goto L38;
        r1 = r1;
    L52:
        r8 = r8 + 1;
        r6 = r23;
        r1 = r1;
        goto L32
    L38:
        r20 = this.period.durationUs;
    L40:
        long r16 = r20 + this.period.getPositionInWindowUs();
        if (r16 >= r27) goto L43;
        r1 = r1;
        goto L52
    L43:
        if (r3 != this.adGroupTimesMs.length) goto L50;
        if (this.adGroupTimesMs.length != 0) goto L47;
        int r17 = 1;
    L48:
        this.adGroupTimesMs = Arrays.copyOf(this.adGroupTimesMs, r17);
        this.playedAdGroups = Arrays.copyOf(this.playedAdGroups, r17);
        goto L50
    L47:
        r17 = this.adGroupTimesMs.length * 2;
    L50:
        this.adGroupTimesMs[r3] = Util.usToMs(r7 + r16);
        this.playedAdGroups[r3] = this.period.hasPlayedAdGroup(r8);
        r3 = r3 + 1;
        goto L52
    L39:
        r23 = r6;
        goto L40
    L53:
        r14 = r14 + 1;
        r5 = r27;
        goto L30
    L54:
        r7 = r7 + this.window.durationUs;
        r13 = r13 + 1;
        r1 = r1;
        r4 = true;
        goto L22
    L27:
        Assertions.checkState(r4 ^ this.multiWindowTimeBar);
        goto L57
    L20:
        r12 = r10;
        goto L21
    L16:
        r11 = r10;
    L57:
        long r18 = Util.usToMs(r7);
        if (this.durationView == null) goto L61;
        this.durationView.setText(Util.getStringForTime(this.formatBuilder, this.formatter, r18));
    L61:
        if (this.timeBar == null) goto L66;
        this.timeBar.setDuration(r18);
        int r19 = this.extraAdGroupTimesMs.length;
        int r21 = r3 + r19;
        if (r21 <= this.adGroupTimesMs.length) goto L65;
        this.adGroupTimesMs = Arrays.copyOf(this.adGroupTimesMs, r21);
        this.playedAdGroups = Arrays.copyOf(this.playedAdGroups, r21);
    L65:
        System.arraycopy(this.extraAdGroupTimesMs, 0, this.adGroupTimesMs, r3, r19);
        System.arraycopy(this.extraPlayedAdGroups, 0, this.playedAdGroups, r3, r19);
        this.timeBar.setAdGroupTimesMs(this.adGroupTimesMs, this.playedAdGroups, r21);
    L66:
        updateProgress();
        return;
    L8:
        if (canShowMultiWindowTimeBar(r1.getCurrentTimeline(), this.window) == false) goto L10;
        r2 = true;
        goto L11
    }

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
        if (r0 != 90) goto L15;
        if (r1.getPlaybackState() == 4) goto L31;
        r1.seekForward();
        return true;
    L31:
        return true;
    L15:
        if (r0 != 89) goto L18;
        r1.seekBack();
        return true;
    L18:
        if (r5.getRepeatCount() != 0) goto L34;
        switch(r0) {
            case 79: goto L25;
            case 85: goto L25;
            case 87: goto L24;
            case 88: goto L23;
            case 126: goto L22;
            case 127: goto L21;
            default: goto L39;
        };
    L21:
        Util.handlePauseButtonAction(r1);
        return true;
    L22:
        Util.handlePlayButtonAction(r1);
        return true;
    L23:
        r1.seekToPrevious();
        return true;
    L24:
        r1.seekToNext();
        return true;
    L25:
        Util.handlePlayPauseButtonAction(r1, this.showPlayButtonIfSuppressed);
        return true;
    L39:
        return true;
    L34:
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchTouchEvent(MotionEvent r3) {
        if (r3.getAction() != 0) goto L6;
        removeCallbacks(this.hideAction);
    L9:
        return super.dispatchTouchEvent(r3);
    L6:
        if (r3.getAction() != 1) goto L9;
        hideAfterTimeout();
        goto L9
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getRepeatToggleModes() {
        return this.repeatToggleModes;
    }

    public boolean getShowShuffleButton() {
        return this.showShuffleButton;
    }

    public int getShowTimeoutMs() {
        return this.showTimeoutMs;
    }

    public boolean getShowVrButton() {
        if (this.vrButton != null) goto L5;
    L7:
        return false;
    L5:
        if (this.vrButton.getVisibility() != 0) goto L7;
        return true;
    }

    public void hide() {
        if (isVisible() == false) goto L11;
        setVisibility(8);
        Iterator<VisibilityListener> r0 = this.visibilityListeners.iterator();
    L6:
        if (r0.hasNext() == false) goto L8;
        r0.next().onVisibilityChange(getVisibility());
        goto L6
    L8:
        removeCallbacks(this.updateProgressAction);
        removeCallbacks(this.hideAction);
        this.hideAtMs = C0180C.TIME_UNSET;
        return;
    }

    public boolean isVisible() {
        if (getVisibility() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.isAttachedToWindow = true;
        if (this.hideAtMs == C0180C.TIME_UNSET) goto L9;
        long r0 = this.hideAtMs - SystemClock.uptimeMillis();
        if (r0 > 0) goto L7;
        hide();
    L12:
        updateAll();
        return;
    L7:
        postDelayed(this.hideAction, r0);
        goto L12
    L9:
        if (isVisible() == false) goto L12;
        hideAfterTimeout();
        goto L12
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isAttachedToWindow = false;
        removeCallbacks(this.updateProgressAction);
        removeCallbacks(this.hideAction);
    }

    public void removeVisibilityListener(VisibilityListener r2) {
        this.visibilityListeners.remove(r2);
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

    public void setRepeatToggleModes(int r4) {
        this.repeatToggleModes = r4;
        if (this.player == null) goto L15;
        int r0 = this.player.getRepeatMode();
        if (r4 != 0) goto L9;
        if (r0 == 0) goto L9;
        this.player.setRepeatMode(0);
    L9:
        if (r4 != 1) goto L12;
        if (r0 != 2) goto L12;
        this.player.setRepeatMode(1);
    L12:
        if (r4 != 2) goto L15;
        if (r0 != 1) goto L15;
        this.player.setRepeatMode(2);
    L15:
        updateRepeatModeButton();
    }

    public void setShowFastForwardButton(boolean r1) {
        this.showFastForwardButton = r1;
        updateNavigation();
    }

    @Deprecated
    public void setShowMultiWindowTimeBar(boolean r1) {
        this.showMultiWindowTimeBar = r1;
        updateTimeline();
    }

    public void setShowNextButton(boolean r1) {
        this.showNextButton = r1;
        updateNavigation();
    }

    public void setShowPlayButtonIfPlaybackIsSuppressed(boolean r1) {
        this.showPlayButtonIfSuppressed = r1;
        updatePlayPauseButton();
    }

    public void setShowPreviousButton(boolean r1) {
        this.showPreviousButton = r1;
        updateNavigation();
    }

    public void setShowRewindButton(boolean r1) {
        this.showRewindButton = r1;
        updateNavigation();
    }

    public void setShowShuffleButton(boolean r1) {
        this.showShuffleButton = r1;
        updateShuffleButton();
    }

    public void setShowTimeoutMs(int r2) {
        this.showTimeoutMs = r2;
        if (isVisible() == false) goto L6;
        hideAfterTimeout();
        return;
    }

    public void setShowVrButton(boolean r3) {
        if (this.vrButton == null) goto L10;
        View r0 = this.vrButton;
        if (r3 == false) goto L7;
        int r1 = 0;
    L8:
        r0.setVisibility(r1);
        return;
    L7:
        r1 = 8;
        goto L8
    }

    public void setTimeBarMinUpdateInterval(int r3) {
        this.timeBarMinUpdateIntervalMs = Util.constrainValue(r3, 16, 1000);
    }

    public void setVrButtonListener(View.OnClickListener r4) {
        if (this.vrButton == null) goto L10;
        this.vrButton.setOnClickListener(r4);
        boolean r0 = getShowVrButton();
        if (r4 == null) goto L7;
        boolean r1 = true;
    L8:
        updateButton(r0, r1, this.vrButton);
        return;
    L7:
        r1 = false;
        goto L8
    }

    public void show() {
        if (isVisible() == true) goto L9;
        setVisibility(0);
        Iterator<VisibilityListener> r0 = this.visibilityListeners.iterator();
    L6:
        if (r0.hasNext() == false) goto L8;
        r0.next().onVisibilityChange(getVisibility());
        goto L6
    L8:
        updateAll();
        requestPlayPauseFocus();
        requestPlayPauseAccessibilityFocus();
    L9:
        hideAfterTimeout();
    }
}
