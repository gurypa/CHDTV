package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.MpegAudioUtil;
import androidx.media3.extractor.TrackOutput;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class MpegAudioReader implements ElementaryStreamReader {
    private static final int HEADER_SIZE = 4;
    private static final int STATE_FINDING_HEADER = 0;
    private static final int STATE_READING_FRAME = 2;
    private static final int STATE_READING_HEADER = 1;
    private String formatId;
    private int frameBytesRead;
    private long frameDurationUs;
    private int frameSize;
    private boolean hasOutputFormat;
    private final MpegAudioUtil.Header header;
    private final ParsableByteArray headerScratch;
    private final String language;
    private boolean lastByteWasFF;
    private TrackOutput output;
    private final int roleFlags;
    private int state;
    private long timeUs;

    public MpegAudioReader() {
        this(null, 0);
    }

    public MpegAudioReader(String r4, int r5) {
        this.state = 0;
        this.headerScratch = new ParsableByteArray(4);
        this.headerScratch.getData()[0] = -1;
        this.header = new MpegAudioUtil.Header();
        this.timeUs = C0180C.TIME_UNSET;
        this.language = r4;
        this.roleFlags = r5;
    }

    private void findHeader(ParsableByteArray r10) {
        byte[] r0 = r10.getData();
        int r1 = r10.getPosition();
        int r2 = r10.limit();
        int r3 = r1;
    L3:
        if (r3 >= r2) goto L19;
        if ((r0[r3] & 255) != 255) goto L7;
        boolean r4 = true;
    L9:
        if (this.lastByteWasFF == true) goto L11;
    L13:
        boolean r5 = false;
    L14:
        this.lastByteWasFF = r4;
        if (r5 == true) goto L16;
        r3 = r3 + 1;
        goto L3
    L16:
        r10.setPosition(r3 + 1);
        this.lastByteWasFF = false;
        this.headerScratch.getData()[1] = r0[r3];
        this.frameBytesRead = 2;
        this.state = 1;
        return;
    L11:
        if ((r0[r3] & 224) != 224) goto L13;
        r5 = true;
        goto L14
    L7:
        r4 = false;
        goto L9
    L19:
        r10.setPosition(r2);
    }

    @RequiresNonNull({"output"})
    private void readFrameRemainder(ParsableByteArray r11) {
        int r0 = Math.min(r11.bytesLeft(), this.frameSize - this.frameBytesRead);
        this.output.sampleData(r11, r0);
        this.frameBytesRead += r0;
        if (this.frameBytesRead >= this.frameSize) goto L6;
        return;
    L6:
        if (this.timeUs == C0180C.TIME_UNSET) goto L8;
        boolean r1 = true;
    L9:
        Assertions.checkState(r1);
        this.output.sampleMetadata(this.timeUs, 1, this.frameSize, 0, null);
        this.timeUs += this.frameDurationUs;
        this.frameBytesRead = 0;
        this.state = 0;
        return;
    L8:
        r1 = false;
        goto L9
    }

    @RequiresNonNull({"output"})
    private void readHeaderRemainder(ParsableByteArray r10) {
        int r0 = Math.min(r10.bytesLeft(), 4 - this.frameBytesRead);
        r10.readBytes(this.headerScratch.getData(), this.frameBytesRead, r0);
        this.frameBytesRead += r0;
        if (this.frameBytesRead >= 4) goto L5;
        return;
    L5:
        this.headerScratch.setPosition(0);
        if (this.header.setForHeaderData(this.headerScratch.readInt()) == true) goto L9;
        this.frameBytesRead = 0;
        this.state = 1;
        return;
    L9:
        this.frameSize = this.header.frameSize;
        if (this.hasOutputFormat == true) goto L12;
        this.frameDurationUs = (((long) this.header.samplesPerFrame) * 1000000) / ((long) this.header.sampleRate);
        this.output.format(new Format.Builder().setId(this.formatId).setSampleMimeType(this.header.mimeType).setMaxInputSize(4096).setChannelCount(this.header.channels).setSampleRate(this.header.sampleRate).setLanguage(this.language).setRoleFlags(this.roleFlags).build());
        this.hasOutputFormat = true;
    L12:
        this.headerScratch.setPosition(0);
        this.output.sampleData(this.headerScratch, 4);
        this.state = 2;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void consume(ParsableByteArray r2) {
        Assertions.checkStateNotNull(this.output);
    L4:
        if (r2.bytesLeft() <= 0) goto L12;
        switch(this.state) {
            case 0: goto L11;
            case 1: goto L10;
            case 2: goto L9;
            default: goto L8;
        };
    L8:
        throw new IllegalStateException();
    L10:
        readHeaderRemainder(r2);
        goto L4
    L11:
        findHeader(r2);
        goto L4
    L9:
        readFrameRemainder(r2);
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
        this.frameBytesRead = 0;
        this.lastByteWasFF = false;
        this.timeUs = C0180C.TIME_UNSET;
    }
}
