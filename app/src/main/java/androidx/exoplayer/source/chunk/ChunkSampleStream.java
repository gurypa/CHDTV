package androidx.media3.exoplayer.source.chunk;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import androidx.media3.exoplayer.drm.DrmSessionManager;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MediaLoadData;
import androidx.media3.exoplayer.source.MediaSourceEventListener;
import androidx.media3.exoplayer.source.SampleQueue;
import androidx.media3.exoplayer.source.SampleStream;
import androidx.media3.exoplayer.source.SequenceableLoader;
import androidx.media3.exoplayer.source.chunk.ChunkSource;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.Loader;
import androidx.media3.exoplayer.util.ReleasableExecutor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ChunkSampleStream<T extends ChunkSource> implements SampleStream, SequenceableLoader, Loader.Callback<Chunk>, Loader.ReleaseCallback {
    private static final String TAG = "ChunkSampleStream";
    private final SequenceableLoader.Callback<ChunkSampleStream<T>> callback;
    private boolean canReportInitialDiscontinuity;
    private BaseMediaChunk canceledMediaChunk;
    private final BaseMediaChunkOutput chunkOutput;
    private final T chunkSource;
    private final SampleQueue[] embeddedSampleQueues;
    private final Format[] embeddedTrackFormats;
    private final int[] embeddedTrackTypes;
    private final boolean[] embeddedTracksSelected;
    private boolean hasInitialDiscontinuity;
    private long lastSeekPositionUs;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private final Loader loader;
    private Chunk loadingChunk;
    boolean loadingFinished;
    private final ArrayList<BaseMediaChunk> mediaChunks;
    private final MediaSourceEventListener.EventDispatcher mediaSourceEventDispatcher;
    private final ChunkHolder nextChunkHolder;
    private int nextNotifyPrimaryFormatMediaChunkIndex;
    private long pendingResetPositionUs;
    private Format primaryDownstreamTrackFormat;
    private final SampleQueue primarySampleQueue;
    public final int primaryTrackType;
    private final List<BaseMediaChunk> readOnlyMediaChunks;
    private ReleaseCallback<T> releaseCallback;

    public final class EmbeddedSampleStream implements SampleStream {
        private final int index;
        private boolean notifiedDownstreamFormat;
        public final ChunkSampleStream<T> parent;
        private final SampleQueue sampleQueue;
        final /* synthetic */ ChunkSampleStream this$0;

        public EmbeddedSampleStream(ChunkSampleStream r1, ChunkSampleStream<T> r2, SampleQueue r3, int r4) {
            this.this$0 = r1;
            this.parent = r2;
            this.sampleQueue = r3;
            this.index = r4;
        }

        private void maybeNotifyDownstreamFormat() {
            if (this.notifiedDownstreamFormat == true) goto L6;
            ChunkSampleStream.access$500(this.this$0).downstreamFormatChanged(ChunkSampleStream.access$200(this.this$0)[this.index], ChunkSampleStream.access$300(this.this$0)[this.index], 0, null, ChunkSampleStream.access$400(this.this$0));
            this.notifiedDownstreamFormat = true;
            return;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public boolean isReady() {
            if (this.this$0.isPendingReset() == false) goto L5;
        L7:
            return false;
        L5:
            if (this.sampleQueue.isReady(this.this$0.loadingFinished) == false) goto L7;
            return true;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public void maybeThrowError() {
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int readData(FormatHolder r4, DecoderInputBuffer r5, int r6) {
            if (this.this$0.isPendingReset() == false) goto L6;
            return -3;
        L6:
            if (ChunkSampleStream.access$000(this.this$0) != null) goto L8;
        L10:
            maybeNotifyDownstreamFormat();
            return this.sampleQueue.read(r4, r5, r6, this.this$0.loadingFinished);
        L8:
            if (ChunkSampleStream.access$000(this.this$0).getFirstSampleIndex(this.index + 1) > this.sampleQueue.getReadIndex()) goto L10;
            return -3;
        }

        public void release() {
            Assertions.checkState(ChunkSampleStream.access$100(this.this$0)[this.index]);
            ChunkSampleStream.access$100(this.this$0)[this.index] = false;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int skipData(long r4) {
            if (this.this$0.isPendingReset() == false) goto L6;
            return 0;
        L6:
            int r0 = this.sampleQueue.getSkipCount(r4, this.this$0.loadingFinished);
            if (ChunkSampleStream.access$000(this.this$0) == null) goto L9;
            r0 = Math.min(r0, ChunkSampleStream.access$000(this.this$0).getFirstSampleIndex(this.index + 1) - this.sampleQueue.getReadIndex());
        L9:
            this.sampleQueue.skip(r0);
            if (r0 <= 0) goto L12;
            maybeNotifyDownstreamFormat();
        L12:
            return r0;
        }
    }

    public interface ReleaseCallback<T extends ChunkSource> {
        void onSampleStreamReleased(ChunkSampleStream<T> r1);
    }

    public ChunkSampleStream(int r20, int[] r21, Format[] r22, T r23, SequenceableLoader.Callback<ChunkSampleStream<T>> r24, Allocator r25, long r26, DrmSessionManager r28, DrmSessionEventListener.EventDispatcher r29, LoadErrorHandlingPolicy r30, MediaSourceEventListener.EventDispatcher r31, boolean r32, ReleasableExecutor r33) {
        this.primaryTrackType = r20;
        if (r21 != null) goto L5;
        int[] r6 = new int[0];
    L6:
        this.embeddedTrackTypes = r6;
        if (r22 != null) goto L9;
        Format[] r7 = new Format[0];
    L10:
        this.embeddedTrackFormats = r7;
        this.chunkSource = r23;
        this.callback = r24;
        this.mediaSourceEventDispatcher = r31;
        this.loadErrorHandlingPolicy = r30;
        this.canReportInitialDiscontinuity = r32;
        if (r33 == null) goto L13;
        Loader r11 = new Loader(r33);
    L14:
        this.loader = r11;
        this.nextChunkHolder = new ChunkHolder();
        this.mediaChunks = new ArrayList();
        this.readOnlyMediaChunks = Collections.unmodifiableList(this.mediaChunks);
        int r12 = this.embeddedTrackTypes.length;
        this.embeddedSampleQueues = new SampleQueue[r12];
        this.embeddedTracksSelected = new boolean[r12];
        int[] r13 = new int[r12 + 1];
        SampleQueue[] r14 = new SampleQueue[r12 + 1];
        this.primarySampleQueue = SampleQueue.createWithDrm(r25, r28, r29);
        r13[0] = r20;
        r14[0] = this.primarySampleQueue;
        int r1 = 0;
    L15:
        if (r1 >= r12) goto L17;
        SampleQueue r16 = SampleQueue.createWithoutDrm(r25);
        int r17 = r1;
        this.embeddedSampleQueues[r17] = r16;
        r14[r17 + 1] = r16;
        r13[r17 + 1] = this.embeddedTrackTypes[r17];
        r1 = r17 + 1;
        goto L15
    L17:
        this.chunkOutput = new BaseMediaChunkOutput(r13, r14);
        this.pendingResetPositionUs = r26;
        this.lastSeekPositionUs = r26;
        return;
    L13:
        r11 = new Loader(TAG);
        goto L14
    L9:
        r7 = r22;
        goto L10
    L5:
        r6 = r21;
        goto L6
    }

    static /* synthetic */ BaseMediaChunk access$000(ChunkSampleStream r1) {
        return r1.canceledMediaChunk;
    }

    static /* synthetic */ boolean[] access$100(ChunkSampleStream r1) {
        return r1.embeddedTracksSelected;
    }

    static /* synthetic */ int[] access$200(ChunkSampleStream r1) {
        return r1.embeddedTrackTypes;
    }

    static /* synthetic */ Format[] access$300(ChunkSampleStream r1) {
        return r1.embeddedTrackFormats;
    }

    static /* synthetic */ long access$400(ChunkSampleStream r2) {
        return r2.lastSeekPositionUs;
    }

    static /* synthetic */ MediaSourceEventListener.EventDispatcher access$500(ChunkSampleStream r1) {
        return r1.mediaSourceEventDispatcher;
    }

    private void discardDownstreamMediaChunks(int r4) {
        int r1 = Math.min(primarySampleIndexToMediaChunkIndex(r4, 0), this.nextNotifyPrimaryFormatMediaChunkIndex);
        if (r1 <= 0) goto L6;
        Util.removeRange(this.mediaChunks, 0, r1);
        this.nextNotifyPrimaryFormatMediaChunkIndex -= r1;
        return;
    }

    private void discardUpstream(int r10) {
        Assertions.checkState(!this.loader.isLoading());
        int r0 = this.mediaChunks.size();
        int r1 = -1;
        int r2 = r10;
    L3:
        if (r2 >= r0) goto L9;
        if (haveReadFromMediaChunk(r2) == false) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        r1 = r2;
    L9:
        if (r1 != (-1)) goto L11;
        return;
    L11:
        long r7 = getLastMediaChunk().endTimeUs;
        BaseMediaChunk r3 = discardUpstreamMediaChunksFromIndex(r1);
        if (this.mediaChunks.isEmpty() == false) goto L14;
        this.pendingResetPositionUs = this.lastSeekPositionUs;
    L14:
        this.loadingFinished = false;
        this.mediaSourceEventDispatcher.upstreamDiscarded(this.primaryTrackType, r3.startTimeUs, r7);
    }

    private BaseMediaChunk discardUpstreamMediaChunksFromIndex(int r5) {
        BaseMediaChunk r0 = this.mediaChunks.get(r5);
        Util.removeRange(this.mediaChunks, r5, this.mediaChunks.size());
        this.nextNotifyPrimaryFormatMediaChunkIndex = Math.max(this.nextNotifyPrimaryFormatMediaChunkIndex, this.mediaChunks.size());
        this.primarySampleQueue.discardUpstreamSamples(r0.getFirstSampleIndex(0));
        int r1 = 0;
    L4:
        if (r1 >= this.embeddedSampleQueues.length) goto L6;
        this.embeddedSampleQueues[r1].discardUpstreamSamples(r0.getFirstSampleIndex(r1 + 1));
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    private BaseMediaChunk getLastMediaChunk() {
        return this.mediaChunks.get(this.mediaChunks.size() - 1);
    }

    private boolean haveReadFromMediaChunk(int r7) {
        BaseMediaChunk r0 = this.mediaChunks.get(r7);
        if (this.primarySampleQueue.getReadIndex() <= r0.getFirstSampleIndex(0)) goto L5;
        return true;
    L5:
        int r1 = 0;
    L7:
        if (r1 >= this.embeddedSampleQueues.length) goto L12;
        if (this.embeddedSampleQueues[r1].getReadIndex() > r0.getFirstSampleIndex(r1 + 1)) goto L10;
        r1 = r1 + 1;
        goto L7
    L10:
        return true;
    L12:
        return false;
    }

    private boolean isMediaChunk(Chunk r2) {
        return r2 instanceof BaseMediaChunk;
    }

    private void maybeNotifyPrimaryTrackFormatChanged() {
        int r1 = primarySampleIndexToMediaChunkIndex(this.primarySampleQueue.getReadIndex(), this.nextNotifyPrimaryFormatMediaChunkIndex - 1);
    L4:
        if (this.nextNotifyPrimaryFormatMediaChunkIndex > r1) goto L6;
        int r2 = this.nextNotifyPrimaryFormatMediaChunkIndex;
        this.nextNotifyPrimaryFormatMediaChunkIndex = r2 + 1;
        maybeNotifyPrimaryTrackFormatChanged(r2);
        goto L4
    }

    private void maybeNotifyPrimaryTrackFormatChanged(int r9) {
        BaseMediaChunk r0 = this.mediaChunks.get(r9);
        Format r3 = r0.trackFormat;
        if (r3.equals(this.primaryDownstreamTrackFormat) == true) goto L5;
        this.mediaSourceEventDispatcher.downstreamFormatChanged(this.primaryTrackType, r3, r0.trackSelectionReason, r0.trackSelectionData, r0.startTimeUs);
    L5:
        this.primaryDownstreamTrackFormat = r3;
    }

    private int primarySampleIndexToMediaChunkIndex(int r4, int r5) {
        int r0 = r5 + 1;
    L4:
        if (r0 >= this.mediaChunks.size()) goto L11;
        if (this.mediaChunks.get(r0).getFirstSampleIndex(0) > r4) goto L8;
        r0 = r0 + 1;
        goto L4
    L8:
        return r0 - 1;
    L11:
        return this.mediaChunks.size() - 1;
    }

    private void resetSampleQueues() {
        this.primarySampleQueue.reset();
        SampleQueue[] r0 = this.embeddedSampleQueues;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].reset();
        r2 = r2 + 1;
        goto L3
    }

    public boolean consumeInitialDiscontinuity() {
        boolean r1 = this.hasInitialDiscontinuity;     // Catch: Throwable -> L6
        this.hasInitialDiscontinuity = false;
        return r1;
    L6:
        th = move-exception;
        this.hasInitialDiscontinuity = false;
        throw th;
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    public boolean continueLoading(LoadingInfo r36) {
        if (this.loadingFinished == false) goto L5;
    L41:
        return false;
    L5:
        if (this.loader.isLoading() == true) goto L41;
        if (this.loader.hasFatalError() == true) goto L41;
        boolean r1 = isPendingReset();
        if (r1 == false) goto L12;
        List<BaseMediaChunk> r10 = Collections.emptyList();
        long r8 = this.pendingResetPositionUs;
    L13:
        this.chunkSource.getNextChunk(r36, r8, r10, this.nextChunkHolder);
        boolean r3 = this.nextChunkHolder.endOfStream;
        Chunk r4 = this.nextChunkHolder.chunk;
        this.nextChunkHolder.clear();
        boolean r7 = true;
        if (r3 == false) goto L17;
        this.pendingResetPositionUs = C0180C.TIME_UNSET;
        this.loadingFinished = true;
        return true;
    L17:
        if (r4 != null) goto L19;
        return false;
    L19:
        this.loadingChunk = r4;
        if (isMediaChunk(r4) == false) goto L35;
        BaseMediaChunk r11 = (BaseMediaChunk) r4;
        if (r1 == true) goto L24;
        boolean r9 = true;
    L34:
        r11.init(this.chunkOutput);
        this.mediaChunks.add(r11);
    L39:
        this.mediaSourceEventDispatcher.loadStarted(new LoadEventInfo(r4.loadTaskId, r4.dataSpec, this.loader.startLoading(r4, this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(r4.type))), r4.type, this.primaryTrackType, r4.trackFormat, r4.trackSelectionReason, r4.trackSelectionData, r4.startTimeUs, r4.endTimeUs);
        return r9;
    L24:
        if (r11.startTimeUs >= this.pendingResetPositionUs) goto L31;
        this.primarySampleQueue.setStartTimeUs(this.pendingResetPositionUs);
        SampleQueue[] r12 = this.embeddedSampleQueues;
        int r13 = r12.length;
        int r14 = 0;
    L26:
        if (r14 >= r13) goto L28;
        r12[r14].setStartTimeUs(this.pendingResetPositionUs);
        r14 = r14 + 1;
        r7 = r7;
        r8 = r8;
        goto L26
    L28:
        r9 = r7;
        if (this.canReportInitialDiscontinuity == false) goto L32;
        this.hasInitialDiscontinuity = !MimeTypes.allSamplesAreSyncSamples(r11.trackFormat.sampleMimeType, r11.trackFormat.codecs);
    L32:
        this.canReportInitialDiscontinuity = false;
        this.pendingResetPositionUs = C0180C.TIME_UNSET;
        goto L34
    L31:
        r9 = true;
        goto L32
    L35:
        r9 = true;
        if ((r4 instanceof InitializationChunk) == false) goto L39;
        ((InitializationChunk) r4).init(this.chunkOutput);
        goto L39
    L12:
        r10 = this.readOnlyMediaChunks;
        r8 = getLastMediaChunk().endTimeUs;
        goto L13
    }

    public void discardBuffer(long r8, boolean r10) {
        if (isPendingReset() == false) goto L5;
        return;
    L5:
        int r0 = this.primarySampleQueue.getFirstIndex();
        this.primarySampleQueue.discardTo(r8, r10, true);
        int r1 = this.primarySampleQueue.getFirstIndex();
        if (r1 <= r0) goto L11;
        long r2 = this.primarySampleQueue.getFirstTimestampUs();
        int r4 = 0;
    L9:
        if (r4 >= this.embeddedSampleQueues.length) goto L11;
        this.embeddedSampleQueues[r4].discardTo(r2, r10, this.embeddedTracksSelected[r4]);
        r4 = r4 + 1;
    L11:
        discardDownstreamMediaChunks(r1);
    }

    public long getAdjustedSeekPositionUs(long r3, SeekParameters r5) {
        return this.chunkSource.getAdjustedSeekPositionUs(r3, r5);
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    public long getBufferedPositionUs() {
        if (this.loadingFinished == false) goto L7;
        return Long.MIN_VALUE;
    L7:
        if (isPendingReset() == true) goto L9;
        long r0 = this.lastSeekPositionUs;
        BaseMediaChunk r2 = getLastMediaChunk();
        if (r2.isLoadCompleted() == false) goto L14;
        BaseMediaChunk r3 = r2;
    L18:
        if (r3 == null) goto L21;
        r0 = Math.max(r0, r3.endTimeUs);
    L21:
        return Math.max(r0, this.primarySampleQueue.getLargestQueuedTimestampUs());
    L14:
        if (this.mediaChunks.size() <= 1) goto L16;
        r3 = this.mediaChunks.get(this.mediaChunks.size() - 2);
        goto L18
    L16:
        r3 = null;
        goto L18
    L9:
        return this.pendingResetPositionUs;
    }

    public T getChunkSource() {
        return this.chunkSource;
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        if (isPendingReset() == false) goto L7;
        return this.pendingResetPositionUs;
    L7:
        if (this.loadingFinished == false) goto L10;
        return Long.MIN_VALUE;
    L10:
        return getLastMediaChunk().endTimeUs;
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    public boolean isLoading() {
        return this.loader.isLoading();
    }

    boolean isPendingReset() {
        if (this.pendingResetPositionUs == C0180C.TIME_UNSET) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.exoplayer.source.SampleStream
    public boolean isReady() {
        if (isPendingReset() == false) goto L5;
    L7:
        return false;
    L5:
        if (this.primarySampleQueue.isReady(this.loadingFinished) == false) goto L7;
        return true;
    }

    @Override // androidx.media3.exoplayer.source.SampleStream
    public void maybeThrowError() throws IOException {
        this.loader.maybeThrowError();
        this.primarySampleQueue.maybeThrowError();
        if (this.loader.isLoading() == true) goto L6;
        this.chunkSource.maybeThrowError();
        return;
    }

    public void onLoadCanceled(Chunk r14, long r15, long r17, boolean r19) {
        this.loadingChunk = null;
        this.canceledMediaChunk = null;
        LoadEventInfo r1 = new LoadEventInfo(r14.loadTaskId, r14.dataSpec, r14.getUri(), r14.getResponseHeaders(), r15, r17, r14.bytesLoaded());
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r14.loadTaskId);
        this.mediaSourceEventDispatcher.loadCanceled(r1, r14.type, this.primaryTrackType, r14.trackFormat, r14.trackSelectionReason, r14.trackSelectionData, r14.startTimeUs, r14.endTimeUs);
        if (r19 == false) goto L5;
        return;
    L5:
        if (isPendingReset() == false) goto L8;
        resetSampleQueues();
    L12:
        this.callback.onContinueLoadingRequested(this);
        return;
    L8:
        if (isMediaChunk(r14) == false) goto L12;
        discardUpstreamMediaChunksFromIndex(this.mediaChunks.size() - 1);
        if (this.mediaChunks.isEmpty() == false) goto L12;
        this.pendingResetPositionUs = this.lastSeekPositionUs;
        goto L12
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ void onLoadCanceled(Loader.Loadable r8, long r9, long r11, boolean r13) {
        onLoadCanceled((Chunk) r8, r9, r11, r13);
    }

    public void onLoadCompleted(Chunk r14, long r15, long r17) {
        this.loadingChunk = null;
        this.chunkSource.onChunkLoadCompleted(r14);
        LoadEventInfo r1 = new LoadEventInfo(r14.loadTaskId, r14.dataSpec, r14.getUri(), r14.getResponseHeaders(), r15, r17, r14.bytesLoaded());
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r14.loadTaskId);
        this.mediaSourceEventDispatcher.loadCompleted(r1, r14.type, this.primaryTrackType, r14.trackFormat, r14.trackSelectionReason, r14.trackSelectionData, r14.startTimeUs, r14.endTimeUs);
        this.callback.onContinueLoadingRequested(this);
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ void onLoadCompleted(Loader.Loadable r7, long r8, long r10) {
        onLoadCompleted((Chunk) r7, r8, r10);
    }

    public Loader.LoadErrorAction onLoadError(Chunk r32, long r33, long r35, IOException r37, int r38) {
        long r12 = r32.bytesLoaded();
        boolean r14 = isMediaChunk(r32);
        int r2 = this.mediaChunks.size() - 1;
        if (r12 == 0) goto L9;
        if (r14 == false) goto L9;
        if (haveReadFromMediaChunk(r2) == false) goto L9;
        boolean r3 = false;
    L10:
        boolean r6 = r3;
        LoadEventInfo r4 = new LoadEventInfo(r32.loadTaskId, r32.dataSpec, r32.getUri(), r32.getResponseHeaders(), r33, r35, r12);
        LoadErrorHandlingPolicy.LoadErrorInfo r5 = new LoadErrorHandlingPolicy.LoadErrorInfo(r4, new MediaLoadData(r32.type, this.primaryTrackType, r32.trackFormat, r32.trackSelectionReason, r32.trackSelectionData, Util.usToMs(r32.startTimeUs), Util.usToMs(r32.endTimeUs)), r37, r38);
        Loader.LoadErrorAction r7 = null;
        if (this.chunkSource.onChunkLoadError(r32, r6, r5, this.loadErrorHandlingPolicy) == false) goto L24;
        if (r6 == false) goto L23;
        r7 = Loader.DONT_RETRY;
        if (r14 == false) goto L24;
        if (discardUpstreamMediaChunksFromIndex(r2) != r32) goto L18;
        boolean r9 = true;
    L19:
        Assertions.checkState(r9);
        if (this.mediaChunks.isEmpty() == false) goto L24;
        this.pendingResetPositionUs = this.lastSeekPositionUs;
        goto L24
    L18:
        r9 = false;
        goto L19
    L23:
        Log.m48w(TAG, "Ignoring attempt to cancel non-cancelable load.");
    L24:
        if (r7 != null) goto L30;
        long r8 = this.loadErrorHandlingPolicy.getRetryDelayMsFor(r5);
        if (r8 == C0180C.TIME_UNSET) goto L28;
        Loader.LoadErrorAction r10 = Loader.createRetryAction(false, r8);
    L29:
        r7 = r10;
        goto L30
    L28:
        r10 = Loader.DONT_RETRY_FATAL;
    L30:
        boolean r30 = !r7.isRetry();
        this.mediaSourceEventDispatcher.loadError(r4, r32.type, this.primaryTrackType, r32.trackFormat, r32.trackSelectionReason, r32.trackSelectionData, r32.startTimeUs, r32.endTimeUs, r37, r30);
        if (r30 == false) goto L33;
        this.loadingChunk = null;
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r32.loadTaskId);
        this.callback.onContinueLoadingRequested(this);
    L33:
        return r7;
    L9:
        r3 = true;
        goto L10
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ Loader.LoadErrorAction onLoadError(Loader.Loadable r9, long r10, long r12, IOException r14, int r15) {
        return onLoadError((Chunk) r9, r10, r12, r14, r15);
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* synthetic */ void onLoadStarted(Loader.Loadable r1, long r2, long r4, int r6) {
        Loader.Callback.CC.$default$onLoadStarted(this, r1, r2, r4, r6);
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.ReleaseCallback
    public void onLoaderReleased() {
        this.primarySampleQueue.release();
        SampleQueue[] r0 = this.embeddedSampleQueues;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].release();
        r2 = r2 + 1;
        goto L3
    L5:
        this.chunkSource.release();
        if (this.releaseCallback == null) goto L10;
        this.releaseCallback.onSampleStreamReleased(this);
        return;
    }

    @Override // androidx.media3.exoplayer.source.SampleStream
    public int readData(FormatHolder r4, DecoderInputBuffer r5, int r6) {
        if (isPendingReset() == false) goto L6;
        return -3;
    L6:
        if (this.canceledMediaChunk != null) goto L8;
    L10:
        maybeNotifyPrimaryTrackFormatChanged();
        return this.primarySampleQueue.read(r4, r5, r6, this.loadingFinished);
    L8:
        if (this.canceledMediaChunk.getFirstSampleIndex(0) > this.primarySampleQueue.getReadIndex()) goto L10;
        return -3;
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    public void reevaluateBuffer(long r4) {
        if (this.loader.hasFatalError() == false) goto L5;
        return;
    L5:
        if (isPendingReset() == false) goto L8;
        return;
    L8:
        if (this.loader.isLoading() == false) goto L20;
        Chunk r0 = (Chunk) Assertions.checkNotNull(this.loadingChunk);
        if (isMediaChunk(r0) == false) goto L15;
        if (haveReadFromMediaChunk(this.mediaChunks.size() - 1) == false) goto L15;
        return;
    L15:
        if (this.chunkSource.shouldCancelLoad(r4, r0, this.readOnlyMediaChunks) == false) goto L25;
        this.loader.cancelLoading();
        if (isMediaChunk(r0) == false) goto L26;
        this.canceledMediaChunk = (BaseMediaChunk) r0;
        return;
    L26:
        return;
    L25:
        return;
    L20:
        int r1 = this.chunkSource.getPreferredQueueSize(r4, this.readOnlyMediaChunks);
        if (r1 >= this.mediaChunks.size()) goto L27;
        discardUpstream(r1);
        return;
    }

    public void release() {
        release(null);
    }

    public void release(ReleaseCallback<T> r5) {
        this.releaseCallback = r5;
        this.primarySampleQueue.preRelease();
        SampleQueue[] r0 = this.embeddedSampleQueues;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].preRelease();
        r2 = r2 + 1;
        goto L3
    L5:
        this.loader.release(this);
    }

    public void seekToUs(long r11) {
        this.lastSeekPositionUs = r11;
        int r0 = 0;
        this.canReportInitialDiscontinuity = false;
        if (isPendingReset() == false) goto L6;
        this.pendingResetPositionUs = r11;
        return;
    L6:
        BaseMediaChunk r1 = null;
        int r2 = 0;
    L8:
        if (r2 >= this.mediaChunks.size()) goto L19;
        BaseMediaChunk r3 = this.mediaChunks.get(r2);
        long r4 = r3.startTimeUs;
        if (r4 != r11) goto L15;
        if (r3.clippedStartTimeUs != C0180C.TIME_UNSET) goto L15;
        r1 = r3;
    L15:
        if (r4 > r11) goto L19;
        r2 = r2 + 1;
    L19:
        if (r1 == null) goto L21;
        boolean r5 = this.primarySampleQueue.seekTo(r1.getFirstSampleIndex(0));
    L26:
        if (r5 == false) goto L30;
        this.nextNotifyPrimaryFormatMediaChunkIndex = primarySampleIndexToMediaChunkIndex(this.primarySampleQueue.getReadIndex(), 0);
        SampleQueue[] r6 = this.embeddedSampleQueues;
        int r7 = r6.length;
    L28:
        if (r0 >= r7) goto L44;
        r6[r0].seekTo(r11, true);
        r0 = r0 + 1;
        goto L28
    L44:
        return;
    L30:
        this.pendingResetPositionUs = r11;
        this.loadingFinished = false;
        this.mediaChunks.clear();
        this.nextNotifyPrimaryFormatMediaChunkIndex = 0;
        if (this.loader.isLoading() == false) goto L36;
        this.primarySampleQueue.discardToEnd();
        SampleQueue[] r8 = this.embeddedSampleQueues;
        int r9 = r8.length;
    L33:
        if (r0 >= r9) goto L35;
        r8[r0].discardToEnd();
        r0 = r0 + 1;
        goto L33
    L35:
        this.loader.cancelLoading();
        return;
    L36:
        this.loader.clearFatalError();
        resetSampleQueues();
        return;
    L21:
        SampleQueue r10 = this.primarySampleQueue;
        if (r11 >= getNextLoadPositionUs()) goto L24;
        boolean r12 = true;
    L25:
        r5 = r10.seekTo(r11, r12);
        goto L26
    L24:
        r12 = false;
        goto L25
    }

    public ChunkSampleStream<T>.EmbeddedSampleStream selectEmbeddedTrack(long r4, int r6) {
        int r0 = 0;
    L4:
        if (r0 >= this.embeddedSampleQueues.length) goto L11;
        if (this.embeddedTrackTypes[r0] == r6) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        Assertions.checkState(!this.embeddedTracksSelected[r0]);
        this.embeddedTracksSelected[r0] = true;
        this.embeddedSampleQueues[r0].seekTo(r4, true);
        return new EmbeddedSampleStream(this, this, this.embeddedSampleQueues[r0], r0);
    L11:
        throw new IllegalStateException();
    }

    @Override // androidx.media3.exoplayer.source.SampleStream
    public int skipData(long r4) {
        if (isPendingReset() == false) goto L5;
        return 0;
    L5:
        int r0 = this.primarySampleQueue.getSkipCount(r4, this.loadingFinished);
        if (this.canceledMediaChunk == null) goto L8;
        r0 = Math.min(r0, this.canceledMediaChunk.getFirstSampleIndex(0) - this.primarySampleQueue.getReadIndex());
    L8:
        this.primarySampleQueue.skip(r0);
        maybeNotifyPrimaryTrackFormatChanged();
        return r0;
    }
}
