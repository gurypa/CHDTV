package androidx.media3.extractor.ogg;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.FlacFrameReader;
import androidx.media3.extractor.FlacMetadataReader;
import androidx.media3.extractor.FlacSeekTableSeekMap;
import androidx.media3.extractor.FlacStreamMetadata;
import androidx.media3.extractor.SeekMap;
import java.util.Arrays;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;

/* JADX INFO: loaded from: classes.dex */
final class FlacReader extends StreamReader {
    private static final byte AUDIO_PACKET_TYPE = -1;
    private static final int FRAME_HEADER_SAMPLE_NUMBER_OFFSET = 4;
    private FlacOggSeeker flacOggSeeker;
    private FlacStreamMetadata streamMetadata;

    private static final class FlacOggSeeker implements OggSeeker {
        private long firstFrameOffset;
        private long pendingSeekGranule;
        private FlacStreamMetadata.SeekTable seekTable;
        private FlacStreamMetadata streamMetadata;

        public FlacOggSeeker(FlacStreamMetadata r3, FlacStreamMetadata.SeekTable r4) {
            this.streamMetadata = r3;
            this.seekTable = r4;
            this.firstFrameOffset = -1;
            this.pendingSeekGranule = -1;
        }

        @Override // androidx.media3.extractor.ogg.OggSeeker
        public SeekMap createSeekMap() {
            if (this.firstFrameOffset == (-1)) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkState(r0);
            return new FlacSeekTableSeekMap(this.streamMetadata, this.firstFrameOffset);
        L5:
            r0 = false;
            goto L6
        }

        @Override // androidx.media3.extractor.ogg.OggSeeker
        public long read(ExtractorInput r8) {
            if (this.pendingSeekGranule < 0) goto L6;
            long r3 = -(this.pendingSeekGranule + 2);
            this.pendingSeekGranule = -1;
            return r3;
        L6:
            return -1;
        }

        public void setFirstFrameOffset(long r1) {
            this.firstFrameOffset = r1;
        }

        @Override // androidx.media3.extractor.ogg.OggSeeker
        public void startSeek(long r5) {
            long[] r0 = this.seekTable.pointSampleNumbers;
            this.pendingSeekGranule = r0[Util.binarySearchFloor(r0, r5, true, true)];
        }
    }

    FlacReader() {
    }

    private int getFlacFrameBlockSize(ParsableByteArray r4) {
        int r0 = (r4.getData()[2] & 255) >> 4;
        if (r0 != 6) goto L5;
    L6:
        r4.skipBytes(4);
        r4.readUtf8EncodedLong();
    L7:
        int r1 = FlacFrameReader.readFrameBlockSizeSamplesFromKey(r4, r0);
        r4.setPosition(0);
        return r1;
    L5:
        if (r0 != 7) goto L7;
        goto L6
    }

    private static boolean isAudioPacket(byte[] r3) {
        if (r3[0] != (-1)) goto L6;
        return true;
    L6:
        return false;
    }

    public static boolean verifyBitstreamType(ParsableByteArray r4) {
        if (r4.bytesLeft() >= 5) goto L5;
    L9:
        return false;
    L5:
        if (r4.readUnsignedByte() != 127) goto L9;
        if (r4.readUnsignedInt() != 1179402563) goto L9;
        return true;
    }

    @Override // androidx.media3.extractor.ogg.StreamReader
    protected long preparePayload(ParsableByteArray r3) {
        if (isAudioPacket(r3.getData()) == true) goto L7;
        return -1;
    L7:
        return getFlacFrameBlockSize(r3);
    }

    @Override // androidx.media3.extractor.ogg.StreamReader
    @EnsuresNonNullIf(expression = {"#3.format"}, result = false)
    protected boolean readHeaders(ParsableByteArray r7, long r8, StreamReader.SetupData r10) {
        byte[] r0 = r7.getData();
        FlacStreamMetadata r1 = this.streamMetadata;
        if (r1 != null) goto L7;
        FlacStreamMetadata r3 = new FlacStreamMetadata(r0, 17);
        this.streamMetadata = r3;
        r10.format = r3.getFormat(Arrays.copyOfRange(r0, 9, r7.limit()), null);
        return true;
    L7:
        if ((r0[0] & 127) != 3) goto L11;
        FlacStreamMetadata.SeekTable r4 = FlacMetadataReader.readSeekTableMetadataBlock(r7);
        FlacStreamMetadata r2 = r1.copyWithSeekTable(r4);
        this.streamMetadata = r2;
        this.flacOggSeeker = new FlacOggSeeker(r2, r4);
        return true;
    L11:
        if (isAudioPacket(r0) == true) goto L13;
        return true;
    L13:
        if (this.flacOggSeeker == null) goto L15;
        this.flacOggSeeker.setFirstFrameOffset(r8);
        r10.oggSeeker = this.flacOggSeeker;
    L15:
        Assertions.checkNotNull(r10.format);
        return false;
    }

    @Override // androidx.media3.extractor.ogg.StreamReader
    protected void reset(boolean r2) {
        super.reset(r2);
        if (r2 == false) goto L6;
        this.streamMetadata = null;
        this.flacOggSeeker = null;
        return;
    }
}
