package androidx.media3.exoplayer.source;

import androidx.media3.common.C0180C;
import androidx.media3.common.StreamKey;
import androidx.media3.common.util.Assertions;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class TimeOffsetMediaPeriod implements MediaPeriod, MediaPeriod.Callback {
    private MediaPeriod.Callback callback;
    private final MediaPeriod mediaPeriod;
    private final long timeOffsetUs;

    private static final class TimeOffsetSampleStream implements SampleStream {
        private final SampleStream sampleStream;
        private final long timeOffsetUs;

        public TimeOffsetSampleStream(SampleStream r1, long r2) {
            this.sampleStream = r1;
            this.timeOffsetUs = r2;
        }

        public SampleStream getChildStream() {
            return this.sampleStream;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public boolean isReady() {
            return this.sampleStream.isReady();
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public void maybeThrowError() throws IOException {
            this.sampleStream.maybeThrowError();
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int readData(FormatHolder r6, DecoderInputBuffer r7, int r8) {
            int r0 = this.sampleStream.readData(r6, r7, r8);
            if (r0 != (-4)) goto L5;
            r7.timeUs += this.timeOffsetUs;
        L5:
            return r0;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int skipData(long r4) {
            return this.sampleStream.skipData(r4 - this.timeOffsetUs);
        }
    }

    public TimeOffsetMediaPeriod(MediaPeriod r1, long r2) {
        this.mediaPeriod = r1;
        this.timeOffsetUs = r2;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean continueLoading(LoadingInfo r7) {
        return this.mediaPeriod.continueLoading(r7.buildUpon().setPlaybackPositionUs(r7.playbackPositionUs - this.timeOffsetUs).build());
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void discardBuffer(long r4, boolean r6) {
        this.mediaPeriod.discardBuffer(r4 - this.timeOffsetUs, r6);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long getAdjustedSeekPositionUs(long r5, SeekParameters r7) {
        return this.mediaPeriod.getAdjustedSeekPositionUs(r5 - this.timeOffsetUs, r7) + this.timeOffsetUs;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getBufferedPositionUs() {
        long r0 = this.mediaPeriod.getBufferedPositionUs();
        if (r0 != Long.MIN_VALUE) goto L6;
        return Long.MIN_VALUE;
    L6:
        return this.timeOffsetUs + r0;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        long r0 = this.mediaPeriod.getNextLoadPositionUs();
        if (r0 != Long.MIN_VALUE) goto L6;
        return Long.MIN_VALUE;
    L6:
        return this.timeOffsetUs + r0;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public List<StreamKey> getStreamKeys(List<ExoTrackSelection> r2) {
        return this.mediaPeriod.getStreamKeys(r2);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return this.mediaPeriod.getTrackGroups();
    }

    public MediaPeriod getWrappedMediaPeriod() {
        return this.mediaPeriod;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean isLoading() {
        return this.mediaPeriod.isLoading();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
        this.mediaPeriod.maybeThrowPrepareError();
    }

    public void onContinueLoadingRequested(MediaPeriod r2) {
        ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onContinueLoadingRequested(this);
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader.Callback
    public /* bridge */ /* synthetic */ void onContinueLoadingRequested(SequenceableLoader r1) {
        onContinueLoadingRequested((MediaPeriod) r1);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod.Callback
    public void onPrepared(MediaPeriod r2) {
        ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onPrepared(this);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void prepare(MediaPeriod.Callback r4, long r5) {
        this.callback = r4;
        this.mediaPeriod.prepare(this, r5 - this.timeOffsetUs);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long readDiscontinuity() {
        long r0 = this.mediaPeriod.readDiscontinuity();
        if (r0 != C0180C.TIME_UNSET) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        return this.timeOffsetUs + r0;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public void reevaluateBuffer(long r4) {
        this.mediaPeriod.reevaluateBuffer(r4 - this.timeOffsetUs);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long seekToUs(long r5) {
        return this.mediaPeriod.seekToUs(r5 - this.timeOffsetUs) + this.timeOffsetUs;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long selectTracks(ExoTrackSelection[] r10, boolean[] r11, SampleStream[] r12, boolean[] r13, long r14) {
        SampleStream[] r4 = new SampleStream[r12.length];
        int r0 = 0;
    L3:
        SampleStream r8 = null;
        if (r0 >= r12.length) goto L9;
        TimeOffsetSampleStream r1 = (TimeOffsetSampleStream) r12[r0];
        if (r1 == null) goto L8;
        r8 = r1.getChildStream();
    L8:
        r4[r0] = r8;
        r0 = r0 + 1;
        goto L3
    L9:
        long r15 = this.mediaPeriod.selectTracks(r10, r11, r4, r13, r14 - this.timeOffsetUs);
        int r16 = 0;
    L11:
        if (r16 >= r12.length) goto L22;
        SampleStream r2 = r4[r16];
        if (r2 != null) goto L16;
        r12[r16] = null;
    L20:
        r16 = r16 + 1;
        goto L11
    L16:
        if (r12[r16] != null) goto L18;
    L19:
        r12[r16] = new TimeOffsetSampleStream(r2, this.timeOffsetUs);
        goto L20
    L18:
        if (((TimeOffsetSampleStream) r12[r16]).getChildStream() == r2) goto L20;
    L22:
        return this.timeOffsetUs + r15;
    }
}
