package androidx.media3.extractor.metadata.scte35;

import androidx.media3.common.Metadata;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.extractor.metadata.MetadataInputBuffer;
import androidx.media3.extractor.metadata.SimpleMetadataDecoder;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public final class SpliceInfoDecoder extends SimpleMetadataDecoder {
    private static final int TYPE_PRIVATE_COMMAND = 255;
    private static final int TYPE_SPLICE_INSERT = 5;
    private static final int TYPE_SPLICE_NULL = 0;
    private static final int TYPE_SPLICE_SCHEDULE = 4;
    private static final int TYPE_TIME_SIGNAL = 6;
    private final ParsableByteArray sectionData;
    private final ParsableBitArray sectionHeader;
    private TimestampAdjuster timestampAdjuster;

    public SpliceInfoDecoder() {
        this.sectionData = new ParsableByteArray();
        this.sectionHeader = new ParsableBitArray();
    }

    @Override // androidx.media3.extractor.metadata.SimpleMetadataDecoder
    protected Metadata decode(MetadataInputBuffer r11, ByteBuffer r12) {
        if (this.timestampAdjuster != null) goto L5;
    L6:
        this.timestampAdjuster = new TimestampAdjuster(r11.timeUs);
        this.timestampAdjuster.adjustSampleTimestamp(r11.timeUs - r11.subsampleOffsetUs);
    L7:
        byte[] r0 = r12.array();
        int r1 = r12.limit();
        this.sectionData.reset(r0, r1);
        this.sectionHeader.reset(r0, r1);
        this.sectionHeader.skipBits(39);
        long r4 = (((long) this.sectionHeader.readBits(1)) << 32) | ((long) this.sectionHeader.readBits(32));
        this.sectionHeader.skipBits(20);
        int r2 = this.sectionHeader.readBits(12);
        int r6 = this.sectionHeader.readBits(8);
        Metadata.Entry r7 = null;
        this.sectionData.skipBytes(14);
        switch(r6) {
            case 0: goto L14;
            case 4: goto L13;
            case 5: goto L12;
            case 6: goto L11;
            case 255: goto L10;
            default: goto L16;
        };
    L10:
        r7 = PrivateCommand.parseFromSection(this.sectionData, r2, r4);
        goto L16
    L11:
        r7 = TimeSignalCommand.parseFromSection(this.sectionData, r4, this.timestampAdjuster);
        goto L16
    L12:
        r7 = SpliceInsertCommand.parseFromSection(this.sectionData, r4, this.timestampAdjuster);
        goto L16
    L13:
        r7 = SpliceScheduleCommand.parseFromSection(this.sectionData);
        goto L16
    L14:
        r7 = new SpliceNullCommand();
    L16:
        if (r7 != null) goto L19;
        return new Metadata(new Metadata.Entry[0]);
    L19:
        return new Metadata(new Metadata.Entry[]{r7});
    L5:
        if (r11.subsampleOffsetUs == this.timestampAdjuster.getTimestampOffsetUs()) goto L7;
        goto L6
    }
}
