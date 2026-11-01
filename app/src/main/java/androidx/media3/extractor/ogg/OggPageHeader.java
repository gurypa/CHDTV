package androidx.media3.extractor.ogg;

import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorUtil;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
final class OggPageHeader {
    private static final int CAPTURE_PATTERN = 1332176723;
    private static final int CAPTURE_PATTERN_SIZE = 4;
    public static final int EMPTY_PAGE_HEADER_SIZE = 27;
    public static final int MAX_PAGE_PAYLOAD = 65025;
    public static final int MAX_PAGE_SIZE = 65307;
    public static final int MAX_SEGMENT_COUNT = 255;
    public int bodySize;
    public long granulePosition;
    public int headerSize;
    public final int[] laces;
    public long pageChecksum;
    public int pageSegmentCount;
    public long pageSequenceNumber;
    public int revision;
    private final ParsableByteArray scratch;
    public long streamSerialNumber;
    public int type;

    OggPageHeader() {
        this.laces = new int[255];
        this.scratch = new ParsableByteArray(255);
    }

    public boolean populate(ExtractorInput r8, boolean r9) throws IOException {
        reset();
        this.scratch.reset(27);
        if (ExtractorUtil.peekFullyQuietly(r8, this.scratch.getData(), 0, 27, r9) == true) goto L5;
    L22:
        return false;
    L5:
        if (this.scratch.readUnsignedInt() != 1332176723) goto L22;
        this.revision = this.scratch.readUnsignedByte();
        if (this.revision == 0) goto L13;
        if (r9 == false) goto L12;
        return false;
    L12:
        throw ParserException.createForUnsupportedContainerFeature("unsupported bit stream revision");
    L13:
        this.type = this.scratch.readUnsignedByte();
        this.granulePosition = this.scratch.readLittleEndianLong();
        this.streamSerialNumber = this.scratch.readLittleEndianUnsignedInt();
        this.pageSequenceNumber = this.scratch.readLittleEndianUnsignedInt();
        this.pageChecksum = this.scratch.readLittleEndianUnsignedInt();
        this.pageSegmentCount = this.scratch.readUnsignedByte();
        this.headerSize = this.pageSegmentCount + 27;
        this.scratch.reset(this.pageSegmentCount);
        if (ExtractorUtil.peekFullyQuietly(r8, this.scratch.getData(), 0, this.pageSegmentCount, r9) == true) goto L16;
        return false;
    L16:
        int r0 = 0;
    L18:
        if (r0 >= this.pageSegmentCount) goto L20;
        this.laces[r0] = this.scratch.readUnsignedByte();
        this.bodySize += this.laces[r0];
        r0 = r0 + 1;
        goto L18
    L20:
        return true;
    }

    public void reset() {
        this.revision = 0;
        this.type = 0;
        this.granulePosition = 0;
        this.streamSerialNumber = 0;
        this.pageSequenceNumber = 0;
        this.pageChecksum = 0;
        this.pageSegmentCount = 0;
        this.headerSize = 0;
        this.bodySize = 0;
    }

    public boolean skipToNextPage(ExtractorInput r3) throws IOException {
        return skipToNextPage(r3, -1);
    }

    public boolean skipToNextPage(ExtractorInput r11, long r12) throws IOException {
        if (r11.getPosition() != r11.getPeekPosition()) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        this.scratch.reset(4);
    L8:
        if (r12 == (-1)) goto L12;
        if ((r11.getPosition() + 4) < r12) goto L12;
    L19:
        if (r12 == (-1)) goto L23;
        if (r11.getPosition() < r12) goto L23;
    L25:
        return false;
    L23:
        if (r11.skip(1) == (-1)) goto L25;
    L12:
        if (ExtractorUtil.peekFullyQuietly(r11, this.scratch.getData(), 0, 4, true) == false) goto L19;
        this.scratch.setPosition(0);
        if (this.scratch.readUnsignedInt() == 1332176723) goto L15;
        r11.skipFully(1);
        goto L8
    L15:
        r11.resetPeekPosition();
        return true;
    L5:
        r0 = false;
        goto L6
    }
}
