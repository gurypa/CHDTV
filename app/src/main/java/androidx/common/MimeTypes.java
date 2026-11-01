package androidx.media3.common;

import android.text.TextUtils;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import com.google.common.base.Ascii;
import com.google.common.primitives.SignedBytes;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.checkerframework.dataflow.qual.Pure;

/* JADX INFO: loaded from: classes.dex */
public final class MimeTypes {
    public static final String APPLICATION_AIT = "application/vnd.dvb.ait";
    public static final String APPLICATION_CAMERA_MOTION = "application/x-camera-motion";
    public static final String APPLICATION_CEA608 = "application/cea-608";
    public static final String APPLICATION_CEA708 = "application/cea-708";
    public static final String APPLICATION_DEPTH_METADATA = "application/x-depth-metadata";
    public static final String APPLICATION_DVBSUBS = "application/dvbsubs";
    public static final String APPLICATION_EMSG = "application/x-emsg";
    public static final String APPLICATION_EXIF = "application/x-exif";
    public static final String APPLICATION_EXTERNALLY_LOADED_IMAGE = "application/x-image-uri";
    public static final String APPLICATION_ICY = "application/x-icy";
    public static final String APPLICATION_ID3 = "application/id3";
    public static final String APPLICATION_M3U8 = "application/x-mpegURL";
    public static final String APPLICATION_MATROSKA = "application/x-matroska";
    public static final String APPLICATION_MEDIA3_CUES = "application/x-media3-cues";
    public static final String APPLICATION_MP4 = "application/mp4";
    public static final String APPLICATION_MP4CEA608 = "application/x-mp4-cea-608";
    public static final String APPLICATION_MP4VTT = "application/x-mp4-vtt";
    public static final String APPLICATION_MPD = "application/dash+xml";
    public static final String APPLICATION_PGS = "application/pgs";

    @Deprecated
    public static final String APPLICATION_RAWCC = "application/x-rawcc";
    public static final String APPLICATION_RTSP = "application/x-rtsp";
    public static final String APPLICATION_SCTE35 = "application/x-scte35";
    public static final String APPLICATION_SDP = "application/sdp";
    public static final String APPLICATION_SS = "application/vnd.ms-sstr+xml";
    public static final String APPLICATION_SUBRIP = "application/x-subrip";
    public static final String APPLICATION_TTML = "application/ttml+xml";
    public static final String APPLICATION_TX3G = "application/x-quicktime-tx3g";
    public static final String APPLICATION_VOBSUB = "application/vobsub";
    public static final String APPLICATION_WEBM = "application/webm";
    public static final String AUDIO_AAC = "audio/mp4a-latm";
    public static final String AUDIO_AC3 = "audio/ac3";
    public static final String AUDIO_AC4 = "audio/ac4";
    public static final String AUDIO_ALAC = "audio/alac";
    public static final String AUDIO_ALAW = "audio/g711-alaw";
    public static final String AUDIO_AMR = "audio/amr";
    public static final String AUDIO_AMR_NB = "audio/3gpp";
    public static final String AUDIO_AMR_WB = "audio/amr-wb";
    public static final String AUDIO_DTS = "audio/vnd.dts";
    public static final String AUDIO_DTS_EXPRESS = "audio/vnd.dts.hd;profile=lbr";
    public static final String AUDIO_DTS_HD = "audio/vnd.dts.hd";
    public static final String AUDIO_DTS_X = "audio/vnd.dts.uhd;profile=p2";
    public static final String AUDIO_EXOPLAYER_MIDI = "audio/x-exoplayer-midi";
    public static final String AUDIO_E_AC3 = "audio/eac3";
    public static final String AUDIO_E_AC3_JOC = "audio/eac3-joc";
    public static final String AUDIO_FLAC = "audio/flac";
    public static final String AUDIO_IAMF = "audio/iamf";
    public static final String AUDIO_MATROSKA = "audio/x-matroska";
    public static final String AUDIO_MIDI = "audio/midi";
    public static final String AUDIO_MLAW = "audio/g711-mlaw";
    public static final String AUDIO_MP4 = "audio/mp4";
    public static final String AUDIO_MPEG = "audio/mpeg";
    public static final String AUDIO_MPEGH_MHA1 = "audio/mha1";
    public static final String AUDIO_MPEGH_MHM1 = "audio/mhm1";
    public static final String AUDIO_MPEG_L1 = "audio/mpeg-L1";
    public static final String AUDIO_MPEG_L2 = "audio/mpeg-L2";
    public static final String AUDIO_MSGSM = "audio/gsm";
    public static final String AUDIO_OGG = "audio/ogg";
    public static final String AUDIO_OPUS = "audio/opus";
    public static final String AUDIO_RAW = "audio/raw";
    public static final String AUDIO_TRUEHD = "audio/true-hd";
    public static final String AUDIO_UNKNOWN = "audio/x-unknown";
    public static final String AUDIO_VORBIS = "audio/vorbis";
    public static final String AUDIO_WAV = "audio/wav";
    public static final String AUDIO_WEBM = "audio/webm";
    public static final String BASE_TYPE_APPLICATION = "application";
    public static final String BASE_TYPE_AUDIO = "audio";
    public static final String BASE_TYPE_IMAGE = "image";
    public static final String BASE_TYPE_TEXT = "text";
    public static final String BASE_TYPE_VIDEO = "video";
    public static final String CODEC_E_AC3_JOC = "ec+3";
    public static final String IMAGE_AVIF = "image/avif";
    public static final String IMAGE_BMP = "image/bmp";
    public static final String IMAGE_HEIC = "image/heic";
    public static final String IMAGE_HEIF = "image/heif";
    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String IMAGE_JPEG_R = "image/jpeg_r";
    public static final String IMAGE_PNG = "image/png";
    public static final String IMAGE_RAW = "image/raw";
    public static final String IMAGE_WEBP = "image/webp";
    private static final Pattern MP4A_RFC_6381_CODEC_PATTERN = null;
    public static final String TEXT_SSA = "text/x-ssa";
    public static final String TEXT_UNKNOWN = "text/x-unknown";
    public static final String TEXT_VTT = "text/vtt";
    public static final String VIDEO_AV1 = "video/av01";
    public static final String VIDEO_AVI = "video/x-msvideo";
    public static final String VIDEO_DIVX = "video/divx";
    public static final String VIDEO_DOLBY_VISION = "video/dolby-vision";
    public static final String VIDEO_FLV = "video/x-flv";
    public static final String VIDEO_H263 = "video/3gpp";
    public static final String VIDEO_H264 = "video/avc";
    public static final String VIDEO_H265 = "video/hevc";
    public static final String VIDEO_MATROSKA = "video/x-matroska";
    public static final String VIDEO_MJPEG = "video/mjpeg";
    public static final String VIDEO_MP2T = "video/mp2t";
    public static final String VIDEO_MP4 = "video/mp4";
    public static final String VIDEO_MP42 = "video/mp42";
    public static final String VIDEO_MP43 = "video/mp43";
    public static final String VIDEO_MP4V = "video/mp4v-es";
    public static final String VIDEO_MPEG = "video/mpeg";
    public static final String VIDEO_MPEG2 = "video/mpeg2";
    public static final String VIDEO_MV_HEVC = "video/mv-hevc";
    public static final String VIDEO_OGG = "video/ogg";
    public static final String VIDEO_PS = "video/mp2p";
    public static final String VIDEO_RAW = "video/raw";
    public static final String VIDEO_UNKNOWN = "video/x-unknown";
    public static final String VIDEO_VC1 = "video/wvc1";
    public static final String VIDEO_VP8 = "video/x-vnd.on2.vp8";
    public static final String VIDEO_VP9 = "video/x-vnd.on2.vp9";
    public static final String VIDEO_WEBM = "video/webm";
    private static final ArrayList<CustomMimeType> customMimeTypes = null;

    private static final class CustomMimeType {
        public final String codecPrefix;
        public final String mimeType;
        public final int trackType;

        public CustomMimeType(String r1, String r2, int r3) {
            this.mimeType = r1;
            this.codecPrefix = r2;
            this.trackType = r3;
        }
    }

    static final class Mp4aObjectType {
        public final int audioObjectTypeIndication;
        public final int objectTypeIndication;

        public Mp4aObjectType(int r1, int r2) {
            this.objectTypeIndication = r1;
            this.audioObjectTypeIndication = r2;
        }

        public int getEncoding() {
            switch(this.audioObjectTypeIndication) {
                case 2: goto L16;
                case 5: goto L14;
                case 22: goto L12;
                case 23: goto L10;
                case 29: goto L8;
                case 42: goto L6;
                default: goto L4;
            };
        L4:
            return 0;
        L6:
            return 16;
        L8:
            return 12;
        L10:
            return 15;
        L12:
            return 1073741824;
        L14:
            return 11;
        L16:
            return 10;
        }
    }

    static {
        customMimeTypes = new ArrayList();
        MP4A_RFC_6381_CODEC_PATTERN = Pattern.compile("^mp4a\\.([a-zA-Z0-9]{2})(?:\\.([0-9]{1,2}))?$");
    }

    private MimeTypes() {
    }

    public static boolean allSamplesAreSyncSamples(String r5, String r6) {
        if (r5 != null) goto L6;
        return false;
    L6:
        switch(r5.hashCode()) {
            case -2123537834: goto L39;
            case -432837260: goto L36;
            case -432837259: goto L33;
            case -53558318: goto L30;
            case 187078296: goto L27;
            case 187094639: goto L24;
            case 1504578661: goto L21;
            case 1504619009: goto L18;
            case 1504831518: goto L15;
            case 1903231877: goto L12;
            case 1903589369: goto L9;
            default: goto L41;
        };
    L41:
        byte r1 = -1;
    L42:
        switch(r1) {
            case 0: goto L55;
            case 1: goto L55;
            case 2: goto L55;
            case 3: goto L55;
            case 4: goto L55;
            case 5: goto L55;
            case 6: goto L55;
            case 7: goto L55;
            case 8: goto L55;
            case 9: goto L55;
            case 10: goto L44;
            default: goto L43;
        };
    L43:
        return false;
    L44:
        if (r6 != null) goto L46;
        return false;
    L46:
        Mp4aObjectType r2 = getObjectTypeFromMp4aRFC6381CodecString(r6);
        if (r2 != null) goto L49;
        return false;
    L49:
        int r3 = r2.getEncoding();
        if (r3 != 0) goto L52;
        return false;
    L52:
        if (r3 != 16) goto L54;
        return false;
    L54:
        return true;
    L55:
        return true;
    L9:
        if (r5.equals(AUDIO_MLAW) == false) goto L41;
        r1 = 5;
        goto L42
    L12:
        if (r5.equals(AUDIO_ALAW) == false) goto L41;
        r1 = 4;
        goto L42
    L15:
        if (r5.equals(AUDIO_MPEG) == false) goto L41;
        r1 = 0;
        goto L42
    L18:
        if (r5.equals(AUDIO_FLAC) == false) goto L41;
        r1 = 6;
        goto L42
    L21:
        if (r5.equals(AUDIO_E_AC3) == false) goto L41;
        r1 = 8;
        goto L42
    L24:
        if (r5.equals(AUDIO_RAW) == false) goto L41;
        r1 = 3;
        goto L42
    L27:
        if (r5.equals(AUDIO_AC3) == false) goto L41;
        r1 = 7;
        goto L42
    L30:
        if (r5.equals(AUDIO_AAC) == false) goto L41;
        r1 = 10;
        goto L42
    L33:
        if (r5.equals(AUDIO_MPEG_L2) == false) goto L41;
        r1 = 2;
        goto L42
    L36:
        if (r5.equals(AUDIO_MPEG_L1) == false) goto L41;
        r1 = 1;
        goto L42
    L39:
        if (r5.equals(AUDIO_E_AC3_JOC) == false) goto L41;
        r1 = 9;
        goto L42
    }

    public static boolean containsCodecsCorrespondingToMimeType(String r1, String r2) {
        if (getCodecsCorrespondingToMimeType(r1, r2) == null) goto L5;
        return true;
    L5:
        return false;
    }

    public static String getAudioMediaMimeType(String r7) {
        if (r7 != null) goto L5;
        return null;
    L5:
        String[] r1 = Util.splitCodecs(r7);
        int r2 = r1.length;
        int r3 = 0;
    L6:
        if (r3 >= r2) goto L13;
        String r5 = getMediaMimeType(r1[r3]);
        if (r5 == null) goto L12;
        if (isAudio(r5) == false) goto L12;
        return r5;
    L12:
        r3 = r3 + 1;
        goto L6
    L13:
        return null;
    }

    public static String getCodecsCorrespondingToMimeType(String r7, String r8) {
        if (r7 == null) goto L19;
        if (r8 == null) goto L19;
        String[] r1 = Util.splitCodecs(r7);
        StringBuilder r2 = new StringBuilder();
        int r3 = r1.length;
        int r4 = 0;
    L7:
        if (r4 >= r3) goto L16;
        String r5 = r1[r4];
        if (r8.equals(getMediaMimeType(r5)) == false) goto L14;
        if (r2.length() <= 0) goto L13;
        r2.append(",");
    L13:
        r2.append(r5);
    L14:
        r4 = r4 + 1;
        goto L7
    L16:
        if (r2.length() > 0) goto L18;
        return null;
    L18:
        return r2.toString();
    L19:
        return null;
    }

    private static String getCustomMimeTypeForCodec(String r4) {
        int r0 = customMimeTypes.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L9;
        CustomMimeType r2 = customMimeTypes.get(r1);
        if (r4.startsWith(r2.codecPrefix) == true) goto L7;
        r1 = r1 + 1;
        goto L3
    L7:
        return r2.mimeType;
    L9:
        return null;
    }

    public static int getEncoding(String r7, String r8) {
        switch(r7.hashCode()) {
            case -2123537834: goto L39;
            case -1365340241: goto L36;
            case -1095064472: goto L33;
            case -53558318: goto L30;
            case 187078296: goto L27;
            case 187078297: goto L24;
            case 550520934: goto L21;
            case 1504578661: goto L18;
            case 1504831518: goto L15;
            case 1504891608: goto L12;
            case 1505942594: goto L9;
            case 1556697186: goto L6;
            default: goto L41;
        };
    L41:
        byte r0 = -1;
    L42:
        switch(r0) {
            case 0: goto L66;
            case 1: goto L59;
            case 2: goto L58;
            case 3: goto L57;
            case 4: goto L55;
            case 5: goto L53;
            case 6: goto L52;
            case 7: goto L51;
            case 8: goto L50;
            case 9: goto L48;
            case 10: goto L46;
            case 11: goto L44;
            default: goto L43;
        };
    L43:
        return 0;
    L44:
        return 20;
    L46:
        return 14;
    L48:
        return 30;
    L50:
        return 8;
    L51:
        return 8;
    L52:
        return 7;
    L53:
        return 17;
    L55:
        return 18;
    L57:
        return 6;
    L58:
        return 5;
    L59:
        if (r8 != null) goto L61;
        return 0;
    L61:
        Mp4aObjectType r1 = getObjectTypeFromMp4aRFC6381CodecString(r8);
        if (r1 != null) goto L65;
        return 0;
    L65:
        return r1.getEncoding();
    L66:
        return 9;
    L6:
        if (r7.equals(AUDIO_TRUEHD) == false) goto L41;
        r0 = 10;
        goto L42
    L9:
        if (r7.equals(AUDIO_DTS_HD) == false) goto L41;
        r0 = 7;
        goto L42
    L12:
        if (r7.equals(AUDIO_OPUS) == false) goto L41;
        r0 = Ascii.f92VT;
        goto L42
    L15:
        if (r7.equals(AUDIO_MPEG) == false) goto L41;
        r0 = 0;
        goto L42
    L18:
        if (r7.equals(AUDIO_E_AC3) == false) goto L41;
        r0 = 3;
        goto L42
    L21:
        if (r7.equals(AUDIO_DTS_X) == false) goto L41;
        r0 = 9;
        goto L42
    L24:
        if (r7.equals(AUDIO_AC4) == false) goto L41;
        r0 = 5;
        goto L42
    L27:
        if (r7.equals(AUDIO_AC3) == false) goto L41;
        r0 = 2;
        goto L42
    L30:
        if (r7.equals(AUDIO_AAC) == false) goto L41;
        r0 = 1;
        goto L42
    L33:
        if (r7.equals(AUDIO_DTS) == false) goto L41;
        r0 = 6;
        goto L42
    L36:
        if (r7.equals(AUDIO_DTS_EXPRESS) == false) goto L41;
        r0 = 8;
        goto L42
    L39:
        if (r7.equals(AUDIO_E_AC3_JOC) == false) goto L41;
        r0 = 4;
        goto L42
    }

    public static String getMediaMimeType(String r3) {
        if (r3 != null) goto L5;
        return null;
    L5:
        String r4 = Ascii.toLowerCase(r3.trim());
        if (r4.startsWith("avc1") == false) goto L8;
        return VIDEO_H264;
    L8:
        if (r4.startsWith("avc3") == false) goto L11;
        return VIDEO_H264;
    L11:
        if (r4.startsWith("hev1") == false) goto L13;
        return VIDEO_H265;
    L13:
        if (r4.startsWith("hvc1") == false) goto L16;
        return VIDEO_H265;
    L16:
        if (r4.startsWith("dvav") == false) goto L18;
        return VIDEO_DOLBY_VISION;
    L18:
        if (r4.startsWith("dva1") == false) goto L20;
        return VIDEO_DOLBY_VISION;
    L20:
        if (r4.startsWith("dvhe") == false) goto L22;
        return VIDEO_DOLBY_VISION;
    L22:
        if (r4.startsWith("dvh1") == false) goto L25;
        return VIDEO_DOLBY_VISION;
    L25:
        if (r4.startsWith("av01") == false) goto L29;
        return VIDEO_AV1;
    L29:
        if (r4.startsWith("vp9") == false) goto L31;
        return VIDEO_VP9;
    L31:
        if (r4.startsWith("vp09") == false) goto L34;
        return VIDEO_VP9;
    L34:
        if (r4.startsWith("vp8") == false) goto L36;
        return VIDEO_VP8;
    L36:
        if (r4.startsWith("vp08") == false) goto L39;
        return VIDEO_VP8;
    L39:
        if (r4.startsWith("mp4a") == false) goto L50;
        String r0 = null;
        if (r4.startsWith("mp4a.") == false) goto L45;
        Mp4aObjectType r1 = getObjectTypeFromMp4aRFC6381CodecString(r4);
        if (r1 == null) goto L45;
        r0 = getMimeTypeFromMp4ObjectType(r1.objectTypeIndication);
    L45:
        if (r0 != null) goto L48;
        return AUDIO_AAC;
    L48:
        return r0;
    L50:
        if (r4.startsWith("mha1") == false) goto L54;
        return AUDIO_MPEGH_MHA1;
    L54:
        if (r4.startsWith("mhm1") == false) goto L58;
        return AUDIO_MPEGH_MHM1;
    L58:
        if (r4.startsWith("ac-3") == false) goto L60;
        return AUDIO_AC3;
    L60:
        if (r4.startsWith("dac3") == false) goto L63;
        return AUDIO_AC3;
    L63:
        if (r4.startsWith("ec-3") == false) goto L65;
        return AUDIO_E_AC3;
    L65:
        if (r4.startsWith("dec3") == false) goto L68;
        return AUDIO_E_AC3;
    L68:
        if (r4.startsWith(CODEC_E_AC3_JOC) == false) goto L72;
        return AUDIO_E_AC3_JOC;
    L72:
        if (r4.startsWith("ac-4") == false) goto L74;
        return AUDIO_AC4;
    L74:
        if (r4.startsWith("dac4") == false) goto L77;
        return AUDIO_AC4;
    L77:
        if (r4.startsWith("dtsc") == false) goto L81;
        return AUDIO_DTS;
    L81:
        if (r4.startsWith("dtse") == false) goto L85;
        return AUDIO_DTS_EXPRESS;
    L85:
        if (r4.startsWith("dtsh") == false) goto L87;
        return AUDIO_DTS_HD;
    L87:
        if (r4.startsWith("dtsl") == false) goto L90;
        return AUDIO_DTS_HD;
    L90:
        if (r4.startsWith("dtsx") == false) goto L94;
        return AUDIO_DTS_X;
    L94:
        if (r4.startsWith("opus") == false) goto L98;
        return AUDIO_OPUS;
    L98:
        if (r4.startsWith("vorbis") == false) goto L102;
        return AUDIO_VORBIS;
    L102:
        if (r4.startsWith("flac") == false) goto L106;
        return AUDIO_FLAC;
    L106:
        if (r4.startsWith("stpp") == false) goto L110;
        return APPLICATION_TTML;
    L110:
        if (r4.startsWith("wvtt") == false) goto L114;
        return TEXT_VTT;
    L114:
        if (r4.contains("cea708") == false) goto L118;
        return APPLICATION_CEA708;
    L118:
        if (r4.contains("eia608") == false) goto L120;
        return APPLICATION_CEA608;
    L120:
        if (r4.contains("cea608") == false) goto L123;
        return APPLICATION_CEA608;
    L123:
        return getCustomMimeTypeForCodec(r4);
    }

    public static String getMimeTypeFromMp4ObjectType(int r1) {
        switch(r1) {
            case 32: goto L37;
            case 33: goto L35;
            case 35: goto L33;
            case 64: goto L31;
            case 96: goto L29;
            case 97: goto L29;
            case 98: goto L29;
            case 99: goto L29;
            case 100: goto L29;
            case 101: goto L29;
            case 102: goto L31;
            case 103: goto L31;
            case 104: goto L31;
            case 105: goto L27;
            case 106: goto L25;
            case 107: goto L27;
            case 108: goto L23;
            case 163: goto L21;
            case 165: goto L19;
            case 166: goto L17;
            case 169: goto L15;
            case 170: goto L13;
            case 171: goto L13;
            case 172: goto L15;
            case 173: goto L11;
            case 174: goto L9;
            case 177: goto L7;
            case 221: goto L5;
            default: goto L3;
        };
    L3:
        return null;
    L5:
        return AUDIO_VORBIS;
    L7:
        return VIDEO_VP9;
    L9:
        return AUDIO_AC4;
    L11:
        return AUDIO_OPUS;
    L13:
        return AUDIO_DTS_HD;
    L15:
        return AUDIO_DTS;
    L17:
        return AUDIO_E_AC3;
    L19:
        return AUDIO_AC3;
    L21:
        return VIDEO_VC1;
    L23:
        return IMAGE_JPEG;
    L25:
        return VIDEO_MPEG;
    L27:
        return AUDIO_MPEG;
    L29:
        return VIDEO_MPEG2;
    L31:
        return AUDIO_AAC;
    L33:
        return VIDEO_H265;
    L35:
        return VIDEO_H264;
    L37:
        return VIDEO_MP4V;
    }

    public static Byte getMp4ObjectTypeFromMimeType(String r1) {
        switch(r1.hashCode()) {
            case -1003765268: goto L12;
            case -53558318: goto L9;
            case 1187890754: goto L6;
            default: goto L14;
        };
    L14:
        byte r0 = -1;
    L15:
        switch(r0) {
            case 0: goto L23;
            case 1: goto L21;
            case 2: goto L19;
            default: goto L16;
        };
    L16:
        return null;
    L19:
        return (byte) 32;
    L21:
        return (byte) -35;
    L23:
        return Byte.valueOf(SignedBytes.MAX_POWER_OF_TWO);
    L6:
        if (r1.equals(VIDEO_MP4V) == false) goto L14;
        r0 = 2;
        goto L15
    L9:
        if (r1.equals(AUDIO_AAC) == false) goto L14;
        r0 = 0;
        goto L15
    L12:
        if (r1.equals(AUDIO_VORBIS) == false) goto L14;
        r0 = 1;
        goto L15
    }

    static Mp4aObjectType getObjectTypeFromMp4aRFC6381CodecString(String r6) {
        Matcher r0 = MP4A_RFC_6381_CODEC_PATTERN.matcher(r6);
        if (r0.matches() == true) goto L5;
        return null;
    L5:
        String r1 = (String) Assertions.checkNotNull(r0.group(1));
        String r3 = r0.group(2);
        int r4 = 0;
        int r5 = Integer.parseInt(r1, 16);     // Catch: NumberFormatException -> L12
        if (r3 == null) goto L11;
        r4 = Integer.parseInt(r3);     // Catch: NumberFormatException -> L12
    L11:
        return new Mp4aObjectType(r5, r4);
    L13:
        return null;
    }

    public static String getTextMediaMimeType(String r7) {
        if (r7 != null) goto L5;
        return null;
    L5:
        String[] r1 = Util.splitCodecs(r7);
        int r2 = r1.length;
        int r3 = 0;
    L6:
        if (r3 >= r2) goto L13;
        String r5 = getMediaMimeType(r1[r3]);
        if (r5 == null) goto L12;
        if (isText(r5) == false) goto L12;
        return r5;
    L12:
        r3 = r3 + 1;
        goto L6
    L13:
        return null;
    }

    private static String getTopLevelType(String r3) {
        if (r3 != null) goto L5;
        return null;
    L5:
        int r1 = r3.indexOf(47);
        if (r1 != (-1)) goto L9;
        return null;
    L9:
        return r3.substring(0, r1);
    }

    public static int getTrackType(String r1) {
        if (TextUtils.isEmpty(r1) == false) goto L7;
        return -1;
    L7:
        if (isAudio(r1) == false) goto L11;
        return 1;
    L11:
        if (isVideo(r1) == false) goto L15;
        return 2;
    L15:
        if (isText(r1) == false) goto L19;
        return 3;
    L19:
        if (isImage(r1) == false) goto L23;
        return 4;
    L23:
        if (APPLICATION_ID3.equals(r1) == false) goto L25;
        return 5;
    L25:
        if (APPLICATION_EMSG.equals(r1) == false) goto L27;
        return 5;
    L27:
        if (APPLICATION_SCTE35.equals(r1) == false) goto L29;
        return 5;
    L29:
        if (APPLICATION_ICY.equals(r1) == false) goto L31;
        return 5;
    L31:
        if (APPLICATION_AIT.equals(r1) == false) goto L34;
        return 5;
    L34:
        if (APPLICATION_CAMERA_MOTION.equals(r1) == false) goto L38;
        return 6;
    L38:
        return getTrackTypeForCustomMimeType(r1);
    }

    private static int getTrackTypeForCustomMimeType(String r4) {
        int r0 = customMimeTypes.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L9;
        CustomMimeType r2 = customMimeTypes.get(r1);
        if (r4.equals(r2.mimeType) == true) goto L7;
        r1 = r1 + 1;
        goto L3
    L7:
        return r2.trackType;
    L9:
        return -1;
    }

    public static int getTrackTypeOfCodec(String r1) {
        return getTrackType(getMediaMimeType(r1));
    }

    public static String getVideoMediaMimeType(String r7) {
        if (r7 != null) goto L5;
        return null;
    L5:
        String[] r1 = Util.splitCodecs(r7);
        int r2 = r1.length;
        int r3 = 0;
    L6:
        if (r3 >= r2) goto L13;
        String r5 = getMediaMimeType(r1[r3]);
        if (r5 == null) goto L12;
        if (isVideo(r5) == false) goto L12;
        return r5;
    L12:
        r3 = r3 + 1;
        goto L6
    L13:
        return null;
    }

    public static boolean isAudio(String r2) {
        return BASE_TYPE_AUDIO.equals(getTopLevelType(r2));
    }

    public static boolean isImage(String r2) {
        if ("image".equals(getTopLevelType(r2)) == false) goto L5;
    L8:
        return true;
    L5:
        if (APPLICATION_EXTERNALLY_LOADED_IMAGE.equals(r2) == true) goto L8;
        return false;
    }

    public static boolean isMatroska(String r2) {
        if (r2 != null) goto L6;
        return false;
    L6:
        if (r2.startsWith(VIDEO_WEBM) == false) goto L8;
    L17:
        return true;
    L8:
        if (r2.startsWith(AUDIO_WEBM) == true) goto L17;
        if (r2.startsWith(APPLICATION_WEBM) == true) goto L17;
        if (r2.startsWith(VIDEO_MATROSKA) == true) goto L17;
        if (r2.startsWith(AUDIO_MATROSKA) == true) goto L17;
        if (r2.startsWith(APPLICATION_MATROSKA) == true) goto L17;
        return false;
    }

    @Pure
    public static boolean isText(String r2) {
        if ("text".equals(getTopLevelType(r2)) == false) goto L5;
    L30:
        return true;
    L5:
        if (APPLICATION_MEDIA3_CUES.equals(r2) == true) goto L30;
        if (APPLICATION_CEA608.equals(r2) == true) goto L30;
        if (APPLICATION_CEA708.equals(r2) == true) goto L30;
        if (APPLICATION_MP4CEA608.equals(r2) == true) goto L30;
        if (APPLICATION_SUBRIP.equals(r2) == true) goto L30;
        if (APPLICATION_TTML.equals(r2) == true) goto L30;
        if (APPLICATION_TX3G.equals(r2) == true) goto L30;
        if (APPLICATION_MP4VTT.equals(r2) == true) goto L30;
        if (APPLICATION_RAWCC.equals(r2) == true) goto L30;
        if (APPLICATION_VOBSUB.equals(r2) == true) goto L30;
        if (APPLICATION_PGS.equals(r2) == true) goto L30;
        if (APPLICATION_DVBSUBS.equals(r2) == true) goto L30;
        return false;
    }

    public static boolean isVideo(String r2) {
        return BASE_TYPE_VIDEO.equals(getTopLevelType(r2));
    }

    public static String normalizeMimeType(String r1) {
        if (r1 != null) goto L5;
        return null;
    L5:
        String r2 = Ascii.toLowerCase(r1);
        switch(r2.hashCode()) {
            case -1833600100: goto L27;
            case -1007807498: goto L24;
            case -979095690: goto L21;
            case -586683234: goto L18;
            case -432836268: goto L15;
            case -432836267: goto L12;
            case 187090231: goto L9;
            default: goto L29;
        };
    L29:
        byte r0 = -1;
    L30:
        switch(r0) {
            case 0: goto L44;
            case 1: goto L42;
            case 2: goto L40;
            case 3: goto L38;
            case 4: goto L36;
            case 5: goto L34;
            case 6: goto L32;
            default: goto L31;
        };
    L31:
        return r2;
    L32:
        return AUDIO_MPEG_L2;
    L34:
        return AUDIO_MPEG_L1;
    L36:
        return APPLICATION_M3U8;
    L38:
        return AUDIO_WAV;
    L40:
        return AUDIO_MPEG;
    L42:
        return AUDIO_FLAC;
    L44:
        return VIDEO_MV_HEVC;
    L9:
        if (r2.equals("audio/mp3") == false) goto L29;
        r0 = 2;
        goto L30
    L12:
        if (r2.equals("audio/mpeg-l2") == false) goto L29;
        r0 = 6;
        goto L30
    L15:
        if (r2.equals("audio/mpeg-l1") == false) goto L29;
        r0 = 5;
        goto L30
    L18:
        if (r2.equals("audio/x-wav") == false) goto L29;
        r0 = 3;
        goto L30
    L21:
        if (r2.equals("application/x-mpegurl") == false) goto L29;
        r0 = 4;
        goto L30
    L24:
        if (r2.equals("audio/x-flac") == false) goto L29;
        r0 = 1;
        goto L30
    L27:
        if (r2.equals("video/x-mvhevc") == false) goto L29;
        r0 = 0;
        goto L30
    }

    public static void registerCustomMimeType(String r4, String r5, int r6) {
        CustomMimeType r0 = new CustomMimeType(r4, r5, r6);
        int r1 = customMimeTypes.size();
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L8;
        if (r4.equals(customMimeTypes.get(r2).mimeType) == true) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        customMimeTypes.remove(r2);
    L8:
        customMimeTypes.add(r0);
    }
}
