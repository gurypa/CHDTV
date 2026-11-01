package androidx.media3.extractor.mkv;

import android.util.Pair;
import android.util.SparseArray;
import androidx.media3.common.C0180C;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.LongArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.container.NalUnitUtil;
import androidx.media3.extractor.AacUtil;
import androidx.media3.extractor.AvcConfig;
import androidx.media3.extractor.ChunkIndex;
import androidx.media3.extractor.DolbyVisionConfig;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.HevcConfig;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.TrueHdSampleRechunker;
import androidx.media3.extractor.text.SubtitleParser;
import androidx.media3.extractor.text.SubtitleTranscodingExtractorOutput;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public class MatroskaExtractor implements Extractor {
    private static final int BLOCK_ADDITIONAL_ID_VP9_ITU_T_35 = 4;
    private static final int BLOCK_ADD_ID_TYPE_DVCC = 1685480259;
    private static final int BLOCK_ADD_ID_TYPE_DVVC = 1685485123;
    private static final int BLOCK_STATE_DATA = 2;
    private static final int BLOCK_STATE_HEADER = 1;
    private static final int BLOCK_STATE_START = 0;
    private static final String CODEC_ID_AAC = "A_AAC";
    private static final String CODEC_ID_AC3 = "A_AC3";
    private static final String CODEC_ID_ACM = "A_MS/ACM";
    private static final String CODEC_ID_ASS = "S_TEXT/ASS";
    private static final String CODEC_ID_AV1 = "V_AV1";
    private static final String CODEC_ID_DTS = "A_DTS";
    private static final String CODEC_ID_DTS_EXPRESS = "A_DTS/EXPRESS";
    private static final String CODEC_ID_DTS_LOSSLESS = "A_DTS/LOSSLESS";
    private static final String CODEC_ID_DVBSUB = "S_DVBSUB";
    private static final String CODEC_ID_E_AC3 = "A_EAC3";
    private static final String CODEC_ID_FLAC = "A_FLAC";
    private static final String CODEC_ID_FOURCC = "V_MS/VFW/FOURCC";
    private static final String CODEC_ID_H264 = "V_MPEG4/ISO/AVC";
    private static final String CODEC_ID_H265 = "V_MPEGH/ISO/HEVC";
    private static final String CODEC_ID_MP2 = "A_MPEG/L2";
    private static final String CODEC_ID_MP3 = "A_MPEG/L3";
    private static final String CODEC_ID_MPEG2 = "V_MPEG2";
    private static final String CODEC_ID_MPEG4_AP = "V_MPEG4/ISO/AP";
    private static final String CODEC_ID_MPEG4_ASP = "V_MPEG4/ISO/ASP";
    private static final String CODEC_ID_MPEG4_SP = "V_MPEG4/ISO/SP";
    private static final String CODEC_ID_OPUS = "A_OPUS";
    private static final String CODEC_ID_PCM_FLOAT = "A_PCM/FLOAT/IEEE";
    private static final String CODEC_ID_PCM_INT_BIG = "A_PCM/INT/BIG";
    private static final String CODEC_ID_PCM_INT_LIT = "A_PCM/INT/LIT";
    private static final String CODEC_ID_PGS = "S_HDMV/PGS";
    private static final String CODEC_ID_SUBRIP = "S_TEXT/UTF8";
    private static final String CODEC_ID_THEORA = "V_THEORA";
    private static final String CODEC_ID_TRUEHD = "A_TRUEHD";
    private static final String CODEC_ID_VOBSUB = "S_VOBSUB";
    private static final String CODEC_ID_VORBIS = "A_VORBIS";
    private static final String CODEC_ID_VP8 = "V_VP8";
    private static final String CODEC_ID_VP9 = "V_VP9";
    private static final String CODEC_ID_VTT = "S_TEXT/WEBVTT";
    private static final String DOC_TYPE_MATROSKA = "matroska";
    private static final String DOC_TYPE_WEBM = "webm";
    private static final int ENCRYPTION_IV_SIZE = 8;

    @Deprecated
    public static final ExtractorsFactory FACTORY = null;
    public static final int FLAG_DISABLE_SEEK_FOR_CUES = 1;
    public static final int FLAG_EMIT_RAW_SUBTITLE_DATA = 2;
    private static final int FOURCC_COMPRESSION_DIVX = 1482049860;
    private static final int FOURCC_COMPRESSION_H263 = 859189832;
    private static final int FOURCC_COMPRESSION_VC1 = 826496599;
    private static final int ID_AUDIO = 225;
    private static final int ID_AUDIO_BIT_DEPTH = 25188;
    private static final int ID_BLOCK = 161;
    private static final int ID_BLOCK_ADDITIONAL = 165;
    private static final int ID_BLOCK_ADDITIONS = 30113;
    private static final int ID_BLOCK_ADDITION_MAPPING = 16868;
    private static final int ID_BLOCK_ADD_ID = 238;
    private static final int ID_BLOCK_ADD_ID_EXTRA_DATA = 16877;
    private static final int ID_BLOCK_ADD_ID_TYPE = 16871;
    private static final int ID_BLOCK_DURATION = 155;
    private static final int ID_BLOCK_GROUP = 160;
    private static final int ID_BLOCK_MORE = 166;
    private static final int ID_CHANNELS = 159;
    private static final int ID_CLUSTER = 524531317;
    private static final int ID_CODEC_DELAY = 22186;
    private static final int ID_CODEC_ID = 134;
    private static final int ID_CODEC_PRIVATE = 25506;
    private static final int ID_COLOUR = 21936;
    private static final int ID_COLOUR_BITS_PER_CHANNEL = 21938;
    private static final int ID_COLOUR_PRIMARIES = 21947;
    private static final int ID_COLOUR_RANGE = 21945;
    private static final int ID_COLOUR_TRANSFER = 21946;
    private static final int ID_CONTENT_COMPRESSION = 20532;
    private static final int ID_CONTENT_COMPRESSION_ALGORITHM = 16980;
    private static final int ID_CONTENT_COMPRESSION_SETTINGS = 16981;
    private static final int ID_CONTENT_ENCODING = 25152;
    private static final int ID_CONTENT_ENCODINGS = 28032;
    private static final int ID_CONTENT_ENCODING_ORDER = 20529;
    private static final int ID_CONTENT_ENCODING_SCOPE = 20530;
    private static final int ID_CONTENT_ENCRYPTION = 20533;
    private static final int ID_CONTENT_ENCRYPTION_AES_SETTINGS = 18407;
    private static final int ID_CONTENT_ENCRYPTION_AES_SETTINGS_CIPHER_MODE = 18408;
    private static final int ID_CONTENT_ENCRYPTION_ALGORITHM = 18401;
    private static final int ID_CONTENT_ENCRYPTION_KEY_ID = 18402;
    private static final int ID_CUES = 475249515;
    private static final int ID_CUE_CLUSTER_POSITION = 241;
    private static final int ID_CUE_POINT = 187;
    private static final int ID_CUE_TIME = 179;
    private static final int ID_CUE_TRACK_POSITIONS = 183;
    private static final int ID_DEFAULT_DURATION = 2352003;
    private static final int ID_DISCARD_PADDING = 30114;
    private static final int ID_DISPLAY_HEIGHT = 21690;
    private static final int ID_DISPLAY_UNIT = 21682;
    private static final int ID_DISPLAY_WIDTH = 21680;
    private static final int ID_DOC_TYPE = 17026;
    private static final int ID_DOC_TYPE_READ_VERSION = 17029;
    private static final int ID_DURATION = 17545;
    private static final int ID_EBML = 440786851;
    private static final int ID_EBML_READ_VERSION = 17143;
    private static final int ID_FLAG_DEFAULT = 136;
    private static final int ID_FLAG_FORCED = 21930;
    private static final int ID_INFO = 357149030;
    private static final int ID_LANGUAGE = 2274716;
    private static final int ID_LUMNINANCE_MAX = 21977;
    private static final int ID_LUMNINANCE_MIN = 21978;
    private static final int ID_MASTERING_METADATA = 21968;
    private static final int ID_MAX_BLOCK_ADDITION_ID = 21998;
    private static final int ID_MAX_CLL = 21948;
    private static final int ID_MAX_FALL = 21949;
    private static final int ID_NAME = 21358;
    private static final int ID_PIXEL_HEIGHT = 186;
    private static final int ID_PIXEL_WIDTH = 176;
    private static final int ID_PRIMARY_B_CHROMATICITY_X = 21973;
    private static final int ID_PRIMARY_B_CHROMATICITY_Y = 21974;
    private static final int ID_PRIMARY_G_CHROMATICITY_X = 21971;
    private static final int ID_PRIMARY_G_CHROMATICITY_Y = 21972;
    private static final int ID_PRIMARY_R_CHROMATICITY_X = 21969;
    private static final int ID_PRIMARY_R_CHROMATICITY_Y = 21970;
    private static final int ID_PROJECTION = 30320;
    private static final int ID_PROJECTION_POSE_PITCH = 30324;
    private static final int ID_PROJECTION_POSE_ROLL = 30325;
    private static final int ID_PROJECTION_POSE_YAW = 30323;
    private static final int ID_PROJECTION_PRIVATE = 30322;
    private static final int ID_PROJECTION_TYPE = 30321;
    private static final int ID_REFERENCE_BLOCK = 251;
    private static final int ID_SAMPLING_FREQUENCY = 181;
    private static final int ID_SEEK = 19899;
    private static final int ID_SEEK_HEAD = 290298740;
    private static final int ID_SEEK_ID = 21419;
    private static final int ID_SEEK_POSITION = 21420;
    private static final int ID_SEEK_PRE_ROLL = 22203;
    private static final int ID_SEGMENT = 408125543;
    private static final int ID_SEGMENT_INFO = 357149030;
    private static final int ID_SIMPLE_BLOCK = 163;
    private static final int ID_STEREO_MODE = 21432;
    private static final int ID_TIMECODE_SCALE = 2807729;
    private static final int ID_TIME_CODE = 231;
    private static final int ID_TRACKS = 374648427;
    private static final int ID_TRACK_ENTRY = 174;
    private static final int ID_TRACK_NUMBER = 215;
    private static final int ID_TRACK_TYPE = 131;
    private static final int ID_VIDEO = 224;
    private static final int ID_WHITE_POINT_CHROMATICITY_X = 21975;
    private static final int ID_WHITE_POINT_CHROMATICITY_Y = 21976;
    private static final int LACING_EBML = 3;
    private static final int LACING_FIXED_SIZE = 2;
    private static final int LACING_NONE = 0;
    private static final int LACING_XIPH = 1;
    private static final int OPUS_MAX_INPUT_SIZE = 5760;
    private static final byte[] SSA_DIALOGUE_FORMAT = null;
    private static final byte[] SSA_PREFIX = null;
    private static final int SSA_PREFIX_END_TIMECODE_OFFSET = 21;
    private static final String SSA_TIMECODE_FORMAT = "%01d:%02d:%02d:%02d";
    private static final long SSA_TIMECODE_LAST_VALUE_SCALING_FACTOR = 10000;
    private static final byte[] SUBRIP_PREFIX = null;
    private static final int SUBRIP_PREFIX_END_TIMECODE_OFFSET = 19;
    private static final String SUBRIP_TIMECODE_FORMAT = "%02d:%02d:%02d,%03d";
    private static final long SUBRIP_TIMECODE_LAST_VALUE_SCALING_FACTOR = 1000;
    private static final String TAG = "MatroskaExtractor";
    private static final Map<String, Integer> TRACK_NAME_TO_ROTATION_DEGREES = null;
    private static final int TRACK_TYPE_AUDIO = 2;
    private static final int UNSET_ENTRY_ID = -1;
    private static final int VORBIS_MAX_INPUT_SIZE = 8192;
    private static final byte[] VTT_PREFIX = null;
    private static final int VTT_PREFIX_END_TIMECODE_OFFSET = 25;
    private static final String VTT_TIMECODE_FORMAT = "%02d:%02d:%02d.%03d";
    private static final long VTT_TIMECODE_LAST_VALUE_SCALING_FACTOR = 1000;
    private static final int WAVE_FORMAT_EXTENSIBLE = 65534;
    private static final int WAVE_FORMAT_PCM = 1;
    private static final int WAVE_FORMAT_SIZE = 18;
    private static final UUID WAVE_SUBFORMAT_PCM = null;
    private int blockAdditionalId;
    private long blockDurationUs;
    private int blockFlags;
    private long blockGroupDiscardPaddingNs;
    private boolean blockHasReferenceBlock;
    private int blockSampleCount;
    private int blockSampleIndex;
    private int[] blockSampleSizes;
    private int blockState;
    private long blockTimeUs;
    private int blockTrackNumber;
    private int blockTrackNumberLength;
    private long clusterTimecodeUs;
    private LongArray cueClusterPositions;
    private LongArray cueTimesUs;
    private long cuesContentPosition;
    private Track currentTrack;
    private long durationTimecode;
    private long durationUs;
    private final ParsableByteArray encryptionInitializationVector;
    private final ParsableByteArray encryptionSubsampleData;
    private ByteBuffer encryptionSubsampleDataBuffer;
    private ExtractorOutput extractorOutput;
    private boolean haveOutputSample;
    private final ParsableByteArray nalLength;
    private final ParsableByteArray nalStartCode;
    private final boolean parseSubtitlesDuringExtraction;
    private final EbmlReader reader;
    private int sampleBytesRead;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;
    private boolean sampleEncodingHandled;
    private boolean sampleInitializationVectorRead;
    private int samplePartitionCount;
    private boolean samplePartitionCountRead;
    private byte sampleSignalByte;
    private boolean sampleSignalByteRead;
    private final ParsableByteArray sampleStrippedBytes;
    private final ParsableByteArray scratch;
    private int seekEntryId;
    private final ParsableByteArray seekEntryIdBytes;
    private long seekEntryPosition;
    private boolean seekForCues;
    private final boolean seekForCuesEnabled;
    private long seekPositionAfterBuildingCues;
    private boolean seenClusterPositionForCurrentCuePoint;
    private long segmentContentPosition;
    private long segmentContentSize;
    private boolean sentSeekMap;
    private final SubtitleParser.Factory subtitleParserFactory;
    private final ParsableByteArray subtitleSample;
    private final ParsableByteArray supplementalData;
    private long timecodeScale;
    private final SparseArray<Track> tracks;
    private final VarintReader varintReader;
    private final ParsableByteArray vorbisNumPageSamples;

    /* JADX INFO: renamed from: androidx.media3.extractor.mkv.MatroskaExtractor$1 */
    static /* synthetic */ class C04381 {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    private final class InnerEbmlProcessor implements EbmlProcessor {
        final /* synthetic */ MatroskaExtractor this$0;

        private InnerEbmlProcessor(MatroskaExtractor r1) {
            this.this$0 = r1;
        }

        /* synthetic */ InnerEbmlProcessor(MatroskaExtractor r1, C04381 r2) {
            this(r1);
        }

        @Override // androidx.media3.extractor.mkv.EbmlProcessor
        public void binaryElement(int r2, int r3, ExtractorInput r4) throws IOException {
            this.this$0.binaryElement(r2, r3, r4);
        }

        @Override // androidx.media3.extractor.mkv.EbmlProcessor
        public void endMasterElement(int r2) throws ParserException {
            this.this$0.endMasterElement(r2);
        }

        @Override // androidx.media3.extractor.mkv.EbmlProcessor
        public void floatElement(int r2, double r3) throws ParserException {
            this.this$0.floatElement(r2, r3);
        }

        @Override // androidx.media3.extractor.mkv.EbmlProcessor
        public int getElementType(int r2) {
            return this.this$0.getElementType(r2);
        }

        @Override // androidx.media3.extractor.mkv.EbmlProcessor
        public void integerElement(int r2, long r3) throws ParserException {
            this.this$0.integerElement(r2, r3);
        }

        @Override // androidx.media3.extractor.mkv.EbmlProcessor
        public boolean isLevel1Element(int r2) {
            return this.this$0.isLevel1Element(r2);
        }

        @Override // androidx.media3.extractor.mkv.EbmlProcessor
        public void startMasterElement(int r7, long r8, long r10) throws ParserException {
            this.this$0.startMasterElement(r7, r8, r10);
        }

        @Override // androidx.media3.extractor.mkv.EbmlProcessor
        public void stringElement(int r2, String r3) throws ParserException {
            this.this$0.stringElement(r2, r3);
        }
    }

    protected static final class Track {
        private static final int DEFAULT_MAX_CLL = 1000;
        private static final int DEFAULT_MAX_FALL = 200;
        private static final int DISPLAY_UNIT_PIXELS = 0;
        private static final int MAX_CHROMATICITY = 50000;
        public int audioBitDepth;
        public int bitsPerChannel;
        private int blockAddIdType;
        public int channelCount;
        public long codecDelayNs;
        public String codecId;
        public byte[] codecPrivate;
        public int colorRange;
        public int colorSpace;
        public int colorTransfer;
        public TrackOutput.CryptoData cryptoData;
        public int defaultSampleDurationNs;
        public int displayHeight;
        public int displayUnit;
        public int displayWidth;
        public byte[] dolbyVisionConfigBytes;
        public DrmInitData drmInitData;
        public boolean flagDefault;
        public boolean flagForced;
        public boolean hasColorInfo;
        public boolean hasContentEncryption;
        public int height;
        private String language;
        public int maxBlockAdditionId;
        public int maxContentLuminance;
        public int maxFrameAverageLuminance;
        public float maxMasteringLuminance;
        public float minMasteringLuminance;
        public int nalUnitLengthFieldLength;
        public String name;
        public int number;
        public TrackOutput output;
        public float primaryBChromaticityX;
        public float primaryBChromaticityY;
        public float primaryGChromaticityX;
        public float primaryGChromaticityY;
        public float primaryRChromaticityX;
        public float primaryRChromaticityY;
        public byte[] projectionData;
        public float projectionPosePitch;
        public float projectionPoseRoll;
        public float projectionPoseYaw;
        public int projectionType;
        public int sampleRate;
        public byte[] sampleStrippedBytes;
        public long seekPreRollNs;
        public int stereoMode;
        public TrueHdSampleRechunker trueHdSampleRechunker;
        public int type;
        public float whitePointChromaticityX;
        public float whitePointChromaticityY;
        public int width;

        protected Track() {
            this.width = -1;
            this.height = -1;
            this.bitsPerChannel = -1;
            this.displayWidth = -1;
            this.displayHeight = -1;
            this.displayUnit = 0;
            this.projectionType = -1;
            this.projectionPoseYaw = 0.0f;
            this.projectionPosePitch = 0.0f;
            this.projectionPoseRoll = 0.0f;
            this.projectionData = null;
            this.stereoMode = -1;
            this.hasColorInfo = false;
            this.colorSpace = -1;
            this.colorTransfer = -1;
            this.colorRange = -1;
            this.maxContentLuminance = 1000;
            this.maxFrameAverageLuminance = 200;
            this.primaryRChromaticityX = -1.0f;
            this.primaryRChromaticityY = -1.0f;
            this.primaryGChromaticityX = -1.0f;
            this.primaryGChromaticityY = -1.0f;
            this.primaryBChromaticityX = -1.0f;
            this.primaryBChromaticityY = -1.0f;
            this.whitePointChromaticityX = -1.0f;
            this.whitePointChromaticityY = -1.0f;
            this.maxMasteringLuminance = -1.0f;
            this.minMasteringLuminance = -1.0f;
            this.channelCount = 1;
            this.audioBitDepth = -1;
            this.sampleRate = 8000;
            this.codecDelayNs = 0;
            this.seekPreRollNs = 0;
            this.flagDefault = true;
            this.language = "eng";
        }

        static /* synthetic */ void access$100(Track r0) {
            r0.assertOutputInitialized();
        }

        static /* synthetic */ int access$200(Track r1) {
            return r1.blockAddIdType;
        }

        static /* synthetic */ int access$202(Track r0, int r1) {
            r0.blockAddIdType = r1;
            return r1;
        }

        static /* synthetic */ String access$302(Track r0, String r1) {
            r0.language = r1;
            return r1;
        }

        static /* synthetic */ boolean access$400(Track r1, boolean r2) {
            return r1.samplesHaveSupplementalData(r2);
        }

        @EnsuresNonNull({"output"})
        private void assertOutputInitialized() {
            Assertions.checkNotNull(this.output);
        }

        @EnsuresNonNull({"codecPrivate"})
        private byte[] getCodecPrivate(String r3) throws ParserException {
            if (this.codecPrivate == null) goto L7;
            return this.codecPrivate;
        L7:
            throw ParserException.createForMalformedContainer("Missing CodecPrivate for codec " + r3, null);
        }

        private byte[] getHdrStaticInfo() {
            if (this.primaryRChromaticityX != (-1.0f)) goto L5;
            return null;
        L5:
            if (this.primaryRChromaticityY != (-1.0f)) goto L7;
            return null;
        L7:
            if (this.primaryGChromaticityX != (-1.0f)) goto L9;
            return null;
        L9:
            if (this.primaryGChromaticityY != (-1.0f)) goto L11;
            return null;
        L11:
            if (this.primaryBChromaticityX != (-1.0f)) goto L13;
            return null;
        L13:
            if (this.primaryBChromaticityY != (-1.0f)) goto L15;
            return null;
        L15:
            if (this.whitePointChromaticityX != (-1.0f)) goto L17;
            return null;
        L17:
            if (this.whitePointChromaticityY != (-1.0f)) goto L19;
            return null;
        L19:
            if (this.maxMasteringLuminance != (-1.0f)) goto L21;
            return null;
        L21:
            if (this.minMasteringLuminance == (-1.0f)) goto L35;
            byte[] r0 = new byte[25];
            ByteBuffer r1 = ByteBuffer.wrap(r0).order(ByteOrder.LITTLE_ENDIAN);
            r1.put((byte) 0);
            r1.putShort((short) ((this.primaryRChromaticityX * 50000.0f) + 0.5f));
            r1.putShort((short) ((this.primaryRChromaticityY * 50000.0f) + 0.5f));
            r1.putShort((short) ((this.primaryGChromaticityX * 50000.0f) + 0.5f));
            r1.putShort((short) ((this.primaryGChromaticityY * 50000.0f) + 0.5f));
            r1.putShort((short) ((this.primaryBChromaticityX * 50000.0f) + 0.5f));
            r1.putShort((short) ((this.primaryBChromaticityY * 50000.0f) + 0.5f));
            r1.putShort((short) ((this.whitePointChromaticityX * 50000.0f) + 0.5f));
            r1.putShort((short) ((this.whitePointChromaticityY * 50000.0f) + 0.5f));
            r1.putShort((short) (this.maxMasteringLuminance + 0.5f));
            r1.putShort((short) (this.minMasteringLuminance + 0.5f));
            r1.putShort((short) this.maxContentLuminance);
            r1.putShort((short) this.maxFrameAverageLuminance);
            return r0;
        L35:
            return null;
        }

        private static Pair<String, List<byte[]>> parseFourCcPrivate(ParsableByteArray r10) throws ParserException {
            r10.skipBytes(16);     // Catch: ArrayIndexOutOfBoundsException -> L31
            long r2 = r10.readLittleEndianUnsignedInt();     // Catch: ArrayIndexOutOfBoundsException -> L31
            if (r2 != 1482049860) goto L8;
            return new Pair(MimeTypes.VIDEO_DIVX, null);
        L8:
            if (r2 != 859189832) goto L12;
            return new Pair(MimeTypes.VIDEO_H263, null);
        L12:
            if (r2 != 826496599) goto L29;
            int r0 = r10.getPosition() + 20;     // Catch: ArrayIndexOutOfBoundsException -> L31
            byte[] r4 = r10.getData();     // Catch: ArrayIndexOutOfBoundsException -> L31
            int r5 = r0;
        L15:
            if (r5 >= (r4.length - 4)) goto L28;
            if (r4[r5] != 0) goto L26;
            if (r4[r5 + 1] != 0) goto L26;
            if (r4[r5 + 2] != 1) goto L26;
            if (r4[r5 + 3] != 15) goto L26;
            return new Pair(MimeTypes.VIDEO_VC1, Collections.singletonList(Arrays.copyOfRange(r4, r5, r4.length)));
        L26:
            r5 = r5 + 1;     // Catch: ArrayIndexOutOfBoundsException -> L31
            goto L15
        L28:
            throw ParserException.createForMalformedContainer("Failed to find FourCC VC1 initialization data", null);     // Catch: ArrayIndexOutOfBoundsException -> L31
        L29:
            Log.m48w(MatroskaExtractor.TAG, "Unknown FourCC. Setting mimeType to video/x-unknown");
            return new Pair(MimeTypes.VIDEO_UNKNOWN, null);
        L33:
            throw ParserException.createForMalformedContainer("Error parsing FourCC private data", null);
        }

        private static boolean parseMsAcmCodecPrivate(ParsableByteArray r8) throws ParserException {
            int r0 = r8.readLittleEndianUnsignedShort();     // Catch: ArrayIndexOutOfBoundsException -> L16
            if (r0 != 1) goto L6;
            return true;
        L6:
            if (r0 != 65534) goto L15;
            r8.setPosition(24);     // Catch: ArrayIndexOutOfBoundsException -> L16
            if (r8.readLong() != MatroskaExtractor.access$700().getMostSignificantBits()) goto L14;
            if (r8.readLong() != MatroskaExtractor.access$700().getLeastSignificantBits()) goto L14;
            return true;
        L14:
            return false;
        L15:
            return false;
        L18:
            throw ParserException.createForMalformedContainer("Error parsing MS/ACM codec private", null);
        }

        private static List<byte[]> parseVorbisCodecPrivate(byte[] r10) throws ParserException {
            if (r10[0] != 2) goto L28;
            int r3 = 1;
            int r5 = 0;
        L7:
            if ((r10[r3] & 255) != 255) goto L9;
            r5 = r5 + 255;     // Catch: ArrayIndexOutOfBoundsException -> L29
            r3 = r3 + 1;     // Catch: ArrayIndexOutOfBoundsException -> L29
            goto L7
        L9:
            int r6 = r3 + 1;     // Catch: ArrayIndexOutOfBoundsException -> L29
            int r7 = r5 + (r10[r3] & 255);     // Catch: ArrayIndexOutOfBoundsException -> L29
            int r4 = 0;
        L11:
            if ((r10[r6] & 255) != 255) goto L13;
            r4 = r4 + 255;     // Catch: ArrayIndexOutOfBoundsException -> L29
            r6 = r6 + 1;     // Catch: ArrayIndexOutOfBoundsException -> L29
            goto L11
        L13:
            int r8 = r6 + 1;     // Catch: ArrayIndexOutOfBoundsException -> L29
            int r9 = r4 + (r10[r6] & 255);     // Catch: ArrayIndexOutOfBoundsException -> L29
            if (r10[r8] != 1) goto L26;
            byte[] r11 = new byte[r7];     // Catch: ArrayIndexOutOfBoundsException -> L29
            System.arraycopy(r10, r8, r11, 0, r7);     // Catch: ArrayIndexOutOfBoundsException -> L29
            int r12 = r8 + r7;     // Catch: ArrayIndexOutOfBoundsException -> L29
            if (r10[r12] != 3) goto L24;
            int r13 = r12 + r9;     // Catch: ArrayIndexOutOfBoundsException -> L29
            if (r10[r13] != 5) goto L22;
            byte[] r14 = new byte[r10.length - r13];     // Catch: ArrayIndexOutOfBoundsException -> L29
            System.arraycopy(r10, r13, r14, 0, r10.length - r13);     // Catch: ArrayIndexOutOfBoundsException -> L29
            ArrayList r1 = new ArrayList(2);     // Catch: ArrayIndexOutOfBoundsException -> L29
            r1.add(r11);     // Catch: ArrayIndexOutOfBoundsException -> L29
            r1.add(r14);     // Catch: ArrayIndexOutOfBoundsException -> L29
            return r1;
        L22:
            throw ParserException.createForMalformedContainer("Error parsing vorbis codec private", null);     // Catch: ArrayIndexOutOfBoundsException -> L29
        L24:
            throw ParserException.createForMalformedContainer("Error parsing vorbis codec private", null);     // Catch: ArrayIndexOutOfBoundsException -> L29
        L26:
            throw ParserException.createForMalformedContainer("Error parsing vorbis codec private", null);     // Catch: ArrayIndexOutOfBoundsException -> L29
        L28:
            throw ParserException.createForMalformedContainer("Error parsing vorbis codec private", null);     // Catch: ArrayIndexOutOfBoundsException -> L29
        L31:
            throw ParserException.createForMalformedContainer("Error parsing vorbis codec private", null);
        }

        private boolean samplesHaveSupplementalData(boolean r3) {
            if (MatroskaExtractor.CODEC_ID_OPUS.equals(this.codecId) == false) goto L6;
            return r3;
        L6:
            if (this.maxBlockAdditionId <= 0) goto L8;
            return true;
        L8:
            return false;
        }

        @EnsuresNonNull({"this.output"})
        @RequiresNonNull({"codecId"})
        public void initializeOutput(ExtractorOutput r17, int r18) throws ParserException {
            int r1 = -1;
            int r2 = -1;
            List<byte[]> r3 = null;
            String r4 = null;
            String r5 = this.codecId;
            switch(r5.hashCode()) {
                case -2095576542: goto L102;
                case -2095575984: goto L99;
                case -1985379776: goto L96;
                case -1784763192: goto L93;
                case -1730367663: goto L90;
                case -1482641358: goto L87;
                case -1482641357: goto L84;
                case -1373388978: goto L81;
                case -933872740: goto L78;
                case -538363189: goto L75;
                case -538363109: goto L72;
                case -425012669: goto L69;
                case -356037306: goto L66;
                case 62923557: goto L63;
                case 62923603: goto L60;
                case 62927045: goto L57;
                case 82318131: goto L54;
                case 82338133: goto L51;
                case 82338134: goto L48;
                case 99146302: goto L45;
                case 444813526: goto L42;
                case 542569478: goto L39;
                case 635596514: goto L36;
                case 725948237: goto L33;
                case 725957860: goto L30;
                case 738597099: goto L27;
                case 855502857: goto L24;
                case 1045209816: goto L21;
                case 1422270023: goto L18;
                case 1809237540: goto L15;
                case 1950749482: goto L12;
                case 1950789798: goto L9;
                case 1951062397: goto L6;
                default: goto L104;
            };
        L104:
            byte r6 = -1;
        L106:
            switch(r6) {
                case 0: goto L164;
                case 1: goto L163;
                case 2: goto L162;
                case 3: goto L161;
                case 4: goto L156;
                case 5: goto L156;
                case 6: goto L156;
                case 7: goto L155;
                case 8: goto L154;
                case 9: goto L153;
                case 10: goto L152;
                case 11: goto L151;
                case 12: goto L150;
                case 13: goto L149;
                case 14: goto L148;
                case 15: goto L147;
                case 16: goto L146;
                case 17: goto L145;
                case 18: goto L144;
                case 19: goto L143;
                case 20: goto L143;
                case 21: goto L142;
                case 22: goto L141;
                case 23: goto L135;
                case 24: goto L132;
                case 25: goto L119;
                case 26: goto L115;
                case 27: goto L114;
                case 28: goto L113;
                case 29: goto L112;
                case 30: goto L111;
                case 31: goto L110;
                case 32: goto L109;
                default: goto L108;
            };
        L109:
            String r7 = MimeTypes.APPLICATION_DVBSUBS;
            byte[] r8 = new byte[4];
            System.arraycopy(getCodecPrivate(this.codecId), 0, r8, 0, 4);
            r3 = ImmutableList.m213of(r8);
        L166:
            if (this.dolbyVisionConfigBytes == null) goto L170;
            DolbyVisionConfig r9 = DolbyVisionConfig.parse(new ParsableByteArray(this.dolbyVisionConfigBytes));
            if (r9 == null) goto L170;
            r4 = r9.codecs;
            r7 = MimeTypes.VIDEO_DOLBY_VISION;
        L170:
            int r10 = 0 | (this.flagDefault ? 1 : 0);
            if (this.flagForced == false) goto L173;
            int r11 = 2;
        L174:
            int r12 = r10 | r11;
            Format.Builder r13 = new Format.Builder();
            if (MimeTypes.isAudio(r7) == false) goto L178;
            int r14 = 1;
            r13.setChannelCount(this.channelCount).setSampleRate(this.sampleRate).setPcmEncoding(r2);
        L242:
            if (this.name != null) goto L244;
        L246:
            Format r15 = r13.setId(r18).setSampleMimeType(r7).setMaxInputSize(r1).setLanguage(this.language).setSelectionFlags(r12).setInitializationData(r3).setCodecs(r4).setDrmInitData(this.drmInitData).build();
            this.output = r17.track(this.number, r14);
            this.output.format(r15);
            return;
        L244:
            if (MatroskaExtractor.access$600().containsKey(this.name) == true) goto L246;
            r13.setLabel(this.name);
            goto L246
        L178:
            if (MimeTypes.isVideo(r7) == false) goto L226;
            r14 = 2;
            if (this.displayUnit == 0) goto L182;
        L190:
            float r16 = -1.0f;
            if (this.displayWidth != (-1)) goto L193;
        L195:
            ColorInfo r19 = null;
            if (this.hasColorInfo == false) goto L198;
            r19 = new ColorInfo.Builder().setColorSpace(this.colorSpace).setColorRange(this.colorRange).setColorTransfer(this.colorTransfer).setHdrStaticInfo(getHdrStaticInfo()).setLumaBitdepth(this.bitsPerChannel).setChromaBitdepth(this.bitsPerChannel).build();
        L198:
            int r110 = -1;
            if (this.name == null) goto L204;
            if (MatroskaExtractor.access$600().containsKey(this.name) == false) goto L204;
            r110 = ((Integer) MatroskaExtractor.access$600().get(this.name)).intValue();
        L204:
            if (this.projectionType == 0) goto L206;
        L224:
            r13.setWidth(this.width).setHeight(this.height).setPixelWidthHeightRatio(r16).setRotationDegrees(r110).setProjectionData(this.projectionData).setStereoMode(this.stereoMode).setColorInfo(r19);
            goto L242
        L206:
            if (Float.compare(this.projectionPoseYaw, 0.0f) != 0) goto L224;
            if (Float.compare(this.projectionPosePitch, 0.0f) != 0) goto L224;
            if (Float.compare(this.projectionPoseRoll, 0.0f) != 0) goto L213;
            r110 = 0;
            goto L224
        L213:
            if (Float.compare(this.projectionPoseRoll, 90.0f) != 0) goto L216;
            r110 = 90;
            goto L224
        L216:
            if (Float.compare(this.projectionPoseRoll, -180.0f) != 0) goto L218;
        L223:
            r110 = 180;
            goto L224
        L218:
            if (Float.compare(this.projectionPoseRoll, 180.0f) == 0) goto L223;
            if (Float.compare(this.projectionPoseRoll, -90.0f) != 0) goto L224;
            r110 = 270;
            goto L224
        L193:
            if (this.displayHeight == (-1)) goto L195;
            r16 = (this.height * this.displayWidth) / (this.width * this.displayHeight);
            goto L195
        L182:
            if (this.displayWidth != (-1)) goto L184;
            int r20 = this.width;
        L185:
            this.displayWidth = r20;
            if (this.displayHeight != (-1)) goto L188;
            int r21 = this.height;
        L189:
            this.displayHeight = r21;
            goto L190
        L188:
            r21 = this.displayHeight;
            goto L189
        L184:
            r20 = this.displayWidth;
            goto L185
        L226:
            if (MimeTypes.APPLICATION_SUBRIP.equals(r7) == false) goto L228;
        L240:
            r14 = 3;
            goto L242
        L228:
            if (MimeTypes.TEXT_SSA.equals(r7) == true) goto L240;
            if (MimeTypes.TEXT_VTT.equals(r7) == true) goto L240;
            if (MimeTypes.APPLICATION_VOBSUB.equals(r7) == true) goto L240;
            if (MimeTypes.APPLICATION_PGS.equals(r7) == true) goto L240;
            if (MimeTypes.APPLICATION_DVBSUBS.equals(r7) == true) goto L240;
            throw ParserException.createForMalformedContainer("Unexpected MIME type.", null);
        L173:
            r11 = 0;
            goto L174
        L110:
            r7 = MimeTypes.APPLICATION_PGS;
            goto L166
        L111:
            r7 = MimeTypes.APPLICATION_VOBSUB;
            r3 = ImmutableList.m213of(getCodecPrivate(this.codecId));
            goto L166
        L112:
            r7 = MimeTypes.TEXT_VTT;
            goto L166
        L113:
            r7 = MimeTypes.TEXT_SSA;
            r3 = ImmutableList.m214of(MatroskaExtractor.access$500(), getCodecPrivate(this.codecId));
            goto L166
        L114:
            r7 = MimeTypes.APPLICATION_SUBRIP;
            goto L166
        L115:
            r7 = MimeTypes.AUDIO_RAW;
            if (this.audioBitDepth != 32) goto L118;
            r2 = 4;
            goto L166
        L118:
            r2 = -1;
            r7 = MimeTypes.AUDIO_UNKNOWN;
            Log.m48w(MatroskaExtractor.TAG, "Unsupported floating point PCM bit depth: " + this.audioBitDepth + ". Setting mimeType to " + MimeTypes.AUDIO_UNKNOWN);
            goto L166
        L119:
            r7 = MimeTypes.AUDIO_RAW;
            if (this.audioBitDepth != 8) goto L123;
            r2 = 3;
            goto L166
        L123:
            if (this.audioBitDepth != 16) goto L126;
            r2 = 268435456;
            goto L166
        L126:
            if (this.audioBitDepth != 24) goto L129;
            r2 = C0180C.ENCODING_PCM_24BIT_BIG_ENDIAN;
            goto L166
        L129:
            if (this.audioBitDepth != 32) goto L131;
            r2 = C0180C.ENCODING_PCM_32BIT_BIG_ENDIAN;
            goto L166
        L131:
            r2 = -1;
            r7 = MimeTypes.AUDIO_UNKNOWN;
            Log.m48w(MatroskaExtractor.TAG, "Unsupported big endian PCM bit depth: " + this.audioBitDepth + ". Setting mimeType to " + MimeTypes.AUDIO_UNKNOWN);
            goto L166
        L132:
            r7 = MimeTypes.AUDIO_RAW;
            r2 = Util.getPcmEncoding(this.audioBitDepth);
            if (r2 != 0) goto L166;
            r2 = -1;
            r7 = MimeTypes.AUDIO_UNKNOWN;
            Log.m48w(MatroskaExtractor.TAG, "Unsupported little endian PCM bit depth: " + this.audioBitDepth + ". Setting mimeType to " + MimeTypes.AUDIO_UNKNOWN);
            goto L166
        L135:
            r7 = MimeTypes.AUDIO_RAW;
            if (parseMsAcmCodecPrivate(new ParsableByteArray(getCodecPrivate(this.codecId))) == false) goto L140;
            r2 = Util.getPcmEncoding(this.audioBitDepth);
            if (r2 != 0) goto L166;
            r2 = -1;
            r7 = MimeTypes.AUDIO_UNKNOWN;
            Log.m48w(MatroskaExtractor.TAG, "Unsupported PCM bit depth: " + this.audioBitDepth + ". Setting mimeType to " + MimeTypes.AUDIO_UNKNOWN);
            goto L166
        L140:
            r7 = MimeTypes.AUDIO_UNKNOWN;
            Log.m48w(MatroskaExtractor.TAG, "Non-PCM MS/ACM is unsupported. Setting mimeType to " + MimeTypes.AUDIO_UNKNOWN);
            goto L166
        L141:
            r7 = MimeTypes.AUDIO_FLAC;
            r3 = Collections.singletonList(getCodecPrivate(this.codecId));
            goto L166
        L142:
            r7 = MimeTypes.AUDIO_DTS_HD;
            goto L166
        L143:
            r7 = MimeTypes.AUDIO_DTS;
            goto L166
        L144:
            r7 = MimeTypes.AUDIO_TRUEHD;
            this.trueHdSampleRechunker = new TrueHdSampleRechunker();
            goto L166
        L145:
            r7 = MimeTypes.AUDIO_E_AC3;
            goto L166
        L146:
            r7 = MimeTypes.AUDIO_AC3;
            goto L166
        L147:
            r7 = MimeTypes.AUDIO_MPEG;
            r1 = 4096;
            goto L166
        L148:
            r7 = MimeTypes.AUDIO_MPEG_L2;
            r1 = 4096;
            goto L166
        L149:
            r7 = MimeTypes.AUDIO_AAC;
            r3 = Collections.singletonList(getCodecPrivate(this.codecId));
            AacUtil.Config r22 = AacUtil.parseAudioSpecificConfig(this.codecPrivate);
            this.sampleRate = r22.sampleRateHz;
            this.channelCount = r22.channelCount;
            r4 = r22.codecs;
            goto L166
        L150:
            r7 = MimeTypes.AUDIO_OPUS;
            r1 = MatroskaExtractor.OPUS_MAX_INPUT_SIZE;
            r3 = new ArrayList(3);
            r3.add(getCodecPrivate(this.codecId));
            r3.add(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(this.codecDelayNs).array());
            r3.add(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(this.seekPreRollNs).array());
            goto L166
        L151:
            r7 = MimeTypes.AUDIO_VORBIS;
            r1 = 8192;
            r3 = parseVorbisCodecPrivate(getCodecPrivate(this.codecId));
            goto L166
        L152:
            r7 = MimeTypes.VIDEO_UNKNOWN;
            goto L166
        L153:
            Pair<String, List<byte[]>> r23 = parseFourCcPrivate(new ParsableByteArray(getCodecPrivate(this.codecId)));
            String r24 = (String) r23.first;
            r3 = (List) r23.second;
            r7 = r24;
            goto L166
        L154:
            r7 = MimeTypes.VIDEO_H265;
            HevcConfig r25 = HevcConfig.parse(new ParsableByteArray(getCodecPrivate(this.codecId)));
            r3 = r25.initializationData;
            this.nalUnitLengthFieldLength = r25.nalUnitLengthFieldLength;
            r4 = r25.codecs;
            goto L166
        L155:
            r7 = MimeTypes.VIDEO_H264;
            AvcConfig r26 = AvcConfig.parse(new ParsableByteArray(getCodecPrivate(this.codecId)));
            r3 = r26.initializationData;
            this.nalUnitLengthFieldLength = r26.nalUnitLengthFieldLength;
            r4 = r26.codecs;
            goto L166
        L156:
            r7 = MimeTypes.VIDEO_MP4V;
            if (this.codecPrivate != null) goto L159;
            List<byte[]> r27 = null;
        L160:
            r3 = r27;
            goto L166
        L159:
            r27 = Collections.singletonList(this.codecPrivate);
            goto L160
        L161:
            r7 = MimeTypes.VIDEO_MPEG2;
            goto L166
        L162:
            r7 = MimeTypes.VIDEO_AV1;
            goto L166
        L163:
            r7 = MimeTypes.VIDEO_VP9;
            goto L166
        L164:
            r7 = MimeTypes.VIDEO_VP8;
            goto L166
        L108:
            throw ParserException.createForMalformedContainer("Unrecognized codec identifier.", null);
        L6:
            if (r5.equals(MatroskaExtractor.CODEC_ID_OPUS) == false) goto L248;
            r6 = Ascii.f81FF;
            goto L106
        L248:
            r6 = -1;
            goto L106
        L9:
            if (r5.equals(MatroskaExtractor.CODEC_ID_FLAC) == false) goto L249;
            r6 = Ascii.SYN;
            goto L106
        L249:
            r6 = -1;
            goto L106
        L12:
            if (r5.equals(MatroskaExtractor.CODEC_ID_E_AC3) == false) goto L250;
            r6 = 17;
            goto L106
        L250:
            r6 = -1;
            goto L106
        L15:
            if (r5.equals(MatroskaExtractor.CODEC_ID_MPEG2) == false) goto L251;
            r6 = 3;
            goto L106
        L251:
            r6 = -1;
            goto L106
        L18:
            if (r5.equals(MatroskaExtractor.CODEC_ID_SUBRIP) == false) goto L252;
            r6 = Ascii.ESC;
            goto L106
        L252:
            r6 = -1;
            goto L106
        L21:
            if (r5.equals(MatroskaExtractor.CODEC_ID_VTT) == false) goto L253;
            r6 = Ascii.f83GS;
            goto L106
        L253:
            r6 = -1;
            goto L106
        L24:
            if (r5.equals(MatroskaExtractor.CODEC_ID_H265) == false) goto L254;
            r6 = 8;
            goto L106
        L254:
            r6 = -1;
            goto L106
        L27:
            if (r5.equals(MatroskaExtractor.CODEC_ID_ASS) == false) goto L255;
            r6 = Ascii.f82FS;
            goto L106
        L255:
            r6 = -1;
            goto L106
        L30:
            if (r5.equals(MatroskaExtractor.CODEC_ID_PCM_INT_LIT) == false) goto L256;
            r6 = 24;
            goto L106
        L256:
            r6 = -1;
            goto L106
        L33:
            if (r5.equals(MatroskaExtractor.CODEC_ID_PCM_INT_BIG) == false) goto L257;
            r6 = Ascii.f80EM;
            goto L106
        L257:
            r6 = -1;
            goto L106
        L36:
            if (r5.equals(MatroskaExtractor.CODEC_ID_PCM_FLOAT) == false) goto L258;
            r6 = Ascii.SUB;
            goto L106
        L258:
            r6 = -1;
            goto L106
        L39:
            if (r5.equals(MatroskaExtractor.CODEC_ID_DTS_EXPRESS) == false) goto L259;
            r6 = Ascii.DC4;
            goto L106
        L259:
            r6 = -1;
            goto L106
        L42:
            if (r5.equals(MatroskaExtractor.CODEC_ID_THEORA) == false) goto L260;
            r6 = 10;
            goto L106
        L260:
            r6 = -1;
            goto L106
        L45:
            if (r5.equals(MatroskaExtractor.CODEC_ID_PGS) == false) goto L261;
            r6 = Ascii.f91US;
            goto L106
        L261:
            r6 = -1;
            goto L106
        L48:
            if (r5.equals(MatroskaExtractor.CODEC_ID_VP9) == false) goto L262;
            r6 = 1;
            goto L106
        L262:
            r6 = -1;
            goto L106
        L51:
            if (r5.equals(MatroskaExtractor.CODEC_ID_VP8) == false) goto L263;
            r6 = 0;
            goto L106
        L263:
            r6 = -1;
            goto L106
        L54:
            if (r5.equals(MatroskaExtractor.CODEC_ID_AV1) == false) goto L264;
            r6 = 2;
            goto L106
        L264:
            r6 = -1;
            goto L106
        L57:
            if (r5.equals(MatroskaExtractor.CODEC_ID_DTS) == false) goto L265;
            r6 = 19;
            goto L106
        L265:
            r6 = -1;
            goto L106
        L60:
            if (r5.equals(MatroskaExtractor.CODEC_ID_AC3) == false) goto L266;
            r6 = 16;
            goto L106
        L266:
            r6 = -1;
            goto L106
        L63:
            if (r5.equals(MatroskaExtractor.CODEC_ID_AAC) == false) goto L267;
            r6 = Ascii.f79CR;
            goto L106
        L267:
            r6 = -1;
            goto L106
        L66:
            if (r5.equals(MatroskaExtractor.CODEC_ID_DTS_LOSSLESS) == false) goto L268;
            r6 = Ascii.NAK;
            goto L106
        L268:
            r6 = -1;
            goto L106
        L69:
            if (r5.equals(MatroskaExtractor.CODEC_ID_VOBSUB) == false) goto L269;
            r6 = Ascii.f87RS;
            goto L106
        L269:
            r6 = -1;
            goto L106
        L72:
            if (r5.equals(MatroskaExtractor.CODEC_ID_H264) == false) goto L270;
            r6 = 7;
            goto L106
        L270:
            r6 = -1;
            goto L106
        L75:
            if (r5.equals(MatroskaExtractor.CODEC_ID_MPEG4_ASP) == false) goto L271;
            r6 = 5;
            goto L106
        L271:
            r6 = -1;
            goto L106
        L78:
            if (r5.equals(MatroskaExtractor.CODEC_ID_DVBSUB) == false) goto L272;
            r6 = 32;
            goto L106
        L272:
            r6 = -1;
            goto L106
        L81:
            if (r5.equals(MatroskaExtractor.CODEC_ID_FOURCC) == false) goto L273;
            r6 = 9;
            goto L106
        L273:
            r6 = -1;
            goto L106
        L84:
            if (r5.equals(MatroskaExtractor.CODEC_ID_MP3) == false) goto L274;
            r6 = Ascii.f88SI;
            goto L106
        L274:
            r6 = -1;
            goto L106
        L87:
            if (r5.equals(MatroskaExtractor.CODEC_ID_MP2) == false) goto L275;
            r6 = Ascii.f89SO;
            goto L106
        L275:
            r6 = -1;
            goto L106
        L90:
            if (r5.equals(MatroskaExtractor.CODEC_ID_VORBIS) == false) goto L276;
            r6 = Ascii.f92VT;
            goto L106
        L276:
            r6 = -1;
            goto L106
        L93:
            if (r5.equals(MatroskaExtractor.CODEC_ID_TRUEHD) == false) goto L277;
            r6 = Ascii.DC2;
            goto L106
        L277:
            r6 = -1;
            goto L106
        L96:
            if (r5.equals(MatroskaExtractor.CODEC_ID_ACM) == false) goto L278;
            r6 = Ascii.ETB;
            goto L106
        L278:
            r6 = -1;
            goto L106
        L99:
            if (r5.equals(MatroskaExtractor.CODEC_ID_MPEG4_SP) == false) goto L279;
            r6 = 4;
            goto L106
        L279:
            r6 = -1;
            goto L106
        L102:
            if (r5.equals(MatroskaExtractor.CODEC_ID_MPEG4_AP) == false) goto L280;
            r6 = 6;
            goto L106
        L280:
            r6 = -1;
            goto L106
        }

        @RequiresNonNull({"output"})
        public void outputPendingSampleMetadata() {
            if (this.trueHdSampleRechunker == null) goto L6;
            this.trueHdSampleRechunker.outputPendingSampleMetadata(this.output, this.cryptoData);
            return;
        }

        public void reset() {
            if (this.trueHdSampleRechunker == null) goto L6;
            this.trueHdSampleRechunker.reset();
            return;
        }
    }

    static {
        FACTORY = new MatroskaExtractor$$ExternalSyntheticLambda1();
        SUBRIP_PREFIX = new byte[]{49, 10, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 48, 48, 32, 45, 45, 62, 32, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 48, 48, 10};
        SSA_DIALOGUE_FORMAT = Util.getUtf8Bytes("Format: Start, End, ReadOrder, Layer, Style, Name, MarginL, MarginR, MarginV, Effect, Text");
        SSA_PREFIX = new byte[]{68, 105, 97, 108, 111, 103, 117, 101, 58, 32, 48, 58, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 58, 48, 48, 58, 48, 48, 58, 48, 48, 44};
        VTT_PREFIX = new byte[]{87, 69, 66, 86, 84, 84, 10, 10, 48, 48, 58, 48, 48, 58, 48, 48, 46, 48, 48, 48, 32, 45, 45, 62, 32, 48, 48, 58, 48, 48, 58, 48, 48, 46, 48, 48, 48, 10};
        WAVE_SUBFORMAT_PCM = new UUID(72057594037932032L, -9223371306706625679L);
        HashMap r0 = new HashMap();
        r0.put("htc_video_rotA-000", 0);
        r0.put("htc_video_rotA-090", 90);
        r0.put("htc_video_rotA-180", 180);
        r0.put("htc_video_rotA-270", 270);
        TRACK_NAME_TO_ROTATION_DEGREES = Collections.unmodifiableMap(r0);
    }

    @Deprecated
    public MatroskaExtractor() {
        this(new DefaultEbmlReader(), 2, SubtitleParser.Factory.UNSUPPORTED);
    }

    @Deprecated
    public MatroskaExtractor(int r4) {
        this(new DefaultEbmlReader(), r4 | 2, SubtitleParser.Factory.UNSUPPORTED);
    }

    MatroskaExtractor(EbmlReader r6, int r7, SubtitleParser.Factory r8) {
        this.segmentContentPosition = -1;
        this.timecodeScale = C0180C.TIME_UNSET;
        this.durationTimecode = C0180C.TIME_UNSET;
        this.durationUs = C0180C.TIME_UNSET;
        this.cuesContentPosition = -1;
        this.seekPositionAfterBuildingCues = -1;
        this.clusterTimecodeUs = C0180C.TIME_UNSET;
        this.reader = r6;
        this.reader.init(new InnerEbmlProcessor(this, null));
        this.subtitleParserFactory = r8;
        boolean r1 = false;
        if ((r7 & 1) != 0) goto L5;
        boolean r0 = true;
    L6:
        this.seekForCuesEnabled = r0;
        if ((r7 & 2) != 0) goto L9;
        r1 = true;
    L9:
        this.parseSubtitlesDuringExtraction = r1;
        this.varintReader = new VarintReader();
        this.tracks = new SparseArray();
        this.scratch = new ParsableByteArray(4);
        this.vorbisNumPageSamples = new ParsableByteArray(ByteBuffer.allocate(4).putInt(-1).array());
        this.seekEntryIdBytes = new ParsableByteArray(4);
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalLength = new ParsableByteArray(4);
        this.sampleStrippedBytes = new ParsableByteArray();
        this.subtitleSample = new ParsableByteArray();
        this.encryptionInitializationVector = new ParsableByteArray(8);
        this.encryptionSubsampleData = new ParsableByteArray();
        this.supplementalData = new ParsableByteArray();
        this.blockSampleSizes = new int[1];
        return;
    L5:
        r0 = false;
        goto L6
    }

    public MatroskaExtractor(SubtitleParser.Factory r3) {
        this(new DefaultEbmlReader(), 0, r3);
    }

    public MatroskaExtractor(SubtitleParser.Factory r2, int r3) {
        this(new DefaultEbmlReader(), r3, r2);
    }

    static /* synthetic */ byte[] access$500() {
        return SSA_DIALOGUE_FORMAT;
    }

    static /* synthetic */ Map access$600() {
        return TRACK_NAME_TO_ROTATION_DEGREES;
    }

    static /* synthetic */ UUID access$700() {
        return WAVE_SUBFORMAT_PCM;
    }

    @EnsuresNonNull({"cueTimesUs", "cueClusterPositions"})
    private void assertInCues(int r3) throws ParserException {
        if (this.cueTimesUs == null) goto L8;
        if (this.cueClusterPositions == null) goto L8;
        return;
    L8:
        throw ParserException.createForMalformedContainer("Element " + r3 + " must be in a Cues", null);
    }

    @EnsuresNonNull({"currentTrack"})
    private void assertInTrackEntry(int r3) throws ParserException {
        if (this.currentTrack == null) goto L6;
        return;
    L6:
        throw ParserException.createForMalformedContainer("Element " + r3 + " must be in a TrackEntry", null);
    }

    @EnsuresNonNull({"extractorOutput"})
    private void assertInitialized() {
        Assertions.checkStateNotNull(this.extractorOutput);
    }

    private SeekMap buildSeekMap(LongArray r11, LongArray r12) {
        if (this.segmentContentPosition == (-1)) goto L31;
        if (this.durationUs == C0180C.TIME_UNSET) goto L31;
        if (r11 == null) goto L31;
        if (r11.size() == 0) goto L31;
        if (r12 == null) goto L31;
        if (r12.size() != r11.size()) goto L31;
        int r0 = r11.size();
        int[] r1 = new int[r0];
        long[] r2 = new long[r0];
        long[] r3 = new long[r0];
        long[] r4 = new long[r0];
        int r5 = 0;
    L14:
        if (r5 >= r0) goto L16;
        r4[r5] = r11.get(r5);
        r2[r5] = this.segmentContentPosition + r12.get(r5);
        r5 = r5 + 1;
        goto L14
    L16:
        int r6 = 0;
    L18:
        if (r6 >= (r0 - 1)) goto L20;
        r1[r6] = (int) (r2[r6 + 1] - r2[r6]);
        r3[r6] = r4[r6 + 1] - r4[r6];
        r6 = r6 + 1;
        goto L18
    L20:
        int r7 = r0 - 1;
    L21:
        if (r7 <= 0) goto L25;
        if (r4[r7] <= this.durationUs) goto L25;
        r7 = r7 - 1;
    L25:
        r1[r7] = (int) ((this.segmentContentPosition + this.segmentContentSize) - r2[r7]);
        r3[r7] = this.durationUs - r4[r7];
        if (r7 >= (r0 - 1)) goto L29;
        Log.m48w(TAG, "Discarding trailing cue points with timestamps greater than total duration");
        r1 = Arrays.copyOf(r1, r7 + 1);
        r2 = Arrays.copyOf(r2, r7 + 1);
        r3 = Arrays.copyOf(r3, r7 + 1);
        r4 = Arrays.copyOf(r4, r7 + 1);
    L29:
        return new ChunkIndex(r1, r2, r3, r4);
    L31:
        return new SeekMap.Unseekable(this.durationUs);
    }

    @RequiresNonNull({"#1.output"})
    private void commitSampleToOutput(Track r20, long r21, int r23, int r24, int r25) {
        if (r20.trueHdSampleRechunker == null) goto L6;
        r20.trueHdSampleRechunker.sampleMetadata(r20.output, r21, r23, r24, r25, r20.cryptoData);
    L34:
        this.haveOutputSample = true;
        return;
    L6:
        if (CODEC_ID_SUBRIP.equals(r20.codecId) == true) goto L12;
        if (CODEC_ID_ASS.equals(r20.codecId) == true) goto L12;
        if (CODEC_ID_VTT.equals(r20.codecId) == true) goto L12;
    L17:
        int r2 = r24;
    L27:
        if ((r23 & 268435456) != 0) goto L29;
    L32:
        int r16 = r2;
    L33:
        r20.output.sampleMetadata(r21, r23, r16, r25, r20.cryptoData);
        goto L34
    L29:
        if (this.blockSampleCount <= 1) goto L31;
        this.supplementalData.reset(0);
        goto L32
    L31:
        int r4 = this.supplementalData.limit();
        r20.output.sampleData(this.supplementalData, r4, 2);
        r16 = r2 + r4;
    L12:
        if (this.blockSampleCount <= 1) goto L15;
        Log.m48w(TAG, "Skipping subtitle sample in laced block.");
        goto L17
    L15:
        if (this.blockDurationUs != C0180C.TIME_UNSET) goto L18;
        Log.m48w(TAG, "Skipping subtitle sample with no duration.");
        goto L17
    L18:
        setSubtitleEndTime(r20.codecId, this.blockDurationUs, this.subtitleSample.getData());
        int r3 = this.subtitleSample.getPosition();
    L20:
        if (r3 >= this.subtitleSample.limit()) goto L25;
        if (this.subtitleSample.getData()[r3] == 0) goto L23;
        r3 = r3 + 1;
        goto L20
    L23:
        this.subtitleSample.setLimit(r3);
    L25:
        r20.output.sampleData(this.subtitleSample, this.subtitleSample.limit());
        r2 = r24 + this.subtitleSample.limit();
        goto L27
    }

    private static int[] ensureArrayCapacity(int[] r1, int r2) {
        if (r1 != null) goto L6;
        return new int[r2];
    L6:
        if (r1.length < r2) goto L9;
        return r1;
    L9:
        return new int[Math.max(r1.length * 2, r2)];
    }

    private int finishWriteSampleData() {
        int r0 = this.sampleBytesWritten;
        resetWriteSampleData();
        return r0;
    }

    private static byte[] formatSubtitleTimecode(long r12, String r14, long r15) {
        if (r12 == C0180C.TIME_UNSET) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        int r1 = (int) (r12 / 3600000000L);
        long r13 = r12 - ((((long) r1) * 3600) * 1000000);
        int r3 = (int) (r13 / 60000000);
        long r16 = r13 - ((((long) r3) * 60) * 1000000);
        int r4 = (int) (r16 / 1000000);
        return Util.getUtf8Bytes(String.format(Locale.US, r14, new Object[]{Integer.valueOf(r1), Integer.valueOf(r3), Integer.valueOf(r4), Integer.valueOf((int) ((r16 - (((long) r4) * 1000000)) / r15))}));
    L5:
        r0 = false;
        goto L6
    }

    private static boolean isCodecSupported(String r3) {
        switch(r3.hashCode()) {
            case -2095576542: goto L102;
            case -2095575984: goto L99;
            case -1985379776: goto L96;
            case -1784763192: goto L93;
            case -1730367663: goto L90;
            case -1482641358: goto L87;
            case -1482641357: goto L84;
            case -1373388978: goto L81;
            case -933872740: goto L78;
            case -538363189: goto L75;
            case -538363109: goto L72;
            case -425012669: goto L69;
            case -356037306: goto L66;
            case 62923557: goto L63;
            case 62923603: goto L60;
            case 62927045: goto L57;
            case 82318131: goto L54;
            case 82338133: goto L51;
            case 82338134: goto L48;
            case 99146302: goto L45;
            case 444813526: goto L42;
            case 542569478: goto L39;
            case 635596514: goto L36;
            case 725948237: goto L33;
            case 725957860: goto L30;
            case 738597099: goto L27;
            case 855502857: goto L24;
            case 1045209816: goto L21;
            case 1422270023: goto L18;
            case 1809237540: goto L15;
            case 1950749482: goto L12;
            case 1950789798: goto L9;
            case 1951062397: goto L6;
            default: goto L104;
        };
    L104:
        byte r0 = -1;
    L105:
        switch(r0) {
            case 0: goto L107;
            case 1: goto L107;
            case 2: goto L107;
            case 3: goto L107;
            case 4: goto L107;
            case 5: goto L107;
            case 6: goto L107;
            case 7: goto L107;
            case 8: goto L107;
            case 9: goto L107;
            case 10: goto L107;
            case 11: goto L107;
            case 12: goto L107;
            case 13: goto L107;
            case 14: goto L107;
            case 15: goto L107;
            case 16: goto L107;
            case 17: goto L107;
            case 18: goto L107;
            case 19: goto L107;
            case 20: goto L107;
            case 21: goto L107;
            case 22: goto L107;
            case 23: goto L107;
            case 24: goto L107;
            case 25: goto L107;
            case 26: goto L107;
            case 27: goto L107;
            case 28: goto L107;
            case 29: goto L107;
            case 30: goto L107;
            case 31: goto L107;
            case 32: goto L107;
            default: goto L106;
        };
    L106:
        return false;
    L107:
        return true;
    L6:
        if (r3.equals(CODEC_ID_OPUS) == false) goto L104;
        r0 = Ascii.f92VT;
        goto L105
    L9:
        if (r3.equals(CODEC_ID_FLAC) == false) goto L104;
        r0 = Ascii.SYN;
        goto L105
    L12:
        if (r3.equals(CODEC_ID_E_AC3) == false) goto L104;
        r0 = 17;
        goto L105
    L15:
        if (r3.equals(CODEC_ID_MPEG2) == false) goto L104;
        r0 = 3;
        goto L105
    L18:
        if (r3.equals(CODEC_ID_SUBRIP) == false) goto L104;
        r0 = Ascii.ESC;
        goto L105
    L21:
        if (r3.equals(CODEC_ID_VTT) == false) goto L104;
        r0 = Ascii.f83GS;
        goto L105
    L24:
        if (r3.equals(CODEC_ID_H265) == false) goto L104;
        r0 = 8;
        goto L105
    L27:
        if (r3.equals(CODEC_ID_ASS) == false) goto L104;
        r0 = Ascii.f82FS;
        goto L105
    L30:
        if (r3.equals(CODEC_ID_PCM_INT_LIT) == false) goto L104;
        r0 = Ascii.CAN;
        goto L105
    L33:
        if (r3.equals(CODEC_ID_PCM_INT_BIG) == false) goto L104;
        r0 = Ascii.f80EM;
        goto L105
    L36:
        if (r3.equals(CODEC_ID_PCM_FLOAT) == false) goto L104;
        r0 = Ascii.SUB;
        goto L105
    L39:
        if (r3.equals(CODEC_ID_DTS_EXPRESS) == false) goto L104;
        r0 = Ascii.DC4;
        goto L105
    L42:
        if (r3.equals(CODEC_ID_THEORA) == false) goto L104;
        r0 = 10;
        goto L105
    L45:
        if (r3.equals(CODEC_ID_PGS) == false) goto L104;
        r0 = Ascii.f91US;
        goto L105
    L48:
        if (r3.equals(CODEC_ID_VP9) == false) goto L104;
        r0 = 1;
        goto L105
    L51:
        if (r3.equals(CODEC_ID_VP8) == false) goto L104;
        r0 = 0;
        goto L105
    L54:
        if (r3.equals(CODEC_ID_AV1) == false) goto L104;
        r0 = 2;
        goto L105
    L57:
        if (r3.equals(CODEC_ID_DTS) == false) goto L104;
        r0 = 19;
        goto L105
    L60:
        if (r3.equals(CODEC_ID_AC3) == false) goto L104;
        r0 = Ascii.DLE;
        goto L105
    L63:
        if (r3.equals(CODEC_ID_AAC) == false) goto L104;
        r0 = Ascii.f79CR;
        goto L105
    L66:
        if (r3.equals(CODEC_ID_DTS_LOSSLESS) == false) goto L104;
        r0 = Ascii.NAK;
        goto L105
    L69:
        if (r3.equals(CODEC_ID_VOBSUB) == false) goto L104;
        r0 = Ascii.f87RS;
        goto L105
    L72:
        if (r3.equals(CODEC_ID_H264) == false) goto L104;
        r0 = 7;
        goto L105
    L75:
        if (r3.equals(CODEC_ID_MPEG4_ASP) == false) goto L104;
        r0 = 5;
        goto L105
    L78:
        if (r3.equals(CODEC_ID_DVBSUB) == false) goto L104;
        r0 = 32;
        goto L105
    L81:
        if (r3.equals(CODEC_ID_FOURCC) == false) goto L104;
        r0 = 9;
        goto L105
    L84:
        if (r3.equals(CODEC_ID_MP3) == false) goto L104;
        r0 = Ascii.f88SI;
        goto L105
    L87:
        if (r3.equals(CODEC_ID_MP2) == false) goto L104;
        r0 = Ascii.f89SO;
        goto L105
    L90:
        if (r3.equals(CODEC_ID_VORBIS) == false) goto L104;
        r0 = Ascii.f81FF;
        goto L105
    L93:
        if (r3.equals(CODEC_ID_TRUEHD) == false) goto L104;
        r0 = Ascii.DC2;
        goto L105
    L96:
        if (r3.equals(CODEC_ID_ACM) == false) goto L104;
        r0 = Ascii.ETB;
        goto L105
    L99:
        if (r3.equals(CODEC_ID_MPEG4_SP) == false) goto L104;
        r0 = 4;
        goto L105
    L102:
        if (r3.equals(CODEC_ID_MPEG4_AP) == false) goto L104;
        r0 = 6;
        goto L105
    }

    static /* synthetic */ Extractor[] lambda$newFactory$0(SubtitleParser.Factory r3) {
        return new Extractor[]{new MatroskaExtractor(r3)};
    }

    static /* synthetic */ Extractor[] lambda$static$1() {
        return new Extractor[]{new MatroskaExtractor(SubtitleParser.Factory.UNSUPPORTED, 2)};
    }

    private boolean maybeSeekForCues(PositionHolder r8, long r9) {
        if (this.seekForCues == false) goto L7;
        this.seekPositionAfterBuildingCues = r9;
        r8.position = this.cuesContentPosition;
        this.seekForCues = false;
        return true;
    L7:
        if (this.sentSeekMap == true) goto L9;
    L12:
        return false;
    L9:
        if (this.seekPositionAfterBuildingCues == (-1)) goto L12;
        r8.position = this.seekPositionAfterBuildingCues;
        this.seekPositionAfterBuildingCues = -1;
        return true;
    }

    public static ExtractorsFactory newFactory(final SubtitleParser.Factory r1) {
        return new MatroskaExtractor$$ExternalSyntheticLambda0(r1);
    }

    private void readScratch(ExtractorInput r4, int r5) throws IOException {
        if (this.scratch.limit() < r5) goto L6;
        return;
    L6:
        if (this.scratch.capacity() >= r5) goto L8;
        this.scratch.ensureCapacity(Math.max(this.scratch.capacity() * 2, r5));
    L8:
        r4.readFully(this.scratch.getData(), this.scratch.limit(), r5 - this.scratch.limit());
        this.scratch.setLimit(r5);
    }

    private void resetWriteSampleData() {
        this.sampleBytesRead = 0;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        this.sampleEncodingHandled = false;
        this.sampleSignalByteRead = false;
        this.samplePartitionCountRead = false;
        this.samplePartitionCount = 0;
        this.sampleSignalByte = 0;
        this.sampleInitializationVectorRead = false;
        this.sampleStrippedBytes.reset(0);
    }

    private long scaleTimecodeToUs(long r8) throws ParserException {
        if (this.timecodeScale == C0180C.TIME_UNSET) goto L7;
        return Util.scaleLargeTimestamp(r8, this.timecodeScale, 1000);
    L7:
        throw ParserException.createForMalformedContainer("Can't scale timecode prior to timecodeScale being set.", null);
    }

    private static void setSubtitleEndTime(String r4, long r5, byte[] r7) {
        switch(r4.hashCode()) {
            case 738597099: goto L12;
            case 1045209816: goto L9;
            case 1422270023: goto L6;
            default: goto L14;
        };
    L14:
        byte r0 = -1;
    L16:
        switch(r0) {
            case 0: goto L21;
            case 1: goto L20;
            case 2: goto L19;
            default: goto L18;
        };
    L19:
        byte[] r1 = formatSubtitleTimecode(r5, VTT_TIMECODE_FORMAT, 1000);
        int r2 = 25;
    L22:
        System.arraycopy(r1, 0, r7, r2, r1.length);
        return;
    L20:
        r1 = formatSubtitleTimecode(r5, SSA_TIMECODE_FORMAT, 10000);
        r2 = 21;
        goto L22
    L21:
        r1 = formatSubtitleTimecode(r5, SUBRIP_TIMECODE_FORMAT, 1000);
        r2 = 19;
        goto L22
    L18:
        throw new IllegalArgumentException();
    L6:
        if (r4.equals(CODEC_ID_SUBRIP) == false) goto L14;
        r0 = 0;
        goto L16
    L9:
        if (r4.equals(CODEC_ID_VTT) == false) goto L14;
        r0 = 2;
        goto L16
    L12:
        if (r4.equals(CODEC_ID_ASS) == false) goto L14;
        r0 = 1;
        goto L16
    }

    @RequiresNonNull({"#2.output"})
    private int writeSampleData(ExtractorInput r20, Track r21, int r22, boolean r23) throws IOException {
        if (CODEC_ID_SUBRIP.equals(r21.codecId) == false) goto L7;
        writeSubtitleSampleData(r20, SUBRIP_PREFIX, r22);
        return finishWriteSampleData();
    L7:
        if (CODEC_ID_ASS.equals(r21.codecId) == false) goto L11;
        writeSubtitleSampleData(r20, SSA_PREFIX, r22);
        return finishWriteSampleData();
    L11:
        if (CODEC_ID_VTT.equals(r21.codecId) == false) goto L14;
        writeSubtitleSampleData(r20, VTT_PREFIX, r22);
        return finishWriteSampleData();
    L14:
        TrackOutput r4 = r21.output;
        int r7 = 2;
        boolean r8 = true;
        if (this.sampleEncodingHandled == true) goto L74;
        if (r21.hasContentEncryption == false) goto L65;
        this.blockFlags &= -1073741825;
        int r10 = 128;
        if (this.sampleSignalByteRead == true) goto L26;
        r20.readFully(this.scratch.getData(), 0, 1);
        this.sampleBytesRead++;
        if ((this.scratch.getData()[0] & 128) == 128) goto L24;
        this.sampleSignalByte = this.scratch.getData()[0];
        this.sampleSignalByteRead = true;
        goto L26
    L24:
        throw ParserException.createForMalformedContainer("Extension bit is set in signal byte", null);
    L26:
        if ((this.sampleSignalByte & 1) != 1) goto L28;
        boolean r5 = true;
    L29:
        if (r5 == true) goto L31;
        int r16 = 2;
    L70:
        if (Track.access$400(r21, r23) == false) goto L72;
        this.blockFlags |= 268435456;
        this.supplementalData.reset(0);
        int r6 = (this.sampleStrippedBytes.limit() + r22) - this.sampleBytesRead;
        this.scratch.reset(4);
        this.scratch.getData()[0] = (byte) ((r6 >> 24) & 255);
        this.scratch.getData()[1] = (byte) ((r6 >> 16) & 255);
        this.scratch.getData()[r16] = (byte) ((r6 >> 8) & 255);
        this.scratch.getData()[3] = (byte) (r6 & 255);
        r4.sampleData(this.scratch, 4, r16);
        this.sampleBytesWritten += 4;
    L72:
        this.sampleEncodingHandled = true;
        goto L74
    L31:
        if ((this.sampleSignalByte & 2) != 2) goto L33;
        boolean r11 = true;
    L34:
        this.blockFlags |= 1073741824;
        if (this.sampleInitializationVectorRead == true) goto L41;
        r20.readFully(this.encryptionInitializationVector.getData(), 0, 8);
        this.sampleBytesRead += 8;
        this.sampleInitializationVectorRead = true;
        byte[] r12 = this.scratch.getData();
        if (r11 == true) goto L40;
        r10 = 0;
    L40:
        r12[0] = (byte) (r10 | 8);
        this.scratch.setPosition(0);
        r4.sampleData(this.scratch, 1, 1);
        this.sampleBytesWritten++;
        this.encryptionInitializationVector.setPosition(0);
        r4.sampleData(this.encryptionInitializationVector, 8, 1);
        this.sampleBytesWritten += 8;
    L41:
        if (r11 == true) goto L43;
        r16 = 2;
        goto L70
    L43:
        if (this.samplePartitionCountRead == true) goto L45;
        r20.readFully(this.scratch.getData(), 0, 1);
        this.sampleBytesRead++;
        this.scratch.setPosition(0);
        this.samplePartitionCount = this.scratch.readUnsignedByte();
        this.samplePartitionCountRead = true;
    L45:
        int r13 = this.samplePartitionCount * 4;
        this.scratch.reset(r13);
        r20.readFully(this.scratch.getData(), 0, r13);
        this.sampleBytesRead += r13;
        short r14 = (short) ((this.samplePartitionCount / 2) + 1);
        int r15 = (r14 * 6) + 2;
        if (this.encryptionSubsampleDataBuffer != null) goto L48;
    L49:
        this.encryptionSubsampleDataBuffer = ByteBuffer.allocate(r15);
    L50:
        this.encryptionSubsampleDataBuffer.position(0);
        this.encryptionSubsampleDataBuffer.putShort(r14);
        int r17 = 0;
        int r18 = 0;
    L51:
        r16 = r7;
        if (r18 >= this.samplePartitionCount) goto L58;
        int r9 = r17;
        r17 = this.scratch.readUnsignedIntToInt();
        if ((r18 % 2) != 0) goto L56;
        this.encryptionSubsampleDataBuffer.putShort((short) (r17 - r9));
    L57:
        r18 = r18 + 1;
        r7 = r16;
        goto L51
    L56:
        this.encryptionSubsampleDataBuffer.putInt(r17 - r9);
        goto L57
    L58:
        int r19 = (r22 - this.sampleBytesRead) - r17;
        if ((this.samplePartitionCount % 2) != 1) goto L61;
        this.encryptionSubsampleDataBuffer.putInt(r19);
    L62:
        this.encryptionSubsampleData.reset(this.encryptionSubsampleDataBuffer.array(), r15);
        r4.sampleData(this.encryptionSubsampleData, r15, 1);
        this.sampleBytesWritten += r15;
        goto L70
    L61:
        this.encryptionSubsampleDataBuffer.putShort((short) r19);
        this.encryptionSubsampleDataBuffer.putInt(0);
        goto L62
    L48:
        if (this.encryptionSubsampleDataBuffer.capacity() >= r15) goto L50;
    L33:
        r11 = false;
        goto L34
    L28:
        r5 = false;
        goto L29
    L65:
        r16 = 2;
        if (r21.sampleStrippedBytes == null) goto L70;
        this.sampleStrippedBytes.reset(r21.sampleStrippedBytes, r21.sampleStrippedBytes.length);
    L74:
        int r3 = r22 + this.sampleStrippedBytes.limit();
        if (CODEC_ID_H264.equals(r21.codecId) == false) goto L77;
    L89:
        byte[] r24 = this.nalLength.getData();
        r24[0] = 0;
        r24[1] = 0;
        r24[2] = 0;
        int r25 = r21.nalUnitLengthFieldLength;
        int r26 = 4 - r21.nalUnitLengthFieldLength;
    L91:
        if (this.sampleBytesRead >= r3) goto L98;
        if (this.sampleCurrentNalBytesRemaining == 0) goto L94;
        int r27 = writeToOutput(r20, r4, this.sampleCurrentNalBytesRemaining);
        this.sampleBytesRead += r27;
        this.sampleBytesWritten += r27;
        this.sampleCurrentNalBytesRemaining -= r27;
        goto L91
    L94:
        writeToTarget(r20, r24, r26, r25);
        this.sampleBytesRead += r25;
        this.nalLength.setPosition(0);
        this.sampleCurrentNalBytesRemaining = this.nalLength.readUnsignedIntToInt();
        this.nalStartCode.setPosition(0);
        r4.sampleData(this.nalStartCode, 4);
        this.sampleBytesWritten += 4;
    L98:
        if (CODEC_ID_VORBIS.equals(r21.codecId) == false) goto L101;
        this.vorbisNumPageSamples.setPosition(0);
        r4.sampleData(this.vorbisNumPageSamples, 4);
        this.sampleBytesWritten += 4;
    L101:
        return finishWriteSampleData();
    L77:
        if (CODEC_ID_H265.equals(r21.codecId) == true) goto L89;
        if (r21.trueHdSampleRechunker == null) goto L87;
        if (this.sampleStrippedBytes.limit() == 0) goto L85;
        r8 = false;
    L85:
        Assertions.checkState(r8);
        r21.trueHdSampleRechunker.startSample(r20);
    L87:
        if (this.sampleBytesRead >= r3) goto L98;
        int r28 = writeToOutput(r20, r4, r3 - this.sampleBytesRead);
        this.sampleBytesRead += r28;
        this.sampleBytesWritten += r28;
        goto L87
    }

    private void writeSubtitleSampleData(ExtractorInput r5, byte[] r6, int r7) throws IOException {
        int r0 = r6.length + r7;
        if (this.subtitleSample.capacity() >= r0) goto L5;
        this.subtitleSample.reset(Arrays.copyOf(r6, r0 + r7));
    L6:
        r5.readFully(this.subtitleSample.getData(), r6.length, r7);
        this.subtitleSample.setPosition(0);
        this.subtitleSample.setLimit(r0);
        return;
    L5:
        System.arraycopy(r6, 0, this.subtitleSample.getData(), 0, r6.length);
        goto L6
    }

    private int writeToOutput(ExtractorInput r4, TrackOutput r5, int r6) throws IOException {
        int r0 = this.sampleStrippedBytes.bytesLeft();
        if (r0 <= 0) goto L6;
        int r1 = Math.min(r6, r0);
        r5.sampleData(this.sampleStrippedBytes, r1);
        return r1;
    L6:
        return r5.sampleData(r4, r6, false);
    }

    private void writeToTarget(ExtractorInput r4, byte[] r5, int r6, int r7) throws IOException {
        int r0 = Math.min(r7, this.sampleStrippedBytes.bytesLeft());
        r4.readFully(r5, r6 + r0, r7 - r0);
        if (r0 <= 0) goto L6;
        this.sampleStrippedBytes.readBytes(r5, r6, r0);
        return;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void binaryElement(int r28, int r29, ExtractorInput r30) throws IOException {
        int r10 = 0;
        int r11 = 1;
        switch(r28) {
            case 161: goto L16;
            case 163: goto L16;
            case 165: goto L13;
            case 16877: goto L11;
            case 16981: goto L10;
            case 18402: goto L9;
            case 21419: goto L8;
            case 25506: goto L7;
            case 30322: goto L6;
            default: goto L5;
        };
    L6:
        assertInTrackEntry(r28);
        this.currentTrack.projectionData = new byte[r29];
        r30.readFully(this.currentTrack.projectionData, 0, r29);
        return;
    L7:
        assertInTrackEntry(r28);
        this.currentTrack.codecPrivate = new byte[r29];
        r30.readFully(this.currentTrack.codecPrivate, 0, r29);
        return;
    L8:
        Arrays.fill(this.seekEntryIdBytes.getData(), (byte) 0);
        r30.readFully(this.seekEntryIdBytes.getData(), 4 - r29, r29);
        this.seekEntryIdBytes.setPosition(0);
        this.seekEntryId = (int) this.seekEntryIdBytes.readUnsignedInt();
        return;
    L9:
        byte[] r1 = new byte[r29];
        r30.readFully(r1, 0, r29);
        getCurrentTrack(r28).cryptoData = new TrackOutput.CryptoData(1, r1, 0, 0);
        return;
    L10:
        assertInTrackEntry(r28);
        this.currentTrack.sampleStrippedBytes = new byte[r29];
        r30.readFully(this.currentTrack.sampleStrippedBytes, 0, r29);
        return;
    L11:
        handleBlockAddIDExtraData(getCurrentTrack(r28), r30, r29);
        return;
    L16:
        int r4 = 8;
        if (this.blockState != 0) goto L19;
        this.blockTrackNumber = (int) this.varintReader.readUnsignedVarint(r30, false, true, 8);
        this.blockTrackNumberLength = this.varintReader.getLastLength();
        this.blockDurationUs = C0180C.TIME_UNSET;
        this.blockState = 1;
        this.scratch.reset(0);
    L19:
        Track r3 = this.tracks.get(this.blockTrackNumber);
        if (r3 != null) goto L23;
        r30.skipFully(r29 - this.blockTrackNumberLength);
        this.blockState = 0;
        return;
    L23:
        Track.access$100(r3);
        if (this.blockState != 1) goto L89;
        readScratch(r30, 3);
        int r12 = (this.scratch.getData()[2] & 6) >> 1;
        if (r12 != 0) goto L28;
        this.blockSampleCount = 1;
        this.blockSampleSizes = ensureArrayCapacity(this.blockSampleSizes, 1);
        this.blockSampleSizes[0] = (r29 - this.blockTrackNumberLength) - 3;
        int r19 = 0;
        int r16 = 1;
        Track r13 = r3;
    L74:
        this.blockTimeUs = this.clusterTimecodeUs + scaleTimecodeToUs((this.scratch.getData()[r19] << 8) | (this.scratch.getData()[r16] & 255));
        if (r13.type != 2) goto L77;
    L82:
        int r2 = r16;
    L83:
        if (r2 == 0) goto L85;
        int r5 = r16;
    L86:
        this.blockFlags = r5;
        this.blockState = 2;
        this.blockSampleIndex = r19;
    L91:
        if (r28 != ID_SIMPLE_BLOCK) goto L97;
    L93:
        if (this.blockSampleIndex >= this.blockSampleCount) goto L95;
        Track r6 = r13;
        commitSampleToOutput(r6, this.blockTimeUs + ((long) ((this.blockSampleIndex * r13.defaultSampleDurationNs) / 1000)), this.blockFlags, writeSampleData(r30, r13, this.blockSampleSizes[this.blockSampleIndex], false), 0);
        this.blockSampleIndex++;
        goto L93
    L95:
        this.blockState = 0;
        return;
    L97:
        if (this.blockSampleIndex >= this.blockSampleCount) goto L120;
        boolean r7 = r16;
        this.blockSampleSizes[this.blockSampleIndex] = writeSampleData(r30, r13, this.blockSampleSizes[this.blockSampleIndex], r7);
        this.blockSampleIndex += r7 ? 1 : 0;
        goto L97
    L120:
        return;
    L85:
        r5 = r19;
        goto L86
    L77:
        if (r28 == ID_SIMPLE_BLOCK) goto L79;
    L81:
        r2 = r19;
        goto L83
    L79:
        if ((this.scratch.getData()[2] & 128) != 128) goto L81;
    L28:
        readScratch(r30, 4);
        this.blockSampleCount = (this.scratch.getData()[3] & 255) + 1;
        this.blockSampleSizes = ensureArrayCapacity(this.blockSampleSizes, this.blockSampleCount);
        if (r12 != 2) goto L31;
        Arrays.fill(this.blockSampleSizes, 0, this.blockSampleCount, ((r29 - this.blockTrackNumberLength) - 4) / this.blockSampleCount);
        r19 = 0;
        r16 = 1;
        r13 = r3;
        goto L74
    L31:
        if (r12 != 1) goto L41;
        int r8 = 0;
        int r9 = 4;
        int r14 = 0;
    L34:
        if (r14 >= (this.blockSampleCount - r11)) goto L40;
        this.blockSampleSizes[r14] = 0;
    L36:
        r9 = r9 + r11;
        readScratch(r30, r9);
        int r15 = this.scratch.getData()[r9 - 1] & 255;
        int r17 = r11;
        int[] r18 = this.blockSampleSizes;
        r18[r14] = r18[r14] + r15;
        if (r15 != 255) goto L38;
        r11 = r17;
        goto L36
    L38:
        r8 = r8 + this.blockSampleSizes[r14];
        r14 = r14 + 1;
        r11 = r17;
        goto L34
    L40:
        r16 = r11;
        this.blockSampleSizes[this.blockSampleCount - 1] = ((r29 - this.blockTrackNumberLength) - r9) - r8;
        r19 = 0;
        r13 = r3;
        goto L74
    L41:
        r16 = 1;
        if (r12 != 3) goto L88;
        int r20 = 0;
        int r110 = 4;
        int r111 = 0;
    L45:
        if (r111 >= (this.blockSampleCount - 1)) goto L73;
        this.blockSampleSizes[r111] = r10;
        r110 = r110 + 1;
        readScratch(r30, r110);
        if (this.scratch.getData()[r110 - 1] == 0) goto L72;
        int r112 = 0;
    L49:
        if (r112 >= r4) goto L59;
        int r21 = r4;
        int r22 = 1 << (7 - r112);
        int r113 = r10;
        if ((this.scratch.getData()[r110 - 1] & r22) != 0) goto L52;
        r112 = r112 + 1;
        r10 = r113;
        r4 = r21;
        goto L49
    L52:
        int r114 = r110 - 1;
        r110 = r110 + r112;
        readScratch(r30, r110);
        int r23 = r114 + 1;
        int r24 = (this.scratch.getData()[r114] & 255) & (~r22);
        Track r115 = r3;
        long r116 = r24;
        int r25 = r23;
    L53:
        if (r25 >= r110) goto L55;
        r116 = (r116 << r21) | ((long) (this.scratch.getData()[r25] & 255));
        r25 = r25 + 1;
        goto L53
    L55:
        if (r111 <= 0) goto L57;
        long r26 = r116 - ((1 << ((r112 * 7) + 6)) - 1);
    L61:
        if (r26 < (-2147483648L)) goto L70;
        if (r26 > 2147483647L) goto L70;
        int r27 = (int) r26;
        int[] r31 = this.blockSampleSizes;
        if (r111 != 0) goto L67;
        int r117 = r27;
    L68:
        r31[r111] = r117;
        r20 = r20 + this.blockSampleSizes[r111];
        r111 = r111 + 1;
        r3 = r115;
        r10 = r113;
        r4 = r21;
        goto L45
    L67:
        r117 = this.blockSampleSizes[r111 - 1] + r27;
    L70:
        throw ParserException.createForMalformedContainer("EBML lacing sample size out of range.", null);
    L57:
        r26 = r116;
        goto L61
    L59:
        r21 = r4;
        r113 = r10;
        r115 = r3;
        r26 = 0;
        goto L61
    L72:
        throw ParserException.createForMalformedContainer("No valid varint length mask found", null);
    L73:
        r19 = r10;
        r13 = r3;
        this.blockSampleSizes[this.blockSampleCount - 1] = ((r29 - this.blockTrackNumberLength) - r110) - r20;
        goto L74
    L88:
        throw ParserException.createForMalformedContainer("Unexpected lacing value: " + r12, null);
    L89:
        r13 = r3;
        r16 = 1;
        goto L91
    L5:
        throw ParserException.createForMalformedContainer("Unexpected id: " + r28, null);
    L13:
        if (this.blockState == 2) goto L15;
        return;
    L15:
        handleBlockAdditionalData(this.tracks.get(this.blockTrackNumber), this.blockAdditionalId, r30, r29);
    }

    protected void endMasterElement(int r11) throws ParserException {
        assertInitialized();
        switch(r11) {
            case 160: goto L56;
            case 174: goto L47;
            case 19899: goto L38;
            case 25152: goto L29;
            case 28032: goto L21;
            case 357149030: goto L15;
            case 374648427: goto L10;
            case 475249515: goto L6;
            default: goto L84;
        };
    L21:
        assertInTrackEntry(r11);
        if (this.currentTrack.hasContentEncryption == false) goto L89;
        if (this.currentTrack.sampleStrippedBytes == null) goto L89;
        throw ParserException.createForMalformedContainer("Combining encryption and compression is not supported", null);
    L89:
        return;
    L29:
        assertInTrackEntry(r11);
        if (this.currentTrack.hasContentEncryption == false) goto L91;
        if (this.currentTrack.cryptoData == null) goto L35;
        this.currentTrack.drmInitData = new DrmInitData(new DrmInitData.SchemeData[]{new DrmInitData.SchemeData(C0180C.UUID_NIL, MimeTypes.VIDEO_WEBM, this.currentTrack.cryptoData.encryptionKey)});
        return;
    L35:
        throw ParserException.createForMalformedContainer("Encrypted Track found but ContentEncKeyID was not found", null);
    L91:
        return;
    L47:
        Track r0 = (Track) Assertions.checkStateNotNull(this.currentTrack);
        if (r0.codecId == null) goto L54;
        if (isCodecSupported(r0.codecId) == false) goto L52;
        r0.initializeOutput(this.extractorOutput, r0.number);
        this.tracks.put(r0.number, r0);
    L52:
        this.currentTrack = null;
        return;
    L54:
        throw ParserException.createForMalformedContainer("CodecId is missing in TrackEntry element", null);
    L84:
        return;
    L6:
        if (this.sentSeekMap == true) goto L8;
        this.extractorOutput.seekMap(buildSeekMap(this.cueTimesUs, this.cueClusterPositions));
        this.sentSeekMap = true;
    L8:
        this.cueTimesUs = null;
        this.cueClusterPositions = null;
        return;
    L10:
        if (this.tracks.size() == 0) goto L13;
        this.extractorOutput.endTracks();
        return;
    L13:
        throw ParserException.createForMalformedContainer("No valid tracks were found", null);
    L15:
        if (this.timecodeScale != C0180C.TIME_UNSET) goto L18;
        this.timecodeScale = 1000000;
    L18:
        if (this.durationTimecode == C0180C.TIME_UNSET) goto L88;
        this.durationUs = scaleTimecodeToUs(this.durationTimecode);
        return;
    L88:
        return;
    L38:
        if (this.seekEntryId == (-1)) goto L46;
        if (this.seekEntryPosition == (-1)) goto L46;
        if (this.seekEntryId != ID_CUES) goto L93;
        this.cuesContentPosition = this.seekEntryPosition;
        return;
    L93:
        return;
    L46:
        throw ParserException.createForMalformedContainer("Mandatory element SeekID or SeekPosition not found", null);
    L56:
        if (this.blockState == 2) goto L58;
        return;
    L58:
        Track r3 = this.tracks.get(this.blockTrackNumber);
        Track.access$100(r3);
        if (this.blockGroupDiscardPaddingNs > 0) goto L61;
    L63:
        int r1 = 0;
        int r2 = 0;
    L65:
        if (r2 >= this.blockSampleCount) goto L67;
        r1 = r1 + this.blockSampleSizes[r2];
        r2 = r2 + 1;
        goto L65
    L67:
        int r9 = 0;
    L69:
        if (r9 >= this.blockSampleCount) goto L77;
        long r4 = this.blockTimeUs + ((long) ((r3.defaultSampleDurationNs * r9) / 1000));
        int r5 = this.blockFlags;
        if (r9 != 0) goto L76;
        if (this.blockHasReferenceBlock == true) goto L76;
        r5 = r5 | 1;
    L76:
        int r7 = this.blockSampleSizes[r9];
        int r8 = r1 - r7;
        commitSampleToOutput(r3, r4, r5, r7, r8);
        r9 = r9 + 1;
        r1 = r8;
        goto L69
    L77:
        this.blockState = 0;
        return;
    L61:
        if (CODEC_ID_OPUS.equals(r3.codecId) == false) goto L63;
        this.supplementalData.reset(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(this.blockGroupDiscardPaddingNs).array());
        goto L63
    }

    protected void floatElement(int r3, double r4) throws ParserException {
        switch(r3) {
            case 181: goto L18;
            case 17545: goto L17;
            case 21969: goto L16;
            case 21970: goto L15;
            case 21971: goto L14;
            case 21972: goto L13;
            case 21973: goto L12;
            case 21974: goto L11;
            case 21975: goto L10;
            case 21976: goto L9;
            case 21977: goto L8;
            case 21978: goto L7;
            case 30323: goto L6;
            case 30324: goto L5;
            case 30325: goto L4;
            default: goto L34;
        };
    L4:
        float r1 = (float) r4;
        getCurrentTrack(r3).projectionPoseRoll = r1;
        return;
    L5:
        float r2 = (float) r4;
        getCurrentTrack(r3).projectionPosePitch = r2;
        return;
    L6:
        float r5 = (float) r4;
        getCurrentTrack(r3).projectionPoseYaw = r5;
        return;
    L7:
        float r6 = (float) r4;
        getCurrentTrack(r3).minMasteringLuminance = r6;
        return;
    L8:
        float r7 = (float) r4;
        getCurrentTrack(r3).maxMasteringLuminance = r7;
        return;
    L9:
        float r8 = (float) r4;
        getCurrentTrack(r3).whitePointChromaticityY = r8;
        return;
    L10:
        float r9 = (float) r4;
        getCurrentTrack(r3).whitePointChromaticityX = r9;
        return;
    L11:
        float r10 = (float) r4;
        getCurrentTrack(r3).primaryBChromaticityY = r10;
        return;
    L12:
        float r11 = (float) r4;
        getCurrentTrack(r3).primaryBChromaticityX = r11;
        return;
    L13:
        float r12 = (float) r4;
        getCurrentTrack(r3).primaryGChromaticityY = r12;
        return;
    L14:
        float r13 = (float) r4;
        getCurrentTrack(r3).primaryGChromaticityX = r13;
        return;
    L15:
        float r14 = (float) r4;
        getCurrentTrack(r3).primaryRChromaticityY = r14;
        return;
    L16:
        float r15 = (float) r4;
        getCurrentTrack(r3).primaryRChromaticityX = r15;
        return;
    L17:
        this.durationTimecode = (long) r4;
        return;
    L18:
        int r16 = (int) r4;
        getCurrentTrack(r3).sampleRate = r16;
        return;
    }

    protected Track getCurrentTrack(int r2) throws ParserException {
        assertInTrackEntry(r2);
        return this.currentTrack;
    }

    protected int getElementType(int r2) {
        switch(r2) {
            case 131: goto L13;
            case 134: goto L11;
            case 136: goto L13;
            case 155: goto L13;
            case 159: goto L13;
            case 160: goto L9;
            case 161: goto L7;
            case 163: goto L7;
            case 165: goto L7;
            case 166: goto L9;
            case 174: goto L9;
            case 176: goto L13;
            case 179: goto L13;
            case 181: goto L5;
            case 183: goto L9;
            case 186: goto L13;
            case 187: goto L9;
            case 215: goto L13;
            case 224: goto L9;
            case 225: goto L9;
            case 231: goto L13;
            case 238: goto L13;
            case 241: goto L13;
            case 251: goto L13;
            case 16868: goto L9;
            case 16871: goto L13;
            case 16877: goto L7;
            case 16980: goto L13;
            case 16981: goto L7;
            case 17026: goto L11;
            case 17029: goto L13;
            case 17143: goto L13;
            case 17545: goto L5;
            case 18401: goto L13;
            case 18402: goto L7;
            case 18407: goto L9;
            case 18408: goto L13;
            case 19899: goto L9;
            case 20529: goto L13;
            case 20530: goto L13;
            case 20532: goto L9;
            case 20533: goto L9;
            case 21358: goto L11;
            case 21419: goto L7;
            case 21420: goto L13;
            case 21432: goto L13;
            case 21680: goto L13;
            case 21682: goto L13;
            case 21690: goto L13;
            case 21930: goto L13;
            case 21936: goto L9;
            case 21938: goto L13;
            case 21945: goto L13;
            case 21946: goto L13;
            case 21947: goto L13;
            case 21948: goto L13;
            case 21949: goto L13;
            case 21968: goto L9;
            case 21969: goto L5;
            case 21970: goto L5;
            case 21971: goto L5;
            case 21972: goto L5;
            case 21973: goto L5;
            case 21974: goto L5;
            case 21975: goto L5;
            case 21976: goto L5;
            case 21977: goto L5;
            case 21978: goto L5;
            case 21998: goto L13;
            case 22186: goto L13;
            case 22203: goto L13;
            case 25152: goto L9;
            case 25188: goto L13;
            case 25506: goto L7;
            case 28032: goto L9;
            case 30113: goto L9;
            case 30114: goto L13;
            case 30320: goto L9;
            case 30321: goto L13;
            case 30322: goto L7;
            case 30323: goto L5;
            case 30324: goto L5;
            case 30325: goto L5;
            case 2274716: goto L11;
            case 2352003: goto L13;
            case 2807729: goto L13;
            case 290298740: goto L9;
            case 357149030: goto L9;
            case 374648427: goto L9;
            case 408125543: goto L9;
            case 440786851: goto L9;
            case 475249515: goto L9;
            case 524531317: goto L9;
            default: goto L3;
        };
    L3:
        return 0;
    L5:
        return 5;
    L7:
        return 4;
    L9:
        return 1;
    L11:
        return 3;
    L13:
        return 2;
    }

    @Override // androidx.media3.extractor.Extractor
    public /* synthetic */ List getSniffFailureDetails() {
        return Extractor.CC.$default$getSniffFailureDetails(this);
    }

    @Override // androidx.media3.extractor.Extractor
    public /* synthetic */ Extractor getUnderlyingImplementation() {
        return Extractor.CC.$default$getUnderlyingImplementation(this);
    }

    protected void handleBlockAddIDExtraData(Track r3, ExtractorInput r4, int r5) throws IOException {
        if (Track.access$200(r3) != 1685485123) goto L5;
    L8:
        r3.dolbyVisionConfigBytes = new byte[r5];
        r4.readFully(r3.dolbyVisionConfigBytes, 0, r5);
        return;
    L5:
        if (Track.access$200(r3) == 1685480259) goto L8;
        r4.skipFully(r5);
    }

    protected void handleBlockAdditionalData(Track r3, int r4, ExtractorInput r5, int r6) throws IOException {
        if (r4 == 4) goto L5;
    L7:
        r5.skipFully(r6);
        return;
    L5:
        if (CODEC_ID_VP9.equals(r3.codecId) == false) goto L7;
        this.supplementalData.reset(r6);
        r5.readFully(this.supplementalData.getData(), 0, r6);
    }

    @Override // androidx.media3.extractor.Extractor
    public final void init(ExtractorOutput r3) {
        if (this.parseSubtitlesDuringExtraction == false) goto L5;
        ExtractorOutput r0 = new SubtitleTranscodingExtractorOutput(r3, this.subtitleParserFactory);
    L6:
        this.extractorOutput = r0;
        return;
    L5:
        r0 = r3;
        goto L6
    }

    protected void integerElement(int r10, long r11) throws ParserException {
        boolean r3 = false;
        switch(r10) {
            case 131: goto L102;
            case 136: goto L98;
            case 155: goto L97;
            case 159: goto L96;
            case 176: goto L95;
            case 179: goto L94;
            case 186: goto L93;
            case 215: goto L92;
            case 231: goto L91;
            case 238: goto L90;
            case 241: goto L88;
            case 251: goto L86;
            case 16871: goto L85;
            case 16980: goto L81;
            case 17029: goto L74;
            case 17143: goto L69;
            case 18401: goto L64;
            case 18408: goto L59;
            case 20529: goto L54;
            case 20530: goto L49;
            case 21420: goto L47;
            case 21432: goto L40;
            case 21680: goto L39;
            case 21682: goto L38;
            case 21690: goto L37;
            case 21930: goto L33;
            case 21938: goto L32;
            case 21945: goto L27;
            case 21946: goto L24;
            case 21947: goto L21;
            case 21948: goto L20;
            case 21949: goto L19;
            case 21998: goto L18;
            case 22186: goto L17;
            case 22203: goto L16;
            case 25188: goto L15;
            case 30114: goto L14;
            case 30321: goto L7;
            case 2352003: goto L6;
            case 2807729: goto L5;
            default: goto L146;
        };
    L5:
        this.timecodeScale = r11;
        return;
    L6:
        getCurrentTrack(r10).defaultSampleDurationNs = (int) r11;
        return;
    L7:
        assertInTrackEntry(r10);
        switch(((int) r11)) {
            case 0: goto L13;
            case 1: goto L12;
            case 2: goto L11;
            case 3: goto L10;
            default: goto L147;
        };
    L10:
        this.currentTrack.projectionType = 3;
        return;
    L11:
        this.currentTrack.projectionType = 2;
        return;
    L12:
        this.currentTrack.projectionType = 1;
        return;
    L13:
        this.currentTrack.projectionType = 0;
        return;
    L147:
        return;
    L14:
        this.blockGroupDiscardPaddingNs = r11;
        return;
    L15:
        getCurrentTrack(r10).audioBitDepth = (int) r11;
        return;
    L16:
        getCurrentTrack(r10).seekPreRollNs = r11;
        return;
    L17:
        getCurrentTrack(r10).codecDelayNs = r11;
        return;
    L18:
        getCurrentTrack(r10).maxBlockAdditionId = (int) r11;
        return;
    L19:
        getCurrentTrack(r10).maxFrameAverageLuminance = (int) r11;
        return;
    L20:
        getCurrentTrack(r10).maxContentLuminance = (int) r11;
        return;
    L21:
        assertInTrackEntry(r10);
        this.currentTrack.hasColorInfo = true;
        int r0 = ColorInfo.isoColorPrimariesToColorSpace((int) r11);
        if (r0 == (-1)) goto L117;
        this.currentTrack.colorSpace = r0;
        return;
    L117:
        return;
    L24:
        assertInTrackEntry(r10);
        int r1 = ColorInfo.isoTransferCharacteristicsToColorTransfer((int) r11);
        if (r1 == (-1)) goto L119;
        this.currentTrack.colorTransfer = r1;
        return;
    L119:
        return;
    L27:
        assertInTrackEntry(r10);
        switch(((int) r11)) {
            case 1: goto L31;
            case 2: goto L30;
            default: goto L148;
        };
    L30:
        this.currentTrack.colorRange = 1;
        return;
    L31:
        this.currentTrack.colorRange = 2;
        return;
    L148:
        return;
    L32:
        assertInTrackEntry(r10);
        this.currentTrack.hasColorInfo = true;
        this.currentTrack.bitsPerChannel = (int) r11;
        return;
    L33:
        Track r2 = getCurrentTrack(r10);
        if (r11 != 1) goto L36;
        r3 = true;
    L36:
        r2.flagForced = r3;
        return;
    L37:
        getCurrentTrack(r10).displayHeight = (int) r11;
        return;
    L38:
        getCurrentTrack(r10).displayUnit = (int) r11;
        return;
    L39:
        getCurrentTrack(r10).displayWidth = (int) r11;
        return;
    L40:
        int r4 = (int) r11;
        assertInTrackEntry(r10);
        switch(r4) {
            case 0: goto L46;
            case 1: goto L45;
            case 3: goto L44;
            case 15: goto L43;
            default: goto L149;
        };
    L43:
        this.currentTrack.stereoMode = 3;
        return;
    L44:
        this.currentTrack.stereoMode = 1;
        return;
    L45:
        this.currentTrack.stereoMode = 2;
        return;
    L46:
        this.currentTrack.stereoMode = 0;
        return;
    L149:
        return;
    L47:
        this.seekEntryPosition = this.segmentContentPosition + r11;
        return;
    L85:
        Track.access$202(getCurrentTrack(r10), (int) r11);
        return;
    L86:
        this.blockHasReferenceBlock = true;
        return;
    L90:
        this.blockAdditionalId = (int) r11;
        return;
    L91:
        this.clusterTimecodeUs = scaleTimecodeToUs(r11);
        return;
    L92:
        getCurrentTrack(r10).number = (int) r11;
        return;
    L93:
        getCurrentTrack(r10).height = (int) r11;
        return;
    L94:
        assertInCues(r10);
        this.cueTimesUs.add(scaleTimecodeToUs(r11));
        return;
    L95:
        getCurrentTrack(r10).width = (int) r11;
        return;
    L96:
        getCurrentTrack(r10).channelCount = (int) r11;
        return;
    L97:
        this.blockDurationUs = scaleTimecodeToUs(r11);
        return;
    L98:
        Track r5 = getCurrentTrack(r10);
        if (r11 != 1) goto L101;
        r3 = true;
    L101:
        r5.flagDefault = r3;
        return;
    L102:
        getCurrentTrack(r10).type = (int) r11;
        return;
    L146:
        return;
    L49:
        if (r11 != 1) goto L52;
        return;
    L52:
        throw ParserException.createForMalformedContainer("ContentEncodingScope " + r11 + " not supported", null);
    L54:
        if (r11 != 0) goto L57;
        return;
    L57:
        throw ParserException.createForMalformedContainer("ContentEncodingOrder " + r11 + " not supported", null);
    L59:
        if (r11 != 1) goto L62;
        return;
    L62:
        throw ParserException.createForMalformedContainer("AESSettingsCipherMode " + r11 + " not supported", null);
    L64:
        if (r11 != 5) goto L67;
        return;
    L67:
        throw ParserException.createForMalformedContainer("ContentEncAlgo " + r11 + " not supported", null);
    L69:
        if (r11 != 1) goto L72;
        return;
    L72:
        throw ParserException.createForMalformedContainer("EBMLReadVersion " + r11 + " not supported", null);
    L74:
        if (r11 < 1) goto L79;
        if (r11 > 2) goto L79;
        return;
    L79:
        throw ParserException.createForMalformedContainer("DocTypeReadVersion " + r11 + " not supported", null);
    L81:
        if (r11 != 3) goto L84;
        return;
    L84:
        throw ParserException.createForMalformedContainer("ContentCompAlgo " + r11 + " not supported", null);
    L88:
        if (this.seenClusterPositionForCurrentCuePoint == true) goto L135;
        assertInCues(r10);
        this.cueClusterPositions.add(r11);
        this.seenClusterPositionForCurrentCuePoint = true;
        return;
    }

    protected boolean isLevel1Element(int r2) {
        if (r2 != 357149030) goto L5;
    L12:
        return true;
    L5:
        if (r2 == ID_CLUSTER) goto L12;
        if (r2 == ID_CUES) goto L12;
        if (r2 == ID_TRACKS) goto L12;
        return false;
    }

    @Override // androidx.media3.extractor.Extractor
    public final int read(ExtractorInput r5, PositionHolder r6) throws IOException {
        this.haveOutputSample = false;
        boolean r1 = true;
    L3:
        if (r1 == false) goto L12;
        if (this.haveOutputSample == true) goto L12;
        r1 = this.reader.read(r5);
        if (r1 == false) goto L3;
        if (maybeSeekForCues(r6, r5.getPosition()) == false) goto L3;
        return 1;
    L12:
        if (r1 == true) goto L19;
        int r0 = 0;
    L15:
        if (r0 >= this.tracks.size()) goto L17;
        Track r2 = this.tracks.valueAt(r0);
        Track.access$100(r2);
        r2.outputPendingSampleMetadata();
        r0 = r0 + 1;
        goto L15
    L17:
        return -1;
    L19:
        return 0;
    }

    @Override // androidx.media3.extractor.Extractor
    public final void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r3, long r5) {
        this.clusterTimecodeUs = C0180C.TIME_UNSET;
        this.blockState = 0;
        this.reader.reset();
        this.varintReader.reset();
        resetWriteSampleData();
        int r0 = 0;
    L4:
        if (r0 >= this.tracks.size()) goto L6;
        this.tracks.valueAt(r0).reset();
        r0 = r0 + 1;
        goto L4
    }

    @Override // androidx.media3.extractor.Extractor
    public final boolean sniff(ExtractorInput r2) throws IOException {
        return new Sniffer().sniff(r2);
    }

    protected void startMasterElement(int r7, long r8, long r10) throws ParserException {
        assertInitialized();
        switch(r7) {
            case 160: goto L28;
            case 174: goto L27;
            case 187: goto L26;
            case 19899: goto L25;
            case 20533: goto L24;
            case 21968: goto L23;
            case 25152: goto L40;
            case 408125543: goto L15;
            case 475249515: goto L13;
            case 524531317: goto L6;
            default: goto L40;
        };
    L13:
        this.cueTimesUs = new LongArray();
        this.cueClusterPositions = new LongArray();
        return;
    L23:
        getCurrentTrack(r7).hasColorInfo = true;
        return;
    L24:
        getCurrentTrack(r7).hasContentEncryption = true;
        return;
    L25:
        this.seekEntryId = -1;
        this.seekEntryPosition = -1;
        return;
    L26:
        this.seenClusterPositionForCurrentCuePoint = false;
        return;
    L27:
        this.currentTrack = new Track();
        return;
    L28:
        this.blockHasReferenceBlock = false;
        this.blockGroupDiscardPaddingNs = 0;
        return;
    L40:
        return;
    L6:
        if (this.sentSeekMap == false) goto L8;
        return;
    L8:
        if (this.seekForCuesEnabled == true) goto L10;
    L12:
        this.extractorOutput.seekMap(new SeekMap.Unseekable(this.durationUs));
        this.sentSeekMap = true;
        return;
    L10:
        if (this.cuesContentPosition == (-1)) goto L12;
        this.seekForCues = true;
        return;
    L15:
        if (this.segmentContentPosition != (-1)) goto L17;
    L21:
        this.segmentContentPosition = r8;
        this.segmentContentSize = r10;
        return;
    L17:
        if (this.segmentContentPosition == r8) goto L21;
        throw ParserException.createForMalformedContainer("Multiple Segment elements not supported", null);
    }

    protected void stringElement(int r3, String r4) throws ParserException {
        switch(r3) {
            case 134: goto L13;
            case 17026: goto L7;
            case 21358: goto L5;
            case 2274716: goto L4;
            default: goto L18;
        };
    L4:
        Track.access$302(getCurrentTrack(r3), r4);
        return;
    L5:
        getCurrentTrack(r3).name = r4;
        return;
    L13:
        getCurrentTrack(r3).codecId = r4;
        return;
    L18:
        return;
    L7:
        if (DOC_TYPE_WEBM.equals(r4) == false) goto L9;
        return;
    L9:
        if (DOC_TYPE_MATROSKA.equals(r4) == false) goto L12;
        return;
    L12:
        throw ParserException.createForMalformedContainer("DocType " + r4 + " not supported", null);
    }
}
