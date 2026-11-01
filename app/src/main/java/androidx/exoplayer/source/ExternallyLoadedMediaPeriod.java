package androidx.media3.exoplayer.source;

import android.net.Uri;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.TrackGroup;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes.dex */
final class ExternallyLoadedMediaPeriod implements MediaPeriod {
    private final ExternalLoader externalLoader;
    private final AtomicBoolean loadingFinished;
    private ListenableFuture<?> loadingFuture;
    private final AtomicReference<Throwable> loadingThrowable;
    private final byte[] sampleData;
    private final TrackGroupArray tracks;
    private final Uri uri;

    private final class SampleStreamImpl implements SampleStream {
        private static final int STREAM_STATE_END_OF_STREAM = 2;
        private static final int STREAM_STATE_SEND_FORMAT = 0;
        private static final int STREAM_STATE_SEND_SAMPLE = 1;
        private int streamState;
        final /* synthetic */ ExternallyLoadedMediaPeriod this$0;

        public SampleStreamImpl(ExternallyLoadedMediaPeriod r1) {
            this.this$0 = r1;
            this.streamState = 0;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public boolean isReady() {
            return ExternallyLoadedMediaPeriod.access$000(this.this$0).get();
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public void maybeThrowError() throws IOException {
            Throwable r0 = (Throwable) ExternallyLoadedMediaPeriod.access$100(this.this$0).get();
            if (r0 != null) goto L6;
            return;
        L6:
            throw new IOException(r0);
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int readData(FormatHolder r8, DecoderInputBuffer r9, int r10) {
            if (this.streamState != 2) goto L7;
            r9.addFlag(4);
            return -4;
        L7:
            if ((r10 & 2) == 0) goto L9;
        L22:
            r8.format = ExternallyLoadedMediaPeriod.access$200(this.this$0).get(0).getFormat(0);
            this.streamState = 1;
            return -5;
        L9:
            if (this.streamState == 0) goto L22;
            if (ExternallyLoadedMediaPeriod.access$000(this.this$0).get() == true) goto L15;
            return -3;
        L15:
            int r0 = ExternallyLoadedMediaPeriod.access$300(this.this$0).length;
            r9.addFlag(1);
            r9.timeUs = 0;
            if ((r10 & 4) != 0) goto L19;
            r9.ensureSpaceForWrite(r0);
            r9.data.put(ExternallyLoadedMediaPeriod.access$300(this.this$0), 0, r0);
        L19:
            if ((r10 & 1) != 0) goto L21;
            this.streamState = 2;
        L21:
            return -4;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int skipData(long r2) {
            return 0;
        }
    }

    public ExternallyLoadedMediaPeriod(Uri r7, String r8, ExternalLoader r9) {
        this.uri = r7;
        Format r0 = new Format.Builder().setSampleMimeType(r8).build();
        this.externalLoader = r9;
        this.tracks = new TrackGroupArray(new TrackGroup[]{new TrackGroup(new Format[]{r0})});
        this.sampleData = r7.toString().getBytes(StandardCharsets.UTF_8);
        this.loadingFinished = new AtomicBoolean();
        this.loadingThrowable = new AtomicReference();
    }

    static /* synthetic */ AtomicBoolean access$000(ExternallyLoadedMediaPeriod r1) {
        return r1.loadingFinished;
    }

    static /* synthetic */ AtomicReference access$100(ExternallyLoadedMediaPeriod r1) {
        return r1.loadingThrowable;
    }

    static /* synthetic */ TrackGroupArray access$200(ExternallyLoadedMediaPeriod r1) {
        return r1.tracks;
    }

    static /* synthetic */ byte[] access$300(ExternallyLoadedMediaPeriod r1) {
        return r1.sampleData;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean continueLoading(LoadingInfo r2) {
        return !this.loadingFinished.get();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void discardBuffer(long r1, boolean r3) {
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long getAdjustedSeekPositionUs(long r1, SeekParameters r3) {
        return r1;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getBufferedPositionUs() {
        if (this.loadingFinished.get() == false) goto L5;
        return Long.MIN_VALUE;
    L5:
        return 0;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        if (this.loadingFinished.get() == false) goto L5;
        return Long.MIN_VALUE;
    L5:
        return 0;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public /* synthetic */ List getStreamKeys(List r1) {
        return MediaPeriod.CC.$default$getStreamKeys(this, r1);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return this.tracks;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean isLoading() {
        return !this.loadingFinished.get();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void maybeThrowPrepareError() {
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void prepare(MediaPeriod.Callback r4, long r5) {
        r4.onPrepared(this);
        this.loadingFuture = this.externalLoader.load(new ExternalLoader.LoadRequest(this.uri));
        Futures.addCallback(this.loadingFuture, new C03331(this), MoreExecutors.directExecutor());
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long readDiscontinuity() {
        return C0180C.TIME_UNSET;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public void reevaluateBuffer(long r1) {
    }

    public void releasePeriod() {
        if (this.loadingFuture == null) goto L6;
        this.loadingFuture.cancel(false);
        return;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long seekToUs(long r1) {
        return r1;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long selectTracks(ExoTrackSelection[] r4, boolean[] r5, SampleStream[] r6, boolean[] r7, long r8) {
        int r0 = 0;
    L4:
        if (r0 >= r4.length) goto L18;
        if (r6[r0] == null) goto L13;
        if (r4[r0] != null) goto L10;
    L11:
        r6[r0] = null;
        goto L13
    L10:
        if (r5[r0] == false) goto L11;
    L13:
        if (r6[r0] != null) goto L17;
        if (r4[r0] == null) goto L17;
        r6[r0] = new SampleStreamImpl(this);
        r7[r0] = true;
    L17:
        r0 = r0 + 1;
        goto L4
    L18:
        return r8;
    }
}
