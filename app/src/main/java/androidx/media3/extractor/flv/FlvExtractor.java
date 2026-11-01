package androidx.media3.extractor.flv;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.IndexSeekMap;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import androidx.recyclerview.widget.ItemTouchHelper;
import java.io.IOException;
import java.util.List;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class FlvExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = null;
    private static final int FLV_HEADER_SIZE = 9;
    private static final int FLV_TAG = 4607062;
    private static final int FLV_TAG_HEADER_SIZE = 11;
    private static final int STATE_READING_FLV_HEADER = 1;
    private static final int STATE_READING_TAG_DATA = 4;
    private static final int STATE_READING_TAG_HEADER = 3;
    private static final int STATE_SKIPPING_TO_TAG_HEADER = 2;
    private static final int TAG_TYPE_AUDIO = 8;
    private static final int TAG_TYPE_SCRIPT_DATA = 18;
    private static final int TAG_TYPE_VIDEO = 9;
    private AudioTagPayloadReader audioReader;
    private int bytesToNextTagHeader;
    private ExtractorOutput extractorOutput;
    private final ParsableByteArray headerBuffer;
    private long mediaTagTimestampOffsetUs;
    private final ScriptTagPayloadReader metadataReader;
    private boolean outputFirstSample;
    private boolean outputSeekMap;
    private final ParsableByteArray scratch;
    private int state;
    private final ParsableByteArray tagData;
    private int tagDataSize;
    private final ParsableByteArray tagHeaderBuffer;
    private long tagTimestampUs;
    private int tagType;
    private VideoTagPayloadReader videoReader;

    static {
        FACTORY = new FlvExtractor$$ExternalSyntheticLambda0();
    }

    public FlvExtractor() {
        this.scratch = new ParsableByteArray(4);
        this.headerBuffer = new ParsableByteArray(9);
        this.tagHeaderBuffer = new ParsableByteArray(11);
        this.tagData = new ParsableByteArray();
        this.metadataReader = new ScriptTagPayloadReader();
        this.state = 1;
    }

    @RequiresNonNull({"extractorOutput"})
    private void ensureReadyForMediaOutput() {
        if (this.outputSeekMap == true) goto L6;
        this.extractorOutput.seekMap(new SeekMap.Unseekable(C0180C.TIME_UNSET));
        this.outputSeekMap = true;
        return;
    }

    private long getCurrentTimestampUs() {
        if (this.outputFirstSample == false) goto L6;
        return this.mediaTagTimestampOffsetUs + this.tagTimestampUs;
    L6:
        if (this.metadataReader.getDurationUs() != C0180C.TIME_UNSET) goto L9;
        return 0;
    L9:
        return this.tagTimestampUs;
    }

    static /* synthetic */ Extractor[] lambda$static$0() {
        return new Extractor[]{new FlvExtractor()};
    }

    private ParsableByteArray prepareTagData(ExtractorInput r5) throws IOException {
        if (this.tagDataSize <= this.tagData.capacity()) goto L5;
        this.tagData.reset(new byte[Math.max(this.tagData.capacity() * 2, this.tagDataSize)], 0);
    L6:
        this.tagData.setLimit(this.tagDataSize);
        r5.readFully(this.tagData.getData(), 0, this.tagDataSize);
        return this.tagData;
    L5:
        this.tagData.setPosition(0);
        goto L6
    }

    @RequiresNonNull({"extractorOutput"})
    private boolean readFlvHeader(ExtractorInput r10) throws IOException {
        boolean r1 = false;
        if (r10.readFully(this.headerBuffer.getData(), 0, 9, true) == true) goto L5;
        return false;
    L5:
        this.headerBuffer.setPosition(0);
        this.headerBuffer.skipBytes(4);
        int r0 = this.headerBuffer.readUnsignedByte();
        if ((r0 & 4) == 0) goto L8;
        boolean r5 = true;
    L10:
        if ((r0 & 1) == 0) goto L12;
        r1 = true;
    L12:
        if (r5 == false) goto L17;
        if (this.audioReader != null) goto L17;
        this.audioReader = new AudioTagPayloadReader(this.extractorOutput.track(8, 1));
    L17:
        if (r1 == true) goto L19;
    L21:
        this.extractorOutput.endTracks();
        this.bytesToNextTagHeader = (this.headerBuffer.readInt() - 9) + 4;
        this.state = 2;
        return true;
    L19:
        if (this.videoReader != null) goto L21;
        this.videoReader = new VideoTagPayloadReader(this.extractorOutput.track(9, 2));
        goto L21
    L8:
        r5 = false;
        goto L10
    }

    @RequiresNonNull({"extractorOutput"})
    private boolean readTagData(ExtractorInput r14) throws IOException {
        boolean r0 = true;
        boolean r1 = false;
        long r2 = getCurrentTimestampUs();
        if (this.tagType != 8) goto L8;
        if (this.audioReader == null) goto L8;
        ensureReadyForMediaOutput();
        r1 = this.audioReader.consume(prepareTagData(r14), r2);
    L22:
        if (this.outputFirstSample == true) goto L29;
        if (r1 == false) goto L29;
        this.outputFirstSample = true;
        if (this.metadataReader.getDurationUs() != C0180C.TIME_UNSET) goto L27;
        long r4 = -this.tagTimestampUs;
    L28:
        this.mediaTagTimestampOffsetUs = r4;
        goto L29
    L27:
        r4 = 0;
    L29:
        this.bytesToNextTagHeader = 4;
        this.state = 2;
        return r0;
    L8:
        if (this.tagType != 9) goto L13;
        if (this.videoReader == null) goto L13;
        ensureReadyForMediaOutput();
        r1 = this.videoReader.consume(prepareTagData(r14), r2);
    L13:
        if (this.tagType == 18) goto L15;
    L20:
        r14.skipFully(this.tagDataSize);
        r0 = false;
        goto L22
    L15:
        if (this.outputSeekMap == true) goto L20;
        r1 = this.metadataReader.consume(prepareTagData(r14), r2);
        long r5 = this.metadataReader.getDurationUs();
        if (r5 == C0180C.TIME_UNSET) goto L22;
        this.extractorOutput.seekMap(new IndexSeekMap(this.metadataReader.getKeyFrameTagPositions(), this.metadataReader.getKeyFrameTimesUs(), r5));
        this.outputSeekMap = true;
        goto L22
    }

    private boolean readTagHeader(ExtractorInput r7) throws IOException {
        if (r7.readFully(this.tagHeaderBuffer.getData(), 0, 11, true) == true) goto L5;
        return false;
    L5:
        this.tagHeaderBuffer.setPosition(0);
        this.tagType = this.tagHeaderBuffer.readUnsignedByte();
        this.tagDataSize = this.tagHeaderBuffer.readUnsignedInt24();
        this.tagTimestampUs = this.tagHeaderBuffer.readUnsignedInt24();
        this.tagTimestampUs = (((long) (this.tagHeaderBuffer.readUnsignedByte() << 24)) | this.tagTimestampUs) * 1000;
        this.tagHeaderBuffer.skipBytes(3);
        this.state = 4;
        return true;
    }

    private void skipToTagHeader(ExtractorInput r2) throws IOException {
        r2.skipFully(this.bytesToNextTagHeader);
        this.bytesToNextTagHeader = 0;
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
    public void init(ExtractorOutput r1) {
        this.extractorOutput = r1;
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r3, PositionHolder r4) throws IOException {
        Assertions.checkStateNotNull(this.extractorOutput);
    L4:
        switch(this.state) {
            case 1: goto L16;
            case 2: goto L14;
            case 3: goto L12;
            case 4: goto L8;
            default: goto L6;
        };
    L6:
        throw new IllegalStateException();
    L12:
        if (readTagHeader(r3) == true) goto L4;
        return -1;
    L14:
        skipToTagHeader(r3);
        goto L4
    L16:
        if (readFlvHeader(r3) == true) goto L4;
        return -1;
    L8:
        if (readTagData(r3) == false) goto L4;
        return 0;
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r3, long r5) {
        if (r3 != 0) goto L5;
        this.state = 1;
        this.outputFirstSample = false;
    L6:
        this.bytesToNextTagHeader = 0;
        return;
    L5:
        this.state = 3;
        goto L6
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r5) throws IOException {
        r5.peekFully(this.scratch.getData(), 0, 3);
        this.scratch.setPosition(0);
        if (this.scratch.readUnsignedInt24() == FLV_TAG) goto L5;
        return false;
    L5:
        r5.peekFully(this.scratch.getData(), 0, 2);
        this.scratch.setPosition(0);
        if ((this.scratch.readUnsignedShort() & ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) == 0) goto L8;
        return false;
    L8:
        r5.peekFully(this.scratch.getData(), 0, 4);
        this.scratch.setPosition(0);
        int r0 = this.scratch.readInt();
        r5.resetPeekPosition();
        r5.advancePeekPosition(r0);
        r5.peekFully(this.scratch.getData(), 0, 4);
        this.scratch.setPosition(0);
        if (this.scratch.readInt() != 0) goto L12;
        return true;
    L12:
        return false;
    }
}
