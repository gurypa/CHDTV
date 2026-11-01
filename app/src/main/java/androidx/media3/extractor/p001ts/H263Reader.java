package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.container.NalUnitUtil;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import java.util.Arrays;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
public final class H263Reader implements ElementaryStreamReader {
    private static final float[] PIXEL_WIDTH_HEIGHT_RATIO_BY_ASPECT_RATIO_INFO = null;
    private static final int START_CODE_VALUE_GROUP_OF_VOP = 179;
    private static final int START_CODE_VALUE_MAX_VIDEO_OBJECT = 31;
    private static final int START_CODE_VALUE_UNSET = -1;
    private static final int START_CODE_VALUE_USER_DATA = 178;
    private static final int START_CODE_VALUE_VISUAL_OBJECT = 181;
    private static final int START_CODE_VALUE_VISUAL_OBJECT_SEQUENCE = 176;
    private static final int START_CODE_VALUE_VOP = 182;
    private static final String TAG = "H263Reader";
    private static final int VIDEO_OBJECT_LAYER_SHAPE_RECTANGULAR = 0;
    private final CsdBuffer csdBuffer;
    private String formatId;
    private boolean hasOutputFormat;
    private TrackOutput output;
    private long pesTimeUs;
    private final boolean[] prefixFlags;
    private SampleReader sampleReader;
    private long totalBytesWritten;
    private final NalUnitTargetBuffer userData;
    private final ParsableByteArray userDataParsable;
    private final UserDataReader userDataReader;

    private static final class CsdBuffer {
        private static final byte[] START_CODE = null;
        private static final int STATE_EXPECT_VIDEO_OBJECT_LAYER_START = 3;
        private static final int STATE_EXPECT_VIDEO_OBJECT_START = 2;
        private static final int STATE_EXPECT_VISUAL_OBJECT_START = 1;
        private static final int STATE_SKIP_TO_VISUAL_OBJECT_SEQUENCE_START = 0;
        private static final int STATE_WAIT_FOR_VOP_START = 4;
        public byte[] data;
        private boolean isFilling;
        public int length;
        private int state;
        public int volStartPosition;

        static {
            START_CODE = new byte[]{0, 0, 1};
        }

        public CsdBuffer(int r2) {
            this.data = new byte[r2];
        }

        public void onData(byte[] r4, int r5, int r6) {
            if (this.isFilling == true) goto L5;
            return;
        L5:
            int r0 = r6 - r5;
            if (this.data.length >= (this.length + r0)) goto L8;
            this.data = Arrays.copyOf(this.data, (this.length + r0) * 2);
        L8:
            System.arraycopy(r4, r5, this.data, this.length, r0);
            this.length += r0;
        }

        public boolean onStartCode(int r7, int r8) {
            switch(this.state) {
                case 0: goto L23;
                case 1: goto L19;
                case 2: goto L16;
                case 3: goto L12;
                case 4: goto L7;
                default: goto L5;
            };
        L19:
            if (r7 == H263Reader.START_CODE_VALUE_VISUAL_OBJECT) goto L21;
            Log.m48w(H263Reader.TAG, "Unexpected start code value");
            reset();
        L25:
            onData(START_CODE, 0, START_CODE.length);
            return false;
        L21:
            this.state = 2;
            goto L25
        L5:
            throw new IllegalStateException();
        L7:
            if (r7 == H263Reader.START_CODE_VALUE_GROUP_OF_VOP) goto L9;
            if (r7 != H263Reader.START_CODE_VALUE_VISUAL_OBJECT) goto L25;
        L9:
            this.length -= r8;
            this.isFilling = false;
            return true;
        L12:
            if ((r7 & PsExtractor.VIDEO_STREAM_MASK) == 32) goto L14;
            Log.m48w(H263Reader.TAG, "Unexpected start code value");
            reset();
            goto L25
        L14:
            this.volStartPosition = this.length;
            this.state = 4;
            goto L25
        L16:
            if (r7 <= 31) goto L18;
            Log.m48w(H263Reader.TAG, "Unexpected start code value");
            reset();
            goto L25
        L18:
            this.state = 3;
            goto L25
        L23:
            if (r7 != H263Reader.START_CODE_VALUE_VISUAL_OBJECT_SEQUENCE) goto L25;
            this.state = 1;
            this.isFilling = true;
            goto L25
        }

        public void reset() {
            this.isFilling = false;
            this.length = 0;
            this.state = 0;
        }
    }

    private static final class SampleReader {
        private static final int OFFSET_VOP_CODING_TYPE = 1;
        private static final int VOP_CODING_TYPE_INTRA = 0;
        private boolean lookingForVopCodingType;
        private final TrackOutput output;
        private boolean readingSample;
        private boolean sampleIsKeyframe;
        private long samplePosition;
        private long sampleTimeUs;
        private int startCodeValue;
        private int vopBytesRead;

        public SampleReader(TrackOutput r1) {
            this.output = r1;
        }

        public void onData(byte[] r4, int r5, int r6) {
            if (this.lookingForVopCodingType == false) goto L13;
            int r0 = (r5 + 1) - this.vopBytesRead;
            if (r0 < r6) goto L7;
            this.vopBytesRead += r6 - r5;
            return;
        L7:
            if (((r4[r0] & 192) >> 6) != 0) goto L9;
            boolean r1 = true;
        L10:
            this.sampleIsKeyframe = r1;
            this.lookingForVopCodingType = false;
            return;
        L9:
            r1 = false;
            goto L10
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
        public void onDataEnd(long r10, int r12, boolean r13) {
            if (this.sampleTimeUs == C0180C.TIME_UNSET) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkState(r0);
            if (this.startCodeValue != H263Reader.START_CODE_VALUE_VOP) goto L14;
            if (r13 == false) goto L14;
            if (this.readingSample == false) goto L14;
            int r6 = (int) (r10 - this.samplePosition);
            boolean r5 = this.sampleIsKeyframe;
            this.output.sampleMetadata(this.sampleTimeUs, r5 ? 1 : 0, r6, r12, null);
        L14:
            if (this.startCodeValue == H263Reader.START_CODE_VALUE_GROUP_OF_VOP) goto L17;
            this.samplePosition = r10;
            return;
        L17:
            return;
        L5:
            r0 = false;
            goto L6
        }

        public void onStartCode(int r5, long r6) {
            this.startCodeValue = r5;
            this.sampleIsKeyframe = false;
            boolean r1 = true;
            if (r5 != H263Reader.START_CODE_VALUE_VOP) goto L5;
        L8:
            boolean r3 = true;
        L9:
            this.readingSample = r3;
            if (r5 == H263Reader.START_CODE_VALUE_VOP) goto L13;
            r1 = false;
        L13:
            this.lookingForVopCodingType = r1;
            this.vopBytesRead = 0;
            this.sampleTimeUs = r6;
            return;
        L5:
            if (r5 == H263Reader.START_CODE_VALUE_GROUP_OF_VOP) goto L8;
            r3 = false;
            goto L9
        }

        public void reset() {
            this.readingSample = false;
            this.lookingForVopCodingType = false;
            this.sampleIsKeyframe = false;
            this.startCodeValue = -1;
        }
    }

    static {
        PIXEL_WIDTH_HEIGHT_RATIO_BY_ASPECT_RATIO_INFO = new float[]{1.0f, 1.0f, 1.0909091f, 0.90909094f, 1.4545455f, 1.2121212f, 1.0f};
    }

    public H263Reader() {
        this(null);
    }

    H263Reader(UserDataReader r5) {
        this.userDataReader = r5;
        this.prefixFlags = new boolean[4];
        this.csdBuffer = new CsdBuffer(128);
        this.pesTimeUs = C0180C.TIME_UNSET;
        if (r5 == null) goto L5;
        this.userData = new NalUnitTargetBuffer(START_CODE_VALUE_USER_DATA, 128);
        this.userDataParsable = new ParsableByteArray();
        return;
    L5:
        this.userData = null;
        this.userDataParsable = null;
    }

    private static Format parseCsdBuffer(CsdBuffer r10, int r11, String r12) {
        byte[] r0 = Arrays.copyOf(r10.data, r10.length);
        ParsableBitArray r1 = new ParsableBitArray(r0);
        r1.skipBytes(r11);
        r1.skipBytes(4);
        r1.skipBit();
        r1.skipBits(8);
        if (r1.readBit() == false) goto L5;
        r1.skipBits(4);
        r1.skipBits(3);
    L5:
        int r2 = r1.readBits(4);
        if (r2 != 15) goto L13;
        int r8 = r1.readBits(8);
        int r3 = r1.readBits(8);
        if (r3 != 0) goto L10;
        Log.m48w(TAG, "Invalid aspect ratio");
        float r4 = 1.0f;
    L17:
        if (r1.readBit() == false) goto L22;
        r1.skipBits(2);
        r1.skipBits(1);
        if (r1.readBit() == false) goto L22;
        r1.skipBits(15);
        r1.skipBit();
        r1.skipBits(15);
        r1.skipBit();
        r1.skipBits(15);
        r1.skipBit();
        r1.skipBits(3);
        r1.skipBits(11);
        r1.skipBit();
        r1.skipBits(15);
        r1.skipBit();
    L22:
        if (r1.readBits(2) == 0) goto L24;
        Log.m48w(TAG, "Unhandled video object layer shape");
    L24:
        r1.skipBit();
        int r5 = r1.readBits(16);
        r1.skipBit();
        if (r1.readBit() == false) goto L32;
        if (r5 != 0) goto L28;
        Log.m48w(TAG, "Invalid vop_increment_time_resolution");
        goto L32
    L28:
        int r6 = r5 - 1;
        int r7 = 0;
    L29:
        if (r6 <= 0) goto L31;
        r7 = r7 + 1;
        r6 = r6 >> 1;
        goto L29
    L31:
        r1.skipBits(r7);
    L32:
        r1.skipBit();
        int r9 = r1.readBits(13);
        r1.skipBit();
        int r13 = r1.readBits(13);
        r1.skipBit();
        r1.skipBit();
        return new Format.Builder().setId(r12).setSampleMimeType(MimeTypes.VIDEO_MP4V).setWidth(r9).setHeight(r13).setPixelWidthHeightRatio(r4).setInitializationData(Collections.singletonList(r0)).build();
    L10:
        r4 = r8 / r3;
        goto L17
    L13:
        if (r2 >= PIXEL_WIDTH_HEIGHT_RATIO_BY_ASPECT_RATIO_INFO.length) goto L15;
        r4 = PIXEL_WIDTH_HEIGHT_RATIO_BY_ASPECT_RATIO_INFO[r2];
        goto L17
    L15:
        Log.m48w(TAG, "Invalid aspect ratio");
        r4 = 1.0f;
        goto L17
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void consume(ParsableByteArray r14) {
        Assertions.checkStateNotNull(this.sampleReader);
        Assertions.checkStateNotNull(this.output);
        int r0 = r14.getPosition();
        int r1 = r14.limit();
        byte[] r2 = r14.getData();
        this.totalBytesWritten += (long) r14.bytesLeft();
        this.output.sampleData(r14, r14.bytesLeft());
    L3:
        int r3 = NalUnitUtil.findNalUnit(r2, r0, r1, this.prefixFlags);
        if (r3 == r1) goto L6;
        int r4 = r14.getData()[r3 + 3] & 255;
        int r5 = r3 - r0;
        if (this.hasOutputFormat == true) goto L22;
        if (r5 <= 0) goto L16;
        this.csdBuffer.onData(r2, r0, r3);
    L16:
        if (r5 >= 0) goto L18;
        int r6 = -r5;
    L20:
        if (this.csdBuffer.onStartCode(r4, r6) == false) goto L22;
        this.output.format(parseCsdBuffer(this.csdBuffer, this.csdBuffer.volStartPosition, (String) Assertions.checkNotNull(this.formatId)));
        this.hasOutputFormat = true;
        goto L22
    L18:
        r6 = 0;
    L22:
        this.sampleReader.onData(r2, r0, r3);
        if (this.userData == null) goto L36;
        int r7 = 0;
        if (r5 <= 0) goto L27;
        this.userData.appendToNalUnit(r2, r0, r3);
    L29:
        if (this.userData.endNalUnit(r7) == false) goto L32;
        ((ParsableByteArray) Util.castNonNull(this.userDataParsable)).reset(this.userData.nalData, NalUnitUtil.unescapeStream(this.userData.nalData, this.userData.nalLength));
        ((UserDataReader) Util.castNonNull(this.userDataReader)).consume(this.pesTimeUs, this.userDataParsable);
    L32:
        if (r4 != START_CODE_VALUE_USER_DATA) goto L36;
        if (r14.getData()[r3 + 2] != 1) goto L36;
        this.userData.startNalUnit(r4);
        goto L36
    L27:
        r7 = -r5;
    L36:
        int r8 = r1 - r3;
        this.sampleReader.onDataEnd(this.totalBytesWritten - ((long) r8), r8, this.hasOutputFormat);
        this.sampleReader.onStartCode(r4, this.pesTimeUs);
        r0 = r3 + 3;
        goto L3
    L6:
        if (this.hasOutputFormat == true) goto L8;
        this.csdBuffer.onData(r2, r0, r1);
    L8:
        this.sampleReader.onData(r2, r0, r1);
        if (this.userData == null) goto L42;
        this.userData.appendToNalUnit(r2, r0, r1);
        return;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void createTracks(ExtractorOutput r3, TsPayloadReader.TrackIdGenerator r4) {
        r4.generateNewId();
        this.formatId = r4.getFormatId();
        this.output = r3.track(r4.getTrackId(), 2);
        this.sampleReader = new SampleReader(this.output);
        if (this.userDataReader == null) goto L6;
        this.userDataReader.createTracks(r3, r4);
        return;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void packetFinished(boolean r6) {
        Assertions.checkStateNotNull(this.sampleReader);
        if (r6 == false) goto L6;
        this.sampleReader.onDataEnd(this.totalBytesWritten, 0, this.hasOutputFormat);
        this.sampleReader.reset();
        return;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void packetStarted(long r1, int r3) {
        this.pesTimeUs = r1;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void seek() {
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.csdBuffer.reset();
        if (this.sampleReader == null) goto L6;
        this.sampleReader.reset();
    L6:
        if (this.userData == null) goto L8;
        this.userData.reset();
    L8:
        this.totalBytesWritten = 0;
        this.pesTimeUs = C0180C.TIME_UNSET;
    }
}
