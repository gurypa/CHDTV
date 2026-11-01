package androidx.media3.exoplayer.source;

import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.upstream.Allocator;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class MergingMediaSource extends CompositeMediaSource<Integer> {
    private static final int PERIOD_COUNT_UNSET = -1;
    private static final MediaItem PLACEHOLDER_MEDIA_ITEM = null;
    private final boolean adjustPeriodTimeOffsets;
    private final boolean clipDurations;
    private final Map<Object, Long> clippedDurationsUs;
    private final Multimap<Object, ClippingMediaPeriod> clippedMediaPeriods;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final List<List<MediaPeriodAndId>> mediaPeriods;
    private final MediaSource[] mediaSources;
    private IllegalMergeException mergeError;
    private final ArrayList<MediaSource> pendingTimelineSources;
    private int periodCount;
    private long[][] periodTimeOffsetsUs;
    private final Timeline[] timelines;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.MergingMediaSource$1 */
    static /* synthetic */ class C03441 {
    }

    private static final class ClippedTimeline extends ForwardingTimeline {
        private final long[] periodDurationsUs;
        private final long[] windowDurationsUs;

        public ClippedTimeline(Timeline r19, Map<Object, Long> r20) {
            Timeline r1 = r19;
            super(r19);
            int r2 = r1.getWindowCount();
            this.windowDurationsUs = new long[r1.getWindowCount()];
            Timeline.Window r3 = new Timeline.Window();
            int r4 = 0;
        L3:
            if (r4 >= r2) goto L5;
            this.windowDurationsUs[r4] = r1.getWindow(r4, r3).durationUs;
            r4 = r4 + 1;
            goto L3
        L5:
            int r5 = r1.getPeriodCount();
            this.periodDurationsUs = new long[r5];
            Timeline.Period r6 = new Timeline.Period();
            int r7 = 0;
        L6:
            if (r7 >= r5) goto L16;
            r1.getPeriod(r7, r6, true);
            long r9 = ((Long) Assertions.checkNotNull(r20.get(r6.uid))).longValue();
            long[] r8 = this.periodDurationsUs;
            if (r9 == Long.MIN_VALUE) goto L10;
            long r11 = r9;
        L11:
            r8[r7] = r11;
            if (r6.durationUs == C0180C.TIME_UNSET) goto L14;
            long[] r10 = this.windowDurationsUs;
            int r12 = r6.windowIndex;
            r10[r12] = r10[r12] - (r6.durationUs - this.periodDurationsUs[r7]);
        L14:
            r7 = r7 + 1;
            r1 = r19;
            goto L6
        L10:
            r11 = r6.durationUs;
            goto L11
        }

        @Override // androidx.media3.exoplayer.source.ForwardingTimeline, androidx.media3.common.Timeline
        public Timeline.Period getPeriod(int r4, Timeline.Period r5, boolean r6) {
            super.getPeriod(r4, r5, r6);
            r5.durationUs = this.periodDurationsUs[r4];
            return r5;
        }

        @Override // androidx.media3.exoplayer.source.ForwardingTimeline, androidx.media3.common.Timeline
        public Timeline.Window getWindow(int r5, Timeline.Window r6, long r7) {
            super.getWindow(r5, r6, r7);
            r6.durationUs = this.windowDurationsUs[r5];
            if (r6.durationUs != C0180C.TIME_UNSET) goto L5;
        L8:
            long r0 = r6.defaultPositionUs;
        L9:
            r6.defaultPositionUs = r0;
            return r6;
        L5:
            if (r6.defaultPositionUs == C0180C.TIME_UNSET) goto L8;
            r0 = Math.min(r6.defaultPositionUs, r6.durationUs);
            goto L9
        }
    }

    public static final class IllegalMergeException extends IOException {
        public static final int REASON_PERIOD_COUNT_MISMATCH = 0;
        public final int reason;

        @Target({ElementType.TYPE_USE})
        @Documented
        @Retention(RetentionPolicy.SOURCE)
        public @interface Reason {
        }

        public IllegalMergeException(int r1) {
            this.reason = r1;
        }
    }

    private static final class MediaPeriodAndId {
        private final MediaPeriod mediaPeriod;
        private final MediaSource.MediaPeriodId mediaPeriodId;

        private MediaPeriodAndId(MediaSource.MediaPeriodId r1, MediaPeriod r2) {
            this.mediaPeriodId = r1;
            this.mediaPeriod = r2;
        }

        /* synthetic */ MediaPeriodAndId(MediaSource.MediaPeriodId r1, MediaPeriod r2, C03441 r3) {
            this(r1, r2);
        }

        static /* synthetic */ MediaPeriod access$100(MediaPeriodAndId r1) {
            return r1.mediaPeriod;
        }

        static /* synthetic */ MediaSource.MediaPeriodId access$200(MediaPeriodAndId r1) {
            return r1.mediaPeriodId;
        }
    }

    static {
        PLACEHOLDER_MEDIA_ITEM = new MediaItem.Builder().setMediaId("MergingMediaSource").build();
    }

    public MergingMediaSource(boolean r4, boolean r5, CompositeSequenceableLoaderFactory r6, MediaSource... r7) {
        this.adjustPeriodTimeOffsets = r4;
        this.clipDurations = r5;
        this.mediaSources = r7;
        this.compositeSequenceableLoaderFactory = r6;
        this.pendingTimelineSources = new ArrayList(Arrays.asList(r7));
        this.periodCount = -1;
        this.mediaPeriods = new ArrayList(r7.length);
        int r0 = 0;
    L4:
        if (r0 >= r7.length) goto L6;
        this.mediaPeriods.add(new ArrayList());
        r0 = r0 + 1;
        goto L4
    L6:
        this.timelines = new Timeline[r7.length];
        this.periodTimeOffsetsUs = new long[0][];
        this.clippedDurationsUs = new HashMap();
        this.clippedMediaPeriods = MultimapBuilder.hashKeys().arrayListValues().build();
    }

    public MergingMediaSource(boolean r2, boolean r3, MediaSource... r4) {
        this(r2, r3, new DefaultCompositeSequenceableLoaderFactory(), r4);
    }

    public MergingMediaSource(boolean r2, MediaSource... r3) {
        this(r2, false, r3);
    }

    public MergingMediaSource(MediaSource... r2) {
        this(false, r2);
    }

    private void computePeriodTimeOffsets() {
        Timeline.Period r0 = new Timeline.Period();
        int r1 = 0;
    L4:
        if (r1 >= this.periodCount) goto L10;
        long r2 = -this.timelines[0].getPeriod(r1, r0).getPositionInWindowUs();
        int r4 = 1;
    L7:
        if (r4 >= this.timelines.length) goto L9;
        long r5 = -this.timelines[r4].getPeriod(r1, r0).getPositionInWindowUs();
        this.periodTimeOffsetsUs[r1][r4] = r2 - r5;
        r4 = r4 + 1;
        goto L7
    L9:
        r1 = r1 + 1;
        goto L4
    }

    private void updateClippedDuration() {
        Timeline.Period r0 = new Timeline.Period();
        int r1 = 0;
    L4:
        if (r1 >= this.periodCount) goto L22;
        long r2 = Long.MIN_VALUE;
        int r4 = 0;
    L7:
        if (r4 >= this.timelines.length) goto L17;
        long r5 = this.timelines[r4].getPeriod(r1, r0).getDurationUs();
        if (r5 == C0180C.TIME_UNSET) goto L16;
        long r8 = this.periodTimeOffsetsUs[r1][r4] + r5;
        if (r2 != Long.MIN_VALUE) goto L14;
    L15:
        r2 = r8;
        goto L16
    L14:
        if (r8 < r2) goto L15;
    L16:
        r4 = r4 + 1;
        goto L7
    L17:
        Object r6 = this.timelines[0].getUidOfPeriod(r1);
        this.clippedDurationsUs.put(r6, Long.valueOf(r2));
        Iterator<ClippingMediaPeriod> r7 = this.clippedMediaPeriods.get(r6).iterator();
    L19:
        if (r7.hasNext() == false) goto L21;
        r7.next().updateClipping(0, r2);
        goto L19
    L21:
        r1 = r1 + 1;
        goto L4
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean canUpdateMediaItem(MediaItem r3) {
        if (this.mediaSources.length > 0) goto L5;
        return false;
    L5:
        if (this.mediaSources[0].canUpdateMediaItem(r3) == false) goto L9;
        return true;
    L9:
        return false;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r14, Allocator r15, long r16) {
        MediaPeriod[] r0 = new MediaPeriod[this.mediaSources.length];
        int r1 = this.timelines[0].getIndexOfPeriod(r14.periodUid);
        int r2 = 0;
    L4:
        if (r2 >= r0.length) goto L6;
        MediaSource.MediaPeriodId r3 = r14.copyWithPeriodUid(this.timelines[r2].getUidOfPeriod(r1));
        r0[r2] = this.mediaSources[r2].createPeriod(r3, r15, r16 - this.periodTimeOffsetsUs[r1][r2]);
        this.mediaPeriods.get(r2).add(new MediaPeriodAndId(r3, r0[r2], null));
        r2 = r2 + 1;
        goto L4
    L6:
        MergingMediaPeriod r4 = new MergingMediaPeriod(this.compositeSequenceableLoaderFactory, this.periodTimeOffsetsUs[r1], r0);
        if (this.clipDurations == false) goto L11;
        ClippingMediaPeriod r6 = new ClippingMediaPeriod(r4, true, 0, ((Long) Assertions.checkNotNull(this.clippedDurationsUs.get(r14.periodUid))).longValue());
        this.clippedMediaPeriods.put(r14.periodUid, r6);
        return r6;
    L11:
        return r4;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaItem getMediaItem() {
        if (this.mediaSources.length <= 0) goto L6;
        return this.mediaSources[0].getMediaItem();
    L6:
        return PLACEHOLDER_MEDIA_ITEM;
    }

    /* JADX INFO: renamed from: getMediaPeriodIdForChildMediaPeriodId, reason: avoid collision after fix types in other method */
    protected MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId2(Integer r5, MediaSource.MediaPeriodId r6) {
        List<MediaPeriodAndId> r0 = this.mediaPeriods.get(r5.intValue());
        int r1 = 0;
    L4:
        if (r1 >= r0.size()) goto L10;
        if (MediaPeriodAndId.access$200(r0.get(r1)).equals(r6) == true) goto L8;
        r1 = r1 + 1;
        goto L4
    L8:
        return MediaPeriodAndId.access$200(this.mediaPeriods.get(0).get(r1));
    L10:
        return null;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(Integer r1, MediaSource.MediaPeriodId r2) {
        return getMediaPeriodIdForChildMediaPeriodId2(r1, r2);
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        if (this.mergeError != null) goto L7;
        super.maybeThrowSourceInfoRefreshError();
        return;
    L7:
        throw this.mergeError;
    }

    /* JADX INFO: renamed from: onChildSourceInfoRefreshed, reason: avoid collision after fix types in other method */
    protected void onChildSourceInfoRefreshed2(Integer r6, MediaSource r7, Timeline r8) {
        if (this.mergeError == null) goto L6;
        return;
    L6:
        if (this.periodCount != (-1)) goto L9;
        this.periodCount = r8.getPeriodCount();
    L13:
        if (this.periodTimeOffsetsUs.length != 0) goto L15;
        this.periodTimeOffsetsUs = (long[][]) Array.newInstance(Long.TYPE, new int[]{this.periodCount, this.timelines.length});
    L15:
        this.pendingTimelineSources.remove(r7);
        this.timelines[r6.intValue()] = r8;
        if (this.pendingTimelineSources.isEmpty() == true) goto L18;
        return;
    L18:
        if (this.adjustPeriodTimeOffsets == false) goto L20;
        computePeriodTimeOffsets();
    L20:
        Timeline r0 = this.timelines[0];
        if (this.clipDurations == false) goto L23;
        updateClippedDuration();
        r0 = new ClippedTimeline(r0, this.clippedDurationsUs);
    L23:
        refreshSourceInfo(r0);
        return;
    L9:
        if (r8.getPeriodCount() == this.periodCount) goto L13;
        this.mergeError = new IllegalMergeException(0);
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ void onChildSourceInfoRefreshed(Integer r1, MediaSource r2, Timeline r3) {
        onChildSourceInfoRefreshed2(r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    protected void prepareSourceInternal(TransferListener r4) {
        super.prepareSourceInternal(r4);
        int r0 = 0;
    L4:
        if (r0 >= this.mediaSources.length) goto L6;
        prepareChildSource(Integer.valueOf(r0), this.mediaSources[r0]);
        r0 = r0 + 1;
        goto L4
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r6) {
        if (this.clipDurations == false) goto L12;
        ClippingMediaPeriod r0 = (ClippingMediaPeriod) r6;
        Iterator<Map.Entry<Object, ClippingMediaPeriod>> r1 = this.clippedMediaPeriods.entries().iterator();
    L6:
        if (r1.hasNext() == false) goto L11;
        Map.Entry<Object, ClippingMediaPeriod> r2 = r1.next();
        if (r2.getValue().equals(r0) == false) goto L6;
        this.clippedMediaPeriods.remove(r2.getKey(), r2.getValue());
    L11:
        r6 = r0.mediaPeriod;
    L12:
        MergingMediaPeriod r3 = (MergingMediaPeriod) r6;
        int r4 = 0;
    L14:
        if (r4 >= this.mediaSources.length) goto L23;
        List<MediaPeriodAndId> r5 = this.mediaPeriods.get(r4);
        int r7 = 0;
    L17:
        if (r7 >= r5.size()) goto L22;
        if (MediaPeriodAndId.access$100(r5.get(r7)).equals(r6) == true) goto L20;
        r7 = r7 + 1;
        goto L17
    L20:
        r5.remove(r7);
    L22:
        this.mediaSources[r4].releasePeriod(r3.getChildPeriod(r4));
        r4 = r4 + 1;
        goto L14
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    protected void releaseSourceInternal() {
        super.releaseSourceInternal();
        Arrays.fill(this.timelines, null);
        this.periodCount = -1;
        this.mergeError = null;
        this.pendingTimelineSources.clear();
        Collections.addAll(this.pendingTimelineSources, this.mediaSources);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public void updateMediaItem(MediaItem r3) {
        this.mediaSources[0].updateMediaItem(r3);
    }
}
