package androidx.media3.common;

import android.net.Uri;
import com.google.common.base.Ascii;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class FileTypes {
    public static final int AC3 = 0;
    public static final int AC4 = 1;
    public static final int ADTS = 2;
    public static final int AMR = 3;
    public static final int AVI = 16;
    public static final int AVIF = 21;
    public static final int BMP = 19;
    private static final String EXTENSION_AAC = ".aac";
    private static final String EXTENSION_AC3 = ".ac3";
    private static final String EXTENSION_AC4 = ".ac4";
    private static final String EXTENSION_ADTS = ".adts";
    private static final String EXTENSION_AMR = ".amr";
    private static final String EXTENSION_AVI = ".avi";
    private static final String EXTENSION_AVIF = ".avif";
    private static final String EXTENSION_BMP = ".bmp";
    private static final String EXTENSION_DIB = ".dib";
    private static final String EXTENSION_EC3 = ".ec3";
    private static final String EXTENSION_FLAC = ".flac";
    private static final String EXTENSION_FLV = ".flv";
    private static final String EXTENSION_HEIC = ".heic";
    private static final String EXTENSION_HEIF = ".heif";
    private static final String EXTENSION_JPEG = ".jpeg";
    private static final String EXTENSION_JPG = ".jpg";
    private static final String EXTENSION_M2P = ".m2p";
    private static final String EXTENSION_MID = ".mid";
    private static final String EXTENSION_MIDI = ".midi";
    private static final String EXTENSION_MP3 = ".mp3";
    private static final String EXTENSION_MP4 = ".mp4";
    private static final String EXTENSION_MPEG = ".mpeg";
    private static final String EXTENSION_MPG = ".mpg";
    private static final String EXTENSION_OPUS = ".opus";
    private static final String EXTENSION_PNG = ".png";
    private static final String EXTENSION_PREFIX_CMF = ".cmf";
    private static final String EXTENSION_PREFIX_M4 = ".m4";
    private static final String EXTENSION_PREFIX_MK = ".mk";
    private static final String EXTENSION_PREFIX_MP4 = ".mp4";
    private static final String EXTENSION_PREFIX_OG = ".og";
    private static final String EXTENSION_PREFIX_TS = ".ts";
    private static final String EXTENSION_PS = ".ps";
    private static final String EXTENSION_SMF = ".smf";
    private static final String EXTENSION_TS = ".ts";
    private static final String EXTENSION_VTT = ".vtt";
    private static final String EXTENSION_WAV = ".wav";
    private static final String EXTENSION_WAVE = ".wave";
    private static final String EXTENSION_WEBM = ".webm";
    private static final String EXTENSION_WEBP = ".webp";
    private static final String EXTENSION_WEBVTT = ".webvtt";
    public static final int FLAC = 4;
    public static final int FLV = 5;
    static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final int HEIF = 20;
    public static final int JPEG = 14;
    public static final int MATROSKA = 6;
    public static final int MIDI = 15;
    public static final int MP3 = 7;
    public static final int MP4 = 8;
    public static final int OGG = 9;
    public static final int PNG = 17;

    /* JADX INFO: renamed from: PS */
    public static final int f24PS = 10;

    /* JADX INFO: renamed from: TS */
    public static final int f25TS = 11;
    public static final int UNKNOWN = -1;
    public static final int WAV = 12;
    public static final int WEBP = 18;
    public static final int WEBVTT = 13;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    private FileTypes() {
    }

    public static int inferFileTypeFromMimeType(String r24) {
        if (r24 != null) goto L5;
        return -1;
    L5:
        String r1 = MimeTypes.normalizeMimeType(r24);
        switch(r1.hashCode()) {
            case -2123537834: goto L102;
            case -1662384011: goto L99;
            case -1662384007: goto L96;
            case -1662095187: goto L93;
            case -1606874997: goto L90;
            case -1487656890: goto L87;
            case -1487464693: goto L84;
            case -1487464690: goto L81;
            case -1487394660: goto L78;
            case -1487018032: goto L75;
            case -1248337486: goto L72;
            case -1079884372: goto L69;
            case -1004728940: goto L66;
            case -879272239: goto L63;
            case -879258763: goto L60;
            case -387023398: goto L57;
            case -43467528: goto L54;
            case 13915911: goto L51;
            case 187078296: goto L48;
            case 187078297: goto L45;
            case 187078669: goto L42;
            case 187090232: goto L39;
            case 187091926: goto L36;
            case 187099443: goto L33;
            case 1331848029: goto L30;
            case 1503095341: goto L27;
            case 1504578661: goto L24;
            case 1504619009: goto L21;
            case 1504824762: goto L18;
            case 1504831518: goto L15;
            case 1505118770: goto L12;
            case 2039520277: goto L9;
            default: goto L104;
        };
    L104:
        byte r2 = -1;
    L105:
        switch(r2) {
            case 0: goto L127;
            case 1: goto L127;
            case 2: goto L127;
            case 3: goto L126;
            case 4: goto L125;
            case 5: goto L125;
            case 6: goto L125;
            case 7: goto L124;
            case 8: goto L123;
            case 9: goto L122;
            case 10: goto L121;
            case 11: goto L121;
            case 12: goto L121;
            case 13: goto L121;
            case 14: goto L121;
            case 15: goto L120;
            case 16: goto L119;
            case 17: goto L119;
            case 18: goto L119;
            case 19: goto L118;
            case 20: goto L117;
            case 21: goto L116;
            case 22: goto L115;
            case 23: goto L114;
            case 24: goto L113;
            case 25: goto L112;
            case 26: goto L111;
            case 27: goto L110;
            case 28: goto L109;
            case 29: goto L108;
            case 30: goto L108;
            case 31: goto L107;
            default: goto L106;
        };
    L106:
        return -1;
    L107:
        return 21;
    L108:
        return 20;
    L109:
        return 19;
    L110:
        return 18;
    L111:
        return 17;
    L112:
        return 16;
    L113:
        return 14;
    L114:
        return 13;
    L115:
        return 12;
    L116:
        return 11;
    L117:
        return 10;
    L118:
        return 9;
    L119:
        return 8;
    L120:
        return 7;
    L121:
        return 6;
    L122:
        return 15;
    L123:
        return 5;
    L124:
        return 4;
    L125:
        return 3;
    L126:
        return 1;
    L127:
        return 0;
    L9:
        if (r1.equals(MimeTypes.VIDEO_MATROSKA) == false) goto L128;
        r2 = 10;
        goto L105
    L128:
        r2 = -1;
        goto L105
    L12:
        if (r1.equals(MimeTypes.AUDIO_WEBM) == false) goto L129;
        r2 = 13;
        goto L105
    L129:
        r2 = -1;
        goto L105
    L15:
        if (r1.equals(MimeTypes.AUDIO_MPEG) == false) goto L130;
        r2 = 15;
        goto L105
    L130:
        r2 = -1;
        goto L105
    L18:
        if (r1.equals(MimeTypes.AUDIO_MIDI) == false) goto L131;
        r2 = 9;
        goto L105
    L131:
        r2 = -1;
        goto L105
    L21:
        if (r1.equals(MimeTypes.AUDIO_FLAC) == false) goto L132;
        r2 = 7;
        goto L105
    L132:
        r2 = -1;
        goto L105
    L24:
        if (r1.equals(MimeTypes.AUDIO_E_AC3) == false) goto L133;
        r2 = 1;
        goto L105
    L133:
        r2 = -1;
        goto L105
    L27:
        if (r1.equals(MimeTypes.AUDIO_AMR_NB) == false) goto L134;
        r2 = 5;
        goto L105
    L134:
        r2 = -1;
        goto L105
    L30:
        if (r1.equals(MimeTypes.VIDEO_MP4) == false) goto L135;
        r2 = 16;
        goto L105
    L135:
        r2 = -1;
        goto L105
    L33:
        if (r1.equals(MimeTypes.AUDIO_WAV) == false) goto L136;
        r2 = Ascii.SYN;
        goto L105
    L136:
        r2 = -1;
        goto L105
    L36:
        if (r1.equals(MimeTypes.AUDIO_OGG) == false) goto L137;
        r2 = 19;
        goto L105
    L137:
        r2 = -1;
        goto L105
    L39:
        if (r1.equals(MimeTypes.AUDIO_MP4) == false) goto L138;
        r2 = 17;
        goto L105
    L138:
        r2 = -1;
        goto L105
    L42:
        if (r1.equals(MimeTypes.AUDIO_AMR) == false) goto L139;
        r2 = 4;
        goto L105
    L139:
        r2 = -1;
        goto L105
    L45:
        if (r1.equals(MimeTypes.AUDIO_AC4) == false) goto L140;
        r2 = 3;
        goto L105
    L140:
        r2 = -1;
        goto L105
    L48:
        if (r1.equals(MimeTypes.AUDIO_AC3) == false) goto L141;
        r2 = 0;
        goto L105
    L141:
        r2 = -1;
        goto L105
    L51:
        if (r1.equals(MimeTypes.VIDEO_FLV) == false) goto L142;
        r2 = 8;
        goto L105
    L142:
        r2 = -1;
        goto L105
    L54:
        if (r1.equals(MimeTypes.APPLICATION_WEBM) == false) goto L143;
        r2 = 14;
        goto L105
    L143:
        r2 = -1;
        goto L105
    L57:
        if (r1.equals(MimeTypes.AUDIO_MATROSKA) == false) goto L144;
        r2 = 11;
        goto L105
    L144:
        r2 = -1;
        goto L105
    L60:
        if (r1.equals(MimeTypes.IMAGE_PNG) == false) goto L145;
        r2 = Ascii.SUB;
        goto L105
    L145:
        r2 = -1;
        goto L105
    L63:
        if (r1.equals(MimeTypes.IMAGE_BMP) == false) goto L146;
        r2 = Ascii.f82FS;
        goto L105
    L146:
        r2 = -1;
        goto L105
    L66:
        if (r1.equals(MimeTypes.TEXT_VTT) == false) goto L147;
        r2 = Ascii.ETB;
        goto L105
    L147:
        r2 = -1;
        goto L105
    L69:
        if (r1.equals(MimeTypes.VIDEO_AVI) == false) goto L148;
        r2 = Ascii.f80EM;
        goto L105
    L148:
        r2 = -1;
        goto L105
    L72:
        if (r1.equals(MimeTypes.APPLICATION_MP4) == false) goto L149;
        r2 = 18;
        goto L105
    L149:
        r2 = -1;
        goto L105
    L75:
        if (r1.equals(MimeTypes.IMAGE_WEBP) == false) goto L150;
        r2 = Ascii.ESC;
        goto L105
    L150:
        r2 = -1;
        goto L105
    L78:
        if (r1.equals(MimeTypes.IMAGE_JPEG) == false) goto L151;
        r2 = Ascii.CAN;
        goto L105
    L151:
        r2 = -1;
        goto L105
    L81:
        if (r1.equals(MimeTypes.IMAGE_HEIF) == false) goto L152;
        r2 = Ascii.f83GS;
        goto L105
    L152:
        r2 = -1;
        goto L105
    L84:
        if (r1.equals(MimeTypes.IMAGE_HEIC) == false) goto L153;
        r2 = Ascii.f87RS;
        goto L105
    L153:
        r2 = -1;
        goto L105
    L87:
        if (r1.equals(MimeTypes.IMAGE_AVIF) == false) goto L154;
        r2 = Ascii.f91US;
        goto L105
    L154:
        r2 = -1;
        goto L105
    L90:
        if (r1.equals(MimeTypes.AUDIO_AMR_WB) == false) goto L155;
        r2 = 6;
        goto L105
    L155:
        r2 = -1;
        goto L105
    L93:
        if (r1.equals(MimeTypes.VIDEO_WEBM) == false) goto L156;
        r2 = 12;
        goto L105
    L156:
        r2 = -1;
        goto L105
    L96:
        if (r1.equals(MimeTypes.VIDEO_MP2T) == false) goto L157;
        r2 = 21;
        goto L105
    L157:
        r2 = -1;
        goto L105
    L99:
        if (r1.equals(MimeTypes.VIDEO_PS) == false) goto L158;
        r2 = 20;
        goto L105
    L158:
        r2 = -1;
        goto L105
    L102:
        if (r1.equals(MimeTypes.AUDIO_E_AC3_JOC) == false) goto L159;
        r2 = 2;
        goto L105
    L159:
        r2 = -1;
        goto L105
    }

    public static int inferFileTypeFromResponseHeaders(Map<String, List<String>> r3) {
        List<String> r0 = r3.get("Content-Type");
        if (r0 != null) goto L5;
    L8:
        String r1 = null;
    L10:
        return inferFileTypeFromMimeType(r1);
    L5:
        if (r0.isEmpty() == true) goto L8;
        r1 = r0.get(0);
        goto L10
    }

    public static int inferFileTypeFromUri(Uri r7) {
        String r0 = r7.getLastPathSegment();
        if (r0 != null) goto L6;
        return -1;
    L6:
        if (r0.endsWith(EXTENSION_AC3) == false) goto L8;
        return 0;
    L8:
        if (r0.endsWith(EXTENSION_EC3) == false) goto L11;
        return 0;
    L11:
        if (r0.endsWith(EXTENSION_AC4) == false) goto L14;
        return 1;
    L14:
        if (r0.endsWith(EXTENSION_ADTS) == false) goto L16;
        return 2;
    L16:
        if (r0.endsWith(EXTENSION_AAC) == false) goto L19;
        return 2;
    L19:
        if (r0.endsWith(EXTENSION_AMR) == false) goto L23;
        return 3;
    L23:
        if (r0.endsWith(EXTENSION_FLAC) == false) goto L27;
        return 4;
    L27:
        if (r0.endsWith(EXTENSION_FLV) == false) goto L31;
        return 5;
    L31:
        if (r0.endsWith(EXTENSION_MID) == false) goto L33;
        return 15;
    L33:
        if (r0.endsWith(EXTENSION_MIDI) == false) goto L35;
        return 15;
    L35:
        if (r0.endsWith(EXTENSION_SMF) == false) goto L38;
        return 15;
    L38:
        if (r0.startsWith(EXTENSION_PREFIX_MK, r0.length() - (EXTENSION_PREFIX_MK.length() + 1)) == false) goto L40;
        return 6;
    L40:
        if (r0.endsWith(EXTENSION_WEBM) == false) goto L43;
        return 6;
    L43:
        if (r0.endsWith(EXTENSION_MP3) == false) goto L47;
        return 7;
    L47:
        if (r0.endsWith(".mp4") == false) goto L49;
        return 8;
    L49:
        if (r0.startsWith(EXTENSION_PREFIX_M4, r0.length() - (EXTENSION_PREFIX_M4.length() + 1)) == false) goto L51;
        return 8;
    L51:
        if (r0.startsWith(".mp4", r0.length() - (".mp4".length() + 1)) == false) goto L53;
        return 8;
    L53:
        if (r0.startsWith(EXTENSION_PREFIX_CMF, r0.length() - (EXTENSION_PREFIX_CMF.length() + 1)) == false) goto L56;
        return 8;
    L56:
        if (r0.startsWith(EXTENSION_PREFIX_OG, r0.length() - (EXTENSION_PREFIX_OG.length() + 1)) == false) goto L58;
        return 9;
    L58:
        if (r0.endsWith(EXTENSION_OPUS) == false) goto L61;
        return 9;
    L61:
        if (r0.endsWith(EXTENSION_PS) == false) goto L63;
        return 10;
    L63:
        if (r0.endsWith(EXTENSION_MPEG) == false) goto L65;
        return 10;
    L65:
        if (r0.endsWith(EXTENSION_MPG) == false) goto L67;
        return 10;
    L67:
        if (r0.endsWith(EXTENSION_M2P) == false) goto L70;
        return 10;
    L70:
        if (r0.endsWith(".ts") == false) goto L72;
        return 11;
    L72:
        if (r0.startsWith(".ts", r0.length() - (".ts".length() + 1)) == false) goto L75;
        return 11;
    L75:
        if (r0.endsWith(EXTENSION_WAV) == false) goto L77;
        return 12;
    L77:
        if (r0.endsWith(EXTENSION_WAVE) == false) goto L80;
        return 12;
    L80:
        if (r0.endsWith(EXTENSION_VTT) == false) goto L82;
        return 13;
    L82:
        if (r0.endsWith(EXTENSION_WEBVTT) == false) goto L85;
        return 13;
    L85:
        if (r0.endsWith(EXTENSION_JPG) == false) goto L87;
        return 14;
    L87:
        if (r0.endsWith(EXTENSION_JPEG) == false) goto L90;
        return 14;
    L90:
        if (r0.endsWith(EXTENSION_AVI) == false) goto L94;
        return 16;
    L94:
        if (r0.endsWith(EXTENSION_PNG) == false) goto L98;
        return 17;
    L98:
        if (r0.endsWith(EXTENSION_WEBP) == false) goto L102;
        return 18;
    L102:
        if (r0.endsWith(EXTENSION_BMP) == false) goto L104;
        return 19;
    L104:
        if (r0.endsWith(EXTENSION_DIB) == false) goto L107;
        return 19;
    L107:
        if (r0.endsWith(EXTENSION_HEIC) == false) goto L109;
        return 20;
    L109:
        if (r0.endsWith(EXTENSION_HEIF) == false) goto L112;
        return 20;
    L112:
        if (r0.endsWith(EXTENSION_AVIF) == false) goto L115;
        return 21;
    L115:
        return -1;
    }
}
