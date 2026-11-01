package androidx.media3.exoplayer;

import androidx.media3.common.C0180C;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.exoplayer.source.ClippingMediaPeriod;
import androidx.media3.exoplayer.source.EmptySampleStream;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.SampleStream;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.trackselection.TrackSelector;
import androidx.media3.exoplayer.trackselection.TrackSelectorResult;
import androidx.media3.exoplayer.upstream.Allocator;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
final class MediaPeriodHolder {
    private static final String TAG = "MediaPeriodHolder";
    public boolean allRenderersInCorrectState;
    public boolean hasEnabledTracks;
    public MediaPeriodInfo info;
    private final boolean[] mayRetainStreamFlags;
    public final MediaPeriod mediaPeriod;
    private final MediaSourceList mediaSourceList;
    private MediaPeriodHolder next;
    public boolean prepareCalled;
    public boolean prepared;
    private final RendererCapabilities[] rendererCapabilities;
    private long rendererPositionOffsetUs;
    public final SampleStream[] sampleStreams;
    public final long targetPreloadBufferDurationUs;
    private TrackGroupArray trackGroups;
    private final TrackSelector trackSelector;
    private TrackSelectorResult trackSelectorResult;
    public final Object uid;

    interface Factory {
        MediaPeriodHolder create(MediaPeriodInfo r1, long r2);
    }

    public MediaPeriodHolder(RendererCapabilities[] r17, long r18, TrackSelector r20, Allocator r21, MediaSourceList r22, MediaPeriodInfo r23, TrackSelectorResult r24, long r25) {
        this.rendererCapabilities = r17;
        this.rendererPositionOffsetUs = r18;
        this.trackSelector = r20;
        this.mediaSourceList = r22;
        this.uid = r23.f35id.periodUid;
        this.info = r23;
        this.targetPreloadBufferDurationUs = r25;
        this.trackGroups = TrackGroupArray.EMPTY;
        this.trackSelectorResult = r24;
        this.sampleStreams = new SampleStream[r17.length];
        this.mayRetainStreamFlags = new boolean[r17.length];
        this.mediaPeriod = createMediaPeriod(r23.f35id, r22, r21, r23.startPositionUs, r23.endPositionUs);
    }

    private void associateNoSampleRenderersWithEmptySampleStream(SampleStream[] r4) {
        int r0 = 0;
    L4:
        if (r0 >= this.rendererCapabilities.length) goto L11;
        if (this.rendererCapabilities[r0].getTrackType() != (-2)) goto L10;
        if (this.trackSelectorResult.isRendererEnabled(r0) == false) goto L10;
        r4[r0] = new EmptySampleStream();
    L10:
        r0 = r0 + 1;
        goto L4
    }

    private static MediaPeriod createMediaPeriod(MediaSource.MediaPeriodId r7, MediaSourceList r8, Allocator r9, long r10, long r12) {
        MediaPeriod r1 = r8.createPeriod(r7, r9, r10);
        if (r12 == C0180C.TIME_UNSET) goto L6;
        return new ClippingMediaPeriod(r1, true, 0, r12);
    L6:
        return r1;
    }

    private void disableTrackSelectionsInResult() {
        if (isLoadingMediaPeriod() == true) goto L5;
        return;
    L5:
        int r0 = 0;
    L7:
        if (r0 >= this.trackSelectorResult.length) goto L13;
        boolean r1 = this.trackSelectorResult.isRendererEnabled(r0);
        ExoTrackSelection r2 = this.trackSelectorResult.selections[r0];
        if (r1 == false) goto L12;
        if (r2 == null) goto L12;
        r2.disable();
    L12:
        r0 = r0 + 1;
        goto L7
    }

    private void disassociateNoSampleRenderersWithEmptySampleStream(SampleStream[] r4) {
        int r0 = 0;
    L4:
        if (r0 >= this.rendererCapabilities.length) goto L9;
        if (this.rendererCapabilities[r0].getTrackType() != (-2)) goto L8;
        r4[r0] = null;
    L8:
        r0 = r0 + 1;
        goto L4
    }

    private void enableTrackSelectionsInResult() {
        if (isLoadingMediaPeriod() == true) goto L5;
        return;
    L5:
        int r0 = 0;
    L7:
        if (r0 >= this.trackSelectorResult.length) goto L13;
        boolean r1 = this.trackSelectorResult.isRendererEnabled(r0);
        ExoTrackSelection r2 = this.trackSelectorResult.selections[r0];
        if (r1 == false) goto L12;
        if (r2 == null) goto L12;
        r2.enable();
    L12:
        r0 = r0 + 1;
        goto L7
    }

    private boolean isLoadingMediaPeriod() {
        if (this.next != null) goto L5;
        return true;
    L5:
        return false;
    }

    private static void releaseMediaPeriod(MediaSourceList r3, MediaPeriod r4) {
    L7:
        e = move-exception;
        Log.m45e(TAG, "Period release failed.", e);
        return;
    L3:
        if ((r4 instanceof ClippingMediaPeriod) == false) goto L5;
        r3.releasePeriod(((ClippingMediaPeriod) r4).mediaPeriod);     // Catch: RuntimeException -> L7
    L10:
        return;
    L5:
        r3.releasePeriod(r4);     // Catch: RuntimeException -> L7
        goto L10
    }

    public long applyTrackSelection(TrackSelectorResult r8, long r9, boolean r11) {
        return applyTrackSelection(r8, r9, r11, new boolean[this.rendererCapabilities.length]);
    }

    public long applyTrackSelection(TrackSelectorResult r12, long r13, boolean r15, boolean[] r16) {
        int r0 = 0;
    L3:
        boolean r2 = false;
        if (r0 >= r12.length) goto L12;
        boolean[] r1 = this.mayRetainStreamFlags;
        if (r15 == true) goto L11;
        if (r12.isEquivalent(this.trackSelectorResult, r0) == false) goto L11;
        r2 = true;
    L11:
        r1[r0] = r2;
        r0 = r0 + 1;
        goto L3
    L12:
        disassociateNoSampleRenderersWithEmptySampleStream(this.sampleStreams);
        disableTrackSelectionsInResult();
        this.trackSelectorResult = r12;
        enableTrackSelectionsInResult();
        long r14 = this.mediaPeriod.selectTracks(r12.selections, this.mayRetainStreamFlags, this.sampleStreams, r16, r13);
        associateNoSampleRenderersWithEmptySampleStream(this.sampleStreams);
        this.hasEnabledTracks = false;
        int r3 = 0;
    L14:
        if (r3 >= this.sampleStreams.length) goto L26;
        if (this.sampleStreams[r3] == null) goto L21;
        Assertions.checkState(r12.isRendererEnabled(r3));
        if (this.rendererCapabilities[r3].getTrackType() == (-2)) goto L25;
        this.hasEnabledTracks = true;
    L25:
        r3 = r3 + 1;
        goto L14
    L21:
        if (r12.selections[r3] != null) goto L23;
        boolean r4 = true;
    L24:
        Assertions.checkState(r4);
        goto L25
    L23:
        r4 = false;
        goto L24
    L26:
        return r14;
    }

    public boolean canBeUsedForMediaPeriodInfo(MediaPeriodInfo r5) {
        if (MediaPeriodQueue.areDurationsCompatible(this.info.durationUs, r5.durationUs) == true) goto L5;
    L9:
        return false;
    L5:
        if (this.info.startPositionUs != r5.startPositionUs) goto L9;
        if (this.info.f35id.equals(r5.f35id) == false) goto L9;
        return true;
    }

    public void continueLoading(LoadingInfo r2) {
        Assertions.checkState(isLoadingMediaPeriod());
        this.mediaPeriod.continueLoading(r2);
    }

    public long getBufferedPositionUs() {
        if (this.prepared == true) goto L7;
        return this.info.startPositionUs;
    L7:
        if (this.hasEnabledTracks == false) goto L9;
        long r3 = this.mediaPeriod.getBufferedPositionUs();
    L11:
        if (r3 != Long.MIN_VALUE) goto L14;
        return this.info.durationUs;
    L14:
        return r3;
    L9:
        r3 = Long.MIN_VALUE;
        goto L11
    }

    public MediaPeriodHolder getNext() {
        return this.next;
    }

    public long getNextLoadPositionUs() {
        if (this.prepared == true) goto L6;
        return 0;
    L6:
        return this.mediaPeriod.getNextLoadPositionUs();
    }

    public long getRendererOffset() {
        return this.rendererPositionOffsetUs;
    }

    public long getStartPositionRendererTime() {
        return this.info.startPositionUs + this.rendererPositionOffsetUs;
    }

    public TrackGroupArray getTrackGroups() {
        return this.trackGroups;
    }

    public TrackSelectorResult getTrackSelectorResult() {
        return this.trackSelectorResult;
    }

    public void handlePrepared(float r10, Timeline r11, boolean r12) throws ExoPlaybackException {
        this.prepared = true;
        this.trackGroups = this.mediaPeriod.getTrackGroups();
        TrackSelectorResult r0 = selectTracks(r10, r11, r12);
        long r1 = this.info.startPositionUs;
        if (this.info.durationUs != C0180C.TIME_UNSET) goto L5;
    L7:
        long r3 = applyTrackSelection(r0, r1, false);
        this.rendererPositionOffsetUs += this.info.startPositionUs - r3;
        this.info = this.info.copyWithStartPositionUs(r3);
        return;
    L5:
        if (r1 < this.info.durationUs) goto L7;
        r1 = Math.max(0, this.info.durationUs - 1);
        goto L7
    }

    public boolean hasLoadingError() {
        if (this.prepared == true) goto L5;
        this.mediaPeriod.maybeThrowPrepareError();     // Catch: IOException -> L13
    L12:
        return false;
    L5:
        SampleStream[] r0 = this.sampleStreams;     // Catch: IOException -> L13
        int r2 = r0.length;     // Catch: IOException -> L13
        int r3 = 0;
    L6:
        if (r3 >= r2) goto L12;
        SampleStream r4 = r0[r3];     // Catch: IOException -> L13
        if (r4 == null) goto L10;
        r4.maybeThrowError();     // Catch: IOException -> L13
    L10:
        r3 = r3 + 1;
        goto L6
    L14:
        return true;
    }

    public boolean isFullyBuffered() {
        if (this.prepared == true) goto L5;
    L9:
        return false;
    L5:
        if (this.hasEnabledTracks == true) goto L7;
    L8:
        return true;
    L7:
        if (this.mediaPeriod.getBufferedPositionUs() != Long.MIN_VALUE) goto L9;
        goto L8
    }

    public boolean isFullyPreloaded() {
        if (this.prepared == true) goto L5;
    L9:
        return false;
    L5:
        if (isFullyBuffered() == false) goto L7;
    L8:
        return true;
    L7:
        if ((getBufferedPositionUs() - this.info.startPositionUs) < this.targetPreloadBufferDurationUs) goto L9;
        goto L8
    }

    public void prepare(MediaPeriod.Callback r2, long r3) {
        this.prepareCalled = true;
        this.mediaPeriod.prepare(r2, r3);
    }

    public void reevaluateBuffer(long r4) {
        Assertions.checkState(isLoadingMediaPeriod());
        if (this.prepared == false) goto L6;
        this.mediaPeriod.reevaluateBuffer(toPeriodTime(r4));
        return;
    }

    public void release() {
        disableTrackSelectionsInResult();
        releaseMediaPeriod(this.mediaSourceList, this.mediaPeriod);
    }

    public TrackSelectorResult selectTracks(float r7, Timeline r8, boolean r9) throws ExoPlaybackException {
        TrackSelectorResult r0 = this.trackSelector.selectTracks(this.rendererCapabilities, getTrackGroups(), this.info.f35id, r8);
        int r1 = 0;
    L3:
        int r3 = 0;
        boolean r4 = false;
        boolean r5 = false;
        if (r1 >= r0.length) goto L18;
        if (r0.isRendererEnabled(r1) == false) goto L14;
        if (r0.selections[r1] == null) goto L10;
    L11:
        r4 = true;
    L12:
        Assertions.checkState(r4);
    L17:
        r1 = r1 + 1;
        goto L3
    L10:
        if (this.rendererCapabilities[r1].getTrackType() != (-2)) goto L12;
    L14:
        if (r0.selections[r1] != null) goto L16;
        r5 = true;
    L16:
        Assertions.checkState(r5);
        goto L17
    L18:
        ExoTrackSelection[] r2 = r0.selections;
        int r6 = r2.length;
    L19:
        if (r3 >= r6) goto L24;
        ExoTrackSelection r10 = r2[r3];
        if (r10 == null) goto L23;
        r10.onPlaybackSpeed(r7);
        r10.onPlayWhenReadyChanged(r9);
    L23:
        r3 = r3 + 1;
        goto L19
    L24:
        return r0;
    }

    public void setNext(MediaPeriodHolder r2) {
        if (r2 != this.next) goto L5;
        return;
    L5:
        disableTrackSelectionsInResult();
        this.next = r2;
        enableTrackSelectionsInResult();
    }

    public void setRendererOffset(long r1) {
        this.rendererPositionOffsetUs = r1;
    }

    public long toPeriodTime(long r3) {
        return r3 - getRendererOffset();
    }

    public long toRendererTime(long r3) {
        return getRendererOffset() + r3;
    }

    public void updateClipping() {
        if ((this.mediaPeriod instanceof ClippingMediaPeriod) == true) goto L5;
        return;
    L5:
        if (this.info.endPositionUs != C0180C.TIME_UNSET) goto L7;
        long r0 = Long.MIN_VALUE;
    L8:
        ((ClippingMediaPeriod) this.mediaPeriod).updateClipping(0, r0);
        return;
    L7:
        r0 = this.info.endPositionUs;
        goto L8
    }
}
