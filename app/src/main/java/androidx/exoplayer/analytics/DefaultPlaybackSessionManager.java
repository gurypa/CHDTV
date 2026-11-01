package androidx.media3.exoplayer.analytics;

import android.util.Base64;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.source.MediaSource;
import com.google.common.base.Supplier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultPlaybackSessionManager implements PlaybackSessionManager {
    public static final Supplier<String> DEFAULT_SESSION_ID_GENERATOR = null;
    private static final Random RANDOM = null;
    private static final int SESSION_ID_LENGTH = 12;
    private String currentSessionId;
    private Timeline currentTimeline;
    private long lastRemovedCurrentWindowSequenceNumber;
    private PlaybackSessionManager.Listener listener;
    private final Timeline.Period period;
    private final Supplier<String> sessionIdGenerator;
    private final HashMap<String, SessionDescriptor> sessions;
    private final Timeline.Window window;

    private final class SessionDescriptor {
        private MediaSource.MediaPeriodId adMediaPeriodId;
        private boolean isActive;
        private boolean isCreated;
        private final String sessionId;
        final /* synthetic */ DefaultPlaybackSessionManager this$0;
        private int windowIndex;
        private long windowSequenceNumber;

        public SessionDescriptor(DefaultPlaybackSessionManager r3, String r4, int r5, MediaSource.MediaPeriodId r6) {
            this.this$0 = r3;
            this.sessionId = r4;
            this.windowIndex = r5;
            if (r6 != null) goto L5;
            long r0 = -1;
        L6:
            this.windowSequenceNumber = r0;
            if (r6 != null) goto L9;
            return;
        L9:
            if (r6.isAd() == false) goto L13;
            this.adMediaPeriodId = r6;
            return;
        L13:
            return;
        L5:
            r0 = r6.windowSequenceNumber;
            goto L6
        }

        static /* synthetic */ String access$000(SessionDescriptor r1) {
            return r1.sessionId;
        }

        static /* synthetic */ long access$100(SessionDescriptor r2) {
            return r2.windowSequenceNumber;
        }

        static /* synthetic */ int access$200(SessionDescriptor r1) {
            return r1.windowIndex;
        }

        static /* synthetic */ boolean access$300(SessionDescriptor r1) {
            return r1.isCreated;
        }

        static /* synthetic */ boolean access$302(SessionDescriptor r0, boolean r1) {
            r0.isCreated = r1;
            return r1;
        }

        static /* synthetic */ boolean access$400(SessionDescriptor r1) {
            return r1.isActive;
        }

        static /* synthetic */ boolean access$402(SessionDescriptor r0, boolean r1) {
            r0.isActive = r1;
            return r1;
        }

        static /* synthetic */ MediaSource.MediaPeriodId access$500(SessionDescriptor r1) {
            return r1.adMediaPeriodId;
        }

        private int resolveWindowIndexToNewTimeline(Timeline r5, Timeline r6, int r7) {
            if (r7 >= r5.getWindowCount()) goto L5;
            r5.getWindow(r7, DefaultPlaybackSessionManager.access$700(this.this$0));
            int r0 = DefaultPlaybackSessionManager.access$700(this.this$0).firstPeriodIndex;
        L10:
            if (r0 > DefaultPlaybackSessionManager.access$700(this.this$0).lastPeriodIndex) goto L16;
            int r3 = r6.getIndexOfPeriod(r5.getUidOfPeriod(r0));
            if (r3 != (-1)) goto L14;
            r0 = r0 + 1;
            goto L10
        L14:
            return r6.getPeriod(r3, DefaultPlaybackSessionManager.access$800(this.this$0)).windowIndex;
        L16:
            return -1;
        L5:
            if (r7 < r6.getWindowCount()) goto L7;
            return -1;
        L7:
            return r7;
        }

        public boolean belongsToSession(int r7, MediaSource.MediaPeriodId r8) {
            if (r8 != null) goto L10;
            if (r7 != this.windowIndex) goto L8;
            return true;
        L8:
            return false;
        L10:
            if (this.adMediaPeriodId != null) goto L19;
            if (r8.isAd() == true) goto L17;
            if (r8.windowSequenceNumber != this.windowSequenceNumber) goto L17;
            return true;
        L17:
            return false;
        L19:
            if (r8.windowSequenceNumber != this.adMediaPeriodId.windowSequenceNumber) goto L26;
            if (r8.adGroupIndex != this.adMediaPeriodId.adGroupIndex) goto L26;
            if (r8.adIndexInAdGroup != this.adMediaPeriodId.adIndexInAdGroup) goto L26;
            return true;
        L26:
            return false;
        }

        public boolean isFinishedAtEventTime(AnalyticsListener.EventTime r9) {
            if (r9.mediaPeriodId != null) goto L10;
            if (this.windowIndex == r9.windowIndex) goto L8;
            return true;
        L8:
            return false;
        L10:
            if (this.windowSequenceNumber != (-1)) goto L13;
            return false;
        L13:
            if (r9.mediaPeriodId.windowSequenceNumber <= this.windowSequenceNumber) goto L16;
            return true;
        L16:
            if (this.adMediaPeriodId != null) goto L18;
            return false;
        L18:
            int r0 = r9.timeline.getIndexOfPeriod(r9.mediaPeriodId.periodUid);
            int r3 = r9.timeline.getIndexOfPeriod(this.adMediaPeriodId.periodUid);
            if (r9.mediaPeriodId.windowSequenceNumber < this.adMediaPeriodId.windowSequenceNumber) goto L42;
            if (r0 < r3) goto L42;
            if (r0 <= r3) goto L25;
            return true;
        L25:
            if (r9.mediaPeriodId.isAd() == false) goto L36;
            int r4 = r9.mediaPeriodId.adGroupIndex;
            int r5 = r9.mediaPeriodId.adIndexInAdGroup;
            if (r4 <= this.adMediaPeriodId.adGroupIndex) goto L29;
            return true;
        L29:
            if (r4 != this.adMediaPeriodId.adGroupIndex) goto L34;
            if (r5 <= this.adMediaPeriodId.adIndexInAdGroup) goto L34;
            return true;
        L34:
            return false;
        L36:
            if (r9.mediaPeriodId.nextAdGroupIndex != (-1)) goto L38;
            return true;
        L38:
            if (r9.mediaPeriodId.nextAdGroupIndex <= this.adMediaPeriodId.adGroupIndex) goto L41;
            return true;
        L41:
            return false;
        L42:
            return false;
        }

        public void maybeSetWindowSequenceNumber(int r5, MediaSource.MediaPeriodId r6) {
            if (this.windowSequenceNumber == (-1)) goto L5;
            return;
        L5:
            if (r5 != this.windowIndex) goto L12;
            if (r6 != null) goto L8;
            return;
        L8:
            if (r6.windowSequenceNumber < DefaultPlaybackSessionManager.access$600(this.this$0)) goto L14;
            this.windowSequenceNumber = r6.windowSequenceNumber;
            return;
        L14:
            return;
        }

        public boolean tryResolvingToNewTimeline(Timeline r5, Timeline r6) {
            this.windowIndex = resolveWindowIndexToNewTimeline(r5, r6, this.windowIndex);
            if (this.windowIndex != (-1)) goto L6;
            return false;
        L6:
            if (this.adMediaPeriodId != null) goto L9;
            return true;
        L9:
            if (r6.getIndexOfPeriod(this.adMediaPeriodId.periodUid) != (-1)) goto L11;
            return false;
        L11:
            return true;
        }
    }

    public static /* synthetic */ String $r8$lambda$EuouJd6Wgl4KHPnNDIH2Q_wUwdk() {
        return generateDefaultSessionId();
    }

    static {
        DEFAULT_SESSION_ID_GENERATOR = new DefaultPlaybackSessionManager$$ExternalSyntheticLambda0();
        RANDOM = new Random();
    }

    public DefaultPlaybackSessionManager() {
        this(DEFAULT_SESSION_ID_GENERATOR);
    }

    public DefaultPlaybackSessionManager(Supplier<String> r3) {
        this.sessionIdGenerator = r3;
        this.window = new Timeline.Window();
        this.period = new Timeline.Period();
        this.sessions = new HashMap();
        this.currentTimeline = Timeline.EMPTY;
        this.lastRemovedCurrentWindowSequenceNumber = -1;
    }

    static /* synthetic */ long access$600(DefaultPlaybackSessionManager r2) {
        return r2.getMinWindowSequenceNumber();
    }

    static /* synthetic */ Timeline.Window access$700(DefaultPlaybackSessionManager r1) {
        return r1.window;
    }

    static /* synthetic */ Timeline.Period access$800(DefaultPlaybackSessionManager r1) {
        return r1.period;
    }

    private void clearCurrentSession(SessionDescriptor r5) {
        if (SessionDescriptor.access$100(r5) == (-1)) goto L5;
        this.lastRemovedCurrentWindowSequenceNumber = SessionDescriptor.access$100(r5);
    L5:
        this.currentSessionId = null;
    }

    private static String generateDefaultSessionId() {
        byte[] r0 = new byte[12];
        RANDOM.nextBytes(r0);
        return Base64.encodeToString(r0, 10);
    }

    private long getMinWindowSequenceNumber() {
        SessionDescriptor r0 = this.sessions.get(this.currentSessionId);
        if (r0 == null) goto L8;
        if (SessionDescriptor.access$100(r0) == (-1)) goto L8;
        return SessionDescriptor.access$100(r0);
    L8:
        return this.lastRemovedCurrentWindowSequenceNumber + 1;
    }

    private SessionDescriptor getOrAddSession(int r10, MediaSource.MediaPeriodId r11) {
        SessionDescriptor r0 = null;
        long r1 = Long.MAX_VALUE;
        Iterator<SessionDescriptor> r3 = this.sessions.values().iterator();
    L4:
        if (r3.hasNext() == false) goto L21;
        SessionDescriptor r4 = r3.next();
        r4.maybeSetWindowSequenceNumber(r10, r11);
        if (r4.belongsToSession(r10, r11) == false) goto L4;
        long r5 = SessionDescriptor.access$100(r4);
        if (r5 == (-1)) goto L19;
        if (r5 < r1) goto L19;
        if (r5 != r1) goto L4;
        if (SessionDescriptor.access$500((SessionDescriptor) Util.castNonNull(r0)) == null) goto L4;
        if (SessionDescriptor.access$500(r4) == null) goto L4;
        r0 = r4;
    L19:
        r0 = r4;
        r1 = r5;
        goto L4
    L21:
        if (r0 != null) goto L39;
        String r6 = this.sessionIdGenerator.get();
        SessionDescriptor r7 = new SessionDescriptor(this, r6, r10, r11);
        this.sessions.put(r6, r7);
        return r7;
    L39:
        return r0;
    }

    @RequiresNonNull({"listener"})
    private void updateCurrentSession(AnalyticsListener.EventTime r8) {
        if (r8.timeline.isEmpty() == true) goto L5;
        SessionDescriptor r0 = this.sessions.get(this.currentSessionId);
        SessionDescriptor r1 = getOrAddSession(r8.windowIndex, r8.mediaPeriodId);
        this.currentSessionId = SessionDescriptor.access$000(r1);
        updateSessions(r8);
        if (r8.mediaPeriodId != null) goto L11;
        return;
    L11:
        if (r8.mediaPeriodId.isAd() == false) goto L25;
        if (r0 != null) goto L14;
    L21:
        MediaSource.MediaPeriodId r2 = new MediaSource.MediaPeriodId(r8.mediaPeriodId.periodUid, r8.mediaPeriodId.windowSequenceNumber);
        SessionDescriptor r3 = getOrAddSession(r8.windowIndex, r2);
        this.listener.onAdPlaybackStarted(r8, SessionDescriptor.access$000(r3), SessionDescriptor.access$000(r1));
        return;
    L14:
        if (SessionDescriptor.access$100(r0) != r8.mediaPeriodId.windowSequenceNumber) goto L21;
        if (SessionDescriptor.access$500(r0) == null) goto L21;
        if (SessionDescriptor.access$500(r0).adGroupIndex != r8.mediaPeriodId.adGroupIndex) goto L21;
        if (SessionDescriptor.access$500(r0).adIndexInAdGroup != r8.mediaPeriodId.adIndexInAdGroup) goto L21;
        return;
    L25:
        return;
    L5:
        if (this.currentSessionId == null) goto L23;
        clearCurrentSession((SessionDescriptor) Assertions.checkNotNull(this.sessions.get(this.currentSessionId)));
        return;
    }

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager
    public synchronized boolean belongsToSession(AnalyticsListener.EventTime r4, String r5) {
        monitor-enter(this);
        SessionDescriptor r0 = this.sessions.get(r5);     // Catch: Throwable -> L11
        if (r0 != null) goto L8;
        monitor-exit(this);
        return false;
    L8:
        r0.maybeSetWindowSequenceNumber(r4.windowIndex, r4.mediaPeriodId);     // Catch: Throwable -> L11
        boolean r1 = r0.belongsToSession(r4.windowIndex, r4.mediaPeriodId);     // Catch: Throwable -> L11
        monitor-exit(this);
        return r1;
    L11:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager
    public synchronized void finishAllSessions(AnalyticsListener.EventTime r6) {
        monitor-enter(this);
    L17:
        th = move-exception;
        throw th;
    L4:
        if (this.currentSessionId == null) goto L6;
        clearCurrentSession((SessionDescriptor) Assertions.checkNotNull(this.sessions.get(this.currentSessionId)));     // Catch: Throwable -> L17
    L6:
        Iterator<SessionDescriptor> r0 = this.sessions.values().iterator();     // Catch: Throwable -> L17
    L7:
        if (r0.hasNext() == false) goto L15;
        SessionDescriptor r1 = r0.next();     // Catch: Throwable -> L17
        r0.remove();     // Catch: Throwable -> L17
        if (SessionDescriptor.access$300(r1) == false) goto L7;
        if (this.listener == null) goto L7;
        this.listener.onSessionFinished(r6, SessionDescriptor.access$000(r1), false);     // Catch: Throwable -> L17
        goto L7
    L15:
        monitor-exit(this);
    }

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager
    public synchronized String getActiveSessionId() {
        monitor-enter(this);
        String r0 = this.currentSessionId;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager
    public synchronized String getSessionForMediaPeriodId(Timeline r3, MediaSource.MediaPeriodId r4) {
        monitor-enter(this);
        String r1 = SessionDescriptor.access$000(getOrAddSession(r3.getPeriodByUid(r4.periodUid, this.period).windowIndex, r4));     // Catch: Throwable -> L6
        monitor-exit(this);
        return r1;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager
    public void setListener(PlaybackSessionManager.Listener r1) {
        this.listener = r1;
    }

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager
    public synchronized void updateSessions(AnalyticsListener.EventTime r25) {
        monitor-enter(this);
        Assertions.checkNotNull(this.listener);     // Catch: Throwable -> L43
        if (r25.timeline.isEmpty() == false) goto L9;
        monitor-exit(this);
        return;
    L9:
        if (r25.mediaPeriodId != null) goto L12;
    L23:
        SessionDescriptor r2 = getOrAddSession(r25.windowIndex, r25.mediaPeriodId);     // Catch: Throwable -> L43
        if (this.currentSessionId != null) goto L27;
        this.currentSessionId = SessionDescriptor.access$000(r2);     // Catch: Throwable -> L43
    L27:
        if (r25.mediaPeriodId == null) goto L34;
        if (r25.mediaPeriodId.isAd() == false) goto L34;
        MediaSource.MediaPeriodId r3 = new MediaSource.MediaPeriodId(r25.mediaPeriodId.periodUid, r25.mediaPeriodId.windowSequenceNumber, r25.mediaPeriodId.adGroupIndex);     // Catch: Throwable -> L43
        SessionDescriptor r4 = getOrAddSession(r25.windowIndex, r3);     // Catch: Throwable -> L43
        if (SessionDescriptor.access$300(r4) == true) goto L34;
        SessionDescriptor.access$302(r4, true);     // Catch: Throwable -> L43
        r25.timeline.getPeriodByUid(r25.mediaPeriodId.periodUid, this.period);     // Catch: Throwable -> L43
        long r15 = Math.max(0, Util.usToMs(this.period.getAdGroupTimeUs(r25.mediaPeriodId.adGroupIndex)) + this.period.getPositionInWindowMs());     // Catch: Throwable -> L43
        AnalyticsListener.EventTime r9 = new AnalyticsListener.EventTime(r25.realtimeMs, r25.timeline, r25.windowIndex, r3, r15, r25.currentTimeline, r25.currentWindowIndex, r25.currentMediaPeriodId, r25.currentPlaybackPositionMs, r25.totalBufferedDurationMs);     // Catch: Throwable -> L43
        this.listener.onSessionCreated(r9, SessionDescriptor.access$000(r4));     // Catch: Throwable -> L43
    L34:
        if (SessionDescriptor.access$300(r2) == true) goto L37;
        SessionDescriptor.access$302(r2, true);     // Catch: Throwable -> L43
        this.listener.onSessionCreated(r25, SessionDescriptor.access$000(r2));     // Catch: Throwable -> L43
    L37:
        if (SessionDescriptor.access$000(r2).equals(this.currentSessionId) == true) goto L39;
    L41:
        monitor-exit(this);
        return;
    L39:
        if (SessionDescriptor.access$400(r2) == true) goto L41;
        SessionDescriptor.access$402(r2, true);     // Catch: Throwable -> L43
        this.listener.onSessionActive(r25, SessionDescriptor.access$000(r2));     // Catch: Throwable -> L43
        goto L41
    L12:
        if (r25.mediaPeriodId.windowSequenceNumber >= getMinWindowSequenceNumber()) goto L15;
        monitor-exit(this);
        return;
    L15:
        SessionDescriptor r5 = this.sessions.get(this.currentSessionId);     // Catch: Throwable -> L43
        if (r5 == null) goto L23;
        if (SessionDescriptor.access$100(r5) != (-1)) goto L23;
        if (SessionDescriptor.access$200(r5) == r25.windowIndex) goto L23;
        monitor-exit(this);
        return;
    L43:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager
    public synchronized void updateSessionsWithDiscontinuity(AnalyticsListener.EventTime r10, int r11) {
        monitor-enter(this);
        Assertions.checkNotNull(this.listener);     // Catch: Throwable -> L28
        if (r11 != 0) goto L6;
        boolean r2 = true;
    L7:
        Iterator<SessionDescriptor> r3 = this.sessions.values().iterator();     // Catch: Throwable -> L28
    L9:
        if (r3.hasNext() == false) goto L25;
        SessionDescriptor r4 = r3.next();     // Catch: Throwable -> L28
        if (r4.isFinishedAtEventTime(r10) == false) goto L9;
        r3.remove();     // Catch: Throwable -> L28
        if (SessionDescriptor.access$300(r4) == false) goto L9;
        boolean r5 = SessionDescriptor.access$000(r4).equals(this.currentSessionId);     // Catch: Throwable -> L28
        if (r2 == false) goto L20;
        if (r5 == false) goto L20;
        if (SessionDescriptor.access$400(r4) == false) goto L20;
        boolean r6 = true;
    L21:
        if (r5 == false) goto L23;
        clearCurrentSession(r4);     // Catch: Throwable -> L28
    L23:
        this.listener.onSessionFinished(r10, SessionDescriptor.access$000(r4), r6);     // Catch: Throwable -> L28
    L20:
        r6 = false;
        goto L21
    L25:
        updateCurrentSession(r10);     // Catch: Throwable -> L28
        monitor-exit(this);
        return;
    L6:
        r2 = false;
    L28:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.analytics.PlaybackSessionManager
    public synchronized void updateSessionsWithTimelineChange(AnalyticsListener.EventTime r7) {
        monitor-enter(this);
        Assertions.checkNotNull(this.listener);     // Catch: Throwable -> L20
        Timeline r0 = this.currentTimeline;     // Catch: Throwable -> L20
        this.currentTimeline = r7.timeline;     // Catch: Throwable -> L20
        Iterator<SessionDescriptor> r1 = this.sessions.values().iterator();     // Catch: Throwable -> L20
    L5:
        if (r1.hasNext() == false) goto L17;
        SessionDescriptor r2 = r1.next();     // Catch: Throwable -> L20
        if (r2.tryResolvingToNewTimeline(r0, this.currentTimeline) == true) goto L9;
    L10:
        r1.remove();     // Catch: Throwable -> L20
        if (SessionDescriptor.access$300(r2) == false) goto L5;
        if (SessionDescriptor.access$000(r2).equals(this.currentSessionId) == false) goto L15;
        clearCurrentSession(r2);     // Catch: Throwable -> L20
    L15:
        this.listener.onSessionFinished(r7, SessionDescriptor.access$000(r2), false);     // Catch: Throwable -> L20
        goto L5
    L9:
        if (r2.isFinishedAtEventTime(r7) == false) goto L5;
    L17:
        updateCurrentSession(r7);     // Catch: Throwable -> L20
        monitor-exit(this);
        return;
    L20:
        th = move-exception;
        throw th;
    }
}
