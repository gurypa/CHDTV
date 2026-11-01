package androidx.media3.extractor;

import androidx.media3.common.Metadata;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.metadata.flac.PictureFrame;
import androidx.media3.extractor.metadata.id3.Id3Decoder;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class FlacMetadataReader {
    private static final int SEEK_POINT_SIZE = 18;
    private static final int STREAM_MARKER = 1716281667;
    private static final int SYNC_CODE = 16382;

    public static final class FlacStreamMetadataHolder {
        public FlacStreamMetadata flacStreamMetadata;

        public FlacStreamMetadataHolder(FlacStreamMetadata r1) {
            this.flacStreamMetadata = r1;
        }
    }

    private FlacMetadataReader() {
    }

    public static boolean checkAndPeekStreamMarker(ExtractorInput r6) throws IOException {
        ParsableByteArray r0 = new ParsableByteArray(4);
        r6.peekFully(r0.getData(), 0, 4);
        if (r0.readUnsignedInt() != 1716281667) goto L6;
        return true;
    L6:
        return false;
    }

    public static int getFrameStartMarker(ExtractorInput r5) throws IOException {
        r5.resetPeekPosition();
        ParsableByteArray r0 = new ParsableByteArray(2);
        r5.peekFully(r0.getData(), 0, 2);
        int r1 = r0.readUnsignedShort();
        if ((r1 >> 2) != SYNC_CODE) goto L6;
        r5.resetPeekPosition();
        return r1;
    L6:
        r5.resetPeekPosition();
        throw ParserException.createForMalformedContainer("First frame does not start with sync code.", null);
    }

    public static Metadata peekId3Metadata(ExtractorInput r4, boolean r5) throws IOException {
        if (r5 == false) goto L5;
        Id3Decoder.FramePredicate r1 = null;
    L6:
        Metadata r2 = new Id3Peeker().peekId3Data(r4, r1);
        if (r2 != null) goto L9;
        return null;
    L9:
        if (r2.length() != 0) goto L12;
        return null;
    L12:
        return r2;
    L5:
        r1 = Id3Decoder.NO_FRAMES_PREDICATE;
        goto L6
    }

    public static Metadata readId3Metadata(ExtractorInput r5, boolean r6) throws IOException {
        r5.resetPeekPosition();
        long r0 = r5.getPeekPosition();
        Metadata r2 = peekId3Metadata(r5, r6);
        r5.skipFully((int) (r5.getPeekPosition() - r0));
        return r2;
    }

    public static boolean readMetadataBlock(ExtractorInput r9, FlacStreamMetadataHolder r10) throws IOException {
        r9.resetPeekPosition();
        ParsableBitArray r0 = new ParsableBitArray(new byte[4]);
        r9.peekFully(r0.data, 0, 4);
        boolean r2 = r0.readBit();
        int r4 = r0.readBits(7);
        int r5 = r0.readBits(24) + 4;
        if (r4 != 0) goto L5;
        r10.flacStreamMetadata = readStreamInfoBlock(r9);
    L16:
        return r2;
    L5:
        FlacStreamMetadata r6 = r10.flacStreamMetadata;
        if (r6 == null) goto L18;
        if (r4 != 3) goto L10;
        r10.flacStreamMetadata = r6.copyWithSeekTable(readSeekTableMetadataBlock(r9, r5));
        goto L16
    L10:
        if (r4 != 4) goto L13;
        r10.flacStreamMetadata = r6.copyWithVorbisComments(readVorbisCommentMetadataBlock(r9, r5));
        goto L16
    L13:
        if (r4 != 6) goto L15;
        ParsableByteArray r7 = new ParsableByteArray(r5);
        r9.readFully(r7.getData(), 0, r5);
        r7.skipBytes(4);
        r10.flacStreamMetadata = r6.copyWithPictureFrames(ImmutableList.m213of(PictureFrame.fromPictureBlock(r7)));
        goto L16
    L15:
        r9.skipFully(r5);
        goto L16
    L18:
        throw new IllegalArgumentException();
    }

    public static FlacStreamMetadata.SeekTable readSeekTableMetadataBlock(ParsableByteArray r11) {
        r11.skipBytes(1);
        int r0 = r11.readUnsignedInt24();
        long r1 = ((long) r11.getPosition()) + ((long) r0);
        int r3 = r0 / 18;
        long[] r4 = new long[r3];
        long[] r5 = new long[r3];
        int r6 = 0;
    L3:
        if (r6 >= r3) goto L8;
        long r7 = r11.readLong();
        if (r7 == (-1)) goto L6;
        r4[r6] = r7;
        r5[r6] = r11.readLong();
        r11.skipBytes(2);
        r6 = r6 + 1;
        goto L3
    L6:
        r4 = Arrays.copyOf(r4, r6);
        r5 = Arrays.copyOf(r5, r6);
    L8:
        r11.skipBytes((int) (r1 - ((long) r11.getPosition())));
        return new FlacStreamMetadata.SeekTable(r4, r5);
    }

    private static FlacStreamMetadata.SeekTable readSeekTableMetadataBlock(ExtractorInput r3, int r4) throws IOException {
        ParsableByteArray r0 = new ParsableByteArray(r4);
        r3.readFully(r0.getData(), 0, r4);
        return readSeekTableMetadataBlock(r0);
    }

    private static FlacStreamMetadata readStreamInfoBlock(ExtractorInput r3) throws IOException {
        byte[] r1 = new byte[38];
        r3.readFully(r1, 0, 38);
        return new FlacStreamMetadata(r1, 4);
    }

    public static void readStreamMarker(ExtractorInput r5) throws IOException {
        ParsableByteArray r0 = new ParsableByteArray(4);
        r5.readFully(r0.getData(), 0, 4);
        if (r0.readUnsignedInt() != 1716281667) goto L6;
        return;
    L6:
        throw ParserException.createForMalformedContainer("Failed to read FLAC stream marker.", null);
    }

    private static List<String> readVorbisCommentMetadataBlock(ExtractorInput r3, int r4) throws IOException {
        ParsableByteArray r0 = new ParsableByteArray(r4);
        r3.readFully(r0.getData(), 0, r4);
        r0.skipBytes(4);
        return Arrays.asList(VorbisUtil.readVorbisCommentHeader(r0, false, false).comments);
    }
}
