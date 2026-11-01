package androidx.media3.extractor.wav;

import android.util.Pair;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.WavUtil;
import androidx.media3.extractor.p001ts.TsExtractor;
import java.io.IOException;
import java.util.List;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class WavExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = null;
    private static final int STATE_READING_FILE_TYPE = 0;
    private static final int STATE_READING_FORMAT = 2;
    private static final int STATE_READING_RF64_SAMPLE_DATA_SIZE = 1;
    private static final int STATE_READING_SAMPLE_DATA = 4;
    private static final int STATE_SKIPPING_TO_SAMPLE_DATA = 3;
    private static final String TAG = "WavExtractor";
    private static final int TARGET_SAMPLES_PER_SECOND = 10;
    private long dataEndPosition;
    private int dataStartPosition;
    private ExtractorOutput extractorOutput;
    private OutputWriter outputWriter;
    private long rf64SampleDataSize;
    private int state;
    private TrackOutput trackOutput;

    private static final class ImaAdPcmOutputWriter implements OutputWriter {
        private static final int[] INDEX_TABLE = null;
        private static final int[] STEP_TABLE = null;
        private final ParsableByteArray decodedData;
        private final ExtractorOutput extractorOutput;
        private final Format format;
        private final int framesPerBlock;
        private final byte[] inputData;
        private long outputFrameCount;
        private int pendingInputBytes;
        private int pendingOutputBytes;
        private long startTimeUs;
        private final int targetSampleSizeFrames;
        private final TrackOutput trackOutput;
        private final WavFormat wavFormat;

        static {
            INDEX_TABLE = new int[]{-1, -1, -1, -1, 2, 4, 6, 8, -1, -1, -1, -1, 2, 4, 6, 8};
            STEP_TABLE = new int[]{7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 19, 21, 23, 25, 28, 31, 34, 37, 41, 45, 50, 55, 60, 66, 73, 80, 88, 97, 107, 118, TsExtractor.TS_STREAM_TYPE_HDMV_DTS, 143, 157, 173, 190, 209, 230, 253, 279, 307, 337, 371, 408, 449, 494, 544, 598, 658, 724, 796, 876, 963, 1060, 1166, 1282, 1411, 1552, 1707, 1878, 2066, 2272, 2499, 2749, 3024, 3327, 3660, 4026, 4428, 4871, 5358, 5894, 6484, 7132, 7845, 8630, 9493, 10442, 11487, 12635, 13899, 15289, 16818, 18500, 20350, 22385, 24623, 27086, 29794, 32767};
        }

        public ImaAdPcmOutputWriter(ExtractorOutput r8, TrackOutput r9, WavFormat r10) throws ParserException {
            this.extractorOutput = r8;
            this.trackOutput = r9;
            this.wavFormat = r10;
            this.targetSampleSizeFrames = Math.max(1, r10.frameRateHz / 10);
            ParsableByteArray r0 = new ParsableByteArray(r10.extraData);
            r0.readLittleEndianUnsignedShort();
            this.framesPerBlock = r0.readLittleEndianUnsignedShort();
            int r2 = r10.numChannels;
            int r3 = (((r10.blockSize - (r2 * 4)) * 8) / (r10.bitsPerSample * r2)) + 1;
            if (this.framesPerBlock != r3) goto L7;
            int r1 = Util.ceilDivide(this.targetSampleSizeFrames, this.framesPerBlock);
            this.inputData = new byte[r10.blockSize * r1];
            this.decodedData = new ParsableByteArray(numOutputFramesToBytes(this.framesPerBlock, r2) * r1);
            int r4 = ((r10.frameRateHz * r10.blockSize) * 8) / this.framesPerBlock;
            this.format = new Format.Builder().setSampleMimeType(MimeTypes.AUDIO_RAW).setAverageBitrate(r4).setPeakBitrate(r4).setMaxInputSize(numOutputFramesToBytes(this.targetSampleSizeFrames, r2)).setChannelCount(r10.numChannels).setSampleRate(r10.frameRateHz).setPcmEncoding(2).build();
            return;
        L7:
            throw ParserException.createForMalformedContainer("Expected frames per block: " + r3 + "; got: " + this.framesPerBlock, null);
        }

        private void decode(byte[] r4, int r5, ParsableByteArray r6) {
            int r0 = 0;
        L3:
            if (r0 >= r5) goto L9;
            int r1 = 0;
        L6:
            if (r1 >= this.wavFormat.numChannels) goto L8;
            decodeBlockForChannel(r4, r0, r1, r6.getData());
            r1 = r1 + 1;
            goto L6
        L8:
            r0 = r0 + 1;
            goto L3
        L9:
            int r2 = numOutputFramesToBytes(this.framesPerBlock * r5);
            r6.setPosition(0);
            r6.setLimit(r2);
        }

        private void decodeBlockForChannel(byte[] r20, int r21, int r22, byte[] r23) {
            int r1 = this.wavFormat.blockSize;
            int r2 = this.wavFormat.numChannels;
            int r4 = (r22 * 4) + (r21 * r1);
            int r5 = (r2 * 4) + r4;
            int r6 = (r1 / r2) - 4;
            int r7 = (short) (((r20[r4 + 1] & 255) << 8) | (r20[r4] & 255));
            int r8 = Math.min(r20[r4 + 2] & 255, 88);
            int r9 = STEP_TABLE[r8];
            int r10 = (((this.framesPerBlock * r21) * r2) + r22) * 2;
            r23[r10] = (byte) (r7 & 255);
            r23[r10 + 1] = (byte) (r7 >> 8);
            int r11 = 0;
        L4:
            if (r11 >= (r6 * 2)) goto L13;
            int r15 = r20[((((r11 / 8) * r2) * 4) + r5) + ((r11 / 2) % 4)] & 255;
            if ((r11 % 2) != 0) goto L8;
            int r16 = r15 & 15;
        L9:
            int r0 = ((((r16 & 7) * 2) + 1) * r9) >> 3;
            if ((r16 & 8) == 0) goto L12;
            r0 = -r0;
        L12:
            r7 = Util.constrainValue(r7 + r0, -32768, 32767);
            r10 = r10 + (r2 * 2);
            r23[r10] = (byte) (r7 & 255);
            r23[r10 + 1] = (byte) (r7 >> 8);
            r8 = Util.constrainValue(r8 + INDEX_TABLE[r16], 0, STEP_TABLE.length - 1);
            r9 = STEP_TABLE[r8];
            r11 = r11 + 1;
            r1 = r1;
            goto L4
        L8:
            r16 = r15 >> 4;
            goto L9
        }

        private int numOutputBytesToFrames(int r2) {
            return r2 / (this.wavFormat.numChannels * 2);
        }

        private int numOutputFramesToBytes(int r2) {
            return numOutputFramesToBytes(r2, this.wavFormat.numChannels);
        }

        private static int numOutputFramesToBytes(int r1, int r2) {
            return (r1 * 2) * r2;
        }

        private void writeSampleMetadata(int r12) {
            long r5 = this.startTimeUs + Util.scaleLargeTimestamp(this.outputFrameCount, 1000000, this.wavFormat.frameRateHz);
            int r8 = numOutputFramesToBytes(r12);
            this.trackOutput.sampleMetadata(r5, 1, r8, this.pendingOutputBytes - r8, null);
            this.outputFrameCount += (long) r12;
            this.pendingOutputBytes -= r8;
        }

        @Override // androidx.media3.extractor.wav.WavExtractor.OutputWriter
        public void init(int r9, long r10) {
            this.extractorOutput.seekMap(new WavSeekMap(this.wavFormat, this.framesPerBlock, r9, r10));
            this.trackOutput.format(this.format);
        }

        @Override // androidx.media3.extractor.wav.WavExtractor.OutputWriter
        public void reset(long r3) {
            this.pendingInputBytes = 0;
            this.startTimeUs = r3;
            this.pendingOutputBytes = 0;
            this.outputFrameCount = 0;
        }

        @Override // androidx.media3.extractor.wav.WavExtractor.OutputWriter
        public boolean sampleData(ExtractorInput r9, long r10) throws IOException {
            int r2 = this.wavFormat.blockSize * Util.ceilDivide(this.targetSampleSizeFrames - numOutputBytesToFrames(this.pendingOutputBytes), this.framesPerBlock);
            if (r10 != 0) goto L5;
            boolean r3 = true;
        L6:
            if (r3 == true) goto L14;
            if (this.pendingInputBytes >= r2) goto L14;
            int r5 = r9.read(this.inputData, this.pendingInputBytes, (int) Math.min(r2 - this.pendingInputBytes, r10));
            if (r5 == (-1)) goto L11;
            this.pendingInputBytes += r5;
            goto L6
        L11:
            r3 = true;
        L14:
            int r4 = this.pendingInputBytes / this.wavFormat.blockSize;
            if (r4 <= 0) goto L19;
            decode(this.inputData, r4, this.decodedData);
            this.pendingInputBytes -= this.wavFormat.blockSize * r4;
            int r6 = this.decodedData.limit();
            this.trackOutput.sampleData(this.decodedData, r6);
            this.pendingOutputBytes += r6;
            if (numOutputBytesToFrames(this.pendingOutputBytes) < this.targetSampleSizeFrames) goto L19;
            writeSampleMetadata(this.targetSampleSizeFrames);
        L19:
            if (r3 == false) goto L23;
            int r7 = numOutputBytesToFrames(this.pendingOutputBytes);
            if (r7 <= 0) goto L23;
            writeSampleMetadata(r7);
        L23:
            return r3;
        L5:
            r3 = false;
            goto L6
        }
    }

    private interface OutputWriter {
        void init(int r1, long r2) throws ParserException;

        void reset(long r1);

        boolean sampleData(ExtractorInput r1, long r2) throws IOException;
    }

    private static final class PassthroughOutputWriter implements OutputWriter {
        private final ExtractorOutput extractorOutput;
        private final Format format;
        private long outputFrameCount;
        private int pendingOutputBytes;
        private long startTimeUs;
        private final int targetSampleSizeBytes;
        private final TrackOutput trackOutput;
        private final WavFormat wavFormat;

        public PassthroughOutputWriter(ExtractorOutput r5, TrackOutput r6, WavFormat r7, String r8, int r9) throws ParserException {
            this.extractorOutput = r5;
            this.trackOutput = r6;
            this.wavFormat = r7;
            int r0 = (r7.numChannels * r7.bitsPerSample) / 8;
            if (r7.blockSize != r0) goto L7;
            int r1 = (r7.frameRateHz * r0) * 8;
            this.targetSampleSizeBytes = Math.max(r0, (r7.frameRateHz * r0) / 10);
            this.format = new Format.Builder().setSampleMimeType(r8).setAverageBitrate(r1).setPeakBitrate(r1).setMaxInputSize(this.targetSampleSizeBytes).setChannelCount(r7.numChannels).setSampleRate(r7.frameRateHz).setPcmEncoding(r9).build();
            return;
        L7:
            throw ParserException.createForMalformedContainer("Expected block size: " + r0 + "; got: " + r7.blockSize, null);
        }

        @Override // androidx.media3.extractor.wav.WavExtractor.OutputWriter
        public void init(int r9, long r10) {
            this.extractorOutput.seekMap(new WavSeekMap(this.wavFormat, 1, r9, r10));
            this.trackOutput.format(this.format);
        }

        @Override // androidx.media3.extractor.wav.WavExtractor.OutputWriter
        public void reset(long r3) {
            this.startTimeUs = r3;
            this.pendingOutputBytes = 0;
            this.outputFrameCount = 0;
        }

        @Override // androidx.media3.extractor.wav.WavExtractor.OutputWriter
        public boolean sampleData(ExtractorInput r21, long r22) throws IOException {
            long r1 = r22;
        L4:
            if (r1 <= 0) goto L12;
            if (this.pendingOutputBytes >= this.targetSampleSizeBytes) goto L12;
            int r4 = this.trackOutput.sampleData(r21, (int) Math.min(this.targetSampleSizeBytes - this.pendingOutputBytes, r1), true);
            if (r4 == (-1)) goto L9;
            this.pendingOutputBytes += r4;
            r1 = r1 - ((long) r4);
            goto L4
        L9:
            r1 = 0;
        L12:
            int r7 = this.wavFormat.blockSize;
            int r8 = this.pendingOutputBytes / r7;
            if (r8 <= 0) goto L16;
            long r14 = this.startTimeUs + Util.scaleLargeTimestamp(this.outputFrameCount, 1000000, this.wavFormat.frameRateHz);
            int r17 = r8 * r7;
            int r18 = this.pendingOutputBytes - r17;
            this.trackOutput.sampleMetadata(r14, 1, r17, r18, null);
            this.outputFrameCount += (long) r8;
            this.pendingOutputBytes = r18;
        L16:
            if (r1 <= 0) goto L27;
            return false;
        L27:
            return true;
        }
    }

    static {
        FACTORY = new WavExtractor$$ExternalSyntheticLambda0();
    }

    public WavExtractor() {
        this.state = 0;
        this.rf64SampleDataSize = -1;
        this.dataStartPosition = -1;
        this.dataEndPosition = -1;
    }

    @EnsuresNonNull({"extractorOutput", "trackOutput"})
    private void assertInitialized() {
        Assertions.checkStateNotNull(this.trackOutput);
        Util.castNonNull(this.extractorOutput);
    }

    static /* synthetic */ Extractor[] lambda$static$0() {
        return new Extractor[]{new WavExtractor()};
    }

    private void readFileType(ExtractorInput r7) throws IOException {
        if (r7.getPosition() != 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        if (this.dataStartPosition == (-1)) goto L11;
        r7.skipFully(this.dataStartPosition);
        this.state = 4;
        return;
    L11:
        if (WavHeaderReader.checkFileType(r7) == false) goto L15;
        r7.skipFully((int) (r7.getPeekPosition() - r7.getPosition()));
        this.state = 1;
        return;
    L15:
        throw ParserException.createForMalformedContainer("Unsupported or unrecognized wav file type.", null);
    L5:
        r0 = false;
        goto L6
    }

    @RequiresNonNull({"extractorOutput", "trackOutput"})
    private void readFormat(ExtractorInput r7) throws IOException {
        WavFormat r3 = WavHeaderReader.readFormat(r7);
        if (r3.formatType != 17) goto L6;
        this.outputWriter = new ImaAdPcmOutputWriter(this.extractorOutput, this.trackOutput, r3);
    L14:
        this.state = 3;
        return;
    L6:
        if (r3.formatType != 6) goto L9;
        this.outputWriter = new PassthroughOutputWriter(this.extractorOutput, this.trackOutput, r3, MimeTypes.AUDIO_ALAW, -1);
        goto L14
    L9:
        if (r3.formatType != 7) goto L11;
        this.outputWriter = new PassthroughOutputWriter(this.extractorOutput, this.trackOutput, r3, MimeTypes.AUDIO_MLAW, -1);
        goto L14
    L11:
        int r5 = WavUtil.getPcmEncodingForType(r3.formatType, r3.bitsPerSample);
        if (r5 == 0) goto L17;
        this.outputWriter = new PassthroughOutputWriter(this.extractorOutput, this.trackOutput, r3, MimeTypes.AUDIO_RAW, r5);
        goto L14
    L17:
        throw ParserException.createForUnsupportedContainerFeature("Unsupported WAV format type: " + r3.formatType);
    }

    private void readRf64SampleDataSize(ExtractorInput r3) throws IOException {
        this.rf64SampleDataSize = WavHeaderReader.readRf64SampleDataSize(r3);
        this.state = 2;
    }

    private int readSampleData(ExtractorInput r7) throws IOException {
        if (this.dataEndPosition == (-1)) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        if (((OutputWriter) Assertions.checkNotNull(this.outputWriter)).sampleData(r7, this.dataEndPosition - r7.getPosition()) == false) goto L11;
        return -1;
    L11:
        return 0;
    L5:
        r0 = false;
        goto L6
    }

    private void skipToSampleData(ExtractorInput r10) throws IOException {
        Pair<Long, Long> r0 = WavHeaderReader.skipToSampleData(r10);
        this.dataStartPosition = ((Long) r0.first).intValue();
        long r1 = ((Long) r0.second).longValue();
        if (this.rf64SampleDataSize != (-1)) goto L5;
    L7:
        this.dataEndPosition = ((long) this.dataStartPosition) + r1;
        long r3 = r10.getLength();
        if (r3 != (-1)) goto L10;
    L12:
        ((OutputWriter) Assertions.checkNotNull(this.outputWriter)).init(this.dataStartPosition, this.dataEndPosition);
        this.state = 4;
        return;
    L10:
        if (this.dataEndPosition <= r3) goto L12;
        Log.m48w(TAG, "Data exceeds input length: " + this.dataEndPosition + ", " + r3);
        this.dataEndPosition = r3;
        goto L12
    L5:
        if (r1 != 4294967295L) goto L7;
        r1 = this.rf64SampleDataSize;
        goto L7
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
        this.trackOutput = r3.track(0, 1);
        r3.endTracks();
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r3, PositionHolder r4) throws IOException {
        assertInitialized();
        switch(this.state) {
            case 0: goto L14;
            case 1: goto L12;
            case 2: goto L10;
            case 3: goto L8;
            case 4: goto L7;
            default: goto L5;
        };
    L8:
        skipToSampleData(r3);
        return 0;
    L10:
        readFormat(r3);
        return 0;
    L12:
        readRf64SampleDataSize(r3);
        return 0;
    L14:
        readFileType(r3);
        return 0;
    L5:
        throw new IllegalStateException();
    L7:
        return readSampleData(r3);
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r3, long r5) {
        if (r3 != 0) goto L5;
        int r0 = 0;
    L6:
        this.state = r0;
        if (this.outputWriter == null) goto L10;
        this.outputWriter.reset(r5);
        return;
    L10:
        return;
    L5:
        r0 = 4;
        goto L6
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r2) throws IOException {
        return WavHeaderReader.checkFileType(r2);
    }
}
