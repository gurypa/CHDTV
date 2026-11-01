package androidx.media3.extractor.p001ts;

import android.util.Pair;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.container.NalUnitUtil;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import com.google.common.base.Ascii;
import java.util.Arrays;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
public final class H262Reader implements ElementaryStreamReader {
    private static final double[] FRAME_RATE_VALUES = null;
    private static final int START_EXTENSION = 181;
    private static final int START_GROUP = 184;
    private static final int START_PICTURE = 0;
    private static final int START_SEQUENCE_HEADER = 179;
    private static final int START_USER_DATA = 178;
    private final CsdBuffer csdBuffer;
    private String formatId;
    private long frameDurationUs;
    private boolean hasOutputFormat;
    private TrackOutput output;
    private long pesTimeUs;
    private final boolean[] prefixFlags;
    private boolean sampleHasPicture;
    private boolean sampleIsKeyframe;
    private long samplePosition;
    private long sampleTimeUs;
    private boolean startedFirstSample;
    private long totalBytesWritten;
    private final NalUnitTargetBuffer userData;
    private final ParsableByteArray userDataParsable;
    private final UserDataReader userDataReader;

    private static final class CsdBuffer {
        private static final byte[] START_CODE = null;
        public byte[] data;
        private boolean isFilling;
        public int length;
        public int sequenceExtensionPosition;

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

        public boolean onStartCode(int r4, int r5) {
            if (this.isFilling == false) goto L12;
            this.length -= r5;
            if (this.sequenceExtensionPosition == 0) goto L7;
        L9:
            this.isFilling = false;
            return true;
        L7:
            if (r4 != H262Reader.START_EXTENSION) goto L9;
            this.sequenceExtensionPosition = this.length;
        L14:
            onData(START_CODE, 0, START_CODE.length);
            return false;
        L12:
            if (r4 != H262Reader.START_SEQUENCE_HEADER) goto L14;
            this.isFilling = true;
            goto L14
        }

        public void reset() {
            this.isFilling = false;
            this.length = 0;
            this.sequenceExtensionPosition = 0;
        }
    }

    static {
        FRAME_RATE_VALUES = new double[]{23.976023976023978d, 24.0d, 25.0d, 29.97002997002997d, 30.0d, 50.0d, 59.94005994005994d, 60.0d};
    }

    public H262Reader() {
        this(null);
    }

    H262Reader(UserDataReader r4) {
        this.userDataReader = r4;
        this.prefixFlags = new boolean[4];
        this.csdBuffer = new CsdBuffer(128);
        if (r4 == null) goto L5;
        this.userData = new NalUnitTargetBuffer(START_USER_DATA, 128);
        this.userDataParsable = new ParsableByteArray();
    L6:
        this.pesTimeUs = C0180C.TIME_UNSET;
        this.sampleTimeUs = C0180C.TIME_UNSET;
        return;
    L5:
        this.userData = null;
        this.userDataParsable = null;
        goto L6
    }

    private static Pair<Format, Long> parseCsdBuffer(CsdBuffer r22, String r23) {
        byte[] r1 = Arrays.copyOf(r22.data, r22.length);
        int r3 = r1[4] & 255;
        int r5 = r1[5] & 255;
        int r7 = (r3 << 4) | (r5 >> 4);
        int r8 = ((r5 & 15) << 8) | (r1[6] & 255);
        float r9 = 1.0f;
        switch(((r1[7] & 240) >> 4)) {
            case 2: goto L7;
            case 3: goto L6;
            case 4: goto L5;
            default: goto L8;
        };
    L5:
        r9 = (r8 * 121) / (r7 * 100);
        goto L8
    L6:
        r9 = (r8 * 16) / (r7 * 9);
        goto L8
    L7:
        r9 = (r8 * 4) / (r7 * 3);
    L8:
        Format r11 = new Format.Builder().setId(r23).setSampleMimeType(MimeTypes.VIDEO_MPEG2).setWidth(r7).setHeight(r8).setPixelWidthHeightRatio(r9).setInitializationData(Collections.singletonList(r1)).build();
        long r13 = 0;
        int r10 = (r1[7] & Ascii.f88SI) - 1;
        if (r10 < 0) goto L19;
        if (r10 >= FRAME_RATE_VALUES.length) goto L19;
        double r16 = FRAME_RATE_VALUES[r10];
        int r15 = r22.sequenceExtensionPosition;
        int r4 = (r1[r15 + 9] & 96) >> 5;
        int r0 = r1[r15 + 9] & Ascii.f91US;
        if (r4 == r0) goto L16;
        r16 = r16 * ((((double) r4) + 1.0d) / ((double) (r0 + 1)));
    L16:
        r13 = (long) (1000000.0d / r16);
    L19:
        return Pair.create(r11, Long.valueOf(r13));
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
    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void consume(ParsableByteArray r22) {
        Assertions.checkStateNotNull(this.output);
        int r1 = r22.getPosition();
        int r2 = r22.limit();
        byte[] r3 = r22.getData();
        this.totalBytesWritten += (long) r22.bytesLeft();
        this.output.sampleData(r22, r22.bytesLeft());
    L3:
        int r4 = NalUnitUtil.findNalUnit(r3, r1, r2, this.prefixFlags);
        if (r4 == r2) goto L6;
        int r5 = r22.getData()[r4 + 3] & 255;
        int r7 = r4 - r1;
        if (this.hasOutputFormat == true) goto L23;
        if (r7 <= 0) goto L16;
        this.csdBuffer.onData(r3, r1, r4);
    L16:
        if (r7 >= 0) goto L18;
        int r8 = -r7;
    L20:
        if (this.csdBuffer.onStartCode(r5, r8) == false) goto L23;
        Pair<Format, Long> r11 = parseCsdBuffer(this.csdBuffer, (String) Assertions.checkNotNull(this.formatId));
        this.output.format((Format) r11.first);
        this.frameDurationUs = ((Long) r11.second).longValue();
        this.hasOutputFormat = true;
        goto L23
    L18:
        r8 = 0;
    L23:
        if (this.userData == null) goto L36;
        int r9 = 0;
        if (r7 <= 0) goto L27;
        this.userData.appendToNalUnit(r3, r1, r4);
    L29:
        if (this.userData.endNalUnit(r9) == false) goto L32;
        ((ParsableByteArray) Util.castNonNull(this.userDataParsable)).reset(this.userData.nalData, NalUnitUtil.unescapeStream(this.userData.nalData, this.userData.nalLength));
        ((UserDataReader) Util.castNonNull(this.userDataReader)).consume(this.sampleTimeUs, this.userDataParsable);
    L32:
        if (r5 != START_USER_DATA) goto L36;
        if (r22.getData()[r4 + 2] != 1) goto L36;
        this.userData.startNalUnit(r5);
        goto L36
    L27:
        r9 = -r7;
    L36:
        if (r5 != 0) goto L38;
    L44:
        int r16 = r2 - r4;
        if (this.sampleHasPicture == true) goto L47;
    L51:
        long r10 = -9223372036854775807L;
    L53:
        if (this.startedFirstSample == true) goto L55;
    L58:
        this.samplePosition = this.totalBytesWritten - ((long) r16);
        if (this.pesTimeUs == r10) goto L62;
        long r12 = this.pesTimeUs;
    L65:
        this.sampleTimeUs = r12;
        boolean r13 = false;
        this.sampleIsKeyframe = false;
        this.pesTimeUs = r10;
        boolean r14 = true;
        this.startedFirstSample = true;
    L66:
        if (r5 != 0) goto L68;
        boolean r15 = r14;
    L69:
        this.sampleHasPicture = r15;
    L70:
        r1 = r4 + 3;
        goto L3
    L68:
        r15 = r13;
        goto L69
    L62:
        if (this.sampleTimeUs == r10) goto L64;
        r12 = this.sampleTimeUs + this.frameDurationUs;
        goto L65
    L64:
        r12 = r10;
        goto L65
    L55:
        if (this.sampleHasPicture == true) goto L58;
        r14 = true;
        r13 = false;
        goto L66
    L47:
        if (this.hasOutputFormat == false) goto L51;
        if (this.sampleTimeUs == C0180C.TIME_UNSET) goto L51;
        boolean r17 = this.sampleIsKeyframe;
        r10 = -9223372036854775807L;
        this.output.sampleMetadata(this.sampleTimeUs, r17 ? 1 : 0, ((int) (this.totalBytesWritten - this.samplePosition)) - r16, r16, null);
        goto L53
    L38:
        if (r5 == START_SEQUENCE_HEADER) goto L44;
        if (r5 != START_GROUP) goto L70;
        this.sampleIsKeyframe = true;
        goto L70
    L6:
        if (this.hasOutputFormat == true) goto L9;
        this.csdBuffer.onData(r3, r1, r2);
    L9:
        if (this.userData == null) goto L75;
        this.userData.appendToNalUnit(r3, r1, r2);
        return;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void createTracks(ExtractorOutput r3, TsPayloadReader.TrackIdGenerator r4) {
        r4.generateNewId();
        this.formatId = r4.getFormatId();
        this.output = r3.track(r4.getTrackId(), 2);
        if (this.userDataReader == null) goto L6;
        this.userDataReader.createTracks(r3, r4);
        return;
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
    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void packetFinished(boolean r9) {
        Assertions.checkStateNotNull(this.output);
        if (r9 == false) goto L6;
        boolean r0 = this.sampleIsKeyframe;
        int r5 = (int) (this.totalBytesWritten - this.samplePosition);
        this.output.sampleMetadata(this.sampleTimeUs, r0 ? 1 : 0, r5, 0, null);
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
        if (this.userData == null) goto L5;
        this.userData.reset();
    L5:
        this.totalBytesWritten = 0;
        this.startedFirstSample = false;
        this.pesTimeUs = C0180C.TIME_UNSET;
        this.sampleTimeUs = C0180C.TIME_UNSET;
    }
}
