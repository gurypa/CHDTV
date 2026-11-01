package androidx.media3.extractor.mp4;

import android.util.Pair;
import androidx.media3.common.C0180C;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.CodecSpecificDataUtil;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.container.MdtaMetadataEntry;
import androidx.media3.container.Mp4Box;
import androidx.media3.container.Mp4LocationData;
import androidx.media3.container.Mp4TimestampData;
import androidx.media3.container.NalUnitUtil;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.extractor.AacUtil;
import androidx.media3.extractor.Ac3Util;
import androidx.media3.extractor.Ac4Util;
import androidx.media3.extractor.AvcConfig;
import androidx.media3.extractor.DolbyVisionConfig;
import androidx.media3.extractor.ExtractorUtil;
import androidx.media3.extractor.GaplessInfoHolder;
import androidx.media3.extractor.HevcConfig;
import androidx.media3.extractor.OpusUtil;
import androidx.media3.extractor.VorbisUtil;
import androidx.media3.extractor.p001ts.PsExtractor;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class BoxParser {
    private static final int MAX_GAPLESS_TRIM_SIZE_SAMPLES = 4;
    private static final String TAG = "BoxParsers";
    private static final int TYPE_clcp = 1668047728;
    private static final int TYPE_mdta = 1835299937;
    private static final int TYPE_meta = 1835365473;
    private static final int TYPE_nclc = 1852009571;
    private static final int TYPE_nclx = 1852009592;
    private static final int TYPE_sbtl = 1935832172;
    private static final int TYPE_soun = 1936684398;
    private static final int TYPE_subt = 1937072756;
    private static final int TYPE_text = 1952807028;
    private static final int TYPE_vide = 1986618469;
    private static final byte[] opusMagic = null;

    private static final class ChunkIterator {
        private final ParsableByteArray chunkOffsets;
        private final boolean chunkOffsetsAreLongs;
        public int index;
        public final int length;
        private int nextSamplesPerChunkChangeIndex;
        public int numSamples;
        public long offset;
        private int remainingSamplesPerChunkChanges;
        private final ParsableByteArray stsc;

        public ChunkIterator(ParsableByteArray r3, ParsableByteArray r4, boolean r5) throws ParserException {
            this.stsc = r3;
            this.chunkOffsets = r4;
            this.chunkOffsetsAreLongs = r5;
            r4.setPosition(12);
            this.length = r4.readUnsignedIntToInt();
            r3.setPosition(12);
            this.remainingSamplesPerChunkChanges = r3.readUnsignedIntToInt();
            boolean r1 = true;
            if (r3.readInt() == 1) goto L6;
            r1 = false;
        L6:
            ExtractorUtil.checkContainerInput(r1, "first_chunk must be 1");
            this.index = -1;
        }

        public boolean moveNext() {
            int r0 = this.index + 1;
            this.index = r0;
            if (r0 != this.length) goto L7;
            return false;
        L7:
            if (this.chunkOffsetsAreLongs == false) goto L9;
            long r2 = this.chunkOffsets.readUnsignedLongToLong();
        L10:
            this.offset = r2;
            if (this.index != this.nextSamplesPerChunkChangeIndex) goto L17;
            this.numSamples = this.stsc.readUnsignedIntToInt();
            this.stsc.skipBytes(4);
            int r1 = this.remainingSamplesPerChunkChanges - 1;
            this.remainingSamplesPerChunkChanges = r1;
            if (r1 <= 0) goto L15;
            int r3 = this.stsc.readUnsignedIntToInt() - 1;
        L16:
            this.nextSamplesPerChunkChangeIndex = r3;
            goto L17
        L15:
            r3 = -1;
        L17:
            return true;
        L9:
            r2 = this.chunkOffsets.readUnsignedInt();
            goto L10
        }
    }

    private static final class EsdsData {
        private final long bitrate;
        private final byte[] initializationData;
        private final String mimeType;
        private final long peakBitrate;

        public EsdsData(String r1, byte[] r2, long r3, long r5) {
            this.mimeType = r1;
            this.initializationData = r2;
            this.bitrate = r3;
            this.peakBitrate = r5;
        }

        static /* synthetic */ byte[] access$1000(EsdsData r1) {
            return r1.initializationData;
        }

        static /* synthetic */ long access$1100(EsdsData r2) {
            return r2.peakBitrate;
        }

        static /* synthetic */ long access$1200(EsdsData r2) {
            return r2.bitrate;
        }

        static /* synthetic */ String access$900(EsdsData r1) {
            return r1.mimeType;
        }
    }

    private static final class EyesData {
        private final StriData striData;

        public EyesData(StriData r1) {
            this.striData = r1;
        }

        static /* synthetic */ StriData access$700(EyesData r1) {
            return r1.striData;
        }
    }

    private static final class MdhdData {
        private final String language;
        private final long mediaDurationUs;
        private final long timescale;

        public MdhdData(long r1, long r3, String r5) {
            this.timescale = r1;
            this.mediaDurationUs = r3;
            this.language = r5;
        }

        static /* synthetic */ String access$300(MdhdData r1) {
            return r1.language;
        }

        static /* synthetic */ long access$400(MdhdData r2) {
            return r2.timescale;
        }

        static /* synthetic */ long access$500(MdhdData r2) {
            return r2.mediaDurationUs;
        }
    }

    private interface SampleSizeBox {
        int getFixedSampleSize();

        int getSampleCount();

        int readNextSampleSize();
    }

    private static final class StriData {
        private final boolean eyeViewsReversed;
        private final boolean hasAdditionalViews;
        private final boolean hasLeftEyeView;
        private final boolean hasRightEyeView;

        public StriData(boolean r1, boolean r2, boolean r3, boolean r4) {
            this.hasLeftEyeView = r1;
            this.hasRightEyeView = r2;
            this.eyeViewsReversed = r3;
            this.hasAdditionalViews = r4;
        }

        static /* synthetic */ boolean access$1300(StriData r1) {
            return r1.hasLeftEyeView;
        }

        static /* synthetic */ boolean access$1400(StriData r1) {
            return r1.hasRightEyeView;
        }

        static /* synthetic */ boolean access$800(StriData r1) {
            return r1.eyeViewsReversed;
        }
    }

    private static final class StsdData {
        public static final int STSD_HEADER_SIZE = 8;
        public Format format;
        public int nalUnitLengthFieldLength;
        public int requiredSampleTransformation;
        public final TrackEncryptionBox[] trackEncryptionBoxes;

        public StsdData(int r2) {
            this.trackEncryptionBoxes = new TrackEncryptionBox[r2];
            this.requiredSampleTransformation = 0;
        }
    }

    static final class StszSampleSizeBox implements SampleSizeBox {
        private final ParsableByteArray data;
        private final int fixedSampleSize;
        private final int sampleCount;

        public StszSampleSizeBox(Mp4Box.LeafBox r5, Format r6) {
            this.data = r5.data;
            this.data.setPosition(12);
            int r0 = this.data.readUnsignedIntToInt();
            if (MimeTypes.AUDIO_RAW.equals(r6.sampleMimeType) == false) goto L9;
            int r1 = Util.getPcmFrameSize(r6.pcmEncoding, r6.channelCount);
            if (r0 != 0) goto L7;
        L8:
            Log.m48w(BoxParser.TAG, "Audio sample size mismatch. stsd sample size: " + r1 + ", stsz sample size: " + r0);
            r0 = r1;
            goto L9
        L7:
            if ((r0 % r1) != 0) goto L8;
        L9:
            if (r0 != 0) goto L11;
            int r2 = -1;
        L12:
            this.fixedSampleSize = r2;
            this.sampleCount = this.data.readUnsignedIntToInt();
            return;
        L11:
            r2 = r0;
            goto L12
        }

        @Override // androidx.media3.extractor.mp4.BoxParser.SampleSizeBox
        public int getFixedSampleSize() {
            return this.fixedSampleSize;
        }

        @Override // androidx.media3.extractor.mp4.BoxParser.SampleSizeBox
        public int getSampleCount() {
            return this.sampleCount;
        }

        @Override // androidx.media3.extractor.mp4.BoxParser.SampleSizeBox
        public int readNextSampleSize() {
            if (this.fixedSampleSize != (-1)) goto L6;
            return this.data.readUnsignedIntToInt();
        L6:
            return this.fixedSampleSize;
        }
    }

    static final class Stz2SampleSizeBox implements SampleSizeBox {
        private int currentByte;
        private final ParsableByteArray data;
        private final int fieldSize;
        private final int sampleCount;
        private int sampleIndex;

        public Stz2SampleSizeBox(Mp4Box.LeafBox r3) {
            this.data = r3.data;
            this.data.setPosition(12);
            this.fieldSize = this.data.readUnsignedIntToInt() & 255;
            this.sampleCount = this.data.readUnsignedIntToInt();
        }

        @Override // androidx.media3.extractor.mp4.BoxParser.SampleSizeBox
        public int getFixedSampleSize() {
            return -1;
        }

        @Override // androidx.media3.extractor.mp4.BoxParser.SampleSizeBox
        public int getSampleCount() {
            return this.sampleCount;
        }

        @Override // androidx.media3.extractor.mp4.BoxParser.SampleSizeBox
        public int readNextSampleSize() {
            if (this.fieldSize != 8) goto L7;
            return this.data.readUnsignedByte();
        L7:
            if (this.fieldSize == 16) goto L9;
            int r0 = this.sampleIndex;
            this.sampleIndex = r0 + 1;
            if ((r0 % 2) != 0) goto L15;
            this.currentByte = this.data.readUnsignedByte();
            return (this.currentByte & PsExtractor.VIDEO_STREAM_MASK) >> 4;
        L15:
            return this.currentByte & 15;
        L9:
            return this.data.readUnsignedShort();
        }
    }

    private static final class TkhdData {
        private final long duration;

        /* JADX INFO: renamed from: id */
        private final int f59id;
        private final int rotationDegrees;

        public TkhdData(int r1, long r2, int r4) {
            this.f59id = r1;
            this.duration = r2;
            this.rotationDegrees = r4;
        }

        static /* synthetic */ long access$000(TkhdData r2) {
            return r2.duration;
        }

        static /* synthetic */ int access$100(TkhdData r1) {
            return r1.f59id;
        }

        static /* synthetic */ int access$200(TkhdData r1) {
            return r1.rotationDegrees;
        }
    }

    static final class VexuData {
        private final EyesData eyesData;

        public VexuData(EyesData r1) {
            this.eyesData = r1;
        }

        static /* synthetic */ EyesData access$600(VexuData r1) {
            return r1.eyesData;
        }

        public boolean hasBothEyeViews() {
            if (this.eyesData != null) goto L5;
        L9:
            return false;
        L5:
            if (StriData.access$1300(EyesData.access$700(this.eyesData)) == false) goto L9;
            if (StriData.access$1400(EyesData.access$700(this.eyesData)) == false) goto L9;
            return true;
        }
    }

    static {
        opusMagic = Util.getUtf8Bytes("OpusHead");
    }

    private BoxParser() {
    }

    private static ByteBuffer allocateHdrStaticInfo() {
        return ByteBuffer.allocate(25).order(ByteOrder.LITTLE_ENDIAN);
    }

    private static boolean canApplyEditWithGaplessInfo(long[] r7, long r8, long r10, long r12) {
        int r0 = r7.length - 1;
        int r4 = Util.constrainValue(4, 0, r0);
        int r2 = Util.constrainValue(r7.length - 4, 0, r0);
        if (r7[0] > r10) goto L12;
        if (r10 >= r7[r4]) goto L12;
        if (r7[r2] >= r12) goto L12;
        if (r12 > r8) goto L12;
        return true;
    L12:
        return false;
    }

    private static int findBoxPosition(ParsableByteArray r6, int r7, int r8, int r9) throws ParserException {
        int r0 = r6.getPosition();
        if (r0 < r8) goto L5;
        boolean r3 = true;
    L6:
        ExtractorUtil.checkContainerInput(r3, null);
    L8:
        if ((r0 - r8) >= r9) goto L17;
        r6.setPosition(r0);
        int r4 = r6.readInt();
        if (r4 <= 0) goto L12;
        boolean r5 = true;
    L13:
        ExtractorUtil.checkContainerInput(r5, "childAtomSize must be positive");
        if (r6.readInt() == r7) goto L15;
        r0 = r0 + r4;
        goto L8
    L15:
        return r0;
    L12:
        r5 = false;
        goto L13
    L17:
        return -1;
    L5:
        r3 = false;
        goto L6
    }

    private static int getTrackTypeForHdlr(int r1) {
        if (r1 != TYPE_soun) goto L7;
        return 1;
    L7:
        if (r1 != TYPE_vide) goto L11;
        return 2;
    L11:
        if (r1 != TYPE_text) goto L13;
        return 3;
    L13:
        if (r1 != TYPE_sbtl) goto L15;
        return 3;
    L15:
        if (r1 != TYPE_subt) goto L17;
        return 3;
    L17:
        if (r1 != TYPE_clcp) goto L20;
        return 3;
    L20:
        if (r1 != 1835365473) goto L23;
        return 5;
    L23:
        return -1;
    }

    public static void maybeSkipRemainingMetaBoxHeaderBytes(ParsableByteArray r3) {
        int r0 = r3.getPosition();
        r3.skipBytes(4);
        if (r3.readInt() == 1751411826) goto L5;
        r0 = r0 + 4;
    L5:
        r3.setPosition(r0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static void parseAudioSampleEntry(ParsableByteArray r28, int r29, int r30, int r31, int r32, String r33, boolean r34, DrmInitData r35, StsdData r36, int r37) throws ParserException {
        int r1 = r29;
        int r3 = r31;
        DrmInitData r6 = r35;
        r28.setPosition((r30 + 8) + 8);
        int r8 = 0;
        if (r34 == false) goto L5;
        r8 = r28.readUnsignedShort();
        r28.skipBytes(6);
    L6:
        int r12 = -1;
        char r10 = 2;
        int r19 = 0;
        if (r8 == 0) goto L47;
        if (r8 == 1) goto L47;
        if (r8 != 2) goto L45;
        r28.skipBytes(16);
        r10 = 2;
        int r11 = (int) Math.round(r28.readDouble());
        int r13 = r28.readUnsignedIntToInt();
        r28.skipBytes(4);
        int r9 = r28.readUnsignedIntToInt();
        int r16 = r28.readUnsignedIntToInt();
        if ((r16 & 1) == 0) goto L14;
        boolean r21 = true;
    L16:
        if ((r16 & 2) == 0) goto L18;
        boolean r24 = true;
    L19:
        if (r21 == true) goto L42;
        if (r9 != 8) goto L24;
        r12 = 3;
    L44:
        r28.skipBytes(8);
        int r14 = 0;
    L51:
        if (r1 != 1767992678) goto L53;
        r13 = -1;
        r11 = -1;
    L53:
        int r15 = r28.getPosition();
        int r22 = r11;
        if (r1 != 1701733217) goto L63;
        Pair<Integer, TrackEncryptionBox> r17 = parseSampleEntryEncryptionData(r28, r30, r3);
        if (r17 == null) goto L62;
        int r2 = ((Integer) r17.first).intValue();
        if (r6 != null) goto L60;
        DrmInitData r18 = null;
    L61:
        r36.trackEncryptionBoxes[r37] = (TrackEncryptionBox) r17.second;
        r1 = r2;
        r6 = r18;
        goto L62
    L60:
        r18 = r6.copyWithSchemeType(((TrackEncryptionBox) r17.second).schemeType);
    L62:
        r28.setPosition(r15);
    L63:
        String r20 = null;
        if (r1 != 1633889587) goto L67;
        r20 = MimeTypes.AUDIO_AC3;
    L137:
        int r4 = r15;
        EsdsData r110 = null;
        String r111 = null;
        int r112 = r13;
        List<byte[]> r113 = null;
        int r114 = r22;
    L138:
        int r115 = r1;
        if ((r4 - r30) >= r3) goto L227;
        r28.setPosition(r4);
        int r5 = r28.readInt();
        if (r5 <= 0) goto L143;
        boolean r7 = 1;
    L144:
        EsdsData r23 = r110;
        ExtractorUtil.checkContainerInput(r7, "childAtomSize must be positive");
        int r25 = r28.readInt();
        if (r25 != 1835557187) goto L155;
        r28.setPosition(r4 + 8);
        r28.skipBytes(1);
        int r26 = r28.readUnsignedByte();
        r28.skipBytes(1);
        if (Objects.equals(r20, MimeTypes.AUDIO_MPEGH_MHM1) == false) goto L149;
        Object[] r116 = new Object[1];
        r116[r19] = Integer.valueOf(r26);
        String r117 = String.format("mhm1.%02X", r116);
    L150:
        int r118 = r28.readUnsignedShort();
        byte[] r119 = new byte[r118];
        String r27 = r117;
        int r120 = r19;
        r28.readBytes(r119, r120, r118);
        if (r113 != null) goto L153;
        r113 = ImmutableList.m213of(r119);
    L154:
        r110 = r23;
        r111 = r27;
        r14 = r14;
    L226:
        r4 = r4 + r5;
        r3 = r31;
        r1 = r115;
        r14 = r14;
        r12 = r12;
        r19 = 0;
        goto L138
    L153:
        r113 = ImmutableList.m214of(r119, r113.get(r120));
        goto L154
    L149:
        Object[] r121 = new Object[1];
        r121[r19] = Integer.valueOf(r26);
        r117 = String.format("mha1.%02X", r121);
        goto L150
    L155:
        r12 = r12;
        if (r25 != 1835557200) goto L165;
        r28.setPosition(r4 + 8);
        int r122 = r28.readUnsignedByte();
        if (r122 <= 0) goto L163;
        byte[] r123 = new byte[r122];
        String r210 = r111;
        r28.readBytes(r123, 0, r122);
        if (r113 != null) goto L162;
        r113 = ImmutableList.m213of(r123);
    L164:
        r14 = r14;
        r110 = r23;
        r111 = r210;
        goto L226
    L162:
        r113 = ImmutableList.m214of(r113.get(0), r123);
        goto L164
    L163:
        r210 = r111;
        goto L164
    L165:
        String r211 = r111;
        if (r25 == 1702061171) goto L208;
        if (r34 == false) goto L172;
        if (r25 == 2002876005) goto L208;
    L172:
        if (r25 != 1684103987) goto L175;
        r28.setPosition(r4 + 8);
        r36.format = Ac3Util.parseAc3AnnexFFormat(r28, Integer.toString(r32), r33, r6);
        r14 = r14;
    L206:
        r110 = r23;
        r111 = r211;
        goto L226
    L175:
        if (r25 != 1684366131) goto L178;
        r28.setPosition(r4 + 8);
        r36.format = Ac3Util.parseEAc3AnnexFFormat(r28, Integer.toString(r32), r33, r6);
        r14 = r14;
        goto L206
    L178:
        if (r25 != 1684103988) goto L181;
        r28.setPosition(r4 + 8);
        r36.format = Ac4Util.parseAc4AnnexEFormat(r28, Integer.toString(r32), r33, r6);
        r14 = r14;
        goto L206
    L181:
        if (r25 != 1684892784) goto L187;
        if (r14 <= 0) goto L185;
        r114 = r14;
        r14 = r14;
        r112 = 2;
        r110 = r23;
        r111 = r211;
        goto L226
    L185:
        throw ParserException.createForMalformedContainer("Invalid sample rate for Dolby TrueHD MLP stream: " + r14, null);
    L187:
        if (r25 == 1684305011) goto L205;
        if (r25 == 1969517683) goto L205;
        if (r25 != 1682927731) goto L195;
        int r124 = r5 - 8;
        byte[] r125 = Arrays.copyOf(opusMagic, opusMagic.length + r124);
        r28.setPosition(r4 + 8);
        r28.readBytes(r125, opusMagic.length, r124);
        r113 = OpusUtil.buildInitializationData(r125);
        r14 = r14;
        r110 = r23;
        r111 = r211;
        goto L226
    L195:
        if (r25 != 1684425825) goto L198;
        int r126 = r5 - 12;
        byte[] r127 = new byte[r126 + 4];
        r127[0] = 102;
        r127[1] = 76;
        r127[r10] = 97;
        r127[3] = 67;
        r28.setPosition(r4 + 12);
        r28.readBytes(r127, 4, r126);
        r113 = ImmutableList.m213of(r127);
        r14 = r14;
        r110 = r23;
        r111 = r211;
        goto L226
    L198:
        if (r25 != 1634492771) goto L200;
        int r128 = r5 - 12;
        byte[] r129 = new byte[r128];
        r28.setPosition(r4 + 12);
        r28.readBytes(r129, 0, r128);
        Pair<Integer, Integer> r130 = CodecSpecificDataUtil.parseAlacAudioSpecificConfig(r129);
        r14 = r14;
        int r38 = ((Integer) r130.first).intValue();
        int r131 = ((Integer) r130.second).intValue();
        r113 = ImmutableList.m213of(r129);
        r112 = r131;
        r110 = r23;
        r111 = r211;
        r114 = r38;
        goto L226
    L200:
        r14 = r14;
        if (r25 != 1767990114) goto L206;
        r28.setPosition((r4 + 8) + 1);
        int r39 = r28.readUnsignedLeb128ToInt();
        byte[] r132 = new byte[r39];
        r28.readBytes(r132, 0, r39);
        r113 = ImmutableList.m213of(r132);
        r110 = r23;
        r111 = r211;
    L205:
        r36.format = new Format.Builder().setId(r32).setSampleMimeType(r20).setChannelCount(r112).setSampleRate(r114).setDrmInitData(r6).setLanguage(r33).build();
    L208:
        if (r25 != 1702061171) goto L210;
        int r40 = r4;
    L212:
        if (r40 == (-1)) goto L224;
        r110 = parseEsdsFromParent(r28, r40);
        r20 = EsdsData.access$900(r110);
        byte[] r133 = EsdsData.access$1000(r110);
        if (r133 != null) goto L216;
        r111 = r211;
        goto L226
    L216:
        if (MimeTypes.AUDIO_VORBIS.equals(r20) == false) goto L219;
        r113 = VorbisUtil.parseVorbisCsdFromEsdsInitializationData(r133);
        r111 = r211;
        goto L226
    L219:
        if (MimeTypes.AUDIO_AAC.equals(r20) == false) goto L221;
        AacUtil.Config r134 = AacUtil.parseAudioSpecificConfig(r133);
        r114 = r134.sampleRateHz;
        r112 = r134.channelCount;
        r111 = r134.codecs;
    L222:
        r113 = ImmutableList.m213of(r133);
        goto L226
    L221:
        r111 = r211;
        goto L222
    L224:
        r110 = r23;
        r111 = r211;
        goto L226
    L210:
        r40 = findBoxPosition(r28, Mp4Box.TYPE_esds, r4, r5);
        goto L212
    L143:
        r7 = r19;
        goto L144
    L227:
        int r212 = r12;
        String r213 = r111;
        EsdsData r214 = r110;
        if (r36.format != null) goto L235;
        if (r20 == null) goto L235;
        Format.Builder r41 = new Format.Builder().setId(r32).setSampleMimeType(r20).setCodecs(r213).setChannelCount(r112).setSampleRate(r114).setPcmEncoding(r212).setInitializationData(r113).setDrmInitData(r6).setLanguage(r33);
        if (r214 == null) goto L233;
        r41.setAverageBitrate(Ints.saturatedCast(EsdsData.access$1200(r214))).setPeakBitrate(Ints.saturatedCast(EsdsData.access$1100(r214)));
    L233:
        r36.format = r41.build();
        return;
    L235:
        return;
    L67:
        if (r1 != 1700998451) goto L70;
        r20 = MimeTypes.AUDIO_E_AC3;
        goto L137
    L70:
        if (r1 != 1633889588) goto L73;
        r20 = MimeTypes.AUDIO_AC4;
        goto L137
    L73:
        if (r1 != 1685353315) goto L76;
        r20 = MimeTypes.AUDIO_DTS;
        goto L137
    L76:
        if (r1 != 1685353320) goto L78;
    L136:
        r20 = MimeTypes.AUDIO_DTS_HD;
        goto L137
    L78:
        if (r1 == 1685353324) goto L136;
        if (r1 != 1685353317) goto L84;
        r20 = MimeTypes.AUDIO_DTS_EXPRESS;
        goto L137
    L84:
        if (r1 != 1685353336) goto L87;
        r20 = MimeTypes.AUDIO_DTS_X;
        goto L137
    L87:
        if (r1 != 1935764850) goto L90;
        r20 = MimeTypes.AUDIO_AMR_NB;
        goto L137
    L90:
        if (r1 != 1935767394) goto L93;
        r20 = MimeTypes.AUDIO_AMR_WB;
        goto L137
    L93:
        if (r1 != 1936684916) goto L96;
        r20 = MimeTypes.AUDIO_RAW;
        r12 = 2;
        goto L137
    L96:
        if (r1 != 1953984371) goto L99;
        r20 = MimeTypes.AUDIO_RAW;
        r12 = 268435456;
        goto L137
    L99:
        if (r1 != 1819304813) goto L104;
        r20 = MimeTypes.AUDIO_RAW;
        if (r12 != (-1)) goto L137;
        r12 = 2;
        goto L137
    L104:
        if (r1 != 778924082) goto L106;
    L135:
        r20 = MimeTypes.AUDIO_MPEG;
        goto L137
    L106:
        if (r1 == 778924083) goto L135;
        if (r1 != 1835557169) goto L112;
        r20 = MimeTypes.AUDIO_MPEGH_MHA1;
        goto L137
    L112:
        if (r1 != 1835560241) goto L115;
        r20 = MimeTypes.AUDIO_MPEGH_MHM1;
        goto L137
    L115:
        if (r1 != 1634492771) goto L118;
        r20 = MimeTypes.AUDIO_ALAC;
        goto L137
    L118:
        if (r1 != 1634492791) goto L121;
        r20 = MimeTypes.AUDIO_ALAW;
        goto L137
    L121:
        if (r1 != 1970037111) goto L124;
        r20 = MimeTypes.AUDIO_MLAW;
        goto L137
    L124:
        if (r1 != 1332770163) goto L127;
        r20 = MimeTypes.AUDIO_OPUS;
        goto L137
    L127:
        if (r1 != 1716281667) goto L130;
        r20 = MimeTypes.AUDIO_FLAC;
        goto L137
    L130:
        if (r1 != 1835823201) goto L133;
        r20 = MimeTypes.AUDIO_TRUEHD;
        goto L137
    L133:
        if (r1 != 1767992678) goto L137;
        r20 = MimeTypes.AUDIO_IAMF;
        goto L137
    L24:
        if (r9 != 16) goto L30;
        if (r24 == false) goto L27;
        int r135 = 268435456;
    L28:
        r12 = r135;
        goto L44
    L27:
        r135 = 2;
        goto L28
    L30:
        if (r9 != 24) goto L36;
        if (r24 == false) goto L33;
        int r136 = C0180C.ENCODING_PCM_24BIT_BIG_ENDIAN;
    L34:
        r12 = r136;
        goto L44
    L33:
        r136 = 21;
        goto L34
    L36:
        if (r9 != 32) goto L44;
        if (r24 == false) goto L39;
        int r137 = C0180C.ENCODING_PCM_32BIT_BIG_ENDIAN;
    L40:
        r12 = r137;
        goto L44
    L39:
        r137 = 22;
        goto L40
    L42:
        if (r9 != 32) goto L44;
        r12 = 4;
        goto L44
    L18:
        r24 = false;
        goto L19
    L14:
        r21 = false;
        goto L16
    L45:
        return;
    L47:
        r13 = r28.readUnsignedShort();
        r28.skipBytes(6);
        r11 = r28.readUnsignedFixedPoint1616();
        r28.setPosition(r28.getPosition() - 4);
        r14 = r28.readInt();
        if (r8 != 1) goto L51;
        r28.skipBytes(16);
        goto L51
    L5:
        r28.skipBytes(8);
        goto L6
    }

    private static ColorInfo parseAv1c(ParsableByteArray r23) {
        ColorInfo.Builder r0 = new ColorInfo.Builder();
        ParsableBitArray r1 = new ParsableBitArray(r23.getData());
        r1.setPosition(r23.getPosition() * 8);
        r1.skipBytes(1);
        int r5 = r1.readBits(3);
        r1.skipBits(6);
        boolean r6 = r1.readBit();
        boolean r7 = r1.readBit();
        int r8 = 12;
        int r9 = 10;
        if (r5 != 2) goto L12;
        if (r6 == false) goto L12;
        if (r7 == false) goto L7;
        int r11 = 12;
    L8:
        r0.setLumaBitdepth(r11);
        if (r7 == false) goto L11;
        r9 = 12;
    L11:
        r0.setChromaBitdepth(r9);
    L21:
        r1.skipBits(13);
        r1.skipBit();
        int r12 = r1.readBits(4);
        if (r12 == 1) goto L26;
        Log.m46i(TAG, "Unsupported obu_type: " + r12);
        return r0.build();
    L26:
        if (r1.readBit() == false) goto L29;
        Log.m46i(TAG, "Unsupported obu_extension_flag");
        return r0.build();
    L29:
        boolean r14 = r1.readBit();
        r1.skipBit();
        if (r14 == true) goto L32;
    L35:
        int r10 = r1.readBits(3);
        r1.skipBit();
        if (r1.readBit() == false) goto L40;
        Log.m46i(TAG, "Unsupported reduced_still_picture_header");
        return r0.build();
    L40:
        if (r1.readBit() == false) goto L44;
        Log.m46i(TAG, "Unsupported timing_info_present_flag");
        return r0.build();
    L44:
        if (r1.readBit() == false) goto L47;
        Log.m46i(TAG, "Unsupported initial_display_delay_present_flag");
        return r0.build();
    L47:
        int r15 = r1.readBits(5);
        int r3 = 0;
    L49:
        if (r3 > r15) goto L54;
        r1.skipBits(r8);
        if (r1.readBits(5) <= 7) goto L53;
        r1.skipBit();
    L53:
        r3 = r3 + 1;
        r8 = 12;
        goto L49
    L54:
        int r4 = r1.readBits(4);
        int r13 = r1.readBits(4);
        r1.skipBits(r4 + 1);
        r1.skipBits(r13 + 1);
        if (r1.readBit() == false) goto L57;
        r1.skipBits(7);
    L57:
        r1.skipBits(7);
        boolean r16 = r1.readBit();
        if (r16 == false) goto L61;
        r1.skipBits(2);
    L61:
        if (r1.readBit() == false) goto L63;
        int r17 = 2;
    L65:
        if (r17 > 0) goto L67;
    L69:
        if (r16 == false) goto L71;
        int r18 = 3;
        r1.skipBits(3);
    L72:
        r1.skipBits(r18);
        boolean r19 = r1.readBit();
        if (r10 != 2) goto L76;
        if (r19 == false) goto L76;
        r1.skipBit();
    L76:
        if (r10 != 1) goto L78;
    L80:
        boolean r110 = false;
    L82:
        if (r1.readBit() == false) goto L100;
        int r2 = r1.readBits(8);
        int r20 = r1.readBits(8);
        int r111 = r1.readBits(8);
        if (r110 == true) goto L92;
        if (r2 != 1) goto L92;
        if (r20 != 13) goto L92;
        if (r111 != 0) goto L92;
        int r112 = 1;
    L93:
        int r113 = r112;
        ColorInfo.Builder r21 = r0.setColorSpace(ColorInfo.isoColorPrimariesToColorSpace(r2));
        int r22 = 1;
        if (r113 == 1) goto L97;
        r22 = 2;
    L97:
        r21.setColorRange(r22).setColorTransfer(ColorInfo.isoTransferCharacteristicsToColorTransfer(r20));
    L92:
        r112 = r1.readBits(1);
    L100:
        return r0.build();
    L78:
        if (r1.readBit() == false) goto L80;
        r110 = true;
        goto L82
    L71:
        r18 = 3;
        goto L72
    L67:
        if (r1.readBit() == true) goto L69;
        r1.skipBits(1);
        goto L69
    L63:
        r17 = r1.readBits(1);
        goto L65
    L32:
        if (r1.readBits(8) <= 127) goto L35;
        Log.m46i(TAG, "Excessive obu_size");
        return r0.build();
    L7:
        r11 = 10;
    L12:
        if (r5 > 2) goto L21;
        if (r6 == false) goto L15;
        int r114 = 10;
    L16:
        r0.setLumaBitdepth(r114);
        if (r6 == true) goto L20;
        r9 = 8;
    L20:
        r0.setChromaBitdepth(r9);
        goto L21
    L15:
        r114 = 8;
        goto L16
    }

    static Pair<Integer, TrackEncryptionBox> parseCommonEncryptionSinfFromParent(ParsableByteArray r9, int r10, int r11) throws ParserException {
        int r0 = r10 + 8;
        int r1 = -1;
        int r2 = 0;
        String r3 = null;
        Integer r4 = null;
    L4:
        if ((r0 - r10) >= r11) goto L16;
        r9.setPosition(r0);
        int r5 = r9.readInt();
        int r6 = r9.readInt();
        if (r6 != 1718775137) goto L9;
        r4 = Integer.valueOf(r9.readInt());
    L14:
        r0 = r0 + r5;
        goto L4
    L9:
        if (r6 != 1935894637) goto L12;
        r9.skipBytes(4);
        r3 = r9.readString(4);
        goto L14
    L12:
        if (r6 != 1935894633) goto L14;
        r1 = r0;
        r2 = r5;
        goto L14
    L16:
        if (C0180C.CENC_TYPE_cenc.equals(r3) == false) goto L18;
    L26:
        boolean r7 = true;
        if (r4 == null) goto L29;
        boolean r8 = true;
    L30:
        ExtractorUtil.checkContainerInput(r8, "frma atom is mandatory");
        if (r1 == (-1)) goto L33;
        boolean r12 = true;
    L34:
        ExtractorUtil.checkContainerInput(r12, "schi atom is mandatory");
        TrackEncryptionBox r13 = parseSchiFromParent(r9, r1, r2, r3);
        if (r13 != null) goto L38;
        r7 = false;
    L38:
        ExtractorUtil.checkContainerInput(r7, "tenc atom is mandatory");
        return Pair.create(r4, (TrackEncryptionBox) Util.castNonNull(r13));
    L33:
        r12 = false;
        goto L34
    L29:
        r8 = false;
        goto L30
    L18:
        if (C0180C.CENC_TYPE_cbc1.equals(r3) == true) goto L26;
        if (C0180C.CENC_TYPE_cens.equals(r3) == true) goto L26;
        if (C0180C.CENC_TYPE_cbcs.equals(r3) == true) goto L26;
        return null;
    }

    private static Pair<long[], long[]> parseEdts(Mp4Box.ContainerBox r11) {
        Mp4Box.LeafBox r0 = r11.getLeafBoxOfType(Mp4Box.TYPE_elst);
        if (r0 != null) goto L6;
        return null;
    L6:
        ParsableByteArray r1 = r0.data;
        r1.setPosition(8);
        int r3 = parseFullBoxVersion(r1.readInt());
        int r4 = r1.readUnsignedIntToInt();
        long[] r5 = new long[r4];
        long[] r6 = new long[r4];
        int r7 = 0;
    L7:
        if (r7 >= r4) goto L22;
        if (r3 != 1) goto L11;
        long r9 = r1.readUnsignedLongToLong();
    L12:
        r5[r7] = r9;
        if (r3 != 1) goto L15;
        long r10 = r1.readLong();
    L16:
        r6[r7] = r10;
        if (r1.readShort() != 1) goto L20;
        r1.skipBytes(2);
        r7 = r7 + 1;
        goto L7
    L20:
        throw new IllegalArgumentException("Unsupported media rate.");
    L15:
        r10 = r1.readInt();
        goto L16
    L11:
        r9 = r1.readUnsignedInt();
        goto L12
    L22:
        return Pair.create(r5, r6);
    }

    private static EsdsData parseEsdsFromParent(ParsableByteArray r18, int r19) {
        r18.setPosition((r19 + 8) + 4);
        r18.skipBytes(1);
        parseExpandableClassSize(r18);
        r18.skipBytes(2);
        int r4 = r18.readUnsignedByte();
        if ((r4 & 128) == 0) goto L6;
        r18.skipBytes(2);
    L6:
        if ((r4 & 64) == 0) goto L9;
        r18.skipBytes(r18.readUnsignedByte());
    L9:
        if ((r4 & 32) == 0) goto L11;
        r18.skipBytes(2);
    L11:
        r18.skipBytes(1);
        parseExpandableClassSize(r18);
        String r6 = MimeTypes.getMimeTypeFromMp4ObjectType(r18.readUnsignedByte());
        if (MimeTypes.AUDIO_MPEG.equals(r6) == true) goto L28;
        if (MimeTypes.AUDIO_DTS.equals(r6) == true) goto L28;
        if (MimeTypes.AUDIO_DTS_HD.equals(r6) == true) goto L28;
        r18.skipBytes(4);
        long r12 = r18.readUnsignedInt();
        long r14 = r18.readUnsignedInt();
        r18.skipBytes(1);
        int r1 = parseExpandableClassSize(r18);
        byte[] r7 = new byte[r1];
        r18.readBytes(r7, 0, r1);
        long r10 = -1;
        if (r14 <= 0) goto L21;
        long r8 = r14;
    L23:
        if (r12 <= 0) goto L26;
        r10 = r12;
    L26:
        return new EsdsData(r6, r7, r8, r10);
    L21:
        r8 = -1;
    L28:
        return new EsdsData(r6, null, -1, -1);
    }

    private static int parseExpandableClassSize(ParsableByteArray r4) {
        int r0 = r4.readUnsignedByte();
        int r1 = r0 & 127;
    L4:
        if ((r0 & 128) != 128) goto L6;
        r0 = r4.readUnsignedByte();
        r1 = (r1 << 7) | (r0 & 127);
        goto L4
    L6:
        return r1;
    }

    public static int parseFullBoxFlags(int r1) {
        return 16777215 & r1;
    }

    public static int parseFullBoxVersion(int r1) {
        return (r1 >> 24) & 255;
    }

    private static int parseHdlr(ParsableByteArray r1) {
        r1.setPosition(16);
        return r1.readInt();
    }

    private static Metadata parseIlst(ParsableByteArray r2, int r3) {
        r2.skipBytes(8);
        ArrayList r0 = new ArrayList();
    L4:
        if (r2.getPosition() >= r3) goto L10;
        Metadata.Entry r1 = MetadataUtil.parseIlstElement(r2);
        if (r1 == null) goto L4;
        r0.add(r1);
        goto L4
    L10:
        if (r0.isEmpty() == false) goto L13;
        return null;
    L13:
        return new Metadata(r0);
    }

    private static MdhdData parseMdhd(ParsableByteArray r15) {
        int r0 = 8;
        r15.setPosition(8);
        int r2 = parseFullBoxVersion(r15.readInt());
        if (r2 != 0) goto L5;
        int r3 = 8;
    L6:
        r15.skipBytes(r3);
        long r5 = r15.readUnsignedInt();
        boolean r4 = true;
        int r10 = r15.getPosition();
        if (r2 != 0) goto L9;
        r0 = 4;
    L9:
        int r6 = 0;
    L10:
        if (r6 >= r0) goto L15;
        if (r15.getData()[r10 + r6] != (-1)) goto L13;
        r6 = r6 + 1;
        goto L10
    L13:
        r4 = false;
    L15:
        if (r4 == false) goto L17;
        r15.skipBytes(r0);
        long r8 = r5;
        long r7 = -9223372036854775807L;
    L24:
        int r11 = r15.readUnsignedShort();
        return new MdhdData(r8, r7, "" + ((char) (((r11 >> 10) & 31) + 96)) + ((char) (((r11 >> 5) & 31) + 96)) + ((char) ((r11 & 31) + 96)));
    L17:
        if (r2 != 0) goto L19;
        long r9 = r15.readUnsignedInt();
    L21:
        if (r9 != 0) goto L23;
        r8 = r5;
        r7 = -9223372036854775807L;
        goto L24
    L23:
        r8 = r5;
        r7 = Util.scaleLargeTimestamp(r9, 1000000, r8);
        goto L24
    L19:
        r9 = r15.readUnsignedLongToLong();
        goto L21
    L5:
        r3 = 16;
        goto L6
    }

    public static Metadata parseMdtaFromMeta(Mp4Box.ContainerBox r15) {
        Mp4Box.LeafBox r0 = r15.getLeafBoxOfType(Mp4Box.TYPE_hdlr);
        Mp4Box.LeafBox r1 = r15.getLeafBoxOfType(Mp4Box.TYPE_keys);
        Mp4Box.LeafBox r2 = r15.getLeafBoxOfType(Mp4Box.TYPE_ilst);
        if (r0 == null) goto L30;
        if (r1 == null) goto L30;
        if (r2 == null) goto L30;
        if (parseHdlr(r0.data) != TYPE_mdta) goto L30;
        ParsableByteArray r4 = r1.data;
        r4.setPosition(12);
        int r5 = r4.readInt();
        String[] r6 = new String[r5];
        int r7 = 0;
    L10:
        if (r7 >= r5) goto L12;
        int r8 = r4.readInt();
        r4.skipBytes(4);
        r6[r7] = r4.readString(r8 - 8);
        r7 = r7 + 1;
        goto L10
    L12:
        ParsableByteArray r9 = r2.data;
        r9.setPosition(8);
        ArrayList r10 = new ArrayList();
    L14:
        if (r9.bytesLeft() <= 8) goto L26;
        int r11 = r9.getPosition();
        int r12 = r9.readInt();
        int r13 = r9.readInt() - 1;
        if (r13 >= 0) goto L18;
    L23:
        Log.m48w(TAG, "Skipped metadata with unknown key index: " + r13);
    L24:
        r9.setPosition(r11 + r12);
        goto L14
    L18:
        if (r13 >= r6.length) goto L23;
        MdtaMetadataEntry r14 = MetadataUtil.parseMdtaMetadataEntryFromIlst(r9, r11 + r12, r6[r13]);
        if (r14 == null) goto L24;
        r10.add(r14);
        goto L24
    L26:
        if (r10.isEmpty() == false) goto L29;
        return null;
    L29:
        return new Metadata(r10);
    L30:
        return null;
    }

    private static void parseMetaDataSampleEntry(ParsableByteArray r2, int r3, int r4, int r5, StsdData r6) {
        r2.setPosition((r4 + 8) + 8);
        if (r3 != 1835365492) goto L8;
        r2.readNullTerminatedString();
        String r0 = r2.readNullTerminatedString();
        if (r0 == null) goto L9;
        r6.format = new Format.Builder().setId(r5).setSampleMimeType(r0).build();
        return;
    L9:
        return;
    }

    public static Mp4TimestampData parseMvhd(ParsableByteArray r13) {
        r13.setPosition(8);
        if (parseFullBoxVersion(r13.readInt()) != 0) goto L5;
        long r2 = r13.readUnsignedInt();
        long r7 = r2;
        long r9 = r13.readUnsignedInt();
    L7:
        return new Mp4TimestampData(r7, r9, r13.readUnsignedInt());
    L5:
        long r3 = r13.readLong();
        r7 = r3;
        r9 = r13.readLong();
        goto L7
    }

    private static float parsePaspFromParent(ParsableByteArray r4, int r5) {
        r4.setPosition(r5 + 8);
        return r4.readUnsignedIntToInt() / r4.readUnsignedIntToInt();
    }

    private static byte[] parseProjFromParent(ParsableByteArray r5, int r6, int r7) {
        int r0 = r6 + 8;
    L4:
        if ((r0 - r6) >= r7) goto L10;
        r5.setPosition(r0);
        int r1 = r5.readInt();
        if (r5.readInt() == 1886547818) goto L8;
        r0 = r0 + r1;
        goto L4
    L8:
        return Arrays.copyOfRange(r5.getData(), r0, r0 + r1);
    L10:
        return null;
    }

    private static Pair<Integer, TrackEncryptionBox> parseSampleEntryEncryptionData(ParsableByteArray r4, int r5, int r6) throws ParserException {
        int r0 = r4.getPosition();
    L4:
        if ((r0 - r5) >= r6) goto L15;
        r4.setPosition(r0);
        int r1 = r4.readInt();
        if (r1 <= 0) goto L8;
        boolean r2 = true;
    L9:
        ExtractorUtil.checkContainerInput(r2, "childAtomSize must be positive");
        if (r4.readInt() != 1936289382) goto L14;
        Pair<Integer, TrackEncryptionBox> r3 = parseCommonEncryptionSinfFromParent(r4, r0, r1);
        if (r3 == null) goto L14;
        return r3;
    L14:
        r0 = r0 + r1;
        goto L4
    L8:
        r2 = false;
        goto L9
    L15:
        return null;
    }

    private static TrackEncryptionBox parseSchiFromParent(ParsableByteArray r19, int r20, int r21, String r22) {
        int r1 = r20 + 8;
    L4:
        if ((r1 - r20) >= r21) goto L23;
        r19.setPosition(r1);
        int r2 = r19.readInt();
        if (r19.readInt() == 1952804451) goto L7;
        r1 = r1 + r2;
        goto L4
    L7:
        int r6 = parseFullBoxVersion(r19.readInt());
        r19.skipBytes(1);
        if (r6 != 0) goto L10;
        r19.skipBytes(1);
        int r16 = 0;
        int r17 = 0;
    L12:
        if (r19.readUnsignedByte() != 1) goto L14;
        boolean r12 = true;
    L15:
        int r14 = r19.readUnsignedByte();
        byte[] r15 = new byte[16];
        r19.readBytes(r15, 0, r15.length);
        if (r12 == false) goto L19;
        if (r14 != 0) goto L19;
        int r8 = r19.readUnsignedByte();
        byte[] r7 = new byte[r8];
        r19.readBytes(r7, 0, r8);
        byte[] r18 = r7;
    L21:
        return new TrackEncryptionBox(r12, r22, r14, r15, r16, r17, r18);
    L19:
        r18 = null;
        goto L21
    L14:
        r12 = false;
        goto L15
    L10:
        int r10 = r19.readUnsignedByte();
        r16 = (r10 & PsExtractor.VIDEO_STREAM_MASK) >> 4;
        r17 = r10 & 15;
        goto L12
    L23:
        return null;
    }

    /* JADX WARN: Failed to calculate best type for var: r0v30 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v30 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r0v70 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v70 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r0v71 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v71 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r0v73 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r0v73 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r10v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r10v0 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r10v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r10v1 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r10v5 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r10v5 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r13v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r13v0 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r13v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r13v1 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r13v2 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r13v2 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r17v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r17v0 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r17v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r17v1 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r17v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r17v3 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r18v14 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r18v14 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r18v15 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r18v15 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r18v8 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r18v8 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r1v21 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v21 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r1v22 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v22 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r1v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v3 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 6 more
     */
    /* JADX WARN: Failed to calculate best type for var: r1v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v3 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r1v42 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v42 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r1v43 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v43 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r1v44 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v44 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r1v7 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v7 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r20v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r20v0 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r20v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r20v1 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r20v6 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r20v6 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v10 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v10 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v14 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v14 ??, new type: boolean
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v15 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v15 ??, new type: boolean
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v24 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v24 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v25 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v25 ??, new type: boolean
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v6 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v6 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r3v19 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v19 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r3v22 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v22 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r3v37 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v37 ??, new type: boolean
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r3v40 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r3v40 ??, new type: boolean
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v11 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v11 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v18 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v18 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v19 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v19 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v26 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v26 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v27 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v27 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v29 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v29 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v30 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v30 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r56v2 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r56v2 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r57v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r57v0 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r57v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r57v1 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r57v2 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r57v2 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r5v18 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r5v18 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r5v19 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r5v19 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r5v22 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r5v22 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r5v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r5v3 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r5v5 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r5v5 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r6v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r6v0 ??, new type: androidx.media3.common.util.ParsableByteArray
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 6 more
     */
    /* JADX WARN: Failed to calculate best type for var: r6v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r6v0 ??, new type: androidx.media3.common.util.ParsableByteArray
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r6v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r6v1 ??, new type: androidx.media3.common.util.ParsableByteArray
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 6 more
     */
    /* JADX WARN: Failed to calculate best type for var: r6v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r6v1 ??, new type: androidx.media3.common.util.ParsableByteArray
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r6v12 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r6v12 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r6v14 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r6v14 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r6v15 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r6v15 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r6v19 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r6v19 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r6v20 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r6v20 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r6v25 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r6v25 ??, new type: androidx.media3.common.util.ParsableByteArray
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r6v26 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r6v26 ??, new type: androidx.media3.common.util.ParsableByteArray
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r6v8 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r6v8 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r6v9 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r6v9 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r7v20 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v20 ??, new type: androidx.media3.common.util.ParsableByteArray
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r7v21 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v21 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r7v34 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v34 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r7v7 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v7 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r8v13 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r8v13 ??, new type: int[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r9v13 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r9v13 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r9v14 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r9v14 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r9v16 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r9v16 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r9v17 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r9v17 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r9v18 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r9v18 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r9v19 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r9v19 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r9v20 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r9v20 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r9v21 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r9v21 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r9v22 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r9v22 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to set immutable type for var: r13v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r13v0 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyWithWiderIgnSame(TypeUpdate.java:73)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setImmutableType(TypeInferenceVisitor.java:111)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$1(TypeInferenceVisitor.java:102)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:102)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 6 more
     */
    /* JADX WARN: Failed to set immutable type for var: r9v14 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r9v14 ??, new type: int
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyWithWiderIgnSame(TypeUpdate.java:73)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setImmutableType(TypeInferenceVisitor.java:111)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$1(TypeInferenceVisitor.java:102)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:102)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 6 more
     */
    /*  JADX ERROR: Types fix failed
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v3 ??, new type: int
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:186)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:245)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
        Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
        	... 5 more
        */
    public static androidx.media3.extractor.mp4.TrackSampleTable parseStbl(androidx.media3.extractor.mp4.Track r76, androidx.media3.container.Mp4Box.ContainerBox r77, androidx.media3.extractor.GaplessInfoHolder r78) throws androidx.media3.common.ParserException {
        /*  JADX ERROR: Simple mode code generation failed
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0025: INVOKE (r13v0 ?? I:int A[IMMUTABLE_TYPE]) = (r12v1 androidx.media3.extractor.mp4.BoxParser$SampleSizeBox) INTERFACE call: androidx.media3.extractor.mp4.BoxParser.SampleSizeBox.getSampleCount():int A[DECLARE_VAR, MD:():int (m)] in method: androidx.media3.extractor.mp4.BoxParser.parseStbl(androidx.media3.extractor.mp4.Track, androidx.media3.container.Mp4Box$ContainerBox, androidx.media3.extractor.GaplessInfoHolder):androidx.media3.extractor.mp4.TrackSampleTable, file: classes.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:310)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:273)
            	at jadx.core.codegen.MethodGen.generateSimpleCode(MethodGen.java:348)
            	at jadx.core.codegen.MethodGen.addSimpleMethodCode(MethodGen.java:316)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:279)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:420)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:345)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:299)
            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
            	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
            Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.ArgType.getPrimitiveType()" because "type" is null
            	at jadx.core.codegen.ClassGen.useType(ClassGen.java:564)
            	at jadx.core.codegen.InsnGen.useType(InsnGen.java:269)
            	at jadx.core.codegen.InsnGen.declareVar(InsnGen.java:166)
            	at jadx.core.codegen.InsnGen.declareVar(InsnGen.java:159)
            	at jadx.core.codegen.InsnGen.assignVar(InsnGen.java:152)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:299)
            	... 11 more
            */
        /*
            Method dump skipped, instruction units count: 1668
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.media3.extractor.mp4.BoxParser.parseStbl(androidx.media3.extractor.mp4.Track, androidx.media3.container.Mp4Box$ContainerBox, androidx.media3.extractor.GaplessInfoHolder):androidx.media3.extractor.mp4.TrackSampleTable");
    }

    private static EyesData parseStereoViewBox(ParsableByteArray r12, int r13, int r14) throws ParserException {
        r12.setPosition(r13 + 8);
        int r0 = r12.getPosition();
    L4:
        if ((r0 - r13) >= r14) goto L29;
        r12.setPosition(r0);
        int r1 = r12.readInt();
        boolean r2 = false;
        if (r1 <= 0) goto L8;
        boolean r4 = true;
    L9:
        ExtractorUtil.checkContainerInput(r4, "childAtomSize must be positive");
        if (r12.readInt() == 1937011305) goto L11;
        r0 = r0 + r1;
        goto L4
    L11:
        r12.skipBytes(4);
        int r5 = r12.readUnsignedByte() & 15;
        if ((r5 & 1) != 1) goto L14;
        boolean r8 = true;
    L16:
        if ((r5 & 2) != 2) goto L18;
        boolean r9 = true;
    L20:
        if ((r5 & 8) != 8) goto L22;
        boolean r10 = true;
    L24:
        if ((r5 & 4) != 4) goto L27;
        r2 = true;
    L27:
        return new EyesData(new StriData(r8, r9, r10, r2));
    L22:
        r10 = false;
        goto L24
    L18:
        r9 = false;
        goto L20
    L14:
        r8 = false;
        goto L16
    L8:
        r4 = false;
        goto L9
    L29:
        return null;
    }

    private static StsdData parseStsd(ParsableByteArray r11, int r12, int r13, String r14, DrmInitData r15, boolean r16) throws ParserException {
        r11.setPosition(12);
        int r10 = r11.readInt();
        StsdData r6 = new StsdData(r10);
        int r8 = 0;
    L3:
        if (r8 >= r10) goto L117;
        int r2 = r11.getPosition();
        int r3 = r11.readInt();
        if (r3 <= 0) goto L7;
        boolean r1 = true;
    L8:
        ExtractorUtil.checkContainerInput(r1, "childAtomSize must be positive");
        int r4 = r11.readInt();
        if (r4 != 1635148593) goto L11;
    L115:
        StsdData r7 = r6;
        parseVideoSampleEntry(r11, r4, r2, r3, r12, r13, r15, r7, r8);
        r6 = r7;
    L116:
        r11.setPosition(r2 + r3);
        r8 = r8 + 1;
        goto L3
    L11:
        if (r4 == 1635148595) goto L115;
        if (r4 == 1701733238) goto L115;
        if (r4 == 1831958048) goto L115;
        if (r4 == 1836070006) goto L115;
        if (r4 == 1752589105) goto L115;
        if (r4 == 1751479857) goto L115;
        if (r4 == 1932670515) goto L115;
        if (r4 == 1211250227) goto L115;
        if (r4 == 1748121139) goto L115;
        if (r4 == 1987063864) goto L115;
        if (r4 == 1987063865) goto L115;
        if (r4 == 1635135537) goto L115;
        if (r4 == 1685479798) goto L115;
        if (r4 == 1685479729) goto L115;
        if (r4 == 1685481573) goto L115;
        if (r4 == 1685481521) goto L115;
        if (r4 != 1836069985) goto L46;
    L114:
        int r9 = r8;
        StsdData r17 = r6;
        parseAudioSampleEntry(r11, r4, r2, r3, r12, r14, r16, r15, r17, r9);
        r6 = r17;
        r8 = r9;
        goto L116
    L46:
        if (r4 == 1701733217) goto L114;
        if (r4 == 1633889587) goto L114;
        if (r4 == 1700998451) goto L114;
        if (r4 == 1633889588) goto L114;
        if (r4 == 1835823201) goto L114;
        if (r4 == 1685353315) goto L114;
        if (r4 == 1685353317) goto L114;
        if (r4 == 1685353320) goto L114;
        if (r4 == 1685353324) goto L114;
        if (r4 == 1685353336) goto L114;
        if (r4 == 1935764850) goto L114;
        if (r4 == 1935767394) goto L114;
        if (r4 == 1819304813) goto L114;
        if (r4 == 1936684916) goto L114;
        if (r4 == 1953984371) goto L114;
        if (r4 == 778924082) goto L114;
        if (r4 == 778924083) goto L114;
        if (r4 == 1835557169) goto L114;
        if (r4 == 1835560241) goto L114;
        if (r4 == 1634492771) goto L114;
        if (r4 == 1634492791) goto L114;
        if (r4 == 1970037111) goto L114;
        if (r4 == 1332770163) goto L114;
        if (r4 == 1716281667) goto L114;
        if (r4 == 1767992678) goto L114;
        if (r4 != 1414810956) goto L99;
    L113:
        parseTextSampleEntry(r11, r4, r2, r3, r12, r14, r6);
        goto L116
    L99:
        if (r4 == 1954034535) goto L113;
        if (r4 == 2004251764) goto L113;
        if (r4 == 1937010800) goto L113;
        if (r4 == 1664495672) goto L113;
        if (r4 != 1835365492) goto L111;
        parseMetaDataSampleEntry(r11, r4, r2, r12, r6);
        goto L116
    L111:
        if (r4 != 1667329389) goto L116;
        r6.format = new Format.Builder().setId(r12).setSampleMimeType(MimeTypes.APPLICATION_CAMERA_MOTION).build();
        goto L116
    L7:
        r1 = false;
        goto L8
    L117:
        return r6;
    }

    private static void parseTextSampleEntry(ParsableByteArray r7, int r8, int r9, int r10, int r11, String r12, StsdData r13) {
        r7.setPosition((r9 + 8) + 8);
        ImmutableList r0 = null;
        long r1 = Long.MAX_VALUE;
        if (r8 != 1414810956) goto L6;
        String r3 = MimeTypes.APPLICATION_TTML;
    L17:
        r13.format = new Format.Builder().setId(r11).setSampleMimeType(r3).setLanguage(r12).setSubsampleOffsetUs(r1).setInitializationData(r0).build();
        return;
    L6:
        if (r8 != 1954034535) goto L9;
        r3 = MimeTypes.APPLICATION_TX3G;
        int r4 = (r10 - 8) - 8;
        byte[] r5 = new byte[r4];
        r7.readBytes(r5, 0, r4);
        r0 = ImmutableList.m213of(r5);
        goto L17
    L9:
        if (r8 != 2004251764) goto L12;
        r3 = MimeTypes.APPLICATION_MP4VTT;
        goto L17
    L12:
        if (r8 != 1937010800) goto L15;
        r3 = MimeTypes.APPLICATION_TTML;
        r1 = 0;
        goto L17
    L15:
        if (r8 != 1664495672) goto L20;
        r3 = MimeTypes.APPLICATION_MP4CEA608;
        r13.requiredSampleTransformation = 1;
        goto L17
    L20:
        throw new IllegalStateException();
    }

    private static TkhdData parseTkhd(ParsableByteArray r15) {
        int r0 = 8;
        r15.setPosition(8);
        int r2 = parseFullBoxVersion(r15.readInt());
        if (r2 != 0) goto L5;
        int r4 = 8;
    L6:
        r15.skipBytes(r4);
        int r5 = r15.readInt();
        r15.skipBytes(4);
        boolean r6 = true;
        int r7 = r15.getPosition();
        if (r2 != 0) goto L9;
        r0 = 4;
    L9:
        int r8 = 0;
    L10:
        if (r8 >= r0) goto L15;
        if (r15.getData()[r7 + r8] != (-1)) goto L13;
        r8 = r8 + 1;
        goto L10
    L13:
        r6 = false;
    L15:
        if (r6 == false) goto L17;
        r15.skipBytes(r0);
        long r9 = C0180C.TIME_UNSET;
    L23:
        r15.skipBytes(16);
        int r3 = r15.readInt();
        int r10 = r15.readInt();
        r15.skipBytes(4);
        int r11 = r15.readInt();
        int r12 = r15.readInt();
        if (r3 != 0) goto L30;
        if (r10 != 65536) goto L30;
        if (r11 != (-65536)) goto L30;
        if (r12 != 0) goto L30;
        int r13 = 90;
    L45:
        return new TkhdData(r5, r9, r13);
    L30:
        if (r3 != 0) goto L37;
        if (r10 != (-65536)) goto L37;
        if (r11 != 65536) goto L37;
        if (r12 != 0) goto L37;
        r13 = 270;
    L37:
        if (r3 != (-65536)) goto L43;
        if (r10 != 0) goto L43;
        if (r11 != 0) goto L43;
        if (r12 != (-65536)) goto L43;
        r13 = 180;
    L43:
        r13 = 0;
        goto L45
    L17:
        if (r2 != 0) goto L19;
        r9 = r15.readUnsignedInt();
    L21:
        if (r9 != 0) goto L23;
        r9 = C0180C.TIME_UNSET;
        goto L23
    L19:
        r9 = r15.readUnsignedLongToLong();
        goto L21
    L5:
        r4 = 16;
        goto L6
    }

    public static Track parseTrak(Mp4Box.ContainerBox r32, Mp4Box.LeafBox r33, long r34, DrmInitData r36, boolean r37, boolean r38) throws ParserException {
        Mp4Box.ContainerBox r1 = (Mp4Box.ContainerBox) Assertions.checkNotNull(r32.getContainerBoxOfType(Mp4Box.TYPE_mdia));
        int r5 = getTrackTypeForHdlr(parseHdlr(((Mp4Box.LeafBox) Assertions.checkNotNull(r1.getLeafBoxOfType(Mp4Box.TYPE_hdlr))).data));
        if (r5 != (-1)) goto L5;
        return null;
    L5:
        TkhdData r2 = parseTkhd(((Mp4Box.LeafBox) Assertions.checkNotNull(r32.getLeafBoxOfType(Mp4Box.TYPE_tkhd))).data);
        if (r34 != C0180C.TIME_UNSET) goto L8;
        long r10 = TkhdData.access$000(r2);
    L9:
        long r14 = parseMvhd(r33.data).timescale;
        if (r10 != C0180C.TIME_UNSET) goto L12;
        long r11 = -9223372036854775807L;
    L13:
        Mp4Box.ContainerBox r6 = (Mp4Box.ContainerBox) Assertions.checkNotNull(((Mp4Box.ContainerBox) Assertions.checkNotNull(r1.getContainerBoxOfType(Mp4Box.TYPE_minf))).getContainerBoxOfType(Mp4Box.TYPE_stbl));
        MdhdData r22 = parseMdhd(((Mp4Box.LeafBox) Assertions.checkNotNull(r1.getLeafBoxOfType(Mp4Box.TYPE_mdhd))).data);
        Mp4Box.LeafBox r7 = r6.getLeafBoxOfType(Mp4Box.TYPE_stsd);
        if (r7 == null) goto L29;
        StsdData r8 = parseStsd(r7.data, TkhdData.access$100(r2), TkhdData.access$200(r2), MdhdData.access$300(r22), r36, r38);
        if (r37 == true) goto L22;
        Mp4Box.ContainerBox r13 = r32.getContainerBoxOfType(Mp4Box.TYPE_edts);
        if (r13 == null) goto L22;
        Pair<long[], long[]> r3 = parseEdts(r13);
        if (r3 == null) goto L22;
        long[] r18 = (long[]) r3.first;
        long[] r19 = (long[]) r3.second;
    L24:
        if (r8.format != null) goto L27;
        return null;
    L27:
        return new Track(TkhdData.access$100(r2), r5, MdhdData.access$400(r22), r14, r11, MdhdData.access$500(r22), r8.format, r8.requiredSampleTransformation, r8.trackEncryptionBoxes, r8.nalUnitLengthFieldLength, r18, r19);
    L22:
        r18 = null;
        r19 = null;
        goto L24
    L29:
        throw ParserException.createForMalformedContainer("Malformed sample table (stbl) missing sample description (stsd)", null);
    L12:
        r11 = Util.scaleLargeTimestamp(r10, 1000000, r14);
        goto L13
    L8:
        r10 = r34;
        goto L9
    }

    public static List<TrackSampleTable> parseTraks(Mp4Box.ContainerBox r10, GaplessInfoHolder r11, long r12, DrmInitData r14, boolean r15, boolean r16, Function<Track, Track> r17) throws ParserException {
        ArrayList r0 = new ArrayList();
        int r1 = 0;
    L4:
        if (r1 >= r10.containerChildren.size()) goto L14;
        Mp4Box.ContainerBox r3 = r10.containerChildren.get(r1);
        if (r3.type != 1953653099) goto L12;
        Track r2 = r17.apply(parseTrak(r3, (Mp4Box.LeafBox) Assertions.checkNotNull(r10.getLeafBoxOfType(Mp4Box.TYPE_mvhd)), r12, r14, r15, r16));
        if (r2 == null) goto L12;
        r0.add(parseStbl(r2, (Mp4Box.ContainerBox) Assertions.checkNotNull(((Mp4Box.ContainerBox) Assertions.checkNotNull(((Mp4Box.ContainerBox) Assertions.checkNotNull(r3.getContainerBoxOfType(Mp4Box.TYPE_mdia))).getContainerBoxOfType(Mp4Box.TYPE_minf))).getContainerBoxOfType(Mp4Box.TYPE_stbl)), r11));
    L12:
        r1 = r1 + 1;
        goto L4
    L14:
        return r0;
    }

    public static Metadata parseUdta(Mp4Box.LeafBox r7) {
        ParsableByteArray r0 = r7.data;
        r0.setPosition(8);
        Metadata r2 = new Metadata(new Metadata.Entry[0]);
    L4:
        if (r0.bytesLeft() < 8) goto L15;
        int r3 = r0.getPosition();
        int r4 = r0.readInt();
        int r5 = r0.readInt();
        if (r5 != 1835365473) goto L9;
        r0.setPosition(r3);
        r2 = r2.copyWithAppendedEntriesFrom(parseUdtaMeta(r0, r3 + r4));
    L14:
        r0.setPosition(r3 + r4);
        goto L4
    L9:
        if (r5 != 1936553057) goto L12;
        r0.setPosition(r3);
        r2 = r2.copyWithAppendedEntriesFrom(SmtaAtomUtil.parseSmta(r0, r3 + r4));
        goto L14
    L12:
        if (r5 != (-1451722374)) goto L14;
        r2 = r2.copyWithAppendedEntriesFrom(parseXyz(r0));
        goto L14
    L15:
        return r2;
    }

    private static Metadata parseUdtaMeta(ParsableByteArray r4, int r5) {
        r4.skipBytes(8);
        maybeSkipRemainingMetaBoxHeaderBytes(r4);
    L4:
        if (r4.getPosition() >= r5) goto L10;
        int r0 = r4.getPosition();
        int r1 = r4.readInt();
        if (r4.readInt() == 1768715124) goto L7;
        r4.setPosition(r0 + r1);
        goto L4
    L7:
        r4.setPosition(r0);
        return parseIlst(r4, r0 + r1);
    L10:
        return null;
    }

    static VexuData parseVideoExtendedUsageBox(ParsableByteArray r5, int r6, int r7) throws ParserException {
        r5.setPosition(r6 + 8);
        int r0 = r5.getPosition();
        EyesData r1 = null;
    L4:
        if ((r0 - r6) >= r7) goto L13;
        r5.setPosition(r0);
        int r2 = r5.readInt();
        if (r2 <= 0) goto L8;
        boolean r3 = true;
    L9:
        ExtractorUtil.checkContainerInput(r3, "childAtomSize must be positive");
        if (r5.readInt() != 1702454643) goto L12;
        r1 = parseStereoViewBox(r5, r0, r2);
    L12:
        r0 = r0 + r2;
        goto L4
    L8:
        r3 = false;
        goto L9
    L13:
        if (r1 != null) goto L16;
        return null;
    L16:
        return new VexuData(r1);
    }

    private static void parseVideoSampleEntry(ParsableByteArray r41, int r42, int r43, int r44, int r45, int r46, DrmInitData r47, StsdData r48, int r49) throws ParserException {
        int r2 = r44;
        StsdData r4 = r48;
        r41.setPosition((r43 + 8) + 8);
        r41.skipBytes(16);
        int r5 = r41.readUnsignedShort();
        int r6 = r41.readUnsignedShort();
        int r9 = 8;
        int r10 = 8;
        r41.skipBytes(50);
        int r11 = r41.getPosition();
        int r14 = r42;
        if (r14 != 1701733238) goto L13;
        Pair<Integer, TrackEncryptionBox> r12 = parseSampleEntryEncryptionData(r41, r43, r2);
        if (r12 == null) goto L11;
        r14 = ((Integer) r12.first).intValue();
        if (r47 != null) goto L9;
        DrmInitData r15 = null;
    L10:
        r4.trackEncryptionBoxes[r49] = (TrackEncryptionBox) r12.second;
    L12:
        r41.setPosition(r11);
    L14:
        String r3 = null;
        if (r14 != 1831958048) goto L18;
        r3 = MimeTypes.VIDEO_MPEG;
    L20:
        List<byte[]> r13 = null;
        String r16 = null;
        EsdsData r19 = null;
        ByteBuffer r25 = null;
        boolean r410 = false;
        int r7 = -1;
        int r18 = -1;
        int r1 = -1;
        byte[] r20 = null;
        int r8 = -1;
        DrmInitData r17 = r15;
        NalUnitUtil.H265VpsData r110 = null;
        float r21 = 1.0f;
        int r22 = -1;
    L21:
        int r23 = r5;
        if ((r11 - r43) >= r2) goto L224;
        r41.setPosition(r11);
        int r24 = r41.getPosition();
        int r411 = r11;
        int r111 = r41.readInt();
        if (r111 != 0) goto L28;
        String r26 = r16;
        if ((r41.getPosition() - r43) == r2) goto L225;
    L29:
        if (r111 <= 0) goto L31;
        boolean r112 = true;
    L32:
        int r27 = 1;
        ExtractorUtil.checkContainerInput(r112, "childAtomSize must be positive");
        int r28 = r41.readInt();
        if (r28 != 1635148611) goto L42;
        if (r3 != null) goto L36;
        boolean r113 = true;
    L37:
        int r29 = r14;
        ExtractorUtil.checkContainerInput(r113, null);
        r41.setPosition(r24 + 8);
        AvcConfig r114 = AvcConfig.parse(r41);
        r13 = r114.initializationData;
        r4.nalUnitLengthFieldLength = r114.nalUnitLengthFieldLength;
        if (r410 == true) goto L40;
        float r115 = r114.pixelWidthHeightRatio;
    L41:
        String r30 = r114.codecs;
        int r31 = r114.maxNumReorderFrames;
        r1 = r114.colorSpace;
        r22 = r114.colorRange;
        r8 = r114.colorTransfer;
        r9 = r114.bitdepthLuma;
        r10 = r114.bitdepthChroma;
        r18 = r31;
        r7 = r7;
        r16 = r30;
        r3 = MimeTypes.VIDEO_H264;
        boolean r32 = r410;
        r21 = r115;
    L223:
        r11 = r411 + r111;
        r2 = r44;
        r4 = r48;
        r410 = r32;
        r5 = r23;
        r14 = r29;
        r7 = r7;
        goto L21
    L40:
        r115 = r21;
        goto L41
    L36:
        r113 = false;
        goto L37
    L42:
        r29 = r14;
        if (r28 != 1752589123) goto L56;
        if (r3 != null) goto L46;
        boolean r116 = true;
    L47:
        ExtractorUtil.checkContainerInput(r116, null);
        r41.setPosition(r24 + 8);
        HevcConfig r117 = HevcConfig.parse(r41);
        List<byte[]> r118 = r117.initializationData;
        r4.nalUnitLengthFieldLength = r117.nalUnitLengthFieldLength;
        if (r410 == true) goto L50;
        float r119 = r117.pixelWidthHeightRatio;
    L51:
        r18 = r117.maxNumReorderPics;
        String r33 = r117.codecs;
        if (r117.stereoMode == (-1)) goto L54;
        r7 = r117.stereoMode;
    L54:
        r1 = r117.colorSpace;
        int r34 = r117.colorRange;
        r8 = r117.colorTransfer;
        int r35 = r117.bitdepthLuma;
        int r36 = r117.bitdepthChroma;
        r7 = r7;
        r110 = r117.vpsData;
        r16 = r33;
        r13 = r118;
        r32 = r410;
        r10 = r36;
        r21 = r119;
        r9 = r35;
        r22 = r34;
        r3 = MimeTypes.VIDEO_H265;
        goto L223
    L50:
        r119 = r21;
        goto L51
    L46:
        r116 = false;
        goto L47
    L56:
        if (r28 != 1818785347) goto L102;
        ExtractorUtil.checkContainerInput(MimeTypes.VIDEO_H265.equals(r3), "lhvC must follow hvcC atom");
        if (r110 != null) goto L60;
    L62:
        boolean r120 = false;
    L63:
        ExtractorUtil.checkContainerInput(r120, "must have at least two layers");
        r41.setPosition(r24 + 8);
        HevcConfig r121 = HevcConfig.parseLayered(r41, (NalUnitUtil.H265VpsData) Assertions.checkNotNull(r110));
        if (r4.nalUnitLengthFieldLength != r121.nalUnitLengthFieldLength) goto L66;
        boolean r37 = true;
    L67:
        ExtractorUtil.checkContainerInput(r37, "nalUnitLengthFieldLength must be same for both hvcC and lhvC atoms");
        if (r121.colorSpace == (-1)) goto L75;
        if (r1 != r121.colorSpace) goto L72;
        boolean r38 = true;
    L73:
        ExtractorUtil.checkContainerInput(r38, "colorSpace must be the same for both views");
        goto L75
    L72:
        r38 = false;
    L75:
        if (r121.colorRange == (-1)) goto L82;
        if (r22 != r121.colorRange) goto L79;
        boolean r39 = true;
    L80:
        ExtractorUtil.checkContainerInput(r39, "colorRange must be the same for both views");
        goto L82
    L79:
        r39 = false;
    L82:
        if (r121.colorTransfer == (-1)) goto L89;
        if (r8 != r121.colorTransfer) goto L86;
        boolean r40 = true;
    L87:
        ExtractorUtil.checkContainerInput(r40, "colorTransfer must be the same for both views");
        goto L89
    L86:
        r40 = false;
    L89:
        if (r9 != r121.bitdepthLuma) goto L91;
        boolean r50 = true;
    L92:
        ExtractorUtil.checkContainerInput(r50, "bitdepthLuma must be the same for both views");
        if (r10 != r121.bitdepthChroma) goto L95;
        boolean r51 = true;
    L96:
        ExtractorUtil.checkContainerInput(r51, "bitdepthChroma must be the same for both views");
        r3 = MimeTypes.VIDEO_MV_HEVC;
        if (r13 == null) goto L99;
        r13 = ImmutableList.builder().addAll(r13).addAll(r121.initializationData).build();
    L100:
        r16 = r121.codecs;
        r7 = r7;
        r32 = r410;
        goto L223
    L99:
        ExtractorUtil.checkContainerInput(false, "initializationData must be already set from hvcC atom");
        goto L100
    L95:
        r51 = false;
        goto L96
    L91:
        r50 = false;
        goto L92
    L66:
        r37 = false;
        goto L67
    L60:
        if (r110.layerInfos.size() < 2) goto L62;
        r120 = true;
        goto L63
    L102:
        if (r28 != 1986361461) goto L120;
        VexuData r52 = parseVideoExtendedUsageBox(r41, r24, r111);
        if (r52 != null) goto L106;
    L118:
        r7 = r7;
        r16 = r26;
        r32 = r410;
        goto L223
    L106:
        if (VexuData.access$600(r52) == null) goto L118;
        if (r110 == null) goto L112;
        if (r110.layerInfos.size() < 2) goto L112;
        ExtractorUtil.checkContainerInput(r52.hasBothEyeViews(), "both eye views must be marked as available");
        ExtractorUtil.checkContainerInput(!StriData.access$800(EyesData.access$700(VexuData.access$600(r52))), "for MV-HEVC, eye_views_reversed must be set to false");
    L112:
        if (r7 != (-1)) goto L118;
        if (StriData.access$800(EyesData.access$700(VexuData.access$600(r52))) == false) goto L116;
        int r122 = 5;
    L117:
        r7 = r122;
        goto L118
    L116:
        r122 = 4;
        goto L117
    L120:
        if (r28 == 1685480259) goto L218;
        if (r28 == 1685485123) goto L218;
        if (r28 != 1987076931) goto L146;
        if (r3 != null) goto L128;
        boolean r123 = true;
    L129:
        ExtractorUtil.checkContainerInput(r123, null);
        if (r29 != 1987063864) goto L132;
        String r53 = MimeTypes.VIDEO_VP8;
    L133:
        r41.setPosition(r24 + 12);
        byte r54 = (byte) r41.readUnsignedByte();
        r29 = r29;
        byte r124 = (byte) r41.readUnsignedByte();
        int r210 = r41.readUnsignedByte();
        r9 = r210 >> 4;
        byte r125 = (byte) ((r210 >> 1) & 7);
        if (r53.equals(MimeTypes.VIDEO_VP9) == false) goto L137;
        r13 = CodecSpecificDataUtil.buildVp9CodecPrivateInitializationData(r54, r124, (byte) r9, r125);
    L137:
        if ((r210 & 1) == 0) goto L139;
        boolean r126 = true;
    L140:
        int r211 = r41.readUnsignedByte();
        int r310 = r41.readUnsignedByte();
        r1 = ColorInfo.isoColorPrimariesToColorSpace(r211);
        if (r126 == true) goto L144;
        r27 = 2;
    L144:
        r8 = ColorInfo.isoTransferCharacteristicsToColorTransfer(r310);
        r3 = r53;
        r7 = r7;
        r16 = r26;
        r22 = r27;
        r10 = r9;
        r32 = r410;
        goto L223
    L139:
        r126 = false;
        goto L140
    L132:
        r53 = MimeTypes.VIDEO_VP9;
        goto L133
    L128:
        r123 = false;
        goto L129
    L146:
        if (r28 != 1635135811) goto L149;
        r3 = MimeTypes.VIDEO_AV1;
        int r55 = r111 - 8;
        byte[] r127 = new byte[r55];
        r41.readBytes(r127, 0, r55);
        r13 = ImmutableList.m213of(r127);
        r41.setPosition(r24 + 8);
        ColorInfo r128 = parseAv1c(r41);
        r9 = r128.lumaBitdepth;
        r10 = r128.chromaBitdepth;
        r1 = r128.colorSpace;
        r22 = r128.colorRange;
        r8 = r128.colorTransfer;
        r7 = r7;
        r16 = r26;
        r32 = r410;
        goto L223
    L149:
        if (r28 != 1668050025) goto L155;
        if (r25 != null) goto L152;
        ByteBuffer r56 = allocateHdrStaticInfo();
    L153:
        r56.position(21);
        r56.putShort(r41.readShort());
        r56.putShort(r41.readShort());
        r25 = r56;
        r7 = r7;
        r16 = r26;
        r32 = r410;
        goto L223
    L152:
        r56 = r25;
        goto L153
    L155:
        if (r28 != 1835295606) goto L160;
        if (r25 != null) goto L158;
        ByteBuffer r57 = allocateHdrStaticInfo();
    L159:
        short r129 = r41.readShort();
        short r130 = r41.readShort();
        NalUnitUtil.H265VpsData r212 = r110;
        short r131 = r41.readShort();
        int r311 = r10;
        short r132 = r41.readShort();
        int r312 = r9;
        short r58 = r41.readShort();
        int r313 = r22;
        short r59 = r41.readShort();
        List<byte[]> r314 = r13;
        short r133 = r41.readShort();
        r7 = r7;
        short r60 = r41.readShort();
        long r213 = r41.readUnsignedInt();
        long r315 = r41.readUnsignedInt();
        r57.position(1);
        r57.putShort(r58);
        r57.putShort(r59);
        r57.putShort(r129);
        r57.putShort(r130);
        r57.putShort(r131);
        r57.putShort(r132);
        r57.putShort(r133);
        r57.putShort(r60);
        r57.putShort((short) (r213 / Renderer.DEFAULT_DURATION_TO_PROGRESS_US));
        r57.putShort((short) (r315 / Renderer.DEFAULT_DURATION_TO_PROGRESS_US));
        r32 = r410;
        r25 = r57;
        r110 = r212;
        r16 = r26;
        r10 = r311;
        r9 = r312;
        r22 = r313;
        r13 = r314;
        r3 = r3;
        goto L223
    L158:
        r57 = r25;
        goto L159
    L160:
        String r316 = r3;
        r7 = r7;
        int r317 = r22;
        int r318 = r9;
        int r319 = r10;
        List<byte[]> r320 = r13;
        NalUnitUtil.H265VpsData r214 = r110;
        if (r28 != 1681012275) goto L167;
        if (r316 != null) goto L164;
        boolean r134 = true;
    L165:
        ExtractorUtil.checkContainerInput(r134, null);
        r3 = MimeTypes.VIDEO_H263;
        r32 = r410;
        r110 = r214;
        r16 = r26;
        r10 = r319;
        r9 = r318;
        r22 = r317;
        r13 = r320;
        goto L223
    L164:
        r134 = false;
        goto L165
    L167:
        if (r28 != 1702061171) goto L177;
        if (r316 != null) goto L170;
        boolean r135 = true;
    L171:
        ExtractorUtil.checkContainerInput(r135, null);
        EsdsData r61 = parseEsdsFromParent(r41, r24);
        String r62 = EsdsData.access$900(r61);
        byte[] r63 = EsdsData.access$1000(r61);
        if (r63 == null) goto L174;
        r13 = ImmutableList.m213of(r63);
    L175:
        r19 = r61;
        r3 = r62;
        r110 = r214;
        r16 = r26;
        r10 = r319;
        r9 = r318;
        r22 = r317;
        r32 = r410;
        goto L223
    L174:
        r13 = r320;
        goto L175
    L170:
        r135 = false;
        goto L171
    L177:
        if (r28 != 1885434736) goto L180;
        r32 = true;
        r21 = parsePaspFromParent(r41, r24);
        r110 = r214;
        r16 = r26;
        r10 = r319;
        r9 = r318;
        r22 = r317;
        r13 = r320;
        r3 = r316;
        goto L223
    L180:
        if (r28 != 1937126244) goto L183;
        r32 = r410;
        r20 = parseProjFromParent(r41, r24, r111);
        r110 = r214;
        r16 = r26;
        r10 = r319;
        r9 = r318;
        r22 = r317;
        r13 = r320;
        r3 = r316;
        goto L223
    L183:
        if (r28 != 1936995172) goto L196;
        int r64 = r41.readUnsignedByte();
        r41.skipBytes(3);
        if (r64 == 0) goto L187;
    L193:
        int r65 = r7;
    L194:
        r7 = r65;
        r110 = r214;
        r16 = r26;
        r10 = r319;
        r9 = r318;
        r22 = r317;
        r13 = r320;
        r3 = r316;
        r32 = r410;
        goto L223
    L187:
        switch(r41.readUnsignedByte()) {
            case 0: goto L192;
            case 1: goto L191;
            case 2: goto L190;
            case 3: goto L189;
            default: goto L193;
        };
    L189:
        r65 = 3;
        goto L194
    L190:
        r65 = 2;
        goto L194
    L191:
        r65 = 1;
        goto L194
    L192:
        r65 = 0;
        goto L194
    L196:
        if (r28 == 1668246642) goto L198;
    L216:
        r32 = r410;
        r110 = r214;
        r16 = r26;
        r10 = r319;
        r9 = r318;
        r22 = r317;
        r13 = r320;
        r3 = r316;
        goto L223
    L198:
        if (r1 != (-1)) goto L216;
        if (r8 != (-1)) goto L216;
        int r66 = r41.readInt();
        if (r66 != TYPE_nclx) goto L203;
    L206:
        int r67 = r41.readUnsignedShort();
        int r68 = r41.readUnsignedShort();
        int r136 = 2;
        r41.skipBytes(2);
        if (r111 == 19) goto L209;
    L211:
        boolean r137 = false;
    L212:
        r1 = ColorInfo.isoColorPrimariesToColorSpace(r67);
        if (r137 == false) goto L215;
        r136 = 1;
    L215:
        r32 = r410;
        r8 = ColorInfo.isoTransferCharacteristicsToColorTransfer(r68);
        r22 = r136;
        r110 = r214;
        r16 = r26;
        r10 = r319;
        r9 = r318;
        r13 = r320;
        r3 = r316;
        goto L223
    L209:
        if ((r41.readUnsignedByte() & 128) == 0) goto L211;
        r137 = true;
        goto L212
    L203:
        if (r66 == TYPE_nclc) goto L206;
        Log.m48w(TAG, "Unsupported color type: " + Mp4Box.getBoxTypeString(r66));
    L218:
        DolbyVisionConfig r69 = DolbyVisionConfig.parse(r41);
        if (r69 == null) goto L221;
        r16 = r69.codecs;
        r3 = MimeTypes.VIDEO_DOLBY_VISION;
    L222:
        r32 = r410;
        r110 = r110;
        r10 = r10;
        r9 = r9;
        r22 = r22;
        r13 = r13;
        goto L223
    L221:
        r16 = r26;
        r3 = r3;
        goto L222
    L31:
        r112 = false;
    L225:
        if (r3 != 0) goto L227;
        return;
    L227:
        Format.Builder r70 = new Format.Builder().setId(r45).setSampleMimeType(r3).setCodecs(r26).setWidth(r23).setHeight(r6).setPixelWidthHeightRatio(r21).setRotationDegrees(r46).setProjectionData(r20).setStereoMode(r7).setInitializationData(r13).setMaxNumReorderSamples(r18).setDrmInitData(r17);
        ColorInfo.Builder r0 = new ColorInfo.Builder().setColorSpace(r1).setColorRange(r22).setColorTransfer(r8);
        if (r25 == null) goto L230;
        byte[] r71 = r25.array();
    L231:
        Format.Builder r72 = r70.setColorInfo(r0.setHdrStaticInfo(r71).setLumaBitdepth(r9).setChromaBitdepth(r10).build());
        if (r19 == null) goto L235;
        r72.setAverageBitrate(Ints.saturatedCast(EsdsData.access$1200(r19))).setPeakBitrate(Ints.saturatedCast(EsdsData.access$1100(r19)));
    L235:
        r48.format = r72.build();
        return;
    L230:
        r71 = null;
        goto L231
    L28:
        r26 = r16;
        goto L29
    L224:
        r26 = r16;
        goto L225
    L18:
        if (r14 != 1211250227) goto L20;
        r3 = MimeTypes.VIDEO_H263;
        goto L20
    L9:
        r15 = r47.copyWithSchemeType(((TrackEncryptionBox) r12.second).schemeType);
        goto L10
    L11:
        r15 = r47;
        goto L12
    L13:
        r15 = r47;
        goto L14
    }

    private static Metadata parseXyz(ParsableByteArray r11) {
        short r0 = r11.readShort();
        r11.skipBytes(2);
        String r1 = r11.readString(r0);
        int r4 = Math.max(r1.lastIndexOf(43), r1.lastIndexOf(45));
        return new Metadata(new Metadata.Entry[]{new Mp4LocationData(Float.parseFloat(r1.substring(0, r4)), Float.parseFloat(r1.substring(r4, r1.length() - 1)))});
    L8:
        return null;
    }
}
