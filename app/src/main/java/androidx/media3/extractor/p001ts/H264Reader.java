package androidx.media3.extractor.p001ts;

import android.util.SparseArray;
import androidx.media3.common.C0180C;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.CodecSpecificDataUtil;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.container.NalUnitUtil;
import androidx.media3.container.ParsableNalUnitBitArray;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import java.util.ArrayList;
import java.util.Arrays;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class H264Reader implements ElementaryStreamReader {
    private final boolean allowNonIdrKeyframes;
    private final boolean detectAccessUnits;
    private String formatId;
    private boolean hasOutputFormat;
    private TrackOutput output;
    private long pesTimeUs;
    private final NalUnitTargetBuffer pps;
    private final boolean[] prefixFlags;
    private boolean randomAccessIndicator;
    private SampleReader sampleReader;
    private final NalUnitTargetBuffer sei;
    private final SeiReader seiReader;
    private final ParsableByteArray seiWrapper;
    private final NalUnitTargetBuffer sps;
    private long totalBytesWritten;

    /* JADX INFO: renamed from: androidx.media3.extractor.ts.H264Reader$1 */
    static /* synthetic */ class C04471 {
    }

    private static final class SampleReader {
        private static final int DEFAULT_BUFFER_SIZE = 128;
        private final boolean allowNonIdrKeyframes;
        private final ParsableNalUnitBitArray bitArray;
        private byte[] buffer;
        private int bufferLength;
        private final boolean detectAccessUnits;
        private boolean isFilling;
        private long nalUnitStartPosition;
        private long nalUnitTimeUs;
        private int nalUnitType;
        private final TrackOutput output;
        private final SparseArray<NalUnitUtil.PpsData> pps;
        private SliceHeaderData previousSliceHeader;
        private boolean randomAccessIndicator;
        private boolean readingSample;
        private boolean sampleIsKeyframe;
        private long samplePosition;
        private long sampleTimeUs;
        private SliceHeaderData sliceHeader;
        private final SparseArray<NalUnitUtil.SpsData> sps;

        private static final class SliceHeaderData {
            private static final int SLICE_TYPE_ALL_I = 7;
            private static final int SLICE_TYPE_I = 2;
            private boolean bottomFieldFlag;
            private boolean bottomFieldFlagPresent;
            private int deltaPicOrderCnt0;
            private int deltaPicOrderCnt1;
            private int deltaPicOrderCntBottom;
            private boolean fieldPicFlag;
            private int frameNum;
            private boolean hasSliceType;
            private boolean idrPicFlag;
            private int idrPicId;
            private boolean isComplete;
            private int nalRefIdc;
            private int picOrderCntLsb;
            private int picParameterSetId;
            private int sliceType;
            private NalUnitUtil.SpsData spsData;

            private SliceHeaderData() {
            }

            /* synthetic */ SliceHeaderData(C04471 r1) {
                this();
            }

            static /* synthetic */ boolean access$100(SliceHeaderData r1, SliceHeaderData r2) {
                return r1.isFirstVclNalUnitOfPicture(r2);
            }

            private boolean isFirstVclNalUnitOfPicture(SliceHeaderData r7) {
                if (this.isComplete == true) goto L6;
                return false;
            L6:
                if (r7.isComplete == true) goto L8;
                return true;
            L8:
                NalUnitUtil.SpsData r0 = (NalUnitUtil.SpsData) Assertions.checkStateNotNull(this.spsData);
                NalUnitUtil.SpsData r3 = (NalUnitUtil.SpsData) Assertions.checkStateNotNull(r7.spsData);
                if (this.frameNum != r7.frameNum) goto L49;
                if (this.picParameterSetId != r7.picParameterSetId) goto L49;
                if (this.fieldPicFlag != r7.fieldPicFlag) goto L49;
                if (this.bottomFieldFlagPresent == false) goto L21;
                if (r7.bottomFieldFlagPresent == false) goto L21;
                if (this.bottomFieldFlag != r7.bottomFieldFlag) goto L49;
            L21:
                if (this.nalRefIdc == r7.nalRefIdc) goto L27;
                if (this.nalRefIdc == 0) goto L49;
                if (r7.nalRefIdc == 0) goto L49;
            L27:
                if (r0.picOrderCountType != 0) goto L35;
                if (r3.picOrderCountType != 0) goto L35;
                if (this.picOrderCntLsb != r7.picOrderCntLsb) goto L49;
                if (this.deltaPicOrderCntBottom != r7.deltaPicOrderCntBottom) goto L49;
            L35:
                if (r0.picOrderCountType != 1) goto L43;
                if (r3.picOrderCountType != 1) goto L43;
                if (this.deltaPicOrderCnt0 != r7.deltaPicOrderCnt0) goto L49;
                if (this.deltaPicOrderCnt1 != r7.deltaPicOrderCnt1) goto L49;
            L43:
                if (this.idrPicFlag != r7.idrPicFlag) goto L49;
                if (this.idrPicFlag == true) goto L47;
                return false;
            L47:
                if (this.idrPicId != r7.idrPicId) goto L49;
                return false;
            L49:
                return true;
            }

            public void clear() {
                this.hasSliceType = false;
                this.isComplete = false;
            }

            public boolean isISlice() {
                if (this.hasSliceType == true) goto L5;
            L9:
                return false;
            L5:
                if (this.sliceType != 7) goto L7;
            L8:
                return true;
            L7:
                if (this.sliceType != 2) goto L9;
                goto L8
            }

            public void setAll(NalUnitUtil.SpsData r2, int r3, int r4, int r5, int r6, boolean r7, boolean r8, boolean r9, boolean r10, int r11, int r12, int r13, int r14, int r15) {
                this.spsData = r2;
                this.nalRefIdc = r3;
                this.sliceType = r4;
                this.frameNum = r5;
                this.picParameterSetId = r6;
                this.fieldPicFlag = r7;
                this.bottomFieldFlagPresent = r8;
                this.bottomFieldFlag = r9;
                this.idrPicFlag = r10;
                this.idrPicId = r11;
                this.picOrderCntLsb = r12;
                this.deltaPicOrderCntBottom = r13;
                this.deltaPicOrderCnt0 = r14;
                this.deltaPicOrderCnt1 = r15;
                this.isComplete = true;
                this.hasSliceType = true;
            }

            public void setSliceType(int r2) {
                this.sliceType = r2;
                this.hasSliceType = true;
            }
        }

        public SampleReader(TrackOutput r4, boolean r5, boolean r6) {
            this.output = r4;
            this.allowNonIdrKeyframes = r5;
            this.detectAccessUnits = r6;
            this.sps = new SparseArray();
            this.pps = new SparseArray();
            C04471 r1 = null;
            this.previousSliceHeader = new SliceHeaderData(r1);
            this.sliceHeader = new SliceHeaderData(r1);
            this.buffer = new byte[128];
            this.bitArray = new ParsableNalUnitBitArray(this.buffer, 0, 0);
            reset();
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        private void outputSample(int r9) {
            if (this.sampleTimeUs != C0180C.TIME_UNSET) goto L5;
            return;
        L5:
            boolean r0 = this.sampleIsKeyframe;
            int r5 = (int) (this.nalUnitStartPosition - this.samplePosition);
            this.output.sampleMetadata(this.sampleTimeUs, r0 ? 1 : 0, r5, r9, null);
        }

        private void setSampleIsKeyframe() {
            if (this.allowNonIdrKeyframes == false) goto L5;
            boolean r0 = this.sliceHeader.isISlice();
        L6:
            boolean r1 = this.sampleIsKeyframe;
            boolean r4 = true;
            if (this.nalUnitType == 5) goto L13;
            if (r0 == true) goto L10;
        L12:
            r4 = false;
            goto L13
        L10:
            if (this.nalUnitType != 1) goto L12;
        L13:
            this.sampleIsKeyframe = r1 | r4;
            return;
        L5:
            r0 = this.randomAccessIndicator;
            goto L6
        }

        public void appendToNalUnit(byte[] r25, int r26, int r27) {
            if (this.isFilling == true) goto L5;
            return;
        L5:
            int r2 = r27 - r26;
            if (this.buffer.length >= (this.bufferLength + r2)) goto L8;
            this.buffer = Arrays.copyOf(this.buffer, (this.bufferLength + r2) * 2);
        L8:
            System.arraycopy(r25, r26, this.buffer, this.bufferLength, r2);
            this.bufferLength += r2;
            this.bitArray.reset(this.buffer, 0, this.bufferLength);
            if (this.bitArray.canReadBits(8) == true) goto L11;
            return;
        L11:
            this.bitArray.skipBit();
            int r11 = this.bitArray.readBits(2);
            this.bitArray.skipBits(5);
            if (this.bitArray.canReadExpGolombCodedNum() == true) goto L14;
            return;
        L14:
            this.bitArray.readUnsignedExpGolombCodedInt();
            if (this.bitArray.canReadExpGolombCodedNum() == true) goto L17;
            return;
        L17:
            int r12 = this.bitArray.readUnsignedExpGolombCodedInt();
            if (this.detectAccessUnits == true) goto L22;
            this.isFilling = false;
            this.sliceHeader.setSliceType(r12);
            return;
        L22:
            if (this.bitArray.canReadExpGolombCodedNum() == true) goto L24;
            return;
        L24:
            int r14 = this.bitArray.readUnsignedExpGolombCodedInt();
            if (this.pps.indexOfKey(r14) >= 0) goto L28;
            this.isFilling = false;
            return;
        L28:
            NalUnitUtil.PpsData r3 = this.pps.get(r14);
            NalUnitUtil.SpsData r10 = this.sps.get(r3.seqParameterSetId);
            if (r10.separateColorPlaneFlag == false) goto L35;
            if (this.bitArray.canReadBits(2) == true) goto L33;
            return;
        L33:
            this.bitArray.skipBits(2);
        L35:
            if (this.bitArray.canReadBits(r10.frameNumLength) == true) goto L37;
            return;
        L37:
            int r13 = this.bitArray.readBits(r10.frameNumLength);
            if (r10.frameMbsOnlyFlag == false) goto L40;
            boolean r15 = false;
            boolean r16 = false;
            boolean r17 = false;
        L51:
            if (this.nalUnitType != 5) goto L53;
            boolean r18 = true;
        L55:
            if (r18 == true) goto L57;
            int r19 = 0;
        L61:
            int r4 = 0;
            int r7 = 0;
            if (r10.picOrderCountType != 0) goto L74;
            if (this.bitArray.canReadBits(r10.picOrderCntLsbLength) == true) goto L66;
            return;
        L66:
            r4 = this.bitArray.readBits(r10.picOrderCntLsbLength);
            if (r3.bottomFieldPicOrderInFramePresentFlag == false) goto L87;
            if (r15 == true) goto L87;
            if (this.bitArray.canReadExpGolombCodedNum() == true) goto L72;
            return;
        L72:
            int r20 = r4;
            int r21 = this.bitArray.readSignedExpGolombCodedInt();
            int r22 = 0;
            int r23 = 0;
        L88:
            this.sliceHeader.setAll(r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23);
            this.isFilling = false;
            return;
        L87:
            r20 = r4;
            r21 = 0;
            r22 = r7;
            r23 = 0;
            goto L88
        L74:
            if (r10.picOrderCountType != 1) goto L87;
            if (r10.deltaPicOrderAlwaysZeroFlag == true) goto L87;
            if (this.bitArray.canReadExpGolombCodedNum() == true) goto L80;
            return;
        L80:
            r7 = this.bitArray.readSignedExpGolombCodedInt();
            if (r3.bottomFieldPicOrderInFramePresentFlag == false) goto L87;
            if (r15 == true) goto L87;
            if (this.bitArray.canReadExpGolombCodedNum() == true) goto L86;
            return;
        L86:
            r20 = 0;
            r21 = 0;
            r22 = r7;
            r23 = this.bitArray.readSignedExpGolombCodedInt();
            goto L88
        L57:
            if (this.bitArray.canReadExpGolombCodedNum() == true) goto L59;
            return;
        L59:
            r19 = this.bitArray.readUnsignedExpGolombCodedInt();
            goto L61
        L53:
            r18 = false;
            goto L55
        L40:
            if (this.bitArray.canReadBits(1) == true) goto L42;
            return;
        L42:
            boolean r5 = this.bitArray.readBit();
            if (r5 == true) goto L45;
            r15 = r5;
            r16 = false;
            r17 = false;
            goto L51
        L45:
            if (this.bitArray.canReadBits(1) == true) goto L47;
            return;
        L47:
            r15 = r5;
            r16 = true;
            r17 = this.bitArray.readBit();
            goto L51
        }

        public void end(long r2) {
            setSampleIsKeyframe();
            this.nalUnitStartPosition = r2;
            outputSample(0);
            this.readingSample = false;
        }

        public boolean endNalUnit(long r3, int r5, boolean r6) {
            if (this.nalUnitType != 9) goto L5;
        L8:
            if (r6 == true) goto L10;
        L12:
            this.samplePosition = this.nalUnitStartPosition;
            this.sampleTimeUs = this.nalUnitTimeUs;
            this.sampleIsKeyframe = false;
            this.readingSample = true;
        L13:
            setSampleIsKeyframe();
            return this.sampleIsKeyframe;
        L10:
            if (this.readingSample == false) goto L12;
            outputSample(r5 + ((int) (r3 - this.nalUnitStartPosition)));
            goto L12
        L5:
            if (this.detectAccessUnits == false) goto L13;
            if (SliceHeaderData.access$100(this.sliceHeader, this.previousSliceHeader) == false) goto L13;
            goto L8
        }

        public boolean needsSpsPps() {
            return this.detectAccessUnits;
        }

        public void putPps(NalUnitUtil.PpsData r3) {
            this.pps.append(r3.picParameterSetId, r3);
        }

        public void putSps(NalUnitUtil.SpsData r3) {
            this.sps.append(r3.seqParameterSetId, r3);
        }

        public void reset() {
            this.isFilling = false;
            this.readingSample = false;
            this.sliceHeader.clear();
        }

        public void startNalUnit(long r4, int r6, long r7, boolean r9) {
            this.nalUnitType = r6;
            this.nalUnitTimeUs = r7;
            this.nalUnitStartPosition = r4;
            this.randomAccessIndicator = r9;
            if (this.allowNonIdrKeyframes == false) goto L7;
            if (this.nalUnitType != 1) goto L7;
        L14:
            SliceHeaderData r0 = this.previousSliceHeader;
            this.previousSliceHeader = this.sliceHeader;
            this.sliceHeader = r0;
            this.sliceHeader.clear();
            this.bufferLength = 0;
            this.isFilling = true;
            return;
        L7:
            if (this.detectAccessUnits == true) goto L9;
            return;
        L9:
            if (this.nalUnitType == 5) goto L14;
            if (this.nalUnitType == 1) goto L14;
            if (this.nalUnitType == 2) goto L14;
        }
    }

    public H264Reader(SeiReader r4, boolean r5, boolean r6) {
        this.seiReader = r4;
        this.allowNonIdrKeyframes = r5;
        this.detectAccessUnits = r6;
        this.prefixFlags = new boolean[3];
        this.sps = new NalUnitTargetBuffer(7, 128);
        this.pps = new NalUnitTargetBuffer(8, 128);
        this.sei = new NalUnitTargetBuffer(6, 128);
        this.pesTimeUs = C0180C.TIME_UNSET;
        this.seiWrapper = new ParsableByteArray();
    }

    @EnsuresNonNull({"output", "sampleReader"})
    private void assertTracksCreated() {
        Assertions.checkStateNotNull(this.output);
        Util.castNonNull(this.sampleReader);
    }

    @RequiresNonNull({"output", "sampleReader"})
    private void endNalUnit(long r9, int r11, int r12, long r13) {
        if (this.hasOutputFormat == true) goto L5;
    L6:
        this.sps.endNalUnit(r12);
        this.pps.endNalUnit(r12);
        if (this.hasOutputFormat == true) goto L14;
        if (this.sps.isCompleted() == true) goto L11;
    L21:
        if (this.sei.endNalUnit(r12) == false) goto L24;
        int r0 = NalUnitUtil.unescapeStream(this.sei.nalData, this.sei.nalLength);
        this.seiWrapper.reset(this.sei.nalData, r0);
        this.seiWrapper.setPosition(4);
        this.seiReader.consume(r13, this.seiWrapper);
    L24:
        if (this.sampleReader.endNalUnit(r9, r11, this.hasOutputFormat) == false) goto L27;
        this.randomAccessIndicator = false;
        return;
    L27:
        return;
    L11:
        if (this.pps.isCompleted() == false) goto L21;
        ArrayList r1 = new ArrayList();
        r1.add(Arrays.copyOf(this.sps.nalData, this.sps.nalLength));
        r1.add(Arrays.copyOf(this.pps.nalData, this.pps.nalLength));
        NalUnitUtil.SpsData r2 = NalUnitUtil.parseSpsNalUnit(this.sps.nalData, 3, this.sps.nalLength);
        NalUnitUtil.PpsData r3 = NalUnitUtil.parsePpsNalUnit(this.pps.nalData, 3, this.pps.nalLength);
        String r4 = CodecSpecificDataUtil.buildAvcCodecString(r2.profileIdc, r2.constraintsFlagsAndReservedZero2Bits, r2.levelIdc);
        this.output.format(new Format.Builder().setId(this.formatId).setSampleMimeType(MimeTypes.VIDEO_H264).setCodecs(r4).setWidth(r2.width).setHeight(r2.height).setColorInfo(new ColorInfo.Builder().setColorSpace(r2.colorSpace).setColorRange(r2.colorRange).setColorTransfer(r2.colorTransfer).setLumaBitdepth(r2.bitDepthLumaMinus8 + 8).setChromaBitdepth(r2.bitDepthChromaMinus8 + 8).build()).setPixelWidthHeightRatio(r2.pixelWidthHeightRatio).setInitializationData(r1).setMaxNumReorderSamples(r2.maxNumReorderFrames).build());
        this.hasOutputFormat = true;
        this.seiReader.setReorderingQueueSize(r2.maxNumReorderFrames);
        this.sampleReader.putSps(r2);
        this.sampleReader.putPps(r3);
        this.sps.reset();
        this.pps.reset();
        goto L21
    L14:
        if (this.sps.isCompleted() == false) goto L17;
        NalUnitUtil.SpsData r5 = NalUnitUtil.parseSpsNalUnit(this.sps.nalData, 3, this.sps.nalLength);
        this.seiReader.setReorderingQueueSize(r5.maxNumReorderFrames);
        this.sampleReader.putSps(r5);
        this.sps.reset();
        goto L21
    L17:
        if (this.pps.isCompleted() == false) goto L21;
        NalUnitUtil.PpsData r6 = NalUnitUtil.parsePpsNalUnit(this.pps.nalData, 3, this.pps.nalLength);
        this.sampleReader.putPps(r6);
        this.pps.reset();
        goto L21
    L5:
        if (this.sampleReader.needsSpsPps() == false) goto L21;
        goto L6
    }

    @RequiresNonNull({"sampleReader"})
    private void nalUnitData(byte[] r2, int r3, int r4) {
        if (this.hasOutputFormat == true) goto L5;
    L6:
        this.sps.appendToNalUnit(r2, r3, r4);
        this.pps.appendToNalUnit(r2, r3, r4);
    L7:
        this.sei.appendToNalUnit(r2, r3, r4);
        this.sampleReader.appendToNalUnit(r2, r3, r4);
        return;
    L5:
        if (this.sampleReader.needsSpsPps() == false) goto L7;
        goto L6
    }

    @RequiresNonNull({"sampleReader"})
    private void startNalUnit(long r9, int r11, long r12) {
        if (this.hasOutputFormat == true) goto L5;
    L6:
        this.sps.startNalUnit(r11);
        this.pps.startNalUnit(r11);
    L7:
        this.sei.startNalUnit(r11);
        this.sampleReader.startNalUnit(r9, r11, r12, this.randomAccessIndicator);
        return;
    L5:
        if (this.sampleReader.needsSpsPps() == false) goto L7;
        goto L6
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void consume(ParsableByteArray r14) {
        assertTracksCreated();
        int r1 = r14.getPosition();
        int r7 = r14.limit();
        byte[] r8 = r14.getData();
        this.totalBytesWritten += (long) r14.bytesLeft();
        this.output.sampleData(r14, r14.bytesLeft());
        int r9 = r1;
    L3:
        int r10 = NalUnitUtil.findNalUnit(r8, r9, r7, this.prefixFlags);
        if (r10 == r7) goto L5;
        int r11 = NalUnitUtil.getNalUnitType(r8, r10);
        int r12 = r10 - r9;
        if (r12 <= 0) goto L10;
        nalUnitData(r8, r9, r10);
    L10:
        int r3 = r7 - r10;
        long r2 = this.totalBytesWritten - ((long) r3);
        if (r12 >= 0) goto L13;
        int r4 = -r12;
    L14:
        endNalUnit(r2, r3, r4, this.pesTimeUs);
        startNalUnit(r2, r11, this.pesTimeUs);
        r9 = r10 + 3;
        goto L3
    L13:
        r4 = 0;
        goto L14
    L5:
        nalUnitData(r8, r9, r7);
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void createTracks(ExtractorOutput r5, TsPayloadReader.TrackIdGenerator r6) {
        r6.generateNewId();
        this.formatId = r6.getFormatId();
        this.output = r5.track(r6.getTrackId(), 2);
        this.sampleReader = new SampleReader(this.output, this.allowNonIdrKeyframes, this.detectAccessUnits);
        this.seiReader.createTracks(r5, r6);
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void packetFinished(boolean r4) {
        assertTracksCreated();
        if (r4 == false) goto L6;
        this.seiReader.flush();
        this.sampleReader.end(this.totalBytesWritten);
        return;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void packetStarted(long r3, int r5) {
        this.pesTimeUs = r3;
        boolean r0 = this.randomAccessIndicator;
        if ((r5 & 2) == 0) goto L5;
        boolean r1 = true;
    L6:
        this.randomAccessIndicator = r0 | r1;
        return;
    L5:
        r1 = false;
        goto L6
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void seek() {
        this.totalBytesWritten = 0;
        this.randomAccessIndicator = false;
        this.pesTimeUs = C0180C.TIME_UNSET;
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.sps.reset();
        this.pps.reset();
        this.sei.reset();
        this.seiReader.flush();
        if (this.sampleReader == null) goto L6;
        this.sampleReader.reset();
        return;
    }
}
