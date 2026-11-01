package androidx.media3.exoplayer.hls;

import android.net.Uri;
import android.os.Handler;
import android.util.SparseIntArray;
import androidx.media3.common.C0180C;
import androidx.media3.common.DataReader;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.HttpDataSource;
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
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.source.chunk.Chunk;
import androidx.media3.exoplayer.source.chunk.MediaChunkIterator;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.trackselection.TrackSelectionUtil;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.Loader;
import androidx.media3.extractor.DiscardingTrackOutput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.metadata.emsg.EventMessage;
import androidx.media3.extractor.metadata.emsg.EventMessageDecoder;
import androidx.media3.extractor.metadata.id3.PrivFrame;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
final class HlsSampleStreamWrapper implements Loader.Callback<Chunk>, Loader.ReleaseCallback, SequenceableLoader, ExtractorOutput, SampleQueue.UpstreamFormatChangedListener {
    private static final Set<Integer> MAPPABLE_TYPES = null;
    public static final int SAMPLE_QUEUE_INDEX_NO_MAPPING_FATAL = -2;
    public static final int SAMPLE_QUEUE_INDEX_NO_MAPPING_NON_FATAL = -3;
    public static final int SAMPLE_QUEUE_INDEX_PENDING = -1;
    private static final String TAG = "HlsSampleStreamWrapper";
    private final Allocator allocator;
    private final Callback callback;
    private final HlsChunkSource chunkSource;
    private Format downstreamTrackFormat;
    private final DrmSessionEventListener.EventDispatcher drmEventDispatcher;
    private DrmInitData drmInitData;
    private final DrmSessionManager drmSessionManager;
    private TrackOutput emsgUnwrappingTrackOutput;
    private int enabledTrackGroupCount;
    private final Handler handler;
    private boolean haveAudioVideoSampleQueues;
    private final ArrayList<HlsSampleStream> hlsSampleStreams;
    private long lastSeekPositionUs;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private final Loader loader;
    private Chunk loadingChunk;
    private boolean loadingFinished;
    private final Runnable maybeFinishPrepareRunnable;
    private final ArrayList<HlsMediaChunk> mediaChunks;
    private final MediaSourceEventListener.EventDispatcher mediaSourceEventDispatcher;
    private final int metadataType;
    private final Format muxedAudioFormat;
    private final HlsChunkSource.HlsChunkHolder nextChunkHolder;
    private final Runnable onTracksEndedRunnable;
    private Set<TrackGroup> optionalTrackGroups;
    private final Map<String, DrmInitData> overridingDrmInitData;
    private long pendingResetPositionUs;
    private boolean pendingResetUpstreamFormats;
    private boolean prepared;
    private int primarySampleQueueIndex;
    private int primarySampleQueueType;
    private int primaryTrackGroupIndex;
    private final List<HlsMediaChunk> readOnlyMediaChunks;
    private boolean released;
    private long sampleOffsetUs;
    private SparseIntArray sampleQueueIndicesByType;
    private boolean[] sampleQueueIsAudioVideoFlags;
    private Set<Integer> sampleQueueMappingDoneByType;
    private int[] sampleQueueTrackIds;
    private HlsSampleQueue[] sampleQueues;
    private boolean sampleQueuesBuilt;
    private boolean[] sampleQueuesEnabledStates;
    private boolean seenFirstTrackSelection;
    private HlsMediaChunk sourceChunk;
    private int[] trackGroupToSampleQueueIndex;
    private TrackGroupArray trackGroups;
    private final int trackType;
    private boolean tracksEnded;
    private final String uid;
    private Format upstreamTrackFormat;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.hls.HlsSampleStreamWrapper$1 */
    static /* synthetic */ class C02931 {
    }

    public interface Callback extends SequenceableLoader.Callback<HlsSampleStreamWrapper> {
        void onPlaylistRefreshRequired(Uri r1);

        void onPrepared();
    }

    private static class EmsgUnwrappingTrackOutput implements TrackOutput {
        private static final Format EMSG_FORMAT = null;
        private static final Format ID3_FORMAT = null;
        private byte[] buffer;
        private int bufferPosition;
        private final TrackOutput delegate;
        private final Format delegateFormat;
        private final EventMessageDecoder emsgDecoder;
        private Format format;

        static {
            ID3_FORMAT = new Format.Builder().setSampleMimeType(MimeTypes.APPLICATION_ID3).build();
            EMSG_FORMAT = new Format.Builder().setSampleMimeType(MimeTypes.APPLICATION_EMSG).build();
        }

        public EmsgUnwrappingTrackOutput(TrackOutput r4, int r5) {
            this.emsgDecoder = new EventMessageDecoder();
            this.delegate = r4;
            switch(r5) {
                case 1: goto L7;
                case 2: goto L5;
                case 3: goto L6;
                default: goto L5;
            };
        L6:
            this.delegateFormat = EMSG_FORMAT;
        L8:
            this.buffer = new byte[0];
            this.bufferPosition = 0;
            return;
        L7:
            this.delegateFormat = ID3_FORMAT;
            goto L8
        L5:
            throw new IllegalArgumentException("Unknown metadataType: " + r5);
        }

        private boolean emsgContainsExpectedWrappedFormat(EventMessage r4) {
            Format r0 = r4.getWrappedMetadataFormat();
            if (r0 != null) goto L5;
        L7:
            return false;
        L5:
            if (Util.areEqual(this.delegateFormat.sampleMimeType, r0.sampleMimeType) == false) goto L7;
            return true;
        }

        private void ensureBufferCapacity(int r3) {
            if (this.buffer.length >= r3) goto L6;
            this.buffer = Arrays.copyOf(this.buffer, (r3 / 2) + r3);
            return;
        }

        private ParsableByteArray getSampleAndTrimBuffer(int r8, int r9) {
            int r0 = this.bufferPosition - r9;
            byte[] r2 = this.buffer;
            ParsableByteArray r3 = new ParsableByteArray(Arrays.copyOfRange(r2, r0 - r8, r0));
            System.arraycopy(this.buffer, r0, this.buffer, 0, r9);
            this.bufferPosition = r9;
            return r3;
        }

        @Override // androidx.media3.extractor.TrackOutput
        public void format(Format r3) {
            this.format = r3;
            this.delegate.format(this.delegateFormat);
        }

        @Override // androidx.media3.extractor.TrackOutput
        public /* synthetic */ int sampleData(DataReader r1, int r2, boolean r3) {
            return TrackOutput.CC.$default$sampleData(this, r1, r2, r3);
        }

        @Override // androidx.media3.extractor.TrackOutput
        public int sampleData(DataReader r3, int r4, boolean r5, int r6) throws IOException {
            ensureBufferCapacity(this.bufferPosition + r4);
            int r0 = r3.read(this.buffer, this.bufferPosition, r4);
            if (r0 != (-1)) goto L8;
            if (r5 == false) goto L7;
            return -1;
        L7:
            throw new EOFException();
        L8:
            this.bufferPosition += r0;
            return r0;
        }

        @Override // androidx.media3.extractor.TrackOutput
        public /* synthetic */ void sampleData(ParsableByteArray r1, int r2) {
            TrackOutput.CC.$default$sampleData(this, r1, r2);
        }

        @Override // androidx.media3.extractor.TrackOutput
        public void sampleData(ParsableByteArray r3, int r4, int r5) {
            ensureBufferCapacity(this.bufferPosition + r4);
            r3.readBytes(this.buffer, this.bufferPosition, r4);
            this.bufferPosition += r4;
        }

        @Override // androidx.media3.extractor.TrackOutput
        public void sampleMetadata(long r10, int r12, int r13, int r14, TrackOutput.CryptoData r15) {
            Assertions.checkNotNull(this.format);
            ParsableByteArray r0 = getSampleAndTrimBuffer(r13, r14);
            if (Util.areEqual(this.format.sampleMimeType, this.delegateFormat.sampleMimeType) == false) goto L6;
            ParsableByteArray r1 = r0;
        L12:
            int r6 = r1.bytesLeft();
            this.delegate.sampleData(r1, r6);
            this.delegate.sampleMetadata(r10, r12, r6, 0, r15);
            return;
        L6:
            if (MimeTypes.APPLICATION_EMSG.equals(this.format.sampleMimeType) == false) goto L14;
            EventMessage r2 = this.emsgDecoder.decode(r0);
            if (emsgContainsExpectedWrappedFormat(r2) == true) goto L11;
            Log.m48w(HlsSampleStreamWrapper.TAG, String.format("Ignoring EMSG. Expected it to contain wrapped %s but actual wrapped format: %s", new Object[]{this.delegateFormat.sampleMimeType, r2.getWrappedMetadataFormat()}));
            return;
        L11:
            r1 = new ParsableByteArray((byte[]) Assertions.checkNotNull(r2.getWrappedMetadataBytes()));
            goto L12
        L14:
            Log.m48w(HlsSampleStreamWrapper.TAG, "Ignoring sample for unsupported format: " + this.format.sampleMimeType);
        }
    }

    private static final class HlsSampleQueue extends SampleQueue {
        private DrmInitData drmInitData;
        private final Map<String, DrmInitData> overridingDrmInitData;

        private HlsSampleQueue(Allocator r1, DrmSessionManager r2, DrmSessionEventListener.EventDispatcher r3, Map<String, DrmInitData> r4) {
            super(r1, r2, r3);
            this.overridingDrmInitData = r4;
        }

        /* synthetic */ HlsSampleQueue(Allocator r1, DrmSessionManager r2, DrmSessionEventListener.EventDispatcher r3, Map r4, C02931 r5) {
            this(r1, r2, r3, r4);
        }

        private Metadata getAdjustedMetadata(Metadata r9) {
            if (r9 != null) goto L5;
            return null;
        L5:
            int r1 = r9.length();
            int r2 = -1;
            int r3 = 0;
        L6:
            if (r3 >= r1) goto L14;
            Metadata.Entry r4 = r9.get(r3);
            if ((r4 instanceof PrivFrame) == false) goto L12;
            if (HlsMediaChunk.PRIV_TIMESTAMP_FRAME_OWNER.equals(((PrivFrame) r4).owner) == false) goto L12;
            r2 = r3;
        L12:
            r3 = r3 + 1;
        L14:
            if (r2 != (-1)) goto L17;
            return r9;
        L17:
            if (r1 != 1) goto L19;
            return null;
        L19:
            Metadata.Entry[] r0 = new Metadata.Entry[r1 - 1];
            int r5 = 0;
        L20:
            if (r5 >= r1) goto L28;
            if (r5 == r2) goto L26;
            if (r5 >= r2) goto L24;
            int r6 = r5;
        L25:
            r0[r6] = r9.get(r5);
            goto L26
        L24:
            r6 = r5 - 1;
        L26:
            r5 = r5 + 1;
            goto L20
        L28:
            return new Metadata(r0);
        }

        @Override // androidx.media3.exoplayer.source.SampleQueue
        public Format getAdjustedUpstreamFormat(Format r4) {
            if (this.drmInitData == null) goto L5;
            DrmInitData r0 = this.drmInitData;
        L6:
            if (r0 == null) goto L10;
            DrmInitData r1 = this.overridingDrmInitData.get(r0.schemeType);
            if (r1 == null) goto L10;
            r0 = r1;
        L10:
            Metadata r2 = getAdjustedMetadata(r4.metadata);
            if (r0 == r4.drmInitData) goto L13;
        L14:
            r4 = r4.buildUpon().setDrmInitData(r0).setMetadata(r2).build();
        L16:
            return super.getAdjustedUpstreamFormat(r4);
        L13:
            if (r2 == r4.metadata) goto L16;
        L5:
            r0 = r4.drmInitData;
            goto L6
        }

        @Override // androidx.media3.exoplayer.source.SampleQueue, androidx.media3.extractor.TrackOutput
        public void sampleMetadata(long r1, int r3, int r4, int r5, TrackOutput.CryptoData r6) {
            super.sampleMetadata(r1, r3, r4, r5, r6);
        }

        public void setDrmInitData(DrmInitData r1) {
            this.drmInitData = r1;
            invalidateUpstreamFormatAdjustment();
        }

        public void setSourceChunk(HlsMediaChunk r3) {
            sourceId(r3.uid);
        }
    }

    public static /* synthetic */ void $r8$lambda$4T0XYfGSx3i_ZqboGl7iI4j9qEU(HlsSampleStreamWrapper r0) {
        r0.onTracksEnded();
    }

    public static /* synthetic */ void $r8$lambda$UK4TNmzu4rF7l558xzg8C7fYdDI(HlsSampleStreamWrapper r0) {
        r0.maybeFinishPrepare();
    }

    static {
        MAPPABLE_TYPES = Collections.unmodifiableSet(new HashSet(Arrays.asList(new Integer[]{1, 2, 5})));
    }

    public HlsSampleStreamWrapper(String r18, int r19, Callback r20, HlsChunkSource r21, Map<String, DrmInitData> r22, Allocator r23, long r24, Format r26, DrmSessionManager r27, DrmSessionEventListener.EventDispatcher r28, LoadErrorHandlingPolicy r29, MediaSourceEventListener.EventDispatcher r30, int r31) {
        this.uid = r18;
        this.trackType = r19;
        this.callback = r20;
        this.chunkSource = r21;
        this.overridingDrmInitData = r22;
        this.allocator = r23;
        this.muxedAudioFormat = r26;
        this.drmSessionManager = r27;
        this.drmEventDispatcher = r28;
        this.loadErrorHandlingPolicy = r29;
        this.mediaSourceEventDispatcher = r30;
        this.metadataType = r31;
        this.loader = new Loader("Loader:HlsSampleStreamWrapper");
        this.nextChunkHolder = new HlsChunkSource.HlsChunkHolder();
        this.sampleQueueTrackIds = new int[0];
        this.sampleQueueMappingDoneByType = new HashSet(MAPPABLE_TYPES.size());
        this.sampleQueueIndicesByType = new SparseIntArray(MAPPABLE_TYPES.size());
        this.sampleQueues = new HlsSampleQueue[0];
        this.sampleQueueIsAudioVideoFlags = new boolean[0];
        this.sampleQueuesEnabledStates = new boolean[0];
        this.mediaChunks = new ArrayList();
        this.readOnlyMediaChunks = Collections.unmodifiableList(this.mediaChunks);
        this.hlsSampleStreams = new ArrayList();
        this.maybeFinishPrepareRunnable = new HlsSampleStreamWrapper$$ExternalSyntheticLambda1(this);
        this.onTracksEndedRunnable = new HlsSampleStreamWrapper$$ExternalSyntheticLambda2(this);
        this.handler = Util.createHandlerForCurrentLooper();
        this.lastSeekPositionUs = r24;
        this.pendingResetPositionUs = r24;
    }

    @EnsuresNonNull({"trackGroups", "optionalTrackGroups"})
    private void assertIsPrepared() {
        Assertions.checkState(this.prepared);
        Assertions.checkNotNull(this.trackGroups);
        Assertions.checkNotNull(this.optionalTrackGroups);
    }

    @EnsuresNonNull({"trackGroups", "optionalTrackGroups", "trackGroupToSampleQueueIndex"})
    private void buildTracksFromSampleStreams() {
        int r0 = -2;
        int r1 = -1;
        int r2 = this.sampleQueues.length;
        int r3 = 0;
    L4:
        if (r3 >= r2) goto L22;
        String r5 = ((Format) Assertions.checkStateNotNull(this.sampleQueues[r3].getUpstreamFormat())).sampleMimeType;
        if (MimeTypes.isVideo(r5) == false) goto L9;
        int r6 = 2;
    L16:
        if (getTrackTypeScore(r6) <= getTrackTypeScore(r0)) goto L18;
        r0 = r6;
        r1 = r3;
    L21:
        r3 = r3 + 1;
        goto L4
    L18:
        if (r6 != r0) goto L21;
        if (r1 == (-1)) goto L21;
        r1 = -1;
        goto L21
    L9:
        if (MimeTypes.isAudio(r5) == false) goto L12;
        r6 = 1;
        goto L16
    L12:
        if (MimeTypes.isText(r5) == false) goto L14;
        r6 = 3;
        goto L16
    L14:
        r6 = -2;
        goto L16
    L22:
        TrackGroup r4 = this.chunkSource.getTrackGroup();
        int r7 = r4.length;
        this.primaryTrackGroupIndex = -1;
        this.trackGroupToSampleQueueIndex = new int[r2];
        int r8 = 0;
    L23:
        if (r8 >= r2) goto L25;
        this.trackGroupToSampleQueueIndex[r8] = r8;
        r8 = r8 + 1;
        goto L23
    L25:
        TrackGroup[] r9 = new TrackGroup[r2];
        int r10 = 0;
    L26:
        boolean r11 = false;
        if (r10 >= r2) goto L55;
        Format r12 = (Format) Assertions.checkStateNotNull(this.sampleQueues[r10].getUpstreamFormat());
        if (r10 != r1) goto L44;
        Format[] r13 = new Format[r7];
        int r14 = 0;
    L31:
        if (r14 >= r7) goto L42;
        Format r15 = r4.getFormat(r14);
        if (r0 == 1) goto L35;
    L38:
        if (r7 != 1) goto L40;
        Format r16 = r12.withManifestFormatInfo(r15);
    L41:
        r13[r14] = r16;
        r14 = r14 + 1;
        goto L31
    L40:
        r16 = deriveFormat(r15, r12, true);
        goto L41
    L35:
        if (this.muxedAudioFormat == null) goto L38;
        r15 = r15.withManifestFormatInfo(this.muxedAudioFormat);
        goto L38
    L42:
        r9[r10] = new TrackGroup(this.uid, r13);
        this.primaryTrackGroupIndex = r10;
    L54:
        r10 = r10 + 1;
        goto L26
    L44:
        if (r0 == 2) goto L46;
    L48:
        Format r17 = null;
    L49:
        StringBuilder r18 = new StringBuilder().append(this.uid).append(":muxed:");
        if (r10 >= r1) goto L52;
        int r19 = r10;
    L53:
        r9[r10] = new TrackGroup(r18.append(r19).toString(), new Format[]{deriveFormat(r17, r12, false)});
        goto L54
    L52:
        r19 = r10 - 1;
        goto L53
    L46:
        if (MimeTypes.isAudio(r12.sampleMimeType) == false) goto L48;
        r17 = this.muxedAudioFormat;
        goto L49
    L55:
        this.trackGroups = createTrackGroupArrayWithDrmInfo(r9);
        if (this.optionalTrackGroups != null) goto L58;
        r11 = true;
    L58:
        Assertions.checkState(r11);
        this.optionalTrackGroups = Collections.emptySet();
    }

    private boolean canDiscardUpstreamMediaChunksFromIndex(int r6) {
        int r0 = r6;
    L4:
        if (r0 >= this.mediaChunks.size()) goto L9;
        if (this.mediaChunks.get(r0).shouldSpliceIn == true) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return false;
    L9:
        HlsMediaChunk r1 = this.mediaChunks.get(r6);
        int r2 = 0;
    L11:
        if (r2 >= this.sampleQueues.length) goto L16;
        int r3 = r1.getFirstSampleIndex(r2);
        if (this.sampleQueues[r2].getReadIndex() > r3) goto L14;
        r2 = r2 + 1;
        goto L11
    L14:
        return false;
    L16:
        return true;
    }

    private static DiscardingTrackOutput createDiscardingTrackOutput(int r2, int r3) {
        Log.m48w(TAG, "Unmapped track with id " + r2 + " of type " + r3);
        return new DiscardingTrackOutput();
    }

    private SampleQueue createSampleQueue(int r9, int r10) {
        int r0 = this.sampleQueues.length;
        boolean r1 = true;
        if (r10 != 1) goto L5;
    L8:
        HlsSampleQueue r2 = new HlsSampleQueue(this.allocator, this.drmSessionManager, this.drmEventDispatcher, this.overridingDrmInitData, null);
        r2.setStartTimeUs(this.lastSeekPositionUs);
        if (r1 == false) goto L11;
        r2.setDrmInitData(this.drmInitData);
    L11:
        r2.setSampleOffsetUs(this.sampleOffsetUs);
        if (this.sourceChunk == null) goto L14;
        r2.setSourceChunk(this.sourceChunk);
    L14:
        r2.setUpstreamFormatChangeListener(this);
        this.sampleQueueTrackIds = Arrays.copyOf(this.sampleQueueTrackIds, r0 + 1);
        this.sampleQueueTrackIds[r0] = r9;
        this.sampleQueues = (HlsSampleQueue[]) Util.nullSafeArrayAppend(this.sampleQueues, r2);
        this.sampleQueueIsAudioVideoFlags = Arrays.copyOf(this.sampleQueueIsAudioVideoFlags, r0 + 1);
        this.sampleQueueIsAudioVideoFlags[r0] = r1;
        this.haveAudioVideoSampleQueues |= this.sampleQueueIsAudioVideoFlags[r0];
        this.sampleQueueMappingDoneByType.add(Integer.valueOf(r10));
        this.sampleQueueIndicesByType.append(r10, r0);
        if (getTrackTypeScore(r10) <= getTrackTypeScore(this.primarySampleQueueType)) goto L17;
        this.primarySampleQueueIndex = r0;
        this.primarySampleQueueType = r10;
    L17:
        this.sampleQueuesEnabledStates = Arrays.copyOf(this.sampleQueuesEnabledStates, r0 + 1);
        return r2;
    L5:
        if (r10 == 2) goto L8;
        r1 = false;
        goto L8
    }

    private TrackGroupArray createTrackGroupArrayWithDrmInfo(TrackGroup[] r7) {
        int r0 = 0;
    L4:
        if (r0 >= r7.length) goto L11;
        TrackGroup r1 = r7[r0];
        Format[] r2 = new Format[r1.length];
        int r3 = 0;
    L7:
        if (r3 >= r1.length) goto L9;
        Format r4 = r1.getFormat(r3);
        r2[r3] = r4.copyWithCryptoType(this.drmSessionManager.getCryptoType(r4));
        r3 = r3 + 1;
        goto L7
    L9:
        r7[r0] = new TrackGroup(r1.f31id, r2);
        r0 = r0 + 1;
        goto L4
    L11:
        return new TrackGroupArray(r7);
    }

    private static Format deriveFormat(Format r8, Format r9, boolean r10) {
        if (r8 != null) goto L4;
        return r9;
    L4:
        int r0 = MimeTypes.getTrackType(r9.sampleMimeType);
        if (Util.getCodecCountOfType(r8.codecs, r0) != 1) goto L7;
        String r1 = Util.getCodecsOfType(r8.codecs, r0);
        String r3 = MimeTypes.getMediaMimeType(r1);
    L8:
        Format.Builder r4 = r9.buildUpon().setId(r8.f26id).setLabel(r8.label).setLabels(r8.labels).setLanguage(r8.language).setSelectionFlags(r8.selectionFlags).setRoleFlags(r8.roleFlags);
        if (r10 == false) goto L11;
        int r6 = r8.averageBitrate;
    L12:
        Format.Builder r5 = r4.setAverageBitrate(r6);
        if (r10 == false) goto L15;
        int r7 = r8.peakBitrate;
    L16:
        Format.Builder r11 = r5.setPeakBitrate(r7).setCodecs(r1);
        if (r0 != 2) goto L19;
        r11.setWidth(r8.width).setHeight(r8.height).setFrameRate(r8.frameRate);
    L19:
        if (r3 == null) goto L22;
        r11.setSampleMimeType(r3);
    L22:
        if (r8.channelCount == (-1)) goto L26;
        if (r0 != 1) goto L26;
        r11.setChannelCount(r8.channelCount);
    L26:
        if (r8.metadata == null) goto L32;
        Metadata r2 = r8.metadata;
        if (r9.metadata == null) goto L30;
        r2 = r9.metadata.copyWithAppendedEntriesFrom(r2);
    L30:
        r11.setMetadata(r2);
    L32:
        return r11.build();
    L15:
        r7 = -1;
        goto L16
    L11:
        r6 = -1;
        goto L12
    L7:
        r1 = MimeTypes.getCodecsCorrespondingToMimeType(r8.codecs, r9.sampleMimeType);
        r3 = r9.sampleMimeType;
        goto L8
    }

    private void discardUpstream(int r9) {
        Assertions.checkState(!this.loader.isLoading());
        int r0 = -1;
        int r1 = r9;
    L4:
        if (r1 >= this.mediaChunks.size()) goto L10;
        if (canDiscardUpstreamMediaChunksFromIndex(r1) == true) goto L7;
        r1 = r1 + 1;
        goto L4
    L7:
        r0 = r1;
    L10:
        if (r0 != (-1)) goto L12;
        return;
    L12:
        long r6 = getLastMediaChunk().endTimeUs;
        HlsMediaChunk r2 = discardUpstreamMediaChunksFromIndex(r0);
        if (this.mediaChunks.isEmpty() == false) goto L15;
        this.pendingResetPositionUs = this.lastSeekPositionUs;
    L16:
        this.loadingFinished = false;
        this.mediaSourceEventDispatcher.upstreamDiscarded(this.primarySampleQueueType, r2.startTimeUs, r6);
        return;
    L15:
        ((HlsMediaChunk) Iterables.getLast(this.mediaChunks)).invalidateExtractor();
        goto L16
    }

    private HlsMediaChunk discardUpstreamMediaChunksFromIndex(int r5) {
        HlsMediaChunk r0 = this.mediaChunks.get(r5);
        Util.removeRange(this.mediaChunks, r5, this.mediaChunks.size());
        int r1 = 0;
    L4:
        if (r1 >= this.sampleQueues.length) goto L6;
        int r2 = r0.getFirstSampleIndex(r1);
        this.sampleQueues[r1].discardUpstreamSamples(r2);
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    private boolean finishedReadingChunk(HlsMediaChunk r8) {
        int r0 = r8.uid;
        int r1 = this.sampleQueues.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L11;
        if (this.sampleQueuesEnabledStates[r2] == false) goto L10;
        if (this.sampleQueues[r2].peekSourceId() != r0) goto L10;
        return false;
    L10:
        r2 = r2 + 1;
        goto L3
    L11:
        return true;
    }

    private static boolean formatsMatch(Format r7, Format r8) {
        String r0 = r7.sampleMimeType;
        String r1 = r8.sampleMimeType;
        int r2 = MimeTypes.getTrackType(r0);
        if (r2 == 3) goto L10;
        if (r2 != MimeTypes.getTrackType(r1)) goto L8;
        return true;
    L8:
        return false;
    L10:
        if (Util.areEqual(r0, r1) == true) goto L13;
        return false;
    L13:
        if (MimeTypes.APPLICATION_CEA608.equals(r0) == true) goto L19;
        if (MimeTypes.APPLICATION_CEA708.equals(r0) == true) goto L19;
        return true;
    L19:
        if (r7.accessibilityChannel != r8.accessibilityChannel) goto L22;
        return true;
    L22:
        return false;
    }

    private HlsMediaChunk getLastMediaChunk() {
        return this.mediaChunks.get(this.mediaChunks.size() - 1);
    }

    private TrackOutput getMappedTrackOutput(int r4, int r5) {
        Assertions.checkArgument(MAPPABLE_TYPES.contains(Integer.valueOf(r5)));
        int r0 = this.sampleQueueIndicesByType.get(r5, -1);
        if (r0 != (-1)) goto L7;
        return null;
    L7:
        if (this.sampleQueueMappingDoneByType.add(Integer.valueOf(r5)) == false) goto L10;
        this.sampleQueueTrackIds[r0] = r4;
    L10:
        if (this.sampleQueueTrackIds[r0] != r4) goto L13;
        return this.sampleQueues[r0];
    L13:
        return createDiscardingTrackOutput(r4, r5);
    }

    private static int getTrackTypeScore(int r1) {
        switch(r1) {
            case 1: goto L9;
            case 2: goto L7;
            case 3: goto L5;
            default: goto L3;
        };
    L3:
        return 0;
    L5:
        return 1;
    L7:
        return 3;
    L9:
        return 2;
    }

    private void initMediaChunkLoad(HlsMediaChunk r8) {
        this.sourceChunk = r8;
        this.upstreamTrackFormat = r8.trackFormat;
        this.pendingResetPositionUs = C0180C.TIME_UNSET;
        this.mediaChunks.add(r8);
        ImmutableList.Builder r0 = ImmutableList.builder();
        HlsSampleQueue[] r1 = this.sampleQueues;
        int r2 = r1.length;
        int r3 = 0;
        int r4 = 0;
    L3:
        if (r4 >= r2) goto L5;
        r0.add(Integer.valueOf(r1[r4].getWriteIndex()));
        r4 = r4 + 1;
        goto L3
    L5:
        r8.init(this, r0.build());
        HlsSampleQueue[] r5 = this.sampleQueues;
        int r6 = r5.length;
    L6:
        if (r3 >= r6) goto L11;
        HlsSampleQueue r7 = r5[r3];
        r7.setSourceChunk(r8);
        if (r8.shouldSpliceIn == false) goto L10;
        r7.splice();
    L10:
        r3 = r3 + 1;
        goto L6
    }

    private static boolean isMediaChunk(Chunk r1) {
        return r1 instanceof HlsMediaChunk;
    }

    private boolean isPendingReset() {
        if (this.pendingResetPositionUs == C0180C.TIME_UNSET) goto L5;
        return true;
    L5:
        return false;
    }

    @EnsuresNonNull({"trackGroupToSampleQueueIndex"})
    @RequiresNonNull({"trackGroups"})
    private void mapSampleQueuesToMatchTrackGroups() {
        int r0 = this.trackGroups.length;
        this.trackGroupToSampleQueueIndex = new int[r0];
        Arrays.fill(this.trackGroupToSampleQueueIndex, -1);
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L12;
        int r2 = 0;
    L6:
        if (r2 >= this.sampleQueues.length) goto L11;
        if (formatsMatch((Format) Assertions.checkStateNotNull(this.sampleQueues[r2].getUpstreamFormat()), this.trackGroups.get(r1).getFormat(0)) == true) goto L9;
        r2 = r2 + 1;
        goto L6
    L9:
        this.trackGroupToSampleQueueIndex[r1] = r2;
    L11:
        r1 = r1 + 1;
        goto L3
    L12:
        Iterator<HlsSampleStream> r3 = this.hlsSampleStreams.iterator();
    L14:
        if (r3.hasNext() == false) goto L16;
        r3.next().bindSampleQueue();
        goto L14
    }

    private void maybeFinishPrepare() {
        if (this.released == false) goto L5;
        return;
    L5:
        if (this.trackGroupToSampleQueueIndex == null) goto L7;
        return;
    L7:
        if (this.sampleQueuesBuilt == false) goto L25;
        HlsSampleQueue[] r0 = this.sampleQueues;
        int r1 = r0.length;
        int r2 = 0;
    L10:
        if (r2 >= r1) goto L16;
        if (r0[r2].getUpstreamFormat() == null) goto L13;
        r2 = r2 + 1;
        goto L10
    L13:
        return;
    L16:
        if (this.trackGroups == null) goto L18;
        mapSampleQueuesToMatchTrackGroups();
        return;
    L18:
        buildTracksFromSampleStreams();
        setIsPrepared();
        this.callback.onPrepared();
        return;
    }

    private void onTracksEnded() {
        this.sampleQueuesBuilt = true;
        maybeFinishPrepare();
    }

    private void resetSampleQueues() {
        HlsSampleQueue[] r0 = this.sampleQueues;
        int r1 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L5;
        r0[r3].reset(this.pendingResetUpstreamFormats);
        r3 = r3 + 1;
        goto L3
    L5:
        this.pendingResetUpstreamFormats = false;
    }

    private boolean seekInsideBufferUs(long r7, HlsMediaChunk r9) {
        int r0 = this.sampleQueues.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L15;
        HlsSampleQueue r2 = this.sampleQueues[r1];
        if (r9 == null) goto L7;
        boolean r4 = r2.seekTo(r9.getFirstSampleIndex(r1));
    L8:
        if (r4 == true) goto L14;
        if (this.sampleQueueIsAudioVideoFlags[r1] == true) goto L13;
        if (this.haveAudioVideoSampleQueues == true) goto L14;
    L13:
        return false;
    L14:
        r1 = r1 + 1;
        goto L3
    L7:
        r4 = r2.seekTo(r7, false);
        goto L8
    L15:
        return true;
    }

    @RequiresNonNull({"trackGroups", "optionalTrackGroups"})
    private void setIsPrepared() {
        this.prepared = true;
    }

    private void updateSampleStreams(SampleStream[] r6) {
        this.hlsSampleStreams.clear();
        int r0 = r6.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L8;
        SampleStream r2 = r6[r1];
        if (r2 == null) goto L7;
        this.hlsSampleStreams.add((HlsSampleStream) r2);
    L7:
        r1 = r1 + 1;
        goto L3
    }

    public int bindSampleQueueToSampleStream(int r5) {
        assertIsPrepared();
        Assertions.checkNotNull(this.trackGroupToSampleQueueIndex);
        int r0 = this.trackGroupToSampleQueueIndex[r5];
        if (r0 != (-1)) goto L10;
        if (this.optionalTrackGroups.contains(this.trackGroups.get(r5)) == false) goto L14;
        return -3;
    L14:
        return -2;
    L10:
        if (this.sampleQueuesEnabledStates[r0] == false) goto L12;
        return -2;
    L12:
        this.sampleQueuesEnabledStates[r0] = true;
        return r0;
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    public boolean continueLoading(LoadingInfo r29) {
        if (this.loadingFinished == false) goto L5;
    L40:
        return false;
    L5:
        if (this.loader.isLoading() == true) goto L40;
        if (this.loader.hasFatalError() == true) goto L40;
        if (isPendingReset() == false) goto L15;
        List<HlsMediaChunk> r1 = Collections.emptyList();
        long r3 = this.pendingResetPositionUs;
        HlsSampleQueue[] r5 = this.sampleQueues;
        int r6 = r5.length;
        int r7 = 0;
    L12:
        if (r7 >= r6) goto L14;
        r5[r7].setStartTimeUs(this.pendingResetPositionUs);
        r7 = r7 + 1;
        goto L12
    L14:
        long r8 = r3;
        List<HlsMediaChunk> r10 = r1;
    L20:
        this.nextChunkHolder.clear();
        HlsChunkSource r9 = this.chunkSource;
        if (this.prepared == false) goto L23;
    L26:
        boolean r11 = true;
    L27:
        r9.getNextChunk(r29, r8, r10, r11, this.nextChunkHolder);
        boolean r2 = this.nextChunkHolder.endOfStream;
        Chunk r4 = this.nextChunkHolder.chunk;
        Uri r12 = this.nextChunkHolder.playlistUrl;
        if (r2 == false) goto L31;
        this.pendingResetPositionUs = C0180C.TIME_UNSET;
        this.loadingFinished = true;
        return true;
    L31:
        if (r4 != null) goto L36;
        if (r12 == null) goto L34;
        this.callback.onPlaylistRefreshRequired(r12);
    L34:
        return false;
    L36:
        if (isMediaChunk(r4) == false) goto L38;
        initMediaChunkLoad((HlsMediaChunk) r4);
    L38:
        this.loadingChunk = r4;
        this.mediaSourceEventDispatcher.loadStarted(new LoadEventInfo(r4.loadTaskId, r4.dataSpec, this.loader.startLoading(r4, this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(r4.type))), r4.type, this.trackType, r4.trackFormat, r4.trackSelectionReason, r4.trackSelectionData, r4.startTimeUs, r4.endTimeUs);
        return true;
    L23:
        if (r10.isEmpty() == false) goto L26;
        r11 = false;
        goto L27
    L15:
        List<HlsMediaChunk> r13 = this.readOnlyMediaChunks;
        HlsMediaChunk r14 = getLastMediaChunk();
        if (r14.isLoadCompleted() == false) goto L18;
        long r15 = r14.endTimeUs;
    L19:
        r8 = r15;
        r10 = r13;
        goto L20
    L18:
        r15 = Math.max(this.lastSeekPositionUs, r14.startTimeUs);
        goto L19
    }

    public void continuePreparing() {
        if (this.prepared == true) goto L6;
        continueLoading(new LoadingInfo.Builder().setPlaybackPositionUs(this.lastSeekPositionUs).build());
        return;
    }

    public void discardBuffer(long r5, boolean r7) {
        if (this.sampleQueuesBuilt == true) goto L5;
        return;
    L5:
        if (isPendingReset() == true) goto L13;
        int r0 = this.sampleQueues.length;
        int r1 = 0;
    L8:
        if (r1 >= r0) goto L10;
        this.sampleQueues[r1].discardTo(r5, r7, this.sampleQueuesEnabledStates[r1]);
        r1 = r1 + 1;
        goto L8
    L10:
        return;
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public void endTracks() {
        this.tracksEnded = true;
        this.handler.post(this.onTracksEndedRunnable);
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
        HlsMediaChunk r2 = getLastMediaChunk();
        if (r2.isLoadCompleted() == false) goto L14;
        HlsMediaChunk r3 = r2;
    L18:
        if (r3 == null) goto L21;
        r0 = Math.max(r0, r3.endTimeUs);
    L21:
        if (this.sampleQueuesBuilt == false) goto L25;
        HlsSampleQueue[] r4 = this.sampleQueues;
        int r5 = r4.length;
        int r6 = 0;
    L23:
        if (r6 >= r5) goto L25;
        r0 = Math.max(r0, r4[r6].getLargestQueuedTimestampUs());
        r6 = r6 + 1;
    L25:
        return r0;
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

    public int getPrimaryTrackGroupIndex() {
        return this.primaryTrackGroupIndex;
    }

    public TrackGroupArray getTrackGroups() {
        assertIsPrepared();
        return this.trackGroups;
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    public boolean isLoading() {
        return this.loader.isLoading();
    }

    public boolean isReady(int r3) {
        if (isPendingReset() == false) goto L5;
    L7:
        return false;
    L5:
        if (this.sampleQueues[r3].isReady(this.loadingFinished) == false) goto L7;
        return true;
    }

    public boolean isVideoSampleStream() {
        if (this.primarySampleQueueType != 2) goto L5;
        return true;
    L5:
        return false;
    }

    /* JADX INFO: renamed from: lambda$onPlaylistUpdated$0$androidx-media3-exoplayer-hls-HlsSampleStreamWrapper */
    /* synthetic */ void m108x332ef84d(HlsMediaChunk r3) {
        this.callback.onPlaylistRefreshRequired(r3.playlistUrl);
    }

    public void maybeThrowError() throws IOException {
        this.loader.maybeThrowError();
        this.chunkSource.maybeThrowError();
    }

    public void maybeThrowError(int r2) throws IOException {
        maybeThrowError();
        this.sampleQueues[r2].maybeThrowError();
    }

    public void maybeThrowPrepareError() throws IOException {
        maybeThrowError();
        if (this.loadingFinished == true) goto L5;
        return;
    L5:
        if (this.prepared == false) goto L8;
        return;
    L8:
        throw ParserException.createForMalformedContainer("Loading finished before preparation is complete.", null);
    }

    public void onLoadCanceled(Chunk r14, long r15, long r17, boolean r19) {
        this.loadingChunk = null;
        LoadEventInfo r1 = new LoadEventInfo(r14.loadTaskId, r14.dataSpec, r14.getUri(), r14.getResponseHeaders(), r15, r17, r14.bytesLoaded());
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r14.loadTaskId);
        this.mediaSourceEventDispatcher.loadCanceled(r1, r14.type, this.trackType, r14.trackFormat, r14.trackSelectionReason, r14.trackSelectionData, r14.startTimeUs, r14.endTimeUs);
        if (r19 == false) goto L5;
        return;
    L5:
        if (isPendingReset() == false) goto L7;
    L8:
        resetSampleQueues();
    L10:
        if (this.enabledTrackGroupCount <= 0) goto L14;
        this.callback.onContinueLoadingRequested(this);
        return;
    L14:
        return;
    L7:
        if (this.enabledTrackGroupCount != 0) goto L10;
        goto L8
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
        this.mediaSourceEventDispatcher.loadCompleted(r1, r14.type, this.trackType, r14.trackFormat, r14.trackSelectionReason, r14.trackSelectionData, r14.startTimeUs, r14.endTimeUs);
        if (this.prepared == true) goto L5;
        continueLoading(new LoadingInfo.Builder().setPlaybackPositionUs(this.lastSeekPositionUs).build());
        return;
    L5:
        this.callback.onContinueLoadingRequested(this);
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Callback
    public /* bridge */ /* synthetic */ void onLoadCompleted(Loader.Loadable r7, long r8, long r10) {
        onLoadCompleted((Chunk) r7, r8, r10);
    }

    public Loader.LoadErrorAction onLoadError(Chunk r29, long r30, long r32, IOException r34, int r35) {
        boolean r15 = isMediaChunk(r29);
        if (r15 == true) goto L5;
    L14:
        long r26 = r29.bytesLoaded();
        LoadEventInfo r16 = new LoadEventInfo(r29.loadTaskId, r29.dataSpec, r29.getUri(), r29.getResponseHeaders(), r30, r32, r26);
        LoadErrorHandlingPolicy.LoadErrorInfo r5 = new LoadErrorHandlingPolicy.LoadErrorInfo(r16, new MediaLoadData(r29.type, this.trackType, r29.trackFormat, r29.trackSelectionReason, r29.trackSelectionData, Util.usToMs(r29.startTimeUs), Util.usToMs(r29.endTimeUs)), r34, r35);
        LoadErrorHandlingPolicy.FallbackSelection r7 = this.loadErrorHandlingPolicy.getFallbackSelectionFor(TrackSelectionUtil.createFallbackOptions(this.chunkSource.getTrackSelection()), r5);
        if (r7 != null) goto L17;
    L19:
        boolean r17 = false;
    L20:
        boolean r2 = false;
        if (r17 == false) goto L33;
        if (r15 == true) goto L24;
    L32:
        Loader.LoadErrorAction r18 = Loader.DONT_RETRY;
    L38:
        boolean r14 = !r18.isRetry();
        this.mediaSourceEventDispatcher.loadError(r16, r29.type, this.trackType, r29.trackFormat, r29.trackSelectionReason, r29.trackSelectionData, r29.startTimeUs, r29.endTimeUs, r34, r14);
        if (r14 == false) goto L41;
        this.loadingChunk = null;
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(r29.loadTaskId);
    L41:
        if (r17 == true) goto L43;
    L46:
        return r18;
    L43:
        if (this.prepared == true) goto L45;
        continueLoading(new LoadingInfo.Builder().setPlaybackPositionUs(this.lastSeekPositionUs).build());
        goto L46
    L45:
        this.callback.onContinueLoadingRequested(this);
        goto L46
    L24:
        if (r26 != 0) goto L32;
        if (this.mediaChunks.remove(this.mediaChunks.size() - 1) != r29) goto L28;
        r2 = true;
    L28:
        Assertions.checkState(r2);
        if (this.mediaChunks.isEmpty() == false) goto L31;
        this.pendingResetPositionUs = this.lastSeekPositionUs;
        goto L32
    L31:
        ((HlsMediaChunk) Iterables.getLast(this.mediaChunks)).invalidateExtractor();
        goto L32
    L33:
        long r9 = this.loadErrorHandlingPolicy.getRetryDelayMsFor(r5);
        if (r9 == C0180C.TIME_UNSET) goto L36;
        Loader.LoadErrorAction r3 = Loader.createRetryAction(false, r9);
    L37:
        r18 = r3;
        goto L38
    L36:
        r3 = Loader.DONT_RETRY_FATAL;
        goto L37
    L17:
        if (r7.type != 2) goto L19;
        r17 = this.chunkSource.maybeExcludeTrack(r29, r7.exclusionDurationMs);
        goto L20
    L5:
        if (((HlsMediaChunk) r29).isPublished() == true) goto L14;
        if ((r34 instanceof HttpDataSource.InvalidResponseCodeException) == false) goto L14;
        int r4 = ((HttpDataSource.InvalidResponseCodeException) r34).responseCode;
        if (r4 == 410) goto L13;
        if (r4 != 404) goto L14;
    L13:
        return Loader.RETRY;
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
        HlsSampleQueue[] r0 = this.sampleQueues;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].release();
        r2 = r2 + 1;
        goto L3
    }

    public void onNewExtractor() {
        this.sampleQueueMappingDoneByType.clear();
    }

    public boolean onPlaylistError(Uri r7, LoadErrorHandlingPolicy.LoadErrorInfo r8, boolean r9) {
        if (this.chunkSource.obtainsChunksForPlaylist(r7) == true) goto L5;
        return true;
    L5:
        long r2 = C0180C.TIME_UNSET;
        if (r9 == true) goto L13;
        LoadErrorHandlingPolicy.FallbackSelection r0 = this.loadErrorHandlingPolicy.getFallbackSelectionFor(TrackSelectionUtil.createFallbackOptions(this.chunkSource.getTrackSelection()), r8);
        if (r0 == null) goto L13;
        if (r0.type != 2) goto L13;
        r2 = r0.exclusionDurationMs;
    L13:
        if (this.chunkSource.onPlaylistError(r7, r2) == true) goto L15;
    L17:
        return false;
    L15:
        if (r2 == C0180C.TIME_UNSET) goto L17;
        return true;
    }

    public void onPlaylistUpdated() {
        if (this.mediaChunks.isEmpty() == false) goto L5;
        return;
    L5:
        final HlsMediaChunk r0 = (HlsMediaChunk) Iterables.getLast(this.mediaChunks);
        int r1 = this.chunkSource.getChunkPublicationState(r0);
        if (r1 != 1) goto L8;
        r0.publish();
        return;
    L8:
        if (r1 != 0) goto L11;
        this.handler.post(new HlsSampleStreamWrapper$$ExternalSyntheticLambda0(this, r0));
        return;
    L11:
        if (r1 == 2) goto L13;
        return;
    L13:
        if (this.loadingFinished == false) goto L15;
        return;
    L15:
        if (this.loader.isLoading() == false) goto L22;
        this.loader.cancelLoading();
        return;
    }

    @Override // androidx.media3.exoplayer.source.SampleQueue.UpstreamFormatChangedListener
    public void onUpstreamFormatChanged(Format r3) {
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    public void prepareWithMultivariantPlaylistInfo(TrackGroup[] r6, int r7, int... r8) {
        this.trackGroups = createTrackGroupArrayWithDrmInfo(r6);
        this.optionalTrackGroups = new HashSet();
        int r0 = r8.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        int r2 = r8[r1];
        this.optionalTrackGroups.add(this.trackGroups.get(r2));
        r1 = r1 + 1;
        goto L3
    L5:
        this.primaryTrackGroupIndex = r7;
        Handler r3 = this.handler;
        final Callback r4 = this.callback;
        Objects.requireNonNull(r4);
        r3.post(new HlsSampleStreamWrapper$$ExternalSyntheticLambda3(r4));
        setIsPrepared();
    }

    public int readData(int r12, FormatHolder r13, DecoderInputBuffer r14, int r15) {
        if (isPendingReset() == false) goto L6;
        return -3;
    L6:
        if (this.mediaChunks.isEmpty() == true) goto L18;
        int r0 = 0;
    L9:
        if (r0 >= (this.mediaChunks.size() - 1)) goto L13;
        if (finishedReadingChunk(this.mediaChunks.get(r0)) == false) goto L13;
        r0 = r0 + 1;
    L13:
        Util.removeRange(this.mediaChunks, 0, r0);
        HlsMediaChunk r3 = this.mediaChunks.get(0);
        Format r6 = r3.trackFormat;
        if (r6.equals(this.downstreamTrackFormat) == true) goto L16;
        this.mediaSourceEventDispatcher.downstreamFormatChanged(this.trackType, r6, r3.trackSelectionReason, r3.trackSelectionData, r3.startTimeUs);
    L16:
        this.downstreamTrackFormat = r6;
    L18:
        if (this.mediaChunks.isEmpty() == false) goto L20;
    L22:
        int r1 = this.sampleQueues[r12].read(r13, r14, r15, this.loadingFinished);
        if (r1 != (-5)) goto L38;
        Format r2 = (Format) Assertions.checkNotNull(r13.format);
        if (r12 != this.primarySampleQueueIndex) goto L37;
        int r4 = Ints.checkedCast(this.sampleQueues[r12].peekSourceId());
        int r5 = 0;
    L28:
        if (r5 >= this.mediaChunks.size()) goto L33;
        if (this.mediaChunks.get(r5).uid == r4) goto L33;
        r5 = r5 + 1;
    L33:
        if (r5 >= this.mediaChunks.size()) goto L35;
        Format r7 = this.mediaChunks.get(r5).trackFormat;
    L36:
        r2 = r2.withManifestFormatInfo(r7);
        goto L37
    L35:
        r7 = (Format) Assertions.checkNotNull(this.upstreamTrackFormat);
    L37:
        r13.format = r2;
    L38:
        return r1;
    L20:
        if (this.mediaChunks.get(0).isPublished() == true) goto L22;
        return -3;
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    public void reevaluateBuffer(long r5) {
        if (this.loader.hasFatalError() == false) goto L5;
        return;
    L5:
        if (isPendingReset() == false) goto L8;
        return;
    L8:
        if (this.loader.isLoading() == false) goto L13;
        Assertions.checkNotNull(this.loadingChunk);
        if (this.chunkSource.shouldCancelLoad(r5, this.loadingChunk, this.readOnlyMediaChunks) == false) goto L28;
        this.loader.cancelLoading();
        return;
    L28:
        return;
    L13:
        int r0 = this.readOnlyMediaChunks.size();
    L14:
        if (r0 <= 0) goto L19;
        if (this.chunkSource.getChunkPublicationState(this.readOnlyMediaChunks.get(r0 - 1)) != 2) goto L19;
        r0 = r0 - 1;
    L19:
        if (r0 >= this.readOnlyMediaChunks.size()) goto L21;
        discardUpstream(r0);
    L21:
        int r1 = this.chunkSource.getPreferredQueueSize(r5, this.readOnlyMediaChunks);
        if (r1 >= this.mediaChunks.size()) goto L29;
        discardUpstream(r1);
        return;
    }

    public void release() {
        if (this.prepared == false) goto L7;
        HlsSampleQueue[] r0 = this.sampleQueues;
        int r1 = r0.length;
        int r2 = 0;
    L5:
        if (r2 >= r1) goto L7;
        r0[r2].preRelease();
        r2 = r2 + 1;
    L7:
        this.chunkSource.reset();
        this.loader.release(this);
        this.handler.removeCallbacksAndMessages(null);
        this.released = true;
        this.hlsSampleStreams.clear();
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public void seekMap(SeekMap r1) {
    }

    public boolean seekToUs(long r8, boolean r10) {
        this.lastSeekPositionUs = r8;
        if (isPendingReset() == false) goto L6;
        this.pendingResetPositionUs = r8;
        return true;
    L6:
        HlsMediaChunk r0 = null;
        if (this.chunkSource.hasIndependentSegments() == false) goto L15;
        int r2 = 0;
    L10:
        if (r2 >= this.mediaChunks.size()) goto L15;
        HlsMediaChunk r3 = this.mediaChunks.get(r2);
        if (r3.startTimeUs == r8) goto L13;
        r2 = r2 + 1;
        goto L10
    L13:
        r0 = r3;
    L15:
        int r4 = 0;
        if (this.sampleQueuesBuilt == false) goto L21;
        if (r10 == true) goto L21;
        if (seekInsideBufferUs(r8, r0) == false) goto L21;
        return false;
    L21:
        this.pendingResetPositionUs = r8;
        this.loadingFinished = false;
        this.mediaChunks.clear();
        if (this.loader.isLoading() == true) goto L24;
        this.loader.clearFatalError();
        resetSampleQueues();
    L30:
        return true;
    L24:
        if (this.sampleQueuesBuilt == false) goto L28;
        HlsSampleQueue[] r5 = this.sampleQueues;
        int r6 = r5.length;
    L26:
        if (r4 >= r6) goto L28;
        r5[r4].discardToEnd();
        r4 = r4 + 1;
    L28:
        this.loader.cancelLoading();
        goto L30
    }

    public boolean selectTracks(ExoTrackSelection[] r20, boolean[] r21, SampleStream[] r22, boolean[] r23, long r24, boolean r26) {
        assertIsPrepared();
        int r12 = this.enabledTrackGroupCount;
        int r3 = 0;
    L4:
        if (r3 >= r20.length) goto L13;
        HlsSampleStream r6 = (HlsSampleStream) r22[r3];
        if (r6 == null) goto L12;
        if (r20[r3] != null) goto L10;
    L11:
        this.enabledTrackGroupCount--;
        r6.unbindSampleQueue();
        r22[r3] = null;
        goto L12
    L10:
        if (r21[r3] == false) goto L11;
    L12:
        r3 = r3 + 1;
        goto L4
    L13:
        if (r26 == false) goto L15;
    L22:
        boolean r7 = true;
    L23:
        ExoTrackSelection r14 = this.chunkSource.getTrackSelection();
        ExoTrackSelection r8 = r14;
        int r9 = 0;
        boolean r15 = r7;
    L25:
        if (r9 >= r20.length) goto L47;
        ExoTrackSelection r10 = r20[r9];
        if (r10 == null) goto L45;
        int r11 = this.trackGroups.indexOf(r10.getTrackGroup());
        if (r11 != this.primaryTrackGroupIndex) goto L33;
        r8 = r10;
        this.chunkSource.setTrackSelection(r10);
    L33:
        if (r22[r9] != null) goto L45;
        this.enabledTrackGroupCount++;
        r22[r9] = new HlsSampleStream(this, r11);
        r23[r9] = true;
        if (this.trackGroupToSampleQueueIndex == null) goto L45;
        ((HlsSampleStream) r22[r9]).bindSampleQueue();
        if (r15 == true) goto L45;
        HlsSampleQueue r4 = this.sampleQueues[this.trackGroupToSampleQueueIndex[r11]];
        if (r4.getReadIndex() != 0) goto L41;
    L43:
        boolean r13 = false;
    L44:
        r15 = r13;
        goto L45
    L41:
        if (r4.seekTo(r24, true) == true) goto L43;
        r13 = true;
    L45:
        r9 = r9 + 1;
        goto L25
    L47:
        if (this.enabledTrackGroupCount != 0) goto L59;
        this.chunkSource.reset();
        this.downstreamTrackFormat = null;
        this.pendingResetUpstreamFormats = true;
        this.mediaChunks.clear();
        if (this.loader.isLoading() == true) goto L51;
        resetSampleQueues();
    L85:
        updateSampleStreams(r22);
        this.seenFirstTrackSelection = true;
        return r15;
    L51:
        if (this.sampleQueuesBuilt == false) goto L55;
        HlsSampleQueue[] r5 = this.sampleQueues;
        int r16 = r5.length;
        int r17 = 0;
    L53:
        if (r17 >= r16) goto L55;
        r5[r17].discardToEnd();
        r17 = r17 + 1;
    L55:
        this.loader.cancelLoading();
        goto L85
    L59:
        if (this.mediaChunks.isEmpty() == true) goto L76;
        if (Util.areEqual(r8, r14) == true) goto L76;
        boolean r18 = false;
        if (this.seenFirstTrackSelection == true) goto L71;
        long r19 = 0;
        if (r24 >= 0) goto L67;
        r19 = -r24;
    L67:
        HlsMediaChunk r25 = getLastMediaChunk();
        MediaChunkIterator[] r110 = this.chunkSource.createMediaChunkIterators(r25, r24);
        ExoTrackSelection r27 = r8;
        r27.updateSelectedTrack(r24, r19, C0180C.TIME_UNSET, this.readOnlyMediaChunks, r110);
        if (r27.getSelectedIndexInTrackGroup() == this.chunkSource.getTrackGroup().indexOf(r25.trackFormat)) goto L72;
        r18 = true;
    L72:
        if (r18 == false) goto L76;
        boolean r28 = true;
        this.pendingResetUpstreamFormats = true;
        r15 = true;
    L77:
        if (r15 == false) goto L85;
        seekToUs(r24, r28);
        int r29 = 0;
    L80:
        if (r29 >= r22.length) goto L85;
        if (r22[r29] == null) goto L84;
        r23[r29] = true;
    L84:
        r29 = r29 + 1;
        goto L80
    L71:
        r18 = true;
    L76:
        r28 = r26;
        goto L77
    L15:
        if (this.seenFirstTrackSelection == false) goto L19;
        if (r12 == 0) goto L22;
    L21:
        r7 = false;
        goto L23
    L19:
        if (r24 == this.lastSeekPositionUs) goto L21;
        goto L21
    }

    public void setDrmInitData(DrmInitData r3) {
        if (Util.areEqual(this.drmInitData, r3) == true) goto L11;
        this.drmInitData = r3;
        int r0 = 0;
    L6:
        if (r0 >= this.sampleQueues.length) goto L15;
        if (this.sampleQueueIsAudioVideoFlags[r0] == false) goto L10;
        this.sampleQueues[r0].setDrmInitData(r3);
    L10:
        r0 = r0 + 1;
        goto L6
    L15:
        return;
    }

    public void setIsPrimaryTimestampSource(boolean r2) {
        this.chunkSource.setIsPrimaryTimestampSource(r2);
    }

    public void setSampleOffsetUs(long r5) {
        if (this.sampleOffsetUs == r5) goto L7;
        this.sampleOffsetUs = r5;
        HlsSampleQueue[] r0 = this.sampleQueues;
        int r1 = r0.length;
        int r2 = 0;
    L5:
        if (r2 >= r1) goto L9;
        r0[r2].setSampleOffsetUs(r5);
        r2 = r2 + 1;
        goto L5
    L9:
        return;
    }

    public int skipData(int r7, long r8) {
        if (isPendingReset() == false) goto L6;
        return 0;
    L6:
        HlsSampleQueue r0 = this.sampleQueues[r7];
        int r1 = r0.getSkipCount(r8, this.loadingFinished);
        HlsMediaChunk r2 = (HlsMediaChunk) Iterables.getLast(this.mediaChunks, null);
        if (r2 != null) goto L9;
    L11:
        r0.skip(r1);
        return r1;
    L9:
        if (r2.isPublished() == true) goto L11;
        int r3 = r0.getReadIndex();
        r1 = Math.min(r1, r2.getFirstSampleIndex(r7) - r3);
        goto L11
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public TrackOutput track(int r4, int r5) {
        TrackOutput r0 = null;
        if (MAPPABLE_TYPES.contains(Integer.valueOf(r5)) == false) goto L5;
        r0 = getMappedTrackOutput(r4, r5);
    L12:
        if (r0 != null) goto L19;
        if (this.tracksEnded == true) goto L16;
        r0 = createSampleQueue(r4, r5);
        goto L19
    L16:
        return createDiscardingTrackOutput(r4, r5);
    L19:
        if (r5 == 5) goto L21;
        return r0;
    L21:
        if (this.emsgUnwrappingTrackOutput != null) goto L24;
        this.emsgUnwrappingTrackOutput = new EmsgUnwrappingTrackOutput(r0, this.metadataType);
    L24:
        return this.emsgUnwrappingTrackOutput;
    L5:
        int r1 = 0;
    L7:
        if (r1 >= this.sampleQueues.length) goto L12;
        if (this.sampleQueueTrackIds[r1] == r4) goto L10;
        r1 = r1 + 1;
        goto L7
    L10:
        r0 = this.sampleQueues[r1];
        goto L12
    }

    public void unbindSampleQueue(int r4) {
        assertIsPrepared();
        Assertions.checkNotNull(this.trackGroupToSampleQueueIndex);
        int r0 = this.trackGroupToSampleQueueIndex[r4];
        Assertions.checkState(this.sampleQueuesEnabledStates[r0]);
        this.sampleQueuesEnabledStates[r0] = false;
    }
}
