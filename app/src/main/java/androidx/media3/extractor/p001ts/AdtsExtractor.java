package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.ConstantBitrateSeekMap;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import java.io.EOFException;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class AdtsExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = null;
    public static final int FLAG_ENABLE_CONSTANT_BITRATE_SEEKING = 1;
    public static final int FLAG_ENABLE_CONSTANT_BITRATE_SEEKING_ALWAYS = 2;
    private static final int MAX_PACKET_SIZE = 2048;
    private static final int MAX_SNIFF_BYTES = 8192;
    private static final int NUM_FRAMES_FOR_AVERAGE_FRAME_SIZE = 1000;
    private int averageFrameSize;
    private ExtractorOutput extractorOutput;
    private long firstFramePosition;
    private long firstSampleTimestampUs;
    private final int flags;
    private boolean hasCalculatedAverageFrameSize;
    private boolean hasOutputSeekMap;
    private final ParsableByteArray packetBuffer;
    private final AdtsReader reader;
    private final ParsableByteArray scratch;
    private final ParsableBitArray scratchBits;
    private boolean startedPacket;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    static {
        FACTORY = new AdtsExtractor$$ExternalSyntheticLambda0();
    }

    public AdtsExtractor() {
        this(0);
    }

    public AdtsExtractor(int r3) {
        if ((r3 & 2) == 0) goto L5;
        r3 = r3 | 1;
    L5:
        this.flags = r3;
        this.reader = new AdtsReader(true);
        this.packetBuffer = new ParsableByteArray(2048);
        this.averageFrameSize = -1;
        this.firstFramePosition = -1;
        this.scratch = new ParsableByteArray(10);
        this.scratchBits = new ParsableBitArray(this.scratch.getData());
    }

    private void calculateAverageFrameSize(ExtractorInput r10) throws IOException {
        if (this.hasCalculatedAverageFrameSize == false) goto L5;
        return;
    L5:
        this.averageFrameSize = -1;
        r10.resetPeekPosition();
        if (r10.getPosition() != 0) goto L8;
        peekId3Header(r10);
    L8:
        int r1 = 0;
        long r2 = 0;
    L37:
        if (r10.peekFully(this.scratch.getData(), 0, 2, true) == false) goto L31;
        this.scratch.setPosition(0);     // Catch: EOFException -> L30
        if (AdtsReader.isAdtsSyncWord(this.scratch.readUnsignedShort()) == false) goto L14;
        if (r10.peekFully(this.scratch.getData(), 0, 4, true) == false) goto L31;
        this.scratchBits.setPosition(14);     // Catch: EOFException -> L30
        int r6 = this.scratchBits.readBits(13);     // Catch: EOFException -> L30
        if (r6 <= 6) goto L27;
        r2 = r2 + ((long) r6);     // Catch: EOFException -> L30
        r1 = r1 + 1;     // Catch: EOFException -> L30
        if (r1 == 1000) goto L31;
        if (r10.advancePeekPosition(r6 - 6, true) == true) goto L37;
    L27:
        this.hasCalculatedAverageFrameSize = true;     // Catch: EOFException -> L30
        throw ParserException.createForMalformedContainer("Malformed ADTS stream", null);     // Catch: EOFException -> L30
    L14:
        r1 = 0;
    L31:
        r10.resetPeekPosition();
        if (r1 <= 0) goto L34;
        this.averageFrameSize = (int) (r2 / ((long) r1));
    L35:
        this.hasCalculatedAverageFrameSize = true;
        return;
    L34:
        this.averageFrameSize = -1;
        goto L35
    }

    private static int getBitrateFromFrameSize(int r4, long r5) {
        return (int) (((((long) r4) * 8) * 1000000) / r5);
    }

    private SeekMap getConstantBitrateSeekMap(long r12, boolean r14) {
        int r8 = getBitrateFromFrameSize(this.averageFrameSize, this.reader.getSampleDurationUs());
        return new ConstantBitrateSeekMap(r12, this.firstFramePosition, r8, this.averageFrameSize, r14);
    }

    static /* synthetic */ Extractor[] lambda$static$0() {
        return new Extractor[]{new AdtsExtractor()};
    }

    @RequiresNonNull({"extractorOutput"})
    private void maybeOutputSeekMap(long r8, boolean r10) {
        if (this.hasOutputSeekMap == false) goto L5;
        return;
    L5:
        boolean r2 = false;
        if ((this.flags & 1) != 0) goto L8;
    L10:
        boolean r0 = false;
    L12:
        if (r0 == true) goto L14;
    L17:
        if (r0 == true) goto L19;
    L24:
        this.extractorOutput.seekMap(new SeekMap.Unseekable(C0180C.TIME_UNSET));
    L25:
        this.hasOutputSeekMap = true;
        return;
    L19:
        if (this.reader.getSampleDurationUs() == C0180C.TIME_UNSET) goto L24;
        ExtractorOutput r3 = this.extractorOutput;
        if ((this.flags & 2) == 0) goto L23;
        r2 = true;
    L23:
        r3.seekMap(getConstantBitrateSeekMap(r8, r2));
        goto L25
    L14:
        if (this.reader.getSampleDurationUs() != C0180C.TIME_UNSET) goto L17;
        if (r10 == true) goto L17;
        return;
    L8:
        if (this.averageFrameSize <= 0) goto L10;
        r0 = true;
        goto L12
    }

    private int peekId3Header(ExtractorInput r6) throws IOException {
        int r0 = 0;
    L3:
        r6.peekFully(this.scratch.getData(), 0, 10);
        this.scratch.setPosition(0);
        if (this.scratch.readUnsignedInt24() != 4801587) goto L5;
        this.scratch.skipBytes(3);
        int r1 = this.scratch.readSynchSafeInt();
        r0 = r0 + (r1 + 10);
        r6.advancePeekPosition(r1);
        goto L3
    L5:
        r6.resetPeekPosition();
        r6.advancePeekPosition(r0);
        if (this.firstFramePosition != (-1)) goto L8;
        this.firstFramePosition = r0;
    L8:
        return r0;
    }

    @Override // androidx.media3.extractor.Extractor
    public /* synthetic */ List getSniffFailureDetails() {
        return Extractor.CC.$default$getSniffFailureDetails(this);
    }

    @Override // androidx.media3.extractor.Extractor
    public /* synthetic */ Extractor getUnderlyingImplementation() {
        return Extractor.CC.$default$getUnderlyingImplementation(this);
    }

    @Override // androidx.media3.extractor.Extractor
    public void init(ExtractorOutput r5) {
        this.extractorOutput = r5;
        this.reader.createTracks(r5, new TsPayloadReader.TrackIdGenerator(0, 1));
        r5.endTracks();
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r12, PositionHolder r13) throws IOException {
        Assertions.checkStateNotNull(this.extractorOutput);
        long r0 = r12.getLength();
        if ((this.flags & 2) == 0) goto L5;
    L10:
        boolean r2 = true;
    L11:
        if (r2 == false) goto L13;
        calculateAverageFrameSize(r12);
    L13:
        int r5 = r12.read(this.packetBuffer.getData(), 0, 2048);
        if (r5 != (-1)) goto L16;
        boolean r7 = true;
    L17:
        maybeOutputSeekMap(r0, r7);
        if (r7 == false) goto L20;
        return -1;
    L20:
        this.packetBuffer.setPosition(0);
        this.packetBuffer.setLimit(r5);
        if (this.startedPacket == true) goto L23;
        this.reader.packetStarted(this.firstSampleTimestampUs, 4);
        this.startedPacket = true;
    L23:
        this.reader.consume(this.packetBuffer);
        return 0;
    L16:
        r7 = false;
        goto L17
    L5:
        if ((this.flags & 1) != 0) goto L7;
    L9:
        r2 = false;
        goto L11
    L7:
        if (r0 == (-1)) goto L9;
        goto L9
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r2, long r4) {
        this.startedPacket = false;
        this.reader.seek();
        this.firstSampleTimestampUs = r4;
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r9) throws IOException {
        int r0 = peekId3Header(r9);
        int r1 = r0;
        int r2 = 0;
        int r3 = 0;
    L3:
        r9.peekFully(this.scratch.getData(), 0, 2);
        this.scratch.setPosition(0);
        if (AdtsReader.isAdtsSyncWord(this.scratch.readUnsignedShort()) == true) goto L6;
        r3 = 0;
        r2 = 0;
        r1 = r1 + 1;
        r9.resetPeekPosition();
        r9.advancePeekPosition(r1);
    L17:
        if ((r1 - r0) < 8192) goto L3;
        return false;
    L6:
        r3 = r3 + 1;
        if (r3 >= 4) goto L9;
    L12:
        r9.peekFully(this.scratch.getData(), 0, 4);
        this.scratchBits.setPosition(14);
        int r5 = this.scratchBits.readBits(13);
        if (r5 > 6) goto L15;
        r3 = 0;
        r2 = 0;
        r1 = r1 + 1;
        r9.resetPeekPosition();
        r9.advancePeekPosition(r1);
        goto L17
    L15:
        r9.advancePeekPosition(r5 - 6);
        r2 = r2 + r5;
        goto L17
    L9:
        if (r2 <= 188) goto L12;
        return true;
    }
}
