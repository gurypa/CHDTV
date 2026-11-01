package androidx.media3.exoplayer.source;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.StreamKey;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class ClippingMediaPeriod implements MediaPeriod, MediaPeriod.Callback {
    private MediaPeriod.Callback callback;
    private ClippingMediaSource.IllegalClippingException clippingError;
    long endUs;
    public final MediaPeriod mediaPeriod;
    private long pendingInitialDiscontinuityPositionUs;
    private ClippingSampleStream[] sampleStreams;
    long startUs;

    private final class ClippingSampleStream implements SampleStream {
        public final SampleStream childStream;
        private boolean sentEos;
        final /* synthetic */ ClippingMediaPeriod this$0;

        public ClippingSampleStream(ClippingMediaPeriod r1, SampleStream r2) {
            this.this$0 = r1;
            this.childStream = r2;
        }

        public void clearSentEos() {
            this.sentEos = false;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public boolean isReady() {
            if (this.this$0.isPendingInitialDiscontinuity() == false) goto L5;
        L7:
            return false;
        L5:
            if (this.childStream.isReady() == false) goto L7;
            return true;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public void maybeThrowError() throws IOException {
            this.childStream.maybeThrowError();
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int readData(FormatHolder r18, DecoderInputBuffer r19, int r20) {
            if (this.this$0.isPendingInitialDiscontinuity() == false) goto L6;
            return -3;
        L6:
            if (this.sentEos == false) goto L9;
            r19.setFlags(4);
            return -4;
        L9:
            long r7 = this.this$0.getBufferedPositionUs();
            int r3 = this.childStream.readData(r18, r19, r20);
            if (r3 != (-5)) goto L26;
            Format r4 = (Format) Assertions.checkNotNull(r18.format);
            if (r4.encoderDelay == 0) goto L14;
        L15:
            int r6 = 0;
            if (this.this$0.startUs == 0) goto L18;
            int r5 = 0;
        L20:
            if (this.this$0.endUs != Long.MIN_VALUE) goto L23;
            r6 = r4.encoderPadding;
        L23:
            r18.format = r4.buildUpon().setEncoderDelay(r5).setEncoderPadding(r6).build();
        L24:
            return -5;
        L18:
            r5 = r4.encoderDelay;
            goto L20
        L14:
            if (r4.encoderPadding == 0) goto L24;
        L26:
            if (this.this$0.endUs == Long.MIN_VALUE) goto L40;
            if (r3 != (-4)) goto L31;
            int r8 = -4;
            if (r19.timeUs < this.this$0.endUs) goto L32;
        L37:
            r19.clear();
            r19.setFlags(4);
            this.sentEos = true;
            return r8;
        L32:
            if (r3 != (-3)) goto L40;
            if (r7 != Long.MIN_VALUE) goto L40;
            if (r19.waitingForKeys == true) goto L40;
        L31:
            r8 = -4;
        L40:
            return r3;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int skipData(long r2) {
            if (this.this$0.isPendingInitialDiscontinuity() == false) goto L7;
            return -3;
        L7:
            return this.childStream.skipData(r2);
        }
    }

    public ClippingMediaPeriod(MediaPeriod r3, boolean r4, long r5, long r7) {
        this.mediaPeriod = r3;
        this.sampleStreams = new ClippingSampleStream[0];
        if (r4 == false) goto L5;
        long r0 = r5;
    L6:
        this.pendingInitialDiscontinuityPositionUs = r0;
        this.startUs = r5;
        this.endUs = r7;
        return;
    L5:
        r0 = C0180C.TIME_UNSET;
        goto L6
    }

    private SeekParameters clipSeekParameters(long r9, SeekParameters r11) {
        long r0 = Util.constrainValue(r11.toleranceBeforeUs, 0, r9 - this.startUs);
        long r2 = r11.toleranceAfterUs;
        if (this.endUs != Long.MIN_VALUE) goto L5;
        long r4 = Long.MAX_VALUE;
    L6:
        long r3 = Util.constrainValue(r2, 0, r4);
        if (r0 != r11.toleranceBeforeUs) goto L12;
        if (r3 != r11.toleranceAfterUs) goto L12;
        return r11;
    L12:
        return new SeekParameters(r0, r3);
    L5:
        r4 = this.endUs - r9;
        goto L6
    }

    private static boolean shouldKeepInitialDiscontinuity(long r7, ExoTrackSelection[] r9) {
        if (r7 == 0) goto L13;
        int r0 = r9.length;
        int r2 = 0;
    L5:
        if (r2 >= r0) goto L13;
        ExoTrackSelection r3 = r9[r2];
        if (r3 == null) goto L12;
        Format r4 = r3.getSelectedFormat();
        if (MimeTypes.allSamplesAreSyncSamples(r4.sampleMimeType, r4.codecs) == true) goto L12;
        return true;
    L12:
        r2 = r2 + 1;
    L13:
        return false;
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
    public long getAdjustedSeekPositionUs(long r4, SeekParameters r6) {
        if (r4 == this.startUs) goto L5;
        SeekParameters r0 = clipSeekParameters(r4, r6);
        return this.mediaPeriod.getAdjustedSeekPositionUs(r4, r0);
    L5:
        return this.startUs;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getBufferedPositionUs() {
        long r0 = this.mediaPeriod.getBufferedPositionUs();
        if (r0 != Long.MIN_VALUE) goto L5;
    L10:
        return Long.MIN_VALUE;
    L5:
        if (this.endUs != Long.MIN_VALUE) goto L7;
    L9:
        return r0;
    L7:
        if (r0 < this.endUs) goto L9;
        goto L9
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        long r0 = this.mediaPeriod.getNextLoadPositionUs();
        if (r0 != Long.MIN_VALUE) goto L5;
    L10:
        return Long.MIN_VALUE;
    L5:
        if (this.endUs != Long.MIN_VALUE) goto L7;
    L9:
        return r0;
    L7:
        if (r0 < this.endUs) goto L9;
        goto L9
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public List<StreamKey> getStreamKeys(List<ExoTrackSelection> r2) {
        return this.mediaPeriod.getStreamKeys(r2);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return this.mediaPeriod.getTrackGroups();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean isLoading() {
        return this.mediaPeriod.isLoading();
    }

    boolean isPendingInitialDiscontinuity() {
        if (this.pendingInitialDiscontinuityPositionUs == C0180C.TIME_UNSET) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
        if (this.clippingError != null) goto L7;
        this.mediaPeriod.maybeThrowPrepareError();
        return;
    L7:
        throw this.clippingError;
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
        if (this.clippingError == null) goto L5;
        return;
    L5:
        ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onPrepared(this);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void prepare(MediaPeriod.Callback r2, long r3) {
        this.callback = r2;
        this.mediaPeriod.prepare(this, r3);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long readDiscontinuity() {
        if (isPendingInitialDiscontinuity() == false) goto L9;
        long r3 = this.pendingInitialDiscontinuityPositionUs;
        this.pendingInitialDiscontinuityPositionUs = C0180C.TIME_UNSET;
        long r5 = readDiscontinuity();
        if (r5 == C0180C.TIME_UNSET) goto L8;
        return r5;
    L8:
        return r3;
    L9:
        long r4 = this.mediaPeriod.readDiscontinuity();
        if (r4 != C0180C.TIME_UNSET) goto L12;
        return C0180C.TIME_UNSET;
    L12:
        boolean r1 = true;
        if (r4 < this.startUs) goto L15;
        boolean r0 = true;
    L16:
        Assertions.checkState(r0);
        if (this.endUs != Long.MIN_VALUE) goto L19;
    L22:
        Assertions.checkState(r1);
        return r4;
    L19:
        if (r4 <= this.endUs) goto L22;
        r1 = false;
        goto L22
    L15:
        r0 = false;
        goto L16
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public void reevaluateBuffer(long r2) {
        this.mediaPeriod.reevaluateBuffer(r2);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long seekToUs(long r8) {
        this.pendingInitialDiscontinuityPositionUs = C0180C.TIME_UNSET;
        ClippingSampleStream[] r0 = this.sampleStreams;
        int r1 = r0.length;
        boolean r2 = false;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L8;
        ClippingSampleStream r4 = r0[r3];
        if (r4 == null) goto L7;
        r4.clearSentEos();
    L7:
        r3 = r3 + 1;
        goto L3
    L8:
        long r5 = this.mediaPeriod.seekToUs(r8);
        if (r5 != r8) goto L11;
    L16:
        r2 = true;
    L17:
        Assertions.checkState(r2);
        return r5;
    L11:
        if (r5 < this.startUs) goto L17;
        if (this.endUs == Long.MIN_VALUE) goto L16;
        if (r5 > this.endUs) goto L17;
        goto L16
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long selectTracks(ExoTrackSelection[] r10, boolean[] r11, SampleStream[] r12, boolean[] r13, long r14) {
        this.sampleStreams = new ClippingSampleStream[r12.length];
        SampleStream[] r4 = new SampleStream[r12.length];
        int r0 = 0;
    L3:
        SampleStream r8 = null;
        if (r0 >= r12.length) goto L9;
        this.sampleStreams[r0] = (ClippingSampleStream) r12[r0];
        if (this.sampleStreams[r0] == null) goto L8;
        r8 = this.sampleStreams[r0].childStream;
    L8:
        r4[r0] = r8;
        r0 = r0 + 1;
        goto L3
    L9:
        long r15 = this.mediaPeriod.selectTracks(r10, r11, r4, r13, r14);
        if (isPendingInitialDiscontinuity() == true) goto L12;
    L16:
        long r16 = C0180C.TIME_UNSET;
    L17:
        this.pendingInitialDiscontinuityPositionUs = r16;
        if (r15 != r14) goto L20;
    L27:
        boolean r17 = true;
    L28:
        Assertions.checkState(r17);
        int r18 = 0;
    L30:
        if (r18 >= r12.length) goto L40;
        if (r4[r18] != null) goto L35;
        this.sampleStreams[r18] = null;
    L39:
        r12[r18] = this.sampleStreams[r18];
        r18 = r18 + 1;
        goto L30
    L35:
        if (this.sampleStreams[r18] != null) goto L37;
    L38:
        this.sampleStreams[r18] = new ClippingSampleStream(this, r4[r18]);
        goto L39
    L37:
        if (this.sampleStreams[r18].childStream == r4[r18]) goto L39;
    L40:
        return r15;
    L20:
        if (r15 >= this.startUs) goto L22;
    L26:
        r17 = false;
        goto L28
    L22:
        if (this.endUs == Long.MIN_VALUE) goto L27;
        if (r15 > this.endUs) goto L26;
    L12:
        if (r14 != this.startUs) goto L16;
        if (shouldKeepInitialDiscontinuity(this.startUs, r10) == false) goto L16;
        r16 = r15;
        goto L17
    }

    public void setClippingError(ClippingMediaSource.IllegalClippingException r1) {
        this.clippingError = r1;
    }

    public void updateClipping(long r1, long r3) {
        this.startUs = r1;
        this.endUs = r3;
    }
}
