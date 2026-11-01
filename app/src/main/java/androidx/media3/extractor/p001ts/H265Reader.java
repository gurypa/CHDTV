package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.CodecSpecificDataUtil;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.container.NalUnitUtil;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import com.google.common.base.Preconditions;
import java.util.Collections;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class H265Reader implements ElementaryStreamReader {
    private String formatId;
    private boolean hasOutputFormat;
    private TrackOutput output;
    private long pesTimeUs;
    private final NalUnitTargetBuffer pps;
    private final boolean[] prefixFlags;
    private final NalUnitTargetBuffer prefixSei;
    private SampleReader sampleReader;
    private final SeiReader seiReader;
    private final ParsableByteArray seiWrapper;
    private final NalUnitTargetBuffer sps;
    private final NalUnitTargetBuffer suffixSei;
    private long totalBytesWritten;
    private final NalUnitTargetBuffer vps;

    private static final class SampleReader {
        private static final int FIRST_SLICE_FLAG_OFFSET = 2;
        private boolean isFirstPrefixNalUnit;
        private boolean isFirstSlice;
        private boolean lookingForFirstSliceFlag;
        private int nalUnitBytesRead;
        private boolean nalUnitHasKeyframeData;
        private long nalUnitPosition;
        private long nalUnitTimeUs;
        private final TrackOutput output;
        private boolean readingPrefix;
        private boolean readingSample;
        private boolean sampleIsKeyframe;
        private long samplePosition;
        private long sampleTimeUs;

        public SampleReader(TrackOutput r1) {
            this.output = r1;
        }

        private static boolean isPrefixNalUnit(int r1) {
            if (32 > r1) goto L7;
            if (r1 > 35) goto L7;
        L8:
            return true;
        L7:
            if (r1 == 39) goto L8;
            return false;
        }

        private static boolean isVclBodyNalUnit(int r1) {
            if (r1 >= 32) goto L5;
        L8:
            return true;
        L5:
            if (r1 == 40) goto L8;
            return false;
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
            int r5 = (int) (this.nalUnitPosition - this.samplePosition);
            this.output.sampleMetadata(this.sampleTimeUs, r0 ? 1 : 0, r5, r9, null);
        }

        public void end(long r3) {
            this.sampleIsKeyframe = this.nalUnitHasKeyframeData;
            outputSample((int) (r3 - this.nalUnitPosition));
            this.samplePosition = this.nalUnitPosition;
            this.nalUnitPosition = r3;
            outputSample(0);
            this.readingSample = false;
        }

        public void endNalUnit(long r3, int r5, boolean r6) {
            if (this.readingPrefix == false) goto L8;
            if (this.isFirstSlice == false) goto L8;
            this.sampleIsKeyframe = this.nalUnitHasKeyframeData;
            this.readingPrefix = false;
            return;
        L8:
            if (this.isFirstPrefixNalUnit == false) goto L10;
        L11:
            if (r6 == true) goto L13;
        L15:
            this.samplePosition = this.nalUnitPosition;
            this.sampleTimeUs = this.nalUnitTimeUs;
            this.sampleIsKeyframe = this.nalUnitHasKeyframeData;
            this.readingSample = true;
            return;
        L13:
            if (this.readingSample == false) goto L15;
            outputSample(r5 + ((int) (r3 - this.nalUnitPosition)));
            goto L15
        L10:
            if (this.isFirstSlice == true) goto L11;
        }

        public void readNalUnitData(byte[] r4, int r5, int r6) {
            if (this.lookingForFirstSliceFlag == false) goto L13;
            int r0 = (r5 + 2) - this.nalUnitBytesRead;
            if (r0 < r6) goto L7;
            this.nalUnitBytesRead += r6 - r5;
            return;
        L7:
            if ((r4[r0] & 128) == 0) goto L9;
            boolean r1 = true;
        L10:
            this.isFirstSlice = r1;
            this.lookingForFirstSliceFlag = false;
            return;
        L9:
            r1 = false;
            goto L10
        }

        public void reset() {
            this.lookingForFirstSliceFlag = false;
            this.isFirstSlice = false;
            this.isFirstPrefixNalUnit = false;
            this.readingSample = false;
            this.readingPrefix = false;
        }

        public void startNalUnit(long r4, int r6, int r7, long r8, boolean r10) {
            boolean r0 = false;
            this.isFirstSlice = false;
            this.isFirstPrefixNalUnit = false;
            this.nalUnitTimeUs = r8;
            this.nalUnitBytesRead = 0;
            this.nalUnitPosition = r4;
            if (isVclBodyNalUnit(r7) == true) goto L15;
            if (this.readingSample == false) goto L12;
            if (this.readingPrefix == true) goto L12;
            if (r10 == false) goto L10;
            outputSample(r6);
        L10:
            this.readingSample = false;
        L12:
            if (isPrefixNalUnit(r7) == false) goto L15;
            this.isFirstPrefixNalUnit = !this.readingPrefix;
            this.readingPrefix = true;
        L15:
            if (r7 >= 16) goto L17;
        L19:
            boolean r1 = false;
        L20:
            this.nalUnitHasKeyframeData = r1;
            if (this.nalUnitHasKeyframeData == false) goto L23;
        L24:
            r0 = true;
        L25:
            this.lookingForFirstSliceFlag = r0;
            return;
        L23:
            if (r7 > 9) goto L25;
        L17:
            if (r7 > 21) goto L19;
            r1 = true;
            goto L20
        }
    }

    public H265Reader(SeiReader r4) {
        this.seiReader = r4;
        this.prefixFlags = new boolean[3];
        this.vps = new NalUnitTargetBuffer(32, 128);
        this.sps = new NalUnitTargetBuffer(33, 128);
        this.pps = new NalUnitTargetBuffer(34, 128);
        this.prefixSei = new NalUnitTargetBuffer(39, 128);
        this.suffixSei = new NalUnitTargetBuffer(40, 128);
        this.pesTimeUs = C0180C.TIME_UNSET;
        this.seiWrapper = new ParsableByteArray();
    }

    @EnsuresNonNull({"output", "sampleReader"})
    private void assertTracksCreated() {
        Assertions.checkStateNotNull(this.output);
        Util.castNonNull(this.sampleReader);
    }

    @RequiresNonNull({"output", "sampleReader"})
    private void endNalUnit(long r5, int r7, int r8, long r9) {
        this.sampleReader.endNalUnit(r5, r7, this.hasOutputFormat);
        if (this.hasOutputFormat == true) goto L16;
        this.vps.endNalUnit(r8);
        this.sps.endNalUnit(r8);
        this.pps.endNalUnit(r8);
        if (this.vps.isCompleted() == false) goto L16;
        if (this.sps.isCompleted() == false) goto L16;
        if (this.pps.isCompleted() == false) goto L16;
        Format r0 = parseMediaFormat(this.formatId, this.vps, this.sps, this.pps);
        this.output.format(r0);
        if (r0.maxNumReorderSamples == (-1)) goto L13;
        boolean r1 = true;
    L14:
        Preconditions.checkState(r1);
        this.seiReader.setReorderingQueueSize(r0.maxNumReorderSamples);
        this.hasOutputFormat = true;
        goto L16
    L13:
        r1 = false;
    L16:
        if (this.prefixSei.endNalUnit(r8) == false) goto L19;
        this.seiWrapper.reset(this.prefixSei.nalData, NalUnitUtil.unescapeStream(this.prefixSei.nalData, this.prefixSei.nalLength));
        this.seiWrapper.skipBytes(5);
        this.seiReader.consume(r9, this.seiWrapper);
    L19:
        if (this.suffixSei.endNalUnit(r8) == false) goto L22;
        this.seiWrapper.reset(this.suffixSei.nalData, NalUnitUtil.unescapeStream(this.suffixSei.nalData, this.suffixSei.nalLength));
        this.seiWrapper.skipBytes(5);
        this.seiReader.consume(r9, this.seiWrapper);
        return;
    }

    @RequiresNonNull({"sampleReader"})
    private void nalUnitData(byte[] r2, int r3, int r4) {
        this.sampleReader.readNalUnitData(r2, r3, r4);
        if (this.hasOutputFormat == true) goto L5;
        this.vps.appendToNalUnit(r2, r3, r4);
        this.sps.appendToNalUnit(r2, r3, r4);
        this.pps.appendToNalUnit(r2, r3, r4);
    L5:
        this.prefixSei.appendToNalUnit(r2, r3, r4);
        this.suffixSei.appendToNalUnit(r2, r3, r4);
    }

    private static Format parseMediaFormat(String r10, NalUnitTargetBuffer r11, NalUnitTargetBuffer r12, NalUnitTargetBuffer r13) {
        byte[] r0 = new byte[(r11.nalLength + r12.nalLength) + r13.nalLength];
        System.arraycopy(r11.nalData, 0, r0, 0, r11.nalLength);
        System.arraycopy(r12.nalData, 0, r0, r11.nalLength, r12.nalLength);
        System.arraycopy(r13.nalData, 0, r0, r11.nalLength + r12.nalLength, r13.nalLength);
        NalUnitUtil.H265SpsData r1 = NalUnitUtil.parseH265SpsNalUnit(r12.nalData, 3, r12.nalLength, null);
        String r2 = null;
        if (r1.profileTierLevel == null) goto L6;
        r2 = CodecSpecificDataUtil.buildHevcCodecString(r1.profileTierLevel.generalProfileSpace, r1.profileTierLevel.generalTierFlag, r1.profileTierLevel.generalProfileIdc, r1.profileTierLevel.generalProfileCompatibilityFlags, r1.profileTierLevel.constraintBytes, r1.profileTierLevel.generalLevelIdc);
    L6:
        return new Format.Builder().setId(r10).setSampleMimeType(MimeTypes.VIDEO_H265).setCodecs(r2).setWidth(r1.width).setHeight(r1.height).setColorInfo(new ColorInfo.Builder().setColorSpace(r1.colorSpace).setColorRange(r1.colorRange).setColorTransfer(r1.colorTransfer).setLumaBitdepth(r1.bitDepthLumaMinus8 + 8).setChromaBitdepth(r1.bitDepthChromaMinus8 + 8).build()).setPixelWidthHeightRatio(r1.pixelWidthHeightRatio).setMaxNumReorderSamples(r1.maxNumReorderPics).setInitializationData(Collections.singletonList(r0)).build();
    }

    @RequiresNonNull({"sampleReader"})
    private void startNalUnit(long r9, int r11, int r12, long r13) {
        this.sampleReader.startNalUnit(r9, r11, r12, r13, this.hasOutputFormat);
        if (this.hasOutputFormat == true) goto L5;
        this.vps.startNalUnit(r12);
        this.sps.startNalUnit(r12);
        this.pps.startNalUnit(r12);
    L5:
        this.prefixSei.startNalUnit(r12);
        this.suffixSei.startNalUnit(r12);
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void consume(ParsableByteArray r14) {
        assertTracksCreated();
    L4:
        if (r14.bytesLeft() <= 0) goto L20;
        int r1 = r14.getPosition();
        int r7 = r14.limit();
        byte[] r8 = r14.getData();
        this.totalBytesWritten += (long) r14.bytesLeft();
        this.output.sampleData(r14, r14.bytesLeft());
        int r9 = r1;
    L6:
        if (r9 >= r7) goto L4;
        int r10 = NalUnitUtil.findNalUnit(r8, r9, r7, this.prefixFlags);
        if (r10 == r7) goto L9;
        int r11 = NalUnitUtil.getH265NalUnitType(r8, r10);
        int r12 = r10 - r9;
        if (r12 <= 0) goto L14;
        nalUnitData(r8, r9, r10);
    L14:
        int r3 = r7 - r10;
        long r2 = this.totalBytesWritten - ((long) r3);
        if (r12 >= 0) goto L17;
        int r4 = -r12;
    L18:
        endNalUnit(r2, r3, r4, this.pesTimeUs);
        startNalUnit(r2, r3, r11, this.pesTimeUs);
        r9 = r10 + 3;
        goto L6
    L17:
        r4 = 0;
        goto L18
    L9:
        nalUnitData(r8, r9, r7);
        return;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void createTracks(ExtractorOutput r3, TsPayloadReader.TrackIdGenerator r4) {
        r4.generateNewId();
        this.formatId = r4.getFormatId();
        this.output = r3.track(r4.getTrackId(), 2);
        this.sampleReader = new SampleReader(this.output);
        this.seiReader.createTracks(r3, r4);
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
    public void packetStarted(long r1, int r3) {
        this.pesTimeUs = r1;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void seek() {
        this.totalBytesWritten = 0;
        this.pesTimeUs = C0180C.TIME_UNSET;
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.vps.reset();
        this.sps.reset();
        this.pps.reset();
        this.prefixSei.reset();
        this.suffixSei.reset();
        this.seiReader.flush();
        if (this.sampleReader == null) goto L6;
        this.sampleReader.reset();
        return;
    }
}
