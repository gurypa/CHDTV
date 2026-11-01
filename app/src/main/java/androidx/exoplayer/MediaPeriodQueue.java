package androidx.media3.exoplayer;

import android.util.Pair;
import androidx.media3.common.C0180C;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.HandlerWrapper;
import androidx.media3.exoplayer.analytics.AnalyticsCollector;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSource;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class MediaPeriodQueue {
    public static final long INITIAL_RENDERER_POSITION_OFFSET_US = 1000000000000L;
    private static final int MAXIMUM_BUFFER_AHEAD_PERIODS = 100;
    private final AnalyticsCollector analyticsCollector;
    private final HandlerWrapper analyticsCollectorHandler;
    private int length;
    private MediaPeriodHolder loading;
    private final MediaPeriodHolder.Factory mediaPeriodHolderFactory;
    private long nextWindowSequenceNumber;
    private Object oldFrontPeriodUid;
    private long oldFrontPeriodWindowSequenceNumber;
    private final Timeline.Period period;
    private MediaPeriodHolder playing;
    private ExoPlayer.PreloadConfiguration preloadConfiguration;
    private List<MediaPeriodHolder> preloadPriorityList;
    private MediaPeriodHolder preloading;
    private MediaPeriodHolder reading;
    private int repeatMode;
    private boolean shuffleModeEnabled;
    private final Timeline.Window window;

    public MediaPeriodQueue(AnalyticsCollector r2, HandlerWrapper r3, MediaPeriodHolder.Factory r4, ExoPlayer.PreloadConfiguration r5) {
        this.analyticsCollector = r2;
        this.analyticsCollectorHandler = r3;
        this.mediaPeriodHolderFactory = r4;
        this.preloadConfiguration = r5;
        this.period = new Timeline.Period();
        this.window = new Timeline.Window();
        this.preloadPriorityList = new ArrayList();
    }

    static boolean areDurationsCompatible(long r2, long r4) {
        if (r2 != C0180C.TIME_UNSET) goto L5;
    L8:
        return true;
    L5:
        if (r2 == r4) goto L8;
        return false;
    }

    private boolean canKeepMediaPeriodHolder(MediaPeriodInfo r5, MediaPeriodInfo r6) {
        if (r5.startPositionUs == r6.startPositionUs) goto L5;
    L7:
        return false;
    L5:
        if (r5.f35id.equals(r6.f35id) == false) goto L7;
        return true;
    }

    private Pair<Object, Long> getDefaultPeriodPositionOfNextWindow(Timeline r12, Object r13, long r14) {
        int r6 = r12.getNextWindowIndex(r12.getPeriodByUid(r13, this.period).windowIndex, this.repeatMode, this.shuffleModeEnabled);
        if (r6 == (-1)) goto L6;
        return r12.getPeriodPositionUs(this.window, this.period, r6, C0180C.TIME_UNSET, r14);
    L6:
        return null;
    }

    private MediaPeriodInfo getFirstMediaPeriodInfo(PlaybackInfo r8) {
        return getMediaPeriodInfo(r8.timeline, r8.periodId, r8.requestedContentPositionUs, r8.positionUs);
    }

    private MediaPeriodInfo getFirstMediaPeriodInfoOfNextPeriod(Timeline r29, MediaPeriodHolder r30, long r31) {
        MediaPeriodInfo r10 = r30.info;
        int r11 = r29.getNextPeriodIndex(r29.getIndexOfPeriod(r10.f35id.periodUid), this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
        if (r11 != (-1)) goto L5;
        return null;
    L5:
        int r4 = r29.getPeriod(r11, this.period, true).windowIndex;
        Object r16 = Assertions.checkNotNull(this.period.uid);
        long r5 = r10.f35id.windowSequenceNumber;
        if (r29.getWindow(r4, this.window).firstPeriodIndex != r11) goto L20;
        long r17 = C0180C.TIME_UNSET;
        Pair<Object, Long> r2 = r29.getPeriodPositionUs(this.window, this.period, r4, C0180C.TIME_UNSET, Math.max(0, r31));
        if (r2 != null) goto L10;
        return null;
    L10:
        Object r1 = r2.first;
        long r3 = ((Long) r2.second).longValue();
        MediaPeriodHolder r6 = r30.getNext();
        if (r6 != null) goto L13;
    L15:
        long r7 = resolvePeriodUidToWindowSequenceNumberInPreloadPeriods(r1);
        if (r7 != (-1)) goto L18;
        long r8 = this.nextWindowSequenceNumber;
        this.nextWindowSequenceNumber = r8 + 1;
        long r9 = r8;
    L19:
        r5 = r9;
        Object r12 = r1;
    L21:
        MediaSource.MediaPeriodId r13 = resolveMediaPeriodIdForAds(r29, r12, r3, r5, this.window, this.period);
        if (r17 != C0180C.TIME_UNSET) goto L24;
    L31:
        long r14 = r3;
        long r15 = r17;
    L33:
        return getMediaPeriodInfo(r29, r13, r15, r14);
    L24:
        if (r10.requestedContentPositionUs == C0180C.TIME_UNSET) goto L31;
        boolean r18 = hasServerSideInsertedAds(r10.f35id.periodUid, r29);
        if (r13.isAd() == false) goto L29;
        if (r18 == false) goto L29;
        r14 = r3;
        r15 = r10.requestedContentPositionUs;
    L29:
        if (r18 == false) goto L31;
        r14 = r10.requestedContentPositionUs;
        r15 = r17;
        goto L33
    L18:
        r9 = r7;
        goto L19
    L13:
        if (r6.uid.equals(r1) == false) goto L15;
        r12 = r1;
        r5 = r6.info.f35id.windowSequenceNumber;
        goto L21
    L20:
        r17 = 0;
        r12 = r16;
        r3 = 0;
        goto L21
    }

    private MediaPeriodInfo getFollowingMediaPeriodInfo(Timeline r6, MediaPeriodHolder r7, long r8) {
        MediaPeriodInfo r0 = r7.info;
        long r1 = (r7.getRendererOffset() + r0.durationUs) - r8;
        if (r0.isLastInTimelinePeriod == false) goto L6;
        return getFirstMediaPeriodInfoOfNextPeriod(r6, r7, r1);
    L6:
        return getFollowingMediaPeriodInfoOfCurrentPeriod(r6, r7, r1);
    }

    private MediaPeriodInfo getFollowingMediaPeriodInfoOfCurrentPeriod(Timeline r23, MediaPeriodHolder r24, long r25) {
        MediaPeriodInfo r10 = r24.info;
        MediaSource.MediaPeriodId r11 = r10.f35id;
        r23.getPeriodByUid(r11.periodUid, this.period);
        if (r11.isAd() == false) goto L21;
        int r2 = r11.adGroupIndex;
        int r12 = this.period.getAdCountInAdGroup(r2);
        if (r12 != (-1)) goto L7;
        return null;
    L7:
        int r4 = this.period.getNextAdIndexToPlay(r2, r11.adIndexInAdGroup);
        if (r4 < r12) goto L10;
        long r0 = r10.requestedContentPositionUs;
        if (r0 != C0180C.TIME_UNSET) goto L17;
        Timeline r1 = r23;
        Pair<Object, Long> r3 = r1.getPeriodPositionUs(this.window, this.period, this.period.windowIndex, C0180C.TIME_UNSET, Math.max(0, r25));
        if (r3 != null) goto L16;
        return null;
    L16:
        long r5 = ((Long) r3.second).longValue();
    L18:
        long r6 = getMinStartPositionAfterAdGroupUs(r1, r11.periodUid, r11.adGroupIndex);
        Timeline r7 = r1;
        return getMediaPeriodInfoForContent(r7, r11.periodUid, Math.max(r6, r5), r10.requestedContentPositionUs, r11.windowSequenceNumber);
    L17:
        r1 = r23;
        r5 = r0;
        goto L18
    L10:
        return getMediaPeriodInfoForAd(r23, r11.periodUid, r2, r4, r10.requestedContentPositionUs, r11.windowSequenceNumber);
    L21:
        if (r11.nextAdGroupIndex != (-1)) goto L23;
    L26:
        int r8 = this.period.getFirstAdIndexToPlay(r11.nextAdGroupIndex);
        if (this.period.isServerSideInsertedAdGroup(r11.nextAdGroupIndex) == true) goto L29;
    L31:
        boolean r9 = false;
    L32:
        boolean r13 = r9;
        if (r8 == this.period.getAdCountInAdGroup(r11.nextAdGroupIndex)) goto L39;
        if (r13 == true) goto L39;
        return getMediaPeriodInfoForAd(r23, r11.periodUid, r11.nextAdGroupIndex, r8, r10.durationUs, r11.windowSequenceNumber);
    L39:
        long r14 = getMinStartPositionAfterAdGroupUs(r23, r11.periodUid, r11.nextAdGroupIndex);
        return getMediaPeriodInfoForContent(r23, r11.periodUid, r14, r10.durationUs, r11.windowSequenceNumber);
    L29:
        if (this.period.getAdState(r11.nextAdGroupIndex, r8) != 3) goto L31;
        r9 = true;
        goto L32
    L23:
        if (this.period.isLivePostrollPlaceholder(r11.nextAdGroupIndex) == false) goto L26;
        return getFirstMediaPeriodInfoOfNextPeriod(r23, r24, r25);
    }

    private MediaPeriodInfo getMediaPeriodInfo(Timeline r11, MediaSource.MediaPeriodId r12, long r13, long r15) {
        r11.getPeriodByUid(r12.periodUid, this.period);
        if (r12.isAd() == false) goto L7;
        return getMediaPeriodInfoForAd(r11, r12.periodUid, r12.adGroupIndex, r12.adIndexInAdGroup, r13, r12.windowSequenceNumber);
    L7:
        return getMediaPeriodInfoForContent(r11, r12.periodUid, r15, r13, r12.windowSequenceNumber);
    }

    private MediaPeriodInfo getMediaPeriodInfoForAd(Timeline r17, Object r18, int r19, int r20, long r21, long r23) {
        MediaSource.MediaPeriodId r1 = new MediaSource.MediaPeriodId(r18, r19, r20, r23);
        long r10 = r17.getPeriodByUid(r1.periodUid, this.period).getAdDurationUs(r1.adGroupIndex, r1.adIndexInAdGroup);
        if (r20 != this.period.getFirstAdIndexToPlay(r19)) goto L5;
        long r8 = this.period.getAdResumePositionUs();
    L6:
        boolean r12 = this.period.isServerSideInsertedAdGroup(r1.adGroupIndex);
        if (r10 == C0180C.TIME_UNSET) goto L12;
        if (r8 < r10) goto L12;
        r8 = Math.max(0, r10 - 1);
    L12:
        return new MediaPeriodInfo(r1, r8, r21, C0180C.TIME_UNSET, r10, r12, false, false, false);
    L5:
        r8 = 0;
        goto L6
    }

    private MediaPeriodInfo getMediaPeriodInfoForContent(Timeline r29, Object r30, long r31, long r33, long r35) {
        r29.getPeriodByUid(r30, this.period);
        int r5 = this.period.getAdGroupIndexAfterPositionUs(r31);
        if (r5 != (-1)) goto L5;
    L7:
        boolean r9 = false;
    L8:
        boolean r10 = false;
        if (r5 != (-1)) goto L18;
        if (this.period.getAdGroupCount() > 0) goto L13;
    L15:
        boolean r11 = false;
    L16:
        r10 = r11;
    L24:
        MediaSource.MediaPeriodId r12 = new MediaSource.MediaPeriodId(r30, r35, r5);
        boolean r13 = isLastInPeriod(r12);
        boolean r26 = isLastInWindow(r29, r12);
        boolean r27 = isLastInTimeline(r29, r12, r13);
        if (r5 != (-1)) goto L27;
    L30:
        boolean r24 = false;
    L32:
        if (r5 == (-1)) goto L35;
        if (r9 == true) goto L35;
        long r20 = this.period.getAdGroupTimeUs(r5);
    L39:
        if (r20 != C0180C.TIME_UNSET) goto L41;
    L44:
        long r22 = this.period.durationUs;
    L46:
        if (r22 != C0180C.TIME_UNSET) goto L48;
    L58:
        long r16 = r31;
    L60:
        return new MediaPeriodInfo(r12, r16, r33, r20, r22, r24, r13, r26, r27);
    L48:
        if (r31 < r22) goto L58;
        if (r27 == true) goto L53;
        if (r10 == false) goto L53;
        boolean r6 = false;
    L54:
        if (r6 == false) goto L56;
        int r14 = 1;
    L57:
        r16 = Math.max(0, r22 - ((long) r14));
        goto L60
    L56:
        r14 = 0;
    L53:
        r6 = true;
        goto L54
    L41:
        if (r20 == Long.MIN_VALUE) goto L44;
        r22 = r20;
    L35:
        if (r10 == false) goto L37;
        r20 = this.period.durationUs;
        goto L39
    L37:
        r20 = -9223372036854775807L;
        goto L39
    L27:
        if (this.period.isServerSideInsertedAdGroup(r5) == false) goto L30;
        if (r9 == true) goto L30;
        r24 = true;
        goto L32
    L13:
        if (this.period.isServerSideInsertedAdGroup(this.period.getRemovedAdGroupCount()) == false) goto L15;
        r11 = true;
        goto L16
    L18:
        if (this.period.isServerSideInsertedAdGroup(r5) == false) goto L24;
        if (this.period.getAdGroupTimeUs(r5) != this.period.durationUs) goto L24;
        if (this.period.hasPlayedAdGroup(r5) == false) goto L24;
        r5 = -1;
        r10 = true;
        goto L24
    L5:
        if (this.period.isLivePostrollPlaceholder(r5) == false) goto L7;
        r9 = true;
        goto L8
    }

    private MediaPeriodInfo getMediaPeriodInfoForPeriodPosition(Timeline r10, Object r11, long r12, long r14) {
        MediaSource.MediaPeriodId r13 = resolveMediaPeriodIdForAds(r10, r11, r12, r14, this.window, this.period);
        if (r13.isAd() == false) goto L6;
        return getMediaPeriodInfoForAd(r10, r13.periodUid, r13.adGroupIndex, r13.adIndexInAdGroup, r12, r13.windowSequenceNumber);
    L6:
        return getMediaPeriodInfoForContent(r10, r13.periodUid, r12, C0180C.TIME_UNSET, r13.windowSequenceNumber);
    }

    private long getMinStartPositionAfterAdGroupUs(Timeline r5, Object r6, int r7) {
        r5.getPeriodByUid(r6, this.period);
        long r0 = this.period.getAdGroupTimeUs(r7);
        if (r0 != Long.MIN_VALUE) goto L7;
        return this.period.durationUs;
    L7:
        return this.period.getContentResumeOffsetUs(r7) + r0;
    }

    private boolean hasServerSideInsertedAds(Object r8, Timeline r9) {
        int r0 = r9.getPeriodByUid(r8, this.period).getAdGroupCount();
        int r1 = this.period.getRemovedAdGroupCount();
        if (r0 > 0) goto L5;
    L11:
        return false;
    L5:
        if (this.period.isServerSideInsertedAdGroup(r1) == false) goto L11;
        if (r0 <= 1) goto L9;
        return true;
    L9:
        if (this.period.getAdGroupTimeUs(r1) == Long.MIN_VALUE) goto L11;
        return true;
    }

    private boolean isLastInPeriod(MediaSource.MediaPeriodId r3) {
        if (r3.isAd() == false) goto L5;
    L7:
        return false;
    L5:
        if (r3.nextAdGroupIndex != (-1)) goto L7;
        return true;
    }

    private boolean isLastInTimeline(Timeline r8, MediaSource.MediaPeriodId r9, boolean r10) {
        int r2 = r8.getIndexOfPeriod(r9.periodUid);
        if (r8.getWindow(r8.getPeriod(r2, this.period).windowIndex, this.window).isDynamic == true) goto L9;
        if (r8.isLastPeriod(r2, this.period, this.window, this.repeatMode, this.shuffleModeEnabled) == false) goto L9;
        if (r10 == false) goto L9;
        return true;
    L9:
        return false;
    }

    private boolean isLastInWindow(Timeline r5, MediaSource.MediaPeriodId r6) {
        if (isLastInPeriod(r6) == true) goto L5;
        return false;
    L5:
        int r0 = r5.getPeriodByUid(r6.periodUid, this.period).windowIndex;
        int r2 = r5.getIndexOfPeriod(r6.periodUid);
        if (r5.getWindow(r0, this.window).lastPeriodIndex != r2) goto L9;
        return true;
    L9:
        return false;
    }

    private static boolean isSkippableAdPeriod(Timeline.Period r9) {
        int r0 = r9.getAdGroupCount();
        if (r0 != 0) goto L5;
    L27:
        return false;
    L5:
        if (r0 != 1) goto L9;
        if (r9.isLivePostrollPlaceholder(0) == true) goto L27;
    L9:
        if (r9.isServerSideInsertedAdGroup(r9.getRemovedAdGroupCount()) == false) goto L27;
        if (r9.getAdGroupIndexForPositionUs(0) != (-1)) goto L27;
        if (r9.durationUs != 0) goto L16;
        return true;
    L16:
        long r3 = 0;
        if (r9.isLivePostrollPlaceholder(r0 - 1) == false) goto L19;
        int r5 = 2;
    L20:
        int r6 = r0 - r5;
        int r7 = 0;
    L21:
        if (r7 > r6) goto L24;
        r3 = r3 + r9.getContentResumeOffsetUs(r7);
        r7 = r7 + 1;
        goto L21
    L24:
        if (r9.durationUs <= r3) goto L26;
        return false;
    L26:
        return true;
    L19:
        r5 = 1;
        goto L20
    }

    private void notifyQueueUpdate() {
        final ImmutableList.Builder r0 = ImmutableList.builder();
        MediaPeriodHolder r1 = this.playing;
    L3:
        if (r1 == null) goto L6;
        r0.add(r1.info.f35id);
        r1 = r1.getNext();
        goto L3
    L6:
        if (this.reading != null) goto L8;
        final MediaSource.MediaPeriodId r2 = null;
    L9:
        this.analyticsCollectorHandler.post(new MediaPeriodQueue$$ExternalSyntheticLambda0(this, r0, r2));
        return;
    L8:
        r2 = this.reading.info.f35id;
        goto L9
    }

    private void releaseAndResetPreloadPriorityList(List<MediaPeriodHolder> r3) {
        int r0 = 0;
    L4:
        if (r0 >= this.preloadPriorityList.size()) goto L6;
        this.preloadPriorityList.get(r0).release();
        r0 = r0 + 1;
        goto L4
    L6:
        this.preloadPriorityList = r3;
        this.preloading = null;
        maybeUpdatePreloadMediaPeriodHolder();
    }

    private MediaPeriodHolder removePreloadedMediaPeriodHolder(MediaPeriodInfo r4) {
        int r0 = 0;
    L4:
        if (r0 >= this.preloadPriorityList.size()) goto L10;
        if (this.preloadPriorityList.get(r0).canBeUsedForMediaPeriodInfo(r4) == true) goto L8;
        r0 = r0 + 1;
        goto L4
    L8:
        return this.preloadPriorityList.remove(r0);
    L10:
        return null;
    }

    private static MediaSource.MediaPeriodId resolveMediaPeriodIdForAds(Timeline r7, Object r8, long r9, long r11, Timeline.Window r13, Timeline.Period r14) {
        r7.getPeriodByUid(r8, r14);
        r7.getWindow(r14.windowIndex, r13);
        int r0 = r7.getIndexOfPeriod(r8);
        Object r2 = r8;
    L4:
        if (isSkippableAdPeriod(r14) == false) goto L8;
        if (r0 > r13.lastPeriodIndex) goto L8;
        r7.getPeriod(r0, r14, true);
        r2 = Assertions.checkNotNull(r14.uid);
        r0 = r0 + 1;
    L8:
        r7.getPeriodByUid(r2, r14);
        int r3 = r14.getAdGroupIndexForPositionUs(r9);
        if (r3 != (-1)) goto L13;
        return new MediaSource.MediaPeriodId(r2, r11, r14.getAdGroupIndexAfterPositionUs(r9));
    L13:
        return new MediaSource.MediaPeriodId(r2, r3, r14.getFirstAdIndexToPlay(r3), r11);
    }

    private long resolvePeriodUidToWindowSequenceNumber(Timeline r9, Object r10) {
        int r0 = r9.getPeriodByUid(r10, this.period).windowIndex;
        if (this.oldFrontPeriodUid == null) goto L10;
        int r1 = r9.getIndexOfPeriod(this.oldFrontPeriodUid);
        if (r1 == (-1)) goto L10;
        if (r9.getPeriod(r1, this.period).windowIndex != r0) goto L10;
        return this.oldFrontPeriodWindowSequenceNumber;
    L10:
        MediaPeriodHolder r2 = this.playing;
    L11:
        if (r2 == null) goto L17;
        if (r2.uid.equals(r10) == true) goto L15;
        r2 = r2.getNext();
        goto L11
    L15:
        return r2.info.f35id.windowSequenceNumber;
    L17:
        MediaPeriodHolder r3 = this.playing;
    L18:
        if (r3 == null) goto L26;
        int r4 = r9.getIndexOfPeriod(r3.uid);
        if (r4 == (-1)) goto L25;
        if (r9.getPeriod(r4, this.period).windowIndex != r0) goto L25;
        return r3.info.f35id.windowSequenceNumber;
    L25:
        r3 = r3.getNext();
        goto L18
    L26:
        long r5 = resolvePeriodUidToWindowSequenceNumberInPreloadPeriods(r10);
        if (r5 == (-1)) goto L29;
        return r5;
    L29:
        long r6 = this.nextWindowSequenceNumber;
        this.nextWindowSequenceNumber = 1 + r6;
        if (this.playing != null) goto L32;
        this.oldFrontPeriodUid = r10;
        this.oldFrontPeriodWindowSequenceNumber = r6;
    L32:
        return r6;
    }

    private long resolvePeriodUidToWindowSequenceNumberInPreloadPeriods(Object r5) {
        int r0 = 0;
    L4:
        if (r0 >= this.preloadPriorityList.size()) goto L10;
        MediaPeriodHolder r1 = this.preloadPriorityList.get(r0);
        if (r1.uid.equals(r5) == true) goto L8;
        r0 = r0 + 1;
        goto L4
    L8:
        return r1.info.f35id.windowSequenceNumber;
    L10:
        return -1;
    }

    private boolean updateForPlaybackModeChange(Timeline r8) {
        MediaPeriodHolder r0 = this.playing;
        if (r0 != null) goto L6;
        return true;
    L6:
        int r2 = r8.getIndexOfPeriod(r0.uid);
    L7:
        Timeline r1 = r8;
        int r9 = r1.getNextPeriodIndex(r2, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
    L9:
        if (((MediaPeriodHolder) Assertions.checkNotNull(r0)).getNext() == null) goto L13;
        if (r0.info.isLastInTimelinePeriod == true) goto L13;
        r0 = r0.getNext();
    L13:
        MediaPeriodHolder r3 = r0.getNext();
        if (r9 == (-1)) goto L21;
        if (r3 == null) goto L21;
        if (r1.getIndexOfPeriod(r3.uid) != r9) goto L21;
        r0 = r3;
        r2 = r9;
        r8 = r1;
    L21:
        boolean r10 = removeAfter(r0);
        r0.info = getUpdatedMediaPeriodInfo(r1, r0.info);
        return !r10;
    }

    public MediaPeriodHolder advancePlayingPeriod() {
        if (this.playing != null) goto L6;
        return null;
    L6:
        if (this.playing != this.reading) goto L8;
        this.reading = this.playing.getNext();
    L8:
        this.playing.release();
        this.length--;
        if (this.length != 0) goto L11;
        this.loading = null;
        this.oldFrontPeriodUid = this.playing.uid;
        this.oldFrontPeriodWindowSequenceNumber = this.playing.info.f35id.windowSequenceNumber;
    L11:
        this.playing = this.playing.getNext();
        notifyQueueUpdate();
        return this.playing;
    }

    public MediaPeriodHolder advanceReadingPeriod() {
        this.reading = ((MediaPeriodHolder) Assertions.checkStateNotNull(this.reading)).getNext();
        notifyQueueUpdate();
        return (MediaPeriodHolder) Assertions.checkStateNotNull(this.reading);
    }

    public void clear() {
        if (this.length != 0) goto L5;
        return;
    L5:
        MediaPeriodHolder r0 = (MediaPeriodHolder) Assertions.checkStateNotNull(this.playing);
        this.oldFrontPeriodUid = r0.uid;
        this.oldFrontPeriodWindowSequenceNumber = r0.info.f35id.windowSequenceNumber;
    L6:
        if (r0 == null) goto L8;
        r0.release();
        r0 = r0.getNext();
        goto L6
    L8:
        this.playing = null;
        this.loading = null;
        this.reading = null;
        this.length = 0;
        notifyQueueUpdate();
    }

    public MediaPeriodHolder enqueueNextMediaPeriodHolder(MediaPeriodInfo r5) {
        if (this.loading != null) goto L5;
        long r0 = INITIAL_RENDERER_POSITION_OFFSET_US;
    L6:
        MediaPeriodHolder r2 = removePreloadedMediaPeriodHolder(r5);
        if (r2 != null) goto L9;
        r2 = this.mediaPeriodHolderFactory.create(r5, r0);
    L11:
        if (this.loading == null) goto L13;
        this.loading.setNext(r2);
    L14:
        this.oldFrontPeriodUid = null;
        this.loading = r2;
        this.length++;
        notifyQueueUpdate();
        return r2;
    L13:
        this.playing = r2;
        this.reading = r2;
        goto L14
    L9:
        r2.info = r5;
        r2.setRendererOffset(r0);
        goto L11
    L5:
        r0 = (this.loading.getRendererOffset() + this.loading.info.durationUs) - r5.startPositionUs;
        goto L6
    }

    public MediaPeriodHolder getLoadingPeriod() {
        return this.loading;
    }

    public MediaPeriodInfo getNextMediaPeriodInfo(long r3, PlaybackInfo r5) {
        if (this.loading != null) goto L6;
        return getFirstMediaPeriodInfo(r5);
    L6:
        return getFollowingMediaPeriodInfo(r5.timeline, this.loading, r3);
    }

    public MediaPeriodHolder getPlayingPeriod() {
        return this.playing;
    }

    public MediaPeriodHolder getPreloadHolderByMediaPeriod(MediaPeriod r4) {
        int r0 = 0;
    L4:
        if (r0 >= this.preloadPriorityList.size()) goto L9;
        MediaPeriodHolder r1 = this.preloadPriorityList.get(r0);
        if (r1.mediaPeriod == r4) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r1;
    L9:
        return null;
    }

    public MediaPeriodHolder getPreloadingPeriod() {
        return this.preloading;
    }

    public MediaPeriodHolder getReadingPeriod() {
        return this.reading;
    }

    public MediaPeriodInfo getUpdatedMediaPeriodInfo(Timeline r18, MediaPeriodInfo r19) {
        MediaSource.MediaPeriodId r4 = r19.f35id;
        boolean r14 = isLastInPeriod(r4);
        boolean r15 = isLastInWindow(r18, r4);
        boolean r16 = isLastInTimeline(r18, r4, r14);
        r18.getPeriodByUid(r19.f35id.periodUid, this.period);
        if (r4.isAd() == false) goto L5;
    L8:
        long r9 = -9223372036854775807L;
    L10:
        if (r4.isAd() == false) goto L13;
        long r11 = this.period.getAdDurationUs(r4.adGroupIndex, r4.adIndexInAdGroup);
    L20:
        if (r4.isAd() == false) goto L23;
        boolean r13 = this.period.isServerSideInsertedAdGroup(r4.adGroupIndex);
    L30:
        return new MediaPeriodInfo(r4, r19.startPositionUs, r19.requestedContentPositionUs, r9, r11, r13, r14, r15, r16);
    L23:
        if (r4.nextAdGroupIndex != (-1)) goto L25;
    L27:
        boolean r3 = false;
    L28:
        r13 = r3;
        goto L30
    L25:
        if (this.period.isServerSideInsertedAdGroup(r4.nextAdGroupIndex) == false) goto L27;
        r3 = true;
        goto L28
    L13:
        if (r9 != C0180C.TIME_UNSET) goto L15;
    L18:
        r11 = this.period.getDurationUs();
        goto L20
    L15:
        if (r9 == Long.MIN_VALUE) goto L18;
        r11 = r9;
        goto L20
    L5:
        if (r4.nextAdGroupIndex == (-1)) goto L8;
        r9 = this.period.getAdGroupTimeUs(r4.nextAdGroupIndex);
        goto L10
    }

    public void invalidatePreloadPool(Timeline r15) {
        if (this.preloadConfiguration.targetPreloadDurationUs == C0180C.TIME_UNSET) goto L24;
        if (this.loading == null) goto L24;
        MediaPeriodHolder r0 = this.loading;
        ArrayList r1 = new ArrayList();
        Pair<Object, Long> r2 = getDefaultPeriodPositionOfNextWindow(r15, r0.info.f35id.periodUid, 0);
        if (r2 == null) goto L21;
        if (r15.getWindow(r15.getPeriodByUid(r2.first, this.period).windowIndex, this.window).isLive() == true) goto L21;
        long r3 = resolvePeriodUidToWindowSequenceNumberInPreloadPeriods(r2.first);
        if (r3 != (-1)) goto L14;
        long r5 = this.nextWindowSequenceNumber;
        this.nextWindowSequenceNumber = 1 + r5;
        long r10 = r5;
    L15:
        MediaPeriodInfo r16 = getMediaPeriodInfoForPeriodPosition(r15, r2.first, ((Long) r2.second).longValue(), r10);
        MediaPeriodHolder r4 = removePreloadedMediaPeriodHolder(r16);
        if (r4 != null) goto L18;
        long r7 = (r0.getRendererOffset() + r0.info.durationUs) - r16.startPositionUs;
        r4 = this.mediaPeriodHolderFactory.create(r16, r7);
    L18:
        r1.add(r4);
        goto L21
    L14:
        r10 = r3;
    L21:
        releaseAndResetPreloadPriorityList(r1);
        return;
    L24:
        releasePreloadPool();
    }

    public boolean isLoading(MediaPeriod r2) {
        if (this.loading != null) goto L5;
    L7:
        return false;
    L5:
        if (this.loading.mediaPeriod != r2) goto L7;
        return true;
    }

    public boolean isPreloading(MediaPeriod r2) {
        if (this.preloading != null) goto L5;
    L7:
        return false;
    L5:
        if (this.preloading.mediaPeriod != r2) goto L7;
        return true;
    }

    /* JADX INFO: renamed from: lambda$notifyQueueUpdate$0$androidx-media3-exoplayer-MediaPeriodQueue */
    /* synthetic */ void m64x6b40a91a(ImmutableList.Builder r3, MediaSource.MediaPeriodId r4) {
        this.analyticsCollector.updateMediaPeriodQueueInfo(r3.build(), r4);
    }

    public void maybeUpdatePreloadMediaPeriodHolder() {
        if (this.preloading != null) goto L5;
    L7:
        this.preloading = null;
        int r0 = 0;
    L9:
        if (r0 >= this.preloadPriorityList.size()) goto L17;
        MediaPeriodHolder r1 = this.preloadPriorityList.get(r0);
        if (r1.isFullyPreloaded() == false) goto L12;
        r0 = r0 + 1;
        goto L9
    L12:
        this.preloading = r1;
        return;
    L17:
        return;
    L5:
        if (this.preloading.isFullyPreloaded() == true) goto L7;
    }

    public void reevaluateBuffer(long r2) {
        if (this.loading == null) goto L6;
        this.loading.reevaluateBuffer(r2);
        return;
    }

    public void releasePreloadPool() {
        if (this.preloadPriorityList.isEmpty() == true) goto L6;
        releaseAndResetPreloadPriorityList(new ArrayList());
        return;
    }

    public boolean removeAfter(MediaPeriodHolder r4) {
        Assertions.checkStateNotNull(r4);
        if (r4.equals(this.loading) == false) goto L6;
        return false;
    L6:
        boolean r0 = false;
        this.loading = r4;
    L8:
        if (r4.getNext() == null) goto L13;
        r4 = (MediaPeriodHolder) Assertions.checkNotNull(r4.getNext());
        if (r4 != this.reading) goto L12;
        this.reading = this.playing;
        r0 = true;
    L12:
        r4.release();
        this.length--;
        goto L8
    L13:
        ((MediaPeriodHolder) Assertions.checkNotNull(this.loading)).setNext(null);
        notifyQueueUpdate();
        return r0;
    }

    public MediaSource.MediaPeriodId resolveMediaPeriodIdForAds(Timeline r9, Object r10, long r11) {
        return resolveMediaPeriodIdForAds(r9, r10, r11, resolvePeriodUidToWindowSequenceNumber(r9, r10), this.window, this.period);
    }

    public MediaSource.MediaPeriodId resolveMediaPeriodIdForAdsAfterPeriodPositionChange(Timeline r12, Object r13, long r14) {
        long r4 = resolvePeriodUidToWindowSequenceNumber(r12, r13);
        r12.getPeriodByUid(r13, this.period);
        r12.getWindow(this.period.windowIndex, this.window);
        Object r0 = r13;
        boolean r1 = false;
        int r2 = r12.getIndexOfPeriod(r13);
    L4:
        if (r2 < this.window.firstPeriodIndex) goto L18;
        boolean r6 = true;
        r12.getPeriod(r2, this.period, true);
        if (this.period.getAdGroupCount() > 0) goto L9;
        r6 = false;
    L9:
        r1 = r1 | r6;
        if (this.period.getAdGroupIndexForPositionUs(this.period.durationUs) == (-1)) goto L12;
        r0 = Assertions.checkNotNull(this.period.uid);
    L12:
        if (r1 == false) goto L17;
        if (r6 == false) goto L16;
        if (this.period.durationUs == 0) goto L17;
    L16:
        Object r3 = r0;
    L20:
        return resolveMediaPeriodIdForAds(r12, r3, r14, r4, this.window, this.period);
    L17:
        r2 = r2 - 1;
        goto L4
    L18:
        r3 = r0;
        goto L20
    }

    public boolean shouldLoadNextMediaPeriod() {
        if (this.loading != null) goto L5;
    L14:
        return true;
    L5:
        if (this.loading.info.isFinal == false) goto L7;
    L13:
        return false;
    L7:
        if (this.loading.isFullyBuffered() == false) goto L13;
        if (this.loading.info.durationUs == C0180C.TIME_UNSET) goto L13;
        if (this.length >= 100) goto L13;
        goto L13
    }

    public void updatePreloadConfiguration(Timeline r1, ExoPlayer.PreloadConfiguration r2) {
        this.preloadConfiguration = r2;
        invalidatePreloadPool(r1);
    }

    public boolean updateQueuedPeriods(Timeline r14, long r15, long r17) {
        MediaPeriodHolder r0 = null;
        MediaPeriodHolder r1 = this.playing;
    L4:
        if (r1 == null) goto L40;
        MediaPeriodInfo r3 = r1.info;
        if (r0 != null) goto L8;
        MediaPeriodInfo r6 = getUpdatedMediaPeriodInfo(r14, r3);
    L16:
        r1.info = r6.copyWithRequestedContentPositionUs(r3.requestedContentPositionUs);
        if (areDurationsCompatible(r3.durationUs, r6.durationUs) == false) goto L18;
        r0 = r1;
        r1 = r1.getNext();
        goto L4
    L18:
        r1.updateClipping();
        if (r6.durationUs != C0180C.TIME_UNSET) goto L21;
        long r7 = Long.MAX_VALUE;
    L23:
        if (r1 == this.reading) goto L25;
    L31:
        boolean r9 = false;
    L33:
        if (removeAfter(r1) == true) goto L37;
        if (r9 == true) goto L37;
        return true;
    L37:
        return false;
    L25:
        if (r1.info.isFollowedByTransitionToSameStream == true) goto L31;
        if (r17 != Long.MIN_VALUE) goto L29;
    L30:
        r9 = true;
        goto L33
    L29:
        if (r17 < r7) goto L31;
    L21:
        r7 = r1.toRendererTime(r6.durationUs);
        goto L23
    L8:
        r6 = getFollowingMediaPeriodInfo(r14, r0, r15);
        if (r6 == null) goto L11;
        if (canKeepMediaPeriodHolder(r3, r6) == true) goto L16;
        return true ^ removeAfter(r0);
    L11:
        return true ^ removeAfter(r0);
    L40:
        return true;
    }

    public boolean updateRepeatMode(Timeline r2, int r3) {
        this.repeatMode = r3;
        return updateForPlaybackModeChange(r2);
    }

    public boolean updateShuffleModeEnabled(Timeline r2, boolean r3) {
        this.shuffleModeEnabled = r3;
        return updateForPlaybackModeChange(r2);
    }
}
