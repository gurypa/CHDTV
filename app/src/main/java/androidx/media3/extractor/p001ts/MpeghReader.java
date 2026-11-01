package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import com.google.common.collect.ImmutableList;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class MpeghReader implements ElementaryStreamReader {
    private static final int MAX_MHAS_PACKET_HEADER_SIZE = 15;
    private static final int MHAS_SYNC_WORD_LENGTH = 3;
    private static final int MIN_MHAS_PACKET_HEADER_SIZE = 2;
    private static final int STATE_FINDING_SYNC = 0;
    private static final int STATE_READING_PACKET_HEADER = 1;
    private static final int STATE_READING_PACKET_PAYLOAD = 2;
    private boolean configFound;
    private boolean dataPending;
    private final ParsableByteArray dataScratchBytes;
    private int flags;
    private String formatId;
    private int frameBytes;
    private MpeghUtil.MhasPacketHeader header;
    private boolean headerDataFinished;
    private final ParsableBitArray headerScratchBits;
    private final ParsableByteArray headerScratchBytes;
    private long mainStreamLabel;
    private TrackOutput output;
    private int payloadBytesRead;
    private boolean rapPending;
    private int samplingRate;
    private int standardFrameLength;
    private int state;
    private int syncBytes;
    private double timeUs;
    private double timeUsPending;
    private int truncationSamples;

    public MpeghReader() {
        this.state = 0;
        this.headerScratchBytes = new ParsableByteArray(new byte[15], 2);
        this.headerScratchBits = new ParsableBitArray();
        this.dataScratchBytes = new ParsableByteArray();
        this.header = new MpeghUtil.MhasPacketHeader();
        this.samplingRate = C0180C.RATE_UNSET_INT;
        this.standardFrameLength = -1;
        this.mainStreamLabel = -1;
        this.rapPending = true;
        this.headerDataFinished = true;
        this.timeUs = -9.223372036854776E18d;
        this.timeUsPending = -9.223372036854776E18d;
    }

    private void copyData(ParsableByteArray r5, ParsableByteArray r6, boolean r7) {
        int r0 = r5.getPosition();
        int r1 = Math.min(r5.bytesLeft(), r6.bytesLeft());
        r5.readBytes(r6.getData(), r6.getPosition(), r1);
        r6.skipBytes(r1);
        if (r7 == false) goto L6;
        r5.setPosition(r0);
        return;
    }

    @RequiresNonNull({"output"})
    private void finalizeFrame() {
        if (this.configFound == false) goto L5;
        this.rapPending = false;
        int r6 = 1;
    L6:
        double r0 = (((double) (this.standardFrameLength - this.truncationSamples)) * 1000000.0d) / ((double) this.samplingRate);
        long r4 = Math.round(this.timeUs);
        if (this.dataPending == false) goto L9;
        this.dataPending = false;
        this.timeUs = this.timeUsPending;
    L10:
        this.output.sampleMetadata(r4, r6, this.frameBytes, 0, null);
        this.configFound = false;
        this.truncationSamples = 0;
        this.frameBytes = 0;
        return;
    L9:
        this.timeUs += r0;
        goto L10
    L5:
        r6 = 0;
        goto L6
    }

    @RequiresNonNull({"output"})
    private void parseConfig(ParsableBitArray r8) throws ParserException {
        MpeghUtil.Mpegh3daConfig r0 = MpeghUtil.parseMpegh3daConfig(r8);
        this.samplingRate = r0.samplingFrequency;
        this.standardFrameLength = r0.standardFrameLength;
        if (this.mainStreamLabel == this.header.packetLabel) goto L13;
        this.mainStreamLabel = this.header.packetLabel;
        String r1 = "mhm1";
        if (r0.profileLevelIndication == (-1)) goto L7;
        r1 = "mhm1" + String.format(".%02X", new Object[]{Integer.valueOf(r0.profileLevelIndication)});
    L7:
        ImmutableList r3 = null;
        if (r0.compatibleProfileLevelSet != null) goto L10;
    L12:
        this.output.format(new Format.Builder().setId(this.formatId).setSampleMimeType(MimeTypes.AUDIO_MPEGH_MHM1).setSampleRate(this.samplingRate).setCodecs(r1).setInitializationData(r3).build());
        goto L13
    L10:
        if (r0.compatibleProfileLevelSet.length <= 0) goto L12;
        r3 = ImmutableList.m214of(Util.EMPTY_BYTE_ARRAY, r0.compatibleProfileLevelSet);
    L13:
        this.configFound = true;
    }

    private boolean parseHeader() throws ParserException {
        int r0 = this.headerScratchBytes.limit();
        this.headerScratchBits.reset(this.headerScratchBytes.getData(), r0);
        boolean r1 = MpeghUtil.parseMhasPacketHeader(this.headerScratchBits, this.header);
        if (r1 == false) goto L5;
        this.payloadBytesRead = 0;
        this.frameBytes += this.header.packetLength + r0;
    L5:
        return r1;
    }

    private boolean shouldParsePacket(int r3) {
        if (r3 != 1) goto L5;
        return true;
    L5:
        if (r3 == 17) goto L10;
        return false;
    L10:
        return true;
    }

    private boolean skipToNextSync(ParsableByteArray r5) {
        if ((this.flags & 2) != 0) goto L7;
        r5.setPosition(r5.limit());
        return false;
    L7:
        if ((this.flags & 4) == 0) goto L9;
        return true;
    L9:
        if (r5.bytesLeft() <= 0) goto L14;
        this.syncBytes <<= 8;
        this.syncBytes |= r5.readUnsignedByte();
        if (MpeghUtil.isSyncWord(this.syncBytes) == false) goto L9;
        r5.setPosition(r5.getPosition() - 3);
        this.syncBytes = 0;
        return true;
    L14:
        return false;
    }

    @RequiresNonNull({"output"})
    private void writeSampleData(ParsableByteArray r4) {
        int r0 = Math.min(r4.bytesLeft(), this.header.packetLength - this.payloadBytesRead);
        this.output.sampleData(r4, r0);
        this.payloadBytesRead += r0;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void consume(ParsableByteArray r6) throws ParserException {
        Assertions.checkStateNotNull(this.output);
    L4:
        if (r6.bytesLeft() <= 0) goto L36;
        switch(this.state) {
            case 0: goto L34;
            case 1: goto L24;
            case 2: goto L10;
            default: goto L8;
        };
    L8:
        throw new IllegalStateException();
    L24:
        copyData(r6, this.headerScratchBytes, false);
        if (this.headerScratchBytes.bytesLeft() == 0) goto L27;
        this.headerDataFinished = false;
        goto L4
    L27:
        if (parseHeader() == true) goto L28;
        if (this.headerScratchBytes.limit() >= 15) goto L4;
        this.headerScratchBytes.setLimit(this.headerScratchBytes.limit() + 1);
        this.headerDataFinished = false;
        goto L4
    L28:
        this.headerScratchBytes.setPosition(0);
        this.output.sampleData(this.headerScratchBytes, this.headerScratchBytes.limit());
        this.headerScratchBytes.reset(2);
        this.dataScratchBytes.reset(this.header.packetLength);
        this.headerDataFinished = true;
        this.state = 2;
        goto L4
    L34:
        if (skipToNextSync(r6) == false) goto L4;
        this.state = 1;
        goto L4
    L10:
        if (shouldParsePacket(this.header.packetType) == false) goto L12;
        copyData(r6, this.dataScratchBytes, true);
    L12:
        writeSampleData(r6);
        if (this.payloadBytesRead != this.header.packetLength) goto L4;
        if (this.header.packetType != 1) goto L18;
        parseConfig(new ParsableBitArray(this.dataScratchBytes.getData()));
    L23:
        this.state = 1;
        goto L4
    L18:
        if (this.header.packetType != 17) goto L21;
        this.truncationSamples = MpeghUtil.parseAudioTruncationInfo(new ParsableBitArray(this.dataScratchBytes.getData()));
        goto L23
    L21:
        if (this.header.packetType != 2) goto L23;
        finalizeFrame();
        goto L23
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void createTracks(ExtractorOutput r3, TsPayloadReader.TrackIdGenerator r4) {
        r4.generateNewId();
        this.formatId = r4.getFormatId();
        this.output = r3.track(r4.getTrackId(), 1);
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void packetFinished(boolean r1) {
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void packetStarted(long r3, int r5) {
        this.flags = r5;
        if (this.rapPending == true) goto L10;
        if (this.frameBytes == 0) goto L7;
    L8:
        this.dataPending = true;
        goto L10
    L7:
        if (this.headerDataFinished == false) goto L8;
    L10:
        if (r3 != C0180C.TIME_UNSET) goto L12;
        return;
    L12:
        if (this.dataPending == false) goto L14;
        this.timeUsPending = r3;
        return;
    L14:
        this.timeUs = r3;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void seek() {
        this.state = 0;
        this.syncBytes = 0;
        this.headerScratchBytes.reset(2);
        this.payloadBytesRead = 0;
        this.frameBytes = 0;
        this.samplingRate = C0180C.RATE_UNSET_INT;
        this.standardFrameLength = -1;
        this.truncationSamples = 0;
        this.mainStreamLabel = -1;
        this.configFound = false;
        this.dataPending = false;
        this.headerDataFinished = true;
        this.rapPending = true;
        this.timeUs = -9.223372036854776E18d;
        this.timeUsPending = -9.223372036854776E18d;
    }
}
