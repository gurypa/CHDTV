package androidx.media3.exoplayer.source;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.upstream.Allocator;
import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.IdentityHashMap;

/* JADX INFO: loaded from: classes.dex */
public final class ConcatenatingMediaSource2 extends CompositeMediaSource<Integer> {
    private static final int MSG_UPDATE_TIMELINE = 1;
    private MediaItem mediaItem;
    private final IdentityHashMap<MediaPeriod, MediaSourceHolder> mediaSourceByMediaPeriod;
    private final ImmutableList<MediaSourceHolder> mediaSourceHolders;
    private Handler playbackThreadHandler;
    private boolean timelineUpdateScheduled;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.ConcatenatingMediaSource2$1 */
    static /* synthetic */ class C03271 {
    }

    public static final class Builder {
        private int index;
        private MediaItem mediaItem;
        private MediaSource.Factory mediaSourceFactory;
        private final ImmutableList.Builder<MediaSourceHolder> mediaSourceHoldersBuilder;

        public Builder() {
            this.mediaSourceHoldersBuilder = ImmutableList.builder();
        }

        public Builder add(MediaItem r3) {
            return add(r3, C0180C.TIME_UNSET);
        }

        public Builder add(MediaItem r5, long r6) {
            Assertions.checkNotNull(r5);
            if (r6 == C0180C.TIME_UNSET) goto L5;
        L7:
            Assertions.checkStateNotNull(this.mediaSourceFactory, "Must use useDefaultMediaSourceFactory or setMediaSourceFactory first.");
            return add(this.mediaSourceFactory.createMediaSource(r5), r6);
        L5:
            if (r5.clippingConfiguration.endPositionMs == Long.MIN_VALUE) goto L7;
            r6 = Util.usToMs(r5.clippingConfiguration.endPositionUs - r5.clippingConfiguration.startPositionUs);
            goto L7
        }

        public Builder add(MediaSource r3) {
            return add(r3, C0180C.TIME_UNSET);
        }

        public Builder add(MediaSource r6, long r7) {
            Assertions.checkNotNull(r6);
            if ((r6 instanceof ProgressiveMediaSource) == true) goto L5;
        L8:
            boolean r0 = true;
        L9:
            Assertions.checkState(r0, "Progressive media source must define an initial placeholder duration.");
            ImmutableList.Builder<MediaSourceHolder> r1 = this.mediaSourceHoldersBuilder;
            int r2 = this.index;
            this.index = r2 + 1;
            r1.add(new MediaSourceHolder(r6, r2, Util.msToUs(r7)));
            return this;
        L5:
            if (r7 != C0180C.TIME_UNSET) goto L8;
            r0 = false;
            goto L9
        }

        public ConcatenatingMediaSource2 build() {
            if (this.index <= 0) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0, "Must add at least one source to the concatenation.");
            if (this.mediaItem != null) goto L10;
            this.mediaItem = MediaItem.fromUri(Uri.EMPTY);
        L10:
            return new ConcatenatingMediaSource2(this.mediaItem, this.mediaSourceHoldersBuilder.build(), null);
        L5:
            r0 = false;
            goto L6
        }

        public Builder setMediaItem(MediaItem r1) {
            this.mediaItem = r1;
            return this;
        }

        public Builder setMediaSourceFactory(MediaSource.Factory r2) {
            this.mediaSourceFactory = (MediaSource.Factory) Assertions.checkNotNull(r2);
            return this;
        }

        public Builder useDefaultMediaSourceFactory(Context r2) {
            return setMediaSourceFactory(new DefaultMediaSourceFactory(r2));
        }
    }

    private static final class ConcatenatedTimeline extends Timeline {
        private final long defaultPositionUs;
        private final long durationUs;
        private final ImmutableList<Integer> firstPeriodIndices;
        private final boolean isDynamic;
        private final boolean isSeekable;
        private final Object manifest;
        private final MediaItem mediaItem;
        private final ImmutableList<Long> periodOffsetsInWindowUs;
        private final ImmutableList<Timeline> timelines;

        public ConcatenatedTimeline(MediaItem r1, ImmutableList<Timeline> r2, ImmutableList<Integer> r3, ImmutableList<Long> r4, boolean r5, boolean r6, long r7, long r9, Object r11) {
            this.mediaItem = r1;
            this.timelines = r2;
            this.firstPeriodIndices = r3;
            this.periodOffsetsInWindowUs = r4;
            this.isSeekable = r5;
            this.isDynamic = r6;
            this.durationUs = r7;
            this.defaultPositionUs = r9;
            this.manifest = r11;
        }

        private int getChildIndexByPeriodIndex(int r4) {
            return Util.binarySearchFloor(this.firstPeriodIndices, Integer.valueOf(r4 + 1), false, false);
        }

        private long getPeriodDurationUs(Timeline.Period r7, int r8) {
            if (r7.durationUs != C0180C.TIME_UNSET) goto L5;
            return C0180C.TIME_UNSET;
        L5:
            long r0 = this.periodOffsetsInWindowUs.get(r8).longValue();
            if (r8 != (this.periodOffsetsInWindowUs.size() - 1)) goto L8;
            long r2 = this.durationUs;
        L10:
            return r2 - r0;
        L8:
            r2 = this.periodOffsetsInWindowUs.get(r8 + 1).longValue();
            goto L10
        }

        @Override // androidx.media3.common.Timeline
        public int getIndexOfPeriod(Object r5) {
            if ((r5 instanceof Pair) == true) goto L5;
        L12:
            return -1;
        L5:
            if ((((Pair) r5).first instanceof Integer) == false) goto L12;
            int r0 = ConcatenatingMediaSource2.access$100(r5);
            Object r2 = ConcatenatingMediaSource2.access$200(r5);
            int r3 = this.timelines.get(r0).getIndexOfPeriod(r2);
            if (r3 != (-1)) goto L11;
            return -1;
        L11:
            return this.firstPeriodIndices.get(r0).intValue() + r3;
        }

        @Override // androidx.media3.common.Timeline
        public Timeline.Period getPeriod(int r5, Timeline.Period r6, boolean r7) {
            int r0 = getChildIndexByPeriodIndex(r5);
            int r1 = this.firstPeriodIndices.get(r0).intValue();
            this.timelines.get(r0).getPeriod(r5 - r1, r6, r7);
            r6.windowIndex = 0;
            r6.positionInWindowUs = this.periodOffsetsInWindowUs.get(r5).longValue();
            r6.durationUs = getPeriodDurationUs(r6, r5);
            if (r7 == false) goto L5;
            r6.uid = ConcatenatingMediaSource2.access$300(r0, Assertions.checkNotNull(r6.uid));
        L5:
            return r6;
        }

        @Override // androidx.media3.common.Timeline
        public Timeline.Period getPeriodByUid(Object r7, Timeline.Period r8) {
            int r0 = ConcatenatingMediaSource2.access$100(r7);
            Object r1 = ConcatenatingMediaSource2.access$200(r7);
            Timeline r2 = this.timelines.get(r0);
            int r3 = this.firstPeriodIndices.get(r0).intValue() + r2.getIndexOfPeriod(r1);
            r2.getPeriodByUid(r1, r8);
            r8.windowIndex = 0;
            r8.positionInWindowUs = this.periodOffsetsInWindowUs.get(r3).longValue();
            r8.durationUs = getPeriodDurationUs(r8, r3);
            r8.uid = r7;
            return r8;
        }

        @Override // androidx.media3.common.Timeline
        public int getPeriodCount() {
            return this.periodOffsetsInWindowUs.size();
        }

        @Override // androidx.media3.common.Timeline
        public Object getUidOfPeriod(int r5) {
            int r0 = getChildIndexByPeriodIndex(r5);
            int r1 = this.firstPeriodIndices.get(r0).intValue();
            return ConcatenatingMediaSource2.access$300(r0, this.timelines.get(r0).getUidOfPeriod(r5 - r1));
        }

        @Override // androidx.media3.common.Timeline
        public Timeline.Window getWindow(int r23, Timeline.Window r24, long r25) {
            return r24.set(Timeline.Window.SINGLE_WINDOW_UID, this.mediaItem, this.manifest, C0180C.TIME_UNSET, C0180C.TIME_UNSET, C0180C.TIME_UNSET, this.isSeekable, this.isDynamic, null, this.defaultPositionUs, this.durationUs, 0, getPeriodCount() - 1, -this.periodOffsetsInWindowUs.get(0).longValue());
        }

        @Override // androidx.media3.common.Timeline
        public int getWindowCount() {
            return 1;
        }
    }

    static final class MediaSourceHolder {
        public int activeMediaPeriods;
        public final int index;
        public final long initialPlaceholderDurationUs;
        public final MaskingMediaSource mediaSource;
        public final HashMap<Object, Long> periodTimeOffsetsByUid;

        public MediaSourceHolder(MediaSource r3, int r4, long r5) {
            this.mediaSource = new MaskingMediaSource(r3, false);
            this.index = r4;
            this.initialPlaceholderDurationUs = r5;
            this.periodTimeOffsetsByUid = new HashMap();
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$bXRn9-o-6gWY7wzcXHo5E0cUSmI, reason: not valid java name */
    public static /* synthetic */ boolean m478$r8$lambda$bXRn9o6gWY7wzcXHo5E0cUSmI(ConcatenatingMediaSource2 r0, Message r1) {
        return r0.handleMessage(r1);
    }

    private ConcatenatingMediaSource2(MediaItem r2, ImmutableList<MediaSourceHolder> r3) {
        this.mediaItem = r2;
        this.mediaSourceHolders = r3;
        this.mediaSourceByMediaPeriod = new IdentityHashMap();
    }

    /* synthetic */ ConcatenatingMediaSource2(MediaItem r1, ImmutableList r2, C03271 r3) {
        this(r1, r2);
    }

    static /* synthetic */ int access$100(Object r1) {
        return getChildIndex(r1);
    }

    static /* synthetic */ Object access$200(Object r1) {
        return getChildPeriodUid(r1);
    }

    static /* synthetic */ Object access$300(int r1, Object r2) {
        return getPeriodUid(r1, r2);
    }

    private void disableUnusedMediaSources() {
        int r0 = 0;
    L4:
        if (r0 >= this.mediaSourceHolders.size()) goto L9;
        MediaSourceHolder r1 = this.mediaSourceHolders.get(r0);
        if (r1.activeMediaPeriods != 0) goto L8;
        disableChildSource(Integer.valueOf(r1.index));
    L8:
        r0 = r0 + 1;
        goto L4
    }

    private static int getChildIndex(Object r1) {
        return ((Integer) ((Pair) r1).first).intValue();
    }

    private static int getChildIndexFromChildWindowSequenceNumber(long r2, int r4) {
        return (int) (r2 % ((long) r4));
    }

    private static Object getChildPeriodUid(Object r1) {
        return ((Pair) r1).second;
    }

    private static long getChildWindowSequenceNumber(long r4, int r6, int r7) {
        return (((long) r6) * r4) + ((long) r7);
    }

    private static Object getPeriodUid(int r1, Object r2) {
        return Pair.create(Integer.valueOf(r1), r2);
    }

    private static long getWindowSequenceNumberFromChildWindowSequenceNumber(long r2, int r4) {
        return r2 / ((long) r4);
    }

    private boolean handleMessage(Message r3) {
        if (r3.what != 1) goto L5;
        updateTimeline();
    L5:
        return true;
    }

    private ConcatenatedTimeline maybeCreateConcatenatedTimeline() {
        ConcatenatingMediaSource2 r0 = this;
        Timeline.Window r1 = new Timeline.Window();
        Timeline.Period r2 = new Timeline.Period();
        ImmutableList.Builder r3 = ImmutableList.builder();
        ImmutableList.Builder r4 = ImmutableList.builder();
        ImmutableList.Builder r5 = ImmutableList.builder();
        long r13 = 0;
        boolean r15 = true;
        boolean r16 = false;
        Object r17 = null;
        int r18 = 0;
        int r6 = r0.mediaSourceHolders.size();
        boolean r24 = true;
        boolean r25 = false;
        long r26 = 0;
        long r28 = 0;
        int r7 = 0;
    L3:
        if (r7 >= r6) goto L73;
        MediaSourceHolder r9 = r0.mediaSourceHolders.get(r7);
        Timeline r10 = r9.mediaSource.getTimeline();
        Assertions.checkArgument(!r10.isEmpty(), "Can't concatenate empty child Timeline.");
        r3.add(r10);
        r4.add(Integer.valueOf(r18));
        r18 = r18 + r10.getPeriodCount();
        int r11 = 0;
    L6:
        if (r11 >= r10.getWindowCount()) goto L72;
        r10.getWindow(r11, r1);
        if (r16 == true) goto L10;
        Object r12 = r1.manifest;
        r16 = true;
    L12:
        if (r15 == true) goto L14;
    L16:
        boolean r8 = false;
    L17:
        r15 = r8;
        ImmutableList.Builder r14 = r3;
        ImmutableList.Builder r31 = r4;
        long r19 = r1.durationUs;
        if (r19 != C0180C.TIME_UNSET) goto L24;
        if (r9.initialPlaceholderDurationUs == C0180C.TIME_UNSET) goto L21;
        r19 = r9.initialPlaceholderDurationUs;
        goto L24
    L21:
        return null;
    L24:
        r26 = r26 + r19;
        if (r9.index != 0) goto L30;
        if (r11 != 0) goto L30;
        r28 = r1.defaultPositionUs;
        r13 = -r1.positionInFirstPeriodUs;
    L30:
        if (r1.isSeekable == false) goto L32;
    L35:
        boolean r20 = true;
    L36:
        r24 = r24 & r20;
        r25 = r25 | r1.isDynamic;
        int r21 = r1.firstPeriodIndex;
    L38:
        if (r21 > r1.lastPeriodIndex) goto L71;
        r5.add(Long.valueOf(r13));
        r10.getPeriod(r21, r2, true);
        long r22 = r2.durationUs;
        if (r22 != C0180C.TIME_UNSET) goto L47;
        if (r1.firstPeriodIndex != r1.lastPeriodIndex) goto L44;
        boolean r23 = true;
    L45:
        Assertions.checkArgument(r23, "Can't apply placeholder duration to multiple periods with unknown duration in a single window.");
        r22 = r19 + r1.positionInFirstPeriodUs;
        goto L47
    L44:
        r23 = false;
    L47:
        long r35 = 0;
        long r37 = r22;
        if (r21 == r1.firstPeriodIndex) goto L50;
    L53:
        boolean r27 = false;
    L54:
        if (r27 == true) goto L56;
    L58:
        long r29 = r37;
    L59:
        int r30 = r21;
        Object r32 = Assertions.checkNotNull(r2.uid);
        Timeline.Window r38 = r1;
        if (r9.activeMediaPeriods != 0) goto L62;
        Timeline.Period r39 = r2;
    L69:
        boolean r33 = true;
    L70:
        Assertions.checkArgument(r33, "Can't handle windows with changing offset in first period.");
        r9.periodTimeOffsetsByUid.put(r32, Long.valueOf(r35));
        r13 = r13 + r29;
        r21 = r30 + 1;
        r1 = r38;
        r2 = r39;
        goto L38
    L62:
        if (r9.periodTimeOffsetsByUid.containsKey(r32) == false) goto L67;
        r39 = r2;
        if (r9.periodTimeOffsetsByUid.get(r32).equals(Long.valueOf(r35)) == true) goto L69;
        r33 = false;
        goto L70
    L67:
        r39 = r2;
        goto L69
    L56:
        if (r37 == C0180C.TIME_UNSET) goto L58;
        long r34 = -r1.positionInFirstPeriodUs;
        r35 = r34;
        r29 = r37 + r34;
        goto L59
    L50:
        if (r9.index != 0) goto L52;
        if (r11 == 0) goto L53;
    L52:
        r27 = true;
        goto L54
    L71:
        r11 = r11 + 1;
        r17 = r12;
        r4 = r31;
        r3 = r14;
        goto L6
    L32:
        if (r1.isPlaceholder == true) goto L35;
        r20 = false;
        goto L36
    L14:
        if (Util.areEqual(r12, r1.manifest) == false) goto L16;
        r8 = true;
        goto L17
    L10:
        r12 = r17;
        goto L12
    L72:
        r7 = r7 + 1;
        r0 = this;
        goto L3
    L73:
        MediaItem r210 = getMediaItem();
        ImmutableList r211 = r3.build();
        ImmutableList r212 = r4.build();
        ImmutableList r213 = r5.build();
        if (r15 == false) goto L76;
        Object r36 = r17;
    L78:
        return new ConcatenatedTimeline(r210, r211, r212, r213, r24, r25, r26, r28, r36);
    L76:
        r36 = null;
        goto L78
    }

    private void scheduleTimelineUpdate() {
        if (this.timelineUpdateScheduled == true) goto L6;
        ((Handler) Assertions.checkNotNull(this.playbackThreadHandler)).obtainMessage(1).sendToTarget();
        this.timelineUpdateScheduled = true;
        return;
    }

    private void updateTimeline() {
        this.timelineUpdateScheduled = false;
        ConcatenatedTimeline r0 = maybeCreateConcatenatedTimeline();
        if (r0 == null) goto L6;
        refreshSourceInfo(r0);
        return;
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean canUpdateMediaItem(MediaItem r2) {
        return true;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r10, Allocator r11, long r12) {
        MediaSourceHolder r1 = this.mediaSourceHolders.get(getChildIndex(r10.periodUid));
        MediaSource.MediaPeriodId r2 = r10.copyWithPeriodUid(getChildPeriodUid(r10.periodUid)).copyWithWindowSequenceNumber(getChildWindowSequenceNumber(r10.windowSequenceNumber, this.mediaSourceHolders.size(), r1.index));
        enableChildSource(Integer.valueOf(r1.index));
        r1.activeMediaPeriods++;
        if (r10.isAd() == false) goto L5;
        long r3 = 0;
    L6:
        TimeOffsetMediaPeriod r5 = new TimeOffsetMediaPeriod(r1.mediaSource.createPeriod(r2, r11, r12 - r3), r3);
        this.mediaSourceByMediaPeriod.put(r5, r1);
        disableUnusedMediaSources();
        return r5;
    L5:
        r3 = ((Long) Assertions.checkNotNull(r1.periodTimeOffsetsByUid.get(r2.periodUid))).longValue();
        goto L6
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    protected void enableInternal() {
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public Timeline getInitialTimeline() {
        return maybeCreateConcatenatedTimeline();
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public synchronized MediaItem getMediaItem() {
        monitor-enter(this);
        MediaItem r0 = this.mediaItem;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    /* JADX INFO: renamed from: getMediaPeriodIdForChildMediaPeriodId, reason: avoid collision after fix types in other method */
    protected MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId2(Integer r6, MediaSource.MediaPeriodId r7) {
        int r0 = getChildIndexFromChildWindowSequenceNumber(r7.windowSequenceNumber, this.mediaSourceHolders.size());
        if (r6.intValue() == r0) goto L6;
        return null;
    L6:
        long r1 = getWindowSequenceNumberFromChildWindowSequenceNumber(r7.windowSequenceNumber, this.mediaSourceHolders.size());
        return r7.copyWithPeriodUid(getPeriodUid(r6.intValue(), r7.periodUid)).copyWithWindowSequenceNumber(r1);
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(Integer r1, MediaSource.MediaPeriodId r2) {
        return getMediaPeriodIdForChildMediaPeriodId2(r1, r2);
    }

    /* JADX INFO: renamed from: getMediaTimeForChildMediaTime, reason: avoid collision after fix types in other method */
    protected long getMediaTimeForChildMediaTime2(Integer r4, long r5, MediaSource.MediaPeriodId r7) {
        if (r5 == C0180C.TIME_UNSET) goto L13;
        if (r7 == null) goto L13;
        if (r7.isAd() == true) goto L13;
        Long r0 = this.mediaSourceHolders.get(r4.intValue()).periodTimeOffsetsByUid.get(r7.periodUid);
        if (r0 != null) goto L12;
        return r5;
    L12:
        return Util.usToMs(r0.longValue()) + r5;
    L13:
        return r5;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ long getMediaTimeForChildMediaTime(Integer r1, long r2, MediaSource.MediaPeriodId r4) {
        return getMediaTimeForChildMediaTime2(r1, r2, r4);
    }

    /* JADX INFO: renamed from: getWindowIndexForChildWindowIndex, reason: avoid collision after fix types in other method */
    protected int getWindowIndexForChildWindowIndex2(Integer r2, int r3) {
        return 0;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ int getWindowIndexForChildWindowIndex(Integer r1, int r2) {
        return getWindowIndexForChildWindowIndex2(r1, r2);
    }

    /* JADX INFO: renamed from: onChildSourceInfoRefreshed, reason: avoid collision after fix types in other method */
    protected void onChildSourceInfoRefreshed2(Integer r1, MediaSource r2, Timeline r3) {
        scheduleTimelineUpdate();
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ void onChildSourceInfoRefreshed(Integer r1, MediaSource r2, Timeline r3) {
        onChildSourceInfoRefreshed2(r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    protected void prepareSourceInternal(TransferListener r5) {
        super.prepareSourceInternal(r5);
        this.playbackThreadHandler = new Handler(new ConcatenatingMediaSource2$$ExternalSyntheticLambda0(this));
        int r0 = 0;
    L4:
        if (r0 >= this.mediaSourceHolders.size()) goto L6;
        MediaSourceHolder r1 = this.mediaSourceHolders.get(r0);
        prepareChildSource(Integer.valueOf(r0), r1.mediaSource);
        r0 = r0 + 1;
        goto L4
    L6:
        scheduleTimelineUpdate();
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r4) {
        MediaSourceHolder r0 = (MediaSourceHolder) Assertions.checkNotNull(this.mediaSourceByMediaPeriod.remove(r4));
        r0.mediaSource.releasePeriod(((TimeOffsetMediaPeriod) r4).getWrappedMediaPeriod());
        r0.activeMediaPeriods--;
        if (this.mediaSourceByMediaPeriod.isEmpty() == true) goto L6;
        disableUnusedMediaSources();
        return;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    protected void releaseSourceInternal() {
        super.releaseSourceInternal();
        if (this.playbackThreadHandler == null) goto L5;
        this.playbackThreadHandler.removeCallbacksAndMessages(null);
        this.playbackThreadHandler = null;
    L5:
        this.timelineUpdateScheduled = false;
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public synchronized void updateMediaItem(MediaItem r1) {
        monitor-enter(this);
        this.mediaItem = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }
}
