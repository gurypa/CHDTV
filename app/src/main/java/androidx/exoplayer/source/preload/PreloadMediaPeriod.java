package androidx.media3.exoplayer.source.preload;

import androidx.media3.common.util.Assertions;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.SampleStream;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
final class PreloadMediaPeriod implements MediaPeriod {
    private MediaPeriod.Callback callback;
    public final MediaPeriod mediaPeriod;
    private PreloadTrackSelectionHolder preloadTrackSelectionHolder;
    private boolean prepareInternalCalled;
    public boolean prepared;

    private static class PreloadTrackSelectionHolder {
        public final boolean[] mayRetainStreamFlags;
        public final ExoTrackSelection[] selections;
        public final boolean[] streamResetFlags;
        public final SampleStream[] streams;
        public final long trackSelectionPositionUs;

        public PreloadTrackSelectionHolder(ExoTrackSelection[] r1, boolean[] r2, SampleStream[] r3, boolean[] r4, long r5) {
            this.selections = r1;
            this.mayRetainStreamFlags = r2;
            this.streams = r3;
            this.streamResetFlags = r4;
            this.trackSelectionPositionUs = r5;
        }
    }

    public PreloadMediaPeriod(MediaPeriod r1) {
        this.mediaPeriod = r1;
    }

    static /* synthetic */ MediaPeriod.Callback access$000(PreloadMediaPeriod r1) {
        return r1.callback;
    }

    private static boolean isSameAdaptionSet(ExoTrackSelection r4, ExoTrackSelection r5) {
        if (Objects.equals(r4.getTrackGroup(), r5.getTrackGroup()) == true) goto L5;
    L16:
        return false;
    L5:
        if (r4.length() != r5.length()) goto L16;
        int r0 = 0;
    L9:
        if (r0 >= r4.length()) goto L14;
        if (r4.getIndexInTrackGroup(r0) != r5.getIndexInTrackGroup(r0)) goto L12;
        r0 = r0 + 1;
        goto L9
    L12:
        return false;
    L14:
        return true;
    }

    private static boolean maybeUpdatePreloadTrackSelectionHolderForReselection(ExoTrackSelection[] r8, PreloadTrackSelectionHolder r9) {
        ExoTrackSelection[] r0 = ((PreloadTrackSelectionHolder) Assertions.checkNotNull(r9)).selections;
        boolean r1 = false;
        int r2 = 0;
    L4:
        if (r2 >= r8.length) goto L27;
        ExoTrackSelection r3 = r8[r2];
        ExoTrackSelection r4 = r0[r2];
        if (r3 != null) goto L9;
        if (r4 != null) goto L9;
    L26:
        r2 = r2 + 1;
    L9:
        r9.mayRetainStreamFlags[r2] = false;
        if (r3 != null) goto L12;
        r9.selections[r2] = null;
        r1 = true;
        goto L26
    L12:
        if (r4 != null) goto L15;
        r9.selections[r2] = r3;
        r1 = true;
        goto L26
    L15:
        if (isSameAdaptionSet(r3, r4) == true) goto L18;
        r9.selections[r2] = r3;
        r1 = true;
        goto L26
    L18:
        if (r3.getTrackGroup().type != 2) goto L20;
    L25:
        r9.mayRetainStreamFlags[r2] = true;
        goto L26
    L20:
        if (r3.getTrackGroup().type == 1) goto L25;
        if (r3.getSelectedIndexInTrackGroup() == r4.getSelectedIndexInTrackGroup()) goto L25;
        r9.selections[r2] = r3;
        r1 = true;
        goto L26
    L27:
        return r1;
    }

    private void prepareInternal(long r3) {
        this.prepareInternalCalled = true;
        this.mediaPeriod.prepare(new C03581(this), r3);
    }

    private long selectTracksInternal(ExoTrackSelection[] r19, boolean[] r20, SampleStream[] r21, boolean[] r22, long r23) {
        if (this.preloadTrackSelectionHolder != null) goto L7;
        return this.mediaPeriod.selectTracks(r19, r20, r21, r22, r23);
    L7:
        if (r21.length != this.preloadTrackSelectionHolder.streams.length) goto L9;
        boolean r1 = true;
    L10:
        Assertions.checkState(r1);
        if (r23 == this.preloadTrackSelectionHolder.trackSelectionPositionUs) goto L21;
        int r2 = 0;
    L14:
        if (r2 >= this.preloadTrackSelectionHolder.streams.length) goto L19;
        if (this.preloadTrackSelectionHolder.streams[r2] == null) goto L18;
        r21[r2] = this.preloadTrackSelectionHolder.streams[r2];
        r20[r2] = false;
    L18:
        r2 = r2 + 1;
        goto L14
    L19:
        this.preloadTrackSelectionHolder = null;
        return this.mediaPeriod.selectTracks(r19, r20, r21, r22, r23);
    L21:
        PreloadTrackSelectionHolder r3 = (PreloadTrackSelectionHolder) Assertions.checkNotNull(this.preloadTrackSelectionHolder);
        long r6 = r3.trackSelectionPositionUs;
        boolean[] r8 = r3.streamResetFlags;
        if (maybeUpdatePreloadTrackSelectionHolderForReselection(r19, r3) == false) goto L31;
        boolean[] r15 = new boolean[r8.length];
        r6 = this.mediaPeriod.selectTracks(r3.selections, r3.mayRetainStreamFlags, r3.streams, r15, r3.trackSelectionPositionUs);
        int r4 = 0;
    L25:
        if (r4 >= r3.mayRetainStreamFlags.length) goto L30;
        if (r3.mayRetainStreamFlags[r4] == false) goto L29;
        r15[r4] = true;
    L29:
        r4 = r4 + 1;
        goto L25
    L30:
        r8 = r15;
    L31:
        System.arraycopy(r3.streams, 0, r21, 0, r3.streams.length);
        System.arraycopy(r8, 0, r22, 0, r8.length);
        this.preloadTrackSelectionHolder = null;
        return r6;
    L9:
        r1 = false;
        goto L10
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean continueLoading(LoadingInfo r2) {
        return this.mediaPeriod.continueLoading(r2);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void discardBuffer(long r2, boolean r4) {
        this.mediaPeriod.discardBuffer(r2, r4);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long getAdjustedSeekPositionUs(long r3, SeekParameters r5) {
        return this.mediaPeriod.getAdjustedSeekPositionUs(r3, r5);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getBufferedPositionUs() {
        return this.mediaPeriod.getBufferedPositionUs();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        return this.mediaPeriod.getNextLoadPositionUs();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public /* synthetic */ List getStreamKeys(List r1) {
        return MediaPeriod.CC.$default$getStreamKeys(this, r1);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return this.mediaPeriod.getTrackGroups();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean isLoading() {
        return this.mediaPeriod.isLoading();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
        this.mediaPeriod.maybeThrowPrepareError();
    }

    public void maybeThrowStreamError() throws IOException {
        Assertions.checkState(this.prepared);
        if (this.preloadTrackSelectionHolder == null) goto L10;
        SampleStream[] r0 = this.preloadTrackSelectionHolder.streams;
        int r1 = r0.length;
        int r2 = 0;
    L5:
        if (r2 >= r1) goto L14;
        SampleStream r3 = r0[r2];
        if (r3 == null) goto L9;
        r3.maybeThrowError();
    L9:
        r2 = r2 + 1;
        goto L5
    L14:
        return;
    }

    public void preload(MediaPeriod.Callback r2, long r3) {
        this.callback = r2;
        if (this.prepared == false) goto L6;
        r2.onPrepared(this);
    L6:
        if (this.prepareInternalCalled == true) goto L9;
        prepareInternal(r3);
        return;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void prepare(MediaPeriod.Callback r2, long r3) {
        this.callback = r2;
        if (this.prepared == false) goto L7;
        r2.onPrepared(this);
        return;
    L7:
        if (this.prepareInternalCalled == true) goto L10;
        prepareInternal(r3);
        return;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long readDiscontinuity() {
        return this.mediaPeriod.readDiscontinuity();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public void reevaluateBuffer(long r2) {
        this.mediaPeriod.reevaluateBuffer(r2);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long seekToUs(long r3) {
        return this.mediaPeriod.seekToUs(r3);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long selectTracks(ExoTrackSelection[] r3, boolean[] r4, SampleStream[] r5, boolean[] r6, long r7) {
        return selectTracksInternal(r3, r4, r5, r6, r7);
    }

    public long selectTracksForPreloading(ExoTrackSelection[] r11, long r12) {
        SampleStream[] r4 = new SampleStream[r11.length];
        boolean[] r5 = new boolean[r11.length];
        boolean[] r3 = new boolean[r11.length];
        long r13 = selectTracksInternal(r11, r3, r4, r5, r12);
        this.preloadTrackSelectionHolder = new PreloadTrackSelectionHolder(r11, r3, r4, r5, r13);
        return r13;
    }
}
