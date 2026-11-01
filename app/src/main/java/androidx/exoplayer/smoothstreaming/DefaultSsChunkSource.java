package androidx.media3.exoplayer.smoothstreaming;

import android.net.Uri;
import android.os.SystemClock;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UriUtil;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.smoothstreaming.manifest.SsManifest;
import androidx.media3.exoplayer.source.BehindLiveWindowException;
import androidx.media3.exoplayer.source.chunk.BaseMediaChunkIterator;
import androidx.media3.exoplayer.source.chunk.BundledChunkExtractor;
import androidx.media3.exoplayer.source.chunk.Chunk;
import androidx.media3.exoplayer.source.chunk.ChunkExtractor;
import androidx.media3.exoplayer.source.chunk.ChunkHolder;
import androidx.media3.exoplayer.source.chunk.ContainerMediaChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunkIterator;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.trackselection.TrackSelectionUtil;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.CmcdData;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.LoaderErrorThrower;
import androidx.media3.extractor.mp4.FragmentedMp4Extractor;
import androidx.media3.extractor.mp4.Track;
import androidx.media3.extractor.mp4.TrackEncryptionBox;
import androidx.media3.extractor.text.DefaultSubtitleParserFactory;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DefaultSsChunkSource implements SsChunkSource {
    private final ChunkExtractor[] chunkExtractors;
    private final CmcdConfiguration cmcdConfiguration;
    private int currentManifestChunkOffset;
    private final DataSource dataSource;
    private IOException fatalError;
    private long lastChunkRequestRealtimeMs;
    private SsManifest manifest;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private final int streamElementIndex;
    private ExoTrackSelection trackSelection;

    public static final class Factory implements SsChunkSource.Factory {
        private final DataSource.Factory dataSourceFactory;
        private boolean parseSubtitlesDuringExtraction;
        private SubtitleParser.Factory subtitleParserFactory;

        public Factory(DataSource.Factory r2) {
            this.dataSourceFactory = r2;
            this.subtitleParserFactory = new DefaultSubtitleParserFactory();
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.SsChunkSource.Factory
        public SsChunkSource createChunkSource(LoaderErrorThrower r12, SsManifest r13, int r14, ExoTrackSelection r15, TransferListener r16, CmcdConfiguration r17) {
            DataSource r7 = this.dataSourceFactory.createDataSource();
            if (r16 == null) goto L6;
            r7.addTransferListener(r16);
        L6:
            return new DefaultSsChunkSource(r12, r13, r14, r15, r7, r17, this.subtitleParserFactory, this.parseSubtitlesDuringExtraction);
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.SsChunkSource.Factory
        public Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
            this.parseSubtitlesDuringExtraction = r1;
            return this;
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.SsChunkSource.Factory
        public /* bridge */ /* synthetic */ SsChunkSource.Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
            return experimentalParseSubtitlesDuringExtraction(r1);
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.SsChunkSource.Factory
        public Format getOutputTextFormat(Format r5) {
            if (this.parseSubtitlesDuringExtraction == true) goto L5;
        L12:
            return r5;
        L5:
            if (this.subtitleParserFactory.supportsFormat(r5) == false) goto L12;
            Format.Builder r0 = r5.buildUpon().setSampleMimeType(MimeTypes.APPLICATION_MEDIA3_CUES).setCueReplacementBehavior(this.subtitleParserFactory.getCueReplacementBehavior(r5));
            StringBuilder r1 = new StringBuilder().append(r5.sampleMimeType);
            if (r5.codecs == null) goto L9;
            String r2 = " " + r5.codecs;
        L11:
            return r0.setCodecs(r1.append(r2).toString()).setSubsampleOffsetUs(Long.MAX_VALUE).build();
        L9:
            r2 = "";
            goto L11
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.SsChunkSource.Factory
        public Factory setSubtitleParserFactory(SubtitleParser.Factory r1) {
            this.subtitleParserFactory = r1;
            return this;
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.SsChunkSource.Factory
        public /* bridge */ /* synthetic */ SsChunkSource.Factory setSubtitleParserFactory(SubtitleParser.Factory r1) {
            return setSubtitleParserFactory(r1);
        }
    }

    private static final class StreamElementIterator extends BaseMediaChunkIterator {
        private final SsManifest.StreamElement streamElement;
        private final int trackIndex;

        public StreamElementIterator(SsManifest.StreamElement r5, int r6, int r7) {
            super(r7, r5.chunkCount - 1);
            this.streamElement = r5;
            this.trackIndex = r6;
        }

        @Override // androidx.media3.exoplayer.source.chunk.MediaChunkIterator
        public long getChunkEndTimeUs() {
            long r0 = getChunkStartTimeUs();
            return this.streamElement.getChunkDurationUs((int) getCurrentIndex()) + r0;
        }

        @Override // androidx.media3.exoplayer.source.chunk.MediaChunkIterator
        public long getChunkStartTimeUs() {
            checkInBounds();
            return this.streamElement.getStartTimeUs((int) getCurrentIndex());
        }

        @Override // androidx.media3.exoplayer.source.chunk.MediaChunkIterator
        public DataSpec getDataSpec() {
            checkInBounds();
            return new DataSpec(this.streamElement.buildRequestUri(this.trackIndex, (int) getCurrentIndex()));
        }
    }

    public DefaultSsChunkSource(LoaderErrorThrower r28, SsManifest r29, int r30, ExoTrackSelection r31, DataSource r32, CmcdConfiguration r33, SubtitleParser.Factory r34, boolean r35) {
        ExoTrackSelection r3 = r31;
        this.manifestLoaderErrorThrower = r28;
        this.manifest = r29;
        this.streamElementIndex = r30;
        this.trackSelection = r3;
        this.dataSource = r32;
        this.cmcdConfiguration = r33;
        this.lastChunkRequestRealtimeMs = C0180C.TIME_UNSET;
        SsManifest.StreamElement r7 = r29.streamElements[r30];
        this.chunkExtractors = new ChunkExtractor[r3.length()];
        int r8 = 0;
    L4:
        if (r8 >= this.chunkExtractors.length) goto L18;
        int r11 = r3.getIndexInTrackGroup(r8);
        Format r9 = r7.formats[r11];
        if (r9.drmInitData == null) goto L8;
        TrackEncryptionBox[] r23 = ((SsManifest.ProtectionElement) Assertions.checkNotNull(r29.protectionElement)).trackEncryptionBoxes;
    L10:
        if (r7.type != 2) goto L12;
        int r10 = 4;
    L13:
        Track r12 = new Track(r11, r7.type, r7.timescale, C0180C.TIME_UNSET, r29.durationUs, r29.durationUs, r9, 0, r23, r10, null, null);
        int r4 = 3;
        if (r35 == true) goto L17;
        r4 = 3 | 32;
    L17:
        this.chunkExtractors[r8] = new BundledChunkExtractor(new FragmentedMp4Extractor(r34, r4, null, r12, ImmutableList.m212of(), null), r7.type, r9);
        r8 = r8 + 1;
        r3 = r31;
        goto L4
    L12:
        r10 = 0;
        goto L13
    L8:
        r23 = null;
        goto L10
    }

    private static MediaChunk newMediaChunk(Format r23, DataSource r24, Uri r25, int r26, long r27, long r29, long r31, int r33, Object r34, ChunkExtractor r35, CmcdData.Factory r36) {
        DataSpec r0 = new DataSpec.Builder().setUri(r25).build();
        if (r36 == null) goto L5;
        DataSpec r5 = r36.createCmcdData().addToDataSpec(r0);
    L7:
        return new ContainerMediaChunk(r24, r5, r23, r33, r34, r27, r29, r31, C0180C.TIME_UNSET, r26, 1, r27, r35);
    L5:
        r5 = r0;
        goto L7
    }

    private long resolveTimeToLiveEdgeUs(long r7) {
        if (this.manifest.isLive == true) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        SsManifest.StreamElement r0 = this.manifest.streamElements[this.streamElementIndex];
        int r1 = r0.chunkCount - 1;
        return (r0.getStartTimeUs(r1) + r0.getChunkDurationUs(r1)) - r7;
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkSource
    public long getAdjustedSeekPositionUs(long r10, SeekParameters r12) {
        SsManifest.StreamElement r0 = this.manifest.streamElements[this.streamElementIndex];
        int r1 = r0.getChunkIndex(r10);
        long r5 = r0.getStartTimeUs(r1);
        if (r5 < r10) goto L5;
    L7:
        long r7 = r5;
    L9:
        return r12.resolveSeekPositionUs(r10, r5, r7);
    L5:
        if (r1 >= (r0.chunkCount - 1)) goto L7;
        r7 = r0.getStartTimeUs(r1 + 1);
        goto L9
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkSource
    public final void getNextChunk(LoadingInfo r44, long r45, List<? extends MediaChunk> r47, ChunkHolder r48) {
        if (this.fatalError == null) goto L5;
        return;
    L5:
        SsManifest.StreamElement r5 = this.manifest.streamElements[this.streamElementIndex];
        if (r5.chunkCount != 0) goto L10;
        r48.endOfStream = !this.manifest.isLive;
        return;
    L10:
        if (r47.isEmpty() == false) goto L12;
        int r6 = r5.getChunkIndex(r45);
        List<? extends MediaChunk> r14 = r47;
    L17:
        if (r6 < r5.chunkCount) goto L20;
        r48.endOfStream = !this.manifest.isLive;
        return;
    L20:
        long r8 = r44.playbackPositionUs;
        long r10 = r45 - r8;
        long r12 = resolveTimeToLiveEdgeUs(r8);
        MediaChunkIterator[] r15 = new MediaChunkIterator[this.trackSelection.length()];
        int r7 = 0;
    L22:
        if (r7 >= r15.length) goto L24;
        r15[r7] = new StreamElementIterator(r5, this.trackSelection.getIndexInTrackGroup(r7), r6);
        r7 = r7 + 1;
        goto L22
    L24:
        this.trackSelection.updateSelectedTrack(r8, r10, r12, r14, r15);
        long r20 = r5.getStartTimeUs(r6);
        long r22 = r20 + r5.getChunkDurationUs(r6);
        if (r47.isEmpty() == false) goto L27;
        long r24 = r45;
    L28:
        int r19 = r6 + this.currentManifestChunkOffset;
        int r2 = this.trackSelection.getSelectedIndex();
        ChunkExtractor r28 = this.chunkExtractors[r2];
        int r3 = this.trackSelection.getIndexInTrackGroup(r2);
        Uri r9 = r5.buildRequestUri(r3, r6);
        if (this.cmcdConfiguration == null) goto L34;
        CmcdData.Factory r16 = new CmcdData.Factory(this.cmcdConfiguration, this.trackSelection, Math.max(0, r10), r44.playbackSpeed, CmcdData.Factory.STREAMING_FORMAT_SS, this.manifest.isLive, r44.rebufferedSince(this.lastChunkRequestRealtimeMs), r47.isEmpty()).setChunkDurationUs(r22 - r20).setObjectType(CmcdData.Factory.getObjectType(this.trackSelection));
        if ((r6 + 1) >= r5.chunkCount) goto L33;
        r16.setNextObjectRequest(UriUtil.getRelativePath(r9, r5.buildRequestUri(r3, r6 + 1)));
    L33:
        CmcdData.Factory r29 = r16;
    L35:
        this.lastChunkRequestRealtimeMs = SystemClock.elapsedRealtime();
        r48.chunk = newMediaChunk(this.trackSelection.getSelectedFormat(), this.dataSource, r9, r19, r20, r22, r24, this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), r28, r29);
        return;
    L34:
        r29 = null;
        goto L35
    L27:
        r24 = -9223372036854775807L;
        goto L28
    L12:
        r14 = r47;
        r6 = (int) (r14.get(r47.size() - 1).getNextChunkIndex() - ((long) this.currentManifestChunkOffset));
        if (r6 >= 0) goto L17;
        this.fatalError = new BehindLiveWindowException();
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkSource
    public int getPreferredQueueSize(long r3, List<? extends MediaChunk> r5) {
        if (this.fatalError != null) goto L10;
        if (this.trackSelection.length() < 2) goto L10;
        return this.trackSelection.evaluateQueueSize(r3, r5);
    L10:
        return r5.size();
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkSource
    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) goto L7;
        this.manifestLoaderErrorThrower.maybeThrowError();
        return;
    L7:
        throw this.fatalError;
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkSource
    public void onChunkLoadCompleted(Chunk r1) {
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkSource
    public boolean onChunkLoadError(Chunk r6, boolean r7, LoadErrorHandlingPolicy.LoadErrorInfo r8, LoadErrorHandlingPolicy r9) {
        LoadErrorHandlingPolicy.FallbackSelection r0 = r9.getFallbackSelectionFor(TrackSelectionUtil.createFallbackOptions(this.trackSelection), r8);
        if (r7 == false) goto L10;
        if (r0 == null) goto L10;
        if (r0.type != 2) goto L10;
        if (this.trackSelection.excludeTrack(this.trackSelection.indexOf(r6.trackFormat), r0.exclusionDurationMs) == false) goto L10;
        return true;
    L10:
        return false;
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkSource
    public void release() {
        ChunkExtractor[] r0 = this.chunkExtractors;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].release();
        r2 = r2 + 1;
        goto L3
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkSource
    public boolean shouldCancelLoad(long r2, Chunk r4, List<? extends MediaChunk> r5) {
        if (this.fatalError == null) goto L7;
        return false;
    L7:
        return this.trackSelection.shouldCancelChunkLoad(r2, r4, r5);
    }

    @Override // androidx.media3.exoplayer.smoothstreaming.SsChunkSource
    public void updateManifest(SsManifest r10) {
        SsManifest.StreamElement r0 = this.manifest.streamElements[this.streamElementIndex];
        int r1 = r0.chunkCount;
        SsManifest.StreamElement r2 = r10.streamElements[this.streamElementIndex];
        if (r1 != 0) goto L5;
    L11:
        this.currentManifestChunkOffset += r1;
    L12:
        this.manifest = r10;
        return;
    L5:
        if (r2.chunkCount == 0) goto L11;
        long r3 = r0.getStartTimeUs(r1 - 1) + r0.getChunkDurationUs(r1 - 1);
        long r5 = r2.getStartTimeUs(0);
        if (r3 > r5) goto L10;
        this.currentManifestChunkOffset += r1;
        goto L12
    L10:
        this.currentManifestChunkOffset += r0.getChunkIndex(r5);
        goto L12
    }

    @Override // androidx.media3.exoplayer.smoothstreaming.SsChunkSource
    public void updateTrackSelection(ExoTrackSelection r1) {
        this.trackSelection = r1;
    }
}
