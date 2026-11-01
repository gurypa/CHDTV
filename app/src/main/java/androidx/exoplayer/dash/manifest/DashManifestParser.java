package androidx.media3.exoplayer.dash.manifest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import android.util.Xml;
import androidx.exifinterface.media.ExifInterface;
import androidx.media3.common.C0180C;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.Label;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UriUtil;
import androidx.media3.common.util.Util;
import androidx.media3.common.util.XmlPullParserUtil;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import androidx.media3.extractor.metadata.emsg.EventMessage;
import androidx.media3.extractor.metadata.icy.IcyHeaders;
import androidx.media3.extractor.mp4.PsshAtomUtil;
import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.net.HttpHeaders;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

/* JADX INFO: loaded from: classes.dex */
public class DashManifestParser extends DefaultHandler implements ParsingLoadable.Parser<DashManifest> {
    private static final Pattern CEA_608_ACCESSIBILITY_PATTERN = null;
    private static final Pattern CEA_708_ACCESSIBILITY_PATTERN = null;
    private static final Pattern FRAME_RATE_PATTERN = null;
    private static final int[] MPEG_CHANNEL_CONFIGURATION_MAPPING = null;
    private static final String TAG = "MpdParser";
    private final XmlPullParserFactory xmlParserFactory;

    protected static final class RepresentationInfo {
        public final ImmutableList<BaseUrl> baseUrls;
        public final ArrayList<DrmInitData.SchemeData> drmSchemeDatas;
        public final String drmSchemeType;
        public final List<Descriptor> essentialProperties;
        public final Format format;
        public final ArrayList<Descriptor> inbandEventStreams;
        public final long revisionId;
        public final SegmentBase segmentBase;
        public final List<Descriptor> supplementalProperties;

        public RepresentationInfo(Format r2, List<BaseUrl> r3, SegmentBase r4, String r5, ArrayList<DrmInitData.SchemeData> r6, ArrayList<Descriptor> r7, List<Descriptor> r8, List<Descriptor> r9, long r10) {
            this.format = r2;
            this.baseUrls = ImmutableList.copyOf(r3);
            this.segmentBase = r4;
            this.drmSchemeType = r5;
            this.drmSchemeDatas = r6;
            this.inbandEventStreams = r7;
            this.essentialProperties = r8;
            this.supplementalProperties = r9;
            this.revisionId = r10;
        }
    }

    static {
        FRAME_RATE_PATTERN = Pattern.compile("(\\d+)(?:/(\\d+))?");
        CEA_608_ACCESSIBILITY_PATTERN = Pattern.compile("CC([1-4])=.*");
        CEA_708_ACCESSIBILITY_PATTERN = Pattern.compile("([1-9]|[1-5][0-9]|6[0-3])=.*");
        MPEG_CHANNEL_CONFIGURATION_MAPPING = new int[]{-1, 1, 2, 3, 4, 5, 6, 8, 2, 3, 4, 7, 8, 24, 8, 12, 10, 12, 14, 12, 14};
    }

    public DashManifestParser() {
        this.xmlParserFactory = XmlPullParserFactory.newInstance();     // Catch: XmlPullParserException -> L5
        return;
    L5:
        e = move-exception;
        throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
    }

    private long addSegmentTimelineElementsToList(List<SegmentBase.SegmentTimelineElement> r4, long r5, long r7, int r9, long r10) {
        if (r9 < 0) goto L4;
        int r0 = r9 + 1;
    L5:
        int r1 = 0;
    L6:
        if (r1 >= r0) goto L8;
        r4.add(buildSegmentTimelineElement(r5, r7));
        r5 = r5 + r7;
        r1 = r1 + 1;
        goto L6
    L8:
        return r5;
    L4:
        r0 = (int) Util.ceilDivide(r10 - r5, r7);
        goto L5
    }

    private static int checkContentTypeConsistency(int r1, int r2) {
        if (r1 != (-1)) goto L5;
        return r2;
    L5:
        if (r2 != (-1)) goto L7;
        return r1;
    L7:
        if (r1 != r2) goto L9;
        boolean r0 = true;
    L10:
        Assertions.checkState(r0);
        return r1;
    L9:
        r0 = false;
        goto L10
    }

    private static String checkLanguageConsistency(String r1, String r2) {
        if (r1 != null) goto L4;
        return r2;
    L4:
        if (r2 != null) goto L6;
        return r1;
    L6:
        Assertions.checkState(r1.equals(r2));
        return r1;
    }

    private static void fillInClearKeyInformation(ArrayList<DrmInitData.SchemeData> r7) {
        String r0 = null;
        int r1 = 0;
    L4:
        if (r1 >= r7.size()) goto L11;
        DrmInitData.SchemeData r2 = r7.get(r1);
        if (C0180C.CLEARKEY_UUID.equals(r2.uuid) == false) goto L10;
        if (r2.licenseServerUrl == null) goto L10;
        r0 = r2.licenseServerUrl;
        r7.remove(r1);
    L10:
        r1 = r1 + 1;
    L11:
        if (r0 != null) goto L13;
        return;
    L13:
        int r3 = 0;
    L15:
        if (r3 >= r7.size()) goto L22;
        DrmInitData.SchemeData r4 = r7.get(r3);
        if (C0180C.COMMON_PSSH_UUID.equals(r4.uuid) == false) goto L21;
        if (r4.licenseServerUrl != null) goto L21;
        r7.set(r3, new DrmInitData.SchemeData(C0180C.CLEARKEY_UUID, r0, r4.mimeType, r4.data));
    L21:
        r3 = r3 + 1;
        goto L15
    }

    private static void filterRedundantIncompleteSchemeDatas(ArrayList<DrmInitData.SchemeData> r4) {
        int r0 = r4.size() - 1;
    L3:
        if (r0 < 0) goto L14;
        DrmInitData.SchemeData r1 = r4.get(r0);
        if (r1.hasData() == true) goto L13;
        int r2 = 0;
    L8:
        if (r2 >= r4.size()) goto L13;
        if (r4.get(r2).canReplace(r1) == true) goto L11;
        r2 = r2 + 1;
        goto L8
    L11:
        r4.remove(r0);
    L13:
        r0 = r0 - 1;
        goto L3
    }

    private static long getFinalAvailabilityTimeOffset(long r4, long r6) {
        long r0 = r6;
        if (r0 != C0180C.TIME_UNSET) goto L6;
        r0 = r4;
    L6:
        if (r0 != Long.MAX_VALUE) goto L9;
        return C0180C.TIME_UNSET;
    L9:
        return r0;
    }

    private static String getSampleMimeType(String r2, String r3) {
        if (MimeTypes.isAudio(r2) == false) goto L7;
        return MimeTypes.getAudioMediaMimeType(r3);
    L7:
        if (MimeTypes.isVideo(r2) == false) goto L11;
        return MimeTypes.getVideoMediaMimeType(r3);
    L11:
        if (MimeTypes.isText(r2) == false) goto L14;
        return r2;
    L14:
        if (MimeTypes.isImage(r2) == false) goto L17;
        return r2;
    L17:
        if (MimeTypes.APPLICATION_MP4.equals(r2) == false) goto L23;
        String r0 = MimeTypes.getMediaMimeType(r3);
        if (MimeTypes.TEXT_VTT.equals(r0) == false) goto L22;
        return MimeTypes.APPLICATION_MP4VTT;
    L22:
        return r0;
    L23:
        return null;
    }

    private boolean isDvbProfileDeclared(String[] r6) {
        int r0 = r6.length;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L9;
        if (r6[r2].startsWith("urn:dvb:dash:profile:dvb-dash:") == true) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    public static void maybeSkipTag(XmlPullParser r2) throws IOException, XmlPullParserException {
        if (XmlPullParserUtil.isStartTag(r2) == true) goto L5;
        return;
    L5:
        int r0 = 1;
    L6:
        if (r0 == 0) goto L13;
        r2.next();
        if (XmlPullParserUtil.isStartTag(r2) == true) goto L9;
        if (XmlPullParserUtil.isEndTag(r2) == false) goto L6;
        r0 = r0 - 1;
        goto L6
    L9:
        r0 = r0 + 1;
        goto L6
    }

    protected static int parseCea608AccessibilityChannel(List<Descriptor> r5) {
        int r0 = 0;
    L4:
        if (r0 >= r5.size()) goto L15;
        Descriptor r1 = r5.get(r0);
        if ("urn:scte:dash:cc:cea-608:2015".equals(r1.schemeIdUri) == false) goto L14;
        if (r1.value == null) goto L14;
        Matcher r2 = CEA_608_ACCESSIBILITY_PATTERN.matcher(r1.value);
        if (r2.matches() == true) goto L12;
        Log.m48w(TAG, "Unable to parse CEA-608 channel number from: " + r1.value);
        goto L14
    L12:
        return Integer.parseInt(r2.group(1));
    L14:
        r0 = r0 + 1;
        goto L4
    L15:
        return -1;
    }

    protected static int parseCea708AccessibilityChannel(List<Descriptor> r5) {
        int r0 = 0;
    L4:
        if (r0 >= r5.size()) goto L15;
        Descriptor r1 = r5.get(r0);
        if ("urn:scte:dash:cc:cea-708:2015".equals(r1.schemeIdUri) == false) goto L14;
        if (r1.value == null) goto L14;
        Matcher r2 = CEA_708_ACCESSIBILITY_PATTERN.matcher(r1.value);
        if (r2.matches() == true) goto L12;
        Log.m48w(TAG, "Unable to parse CEA-708 service block number from: " + r1.value);
        goto L14
    L12:
        return Integer.parseInt(r2.group(1));
    L14:
        r0 = r0 + 1;
        goto L4
    L15:
        return -1;
    }

    protected static long parseDateTime(XmlPullParser r3, String r4, long r5) throws ParserException {
        String r0 = r3.getAttributeValue(null, r4);
        if (r0 != null) goto L6;
        return r5;
    L6:
        return Util.parseXsDateTime(r0);
    }

    protected static Descriptor parseDescriptor(XmlPullParser r4, String r5) throws XmlPullParserException, IOException {
        String r0 = parseString(r4, "schemeIdUri", "");
        String r1 = parseString(r4, "value", null);
        String r2 = parseString(r4, "id", null);
    L3:
        r4.next();
        if (XmlPullParserUtil.isEndTag(r4, r5) == false) goto L3;
        return new Descriptor(r0, r1, r2);
    }

    protected static int parseDolbyChannelConfiguration(XmlPullParser r6) {
        String r0 = r6.getAttributeValue(null, "value");
        if (r0 != null) goto L5;
        return -1;
    L5:
        String r2 = Ascii.toLowerCase(r0);
        switch(r2.hashCode()) {
            case 1596796: goto L21;
            case 2937391: goto L18;
            case 3094034: goto L15;
            case 3094035: goto L12;
            case 3133436: goto L9;
            default: goto L23;
        };
    L23:
        byte r3 = -1;
    L24:
        switch(r3) {
            case 0: goto L33;
            case 1: goto L32;
            case 2: goto L30;
            case 3: goto L28;
            case 4: goto L26;
            default: goto L25;
        };
    L25:
        return -1;
    L26:
        return 8;
    L28:
        return 6;
    L30:
        return 5;
    L32:
        return 2;
    L33:
        return 1;
    L9:
        if (r2.equals("fa01") == false) goto L34;
        r3 = 4;
        goto L24
    L34:
        r3 = -1;
        goto L24
    L12:
        if (r2.equals("f801") == false) goto L35;
        r3 = 3;
        goto L24
    L35:
        r3 = -1;
        goto L24
    L15:
        if (r2.equals("f800") == false) goto L36;
        r3 = 2;
        goto L24
    L36:
        r3 = -1;
        goto L24
    L18:
        if (r2.equals("a000") == false) goto L37;
        r3 = 1;
        goto L24
    L37:
        r3 = -1;
        goto L24
    L21:
        if (r2.equals("4000") == false) goto L38;
        r3 = 0;
        goto L24
    L38:
        r3 = -1;
        goto L24
    }

    protected static int parseDtsChannelConfiguration(XmlPullParser r3) {
        int r0 = parseInt(r3, "value", -1);
        if (r0 > 0) goto L5;
        return -1;
    L5:
        if (r0 < 33) goto L7;
        return -1;
    L7:
        return r0;
    }

    protected static int parseDtsxChannelConfiguration(XmlPullParser r3) {
        String r0 = r3.getAttributeValue(null, "value");
        if (r0 != null) goto L5;
        return -1;
    L5:
        int r2 = Integer.bitCount(Integer.parseInt(r0, 16));
        if (r2 != 0) goto L9;
        return -1;
    L9:
        return r2;
    }

    protected static long parseDuration(XmlPullParser r3, String r4, long r5) {
        String r0 = r3.getAttributeValue(null, r4);
        if (r0 != null) goto L6;
        return r5;
    L6:
        return Util.parseXsDuration(r0);
    }

    protected static String parseEac3SupplementalProperties(List<Descriptor> r5) {
        int r0 = 0;
    L4:
        if (r0 >= r5.size()) goto L17;
        Descriptor r1 = r5.get(r0);
        String r2 = r1.schemeIdUri;
        if ("tag:dolby.com,2018:dash:EC3_ExtensionType:2018".equals(r2) == false) goto L11;
        if ("JOC".equals(r1.value) == false) goto L11;
        return MimeTypes.AUDIO_E_AC3_JOC;
    L11:
        if ("tag:dolby.com,2014:dash:DolbyDigitalPlusExtensionType:2014".equals(r2) == false) goto L16;
        if (MimeTypes.CODEC_E_AC3_JOC.equals(r1.value) == false) goto L16;
        return MimeTypes.AUDIO_E_AC3_JOC;
    L16:
        r0 = r0 + 1;
        goto L4
    L17:
        return MimeTypes.AUDIO_E_AC3;
    }

    protected static float parseFloat(XmlPullParser r2, String r3, float r4) {
        String r0 = r2.getAttributeValue(null, r3);
        if (r0 != null) goto L6;
        return r4;
    L6:
        return Float.parseFloat(r0);
    }

    protected static float parseFrameRate(XmlPullParser r7, float r8) {
        String r1 = r7.getAttributeValue(null, "frameRate");
        if (r1 == null) goto L11;
        Matcher r2 = FRAME_RATE_PATTERN.matcher(r1);
        if (r2.matches() == false) goto L12;
        int r3 = Integer.parseInt(r2.group(1));
        String r4 = r2.group(2);
        if (TextUtils.isEmpty(r4) == true) goto L10;
        return r3 / Integer.parseInt(r4);
    L10:
        return r3;
    L12:
        return r8;
    L11:
        return r8;
    }

    protected static int parseInt(XmlPullParser r2, String r3, int r4) {
        String r0 = r2.getAttributeValue(null, r3);
        if (r0 != null) goto L6;
        return r4;
    L6:
        return Integer.parseInt(r0);
    }

    protected static long parseLastSegmentNumberSupplementalProperty(List<Descriptor> r4) {
        int r0 = 0;
    L4:
        if (r0 >= r4.size()) goto L10;
        Descriptor r1 = r4.get(r0);
        if (Ascii.equalsIgnoreCase("http://dashif.org/guidelines/last-segment-number", r1.schemeIdUri) == true) goto L8;
        r0 = r0 + 1;
        goto L4
    L8:
        return Long.parseLong(r1.value);
    L10:
        return -1;
    }

    protected static long parseLong(XmlPullParser r3, String r4, long r5) {
        String r0 = r3.getAttributeValue(null, r4);
        if (r0 != null) goto L6;
        return r5;
    L6:
        return Long.parseLong(r0);
    }

    protected static int parseMpegChannelConfiguration(XmlPullParser r3) {
        int r0 = parseInt(r3, "value", -1);
        if (r0 >= 0) goto L5;
        return -1;
    L5:
        if (r0 < MPEG_CHANNEL_CONFIGURATION_MAPPING.length) goto L8;
        return -1;
    L8:
        return MPEG_CHANNEL_CONFIGURATION_MAPPING[r0];
    }

    protected static String parseString(XmlPullParser r2, String r3, String r4) {
        String r0 = r2.getAttributeValue(null, r3);
        if (r0 != null) goto L6;
        return r4;
    L6:
        return r0;
    }

    protected static String parseText(XmlPullParser r3, String r4) throws XmlPullParserException, IOException {
        String r0 = "";
    L3:
        r3.next();
        if (r3.getEventType() != 4) goto L6;
        r0 = r3.getText();
    L8:
        if (XmlPullParserUtil.isEndTag(r3, r4) == false) goto L3;
        return r0;
    L6:
        maybeSkipTag(r3);
        goto L8
    }

    protected AdaptationSet buildAdaptationSet(long r9, int r11, List<Representation> r12, List<Descriptor> r13, List<Descriptor> r14, List<Descriptor> r15) {
        return new AdaptationSet(r9, r11, r12, r13, r14, r15);
    }

    protected EventMessage buildEvent(String r9, String r10, long r11, long r13, byte[] r15) {
        return new EventMessage(r9, r10, r13, r11, r15);
    }

    protected EventStream buildEventStream(String r8, String r9, long r10, long[] r12, EventMessage[] r13) {
        return new EventStream(r8, r9, r10, r12, r13);
    }

    protected Format buildFormat(String r18, String r19, int r20, int r21, float r22, int r23, int r24, int r25, String r26, List<Descriptor> r27, List<Descriptor> r28, String r29, List<Descriptor> r30, List<Descriptor> r31) {
        String r6 = r29;
        String r7 = getSampleMimeType(r19, r6);
        if (MimeTypes.AUDIO_E_AC3.equals(r7) == false) goto L7;
        r7 = parseEac3SupplementalProperties(r31);
        if (MimeTypes.AUDIO_E_AC3_JOC.equals(r7) == false) goto L7;
        r6 = MimeTypes.CODEC_E_AC3_JOC;
    L7:
        int r8 = parseSelectionFlagsFromRoleDescriptors(r27);
        int r9 = ((parseRoleFlagsFromRoleDescriptors(r27) | parseRoleFlagsFromAccessibilityDescriptors(r28)) | parseRoleFlagsFromProperties(r30)) | parseRoleFlagsFromProperties(r31);
        Pair<Integer, Integer> r12 = parseTileCountFromProperties(r30);
        Format.Builder r13 = new Format.Builder().setId(r18).setContainerMimeType(r19).setSampleMimeType(r7).setCodecs(r6).setPeakBitrate(r25).setSelectionFlags(r8).setRoleFlags(r9).setLanguage(r26);
        int r16 = -1;
        if (r12 == null) goto L10;
        int r0 = ((Integer) r12.first).intValue();
    L11:
        Format.Builder r1 = r13.setTileCountHorizontal(r0);
        if (r12 == null) goto L14;
        r16 = ((Integer) r12.second).intValue();
    L14:
        Format.Builder r2 = r1.setTileCountVertical(r16);
        if (MimeTypes.isVideo(r7) == false) goto L18;
        r2.setWidth(r20).setHeight(r21).setFrameRate(r22);
    L35:
        return r2.build();
    L18:
        if (MimeTypes.isAudio(r7) == false) goto L21;
        r2.setChannelCount(r23).setSampleRate(r24);
        goto L35
    L21:
        if (MimeTypes.isText(r7) == false) goto L31;
        if (MimeTypes.APPLICATION_CEA608.equals(r7) == false) goto L26;
        int r3 = parseCea608AccessibilityChannel(r28);
    L29:
        r2.setAccessibilityChannel(r3);
        goto L35
    L26:
        if (MimeTypes.APPLICATION_CEA708.equals(r7) == false) goto L28;
        r3 = parseCea708AccessibilityChannel(r28);
        goto L29
    L28:
        r3 = -1;
        goto L29
    L31:
        if (MimeTypes.isImage(r7) == false) goto L35;
        r2.setWidth(r20).setHeight(r21);
        goto L35
    L10:
        r0 = -1;
        goto L11
    }

    protected DashManifest buildMediaPresentationDescription(long r22, long r24, long r26, boolean r28, long r29, long r31, long r33, long r35, ProgramInformation r37, UtcTimingElement r38, ServiceDescriptionElement r39, Uri r40, List<Period> r41) {
        return new DashManifest(r22, r24, r26, r28, r29, r31, r33, r35, r37, r38, r39, r40, r41);
    }

    protected Period buildPeriod(String r8, long r9, List<AdaptationSet> r11, List<EventStream> r12, Descriptor r13) {
        return new Period(r8, r9, r11, r12, r13);
    }

    protected RangedUri buildRangedUri(String r7, long r8, long r10) {
        return new RangedUri(r7, r8, r10);
    }

    protected Representation buildRepresentation(RepresentationInfo r18, String r19, List<Label> r20, String r21, ArrayList<DrmInitData.SchemeData> r22, ArrayList<Descriptor> r23) {
        Format.Builder r2 = r18.format.buildUpon();
        if (r19 != null) goto L5;
    L7:
        r2.setLabels(r20);
    L8:
        String r4 = r18.drmSchemeType;
        if (r4 != null) goto L11;
        r4 = r21;
    L11:
        ArrayList<DrmInitData.SchemeData> r5 = r18.drmSchemeDatas;
        r5.addAll(r22);
        if (r5.isEmpty() == true) goto L14;
        fillInClearKeyInformation(r5);
        filterRedundantIncompleteSchemeDatas(r5);
        r2.setDrmInitData(new DrmInitData(r4, r5));
    L14:
        ArrayList<Descriptor> r13 = r18.inbandEventStreams;
        r13.addAll(r23);
        return Representation.newInstance(r18.revisionId, r2.build(), r18.baseUrls, r18.segmentBase, r13, r18.essentialProperties, r18.supplementalProperties, null);
    L5:
        if (r20.isEmpty() == false) goto L7;
        r2.setLabel(r19);
        goto L8
    }

    protected SegmentBase.SegmentList buildSegmentList(RangedUri r19, long r20, long r22, long r24, long r26, List<SegmentBase.SegmentTimelineElement> r28, long r29, List<RangedUri> r31, long r32, long r34) {
        return new SegmentBase.SegmentList(r19, r20, r22, r24, r26, r28, r29, r31, Util.msToUs(r32), Util.msToUs(r34));
    }

    protected SegmentBase.SegmentTemplate buildSegmentTemplate(RangedUri r22, long r23, long r25, long r27, long r29, long r31, List<SegmentBase.SegmentTimelineElement> r33, long r34, UrlTemplate r36, UrlTemplate r37, long r38, long r40) {
        return new SegmentBase.SegmentTemplate(r22, r23, r25, r27, r29, r31, r33, r34, r36, r37, Util.msToUs(r38), Util.msToUs(r40));
    }

    protected SegmentBase.SegmentTimelineElement buildSegmentTimelineElement(long r2, long r4) {
        return new SegmentBase.SegmentTimelineElement(r2, r4);
    }

    protected SegmentBase.SingleSegmentBase buildSingleSegmentBase(RangedUri r11, long r12, long r14, long r16, long r18) {
        return new SegmentBase.SingleSegmentBase(r11, r12, r14, r16, r18);
    }

    protected UtcTimingElement buildUtcTimingElement(String r2, String r3) {
        return new UtcTimingElement(r2, r3);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
    public DashManifest parse(Uri r6, InputStream r7) throws IOException {
        XmlPullParser r1 = this.xmlParserFactory.newPullParser();     // Catch: XmlPullParserException -> L11
        r1.setInput(r7, null);     // Catch: XmlPullParserException -> L11
        if (r1.next() != 2) goto L10;
        if ("MPD".equals(r1.getName()) == false) goto L10;
        return parseMediaPresentationDescription(r1, r6);
    L10:
        throw ParserException.createForMalformedManifest("inputStream does not contain a valid media presentation description", null);     // Catch: XmlPullParserException -> L11
    L11:
        e = move-exception;
        throw ParserException.createForMalformedManifest(null, e);
    }

    @Override // androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
    public /* bridge */ /* synthetic */ DashManifest parse(Uri r1, InputStream r2) throws IOException {
        return parse(r1, r2);
    }

    protected AdaptationSet parseAdaptationSet(XmlPullParser r43, List<BaseUrl> r44, SegmentBase r45, long r46, long r48, long r50, long r52, long r54, boolean r56) throws XmlPullParserException, IOException {
        XmlPullParser r1 = r43;
        long r28 = parseLong(r1, "id", -1);
        int r2 = parseContentType(r43);
        String r3 = r1.getAttributeValue(null, "mimeType");
        String r5 = r1.getAttributeValue(null, "codecs");
        int r6 = parseInt(r1, StreamInformation.KEY_WIDTH, -1);
        int r8 = parseInt(r1, StreamInformation.KEY_HEIGHT, -1);
        float r9 = parseFrameRate(r1, -1.0f);
        int r7 = parseInt(r1, "audioSamplingRate", -1);
        String r11 = "lang";
        String r12 = r1.getAttributeValue(null, "lang");
        String r30 = r1.getAttributeValue(null, "label");
        List<Label> r13 = new ArrayList();
        ArrayList<DrmInitData.SchemeData> r15 = new ArrayList();
        ArrayList<Descriptor> r31 = new ArrayList();
        ArrayList r17 = new ArrayList();
        ArrayList r18 = new ArrayList();
        ArrayList r19 = new ArrayList();
        ArrayList r20 = new ArrayList();
        ArrayList r32 = new ArrayList();
        ArrayList r16 = new ArrayList();
        long r23 = r50;
        int r21 = r7;
        int r10 = r8;
        float r14 = r9;
        int r22 = -1;
        String r34 = null;
        boolean r35 = false;
        SegmentBase r110 = r45;
        String r111 = r5;
        String r112 = r12;
        long r4 = r48;
        int r113 = r2;
    L3:
        r1.next();
        if (XmlPullParserUtil.isStartTag(r1, "BaseURL") == false) goto L8;
        if (r35 == true) goto L7;
        r4 = parseAvailabilityTimeOffsetUs(r1, r4);
        r35 = true;
    L7:
        String r24 = r3;
        long r49 = r4;
        r16.addAll(parseBaseUrl(r1, r44, r56));
        String r39 = r11;
        List<Label> r25 = r13;
        ArrayList<DrmInitData.SchemeData> r37 = r15;
        SegmentBase r26 = r110;
        long r27 = r23;
        ArrayList r47 = r32;
        String r29 = r34;
        int r114 = r6;
        int r115 = r10;
        int r210 = r22;
        String r211 = r112;
        ArrayList r33 = r20;
        ArrayList<Descriptor> r36 = r31;
        String r116 = r111;
        ArrayList r212 = r19;
        long r38 = r49;
        ArrayList r117 = r18;
        ArrayList r118 = r17;
        float r119 = r14;
        XmlPullParser r40 = r1;
    L62:
        if (XmlPullParserUtil.isEndTag(r40, "AdaptationSet") == true) goto L63;
        List<Label> r310 = r25;
        r34 = r29;
        r32 = r47;
        r30 = r30;
        r31 = r36;
        r1 = r40;
        r4 = r38;
        r111 = r116;
        r6 = r114;
        r14 = r119;
        r17 = r118;
        r18 = r117;
        r19 = r212;
        r3 = r24;
        r112 = r211;
        r22 = r210;
        r23 = r27;
        r13 = r310;
        r15 = r37;
        r11 = r39;
        r20 = r33;
        r10 = r115;
        r110 = r26;
        goto L3
    L63:
        List<Representation> r0 = new ArrayList(r47.size());
        int r120 = 0;
    L65:
        if (r120 >= r47.size()) goto L68;
        ArrayList r41 = r47;
        String r42 = r30;
        r0.add(buildRepresentation((RepresentationInfo) r41.get(r120), r42, r25, r29, r37, r36));
        r120 = r120 + 1;
        r47 = r41;
        r30 = r42;
        goto L65
    L68:
        return buildAdaptationSet(r28, r113, r0, r118, r212, r33);
    L8:
        r24 = r3;
        if (XmlPullParserUtil.isStartTag(r1, "ContentProtection") == false) goto L18;
        Pair<String, DrmInitData.SchemeData> r51 = parseContentProtection(r43);
        if (r51.first == null) goto L14;
        r34 = (String) r51.first;
    L14:
        if (r51.second == null) goto L16;
        r15.add((DrmInitData.SchemeData) r51.second);
    L16:
        r39 = r11;
        r25 = r13;
        r37 = r15;
        r26 = r110;
        r27 = r23;
        r47 = r32;
        r114 = r6;
        r115 = r10;
        r210 = r22;
        r211 = r112;
        r33 = r20;
        r36 = r31;
        r116 = r111;
        r212 = r19;
        r38 = r4;
        r117 = r18;
        r29 = r34;
        r118 = r17;
        r119 = r14;
        r40 = r1;
        goto L62
    L18:
        if (XmlPullParserUtil.isStartTag(r1, "ContentComponent") == false) goto L21;
        String r53 = checkLanguageConsistency(r112, r1.getAttributeValue(null, r11));
        r113 = checkContentTypeConsistency(r113, parseContentType(r43));
        r116 = r111;
        r39 = r11;
        r25 = r13;
        r37 = r15;
        r26 = r110;
        r27 = r23;
        r47 = r32;
        r211 = r53;
        r114 = r6;
        r115 = r10;
        r210 = r22;
        r33 = r20;
        r36 = r31;
        r38 = r4;
        r212 = r19;
        r29 = r34;
        r117 = r18;
        r118 = r17;
        r119 = r14;
        r40 = r1;
        goto L62
    L21:
        if (XmlPullParserUtil.isStartTag(r1, "Role") == false) goto L23;
        ArrayList r55 = r18;
        r55.add(parseDescriptor(r1, "Role"));
        r39 = r11;
        int r213 = r113;
        r25 = r13;
        r37 = r15;
        r26 = r110;
        r118 = r17;
        r47 = r32;
        r114 = r6;
        r115 = r10;
        r119 = r14;
        r33 = r20;
        r36 = r31;
        r40 = r1;
        long r57 = r4;
        r212 = r19;
        long r58 = r23;
        r117 = r55;
        r210 = r22;
        r211 = r112;
        r116 = r111;
    L60:
        r38 = r57;
        r113 = r213;
        r27 = r58;
        r29 = r34;
        goto L62
    L23:
        ArrayList r59 = r18;
        if (XmlPullParserUtil.isStartTag(r1, "AudioChannelConfiguration") == false) goto L27;
        r39 = r11;
        r25 = r13;
        r37 = r15;
        r26 = r110;
        r118 = r17;
        r27 = r23;
        r47 = r32;
        r210 = parseAudioChannelConfiguration(r43);
        r114 = r6;
        r115 = r10;
        r119 = r14;
        r211 = r112;
        r33 = r20;
        r36 = r31;
        r40 = r1;
        r116 = r111;
        r212 = r19;
        r117 = r59;
        r38 = r4;
        r29 = r34;
        goto L62
    L27:
        if (XmlPullParserUtil.isStartTag(r1, "Accessibility") == false) goto L29;
        ArrayList r60 = r17;
        r60.add(parseDescriptor(r1, "Accessibility"));
        r119 = r14;
        r39 = r11;
        r213 = r113;
        r25 = r13;
        r37 = r15;
        r26 = r110;
        r47 = r32;
        r40 = r1;
        r57 = r4;
        r114 = r6;
        r115 = r10;
        r33 = r20;
        r58 = r23;
        r36 = r31;
        r210 = r22;
        r211 = r112;
        r212 = r19;
        r116 = r111;
        r117 = r59;
        r118 = r60;
        goto L60
    L29:
        ArrayList r61 = r17;
        if (XmlPullParserUtil.isStartTag(r1, "EssentialProperty") == false) goto L32;
        ArrayList r62 = r19;
        r62.add(parseDescriptor(r1, "EssentialProperty"));
        r39 = r11;
        r213 = r113;
        r25 = r13;
        r37 = r15;
        r26 = r110;
        r117 = r59;
        r47 = r32;
        r114 = r6;
        r115 = r10;
        r118 = r61;
        r33 = r20;
        r36 = r31;
        r212 = r62;
        r119 = r14;
        r40 = r1;
        r57 = r4;
        r58 = r23;
        r210 = r22;
        r211 = r112;
        r116 = r111;
        goto L60
    L32:
        ArrayList r63 = r19;
        if (XmlPullParserUtil.isStartTag(r1, "SupplementalProperty") == false) goto L35;
        ArrayList r64 = r20;
        r64.add(parseDescriptor(r1, "SupplementalProperty"));
        r39 = r11;
        r213 = r113;
        r25 = r13;
        r37 = r15;
        r26 = r110;
        r212 = r63;
        r47 = r32;
        r114 = r6;
        r115 = r10;
        r117 = r59;
        r36 = r31;
        r33 = r64;
        r118 = r61;
        r119 = r14;
        r40 = r1;
        r57 = r4;
        r58 = r23;
        r210 = r22;
        r211 = r112;
        r116 = r111;
        goto L60
    L35:
        ArrayList r65 = r20;
        if (XmlPullParserUtil.isStartTag(r1, "Representation") == true) goto L38;
        r39 = r11;
        int r66 = r113;
        List<Label> r311 = r13;
        r37 = r15;
        r26 = r110;
        r212 = r63;
        r114 = r6;
        r115 = r10;
        r117 = r59;
        ArrayList r67 = r32;
        XmlPullParser r68 = r1;
        r118 = r61;
        r119 = r14;
        long r69 = r4;
        r58 = r23;
        r210 = r22;
        r211 = r112;
        r116 = r111;
        if (XmlPullParserUtil.isStartTag(r68, "SegmentBase") == false) goto L46;
        r113 = r66;
        r26 = parseSegmentBase(r68, (SegmentBase.SingleSegmentBase) r26);
        r27 = r58;
        r47 = r67;
        r38 = r69;
        r33 = r65;
        r36 = r31;
        r29 = r34;
        r25 = r311;
        r40 = r68;
        goto L62
    L46:
        if (XmlPullParserUtil.isStartTag(r68, "SegmentList") == false) goto L48;
        long r70 = parseAvailabilityTimeOffsetUs(r68, r58);
        r47 = r67;
        r26 = parseSegmentList(r43, (SegmentBase.SegmentList) r26, r52, r46, r69, r70, r54);
        r113 = r66;
        r36 = r31;
        r29 = r34;
        r25 = r311;
        r27 = r70;
        r38 = r69;
        r33 = r65;
        r40 = r43;
        goto L62
    L48:
        r213 = r66;
        r47 = r67;
        if (XmlPullParserUtil.isStartTag(r68, "SegmentTemplate") == false) goto L51;
        long r121 = parseAvailabilityTimeOffsetUs(r68, r58);
        SegmentBase r71 = parseSegmentTemplate(r68, (SegmentBase.SegmentTemplate) r26, r65, r52, r46, r69, r121, r54);
        r40 = r68;
        r33 = r65;
        r26 = r71;
        r113 = r213;
        r36 = r31;
        r29 = r34;
        r25 = r311;
        r27 = r121;
        r38 = r69;
        goto L62
    L51:
        r40 = r68;
        r57 = r69;
        r33 = r65;
        if (XmlPullParserUtil.isStartTag(r40, "InbandEventStream") == false) goto L54;
        r36 = r31;
        r36.add(parseDescriptor(r40, "InbandEventStream"));
        r25 = r311;
        goto L60
    L54:
        r36 = r31;
        if (XmlPullParserUtil.isStartTag(r40, "Label") == false) goto L57;
        r25 = r311;
        r25.add(parseLabel(r43));
        goto L60
    L57:
        r25 = r311;
        if (XmlPullParserUtil.isStartTag(r40) == false) goto L60;
        parseAdaptationSetChild(r43);
        goto L60
    L38:
        if (r16.isEmpty() == true) goto L40;
        List<BaseUrl> r72 = r16;
    L41:
        r39 = r11;
        String r122 = r112;
        r37 = r15;
        XmlPullParser r73 = r1;
        long r410 = r4;
        String r74 = r111;
        int r123 = r21;
        RepresentationInfo r75 = parseRepresentation(r73, r72, r24, r74, r6, r10, r14, r22, r123, r122, r59, r61, r63, r65, r110, r52, r46, r410, r23, r54, r56);
        r119 = r14;
        r117 = r59;
        r118 = r61;
        r212 = r63;
        r26 = r110;
        r116 = r74;
        r114 = r6;
        r115 = r10;
        r24 = r24;
        r21 = r123;
        long r76 = r23;
        r210 = r22;
        r211 = r122;
        r113 = checkContentTypeConsistency(r113, MimeTypes.getTrackType(r75.format.sampleMimeType));
        ArrayList r77 = r32;
        r77.add(r75);
        r27 = r76;
        r47 = r77;
        r38 = r410;
        r33 = r65;
        r36 = r31;
        r29 = r34;
        r25 = r13;
        r40 = r73;
        goto L62
    L40:
        r72 = r44;
        goto L41
    }

    protected void parseAdaptationSetChild(XmlPullParser r1) throws XmlPullParserException, IOException {
        maybeSkipTag(r1);
    }

    protected int parseAudioChannelConfiguration(XmlPullParser r4) throws XmlPullParserException, IOException {
        String r0 = parseString(r4, "schemeIdUri", null);
        switch(r0.hashCode()) {
            case -2128649360: goto L24;
            case -1352850286: goto L21;
            case -1138141449: goto L18;
            case -986633423: goto L15;
            case -79006963: goto L12;
            case 312179081: goto L9;
            case 2036691300: goto L6;
            default: goto L26;
        };
    L26:
        byte r1 = -1;
    L27:
        switch(r1) {
            case 0: goto L33;
            case 1: goto L32;
            case 2: goto L31;
            case 3: goto L31;
            case 4: goto L30;
            case 5: goto L29;
            case 6: goto L29;
            default: goto L28;
        };
    L28:
        int r2 = -1;
    L34:
        r4.next();
        if (XmlPullParserUtil.isEndTag(r4, "AudioChannelConfiguration") == false) goto L34;
        return r2;
    L29:
        r2 = parseDolbyChannelConfiguration(r4);
        goto L34
    L30:
        r2 = parseDtsxChannelConfiguration(r4);
        goto L34
    L31:
        r2 = parseDtsChannelConfiguration(r4);
        goto L34
    L32:
        r2 = parseMpegChannelConfiguration(r4);
        goto L34
    L33:
        r2 = parseInt(r4, "value", -1);
        goto L34
    L6:
        if (r0.equals("urn:dolby:dash:audio_channel_configuration:2011") == false) goto L40;
        r1 = 6;
        goto L27
    L40:
        r1 = -1;
        goto L27
    L9:
        if (r0.equals("tag:dts.com,2018:uhd:audio_channel_configuration") == false) goto L41;
        r1 = 4;
        goto L27
    L41:
        r1 = -1;
        goto L27
    L12:
        if (r0.equals("tag:dts.com,2014:dash:audio_channel_configuration:2012") == false) goto L42;
        r1 = 2;
        goto L27
    L42:
        r1 = -1;
        goto L27
    L15:
        if (r0.equals("urn:mpeg:mpegB:cicp:ChannelConfiguration") == false) goto L43;
        r1 = 1;
        goto L27
    L43:
        r1 = -1;
        goto L27
    L18:
        if (r0.equals("tag:dolby.com,2014:dash:audio_channel_configuration:2011") == false) goto L44;
        r1 = 5;
        goto L27
    L44:
        r1 = -1;
        goto L27
    L21:
        if (r0.equals("urn:mpeg:dash:23003:3:audio_channel_configuration:2011") == false) goto L45;
        r1 = 0;
        goto L27
    L45:
        r1 = -1;
        goto L27
    L24:
        if (r0.equals("urn:dts:dash:audio_channel_configuration:2012") == false) goto L46;
        r1 = 3;
        goto L27
    L46:
        r1 = -1;
        goto L27
    }

    protected long parseAvailabilityTimeOffsetUs(XmlPullParser r4, long r5) {
        String r0 = r4.getAttributeValue(null, "availabilityTimeOffset");
        if (r0 != null) goto L6;
        return r5;
    L6:
        if ("INF".equals(r0) == false) goto L10;
        return Long.MAX_VALUE;
    L10:
        return (long) (Float.parseFloat(r0) * 1000000.0f);
    }

    protected List<BaseUrl> parseBaseUrl(XmlPullParser r13, List<BaseUrl> r14, boolean r15) throws XmlPullParserException, IOException {
        String r0 = r13.getAttributeValue(null, "dvb:priority");
        if (r0 == null) goto L5;
        int r3 = Integer.parseInt(r0);
    L8:
        String r4 = r13.getAttributeValue(null, "dvb:weight");
        if (r4 == null) goto L11;
        int r5 = Integer.parseInt(r4);
    L12:
        String r1 = r13.getAttributeValue(null, "serviceLocation");
        String r6 = parseText(r13, "BaseURL");
        if (UriUtil.isAbsolute(r6) == false) goto L18;
        if (r1 != null) goto L17;
        r1 = r6;
    L17:
        return Lists.newArrayList(new BaseUrl[]{new BaseUrl(r6, r1, r3, r5)});
    L18:
        ArrayList r2 = new ArrayList();
        int r7 = 0;
    L20:
        if (r7 >= r14.size()) goto L28;
        BaseUrl r8 = r14.get(r7);
        String r9 = UriUtil.resolve(r8.url, r6);
        if (r1 != null) goto L24;
        String r10 = r9;
    L25:
        if (r15 == false) goto L27;
        r3 = r8.priority;
        r5 = r8.weight;
        r10 = r8.serviceLocation;
    L27:
        r2.add(new BaseUrl(r9, r10, r3, r5));
        r7 = r7 + 1;
        goto L20
    L24:
        r10 = r1;
        goto L25
    L28:
        return r2;
    L11:
        r5 = 1;
        goto L12
    L5:
        if (r15 == false) goto L7;
        r3 = 1;
        goto L8
    L7:
        r3 = Integer.MIN_VALUE;
        goto L8
    }

    protected Pair<String, DrmInitData.SchemeData> parseContentProtection(XmlPullParser r14) throws XmlPullParserException, IOException {
        String r0 = null;
        String r1 = null;
        byte[] r2 = null;
        UUID r3 = null;
        DrmInitData.SchemeData r5 = null;
        String r4 = r14.getAttributeValue(null, "schemeIdUri");
        if (r4 == null) goto L35;
        String r8 = Ascii.toLowerCase(r4);
        switch(r8.hashCode()) {
            case -1980789791: goto L17;
            case 489446379: goto L14;
            case 755418770: goto L11;
            case 1812765994: goto L8;
            default: goto L19;
        };
    L19:
        byte r9 = -1;
    L20:
        switch(r9) {
            case 0: goto L25;
            case 1: goto L24;
            case 2: goto L23;
            case 3: goto L22;
            default: goto L35;
        };
    L22:
        r3 = C0180C.CLEARKEY_UUID;
        goto L35
    L23:
        r3 = C0180C.WIDEVINE_UUID;
        goto L35
    L24:
        r3 = C0180C.PLAYREADY_UUID;
        goto L35
    L25:
        r0 = r14.getAttributeValue(null, "value");
        String r10 = XmlPullParserUtil.getAttributeValueIgnorePrefix(r14, "default_KID");
        if (TextUtils.isEmpty(r10) == false) goto L28;
    L34:
        Log.m48w(TAG, "Ignoring <ContentProtection> with schemeIdUri=\"urn:mpeg:dash:mp4protection:2011\" (ClearKey) due to missing required default_KID attribute.");
        goto L35
    L28:
        if ("00000000-0000-0000-0000-000000000000".equals(r10) == true) goto L34;
        String[] r11 = r10.split("\\s+");
        UUID[] r12 = new UUID[r11.length];
        int r13 = 0;
    L31:
        if (r13 >= r11.length) goto L33;
        r12[r13] = UUID.fromString(r11[r13]);
        r13 = r13 + 1;
        goto L31
    L33:
        r2 = PsshAtomUtil.buildPsshAtom(C0180C.COMMON_PSSH_UUID, r12, null);
        r3 = C0180C.COMMON_PSSH_UUID;
        goto L35
    L8:
        if (r8.equals("urn:mpeg:dash:mp4protection:2011") == false) goto L19;
        r9 = 0;
        goto L20
    L11:
        if (r8.equals("urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed") == false) goto L19;
        r9 = 2;
        goto L20
    L14:
        if (r8.equals("urn:uuid:9a04f079-9840-4286-ab92-e65be0885f95") == false) goto L19;
        r9 = 1;
        goto L20
    L17:
        if (r8.equals("urn:uuid:e2719d58-a985-b3c9-781a-b030af78d30e") == false) goto L19;
        r9 = 3;
    L35:
        r14.next();
        if (XmlPullParserUtil.isStartTag(r14, "clearkey:Laurl") == true) goto L40;
        if (XmlPullParserUtil.isStartTag(r14, "dashif:Laurl") == true) goto L40;
    L43:
        if (XmlPullParserUtil.isStartTag(r14, "ms:laurl") == false) goto L45;
        r1 = r14.getAttributeValue(null, "licenseUrl");
    L63:
        if (XmlPullParserUtil.isEndTag(r14, "ContentProtection") == false) goto L35;
        if (r3 == null) goto L67;
        r5 = new DrmInitData.SchemeData(r3, r1, MimeTypes.VIDEO_MP4, r2);
    L67:
        return Pair.create(r0, r5);
    L45:
        if (r2 == null) goto L47;
    L53:
        if (r2 == null) goto L55;
    L61:
        maybeSkipTag(r14);
        goto L63
    L55:
        if (C0180C.PLAYREADY_UUID.equals(r3) == false) goto L61;
        if (XmlPullParserUtil.isStartTag(r14, "mspr:pro") == false) goto L61;
        if (r14.next() != 4) goto L61;
        r2 = PsshAtomUtil.buildPsshAtom(C0180C.PLAYREADY_UUID, Base64.decode(r14.getText(), 0));
        goto L63
    L47:
        if (XmlPullParserUtil.isStartTagIgnorePrefix(r14, "pssh") == false) goto L53;
        if (r14.next() != 4) goto L53;
        r2 = Base64.decode(r14.getText(), 0);
        r3 = PsshAtomUtil.parseUuid(r2);
        if (r3 != null) goto L63;
        Log.m48w(TAG, "Skipping malformed cenc:pssh data");
        r2 = null;
    L40:
        if (r14.next() != 4) goto L43;
        r1 = r14.getText();
        goto L63
    }

    protected int parseContentType(XmlPullParser r4) {
        String r0 = r4.getAttributeValue(null, "contentType");
        if (TextUtils.isEmpty(r0) == false) goto L6;
        return -1;
    L6:
        if (MimeTypes.BASE_TYPE_AUDIO.equals(r0) == false) goto L9;
        return 1;
    L9:
        if (MimeTypes.BASE_TYPE_VIDEO.equals(r0) == false) goto L12;
        return 2;
    L12:
        if ("text".equals(r0) == false) goto L15;
        return 3;
    L15:
        if ("image".equals(r0) == false) goto L23;
        return 4;
    L23:
        return -1;
    }

    protected Pair<Long, EventMessage> parseEvent(XmlPullParser r20, String r21, String r22, long r23, long r25, ByteArrayOutputStream r27) throws IOException, XmlPullParserException {
        long r7 = parseLong(r20, "id", 0);
        long r9 = parseLong(r20, MediaInformation.KEY_DURATION, C0180C.TIME_UNSET);
        long r1 = parseLong(r20, "presentationTime", 0);
        long r3 = Util.scaleLargeTimestamp(r9, 1000, r23);
        long r12 = Util.scaleLargeTimestamp(r1 - r25, 1000000, r23);
        String r14 = parseString(r20, "messageData", null);
        byte[] r16 = parseEventObject(r20, r27);
        Long r6 = Long.valueOf(r12);
        if (r14 != null) goto L5;
        byte[] r11 = r16;
    L7:
        return Pair.create(r6, buildEvent(r21, r22, r7, r3, r11));
    L5:
        r11 = Util.getUtf8Bytes(r14);
        goto L7
    }

    protected byte[] parseEventObject(XmlPullParser r6, ByteArrayOutputStream r7) throws XmlPullParserException, IOException {
        r7.reset();
        XmlSerializer r0 = Xml.newSerializer();
        r0.setOutput(r7, StandardCharsets.UTF_8.name());
        r6.nextToken();
    L4:
        if (XmlPullParserUtil.isEndTag(r6, "Event") == true) goto L24;
        switch(r6.getEventType()) {
            case 0: goto L22;
            case 1: goto L21;
            case 2: goto L16;
            case 3: goto L15;
            case 4: goto L14;
            case 5: goto L13;
            case 6: goto L12;
            case 7: goto L11;
            case 8: goto L10;
            case 9: goto L9;
            case 10: goto L8;
            default: goto L23;
        };
    L8:
        r0.docdecl(r6.getText());
        goto L23
    L9:
        r0.comment(r6.getText());
        goto L23
    L10:
        r0.processingInstruction(r6.getText());
        goto L23
    L11:
        r0.ignorableWhitespace(r6.getText());
        goto L23
    L12:
        r0.entityRef(r6.getText());
        goto L23
    L13:
        r0.cdsect(r6.getText());
        goto L23
    L14:
        r0.text(r6.getText());
        goto L23
    L15:
        r0.endTag(r6.getNamespace(), r6.getName());
        goto L23
    L16:
        r0.startTag(r6.getNamespace(), r6.getName());
        int r1 = 0;
    L18:
        if (r1 >= r6.getAttributeCount()) goto L23;
        r0.attribute(r6.getAttributeNamespace(r1), r6.getAttributeName(r1), r6.getAttributeValue(r1));
        r1 = r1 + 1;
        goto L18
    L21:
        r0.endDocument();
        goto L23
    L22:
        r0.startDocument(null, false);
    L23:
        r6.nextToken();
        goto L4
    L24:
        r0.flush();
        return r7.toByteArray();
    }

    protected EventStream parseEventStream(XmlPullParser r15) throws XmlPullParserException, IOException {
        String r3 = parseString(r15, "schemeIdUri", "");
        String r4 = parseString(r15, "value", "");
        long r5 = parseLong(r15, "timescale", 1);
        long r8 = parseLong(r15, "presentationTimeOffset", 0);
        ArrayList r0 = new ArrayList();
        ByteArrayOutputStream r10 = new ByteArrayOutputStream(512);
    L3:
        r15.next();
        if (XmlPullParserUtil.isStartTag(r15, "Event") == false) goto L6;
        long r6 = r5;
        String r7 = r4;
        String r9 = r3;
        XmlPullParser r11 = r15;
        Pair<Long, EventMessage> r16 = parseEvent(r11, r9, r7, r6, r8, r10);
        XmlPullParser r1 = r11;
        r3 = r9;
        r4 = r7;
        r5 = r6;
        r0.add(r16);
    L8:
        if (XmlPullParserUtil.isEndTag(r1, "EventStream") == true) goto L9;
        r15 = r1;
        r8 = r8;
        r10 = r10;
        goto L3
    L9:
        long[] r12 = new long[r0.size()];
        EventMessage[] r13 = new EventMessage[r0.size()];
        int r17 = 0;
    L11:
        if (r17 >= r0.size()) goto L14;
        Pair r2 = (Pair) r0.get(r17);
        r12[r17] = ((Long) r2.first).longValue();
        r13[r17] = (EventMessage) r2.second;
        r17 = r17 + 1;
        goto L11
    L14:
        return buildEventStream(r3, r4, r5, r12, r13);
    L6:
        r1 = r15;
        maybeSkipTag(r1);
        goto L8
    }

    protected RangedUri parseInitialization(XmlPullParser r3) {
        return parseRangedUrl(r3, "sourceURL", "range");
    }

    protected Label parseLabel(XmlPullParser r4) throws XmlPullParserException, IOException {
        return new Label(r4.getAttributeValue(null, "lang"), parseText(r4, "Label"));
    }

    protected DashManifest parseMediaPresentationDescription(XmlPullParser r45, Uri r46) throws XmlPullParserException, IOException {
        DashManifestParser r0 = this;
        XmlPullParser r1 = r45;
        boolean r11 = r0.isDvbProfileDeclared(r0.parseProfiles(r1, "profiles", new String[0]));
        long r7 = parseDateTime(r1, "availabilityStartTime", C0180C.TIME_UNSET);
        long r14 = parseDuration(r1, "mediaPresentationDuration", C0180C.TIME_UNSET);
        long r16 = parseDuration(r1, "minBufferTime", C0180C.TIME_UNSET);
        boolean r18 = "dynamic".equals(r1.getAttributeValue(null, "type"));
        if (r18 == false) goto L5;
        long r9 = parseDuration(r1, "minimumUpdatePeriod", C0180C.TIME_UNSET);
    L6:
        long r19 = r9;
        if (r18 == false) goto L9;
        long r10 = parseDuration(r1, "timeShiftBufferDepth", C0180C.TIME_UNSET);
    L10:
        if (r18 == false) goto L12;
        long r5 = parseDuration(r1, "suggestedPresentationDelay", C0180C.TIME_UNSET);
    L13:
        long r21 = r5;
        long r23 = parseDateTime(r1, "publishTime", C0180C.TIME_UNSET);
        long r27 = 0;
        if (r18 == false) goto L16;
        long r29 = 0;
    L17:
        String r4 = r46.toString();
        String r12 = r46.toString();
        if (r11 == false) goto L20;
        int r3 = 1;
    L21:
        ArrayList r13 = Lists.newArrayList(new BaseUrl[]{new BaseUrl(r4, r12, r3, 1)});
        ArrayList r15 = new ArrayList();
        ArrayList r2 = new ArrayList();
        if (r18 == false) goto L24;
        r27 = -9223372036854775807L;
    L24:
        boolean r25 = false;
        boolean r26 = false;
        long r6 = r27;
        ProgramInformation r28 = null;
        UtcTimingElement r210 = null;
        long r8 = r29;
        Uri r211 = null;
        ServiceDescriptionElement r30 = null;
    L25:
        r1.next();
        long r37 = r6;
        if (XmlPullParserUtil.isStartTag(r1, "BaseURL") == false) goto L31;
        if (r26 == true) goto L29;
        r8 = r0.parseAvailabilityTimeOffsetUs(r1, r8);
        r26 = true;
    L29:
        r2.addAll(r0.parseBaseUrl(r1, r13, r11));
        XmlPullParser r17 = r1;
        UtcTimingElement r110 = r210;
        long r40 = r37;
        ArrayList r111 = r15;
        boolean r212 = r26;
        ProgramInformation r42 = r28;
        boolean r213 = r25;
        long r214 = r8;
        ProgramInformation r112 = r42;
    L63:
        if (XmlPullParserUtil.isEndTag(r17, "MPD") == true) goto L65;
        long r20 = r21;
        r1 = r17;
        r15 = r111;
        r0 = this;
        r21 = r20;
        r19 = r19;
        r10 = r10;
        r13 = r13;
        r11 = r11;
        UtcTimingElement r43 = r110;
        r18 = r18;
        boolean r44 = r213;
        r28 = r112;
        r16 = r16;
        r8 = r214;
        r25 = r44;
        r26 = r212;
        r7 = r7;
        r210 = r43;
        r2 = r2;
        r6 = r40;
        goto L25
    L65:
        if (r14 == C0180C.TIME_UNSET) goto L67;
    L73:
        long r22 = r14;
    L75:
        if (r111.isEmpty() == true) goto L79;
        return buildMediaPresentationDescription(r7, r22, r16, r18, r19, r10, r21, r23, r112, r110, r30, r211, r111);
    L79:
        throw ParserException.createForMalformedManifest("No periods found.", null);
    L67:
        if (r40 == C0180C.TIME_UNSET) goto L69;
        r22 = r40;
        goto L75
    L69:
        if (r18 != 0) goto L73;
        throw ParserException.createForMalformedManifest("Unable to determine duration of static manifest.", null);
    L31:
        if (XmlPullParserUtil.isStartTag(r1, "ProgramInformation") == false) goto L34;
        r17 = r1;
        r110 = r210;
        r40 = r37;
        r111 = r15;
        r212 = r26;
        r213 = r25;
        r214 = r8;
        r112 = parseProgramInformation(r45);
        goto L63
    L34:
        if (XmlPullParserUtil.isStartTag(r1, "UTCTiming") == false) goto L37;
        r17 = r1;
        r110 = parseUtcTiming(r45);
        r40 = r37;
        r111 = r15;
        r212 = r26;
        ProgramInformation r47 = r28;
        r213 = r25;
        r214 = r8;
        r112 = r47;
        goto L63
    L37:
        if (XmlPullParserUtil.isStartTag(r1, HttpHeaders.LOCATION) == false) goto L40;
        r211 = UriUtil.resolveToUri(r46.toString(), r1.nextText());
        r17 = r1;
        r110 = r210;
        r40 = r37;
        r111 = r15;
        r212 = r26;
        ProgramInformation r48 = r28;
        r213 = r25;
        r214 = r8;
        r112 = r48;
        goto L63
    L40:
        if (XmlPullParserUtil.isStartTag(r1, "ServiceDescription") == false) goto L43;
        r30 = parseServiceDescription(r45);
        r17 = r1;
        r110 = r210;
        r40 = r37;
        r111 = r15;
        r212 = r26;
        ProgramInformation r49 = r28;
        r213 = r25;
        r214 = r8;
        r112 = r49;
        goto L63
    L43:
        if (XmlPullParserUtil.isStartTag(r1, "Period") == false) goto L61;
        if (r25 == true) goto L61;
        if (r2.isEmpty() == false) goto L49;
        r2 = r13;
    L49:
        long r24 = r37;
        Pair<Period, Long> r31 = r0.parsePeriod(r1, r2, r24, r8, r7, r10, r11);
        r17 = r1;
        Period r32 = (Period) r31.first;
        r111 = r15;
        if (r32.startMs != C0180C.TIME_UNSET) goto L55;
        if (r18 == false) goto L54;
        r25 = true;
    L60:
        r40 = r24;
        r110 = r210;
        r212 = r26;
        ProgramInformation r410 = r28;
        r213 = r25;
        r214 = r8;
        r112 = r410;
        goto L63
    L54:
        throw ParserException.createForMalformedManifest("Unable to determine start of period " + r111.size(), null);
    L55:
        long r113 = ((Long) r31.second).longValue();
        if (r113 != C0180C.TIME_UNSET) goto L58;
        long r33 = -9223372036854775807L;
    L59:
        r24 = r33;
        r111.add(r32);
        goto L60
    L58:
        r33 = r32.startMs + r113;
    L61:
        r17 = r1;
        r40 = r37;
        r111 = r15;
        maybeSkipTag(r17);
        r110 = r210;
        r212 = r26;
        ProgramInformation r411 = r28;
        r213 = r25;
        r214 = r8;
        r112 = r411;
        goto L63
    L20:
        r3 = Integer.MIN_VALUE;
        goto L21
    L16:
        r29 = -9223372036854775807L;
        goto L17
    L12:
        r5 = -9223372036854775807L;
        goto L13
    L9:
        r10 = -9223372036854775807L;
        goto L10
    L5:
        r9 = -9223372036854775807L;
        goto L6
    }

    protected Pair<Period, Long> parsePeriod(XmlPullParser r32, List<BaseUrl> r33, long r34, long r36, long r38, long r40, boolean r42) throws XmlPullParserException, IOException {
        XmlPullParser r1 = r32;
        String r16 = r1.getAttributeValue(null, "id");
        long r17 = parseDuration(r1, "start", r34);
        if (r38 == C0180C.TIME_UNSET) goto L5;
        long r10 = r38 + r17;
    L6:
        long r7 = parseDuration(r1, MediaInformation.KEY_DURATION, C0180C.TIME_UNSET);
        SegmentBase r2 = null;
        ArrayList r12 = new ArrayList();
        ArrayList r13 = new ArrayList();
        ArrayList r14 = new ArrayList();
        long r5 = r36;
        long r24 = r7;
        Descriptor r19 = null;
        long r8 = -9223372036854775807L;
        boolean r20 = false;
    L7:
        r1.next();
        if (XmlPullParserUtil.isStartTag(r1, "BaseURL") == false) goto L12;
        if (r20 == true) goto L11;
        r5 = parseAvailabilityTimeOffsetUs(r1, r5);
        r20 = true;
    L11:
        r14.addAll(parseBaseUrl(r1, r33, r42));
        XmlPullParser r9 = r1;
        ArrayList r23 = r12;
        ArrayList r26 = r13;
        ArrayList r22 = r14;
        boolean r15 = r20;
        Object r21 = null;
        long r29 = r10;
        SegmentBase r11 = r2;
        long r18 = r5;
        Descriptor r6 = r19;
        long r110 = r8;
        long r25 = r29;
    L37:
        if (XmlPullParserUtil.isEndTag(r9, "Period") == true) goto L39;
        r1 = r9;
        r2 = r11;
        r14 = r22;
        r12 = r23;
        r10 = r25;
        r8 = r110;
        r19 = r6;
        r5 = r18;
        r20 = r15;
        r13 = r26;
        goto L7
    L39:
        return Pair.create(buildPeriod(r16, r17, r23, r26, r6), Long.valueOf(r24));
    L12:
        SegmentBase r37 = r2;
        if (XmlPullParserUtil.isStartTag(r1, "AdaptationSet") == true) goto L15;
        long r210 = r8;
        long r27 = r5;
        long r4 = r10;
        long r111 = r210;
        ArrayList r112 = r12;
        r26 = r13;
        r22 = r14;
        long r28 = r24;
        if (XmlPullParserUtil.isStartTag(r1, "EventStream") == false) goto L23;
        r26.add(parseEventStream(r32));
        r24 = r28;
        SegmentBase r39 = r37;
        r23 = r112;
        r21 = null;
        r9 = r1;
        long r0 = r27;
        r25 = r4;
    L35:
        r6 = r19;
        r15 = r20;
        r110 = r111;
        r11 = r39;
        r18 = r0;
        goto L37
    L23:
        if (XmlPullParserUtil.isStartTag(r1, "SegmentBase") == false) goto L26;
        r21 = null;
        r24 = r28;
        r26 = r26;
        r23 = r112;
        r6 = r19;
        r15 = r20;
        r9 = r1;
        r110 = r111;
        r11 = parseSegmentBase(r1, null);
        r18 = r27;
        r25 = r4;
        goto L37
    L26:
        if (XmlPullParserUtil.isStartTag(r1, "SegmentList") == false) goto L28;
        long r30 = parseAvailabilityTimeOffsetUs(r1, C0180C.TIME_UNSET);
        r23 = r112;
        r21 = null;
        SegmentBase.SegmentList r3 = parseSegmentList(r1, null, r4, r28, r27, r30, r40);
        r24 = r28;
        r26 = r26;
        r6 = r19;
        r15 = r20;
        r9 = r1;
        r110 = r30;
        r11 = r3;
        r18 = r27;
        r25 = r4;
        goto L37
    L28:
        r21 = null;
        r39 = r37;
        r23 = r112;
        if (XmlPullParserUtil.isStartTag(r1, "SegmentTemplate") == false) goto L31;
        long r113 = parseAvailabilityTimeOffsetUs(r1, C0180C.TIME_UNSET);
        r26 = r26;
        SegmentBase.SegmentTemplate r31 = parseSegmentTemplate(r1, null, ImmutableList.m212of(), r4, r28, r27, r113, r40);
        r24 = r28;
        r9 = r1;
        r25 = r4;
        r6 = r19;
        r15 = r20;
        r110 = r113;
        r18 = r27;
        r11 = r31;
        goto L37
    L31:
        r24 = r28;
        r26 = r26;
        r9 = r1;
        r0 = r27;
        r25 = r4;
        if (XmlPullParserUtil.isStartTag(r9, "AssetIdentifier") == false) goto L34;
        r6 = parseDescriptor(r9, "AssetIdentifier");
        r15 = r20;
        r110 = r111;
        r11 = r39;
        r18 = r0;
        goto L37
    L34:
        maybeSkipTag(r9);
        goto L35
    L15:
        if (r14.isEmpty() == true) goto L17;
        List r35 = r14;
    L18:
        long r41 = r5;
        r26 = r13;
        r22 = r14;
        long r43 = r24;
        ArrayList r114 = r12;
        AdaptationSet r44 = parseAdaptationSet(r1, r35, r37, r43, r41, r8, r10, r40, r42);
        long r45 = r10;
        r111 = r8;
        r114.add(r44);
        r24 = r43;
        r39 = r37;
        r23 = r114;
        r21 = null;
        r9 = r1;
        r0 = r41;
        r25 = r45;
        goto L35
    L17:
        r35 = r33;
        goto L18
    L5:
        r10 = -9223372036854775807L;
        goto L6
    }

    protected String[] parseProfiles(XmlPullParser r3, String r4, String[] r5) {
        String r0 = r3.getAttributeValue(null, r4);
        if (r0 != null) goto L6;
        return r5;
    L6:
        return r0.split(",");
    }

    protected ProgramInformation parseProgramInformation(XmlPullParser r12) throws IOException, XmlPullParserException {
        String r0 = null;
        String r1 = null;
        String r2 = null;
        String r9 = parseString(r12, "moreInformationURL", null);
        String r10 = parseString(r12, "lang", null);
    L3:
        r12.next();
        if (XmlPullParserUtil.isStartTag(r12, "Title") == false) goto L7;
        String r6 = r12.nextText();
        String r7 = r1;
        String r8 = r2;
    L14:
        if (XmlPullParserUtil.isEndTag(r12, "ProgramInformation") == true) goto L16;
        r0 = r6;
        r1 = r7;
        r2 = r8;
        goto L3
    L16:
        return new ProgramInformation(r6, r7, r8, r9, r10);
    L7:
        if (XmlPullParserUtil.isStartTag(r12, "Source") == false) goto L10;
        r6 = r0;
        r7 = r12.nextText();
        r8 = r2;
        goto L14
    L10:
        if (XmlPullParserUtil.isStartTag(r12, ExifInterface.TAG_COPYRIGHT) == false) goto L12;
        r6 = r0;
        r7 = r1;
        r8 = r12.nextText();
        goto L14
    L12:
        maybeSkipTag(r12);
        r6 = r0;
        r7 = r1;
        r8 = r2;
        goto L14
    }

    protected RangedUri parseRangedUrl(XmlPullParser r12, String r13, String r14) {
        String r2 = r12.getAttributeValue(null, r13);
        long r3 = 0;
        long r5 = -1;
        String r0 = r12.getAttributeValue(null, r14);
        if (r0 == null) goto L8;
        String[] r1 = r0.split("-");
        r3 = Long.parseLong(r1[0]);
        if (r1.length != 2) goto L8;
        r5 = (Long.parseLong(r1[1]) - r3) + 1;
    L8:
        return buildRangedUri(r2, r3, r5);
    }

    protected RepresentationInfo parseRepresentation(XmlPullParser r42, List<BaseUrl> r43, String r44, String r45, int r46, int r47, float r48, int r49, int r50, String r51, List<Descriptor> r52, List<Descriptor> r53, List<Descriptor> r54, List<Descriptor> r55, SegmentBase r56, long r57, long r59, long r61, long r63, long r65, boolean r67) throws XmlPullParserException, IOException {
        XmlPullParser r1 = r42;
        String r14 = r1.getAttributeValue(null, "id");
        int r15 = parseInt(r1, "bandwidth", -1);
        String r16 = parseString(r1, "mimeType", r44);
        String r17 = parseString(r1, "codecs", r45);
        int r18 = parseInt(r1, StreamInformation.KEY_WIDTH, r46);
        int r19 = parseInt(r1, StreamInformation.KEY_HEIGHT, r47);
        float r20 = parseFrameRate(r1, r48);
        int r6 = r49;
        int r21 = parseInt(r1, "audioSamplingRate", r50);
        ArrayList r27 = new ArrayList();
        ArrayList r28 = new ArrayList();
        ArrayList r29 = new ArrayList(r54);
        ArrayList r30 = new ArrayList(r55);
        ArrayList r9 = new ArrayList();
        SegmentBase r22 = r56;
        long r2 = r63;
        String r24 = null;
        boolean r25 = false;
        long r7 = r61;
    L3:
        int r23 = r6;
        r1.next();
        if (XmlPullParserUtil.isStartTag(r1, "BaseURL") == false) goto L9;
        if (r25 == true) goto L7;
        r7 = parseAvailabilityTimeOffsetUs(r1, r7);
        r25 = true;
    L7:
        r9.addAll(parseBaseUrl(r1, r43, r67));
        XmlPullParser r0 = r1;
        long r34 = r2;
        long r36 = r7;
        ArrayList r33 = r9;
        int r26 = r15;
        r6 = r23;
        boolean r38 = r25;
        ArrayList r110 = r27;
        ArrayList r13 = r29;
        ArrayList r10 = r30;
        String r210 = r14;
        ArrayList r111 = r28;
    L41:
        if (XmlPullParserUtil.isEndTag(r0, "Representation") == true) goto L42;
        r28 = r111;
        String r3 = r210;
        r27 = r110;
        r14 = r3;
        r15 = r26;
        r30 = r10;
        r29 = r13;
        r9 = r33;
        r2 = r34;
        r7 = r36;
        r25 = r38;
        r1 = r42;
        goto L3
    L42:
        ArrayList r211 = r111;
        List<Descriptor> r112 = r10;
        Format r212 = buildFormat(r210, r16, r18, r19, r20, r6, r21, r26, r51, r52, r53, r17, r13, r112);
        if (r22 == null) goto L45;
        SegmentBase r213 = r22;
    L46:
        String r214 = r24;
        if (r33.isEmpty() == true) goto L49;
        List r215 = r33;
    L51:
        return new RepresentationInfo(r212, r215, r213, r214, r110, r211, r13, r112, -1);
    L49:
        r215 = r43;
        goto L51
    L45:
        r213 = new SegmentBase.SingleSegmentBase();
        goto L46
    L9:
        if (XmlPullParserUtil.isStartTag(r1, "AudioChannelConfiguration") == false) goto L12;
        r0 = r1;
        r34 = r2;
        r36 = r7;
        r33 = r9;
        r26 = r15;
        r6 = parseAudioChannelConfiguration(r42);
        r38 = r25;
        r110 = r27;
        r13 = r29;
        r10 = r30;
        r210 = r14;
        r111 = r28;
        goto L41
    L12:
        if (XmlPullParserUtil.isStartTag(r1, "SegmentBase") == false) goto L15;
        r0 = r1;
        r34 = r2;
        r22 = parseSegmentBase(r1, (SegmentBase.SingleSegmentBase) r22);
        r36 = r7;
        r33 = r9;
        r26 = r15;
        r6 = r23;
        r38 = r25;
        r110 = r27;
        r13 = r29;
        r10 = r30;
        r210 = r14;
        r111 = r28;
        goto L41
    L15:
        if (XmlPullParserUtil.isStartTag(r1, "SegmentList") == false) goto L17;
        ArrayList r4 = r9;
        long r11 = parseAvailabilityTimeOffsetUs(r1, r2);
        r33 = r4;
        r26 = r15;
        r110 = r27;
        r210 = r14;
        r111 = r28;
        r0 = r1;
        r22 = parseSegmentList(r1, (SegmentBase.SegmentList) r22, r57, r59, r7, r11, r65);
        r36 = r7;
        r6 = r23;
        r38 = r25;
        r13 = r29;
        r10 = r30;
        r34 = r11;
        goto L41
    L17:
        r33 = r9;
        r26 = r15;
        r110 = r27;
        ArrayList r35 = r29;
        ArrayList r37 = r30;
        r210 = r14;
        r111 = r28;
        if (XmlPullParserUtil.isStartTag(r1, "SegmentTemplate") == false) goto L20;
        long r12 = parseAvailabilityTimeOffsetUs(r1, r2);
        long r8 = r7;
        r0 = r1;
        r22 = parseSegmentTemplate(r1, (SegmentBase.SegmentTemplate) r22, r55, r57, r59, r8, r12, r65);
        r36 = r8;
        r6 = r23;
        r38 = r25;
        r13 = r35;
        r10 = r37;
        r34 = r12;
        goto L41
    L20:
        r0 = r1;
        if (XmlPullParserUtil.isStartTag(r0, "ContentProtection") == false) goto L30;
        Pair<String, DrmInitData.SchemeData> r5 = parseContentProtection(r42);
        if (r5.first == null) goto L26;
        r24 = (String) r5.first;
    L26:
        if (r5.second == null) goto L28;
        r110.add((DrmInitData.SchemeData) r5.second);
    L28:
        r36 = r7;
        r6 = r23;
        r38 = r25;
        r13 = r35;
        r10 = r37;
        r34 = r2;
        goto L41
    L30:
        if (XmlPullParserUtil.isStartTag(r0, "InbandEventStream") == false) goto L33;
        r111.add(parseDescriptor(r0, "InbandEventStream"));
        r13 = r35;
        r10 = r37;
    L39:
        r34 = r2;
        r36 = r7;
        r6 = r23;
        r38 = r25;
        goto L41
    L33:
        if (XmlPullParserUtil.isStartTag(r0, "EssentialProperty") == false) goto L35;
        r13 = r35;
        r13.add(parseDescriptor(r0, "EssentialProperty"));
        r10 = r37;
        goto L39
    L35:
        r13 = r35;
        if (XmlPullParserUtil.isStartTag(r0, "SupplementalProperty") == false) goto L38;
        r10 = r37;
        r10.add(parseDescriptor(r0, "SupplementalProperty"));
        goto L39
    L38:
        r10 = r37;
        maybeSkipTag(r0);
        goto L39
    }

    protected int parseRoleFlagsFromAccessibilityDescriptors(List<Descriptor> r6) {
        int r0 = 0;
        int r1 = 0;
    L4:
        if (r1 >= r6.size()) goto L12;
        Descriptor r2 = r6.get(r1);
        if (Ascii.equalsIgnoreCase("urn:mpeg:dash:role:2011", r2.schemeIdUri) == false) goto L9;
        r0 = r0 | parseRoleFlagsFromDashRoleScheme(r2.value);
    L11:
        r1 = r1 + 1;
        goto L4
    L9:
        if (Ascii.equalsIgnoreCase("urn:tva:metadata:cs:AudioPurposeCS:2007", r2.schemeIdUri) == false) goto L11;
        r0 = r0 | parseTvaAudioPurposeCsValue(r2.value);
        goto L11
    L12:
        return r0;
    }

    protected int parseRoleFlagsFromDashRoleScheme(String r7) {
        if (r7 != null) goto L6;
        return 0;
    L6:
        switch(r7.hashCode()) {
            case -2060497896: goto L45;
            case -1724546052: goto L42;
            case -1580883024: goto L39;
            case -1574842690: goto L36;
            case -1408024454: goto L33;
            case -1396432756: goto L30;
            case 99825: goto L27;
            case 3343801: goto L24;
            case 3530173: goto L21;
            case 552573414: goto L18;
            case 899152809: goto L15;
            case 1629013393: goto L12;
            case 1855372047: goto L9;
            default: goto L47;
        };
    L47:
        byte r1 = -1;
    L48:
        switch(r1) {
            case 0: goto L67;
            case 1: goto L66;
            case 2: goto L65;
            case 3: goto L64;
            case 4: goto L62;
            case 5: goto L60;
            case 6: goto L58;
            case 7: goto L56;
            case 8: goto L56;
            case 9: goto L56;
            case 10: goto L54;
            case 11: goto L52;
            case 12: goto L50;
            default: goto L49;
        };
    L49:
        return 0;
    L50:
        return 2048;
    L52:
        return 512;
    L54:
        return 256;
    L56:
        return 128;
    L58:
        return 64;
    L60:
        return 32;
    L62:
        return 16;
    L64:
        return 8;
    L65:
        return 4;
    L66:
        return 2;
    L67:
        return 1;
    L9:
        if (r7.equals("supplementary") == false) goto L47;
        r1 = 2;
        goto L48
    L12:
        if (r7.equals("emergency") == false) goto L47;
        r1 = 5;
        goto L48
    L15:
        if (r7.equals("commentary") == false) goto L47;
        r1 = 3;
        goto L48
    L18:
        if (r7.equals("caption") == false) goto L47;
        r1 = 6;
        goto L48
    L21:
        if (r7.equals("sign") == false) goto L47;
        r1 = 10;
        goto L48
    L24:
        if (r7.equals("main") == false) goto L47;
        r1 = 0;
        goto L48
    L27:
        if (r7.equals("dub") == false) goto L47;
        r1 = 4;
        goto L48
    L30:
        if (r7.equals("forced-subtitle") == false) goto L47;
        r1 = 8;
        goto L48
    L33:
        if (r7.equals("alternate") == false) goto L47;
        r1 = 1;
        goto L48
    L36:
        if (r7.equals("forced_subtitle") == false) goto L47;
        r1 = 7;
        goto L48
    L39:
        if (r7.equals("enhanced-audio-intelligibility") == false) goto L47;
        r1 = Ascii.f81FF;
        goto L48
    L42:
        if (r7.equals("description") == false) goto L47;
        r1 = Ascii.f92VT;
        goto L48
    L45:
        if (r7.equals("subtitle") == false) goto L47;
        r1 = 9;
        goto L48
    }

    protected int parseRoleFlagsFromProperties(List<Descriptor> r6) {
        int r0 = 0;
        int r1 = 0;
    L4:
        if (r1 >= r6.size()) goto L9;
        if (Ascii.equalsIgnoreCase("http://dashif.org/guidelines/trickmode", r6.get(r1).schemeIdUri) == false) goto L8;
        r0 = r0 | 16384;
    L8:
        r1 = r1 + 1;
        goto L4
    L9:
        return r0;
    }

    protected int parseRoleFlagsFromRoleDescriptors(List<Descriptor> r6) {
        int r0 = 0;
        int r1 = 0;
    L4:
        if (r1 >= r6.size()) goto L9;
        Descriptor r2 = r6.get(r1);
        if (Ascii.equalsIgnoreCase("urn:mpeg:dash:role:2011", r2.schemeIdUri) == false) goto L8;
        r0 = r0 | parseRoleFlagsFromDashRoleScheme(r2.value);
    L8:
        r1 = r1 + 1;
        goto L4
    L9:
        return r0;
    }

    protected SegmentBase.SingleSegmentBase parseSegmentBase(XmlPullParser r18, SegmentBase.SingleSegmentBase r19) throws XmlPullParserException, IOException {
        if (r19 == null) goto L5;
        long r4 = r19.timescale;
    L6:
        long r9 = parseLong(r18, "timescale", r4);
        long r5 = 0;
        if (r19 == null) goto L9;
        long r6 = r19.presentationTimeOffset;
    L10:
        long r11 = parseLong(r18, "presentationTimeOffset", r6);
        if (r19 == null) goto L13;
        long r7 = r19.indexStart;
    L14:
        if (r19 == null) goto L16;
        r5 = r19.indexLength;
    L16:
        RangedUri r13 = null;
        String r8 = r18.getAttributeValue(null, "indexRange");
        if (r8 == null) goto L19;
        String[] r14 = r8.split("-");
        r7 = Long.parseLong(r14[0]);
        long r15 = (Long.parseLong(r14[1]) - r7) + 1;
    L20:
        if (r19 == null) goto L22;
        r13 = r19.initialization;
    L22:
        r18.next();
        if (XmlPullParserUtil.isStartTag(r18, "Initialization") == false) goto L25;
        r13 = parseInitialization(r18);
    L27:
        if (XmlPullParserUtil.isEndTag(r18, "SegmentBase") == true) goto L29;
        r13 = r13;
        r8 = r8;
        goto L22
    L29:
        return buildSingleSegmentBase(r13, r9, r11, r7, r15);
    L25:
        maybeSkipTag(r18);
        goto L27
    L19:
        r15 = r5;
        goto L20
    L13:
        r7 = 0;
        goto L14
    L9:
        r6 = 0;
        goto L10
    L5:
        r4 = 1;
        goto L6
    }

    protected SegmentBase.SegmentList parseSegmentList(XmlPullParser r26, SegmentBase.SegmentList r27, long r28, long r30, long r32, long r34, long r36) throws XmlPullParserException, IOException {
        long r2 = 1;
        if (r27 == null) goto L5;
        long r4 = r27.timescale;
    L6:
        long r9 = parseLong(r26, "timescale", r4);
        if (r27 == null) goto L9;
        long r5 = r27.presentationTimeOffset;
    L10:
        long r11 = parseLong(r26, "presentationTimeOffset", r5);
        if (r27 == null) goto L13;
        long r6 = r27.duration;
    L14:
        long r15 = parseLong(r26, MediaInformation.KEY_DURATION, r6);
        if (r27 == null) goto L17;
        r2 = r27.startNumber;
    L17:
        long r13 = parseLong(r26, "startNumber", r2);
        long r18 = getFinalAvailabilityTimeOffset(r32, r34);
        RangedUri r7 = null;
        List<SegmentBase.SegmentTimelineElement> r8 = null;
        ArrayList r17 = null;
    L18:
        r26.next();
        if (XmlPullParserUtil.isStartTag(r26, "Initialization") == false) goto L22;
        r7 = parseInitialization(r26);
    L32:
        if (XmlPullParserUtil.isEndTag(r26, "SegmentList") == false) goto L18;
        if (r27 == null) goto L46;
        if (r7 == null) goto L36;
        RangedUri r0 = r7;
    L37:
        RangedUri r10 = r0;
        if (r8 == null) goto L40;
        List<SegmentBase.SegmentTimelineElement> r1 = r8;
    L41:
        List<SegmentBase.SegmentTimelineElement> r12 = r1;
        if (r17 == null) goto L44;
        List<RangedUri> r3 = r17;
    L45:
        List<RangedUri> r20 = r3;
        List<SegmentBase.SegmentTimelineElement> r19 = r12;
        RangedUri r14 = r10;
    L48:
        return buildSegmentList(r14, r9, r11, r13, r15, r19, r18, r20, r36, r28);
    L44:
        r3 = r27.mediaSegments;
        goto L45
    L40:
        r1 = r27.segmentTimeline;
        goto L41
    L36:
        r0 = r27.initialization;
        goto L37
    L46:
        r20 = r17;
        r19 = r8;
        r14 = r7;
        goto L48
    L22:
        if (XmlPullParserUtil.isStartTag(r26, "SegmentTimeline") == false) goto L25;
        r8 = parseSegmentTimeline(r26, r9, r30);
        goto L32
    L25:
        if (XmlPullParserUtil.isStartTag(r26, "SegmentURL") == false) goto L30;
        if (r17 != null) goto L28;
        ArrayList r16 = new ArrayList();
    L29:
        r16.add(parseSegmentUrl(r26));
        r17 = r16;
        goto L32
    L28:
        r16 = r17;
        goto L29
    L30:
        maybeSkipTag(r26);
        goto L32
    L13:
        r6 = C0180C.TIME_UNSET;
        goto L14
    L9:
        r5 = 0;
        goto L10
    L5:
        r4 = 1;
        goto L6
    }

    protected SegmentBase.SegmentTemplate parseSegmentTemplate(XmlPullParser r24, SegmentBase.SegmentTemplate r25, List<Descriptor> r26, long r27, long r29, long r31, long r33, long r35) throws XmlPullParserException, IOException {
        DashManifestParser r0 = this;
        XmlPullParser r1 = r24;
        SegmentBase.SegmentTemplate r6 = r25;
        long r2 = 1;
        if (r6 == null) goto L5;
        long r4 = r6.timescale;
    L6:
        long r5 = parseLong(r1, "timescale", r4);
        if (r6 == null) goto L9;
        long r7 = r6.presentationTimeOffset;
    L10:
        long r8 = parseLong(r1, "presentationTimeOffset", r7);
        if (r6 == null) goto L13;
        long r9 = r6.duration;
    L14:
        long r11 = parseLong(r1, MediaInformation.KEY_DURATION, r9);
        if (r6 == null) goto L17;
        r2 = r6.startNumber;
    L17:
        long r10 = parseLong(r1, "startNumber", r2);
        long r12 = parseLastSegmentNumberSupplementalProperty(r26);
        long r16 = r8;
        long r14 = getFinalAvailabilityTimeOffset(r31, r33);
        UrlTemplate r3 = null;
        if (r6 == null) goto L20;
        UrlTemplate r13 = r6.mediaTemplate;
    L21:
        UrlTemplate r15 = r0.parseUrlTemplate(r1, "media", r13);
        if (r6 == null) goto L24;
        r3 = r6.initializationTemplate;
    L24:
        UrlTemplate r18 = r0.parseUrlTemplate(r1, "initialization", r3);
        RangedUri r19 = null;
        List<SegmentBase.SegmentTimelineElement> r20 = null;
    L25:
        r1.next();
        if (XmlPullParserUtil.isStartTag(r1, "Initialization") == false) goto L29;
        XmlPullParser r17 = r1;
        r19 = parseInitialization(r24);
        long r21 = r5;
    L33:
        if (XmlPullParserUtil.isEndTag(r17, "SegmentTemplate") == true) goto L34;
        r1 = r17;
        r0 = this;
        r16 = r16;
        r6 = r25;
        r5 = r21;
        goto L25
    L34:
        if (r6 == null) goto L44;
        if (r19 == null) goto L37;
        RangedUri r22 = r19;
    L38:
        r19 = r22;
        if (r20 == null) goto L41;
        List<SegmentBase.SegmentTimelineElement> r23 = r20;
    L42:
        r20 = r23;
        goto L44
    L41:
        r23 = r6.segmentTimeline;
        goto L42
    L37:
        r22 = r6.initialization;
    L44:
        return buildSegmentTemplate(r19, r21, r16, r10, r12, r11, r20, r14, r18, r15, r35, r27);
    L29:
        if (XmlPullParserUtil.isStartTag(r1, "SegmentTimeline") == false) goto L31;
        r21 = r5;
        r20 = r0.parseSegmentTimeline(r1, r21, r29);
        r17 = r1;
        goto L33
    L31:
        r17 = r1;
        r21 = r5;
        maybeSkipTag(r17);
        goto L33
    L20:
        r13 = null;
        goto L21
    L13:
        r9 = C0180C.TIME_UNSET;
        goto L14
    L9:
        r7 = 0;
        goto L10
    L5:
        r4 = 1;
        goto L6
    }

    protected List<SegmentBase.SegmentTimelineElement> parseSegmentTimeline(XmlPullParser r18, long r19, long r21) throws XmlPullParserException, IOException {
        ArrayList r1 = new ArrayList();
        int r8 = 0;
        boolean r2 = false;
        long r6 = -9223372036854775807L;
        long r4 = 0;
    L3:
        r18.next();
        if (XmlPullParserUtil.isStartTag(r18, ExifInterface.LATITUDE_SOUTH) == false) goto L12;
        long r9 = parseLong(r18, "t", C0180C.TIME_UNSET);
        if (r2 == false) goto L9;
        r4 = addSegmentTimelineElementsToList(r1, r4, r6, r8, r9);
    L9:
        if (r9 == C0180C.TIME_UNSET) goto L11;
        r4 = r9;
    L11:
        r6 = parseLong(r18, "d", C0180C.TIME_UNSET);
        r8 = parseInt(r18, "r", 0);
        r2 = true;
    L14:
        if (XmlPullParserUtil.isEndTag(r18, "SegmentTimeline") == false) goto L3;
        if (r2 == false) goto L17;
        addSegmentTimelineElementsToList(r1, r4, r6, r8, Util.scaleLargeTimestamp(r21, r19, 1000));
    L17:
        return r1;
    L12:
        maybeSkipTag(r18);
        goto L14
    }

    protected RangedUri parseSegmentUrl(XmlPullParser r3) {
        return parseRangedUrl(r3, "media", "mediaRange");
    }

    protected int parseSelectionFlagsFromDashRoleScheme(String r3) {
        if (r3 != null) goto L6;
        return 0;
    L6:
        switch(r3.hashCode()) {
            case -1574842690: goto L12;
            case -1396432756: goto L9;
            default: goto L14;
        };
    L14:
        byte r1 = -1;
    L15:
        switch(r1) {
            case 0: goto L17;
            case 1: goto L17;
            default: goto L16;
        };
    L16:
        return 0;
    L17:
        return 2;
    L9:
        if (r3.equals("forced-subtitle") == false) goto L14;
        r1 = 1;
        goto L15
    L12:
        if (r3.equals("forced_subtitle") == false) goto L14;
        r1 = 0;
        goto L15
    }

    protected int parseSelectionFlagsFromRoleDescriptors(List<Descriptor> r6) {
        int r0 = 0;
        int r1 = 0;
    L4:
        if (r1 >= r6.size()) goto L9;
        Descriptor r2 = r6.get(r1);
        if (Ascii.equalsIgnoreCase("urn:mpeg:dash:role:2011", r2.schemeIdUri) == false) goto L8;
        r0 = r0 | parseSelectionFlagsFromDashRoleScheme(r2.value);
    L8:
        r1 = r1 + 1;
        goto L4
    L9:
        return r0;
    }

    protected ServiceDescriptionElement parseServiceDescription(XmlPullParser r19) throws XmlPullParserException, IOException {
        long r1 = C0180C.TIME_UNSET;
        long r3 = C0180C.TIME_UNSET;
        long r5 = C0180C.TIME_UNSET;
        float r7 = -3.4028235E38f;
        float r8 = -3.4028235E38f;
    L3:
        r19.next();
        if (XmlPullParserUtil.isStartTag(r19, "Latency") == false) goto L7;
        long r2 = parseLong(r19, "target", C0180C.TIME_UNSET);
        long r4 = parseLong(r19, "min", C0180C.TIME_UNSET);
        long r10 = r2;
        long r12 = r4;
        long r14 = parseLong(r19, "max", C0180C.TIME_UNSET);
        float r16 = r7;
        float r17 = r8;
    L11:
        if (XmlPullParserUtil.isEndTag(r19, "ServiceDescription") == true) goto L13;
        r1 = r10;
        r3 = r12;
        r5 = r14;
        r7 = r16;
        r8 = r17;
        goto L3
    L13:
        return new ServiceDescriptionElement(r10, r12, r14, r16, r17);
    L7:
        if (XmlPullParserUtil.isStartTag(r19, "PlaybackRate") == false) goto L9;
        float r9 = parseFloat(r19, "min", -3.4028235E38f);
        r10 = r1;
        r12 = r3;
        r14 = r5;
        r16 = r9;
        r17 = parseFloat(r19, "max", -3.4028235E38f);
        goto L11
    L9:
        r10 = r1;
        r12 = r3;
        r14 = r5;
        r16 = r7;
        r17 = r8;
        goto L11
    }

    protected Pair<Integer, Integer> parseTileCountFromProperties(List<Descriptor> r9) {
        int r0 = 0;
    L4:
        if (r0 >= r9.size()) goto L19;
        Descriptor r1 = r9.get(r0);
        if (Ascii.equalsIgnoreCase("http://dashif.org/thumbnail_tile", r1.schemeIdUri) == true) goto L10;
        if (Ascii.equalsIgnoreCase("http://dashif.org/guidelines/thumbnail_tile", r1.schemeIdUri) == true) goto L10;
    L18:
        r0 = r0 + 1;
    L10:
        if (r1.value == null) goto L18;
        String[] r3 = Util.split(r1.value, "x");
        if (r3.length != 2) goto L18;
        int r4 = Integer.parseInt(r3[0]);     // Catch: NumberFormatException -> L17
        int r5 = Integer.parseInt(r3[1]);     // Catch: NumberFormatException -> L17
        return Pair.create(Integer.valueOf(r4), Integer.valueOf(r5));
    L19:
        return null;
    }

    protected int parseTvaAudioPurposeCsValue(String r5) {
        if (r5 != null) goto L6;
        return 0;
    L6:
        switch(r5.hashCode()) {
            case 49: goto L21;
            case 50: goto L18;
            case 51: goto L15;
            case 52: goto L12;
            case 53: goto L23;
            case 54: goto L9;
            default: goto L23;
        };
    L23:
        byte r1 = -1;
    L24:
        switch(r1) {
            case 0: goto L32;
            case 1: goto L30;
            case 2: goto L29;
            case 3: goto L27;
            case 4: goto L26;
            default: goto L25;
        };
    L25:
        return 0;
    L26:
        return 1;
    L27:
        return 8;
    L29:
        return 4;
    L30:
        return 2048;
    L32:
        return 512;
    L9:
        if (r5.equals("6") == false) goto L23;
        r1 = 4;
        goto L24
    L12:
        if (r5.equals("4") == false) goto L23;
        r1 = 3;
        goto L24
    L15:
        if (r5.equals(ExifInterface.GPS_MEASUREMENT_3D) == false) goto L23;
        r1 = 2;
        goto L24
    L18:
        if (r5.equals(ExifInterface.GPS_MEASUREMENT_2D) == false) goto L23;
        r1 = 1;
        goto L24
    L21:
        if (r5.equals(IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE) == false) goto L23;
        r1 = 0;
        goto L24
    }

    protected UrlTemplate parseUrlTemplate(XmlPullParser r3, String r4, UrlTemplate r5) {
        String r0 = r3.getAttributeValue(null, r4);
        if (r0 != null) goto L5;
        return r5;
    L5:
        return UrlTemplate.compile(r0);
    }

    protected UtcTimingElement parseUtcTiming(XmlPullParser r4) {
        return buildUtcTimingElement(r4.getAttributeValue(null, "schemeIdUri"), r4.getAttributeValue(null, "value"));
    }
}
