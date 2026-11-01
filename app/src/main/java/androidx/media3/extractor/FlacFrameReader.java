package androidx.media3.extractor;

import androidx.media3.common.ParserException;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.p001ts.PsExtractor;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class FlacFrameReader {

    public static final class SampleNumberHolder {
        public long sampleNumber;

        public SampleNumberHolder() {
        }
    }

    private FlacFrameReader() {
    }

    private static boolean checkAndReadBlockSizeSamples(ParsableByteArray r2, FlacStreamMetadata r3, int r4) {
        int r0 = readFrameBlockSizeSamplesFromKey(r2, r4);
        if (r0 != (-1)) goto L5;
    L7:
        return false;
    L5:
        if (r0 > r3.maxBlockSizeSamples) goto L7;
        return true;
    }

    private static boolean checkAndReadCrc(ParsableByteArray r5, int r6) {
        if (r5.readUnsignedByte() != Util.crc8(r5.getData(), r6, r5.getPosition() - 1, 0)) goto L6;
        return true;
    L6:
        return false;
    }

    private static boolean checkAndReadFirstSampleNumber(ParsableByteArray r4, FlacStreamMetadata r5, boolean r6, SampleNumberHolder r7) {
        long r0 = r4.readUtf8EncodedLong();     // Catch: NumberFormatException -> L8
        if (r6 == false) goto L5;
        long r2 = r0;
    L6:
        r7.sampleNumber = r2;
        return true;
    L5:
        r2 = ((long) r5.maxBlockSizeSamples) * r0;
    L9:
        return false;
    }

    public static boolean checkAndReadFrameHeader(ParsableByteArray r18, FlacStreamMetadata r19, int r20, SampleNumberHolder r21) {
        int r2 = r18.getPosition();
        long r3 = r18.readUnsignedInt();
        if ((r3 >>> 16) == r20) goto L6;
        return false;
    L6:
        if (((r3 >>> 16) & 1) != 1) goto L8;
        boolean r5 = true;
    L9:
        int r11 = (int) ((r3 >> 12) & 15);
        int r6 = (int) ((r3 >> 8) & 15);
        int r7 = (int) (15 & (r3 >> 4));
        int r13 = (int) ((r3 >> 1) & 7);
        if ((r3 & 1) != 1) goto L12;
        boolean r9 = true;
    L14:
        if (checkChannelAssignment(r7, r19) == false) goto L29;
        if (checkBitsPerSample(r13, r19) == false) goto L29;
        if (r9 == true) goto L29;
        if (checkAndReadFirstSampleNumber(r18, r19, r5, r21) == false) goto L29;
        if (checkAndReadBlockSizeSamples(r18, r19, r11) == false) goto L29;
        if (checkAndReadSampleRate(r18, r19, r6) == false) goto L29;
        if (checkAndReadCrc(r18, r2) == false) goto L29;
        return true;
    L29:
        return false;
    L12:
        r9 = false;
        goto L14
    L8:
        r5 = false;
        goto L9
    }

    private static boolean checkAndReadSampleRate(ParsableByteArray r5, FlacStreamMetadata r6, int r7) {
        int r0 = r6.sampleRate;
        if (r7 != 0) goto L6;
        return true;
    L6:
        if (r7 > 11) goto L13;
        if (r7 != r6.sampleRateLookupKey) goto L11;
        return true;
    L11:
        return false;
    L13:
        if (r7 != 12) goto L20;
        if ((r5.readUnsignedByte() * 1000) != r0) goto L18;
        return true;
    L18:
        return false;
    L20:
        if (r7 > 14) goto L28;
        int r4 = r5.readUnsignedShort();
        if (r7 != 14) goto L24;
        r4 = r4 * 10;
    L24:
        if (r4 != r0) goto L27;
        return true;
    L27:
        return false;
    L28:
        return false;
    }

    private static boolean checkBitsPerSample(int r2, FlacStreamMetadata r3) {
        if (r2 != 0) goto L6;
        return true;
    L6:
        if (r2 == r3.bitsPerSampleLookupKey) goto L10;
        return false;
    L10:
        return true;
    }

    private static boolean checkChannelAssignment(int r4, FlacStreamMetadata r5) {
        if (r4 > 7) goto L9;
        if (r4 == (r5.channels - 1)) goto L7;
        return false;
    L7:
        return true;
    L9:
        if (r4 <= 10) goto L11;
        return false;
    L11:
        if (r5.channels == 2) goto L13;
        return false;
    L13:
        return true;
    }

    public static boolean checkFrameHeaderFromPeek(ExtractorInput r9, FlacStreamMetadata r10, int r11, SampleNumberHolder r12) throws IOException {
        long r0 = r9.getPeekPosition();
        byte[] r3 = new byte[2];
        r9.peekFully(r3, 0, 2);
        if ((((r3[0] & 255) << 8) | (r3[1] & 255)) == r11) goto L6;
        r9.resetPeekPosition();
        r9.advancePeekPosition((int) (r0 - r9.getPosition()));
        return false;
    L6:
        ParsableByteArray r6 = new ParsableByteArray(16);
        System.arraycopy(r3, 0, r6.getData(), 0, 2);
        r6.setLimit(ExtractorUtil.peekToLength(r9, r6.getData(), 2, 14));
        r9.resetPeekPosition();
        r9.advancePeekPosition((int) (r0 - r9.getPosition()));
        return checkAndReadFrameHeader(r6, r10, r11, r12);
    }

    public static long getFirstSampleNumber(ExtractorInput r8, FlacStreamMetadata r9) throws IOException {
        r8.resetPeekPosition();
        boolean r0 = true;
        r8.advancePeekPosition(1);
        byte[] r1 = new byte[1];
        r8.peekFully(r1, 0, 1);
        if ((r1[0] & 1) == 1) goto L6;
        r0 = false;
    L6:
        r8.advancePeekPosition(2);
        if (r0 == false) goto L9;
        int r3 = 7;
    L10:
        ParsableByteArray r4 = new ParsableByteArray(r3);
        r4.setLimit(ExtractorUtil.peekToLength(r8, r4.getData(), 0, r3));
        r8.resetPeekPosition();
        SampleNumberHolder r5 = new SampleNumberHolder();
        if (checkAndReadFirstSampleNumber(r4, r9, r0, r5) == false) goto L15;
        return r5.sampleNumber;
    L15:
        throw ParserException.createForMalformedContainer(null, null);
    L9:
        r3 = 6;
        goto L10
    }

    public static int readFrameBlockSizeSamplesFromKey(ParsableByteArray r2, int r3) {
        switch(r3) {
            case 1: goto L13;
            case 2: goto L12;
            case 3: goto L12;
            case 4: goto L12;
            case 5: goto L12;
            case 6: goto L10;
            case 7: goto L8;
            case 8: goto L6;
            case 9: goto L6;
            case 10: goto L6;
            case 11: goto L6;
            case 12: goto L6;
            case 13: goto L6;
            case 14: goto L6;
            case 15: goto L6;
            default: goto L3;
        };
    L3:
        return -1;
    L13:
        return PsExtractor.AUDIO_STREAM;
    L6:
        return 256 << (r3 - 8);
    L8:
        return r2.readUnsignedShort() + 1;
    L10:
        return r2.readUnsignedByte() + 1;
    L12:
        return 576 << (r3 - 2);
    }
}
