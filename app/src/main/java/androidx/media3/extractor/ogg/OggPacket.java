package androidx.media3.extractor.ogg;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorUtil;
import java.io.IOException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
final class OggPacket {
    private int currentSegmentIndex;
    private final ParsableByteArray packetArray;
    private final OggPageHeader pageHeader;
    private boolean populated;
    private int segmentCount;

    OggPacket() {
        this.pageHeader = new OggPageHeader();
        this.packetArray = new ParsableByteArray(new byte[OggPageHeader.MAX_PAGE_PAYLOAD], 0);
        this.currentSegmentIndex = -1;
    }

    private int calculatePacketSize(int r5) {
        this.segmentCount = 0;
        int r0 = 0;
    L4:
        if ((this.segmentCount + r5) >= this.pageHeader.pageSegmentCount) goto L9;
        int[] r1 = this.pageHeader.laces;
        int r2 = this.segmentCount;
        this.segmentCount = r2 + 1;
        int r3 = r1[r2 + r5];
        r0 = r0 + r3;
        if (r3 == 255) goto L4;
    L9:
        return r0;
    }

    public OggPageHeader getPageHeader() {
        return this.pageHeader;
    }

    public ParsableByteArray getPayload() {
        return this.packetArray;
    }

    public boolean populate(ExtractorInput r7) throws IOException {
        if (r7 == null) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkState(r2);
        if (this.populated == false) goto L10;
        this.populated = false;
        this.packetArray.reset(0);
    L10:
        if (this.populated == true) goto L43;
        if (this.currentSegmentIndex < 0) goto L14;
    L28:
        int r3 = calculatePacketSize(this.currentSegmentIndex);
        int r4 = this.currentSegmentIndex + this.segmentCount;
        if (r3 <= 0) goto L39;
        this.packetArray.ensureCapacity(this.packetArray.limit() + r3);
        if (ExtractorUtil.readFullyQuietly(r7, this.packetArray.getData(), this.packetArray.limit(), r3) == false) goto L32;
        this.packetArray.setLimit(this.packetArray.limit() + r3);
        if (this.pageHeader.laces[r4 - 1] == 255) goto L36;
        boolean r5 = true;
    L37:
        this.populated = r5;
        goto L39
    L36:
        r5 = false;
        goto L37
    L32:
        return false;
    L39:
        if (r4 != this.pageHeader.pageSegmentCount) goto L41;
        int r6 = -1;
    L42:
        this.currentSegmentIndex = r6;
        goto L10
    L41:
        r6 = r4;
        goto L42
    L14:
        if (this.pageHeader.skipToNextPage(r7) == false) goto L27;
        if (this.pageHeader.populate(r7, true) == false) goto L27;
        int r8 = 0;
        int r9 = this.pageHeader.headerSize;
        if ((this.pageHeader.type & 1) != 1) goto L24;
        if (this.packetArray.limit() != 0) goto L24;
        r9 = r9 + calculatePacketSize(0);
        r8 = 0 + this.segmentCount;
    L24:
        if (ExtractorUtil.skipFullyQuietly(r7, r9) == false) goto L25;
        this.currentSegmentIndex = r8;
        goto L28
    L25:
        return false;
    L27:
        return false;
    L43:
        return true;
    L5:
        r2 = false;
        goto L6
    }

    public void reset() {
        this.pageHeader.reset();
        this.packetArray.reset(0);
        this.currentSegmentIndex = -1;
        this.populated = false;
    }

    public void trimPayload() {
        if (this.packetArray.getData().length != 65025) goto L5;
        return;
    L5:
        this.packetArray.reset(Arrays.copyOf(this.packetArray.getData(), Math.max(OggPageHeader.MAX_PAGE_PAYLOAD, this.packetArray.limit())), this.packetArray.limit());
    }
}
