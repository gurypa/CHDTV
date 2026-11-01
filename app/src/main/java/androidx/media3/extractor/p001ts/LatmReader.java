package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.AacUtil;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import java.util.Collections;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class LatmReader implements ElementaryStreamReader {
    private static final int INITIAL_BUFFER_SIZE = 1024;
    private static final int STATE_FINDING_SYNC_1 = 0;
    private static final int STATE_FINDING_SYNC_2 = 1;
    private static final int STATE_READING_HEADER = 2;
    private static final int STATE_READING_SAMPLE = 3;
    private static final int SYNC_BYTE_FIRST = 86;
    private static final int SYNC_BYTE_SECOND = 224;
    private int audioMuxVersionA;
    private int bytesRead;
    private int channelCount;
    private String codecs;
    private Format format;
    private String formatId;
    private int frameLengthType;
    private final String language;
    private int numSubframes;
    private long otherDataLenBits;
    private boolean otherDataPresent;
    private TrackOutput output;
    private final int roleFlags;
    private final ParsableBitArray sampleBitArray;
    private final ParsableByteArray sampleDataBuffer;
    private long sampleDurationUs;
    private int sampleRateHz;
    private int sampleSize;
    private int secondHeaderByte;
    private int state;
    private boolean streamMuxRead;
    private long timeUs;

    public LatmReader(String r3, int r4) {
        this.language = r3;
        this.roleFlags = r4;
        this.sampleDataBuffer = new ParsableByteArray(1024);
        this.sampleBitArray = new ParsableBitArray(this.sampleDataBuffer.getData());
        this.timeUs = C0180C.TIME_UNSET;
    }

    private static long latmGetValue(ParsableBitArray r3) {
        return r3.readBits((r3.readBits(2) + 1) * 8);
    }

    @RequiresNonNull({"output"})
    private void parseAudioMuxElement(ParsableBitArray r5) throws ParserException {
        if (r5.readBit() == true) goto L6;
        this.streamMuxRead = true;
        parseStreamMuxConfig(r5);
    L9:
        if (this.audioMuxVersionA != 0) goto L20;
        if (this.numSubframes != 0) goto L18;
        parsePayloadMux(r5, parsePayloadLengthInfo(r5));
        if (this.otherDataPresent == false) goto L21;
        r5.skipBits((int) this.otherDataLenBits);
        return;
    L21:
        return;
    L18:
        throw ParserException.createForMalformedContainer(null, null);
    L20:
        throw ParserException.createForMalformedContainer(null, null);
    L6:
        if (this.streamMuxRead == true) goto L9;
    }

    private int parseAudioSpecificConfig(ParsableBitArray r4) throws ParserException {
        int r0 = r4.bitsLeft();
        AacUtil.Config r1 = AacUtil.parseAudioSpecificConfig(r4, true);
        this.codecs = r1.codecs;
        this.sampleRateHz = r1.sampleRateHz;
        this.channelCount = r1.channelCount;
        return r0 - r4.bitsLeft();
    }

    private void parseFrameLength(ParsableBitArray r2) {
        this.frameLengthType = r2.readBits(3);
        switch(this.frameLengthType) {
            case 0: goto L9;
            case 1: goto L8;
            case 2: goto L5;
            case 3: goto L7;
            case 4: goto L7;
            case 5: goto L7;
            case 6: goto L6;
            case 7: goto L6;
            default: goto L5;
        };
    L6:
        r2.skipBits(1);
        return;
    L7:
        r2.skipBits(6);
        return;
    L8:
        r2.skipBits(9);
        return;
    L9:
        r2.skipBits(8);
        return;
    L5:
        throw new IllegalStateException();
    }

    private int parsePayloadLengthInfo(ParsableBitArray r4) throws ParserException {
        int r0 = 0;
        if (this.frameLengthType != 0) goto L8;
    L4:
        int r1 = r4.readBits(8);
        r0 = r0 + r1;
        if (r1 == 255) goto L4;
        return r0;
    L8:
        throw ParserException.createForMalformedContainer(null, null);
    }

    @RequiresNonNull({"output"})
    private void parsePayloadMux(ParsableBitArray r11, int r12) {
        int r0 = r11.getPosition();
        boolean r2 = false;
        if ((r0 & 7) != 0) goto L5;
        this.sampleDataBuffer.setPosition(r0 >> 3);
    L6:
        this.output.sampleData(this.sampleDataBuffer, r12);
        if (this.timeUs == C0180C.TIME_UNSET) goto L9;
        r2 = true;
    L9:
        Assertions.checkState(r2);
        this.output.sampleMetadata(this.timeUs, 1, r12, 0, null);
        this.timeUs += this.sampleDurationUs;
        return;
    L5:
        r11.readBits(this.sampleDataBuffer.getData(), 0, r12 * 8);
        this.sampleDataBuffer.setPosition(0);
        goto L6
    }

    @RequiresNonNull({"output"})
    private void parseStreamMuxConfig(ParsableBitArray r14) throws ParserException {
        int r1 = r14.readBits(1);
        if (r1 != 1) goto L5;
        int r3 = r14.readBits(1);
    L6:
        this.audioMuxVersionA = r3;
        if (this.audioMuxVersionA != 0) goto L38;
        if (r1 != 1) goto L11;
        latmGetValue(r14);
    L11:
        if (r14.readBit() == false) goto L36;
        this.numSubframes = r14.readBits(6);
        int r4 = r14.readBits(4);
        int r5 = r14.readBits(3);
        if (r4 != 0) goto L34;
        if (r5 != 0) goto L34;
        if (r1 != 0) goto L21;
        int r6 = r14.getPosition();
        int r7 = parseAudioSpecificConfig(r14);
        r14.setPosition(r6);
        byte[] r8 = new byte[(r7 + 7) / 8];
        r14.readBits(r8, 0, r7);
        Format r2 = new Format.Builder().setId(this.formatId).setSampleMimeType(MimeTypes.AUDIO_AAC).setCodecs(this.codecs).setChannelCount(this.channelCount).setSampleRate(this.sampleRateHz).setInitializationData(Collections.singletonList(r8)).setLanguage(this.language).setRoleFlags(this.roleFlags).build();
        if (r2.equals(this.format) == true) goto L22;
        this.format = r2;
        this.sampleDurationUs = 1024000000 / ((long) r2.sampleRate);
        this.output.format(r2);
    L22:
        parseFrameLength(r14);
        this.otherDataPresent = r14.readBit();
        this.otherDataLenBits = 0;
        if (this.otherDataPresent == false) goto L29;
        if (r1 != 1) goto L26;
        this.otherDataLenBits = latmGetValue(r14);
    L26:
        boolean r0 = r14.readBit();
        this.otherDataLenBits = (this.otherDataLenBits << 8) + ((long) r14.readBits(8));
        if (r0 == true) goto L26;
    L29:
        if (r14.readBit() == false) goto L41;
        r14.skipBits(8);
        return;
    L41:
        return;
    L21:
        r14.skipBits(((int) latmGetValue(r14)) - parseAudioSpecificConfig(r14));
    L34:
        throw ParserException.createForMalformedContainer(null, null);
    L36:
        throw ParserException.createForMalformedContainer(null, null);
    L38:
        throw ParserException.createForMalformedContainer(null, null);
    L5:
        r3 = 0;
        goto L6
    }

    private void resetBufferForSize(int r3) {
        this.sampleDataBuffer.reset(r3);
        this.sampleBitArray.reset(this.sampleDataBuffer.getData());
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void consume(ParsableByteArray r6) throws ParserException {
        Assertions.checkStateNotNull(this.output);
    L4:
        if (r6.bytesLeft() <= 0) goto L24;
        switch(this.state) {
            case 0: goto L22;
            case 1: goto L16;
            case 2: goto L12;
            case 3: goto L9;
            default: goto L8;
        };
    L8:
        throw new IllegalStateException();
    L12:
        this.sampleSize = ((this.secondHeaderByte & (-225)) << 8) | r6.readUnsignedByte();
        if (this.sampleSize <= this.sampleDataBuffer.getData().length) goto L15;
        resetBufferForSize(this.sampleSize);
    L15:
        this.bytesRead = 0;
        this.state = 3;
        goto L4
    L16:
        int r0 = r6.readUnsignedByte();
        if ((r0 & 224) == 224) goto L18;
        if (r0 == SYNC_BYTE_FIRST) goto L4;
        this.state = 0;
        goto L4
    L18:
        this.secondHeaderByte = r0;
        this.state = 2;
        goto L4
    L22:
        if (r6.readUnsignedByte() != SYNC_BYTE_FIRST) goto L4;
        this.state = 1;
        goto L4
    L9:
        int r1 = Math.min(r6.bytesLeft(), this.sampleSize - this.bytesRead);
        r6.readBytes(this.sampleBitArray.data, this.bytesRead, r1);
        this.bytesRead += r1;
        if (this.bytesRead != this.sampleSize) goto L4;
        this.sampleBitArray.setPosition(0);
        parseAudioMuxElement(this.sampleBitArray);
        this.state = 0;
        goto L4
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void createTracks(ExtractorOutput r3, TsPayloadReader.TrackIdGenerator r4) {
        r4.generateNewId();
        this.output = r3.track(r4.getTrackId(), 1);
        this.formatId = r4.getFormatId();
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void packetFinished(boolean r1) {
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void packetStarted(long r1, int r3) {
        this.timeUs = r1;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void seek() {
        this.state = 0;
        this.timeUs = C0180C.TIME_UNSET;
        this.streamMuxRead = false;
    }
}
