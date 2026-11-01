package androidx.media3.exoplayer.analytics;

import android.content.Context;
import android.media.DeniedByServerException;
import android.media.MediaCodec;
import android.media.MediaDrm;
import android.media.MediaDrmResetException;
import android.media.NotProvisionedException;
import android.media.metrics.LogSessionId;
import android.media.metrics.MediaMetricsManager;
import android.media.metrics.NetworkEvent;
import android.media.metrics.PlaybackErrorEvent;
import android.media.metrics.PlaybackMetrics;
import android.media.metrics.PlaybackSession;
import android.media.metrics.PlaybackStateEvent;
import android.media.metrics.TrackChangeEvent;
import android.os.SystemClock;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.util.Pair;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C0180C;
import androidx.media3.common.DeviceInfo;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaLibraryInfo;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.media3.common.ParserException;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.Tracks;
import androidx.media3.common.VideoSize;
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.NetworkTypeObserver;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.FileDataSource;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.datasource.UdpDataSource;
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.DecoderReuseEvaluation;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.audio.AudioSink;
import androidx.media3.exoplayer.drm.DefaultDrmSessionManager;
import androidx.media3.exoplayer.drm.DrmSession;
import androidx.media3.exoplayer.drm.UnsupportedDrmException;
import androidx.media3.exoplayer.mediacodec.MediaCodecDecoderException;
import androidx.media3.exoplayer.mediacodec.MediaCodecRenderer;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MediaLoadData;
import androidx.media3.exoplayer.source.MediaSource;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class MediaMetricsListener implements AnalyticsListener, PlaybackSessionManager.Listener {
    private String activeSessionId;
    private int audioUnderruns;
    private final HashMap<String, Long> bandwidthBytes;
    private final HashMap<String, Long> bandwidthTimeMs;
    private final Context context;
    private Format currentAudioFormat;
    private int currentNetworkType;
    private int currentPlaybackState;
    private Format currentTextFormat;
    private Format currentVideoFormat;
    private int discontinuityReason;
    private int droppedFrames;
    private boolean hasFatalError;
    private int ioErrorType;
    private boolean isSeeking;
    private PlaybackMetrics.Builder metricsBuilder;
    private PendingFormatUpdate pendingAudioFormat;
    private PlaybackException pendingPlayerError;
    private PendingFormatUpdate pendingTextFormat;
    private PendingFormatUpdate pendingVideoFormat;
    private final Timeline.Period period;
    private final PlaybackSession playbackSession;
    private int playedFrames;
    private boolean reportedEventsForCurrentSession;
    private final PlaybackSessionManager sessionManager;
    private final long startTimeMs;
    private final Timeline.Window window;

    private static final class ErrorInfo {
        public final int errorCode;
        public final int subErrorCode;

        public ErrorInfo(int r1, int r2) {
            this.errorCode = r1;
            this.subErrorCode = r2;
        }
    }

    private static final class PendingFormatUpdate {
        public final Format format;
        public final int selectionReason;
        public final String sessionId;

        public PendingFormatUpdate(Format r1, int r2, String r3) {
            this.format = r1;
            this.selectionReason = r2;
            this.sessionId = r3;
        }
    }

    private MediaMetricsListener(Context r3, PlaybackSession r4) {
        this.context = r3.getApplicationContext();
        this.playbackSession = r4;
        this.window = new Timeline.Window();
        this.period = new Timeline.Period();
        this.bandwidthBytes = new HashMap();
        this.bandwidthTimeMs = new HashMap();
        this.startTimeMs = SystemClock.elapsedRealtime();
        this.currentPlaybackState = 0;
        this.currentNetworkType = 0;
        this.sessionManager = new DefaultPlaybackSessionManager();
        this.sessionManager.setListener(this);
    }

    @EnsuresNonNullIf(expression = {"#1"}, result = true)
    private boolean canReportPendingFormatUpdate(PendingFormatUpdate r3) {
        if (r3 != null) goto L4;
    L6:
        return false;
    L4:
        if (r3.sessionId.equals(this.sessionManager.getActiveSessionId()) == false) goto L6;
        return true;
    }

    public static MediaMetricsListener create(Context r3) {
        MediaMetricsManager r0 = (MediaMetricsManager) r3.getSystemService("media_metrics");
        if (r0 != null) goto L6;
        return null;
    L6:
        return new MediaMetricsListener(r3, r0.createPlaybackSession());
    }

    private void finishCurrentSession() {
        if (this.metricsBuilder != null) goto L5;
    L21:
        this.metricsBuilder = null;
        this.activeSessionId = null;
        this.audioUnderruns = 0;
        this.droppedFrames = 0;
        this.playedFrames = 0;
        this.currentVideoFormat = null;
        this.currentAudioFormat = null;
        this.currentTextFormat = null;
        this.reportedEventsForCurrentSession = false;
        return;
    L5:
        if (this.reportedEventsForCurrentSession == false) goto L21;
        this.metricsBuilder.setAudioUnderrunCount(this.audioUnderruns);
        this.metricsBuilder.setVideoFramesDropped(this.droppedFrames);
        this.metricsBuilder.setVideoFramesPlayed(this.playedFrames);
        Long r0 = this.bandwidthTimeMs.get(this.activeSessionId);
        PlaybackMetrics.Builder r2 = this.metricsBuilder;
        if (r0 != null) goto L9;
        long r5 = 0;
    L10:
        r2.setNetworkTransferDurationMillis(r5);
        Long r3 = this.bandwidthBytes.get(this.activeSessionId);
        PlaybackMetrics.Builder r6 = this.metricsBuilder;
        if (r3 != null) goto L13;
        long r7 = 0;
    L14:
        r6.setNetworkBytesRead(r7);
        PlaybackMetrics.Builder r8 = this.metricsBuilder;
        if (r3 != null) goto L17;
    L19:
        int r4 = 0;
    L20:
        r8.setStreamSource(r4);
        this.playbackSession.reportPlaybackMetrics(this.metricsBuilder.build());
        goto L21
    L17:
        if (r3.longValue() <= 0) goto L19;
        r4 = 1;
        goto L20
    L13:
        r7 = r3.longValue();
        goto L14
    L9:
        r5 = r0.longValue();
        goto L10
    }

    private static int getDrmErrorCode(int r1) {
        switch(Util.getErrorCodeForMediaDrmErrorCode(r1)) {
            case 6002: goto L12;
            case 6003: goto L10;
            case 6004: goto L8;
            case 6005: goto L6;
            default: goto L4;
        };
    L4:
        return 27;
    L6:
        return 26;
    L8:
        return 25;
    L10:
        return 28;
    L12:
        return 24;
    }

    private static DrmInitData getDrmInitData(ImmutableList<Tracks.Group> r4) {
        UnmodifiableIterator<Tracks.Group> r0 = r4.iterator();
    L4:
        if (r0.hasNext() == false) goto L15;
        Tracks.Group r1 = r0.next();
        int r2 = 0;
    L7:
        if (r2 >= r1.length) goto L4;
        if (r1.isTrackSelected(r2) == false) goto L13;
        DrmInitData r3 = r1.getTrackFormat(r2).drmInitData;
        if (r3 == null) goto L13;
        return r3;
    L13:
        r2 = r2 + 1;
        goto L7
    L15:
        return null;
    }

    private static int getDrmType(DrmInitData r3) {
        int r0 = 0;
    L4:
        if (r0 >= r3.schemeDataCount) goto L18;
        UUID r1 = r3.get(r0).uuid;
        if (r1.equals(C0180C.WIDEVINE_UUID) == true) goto L7;
        if (r1.equals(C0180C.PLAYREADY_UUID) == true) goto L11;
        if (r1.equals(C0180C.CLEARKEY_UUID) == true) goto L15;
        r0 = r0 + 1;
        goto L4
    L15:
        return 6;
    L11:
        return 2;
    L7:
        return 3;
    L18:
        return 1;
    }

    private static ErrorInfo getErrorInfo(PlaybackException r8, Context r9, boolean r10) {
        if (r8.errorCode == 1001) goto L5;
        boolean r0 = false;
        int r1 = 0;
        if ((r8 instanceof ExoPlaybackException) == false) goto L13;
        ExoPlaybackException r3 = (ExoPlaybackException) r8;
        if (r3.type != 1) goto L11;
        boolean r5 = true;
    L12:
        r0 = r5;
        r1 = r3.rendererFormatSupport;
        goto L13
    L11:
        r5 = false;
    L13:
        Throwable r4 = (Throwable) Assertions.checkNotNull(r8.getCause());
        if ((r4 instanceof IOException) == true) goto L16;
        if (r0 == false) goto L108;
        if (r1 == 0) goto L107;
        if (r1 != 1) goto L108;
    L107:
        return new ErrorInfo(35, 0);
    L108:
        if (r0 == false) goto L112;
        if (r1 != 3) goto L112;
        return new ErrorInfo(15, 0);
    L112:
        if (r0 == false) goto L118;
        if (r1 != 2) goto L118;
        return new ErrorInfo(23, 0);
    L118:
        if ((r4 instanceof MediaCodecRenderer.DecoderInitializationException) == false) goto L122;
        return new ErrorInfo(13, Util.getErrorCodeFromPlatformDiagnosticsInfo(((MediaCodecRenderer.DecoderInitializationException) r4).diagnosticInfo));
    L122:
        if ((r4 instanceof MediaCodecDecoderException) == false) goto L126;
        return new ErrorInfo(14, ((MediaCodecDecoderException) r4).errorCode);
    L126:
        if ((r4 instanceof OutOfMemoryError) == false) goto L130;
        return new ErrorInfo(14, 0);
    L130:
        if ((r4 instanceof AudioSink.InitializationException) == false) goto L134;
        return new ErrorInfo(17, ((AudioSink.InitializationException) r4).audioTrackState);
    L134:
        if ((r4 instanceof AudioSink.WriteException) == false) goto L138;
        return new ErrorInfo(18, ((AudioSink.WriteException) r4).errorCode);
    L138:
        if ((r4 instanceof MediaCodec.CryptoException) == false) goto L142;
        int r2 = ((MediaCodec.CryptoException) r4).getErrorCode();
        return new ErrorInfo(getDrmErrorCode(r2), r2);
    L142:
        return new ErrorInfo(22, 0);
    L16:
        if ((r4 instanceof HttpDataSource.InvalidResponseCodeException) == false) goto L20;
        return new ErrorInfo(5, ((HttpDataSource.InvalidResponseCodeException) r4).responseCode);
    L20:
        if ((r4 instanceof HttpDataSource.InvalidContentTypeException) == true) goto L98;
        if ((r4 instanceof ParserException) == true) goto L98;
        if ((r4 instanceof HttpDataSource.HttpDataSourceException) == true) goto L78;
        if ((r4 instanceof UdpDataSource.UdpDataSourceException) == true) goto L78;
        if (r8.errorCode != 1002) goto L34;
        return new ErrorInfo(21, 0);
    L34:
        if ((r4 instanceof DrmSession.DrmSessionException) == false) goto L64;
        Throwable r6 = (Throwable) Assertions.checkNotNull(r4.getCause());
        if ((r6 instanceof MediaDrm.MediaDrmStateException) == false) goto L40;
        int r7 = Util.getErrorCodeFromPlatformDiagnosticsInfo(((MediaDrm.MediaDrmStateException) r6).getDiagnosticInfo());
        return new ErrorInfo(getDrmErrorCode(r7), r7);
    L40:
        if (Util.SDK_INT < 23) goto L46;
        if ((r6 instanceof MediaDrmResetException) == false) goto L46;
        return new ErrorInfo(27, 0);
    L46:
        if ((r6 instanceof NotProvisionedException) == false) goto L50;
        return new ErrorInfo(24, 0);
    L50:
        if ((r6 instanceof DeniedByServerException) == false) goto L54;
        return new ErrorInfo(29, 0);
    L54:
        if ((r6 instanceof UnsupportedDrmException) == false) goto L58;
        return new ErrorInfo(23, 0);
    L58:
        if ((r6 instanceof DefaultDrmSessionManager.MissingSchemeDataException) == false) goto L62;
        return new ErrorInfo(28, 0);
    L62:
        return new ErrorInfo(30, 0);
    L64:
        if ((r4 instanceof FileDataSource.FileDataSourceException) == false) goto L76;
        if ((r4.getCause() instanceof FileNotFoundException) == false) goto L76;
        Throwable r11 = ((Throwable) Assertions.checkNotNull(r4.getCause())).getCause();
        if ((r11 instanceof ErrnoException) == false) goto L74;
        if (((ErrnoException) r11).errno != OsConstants.EACCES) goto L74;
        return new ErrorInfo(32, 0);
    L74:
        return new ErrorInfo(31, 0);
    L76:
        return new ErrorInfo(9, 0);
    L78:
        if (NetworkTypeObserver.getInstance(r9).getNetworkType() == 1) goto L80;
        Throwable r12 = r4.getCause();
        if ((r12 instanceof UnknownHostException) == false) goto L86;
        return new ErrorInfo(6, 0);
    L86:
        if ((r12 instanceof SocketTimeoutException) == false) goto L90;
        return new ErrorInfo(7, 0);
    L90:
        if ((r4 instanceof HttpDataSource.HttpDataSourceException) == false) goto L96;
        if (((HttpDataSource.HttpDataSourceException) r4).type != 1) goto L96;
        return new ErrorInfo(4, 0);
    L96:
        return new ErrorInfo(8, 0);
    L80:
        return new ErrorInfo(3, 0);
    L98:
        if (r10 == false) goto L100;
        int r13 = 10;
    L102:
        return new ErrorInfo(r13, 0);
    L100:
        r13 = 11;
        goto L102
    L5:
        return new ErrorInfo(20, 0);
    }

    private static Pair<String, String> getLanguageAndRegion(String r4) {
        String[] r0 = Util.split(r4, "-");
        String r1 = r0[0];
        if (r0.length < 2) goto L5;
        String r2 = r0[1];
    L7:
        return Pair.create(r1, r2);
    L5:
        r2 = null;
        goto L7
    }

    private static int getNetworkType(Context r1) {
        switch(NetworkTypeObserver.getInstance(r1).getNetworkType()) {
            case 0: goto L22;
            case 1: goto L20;
            case 2: goto L18;
            case 3: goto L16;
            case 4: goto L14;
            case 5: goto L12;
            case 6: goto L4;
            case 7: goto L10;
            case 8: goto L4;
            case 9: goto L8;
            case 10: goto L6;
            default: goto L4;
        };
    L4:
        return 1;
    L6:
        return 7;
    L8:
        return 8;
    L10:
        return 3;
    L12:
        return 6;
    L14:
        return 5;
    L16:
        return 4;
    L18:
        return 2;
    L20:
        return 9;
    L22:
        return 0;
    }

    private static int getStreamType(MediaItem r2) {
        if (r2.localConfiguration != null) goto L7;
        return 0;
    L7:
        switch(Util.inferContentTypeForUriAndMimeType(r2.localConfiguration.uri, r2.localConfiguration.mimeType)) {
            case 0: goto L14;
            case 1: goto L12;
            case 2: goto L10;
            default: goto L8;
        };
    L8:
        return 1;
    L10:
        return 4;
    L12:
        return 5;
    L14:
        return 3;
    }

    private static int getTrackChangeReason(int r1) {
        switch(r1) {
            case 1: goto L9;
            case 2: goto L7;
            case 3: goto L5;
            default: goto L3;
        };
    L3:
        return 1;
    L5:
        return 4;
    L7:
        return 3;
    L9:
        return 2;
    }

    private void maybeAddSessions(AnalyticsListener.Events r6) {
        int r0 = 0;
    L4:
        if (r0 >= r6.size()) goto L13;
        int r1 = r6.get(r0);
        AnalyticsListener.EventTime r2 = r6.getEventTime(r1);
        if (r1 != 0) goto L9;
        this.sessionManager.updateSessionsWithTimelineChange(r2);
    L12:
        r0 = r0 + 1;
        goto L4
    L9:
        if (r1 != 11) goto L11;
        this.sessionManager.updateSessionsWithDiscontinuity(r2, this.discontinuityReason);
        goto L12
    L11:
        this.sessionManager.updateSessions(r2);
        goto L12
    }

    private void maybeReportNetworkChange(long r6) {
        int r0 = getNetworkType(this.context);
        if (r0 == this.currentNetworkType) goto L6;
        this.currentNetworkType = r0;
        this.playbackSession.reportNetworkEvent(new NetworkEvent.Builder().setNetworkType(r0).setTimeSinceCreatedMillis(r6 - this.startTimeMs).build());
        return;
    }

    private void maybeReportPlaybackError(long r8) {
        PlaybackException r0 = this.pendingPlayerError;
        if (r0 != null) goto L5;
        return;
    L5:
        Context r1 = this.context;
        if (this.ioErrorType != 4) goto L8;
        boolean r2 = true;
    L9:
        ErrorInfo r3 = getErrorInfo(r0, r1, r2);
        this.playbackSession.reportPlaybackErrorEvent(new PlaybackErrorEvent.Builder().setTimeSinceCreatedMillis(r8 - this.startTimeMs).setErrorCode(r3.errorCode).setSubErrorCode(r3.subErrorCode).setException(r0).build());
        this.reportedEventsForCurrentSession = true;
        this.pendingPlayerError = null;
        return;
    L8:
        r2 = false;
        goto L9
    }

    private void maybeReportPlaybackStateChange(Player r6, AnalyticsListener.Events r7, long r8) {
        if (r6.getPlaybackState() == 2) goto L6;
        this.isSeeking = false;
    L6:
        if (r6.getPlayerError() != null) goto L9;
        this.hasFatalError = false;
    L11:
        int r0 = resolveNewPlaybackState(r6);
        if (this.currentPlaybackState == r0) goto L15;
        this.currentPlaybackState = r0;
        this.reportedEventsForCurrentSession = true;
        this.playbackSession.reportPlaybackStateEvent(new PlaybackStateEvent.Builder().setState(this.currentPlaybackState).setTimeSinceCreatedMillis(r8 - this.startTimeMs).build());
        return;
    L15:
        return;
    L9:
        if (r7.contains(10) == false) goto L11;
        this.hasFatalError = true;
        goto L11
    }

    private void maybeReportTrackChanges(Player r7, AnalyticsListener.Events r8, long r9) {
        if (r8.contains(2) == false) goto L16;
        Tracks r1 = r7.getCurrentTracks();
        boolean r0 = r1.isTypeSelected(2);
        boolean r3 = r1.isTypeSelected(1);
        boolean r4 = r1.isTypeSelected(3);
        if (r0 == true) goto L9;
        if (r3 == true) goto L9;
        if (r4 == false) goto L16;
    L9:
        if (r0 == true) goto L11;
        maybeUpdateVideoFormat(r9, null, 0);
    L11:
        if (r3 == true) goto L13;
        maybeUpdateAudioFormat(r9, null, 0);
    L13:
        if (r4 == true) goto L16;
        maybeUpdateTextFormat(r9, null, 0);
    L16:
        if (canReportPendingFormatUpdate(this.pendingVideoFormat) == false) goto L21;
        if (this.pendingVideoFormat.format.height == (-1)) goto L21;
        maybeUpdateVideoFormat(r9, this.pendingVideoFormat.format, this.pendingVideoFormat.selectionReason);
        this.pendingVideoFormat = null;
    L21:
        if (canReportPendingFormatUpdate(this.pendingAudioFormat) == false) goto L24;
        maybeUpdateAudioFormat(r9, this.pendingAudioFormat.format, this.pendingAudioFormat.selectionReason);
        this.pendingAudioFormat = null;
    L24:
        if (canReportPendingFormatUpdate(this.pendingTextFormat) == false) goto L27;
        maybeUpdateTextFormat(r9, this.pendingTextFormat.format, this.pendingTextFormat.selectionReason);
        this.pendingTextFormat = null;
        return;
    }

    private void maybeUpdateAudioFormat(long r7, Format r9, int r10) {
        if (Util.areEqual(this.currentAudioFormat, r9) == false) goto L6;
        return;
    L6:
        if (this.currentAudioFormat != null) goto L9;
        if (r10 != 0) goto L9;
        int r5 = 1;
    L10:
        this.currentAudioFormat = r9;
        reportTrackChangeEvent(0, r7, r9, r5);
        return;
    L9:
        r5 = r10;
        goto L10
    }

    private void maybeUpdateMetricsBuilderValues(Player r4, AnalyticsListener.Events r5) {
        if (r5.contains(0) == false) goto L8;
        AnalyticsListener.EventTime r0 = r5.getEventTime(0);
        if (this.metricsBuilder == null) goto L8;
        maybeUpdateTimelineMetadata(r0.timeline, r0.mediaPeriodId);
    L8:
        if (r5.contains(2) == false) goto L15;
        if (this.metricsBuilder == null) goto L15;
        DrmInitData r1 = getDrmInitData(r4.getCurrentTracks().getGroups());
        if (r1 == null) goto L15;
        ((PlaybackMetrics.Builder) Util.castNonNull(this.metricsBuilder)).setDrmType(getDrmType(r1));
    L15:
        if (r5.contains(1011) == false) goto L18;
        this.audioUnderruns++;
        return;
    }

    private void maybeUpdateTextFormat(long r7, Format r9, int r10) {
        if (Util.areEqual(this.currentTextFormat, r9) == false) goto L6;
        return;
    L6:
        if (this.currentTextFormat != null) goto L9;
        if (r10 != 0) goto L9;
        int r5 = 1;
    L10:
        this.currentTextFormat = r9;
        reportTrackChangeEvent(2, r7, r9, r5);
        return;
    L9:
        r5 = r10;
        goto L10
    }

    @RequiresNonNull({"metricsBuilder"})
    private void maybeUpdateTimelineMetadata(Timeline r7, MediaSource.MediaPeriodId r8) {
        PlaybackMetrics.Builder r0 = this.metricsBuilder;
        if (r8 != null) goto L5;
        return;
    L5:
        int r1 = r7.getIndexOfPeriod(r8.periodUid);
        if (r1 != (-1)) goto L8;
        return;
    L8:
        r7.getPeriod(r1, this.period);
        r7.getWindow(this.period.windowIndex, this.window);
        r0.setStreamType(getStreamType(this.window.mediaItem));
        if (this.window.durationUs == C0180C.TIME_UNSET) goto L18;
        if (this.window.isPlaceholder == true) goto L18;
        if (this.window.isDynamic == true) goto L18;
        if (this.window.isLive() == true) goto L18;
        r0.setMediaDurationMillis(this.window.getDurationMs());
    L18:
        if (this.window.isLive() == false) goto L20;
        int r2 = 2;
    L21:
        r0.setPlaybackType(r2);
        this.reportedEventsForCurrentSession = true;
        return;
    L20:
        r2 = 1;
        goto L21
    }

    private void maybeUpdateVideoFormat(long r7, Format r9, int r10) {
        if (Util.areEqual(this.currentVideoFormat, r9) == false) goto L6;
        return;
    L6:
        if (this.currentVideoFormat != null) goto L9;
        if (r10 != 0) goto L9;
        int r5 = 1;
    L10:
        this.currentVideoFormat = r9;
        reportTrackChangeEvent(1, r7, r9, r5);
        return;
    L9:
        r5 = r10;
        goto L10
    }

    private void reportTrackChangeEvent(int r5, long r6, Format r8, int r9) {
        TrackChangeEvent.Builder r0 = new TrackChangeEvent.Builder(r5).setTimeSinceCreatedMillis(r6 - this.startTimeMs);
        if (r8 == null) goto L36;
        r0.setTrackState(1);
        r0.setTrackChangeReason(getTrackChangeReason(r9));
        if (r8.containerMimeType == null) goto L8;
        r0.setContainerMimeType(r8.containerMimeType);
    L8:
        if (r8.sampleMimeType == null) goto L11;
        r0.setSampleMimeType(r8.sampleMimeType);
    L11:
        if (r8.codecs == null) goto L14;
        r0.setCodecName(r8.codecs);
    L14:
        if (r8.bitrate == (-1)) goto L17;
        r0.setBitrate(r8.bitrate);
    L17:
        if (r8.width == (-1)) goto L20;
        r0.setWidth(r8.width);
    L20:
        if (r8.height == (-1)) goto L23;
        r0.setHeight(r8.height);
    L23:
        if (r8.channelCount == (-1)) goto L26;
        r0.setChannelCount(r8.channelCount);
    L26:
        if (r8.sampleRate == (-1)) goto L29;
        r0.setAudioSampleRate(r8.sampleRate);
    L29:
        if (r8.language == null) goto L34;
        Pair<String, String> r2 = getLanguageAndRegion(r8.language);
        r0.setLanguage((String) r2.first);
        if (r2.second == null) goto L34;
        r0.setLanguageRegion((String) r2.second);
    L34:
        if (r8.frameRate == (-1.0f)) goto L37;
        r0.setVideoFrameRate(r8.frameRate);
    L37:
        this.reportedEventsForCurrentSession = true;
        this.playbackSession.reportTrackChangeEvent(r0.build());
        return;
    L36:
        r0.setTrackState(0);
        goto L37
    }

    private int resolveNewPlaybackState(Player r5) {
        int r0 = r5.getPlaybackState();
        if (this.isSeeking == false) goto L7;
        return 5;
    L7:
        if (this.hasFatalError == false) goto L11;
        return 13;
    L11:
        if (r0 != 4) goto L15;
        return 11;
    L15:
        if (r0 != 2) goto L34;
        if (this.currentPlaybackState != 0) goto L19;
    L32:
        return 2;
    L19:
        if (this.currentPlaybackState == 2) goto L32;
        if (this.currentPlaybackState == 12) goto L32;
        if (r5.getPlayWhenReady() == true) goto L28;
        return 7;
    L28:
        if (r5.getPlaybackSuppressionReason() == 0) goto L30;
        return 10;
    L30:
        return 6;
    L34:
        if (r0 != 3) goto L44;
        if (r5.getPlayWhenReady() == true) goto L39;
        return 4;
    L39:
        if (r5.getPlaybackSuppressionReason() == 0) goto L51;
        return 9;
    L51:
        return 3;
    L44:
        if (r0 != 1) goto L49;
        if (this.currentPlaybackState == 0) goto L49;
        return 12;
    L49:
        return this.currentPlaybackState;
    }

    public LogSessionId getLogSessionId() {
        return this.playbackSession.getSessionId();
    }

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager.Listener
    public void onAdPlaybackStarted(AnalyticsListener.EventTime r1, String r2, String r3) {
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onAudioAttributesChanged(AnalyticsListener.EventTime r1, AudioAttributes r2) {
        AnalyticsListener.CC.$default$onAudioAttributesChanged(this, r1, r2);
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
    public /* synthetic */ void onAudioDecoderInitialized(AnalyticsListener.EventTime r1, String r2, long r3, long r5) {
        AnalyticsListener.CC.$default$onAudioDecoderInitialized(this, r1, r2, r3, r5);
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
    public /* synthetic */ void onAudioInputFormatChanged(AnalyticsListener.EventTime r1, Format r2, DecoderReuseEvaluation r3) {
        AnalyticsListener.CC.$default$onAudioInputFormatChanged(this, r1, r2, r3);
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
    public /* synthetic */ void onAudioSinkError(AnalyticsListener.EventTime r1, Exception r2) {
        AnalyticsListener.CC.$default$onAudioSinkError(this, r1, r2);
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
    public /* synthetic */ void onAudioUnderrun(AnalyticsListener.EventTime r1, int r2, long r3, long r5) {
        AnalyticsListener.CC.$default$onAudioUnderrun(this, r1, r2, r3, r5);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onAvailableCommandsChanged(AnalyticsListener.EventTime r1, Player.Commands r2) {
        AnalyticsListener.CC.$default$onAvailableCommandsChanged(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onBandwidthEstimate(AnalyticsListener.EventTime r9, int r10, long r11, long r13) {
        if (r9.mediaPeriodId == null) goto L14;
        String r0 = this.sessionManager.getSessionForMediaPeriodId(r9.timeline, (MediaSource.MediaPeriodId) Assertions.checkNotNull(r9.mediaPeriodId));
        Long r1 = this.bandwidthBytes.get(r0);
        Long r2 = this.bandwidthTimeMs.get(r0);
        HashMap<String, Long> r3 = this.bandwidthBytes;
        long r4 = 0;
        if (r1 != null) goto L7;
        long r6 = 0;
    L8:
        r3.put(r0, Long.valueOf(r6 + r11));
        HashMap<String, Long> r5 = this.bandwidthTimeMs;
        if (r2 == null) goto L12;
        r4 = r2.longValue();
    L12:
        r5.put(r0, Long.valueOf(r4 + ((long) r10)));
        return;
    L7:
        r6 = r1.longValue();
        goto L8
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
    public void onDownstreamFormatChanged(AnalyticsListener.EventTime r7, MediaLoadData r8) {
        if (r7.mediaPeriodId != null) goto L5;
        return;
    L5:
        PendingFormatUpdate r0 = new PendingFormatUpdate((Format) Assertions.checkNotNull(r8.trackFormat), r8.trackSelectionReason, this.sessionManager.getSessionForMediaPeriodId(r7.timeline, (MediaSource.MediaPeriodId) Assertions.checkNotNull(r7.mediaPeriodId)));
        switch(r8.trackType) {
            case 0: goto L10;
            case 1: goto L9;
            case 2: goto L10;
            case 3: goto L8;
            default: goto L14;
        };
    L8:
        this.pendingTextFormat = r0;
        return;
    L9:
        this.pendingAudioFormat = r0;
        return;
    L10:
        this.pendingVideoFormat = r0;
        return;
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
    public /* synthetic */ void onDroppedVideoFrames(AnalyticsListener.EventTime r1, int r2, long r3) {
        AnalyticsListener.CC.$default$onDroppedVideoFrames(this, r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onEvents(Player r5, AnalyticsListener.Events r6) {
        if (r6.size() != 0) goto L5;
        return;
    L5:
        maybeAddSessions(r6);
        long r0 = SystemClock.elapsedRealtime();
        maybeUpdateMetricsBuilderValues(r5, r6);
        maybeReportPlaybackError(r0);
        maybeReportTrackChanges(r5, r6, r0);
        maybeReportNetworkChange(r0);
        maybeReportPlaybackStateChange(r5, r6, r0);
        if (r6.contains(AnalyticsListener.EVENT_PLAYER_RELEASED) == false) goto L9;
        this.sessionManager.finishAllSessions(r6.getEventTime(AnalyticsListener.EVENT_PLAYER_RELEASED));
        return;
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
    public /* synthetic */ void onLoadCompleted(AnalyticsListener.EventTime r1, LoadEventInfo r2, MediaLoadData r3) {
        AnalyticsListener.CC.$default$onLoadCompleted(this, r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onLoadError(AnalyticsListener.EventTime r2, LoadEventInfo r3, MediaLoadData r4, IOException r5, boolean r6) {
        this.ioErrorType = r4.dataType;
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
    public void onPlayerError(AnalyticsListener.EventTime r1, PlaybackException r2) {
        this.pendingPlayerError = r2;
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
    public void onPositionDiscontinuity(AnalyticsListener.EventTime r2, Player.PositionInfo r3, Player.PositionInfo r4, int r5) {
        if (r5 != 1) goto L5;
        this.isSeeking = true;
    L5:
        this.discontinuityReason = r5;
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onRenderedFirstFrame(AnalyticsListener.EventTime r1, Object r2, long r3) {
        AnalyticsListener.CC.$default$onRenderedFirstFrame(this, r1, r2, r3);
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

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager.Listener
    public void onSessionActive(AnalyticsListener.EventTime r3, String r4) {
        if (r3.mediaPeriodId != null) goto L5;
    L7:
        finishCurrentSession();
        this.activeSessionId = r4;
        this.metricsBuilder = new PlaybackMetrics.Builder().setPlayerName(MediaLibraryInfo.TAG).setPlayerVersion(MediaLibraryInfo.VERSION);
        maybeUpdateTimelineMetadata(r3.timeline, r3.mediaPeriodId);
        return;
    L5:
        if (r3.mediaPeriodId.isAd() == false) goto L7;
    }

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager.Listener
    public void onSessionCreated(AnalyticsListener.EventTime r1, String r2) {
    }

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager.Listener
    public void onSessionFinished(AnalyticsListener.EventTime r2, String r3, boolean r4) {
        if (r2.mediaPeriodId == null) goto L7;
        if (r2.mediaPeriodId.isAd() == false) goto L7;
    L10:
        this.bandwidthTimeMs.remove(r3);
        this.bandwidthBytes.remove(r3);
        return;
    L7:
        if (r3.equals(this.activeSessionId) == false) goto L10;
        finishCurrentSession();
        goto L10
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
    public /* synthetic */ void onVideoDecoderInitialized(AnalyticsListener.EventTime r1, String r2, long r3, long r5) {
        AnalyticsListener.CC.$default$onVideoDecoderInitialized(this, r1, r2, r3, r5);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onVideoDecoderReleased(AnalyticsListener.EventTime r1, String r2) {
        AnalyticsListener.CC.$default$onVideoDecoderReleased(this, r1, r2);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onVideoDisabled(AnalyticsListener.EventTime r3, DecoderCounters r4) {
        this.droppedFrames += r4.droppedBufferCount;
        this.playedFrames += r4.renderedOutputBufferCount;
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
    public /* synthetic */ void onVideoInputFormatChanged(AnalyticsListener.EventTime r1, Format r2, DecoderReuseEvaluation r3) {
        AnalyticsListener.CC.$default$onVideoInputFormatChanged(this, r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onVideoSizeChanged(AnalyticsListener.EventTime r1, int r2, int r3, int r4, float r5) {
        AnalyticsListener.CC.$default$onVideoSizeChanged(this, r1, r2, r3, r4, r5);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onVideoSizeChanged(AnalyticsListener.EventTime r6, VideoSize r7) {
        PendingFormatUpdate r0 = this.pendingVideoFormat;
        if (r0 != null) goto L5;
        return;
    L5:
        if (r0.format.height != (-1)) goto L9;
        this.pendingVideoFormat = new PendingFormatUpdate(r0.format.buildUpon().setWidth(r7.width).setHeight(r7.height).build(), r0.selectionReason, r0.sessionId);
        return;
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onVolumeChanged(AnalyticsListener.EventTime r1, float r2) {
        AnalyticsListener.CC.$default$onVolumeChanged(this, r1, r2);
    }
}
