package androidx.media3.exoplayer.source;

import android.util.Pair;
import androidx.media3.common.AdPlaybackState;
import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.upstream.Allocator;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class MaskingMediaSource extends WrappingMediaSource {
    private boolean hasRealTimeline;
    private boolean hasStartedPreparing;
    private boolean isPrepared;
    private final Timeline.Period period;
    private MaskingTimeline timeline;
    private MaskingMediaPeriod unpreparedMaskingMediaPeriod;
    private final boolean useLazyPreparation;
    private final Timeline.Window window;

    private static final class MaskingTimeline extends ForwardingTimeline {
        public static final Object MASKING_EXTERNAL_PERIOD_UID = null;
        private final Object replacedInternalPeriodUid;
        private final Object replacedInternalWindowUid;

        static {
            MASKING_EXTERNAL_PERIOD_UID = new Object();
        }

        private MaskingTimeline(Timeline r1, Object r2, Object r3) {
            super(r1);
            this.replacedInternalWindowUid = r2;
            this.replacedInternalPeriodUid = r3;
        }

        static /* synthetic */ Object access$000(MaskingTimeline r1) {
            return r1.replacedInternalPeriodUid;
        }

        public static MaskingTimeline createWithPlaceholderTimeline(MediaItem r4) {
            return new MaskingTimeline(new PlaceholderTimeline(r4), Timeline.Window.SINGLE_WINDOW_UID, MASKING_EXTERNAL_PERIOD_UID);
        }

        public static MaskingTimeline createWithRealTimeline(Timeline r1, Object r2, Object r3) {
            return new MaskingTimeline(r1, r2, r3);
        }

        public MaskingTimeline cloneWithUpdatedTimeline(Timeline r4) {
            return new MaskingTimeline(r4, this.replacedInternalWindowUid, this.replacedInternalPeriodUid);
        }

        @Override // androidx.media3.exoplayer.source.ForwardingTimeline, androidx.media3.common.Timeline
        public int getIndexOfPeriod(Object r3) {
            Timeline r0 = this.timeline;
            if (MASKING_EXTERNAL_PERIOD_UID.equals(r3) == true) goto L5;
        L7:
            Object r1 = r3;
        L9:
            return r0.getIndexOfPeriod(r1);
        L5:
            if (this.replacedInternalPeriodUid == null) goto L7;
            r1 = this.replacedInternalPeriodUid;
            goto L9
        }

        @Override // androidx.media3.exoplayer.source.ForwardingTimeline, androidx.media3.common.Timeline
        public Timeline.Period getPeriod(int r3, Timeline.Period r4, boolean r5) {
            this.timeline.getPeriod(r3, r4, r5);
            if (Util.areEqual(r4.uid, this.replacedInternalPeriodUid) == false) goto L6;
            if (r5 == false) goto L6;
            r4.uid = MASKING_EXTERNAL_PERIOD_UID;
        L6:
            return r4;
        }

        @Override // androidx.media3.exoplayer.source.ForwardingTimeline, androidx.media3.common.Timeline
        public Object getUidOfPeriod(int r3) {
            Object r0 = this.timeline.getUidOfPeriod(r3);
            if (Util.areEqual(r0, this.replacedInternalPeriodUid) == false) goto L6;
            return MASKING_EXTERNAL_PERIOD_UID;
        L6:
            return r0;
        }

        @Override // androidx.media3.exoplayer.source.ForwardingTimeline, androidx.media3.common.Timeline
        public Timeline.Window getWindow(int r3, Timeline.Window r4, long r5) {
            this.timeline.getWindow(r3, r4, r5);
            if (Util.areEqual(r4.uid, this.replacedInternalWindowUid) == false) goto L5;
            r4.uid = Timeline.Window.SINGLE_WINDOW_UID;
        L5:
            return r4;
        }
    }

    public static final class PlaceholderTimeline extends Timeline {
        private final MediaItem mediaItem;

        public PlaceholderTimeline(MediaItem r1) {
            this.mediaItem = r1;
        }

        @Override // androidx.media3.common.Timeline
        public int getIndexOfPeriod(Object r2) {
            if (r2 != MaskingTimeline.MASKING_EXTERNAL_PERIOD_UID) goto L5;
            return 0;
        L5:
            return -1;
        }

        @Override // androidx.media3.common.Timeline
        public Timeline.Period getPeriod(int r13, Timeline.Period r14, boolean r15) {
            Object r0 = null;
            if (r15 == false) goto L5;
            Integer r3 = 0;
        L6:
            if (r15 == false) goto L8;
            r0 = MaskingTimeline.MASKING_EXTERNAL_PERIOD_UID;
        L8:
            r14.set(r3, r0, 0, C0180C.TIME_UNSET, 0, AdPlaybackState.NONE, true);
            return r14;
        L5:
            r3 = null;
            goto L6
        }

        @Override // androidx.media3.common.Timeline
        public int getPeriodCount() {
            return 1;
        }

        @Override // androidx.media3.common.Timeline
        public Object getUidOfPeriod(int r2) {
            return MaskingTimeline.MASKING_EXTERNAL_PERIOD_UID;
        }

        @Override // androidx.media3.common.Timeline
        public Timeline.Window getWindow(int r22, Timeline.Window r23, long r24) {
            r23.set(Timeline.Window.SINGLE_WINDOW_UID, this.mediaItem, null, C0180C.TIME_UNSET, C0180C.TIME_UNSET, C0180C.TIME_UNSET, false, true, null, 0, C0180C.TIME_UNSET, 0, 0, 0);
            r23.isPlaceholder = true;
            return r23;
        }

        @Override // androidx.media3.common.Timeline
        public int getWindowCount() {
            return 1;
        }
    }

    public MaskingMediaSource(MediaSource r4, boolean r5) {
        super(r4);
        if (r5 == true) goto L5;
    L7:
        boolean r1 = false;
    L8:
        this.useLazyPreparation = r1;
        this.window = new Timeline.Window();
        this.period = new Timeline.Period();
        Timeline r2 = r4.getInitialTimeline();
        if (r2 == null) goto L11;
        this.timeline = MaskingTimeline.createWithRealTimeline(r2, null, null);
        this.hasRealTimeline = true;
        return;
    L11:
        this.timeline = MaskingTimeline.createWithPlaceholderTimeline(r4.getMediaItem());
        return;
    L5:
        if (r4.isSingleWindow() == false) goto L7;
        r1 = true;
        goto L8
    }

    private Object getExternalPeriodUid(Object r2) {
        if (MaskingTimeline.access$000(this.timeline) == null) goto L8;
        if (MaskingTimeline.access$000(this.timeline).equals(r2) == false) goto L8;
        return MaskingTimeline.MASKING_EXTERNAL_PERIOD_UID;
    L8:
        return r2;
    }

    private Object getInternalPeriodUid(Object r2) {
        if (MaskingTimeline.access$000(this.timeline) == null) goto L8;
        if (r2.equals(MaskingTimeline.MASKING_EXTERNAL_PERIOD_UID) == false) goto L8;
        return MaskingTimeline.access$000(this.timeline);
    L8:
        return r2;
    }

    @RequiresNonNull({"unpreparedMaskingMediaPeriod"})
    private boolean setPreparePositionOverrideToUnpreparedMaskingPeriod(long r9) {
        MaskingMediaPeriod r0 = this.unpreparedMaskingMediaPeriod;
        int r1 = this.timeline.getIndexOfPeriod(r0.f44id.periodUid);
        if (r1 != (-1)) goto L6;
        return false;
    L6:
        long r2 = this.timeline.getPeriod(r1, this.period).durationUs;
        if (r2 != C0180C.TIME_UNSET) goto L9;
    L11:
        r0.overridePreparePositionUs(r9);
        return true;
    L9:
        if (r9 < r2) goto L11;
        r9 = Math.max(0, r2 - 1);
        goto L11
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean canUpdateMediaItem(MediaItem r2) {
        return this.mediaSource.canUpdateMediaItem(r2);
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.MediaSource
    public MaskingMediaPeriod createPeriod(MediaSource.MediaPeriodId r3, Allocator r4, long r5) {
        MaskingMediaPeriod r0 = new MaskingMediaPeriod(r3, r4, r5);
        r0.setMediaSource(this.mediaSource);
        if (this.isPrepared == false) goto L5;
        r0.createPeriod(r3.copyWithPeriodUid(getInternalPeriodUid(r3.periodUid)));
    L8:
        return r0;
    L5:
        this.unpreparedMaskingMediaPeriod = r0;
        if (this.hasStartedPreparing == true) goto L8;
        this.hasStartedPreparing = true;
        prepareChildSource();
        goto L8
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.MediaSource
    public /* bridge */ /* synthetic */ MediaPeriod createPeriod(MediaSource.MediaPeriodId r1, Allocator r2, long r3) {
        return createPeriod(r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource
    protected MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(MediaSource.MediaPeriodId r2) {
        return r2.copyWithPeriodUid(getExternalPeriodUid(r2.periodUid));
    }

    public Timeline getTimeline() {
        return this.timeline;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() {
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource
    protected void onChildSourceInfoRefreshed(Timeline r13) {
        MediaSource.MediaPeriodId r6 = null;
        if (this.isPrepared == false) goto L8;
        this.timeline = this.timeline.cloneWithUpdatedTimeline(r13);
        if (this.unpreparedMaskingMediaPeriod == null) goto L29;
        setPreparePositionOverrideToUnpreparedMaskingPeriod(this.unpreparedMaskingMediaPeriod.getPreparePositionOverrideUs());
    L29:
        this.hasRealTimeline = true;
        this.isPrepared = true;
        refreshSourceInfo(this.timeline);
        if (r6 == null) goto L33;
        ((MaskingMediaPeriod) Assertions.checkNotNull(this.unpreparedMaskingMediaPeriod)).createPeriod(r6);
        return;
    L33:
        return;
    L8:
        if (r13.isEmpty() == true) goto L10;
        r13.getWindow(0, this.window);
        long r3 = this.window.getDefaultPositionUs();
        Object r7 = this.window.uid;
        if (this.unpreparedMaskingMediaPeriod == null) goto L19;
        long r8 = this.unpreparedMaskingMediaPeriod.getPreparePositionUs();
        this.timeline.getPeriodByUid(this.unpreparedMaskingMediaPeriod.f44id.periodUid, this.period);
        long r10 = this.period.getPositionInWindowUs() + r8;
        if (r10 == this.timeline.getWindow(0, this.window).getDefaultPositionUs()) goto L19;
        long r4 = r10;
    L20:
        Pair<Object, Long> r1 = r13.getPeriodPositionUs(this.window, this.period, 0, r4);
        Object r2 = r1.first;
        long r9 = ((Long) r1.second).longValue();
        if (this.hasRealTimeline == false) goto L23;
        MaskingTimeline r5 = this.timeline.cloneWithUpdatedTimeline(r13);
    L24:
        this.timeline = r5;
        if (this.unpreparedMaskingMediaPeriod == null) goto L29;
        MaskingMediaPeriod r11 = this.unpreparedMaskingMediaPeriod;
        if (setPreparePositionOverrideToUnpreparedMaskingPeriod(r9) == false) goto L29;
        r6 = r11.f44id.copyWithPeriodUid(getInternalPeriodUid(r11.f44id.periodUid));
        goto L29
    L23:
        r5 = MaskingTimeline.createWithRealTimeline(r13, r7, r2);
    L19:
        r4 = r3;
        goto L20
    L10:
        if (this.hasRealTimeline == false) goto L12;
        MaskingTimeline r12 = this.timeline.cloneWithUpdatedTimeline(r13);
    L13:
        this.timeline = r12;
        goto L29
    L12:
        r12 = MaskingTimeline.createWithRealTimeline(r13, Timeline.Window.SINGLE_WINDOW_UID, MaskingTimeline.MASKING_EXTERNAL_PERIOD_UID);
        goto L13
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource
    public void prepareSourceInternal() {
        if (this.useLazyPreparation == true) goto L6;
        this.hasStartedPreparing = true;
        prepareChildSource();
        return;
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r2) {
        ((MaskingMediaPeriod) r2).releasePeriod();
        if (r2 != this.unpreparedMaskingMediaPeriod) goto L6;
        this.unpreparedMaskingMediaPeriod = null;
        return;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    public void releaseSourceInternal() {
        this.isPrepared = false;
        this.hasStartedPreparing = false;
        super.releaseSourceInternal();
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public void updateMediaItem(MediaItem r4) {
        if (this.hasRealTimeline == false) goto L5;
        this.timeline = this.timeline.cloneWithUpdatedTimeline(new TimelineWithUpdatedMediaItem(this.timeline.timeline, r4));
    L6:
        this.mediaSource.updateMediaItem(r4);
        return;
    L5:
        this.timeline = MaskingTimeline.createWithPlaceholderTimeline(r4);
        goto L6
    }
}
