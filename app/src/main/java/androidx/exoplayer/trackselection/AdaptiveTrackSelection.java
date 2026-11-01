package androidx.media3.exoplayer.trackselection;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunkIterator;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AdaptiveTrackSelection extends BaseTrackSelection {
    public static final float DEFAULT_BANDWIDTH_FRACTION = 0.7f;
    public static final float DEFAULT_BUFFERED_FRACTION_TO_LIVE_EDGE_FOR_QUALITY_INCREASE = 0.75f;
    public static final int DEFAULT_MAX_DURATION_FOR_QUALITY_DECREASE_MS = 25000;
    public static final int DEFAULT_MAX_HEIGHT_TO_DISCARD = 719;
    public static final int DEFAULT_MAX_WIDTH_TO_DISCARD = 1279;
    public static final int DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS = 10000;
    public static final int DEFAULT_MIN_DURATION_TO_RETAIN_AFTER_DISCARD_MS = 25000;
    private static final long MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS = 1000;
    private static final String TAG = "AdaptiveTrackSelection";
    private final ImmutableList<AdaptationCheckpoint> adaptationCheckpoints;
    private final float bandwidthFraction;
    private final BandwidthMeter bandwidthMeter;
    private final float bufferedFractionToLiveEdgeForQualityIncrease;
    private final Clock clock;
    private MediaChunk lastBufferEvaluationMediaChunk;
    private long lastBufferEvaluationMs;
    private long latestBitrateEstimate;
    private final long maxDurationForQualityDecreaseUs;
    private final int maxHeightToDiscard;
    private final int maxWidthToDiscard;
    private final long minDurationForQualityIncreaseUs;
    private final long minDurationToRetainAfterDiscardUs;
    private float playbackSpeed;
    private int reason;
    private int selectedIndex;

    public static final class AdaptationCheckpoint {
        public final long allocatedBandwidth;
        public final long totalBandwidth;

        public AdaptationCheckpoint(long r1, long r3) {
            this.totalBandwidth = r1;
            this.allocatedBandwidth = r3;
        }

        public boolean equals(Object r8) {
            if (this != r8) goto L6;
            return true;
        L6:
            if ((r8 instanceof AdaptationCheckpoint) == true) goto L8;
            return false;
        L8:
            AdaptationCheckpoint r1 = (AdaptationCheckpoint) r8;
            if (this.totalBandwidth != r1.totalBandwidth) goto L14;
            if (this.allocatedBandwidth != r1.allocatedBandwidth) goto L14;
            return true;
        L14:
            return false;
        }

        public int hashCode() {
            return (((int) this.totalBandwidth) * 31) + ((int) this.allocatedBandwidth);
        }
    }

    public static class Factory implements ExoTrackSelection.Factory {
        private final float bandwidthFraction;
        private final float bufferedFractionToLiveEdgeForQualityIncrease;
        private final Clock clock;
        private final int maxDurationForQualityDecreaseMs;
        private final int maxHeightToDiscard;
        private final int maxWidthToDiscard;
        private final int minDurationForQualityIncreaseMs;
        private final int minDurationToRetainAfterDiscardMs;

        public Factory() {
            this(10000, 25000, 25000, 0.7f);
        }

        public Factory(int r10, int r11, int r12, float r13) {
            this(r10, r11, r12, AdaptiveTrackSelection.DEFAULT_MAX_WIDTH_TO_DISCARD, AdaptiveTrackSelection.DEFAULT_MAX_HEIGHT_TO_DISCARD, r13, 0.75f, Clock.DEFAULT);
        }

        public Factory(int r10, int r11, int r12, float r13, float r14, Clock r15) {
            this(r10, r11, r12, AdaptiveTrackSelection.DEFAULT_MAX_WIDTH_TO_DISCARD, AdaptiveTrackSelection.DEFAULT_MAX_HEIGHT_TO_DISCARD, r13, r14, r15);
        }

        public Factory(int r10, int r11, int r12, int r13, int r14, float r15) {
            this(r10, r11, r12, r13, r14, r15, 0.75f, Clock.DEFAULT);
        }

        public Factory(int r1, int r2, int r3, int r4, int r5, float r6, float r7, Clock r8) {
            this.minDurationForQualityIncreaseMs = r1;
            this.maxDurationForQualityDecreaseMs = r2;
            this.minDurationToRetainAfterDiscardMs = r3;
            this.maxWidthToDiscard = r4;
            this.maxHeightToDiscard = r5;
            this.bandwidthFraction = r6;
            this.bufferedFractionToLiveEdgeForQualityIncrease = r7;
            this.clock = r8;
        }

        protected AdaptiveTrackSelection createAdaptiveTrackSelection(TrackGroup r19, int[] r20, int r21, BandwidthMeter r22, ImmutableList<AdaptationCheckpoint> r23) {
            return new AdaptiveTrackSelection(r19, r20, r21, r22, this.minDurationForQualityIncreaseMs, this.maxDurationForQualityDecreaseMs, this.minDurationToRetainAfterDiscardMs, this.maxWidthToDiscard, this.maxHeightToDiscard, this.bandwidthFraction, this.bufferedFractionToLiveEdgeForQualityIncrease, r23, this.clock);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection.Factory
        public final ExoTrackSelection[] createTrackSelections(ExoTrackSelection.Definition[] r16, BandwidthMeter r17, MediaSource.MediaPeriodId r18, Timeline r19) {
            ImmutableList r1 = AdaptiveTrackSelection.access$000(r16);
            ExoTrackSelection[] r2 = new ExoTrackSelection[r16.length];
            int r3 = 0;
        L4:
            if (r3 >= r16.length) goto L16;
            ExoTrackSelection.Definition r4 = r16[r3];
            if (r4 == null) goto L15;
            if (r4.tracks.length == 0) goto L15;
            if (r4.tracks.length != 1) goto L13;
            ExoTrackSelection r5 = new FixedTrackSelection(r4.group, r4.tracks[0], r4.type);
        L14:
            r2[r3] = r5;
            goto L15
        L13:
            r5 = createAdaptiveTrackSelection(r4.group, r4.tracks, r4.type, r17, (ImmutableList) r1.get(r3));
        L15:
            r3 = r3 + 1;
            goto L4
        L16:
            return r2;
        }
    }

    protected AdaptiveTrackSelection(TrackGroup r10, int[] r11, int r12, BandwidthMeter r13, long r14, long r16, long r18, int r20, int r21, float r22, float r23, List<AdaptationCheckpoint> r24, Clock r25) {
        super(r10, r11, r12);
        if (r18 >= r14) goto L5;
        Log.m48w(TAG, "Adjusting minDurationToRetainAfterDiscardMs to be at least minDurationForQualityIncreaseMs");
        long r0 = r14;
    L6:
        this.bandwidthMeter = r13;
        this.minDurationForQualityIncreaseUs = r14 * 1000;
        this.maxDurationForQualityDecreaseUs = r16 * 1000;
        this.minDurationToRetainAfterDiscardUs = 1000 * r0;
        this.maxWidthToDiscard = r20;
        this.maxHeightToDiscard = r21;
        this.bandwidthFraction = r22;
        this.bufferedFractionToLiveEdgeForQualityIncrease = r23;
        this.adaptationCheckpoints = ImmutableList.copyOf(r24);
        this.clock = r25;
        this.playbackSpeed = 1.0f;
        this.reason = 0;
        this.lastBufferEvaluationMs = C0180C.TIME_UNSET;
        this.latestBitrateEstimate = -2147483647L;
        return;
    L5:
        r0 = r18;
        goto L6
    }

    public AdaptiveTrackSelection(TrackGroup r18, int[] r19, BandwidthMeter r20) {
        this(r18, r19, 0, r20, Renderer.DEFAULT_DURATION_TO_PROGRESS_US, 25000, 25000, DEFAULT_MAX_WIDTH_TO_DISCARD, DEFAULT_MAX_HEIGHT_TO_DISCARD, 0.7f, 0.75f, ImmutableList.m212of(), Clock.DEFAULT);
    }

    static /* synthetic */ ImmutableList access$000(ExoTrackSelection.Definition[] r1) {
        return getAdaptationCheckpoints(r1);
    }

    private static void addCheckpoint(List<ImmutableList.Builder<AdaptationCheckpoint>> r7, long[] r8) {
        long r0 = 0;
        int r2 = 0;
    L4:
        if (r2 >= r8.length) goto L6;
        r0 = r0 + r8[r2];
        r2 = r2 + 1;
        goto L4
    L6:
        int r3 = 0;
    L8:
        if (r3 >= r7.size()) goto L14;
        ImmutableList.Builder<AdaptationCheckpoint> r4 = r7.get(r3);
        if (r4 == null) goto L13;
        r4.add(new AdaptationCheckpoint(r0, r8[r3]));
    L13:
        r3 = r3 + 1;
        goto L8
    }

    private int determineIdealSelectedIndex(long r7, long r9) {
        long r0 = getAllocatedBandwidth(r9);
        int r2 = 0;
        int r3 = 0;
    L4:
        if (r3 >= this.length) goto L14;
        if (r7 != Long.MIN_VALUE) goto L8;
    L9:
        Format r4 = getFormat(r3);
        if (canSelectFormat(r4, r4.bitrate, r0) == true) goto L11;
        r2 = r3;
    L13:
        r3 = r3 + 1;
        goto L4
    L11:
        return r3;
    L8:
        if (isTrackExcluded(r3, r7) == true) goto L13;
    L14:
        return r2;
    }

    private static ImmutableList<ImmutableList<AdaptationCheckpoint>> getAdaptationCheckpoints(ExoTrackSelection.Definition[] r12) {
        ArrayList r0 = new ArrayList();
        int r1 = 0;
    L4:
        if (r1 >= r12.length) goto L12;
        if (r12[r1] != null) goto L8;
    L10:
        r0.add(null);
    L11:
        r1 = r1 + 1;
        goto L4
    L8:
        if (r12[r1].tracks.length <= 1) goto L10;
        ImmutableList.Builder r2 = ImmutableList.builder();
        r2.add(new AdaptationCheckpoint(0, 0));
        r0.add(r2);
        goto L11
    L12:
        long[][] r3 = getSortedTrackBitrates(r12);
        int[] r4 = new int[r3.length];
        long[] r6 = new long[r3.length];
        int r7 = 0;
    L14:
        if (r7 >= r3.length) goto L20;
        if (r3[r7].length != 0) goto L18;
        long r9 = 0;
    L19:
        r6[r7] = r9;
        r7 = r7 + 1;
        goto L14
    L18:
        r9 = r3[r7][0];
        goto L19
    L20:
        addCheckpoint(r0, r6);
        ImmutableList<Integer> r5 = getSwitchOrder(r3);
        int r8 = 0;
    L22:
        if (r8 >= r5.size()) goto L24;
        int r10 = r5.get(r8).intValue();
        int r11 = r4[r10] + 1;
        r4[r10] = r11;
        r6[r10] = r3[r10][r11];
        addCheckpoint(r0, r6);
        r8 = r8 + 1;
        goto L22
    L24:
        int r13 = 0;
    L26:
        if (r13 >= r12.length) goto L31;
        if (r0.get(r13) == null) goto L30;
        r6[r13] = r6[r13] * 2;
    L30:
        r13 = r13 + 1;
        goto L26
    L31:
        addCheckpoint(r0, r6);
        ImmutableList.Builder r14 = ImmutableList.builder();
        int r15 = 0;
    L33:
        if (r15 >= r0.size()) goto L40;
        ImmutableList.Builder r16 = (ImmutableList.Builder) r0.get(r15);
        if (r16 != null) goto L37;
        ImmutableList r17 = ImmutableList.m212of();
    L38:
        r14.add(r17);
        r15 = r15 + 1;
        goto L33
    L37:
        r17 = r16.build();
        goto L38
    L40:
        return r14.build();
    }

    private long getAllocatedBandwidth(long r13) {
        long r0 = getTotalAllocatableBandwidth(r13);
        if (this.adaptationCheckpoints.isEmpty() == false) goto L5;
        return r0;
    L5:
        int r2 = 1;
    L7:
        if (r2 >= (this.adaptationCheckpoints.size() - 1)) goto L11;
        if (this.adaptationCheckpoints.get(r2).totalBandwidth >= r0) goto L11;
        r2 = r2 + 1;
    L11:
        AdaptationCheckpoint r3 = this.adaptationCheckpoints.get(r2 - 1);
        AdaptationCheckpoint r4 = this.adaptationCheckpoints.get(r2);
        return r3.allocatedBandwidth + ((long) ((r4.allocatedBandwidth - r3.allocatedBandwidth) * ((r0 - r3.totalBandwidth) / (r4.totalBandwidth - r3.totalBandwidth))));
    }

    private long getLastChunkDurationUs(List<? extends MediaChunk> r6) {
        if (r6.isEmpty() == false) goto L5;
        return C0180C.TIME_UNSET;
    L5:
        MediaChunk r0 = (MediaChunk) Iterables.getLast(r6);
        if (r0.startTimeUs != C0180C.TIME_UNSET) goto L8;
        return C0180C.TIME_UNSET;
    L8:
        if (r0.endTimeUs != C0180C.TIME_UNSET) goto L11;
        return C0180C.TIME_UNSET;
    L11:
        return r0.endTimeUs - r0.startTimeUs;
    }

    private long getNextChunkDurationUs(MediaChunkIterator[] r6, List<? extends MediaChunk> r7) {
        if (this.selectedIndex < r6.length) goto L5;
    L8:
        int r0 = r6.length;
        int r1 = 0;
    L9:
        if (r1 >= r0) goto L16;
        MediaChunkIterator r2 = r6[r1];
        if (r2.next() == true) goto L13;
        r1 = r1 + 1;
        goto L9
    L13:
        return r2.getChunkEndTimeUs() - r2.getChunkStartTimeUs();
    L16:
        return getLastChunkDurationUs(r7);
    L5:
        if (r6[this.selectedIndex].next() == false) goto L8;
        MediaChunkIterator r3 = r6[this.selectedIndex];
        return r3.getChunkEndTimeUs() - r3.getChunkStartTimeUs();
    }

    private static long[][] getSortedTrackBitrates(ExoTrackSelection.Definition[] r9) {
        long[][] r0 = new long[r9.length][];
        int r1 = 0;
    L4:
        if (r1 >= r9.length) goto L18;
        ExoTrackSelection.Definition r2 = r9[r1];
        if (r2 != null) goto L8;
        r0[r1] = new long[0];
    L17:
        r1 = r1 + 1;
        goto L4
    L8:
        r0[r1] = new long[r2.tracks.length];
        int r3 = 0;
    L10:
        if (r3 >= r2.tracks.length) goto L16;
        long r4 = r2.group.getFormat(r2.tracks[r3]).bitrate;
        long[] r6 = r0[r1];
        if (r4 != (-1)) goto L14;
        long r7 = 0;
    L15:
        r6[r3] = r7;
        r3 = r3 + 1;
        goto L10
    L14:
        r7 = r4;
        goto L15
    L16:
        Arrays.sort(r0[r1]);
        goto L17
    L18:
        return r0;
    }

    private static ImmutableList<Integer> getSwitchOrder(long[][] r16) {
        Multimap r1 = MultimapBuilder.treeKeys().arrayListValues().build();
        int r2 = 0;
    L4:
        if (r2 >= r16.length) goto L26;
        int r4 = 1;
        if (r16[r2].length <= 1) goto L24;
        double[] r3 = new double[r16[r2].length];
        int r5 = 0;
    L9:
        double r7 = 0.0d;
        if (r5 >= r16[r2].length) goto L16;
        if (r16[r2][r5] == (-1)) goto L15;
        r7 = Math.log(r16[r2][r5]);
    L15:
        r3[r5] = r7;
        r5 = r5 + 1;
        goto L9
    L16:
        double r6 = r3[r3.length - 1] - r3[0];
        int r10 = 0;
    L18:
        if (r10 >= (r3.length - r4)) goto L24;
        double r11 = (r3[r10] + r3[r10 + 1]) * 0.5d;
        if (r6 != 0.0d) goto L22;
        double r13 = 1.0d;
    L23:
        r1.put(Double.valueOf(r13), Integer.valueOf(r2));
        r10 = r10 + 1;
        r4 = 1;
        goto L18
    L22:
        r13 = (r11 - r3[0]) / r6;
    L24:
        r2 = r2 + 1;
        goto L4
    L26:
        return ImmutableList.copyOf(r1.values());
    }

    private long getTotalAllocatableBandwidth(long r8) {
        this.latestBitrateEstimate = this.bandwidthMeter.getBitrateEstimate();
        long r0 = (long) (this.latestBitrateEstimate * this.bandwidthFraction);
        long r2 = this.bandwidthMeter.getTimeToFirstByteEstimateUs();
        if (r2 == C0180C.TIME_UNSET) goto L10;
        if (r8 == C0180C.TIME_UNSET) goto L10;
        return (long) ((r0 * Math.max((r8 / this.playbackSpeed) - r2, 0.0f)) / r8);
    L10:
        return (long) (r0 / this.playbackSpeed);
    }

    private long minDurationForQualityIncreaseUs(long r5, long r7) {
        if (r5 != C0180C.TIME_UNSET) goto L7;
        return this.minDurationForQualityIncreaseUs;
    L7:
        if (r7 == C0180C.TIME_UNSET) goto L10;
        r5 = r5 - r7;
    L10:
        return Math.min((long) (r5 * this.bufferedFractionToLiveEdgeForQualityIncrease), this.minDurationForQualityIncreaseUs);
    }

    protected boolean canSelectFormat(Format r3, int r4, long r5) {
        if (r4 > r5) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.exoplayer.trackselection.BaseTrackSelection, androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public void disable() {
        this.lastBufferEvaluationMediaChunk = null;
    }

    @Override // androidx.media3.exoplayer.trackselection.BaseTrackSelection, androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public void enable() {
        this.lastBufferEvaluationMs = C0180C.TIME_UNSET;
        this.lastBufferEvaluationMediaChunk = null;
    }

    @Override // androidx.media3.exoplayer.trackselection.BaseTrackSelection, androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public int evaluateQueueSize(long r22, List<? extends MediaChunk> r24) {
        List<? extends MediaChunk> r1 = r24;
        long r2 = this.clock.elapsedRealtime();
        if (shouldEvaluateQueueSize(r2, r1) == false) goto L5;
        this.lastBufferEvaluationMs = r2;
        if (r1.isEmpty() == false) goto L9;
        MediaChunk r4 = null;
    L10:
        this.lastBufferEvaluationMediaChunk = r4;
        if (r1.isEmpty() == false) goto L14;
        return 0;
    L14:
        int r5 = r1.size();
        long r6 = Util.getPlayoutDurationForMediaDuration(r1.get(r5 - 1).startTimeUs - r22, this.playbackSpeed);
        long r8 = getMinDurationToRetainAfterDiscardUs();
        if (r6 >= r8) goto L17;
        return r5;
    L17:
        Format r11 = getFormat(determineIdealSelectedIndex(r2, getLastChunkDurationUs(r1)));
        int r12 = 0;
    L18:
        if (r12 >= r5) goto L36;
        MediaChunk r13 = r1.get(r12);
        Format r14 = r13.trackFormat;
        long r15 = r2;
        if (Util.getPlayoutDurationForMediaDuration(r13.startTimeUs - r22, this.playbackSpeed) < r8) goto L35;
        if (r14.bitrate >= r11.bitrate) goto L35;
        if (r14.height == (-1)) goto L35;
        if (r14.height > this.maxHeightToDiscard) goto L35;
        if (r14.width == (-1)) goto L35;
        if (r14.width > this.maxWidthToDiscard) goto L35;
        if (r14.height >= r11.height) goto L35;
        return r12;
    L35:
        r12 = r12 + 1;
        r1 = r24;
        r2 = r15;
        goto L18
    L36:
        return r5;
    L9:
        r4 = (MediaChunk) Iterables.getLast(r1);
        goto L10
    L5:
        return r1.size();
    }

    @Override // androidx.media3.exoplayer.trackselection.BaseTrackSelection, androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public long getLatestBitrateEstimate() {
        return this.latestBitrateEstimate;
    }

    protected long getMinDurationToRetainAfterDiscardUs() {
        return this.minDurationToRetainAfterDiscardUs;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public Object getSelectionData() {
        return null;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public int getSelectionReason() {
        return this.reason;
    }

    @Override // androidx.media3.exoplayer.trackselection.BaseTrackSelection, androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public void onPlaybackSpeed(float r1) {
        this.playbackSpeed = r1;
    }

    protected boolean shouldEvaluateQueueSize(long r5, List<? extends MediaChunk> r7) {
        if (this.lastBufferEvaluationMs != C0180C.TIME_UNSET) goto L5;
    L12:
        return true;
    L5:
        if ((r5 - this.lastBufferEvaluationMs) >= 1000) goto L12;
        if (r7.isEmpty() == false) goto L9;
    L11:
        return false;
    L9:
        if (((MediaChunk) Iterables.getLast(r7)).equals(this.lastBufferEvaluationMediaChunk) == true) goto L11;
        goto L11
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public void updateSelectedTrack(long r20, long r22, long r24, List<? extends MediaChunk> r26, MediaChunkIterator[] r27) {
        long r1 = this.clock.elapsedRealtime();
        long r5 = getNextChunkDurationUs(r27, r26);
        if (this.reason != 0) goto L6;
        this.reason = 1;
        this.selectedIndex = determineIdealSelectedIndex(r1, r5);
        return;
    L6:
        int r7 = this.selectedIndex;
        int r8 = this.reason;
        if (r26.isEmpty() == false) goto L9;
        int r9 = -1;
    L10:
        if (r9 == (-1)) goto L12;
        r7 = r9;
        r8 = ((MediaChunk) Iterables.getLast(r26)).trackSelectionReason;
    L12:
        int r10 = determineIdealSelectedIndex(r1, r5);
        if (r10 == r7) goto L29;
        if (isTrackExcluded(r7, r1) == true) goto L29;
        Format r11 = getFormat(r7);
        Format r12 = getFormat(r10);
        long r15 = minDurationForQualityIncreaseUs(r24, r5);
        if (r12.bitrate <= r11.bitrate) goto L22;
        if (r22 >= r15) goto L22;
        r10 = r7;
    L22:
        if (r12.bitrate >= r11.bitrate) goto L29;
        if (r22 < this.maxDurationForQualityDecreaseUs) goto L29;
        r10 = r7;
    L29:
        if (r10 != r7) goto L31;
        int r2 = r8;
    L32:
        this.reason = r2;
        this.selectedIndex = r10;
        return;
    L31:
        r2 = 3;
        goto L32
    L9:
        r9 = indexOf(((MediaChunk) Iterables.getLast(r26)).trackFormat);
        goto L10
    }
}
