package androidx.media3.extractor.flac;

import androidx.media3.common.Metadata;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.FlacFrameReader;
import androidx.media3.extractor.FlacMetadataReader;
import androidx.media3.extractor.FlacSeekTableSeekMap;
import androidx.media3.extractor.FlacStreamMetadata;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.TrackOutput;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class FlacExtractor implements Extractor {
    private static final int BUFFER_LENGTH = 32768;
    public static final ExtractorsFactory FACTORY = null;
    public static final int FLAG_DISABLE_ID3_METADATA = 1;
    private static final int SAMPLE_NUMBER_UNKNOWN = -1;
    private static final int STATE_GET_FRAME_START_MARKER = 4;
    private static final int STATE_GET_STREAM_MARKER_AND_INFO_BLOCK_BYTES = 1;
    private static final int STATE_READ_FRAMES = 5;
    private static final int STATE_READ_ID3_METADATA = 0;
    private static final int STATE_READ_METADATA_BLOCKS = 3;
    private static final int STATE_READ_STREAM_MARKER = 2;
    private FlacBinarySearchSeeker binarySearchSeeker;
    private final ParsableByteArray buffer;
    private int currentFrameBytesWritten;
    private long currentFrameFirstSampleNumber;
    private ExtractorOutput extractorOutput;
    private FlacStreamMetadata flacStreamMetadata;
    private int frameStartMarker;
    private Metadata id3Metadata;
    private final boolean id3MetadataDisabled;
    private int minFrameSize;
    private final FlacFrameReader.SampleNumberHolder sampleNumberHolder;
    private int state;
    private final byte[] streamMarkerAndInfoBlock;
    private TrackOutput trackOutput;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    static {
        FACTORY = new FlacExtractor$$ExternalSyntheticLambda0();
    }

    public FlacExtractor() {
        this(0);
    }

    public FlacExtractor(int r4) {
        this.streamMarkerAndInfoBlock = new byte[42];
        this.buffer = new ParsableByteArray(new byte[32768], 0);
        if ((r4 & 1) == 0) goto L5;
        boolean r0 = true;
    L6:
        this.id3MetadataDisabled = r0;
        this.sampleNumberHolder = new FlacFrameReader.SampleNumberHolder();
        this.state = 0;
        return;
    L5:
        r0 = false;
        goto L6
    }

    private long findFrame(ParsableByteArray r5, boolean r6) {
        Assertions.checkNotNull(this.flacStreamMetadata);
        int r0 = r5.getPosition();
    L4:
        if (r0 > (r5.limit() - 16)) goto L10;
        r5.setPosition(r0);
        if (FlacFrameReader.checkAndReadFrameHeader(r5, this.flacStreamMetadata, this.frameStartMarker, this.sampleNumberHolder) == true) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        r5.setPosition(r0);
        return this.sampleNumberHolder.sampleNumber;
    L10:
        if (r6 == true) goto L12;
        r5.setPosition(r0);
        return -1;
    L12:
        if (r0 > (r5.limit() - this.minFrameSize)) goto L25;
        r5.setPosition(r0);
        boolean r1 = FlacFrameReader.checkAndReadFrameHeader(r5, this.flacStreamMetadata, this.frameStartMarker, this.sampleNumberHolder);     // Catch: IndexOutOfBoundsException -> L16
    L19:
        if (r5.getPosition() <= r5.limit()) goto L21;
        r1 = false;
    L21:
        if (r1 == true) goto L22;
        r0 = r0 + 1;
        goto L12
    L22:
        r5.setPosition(r0);
        return this.sampleNumberHolder.sampleNumber;
    L17:
        r1 = false;
        goto L19
    L25:
        r5.setPosition(r5.limit());
        return -1;
    }

    private void getFrameStartMarker(ExtractorInput r6) throws IOException {
        this.frameStartMarker = FlacMetadataReader.getFrameStartMarker(r6);
        ((ExtractorOutput) Util.castNonNull(this.extractorOutput)).seekMap(getSeekMap(r6.getPosition(), r6.getLength()));
        this.state = 5;
    }

    private SeekMap getSeekMap(long r9, long r11) {
        Assertions.checkNotNull(this.flacStreamMetadata);
        if (this.flacStreamMetadata.seekTable == null) goto L7;
        return new FlacSeekTableSeekMap(this.flacStreamMetadata, r9);
    L7:
        if (r11 == (-1)) goto L13;
        if (this.flacStreamMetadata.totalSamples <= 0) goto L13;
        this.binarySearchSeeker = new FlacBinarySearchSeeker(this.flacStreamMetadata, this.frameStartMarker, r9, r11);
        return this.binarySearchSeeker.getSeekMap();
    L13:
        return new SeekMap.Unseekable(this.flacStreamMetadata.getDurationUs());
    }

    private void getStreamMarkerAndInfoBlockBytes(ExtractorInput r4) throws IOException {
        r4.peekFully(this.streamMarkerAndInfoBlock, 0, this.streamMarkerAndInfoBlock.length);
        r4.resetPeekPosition();
        this.state = 2;
    }

    static /* synthetic */ Extractor[] lambda$static$0() {
        return new Extractor[]{new FlacExtractor()};
    }

    private void outputSampleMetadata() {
        long r5 = (this.currentFrameFirstSampleNumber * 1000000) / ((long) ((FlacStreamMetadata) Util.castNonNull(this.flacStreamMetadata)).sampleRate);
        ((TrackOutput) Util.castNonNull(this.trackOutput)).sampleMetadata(r5, 1, this.currentFrameBytesWritten, 0, null);
    }

    private int readFrames(ExtractorInput r12, PositionHolder r13) throws IOException {
        Assertions.checkNotNull(this.trackOutput);
        Assertions.checkNotNull(this.flacStreamMetadata);
        if (this.binarySearchSeeker == null) goto L9;
        if (this.binarySearchSeeker.isSeeking() == false) goto L9;
        return this.binarySearchSeeker.handlePendingSeek(r12, r13);
    L9:
        if (this.currentFrameFirstSampleNumber != (-1)) goto L12;
        this.currentFrameFirstSampleNumber = FlacFrameReader.getFirstSampleNumber(r12, this.flacStreamMetadata);
        return 0;
    L12:
        int r0 = this.buffer.limit();
        boolean r4 = false;
        if (r0 >= 32768) goto L25;
        int r5 = r12.read(this.buffer.getData(), r0, 32768 - r0);
        if (r5 != (-1)) goto L17;
        boolean r7 = true;
    L18:
        r4 = r7;
        if (r4 == true) goto L22;
        this.buffer.setLimit(r0 + r5);
        goto L25
    L22:
        if (this.buffer.bytesLeft() != 0) goto L25;
        outputSampleMetadata();
        return -1;
    L17:
        r7 = false;
    L25:
        int r6 = this.buffer.getPosition();
        if (this.currentFrameBytesWritten >= this.minFrameSize) goto L28;
        this.buffer.skipBytes(Math.min(this.minFrameSize - this.currentFrameBytesWritten, this.buffer.bytesLeft()));
    L28:
        long r8 = findFrame(this.buffer, r4);
        int r9 = this.buffer.getPosition() - r6;
        this.buffer.setPosition(r6);
        this.trackOutput.sampleData(this.buffer, r9);
        this.currentFrameBytesWritten += r9;
        if (r8 == (-1)) goto L32;
        outputSampleMetadata();
        this.currentFrameBytesWritten = 0;
        this.currentFrameFirstSampleNumber = r8;
    L32:
        if (this.buffer.bytesLeft() >= 16) goto L34;
        int r2 = this.buffer.bytesLeft();
        System.arraycopy(this.buffer.getData(), this.buffer.getPosition(), this.buffer.getData(), 0, r2);
        this.buffer.setPosition(0);
        this.buffer.setLimit(r2);
    L34:
        return 0;
    }

    private void readId3Metadata(ExtractorInput r3) throws IOException {
        this.id3Metadata = FlacMetadataReader.readId3Metadata(r3, !this.id3MetadataDisabled);
        this.state = 1;
    }

    private void readMetadataBlocks(ExtractorInput r7) throws IOException {
        boolean r0 = false;
        FlacMetadataReader.FlacStreamMetadataHolder r1 = new FlacMetadataReader.FlacStreamMetadataHolder(this.flacStreamMetadata);
    L3:
        if (r0 == true) goto L5;
        r0 = FlacMetadataReader.readMetadataBlock(r7, r1);
        this.flacStreamMetadata = (FlacStreamMetadata) Util.castNonNull(r1.flacStreamMetadata);
        goto L3
    L5:
        Assertions.checkNotNull(this.flacStreamMetadata);
        this.minFrameSize = Math.max(this.flacStreamMetadata.minFrameSize, 6);
        ((TrackOutput) Util.castNonNull(this.trackOutput)).format(this.flacStreamMetadata.getFormat(this.streamMarkerAndInfoBlock, this.id3Metadata));
        this.state = 4;
    }

    private void readStreamMarker(ExtractorInput r2) throws IOException {
        FlacMetadataReader.readStreamMarker(r2);
        this.state = 3;
    }

    @Override // androidx.media3.extractor.Extractor
    public /* synthetic */ List getSniffFailureDetails() {
        return Extractor.CC.$default$getSniffFailureDetails(this);
    }

    @Override // androidx.media3.extractor.Extractor
    public /* synthetic */ Extractor getUnderlyingImplementation() {
        return Extractor.CC.$default$getUnderlyingImplementation(this);
    }

    @Override // androidx.media3.extractor.Extractor
    public void init(ExtractorOutput r3) {
        this.extractorOutput = r3;
        this.trackOutput = r3.track(0, 1);
        r3.endTracks();
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r3, PositionHolder r4) throws IOException {
        switch(this.state) {
            case 0: goto L16;
            case 1: goto L14;
            case 2: goto L12;
            case 3: goto L10;
            case 4: goto L8;
            case 5: goto L7;
            default: goto L5;
        };
    L8:
        getFrameStartMarker(r3);
        return 0;
    L10:
        readMetadataBlocks(r3);
        return 0;
    L12:
        readStreamMarker(r3);
        return 0;
    L14:
        getStreamMarkerAndInfoBlockBytes(r3);
        return 0;
    L16:
        readId3Metadata(r3);
        return 0;
    L5:
        throw new IllegalStateException();
    L7:
        return readFrames(r3, r4);
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r5, long r7) {
        long r0 = 0;
        if (r5 != 0) goto L6;
        this.state = 0;
    L9:
        if (r7 == 0) goto L12;
        r0 = -1;
    L12:
        this.currentFrameFirstSampleNumber = r0;
        this.currentFrameBytesWritten = 0;
        this.buffer.reset(0);
        return;
    L6:
        if (this.binarySearchSeeker == null) goto L9;
        this.binarySearchSeeker.setSeekTargetUs(r7);
        goto L9
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r2) throws IOException {
        FlacMetadataReader.peekId3Metadata(r2, false);
        return FlacMetadataReader.checkAndPeekStreamMarker(r2);
    }
}
