package androidx.media3.extractor.wav;

import android.util.Pair;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.WavUtil;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
final class WavHeaderReader {
    private static final String TAG = "WavHeaderReader";

    private static final class ChunkHeader {
        public static final int SIZE_IN_BYTES = 8;

        /* JADX INFO: renamed from: id */
        public final int f68id;
        public final long size;

        private ChunkHeader(int r1, long r2) {
            this.f68id = r1;
            this.size = r2;
        }

        public static ChunkHeader peek(ExtractorInput r4, ParsableByteArray r5) throws IOException {
            r4.peekFully(r5.getData(), 0, 8);
            r5.setPosition(0);
            return new ChunkHeader(r5.readInt(), r5.readLittleEndianUnsignedInt());
        }
    }

    private WavHeaderReader() {
    }

    public static boolean checkFileType(ExtractorInput r6) throws IOException {
        ParsableByteArray r0 = new ParsableByteArray(8);
        ChunkHeader r1 = ChunkHeader.peek(r6, r0);
        if (r1.f68id != 1380533830) goto L5;
    L7:
        r6.peekFully(r0.getData(), 0, 4);
        r0.setPosition(0);
        int r2 = r0.readInt();
        if (r2 == 1463899717) goto L11;
        Log.m44e(TAG, "Unsupported form type: " + r2);
        return false;
    L11:
        return true;
    L5:
        if (r1.f68id == 1380333108) goto L7;
        return false;
    }

    public static WavFormat readFormat(ExtractorInput r15) throws IOException {
        ParsableByteArray r0 = new ParsableByteArray(16);
        ChunkHeader r2 = skipToChunk(WavUtil.FMT_FOURCC, r15, r0);
        if (r2.size < 16) goto L5;
        boolean r3 = true;
    L6:
        Assertions.checkState(r3);
        r15.peekFully(r0.getData(), 0, 16);
        r0.setPosition(0);
        int r6 = r0.readLittleEndianUnsignedShort();
        int r7 = r0.readLittleEndianUnsignedShort();
        int r8 = r0.readLittleEndianUnsignedIntToInt();
        int r9 = r0.readLittleEndianUnsignedIntToInt();
        int r10 = r0.readLittleEndianUnsignedShort();
        int r11 = r0.readLittleEndianUnsignedShort();
        int r4 = ((int) r2.size) - 16;
        if (r4 <= 0) goto L9;
        byte[] r1 = new byte[r4];
        r15.peekFully(r1, 0, r4);
        byte[] r12 = r1;
    L10:
        r15.skipFully((int) (r15.getPeekPosition() - r15.getPosition()));
        return new WavFormat(r6, r7, r8, r9, r10, r11, r12);
    L9:
        r12 = Util.EMPTY_BYTE_ARRAY;
        goto L10
    L5:
        r3 = false;
        goto L6
    }

    public static long readRf64SampleDataSize(ExtractorInput r7) throws IOException {
        ParsableByteArray r0 = new ParsableByteArray(8);
        ChunkHeader r2 = ChunkHeader.peek(r7, r0);
        if (r2.f68id == 1685272116) goto L6;
        r7.resetPeekPosition();
        return -1;
    L6:
        r7.advancePeekPosition(8);
        r0.setPosition(0);
        r7.peekFully(r0.getData(), 0, 8);
        long r3 = r0.readLittleEndianLong();
        r7.skipFully(((int) r2.size) + 8);
        return r3;
    }

    private static ChunkHeader skipToChunk(int r7, ExtractorInput r8, ParsableByteArray r9) throws IOException {
        ChunkHeader r0 = ChunkHeader.peek(r8, r9);
    L4:
        if (r0.f68id == r7) goto L13;
        Log.m48w(TAG, "Ignoring unknown WAV chunk: " + r0.f68id);
        long r3 = r0.size + 8;
        if ((r0.size % 2) == 0) goto L9;
        r3 = r3 + 1;
    L9:
        if (r3 > 2147483647L) goto L12;
        r8.skipFully((int) r3);
        r0 = ChunkHeader.peek(r8, r9);
        goto L4
    L12:
        throw ParserException.createForUnsupportedContainerFeature("Chunk is too large (~2GB+) to skip; id: " + r0.f68id);
    L13:
        return r0;
    }

    public static Pair<Long, Long> skipToSampleData(ExtractorInput r7) throws IOException {
        r7.resetPeekPosition();
        ChunkHeader r2 = skipToChunk(1684108385, r7, new ParsableByteArray(8));
        r7.skipFully(8);
        return Pair.create(Long.valueOf(r7.getPosition()), Long.valueOf(r2.size));
    }
}
