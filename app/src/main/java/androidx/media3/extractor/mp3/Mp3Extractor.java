package androidx.media3.extractor.mp3;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.DiscardingTrackOutput;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.GaplessInfoHolder;
import androidx.media3.extractor.Id3Peeker;
import androidx.media3.extractor.MpegAudioUtil;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.metadata.id3.Id3Decoder;
import androidx.media3.extractor.metadata.id3.MlltFrame;
import androidx.media3.extractor.metadata.id3.TextInformationFrame;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import java.io.EOFException;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.RoundingMode;
import java.util.List;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class Mp3Extractor implements Extractor {
    public static final ExtractorsFactory FACTORY = null;
    public static final int FLAG_DISABLE_ID3_METADATA = 8;
    public static final int FLAG_ENABLE_CONSTANT_BITRATE_SEEKING = 1;
    public static final int FLAG_ENABLE_CONSTANT_BITRATE_SEEKING_ALWAYS = 2;
    public static final int FLAG_ENABLE_INDEX_SEEKING = 4;
    private static final int MAX_SNIFF_BYTES = 32768;
    private static final int MAX_SYNC_BYTES = 131072;
    private static final int MPEG_AUDIO_HEADER_MASK = -128000;
    private static final Id3Decoder.FramePredicate REQUIRED_ID3_FRAME_PREDICATE = null;
    private static final int SCRATCH_LENGTH = 10;
    private static final int SEEK_HEADER_INFO = 1231971951;
    private static final int SEEK_HEADER_UNSET = 0;
    private static final int SEEK_HEADER_VBRI = 1447187017;
    private static final int SEEK_HEADER_XING = 1483304551;
    private static final String TAG = "Mp3Extractor";
    private long basisTimeUs;
    private TrackOutput currentTrackOutput;
    private boolean disableSeeking;
    private long endPositionOfLastSampleRead;
    private ExtractorOutput extractorOutput;
    private long firstSamplePosition;
    private final int flags;
    private final long forcedFirstSampleTimestampUs;
    private final GaplessInfoHolder gaplessInfoHolder;
    private final Id3Peeker id3Peeker;
    private boolean isSeekInProgress;
    private Metadata metadata;
    private TrackOutput realTrackOutput;
    private int sampleBytesRemaining;
    private long samplesRead;
    private final ParsableByteArray scratch;
    private long seekTimeUs;
    private Seeker seeker;
    private final TrackOutput skippingTrackOutput;
    private final MpegAudioUtil.Header synchronizedHeader;
    private int synchronizedHeaderData;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    static {
        FACTORY = new Mp3Extractor$$ExternalSyntheticLambda0();
        REQUIRED_ID3_FRAME_PREDICATE = new Mp3Extractor$$ExternalSyntheticLambda1();
    }

    public Mp3Extractor() {
        this(0);
    }

    public Mp3Extractor(int r3) {
        this(r3, C0180C.TIME_UNSET);
    }

    public Mp3Extractor(int r3, long r4) {
        if ((r3 & 2) == 0) goto L5;
        r3 = r3 | 1;
    L5:
        this.flags = r3;
        this.forcedFirstSampleTimestampUs = r4;
        this.scratch = new ParsableByteArray(10);
        this.synchronizedHeader = new MpegAudioUtil.Header();
        this.gaplessInfoHolder = new GaplessInfoHolder();
        this.basisTimeUs = C0180C.TIME_UNSET;
        this.id3Peeker = new Id3Peeker();
        this.skippingTrackOutput = new DiscardingTrackOutput();
        this.currentTrackOutput = this.skippingTrackOutput;
        this.endPositionOfLastSampleRead = -1;
    }

    @EnsuresNonNull({"extractorOutput", "realTrackOutput"})
    private void assertInitialized() {
        Assertions.checkStateNotNull(this.realTrackOutput);
        Util.castNonNull(this.extractorOutput);
    }

    private Seeker computeSeeker(ExtractorInput r15) throws IOException {
        Seeker r0 = maybeReadSeekFrame(r15);
        MlltSeeker r1 = maybeHandleSeekMetadata(this.metadata, r15.getPosition());
        if (this.disableSeeking == true) goto L5;
        Seeker r2 = null;
        if ((this.flags & 4) != 0) goto L9;
        if (r1 == null) goto L17;
        r2 = r1;
    L19:
        boolean r3 = true;
        if (r2 == null) goto L26;
        if (r2.isSeekable() == false) goto L24;
        return r2;
    L24:
        if ((this.flags & 1) != 0) goto L26;
        return r2;
    L26:
        if ((this.flags & 2) != 0) goto L30;
        r3 = false;
    L30:
        return getConstantBitrateSeeker(r15, r3);
    L17:
        if (r0 == null) goto L19;
        r2 = r0;
        goto L19
    L9:
        if (r1 == null) goto L11;
        long r5 = r1.getDurationUs();
        long r12 = r1.getDataEndPosition();
        long r8 = r5;
    L14:
        r2 = new IndexSeeker(r8, r15.getPosition(), r12);
        goto L19
    L11:
        if (r0 == null) goto L13;
        long r6 = r0.getDurationUs();
        r12 = r0.getDataEndPosition();
        r8 = r6;
        goto L14
    L13:
        r12 = -1;
        r8 = getId3TlenUs(this.metadata);
        goto L14
    L5:
        return new Seeker.UnseekableSeeker();
    }

    private long computeTimeUs(long r7) {
        return this.basisTimeUs + ((1000000 * r7) / ((long) this.synchronizedHeader.sampleRate));
    }

    private Seeker getConstantBitrateSeeker(long r16, XingFrame r18, long r19) {
        long r5 = r18.computeDurationUs();
        if (r5 != C0180C.TIME_UNSET) goto L6;
        return null;
    L6:
        if (r18.dataSize == (-1)) goto L9;
        long r1 = r16 + r18.dataSize;
        long r8 = r1;
        long r2 = r18.dataSize - ((long) r18.header.frameSize);
    L11:
        int r12 = Ints.checkedCast(Util.scaleLargeValue(r2, 8000000, r5, RoundingMode.HALF_UP));
        int r13 = Ints.checkedCast(LongMath.divide(r2, r18.frameCount, RoundingMode.HALF_UP));
        return new ConstantBitrateSeeker(r8, r16 + ((long) r18.header.frameSize), r12, r13, false);
    L9:
        if (r19 == (-1)) goto L13;
        r8 = r19;
        r2 = (r19 - r16) - ((long) r18.header.frameSize);
        goto L11
    L13:
        return null;
    }

    private Seeker getConstantBitrateSeeker(ExtractorInput r10, boolean r11) throws IOException {
        r10.peekFully(this.scratch.getData(), 0, 4);
        this.scratch.setPosition(0);
        this.synchronizedHeader.setForHeaderData(this.scratch.readInt());
        return new ConstantBitrateSeeker(r10.getLength(), r10.getPosition(), this.synchronizedHeader, r11);
    }

    private static long getId3TlenUs(Metadata r5) {
        if (r5 == null) goto L12;
        int r0 = r5.length();
        int r1 = 0;
    L4:
        if (r1 >= r0) goto L18;
        Metadata.Entry r2 = r5.get(r1);
        if ((r2 instanceof TextInformationFrame) == false) goto L11;
        if (((TextInformationFrame) r2).f55id.equals("TLEN") == false) goto L11;
        return Util.msToUs(Long.parseLong(((TextInformationFrame) r2).values.get(0)));
    L11:
        r1 = r1 + 1;
        goto L4
    L18:
        return C0180C.TIME_UNSET;
    L12:
        return C0180C.TIME_UNSET;
    }

    private static int getSeekFrameHeader(ParsableByteArray r2, int r3) {
        if (r2.limit() < (r3 + 4)) goto L10;
        r2.setPosition(r3);
        int r0 = r2.readInt();
        if (r0 != SEEK_HEADER_XING) goto L7;
    L8:
        return r0;
    L7:
        if (r0 == SEEK_HEADER_INFO) goto L8;
    L10:
        if (r2.limit() < 40) goto L14;
        r2.setPosition(36);
        if (r2.readInt() != SEEK_HEADER_VBRI) goto L16;
        return SEEK_HEADER_VBRI;
    L16:
        return 0;
    L14:
        return 0;
    }

    private static boolean headersMatch(int r4, long r5) {
        if ((MPEG_AUDIO_HEADER_MASK & r4) != ((-128000) & r5)) goto L5;
        return true;
    L5:
        return false;
    }

    static /* synthetic */ Extractor[] lambda$static$0() {
        return new Extractor[]{new Mp3Extractor()};
    }

    static /* synthetic */ boolean lambda$static$1(int r3, int r4, int r5, int r6, int r7) {
        if (r4 == 67) goto L5;
    L9:
        if (r4 == 77) goto L11;
    L17:
        return false;
    L11:
        if (r5 != 76) goto L17;
        if (r6 != 76) goto L17;
        if (r7 == 84) goto L16;
        if (r3 != 2) goto L17;
    L16:
        return true;
    L5:
        if (r5 != 79) goto L9;
        if (r6 != 77) goto L9;
        if (r7 == 77) goto L16;
        if (r3 == 2) goto L16;
        goto L9
    }

    private static MlltSeeker maybeHandleSeekMetadata(Metadata r6, long r7) {
        if (r6 == null) goto L10;
        int r0 = r6.length();
        int r1 = 0;
    L4:
        if (r1 >= r0) goto L14;
        Metadata.Entry r2 = r6.get(r1);
        if ((r2 instanceof MlltFrame) == true) goto L8;
        r1 = r1 + 1;
        goto L4
    L8:
        return MlltSeeker.create(r7, (MlltFrame) r2, getId3TlenUs(r6));
    L14:
        return null;
    L10:
        return null;
    }

    private Seeker maybeReadSeekFrame(ExtractorInput r15) throws IOException {
        ParsableByteArray r0 = new ParsableByteArray(this.synchronizedHeader.frameSize);
        r15.peekFully(r0.getData(), 0, this.synchronizedHeader.frameSize);
        int r2 = 21;
        if ((this.synchronizedHeader.version & 1) == 0) goto L8;
        if (this.synchronizedHeader.channels == 1) goto L11;
        r2 = 36;
    L11:
        int r1 = getSeekFrameHeader(r0, r2);
        switch(r1) {
            case 1231971951: goto L15;
            case 1447187017: goto L14;
            case 1483304551: goto L15;
            default: goto L13;
        };
    L13:
        r15.resetPeekPosition();
        return null;
    L14:
        VbriSeeker r3 = VbriSeeker.create(r15.getLength(), r15.getPosition(), this.synchronizedHeader, r0);
        r15.skipFully(this.synchronizedHeader.frameSize);
        return r3;
    L15:
        XingFrame r11 = XingFrame.parse(this.synchronizedHeader, r0);
        if (this.gaplessInfoHolder.hasGaplessInfo() == false) goto L18;
    L22:
        long r9 = r15.getPosition();
        if (r15.getLength() != (-1)) goto L25;
    L29:
        r15.skipFully(this.synchronizedHeader.frameSize);
        if (r1 != SEEK_HEADER_XING) goto L33;
        return XingSeeker.create(r11, r9);
    L33:
        return getConstantBitrateSeeker(r9, r11, r15.getLength());
    L25:
        if (r11.dataSize == (-1)) goto L29;
        if (r15.getLength() == (r11.dataSize + r9)) goto L29;
        Log.m46i(TAG, "Data size mismatch between stream (" + r15.getLength() + ") and Xing frame (" + (r11.dataSize + r9) + "), using Xing value.");
        goto L29
    L18:
        if (r11.encoderDelay == (-1)) goto L22;
        if (r11.encoderPadding == (-1)) goto L22;
        this.gaplessInfoHolder.encoderDelay = r11.encoderDelay;
        this.gaplessInfoHolder.encoderPadding = r11.encoderPadding;
        goto L22
    L8:
        if (this.synchronizedHeader.channels != 1) goto L11;
        r2 = 13;
        goto L11
    }

    private void maybeUpdateCbrDurationToLastSample() {
        if ((this.seeker instanceof ConstantBitrateSeeker) == true) goto L5;
        return;
    L5:
        if (this.seeker.isSeekable() == true) goto L7;
        return;
    L7:
        if (this.endPositionOfLastSampleRead != (-1)) goto L9;
        return;
    L9:
        if (this.endPositionOfLastSampleRead == this.seeker.getDataEndPosition()) goto L15;
        this.seeker = ((ConstantBitrateSeeker) this.seeker).copyWithNewDataEndPosition(this.endPositionOfLastSampleRead);
        ((ExtractorOutput) Assertions.checkNotNull(this.extractorOutput)).seekMap(this.seeker);
        return;
    }

    private boolean peekEndOfStreamOrHeader(ExtractorInput r9) throws IOException {
        if (this.seeker == null) goto L14;
        long r2 = this.seeker.getDataEndPosition();
        if (r2 == (-1)) goto L14;
        if (r9.getPeekPosition() <= (r2 - 4)) goto L14;
        return true;
    L14:
        return !r9.peekFully(this.scratch.getData(), 0, 4, true);
    L13:
        return true;
    }

    @RequiresNonNull({"extractorOutput", "realTrackOutput"})
    private int readInternal(ExtractorInput r5) throws IOException {
        if (this.synchronizedHeaderData != 0) goto L11;
        synchronize(r5, false);     // Catch: EOFException -> L7
    L8:
        return -1;
    L11:
        if (this.seeker != null) goto L21;
        this.seeker = computeSeeker(r5);
        this.extractorOutput.seekMap(this.seeker);
        Format.Builder r0 = new Format.Builder().setSampleMimeType(this.synchronizedHeader.mimeType).setMaxInputSize(4096).setChannelCount(this.synchronizedHeader.channels).setSampleRate(this.synchronizedHeader.sampleRate).setEncoderDelay(this.gaplessInfoHolder.encoderDelay).setEncoderPadding(this.gaplessInfoHolder.encoderPadding);
        if ((this.flags & 8) == 0) goto L15;
        Metadata r1 = null;
    L16:
        Format.Builder r2 = r0.setMetadata(r1);
        if (this.seeker.getAverageBitrate() == (-2147483647)) goto L19;
        r2.setAverageBitrate(this.seeker.getAverageBitrate());
    L19:
        this.currentTrackOutput.format(r2.build());
        this.firstSamplePosition = r5.getPosition();
    L27:
        return readSample(r5);
    L15:
        r1 = this.metadata;
        goto L16
    L21:
        if (this.firstSamplePosition == 0) goto L27;
        long r3 = r5.getPosition();
        if (r3 >= this.firstSamplePosition) goto L27;
        r5.skipFully((int) (this.firstSamplePosition - r3));
        goto L27
    }

    @RequiresNonNull({"realTrackOutput", "seeker"})
    private int readSample(ExtractorInput r12) throws IOException {
        if (this.sampleBytesRemaining != 0) goto L26;
        r12.resetPeekPosition();
        if (peekEndOfStreamOrHeader(r12) == false) goto L7;
        return -1;
    L7:
        this.scratch.setPosition(0);
        int r0 = this.scratch.readInt();
        if (headersMatch(r0, this.synchronizedHeaderData) == true) goto L10;
    L24:
        r12.skipFully(1);
        this.synchronizedHeaderData = 0;
        return 0;
    L10:
        if (MpegAudioUtil.getFrameSize(r0) == (-1)) goto L24;
        this.synchronizedHeader.setForHeaderData(r0);
        if (this.basisTimeUs != C0180C.TIME_UNSET) goto L17;
        this.basisTimeUs = this.seeker.getTimeUs(r12.getPosition());
        if (this.forcedFirstSampleTimestampUs == C0180C.TIME_UNSET) goto L17;
        this.basisTimeUs += this.forcedFirstSampleTimestampUs - this.seeker.getTimeUs(0);
    L17:
        this.sampleBytesRemaining = this.synchronizedHeader.frameSize;
        this.endPositionOfLastSampleRead = r12.getPosition() + ((long) this.synchronizedHeader.frameSize);
        if ((this.seeker instanceof IndexSeeker) == false) goto L26;
        IndexSeeker r4 = (IndexSeeker) this.seeker;
        r4.maybeAddSeekPoint(computeTimeUs(this.samplesRead + ((long) this.synchronizedHeader.samplesPerFrame)), this.endPositionOfLastSampleRead);
        if (this.isSeekInProgress == false) goto L26;
        if (r4.isTimeUsInIndex(this.seekTimeUs) == false) goto L26;
        this.isSeekInProgress = false;
        this.currentTrackOutput = this.realTrackOutput;
    L26:
        int r1 = this.currentTrackOutput.sampleData(r12, this.sampleBytesRemaining, true);
        if (r1 != (-1)) goto L29;
        return -1;
    L29:
        this.sampleBytesRemaining -= r1;
        if (this.sampleBytesRemaining <= 0) goto L32;
        return 0;
    L32:
        this.currentTrackOutput.sampleMetadata(computeTimeUs(this.samplesRead), 1, this.synchronizedHeader.frameSize, 0, null);
        this.samplesRead += (long) this.synchronizedHeader.samplesPerFrame;
        this.sampleBytesRemaining = 0;
        return 0;
    }

    private boolean synchronize(ExtractorInput r12, boolean r13) throws IOException {
        int r0 = 0;
        int r1 = 0;
        int r2 = 0;
        int r3 = 0;
        if (r13 == false) goto L5;
        int r4 = 32768;
    L6:
        r12.resetPeekPosition();
        if (r12.getPosition() != 0) goto L22;
        if ((this.flags & 8) != 0) goto L11;
        boolean r5 = true;
    L12:
        if (r5 == false) goto L14;
        Id3Decoder.FramePredicate r8 = null;
    L15:
        this.metadata = this.id3Peeker.peekId3Data(r12, r8);
        if (this.metadata == null) goto L18;
        this.gaplessInfoHolder.setFromMetadata(this.metadata);
    L18:
        r2 = (int) r12.getPeekPosition();
        if (r13 == true) goto L22;
        r12.skipFully(r2);
        goto L22
    L14:
        r8 = REQUIRED_ID3_FRAME_PREDICATE;
        goto L15
    L11:
        r5 = false;
    L22:
        if (peekEndOfStreamOrHeader(r12) == true) goto L23;
        this.scratch.setPosition(0);
        int r6 = this.scratch.readInt();
        if (r1 != 0) goto L30;
    L31:
        int r9 = MpegAudioUtil.getFrameSize(r6);
        if (r9 == (-1)) goto L33;
        r0 = r0 + 1;
        if (r0 != 1) goto L48;
        this.synchronizedHeader.setForHeaderData(r6);
        r1 = r6;
    L54:
        r12.advancePeekPosition(r9 - 4);
        goto L22
    L48:
        if (r0 != 4) goto L54;
    L49:
        if (r13 == false) goto L51;
        r12.skipFully(r2 + r3);
    L52:
        this.synchronizedHeaderData = r1;
        return true;
    L51:
        r12.resetPeekPosition();
    L33:
        int r10 = r3 + 1;
        if (r3 == r4) goto L35;
        r0 = 0;
        r1 = 0;
        if (r13 == false) goto L42;
        r12.resetPeekPosition();
        r12.advancePeekPosition(r2 + r10);
    L43:
        r3 = r10;
        goto L22
    L42:
        r12.skipFully(1);
        goto L43
    L35:
        if (r13 == false) goto L37;
        return false;
    L37:
        maybeUpdateCbrDurationToLastSample();
        throw new EOFException();
    L30:
        if (headersMatch(r6, r1) == false) goto L33;
    L23:
        if (r0 > 0) goto L49;
        maybeUpdateCbrDurationToLastSample();
        throw new EOFException();
    L5:
        r4 = 131072;
        goto L6
    }

    public void disableSeeking() {
        this.disableSeeking = true;
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
    public void init(ExtractorOutput r4) {
        this.extractorOutput = r4;
        this.realTrackOutput = this.extractorOutput.track(0, 1);
        this.currentTrackOutput = this.realTrackOutput;
        this.extractorOutput.endTracks();
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r6, PositionHolder r7) throws IOException {
        assertInitialized();
        int r0 = readInternal(r6);
        if (r0 == (-1)) goto L5;
    L9:
        return r0;
    L5:
        if ((this.seeker instanceof IndexSeeker) == false) goto L9;
        long r1 = computeTimeUs(this.samplesRead);
        if (this.seeker.getDurationUs() == r1) goto L9;
        ((IndexSeeker) this.seeker).setDurationUs(r1);
        this.extractorOutput.seekMap(this.seeker);
        goto L9
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r4, long r6) {
        this.synchronizedHeaderData = 0;
        this.basisTimeUs = C0180C.TIME_UNSET;
        this.samplesRead = 0;
        this.sampleBytesRemaining = 0;
        this.seekTimeUs = r6;
        if ((this.seeker instanceof IndexSeeker) == true) goto L5;
        return;
    L5:
        if (((IndexSeeker) this.seeker).isTimeUsInIndex(r6) == true) goto L9;
        this.isSeekInProgress = true;
        this.currentTrackOutput = this.skippingTrackOutput;
        return;
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r2) throws IOException {
        return synchronize(r2, true);
    }
}
