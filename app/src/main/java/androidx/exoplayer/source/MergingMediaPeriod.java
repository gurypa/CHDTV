package androidx.media3.exoplayer.source;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.source.chunk.Chunk;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunkIterator;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class MergingMediaPeriod implements MediaPeriod, MediaPeriod.Callback {
    private MediaPeriod.Callback callback;
    private final HashMap<TrackGroup, TrackGroup> childTrackGroupByMergedTrackGroup;
    private final ArrayList<MediaPeriod> childrenPendingPreparation;
    private SequenceableLoader compositeSequenceableLoader;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private MediaPeriod[] enabledPeriods;
    private final MediaPeriod[] periods;
    private final IdentityHashMap<SampleStream, Integer> streamPeriodIndices;
    private TrackGroupArray trackGroups;

    private static final class ForwardingTrackSelection implements ExoTrackSelection {
        private final TrackGroup trackGroup;
        private final ExoTrackSelection trackSelection;

        public ForwardingTrackSelection(ExoTrackSelection r1, TrackGroup r2) {
            this.trackSelection = r1;
            this.trackGroup = r2;
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public void disable() {
            this.trackSelection.disable();
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public void enable() {
            this.trackSelection.enable();
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if ((r6 instanceof ForwardingTrackSelection) == true) goto L8;
            return false;
        L8:
            ForwardingTrackSelection r1 = (ForwardingTrackSelection) r6;
            if (this.trackSelection.equals(r1.trackSelection) == false) goto L14;
            if (this.trackGroup.equals(r1.trackGroup) == false) goto L14;
            return true;
        L14:
            return false;
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public int evaluateQueueSize(long r2, List<? extends MediaChunk> r4) {
            return this.trackSelection.evaluateQueueSize(r2, r4);
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public boolean excludeTrack(int r2, long r3) {
            return this.trackSelection.excludeTrack(r2, r3);
        }

        @Override // androidx.media3.exoplayer.trackselection.TrackSelection
        public Format getFormat(int r3) {
            return this.trackGroup.getFormat(this.trackSelection.getIndexInTrackGroup(r3));
        }

        @Override // androidx.media3.exoplayer.trackselection.TrackSelection
        public int getIndexInTrackGroup(int r2) {
            return this.trackSelection.getIndexInTrackGroup(r2);
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public long getLatestBitrateEstimate() {
            return this.trackSelection.getLatestBitrateEstimate();
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public Format getSelectedFormat() {
            return this.trackGroup.getFormat(this.trackSelection.getSelectedIndexInTrackGroup());
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public int getSelectedIndex() {
            return this.trackSelection.getSelectedIndex();
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public int getSelectedIndexInTrackGroup() {
            return this.trackSelection.getSelectedIndexInTrackGroup();
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public Object getSelectionData() {
            return this.trackSelection.getSelectionData();
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public int getSelectionReason() {
            return this.trackSelection.getSelectionReason();
        }

        @Override // androidx.media3.exoplayer.trackselection.TrackSelection
        public TrackGroup getTrackGroup() {
            return this.trackGroup;
        }

        @Override // androidx.media3.exoplayer.trackselection.TrackSelection
        public int getType() {
            return this.trackSelection.getType();
        }

        public int hashCode() {
            return (((17 * 31) + this.trackGroup.hashCode()) * 31) + this.trackSelection.hashCode();
        }

        @Override // androidx.media3.exoplayer.trackselection.TrackSelection
        public int indexOf(int r2) {
            return this.trackSelection.indexOf(r2);
        }

        @Override // androidx.media3.exoplayer.trackselection.TrackSelection
        public int indexOf(Format r3) {
            return this.trackSelection.indexOf(this.trackGroup.indexOf(r3));
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public boolean isTrackExcluded(int r2, long r3) {
            return this.trackSelection.isTrackExcluded(r2, r3);
        }

        @Override // androidx.media3.exoplayer.trackselection.TrackSelection
        public int length() {
            return this.trackSelection.length();
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public void onDiscontinuity() {
            this.trackSelection.onDiscontinuity();
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public void onPlayWhenReadyChanged(boolean r2) {
            this.trackSelection.onPlayWhenReadyChanged(r2);
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public void onPlaybackSpeed(float r2) {
            this.trackSelection.onPlaybackSpeed(r2);
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public void onRebuffer() {
            this.trackSelection.onRebuffer();
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public boolean shouldCancelChunkLoad(long r2, Chunk r4, List<? extends MediaChunk> r5) {
            return this.trackSelection.shouldCancelChunkLoad(r2, r4, r5);
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public void updateSelectedTrack(long r10, long r12, long r14, List<? extends MediaChunk> r16, MediaChunkIterator[] r17) {
            this.trackSelection.updateSelectedTrack(r10, r12, r14, r16, r17);
        }
    }

    public MergingMediaPeriod(CompositeSequenceableLoaderFactory r7, long[] r8, MediaPeriod... r9) {
        this.compositeSequenceableLoaderFactory = r7;
        this.periods = r9;
        this.childrenPendingPreparation = new ArrayList();
        this.childTrackGroupByMergedTrackGroup = new HashMap();
        this.compositeSequenceableLoader = r7.empty();
        this.streamPeriodIndices = new IdentityHashMap();
        this.enabledPeriods = new MediaPeriod[0];
        int r0 = 0;
    L4:
        if (r0 >= r9.length) goto L9;
        if (r8[r0] == 0) goto L8;
        this.periods[r0] = new TimeOffsetMediaPeriod(r9[r0], r8[r0]);
    L8:
        r0 = r0 + 1;
        goto L4
    }

    static /* synthetic */ List lambda$selectTracks$0(MediaPeriod r1) {
        return r1.getTrackGroups().getTrackTypes();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean continueLoading(LoadingInfo r4) {
        if (this.childrenPendingPreparation.isEmpty() == true) goto L10;
        int r0 = this.childrenPendingPreparation.size();
        int r1 = 0;
    L5:
        if (r1 >= r0) goto L7;
        this.childrenPendingPreparation.get(r1).continueLoading(r4);
        r1 = r1 + 1;
        goto L5
    L7:
        return false;
    L10:
        return this.compositeSequenceableLoader.continueLoading(r4);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void discardBuffer(long r5, boolean r7) {
        MediaPeriod[] r0 = this.enabledPeriods;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].discardBuffer(r5, r7);
        r2 = r2 + 1;
        goto L3
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long getAdjustedSeekPositionUs(long r4, SeekParameters r6) {
        if (this.enabledPeriods.length <= 0) goto L5;
        MediaPeriod r0 = this.enabledPeriods[0];
    L7:
        return r0.getAdjustedSeekPositionUs(r4, r6);
    L5:
        r0 = this.periods[0];
        goto L7
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getBufferedPositionUs() {
        return this.compositeSequenceableLoader.getBufferedPositionUs();
    }

    public MediaPeriod getChildPeriod(int r2) {
        if ((this.periods[r2] instanceof TimeOffsetMediaPeriod) == false) goto L6;
        return ((TimeOffsetMediaPeriod) this.periods[r2]).getWrappedMediaPeriod();
    L6:
        return this.periods[r2];
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        return this.compositeSequenceableLoader.getNextLoadPositionUs();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public /* synthetic */ List getStreamKeys(List r1) {
        return MediaPeriod.CC.$default$getStreamKeys(this, r1);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return (TrackGroupArray) Assertions.checkNotNull(this.trackGroups);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean isLoading() {
        return this.compositeSequenceableLoader.isLoading();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
        MediaPeriod[] r0 = this.periods;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].maybeThrowPrepareError();
        r2 = r2 + 1;
        goto L3
    }

    public void onContinueLoadingRequested(MediaPeriod r2) {
        ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onContinueLoadingRequested(this);
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader.Callback
    public /* bridge */ /* synthetic */ void onContinueLoadingRequested(SequenceableLoader r1) {
        onContinueLoadingRequested((MediaPeriod) r1);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod.Callback
    public void onPrepared(MediaPeriod r15) {
        this.childrenPendingPreparation.remove(r15);
        if (this.childrenPendingPreparation.isEmpty() == true) goto L5;
        return;
    L5:
        int r0 = 0;
        MediaPeriod[] r1 = this.periods;
        int r2 = r1.length;
        int r3 = 0;
    L6:
        if (r3 >= r2) goto L8;
        r0 = r0 + r1[r3].getTrackGroups().length;
        r3 = r3 + 1;
        goto L6
    L8:
        TrackGroup[] r4 = new TrackGroup[r0];
        int r5 = 0;
        int r6 = 0;
    L10:
        if (r6 >= this.periods.length) goto L23;
        TrackGroupArray r7 = this.periods[r6].getTrackGroups();
        int r8 = r7.length;
        int r9 = 0;
    L12:
        if (r9 >= r8) goto L22;
        TrackGroup r10 = r7.get(r9);
        Format[] r11 = new Format[r10.length];
        int r12 = 0;
    L15:
        if (r12 >= r10.length) goto L21;
        Format r13 = r10.getFormat(r12);
        Format.Builder r14 = r13.buildUpon();
        StringBuilder r16 = new StringBuilder().append(r6).append(":");
        if (r13.f26id != null) goto L19;
        String r17 = "";
    L20:
        r11[r12] = r14.setId(r16.append(r17).toString()).build();
        r12 = r12 + 1;
        goto L15
    L19:
        r17 = r13.f26id;
        goto L20
    L21:
        TrackGroup r18 = new TrackGroup(r6 + ":" + r10.f31id, r11);
        this.childTrackGroupByMergedTrackGroup.put(r18, r10);
        r4[r5] = r18;
        r9 = r9 + 1;
        r5 = r5 + 1;
        goto L12
    L22:
        r6 = r6 + 1;
        goto L10
    L23:
        this.trackGroups = new TrackGroupArray(r4);
        ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onPrepared(this);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void prepare(MediaPeriod.Callback r5, long r6) {
        this.callback = r5;
        Collections.addAll(this.childrenPendingPreparation, this.periods);
        MediaPeriod[] r0 = this.periods;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].prepare(this, r6);
        r2 = r2 + 1;
        goto L3
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long readDiscontinuity() {
        long r1 = C0180C.TIME_UNSET;
        MediaPeriod[] r3 = this.enabledPeriods;
        int r4 = r3.length;
        int r6 = 0;
    L3:
        if (r6 >= r4) goto L32;
        MediaPeriod r7 = r3[r6];
        long r8 = r7.readDiscontinuity();
        if (r8 == C0180C.TIME_UNSET) goto L25;
        if (r1 != C0180C.TIME_UNSET) goto L20;
        r1 = r8;
        MediaPeriod[] r10 = this.enabledPeriods;
        int r11 = r10.length;
        int r12 = 0;
    L9:
        if (r12 >= r11) goto L31;
        MediaPeriod r14 = r10[r12];
        if (r14 == r7) goto L31;
        if (r14.seekToUs(r1) != r1) goto L17;
        r12 = r12 + 1;
        goto L9
    L17:
        throw new IllegalStateException("Unexpected child seekToUs result.");
    L31:
        r6 = r6 + 1;
        goto L3
    L20:
        if (r8 == r1) goto L31;
        throw new IllegalStateException("Conflicting discontinuities.");
    L25:
        if (r1 == C0180C.TIME_UNSET) goto L31;
        if (r7.seekToUs(r1) == r1) goto L31;
        throw new IllegalStateException("Unexpected child seekToUs result.");
    L32:
        return r1;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public void reevaluateBuffer(long r2) {
        this.compositeSequenceableLoader.reevaluateBuffer(r2);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long seekToUs(long r4) {
        long r5 = this.enabledPeriods[0].seekToUs(r4);
        int r0 = 1;
    L4:
        if (r0 >= this.enabledPeriods.length) goto L10;
        if (this.enabledPeriods[r0].seekToUs(r5) != r5) goto L9;
        r0 = r0 + 1;
        goto L4
    L9:
        throw new IllegalStateException("Unexpected child seekToUs result.");
    L10:
        return r5;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long selectTracks(ExoTrackSelection[] r20, boolean[] r21, SampleStream[] r22, boolean[] r23, long r24) {
        ExoTrackSelection[] r1 = r20;
        int[] r3 = new int[r1.length];
        int[] r4 = new int[r1.length];
        int r5 = 0;
    L4:
        if (r5 >= r1.length) goto L18;
        if (r22[r5] != null) goto L8;
        Integer r7 = null;
    L10:
        if (r7 != null) goto L12;
        int r9 = -1;
    L13:
        r3[r5] = r9;
        if (r1[r5] == null) goto L16;
        TrackGroup r6 = r1[r5].getTrackGroup();
        r4[r5] = Integer.parseInt(r6.f31id.substring(0, r6.f31id.indexOf(":")));
    L17:
        r5 = r5 + 1;
        goto L4
    L16:
        r4[r5] = -1;
        goto L17
    L12:
        r9 = r7.intValue();
        goto L13
    L8:
        r7 = this.streamPeriodIndices.get(r22[r5]);
        goto L10
    L18:
        this.streamPeriodIndices.clear();
        SampleStream[] r8 = new SampleStream[r1.length];
        SampleStream[] r12 = new SampleStream[r1.length];
        ExoTrackSelection[] r10 = new ExoTrackSelection[r1.length];
        ArrayList r11 = new ArrayList(this.periods.length);
        int r13 = 0;
        long r14 = r24;
    L20:
        if (r13 >= this.periods.length) goto L59;
        int r15 = 0;
    L23:
        if (r15 >= r1.length) goto L33;
        if (r3[r15] != r13) goto L27;
        SampleStream r16 = r22[r15];
    L28:
        r12[r15] = r16;
        if (r4[r15] != r13) goto L31;
        ExoTrackSelection r17 = (ExoTrackSelection) Assertions.checkNotNull(r1[r15]);
        r10[r15] = new ForwardingTrackSelection(r17, (TrackGroup) Assertions.checkNotNull(this.childTrackGroupByMergedTrackGroup.get(r17.getTrackGroup())));
    L32:
        r15 = r15 + 1;
        r3 = r3;
        goto L23
    L31:
        r10[r15] = null;
        goto L32
    L27:
        r16 = null;
        goto L28
    L33:
        int[] r18 = r3;
        int r19 = r13;
        long r25 = this.periods[r13].selectTracks(r10, r21, r12, r23, r14);
        if (r19 != 0) goto L37;
        r14 = r25;
    L38:
        boolean r26 = false;
        int r110 = 0;
    L40:
        if (r110 >= r1.length) goto L53;
        if (r4[r110] != r19) goto L45;
        SampleStream r111 = (SampleStream) Assertions.checkNotNull(r12[r110]);
        r8[r110] = r12[r110];
        r26 = true;
        int[] r112 = r4;
        this.streamPeriodIndices.put(r111, Integer.valueOf(r19));
    L52:
        r110 = r110 + 1;
        r1 = r20;
        r4 = r112;
        goto L40
    L45:
        r112 = r4;
        if (r18[r110] != r19) goto L52;
        if (r12[r110] != null) goto L50;
        boolean r2 = true;
    L51:
        Assertions.checkState(r2);
        goto L52
    L50:
        r2 = false;
        goto L51
    L53:
        int[] r113 = r4;
        if (r26 == false) goto L56;
        r11.add(this.periods[r19]);
    L56:
        r13 = r19 + 1;
        r1 = r20;
        r3 = r18;
        r4 = r113;
        goto L20
    L37:
        if (r25 == r14) goto L38;
        throw new IllegalStateException("Children enabled at different positions.");
    L59:
        System.arraycopy(r8, 0, r22, 0, r8.length);
        this.enabledPeriods = (MediaPeriod[]) r11.toArray(new MediaPeriod[0]);
        this.compositeSequenceableLoader = this.compositeSequenceableLoaderFactory.create(r11, Lists.transform(r11, new MergingMediaPeriod$$ExternalSyntheticLambda0()));
        return r14;
    }
}
