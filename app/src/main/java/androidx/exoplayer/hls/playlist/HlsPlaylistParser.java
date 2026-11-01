package androidx.media3.exoplayer.hls.playlist;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import androidx.media3.common.C0180C;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UriUtil;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.hls.HlsTrackMetadataEntry;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import androidx.media3.extractor.metadata.icy.IcyHeaders;
import androidx.media3.extractor.mp4.PsshAtomUtil;
import com.google.common.collect.Iterables;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;

/* JADX INFO: loaded from: classes.dex */
public final class HlsPlaylistParser implements ParsingLoadable.Parser<HlsPlaylist> {
    private static final String ATTR_CLOSED_CAPTIONS_NONE = "CLOSED-CAPTIONS=NONE";
    private static final String BOOLEAN_FALSE = "NO";
    private static final String BOOLEAN_TRUE = "YES";
    private static final String KEYFORMAT_IDENTITY = "identity";
    private static final String KEYFORMAT_PLAYREADY = "com.microsoft.playready";
    private static final String KEYFORMAT_WIDEVINE_PSSH_BINARY = "urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed";
    private static final String KEYFORMAT_WIDEVINE_PSSH_JSON = "com.widevine";
    private static final String LOG_TAG = "HlsPlaylistParser";
    private static final String METHOD_AES_128 = "AES-128";
    private static final String METHOD_NONE = "NONE";
    private static final String METHOD_SAMPLE_AES = "SAMPLE-AES";
    private static final String METHOD_SAMPLE_AES_CENC = "SAMPLE-AES-CENC";
    private static final String METHOD_SAMPLE_AES_CTR = "SAMPLE-AES-CTR";
    private static final String PLAYLIST_HEADER = "#EXTM3U";
    private static final Pattern REGEX_ATTR_BYTERANGE = null;
    private static final Pattern REGEX_ATTR_DURATION = null;
    private static final Pattern REGEX_AUDIO = null;
    private static final Pattern REGEX_AUTOSELECT = null;
    private static final Pattern REGEX_AVERAGE_BANDWIDTH = null;
    private static final Pattern REGEX_BANDWIDTH = null;
    private static final Pattern REGEX_BYTERANGE = null;
    private static final Pattern REGEX_BYTERANGE_LENGTH = null;
    private static final Pattern REGEX_BYTERANGE_START = null;
    private static final Pattern REGEX_CAN_BLOCK_RELOAD = null;
    private static final Pattern REGEX_CAN_SKIP_DATE_RANGES = null;
    private static final Pattern REGEX_CAN_SKIP_UNTIL = null;
    private static final Pattern REGEX_CHANNELS = null;
    private static final Pattern REGEX_CHARACTERISTICS = null;
    private static final Pattern REGEX_CLOSED_CAPTIONS = null;
    private static final Pattern REGEX_CODECS = null;
    private static final Pattern REGEX_DEFAULT = null;
    private static final Pattern REGEX_FORCED = null;
    private static final Pattern REGEX_FRAME_RATE = null;
    private static final Pattern REGEX_GAP = null;
    private static final Pattern REGEX_GROUP_ID = null;
    private static final Pattern REGEX_HOLD_BACK = null;
    private static final Pattern REGEX_IMPORT = null;
    private static final Pattern REGEX_INDEPENDENT = null;
    private static final Pattern REGEX_INSTREAM_ID = null;
    private static final Pattern REGEX_IV = null;
    private static final Pattern REGEX_KEYFORMAT = null;
    private static final Pattern REGEX_KEYFORMATVERSIONS = null;
    private static final Pattern REGEX_LANGUAGE = null;
    private static final Pattern REGEX_LAST_MSN = null;
    private static final Pattern REGEX_LAST_PART = null;
    private static final Pattern REGEX_MEDIA_DURATION = null;
    private static final Pattern REGEX_MEDIA_SEQUENCE = null;
    private static final Pattern REGEX_MEDIA_TITLE = null;
    private static final Pattern REGEX_METHOD = null;
    private static final Pattern REGEX_NAME = null;
    private static final Pattern REGEX_PART_HOLD_BACK = null;
    private static final Pattern REGEX_PART_TARGET_DURATION = null;
    private static final Pattern REGEX_PLAYLIST_TYPE = null;
    private static final Pattern REGEX_PRECISE = null;
    private static final Pattern REGEX_PRELOAD_HINT_TYPE = null;
    private static final Pattern REGEX_RESOLUTION = null;
    private static final Pattern REGEX_SKIPPED_SEGMENTS = null;
    private static final Pattern REGEX_SUBTITLES = null;
    private static final Pattern REGEX_TARGET_DURATION = null;
    private static final Pattern REGEX_TIME_OFFSET = null;
    private static final Pattern REGEX_TYPE = null;
    private static final Pattern REGEX_URI = null;
    private static final Pattern REGEX_VALUE = null;
    private static final Pattern REGEX_VARIABLE_REFERENCE = null;
    private static final Pattern REGEX_VERSION = null;
    private static final Pattern REGEX_VIDEO = null;
    private static final String TAG_BYTERANGE = "#EXT-X-BYTERANGE";
    private static final String TAG_DEFINE = "#EXT-X-DEFINE";
    private static final String TAG_DISCONTINUITY = "#EXT-X-DISCONTINUITY";
    private static final String TAG_DISCONTINUITY_SEQUENCE = "#EXT-X-DISCONTINUITY-SEQUENCE";
    private static final String TAG_ENDLIST = "#EXT-X-ENDLIST";
    private static final String TAG_GAP = "#EXT-X-GAP";
    private static final String TAG_IFRAME = "#EXT-X-I-FRAMES-ONLY";
    private static final String TAG_INDEPENDENT_SEGMENTS = "#EXT-X-INDEPENDENT-SEGMENTS";
    private static final String TAG_INIT_SEGMENT = "#EXT-X-MAP";
    private static final String TAG_I_FRAME_STREAM_INF = "#EXT-X-I-FRAME-STREAM-INF";
    private static final String TAG_KEY = "#EXT-X-KEY";
    private static final String TAG_MEDIA = "#EXT-X-MEDIA";
    private static final String TAG_MEDIA_DURATION = "#EXTINF";
    private static final String TAG_MEDIA_SEQUENCE = "#EXT-X-MEDIA-SEQUENCE";
    private static final String TAG_PART = "#EXT-X-PART";
    private static final String TAG_PART_INF = "#EXT-X-PART-INF";
    private static final String TAG_PLAYLIST_TYPE = "#EXT-X-PLAYLIST-TYPE";
    private static final String TAG_PREFIX = "#EXT";
    private static final String TAG_PRELOAD_HINT = "#EXT-X-PRELOAD-HINT";
    private static final String TAG_PROGRAM_DATE_TIME = "#EXT-X-PROGRAM-DATE-TIME";
    private static final String TAG_RENDITION_REPORT = "#EXT-X-RENDITION-REPORT";
    private static final String TAG_SERVER_CONTROL = "#EXT-X-SERVER-CONTROL";
    private static final String TAG_SESSION_KEY = "#EXT-X-SESSION-KEY";
    private static final String TAG_SKIP = "#EXT-X-SKIP";
    private static final String TAG_START = "#EXT-X-START";
    private static final String TAG_STREAM_INF = "#EXT-X-STREAM-INF";
    private static final String TAG_TARGET_DURATION = "#EXT-X-TARGETDURATION";
    private static final String TAG_VERSION = "#EXT-X-VERSION";
    private static final String TYPE_AUDIO = "AUDIO";
    private static final String TYPE_CLOSED_CAPTIONS = "CLOSED-CAPTIONS";
    private static final String TYPE_MAP = "MAP";
    private static final String TYPE_PART = "PART";
    private static final String TYPE_SUBTITLES = "SUBTITLES";
    private static final String TYPE_VIDEO = "VIDEO";
    private final HlsMultivariantPlaylist multivariantPlaylist;
    private final HlsMediaPlaylist previousMediaPlaylist;

    public static final class DeltaUpdateException extends IOException {
        public DeltaUpdateException() {
        }
    }

    private static class LineIterator {
        private final Queue<String> extraLines;
        private String next;
        private final BufferedReader reader;

        public LineIterator(Queue<String> r1, BufferedReader r2) {
            this.extraLines = r1;
            this.reader = r2;
        }

        @EnsuresNonNullIf(expression = {"next"}, result = true)
        public boolean hasNext() throws IOException {
            if (this.next == null) goto L6;
            return true;
        L6:
            if (this.extraLines.isEmpty() == true) goto L9;
            this.next = (String) Assertions.checkNotNull(this.extraLines.poll());
            return true;
        L9:
            String r0 = this.reader.readLine();
            this.next = r0;
            if (r0 == null) goto L14;
            this.next = this.next.trim();
            if (this.next.isEmpty() == true) goto L9;
            return true;
        L14:
            return false;
        }

        public String next() throws IOException {
            if (hasNext() == false) goto L7;
            String r0 = this.next;
            this.next = null;
            return r0;
        L7:
            throw new NoSuchElementException();
        }
    }

    static {
        REGEX_AVERAGE_BANDWIDTH = Pattern.compile("AVERAGE-BANDWIDTH=(\\d+)\\b");
        REGEX_VIDEO = Pattern.compile("VIDEO=\"(.+?)\"");
        REGEX_AUDIO = Pattern.compile("AUDIO=\"(.+?)\"");
        REGEX_SUBTITLES = Pattern.compile("SUBTITLES=\"(.+?)\"");
        REGEX_CLOSED_CAPTIONS = Pattern.compile("CLOSED-CAPTIONS=\"(.+?)\"");
        REGEX_BANDWIDTH = Pattern.compile("[^-]BANDWIDTH=(\\d+)\\b");
        REGEX_CHANNELS = Pattern.compile("CHANNELS=\"(.+?)\"");
        REGEX_CODECS = Pattern.compile("CODECS=\"(.+?)\"");
        REGEX_RESOLUTION = Pattern.compile("RESOLUTION=(\\d+x\\d+)");
        REGEX_FRAME_RATE = Pattern.compile("FRAME-RATE=([\\d\\.]+)\\b");
        REGEX_TARGET_DURATION = Pattern.compile("#EXT-X-TARGETDURATION:(\\d+)\\b");
        REGEX_ATTR_DURATION = Pattern.compile("DURATION=([\\d\\.]+)\\b");
        REGEX_PART_TARGET_DURATION = Pattern.compile("PART-TARGET=([\\d\\.]+)\\b");
        REGEX_VERSION = Pattern.compile("#EXT-X-VERSION:(\\d+)\\b");
        REGEX_PLAYLIST_TYPE = Pattern.compile("#EXT-X-PLAYLIST-TYPE:(.+)\\b");
        REGEX_CAN_SKIP_UNTIL = Pattern.compile("CAN-SKIP-UNTIL=([\\d\\.]+)\\b");
        REGEX_CAN_SKIP_DATE_RANGES = compileBooleanAttrPattern("CAN-SKIP-DATERANGES");
        REGEX_SKIPPED_SEGMENTS = Pattern.compile("SKIPPED-SEGMENTS=(\\d+)\\b");
        REGEX_HOLD_BACK = Pattern.compile("[:|,]HOLD-BACK=([\\d\\.]+)\\b");
        REGEX_PART_HOLD_BACK = Pattern.compile("PART-HOLD-BACK=([\\d\\.]+)\\b");
        REGEX_CAN_BLOCK_RELOAD = compileBooleanAttrPattern("CAN-BLOCK-RELOAD");
        REGEX_MEDIA_SEQUENCE = Pattern.compile("#EXT-X-MEDIA-SEQUENCE:(\\d+)\\b");
        REGEX_MEDIA_DURATION = Pattern.compile("#EXTINF:([\\d\\.]+)\\b");
        REGEX_MEDIA_TITLE = Pattern.compile("#EXTINF:[\\d\\.]+\\b,(.+)");
        REGEX_LAST_MSN = Pattern.compile("LAST-MSN=(\\d+)\\b");
        REGEX_LAST_PART = Pattern.compile("LAST-PART=(\\d+)\\b");
        REGEX_TIME_OFFSET = Pattern.compile("TIME-OFFSET=(-?[\\d\\.]+)\\b");
        REGEX_BYTERANGE = Pattern.compile("#EXT-X-BYTERANGE:(\\d+(?:@\\d+)?)\\b");
        REGEX_ATTR_BYTERANGE = Pattern.compile("BYTERANGE=\"(\\d+(?:@\\d+)?)\\b\"");
        REGEX_BYTERANGE_START = Pattern.compile("BYTERANGE-START=(\\d+)\\b");
        REGEX_BYTERANGE_LENGTH = Pattern.compile("BYTERANGE-LENGTH=(\\d+)\\b");
        REGEX_METHOD = Pattern.compile("METHOD=(NONE|AES-128|SAMPLE-AES|SAMPLE-AES-CENC|SAMPLE-AES-CTR)\\s*(?:,|$)");
        REGEX_KEYFORMAT = Pattern.compile("KEYFORMAT=\"(.+?)\"");
        REGEX_KEYFORMATVERSIONS = Pattern.compile("KEYFORMATVERSIONS=\"(.+?)\"");
        REGEX_URI = Pattern.compile("URI=\"(.+?)\"");
        REGEX_IV = Pattern.compile("IV=([^,.*]+)");
        REGEX_TYPE = Pattern.compile("TYPE=(AUDIO|VIDEO|SUBTITLES|CLOSED-CAPTIONS)");
        REGEX_PRELOAD_HINT_TYPE = Pattern.compile("TYPE=(PART|MAP)");
        REGEX_LANGUAGE = Pattern.compile("LANGUAGE=\"(.+?)\"");
        REGEX_NAME = Pattern.compile("NAME=\"(.+?)\"");
        REGEX_GROUP_ID = Pattern.compile("GROUP-ID=\"(.+?)\"");
        REGEX_CHARACTERISTICS = Pattern.compile("CHARACTERISTICS=\"(.+?)\"");
        REGEX_INSTREAM_ID = Pattern.compile("INSTREAM-ID=\"((?:CC|SERVICE)\\d+)\"");
        REGEX_AUTOSELECT = compileBooleanAttrPattern("AUTOSELECT");
        REGEX_DEFAULT = compileBooleanAttrPattern("DEFAULT");
        REGEX_FORCED = compileBooleanAttrPattern("FORCED");
        REGEX_INDEPENDENT = compileBooleanAttrPattern("INDEPENDENT");
        REGEX_GAP = compileBooleanAttrPattern("GAP");
        REGEX_PRECISE = compileBooleanAttrPattern("PRECISE");
        REGEX_VALUE = Pattern.compile("VALUE=\"(.+?)\"");
        REGEX_IMPORT = Pattern.compile("IMPORT=\"(.+?)\"");
        REGEX_VARIABLE_REFERENCE = Pattern.compile("\\{\\$([a-zA-Z0-9\\-_]+)\\}");
    }

    public HlsPlaylistParser() {
        this(HlsMultivariantPlaylist.EMPTY, null);
    }

    public HlsPlaylistParser(HlsMultivariantPlaylist r1, HlsMediaPlaylist r2) {
        this.multivariantPlaylist = r1;
        this.previousMediaPlaylist = r2;
    }

    private static boolean checkPlaylistHeader(BufferedReader r6) throws IOException {
        int r0 = r6.read();
        if (r0 == 239) goto L5;
    L11:
        int r1 = skipIgnorableWhitespace(r6, true, r0);
        int r3 = PLAYLIST_HEADER.length();
        int r4 = 0;
    L12:
        if (r4 >= r3) goto L18;
        if (r1 != PLAYLIST_HEADER.charAt(r4)) goto L15;
        r1 = r6.read();
        r4 = r4 + 1;
        goto L12
    L15:
        return false;
    L18:
        return Util.isLinebreak(skipIgnorableWhitespace(r6, false, r1));
    L5:
        if (r6.read() == 187) goto L7;
    L10:
        return false;
    L7:
        if (r6.read() != 191) goto L10;
        r0 = r6.read();
        goto L11
    }

    private static Pattern compileBooleanAttrPattern(String r2) {
        return Pattern.compile(r2 + "=(" + BOOLEAN_FALSE + "|" + BOOLEAN_TRUE + ")");
    }

    private static DrmInitData getPlaylistProtectionSchemes(String r4, DrmInitData.SchemeData[] r5) {
        DrmInitData.SchemeData[] r0 = new DrmInitData.SchemeData[r5.length];
        int r1 = 0;
    L4:
        if (r1 >= r5.length) goto L7;
        r0[r1] = r5[r1].copyWithData(null);
        r1 = r1 + 1;
        goto L4
    L7:
        return new DrmInitData(r4, r0);
    }

    private static String getSegmentEncryptionIV(long r1, String r3, String r4) {
        if (r3 != null) goto L5;
        return null;
    L5:
        if (r4 == null) goto L8;
        return r4;
    L8:
        return Long.toHexString(r1);
    }

    private static HlsMultivariantPlaylist.Variant getVariantWithAudioGroup(ArrayList<HlsMultivariantPlaylist.Variant> r3, String r4) {
        int r0 = 0;
    L4:
        if (r0 >= r3.size()) goto L9;
        HlsMultivariantPlaylist.Variant r1 = r3.get(r0);
        if (r4.equals(r1.audioGroupId) == true) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r1;
    L9:
        return null;
    }

    private static HlsMultivariantPlaylist.Variant getVariantWithSubtitleGroup(ArrayList<HlsMultivariantPlaylist.Variant> r3, String r4) {
        int r0 = 0;
    L4:
        if (r0 >= r3.size()) goto L9;
        HlsMultivariantPlaylist.Variant r1 = r3.get(r0);
        if (r4.equals(r1.subtitleGroupId) == true) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r1;
    L9:
        return null;
    }

    private static HlsMultivariantPlaylist.Variant getVariantWithVideoGroup(ArrayList<HlsMultivariantPlaylist.Variant> r3, String r4) {
        int r0 = 0;
    L4:
        if (r0 >= r3.size()) goto L9;
        HlsMultivariantPlaylist.Variant r1 = r3.get(r0);
        if (r4.equals(r1.videoGroupId) == true) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r1;
    L9:
        return null;
    }

    private static double parseDoubleAttr(String r2, Pattern r3) throws ParserException {
        return Double.parseDouble(parseStringAttr(r2, r3, Collections.emptyMap()));
    }

    private static DrmInitData.SchemeData parseDrmSchemeData(String r7, String r8, Map<String, String> r9) throws ParserException {
        String r0 = parseOptionalStringAttr(r7, REGEX_KEYFORMATVERSIONS, IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE, r9);
        if (KEYFORMAT_WIDEVINE_PSSH_BINARY.equals(r8) == false) goto L7;
        String r1 = parseStringAttr(r7, REGEX_URI, r9);
        return new DrmInitData.SchemeData(C0180C.WIDEVINE_UUID, MimeTypes.VIDEO_MP4, Base64.decode(r1.substring(r1.indexOf(44)), 0));
    L7:
        if (KEYFORMAT_WIDEVINE_PSSH_JSON.equals(r8) == false) goto L11;
        return new DrmInitData.SchemeData(C0180C.WIDEVINE_UUID, "hls", Util.getUtf8Bytes(r7));
    L11:
        if (KEYFORMAT_PLAYREADY.equals(r8) == true) goto L13;
        return null;
    L13:
        if (IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE.equals(r0) == false) goto L18;
        String r2 = parseStringAttr(r7, REGEX_URI, r9);
        byte[] r3 = Base64.decode(r2.substring(r2.indexOf(44)), 0);
        byte[] r4 = PsshAtomUtil.buildPsshAtom(C0180C.PLAYREADY_UUID, r3);
        return new DrmInitData.SchemeData(C0180C.PLAYREADY_UUID, MimeTypes.VIDEO_MP4, r4);
    L18:
        return null;
    }

    private static String parseEncryptionScheme(String r1) {
        if (METHOD_SAMPLE_AES_CENC.equals(r1) == false) goto L5;
    L8:
        return C0180C.CENC_TYPE_cenc;
    L5:
        if (METHOD_SAMPLE_AES_CTR.equals(r1) == true) goto L8;
        return C0180C.CENC_TYPE_cbcs;
    }

    private static int parseIntAttr(String r1, Pattern r2) throws ParserException {
        return Integer.parseInt(parseStringAttr(r1, r2, Collections.emptyMap()));
    }

    private static long parseLongAttr(String r2, Pattern r3) throws ParserException {
        return Long.parseLong(parseStringAttr(r2, r3, Collections.emptyMap()));
    }

    private static HlsMediaPlaylist parseMediaPlaylist(HlsMultivariantPlaylist r84, HlsMediaPlaylist r85, LineIterator r86, String r87) throws IOException {
        HlsMultivariantPlaylist r0 = r84;
        HlsMediaPlaylist r1 = r85;
        int r2 = 0;
        boolean r12 = r0.hasIndependentSegments;
        HashMap r15 = new HashMap();
        HashMap r17 = new HashMap();
        ArrayList r40 = new ArrayList();
        ArrayList r16 = new ArrayList();
        ArrayList r44 = new ArrayList();
        ArrayList r19 = new ArrayList();
        long r20 = 0;
        HlsMediaPlaylist.ServerControl r45 = new HlsMediaPlaylist.ServerControl(C0180C.TIME_UNSET, false, C0180C.TIME_UNSET, C0180C.TIME_UNSET, false);
        DrmInitData r46 = null;
        TreeMap r50 = new TreeMap();
        DrmInitData r51 = null;
        HlsMediaPlaylist.Segment r53 = null;
        int r56 = 0;
        long r25 = 0;
        long r57 = 0;
        boolean r14 = false;
        boolean r70 = false;
        String r60 = null;
        String r66 = null;
        boolean r36 = r12;
        boolean r27 = false;
        int r31 = 1;
        String r7 = null;
        boolean r37 = false;
        long r38 = 0;
        long r13 = 0;
        int r28 = 0;
        boolean r24 = false;
        long r29 = 0;
        long r5 = 0;
        HlsMediaPlaylist.ServerControl r42 = r45;
        ArrayList r3 = r16;
        HlsMediaPlaylist.Part r4 = null;
        String r18 = "";
        long r22 = -9223372036854775807L;
        long r32 = -9223372036854775807L;
        long r34 = -9223372036854775807L;
        long r8 = 0;
        long r10 = -1;
    L3:
        int r41 = r2;
        ArrayList r71 = r3;
        if (r86.hasNext() == false) goto L241;
        String r6 = r86.next();
        if (r6.startsWith(TAG_PREFIX) == false) goto L8;
        ArrayList r9 = r19;
        r9.add(r6);
    L9:
        r19 = r9;
        if (r6.startsWith(TAG_PLAYLIST_TYPE) == true) goto L11;
        long r74 = r8;
        if (r6.equals(TAG_IFRAME) == true) goto L21;
        if (r6.startsWith(TAG_START) == true) goto L24;
        if (r6.startsWith(TAG_SERVER_CONTROL) == true) goto L27;
        if (r6.startsWith(TAG_PART_INF) == true) goto L30;
        if (r6.startsWith(TAG_INIT_SEGMENT) == true) goto L33;
        String r11 = r60;
        String r21 = r66;
        long r76 = r10;
        if (r6.startsWith(TAG_TARGET_DURATION) == true) goto L55;
        if (r6.startsWith(TAG_MEDIA_SEQUENCE) == true) goto L58;
        if (r6.startsWith(TAG_VERSION) == true) goto L61;
        if (r6.startsWith(TAG_DEFINE) == true) goto L64;
        if (r6.startsWith(TAG_MEDIA_DURATION) == false) goto L76;
        r20 = parseTimeSecondsToUs(r6, REGEX_MEDIA_DURATION);
        r18 = parseOptionalStringAttr(r6, REGEX_MEDIA_TITLE, "", r15);
        r60 = r11;
        r66 = r21;
        r2 = r41;
        r3 = r71;
        r8 = r74;
        r10 = r76;
        goto L3
    L76:
        if (r6.startsWith(TAG_SKIP) == false) goto L106;
        int r23 = parseIntAttr(r6, REGEX_SKIPPED_SEGMENTS);
        if (r1 != null) goto L80;
    L82:
        boolean r47 = false;
    L83:
        Assertions.checkState(r47);
        int r110 = (int) (r29 - ((HlsMediaPlaylist) Util.castNonNull(r1)).mediaSequence);
        int r111 = r110 + r23;
        if (r110 < 0) goto L105;
        if (r111 > r1.segments.size()) goto L105;
        int r26 = r110;
        long r80 = r38;
        long r39 = r5;
        long r30 = r80;
        r66 = r21;
    L88:
        if (r26 >= r111) goto L103;
        HlsMediaPlaylist.Segment r33 = r1.segments.get(r26);
        int r112 = r110;
        int r43 = r111;
        if (r29 == r1.mediaSequence) goto L92;
        r33 = r33.copyWith(r30, (r1.discontinuitySequence - r28) + r33.relativeDiscontinuitySequence);
    L92:
        ArrayList r113 = r40;
        r113.add(r33);
        int r114 = r26;
        r30 = r30 + r33.durationUs;
        r57 = r30;
        if (r33.byteRangeLength == (-1)) goto L95;
        r74 = r33.byteRangeOffset + r33.byteRangeLength;
    L95:
        int r35 = r33.relativeDiscontinuitySequence;
        HlsMediaPlaylist.Segment r48 = r33.initializationSegment;
        DrmInitData r49 = r33.drmInitData;
        r11 = r33.fullSegmentEncryptionKeyUri;
        if (r33.encryptionIV == null) goto L100;
        HlsMediaPlaylist.Segment r410 = r48;
        if (r33.encryptionIV.equals(Long.toHexString(r39)) == true) goto L102;
    L101:
        r66 = r33.encryptionIV;
    L102:
        r39 = r39 + 1;
        r26 = r114 + 1;
        r1 = r85;
        r56 = r35;
        r111 = r43;
        r51 = r49;
        r53 = r410;
        r40 = r113;
        r110 = r112;
        goto L88
    L100:
        r410 = r48;
        goto L101
    L103:
        long r52 = r39;
        r38 = r30;
        r5 = r52;
        r0 = r84;
        r1 = r85;
        r60 = r11;
        r2 = r41;
        r3 = r71;
        r8 = r74;
        r10 = r76;
    L105:
        throw new DeltaUpdateException();
    L80:
        if (r40.isEmpty() == false) goto L82;
        r47 = true;
        goto L83
    L106:
        ArrayList r115 = r40;
        if (r6.startsWith(TAG_KEY) == false) goto L124;
        String r54 = parseStringAttr(r6, REGEX_METHOD, r15);
        String r55 = parseOptionalStringAttr(r6, REGEX_KEYFORMAT, KEYFORMAT_IDENTITY, r15);
        if (METHOD_NONE.equals(r54) == false) goto L111;
        r50.clear();
        r60 = null;
        r51 = null;
        r66 = null;
        TreeMap r58 = r50;
    L123:
        r1 = r85;
        r50 = r58;
        r40 = r115;
        r2 = r41;
        r3 = r71;
        r8 = r74;
        r10 = r76;
        r0 = r84;
        goto L3
    L111:
        String r59 = parseOptionalStringAttr(r6, REGEX_IV, r15);
        if (KEYFORMAT_IDENTITY.equals(r55) == true) goto L114;
        if (r7 != null) goto L119;
        r7 = parseEncryptionScheme(r54);
    L119:
        DrmInitData.SchemeData r61 = parseDrmSchemeData(r6, r55, r15);
        if (r61 == null) goto L122;
        r58 = r50;
        r58.put(r55, r61);
        r60 = null;
        r66 = r59;
        r51 = null;
        goto L123
    L122:
        r58 = r50;
        r60 = null;
        r66 = r59;
        goto L123
    L114:
        if (METHOD_AES_128.equals(r54) == false) goto L116;
        r60 = parseStringAttr(r6, REGEX_URI, r15);
        r66 = r59;
        r58 = r50;
        goto L123
    L116:
        r60 = null;
        r66 = r59;
        r58 = r50;
        goto L123
    L124:
        TreeMap r62 = r50;
        if (r6.startsWith(TAG_BYTERANGE) == false) goto L131;
        String[] r63 = Util.split(parseStringAttr(r6, REGEX_BYTERANGE, r15), "@");
        long r411 = Long.parseLong(r63[0]);
        if (r63.length <= 1) goto L129;
        long r510 = Long.parseLong(r63[1]);
    L130:
        r0 = r84;
        r1 = r85;
        r60 = r11;
        r66 = r21;
        r40 = r115;
        r2 = r41;
        r10 = r411;
        r50 = r62;
        r8 = r510;
        r3 = r71;
        goto L3
    L129:
        r510 = r74;
        goto L130
    L131:
        TreeMap r412 = r62;
        if (r6.startsWith(TAG_DISCONTINUITY_SEQUENCE) == false) goto L135;
        r27 = true;
        r28 = Integer.parseInt(r6.substring(r6.indexOf(58) + 1));
        r0 = r84;
        r1 = r85;
        r60 = r11;
        r66 = r21;
        r40 = r115;
        r2 = r41;
        r50 = r412;
        r3 = r71;
        r8 = r74;
        r10 = r76;
        goto L3
    L135:
        if (r6.equals(TAG_DISCONTINUITY) == false) goto L138;
        r56 = r56 + 1;
        r0 = r84;
        r1 = r85;
        r60 = r11;
        r66 = r21;
        r40 = r115;
        r2 = r41;
        r50 = r412;
        r3 = r71;
        r8 = r74;
        r10 = r76;
        goto L3
    L138:
        if (r6.startsWith(TAG_PROGRAM_DATE_TIME) == true) goto L140;
        if (r6.equals(TAG_GAP) == false) goto L147;
        r70 = true;
        r0 = r84;
        r1 = r85;
        r60 = r11;
        r66 = r21;
        r40 = r115;
        r2 = r41;
        r50 = r412;
        r3 = r71;
        r8 = r74;
        r10 = r76;
        goto L3
    L147:
        if (r6.equals(TAG_INDEPENDENT_SEGMENTS) == false) goto L150;
        r36 = true;
        r0 = r84;
        r1 = r85;
        r60 = r11;
        r66 = r21;
        r40 = r115;
        r2 = r41;
        r50 = r412;
        r3 = r71;
        r8 = r74;
        r10 = r76;
        goto L3
    L150:
        if (r6.equals(TAG_ENDLIST) == false) goto L153;
        r37 = true;
        r0 = r84;
        r1 = r85;
        r60 = r11;
        r66 = r21;
        r40 = r115;
        r2 = r41;
        r50 = r412;
        r3 = r71;
        r8 = r74;
        r10 = r76;
        goto L3
    L153:
        if (r6.startsWith(TAG_RENDITION_REPORT) == true) goto L154;
        long r78 = r13;
        ArrayList r64 = r44;
        if (r6.startsWith(TAG_PRELOAD_HINT) == true) goto L157;
        if (r6.startsWith(TAG_PART) == false) goto L213;
        String r65 = getSegmentEncryptionIV(r5, r11, r21);
        String r511 = parseStringAttr(r6, REGEX_URI, r15);
        long r116 = (long) (parseDoubleAttr(r6, REGEX_ATTR_DURATION) * 1000000.0d);
        boolean r67 = parseOptionalBooleanAttribute(r6, REGEX_INDEPENDENT, false);
        if (r36 == true) goto L186;
    L188:
        boolean r117 = false;
    L189:
        boolean r68 = r67 | r117;
        boolean r69 = parseOptionalBooleanAttribute(r6, REGEX_GAP, false);
        String r72 = parseOptionalStringAttr(r6, REGEX_ATTR_BYTERANGE, r15);
        if (r72 == null) goto L195;
        String[] r73 = Util.split(r72, "@");
        long r413 = Long.parseLong(r73[0]);
        if (r73.length <= 1) goto L194;
        long r610 = r413;
        r78 = Long.parseLong(r73[1]);
    L197:
        if (r610 != (-1)) goto L199;
        long r611 = 0;
    L200:
        if (r51 == null) goto L202;
    L207:
        DrmInitData r512 = r51;
    L208:
        r71.add(new HlsMediaPlaylist.Part(r511, r53, r116, r56, r57, r512, r11, r65, r611, r610, r69, r68, false));
        r57 = r57 + r116;
        if (r610 == (-1)) goto L212;
        r611 = r611 + r610;
    L212:
        r13 = r611;
        r1 = r85;
        r44 = r64;
        r60 = r11;
        r66 = r21;
        r40 = r115;
        r3 = r71;
        r2 = r41;
        r50 = r412;
        r51 = r512;
        r8 = r74;
        r10 = r76;
        r0 = r84;
        goto L3
    L202:
        if (r412.isEmpty() == true) goto L207;
        DrmInitData.SchemeData[] r75 = (DrmInitData.SchemeData[]) r412.values().toArray(new DrmInitData.SchemeData[0]);
        DrmInitData r77 = new DrmInitData(r7, r75);
        if (r46 != null) goto L206;
        r46 = getPlaylistProtectionSchemes(r7, r75);
        r512 = r77;
        goto L208
    L206:
        r512 = r77;
        goto L208
    L199:
        r611 = r78;
        goto L200
    L194:
        r610 = r413;
        goto L197
    L195:
        r610 = -1;
        goto L197
    L186:
        if (r71.isEmpty() == false) goto L188;
        r117 = true;
        goto L189
    L213:
        long r79 = r57;
        ArrayList r118 = r71;
        if (r6.startsWith("#") == true) goto L239;
        String r612 = getSegmentEncryptionIV(r5, r11, r21);
        r5 = r5 + 1;
        String r119 = replaceVariableReferences(r6, r15);
        HashMap r120 = r17;
        HlsMediaPlaylist.Segment r121 = (HlsMediaPlaylist.Segment) r120.get(r119);
        if (r76 != (-1)) goto L218;
        long r613 = 0;
    L223:
        if (r51 == null) goto L225;
    L230:
        DrmInitData r614 = r51;
    L231:
        int r615 = r56;
        if (r53 == null) goto L234;
        HlsMediaPlaylist.Segment r513 = r53;
    L235:
        long r514 = r20;
        long r616 = r38;
        HlsMediaPlaylist.Segment r515 = new HlsMediaPlaylist.Segment(r119, r513, r18, r514, r615, r616, r614, r11, r612, r613, r76, r70, r118);
        r56 = r615;
        r60 = r11;
        r115.add(r515);
        r38 = r616 + r514;
        r20 = 0;
        r18 = "";
        ArrayList r81 = new ArrayList();
        if (r76 == (-1)) goto L238;
        r613 = r613 + r76;
    L238:
        r70 = false;
        r57 = r38;
        r3 = r81;
        r40 = r115;
        r17 = r120;
        r2 = r41;
        r10 = -1;
        r50 = r412;
        r51 = r614;
        r13 = r78;
        r1 = r85;
        r44 = r64;
        r0 = r84;
        long r82 = r613;
        r66 = r21;
        r8 = r82;
        goto L3
    L234:
        r513 = r121;
        goto L235
    L225:
        if (r412.isEmpty() == true) goto L230;
        DrmInitData.SchemeData[] r83 = (DrmInitData.SchemeData[]) r412.values().toArray(new DrmInitData.SchemeData[0]);
        DrmInitData r88 = new DrmInitData(r7, r83);
        if (r46 != null) goto L229;
        r46 = getPlaylistProtectionSchemes(r7, r83);
        r614 = r88;
        goto L231
    L229:
        r614 = r88;
        goto L231
    L218:
        if (r14 == false) goto L222;
        if (r53 != null) goto L222;
        if (r121 != null) goto L222;
        r121 = new HlsMediaPlaylist.Segment(r119, 0, r74, null, null);
        r120.put(r119, r121);
        r613 = r74;
    L222:
        r613 = r74;
        goto L223
    L239:
        r60 = r11;
        long r710 = r79;
    L240:
        r1 = r85;
        r66 = r21;
        r40 = r115;
        r3 = r118;
        r17 = r17;
        r2 = r41;
        r38 = r38;
        r50 = r412;
        r18 = r18;
        r20 = r20;
        r8 = r74;
        r57 = r710;
        r10 = r76;
        r13 = r78;
        r44 = r64;
        r0 = r84;
        goto L3
    L157:
        if (r4 != null) goto L158;
        if (TYPE_PART.equals(parseStringAttr(r6, REGEX_PRELOAD_HINT_TYPE, r15)) == false) goto L161;
        String r516 = parseStringAttr(r6, REGEX_URI, r15);
        long r414 = parseOptionalLongAttr(r6, REGEX_BYTERANGE_START, -1);
        long r617 = parseOptionalLongAttr(r6, REGEX_BYTERANGE_LENGTH, -1);
        String r618 = getSegmentEncryptionIV(r5, r11, r21);
        if (r51 == null) goto L165;
    L170:
        DrmInitData r517 = r51;
    L172:
        if (r414 == (-1)) goto L176;
        if (r617 != (-1)) goto L176;
    L180:
        r1 = r85;
        r44 = r64;
        r60 = r11;
        r66 = r21;
        r40 = r115;
        r2 = r41;
        r50 = r412;
        r51 = r517;
        r3 = r71;
        r8 = r74;
        r10 = r76;
        r13 = r78;
        r0 = r84;
    L176:
        if (r414 == (-1)) goto L178;
        long r619 = r414;
    L179:
        r4 = new HlsMediaPlaylist.Part(r516, r53, 0, r56, r57, r517, r11, r618, r619, r617, false, false, true);
        goto L180
    L178:
        r619 = 0;
        goto L179
    L165:
        if (r412.isEmpty() == true) goto L170;
        DrmInitData.SchemeData[] r89 = (DrmInitData.SchemeData[]) r412.values().toArray(new DrmInitData.SchemeData[0]);
        DrmInitData r122 = new DrmInitData(r7, r89);
        if (r46 != null) goto L169;
        r46 = getPlaylistProtectionSchemes(r7, r89);
        r517 = r122;
        goto L172
    L169:
        r517 = r122;
        goto L172
    L161:
        r60 = r11;
        r118 = r71;
        r710 = r57;
        goto L240
    L158:
        r60 = r11;
        r118 = r71;
        r710 = r57;
        goto L240
    L154:
        r78 = r13;
        HlsMediaPlaylist.RenditionReport r123 = new HlsMediaPlaylist.RenditionReport(Uri.parse(UriUtil.resolve(r87, parseStringAttr(r6, REGEX_URI, r15))), parseOptionalLongAttr(r6, REGEX_LAST_MSN, -1), parseOptionalIntAttr(r6, REGEX_LAST_PART, -1));
        r64 = r44;
        r64.add(r123);
        r60 = r11;
        r118 = r71;
        r710 = r57;
        goto L240
    L140:
        if (r25 != 0) goto L142;
        r25 = Util.msToUs(Util.parseXsDateTime(r6.substring(r6.indexOf(58) + 1))) - r38;
        r0 = r84;
        r1 = r85;
        r60 = r11;
        r66 = r21;
        r40 = r115;
        r2 = r41;
        r50 = r412;
        r3 = r71;
        r8 = r74;
        r10 = r76;
        goto L3
    L142:
        r60 = r11;
        r78 = r13;
        r64 = r44;
        r118 = r71;
        r710 = r57;
        goto L240
    L64:
        String r90 = parseOptionalStringAttr(r6, REGEX_IMPORT, r15);
        if (r90 == null) goto L70;
        String r124 = r0.variableDefinitions.get(r90);
        if (r124 == null) goto L71;
        r15.put(r90, r124);
    L71:
        r60 = r11;
        r78 = r13;
        r115 = r40;
        r64 = r44;
        r412 = r50;
        r118 = r71;
        r710 = r57;
        goto L240
    L70:
        r15.put(parseStringAttr(r6, REGEX_NAME, r15), parseStringAttr(r6, REGEX_VALUE, r15));
        goto L71
    L61:
        r31 = parseIntAttr(r6, REGEX_VERSION);
        r60 = r11;
        r66 = r21;
        r2 = r41;
        r3 = r71;
        r8 = r74;
        r10 = r76;
        goto L3
    L58:
        r29 = parseLongAttr(r6, REGEX_MEDIA_SEQUENCE);
        r5 = r29;
        r60 = r11;
        r66 = r21;
        r2 = r41;
        r3 = r71;
        r8 = r74;
        r10 = r76;
        goto L3
    L55:
        r32 = ((long) parseIntAttr(r6, REGEX_TARGET_DURATION)) * 1000000;
        r60 = r11;
        r66 = r21;
        r2 = r41;
        r3 = r71;
        r8 = r74;
        r10 = r76;
        goto L3
    L33:
        String r91 = parseStringAttr(r6, REGEX_URI, r15);
        String r92 = parseOptionalStringAttr(r6, REGEX_ATTR_BYTERANGE, r15);
        if (r92 == null) goto L39;
        String[] r93 = Util.split(r92, "@");
        long r125 = Long.parseLong(r93[0]);
        String r415 = r91;
        if (r93.length <= 1) goto L38;
        long r620 = r125;
        r74 = Long.parseLong(r93[1]);
    L41:
        if (r620 != (-1)) goto L43;
        long r621 = 0;
    L44:
        if (r60 == null) goto L49;
        if (r66 != null) goto L49;
        throw ParserException.createForMalformedManifest("The encryption IV attribute must be present when an initialization segment is encrypted with METHOD=AES-128.", null);
    L49:
        String r622 = r60;
        r53 = new HlsMediaPlaylist.Segment(r415, r621, r620, r622, r66);
        if (r620 == (-1)) goto L52;
        r621 = r621 + r620;
    L52:
        r10 = -1;
        r60 = r622;
        r2 = r41;
        r8 = r621;
        r3 = r71;
        goto L3
    L43:
        r621 = r74;
        goto L44
    L38:
        r620 = r125;
        goto L41
    L39:
        r415 = r91;
        r620 = r10;
        goto L41
    L30:
        r34 = (long) (1000000.0d * parseDoubleAttr(r6, REGEX_PART_TARGET_DURATION));
        r2 = r41;
        r3 = r71;
        r8 = r74;
        goto L3
    L27:
        r42 = parseServerControl(r6);
        r2 = r41;
        r3 = r71;
        r8 = r74;
        goto L3
    L24:
        r22 = (long) (1000000.0d * parseDoubleAttr(r6, REGEX_TIME_OFFSET));
        r24 = parseOptionalBooleanAttribute(r6, REGEX_PRECISE, false);
        r2 = r41;
        r3 = r71;
        r8 = r74;
        goto L3
    L21:
        r14 = true;
        r2 = r41;
        r3 = r71;
        r8 = r74;
        goto L3
    L11:
        String r94 = parseStringAttr(r6, REGEX_PLAYLIST_TYPE, r15);
        long r711 = r8;
        if ("VOD".equals(r94) == false) goto L15;
        r2 = 1;
    L18:
        r3 = r71;
        r8 = r711;
        goto L3
    L15:
        if ("EVENT".equals(r94) == false) goto L17;
        r2 = 2;
        goto L18
    L17:
        r2 = r41;
        goto L18
    L8:
        r9 = r19;
        goto L9
    L241:
        ArrayList r126 = r40;
        ArrayList r95 = r44;
        HashMap r96 = new HashMap();
        int r97 = 0;
    L243:
        if (r97 >= r95.size()) goto L260;
        HlsMediaPlaylist.RenditionReport r98 = (HlsMediaPlaylist.RenditionReport) r95.get(r97);
        int r127 = r97;
        long r99 = r98.lastMediaSequence;
        if (r99 != (-1)) goto L248;
        r99 = (r29 + ((long) r126.size())) - (r71.isEmpty() ? 1 : 0);
    L248:
        int r128 = r98.lastPartIndex;
        ArrayList r518 = r95;
        if (r128 != (-1)) goto L258;
        if (r34 == C0180C.TIME_UNSET) goto L258;
        if (r71.isEmpty() == false) goto L255;
        List<HlsMediaPlaylist.Part> r100 = ((HlsMediaPlaylist.Segment) Iterables.getLast(r126)).parts;
    L256:
        r128 = r100.size() - 1;
        goto L258
    L255:
        r100 = r71;
    L258:
        r96.put(r98.playlistUri, new HlsMediaPlaylist.RenditionReport(r98.playlistUri, r99, r128));
        r97 = r127 + 1;
        r95 = r518;
        r5 = r5;
        goto L243
    L260:
        if (r4 == null) goto L263;
        r71.add(r4);
    L263:
        if (r25 == 0) goto L265;
        boolean r310 = true;
    L267:
        return new HlsMediaPlaylist(r41, r87, r19, r22, r24, r25, r27, r28, r29, r31, r32, r34, r36, r37, r310, r46, r126, r71, r42, r96);
    L265:
        r310 = false;
        goto L267
    }

    private static HlsMultivariantPlaylist parseMultivariantPlaylist(LineIterator r37, String r38) throws IOException {
        String r1 = r38;
        HashMap r0 = new HashMap();
        HashMap r11 = new HashMap();
        ArrayList r2 = new ArrayList();
        ArrayList r4 = new ArrayList();
        ArrayList r5 = new ArrayList();
        ArrayList r6 = new ArrayList();
        ArrayList r7 = new ArrayList();
        ArrayList r3 = new ArrayList();
        ArrayList r12 = new ArrayList();
        ArrayList r8 = new ArrayList();
        Format r9 = null;
        List r10 = null;
        boolean r16 = false;
        boolean r13 = false;
    L3:
        boolean r14 = r37.hasNext();
        String r15 = MimeTypes.APPLICATION_M3U8;
        Format r18 = r9;
        if (r14 == false) goto L59;
        String r17 = r37.next();
        if (r17.startsWith(TAG_PREFIX) == false) goto L8;
        r8.add(r17);
    L8:
        boolean r19 = r17.startsWith(TAG_I_FRAME_STREAM_INF);
        if (r17.startsWith(TAG_DEFINE) == false) goto L12;
        r11.put(parseStringAttr(r17, REGEX_NAME, r11), parseStringAttr(r17, REGEX_VALUE, r11));
        ArrayList r23 = r8;
        List r24 = r10;
    L28:
        r12 = r12;
    L56:
        r9 = r18;
        r8 = r23;
        r10 = r24;
        r7 = r7;
        r12 = r12;
        r4 = r4;
        r5 = r5;
        goto L3
    L12:
        if (r17.equals(TAG_INDEPENDENT_SEGMENTS) == false) goto L15;
        r13 = true;
        r23 = r8;
        r24 = r10;
        r12 = r12;
        goto L56
    L15:
        if (r17.startsWith(TAG_MEDIA) == false) goto L18;
        r3.add(r17);
        r23 = r8;
        r24 = r10;
        goto L28
    L18:
        if (r17.startsWith(TAG_SESSION_KEY) == false) goto L23;
        DrmInitData.SchemeData r20 = parseDrmSchemeData(r17, parseOptionalStringAttr(r17, REGEX_KEYFORMAT, KEYFORMAT_IDENTITY, r11), r11);
        if (r20 == null) goto L22;
        r23 = r8;
        r24 = r10;
        r12.add(new DrmInitData(parseEncryptionScheme(parseStringAttr(r17, REGEX_METHOD, r11)), new DrmInitData.SchemeData[]{r20}));
        goto L28
    L22:
        r23 = r8;
        r24 = r10;
        goto L28
    L23:
        r23 = r8;
        r24 = r10;
        if (r17.startsWith(TAG_STREAM_INF) == true) goto L29;
        if (r19 == false) goto L28;
    L29:
        boolean r21 = r16 | r17.contains(ATTR_CLOSED_CAPTIONS_NONE);
        if (r19 == false) goto L32;
        int r22 = 16384;
    L33:
        int r25 = parseIntAttr(r17, REGEX_BANDWIDTH);
        r16 = r21;
        int r26 = parseOptionalIntAttr(r17, REGEX_AVERAGE_BANDWIDTH, -1);
        String r27 = parseOptionalStringAttr(r17, REGEX_CODECS, r11);
        boolean r110 = r13;
        String r111 = parseOptionalStringAttr(r17, REGEX_RESOLUTION, r11);
        if (r111 == null) goto L40;
        String[] r112 = Util.split(r111, "x");
        int r113 = Integer.parseInt(r112[0]);
        int r28 = Integer.parseInt(r112[1]);
        if (r113 <= 0) goto L38;
        if (r28 <= 0) goto L38;
    L39:
        int r114 = r28;
        int r115 = r113;
    L41:
        String r29 = parseOptionalStringAttr(r17, REGEX_FRAME_RATE, r11);
        if (r29 == null) goto L44;
        float r30 = Float.parseFloat(r29);
    L45:
        String r210 = parseOptionalStringAttr(r17, REGEX_VIDEO, r11);
        String r211 = parseOptionalStringAttr(r17, REGEX_AUDIO, r11);
        String r31 = parseOptionalStringAttr(r17, REGEX_SUBTITLES, r11);
        String r32 = parseOptionalStringAttr(r17, REGEX_CLOSED_CAPTIONS, r11);
        if (r19 == false) goto L49;
        Uri r212 = UriUtil.resolveToUri(r1, parseStringAttr(r17, REGEX_URI, r11));
    L51:
        Uri r33 = r212;
        r2.add(new HlsMultivariantPlaylist.Variant(r212, new Format.Builder().setId(r2.size()).setContainerMimeType(MimeTypes.APPLICATION_M3U8).setCodecs(r27).setAverageBitrate(r26).setPeakBitrate(r25).setWidth(r115).setHeight(r114).setFrameRate(r30).setRoleFlags(r22).build(), r210, r211, r31, r32));
        ArrayList r213 = (ArrayList) r0.get(r33);
        if (r213 != null) goto L54;
        ArrayList r34 = new ArrayList();
        r0.put(r33, r34);
    L55:
        r34.add(new HlsTrackMetadataEntry.VariantInfo(r26, r25, r210, r211, r31, r32));
        r13 = r110;
        goto L56
    L54:
        r34 = r213;
        goto L55
    L49:
        if (r37.hasNext() == false) goto L58;
        String r35 = replaceVariableReferences(r37.next(), r11);
        r212 = UriUtil.resolveToUri(r1, r35);
        r17 = r35;
        goto L51
    L58:
        throw ParserException.createForMalformedManifest("#EXT-X-STREAM-INF must be followed by another line", null);
    L44:
        r30 = -1.0f;
    L38:
        r113 = -1;
        r28 = -1;
        goto L39
    L40:
        r114 = -1;
        r115 = -1;
        goto L41
    L32:
        r22 = 0;
        goto L33
    L59:
        ArrayList r214 = r8;
        List r215 = r10;
        ArrayList r36 = r4;
        ArrayList r39 = r5;
        ArrayList r310 = r7;
        boolean r116 = r13;
        ArrayList r311 = r12;
        ArrayList r40 = new ArrayList();
        HashSet r41 = new HashSet();
        int r42 = 0;
    L61:
        if (r42 >= r2.size()) goto L71;
        HlsMultivariantPlaylist.Variant r43 = (HlsMultivariantPlaylist.Variant) r2.get(r42);
        if (r41.add(r43.url) == false) goto L70;
        if (r43.format.metadata != null) goto L67;
        boolean r44 = true;
    L68:
        Assertions.checkState(r44);
        r40.add(r43.copyWithFormat(r43.format.buildUpon().setMetadata(new Metadata(new Metadata.Entry[]{new HlsTrackMetadataEntry(null, null, (List) Assertions.checkNotNull((ArrayList) r0.get(r43.url)))})).build()));
        goto L70
    L67:
        r44 = false;
    L70:
        r42 = r42 + 1;
        goto L61
    L71:
        int r45 = 0;
        Format r46 = r18;
    L73:
        if (r45 >= r3.size()) goto L141;
        String r47 = (String) r3.get(r45);
        String r48 = parseStringAttr(r47, REGEX_GROUP_ID, r11);
        String r49 = parseStringAttr(r47, REGEX_NAME, r11);
        HashSet r216 = r41;
        Format.Builder r50 = new Format.Builder().setId(r48 + ":" + r49).setLabel(r49).setContainerMimeType(r15).setSelectionFlags(parseSelectionFlags(r47)).setRoleFlags(parseRoleFlags(r47, r11)).setLanguage(parseOptionalStringAttr(r47, REGEX_LANGUAGE, r11));
        String r117 = parseOptionalStringAttr(r47, REGEX_URI, r11);
        if (r117 != null) goto L77;
        Uri r118 = null;
    L78:
        int r217 = r45;
        ArrayList r218 = r40;
        Format r219 = r46;
        Metadata r51 = new Metadata(new Metadata.Entry[]{new HlsTrackMetadataEntry(r48, r49, Collections.emptyList())});
        String r52 = parseStringAttr(r47, REGEX_TYPE, r11);
        switch(r52.hashCode()) {
            case -959297733: goto L91;
            case -333210994: goto L88;
            case 62628790: goto L85;
            case 81665115: goto L82;
            default: goto L93;
        };
    L93:
        byte r53 = -1;
    L94:
        switch(r53) {
            case 0: goto L132;
            case 1: goto L113;
            case 2: goto L104;
            case 3: goto L96;
            default: goto L95;
        };
    L95:
        String r220 = r15;
        ArrayList r54 = r36;
        ArrayList r55 = r39;
    L139:
        r46 = r219;
    L140:
        r45 = r217 + 1;
        r1 = r38;
        r36 = r54;
        r39 = r55;
        r41 = r216;
        r40 = r218;
        r15 = r220;
        goto L73
    L96:
        String r56 = parseStringAttr(r47, REGEX_INSTREAM_ID, r11);
        if (r56.startsWith("CC") == false) goto L99;
        String r57 = MimeTypes.APPLICATION_CEA608;
        int r58 = Integer.parseInt(r56.substring(2));
    L100:
        if (r215 != null) goto L102;
        List r59 = new ArrayList();
    L103:
        r220 = r15;
        r50.setSampleMimeType(r57).setAccessibilityChannel(r58);
        r59.add(r50.build());
        r215 = r59;
        r46 = r219;
        r54 = r36;
        r55 = r39;
        goto L140
    L102:
        r59 = r215;
        goto L103
    L99:
        r57 = MimeTypes.APPLICATION_CEA708;
        r58 = Integer.parseInt(r56.substring(7));
        goto L100
    L104:
        r220 = r15;
        String r60 = null;
        HlsMultivariantPlaylist.Variant r61 = getVariantWithSubtitleGroup(r2, r48);
        if (r61 == null) goto L107;
        String r62 = Util.getCodecsOfType(r61.format.codecs, 3);
        r50.setCodecs(r62);
        r60 = MimeTypes.getMediaMimeType(r62);
    L107:
        if (r60 != null) goto L109;
        r60 = MimeTypes.TEXT_VTT;
    L109:
        r50.setSampleMimeType(r60).setMetadata(r51);
        if (r118 == null) goto L112;
        r6.add(new HlsMultivariantPlaylist.Rendition(r118, r50.build(), r48, r49));
        r54 = r36;
        r55 = r39;
        goto L139
    L112:
        Log.m48w(LOG_TAG, "EXT-X-MEDIA tag with missing mandatory URI attribute: skipping");
        r54 = r36;
        r55 = r39;
        goto L139
    L113:
        r220 = r15;
        String r63 = null;
        HlsMultivariantPlaylist.Variant r64 = getVariantWithAudioGroup(r2, r48);
        if (r64 == null) goto L117;
        String r65 = Util.getCodecsOfType(r64.format.codecs, 1);
        r50.setCodecs(r65);
        r63 = MimeTypes.getMediaMimeType(r65);
    L117:
        String r66 = parseOptionalStringAttr(r47, REGEX_CHANNELS, r11);
        if (r66 == null) goto L124;
        r50.setChannelCount(Integer.parseInt(Util.splitAtFirst(r66, "/")[0]));
        HlsMultivariantPlaylist.Variant r221 = r64;
        if (MimeTypes.AUDIO_E_AC3.equals(r63) == true) goto L122;
    L125:
        r50.setSampleMimeType(r63);
        if (r118 == null) goto L128;
        r50.setMetadata(r51);
        r55 = r39;
        r55.add(new HlsMultivariantPlaylist.Rendition(r118, r50.build(), r48, r49));
        r54 = r36;
        goto L139
    L128:
        r55 = r39;
        if (r221 == null) goto L131;
        r46 = r50.build();
        r54 = r36;
        goto L140
    L131:
        r54 = r36;
        goto L139
    L122:
        if (r66.endsWith("/JOC") == false) goto L125;
        r63 = MimeTypes.AUDIO_E_AC3_JOC;
        r50.setCodecs(MimeTypes.CODEC_E_AC3_JOC);
        goto L125
    L124:
        r221 = r64;
        goto L125
    L132:
        r220 = r15;
        r55 = r39;
        HlsMultivariantPlaylist.Variant r67 = getVariantWithVideoGroup(r2, r48);
        if (r67 == null) goto L136;
        Format r68 = r67.format;
        String r69 = Util.getCodecsOfType(r68.codecs, 2);
        r50.setCodecs(r69).setSampleMimeType(MimeTypes.getMediaMimeType(r69)).setWidth(r68.width).setHeight(r68.height).setFrameRate(r68.frameRate);
    L136:
        if (r118 != null) goto L138;
        r54 = r36;
        goto L139
    L138:
        r50.setMetadata(r51);
        r54 = r36;
        r54.add(new HlsMultivariantPlaylist.Rendition(r118, r50.build(), r48, r49));
        goto L139
    L82:
        if (r52.equals(TYPE_VIDEO) == false) goto L93;
        r53 = 0;
        goto L94
    L85:
        if (r52.equals(TYPE_AUDIO) == false) goto L93;
        r53 = 1;
        goto L94
    L88:
        if (r52.equals(TYPE_CLOSED_CAPTIONS) == false) goto L93;
        r53 = 3;
        goto L94
    L91:
        if (r52.equals(TYPE_SUBTITLES) == false) goto L93;
        r53 = 2;
        goto L94
    L77:
        r118 = UriUtil.resolveToUri(r1, r117);
        goto L78
    L141:
        ArrayList r222 = r40;
        Format r223 = r46;
        ArrayList r70 = r36;
        ArrayList r71 = r39;
        if (r16 == false) goto L145;
        r215 = Collections.emptyList();
    L145:
        return new HlsMultivariantPlaylist(r38, r214, r222, r70, r71, r6, r310, r223, r215, r116, r11, r311);
    }

    private static boolean parseOptionalBooleanAttribute(String r3, Pattern r4, boolean r5) {
        Matcher r0 = r4.matcher(r3);
        if (r0.find() == true) goto L5;
        return r5;
    L5:
        return BOOLEAN_TRUE.equals(r0.group(1));
    }

    private static double parseOptionalDoubleAttr(String r3, Pattern r4, double r5) {
        Matcher r0 = r4.matcher(r3);
        if (r0.find() == true) goto L5;
        return r5;
    L5:
        return Double.parseDouble((String) Assertions.checkNotNull(r0.group(1)));
    }

    private static int parseOptionalIntAttr(String r2, Pattern r3, int r4) {
        Matcher r0 = r3.matcher(r2);
        if (r0.find() == true) goto L5;
        return r4;
    L5:
        return Integer.parseInt((String) Assertions.checkNotNull(r0.group(1)));
    }

    private static long parseOptionalLongAttr(String r3, Pattern r4, long r5) {
        Matcher r0 = r4.matcher(r3);
        if (r0.find() == true) goto L5;
        return r5;
    L5:
        return Long.parseLong((String) Assertions.checkNotNull(r0.group(1)));
    }

    private static String parseOptionalStringAttr(String r3, Pattern r4, String r5, Map<String, String> r6) {
        Matcher r0 = r4.matcher(r3);
        if (r0.find() == false) goto L5;
        String r1 = (String) Assertions.checkNotNull(r0.group(1));
    L7:
        if (r6.isEmpty() == true) goto L12;
        if (r1 == null) goto L12;
        return replaceVariableReferences(r1, r6);
    L12:
        return r1;
    L5:
        r1 = r5;
        goto L7
    }

    private static String parseOptionalStringAttr(String r1, Pattern r2, Map<String, String> r3) {
        return parseOptionalStringAttr(r1, r2, null, r3);
    }

    private static int parseRoleFlags(String r4, Map<String, String> r5) {
        String r0 = parseOptionalStringAttr(r4, REGEX_CHARACTERISTICS, r5);
        if (TextUtils.isEmpty(r0) == false) goto L6;
        return 0;
    L6:
        String[] r1 = Util.split(r0, ",");
        int r2 = 0;
        if (Util.contains(r1, "public.accessibility.describes-video") == false) goto L10;
        r2 = 0 | 512;
    L10:
        if (Util.contains(r1, "public.accessibility.transcribes-spoken-dialog") == false) goto L13;
        r2 = r2 | 4096;
    L13:
        if (Util.contains(r1, "public.accessibility.describes-music-and-sound") == false) goto L16;
        r2 = r2 | 1024;
    L16:
        if (Util.contains(r1, "public.easy-to-read") == true) goto L18;
        return r2;
    L18:
        return r2 | 8192;
    }

    private static int parseSelectionFlags(String r3) {
        int r0 = 0;
        if (parseOptionalBooleanAttribute(r3, REGEX_DEFAULT, false) == false) goto L6;
        r0 = 0 | 1;
    L6:
        if (parseOptionalBooleanAttribute(r3, REGEX_FORCED, false) == false) goto L9;
        r0 = r0 | 2;
    L9:
        if (parseOptionalBooleanAttribute(r3, REGEX_AUTOSELECT, false) == true) goto L11;
        return r0;
    L11:
        return r0 | 4;
    }

    private static HlsMediaPlaylist.ServerControl parseServerControl(String r25) {
        double r4 = parseOptionalDoubleAttr(r25, REGEX_CAN_SKIP_UNTIL, -9.223372036854776E18d);
        if (r4 != (-9.223372036854776E18d)) goto L5;
        long r10 = -9223372036854775807L;
    L6:
        long r13 = r10;
        boolean r15 = parseOptionalBooleanAttribute(r25, REGEX_CAN_SKIP_DATE_RANGES, false);
        double r21 = parseOptionalDoubleAttr(r25, REGEX_HOLD_BACK, -9.223372036854776E18d);
        if (r21 != (-9.223372036854776E18d)) goto L9;
        long r16 = -9223372036854775807L;
    L10:
        double r23 = parseOptionalDoubleAttr(r25, REGEX_PART_HOLD_BACK, -9.223372036854776E18d);
        if (r23 != (-9.223372036854776E18d)) goto L13;
        long r18 = -9223372036854775807L;
    L15:
        return new HlsMediaPlaylist.ServerControl(r13, r15, r16, r18, parseOptionalBooleanAttribute(r25, REGEX_CAN_BLOCK_RELOAD, false));
    L13:
        r18 = (long) (1000000.0d * r23);
        goto L15
    L9:
        r16 = (long) (r21 * 1000000.0d);
        goto L10
    L5:
        r10 = (long) (r4 * 1000000.0d);
        goto L6
    }

    private static String parseStringAttr(String r3, Pattern r4, Map<String, String> r5) throws ParserException {
        String r0 = parseOptionalStringAttr(r3, r4, r5);
        if (r0 == null) goto L6;
        return r0;
    L6:
        throw ParserException.createForMalformedManifest("Couldn't match " + r4.pattern() + " in " + r3, null);
    }

    private static long parseTimeSecondsToUs(String r5, Pattern r6) throws ParserException {
        return new BigDecimal(parseStringAttr(r5, r6, Collections.emptyMap())).multiply(new BigDecimal(1000000)).longValue();
    }

    private static String replaceVariableReferences(String r4, Map<String, String> r5) {
        Matcher r0 = REGEX_VARIABLE_REFERENCE.matcher(r4);
        StringBuffer r1 = new StringBuffer();
    L4:
        if (r0.find() == false) goto L9;
        String r2 = r0.group(1);
        if (r5.containsKey(r2) == false) goto L4;
        r0.appendReplacement(r1, Matcher.quoteReplacement(r5.get(r2)));
        goto L4
    L9:
        r0.appendTail(r1);
        return r1.toString();
    }

    private static int skipIgnorableWhitespace(BufferedReader r1, boolean r2, int r3) throws IOException {
    L3:
        if (r3 == (-1)) goto L10;
        if (Character.isWhitespace(r3) == false) goto L10;
        if (r2 == true) goto L9;
        if (Util.isLinebreak(r3) == true) goto L10;
    L9:
        r3 = r1.read();
    L10:
        return r3;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
    public HlsPlaylist parse(Uri r8, InputStream r9) throws IOException {
        BufferedReader r0 = new BufferedReader(new InputStreamReader(r9));
        ArrayDeque r1 = new ArrayDeque();
    L40:
        th = move-exception;
        Util.closeQuietly(r0);
        throw th;
    L4:
        if (checkPlaylistHeader(r0) == false) goto L39;
    L5:
        String r2 = r0.readLine();     // Catch: Throwable -> L40
        if (r2 == null) goto L36;
        String r3 = r2.trim();     // Catch: Throwable -> L40
        if (r3.isEmpty() == true) goto L5;
        if (r3.startsWith(TAG_STREAM_INF) == true) goto L12;
        if (r3.startsWith(TAG_TARGET_DURATION) == true) goto L33;
        if (r3.startsWith(TAG_MEDIA_SEQUENCE) == true) goto L33;
        if (r3.startsWith(TAG_MEDIA_DURATION) == true) goto L33;
        if (r3.startsWith(TAG_KEY) == true) goto L33;
        if (r3.startsWith(TAG_BYTERANGE) == true) goto L33;
        if (r3.equals(TAG_DISCONTINUITY) == true) goto L33;
        if (r3.equals(TAG_DISCONTINUITY_SEQUENCE) == true) goto L33;
        if (r3.equals(TAG_ENDLIST) == true) goto L33;
        r1.add(r3);     // Catch: Throwable -> L40
    L33:
        r1.add(r3);     // Catch: Throwable -> L40
        HlsMediaPlaylist r4 = parseMediaPlaylist(this.multivariantPlaylist, this.previousMediaPlaylist, new LineIterator(r1, r0), r8.toString());     // Catch: Throwable -> L40
        Util.closeQuietly(r0);
        return r4;
    L12:
        r1.add(r3);     // Catch: Throwable -> L40
        HlsMultivariantPlaylist r5 = parseMultivariantPlaylist(new LineIterator(r1, r0), r8.toString());     // Catch: Throwable -> L40
        Util.closeQuietly(r0);
        return r5;
    L36:
        Util.closeQuietly(r0);
        throw ParserException.createForMalformedManifest("Failed to parse the playlist, could not identify any tags.", null);
    L39:
        throw ParserException.createForMalformedManifest("Input does not start with the #EXTM3U header.", null);     // Catch: Throwable -> L40
    }

    @Override // androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
    public /* bridge */ /* synthetic */ HlsPlaylist parse(Uri r1, InputStream r2) throws IOException {
        return parse(r1, r2);
    }
}
