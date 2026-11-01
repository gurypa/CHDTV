package androidx.media3.exoplayer.source;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.upstream.Allocator;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class MaskingMediaPeriod implements MediaPeriod, MediaPeriod.Callback {
    private final Allocator allocator;
    private MediaPeriod.Callback callback;

    /* JADX INFO: renamed from: id */
    public final MediaSource.MediaPeriodId f44id;
    private PrepareListener listener;
    private MediaPeriod mediaPeriod;
    private MediaSource mediaSource;
    private boolean notifiedPrepareError;
    private long preparePositionOverrideUs;
    private final long preparePositionUs;

    public interface PrepareListener {
        void onPrepareComplete(MediaSource.MediaPeriodId r1);

        void onPrepareError(MediaSource.MediaPeriodId r1, IOException r2);
    }

    public MaskingMediaPeriod(MediaSource.MediaPeriodId r3, Allocator r4, long r5) {
        this.f44id = r3;
        this.allocator = r4;
        this.preparePositionUs = r5;
        this.preparePositionOverrideUs = C0180C.TIME_UNSET;
    }

    private long getPreparePositionWithOverride(long r5) {
        if (this.preparePositionOverrideUs == C0180C.TIME_UNSET) goto L6;
        return this.preparePositionOverrideUs;
    L6:
        return r5;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean continueLoading(LoadingInfo r2) {
        if (this.mediaPeriod != null) goto L5;
    L7:
        return false;
    L5:
        if (this.mediaPeriod.continueLoading(r2) == false) goto L7;
        return true;
    }

    public void createPeriod(MediaSource.MediaPeriodId r5) {
        long r0 = getPreparePositionWithOverride(this.preparePositionUs);
        this.mediaPeriod = ((MediaSource) Assertions.checkNotNull(this.mediaSource)).createPeriod(r5, this.allocator, r0);
        if (this.callback == null) goto L6;
        this.mediaPeriod.prepare(this, r0);
        return;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void discardBuffer(long r2, boolean r4) {
        ((MediaPeriod) Util.castNonNull(this.mediaPeriod)).discardBuffer(r2, r4);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long getAdjustedSeekPositionUs(long r3, SeekParameters r5) {
        return ((MediaPeriod) Util.castNonNull(this.mediaPeriod)).getAdjustedSeekPositionUs(r3, r5);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getBufferedPositionUs() {
        return ((MediaPeriod) Util.castNonNull(this.mediaPeriod)).getBufferedPositionUs();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        return ((MediaPeriod) Util.castNonNull(this.mediaPeriod)).getNextLoadPositionUs();
    }

    public long getPreparePositionOverrideUs() {
        return this.preparePositionOverrideUs;
    }

    public long getPreparePositionUs() {
        return this.preparePositionUs;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public /* synthetic */ List getStreamKeys(List r1) {
        return MediaPeriod.CC.$default$getStreamKeys(this, r1);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return ((MediaPeriod) Util.castNonNull(this.mediaPeriod)).getTrackGroups();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean isLoading() {
        if (this.mediaPeriod != null) goto L5;
    L7:
        return false;
    L5:
        if (this.mediaPeriod.isLoading() == false) goto L7;
        return true;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
    L9:
        e = move-exception;
        if (this.listener != null) goto L13;
        throw e;
    L13:
        if (this.notifiedPrepareError == true) goto L21;
        this.notifiedPrepareError = true;
        this.listener.onPrepareError(this.f44id, e);
        return;
    L21:
        return;
    L3:
        if (this.mediaPeriod == null) goto L6;
        this.mediaPeriod.maybeThrowPrepareError();     // Catch: IOException -> L9
    L17:
        return;
    L6:
        if (this.mediaSource == null) goto L17;
        this.mediaSource.maybeThrowSourceInfoRefreshError();     // Catch: IOException -> L9
        goto L17
    }

    public void onContinueLoadingRequested(MediaPeriod r2) {
        ((MediaPeriod.Callback) Util.castNonNull(this.callback)).onContinueLoadingRequested(this);
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader.Callback
    public /* bridge */ /* synthetic */ void onContinueLoadingRequested(SequenceableLoader r1) {
        onContinueLoadingRequested((MediaPeriod) r1);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod.Callback
    public void onPrepared(MediaPeriod r3) {
        ((MediaPeriod.Callback) Util.castNonNull(this.callback)).onPrepared(this);
        if (this.listener == null) goto L6;
        this.listener.onPrepareComplete(this.f44id);
        return;
    }

    public void overridePreparePositionUs(long r1) {
        this.preparePositionOverrideUs = r1;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void prepare(MediaPeriod.Callback r4, long r5) {
        this.callback = r4;
        if (this.mediaPeriod == null) goto L6;
        this.mediaPeriod.prepare(this, getPreparePositionWithOverride(this.preparePositionUs));
        return;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long readDiscontinuity() {
        return ((MediaPeriod) Util.castNonNull(this.mediaPeriod)).readDiscontinuity();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public void reevaluateBuffer(long r2) {
        ((MediaPeriod) Util.castNonNull(this.mediaPeriod)).reevaluateBuffer(r2);
    }

    public void releasePeriod() {
        if (this.mediaPeriod == null) goto L6;
        ((MediaSource) Assertions.checkNotNull(this.mediaSource)).releasePeriod(this.mediaPeriod);
        return;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long seekToUs(long r3) {
        return ((MediaPeriod) Util.castNonNull(this.mediaPeriod)).seekToUs(r3);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long selectTracks(ExoTrackSelection[] r12, boolean[] r13, SampleStream[] r14, boolean[] r15, long r16) {
        if (this.preparePositionOverrideUs != C0180C.TIME_UNSET) goto L5;
    L7:
        long r9 = r16;
    L8:
        this.preparePositionOverrideUs = C0180C.TIME_UNSET;
        return ((MediaPeriod) Util.castNonNull(this.mediaPeriod)).selectTracks(r12, r13, r14, r15, r9);
    L5:
        if (r16 != this.preparePositionUs) goto L7;
        r9 = this.preparePositionOverrideUs;
        goto L8
    }

    public void setMediaSource(MediaSource r2) {
        if (this.mediaSource != null) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        this.mediaSource = r2;
        return;
    L5:
        r0 = false;
        goto L6
    }

    public void setPrepareListener(PrepareListener r1) {
        this.listener = r1;
    }
}
