package androidx.media3.exoplayer.source;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSourceUtil;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.StatsDataSource;
import androidx.media3.datasource.TransferListener;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.Loader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class SingleSampleMediaPeriod implements MediaPeriod, Loader.Callback<SourceLoadable> {
    private static final int INITIAL_SAMPLE_SIZE = 1024;
    private static final String TAG = "SingleSampleMediaPeriod";
    private final DataSource.Factory dataSourceFactory;
    private final DataSpec dataSpec;
    private final long durationUs;
    private final MediaSourceEventListener.EventDispatcher eventDispatcher;
    final Format format;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    final Loader loader;
    boolean loadingFinished;
    byte[] sampleData;
    int sampleSize;
    private final ArrayList<SampleStreamImpl> sampleStreams;
    private final TrackGroupArray tracks;
    private final TransferListener transferListener;
    final boolean treatLoadErrorsAsEndOfStream;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.SingleSampleMediaPeriod$1 */
    static /* synthetic */ class C03491 {
    }

    private final class SampleStreamImpl implements SampleStream {
        private static final int STREAM_STATE_END_OF_STREAM = 2;
        private static final int STREAM_STATE_SEND_FORMAT = 0;
        private static final int STREAM_STATE_SEND_SAMPLE = 1;
        private boolean notifiedDownstreamFormat;
        private int streamState;
        final /* synthetic */ SingleSampleMediaPeriod this$0;

        private SampleStreamImpl(SingleSampleMediaPeriod r1) {
            this.this$0 = r1;
        }

        /* synthetic */ SampleStreamImpl(SingleSampleMediaPeriod r1, C03491 r2) {
            this(r1);
        }

        private void maybeNotifyDownstreamFormat() {
            if (this.notifiedDownstreamFormat == true) goto L6;
            SingleSampleMediaPeriod.access$300(this.this$0).downstreamFormatChanged(MimeTypes.getTrackType(this.this$0.format.sampleMimeType), this.this$0.format, 0, null, 0);
            this.notifiedDownstreamFormat = true;
            return;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public boolean isReady() {
            return this.this$0.loadingFinished;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public void maybeThrowError() throws IOException {
            if (this.this$0.treatLoadErrorsAsEndOfStream == true) goto L6;
            this.this$0.loader.maybeThrowError();
            return;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int readData(FormatHolder r7, DecoderInputBuffer r8, int r9) {
            maybeNotifyDownstreamFormat();
            if (this.this$0.loadingFinished == false) goto L8;
            if (this.this$0.sampleData != null) goto L8;
            this.streamState = 2;
        L8:
            if (this.streamState != 2) goto L12;
            r8.addFlag(4);
            return -4;
        L12:
            if ((r9 & 2) == 0) goto L14;
        L27:
            r7.format = this.this$0.format;
            this.streamState = 1;
            return -5;
        L14:
            if (this.streamState == 0) goto L27;
            if (this.this$0.loadingFinished == true) goto L20;
            return -3;
        L20:
            Assertions.checkNotNull(this.this$0.sampleData);
            r8.addFlag(1);
            r8.timeUs = 0;
            if ((r9 & 4) != 0) goto L24;
            r8.ensureSpaceForWrite(this.this$0.sampleSize);
            r8.data.put(this.this$0.sampleData, 0, this.this$0.sampleSize);
        L24:
            if ((r9 & 1) != 0) goto L26;
            this.streamState = 2;
        L26:
            return -4;
        }

        public void reset() {
            if (this.streamState != 2) goto L6;
            this.streamState = 1;
            return;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int skipData(long r3) {
            maybeNotifyDownstreamFormat();
            if (r3 > 0) goto L5;
            return 0;
        L5:
            if (this.streamState == 2) goto L10;
            this.streamState = 2;
            return 1;
        L10:
            return 0;
        }
    }

    static final class SourceLoadable implements Loader.Loadable {
        private final StatsDataSource dataSource;
        public final DataSpec dataSpec;
        public final long loadTaskId;
        private byte[] sampleData;

        public SourceLoadable(DataSpec r3, DataSource r4) {
            this.loadTaskId = LoadEventInfo.getNewId();
            this.dataSpec = r3;
            this.dataSource = new StatsDataSource(r4);
        }

        static /* synthetic */ StatsDataSource access$100(SourceLoadable r1) {
            return r1.dataSource;
        }

        static /* synthetic */ byte[] access$200(SourceLoadable r1) {
            return r1.sampleData;
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
        public void cancelLoad() {
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
        public void load() throws IOException {
            this.dataSource.resetBytesRead();
            this.dataSource.open(this.dataSpec);     // Catch: Throwable -> L16
            int r0 = 0;
        L5:
            if (r0 == (-1)) goto L14;
            int r1 = (int) this.dataSource.getBytesRead();     // Catch: Throwable -> L16
            if (this.sampleData != null) goto L10;
            this.sampleData = new byte[1024];     // Catch: Throwable -> L16
        L13:
            r0 = this.dataSource.read(this.sampleData, r1, this.sampleData.length - r1);     // Catch: Throwable -> L16
            goto L5
        L10:
            if (r1 != this.sampleData.length) goto L13;
            this.sampleData = Arrays.copyOf(this.sampleData, this.sampleData.length * 2);     // Catch: Throwable -> L16
            goto L13
        L14:
            DataSourceUtil.closeQuietly(this.dataSource);
            return;
        L16:
            th = move-exception;
            DataSourceUtil.closeQuietly(this.dataSource);
            throw th;
        }
    }

    public SingleSampleMediaPeriod(DataSpec r6, DataSource.Factory r7, TransferListener r8, Format r9, long r10, LoadErrorHandlingPolicy r12, MediaSourceEventListener.EventDispatcher r13, boolean r14) {
        this.dataSpec = r6;
        this.dataSourceFactory = r7;
        this.transferListener = r8;
        this.format = r9;
        this.durationUs = r10;
        this.loadErrorHandlingPolicy = r12;
        this.eventDispatcher = r13;
        this.treatLoadErrorsAsEndOfStream = r14;
        this.tracks = new TrackGroupArray(new TrackGroup[]{new TrackGroup(new Format[]{r9})});
        this.sampleStreams = new ArrayList();
        this.loader = new Loader(TAG);
    }

    static /* synthetic */ MediaSourceEventListener.EventDispatcher access$300(SingleSampleMediaPeriod r1) {
        return r1.eventDispatcher;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean continueLoading(LoadingInfo r24) {
        if (this.loadingFinished == false) goto L5;
        return false;
    L5:
        if (this.loader.isLoading() == false) goto L7;
        return false;
    L7:
        if (this.loader.hasFatalError() == true) goto L17;
        DataSource r1 = this.dataSourceFactory.createDataSource();
        if (this.transferListener == null) goto L12;
        r1.addTransferListener(this.transferListener);
    L12:
        SourceLoadable r2 = new SourceLoadable(this.dataSpec, r1);
        long r10 = this.loader.startLoading(r2, this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(1));
        this.eventDispatcher.loadStarted(new LoadEventInfo(r2.loadTaskId, this.dataSpec, r10), 1, -1, this.format, 0, null, 0, this.durationUs);
        return true;
    L17:
        return false;
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
        if (this.loadingFinished == false) goto L5;
        return Long.MIN_VALUE;
    L5:
        return 0;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        if (this.loadingFinished == false) goto L5;
    L8:
        return Long.MIN_VALUE;
    L5:
        if (this.loader.isLoading() == true) goto L8;
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
        return this.loader.isLoading();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void maybeThrowPrepareError() {
    }

    public void onLoadCanceled(SourceLoadable r14, long r15, long r17, boolean r19) {
        StatsDataSource r0 = SourceLoadable.access$100(r14);
        LoadEventInfo r1 = new LoadEventInfo(r14.loadTaskId, r14.dataSpec, r0.getLastOpenedUri(), r0.getLastResponseHeaders(), r15, r17, r0.getBytesRead());
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r14.loadTaskId);
        this.eventDispatcher.loadCanceled(r1, 1, -1, null, 0, null, 0, this.durationUs);
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ void onLoadCanceled(Loader.Loadable r8, long r9, long r11, boolean r13) {
        onLoadCanceled((SourceLoadable) r8, r9, r11, r13);
    }

    public void onLoadCompleted(SourceLoadable r14, long r15, long r17) {
        this.sampleSize = (int) SourceLoadable.access$100(r14).getBytesRead();
        this.sampleData = (byte[]) Assertions.checkNotNull(SourceLoadable.access$200(r14));
        this.loadingFinished = true;
        StatsDataSource r0 = SourceLoadable.access$100(r14);
        LoadEventInfo r1 = new LoadEventInfo(r14.loadTaskId, r14.dataSpec, r0.getLastOpenedUri(), r0.getLastResponseHeaders(), r15, r17, this.sampleSize);
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r14.loadTaskId);
        this.eventDispatcher.loadCompleted(r1, 1, -1, this.format, 0, null, 0, this.durationUs);
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ void onLoadCompleted(Loader.Loadable r7, long r8, long r10) {
        onLoadCompleted((SourceLoadable) r7, r8, r10);
    }

    public Loader.LoadErrorAction onLoadError(SourceLoadable r30, long r31, long r33, IOException r35, int r36) {
        StatsDataSource r16 = SourceLoadable.access$100(r30);
        LoadEventInfo r17 = new LoadEventInfo(r30.loadTaskId, r30.dataSpec, r16.getLastOpenedUri(), r16.getLastResponseHeaders(), r31, r33, r16.getBytesRead());
        long r4 = this.loadErrorHandlingPolicy.getRetryDelayMsFor(new LoadErrorHandlingPolicy.LoadErrorInfo(r17, new MediaLoadData(1, -1, this.format, 0, null, 0, Util.usToMs(this.durationUs)), r35, r36));
        if (r4 != C0180C.TIME_UNSET) goto L5;
    L8:
        boolean r8 = true;
    L9:
        boolean r18 = r8;
        if (this.treatLoadErrorsAsEndOfStream == false) goto L14;
        if (r18 == false) goto L14;
        Log.m49w(TAG, "Loading failed, treating as end-of-stream.", r35);
        this.loadingFinished = true;
        Loader.LoadErrorAction r19 = Loader.DONT_RETRY;
    L18:
        boolean r14 = !r19.isRetry();
        this.eventDispatcher.loadError(r17, 1, -1, this.format, 0, null, 0, this.durationUs, r35, r14);
        if (r14 == false) goto L21;
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r30.loadTaskId);
    L21:
        return r19;
    L14:
        if (r4 == C0180C.TIME_UNSET) goto L16;
        Loader.LoadErrorAction r6 = Loader.createRetryAction(false, r4);
    L17:
        r19 = r6;
        goto L18
    L16:
        r6 = Loader.DONT_RETRY_FATAL;
        goto L17
    L5:
        if (r36 >= this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(1)) goto L8;
        r8 = false;
        goto L9
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ Loader.LoadErrorAction onLoadError(Loader.Loadable r9, long r10, long r12, IOException r14, int r15) {
        return onLoadError((SourceLoadable) r9, r10, r12, r14, r15);
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* synthetic */ void onLoadStarted(Loader.Loadable r1, long r2, long r4, int r6) {
        Loader.Callback.CC.$default$onLoadStarted(this, r1, r2, r4, r6);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void prepare(MediaPeriod.Callback r1, long r2) {
        r1.onPrepared(this);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long readDiscontinuity() {
        return C0180C.TIME_UNSET;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public void reevaluateBuffer(long r1) {
    }

    public void release() {
        this.loader.release();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long seekToUs(long r3) {
        int r0 = 0;
    L4:
        if (r0 >= this.sampleStreams.size()) goto L6;
        this.sampleStreams.get(r0).reset();
        r0 = r0 + 1;
        goto L4
    L6:
        return r3;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long selectTracks(ExoTrackSelection[] r5, boolean[] r6, SampleStream[] r7, boolean[] r8, long r9) {
        int r0 = 0;
    L4:
        if (r0 >= r5.length) goto L18;
        C03491 r2 = null;
        if (r7[r0] == null) goto L13;
        if (r5[r0] != null) goto L10;
    L11:
        this.sampleStreams.remove(r7[r0]);
        r7[r0] = null;
        goto L13
    L10:
        if (r6[r0] == false) goto L11;
    L13:
        if (r7[r0] != null) goto L17;
        if (r5[r0] == null) goto L17;
        SampleStreamImpl r1 = new SampleStreamImpl(this, r2);
        this.sampleStreams.add(r1);
        r7[r0] = r1;
        r8[r0] = true;
    L17:
        r0 = r0 + 1;
        goto L4
    L18:
        return r9;
    }
}
