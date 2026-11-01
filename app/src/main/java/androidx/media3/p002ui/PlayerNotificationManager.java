package androidx.media3.p002ui;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.session.MediaSession;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.media.session.MediaSessionCompat;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C0180C;
import androidx.media3.common.DeviceInfo;
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
import androidx.media3.common.util.NotificationUtil;
import androidx.media3.common.util.Util;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class PlayerNotificationManager {
    private static final String ACTION_DISMISS = "androidx.media3.ui.notification.dismiss";
    public static final String ACTION_FAST_FORWARD = "androidx.media3.ui.notification.ffwd";
    public static final String ACTION_NEXT = "androidx.media3.ui.notification.next";
    public static final String ACTION_PAUSE = "androidx.media3.ui.notification.pause";
    public static final String ACTION_PLAY = "androidx.media3.ui.notification.play";
    public static final String ACTION_PREVIOUS = "androidx.media3.ui.notification.prev";
    public static final String ACTION_REWIND = "androidx.media3.ui.notification.rewind";
    public static final String ACTION_STOP = "androidx.media3.ui.notification.stop";
    public static final String EXTRA_INSTANCE_ID = "INSTANCE_ID";
    private static final int MSG_START_OR_UPDATE_NOTIFICATION = 1;
    private static final int MSG_UPDATE_NOTIFICATION_BITMAP = 2;
    private static int instanceIdCounter;
    private int badgeIconType;
    private NotificationCompat.Builder builder;
    private List<NotificationCompat.Action> builderActions;
    private final String channelId;
    private int color;
    private boolean colorized;
    private final Context context;
    private int currentNotificationTag;
    private final CustomActionReceiver customActionReceiver;
    private final Map<String, NotificationCompat.Action> customActions;
    private int defaults;
    private final PendingIntent dismissPendingIntent;
    private String groupKey;
    private final int instanceId;
    private final IntentFilter intentFilter;
    private boolean isNotificationStarted;
    private final Handler mainHandler;
    private final MediaDescriptionAdapter mediaDescriptionAdapter;
    private MediaSession.Token mediaSessionToken;
    private final NotificationBroadcastReceiver notificationBroadcastReceiver;
    private final int notificationId;
    private final NotificationListener notificationListener;
    private final NotificationManagerCompat notificationManager;
    private final Map<String, NotificationCompat.Action> playbackActions;
    private Player player;
    private final Player.Listener playerListener;
    private int priority;
    private boolean showPlayButtonIfSuppressed;
    private int smallIconResourceId;
    private boolean useChronometer;
    private boolean useFastForwardAction;
    private boolean useFastForwardActionInCompactView;
    private boolean useNextAction;
    private boolean useNextActionInCompactView;
    private boolean usePlayPauseActions;
    private boolean usePreviousAction;
    private boolean usePreviousActionInCompactView;
    private boolean useRewindAction;
    private boolean useRewindActionInCompactView;
    private boolean useStopAction;
    private int visibility;

    /* JADX INFO: renamed from: androidx.media3.ui.PlayerNotificationManager$1 */
    static /* synthetic */ class C04651 {
    }

    public final class BitmapCallback {
        private final int notificationTag;
        final /* synthetic */ PlayerNotificationManager this$0;

        private BitmapCallback(PlayerNotificationManager r1, int r2) {
            this.this$0 = r1;
            this.notificationTag = r2;
        }

        /* synthetic */ BitmapCallback(PlayerNotificationManager r1, int r2, C04651 r3) {
            this(r1, r2);
        }

        public void onBitmap(Bitmap r3) {
            if (r3 == null) goto L5;
            PlayerNotificationManager.access$000(this.this$0, r3, this.notificationTag);
            return;
        }
    }

    public static class Builder {
        protected int channelDescriptionResourceId;
        protected final String channelId;
        protected int channelImportance;
        protected int channelNameResourceId;
        protected final Context context;
        protected CustomActionReceiver customActionReceiver;
        protected int fastForwardActionIconResourceId;
        protected String groupKey;
        protected MediaDescriptionAdapter mediaDescriptionAdapter;
        protected int nextActionIconResourceId;
        protected final int notificationId;
        protected NotificationListener notificationListener;
        protected int pauseActionIconResourceId;
        protected int playActionIconResourceId;
        protected int previousActionIconResourceId;
        protected int rewindActionIconResourceId;
        protected int smallIconResourceId;
        protected int stopActionIconResourceId;

        public Builder(Context r3, int r4, String r5) {
            if (r4 <= 0) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            this.context = r3;
            this.notificationId = r4;
            this.channelId = r5;
            this.channelImportance = 2;
            this.mediaDescriptionAdapter = new DefaultMediaDescriptionAdapter(null);
            this.smallIconResourceId = C0467R.drawable.exo_notification_small_icon;
            this.playActionIconResourceId = C0467R.drawable.exo_notification_play;
            this.pauseActionIconResourceId = C0467R.drawable.exo_notification_pause;
            this.stopActionIconResourceId = C0467R.drawable.exo_notification_stop;
            this.rewindActionIconResourceId = C0467R.drawable.exo_notification_rewind;
            this.fastForwardActionIconResourceId = C0467R.drawable.exo_notification_fastforward;
            this.previousActionIconResourceId = C0467R.drawable.exo_notification_previous;
            this.nextActionIconResourceId = C0467R.drawable.exo_notification_next;
            return;
        L5:
            r0 = false;
            goto L6
        }

        @Deprecated
        public Builder(Context r1, int r2, String r3, MediaDescriptionAdapter r4) {
            this(r1, r2, r3);
            this.mediaDescriptionAdapter = r4;
        }

        public PlayerNotificationManager build() {
            if (this.channelNameResourceId == 0) goto L6;
            NotificationUtil.createNotificationChannel(this.context, this.channelId, this.channelNameResourceId, this.channelDescriptionResourceId, this.channelImportance);
        L6:
            return new PlayerNotificationManager(this.context, this.channelId, this.notificationId, this.mediaDescriptionAdapter, this.notificationListener, this.customActionReceiver, this.smallIconResourceId, this.playActionIconResourceId, this.pauseActionIconResourceId, this.stopActionIconResourceId, this.rewindActionIconResourceId, this.fastForwardActionIconResourceId, this.previousActionIconResourceId, this.nextActionIconResourceId, this.groupKey);
        }

        public Builder setChannelDescriptionResourceId(int r1) {
            this.channelDescriptionResourceId = r1;
            return this;
        }

        public Builder setChannelImportance(int r1) {
            this.channelImportance = r1;
            return this;
        }

        public Builder setChannelNameResourceId(int r1) {
            this.channelNameResourceId = r1;
            return this;
        }

        public Builder setCustomActionReceiver(CustomActionReceiver r1) {
            this.customActionReceiver = r1;
            return this;
        }

        public Builder setFastForwardActionIconResourceId(int r1) {
            this.fastForwardActionIconResourceId = r1;
            return this;
        }

        public Builder setGroup(String r1) {
            this.groupKey = r1;
            return this;
        }

        public Builder setMediaDescriptionAdapter(MediaDescriptionAdapter r1) {
            this.mediaDescriptionAdapter = r1;
            return this;
        }

        public Builder setNextActionIconResourceId(int r1) {
            this.nextActionIconResourceId = r1;
            return this;
        }

        public Builder setNotificationListener(NotificationListener r1) {
            this.notificationListener = r1;
            return this;
        }

        public Builder setPauseActionIconResourceId(int r1) {
            this.pauseActionIconResourceId = r1;
            return this;
        }

        public Builder setPlayActionIconResourceId(int r1) {
            this.playActionIconResourceId = r1;
            return this;
        }

        public Builder setPreviousActionIconResourceId(int r1) {
            this.previousActionIconResourceId = r1;
            return this;
        }

        public Builder setRewindActionIconResourceId(int r1) {
            this.rewindActionIconResourceId = r1;
            return this;
        }

        public Builder setSmallIconResourceId(int r1) {
            this.smallIconResourceId = r1;
            return this;
        }

        public Builder setStopActionIconResourceId(int r1) {
            this.stopActionIconResourceId = r1;
            return this;
        }
    }

    public interface CustomActionReceiver {
        Map<String, NotificationCompat.Action> createCustomActions(Context r1, int r2);

        List<String> getCustomActions(Player r1);

        void onCustomAction(Player r1, String r2, Intent r3);
    }

    public interface MediaDescriptionAdapter {

        /* JADX INFO: renamed from: androidx.media3.ui.PlayerNotificationManager$MediaDescriptionAdapter$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static CharSequence $default$getCurrentSubText(MediaDescriptionAdapter r1, Player r2) {
                return null;
            }
        }

        PendingIntent createCurrentContentIntent(Player r1);

        CharSequence getCurrentContentText(Player r1);

        CharSequence getCurrentContentTitle(Player r1);

        Bitmap getCurrentLargeIcon(Player r1, BitmapCallback r2);

        CharSequence getCurrentSubText(Player r1);
    }

    private static final class MediaStyle extends NotificationCompat.Style {
        private final int[] actionsToShowInCompact;
        private final MediaSession.Token token;

        public MediaStyle(MediaSession.Token r1, int[] r2) {
            this.token = r1;
            this.actionsToShowInCompact = r2;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void apply(NotificationBuilderWithBuilderAccessor r3) {
            Notification.MediaStyle r0 = new Notification.MediaStyle();
            r0.setShowActionsInCompactView(this.actionsToShowInCompact);
            if (this.token == null) goto L5;
            r0.setMediaSession(this.token);
        L5:
            r3.getBuilder().setStyle(r0);
        }
    }

    private class NotificationBroadcastReceiver extends BroadcastReceiver {
        final /* synthetic */ PlayerNotificationManager this$0;

        private NotificationBroadcastReceiver(PlayerNotificationManager r1) {
            this.this$0 = r1;
        }

        /* synthetic */ NotificationBroadcastReceiver(PlayerNotificationManager r1, C04651 r2) {
            this(r1);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context r5, Intent r6) {
            Player r0 = PlayerNotificationManager.access$500(this.this$0);
            if (r0 != null) goto L5;
            return;
        L5:
            if (PlayerNotificationManager.access$600(this.this$0) == true) goto L7;
            return;
        L7:
            if (r6.getIntExtra(PlayerNotificationManager.EXTRA_INSTANCE_ID, PlayerNotificationManager.access$700(this.this$0)) != PlayerNotificationManager.access$700(this.this$0)) goto L71;
            String r1 = r6.getAction();
            if (PlayerNotificationManager.ACTION_PLAY.equals(r1) == false) goto L13;
            Util.handlePlayButtonAction(r0);
            return;
        L13:
            if (PlayerNotificationManager.ACTION_PAUSE.equals(r1) == false) goto L16;
            Util.handlePauseButtonAction(r0);
            return;
        L16:
            if (PlayerNotificationManager.ACTION_PREVIOUS.equals(r1) == false) goto L21;
            if (r0.isCommandAvailable(7) == false) goto L56;
            r0.seekToPrevious();
            return;
        L56:
            return;
        L21:
            if (PlayerNotificationManager.ACTION_REWIND.equals(r1) == false) goto L26;
            if (r0.isCommandAvailable(11) == false) goto L58;
            r0.seekBack();
            return;
        L58:
            return;
        L26:
            if (PlayerNotificationManager.ACTION_FAST_FORWARD.equals(r1) == false) goto L31;
            if (r0.isCommandAvailable(12) == false) goto L60;
            r0.seekForward();
            return;
        L60:
            return;
        L31:
            if (PlayerNotificationManager.ACTION_NEXT.equals(r1) == false) goto L36;
            if (r0.isCommandAvailable(9) == false) goto L62;
            r0.seekToNext();
            return;
        L62:
            return;
        L36:
            if (PlayerNotificationManager.ACTION_STOP.equals(r1) == false) goto L44;
            if (r0.isCommandAvailable(3) == false) goto L41;
            r0.stop();
        L41:
            if (r0.isCommandAvailable(20) == false) goto L64;
            r0.clearMediaItems();
            return;
        L64:
            return;
        L44:
            if (PlayerNotificationManager.ACTION_DISMISS.equals(r1) == false) goto L46;
            PlayerNotificationManager.access$800(this.this$0, true);
            return;
        L46:
            if (r1 != null) goto L48;
            return;
        L48:
            if (PlayerNotificationManager.access$900(this.this$0) != null) goto L50;
            return;
        L50:
            if (PlayerNotificationManager.access$1000(this.this$0).containsKey(r1) == false) goto L69;
            PlayerNotificationManager.access$900(this.this$0).onCustomAction(r0, r1, r6);
            return;
        L69:
            return;
        }
    }

    public interface NotificationListener {

        /* JADX INFO: renamed from: androidx.media3.ui.PlayerNotificationManager$NotificationListener$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static void $default$onNotificationCancelled(NotificationListener r0, int r1, boolean r2) {
            }

            public static void $default$onNotificationPosted(NotificationListener r0, int r1, Notification r2, boolean r3) {
            }
        }

        void onNotificationCancelled(int r1, boolean r2);

        void onNotificationPosted(int r1, Notification r2, boolean r3);
    }

    private class PlayerListener implements Player.Listener {
        final /* synthetic */ PlayerNotificationManager this$0;

        private PlayerListener(PlayerNotificationManager r1) {
            this.this$0 = r1;
        }

        /* synthetic */ PlayerListener(PlayerNotificationManager r1, C04651 r2) {
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
        public void onEvents(Player r2, Player.Events r3) {
            if (r3.containsAny(new int[]{4, 5, 7, 0, 12, 11, 8, 9, 14}) == false) goto L6;
            PlayerNotificationManager.access$400(this.this$0);
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

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Priority {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {
    }

    /* JADX INFO: renamed from: $r8$lambda$u3HEfsORrwGTCkrRpEU4-odV89c, reason: not valid java name */
    public static /* synthetic */ boolean m493$r8$lambda$u3HEfsORrwGTCkrRpEU4odV89c(PlayerNotificationManager r0, Message r1) {
        return r0.handleMessage(r1);
    }

    protected PlayerNotificationManager(Context r18, String r19, int r20, MediaDescriptionAdapter r21, NotificationListener r22, CustomActionReceiver r23, int r24, int r25, int r26, int r27, int r28, int r29, int r30, int r31, String r32) {
        Context r2 = r18.getApplicationContext();
        this.context = r2;
        this.channelId = r19;
        this.notificationId = r20;
        this.mediaDescriptionAdapter = r21;
        this.notificationListener = r22;
        this.customActionReceiver = r23;
        this.smallIconResourceId = r24;
        this.groupKey = r32;
        int r4 = instanceIdCounter;
        instanceIdCounter = r4 + 1;
        this.instanceId = r4;
        this.mainHandler = Util.createHandler(Looper.getMainLooper(), new PlayerNotificationManager$$ExternalSyntheticLambda0(this));
        this.notificationManager = NotificationManagerCompat.from(r2);
        C04651 r6 = null;
        this.playerListener = new PlayerListener(this, r6);
        this.notificationBroadcastReceiver = new NotificationBroadcastReceiver(this, r6);
        this.intentFilter = new IntentFilter();
        this.usePreviousAction = true;
        this.useNextAction = true;
        this.usePlayPauseActions = true;
        this.showPlayButtonIfSuppressed = true;
        this.useRewindAction = true;
        this.useFastForwardAction = true;
        this.colorized = true;
        this.useChronometer = true;
        this.color = 0;
        this.defaults = 0;
        this.priority = -1;
        this.badgeIconType = 1;
        this.visibility = 1;
        this.playbackActions = createPlaybackActions(r2, this.instanceId, r25, r26, r27, r28, r29, r30, r31);
        Iterator<String> r3 = this.playbackActions.keySet().iterator();
    L4:
        if (r3.hasNext() == false) goto L7;
        String r5 = r3.next();
        this.intentFilter.addAction(r5);
        goto L4
    L7:
        if (r23 == null) goto L9;
        Map<String, NotificationCompat.Action> r7 = r23.createCustomActions(r2, this.instanceId);
    L10:
        this.customActions = r7;
        Iterator<String> r8 = this.customActions.keySet().iterator();
    L12:
        if (r8.hasNext() == false) goto L14;
        String r9 = r8.next();
        this.intentFilter.addAction(r9);
        goto L12
    L14:
        this.dismissPendingIntent = createBroadcastIntent(ACTION_DISMISS, r2, this.instanceId);
        this.intentFilter.addAction(ACTION_DISMISS);
        return;
    L9:
        r7 = Collections.emptyMap();
        goto L10
    }

    static /* synthetic */ void access$000(PlayerNotificationManager r0, Bitmap r1, int r2) {
        r0.postUpdateNotificationBitmap(r1, r2);
    }

    static /* synthetic */ Map access$1000(PlayerNotificationManager r1) {
        return r1.customActions;
    }

    static /* synthetic */ void access$400(PlayerNotificationManager r0) {
        r0.postStartOrUpdateNotification();
    }

    static /* synthetic */ Player access$500(PlayerNotificationManager r1) {
        return r1.player;
    }

    static /* synthetic */ boolean access$600(PlayerNotificationManager r1) {
        return r1.isNotificationStarted;
    }

    static /* synthetic */ int access$700(PlayerNotificationManager r1) {
        return r1.instanceId;
    }

    static /* synthetic */ void access$800(PlayerNotificationManager r0, boolean r1) {
        r0.stopNotification(r1);
    }

    static /* synthetic */ CustomActionReceiver access$900(PlayerNotificationManager r1) {
        return r1.customActionReceiver;
    }

    private static PendingIntent createBroadcastIntent(String r3, Context r4, int r5) {
        Intent r0 = new Intent(r3).setPackage(r4.getPackageName());
        r0.putExtra(EXTRA_INSTANCE_ID, r5);
        if (Util.SDK_INT < 23) goto L5;
        int r1 = 201326592;
    L7:
        return PendingIntent.getBroadcast(r4, r5, r0, r1);
    L5:
        r1 = C0180C.BUFFER_FLAG_FIRST_SAMPLE;
        goto L7
    }

    private static Map<String, NotificationCompat.Action> createPlaybackActions(Context r5, int r6, int r7, int r8, int r9, int r10, int r11, int r12, int r13) {
        HashMap r0 = new HashMap();
        r0.put(ACTION_PLAY, new NotificationCompat.Action(r7, r5.getString(C0467R.string.exo_controls_play_description), createBroadcastIntent(ACTION_PLAY, r5, r6)));
        r0.put(ACTION_PAUSE, new NotificationCompat.Action(r8, r5.getString(C0467R.string.exo_controls_pause_description), createBroadcastIntent(ACTION_PAUSE, r5, r6)));
        r0.put(ACTION_STOP, new NotificationCompat.Action(r9, r5.getString(C0467R.string.exo_controls_stop_description), createBroadcastIntent(ACTION_STOP, r5, r6)));
        r0.put(ACTION_REWIND, new NotificationCompat.Action(r10, r5.getString(C0467R.string.exo_controls_rewind_description), createBroadcastIntent(ACTION_REWIND, r5, r6)));
        r0.put(ACTION_FAST_FORWARD, new NotificationCompat.Action(r11, r5.getString(C0467R.string.exo_controls_fastforward_description), createBroadcastIntent(ACTION_FAST_FORWARD, r5, r6)));
        r0.put(ACTION_PREVIOUS, new NotificationCompat.Action(r12, r5.getString(C0467R.string.exo_controls_previous_description), createBroadcastIntent(ACTION_PREVIOUS, r5, r6)));
        r0.put(ACTION_NEXT, new NotificationCompat.Action(r13, r5.getString(C0467R.string.exo_controls_next_description), createBroadcastIntent(ACTION_NEXT, r5, r6)));
        return r0;
    }

    private boolean handleMessage(Message r3) {
        switch(r3.what) {
            case 1: goto L14;
            case 2: goto L7;
            default: goto L4;
        };
    L4:
        return false;
    L7:
        if (this.player != null) goto L9;
        return true;
    L9:
        if (this.isNotificationStarted == true) goto L11;
        return true;
    L11:
        if (this.currentNotificationTag != r3.arg1) goto L20;
        startOrUpdateNotification(this.player, (Bitmap) r3.obj);
        return true;
    L20:
        return true;
    L14:
        if (this.player == null) goto L22;
        startOrUpdateNotification(this.player, null);
        return true;
    L22:
        return true;
    }

    private void postStartOrUpdateNotification() {
        if (this.mainHandler.hasMessages(1) == true) goto L6;
        this.mainHandler.sendEmptyMessage(1);
        return;
    }

    private void postUpdateNotificationBitmap(Bitmap r4, int r5) {
        this.mainHandler.obtainMessage(2, r5, -1, r4).sendToTarget();
    }

    private static void setLargeIcon(NotificationCompat.Builder r0, Bitmap r1) {
        r0.setLargeIcon(r1);
    }

    private void startOrUpdateNotification(Player r8, Bitmap r9) {
        boolean r0 = getOngoing(r8);
        this.builder = createNotification(r8, this.builder, r0, r9);
        boolean r2 = false;
        if (this.builder != null) goto L6;
        stopNotification(false);
        return;
    L6:
        Notification r1 = this.builder.build();
        this.notificationManager.notify(this.notificationId, r1);
        if (this.isNotificationStarted == true) goto L10;
        Util.registerReceiverNotExported(this.context, this.notificationBroadcastReceiver, this.intentFilter);
    L10:
        if (this.notificationListener == null) goto L17;
        NotificationListener r3 = this.notificationListener;
        int r5 = this.notificationId;
        if (r0 == false) goto L14;
    L15:
        r2 = true;
    L16:
        r3.onNotificationPosted(r5, r1, r2);
        goto L17
    L14:
        if (this.isNotificationStarted == true) goto L16;
    L17:
        this.isNotificationStarted = true;
    }

    private void stopNotification(boolean r3) {
        if (this.isNotificationStarted == false) goto L8;
        this.isNotificationStarted = false;
        this.mainHandler.removeMessages(1);
        this.notificationManager.cancel(this.notificationId);
        this.context.unregisterReceiver(this.notificationBroadcastReceiver);
        if (this.notificationListener == null) goto L9;
        this.notificationListener.onNotificationCancelled(this.notificationId, r3);
        return;
    L9:
        return;
    }

    protected NotificationCompat.Builder createNotification(Player r10, NotificationCompat.Builder r11, boolean r12, Bitmap r13) {
        C04651 r1 = null;
        if (r10.getPlaybackState() == 1) goto L5;
    L10:
        List<String> r0 = getActions(r10);
        ArrayList r3 = new ArrayList(r0.size());
        int r4 = 0;
    L12:
        if (r4 >= r0.size()) goto L21;
        String r5 = r0.get(r4);
        if (this.playbackActions.containsKey(r5) == false) goto L16;
        NotificationCompat.Action r6 = this.playbackActions.get(r5);
    L18:
        if (r6 == null) goto L20;
        r3.add(r6);
    L20:
        r4 = r4 + 1;
        goto L12
    L16:
        r6 = this.customActions.get(r5);
        goto L18
    L21:
        if (r11 != null) goto L23;
    L24:
        r11 = new NotificationCompat.Builder(this.context, this.channelId);
        this.builderActions = r3;
        int r7 = 0;
    L26:
        if (r7 >= r3.size()) goto L28;
        r11.addAction((NotificationCompat.Action) r3.get(r7));
        r7 = r7 + 1;
    L28:
        int[] r8 = getActionIndicesForCompactView(r0, r10);
        r11.setStyle(new MediaStyle(this.mediaSessionToken, r8));
        r11.setDeleteIntent(this.dismissPendingIntent);
        r11.setBadgeIconType(this.badgeIconType).setOngoing(r12).setColor(this.color).setColorized(this.colorized).setSmallIcon(this.smallIconResourceId).setVisibility(this.visibility).setPriority(this.priority).setDefaults(this.defaults);
        if (this.useChronometer == true) goto L31;
    L41:
        r11.setShowWhen(false).setUsesChronometer(false);
    L42:
        r11.setContentTitle(this.mediaDescriptionAdapter.getCurrentContentTitle(r10));
        r11.setContentText(this.mediaDescriptionAdapter.getCurrentContentText(r10));
        r11.setSubText(this.mediaDescriptionAdapter.getCurrentSubText(r10));
        if (r13 != null) goto L45;
        MediaDescriptionAdapter r9 = this.mediaDescriptionAdapter;
        int r14 = this.currentNotificationTag + 1;
        this.currentNotificationTag = r14;
        r13 = r9.getCurrentLargeIcon(r10, new BitmapCallback(this, r14, r1));
    L45:
        setLargeIcon(r11, r13);
        r11.setContentIntent(this.mediaDescriptionAdapter.createCurrentContentIntent(r10));
        if (this.groupKey == null) goto L48;
        r11.setGroup(this.groupKey);
    L48:
        r11.setOnlyAlertOnce(true);
        return r11;
    L31:
        if (r10.isCommandAvailable(16) == false) goto L41;
        if (r10.isPlaying() == false) goto L41;
        if (r10.isPlayingAd() == true) goto L41;
        if (r10.isCurrentMediaItemDynamic() == true) goto L41;
        if (r10.getPlaybackParameters().speed != 1.0f) goto L41;
        r11.setWhen(System.currentTimeMillis() - r10.getContentPosition()).setShowWhen(true).setUsesChronometer(true);
        goto L42
    L23:
        if (r3.equals(this.builderActions) == true) goto L28;
    L5:
        if (r10.isCommandAvailable(17) == false) goto L10;
        if (r10.getCurrentTimeline().isEmpty() == false) goto L10;
        this.builderActions = null;
        return null;
    }

    protected int[] getActionIndicesForCompactView(List<String> r10, Player r11) {
        int r0 = r10.indexOf(ACTION_PAUSE);
        int r1 = r10.indexOf(ACTION_PLAY);
        if (this.usePreviousActionInCompactView == false) goto L6;
        int r2 = r10.indexOf(ACTION_PREVIOUS);
    L10:
        if (this.useNextActionInCompactView == false) goto L13;
        int r4 = r10.indexOf(ACTION_NEXT);
    L16:
        int[] r5 = new int[3];
        int r6 = 0;
        if (r2 == (-1)) goto L19;
        r5[0] = r2;
        r6 = 0 + 1;
    L19:
        boolean r7 = Util.shouldShowPlayButton(r11, this.showPlayButtonIfSuppressed);
        if (r0 == (-1)) goto L23;
        if (r7 == true) goto L23;
        r5[r6] = r0;
        r6 = r6 + 1;
    L26:
        if (r4 == (-1)) goto L29;
        r5[r6] = r4;
        r6 = r6 + 1;
    L29:
        return Arrays.copyOf(r5, r6);
    L23:
        if (r1 == (-1)) goto L26;
        if (r7 == false) goto L26;
        r5[r6] = r1;
        r6 = r6 + 1;
        goto L26
    L13:
        if (this.useFastForwardActionInCompactView == false) goto L15;
        r4 = r10.indexOf(ACTION_FAST_FORWARD);
        goto L16
    L15:
        r4 = -1;
        goto L16
    L6:
        if (this.useRewindActionInCompactView == false) goto L8;
        r2 = r10.indexOf(ACTION_REWIND);
        goto L10
    L8:
        r2 = -1;
        goto L10
    }

    protected List<String> getActions(Player r7) {
        boolean r0 = r7.isCommandAvailable(7);
        boolean r1 = r7.isCommandAvailable(11);
        boolean r2 = r7.isCommandAvailable(12);
        boolean r3 = r7.isCommandAvailable(9);
        ArrayList r4 = new ArrayList();
        if (this.usePreviousAction == false) goto L7;
        if (r0 == false) goto L7;
        r4.add(ACTION_PREVIOUS);
    L7:
        if (this.useRewindAction == false) goto L11;
        if (r1 == false) goto L11;
        r4.add(ACTION_REWIND);
    L11:
        if (this.usePlayPauseActions == false) goto L17;
        if (Util.shouldShowPlayButton(r7, this.showPlayButtonIfSuppressed) == false) goto L15;
        r4.add(ACTION_PLAY);
        goto L17
    L15:
        r4.add(ACTION_PAUSE);
    L17:
        if (this.useFastForwardAction == false) goto L21;
        if (r2 == false) goto L21;
        r4.add(ACTION_FAST_FORWARD);
    L21:
        if (this.useNextAction == false) goto L25;
        if (r3 == false) goto L25;
        r4.add(ACTION_NEXT);
    L25:
        if (this.customActionReceiver == null) goto L28;
        r4.addAll(this.customActionReceiver.getCustomActions(r7));
    L28:
        if (this.useStopAction == false) goto L30;
        r4.add(ACTION_STOP);
    L30:
        return r4;
    }

    protected boolean getOngoing(Player r3) {
        int r0 = r3.getPlaybackState();
        if (r0 == 2) goto L7;
        if (r0 == 3) goto L7;
    L9:
        return false;
    L7:
        if (r3.getPlayWhenReady() == false) goto L9;
        return true;
    }

    public final void invalidate() {
        if (this.isNotificationStarted == false) goto L6;
        postStartOrUpdateNotification();
        return;
    }

    public final void setBadgeIconType(int r2) {
        if (this.badgeIconType != r2) goto L5;
        return;
    L5:
        switch(r2) {
            case 0: goto L8;
            case 1: goto L8;
            case 2: goto L8;
            default: goto L7;
        };
    L8:
        this.badgeIconType = r2;
        invalidate();
        return;
    L7:
        throw new IllegalArgumentException();
    }

    public final void setColor(int r2) {
        if (this.color == r2) goto L6;
        this.color = r2;
        invalidate();
        return;
    }

    public final void setColorized(boolean r2) {
        if (this.colorized == r2) goto L6;
        this.colorized = r2;
        invalidate();
        return;
    }

    public final void setDefaults(int r2) {
        if (this.defaults == r2) goto L6;
        this.defaults = r2;
        invalidate();
        return;
    }

    public final void setMediaSessionToken(MediaSession.Token r2) {
        if (Util.areEqual(this.mediaSessionToken, r2) == true) goto L6;
        this.mediaSessionToken = r2;
        invalidate();
        return;
    }

    @Deprecated
    public final void setMediaSessionToken(MediaSessionCompat.Token r2) {
        setMediaSessionToken((MediaSession.Token) r2.getToken());
    }

    public final void setPlayer(Player r5) {
        boolean r2 = true;
        if (Looper.myLooper() != Looper.getMainLooper()) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        if (r5 != null) goto L9;
    L12:
        Assertions.checkArgument(r2);
        if (this.player != r5) goto L16;
        return;
    L16:
        if (this.player == null) goto L20;
        this.player.removeListener(this.playerListener);
        if (r5 != null) goto L20;
        stopNotification(false);
    L20:
        this.player = r5;
        if (r5 == null) goto L24;
        r5.addListener(this.playerListener);
        postStartOrUpdateNotification();
        return;
    L24:
        return;
    L9:
        if (r5.getApplicationLooper() == Looper.getMainLooper()) goto L12;
        r2 = false;
        goto L12
    L5:
        r0 = false;
        goto L6
    }

    public final void setPriority(int r2) {
        if (this.priority != r2) goto L5;
        return;
    L5:
        switch(r2) {
            case -2: goto L8;
            case -1: goto L8;
            case 0: goto L8;
            case 1: goto L8;
            case 2: goto L8;
            default: goto L7;
        };
    L8:
        this.priority = r2;
        invalidate();
        return;
    L7:
        throw new IllegalArgumentException();
    }

    public void setShowPlayButtonIfPlaybackIsSuppressed(boolean r2) {
        if (this.showPlayButtonIfSuppressed == r2) goto L6;
        this.showPlayButtonIfSuppressed = r2;
        invalidate();
        return;
    }

    public final void setSmallIcon(int r2) {
        if (this.smallIconResourceId == r2) goto L6;
        this.smallIconResourceId = r2;
        invalidate();
        return;
    }

    public final void setUseChronometer(boolean r2) {
        if (this.useChronometer == r2) goto L6;
        this.useChronometer = r2;
        invalidate();
        return;
    }

    public final void setUseFastForwardAction(boolean r2) {
        if (this.useFastForwardAction == r2) goto L6;
        this.useFastForwardAction = r2;
        invalidate();
        return;
    }

    public final void setUseFastForwardActionInCompactView(boolean r2) {
        if (this.useFastForwardActionInCompactView == r2) goto L9;
        this.useFastForwardActionInCompactView = r2;
        if (r2 == false) goto L7;
        this.useNextActionInCompactView = false;
    L7:
        invalidate();
        return;
    }

    public final void setUseNextAction(boolean r2) {
        if (this.useNextAction == r2) goto L6;
        this.useNextAction = r2;
        invalidate();
        return;
    }

    public final void setUseNextActionInCompactView(boolean r2) {
        if (this.useNextActionInCompactView == r2) goto L9;
        this.useNextActionInCompactView = r2;
        if (r2 == false) goto L7;
        this.useFastForwardActionInCompactView = false;
    L7:
        invalidate();
        return;
    }

    public final void setUsePlayPauseActions(boolean r2) {
        if (this.usePlayPauseActions == r2) goto L6;
        this.usePlayPauseActions = r2;
        invalidate();
        return;
    }

    public final void setUsePreviousAction(boolean r2) {
        if (this.usePreviousAction == r2) goto L6;
        this.usePreviousAction = r2;
        invalidate();
        return;
    }

    public final void setUsePreviousActionInCompactView(boolean r2) {
        if (this.usePreviousActionInCompactView == r2) goto L9;
        this.usePreviousActionInCompactView = r2;
        if (r2 == false) goto L7;
        this.useRewindActionInCompactView = false;
    L7:
        invalidate();
        return;
    }

    public final void setUseRewindAction(boolean r2) {
        if (this.useRewindAction == r2) goto L6;
        this.useRewindAction = r2;
        invalidate();
        return;
    }

    public final void setUseRewindActionInCompactView(boolean r2) {
        if (this.useRewindActionInCompactView == r2) goto L9;
        this.useRewindActionInCompactView = r2;
        if (r2 == false) goto L7;
        this.usePreviousActionInCompactView = false;
    L7:
        invalidate();
        return;
    }

    public final void setUseStopAction(boolean r2) {
        if (this.useStopAction != r2) goto L5;
        return;
    L5:
        this.useStopAction = r2;
        invalidate();
    }

    public final void setVisibility(int r2) {
        if (this.visibility != r2) goto L5;
        return;
    L5:
        switch(r2) {
            case -1: goto L8;
            case 0: goto L8;
            case 1: goto L8;
            default: goto L7;
        };
    L8:
        this.visibility = r2;
        invalidate();
        return;
    L7:
        throw new IllegalStateException();
    }
}
