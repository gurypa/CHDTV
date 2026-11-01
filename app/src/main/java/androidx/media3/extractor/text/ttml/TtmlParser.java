package androidx.media3.extractor.text.ttml;

import android.text.Layout;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ColorParser;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.common.util.XmlPullParserUtil;
import androidx.media3.exoplayer.upstream.CmcdData;
import androidx.media3.extractor.text.CuesWithTiming;
import androidx.media3.extractor.text.LegacySubtitleUtil;
import androidx.media3.extractor.text.Subtitle;
import androidx.media3.extractor.text.SubtitleDecoderException;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.base.Ascii;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* JADX INFO: loaded from: classes.dex */
public final class TtmlParser implements SubtitleParser {
    private static final String ATTR_BEGIN = "begin";
    private static final String ATTR_DURATION = "dur";
    private static final String ATTR_END = "end";
    private static final String ATTR_IMAGE = "backgroundImage";
    private static final String ATTR_REGION = "region";
    private static final String ATTR_STYLE = "style";
    private static final Pattern CELL_RESOLUTION = null;
    private static final Pattern CLOCK_TIME = null;
    public static final int CUE_REPLACEMENT_BEHAVIOR = 1;
    private static final int DEFAULT_CELL_ROWS = 15;
    private static final FrameAndTickRate DEFAULT_FRAME_AND_TICK_RATE = null;
    private static final int DEFAULT_FRAME_RATE = 30;
    private static final Pattern FONT_SIZE = null;
    private static final Pattern OFFSET_TIME = null;
    static final Pattern PERCENTAGE_COORDINATES = null;
    private static final Pattern PIXEL_COORDINATES = null;
    static final Pattern SIGNED_PERCENTAGE = null;
    private static final String TAG = "TtmlParser";
    private static final String TTP = "http://www.w3.org/ns/ttml#parameter";
    private final XmlPullParserFactory xmlParserFactory;

    private static final class FrameAndTickRate {
        final float effectiveFrameRate;
        final int subFrameRate;
        final int tickRate;

        FrameAndTickRate(float r1, int r2, int r3) {
            this.effectiveFrameRate = r1;
            this.subFrameRate = r2;
            this.tickRate = r3;
        }
    }

    private static final class TtsExtent {
        final int height;
        final int width;

        TtsExtent(int r1, int r2) {
            this.width = r1;
            this.height = r2;
        }
    }

    static {
        CLOCK_TIME = Pattern.compile("^([0-9][0-9]+):([0-9][0-9]):([0-9][0-9])(?:(\\.[0-9]+)|:([0-9][0-9])(?:\\.([0-9]+))?)?$");
        OFFSET_TIME = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)(h|m|s|ms|f|t)$");
        FONT_SIZE = Pattern.compile("^(([0-9]*.)?[0-9]+)(px|em|%)$");
        SIGNED_PERCENTAGE = Pattern.compile("^([-+]?\\d+\\.?\\d*?)%$");
        PERCENTAGE_COORDINATES = Pattern.compile("^([-+]?\\d+\\.?\\d*?)% ([-+]?\\d+\\.?\\d*?)%$");
        PIXEL_COORDINATES = Pattern.compile("^([-+]?\\d+\\.?\\d*?)px ([-+]?\\d+\\.?\\d*?)px$");
        CELL_RESOLUTION = Pattern.compile("^(\\d+) (\\d+)$");
        DEFAULT_FRAME_AND_TICK_RATE = new FrameAndTickRate(30.0f, 1, 1);
    }

    public TtmlParser() {
        this.xmlParserFactory = XmlPullParserFactory.newInstance();     // Catch: XmlPullParserException -> L5
        this.xmlParserFactory.setNamespaceAware(true);     // Catch: XmlPullParserException -> L5
        return;
    L5:
        e = move-exception;
        throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
    }

    private static TtmlStyle createIfNull(TtmlStyle r1) {
        if (r1 != null) goto L5;
        return new TtmlStyle();
    L5:
        return r1;
    }

    private static boolean isSupportedTag(String r1) {
        if (r1.equals(TtmlNode.TAG_TT) == false) goto L5;
    L34:
        return true;
    L5:
        if (r1.equals(TtmlNode.TAG_HEAD) == true) goto L34;
        if (r1.equals(TtmlNode.TAG_BODY) == true) goto L34;
        if (r1.equals(TtmlNode.TAG_DIV) == true) goto L34;
        if (r1.equals(TtmlNode.TAG_P) == true) goto L34;
        if (r1.equals(TtmlNode.TAG_SPAN) == true) goto L34;
        if (r1.equals("br") == true) goto L34;
        if (r1.equals("style") == true) goto L34;
        if (r1.equals(TtmlNode.TAG_STYLING) == true) goto L34;
        if (r1.equals(TtmlNode.TAG_LAYOUT) == true) goto L34;
        if (r1.equals("region") == true) goto L34;
        if (r1.equals(TtmlNode.TAG_METADATA) == true) goto L34;
        if (r1.equals("image") == true) goto L34;
        if (r1.equals("data") == true) goto L34;
        if (r1.equals(TtmlNode.TAG_INFORMATION) == true) goto L34;
        return false;
    }

    private static Layout.Alignment parseAlignment(String r2) {
        String r0 = Ascii.toLowerCase(r2);
        switch(r0.hashCode()) {
            case -1364013995: goto L18;
            case 100571: goto L15;
            case 3317767: goto L12;
            case 108511772: goto L9;
            case 109757538: goto L6;
            default: goto L20;
        };
    L20:
        byte r1 = -1;
    L21:
        switch(r1) {
            case 0: goto L29;
            case 1: goto L29;
            case 2: goto L27;
            case 3: goto L27;
            case 4: goto L25;
            default: goto L22;
        };
    L22:
        return null;
    L25:
        return Layout.Alignment.ALIGN_CENTER;
    L27:
        return Layout.Alignment.ALIGN_OPPOSITE;
    L29:
        return Layout.Alignment.ALIGN_NORMAL;
    L6:
        if (r0.equals("start") == false) goto L20;
        r1 = 1;
        goto L21
    L9:
        if (r0.equals(TtmlNode.RIGHT) == false) goto L20;
        r1 = 2;
        goto L21
    L12:
        if (r0.equals(TtmlNode.LEFT) == false) goto L20;
        r1 = 0;
        goto L21
    L15:
        if (r0.equals("end") == false) goto L20;
        r1 = 3;
        goto L21
    L18:
        if (r0.equals(TtmlNode.CENTER) == false) goto L20;
        r1 = 4;
        goto L21
    }

    private static int parseCellRows(XmlPullParser r9, int r10) {
        String r0 = r9.getAttributeValue(TTP, "cellResolution");
        if (r0 != null) goto L5;
        return r10;
    L5:
        Matcher r1 = CELL_RESOLUTION.matcher(r0);
        if (r1.matches() == true) goto L9;
        Log.m48w(TAG, "Ignoring malformed cell resolution: " + r0);
        return r10;
    L9:
        boolean r2 = true;
        int r5 = Integer.parseInt((String) Assertions.checkNotNull(r1.group(1)));     // Catch: NumberFormatException -> L17
        int r6 = Integer.parseInt((String) Assertions.checkNotNull(r1.group(2)));     // Catch: NumberFormatException -> L17
        if (r5 == 0) goto L14;
        if (r6 == 0) goto L14;
    L15:
        Assertions.checkArgument(r2, "Invalid cell resolution " + r5 + " " + r6);     // Catch: NumberFormatException -> L17
        return r6;
    L14:
        r2 = false;
    L18:
        Log.m48w(TAG, "Ignoring malformed cell resolution: " + r0);
        return r10;
    }

    private static void parseFontSize(String r8, TtmlStyle r9) throws SubtitleDecoderException {
        String[] r0 = Util.split(r8, "\\s+");
        if (r0.length != 1) goto L6;
        Matcher r1 = FONT_SIZE.matcher(r8);
    L9:
        if (r1.matches() == false) goto L32;
        String r6 = (String) Assertions.checkNotNull(r1.group(3));
        switch(r6.hashCode()) {
            case 37: goto L20;
            case 3240: goto L17;
            case 3592: goto L14;
            default: goto L22;
        };
    L22:
        byte r7 = -1;
    L23:
        switch(r7) {
            case 0: goto L28;
            case 1: goto L27;
            case 2: goto L26;
            default: goto L25;
        };
    L26:
        r9.setFontSizeUnit(3);
    L29:
        r9.setFontSize(Float.parseFloat((String) Assertions.checkNotNull(r1.group(1))));
        return;
    L27:
        r9.setFontSizeUnit(2);
        goto L29
    L28:
        r9.setFontSizeUnit(1);
        goto L29
    L25:
        throw new SubtitleDecoderException("Invalid unit for fontSize: '" + r6 + "'.");
    L14:
        if (r6.equals("px") == false) goto L22;
        r7 = 0;
        goto L23
    L17:
        if (r6.equals("em") == false) goto L22;
        r7 = 1;
        goto L23
    L20:
        if (r6.equals("%") == false) goto L22;
        r7 = 2;
        goto L23
    L32:
        throw new SubtitleDecoderException("Invalid expression for fontSize: '" + r8 + "'.");
    L6:
        if (r0.length != 2) goto L34;
        r1 = FONT_SIZE.matcher(r0[1]);
        Log.m48w(TAG, "Multiple values in fontSize attribute. Picking the second value for vertical font size and ignoring the first.");
        goto L9
    L34:
        throw new SubtitleDecoderException("Invalid number of entries for fontSize: " + r0.length + ".");
    }

    private static FrameAndTickRate parseFrameAndTickRates(XmlPullParser r10) {
        int r0 = 30;
        String r1 = r10.getAttributeValue(TTP, "frameRate");
        if (r1 == null) goto L5;
        r0 = Integer.parseInt(r1);
    L5:
        float r3 = 1.0f;
        String r4 = r10.getAttributeValue(TTP, "frameRateMultiplier");
        if (r4 == null) goto L12;
        String[] r5 = Util.split(r4, " ");
        if (r5.length != 2) goto L10;
        boolean r6 = true;
    L11:
        Assertions.checkArgument(r6, "frameRateMultiplier doesn't have 2 parts");
        r3 = Integer.parseInt(r5[0]) / Integer.parseInt(r5[1]);
        goto L12
    L10:
        r6 = false;
    L12:
        int r7 = DEFAULT_FRAME_AND_TICK_RATE.subFrameRate;
        String r8 = r10.getAttributeValue(TTP, "subFrameRate");
        if (r8 == null) goto L15;
        r7 = Integer.parseInt(r8);
    L15:
        int r9 = DEFAULT_FRAME_AND_TICK_RATE.tickRate;
        String r2 = r10.getAttributeValue(TTP, "tickRate");
        if (r2 == null) goto L19;
        r9 = Integer.parseInt(r2);
    L19:
        return new FrameAndTickRate(r0 * r3, r7, r9);
    }

    private static Map<String, TtmlStyle> parseHeader(XmlPullParser r7, Map<String, TtmlStyle> r8, int r9, TtsExtent r10, Map<String, TtmlRegion> r11, Map<String, String> r12) throws IOException, XmlPullParserException {
    L2:
        r7.next();
        if (XmlPullParserUtil.isStartTag(r7, "style") == false) goto L14;
        String r0 = XmlPullParserUtil.getAttributeValue(r7, "style");
        TtmlStyle r1 = parseStyleAttributes(r7, new TtmlStyle());
        if (r0 == null) goto L9;
        String[] r2 = parseStyleIds(r0);
        int r3 = r2.length;
        int r4 = 0;
    L7:
        if (r4 >= r3) goto L9;
        r1.chain(r8.get(r2[r4]));
        r4 = r4 + 1;
    L9:
        String r5 = r1.getId();
        if (r5 == null) goto L23;
        r8.put(r5, r1);
    L23:
        if (XmlPullParserUtil.isEndTag(r7, TtmlNode.TAG_HEAD) == false) goto L2;
        return r8;
    L14:
        if (XmlPullParserUtil.isStartTag(r7, "region") == false) goto L19;
        TtmlRegion r6 = parseRegionAttributes(r7, r9, r10);
        if (r6 == null) goto L23;
        r11.put(r6.f64id, r6);
        goto L23
    L19:
        if (XmlPullParserUtil.isStartTag(r7, TtmlNode.TAG_METADATA) == false) goto L23;
        parseMetadata(r7, r12);
        goto L23
    }

    private static void parseMetadata(XmlPullParser r2, Map<String, String> r3) throws IOException, XmlPullParserException {
    L2:
        r2.next();
        if (XmlPullParserUtil.isStartTag(r2, "image") == false) goto L8;
        String r0 = XmlPullParserUtil.getAttributeValue(r2, "id");
        if (r0 == null) goto L8;
        r3.put(r0, r2.nextText());
    L8:
        if (XmlPullParserUtil.isEndTag(r2, TtmlNode.TAG_METADATA) == false) goto L2;
    }

    private static TtmlNode parseNode(XmlPullParser r22, TtmlNode r23, Map<String, TtmlRegion> r24, FrameAndTickRate r25) throws SubtitleDecoderException {
        XmlPullParser r0 = r22;
        long r3 = C0180C.TIME_UNSET;
        long r5 = C0180C.TIME_UNSET;
        int r12 = r0.getAttributeCount();
        TtmlStyle r13 = parseStyleAttributes(r0, null);
        int r1 = 0;
        long r14 = -9223372036854775807L;
        String r8 = "";
        String[] r7 = null;
        String r9 = null;
    L3:
        if (r1 >= r12) goto L42;
        String r2 = r0.getAttributeName(r1);
        long r16 = r3;
        String r4 = r0.getAttributeValue(r1);
        switch(r2.hashCode()) {
            case -934795532: goto L23;
            case 99841: goto L20;
            case 100571: goto L17;
            case 93616297: goto L14;
            case 109780401: goto L11;
            case 1292595405: goto L8;
            default: goto L25;
        };
    L25:
        byte r6 = -1;
    L26:
        switch(r6) {
            case 0: goto L39;
            case 1: goto L38;
            case 2: goto L37;
            case 3: goto L34;
            case 4: goto L32;
            case 5: goto L29;
            default: goto L41;
        };
    L34:
        String[] r10 = parseStyleIds(r4);
        if (r10.length <= 0) goto L41;
        r7 = r10;
        goto L41
    L37:
        r14 = parseTimeExpression(r4, r25);
        goto L41
    L38:
        r5 = parseTimeExpression(r4, r25);
        goto L41
    L39:
        r16 = parseTimeExpression(r4, r25);
        goto L41
    L29:
        if (r4.startsWith("#") == false) goto L41;
        r9 = r4.substring(1);
        goto L41
    L32:
        if (r24.containsKey(r4) == false) goto L41;
        r8 = r4;
    L41:
        r3 = r16;
        r1 = r1 + 1;
        r0 = r22;
        goto L3
    L8:
        if (r2.equals(ATTR_IMAGE) == false) goto L25;
        r6 = 5;
        goto L26
    L11:
        if (r2.equals("style") == false) goto L25;
        r6 = 3;
        goto L26
    L14:
        if (r2.equals(ATTR_BEGIN) == false) goto L25;
        r6 = 0;
        goto L26
    L17:
        if (r2.equals("end") == false) goto L25;
        r6 = 1;
        goto L26
    L20:
        if (r2.equals(ATTR_DURATION) == false) goto L25;
        r6 = 2;
        goto L26
    L23:
        if (r2.equals("region") == false) goto L25;
        r6 = 4;
        goto L26
    L42:
        long r17 = r3;
        if (r23 != null) goto L45;
    L54:
        long r11 = r17;
    L56:
        if (r5 == r0) goto L58;
    L64:
        long r15 = r5;
    L66:
        return TtmlNode.buildNode(r22.getName(), r11, r15, r13, r7, r8, r9, r23);
    L58:
        if (r14 == r0) goto L60;
        r15 = r11 + r14;
        goto L66
    L60:
        if (r23 == null) goto L64;
        if (r23.endTimeUs == (-9223372036854775807(0x8000000000000001, double:-4.9E-324))) goto L64;
        r15 = r23.endTimeUs;
        goto L66
    L45:
        if (r23.startTimeUs == C0180C.TIME_UNSET) goto L54;
        if (r17 == C0180C.TIME_UNSET) goto L49;
        long r18 = r17 + r23.startTimeUs;
    L51:
        if (r5 == C0180C.TIME_UNSET) goto L53;
        r5 = r5 + r23.startTimeUs;
        r11 = r18;
        goto L56
    L53:
        r11 = r18;
        goto L56
    L49:
        r18 = r17;
        goto L51
    }

    private static TtmlRegion parseRegionAttributes(XmlPullParser r20, int r21, TtsExtent r22) {
        String r4 = XmlPullParserUtil.getAttributeValue(r20, "id");
        if (r4 != null) goto L5;
        return null;
    L5:
        String r14 = XmlPullParserUtil.getAttributeValue(r20, "origin");
        if (r14 == null) goto L92;
        Matcher r6 = PERCENTAGE_COORDINATES.matcher(r14);
        Matcher r7 = PIXEL_COORDINATES.matcher(r14);
        if (r6.matches() == false) goto L15;
        float r0 = Float.parseFloat((String) Assertions.checkNotNull(r6.group(1))) / 100.0f;     // Catch: NumberFormatException -> L11
        float r8 = Float.parseFloat((String) Assertions.checkNotNull(r6.group(2))) / 100.0f;
        TtmlRegion r16 = null;
        float r3 = r0;
    L23:
        String r15 = XmlPullParserUtil.getAttributeValue(r20, TtmlNode.ATTR_TTS_EXTENT);
        if (r15 == null) goto L82;
        Matcher r9 = PERCENTAGE_COORDINATES.matcher(r15);
        Matcher r10 = PIXEL_COORDINATES.matcher(r15);
        if (r9.matches() == false) goto L33;
        float r11 = Float.parseFloat((String) Assertions.checkNotNull(r9.group(1))) / 100.0f;     // Catch: NumberFormatException -> L29
        float r12 = Float.parseFloat((String) Assertions.checkNotNull(r9.group(2))) / 100.0f;
    L39:
        String r17 = XmlPullParserUtil.getAttributeValue(r20, TtmlNode.ATTR_TTS_DISPLAY_ALIGN);
        if (r17 == null) goto L55;
        String r13 = Ascii.toLowerCase(r17);
        switch(r13.hashCode()) {
            case -1364013995: goto L48;
            case 92734940: goto L45;
            default: goto L50;
        };
    L50:
        byte r18 = -1;
    L51:
        switch(r18) {
            case 0: goto L54;
            case 1: goto L53;
            default: goto L55;
        };
    L53:
        float r19 = r8 + r12;
        int r23 = 2;
        float r24 = r19;
    L56:
        float r25 = 1.0f / r21;
        int r110 = Integer.MIN_VALUE;
        String r1 = XmlPullParserUtil.getAttributeValue(r20, TtmlNode.ATTR_TTS_WRITING_MODE);
        if (r1 == null) goto L76;
        String r5 = Ascii.toLowerCase(r1);
        switch(r5.hashCode()) {
            case 3694: goto L68;
            case 3553396: goto L65;
            case 3553576: goto L62;
            default: goto L70;
        };
    L70:
        byte r111 = -1;
    L71:
        switch(r111) {
            case 0: goto L74;
            case 1: goto L74;
            case 2: goto L73;
            default: goto L76;
        };
    L73:
        r110 = 1;
        goto L76
    L74:
        r110 = 2;
        goto L76
    L62:
        if (r5.equals(TtmlNode.VERTICAL_RL) == false) goto L70;
        r111 = 2;
        goto L71
    L65:
        if (r5.equals(TtmlNode.VERTICAL_LR) == false) goto L70;
        r111 = 1;
        goto L71
    L68:
        if (r5.equals("tb") == false) goto L70;
        r111 = 0;
    L76:
        return new TtmlRegion(r4, r3, r24, 0, r23, r11, r12, 1, r25, r110);
    L54:
        float r26 = r8 + (r12 / 2.0f);
        r23 = 1;
        r24 = r26;
        goto L56
    L45:
        if (r13.equals(TtmlNode.ANNOTATION_POSITION_AFTER) == false) goto L104;
        r18 = 1;
        goto L51
    L104:
        r18 = -1;
        goto L51
    L48:
        if (r13.equals(TtmlNode.CENTER) == false) goto L105;
        r18 = 0;
        goto L51
    L105:
        r18 = -1;
    L55:
        float r112 = r8;
        r23 = 0;
        r24 = r112;
    L30:
        Log.m48w(TAG, "Ignoring region with malformed extent: " + r14);
        return r16;
    L33:
        if (r10.matches() == false) goto L80;
        if (r22 != null) goto L96;
        Log.m48w(TAG, "Ignoring region with missing tts:extent: " + r14);
        return r16;
    L96:
        int r2 = Integer.parseInt((String) Assertions.checkNotNull(r10.group(1)));     // Catch: NumberFormatException -> L77
        int r27 = Integer.parseInt((String) Assertions.checkNotNull(r10.group(2)));     // Catch: NumberFormatException -> L77
        float r113 = r27;
        r11 = r2 / r22.width;     // Catch: NumberFormatException -> L77
        r12 = r113 / r22.height;
    L78:
        Log.m48w(TAG, "Ignoring region with malformed extent: " + r14);
        return r16;
    L80:
        Log.m48w(TAG, "Ignoring region with unsupported extent: " + r14);
        return r16;
    L82:
        Log.m48w(TAG, "Ignoring region without an extent");
        return r16;
    L12:
        Log.m48w(TAG, "Ignoring region with malformed origin: " + r14);
        return null;
    L15:
        if (r7.matches() == false) goto L90;
        if (r22 != null) goto L102;
        Log.m48w(TAG, "Ignoring region with missing tts:extent: " + r14);
        return null;
    L102:
        int r28 = Integer.parseInt((String) Assertions.checkNotNull(r7.group(1)));     // Catch: NumberFormatException -> L86
        int r114 = Integer.parseInt((String) Assertions.checkNotNull(r7.group(2)));     // Catch: NumberFormatException -> L86
        r16 = null;
        r3 = r28 / r22.width;     // Catch: NumberFormatException -> L84
        r8 = r114 / r22.height;
    L88:
        Log.m48w(TAG, "Ignoring region with malformed origin: " + r14);
        return r16;
    L87:
        r16 = null;
        goto L88
    L90:
        Log.m48w(TAG, "Ignoring region with unsupported origin: " + r14);
        return null;
    L92:
        Log.m48w(TAG, "Ignoring region without an origin");
        return null;
    }

    private static float parseShear(String r6) {
        Matcher r0 = SIGNED_PERCENTAGE.matcher(r6);
        if (r0.matches() == true) goto L12;
        Log.m48w(TAG, "Invalid value for shear: " + r6);
        return Float.MAX_VALUE;
    L12:
        return Math.min(100.0f, Math.max(-100.0f, Float.parseFloat((String) Assertions.checkNotNull(r0.group(1)))));
    L9:
        e = move-exception;
        Log.m49w(TAG, "Failed to parse shear: " + r6, e);
        return Float.MAX_VALUE;
    }

    private static TtmlStyle parseStyleAttributes(XmlPullParser r12, TtmlStyle r13) {
        int r0 = r12.getAttributeCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L161;
        String r2 = r12.getAttributeValue(r1);
        String r3 = r12.getAttributeName(r1);
        byte r5 = 5;
        byte r7 = 3;
        byte r9 = -1;
        switch(r3.hashCode()) {
            case -1550943582: goto L50;
            case -1224696685: goto L47;
            case -1065511464: goto L44;
            case -879295043: goto L41;
            case -734428249: goto L38;
            case 3355: goto L35;
            case 3511770: goto L32;
            case 94842723: goto L29;
            case 109403361: goto L26;
            case 110138194: goto L23;
            case 365601008: goto L20;
            case 921125321: goto L17;
            case 1115953443: goto L14;
            case 1287124693: goto L11;
            case 1754920356: goto L8;
            default: goto L52;
        };
    L52:
        byte r4 = -1;
    L54:
        switch(r4) {
            case 0: goto L158;
            case 1: goto L152;
            case 2: goto L147;
            case 3: goto L146;
            case 4: goto L164;
            case 5: goto L141;
            case 6: goto L140;
            case 7: goto L139;
            case 8: goto L138;
            case 9: goto L124;
            case 10: goto L95;
            case 11: goto L81;
            case 12: goto L58;
            case 13: goto L57;
            case 14: goto L56;
            default: goto L160;
        };
    L56:
        r13 = createIfNull(r13).setShearPercentage(parseShear(r2));
        goto L160
    L57:
        r13 = createIfNull(r13).setTextEmphasis(TextEmphasis.parse(r2));
        goto L160
    L58:
        String r6 = Ascii.toLowerCase(r2);
        switch(r6.hashCode()) {
            case -1461280213: goto L71;
            case -1026963764: goto L68;
            case 913457136: goto L65;
            case 1679736913: goto L62;
            default: goto L73;
        };
    L73:
        r7 = -1;
    L74:
        switch(r7) {
            case 0: goto L79;
            case 1: goto L78;
            case 2: goto L77;
            case 3: goto L76;
            default: goto L160;
        };
    L76:
        r13 = createIfNull(r13).setUnderline(false);
        goto L160
    L77:
        r13 = createIfNull(r13).setUnderline(true);
        goto L160
    L78:
        r13 = createIfNull(r13).setLinethrough(false);
        goto L160
    L79:
        r13 = createIfNull(r13).setLinethrough(true);
        goto L160
    L62:
        if (r6.equals(TtmlNode.LINETHROUGH) == false) goto L201;
        r7 = 0;
        goto L74
    L201:
        r7 = -1;
        goto L74
    L65:
        if (r6.equals(TtmlNode.NO_LINETHROUGH) == false) goto L202;
        r7 = 1;
        goto L74
    L202:
        r7 = -1;
        goto L74
    L68:
        if (r6.equals(TtmlNode.UNDERLINE) == false) goto L203;
        r7 = 2;
        goto L74
    L203:
        r7 = -1;
        goto L74
    L71:
        if (r6.equals(TtmlNode.NO_UNDERLINE) == true) goto L74;
        r7 = -1;
        goto L74
    L81:
        String r8 = Ascii.toLowerCase(r2);
        switch(r8.hashCode()) {
            case -1392885889: goto L88;
            case 92734940: goto L85;
            default: goto L90;
        };
    L90:
        switch(r9) {
            case 0: goto L93;
            case 1: goto L92;
            default: goto L160;
        };
    L92:
        r13 = createIfNull(r13).setRubyPosition(2);
        goto L160
    L93:
        r13 = createIfNull(r13).setRubyPosition(1);
        goto L160
    L85:
        if (r8.equals(TtmlNode.ANNOTATION_POSITION_AFTER) == false) goto L90;
        r9 = 1;
        goto L90
    L88:
        if (r8.equals(TtmlNode.ANNOTATION_POSITION_BEFORE) == false) goto L90;
        r9 = 0;
        goto L90
    L95:
        String r10 = Ascii.toLowerCase(r2);
        switch(r10.hashCode()) {
            case -618561360: goto L114;
            case -410956671: goto L111;
            case -250518009: goto L108;
            case -136074796: goto L105;
            case 3016401: goto L102;
            case 3556653: goto L99;
            default: goto L116;
        };
    L116:
        r5 = -1;
    L117:
        switch(r5) {
            case 0: goto L122;
            case 1: goto L121;
            case 2: goto L121;
            case 3: goto L120;
            case 4: goto L120;
            case 5: goto L119;
            default: goto L160;
        };
    L119:
        r13 = createIfNull(r13).setRubyType(4);
        goto L160
    L120:
        r13 = createIfNull(r13).setRubyType(3);
        goto L160
    L121:
        r13 = createIfNull(r13).setRubyType(2);
        goto L160
    L122:
        r13 = createIfNull(r13).setRubyType(1);
        goto L160
    L99:
        if (r10.equals("text") == false) goto L205;
        r5 = 3;
        goto L117
    L205:
        r5 = -1;
        goto L117
    L102:
        if (r10.equals(TtmlNode.RUBY_BASE) == false) goto L206;
        r5 = 1;
        goto L117
    L206:
        r5 = -1;
        goto L117
    L105:
        if (r10.equals(TtmlNode.RUBY_TEXT_CONTAINER) == false) goto L207;
        r5 = 4;
        goto L117
    L207:
        r5 = -1;
        goto L117
    L108:
        if (r10.equals(TtmlNode.RUBY_DELIMITER) == true) goto L117;
        r5 = -1;
        goto L117
    L111:
        if (r10.equals(TtmlNode.RUBY_CONTAINER) == false) goto L209;
        r5 = 0;
        goto L117
    L209:
        r5 = -1;
        goto L117
    L114:
        if (r10.equals(TtmlNode.RUBY_BASE_CONTAINER) == false) goto L210;
        r5 = 2;
        goto L117
    L210:
        r5 = -1;
        goto L117
    L124:
        String r11 = Ascii.toLowerCase(r2);
        switch(r11.hashCode()) {
            case 96673: goto L131;
            case 3387192: goto L128;
            default: goto L133;
        };
    L133:
        switch(r9) {
            case 0: goto L136;
            case 1: goto L135;
            default: goto L160;
        };
    L135:
        r13 = createIfNull(r13).setTextCombine(true);
        goto L160
    L136:
        r13 = createIfNull(r13).setTextCombine(false);
        goto L160
    L128:
        if (r11.equals("none") == false) goto L133;
        r9 = 0;
        goto L133
    L131:
        if (r11.equals(TtmlNode.COMBINE_ALL) == false) goto L133;
        r9 = 1;
        goto L133
    L138:
        r13 = createIfNull(r13).setMultiRowAlign(parseAlignment(r2));
        goto L160
    L139:
        r13 = createIfNull(r13).setTextAlign(parseAlignment(r2));
        goto L160
    L140:
        r13 = createIfNull(r13).setItalic(TtmlNode.ITALIC.equalsIgnoreCase(r2));
        goto L160
    L141:
        r13 = createIfNull(r13).setBold(TtmlNode.BOLD.equalsIgnoreCase(r2));
        goto L160
    L146:
        r13 = createIfNull(r13).setFontFamily(r2);
        goto L160
    L147:
        r13 = createIfNull(r13);
        r13.setFontColor(ColorParser.parseTtmlColor(r2));     // Catch: IllegalArgumentException -> L150
    L151:
        Log.m48w(TAG, "Failed parsing color value: " + r2);
        goto L160
    L152:
        r13 = createIfNull(r13);
        r13.setBackgroundColor(ColorParser.parseTtmlColor(r2));     // Catch: IllegalArgumentException -> L155
    L156:
        Log.m48w(TAG, "Failed parsing background value: " + r2);
        goto L160
    L164:
        r13 = createIfNull(r13);     // Catch: SubtitleDecoderException -> L144
        parseFontSize(r2, r13);     // Catch: SubtitleDecoderException -> L144
    L145:
        Log.m48w(TAG, "Failed parsing fontSize value: " + r2);
        goto L160
    L158:
        if ("style".equals(r12.getName()) == false) goto L160;
        r13 = createIfNull(r13).setId(r2);
    L160:
        r1 = r1 + 1;
        goto L3
    L8:
        if (r3.equals(TtmlNode.ATTR_EBUTTS_MULTI_ROW_ALIGN) == false) goto L211;
        r4 = 8;
        goto L54
    L211:
        r4 = -1;
        goto L54
    L11:
        if (r3.equals(TtmlNode.ATTR_TTS_BACKGROUND_COLOR) == false) goto L212;
        r4 = 1;
        goto L54
    L212:
        r4 = -1;
        goto L54
    L14:
        if (r3.equals(TtmlNode.ATTR_TTS_RUBY_POSITION) == false) goto L213;
        r4 = Ascii.f92VT;
        goto L54
    L213:
        r4 = -1;
        goto L54
    L17:
        if (r3.equals(TtmlNode.ATTR_TTS_TEXT_EMPHASIS) == false) goto L214;
        r4 = Ascii.f79CR;
        goto L54
    L214:
        r4 = -1;
        goto L54
    L20:
        if (r3.equals(TtmlNode.ATTR_TTS_FONT_SIZE) == false) goto L215;
        r4 = 4;
        goto L54
    L215:
        r4 = -1;
        goto L54
    L23:
        if (r3.equals(TtmlNode.ATTR_TTS_TEXT_COMBINE) == false) goto L216;
        r4 = 9;
        goto L54
    L216:
        r4 = -1;
        goto L54
    L26:
        if (r3.equals(TtmlNode.ATTR_TTS_SHEAR) == false) goto L217;
        r4 = Ascii.f89SO;
        goto L54
    L217:
        r4 = -1;
        goto L54
    L29:
        if (r3.equals(TtmlNode.ATTR_TTS_COLOR) == false) goto L218;
        r4 = 2;
        goto L54
    L218:
        r4 = -1;
        goto L54
    L32:
        if (r3.equals(TtmlNode.ATTR_TTS_RUBY) == false) goto L219;
        r4 = 10;
        goto L54
    L219:
        r4 = -1;
        goto L54
    L35:
        if (r3.equals("id") == false) goto L220;
        r4 = 0;
        goto L54
    L220:
        r4 = -1;
        goto L54
    L38:
        if (r3.equals(TtmlNode.ATTR_TTS_FONT_WEIGHT) == false) goto L221;
        r4 = 5;
        goto L54
    L221:
        r4 = -1;
        goto L54
    L41:
        if (r3.equals(TtmlNode.ATTR_TTS_TEXT_DECORATION) == false) goto L222;
        r4 = Ascii.f81FF;
        goto L54
    L222:
        r4 = -1;
        goto L54
    L44:
        if (r3.equals(TtmlNode.ATTR_TTS_TEXT_ALIGN) == false) goto L223;
        r4 = 7;
        goto L54
    L223:
        r4 = -1;
        goto L54
    L47:
        if (r3.equals(TtmlNode.ATTR_TTS_FONT_FAMILY) == false) goto L224;
        r4 = 3;
        goto L54
    L224:
        r4 = -1;
        goto L54
    L50:
        if (r3.equals(TtmlNode.ATTR_TTS_FONT_STYLE) == false) goto L225;
        r4 = 6;
        goto L54
    L225:
        r4 = -1;
        goto L54
    L161:
        return r13;
    }

    private static String[] parseStyleIds(String r1) {
        String r2 = r1.trim();
        if (r2.isEmpty() == false) goto L6;
        return new String[0];
    L6:
        return Util.split(r2, "\\s+");
    }

    private static long parseTimeExpression(String r18, FrameAndTickRate r19) throws SubtitleDecoderException {
        Matcher r2 = CLOCK_TIME.matcher(r18);
        byte r6 = 5;
        if (r2.matches() == false) goto L18;
        double r10 = ((Long.parseLong((String) Assertions.checkNotNull(r2.group(1))) * 3600) + (Long.parseLong((String) Assertions.checkNotNull(r2.group(2))) * 60)) + Long.parseLong((String) Assertions.checkNotNull(r2.group(3)));
        String r7 = r2.group(4);
        double r12 = 0.0d;
        if (r7 == null) goto L7;
        double r14 = Double.parseDouble(r7);
    L8:
        double r11 = r10 + r14;
        String r8 = r2.group(5);
        if (r8 == null) goto L11;
        double r15 = Long.parseLong(r8) / r19.effectiveFrameRate;
    L12:
        double r13 = r11 + r15;
        String r16 = r2.group(6);
        if (r16 == null) goto L15;
        double r17 = 1000000.0d;
        r12 = (Long.parseLong(r16) / ((double) r19.subFrameRate)) / ((double) r19.effectiveFrameRate);
    L17:
        return (long) ((r13 + r12) * r17);
    L15:
        r17 = 1000000.0d;
        goto L17
    L11:
        r15 = 0.0d;
        goto L12
    L7:
        r14 = 0.0d;
        goto L8
    L18:
        Matcher r3 = OFFSET_TIME.matcher(r18);
        if (r3.matches() == false) goto L53;
        double r4 = Double.parseDouble((String) Assertions.checkNotNull(r3.group(1)));
        String r110 = (String) Assertions.checkNotNull(r3.group(2));
        switch(r110.hashCode()) {
            case 102: goto L39;
            case 104: goto L36;
            case 109: goto L33;
            case 115: goto L30;
            case 116: goto L27;
            case 3494: goto L24;
            default: goto L41;
        };
    L41:
        r6 = -1;
    L42:
        switch(r6) {
            case 0: goto L49;
            case 1: goto L48;
            case 2: goto L51;
            case 3: goto L46;
            case 4: goto L45;
            case 5: goto L44;
            default: goto L51;
        };
    L44:
        r4 = r4 / ((double) r19.tickRate);
        goto L51
    L45:
        r4 = r4 / ((double) r19.effectiveFrameRate);
        goto L51
    L46:
        r4 = r4 / 1000.0d;
        goto L51
    L48:
        r4 = r4 * 60.0d;
        goto L51
    L49:
        r4 = r4 * 3600.0d;
    L51:
        return (long) (r4 * 1000000.0d);
    L24:
        if (r110.equals("ms") == false) goto L41;
        r6 = 3;
        goto L42
    L27:
        if (r110.equals("t") == false) goto L41;
    L30:
        if (r110.equals(CmcdData.Factory.STREAMING_FORMAT_SS) == false) goto L41;
        r6 = 2;
        goto L42
    L33:
        if (r110.equals("m") == false) goto L41;
        r6 = 1;
        goto L42
    L36:
        if (r110.equals(CmcdData.Factory.STREAMING_FORMAT_HLS) == false) goto L41;
        r6 = 0;
        goto L42
    L39:
        if (r110.equals("f") == false) goto L41;
        r6 = 4;
        goto L42
    L53:
        throw new SubtitleDecoderException("Malformed time expression: " + r18);
    }

    private static TtsExtent parseTtsExtent(XmlPullParser r7) {
        String r0 = XmlPullParserUtil.getAttributeValue(r7, TtmlNode.ATTR_TTS_EXTENT);
        if (r0 != null) goto L5;
        return null;
    L5:
        Matcher r2 = PIXEL_COORDINATES.matcher(r0);
        if (r2.matches() == true) goto L15;
        Log.m48w(TAG, "Ignoring non-pixel tts extent: " + r0);
        return null;
    L15:
        return new TtsExtent(Integer.parseInt((String) Assertions.checkNotNull(r2.group(1))), Integer.parseInt((String) Assertions.checkNotNull(r2.group(2))));
    L13:
        Log.m48w(TAG, "Ignoring malformed tts extent: " + r0);
        return null;
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public int getCueReplacementBehavior() {
        return 1;
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public void parse(byte[] r2, int r3, int r4, SubtitleParser.OutputOptions r5, Consumer<CuesWithTiming> r6) {
        LegacySubtitleUtil.toCuesWithTiming(parseToLegacySubtitle(r2, r3, r4), r5, r6);
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public /* synthetic */ void parse(byte[] r1, SubtitleParser.OutputOptions r2, Consumer r3) {
        SubtitleParser.CC.$default$parse(this, r1, r2, r3);
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public Subtitle parseToLegacySubtitle(byte[] r22, int r23, int r24) {
        XmlPullParser r3 = this.xmlParserFactory.newPullParser();     // Catch: IOException -> L60 XmlPullParserException -> L64
        HashMap r4 = new HashMap();     // Catch: IOException -> L60 XmlPullParserException -> L64
        HashMap r7 = new HashMap();     // Catch: IOException -> L60 XmlPullParserException -> L64
        HashMap r8 = new HashMap();     // Catch: IOException -> L60 XmlPullParserException -> L64
        r7.put("", new TtmlRegion(""));     // Catch: IOException -> L60 XmlPullParserException -> L64
    L70:
        r3.setInput(new ByteArrayInputStream(r22, r23, r24), null);     // Catch: IOException -> L56 XmlPullParserException -> L58
        ArrayDeque r5 = new ArrayDeque();     // Catch: IOException -> L56 XmlPullParserException -> L58
        int r6 = r3.getEventType();     // Catch: IOException -> L56 XmlPullParserException -> L58
        TtmlSubtitle r13 = null;
        FrameAndTickRate r0 = DEFAULT_FRAME_AND_TICK_RATE;     // Catch: IOException -> L56 XmlPullParserException -> L58
        int r14 = 0;
        int r9 = 15;
        int r15 = r6;
        TtsExtent r10 = null;
    L6:
        FrameAndTickRate r16 = r0;
        if (r15 == 1) goto L54;
        TtmlNode r1 = (TtmlNode) r5.peek();     // Catch: IOException -> L56 XmlPullParserException -> L58
        if (r14 != 0) goto L46;
        String r18 = r3.getName();     // Catch: IOException -> L56 XmlPullParserException -> L58
        if (r15 == 2) goto L15;
        HashMap r2 = r8;
        HashMap r11 = r7;
        HashMap r12 = r4;
        XmlPullParser r17 = r3;
        int r19 = r9;
        if (r15 != 4) goto L39;
        ((TtmlNode) Assertions.checkNotNull(r1)).addChild(TtmlNode.buildTextNode(r17.getText()));     // Catch: IOException -> L56 XmlPullParserException -> L58
    L44:
        r0 = r16;
        int r110 = r19;
    L45:
        int r20 = r110;
    L53:
        r17.next();     // Catch: IOException -> L56 XmlPullParserException -> L58
        r15 = r17.getEventType();     // Catch: IOException -> L56 XmlPullParserException -> L58
        r9 = r20;
        r3 = r17;
        r4 = r12;
        r7 = r11;
        r8 = r2;
        goto L6
    L39:
        if (r15 != 3) goto L44;
        if (r17.getName().equals(TtmlNode.TAG_TT) == false) goto L43;
        r13 = new TtmlSubtitle((TtmlNode) Assertions.checkNotNull((TtmlNode) r5.peek()), r12, r11, r2);     // Catch: IOException -> L56 XmlPullParserException -> L58
    L43:
        r5.pop();     // Catch: IOException -> L56 XmlPullParserException -> L58
        r0 = r16;
        r110 = r19;
        goto L45
    L15:
        if (TtmlNode.TAG_TT.equals(r18) == false) goto L17;
        FrameAndTickRate r21 = parseFrameAndTickRates(r3);     // Catch: IOException -> L56 XmlPullParserException -> L58
        r9 = parseCellRows(r3, 15);     // Catch: IOException -> L56 XmlPullParserException -> L58
        r10 = parseTtsExtent(r3);     // Catch: IOException -> L56 XmlPullParserException -> L58
        FrameAndTickRate r25 = r21;
    L18:
        boolean r26 = isSupportedTag(r18);     // Catch: IOException -> L56 XmlPullParserException -> L58
        XmlPullParser r111 = r3;
        if (r26 == true) goto L22;
        HashMap r112 = r4;
        Log.m46i(TAG, "Ignoring unsupported tag: " + r111.getName());     // Catch: IOException -> L56 XmlPullParserException -> L58
        r14 = r14 + 1;     // Catch: IOException -> L56 XmlPullParserException -> L58
        r0 = r25;
        r110 = r9;
        r2 = r8;
        r17 = r111;
        r11 = r7;
        r12 = r112;
        goto L45
    L22:
        HashMap r113 = r4;
        if (TtmlNode.TAG_HEAD.equals(r18) == false) goto L26;
        parseHeader(r111, r113, r9, r10, r7, r8);     // Catch: IOException -> L56 XmlPullParserException -> L58
        r17 = r111;
        HashMap r27 = r8;
        r11 = r7;
        r12 = r113;
        r2 = r27;
        r110 = r9;
    L31:
        r0 = r25;
        goto L45
    L26:
        r17 = r111;
        r2 = r8;
        r11 = r7;
        r12 = r113;
        r110 = r9;
        TtmlNode r28 = parseNode(r17, r1, r11, r25);     // Catch: SubtitleDecoderException -> L32 IOException -> L56 XmlPullParserException -> L58
        r5.push(r28);     // Catch: SubtitleDecoderException -> L32 IOException -> L56 XmlPullParserException -> L58
        if (r1 == null) goto L31;
        r1.addChild(r28);     // Catch: SubtitleDecoderException -> L32 IOException -> L56 XmlPullParserException -> L58
    L32:
        e = move-exception;
        FrameAndTickRate r114 = r25;
        Log.m49w(TAG, "Suppressing parser error", e);     // Catch: IOException -> L56 XmlPullParserException -> L58
        r14 = r14 + 1;     // Catch: IOException -> L56 XmlPullParserException -> L58
        r0 = r114;
        goto L45
    L17:
        r25 = r16;
        goto L18
    L46:
        r2 = r8;
        r11 = r7;
        r12 = r4;
        r17 = r3;
        r20 = r9;
        if (r15 != 2) goto L50;
        r14 = r14 + 1;     // Catch: IOException -> L56 XmlPullParserException -> L58
        r0 = r16;
        goto L53
    L50:
        if (r15 != 3) goto L52;
        r14 = r14 - 1;     // Catch: IOException -> L56 XmlPullParserException -> L58
        r0 = r16;
        goto L53
    L52:
        r0 = r16;
        goto L53
    L54:
        return (Subtitle) Assertions.checkNotNull(r13);
    L56:
        e = e;
    L63:
        throw new IllegalStateException("Unexpected error when reading input.", e);
    L58:
        e = e;
    L67:
        throw new IllegalStateException("Unable to decode source", e);
    L60:
        e = e;
    L64:
        e = e;
        goto L67
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public /* synthetic */ void reset() {
        SubtitleParser.CC.$default$reset(this);
    }
}
