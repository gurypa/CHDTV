package androidx.media3.p002ui;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.AttachedSurfaceControl;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.window.SurfaceSyncGroup;
import androidx.core.content.ContextCompat;
import androidx.media3.common.AdOverlayInfo;
import androidx.media3.common.AdViewProvider;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.DeviceInfo;
import androidx.media3.common.ErrorMessageProvider;
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
import androidx.media3.common.util.Util;
import com.google.common.collect.ImmutableList;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;

/* JADX INFO: loaded from: classes.dex */
public class PlayerView extends FrameLayout implements AdViewProvider {
    public static final int ARTWORK_DISPLAY_MODE_FILL = 2;
    public static final int ARTWORK_DISPLAY_MODE_FIT = 1;
    public static final int ARTWORK_DISPLAY_MODE_OFF = 0;
    public static final int IMAGE_DISPLAY_MODE_FILL = 1;
    public static final int IMAGE_DISPLAY_MODE_FIT = 0;
    public static final int SHOW_BUFFERING_ALWAYS = 2;
    public static final int SHOW_BUFFERING_NEVER = 0;
    public static final int SHOW_BUFFERING_WHEN_PLAYING = 1;
    private static final int SURFACE_TYPE_NONE = 0;
    private static final int SURFACE_TYPE_SPHERICAL_GL_SURFACE_VIEW = 3;
    private static final int SURFACE_TYPE_SURFACE_VIEW = 1;
    private static final int SURFACE_TYPE_TEXTURE_VIEW = 2;
    private static final int SURFACE_TYPE_VIDEO_DECODER_GL_SURFACE_VIEW = 4;
    private final FrameLayout adOverlayFrameLayout;
    private int artworkDisplayMode;
    private final ImageView artworkView;
    private final View bufferingView;
    private final ComponentListener componentListener;
    private final AspectRatioFrameLayout contentFrame;
    private final PlayerControlView controller;
    private boolean controllerAutoShow;
    private boolean controllerHideDuringAds;
    private boolean controllerHideOnTouch;
    private int controllerShowTimeoutMs;
    private ControllerVisibilityListener controllerVisibilityListener;
    private CharSequence customErrorMessage;
    private Drawable defaultArtwork;
    private boolean enableComposeSurfaceSyncWorkaround;
    private ErrorMessageProvider<? super PlaybackException> errorMessageProvider;
    private final TextView errorMessageView;
    private final Class<?> exoPlayerClazz;
    private FullscreenButtonClickListener fullscreenButtonClickListener;
    private int imageDisplayMode;
    private final Object imageOutput;
    private final ImageView imageView;
    private boolean keepContentOnPlayerReset;
    private PlayerControlView.VisibilityListener legacyControllerVisibilityListener;
    private final Handler mainLooperHandler;
    private final FrameLayout overlayFrameLayout;
    private Player player;
    private final Method setImageOutputMethod;
    private int showBuffering;
    private final View shutterView;
    private final SubtitleView subtitleView;
    private final SurfaceSyncGroupCompatV34 surfaceSyncGroupV34;
    private final View surfaceView;
    private final boolean surfaceViewIgnoresVideoAspectRatio;
    private boolean useController;

    /* JADX INFO: renamed from: androidx.media3.ui.PlayerView$1 */
    static /* synthetic */ class C04661 {
    }

    private static class Api34 {
        private Api34() {
        }

        public static void setSurfaceLifecycleToFollowsAttachment(SurfaceView r1) {
            r1.setSurfaceLifecycle(2);
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ArtworkDisplayMode {
    }

    private final class ComponentListener implements Player.Listener, View.OnClickListener, PlayerControlView.VisibilityListener, PlayerControlView.OnFullScreenModeChangedListener {
        private Object lastPeriodUidWithTracks;
        private final Timeline.Period period;
        final /* synthetic */ PlayerView this$0;

        public ComponentListener(PlayerView r1) {
            this.this$0 = r1;
            this.period = new Timeline.Period();
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
        public void onClick(View r2) {
            PlayerView.access$1800(this.this$0);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onCues(CueGroup r3) {
            if (PlayerView.access$100(this.this$0) == null) goto L6;
            PlayerView.access$100(this.this$0).setCues(r3.cues);
            return;
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

        @Override // androidx.media3.ui.PlayerControlView.OnFullScreenModeChangedListener
        public void onFullScreenModeChanged(boolean r2) {
            if (PlayerView.access$2100(this.this$0) == null) goto L6;
            PlayerView.access$2100(this.this$0).onFullscreenButtonClick(r2);
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
        public void onPlayWhenReadyChanged(boolean r2, int r3) {
            PlayerView.access$1300(this.this$0);
            PlayerView.access$1500(this.this$0);
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onPlaybackParametersChanged(PlaybackParameters r1) {
            Player.Listener.CC.$default$onPlaybackParametersChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlaybackStateChanged(int r2) {
            PlayerView.access$1300(this.this$0);
            PlayerView.access$1400(this.this$0);
            PlayerView.access$1500(this.this$0);
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
            if (PlayerView.access$1600(this.this$0) == true) goto L5;
            return;
        L5:
            if (PlayerView.access$1700(this.this$0) == false) goto L9;
            this.this$0.hideController();
            return;
        }

        @Override // androidx.media3.common.Player.Listener
        public void onRenderedFirstFrame() {
            if (PlayerView.access$800(this.this$0) == null) goto L9;
            PlayerView.access$800(this.this$0).setVisibility(4);
            if (PlayerView.access$900(this.this$0) == false) goto L7;
            PlayerView.access$1000(this.this$0);
            return;
        L7:
            PlayerView.access$1100(this.this$0);
            return;
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
        public void onSurfaceSizeChanged(int r6, int r7) {
            if (Util.SDK_INT == 34) goto L5;
            return;
        L5:
            if ((PlayerView.access$400(this.this$0) instanceof SurfaceView) == true) goto L7;
            return;
        L7:
            if (PlayerView.access$500(this.this$0) == false) goto L12;
            SurfaceSyncGroupCompatV34 r0 = (SurfaceSyncGroupCompatV34) Assertions.checkNotNull(PlayerView.access$700(this.this$0));
            Handler r1 = PlayerView.access$600(this.this$0);
            SurfaceView r2 = (SurfaceView) PlayerView.access$400(this.this$0);
            final PlayerView r3 = this.this$0;
            r0.postRegister(r1, r2, new PlayerView$ComponentListener$$ExternalSyntheticLambda0(r3));
            return;
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
        public void onTracksChanged(Tracks r7) {
            Player r0 = (Player) Assertions.checkNotNull(PlayerView.access$200(this.this$0));
            if (r0.isCommandAvailable(17) == false) goto L5;
            Timeline r1 = r0.getCurrentTimeline();
        L7:
            if (r1.isEmpty() == false) goto L10;
            this.lastPeriodUidWithTracks = null;
        L22:
            PlayerView.access$1200(this.this$0, false);
            return;
        L10:
            if (r0.isCommandAvailable(30) == false) goto L15;
            if (r0.getCurrentTracks().isEmpty() == true) goto L15;
            this.lastPeriodUidWithTracks = r1.getPeriod(r0.getCurrentPeriodIndex(), this.period, true).uid;
        L15:
            if (this.lastPeriodUidWithTracks == null) goto L22;
            int r2 = r1.getIndexOfPeriod(this.lastPeriodUidWithTracks);
            if (r2 == (-1)) goto L21;
            int r4 = r1.getPeriod(r2, this.period).windowIndex;
            if (r0.getCurrentMediaItemIndex() != r4) goto L21;
            return;
        L21:
            this.lastPeriodUidWithTracks = null;
            goto L22
        L5:
            r1 = Timeline.EMPTY;
            goto L7
        }

        @Override // androidx.media3.common.Player.Listener
        public void onVideoSizeChanged(VideoSize r3) {
            if (r3.equals(VideoSize.UNKNOWN) == false) goto L5;
            return;
        L5:
            if (PlayerView.access$200(this.this$0) != null) goto L7;
            return;
        L7:
            if (PlayerView.access$200(this.this$0).getPlaybackState() == 1) goto L13;
            PlayerView.access$300(this.this$0);
            return;
        }

        @Override // androidx.media3.ui.PlayerControlView.VisibilityListener
        public void onVisibilityChange(int r2) {
            PlayerView.access$1900(this.this$0);
            if (PlayerView.access$2000(this.this$0) == null) goto L6;
            PlayerView.access$2000(this.this$0).onVisibilityChanged(r2);
            return;
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onVolumeChanged(float r1) {
            Player.Listener.CC.$default$onVolumeChanged(this, r1);
        }
    }

    public interface ControllerVisibilityListener {
        void onVisibilityChanged(int r1);
    }

    public interface FullscreenButtonClickListener {
        void onFullscreenButtonClick(boolean r1);
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ImageDisplayMode {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShowBuffering {
    }

    private static final class SurfaceSyncGroupCompatV34 {
        SurfaceSyncGroup surfaceSyncGroup;

        private SurfaceSyncGroupCompatV34() {
        }

        /* synthetic */ SurfaceSyncGroupCompatV34(C04661 r1) {
            this();
        }

        static /* synthetic */ void lambda$postRegister$0() {
        }

        /* JADX INFO: renamed from: lambda$postRegister$1$androidx-media3-ui-PlayerView$SurfaceSyncGroupCompatV34 */
        /* synthetic */ void m175xd2b35cc8(SurfaceView r4, Runnable r5) {
            AttachedSurfaceControl r0 = r4.getRootSurfaceControl();
            if (r0 != null) goto L5;
            return;
        L5:
            this.surfaceSyncGroup = new SurfaceSyncGroup("exo-sync-b-334901521");
            Assertions.checkState(this.surfaceSyncGroup.add(r0, new PlayerView$SurfaceSyncGroupCompatV34$$ExternalSyntheticLambda1()));
            r5.run();
            r0.applyTransactionOnDraw(new SurfaceControl.Transaction());
        }

        public void maybeMarkSyncReadyAndClear() {
            if (this.surfaceSyncGroup == null) goto L6;
            this.surfaceSyncGroup.markSyncReady();
            this.surfaceSyncGroup = null;
            return;
        }

        public void postRegister(Handler r2, final SurfaceView r3, final Runnable r4) {
            r2.post(new PlayerView$SurfaceSyncGroupCompatV34$$ExternalSyntheticLambda0(this, r3, r4));
        }
    }

    public PlayerView(Context r2) {
        this(r2, null);
    }

    public PlayerView(Context r2, AttributeSet r3) {
        this(r2, r3, 0);
    }

    public PlayerView(Context r30, AttributeSet r31, int r32) {
        super(r30, r31, r32);
        this.componentListener = new ComponentListener(this);
        this.mainLooperHandler = new Handler(Looper.getMainLooper());
        if (isInEditMode() == false) goto L10;
        this.contentFrame = null;
        this.shutterView = null;
        this.surfaceView = null;
        this.surfaceViewIgnoresVideoAspectRatio = false;
        this.surfaceSyncGroupV34 = null;
        this.imageView = null;
        this.artworkView = null;
        this.subtitleView = null;
        this.bufferingView = null;
        this.errorMessageView = null;
        this.controller = null;
        this.adOverlayFrameLayout = null;
        this.overlayFrameLayout = null;
        this.exoPlayerClazz = null;
        this.setImageOutputMethod = null;
        this.imageOutput = null;
        ImageView r0 = new ImageView(r30);
        if (Util.SDK_INT < 23) goto L7;
        configureEditModeLogoV23(r30, getResources(), r0);
    L8:
        addView(r0);
        return;
    L7:
        configureEditModeLogo(r30, getResources(), r0);
        goto L8
    L10:
        int r8 = C0467R.layout.exo_player_view;
        boolean r17 = false;
        if (r31 == null) goto L47;
        TypedArray r1 = r30.getTheme().obtainStyledAttributes(r31, C0467R.styleable.PlayerView, r32, 0);
        boolean r2 = r1.hasValue(C0467R.styleable.PlayerView_shutter_background_color);     // Catch: Throwable -> L43
        int r3 = r1.getColor(C0467R.styleable.PlayerView_shutter_background_color, 0);     // Catch: Throwable -> L41
        int r4 = r1.getResourceId(C0467R.styleable.PlayerView_player_layout_id, r8);     // Catch: Throwable -> L41
        boolean r5 = r1.getBoolean(C0467R.styleable.PlayerView_use_artwork, true);     // Catch: Throwable -> L41
        int r6 = r1.getInt(C0467R.styleable.PlayerView_artwork_display_mode, 1);     // Catch: Throwable -> L41
        int r7 = r1.getResourceId(C0467R.styleable.PlayerView_default_artwork, 0);     // Catch: Throwable -> L41
        int r9 = r1.getInt(C0467R.styleable.PlayerView_image_display_mode, 0);     // Catch: Throwable -> L41
        boolean r10 = r1.getBoolean(C0467R.styleable.PlayerView_use_controller, true);     // Catch: Throwable -> L41
        int r11 = r1.getInt(C0467R.styleable.PlayerView_surface_type, 1);     // Catch: Throwable -> L41
        int r12 = r1.getInt(C0467R.styleable.PlayerView_resize_mode, 0);     // Catch: Throwable -> L41
        int r13 = r1.getInt(C0467R.styleable.PlayerView_show_timeout, 5000);     // Catch: Throwable -> L41
        boolean r14 = r1.getBoolean(C0467R.styleable.PlayerView_hide_on_touch, true);     // Catch: Throwable -> L41
        boolean r15 = r1.getBoolean(C0467R.styleable.PlayerView_auto_show, true);     // Catch: Throwable -> L37
        int r16 = 0;
        r16 = r1.getInteger(C0467R.styleable.PlayerView_show_buffering, 0);     // Catch: Throwable -> L33
        final PlayerView r18 = this;
        r18.keepContentOnPlayerReset = r1.getBoolean(C0467R.styleable.PlayerView_keep_content_on_player_reset, r18.keepContentOnPlayerReset);     // Catch: Throwable -> L31
        boolean r19 = r1.getBoolean(C0467R.styleable.PlayerView_hide_during_ads, true);     // Catch: Throwable -> L29
        r1.recycle();
        r17 = r2;
        int r20 = r12;
        int r110 = r11;
        boolean r111 = r10;
        int r112 = r7;
        int r113 = r9;
        int r114 = r6;
        boolean r115 = r5;
        int r21 = r4;
        int r22 = r3;
        boolean r23 = r15;
        boolean r24 = r19;
        int r116 = r13;
        boolean r25 = r14;
        int r26 = r16;
    L48:
        LayoutInflater.from(r30).inflate(r21, r18);
        r18.setDescendantFocusability(262144);
        r18.contentFrame = (AspectRatioFrameLayout) r18.findViewById(C0467R.id.exo_content_frame);
        if (r18.contentFrame == null) goto L51;
        setResizeModeRaw(r18.contentFrame, r20);
    L51:
        r18.shutterView = r18.findViewById(C0467R.id.exo_shutter);
        if (r18.shutterView == null) goto L55;
        if (r17 == false) goto L55;
        r18.shutterView.setBackgroundColor(r22);
    L55:
        boolean r117 = false;
        if (r18.contentFrame == null) goto L82;
        if (r110 == 0) goto L82;
        ViewGroup.LayoutParams r27 = new ViewGroup.LayoutParams(-1, -1);
        switch(r110) {
            case 2: goto L79;
            case 3: goto L164;
            case 4: goto L168;
            default: goto L60;
        };
    L60:
        Context r28 = r30;
        SurfaceView r29 = new SurfaceView(r28);
        if (Util.SDK_INT < 34) goto L80;
        Api34.setSurfaceLifecycleToFollowsAttachment(r29);
    L80:
        r18.surfaceView = r29;
    L81:
        r18.surfaceView.setLayoutParams(r27);
        r18.surfaceView.setOnClickListener(r18.componentListener);
        r18.surfaceView.setClickable(false);
        r18.contentFrame.addView(r18.surfaceView, 0);
        C04661 r33 = null;
        boolean r34 = r117;
    L83:
        r18.surfaceViewIgnoresVideoAspectRatio = r34;
        if (Util.SDK_INT != 34) goto L86;
        SurfaceSyncGroupCompatV34 r35 = new SurfaceSyncGroupCompatV34(r33);
    L87:
        r18.surfaceSyncGroupV34 = r35;
        r18.adOverlayFrameLayout = (FrameLayout) r18.findViewById(C0467R.id.exo_ad_overlay);
        r18.overlayFrameLayout = (FrameLayout) r18.findViewById(C0467R.id.exo_overlay);
        r18.imageView = (ImageView) r18.findViewById(C0467R.id.exo_image);
        r18.imageDisplayMode = r113;
        Class<?> r36 = Class.forName("androidx.media3.exoplayer.ExoPlayer");     // Catch: Throwable -> L96 ClassNotFoundException -> L98
        Class<?> r37 = Class.forName("androidx.media3.exoplayer.image.ImageOutput");     // Catch: Throwable -> L96 ClassNotFoundException -> L98
        Method r38 = r36.getMethod("setImageOutput", new Class[]{r37});     // Catch: NoSuchMethodException -> L92 ClassNotFoundException -> L94
        Object r118 = Proxy.newProxyInstance(r37.getClassLoader(), new Class[]{r37}, new PlayerView$$ExternalSyntheticLambda0(r18));     // Catch: NoSuchMethodException -> L92 ClassNotFoundException -> L94
        Class<?> r39 = r36;
    L101:
        r18.exoPlayerClazz = r39;
        r18.setImageOutputMethod = r38;
        r18.imageOutput = r118;
        r18.artworkView = (ImageView) r18.findViewById(C0467R.id.exo_artwork);
        if (r115 == false) goto L107;
        if (r114 == 0) goto L107;
        if (r18.artworkView == null) goto L107;
        boolean r40 = true;
    L109:
        if (r40 == false) goto L111;
        int r41 = r114;
    L112:
        r18.artworkDisplayMode = r41;
        if (r112 == 0) goto L115;
        r18.defaultArtwork = ContextCompat.getDrawable(r18.getContext(), r112);
    L115:
        r18.subtitleView = (SubtitleView) r18.findViewById(C0467R.id.exo_subtitles);
        if (r18.subtitleView == null) goto L118;
        r18.subtitleView.setUserDefaultStyle();
        r18.subtitleView.setUserDefaultTextSize();
    L118:
        r18.bufferingView = r18.findViewById(C0467R.id.exo_buffering);
        if (r18.bufferingView == null) goto L122;
        r18.bufferingView.setVisibility(8);
    L122:
        r18.showBuffering = r26;
        r18.errorMessageView = (TextView) r18.findViewById(C0467R.id.exo_error_message);
        if (r18.errorMessageView == null) goto L125;
        r18.errorMessageView.setVisibility(8);
    L125:
        PlayerControlView r42 = (PlayerControlView) r18.findViewById(C0467R.id.exo_controller);
        View r43 = r18.findViewById(C0467R.id.exo_controller_placeholder);
        if (r42 == null) goto L128;
        r18.controller = r42;
    L132:
        if (r18.controller == null) goto L134;
        int r44 = r116;
    L135:
        r18.controllerShowTimeoutMs = r44;
        r18.controllerHideOnTouch = r25;
        r18.controllerAutoShow = r23;
        r18.controllerHideDuringAds = r24;
        if (r111 == true) goto L138;
    L140:
        boolean r45 = false;
    L141:
        r18.useController = r45;
        if (r18.controller == null) goto L144;
        r18.controller.hideImmediately();
        r18.controller.addVisibilityListener(r18.componentListener);
    L144:
        if (r111 == false) goto L146;
        r18.setClickable(true);
    L146:
        r18.updateContentDescription();
        return;
    L138:
        if (r18.controller == null) goto L140;
        r45 = true;
        goto L141
    L134:
        r44 = 0;
        goto L135
    L128:
        if (r43 == null) goto L130;
        r18.controller = new PlayerControlView(r28, null, 0, r31);
        r18.controller.setId(C0467R.id.exo_controller);
        r18.controller.setLayoutParams(r43.getLayoutParams());
        ViewGroup r46 = (ViewGroup) r43.getParent();
        int r47 = r46.indexOfChild(r43);
        r46.removeView(r43);
        r46.addView(r18.controller, r47);
        goto L132
    L130:
        r18.controller = null;
        goto L132
    L111:
        r41 = 0;
    L107:
        r40 = false;
        goto L109
    L100:
        r118 = null;
        r39 = null;
        r38 = null;
        goto L101
    L86:
        r35 = null;
        goto L87
    L79:
        r28 = r30;
        r18.surfaceView = new TextureView(r28);
        goto L81
    L168:
        r18.surfaceView = (View) Class.forName("androidx.media3.exoplayer.video.VideoDecoderGLSurfaceView").getConstructor(new Class[]{Context.class}).newInstance(new Object[]{r30});     // Catch: Exception -> L67
        r28 = r30;
    L67:
        e = e;
    L72:
        throw new IllegalStateException("video_decoder_gl_surface_view requires an ExoPlayer dependency", e);
    L69:
        e = e;
        goto L72
    L164:
        r18.surfaceView = (View) Class.forName("androidx.media3.exoplayer.video.spherical.SphericalGLSurfaceView").getConstructor(new Class[]{Context.class}).newInstance(new Object[]{r30});     // Catch: Exception -> L76
        r28 = r30;
        r117 = true;
    L76:
        e = move-exception;
        throw new IllegalStateException("spherical_gl_surface_view requires an ExoPlayer dependency", e);
    L82:
        r115 = r115;
        r28 = r30;
        r33 = null;
        r18.surfaceView = null;
        r34 = false;
    L29:
        th = th;
    L45:
        r1.recycle();
        throw th;
    L31:
        th = th;
    L33:
        th = th;
    L35:
        th = th;
    L37:
        th = th;
    L39:
        th = th;
    L41:
        th = th;
    L43:
        th = th;
        goto L45
    L47:
        r18 = this;
        r20 = 0;
        r24 = true;
        r110 = 1;
        r111 = true;
        r112 = 0;
        r113 = 0;
        r114 = 1;
        r115 = true;
        r21 = r8;
        r22 = 0;
        r23 = true;
        r116 = 5000;
        r25 = true;
        r26 = 0;
        goto L48
    }

    static /* synthetic */ SubtitleView access$100(PlayerView r1) {
        return r1.subtitleView;
    }

    static /* synthetic */ void access$1000(PlayerView r0) {
        r0.hideImage();
    }

    static /* synthetic */ void access$1100(PlayerView r0) {
        r0.hideAndClearImage();
    }

    static /* synthetic */ void access$1200(PlayerView r0, boolean r1) {
        r0.updateForCurrentTrackSelections(r1);
    }

    static /* synthetic */ void access$1300(PlayerView r0) {
        r0.updateBuffering();
    }

    static /* synthetic */ void access$1400(PlayerView r0) {
        r0.updateErrorMessage();
    }

    static /* synthetic */ void access$1500(PlayerView r0) {
        r0.updateControllerVisibility();
    }

    static /* synthetic */ boolean access$1600(PlayerView r1) {
        return r1.isPlayingAd();
    }

    static /* synthetic */ boolean access$1700(PlayerView r1) {
        return r1.controllerHideDuringAds;
    }

    static /* synthetic */ void access$1800(PlayerView r0) {
        r0.toggleControllerVisibility();
    }

    static /* synthetic */ void access$1900(PlayerView r0) {
        r0.updateContentDescription();
    }

    static /* synthetic */ Player access$200(PlayerView r1) {
        return r1.player;
    }

    static /* synthetic */ ControllerVisibilityListener access$2000(PlayerView r1) {
        return r1.controllerVisibilityListener;
    }

    static /* synthetic */ FullscreenButtonClickListener access$2100(PlayerView r1) {
        return r1.fullscreenButtonClickListener;
    }

    static /* synthetic */ void access$300(PlayerView r0) {
        r0.updateAspectRatio();
    }

    static /* synthetic */ View access$400(PlayerView r1) {
        return r1.surfaceView;
    }

    static /* synthetic */ boolean access$500(PlayerView r1) {
        return r1.enableComposeSurfaceSyncWorkaround;
    }

    static /* synthetic */ Handler access$600(PlayerView r1) {
        return r1.mainLooperHandler;
    }

    static /* synthetic */ SurfaceSyncGroupCompatV34 access$700(PlayerView r1) {
        return r1.surfaceSyncGroupV34;
    }

    static /* synthetic */ View access$800(PlayerView r1) {
        return r1.shutterView;
    }

    static /* synthetic */ boolean access$900(PlayerView r1) {
        return r1.hasSelectedImageTrack();
    }

    private void clearImageOutput(Player r5) {
        if (this.exoPlayerClazz != null) goto L5;
        return;
    L5:
        if (this.exoPlayerClazz.isAssignableFrom(r5.getClass()) == false) goto L16;
        ((Method) Assertions.checkNotNull(this.setImageOutputMethod)).invoke(r5, new Object[]{null});     // Catch: Throwable -> L8 IllegalAccessException -> L10
        return;
    L8:
        e = move-exception;
        throw new RuntimeException(e);
    }

    private void closeShutter() {
        if (this.shutterView == null) goto L6;
        this.shutterView.setVisibility(0);
        return;
    }

    private static void configureEditModeLogo(Context r1, Resources r2, ImageView r3) {
        r3.setImageDrawable(Util.getDrawable(r1, r2, C0467R.drawable.exo_edit_mode_logo));
        r3.setBackgroundColor(r2.getColor(C0467R.color.exo_edit_mode_background_color));
    }

    private static void configureEditModeLogoV23(Context r2, Resources r3, ImageView r4) {
        r4.setImageDrawable(Util.getDrawable(r2, r3, C0467R.drawable.exo_edit_mode_logo));
        r4.setBackgroundColor(r3.getColor(C0467R.color.exo_edit_mode_background_color, null));
    }

    private boolean hasSelectedImageTrack() {
        Player r0 = this.player;
        if (r0 != null) goto L5;
    L11:
        return false;
    L5:
        if (this.imageOutput == null) goto L11;
        if (r0.isCommandAvailable(30) == false) goto L11;
        if (r0.getCurrentTracks().isTypeSelected(4) == false) goto L11;
        return true;
    }

    private boolean hasSelectedVideoTrack() {
        Player r0 = this.player;
        if (r0 != null) goto L5;
    L9:
        return false;
    L5:
        if (r0.isCommandAvailable(30) == false) goto L9;
        if (r0.getCurrentTracks().isTypeSelected(2) == false) goto L9;
        return true;
    }

    private void hideAndClearImage() {
        hideImage();
        if (this.imageView == null) goto L6;
        this.imageView.setImageResource(R.color.transparent);
        return;
    }

    private void hideArtwork() {
        if (this.artworkView == null) goto L6;
        this.artworkView.setImageResource(R.color.transparent);
        this.artworkView.setVisibility(4);
        return;
    }

    private void hideImage() {
        if (this.imageView == null) goto L6;
        this.imageView.setVisibility(4);
        return;
    }

    private boolean isDpadKey(int r2) {
        if (r2 != 19) goto L5;
    L22:
        return true;
    L5:
        if (r2 == 270) goto L22;
        if (r2 == 22) goto L22;
        if (r2 == 271) goto L22;
        if (r2 == 20) goto L22;
        if (r2 == 269) goto L22;
        if (r2 == 21) goto L22;
        if (r2 == 268) goto L22;
        if (r2 == 23) goto L22;
        return false;
    }

    private boolean isImageSet() {
        if (this.imageView != null) goto L5;
        return false;
    L5:
        Drawable r0 = this.imageView.getDrawable();
        if (r0 != null) goto L8;
        return false;
    L8:
        if (r0.getAlpha() == 0) goto L12;
        return true;
    L12:
        return false;
    }

    private boolean isPlayingAd() {
        if (this.player != null) goto L5;
    L11:
        return false;
    L5:
        if (this.player.isCommandAvailable(16) == false) goto L11;
        if (this.player.isPlayingAd() == false) goto L11;
        if (this.player.getPlayWhenReady() == false) goto L11;
        return true;
    }

    private void maybeShowController(boolean r3) {
        if (isPlayingAd() == false) goto L8;
        if (this.controllerHideDuringAds == false) goto L8;
        return;
    L8:
        if (useController() == true) goto L10;
        return;
    L10:
        if (this.controller.isFullyVisible() == true) goto L12;
    L14:
        boolean r0 = false;
    L15:
        boolean r1 = shouldShowControllerIndefinitely();
        if (r3 == true) goto L19;
        if (r0 == true) goto L19;
        if (r1 == true) goto L19;
        return;
    L19:
        showController(r1);
        return;
    L12:
        if (this.controller.getShowTimeoutMs() > 0) goto L14;
        r0 = true;
        goto L15
    }

    private void onImageAvailable(final Bitmap r3) {
        this.mainLooperHandler.post(new PlayerView$$ExternalSyntheticLambda1(this, r3));
    }

    private boolean setArtworkFromMediaMetadata(Player r5) {
        if (r5 != null) goto L5;
    L12:
        return false;
    L5:
        if (r5.isCommandAvailable(18) == false) goto L12;
        MediaMetadata r1 = r5.getMediaMetadata();
        if (r1.artworkData != null) goto L10;
        return false;
    L10:
        Bitmap r0 = BitmapFactory.decodeByteArray(r1.artworkData, 0, r1.artworkData.length);
        return setDrawableArtwork(new BitmapDrawable(getResources(), r0));
    }

    private boolean setDrawableArtwork(Drawable r8) {
        if (this.artworkView == null) goto L13;
        if (r8 == null) goto L13;
        int r0 = r8.getIntrinsicWidth();
        int r2 = r8.getIntrinsicHeight();
        if (r0 <= 0) goto L13;
        if (r2 <= 0) goto L13;
        float r3 = r0 / r2;
        ImageView.ScaleType r4 = ImageView.ScaleType.FIT_XY;
        if (this.artworkDisplayMode != 2) goto L11;
        r3 = getWidth() / getHeight();
        r4 = ImageView.ScaleType.CENTER_CROP;
    L11:
        onContentAspectRatioChanged(this.contentFrame, r3);
        this.artworkView.setScaleType(r4);
        this.artworkView.setImageDrawable(r8);
        this.artworkView.setVisibility(0);
        return true;
    L13:
        return false;
    }

    private void setImage(Drawable r2) {
        if (this.imageView != null) goto L5;
        return;
    L5:
        this.imageView.setImageDrawable(r2);
        updateImageViewAspectRatio();
    }

    private void setImageOutput(Player r5) {
        if (this.exoPlayerClazz != null) goto L5;
        return;
    L5:
        if (this.exoPlayerClazz.isAssignableFrom(r5.getClass()) == false) goto L16;
        ((Method) Assertions.checkNotNull(this.setImageOutputMethod)).invoke(r5, new Object[]{Assertions.checkNotNull(this.imageOutput)});     // Catch: Throwable -> L8 IllegalAccessException -> L10
        return;
    L8:
        e = move-exception;
        throw new RuntimeException(e);
    }

    private static void setResizeModeRaw(AspectRatioFrameLayout r0, int r1) {
        r0.setResizeMode(r1);
    }

    private boolean shouldShowControllerIndefinitely() {
        if (this.player != null) goto L5;
        return true;
    L5:
        int r0 = this.player.getPlaybackState();
        if (this.controllerAutoShow == true) goto L8;
    L17:
        return false;
    L8:
        if (this.player.isCommandAvailable(17) == true) goto L10;
    L11:
        if (r0 != 1) goto L13;
        return true;
    L13:
        if (r0 != 4) goto L15;
        return true;
    L15:
        if (((Player) Assertions.checkNotNull(this.player)).getPlayWhenReady() == true) goto L17;
        return true;
    L10:
        if (this.player.getCurrentTimeline().isEmpty() == true) goto L17;
        goto L11
    }

    private void showController(boolean r3) {
        if (useController() == true) goto L5;
        return;
    L5:
        PlayerControlView r0 = this.controller;
        if (r3 == false) goto L8;
        int r1 = 0;
    L9:
        r0.setShowTimeoutMs(r1);
        this.controller.show();
        return;
    L8:
        r1 = this.controllerShowTimeoutMs;
        goto L9
    }

    private void showImage() {
        if (this.imageView == null) goto L6;
        this.imageView.setVisibility(0);
        updateImageViewAspectRatio();
        return;
    }

    public static void switchTargetView(Player r1, PlayerView r2, PlayerView r3) {
        if (r2 != r3) goto L4;
        return;
    L4:
        if (r3 == null) goto L6;
        r3.setPlayer(r1);
    L6:
        if (r2 == null) goto L9;
        r2.setPlayer(null);
        return;
    }

    private void toggleControllerVisibility() {
        if (useController() == true) goto L5;
        return;
    L5:
        if (this.player != null) goto L8;
        return;
    L8:
        if (this.controller.isFullyVisible() == true) goto L11;
        maybeShowController(true);
        return;
    L11:
        if (this.controllerHideOnTouch == false) goto L16;
        this.controller.hide();
        return;
    }

    private void updateAspectRatio() {
        if (this.player == null) goto L5;
        VideoSize r0 = this.player.getVideoSize();
    L6:
        int r1 = r0.width;
        int r2 = r0.height;
        float r3 = 0.0f;
        if (r2 == 0) goto L11;
        if (r1 == 0) goto L11;
        float r4 = (r1 * r0.pixelWidthHeightRatio) / r2;
    L12:
        AspectRatioFrameLayout r5 = this.contentFrame;
        if (this.surfaceViewIgnoresVideoAspectRatio == true) goto L16;
        r3 = r4;
    L16:
        onContentAspectRatioChanged(r5, r3);
        return;
    L11:
        r4 = 0.0f;
        goto L12
    L5:
        r0 = VideoSize.UNKNOWN;
        goto L6
    }

    private void updateBuffering() {
        if (this.bufferingView == null) goto L22;
        int r1 = 0;
        if (this.player != null) goto L7;
    L15:
        boolean r3 = false;
    L16:
        View r0 = this.bufferingView;
        if (r3 == true) goto L20;
        r1 = 8;
    L20:
        r0.setVisibility(r1);
        return;
    L7:
        if (this.player.getPlaybackState() != 2) goto L15;
        r3 = true;
        if (this.showBuffering == 2) goto L16;
        if (this.showBuffering != 1) goto L15;
        if (this.player.getPlayWhenReady() == false) goto L15;
    }

    private void updateContentDescription() {
        String r1 = null;
        if (this.controller != null) goto L5;
    L15:
        setContentDescription(null);
        return;
    L5:
        if (this.useController == false) goto L15;
        if (this.controller.isFullyVisible() == true) goto L10;
        setContentDescription(getResources().getString(C0467R.string.exo_controls_show));
        return;
    L10:
        if (this.controllerHideOnTouch == false) goto L13;
        r1 = getResources().getString(C0467R.string.exo_controls_hide);
    L13:
        setContentDescription(r1);
    }

    private void updateControllerVisibility() {
        if (isPlayingAd() == true) goto L5;
    L7:
        maybeShowController(false);
        return;
    L5:
        if (this.controllerHideDuringAds == false) goto L7;
        hideController();
    }

    private void updateErrorMessage() {
        if (this.errorMessageView != null) goto L5;
        return;
    L5:
        if (this.customErrorMessage == null) goto L9;
        this.errorMessageView.setText(this.customErrorMessage);
        this.errorMessageView.setVisibility(0);
        return;
    L9:
        if (this.player == null) goto L11;
        PlaybackException r0 = this.player.getPlayerError();
    L12:
        if (r0 != null) goto L14;
    L16:
        this.errorMessageView.setVisibility(8);
        return;
    L14:
        if (this.errorMessageProvider == null) goto L16;
        CharSequence r2 = (CharSequence) this.errorMessageProvider.getErrorMessage(r0).second;
        this.errorMessageView.setText(r2);
        this.errorMessageView.setVisibility(0);
        return;
    L11:
        r0 = null;
        goto L12
    }

    private void updateForCurrentTrackSelections(boolean r9) {
        Player r0 = this.player;
        boolean r1 = true;
        if (r0 != null) goto L5;
    L9:
        boolean r3 = false;
    L11:
        if (this.keepContentOnPlayerReset == true) goto L15;
        if (r3 == false) goto L14;
        if (r9 == false) goto L15;
    L14:
        hideArtwork();
        closeShutter();
        hideAndClearImage();
    L15:
        if (r3 == true) goto L17;
        return;
    L17:
        boolean r4 = hasSelectedVideoTrack();
        boolean r5 = hasSelectedImageTrack();
        if (r4 == true) goto L22;
        if (r5 == true) goto L22;
        closeShutter();
        hideAndClearImage();
    L22:
        if (this.shutterView != null) goto L24;
    L28:
        boolean r6 = false;
    L29:
        if (r5 == false) goto L33;
        if (r4 == true) goto L33;
        if (r6 == false) goto L33;
        closeShutter();
        showImage();
    L37:
        if (r4 == true) goto L42;
        if (r5 == true) goto L42;
        if (useArtwork() == false) goto L42;
    L43:
        if (r1 == true) goto L45;
    L50:
        hideArtwork();
        return;
    L45:
        if (setArtworkFromMediaMetadata(r0) == false) goto L48;
        return;
    L48:
        if (setDrawableArtwork(this.defaultArtwork) == false) goto L50;
        return;
    L42:
        r1 = false;
    L33:
        if (r4 == false) goto L37;
        if (r5 == true) goto L37;
        if (r6 == false) goto L37;
        hideAndClearImage();
        goto L37
    L24:
        if (this.shutterView.getVisibility() != 4) goto L28;
        if (isImageSet() == false) goto L28;
        r6 = true;
        goto L29
    L5:
        if (r0.isCommandAvailable(30) == false) goto L9;
        if (r0.getCurrentTracks().isEmpty() == true) goto L9;
        r3 = true;
        goto L11
    }

    private void updateImageViewAspectRatio() {
        if (this.imageView != null) goto L5;
        return;
    L5:
        Drawable r0 = this.imageView.getDrawable();
        if (r0 != null) goto L8;
        return;
    L8:
        int r1 = r0.getIntrinsicWidth();
        int r2 = r0.getIntrinsicHeight();
        if (r1 <= 0) goto L20;
        if (r2 <= 0) goto L21;
        float r3 = r1 / r2;
        ImageView.ScaleType r4 = ImageView.ScaleType.FIT_XY;
        if (this.imageDisplayMode != 1) goto L16;
        r3 = getWidth() / getHeight();
        r4 = ImageView.ScaleType.CENTER_CROP;
    L16:
        if (this.imageView.getVisibility() != 0) goto L18;
        onContentAspectRatioChanged(this.contentFrame, r3);
    L18:
        this.imageView.setScaleType(r4);
        return;
    L21:
        return;
    }

    private boolean useArtwork() {
        if (this.artworkDisplayMode == 0) goto L6;
        Assertions.checkStateNotNull(this.artworkView);
        return true;
    L6:
        return false;
    }

    @EnsuresNonNullIf(expression = {"controller"}, result = true)
    private boolean useController() {
        if (this.useController == false) goto L6;
        Assertions.checkStateNotNull(this.controller);
        return true;
    L6:
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas r3) {
        super.dispatchDraw(r3);
        if (Util.SDK_INT == 34) goto L5;
        return;
    L5:
        if (this.surfaceSyncGroupV34 != null) goto L7;
        return;
    L7:
        if (this.enableComposeSurfaceSyncWorkaround == false) goto L12;
        this.surfaceSyncGroupV34.maybeMarkSyncReadyAndClear();
        return;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent r5) {
        if (this.player != null) goto L5;
    L10:
        boolean r0 = isDpadKey(r5.getKeyCode());
        if (r0 == false) goto L18;
        if (useController() == false) goto L18;
        if (this.controller.isFullyVisible() == true) goto L18;
        maybeShowController(true);
        return true;
    L18:
        if (dispatchMediaKeyEvent(r5) == false) goto L20;
    L26:
        maybeShowController(true);
        return true;
    L20:
        if (super.dispatchKeyEvent(r5) == true) goto L26;
        if (r0 == true) goto L24;
        return false;
    L24:
        if (useController() == false) goto L30;
        maybeShowController(true);
        return false;
    L30:
        return false;
    L5:
        if (this.player.isCommandAvailable(16) == false) goto L10;
        if (this.player.isPlayingAd() == false) goto L10;
        return super.dispatchKeyEvent(r5);
    }

    public boolean dispatchMediaKeyEvent(KeyEvent r2) {
        if (useController() == true) goto L5;
    L7:
        return false;
    L5:
        if (this.controller.dispatchMediaKeyEvent(r2) == false) goto L7;
        return true;
    }

    @Override // androidx.media3.common.AdViewProvider
    public List<AdOverlayInfo> getAdOverlayInfos() {
        ArrayList r0 = new ArrayList();
        if (this.overlayFrameLayout == null) goto L6;
        r0.add(new AdOverlayInfo.Builder(this.overlayFrameLayout, 4).setDetailedReason("Transparent overlay does not impact viewability").build());
    L6:
        if (this.controller == null) goto L9;
        r0.add(new AdOverlayInfo.Builder(this.controller, 1).build());
    L9:
        return ImmutableList.copyOf(r0);
    }

    @Override // androidx.media3.common.AdViewProvider
    public ViewGroup getAdViewGroup() {
        return (ViewGroup) Assertions.checkStateNotNull(this.adOverlayFrameLayout, "exo_ad_overlay must be present for ad playback");
    }

    public int getArtworkDisplayMode() {
        return this.artworkDisplayMode;
    }

    public boolean getControllerAutoShow() {
        return this.controllerAutoShow;
    }

    public boolean getControllerHideOnTouch() {
        return this.controllerHideOnTouch;
    }

    public int getControllerShowTimeoutMs() {
        return this.controllerShowTimeoutMs;
    }

    public Drawable getDefaultArtwork() {
        return this.defaultArtwork;
    }

    public int getImageDisplayMode() {
        return this.imageDisplayMode;
    }

    public FrameLayout getOverlayFrameLayout() {
        return this.overlayFrameLayout;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getResizeMode() {
        Assertions.checkStateNotNull(this.contentFrame);
        return this.contentFrame.getResizeMode();
    }

    public SubtitleView getSubtitleView() {
        return this.subtitleView;
    }

    @Deprecated
    public boolean getUseArtwork() {
        if (this.artworkDisplayMode == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean getUseController() {
        return this.useController;
    }

    public View getVideoSurfaceView() {
        return this.surfaceView;
    }

    public void hideController() {
        if (this.controller == null) goto L6;
        this.controller.hide();
        return;
    }

    public boolean isControllerFullyVisible() {
        if (this.controller != null) goto L5;
    L7:
        return false;
    L5:
        if (this.controller.isFullyVisible() == false) goto L7;
        return true;
    }

    /* JADX INFO: renamed from: lambda$new$0$androidx-media3-ui-PlayerView, reason: not valid java name */
    /* synthetic */ Object m494lambda$new$0$androidxmedia3uiPlayerView(Object r3, Method r4, Object[] r5) throws Throwable {
        if (r4.getName().equals("onImageAvailable") == false) goto L7;
        onImageAvailable((Bitmap) r5[1]);
        return null;
    L7:
        return null;
    }

    /* JADX INFO: renamed from: lambda$onImageAvailable$1$androidx-media3-ui-PlayerView, reason: not valid java name */
    /* synthetic */ void m495lambda$onImageAvailable$1$androidxmedia3uiPlayerView(Bitmap r3) {
        setImage(new BitmapDrawable(getResources(), r3));
        if (hasSelectedVideoTrack() == true) goto L6;
        showImage();
        closeShutter();
        return;
    }

    protected void onContentAspectRatioChanged(AspectRatioFrameLayout r1, float r2) {
        if (r1 == null) goto L5;
        r1.setAspectRatio(r2);
        return;
    }

    public void onPause() {
        if ((this.surfaceView instanceof GLSurfaceView) == false) goto L6;
        ((GLSurfaceView) this.surfaceView).onPause();
        return;
    }

    public void onResume() {
        if ((this.surfaceView instanceof GLSurfaceView) == false) goto L6;
        ((GLSurfaceView) this.surfaceView).onResume();
        return;
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent r2) {
        if (useController() == true) goto L5;
        return false;
    L5:
        if (this.player == null) goto L11;
        maybeShowController(true);
        return true;
    L11:
        return false;
    }

    @Override // android.view.View
    public boolean performClick() {
        toggleControllerVisibility();
        return super.performClick();
    }

    public void setArtworkDisplayMode(int r3) {
        if (r3 != 0) goto L5;
    L8:
        boolean r1 = true;
    L9:
        Assertions.checkState(r1);
        if (this.artworkDisplayMode == r3) goto L13;
        this.artworkDisplayMode = r3;
        updateForCurrentTrackSelections(false);
        return;
    L13:
        return;
    L5:
        if (this.artworkView != null) goto L8;
        r1 = false;
        goto L9
    }

    public void setAspectRatioListener(AspectRatioFrameLayout.AspectRatioListener r2) {
        Assertions.checkStateNotNull(this.contentFrame);
        this.contentFrame.setAspectRatioListener(r2);
    }

    public void setControllerAnimationEnabled(boolean r2) {
        Assertions.checkStateNotNull(this.controller);
        this.controller.setAnimationEnabled(r2);
    }

    public void setControllerAutoShow(boolean r1) {
        this.controllerAutoShow = r1;
    }

    public void setControllerHideDuringAds(boolean r1) {
        this.controllerHideDuringAds = r1;
    }

    public void setControllerHideOnTouch(boolean r2) {
        Assertions.checkStateNotNull(this.controller);
        this.controllerHideOnTouch = r2;
        updateContentDescription();
    }

    @Deprecated
    public void setControllerOnFullScreenModeChangedListener(PlayerControlView.OnFullScreenModeChangedListener r2) {
        Assertions.checkStateNotNull(this.controller);
        this.fullscreenButtonClickListener = null;
        this.controller.setOnFullScreenModeChangedListener(r2);
    }

    public void setControllerShowTimeoutMs(int r2) {
        Assertions.checkStateNotNull(this.controller);
        this.controllerShowTimeoutMs = r2;
        if (this.controller.isFullyVisible() == false) goto L6;
        showController();
        return;
    }

    @Deprecated
    public void setControllerVisibilityListener(PlayerControlView.VisibilityListener r3) {
        Assertions.checkStateNotNull(this.controller);
        if (this.legacyControllerVisibilityListener != r3) goto L6;
        return;
    L6:
        if (this.legacyControllerVisibilityListener == null) goto L8;
        this.controller.removeVisibilityListener(this.legacyControllerVisibilityListener);
    L8:
        this.legacyControllerVisibilityListener = r3;
        if (r3 == null) goto L12;
        this.controller.addVisibilityListener(r3);
        ControllerVisibilityListener r1 = (ControllerVisibilityListener) null;
        setControllerVisibilityListener(null);
        return;
    }

    public void setControllerVisibilityListener(ControllerVisibilityListener r3) {
        this.controllerVisibilityListener = r3;
        if (r3 == null) goto L6;
        PlayerControlView.VisibilityListener r1 = (PlayerControlView.VisibilityListener) null;
        setControllerVisibilityListener(null);
        return;
    }

    public void setCustomErrorMessage(CharSequence r2) {
        if (this.errorMessageView == null) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        this.customErrorMessage = r2;
        updateErrorMessage();
        return;
    L5:
        r0 = false;
        goto L6
    }

    public void setDefaultArtwork(Drawable r2) {
        if (this.defaultArtwork == r2) goto L6;
        this.defaultArtwork = r2;
        updateForCurrentTrackSelections(false);
        return;
    }

    public void setEnableComposeSurfaceSyncWorkaround(boolean r1) {
        this.enableComposeSurfaceSyncWorkaround = r1;
    }

    public void setErrorMessageProvider(ErrorMessageProvider<? super PlaybackException> r2) {
        if (this.errorMessageProvider == r2) goto L6;
        this.errorMessageProvider = r2;
        updateErrorMessage();
        return;
    }

    public void setExtraAdGroupMarkers(long[] r2, boolean[] r3) {
        Assertions.checkStateNotNull(this.controller);
        this.controller.setExtraAdGroupMarkers(r2, r3);
    }

    public void setFullscreenButtonClickListener(FullscreenButtonClickListener r3) {
        Assertions.checkStateNotNull(this.controller);
        this.fullscreenButtonClickListener = r3;
        this.controller.setOnFullScreenModeChangedListener(this.componentListener);
    }

    public void setFullscreenButtonState(boolean r2) {
        Assertions.checkStateNotNull(this.controller);
        this.controller.updateIsFullscreen(r2);
    }

    public void setImageDisplayMode(int r2) {
        if (this.imageView == null) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        if (this.imageDisplayMode == r2) goto L10;
        this.imageDisplayMode = r2;
        updateImageViewAspectRatio();
        return;
    L10:
        return;
    L5:
        r0 = false;
        goto L6
    }

    public void setKeepContentOnPlayerReset(boolean r2) {
        if (this.keepContentOnPlayerReset == r2) goto L6;
        this.keepContentOnPlayerReset = r2;
        updateForCurrentTrackSelections(false);
        return;
    }

    public void setPlayer(Player r7) {
        if (Looper.myLooper() != Looper.getMainLooper()) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        if (r7 != null) goto L9;
    L12:
        boolean r1 = true;
    L13:
        Assertions.checkArgument(r1);
        if (this.player != r7) goto L16;
        return;
    L16:
        Player r2 = this.player;
        if (r2 == null) goto L28;
        r2.removeListener(this.componentListener);
        if (r2.isCommandAvailable(27) == true) goto L21;
    L26:
        clearImageOutput(r2);
        goto L28
    L21:
        if ((this.surfaceView instanceof TextureView) == false) goto L24;
        r2.clearVideoTextureView((TextureView) this.surfaceView);
        goto L26
    L24:
        if ((this.surfaceView instanceof SurfaceView) == false) goto L26;
        r2.clearVideoSurfaceView((SurfaceView) this.surfaceView);
    L28:
        if (this.subtitleView == null) goto L30;
        this.subtitleView.setCues(null);
    L30:
        this.player = r7;
        if (useController() == false) goto L33;
        this.controller.setPlayer(r7);
    L33:
        updateBuffering();
        updateErrorMessage();
        updateForCurrentTrackSelections(true);
        if (r7 != null) goto L36;
        hideController();
        return;
    L36:
        if (r7.isCommandAvailable(27) == false) goto L49;
        if ((this.surfaceView instanceof TextureView) == false) goto L41;
        r7.setVideoTextureView((TextureView) this.surfaceView);
    L44:
        if (r7.isCommandAvailable(30) == true) goto L46;
    L47:
        updateAspectRatio();
        goto L49
    L46:
        if (r7.getCurrentTracks().isTypeSupported(2) == false) goto L49;
    L41:
        if ((this.surfaceView instanceof SurfaceView) == false) goto L44;
        r7.setVideoSurfaceView((SurfaceView) this.surfaceView);
    L49:
        if (this.subtitleView != null) goto L51;
    L53:
        r7.addListener(this.componentListener);
        setImageOutput(r7);
        maybeShowController(false);
        return;
    L51:
        if (r7.isCommandAvailable(28) == false) goto L53;
        this.subtitleView.setCues(r7.getCurrentCues().cues);
        goto L53
    L9:
        if (r7.getApplicationLooper() == Looper.getMainLooper()) goto L12;
        r1 = false;
        goto L13
    L5:
        r0 = false;
        goto L6
    }

    public void setRepeatToggleModes(int r2) {
        Assertions.checkStateNotNull(this.controller);
        this.controller.setRepeatToggleModes(r2);
    }

    public void setResizeMode(int r2) {
        Assertions.checkStateNotNull(this.contentFrame);
        this.contentFrame.setResizeMode(r2);
    }

    public void setShowBuffering(int r2) {
        if (this.showBuffering == r2) goto L6;
        this.showBuffering = r2;
        updateBuffering();
        return;
    }

    public void setShowFastForwardButton(boolean r2) {
        Assertions.checkStateNotNull(this.controller);
        this.controller.setShowFastForwardButton(r2);
    }

    @Deprecated
    public void setShowMultiWindowTimeBar(boolean r2) {
        Assertions.checkStateNotNull(this.controller);
        this.controller.setShowMultiWindowTimeBar(r2);
    }

    public void setShowNextButton(boolean r2) {
        Assertions.checkStateNotNull(this.controller);
        this.controller.setShowNextButton(r2);
    }

    public void setShowPlayButtonIfPlaybackIsSuppressed(boolean r2) {
        Assertions.checkStateNotNull(this.controller);
        this.controller.setShowPlayButtonIfPlaybackIsSuppressed(r2);
    }

    public void setShowPreviousButton(boolean r2) {
        Assertions.checkStateNotNull(this.controller);
        this.controller.setShowPreviousButton(r2);
    }

    public void setShowRewindButton(boolean r2) {
        Assertions.checkStateNotNull(this.controller);
        this.controller.setShowRewindButton(r2);
    }

    public void setShowShuffleButton(boolean r2) {
        Assertions.checkStateNotNull(this.controller);
        this.controller.setShowShuffleButton(r2);
    }

    public void setShowSubtitleButton(boolean r2) {
        Assertions.checkStateNotNull(this.controller);
        this.controller.setShowSubtitleButton(r2);
    }

    public void setShowVrButton(boolean r2) {
        Assertions.checkStateNotNull(this.controller);
        this.controller.setShowVrButton(r2);
    }

    public void setShutterBackgroundColor(int r2) {
        if (this.shutterView == null) goto L6;
        this.shutterView.setBackgroundColor(r2);
        return;
    }

    @Deprecated
    public void setUseArtwork(boolean r2) {
        setArtworkDisplayMode(!r2 ? 1 : 0);
    }

    public void setUseController(boolean r4) {
        boolean r0 = false;
        if (r4 == true) goto L5;
    L8:
        boolean r2 = true;
    L9:
        Assertions.checkState(r2);
        if (r4 == false) goto L12;
    L13:
        r0 = true;
    L14:
        setClickable(r0);
        if (this.useController != r4) goto L17;
        return;
    L17:
        this.useController = r4;
        if (useController() == false) goto L21;
        this.controller.setPlayer(this.player);
    L23:
        updateContentDescription();
        return;
    L21:
        if (this.controller == null) goto L23;
        this.controller.hide();
        this.controller.setPlayer(null);
        goto L23
    L12:
        if (hasOnClickListeners() == false) goto L14;
    L5:
        if (this.controller != null) goto L8;
        r2 = false;
        goto L9
    }

    @Override // android.view.View
    public void setVisibility(int r2) {
        super.setVisibility(r2);
        if ((this.surfaceView instanceof SurfaceView) == false) goto L6;
        this.surfaceView.setVisibility(r2);
        return;
    }

    public void showController() {
        showController(shouldShowControllerIndefinitely());
    }
}
