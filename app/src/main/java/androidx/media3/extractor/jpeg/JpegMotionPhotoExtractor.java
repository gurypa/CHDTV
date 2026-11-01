package androidx.media3.extractor.jpeg;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.metadata.mp4.MotionPhotoMetadata;
import androidx.media3.extractor.mp4.Mp4Extractor;
import androidx.media3.extractor.text.SubtitleParser;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class JpegMotionPhotoExtractor implements Extractor {
    private static final long EXIF_HEADER = 1165519206;
    private static final int EXIF_ID_CODE_LENGTH = 6;
    private static final String HEADER_XMP_APP1 = "http://ns.adobe.com/xap/1.0/";
    private static final int MARKER_APP0 = 65504;
    private static final int MARKER_APP1 = 65505;
    private static final int MARKER_SOI = 65496;
    private static final int MARKER_SOS = 65498;
    private static final int STATE_ENDED = 6;
    private static final int STATE_READING_MARKER = 0;
    private static final int STATE_READING_MOTION_PHOTO_VIDEO = 5;
    private static final int STATE_READING_SEGMENT = 2;
    private static final int STATE_READING_SEGMENT_LENGTH = 1;
    private static final int STATE_SNIFFING_MOTION_PHOTO_VIDEO = 4;
    private ExtractorOutput extractorOutput;
    private ExtractorInput lastExtractorInput;
    private int marker;
    private MotionPhotoMetadata motionPhotoMetadata;
    private Mp4Extractor mp4Extractor;
    private StartOffsetExtractorInput mp4ExtractorStartOffsetExtractorInput;
    private long mp4StartPosition;
    private final ParsableByteArray scratch;
    private int segmentLength;
    private int state;

    public JpegMotionPhotoExtractor() {
        this.scratch = new ParsableByteArray(6);
        this.mp4StartPosition = -1;
    }

    private void advancePeekPositionToNextSegment(ExtractorInput r4) throws IOException {
        this.scratch.reset(2);
        r4.peekFully(this.scratch.getData(), 0, 2);
        r4.advancePeekPosition(this.scratch.readUnsignedShort() - 2);
    }

    private void endReading() {
        ((ExtractorOutput) Assertions.checkNotNull(this.extractorOutput)).endTracks();
        this.extractorOutput.seekMap(new SeekMap.Unseekable(C0180C.TIME_UNSET));
        this.state = 6;
    }

    private static MotionPhotoMetadata getMotionPhotoMetadata(String r2, long r3) throws IOException {
        if (r3 != (-1)) goto L5;
        return null;
    L5:
        MotionPhotoDescription r0 = XmpMotionPhotoDescriptionParser.parse(r2);
        if (r0 != null) goto L9;
        return null;
    L9:
        return r0.getMotionPhotoMetadata(r3);
    }

    private void outputImageTrack(MotionPhotoMetadata r6) {
        ((ExtractorOutput) Assertions.checkNotNull(this.extractorOutput)).track(1024, 4).format(new Format.Builder().setContainerMimeType(MimeTypes.IMAGE_JPEG).setMetadata(new Metadata(new Metadata.Entry[]{r6})).build());
    }

    private int peekMarker(ExtractorInput r4) throws IOException {
        this.scratch.reset(2);
        r4.peekFully(this.scratch.getData(), 0, 2);
        return this.scratch.readUnsignedShort();
    }

    private void readMarker(ExtractorInput r5) throws IOException {
        this.scratch.reset(2);
        r5.readFully(this.scratch.getData(), 0, 2);
        this.marker = this.scratch.readUnsignedShort();
        if (this.marker != MARKER_SOS) goto L9;
        if (this.mp4StartPosition == (-1)) goto L7;
        this.state = 4;
        return;
    L7:
        endReading();
        return;
    L9:
        if (this.marker < 65488) goto L13;
        if (this.marker > 65497) goto L13;
        return;
    L13:
        if (this.marker == 65281) goto L19;
        this.state = 1;
        return;
    }

    private void readSegment(ExtractorInput r6) throws IOException {
        if (this.marker != MARKER_APP1) goto L14;
        ParsableByteArray r0 = new ParsableByteArray(this.segmentLength);
        r6.readFully(r0.getData(), 0, this.segmentLength);
        if (this.motionPhotoMetadata == null) goto L7;
    L15:
        this.state = 0;
        return;
    L7:
        if (HEADER_XMP_APP1.equals(r0.readNullTerminatedString()) == false) goto L15;
        String r1 = r0.readNullTerminatedString();
        if (r1 == null) goto L15;
        this.motionPhotoMetadata = getMotionPhotoMetadata(r1, r6.getLength());
        if (this.motionPhotoMetadata == null) goto L15;
        this.mp4StartPosition = this.motionPhotoMetadata.videoStartPosition;
        goto L15
    L14:
        r6.skipFully(this.segmentLength);
        goto L15
    }

    private void readSegmentLength(ExtractorInput r4) throws IOException {
        this.scratch.reset(2);
        r4.readFully(this.scratch.getData(), 0, 2);
        this.segmentLength = this.scratch.readUnsignedShort() - 2;
        this.state = 2;
    }

    private void sniffMotionPhotoVideo(ExtractorInput r7) throws IOException {
        if (r7.peekFully(this.scratch.getData(), 0, 1, true) == true) goto L5;
        endReading();
        return;
    L5:
        r7.resetPeekPosition();
        if (this.mp4Extractor != null) goto L8;
        this.mp4Extractor = new Mp4Extractor(SubtitleParser.Factory.UNSUPPORTED, 8);
    L8:
        this.mp4ExtractorStartOffsetExtractorInput = new StartOffsetExtractorInput(r7, this.mp4StartPosition);
        if (this.mp4Extractor.sniff(this.mp4ExtractorStartOffsetExtractorInput) == false) goto L11;
        this.mp4Extractor.init(new StartOffsetExtractorOutput(this.mp4StartPosition, (ExtractorOutput) Assertions.checkNotNull(this.extractorOutput)));
        startReadingMotionPhoto();
        return;
    L11:
        endReading();
    }

    private void startReadingMotionPhoto() {
        outputImageTrack((MotionPhotoMetadata) Assertions.checkNotNull(this.motionPhotoMetadata));
        this.state = 5;
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
    public void init(ExtractorOutput r1) {
        this.extractorOutput = r1;
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r8, PositionHolder r9) throws IOException {
        switch(this.state) {
            case 0: goto L27;
            case 1: goto L25;
            case 2: goto L23;
            case 3: goto L5;
            case 4: goto L18;
            case 5: goto L9;
            case 6: goto L6;
            default: goto L5;
        };
    L6:
        return -1;
    L23:
        readSegment(r8);
        return 0;
    L25:
        readSegmentLength(r8);
        return 0;
    L27:
        readMarker(r8);
        return 0;
    L5:
        throw new IllegalStateException();
    L9:
        if (this.mp4ExtractorStartOffsetExtractorInput != null) goto L11;
    L12:
        this.lastExtractorInput = r8;
        this.mp4ExtractorStartOffsetExtractorInput = new StartOffsetExtractorInput(r8, this.mp4StartPosition);
    L13:
        int r0 = ((Mp4Extractor) Assertions.checkNotNull(this.mp4Extractor)).read(this.mp4ExtractorStartOffsetExtractorInput, r9);
        if (r0 != 1) goto L16;
        r9.position += this.mp4StartPosition;
    L16:
        return r0;
    L11:
        if (r8 == this.lastExtractorInput) goto L13;
    L18:
        if (r8.getPosition() == this.mp4StartPosition) goto L21;
        r9.position = this.mp4StartPosition;
        return 1;
    L21:
        sniffMotionPhotoVideo(r8);
        return 0;
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
        if (this.mp4Extractor == null) goto L6;
        this.mp4Extractor.release();
        return;
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r3, long r5) {
        if (r3 != 0) goto L6;
        this.state = 0;
        this.mp4Extractor = null;
        return;
    L6:
        if (this.state != 5) goto L10;
        ((Mp4Extractor) Assertions.checkNotNull(this.mp4Extractor)).seek(r3, r5);
        return;
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r6) throws IOException {
        if (peekMarker(r6) == MARKER_SOI) goto L5;
        return false;
    L5:
        this.marker = peekMarker(r6);
        if (this.marker != MARKER_APP0) goto L9;
        advancePeekPositionToNextSegment(r6);
        this.marker = peekMarker(r6);
    L9:
        if (this.marker == MARKER_APP1) goto L11;
        return false;
    L11:
        r6.advancePeekPosition(2);
        this.scratch.reset(6);
        r6.peekFully(this.scratch.getData(), 0, 6);
        if (this.scratch.readUnsignedInt() == EXIF_HEADER) goto L14;
        return false;
    L14:
        if (this.scratch.readUnsignedShort() != 0) goto L18;
        return true;
    L18:
        return false;
    }
}
