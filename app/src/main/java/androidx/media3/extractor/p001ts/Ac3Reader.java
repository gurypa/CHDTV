package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.Ac3Util;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import com.google.common.base.Ascii;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class Ac3Reader implements ElementaryStreamReader {
    private static final int HEADER_SIZE = 128;
    private static final int STATE_FINDING_SYNC = 0;
    private static final int STATE_READING_HEADER = 1;
    private static final int STATE_READING_SAMPLE = 2;
    private int bytesRead;
    private Format format;
    private String formatId;
    private final ParsableBitArray headerScratchBits;
    private final ParsableByteArray headerScratchBytes;
    private final String language;
    private boolean lastByteWas0B;
    private TrackOutput output;
    private final int roleFlags;
    private long sampleDurationUs;
    private int sampleSize;
    private int state;
    private long timeUs;

    public Ac3Reader() {
        this(null, 0);
    }

    public Ac3Reader(String r3, int r4) {
        this.headerScratchBits = new ParsableBitArray(new byte[128]);
        this.headerScratchBytes = new ParsableByteArray(this.headerScratchBits.data);
        this.state = 0;
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
        Ac3Util.SyncFrameInfo r0 = Ac3Util.parseAc3SyncframeInfo(this.headerScratchBits);
        if (this.format != null) goto L5;
    L10:
        Format.Builder r1 = new Format.Builder().setId(this.formatId).setSampleMimeType(r0.mimeType).setChannelCount(r0.channelCount).setSampleRate(r0.sampleRate).setLanguage(this.language).setRoleFlags(this.roleFlags).setPeakBitrate(r0.bitrate);
        if (MimeTypes.AUDIO_AC3.equals(r0.mimeType) == false) goto L13;
        r1.setAverageBitrate(r0.bitrate);
    L13:
        this.format = r1.build();
        this.output.format(this.format);
    L14:
        this.sampleSize = r0.frameSize;
        this.sampleDurationUs = (((long) r0.sampleCount) * 1000000) / ((long) this.format.sampleRate);
        return;
    L5:
        if (r0.channelCount != this.format.channelCount) goto L10;
        if (r0.sampleRate != this.format.sampleRate) goto L10;
        if (Util.areEqual(r0.mimeType, this.format.sampleMimeType) == true) goto L14;
        goto L10
    }

    private boolean skipToNextSync(ParsableByteArray r6) {
    L2:
        boolean r1 = false;
        if (r6.bytesLeft() <= 0) goto L17;
        if (this.lastByteWas0B == false) goto L7;
        int r0 = r6.readUnsignedByte();
        if (r0 == 119) goto L12;
        if (r0 != 11) goto L16;
        r1 = true;
    L16:
        this.lastByteWas0B = r1;
        goto L2
    L12:
        this.lastByteWas0B = false;
        return true;
    L7:
        if (r6.readUnsignedByte() != 11) goto L9;
        r1 = true;
    L9:
        this.lastByteWas0B = r1;
        goto L2
    L17:
        return false;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void consume(ParsableByteArray r12) {
        Assertions.checkStateNotNull(this.output);
    L4:
        if (r12.bytesLeft() <= 0) goto L21;
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
        if (continueRead(r12, this.headerScratchBytes.getData(), 128) == false) goto L4;
        parseHeader();
        this.headerScratchBytes.setPosition(0);
        this.output.sampleData(this.headerScratchBytes, 128);
        this.state = 2;
        goto L4
    L19:
        if (skipToNextSync(r12) == false) goto L4;
        this.state = 1;
        this.headerScratchBytes.getData()[0] = Ascii.f92VT;
        this.headerScratchBytes.getData()[1] = 119;
        this.bytesRead = 2;
        goto L4
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
        this.lastByteWas0B = false;
        this.timeUs = C0180C.TIME_UNSET;
    }
}
