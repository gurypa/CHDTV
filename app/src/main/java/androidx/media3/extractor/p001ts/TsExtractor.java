package androidx.media3.extractor.p001ts;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import androidx.media3.common.C0180C;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.text.SubtitleParser;
import androidx.media3.extractor.text.SubtitleTranscodingExtractorOutput;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class TsExtractor implements Extractor {
    private static final long AC3_FORMAT_IDENTIFIER = 1094921523;
    private static final long AC4_FORMAT_IDENTIFIER = 1094921524;
    private static final int BUFFER_SIZE = 9400;
    public static final int DEFAULT_TIMESTAMP_SEARCH_BYTES = 112800;
    private static final long E_AC3_FORMAT_IDENTIFIER = 1161904947;

    @Deprecated
    public static final ExtractorsFactory FACTORY = null;
    public static final int FLAG_EMIT_RAW_SUBTITLE_DATA = 1;
    private static final long HEVC_FORMAT_IDENTIFIER = 1212503619;
    private static final int MAX_PID_PLUS_ONE = 8192;
    public static final int MODE_HLS = 2;
    public static final int MODE_MULTI_PMT = 0;
    public static final int MODE_SINGLE_PMT = 1;
    private static final int SNIFF_TS_PACKET_COUNT = 5;
    public static final int TS_PACKET_SIZE = 188;
    private static final int TS_PAT_PID = 0;
    public static final int TS_STREAM_TYPE_AAC_ADTS = 15;
    public static final int TS_STREAM_TYPE_AAC_LATM = 17;
    public static final int TS_STREAM_TYPE_AC3 = 129;
    public static final int TS_STREAM_TYPE_AC4 = 172;
    public static final int TS_STREAM_TYPE_AIT = 257;
    public static final int TS_STREAM_TYPE_DC2_H262 = 128;
    public static final int TS_STREAM_TYPE_DTS = 138;
    public static final int TS_STREAM_TYPE_DTS_HD = 136;
    public static final int TS_STREAM_TYPE_DTS_UHD = 139;
    public static final int TS_STREAM_TYPE_DVBSUBS = 89;
    public static final int TS_STREAM_TYPE_E_AC3 = 135;
    public static final int TS_STREAM_TYPE_H262 = 2;
    public static final int TS_STREAM_TYPE_H263 = 16;
    public static final int TS_STREAM_TYPE_H264 = 27;
    public static final int TS_STREAM_TYPE_H265 = 36;
    public static final int TS_STREAM_TYPE_HDMV_DTS = 130;
    public static final int TS_STREAM_TYPE_ID3 = 21;
    public static final int TS_STREAM_TYPE_MHAS = 45;
    public static final int TS_STREAM_TYPE_MPA = 3;
    public static final int TS_STREAM_TYPE_MPA_LSF = 4;
    public static final int TS_STREAM_TYPE_SPLICE_INFO = 134;
    public static final int TS_SYNC_BYTE = 71;
    private int bytesSinceLastSync;
    private final SparseIntArray continuityCounters;
    private final TsDurationReader durationReader;
    private final int extractorFlags;
    private boolean hasOutputSeekMap;
    private TsPayloadReader id3Reader;
    private final int mode;
    private ExtractorOutput output;
    private final TsPayloadReader.Factory payloadReaderFactory;
    private int pcrPid;
    private boolean pendingSeekToStart;
    private int remainingPmts;
    private final SubtitleParser.Factory subtitleParserFactory;
    private final List<TimestampAdjuster> timestampAdjusters;
    private final int timestampSearchBytes;
    private final SparseBooleanArray trackIds;
    private final SparseBooleanArray trackPids;
    private boolean tracksEnded;
    private TsBinarySearchSeeker tsBinarySearchSeeker;
    private final ParsableByteArray tsPacketBuffer;
    private final SparseArray<TsPayloadReader> tsPayloadReaders;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    private class PatReader implements SectionPayloadReader {
        private final ParsableBitArray patScratch;
        final /* synthetic */ TsExtractor this$0;

        public PatReader(TsExtractor r2) {
            this.this$0 = r2;
            this.patScratch = new ParsableBitArray(new byte[4]);
        }

        @Override // androidx.media3.extractor.p001ts.SectionPayloadReader
        public void consume(ParsableByteArray r12) {
            if (r12.readUnsignedByte() == 0) goto L6;
            return;
        L6:
            if ((r12.readUnsignedByte() & 128) != 0) goto L8;
            return;
        L8:
            r12.skipBytes(6);
            int r2 = r12.bytesLeft() / 4;
            int r4 = 0;
        L9:
            if (r4 >= r2) goto L18;
            r12.readBytes(this.patScratch, 4);
            int r5 = this.patScratch.readBits(16);
            this.patScratch.skipBits(3);
            if (r5 != 0) goto L13;
            this.patScratch.skipBits(13);
        L16:
            r4 = r4 + 1;
            goto L9
        L13:
            int r6 = this.patScratch.readBits(13);
            if (TsExtractor.access$000(this.this$0).get(r6) != null) goto L16;
            TsExtractor.access$000(this.this$0).put(r6, new SectionReader(new PmtReader(this.this$0, r6)));
            TsExtractor.access$108(this.this$0);
            goto L16
        L18:
            if (TsExtractor.access$200(this.this$0) == 2) goto L25;
            TsExtractor.access$000(this.this$0).remove(0);
            return;
        }

        @Override // androidx.media3.extractor.p001ts.SectionPayloadReader
        public void init(TimestampAdjuster r1, ExtractorOutput r2, TsPayloadReader.TrackIdGenerator r3) {
        }
    }

    private class PmtReader implements SectionPayloadReader {
        private static final int TS_PMT_DESC_AC3 = 106;
        private static final int TS_PMT_DESC_AIT = 111;
        private static final int TS_PMT_DESC_DTS = 123;
        private static final int TS_PMT_DESC_DVBSUBS = 89;
        private static final int TS_PMT_DESC_DVB_EXT = 127;
        private static final int TS_PMT_DESC_DVB_EXT_AC4 = 21;
        private static final int TS_PMT_DESC_DVB_EXT_DTS_HD = 14;
        private static final int TS_PMT_DESC_DVB_EXT_DTS_UHD = 33;
        private static final int TS_PMT_DESC_EAC3 = 122;
        private static final int TS_PMT_DESC_ISO639_LANG = 10;
        private static final int TS_PMT_DESC_REGISTRATION = 5;
        private final int pid;
        private final ParsableBitArray pmtScratch;
        final /* synthetic */ TsExtractor this$0;
        private final SparseIntArray trackIdToPidScratch;
        private final SparseArray<TsPayloadReader> trackIdToReaderScratch;

        public PmtReader(TsExtractor r2, int r3) {
            this.this$0 = r2;
            this.pmtScratch = new ParsableBitArray(new byte[5]);
            this.trackIdToReaderScratch = new SparseArray();
            this.trackIdToPidScratch = new SparseIntArray();
            this.pid = r3;
        }

        private TsPayloadReader.EsInfo readEsInfo(ParsableByteArray r17, int r18) {
            int r1 = r17.getPosition();
            int r2 = r1 + r18;
            int r8 = -1;
            String r9 = null;
            int r10 = 0;
            ArrayList r11 = null;
        L4:
            if (r17.getPosition() >= r2) goto L58;
            int r3 = r17.readUnsignedByte();
            int r5 = r17.getPosition() + r17.readUnsignedByte();
            if (r5 > r2) goto L58;
            if (r3 != 5) goto L24;
            long r6 = r17.readUnsignedInt();
            if (r6 != TsExtractor.AC3_FORMAT_IDENTIFIER) goto L14;
            r8 = TsExtractor.TS_STREAM_TYPE_AC3;
        L57:
            r17.skipBytes(r5 - r17.getPosition());
            goto L4
        L14:
            if (r6 != TsExtractor.E_AC3_FORMAT_IDENTIFIER) goto L17;
            r8 = TsExtractor.TS_STREAM_TYPE_E_AC3;
            goto L57
        L17:
            if (r6 != TsExtractor.AC4_FORMAT_IDENTIFIER) goto L20;
            r8 = TsExtractor.TS_STREAM_TYPE_AC4;
            goto L57
        L20:
            if (r6 != TsExtractor.HEVC_FORMAT_IDENTIFIER) goto L57;
            r8 = 36;
            goto L57
        L24:
            if (r3 != TS_PMT_DESC_AC3) goto L27;
            r8 = 129;
            goto L57
        L27:
            if (r3 != TS_PMT_DESC_EAC3) goto L30;
            r8 = 135;
            goto L57
        L30:
            if (r3 != TS_PMT_DESC_DVB_EXT) goto L42;
            int r7 = r17.readUnsignedByte();
            if (r7 != 21) goto L35;
            r8 = TsExtractor.TS_STREAM_TYPE_AC4;
            goto L57
        L35:
            if (r7 != 14) goto L38;
            r8 = TsExtractor.TS_STREAM_TYPE_DTS_HD;
            goto L57
        L38:
            if (r7 != 33) goto L57;
            r8 = TsExtractor.TS_STREAM_TYPE_DTS_UHD;
            goto L57
        L42:
            if (r3 != TS_PMT_DESC_DTS) goto L45;
            r8 = 138;
            goto L57
        L45:
            if (r3 != 10) goto L48;
            r9 = r17.readString(3).trim();
            r10 = r17.readUnsignedByte();
            goto L57
        L48:
            if (r3 != 89) goto L55;
            ArrayList r12 = new ArrayList();
        L51:
            if (r17.getPosition() >= r5) goto L53;
            String r13 = r17.readString(3).trim();
            int r14 = r17.readUnsignedByte();
            byte[] r15 = new byte[4];
            r17.readBytes(r15, 0, 4);
            r12.add(new TsPayloadReader.DvbSubtitleInfo(r13, r14, r15));
            goto L51
        L53:
            r11 = r12;
            r8 = 89;
            goto L57
        L55:
            if (r3 != TS_PMT_DESC_AIT) goto L57;
            r8 = 257;
        L58:
            r17.setPosition(r2);
            return new TsPayloadReader.EsInfo(r8, r9, r10, r11, Arrays.copyOfRange(r17.getData(), r1, r2));
        }

        @Override // androidx.media3.extractor.p001ts.SectionPayloadReader
        public void consume(ParsableByteArray r23) {
            if (r23.readUnsignedByte() == 2) goto L6;
            return;
        L6:
            if (TsExtractor.access$200(this.this$0) != 1) goto L8;
        L13:
            TimestampAdjuster r4 = (TimestampAdjuster) TsExtractor.access$300(this.this$0).get(0);
        L15:
            if ((r23.readUnsignedByte() & 128) != 0) goto L17;
            return;
        L17:
            r23.skipBytes(1);
            int r8 = r23.readUnsignedShort();
            int r9 = 3;
            r23.skipBytes(3);
            r23.readBytes(this.pmtScratch, 2);
            this.pmtScratch.skipBits(3);
            int r12 = 13;
            TsExtractor.access$402(this.this$0, this.pmtScratch.readBits(13));
            r23.readBytes(this.pmtScratch, 2);
            int r11 = 4;
            this.pmtScratch.skipBits(4);
            r23.skipBytes(this.pmtScratch.readBits(12));
            int r5 = 21;
            if (TsExtractor.access$200(this.this$0) == 2) goto L20;
        L24:
            this.trackIdToReaderScratch.clear();
            this.trackIdToPidScratch.clear();
            int r3 = r23.bytesLeft();
        L25:
            if (r3 <= 0) goto L49;
            r23.readBytes(this.pmtScratch, 5);
            int r6 = this.pmtScratch.readBits(8);
            this.pmtScratch.skipBits(r9);
            int r14 = this.pmtScratch.readBits(r12);
            this.pmtScratch.skipBits(r11);
            int r10 = this.pmtScratch.readBits(12);
            TsPayloadReader.EsInfo r13 = readEsInfo(r23, r10);
            if (r6 == 6) goto L29;
            if (r6 == 5) goto L29;
        L30:
            r3 = r3 - (r10 + 5);
            if (TsExtractor.access$200(this.this$0) != 2) goto L33;
            int r15 = r6;
        L35:
            if (TsExtractor.access$800(this.this$0).get(r15) == true) goto L48;
            if (TsExtractor.access$200(this.this$0) != 2) goto L41;
            if (r6 != r5) goto L41;
            TsPayloadReader r16 = TsExtractor.access$500(this.this$0);
        L43:
            if (TsExtractor.access$200(this.this$0) == 2) goto L45;
        L46:
            this.trackIdToPidScratch.put(r15, r14);
            this.trackIdToReaderScratch.put(r15, r16);
            goto L48
        L45:
            if (r14 >= this.trackIdToPidScratch.get(r15, 8192)) goto L48;
        L41:
            r16 = TsExtractor.access$600(this.this$0).createPayloadReader(r6, r13);
        L48:
            r5 = 21;
            r9 = 3;
            r11 = 4;
            r12 = 13;
            goto L25
        L33:
            r15 = r14;
        L29:
            r6 = r13.streamType;
            goto L30
        L49:
            int r7 = this.trackIdToPidScratch.size();
            int r17 = 0;
        L50:
            if (r17 >= r7) goto L61;
            int r18 = this.trackIdToPidScratch.keyAt(r17);
            int r19 = this.trackIdToPidScratch.valueAt(r17);
            TsExtractor.access$800(this.this$0).put(r18, true);
            TsExtractor.access$900(this.this$0).put(r19, true);
            TsPayloadReader r110 = this.trackIdToReaderScratch.valueAt(r17);
            if (r110 == null) goto L59;
            if (r110 == TsExtractor.access$500(this.this$0)) goto L57;
            r110.init(r4, TsExtractor.access$700(this.this$0), new TsPayloadReader.TrackIdGenerator(r8, r18, 8192));
        L57:
            TsExtractor.access$000(this.this$0).put(r19, r110);
        L59:
            r17 = r17 + 1;
            goto L50
        L61:
            if (TsExtractor.access$200(this.this$0) == 2) goto L63;
            int r20 = 0;
            TsExtractor.access$000(this.this$0).remove(this.pid);
            TsExtractor r21 = this.this$0;
            if (TsExtractor.access$200(this.this$0) == 1) goto L69;
            r20 = TsExtractor.access$100(this.this$0) - 1;
        L69:
            TsExtractor.access$102(r21, r20);
            if (TsExtractor.access$100(this.this$0) != 0) goto L82;
            TsExtractor.access$700(this.this$0).endTracks();
            TsExtractor.access$1002(this.this$0, true);
            return;
        L82:
            return;
        L63:
            if (TsExtractor.access$1000(this.this$0) == true) goto L80;
            TsExtractor.access$700(this.this$0).endTracks();
            TsExtractor.access$102(this.this$0, 0);
            TsExtractor.access$1002(this.this$0, true);
            return;
        L80:
            return;
        L20:
            if (TsExtractor.access$500(this.this$0) != null) goto L24;
            TsPayloadReader.EsInfo r111 = new TsPayloadReader.EsInfo(21, null, 0, null, Util.EMPTY_BYTE_ARRAY);
            TsExtractor.access$502(this.this$0, TsExtractor.access$600(this.this$0).createPayloadReader(21, r111));
            if (TsExtractor.access$500(this.this$0) == null) goto L24;
            TsExtractor.access$500(this.this$0).init(r4, TsExtractor.access$700(this.this$0), new TsPayloadReader.TrackIdGenerator(r8, 21, 8192));
            goto L24
        L8:
            if (TsExtractor.access$200(this.this$0) == 2) goto L13;
            if (TsExtractor.access$100(this.this$0) == 1) goto L13;
            r4 = new TimestampAdjuster(((TimestampAdjuster) TsExtractor.access$300(this.this$0).get(0)).getFirstSampleTimestampUs());
            TsExtractor.access$300(this.this$0).add(r4);
            goto L15
        }

        @Override // androidx.media3.extractor.p001ts.SectionPayloadReader
        public void init(TimestampAdjuster r1, ExtractorOutput r2, TsPayloadReader.TrackIdGenerator r3) {
        }
    }

    static {
        FACTORY = new TsExtractor$$ExternalSyntheticLambda1();
    }

    @Deprecated
    public TsExtractor() {
        this(1, 1, SubtitleParser.Factory.UNSUPPORTED, new TimestampAdjuster(0), new DefaultTsPayloadReaderFactory(0), DEFAULT_TIMESTAMP_SEARCH_BYTES);
    }

    @Deprecated
    public TsExtractor(int r8) {
        this(1, 1, SubtitleParser.Factory.UNSUPPORTED, new TimestampAdjuster(0), new DefaultTsPayloadReaderFactory(r8), DEFAULT_TIMESTAMP_SEARCH_BYTES);
    }

    @Deprecated
    public TsExtractor(int r8, int r9, int r10) {
        this(r8, 1, SubtitleParser.Factory.UNSUPPORTED, new TimestampAdjuster(0), new DefaultTsPayloadReaderFactory(r9), r10);
    }

    public TsExtractor(int r4, int r5, SubtitleParser.Factory r6, TimestampAdjuster r7, TsPayloadReader.Factory r8, int r9) {
        this.payloadReaderFactory = (TsPayloadReader.Factory) Assertions.checkNotNull(r8);
        this.timestampSearchBytes = r9;
        this.mode = r4;
        this.extractorFlags = r5;
        this.subtitleParserFactory = r6;
        if (r4 != 1) goto L5;
    L8:
        this.timestampAdjusters = Collections.singletonList(r7);
    L9:
        this.tsPacketBuffer = new ParsableByteArray(new byte[BUFFER_SIZE], 0);
        this.trackIds = new SparseBooleanArray();
        this.trackPids = new SparseBooleanArray();
        this.tsPayloadReaders = new SparseArray();
        this.continuityCounters = new SparseIntArray();
        this.durationReader = new TsDurationReader(r9);
        this.output = ExtractorOutput.PLACEHOLDER;
        this.pcrPid = -1;
        resetPayloadReaders();
        return;
    L5:
        if (r4 == 2) goto L8;
        this.timestampAdjusters = new ArrayList();
        this.timestampAdjusters.add(r7);
        goto L9
    }

    @Deprecated
    public TsExtractor(int r8, TimestampAdjuster r9, TsPayloadReader.Factory r10) {
        this(r8, 1, SubtitleParser.Factory.UNSUPPORTED, r9, r10, DEFAULT_TIMESTAMP_SEARCH_BYTES);
    }

    @Deprecated
    public TsExtractor(int r8, TimestampAdjuster r9, TsPayloadReader.Factory r10, int r11) {
        this(r8, 1, SubtitleParser.Factory.UNSUPPORTED, r9, r10, r11);
    }

    public TsExtractor(int r8, SubtitleParser.Factory r9) {
        this(1, r8, r9, new TimestampAdjuster(0), new DefaultTsPayloadReaderFactory(0), DEFAULT_TIMESTAMP_SEARCH_BYTES);
    }

    public TsExtractor(SubtitleParser.Factory r8) {
        this(1, 0, r8, new TimestampAdjuster(0), new DefaultTsPayloadReaderFactory(0), DEFAULT_TIMESTAMP_SEARCH_BYTES);
    }

    static /* synthetic */ SparseArray access$000(TsExtractor r1) {
        return r1.tsPayloadReaders;
    }

    static /* synthetic */ int access$100(TsExtractor r1) {
        return r1.remainingPmts;
    }

    static /* synthetic */ boolean access$1000(TsExtractor r1) {
        return r1.tracksEnded;
    }

    static /* synthetic */ boolean access$1002(TsExtractor r0, boolean r1) {
        r0.tracksEnded = r1;
        return r1;
    }

    static /* synthetic */ int access$102(TsExtractor r0, int r1) {
        r0.remainingPmts = r1;
        return r1;
    }

    static /* synthetic */ int access$108(TsExtractor r2) {
        int r0 = r2.remainingPmts;
        r2.remainingPmts = r0 + 1;
        return r0;
    }

    static /* synthetic */ int access$200(TsExtractor r1) {
        return r1.mode;
    }

    static /* synthetic */ List access$300(TsExtractor r1) {
        return r1.timestampAdjusters;
    }

    static /* synthetic */ int access$402(TsExtractor r0, int r1) {
        r0.pcrPid = r1;
        return r1;
    }

    static /* synthetic */ TsPayloadReader access$500(TsExtractor r1) {
        return r1.id3Reader;
    }

    static /* synthetic */ TsPayloadReader access$502(TsExtractor r0, TsPayloadReader r1) {
        r0.id3Reader = r1;
        return r1;
    }

    static /* synthetic */ TsPayloadReader.Factory access$600(TsExtractor r1) {
        return r1.payloadReaderFactory;
    }

    static /* synthetic */ ExtractorOutput access$700(TsExtractor r1) {
        return r1.output;
    }

    static /* synthetic */ SparseBooleanArray access$800(TsExtractor r1) {
        return r1.trackIds;
    }

    static /* synthetic */ SparseBooleanArray access$900(TsExtractor r1) {
        return r1.trackPids;
    }

    private boolean fillBufferWithAtLeastOnePacket(ExtractorInput r8) throws IOException {
        byte[] r0 = this.tsPacketBuffer.getData();
        if ((9400 - this.tsPacketBuffer.getPosition()) >= 188) goto L9;
        int r1 = this.tsPacketBuffer.bytesLeft();
        if (r1 <= 0) goto L7;
        System.arraycopy(r0, this.tsPacketBuffer.getPosition(), r0, 0, r1);
    L7:
        this.tsPacketBuffer.reset(r0, r1);
    L9:
        if (this.tsPacketBuffer.bytesLeft() >= 188) goto L14;
        int r2 = this.tsPacketBuffer.limit();
        int r4 = r8.read(r0, r2, 9400 - r2);
        if (r4 == (-1)) goto L12;
        this.tsPacketBuffer.setLimit(r2 + r4);
        goto L9
    L12:
        return false;
    L14:
        return true;
    }

    private int findEndOfFirstTsPacketInBuffer() throws ParserException {
        int r0 = this.tsPacketBuffer.getPosition();
        int r1 = this.tsPacketBuffer.limit();
        int r2 = TsUtil.findSyncBytePosition(this.tsPacketBuffer.getData(), r0, r1);
        this.tsPacketBuffer.setPosition(r2);
        int r3 = r2 + TS_PACKET_SIZE;
        if (r3 <= r1) goto L11;
        this.bytesSinceLastSync += r2 - r0;
        if (this.mode == 2) goto L7;
    L12:
        return r3;
    L7:
        if (this.bytesSinceLastSync <= 376) goto L12;
        throw ParserException.createForMalformedContainer("Cannot find sync byte. Most likely not a Transport Stream.", null);
    L11:
        this.bytesSinceLastSync = 0;
        goto L12
    }

    static /* synthetic */ Extractor[] lambda$newFactory$0(SubtitleParser.Factory r3) {
        return new Extractor[]{new TsExtractor(r3)};
    }

    static /* synthetic */ Extractor[] lambda$static$1() {
        return new Extractor[]{new TsExtractor(1, SubtitleParser.Factory.UNSUPPORTED)};
    }

    private void maybeOutputSeekMap(long r10) {
        if (this.hasOutputSeekMap == true) goto L9;
        this.hasOutputSeekMap = true;
        if (this.durationReader.getDurationUs() == C0180C.TIME_UNSET) goto L7;
        this.tsBinarySearchSeeker = new TsBinarySearchSeeker(this.durationReader.getPcrTimestampAdjuster(), this.durationReader.getDurationUs(), r10, this.pcrPid, this.timestampSearchBytes);
        this.output.seekMap(this.tsBinarySearchSeeker.getSeekMap());
        return;
    L7:
        this.output.seekMap(new SeekMap.Unseekable(this.durationReader.getDurationUs()));
        return;
    }

    public static ExtractorsFactory newFactory(final SubtitleParser.Factory r1) {
        return new TsExtractor$$ExternalSyntheticLambda0(r1);
    }

    private void resetPayloadReaders() {
        this.trackIds.clear();
        this.tsPayloadReaders.clear();
        SparseArray<TsPayloadReader> r0 = this.payloadReaderFactory.createInitialPayloadReaders();
        int r1 = r0.size();
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        this.tsPayloadReaders.put(r0.keyAt(r2), r0.valueAt(r2));
        r2 = r2 + 1;
        goto L3
    L5:
        this.tsPayloadReaders.put(0, new SectionReader(new PatReader(this)));
        this.id3Reader = null;
    }

    private boolean shouldConsumePacketPayload(int r3) {
        if (this.mode != 2) goto L5;
    L8:
        return true;
    L5:
        if (this.tracksEnded == true) goto L8;
        if (this.trackPids.get(r3, false) == false) goto L8;
        return false;
    }

    @Override // androidx.media3.extractor.Extractor
    public /* synthetic */ List getSniffFailureDetails() {
        return Extractor.CC.$default$getSniffFailureDetails(this);
    }

    @Override // androidx.media3.extractor.Extractor
    public /* synthetic */ Extractor getUnderlyingImplementation() {
        return Extractor.CC.$default$getUnderlyingImplementation(this);
    }

    @Override // androidx.media3.extractor.Extractor
    public void init(ExtractorOutput r3) {
        if ((this.extractorFlags & 1) != 0) goto L5;
        ExtractorOutput r0 = new SubtitleTranscodingExtractorOutput(r3, this.subtitleParserFactory);
    L6:
        this.output = r0;
        return;
    L5:
        r0 = r3;
        goto L6
    }

    /* JADX WARN: Failed to calculate best type for var: r13v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r13v1 ??, new type: int
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
    /* JADX WARN: Failed to calculate best type for var: r13v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r13v3 ??, new type: int
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
    /* JADX WARN: Failed to calculate best type for var: r7v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v1 ??, new type: androidx.media3.extractor.ts.TsPayloadReader
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
    /* JADX WARN: Failed to calculate best type for var: r7v2 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v2 ??, new type: androidx.media3.extractor.ts.TsPayloadReader
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
    /* JADX WARN: Failed to calculate best type for var: r7v5 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r7v5 ??, new type: androidx.media3.extractor.ts.TsPayloadReader
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
    /*  JADX ERROR: Types fix failed
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r13v1 ??, new type: int
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
    @Override // androidx.media3.extractor.Extractor
    public int read(androidx.media3.extractor.ExtractorInput r22, androidx.media3.extractor.PositionHolder r23) throws java.io.IOException {
        /*  JADX ERROR: Simple mode code generation failed
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0020: MOVE (r9v9 ?? I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (1 ??[boolean, int, float, short, byte, char]) A[DECLARE_VAR] in method: androidx.media3.extractor.ts.TsExtractor.read(androidx.media3.extractor.ExtractorInput, androidx.media3.extractor.PositionHolder):int, file: classes.dex
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
            Method dump skipped, instruction units count: 351
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.media3.extractor.p001ts.TsExtractor.read(androidx.media3.extractor.ExtractorInput, androidx.media3.extractor.PositionHolder):int");
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r17, long r19) {
        if (this.mode == 2) goto L5;
        boolean r3 = true;
    L6:
        Assertions.checkState(r3);
        int r4 = this.timestampAdjusters.size();
        int r5 = 0;
    L8:
        if (r5 >= r4) goto L27;
        TimestampAdjuster r9 = this.timestampAdjusters.get(r5);
        if (r9.getTimestampOffsetUs() != C0180C.TIME_UNSET) goto L12;
        boolean r10 = true;
    L13:
        if (r10 == true) goto L23;
        long r14 = r9.getFirstSampleTimestampUs();
        if (r14 != C0180C.TIME_UNSET) goto L17;
    L21:
        boolean r7 = false;
    L22:
        r10 = r7;
        goto L23
    L17:
        if (r14 == 0) goto L21;
        if (r14 == r19) goto L21;
        r7 = true;
    L23:
        if (r10 == false) goto L25;
        r9.reset(r19);
    L25:
        r5 = r5 + 1;
        goto L8
    L12:
        r10 = false;
        goto L13
    L27:
        if (r19 != 0) goto L29;
    L31:
        this.tsPacketBuffer.reset(0);
        this.continuityCounters.clear();
        int r6 = 0;
    L33:
        if (r6 >= this.tsPayloadReaders.size()) goto L35;
        this.tsPayloadReaders.valueAt(r6).seek();
        r6 = r6 + 1;
        goto L33
    L35:
        this.bytesSinceLastSync = 0;
        return;
    L29:
        if (this.tsBinarySearchSeeker == null) goto L31;
        this.tsBinarySearchSeeker.setSeekTargetUs(r19);
        goto L31
    L5:
        r3 = false;
        goto L6
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r8) throws IOException {
        byte[] r0 = this.tsPacketBuffer.getData();
        r8.peekFully(r0, 0, 940);
        int r1 = 0;
    L4:
        if (r1 >= 188) goto L16;
        boolean r3 = true;
        int r4 = 0;
    L7:
        if (r4 >= 5) goto L12;
        if (r0[(r4 * TS_PACKET_SIZE) + r1] != 71) goto L10;
        r4 = r4 + 1;
        goto L7
    L10:
        r3 = false;
    L12:
        if (r3 == true) goto L13;
        r1 = r1 + 1;
        goto L4
    L13:
        r8.skipFully(r1);
        return true;
    L16:
        return false;
    }
}
