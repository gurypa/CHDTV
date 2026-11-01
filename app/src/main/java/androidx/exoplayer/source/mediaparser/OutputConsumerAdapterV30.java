package androidx.media3.exoplayer.source.mediaparser;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaParser;
import android.util.Pair;
import androidx.core.os.EnvironmentCompat;
import androidx.media3.common.C0180C;
import androidx.media3.common.DataReader;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.MediaFormatUtil;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.ChunkIndex;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.NoOpExtractorOutput;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.SeekPoint;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.arthenica.ffmpegkit.StreamInformation;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class OutputConsumerAdapterV30 implements MediaParser.OutputConsumer {
    private static final String MEDIA_FORMAT_KEY_CHUNK_INDEX_DURATIONS = "chunk-index-long-us-durations";
    private static final String MEDIA_FORMAT_KEY_CHUNK_INDEX_OFFSETS = "chunk-index-long-offsets";
    private static final String MEDIA_FORMAT_KEY_CHUNK_INDEX_SIZES = "chunk-index-int-sizes";
    private static final String MEDIA_FORMAT_KEY_CHUNK_INDEX_TIMES = "chunk-index-long-us-times";
    private static final String MEDIA_FORMAT_KEY_TRACK_TYPE = "track-type-string";
    private static final Pattern REGEX_CRYPTO_INFO_PATTERN = null;
    private static final Pair<MediaParser.SeekPoint, MediaParser.SeekPoint> SEEK_POINT_PAIR_START = null;
    private static final String TAG = "OConsumerAdapterV30";
    private String containerMimeType;
    private MediaParser.SeekMap dummySeekMap;
    private final boolean expectDummySeekMap;
    private ExtractorOutput extractorOutput;
    private ChunkIndex lastChunkIndex;
    private final ArrayList<TrackOutput.CryptoData> lastOutputCryptoDatas;
    private final ArrayList<MediaCodec.CryptoInfo> lastReceivedCryptoInfos;
    private MediaParser.SeekMap lastSeekMap;
    private List<Format> muxedCaptionFormats;
    private int primaryTrackIndex;
    private final Format primaryTrackManifestFormat;
    private final int primaryTrackType;
    private long sampleTimestampUpperLimitFilterUs;
    private final DataReaderAdapter scratchDataReaderAdapter;
    private boolean seekingDisabled;
    private TimestampAdjuster timestampAdjuster;
    private final ArrayList<Format> trackFormats;
    private final ArrayList<TrackOutput> trackOutputs;
    private boolean tracksEnded;
    private boolean tracksFoundCalled;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.mediaparser.OutputConsumerAdapterV30$1 */
    static /* synthetic */ class C03531 {
    }

    private static final class DataReaderAdapter implements DataReader {
        public MediaParser.InputReader input;

        private DataReaderAdapter() {
        }

        /* synthetic */ DataReaderAdapter(C03531 r1) {
            this();
        }

        @Override // androidx.media3.common.DataReader
        public int read(byte[] r2, int r3, int r4) throws IOException {
            return ((MediaParser.InputReader) Util.castNonNull(this.input)).read(r2, r3, r4);
        }
    }

    private static final class SeekMapAdapter implements SeekMap {
        private final MediaParser.SeekMap adaptedSeekMap;

        public SeekMapAdapter(MediaParser.SeekMap r1) {
            this.adaptedSeekMap = r1;
        }

        private static SeekPoint asExoPlayerSeekPoint(MediaParser.SeekPoint r5) {
            return new SeekPoint(r5.timeMicros, r5.position);
        }

        @Override // androidx.media3.extractor.SeekMap
        public long getDurationUs() {
            long r0 = this.adaptedSeekMap.getDurationMicros();
            if (r0 != (-2147483648L)) goto L7;
            return C0180C.TIME_UNSET;
        L7:
            return r0;
        }

        @Override // androidx.media3.extractor.SeekMap
        public SeekMap.SeekPoints getSeekPoints(long r5) {
            Pair<MediaParser.SeekPoint, MediaParser.SeekPoint> r0 = this.adaptedSeekMap.getSeekPoints(r5);
            if (r0.first != r0.second) goto L6;
            return new SeekMap.SeekPoints(asExoPlayerSeekPoint((MediaParser.SeekPoint) r0.first));
        L6:
            return new SeekMap.SeekPoints(asExoPlayerSeekPoint((MediaParser.SeekPoint) r0.first), asExoPlayerSeekPoint((MediaParser.SeekPoint) r0.second));
        }

        @Override // androidx.media3.extractor.SeekMap
        public boolean isSeekable() {
            return this.adaptedSeekMap.isSeekable();
        }
    }

    static {
        SEEK_POINT_PAIR_START = Pair.create(MediaParser.SeekPoint.START, MediaParser.SeekPoint.START);
        REGEX_CRYPTO_INFO_PATTERN = Pattern.compile("pattern \\(encrypt: (\\d+), skip: (\\d+)\\)");
    }

    public OutputConsumerAdapterV30() {
        this(null, -2, false);
    }

    public OutputConsumerAdapterV30(Format r3, int r4, boolean r5) {
        this.expectDummySeekMap = r5;
        this.primaryTrackManifestFormat = r3;
        this.primaryTrackType = r4;
        this.trackOutputs = new ArrayList();
        this.trackFormats = new ArrayList();
        this.lastReceivedCryptoInfos = new ArrayList();
        this.lastOutputCryptoDatas = new ArrayList();
        this.scratchDataReaderAdapter = new DataReaderAdapter(null);
        this.extractorOutput = new NoOpExtractorOutput();
        this.sampleTimestampUpperLimitFilterUs = C0180C.TIME_UNSET;
        this.muxedCaptionFormats = ImmutableList.m212of();
    }

    private void ensureSpaceForTrackIndex(int r4) {
        int r0 = this.trackOutputs.size();
    L3:
        if (r0 > r4) goto L5;
        this.trackOutputs.add(null);
        this.trackFormats.add(null);
        this.lastReceivedCryptoInfos.add(null);
        this.lastOutputCryptoDatas.add(null);
        r0 = r0 + 1;
        goto L3
    }

    private static int getFlag(MediaFormat r2, String r3, int r4) {
        if (r2.getInteger(r3, 0) != 0) goto L5;
        return 0;
    L5:
        return r4;
    }

    private static List<byte[]> getInitializationData(MediaFormat r4) {
        ArrayList r0 = new ArrayList();
        int r1 = 0;
    L3:
        int r3 = r1 + 1;
        ByteBuffer r2 = r4.getByteBuffer("csd-" + r1);
        if (r2 == null) goto L5;
        r0.add(MediaFormatUtil.getArray(r2));
        r1 = r3;
        goto L3
    L5:
        return r0;
    }

    private static String getMimeType(String r3) {
        switch(r3.hashCode()) {
            case -2063506020: goto L45;
            case -1870824006: goto L42;
            case -1566427438: goto L39;
            case -900207883: goto L36;
            case -589864617: goto L33;
            case 52265814: goto L30;
            case 116768877: goto L27;
            case 376876796: goto L24;
            case 703268017: goto L21;
            case 768643067: goto L18;
            case 965962719: goto L15;
            case 1264380477: goto L12;
            case 1343957595: goto L9;
            case 2063134683: goto L6;
            default: goto L47;
        };
    L47:
        byte r0 = -1;
    L48:
        switch(r0) {
            case 0: goto L75;
            case 1: goto L73;
            case 2: goto L73;
            case 3: goto L71;
            case 4: goto L69;
            case 5: goto L67;
            case 6: goto L65;
            case 7: goto L63;
            case 8: goto L61;
            case 9: goto L59;
            case 10: goto L57;
            case 11: goto L55;
            case 12: goto L53;
            case 13: goto L51;
            default: goto L50;
        };
    L51:
        return MimeTypes.AUDIO_FLAC;
    L53:
        return MimeTypes.AUDIO_AC4;
    L55:
        return MimeTypes.AUDIO_AMR;
    L57:
        return MimeTypes.AUDIO_RAW;
    L59:
        return MimeTypes.VIDEO_PS;
    L61:
        return MimeTypes.AUDIO_OGG;
    L63:
        return MimeTypes.VIDEO_FLV;
    L65:
        return MimeTypes.VIDEO_MP2T;
    L67:
        return MimeTypes.AUDIO_AC3;
    L69:
        return MimeTypes.AUDIO_AAC;
    L71:
        return MimeTypes.AUDIO_MPEG;
    L73:
        return MimeTypes.VIDEO_MP4;
    L75:
        return MimeTypes.VIDEO_WEBM;
    L50:
        throw new IllegalArgumentException("Illegal parser name: " + r3);
    L6:
        if (r3.equals("android.media.mediaparser.FlvParser") == false) goto L47;
        r0 = 7;
        goto L48
    L9:
        if (r3.equals("android.media.mediaparser.Mp3Parser") == false) goto L47;
        r0 = 3;
        goto L48
    L12:
        if (r3.equals("android.media.mediaparser.Ac4Parser") == false) goto L47;
        r0 = Ascii.f81FF;
        goto L48
    L15:
        if (r3.equals("android.media.mediaparser.MatroskaParser") == false) goto L47;
        r0 = 0;
        goto L48
    L18:
        if (r3.equals("android.media.mediaparser.FlacParser") == false) goto L47;
        r0 = Ascii.f79CR;
        goto L48
    L21:
        if (r3.equals("android.media.mediaparser.AmrParser") == false) goto L47;
        r0 = Ascii.f92VT;
        goto L48
    L24:
        if (r3.equals("android.media.mediaparser.Ac3Parser") == false) goto L47;
        r0 = 5;
        goto L48
    L27:
        if (r3.equals("android.media.mediaparser.FragmentedMp4Parser") == false) goto L47;
        r0 = 1;
        goto L48
    L30:
        if (r3.equals("android.media.mediaparser.PsParser") == false) goto L47;
        r0 = 9;
        goto L48
    L33:
        if (r3.equals("android.media.mediaparser.WavParser") == false) goto L47;
        r0 = 10;
        goto L48
    L36:
        if (r3.equals("android.media.mediaparser.AdtsParser") == false) goto L47;
        r0 = 4;
        goto L48
    L39:
        if (r3.equals("android.media.mediaparser.TsParser") == false) goto L47;
        r0 = 6;
        goto L48
    L42:
        if (r3.equals("android.media.mediaparser.OggParser") == false) goto L47;
        r0 = 8;
        goto L48
    L45:
        if (r3.equals("android.media.mediaparser.Mp4Parser") == false) goto L47;
        r0 = 2;
        goto L48
    }

    private static int getSelectionFlags(MediaFormat r3) {
        return ((0 | getFlag(r3, "is-autoselect", 4)) | getFlag(r3, "is-default", 1)) | getFlag(r3, "is-forced-subtitle", 2);
    }

    private void maybeEndTracks() {
        if (this.tracksFoundCalled == true) goto L5;
        return;
    L5:
        if (this.tracksEnded == true) goto L18;
        int r0 = this.trackOutputs.size();
        int r1 = 0;
    L8:
        if (r1 >= r0) goto L13;
        if (this.trackOutputs.get(r1) == null) goto L11;
        r1 = r1 + 1;
        goto L8
    L11:
        return;
    L13:
        this.extractorOutput.endTracks();
        this.tracksEnded = true;
        return;
    }

    private boolean maybeObtainChunkIndex(MediaFormat r12) {
        ByteBuffer r0 = r12.getByteBuffer(MEDIA_FORMAT_KEY_CHUNK_INDEX_SIZES);
        if (r0 != null) goto L6;
        return false;
    L6:
        IntBuffer r1 = r0.asIntBuffer();
        LongBuffer r2 = ((ByteBuffer) Assertions.checkNotNull(r12.getByteBuffer(MEDIA_FORMAT_KEY_CHUNK_INDEX_OFFSETS))).asLongBuffer();
        LongBuffer r3 = ((ByteBuffer) Assertions.checkNotNull(r12.getByteBuffer(MEDIA_FORMAT_KEY_CHUNK_INDEX_DURATIONS))).asLongBuffer();
        LongBuffer r4 = ((ByteBuffer) Assertions.checkNotNull(r12.getByteBuffer(MEDIA_FORMAT_KEY_CHUNK_INDEX_TIMES))).asLongBuffer();
        int[] r5 = new int[r1.remaining()];
        long[] r6 = new long[r2.remaining()];
        long[] r7 = new long[r3.remaining()];
        long[] r8 = new long[r4.remaining()];
        r1.get(r5);
        r2.get(r6);
        r3.get(r7);
        r4.get(r8);
        this.lastChunkIndex = new ChunkIndex(r5, r6, r7, r8);
        this.extractorOutput.seekMap(this.lastChunkIndex);
        return true;
    }

    private TrackOutput.CryptoData toExoPlayerCryptoData(int r7, MediaCodec.CryptoInfo r8) {
        if (r8 != null) goto L6;
        return null;
    L6:
        if (this.lastReceivedCryptoInfos.get(r7) == r8) goto L16;
        Matcher r1 = REGEX_CRYPTO_INFO_PATTERN.matcher(r8.toString());     // Catch: RuntimeException -> L10
        r1.find();     // Catch: RuntimeException -> L10
        int r2 = Integer.parseInt((String) Util.castNonNull(r1.group(1)));     // Catch: RuntimeException -> L10
        int r3 = Integer.parseInt((String) Util.castNonNull(r1.group(2)));     // Catch: RuntimeException -> L10
    L12:
        TrackOutput.CryptoData r4 = new TrackOutput.CryptoData(r8.mode, r8.key, r2, r3);
        this.lastReceivedCryptoInfos.set(r7, r8);
        this.lastOutputCryptoDatas.set(r7, r4);
        return r4;
    L10:
        e = move-exception;
        Log.m45e(TAG, "Unexpected error while parsing CryptoInfo: " + r8, e);
        r2 = 0;
        r3 = 0;
        goto L12
    L16:
        return (TrackOutput.CryptoData) Assertions.checkNotNull(this.lastOutputCryptoDatas.get(r7));
    }

    private static DrmInitData toExoPlayerDrmInitData(String r7, android.media.DrmInitData r8) {
        if (r8 != null) goto L5;
        return null;
    L5:
        DrmInitData.SchemeData[] r0 = new DrmInitData.SchemeData[r8.getSchemeInitDataCount()];
        int r1 = 0;
    L7:
        if (r1 >= r0.length) goto L10;
        android.media.DrmInitData.SchemeInitData r2 = r8.getSchemeInitDataAt(r1);
        r0[r1] = new DrmInitData.SchemeData(r2.uuid, r2.mimeType, r2.data);
        r1 = r1 + 1;
        goto L7
    L10:
        return new DrmInitData(r7, r0);
    }

    private Format toExoPlayerFormat(MediaParser.TrackData r9) {
        MediaFormat r0 = r9.mediaFormat;
        String r1 = r0.getString("mime");
        int r2 = r0.getInteger("caption-service-number", -1);
        Format.Builder r3 = new Format.Builder().setDrmInitData(toExoPlayerDrmInitData(r0.getString("crypto-mode-fourcc"), r9.drmInitData)).setContainerMimeType(this.containerMimeType).setPeakBitrate(r0.getInteger("bitrate", -1)).setChannelCount(r0.getInteger("channel-count", -1)).setColorInfo(MediaFormatUtil.getColorInfo(r0)).setSampleMimeType(r1).setCodecs(r0.getString("codecs-string")).setFrameRate(r0.getFloat("frame-rate", -1.0f)).setWidth(r0.getInteger(StreamInformation.KEY_WIDTH, -1)).setHeight(r0.getInteger(StreamInformation.KEY_HEIGHT, -1)).setInitializationData(getInitializationData(r0)).setLanguage(r0.getString("language")).setMaxInputSize(r0.getInteger("max-input-size", -1)).setPcmEncoding(r0.getInteger("exo-pcm-encoding", -1)).setRotationDegrees(r0.getInteger("rotation-degrees", 0)).setSampleRate(r0.getInteger("sample-rate", -1)).setSelectionFlags(getSelectionFlags(r0)).setEncoderDelay(r0.getInteger("encoder-delay", 0)).setEncoderPadding(r0.getInteger("encoder-padding", 0)).setPixelWidthHeightRatio(r0.getFloat("pixel-width-height-ratio-float", 1.0f)).setSubsampleOffsetUs(r0.getLong("subsample-offset-us-long", Long.MAX_VALUE)).setAccessibilityChannel(r2);
        int r4 = 0;
    L4:
        if (r4 >= this.muxedCaptionFormats.size()) goto L12;
        Format r5 = this.muxedCaptionFormats.get(r4);
        if (Util.areEqual(r5.sampleMimeType, r1) == false) goto L10;
        if (r5.accessibilityChannel != r2) goto L10;
        r3.setLanguage(r5.language).setRoleFlags(r5.roleFlags).setSelectionFlags(r5.selectionFlags).setLabel(r5.label).setLabels(r5.labels).setMetadata(r5.metadata);
    L10:
        r4 = r4 + 1;
    L12:
        return r3.build();
    }

    private static int toTrackTypeConstant(String r5) {
        if (r5 != null) goto L6;
        return -1;
    L6:
        switch(r5.hashCode()) {
            case -450004177: goto L21;
            case -284840886: goto L18;
            case 3556653: goto L15;
            case 93166550: goto L12;
            case 112202875: goto L9;
            default: goto L23;
        };
    L23:
        byte r1 = -1;
    L24:
        switch(r1) {
            case 0: goto L32;
            case 1: goto L31;
            case 2: goto L30;
            case 3: goto L28;
            case 4: goto L27;
            default: goto L26;
        };
    L27:
        return -1;
    L28:
        return 5;
    L30:
        return 3;
    L31:
        return 2;
    L32:
        return 1;
    L26:
        return MimeTypes.getTrackType(r5);
    L9:
        if (r5.equals(MimeTypes.BASE_TYPE_VIDEO) == false) goto L33;
        r1 = 1;
        goto L24
    L33:
        r1 = -1;
        goto L24
    L12:
        if (r5.equals(MimeTypes.BASE_TYPE_AUDIO) == false) goto L34;
        r1 = 0;
        goto L24
    L34:
        r1 = -1;
        goto L24
    L15:
        if (r5.equals("text") == false) goto L35;
        r1 = 2;
        goto L24
    L35:
        r1 = -1;
        goto L24
    L18:
        if (r5.equals(EnvironmentCompat.MEDIA_UNKNOWN) == false) goto L36;
        r1 = 4;
        goto L24
    L36:
        r1 = -1;
        goto L24
    L21:
        if (r5.equals(TtmlNode.TAG_METADATA) == false) goto L37;
        r1 = 3;
        goto L24
    L37:
        r1 = -1;
        goto L24
    }

    public void disableSeeking() {
        this.seekingDisabled = true;
    }

    public ChunkIndex getChunkIndex() {
        return this.lastChunkIndex;
    }

    public MediaParser.SeekMap getDummySeekMap() {
        return this.dummySeekMap;
    }

    public Format[] getSampleFormats() {
        if (this.tracksFoundCalled == true) goto L6;
        return null;
    L6:
        Format[] r0 = new Format[this.trackFormats.size()];
        int r1 = 0;
    L8:
        if (r1 >= this.trackFormats.size()) goto L10;
        r0[r1] = (Format) Assertions.checkNotNull(this.trackFormats.get(r1));
        r1 = r1 + 1;
        goto L8
    L10:
        return r0;
    }

    public Pair<MediaParser.SeekPoint, MediaParser.SeekPoint> getSeekPoints(long r2) {
        if (this.lastSeekMap == null) goto L6;
        return this.lastSeekMap.getSeekPoints(r2);
    L6:
        return SEEK_POINT_PAIR_START;
    }

    @Override // android.media.MediaParser.OutputConsumer
    public void onSampleCompleted(int r8, long r9, int r11, int r12, int r13, MediaCodec.CryptoInfo r14) {
        if (this.sampleTimestampUpperLimitFilterUs == C0180C.TIME_UNSET) goto L8;
        if (r9 < this.sampleTimestampUpperLimitFilterUs) goto L8;
        return;
    L8:
        if (this.timestampAdjuster == null) goto L10;
        long r1 = this.timestampAdjuster.adjustSampleTimestamp(r9);
    L11:
        ((TrackOutput) Assertions.checkNotNull(this.trackOutputs.get(r8))).sampleMetadata(r1, r11, r12, r13, toExoPlayerCryptoData(r8, r14));
        return;
    L10:
        r1 = r9;
        goto L11
    }

    @Override // android.media.MediaParser.OutputConsumer
    public void onSampleDataFound(int r5, MediaParser.InputReader r6) throws IOException {
        ensureSpaceForTrackIndex(r5);
        this.scratchDataReaderAdapter.input = r6;
        TrackOutput r0 = this.trackOutputs.get(r5);
        if (r0 != null) goto L5;
        r0 = this.extractorOutput.track(r5, -1);
        this.trackOutputs.set(r5, r0);
    L5:
        r0.sampleData(this.scratchDataReaderAdapter, (int) r6.getLength(), true);
    }

    @Override // android.media.MediaParser.OutputConsumer
    public void onSeekMapFound(MediaParser.SeekMap r7) {
        if (this.expectDummySeekMap == true) goto L5;
    L7:
        this.lastSeekMap = r7;
        long r0 = r7.getDurationMicros();
        ExtractorOutput r2 = this.extractorOutput;
        if (this.seekingDisabled == true) goto L10;
        SeekMap r3 = new SeekMapAdapter(r7);
    L15:
        r2.seekMap(r3);
        return;
    L10:
        if (r0 == (-2147483648L)) goto L12;
        long r4 = r0;
    L13:
        r3 = new SeekMap.Unseekable(r4);
        goto L15
    L12:
        r4 = C0180C.TIME_UNSET;
        goto L13
    L5:
        if (this.dummySeekMap != null) goto L7;
        this.dummySeekMap = r7;
    }

    @Override // android.media.MediaParser.OutputConsumer
    public void onTrackCountFound(int r2) {
        this.tracksFoundCalled = true;
        maybeEndTracks();
    }

    @Override // android.media.MediaParser.OutputConsumer
    public void onTrackDataFound(int r5, MediaParser.TrackData r6) {
        if (maybeObtainChunkIndex(r6.mediaFormat) == false) goto L5;
        return;
    L5:
        ensureSpaceForTrackIndex(r5);
        TrackOutput r0 = this.trackOutputs.get(r5);
        if (r0 != null) goto L17;
        String r1 = r6.mediaFormat.getString(MEDIA_FORMAT_KEY_TRACK_TYPE);
        if (r1 == null) goto L10;
        String r2 = r1;
    L11:
        int r3 = toTrackTypeConstant(r2);
        if (r3 != this.primaryTrackType) goto L14;
        this.primaryTrackIndex = r5;
    L14:
        r0 = this.extractorOutput.track(r5, r3);
        this.trackOutputs.set(r5, r0);
        if (r1 == null) goto L17;
        return;
    L10:
        r2 = r6.mediaFormat.getString("mime");
    L17:
        Format r4 = toExoPlayerFormat(r6);
        if (this.primaryTrackManifestFormat != null) goto L20;
    L22:
        Format r7 = r4;
    L23:
        r0.format(r7);
        this.trackFormats.set(r5, r4);
        maybeEndTracks();
        return;
    L20:
        if (r5 != this.primaryTrackIndex) goto L22;
        r7 = r4.withManifestFormatInfo(this.primaryTrackManifestFormat);
        goto L23
    }

    public void setExtractorOutput(ExtractorOutput r1) {
        this.extractorOutput = r1;
    }

    public void setMuxedCaptionFormats(List<Format> r1) {
        this.muxedCaptionFormats = r1;
    }

    public void setSampleTimestampUpperLimitFilterUs(long r1) {
        this.sampleTimestampUpperLimitFilterUs = r1;
    }

    public void setSelectedParserName(String r2) {
        this.containerMimeType = getMimeType(r2);
    }

    public void setTimestampAdjuster(TimestampAdjuster r1) {
        this.timestampAdjuster = r1;
    }
}
