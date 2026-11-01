package androidx.media3.p002ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.PowerManager;
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
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.SystemClock;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.p001ts.TsExtractor;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public final class WearUnsuitableOutputPlaybackSuppressionResolverListener implements Player.Listener {
    public static final long DEFAULT_PLAYBACK_SUPPRESSION_AUTO_RESUME_TIMEOUT_MS = 0;
    private static final String EXTRA_BLUETOOTH_SETTINGS_CLOSE_ON_CONNECT = "EXTRA_CLOSE_ON_CONNECT";
    private static final String EXTRA_BLUETOOTH_SETTINGS_CONNECTION_ONLY = "EXTRA_CONNECTION_ONLY";
    private static final String EXTRA_BLUETOOTH_SETTINGS_FILTER_TYPE = "android.bluetooth.devicepicker.extra.FILTER_TYPE";
    private static final String EXTRA_OUTPUT_SWITCHER_PACKAGE_NAME = "com.android.settings.panel.extra.PACKAGE_NAME";
    private static final int FILTER_TYPE_AUDIO = 1;
    private static final String OUTPUT_SWITCHER_INTENT_ACTION_NAME = "com.android.settings.panel.action.MEDIA_OUTPUT";
    private static final String WAKE_LOCK_TAG = "WearUnsuitableOutputPlaybackSuppressionResolverListener:WakeLock";
    private final Context applicationContext;
    private final long autoResumeTimeoutAfterUnsuitableOutputSuppressionMs;
    private final Clock clock;
    private long unsuitableOutputPlaybackSuppressionStartRealtimeMs;
    private final PowerManager.WakeLock wakeLock;

    static {
        DEFAULT_PLAYBACK_SUPPRESSION_AUTO_RESUME_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(5);
    }

    public WearUnsuitableOutputPlaybackSuppressionResolverListener(Context r3) {
        this(r3, DEFAULT_PLAYBACK_SUPPRESSION_AUTO_RESUME_TIMEOUT_MS);
    }

    public WearUnsuitableOutputPlaybackSuppressionResolverListener(Context r2, long r3) {
        this(r2, r3, SystemClock.DEFAULT);
    }

    WearUnsuitableOutputPlaybackSuppressionResolverListener(Context r6, long r7, Clock r9) {
        if (r7 < 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        this.applicationContext = r6.getApplicationContext();
        this.autoResumeTimeoutAfterUnsuitableOutputSuppressionMs = r7;
        this.clock = r9;
        this.unsuitableOutputPlaybackSuppressionStartRealtimeMs = C0180C.TIME_UNSET;
        PowerManager.WakeLock r1 = null;
        PowerManager r3 = (PowerManager) this.applicationContext.getSystemService("power");
        if (r3 == null) goto L9;
        r1 = r3.newWakeLock(1, WAKE_LOCK_TAG);
        r1.setReferenceCounted(false);
    L9:
        this.wakeLock = r1;
        return;
    L5:
        r0 = false;
        goto L6
    }

    private static ComponentName getSystemOrSystemUpdatedAppComponent(Context r9, Intent r10) {
        Iterator<ResolveInfo> r2 = r9.getPackageManager().queryIntentActivities(r10, 0).iterator();
    L4:
        if (r2.hasNext() == false) goto L15;
        ActivityInfo r4 = r2.next().activityInfo;
        if (r4 == null) goto L4;
        if (r4.applicationInfo == null) goto L4;
        if ((r4.applicationInfo.flags & TsExtractor.TS_STREAM_TYPE_AC3) == 0) goto L4;
        return new ComponentName(r4.packageName, r4.name);
    L15:
        return null;
    }

    private static void launchSystemMediaOutputSwitcherUi(Context r5) {
        Intent r0 = new Intent(OUTPUT_SWITCHER_INTENT_ACTION_NAME).addFlags(268435456).putExtra(EXTRA_OUTPUT_SWITCHER_PACKAGE_NAME, r5.getPackageName());
        ComponentName r1 = getSystemOrSystemUpdatedAppComponent(r5, r0);
        if (r1 == null) goto L5;
        r0.setComponent(r1);
        r5.startActivity(r0);
        return;
    L5:
        Intent r2 = new Intent("android.settings.BLUETOOTH_SETTINGS").addFlags(268468224).putExtra(EXTRA_BLUETOOTH_SETTINGS_CLOSE_ON_CONNECT, true).putExtra(EXTRA_BLUETOOTH_SETTINGS_CONNECTION_ONLY, true).putExtra(EXTRA_BLUETOOTH_SETTINGS_FILTER_TYPE, 1);
        ComponentName r3 = getSystemOrSystemUpdatedAppComponent(r5, r2);
        if (r3 == null) goto L10;
        r2.setComponent(r3);
        r5.startActivity(r2);
        return;
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
    public void onEvents(Player r7, Player.Events r8) {
        if (Util.isWear(this.applicationContext) == true) goto L6;
        return;
    L6:
        if (r8.contains(6) == true) goto L10;
        if (r8.contains(5) == true) goto L10;
    L22:
        if (r8.contains(6) == true) goto L24;
        return;
    L24:
        if (r7.getPlaybackSuppressionReason() == 0) goto L26;
        return;
    L26:
        if (this.unsuitableOutputPlaybackSuppressionStartRealtimeMs != C0180C.TIME_UNSET) goto L28;
        return;
    L28:
        if ((this.clock.elapsedRealtime() - this.unsuitableOutputPlaybackSuppressionStartRealtimeMs) >= this.autoResumeTimeoutAfterUnsuitableOutputSuppressionMs) goto L38;
        this.unsuitableOutputPlaybackSuppressionStartRealtimeMs = C0180C.TIME_UNSET;
        r7.play();
        if (this.wakeLock == null) goto L39;
        this.wakeLock.release();
        return;
    L39:
        return;
    L38:
        return;
    L10:
        if (r7.getPlayWhenReady() == false) goto L22;
        if (r7.getPlaybackSuppressionReason() != 3) goto L22;
        r7.pause();
        this.unsuitableOutputPlaybackSuppressionStartRealtimeMs = this.clock.elapsedRealtime();
        if (this.wakeLock == null) goto L19;
        if (this.wakeLock.isHeld() == true) goto L19;
        this.wakeLock.acquire(this.autoResumeTimeoutAfterUnsuitableOutputSuppressionMs);
    L19:
        if (r8.contains(5) == false) goto L33;
        launchSystemMediaOutputSwitcherUi(this.applicationContext);
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
