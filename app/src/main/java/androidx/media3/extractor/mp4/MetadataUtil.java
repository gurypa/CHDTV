package androidx.media3.extractor.mp4;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.container.MdtaMetadataEntry;
import androidx.media3.container.Mp4Box;
import androidx.media3.extractor.GaplessInfoHolder;
import androidx.media3.extractor.metadata.id3.ApicFrame;
import androidx.media3.extractor.metadata.id3.CommentFrame;
import androidx.media3.extractor.metadata.id3.Id3Frame;
import androidx.media3.extractor.metadata.id3.Id3Util;
import androidx.media3.extractor.metadata.id3.InternalFrame;
import androidx.media3.extractor.metadata.id3.TextInformationFrame;
import com.google.common.collect.ImmutableList;

/* JADX INFO: loaded from: classes.dex */
final class MetadataUtil {
    private static final int PICTURE_TYPE_FRONT_COVER = 3;
    private static final int SHORT_TYPE_ALBUM = 6384738;
    private static final int SHORT_TYPE_ARTIST = 4280916;
    private static final int SHORT_TYPE_COMMENT = 6516084;
    private static final int SHORT_TYPE_COMPOSER_1 = 6516589;
    private static final int SHORT_TYPE_COMPOSER_2 = 7828084;
    private static final int SHORT_TYPE_ENCODER = 7630703;
    private static final int SHORT_TYPE_GENRE = 6776174;
    private static final int SHORT_TYPE_LYRICS = 7108978;
    private static final int SHORT_TYPE_NAME_1 = 7233901;
    private static final int SHORT_TYPE_NAME_2 = 7631467;
    private static final int SHORT_TYPE_YEAR = 6578553;
    private static final String TAG = "MetadataUtil";
    private static final int TYPE_ALBUM_ARTIST = 1631670868;
    private static final int TYPE_COMPILATION = 1668311404;
    private static final int TYPE_COVER_ART = 1668249202;
    private static final int TYPE_DISK_NUMBER = 1684632427;
    private static final int TYPE_GAPLESS_ALBUM = 1885823344;
    private static final int TYPE_GENRE = 1735291493;
    private static final int TYPE_GROUPING = 6779504;
    private static final int TYPE_INTERNAL = 757935405;
    private static final int TYPE_RATING = 1920233063;
    private static final int TYPE_SORT_ALBUM = 1936679276;
    private static final int TYPE_SORT_ALBUM_ARTIST = 1936679265;
    private static final int TYPE_SORT_ARTIST = 1936679282;
    private static final int TYPE_SORT_COMPOSER = 1936679791;
    private static final int TYPE_SORT_TRACK_NAME = 1936682605;
    private static final int TYPE_TEMPO = 1953329263;
    private static final int TYPE_TOP_BYTE_COPYRIGHT = 169;
    private static final int TYPE_TOP_BYTE_REPLACEMENT = 253;
    private static final int TYPE_TRACK_NUMBER = 1953655662;
    private static final int TYPE_TV_SHOW = 1953919848;
    private static final int TYPE_TV_SORT_SHOW = 1936683886;

    private MetadataUtil() {
    }

    public static MdtaMetadataEntry findMdtaMetadataEntryWithKey(Metadata r4, String r5) {
        int r0 = 0;
    L4:
        if (r0 >= r4.length()) goto L11;
        Metadata.Entry r1 = r4.get(r0);
        if ((r1 instanceof MdtaMetadataEntry) == false) goto L10;
        MdtaMetadataEntry r2 = (MdtaMetadataEntry) r1;
        if (r2.key.equals(r5) == false) goto L10;
        return r2;
    L10:
        r0 = r0 + 1;
        goto L4
    L11:
        return null;
    }

    private static CommentFrame parseCommentAttribute(int r5, ParsableByteArray r6) {
        int r0 = r6.readInt();
        if (r6.readInt() != 1684108385) goto L6;
        r6.skipBytes(8);
        String r2 = r6.readNullTerminatedString(r0 - 16);
        return new CommentFrame(C0180C.LANGUAGE_UNDETERMINED, r2, r2);
    L6:
        Log.m48w(TAG, "Failed to parse comment attribute: " + Mp4Box.getBoxTypeString(r5));
        return null;
    }

    private static ApicFrame parseCoverArt(ParsableByteArray r9) {
        int r0 = r9.readInt();
        if (r9.readInt() != 1684108385) goto L16;
        int r5 = BoxParser.parseFullBoxFlags(r9.readInt());
        if (r5 != 13) goto L8;
        String r6 = MimeTypes.IMAGE_JPEG;
    L11:
        if (r6 != null) goto L14;
        Log.m48w(TAG, "Unrecognized cover art flags: " + r5);
        return null;
    L14:
        r9.skipBytes(4);
        byte[] r3 = new byte[r0 - 16];
        r9.readBytes(r3, 0, r3.length);
        return new ApicFrame(r6, null, 3, r3);
    L8:
        if (r5 != 14) goto L10;
        r6 = MimeTypes.IMAGE_PNG;
        goto L11
    L10:
        r6 = null;
        goto L11
    L16:
        Log.m48w(TAG, "Failed to parse cover art attribute");
        return null;
    }

    public static Metadata.Entry parseIlstElement(ParsableByteArray r7) {
        int r1 = r7.readInt() + r7.getPosition();
        int r2 = r7.readInt();
        int r3 = (r2 >> 24) & 255;
        if (r3 != TYPE_TOP_BYTE_COPYRIGHT) goto L5;
    L92:
        int r4 = 16777215 & r2;
        if (r4 != SHORT_TYPE_COMMENT) goto L100;
        CommentFrame r5 = parseCommentAttribute(r2, r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r5;
    L100:
        if (r4 != SHORT_TYPE_NAME_1) goto L102;
    L151:
        TextInformationFrame r6 = parseTextAttribute(r2, "TIT2", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r6;
    L102:
        if (r4 == SHORT_TYPE_NAME_2) goto L151;
        if (r4 != SHORT_TYPE_COMPOSER_1) goto L107;
    L148:
        TextInformationFrame r8 = parseTextAttribute(r2, "TCOM", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r8;
    L107:
        if (r4 == SHORT_TYPE_COMPOSER_2) goto L148;
        if (r4 != SHORT_TYPE_YEAR) goto L115;
        TextInformationFrame r9 = parseTextAttribute(r2, "TDRC", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r9;
    L115:
        if (r4 != SHORT_TYPE_ARTIST) goto L120;
        TextInformationFrame r10 = parseTextAttribute(r2, "TPE1", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r10;
    L120:
        if (r4 != SHORT_TYPE_ENCODER) goto L125;
        TextInformationFrame r11 = parseTextAttribute(r2, "TSSE", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r11;
    L125:
        if (r4 != SHORT_TYPE_ALBUM) goto L130;
        TextInformationFrame r12 = parseTextAttribute(r2, "TALB", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r12;
    L130:
        if (r4 != SHORT_TYPE_LYRICS) goto L135;
        TextInformationFrame r13 = parseTextAttribute(r2, "USLT", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r13;
    L135:
        if (r4 != SHORT_TYPE_GENRE) goto L140;
        TextInformationFrame r14 = parseTextAttribute(r2, "TCON", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r14;
    L140:
        if (r4 != TYPE_GROUPING) goto L145;
        TextInformationFrame r15 = parseTextAttribute(r2, "TIT1", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r15;
    L145:
        Log.m42d(TAG, "Skipped unknown metadata entry: " + Mp4Box.getBoxTypeString(r2));     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return null;
    L5:
        if (r3 == TYPE_TOP_BYTE_REPLACEMENT) goto L92;
        if (r2 != TYPE_GENRE) goto L13;
        TextInformationFrame r16 = parseStandardGenreAttribute(r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r16;
    L13:
        if (r2 != TYPE_DISK_NUMBER) goto L18;
        TextInformationFrame r17 = parseIndexAndCountAttribute(r2, "TPOS", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r17;
    L18:
        if (r2 != TYPE_TRACK_NUMBER) goto L23;
        TextInformationFrame r18 = parseIndexAndCountAttribute(r2, "TRCK", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r18;
    L23:
        if (r2 != TYPE_TEMPO) goto L28;
        Id3Frame r19 = parseIntegerAttribute(r2, "TBPM", r7, true, false);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r19;
    L28:
        if (r2 != TYPE_COMPILATION) goto L33;
        Id3Frame r20 = parseIntegerAttribute(r2, "TCMP", r7, true, true);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r20;
    L33:
        if (r2 != TYPE_COVER_ART) goto L38;
        ApicFrame r21 = parseCoverArt(r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r21;
    L38:
        if (r2 != TYPE_ALBUM_ARTIST) goto L43;
        TextInformationFrame r22 = parseTextAttribute(r2, "TPE2", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r22;
    L43:
        if (r2 != TYPE_SORT_TRACK_NAME) goto L48;
        TextInformationFrame r23 = parseTextAttribute(r2, "TSOT", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r23;
    L48:
        if (r2 != TYPE_SORT_ALBUM) goto L53;
        TextInformationFrame r24 = parseTextAttribute(r2, "TSOA", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r24;
    L53:
        if (r2 != TYPE_SORT_ARTIST) goto L58;
        TextInformationFrame r25 = parseTextAttribute(r2, "TSOP", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r25;
    L58:
        if (r2 != TYPE_SORT_ALBUM_ARTIST) goto L63;
        TextInformationFrame r26 = parseTextAttribute(r2, "TSO2", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r26;
    L63:
        if (r2 != TYPE_SORT_COMPOSER) goto L68;
        TextInformationFrame r27 = parseTextAttribute(r2, "TSOC", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r27;
    L68:
        if (r2 != TYPE_RATING) goto L73;
        Id3Frame r28 = parseIntegerAttribute(r2, "ITUNESADVISORY", r7, false, false);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r28;
    L73:
        if (r2 != TYPE_GAPLESS_ALBUM) goto L78;
        Id3Frame r29 = parseIntegerAttribute(r2, "ITUNESGAPLESS", r7, false, true);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r29;
    L78:
        if (r2 != TYPE_TV_SORT_SHOW) goto L83;
        TextInformationFrame r30 = parseTextAttribute(r2, "TVSHOWSORT", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r30;
    L83:
        if (r2 != TYPE_TV_SHOW) goto L88;
        TextInformationFrame r31 = parseTextAttribute(r2, "TVSHOW", r7);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r31;
    L88:
        if (r2 != TYPE_INTERNAL) goto L145;
        Id3Frame r32 = parseInternalAttribute(r7, r1);     // Catch: Throwable -> L97
        r7.setPosition(r1);
        return r32;
    L97:
        th = move-exception;
        r7.setPosition(r1);
        throw th;
    }

    private static TextInformationFrame parseIndexAndCountAttribute(int r8, String r9, ParsableByteArray r10) {
        int r0 = r10.readInt();
        if (r10.readInt() == 1684108385) goto L5;
    L13:
        Log.m48w(TAG, "Failed to parse index/count attribute: " + Mp4Box.getBoxTypeString(r8));
        return null;
    L5:
        if (r0 < 22) goto L13;
        r10.skipBytes(10);
        int r2 = r10.readUnsignedShort();
        if (r2 <= 0) goto L13;
        String r4 = "" + r2;
        int r5 = r10.readUnsignedShort();
        if (r5 <= 0) goto L12;
        r4 = r4 + "/" + r5;
    L12:
        return new TextInformationFrame(r9, null, ImmutableList.m213of(r4));
    }

    private static int parseIntegerAttribute(ParsableByteArray r4) {
        int r0 = r4.readInt();
        if (r4.readInt() != 1684108385) goto L17;
        r4.skipBytes(8);
        switch((r0 - 16)) {
            case 1: goto L16;
            case 2: goto L14;
            case 3: goto L12;
            case 4: goto L8;
            default: goto L17;
        };
    L8:
        if ((r4.peekUnsignedByte() & 128) != 0) goto L17;
        return r4.readUnsignedIntToInt();
    L12:
        return r4.readUnsignedInt24();
    L14:
        return r4.readUnsignedShort();
    L16:
        return r4.readUnsignedByte();
    L17:
        Log.m48w(TAG, "Failed to parse data atom to int");
        return -1;
    }

    private static Id3Frame parseIntegerAttribute(int r4, String r5, ParsableByteArray r6, boolean r7, boolean r8) {
        int r0 = parseIntegerAttribute(r6);
        if (r8 == false) goto L6;
        r0 = Math.min(1, r0);
    L6:
        if (r0 < 0) goto L11;
        if (r7 == false) goto L10;
        return new TextInformationFrame(r5, null, ImmutableList.m213of(Integer.toString(r0)));
    L10:
        return new CommentFrame(C0180C.LANGUAGE_UNDETERMINED, r5, Integer.toString(r0));
    L11:
        Log.m48w(TAG, "Failed to parse uint8 attribute: " + Mp4Box.getBoxTypeString(r4));
        return null;
    }

    private static Id3Frame parseInternalAttribute(ParsableByteArray r8, int r9) {
        String r0 = null;
        String r1 = null;
        int r2 = -1;
        int r3 = -1;
    L4:
        if (r8.getPosition() >= r9) goto L16;
        int r4 = r8.getPosition();
        int r5 = r8.readInt();
        int r6 = r8.readInt();
        r8.skipBytes(4);
        if (r6 == 1835360622) goto L7;
        if (r6 == 1851878757) goto L10;
        if (r6 != 1684108385) goto L14;
        r2 = r4;
        r3 = r5;
    L14:
        r8.skipBytes(r5 - 12);
        goto L4
    L10:
        r1 = r8.readNullTerminatedString(r5 - 12);
        goto L4
    L7:
        r0 = r8.readNullTerminatedString(r5 - 12);
        goto L4
    L16:
        if (r0 == null) goto L23;
        if (r1 != null) goto L19;
        return null;
    L19:
        if (r2 == (-1)) goto L35;
        r8.setPosition(r2);
        r8.skipBytes(16);
        return new InternalFrame(r0, r1, r8.readNullTerminatedString(r3 - 16));
    L35:
        return null;
    L23:
        return null;
    }

    public static MdtaMetadataEntry parseMdtaMetadataEntryFromIlst(ParsableByteArray r8, int r9, String r10) {
    L2:
        int r0 = r8.getPosition();
        if (r0 >= r9) goto L9;
        int r1 = r8.readInt();
        if (r8.readInt() == 1684108385) goto L6;
        r8.setPosition(r0 + r1);
        goto L2
    L6:
        int r3 = r8.readInt();
        int r4 = r8.readInt();
        int r5 = r1 - 16;
        byte[] r6 = new byte[r5];
        r8.readBytes(r6, 0, r5);
        return new MdtaMetadataEntry(r10, r6, r4, r3);
    L9:
        return null;
    }

    private static TextInformationFrame parseStandardGenreAttribute(ParsableByteArray r6) {
        String r1 = Id3Util.resolveV1Genre(parseIntegerAttribute(r6) - 1);
        if (r1 != null) goto L5;
        Log.m48w(TAG, "Failed to parse standard genre code");
        return null;
    L5:
        return new TextInformationFrame("TCON", null, ImmutableList.m213of(r1));
    }

    private static TextInformationFrame parseTextAttribute(int r6, String r7, ParsableByteArray r8) {
        int r0 = r8.readInt();
        if (r8.readInt() != 1684108385) goto L6;
        r8.skipBytes(8);
        return new TextInformationFrame(r7, null, ImmutableList.m213of(r8.readNullTerminatedString(r0 - 16)));
    L6:
        Log.m48w(TAG, "Failed to parse text attribute: " + Mp4Box.getBoxTypeString(r6));
        return null;
    }

    public static void setFormatGaplessInfo(int r2, GaplessInfoHolder r3, Format.Builder r4) {
        if (r2 == 1) goto L5;
        return;
    L5:
        if (r3.hasGaplessInfo() == false) goto L9;
        r4.setEncoderDelay(r3.encoderDelay).setEncoderPadding(r3.encoderPadding);
        return;
    }

    public static void setFormatMetadata(int r7, Metadata r8, Format.Builder r9, Metadata... r10) {
        int r1 = 0;
        Metadata r0 = new Metadata(new Metadata.Entry[0]);
        if (r8 == null) goto L16;
        int r2 = 0;
    L6:
        if (r2 >= r8.length()) goto L16;
        Metadata.Entry r3 = r8.get(r2);
        if ((r3 instanceof MdtaMetadataEntry) == false) goto L15;
        MdtaMetadataEntry r4 = (MdtaMetadataEntry) r3;
        if (r4.key.equals(MdtaMetadataEntry.KEY_ANDROID_CAPTURE_FPS) == true) goto L12;
        r0 = r0.copyWithAppendedEntries(new Metadata.Entry[]{r4});
        goto L15
    L12:
        if (r7 != 2) goto L15;
        r0 = r0.copyWithAppendedEntries(new Metadata.Entry[]{r4});
    L15:
        r2 = r2 + 1;
    L16:
        int r5 = r10.length;
    L17:
        if (r1 >= r5) goto L20;
        r0 = r0.copyWithAppendedEntriesFrom(r10[r1]);
        r1 = r1 + 1;
        goto L17
    L20:
        if (r0.length() <= 0) goto L29;
        r9.setMetadata(r0);
        return;
    }
}
