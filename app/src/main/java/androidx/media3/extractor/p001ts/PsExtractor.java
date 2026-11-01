package androidx.media3.extractor.p001ts;

import android.util.SparseArray;
import androidx.core.view.InputDeviceCompat;
import androidx.media3.common.C0180C;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import java.io.IOException;
import java.util.List;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class PsExtractor implements Extractor {
    public static final int AUDIO_STREAM = 192;
    public static final int AUDIO_STREAM_MASK = 224;
    public static final ExtractorsFactory FACTORY = null;
    private static final long MAX_SEARCH_LENGTH = 1048576;
    private static final long MAX_SEARCH_LENGTH_AFTER_AUDIO_AND_VIDEO_FOUND = 8192;
    private static final int MAX_STREAM_ID_PLUS_ONE = 256;
    static final int MPEG_PROGRAM_END_CODE = 441;
    static final int PACKET_START_CODE_PREFIX = 1;
    static final int PACK_START_CODE = 442;
    public static final int PRIVATE_STREAM_1 = 189;
    static final int SYSTEM_HEADER_START_CODE = 443;
    public static final int VIDEO_STREAM = 224;
    public static final int VIDEO_STREAM_MASK = 240;
    private final PsDurationReader durationReader;
    private boolean foundAllTracks;
    private boolean foundAudioTrack;
    private boolean foundVideoTrack;
    private boolean hasOutputSeekMap;
    private long lastTrackPosition;
    private ExtractorOutput output;
    private PsBinarySearchSeeker psBinarySearchSeeker;
    private final ParsableByteArray psPacketBuffer;
    private final SparseArray<PesReader> psPayloadReaders;
    private final TimestampAdjuster timestampAdjuster;

    private static final class PesReader {
        private static final int PES_SCRATCH_SIZE = 64;
        private boolean dtsFlag;
        private int extendedHeaderLength;
        private final ElementaryStreamReader pesPayloadReader;
        private final ParsableBitArray pesScratch;
        private boolean ptsFlag;
        private boolean seenFirstDts;
        private long timeUs;
        private final TimestampAdjuster timestampAdjuster;

        public PesReader(ElementaryStreamReader r3, TimestampAdjuster r4) {
            this.pesPayloadReader = r3;
            this.timestampAdjuster = r4;
            this.pesScratch = new ParsableBitArray(new byte[64]);
        }

        private void parseHeader() {
            this.pesScratch.skipBits(8);
            this.ptsFlag = this.pesScratch.readBit();
            this.dtsFlag = this.pesScratch.readBit();
            this.pesScratch.skipBits(6);
            this.extendedHeaderLength = this.pesScratch.readBits(8);
        }

        private void parseHeaderExtension() {
            this.timeUs = 0;
            if (this.ptsFlag == false) goto L11;
            this.pesScratch.skipBits(4);
            long r3 = ((long) this.pesScratch.readBits(3)) << 30;
            this.pesScratch.skipBits(1);
            long r4 = r3 | ((long) (this.pesScratch.readBits(15) << 15));
            this.pesScratch.skipBits(1);
            long r5 = r4 | ((long) this.pesScratch.readBits(15));
            this.pesScratch.skipBits(1);
            if (this.seenFirstDts == false) goto L7;
        L9:
            this.timeUs = this.timestampAdjuster.adjustTsTimestamp(r5);
            return;
        L7:
            if (this.dtsFlag == false) goto L9;
            this.pesScratch.skipBits(4);
            long r0 = ((long) this.pesScratch.readBits(3)) << 30;
            this.pesScratch.skipBits(1);
            long r1 = r0 | ((long) (this.pesScratch.readBits(15) << 15));
            this.pesScratch.skipBits(1);
            long r2 = r1 | ((long) this.pesScratch.readBits(15));
            this.pesScratch.skipBits(1);
            this.timestampAdjuster.adjustTsTimestamp(r2);
            this.seenFirstDts = true;
            goto L9
        }

        public void consume(ParsableByteArray r6) throws ParserException {
            r6.readBytes(this.pesScratch.data, 0, 3);
            this.pesScratch.setPosition(0);
            parseHeader();
            r6.readBytes(this.pesScratch.data, 0, this.extendedHeaderLength);
            this.pesScratch.setPosition(0);
            parseHeaderExtension();
            this.pesPayloadReader.packetStarted(this.timeUs, 4);
            this.pesPayloadReader.consume(r6);
            this.pesPayloadReader.packetFinished(false);
        }

        public void seek() {
            this.seenFirstDts = false;
            this.pesPayloadReader.seek();
        }
    }

    static {
        FACTORY = new PsExtractor$$ExternalSyntheticLambda0();
    }

    public PsExtractor() {
        this(new TimestampAdjuster(0));
    }

    public PsExtractor(TimestampAdjuster r3) {
        this.timestampAdjuster = r3;
        this.psPacketBuffer = new ParsableByteArray(4096);
        this.psPayloadReaders = new SparseArray();
        this.durationReader = new PsDurationReader();
    }

    static /* synthetic */ Extractor[] lambda$static$0() {
        return new Extractor[]{new PsExtractor()};
    }

    @RequiresNonNull({"output"})
    private void maybeOutputSeekMap(long r8) {
        if (this.hasOutputSeekMap == true) goto L9;
        this.hasOutputSeekMap = true;
        if (this.durationReader.getDurationUs() == C0180C.TIME_UNSET) goto L7;
        this.psBinarySearchSeeker = new PsBinarySearchSeeker(this.durationReader.getScrTimestampAdjuster(), this.durationReader.getDurationUs(), r8);
        this.output.seekMap(this.psBinarySearchSeeker.getSeekMap());
        return;
    L7:
        this.output.seekMap(new SeekMap.Unseekable(this.durationReader.getDurationUs()));
        return;
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
    public void init(ExtractorOutput r1) {
        this.output = r1;
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r21, PositionHolder r22) throws IOException {
        Assertions.checkStateNotNull(this.output);
        long r3 = r21.getLength();
        if (r3 == (-1)) goto L5;
        boolean r7 = true;
    L6:
        if (r7 == true) goto L8;
    L11:
        maybeOutputSeekMap(r3);
        if (this.psBinarySearchSeeker != null) goto L14;
    L17:
        r21.resetPeekPosition();
        if (r3 == (-1)) goto L20;
        long r10 = r3 - r21.getPeekPosition();
    L22:
        if (r10 == (-1)) goto L27;
        if (r10 >= 4) goto L27;
        return -1;
    L27:
        if (r21.peekFully(this.psPacketBuffer.getData(), 0, 4, true) == true) goto L29;
        return -1;
    L29:
        this.psPacketBuffer.setPosition(0);
        int r5 = this.psPacketBuffer.readInt();
        if (r5 != MPEG_PROGRAM_END_CODE) goto L33;
        return -1;
    L33:
        if (r5 != PACK_START_CODE) goto L37;
        r21.peekFully(this.psPacketBuffer.getData(), 0, 10);
        this.psPacketBuffer.setPosition(9);
        r21.skipFully((this.psPacketBuffer.readUnsignedByte() & 7) + 14);
        return 0;
    L37:
        if (r5 != SYSTEM_HEADER_START_CODE) goto L41;
        r21.peekFully(this.psPacketBuffer.getData(), 0, 2);
        this.psPacketBuffer.setPosition(0);
        r21.skipFully(this.psPacketBuffer.readUnsignedShort() + 6);
        return 0;
    L41:
        if (((r5 & InputDeviceCompat.SOURCE_ANY) >> 8) == 1) goto L44;
        r21.skipFully(1);
        return 0;
    L44:
        int r6 = r5 & 255;
        PesReader r13 = this.psPayloadReaders.get(r6);
        if (this.foundAllTracks == true) goto L69;
        if (r13 != null) goto L60;
        ElementaryStreamReader r14 = null;
        if (r6 != 189) goto L51;
        r14 = new Ac3Reader();
        this.foundAudioTrack = true;
        this.lastTrackPosition = r21.getPosition();
    L56:
        if (r14 == null) goto L60;
        r14.createTracks(this.output, new TsPayloadReader.TrackIdGenerator(r6, 256));
        PesReader r11 = new PesReader(r14, this.timestampAdjuster);
        this.psPayloadReaders.put(r6, r11);
        r13 = r11;
        goto L60
    L51:
        if ((r6 & 224) != 192) goto L54;
        r14 = new MpegAudioReader();
        this.foundAudioTrack = true;
        this.lastTrackPosition = r21.getPosition();
        goto L56
    L54:
        if ((r6 & VIDEO_STREAM_MASK) != 224) goto L56;
        r14 = new H262Reader();
        this.foundVideoTrack = true;
        this.lastTrackPosition = r21.getPosition();
    L60:
        if (this.foundAudioTrack == true) goto L62;
    L64:
        long r9 = 1048576;
    L66:
        if (r21.getPosition() <= r9) goto L69;
        this.foundAllTracks = true;
        this.output.endTracks();
        goto L69
    L62:
        if (this.foundVideoTrack == false) goto L64;
        r9 = this.lastTrackPosition + 8192;
    L69:
        r21.peekFully(this.psPacketBuffer.getData(), 0, 2);
        this.psPacketBuffer.setPosition(0);
        int r12 = this.psPacketBuffer.readUnsignedShort() + 6;
        if (r13 != null) goto L72;
        r21.skipFully(r12);
        return 0;
    L72:
        this.psPacketBuffer.reset(r12);
        r21.readFully(this.psPacketBuffer.getData(), 0, r12);
        this.psPacketBuffer.setPosition(6);
        r13.consume(this.psPacketBuffer);
        this.psPacketBuffer.setLimit(this.psPacketBuffer.capacity());
        return 0;
    L20:
        r10 = -1;
        goto L22
    L14:
        if (this.psBinarySearchSeeker.isSeeking() == false) goto L17;
        return this.psBinarySearchSeeker.handlePendingSeek(r21, r22);
    L8:
        if (this.durationReader.isDurationReadFinished() == true) goto L11;
        return this.durationReader.readDuration(r21, r22);
    L5:
        r7 = false;
        goto L6
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r8, long r10) {
        boolean r1 = false;
        if (this.timestampAdjuster.getTimestampOffsetUs() != C0180C.TIME_UNSET) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == true) goto L15;
        long r5 = this.timestampAdjuster.getFirstSampleTimestampUs();
        if (r5 != C0180C.TIME_UNSET) goto L10;
    L14:
        r0 = r1;
        goto L15
    L10:
        if (r5 == 0) goto L14;
        if (r5 == r10) goto L14;
        r1 = true;
    L15:
        if (r0 == false) goto L18;
        this.timestampAdjuster.reset(r10);
    L18:
        if (this.psBinarySearchSeeker == null) goto L20;
        this.psBinarySearchSeeker.setSeekTargetUs(r10);
    L20:
        int r2 = 0;
    L22:
        if (r2 >= this.psPayloadReaders.size()) goto L24;
        this.psPayloadReaders.valueAt(r2).seek();
        r2 = r2 + 1;
        goto L22
    L24:
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r10) throws IOException {
        byte[] r1 = new byte[14];
        r10.peekFully(r1, 0, 14);
        if (PACK_START_CODE == (((((r1[0] & 255) << 24) | ((r1[1] & 255) << 16)) | ((r1[2] & 255) << 8)) | (r1[3] & 255))) goto L6;
        return false;
    L6:
        if ((r1[4] & 196) == 68) goto L9;
        return false;
    L9:
        if ((r1[6] & 4) == 4) goto L12;
        return false;
    L12:
        if ((r1[8] & 4) == 4) goto L15;
        return false;
    L15:
        if ((r1[9] & 1) == 1) goto L18;
        return false;
    L18:
        if ((r1[12] & 3) == 3) goto L20;
        return false;
    L20:
        r10.advancePeekPosition(r1[13] & 7);
        r10.peekFully(r1, 0, 3);
        int r5 = ((r1[0] & 255) << 16) | ((r1[1] & 255) << 8);
        if (1 == ((r1[2] & 255) | r5)) goto L23;
        return false;
    L23:
        return true;
    }
}
