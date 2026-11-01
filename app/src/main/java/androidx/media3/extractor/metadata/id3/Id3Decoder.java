package androidx.media3.extractor.metadata.id3;

import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.metadata.MetadataInputBuffer;
import androidx.media3.extractor.metadata.SimpleMetadataDecoder;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class Id3Decoder extends SimpleMetadataDecoder {
    private static final int FRAME_FLAG_V3_HAS_GROUP_IDENTIFIER = 32;
    private static final int FRAME_FLAG_V3_IS_COMPRESSED = 128;
    private static final int FRAME_FLAG_V3_IS_ENCRYPTED = 64;
    private static final int FRAME_FLAG_V4_HAS_DATA_LENGTH = 1;
    private static final int FRAME_FLAG_V4_HAS_GROUP_IDENTIFIER = 64;
    private static final int FRAME_FLAG_V4_IS_COMPRESSED = 8;
    private static final int FRAME_FLAG_V4_IS_ENCRYPTED = 4;
    private static final int FRAME_FLAG_V4_IS_UNSYNCHRONIZED = 2;
    public static final int ID3_HEADER_LENGTH = 10;
    public static final int ID3_TAG = 4801587;
    private static final int ID3_TEXT_ENCODING_ISO_8859_1 = 0;
    private static final int ID3_TEXT_ENCODING_UTF_16 = 1;
    private static final int ID3_TEXT_ENCODING_UTF_16BE = 2;
    private static final int ID3_TEXT_ENCODING_UTF_8 = 3;
    public static final FramePredicate NO_FRAMES_PREDICATE = null;
    private static final String TAG = "Id3Decoder";
    private final FramePredicate framePredicate;

    public interface FramePredicate {
        boolean evaluate(int r1, int r2, int r3, int r4, int r5);
    }

    private static final class Id3Header {
        private final int framesSize;
        private final boolean isUnsynchronized;
        private final int majorVersion;

        public Id3Header(int r1, boolean r2, int r3) {
            this.majorVersion = r1;
            this.isUnsynchronized = r2;
            this.framesSize = r3;
        }

        static /* synthetic */ int access$000(Id3Header r1) {
            return r1.majorVersion;
        }

        static /* synthetic */ int access$100(Id3Header r1) {
            return r1.framesSize;
        }

        static /* synthetic */ boolean access$200(Id3Header r1) {
            return r1.isUnsynchronized;
        }
    }

    static {
        NO_FRAMES_PREDICATE = new Id3Decoder$$ExternalSyntheticLambda0();
    }

    public Id3Decoder() {
        this(null);
    }

    public Id3Decoder(FramePredicate r1) {
        this.framePredicate = r1;
    }

    private static byte[] copyOfRangeIfValid(byte[] r1, int r2, int r3) {
        if (r3 > r2) goto L6;
        return Util.EMPTY_BYTE_ARRAY;
    L6:
        return Arrays.copyOfRange(r1, r2, r3);
    }

    private static ApicFrame decodeApicFrame(ParsableByteArray r12, int r13, int r14) {
        int r0 = r12.readUnsignedByte();
        Charset r1 = getCharset(r0);
        byte[] r2 = new byte[r13 - 1];
        r12.readBytes(r2, 0, r13 - 1);
        if (r14 != 2) goto L7;
        int r3 = 2;
        String r4 = "image/" + Ascii.toLowerCase(new String(r2, 0, 3, StandardCharsets.ISO_8859_1));
        if ("image/jpg".equals(r4) == false) goto L10;
        r4 = MimeTypes.IMAGE_JPEG;
    L10:
        int r5 = r2[r3 + 1] & 255;
        int r6 = r3 + 2;
        int r7 = indexOfTerminator(r2, r6, r0);
        return new ApicFrame(r4, new String(r2, r6, r7 - r6, r1), r5, copyOfRangeIfValid(r2, delimiterLength(r0) + r7, r2.length));
    L7:
        r3 = indexOfZeroByte(r2, 0);
        r4 = Ascii.toLowerCase(new String(r2, 0, r3, StandardCharsets.ISO_8859_1));
        if (r4.indexOf(47) != (-1)) goto L10;
        r4 = "image/" + r4;
        goto L10
    }

    private static BinaryFrame decodeBinaryFrame(ParsableByteArray r2, int r3, String r4) {
        byte[] r0 = new byte[r3];
        r2.readBytes(r0, 0, r3);
        return new BinaryFrame(r4, r0);
    }

    private static ChapterFrame decodeChapterFrame(ParsableByteArray r17, int r18, int r19, boolean r20, int r21, FramePredicate r22) {
        int r1 = r17.getPosition();
        int r2 = indexOfZeroByte(r17.getData(), r1);
        String r3 = new String(r17.getData(), r1, r2 - r1, StandardCharsets.ISO_8859_1);
        r17.setPosition(r2 + 1);
        int r9 = r17.readInt();
        int r10 = r17.readInt();
        long r4 = r17.readUnsignedInt();
        if (r4 != 4294967295L) goto L5;
        long r11 = -1;
    L6:
        long r5 = r17.readUnsignedInt();
        if (r5 != 4294967295L) goto L9;
        long r13 = -1;
    L10:
        ArrayList r6 = new ArrayList();
        int r7 = r1 + r18;
    L12:
        if (r17.getPosition() >= r7) goto L18;
        int r16 = r1;
        Id3Frame r8 = decodeFrame(r19, r17, r20, r21, r22);
        if (r8 == null) goto L16;
        r6.add(r8);
    L16:
        r1 = r16;
        goto L12
    L18:
        return new ChapterFrame(r3, r9, r10, r11, r13, (Id3Frame[]) r6.toArray(new Id3Frame[0]));
    L9:
        r13 = r5;
        goto L10
    L5:
        r11 = r4;
        goto L6
    }

    private static ChapterTocFrame decodeChapterTOCFrame(ParsableByteArray r16, int r17, int r18, boolean r19, int r20, FramePredicate r21) {
        int r1 = r16.getPosition();
        int r2 = indexOfZeroByte(r16.getData(), r1);
        String r3 = new String(r16.getData(), r1, r2 - r1, StandardCharsets.ISO_8859_1);
        r16.setPosition(r2 + 1);
        int r4 = r16.readUnsignedByte();
        if ((r4 & 2) == 0) goto L5;
        boolean r9 = true;
    L7:
        if ((r4 & 1) == 0) goto L9;
        boolean r10 = true;
    L10:
        int r5 = r16.readUnsignedByte();
        String[] r11 = new String[r5];
        int r6 = 0;
    L11:
        if (r6 >= r5) goto L13;
        int r7 = r16.getPosition();
        int r12 = indexOfZeroByte(r16.getData(), r7);
        r11[r6] = new String(r16.getData(), r7, r12 - r7, StandardCharsets.ISO_8859_1);
        r16.setPosition(r12 + 1);
        r6 = r6 + 1;
        goto L11
    L13:
        ArrayList r8 = new ArrayList();
        int r13 = r1 + r17;
    L15:
        if (r16.getPosition() >= r13) goto L21;
        Id3Frame r14 = decodeFrame(r18, r16, r19, r20, r21);
        if (r14 == null) goto L15;
        r8.add(r14);
        goto L15
    L21:
        return new ChapterTocFrame(r3, r9, r10, r11, (Id3Frame[]) r8.toArray(new Id3Frame[0]));
    L9:
        r10 = false;
        goto L10
    L5:
        r9 = false;
        goto L7
    }

    private static CommentFrame decodeCommentFrame(ParsableByteArray r10, int r11) {
        if (r11 >= 4) goto L6;
        return null;
    L6:
        int r0 = r10.readUnsignedByte();
        Charset r1 = getCharset(r0);
        byte[] r3 = new byte[3];
        r10.readBytes(r3, 0, 3);
        String r5 = new String(r3, 0, 3);
        byte[] r2 = new byte[r11 - 4];
        r10.readBytes(r2, 0, r11 - 4);
        int r4 = indexOfTerminator(r2, 0, r0);
        String r6 = new String(r2, 0, r4, r1);
        int r7 = delimiterLength(r0) + r4;
        return new CommentFrame(r5, r6, decodeStringIfValid(r2, r7, indexOfTerminator(r2, r7, r0), r1));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [androidx.media3.extractor.metadata.id3.Id3Frame] */
    /* JADX WARN: Type inference failed for: r0v66 */
    /* JADX WARN: Type inference failed for: r0v9 */
    /* JADX WARN: Type inference failed for: r19v4 */
    /* JADX WARN: Type inference failed for: r19v7 */
    /* JADX WARN: Type inference failed for: r1v1, types: [int] */
    /* JADX WARN: Type inference failed for: r1v10, types: [androidx.media3.common.util.ParsableByteArray] */
    /* JADX WARN: Type inference failed for: r1v11, types: [androidx.media3.common.util.ParsableByteArray] */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v14 */
    /* JADX WARN: Type inference failed for: r1v16 */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v21 */
    /* JADX WARN: Type inference failed for: r1v22 */
    /* JADX WARN: Type inference failed for: r1v23 */
    /* JADX WARN: Type inference failed for: r1v24 */
    /* JADX WARN: Type inference failed for: r1v25 */
    /* JADX WARN: Type inference failed for: r1v26 */
    /* JADX WARN: Type inference failed for: r1v27 */
    /* JADX WARN: Type inference failed for: r1v28 */
    /* JADX WARN: Type inference failed for: r1v29 */
    /* JADX WARN: Type inference failed for: r1v3, types: [androidx.media3.common.util.ParsableByteArray] */
    /* JADX WARN: Type inference failed for: r1v30 */
    /* JADX WARN: Type inference failed for: r1v31, types: [androidx.media3.common.util.ParsableByteArray] */
    /* JADX WARN: Type inference failed for: r1v32 */
    /* JADX WARN: Type inference failed for: r1v33 */
    /* JADX WARN: Type inference failed for: r1v34 */
    /* JADX WARN: Type inference failed for: r1v36 */
    /* JADX WARN: Type inference failed for: r1v37 */
    /* JADX WARN: Type inference failed for: r1v38 */
    /* JADX WARN: Type inference failed for: r1v39 */
    /* JADX WARN: Type inference failed for: r1v4 */
    /* JADX WARN: Type inference failed for: r1v40 */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r1v7 */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r1v9 */
    /* JADX WARN: Type inference failed for: r20v0 */
    /* JADX WARN: Type inference failed for: r20v1 */
    /* JADX WARN: Type inference failed for: r20v2 */
    /* JADX WARN: Type inference failed for: r20v3 */
    /* JADX WARN: Type inference failed for: r20v4 */
    /* JADX WARN: Type inference failed for: r25v0, types: [androidx.media3.extractor.metadata.id3.Id3Decoder$FramePredicate] */
    /* JADX WARN: Type inference failed for: r6v0 */
    /* JADX WARN: Type inference failed for: r6v1, types: [int] */
    /* JADX WARN: Type inference failed for: r6v26 */
    /* JADX WARN: Type inference failed for: r7v0, types: [androidx.media3.common.util.ParsableByteArray] */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v11, types: [int] */
    /* JADX WARN: Type inference failed for: r7v12 */
    /* JADX WARN: Type inference failed for: r7v2 */
    /* JADX WARN: Type inference failed for: r7v20 */
    /* JADX WARN: Type inference failed for: r7v23 */
    /* JADX WARN: Type inference failed for: r7v24 */
    /* JADX WARN: Type inference failed for: r7v25 */
    /* JADX WARN: Type inference failed for: r7v3 */
    /* JADX WARN: Type inference failed for: r7v4 */
    /* JADX WARN: Type inference failed for: r7v5 */
    /* JADX WARN: Type inference failed for: r7v6 */
    /* JADX WARN: Type inference failed for: r7v7 */
    /* JADX WARN: Type inference failed for: r7v8 */
    /* JADX WARN: Type inference failed for: r7v9, types: [int] */
    /* JADX WARN: Type inference failed for: r9v10 */
    /* JADX WARN: Type inference failed for: r9v11, types: [int] */
    /* JADX WARN: Type inference failed for: r9v12, types: [int] */
    /* JADX WARN: Type inference failed for: r9v13 */
    /* JADX WARN: Type inference failed for: r9v2 */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v31 */
    /* JADX WARN: Type inference failed for: r9v37 */
    /* JADX WARN: Type inference failed for: r9v38 */
    /* JADX WARN: Type inference failed for: r9v39 */
    /* JADX WARN: Type inference failed for: r9v4 */
    /* JADX WARN: Type inference failed for: r9v40 */
    /* JADX WARN: Type inference failed for: r9v41 */
    /* JADX WARN: Type inference failed for: r9v5 */
    /* JADX WARN: Type inference failed for: r9v6 */
    /* JADX WARN: Type inference failed for: r9v7 */
    /* JADX WARN: Type inference failed for: r9v8 */
    /* JADX WARN: Type inference failed for: r9v9 */
    private static Id3Frame decodeFrame(int r21, ParsableByteArray r22, boolean r23, int r24, FramePredicate r25) {
        int r3 = r21;
        ?? r7 = r22;
        int r1 = r7.readUnsignedByte();
        int r4 = r7.readUnsignedByte();
        int r5 = r7.readUnsignedByte();
        if (r3 < 3) goto L5;
        ?? r6 = r7.readUnsignedByte();
    L7:
        if (r3 != 4) goto L12;
        int r2 = r7.readUnsignedIntToInt();
        if (r23 == true) goto L11;
        r4 = (((r2 & 255) | (((r2 >> 8) & 255) << 7)) | (((r2 >> 16) & 255) << 14)) | (((r2 >> 24) & 255) << 21);
    L15:
        if (r3 < 3) goto L17;
        int r8 = r7.readUnsignedShort();
    L18:
        int r11 = r8;
        if (r1 != 0) goto L27;
        if (r4 != 0) goto L27;
        if (r5 != 0) goto L27;
        if (r6 != 0) goto L27;
        if (r4 != 0) goto L27;
        if (r11 != 0) goto L27;
        r7.setPosition(r7.limit());
        return null;
    L27:
        int r13 = r7.getPosition() + r4;
        if (r13 <= r7.limit()) goto L31;
        Log.m48w(TAG, "Frame size exceeds remaining tag data");
        r7.setPosition(r7.limit());
        return null;
    L31:
        if (r25 == 0) goto L36;
        boolean r15 = r25.evaluate(r3, r1, r4, r5, r6);
        r7 = r1;
        r3 = r3;
        if (r15 == true) goto L37;
        r7.setPosition(r13);
        return null;
    L37:
        boolean r16 = false;
        boolean r17 = false;
        int r18 = 0;
        boolean r19 = false;
        if (r3 == 3) goto L40;
        if (r3 == 4) goto L54;
        r5 = 0;
    L75:
        if (r5 != 0) goto L203;
        if (r16 == false) goto L78;
        ?? r9 = r7;
    L204:
        Log.m48w(TAG, "Skipping unsupported compressed or encrypted frame");
        r9.setPosition(r13);
        return null;
    L78:
        if (r19 == false) goto L80;
        r4 = r4 - 1;
        r7.skipBytes(1);
    L80:
        if (r18 == 0) goto L82;
        r4 = r4 - 4;
        r7.skipBytes(4);
    L82:
        if (r17 == false) goto L84;
        r4 = removeUnsynchronization(r7, r4);
    L84:
        r6 = 0;
        r6 = 0;
        r6 = 0;
        Throwable r12 = null;
        if (r7 != 84) goto L93;
        if (r4 != 88) goto L93;
        if (r5 != 88) goto L93;
        if (r3 == 2) goto L91;
        if (r6 != 88) goto L93;
    L91:
        r7 = r7;
        r4 = r4;
        Object r0 = decodeTxxxFrame(r7, r4);     // Catch: Throwable -> L95 Exception -> L97 OutOfMemoryError -> L99
    L208:
    L192:
        r7.setPosition(r13);
        ?? r10 = r0;
        ?? r14 = r7;
        ?? r20 = r6;
    L200:
        if (r10 != 0) goto L202;
        Log.m49w(TAG, "Failed to decode frame: id=" + getFrameId(r3, r14, r4, r5, r20) + ", frameSize=" + r4, r12);
    L202:
        return r10;
    L93:
        if (r7 != 84) goto L102;
        TextInformationFrame r26 = decodeTextInformationFrame(r7, r4, getFrameId(r3, r7, r4, r5, r6));     // Catch: Throwable -> L95 Exception -> L97 OutOfMemoryError -> L99
        r7 = r7;
        r4 = r4;
        r0 = r26;
        goto L208
    L102:
        if (r7 != 87) goto L110;
        if (r4 != 88) goto L110;
        if (r5 != 88) goto L110;
        if (r3 == 2) goto L108;
        if (r6 != 88) goto L110;
    L108:
        r7 = r7;
        r4 = r4;
        r0 = decodeWxxxFrame(r7, r4);     // Catch: Throwable -> L95 Exception -> L97 OutOfMemoryError -> L99
    L110:
        if (r7 != 87) goto L113;
        UrlLinkFrame r27 = decodeUrlLinkFrame(r7, r4, getFrameId(r3, r7, r4, r5, r6));     // Catch: Throwable -> L95 Exception -> L97 OutOfMemoryError -> L99
        r7 = r7;
        r4 = r4;
        r0 = r27;
        goto L208
    L113:
        if (r7 != 80) goto L121;
        if (r4 != 82) goto L121;
        if (r5 != 73) goto L121;
        if (r6 != 86) goto L121;
        r7 = r7;
        r4 = r4;
        r0 = decodePrivFrame(r7, r4);     // Catch: Throwable -> L95 Exception -> L97 OutOfMemoryError -> L99
    L121:
        if (r7 != 71) goto L131;
        if (r4 != 69) goto L131;
        if (r5 != 79) goto L131;
        if (r6 != 66) goto L128;
    L129:
        r7 = r7;
        r4 = r4;
        r0 = decodeGeobFrame(r7, r4);     // Catch: Throwable -> L95 Exception -> L97 OutOfMemoryError -> L99
        goto L208
    L128:
        if (r3 == 2) goto L129;
    L131:
        if (r3 != 2) goto L139;
        if (r7 != 80) goto L147;
        if (r4 != 73) goto L147;
        if (r5 != 67) goto L147;
    L145:
        r7 = r7;
        r4 = r4;
        r0 = decodeApicFrame(r7, r4, r3);     // Catch: Throwable -> L95 Exception -> L97 OutOfMemoryError -> L99
    L147:
        if (r7 != 67) goto L157;
        if (r4 != 79) goto L157;
        if (r5 != 77) goto L157;
        if (r6 == 77) goto L155;
        if (r3 != 2) goto L157;
    L155:
        r7 = r7;
        r4 = r4;
        r0 = decodeCommentFrame(r7, r4);     // Catch: Throwable -> L95 Exception -> L97 OutOfMemoryError -> L99
    L157:
        if (r7 == 67) goto L159;
    L173:
        int r28 = r4;
        r4 = r4;
        r4 = r28;
        r7 = r7;
        ?? r29 = 0;
        r5 = r5;
        r6 = r6;
        if (r7 == 67) goto L176;
    L182:
        r3 = r21;
        ParsableByteArray r30 = r22;
        if (r7 != 77) goto L191;
        if (r4 != 76) goto L191;
        if (r5 != 76) goto L191;
        if (r6 != 84) goto L191;
        r0 = decodeMlltFrame(r30, r4);     // Catch: Throwable -> L193 Exception -> L196 OutOfMemoryError -> L198
        r7 = r30;
    L211:
    L191:
        r0 = decodeBinaryFrame(r30, r4, getFrameId(r3, r7, r4, r5, r6));     // Catch: Throwable -> L193 Exception -> L196 OutOfMemoryError -> L198
        r7 = r30;
        goto L211
    L176:
        if (r4 != 84) goto L182;
        if (r5 != 79) goto L182;
        if (r6 != 67) goto L182;
        r3 = r21;
        ParsableByteArray r31 = r22;
        r0 = decodeChapterTOCFrame(r31, r4, r3, r23, r24, r25);     // Catch: Throwable -> L193 Exception -> L196 OutOfMemoryError -> L198
        r7 = r31;
    L196:
        e = e;
    L199:
        r12 = e;
        r7.setPosition(r13);
        r10 = r29;
        r14 = r7;
        r20 = r6;
    L198:
        e = e;
    L193:
        th = th;
    L194:
        r7.setPosition(r13);
        throw th;
    L159:
        if (r4 != 72) goto L173;
        if (r5 != 65) goto L173;
        if (r6 != 80) goto L173;
        r7 = r7;
        int r32 = r4;
        r4 = r4;
        r4 = r32;
        r29 = 0;
        r5 = r5;
        r6 = r6;
        r0 = decodeChapterFrame(r7, r4, r3, r23, r24, r25);     // Catch: Throwable -> L167 Exception -> L169 OutOfMemoryError -> L171
        r3 = r21;
        r7 = r22;
        goto L192
    L169:
        e = e;
    L172:
        r3 = r21;
        r7 = r22;
    L171:
        e = e;
    L167:
        th = th;
        r7 = r22;
        goto L194
    L139:
        if (r7 != 65) goto L147;
        if (r4 != 80) goto L147;
        if (r5 != 73) goto L147;
        if (r6 != 67) goto L147;
    L97:
        e = e;
    L100:
        ?? r110 = r7;
        r7 = r7;
        r7 = r110;
        int r111 = r4;
        r4 = r4;
        r4 = r111;
        r29 = r6;
        r5 = r5;
        r6 = r6;
    L99:
        e = e;
    L95:
        th = th;
        r7 = r7;
        goto L194
    L203:
        r9 = r7;
        goto L204
    L54:
        if ((r11 & 64) == 0) goto L56;
        boolean r33 = true;
    L57:
        r19 = r33;
        if ((r11 & 8) == 0) goto L60;
        int r34 = 1;
    L61:
        int r35 = r34;
        if ((r11 & 4) == 0) goto L64;
        boolean r36 = true;
    L65:
        r16 = r36;
        if ((r11 & 2) == 0) goto L68;
        boolean r37 = true;
    L69:
        r17 = r37;
        if ((r11 & 1) == 0) goto L72;
        int r112 = 1;
    L73:
        r18 = r112;
        r5 = r35;
        goto L75
    L72:
        r112 = 0;
        goto L73
    L68:
        r37 = false;
        goto L69
    L64:
        r36 = false;
        goto L65
    L60:
        r34 = 0;
        goto L61
    L56:
        r33 = false;
        goto L57
    L40:
        if ((r11 & 128) == 0) goto L42;
        int r38 = 1;
    L43:
        int r39 = r38;
        if ((r11 & 64) == 0) goto L46;
        boolean r40 = true;
    L47:
        r16 = r40;
        if ((r11 & 32) == 0) goto L50;
        boolean r113 = true;
    L51:
        r19 = r113;
        r18 = r39;
        r5 = r39;
        goto L75
    L50:
        r113 = false;
        goto L51
    L46:
        r40 = false;
        goto L47
    L42:
        r38 = 0;
        goto L43
    L36:
        r7 = r1;
        goto L37
    L17:
        r8 = 0;
        goto L18
    L11:
        r4 = r2;
        goto L15
    L12:
        if (r3 != 3) goto L14;
        r4 = r7.readUnsignedIntToInt();
        goto L15
    L14:
        r4 = r7.readUnsignedInt24();
        goto L15
    L5:
        r6 = 0;
        goto L7
    }

    private static GeobFrame decodeGeobFrame(ParsableByteArray r14, int r15) {
        int r0 = r14.readUnsignedByte();
        Charset r1 = getCharset(r0);
        byte[] r2 = new byte[r15 - 1];
        r14.readBytes(r2, 0, r15 - 1);
        int r3 = indexOfZeroByte(r2, 0);
        String r4 = MimeTypes.normalizeMimeType(new String(r2, 0, r3, StandardCharsets.ISO_8859_1));
        int r5 = r3 + 1;
        int r6 = indexOfTerminator(r2, r5, r0);
        String r7 = decodeStringIfValid(r2, r5, r6, r1);
        int r8 = delimiterLength(r0) + r6;
        int r9 = indexOfTerminator(r2, r8, r0);
        return new GeobFrame(r4, r7, decodeStringIfValid(r2, r8, r9, r1), copyOfRangeIfValid(r2, delimiterLength(r0) + r9, r2.length));
    }

    private static Id3Header decodeHeader(ParsableByteArray r10) {
        if (r10.bytesLeft() >= 10) goto L6;
        Log.m48w(TAG, "Data too short to be an ID3 tag");
        return null;
    L6:
        int r0 = r10.readUnsignedInt24();
        boolean r4 = false;
        if (r0 == 4801587) goto L10;
        Log.m48w(TAG, "Unexpected first three bytes of ID3 tag header: 0x" + String.format("%06X", new Object[]{Integer.valueOf(r0)}));
        return null;
    L10:
        int r1 = r10.readUnsignedByte();
        r10.skipBytes(1);
        int r6 = r10.readUnsignedByte();
        int r7 = r10.readSynchSafeInt();
        if (r1 != 2) goto L21;
        if ((r6 & 64) == 0) goto L15;
        boolean r8 = true;
    L16:
        if (r8 == false) goto L43;
        Log.m48w(TAG, "Skipped ID3 tag with majorVersion=2 and undefined compression scheme");
        return null;
    L43:
        if (r1 >= 4) goto L48;
        if ((r6 & 128) == 0) goto L48;
        r4 = true;
    L48:
        return new Id3Header(r1, r4, r7);
    L15:
        r8 = false;
        goto L16
    L21:
        if (r1 == 3) goto L23;
        if (r1 == 4) goto L31;
        Log.m48w(TAG, "Skipped ID3 tag with unsupported majorVersion=" + r1);
        return null;
    L31:
        if ((r6 & 64) == 0) goto L33;
        boolean r2 = true;
    L34:
        if (r2 == false) goto L37;
        int r3 = r10.readSynchSafeInt();
        r10.skipBytes(r3 - 4);
        r7 = r7 - r3;
    L37:
        if ((r6 & 16) == 0) goto L39;
        boolean r5 = true;
    L40:
        if (r5 == false) goto L43;
        r7 = r7 - 10;
        goto L43
    L39:
        r5 = false;
        goto L40
    L33:
        r2 = false;
        goto L34
    L23:
        if ((r6 & 64) == 0) goto L25;
        boolean r9 = true;
    L26:
        if (r9 == false) goto L43;
        int r11 = r10.readInt();
        r10.skipBytes(r11);
        r7 = r7 - (r11 + 4);
        goto L43
    L25:
        r9 = false;
        goto L26
    }

    private static MlltFrame decodeMlltFrame(ParsableByteArray r14, int r15) {
        int r1 = r14.readUnsignedShort();
        int r2 = r14.readUnsignedInt24();
        int r3 = r14.readUnsignedInt24();
        int r6 = r14.readUnsignedByte();
        int r7 = r14.readUnsignedByte();
        ParsableBitArray r0 = new ParsableBitArray();
        r0.reset(r14);
        int r11 = ((r15 - 10) * 8) / (r6 + r7);
        int[] r4 = new int[r11];
        int[] r5 = new int[r11];
        int r8 = 0;
    L3:
        if (r8 >= r11) goto L6;
        int r12 = r0.readBits(r6);
        int r13 = r0.readBits(r7);
        r4[r8] = r12;
        r5[r8] = r13;
        r8 = r8 + 1;
        goto L3
    L6:
        return new MlltFrame(r1, r2, r3, r4, r5);
    }

    private static PrivFrame decodePrivFrame(ParsableByteArray r6, int r7) {
        byte[] r0 = new byte[r7];
        r6.readBytes(r0, 0, r7);
        int r2 = indexOfZeroByte(r0, 0);
        return new PrivFrame(new String(r0, 0, r2, StandardCharsets.ISO_8859_1), copyOfRangeIfValid(r0, r2 + 1, r0.length));
    }

    private static String decodeStringIfValid(byte[] r2, int r3, int r4, Charset r5) {
        if (r4 > r3) goto L4;
        return "";
    L4:
        if (r4 <= r2.length) goto L7;
        return "";
    L7:
        return new String(r2, r3, r4 - r3, r5);
    }

    private static TextInformationFrame decodeTextInformationFrame(ParsableByteArray r5, int r6, String r7) {
        if (r6 >= 1) goto L5;
        return null;
    L5:
        int r0 = r5.readUnsignedByte();
        byte[] r2 = new byte[r6 - 1];
        r5.readBytes(r2, 0, r6 - 1);
        return new TextInformationFrame(r7, null, decodeTextInformationFrameValues(r2, r0, 0));
    }

    private static ImmutableList<String> decodeTextInformationFrameValues(byte[] r7, int r8, int r9) {
        if (r9 >= r7.length) goto L5;
        ImmutableList.Builder r0 = ImmutableList.builder();
        int r2 = r9;
        int r3 = indexOfTerminator(r7, r2, r8);
    L7:
        if (r2 >= r3) goto L9;
        r0.add(new String(r7, r2, r3 - r2, getCharset(r8)));
        r2 = r3 + delimiterLength(r8);
        r3 = indexOfTerminator(r7, r2, r8);
        goto L7
    L9:
        ImmutableList<String> r4 = r0.build();
        if (r4.isEmpty() == false) goto L13;
        return ImmutableList.m213of("");
    L13:
        return r4;
    L5:
        return ImmutableList.m213of("");
    }

    private static TextInformationFrame decodeTxxxFrame(ParsableByteArray r7, int r8) {
        if (r8 >= 1) goto L6;
        return null;
    L6:
        int r0 = r7.readUnsignedByte();
        byte[] r1 = new byte[r8 - 1];
        r7.readBytes(r1, 0, r8 - 1);
        int r2 = indexOfTerminator(r1, 0, r0);
        return new TextInformationFrame("TXXX", new String(r1, 0, r2, getCharset(r0)), decodeTextInformationFrameValues(r1, r0, delimiterLength(r0) + r2));
    }

    private static UrlLinkFrame decodeUrlLinkFrame(ParsableByteArray r5, int r6, String r7) {
        byte[] r0 = new byte[r6];
        r5.readBytes(r0, 0, r6);
        return new UrlLinkFrame(r7, null, new String(r0, 0, indexOfZeroByte(r0, 0), StandardCharsets.ISO_8859_1));
    }

    private static UrlLinkFrame decodeWxxxFrame(ParsableByteArray r9, int r10) {
        if (r10 >= 1) goto L6;
        return null;
    L6:
        int r0 = r9.readUnsignedByte();
        byte[] r1 = new byte[r10 - 1];
        r9.readBytes(r1, 0, r10 - 1);
        int r2 = indexOfTerminator(r1, 0, r0);
        String r4 = new String(r1, 0, r2, getCharset(r0));
        int r3 = delimiterLength(r0) + r2;
        return new UrlLinkFrame("WXXX", r4, decodeStringIfValid(r1, r3, indexOfZeroByte(r1, r3), StandardCharsets.ISO_8859_1));
    }

    private static int delimiterLength(int r1) {
        if (r1 != 0) goto L4;
    L7:
        return 1;
    L4:
        if (r1 == 3) goto L7;
        return 2;
    }

    private static Charset getCharset(int r1) {
        switch(r1) {
            case 1: goto L10;
            case 2: goto L8;
            case 3: goto L6;
            default: goto L4;
        };
    L4:
        return StandardCharsets.ISO_8859_1;
    L6:
        return StandardCharsets.UTF_8;
    L8:
        return StandardCharsets.UTF_16BE;
    L10:
        return StandardCharsets.UTF_16;
    }

    private static String getFrameId(int r10, int r11, int r12, int r13, int r14) {
        if (r10 != 2) goto L6;
        return String.format(Locale.US, "%c%c%c", new Object[]{Integer.valueOf(r11), Integer.valueOf(r12), Integer.valueOf(r13)});
    L6:
        return String.format(Locale.US, "%c%c%c%c", new Object[]{Integer.valueOf(r11), Integer.valueOf(r12), Integer.valueOf(r13), Integer.valueOf(r14)});
    }

    private static int indexOfTerminator(byte[] r2, int r3, int r4) {
        int r0 = indexOfZeroByte(r2, r3);
        if (r4 != 0) goto L5;
    L17:
        return r0;
    L5:
        if (r4 == 3) goto L17;
    L8:
        if (r0 >= (r2.length - 1)) goto L16;
        if (((r0 - r3) % 2) != 0) goto L14;
        if (r2[r0 + 1] != 0) goto L14;
        return r0;
    L14:
        r0 = indexOfZeroByte(r2, r0 + 1);
        goto L8
    L16:
        return r2.length;
    }

    private static int indexOfZeroByte(byte[] r2, int r3) {
        int r0 = r3;
    L4:
        if (r0 >= r2.length) goto L10;
        if (r2[r0] == 0) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r0;
    L10:
        return r2.length;
    }

    static /* synthetic */ boolean lambda$static$0(int r1, int r2, int r3, int r4, int r5) {
        return false;
    }

    private static int removeUnsynchronization(ParsableByteArray r7, int r8) {
        byte[] r0 = r7.getData();
        int r1 = r7.getPosition();
        int r2 = r1;
    L4:
        if ((r2 + 1) >= (r1 + r8)) goto L11;
        if ((r0[r2] & 255) != 255) goto L10;
        if (r0[r2 + 1] != 0) goto L10;
        System.arraycopy(r0, r2 + 2, r0, r2 + 1, (r8 - (r2 - r1)) - 2);
        r8 = r8 - 1;
    L10:
        r2 = r2 + 1;
        goto L4
    L11:
        return r8;
    }

    private static boolean validateFrames(ParsableByteArray r19, int r20, int r21, boolean r22) {
        int r3 = r19.getPosition();
    L74:
        boolean r4 = true;
        if (r19.bytesLeft() < r21) goto L66;
        if (r20 >= 3) goto L72;
        int r6 = r19.readUnsignedInt24();     // Catch: Throwable -> L64
        long r7 = r19.readUnsignedInt24();     // Catch: Throwable -> L64
        int r9 = 0;
    L12:
        if (r6 != 0) goto L19;
        if (r7 != 0) goto L19;
        if (r9 != 0) goto L19;
        r19.setPosition(r3);
        return true;
    L19:
        if (r20 != 4) goto L26;
        if (r22 == true) goto L26;
        if ((8421504 & r7) != 0) goto L23;
        r7 = (((r7 & 255) | (((r7 >> 8) & 255) << 7)) | (((r7 >> 16) & 255) << 14)) | (((r7 >> 24) & 255) << 21);
        goto L26
    L23:
        r19.setPosition(r3);
        return false;
    L26:
        boolean r10 = false;
        boolean r11 = false;
        if (r20 == 4) goto L29;
        if (r20 == 3) goto L39;
    L47:
        int r0 = 0;
        if (r10 == false) goto L50;
        r0 = 0 + 1;
    L50:
        if (r11 == false) goto L53;
        r0 = r0 + 4;
    L53:
        if (r7 < r0) goto L54;
        if (r19.bytesLeft() < r7) goto L59;
        r19.skipBytes((int) r7);     // Catch: Throwable -> L64
        goto L74
    L59:
        r19.setPosition(r3);
        return false;
    L54:
        r19.setPosition(r3);
        return false;
    L39:
        if ((r9 & 32) == 0) goto L41;
        boolean r1 = true;
    L42:
        r10 = r1;
        if ((r9 & 128) != 0) goto L46;
        r4 = false;
    L46:
        r11 = r4;
        goto L47
    L41:
        r1 = false;
        goto L42
    L29:
        if ((r9 & 64) == 0) goto L31;
        boolean r2 = true;
    L32:
        r10 = r2;
        if ((r9 & 1) != 0) goto L36;
        r4 = false;
    L36:
        r11 = r4;
        goto L47
    L31:
        r2 = false;
        goto L32
    L72:
        r6 = r19.readInt();     // Catch: Throwable -> L64
        r7 = r19.readUnsignedInt();     // Catch: Throwable -> L64
        r9 = r19.readUnsignedShort();     // Catch: Throwable -> L64
    L64:
        th = th;
    L70:
        r19.setPosition(r3);
        throw th;
    L66:
        r19.setPosition(r3);
        return true;
    L68:
        th = th;
        goto L70
    }

    @Override // androidx.media3.extractor.metadata.SimpleMetadataDecoder
    protected Metadata decode(MetadataInputBuffer r3, ByteBuffer r4) {
        return decode(r4.array(), r4.limit());
    }

    public Metadata decode(byte[] r11, int r12) {
        ArrayList r0 = new ArrayList();
        ParsableByteArray r1 = new ParsableByteArray(r11, r12);
        Id3Header r2 = decodeHeader(r1);
        if (r2 != null) goto L5;
        return null;
    L5:
        int r4 = r1.getPosition();
        if (Id3Header.access$000(r2) != 2) goto L8;
        int r5 = 6;
    L9:
        int r6 = Id3Header.access$100(r2);
        if (Id3Header.access$200(r2) == false) goto L12;
        r6 = removeUnsynchronization(r1, Id3Header.access$100(r2));
    L12:
        r1.setLimit(r4 + r6);
        boolean r7 = false;
        if (validateFrames(r1, Id3Header.access$000(r2), r5, false) == true) goto L22;
        if (Id3Header.access$000(r2) == 4) goto L17;
    L19:
        Log.m48w(TAG, "Failed to validate ID3 tag with majorVersion=" + Id3Header.access$000(r2));
        return null;
    L17:
        if (validateFrames(r1, 4, r5, true) == false) goto L19;
        r7 = true;
    L22:
        if (r1.bytesLeft() < r5) goto L28;
        Id3Frame r3 = decodeFrame(Id3Header.access$000(r2), r1, r7, r5, this.framePredicate);
        if (r3 == null) goto L22;
        r0.add(r3);
        goto L22
    L28:
        return new Metadata(r0);
    L8:
        r5 = 10;
        goto L9
    }
}
