package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.Ac4Util;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class Ac4Reader implements ElementaryStreamReader {
    private static final int STATE_FINDING_SYNC = 0;
    private static final int STATE_READING_HEADER = 1;
    private static final int STATE_READING_SAMPLE = 2;
    private int bytesRead;
    private Format format;
    private String formatId;
    private boolean hasCRC;
    private final ParsableBitArray headerScratchBits;
    private final ParsableByteArray headerScratchBytes;
    private final String language;
    private boolean lastByteWasAC;
    private TrackOutput output;
    private final int roleFlags;
    private long sampleDurationUs;
    private int sampleSize;
    private int state;
    private long timeUs;

    public Ac4Reader() {
        this(null, 0);
    }

    public Ac4Reader(String r3, int r4) {
        this.headerScratchBits = new ParsableBitArray(new byte[16]);
        this.headerScratchBytes = new ParsableByteArray(this.headerScratchBits.data);
        this.state = 0;
        this.bytesRead = 0;
        this.lastByteWasAC = false;
        this.hasCRC = false;
        this.timeUs = C0180C.TIME_UNSET;
        this.language = r3;
        this.roleFlags = r4;
    }

    private boolean continueRead(ParsableByteArray r3, byte[] r4, int r5) {
        int r0 = Math.min(r3.bytesLeft(), r5 - this.bytesRead);
        r3.readBytes(r4, this.bytesRead, r0);
        this.bytesRead += r0;
        if (this.bytesRead != r5) goto L5;
        return true;
    L5:
        return false;
    }

    @RequiresNonNull({"output"})
    private void parseHeader() {
        this.headerScratchBits.setPosition(0);
        Ac4Util.SyncFrameInfo r0 = Ac4Util.parseAc4SyncframeInfo(this.headerScratchBits);
        if (this.format != null) goto L5;
    L10:
        this.format = new Format.Builder().setId(this.formatId).setSampleMimeType(MimeTypes.AUDIO_AC4).setChannelCount(r0.channelCount).setSampleRate(r0.sampleRate).setLanguage(this.language).setRoleFlags(this.roleFlags).build();
        this.output.format(this.format);
    L11:
        this.sampleSize = r0.frameSize;
        this.sampleDurationUs = (((long) r0.sampleCount) * 1000000) / ((long) this.format.sampleRate);
        return;
    L5:
        if (r0.channelCount != this.format.channelCount) goto L10;
        if (r0.sampleRate != this.format.sampleRate) goto L10;
        if (MimeTypes.AUDIO_AC4.equals(this.format.sampleMimeType) == true) goto L11;
        goto L10
    }

    private boolean skipToNextSync(ParsableByteArray r6) {
    L2:
        boolean r1 = false;
        if (r6.bytesLeft() <= 0) goto L23;
        if (this.lastByteWasAC == false) goto L7;
        int r0 = r6.readUnsignedByte();
        if (r0 != 172) goto L13;
        boolean r2 = true;
    L14:
        this.lastByteWasAC = r2;
        if (r0 == 64) goto L19;
        if (r0 != 65) goto L2;
    L19:
        if (r0 != 65) goto L21;
        r1 = true;
    L21:
        this.hasCRC = r1;
        return true;
    L13:
        r2 = false;
        goto L14
    L7:
        if (r6.readUnsignedByte() != 172) goto L9;
        r1 = true;
    L9:
        this.lastByteWasAC = r1;
        goto L2
    L23:
        return false;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void consume(ParsableByteArray r12) {
        Assertions.checkStateNotNull(this.output);
    L4:
        if (r12.bytesLeft() <= 0) goto L25;
        boolean r2 = true;
        switch(this.state) {
            case 0: goto L19;
            case 1: goto L16;
            case 2: goto L8;
            default: goto L4;
        };
    L8:
        int r0 = Math.min(r12.bytesLeft(), this.sampleSize - this.bytesRead);
        this.output.sampleData(r12, r0);
        this.bytesRead += r0;
        if (this.bytesRead != this.sampleSize) goto L4;
        if (this.timeUs != C0180C.TIME_UNSET) goto L14;
        r2 = false;
    L14:
        Assertions.checkState(r2);
        this.output.sampleMetadata(this.timeUs, 1, this.sampleSize, 0, null);
        this.timeUs += this.sampleDurationUs;
        this.state = 0;
        goto L4
    L16:
        if (continueRead(r12, this.headerScratchBytes.getData(), 16) == false) goto L4;
        parseHeader();
        this.headerScratchBytes.setPosition(0);
        this.output.sampleData(this.headerScratchBytes, 16);
        this.state = 2;
        goto L4
    L19:
        if (skipToNextSync(r12) == false) goto L4;
        this.state = 1;
        this.headerScratchBytes.getData()[0] = -84;
        byte[] r1 = this.headerScratchBytes.getData();
        if (this.hasCRC == false) goto L23;
        int r3 = 65;
    L24:
        r1[1] = (byte) r3;
        this.bytesRead = 2;
        goto L4
    L23:
        r3 = 64;
        goto L24
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
    public void packetStarted(long r1, int r3) {
        this.timeUs = r1;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void seek() {
        this.state = 0;
        this.bytesRead = 0;
        this.lastByteWasAC = false;
        this.hasCRC = false;
        this.timeUs = C0180C.TIME_UNSET;
    }
}
