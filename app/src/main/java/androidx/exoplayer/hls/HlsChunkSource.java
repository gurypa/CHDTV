package androidx.media3.exoplayer.hls;

import android.net.Uri;
import android.os.SystemClock;
import android.util.Pair;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UriUtil;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.hls.playlist.HlsMediaPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker;
import androidx.media3.exoplayer.source.BehindLiveWindowException;
import androidx.media3.exoplayer.source.chunk.BaseMediaChunkIterator;
import androidx.media3.exoplayer.source.chunk.Chunk;
import androidx.media3.exoplayer.source.chunk.DataChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunkIterator;
import androidx.media3.exoplayer.trackselection.BaseTrackSelection;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.CmcdData;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class HlsChunkSource {
    public static final int CHUNK_PUBLICATION_STATE_PRELOAD = 0;
    public static final int CHUNK_PUBLICATION_STATE_PUBLISHED = 1;
    public static final int CHUNK_PUBLICATION_STATE_REMOVED = 2;
    private static final int KEY_CACHE_SIZE = 4;
    private final CmcdConfiguration cmcdConfiguration;
    private final DataSource encryptionDataSource;
    private Uri expectedPlaylistUrl;
    private final HlsExtractorFactory extractorFactory;
    private IOException fatalError;
    private boolean independentSegments;
    private boolean isPrimaryTimestampSource;
    private final FullSegmentEncryptionKeyCache keyCache;
    private long lastChunkRequestRealtimeMs;
    private long liveEdgeInPeriodTimeUs;
    private final DataSource mediaDataSource;
    private final List<Format> muxedCaptionFormats;
    private final PlayerId playerId;
    private final Format[] playlistFormats;
    private final HlsPlaylistTracker playlistTracker;
    private final Uri[] playlistUrls;
    private byte[] scratchSpace;
    private boolean seenExpectedPlaylistError;
    private final long timestampAdjusterInitializationTimeoutMs;
    private final TimestampAdjusterProvider timestampAdjusterProvider;
    private final TrackGroup trackGroup;
    private ExoTrackSelection trackSelection;

    private static final class EncryptionKeyChunk extends DataChunk {
        private byte[] result;

        public EncryptionKeyChunk(DataSource r9, DataSpec r10, Format r11, int r12, Object r13, byte[] r14) {
            super(r9, r10, 3, r11, r12, r13, r14);
        }

        @Override // androidx.media3.exoplayer.source.chunk.DataChunk
        protected void consume(byte[] r2, int r3) {
            this.result = Arrays.copyOf(r2, r3);
        }

        public byte[] getResult() {
            return this.result;
        }
    }

    public static final class HlsChunkHolder {
        public Chunk chunk;
        public boolean endOfStream;
        public Uri playlistUrl;

        public HlsChunkHolder() {
            clear();
        }

        public void clear() {
            this.chunk = null;
            this.endOfStream = false;
            this.playlistUrl = null;
        }
    }

    static final class HlsMediaPlaylistSegmentIterator extends BaseMediaChunkIterator {
        private final String playlistBaseUri;
        private final List<HlsMediaPlaylist.SegmentBase> segmentBases;
        private final long startOfPlaylistInPeriodUs;

        public HlsMediaPlaylistSegmentIterator(String r5, long r6, List<HlsMediaPlaylist.SegmentBase> r8) {
            super(0, r8.size() - 1);
            this.playlistBaseUri = r5;
            this.startOfPlaylistInPeriodUs = r6;
            this.segmentBases = r8;
        }

        @Override // androidx.media3.exoplayer.source.chunk.MediaChunkIterator
        public long getChunkEndTimeUs() {
            checkInBounds();
            HlsMediaPlaylist.SegmentBase r0 = this.segmentBases.get((int) getCurrentIndex());
            long r1 = this.startOfPlaylistInPeriodUs + r0.relativeStartTimeUs;
            return r0.durationUs + r1;
        }

        @Override // androidx.media3.exoplayer.source.chunk.MediaChunkIterator
        public long getChunkStartTimeUs() {
            checkInBounds();
            return this.startOfPlaylistInPeriodUs + this.segmentBases.get((int) getCurrentIndex()).relativeStartTimeUs;
        }

        @Override // androidx.media3.exoplayer.source.chunk.MediaChunkIterator
        public DataSpec getDataSpec() {
            checkInBounds();
            HlsMediaPlaylist.SegmentBase r0 = this.segmentBases.get((int) getCurrentIndex());
            return new DataSpec(UriUtil.resolveToUri(this.playlistBaseUri, r0.url), r0.byteRangeOffset, r0.byteRangeLength);
        }
    }

    private static final class InitializationTrackSelection extends BaseTrackSelection {
        private int selectedIndex;

        public InitializationTrackSelection(TrackGroup r2, int[] r3) {
            super(r2, r3);
            this.selectedIndex = indexOf(r2.getFormat(r3[0]));
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public int getSelectedIndex() {
            return this.selectedIndex;
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public Object getSelectionData() {
            return null;
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public int getSelectionReason() {
            return 0;
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public void updateSelectedTrack(long r5, long r7, long r9, List<? extends MediaChunk> r11, MediaChunkIterator[] r12) {
            long r0 = SystemClock.elapsedRealtime();
            if (isTrackExcluded(this.selectedIndex, r0) == true) goto L5;
            return;
        L5:
            int r2 = this.length - 1;
        L6:
            if (r2 < 0) goto L13;
            if (isTrackExcluded(r2, r0) == false) goto L9;
            r2 = r2 - 1;
            goto L6
        L9:
            this.selectedIndex = r2;
            return;
        L13:
            throw new IllegalStateException();
        }
    }

    static final class SegmentBaseHolder {
        public final boolean isPreload;
        public final long mediaSequence;
        public final int partIndex;
        public final HlsMediaPlaylist.SegmentBase segmentBase;

        public SegmentBaseHolder(HlsMediaPlaylist.SegmentBase r2, long r3, int r5) {
            this.segmentBase = r2;
            this.mediaSequence = r3;
            this.partIndex = r5;
            if ((r2 instanceof HlsMediaPlaylist.Part) == true) goto L5;
        L7:
            boolean r0 = false;
        L8:
            this.isPreload = r0;
            return;
        L5:
            if (((HlsMediaPlaylist.Part) r2).isPreload == false) goto L7;
            r0 = true;
            goto L8
        }
    }

    public HlsChunkSource(HlsExtractorFactory r17, HlsPlaylistTracker r18, Uri[] r19, Format[] r20, HlsDataSourceFactory r21, TransferListener r22, TimestampAdjusterProvider r23, long r24, List<Format> r26, PlayerId r27, CmcdConfiguration r28) {
        this.extractorFactory = r17;
        this.playlistTracker = r18;
        this.playlistUrls = r19;
        this.playlistFormats = r20;
        this.timestampAdjusterProvider = r23;
        this.timestampAdjusterInitializationTimeoutMs = r24;
        this.muxedCaptionFormats = r26;
        this.playerId = r27;
        this.cmcdConfiguration = r28;
        this.lastChunkRequestRealtimeMs = C0180C.TIME_UNSET;
        this.keyCache = new FullSegmentEncryptionKeyCache(4);
        this.scratchSpace = Util.EMPTY_BYTE_ARRAY;
        this.liveEdgeInPeriodTimeUs = C0180C.TIME_UNSET;
        this.mediaDataSource = r21.createDataSource(1);
        if (r22 == null) goto L5;
        this.mediaDataSource.addTransferListener(r22);
    L5:
        this.encryptionDataSource = r21.createDataSource(3);
        this.trackGroup = new TrackGroup(r20);
        ArrayList r13 = new ArrayList();
        int r14 = 0;
    L7:
        if (r14 >= r19.length) goto L12;
        if ((r20[r14].roleFlags & 16384) != 0) goto L11;
        r13.add(Integer.valueOf(r14));
    L11:
        r14 = r14 + 1;
        goto L7
    L12:
        this.trackSelection = new InitializationTrackSelection(this.trackGroup, Ints.toArray(r13));
    }

    private void deactivatePlaylistForSelectedTrack() {
        int r0 = this.trackSelection.getSelectedIndexInTrackGroup();
        this.playlistTracker.deactivatePlaylistForPlayback(this.playlistUrls[r0]);
    }

    private static Uri getFullEncryptionKeyUri(HlsMediaPlaylist r2, HlsMediaPlaylist.SegmentBase r3) {
        if (r3 != null) goto L4;
        return null;
    L4:
        if (r3.fullSegmentEncryptionKeyUri != null) goto L7;
        return null;
    L7:
        return UriUtil.resolveToUri(r2.baseUri, r3.fullSegmentEncryptionKeyUri);
    }

    private boolean getIsMuxedAudioAndVideo() {
        Format r0 = this.trackGroup.getFormat(this.trackSelection.getSelectedIndex());
        String r1 = MimeTypes.getAudioMediaMimeType(r0.codecs);
        String r2 = MimeTypes.getVideoMediaMimeType(r0.codecs);
        if (r1 == null) goto L6;
        if (r2 == null) goto L6;
        return true;
    L6:
        return false;
    }

    private Pair<Long, Integer> getNextMediaSequenceAndPartIndex(HlsMediaChunk r20, boolean r21, HlsMediaPlaylist r22, long r23, long r25) {
        int r4 = -1;
        if (r20 == null) goto L19;
        if (r21 == true) goto L19;
        if (r20.isLoadCompleted() == false) goto L18;
        if (r20.partIndex != (-1)) goto L11;
        long r6 = r20.getNextChunkIndex();
    L12:
        Long r7 = Long.valueOf(r6);
        if (r20.partIndex == (-1)) goto L64;
        r4 = r20.partIndex + 1;
    L64:
        return new Pair(r7, Integer.valueOf(r4));
    L11:
        r6 = r20.chunkIndex;
        goto L12
    L18:
        return new Pair(Long.valueOf(r20.chunkIndex), Integer.valueOf(r20.partIndex));
    L19:
        long r5 = r23 + r22.durationUs;
        if (r20 != null) goto L22;
    L25:
        long r8 = r25;
    L27:
        if (r22.hasEndTag == false) goto L29;
    L32:
        long r9 = r8 - r23;
        List<HlsMediaPlaylist.Segment> r10 = r22.segments;
        Long r11 = Long.valueOf(r9);
        if (this.playlistTracker.isLive() == false) goto L37;
        if (r20 == null) goto L37;
        boolean r12 = false;
    L38:
        int r3 = Util.binarySearchFloor(r10, r11, true, r12);
        long r13 = ((long) r3) + r22.mediaSequence;
        int r14 = -1;
        if (r3 < 0) goto L58;
        HlsMediaPlaylist.Segment r15 = r22.segments.get(r3);
        if (r9 >= (r15.relativeStartTimeUs + r15.durationUs)) goto L43;
        List<HlsMediaPlaylist.Part> r0 = r15.parts;
    L44:
        int r1 = 0;
    L46:
        if (r1 >= r0.size()) goto L57;
        HlsMediaPlaylist.Part r16 = r0.get(r1);
        int r17 = r3;
        int r18 = r14;
        if (r9 < (r16.relativeStartTimeUs + r16.durationUs)) goto L50;
        r1 = r1 + 1;
        r3 = r17;
        r14 = r18;
        goto L46
    L50:
        if (r16.isIndependent == false) goto L59;
        int r19 = r1;
        if (r0 != r22.trailingParts) goto L54;
        long r110 = 1;
    L55:
        r13 = r13 + r110;
    L61:
        return new Pair(Long.valueOf(r13), Integer.valueOf(r19));
    L54:
        r110 = 0;
    L59:
        r19 = r18;
        goto L61
    L57:
        r18 = r14;
        goto L59
    L43:
        r0 = r22.trailingParts;
        goto L44
    L58:
        r18 = -1;
    L37:
        r12 = true;
        goto L38
    L29:
        if (r8 < r5) goto L32;
        return new Pair(Long.valueOf(r22.mediaSequence + ((long) r22.segments.size())), -1);
    L22:
        if (this.independentSegments == true) goto L25;
        r8 = r20.startTimeUs;
        goto L27
    }

    private static SegmentBaseHolder getNextSegmentHolder(HlsMediaPlaylist r9, long r10, int r12) {
        int r0 = (int) (r10 - r9.mediaSequence);
        int r3 = 0;
        if (r0 != r9.segments.size()) goto L11;
        if (r12 == (-1)) goto L7;
        r3 = r12;
    L7:
        if (r3 < r9.trailingParts.size()) goto L10;
        return null;
    L10:
        return new SegmentBaseHolder(r9.trailingParts.get(r3), r10, r3);
    L11:
        HlsMediaPlaylist.Segment r1 = r9.segments.get(r0);
        if (r12 != (-1)) goto L16;
        return new SegmentBaseHolder(r1, r10, -1);
    L16:
        if (r12 >= r1.parts.size()) goto L20;
        return new SegmentBaseHolder(r1.parts.get(r12), r10, r12);
    L20:
        if ((r0 + 1) >= r9.segments.size()) goto L24;
        return new SegmentBaseHolder(r9.segments.get(r0 + 1), 1 + r10, -1);
    L24:
        if (r9.trailingParts.isEmpty() == false) goto L26;
        return null;
    L26:
        return new SegmentBaseHolder(r9.trailingParts.get(0), 1 + r10, 0);
    }

    static List<HlsMediaPlaylist.SegmentBase> getSegmentBaseList(HlsMediaPlaylist r8, long r9, int r11) {
        int r0 = (int) (r9 - r8.mediaSequence);
        if (r0 < 0) goto L29;
        if (r8.segments.size() < r0) goto L29;
        ArrayList r1 = new ArrayList();
        if (r0 >= r8.segments.size()) goto L19;
        if (r11 == (-1)) goto L17;
        HlsMediaPlaylist.Segment r2 = r8.segments.get(r0);
        if (r11 != 0) goto L14;
        r1.add(r2);
    L16:
        r0 = r0 + 1;
        goto L17
    L14:
        if (r11 >= r2.parts.size()) goto L16;
        r1.addAll(r2.parts.subList(r11, r2.parts.size()));
    L17:
        r11 = 0;
        r1.addAll(r8.segments.subList(r0, r8.segments.size()));
    L19:
        if (r8.partTargetDurationUs == C0180C.TIME_UNSET) goto L27;
        if (r11 != (-1)) goto L22;
        int r3 = 0;
    L23:
        int r12 = r3;
        if (r12 >= r8.trailingParts.size()) goto L27;
        r1.addAll(r8.trailingParts.subList(r12, r8.trailingParts.size()));
        goto L27
    L22:
        r3 = r11;
    L27:
        return Collections.unmodifiableList(r1);
    L29:
        return ImmutableList.m212of();
    }

    private Chunk maybeCreateEncryptionChunkFor(Uri r11, int r12, boolean r13, CmcdData.Factory r14) {
        if (r11 != null) goto L5;
        return null;
    L5:
        byte[] r1 = this.keyCache.remove(r11);
        if (r1 == null) goto L9;
        this.keyCache.put(r11, r1);
        return null;
    L9:
        DataSpec r0 = new DataSpec.Builder().setUri(r11).setFlags(1).build();
        if (r14 == null) goto L14;
        if (r13 == false) goto L13;
        r14.setObjectType(CmcdData.Factory.OBJECT_TYPE_INIT_SEGMENT);
    L13:
        DataSpec r5 = r14.createCmcdData().addToDataSpec(r0);
    L16:
        return new EncryptionKeyChunk(this.encryptionDataSource, r5, this.playlistFormats[r12], this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), this.scratchSpace);
    L14:
        r5 = r0;
        goto L16
    }

    private long resolveTimeToLiveEdgeUs(long r5) {
        if (this.liveEdgeInPeriodTimeUs == C0180C.TIME_UNSET) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == true) goto L9;
        return C0180C.TIME_UNSET;
    L9:
        return this.liveEdgeInPeriodTimeUs - r5;
    L5:
        r0 = false;
        goto L6
    }

    private void updateLiveEdgeTimeUs(HlsMediaPlaylist r5) {
        if (r5.hasEndTag == false) goto L5;
        long r0 = C0180C.TIME_UNSET;
    L6:
        this.liveEdgeInPeriodTimeUs = r0;
        return;
    L5:
        r0 = r5.getEndTimeUs() - this.playlistTracker.getInitialStartTimeUs();
        goto L6
    }

    public MediaChunkIterator[] createMediaChunkIterators(HlsMediaChunk r17, long r18) {
        HlsChunkSource r0 = this;
        HlsMediaChunk r1 = r17;
        if (r1 != null) goto L5;
        int r2 = -1;
    L6:
        int r8 = r2;
        MediaChunkIterator[] r9 = new MediaChunkIterator[r0.trackSelection.length()];
        int r10 = 0;
    L8:
        if (r10 >= r9.length) goto L17;
        int r11 = r0.trackSelection.getIndexInTrackGroup(r10);
        Uri r12 = r0.playlistUrls[r11];
        if (r0.playlistTracker.isSnapshotValid(r12) == true) goto L12;
        r9[r10] = MediaChunkIterator.EMPTY;
    L16:
        r10 = r10 + 1;
        r0 = this;
        r1 = r17;
        goto L8
    L12:
        boolean r3 = false;
        HlsMediaPlaylist r4 = r0.playlistTracker.getPlaylistSnapshot(r12, false);
        Assertions.checkNotNull(r4);
        long r5 = r4.startTimeUs - r0.playlistTracker.getInitialStartTimeUs();
        if (r11 == r8) goto L15;
        r3 = true;
    L15:
        Pair<Long, Integer> r13 = r0.getNextMediaSequenceAndPartIndex(r1, r3, r4, r5, r18);
        r9[r10] = new HlsMediaPlaylistSegmentIterator(r4.baseUri, r5, getSegmentBaseList(r4, ((Long) r13.first).longValue(), ((Integer) r13.second).intValue()));
        goto L16
    L17:
        return r9;
    L5:
        r2 = r0.trackGroup.indexOf(r1.trackFormat);
        goto L6
    }

    public long getAdjustedSeekPositionUs(long r17, SeekParameters r19) {
        int r1 = this.trackSelection.getSelectedIndex();
        if (r1 < this.playlistUrls.length) goto L5;
    L7:
        HlsMediaPlaylist r2 = null;
    L9:
        if (r2 != null) goto L11;
    L20:
        return r17;
    L11:
        if (r2.segments.isEmpty() == true) goto L20;
        if (r2.hasIndependentSegments == false) goto L20;
        long r4 = r2.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
        long r7 = r17 - r4;
        int r13 = Util.binarySearchFloor(r2.segments, Long.valueOf(r7), true, true);
        long r9 = r2.segments.get(r13).relativeStartTimeUs;
        long r11 = r9;
        if (r13 == (r2.segments.size() - 1)) goto L19;
        r11 = r2.segments.get(r13 + 1).relativeStartTimeUs;
    L19:
        return r19.resolveSeekPositionUs(r7, r9, r11) + r4;
    L5:
        if (r1 == (-1)) goto L7;
        r2 = this.playlistTracker.getPlaylistSnapshot(this.playlistUrls[this.trackSelection.getSelectedIndexInTrackGroup()], true);
        goto L9
    }

    public int getChunkPublicationState(HlsMediaChunk r10) {
        if (r10.partIndex != (-1)) goto L5;
        return 1;
    L5:
        Uri r0 = this.playlistUrls[this.trackGroup.indexOf(r10.trackFormat)];
        HlsMediaPlaylist r1 = (HlsMediaPlaylist) Assertions.checkNotNull(this.playlistTracker.getPlaylistSnapshot(r0, false));
        int r4 = (int) (r10.chunkIndex - r1.mediaSequence);
        if (r4 >= 0) goto L9;
        return 1;
    L9:
        if (r4 >= r1.segments.size()) goto L11;
        List<HlsMediaPlaylist.Part> r5 = r1.segments.get(r4).parts;
    L13:
        if (r10.partIndex < r5.size()) goto L15;
        return 2;
    L15:
        HlsMediaPlaylist.Part r6 = r5.get(r10.partIndex);
        if (r6.isPreload == false) goto L19;
        return 0;
    L19:
        if (Util.areEqual(Uri.parse(UriUtil.resolve(r1.baseUri, r6.url)), r10.dataSpec.uri) == false) goto L22;
        return 1;
    L22:
        return 2;
    L11:
        r5 = r1.trailingParts;
        goto L13
    }

    public void getNextChunk(LoadingInfo r52, long r53, List<HlsMediaChunk> r55, boolean r56, HlsChunkHolder r57) {
        if (r55.isEmpty() == false) goto L5;
        HlsMediaChunk r1 = null;
    L6:
        if (r1 != null) goto L8;
        int r2 = -1;
    L9:
        int r12 = r2;
        long r14 = r52.playbackPositionUs;
        long r3 = r53 - r14;
        long r4 = resolveTimeToLiveEdgeUs(r14);
        if (r1 != null) goto L12;
    L17:
        long r16 = r3;
        long r18 = r4;
    L18:
        this.trackSelection.updateSelectedTrack(r14, r16, r18, r55, createMediaChunkIterators(r1, r53));
        long r13 = r16;
        int r15 = this.trackSelection.getSelectedIndexInTrackGroup();
        if (r12 == r15) goto L21;
        boolean r5 = true;
    L22:
        Uri r6 = this.playlistUrls[r15];
        if (this.playlistTracker.isSnapshotValid(r6) == true) goto L26;
        r57.playlistUrl = r6;
        this.seenExpectedPlaylistError &= r6.equals(this.expectedPlaylistUrl);
        this.expectedPlaylistUrl = r6;
        return;
    L26:
        HlsMediaPlaylist r7 = this.playlistTracker.getPlaylistSnapshot(r6, true);
        Assertions.checkNotNull(r7);
        this.independentSegments = r7.hasIndependentSegments;
        updateLiveEdgeTimeUs(r7);
        long r10 = r7.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
        boolean r8 = r5;
        Pair<Long, Integer> r11 = getNextMediaSequenceAndPartIndex(r1, r8, r7, r10, r53);
        long r20 = ((Long) r11.first).longValue();
        int r24 = ((Integer) r11.second).intValue();
        if (r20 >= r7.mediaSequence) goto L31;
        if (r1 == null) goto L31;
        if (r8 == false) goto L31;
        Uri r17 = this.playlistUrls[r12];
        HlsMediaPlaylist r9 = this.playlistTracker.getPlaylistSnapshot(r17, true);
        Assertions.checkNotNull(r9);
        long r19 = r9.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
        Pair<Long, Integer> r21 = getNextMediaSequenceAndPartIndex(r1, false, r9, r19, r53);
        long r22 = ((Long) r21.first).longValue();
        long r23 = r19;
        long r25 = r22;
        HlsMediaPlaylist r26 = r9;
        Uri r27 = r17;
        int r110 = r12;
        int r111 = ((Integer) r21.second).intValue();
    L32:
        if (r110 == r12) goto L38;
        if (r12 == (-1)) goto L38;
        this.playlistTracker.deactivatePlaylistForPlayback(this.playlistUrls[r12]);
    L38:
        if (r25 >= r26.mediaSequence) goto L41;
        this.fatalError = new BehindLiveWindowException();
        return;
    L41:
        SegmentBaseHolder r28 = getNextSegmentHolder(r26, r25, r111);
        if (r28 != null) goto L55;
        long r29 = 1;
        if (r26.hasEndTag == true) goto L47;
        r57.playlistUrl = r27;
        this.seenExpectedPlaylistError &= r27.equals(this.expectedPlaylistUrl);
        this.expectedPlaylistUrl = r27;
        return;
    L47:
        if (r56 == true) goto L53;
        if (r26.segments.isEmpty() == true) goto L53;
        r28 = new SegmentBaseHolder((HlsMediaPlaylist.SegmentBase) Iterables.getLast(r26.segments), (r26.mediaSequence + ((long) r26.segments.size())) - 1, -1);
    L56:
        this.seenExpectedPlaylistError = false;
        this.expectedPlaylistUrl = null;
        CmcdData.Factory r30 = null;
        if (this.cmcdConfiguration == null) goto L79;
        CmcdData.Factory r38 = new CmcdData.Factory(this.cmcdConfiguration, this.trackSelection, Math.max(0, r13), r52.playbackSpeed, CmcdData.Factory.STREAMING_FORMAT_HLS, !r26.hasEndTag, r52.rebufferedSince(this.lastChunkRequestRealtimeMs), r55.isEmpty());
        if (getIsMuxedAudioAndVideo() == false) goto L61;
        String r31 = CmcdData.Factory.OBJECT_TYPE_MUXED_AUDIO_AND_VIDEO;
    L62:
        r30 = r38.setObjectType(r31);
        if (r28.partIndex != (-1)) goto L65;
        long r32 = r28.mediaSequence + r29;
    L67:
        if (r28.partIndex != (-1)) goto L69;
        int r33 = -1;
    L70:
        SegmentBaseHolder r34 = getNextSegmentHolder(r26, r32, r33);
        if (r34 == null) goto L79;
        r30.setNextObjectRequest(UriUtil.getRelativePath(UriUtil.resolveToUri(r26.baseUri, r28.segmentBase.url), UriUtil.resolveToUri(r26.baseUri, r34.segmentBase.url)));
        String r35 = r34.segmentBase.byteRangeOffset + "-";
        if (r34.segmentBase.byteRangeLength == (-1)) goto L76;
        r35 = r35 + (r34.segmentBase.byteRangeOffset + r34.segmentBase.byteRangeLength);
    L76:
        r30.setNextRangeRequest(r35);
        goto L79
    L69:
        r33 = r28.partIndex + 1;
        goto L70
    L65:
        r32 = r28.mediaSequence;
        goto L67
    L61:
        r31 = CmcdData.Factory.getObjectType(this.trackSelection);
    L79:
        this.lastChunkRequestRealtimeMs = SystemClock.elapsedRealtime();
        Uri r112 = getFullEncryptionKeyUri(r26, r28.segmentBase.initializationSegment);
        r57.chunk = maybeCreateEncryptionChunkFor(r112, r110, true, r30);
        if (r57.chunk == null) goto L82;
        return;
    L82:
        Uri r113 = getFullEncryptionKeyUri(r26, r28.segmentBase);
        r57.chunk = maybeCreateEncryptionChunkFor(r113, r110, false, r30);
        if (r57.chunk == null) goto L85;
        return;
    L85:
        Uri r36 = r27;
        boolean r210 = HlsMediaChunk.shouldSpliceIn(r1, r36, r26, r28, r23);
        HlsMediaPlaylist r114 = r26;
        if (r210 == true) goto L88;
    L90:
        SegmentBaseHolder r115 = r28;
        long r116 = r23;
        r57.chunk = HlsMediaChunk.createInstance(this.extractorFactory, this.mediaDataSource, this.playlistFormats[r110], r116, r114, r115, r36, this.muxedCaptionFormats, this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), this.isPrimaryTimestampSource, this.timestampAdjusterProvider, this.timestampAdjusterInitializationTimeoutMs, r1, this.keyCache.get(r113), this.keyCache.get(r112), r210, this.playerId, r30);
        return;
    L88:
        if (r28.isPreload == false) goto L90;
        return;
    L53:
        r57.endOfStream = true;
        return;
    L55:
        r29 = 1;
    L31:
        r23 = r10;
        r25 = r20;
        r26 = r7;
        r27 = r6;
        r110 = r15;
        r111 = r24;
        goto L32
    L21:
        r5 = false;
        goto L22
    L12:
        if (this.independentSegments == true) goto L17;
        long r117 = r1.getDurationUs();
        long r37 = Math.max(0, r3 - r117);
        if (r4 == C0180C.TIME_UNSET) goto L16;
        long r39 = Math.max(0, r4 - r117);
        r16 = r37;
        r18 = r39;
        goto L18
    L16:
        r16 = r37;
        r18 = r4;
        goto L18
    L8:
        r2 = this.trackGroup.indexOf(r1.trackFormat);
        goto L9
    L5:
        r1 = (HlsMediaChunk) Iterables.getLast(r55);
        goto L6
    }

    public int getPreferredQueueSize(long r3, List<? extends MediaChunk> r5) {
        if (this.fatalError != null) goto L10;
        if (this.trackSelection.length() < 2) goto L10;
        return this.trackSelection.evaluateQueueSize(r3, r5);
    L10:
        return r5.size();
    }

    public TrackGroup getTrackGroup() {
        return this.trackGroup;
    }

    public ExoTrackSelection getTrackSelection() {
        return this.trackSelection;
    }

    public boolean hasIndependentSegments() {
        return this.independentSegments;
    }

    public boolean maybeExcludeTrack(Chunk r5, long r6) {
        return this.trackSelection.excludeTrack(this.trackSelection.indexOf(this.trackGroup.indexOf(r5.trackFormat)), r6);
    }

    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) goto L11;
        if (this.expectedPlaylistUrl != null) goto L7;
        return;
    L7:
        if (this.seenExpectedPlaylistError == false) goto L13;
        this.playlistTracker.maybeThrowPlaylistRefreshError(this.expectedPlaylistUrl);
        return;
    L13:
        return;
    L11:
        throw this.fatalError;
    }

    public boolean obtainsChunksForPlaylist(Uri r2) {
        return Util.contains(this.playlistUrls, r2);
    }

    public void onChunkLoadCompleted(Chunk r5) {
        if ((r5 instanceof EncryptionKeyChunk) == false) goto L6;
        EncryptionKeyChunk r0 = (EncryptionKeyChunk) r5;
        this.scratchSpace = r0.getDataHolder();
        this.keyCache.put(r0.dataSpec.uri, (byte[]) Assertions.checkNotNull(r0.getResult()));
        return;
    }

    public boolean onPlaylistError(Uri r7, long r8) {
        int r0 = -1;
        int r1 = 0;
    L4:
        if (r1 >= this.playlistUrls.length) goto L10;
        if (this.playlistUrls[r1].equals(r7) == true) goto L7;
        r1 = r1 + 1;
        goto L4
    L7:
        r0 = r1;
    L10:
        if (r0 != (-1)) goto L12;
        return true;
    L12:
        int r3 = this.trackSelection.indexOf(r0);
        if (r3 != (-1)) goto L15;
        return true;
    L15:
        this.seenExpectedPlaylistError |= r7.equals(this.expectedPlaylistUrl);
        if (r8 != C0180C.TIME_UNSET) goto L18;
        return true;
    L18:
        if (this.trackSelection.excludeTrack(r3, r8) == true) goto L20;
    L22:
        return false;
    L20:
        if (this.playlistTracker.excludeMediaPlaylist(r7, r8) == false) goto L22;
        return true;
    }

    public void reset() {
        deactivatePlaylistForSelectedTrack();
        this.fatalError = null;
    }

    public void setIsPrimaryTimestampSource(boolean r1) {
        this.isPrimaryTimestampSource = r1;
    }

    public void setTrackSelection(ExoTrackSelection r1) {
        deactivatePlaylistForSelectedTrack();
        this.trackSelection = r1;
    }

    public boolean shouldCancelLoad(long r2, Chunk r4, List<? extends MediaChunk> r5) {
        if (this.fatalError == null) goto L7;
        return false;
    L7:
        return this.trackSelection.shouldCancelChunkLoad(r2, r4, r5);
    }
}
