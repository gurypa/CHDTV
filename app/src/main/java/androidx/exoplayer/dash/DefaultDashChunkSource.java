package androidx.media3.exoplayer.dash;

import android.os.SystemClock;
import android.util.Pair;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UriUtil;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.dash.manifest.AdaptationSet;
import androidx.media3.exoplayer.dash.manifest.BaseUrl;
import androidx.media3.exoplayer.dash.manifest.DashManifest;
import androidx.media3.exoplayer.dash.manifest.RangedUri;
import androidx.media3.exoplayer.dash.manifest.Representation;
import androidx.media3.exoplayer.source.BehindLiveWindowException;
import androidx.media3.exoplayer.source.chunk.BaseMediaChunkIterator;
import androidx.media3.exoplayer.source.chunk.BundledChunkExtractor;
import androidx.media3.exoplayer.source.chunk.Chunk;
import androidx.media3.exoplayer.source.chunk.ChunkExtractor;
import androidx.media3.exoplayer.source.chunk.ChunkHolder;
import androidx.media3.exoplayer.source.chunk.ContainerMediaChunk;
import androidx.media3.exoplayer.source.chunk.InitializationChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunkIterator;
import androidx.media3.exoplayer.source.chunk.SingleSampleMediaChunk;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.CmcdData;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.LoaderErrorThrower;
import androidx.media3.extractor.ChunkIndex;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public class DefaultDashChunkSource implements DashChunkSource {
    private final int[] adaptationSetIndices;
    private final BaseUrlExclusionList baseUrlExclusionList;
    private final CmcdConfiguration cmcdConfiguration;
    private final DataSource dataSource;
    private final long elapsedRealtimeOffsetMs;
    private IOException fatalError;
    private long lastChunkRequestRealtimeMs;
    private DashManifest manifest;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private final int maxSegmentsPerLoad;
    private boolean missingLastSegment;
    private int periodIndex;
    private final PlayerEmsgHandler.PlayerTrackEmsgHandler playerTrackEmsgHandler;
    protected final RepresentationHolder[] representationHolders;
    private ExoTrackSelection trackSelection;
    private final int trackType;

    public static final class Factory implements DashChunkSource.Factory {
        private final ChunkExtractor.Factory chunkExtractorFactory;
        private final DataSource.Factory dataSourceFactory;
        private final int maxSegmentsPerLoad;

        public Factory(DataSource.Factory r2) {
            this(r2, 1);
        }

        public Factory(DataSource.Factory r2, int r3) {
            this(BundledChunkExtractor.FACTORY, r2, r3);
        }

        public Factory(ChunkExtractor.Factory r1, DataSource.Factory r2, int r3) {
            this.chunkExtractorFactory = r1;
            this.dataSourceFactory = r2;
            this.maxSegmentsPerLoad = r3;
        }

        @Override // androidx.media3.exoplayer.dash.DashChunkSource.Factory
        public DashChunkSource createDashChunkSource(LoaderErrorThrower r22, DashManifest r23, BaseUrlExclusionList r24, int r25, int[] r26, ExoTrackSelection r27, int r28, long r29, boolean r31, List<Format> r32, PlayerEmsgHandler.PlayerTrackEmsgHandler r33, TransferListener r34, PlayerId r35, CmcdConfiguration r36) {
            DataSource r12 = this.dataSourceFactory.createDataSource();
            if (r34 == null) goto L6;
            r12.addTransferListener(r34);
        L6:
            return new DefaultDashChunkSource(this.chunkExtractorFactory, r22, r23, r24, r25, r26, r27, r28, r12, r29, this.maxSegmentsPerLoad, r31, r32, r33, r35, r36);
        }

        @Override // androidx.media3.exoplayer.dash.DashChunkSource.Factory
        public /* bridge */ /* synthetic */ DashChunkSource.Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
            return experimentalParseSubtitlesDuringExtraction(r1);
        }

        @Override // androidx.media3.exoplayer.dash.DashChunkSource.Factory
        public Factory experimentalParseSubtitlesDuringExtraction(boolean r2) {
            this.chunkExtractorFactory.experimentalParseSubtitlesDuringExtraction(r2);
            return this;
        }

        @Override // androidx.media3.exoplayer.dash.DashChunkSource.Factory
        public Format getOutputTextFormat(Format r2) {
            return this.chunkExtractorFactory.getOutputTextFormat(r2);
        }

        @Override // androidx.media3.exoplayer.dash.DashChunkSource.Factory
        public /* bridge */ /* synthetic */ DashChunkSource.Factory setSubtitleParserFactory(SubtitleParser.Factory r1) {
            return setSubtitleParserFactory(r1);
        }

        @Override // androidx.media3.exoplayer.dash.DashChunkSource.Factory
        public Factory setSubtitleParserFactory(SubtitleParser.Factory r2) {
            this.chunkExtractorFactory.setSubtitleParserFactory(r2);
            return this;
        }
    }

    protected static final class RepresentationHolder {
        final ChunkExtractor chunkExtractor;
        private final long periodDurationUs;
        public final Representation representation;
        public final DashSegmentIndex segmentIndex;
        private final long segmentNumShift;
        public final BaseUrl selectedBaseUrl;

        RepresentationHolder(long r1, Representation r3, BaseUrl r4, ChunkExtractor r5, long r6, DashSegmentIndex r8) {
            this.periodDurationUs = r1;
            this.representation = r3;
            this.selectedBaseUrl = r4;
            this.segmentNumShift = r6;
            this.chunkExtractor = r5;
            this.segmentIndex = r8;
        }

        static /* synthetic */ long access$000(RepresentationHolder r2) {
            return r2.periodDurationUs;
        }

        RepresentationHolder copyWithNewRepresentation(long r27, Representation r29) throws BehindLiveWindowException {
            DashSegmentIndex r9 = this.representation.getIndex();
            DashSegmentIndex r10 = r29.getIndex();
            if (r9 != null) goto L7;
            return new RepresentationHolder(r27, r29, this.selectedBaseUrl, this.chunkExtractor, this.segmentNumShift, r9);
        L7:
            if (r9.isExplicit() == false) goto L9;
            long r12 = r9.getSegmentCount(r27);
            if (r12 == 0) goto L13;
            Assertions.checkStateNotNull(r10);
            long r14 = r9.getFirstSegmentNum();
            long r4 = r9.getTimeUs(r14);
            long r6 = (r14 + r12) - 1;
            long r18 = r9.getTimeUs(r6) + r9.getDurationUs(r6, r27);
            long r13 = r10.getFirstSegmentNum();
            long r15 = r10.getTimeUs(r13);
            long r7 = this.segmentNumShift;
            if (r18 != r15) goto L18;
            long r8 = r7 + ((r6 + 1) - r13);
        L24:
            return new RepresentationHolder(r27, r29, this.selectedBaseUrl, this.chunkExtractor, r8, r10);
        L18:
            if (r18 < r15) goto L26;
            if (r15 >= r4) goto L22;
            r8 = r7 - (r10.getSegmentNum(r4, r27) - r14);
            goto L24
        L22:
            r8 = r7 + (r9.getSegmentNum(r15, r27) - r13);
            goto L24
        L26:
            throw new BehindLiveWindowException();
        L13:
            return new RepresentationHolder(r27, r29, this.selectedBaseUrl, this.chunkExtractor, this.segmentNumShift, r10);
        L9:
            return new RepresentationHolder(r27, r29, this.selectedBaseUrl, this.chunkExtractor, this.segmentNumShift, r10);
        }

        RepresentationHolder copyWithNewSegmentIndex(DashSegmentIndex r10) {
            return new RepresentationHolder(this.periodDurationUs, this.representation, this.selectedBaseUrl, this.chunkExtractor, this.segmentNumShift, r10);
        }

        RepresentationHolder copyWithNewSelectedBaseUrl(BaseUrl r10) {
            return new RepresentationHolder(this.periodDurationUs, this.representation, r10, this.chunkExtractor, this.segmentNumShift, this.segmentIndex);
        }

        public long getFirstAvailableSegmentNum(long r5) {
            return ((DashSegmentIndex) Assertions.checkStateNotNull(this.segmentIndex)).getFirstAvailableSegmentNum(this.periodDurationUs, r5) + this.segmentNumShift;
        }

        public long getFirstSegmentNum() {
            return ((DashSegmentIndex) Assertions.checkStateNotNull(this.segmentIndex)).getFirstSegmentNum() + this.segmentNumShift;
        }

        public long getLastAvailableSegmentNum(long r6) {
            return (getFirstAvailableSegmentNum(r6) + ((DashSegmentIndex) Assertions.checkStateNotNull(this.segmentIndex)).getAvailableSegmentCount(this.periodDurationUs, r6)) - 1;
        }

        public long getSegmentCount() {
            return ((DashSegmentIndex) Assertions.checkStateNotNull(this.segmentIndex)).getSegmentCount(this.periodDurationUs);
        }

        public long getSegmentEndTimeUs(long r8) {
            return getSegmentStartTimeUs(r8) + ((DashSegmentIndex) Assertions.checkStateNotNull(this.segmentIndex)).getDurationUs(r8 - this.segmentNumShift, this.periodDurationUs);
        }

        public long getSegmentNum(long r5) {
            return ((DashSegmentIndex) Assertions.checkStateNotNull(this.segmentIndex)).getSegmentNum(r5, this.periodDurationUs) + this.segmentNumShift;
        }

        public long getSegmentStartTimeUs(long r4) {
            return ((DashSegmentIndex) Assertions.checkStateNotNull(this.segmentIndex)).getTimeUs(r4 - this.segmentNumShift);
        }

        public RangedUri getSegmentUrl(long r4) {
            return ((DashSegmentIndex) Assertions.checkStateNotNull(this.segmentIndex)).getSegmentUrl(r4 - this.segmentNumShift);
        }

        public boolean isSegmentAvailableAtFullNetworkSpeed(long r5, long r7) {
            if (((DashSegmentIndex) Assertions.checkStateNotNull(this.segmentIndex)).isExplicit() == false) goto L6;
            return true;
        L6:
            if (r7 != C0180C.TIME_UNSET) goto L8;
            return true;
        L8:
            if (getSegmentEndTimeUs(r5) <= r7) goto L13;
            return false;
        L13:
            return true;
        }
    }

    protected static final class RepresentationSegmentIterator extends BaseMediaChunkIterator {
        private final long nowPeriodTimeUs;
        private final RepresentationHolder representationHolder;

        public RepresentationSegmentIterator(RepresentationHolder r1, long r2, long r4, long r6) {
            super(r2, r4);
            this.representationHolder = r1;
            this.nowPeriodTimeUs = r6;
        }

        @Override // androidx.media3.exoplayer.source.chunk.MediaChunkIterator
        public long getChunkEndTimeUs() {
            checkInBounds();
            return this.representationHolder.getSegmentEndTimeUs(getCurrentIndex());
        }

        @Override // androidx.media3.exoplayer.source.chunk.MediaChunkIterator
        public long getChunkStartTimeUs() {
            checkInBounds();
            return this.representationHolder.getSegmentStartTimeUs(getCurrentIndex());
        }

        @Override // androidx.media3.exoplayer.source.chunk.MediaChunkIterator
        public DataSpec getDataSpec() {
            checkInBounds();
            long r0 = getCurrentIndex();
            RangedUri r2 = this.representationHolder.getSegmentUrl(r0);
            if (this.representationHolder.isSegmentAvailableAtFullNetworkSpeed(r0, this.nowPeriodTimeUs) == false) goto L5;
            int r3 = 0;
        L7:
            return DashUtil.buildDataSpec(this.representationHolder.representation, this.representationHolder.selectedBaseUrl.url, r2, r3, ImmutableMap.m231of());
        L5:
            r3 = 8;
            goto L7
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [androidx.media3.exoplayer.dash.DefaultDashChunkSource, java.lang.Object] */
    public DefaultDashChunkSource(ChunkExtractor.Factory r27, LoaderErrorThrower r28, DashManifest r29, BaseUrlExclusionList r30, int r31, int[] r32, ExoTrackSelection r33, int r34, DataSource r35, long r36, int r38, boolean r39, List<Format> r40, PlayerEmsgHandler.PlayerTrackEmsgHandler r41, PlayerId r42, CmcdConfiguration r43) {
        ?? r0 = new Object();
        r0.manifestLoaderErrorThrower = r28;
        r0.manifest = r29;
        r0.baseUrlExclusionList = r30;
        r0.adaptationSetIndices = r32;
        r0.trackSelection = r33;
        int r8 = r34;
        r0.trackType = r8;
        r0.dataSource = r35;
        r0.periodIndex = r31;
        r0.elapsedRealtimeOffsetMs = r36;
        r0.maxSegmentsPerLoad = r38;
        PlayerEmsgHandler.PlayerTrackEmsgHandler r12 = r41;
        r0.playerTrackEmsgHandler = r12;
        r0.cmcdConfiguration = r43;
        r0.lastChunkRequestRealtimeMs = C0180C.TIME_UNSET;
        long r17 = r29.getPeriodDurationUs(r31);
        ArrayList<Representation> r5 = r0.getRepresentations();
        r0.representationHolders = new RepresentationHolder[r33.length()];
        int r6 = 0;
        DefaultDashChunkSource r1 = r0;
    L4:
        if (r6 >= r1.representationHolders.length) goto L10;
        Representation r11 = r5.get(r33.getIndexInTrackGroup(r6));
        BaseUrl r25 = r30.selectBaseUrl(r11.baseUrls);
        RepresentationHolder[] r13 = r1.representationHolders;
        if (r25 == null) goto L8;
        BaseUrl r20 = r25;
    L9:
        r13[r6] = new RepresentationHolder(r17, r11, r20, r27.createProgressiveMediaExtractor(r8, r11.format, r39, r40, r12, r42), 0, r11.getIndex());
        r6 = r6 + 1;
        r1 = this;
        r8 = r34;
        r12 = r41;
        goto L4
    L8:
        r20 = r11.baseUrls.get(0);
        goto L9
    }

    private LoadErrorHandlingPolicy.FallbackOptions createFallbackOptions(ExoTrackSelection r8, List<BaseUrl> r9) {
        long r0 = SystemClock.elapsedRealtime();
        int r2 = r8.length();
        int r3 = 0;
        int r4 = 0;
    L3:
        if (r4 >= r2) goto L8;
        if (r8.isTrackExcluded(r4, r0) == false) goto L7;
        r3 = r3 + 1;
    L7:
        r4 = r4 + 1;
        goto L3
    L8:
        int r5 = BaseUrlExclusionList.getPriorityCount(r9);
        return new LoadErrorHandlingPolicy.FallbackOptions(r5, r5 - this.baseUrlExclusionList.getPriorityCountAfterExclusion(r9), r2, r3);
    }

    private long getAvailableLiveDurationUs(long r15, long r17) {
        if (this.manifest.dynamic == false) goto L10;
        if (this.representationHolders[0].getSegmentCount() == 0) goto L12;
        long r6 = this.representationHolders[0].getLastAvailableSegmentNum(r15);
        long r0 = this.representationHolders[0].getSegmentEndTimeUs(r6);
        return Math.max(0, Math.min(getNowPeriodTimeUs(r15), r0) - r17);
    L12:
        return C0180C.TIME_UNSET;
    L10:
        return C0180C.TIME_UNSET;
    }

    private Pair<String, String> getNextObjectAndRangeRequest(long r11, RangedUri r13, RepresentationHolder r14) {
        if ((r11 + 1) < r14.getSegmentCount()) goto L6;
        return null;
    L6:
        RangedUri r0 = r14.getSegmentUrl(1 + r11);
        String r3 = UriUtil.getRelativePath(r13.resolveUri(r14.selectedBaseUrl.url), r0.resolveUri(r14.selectedBaseUrl.url));
        String r4 = r0.start + "-";
        if (r0.length == (-1)) goto L10;
        r4 = r4 + (r0.start + r0.length);
    L10:
        return new Pair(r3, r4);
    }

    private long getNowPeriodTimeUs(long r5) {
        if (this.manifest.availabilityStartTimeMs != C0180C.TIME_UNSET) goto L6;
        return C0180C.TIME_UNSET;
    L6:
        return r5 - Util.msToUs(this.manifest.availabilityStartTimeMs + this.manifest.getPeriod(this.periodIndex).startMs);
    }

    @RequiresNonNull({"manifest", "adaptationSetIndices"})
    private ArrayList<Representation> getRepresentations() {
        List<AdaptationSet> r0 = this.manifest.getPeriod(this.periodIndex).adaptationSets;
        ArrayList<Representation> r1 = new ArrayList();
        int[] r2 = this.adaptationSetIndices;
        int r3 = r2.length;
        int r4 = 0;
    L3:
        if (r4 >= r3) goto L5;
        r1.addAll(r0.get(r2[r4]).representations);
        r4 = r4 + 1;
        goto L3
    L5:
        return r1;
    }

    private long getSegmentNum(RepresentationHolder r9, MediaChunk r10, long r11, long r13, long r15) {
        if (r10 == null) goto L5;
        return r10.getNextChunkIndex();
    L5:
        return Util.constrainValue(r9.getSegmentNum(r11), r13, r15);
    }

    private RepresentationHolder updateSelectedBaseUrl(int r4) {
        RepresentationHolder r0 = this.representationHolders[r4];
        BaseUrl r1 = this.baseUrlExclusionList.selectBaseUrl(r0.representation.baseUrls);
        if (r1 != null) goto L5;
        return r0;
    L5:
        if (r1.equals(r0.selectedBaseUrl) == true) goto L9;
        RepresentationHolder r2 = r0.copyWithNewSelectedBaseUrl(r1);
        this.representationHolders[r4] = r2;
        return r2;
    L9:
        return r0;
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkSource
    public long getAdjustedSeekPositionUs(long r15, SeekParameters r17) {
        RepresentationHolder[] r0 = this.representationHolders;
        int r3 = r0.length;
        int r4 = 0;
    L3:
        if (r4 >= r3) goto L20;
        RepresentationHolder r7 = r0[r4];
        if (r7.segmentIndex == null) goto L19;
        long r8 = r7.getSegmentCount();
        if (r8 == 0) goto L19;
        long r10 = r7.getSegmentNum(r15);
        long r5 = r7.getSegmentStartTimeUs(r10);
        if (r5 < r15) goto L12;
    L16:
        long r6 = r5;
    L18:
        return r17.resolveSeekPositionUs(r15, r5, r6);
    L12:
        if (r8 != (-1)) goto L14;
    L15:
        r6 = r7.getSegmentStartTimeUs(1 + r10);
        goto L18
    L14:
        if (r10 >= ((r7.getFirstSegmentNum() + r8) - 1)) goto L16;
    L19:
        r4 = r4 + 1;
        goto L3
    L20:
        return r15;
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkSource
    public void getNextChunk(LoadingInfo r47, long r48, List<? extends MediaChunk> r50, ChunkHolder r51) {
        if (this.fatalError == null) goto L5;
        return;
    L5:
        long r10 = r47.playbackPositionUs;
        long r19 = r48 - r10;
        long r12 = (Util.msToUs(this.manifest.availabilityStartTimeMs) + Util.msToUs(this.manifest.getPeriod(this.periodIndex).startMs)) + r48;
        if (this.playerTrackEmsgHandler != null) goto L8;
    L10:
        long r1 = Util.msToUs(Util.getNowUnixTimeMs(this.elapsedRealtimeOffsetMs));
        long r27 = getNowPeriodTimeUs(r1);
        boolean r4 = true;
        if (r50.isEmpty() == false) goto L13;
        MediaChunk r3 = null;
    L14:
        MediaChunkIterator[] r6 = new MediaChunkIterator[this.trackSelection.length()];
        int r7 = 0;
    L16:
        if (r7 >= r6.length) goto L25;
        RepresentationHolder r8 = this.representationHolders[r7];
        if (r8.segmentIndex != null) goto L20;
        r6[r7] = MediaChunkIterator.EMPTY;
        long r14 = r1;
        MediaChunkIterator[] r16 = r6;
        long r31 = r12;
        boolean r30 = true;
        MediaChunk r13 = r3;
        int r15 = r7;
    L24:
        r7 = r15 + 1;
        r3 = r13;
        r1 = r14;
        r6 = r16;
        r4 = r30;
        r12 = r31;
        goto L16
    L20:
        long r5 = r8.getFirstAvailableSegmentNum(r1);
        long r25 = r8.getLastAvailableSegmentNum(r1);
        r14 = r1;
        MediaChunk r2 = r3;
        r31 = r12;
        r16 = r6;
        r30 = true;
        r15 = r7;
        long r23 = getSegmentNum(r8, r2, r48, r5, r25);
        r13 = r2;
        if (r23 >= r5) goto L23;
        r16[r15] = MediaChunkIterator.EMPTY;
        goto L24
    L23:
        r16[r15] = new RepresentationSegmentIterator(updateSelectedBaseUrl(r15), r23, r25, r27);
        goto L24
    L25:
        long r17 = r1;
        boolean r32 = r4;
        MediaChunk r18 = r3;
        long r21 = getAvailableLiveDurationUs(r17, r10);
        this.trackSelection.updateSelectedTrack(r10, r19, r21, r50, r6);
        int r110 = this.trackSelection.getSelectedIndex();
        if (this.cmcdConfiguration != null) goto L28;
        CmcdData.Factory r9 = null;
    L29:
        this.lastChunkRequestRealtimeMs = SystemClock.elapsedRealtime();
        RepresentationHolder r11 = updateSelectedBaseUrl(r110);
        if (r11.chunkExtractor == null) goto L46;
        Representation r20 = r11.representation;
        if (r11.chunkExtractor.getSampleFormats() != null) goto L34;
        RangedUri r22 = r20.getInitializationUri();
    L36:
        if (r11.segmentIndex != null) goto L38;
        RangedUri r24 = r20.getIndexUri();
    L39:
        if (r22 != null) goto L43;
        if (r24 == null) goto L46;
    L43:
        r51.chunk = newInitializationChunk(r11, this.dataSource, this.trackSelection.getSelectedFormat(), this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), r22, r24, r9);
        return;
    L38:
        r24 = null;
        goto L39
    L34:
        r22 = null;
    L46:
        long r26 = RepresentationHolder.access$000(r11);
        boolean r28 = false;
        if (this.manifest.dynamic == true) goto L49;
    L51:
        boolean r29 = false;
    L52:
        boolean r111 = r29;
        long r33 = C0180C.TIME_UNSET;
        if (r111 == true) goto L55;
    L58:
        boolean r34 = r32;
    L60:
        if (r11.getSegmentCount() != 0) goto L63;
        r51.endOfStream = r34;
        return;
    L63:
        long r35 = r11.getFirstAvailableSegmentNum(r17);
        long r36 = r11.getLastAvailableSegmentNum(r17);
        if (r111 == false) goto L69;
        long r112 = r11.getSegmentEndTimeUs(r36);
        if ((r112 + (r112 - r11.getSegmentStartTimeUs(r36))) < r26) goto L68;
        r28 = r32;
    L68:
        r34 = r34 & r28;
    L69:
        boolean r113 = r34;
        long r114 = getSegmentNum(r11, r18, r48, r35, r36);
        if (r114 >= r35) goto L74;
        this.fatalError = new BehindLiveWindowException();
        return;
    L74:
        if (r114 > r36) goto L98;
        if (this.missingLastSegment == true) goto L78;
    L80:
        if (r113 == true) goto L82;
    L85:
        int r37 = (int) Math.min(this.maxSegmentsPerLoad, (r36 - r114) + 1);
        if (r26 == C0180C.TIME_UNSET) goto L93;
    L88:
        if (r37 <= 1) goto L93;
        if (r11.getSegmentStartTimeUs((((long) r37) + r114) - 1) < r26) goto L93;
        r37 = r37 - 1;
    L93:
        if (r50.isEmpty() == false) goto L95;
        r33 = r48;
    L95:
        r51.chunk = newMediaChunk(r11, this.dataSource, this.trackType, this.trackSelection.getSelectedFormat(), this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), r114, r37, r33, r27, r9);
        return;
    L82:
        if (r11.getSegmentStartTimeUs(r114) < r26) goto L85;
        r51.endOfStream = r32;
        return;
    L78:
        if (r114 < r36) goto L80;
    L98:
        r51.endOfStream = r113;
        return;
    L55:
        if (r26 != C0180C.TIME_UNSET) goto L58;
        r34 = false;
        goto L60
    L49:
        if (this.periodIndex != (this.manifest.getPeriodCount() - 1)) goto L51;
        r29 = r32;
        goto L52
    L28:
        r9 = new CmcdData.Factory(this.cmcdConfiguration, this.trackSelection, Math.max(0, r19), r47.playbackSpeed, "d", this.manifest.dynamic, r47.rebufferedSince(this.lastChunkRequestRealtimeMs), r50.isEmpty());
        goto L29
    L13:
        r3 = r50.get(r50.size() - 1);
        goto L14
    L8:
        if (this.playerTrackEmsgHandler.maybeRefreshManifestBeforeLoadingNextChunk(r12) == false) goto L10;
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

    @RequiresNonNull({"#1.chunkExtractor"})
    protected Chunk newInitializationChunk(RepresentationHolder r16, DataSource r17, Format r18, int r19, Object r20, RangedUri r21, RangedUri r22, CmcdData.Factory r23) {
        Representation r3 = r16.representation;
        if (r21 == null) goto L7;
        RangedUri r4 = r21.attemptMerge(r22, r16.selectedBaseUrl.url);
        if (r4 != null) goto L8;
        r4 = r21;
    L8:
        DataSpec r6 = DashUtil.buildDataSpec(r3, r16.selectedBaseUrl.url, r4, 0, ImmutableMap.m231of());
        if (r23 == null) goto L11;
        DataSpec r10 = r23.setObjectType(CmcdData.Factory.OBJECT_TYPE_INIT_SEGMENT).createCmcdData().addToDataSpec(r6);
    L13:
        return new InitializationChunk(r17, r10, r18, r19, r20, r16.chunkExtractor);
    L11:
        r10 = r6;
        goto L13
    L7:
        r4 = (RangedUri) Assertions.checkNotNull(r22);
        goto L8
    }

    protected Chunk newMediaChunk(RepresentationHolder r31, DataSource r32, int r33, Format r34, int r35, Object r36, long r37, int r39, long r40, long r42, CmcdData.Factory r44) {
        Representation r5 = r31.representation;
        long r8 = r31.getSegmentStartTimeUs(r37);
        RangedUri r6 = r31.getSegmentUrl(r37);
        if (r31.chunkExtractor != null) goto L16;
        int r7 = 0;
        long r10 = r31.getSegmentEndTimeUs(r37);
        if (r31.isSegmentAvailableAtFullNetworkSpeed(r37, r42) == true) goto L8;
        r7 = 8;
    L8:
        DataSpec r14 = DashUtil.buildDataSpec(r5, r31.selectedBaseUrl.url, r6, r7, ImmutableMap.m231of());
        if (r44 == null) goto L15;
        r44.setChunkDurationUs(r10 - r8).setObjectType(CmcdData.Factory.getObjectType(this.trackSelection));
        Pair<String, String> r2 = getNextObjectAndRangeRequest(r37, r6, r31);
        if (r2 == null) goto L13;
        r44.setNextObjectRequest((String) r2.first).setNextRangeRequest((String) r2.second);
    L13:
        r14 = r44.createCmcdData().addToDataSpec(r14);
    L15:
        return new SingleSampleMediaChunk(r32, r14, r34, r35, r36, r8, r10, r37, r33, r34);
    L16:
        int r9 = 0;
        int r3 = 1;
        int r4 = 1;
        RangedUri r11 = r6;
    L18:
        if (r4 >= r39) goto L23;
        RangedUri r12 = r11.attemptMerge(r31.getSegmentUrl(((long) r4) + r37), r31.selectedBaseUrl.url);
        if (r12 == null) goto L23;
        r11 = r12;
        r3 = r3 + 1;
        r4 = r4 + 1;
    L23:
        long r13 = (((long) r3) + r37) - 1;
        long r15 = r31.getSegmentEndTimeUs(r13);
        long r22 = RepresentationHolder.access$000(r31);
        long r17 = C0180C.TIME_UNSET;
        if (r22 != C0180C.TIME_UNSET) goto L26;
    L29:
        int r16 = r3;
        if (r31.isSegmentAvailableAtFullNetworkSpeed(r13, r42) == true) goto L33;
        r9 = 8;
    L33:
        DataSpec r18 = DashUtil.buildDataSpec(r5, r31.selectedBaseUrl.url, r11, r9, ImmutableMap.m231of());
        if (r44 == null) goto L40;
        int r19 = r16;
        r44.setChunkDurationUs(r15 - r8).setObjectType(CmcdData.Factory.getObjectType(this.trackSelection));
        Pair<String, String> r20 = getNextObjectAndRangeRequest(r37, r11, r31);
        if (r20 == null) goto L39;
        r44.setNextObjectRequest((String) r20.first).setNextRangeRequest((String) r20.second);
    L39:
        DataSpec r21 = r44.createCmcdData().addToDataSpec(r18);
    L41:
        long r23 = -r5.presentationTimeOffsetUs;
        if (MimeTypes.isImage(r34.sampleMimeType) == false) goto L45;
        r23 = r23 + r8;
    L45:
        return new ContainerMediaChunk(r32, r21, r34, r35, r36, r8, r15, r40, r17, r37, r19, r23, r31.chunkExtractor);
    L40:
        r11 = r11;
        r19 = r16;
        r21 = r18;
        goto L41
    L26:
        if (r22 > r15) goto L29;
        r17 = r22;
        goto L29
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkSource
    public void onChunkLoadCompleted(Chunk r9) {
        if ((r9 instanceof InitializationChunk) == false) goto L10;
        int r1 = this.trackSelection.indexOf(((InitializationChunk) r9).trackFormat);
        RepresentationHolder r2 = this.representationHolders[r1];
        if (r2.segmentIndex != null) goto L10;
        ChunkIndex r3 = ((ChunkExtractor) Assertions.checkStateNotNull(r2.chunkExtractor)).getChunkIndex();
        if (r3 == null) goto L10;
        this.representationHolders[r1] = r2.copyWithNewSegmentIndex(new DashWrappingSegmentIndex(r3, r2.representation.presentationTimeOffsetUs));
    L10:
        if (this.playerTrackEmsgHandler == null) goto L13;
        this.playerTrackEmsgHandler.onChunkLoadCompleted(r9);
        return;
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkSource
    public boolean onChunkLoadError(Chunk r11, boolean r12, LoadErrorHandlingPolicy.LoadErrorInfo r13, LoadErrorHandlingPolicy r14) {
        if (r12 == true) goto L6;
        return false;
    L6:
        if (this.playerTrackEmsgHandler == null) goto L11;
        if (this.playerTrackEmsgHandler.onChunkLoadError(r11) == false) goto L11;
        return true;
    L11:
        if (this.manifest.dynamic == false) goto L13;
    L26:
        int r1 = this.trackSelection.indexOf(r11.trackFormat);
        RepresentationHolder r3 = this.representationHolders[r1];
        BaseUrl r4 = this.baseUrlExclusionList.selectBaseUrl(r3.representation.baseUrls);
        if (r4 != null) goto L29;
    L31:
        LoadErrorHandlingPolicy.FallbackOptions r5 = createFallbackOptions(this.trackSelection, r3.representation.baseUrls);
        if (r5.isFallbackAvailable(2) == false) goto L34;
    L36:
        LoadErrorHandlingPolicy.FallbackSelection r7 = r14.getFallbackSelectionFor(r5, r13);
        if (r7 != null) goto L39;
    L48:
        return false;
    L39:
        if (r5.isFallbackAvailable(r7.type) == false) goto L48;
        if (r7.type != 2) goto L45;
        return this.trackSelection.excludeTrack(this.trackSelection.indexOf(r11.trackFormat), r7.exclusionDurationMs);
    L45:
        if (r7.type != 1) goto L50;
        this.baseUrlExclusionList.exclude(r3.selectedBaseUrl, r7.exclusionDurationMs);
        return true;
    L50:
        return false;
    L34:
        if (r5.isFallbackAvailable(1) == true) goto L36;
        return false;
    L29:
        if (r3.selectedBaseUrl.equals(r4) == true) goto L31;
        return true;
    L13:
        if ((r11 instanceof MediaChunk) == false) goto L26;
        if ((r13.exception instanceof HttpDataSource.InvalidResponseCodeException) == false) goto L26;
        if (((HttpDataSource.InvalidResponseCodeException) r13.exception).responseCode != 404) goto L26;
        RepresentationHolder r2 = this.representationHolders[this.trackSelection.indexOf(r11.trackFormat)];
        long r6 = r2.getSegmentCount();
        if (r6 == (-1)) goto L26;
        if (r6 == 0) goto L26;
        long r8 = (r2.getFirstSegmentNum() + r6) - 1;
        if (((MediaChunk) r11).getNextChunkIndex() <= r8) goto L26;
        this.missingLastSegment = true;
        return true;
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkSource
    public void release() {
        RepresentationHolder[] r0 = this.representationHolders;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L8;
        ChunkExtractor r4 = r0[r2].chunkExtractor;
        if (r4 == null) goto L7;
        r4.release();
    L7:
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

    @Override // androidx.media3.exoplayer.dash.DashChunkSource
    public void updateManifest(DashManifest r8, int r9) {
        this.manifest = r8;     // Catch: BehindLiveWindowException -> L8
        this.periodIndex = r9;     // Catch: BehindLiveWindowException -> L8
        long r0 = this.manifest.getPeriodDurationUs(this.periodIndex);     // Catch: BehindLiveWindowException -> L8
        ArrayList<Representation> r2 = getRepresentations();     // Catch: BehindLiveWindowException -> L8
        int r3 = 0;
    L4:
        if (r3 >= this.representationHolders.length) goto L14;
        Representation r4 = r2.get(this.trackSelection.getIndexInTrackGroup(r3));     // Catch: BehindLiveWindowException -> L8
        this.representationHolders[r3] = this.representationHolders[r3].copyWithNewRepresentation(r0, r4);     // Catch: BehindLiveWindowException -> L8
        r3 = r3 + 1;
        goto L4
    L14:
        return;
    L8:
        e = move-exception;
        this.fatalError = e;
    }

    @Override // androidx.media3.exoplayer.dash.DashChunkSource
    public void updateTrackSelection(ExoTrackSelection r1) {
        this.trackSelection = r1;
    }
}
