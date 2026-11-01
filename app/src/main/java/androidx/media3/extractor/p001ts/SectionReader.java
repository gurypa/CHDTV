package androidx.media3.extractor.p001ts;

import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.ExtractorOutput;

/* JADX INFO: loaded from: classes.dex */
public final class SectionReader implements TsPayloadReader {
    private static final int DEFAULT_SECTION_BUFFER_LENGTH = 32;
    private static final int MAX_SECTION_LENGTH = 4098;
    private static final int SECTION_HEADER_LENGTH = 3;
    private int bytesRead;
    private final SectionPayloadReader reader;
    private final ParsableByteArray sectionData;
    private boolean sectionSyntaxIndicator;
    private int totalSectionLength;
    private boolean waitingForPayloadStart;

    public SectionReader(SectionPayloadReader r3) {
        this.reader = r3;
        this.sectionData = new ParsableByteArray(32);
    }

    @Override // androidx.media3.extractor.p001ts.TsPayloadReader
    public void consume(ParsableByteArray r10, int r11) {
        if ((r11 & 1) == 0) goto L5;
        boolean r0 = true;
    L6:
        int r3 = -1;
        if (r0 == false) goto L10;
        r3 = r10.getPosition() + r10.readUnsignedByte();
    L10:
        if (this.waitingForPayloadStart == false) goto L15;
        if (r0 == true) goto L13;
        return;
    L13:
        this.waitingForPayloadStart = false;
        r10.setPosition(r3);
        this.bytesRead = 0;
    L15:
        if (r10.bytesLeft() <= 0) goto L46;
        if (this.bytesRead < 3) goto L19;
        int r4 = Math.min(r10.bytesLeft(), this.totalSectionLength - this.bytesRead);
        r10.readBytes(this.sectionData.getData(), this.bytesRead, r4);
        this.bytesRead += r4;
        if (this.bytesRead != this.totalSectionLength) goto L15;
        if (this.sectionSyntaxIndicator == true) goto L39;
        this.sectionData.setLimit(this.totalSectionLength);
    L44:
        this.sectionData.setPosition(0);
        this.reader.consume(this.sectionData);
        this.bytesRead = 0;
        goto L15
    L39:
        if (Util.crc32(this.sectionData.getData(), 0, this.totalSectionLength, -1) != 0) goto L40;
        this.sectionData.setLimit(this.totalSectionLength - 4);
        goto L44
    L40:
        this.waitingForPayloadStart = true;
        return;
    L19:
        if (this.bytesRead != 0) goto L24;
        int r5 = r10.readUnsignedByte();
        r10.setPosition(r10.getPosition() - 1);
        if (r5 != 255) goto L24;
        this.waitingForPayloadStart = true;
        return;
    L24:
        int r6 = Math.min(r10.bytesLeft(), 3 - this.bytesRead);
        r10.readBytes(this.sectionData.getData(), this.bytesRead, r6);
        this.bytesRead += r6;
        if (this.bytesRead != 3) goto L15;
        this.sectionData.setPosition(0);
        this.sectionData.setLimit(3);
        this.sectionData.skipBytes(1);
        int r7 = this.sectionData.readUnsignedByte();
        int r8 = this.sectionData.readUnsignedByte();
        if ((r7 & 128) == 0) goto L29;
        boolean r9 = true;
    L30:
        this.sectionSyntaxIndicator = r9;
        this.totalSectionLength = (((r7 & 15) << 8) | r8) + 3;
        if (this.sectionData.capacity() >= this.totalSectionLength) goto L15;
        this.sectionData.ensureCapacity(Math.min(4098, Math.max(this.totalSectionLength, this.sectionData.capacity() * 2)));
        goto L15
    L29:
        r9 = false;
        goto L30
    L46:
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.extractor.p001ts.TsPayloadReader
    public void init(TimestampAdjuster r2, ExtractorOutput r3, TsPayloadReader.TrackIdGenerator r4) {
        this.reader.init(r2, r3, r4);
        this.waitingForPayloadStart = true;
    }

    @Override // androidx.media3.extractor.p001ts.TsPayloadReader
    public void seek() {
        this.waitingForPayloadStart = true;
    }
}
