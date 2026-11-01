package androidx.media3.extractor;

import android.util.Base64;
import androidx.media3.common.Metadata;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.metadata.flac.PictureFrame;
import androidx.media3.extractor.metadata.vorbis.VorbisComment;
import androidx.media3.extractor.p001ts.PsExtractor;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class VorbisUtil {
    private static final String TAG = "VorbisUtil";

    public static final class CommentHeader {
        public final String[] comments;
        public final int length;
        public final String vendor;

        public CommentHeader(String r1, String[] r2, int r3) {
            this.vendor = r1;
            this.comments = r2;
            this.length = r3;
        }
    }

    public static final class Mode {
        public final boolean blockFlag;
        public final int mapping;
        public final int transformType;
        public final int windowType;

        public Mode(boolean r1, int r2, int r3, int r4) {
            this.blockFlag = r1;
            this.windowType = r2;
            this.transformType = r3;
            this.mapping = r4;
        }
    }

    public static final class VorbisIdHeader {
        public final int bitrateMaximum;
        public final int bitrateMinimum;
        public final int bitrateNominal;
        public final int blockSize0;
        public final int blockSize1;
        public final int channels;
        public final byte[] data;
        public final boolean framingFlag;
        public final int sampleRate;
        public final int version;

        public VorbisIdHeader(int r1, int r2, int r3, int r4, int r5, int r6, int r7, int r8, boolean r9, byte[] r10) {
            this.version = r1;
            this.channels = r2;
            this.sampleRate = r3;
            this.bitrateMaximum = r4;
            this.bitrateNominal = r5;
            this.bitrateMinimum = r6;
            this.blockSize0 = r7;
            this.blockSize1 = r8;
            this.framingFlag = r9;
            this.data = r10;
        }
    }

    private VorbisUtil() {
    }

    public static int[] getVorbisToAndroidChannelLayoutMapping(int r5) {
        switch(r5) {
            case 3: goto L15;
            case 4: goto L4;
            case 5: goto L13;
            case 6: goto L11;
            case 7: goto L9;
            case 8: goto L7;
            default: goto L4;
        };
    L4:
        return null;
    L7:
        return new int[]{0, 2, 1, 7, 5, 6, 3, 4};
    L9:
        return new int[]{0, 2, 1, 6, 5, 3, 4};
    L11:
        return new int[]{0, 2, 1, 5, 3, 4};
    L13:
        return new int[]{0, 2, 1, 3, 4};
    L15:
        return new int[]{0, 2, 1};
    }

    public static int iLog(int r1) {
        int r0 = 0;
    L3:
        if (r1 <= 0) goto L5;
        r0 = r0 + 1;
        r1 = r1 >>> 1;
        goto L3
    L5:
        return r0;
    }

    private static long mapType1QuantValues(long r6, long r8) {
        return (long) Math.floor(Math.pow(r6, 1.0d / r8));
    }

    public static Metadata parseVorbisComments(List<String> r8) {
        ArrayList r0 = new ArrayList();
        int r1 = 0;
    L4:
        if (r1 >= r8.size()) goto L17;
        String r2 = r8.get(r1);
        String[] r3 = Util.splitAtFirst(r2, "=");
        if (r3.length == 2) goto L9;
        Log.m48w(TAG, "Failed to parse Vorbis comment: " + r2);
    L15:
        r1 = r1 + 1;
        goto L4
    L9:
        if (r3[0].equals("METADATA_BLOCK_PICTURE") == true) goto L21;
        r0.add(new VorbisComment(r3[0], r3[1]));
        goto L15
    L21:
        r0.add(PictureFrame.fromPictureBlock(new ParsableByteArray(Base64.decode(r3[1], 0))));     // Catch: RuntimeException -> L12
    L12:
        e = move-exception;
        Log.m49w(TAG, "Failed to parse vorbis picture", e);
        goto L15
    L17:
        if (r0.isEmpty() == false) goto L20;
        return null;
    L20:
        return new Metadata(r0);
    }

    public static ImmutableList<byte[]> parseVorbisCsdFromEsdsInitializationData(byte[] r9) {
        ParsableByteArray r0 = new ParsableByteArray(r9);
        r0.skipBytes(1);
        int r2 = 0;
    L4:
        if (r0.bytesLeft() <= 0) goto L8;
        if (r0.peekUnsignedByte() != 255) goto L8;
        r2 = r2 + 255;
        r0.skipBytes(1);
    L8:
        int r3 = r2 + r0.readUnsignedByte();
        int r4 = 0;
    L10:
        if (r0.bytesLeft() <= 0) goto L14;
        if (r0.peekUnsignedByte() != 255) goto L14;
        r4 = r4 + 255;
        r0.skipBytes(1);
    L14:
        int r5 = r4 + r0.readUnsignedByte();
        byte[] r1 = new byte[r3];
        int r6 = r0.getPosition();
        System.arraycopy(r9, r6, r1, 0, r3);
        int r7 = (r6 + r3) + r5;
        int r8 = r9.length - r7;
        byte[] r10 = new byte[r8];
        System.arraycopy(r9, r7, r10, 0, r8);
        return ImmutableList.m214of(r1, r10);
    }

    private static void readFloors(VorbisBitArray r15) throws ParserException {
        int r1 = r15.readBits(6) + 1;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L37;
        int r5 = r15.readBits(16);
        switch(r5) {
            case 0: goto L32;
            case 1: goto L8;
            default: goto L7;
        };
    L8:
        int r4 = r15.readBits(5);
        int r8 = -1;
        int[] r9 = new int[r4];
        int r10 = 0;
    L9:
        if (r10 >= r4) goto L14;
        r9[r10] = r15.readBits(4);
        if (r9[r10] <= r8) goto L13;
        r8 = r9[r10];
    L13:
        r10 = r10 + 1;
        goto L9
    L14:
        int[] r11 = new int[r8 + 1];
        int r12 = 0;
    L16:
        if (r12 >= r11.length) goto L25;
        r11[r12] = r15.readBits(3) + 1;
        int r13 = r15.readBits(2);
        if (r13 <= 0) goto L20;
        r15.skipBits(8);
    L20:
        int r14 = 0;
    L22:
        if (r14 >= (1 << r13)) goto L24;
        r15.skipBits(8);
        r14 = r14 + 1;
        goto L22
    L24:
        r12 = r12 + 1;
        goto L16
    L25:
        r15.skipBits(2);
        int r6 = r15.readBits(4);
        int r7 = 0;
        int r16 = 0;
        int r17 = 0;
    L26:
        if (r16 >= r4) goto L36;
        r7 = r7 + r11[r9[r16]];
    L28:
        if (r17 >= r7) goto L30;
        r15.skipBits(r6);
        r17 = r17 + 1;
        goto L28
    L30:
        r16 = r16 + 1;
    L36:
        r3 = r3 + 1;
        goto L3
    L32:
        r15.skipBits(8);
        r15.skipBits(16);
        r15.skipBits(16);
        r15.skipBits(6);
        r15.skipBits(8);
        int r18 = r15.readBits(4) + 1;
        int r19 = 0;
    L33:
        if (r19 >= r18) goto L36;
        r15.skipBits(8);
        r19 = r19 + 1;
        goto L33
    L7:
        throw ParserException.createForMalformedContainer("floor type greater than 1 not decodable: " + r5, null);
    }

    private static void readMappings(int r10, VorbisBitArray r11) throws ParserException {
        int r0 = r11.readBits(6) + 1;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L28;
        int r3 = r11.readBits(16);
        if (r3 == 0) goto L8;
        Log.m44e(TAG, "mapping type other than 0 not supported: " + r3);
    L25:
        r2 = r2 + 1;
        goto L3
    L8:
        if (r11.readBit() == false) goto L10;
        int r4 = r11.readBits(4) + 1;
    L12:
        if (r11.readBit() == false) goto L17;
        int r6 = r11.readBits(8) + 1;
        int r8 = 0;
    L14:
        if (r8 >= r6) goto L17;
        r11.skipBits(iLog(r10 - 1));
        r11.skipBits(iLog(r10 - 1));
        r8 = r8 + 1;
    L17:
        if (r11.readBits(2) != 0) goto L27;
        if (r4 <= 1) goto L22;
        int r7 = 0;
    L20:
        if (r7 >= r10) goto L22;
        r11.skipBits(4);
        r7 = r7 + 1;
    L22:
        int r5 = 0;
    L23:
        if (r5 >= r4) goto L25;
        r11.skipBits(8);
        r11.skipBits(8);
        r11.skipBits(8);
        r5 = r5 + 1;
        goto L23
    L27:
        throw ParserException.createForMalformedContainer("to reserved bits must be zero after mapping coupling steps", null);
    L10:
        r4 = 1;
        goto L12
    }

    private static Mode[] readModes(VorbisBitArray r8) {
        int r0 = r8.readBits(6) + 1;
        Mode[] r1 = new Mode[r0];
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L5;
        r1[r2] = new Mode(r8.readBit(), r8.readBits(16), r8.readBits(16), r8.readBits(8));
        r2 = r2 + 1;
        goto L3
    L5:
        return r1;
    }

    private static void readResidues(VorbisBitArray r12) throws ParserException {
        int r1 = r12.readBits(6) + 1;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L24;
        if (r12.readBits(16) > 2) goto L23;
        r12.skipBits(24);
        r12.skipBits(24);
        r12.skipBits(24);
        int r5 = r12.readBits(6) + 1;
        r12.skipBits(8);
        int[] r7 = new int[r5];
        int r8 = 0;
    L7:
        if (r8 >= r5) goto L12;
        int r9 = 0;
        int r10 = r12.readBits(3);
        if (r12.readBit() == false) goto L11;
        r9 = r12.readBits(5);
    L11:
        r7[r8] = (r9 * 8) + r10;
        r8 = r8 + 1;
        goto L7
    L12:
        int r11 = 0;
    L13:
        if (r11 >= r5) goto L21;
        int r13 = 0;
    L15:
        if (r13 >= 8) goto L20;
        if ((r7[r11] & (1 << r13)) == 0) goto L19;
        r12.skipBits(8);
    L19:
        r13 = r13 + 1;
        goto L15
    L20:
        r11 = r11 + 1;
        goto L13
    L21:
        r3 = r3 + 1;
        goto L3
    L23:
        throw ParserException.createForMalformedContainer("residueType greater than 2 is not decodable", null);
    }

    public static CommentHeader readVorbisCommentHeader(ParsableByteArray r1) throws ParserException {
        return readVorbisCommentHeader(r1, true, true);
    }

    public static CommentHeader readVorbisCommentHeader(ParsableByteArray r9, boolean r10, boolean r11) throws ParserException {
        if (r10 == false) goto L4;
        verifyVorbisHeaderCapturePattern(3, r9, false);
    L4:
        String r2 = r9.readString((int) r9.readLittleEndianUnsignedInt());
        int r0 = (7 + 4) + r2.length();
        long r3 = r9.readLittleEndianUnsignedInt();
        String[] r5 = new String[(int) r3];
        int r1 = r0 + 4;
        int r6 = 0;
    L6:
        if (r6 >= r3) goto L8;
        r5[r6] = r9.readString((int) r9.readLittleEndianUnsignedInt());
        r1 = (r1 + 4) + r5[r6].length();
        r6 = r6 + 1;
        goto L6
    L8:
        if (r11 == false) goto L15;
        if ((r9.readUnsignedByte() & 1) != 0) goto L15;
        throw ParserException.createForMalformedContainer("framing bit expected to be set", null);
    L15:
        return new CommentHeader(r2, r5, r1 + 1);
    }

    public static VorbisIdHeader readVorbisIdentificationHeader(ParsableByteArray r14) throws ParserException {
        verifyVorbisHeaderCapturePattern(1, r14, false);
        int r3 = r14.readLittleEndianUnsignedIntToInt();
        int r4 = r14.readUnsignedByte();
        int r5 = r14.readLittleEndianUnsignedIntToInt();
        int r2 = r14.readLittleEndianInt();
        if (r2 > 0) goto L5;
        int r6 = -1;
    L6:
        int r7 = r14.readLittleEndianInt();
        if (r7 > 0) goto L9;
        int r8 = -1;
    L10:
        int r9 = r14.readLittleEndianInt();
        if (r9 > 0) goto L13;
        int r10 = -1;
    L14:
        int r13 = r14.readUnsignedByte();
        int r11 = (int) Math.pow(2.0d, r13 & 15);
        int r0 = (int) Math.pow(2.0d, (r13 & PsExtractor.VIDEO_STREAM_MASK) >> 4);
        if ((r14.readUnsignedByte() & 1) <= 0) goto L17;
        boolean r12 = true;
    L19:
        return new VorbisIdHeader(r3, r4, r5, r6, r8, r10, r11, r0, r12, Arrays.copyOf(r14.getData(), r14.limit()));
    L17:
        r12 = false;
        goto L19
    L13:
        r10 = r9;
        goto L14
    L9:
        r8 = r7;
        goto L10
    L5:
        r6 = r2;
        goto L6
    }

    public static Mode[] readVorbisModes(ParsableByteArray r6, int r7) throws ParserException {
        verifyVorbisHeaderCapturePattern(5, r6, false);
        int r0 = r6.readUnsignedByte() + 1;
        VorbisBitArray r1 = new VorbisBitArray(r6.getData());
        r1.skipBits(r6.getPosition() * 8);
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L5;
        skipBook(r1);
        r2 = r2 + 1;
        goto L3
    L5:
        int r3 = r1.readBits(6) + 1;
        int r4 = 0;
    L7:
        if (r4 >= r3) goto L13;
        if (r1.readBits(16) != 0) goto L12;
        r4 = r4 + 1;
        goto L7
    L12:
        throw ParserException.createForMalformedContainer("placeholder of time domain transforms not zeroed out", null);
    L13:
        readFloors(r1);
        readResidues(r1);
        readMappings(r7, r1);
        Mode[] r5 = readModes(r1);
        if (r1.readBit() == false) goto L17;
        return r5;
    L17:
        throw ParserException.createForMalformedContainer("framing bit after modes not set as expected", null);
    }

    private static void skipBook(VorbisBitArray r10) throws ParserException {
        if (r10.readBits(24) != 5653314) goto L34;
        int r1 = r10.readBits(16);
        int r0 = r10.readBits(24);
        if (r10.readBit() == true) goto L15;
        boolean r5 = r10.readBit();
        int r6 = 0;
    L7:
        if (r6 >= r0) goto L18;
        if (r5 == true) goto L10;
        r10.skipBits(5);
    L13:
        r6 = r6 + 1;
        goto L7
    L10:
        if (r10.readBit() == false) goto L13;
        r10.skipBits(5);
    L18:
        int r7 = r10.readBits(4);
        if (r7 > 2) goto L32;
        if (r7 == 1) goto L23;
        if (r7 == 2) goto L23;
        return;
    L23:
        r10.skipBits(32);
        r10.skipBits(32);
        int r4 = r10.readBits(4) + 1;
        r10.skipBits(1);
        if (r7 != 1) goto L28;
        if (r1 == 0) goto L27;
        long r8 = mapType1QuantValues(r0, r1);
    L29:
        r10.skipBits((int) (((long) r4) * r8));
        return;
    L27:
        r8 = 0;
        goto L29
    L28:
        r8 = ((long) r0) * ((long) r1);
        goto L29
    L32:
        throw ParserException.createForMalformedContainer("lookup type greater than 2 not decodable: " + r7, null);
    L15:
        r10.skipBits(5);
        int r9 = 0;
    L16:
        if (r9 >= r0) goto L18;
        r9 = r9 + r10.readBits(iLog(r0 - r9));
        goto L16
    L34:
        throw ParserException.createForMalformedContainer("expected code book to start with [0x56, 0x43, 0x42] at " + r10.getPosition(), null);
    }

    public static boolean verifyVorbisHeaderCapturePattern(int r4, ParsableByteArray r5, boolean r6) throws ParserException {
        if (r5.bytesLeft() >= 7) goto L9;
        if (r6 == false) goto L7;
        return false;
    L7:
        throw ParserException.createForMalformedContainer("too short header: " + r5.bytesLeft(), null);
    L9:
        if (r5.readUnsignedByte() == r4) goto L15;
        if (r6 == false) goto L13;
        return false;
    L13:
        throw ParserException.createForMalformedContainer("expected header type " + Integer.toHexString(r4), null);
    L15:
        if (r5.readUnsignedByte() == 118) goto L17;
    L29:
        if (r6 == false) goto L32;
        return false;
    L32:
        throw ParserException.createForMalformedContainer("expected characters 'vorbis'", null);
    L17:
        if (r5.readUnsignedByte() != 111) goto L29;
        if (r5.readUnsignedByte() != 114) goto L29;
        if (r5.readUnsignedByte() != 98) goto L29;
        if (r5.readUnsignedByte() != 105) goto L29;
        if (r5.readUnsignedByte() != 115) goto L29;
        return true;
    }
}
