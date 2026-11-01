package androidx.media3.common;

import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Pair;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.BundleCollectionUtil;
import androidx.media3.common.util.Util;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public abstract class Timeline {
    public static final Timeline EMPTY = null;
    private static final String FIELD_PERIODS = null;
    private static final String FIELD_SHUFFLED_WINDOW_INDICES = null;
    private static final String FIELD_WINDOWS = null;

    public static final class Period {
        private static final String FIELD_AD_PLAYBACK_STATE = null;
        private static final String FIELD_DURATION_US = null;
        private static final String FIELD_PLACEHOLDER = null;
        private static final String FIELD_POSITION_IN_WINDOW_US = null;
        private static final String FIELD_WINDOW_INDEX = null;
        public AdPlaybackState adPlaybackState;
        public long durationUs;

        /* JADX INFO: renamed from: id */
        public Object f30id;
        public boolean isPlaceholder;
        public long positionInWindowUs;
        public Object uid;
        public int windowIndex;

        static {
            FIELD_WINDOW_INDEX = Util.intToStringMaxRadix(0);
            FIELD_DURATION_US = Util.intToStringMaxRadix(1);
            FIELD_POSITION_IN_WINDOW_US = Util.intToStringMaxRadix(2);
            FIELD_PLACEHOLDER = Util.intToStringMaxRadix(3);
            FIELD_AD_PLAYBACK_STATE = Util.intToStringMaxRadix(4);
        }

        public Period() {
            this.adPlaybackState = AdPlaybackState.NONE;
        }

        public static Period fromBundle(Bundle r12) {
            int r5 = r12.getInt(FIELD_WINDOW_INDEX, 0);
            long r6 = r12.getLong(FIELD_DURATION_US, C0180C.TIME_UNSET);
            long r8 = r12.getLong(FIELD_POSITION_IN_WINDOW_US, 0);
            boolean r11 = r12.getBoolean(FIELD_PLACEHOLDER, false);
            Bundle r0 = r12.getBundle(FIELD_AD_PLAYBACK_STATE);
            if (r0 == null) goto L5;
            AdPlaybackState r10 = AdPlaybackState.fromBundle(r0);
        L6:
            Period r2 = new Period();
            r2.set(null, null, r5, r6, r8, r10, r11);
            return r2;
        L5:
            r10 = AdPlaybackState.NONE;
            goto L6
        }

        public boolean equals(Object r8) {
            if (this != r8) goto L6;
            return true;
        L6:
            if (r8 != null) goto L8;
        L27:
            return false;
        L8:
            if (getClass().equals(r8.getClass()) == false) goto L27;
            Period r2 = (Period) r8;
            if (Util.areEqual(this.f30id, r2.f30id) == false) goto L26;
            if (Util.areEqual(this.uid, r2.uid) == false) goto L26;
            if (this.windowIndex != r2.windowIndex) goto L26;
            if (this.durationUs != r2.durationUs) goto L26;
            if (this.positionInWindowUs != r2.positionInWindowUs) goto L26;
            if (this.isPlaceholder != r2.isPlaceholder) goto L26;
            if (Util.areEqual(this.adPlaybackState, r2.adPlaybackState) == false) goto L26;
            return true;
        L26:
            return false;
        }

        public int getAdCountInAdGroup(int r2) {
            return this.adPlaybackState.getAdGroup(r2).count;
        }

        public long getAdDurationUs(int r5, int r6) {
            AdPlaybackState.AdGroup r0 = this.adPlaybackState.getAdGroup(r5);
            if (r0.count != (-1)) goto L7;
            return C0180C.TIME_UNSET;
        L7:
            return r0.durationsUs[r6];
        }

        public int getAdGroupCount() {
            return this.adPlaybackState.adGroupCount;
        }

        public int getAdGroupIndexAfterPositionUs(long r4) {
            return this.adPlaybackState.getAdGroupIndexAfterPositionUs(r4, this.durationUs);
        }

        public int getAdGroupIndexForPositionUs(long r4) {
            return this.adPlaybackState.getAdGroupIndexForPositionUs(r4, this.durationUs);
        }

        public long getAdGroupTimeUs(int r3) {
            return this.adPlaybackState.getAdGroup(r3).timeUs;
        }

        public long getAdResumePositionUs() {
            return this.adPlaybackState.adResumePositionUs;
        }

        public int getAdState(int r4, int r5) {
            AdPlaybackState.AdGroup r0 = this.adPlaybackState.getAdGroup(r4);
            if (r0.count != (-1)) goto L7;
            return 0;
        L7:
            return r0.states[r5];
        }

        public Object getAdsId() {
            return this.adPlaybackState.adsId;
        }

        public long getContentResumeOffsetUs(int r3) {
            return this.adPlaybackState.getAdGroup(r3).contentResumeOffsetUs;
        }

        public long getDurationMs() {
            return Util.usToMs(this.durationUs);
        }

        public long getDurationUs() {
            return this.durationUs;
        }

        public int getFirstAdIndexToPlay(int r2) {
            return this.adPlaybackState.getAdGroup(r2).getFirstAdIndexToPlay();
        }

        public int getNextAdIndexToPlay(int r2, int r3) {
            return this.adPlaybackState.getAdGroup(r2).getNextAdIndexToPlay(r3);
        }

        public long getPositionInWindowMs() {
            return Util.usToMs(this.positionInWindowUs);
        }

        public long getPositionInWindowUs() {
            return this.positionInWindowUs;
        }

        public int getRemovedAdGroupCount() {
            return this.adPlaybackState.removedAdGroupCount;
        }

        public boolean hasPlayedAdGroup(int r2) {
            return !this.adPlaybackState.getAdGroup(r2).hasUnplayedAds();
        }

        public int hashCode() {
            int r1 = 7 * 31;
            int r3 = 0;
            if (this.f30id != null) goto L5;
            int r2 = 0;
        L6:
            int r0 = (r1 + r2) * 31;
            if (this.uid == null) goto L11;
            r3 = this.uid.hashCode();
        L11:
            return ((((((((((r0 + r3) * 31) + this.windowIndex) * 31) + ((int) (this.durationUs ^ (this.durationUs >>> 32)))) * 31) + ((int) (this.positionInWindowUs ^ (this.positionInWindowUs >>> 32)))) * 31) + (this.isPlaceholder ? 1 : 0)) * 31) + this.adPlaybackState.hashCode();
        L5:
            r2 = this.f30id.hashCode();
            goto L6
        }

        public boolean isLivePostrollPlaceholder(int r3) {
            if (r3 == (getAdGroupCount() - 1)) goto L5;
        L7:
            return false;
        L5:
            if (this.adPlaybackState.isLivePostrollPlaceholder(r3) == false) goto L7;
            return true;
        }

        public boolean isServerSideInsertedAdGroup(int r2) {
            return this.adPlaybackState.getAdGroup(r2).isServerSideInserted;
        }

        public Period set(Object r11, Object r12, int r13, long r14, long r16) {
            return set(r11, r12, r13, r14, r16, AdPlaybackState.NONE, false);
        }

        public Period set(Object r1, Object r2, int r3, long r4, long r6, AdPlaybackState r8, boolean r9) {
            this.f30id = r1;
            this.uid = r2;
            this.windowIndex = r3;
            this.durationUs = r4;
            this.positionInWindowUs = r6;
            this.adPlaybackState = r8;
            this.isPlaceholder = r9;
            return this;
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            if (this.windowIndex == 0) goto L6;
            r0.putInt(FIELD_WINDOW_INDEX, this.windowIndex);
        L6:
            if (this.durationUs == C0180C.TIME_UNSET) goto L9;
            r0.putLong(FIELD_DURATION_US, this.durationUs);
        L9:
            if (this.positionInWindowUs == 0) goto L12;
            r0.putLong(FIELD_POSITION_IN_WINDOW_US, this.positionInWindowUs);
        L12:
            if (this.isPlaceholder == false) goto L15;
            r0.putBoolean(FIELD_PLACEHOLDER, this.isPlaceholder);
        L15:
            if (this.adPlaybackState.equals(AdPlaybackState.NONE) == true) goto L17;
            r0.putBundle(FIELD_AD_PLAYBACK_STATE, this.adPlaybackState.toBundle());
        L17:
            return r0;
        }
    }

    public static final class RemotableTimeline extends Timeline {
        private final ImmutableList<Period> periods;
        private final int[] shuffledWindowIndices;
        private final int[] windowIndicesInShuffled;
        private final ImmutableList<Window> windows;

        public RemotableTimeline(ImmutableList<Window> r4, ImmutableList<Period> r5, int[] r6) {
            if (r4.size() != r6.length) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            this.windows = r4;
            this.periods = r5;
            this.shuffledWindowIndices = r6;
            this.windowIndicesInShuffled = new int[r6.length];
            int r1 = 0;
        L8:
            if (r1 >= r6.length) goto L10;
            this.windowIndicesInShuffled[r6[r1]] = r1;
            r1 = r1 + 1;
            goto L8
        L10:
            return;
        L5:
            r0 = false;
            goto L6
        }

        @Override // androidx.media3.common.Timeline
        public int getFirstWindowIndex(boolean r3) {
            if (isEmpty() == false) goto L7;
            return -1;
        L7:
            if (r3 == true) goto L9;
            return 0;
        L9:
            return this.shuffledWindowIndices[0];
        }

        @Override // androidx.media3.common.Timeline
        public int getIndexOfPeriod(Object r2) {
            throw new UnsupportedOperationException();
        }

        @Override // androidx.media3.common.Timeline
        public int getLastWindowIndex(boolean r3) {
            if (isEmpty() == false) goto L6;
            return -1;
        L6:
            if (r3 == false) goto L9;
            return this.shuffledWindowIndices[getWindowCount() - 1];
        L9:
            return getWindowCount() - 1;
        }

        @Override // androidx.media3.common.Timeline
        public int getNextWindowIndex(int r4, int r5, boolean r6) {
            if (r5 != 1) goto L6;
            return r4;
        L6:
            if (r4 == getLastWindowIndex(r6)) goto L8;
            if (r6 == false) goto L15;
            return this.shuffledWindowIndices[this.windowIndicesInShuffled[r4] + 1];
        L15:
            return r4 + 1;
        L8:
            if (r5 == 2) goto L16;
            return -1;
        L16:
            return getFirstWindowIndex(r6);
        }

        @Override // androidx.media3.common.Timeline
        public Period getPeriod(int r12, Period r13, boolean r14) {
            Period r0 = this.periods.get(r12);
            r13.set(r0.f30id, r0.uid, r0.windowIndex, r0.durationUs, r0.positionInWindowUs, r0.adPlaybackState, r0.isPlaceholder);
            return r13;
        }

        @Override // androidx.media3.common.Timeline
        public int getPeriodCount() {
            return this.periods.size();
        }

        @Override // androidx.media3.common.Timeline
        public int getPreviousWindowIndex(int r4, int r5, boolean r6) {
            if (r5 != 1) goto L6;
            return r4;
        L6:
            if (r4 == getFirstWindowIndex(r6)) goto L8;
            if (r6 == false) goto L15;
            return this.shuffledWindowIndices[this.windowIndicesInShuffled[r4] - 1];
        L15:
            return r4 - 1;
        L8:
            if (r5 == 2) goto L16;
            return -1;
        L16:
            return getLastWindowIndex(r6);
        }

        @Override // androidx.media3.common.Timeline
        public Object getUidOfPeriod(int r2) {
            throw new UnsupportedOperationException();
        }

        @Override // androidx.media3.common.Timeline
        public Window getWindow(int r27, Window r28, long r29) {
            Window r1 = this.windows.get(r27);
            r28.set(r1.uid, r1.mediaItem, r1.manifest, r1.presentationStartTimeMs, r1.windowStartTimeMs, r1.elapsedRealtimeEpochOffsetMs, r1.isSeekable, r1.isDynamic, r1.liveConfiguration, r1.defaultPositionUs, r1.durationUs, r1.firstPeriodIndex, r1.lastPeriodIndex, r1.positionInFirstPeriodUs);
            r28.isPlaceholder = r1.isPlaceholder;
            return r28;
        }

        @Override // androidx.media3.common.Timeline
        public int getWindowCount() {
            return this.windows.size();
        }
    }

    public static final class Window {
        private static final Object FAKE_WINDOW_UID = null;
        private static final String FIELD_DEFAULT_POSITION_US = null;
        private static final String FIELD_DURATION_US = null;
        private static final String FIELD_ELAPSED_REALTIME_EPOCH_OFFSET_MS = null;
        private static final String FIELD_FIRST_PERIOD_INDEX = null;
        private static final String FIELD_IS_DYNAMIC = null;
        private static final String FIELD_IS_PLACEHOLDER = null;
        private static final String FIELD_IS_SEEKABLE = null;
        private static final String FIELD_LAST_PERIOD_INDEX = null;
        private static final String FIELD_LIVE_CONFIGURATION = null;
        private static final String FIELD_MEDIA_ITEM = null;
        private static final String FIELD_POSITION_IN_FIRST_PERIOD_US = null;
        private static final String FIELD_PRESENTATION_START_TIME_MS = null;
        private static final String FIELD_WINDOW_START_TIME_MS = null;
        private static final MediaItem PLACEHOLDER_MEDIA_ITEM = null;
        public static final Object SINGLE_WINDOW_UID = null;
        public long defaultPositionUs;
        public long durationUs;
        public long elapsedRealtimeEpochOffsetMs;
        public int firstPeriodIndex;
        public boolean isDynamic;
        public boolean isPlaceholder;
        public boolean isSeekable;
        public int lastPeriodIndex;
        public MediaItem.LiveConfiguration liveConfiguration;
        public Object manifest;
        public MediaItem mediaItem;
        public long positionInFirstPeriodUs;
        public long presentationStartTimeMs;

        @Deprecated
        public Object tag;
        public Object uid;
        public long windowStartTimeMs;

        static {
            SINGLE_WINDOW_UID = new Object();
            FAKE_WINDOW_UID = new Object();
            PLACEHOLDER_MEDIA_ITEM = new MediaItem.Builder().setMediaId("androidx.media3.common.Timeline").setUri(Uri.EMPTY).build();
            FIELD_MEDIA_ITEM = Util.intToStringMaxRadix(1);
            FIELD_PRESENTATION_START_TIME_MS = Util.intToStringMaxRadix(2);
            FIELD_WINDOW_START_TIME_MS = Util.intToStringMaxRadix(3);
            FIELD_ELAPSED_REALTIME_EPOCH_OFFSET_MS = Util.intToStringMaxRadix(4);
            FIELD_IS_SEEKABLE = Util.intToStringMaxRadix(5);
            FIELD_IS_DYNAMIC = Util.intToStringMaxRadix(6);
            FIELD_LIVE_CONFIGURATION = Util.intToStringMaxRadix(7);
            FIELD_IS_PLACEHOLDER = Util.intToStringMaxRadix(8);
            FIELD_DEFAULT_POSITION_US = Util.intToStringMaxRadix(9);
            FIELD_DURATION_US = Util.intToStringMaxRadix(10);
            FIELD_FIRST_PERIOD_INDEX = Util.intToStringMaxRadix(11);
            FIELD_LAST_PERIOD_INDEX = Util.intToStringMaxRadix(12);
            FIELD_POSITION_IN_FIRST_PERIOD_US = Util.intToStringMaxRadix(13);
        }

        public Window() {
            this.uid = SINGLE_WINDOW_UID;
            this.mediaItem = PLACEHOLDER_MEDIA_ITEM;
        }

        public static Window fromBundle(Bundle r30) {
            Bundle r1 = r30.getBundle(FIELD_MEDIA_ITEM);
            if (r1 == null) goto L5;
            MediaItem r2 = MediaItem.fromBundle(r1);
        L6:
            MediaItem r5 = r2;
            long r7 = r30.getLong(FIELD_PRESENTATION_START_TIME_MS, C0180C.TIME_UNSET);
            long r9 = r30.getLong(FIELD_WINDOW_START_TIME_MS, C0180C.TIME_UNSET);
            long r11 = r30.getLong(FIELD_ELAPSED_REALTIME_EPOCH_OFFSET_MS, C0180C.TIME_UNSET);
            boolean r13 = r30.getBoolean(FIELD_IS_SEEKABLE, false);
            boolean r14 = r30.getBoolean(FIELD_IS_DYNAMIC, false);
            Bundle r3 = r30.getBundle(FIELD_LIVE_CONFIGURATION);
            if (r3 == null) goto L9;
            MediaItem.LiveConfiguration r15 = MediaItem.LiveConfiguration.fromBundle(r3);
        L10:
            boolean r4 = r30.getBoolean(FIELD_IS_PLACEHOLDER, false);
            long r20 = r30.getLong(FIELD_DEFAULT_POSITION_US, 0);
            long r8 = r30.getLong(FIELD_DURATION_US, C0180C.TIME_UNSET);
            int r6 = r30.getInt(FIELD_FIRST_PERIOD_INDEX, 0);
            int r10 = r30.getInt(FIELD_LAST_PERIOD_INDEX, 0);
            long r12 = r30.getLong(FIELD_POSITION_IN_FIRST_PERIOD_US, 0);
            Window r16 = new Window();
            r16.set(FAKE_WINDOW_UID, r5, null, r7, r9, r11, r13, r14, r15, r20, r8, r6, r10, r12);
            r16.isPlaceholder = r4;
            return r16;
        L9:
            r15 = null;
            goto L10
        L5:
            r2 = MediaItem.EMPTY;
            goto L6
        }

        public boolean equals(Object r8) {
            if (this != r8) goto L6;
            return true;
        L6:
            if (r8 != null) goto L8;
        L43:
            return false;
        L8:
            if (getClass().equals(r8.getClass()) == false) goto L43;
            Window r2 = (Window) r8;
            if (Util.areEqual(this.uid, r2.uid) == false) goto L42;
            if (Util.areEqual(this.mediaItem, r2.mediaItem) == false) goto L42;
            if (Util.areEqual(this.manifest, r2.manifest) == false) goto L42;
            if (Util.areEqual(this.liveConfiguration, r2.liveConfiguration) == false) goto L42;
            if (this.presentationStartTimeMs != r2.presentationStartTimeMs) goto L42;
            if (this.windowStartTimeMs != r2.windowStartTimeMs) goto L42;
            if (this.elapsedRealtimeEpochOffsetMs != r2.elapsedRealtimeEpochOffsetMs) goto L42;
            if (this.isSeekable != r2.isSeekable) goto L42;
            if (this.isDynamic != r2.isDynamic) goto L42;
            if (this.isPlaceholder != r2.isPlaceholder) goto L42;
            if (this.defaultPositionUs != r2.defaultPositionUs) goto L42;
            if (this.durationUs != r2.durationUs) goto L42;
            if (this.firstPeriodIndex != r2.firstPeriodIndex) goto L42;
            if (this.lastPeriodIndex != r2.lastPeriodIndex) goto L42;
            if (this.positionInFirstPeriodUs != r2.positionInFirstPeriodUs) goto L42;
            return true;
        L42:
            return false;
        }

        public long getCurrentUnixTimeMs() {
            return Util.getNowUnixTimeMs(this.elapsedRealtimeEpochOffsetMs);
        }

        public long getDefaultPositionMs() {
            return Util.usToMs(this.defaultPositionUs);
        }

        public long getDefaultPositionUs() {
            return this.defaultPositionUs;
        }

        public long getDurationMs() {
            return Util.usToMs(this.durationUs);
        }

        public long getDurationUs() {
            return this.durationUs;
        }

        public long getPositionInFirstPeriodMs() {
            return Util.usToMs(this.positionInFirstPeriodUs);
        }

        public long getPositionInFirstPeriodUs() {
            return this.positionInFirstPeriodUs;
        }

        public int hashCode() {
            int r1 = ((((7 * 31) + this.uid.hashCode()) * 31) + this.mediaItem.hashCode()) * 31;
            int r3 = 0;
            if (this.manifest != null) goto L5;
            int r2 = 0;
        L6:
            int r0 = (r1 + r2) * 31;
            if (this.liveConfiguration == null) goto L11;
            r3 = this.liveConfiguration.hashCode();
        L11:
            return ((((((((((((((((((((((r0 + r3) * 31) + ((int) (this.presentationStartTimeMs ^ (this.presentationStartTimeMs >>> 32)))) * 31) + ((int) (this.windowStartTimeMs ^ (this.windowStartTimeMs >>> 32)))) * 31) + ((int) (this.elapsedRealtimeEpochOffsetMs ^ (this.elapsedRealtimeEpochOffsetMs >>> 32)))) * 31) + (this.isSeekable ? 1 : 0)) * 31) + (this.isDynamic ? 1 : 0)) * 31) + (this.isPlaceholder ? 1 : 0)) * 31) + ((int) (this.defaultPositionUs ^ (this.defaultPositionUs >>> 32)))) * 31) + ((int) (this.durationUs ^ (this.durationUs >>> 32)))) * 31) + this.firstPeriodIndex) * 31) + this.lastPeriodIndex) * 31) + ((int) (this.positionInFirstPeriodUs ^ (this.positionInFirstPeriodUs >>> 32)));
        L5:
            r2 = this.manifest.hashCode();
            goto L6
        }

        public boolean isLive() {
            if (this.liveConfiguration == null) goto L5;
            return true;
        L5:
            return false;
        }

        public Window set(Object r17, MediaItem r18, Object r19, long r20, long r22, long r24, boolean r26, boolean r27, MediaItem.LiveConfiguration r28, long r29, long r31, int r33, int r34, long r35) {
            this.uid = r17;
            if (r18 == null) goto L5;
            MediaItem r3 = r18;
        L6:
            this.mediaItem = r3;
            if (r18 != null) goto L9;
        L11:
            Object r4 = null;
        L12:
            this.tag = r4;
            this.manifest = r19;
            this.presentationStartTimeMs = r20;
            this.windowStartTimeMs = r22;
            this.elapsedRealtimeEpochOffsetMs = r24;
            this.isSeekable = r26;
            this.isDynamic = r27;
            this.liveConfiguration = r28;
            this.defaultPositionUs = r29;
            this.durationUs = r31;
            this.firstPeriodIndex = r33;
            this.lastPeriodIndex = r34;
            this.positionInFirstPeriodUs = r35;
            this.isPlaceholder = false;
            return this;
        L9:
            if (r18.localConfiguration == null) goto L11;
            r4 = r18.localConfiguration.tag;
            goto L12
        L5:
            r3 = PLACEHOLDER_MEDIA_ITEM;
            goto L6
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            if (MediaItem.EMPTY.equals(this.mediaItem) == true) goto L6;
            r0.putBundle(FIELD_MEDIA_ITEM, this.mediaItem.toBundle());
        L6:
            if (this.presentationStartTimeMs == C0180C.TIME_UNSET) goto L9;
            r0.putLong(FIELD_PRESENTATION_START_TIME_MS, this.presentationStartTimeMs);
        L9:
            if (this.windowStartTimeMs == C0180C.TIME_UNSET) goto L12;
            r0.putLong(FIELD_WINDOW_START_TIME_MS, this.windowStartTimeMs);
        L12:
            if (this.elapsedRealtimeEpochOffsetMs == C0180C.TIME_UNSET) goto L15;
            r0.putLong(FIELD_ELAPSED_REALTIME_EPOCH_OFFSET_MS, this.elapsedRealtimeEpochOffsetMs);
        L15:
            if (this.isSeekable == false) goto L18;
            r0.putBoolean(FIELD_IS_SEEKABLE, this.isSeekable);
        L18:
            if (this.isDynamic == false) goto L20;
            r0.putBoolean(FIELD_IS_DYNAMIC, this.isDynamic);
        L20:
            MediaItem.LiveConfiguration r1 = this.liveConfiguration;
            if (r1 == null) goto L24;
            r0.putBundle(FIELD_LIVE_CONFIGURATION, r1.toBundle());
        L24:
            if (this.isPlaceholder == false) goto L27;
            r0.putBoolean(FIELD_IS_PLACEHOLDER, this.isPlaceholder);
        L27:
            if (this.defaultPositionUs == 0) goto L30;
            r0.putLong(FIELD_DEFAULT_POSITION_US, this.defaultPositionUs);
        L30:
            if (this.durationUs == C0180C.TIME_UNSET) goto L33;
            r0.putLong(FIELD_DURATION_US, this.durationUs);
        L33:
            if (this.firstPeriodIndex == 0) goto L36;
            r0.putInt(FIELD_FIRST_PERIOD_INDEX, this.firstPeriodIndex);
        L36:
            if (this.lastPeriodIndex == 0) goto L39;
            r0.putInt(FIELD_LAST_PERIOD_INDEX, this.lastPeriodIndex);
        L39:
            if (this.positionInFirstPeriodUs == 0) goto L41;
            r0.putLong(FIELD_POSITION_IN_FIRST_PERIOD_US, this.positionInFirstPeriodUs);
        L41:
            return r0;
        }
    }

    static {
        EMPTY = new C01961();
        FIELD_WINDOWS = Util.intToStringMaxRadix(0);
        FIELD_PERIODS = Util.intToStringMaxRadix(1);
        FIELD_SHUFFLED_WINDOW_INDICES = Util.intToStringMaxRadix(2);
    }

    protected Timeline() {
    }

    public static Timeline fromBundle(Bundle r5) {
        ImmutableList r0 = fromBundleListRetriever(new Timeline$$ExternalSyntheticLambda0(), r5.getBinder(FIELD_WINDOWS));
        ImmutableList r1 = fromBundleListRetriever(new Timeline$$ExternalSyntheticLambda1(), r5.getBinder(FIELD_PERIODS));
        int[] r2 = r5.getIntArray(FIELD_SHUFFLED_WINDOW_INDICES);
        if (r2 != null) goto L5;
        int[] r4 = generateUnshuffledIndices(r0.size());
    L7:
        return new RemotableTimeline(r0, r1, r4);
    L5:
        r4 = r2;
        goto L7
    }

    private static <T> ImmutableList<T> fromBundleListRetriever(Function<Bundle, T> r1, IBinder r2) {
        if (r2 != null) goto L6;
        return ImmutableList.m212of();
    L6:
        return BundleCollectionUtil.fromBundleList(r1, BundleListRetriever.getList(r2));
    }

    private static int[] generateUnshuffledIndices(int r2) {
        int[] r0 = new int[r2];
        int r1 = 0;
    L3:
        if (r1 >= r2) goto L5;
        r0[r1] = r1;
        r1 = r1 + 1;
        goto L3
    L5:
        return r0;
    }

    public final Timeline copyWithSingleWindow(int r7) {
        if (getWindowCount() != 1) goto L5;
        return this;
    L5:
        Window r0 = getWindow(r7, new Window(), 0);
        ImmutableList.Builder r2 = ImmutableList.builder();
        int r3 = r0.firstPeriodIndex;
    L7:
        if (r3 > r0.lastPeriodIndex) goto L9;
        Period r4 = getPeriod(r3, new Period(), true);
        r4.windowIndex = 0;
        r2.add(r4);
        r3 = r3 + 1;
        goto L7
    L9:
        r0.lastPeriodIndex -= r0.firstPeriodIndex;
        r0.firstPeriodIndex = 0;
        return new RemotableTimeline(ImmutableList.m213of(r0), r2.build(), new int[]{0});
    }

    public boolean equals(Object r12) {
        if (this != r12) goto L6;
        return true;
    L6:
        if ((r12 instanceof Timeline) == true) goto L8;
        return false;
    L8:
        Timeline r1 = (Timeline) r12;
        if (r1.getWindowCount() == getWindowCount()) goto L11;
    L39:
        return false;
    L11:
        if (r1.getPeriodCount() != getPeriodCount()) goto L39;
        Window r3 = new Window();
        Period r4 = new Period();
        Window r5 = new Window();
        Period r6 = new Period();
        int r7 = 0;
    L15:
        if (r7 >= getWindowCount()) goto L20;
        if (getWindow(r7, r3).equals(r1.getWindow(r7, r5)) == false) goto L18;
        r7 = r7 + 1;
        goto L15
    L18:
        return false;
    L20:
        int r8 = 0;
    L22:
        if (r8 >= getPeriodCount()) goto L27;
        if (getPeriod(r8, r4, true).equals(r1.getPeriod(r8, r6, true)) == false) goto L25;
        r8 = r8 + 1;
        goto L22
    L25:
        return false;
    L27:
        int r9 = getFirstWindowIndex(true);
        if (r9 == r1.getFirstWindowIndex(true)) goto L30;
        return false;
    L30:
        int r10 = getLastWindowIndex(true);
        if (r10 == r1.getLastWindowIndex(true)) goto L33;
        return false;
    L33:
        if (r9 == r10) goto L38;
        int r11 = getNextWindowIndex(r9, 0, true);
        if (r11 != r1.getNextWindowIndex(r9, 0, true)) goto L36;
        r9 = r11;
        goto L33
    L36:
        return false;
    L38:
        return true;
    }

    public int getFirstWindowIndex(boolean r2) {
        if (isEmpty() == false) goto L5;
        return -1;
    L5:
        return 0;
    }

    public abstract int getIndexOfPeriod(Object r1);

    public int getLastWindowIndex(boolean r2) {
        if (isEmpty() == false) goto L6;
        return -1;
    L6:
        return getWindowCount() - 1;
    }

    public final int getNextPeriodIndex(int r4, Period r5, Window r6, int r7, boolean r8) {
        int r0 = getPeriod(r4, r5).windowIndex;
        if (getWindow(r0, r6).lastPeriodIndex != r4) goto L10;
        int r1 = getNextWindowIndex(r0, r7, r8);
        if (r1 != (-1)) goto L8;
        return -1;
    L8:
        return getWindow(r1, r6).firstPeriodIndex;
    L10:
        return r4 + 1;
    }

    public int getNextWindowIndex(int r2, int r3, boolean r4) {
        switch(r3) {
            case 0: goto L12;
            case 1: goto L10;
            case 2: goto L6;
            default: goto L4;
        };
    L10:
        return r2;
    L4:
        throw new IllegalStateException();
    L6:
        if (r2 != getLastWindowIndex(r4)) goto L9;
        return getFirstWindowIndex(r4);
    L9:
        return r2 + 1;
    L12:
        if (r2 != getLastWindowIndex(r4)) goto L15;
        return -1;
    L15:
        return r2 + 1;
    }

    public final Period getPeriod(int r2, Period r3) {
        return getPeriod(r2, r3, false);
    }

    public abstract Period getPeriod(int r1, Period r2, boolean r3);

    public Period getPeriodByUid(Object r3, Period r4) {
        return getPeriod(getIndexOfPeriod(r3), r4, true);
    }

    public abstract int getPeriodCount();

    @Deprecated
    public final Pair<Object, Long> getPeriodPosition(Window r2, Period r3, int r4, long r5) {
        return getPeriodPositionUs(r2, r3, r4, r5);
    }

    @Deprecated
    public final Pair<Object, Long> getPeriodPosition(Window r2, Period r3, int r4, long r5, long r7) {
        return getPeriodPositionUs(r2, r3, r4, r5, r7);
    }

    public final Pair<Object, Long> getPeriodPositionUs(Window r9, Period r10, int r11, long r12) {
        return (Pair) Assertions.checkNotNull(getPeriodPositionUs(r9, r10, r11, r12, 0));
    }

    public final Pair<Object, Long> getPeriodPositionUs(Window r8, Period r9, int r10, long r11, long r13) {
        Assertions.checkIndex(r10, 0, getWindowCount());
        getWindow(r10, r8, r13);
        if (r11 != C0180C.TIME_UNSET) goto L8;
        r11 = r8.getDefaultPositionUs();
        if (r11 != C0180C.TIME_UNSET) goto L8;
        return null;
    L8:
        int r2 = r8.firstPeriodIndex;
        getPeriod(r2, r9);
    L10:
        if (r2 >= r8.lastPeriodIndex) goto L16;
        if (r9.positionInWindowUs == r11) goto L16;
        if (getPeriod(r2 + 1, r9).positionInWindowUs > r11) goto L16;
        r2 = r2 + 1;
    L16:
        getPeriod(r2, r9, true);
        long r3 = r11 - r9.positionInWindowUs;
        if (r9.durationUs == C0180C.TIME_UNSET) goto L19;
        r3 = Math.min(r3, r9.durationUs - 1);
    L19:
        long r0 = Math.max(0, r3);
        return Pair.create(Assertions.checkNotNull(r9.uid), Long.valueOf(r0));
    }

    public int getPreviousWindowIndex(int r2, int r3, boolean r4) {
        switch(r3) {
            case 0: goto L12;
            case 1: goto L10;
            case 2: goto L6;
            default: goto L4;
        };
    L10:
        return r2;
    L4:
        throw new IllegalStateException();
    L6:
        if (r2 != getFirstWindowIndex(r4)) goto L9;
        return getLastWindowIndex(r4);
    L9:
        return r2 - 1;
    L12:
        if (r2 != getFirstWindowIndex(r4)) goto L15;
        return -1;
    L15:
        return r2 - 1;
    }

    public abstract Object getUidOfPeriod(int r1);

    public final Window getWindow(int r3, Window r4) {
        return getWindow(r3, r4, 0);
    }

    public abstract Window getWindow(int r1, Window r2, long r3);

    public abstract int getWindowCount();

    public int hashCode() {
        Window r0 = new Window();
        Period r1 = new Period();
        int r3 = (7 * 31) + getWindowCount();
        int r2 = 0;
    L4:
        if (r2 >= getWindowCount()) goto L6;
        r3 = (r3 * 31) + getWindow(r2, r0).hashCode();
        r2 = r2 + 1;
        goto L4
    L6:
        int r4 = (r3 * 31) + getPeriodCount();
        int r5 = 0;
    L8:
        if (r5 >= getPeriodCount()) goto L10;
        r4 = (r4 * 31) + getPeriod(r5, r1, true).hashCode();
        r5 = r5 + 1;
        goto L8
    L10:
        int r6 = getFirstWindowIndex(true);
    L12:
        if (r6 == (-1)) goto L14;
        r4 = (r4 * 31) + r6;
        r6 = getNextWindowIndex(r6, 0, true);
        goto L12
    L14:
        return r4;
    }

    public final boolean isEmpty() {
        if (getWindowCount() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    public final boolean isLastPeriod(int r3, Period r4, Window r5, int r6, boolean r7) {
        if (getNextPeriodIndex(r3, r4, r5, r6, r7) != (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    public final Bundle toBundle() {
        ArrayList r0 = new ArrayList();
        int r1 = getWindowCount();
        Window r2 = new Window();
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L5;
        r0.add(getWindow(r3, r2, 0).toBundle());
        r3 = r3 + 1;
        goto L3
    L5:
        ArrayList r4 = new ArrayList();
        int r5 = getPeriodCount();
        Period r6 = new Period();
        int r7 = 0;
    L7:
        if (r7 >= r5) goto L9;
        r4.add(getPeriod(r7, r6, false).toBundle());
        r7 = r7 + 1;
        goto L7
    L9:
        int[] r8 = new int[r1];
        if (r1 <= 0) goto L12;
        r8[0] = getFirstWindowIndex(true);
    L12:
        int r9 = 1;
    L13:
        if (r9 >= r1) goto L15;
        r8[r9] = getNextWindowIndex(r8[r9 - 1], 0, true);
        r9 = r9 + 1;
        goto L13
    L15:
        Bundle r10 = new Bundle();
        r10.putBinder(FIELD_WINDOWS, new BundleListRetriever(r0));
        r10.putBinder(FIELD_PERIODS, new BundleListRetriever(r4));
        r10.putIntArray(FIELD_SHUFFLED_WINDOW_INDICES, r8);
        return r10;
    }
}
