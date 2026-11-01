package androidx.media3.exoplayer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaDataSource;
import android.media.MediaFormat;
import android.net.Uri;
import android.util.SparseArray;
import androidx.media3.common.Format;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.MediaFormatUtil;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSourceUtil;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.FileDescriptorDataSource;
import androidx.media3.datasource.MediaDataSourceAdapter;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil;
import androidx.media3.exoplayer.source.SampleQueue;
import androidx.media3.exoplayer.source.UnrecognizedInputFormatException;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.DefaultAllocator;
import androidx.media3.extractor.DefaultExtractorInput;
import androidx.media3.extractor.DefaultExtractorsFactory;
import androidx.media3.extractor.DiscardingTrackOutput;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.SeekPoint;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.mp4.Mp4Extractor;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;

/* JADX INFO: loaded from: classes.dex */
public final class MediaExtractorCompat {
    public static final int SEEK_TO_CLOSEST_SYNC = 2;
    public static final int SEEK_TO_NEXT_SYNC = 1;
    public static final int SEEK_TO_PREVIOUS_SYNC = 0;
    private static final String TAG = "MediaExtractorCompat";
    private final Allocator allocator;
    private DataSource currentDataSource;
    private Extractor currentExtractor;
    private ExtractorInput currentExtractorInput;
    private final DataSource.Factory dataSourceFactory;
    private final ExtractorsFactory extractorsFactory;
    private final FormatHolder formatHolder;
    private boolean hasBeenPrepared;
    private Map<String, String> httpRequestHeaders;
    private final DecoderInputBuffer noDataBuffer;
    private long offsetInCurrentFile;
    private SeekPoint pendingSeek;
    private final PositionHolder positionHolder;
    private final DecoderInputBuffer sampleHolderWithBufferReplacementDirect;
    private final DecoderInputBuffer sampleHolderWithBufferReplacementDisabled;
    private final SparseArray<MediaExtractorSampleQueue> sampleQueues;
    private SeekMap seekMap;
    private final Set<Integer> selectedTrackIndices;
    private final ArrayDeque<Integer> trackIndicesPerSampleInQueuedOrder;
    private final ArrayList<MediaExtractorTrack> tracks;
    private boolean tracksEnded;
    private int upstreamFormatsCount;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.MediaExtractorCompat$1 */
    static /* synthetic */ class C02351 {
    }

    private final class ExtractorOutputImpl implements ExtractorOutput {
        final /* synthetic */ MediaExtractorCompat this$0;

        private ExtractorOutputImpl(MediaExtractorCompat r1) {
            this.this$0 = r1;
        }

        /* synthetic */ ExtractorOutputImpl(MediaExtractorCompat r1, C02351 r2) {
            this(r1);
        }

        @Override // androidx.media3.extractor.ExtractorOutput
        public void endTracks() {
            MediaExtractorCompat.access$302(this.this$0, true);
        }

        @Override // androidx.media3.extractor.ExtractorOutput
        public void seekMap(SeekMap r2) {
            MediaExtractorCompat.access$502(this.this$0, r2);
        }

        @Override // androidx.media3.extractor.ExtractorOutput
        public TrackOutput track(int r5, int r6) {
            MediaExtractorSampleQueue r0 = (MediaExtractorSampleQueue) MediaExtractorCompat.access$200(this.this$0).get(r5);
            if (r0 == null) goto L6;
            return r0;
        L6:
            if (MediaExtractorCompat.access$300(this.this$0) == true) goto L8;
            MediaExtractorSampleQueue r1 = new MediaExtractorSampleQueue(this.this$0, MediaExtractorCompat.access$400(this.this$0), r5);
            MediaExtractorCompat.access$200(this.this$0).put(r5, r1);
            return r1;
        L8:
            return new DiscardingTrackOutput();
        }
    }

    private final class MediaExtractorSampleQueue extends SampleQueue {
        private int compatibilityTrackIndex;
        private int mainTrackIndex;
        final /* synthetic */ MediaExtractorCompat this$0;
        public final int trackId;

        public MediaExtractorSampleQueue(MediaExtractorCompat r1, Allocator r2, int r3) {
            this.this$0 = r1;
            super(r2, null, null);
            this.trackId = r3;
            this.mainTrackIndex = -1;
            this.compatibilityTrackIndex = -1;
        }

        @Override // androidx.media3.exoplayer.source.SampleQueue
        public Format getAdjustedUpstreamFormat(Format r2) {
            if (getUpstreamFormat() != null) goto L6;
            MediaExtractorCompat.access$600(this.this$0, this, r2);
        L6:
            return super.getAdjustedUpstreamFormat(r2);
        }

        @Override // androidx.media3.exoplayer.source.SampleQueue, androidx.media3.extractor.TrackOutput
        public void sampleMetadata(long r9, int r11, int r12, int r13, TrackOutput.CryptoData r14) {
            int r4 = r11 & (-536870913);
            if (this.mainTrackIndex == (-1)) goto L5;
            boolean r15 = true;
        L6:
            Assertions.checkState(r15);
            int r16 = getWriteIndex();
            super.sampleMetadata(r9, r4, r12, r13, r14);
            if (getWriteIndex() == (r16 + 1)) goto L9;
            return;
        L9:
            if (this.compatibilityTrackIndex == (-1)) goto L11;
            MediaExtractorCompat.access$700(this.this$0).addLast(Integer.valueOf(this.compatibilityTrackIndex));
        L11:
            MediaExtractorCompat.access$700(this.this$0).addLast(Integer.valueOf(this.mainTrackIndex));
            return;
        L5:
            r15 = false;
            goto L6
        }

        public void setCompatibilityTrackIndex(int r1) {
            this.compatibilityTrackIndex = r1;
        }

        public void setMainTrackIndex(int r1) {
            this.mainTrackIndex = r1;
        }

        public String toString() {
            return String.format("trackId: %s, mainTrackIndex: %s, compatibilityTrackIndex: %s", new Object[]{Integer.valueOf(this.trackId), Integer.valueOf(this.mainTrackIndex), Integer.valueOf(this.compatibilityTrackIndex)});
        }
    }

    private static final class MediaExtractorTrack {
        public final String compatibilityTrackMimeType;
        public final boolean isCompatibilityTrack;
        public final MediaExtractorSampleQueue sampleQueue;

        private MediaExtractorTrack(MediaExtractorSampleQueue r1, boolean r2, String r3) {
            this.sampleQueue = r1;
            this.isCompatibilityTrack = r2;
            this.compatibilityTrackMimeType = r3;
        }

        /* synthetic */ MediaExtractorTrack(MediaExtractorSampleQueue r1, boolean r2, String r3, C02351 r4) {
            this(r1, r2, r3);
        }

        public MediaFormat createDownstreamMediaFormat(FormatHolder r5, DecoderInputBuffer r6) {
            r5.clear();
            this.sampleQueue.read(r5, r6, 2, false);
            MediaFormat r1 = MediaFormatUtil.createMediaFormatFromFormat((Format) Assertions.checkNotNull(r5.format));
            r5.clear();
            if (this.compatibilityTrackMimeType != null) goto L5;
        L8:
            return r1;
        L5:
            if (Util.SDK_INT < 29) goto L7;
            r1.removeKey("codecs-string");
        L7:
            r1.setString("mime", this.compatibilityTrackMimeType);
            goto L8
        }

        public void discardFrontSample() {
            this.sampleQueue.skip(1);
            this.sampleQueue.discardToRead();
        }

        public int getIdOfBackingTrack() {
            return this.sampleQueue.trackId;
        }

        public String toString() {
            return String.format("MediaExtractorSampleQueue: %s, isCompatibilityTrack: %s, compatibilityTrackMimeType: %s", new Object[]{this.sampleQueue, Boolean.valueOf(this.isCompatibilityTrack), this.compatibilityTrackMimeType});
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SeekMode {
    }

    public MediaExtractorCompat(Context r3) {
        this(new DefaultExtractorsFactory(), new DefaultDataSource.Factory(r3));
    }

    public MediaExtractorCompat(ExtractorsFactory r4, DataSource.Factory r5) {
        this.extractorsFactory = r4;
        this.dataSourceFactory = r5;
        this.positionHolder = new PositionHolder();
        this.allocator = new DefaultAllocator(true, 65536);
        this.tracks = new ArrayList();
        this.sampleQueues = new SparseArray();
        this.trackIndicesPerSampleInQueuedOrder = new ArrayDeque();
        this.formatHolder = new FormatHolder();
        this.sampleHolderWithBufferReplacementDisabled = new DecoderInputBuffer(0);
        this.sampleHolderWithBufferReplacementDirect = new DecoderInputBuffer(2);
        this.noDataBuffer = DecoderInputBuffer.newNoDataInstance();
        this.selectedTrackIndices = new HashSet();
    }

    static /* synthetic */ SparseArray access$200(MediaExtractorCompat r1) {
        return r1.sampleQueues;
    }

    static /* synthetic */ boolean access$300(MediaExtractorCompat r1) {
        return r1.tracksEnded;
    }

    static /* synthetic */ boolean access$302(MediaExtractorCompat r0, boolean r1) {
        r0.tracksEnded = r1;
        return r1;
    }

    static /* synthetic */ Allocator access$400(MediaExtractorCompat r1) {
        return r1.allocator;
    }

    static /* synthetic */ SeekMap access$502(MediaExtractorCompat r0, SeekMap r1) {
        r0.seekMap = r1;
        return r1;
    }

    static /* synthetic */ void access$600(MediaExtractorCompat r0, MediaExtractorSampleQueue r1, Format r2) {
        r0.onSampleQueueFormatInitialized(r1, r2);
    }

    static /* synthetic */ ArrayDeque access$700(MediaExtractorCompat r1) {
        return r1.trackIndicesPerSampleInQueuedOrder;
    }

    @EnsuresNonNullIf(expression = {"trackIndicesPerSampleInQueuedOrder.peekFirst()"}, result = true)
    private boolean advanceToSampleOrEndOfInput() {
        maybeResolvePendingSeek();     // Catch: IOException -> L24
        boolean r3 = false;
    L6:
        if (this.trackIndicesPerSampleInQueuedOrder.isEmpty() == false) goto L8;
        if (r3 == true) goto L23;
        int r4 = ((Extractor) Assertions.checkNotNull(this.currentExtractor)).read((ExtractorInput) Assertions.checkNotNull(this.currentExtractorInput), this.positionHolder);     // Catch: Throwable -> L18 Exception -> L20
        if (r4 == (-1)) goto L14;
        if (r4 != 1) goto L6;
        this.currentExtractorInput = reopenCurrentDataSource(this.positionHolder.position);     // Catch: Throwable -> L18 Exception -> L20
        goto L6
    L14:
        r3 = true;
    L18:
        e = move-exception;
        Log.m49w(TAG, "Treating exception as the end of input.", e);
        r3 = true;
        goto L6
    L23:
        return false;
    L8:
        if (this.selectedTrackIndices.contains(this.trackIndicesPerSampleInQueuedOrder.peekFirst()) == true) goto L9;
        skipOneSample();
        goto L6
    L9:
        return true;
    L24:
        e = move-exception;
        Log.m49w(TAG, "Treating exception as the end of input.", e);
        return false;
    }

    private DataSpec buildDataSpec(Uri r3, long r4) {
        DataSpec.Builder r0 = new DataSpec.Builder().setUri(r3).setPosition(r4).setFlags(6);
        if (this.httpRequestHeaders == null) goto L6;
        r0.setHttpRequestHeaders(this.httpRequestHeaders);
    L6:
        return r0.build();
    }

    static /* synthetic */ String lambda$selectExtractor$0(Extractor r1) {
        return r1.getUnderlyingImplementation().getClass().getSimpleName();
    }

    private void maybeResolvePendingSeek() throws IOException {
        if (this.pendingSeek != null) goto L5;
        return;
    L5:
        SeekPoint r0 = (SeekPoint) Assertions.checkNotNull(this.pendingSeek);
        ((Extractor) Assertions.checkNotNull(this.currentExtractor)).seek(r0.position, r0.timeUs);
        this.currentExtractorInput = reopenCurrentDataSource(r0.position);
        this.pendingSeek = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onSampleQueueFormatInitialized(MediaExtractorSampleQueue r6, Format r7) {
        boolean r1 = true;
        this.upstreamFormatsCount++;
        r6.setMainTrackIndex(this.tracks.size());
        Object[] r4 = 0;
        this.tracks.add(new MediaExtractorTrack(r6, false, null, 0 == true ? 1 : 0));
        String r0 = MediaCodecUtil.getAlternativeCodecMimeType(r7);
        if (r0 == null) goto L6;
        r6.setCompatibilityTrackIndex(this.tracks.size());
        this.tracks.add(new MediaExtractorTrack(r6, r1, r0, r4 == true ? 1 : 0));
        return;
    }

    private void peekNextSelectedTrackSample(DecoderInputBuffer r8, boolean r9) {
        MediaExtractorSampleQueue r1 = this.tracks.get(((Integer) Assertions.checkNotNull(this.trackIndicesPerSampleInQueuedOrder.peekFirst())).intValue()).sampleQueue;
        boolean r2 = false;
        if (r9 == false) goto L5;
        int r3 = 4;
    L6:
        int r4 = r3 | 1;
        int r5 = r1.read(this.formatHolder, r8, r4, false);
        if (r5 != (-5)) goto L9;
        r5 = r1.read(this.formatHolder, r8, r4, false);
    L9:
        this.formatHolder.clear();
        if (r5 != (-4)) goto L12;
        r2 = true;
    L12:
        Assertions.checkState(r2);
        return;
    L5:
        r3 = 0;
        goto L6
    }

    private void prepareDataSource(DataSource r11, DataSpec r12) throws IOException {
        Assertions.checkState(!this.hasBeenPrepared);
        this.hasBeenPrepared = true;
        this.offsetInCurrentFile = r12.position;
        this.currentDataSource = r11;
        DefaultExtractorInput r2 = new DefaultExtractorInput(this.currentDataSource, 0, this.currentDataSource.open(r12));
        Extractor r3 = selectExtractor(r2);
        r3.init(new ExtractorOutputImpl(this, null));
        Throwable r5 = null;
        ExtractorInput r4 = r2;
        boolean r6 = true;
    L3:
        if (r6 == false) goto L34;
        int r0 = r3.read(r4, this.positionHolder);     // Catch: OutOfMemoryError -> L6 Throwable -> L8
    L11:
        if (this.tracksEnded == true) goto L13;
    L18:
        boolean r8 = true;
    L19:
        r6 = r8;
        if (r5 != null) goto L28;
        if (r6 == true) goto L23;
    L25:
        if (r0 != 1) goto L3;
        r4 = reopenCurrentDataSource(this.positionHolder.position);
        goto L3
    L23:
        if (r0 != (-1)) goto L25;
    L28:
        release();
        if (r5 == null) goto L31;
        String r1 = "Exception encountered while parsing input media.";
    L33:
        throw ParserException.createForMalformedContainer(r1, r5);
    L31:
        r1 = "Reached end of input before preparation completed.";
        goto L33
    L13:
        if (this.upstreamFormatsCount < this.sampleQueues.size()) goto L18;
        if (this.seekMap == null) goto L18;
        r8 = false;
    L8:
        e = move-exception;
        r5 = e;
        r0 = -1;
        goto L11
    L34:
        this.currentExtractorInput = r4;
        this.currentExtractor = r3;
    }

    private ExtractorInput reopenCurrentDataSource(long r8) throws IOException {
        DataSource r2 = (DataSource) Assertions.checkNotNull(this.currentDataSource);
        Uri r0 = (Uri) Assertions.checkNotNull(r2.getUri());
        DataSourceUtil.closeQuietly(r2);
        long r3 = r2.open(buildDataSpec(r0, this.offsetInCurrentFile + r8));
        if (r3 == (-1)) goto L5;
        long r5 = r3 + r8;
    L7:
        return new DefaultExtractorInput(r2, r8, r5);
    L5:
        r5 = r3;
        goto L7
    }

    private Extractor selectExtractor(ExtractorInput r8) throws IOException {
        Extractor[] r0 = this.extractorsFactory.createExtractors();
        Extractor r1 = null;
        int r2 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L13;
        Extractor r4 = r0[r3];
        if (r4.sniff(r8) == false) goto L12;
        r1 = r4;
        r8.resetPeekPosition();
        goto L13
    L8:
        th = move-exception;
        r8.resetPeekPosition();
        throw th;
    L12:
        r8.resetPeekPosition();
        r3 = r3 + 1;
    L13:
        if (r1 == null) goto L16;
        return r1;
    L16:
        throw new UnrecognizedInputFormatException("None of the available extractors (" + Joiner.m184on(", ").join(Lists.transform(ImmutableList.copyOf(r0), new MediaExtractorCompat$$ExternalSyntheticLambda0())) + ") could read the stream.", (Uri) Assertions.checkNotNull(((DataSource) Assertions.checkNotNull(this.currentDataSource)).getUri()), ImmutableList.m212of());
    }

    private void skipOneSample() {
        int r0 = this.trackIndicesPerSampleInQueuedOrder.removeFirst().intValue();
        MediaExtractorTrack r1 = this.tracks.get(r0);
        if (r1.isCompatibilityTrack == true) goto L6;
        r1.discardFrontSample();
        return;
    }

    public boolean advance() {
        if (advanceToSampleOrEndOfInput() == true) goto L6;
        return false;
    L6:
        skipOneSample();
        return advanceToSampleOrEndOfInput();
    }

    public Allocator getAllocator() {
        return this.allocator;
    }

    public int getSampleFlags() {
        if (advanceToSampleOrEndOfInput() == true) goto L6;
        return -1;
    L6:
        peekNextSelectedTrackSample(this.noDataBuffer, true);
        if (this.noDataBuffer.isEncrypted() == false) goto L9;
        int r1 = 2;
    L11:
        return (0 | r1) | (this.noDataBuffer.isKeyFrame() ? 1 : 0);
    L9:
        r1 = 0;
        goto L11
    }

    public long getSampleSize() {
        if (advanceToSampleOrEndOfInput() == true) goto L6;
        return -1;
    L6:
        peekNextSelectedTrackSample(this.sampleHolderWithBufferReplacementDirect, false);
        ByteBuffer r0 = (ByteBuffer) Assertions.checkNotNull(this.sampleHolderWithBufferReplacementDirect.data);
        int r2 = r0.position();
        r0.position(0);
        return r2;
    }

    public long getSampleTime() {
        if (advanceToSampleOrEndOfInput() == true) goto L6;
        return -1;
    L6:
        peekNextSelectedTrackSample(this.noDataBuffer, true);
        return this.noDataBuffer.timeUs;
    }

    public int getSampleTrackIndex() {
        if (advanceToSampleOrEndOfInput() == true) goto L7;
        return -1;
    L7:
        return this.trackIndicesPerSampleInQueuedOrder.peekFirst().intValue();
    }

    public int getTrackCount() {
        return this.tracks.size();
    }

    public MediaFormat getTrackFormat(int r4) {
        return this.tracks.get(r4).createDownstreamMediaFormat(this.formatHolder, this.noDataBuffer);
    }

    public int readSampleData(ByteBuffer r3, int r4) {
        if (advanceToSampleOrEndOfInput() == true) goto L6;
        return -1;
    L6:
        r3.position(r4);
        r3.limit(r3.capacity());
        this.sampleHolderWithBufferReplacementDisabled.data = r3;
        peekNextSelectedTrackSample(this.sampleHolderWithBufferReplacementDisabled, false);
        r3.flip();
        r3.position(r4);
        this.sampleHolderWithBufferReplacementDisabled.data = null;
        return r3.remaining();
    }

    public void release() {
        int r0 = 0;
    L4:
        if (r0 >= this.sampleQueues.size()) goto L6;
        this.sampleQueues.valueAt(r0).release();
        r0 = r0 + 1;
        goto L4
    L6:
        this.sampleQueues.clear();
        if (this.currentExtractor == null) goto L9;
        this.currentExtractor.release();
        this.currentExtractor = null;
    L9:
        this.currentExtractorInput = null;
        this.pendingSeek = null;
        DataSourceUtil.closeQuietly(this.currentDataSource);
        this.currentDataSource = null;
    }

    public void seekTo(long r6, int r8) {
        if (this.seekMap != null) goto L6;
        return;
    L6:
        if (this.selectedTrackIndices.size() == 1) goto L8;
    L10:
        SeekMap.SeekPoints r0 = this.seekMap.getSeekPoints(r6);
    L11:
        switch(r8) {
            case 0: goto L20;
            case 1: goto L19;
            case 2: goto L15;
            default: goto L13;
        };
    L19:
        SeekPoint r1 = r0.second;
    L21:
        this.trackIndicesPerSampleInQueuedOrder.clear();
        int r2 = 0;
    L23:
        if (r2 >= this.sampleQueues.size()) goto L25;
        this.sampleQueues.valueAt(r2).reset();
        r2 = r2 + 1;
        goto L23
    L25:
        this.pendingSeek = r1;
        return;
    L20:
        r1 = r0.first;
        goto L21
    L13:
        throw new IllegalArgumentException();
    L15:
        if (Math.abs(r6 - r0.second.timeUs) >= Math.abs(r6 - r0.first.timeUs)) goto L17;
        r1 = r0.second;
        goto L21
    L17:
        r1 = r0.first;
        goto L21
    L8:
        if ((this.currentExtractor instanceof Mp4Extractor) == false) goto L10;
        r0 = ((Mp4Extractor) this.currentExtractor).getSeekPoints(r6, this.tracks.get(this.selectedTrackIndices.iterator().next().intValue()).getIdOfBackingTrack());
        goto L11
    }

    public void selectTrack(int r3) {
        this.selectedTrackIndices.add(Integer.valueOf(r3));
    }

    public void setDataSource(Context r7, Uri r8, Map<String, String> r9) throws IOException {
        String r0 = r8.getScheme();
        String r1 = r8.getPath();
        if (r0 != null) goto L5;
    L6:
        if (r1 == null) goto L35;
        setDataSource(r1);
        return;
    L35:
        AssetFileDescriptor r3 = r7.getContentResolver().openAssetFileDescriptor(r8, "r");     // Catch: FileNotFoundException -> L26 SecurityException -> L28
        if (r3 != null) goto L33;
        if (r3 == null) goto L29;
        r3.close();     // Catch: FileNotFoundException -> L26 SecurityException -> L28
        goto L29
    L33:
        setDataSource(r3);     // Catch: Throwable -> L16
        if (r3 == null) goto L36;
        r3.close();     // Catch: FileNotFoundException -> L26 SecurityException -> L28
        return;
    L36:
        return;
    L16:
        th = move-exception;
        if (r3 != null) goto L31;
    L22:
        throw th;     // Catch: FileNotFoundException -> L26 SecurityException -> L28
    L31:
        r3.close();     // Catch: Throwable -> L20
    L20:
        th = move-exception;
        th.addSuppressed(th);     // Catch: FileNotFoundException -> L26 SecurityException -> L28
    L29:
        setDataSource(r8.toString(), r9);
        return;
    L5:
        if (r0.equals("file") == false) goto L35;
        goto L6
    }

    public void setDataSource(AssetFileDescriptor r8) throws IOException {
        if (r8.getDeclaredLength() != (-1)) goto L5;
        setDataSource(r8.getFileDescriptor());
        return;
    L5:
        setDataSource(r8.getFileDescriptor(), r8.getStartOffset(), r8.getDeclaredLength());
    }

    public void setDataSource(MediaDataSource r5) throws IOException {
        prepareDataSource(new MediaDataSourceAdapter(r5, false), buildDataSpec(Uri.EMPTY, 0));
    }

    public void setDataSource(Uri r3, long r4) throws IOException {
        prepareDataSource(this.dataSourceFactory.createDataSource(), buildDataSpec(r3, r4));
    }

    public void setDataSource(FileDescriptor r7) throws IOException {
        setDataSource(r7, 0, -1);
    }

    public void setDataSource(FileDescriptor r7, long r8, long r10) throws IOException {
        prepareDataSource(new FileDescriptorDataSource(r7, r8, r10), buildDataSpec(Uri.EMPTY, 0));
    }

    public void setDataSource(String r2) throws IOException {
        setDataSource(r2, null);
    }

    public void setDataSource(String r5, Map<String, String> r6) throws IOException {
        this.httpRequestHeaders = r6;
        prepareDataSource(this.dataSourceFactory.createDataSource(), buildDataSpec(Uri.parse(r5), 0));
    }

    public void unselectTrack(int r3) {
        this.selectedTrackIndices.remove(Integer.valueOf(r3));
    }
}
