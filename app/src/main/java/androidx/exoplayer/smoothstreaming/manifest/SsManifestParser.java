package androidx.media3.exoplayer.smoothstreaming.manifest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import androidx.media3.common.C0180C;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.CodecSpecificDataUtil;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import androidx.media3.extractor.AacUtil;
import androidx.media3.extractor.mp4.PsshAtomUtil;
import androidx.media3.extractor.mp4.TrackEncryptionBox;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* JADX INFO: loaded from: classes.dex */
public class SsManifestParser implements ParsingLoadable.Parser<SsManifest> {
    private final XmlPullParserFactory xmlParserFactory;

    private static abstract class ElementParser {
        private final String baseUri;
        private final List<Pair<String, Object>> normalizedAttributes;
        private final ElementParser parent;
        private final String tag;

        public ElementParser(ElementParser r2, String r3, String r4) {
            this.parent = r2;
            this.baseUri = r3;
            this.tag = r4;
            this.normalizedAttributes = new LinkedList();
        }

        private ElementParser newChildParser(ElementParser r2, String r3, String r4) {
            if (QualityLevelParser.TAG.equals(r3) == false) goto L7;
            return new QualityLevelParser(r2, r4);
        L7:
            if (ProtectionParser.TAG.equals(r3) == false) goto L11;
            return new ProtectionParser(r2, r4);
        L11:
            if (StreamIndexParser.TAG.equals(r3) == true) goto L13;
            return null;
        L13:
            return new StreamIndexParser(r2, r4);
        }

        protected void addChild(Object r1) {
        }

        protected abstract Object build();

        protected final Object getNormalizedAttribute(String r4) {
            int r0 = 0;
        L4:
            if (r0 >= this.normalizedAttributes.size()) goto L11;
            Pair<String, Object> r1 = this.normalizedAttributes.get(r0);
            if (((String) r1.first).equals(r4) == true) goto L8;
            r0 = r0 + 1;
            goto L4
        L8:
            return r1.second;
        L11:
            if (this.parent != null) goto L14;
            return null;
        L14:
            return this.parent.getNormalizedAttribute(r4);
        }

        protected boolean handleChildInline(String r2) {
            return false;
        }

        public final Object parse(XmlPullParser r7) throws XmlPullParserException, IOException {
            boolean r0 = false;
            int r1 = 0;
        L4:
            switch(r7.getEventType()) {
                case 1: goto L30;
                case 2: goto L16;
                case 3: goto L9;
                case 4: goto L6;
                default: goto L32;
            };
        L6:
            if (r0 == false) goto L32;
            if (r1 != 0) goto L32;
            parseText(r7);
            goto L32
        L9:
            if (r0 == false) goto L32;
            if (r1 <= 0) goto L12;
            r1 = r1 - 1;
            goto L32
        L12:
            String r3 = r7.getName();
            parseEndTag(r7);
            if (handleChildInline(r3) == true) goto L32;
            return build();
        L16:
            String r4 = r7.getName();
            if (this.tag.equals(r4) == false) goto L19;
            r0 = true;
            parseStartTag(r7);
            goto L32
        L19:
            if (r0 == false) goto L32;
            if (r1 <= 0) goto L23;
            r1 = r1 + 1;
            goto L32
        L23:
            if (handleChildInline(r4) == false) goto L25;
            parseStartTag(r7);
            goto L32
        L25:
            ElementParser r5 = newChildParser(this, r4, this.baseUri);
            if (r5 != null) goto L28;
            r1 = 1;
            goto L32
        L28:
            addChild(r5.parse(r7));
            goto L32
        L30:
            return null;
        L32:
            r7.next();
            goto L4
        }

        protected final boolean parseBoolean(XmlPullParser r3, String r4, boolean r5) {
            String r0 = r3.getAttributeValue(null, r4);
            if (r0 != null) goto L5;
            return r5;
        L5:
            return Boolean.parseBoolean(r0);
        }

        protected void parseEndTag(XmlPullParser r1) {
        }

        protected final int parseInt(XmlPullParser r4, String r5, int r6) throws ParserException {
            String r1 = r4.getAttributeValue(null, r5);
            if (r1 != null) goto L10;
            return r6;
        L10:
            return Integer.parseInt(r1);
        L6:
            e = move-exception;
            throw ParserException.createForMalformedManifest(null, e);
        }

        protected final long parseLong(XmlPullParser r5, String r6, long r7) throws ParserException {
            String r1 = r5.getAttributeValue(null, r6);
            if (r1 != null) goto L10;
            return r7;
        L10:
            return Long.parseLong(r1);
        L6:
            e = move-exception;
            throw ParserException.createForMalformedManifest(null, e);
        }

        protected final int parseRequiredInt(XmlPullParser r4, String r5) throws ParserException {
            String r1 = r4.getAttributeValue(null, r5);
            if (r1 == null) goto L10;
            return Integer.parseInt(r1);
        L6:
            e = move-exception;
            throw ParserException.createForMalformedManifest(null, e);
        L10:
            throw new MissingFieldException(r5);
        }

        protected final long parseRequiredLong(XmlPullParser r5, String r6) throws ParserException {
            String r1 = r5.getAttributeValue(null, r6);
            if (r1 == null) goto L10;
            return Long.parseLong(r1);
        L6:
            e = move-exception;
            throw ParserException.createForMalformedManifest(null, e);
        L10:
            throw new MissingFieldException(r6);
        }

        protected final String parseRequiredString(XmlPullParser r3, String r4) throws MissingFieldException {
            String r0 = r3.getAttributeValue(null, r4);
            if (r0 == null) goto L6;
            return r0;
        L6:
            throw new MissingFieldException(r4);
        }

        protected void parseStartTag(XmlPullParser r1) throws ParserException {
        }

        protected void parseText(XmlPullParser r1) {
        }

        protected final void putNormalizedAttribute(String r3, Object r4) {
            this.normalizedAttributes.add(Pair.create(r3, r4));
        }
    }

    public static class MissingFieldException extends ParserException {
        public MissingFieldException(String r5) {
            super("Missing required field: " + r5, null, true, 4);
        }
    }

    private static class ProtectionParser extends ElementParser {
        private static final int INITIALIZATION_VECTOR_SIZE = 8;
        public static final String KEY_SYSTEM_ID = "SystemID";
        public static final String TAG = "Protection";
        public static final String TAG_PROTECTION_HEADER = "ProtectionHeader";
        private boolean inProtectionHeader;
        private byte[] initData;
        private UUID uuid;

        public ProtectionParser(ElementParser r2, String r3) {
            super(r2, r3, TAG);
        }

        private static TrackEncryptionBox[] buildTrackEncryptionBoxes(byte[] r9) {
            return new TrackEncryptionBox[]{new TrackEncryptionBox(true, null, 8, getProtectionElementKeyId(r9), 0, 0, null)};
        }

        private static byte[] getProtectionElementKeyId(byte[] r7) {
            StringBuilder r0 = new StringBuilder();
            int r1 = 0;
        L4:
            if (r1 >= r7.length) goto L6;
            r0.append((char) r7[r1]);
            r1 = r1 + 2;
            goto L4
        L6:
            String r2 = r0.toString();
            byte[] r5 = Base64.decode(r2.substring(r2.indexOf("<KID>") + 5, r2.indexOf("</KID>")), 0);
            swap(r5, 0, 3);
            swap(r5, 1, 2);
            swap(r5, 4, 5);
            swap(r5, 6, 7);
            return r5;
        }

        private static String stripCurlyBraces(String r3) {
            if (r3.charAt(0) == '{') goto L5;
            return r3;
        L5:
            if (r3.charAt(r3.length() - 1) == '}') goto L7;
            return r3;
        L7:
            return r3.substring(1, r3.length() - 1);
        }

        private static void swap(byte[] r2, int r3, int r4) {
            byte r0 = r2[r3];
            r2[r3] = r2[r4];
            r2[r4] = r0;
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser.ElementParser
        public Object build() {
            return new SsManifest.ProtectionElement(this.uuid, PsshAtomUtil.buildPsshAtom(this.uuid, this.initData), buildTrackEncryptionBoxes(this.initData));
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser.ElementParser
        public boolean handleChildInline(String r2) {
            return TAG_PROTECTION_HEADER.equals(r2);
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseEndTag(XmlPullParser r3) {
            if (TAG_PROTECTION_HEADER.equals(r3.getName()) == false) goto L6;
            this.inProtectionHeader = false;
            return;
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseStartTag(XmlPullParser r3) {
            if (TAG_PROTECTION_HEADER.equals(r3.getName()) == false) goto L6;
            this.inProtectionHeader = true;
            this.uuid = UUID.fromString(stripCurlyBraces(r3.getAttributeValue(null, KEY_SYSTEM_ID)));
            return;
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseText(XmlPullParser r3) {
            if (this.inProtectionHeader == false) goto L6;
            this.initData = Base64.decode(r3.getText(), 0);
            return;
        }
    }

    private static class QualityLevelParser extends ElementParser {
        private static final String KEY_BITRATE = "Bitrate";
        private static final String KEY_CHANNELS = "Channels";
        private static final String KEY_CODEC_PRIVATE_DATA = "CodecPrivateData";
        private static final String KEY_FOUR_CC = "FourCC";
        private static final String KEY_INDEX = "Index";
        private static final String KEY_LANGUAGE = "Language";
        private static final String KEY_MAX_HEIGHT = "MaxHeight";
        private static final String KEY_MAX_WIDTH = "MaxWidth";
        private static final String KEY_NAME = "Name";
        private static final String KEY_SAMPLING_RATE = "SamplingRate";
        private static final String KEY_SUB_TYPE = "Subtype";
        private static final String KEY_TYPE = "Type";
        public static final String TAG = "QualityLevel";
        private Format format;

        public QualityLevelParser(ElementParser r2, String r3) {
            super(r2, r3, TAG);
        }

        private static List<byte[]> buildCodecSpecificData(String r3) {
            ArrayList r0 = new ArrayList();
            if (TextUtils.isEmpty(r3) == true) goto L8;
            byte[] r1 = Util.getBytesFromHexString(r3);
            byte[][] r2 = CodecSpecificDataUtil.splitNalUnits(r1);
            if (r2 != null) goto L7;
            r0.add(r1);
            goto L8
        L7:
            Collections.addAll(r0, r2);
        L8:
            return r0;
        }

        private static String fourCCToMimeType(String r1) {
            if (r1.equalsIgnoreCase("H264") == false) goto L5;
            return MimeTypes.VIDEO_H264;
        L5:
            if (r1.equalsIgnoreCase("X264") == false) goto L7;
            return MimeTypes.VIDEO_H264;
        L7:
            if (r1.equalsIgnoreCase("AVC1") == false) goto L9;
            return MimeTypes.VIDEO_H264;
        L9:
            if (r1.equalsIgnoreCase("DAVC") == false) goto L12;
            return MimeTypes.VIDEO_H264;
        L12:
            if (r1.equalsIgnoreCase("AAC") == false) goto L14;
            return MimeTypes.AUDIO_AAC;
        L14:
            if (r1.equalsIgnoreCase("AACL") == false) goto L16;
            return MimeTypes.AUDIO_AAC;
        L16:
            if (r1.equalsIgnoreCase("AACH") == false) goto L18;
            return MimeTypes.AUDIO_AAC;
        L18:
            if (r1.equalsIgnoreCase("AACP") == false) goto L21;
            return MimeTypes.AUDIO_AAC;
        L21:
            if (r1.equalsIgnoreCase("TTML") == false) goto L23;
            return MimeTypes.APPLICATION_TTML;
        L23:
            if (r1.equalsIgnoreCase("DFXP") == false) goto L26;
            return MimeTypes.APPLICATION_TTML;
        L26:
            if (r1.equalsIgnoreCase("ac-3") == false) goto L28;
            return MimeTypes.AUDIO_AC3;
        L28:
            if (r1.equalsIgnoreCase("dac3") == false) goto L31;
            return MimeTypes.AUDIO_AC3;
        L31:
            if (r1.equalsIgnoreCase("ec-3") == false) goto L33;
            return MimeTypes.AUDIO_E_AC3;
        L33:
            if (r1.equalsIgnoreCase("dec3") == false) goto L36;
            return MimeTypes.AUDIO_E_AC3;
        L36:
            if (r1.equalsIgnoreCase("dtsc") == false) goto L40;
            return MimeTypes.AUDIO_DTS;
        L40:
            if (r1.equalsIgnoreCase("dtsh") == false) goto L42;
            return MimeTypes.AUDIO_DTS_HD;
        L42:
            if (r1.equalsIgnoreCase("dtsl") == false) goto L45;
            return MimeTypes.AUDIO_DTS_HD;
        L45:
            if (r1.equalsIgnoreCase("dtse") == false) goto L49;
            return MimeTypes.AUDIO_DTS_EXPRESS;
        L49:
            if (r1.equalsIgnoreCase("opus") == false) goto L52;
            return MimeTypes.AUDIO_OPUS;
        L52:
            return null;
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser.ElementParser
        public Object build() {
            return this.format;
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseStartTag(XmlPullParser r10) throws ParserException {
            Format.Builder r0 = new Format.Builder();
            String r1 = fourCCToMimeType(parseRequiredString(r10, KEY_FOUR_CC));
            int r2 = ((Integer) getNormalizedAttribute(KEY_TYPE)).intValue();
            if (r2 != 2) goto L5;
            List<byte[]> r3 = buildCodecSpecificData(r10.getAttributeValue(null, KEY_CODEC_PRIVATE_DATA));
            r0.setContainerMimeType(MimeTypes.VIDEO_MP4).setWidth(parseRequiredInt(r10, KEY_MAX_WIDTH)).setHeight(parseRequiredInt(r10, KEY_MAX_HEIGHT)).setInitializationData(r3);
        L35:
            this.format = r0.setId(r10.getAttributeValue(null, KEY_INDEX)).setLabel((String) getNormalizedAttribute(KEY_NAME)).setSampleMimeType(r1).setAverageBitrate(parseRequiredInt(r10, KEY_BITRATE)).setLanguage((String) getNormalizedAttribute(KEY_LANGUAGE)).build();
            return;
        L5:
            byte r4 = 1;
            if (r2 != 1) goto L16;
            if (r1 != null) goto L9;
            r1 = MimeTypes.AUDIO_AAC;
        L9:
            int r5 = parseRequiredInt(r10, KEY_CHANNELS);
            int r6 = parseRequiredInt(r10, KEY_SAMPLING_RATE);
            List<byte[]> r7 = buildCodecSpecificData(r10.getAttributeValue(null, KEY_CODEC_PRIVATE_DATA));
            if (r7.isEmpty() == true) goto L12;
        L14:
            r0.setContainerMimeType(MimeTypes.AUDIO_MP4).setChannelCount(r5).setSampleRate(r6).setInitializationData(r7);
            goto L35
        L12:
            if (MimeTypes.AUDIO_AAC.equals(r1) == false) goto L14;
            r7 = Collections.singletonList(AacUtil.buildAacLcAudioSpecificConfig(r6, r5));
            goto L14
        L16:
            if (r2 != 3) goto L34;
            int r8 = 0;
            String r9 = (String) getNormalizedAttribute(KEY_SUB_TYPE);
            if (r9 != null) goto L20;
        L33:
            r0.setContainerMimeType(MimeTypes.APPLICATION_MP4).setRoleFlags(r8);
            goto L35
        L20:
            switch(r9.hashCode()) {
                case 2061026: goto L26;
                case 2094737: goto L23;
                default: goto L28;
            };
        L28:
            r4 = -1;
        L29:
            switch(r4) {
                case 0: goto L32;
                case 1: goto L31;
                default: goto L33;
            };
        L31:
            r8 = 1024;
            goto L33
        L32:
            r8 = 64;
            goto L33
        L23:
            if (r9.equals("DESC") == false) goto L28;
        L26:
            if (r9.equals("CAPT") == false) goto L28;
            r4 = 0;
            goto L29
        L34:
            r0.setContainerMimeType(MimeTypes.APPLICATION_MP4);
            goto L35
        }
    }

    private static class SmoothStreamingMediaParser extends ElementParser {
        private static final String KEY_DURATION = "Duration";
        private static final String KEY_DVR_WINDOW_LENGTH = "DVRWindowLength";
        private static final String KEY_IS_LIVE = "IsLive";
        private static final String KEY_LOOKAHEAD_COUNT = "LookaheadCount";
        private static final String KEY_MAJOR_VERSION = "MajorVersion";
        private static final String KEY_MINOR_VERSION = "MinorVersion";
        private static final String KEY_TIME_SCALE = "TimeScale";
        public static final String TAG = "SmoothStreamingMedia";
        private long duration;
        private long dvrWindowLength;
        private boolean isLive;
        private int lookAheadCount;
        private int majorVersion;
        private int minorVersion;
        private SsManifest.ProtectionElement protectionElement;
        private final List<SsManifest.StreamElement> streamElements;
        private long timescale;

        public SmoothStreamingMediaParser(ElementParser r2, String r3) {
            super(r2, r3, TAG);
            this.lookAheadCount = -1;
            this.protectionElement = null;
            this.streamElements = new LinkedList();
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void addChild(Object r3) {
            if ((r3 instanceof SsManifest.StreamElement) == false) goto L6;
            this.streamElements.add((SsManifest.StreamElement) r3);
            return;
        L6:
            if ((r3 instanceof SsManifest.ProtectionElement) == true) goto L8;
            return;
        L8:
            if (this.protectionElement != null) goto L10;
            boolean r0 = true;
        L11:
            Assertions.checkState(r0);
            this.protectionElement = (SsManifest.ProtectionElement) r3;
            return;
        L10:
            r0 = false;
            goto L11
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser.ElementParser
        public Object build() {
            SsManifest.StreamElement[] r13 = new SsManifest.StreamElement[this.streamElements.size()];
            this.streamElements.toArray(r13);
            if (this.protectionElement == null) goto L15;
            int r4 = 0;
            DrmInitData r0 = new DrmInitData(new DrmInitData.SchemeData[]{new DrmInitData.SchemeData(this.protectionElement.uuid, MimeTypes.VIDEO_MP4, this.protectionElement.data)});
            int r2 = r13.length;
        L5:
            if (r4 >= r2) goto L15;
            SsManifest.StreamElement r3 = r13[r4];
            int r5 = r3.type;
            if (r5 == 2) goto L9;
            if (r5 == 1) goto L9;
        L13:
            r4 = r4 + 1;
        L9:
            Format[] r6 = r3.formats;
            int r7 = 0;
        L11:
            if (r7 >= r6.length) goto L13;
            r6[r7] = r6[r7].buildUpon().setDrmInitData(r0).build();
            r7 = r7 + 1;
        L15:
            return new SsManifest(this.majorVersion, this.minorVersion, this.timescale, this.duration, this.dvrWindowLength, this.lookAheadCount, this.isLive, this.protectionElement, r13);
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseStartTag(XmlPullParser r6) throws ParserException {
            this.majorVersion = parseRequiredInt(r6, KEY_MAJOR_VERSION);
            this.minorVersion = parseRequiredInt(r6, KEY_MINOR_VERSION);
            this.timescale = parseLong(r6, KEY_TIME_SCALE, 10000000);
            this.duration = parseRequiredLong(r6, KEY_DURATION);
            this.dvrWindowLength = parseLong(r6, KEY_DVR_WINDOW_LENGTH, 0);
            this.lookAheadCount = parseInt(r6, KEY_LOOKAHEAD_COUNT, -1);
            this.isLive = parseBoolean(r6, KEY_IS_LIVE, false);
            putNormalizedAttribute(KEY_TIME_SCALE, Long.valueOf(this.timescale));
        }
    }

    private static class StreamIndexParser extends ElementParser {
        private static final String KEY_DISPLAY_HEIGHT = "DisplayHeight";
        private static final String KEY_DISPLAY_WIDTH = "DisplayWidth";
        private static final String KEY_FRAGMENT_DURATION = "d";
        private static final String KEY_FRAGMENT_REPEAT_COUNT = "r";
        private static final String KEY_FRAGMENT_START_TIME = "t";
        private static final String KEY_LANGUAGE = "Language";
        private static final String KEY_MAX_HEIGHT = "MaxHeight";
        private static final String KEY_MAX_WIDTH = "MaxWidth";
        private static final String KEY_NAME = "Name";
        private static final String KEY_SUB_TYPE = "Subtype";
        private static final String KEY_TIME_SCALE = "TimeScale";
        private static final String KEY_TYPE = "Type";
        private static final String KEY_TYPE_AUDIO = "audio";
        private static final String KEY_TYPE_TEXT = "text";
        private static final String KEY_TYPE_VIDEO = "video";
        private static final String KEY_URL = "Url";
        public static final String TAG = "StreamIndex";
        private static final String TAG_STREAM_FRAGMENT = "c";
        private final String baseUri;
        private int displayHeight;
        private int displayWidth;
        private final List<Format> formats;
        private String language;
        private long lastChunkDuration;
        private int maxHeight;
        private int maxWidth;
        private String name;
        private ArrayList<Long> startTimes;
        private String subType;
        private long timescale;
        private int type;
        private String url;

        public StreamIndexParser(ElementParser r2, String r3) {
            super(r2, r3, TAG);
            this.baseUri = r3;
            this.formats = new LinkedList();
        }

        private void parseStreamElementStartTag(XmlPullParser r6) throws ParserException {
            this.type = parseType(r6);
            putNormalizedAttribute(KEY_TYPE, Integer.valueOf(this.type));
            if (this.type != 3) goto L5;
            this.subType = parseRequiredString(r6, KEY_SUB_TYPE);
        L6:
            putNormalizedAttribute(KEY_SUB_TYPE, this.subType);
            this.name = r6.getAttributeValue(null, KEY_NAME);
            putNormalizedAttribute(KEY_NAME, this.name);
            this.url = parseRequiredString(r6, KEY_URL);
            this.maxWidth = parseInt(r6, KEY_MAX_WIDTH, -1);
            this.maxHeight = parseInt(r6, KEY_MAX_HEIGHT, -1);
            this.displayWidth = parseInt(r6, KEY_DISPLAY_WIDTH, -1);
            this.displayHeight = parseInt(r6, KEY_DISPLAY_HEIGHT, -1);
            this.language = r6.getAttributeValue(null, KEY_LANGUAGE);
            putNormalizedAttribute(KEY_LANGUAGE, this.language);
            this.timescale = parseInt(r6, KEY_TIME_SCALE, -1);
            if (this.timescale != (-1)) goto L9;
            this.timescale = ((Long) getNormalizedAttribute(KEY_TIME_SCALE)).longValue();
        L9:
            this.startTimes = new ArrayList();
            return;
        L5:
            this.subType = r6.getAttributeValue(null, KEY_SUB_TYPE);
            goto L6
        }

        private void parseStreamFragmentStartTag(XmlPullParser r14) throws ParserException {
            int r0 = this.startTimes.size();
            long r4 = parseLong(r14, KEY_FRAGMENT_START_TIME, C0180C.TIME_UNSET);
            if (r4 != C0180C.TIME_UNSET) goto L11;
            if (r0 != 0) goto L7;
            r4 = 0;
            goto L11
        L7:
            if (this.lastChunkDuration == (-1)) goto L10;
            r4 = this.startTimes.get(r0 - 1).longValue() + this.lastChunkDuration;
            goto L11
        L10:
            throw ParserException.createForMalformedManifest("Unable to infer start time", null);
        L11:
            int r1 = r0 + 1;
            this.startTimes.add(Long.valueOf(r4));
            this.lastChunkDuration = parseLong(r14, "d", C0180C.TIME_UNSET);
            long r9 = parseLong(r14, KEY_FRAGMENT_REPEAT_COUNT, 1);
            if (r9 > 1) goto L14;
        L18:
            int r2 = 1;
        L20:
            if (r2 >= r9) goto L22;
            r1 = r1 + 1;
            this.startTimes.add(Long.valueOf((this.lastChunkDuration * ((long) r2)) + r4));
            r2 = r2 + 1;
            goto L20
        L22:
            return;
        L14:
            if (this.lastChunkDuration != C0180C.TIME_UNSET) goto L18;
            throw ParserException.createForMalformedManifest("Repeated chunk with unspecified duration", null);
        }

        private int parseType(XmlPullParser r5) throws ParserException {
            String r2 = r5.getAttributeValue(null, KEY_TYPE);
            if (r2 == null) goto L19;
            if ("audio".equalsIgnoreCase(r2) == false) goto L9;
            return 1;
        L9:
            if ("video".equalsIgnoreCase(r2) == false) goto L13;
            return 2;
        L13:
            if ("text".equalsIgnoreCase(r2) == false) goto L17;
            return 3;
        L17:
            throw ParserException.createForMalformedManifest("Invalid key value[" + r2 + "]", null);
        L19:
            throw new MissingFieldException(KEY_TYPE);
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void addChild(Object r3) {
            if ((r3 instanceof Format) == false) goto L6;
            this.formats.add((Format) r3);
            return;
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser.ElementParser
        public Object build() {
            Format[] r15 = new Format[this.formats.size()];
            this.formats.toArray(r15);
            return new SsManifest.StreamElement(this.baseUri, this.url, this.type, this.subType, this.timescale, this.name, this.maxWidth, this.maxHeight, this.displayWidth, this.displayHeight, this.language, r15, this.startTimes, this.lastChunkDuration);
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser.ElementParser
        public boolean handleChildInline(String r2) {
            return TAG_STREAM_FRAGMENT.equals(r2);
        }

        @Override // androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseStartTag(XmlPullParser r3) throws ParserException {
            if (TAG_STREAM_FRAGMENT.equals(r3.getName()) == false) goto L5;
            parseStreamFragmentStartTag(r3);
            return;
        L5:
            parseStreamElementStartTag(r3);
        }
    }

    public SsManifestParser() {
        this.xmlParserFactory = XmlPullParserFactory.newInstance();     // Catch: XmlPullParserException -> L5
        return;
    L5:
        e = move-exception;
        throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
    public SsManifest parse(Uri r5, InputStream r6) throws IOException {
        XmlPullParser r1 = this.xmlParserFactory.newPullParser();     // Catch: XmlPullParserException -> L5
        r1.setInput(r6, null);     // Catch: XmlPullParserException -> L5
        return (SsManifest) new SmoothStreamingMediaParser(null, r5.toString()).parse(r1);
    L5:
        e = move-exception;
        throw ParserException.createForMalformedManifest(null, e);
    }

    @Override // androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
    public /* bridge */ /* synthetic */ SsManifest parse(Uri r1, InputStream r2) throws IOException {
        return parse(r1, r2);
    }
}
