package androidx.media3.exoplayer.analytics;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class PlaybackStats {
    public static final PlaybackStats EMPTY = null;
    public static final int PLAYBACK_STATE_ABANDONED = 15;
    public static final int PLAYBACK_STATE_BUFFERING = 6;
    static final int PLAYBACK_STATE_COUNT = 16;
    public static final int PLAYBACK_STATE_ENDED = 11;
    public static final int PLAYBACK_STATE_FAILED = 13;
    public static final int PLAYBACK_STATE_INTERRUPTED_BY_AD = 14;
    public static final int PLAYBACK_STATE_JOINING_BACKGROUND = 1;
    public static final int PLAYBACK_STATE_JOINING_FOREGROUND = 2;
    public static final int PLAYBACK_STATE_NOT_STARTED = 0;
    public static final int PLAYBACK_STATE_PAUSED = 4;
    public static final int PLAYBACK_STATE_PAUSED_BUFFERING = 7;
    public static final int PLAYBACK_STATE_PLAYING = 3;
    public static final int PLAYBACK_STATE_SEEKING = 5;
    public static final int PLAYBACK_STATE_STOPPED = 12;
    public static final int PLAYBACK_STATE_SUPPRESSED = 9;
    public static final int PLAYBACK_STATE_SUPPRESSED_BUFFERING = 10;
    public final int abandonedBeforeReadyCount;
    public final int adPlaybackCount;
    public final List<EventTimeAndFormat> audioFormatHistory;
    public final int backgroundJoiningCount;
    public final int endedCount;
    public final int fatalErrorCount;
    public final List<EventTimeAndException> fatalErrorHistory;
    public final int fatalErrorPlaybackCount;
    public final long firstReportedTimeMs;
    public final int foregroundPlaybackCount;
    public final int initialAudioFormatBitrateCount;
    public final int initialVideoFormatBitrateCount;
    public final int initialVideoFormatHeightCount;
    public final long maxRebufferTimeMs;
    public final List<long[]> mediaTimeHistory;
    public final int nonFatalErrorCount;
    public final List<EventTimeAndException> nonFatalErrorHistory;
    public final int playbackCount;
    private final long[] playbackStateDurationsMs;
    public final List<EventTimeAndPlaybackState> playbackStateHistory;
    public final long totalAudioFormatBitrateTimeProduct;
    public final long totalAudioFormatTimeMs;
    public final long totalAudioUnderruns;
    public final long totalBandwidthBytes;
    public final long totalBandwidthTimeMs;
    public final long totalDroppedFrames;
    public final long totalInitialAudioFormatBitrate;
    public final long totalInitialVideoFormatBitrate;
    public final int totalInitialVideoFormatHeight;
    public final int totalPauseBufferCount;
    public final int totalPauseCount;
    public final int totalRebufferCount;
    public final int totalSeekCount;
    public final long totalValidJoinTimeMs;
    public final long totalVideoFormatBitrateTimeMs;
    public final long totalVideoFormatBitrateTimeProduct;
    public final long totalVideoFormatHeightTimeMs;
    public final long totalVideoFormatHeightTimeProduct;
    public final int validJoinTimeCount;
    public final List<EventTimeAndFormat> videoFormatHistory;

    public static final class EventTimeAndException {
        public final AnalyticsListener.EventTime eventTime;
        public final Exception exception;

        public EventTimeAndException(AnalyticsListener.EventTime r1, Exception r2) {
            this.eventTime = r1;
            this.exception = r2;
        }

        public boolean equals(Object r5) {
            if (this != r5) goto L6;
            return true;
        L6:
            if (r5 != null) goto L8;
        L15:
            return false;
        L8:
            if (getClass() != r5.getClass()) goto L15;
            EventTimeAndException r1 = (EventTimeAndException) r5;
            if (this.eventTime.equals(r1.eventTime) == true) goto L14;
            return false;
        L14:
            return this.exception.equals(r1.exception);
        }

        public int hashCode() {
            return (this.eventTime.hashCode() * 31) + this.exception.hashCode();
        }
    }

    public static final class EventTimeAndFormat {
        public final AnalyticsListener.EventTime eventTime;
        public final Format format;

        public EventTimeAndFormat(AnalyticsListener.EventTime r1, Format r2) {
            this.eventTime = r1;
            this.format = r2;
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if (r6 != null) goto L8;
        L21:
            return false;
        L8:
            if (getClass() != r6.getClass()) goto L21;
            EventTimeAndFormat r2 = (EventTimeAndFormat) r6;
            if (this.eventTime.equals(r2.eventTime) == true) goto L14;
            return false;
        L14:
            if (this.format == null) goto L17;
            return this.format.equals(r2.format);
        L17:
            if (r2.format != null) goto L20;
            return true;
        L20:
            return false;
        }

        public int hashCode() {
            int r1 = this.eventTime.hashCode() * 31;
            if (this.format == null) goto L5;
            int r2 = this.format.hashCode();
        L7:
            return r1 + r2;
        L5:
            r2 = 0;
            goto L7
        }
    }

    public static final class EventTimeAndPlaybackState {
        public final AnalyticsListener.EventTime eventTime;
        public final int playbackState;

        public EventTimeAndPlaybackState(AnalyticsListener.EventTime r1, int r2) {
            this.eventTime = r1;
            this.playbackState = r2;
        }

        public boolean equals(Object r5) {
            if (this != r5) goto L6;
            return true;
        L6:
            if (r5 != null) goto L8;
        L15:
            return false;
        L8:
            if (getClass() != r5.getClass()) goto L15;
            EventTimeAndPlaybackState r1 = (EventTimeAndPlaybackState) r5;
            if (this.playbackState == r1.playbackState) goto L14;
            return false;
        L14:
            return this.eventTime.equals(r1.eventTime);
        }

        public int hashCode() {
            return (this.eventTime.hashCode() * 31) + this.playbackState;
        }
    }

    static {
        EMPTY = merge(new PlaybackStats[0]);
    }

    PlaybackStats(int r17, long[] r18, List<EventTimeAndPlaybackState> r19, List<long[]> r20, long r21, int r23, int r24, int r25, int r26, long r27, int r29, int r30, int r31, int r32, int r33, long r34, int r36, List<EventTimeAndFormat> r37, List<EventTimeAndFormat> r38, long r39, long r41, long r43, long r45, long r47, long r49, int r51, int r52, int r53, long r54, int r56, long r57, long r59, long r61, long r63, long r65, int r67, int r68, int r69, List<EventTimeAndException> r70, List<EventTimeAndException> r71) {
        this.playbackCount = r17;
        this.playbackStateDurationsMs = r18;
        this.playbackStateHistory = Collections.unmodifiableList(r19);
        this.mediaTimeHistory = Collections.unmodifiableList(r20);
        this.firstReportedTimeMs = r21;
        this.foregroundPlaybackCount = r23;
        this.abandonedBeforeReadyCount = r24;
        this.endedCount = r25;
        this.backgroundJoiningCount = r26;
        this.totalValidJoinTimeMs = r27;
        this.validJoinTimeCount = r29;
        this.totalPauseCount = r30;
        this.totalPauseBufferCount = r31;
        this.totalSeekCount = r32;
        this.totalRebufferCount = r33;
        this.maxRebufferTimeMs = r34;
        this.adPlaybackCount = r36;
        this.videoFormatHistory = Collections.unmodifiableList(r37);
        this.audioFormatHistory = Collections.unmodifiableList(r38);
        this.totalVideoFormatHeightTimeMs = r39;
        this.totalVideoFormatHeightTimeProduct = r41;
        this.totalVideoFormatBitrateTimeMs = r43;
        this.totalVideoFormatBitrateTimeProduct = r45;
        this.totalAudioFormatTimeMs = r47;
        this.totalAudioFormatBitrateTimeProduct = r49;
        this.initialVideoFormatHeightCount = r51;
        this.initialVideoFormatBitrateCount = r52;
        this.totalInitialVideoFormatHeight = r53;
        this.totalInitialVideoFormatBitrate = r54;
        this.initialAudioFormatBitrateCount = r56;
        this.totalInitialAudioFormatBitrate = r57;
        this.totalBandwidthTimeMs = r59;
        this.totalBandwidthBytes = r61;
        this.totalDroppedFrames = r63;
        this.totalAudioUnderruns = r65;
        this.fatalErrorPlaybackCount = r67;
        this.fatalErrorCount = r68;
        this.nonFatalErrorCount = r69;
        this.fatalErrorHistory = Collections.unmodifiableList(r70);
        this.nonFatalErrorHistory = Collections.unmodifiableList(r71);
    }

    public static PlaybackStats merge(PlaybackStats... r65) {
        long[] r5 = new long[16];
        int r2 = r65.length;
        int r54 = 0;
        int r55 = 0;
        int r56 = 0;
        long r48 = 0;
        long r50 = 0;
        int r43 = 0;
        long r44 = -1;
        int r38 = 0;
        int r39 = 0;
        long r32 = 0;
        long r26 = 0;
        int r20 = 0;
        int r16 = 0;
        int r12 = 0;
        int r4 = 0;
        int r1 = -1;
        int r3 = 0;
        long r52 = 0;
        long r46 = 0;
        long r41 = -1;
        long r34 = 0;
        long r36 = 0;
        long r28 = 0;
        long r30 = 0;
        int r23 = 0;
        int r19 = 0;
        int r10 = 0;
        int r11 = 0;
        long r6 = -9223372036854775807L;
        int r17 = 0;
        int r18 = 0;
        int r13 = 0;
        long r14 = -9223372036854775807L;
        long r8 = -9223372036854775807L;
        int r21 = 16;
    L3:
        if (r3 >= r2) goto L46;
        PlaybackStats r0 = r65[r3];
        int r22 = r2;
        r4 = r4 + r0.playbackCount;
        int r7 = 0;
    L5:
        int r24 = r3;
        if (r7 >= r21) goto L9;
        r5[r7] = r5[r7] + r0.playbackStateDurationsMs[r7];
        r7 = r7 + 1;
        r3 = r24;
        r21 = 16;
        goto L5
    L9:
        if (r8 != C0180C.TIME_UNSET) goto L11;
        r8 = r0.firstReportedTimeMs;
        long r57 = -9223372036854775807L;
    L14:
        r10 = r10 + r0.foregroundPlaybackCount;
        r11 = r11 + r0.abandonedBeforeReadyCount;
        r12 = r12 + r0.endedCount;
        r13 = r13 + r0.backgroundJoiningCount;
        if (r14 != r57) goto L18;
        r14 = r0.totalValidJoinTimeMs;
    L20:
        r16 = r16 + r0.validJoinTimeCount;
        r17 = r17 + r0.totalPauseCount;
        r18 = r18 + r0.totalPauseBufferCount;
        r19 = r19 + r0.totalSeekCount;
        r20 = r20 + r0.totalRebufferCount;
        if (r6 != r57) goto L24;
        r6 = r0.maxRebufferTimeMs;
    L26:
        r23 = r23 + r0.adPlaybackCount;
        r26 = r26 + r0.totalVideoFormatHeightTimeMs;
        r28 = r28 + r0.totalVideoFormatHeightTimeProduct;
        r30 = r30 + r0.totalVideoFormatBitrateTimeMs;
        r32 = r32 + r0.totalVideoFormatBitrateTimeProduct;
        r34 = r34 + r0.totalAudioFormatTimeMs;
        r36 = r36 + r0.totalAudioFormatBitrateTimeProduct;
        r38 = r38 + r0.initialVideoFormatHeightCount;
        r39 = r39 + r0.initialVideoFormatBitrateCount;
        if (r1 != (-1)) goto L30;
        r1 = r0.totalInitialVideoFormatHeight;
    L33:
        if (r41 != (-1)) goto L35;
        long r58 = -1;
        r41 = r0.totalInitialVideoFormatBitrate;
    L38:
        r43 = r43 + r0.initialAudioFormatBitrateCount;
        if (r44 != r58) goto L42;
        r44 = r0.totalInitialAudioFormatBitrate;
    L44:
        r46 = r46 + r0.totalBandwidthTimeMs;
        r48 = r48 + r0.totalBandwidthBytes;
        r50 = r50 + r0.totalDroppedFrames;
        r52 = r52 + r0.totalAudioUnderruns;
        r54 = r54 + r0.fatalErrorPlaybackCount;
        r55 = r55 + r0.fatalErrorCount;
        r56 = r56 + r0.nonFatalErrorCount;
        r3 = r24 + 1;
        r2 = r22;
        r21 = 16;
        goto L3
    L42:
        if (r0.totalInitialAudioFormatBitrate == r58) goto L44;
        r44 = r44 + r0.totalInitialAudioFormatBitrate;
        goto L44
    L35:
        r58 = -1;
        if (r0.totalInitialVideoFormatBitrate == (-1)) goto L38;
        r41 = r41 + r0.totalInitialVideoFormatBitrate;
        goto L38
    L30:
        if (r0.totalInitialVideoFormatHeight == (-1)) goto L33;
        r1 = r1 + r0.totalInitialVideoFormatHeight;
        goto L33
    L24:
        if (r0.maxRebufferTimeMs == r57) goto L26;
        r6 = Math.max(r6, r0.maxRebufferTimeMs);
        goto L26
    L18:
        if (r0.totalValidJoinTimeMs == r57) goto L20;
        r14 = r14 + r0.totalValidJoinTimeMs;
        goto L20
    L11:
        r57 = -9223372036854775807L;
        if (r0.firstReportedTimeMs == C0180C.TIME_UNSET) goto L14;
        r8 = Math.min(r8, r0.firstReportedTimeMs);
        goto L14
    L46:
        return new PlaybackStats(r4, r5, Collections.emptyList(), Collections.emptyList(), r8, r10, r11, r12, r13, r14, r16, r17, r18, r19, r20, r6, r23, Collections.emptyList(), Collections.emptyList(), r26, r28, r30, r32, r34, r36, r38, r39, r1, r41, r43, r44, r46, r48, r50, r52, r54, r55, r56, Collections.emptyList(), Collections.emptyList());
    }

    public float getAbandonedBeforeReadyRatio() {
        int r0 = this.abandonedBeforeReadyCount - (this.playbackCount - this.foregroundPlaybackCount);
        if (this.foregroundPlaybackCount != 0) goto L6;
        return 0.0f;
    L6:
        return r0 / this.foregroundPlaybackCount;
    }

    public float getAudioUnderrunRate() {
        long r0 = getTotalPlayTimeMs();
        if (r0 != 0) goto L6;
        return 0.0f;
    L6:
        return (this.totalAudioUnderruns * 1000.0f) / r0;
    }

    public float getDroppedFramesRate() {
        long r0 = getTotalPlayTimeMs();
        if (r0 != 0) goto L6;
        return 0.0f;
    L6:
        return (this.totalDroppedFrames * 1000.0f) / r0;
    }

    public float getEndedRatio() {
        if (this.foregroundPlaybackCount != 0) goto L6;
        return 0.0f;
    L6:
        return this.endedCount / this.foregroundPlaybackCount;
    }

    public float getFatalErrorRate() {
        long r0 = getTotalPlayTimeMs();
        if (r0 != 0) goto L6;
        return 0.0f;
    L6:
        return (this.fatalErrorCount * 1000.0f) / r0;
    }

    public float getFatalErrorRatio() {
        if (this.foregroundPlaybackCount != 0) goto L6;
        return 0.0f;
    L6:
        return this.fatalErrorPlaybackCount / this.foregroundPlaybackCount;
    }

    public float getJoinTimeRatio() {
        long r0 = getTotalPlayAndWaitTimeMs();
        if (r0 != 0) goto L6;
        return 0.0f;
    L6:
        return getTotalJoinTimeMs() / r0;
    }

    public int getMeanAudioFormatBitrate() {
        if (this.totalAudioFormatTimeMs != 0) goto L6;
        return -1;
    L6:
        return (int) (this.totalAudioFormatBitrateTimeProduct / this.totalAudioFormatTimeMs);
    }

    public int getMeanBandwidth() {
        if (this.totalBandwidthTimeMs != 0) goto L6;
        return -1;
    L6:
        return (int) ((this.totalBandwidthBytes * 8000) / this.totalBandwidthTimeMs);
    }

    public long getMeanElapsedTimeMs() {
        if (this.playbackCount != 0) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        return getTotalElapsedTimeMs() / ((long) this.playbackCount);
    }

    public int getMeanInitialAudioFormatBitrate() {
        if (this.initialAudioFormatBitrateCount != 0) goto L6;
        return -1;
    L6:
        return (int) (this.totalInitialAudioFormatBitrate / ((long) this.initialAudioFormatBitrateCount));
    }

    public int getMeanInitialVideoFormatBitrate() {
        if (this.initialVideoFormatBitrateCount != 0) goto L6;
        return -1;
    L6:
        return (int) (this.totalInitialVideoFormatBitrate / ((long) this.initialVideoFormatBitrateCount));
    }

    public int getMeanInitialVideoFormatHeight() {
        if (this.initialVideoFormatHeightCount != 0) goto L6;
        return -1;
    L6:
        return this.totalInitialVideoFormatHeight / this.initialVideoFormatHeightCount;
    }

    public long getMeanJoinTimeMs() {
        if (this.validJoinTimeCount != 0) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        return this.totalValidJoinTimeMs / ((long) this.validJoinTimeCount);
    }

    public float getMeanNonFatalErrorCount() {
        if (this.foregroundPlaybackCount != 0) goto L6;
        return 0.0f;
    L6:
        return this.nonFatalErrorCount / this.foregroundPlaybackCount;
    }

    public float getMeanPauseBufferCount() {
        if (this.foregroundPlaybackCount != 0) goto L6;
        return 0.0f;
    L6:
        return this.totalPauseBufferCount / this.foregroundPlaybackCount;
    }

    public float getMeanPauseCount() {
        if (this.foregroundPlaybackCount != 0) goto L6;
        return 0.0f;
    L6:
        return this.totalPauseCount / this.foregroundPlaybackCount;
    }

    public long getMeanPausedTimeMs() {
        if (this.foregroundPlaybackCount != 0) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        return getTotalPausedTimeMs() / ((long) this.foregroundPlaybackCount);
    }

    public long getMeanPlayAndWaitTimeMs() {
        if (this.foregroundPlaybackCount != 0) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        return getTotalPlayAndWaitTimeMs() / ((long) this.foregroundPlaybackCount);
    }

    public long getMeanPlayTimeMs() {
        if (this.foregroundPlaybackCount != 0) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        return getTotalPlayTimeMs() / ((long) this.foregroundPlaybackCount);
    }

    public float getMeanRebufferCount() {
        if (this.foregroundPlaybackCount != 0) goto L6;
        return 0.0f;
    L6:
        return this.totalRebufferCount / this.foregroundPlaybackCount;
    }

    public long getMeanRebufferTimeMs() {
        if (this.foregroundPlaybackCount != 0) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        return getTotalRebufferTimeMs() / ((long) this.foregroundPlaybackCount);
    }

    public float getMeanSeekCount() {
        if (this.foregroundPlaybackCount != 0) goto L6;
        return 0.0f;
    L6:
        return this.totalSeekCount / this.foregroundPlaybackCount;
    }

    public long getMeanSeekTimeMs() {
        if (this.foregroundPlaybackCount != 0) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        return getTotalSeekTimeMs() / ((long) this.foregroundPlaybackCount);
    }

    public long getMeanSingleRebufferTimeMs() {
        if (this.totalRebufferCount != 0) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        return (getPlaybackStateDurationMs(6) + getPlaybackStateDurationMs(7)) / ((long) this.totalRebufferCount);
    }

    public long getMeanSingleSeekTimeMs() {
        if (this.totalSeekCount != 0) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        return getTotalSeekTimeMs() / ((long) this.totalSeekCount);
    }

    public float getMeanTimeBetweenFatalErrors() {
        return 1.0f / getFatalErrorRate();
    }

    public float getMeanTimeBetweenNonFatalErrors() {
        return 1.0f / getNonFatalErrorRate();
    }

    public float getMeanTimeBetweenRebuffers() {
        return 1.0f / getRebufferRate();
    }

    public int getMeanVideoFormatBitrate() {
        if (this.totalVideoFormatBitrateTimeMs != 0) goto L6;
        return -1;
    L6:
        return (int) (this.totalVideoFormatBitrateTimeProduct / this.totalVideoFormatBitrateTimeMs);
    }

    public int getMeanVideoFormatHeight() {
        if (this.totalVideoFormatHeightTimeMs != 0) goto L6;
        return -1;
    L6:
        return (int) (this.totalVideoFormatHeightTimeProduct / this.totalVideoFormatHeightTimeMs);
    }

    public long getMeanWaitTimeMs() {
        if (this.foregroundPlaybackCount != 0) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        return getTotalWaitTimeMs() / ((long) this.foregroundPlaybackCount);
    }

    public long getMediaTimeMsAtRealtimeMs(long r17) {
        if (this.mediaTimeHistory.isEmpty() == false) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        int r1 = 0;
    L8:
        if (r1 >= this.mediaTimeHistory.size()) goto L13;
        if (this.mediaTimeHistory.get(r1)[0] > r17) goto L13;
        r1 = r1 + 1;
    L13:
        if (r1 != 0) goto L17;
        return this.mediaTimeHistory.get(0)[1];
    L17:
        if (r1 == this.mediaTimeHistory.size()) goto L19;
        long r5 = this.mediaTimeHistory.get(r1 - 1)[0];
        long r7 = this.mediaTimeHistory.get(r1 - 1)[1];
        long r3 = this.mediaTimeHistory.get(r1)[0];
        long r10 = this.mediaTimeHistory.get(r1)[1];
        long r12 = r3 - r5;
        if (r12 != 0) goto L24;
        return r7;
    L24:
        return ((long) ((r10 - r7) * ((r17 - r5) / r12))) + r7;
    L19:
        return this.mediaTimeHistory.get(this.mediaTimeHistory.size() - 1)[1];
    }

    public float getNonFatalErrorRate() {
        long r0 = getTotalPlayTimeMs();
        if (r0 != 0) goto L6;
        return 0.0f;
    L6:
        return (this.nonFatalErrorCount * 1000.0f) / r0;
    }

    public int getPlaybackStateAtTime(long r6) {
        int r0 = 0;
        Iterator<EventTimeAndPlaybackState> r1 = this.playbackStateHistory.iterator();
    L4:
        if (r1.hasNext() == false) goto L9;
        EventTimeAndPlaybackState r2 = r1.next();
        if (r2.eventTime.realtimeMs > r6) goto L9;
        r0 = r2.playbackState;
    L9:
        return r0;
    }

    public long getPlaybackStateDurationMs(int r4) {
        return this.playbackStateDurationsMs[r4];
    }

    public float getRebufferRate() {
        long r0 = getTotalPlayTimeMs();
        if (r0 != 0) goto L6;
        return 0.0f;
    L6:
        return (this.totalRebufferCount * 1000.0f) / r0;
    }

    public float getRebufferTimeRatio() {
        long r0 = getTotalPlayAndWaitTimeMs();
        if (r0 != 0) goto L6;
        return 0.0f;
    L6:
        return getTotalRebufferTimeMs() / r0;
    }

    public float getSeekTimeRatio() {
        long r0 = getTotalPlayAndWaitTimeMs();
        if (r0 != 0) goto L6;
        return 0.0f;
    L6:
        return getTotalSeekTimeMs() / r0;
    }

    public long getTotalElapsedTimeMs() {
        long r0 = 0;
        int r2 = 0;
    L4:
        if (r2 >= 16) goto L6;
        r0 = r0 + this.playbackStateDurationsMs[r2];
        r2 = r2 + 1;
        goto L4
    L6:
        return r0;
    }

    public long getTotalJoinTimeMs() {
        return getPlaybackStateDurationMs(2);
    }

    public long getTotalPausedTimeMs() {
        return getPlaybackStateDurationMs(4) + getPlaybackStateDurationMs(7);
    }

    public long getTotalPlayAndWaitTimeMs() {
        return getTotalPlayTimeMs() + getTotalWaitTimeMs();
    }

    public long getTotalPlayTimeMs() {
        return getPlaybackStateDurationMs(3);
    }

    public long getTotalRebufferTimeMs() {
        return getPlaybackStateDurationMs(6);
    }

    public long getTotalSeekTimeMs() {
        return getPlaybackStateDurationMs(5);
    }

    public long getTotalWaitTimeMs() {
        return (getPlaybackStateDurationMs(2) + getPlaybackStateDurationMs(6)) + getPlaybackStateDurationMs(5);
    }

    public float getWaitTimeRatio() {
        long r0 = getTotalPlayAndWaitTimeMs();
        if (r0 != 0) goto L6;
        return 0.0f;
    L6:
        return getTotalWaitTimeMs() / r0;
    }
}
