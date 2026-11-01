package androidx.media3.exoplayer.hls;

import android.net.Uri;
import androidx.media3.common.C0180C;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.common.util.UriUtil;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSourceUtil;
import androidx.media3.datasource.DataSpec;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.hls.playlist.HlsMediaPlaylist;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import androidx.media3.exoplayer.upstream.CmcdData;
import androidx.media3.extractor.DefaultExtractorInput;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.metadata.id3.Id3Decoder;
import androidx.media3.extractor.metadata.id3.PrivFrame;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
final class HlsMediaChunk extends MediaChunk {
    public static final String PRIV_TIMESTAMP_FRAME_OWNER = "com.apple.streaming.transportStreamTimestamp";
    private static final AtomicInteger uidSource = null;
    public final int discontinuitySequenceNumber;
    private final DrmInitData drmInitData;
    private HlsMediaChunkExtractor extractor;
    private final HlsExtractorFactory extractorFactory;
    private boolean extractorInvalidated;
    private final boolean hasGapTag;
    private final Id3Decoder id3Decoder;
    private boolean initDataLoadRequired;
    private final DataSource initDataSource;
    private final DataSpec initDataSpec;
    private final boolean initSegmentEncrypted;
    private final boolean isPrimaryTimestampSource;
    private boolean isPublished;
    private volatile boolean loadCanceled;
    private boolean loadCompleted;
    private final boolean mediaSegmentEncrypted;
    private final List<Format> muxedCaptionFormats;
    private int nextLoadPosition;
    private HlsSampleStreamWrapper output;
    public final int partIndex;
    private final PlayerId playerId;
    public final Uri playlistUrl;
    private final HlsMediaChunkExtractor previousExtractor;
    private ImmutableList<Integer> sampleQueueFirstSampleIndices;
    private final ParsableByteArray scratchId3Data;
    public final boolean shouldSpliceIn;
    private final TimestampAdjuster timestampAdjuster;
    private final long timestampAdjusterInitializationTimeoutMs;
    public final int uid;

    static {
        uidSource = new AtomicInteger();
    }

    private HlsMediaChunk(HlsExtractorFactory r17, DataSource r18, DataSpec r19, Format r20, boolean r21, DataSource r22, DataSpec r23, boolean r24, Uri r25, List<Format> r26, int r27, Object r28, long r29, long r31, long r33, int r35, boolean r36, int r37, boolean r38, boolean r39, TimestampAdjuster r40, long r41, DrmInitData r43, HlsMediaChunkExtractor r44, Id3Decoder r45, ParsableByteArray r46, boolean r47, PlayerId r48) {
        super(r18, r19, r20, r27, r28, r29, r31, r33);
        this.mediaSegmentEncrypted = r21;
        this.partIndex = r35;
        this.isPublished = r36;
        this.discontinuitySequenceNumber = r37;
        this.initDataSpec = r23;
        this.initDataSource = r22;
        if (r23 == null) goto L5;
        boolean r6 = true;
    L6:
        this.initDataLoadRequired = r6;
        this.initSegmentEncrypted = r24;
        this.playlistUrl = r25;
        this.isPrimaryTimestampSource = r39;
        this.timestampAdjuster = r40;
        this.timestampAdjusterInitializationTimeoutMs = r41;
        this.hasGapTag = r38;
        this.extractorFactory = r17;
        this.muxedCaptionFormats = r26;
        this.drmInitData = r43;
        this.previousExtractor = r44;
        this.id3Decoder = r45;
        this.scratchId3Data = r46;
        this.shouldSpliceIn = r47;
        this.playerId = r48;
        this.sampleQueueFirstSampleIndices = ImmutableList.m212of();
        this.uid = uidSource.getAndIncrement();
        return;
    L5:
        r6 = false;
        goto L6
    }

    private static DataSource buildDataSource(DataSource r1, byte[] r2, byte[] r3) {
        if (r2 == null) goto L5;
        Assertions.checkNotNull(r3);
        return new Aes128DataSource(r1, r2, r3);
    L5:
        return r1;
    }

    public static HlsMediaChunk createInstance(HlsExtractorFactory r44, DataSource r45, Format r46, long r47, HlsMediaPlaylist r49, HlsChunkSource.SegmentBaseHolder r50, Uri r51, List<Format> r52, int r53, Object r54, boolean r55, TimestampAdjusterProvider r56, long r57, HlsMediaChunk r59, byte[] r60, byte[] r61, boolean r62, PlayerId r63, CmcdData.Factory r64) {
        HlsMediaPlaylist.SegmentBase r7 = r50.segmentBase;
        DataSpec.Builder r8 = new DataSpec.Builder().setUri(UriUtil.resolveToUri(r49.baseUri, r7.url)).setPosition(r7.byteRangeOffset).setLength(r7.byteRangeLength);
        if (r50.isPreload == false) goto L5;
        int r9 = 8;
    L6:
        DataSpec r10 = r8.setFlags(r9).build();
        if (r64 == null) goto L9;
        DataSpec r14 = r64.setChunkDurationUs(r7.durationUs).createCmcdData().addToDataSpec(r10);
    L10:
        if (r60 == null) goto L12;
        boolean r16 = true;
    L13:
        if (r16 == false) goto L15;
        byte[] r11 = getEncryptionIvArray((String) Assertions.checkNotNull(r7.encryptionIV));
    L16:
        DataSource r13 = buildDataSource(r45, r60, r11);
        HlsMediaPlaylist.Segment r12 = r7.initializationSegment;
        DataSpec r15 = null;
        boolean r17 = false;
        DataSource r18 = null;
        if (r12 == null) goto L30;
        if (r61 == null) goto L20;
        boolean r19 = true;
    L21:
        r17 = r19;
        if (r17 == false) goto L24;
        boolean r110 = true;
        byte[] r20 = getEncryptionIvArray((String) Assertions.checkNotNull(r12.encryptionIV));
    L25:
        DataSpec r111 = new DataSpec.Builder().setUri(UriUtil.resolveToUri(r49.baseUri, r12.url)).setPosition(r12.byteRangeOffset).setLength(r12.byteRangeLength).build();
        if (r64 == null) goto L28;
        r15 = r64.setObjectType(CmcdData.Factory.OBJECT_TYPE_INIT_SEGMENT).createCmcdData().addToDataSpec(r111);
    L29:
        r18 = buildDataSource(r45, r61, r20);
    L31:
        long r24 = r47 + r7.relativeStartTimeUs;
        long r26 = r24 + r7.durationUs;
        int r21 = r49.discontinuitySequence + r7.relativeDiscontinuitySequence;
        if (r59 != null) goto L34;
        Uri r112 = r51;
        Id3Decoder r40 = new Id3Decoder();
        HlsMediaChunkExtractor r39 = null;
        ParsableByteArray r41 = new ParsableByteArray(10);
    L62:
        boolean r113 = r17;
        return new HlsMediaChunk(r44, r13, r14, r46, r16, r18, r15, r113, r112, r52, r53, r54, r24, r26, r50.mediaSequence, r50.partIndex, !r50.isPreload, r21, r7.hasGapTag, r55, r56.getAdjuster(r21), r57, r7.drmInitData, r39, r40, r41, r62, r63);
    L34:
        if (r15 == r59.initDataSpec) goto L44;
        if (r15 != null) goto L37;
    L43:
        boolean r0 = false;
    L45:
        r112 = r51;
        if (r112.equals(r59.playlistUrl) == true) goto L48;
    L50:
        boolean r22 = false;
    L51:
        Id3Decoder r1 = r59.id3Decoder;
        ParsableByteArray r114 = r59.scratchId3Data;
        if (r0 == false) goto L59;
        if (r22 == false) goto L59;
        if (r59.extractorInvalidated == true) goto L59;
        if (r59.discontinuitySequenceNumber != r21) goto L59;
        HlsMediaChunkExtractor r115 = r59.extractor;
    L60:
        r40 = r1;
        r39 = r115;
        r41 = r114;
    L59:
        r115 = null;
        goto L60
    L48:
        if (r59.loadCompleted == false) goto L50;
        r22 = r110;
        goto L51
    L37:
        if (r59.initDataSpec == null) goto L43;
        if (r15.uri.equals(r59.initDataSpec.uri) == false) goto L43;
        if (r15.position != r59.initDataSpec.position) goto L43;
    L44:
        r0 = r110;
        goto L45
    L28:
        r15 = r111;
        goto L29
    L24:
        r110 = true;
        r20 = null;
        goto L25
    L20:
        r19 = false;
        goto L21
    L30:
        r110 = true;
        goto L31
    L15:
        r11 = null;
        goto L16
    L12:
        r16 = false;
        goto L13
    L9:
        r14 = r10;
        goto L10
    L5:
        r9 = 0;
        goto L6
    }

    @RequiresNonNull({"output"})
    private void feedDataToExtractor(DataSource r9, DataSpec r10, boolean r11, boolean r12) throws IOException {
        if (r11 == false) goto L8;
        DataSpec r0 = r10;
        if (this.nextLoadPosition == 0) goto L6;
        boolean r1 = true;
    L38:
        DefaultExtractorInput r2 = prepareExtraction(r9, r0, r12);     // Catch: Throwable -> L35
        if (r1 == false) goto L39;
        r2.skipFully(this.nextLoadPosition);     // Catch: Throwable -> L35
    L39:
    L21:
        th = move-exception;
        this.nextLoadPosition = (int) (r2.getPosition() - r10.position);     // Catch: Throwable -> L35
        throw th;     // Catch: Throwable -> L35
    L23:
        e = move-exception;
        if ((this.trackFormat.roleFlags & 16384) == 0) goto L32;
        this.extractor.onTruncatedSegmentParsed();     // Catch: Throwable -> L21
        long r3 = r2.getPosition();     // Catch: Throwable -> L35
        long r5 = r10.position;     // Catch: Throwable -> L35
    L19:
        this.nextLoadPosition = (int) (r3 - r5);     // Catch: Throwable -> L35
        DataSourceUtil.closeQuietly(r9);
        return;
    L32:
        throw e;     // Catch: Throwable -> L21
    L14:
        if (this.loadCanceled == true) goto L18;
        if (this.extractor.read(r2) == false) goto L18;
    L18:
        r3 = r2.getPosition();     // Catch: Throwable -> L35
        r5 = r10.position;     // Catch: Throwable -> L35
    L35:
        th = move-exception;
        DataSourceUtil.closeQuietly(r9);
        throw th;
    L6:
        r1 = false;
        goto L38
    L8:
        r0 = r10.subrange(this.nextLoadPosition);
        r1 = false;
        goto L38
    }

    private static byte[] getEncryptionIvArray(String r6) {
        if (Ascii.toLowerCase(r6).startsWith("0x") == false) goto L5;
        String r0 = r6.substring(2);
    L6:
        byte[] r1 = new BigInteger(r0, 16).toByteArray();
        byte[] r3 = new byte[16];
        if (r1.length <= 16) goto L9;
        int r4 = r1.length - 16;
    L10:
        System.arraycopy(r1, r4, r3, (r3.length - r1.length) + r4, r1.length - r4);
        return r3;
    L9:
        r4 = 0;
        goto L10
    L5:
        r0 = r6;
        goto L6
    }

    private static boolean isIndependent(HlsChunkSource.SegmentBaseHolder r1, HlsMediaPlaylist r2) {
        if ((r1.segmentBase instanceof HlsMediaPlaylist.Part) == false) goto L15;
        if (((HlsMediaPlaylist.Part) r1.segmentBase).isIndependent == false) goto L7;
    L12:
        return true;
    L7:
        if (r1.partIndex == 0) goto L9;
    L11:
        return false;
    L9:
        if (r2.hasIndependentSegments == false) goto L11;
    L15:
        return r2.hasIndependentSegments;
    }

    @RequiresNonNull({"output"})
    private void loadMedia() throws IOException {
        feedDataToExtractor(this.dataSource, this.dataSpec, this.mediaSegmentEncrypted, true);
    }

    @RequiresNonNull({"output"})
    private void maybeLoadInitData() throws IOException {
        if (this.initDataLoadRequired == true) goto L5;
        return;
    L5:
        Assertions.checkNotNull(this.initDataSource);
        Assertions.checkNotNull(this.initDataSpec);
        feedDataToExtractor(this.initDataSource, this.initDataSpec, this.initSegmentEncrypted, false);
        this.nextLoadPosition = 0;
        this.initDataLoadRequired = false;
    }

    private long peekId3PrivTimestamp(ExtractorInput r14) throws IOException {
        r14.resetPeekPosition();
        this.scratchId3Data.reset(10);     // Catch: EOFException -> L23
        r14.peekFully(this.scratchId3Data.getData(), 0, 10);     // Catch: EOFException -> L23
        if (this.scratchId3Data.readUnsignedInt24() == 4801587) goto L7;
        return C0180C.TIME_UNSET;
    L7:
        this.scratchId3Data.skipBytes(3);
        int r5 = this.scratchId3Data.readSynchSafeInt();
        int r6 = r5 + 10;
        if (r6 <= this.scratchId3Data.capacity()) goto L10;
        byte[] r7 = this.scratchId3Data.getData();
        this.scratchId3Data.reset(r6);
        System.arraycopy(r7, 0, this.scratchId3Data.getData(), 0, 10);
    L10:
        r14.peekFully(this.scratchId3Data.getData(), 10, r5);
        Metadata r3 = this.id3Decoder.decode(this.scratchId3Data.getData(), r5);
        if (r3 != null) goto L13;
        return C0180C.TIME_UNSET;
    L13:
        int r8 = r3.length();
        int r9 = 0;
    L14:
        if (r9 >= r8) goto L22;
        Metadata.Entry r10 = r3.get(r9);
        if ((r10 instanceof PrivFrame) == false) goto L21;
        PrivFrame r11 = (PrivFrame) r10;
        if (PRIV_TIMESTAMP_FRAME_OWNER.equals(r11.owner) == false) goto L21;
        System.arraycopy(r11.privateData, 0, this.scratchId3Data.getData(), 0, 8);
        this.scratchId3Data.setPosition(0);
        this.scratchId3Data.setLimit(8);
        return this.scratchId3Data.readLong() & 8589934591L;
    L21:
        r9 = r9 + 1;
        goto L14
    L22:
        return C0180C.TIME_UNSET;
    L24:
        return C0180C.TIME_UNSET;
    }

    @EnsuresNonNull({"extractor"})
    @RequiresNonNull({"output"})
    private DefaultExtractorInput prepareExtraction(DataSource r18, DataSpec r19, boolean r20) throws IOException {
        long r7 = r18.open(r19);
        if (r20 == true) goto L29;
    L12:
        DefaultExtractorInput r3 = new DefaultExtractorInput(r18, r19.position, r7);
        if (this.extractor != null) goto L27;
        long r4 = peekId3PrivTimestamp(r3);
        r3.resetPeekPosition();
        if (this.previousExtractor == null) goto L17;
        HlsMediaChunkExtractor r0 = this.previousExtractor.recreate();
    L18:
        this.extractor = r0;
        if (this.extractor.isPackedAudioExtractor() == false) goto L25;
        HlsSampleStreamWrapper r1 = this.output;
        if (r4 == C0180C.TIME_UNSET) goto L23;
        long r5 = this.timestampAdjuster.adjustTsTimestamp(r4);
    L24:
        r1.setSampleOffsetUs(r5);
    L26:
        this.output.onNewExtractor();
        this.extractor.init(this.output);
        goto L27
    L23:
        r5 = this.startTimeUs;
        goto L24
    L25:
        this.output.setSampleOffsetUs(0);
        goto L26
    L17:
        r0 = this.extractorFactory.createExtractor(r19.uri, this.trackFormat, this.muxedCaptionFormats, this.timestampAdjuster, r18.getResponseHeaders(), r3, this.playerId);
    L27:
        this.output.setDrmInitData(this.drmInitData);
        return r3;
    L29:
        this.timestampAdjuster.sharedInitializeOrWait(this.isPrimaryTimestampSource, this.startTimeUs, this.timestampAdjusterInitializationTimeoutMs);     // Catch: TimeoutException -> L6 InterruptedException -> L9
    L6:
        e = move-exception;
        throw new IOException(e);
    L11:
        throw new InterruptedIOException();
    }

    public static boolean shouldSpliceIn(HlsMediaChunk r5, Uri r6, HlsMediaPlaylist r7, HlsChunkSource.SegmentBaseHolder r8, long r9) {
        if (r5 != null) goto L6;
        return false;
    L6:
        if (r6.equals(r5.playlistUrl) == true) goto L8;
    L10:
        long r1 = r8.segmentBase.relativeStartTimeUs + r9;
        if (isIndependent(r8, r7) == true) goto L13;
    L14:
        return true;
    L13:
        if (r1 < r5.endTimeUs) goto L14;
        return false;
    L8:
        if (r5.loadCompleted == false) goto L10;
        return false;
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
    public void cancelLoad() {
        this.loadCanceled = true;
    }

    public int getFirstSampleIndex(int r2) {
        Assertions.checkState(!this.shouldSpliceIn);
        if (r2 < this.sampleQueueFirstSampleIndices.size()) goto L7;
        return 0;
    L7:
        return this.sampleQueueFirstSampleIndices.get(r2).intValue();
    }

    public void init(HlsSampleStreamWrapper r1, ImmutableList<Integer> r2) {
        this.output = r1;
        this.sampleQueueFirstSampleIndices = r2;
    }

    public void invalidateExtractor() {
        this.extractorInvalidated = true;
    }

    @Override // androidx.media3.exoplayer.source.chunk.MediaChunk
    public boolean isLoadCompleted() {
        return this.loadCompleted;
    }

    public boolean isPublished() {
        return this.isPublished;
    }

    @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
    public void load() throws IOException {
        Assertions.checkNotNull(this.output);
        if (this.extractor == null) goto L5;
    L9:
        maybeLoadInitData();
        if (this.loadCanceled == false) goto L12;
        return;
    L12:
        if (this.hasGapTag == true) goto L14;
        loadMedia();
    L14:
        this.loadCompleted = !this.loadCanceled;
        return;
    L5:
        if (this.previousExtractor == null) goto L9;
        if (this.previousExtractor.isReusable() == false) goto L9;
        this.extractor = this.previousExtractor;
        this.initDataLoadRequired = false;
        goto L9
    }

    public void publish() {
        this.isPublished = true;
    }
}
