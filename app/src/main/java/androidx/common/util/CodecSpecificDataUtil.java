package androidx.media3.common.util;

import android.util.Pair;
import androidx.exifinterface.media.ExifInterface;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.extractor.metadata.icy.IcyHeaders;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class CodecSpecificDataUtil {
    private static final String CODEC_ID_AV01 = "av01";
    private static final String CODEC_ID_AVC1 = "avc1";
    private static final String CODEC_ID_AVC2 = "avc2";
    private static final String CODEC_ID_H263 = "s263";
    private static final String CODEC_ID_HEV1 = "hev1";
    private static final String CODEC_ID_HVC1 = "hvc1";
    private static final String CODEC_ID_MP4A = "mp4a";
    private static final String CODEC_ID_VP09 = "vp09";
    private static final int EXTENDED_PAR = 15;
    private static final String[] HEVC_GENERAL_PROFILE_SPACE_STRINGS = null;
    private static final byte[] NAL_START_CODE = null;
    private static final Pattern PROFILE_PATTERN = null;
    private static final int RECTANGULAR = 0;
    private static final String TAG = "CodecSpecificDataUtil";
    private static final int VISUAL_OBJECT_LAYER = 1;
    private static final int VISUAL_OBJECT_LAYER_START = 32;

    static {
        NAL_START_CODE = new byte[]{0, 0, 0, 1};
        HEVC_GENERAL_PROFILE_SPACE_STRINGS = new String[]{"", ExifInterface.GPS_MEASUREMENT_IN_PROGRESS, "B", "C"};
        PROFILE_PATTERN = Pattern.compile("^\\D?(\\d+)$");
    }

    private CodecSpecificDataUtil() {
    }

    private static int av1LevelNumberToConst(int r1) {
        switch(r1) {
            case 0: goto L51;
            case 1: goto L49;
            case 2: goto L47;
            case 3: goto L45;
            case 4: goto L43;
            case 5: goto L41;
            case 6: goto L39;
            case 7: goto L37;
            case 8: goto L35;
            case 9: goto L33;
            case 10: goto L31;
            case 11: goto L29;
            case 12: goto L27;
            case 13: goto L25;
            case 14: goto L23;
            case 15: goto L21;
            case 16: goto L19;
            case 17: goto L17;
            case 18: goto L15;
            case 19: goto L13;
            case 20: goto L11;
            case 21: goto L9;
            case 22: goto L7;
            case 23: goto L5;
            default: goto L3;
        };
    L3:
        return -1;
    L5:
        return 8388608;
    L7:
        return 4194304;
    L9:
        return 2097152;
    L11:
        return 1048576;
    L13:
        return 524288;
    L15:
        return 262144;
    L17:
        return 131072;
    L19:
        return 65536;
    L21:
        return 32768;
    L23:
        return 16384;
    L25:
        return 8192;
    L27:
        return 4096;
    L29:
        return 2048;
    L31:
        return 1024;
    L33:
        return 512;
    L35:
        return 256;
    L37:
        return 128;
    L39:
        return 64;
    L41:
        return 32;
    L43:
        return 16;
    L45:
        return 8;
    L47:
        return 4;
    L49:
        return 2;
    L51:
        return 1;
    }

    private static int avcLevelNumberToConst(int r1) {
        switch(r1) {
            case 10: goto L35;
            case 11: goto L33;
            case 12: goto L31;
            case 13: goto L29;
            case 20: goto L27;
            case 21: goto L25;
            case 22: goto L23;
            case 30: goto L21;
            case 31: goto L19;
            case 32: goto L17;
            case 40: goto L15;
            case 41: goto L13;
            case 42: goto L11;
            case 50: goto L9;
            case 51: goto L7;
            case 52: goto L5;
            default: goto L3;
        };
    L3:
        return -1;
    L5:
        return 65536;
    L7:
        return 32768;
    L9:
        return 16384;
    L11:
        return 8192;
    L13:
        return 4096;
    L15:
        return 2048;
    L17:
        return 1024;
    L19:
        return 512;
    L21:
        return 256;
    L23:
        return 128;
    L25:
        return 64;
    L27:
        return 32;
    L29:
        return 16;
    L31:
        return 8;
    L33:
        return 4;
    L35:
        return 1;
    }

    private static int avcProfileNumberToConst(int r1) {
        switch(r1) {
            case 66: goto L17;
            case 77: goto L15;
            case 88: goto L13;
            case 100: goto L11;
            case 110: goto L9;
            case 122: goto L7;
            case 244: goto L5;
            default: goto L3;
        };
    L3:
        return -1;
    L5:
        return 64;
    L7:
        return 32;
    L9:
        return 16;
    L11:
        return 8;
    L13:
        return 4;
    L15:
        return 2;
    L17:
        return 1;
    }

    public static String buildAvcCodecString(int r5, int r6, int r7) {
        return String.format("avc1.%02X%02X%02X", new Object[]{Integer.valueOf(r5), Integer.valueOf(r6), Integer.valueOf(r7)});
    }

    public static List<byte[]> buildCea708InitializationData(boolean r3) {
        byte[] r2 = new byte[1];
        if (r3 == false) goto L5;
        r2[0] = 1;
    L7:
        return Collections.singletonList(r2);
    L5:
        r2[0] = 0;
        goto L7
    }

    public static String buildH263CodecString(int r4, int r5) {
        return Util.formatInvariant("s263.%d.%d", new Object[]{Integer.valueOf(r4), Integer.valueOf(r5)});
    }

    public static String buildHevcCodecString(int r8, boolean r9, int r10, int r11, int[] r12, int r13) {
        String r1 = HEVC_GENERAL_PROFILE_SPACE_STRINGS[r8];
        Integer r2 = Integer.valueOf(r10);
        Integer r3 = Integer.valueOf(r11);
        if (r9 == false) goto L5;
        char r4 = 'H';
    L6:
        StringBuilder r0 = new StringBuilder(Util.formatInvariant("hvc1.%s%d.%X.%c%d", new Object[]{r1, r2, r3, Character.valueOf(r4), Integer.valueOf(r13)}));
        int r5 = r12.length;
    L7:
        if (r5 <= 0) goto L11;
        if (r12[r5 - 1] != 0) goto L11;
        r5 = r5 - 1;
    L11:
        int r6 = 0;
    L12:
        if (r6 >= r5) goto L15;
        r0.append(String.format(".%02X", new Object[]{Integer.valueOf(r12[r6])}));
        r6 = r6 + 1;
        goto L12
    L15:
        return r0.toString();
    L5:
        r4 = 'L';
        goto L6
    }

    public static byte[] buildNalUnit(byte[] r4, int r5, int r6) {
        byte[] r0 = new byte[NAL_START_CODE.length + r6];
        System.arraycopy(NAL_START_CODE, 0, r0, 0, NAL_START_CODE.length);
        System.arraycopy(r4, r5, r0, NAL_START_CODE.length, r6);
        return r0;
    }

    public static ImmutableList<byte[]> buildVp9CodecPrivateInitializationData(byte r7, byte r8, byte r9, byte r10) {
        return ImmutableList.m213of(new byte[]{1, 1, r7, 2, 1, r8, 3, 1, r9, 4, 1, r10});
    }

    private static Integer dolbyVisionStringToLevel(String r6) {
        if (r6 != null) goto L6;
        return null;
    L6:
        switch(r6.hashCode()) {
            case 1537: goto L45;
            case 1538: goto L42;
            case 1539: goto L39;
            case 1540: goto L36;
            case 1541: goto L33;
            case 1542: goto L30;
            case 1543: goto L27;
            case 1544: goto L24;
            case 1545: goto L21;
            case 1567: goto L18;
            case 1568: goto L15;
            case 1569: goto L12;
            case 1570: goto L9;
            default: goto L47;
        };
    L47:
        byte r1 = -1;
    L48:
        switch(r1) {
            case 0: goto L75;
            case 1: goto L73;
            case 2: goto L71;
            case 3: goto L69;
            case 4: goto L67;
            case 5: goto L65;
            case 6: goto L63;
            case 7: goto L61;
            case 8: goto L59;
            case 9: goto L57;
            case 10: goto L55;
            case 11: goto L53;
            case 12: goto L51;
            default: goto L49;
        };
    L49:
        return null;
    L51:
        return 4096;
    L53:
        return 2048;
    L55:
        return 1024;
    L57:
        return 512;
    L59:
        return 256;
    L61:
        return 128;
    L63:
        return 64;
    L65:
        return 32;
    L67:
        return 16;
    L69:
        return 8;
    L71:
        return 4;
    L73:
        return 2;
    L75:
        return 1;
    L9:
        if (r6.equals("13") == false) goto L47;
        r1 = Ascii.f81FF;
        goto L48
    L12:
        if (r6.equals("12") == false) goto L47;
        r1 = Ascii.f92VT;
        goto L48
    L15:
        if (r6.equals("11") == false) goto L47;
        r1 = 10;
        goto L48
    L18:
        if (r6.equals("10") == false) goto L47;
        r1 = 9;
        goto L48
    L21:
        if (r6.equals("09") == false) goto L47;
        r1 = 8;
        goto L48
    L24:
        if (r6.equals("08") == false) goto L47;
        r1 = 7;
        goto L48
    L27:
        if (r6.equals("07") == false) goto L47;
        r1 = 6;
        goto L48
    L30:
        if (r6.equals("06") == false) goto L47;
        r1 = 5;
        goto L48
    L33:
        if (r6.equals("05") == false) goto L47;
        r1 = 4;
        goto L48
    L36:
        if (r6.equals("04") == false) goto L47;
        r1 = 3;
        goto L48
    L39:
        if (r6.equals("03") == false) goto L47;
        r1 = 2;
        goto L48
    L42:
        if (r6.equals("02") == false) goto L47;
        r1 = 1;
        goto L48
    L45:
        if (r6.equals("01") == false) goto L47;
        r1 = 0;
        goto L48
    }

    private static Integer dolbyVisionStringToProfile(String r6) {
        if (r6 != null) goto L6;
        return null;
    L6:
        switch(r6.hashCode()) {
            case 1536: goto L39;
            case 1537: goto L36;
            case 1538: goto L33;
            case 1539: goto L30;
            case 1540: goto L27;
            case 1541: goto L24;
            case 1542: goto L21;
            case 1543: goto L18;
            case 1544: goto L15;
            case 1545: goto L12;
            case 1567: goto L9;
            default: goto L41;
        };
    L41:
        byte r1 = -1;
    L42:
        switch(r1) {
            case 0: goto L65;
            case 1: goto L63;
            case 2: goto L61;
            case 3: goto L59;
            case 4: goto L57;
            case 5: goto L55;
            case 6: goto L53;
            case 7: goto L51;
            case 8: goto L49;
            case 9: goto L47;
            case 10: goto L45;
            default: goto L43;
        };
    L43:
        return null;
    L45:
        return 1024;
    L47:
        return 512;
    L49:
        return 256;
    L51:
        return 128;
    L53:
        return 64;
    L55:
        return 32;
    L57:
        return 16;
    L59:
        return 8;
    L61:
        return 4;
    L63:
        return 2;
    L65:
        return 1;
    L9:
        if (r6.equals("10") == false) goto L41;
        r1 = 10;
        goto L42
    L12:
        if (r6.equals("09") == false) goto L41;
        r1 = 9;
        goto L42
    L15:
        if (r6.equals("08") == false) goto L41;
        r1 = 8;
        goto L42
    L18:
        if (r6.equals("07") == false) goto L41;
        r1 = 7;
        goto L42
    L21:
        if (r6.equals("06") == false) goto L41;
        r1 = 6;
        goto L42
    L24:
        if (r6.equals("05") == false) goto L41;
        r1 = 5;
        goto L42
    L27:
        if (r6.equals("04") == false) goto L41;
        r1 = 4;
        goto L42
    L30:
        if (r6.equals("03") == false) goto L41;
        r1 = 3;
        goto L42
    L33:
        if (r6.equals("02") == false) goto L41;
        r1 = 2;
        goto L42
    L36:
        if (r6.equals("01") == false) goto L41;
        r1 = 1;
        goto L42
    L39:
        if (r6.equals("00") == false) goto L41;
        r1 = 0;
        goto L42
    }

    private static int findNalStartCode(byte[] r3, int r4) {
        int r0 = r3.length - NAL_START_CODE.length;
        int r1 = r4;
    L3:
        if (r1 > r0) goto L8;
        if (isNalStartCode(r3, r1) == true) goto L6;
        r1 = r1 + 1;
        goto L3
    L6:
        return r1;
    L8:
        return -1;
    }

    private static Pair<Integer, Integer> getAacCodecProfileAndLevel(String r10, String[] r11) {
        if (r11.length == 3) goto L17;
        Log.m48w(TAG, "Ignoring malformed MP4A codec string: " + r10);
        return null;
    L17:
        if (MimeTypes.AUDIO_AAC.equals(MimeTypes.getMimeTypeFromMp4ObjectType(Integer.parseInt(r11[1], 16))) == false) goto L16;
        int r6 = mp4aAudioObjectTypeToProfile(Integer.parseInt(r11[2]));     // Catch: NumberFormatException -> L14
        if (r6 == (-1)) goto L16;
        return new Pair(Integer.valueOf(r6), 0);
    L16:
        return null;
    L15:
        Log.m48w(TAG, "Ignoring malformed MP4A codec string: " + r10);
        goto L16
    }

    private static Pair<Integer, Integer> getAv1ProfileAndLevel(String r9, String[] r10, ColorInfo r11) {
        if (r10.length >= 4) goto L37;
        Log.m48w(TAG, "Ignoring malformed AV1 codec string: " + r9);
        return null;
    L37:
        int r0 = Integer.parseInt(r10[1]);     // Catch: NumberFormatException -> L34
        int r1 = Integer.parseInt(r10[2].substring(0, 2));     // Catch: NumberFormatException -> L34
        int r2 = Integer.parseInt(r10[3]);     // Catch: NumberFormatException -> L34
        if (r0 == 0) goto L12;
        Log.m48w(TAG, "Unknown AV1 profile: " + r0);
        return null;
    L12:
        if (r2 != 8) goto L14;
    L17:
        if (r2 != 8) goto L19;
        int r5 = 1;
    L28:
        int r6 = av1LevelNumberToConst(r1);
        if (r6 != (-1)) goto L33;
        Log.m48w(TAG, "Unknown AV1 level: " + r1);
        return null;
    L33:
        return new Pair(Integer.valueOf(r5), Integer.valueOf(r6));
    L19:
        if (r11 != null) goto L21;
    L27:
        r5 = 2;
        goto L28
    L21:
        if (r11.hdrStaticInfo == null) goto L23;
    L26:
        r5 = 4096;
        goto L28
    L23:
        if (r11.colorTransfer == 7) goto L26;
        if (r11.colorTransfer != 6) goto L27;
    L14:
        if (r2 == 10) goto L17;
        Log.m48w(TAG, "Unknown AV1 bit depth: " + r2);
        return null;
    L35:
        Log.m48w(TAG, "Ignoring malformed AV1 codec string: " + r9);
        return null;
    }

    private static Pair<Integer, Integer> getAvcProfileAndLevel(String r8, String[] r9) {
        if (r9.length >= 2) goto L29;
        Log.m48w(TAG, "Ignoring malformed AVC codec string: " + r8);
        return null;
    L29:
        if (r9[1].length() != 6) goto L11;
        int r4 = Integer.parseInt(r9[1].substring(0, 2), 16);     // Catch: NumberFormatException -> L26
        int r0 = Integer.parseInt(r9[1].substring(4), 16);     // Catch: NumberFormatException -> L26
    L14:
        int r1 = avcProfileNumberToConst(r4);
        if (r1 != (-1)) goto L18;
        Log.m48w(TAG, "Unknown AVC profile: " + r4);
        return null;
    L18:
        int r6 = avcLevelNumberToConst(r0);
        if (r6 != (-1)) goto L23;
        Log.m48w(TAG, "Unknown AVC level: " + r0);
        return null;
    L23:
        return new Pair(Integer.valueOf(r1), Integer.valueOf(r6));
    L11:
        if (r9.length >= 3) goto L13;
        Log.m48w(TAG, "Ignoring malformed AVC codec string: " + r8);     // Catch: NumberFormatException -> L26
        return null;
    L13:
        r4 = Integer.parseInt(r9[1]);     // Catch: NumberFormatException -> L26
        r0 = Integer.parseInt(r9[2]);     // Catch: NumberFormatException -> L26
        goto L14
    L27:
        Log.m48w(TAG, "Ignoring malformed AVC codec string: " + r8);
        return null;
    }

    public static Pair<Integer, Integer> getCodecProfileAndLevel(Format r5) {
        if (r5.codecs != null) goto L5;
        return null;
    L5:
        String[] r0 = r5.codecs.split("\\.");
        if (MimeTypes.VIDEO_DOLBY_VISION.equals(r5.sampleMimeType) == true) goto L8;
        byte r2 = 0;
        String r3 = r0[0];
        switch(r3.hashCode()) {
            case 3004662: goto L34;
            case 3006243: goto L31;
            case 3006244: goto L28;
            case 3199032: goto L25;
            case 3214780: goto L22;
            case 3356560: goto L19;
            case 3475740: goto L16;
            case 3624515: goto L13;
            default: goto L36;
        };
    L36:
        r2 = -1;
    L37:
        switch(r2) {
            case 0: goto L50;
            case 1: goto L48;
            case 2: goto L48;
            case 3: goto L46;
            case 4: goto L44;
            case 5: goto L44;
            case 6: goto L42;
            case 7: goto L40;
            default: goto L38;
        };
    L38:
        return null;
    L40:
        return getAacCodecProfileAndLevel(r5.codecs, r0);
    L42:
        return getAv1ProfileAndLevel(r5.codecs, r0, r5.colorInfo);
    L44:
        return getHevcProfileAndLevel(r5.codecs, r0, r5.colorInfo);
    L46:
        return getVp9ProfileAndLevel(r5.codecs, r0);
    L48:
        return getAvcProfileAndLevel(r5.codecs, r0);
    L50:
        return getH263ProfileAndLevel(r5.codecs, r0);
    L13:
        if (r3.equals(CODEC_ID_VP09) == false) goto L36;
        r2 = 3;
        goto L37
    L16:
        if (r3.equals(CODEC_ID_H263) == false) goto L36;
    L19:
        if (r3.equals(CODEC_ID_MP4A) == false) goto L36;
        r2 = 7;
        goto L37
    L22:
        if (r3.equals(CODEC_ID_HVC1) == false) goto L36;
        r2 = 5;
        goto L37
    L25:
        if (r3.equals(CODEC_ID_HEV1) == false) goto L36;
        r2 = 4;
        goto L37
    L28:
        if (r3.equals(CODEC_ID_AVC2) == false) goto L36;
        r2 = 2;
        goto L37
    L31:
        if (r3.equals(CODEC_ID_AVC1) == false) goto L36;
        r2 = 1;
        goto L37
    L34:
        if (r3.equals(CODEC_ID_AV01) == false) goto L36;
        r2 = 6;
        goto L37
    L8:
        return getDolbyVisionProfileAndLevel(r5.codecs, r0);
    }

    private static Pair<Integer, Integer> getDolbyVisionProfileAndLevel(String r9, String[] r10) {
        if (r10.length >= 3) goto L6;
        Log.m48w(TAG, "Ignoring malformed Dolby Vision codec string: " + r9);
        return null;
    L6:
        Matcher r0 = PROFILE_PATTERN.matcher(r10[1]);
        if (r0.matches() == true) goto L10;
        Log.m48w(TAG, "Ignoring malformed Dolby Vision codec string: " + r9);
        return null;
    L10:
        String r1 = r0.group(1);
        Integer r2 = dolbyVisionStringToProfile(r1);
        if (r2 != null) goto L14;
        Log.m48w(TAG, "Unknown Dolby Vision profile string: " + r1);
        return null;
    L14:
        String r5 = r10[2];
        Integer r6 = dolbyVisionStringToLevel(r5);
        if (r6 != null) goto L19;
        Log.m48w(TAG, "Unknown Dolby Vision level string: " + r5);
        return null;
    L19:
        return new Pair(r2, r6);
    }

    private static Pair<Integer, Integer> getH263ProfileAndLevel(String r8, String[] r9) {
        Pair<Integer, Integer> r0 = new Pair(1, 1);
        if (r9.length >= 3) goto L11;
        Log.m48w(TAG, "Ignoring malformed H263 codec string: " + r8);
        return r0;
    L11:
        return new Pair(Integer.valueOf(Integer.parseInt(r9[1])), Integer.valueOf(Integer.parseInt(r9[2])));
    L9:
        Log.m48w(TAG, "Ignoring malformed H263 codec string: " + r8);
        return r0;
    }

    public static Pair<Integer, Integer> getHevcProfileAndLevel(String r9, String[] r10, ColorInfo r11) {
        if (r10.length >= 4) goto L6;
        Log.m48w(TAG, "Ignoring malformed HEVC codec string: " + r9);
        return null;
    L6:
        Matcher r0 = PROFILE_PATTERN.matcher(r10[1]);
        if (r0.matches() == true) goto L10;
        Log.m48w(TAG, "Ignoring malformed HEVC codec string: " + r9);
        return null;
    L10:
        String r1 = r0.group(1);
        if (IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE.equals(r1) == false) goto L14;
        int r2 = 1;
    L23:
        String r5 = r10[3];
        Integer r6 = hevcCodecStringToProfileLevel(r5);
        if (r6 != null) goto L28;
        Log.m48w(TAG, "Unknown HEVC level string: " + r5);
        return null;
    L28:
        return new Pair(Integer.valueOf(r2), r6);
    L14:
        if (ExifInterface.GPS_MEASUREMENT_2D.equals(r1) == false) goto L21;
        if (r11 != null) goto L17;
    L19:
        r2 = 2;
        goto L23
    L17:
        if (r11.colorTransfer != 6) goto L19;
        r2 = 4096;
        goto L23
    L21:
        if ("6".equals(r1) == false) goto L29;
        r2 = 6;
        goto L23
    L29:
        Log.m48w(TAG, "Unknown HEVC profile string: " + r1);
        return null;
    }

    public static Pair<Integer, Integer> getVideoResolutionFromMpeg4VideoConfig(byte[] r11) {
        int r0 = 0;
        boolean r1 = false;
        ParsableByteArray r2 = new ParsableByteArray(r11);
    L4:
        if ((r0 + 3) >= r11.length) goto L12;
        if (r2.readUnsignedInt24() != 1) goto L11;
        if ((r11[r0 + 3] & 240) != 32) goto L11;
        r1 = true;
    L11:
        r2.setPosition(r2.getPosition() - 2);
        r0 = r0 + 1;
    L12:
        Assertions.checkArgument(r1, "Invalid input: VOL not found.");
        ParsableBitArray r3 = new ParsableBitArray(r11);
        r3.skipBits((r0 + 4) * 8);
        r3.skipBits(1);
        r3.skipBits(8);
        if (r3.readBit() == false) goto L16;
        r3.skipBits(4);
        r3.skipBits(3);
    L16:
        if (r3.readBits(4) != 15) goto L19;
        r3.skipBits(8);
        r3.skipBits(8);
    L19:
        if (r3.readBit() == false) goto L23;
        r3.skipBits(2);
        r3.skipBits(1);
        if (r3.readBit() == false) goto L23;
        r3.skipBits(79);
    L23:
        boolean r7 = false;
        if (r3.readBits(2) != 0) goto L26;
        boolean r8 = true;
    L27:
        Assertions.checkArgument(r8, "Only supports rectangular video object layer shape.");
        Assertions.checkArgument(r3.readBit());
        int r9 = r3.readBits(16);
        Assertions.checkArgument(r3.readBit());
        if (r3.readBit() == false) goto L35;
        if (r9 <= 0) goto L31;
        r7 = true;
    L31:
        Assertions.checkArgument(r7);
        int r10 = r9 - 1;
        int r12 = 0;
    L32:
        if (r10 <= 0) goto L34;
        r12 = r12 + 1;
        r10 = r10 >> 1;
        goto L32
    L34:
        r3.skipBits(r12);
    L35:
        Assertions.checkArgument(r3.readBit());
        int r13 = r3.readBits(13);
        Assertions.checkArgument(r3.readBit());
        int r14 = r3.readBits(13);
        Assertions.checkArgument(r3.readBit());
        r3.skipBits(1);
        return Pair.create(Integer.valueOf(r13), Integer.valueOf(r14));
    L26:
        r8 = false;
        goto L27
    }

    private static Pair<Integer, Integer> getVp9ProfileAndLevel(String r8, String[] r9) {
        if (r9.length >= 3) goto L21;
        Log.m48w(TAG, "Ignoring malformed VP9 codec string: " + r8);
        return null;
    L21:
        int r0 = Integer.parseInt(r9[1]);     // Catch: NumberFormatException -> L18
        int r1 = Integer.parseInt(r9[2]);     // Catch: NumberFormatException -> L18
        int r2 = vp9ProfileNumberToConst(r0);
        if (r2 != (-1)) goto L12;
        Log.m48w(TAG, "Unknown VP9 profile: " + r0);
        return null;
    L12:
        int r6 = vp9LevelNumberToConst(r1);
        if (r6 != (-1)) goto L17;
        Log.m48w(TAG, "Unknown VP9 level: " + r1);
        return null;
    L17:
        return new Pair(Integer.valueOf(r2), Integer.valueOf(r6));
    L19:
        Log.m48w(TAG, "Ignoring malformed VP9 codec string: " + r8);
        return null;
    }

    private static Integer hevcCodecStringToProfileLevel(String r7) {
        if (r7 != null) goto L6;
        return null;
    L6:
        switch(r7.hashCode()) {
            case 70821: goto L84;
            case 70914: goto L81;
            case 70917: goto L78;
            case 71007: goto L75;
            case 71010: goto L72;
            case 74665: goto L69;
            case 74758: goto L66;
            case 74761: goto L63;
            case 74851: goto L60;
            case 74854: goto L57;
            case 2193639: goto L54;
            case 2193642: goto L51;
            case 2193732: goto L48;
            case 2193735: goto L45;
            case 2193738: goto L42;
            case 2193825: goto L39;
            case 2193828: goto L36;
            case 2193831: goto L33;
            case 2312803: goto L30;
            case 2312806: goto L27;
            case 2312896: goto L24;
            case 2312899: goto L21;
            case 2312902: goto L18;
            case 2312989: goto L15;
            case 2312992: goto L12;
            case 2312995: goto L9;
            default: goto L86;
        };
    L86:
        byte r1 = -1;
    L87:
        switch(r1) {
            case 0: goto L140;
            case 1: goto L138;
            case 2: goto L136;
            case 3: goto L134;
            case 4: goto L132;
            case 5: goto L130;
            case 6: goto L128;
            case 7: goto L126;
            case 8: goto L124;
            case 9: goto L122;
            case 10: goto L120;
            case 11: goto L118;
            case 12: goto L116;
            case 13: goto L114;
            case 14: goto L112;
            case 15: goto L110;
            case 16: goto L108;
            case 17: goto L106;
            case 18: goto L104;
            case 19: goto L102;
            case 20: goto L100;
            case 21: goto L98;
            case 22: goto L96;
            case 23: goto L94;
            case 24: goto L92;
            case 25: goto L90;
            default: goto L88;
        };
    L88:
        return null;
    L90:
        return 33554432;
    L92:
        return 8388608;
    L94:
        return 2097152;
    L96:
        return 524288;
    L98:
        return 131072;
    L100:
        return 32768;
    L102:
        return 8192;
    L104:
        return 2048;
    L106:
        return 512;
    L108:
        return 128;
    L110:
        return 32;
    L112:
        return 8;
    L114:
        return 2;
    L116:
        return 16777216;
    L118:
        return 4194304;
    L120:
        return 1048576;
    L122:
        return 262144;
    L124:
        return 65536;
    L126:
        return 16384;
    L128:
        return 4096;
    L130:
        return 1024;
    L132:
        return 256;
    L134:
        return 64;
    L136:
        return 16;
    L138:
        return 4;
    L140:
        return 1;
    L9:
        if (r7.equals("L186") == false) goto L86;
        r1 = Ascii.f81FF;
        goto L87
    L12:
        if (r7.equals("L183") == false) goto L86;
        r1 = Ascii.f92VT;
        goto L87
    L15:
        if (r7.equals("L180") == false) goto L86;
        r1 = 10;
        goto L87
    L18:
        if (r7.equals("L156") == false) goto L86;
        r1 = 9;
        goto L87
    L21:
        if (r7.equals("L153") == false) goto L86;
        r1 = 8;
        goto L87
    L24:
        if (r7.equals("L150") == false) goto L86;
        r1 = 7;
        goto L87
    L27:
        if (r7.equals("L123") == false) goto L86;
        r1 = 6;
        goto L87
    L30:
        if (r7.equals("L120") == false) goto L86;
        r1 = 5;
        goto L87
    L33:
        if (r7.equals("H186") == false) goto L86;
        r1 = Ascii.f80EM;
        goto L87
    L36:
        if (r7.equals("H183") == false) goto L86;
        r1 = Ascii.CAN;
        goto L87
    L39:
        if (r7.equals("H180") == false) goto L86;
        r1 = Ascii.ETB;
        goto L87
    L42:
        if (r7.equals("H156") == false) goto L86;
        r1 = Ascii.SYN;
        goto L87
    L45:
        if (r7.equals("H153") == false) goto L86;
        r1 = Ascii.NAK;
        goto L87
    L48:
        if (r7.equals("H150") == false) goto L86;
        r1 = Ascii.DC4;
        goto L87
    L51:
        if (r7.equals("H123") == false) goto L86;
        r1 = 19;
        goto L87
    L54:
        if (r7.equals("H120") == false) goto L86;
        r1 = Ascii.DC2;
        goto L87
    L57:
        if (r7.equals("L93") == false) goto L86;
        r1 = 4;
        goto L87
    L60:
        if (r7.equals("L90") == false) goto L86;
        r1 = 3;
        goto L87
    L63:
        if (r7.equals("L63") == false) goto L86;
        r1 = 2;
        goto L87
    L66:
        if (r7.equals("L60") == false) goto L86;
        r1 = 1;
        goto L87
    L69:
        if (r7.equals("L30") == false) goto L86;
        r1 = 0;
        goto L87
    L72:
        if (r7.equals("H93") == false) goto L86;
        r1 = 17;
        goto L87
    L75:
        if (r7.equals("H90") == false) goto L86;
        r1 = 16;
        goto L87
    L78:
        if (r7.equals("H63") == false) goto L86;
        r1 = Ascii.f88SI;
        goto L87
    L81:
        if (r7.equals("H60") == false) goto L86;
        r1 = Ascii.f89SO;
        goto L87
    L84:
        if (r7.equals("H30") == false) goto L86;
        r1 = Ascii.f79CR;
        goto L87
    }

    private static boolean isNalStartCode(byte[] r4, int r5) {
        if ((r4.length - r5) > NAL_START_CODE.length) goto L5;
        return false;
    L5:
        int r0 = 0;
    L7:
        if (r0 >= NAL_START_CODE.length) goto L12;
        if (r4[r5 + r0] != NAL_START_CODE[r0]) goto L10;
        r0 = r0 + 1;
        goto L7
    L10:
        return false;
    L12:
        return true;
    }

    private static int mp4aAudioObjectTypeToProfile(int r1) {
        switch(r1) {
            case 1: goto L27;
            case 2: goto L25;
            case 3: goto L23;
            case 4: goto L21;
            case 5: goto L19;
            case 6: goto L17;
            case 17: goto L15;
            case 20: goto L13;
            case 23: goto L11;
            case 29: goto L9;
            case 39: goto L7;
            case 42: goto L5;
            default: goto L3;
        };
    L3:
        return -1;
    L5:
        return 42;
    L7:
        return 39;
    L9:
        return 29;
    L11:
        return 23;
    L13:
        return 20;
    L15:
        return 17;
    L17:
        return 6;
    L19:
        return 5;
    L21:
        return 4;
    L23:
        return 3;
    L25:
        return 2;
    L27:
        return 1;
    }

    public static Pair<Integer, Integer> parseAlacAudioSpecificConfig(byte[] r5) {
        ParsableByteArray r0 = new ParsableByteArray(r5);
        r0.setPosition(9);
        int r1 = r0.readUnsignedByte();
        r0.setPosition(20);
        return Pair.create(Integer.valueOf(r0.readUnsignedIntToInt()), Integer.valueOf(r1));
    }

    public static boolean parseCea708InitializationData(List<byte[]> r3) {
        if (r3.size() == 1) goto L5;
        return false;
    L5:
        if (r3.get(0).length == 1) goto L7;
        return false;
    L7:
        if (r3.get(0)[0] == 1) goto L10;
        return false;
    L10:
        return true;
    }

    public static byte[][] splitNalUnits(byte[] r9) {
        if (isNalStartCode(r9, 0) == true) goto L6;
        return null;
    L6:
        ArrayList r1 = new ArrayList();
        int r2 = 0;
    L7:
        r1.add(Integer.valueOf(r2));
        r2 = findNalStartCode(r9, NAL_START_CODE.length + r2);
        if (r2 != (-1)) goto L7;
        byte[][] r3 = new byte[r1.size()][];
        int r4 = 0;
    L11:
        if (r4 >= r1.size()) goto L17;
        int r5 = ((Integer) r1.get(r4)).intValue();
        if (r4 >= (r1.size() - 1)) goto L15;
        int r6 = ((Integer) r1.get(r4 + 1)).intValue();
    L16:
        byte[] r7 = new byte[r6 - r5];
        System.arraycopy(r9, r5, r7, 0, r7.length);
        r3[r4] = r7;
        r4 = r4 + 1;
        goto L11
    L15:
        r6 = r9.length;
        goto L16
    L17:
        return r3;
    }

    private static int vp9LevelNumberToConst(int r1) {
        switch(r1) {
            case 10: goto L29;
            case 11: goto L27;
            case 20: goto L25;
            case 21: goto L23;
            case 30: goto L21;
            case 31: goto L19;
            case 40: goto L17;
            case 41: goto L15;
            case 50: goto L13;
            case 51: goto L11;
            case 60: goto L9;
            case 61: goto L7;
            case 62: goto L5;
            default: goto L3;
        };
    L3:
        return -1;
    L5:
        return 8192;
    L7:
        return 4096;
    L9:
        return 2048;
    L11:
        return 512;
    L13:
        return 256;
    L15:
        return 128;
    L17:
        return 64;
    L19:
        return 32;
    L21:
        return 16;
    L23:
        return 8;
    L25:
        return 4;
    L27:
        return 2;
    L29:
        return 1;
    }

    private static int vp9ProfileNumberToConst(int r1) {
        switch(r1) {
            case 0: goto L11;
            case 1: goto L9;
            case 2: goto L7;
            case 3: goto L5;
            default: goto L3;
        };
    L3:
        return -1;
    L5:
        return 8;
    L7:
        return 4;
    L9:
        return 2;
    L11:
        return 1;
    }
}
