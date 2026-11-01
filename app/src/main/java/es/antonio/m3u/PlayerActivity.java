package es.antonio.m3u;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PictureInPictureParams;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C0180C;
import androidx.media3.common.DeviceInfo;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.Tracks;
import androidx.media3.common.VideoSize;
import androidx.media3.common.text.CueGroup;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.datasource.ResolvingDataSource;
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.DecoderReuseEvaluation;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.exoplayer.analytics.AnalyticsListener;
import androidx.media3.exoplayer.audio.AudioSink;
import androidx.media3.exoplayer.drm.DefaultDrmSessionManager;
import androidx.media3.exoplayer.drm.DrmSessionManager;
import androidx.media3.exoplayer.drm.FrameworkMediaDrm;
import androidx.media3.exoplayer.drm.LocalMediaDrmCallback;
import androidx.media3.exoplayer.mediacodec.MediaCodecDecoderException;
import androidx.media3.exoplayer.mediacodec.MediaCodecInfo;
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil;
import androidx.media3.exoplayer.smoothstreaming.SsMediaSource;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MediaLoadData;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.p002ui.C0467R;
import androidx.media3.p002ui.PlayerView;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.net.HttpHeaders;
import es.antonio.chdtv.playback.HlsPlaybackModule;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
public class PlayerActivity extends Activity {
    private String audioLabel;
    private Dialog browseDialog;
    private List<Entry> catalog;
    private LinearLayout controls;
    private boolean daznPreflight;
    private boolean daznScoped;
    private Entry entry;
    private EpgData epg;
    private Button epgButton;
    private AlertDialog epgDialog;
    private boolean epgLoaded;
    private Runnable epgRender;
    private boolean epgRequested;
    private final Runnable epgTick;
    private final Set failedDecoders;
    private ChannelFallback fallback;
    private Catalog fallbackCatalog;
    private String fallbackList;
    private boolean fallbackSwitching;
    private long firstFrameMs;
    private Button fit;
    private Runnable healthyTask;
    private final Runnable historyTick;
    private Runnable httpRetryTask;
    private PlaybackException lastPlaybackFailure;
    private boolean loaded;
    private final Handler main;
    private Button next;
    private PlaybackException pendingFallbackFailure;
    private boolean playbackCatalogRequested;
    private boolean playbackError;
    private float playbackSpeed;
    private ExoPlayer player;
    private boolean playerMuted;
    private float playerVolume;
    private long position;

    /* JADX INFO: Incorrect field signature: Ljava/util/Set<Ljava/lang/String;>; */
    private boolean preferSoftwareAacForVod;
    private Button previous;
    private Map<Entry, String> providers;
    private String qualityLabel;
    private long rateDeadline;
    private boolean rateNoAuto;
    private boolean rateRetryUsed;
    private final LiveReconnectPolicy reconnect;
    private Runnable reconnectTask;
    private boolean reconnectWaiting;
    private FrameLayout root;
    private boolean screenDrawn;
    private PlaybackSequence sequence;
    private int sequenceTicket;
    private View settingsButton;
    private AlertDialog settingsDialog;
    private Runnable settingsRender;
    private boolean started;
    private final PlaybackStartup startup;
    private long startupAt;
    private TextView status;
    private String subtitleLabel;
    private boolean tokenPreflight;
    private boolean tokenRetryUsed;
    private boolean tokenScoped;
    private int tokenTicket;
    private boolean tokenWaiting;
    private final ExecutorService tokenWorker;
    private TrackSelectionParameters trackParameters;
    private AlertDialog tracksDialog;
    private Runnable tracksRender;
    private PlayerView view;
    private final ExecutorService worker;
    private boolean zoom;

    /* JADX INFO: renamed from: es.antonio.m3u.PlayerActivity$2 */
    class ViewTreeObserverOnPreDrawListenerC10152 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ PlayerActivity this$0;

        public static /* synthetic */ void $r8$lambda$89l0XcSdUWqWf9lQ9xKVBrjCGww(ViewTreeObserverOnPreDrawListenerC10152 r0) {
            r0.lambda$onPreDraw$0();
        }

        ViewTreeObserverOnPreDrawListenerC10152(PlayerActivity r1) {
            this.this$0 = r1;
        }

        private /* synthetic */ void lambda$onPreDraw$0() {
            if (PlayerActivity.m676$$Nest$fgetstarted(this.this$0) == true) goto L5;
            return;
        L5:
            if (this.this$0.isFinishing() == false) goto L7;
            return;
        L7:
            if (this.this$0.isDestroyed() == true) goto L12;
            PlayerActivity.m705$$Nest$mstartPlayer(this.this$0);
            return;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            PlayerActivity.m674$$Nest$fgetroot(this.this$0).getViewTreeObserver().removeOnPreDrawListener(this);
            PlayerActivity.m688$$Nest$fputscreenDrawn(this.this$0, true);
            PlayerActivity.m671$$Nest$fgetmain(this.this$0).post(new PlayerActivity$2$$ExternalSyntheticLambda0(this));
            return true;
        }
    }

    /* JADX INFO: renamed from: es.antonio.m3u.PlayerActivity$4 */
    class C10174 implements Player.Listener {
        final /* synthetic */ PlayerActivity this$0;

        /* JADX INFO: renamed from: $r8$lambda$xAlBRIebMM4iiz-lAqZ-E3CcP18, reason: not valid java name */
        public static /* synthetic */ void m708$r8$lambda$xAlBRIebMM4iizlAqZE3CcP18(C10174 r0) {
            r0.lambda$onPlaybackStateChanged$0();
        }

        C10174(PlayerActivity r1) {
            this.this$0 = r1;
        }

        private /* synthetic */ void lambda$onPlaybackStateChanged$0() {
            PlayerActivity.m693$$Nest$mloadEpgOnce(this.this$0);
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
        public void onIsPlayingChanged(boolean r3) {
            this.this$0.updateFloatingParams();
            if (r3 == true) goto L5;
        L9:
            PlayerActivity.m691$$Nest$mcancelHealthyReset(this.this$0);
            return;
        L5:
            if (PlayerActivity.m672$$Nest$fgetplayer(this.this$0) == null) goto L9;
            if (PlayerActivity.m672$$Nest$fgetplayer(this.this$0).getPlaybackState() != 3) goto L9;
            PlayerActivity.m702$$Nest$mscheduleHealthyReset(this.this$0);
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
            this.this$0.updateFloatingParams();
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onPlaybackParametersChanged(PlaybackParameters r1) {
            Player.Listener.CC.$default$onPlaybackParametersChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlaybackStateChanged(int r6) {
            StringBuilder r0 = new StringBuilder().append("state=");
            if (r6 != 3) goto L6;
            String r3 = "READY";
        L11:
            BlackBox.event("PLAYER_STATE", r0.append(r3).toString());
            PlayerActivity.m677$$Nest$fgetstartup(this.this$0).state(r6);
            this.this$0.updateFloatingParams();
            PlayerActivity r1 = this.this$0;
            if (r6 != 3) goto L14;
            PlayerActivity.m702$$Nest$mscheduleHealthyReset(r1);
        L15:
            if (r6 == 3) goto L17;
        L24:
            if (r6 != 4) goto L27;
            WatchHistory.remove(this.this$0, PlayerActivity.m666$$Nest$fgetentry(this.this$0));
            return;
        L27:
            return;
        L17:
            if (PlayerActivity.m668$$Nest$fgetepgRequested(this.this$0) == true) goto L19;
            PlayerActivity.m671$$Nest$fgetmain(this.this$0).postDelayed(new PlayerActivity$4$$ExternalSyntheticLambda0(this), 750);
        L19:
            PlayerActivity.m686$$Nest$fputplaybackError(this.this$0, false);
            TextView r2 = PlayerActivity.m679$$Nest$fgetstatus(this.this$0);
            StringBuilder r4 = new StringBuilder().append(PlayerActivity.m666$$Nest$fgetentry(this.this$0).title);
            if (PlayerActivity.m669$$Nest$fgetfallbackList(this.this$0).isEmpty() == false) goto L22;
            String r5 = "";
        L23:
            r2.setText(r4.append(r5).toString());
            PlayerActivity.m706$$Nest$mupdateProgramme(this.this$0);
            PlayerActivity.m707$$Nest$mupdateTracks(this.this$0);
            PlayerActivity.m682$$Nest$fgetview(this.this$0).setControllerShowTimeoutMs(5000);
            goto L24
        L22:
            r5 = " · Fuente: " + PlayerActivity.m669$$Nest$fgetfallbackList(this.this$0);
            goto L23
        L14:
            PlayerActivity.m691$$Nest$mcancelHealthyReset(r1);
            goto L15
        L6:
            if (r6 != 2) goto L8;
            r3 = "BUFFERING";
            goto L11
        L8:
            if (r6 != 4) goto L10;
            r3 = "ENDED";
            goto L11
        L10:
            r3 = "IDLE";
            goto L11
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onPlaybackSuppressionReasonChanged(int r1) {
            Player.Listener.CC.$default$onPlaybackSuppressionReasonChanged(this, r1);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlayerError(PlaybackException r5) {
            BlackBox.failure("PLAYER_ERROR_" + r5.getErrorCodeName(), r5);
            PlayerActivity.m691$$Nest$mcancelHealthyReset(this.this$0);
            if (PlayerActivity.m699$$Nest$mretryRateLimit(this.this$0, r5) == false) goto L5;
            return;
        L5:
            if (PlayerActivity.m696$$Nest$mretryDazn(this.this$0, r5) == false) goto L7;
            return;
        L7:
            if (PlayerActivity.m700$$Nest$mretryToken(this.this$0, r5) == false) goto L9;
            return;
        L9:
            if (PlayerActivity.m698$$Nest$mretryLive(this.this$0, r5) == false) goto L11;
            return;
        L11:
            if (PlayerActivity.m695$$Nest$mretryAlternative(this.this$0, r5) == false) goto L13;
            return;
        L13:
            if (PlayerActivity.m697$$Nest$mretryDecoder(this.this$0, r5) == true) goto L52;
            r5.getErrorCodeName();
            PlayerActivity.m685$$Nest$fputlastPlaybackFailure(this.this$0, r5);
            String r0 = PlaybackFailure.summary(r5);
            if (r0.startsWith("ERROR_CODE_DRM") == false) goto L18;
            r0 = "DRM: claves, licencia o cifrado no válidos/compatibles · Detalles en ⚙";
        L18:
            Throwable r1 = r5;
        L19:
            if (r1 == null) goto L36;
            if ((r1 instanceof IOException) == false) goto L35;
            if (r1.getMessage() == null) goto L35;
            if (r1.getMessage().startsWith("HLS con cifrado") == true) goto L34;
            if (r1.getMessage().startsWith("ClearKey HLS") == true) goto L34;
            if (r1.getMessage().startsWith("Señalización de cifrado HLS") == true) goto L34;
            if (r1.getMessage().startsWith("La playlist HLS") == true) goto L34;
            if (r1.getMessage().startsWith("Sample-AES:") == false) goto L35;
        L34:
            r0 = r1.getMessage();
        L35:
            r1 = r1.getCause();
        L36:
            PlayerActivity.m704$$Nest$mshowError(this.this$0, r0);
            return;
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
        public void onTrackSelectionParametersChanged(TrackSelectionParameters r2) {
            PlayerActivity.m690$$Nest$fputtrackParameters(this.this$0, r2);
            PlayerActivity.m707$$Nest$mupdateTracks(this.this$0);
            if (PlayerActivity.m681$$Nest$fgettracksRender(this.this$0) == null) goto L6;
            PlayerActivity.m681$$Nest$fgettracksRender(this.this$0).run();
            return;
        }

        @Override // androidx.media3.common.Player.Listener
        public void onTracksChanged(Tracks r2) {
            PlayerActivity.m707$$Nest$mupdateTracks(this.this$0);
            if (PlayerActivity.m681$$Nest$fgettracksRender(this.this$0) == null) goto L6;
            PlayerActivity.m681$$Nest$fgettracksRender(this.this$0).run();
            return;
        }

        @Override // androidx.media3.common.Player.Listener
        public void onVideoSizeChanged(VideoSize r2) {
            PlayerActivity.m707$$Nest$mupdateTracks(this.this$0);
            this.this$0.updateFloatingParams();
        }

        @Override // androidx.media3.common.Player.Listener
        public /* synthetic */ void onVolumeChanged(float r1) {
            Player.Listener.CC.$default$onVolumeChanged(this, r1);
        }
    }

    /* JADX INFO: renamed from: es.antonio.m3u.PlayerActivity$5 */
    class C10185 implements AnalyticsListener {
        final /* synthetic */ PlayerActivity this$0;

        public static /* synthetic */ void $r8$lambda$6CQrn6B_MgyEsfioRc56rx100d0(C10185 r0) {
            r0.lambda$onRenderedFirstFrame$0();
        }

        C10185(PlayerActivity r1) {
            this.this$0 = r1;
        }

        private /* synthetic */ void lambda$onRenderedFirstFrame$0() {
            PlayerActivity.m694$$Nest$mloadPlaybackCatalog(this.this$0);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onAudioAttributesChanged(AnalyticsListener.EventTime r1, AudioAttributes r2) {
            AnalyticsListener.CC.$default$onAudioAttributesChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public void onAudioCodecError(AnalyticsListener.EventTime r3, Exception r4) {
            if (this.this$0.isVodEntry() == false) goto L6;
            BlackBox.failure("VOD_AUDIO_CODEC_ERROR", r4);
            return;
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onAudioDecoderInitialized(AnalyticsListener.EventTime r1, String r2, long r3) {
            AnalyticsListener.CC.$default$onAudioDecoderInitialized(this, r1, r2, r3);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public void onAudioDecoderInitialized(AnalyticsListener.EventTime r4, String r5, long r6, long r8) {
            if (this.this$0.isVodEntry() == false) goto L6;
            BlackBox.event("VOD_AUDIO_DECODER", "name=" + r5 + " init_ms=" + r8);
            return;
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onAudioDecoderReleased(AnalyticsListener.EventTime r1, String r2) {
            AnalyticsListener.CC.$default$onAudioDecoderReleased(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onAudioDisabled(AnalyticsListener.EventTime r1, DecoderCounters r2) {
            AnalyticsListener.CC.$default$onAudioDisabled(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onAudioEnabled(AnalyticsListener.EventTime r1, DecoderCounters r2) {
            AnalyticsListener.CC.$default$onAudioEnabled(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public void onAudioInputFormatChanged(AnalyticsListener.EventTime r6, Format r7, DecoderReuseEvaluation r8) {
            PlayerActivity r0 = this.this$0;
            PlayerActivity.m707$$Nest$mupdateTracks(r0);
            if (r0.isVodEntry() == false) goto L6;
            BlackBox.event("VOD_AUDIO_INPUT", "mime=" + r7.sampleMimeType + " sample_rate=" + r7.sampleRate + " channels=" + r7.channelCount + " bitrate=" + r7.bitrate);
            return;
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onAudioPositionAdvancing(AnalyticsListener.EventTime r1, long r2) {
            AnalyticsListener.CC.$default$onAudioPositionAdvancing(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onAudioSessionIdChanged(AnalyticsListener.EventTime r1, int r2) {
            AnalyticsListener.CC.$default$onAudioSessionIdChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public void onAudioSinkError(AnalyticsListener.EventTime r3, Exception r4) {
            if (this.this$0.isVodEntry() == false) goto L6;
            BlackBox.failure("VOD_AUDIO_SINK_ERROR", r4);
            return;
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onAudioTrackInitialized(AnalyticsListener.EventTime r1, AudioSink.AudioTrackConfig r2) {
            AnalyticsListener.CC.$default$onAudioTrackInitialized(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onAudioTrackReleased(AnalyticsListener.EventTime r1, AudioSink.AudioTrackConfig r2) {
            AnalyticsListener.CC.$default$onAudioTrackReleased(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public void onAudioUnderrun(AnalyticsListener.EventTime r5, int r6, long r7, long r9) {
            if (this.this$0.isVodEntry() == false) goto L6;
            BlackBox.event("VOD_AUDIO_UNDERRUN", "buffer_bytes=" + r6 + " buffer_ms=" + r7 + " since_feed_ms=" + r9);
            return;
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
        public /* synthetic */ void onDownstreamFormatChanged(AnalyticsListener.EventTime r1, MediaLoadData r2) {
            AnalyticsListener.CC.$default$onDownstreamFormatChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onDrmKeysLoaded(AnalyticsListener.EventTime r1) {
            AnalyticsListener.CC.$default$onDrmKeysLoaded(this, r1);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onDrmKeysRemoved(AnalyticsListener.EventTime r1) {
            AnalyticsListener.CC.$default$onDrmKeysRemoved(this, r1);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onDrmKeysRestored(AnalyticsListener.EventTime r1) {
            AnalyticsListener.CC.$default$onDrmKeysRestored(this, r1);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onDrmSessionAcquired(AnalyticsListener.EventTime r1) {
            AnalyticsListener.CC.$default$onDrmSessionAcquired(this, r1);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onDrmSessionAcquired(AnalyticsListener.EventTime r1, int r2) {
            AnalyticsListener.CC.$default$onDrmSessionAcquired(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onDrmSessionManagerError(AnalyticsListener.EventTime r1, Exception r2) {
            AnalyticsListener.CC.$default$onDrmSessionManagerError(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onDrmSessionReleased(AnalyticsListener.EventTime r1) {
            AnalyticsListener.CC.$default$onDrmSessionReleased(this, r1);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public void onDroppedVideoFrames(AnalyticsListener.EventTime r3, int r4, long r5) {
            Log.w("CHDTVPlayback", "dropped_frames=" + r4 + " elapsed_ms=" + r5);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onEvents(Player r1, AnalyticsListener.Events r2) {
            AnalyticsListener.CC.$default$onEvents(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onIsLoadingChanged(AnalyticsListener.EventTime r1, boolean r2) {
            AnalyticsListener.CC.$default$onIsLoadingChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onIsPlayingChanged(AnalyticsListener.EventTime r1, boolean r2) {
            AnalyticsListener.CC.$default$onIsPlayingChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onLoadCanceled(AnalyticsListener.EventTime r1, LoadEventInfo r2, MediaLoadData r3) {
            AnalyticsListener.CC.$default$onLoadCanceled(this, r1, r2, r3);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public void onLoadCompleted(AnalyticsListener.EventTime r10, LoadEventInfo r11, MediaLoadData r12) {
            PlayerActivity.m677$$Nest$fgetstartup(this.this$0).completed(r12.dataType, r11.bytesLoaded, r11.loadDurationMs, SystemClock.elapsedRealtime());
            if (r12.dataType != 4) goto L6;
            BlackBox.event("MANIFEST_LOADED", "bytes=" + r11.bytesLoaded + " duration_ms=" + r11.loadDurationMs);
            return;
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public void onLoadError(AnalyticsListener.EventTime r3, LoadEventInfo r4, MediaLoadData r5, IOException r6, boolean r7) {
            if (r7 == true) goto L7;
            PlayerActivity.m677$$Nest$fgetstartup(this.this$0).failed(r6);
            BlackBox.failure("MEDIA_LOAD_ERROR", r6);
            if ((r6 instanceof HttpDataSource.InvalidResponseCodeException) == false) goto L8;
            BlackBox.event("HTTP_ERROR", "status=" + ((HttpDataSource.InvalidResponseCodeException) r6).responseCode);
            return;
        L8:
            return;
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public void onLoadStarted(AnalyticsListener.EventTime r3, LoadEventInfo r4, MediaLoadData r5) {
            PlayerActivity.m677$$Nest$fgetstartup(this.this$0).loading(r5.dataType);
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
        public /* synthetic */ void onMediaItemTransition(AnalyticsListener.EventTime r1, MediaItem r2, int r3) {
            AnalyticsListener.CC.$default$onMediaItemTransition(this, r1, r2, r3);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onMediaMetadataChanged(AnalyticsListener.EventTime r1, MediaMetadata r2) {
            AnalyticsListener.CC.$default$onMediaMetadataChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onMetadata(AnalyticsListener.EventTime r1, Metadata r2) {
            AnalyticsListener.CC.$default$onMetadata(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onPlayWhenReadyChanged(AnalyticsListener.EventTime r1, boolean r2, int r3) {
            AnalyticsListener.CC.$default$onPlayWhenReadyChanged(this, r1, r2, r3);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onPlaybackParametersChanged(AnalyticsListener.EventTime r1, PlaybackParameters r2) {
            AnalyticsListener.CC.$default$onPlaybackParametersChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onPlaybackStateChanged(AnalyticsListener.EventTime r1, int r2) {
            AnalyticsListener.CC.$default$onPlaybackStateChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onPlaybackSuppressionReasonChanged(AnalyticsListener.EventTime r1, int r2) {
            AnalyticsListener.CC.$default$onPlaybackSuppressionReasonChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onPlayerError(AnalyticsListener.EventTime r1, PlaybackException r2) {
            AnalyticsListener.CC.$default$onPlayerError(this, r1, r2);
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
        public /* synthetic */ void onPositionDiscontinuity(AnalyticsListener.EventTime r1, Player.PositionInfo r2, Player.PositionInfo r3, int r4) {
            AnalyticsListener.CC.$default$onPositionDiscontinuity(this, r1, r2, r3, r4);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public void onRenderedFirstFrame(AnalyticsListener.EventTime r6, Object r7, long r8) {
            PlayerActivity.m677$$Nest$fgetstartup(this.this$0).frame(SystemClock.elapsedRealtime());
            BlackBox.event("FIRST_FRAME", PlayerActivity.m677$$Nest$fgetstartup(this.this$0).report(SystemClock.elapsedRealtime()));
            if (PlayerActivity.m670$$Nest$fgetfirstFrameMs(this.this$0) >= 0) goto L7;
            PlayerActivity.m683$$Nest$fputfirstFrameMs(this.this$0, SystemClock.elapsedRealtime() - PlayerActivity.m678$$Nest$fgetstartupAt(this.this$0));
            Log.i("CHDTV", "Primera imagen: " + PlayerActivity.m670$$Nest$fgetfirstFrameMs(this.this$0) + " ms · " + StreamFormat.mime(PlayerActivity.m666$$Nest$fgetentry(this.this$0)));
            if (PlayerActivity.m675$$Nest$fgetsettingsRender(this.this$0) == null) goto L7;
            PlayerActivity.m675$$Nest$fgetsettingsRender(this.this$0).run();
        L7:
            PlayerActivity.m671$$Nest$fgetmain(this.this$0).postDelayed(new PlayerActivity$5$$ExternalSyntheticLambda0(this), 300);
            PlayerActivity.m693$$Nest$mloadEpgOnce(this.this$0);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onRendererReadyChanged(AnalyticsListener.EventTime r1, int r2, int r3, boolean r4) {
            AnalyticsListener.CC.$default$onRendererReadyChanged(this, r1, r2, r3, r4);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onRepeatModeChanged(AnalyticsListener.EventTime r1, int r2) {
            AnalyticsListener.CC.$default$onRepeatModeChanged(this, r1, r2);
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
        public /* synthetic */ void onShuffleModeChanged(AnalyticsListener.EventTime r1, boolean r2) {
            AnalyticsListener.CC.$default$onShuffleModeChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onSkipSilenceEnabledChanged(AnalyticsListener.EventTime r1, boolean r2) {
            AnalyticsListener.CC.$default$onSkipSilenceEnabledChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onSurfaceSizeChanged(AnalyticsListener.EventTime r1, int r2, int r3) {
            AnalyticsListener.CC.$default$onSurfaceSizeChanged(this, r1, r2, r3);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onTimelineChanged(AnalyticsListener.EventTime r1, int r2) {
            AnalyticsListener.CC.$default$onTimelineChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onTrackSelectionParametersChanged(AnalyticsListener.EventTime r1, TrackSelectionParameters r2) {
            AnalyticsListener.CC.$default$onTrackSelectionParametersChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onTracksChanged(AnalyticsListener.EventTime r1, Tracks r2) {
            AnalyticsListener.CC.$default$onTracksChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onUpstreamDiscarded(AnalyticsListener.EventTime r1, MediaLoadData r2) {
            AnalyticsListener.CC.$default$onUpstreamDiscarded(this, r1, r2);
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
        public void onVideoDecoderInitialized(AnalyticsListener.EventTime r4, String r5, long r6, long r8) {
            PlayerActivity.m677$$Nest$fgetstartup(this.this$0).decoder(r5);
            BlackBox.event("VIDEO_DECODER", "name=" + r5 + " init_ms=" + r8);
            Log.i("CHDTVPlayback", "video_decoder=" + r5 + " init_ms=" + r8);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onVideoDecoderReleased(AnalyticsListener.EventTime r1, String r2) {
            AnalyticsListener.CC.$default$onVideoDecoderReleased(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onVideoDisabled(AnalyticsListener.EventTime r1, DecoderCounters r2) {
            AnalyticsListener.CC.$default$onVideoDisabled(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onVideoEnabled(AnalyticsListener.EventTime r1, DecoderCounters r2) {
            AnalyticsListener.CC.$default$onVideoEnabled(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onVideoFrameProcessingOffset(AnalyticsListener.EventTime r1, long r2, int r4) {
            AnalyticsListener.CC.$default$onVideoFrameProcessingOffset(this, r1, r2, r4);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public void onVideoInputFormatChanged(AnalyticsListener.EventTime r2, Format r3, DecoderReuseEvaluation r4) {
            PlayerActivity.m707$$Nest$mupdateTracks(this.this$0);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onVideoSizeChanged(AnalyticsListener.EventTime r1, int r2, int r3, int r4, float r5) {
            AnalyticsListener.CC.$default$onVideoSizeChanged(this, r1, r2, r3, r4, r5);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onVideoSizeChanged(AnalyticsListener.EventTime r1, VideoSize r2) {
            AnalyticsListener.CC.$default$onVideoSizeChanged(this, r1, r2);
        }

        @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
        public /* synthetic */ void onVolumeChanged(AnalyticsListener.EventTime r1, float r2) {
            AnalyticsListener.CC.$default$onVolumeChanged(this, r1, r2);
        }
    }

    public static /* synthetic */ void $r8$lambda$0Ckt3HxfJGzg7sbgzimcPvlJjt4(PlayerActivity r0, ExoPlayer r1, List r2, int r3, List r4, ArrayAdapter r5, ListView r6, TextView r7) {
        r0.lambda$showTracks$48(r1, r2, r3, r4, r5, r6, r7);
    }

    /* JADX INFO: renamed from: $r8$lambda$0HQ1DguYKg_i-j37iacD_S7gfWc, reason: not valid java name */
    public static /* synthetic */ void m643$r8$lambda$0HQ1DguYKg_ij37iacD_S7gfWc(PlayerActivity r0, AlertDialog r1, DialogInterface r2) {
        r0.lambda$showTracks$50(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$0zqfL-SStukS4ww4sykdQWMn87o, reason: not valid java name */
    public static /* synthetic */ void m644$r8$lambda$0zqfLSStukS4ww4sykdQWMn87o(PlayerActivity r0, List r1, Entry r2, AdapterView r3, View r4, int r5, long r6) {
        r0.lambda$showEpg$68(r1, r2, r3, r4, r5, r6);
    }

    /* JADX INFO: renamed from: $r8$lambda$3SQNK22b2LJQq-iXN8a3CQL2L6I, reason: not valid java name */
    public static /* synthetic */ void m645$r8$lambda$3SQNK22b2LJQqiXN8a3CQL2L6I(PlayerActivity r0, Entry r1, ChannelFallback.Choice r2) {
        r0.lambda$retryAlternative$40(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$3i7ylVQBPkq5ERQ7CT5emTViCuc(PlayerActivity r0, Entry r1, int r2) {
        r0.lambda$startPlayer$23(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$3tcNRcYREhqrDy4nWFN9VmD3SeQ(PlayerActivity r0, int r1, Entry r2) {
        r0.lambda$retryLive$35(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$4LU4xNLht2dk3cEWWX0nXE2Tt8E(PlayerActivity r0, int r1, boolean r2, Entry r3, Boolean r4, Throwable r5) {
        r0.lambda$startPlayer$22(r1, r2, r3, r4, r5);
    }

    public static /* synthetic */ void $r8$lambda$4a2HVMxm7C4RlLOk4EM01SOLF6M(PlayerActivity r0, EpgGridView r1, Dialog r2, Entry r3) {
        r0.lambda$browseGuide$61(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$5YpMB4Bf1aEKb_cAZyfraAjIEGI(PlayerActivity r0, int r1, Boolean r2, PlaybackException r3) {
        r0.lambda$retryToken$38(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$A56s1WHNAhWA4_nT_P9js69ryV0(PlayerActivity r0, List r1, AlertDialog r2, AdapterView r3, View r4, int r5, long r6) {
        r0.lambda$browse$54(r1, r2, r3, r4, r5, r6);
    }

    /* JADX INFO: renamed from: $r8$lambda$BzuC2pHqddRfD8My-MvlfMiIqM0, reason: not valid java name */
    public static /* synthetic */ void m646$r8$lambda$BzuC2pHqddRfD8MyMvlfMiIqM0(PlayerActivity r0, View r1) {
        r0.lambda$onCreate$6(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$ER-KD4E3WHuI4Q4sFAifFE2JONo, reason: not valid java name */
    public static /* synthetic */ void m647$r8$lambda$ERKD4E3WHuI4Q4sFAifFE2JONo(PlayerActivity r0, AlertDialog r1, DialogInterface r2) {
        r0.lambda$showSpeed$47(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$FRC0EhzeSRPlqMk15K43u6kLWss(PlayerActivity r0, int r1, PlaybackException r2, Boolean r3, Throwable r4) {
        r0.lambda$retryDazn$37(r1, r2, r3, r4);
    }

    /* JADX INFO: renamed from: $r8$lambda$FaEUGG9yPXf_cC-yBthQqre4eAs, reason: not valid java name */
    public static /* synthetic */ List m648$r8$lambda$FaEUGG9yPXf_cCyBthQqre4eAs(PlayerActivity r0, String r1, boolean r2, boolean r3) {
        return r0.lambda$startPlayer$33(r1, r2, r3);
    }

    /* JADX INFO: renamed from: $r8$lambda$FvBz3UzmWlZUIaOg2wu-qRiIcms, reason: not valid java name */
    public static /* synthetic */ void m649$r8$lambda$FvBz3UzmWlZUIaOg2wuqRiIcms(PlayerActivity r0, List r1, ArrayAdapter r2) {
        r0.lambda$showPlaybackSettings$41(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$HQy3ps4lGaUlAMIQI6zHykiHvgs(PlayerActivity r0, Entry r1, EpgData.Programme r2, DialogInterface r3, int r4) {
        r0.lambda$showEpg$67(r1, r2, r3, r4);
    }

    /* JADX INFO: renamed from: $r8$lambda$IQHG-VVjt85qqBrkPXDMfvpW8Zg, reason: not valid java name */
    public static /* synthetic */ void m650$r8$lambda$IQHGVVjt85qqBrkPXDMfvpW8Zg(PlayerActivity r0, Entry r1, int r2) {
        r0.lambda$startPlayer$26(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$JefbjCGx_CmH95eLsEyVGBrAUuc(PlayerActivity r0, TextView r1, Entry r2, EpgData.Programme r3) {
        r0.lambda$browseGuide$62(r1, r2, r3);
    }

    /* JADX INFO: renamed from: $r8$lambda$KB4-3K8JJXM666YUieK-6Y0C-s4, reason: not valid java name */
    public static /* synthetic */ void m651$r8$lambda$KB43K8JJXM666YUieK6Y0Cs4(PlayerActivity r0, List r1, DialogInterface r2, int r3) {
        r0.lambda$showPlaybackSettings$44(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$Kf_cdFCJnifFCDJjNwIZC_d2i9Y(PlayerActivity r0, int r1, JSONObject r2, Boolean r3, Throwable r4) {
        r0.lambda$startPlayer$25(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$KpQX0RutbDsePFQCjPtRy5LKLVY(PlayerActivity r0, String r1, DialogInterface r2, int r3) {
        r0.lambda$showFailureDetails$51(r1, r2, r3);
    }

    /* JADX INFO: renamed from: $r8$lambda$KryHfIamyMzBwPfwmN9SuT4g-u4, reason: not valid java name */
    public static /* synthetic */ void m652$r8$lambda$KryHfIamyMzBwPfwmN9SuT4gu4(PlayerActivity r0, ExoPlayer r1, float[] r2, DialogInterface r3, int r4) {
        r0.lambda$showSpeed$46(r1, r2, r3, r4);
    }

    /* JADX INFO: renamed from: $r8$lambda$L_X0m-XflamSaNblcYsU0GNA81U, reason: not valid java name */
    public static /* synthetic */ void m653$r8$lambda$L_X0mXflamSaNblcYsU0GNA81U(PlayerActivity r0) {
        r0.lambda$loadEpgOnce$16();
    }

    public static /* synthetic */ void $r8$lambda$NpQl47_QgKpAE7wVsvWd11QCtLQ(PlayerActivity r0, View r1) {
        r0.lambda$onCreate$5(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$O9yTxmcF4GuNZ-cdAbo6-cBDWwg, reason: not valid java name */
    public static /* synthetic */ void m654$r8$lambda$O9yTxmcF4GuNZcdAbo6cBDWwg(PlayerActivity r0, Runnable r1, DialogInterface r2) {
        r0.lambda$browseGuide$64(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$Or6mQ4LmX47gE1-X_hd09NVmh1U, reason: not valid java name */
    public static /* synthetic */ void m655$r8$lambda$Or6mQ4LmX47gE1X_hd09NVmh1U(PlayerActivity r0) {
        r0.lambda$onStart$20();
    }

    /* JADX INFO: renamed from: $r8$lambda$PW-YCxUrjRm-tV8sXh6zlKnFKjk, reason: not valid java name */
    public static /* synthetic */ void m656$r8$lambda$PWYCxUrjRmtV8sXh6zlKnFKjk(PlayerActivity r0, View r1) {
        r0.lambda$onCreate$7(r1);
    }

    public static /* synthetic */ DataSpec $r8$lambda$QxlOh4ha2p824bMh4w04Hxaq5VQ(PlayerActivity r0, DataSpec r1) {
        return r0.lambda$startPlayer$28(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$SDNJauhSlyb2-T1NjIXGQO2f5-s, reason: not valid java name */
    public static /* synthetic */ void m657$r8$lambda$SDNJauhSlyb2T1NjIXGQO2f5s(PlayerActivity r0, ExoPlayer r1) {
        r0.lambda$scheduleHealthyReset$34(r1);
    }

    public static /* synthetic */ void $r8$lambda$SEyjL6ZaSV0h5Avkg2CaxgAeq28(PlayerActivity r0, View r1) {
        r0.lambda$onCreate$9(r1);
    }

    public static /* synthetic */ void $r8$lambda$TKaEP_1vnJcujzXdu0aJxLgYRp8(PlayerActivity r0, EpgGridView r1, View r2) {
        r0.lambda$browseGuide$60(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$TQ7l62i9XNkSbG5uhLPTtnv0IL8(PlayerActivity r0, DialogInterface r1) {
        r0.lambda$showEpg$69(r1);
    }

    public static /* synthetic */ void $r8$lambda$Vawa57CJ1xd2qGWFLVsJ9Bf2YPI(PlayerActivity r0, AlertDialog r1, DialogInterface r2) {
        r0.lambda$showPlaybackSettings$45(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$XtjZ1VSSQlxqbhhGDEhsJmBczo8(PlayerActivity r0, int r1) {
        r0.lambda$onCreate$12(r1);
    }

    public static /* synthetic */ void $r8$lambda$Zb1C1R1JyqKD804GGdSJ9hfME4g(PlayerActivity r0, ExoPlayer r1, List r2, int r3, AlertDialog r4, AdapterView r5, View r6, int r7, long r8) {
        r0.lambda$showTracks$49(r1, r2, r3, r4, r5, r6, r7, r8);
    }

    public static /* synthetic */ void $r8$lambda$ab7IXeZfRRRFLxTjARl06GPBfks(PlayerActivity r0, int r1, JSONObject r2) {
        r0.lambda$startPlayer$24(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$d-BF_-lHKZGko7m3BP3AUOv6XJ0, reason: not valid java name */
    public static /* synthetic */ void m658$r8$lambda$dBF_lHKZGko7m3BP3AUOv6XJ0(PlayerActivity r0, int r1, PlaybackException r2, Boolean r3, Throwable r4) {
        r0.lambda$retryToken$39(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$dOj6igksV8aIEESXKgRmBFYUTKk(PlayerActivity r0, Runnable r1, LogoLoader r2, DialogInterface r3) {
        r0.lambda$browse$55(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$eIjgEeB8z9ogvc4euAW415NMNmA(PlayerActivity r0, Entry r1) {
        r0.lambda$loadPlaybackCatalog$14(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$eT_W6TKestYpj9tC6YZ2KFUcr-U, reason: not valid java name */
    public static /* synthetic */ void m659$r8$lambda$eT_W6TKestYpj9tC6YZ2KFUcrU(PlayerActivity r0, Entry r1) {
        r0.lambda$select$19(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$fNJNFsw78D9H2NPN-R53qjqMyv0, reason: not valid java name */
    public static /* synthetic */ void m660$r8$lambda$fNJNFsw78D9H2NPNR53qjqMyv0(PlayerActivity r0, List r1, List r2, Entry r3, long[] r4, int[] r5, ArrayAdapter r6) {
        r0.lambda$showEpg$66(r1, r2, r3, r4, r5, r6);
    }

    public static /* synthetic */ void $r8$lambda$gbUTnvedCL66AkdZjSBIHnxE1I0(PlayerActivity r0, View r1) {
        r0.lambda$onCreate$3(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$hNM9ml1SX-HqDganANIi-Y4DF5c, reason: not valid java name */
    public static /* synthetic */ void m661$r8$lambda$hNM9ml1SXHqDganANIiY4DF5c(PlayerActivity r0, EditText r1, List r2, EpgGridView r3) {
        r0.lambda$browseGuide$63(r1, r2, r3);
    }

    public static /* synthetic */ DataSpec $r8$lambda$i1GLGZ3geDnTj6g3Ex_xlb2Lxlg(PlayerActivity r0, DataSpec r1) {
        return r0.lambda$startPlayer$27(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$iOJSywiXek3p-yVsoRz4ZKr3kik, reason: not valid java name */
    public static /* synthetic */ void m662$r8$lambda$iOJSywiXek3pyVsoRz4ZKr3kik(PlayerActivity r0, List r1, Entry r2, Map r3, int r4) {
        r0.lambda$rebuild$18(r1, r2, r3, r4);
    }

    /* JADX INFO: renamed from: $r8$lambda$j5F-7pbHgmj4CnNa77jxj2hhtOE, reason: not valid java name */
    public static /* synthetic */ WindowInsets m663$r8$lambda$j5F7pbHgmj4CnNa77jxj2hhtOE(PlayerActivity r0, View r1, WindowInsets r2) {
        return r0.lambda$onCreate$11(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$kIk6plorZ1lUoUNSL0JjFZ-s-Os, reason: not valid java name */
    public static /* synthetic */ void m664$r8$lambda$kIk6plorZ1lUoUNSL0JjFZsOs(PlayerActivity r0, int r1, Boolean r2, PlaybackException r3) {
        r0.lambda$retryDazn$36(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$n5ovKKA6ElBN6Ygk9lxxuBbHxtM(PlayerActivity r0, String r1, DialogInterface r2, int r3) {
        r0.lambda$showPlaybackSettings$43(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$o9jtiW6FjgH3YtQGGQ5kV9MXku4(PlayerActivity r0, DialogInterface r1) {
        r0.lambda$showFailureDetails$52(r1);
    }

    public static /* synthetic */ void $r8$lambda$oIEI4t2cUI6YMJvuvYgwVhPlm80(PlayerActivity r0, View r1) {
        r0.lambda$onCreate$8(r1);
    }

    public static /* synthetic */ void $r8$lambda$oVDn0xNKMtKB1tnGhT4pLPRUgjE(PlayerActivity r0, View r1) {
        r0.lambda$onCreate$10(r1);
    }

    public static /* synthetic */ void $r8$lambda$oZq0O2PLzxyewn9J1mjo4Rm_Wnc(PlayerActivity r0, List r1, Catalog r2, Map r3, Entry r4, PlaybackSequence r5) {
        r0.lambda$loadPlaybackCatalog$13(r1, r2, r3, r4, r5);
    }

    /* JADX INFO: renamed from: $r8$lambda$pQWdie8FKizGpf-akr-hZgVs6cI, reason: not valid java name */
    public static /* synthetic */ void m665$r8$lambda$pQWdie8FKizGpfakrhZgVs6cI(PlayerActivity r0, DialogInterface r1, int r2) {
        r0.lambda$showPlaybackSettings$42(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$sAdA7ZHKzRtyJPn8K_veKNUIpuI(PlayerActivity r0, EpgData r1) {
        r0.lambda$loadEpgOnce$15(r1);
    }

    public static /* synthetic */ void $r8$lambda$uxvUQKg8b0z3IzoepPLPA2ZJOpI(PlayerActivity r0, View r1) {
        r0.lambda$onCreate$4(r1);
    }

    public static /* synthetic */ void $r8$lambda$w2RlF9t8G20TJNWc8EqhprhjPJ8(PlayerActivity r0, int r1, boolean r2, Boolean r3, Entry r4) {
        r0.lambda$startPlayer$21(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$wDpLVronO6GBgO7neTlLnXTMXp8(PlayerActivity r0, View r1) {
        r0.lambda$onCreate$2(r1);
    }

    public static /* synthetic */ void $r8$lambda$x1bU3OLW2ZcCUqNYzZRNypj3dJE(PlayerActivity r0, int r1, Entry r2, PlaybackSequence r3) {
        r0.lambda$rebuild$17(r1, r2, r3);
    }

    public static /* synthetic */ WindowInsets $r8$lambda$znvHAlJoB_Y39gg9Def_3LJWTFA(PlayerActivity r0, LinearLayout r1, View r2, WindowInsets r3) {
        return r0.lambda$browseGuide$65(r1, r2, r3);
    }

    /* JADX INFO: renamed from: -$$Nest$fgetentry, reason: not valid java name */
    static /* bridge */ /* synthetic */ Entry m666$$Nest$fgetentry(PlayerActivity r0) {
        return r0.entry;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetepgRender, reason: not valid java name */
    static /* bridge */ /* synthetic */ Runnable m667$$Nest$fgetepgRender(PlayerActivity r0) {
        return r0.epgRender;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetepgRequested, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m668$$Nest$fgetepgRequested(PlayerActivity r0) {
        return r0.epgRequested;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetfallbackList, reason: not valid java name */
    static /* bridge */ /* synthetic */ String m669$$Nest$fgetfallbackList(PlayerActivity r0) {
        return r0.fallbackList;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetfirstFrameMs, reason: not valid java name */
    static /* bridge */ /* synthetic */ long m670$$Nest$fgetfirstFrameMs(PlayerActivity r2) {
        return r2.firstFrameMs;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetmain, reason: not valid java name */
    static /* bridge */ /* synthetic */ Handler m671$$Nest$fgetmain(PlayerActivity r0) {
        return r0.main;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetplayer, reason: not valid java name */
    static /* bridge */ /* synthetic */ ExoPlayer m672$$Nest$fgetplayer(PlayerActivity r0) {
        return r0.player;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetrateDeadline, reason: not valid java name */
    static /* bridge */ /* synthetic */ long m673$$Nest$fgetrateDeadline(PlayerActivity r2) {
        return r2.rateDeadline;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetroot, reason: not valid java name */
    static /* bridge */ /* synthetic */ FrameLayout m674$$Nest$fgetroot(PlayerActivity r0) {
        return r0.root;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetsettingsRender, reason: not valid java name */
    static /* bridge */ /* synthetic */ Runnable m675$$Nest$fgetsettingsRender(PlayerActivity r0) {
        return r0.settingsRender;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetstarted, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m676$$Nest$fgetstarted(PlayerActivity r0) {
        return r0.started;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetstartup, reason: not valid java name */
    static /* bridge */ /* synthetic */ PlaybackStartup m677$$Nest$fgetstartup(PlayerActivity r0) {
        return r0.startup;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetstartupAt, reason: not valid java name */
    static /* bridge */ /* synthetic */ long m678$$Nest$fgetstartupAt(PlayerActivity r2) {
        return r2.startupAt;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetstatus, reason: not valid java name */
    static /* bridge */ /* synthetic */ TextView m679$$Nest$fgetstatus(PlayerActivity r0) {
        return r0.status;
    }

    /* JADX INFO: renamed from: -$$Nest$fgettokenTicket, reason: not valid java name */
    static /* bridge */ /* synthetic */ int m680$$Nest$fgettokenTicket(PlayerActivity r0) {
        return r0.tokenTicket;
    }

    /* JADX INFO: renamed from: -$$Nest$fgettracksRender, reason: not valid java name */
    static /* bridge */ /* synthetic */ Runnable m681$$Nest$fgettracksRender(PlayerActivity r0) {
        return r0.tracksRender;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetview, reason: not valid java name */
    static /* bridge */ /* synthetic */ PlayerView m682$$Nest$fgetview(PlayerActivity r0) {
        return r0.view;
    }

    /* JADX INFO: renamed from: -$$Nest$fputfirstFrameMs, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m683$$Nest$fputfirstFrameMs(PlayerActivity r0, long r1) {
        r0.firstFrameMs = r1;
    }

    /* JADX INFO: renamed from: -$$Nest$fputhttpRetryTask, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m684$$Nest$fputhttpRetryTask(PlayerActivity r0, Runnable r1) {
        r0.httpRetryTask = r1;
    }

    /* JADX INFO: renamed from: -$$Nest$fputlastPlaybackFailure, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m685$$Nest$fputlastPlaybackFailure(PlayerActivity r0, PlaybackException r1) {
        r0.lastPlaybackFailure = r1;
    }

    /* JADX INFO: renamed from: -$$Nest$fputplaybackError, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m686$$Nest$fputplaybackError(PlayerActivity r0, boolean r1) {
        r0.playbackError = r1;
    }

    /* JADX INFO: renamed from: -$$Nest$fputrateDeadline, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m687$$Nest$fputrateDeadline(PlayerActivity r0, long r1) {
        r0.rateDeadline = r1;
    }

    /* JADX INFO: renamed from: -$$Nest$fputscreenDrawn, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m688$$Nest$fputscreenDrawn(PlayerActivity r0, boolean r1) {
        r0.screenDrawn = r1;
    }

    /* JADX INFO: renamed from: -$$Nest$fputtokenWaiting, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m689$$Nest$fputtokenWaiting(PlayerActivity r0, boolean r1) {
        r0.tokenWaiting = r1;
    }

    /* JADX INFO: renamed from: -$$Nest$fputtrackParameters, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m690$$Nest$fputtrackParameters(PlayerActivity r0, TrackSelectionParameters r1) {
        r0.trackParameters = r1;
    }

    /* JADX INFO: renamed from: -$$Nest$mcancelHealthyReset, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m691$$Nest$mcancelHealthyReset(PlayerActivity r0) {
        r0.cancelHealthyReset();
    }

    /* JADX INFO: renamed from: -$$Nest$mdp, reason: not valid java name */
    static /* bridge */ /* synthetic */ int m692$$Nest$mdp(PlayerActivity r0, int r1) {
        return r0.m404dp(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$mloadEpgOnce, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m693$$Nest$mloadEpgOnce(PlayerActivity r0) {
        r0.loadEpgOnce();
    }

    /* JADX INFO: renamed from: -$$Nest$mloadPlaybackCatalog, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m694$$Nest$mloadPlaybackCatalog(PlayerActivity r0) {
        r0.loadPlaybackCatalog();
    }

    /* JADX INFO: renamed from: -$$Nest$mretryAlternative, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m695$$Nest$mretryAlternative(PlayerActivity r0, PlaybackException r1) {
        return r0.retryAlternative(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$mretryDazn, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m696$$Nest$mretryDazn(PlayerActivity r0, PlaybackException r1) {
        return r0.retryDazn(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$mretryDecoder, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m697$$Nest$mretryDecoder(PlayerActivity r0, PlaybackException r1) {
        return r0.retryDecoder(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$mretryLive, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m698$$Nest$mretryLive(PlayerActivity r0, PlaybackException r1) {
        return r0.retryLive(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$mretryRateLimit, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m699$$Nest$mretryRateLimit(PlayerActivity r0, PlaybackException r1) {
        return r0.retryRateLimit(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$mretryToken, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m700$$Nest$mretryToken(PlayerActivity r0, PlaybackException r1) {
        return r0.retryToken(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$msaveHistory, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m701$$Nest$msaveHistory(PlayerActivity r0) {
        r0.saveHistory();
    }

    /* JADX INFO: renamed from: -$$Nest$mscheduleHealthyReset, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m702$$Nest$mscheduleHealthyReset(PlayerActivity r0) {
        r0.scheduleHealthyReset();
    }

    /* JADX INFO: renamed from: -$$Nest$mshape, reason: not valid java name */
    static /* bridge */ /* synthetic */ GradientDrawable m703$$Nest$mshape(PlayerActivity r0, int r1, int r2) {
        return r0.shape(r1, r2);
    }

    /* JADX INFO: renamed from: -$$Nest$mshowError, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m704$$Nest$mshowError(PlayerActivity r0, String r1) {
        r0.showError(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$mstartPlayer, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m705$$Nest$mstartPlayer(PlayerActivity r0) {
        r0.startPlayer();
    }

    /* JADX INFO: renamed from: -$$Nest$mupdateProgramme, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m706$$Nest$mupdateProgramme(PlayerActivity r0) {
        r0.updateProgramme();
    }

    /* JADX INFO: renamed from: -$$Nest$mupdateTracks, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m707$$Nest$mupdateTracks(PlayerActivity r0) {
        r0.updateTracks();
    }

    public PlayerActivity() {
        this.failedDecoders = new HashSet();
        this.epg = new EpgData();
        this.playbackError = false;
        this.epgLoaded = false;
        this.startup = new PlaybackStartup();
        this.startupAt = SystemClock.elapsedRealtime();
        this.firstFrameMs = -1;
        this.playerVolume = 1.0f;
        this.qualityLabel = "Calidad · Auto";
        this.audioLabel = "Audio";
        this.subtitleLabel = "Subtítulos";
        this.playbackSpeed = 1.0f;
        this.fallbackCatalog = new Catalog();
        this.fallbackList = "";
        this.reconnect = new LiveReconnectPolicy();
        this.tokenWorker = Executors.newSingleThreadExecutor();
        this.zoom = false;
        this.loaded = false;
        this.started = false;
        this.catalog = new ArrayList();
        this.providers = new IdentityHashMap();
        this.worker = Executors.newSingleThreadExecutor(new PlayerActivity$$ExternalSyntheticLambda34());
        this.main = new Handler(Looper.getMainLooper());
        this.historyTick = new RunnableC10091(this);
        this.epgTick = new RunnableC101212(this);
    }

    private void add(LinearLayout r5, View r6) {
        LinearLayout.LayoutParams r0 = new LinearLayout.LayoutParams(0, m404dp(38), 1.0f);
        r0.setMargins(m404dp(3), 0, m404dp(3), 0);
        r5.addView(r6, r0);
    }

    private void adjacent(int r5) {
        if (this.loaded == true) goto L5;
        return;
    L5:
        Entry r0 = this.sequence.neighbor(r5);
        if (r0 == null) goto L8;
    L16:
        if (r0 == null) goto L19;
        select(r0);
        return;
    L19:
        return;
    L8:
        if (this.entry.kind.equals("Canales") == false) goto L16;
        if (this.sequence.entries.size() <= 1) goto L16;
        List<Entry> r1 = this.sequence.entries;
        if (r5 <= 0) goto L14;
        int r2 = 0;
    L15:
        r0 = r1.get(r2);
        goto L16
    L14:
        r2 = this.sequence.entries.size() - 1;
        goto L15
    }

    private void adjustVolume(int r7) {
        AudioManager r0 = (AudioManager) getSystemService(MimeTypes.BASE_TYPE_AUDIO);
        if (r0 != null) goto L5;
    L12:
        this.playerMuted = false;
        this.playerVolume = Math.max(0.0f, Math.min(1.0f, Math.round((this.playerVolume + (r7 * 0.05f)) * 100.0f) / 100.0f));
        getPreferences(0).edit().putFloat("player-volume", this.playerVolume).apply();
        if (this.player == null) goto L15;
        this.player.setVolume(this.playerVolume);
    L15:
        Toast.makeText(this, "Volumen CHDTV · " + Math.round(this.playerVolume * 100.0f) + "%", 0).show();
        return;
    L5:
        if (r0.isVolumeFixed() == true) goto L12;
        if (r7 <= 0) goto L9;
        int r2 = 1;
    L10:
        r0.adjustStreamVolume(3, r2, 1);
        return;
    L9:
        r2 = -1;
        goto L10
    }

    private void browse() {
        if (this.loaded == false) goto L9;
        if ("Canales".equals(this.entry.kind) == false) goto L9;
        browseGuide();
        return;
    L9:
        if (this.loaded == true) goto L12;
        loadPlaybackCatalog();
        Toast.makeText(this, "Preparando tu catálogo…", 0).show();
        return;
    L12:
        LinearLayout r0 = new LinearLayout(this);
        r0.setOrientation(1);
        r0.setPadding(m404dp(12), 0, m404dp(12), 0);
        final LogoLoader r5 = new LogoLoader(this);
        final ArrayList r1 = new ArrayList(this.sequence.entries);
        final ArrayList r4 = new ArrayList();
        ListView r2 = new ListView(this);
        r2.setDivider(null);
        r2.setDividerHeight(m404dp(6));
        final int r3 = android.R.layout.simple_list_item_1;
        final ArrayAdapter<Entry> r6 = new C10229(this, this, r3, r4, r5);
        r2.setAdapter(r6);
        final EditText r7 = new EditText(this);
        r7.setSingleLine();
        r7.setTextColor(AppTheme.color(this, -1903617));
        r7.setHintTextColor(-7232068);
        r7.setTextSize(14.0f);
        String r12 = "Películas";
        if ("Series".equals(this.entry.kind) == false) goto L16;
        String r8 = "Buscar episodio";
    L19:
        r7.setHint(r8);
        r0.addView(r7, new LinearLayout.LayoutParams(-1, m404dp(42)));
        final Runnable r9 = new PlayerActivity$$ExternalSyntheticLambda61(r4, r7, r1, r6);
        r7.addTextChangedListener(new C101010(this, r9));
        r0.addView(r2, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        if ("Series".equals(this.entry.kind) == false) goto L24;
        StringBuilder r10 = new StringBuilder().append(CatalogCounts.series(this.entry));
        String r11 = " · Episodios";
    L22:
        r12 = r10.append(r11).toString();
    L27:
        r9.run();
        final AlertDialog r13 = new AlertDialog.Builder(this).setTitle(r12).setView(r0).setNegativeButton("Cerrar", null).create();
        r2.setOnItemClickListener(new PlayerActivity$$ExternalSyntheticLambda62(this, r4, r13));
        this.browseDialog = r13;
        r13.setOnDismissListener(new PlayerActivity$$ExternalSyntheticLambda63(this, r9, r5));
        r13.show();
        styleDialog(r13);
        r13.getWindow().getDecorView().setSystemUiVisibility(5894);
        r13.getWindow().setBackgroundDrawable(new ColorDrawable(-2063062197));
        r13.getWindow().setLayout(-1, -1);
        int r14 = 0;
    L29:
        if (r14 >= r4.size()) goto L37;
        if (PlaybackSequence.same((Entry) r4.get(r14), this.entry) == true) goto L32;
        r14 = r14 + 1;
        goto L29
    L32:
        r2.setSelection(r14);
        return;
    L37:
        return;
    L24:
        if ("Películas".equals(this.entry.kind) == true) goto L27;
        r10 = new StringBuilder().append("TV · ");
        r11 = PlaybackSequence.providerFor(this.catalog, this.entry, this.providers);
        goto L22
    L16:
        if ("Películas".equals(this.entry.kind) == false) goto L18;
        r8 = "Buscar película";
        goto L19
    L18:
        r8 = "Buscar canal";
        goto L19
    }

    private void browseGuide() {
        if (this.browseDialog != null) goto L5;
    L7:
        final Dialog r0 = new Dialog(this);
        this.browseDialog = r0;
        r0.requestWindowFeature(1);
        final LinearLayout r2 = new LinearLayout(this);
        r2.setOrientation(1);
        r2.setBackgroundColor(AppTheme.color(this, 1695034187));
        r2.setPadding(m404dp(8), m404dp(4), m404dp(8), m404dp(4));
        LinearLayout r3 = new LinearLayout(this);
        r3.setGravity(16);
        r2.addView(r3, new LinearLayout.LayoutParams(-1, m404dp(44)));
        TextView r5 = new TextView(this);
        r5.setText("TV / Guía · " + PlaybackSequence.providerFor(this.catalog, this.entry, this.providers));
        r5.setTextColor(AppTheme.color(this, -8790273));
        r5.setTextSize(16.0f);
        r3.addView(r5, new LinearLayout.LayoutParams(0, -2, 1.0f));
        final EpgGridView r6 = new EpgGridView(this);
        r6.setOverlay(true);
        add(r3, button("−1 h", new PlayerActivity$$ExternalSyntheticLambda38(r6)));
        add(r3, button("Ahora", new PlayerActivity$$ExternalSyntheticLambda39(r6)));
        add(r3, button("+1 h", new PlayerActivity$$ExternalSyntheticLambda40(r6)));
        add(r3, button("Cerrar", new PlayerActivity$$ExternalSyntheticLambda41(r0)));
        final EditText r1 = new EditText(this);
        r1.setSingleLine();
        r1.setTextSize(14.0f);
        r1.setTextColor(-1);
        r1.setHintTextColor(-5978919);
        r1.setHint("Buscar canal del proveedor");
        r2.addView(r1, new LinearLayout.LayoutParams(-1, m404dp(40)));
        r2.addView(r6, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        LinearLayout r11 = new LinearLayout(this);
        r11.setGravity(16);
        r2.addView(r11, new LinearLayout.LayoutParams(-1, m404dp(64)));
        final TextView r4 = new TextView(this);
        r4.setTextColor(-1);
        r4.setTextSize(13.0f);
        r4.setMaxLines(3);
        r11.addView(r4, new LinearLayout.LayoutParams(0, -2, 1.0f));
        add(r11, button("Grabar EPG", new PlayerActivity$$ExternalSyntheticLambda42(this, r6)));
        r6.setActivationListener(new PlayerActivity$$ExternalSyntheticLambda43(this, r6, r0));
        r6.setSelectionListener(new PlayerActivity$$ExternalSyntheticLambda45(this, r4));
        final ArrayList r9 = new ArrayList(this.sequence.entries);
        final Runnable r10 = new PlayerActivity$$ExternalSyntheticLambda46(this, r1, r9, r6);
        r1.addTextChangedListener(new C101111(this, r10));
        r10.run();
        r0.setContentView(r2);
        r0.setOnDismissListener(new PlayerActivity$$ExternalSyntheticLambda47(this, r10));
        r0.show();
        Window r12 = r0.getWindow();
        r12.setBackgroundDrawable(new ColorDrawable(0));
        r12.clearFlags(2);
        r12.getDecorView().setSystemUiVisibility(5894);
        r12.setLayout(-1, -1);
        r2.setOnApplyWindowInsetsListener(new PlayerActivity$$ExternalSyntheticLambda48(this, r2));
        r6.requestFocus();
        return;
    L5:
        if (this.browseDialog.isShowing() == false) goto L7;
    }

    private Button button(String r8, View.OnClickListener r9) {
        Button r0 = new Button(this);
        r0.setText(r8);
        r0.setAllCaps(false);
        r0.setTextSize(12.0f);
        r0.setTextColor(-1);
        r0.setMinimumWidth(0);
        r0.setMinWidth(0);
        r0.setMinimumHeight(0);
        r0.setMinHeight(0);
        r0.setPadding(m404dp(4), m404dp(4), m404dp(4), m404dp(4));
        StateListDrawable r2 = new StateListDrawable();
        r2.addState(new int[]{android.R.attr.state_focused}, shape(-14396058, -12986907));
        r2.addState(new int[]{android.R.attr.state_pressed}, shape(-14396058, -12986907));
        r2.addState(new int[0], shape(-1340857547, -13348762));
        r0.setBackground(r2);
        r0.setOnClickListener(r9);
        return r0;
    }

    private boolean canFloat() {
        if (this.started == true) goto L5;
    L17:
        return false;
    L5:
        if (isFinishing() == true) goto L17;
        if (PipPlayback.supported(this) == false) goto L17;
        if (this.player == null) goto L17;
        if (this.player.getPlayWhenReady() == false) goto L17;
        if (this.player.getPlaybackState() != 3) goto L15;
    L16:
        return true;
    L15:
        if (this.player.getPlaybackState() != 2) goto L17;
        goto L16
    }

    private void cancelHealthyReset() {
        if (this.healthyTask == null) goto L6;
        this.main.removeCallbacks(this.healthyTask);
        this.healthyTask = null;
        return;
    }

    private void cancelHttpRetry() {
        if (this.httpRetryTask == null) goto L6;
        this.main.removeCallbacks(this.httpRetryTask);
        this.httpRetryTask = null;
        return;
    }

    private void cancelReconnect(boolean r3) {
        if (this.reconnectTask == null) goto L5;
        this.main.removeCallbacks(this.reconnectTask);
        this.reconnectTask = null;
    L5:
        this.reconnectWaiting = false;
        cancelHealthyReset();
        if (r3 == false) goto L9;
        this.reconnect.reset();
        return;
    }

    private void chooseVideoFraming() {
        this.zoom = !this.zoom;
        PlayerView r0 = this.view;
        if (this.zoom == false) goto L5;
        int r1 = 4;
    L6:
        r0.setResizeMode(r1);
        Button r2 = this.fit;
        if (this.zoom == false) goto L9;
        String r3 = "Proporción";
    L10:
        r2.setText(r3);
        return;
    L9:
        r3 = "Pantalla";
        goto L10
    L5:
        r1 = 0;
        goto L6
    }

    private byte[] clearKey(String r22) throws Exception {
        if (r22 != null) goto L5;
        String r3 = "";
    L6:
        JSONArray r4 = new JSONArray();
        if (r3.startsWith("{") == true) goto L9;
    L41:
        Iterator<Map.Entry<String, String>> r0 = ClearKeyPairs.parse(r3).entrySet().iterator();
    L43:
        if (r0.hasNext() == false) goto L46;
        Map.Entry<String, String> r2 = r0.next();
        r4.put(new JSONObject().put("kty", "oct").put("kid", hex64(r2.getKey())).put("k", hex64(r2.getValue())));
    L46:
        return new JSONObject().put("keys", r4).put("type", "temporary").toString().getBytes(StandardCharsets.UTF_8);
    L9:
        if (r3.contains("\"keys\"") == false) goto L41;
        JSONObject r5 = new JSONObject(r3);     // Catch: Exception -> L37
        JSONArray r11 = r5.getJSONArray("keys");     // Catch: Exception -> L37
        if (r11.length() == 0) goto L34;
        int r12 = 0;
    L14:
        if (r12 >= r11.length()) goto L46;
        JSONObject r13 = r11.getJSONObject(r12);     // Catch: Exception -> L37
        if (r13.getString("kty").equals("oct") == false) goto L31;
        String[] r14 = {"kid", "k"};     // Catch: Exception -> L37
        int r15 = 0;
    L19:
        String r16 = r3;
        if (r15 >= 2) goto L29;
        String r18 = r13.getString(r14[r15]);     // Catch: Exception -> L35
        JSONObject r20 = r5;
        if (r18.matches("[A-Za-z0-9_+/\\-]{22}={0,2}") == false) goto L28;
        JSONArray r19 = r11;
        if (Base64.decode(r18.replace('+', '-').replace('/', '_'), 8).length != 16) goto L28;
        r15 = r15 + 1;     // Catch: Exception -> L35
        r3 = r16;
        r11 = r19;
        r5 = r20;
    L28:
        throw new IllegalArgumentException();     // Catch: Exception -> L35
    L29:
        JSONObject r21 = r5;
        r4.put(new JSONObject().put("kty", "oct").put("kid", r13.getString("kid").replace('+', '-').replace('/', '_').replace("=", "")).put("k", r13.getString("k").replace('+', '-').replace('/', '_').replace("=", "")));     // Catch: Exception -> L35
        r12 = r12 + 1;     // Catch: Exception -> L35
        r3 = r16;
        r11 = r11;
        r5 = r21;
        goto L14
    L31:
        throw new IllegalArgumentException();     // Catch: Exception -> L35
    L34:
        throw new IllegalArgumentException();     // Catch: Exception -> L35
    L40:
        throw new IllegalArgumentException("Objetos JWK ClearKey no válidos");
    L5:
        r3 = r22.trim();
        goto L6
    }

    private String clock(long r4) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(r4));
    }

    /* JADX INFO: renamed from: dp */
    private int m404dp(int r3) {
        return Math.round(r3 * getResources().getDisplayMetrics().density);
    }

    private boolean enterFloatingWindow() {
        if (canFloat() == true) goto L11;
        return false;
    L11:
        return enterPictureInPictureMode(floatingParams());
    L10:
        return false;
    }

    private PictureInPictureParams floatingParams() {
        if (this.player != null) goto L5;
        VideoSize r0 = VideoSize.UNKNOWN;
    L6:
        PictureInPictureParams.Builder r1 = new PictureInPictureParams.Builder().setAspectRatio(PipPlayback.aspect(r0.width, r0.height, r0.pixelWidthHeightRatio));
        Rect r2 = new Rect();
        if (this.view == null) goto L14;
        if (this.view.getGlobalVisibleRect(r2) == false) goto L14;
        if (r2.isEmpty() == true) goto L14;
        r1.setSourceRectHint(r2);
    L14:
        if (Build.VERSION.SDK_INT < 31) goto L23;
        if (PipPlayback.enabled(this) == true) goto L18;
    L20:
        boolean r3 = false;
    L21:
        r1.setAutoEnterEnabled(r3).setSeamlessResizeEnabled(true);
        goto L23
    L18:
        if (canFloat() == false) goto L20;
        r3 = true;
    L23:
        return r1.build();
    L5:
        r0 = this.player.getVideoSize();
        goto L6
    }

    private ArrayAdapter<String> guideAdapter(final List<String> r3) {
        final int r1 = android.R.layout.simple_list_item_1;
        return new C101313(this, this, r1, r3);
    }

    private String hex64(String r6) {
        if (r6.matches("[a-fA-F0-9]{32}") == false) goto L10;
        byte[] r1 = new byte[16];
        int r2 = 0;
    L5:
        if (r2 >= 16) goto L8;
        r1[r2] = (byte) Integer.parseInt(r6.substring(r2 * 2, (r2 * 2) + 2), 16);
        r2 = r2 + 1;
        goto L5
    L8:
        return Base64.encodeToString(r1, 11);
    L10:
        throw new IllegalArgumentException("Clave ClearKey inválida");
    }

    private void immersive() {
        if (inFloatingWindow() == false) goto L6;
        return;
    L6:
        if (Build.VERSION.SDK_INT < 30) goto L10;
        WindowInsetsController r0 = getWindow().getInsetsController();
        if (r0 == null) goto L12;
        r0.hide(WindowInsets.Type.systemBars());
        r0.setSystemBarsBehavior(2);
        return;
    L12:
        return;
    L10:
        getWindow().getDecorView().setSystemUiVisibility(5894);
    }

    private boolean inFloatingWindow() {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
    L7:
        return false;
    L5:
        if (isInPictureInPictureMode() == false) goto L7;
        return true;
    }

    private static boolean isVodAudioFallbackMime(String r1) {
        if (MimeTypes.AUDIO_AAC.equals(r1) == false) goto L5;
    L10:
        return true;
    L5:
        if (MimeTypes.AUDIO_E_AC3.equals(r1) == true) goto L10;
        if (MimeTypes.AUDIO_E_AC3_JOC.equals(r1) == true) goto L10;
        return false;
    }

    static /* synthetic */ void lambda$browse$53(List r5, EditText r6, List r7, ArrayAdapter r8) {
        r5.clear();
        String r0 = r6.getText().toString().toLowerCase(Locale.ROOT);
        Iterator r1 = r7.iterator();
    L4:
        if (r1.hasNext() == false) goto L10;
        Entry r2 = (Entry) r1.next();
        if (r0.isEmpty() == true) goto L9;
        if ((r2.title + " " + r2.group).toLowerCase(Locale.ROOT).contains(r0) == false) goto L4;
    L9:
        r5.add(r2);
        goto L4
    L10:
        r8.notifyDataSetChanged();
    }

    private /* synthetic */ void lambda$browse$54(List r2, AlertDialog r3, AdapterView r4, View r5, int r6, long r7) {
        Entry r0 = (Entry) r2.get(r6);
        r3.dismiss();
        select(r0);
    }

    private /* synthetic */ void lambda$browse$55(Runnable r2, LogoLoader r3, DialogInterface r4) {
        this.main.removeCallbacks(r2);
        r3.close();
        this.browseDialog = null;
        immersive();
    }

    static /* synthetic */ void lambda$browseGuide$56(EpgGridView r1, View r2) {
        r1.shift(-1);
    }

    static /* synthetic */ void lambda$browseGuide$57(EpgGridView r0, View r1) {
        r0.now();
    }

    static /* synthetic */ void lambda$browseGuide$58(EpgGridView r1, View r2) {
        r1.shift(1);
    }

    static /* synthetic */ void lambda$browseGuide$59(Dialog r0, View r1) {
        r0.dismiss();
    }

    private /* synthetic */ void lambda$browseGuide$60(EpgGridView r4, View r5) {
        Entry r0 = r4.selectedChannel();
        if (r0 == null) goto L6;
        RecordingUi.choose(this, r0, r4.selectedProgramme(), this.epg.schedule(r0));
        return;
    }

    private /* synthetic */ void lambda$browseGuide$61(EpgGridView r6, Dialog r7, Entry r8) {
        EpgData.Programme r0 = r6.selectedProgramme();
        if (r0 != null) goto L5;
    L7:
        r7.dismiss();
        select(r8);
        return;
    L5:
        if (r0.start <= System.currentTimeMillis()) goto L7;
        RecordingUi.programme(this, r8, r0);
    }

    private /* synthetic */ void lambda$browseGuide$62(TextView r6, Entry r7, EpgData.Programme r8) {
        if (r7 != null) goto L4;
        String r0 = "Sin canales";
    L9:
        r6.setText(r0);
        return;
    L4:
        StringBuilder r1 = new StringBuilder().append(r7.title).append("\n");
        if (r8 != null) goto L7;
        String r2 = "Sin programación disponible";
    L8:
        r0 = r1.append(r2).toString();
        goto L9
    L7:
        r2 = clock(r8.start) + "–" + clock(r8.stop) + " · " + r8.title + "\n" + r8.description;
        goto L8
    }

    private /* synthetic */ void lambda$browseGuide$63(EditText r7, List r8, EpgGridView r9) {
        ArrayList r0 = new ArrayList();
        String r1 = r7.getText().toString().toLowerCase(Locale.ROOT);
        Iterator r2 = r8.iterator();
    L4:
        if (r2.hasNext() == false) goto L8;
        Entry r3 = (Entry) r2.next();
        if (r3.title.toLowerCase(Locale.ROOT).contains(r1) == false) goto L4;
        r0.add(r3);
        goto L4
    L8:
        r9.setChannels(r0, this.epg, this.entry.identity());
    }

    private /* synthetic */ void lambda$browseGuide$64(Runnable r2, DialogInterface r3) {
        this.main.removeCallbacks(r2);
        this.browseDialog = null;
        immersive();
    }

    private /* synthetic */ WindowInsets lambda$browseGuide$65(LinearLayout r6, View r7, WindowInsets r8) {
        r6.setPadding(m404dp(8) + r8.getSystemWindowInsetLeft(), m404dp(4) + r8.getSystemWindowInsetTop(), m404dp(8) + r8.getSystemWindowInsetRight(), m404dp(4) + r8.getSystemWindowInsetBottom());
        return r8;
    }

    private /* synthetic */ void lambda$loadEpgOnce$15(EpgData r2) {
        if (isFinishing() == false) goto L5;
        return;
    L5:
        if (isDestroyed() == true) goto L13;
        this.epg = r2;
        this.epgLoaded = true;
        refreshEpg();
        if (this.epgRender == null) goto L12;
        this.epgRender.run();
        return;
    L12:
        return;
    }

    private /* synthetic */ void lambda$loadEpgOnce$16() {
        EpgData r0 = new EpgData();
        Object r1 = new AppStorage(getApplicationContext()).read("epg.dat");     // Catch: Exception -> L7
        if ((r1 instanceof EpgData) == false) goto L8;
        r0 = (EpgData) r1;     // Catch: Exception -> L7
        r0.ensureIndexed();     // Catch: Exception -> L7
    L8:
        final EpgData r2 = r0;
        runOnUiThread(new PlayerActivity$$ExternalSyntheticLambda30(this, r2));
    }

    private /* synthetic */ void lambda$loadPlaybackCatalog$13(List r3, Catalog r4, Map r5, Entry r6, PlaybackSequence r7) {
        if (isFinishing() == false) goto L5;
        return;
    L5:
        if (isDestroyed() == true) goto L20;
        this.catalog = r3;
        this.fallbackCatalog = r4;
        this.providers = r5;
        this.loaded = true;
        if (this.entry != r6) goto L10;
        this.sequence = r7;
        updateButtons();
    L12:
        if (this.pendingFallbackFailure == null) goto L18;
        PlaybackException r0 = this.pendingFallbackFailure;
        this.pendingFallbackFailure = null;
        if (retryAlternative(r0) == true) goto L19;
        showError(PlaybackFailure.summary(r0));
        return;
    L19:
        return;
    L18:
        return;
    L10:
        rebuild();
        goto L12
    }

    private /* synthetic */ void lambda$loadPlaybackCatalog$14(Entry r1) {
        SectionPlayback.load(this, r1);
    }

    static /* synthetic */ void lambda$new$0(Runnable r1) {
        Process.setThreadPriority(10);
        r1.run();
    }

    static /* synthetic */ Thread lambda$new$1(final Runnable r3) {
        return new Thread(new PlayerActivity$$ExternalSyntheticLambda31(r3), "CHDTVPlayerCatalog");
    }

    private /* synthetic */ void lambda$onCreate$10(View r1) {
        showPlaybackSettings();
    }

    private /* synthetic */ WindowInsets lambda$onCreate$11(View r7, WindowInsets r8) {
        int r1 = m404dp(8);
        int r3 = m404dp(12);
        int r2 = m404dp(12);
        if (Build.VERSION.SDK_INT >= 28) goto L5;
    L7:
        this.controls.setPadding(r3, r1, r2, m404dp(8));
        return r8;
    L5:
        if (r8.getDisplayCutout() == null) goto L7;
        r1 = r1 + r8.getDisplayCutout().getSafeInsetTop();
        r3 = r3 + r8.getDisplayCutout().getSafeInsetLeft();
        r2 = r2 + r8.getDisplayCutout().getSafeInsetRight();
        goto L7
    }

    private /* synthetic */ void lambda$onCreate$12(int r3) {
        LinearLayout r0 = this.controls;
        if (inFloatingWindow() == false) goto L5;
        int r1 = 8;
    L6:
        r0.setVisibility(r1);
        return;
    L5:
        r1 = r3;
        goto L6
    }

    private /* synthetic */ void lambda$onCreate$2(View r2) {
        adjacent(-1);
    }

    private /* synthetic */ void lambda$onCreate$3(View r2) {
        adjacent(1);
    }

    private /* synthetic */ void lambda$onCreate$4(View r1) {
        chooseVideoFraming();
    }

    private /* synthetic */ void lambda$onCreate$5(View r1) {
        browse();
    }

    private /* synthetic */ void lambda$onCreate$6(View r1) {
        showEpg();
    }

    private /* synthetic */ void lambda$onCreate$7(View r1) {
        finish();
    }

    private /* synthetic */ void lambda$onCreate$8(View r2) {
        adjustVolume(-1);
    }

    private /* synthetic */ void lambda$onCreate$9(View r2) {
        adjustVolume(1);
    }

    private /* synthetic */ void lambda$onStart$20() {
        this.started = true;
        primeTvAccount();
        startPlayer();
        if (tvHlsEnabled() == false) goto L5;
        loadPlaybackCatalog();
    L5:
        this.main.removeCallbacks(this.historyTick);
        this.main.postDelayed(this.historyTick, Renderer.DEFAULT_DURATION_TO_PROGRESS_US);
        refreshEpg();
        immersive();
    }

    private /* synthetic */ void lambda$rebuild$17(int r2, Entry r3, PlaybackSequence r4) {
        if (r2 == this.sequenceTicket) goto L5;
        return;
    L5:
        if (this.entry == r3) goto L7;
        return;
    L7:
        if (isFinishing() == false) goto L9;
        return;
    L9:
        if (isDestroyed() == true) goto L15;
        this.sequence = r4;
        updateButtons();
        return;
    }

    private /* synthetic */ void lambda$rebuild$18(List r3, final Entry r4, Map r5, final int r6) {
        final PlaybackSequence r0 = new PlaybackSequence(r3, r4, r5);
        runOnUiThread(new PlayerActivity$$ExternalSyntheticLambda59(this, r6, r4, r0));
    }

    private /* synthetic */ void lambda$retryAlternative$40(Entry r4, ChannelFallback.Choice r5) {
        this.fallbackSwitching = false;
        if (this.started == true) goto L5;
        return;
    L5:
        if (isDestroyed() == true) goto L10;
        this.entry = r4;
        this.fallbackList = r5.list;
        this.position = 0;
        this.tokenPreflight = false;
        this.tokenScoped = false;
        rebuild();
        refreshEpg();
        startPlayer();
        return;
    }

    private /* synthetic */ void lambda$retryDazn$36(int r2, Boolean r3, PlaybackException r4) {
        if (r2 == this.tokenTicket) goto L5;
        return;
    L5:
        if (this.started == true) goto L7;
        return;
    L7:
        if (isDestroyed() == true) goto L20;
        this.tokenWaiting = false;
        if (Boolean.TRUE.equals(r3) == false) goto L13;
        startPlayer();
        return;
    L13:
        if (retryAlternative(r4) == true) goto L19;
        showError("No se pudo renovar DAZN · revisa la sesión en Ajustes");
        return;
    L19:
        return;
    }

    private /* synthetic */ void lambda$retryDazn$37(final int r2, final PlaybackException r3, final Boolean r4, Throwable r5) {
        runOnUiThread(new PlayerActivity$$ExternalSyntheticLambda26(this, r2, r4, r3));
    }

    private /* synthetic */ void lambda$retryLive$35(int r3, Entry r4) {
        this.reconnectTask = null;
        this.reconnectWaiting = false;
        if (r3 == this.tokenTicket) goto L5;
        return;
    L5:
        if (this.started == true) goto L7;
        return;
    L7:
        if (isFinishing() == false) goto L9;
        return;
    L9:
        if (isDestroyed() == false) goto L11;
        return;
    L11:
        if (this.entry == r4) goto L13;
        return;
    L13:
        if (TimedAccess.allowed(this) == false) goto L22;
        this.position = 0;
        this.startupAt = SystemClock.elapsedRealtime();
        this.firstFrameMs = -1;
        startPlayer();
        return;
    }

    private /* synthetic */ void lambda$retryToken$38(int r5, Boolean r6, PlaybackException r7) {
        if (r5 == this.tokenTicket) goto L5;
        return;
    L5:
        if (this.started == true) goto L7;
        return;
    L7:
        if (isDestroyed() == true) goto L20;
        this.tokenWaiting = false;
        if (Boolean.TRUE.equals(r6) == false) goto L13;
        startPlayer();
        return;
    L13:
        if (retryAlternative(r7) == true) goto L19;
        showError("No se pudo renovar el token · " + CdnTokens.prefs(this).getString("error", "Revisa la cuenta en Ajustes"));
        return;
    L19:
        return;
    }

    private /* synthetic */ void lambda$retryToken$39(final int r2, final PlaybackException r3, final Boolean r4, Throwable r5) {
        runOnUiThread(new PlayerActivity$$ExternalSyntheticLambda25(this, r2, r4, r3));
    }

    private /* synthetic */ void lambda$scheduleHealthyReset$34(ExoPlayer r3) {
        this.healthyTask = null;
        if (this.started == true) goto L5;
        return;
    L5:
        if (this.player != r3) goto L14;
        if (r3 != null) goto L8;
        return;
    L8:
        if (r3.getPlaybackState() == 3) goto L10;
        return;
    L10:
        if (r3.isPlaying() == false) goto L17;
        this.reconnect.reset();
        return;
    L17:
        return;
    }

    private /* synthetic */ void lambda$select$19(Entry r1) {
        selectUnlocked(r1);
    }

    private /* synthetic */ void lambda$showEpg$66(List r10, List r11, Entry r12, long[] r13, int[] r14, ArrayAdapter r15) {
        r10.clear();
        r11.clear();
        long r0 = System.currentTimeMillis();
        Iterator<EpgData.Programme> r2 = this.epg.schedule(r12).iterator();
    L4:
        if (r2.hasNext() == false) goto L10;
        EpgData.Programme r3 = r2.next();
        if (r3.stop <= Math.max(r0, r13[r14[0]])) goto L4;
        if (r3.start >= r13[r14[0] + 1]) goto L4;
        r10.add(r3);
        goto L4
    L10:
        SimpleDateFormat r4 = new SimpleDateFormat("EEE d · HH:mm", Locale.getDefault());
        Iterator r5 = r10.iterator();
    L12:
        if (r5.hasNext() == false) goto L19;
        EpgData.Programme r6 = (EpgData.Programme) r5.next();
        StringBuilder r7 = new StringBuilder();
        if (r6.start > r0) goto L16;
        String r8 = "AHORA · ";
    L17:
        r11.add(r7.append(r8).append(r4.format(new Date(r6.start))).append("–").append(clock(r6.stop)).append("\n").append(r6.title).toString());
        goto L12
    L16:
        r8 = "";
        goto L17
    L19:
        if (r11.isEmpty() == true) goto L21;
    L28:
        r15.notifyDataSetChanged();
        return;
    L21:
        if (this.epgLoaded == true) goto L24;
        String r9 = "Cargando tu guía…";
    L27:
        r11.add(r9);
        goto L28
    L24:
        if (this.epg.count() != 0) goto L26;
        r9 = "Carga una guía desde EPG en la pantalla principal";
        goto L27
    L26:
        r9 = "Sin programación disponible para este canal en el día elegido. Actualiza la EPG si es necesario.";
        goto L27
    }

    private /* synthetic */ void lambda$showEpg$67(Entry r1, EpgData.Programme r2, DialogInterface r3, int r4) {
        RecordingUi.programme(this, r1, r2);
    }

    private /* synthetic */ void lambda$showEpg$68(List r5, final Entry r6, AdapterView r7, View r8, int r9, long r10) {
        if (r9 >= r5.size()) goto L10;
        final EpgData.Programme r0 = (EpgData.Programme) r5.get(r9);
        AlertDialog.Builder r1 = new AlertDialog.Builder(this).setTitle(r0.title);
        if (r0.description.isEmpty() == false) goto L7;
        String r2 = "Sin descripción";
    L8:
        AlertDialog r3 = r1.setMessage(r2).setNeutralButton("Programar grabación", new PlayerActivity$$ExternalSyntheticLambda58(this, r6, r0)).setPositiveButton("Cerrar", null).create();
        r3.show();
        styleDialog(r3);
        return;
    L7:
        r2 = r0.description;
        goto L8
    }

    private /* synthetic */ void lambda$showEpg$69(DialogInterface r2) {
        this.epgRender = null;
        this.epgDialog = null;
        immersive();
    }

    private /* synthetic */ void lambda$showFailureDetails$51(String r4, DialogInterface r5, int r6) {
        ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Diagnóstico Chdtv", r4));
        Toast.makeText(this, "Detalle copiado", 0).show();
    }

    private /* synthetic */ void lambda$showFailureDetails$52(DialogInterface r1) {
        immersive();
    }

    private /* synthetic */ void lambda$showPlaybackSettings$41(List r9, ArrayAdapter r10) {
        r9.clear();
        r9.add(this.qualityLabel);
        r9.add(this.audioLabel);
        r9.add(this.subtitleLabel);
        StringBuilder r0 = new StringBuilder().append("Velocidad · ");
        if (this.player != null) goto L5;
    L8:
        String r1 = "Normal";
    L9:
        r9.add(r0.append(r1).toString());
        StringBuilder r2 = new StringBuilder().append("Arranque de directos · ");
        if (getPreferences(0).getBoolean("hls-fast-start", true) == false) goto L12;
        String r3 = "Rápido";
    L13:
        r9.add(r2.append(r3).toString());
        if (this.firstFrameMs < 0) goto L17;
        r9.add(String.format(Locale.getDefault(), "Primera imagen · %.2f s", new Object[]{Double.valueOf(this.firstFrameMs / 1000.0d)}));
    L17:
        if (this.lastPlaybackFailure == null) goto L20;
        r9.add("Detalles del error");
    L20:
        if (PipPlayback.supported(this) == false) goto L22;
        r9.add("Ventana flotante ahora");
        r9.add("Ventana flotante al salir");
    L22:
        r9.add("Grabar este contenido");
        r9.add("Mis grabaciones");
        r9.add("Diagnóstico de arranque");
        r10.notifyDataSetChanged();
        return;
    L12:
        r3 = "Equilibrado";
        goto L13
    L5:
        if (this.player.getPlaybackParameters().speed == 1.0f) goto L8;
        r1 = String.format(Locale.getDefault(), "%.2f×", new Object[]{Float.valueOf(this.player.getPlaybackParameters().speed)});
        goto L9
    }

    private /* synthetic */ void lambda$showPlaybackSettings$42(DialogInterface r4, int r5) {
        boolean r0 = false;
        SharedPreferences.Editor r1 = getPreferences(0).edit();
        if (r5 != 0) goto L5;
        r0 = true;
    L5:
        r1.putBoolean("hls-fast-start", r0).apply();
        r4.dismiss();
    }

    private /* synthetic */ void lambda$showPlaybackSettings$43(String r3, DialogInterface r4, int r5) {
        ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("CHDTV", r3));
    }

    private /* synthetic */ void lambda$showPlaybackSettings$44(List r8, DialogInterface r9, int r10) {
        if (r10 != 0) goto L6;
        showTracks(2);
        return;
    L6:
        if (r10 != 1) goto L9;
        showTracks(1);
        return;
    L9:
        if (r10 != 2) goto L11;
        showTracks(3);
        return;
    L11:
        if (r10 != 3) goto L14;
        showSpeed();
        return;
    L14:
        if (r10 != 4) goto L17;
        new AlertDialog.Builder(this).setTitle("Arranque de directos (próximo canal)").setSingleChoiceItems(new String[]{"Rápido · 0,2 s de datos", "Equilibrado · 1 s de datos"}, 1 ^ (getPreferences(0).getBoolean("hls-fast-start", true) ? 1 : 0), new PlayerActivity$$ExternalSyntheticLambda9(this)).setNegativeButton("Cerrar", null).show();
        return;
    L17:
        if (((String) r8.get(r10)).equals("Grabar este contenido") == false) goto L20;
        RecordingUi.open(this, this.entry);
        return;
    L20:
        if (((String) r8.get(r10)).equals("Mis grabaciones") == false) goto L23;
        startActivity(new Intent(this, RecordingsActivity.class));
        return;
    L23:
        if (((String) r8.get(r10)).equals("Diagnóstico de arranque") == false) goto L26;
        final String r0 = this.startup.report(SystemClock.elapsedRealtime());
        new AlertDialog.Builder(this).setTitle("Diagnóstico de arranque").setMessage(r0).setPositiveButton("Copiar", new PlayerActivity$$ExternalSyntheticLambda10(this, r0)).setNegativeButton("Cerrar", null).show();
        return;
    L26:
        if (((String) r8.get(r10)).equals("Detalles del error") == false) goto L29;
        showFailureDetails();
        return;
    L29:
        if (((String) r8.get(r10)).equals("Ventana flotante ahora") == false) goto L34;
        if (enterFloatingWindow() == true) goto L46;
        Toast.makeText(this, "No se pudo abrir la ventana flotante. Revisa el permiso de imagen en imagen de CHDTV.", 1).show();
        return;
    L46:
        return;
    L34:
        if (((String) r8.get(r10)).equals("Ventana flotante al salir") == false) goto L48;
        PipPlayback.settings(this);
        return;
    }

    private /* synthetic */ void lambda$showPlaybackSettings$45(AlertDialog r2, DialogInterface r3) {
        if (this.settingsDialog != r2) goto L5;
        this.settingsDialog = null;
        this.settingsRender = null;
    L5:
        immersive();
    }

    private /* synthetic */ void lambda$showSpeed$46(ExoPlayer r2, float[] r3, DialogInterface r4, int r5) {
        if (this.player != r2) goto L5;
        this.playbackSpeed = r3[r5];
        r2.setPlaybackSpeed(this.playbackSpeed);
    L5:
        r4.dismiss();
        this.view.showController();
    }

    private /* synthetic */ void lambda$showSpeed$47(AlertDialog r2, DialogInterface r3) {
        if (this.tracksDialog != r2) goto L5;
        this.tracksDialog = null;
    L5:
        immersive();
    }

    private /* synthetic */ void lambda$showTracks$48(ExoPlayer r6, List r7, int r8, List r9, ArrayAdapter r10, ListView r11, TextView r12) {
        if (this.player == r6) goto L5;
        return;
    L5:
        r7.clear();
        r7.addAll(PlaybackTracks.options(r6.getCurrentTracks(), r8));
        r9.clear();
        if (r8 != 3) goto L8;
        String r0 = "Desactivar subtítulos";
    L9:
        r9.add(r0);
        Iterator r1 = r7.iterator();
    L11:
        if (r1.hasNext() == false) goto L13;
        r9.add(((PlaybackTracks.Option) r1.next()).label);
        goto L11
    L13:
        r10.notifyDataSetChanged();
        r11.clearChoices();
        int r2 = PlaybackTracks.checked(r7, r6.getTrackSelectionParameters(), r8);
        if (r2 >= 0) goto L23;
        int r3 = 0;
    L17:
        if (r3 >= r7.size()) goto L23;
        if (((PlaybackTracks.Option) r7.get(r3)).group.isTrackSelected(((PlaybackTracks.Option) r7.get(r3)).index) == true) goto L20;
        r3 = r3 + 1;
        goto L17
    L20:
        r2 = r3 + 1;
    L23:
        if (r2 < 0) goto L26;
        r11.setItemChecked(r2, true);
    L26:
        if (r7.isEmpty() == true) goto L28;
        if (r8 != 2) goto L42;
        String r4 = "Automático adapta la calidad a tu conexión. Solo se muestran resoluciones del contenido.";
    L43:
        r12.setText(r4);
        return;
    L42:
        r4 = "Selecciona una pista disponible en este contenido.";
        goto L43
    L28:
        if (r6.getPlaybackState() != 2) goto L30;
    L39:
        r4 = "Buscando pistas del contenido…";
        goto L43
    L30:
        if (r6.getPlaybackState() == 1) goto L39;
        StringBuilder r5 = new StringBuilder().append("Este contenido no ofrece pistas de ");
        if (r8 != 2) goto L35;
        String r13 = "vídeo";
    L38:
        r4 = r5.append(r13).toString();
        goto L43
    L35:
        if (r8 != 1) goto L37;
        r13 = MimeTypes.BASE_TYPE_AUDIO;
        goto L38
    L37:
        r13 = "subtítulos";
        goto L38
    L8:
        r0 = "Automático";
        goto L9
    }

    private /* synthetic */ void lambda$showTracks$49(ExoPlayer r3, List r4, int r5, AlertDialog r6, AdapterView r7, View r8, int r9, long r10) {
        if (this.player == r3) goto L5;
        return;
    L5:
        if (r9 > r4.size()) goto L17;
        if (r9 != 0) goto L9;
        PlaybackTracks.Option r0 = null;
    L10:
        if (r0 != null) goto L12;
    L14:
        r3.setTrackSelectionParameters(PlaybackTracks.apply(r3.getTrackSelectionParameters(), r5, r0));
        this.trackParameters = r3.getTrackSelectionParameters();
        r6.dismiss();
        this.view.showController();
        return;
    L12:
        if (r0.supported == true) goto L14;
        return;
    L9:
        r0 = (PlaybackTracks.Option) r4.get(r9 - 1);
        goto L10
    }

    private /* synthetic */ void lambda$showTracks$50(AlertDialog r2, DialogInterface r3) {
        if (this.tracksDialog != r2) goto L5;
        this.tracksRender = null;
        this.tracksDialog = null;
    L5:
        immersive();
    }

    private /* synthetic */ void lambda$startPlayer$21(int r5, boolean r6, Boolean r7, Entry r8) {
        if (r5 == this.tokenTicket) goto L5;
        return;
    L5:
        if (this.started == true) goto L7;
        return;
    L7:
        if (isDestroyed() == true) goto L23;
        this.tokenWaiting = false;
        this.tokenScoped = r6;
        if (r6 == true) goto L12;
    L19:
        startPlayer();
        return;
    L12:
        if (Boolean.TRUE.equals(r7) == true) goto L19;
        if (CdnTokens.current(this, r8).isEmpty() == false) goto L19;
        if (CdnTokens.valid(CdnTokens.originalToken(r8), System.currentTimeMillis()) == true) goto L19;
        showError("No se pudo renovar Movistar · " + CdnTokens.prefs(this).getString("error", "Revisa la cuenta en Ajustes"));
        return;
    }

    private /* synthetic */ void lambda$startPlayer$22(final int r7, final boolean r8, final Entry r9, final Boolean r10, Throwable r11) {
        runOnUiThread(new PlayerActivity$$ExternalSyntheticLambda49(this, r7, r8, r10, r9));
    }

    private /* synthetic */ void lambda$startPlayer$23(final Entry r9, final int r10) {
        long r0 = SystemClock.elapsedRealtime();
        BlackBox.event("TOKEN_SCOPE_START", "");
        if (CdnTokens.scoped(getApplicationContext(), r9) == true) goto L5;
    L7:
        final boolean r2 = false;
    L8:
        BlackBox.event("TOKEN_SCOPE_END", "duration_ms=" + (SystemClock.elapsedRealtime() - r0));
        if (r2 == true) goto L11;
    L15:
        if (r2 == false) goto L17;
        CdnTokens.Pending r4 = CdnTokens.forPlayback(getApplicationContext());
    L18:
        r4.whenComplete(new PlayerActivity$$ExternalSyntheticLambda60(this, r10, r2, r9));
        return;
    L17:
        r4 = CdnTokens.Pending.completed(true);
        goto L18
    L11:
        if (CdnTokens.current(getApplicationContext(), r9).isEmpty() == false) goto L15;
        if (CdnTokens.valid(CdnTokens.originalToken(r9), System.currentTimeMillis()) == false) goto L15;
        goto L15
    L5:
        if (CdnTokens.allowedHost(Entry.details(r9).url) == false) goto L7;
        r2 = true;
        goto L8
    }

    private /* synthetic */ void lambda$startPlayer$24(int r2, JSONObject r3) {
        if (r2 == this.tokenTicket) goto L5;
        return;
    L5:
        if (this.started == true) goto L7;
        return;
    L7:
        if (isDestroyed() == true) goto L16;
        boolean r0 = false;
        this.tokenWaiting = false;
        if (r3 == null) goto L12;
        r0 = true;
    L12:
        this.daznScoped = r0;
        startPlayer();
        return;
    }

    private /* synthetic */ void lambda$startPlayer$25(final int r2, final JSONObject r3, Boolean r4, Throwable r5) {
        runOnUiThread(new PlayerActivity$$ExternalSyntheticLambda68(this, r2, r3));
    }

    private /* synthetic */ void lambda$startPlayer$26(Entry r17, final int r18) {
        final JSONObject r3 = DaznTokens.match(getApplicationContext(), r17);
        boolean r4 = false;
        if (r3 == null) goto L20;
        JSONObject r7 = r3.optJSONObject("current");
        if (r7 == null) goto L20;
        long r10 = r7.optLong("expires", 0);
        long r12 = DaznTokenClient.payload(r7.optString("token")).optLong("exp", 0) * 1000;     // Catch: Exception -> L12
        if (r10 != 0) goto L10;
        long r8 = r12;
    L11:
        r10 = r8;
        goto L14
    L10:
        r8 = Math.min(r10, r12);     // Catch: Exception -> L12
    L14:
        if (r7.optString("token").isEmpty() == false) goto L16;
    L18:
        boolean r0 = false;
    L19:
        r4 = r0;
        goto L20
    L16:
        if (r10 <= (System.currentTimeMillis() + 30000)) goto L18;
        r0 = true;
    L20:
        if (r3 != null) goto L22;
        CdnTokens.Pending r1 = CdnTokens.Pending.completed(true);
    L23:
        if (r4 == false) goto L25;
        r1 = CdnTokens.Pending.completed(true);
    L25:
        r1.whenComplete(new PlayerActivity$$ExternalSyntheticLambda12(this, r18, r3));
        return;
    L22:
        r1 = DaznTokens.renew(getApplicationContext(), false, r17);
        goto L23
    }

    private /* synthetic */ DataSpec lambda$startPlayer$27(DataSpec r4) throws IOException {
        String r0 = CdnTokens.current(this, this.entry);
        if (r0.isEmpty() == false) goto L6;
        r0 = CdnTokens.originalToken(this.entry);
    L6:
        if (r0.isEmpty() == false) goto L8;
    L11:
        return r4;
    L8:
        if (CdnTokens.allowedHost(r4.uri.toString()) == false) goto L11;
        return r4.withUri(Uri.parse(MovistarStreams.withToken(r4.uri.toString(), r0))).withRequestHeaders(CdnTokens.headers(r4.httpRequestHeaders, r0));
    }

    private /* synthetic */ DataSpec lambda$startPlayer$28(DataSpec r2) throws IOException {
        return DaznTokens.resolve(this, this.entry, r2);
    }

    static /* synthetic */ DrmSessionManager lambda$startPlayer$29(DefaultDrmSessionManager r0, MediaItem r1) {
        return r0;
    }

    static /* synthetic */ DrmSessionManager lambda$startPlayer$30(DefaultDrmSessionManager r0, MediaItem r1) {
        return r0;
    }

    static /* synthetic */ DrmSessionManager lambda$startPlayer$31(DefaultDrmSessionManager r0, MediaItem r1) {
        return r0;
    }

    static /* synthetic */ int lambda$startPlayer$32(MediaCodecInfo r2, MediaCodecInfo r3) {
        return Boolean.compare(r3.hardwareAccelerated, r2.hardwareAccelerated);
    }

    private /* synthetic */ List lambda$startPlayer$33(String r3, boolean r4, boolean r5) throws MediaCodecUtil.DecoderQueryException {
        ArrayList r0 = new ArrayList(MediaCodecUtil.getDecoderInfos(r3, r4, r5));
        Iterator r6 = r0.iterator();
    L4:
        if (r6.hasNext() == false) goto L9;
        if (this.failedDecoders.contains(((MediaCodecInfo) r6.next()).name) == false) goto L4;
        r6.remove();
        goto L4
    L9:
        if (this.preferSoftwareAacForVod == true) goto L11;
    L15:
        boolean r7 = false;
    L16:
        orderCodecs(r0, r7);
        return r0;
    L11:
        if (isVodEntry() == false) goto L15;
        if (isVodAudioFallbackMime(r3) == false) goto L15;
        r7 = true;
        goto L16
    }

    private void loadEpgOnce() {
        if ("Canales".equals(this.entry.kind) == true) goto L5;
        return;
    L5:
        if (this.epgRequested == false) goto L7;
        return;
    L7:
        if (isDestroyed() == false) goto L9;
        return;
    L9:
        if (this.worker.isShutdown() == true) goto L16;
        this.epgRequested = true;
        this.worker.execute(new PlayerActivity$$ExternalSyntheticLambda53(this));
        return;
    }

    private void loadPlaybackCatalog() {
        if (this.playbackCatalogRequested == false) goto L5;
        return;
    L5:
        if (isDestroyed() == false) goto L7;
        return;
    L7:
        if (this.worker.isShutdown() == true) goto L13;
        this.playbackCatalogRequested = true;
        final Entry r0 = this.entry;
        this.worker.execute(new PlayerActivity$$ExternalSyntheticLambda54(this, r0));
        return;
    }

    private static void orderCodecs(List<MediaCodecInfo> r5, boolean r6) {
        ArrayList r0 = new ArrayList();
        ArrayList r1 = new ArrayList();
        Iterator<MediaCodecInfo> r2 = r5.iterator();
    L4:
        if (r2.hasNext() == false) goto L10;
        MediaCodecInfo r3 = r2.next();
        if (r3.hardwareAccelerated != r6) goto L7;
        r1.add(r3);
        goto L4
    L7:
        r0.add(r3);
        goto L4
    L10:
        r5.clear();
        r5.addAll(r0);
        r5.addAll(r1);
    }

    private void placeSettings(AlertDialog r11, int r12, int r13) {
        if (this.root.getWidth() <= 0) goto L5;
        int r0 = this.root.getWidth();
    L7:
        if (this.root.getHeight() <= 0) goto L9;
        int r1 = this.root.getHeight();
    L10:
        int[] r2 = new int[2];
        this.settingsButton.getLocationInWindow(r2);
        if (r2[1] <= 0) goto L13;
        int r3 = Math.max(m404dp(12), (r1 - r2[1]) + m404dp(6));
    L14:
        int r4 = Math.min(r3, Math.max(m404dp(12), r1 / 3));
        int r5 = m404dp(12);
        if (Build.VERSION.SDK_INT >= 28) goto L17;
    L21:
        Window r6 = r11.getWindow();
        r6.setGravity(85);
        WindowManager.LayoutParams r7 = r6.getAttributes();
        r7.x = r5;
        r7.y = r4;
        r6.setAttributes(r7);
        r6.setLayout(Math.min(r12, (r0 - r5) - m404dp(12)), Math.min(r13, (r1 - r4) - m404dp(12)));
        return;
    L17:
        if (this.root.getRootWindowInsets() == null) goto L21;
        if (this.root.getRootWindowInsets().getDisplayCutout() == null) goto L21;
        r5 = r5 + this.root.getRootWindowInsets().getDisplayCutout().getSafeInsetRight();
        goto L21
    L13:
        r3 = m404dp(64);
        goto L14
    L9:
        r1 = getResources().getDisplayMetrics().heightPixels;
        goto L10
    L5:
        r0 = getResources().getDisplayMetrics().widthPixels;
        goto L7
    }

    private void primeTvAccount() {
        tvSkipHlsToken();
        if (this.entry != null) goto L5;
        return;
    L5:
        if (MimeTypes.APPLICATION_MPD.equals(StreamFormat.mime(Entry.details(this.entry))) == true) goto L7;
        return;
    L7:
        if ("Canales".equals(this.entry.kind) == false) goto L9;
    L13:
        long r13 = SystemClock.elapsedRealtime();
        boolean r4 = this.screenDrawn;
        boolean r5 = this.started;
        if (this.player == null) goto L16;
        boolean r6 = true;
    L18:
        if (TvPlayback.canPrime(true, r4, r5, r6, this.tokenPreflight, this.tokenWaiting, this.reconnectWaiting, this.rateNoAuto, this.rateDeadline, r13, TimedAccess.allowed(this)) == true) goto L21;
        return;
    L21:
        if (CdnTokens.allowedHost(Entry.details(this.entry).url) == true) goto L23;
        return;
    L23:
        if (CdnTokens.hasAccount(this) == false) goto L35;
        this.tokenPreflight = true;
        this.tokenWaiting = true;
        int r1 = this.tokenTicket + 1;
        this.tokenTicket = r1;
        if ("Canales".equals(this.entry.kind) == false) goto L28;
        String r2 = "TV_TOKEN_EARLY";
    L29:
        BlackBox.event(r2, "before_first_draw=true");
        this.tokenWorker.execute(new TvPlayback.Scope(this, this.entry, r1));
        return;
    L28:
        r2 = "VOD_TOKEN_EARLY";
        goto L29
    L35:
        return;
    L16:
        r6 = false;
        goto L18
    L9:
        if ("Películas".equals(this.entry.kind) == true) goto L13;
        if ("Series".equals(this.entry.kind) == true) goto L13;
    }

    private String programmeSummary(Entry r7) {
        EpgData.Programme[] r0 = this.epg.nowNext(r7, System.currentTimeMillis());
        StringBuilder r1 = new StringBuilder();
        if (r0[0] != null) goto L5;
        String r2 = "Ahora · Sin programación";
    L6:
        StringBuilder r3 = r1.append(r2).append("\n");
        if (r0[1] != null) goto L9;
        String r4 = "Después · Sin programación";
    L11:
        return r3.append(r4).toString();
    L9:
        r4 = clock(r0[1].start) + " · " + r0[1].title;
        goto L11
    L5:
        r2 = "Ahora · " + r0[0].title;
        goto L6
    }

    private String reason222(String r2) {
        if (this.lastPlaybackFailure != null) goto L6;
        return r2;
    L6:
        return PlaybackReason222.summary(this.lastPlaybackFailure);
    }

    private void rebuild() {
        final int r0 = this.sequenceTicket + 1;
        this.sequenceTicket = r0;
        if (this.sequence != null) goto L5;
        PlaybackSequence r1 = null;
    L6:
        if (r1 == null) goto L9;
        this.sequence = r1;
        updateButtons();
        return;
    L9:
        final Entry r4 = this.entry;
        final List<Entry> r3 = this.catalog;
        final Map<Entry, String> r5 = this.providers;
        this.sequence = new PlaybackSequence(Collections.emptyList(), r4);
        updateButtons();
        this.worker.execute(new PlayerActivity$$ExternalSyntheticLambda69(this, r3, r4, r5, r0));
        return;
    L5:
        r1 = this.sequence.selectExisting(this.entry);
        goto L6
    }

    private void refreshEpg() {
        this.main.removeCallbacks(this.epgTick);
        this.epgTick.run();
    }

    private void release() {
        ChannelFallbackWork.cancel(this);
        TvManifestWarm.release(this);
        TvHlsOverlap.cancel(this);
        if (this.player != null) goto L5;
        return;
    L5:
        this.trackParameters = this.player.getTrackSelectionParameters();
        this.playbackSpeed = this.player.getPlaybackParameters().speed;
        this.position = this.player.getCurrentPosition();
        saveHistory();
        if (this.player.getPlaybackState() != 4) goto L8;
        this.position = 0;
    L8:
        this.view.setPlayer(null);
        this.player.release();
        this.player = null;
        updateFloatingParams();
    }

    private long resume(Entry r3) {
        return WatchHistory.resume(this, r3);
    }

    private boolean retryAlternative(PlaybackException r10) {
        Throwable r0 = r10;
    L3:
        int r2 = 0;
        if (r0 == null) goto L9;
        if ((r0 instanceof ClearKeyHls.UnsupportedEncryptionException) == true) goto L7;
        r0 = r0.getCause();
        goto L3
    L7:
        boolean r1 = true;
    L10:
        HttpDataSource.InvalidResponseCodeException r3 = HttpRetry.response(r10);
        if (this.started == true) goto L13;
    L34:
        return false;
    L13:
        if ("Canales".equals(this.entry.kind) == false) goto L34;
        if (r1 == true) goto L22;
        if (r10.getErrorCodeName().startsWith("ERROR_CODE_DRM") == true) goto L34;
        if (r3 == null) goto L34;
        if (r3.responseCode != 403) goto L34;
    L22:
        if (this.fallbackSwitching == false) goto L24;
        return true;
    L24:
        this.lastPlaybackFailure = r10;
        if (this.loaded == true) goto L28;
        loadPlaybackCatalog();
        this.pendingFallbackFailure = r10;
        this.status.setText(this.entry.title + " · Buscando otra fuente…");
        return true;
    L28:
        this.fallbackSwitching = true;
        this.status.setText(this.entry.title + " · Buscando otra fuente del mismo canal…");
        StringBuilder r4 = new StringBuilder().append("format=").append(StreamFormat.label(this.entry)).append(" http=");
        if (r3 == null) goto L32;
        r2 = r3.responseCode;
    L32:
        BlackBox.event("CHANNEL_FALLBACK_REQUEST", r4.append(r2).toString());
        ChannelFallbackWork.submit(this, this.fallbackCatalog, this.fallback, this.entry, this.tokenTicket, r10);
        return true;
    L9:
        r1 = false;
        goto L10
    }

    private boolean retryDazn(final PlaybackException r7) {
        HttpDataSource.InvalidResponseCodeException r0 = HttpRetry.response(r7);
        if (this.started == true) goto L5;
        return false;
    L5:
        if (this.tokenRetryUsed == false) goto L7;
        return false;
    L7:
        if (r7.getErrorCodeName().startsWith("ERROR_CODE_DRM") == false) goto L9;
        return false;
    L9:
        if (DaznTokens.match(this, this.entry) == null) goto L24;
        if (r0 != null) goto L12;
        return false;
    L12:
        if (r0.responseCode != 401) goto L14;
    L18:
        this.tokenRetryUsed = true;
        release();
        this.tokenWaiting = true;
        final int r2 = this.tokenTicket + 1;
        this.tokenTicket = r2;
        this.status.setText(this.entry.title + " · Renovando token DAZN…");
        DaznTokens.renew(this, true, this.entry).whenComplete(new PlayerActivity$$ExternalSyntheticLambda32(this, r2, r7));
        return true;
    L14:
        if (r0.responseCode == 403) goto L18;
        if (r0.responseCode == 418) goto L18;
        return false;
    L24:
        return false;
    }

    private boolean retryDecoder(PlaybackException r13) {
        if (this.started == true) goto L5;
    L60:
        return false;
    L5:
        if (this.player == null) goto L60;
        if ((r13 instanceof ExoPlaybackException) == false) goto L60;
        ExoPlaybackException r3 = (ExoPlaybackException) r13;
        Throwable r14 = r13;
        if (r3.rendererFormat != null) goto L13;
        return false;
    L13:
        if (r14 == null) goto L18;
        if ((r14 instanceof MediaCodecDecoderException) == true) goto L16;
        r14 = r14.getCause();
        goto L13
    L16:
        MediaCodecInfo r15 = ((MediaCodecDecoderException) r14).codecInfo;
    L19:
        if (r15 != null) goto L21;
    L59:
        return false;
    L21:
        if (this.failedDecoders.contains(r15.name) == true) goto L59;
        this.failedDecoders.add(r15.name);
        String r5 = r3.rendererFormat.sampleMimeType;
        if (isVodEntry() == true) goto L26;
    L34:
        boolean r6 = false;
    L61:
        ArrayList r8 = new ArrayList(MediaCodecUtil.getDecoderInfos(r5, r15.secure, r15.tunneling));     // Catch: MediaCodecUtil.DecoderQueryException -> L55
        if (r6 == false) goto L38;
        orderCodecs(r8, true);     // Catch: MediaCodecUtil.DecoderQueryException -> L55
    L38:
        Iterator r9 = r8.iterator();     // Catch: MediaCodecUtil.DecoderQueryException -> L55
    L40:
        if (r9.hasNext() == false) goto L52;
        MediaCodecInfo r10 = (MediaCodecInfo) r9.next();     // Catch: MediaCodecUtil.DecoderQueryException -> L55
        if (this.failedDecoders.contains(r10.name) == true) goto L40;
        if (r10.isFormatSupported(r3.rendererFormat) == false) goto L40;
        if (r6 == true) goto L47;
    L49:
        release();     // Catch: MediaCodecUtil.DecoderQueryException -> L55
        startPlayer();     // Catch: MediaCodecUtil.DecoderQueryException -> L55
        return true;
    L47:
        if (r10.hardwareAccelerated == true) goto L49;
        this.preferSoftwareAacForVod = true;     // Catch: MediaCodecUtil.DecoderQueryException -> L55
        BlackBox.event("VOD_AUDIO_CODEC_FALLBACK", "mime=" + r5 + " failed_decoder=" + r15.name + " selected_decoder=" + r10.name + " reason=hardware_decoder_error");     // Catch: MediaCodecUtil.DecoderQueryException -> L55
        goto L49
    L52:
        if (r6 == false) goto L58;
        BlackBox.event("VOD_AUDIO_CODEC_FALLBACK_UNAVAILABLE", "mime=" + r5 + " failed_decoder=" + r15.name + " reason=no_compatible_alternative");     // Catch: MediaCodecUtil.DecoderQueryException -> L55
    L58:
        return false;
    L56:
        if (r6 == false) goto L58;
        BlackBox.event("VOD_AUDIO_CODEC_FALLBACK_UNAVAILABLE", "mime=" + r5 + " failed_decoder=" + r15.name + " reason=decoder_query_failed");
        goto L58
    L26:
        if (isVodAudioFallbackMime(r5) == false) goto L34;
        if (r15.hardwareAccelerated == false) goto L34;
        if (r15.secure == true) goto L34;
        if (r15.tunneling == true) goto L34;
        r6 = true;
        goto L61
    L18:
        r15 = null;
        goto L19
    }

    private boolean retryLive(PlaybackException r11) {
        if (this.started == true) goto L5;
    L19:
        return false;
    L5:
        if (isFinishing() == true) goto L19;
        if (isDestroyed() == true) goto L19;
        if (TimedAccess.allowed(this) == false) goto L19;
        if (this.reconnectWaiting == false) goto L14;
        return true;
    L14:
        long r3 = this.reconnect.next(this.entry, r11);
        if (r3 >= 0) goto L17;
        return false;
    L17:
        this.lastPlaybackFailure = r11;
        final Entry r0 = this.entry;
        release();
        cancelHttpRetry();
        cancelHealthyReset();
        final int r5 = this.tokenTicket + 1;
        this.tokenTicket = r5;
        this.tokenWaiting = false;
        this.tokenPreflight = false;
        this.daznPreflight = false;
        this.reconnectWaiting = true;
        this.playbackError = true;
        this.status.setTextColor(AppTheme.color(this, -16538));
        this.status.setText(this.entry.title + " · Reconectando " + this.reconnect.attempts() + "/3 en " + (r3 / 1000) + " s…");
        this.view.setControllerShowTimeoutMs(0);
        this.view.showController();
        Log.i("CHDTV", "Reconexión de directo " + this.reconnect.attempts() + "/3; error=" + r11.errorCode);
        this.reconnectTask = new PlayerActivity$$ExternalSyntheticLambda64(this, r5, r0);
        this.main.postDelayed(this.reconnectTask, r3);
        return true;
    }

    private boolean retryRateLimit(PlaybackException r7) {
        HttpDataSource.InvalidResponseCodeException r0 = HttpRetry.response(r7);
        if (this.started == true) goto L5;
        return false;
    L5:
        if (this.rateRetryUsed == true) goto L18;
        if (r0 != null) goto L8;
        return false;
    L8:
        if (r0.responseCode != 429) goto L20;
        this.rateRetryUsed = true;
        this.lastPlaybackFailure = r7;
        long r2 = HttpRetry.delay(r0.headerFields, System.currentTimeMillis());
        release();
        if (r2 <= 86400000) goto L14;
        this.rateNoAuto = true;
        showError("HTTP 429 · El servidor exige una espera superior a 24 horas. No se reintentará automáticamente");
        return true;
    L14:
        this.rateDeadline = SystemClock.elapsedRealtime() + r2;
        waitForRateLimit();
        return true;
    L20:
        return false;
    L18:
        return false;
    }

    private boolean retryToken(final PlaybackException r6) {
        if (this.started == true) goto L5;
        return false;
    L5:
        if (r6.getErrorCodeName().startsWith("ERROR_CODE_DRM") == false) goto L7;
        return false;
    L7:
        if (this.tokenScoped == true) goto L9;
        return false;
    L9:
        if (this.tokenRetryUsed == false) goto L11;
        return false;
    L11:
        if (CdnTokens.rejected(r6) == false) goto L20;
        this.tokenRetryUsed = true;
        release();
        this.tokenWaiting = true;
        final int r1 = this.tokenTicket + 1;
        this.tokenTicket = r1;
        this.status.setText(this.entry.title + " · Renovando token y reintentando…");
        CdnTokens.renew(this, true).whenComplete(new PlayerActivity$$ExternalSyntheticLambda27(this, r1, r6));
        return true;
    L20:
        return false;
    }

    private void saveHistory() {
        if (this.player == null) goto L12;
        boolean r1 = true;
        if (this.player.getPlaybackState() == 1) goto L13;
        Entry r3 = this.entry;
        long r4 = this.player.getCurrentPosition();
        long r6 = this.player.getDuration();
        if (this.player.getPlaybackState() == 4) goto L10;
        r1 = false;
    L10:
        WatchHistory.save(this, r3, r4, r6, r1);
        return;
    L13:
        return;
    }

    private void scheduleHealthyReset() {
        cancelHealthyReset();
        final ExoPlayer r0 = this.player;
        this.healthyTask = new PlayerActivity$$ExternalSyntheticLambda66(this, r0);
        this.main.postDelayed(this.healthyTask, 60000);
    }

    private void select(final Entry r5) {
        this.startup.requested(SystemClock.elapsedRealtime());
        cancelReconnect(true);
        this.daznPreflight = false;
        this.fallbackSwitching = false;
        this.fallback = new ChannelFallback(r5);
        this.fallbackList = "";
        this.pendingFallbackFailure = null;
        cancelHttpRetry();
        this.rateRetryUsed = false;
        this.rateNoAuto = false;
        this.rateDeadline = 0;
        this.tokenTicket++;
        this.tokenWaiting = false;
        this.tokenPreflight = false;
        this.tokenRetryUsed = false;
        release();
        ParentalControl.requireEntry(this, r5, new PlayerActivity$$ExternalSyntheticLambda65(this, r5));
    }

    private void selectUnlocked(Entry r3) {
        this.failedDecoders.clear();
        this.preferSoftwareAacForVod = false;
        if (this.settingsDialog == null) goto L6;
        this.settingsDialog.dismiss();
    L6:
        if (this.tracksDialog == null) goto L9;
        this.tracksDialog.dismiss();
    L9:
        if (PlaybackSequence.same(this.entry, r3) == true) goto L11;
        this.trackParameters = null;
    L11:
        this.entry = r3;
        this.startupAt = SystemClock.elapsedRealtime();
        this.firstFrameMs = -1;
        this.position = resume(r3);
        rebuild();
        if (this.started == false) goto L15;
        startPlayer();
        return;
    }

    private GradientDrawable shape(int r4, int r5) {
        GradientDrawable r0 = new GradientDrawable();
        r0.setColor(AppTheme.color(this, r4));
        r0.setCornerRadius(m404dp(12));
        r0.setStroke(m404dp(1), AppTheme.color(this, r5));
        return r0;
    }

    private void showEpg() {
        loadEpgOnce();
        if (this.epgDialog != null) goto L5;
    L7:
        final Entry r4 = this.entry;
        Calendar r8 = Calendar.getInstance();
        r8.set(11, 0);
        r8.set(12, 0);
        r8.set(13, 0);
        r8.set(14, 0);
        final long[] r5 = new long[8];
        ArrayList r2 = new ArrayList();
        SimpleDateFormat r3 = new SimpleDateFormat("EEE d MMM", Locale.getDefault());
        int r6 = 0;
    L9:
        if (r6 >= 8) goto L18;
        r5[r6] = r8.getTimeInMillis();
        if (r6 >= 7) goto L17;
        StringBuilder r7 = new StringBuilder();
        if (r6 != 0) goto L15;
        String r9 = "Hoy · ";
    L16:
        r2.add(r7.append(r9).append(r3.format(r8.getTime())).toString());
        goto L17
    L15:
        r9 = "";
    L17:
        r8.add(5, 1);
        r6 = r6 + 1;
        goto L9
    L18:
        final int[] r10 = {0};
        LinearLayout r0 = new LinearLayout(this);
        r0.setOrientation(1);
        Spinner r1 = new Spinner(this);
        r1.setAdapter(guideAdapter(r2));
        r0.addView(r1, new LinearLayout.LayoutParams(-1, m404dp(48)));
        final ArrayList r11 = new ArrayList();
        final ArrayList r12 = new ArrayList();
        ListView r13 = new ListView(this);
        final ArrayAdapter<String> r14 = guideAdapter(r12);
        r13.setAdapter(r14);
        r13.setDivider(new ColorDrawable(1345494235));
        r13.setDividerHeight(m404dp(1));
        this.epgRender = new PlayerActivity$$ExternalSyntheticLambda22(this, r11, r12, r4, r5, r10, r14);
        this.epgRender.run();
        r1.setOnItemSelectedListener(new C101414(this, r10));
        r0.addView(r13, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        r13.setOnItemClickListener(new PlayerActivity$$ExternalSyntheticLambda33(this, r11, r4));
        this.epgDialog = new AlertDialog.Builder(this).setTitle(r4.title + " · EPG · 7 días").setView(r0).setNegativeButton("Cerrar", null).create();
        this.epgDialog.setOnDismissListener(new PlayerActivity$$ExternalSyntheticLambda44(this));
        this.epgDialog.show();
        styleDialog(this.epgDialog);
        this.epgDialog.getWindow().setLayout((int) (((double) getResources().getDisplayMetrics().widthPixels) * 0.85d), (int) (((double) getResources().getDisplayMetrics().heightPixels) * 0.8d));
        return;
    L5:
        if (this.epgDialog.isShowing() == false) goto L7;
    }

    private void showError(String r4) {
        String r5 = reason222(r4);
        this.playbackError = true;
        this.status.setTextColor(AppTheme.color(this, -36480));
        this.status.setText(this.entry.title + " · " + r5);
        int r1 = 0;
        this.view.setControllerShowTimeoutMs(0);
        this.view.showController();
        LinearLayout r0 = this.controls;
        if (inFloatingWindow() == false) goto L5;
        r1 = 8;
    L5:
        r0.setVisibility(r1);
        if (this.settingsRender == null) goto L9;
        this.settingsRender.run();
        return;
    }

    private void showFailureDetails() {
        if (this.lastPlaybackFailure != null) goto L5;
        return;
    L5:
        final String r0 = PlaybackFailure.details(this.lastPlaybackFailure);
        TextView r1 = new TextView(this);
        r1.setText(r0);
        r1.setTextColor(AppTheme.color(this, -1903617));
        r1.setTextSize(13.0f);
        r1.setTextIsSelectable(true);
        r1.setPadding(m404dp(16), m404dp(12), m404dp(16), m404dp(12));
        ScrollView r2 = new ScrollView(this);
        r2.addView(r1);
        AlertDialog r3 = new AlertDialog.Builder(this).setTitle("Detalle de reproducción").setView(r2).setPositiveButton("Copiar", new PlayerActivity$$ExternalSyntheticLambda56(this, r0)).setNegativeButton("Cerrar", null).create();
        r3.setOnDismissListener(new PlayerActivity$$ExternalSyntheticLambda57(this));
        r3.show();
        styleDialog(r3);
        placeSettings(r3, m404dp(520), m404dp(400));
    }

    private void showPlaybackSettings() {
        if (this.player != null) goto L6;
        return;
    L6:
        if (this.settingsDialog != null) goto L8;
    L10:
        updateTracks();
        final ArrayList r0 = new ArrayList();
        final int r2 = android.R.layout.simple_list_item_1;
        final ArrayAdapter<String> r1 = new C10207(this, this, r2, r0);
        this.settingsRender = new PlayerActivity$$ExternalSyntheticLambda35(this, r0, r1);
        this.settingsRender.run();
        final AlertDialog r3 = new AlertDialog.Builder(this).setAdapter(r1, new PlayerActivity$$ExternalSyntheticLambda36(this, r0)).create();
        this.settingsDialog = r3;
        r3.setOnDismissListener(new PlayerActivity$$ExternalSyntheticLambda37(this, r3));
        r3.show();
        styleDialog(r3);
        int r4 = m404dp(340);
        if (this.lastPlaybackFailure != null) goto L13;
        int r5 = 360;
    L14:
        placeSettings(r3, r4, m404dp(r5));
        return;
    L13:
        r5 = 420;
        goto L14
    L8:
        if (this.settingsDialog.isShowing() == false) goto L10;
    }

    private void showSpeed() {
        if (this.player != null) goto L5;
        return;
    L5:
        final float[] r0 = {0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f};
        String[] r1 = {"0,5×", "0,75×", "Normal", "1,25×", "1,5×", "2×"};
        int r2 = 2;
        int r3 = 0;
    L7:
        if (r3 >= r0.length) goto L12;
        if (this.player.getPlaybackParameters().speed != r0[r3]) goto L11;
        r2 = r3;
    L11:
        r3 = r3 + 1;
        goto L7
    L12:
        final ExoPlayer r4 = this.player;
        this.tracksRender = null;
        final AlertDialog r5 = new AlertDialog.Builder(this).setTitle("Velocidad").setSingleChoiceItems(r1, r2, new PlayerActivity$$ExternalSyntheticLambda28(this, r4, r0)).setNegativeButton("Cerrar", null).create();
        this.tracksDialog = r5;
        r5.setOnDismissListener(new PlayerActivity$$ExternalSyntheticLambda29(this, r5));
        r5.show();
        styleDialog(r5);
        placeSettings(r5, m404dp(300), m404dp(360));
    }

    private void showTracks(final int r14) {
        if (this.player != null) goto L7;
        Toast.makeText(this, "El reproductor todavía no está preparado", 0).show();
        return;
    L7:
        if (this.tracksDialog != null) goto L9;
    L11:
        final ExoPlayer r4 = this.player;
        final ArrayList r5 = new ArrayList();
        final ArrayList r7 = new ArrayList();
        final ListView r9 = new ListView(this);
        r9.setChoiceMode(1);
        r9.setDivider(new ColorDrawable(1345494235));
        r9.setDividerHeight(m404dp(1));
        final int r10 = android.R.layout.simple_list_item_single_choice;
        final ArrayAdapter<String> r6 = new C10218(this, this, r10, r7, r5);
        r9.setAdapter(r6);
        final TextView r11 = new TextView(this);
        r11.setTextColor(AppTheme.color(this, -7223586));
        r11.setTextSize(12.0f);
        r11.setPadding(m404dp(16), m404dp(8), m404dp(16), m404dp(8));
        LinearLayout r2 = new LinearLayout(this);
        r2.setOrientation(1);
        r2.addView(r11);
        r2.addView(r9, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        this.tracksRender = new PlayerActivity$$ExternalSyntheticLambda50(this, r4, r5, r14, r7, r6, r9, r11);
        this.tracksRender.run();
        if (r14 != 2) goto L14;
        String r0 = "Calidad / Resolución";
    L17:
        final AlertDialog r8 = new AlertDialog.Builder(this).setTitle(r0).setView(r2).setNegativeButton("Cerrar", null).create();
        this.tracksDialog = r8;
        r9.setOnItemClickListener(new PlayerActivity$$ExternalSyntheticLambda51(this, r4, r5, r14, r8));
        r8.setOnDismissListener(new PlayerActivity$$ExternalSyntheticLambda52(this, r8));
        r8.show();
        styleDialog(r8);
        placeSettings(r8, m404dp(380), m404dp(360));
        return;
    L14:
        if (r14 != 1) goto L16;
        r0 = "Audio / Idioma";
        goto L17
    L16:
        r0 = "Subtítulos";
        goto L17
    L9:
        if (this.tracksDialog.isShowing() == false) goto L11;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v3 */
    /* JADX WARN: Type inference failed for: r0v4, types: [androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy] */
    /* JADX WARN: Type inference failed for: r0v40 */
    /* JADX WARN: Type inference failed for: r0v58 */
    /* JADX WARN: Type inference failed for: r0v63 */
    /* JADX WARN: Type inference failed for: r16v0 */
    /* JADX WARN: Type inference failed for: r4v15, types: [androidx.media3.exoplayer.hls.HlsMediaSource$Factory] */
    /* JADX WARN: Type inference failed for: r4v37, types: [es.antonio.m3u.PlayerActivity$3] */
    /* JADX WARN: Type inference failed for: r4v6, types: [androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy, es.antonio.m3u.PlayerActivity$3] */
    /* JADX WARN: Type inference failed for: r5v0, types: [es.antonio.m3u.DemandMediaSourceFactory] */
    /* JADX WARN: Type inference failed for: r6v19, types: [androidx.media3.exoplayer.hls.HlsMediaSource$Factory] */
    private void startPlayer() {
        BackgroundWork.playback();
        tvSkipHlsToken();
        if (TimedAccess.allowed(this) == true) goto L7;
        finish();
        return;
    L7:
        if (this.player == null) goto L9;
        return;
    L9:
        if (this.entry != null) goto L11;
        return;
    L11:
        if (this.tokenWaiting == false) goto L13;
        return;
    L13:
        if (this.reconnectWaiting == false) goto L16;
        return;
    L16:
        if (this.rateNoAuto == false) goto L20;
        showError("HTTP 429 · Espera solicitada superior a 24 horas; sin reintento automático");
        return;
    L20:
        if (this.rateDeadline <= SystemClock.elapsedRealtime()) goto L23;
        waitForRateLimit();
        return;
    L23:
        this.rateDeadline = 0;
        if (this.screenDrawn == true) goto L27;
        return;
    L27:
        if (this.tokenPreflight == true) goto L34;
        if (CdnTokens.allowedHost(Entry.details(this.entry).url) == true) goto L31;
    L32:
        this.tokenPreflight = true;
        this.tokenScoped = false;
        goto L34
    L31:
        if (CdnTokens.hasAccount(this) == false) goto L32;
    L34:
        if (this.tokenPreflight == true) goto L38;
        this.tokenPreflight = true;
        this.tokenWaiting = true;
        final int r0 = this.tokenTicket + 1;
        this.tokenTicket = r0;
        final Entry r2 = this.entry;
        this.status.setText(this.entry.title);
        this.tokenWorker.execute(new PlayerActivity$$ExternalSyntheticLambda1(this, r2, r0));
        return;
    L38:
        if (this.daznPreflight == true) goto L45;
        this.daznPreflight = true;
        this.daznScoped = false;
        if ("Canales".equals(this.entry.kind) == false) goto L45;
        if (DaznTokens.file(this).isFile() == false) goto L45;
        this.tokenWaiting = true;
        final int r1 = this.tokenTicket + 1;
        this.tokenTicket = r1;
        final Entry r3 = this.entry;
        this.tokenWorker.execute(new PlayerActivity$$ExternalSyntheticLambda2(this, r3, r1));
        return;
    L45:
        BlackBox.event("PLAYER_PREPARE", BlackBox.stream(this.entry));
        this.startup.reset(SystemClock.elapsedRealtime());
        this.playbackError = false;
        this.lastPlaybackFailure = null;
        this.status.setTextColor(AppTheme.color(this, -12986907));
        this.status.setText(this.entry.title);
        this.view.setControllerShowTimeoutMs(5000);
    L169:
        e = move-exception;
        if ((e instanceof IllegalArgumentException) == false) goto L173;
        String r4 = e.getMessage();
    L174:
        showError(r4);
        return;
    L173:
        r4 = "No se pudo preparar el reproductor";
        goto L174
    L47:
        if (this.tokenScoped == false) goto L49;
        Map<String, String> r5 = CdnTokens.withoutToken(this.entry.headers);     // Catch: Exception -> L169
    L51:
        if (this.daznScoped == false) goto L53;
        r5 = DaznTokens.withoutToken(r5);     // Catch: Exception -> L169
    L53:
        Iterator<String> r6 = this.entry.headers.keySet().iterator();     // Catch: Exception -> L169
        boolean r12 = false;
    L55:
        if (r6.hasNext() == false) goto L60;
        if (r6.next().equalsIgnoreCase("dazn-token") == false) goto L55;
        r12 = true;
        goto L55
    L60:
        if (this.daznScoped == true) goto L64;
        if (r12 == true) goto L64;
        String r7 = "";
    L65:
        String r13 = r7;
        if (r13.isEmpty() == true) goto L68;
        r5 = new LinkedHashMap(r5);     // Catch: Exception -> L169
        r5.put(HttpHeaders.USER_AGENT, r13);     // Catch: Exception -> L169
    L68:
        DefaultHttpDataSource.Factory r8 = new DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true).setConnectTimeoutMs(15000).setReadTimeoutMs(AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH).setDefaultRequestProperties(r5);     // Catch: Exception -> L169
        if (this.tokenScoped == false) goto L71;
        DataSource.Factory r9 = new ResolvingDataSource.Factory(r8, new PlayerActivity$$ExternalSyntheticLambda3(this));     // Catch: Exception -> L169
    L72:
        DataSource.Factory r10 = r9;
        if (this.daznScoped == false) goto L75;
        r9 = new ResolvingDataSource.Factory(r10, new PlayerActivity$$ExternalSyntheticLambda4(this));     // Catch: Exception -> L169
    L75:
        DataSource.Factory r11 = r9;
        if (this.daznScoped == false) goto L78;
        String r14 = DaznTokens.url(this, this.entry);     // Catch: Exception -> L169
    L79:
        ?? r15 = new C10163(this);     // Catch: Exception -> L169
        DemandMediaSourceFactory r16 = DemandMediaSourceFactory.create(this, TvManifestWarm.create(this, r11)).setLoadErrorHandlingPolicy(r15);     // Catch: Exception -> L169
        MediaItem.Builder r17 = new MediaItem.Builder().setUri(r14);     // Catch: Exception -> L169
        String r18 = StreamFormat.mime(this.entry);     // Catch: Exception -> L169
        if (r14.equals(Entry.details(this.entry).url) == true) goto L82;
        r18 = MimeTypes.APPLICATION_MPD;
    L82:
        if (r18 == null) goto L85;
        r17.setMimeType(r18);     // Catch: Exception -> L169
    L85:
        String r19 = ".*\\.m3u8(?:[?].*)?$";
        if (this.entry.licenseType.isEmpty() == true) goto L135;
        r14 = r14;
        String r20 = this.entry.licenseType.toLowerCase(Locale.ROOT);     // Catch: Exception -> L169
        if (r20.contains("clearkey") == false) goto L100;
        byte[] r21 = clearKey(Entry.details(this.entry).license);     // Catch: Exception -> L169
        final DefaultDrmSessionManager r22 = new DefaultDrmSessionManager.Builder().setUuidAndExoMediaDrmProvider(C0180C.CLEARKEY_UUID, FrameworkMediaDrm.DEFAULT_PROVIDER).build(new LocalMediaDrmCallback(r21));     // Catch: Exception -> L169
        r16.setDrmSessionManagerProvider(new PlayerActivity$$ExternalSyntheticLambda5(r22));     // Catch: Exception -> L169
        if (MimeTypes.APPLICATION_M3U8.equals(r18) == false) goto L92;
    L98:
        MediaSource r23 = HlsPlaybackModule.createSampleAesFactory(this, r11, r21, new ClearKeyHls(r21)).setLoadErrorHandlingPolicy(r15).setDrmSessionManagerProvider(new PlayerActivity$$ExternalSyntheticLambda6(r22)).createMediaSource(r17.build());     // Catch: Exception -> L169
    L99:
        ?? r24 = r15;
        r16 = r16;
        r14 = r14;
        MediaItem.Builder r110 = r17;
        r19 = ".*\\.m3u8(?:[?].*)?$";
        MediaSource r25 = r23;
    L137:
        if (r25 == null) goto L139;
    L145:
        ActivityManager r26 = (ActivityManager) getSystemService("activity");     // Catch: Exception -> L169
        String r27 = this.entry.kind;     // Catch: Exception -> L169
        if (r26 != null) goto L148;
    L152:
        boolean r28 = false;
    L153:
        PlaybackBufferPolicy r29 = PlaybackBufferPolicy.forMime(r27, r18, r28, getPreferences(0).getBoolean("hls-fast-start", true));     // Catch: Exception -> L169
        this.player = TvHlsStartup.builder(this, new DefaultRenderersFactory(this).setEnableDecoderFallback(true).setMediaCodecSelector(new PlayerActivity$$ExternalSyntheticLambda8(this))).setLoadControl(new DefaultLoadControl.Builder().setBufferDurationsMs(r29.min, r29.max, r29.start, r29.rebuffer).setTargetBufferBytes(r29.bytes).setPrioritizeTimeOverSizeThresholds(false).build()).setMediaSourceFactory(r16).build();     // Catch: Exception -> L169
        if (this.trackParameters == null) goto L156;
        this.player.setTrackSelectionParameters(this.trackParameters);     // Catch: Exception -> L169
    L156:
        this.player.setPlaybackSpeed(this.playbackSpeed);     // Catch: Exception -> L169
        ExoPlayer r30 = this.player;     // Catch: Exception -> L169
        if (this.playerMuted == false) goto L159;
        float r31 = 0.0f;
    L160:
        r30.setVolume(r31);     // Catch: Exception -> L169
        this.view.setPlayer(this.player);     // Catch: Exception -> L169
        updateTracks();     // Catch: Exception -> L169
        this.player.addListener(new C10174(this));     // Catch: Exception -> L169
        this.player.addAnalyticsListener(new C10185(this));     // Catch: Exception -> L169
        if (r25 == null) goto L163;
        this.player.setMediaSource(r25);     // Catch: Exception -> L169
    L165:
        if (this.entry.kind.equals("Canales") == true) goto L167;
        this.player.seekTo(this.position);     // Catch: Exception -> L169
    L167:
        Log.i("CHDTVPlayback", "prepare_called_ms=" + (SystemClock.elapsedRealtime() - this.startupAt));     // Catch: Exception -> L169
        this.player.prepare();     // Catch: Exception -> L169
        this.player.play();     // Catch: Exception -> L169
        this.view.showController();     // Catch: Exception -> L169
        this.view.requestFocus();     // Catch: Exception -> L169
        return;
    L163:
        this.player.setMediaItem(r110.build());     // Catch: Exception -> L169
        goto L165
    L159:
        r31 = this.playerVolume;     // Catch: Exception -> L169
        goto L160
    L148:
        if (r26.isLowRamDevice() == false) goto L150;
    L151:
        r28 = true;
        goto L153
    L150:
        if (r26.getMemoryClass() > 128) goto L152;
    L139:
        if (this.entry.licenseType.isEmpty() == false) goto L145;
        if (MimeTypes.APPLICATION_M3U8.equals(r18) == false) goto L143;
    L144:
        r25 = HlsPlaybackModule.createSampleAesFactory(this, r11, null, null).setLoadErrorHandlingPolicy(r24).createMediaSource(r110.build());     // Catch: Exception -> L169
        goto L145
    L143:
        if (r14.toLowerCase(Locale.ROOT).matches(r19) == false) goto L145;
    L92:
        if (Entry.details(this.entry).url.toLowerCase(Locale.ROOT).matches(".*\\.m3u8(?:[?].*)?$") == true) goto L98;
        if (MimeTypes.APPLICATION_SS.equals(r18) == false) goto L97;
        r23 = new SsMediaSource.Factory(TvManifestWarm.create(this, r11)).setManifestParser(new ClearKeySmooth(r21)).setDrmSessionManagerProvider(new PlayerActivity$$ExternalSyntheticLambda7(r22)).createMediaSource(r17.build());     // Catch: Exception -> L169
        goto L99
    L97:
        r23 = null;
        goto L99
    L100:
        r110 = r17;
        if (r20.contains("widevine") == true) goto L109;
        if (r20.contains("playready") == true) goto L109;
        throw new IllegalArgumentException("Sistema DRM no compatible con esta versión");     // Catch: Exception -> L169
    L109:
        if (Entry.details(this.entry).license.contains("|") == true) goto L134;
        if (Entry.details(this.entry).license.matches("(?i)https?://.*") == false) goto L134;
        if (r20.contains("widevine") == false) goto L129;
        if (MimeTypes.APPLICATION_M3U8.equals(r18) == true) goto L121;
        if (Entry.details(this.entry).url.toLowerCase(Locale.ROOT).matches(".*\\.m3u8(?:[?].*)?$") == false) goto L129;
    L121:
        if (this.tokenScoped == true) goto L123;
    L125:
        Map<String, String> r32 = this.entry.headers;     // Catch: Exception -> L169
    L126:
        Map<String, String> r33 = r32;
        r24 = r15;
        r14 = r14;
        r110 = r110;
        r19 = ".*\\.m3u8(?:[?].*)?$";
        r16 = r16;
        r25 = HlsPlaybackModule.createSource(this, r14, null, r33, Entry.details(this.entry).license, this.entry.licenseRequestHeaders(), r11);     // Catch: Exception -> L169
        goto L137
    L123:
        if (CdnTokens.current(this, this.entry).isEmpty() == true) goto L125;
        r32 = CdnTokens.headers(this.entry.headers, CdnTokens.current(this, this.entry));     // Catch: Exception -> L169
    L129:
        if (r20.contains("playready") == false) goto L131;
        UUID r34 = C0180C.PLAYREADY_UUID;     // Catch: Exception -> L169
    L132:
        r110.setDrmConfiguration(new MediaItem.DrmConfiguration.Builder(r34).setLicenseUri(Entry.details(this.entry).license).setLicenseRequestHeaders(this.entry.licenseRequestHeaders()).build());     // Catch: Exception -> L169
        ?? r35 = r15;
    L136:
        r25 = null;
        r24 = r35;
        goto L137
    L131:
        r34 = C0180C.WIDEVINE_UUID;     // Catch: Exception -> L169
    L134:
        throw new IllegalArgumentException("La licencia necesita una URL HTTP/HTTPS simple");     // Catch: Exception -> L169
    L135:
        r35 = r15;
        r19 = ".*\\.m3u8(?:[?].*)?$";
        r16 = r16;
        r110 = r17;
        goto L136
    L78:
        r14 = Entry.details(this.entry).url;     // Catch: Exception -> L169
        goto L79
    L71:
        r9 = r8;
    L64:
        r7 = DaznTokens.userAgent(this, this.entry);     // Catch: Exception -> L169
        goto L65
    L49:
        r5 = this.entry.headers;     // Catch: Exception -> L169
        goto L51
    }

    private void styleDialog(AlertDialog r7) {
        Window r0 = r7.getWindow();
        if (r0 != null) goto L5;
        return;
    L5:
        r0.setBackgroundDrawable(shape(-1207424181, -13460261));
        r0.clearFlags(2);
        int[] r1 = {-1, -2, -3};
        int r2 = 0;
    L7:
        if (r2 >= 3) goto L12;
        Button r4 = r7.getButton(r1[r2]);
        if (r4 == null) goto L11;
        r4.setTextColor(AppTheme.color(this, -10299137));
    L11:
        r2 = r2 + 1;
        goto L7
    }

    private void updateButtons() {
        Button r0 = this.epgButton;
        boolean r3 = false;
        if ("Canales".equals(this.entry.kind) == false) goto L5;
        int r1 = 0;
    L6:
        r0.setVisibility(r1);
        if (this.entry.kind.equals("Canales") == true) goto L9;
    L11:
        boolean r2 = false;
    L12:
        Button r4 = this.previous;
        if (this.loaded == false) goto L18;
        if (r2 == false) goto L16;
    L17:
        boolean r5 = true;
    L19:
        r4.setEnabled(r5);
        Button r6 = this.next;
        if (this.loaded == false) goto L25;
        if (r2 == false) goto L23;
    L24:
        r3 = true;
        goto L25
    L23:
        if (this.sequence.neighbor(1) != null) goto L24;
    L25:
        r6.setEnabled(r3);
        Button r7 = this.previous;
        float r8 = 1.0f;
        if (this.previous.isEnabled() == false) goto L28;
        float r9 = 1.0f;
    L29:
        r7.setAlpha(r9);
        Button r10 = this.next;
        if (this.next.isEnabled() == true) goto L33;
        r8 = 0.4f;
    L33:
        r10.setAlpha(r8);
        return;
    L28:
        r9 = 0.4f;
        goto L29
    L16:
        if (this.sequence.neighbor(-1) != null) goto L17;
    L18:
        r5 = false;
        goto L19
    L9:
        if (this.sequence.entries.size() <= 1) goto L11;
        r2 = true;
        goto L12
    L5:
        r1 = 8;
        goto L6
    }

    private void updateProgramme() {
        if (this.playbackError == false) goto L5;
        return;
    L5:
        if (this.tokenWaiting == false) goto L7;
        return;
    L7:
        if (this.pendingFallbackFailure == null) goto L9;
        return;
    L9:
        if ("Canales".equals(this.entry.kind) == false) goto L23;
        EpgData.Programme r0 = this.epg.nowNext(this.entry, System.currentTimeMillis())[0];
        TextView r1 = this.status;
        StringBuilder r2 = new StringBuilder().append(this.entry.title);
        if (this.fallbackList.isEmpty() == false) goto L13;
        String r3 = "";
    L14:
        StringBuilder r4 = r2.append(r3);
        if (r0 != null) goto L17;
        String r5 = " · Sin programación";
    L18:
        r1.setText(r4.append(r5).toString());
        return;
    L17:
        r5 = " · " + clock(r0.start) + "–" + clock(r0.stop) + " · " + r0.title;
        goto L18
    L13:
        r3 = " · Fuente: " + this.fallbackList;
        goto L14
    }

    private void updateTracks() {
        if (this.player != null) goto L5;
        return;
    L5:
        TrackSelectionParameters r0 = this.player.getTrackSelectionParameters();
        boolean r1 = false;
        UnmodifiableIterator<Tracks.Group> r2 = this.player.getCurrentTracks().getGroups().iterator();
    L7:
        if (r2.hasNext() == false) goto L13;
        Tracks.Group r3 = r2.next();
        if (r3.getType() != 2) goto L7;
        if (r0.overrides.containsKey(r3.getMediaTrackGroup()) == false) goto L7;
        r1 = true;
    L13:
        Format r4 = this.player.getVideoFormat();
        Format r5 = this.player.getAudioFormat();
        String r6 = "";
        if (r4 != null) goto L16;
        String r7 = "";
    L17:
        StringBuilder r8 = new StringBuilder().append("Calidad · ");
        if (r1 == true) goto L20;
        StringBuilder r9 = new StringBuilder().append("Auto");
        if (r7.isEmpty() == true) goto L27;
        r6 = " · " + r7;
    L27:
        String r10 = r9.append(r6).toString();
    L28:
        this.qualityLabel = r8.append(r10).toString();
        if (this.player.getCurrentTracks().isEmpty() == true) goto L31;
    L33:
        boolean r11 = false;
    L34:
        StringBuilder r12 = new StringBuilder().append("Audio · ");
        String r13 = "Buscando…";
        if (r5 != null) goto L39;
        if (r11 == false) goto L38;
        String r14 = "Buscando…";
    L40:
        this.audioLabel = r12.append(r14).toString();
        if (r11 == true) goto L44;
        r13 = "No disponibles";
    L44:
        UnmodifiableIterator<Tracks.Group> r15 = this.player.getCurrentTracks().getGroups().iterator();
    L46:
        if (r15.hasNext() == false) goto L51;
        if (r15.next().getType() != 3) goto L46;
        r13 = "Desactivados";
    L51:
        UnmodifiableIterator<Tracks.Group> r16 = this.player.getCurrentTracks().getGroups().iterator();
    L53:
        if (r16.hasNext() == false) goto L63;
        Tracks.Group r17 = r16.next();
        if (r17.getType() != 3) goto L53;
        int r18 = 0;
    L58:
        if (r18 >= r17.length) goto L53;
        if (r17.isTrackSelected(r18) == false) goto L62;
        r13 = PlaybackTracks.language(r17.getTrackFormat(r18));
    L62:
        r18 = r18 + 1;
        goto L58
    L63:
        this.subtitleLabel = "Subtítulos · " + r13;
        if (this.settingsRender == null) goto L83;
        this.settingsRender.run();
        return;
    L83:
        return;
    L38:
        r14 = "No disponible";
        goto L40
    L39:
        r14 = PlaybackTracks.audio(r5);
        goto L40
    L31:
        if (this.player.getPlaybackState() == 3) goto L33;
        r11 = true;
        goto L34
    L20:
        if (r7.isEmpty() == false) goto L22;
        r10 = "Manual";
        goto L28
    L22:
        r10 = r7;
        goto L28
    L16:
        r7 = PlaybackTracks.resolution(r4);
        goto L17
    }

    private void waitForRateLimit() {
        this.tokenWaiting = true;
        final int r1 = this.tokenTicket + 1;
        this.tokenTicket = r1;
        this.view.setControllerShowTimeoutMs(0);
        this.view.showController();
        cancelHttpRetry();
        this.httpRetryTask = new RunnableC10196(this, r1);
        this.main.post(this.httpRetryTask);
    }

    public void cfReady(Entry r7, int r8, PlaybackException r9, ChannelFallback.Choice r10, Throwable r11) {
        if (r8 == this.tokenTicket) goto L5;
        return;
    L5:
        if (this.entry == r7) goto L7;
        return;
    L7:
        if (this.started == true) goto L9;
        return;
    L9:
        if (this.fallbackSwitching == true) goto L11;
        return;
    L11:
        if (isFinishing() == false) goto L13;
        return;
    L13:
        if (isDestroyed() == true) goto L35;
        if (r10 == null) goto L20;
        ChannelFallback.sameFormat(r7, r10.entry);
        if (1 != 0) goto L20;
        r11 = new IllegalStateException("format_mismatch");
    L20:
        if (r11 != null) goto L25;
        if (r10 == null) goto L25;
        release();
        cancelHttpRetry();
        this.tokenTicket++;
        this.tokenWaiting = false;
        this.tokenPreflight = true;
        this.rateRetryUsed = false;
        this.rateNoAuto = false;
        this.rateDeadline = 0;
        this.failedDecoders.clear();
        this.trackParameters = null;
        this.daznPreflight = false;
        this.fallbackSwitching = true;
        Entry r12 = r10.entry;
        this.status.setText(r12.title + " · Cambiando a " + r10.list + "…");
        ParentalControl.requireEntry(this, r12, new ChannelFallbackWork.Select(this, r7, this.tokenTicket, r12, r10.list));
        return;
    L25:
        this.fallbackSwitching = false;
        if (r11 == null) goto L28;
        BlackBox.event("CHANNEL_FALLBACK_FAILED", r11.getClass().getSimpleName());
    L28:
        showError(PlaybackFailure.summary(r9));
        return;
    }

    public void cfSelect(Entry r2, int r3, Entry r4, String r5) {
        if (r3 == this.tokenTicket) goto L5;
        return;
    L5:
        if (this.entry == r2) goto L7;
        return;
    L7:
        if (this.started == true) goto L9;
        return;
    L9:
        if (isFinishing() == false) goto L11;
        return;
    L11:
        if (isDestroyed() == true) goto L23;
        ChannelFallback.sameFormat(r2, r4);
        if (1 != 0) goto L17;
        this.fallbackSwitching = false;
        showError("La fuente alternativa tiene otro formato");
        return;
    L17:
        this.fallbackSwitching = false;
        this.entry = r4;
        this.fallbackList = r5;
        this.position = 0;
        this.tokenPreflight = false;
        this.tokenScoped = false;
        rebuild();
        refreshEpg();
        startPlayer();
        return;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent r21) {
        int r1 = r21.getKeyCode();
        boolean r3 = false;
        if (r1 != 166) goto L5;
    L8:
        boolean r2 = true;
    L10:
        if (r1 != 167) goto L12;
    L13:
        r3 = true;
    L14:
        int r5 = -1;
        if (r2 == true) goto L75;
        if (r3 == true) goto L75;
        AudioManager r6 = (AudioManager) getSystemService(MimeTypes.BASE_TYPE_AUDIO);
        if (r6 == null) goto L44;
        if (r6.isVolumeFixed() == false) goto L44;
        if (r1 == 24) goto L28;
        if (r1 == 25) goto L28;
        if (r1 != 164) goto L44;
    L28:
        if (r21.getAction() != 0) goto L42;
        if (r1 == 164) goto L31;
        if (r1 != 24) goto L41;
        r5 = 1;
    L41:
        adjustVolume(r5);
        goto L42
    L31:
        if (r21.getRepeatCount() != 0) goto L42;
        this.playerMuted = !this.playerMuted;
        if (this.player == null) goto L42;
        ExoPlayer r7 = this.player;
        if (this.playerMuted == false) goto L37;
        float r8 = 0.0f;
    L38:
        r7.setVolume(r8);
        goto L42
    L37:
        r8 = this.playerVolume;
    L42:
        return true;
    L44:
        if (r1 != 160) goto L46;
    L49:
        KeyEvent r9 = new KeyEvent(r21.getDownTime(), r21.getEventTime(), r21.getAction(), 23, r21.getRepeatCount(), r21.getMetaState(), r21.getDeviceId(), r21.getScanCode(), r21.getFlags(), r21.getSource());
    L51:
        if (this.view == null) goto L73;
        if (this.view.isControllerFullyVisible() == true) goto L73;
        if (r9.getAction() != 0) goto L73;
        if (r9.getKeyCode() != 23) goto L59;
    L66:
        this.view.showController();
        if (this.previous.isEnabled() == false) goto L69;
        Button r10 = this.previous;
    L70:
        r10.requestFocus();
        return true;
    L69:
        r10 = this.fit;
        goto L70
    L59:
        if (r9.getKeyCode() == 19) goto L66;
        if (r9.getKeyCode() == 20) goto L66;
        if (r9.getKeyCode() == 21) goto L66;
        if (r9.getKeyCode() == 22) goto L66;
    L73:
        return super.dispatchKeyEvent(r9);
    L46:
        if (r1 == 96) goto L49;
        r9 = r21;
    L75:
        if (r21.getAction() == 0) goto L77;
    L81:
        return true;
    L77:
        if (r21.getRepeatCount() != 0) goto L81;
        if (r2 == false) goto L80;
        r5 = 1;
    L80:
        adjacent(r5);
        goto L81
    L12:
        if (r1 != 88) goto L14;
    L5:
        if (r1 == 87) goto L8;
        r2 = false;
        goto L10
    }

    boolean isVodEntry() {
        Entry r0 = this.entry;
        if (r0 == null) goto L10;
        String r1 = r0.kind;
        if ("Películas".equals(r1) == false) goto L7;
        return true;
    L7:
        if ("Series".equals(r1) == false) goto L13;
        return true;
    L13:
        return false;
    L10:
        return false;
    }

    @Override // android.app.Activity
    protected void onActivityResult(int r2, int r3, Intent r4) {
        super.onActivityResult(r2, r3, r4);
        if (r2 == 42) goto L5;
        return;
    L5:
        if (r3 != (-1)) goto L12;
        if (r4 == null) goto L13;
        Entry r0 = (Entry) r4.getSerializableExtra("entry");
        if (r0 == null) goto L14;
        select(r0);
        return;
    L14:
        return;
    L13:
        return;
    }

    @Override // android.app.Activity
    public void onCreate(Bundle r14) {
        AppTheme.apply(this);
        super.onCreate(r14);
        this.startup.requested(getIntent().getLongExtra("playbackRequestedAt", this.startupAt));
        if (r14 != null) goto L5;
        Serializable r0 = getIntent().getSerializableExtra("entry");
    L6:
        this.entry = (Entry) r0;
        if (this.entry != null) goto L11;
        finish();
        return;
    L11:
        if (r14 != null) goto L13;
    L15:
        Entry r1 = this.entry;
    L16:
        this.fallback = new ChannelFallback(r1);
        if (r14 == null) goto L22;
        ArrayList<String> r2 = r14.getStringArrayList("fallbackTried");
        if (r2 == null) goto L21;
        this.fallback.tried.addAll(r2);
    L21:
        this.tokenRetryUsed = r14.getBoolean("tokenRetryUsed");
        this.fallbackList = r14.getString("fallbackList", "");
    L22:
        if (r14 == null) goto L25;
        this.rateRetryUsed = r14.getBoolean("rateRetryUsed");
        this.rateNoAuto = r14.getBoolean("rateNoAuto");
        this.rateDeadline = r14.getLong("rateDeadline");
    L25:
        if (r14 == null) goto L27;
        this.playbackSpeed = r14.getFloat("speed", 1.0f);
    L27:
        if (r14 != null) goto L29;
    L31:
        int r3 = 0;
        if (r14 != null) goto L34;
    L36:
        boolean r4 = false;
    L37:
        this.zoom = r4;
        if (r14 != null) goto L40;
        long r5 = resume(this.entry);
    L41:
        this.position = r5;
        setVolumeControlStream(3);
        this.playerVolume = getPreferences(0).getFloat("player-volume", 1.0f);
        setRequestedOrientation(6);
        getWindow().addFlags(128);
        this.root = new FrameLayout(this);
        this.root.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.view = new LogoAwarePlayerView(this);
        PlayerView r6 = this.view;
        if (this.zoom == false) goto L44;
        int r7 = 4;
    L45:
        r6.setResizeMode(r7);
        this.view.setShowNextButton(false);
        this.view.setShowPreviousButton(false);
        this.view.setControllerShowTimeoutMs(5000);
        this.root.addView(this.view, new FrameLayout.LayoutParams(-1, -1));
        this.controls = new LinearLayout(this);
        this.controls.setOrientation(1);
        this.controls.setPadding(m404dp(12), m404dp(8), m404dp(12), m404dp(8));
        this.controls.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{AppTheme.color(this, -804711649), AppTheme.color(this, 269030175)}));
        ImageView r8 = new ImageView(this);
        ChdtvBrand.show(r8);
        r8.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (AppTheme.cine(this) == false) goto L48;
        String r9 = "CHDTV";
    L49:
        r8.setContentDescription(r9);
        LinearLayout r10 = new LinearLayout(this);
        r10.setGravity(16);
        r10.addView(r8, new LinearLayout.LayoutParams(m404dp(96), m404dp(54)));
        this.controls.addView(r10, new LinearLayout.LayoutParams(-1, -2));
        this.status = new TextView(this);
        this.status.setTextSize(16.0f);
        this.status.setTextColor(AppTheme.color(this, -12986907));
        this.status.setTypeface(null, 1);
        this.status.setMaxLines(2);
        this.status.setEllipsize(TextUtils.TruncateAt.END);
        this.status.setText(this.entry.title);
        PlayerControlBar r11 = new PlayerControlBar(this);
        this.previous = button("◀ Anterior", new PlayerActivity$$ExternalSyntheticLambda13(this));
        this.next = button("Siguiente ▶", new PlayerActivity$$ExternalSyntheticLambda15(this));
        if (this.zoom == false) goto L52;
        String r12 = "Proporción";
    L53:
        this.fit = button(r12, new PlayerActivity$$ExternalSyntheticLambda16(this));
        r11.addControl(this.previous);
        r11.addControl(button("Lista", new PlayerActivity$$ExternalSyntheticLambda17(this)));
        r11.addControl(this.next);
        this.epgButton = button("EPG", new PlayerActivity$$ExternalSyntheticLambda18(this));
        r11.addControl(this.epgButton);
        r11.addControl(this.fit);
        r11.addControl(button("‹ Atrás", new PlayerActivity$$ExternalSyntheticLambda19(this)));
        Button r13 = button("Vol −", new PlayerActivity$$ExternalSyntheticLambda20(this));
        Button r15 = button("Vol +", new PlayerActivity$$ExternalSyntheticLambda21(this));
        r13.setContentDescription("Bajar volumen");
        r15.setContentDescription("Subir volumen");
        r11.addControl(r13);
        r11.addControl(r15);
        r10.addView(r11, new LinearLayout.LayoutParams(0, m404dp(54), 1.0f));
        this.controls.addView(this.status, new LinearLayout.LayoutParams(-1, -2));
        this.settingsButton = this.view.findViewById(C0467R.id.exo_settings);
        this.settingsButton.setContentDescription("Ajustes de reproducción: calidad, audio y subtítulos");
        this.settingsButton.setOnClickListener(new PlayerActivity$$ExternalSyntheticLambda23(this));
        this.root.addView(this.controls, new FrameLayout.LayoutParams(-1, -2, 48));
        setContentView(this.root);
        this.root.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserverOnPreDrawListenerC10152(this));
        this.view.setControllerShowTimeoutMs(0);
        this.view.showController();
        LinearLayout r16 = this.controls;
        if (inFloatingWindow() == false) goto L56;
        r3 = 8;
    L56:
        r16.setVisibility(r3);
        this.root.setOnApplyWindowInsetsListener(new PlayerActivity$$ExternalSyntheticLambda24(this));
        this.view.setControllerVisibilityListener(new PlayerActivity$$ExternalSyntheticLambda14(this));
        immersive();
        this.sequence = new PlaybackSequence(Collections.emptyList(), this.entry);
        updateButtons();
        return;
    L52:
        r12 = "Pantalla";
        goto L53
    L48:
        r9 = "CHDTV · pitufo con cerveza";
        goto L49
    L44:
        r7 = 0;
        goto L45
    L40:
        r5 = r14.getLong("position");
        goto L41
    L34:
        if (r14.getBoolean("zoom") == false) goto L36;
        r4 = true;
        goto L37
    L29:
        if (r14.getBundle("tracks") == null) goto L31;
        this.trackParameters = TrackSelectionParameters.fromBundle(r14.getBundle("tracks"));
        goto L31
    L13:
        if ((r14.getSerializable("fallbackAnchor") instanceof Entry) == false) goto L15;
        r1 = (Entry) r14.getSerializable("fallbackAnchor");
        goto L16
    L5:
        r0 = r14.getSerializable("entry");
        goto L6
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.started = false;
        release();
        cancelReconnect(false);
        if (this.epgDialog == null) goto L6;
        this.epgDialog.dismiss();
    L6:
        if (this.settingsDialog == null) goto L9;
        this.settingsDialog.dismiss();
    L9:
        if (this.tracksDialog == null) goto L11;
        this.tracksDialog.dismiss();
    L11:
        this.tokenWorker.shutdownNow();
        this.worker.shutdownNow();
        this.main.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int r3, KeyEvent r4) {
        if (r3 != 166) goto L5;
    L16:
        adjacent(1);
        return true;
    L5:
        if (r3 == 87) goto L16;
        if (r3 != 167) goto L10;
    L14:
        adjacent(-1);
        return true;
    L10:
        if (r3 == 88) goto L14;
        return super.onKeyDown(r3, r4);
    }

    @Override // android.app.Activity
    public void onPictureInPictureModeChanged(boolean r4, Configuration r5) {
        super.onPictureInPictureModeChanged(r4, r5);
        int r0 = 0;
        if (r4 == false) goto L18;
        if (this.settingsDialog == null) goto L8;
        this.settingsDialog.dismiss();
    L8:
        if (this.tracksDialog == null) goto L11;
        this.tracksDialog.dismiss();
    L11:
        if (this.browseDialog == null) goto L14;
        this.browseDialog.dismiss();
    L14:
        if (this.epgDialog == null) goto L16;
        this.epgDialog.dismiss();
    L16:
        this.view.hideController();
        this.view.setUseController(false);
        this.controls.setVisibility(8);
        this.view.setResizeMode(0);
        return;
    L18:
        if (isFinishing() == true) goto L25;
        this.view.setUseController(true);
        PlayerView r1 = this.view;
        if (this.zoom == false) goto L22;
        r0 = 4;
    L22:
        r1.setResizeMode(r0);
        this.view.setControllerShowTimeoutMs(5000);
        this.view.showController();
        immersive();
        updateFloatingParams();
        return;
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        updateFloatingParams();
        RecordingIndicator.attach(this);
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle r4) {
        r4.putSerializable("entry", this.entry);
        r4.putSerializable("fallbackAnchor", this.fallback.anchor);
        r4.putStringArrayList("fallbackTried", new ArrayList(this.fallback.tried));
        r4.putBoolean("tokenRetryUsed", this.tokenRetryUsed);
        r4.putString("fallbackList", this.fallbackList);
        r4.putBoolean("rateRetryUsed", this.rateRetryUsed);
        r4.putBoolean("rateNoAuto", this.rateNoAuto);
        r4.putLong("rateDeadline", this.rateDeadline);
        if (this.player != null) goto L5;
        long r0 = this.position;
    L6:
        r4.putLong("position", r0);
        r4.putBoolean("zoom", this.zoom);
        if (this.player != null) goto L9;
        float r1 = this.playbackSpeed;
    L10:
        r4.putFloat("speed", r1);
        if (this.player == null) goto L14;
        this.trackParameters = this.player.getTrackSelectionParameters();
    L14:
        if (this.trackParameters == null) goto L16;
        r4.putBundle("tracks", this.trackParameters.toBundle());
    L16:
        super.onSaveInstanceState(r4);
        return;
    L9:
        r1 = this.player.getPlaybackParameters().speed;
        goto L10
    L5:
        r0 = this.player.getCurrentPosition();
        goto L6
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        ParentalControl.requireEntry(this, this.entry, new PlayerActivity$$ExternalSyntheticLambda55(this));
    }

    @Override // android.app.Activity
    protected void onStop() {
        cancelReconnect(false);
        if (this.browseDialog == null) goto L5;
        this.browseDialog.dismiss();
    L5:
        this.started = false;
        this.fallbackSwitching = false;
        this.pendingFallbackFailure = null;
        cancelHttpRetry();
        this.tokenTicket++;
        this.tokenWaiting = false;
        this.tokenPreflight = false;
        this.main.removeCallbacks(this.historyTick);
        this.main.removeCallbacks(this.epgTick);
        if (this.settingsDialog == null) goto L9;
        this.settingsDialog.dismiss();
    L9:
        if (this.tracksDialog == null) goto L11;
        this.tracksDialog.dismiss();
    L11:
        release();
        super.onStop();
    }

    @Override // android.app.Activity
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (Build.VERSION.SDK_INT < 31) goto L5;
        return;
    L5:
        if (PipPlayback.enabled(this) == true) goto L7;
        return;
    L7:
        if (inFloatingWindow() == true) goto L12;
        enterFloatingWindow();
        return;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean r1) {
        super.onWindowFocusChanged(r1);
        if (r1 == false) goto L6;
        immersive();
        return;
    }

    public void sectionReady(List r1, Catalog r2, Map r3, Entry r4, PlaybackSequence r5) {
        lambda$loadPlaybackCatalog$13(r1, r2, r3, r4, r5);
    }

    public Entry tvCurrent() {
        return this.entry;
    }

    public boolean tvHlsEnabled() {
        if (this.entry != null) goto L5;
        return false;
    L5:
        if ("Canales".equals(this.entry.kind) == false) goto L10;
        return true;
    L10:
        return false;
    }

    public boolean tvHlsFastStartup() {
        return false;
    }

    public List<Entry> tvNeighbors() {
        ArrayList r0 = new ArrayList();
        if (this.sequence != null) goto L5;
    L23:
        return r0;
    L5:
        if (this.sequence.entries.size() < 2) goto L23;
        int[] r1 = {-1, 1};
        int r5 = 0;
    L8:
        if (r5 >= 2) goto L9;
        int r6 = r1[r5];
        Entry r7 = this.sequence.neighbor(r6);
        if (r7 != null) goto L18;
        List<Entry> r8 = this.sequence.entries;
        if (r6 >= 0) goto L15;
        int r9 = this.sequence.entries.size() - 1;
    L16:
        r7 = r8.get(r9);
        goto L18
    L15:
        r9 = 0;
    L18:
        if (r7 == this.entry) goto L22;
        if (r0.contains(r7) == true) goto L22;
        r0.add(r7);
    L22:
        r5 = r5 + 1;
        goto L8
    L9:
        return r0;
    }

    public void tvSkipHlsToken() {
        if (this.entry != null) goto L5;
        return;
    L5:
        if (this.tokenPreflight == false) goto L7;
        return;
    L7:
        if (this.tokenWaiting == false) goto L9;
        return;
    L9:
        if (MimeTypes.APPLICATION_M3U8.equals(StreamFormat.mime(Entry.details(this.entry))) == false) goto L15;
        this.tokenPreflight = true;
        this.tokenScoped = false;
        BlackBox.event("HLS_TOKEN_BYPASS", "movistar_account_skipped=true");
        return;
    }

    public void tvTokenScope(Entry r1, int r2) {
        lambda$startPlayer$23(r1, r2);
    }

    public boolean tvWarmActive() {
        return this.started;
    }

    public boolean tvWarmAllowed() {
        if (tvWarmPlainHls() == false) goto L5;
        return true;
    L5:
        if (tvHlsEnabled() == true) goto L7;
        return false;
    L7:
        if (this.tokenScoped == true) goto L9;
        return false;
    L9:
        if (this.daznScoped == false) goto L16;
        return false;
    L16:
        return true;
    }

    public boolean tvWarmCanPrime() {
        if (tvWarmPlainHls() == false) goto L13;
        if (this.started == true) goto L7;
    L11:
        return false;
    L7:
        if (this.loaded == false) goto L11;
        if (this.player == null) goto L11;
        return true;
    L13:
        return tvWarmReady();
    }

    public boolean tvWarmPlainHls() {
        if (tvHlsFastStartup() == true) goto L5;
        return false;
    L5:
        if (this.tokenScoped == false) goto L7;
        return false;
    L7:
        if (this.daznScoped == false) goto L9;
        return false;
    L9:
        if (this.entry != null) goto L11;
        return false;
    L11:
        if (MimeTypes.APPLICATION_M3U8.equals(StreamFormat.mime(this.entry)) == false) goto L19;
        return true;
    L19:
        return false;
    }

    public boolean tvWarmReady() {
        if (this.started == true) goto L5;
        return false;
    L5:
        if (this.loaded == true) goto L7;
        return false;
    L7:
        if (this.player != null) goto L9;
        return false;
    L9:
        if (this.player.getPlaybackState() != 3) goto L16;
        return true;
    L16:
        return false;
    }

    void updateFloatingParams() {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return;
    L5:
        if (PipPlayback.supported(this) == true) goto L7;
        return;
    L7:
        if (isFinishing() == true) goto L19;
        setPictureInPictureParams(floatingParams());     // Catch: IllegalArgumentException -> L11 IllegalStateException -> L13
    L20:
        return;
    L21:
        return;
    L14:
        return;
    }
}
