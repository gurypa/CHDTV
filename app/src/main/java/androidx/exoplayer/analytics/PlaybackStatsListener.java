package androidx.media3.exoplayer.analytics;

import android.os.SystemClock;
import android.util.Pair;
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
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.DecoderReuseEvaluation;
import androidx.media3.exoplayer.audio.AudioSink;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MediaLoadData;
import androidx.media3.exoplayer.source.MediaSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class PlaybackStatsListener implements AnalyticsListener, PlaybackSessionManager.Listener {
    private Format audioFormat;
    private long bandwidthBytes;
    private long bandwidthTimeMs;
    private final Callback callback;
    private long discontinuityFromPositionMs;
    private String discontinuityFromSession;
    private int discontinuityReason;
    private int droppedFrames;
    private PlaybackStats finishedPlaybackStats;
    private final boolean keepHistory;
    private Exception nonFatalException;
    private final Timeline.Period period;
    private final Map<String, PlaybackStatsTracker> playbackStatsTrackers;
    private final PlaybackSessionManager sessionManager;
    private final Map<String, AnalyticsListener.EventTime> sessionStartEventTimes;
    private Format videoFormat;
    private VideoSize videoSize;

    public interface Callback {
        void onPlaybackStatsReady(AnalyticsListener.EventTime r1, PlaybackStats r2);
    }

    private static final class PlaybackStatsTracker {
        private long audioFormatBitrateTimeProduct;
        private final List<PlaybackStats.EventTimeAndFormat> audioFormatHistory;
        private long audioFormatTimeMs;
        private long audioUnderruns;
        private long bandwidthBytes;
        private long bandwidthTimeMs;
        private Format currentAudioFormat;
        private float currentPlaybackSpeed;
        private int currentPlaybackState;
        private long currentPlaybackStateStartTimeMs;
        private Format currentVideoFormat;
        private long droppedFrames;
        private int fatalErrorCount;
        private final List<PlaybackStats.EventTimeAndException> fatalErrorHistory;
        private long firstReportedTimeMs;
        private boolean hasBeenReady;
        private boolean hasEnded;
        private boolean hasFatalError;
        private long initialAudioFormatBitrate;
        private long initialVideoFormatBitrate;
        private int initialVideoFormatHeight;
        private final boolean isAd;
        private boolean isForeground;
        private boolean isInterruptedByAd;
        private boolean isJoinTimeInvalid;
        private boolean isSeeking;
        private final boolean keepHistory;
        private long lastAudioFormatStartTimeMs;
        private long lastRebufferStartTimeMs;
        private long lastVideoFormatStartTimeMs;
        private long maxRebufferTimeMs;
        private final List<long[]> mediaTimeHistory;
        private int nonFatalErrorCount;
        private final List<PlaybackStats.EventTimeAndException> nonFatalErrorHistory;
        private int pauseBufferCount;
        private int pauseCount;
        private final long[] playbackStateDurationsMs;
        private final List<PlaybackStats.EventTimeAndPlaybackState> playbackStateHistory;
        private int rebufferCount;
        private int seekCount;
        private boolean startedLoading;
        private long videoFormatBitrateTimeMs;
        private long videoFormatBitrateTimeProduct;
        private long videoFormatHeightTimeMs;
        private long videoFormatHeightTimeProduct;
        private final List<PlaybackStats.EventTimeAndFormat> videoFormatHistory;

        public PlaybackStatsTracker(boolean r4, AnalyticsListener.EventTime r5) {
            this.keepHistory = r4;
            this.playbackStateDurationsMs = new long[16];
            if (r4 == false) goto L5;
            List<PlaybackStats.EventTimeAndPlaybackState> r0 = new ArrayList();
        L6:
            this.playbackStateHistory = r0;
            if (r4 == false) goto L9;
            List<long[]> r1 = new ArrayList();
        L10:
            this.mediaTimeHistory = r1;
            if (r4 == false) goto L13;
            List<PlaybackStats.EventTimeAndFormat> r2 = new ArrayList();
        L14:
            this.videoFormatHistory = r2;
            if (r4 == false) goto L17;
            List<PlaybackStats.EventTimeAndFormat> r3 = new ArrayList();
        L18:
            this.audioFormatHistory = r3;
            if (r4 == false) goto L21;
            List<PlaybackStats.EventTimeAndException> r6 = new ArrayList();
        L22:
            this.fatalErrorHistory = r6;
            if (r4 == false) goto L25;
            List<PlaybackStats.EventTimeAndException> r7 = new ArrayList();
        L26:
            this.nonFatalErrorHistory = r7;
            boolean r8 = false;
            this.currentPlaybackState = 0;
            this.currentPlaybackStateStartTimeMs = r5.realtimeMs;
            this.firstReportedTimeMs = C0180C.TIME_UNSET;
            this.maxRebufferTimeMs = C0180C.TIME_UNSET;
            if (r5.mediaPeriodId != null) goto L29;
        L31:
            this.isAd = r8;
            this.initialAudioFormatBitrate = -1;
            this.initialVideoFormatBitrate = -1;
            this.initialVideoFormatHeight = -1;
            this.currentPlaybackSpeed = 1.0f;
            return;
        L29:
            if (r5.mediaPeriodId.isAd() == false) goto L31;
            r8 = true;
            goto L31
        L25:
            r7 = Collections.emptyList();
            goto L26
        L21:
            r6 = Collections.emptyList();
            goto L22
        L17:
            r3 = Collections.emptyList();
            goto L18
        L13:
            r2 = Collections.emptyList();
            goto L14
        L9:
            r1 = Collections.emptyList();
            goto L10
        L5:
            r0 = Collections.emptyList();
            goto L6
        }

        private long[] guessMediaTimeBasedOnElapsedRealtime(long r13) {
            long[] r0 = this.mediaTimeHistory.get(this.mediaTimeHistory.size() - 1);
            return new long[]{r13, r0[1] + ((long) ((r13 - r0[0]) * this.currentPlaybackSpeed))};
        }

        private static boolean isInvalidJoinTransition(int r4, int r5) {
            if (r4 == 1) goto L7;
            if (r4 == 2) goto L7;
            if (r4 == 14) goto L7;
            return false;
        L7:
            if (r5 == 1) goto L20;
            if (r5 == 2) goto L21;
            if (r5 != 14) goto L11;
            return false;
        L11:
            if (r5 != 3) goto L13;
            return false;
        L13:
            if (r5 != 4) goto L15;
            return false;
        L15:
            if (r5 != 9) goto L17;
            return false;
        L17:
            if (r5 != 11) goto L19;
            return false;
        L19:
            return true;
        L21:
            return false;
        L20:
            return false;
        }

        private static boolean isPausedState(int r1) {
            if (r1 != 4) goto L5;
        L8:
            return true;
        L5:
            if (r1 == 7) goto L8;
            return false;
        }

        private static boolean isReadyState(int r1) {
            if (r1 != 3) goto L5;
        L10:
            return true;
        L5:
            if (r1 == 4) goto L10;
            if (r1 == 9) goto L10;
            return false;
        }

        private static boolean isRebufferingState(int r1) {
            if (r1 != 6) goto L5;
        L10:
            return true;
        L5:
            if (r1 == 7) goto L10;
            if (r1 == 10) goto L10;
            return false;
        }

        private void maybeRecordAudioFormatTime(long r7) {
            if (this.currentPlaybackState == 3) goto L5;
        L9:
            this.lastAudioFormatStartTimeMs = r7;
            return;
        L5:
            if (this.currentAudioFormat == null) goto L9;
            if (this.currentAudioFormat.bitrate == (-1)) goto L9;
            long r0 = (long) ((r7 - this.lastAudioFormatStartTimeMs) * this.currentPlaybackSpeed);
            this.audioFormatTimeMs += r0;
            this.audioFormatBitrateTimeProduct += ((long) this.currentAudioFormat.bitrate) * r0;
            goto L9
        }

        private void maybeRecordVideoFormatTime(long r9) {
            if (this.currentPlaybackState == 3) goto L5;
        L12:
            this.lastVideoFormatStartTimeMs = r9;
            return;
        L5:
            if (this.currentVideoFormat == null) goto L12;
            long r0 = (long) ((r9 - this.lastVideoFormatStartTimeMs) * this.currentPlaybackSpeed);
            if (this.currentVideoFormat.height == (-1)) goto L10;
            this.videoFormatHeightTimeMs += r0;
            this.videoFormatHeightTimeProduct += ((long) this.currentVideoFormat.height) * r0;
        L10:
            if (this.currentVideoFormat.bitrate == (-1)) goto L12;
            this.videoFormatBitrateTimeMs += r0;
            this.videoFormatBitrateTimeProduct += ((long) this.currentVideoFormat.bitrate) * r0;
            goto L12
        }

        private void maybeUpdateAudioFormat(AnalyticsListener.EventTime r5, Format r6) {
            if (Util.areEqual(this.currentAudioFormat, r6) == false) goto L5;
            return;
        L5:
            maybeRecordAudioFormatTime(r5.realtimeMs);
            if (r6 != null) goto L8;
        L12:
            this.currentAudioFormat = r6;
            if (this.keepHistory == false) goto L16;
            this.audioFormatHistory.add(new PlaybackStats.EventTimeAndFormat(r5, this.currentAudioFormat));
            return;
        L16:
            return;
        L8:
            if (this.initialAudioFormatBitrate != (-1)) goto L12;
            if (r6.bitrate == (-1)) goto L12;
            this.initialAudioFormatBitrate = r6.bitrate;
            goto L12
        }

        private void maybeUpdateMaxRebufferTimeMs(long r7) {
            if (isRebufferingState(this.currentPlaybackState) == false) goto L10;
            long r0 = r7 - this.lastRebufferStartTimeMs;
            if (this.maxRebufferTimeMs != C0180C.TIME_UNSET) goto L7;
        L8:
            this.maxRebufferTimeMs = r0;
            return;
        L7:
            if (r0 > this.maxRebufferTimeMs) goto L8;
            return;
        }

        private void maybeUpdateMediaTimeHistory(long r10, long r12) {
            if (this.keepHistory == true) goto L6;
            return;
        L6:
            if (this.currentPlaybackState == 3) goto L16;
            if (r12 != C0180C.TIME_UNSET) goto L11;
            return;
        L11:
            if (this.mediaTimeHistory.isEmpty() == true) goto L16;
            long r7 = this.mediaTimeHistory.get(this.mediaTimeHistory.size() - 1)[1];
            if (r7 == r12) goto L16;
            this.mediaTimeHistory.add(new long[]{r10, r7});
        L16:
            if (r12 == C0180C.TIME_UNSET) goto L19;
            this.mediaTimeHistory.add(new long[]{r10, r12});
            return;
        L19:
            if (this.mediaTimeHistory.isEmpty() == true) goto L23;
            this.mediaTimeHistory.add(guessMediaTimeBasedOnElapsedRealtime(r10));
            return;
        }

        private void maybeUpdateVideoFormat(AnalyticsListener.EventTime r7, Format r8) {
            if (Util.areEqual(this.currentVideoFormat, r8) == false) goto L5;
            return;
        L5:
            maybeRecordVideoFormatTime(r7.realtimeMs);
            if (r8 != null) goto L8;
        L17:
            this.currentVideoFormat = r8;
            if (this.keepHistory == false) goto L21;
            this.videoFormatHistory.add(new PlaybackStats.EventTimeAndFormat(r7, this.currentVideoFormat));
            return;
        L21:
            return;
        L8:
            if (this.initialVideoFormatHeight != (-1)) goto L13;
            if (r8.height == (-1)) goto L13;
            this.initialVideoFormatHeight = r8.height;
        L13:
            if (this.initialVideoFormatBitrate != (-1)) goto L17;
            if (r8.bitrate == (-1)) goto L17;
            this.initialVideoFormatBitrate = r8.bitrate;
            goto L17
        }

        private int resolveNewPlaybackState(Player r6) {
            int r0 = r6.getPlaybackState();
            if (this.isSeeking == false) goto L9;
            if (this.isForeground == false) goto L9;
            return 5;
        L9:
            if (this.hasFatalError == false) goto L13;
            return 13;
        L13:
            if (this.isForeground == true) goto L20;
            if (this.startedLoading == true) goto L64;
            return 0;
        L64:
            return 1;
        L20:
            if (this.isInterruptedByAd == false) goto L23;
            return 14;
        L23:
            if (r0 != 4) goto L27;
            return 11;
        L27:
            if (r0 != 2) goto L48;
            if (this.currentPlaybackState != 0) goto L31;
        L46:
            return 2;
        L31:
            if (this.currentPlaybackState == 1) goto L46;
            if (this.currentPlaybackState == 2) goto L46;
            if (this.currentPlaybackState == 14) goto L46;
            if (r6.getPlayWhenReady() == true) goto L42;
            return 7;
        L42:
            if (r6.getPlaybackSuppressionReason() == 0) goto L44;
            return 10;
        L44:
            return 6;
        L48:
            if (r0 == 3) goto L50;
            if (r0 != 1) goto L63;
            if (this.currentPlaybackState == 0) goto L63;
            return 12;
        L63:
            return this.currentPlaybackState;
        L50:
            if (r6.getPlayWhenReady() == true) goto L53;
            return 4;
        L53:
            if (r6.getPlaybackSuppressionReason() == 0) goto L66;
            return 9;
        L66:
            return 3;
        }

        private void updatePlaybackState(int r10, AnalyticsListener.EventTime r11) {
            boolean r1 = false;
            if (r11.realtimeMs < this.currentPlaybackStateStartTimeMs) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            long r3 = r11.realtimeMs - this.currentPlaybackStateStartTimeMs;
            long[] r2 = this.playbackStateDurationsMs;
            int r5 = this.currentPlaybackState;
            r2[r5] = r2[r5] + r3;
            if (this.firstReportedTimeMs != C0180C.TIME_UNSET) goto L9;
            this.firstReportedTimeMs = r11.realtimeMs;
        L9:
            this.isJoinTimeInvalid |= isInvalidJoinTransition(this.currentPlaybackState, r10);
            this.hasBeenReady |= isReadyState(r10);
            boolean r4 = this.hasEnded;
            if (r10 != 11) goto L12;
            r1 = true;
        L12:
            this.hasEnded = r4 | r1;
            if (isPausedState(this.currentPlaybackState) == true) goto L18;
            if (isPausedState(r10) == false) goto L18;
            this.pauseCount++;
        L18:
            if (r10 != 5) goto L21;
            this.seekCount++;
        L21:
            if (isRebufferingState(this.currentPlaybackState) == true) goto L26;
            if (isRebufferingState(r10) == false) goto L26;
            this.rebufferCount++;
            this.lastRebufferStartTimeMs = r11.realtimeMs;
        L26:
            if (isRebufferingState(this.currentPlaybackState) == true) goto L28;
        L31:
            maybeUpdateMaxRebufferTimeMs(r11.realtimeMs);
            this.currentPlaybackState = r10;
            this.currentPlaybackStateStartTimeMs = r11.realtimeMs;
            if (this.keepHistory == false) goto L35;
            this.playbackStateHistory.add(new PlaybackStats.EventTimeAndPlaybackState(r11, this.currentPlaybackState));
            return;
        L35:
            return;
        L28:
            if (this.currentPlaybackState == 7) goto L31;
            if (r10 != 7) goto L31;
            this.pauseBufferCount++;
            goto L31
        L5:
            r0 = false;
            goto L6
        }

        public PlaybackStats build(boolean r68) {
            long[] r1 = this.playbackStateDurationsMs;
            List<long[]> r2 = this.mediaTimeHistory;
            if (r68 == true) goto L10;
            long r5 = SystemClock.elapsedRealtime();
            long[] r3 = Arrays.copyOf(this.playbackStateDurationsMs, 16);
            long r7 = Math.max(0, r5 - this.currentPlaybackStateStartTimeMs);
            int r9 = this.currentPlaybackState;
            r3[r9] = r3[r9] + r7;
            maybeUpdateMaxRebufferTimeMs(r5);
            maybeRecordVideoFormatTime(r5);
            maybeRecordAudioFormatTime(r5);
            ArrayList r10 = new ArrayList(this.mediaTimeHistory);
            if (this.keepHistory == true) goto L7;
        L9:
            long[] r12 = r3;
            List<long[]> r14 = r10;
        L12:
            if (this.isJoinTimeInvalid == false) goto L14;
        L17:
            boolean r4 = true;
        L18:
            if (r4 == false) goto L20;
            long r21 = -9223372036854775807L;
        L22:
            if (r12[1] <= 0) goto L24;
            boolean r6 = true;
        L25:
            if (r68 == false) goto L27;
            List r8 = this.videoFormatHistory;
        L28:
            List r31 = r8;
            if (r68 == false) goto L31;
            List r11 = this.audioFormatHistory;
        L32:
            List r32 = r11;
            if (r68 == false) goto L35;
            List r13 = this.playbackStateHistory;
        L36:
            List r15 = r13;
            long r16 = this.firstReportedTimeMs;
            boolean r17 = this.isForeground;
            int r18 = !this.hasBeenReady ? 1 : 0;
            boolean r19 = this.hasEnded;
            if (r6 == false) goto L39;
            int r20 = 1;
        L40:
            if (r4 == false) goto L42;
            int r23 = 0;
        L43:
            int r22 = this.pauseCount;
            int r110 = this.pauseBufferCount;
            int r111 = this.seekCount;
            int r24 = this.rebufferCount;
            long r25 = this.maxRebufferTimeMs;
            boolean r26 = this.isAd;
            long r27 = this.videoFormatHeightTimeMs;
            long r28 = this.videoFormatHeightTimeProduct;
            long r29 = this.videoFormatBitrateTimeMs;
            long r30 = this.videoFormatBitrateTimeProduct;
            long r33 = this.audioFormatTimeMs;
            long r34 = this.audioFormatBitrateTimeProduct;
            if (this.initialVideoFormatHeight != (-1)) goto L46;
            int r45 = 0;
        L48:
            if (this.initialVideoFormatBitrate != (-1)) goto L50;
            int r46 = 0;
        L51:
            int r35 = this.initialVideoFormatHeight;
            long r36 = this.initialVideoFormatBitrate;
            if (this.initialAudioFormatBitrate != (-1)) goto L54;
            int r50 = 0;
        L55:
            long r37 = this.initialAudioFormatBitrate;
            long r38 = this.bandwidthTimeMs;
            long r39 = this.bandwidthBytes;
            long r40 = this.droppedFrames;
            long r41 = this.audioUnderruns;
            if (this.fatalErrorCount <= 0) goto L58;
            int r61 = 1;
        L60:
            return new PlaybackStats(1, r12, r15, r14, r16, r17 ? 1 : 0, r18, r19 ? 1 : 0, r20, r21, r23, r22, r110, r111, r24, r25, r26 ? 1 : 0, r31, r32, r27, r28, r29, r30, r33, r34, r45, r46, r35, r36, r50, r37, r38, r39, r40, r41, r61, this.fatalErrorCount, this.nonFatalErrorCount, this.fatalErrorHistory, this.nonFatalErrorHistory);
        L58:
            r61 = 0;
            goto L60
        L54:
            r50 = 1;
            goto L55
        L50:
            r46 = 1;
            goto L51
        L46:
            r45 = 1;
            goto L48
        L42:
            r23 = 1;
            goto L43
        L39:
            r20 = 0;
            goto L40
        L35:
            r13 = new ArrayList(this.playbackStateHistory);
            goto L36
        L31:
            r11 = new ArrayList(this.audioFormatHistory);
            goto L32
        L27:
            r8 = new ArrayList(this.videoFormatHistory);
            goto L28
        L24:
            r6 = false;
            goto L25
        L20:
            r21 = r12[2];
            goto L22
        L14:
            if (this.hasBeenReady == false) goto L17;
            r4 = false;
            goto L18
        L7:
            if (this.currentPlaybackState != 3) goto L9;
            r10.add(guessMediaTimeBasedOnElapsedRealtime(r5));
            goto L9
        L10:
            r12 = r1;
            r14 = r2;
            goto L12
        }

        public void onEvents(Player r18, AnalyticsListener.EventTime r19, boolean r20, long r21, boolean r23, int r24, boolean r25, boolean r26, PlaybackException r27, Exception r28, long r29, long r31, Format r33, Format r34, VideoSize r35) {
            if (r21 == C0180C.TIME_UNSET) goto L6;
            maybeUpdateMediaTimeHistory(r19.realtimeMs, r21);
            this.isSeeking = true;
        L6:
            if (r18.getPlaybackState() == 2) goto L8;
            this.isSeeking = false;
        L8:
            int r11 = r18.getPlaybackState();
            if (r11 != 1) goto L11;
        L13:
            this.isInterruptedByAd = false;
        L14:
            if (r27 == null) goto L19;
            this.hasFatalError = true;
            this.fatalErrorCount++;
            if (this.keepHistory == false) goto L22;
            this.fatalErrorHistory.add(new PlaybackStats.EventTimeAndException(r19, r27));
        L22:
            if (this.isForeground == true) goto L24;
        L31:
            if (r33 == null) goto L33;
            maybeUpdateVideoFormat(r19, r33);
        L33:
            if (r34 == null) goto L36;
            maybeUpdateAudioFormat(r19, r34);
        L36:
            if (this.currentVideoFormat != null) goto L38;
        L41:
            if (r26 == false) goto L43;
            this.startedLoading = true;
        L43:
            if (r25 == false) goto L45;
            this.audioUnderruns++;
        L45:
            this.droppedFrames += (long) r24;
            this.bandwidthTimeMs += r29;
            this.bandwidthBytes += r31;
            if (r28 == null) goto L50;
            this.nonFatalErrorCount++;
            if (this.keepHistory == false) goto L50;
            this.nonFatalErrorHistory.add(new PlaybackStats.EventTimeAndException(r19, r28));
        L50:
            int r9 = resolveNewPlaybackState(r18);
            float r10 = r18.getPlaybackParameters().speed;
            if (this.currentPlaybackState == r9) goto L53;
        L54:
            long r12 = r19.realtimeMs;
            if (r20 == false) goto L57;
            long r2 = r19.eventPlaybackPositionMs;
        L58:
            maybeUpdateMediaTimeHistory(r12, r2);
            maybeRecordVideoFormatTime(r19.realtimeMs);
            maybeRecordAudioFormatTime(r19.realtimeMs);
        L59:
            this.currentPlaybackSpeed = r10;
            if (this.currentPlaybackState == r9) goto L63;
            updatePlaybackState(r9, r19);
            return;
        L63:
            return;
        L57:
            r2 = C0180C.TIME_UNSET;
            goto L58
        L53:
            if (this.currentPlaybackSpeed == r10) goto L59;
        L38:
            if (this.currentVideoFormat.height != (-1)) goto L41;
            if (r35 == null) goto L41;
            maybeUpdateVideoFormat(r19, this.currentVideoFormat.buildUpon().setWidth(r35.width).setHeight(r35.height).build());
            goto L41
        L24:
            if (this.isInterruptedByAd == true) goto L31;
            Tracks r14 = r18.getCurrentTracks();
            if (r14.isTypeSelected(2) == true) goto L29;
            maybeUpdateVideoFormat(r19, null);
        L29:
            if (r14.isTypeSelected(1) == true) goto L31;
            maybeUpdateAudioFormat(r19, null);
            goto L31
        L19:
            if (r18.getPlayerError() != null) goto L22;
            this.hasFatalError = false;
            goto L22
        L11:
            if (r11 == 4) goto L13;
            if (r23 == false) goto L14;
            goto L13
        }

        public void onFinished(AnalyticsListener.EventTime r5, boolean r6, long r7) {
            int r1 = 11;
            if (this.currentPlaybackState == 11) goto L8;
            if (r6 == true) goto L8;
            r1 = 15;
        L8:
            maybeUpdateMediaTimeHistory(r5.realtimeMs, r7);
            maybeRecordVideoFormatTime(r5.realtimeMs);
            maybeRecordAudioFormatTime(r5.realtimeMs);
            updatePlaybackState(r1, r5);
        }

        public void onForeground() {
            this.isForeground = true;
        }

        public void onInterruptedByAd() {
            this.isInterruptedByAd = true;
            this.isSeeking = false;
        }
    }

    public PlaybackStatsListener(boolean r2, Callback r3) {
        this.callback = r3;
        this.keepHistory = r2;
        this.sessionManager = new DefaultPlaybackSessionManager();
        this.playbackStatsTrackers = new HashMap();
        this.sessionStartEventTimes = new HashMap();
        this.finishedPlaybackStats = PlaybackStats.EMPTY;
        this.period = new Timeline.Period();
        this.videoSize = VideoSize.UNKNOWN;
        this.sessionManager.setListener(this);
    }

    private Pair<AnalyticsListener.EventTime, Boolean> findBestEventTime(AnalyticsListener.Events r28, String r29) {
        AnalyticsListener.EventTime r3 = null;
        boolean r4 = false;
        int r5 = 0;
    L4:
        if (r5 >= r28.size()) goto L14;
        AnalyticsListener.EventTime r7 = r28.getEventTime(r28.get(r5));
        boolean r8 = this.sessionManager.belongsToSession(r7, r29);
        if (r3 == null) goto L12;
        if (r8 == false) goto L9;
        if (r4 == false) goto L12;
    L9:
        if (r8 != r4) goto L13;
        if (r7.realtimeMs > r3.realtimeMs) goto L12;
    L13:
        r5 = r5 + 1;
    L12:
        r3 = r7;
        r4 = r8;
        goto L13
    L14:
        Assertions.checkNotNull(r3);
        if (r4 == false) goto L17;
    L24:
        boolean r6 = r4;
    L26:
        return Pair.create(r3, Boolean.valueOf(r6));
    L17:
        if (r3.mediaPeriodId == null) goto L24;
        if (r3.mediaPeriodId.isAd() == false) goto L24;
        long r9 = r3.timeline.getPeriodByUid(r3.mediaPeriodId.periodUid, this.period).getAdGroupTimeUs(r3.mediaPeriodId.adGroupIndex);
        if (r9 != Long.MIN_VALUE) goto L23;
        r9 = this.period.durationUs;
    L23:
        long r10 = this.period.getPositionInWindowUs() + r9;
        r3 = new AnalyticsListener.EventTime(r3.realtimeMs, r3.timeline, r3.windowIndex, new MediaSource.MediaPeriodId(r3.mediaPeriodId.periodUid, r3.mediaPeriodId.windowSequenceNumber, r3.mediaPeriodId.adGroupIndex), Util.usToMs(r10), r3.timeline, r3.currentWindowIndex, r3.currentMediaPeriodId, r3.currentPlaybackPositionMs, r3.totalBufferedDurationMs);
        r6 = this.sessionManager.belongsToSession(r3, r29);
        goto L26
    }

    private boolean hasEvent(AnalyticsListener.Events r3, String r4, int r5) {
        if (r3.contains(r5) == true) goto L5;
    L7:
        return false;
    L5:
        if (this.sessionManager.belongsToSession(r3.getEventTime(r5), r4) == false) goto L7;
        return true;
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

    public PlaybackStats getCombinedPlaybackStats() {
        PlaybackStats[] r0 = new PlaybackStats[this.playbackStatsTrackers.size() + 1];
        r0[0] = this.finishedPlaybackStats;
        int r1 = 1;
        Iterator<PlaybackStatsTracker> r3 = this.playbackStatsTrackers.values().iterator();
    L4:
        if (r3.hasNext() == false) goto L7;
        r0[r1] = r3.next().build(false);
        r1 = r1 + 1;
        goto L4
    L7:
        return PlaybackStats.merge(r0);
    }

    public PlaybackStats getPlaybackStats() {
        String r0 = this.sessionManager.getActiveSessionId();
        if (r0 != null) goto L5;
        PlaybackStatsTracker r2 = null;
    L6:
        if (r2 != null) goto L9;
        return null;
    L9:
        return r2.build(false);
    L5:
        r2 = this.playbackStatsTrackers.get(r0);
        goto L6
    }

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager.Listener
    public void onAdPlaybackStarted(AnalyticsListener.EventTime r2, String r3, String r4) {
        ((PlaybackStatsTracker) Assertions.checkNotNull(this.playbackStatsTrackers.get(r3))).onInterruptedByAd();
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
    public void onBandwidthEstimate(AnalyticsListener.EventTime r3, int r4, long r5, long r7) {
        this.bandwidthTimeMs = r4;
        this.bandwidthBytes = r5;
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
        if (r4.trackType != 2) goto L5;
    L10:
        this.videoFormat = r4.trackFormat;
        return;
    L5:
        if (r4.trackType == 0) goto L10;
        if (r4.trackType != 1) goto L12;
        this.audioFormat = r4.trackFormat;
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
    public void onDrmSessionManagerError(AnalyticsListener.EventTime r1, Exception r2) {
        this.nonFatalException = r2;
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onDrmSessionReleased(AnalyticsListener.EventTime r1) {
        AnalyticsListener.CC.$default$onDrmSessionReleased(this, r1);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onDroppedVideoFrames(AnalyticsListener.EventTime r1, int r2, long r3) {
        this.droppedFrames = r2;
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onEvents(Player r33, AnalyticsListener.Events r34) {
        if (r34.size() != 0) goto L5;
        return;
    L5:
        maybeAddSessions(r34);
        Iterator<String> r2 = this.playbackStatsTrackers.keySet().iterator();
    L7:
        if (r2.hasNext() == false) goto L45;
        String r3 = r2.next();
        Pair<AnalyticsListener.EventTime, Boolean> r5 = findBestEventTime(r34, r3);
        PlaybackStatsTracker r7 = this.playbackStatsTrackers.get(r3);
        boolean r13 = hasEvent(r34, r3, 11);
        boolean r6 = hasEvent(r34, r3, 1018);
        boolean r15 = hasEvent(r34, r3, 1011);
        boolean r16 = hasEvent(r34, r3, 1000);
        boolean r26 = hasEvent(r34, r3, 10);
        int r9 = 0;
        if (hasEvent(r34, r3, 1003) == false) goto L11;
    L14:
        boolean r8 = true;
    L15:
        boolean r27 = r8;
        boolean r28 = hasEvent(r34, r3, 1006);
        boolean r29 = hasEvent(r34, r3, 1004);
        boolean r30 = hasEvent(r34, r3, 25);
        AnalyticsListener.EventTime r10 = (AnalyticsListener.EventTime) r5.first;
        boolean r11 = ((Boolean) r5.second).booleanValue();
        if (r3.equals(this.discontinuityFromSession) == false) goto L18;
        long r12 = this.discontinuityFromPositionMs;
    L19:
        if (r6 == false) goto L21;
        r9 = this.droppedFrames;
    L21:
        int r14 = r9;
        if (r26 == false) goto L24;
        PlaybackException r17 = r33.getPlayerError();
    L25:
        if (r27 == false) goto L27;
        Exception r18 = this.nonFatalException;
    L28:
        long r19 = 0;
        if (r28 == false) goto L31;
        long r4 = this.bandwidthTimeMs;
    L32:
        if (r28 == false) goto L35;
        r19 = this.bandwidthBytes;
    L35:
        if (r29 == false) goto L37;
        Format r23 = this.videoFormat;
    L38:
        if (r29 == false) goto L40;
        Format r24 = this.audioFormat;
    L41:
        if (r30 == false) goto L43;
        VideoSize r25 = this.videoSize;
    L44:
        r7.onEvents(r33, r10, r11, r12, r13, r14, r15, r16, r17, r18, r4, r19, r23, r24, r25);
        goto L7
    L43:
        r25 = null;
        goto L44
    L40:
        r24 = null;
        goto L41
    L37:
        r23 = null;
        goto L38
    L31:
        r4 = 0;
        goto L32
    L27:
        r18 = null;
        goto L28
    L24:
        r17 = null;
        goto L25
    L18:
        r12 = C0180C.TIME_UNSET;
        goto L19
    L11:
        if (hasEvent(r34, r3, 1024) == true) goto L14;
        r8 = false;
        goto L15
    L45:
        this.videoFormat = null;
        this.audioFormat = null;
        this.discontinuityFromSession = null;
        if (r34.contains(AnalyticsListener.EVENT_PLAYER_RELEASED) == false) goto L52;
        this.sessionManager.finishAllSessions(r34.getEventTime(AnalyticsListener.EVENT_PLAYER_RELEASED));
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
    public void onLoadError(AnalyticsListener.EventTime r1, LoadEventInfo r2, MediaLoadData r3, IOException r4, boolean r5) {
        this.nonFatalException = r4;
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
    public void onPositionDiscontinuity(AnalyticsListener.EventTime r3, Player.PositionInfo r4, Player.PositionInfo r5, int r6) {
        if (this.discontinuityFromSession != null) goto L5;
        this.discontinuityFromSession = this.sessionManager.getActiveSessionId();
        this.discontinuityFromPositionMs = r4.positionMs;
    L5:
        this.discontinuityReason = r6;
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
    public void onSessionActive(AnalyticsListener.EventTime r2, String r3) {
        ((PlaybackStatsTracker) Assertions.checkNotNull(this.playbackStatsTrackers.get(r3))).onForeground();
    }

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager.Listener
    public void onSessionCreated(AnalyticsListener.EventTime r3, String r4) {
        PlaybackStatsTracker r0 = new PlaybackStatsTracker(this.keepHistory, r3);
        this.playbackStatsTrackers.put(r4, r0);
        this.sessionStartEventTimes.put(r4, r3);
    }

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager.Listener
    public void onSessionFinished(AnalyticsListener.EventTime r10, String r11, boolean r12) {
        PlaybackStatsTracker r0 = (PlaybackStatsTracker) Assertions.checkNotNull(this.playbackStatsTrackers.remove(r11));
        AnalyticsListener.EventTime r1 = (AnalyticsListener.EventTime) Assertions.checkNotNull(this.sessionStartEventTimes.remove(r11));
        if (r11.equals(this.discontinuityFromSession) == false) goto L5;
        long r2 = this.discontinuityFromPositionMs;
    L6:
        r0.onFinished(r10, r12, r2);
        PlaybackStats r5 = r0.build(true);
        this.finishedPlaybackStats = PlaybackStats.merge(new PlaybackStats[]{this.finishedPlaybackStats, r5});
        if (this.callback == null) goto L10;
        this.callback.onPlaybackStatsReady(r1, r5);
        return;
    L10:
        return;
    L5:
        r2 = C0180C.TIME_UNSET;
        goto L6
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
    public /* synthetic */ void onVideoInputFormatChanged(AnalyticsListener.EventTime r1, Format r2, DecoderReuseEvaluation r3) {
        AnalyticsListener.CC.$default$onVideoInputFormatChanged(this, r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onVideoSizeChanged(AnalyticsListener.EventTime r1, int r2, int r3, int r4, float r5) {
        AnalyticsListener.CC.$default$onVideoSizeChanged(this, r1, r2, r3, r4, r5);
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public void onVideoSizeChanged(AnalyticsListener.EventTime r1, VideoSize r2) {
        this.videoSize = r2;
    }

    @Override // androidx.media3.exoplayer.analytics.AnalyticsListener
    public /* synthetic */ void onVolumeChanged(AnalyticsListener.EventTime r1, float r2) {
        AnalyticsListener.CC.$default$onVolumeChanged(this, r1, r2);
    }
}
