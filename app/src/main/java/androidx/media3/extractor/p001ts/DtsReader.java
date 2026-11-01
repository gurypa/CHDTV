package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.DtsUtil;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import com.google.common.primitives.Ints;
import java.util.concurrent.atomic.AtomicInteger;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class DtsReader implements ElementaryStreamReader {
    private static final int CORE_HEADER_SIZE = 18;
    static final int EXTSS_HEADER_SIZE_MAX = 4096;
    static final int FTOC_MAX_HEADER_SIZE = 5408;
    private static final int STATE_FINDING_EXTSS_HEADER_SIZE = 2;
    private static final int STATE_FINDING_SYNC = 0;
    private static final int STATE_FINDING_UHD_HEADER_SIZE = 4;
    private static final int STATE_READING_CORE_HEADER = 1;
    private static final int STATE_READING_EXTSS_HEADER = 3;
    private static final int STATE_READING_SAMPLE = 6;
    private static final int STATE_READING_UHD_HEADER = 5;
    private int bytesRead;
    private int extensionSubstreamHeaderSize;
    private Format format;
    private String formatId;
    private int frameType;
    private final ParsableByteArray headerScratchBytes;
    private final String language;
    private TrackOutput output;
    private final int roleFlags;
    private long sampleDurationUs;
    private int sampleSize;
    private int state;
    private int syncBytes;
    private long timeUs;
    private final AtomicInteger uhdAudioChunkId;
    private int uhdHeaderSize;

    public DtsReader(String r3, int r4, int r5) {
        this.headerScratchBytes = new ParsableByteArray(new byte[r5]);
        this.state = 0;
        this.timeUs = C0180C.TIME_UNSET;
        this.uhdAudioChunkId = new AtomicInteger();
        this.extensionSubstreamHeaderSize = -1;
        this.uhdHeaderSize = -1;
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
    private void parseCoreHeader() {
        byte[] r0 = this.headerScratchBytes.getData();
        if (this.format != null) goto L5;
        this.format = DtsUtil.parseDtsFormat(r0, this.formatId, this.language, this.roleFlags, null);
        this.output.format(this.format);
    L5:
        this.sampleSize = DtsUtil.getDtsFrameSize(r0);
        this.sampleDurationUs = Ints.checkedCast(Util.sampleCountToDurationUs(DtsUtil.parseDtsAudioSampleCount(r0), this.format.sampleRate));
    }

    @RequiresNonNull({"output"})
    private void parseExtensionSubstreamHeader() throws ParserException {
        DtsUtil.DtsHeader r0 = DtsUtil.parseDtsHdHeader(this.headerScratchBytes.getData());
        updateFormatWithDtsHeaderInfo(r0);
        this.sampleSize = r0.frameSize;
        if (r0.frameDurationUs != C0180C.TIME_UNSET) goto L5;
        long r1 = 0;
    L6:
        this.sampleDurationUs = r1;
        return;
    L5:
        r1 = r0.frameDurationUs;
        goto L6
    }

    @RequiresNonNull({"output"})
    private void parseUhdHeader() throws ParserException {
        DtsUtil.DtsHeader r0 = DtsUtil.parseDtsUhdHeader(this.headerScratchBytes.getData(), this.uhdAudioChunkId);
        if (this.frameType != 3) goto L5;
        updateFormatWithDtsHeaderInfo(r0);
    L5:
        this.sampleSize = r0.frameSize;
        if (r0.frameDurationUs != C0180C.TIME_UNSET) goto L8;
        long r1 = 0;
    L9:
        this.sampleDurationUs = r1;
        return;
    L8:
        r1 = r0.frameDurationUs;
        goto L9
    }

    private boolean skipToNextSyncWord(ParsableByteArray r6) {
    L3:
        if (r6.bytesLeft() <= 0) goto L8;
        this.syncBytes <<= 8;
        this.syncBytes |= r6.readUnsignedByte();
        this.frameType = DtsUtil.getFrameType(this.syncBytes);
        if (this.frameType == 0) goto L3;
        byte[] r0 = this.headerScratchBytes.getData();
        r0[0] = (byte) ((this.syncBytes >> 24) & 255);
        r0[1] = (byte) ((this.syncBytes >> 16) & 255);
        r0[2] = (byte) ((this.syncBytes >> 8) & 255);
        r0[3] = (byte) (this.syncBytes & 255);
        this.bytesRead = 4;
        this.syncBytes = 0;
        return true;
    L8:
        return false;
    }

    @RequiresNonNull({"output"})
    private void updateFormatWithDtsHeaderInfo(DtsUtil.DtsHeader r4) {
        if (r4.sampleRate != (-2147483647)) goto L5;
        return;
    L5:
        if (r4.channelCount != (-1)) goto L8;
        return;
    L8:
        if (this.format == null) goto L16;
        if (r4.channelCount != this.format.channelCount) goto L16;
        if (r4.sampleRate != this.format.sampleRate) goto L16;
        if (Util.areEqual(r4.mimeType, this.format.sampleMimeType) == false) goto L16;
        return;
    L16:
        if (this.format != null) goto L18;
        Format.Builder r0 = new Format.Builder();
    L19:
        this.format = r0.setId(this.formatId).setSampleMimeType(r4.mimeType).setChannelCount(r4.channelCount).setSampleRate(r4.sampleRate).setLanguage(this.language).setRoleFlags(this.roleFlags).build();
        this.output.format(this.format);
        return;
    L18:
        r0 = this.format.buildUpon();
        goto L19
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void consume(ParsableByteArray r14) throws ParserException {
        Assertions.checkStateNotNull(this.output);
    L4:
        if (r14.bytesLeft() <= 0) goto L50;
        switch(this.state) {
            case 0: goto L39;
            case 1: goto L36;
            case 2: goto L33;
            case 3: goto L30;
            case 4: goto L24;
            case 5: goto L21;
            case 6: goto L9;
            default: goto L8;
        };
    L8:
        throw new IllegalStateException();
    L21:
        if (continueRead(r14, this.headerScratchBytes.getData(), this.uhdHeaderSize) == false) goto L4;
        parseUhdHeader();
        this.headerScratchBytes.setPosition(0);
        this.output.sampleData(this.headerScratchBytes, this.uhdHeaderSize);
        this.state = 6;
        goto L4
    L24:
        if (continueRead(r14, this.headerScratchBytes.getData(), 6) == false) goto L4;
        this.uhdHeaderSize = DtsUtil.parseDtsUhdHeaderSize(this.headerScratchBytes.getData());
        if (this.bytesRead <= this.uhdHeaderSize) goto L28;
        int r0 = this.bytesRead - this.uhdHeaderSize;
        this.bytesRead -= r0;
        r14.setPosition(r14.getPosition() - r0);
    L28:
        this.state = 5;
        goto L4
    L30:
        if (continueRead(r14, this.headerScratchBytes.getData(), this.extensionSubstreamHeaderSize) == false) goto L4;
        parseExtensionSubstreamHeader();
        this.headerScratchBytes.setPosition(0);
        this.output.sampleData(this.headerScratchBytes, this.extensionSubstreamHeaderSize);
        this.state = 6;
        goto L4
    L33:
        if (continueRead(r14, this.headerScratchBytes.getData(), 7) == false) goto L4;
        this.extensionSubstreamHeaderSize = DtsUtil.parseDtsHdHeaderSize(this.headerScratchBytes.getData());
        this.state = 3;
        goto L4
    L36:
        if (continueRead(r14, this.headerScratchBytes.getData(), 18) == false) goto L4;
        parseCoreHeader();
        this.headerScratchBytes.setPosition(0);
        this.output.sampleData(this.headerScratchBytes, 18);
        this.state = 6;
        goto L4
    L39:
        if (skipToNextSyncWord(r14) == false) goto L4;
        if (this.frameType == 3) goto L49;
        if (this.frameType == 4) goto L49;
        if (this.frameType == 1) goto L47;
        this.state = 2;
        goto L4
    L47:
        this.state = 1;
    L49:
        this.state = 4;
        goto L4
    L9:
        int r1 = Math.min(r14.bytesLeft(), this.sampleSize - this.bytesRead);
        this.output.sampleData(r14, r1);
        this.bytesRead += r1;
        if (this.bytesRead != this.sampleSize) goto L4;
        if (this.timeUs == C0180C.TIME_UNSET) goto L14;
        boolean r2 = true;
    L15:
        Assertions.checkState(r2);
        TrackOutput r6 = this.output;
        long r7 = this.timeUs;
        if (this.frameType != 4) goto L18;
        int r9 = 0;
    L19:
        r6.sampleMetadata(r7, r9, this.sampleSize, 0, null);
        this.timeUs += this.sampleDurationUs;
        this.state = 0;
        goto L4
    L18:
        r9 = 1;
        goto L19
    L14:
        r2 = false;
        goto L15
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
        this.syncBytes = 0;
        this.timeUs = C0180C.TIME_UNSET;
        this.uhdAudioChunkId.set(0);
    }
}
