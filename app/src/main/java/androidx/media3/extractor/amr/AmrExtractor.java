package androidx.media3.extractor.amr;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.audio.SilenceSkippingAudioProcessor;
import androidx.media3.extractor.ConstantBitrateSeekMap;
import androidx.media3.extractor.DiscardingTrackOutput;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.IndexSeekMap;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.TrackOutput;
import java.io.EOFException;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class AmrExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = null;
    public static final int FLAG_ENABLE_CONSTANT_BITRATE_SEEKING = 1;
    public static final int FLAG_ENABLE_CONSTANT_BITRATE_SEEKING_ALWAYS = 2;
    public static final int FLAG_ENABLE_INDEX_SEEKING = 4;
    private static final int NUM_SAME_SIZE_CONSTANT_BIT_RATE_THRESHOLD = 20;
    private static final int SAMPLE_RATE_NB = 8000;
    private static final int SAMPLE_RATE_WB = 16000;
    private static final int SAMPLE_TIME_PER_FRAME_US = 20000;
    private static final byte[] amrSignatureNb = null;
    private static final byte[] amrSignatureWb = null;
    private static final int[] frameSizeBytesByTypeNb = null;
    private static final int[] frameSizeBytesByTypeWb = null;
    private int currentSampleBytesRemaining;
    private int currentSampleSize;
    private long currentSampleTimeUs;
    private TrackOutput currentTrackOutput;
    private ExtractorOutput extractorOutput;
    private long firstSamplePosition;
    private int firstSampleSize;
    private final int flags;
    private boolean hasOutputFormat;
    private boolean isSeekInProgress;
    private boolean isWideBand;
    private int numSamplesWithSameSize;
    private TrackOutput realTrackOutput;
    private final byte[] scratch;
    private SeekMap seekMap;
    private long seekTimeUs;
    private final TrackOutput skippingTrackOutput;
    private long timeOffsetUs;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    static {
        FACTORY = new AmrExtractor$$ExternalSyntheticLambda0();
        frameSizeBytesByTypeNb = new int[]{13, 14, 16, 18, 20, 21, 27, 32, 6, 7, 6, 6, 1, 1, 1, 1};
        frameSizeBytesByTypeWb = new int[]{18, 24, 33, 37, 41, 47, 51, 59, 61, 6, 1, 1, 1, 1, 1, 1};
        amrSignatureNb = Util.getUtf8Bytes("#!AMR\n");
        amrSignatureWb = Util.getUtf8Bytes("#!AMR-WB\n");
    }

    public AmrExtractor() {
        this(0);
    }

    public AmrExtractor(int r2) {
        if ((r2 & 2) == 0) goto L5;
        r2 = r2 | 1;
    L5:
        this.flags = r2;
        this.scratch = new byte[1];
        this.firstSampleSize = -1;
        this.skippingTrackOutput = new DiscardingTrackOutput();
        this.currentTrackOutput = this.skippingTrackOutput;
    }

    static byte[] amrSignatureNb() {
        return Arrays.copyOf(amrSignatureNb, amrSignatureNb.length);
    }

    static byte[] amrSignatureWb() {
        return Arrays.copyOf(amrSignatureWb, amrSignatureWb.length);
    }

    @EnsuresNonNull({"extractorOutput", "realTrackOutput"})
    private void assertInitialized() {
        Assertions.checkStateNotNull(this.realTrackOutput);
        Util.castNonNull(this.extractorOutput);
    }

    static int frameSizeBytesByTypeNb(int r1) {
        return frameSizeBytesByTypeNb[r1];
    }

    static int frameSizeBytesByTypeWb(int r1) {
        return frameSizeBytesByTypeWb[r1];
    }

    private static int getBitrateFromFrameSize(int r4, long r5) {
        return (int) (((((long) r4) * 8) * 1000000) / r5);
    }

    private SeekMap getConstantBitrateSeekMap(long r12, boolean r14) {
        int r8 = getBitrateFromFrameSize(this.firstSampleSize, 20000);
        return new ConstantBitrateSeekMap(r12, this.firstSamplePosition, r8, this.firstSampleSize, r14);
    }

    private int getFrameSizeInBytes(int r3) throws ParserException {
        if (isValidFrameType(r3) == true) goto L11;
        StringBuilder r0 = new StringBuilder().append("Illegal AMR ");
        if (this.isWideBand == false) goto L7;
        String r1 = "WB";
    L9:
        throw ParserException.createForMalformedContainer(r0.append(r1).append(" frame type ").append(r3).toString(), null);
    L7:
        r1 = "NB";
        goto L9
    L11:
        if (this.isWideBand == false) goto L14;
        return frameSizeBytesByTypeWb[r3];
    L14:
        return frameSizeBytesByTypeNb[r3];
    }

    private boolean isNarrowBandValidFrameType(int r2) {
        if (this.isWideBand == false) goto L5;
    L9:
        return false;
    L5:
        if (r2 >= 12) goto L7;
    L8:
        return true;
    L7:
        if (r2 <= 14) goto L9;
        goto L8
    }

    private boolean isSeekTimeUsWithinRange(long r5, long r7) {
        if (Math.abs(r7 - r5) >= 20000) goto L5;
        return true;
    L5:
        return false;
    }

    private boolean isValidFrameType(int r2) {
        if (r2 >= 0) goto L4;
    L10:
        return false;
    L4:
        if (r2 > 15) goto L10;
        if (isWideBandValidFrameType(r2) == false) goto L8;
    L9:
        return true;
    L8:
        if (isNarrowBandValidFrameType(r2) == false) goto L10;
        goto L9
    }

    private boolean isWideBandValidFrameType(int r2) {
        if (this.isWideBand == true) goto L5;
    L9:
        return false;
    L5:
        if (r2 >= 10) goto L7;
    L8:
        return true;
    L7:
        if (r2 <= 13) goto L9;
        goto L8
    }

    static /* synthetic */ Extractor[] lambda$static$0() {
        return new Extractor[]{new AmrExtractor()};
    }

    @RequiresNonNull({"realTrackOutput"})
    private void maybeOutputFormat() {
        if (this.hasOutputFormat == true) goto L18;
        this.hasOutputFormat = true;
        if (this.isWideBand == false) goto L7;
        String r1 = MimeTypes.AUDIO_AMR_WB;
    L9:
        if (this.isWideBand == false) goto L11;
        int r2 = 16000;
    L13:
        if (this.isWideBand == false) goto L15;
        int r3 = frameSizeBytesByTypeWb[8];
    L16:
        this.currentTrackOutput.format(new Format.Builder().setSampleMimeType(r1).setMaxInputSize(r3).setChannelCount(1).setSampleRate(r2).build());
        return;
    L15:
        r3 = frameSizeBytesByTypeNb[7];
        goto L16
    L11:
        r2 = 8000;
        goto L13
    L7:
        r1 = MimeTypes.AUDIO_AMR_NB;
        goto L9
    }

    @RequiresNonNull({"extractorOutput"})
    private void maybeOutputSeekMap(long r9, int r11) {
        if (this.seekMap == null) goto L5;
        return;
    L5:
        boolean r3 = false;
        if ((this.flags & 4) == 0) goto L9;
        this.seekMap = new IndexSeekMap(new long[]{this.firstSamplePosition}, new long[]{0}, C0180C.TIME_UNSET);
    L24:
        if (this.seekMap == null) goto L27;
        this.extractorOutput.seekMap(this.seekMap);
        return;
    L27:
        return;
    L9:
        if ((this.flags & 1) != 0) goto L11;
    L22:
        this.seekMap = new SeekMap.Unseekable(C0180C.TIME_UNSET);
        goto L24
    L11:
        if (this.firstSampleSize == (-1)) goto L16;
        if (this.firstSampleSize != this.currentSampleSize) goto L22;
    L16:
        if (this.numSamplesWithSameSize >= 20) goto L19;
        if (r11 != (-1)) goto L24;
    L19:
        if ((this.flags & 2) == 0) goto L21;
        r3 = true;
    L21:
        this.seekMap = getConstantBitrateSeekMap(r9, r3);
        goto L24
    }

    private static boolean peekAmrSignature(ExtractorInput r3, byte[] r4) throws IOException {
        r3.resetPeekPosition();
        byte[] r0 = new byte[r4.length];
        r3.peekFully(r0, 0, r4.length);
        return Arrays.equals(r0, r4);
    }

    private int peekNextSampleSize(ExtractorInput r4) throws IOException {
        r4.resetPeekPosition();
        r4.peekFully(this.scratch, 0, 1);
        byte r0 = this.scratch[0];
        if ((r0 & 131) > 0) goto L7;
        return getFrameSizeInBytes((r0 >> 3) & 15);
    L7:
        throw ParserException.createForMalformedContainer("Invalid padding bits for frame header " + r0, null);
    }

    private boolean readAmrHeader(ExtractorInput r4) throws IOException {
        if (peekAmrSignature(r4, amrSignatureNb) == false) goto L7;
        this.isWideBand = false;
        r4.skipFully(amrSignatureNb.length);
        return true;
    L7:
        if (peekAmrSignature(r4, amrSignatureWb) == false) goto L10;
        this.isWideBand = true;
        r4.skipFully(amrSignatureWb.length);
        return true;
    L10:
        return false;
    }

    @RequiresNonNull({"realTrackOutput"})
    private int readSample(ExtractorInput r13) throws IOException {
        if (this.currentSampleBytesRemaining == 0) goto L31;
    L23:
        int r0 = this.currentTrackOutput.sampleData(r13, this.currentSampleBytesRemaining, true);
        if (r0 != (-1)) goto L26;
        return -1;
    L26:
        this.currentSampleBytesRemaining -= r0;
        if (this.currentSampleBytesRemaining <= 0) goto L29;
        return 0;
    L29:
        this.currentTrackOutput.sampleMetadata(this.timeOffsetUs + this.currentSampleTimeUs, 1, this.currentSampleSize, 0, null);
        this.currentSampleTimeUs += 20000;
        return 0;
    L31:
        this.currentSampleSize = peekNextSampleSize(r13);     // Catch: EOFException -> L21
        this.currentSampleBytesRemaining = this.currentSampleSize;
        if (this.firstSampleSize != (-1)) goto L9;
        this.firstSamplePosition = r13.getPosition();
        this.firstSampleSize = this.currentSampleSize;
    L9:
        if (this.firstSampleSize != this.currentSampleSize) goto L12;
        this.numSamplesWithSameSize++;
    L12:
        if ((this.seekMap instanceof IndexSeekMap) == false) goto L23;
        IndexSeekMap r1 = (IndexSeekMap) this.seekMap;
        long r6 = (this.timeOffsetUs + this.currentSampleTimeUs) + 20000;
        long r8 = r13.getPosition() + ((long) this.currentSampleSize);
        if (r1.isTimeUsInIndex(r6, SilenceSkippingAudioProcessor.DEFAULT_MINIMUM_SILENCE_DURATION_US) == true) goto L17;
        r1.addSeekPoint(r6, r8);
    L17:
        if (this.isSeekInProgress == false) goto L23;
        if (isSeekTimeUsWithinRange(r6, this.seekTimeUs) == false) goto L23;
        this.isSeekInProgress = false;
        this.currentTrackOutput = this.realTrackOutput;
    L22:
        return -1;
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
    public void init(ExtractorOutput r3) {
        this.extractorOutput = r3;
        this.realTrackOutput = r3.track(0, 1);
        this.currentTrackOutput = this.realTrackOutput;
        r3.endTracks();
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r6, PositionHolder r7) throws IOException {
        assertInitialized();
        if (r6.getPosition() == 0) goto L5;
    L9:
        maybeOutputFormat();
        int r0 = readSample(r6);
        maybeOutputSeekMap(r6.getLength(), r0);
        if (r0 == (-1)) goto L12;
    L14:
        return r0;
    L12:
        if ((this.seekMap instanceof IndexSeekMap) == false) goto L14;
        long r1 = this.timeOffsetUs + this.currentSampleTimeUs;
        ((IndexSeekMap) this.seekMap).setDurationUs(r1);
        this.extractorOutput.seekMap(this.seekMap);
        goto L14
    L5:
        if (readAmrHeader(r6) == true) goto L9;
        throw ParserException.createForMalformedContainer("Could not find AMR header.", null);
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r5, long r7) {
        this.currentSampleTimeUs = 0;
        this.currentSampleSize = 0;
        this.currentSampleBytesRemaining = 0;
        this.seekTimeUs = r7;
        if ((this.seekMap instanceof IndexSeekMap) == false) goto L8;
        this.timeOffsetUs = ((IndexSeekMap) this.seekMap).getTimeUs(r5);
        if (isSeekTimeUsWithinRange(this.timeOffsetUs, this.seekTimeUs) == true) goto L14;
        this.isSeekInProgress = true;
        this.currentTrackOutput = this.skippingTrackOutput;
        return;
    L14:
        return;
    L8:
        if (r5 != 0) goto L10;
    L12:
        this.timeOffsetUs = 0;
        return;
    L10:
        if ((this.seekMap instanceof ConstantBitrateSeekMap) == false) goto L12;
        this.timeOffsetUs = ((ConstantBitrateSeekMap) this.seekMap).getTimeUsAtPosition(r5);
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r2) throws IOException {
        return readAmrHeader(r2);
    }
}
