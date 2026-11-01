package androidx.media3.exoplayer.source;

import android.net.Uri;
import android.os.Handler;
import androidx.media3.common.C0180C;
import androidx.media3.common.DataReader;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ConditionVariable;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSourceUtil;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.StatsDataSource;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import androidx.media3.exoplayer.drm.DrmSessionManager;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.Loader;
import androidx.media3.exoplayer.util.ReleasableExecutor;
import androidx.media3.extractor.DiscardingTrackOutput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.metadata.icy.IcyHeaders;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;

/* JADX INFO: loaded from: classes.dex */
final class ProgressiveMediaPeriod implements MediaPeriod, ExtractorOutput, Loader.Callback<ExtractingLoadable>, Loader.ReleaseCallback, SampleQueue.UpstreamFormatChangedListener {
    private static final long DEFAULT_LAST_SAMPLE_DURATION_US = 10000;
    private static final Format ICY_FORMAT = null;
    private static final Map<String, String> ICY_METADATA_HEADERS = null;
    private static final String TAG = "ProgressiveMediaPeriod";
    private final Allocator allocator;
    private MediaPeriod.Callback callback;
    private final long continueLoadingCheckIntervalBytes;
    private final String customCacheKey;
    private final DataSource dataSource;
    private int dataType;
    private final DrmSessionEventListener.EventDispatcher drmEventDispatcher;
    private final DrmSessionManager drmSessionManager;
    private long durationUs;
    private int enabledTrackCount;
    private int extractedSamplesCountAtStartOfLoad;
    private final Handler handler;
    private boolean haveAudioVideoTracks;
    private IcyHeaders icyHeaders;
    private boolean isLengthKnown;
    private boolean isLive;
    private boolean isSingleSample;
    private long lastSeekPositionUs;
    private final Listener listener;
    private final ConditionVariable loadCondition;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private final Loader loader;
    private boolean loadingFinished;
    private final Runnable maybeFinishPrepareRunnable;
    private final MediaSourceEventListener.EventDispatcher mediaSourceEventDispatcher;
    private boolean notifyDiscontinuity;
    private final Runnable onContinueLoadingRequestedRunnable;
    private boolean pendingDeferredRetry;
    private boolean pendingInitialDiscontinuity;
    private long pendingResetPositionUs;
    private boolean prepared;
    private final ProgressiveMediaExtractor progressiveMediaExtractor;
    private boolean released;
    private TrackId[] sampleQueueTrackIds;
    private SampleQueue[] sampleQueues;
    private boolean sampleQueuesBuilt;
    private SeekMap seekMap;
    private boolean seenFirstTrackSelection;
    private final long singleSampleDurationUs;
    private final boolean suppressPrepareError;
    private TrackState trackState;
    private final Uri uri;

    final class ExtractingLoadable implements Loader.Loadable, IcyDataSource.Listener {
        private final StatsDataSource dataSource;
        private DataSpec dataSpec;
        private final ExtractorOutput extractorOutput;
        private TrackOutput icyTrackOutput;
        private volatile boolean loadCanceled;
        private final ConditionVariable loadCondition;
        private final long loadTaskId;
        private boolean pendingExtractorSeek;
        private final PositionHolder positionHolder;
        private final ProgressiveMediaExtractor progressiveMediaExtractor;
        private long seekTimeUs;
        private boolean seenIcyMetadata;
        final /* synthetic */ ProgressiveMediaPeriod this$0;
        private final Uri uri;

        public ExtractingLoadable(ProgressiveMediaPeriod r3, Uri r4, DataSource r5, ProgressiveMediaExtractor r6, ExtractorOutput r7, ConditionVariable r8) {
            this.this$0 = r3;
            this.uri = r4;
            this.dataSource = new StatsDataSource(r5);
            this.progressiveMediaExtractor = r6;
            this.extractorOutput = r7;
            this.loadCondition = r8;
            this.positionHolder = new PositionHolder();
            this.pendingExtractorSeek = true;
            this.loadTaskId = LoadEventInfo.getNewId();
            this.dataSpec = buildDataSpec(0);
        }

        static /* synthetic */ StatsDataSource access$100(ExtractingLoadable r1) {
            return r1.dataSource;
        }

        static /* synthetic */ long access$200(ExtractingLoadable r2) {
            return r2.loadTaskId;
        }

        static /* synthetic */ DataSpec access$300(ExtractingLoadable r1) {
            return r1.dataSpec;
        }

        static /* synthetic */ long access$400(ExtractingLoadable r2) {
            return r2.seekTimeUs;
        }

        static /* synthetic */ void access$600(ExtractingLoadable r0, long r1, long r3) {
            r0.setLoadPosition(r1, r3);
        }

        private DataSpec buildDataSpec(long r3) {
            return new DataSpec.Builder().setUri(this.uri).setPosition(r3).setKey(ProgressiveMediaPeriod.access$1500(this.this$0)).setFlags(6).setHttpRequestHeaders(ProgressiveMediaPeriod.access$1400()).build();
        }

        private void setLoadPosition(long r2, long r4) {
            this.positionHolder.position = r2;
            this.seekTimeUs = r4;
            this.pendingExtractorSeek = true;
            this.seenIcyMetadata = false;
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
        public void cancelLoad() {
            this.loadCanceled = true;
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
        public void load() throws IOException {
            int r2 = 0;
        L3:
            if (r2 != 0) goto L73;
            if (this.loadCanceled == true) goto L74;
            long r12 = this.positionHolder.position;     // Catch: Throwable -> L49
            this.dataSpec = buildDataSpec(r12);     // Catch: Throwable -> L49
            long r6 = this.dataSource.open(this.dataSpec);     // Catch: Throwable -> L49
            if (this.loadCanceled == true) goto L9;
            if (r6 == (-1)) goto L19;
            long r7 = r6 + r12;
            ProgressiveMediaPeriod.access$700(this.this$0);     // Catch: Throwable -> L49
            long r14 = r7;
        L20:
            ProgressiveMediaPeriod.access$802(this.this$0, IcyHeaders.parse(this.dataSource.getResponseHeaders()));     // Catch: Throwable -> L49
            StatsDataSource r0 = this.dataSource;     // Catch: Throwable -> L49
            if (ProgressiveMediaPeriod.access$800(this.this$0) != null) goto L23;
        L25:
            DataReader r9 = r0;
        L26:
            this.progressiveMediaExtractor.init(r9, this.uri, this.dataSource.getResponseHeaders(), r12, r14, this.extractorOutput);     // Catch: Throwable -> L49
            if (ProgressiveMediaPeriod.access$800(this.this$0) == null) goto L30;
            this.progressiveMediaExtractor.disableSeekingOnMp3Streams();     // Catch: Throwable -> L49
        L30:
            if (this.pendingExtractorSeek == false) goto L32;
            this.progressiveMediaExtractor.seek(r12, this.seekTimeUs);     // Catch: Throwable -> L49
            this.pendingExtractorSeek = false;     // Catch: Throwable -> L49
        L32:
            if (r2 != 0) goto L43;
            if (this.loadCanceled == true) goto L43;
            this.loadCondition.block();     // Catch: InterruptedException -> L40 Throwable -> L49
            r2 = this.progressiveMediaExtractor.read(this.positionHolder);     // Catch: Throwable -> L49
            long r8 = this.progressiveMediaExtractor.getCurrentInputPosition();     // Catch: Throwable -> L49
            if (r8 <= (ProgressiveMediaPeriod.access$1000(this.this$0) + r12)) goto L32;
            this.loadCondition.close();     // Catch: Throwable -> L49
            ProgressiveMediaPeriod.access$1200(this.this$0).post(ProgressiveMediaPeriod.access$1100(this.this$0));     // Catch: Throwable -> L49
            r12 = r8;
            goto L32
        L42:
            throw new InterruptedIOException();     // Catch: Throwable -> L49
        L43:
            if (r2 != 1) goto L46;
            r2 = 0;
        L48:
            DataSourceUtil.closeQuietly(this.dataSource);
            goto L3
        L46:
            if (this.progressiveMediaExtractor.getCurrentInputPosition() == (-1)) goto L48;
            this.positionHolder.position = this.progressiveMediaExtractor.getCurrentInputPosition();
            goto L48
        L23:
            if (ProgressiveMediaPeriod.access$800(this.this$0).metadataInterval == (-1)) goto L25;
            IcyDataSource r10 = new IcyDataSource(this.dataSource, ProgressiveMediaPeriod.access$800(this.this$0).metadataInterval, this);     // Catch: Throwable -> L49
            this.icyTrackOutput = this.this$0.icyTrack();     // Catch: Throwable -> L49
            this.icyTrackOutput.format(ProgressiveMediaPeriod.access$900());     // Catch: Throwable -> L49
            r9 = r10;
            goto L26
        L19:
            r14 = r6;
            goto L20
        L9:
            if (r2 != 1) goto L12;
        L14:
            DataSourceUtil.closeQuietly(this.dataSource);
            return;
        L12:
            if (this.progressiveMediaExtractor.getCurrentInputPosition() == (-1)) goto L14;
            this.positionHolder.position = this.progressiveMediaExtractor.getCurrentInputPosition();
        L49:
            th = move-exception;
            if (r2 == 1) goto L55;
            if (this.progressiveMediaExtractor.getCurrentInputPosition() == (-1)) goto L55;
            this.positionHolder.position = this.progressiveMediaExtractor.getCurrentInputPosition();
        L55:
            DataSourceUtil.closeQuietly(this.dataSource);
            throw th;
        L74:
            return;
        }

        @Override // androidx.media3.exoplayer.source.IcyDataSource.Listener
        public void onIcyMetadata(ParsableByteArray r12) {
            if (this.seenIcyMetadata == true) goto L5;
            long r2 = this.seekTimeUs;
        L6:
            int r8 = r12.bytesLeft();
            TrackOutput r4 = (TrackOutput) Assertions.checkNotNull(this.icyTrackOutput);
            r4.sampleData(r12, r8);
            r4.sampleMetadata(r2, 1, r8, 0, null);
            this.seenIcyMetadata = true;
            return;
        L5:
            r2 = Math.max(ProgressiveMediaPeriod.access$1300(this.this$0, true), this.seekTimeUs);
            goto L6
        }
    }

    interface Listener {
        void onSourceInfoRefreshed(long r1, boolean r3, boolean r4);
    }

    private final class SampleStreamImpl implements SampleStream {
        final /* synthetic */ ProgressiveMediaPeriod this$0;
        private final int track;

        public SampleStreamImpl(ProgressiveMediaPeriod r1, int r2) {
            this.this$0 = r1;
            this.track = r2;
        }

        static /* synthetic */ int access$000(SampleStreamImpl r1) {
            return r1.track;
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public boolean isReady() {
            return this.this$0.isReady(this.track);
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public void maybeThrowError() throws IOException {
            this.this$0.maybeThrowError(this.track);
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int readData(FormatHolder r3, DecoderInputBuffer r4, int r5) {
            return this.this$0.readData(this.track, r3, r4, r5);
        }

        @Override // androidx.media3.exoplayer.source.SampleStream
        public int skipData(long r3) {
            return this.this$0.skipData(this.track, r3);
        }
    }

    private static final class TrackId {

        /* JADX INFO: renamed from: id */
        public final int f45id;
        public final boolean isIcyTrack;

        public TrackId(int r1, boolean r2) {
            this.f45id = r1;
            this.isIcyTrack = r2;
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if (r6 != null) goto L8;
        L17:
            return false;
        L8:
            if (getClass() != r6.getClass()) goto L17;
            TrackId r2 = (TrackId) r6;
            if (this.f45id != r2.f45id) goto L16;
            if (this.isIcyTrack != r2.isIcyTrack) goto L16;
            return true;
        L16:
            return false;
        }

        public int hashCode() {
            return (this.f45id * 31) + (this.isIcyTrack ? 1 : 0);
        }
    }

    private static final class TrackState {
        public final boolean[] trackEnabledStates;
        public final boolean[] trackIsAudioVideoFlags;
        public final boolean[] trackNotifiedDownstreamFormats;
        public final TrackGroupArray tracks;

        public TrackState(TrackGroupArray r2, boolean[] r3) {
            this.tracks = r2;
            this.trackIsAudioVideoFlags = r3;
            this.trackEnabledStates = new boolean[r2.length];
            this.trackNotifiedDownstreamFormats = new boolean[r2.length];
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$S--pTorCq-DuXYVXYm-nCp1JewM, reason: not valid java name */
    public static /* synthetic */ void m479$r8$lambda$SpTorCqDuXYVXYmnCp1JewM(ProgressiveMediaPeriod r0) {
        r0.maybeFinishPrepare();
    }

    static {
        ICY_METADATA_HEADERS = createIcyMetadataHeaders();
        ICY_FORMAT = new Format.Builder().setId("icy").setSampleMimeType(MimeTypes.APPLICATION_ICY).build();
    }

    public ProgressiveMediaPeriod(Uri r17, DataSource r18, ProgressiveMediaExtractor r19, DrmSessionManager r20, DrmSessionEventListener.EventDispatcher r21, LoadErrorHandlingPolicy r22, MediaSourceEventListener.EventDispatcher r23, Listener r24, Allocator r25, String r26, int r27, boolean r28, long r29, ReleasableExecutor r31) {
        this.uri = r17;
        this.dataSource = r18;
        this.drmSessionManager = r20;
        this.drmEventDispatcher = r21;
        this.loadErrorHandlingPolicy = r22;
        this.mediaSourceEventDispatcher = r23;
        this.listener = r24;
        this.allocator = r25;
        this.customCacheKey = r26;
        this.continueLoadingCheckIntervalBytes = r27;
        this.suppressPrepareError = r28;
        if (r31 == null) goto L5;
        Loader r13 = new Loader(r31);
    L6:
        this.loader = r13;
        this.progressiveMediaExtractor = r19;
        this.singleSampleDurationUs = r29;
        this.loadCondition = new ConditionVariable();
        this.maybeFinishPrepareRunnable = new ProgressiveMediaPeriod$$ExternalSyntheticLambda1(this);
        this.onContinueLoadingRequestedRunnable = new ProgressiveMediaPeriod$$ExternalSyntheticLambda2(this);
        this.handler = Util.createHandlerForCurrentLooper();
        this.sampleQueueTrackIds = new TrackId[0];
        this.sampleQueues = new SampleQueue[0];
        this.pendingResetPositionUs = C0180C.TIME_UNSET;
        this.dataType = 1;
        return;
    L5:
        r13 = new Loader(TAG);
        goto L6
    }

    static /* synthetic */ long access$1000(ProgressiveMediaPeriod r2) {
        return r2.continueLoadingCheckIntervalBytes;
    }

    static /* synthetic */ Runnable access$1100(ProgressiveMediaPeriod r1) {
        return r1.onContinueLoadingRequestedRunnable;
    }

    static /* synthetic */ Handler access$1200(ProgressiveMediaPeriod r1) {
        return r1.handler;
    }

    static /* synthetic */ long access$1300(ProgressiveMediaPeriod r2, boolean r3) {
        return r2.getLargestQueuedTimestampUs(r3);
    }

    static /* synthetic */ Map access$1400() {
        return ICY_METADATA_HEADERS;
    }

    static /* synthetic */ String access$1500(ProgressiveMediaPeriod r1) {
        return r1.customCacheKey;
    }

    static /* synthetic */ long access$500(ProgressiveMediaPeriod r2) {
        return r2.durationUs;
    }

    static /* synthetic */ void access$700(ProgressiveMediaPeriod r0) {
        r0.onLengthKnown();
    }

    static /* synthetic */ IcyHeaders access$800(ProgressiveMediaPeriod r1) {
        return r1.icyHeaders;
    }

    static /* synthetic */ IcyHeaders access$802(ProgressiveMediaPeriod r0, IcyHeaders r1) {
        r0.icyHeaders = r1;
        return r1;
    }

    static /* synthetic */ Format access$900() {
        return ICY_FORMAT;
    }

    @EnsuresNonNull({"trackState", "seekMap"})
    private void assertPrepared() {
        Assertions.checkState(this.prepared);
        Assertions.checkNotNull(this.trackState);
        Assertions.checkNotNull(this.seekMap);
    }

    private boolean configureRetry(ExtractingLoadable r8, int r9) {
        if (this.isLengthKnown == false) goto L5;
    L20:
        this.extractedSamplesCountAtStartOfLoad = r9;
        return true;
    L5:
        if (this.seekMap != null) goto L7;
    L9:
        int r2 = 0;
        if (this.prepared == true) goto L12;
    L15:
        this.notifyDiscontinuity = this.prepared;
        this.lastSeekPositionUs = 0;
        this.extractedSamplesCountAtStartOfLoad = 0;
        SampleQueue[] r0 = this.sampleQueues;
        int r5 = r0.length;
    L16:
        if (r2 >= r5) goto L18;
        r0[r2].reset();
        r2 = r2 + 1;
        goto L16
    L18:
        ExtractingLoadable.access$600(r8, 0, 0);
        return true;
    L12:
        if (suppressRead() == true) goto L15;
        this.pendingDeferredRetry = true;
        return false;
    L7:
        if (this.seekMap.getDurationUs() == C0180C.TIME_UNSET) goto L9;
        goto L9
    }

    private static Map<String, String> createIcyMetadataHeaders() {
        HashMap r0 = new HashMap();
        r0.put(IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_NAME, IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
        return Collections.unmodifiableMap(r0);
    }

    private int getExtractedSamplesCount() {
        int r0 = 0;
        SampleQueue[] r1 = this.sampleQueues;
        int r2 = r1.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L5;
        r0 = r0 + r1[r3].getWriteIndex();
        r3 = r3 + 1;
        goto L3
    L5:
        return r0;
    }

    private long getLargestQueuedTimestampUs(boolean r6) {
        long r0 = Long.MIN_VALUE;
        int r2 = 0;
    L4:
        if (r2 >= this.sampleQueues.length) goto L10;
        if (r6 == false) goto L7;
    L8:
        r0 = Math.max(r0, this.sampleQueues[r2].getLargestQueuedTimestampUs());
    L9:
        r2 = r2 + 1;
        goto L4
    L7:
        if (((TrackState) Assertions.checkNotNull(this.trackState)).trackEnabledStates[r2] == false) goto L9;
    L10:
        return r0;
    }

    private boolean isPendingReset() {
        if (this.pendingResetPositionUs == C0180C.TIME_UNSET) goto L5;
        return true;
    L5:
        return false;
    }

    private void maybeFinishPrepare() {
        if (this.released == false) goto L5;
        return;
    L5:
        if (this.prepared == false) goto L7;
        return;
    L7:
        if (this.sampleQueuesBuilt == true) goto L9;
        return;
    L9:
        if (this.seekMap == null) goto L71;
        SampleQueue[] r0 = this.sampleQueues;
        int r1 = r0.length;
        int r3 = 0;
    L12:
        if (r3 >= r1) goto L17;
        if (r0[r3].getUpstreamFormat() == null) goto L15;
        r3 = r3 + 1;
        goto L12
    L15:
        return;
    L17:
        this.loadCondition.close();
        int r2 = this.sampleQueues.length;
        TrackGroup[] r4 = new TrackGroup[r2];
        boolean[] r5 = new boolean[r2];
        int r6 = 0;
    L19:
        if (r6 >= r2) goto L52;
        Format r8 = (Format) Assertions.checkNotNull(this.sampleQueues[r6].getUpstreamFormat());
        String r9 = r8.sampleMimeType;
        boolean r10 = MimeTypes.isAudio(r9);
        if (r10 == false) goto L23;
    L26:
        boolean r11 = true;
    L27:
        r5[r6] = r11;
        this.haveAudioVideoTracks |= r11;
        boolean r12 = MimeTypes.isImage(r9);
        if (this.singleSampleDurationUs == C0180C.TIME_UNSET) goto L32;
        if (r2 != 1) goto L32;
        if (r12 == false) goto L32;
        boolean r7 = true;
    L33:
        this.isSingleSample = r7;
        IcyHeaders r13 = this.icyHeaders;
        if (r13 == null) goto L51;
        if (r10 == false) goto L37;
    L38:
        Metadata r14 = r8.metadata;
        if (r14 != null) goto L41;
        Metadata r15 = new Metadata(new Metadata.Entry[]{r13});
    L42:
        r8 = r8.buildUpon().setMetadata(r15).build();
    L43:
        if (r10 == false) goto L51;
        if (r8.averageBitrate != (-1)) goto L51;
        if (r8.peakBitrate != (-1)) goto L51;
        if (r13.bitrate == (-1)) goto L51;
        r8 = r8.buildUpon().setAverageBitrate(r13.bitrate).build();
        goto L51
    L41:
        r15 = r14.copyWithAppendedEntries(new Metadata.Entry[]{r13});
        goto L42
    L37:
        if (this.sampleQueueTrackIds[r6].isIcyTrack == false) goto L43;
    L51:
        Format r16 = r8.copyWithCryptoType(this.drmSessionManager.getCryptoType(r8));
        r4[r6] = new TrackGroup(Integer.toString(r6), new Format[]{r16});
        this.pendingInitialDiscontinuity |= r16.hasPrerollSamples;
        r6 = r6 + 1;
    L32:
        r7 = false;
        goto L33
    L23:
        if (MimeTypes.isVideo(r9) == true) goto L26;
        r11 = false;
        goto L27
    L52:
        this.trackState = new TrackState(new TrackGroupArray(r4), r5);
        if (this.isSingleSample == true) goto L55;
    L57:
        this.listener.onSourceInfoRefreshed(this.durationUs, this.seekMap.isSeekable(), this.isLive);
        this.prepared = true;
        ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onPrepared(this);
        return;
    L55:
        if (this.durationUs != C0180C.TIME_UNSET) goto L57;
        this.durationUs = this.singleSampleDurationUs;
        final SeekMap r17 = this.seekMap;
        this.seekMap = new C03451(this, r17);
        goto L57
    }

    private void maybeNotifyDownstreamFormat(int r11) {
        assertPrepared();
        boolean[] r0 = this.trackState.trackNotifiedDownstreamFormats;
        if (r0[r11] == true) goto L6;
        Format r5 = this.trackState.tracks.get(r11).getFormat(0);
        this.mediaSourceEventDispatcher.downstreamFormatChanged(MimeTypes.getTrackType(r5.sampleMimeType), r5, 0, null, this.lastSeekPositionUs);
        r0[r11] = true;
        return;
    }

    private void maybeStartDeferredRetry(int r6) {
        assertPrepared();
        boolean[] r0 = this.trackState.trackIsAudioVideoFlags;
        if (this.pendingDeferredRetry == true) goto L5;
        return;
    L5:
        if (r0[r6] == false) goto L16;
        int r2 = 0;
        if (this.sampleQueues[r6].isReady(false) == true) goto L17;
        this.pendingResetPositionUs = 0;
        this.pendingDeferredRetry = false;
        this.notifyDiscontinuity = true;
        this.lastSeekPositionUs = 0;
        this.extractedSamplesCountAtStartOfLoad = 0;
        SampleQueue[] r1 = this.sampleQueues;
        int r3 = r1.length;
    L10:
        if (r2 >= r3) goto L12;
        r1[r2].reset();
        r2 = r2 + 1;
        goto L10
    L12:
        ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onContinueLoadingRequested(this);
        return;
    L17:
        return;
    }

    private void onLengthKnown() {
        this.handler.post(new ProgressiveMediaPeriod$$ExternalSyntheticLambda0(this));
    }

    private TrackOutput prepareTrackOutput(TrackId r6) {
        int r0 = this.sampleQueues.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L10;
        if (r6.equals(this.sampleQueueTrackIds[r1]) == true) goto L7;
        r1 = r1 + 1;
        goto L3
    L7:
        return this.sampleQueues[r1];
    L10:
        if (this.sampleQueuesBuilt == false) goto L13;
        Log.m48w(TAG, "Extractor added new track (id=" + r6.f45id + ") after finishing tracks.");
        return new DiscardingTrackOutput();
    L13:
        SampleQueue r2 = SampleQueue.createWithDrm(this.allocator, this.drmSessionManager, this.drmEventDispatcher);
        r2.setUpstreamFormatChangeListener(this);
        TrackId[] r3 = (TrackId[]) Arrays.copyOf(this.sampleQueueTrackIds, r0 + 1);
        r3[r0] = r6;
        this.sampleQueueTrackIds = (TrackId[]) Util.castNonNullTypeArray(r3);
        SampleQueue[] r4 = (SampleQueue[]) Arrays.copyOf(this.sampleQueues, r0 + 1);
        r4[r0] = r2;
        this.sampleQueues = (SampleQueue[]) Util.castNonNullTypeArray(r4);
        return r2;
    }

    private boolean seekInsideBufferUs(boolean[] r7, long r8, boolean r10) {
        int r0 = this.sampleQueues.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L20;
        SampleQueue r2 = this.sampleQueues[r1];
        if (r2.getReadIndex() != 0) goto L9;
        if (r10 == false) goto L9;
    L19:
        r1 = r1 + 1;
    L9:
        if (this.isSingleSample == false) goto L11;
        boolean r3 = r2.seekTo(r2.getFirstIndex());
    L13:
        if (r3 == true) goto L19;
        if (r7[r1] == true) goto L18;
        if (this.haveAudioVideoTracks == true) goto L19;
    L18:
        return false;
    L11:
        r3 = r2.seekTo(r8, false);
        goto L13
    L20:
        return true;
    }

    private void setSeekMap(SeekMap r7) {
        if (this.icyHeaders != null) goto L5;
        SeekMap r0 = r7;
    L6:
        this.seekMap = r0;
        this.durationUs = r7.getDurationUs();
        int r3 = 1;
        if (this.isLengthKnown == false) goto L9;
    L11:
        boolean r1 = false;
    L12:
        this.isLive = r1;
        if (this.isLive == false) goto L15;
        r3 = 7;
    L15:
        this.dataType = r3;
        if (this.prepared == false) goto L18;
        this.listener.onSourceInfoRefreshed(this.durationUs, r7.isSeekable(), this.isLive);
        return;
    L18:
        maybeFinishPrepare();
        return;
    L9:
        if (r7.getDurationUs() != C0180C.TIME_UNSET) goto L11;
        r1 = true;
        goto L12
    L5:
        r0 = new SeekMap.Unseekable(C0180C.TIME_UNSET);
        goto L6
    }

    private void startLoading() {
        ExtractingLoadable r0 = new ExtractingLoadable(this, this.uri, this.dataSource, this.progressiveMediaExtractor, this, this.loadCondition);
        if (this.prepared == false) goto L14;
        Assertions.checkState(isPendingReset());
        if (this.durationUs != C0180C.TIME_UNSET) goto L7;
    L10:
        ExtractingLoadable.access$600(r0, ((SeekMap) Assertions.checkNotNull(this.seekMap)).getSeekPoints(this.pendingResetPositionUs).first.position, this.pendingResetPositionUs);
        SampleQueue[] r2 = this.sampleQueues;
        int r3 = r2.length;
        int r6 = 0;
    L11:
        if (r6 >= r3) goto L13;
        r2[r6].setStartTimeUs(this.pendingResetPositionUs);
        r6 = r6 + 1;
        goto L11
    L13:
        this.pendingResetPositionUs = C0180C.TIME_UNSET;
        goto L14
    L7:
        if (this.pendingResetPositionUs <= this.durationUs) goto L10;
        this.loadingFinished = true;
        this.pendingResetPositionUs = C0180C.TIME_UNSET;
        return;
    L14:
        this.extractedSamplesCountAtStartOfLoad = getExtractedSamplesCount();
        long r8 = this.loader.startLoading(r0, this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(this.dataType));
        DataSpec r7 = ExtractingLoadable.access$300(r0);
        this.mediaSourceEventDispatcher.loadStarted(new LoadEventInfo(ExtractingLoadable.access$200(r0), r7, r8), 1, -1, null, 0, null, ExtractingLoadable.access$400(r0), this.durationUs);
    }

    private boolean suppressRead() {
        if (this.notifyDiscontinuity == false) goto L5;
    L8:
        return true;
    L5:
        if (isPendingReset() == true) goto L8;
        return false;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean continueLoading(LoadingInfo r3) {
        if (this.loadingFinished == false) goto L5;
        return false;
    L5:
        if (this.loader.hasFatalError() == false) goto L7;
        return false;
    L7:
        if (this.pendingDeferredRetry == false) goto L9;
        return false;
    L9:
        if (this.prepared == true) goto L11;
    L13:
        boolean r0 = this.loadCondition.open();
        if (this.loader.isLoading() == true) goto L19;
        startLoading();
        return true;
    L19:
        return r0;
    L11:
        if (this.enabledTrackCount != 0) goto L13;
        return false;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void discardBuffer(long r6, boolean r8) {
        if (this.isSingleSample == false) goto L5;
        return;
    L5:
        assertPrepared();
        if (isPendingReset() == false) goto L8;
        return;
    L8:
        boolean[] r0 = this.trackState.trackEnabledStates;
        int r1 = this.sampleQueues.length;
        int r2 = 0;
    L9:
        if (r2 >= r1) goto L11;
        this.sampleQueues[r2].discardTo(r6, r8, r0[r2]);
        r2 = r2 + 1;
        goto L9
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public void endTracks() {
        this.sampleQueuesBuilt = true;
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long getAdjustedSeekPositionUs(long r10, SeekParameters r12) {
        assertPrepared();
        if (this.seekMap.isSeekable() == true) goto L6;
        return 0;
    L6:
        SeekMap.SeekPoints r0 = this.seekMap.getSeekPoints(r10);
        return r12.resolveSeekPositionUs(r10, r0.first.timeUs, r0.second.timeUs);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getBufferedPositionUs() {
        assertPrepared();
        if (this.loadingFinished == false) goto L5;
    L31:
        return Long.MIN_VALUE;
    L5:
        if (this.enabledTrackCount == 0) goto L31;
        if (isPendingReset() == true) goto L10;
        long r3 = Long.MAX_VALUE;
        if (this.haveAudioVideoTracks == false) goto L24;
        int r0 = this.sampleQueues.length;
        int r5 = 0;
    L14:
        if (r5 >= r0) goto L24;
        if (this.trackState.trackIsAudioVideoFlags[r5] == false) goto L22;
        if (this.trackState.trackEnabledStates[r5] == false) goto L22;
        if (this.sampleQueues[r5].isLastSampleQueued() == true) goto L22;
        r3 = Math.min(r3, this.sampleQueues[r5].getLargestQueuedTimestampUs());
    L22:
        r5 = r5 + 1;
    L24:
        if (r3 != Long.MAX_VALUE) goto L27;
        r3 = getLargestQueuedTimestampUs(false);
    L27:
        if (r3 != Long.MIN_VALUE) goto L30;
        return this.lastSeekPositionUs;
    L30:
        return r3;
    L10:
        return this.pendingResetPositionUs;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        return getBufferedPositionUs();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public /* synthetic */ List getStreamKeys(List r1) {
        return MediaPeriod.CC.$default$getStreamKeys(this, r1);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        assertPrepared();
        return this.trackState.tracks;
    }

    TrackOutput icyTrack() {
        return prepareTrackOutput(new TrackId(0, true));
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean isLoading() {
        if (this.loader.isLoading() == true) goto L5;
    L7:
        return false;
    L5:
        if (this.loadCondition.isOpen() == false) goto L7;
        return true;
    }

    boolean isReady(int r3) {
        if (suppressRead() == false) goto L5;
    L7:
        return false;
    L5:
        if (this.sampleQueues[r3].isReady(this.loadingFinished) == false) goto L7;
        return true;
    }

    /* JADX INFO: renamed from: lambda$new$0$androidx-media3-exoplayer-source-ProgressiveMediaPeriod */
    /* synthetic */ void m128x97cae34d() {
        if (this.released == true) goto L6;
        ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onContinueLoadingRequested(this);
        return;
    }

    /* JADX INFO: renamed from: lambda$onLengthKnown$2$androidx-media3-exoplayer-source-ProgressiveMediaPeriod */
    /* synthetic */ void m129xa2bd730d() {
        this.isLengthKnown = true;
    }

    /* JADX INFO: renamed from: lambda$seekMap$1$androidx-media3-exoplayer-source-ProgressiveMediaPeriod */
    /* synthetic */ void m130x33ac0ff2(SeekMap r1) {
        setSeekMap(r1);
    }

    void maybeThrowError() throws IOException {
        this.loader.maybeThrowError(this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(this.dataType));
    }

    void maybeThrowError(int r2) throws IOException {
        this.sampleQueues[r2].maybeThrowError();
        maybeThrowError();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
        maybeThrowError();     // Catch: IOException -> L4
    L9:
        if (this.loadingFinished == true) goto L11;
        return;
    L11:
        if (this.prepared == false) goto L14;
        return;
    L14:
        throw ParserException.createForMalformedContainer("Loading finished before preparation is complete.", null);
    L4:
        e = move-exception;
        if (this.suppressPrepareError == false) goto L16;
        Log.m45e(TAG, "Suppressing preparation error because suppressPrepareError=true", e);
        this.sampleQueuesBuilt = true;
        setSeekMap(new SeekMap.Unseekable(C0180C.TIME_UNSET));
        goto L9
    L16:
        throw e;
    }

    public void onLoadCanceled(ExtractingLoadable r14, long r15, long r17, boolean r19) {
        StatsDataSource r0 = ExtractingLoadable.access$100(r14);
        LoadEventInfo r1 = new LoadEventInfo(ExtractingLoadable.access$200(r14), ExtractingLoadable.access$300(r14), r0.getLastOpenedUri(), r0.getLastResponseHeaders(), r15, r17, r0.getBytesRead());
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(ExtractingLoadable.access$200(r14));
        this.mediaSourceEventDispatcher.loadCanceled(r1, 1, -1, null, 0, null, ExtractingLoadable.access$400(r14), this.durationUs);
        if (r19 == true) goto L12;
        SampleQueue[] r2 = this.sampleQueues;
        int r3 = r2.length;
        int r4 = 0;
    L5:
        if (r4 >= r3) goto L8;
        r2[r4].reset();
        r4 = r4 + 1;
        goto L5
    L8:
        if (this.enabledTrackCount <= 0) goto L13;
        ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onContinueLoadingRequested(this);
        return;
    L13:
        return;
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ void onLoadCanceled(Loader.Loadable r8, long r9, long r11, boolean r13) {
        onLoadCanceled((ExtractingLoadable) r8, r9, r11, r13);
    }

    public void onLoadCompleted(ExtractingLoadable r15, long r16, long r18) {
        if (this.durationUs == C0180C.TIME_UNSET) goto L5;
    L11:
        StatsDataSource r0 = ExtractingLoadable.access$100(r15);
        LoadEventInfo r2 = new LoadEventInfo(ExtractingLoadable.access$200(r15), ExtractingLoadable.access$300(r15), r0.getLastOpenedUri(), r0.getLastResponseHeaders(), r16, r18, r0.getBytesRead());
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(ExtractingLoadable.access$200(r15));
        this.mediaSourceEventDispatcher.loadCompleted(r2, 1, -1, null, 0, null, ExtractingLoadable.access$400(r15), this.durationUs);
        this.loadingFinished = true;
        ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onContinueLoadingRequested(this);
        return;
    L5:
        if (this.seekMap == null) goto L11;
        boolean r1 = this.seekMap.isSeekable();
        long r3 = getLargestQueuedTimestampUs(true);
        if (r3 != Long.MIN_VALUE) goto L9;
        long r4 = 0;
    L10:
        this.durationUs = r4;
        this.listener.onSourceInfoRefreshed(this.durationUs, r1, this.isLive);
        goto L11
    L9:
        r4 = 10000 + r3;
        goto L10
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ void onLoadCompleted(Loader.Loadable r7, long r8, long r10) {
        onLoadCompleted((ExtractingLoadable) r7, r8, r10);
    }

    public Loader.LoadErrorAction onLoadError(ExtractingLoadable r20, long r21, long r23, IOException r25, int r26) {
        StatsDataSource r1 = ExtractingLoadable.access$100(r20);
        LoadEventInfo r2 = new LoadEventInfo(ExtractingLoadable.access$200(r20), ExtractingLoadable.access$300(r20), r1.getLastOpenedUri(), r1.getLastResponseHeaders(), r21, r23, r1.getBytesRead());
        long r7 = this.loadErrorHandlingPolicy.getRetryDelayMsFor(new LoadErrorHandlingPolicy.LoadErrorInfo(r2, new MediaLoadData(1, -1, null, 0, null, Util.usToMs(ExtractingLoadable.access$400(r20)), Util.usToMs(this.durationUs)), r25, r26));
        if (r7 != C0180C.TIME_UNSET) goto L5;
        Loader.LoadErrorAction r16 = Loader.DONT_RETRY_FATAL;
    L14:
        boolean r15 = !r16.isRetry();
        this.mediaSourceEventDispatcher.loadError(r2, 1, -1, null, 0, null, ExtractingLoadable.access$400(r20), this.durationUs, r25, r15);
        if (r15 == false) goto L17;
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(ExtractingLoadable.access$200(r20));
    L17:
        return r16;
    L5:
        int r3 = getExtractedSamplesCount();
        if (r3 <= this.extractedSamplesCountAtStartOfLoad) goto L8;
        boolean r9 = true;
    L10:
        if (configureRetry(r20, r3) == false) goto L12;
        Loader.LoadErrorAction r11 = Loader.createRetryAction(r9, r7);
    L13:
        r16 = r11;
        goto L14
    L12:
        r11 = Loader.DONT_RETRY;
        goto L13
    L8:
        r9 = false;
        goto L10
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ Loader.LoadErrorAction onLoadError(Loader.Loadable r9, long r10, long r12, IOException r14, int r15) {
        return onLoadError((ExtractingLoadable) r9, r10, r12, r14, r15);
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* synthetic */ void onLoadStarted(Loader.Loadable r1, long r2, long r4, int r6) {
        Loader.Callback.CC.$default$onLoadStarted(this, r1, r2, r4, r6);
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.ReleaseCallback
    public void onLoaderReleased() {
        SampleQueue[] r0 = this.sampleQueues;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].release();
        r2 = r2 + 1;
        goto L3
    L5:
        this.progressiveMediaExtractor.release();
    }

    @Override // androidx.media3.exoplayer.source.SampleQueue.UpstreamFormatChangedListener
    public void onUpstreamFormatChanged(Format r3) {
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void prepare(MediaPeriod.Callback r2, long r3) {
        this.callback = r2;
        this.loadCondition.open();
        startLoading();
    }

    int readData(int r4, FormatHolder r5, DecoderInputBuffer r6, int r7) {
        if (suppressRead() == false) goto L5;
        return -3;
    L5:
        maybeNotifyDownstreamFormat(r4);
        int r0 = this.sampleQueues[r4].read(r5, r6, r7, this.loadingFinished);
        if (r0 != (-3)) goto L8;
        maybeStartDeferredRetry(r4);
    L8:
        return r0;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long readDiscontinuity() {
        if (this.pendingInitialDiscontinuity == false) goto L7;
        this.pendingInitialDiscontinuity = false;
        return this.lastSeekPositionUs;
    L7:
        if (this.notifyDiscontinuity == true) goto L9;
        return C0180C.TIME_UNSET;
    L9:
        if (this.loadingFinished == false) goto L11;
    L12:
        this.notifyDiscontinuity = false;
        return this.lastSeekPositionUs;
    L11:
        if (getExtractedSamplesCount() > this.extractedSamplesCountAtStartOfLoad) goto L12;
        return C0180C.TIME_UNSET;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public void reevaluateBuffer(long r1) {
    }

    public void release() {
        if (this.prepared == false) goto L7;
        SampleQueue[] r0 = this.sampleQueues;
        int r1 = r0.length;
        int r2 = 0;
    L5:
        if (r2 >= r1) goto L7;
        r0[r2].preRelease();
        r2 = r2 + 1;
    L7:
        this.loader.release(this);
        this.handler.removeCallbacksAndMessages(null);
        this.callback = null;
        this.released = true;
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public void seekMap(final SeekMap r3) {
        this.handler.post(new ProgressiveMediaPeriod$$ExternalSyntheticLambda3(this, r3));
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long seekToUs(long r7) {
        assertPrepared();
        boolean[] r0 = this.trackState.trackIsAudioVideoFlags;
        if (this.seekMap.isSeekable() == false) goto L5;
        long r1 = r7;
    L6:
        int r8 = 0;
        this.notifyDiscontinuity = false;
        if (this.lastSeekPositionUs != r1) goto L9;
        boolean r9 = true;
    L10:
        this.lastSeekPositionUs = r1;
        if (isPendingReset() == false) goto L15;
        this.pendingResetPositionUs = r1;
        return r1;
    L15:
        if (this.dataType != 7) goto L17;
    L23:
        this.pendingDeferredRetry = false;
        this.pendingResetPositionUs = r1;
        this.loadingFinished = false;
        this.pendingInitialDiscontinuity = false;
        if (this.loader.isLoading() == false) goto L29;
        SampleQueue[] r3 = this.sampleQueues;
        int r4 = r3.length;
    L26:
        if (r8 >= r4) goto L28;
        r3[r8].discardToEnd();
        r8 = r8 + 1;
        goto L26
    L28:
        this.loader.cancelLoading();
    L32:
        return r1;
    L29:
        this.loader.clearFatalError();
        SampleQueue[] r5 = this.sampleQueues;
        int r6 = r5.length;
    L30:
        if (r8 >= r6) goto L32;
        r5[r8].reset();
        r8 = r8 + 1;
        goto L30
    L17:
        if (this.loadingFinished == true) goto L21;
        if (this.loader.isLoading() == false) goto L23;
    L21:
        if (seekInsideBufferUs(r0, r1, r9) == false) goto L23;
        return r1;
    L9:
        r9 = false;
        goto L10
    L5:
        r1 = 0;
        goto L6
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long selectTracks(ExoTrackSelection[] r17, boolean[] r18, SampleStream[] r19, boolean[] r20, long r21) {
        long r3 = r21;
        assertPrepared();
        TrackGroupArray r5 = this.trackState.tracks;
        boolean[] r6 = this.trackState.trackEnabledStates;
        int r7 = this.enabledTrackCount;
        int r8 = 0;
    L3:
        int r10 = 0;
        if (r8 >= r17.length) goto L14;
        if (r19[r8] == null) goto L12;
        if (r17[r8] != null) goto L10;
    L11:
        int r9 = SampleStreamImpl.access$000((SampleStreamImpl) r19[r8]);
        Assertions.checkState(r6[r9]);
        this.enabledTrackCount--;
        r6[r9] = false;
        r19[r8] = null;
        goto L12
    L10:
        if (r18[r8] == false) goto L11;
    L12:
        r8 = r8 + 1;
        goto L3
    L14:
        if (this.seenFirstTrackSelection == false) goto L18;
        if (r7 != 0) goto L22;
    L16:
        boolean r11 = true;
    L23:
        int r12 = 0;
    L25:
        if (r12 >= r17.length) goto L49;
        if (r19[r12] != null) goto L47;
        if (r17[r12] == null) goto L47;
        ExoTrackSelection r13 = r17[r12];
        if (r13.length() != 1) goto L33;
        boolean r14 = true;
    L34:
        Assertions.checkState(r14);
        if (r13.getIndexInTrackGroup(0) != 0) goto L37;
        boolean r15 = true;
    L38:
        Assertions.checkState(r15);
        int r16 = r5.indexOf(r13.getTrackGroup());
        Assertions.checkState(!r6[r16]);
        this.enabledTrackCount++;
        r6[r16] = true;
        this.pendingInitialDiscontinuity |= r13.getSelectedFormat().hasPrerollSamples;
        r19[r12] = new SampleStreamImpl(this, r16);
        r20[r12] = true;
        if (r11 == true) goto L47;
        SampleQueue r110 = this.sampleQueues[r16];
        if (r110.getReadIndex() != 0) goto L43;
    L45:
        boolean r111 = false;
    L46:
        r11 = r111;
        goto L47
    L43:
        if (r110.seekTo(r3, true) == true) goto L45;
        r111 = true;
        goto L46
    L37:
        r15 = false;
        goto L38
    L33:
        r14 = false;
    L47:
        r12 = r12 + 1;
        goto L25
    L49:
        if (this.enabledTrackCount != 0) goto L59;
        this.pendingDeferredRetry = false;
        this.notifyDiscontinuity = false;
        this.pendingInitialDiscontinuity = false;
        if (this.loader.isLoading() == false) goto L56;
        SampleQueue[] r22 = this.sampleQueues;
        int r112 = r22.length;
    L53:
        if (r10 >= r112) goto L55;
        r22[r10].discardToEnd();
        r10 = r10 + 1;
        goto L53
    L55:
        this.loader.cancelLoading();
    L67:
        this.seenFirstTrackSelection = true;
        return r3;
    L56:
        this.loadingFinished = false;
        SampleQueue[] r23 = this.sampleQueues;
        int r113 = r23.length;
    L57:
        if (r10 >= r113) goto L67;
        r23[r10].reset();
        r10 = r10 + 1;
        goto L57
    L59:
        if (r11 == false) goto L67;
        r3 = seekToUs(r3);
        int r24 = 0;
    L62:
        if (r24 >= r19.length) goto L67;
        if (r19[r24] == null) goto L66;
        r20[r24] = true;
    L66:
        r24 = r24 + 1;
    L22:
        r11 = false;
        goto L23
    L18:
        if (r3 == 0) goto L22;
        if (this.isSingleSample == true) goto L22;
        goto L22
    }

    int skipData(int r3, long r4) {
        if (suppressRead() == false) goto L6;
        return 0;
    L6:
        maybeNotifyDownstreamFormat(r3);
        SampleQueue r0 = this.sampleQueues[r3];
        int r1 = r0.getSkipCount(r4, this.loadingFinished);
        r0.skip(r1);
        if (r1 != 0) goto L9;
        maybeStartDeferredRetry(r3);
    L9:
        return r1;
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public TrackOutput track(int r3, int r4) {
        return prepareTrackOutput(new TrackId(r3, false));
    }
}
