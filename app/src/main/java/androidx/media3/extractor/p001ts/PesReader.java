package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.extractor.ExtractorOutput;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class PesReader implements TsPayloadReader {
    private static final int HEADER_SIZE = 9;
    private static final int MAX_HEADER_EXTENSION_SIZE = 10;
    private static final int PES_SCRATCH_SIZE = 10;
    private static final int STATE_FINDING_HEADER = 0;
    private static final int STATE_READING_BODY = 3;
    private static final int STATE_READING_HEADER = 1;
    private static final int STATE_READING_HEADER_EXTENSION = 2;
    private static final String TAG = "PesReader";
    private int bytesRead;
    private boolean dataAlignmentIndicator;
    private boolean dtsFlag;
    private int extendedHeaderLength;
    private int payloadSize;
    private final ParsableBitArray pesScratch;
    private boolean ptsFlag;
    private final ElementaryStreamReader reader;
    private boolean seenFirstDts;
    private int state;
    private long timeUs;
    private TimestampAdjuster timestampAdjuster;

    public PesReader(ElementaryStreamReader r3) {
        this.reader = r3;
        this.pesScratch = new ParsableBitArray(new byte[10]);
        this.state = 0;
    }

    private boolean continueRead(ParsableByteArray r4, byte[] r5, int r6) {
        int r0 = Math.min(r4.bytesLeft(), r6 - this.bytesRead);
        if (r0 > 0) goto L5;
        return true;
    L5:
        if (r5 != null) goto L7;
        r4.skipBytes(r0);
    L8:
        this.bytesRead += r0;
        if (this.bytesRead == r6) goto L13;
        return false;
    L13:
        return true;
    L7:
        r4.readBytes(r5, this.bytesRead, r0);
        goto L8
    }

    private boolean parseHeader() {
        this.pesScratch.setPosition(0);
        int r0 = this.pesScratch.readBits(24);
        if (r0 == 1) goto L6;
        Log.m48w(TAG, "Unexpected start code prefix: " + r0);
        this.payloadSize = -1;
        return false;
    L6:
        this.pesScratch.skipBits(8);
        int r1 = this.pesScratch.readBits(16);
        this.pesScratch.skipBits(5);
        this.dataAlignmentIndicator = this.pesScratch.readBit();
        this.pesScratch.skipBits(2);
        this.ptsFlag = this.pesScratch.readBit();
        this.dtsFlag = this.pesScratch.readBit();
        this.pesScratch.skipBits(6);
        this.extendedHeaderLength = this.pesScratch.readBits(8);
        if (r1 != 0) goto L9;
        this.payloadSize = -1;
    L12:
        return true;
    L9:
        this.payloadSize = ((r1 + 6) - 9) - this.extendedHeaderLength;
        if (this.payloadSize >= 0) goto L12;
        Log.m48w(TAG, "Found negative packet payload size: " + this.payloadSize);
        this.payloadSize = -1;
        goto L12
    }

    @RequiresNonNull({"timestampAdjuster"})
    private void parseHeaderExtension() {
        this.pesScratch.setPosition(0);
        this.timeUs = C0180C.TIME_UNSET;
        if (this.ptsFlag == false) goto L11;
        this.pesScratch.skipBits(4);
        long r3 = ((long) this.pesScratch.readBits(3)) << 30;
        this.pesScratch.skipBits(1);
        long r4 = r3 | ((long) (this.pesScratch.readBits(15) << 15));
        this.pesScratch.skipBits(1);
        long r5 = r4 | ((long) this.pesScratch.readBits(15));
        this.pesScratch.skipBits(1);
        if (this.seenFirstDts == false) goto L7;
    L9:
        this.timeUs = this.timestampAdjuster.adjustTsTimestamp(r5);
        return;
    L7:
        if (this.dtsFlag == false) goto L9;
        this.pesScratch.skipBits(4);
        long r0 = ((long) this.pesScratch.readBits(3)) << 30;
        this.pesScratch.skipBits(1);
        long r1 = r0 | ((long) (this.pesScratch.readBits(15) << 15));
        this.pesScratch.skipBits(1);
        long r2 = r1 | ((long) this.pesScratch.readBits(15));
        this.pesScratch.skipBits(1);
        this.timestampAdjuster.adjustTsTimestamp(r2);
        this.seenFirstDts = true;
        goto L9
    }

    private void setState(int r2) {
        this.state = r2;
        this.bytesRead = 0;
    }

    public boolean canConsumeSynthesizedEmptyPusi(boolean r3) {
        if (this.state == 3) goto L5;
    L10:
        return false;
    L5:
        if (this.payloadSize != (-1)) goto L10;
        if (r3 == true) goto L8;
    L9:
        return true;
    L8:
        if ((this.reader instanceof H262Reader) == true) goto L10;
        goto L9
    }

    @Override // androidx.media3.extractor.p001ts.TsPayloadReader
    public void consume(ParsableByteArray r8, int r9) throws ParserException {
        Assertions.checkStateNotNull(this.timestampAdjuster);
        if ((r9 & 1) == 0) goto L20;
        switch(this.state) {
            case 0: goto L18;
            case 1: goto L18;
            case 2: goto L16;
            case 3: goto L9;
            default: goto L7;
        };
    L16:
        Log.m48w(TAG, "Unexpected start indicator reading extended header");
    L18:
        setState(1);
        goto L20
    L7:
        throw new IllegalStateException();
    L9:
        if (this.payloadSize == (-1)) goto L12;
        Log.m48w(TAG, "Unexpected start indicator: expected " + this.payloadSize + " more bytes");
    L12:
        if (r8.limit() != 0) goto L14;
        boolean r0 = true;
    L15:
        this.reader.packetFinished(r0);
        goto L18
    L14:
        r0 = false;
    L20:
        if (r8.bytesLeft() <= 0) goto L53;
        switch(this.state) {
            case 0: goto L52;
            case 1: goto L46;
            case 2: goto L37;
            case 3: goto L25;
            default: goto L24;
        };
    L24:
        throw new IllegalStateException();
    L37:
        if (continueRead(r8, this.pesScratch.data, Math.min(10, this.extendedHeaderLength)) == false) goto L20;
        if (continueRead(r8, null, this.extendedHeaderLength) == false) goto L20;
        parseHeaderExtension();
        if (this.dataAlignmentIndicator == false) goto L43;
        int r4 = 4;
    L44:
        r9 = r9 | r4;
        this.reader.packetStarted(this.timeUs, r9);
        setState(3);
        goto L20
    L43:
        r4 = 0;
        goto L44
    L46:
        if (continueRead(r8, this.pesScratch.data, 9) == false) goto L20;
        if (parseHeader() == false) goto L50;
        int r1 = 2;
    L51:
        setState(r1);
        goto L20
    L50:
        r1 = 0;
        goto L51
    L52:
        r8.skipBytes(r8.bytesLeft());
        goto L20
    L25:
        int r2 = r8.bytesLeft();
        if (this.payloadSize != (-1)) goto L28;
        int r5 = 0;
    L29:
        if (r5 <= 0) goto L31;
        r2 = r2 - r5;
        r8.setLimit(r8.getPosition() + r2);
    L31:
        this.reader.consume(r8);
        if (this.payloadSize == (-1)) goto L20;
        this.payloadSize -= r2;
        if (this.payloadSize != 0) goto L20;
        this.reader.packetFinished(false);
        setState(1);
        goto L20
    L28:
        r5 = r2 - this.payloadSize;
        goto L29
    }

    @Override // androidx.media3.extractor.p001ts.TsPayloadReader
    public void init(TimestampAdjuster r2, ExtractorOutput r3, TsPayloadReader.TrackIdGenerator r4) {
        this.timestampAdjuster = r2;
        this.reader.createTracks(r3, r4);
    }

    @Override // androidx.media3.extractor.p001ts.TsPayloadReader
    public void seek() {
        this.state = 0;
        this.bytesRead = 0;
        this.seenFirstDts = false;
        this.reader.seek();
    }
}
