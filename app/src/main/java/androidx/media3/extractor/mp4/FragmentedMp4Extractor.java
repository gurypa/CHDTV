package androidx.media3.extractor.mp4;

import android.util.Pair;
import android.util.SparseArray;
import androidx.media3.common.C0180C;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.common.util.Util;
import androidx.media3.container.Mp4Box;
import androidx.media3.container.NalUnitUtil;
import androidx.media3.container.ReorderingSeiMessageQueue;
import androidx.media3.extractor.Ac4Util;
import androidx.media3.extractor.CeaUtil;
import androidx.media3.extractor.ChunkIndex;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.GaplessInfoHolder;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.SniffFailure;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.metadata.emsg.EventMessage;
import androidx.media3.extractor.metadata.emsg.EventMessageEncoder;
import androidx.media3.extractor.p001ts.PsExtractor;
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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class FragmentedMp4Extractor implements Extractor {
    private static final Format EMSG_FORMAT = null;
    private static final int EXTRA_TRACKS_BASE_ID = 100;

    @Deprecated
    public static final ExtractorsFactory FACTORY = null;
    public static final int FLAG_EMIT_RAW_SUBTITLE_DATA = 32;
    public static final int FLAG_ENABLE_EMSG_TRACK = 4;
    public static final int FLAG_READ_WITHIN_GOP_SAMPLE_DEPENDENCIES = 64;
    public static final int FLAG_WORKAROUND_EVERY_VIDEO_FRAME_IS_SYNC_FRAME = 1;
    public static final int FLAG_WORKAROUND_IGNORE_EDIT_LISTS = 16;
    public static final int FLAG_WORKAROUND_IGNORE_TFDT_BOX = 2;
    private static final byte[] PIFF_SAMPLE_ENCRYPTION_BOX_EXTENDED_TYPE = null;
    private static final int SAMPLE_GROUP_TYPE_seig = 1936025959;
    private static final int STATE_READING_ATOM_HEADER = 0;
    private static final int STATE_READING_ATOM_PAYLOAD = 1;
    private static final int STATE_READING_ENCRYPTION_DATA = 2;
    private static final int STATE_READING_SAMPLE_CONTINUE = 4;
    private static final int STATE_READING_SAMPLE_START = 3;
    private static final String TAG = "FragmentedMp4Extractor";
    private final TrackOutput additionalEmsgTrackOutput;
    private ParsableByteArray atomData;
    private final ParsableByteArray atomHeader;
    private int atomHeaderBytesRead;
    private long atomSize;
    private int atomType;
    private TrackOutput[] ceaTrackOutputs;
    private final List<Format> closedCaptionFormats;
    private final ArrayDeque<Mp4Box.ContainerBox> containerAtoms;
    private TrackBundle currentTrackBundle;
    private long durationUs;
    private TrackOutput[] emsgTrackOutputs;
    private long endOfMdatPosition;
    private final EventMessageEncoder eventMessageEncoder;
    private ExtractorOutput extractorOutput;
    private final int flags;
    private boolean haveOutputSeekMap;
    private boolean isSampleDependedOn;
    private ImmutableList<SniffFailure> lastSniffFailures;
    private final ParsableByteArray nalBuffer;
    private final ParsableByteArray nalPrefix;
    private final ParsableByteArray nalStartCode;
    private int parserState;
    private int pendingMetadataSampleBytes;
    private final ArrayDeque<MetadataSampleInfo> pendingMetadataSampleInfos;
    private long pendingSeekTimeUs;
    private boolean processSeiNalUnitPayload;
    private final ReorderingSeiMessageQueue reorderingSeiMessageQueue;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;
    private int sampleSize;
    private final ParsableByteArray scratch;
    private final byte[] scratchBytes;
    private long segmentIndexEarliestPresentationTimeUs;
    private final Track sideloadedTrack;
    private final SubtitleParser.Factory subtitleParserFactory;
    private final TimestampAdjuster timestampAdjuster;
    private final SparseArray<TrackBundle> trackBundles;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    private static final class MetadataSampleInfo {
        public final boolean sampleTimeIsRelative;
        public final long sampleTimeUs;
        public final int size;

        public MetadataSampleInfo(long r1, boolean r3, int r4) {
            this.sampleTimeUs = r1;
            this.sampleTimeIsRelative = r3;
            this.size = r4;
        }
    }

    private static final class TrackBundle {
        private static final int SINGLE_SUBSAMPLE_ENCRYPTION_DATA_LENGTH = 8;
        public int currentSampleInTrackRun;
        public int currentSampleIndex;
        public int currentTrackRunIndex;
        private boolean currentlyInFragment;
        private final ParsableByteArray defaultInitializationVector;
        public DefaultSampleValues defaultSampleValues;
        private final ParsableByteArray encryptionSignalByte;
        public int firstSampleToOutputIndex;
        public final TrackFragment fragment;
        public TrackSampleTable moovSampleTable;
        public final TrackOutput output;
        public final ParsableByteArray scratch;

        public TrackBundle(TrackOutput r3, TrackSampleTable r4, DefaultSampleValues r5) {
            this.output = r3;
            this.moovSampleTable = r4;
            this.defaultSampleValues = r5;
            this.fragment = new TrackFragment();
            this.scratch = new ParsableByteArray();
            this.encryptionSignalByte = new ParsableByteArray(1);
            this.defaultInitializationVector = new ParsableByteArray();
            reset(r4, r5);
        }

        static /* synthetic */ boolean access$000(TrackBundle r1) {
            return r1.currentlyInFragment;
        }

        static /* synthetic */ boolean access$002(TrackBundle r0, boolean r1) {
            r0.currentlyInFragment = r1;
            return r1;
        }

        public int getCurrentSampleFlags() {
            if (this.currentlyInFragment == true) goto L6;
            int r0 = this.moovSampleTable.flags[this.currentSampleIndex];
        L10:
            if (getEncryptionBoxIfEncrypted() != null) goto L12;
            return r0;
        L12:
            return r0 | 1073741824;
        L6:
            if (this.fragment.sampleIsSyncFrameTable[this.currentSampleIndex] == false) goto L8;
            r0 = 1;
            goto L10
        L8:
            r0 = 0;
            goto L10
        }

        public long getCurrentSampleOffset() {
            if (this.currentlyInFragment == true) goto L6;
            return this.moovSampleTable.offsets[this.currentSampleIndex];
        L6:
            return this.fragment.trunDataPosition[this.currentTrackRunIndex];
        }

        public long getCurrentSamplePresentationTimeUs() {
            if (this.currentlyInFragment == true) goto L6;
            return this.moovSampleTable.timestampsUs[this.currentSampleIndex];
        L6:
            return this.fragment.getSamplePresentationTimeUs(this.currentSampleIndex);
        }

        public int getCurrentSampleSize() {
            if (this.currentlyInFragment == true) goto L6;
            return this.moovSampleTable.sizes[this.currentSampleIndex];
        L6:
            return this.fragment.sampleSizeTable[this.currentSampleIndex];
        }

        public TrackEncryptionBox getEncryptionBoxIfEncrypted() {
            if (this.currentlyInFragment == true) goto L5;
            return null;
        L5:
            int r0 = ((DefaultSampleValues) Util.castNonNull(this.fragment.header)).sampleDescriptionIndex;
            if (this.fragment.trackEncryptionBox == null) goto L8;
            TrackEncryptionBox r2 = this.fragment.trackEncryptionBox;
        L10:
            if (r2 != null) goto L12;
            return null;
        L12:
            if (r2.isEncrypted == true) goto L14;
            return null;
        L14:
            return r2;
        L8:
            r2 = this.moovSampleTable.track.getSampleDescriptionEncryptionBox(r0);
            goto L10
        }

        public boolean next() {
            this.currentSampleIndex++;
            if (this.currentlyInFragment == true) goto L5;
            return false;
        L5:
            this.currentSampleInTrackRun++;
            if (this.currentSampleInTrackRun != this.fragment.trunLength[this.currentTrackRunIndex]) goto L9;
            this.currentTrackRunIndex++;
            this.currentSampleInTrackRun = 0;
            return false;
        L9:
            return true;
        }

        public int outputSampleEncryptionData(int r19, int r20) {
            TrackEncryptionBox r3 = getEncryptionBoxIfEncrypted();
            if (r3 != null) goto L6;
            return 0;
        L6:
            if (r3.perSampleIvSize == 0) goto L8;
            ParsableByteArray r5 = this.fragment.sampleEncryptionData;
            int r6 = r3.perSampleIvSize;
        L9:
            boolean r7 = this.fragment.sampleHasSubsampleEncryptionTable(this.currentSampleIndex);
            if (r7 == true) goto L14;
            if (r20 != 0) goto L14;
            boolean r9 = false;
        L15:
            byte[] r10 = this.encryptionSignalByte.getData();
            if (r9 == false) goto L18;
            int r11 = 128;
        L19:
            r10[0] = (byte) (r11 | r6);
            this.encryptionSignalByte.setPosition(0);
            this.output.sampleData(this.encryptionSignalByte, 1, 1);
            this.output.sampleData(r5, r6, 1);
            if (r9 == true) goto L24;
            return r6 + 1;
        L24:
            if (r7 == true) goto L27;
            this.scratch.reset(8);
            byte[] r13 = this.scratch.getData();
            r13[0] = 0;
            r13[1] = 1;
            r13[2] = (byte) ((r20 >> 8) & 255);
            r13[3] = (byte) (r20 & 255);
            r13[4] = (byte) ((r19 >> 24) & 255);
            r13[5] = (byte) ((r19 >> 16) & 255);
            r13[6] = (byte) ((r19 >> 8) & 255);
            r13[7] = (byte) (r19 & 255);
            this.output.sampleData(this.scratch, 8, 1);
            return (r6 + 1) + 8;
        L27:
            ParsableByteArray r14 = this.fragment.sampleEncryptionData;
            int r15 = r14.readUnsignedShort();
            r14.skipBytes(-2);
            int r16 = (r15 * 6) + 2;
            if (r20 == 0) goto L30;
            this.scratch.reset(r16);
            byte[] r12 = this.scratch.getData();
            r14.readBytes(r12, 0, r16);
            int r17 = (((r12[2] & 255) << 8) | (r12[3] & 255)) + r20;
            r12[2] = (byte) ((r17 >> 8) & 255);
            r12[3] = (byte) (r17 & 255);
            r14 = this.scratch;
        L30:
            this.output.sampleData(r14, r16, 1);
            return (r6 + 1) + r16;
        L18:
            r11 = 0;
        L14:
            r9 = true;
            goto L15
        L8:
            byte[] r8 = (byte[]) Util.castNonNull(r3.defaultInitializationVector);
            this.defaultInitializationVector.reset(r8, r8.length);
            ParsableByteArray r18 = this.defaultInitializationVector;
            int r21 = r8.length;
            r5 = r18;
            r6 = r21;
            goto L9
        }

        public void reset(TrackSampleTable r3, DefaultSampleValues r4) {
            this.moovSampleTable = r3;
            this.defaultSampleValues = r4;
            this.output.format(r3.track.format);
            resetFragmentInfo();
        }

        public void resetFragmentInfo() {
            this.fragment.reset();
            this.currentSampleIndex = 0;
            this.currentTrackRunIndex = 0;
            this.currentSampleInTrackRun = 0;
            this.firstSampleToOutputIndex = 0;
            this.currentlyInFragment = false;
        }

        public void seek(long r4) {
            int r0 = this.currentSampleIndex;
        L4:
            if (r0 >= this.fragment.sampleCount) goto L11;
            if (this.fragment.getSamplePresentationTimeUs(r0) > r4) goto L16;
            if (this.fragment.sampleIsSyncFrameTable[r0] == false) goto L10;
            this.firstSampleToOutputIndex = r0;
        L10:
            r0 = r0 + 1;
            goto L4
        L16:
            return;
        }

        public void skipSampleEncryptionData() {
            TrackEncryptionBox r0 = getEncryptionBoxIfEncrypted();
            if (r0 != null) goto L5;
            return;
        L5:
            ParsableByteArray r1 = this.fragment.sampleEncryptionData;
            if (r0.perSampleIvSize == 0) goto L9;
            r1.skipBytes(r0.perSampleIvSize);
        L9:
            if (this.fragment.sampleHasSubsampleEncryptionTable(this.currentSampleIndex) == false) goto L12;
            r1.skipBytes(r1.readUnsignedShort() * 6);
            return;
        }

        public void updateDrmInitData(DrmInitData r6) {
            TrackEncryptionBox r0 = this.moovSampleTable.track.getSampleDescriptionEncryptionBox(((DefaultSampleValues) Util.castNonNull(this.fragment.header)).sampleDescriptionIndex);
            if (r0 == null) goto L5;
            String r1 = r0.schemeType;
        L6:
            DrmInitData r2 = r6.copyWithSchemeType(r1);
            Format r3 = this.moovSampleTable.track.format.buildUpon().setDrmInitData(r2).build();
            this.output.format(r3);
            return;
        L5:
            r1 = null;
            goto L6
        }
    }

    static {
        FACTORY = new FragmentedMp4Extractor$$ExternalSyntheticLambda1();
        PIFF_SAMPLE_ENCRYPTION_BOX_EXTENDED_TYPE = new byte[]{-94, 57, 79, 82, 90, -101, 79, Ascii.DC4, -94, 68, 108, 66, 124, 100, -115, -12};
        EMSG_FORMAT = new Format.Builder().setSampleMimeType(MimeTypes.APPLICATION_EMSG).build();
    }

    @Deprecated
    public FragmentedMp4Extractor() {
        this(SubtitleParser.Factory.UNSUPPORTED, 32, null, null, ImmutableList.m212of(), null);
    }

    @Deprecated
    public FragmentedMp4Extractor(int r8) {
        this(SubtitleParser.Factory.UNSUPPORTED, r8 | 32, null, null, ImmutableList.m212of(), null);
    }

    @Deprecated
    public FragmentedMp4Extractor(int r8, TimestampAdjuster r9) {
        this(SubtitleParser.Factory.UNSUPPORTED, r8 | 32, r9, null, ImmutableList.m212of(), null);
    }

    @Deprecated
    public FragmentedMp4Extractor(int r8, TimestampAdjuster r9, Track r10) {
        this(SubtitleParser.Factory.UNSUPPORTED, r8 | 32, r9, r10, ImmutableList.m212of(), null);
    }

    @Deprecated
    public FragmentedMp4Extractor(int r8, TimestampAdjuster r9, Track r10, List<Format> r11) {
        this(SubtitleParser.Factory.UNSUPPORTED, r8 | 32, r9, r10, r11, null);
    }

    @Deprecated
    public FragmentedMp4Extractor(int r8, TimestampAdjuster r9, Track r10, List<Format> r11, TrackOutput r12) {
        this(SubtitleParser.Factory.UNSUPPORTED, r8 | 32, r9, r10, r11, r12);
    }

    public FragmentedMp4Extractor(SubtitleParser.Factory r8) {
        this(r8, 0, null, null, ImmutableList.m212of(), null);
    }

    public FragmentedMp4Extractor(SubtitleParser.Factory r8, int r9) {
        this(r8, r9, null, null, ImmutableList.m212of(), null);
    }

    public FragmentedMp4Extractor(SubtitleParser.Factory r4, int r5, TimestampAdjuster r6, Track r7, List<Format> r8, TrackOutput r9) {
        this.subtitleParserFactory = r4;
        this.flags = r5;
        this.timestampAdjuster = r6;
        this.sideloadedTrack = r7;
        this.closedCaptionFormats = Collections.unmodifiableList(r8);
        this.additionalEmsgTrackOutput = r9;
        this.eventMessageEncoder = new EventMessageEncoder();
        this.atomHeader = new ParsableByteArray(16);
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalPrefix = new ParsableByteArray(5);
        this.nalBuffer = new ParsableByteArray();
        this.scratchBytes = new byte[16];
        this.scratch = new ParsableByteArray(this.scratchBytes);
        this.containerAtoms = new ArrayDeque();
        this.pendingMetadataSampleInfos = new ArrayDeque();
        this.trackBundles = new SparseArray();
        this.lastSniffFailures = ImmutableList.m212of();
        this.durationUs = C0180C.TIME_UNSET;
        this.pendingSeekTimeUs = C0180C.TIME_UNSET;
        this.segmentIndexEarliestPresentationTimeUs = C0180C.TIME_UNSET;
        this.extractorOutput = ExtractorOutput.PLACEHOLDER;
        this.emsgTrackOutputs = new TrackOutput[0];
        this.ceaTrackOutputs = new TrackOutput[0];
        this.reorderingSeiMessageQueue = new ReorderingSeiMessageQueue(new FragmentedMp4Extractor$$ExternalSyntheticLambda3(this));
    }

    private static int checkNonNegative(int r2) throws ParserException {
        if (r2 < 0) goto L5;
        return r2;
    L5:
        throw ParserException.createForMalformedContainer("Unexpected negative value: " + r2, null);
    }

    private void enterReadingAtomHeaderState() {
        this.parserState = 0;
        this.atomHeaderBytesRead = 0;
    }

    private DefaultSampleValues getDefaultSampleValues(SparseArray<DefaultSampleValues> r3, int r4) {
        if (r3.size() != 1) goto L7;
        return r3.valueAt(0);
    L7:
        return (DefaultSampleValues) Assertions.checkNotNull(r3.get(r4));
    }

    private static DrmInitData getDrmInitDataFromAtoms(List<Mp4Box.LeafBox> r8) {
        ArrayList r0 = null;
        int r1 = r8.size();
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L13;
        Mp4Box.LeafBox r3 = r8.get(r2);
        if (r3.type != 1886614376) goto L12;
        if (r0 != null) goto L8;
        r0 = new ArrayList();
    L8:
        byte[] r4 = r3.data.getData();
        UUID r5 = PsshAtomUtil.parseUuid(r4);
        if (r5 != null) goto L11;
        Log.m48w(TAG, "Skipped pssh atom (failed to extract uuid)");
        goto L12
    L11:
        r0.add(new DrmInitData.SchemeData(r5, MimeTypes.VIDEO_MP4, r4));
    L12:
        r2 = r2 + 1;
        goto L3
    L13:
        if (r0 != null) goto L16;
        return null;
    L16:
        return new DrmInitData(r0);
    }

    private static TrackBundle getNextTrackBundle(SparseArray<TrackBundle> r9) {
        TrackBundle r0 = null;
        long r1 = Long.MAX_VALUE;
        int r3 = r9.size();
        int r4 = 0;
    L3:
        if (r4 >= r3) goto L17;
        TrackBundle r5 = r9.valueAt(r4);
        if (TrackBundle.access$000(r5) == true) goto L9;
        if (r5.currentSampleIndex != r5.moovSampleTable.sampleCount) goto L9;
    L16:
        r4 = r4 + 1;
    L9:
        if (TrackBundle.access$000(r5) == true) goto L11;
    L13:
        long r6 = r5.getCurrentSampleOffset();
        if (r6 >= r1) goto L16;
        r0 = r5;
        r1 = r6;
        goto L16
    L11:
        if (r5.currentTrackRunIndex != r5.fragment.trunCount) goto L13;
    L17:
        return r0;
    }

    private void initExtraTracks() {
        int r0 = 100;
        this.emsgTrackOutputs = new TrackOutput[2];
        int r1 = 0;
        if (this.additionalEmsgTrackOutput == null) goto L6;
        this.emsgTrackOutputs[0] = this.additionalEmsgTrackOutput;
        r1 = 0 + 1;
    L6:
        if ((this.flags & 4) == 0) goto L8;
        this.emsgTrackOutputs[r1] = this.extractorOutput.track(100, 5);
        r1 = r1 + 1;
        r0 = 100 + 1;
    L8:
        this.emsgTrackOutputs = (TrackOutput[]) Util.nullSafeArrayCopy(this.emsgTrackOutputs, r1);
        TrackOutput[] r2 = this.emsgTrackOutputs;
        int r3 = r2.length;
        int r4 = 0;
    L9:
        if (r4 >= r3) goto L11;
        r2[r4].format(EMSG_FORMAT);
        r4 = r4 + 1;
        goto L9
    L11:
        this.ceaTrackOutputs = new TrackOutput[this.closedCaptionFormats.size()];
        int r5 = 0;
    L13:
        if (r5 >= this.ceaTrackOutputs.length) goto L15;
        int r6 = r0 + 1;
        TrackOutput r7 = this.extractorOutput.track(r0, 3);
        r7.format(this.closedCaptionFormats.get(r5));
        this.ceaTrackOutputs[r5] = r7;
        r5 = r5 + 1;
        r0 = r6;
        goto L13
    }

    private static boolean isEdtsListDurationForEntireMediaTimeline(Track r11) {
        if (r11.editListDurations != null) goto L5;
    L16:
        return false;
    L5:
        if (r11.editListDurations.length != 1) goto L16;
        if (r11.editListMediaTimes == null) goto L16;
        if (r11.editListDurations[0] != 0) goto L13;
        return true;
    L13:
        if ((Util.scaleLargeTimestamp(r11.editListDurations[0], 1000000, r11.movieTimescale) + Util.scaleLargeTimestamp(r11.editListMediaTimes[0], 1000000, r11.timescale)) >= r11.durationUs) goto L15;
        return false;
    L15:
        return true;
    }

    static /* synthetic */ Extractor[] lambda$newFactory$0(SubtitleParser.Factory r3) {
        return new Extractor[]{new FragmentedMp4Extractor(r3)};
    }

    static /* synthetic */ Extractor[] lambda$static$1() {
        return new Extractor[]{new FragmentedMp4Extractor(SubtitleParser.Factory.UNSUPPORTED, 32)};
    }

    public static ExtractorsFactory newFactory(final SubtitleParser.Factory r1) {
        return new FragmentedMp4Extractor$$ExternalSyntheticLambda2(r1);
    }

    private void onContainerAtomRead(Mp4Box.ContainerBox r3) throws ParserException {
        if (r3.type != 1836019574) goto L6;
        onMoovContainerAtomRead(r3);
        return;
    L6:
        if (r3.type != 1836019558) goto L9;
        onMoofContainerAtomRead(r3);
        return;
    L9:
        if (this.containerAtoms.isEmpty() == true) goto L14;
        this.containerAtoms.peek().add(r3);
        return;
    }

    private void onEmsgLeafAtomRead(ParsableByteArray r33) {
        if (this.emsgTrackOutputs.length != 0) goto L5;
        return;
    L5:
        r33.setPosition(8);
        int r3 = BoxParser.parseFullBoxVersion(r33.readInt());
        long r4 = C0180C.TIME_UNSET;
        long r6 = C0180C.TIME_UNSET;
        switch(r3) {
            case 0: goto L10;
            case 1: goto L9;
            default: goto L7;
        };
    L7:
        Log.m48w(TAG, "Skipping unsupported emsg version: " + r3);
        return;
    L9:
        long r14 = r33.readUnsignedInt();
        r6 = Util.scaleLargeTimestamp(r33.readUnsignedLongToLong(), 1000000, r14);
        long r10 = Util.scaleLargeTimestamp(r33.readUnsignedInt(), 1000, r14);
        long r12 = r33.readUnsignedInt();
        String r11 = (String) Assertions.checkNotNull(r33.readNullTerminatedString());
        long r15 = r12;
        String r13 = (String) Assertions.checkNotNull(r33.readNullTerminatedString());
        long r16 = r10;
    L14:
        byte[] r17 = new byte[r33.bytesLeft()];
        r33.readBytes(r17, 0, r33.bytesLeft());
        ParsableByteArray r8 = new ParsableByteArray(this.eventMessageEncoder.encode(new EventMessage(r11, r13, r16, r15, r17)));
        int r9 = r8.bytesLeft();
        TrackOutput[] r1 = this.emsgTrackOutputs;
        int r2 = r1.length;
        int r5 = 0;
    L15:
        if (r5 >= r2) goto L18;
        int r23 = r5;
        TrackOutput r7 = r1[r23];
        r8.setPosition(0);
        r7.sampleData(r8, r9);
        r5 = r23 + 1;
        r2 = r2;
        goto L15
    L18:
        if (r6 != C0180C.TIME_UNSET) goto L21;
        this.pendingMetadataSampleInfos.addLast(new MetadataSampleInfo(r4, true, r9));
        this.pendingMetadataSampleBytes += r9;
    L40:
        return;
    L21:
        if (this.pendingMetadataSampleInfos.isEmpty() == true) goto L24;
        this.pendingMetadataSampleInfos.addLast(new MetadataSampleInfo(r6, false, r9));
        this.pendingMetadataSampleBytes += r9;
        goto L40
    L24:
        if (this.timestampAdjuster != null) goto L26;
    L29:
        int r18 = 0;
        if (this.timestampAdjuster == null) goto L32;
        long r24 = this.timestampAdjuster.adjustSampleTimestamp(r6);
    L33:
        TrackOutput[] r19 = this.emsgTrackOutputs;
        int r20 = r19.length;
    L34:
        if (r18 >= r20) goto L37;
        r19[r18].sampleMetadata(r24, 1, r9, 0, null);
        r18 = r18 + 1;
        goto L34
    L37:
        return;
    L32:
        r24 = r6;
        goto L33
    L26:
        if (this.timestampAdjuster.isInitialized() == true) goto L29;
        this.pendingMetadataSampleInfos.addLast(new MetadataSampleInfo(r6, false, r9));
        this.pendingMetadataSampleBytes += r9;
        goto L40
    L10:
        String r110 = (String) Assertions.checkNotNull(r33.readNullTerminatedString());
        String r111 = (String) Assertions.checkNotNull(r33.readNullTerminatedString());
        long r112 = r33.readUnsignedInt();
        r4 = Util.scaleLargeTimestamp(r33.readUnsignedInt(), 1000000, r112);
        if (this.segmentIndexEarliestPresentationTimeUs == C0180C.TIME_UNSET) goto L13;
        r6 = this.segmentIndexEarliestPresentationTimeUs + r4;
    L13:
        long r113 = Util.scaleLargeTimestamp(r33.readUnsignedInt(), 1000, r112);
        r11 = r110;
        r15 = r33.readUnsignedInt();
        r13 = r111;
        r16 = r113;
        goto L14
    }

    private void onLeafAtomRead(Mp4Box.LeafBox r4, long r5) throws ParserException {
        if (this.containerAtoms.isEmpty() == true) goto L6;
        this.containerAtoms.peek().add(r4);
        return;
    L6:
        if (r4.type != 1936286840) goto L9;
        Pair<Long, ChunkIndex> r0 = parseSidx(r4.data, r5);
        this.segmentIndexEarliestPresentationTimeUs = ((Long) r0.first).longValue();
        this.extractorOutput.seekMap((SeekMap) r0.second);
        this.haveOutputSeekMap = true;
        return;
    L9:
        if (r4.type != 1701671783) goto L15;
        onEmsgLeafAtomRead(r4.data);
        return;
    }

    private void onMoofContainerAtomRead(Mp4Box.ContainerBox r9) throws ParserException {
        SparseArray<TrackBundle> r0 = this.trackBundles;
        if (this.sideloadedTrack == null) goto L5;
        boolean r1 = true;
    L6:
        parseMoof(r9, r0, r1, this.flags, this.scratchBytes);
        DrmInitData r2 = getDrmInitDataFromAtoms(r9.leafChildren);
        if (r2 == null) goto L12;
        int r3 = this.trackBundles.size();
        int r4 = 0;
    L9:
        if (r4 >= r3) goto L12;
        this.trackBundles.valueAt(r4).updateDrmInitData(r2);
        r4 = r4 + 1;
    L12:
        if (this.pendingSeekTimeUs == C0180C.TIME_UNSET) goto L20;
        int r5 = this.trackBundles.size();
        int r6 = 0;
    L14:
        if (r6 >= r5) goto L16;
        this.trackBundles.valueAt(r6).seek(this.pendingSeekTimeUs);
        r6 = r6 + 1;
        goto L14
    L16:
        this.pendingSeekTimeUs = C0180C.TIME_UNSET;
        return;
    L20:
        return;
    L5:
        r1 = false;
        goto L6
    }

    private void onMoovContainerAtomRead(Mp4Box.ContainerBox r21) throws ParserException {
        boolean r9 = false;
        if (this.sideloadedTrack != null) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkState(r2, "Unexpected moov box.");
        DrmInitData r5 = getDrmInitDataFromAtoms(r21.leafChildren);
        Mp4Box.ContainerBox r11 = (Mp4Box.ContainerBox) Assertions.checkNotNull(r21.getContainerBoxOfType(Mp4Box.TYPE_mvex));
        SparseArray<DefaultSampleValues> r3 = new SparseArray();
        int r13 = r11.leafChildren.size();
        int r4 = 0;
        long r6 = -9223372036854775807L;
    L7:
        if (r4 >= r13) goto L16;
        Mp4Box.LeafBox r7 = r11.leafChildren.get(r4);
        if (r7.type != 1953654136) goto L12;
        Pair<Integer, DefaultSampleValues> r8 = parseTrex(r7.data);
        r3.put(((Integer) r8.first).intValue(), (DefaultSampleValues) r8.second);
    L15:
        r4 = r4 + 1;
        goto L7
    L12:
        if (r7.type != 1835362404) goto L15;
        r6 = parseMehd(r7.data);
        goto L15
    L16:
        GaplessInfoHolder r10 = new GaplessInfoHolder();
        if ((this.flags & 16) == 0) goto L19;
        boolean r12 = true;
    L20:
        List<TrackSampleTable> r14 = BoxParser.parseTraks(r21, r10, r6, r5, r12, false, new FragmentedMp4Extractor$$ExternalSyntheticLambda0(this));
        int r1 = r14.size();
        if (this.trackBundles.size() != 0) goto L27;
        int r15 = 0;
    L23:
        if (r15 >= r1) goto L25;
        TrackSampleTable r16 = r14.get(r15);
        Track r17 = r16.track;
        this.trackBundles.put(r17.f60id, new TrackBundle(this.extractorOutput.track(r15, r17.type), r16, getDefaultSampleValues(r3, r17.f60id)));
        this.durationUs = Math.max(this.durationUs, r17.durationUs);
        r15 = r15 + 1;
        r6 = r6;
        goto L23
    L25:
        this.extractorOutput.endTracks();
        return;
    L27:
        if (this.trackBundles.size() != r1) goto L29;
        r9 = true;
    L29:
        Assertions.checkState(r9);
        int r18 = 0;
    L30:
        if (r18 >= r1) goto L39;
        TrackSampleTable r19 = r14.get(r18);
        Track r20 = r19.track;
        this.trackBundles.get(r20.f60id).reset(r19, getDefaultSampleValues(r3, r20.f60id));
        r18 = r18 + 1;
        goto L30
    L39:
        return;
    L19:
        r12 = false;
        goto L20
    L5:
        r2 = false;
        goto L6
    }

    private void outputPendingMetadataSamples(long r12) {
    L3:
        if (this.pendingMetadataSampleInfos.isEmpty() == true) goto L15;
        MetadataSampleInfo r0 = this.pendingMetadataSampleInfos.removeFirst();
        this.pendingMetadataSampleBytes -= r0.size;
        long r1 = r0.sampleTimeUs;
        if (r0.sampleTimeIsRelative == false) goto L8;
        r1 = r1 + r12;
    L8:
        if (this.timestampAdjuster == null) goto L10;
        long r4 = this.timestampAdjuster.adjustSampleTimestamp(r1);
    L11:
        TrackOutput[] r2 = this.emsgTrackOutputs;
        int r3 = r2.length;
        int r10 = 0;
    L12:
        if (r10 >= r3) goto L3;
        r2[r10].sampleMetadata(r4, 1, r0.size, this.pendingMetadataSampleBytes, null);
        r10 = r10 + 1;
        goto L12
    L10:
        r4 = r1;
        goto L11
    }

    private static long parseMehd(ParsableByteArray r4) {
        r4.setPosition(8);
        if (BoxParser.parseFullBoxVersion(r4.readInt()) != 0) goto L6;
        return r4.readUnsignedInt();
    L6:
        return r4.readUnsignedLongToLong();
    }

    private static void parseMoof(Mp4Box.ContainerBox r5, SparseArray<TrackBundle> r6, boolean r7, int r8, byte[] r9) throws ParserException {
        int r0 = r5.containerChildren.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L8;
        Mp4Box.ContainerBox r2 = r5.containerChildren.get(r1);
        if (r2.type != 1953653094) goto L7;
        parseTraf(r2, r6, r7, r8, r9);
    L7:
        r1 = r1 + 1;
        goto L3
    }

    private static void parseSaio(ParsableByteArray r8, TrackFragment r9) throws ParserException {
        r8.setPosition(8);
        int r1 = r8.readInt();
        if ((BoxParser.parseFullBoxFlags(r1) & 1) != 1) goto L5;
        r8.skipBytes(8);
    L5:
        int r0 = r8.readUnsignedIntToInt();
        if (r0 != 1) goto L14;
        int r3 = BoxParser.parseFullBoxVersion(r1);
        long r4 = r9.auxiliaryDataPosition;
        if (r3 != 0) goto L10;
        long r6 = r8.readUnsignedInt();
    L11:
        r9.auxiliaryDataPosition = r4 + r6;
        return;
    L10:
        r6 = r8.readUnsignedLongToLong();
        goto L11
    L14:
        throw ParserException.createForMalformedContainer("Unexpected saio entry count: " + r0, null);
    }

    private static void parseSaiz(TrackEncryptionBox r12, ParsableByteArray r13, TrackFragment r14) throws ParserException {
        int r0 = r12.perSampleIvSize;
        r13.setPosition(8);
        boolean r5 = true;
        if ((BoxParser.parseFullBoxFlags(r13.readInt()) & 1) != 1) goto L5;
        r13.skipBytes(8);
    L5:
        int r1 = r13.readUnsignedByte();
        int r4 = r13.readUnsignedIntToInt();
        if (r4 > r14.sampleCount) goto L26;
        int r6 = 0;
        if (r1 != 0) goto L17;
        boolean[] r8 = r14.sampleHasSubsampleEncryptionTable;
        int r9 = 0;
    L10:
        if (r9 >= r4) goto L21;
        int r10 = r13.readUnsignedByte();
        r6 = r6 + r10;
        if (r10 <= r0) goto L14;
        boolean r11 = true;
    L15:
        r8[r9] = r11;
        r9 = r9 + 1;
        goto L10
    L14:
        r11 = false;
    L21:
        Arrays.fill(r14.sampleHasSubsampleEncryptionTable, r4, r14.sampleCount, false);
        if (r6 <= 0) goto L30;
        r14.initEncryptionData(r6);
        return;
    L30:
        return;
    L17:
        if (r1 > r0) goto L20;
        r5 = false;
    L20:
        r6 = 0 + (r1 * r4);
        Arrays.fill(r14.sampleHasSubsampleEncryptionTable, 0, r4, r5);
        goto L21
    L26:
        throw ParserException.createForMalformedContainer("Saiz sample count " + r4 + " is greater than fragment sample count" + r14.sampleCount, null);
    }

    private static void parseSampleGroups(Mp4Box.ContainerBox r18, String r19, TrackFragment r20) throws ParserException {
        ParsableByteArray r2 = null;
        ParsableByteArray r3 = null;
        int r4 = 0;
    L4:
        if (r4 >= r18.leafChildren.size()) goto L16;
        Mp4Box.LeafBox r5 = r18.leafChildren.get(r4);
        ParsableByteArray r6 = r5.data;
        if (r5.type != 1935828848) goto L11;
        r6.setPosition(12);
        if (r6.readInt() != SAMPLE_GROUP_TYPE_seig) goto L15;
        r2 = r6;
    L15:
        r4 = r4 + 1;
        goto L4
    L11:
        if (r5.type != 1936158820) goto L15;
        r6.setPosition(12);
        if (r6.readInt() != SAMPLE_GROUP_TYPE_seig) goto L15;
        r3 = r6;
        goto L15
    L16:
        if (r2 == null) goto L52;
        if (r3 == null) goto L59;
        r2.setPosition(8);
        int r7 = BoxParser.parseFullBoxVersion(r2.readInt());
        r2.skipBytes(4);
        if (r7 != 1) goto L23;
        r2.skipBytes(4);
    L23:
        if (r2.readInt() != 1) goto L51;
        r3.setPosition(8);
        int r8 = BoxParser.parseFullBoxVersion(r3.readInt());
        r3.skipBytes(4);
        if (r8 != 1) goto L32;
        if (r3.readUnsignedInt() != 0) goto L35;
        throw ParserException.createForUnsupportedContainerFeature("Variable length description in sgpd found (unsupported)");
    L35:
        if (r3.readUnsignedInt() != 1) goto L49;
        r3.skipBytes(1);
        int r9 = r3.readUnsignedByte();
        int r15 = (r9 & PsExtractor.VIDEO_STREAM_MASK) >> 4;
        int r16 = r9 & 15;
        if (r3.readUnsignedByte() != 1) goto L39;
        boolean r11 = true;
    L40:
        if (r11 == true) goto L42;
        return;
    L42:
        int r13 = r3.readUnsignedByte();
        byte[] r14 = new byte[16];
        r3.readBytes(r14, 0, r14.length);
        if (r13 != 0) goto L45;
        int r10 = r3.readUnsignedByte();
        byte[] r12 = new byte[r10];
        r3.readBytes(r12, 0, r10);
        byte[] r17 = r12;
    L46:
        r20.definesEncryptionData = true;
        r20.trackEncryptionBox = new TrackEncryptionBox(r11, r19, r13, r14, r15, r16, r17);
        return;
    L45:
        r17 = null;
        goto L46
    L39:
        r11 = false;
        goto L40
    L49:
        throw ParserException.createForUnsupportedContainerFeature("Entry count in sgpd != 1 (unsupported).");
    L32:
        if (r8 < 2) goto L35;
        r3.skipBytes(4);
        goto L35
    L51:
        throw ParserException.createForUnsupportedContainerFeature("Entry count in sbgp != 1 (unsupported).");
    L59:
        return;
    }

    private static void parseSenc(ParsableByteArray r7, int r8, TrackFragment r9) throws ParserException {
        r7.setPosition(r8 + 8);
        int r1 = BoxParser.parseFullBoxFlags(r7.readInt());
        if ((r1 & 1) != 0) goto L19;
        if ((r1 & 2) == 0) goto L7;
        boolean r2 = true;
    L8:
        int r4 = r7.readUnsignedIntToInt();
        if (r4 != 0) goto L13;
        Arrays.fill(r9.sampleHasSubsampleEncryptionTable, 0, r9.sampleCount, false);
        return;
    L13:
        if (r4 != r9.sampleCount) goto L17;
        Arrays.fill(r9.sampleHasSubsampleEncryptionTable, 0, r4, r2);
        r9.initEncryptionData(r7.bytesLeft());
        r9.fillEncryptionData(r7);
        return;
    L17:
        throw ParserException.createForMalformedContainer("Senc sample count " + r4 + " is different from fragment sample count" + r9.sampleCount, null);
    L7:
        r2 = false;
        goto L8
    L19:
        throw ParserException.createForUnsupportedContainerFeature("Overriding TrackEncryptionBox parameters is unsupported.");
    }

    private static void parseSenc(ParsableByteArray r1, TrackFragment r2) throws ParserException {
        parseSenc(r1, 0, r2);
    }

    private static Pair<Long, ChunkIndex> parseSidx(ParsableByteArray r31, long r32) throws ParserException {
        r31.setPosition(8);
        int r1 = r31.readInt();
        int r2 = BoxParser.parseFullBoxVersion(r1);
        r31.skipBytes(4);
        long r8 = r31.readUnsignedInt();
        if (r2 != 0) goto L5;
        long r6 = r31.readUnsignedInt();
        long r10 = r32 + r31.readUnsignedInt();
        long r4 = r6;
    L6:
        long r12 = Util.scaleLargeTimestamp(r4, 1000000, r8);
        long r14 = r4;
        r31.skipBytes(2);
        int r5 = r31.readUnsignedShort();
        int[] r7 = new int[r5];
        long[] r9 = new long[r5];
        long[] r3 = new long[r5];
        long r16 = r14;
        long r18 = r12;
        long[] r20 = new long[r5];
        int r11 = 0;
    L7:
        if (r11 >= r5) goto L14;
        int r21 = r31.readInt();
        if ((r21 & Integer.MIN_VALUE) != 0) goto L12;
        long r23 = r31.readUnsignedInt();
        r7[r11] = r21 & Integer.MAX_VALUE;
        r20[r11] = r10;
        r3[r11] = r18;
        r16 = r16 + r23;
        int r26 = r11;
        long[] r25 = r9;
        int r27 = r1;
        int r28 = r2;
        int[] r13 = r7;
        r18 = Util.scaleLargeTimestamp(r16, 1000000, r8);
        r25[r26] = r18 - r3[r26];
        r31.skipBytes(4);
        long r15 = r10 + ((long) r13[r26]);
        r5 = r5;
        r7 = r13;
        r20 = r20;
        r1 = r27;
        r2 = r28;
        r9 = r25;
        r11 = r26 + 1;
        r10 = r15;
        goto L7
    L12:
        throw ParserException.createForMalformedContainer("Unhandled indirect reference", null);
    L14:
        return Pair.create(Long.valueOf(r12), new ChunkIndex(r7, r20, r9, r3));
    L5:
        long r17 = r31.readUnsignedLongToLong();
        r10 = r32 + r31.readUnsignedLongToLong();
        r4 = r17;
        goto L6
    }

    private static long parseTfdt(ParsableByteArray r4) {
        r4.setPosition(8);
        if (BoxParser.parseFullBoxVersion(r4.readInt()) != 1) goto L6;
        return r4.readUnsignedLongToLong();
    L6:
        return r4.readUnsignedInt();
    }

    private static TrackBundle parseTfhd(ParsableByteArray r11, SparseArray<TrackBundle> r12, boolean r13) {
        r11.setPosition(8);
        int r1 = BoxParser.parseFullBoxFlags(r11.readInt());
        int r2 = r11.readInt();
        if (r13 == false) goto L5;
        TrackBundle r3 = r12.valueAt(0);
    L6:
        TrackBundle r4 = r3;
        if (r4 != null) goto L11;
        return null;
    L11:
        if ((r1 & 1) == 0) goto L13;
        long r5 = r11.readUnsignedLongToLong();
        r4.fragment.dataPosition = r5;
        r4.fragment.auxiliaryDataPosition = r5;
    L13:
        DefaultSampleValues r6 = r4.defaultSampleValues;
        if ((r1 & 2) == 0) goto L16;
        int r7 = r11.readInt() - 1;
    L18:
        if ((r1 & 8) == 0) goto L20;
        int r8 = r11.readInt();
    L22:
        if ((r1 & 16) == 0) goto L24;
        int r9 = r11.readInt();
    L26:
        if ((r1 & 32) == 0) goto L28;
        int r10 = r11.readInt();
    L29:
        r4.fragment.header = new DefaultSampleValues(r7, r8, r9, r10);
        return r4;
    L28:
        r10 = r6.flags;
        goto L29
    L24:
        r9 = r6.size;
        goto L26
    L20:
        r8 = r6.duration;
        goto L22
    L16:
        r7 = r6.sampleDescriptionIndex;
        goto L18
    L5:
        r3 = r12.get(r2);
        goto L6
    }

    private static void parseTraf(Mp4Box.ContainerBox r18, SparseArray<TrackBundle> r19, boolean r20, int r21, byte[] r22) throws ParserException {
        Mp4Box.ContainerBox r0 = r18;
        Mp4Box.LeafBox r2 = (Mp4Box.LeafBox) Assertions.checkNotNull(r0.getLeafBoxOfType(Mp4Box.TYPE_tfhd));
        TrackBundle r3 = parseTfhd(r2.data, r19, r20);
        if (r3 != null) goto L5;
        return;
    L5:
        TrackFragment r6 = r3.fragment;
        long r7 = r6.nextFragmentDecodeTime;
        boolean r9 = r6.nextFragmentDecodeTimeIncludesMoov;
        r3.resetFragmentInfo();
        TrackBundle.access$002(r3, true);
        Mp4Box.LeafBox r11 = r0.getLeafBoxOfType(Mp4Box.TYPE_tfdt);
        if (r11 != null) goto L8;
    L10:
        r6.nextFragmentDecodeTime = r7;
        r6.nextFragmentDecodeTimeIncludesMoov = r9;
    L11:
        parseTruns(r0, r3, r21);
        TrackEncryptionBox r10 = r3.moovSampleTable.track.getSampleDescriptionEncryptionBox(((DefaultSampleValues) Assertions.checkNotNull(r6.header)).sampleDescriptionIndex);
        Mp4Box.LeafBox r12 = r0.getLeafBoxOfType(Mp4Box.TYPE_saiz);
        if (r12 == null) goto L14;
        parseSaiz((TrackEncryptionBox) Assertions.checkNotNull(r10), r12.data, r6);
    L14:
        Mp4Box.LeafBox r13 = r0.getLeafBoxOfType(Mp4Box.TYPE_saio);
        if (r13 == null) goto L17;
        parseSaio(r13.data, r6);
    L17:
        Mp4Box.LeafBox r14 = r0.getLeafBoxOfType(Mp4Box.TYPE_senc);
        if (r14 == null) goto L20;
        parseSenc(r14.data, r6);
    L20:
        if (r10 == null) goto L22;
        String r15 = r10.schemeType;
    L23:
        parseSampleGroups(r0, r15, r6);
        int r16 = r0.leafChildren.size();
        int r1 = 0;
    L24:
        if (r1 >= r16) goto L30;
        Mp4Box.LeafBox r17 = r2;
        Mp4Box.LeafBox r4 = r0.leafChildren.get(r1);
        int r110 = r1;
        if (r4.type != 1970628964) goto L29;
        parseUuid(r4.data, r6, r22);
    L29:
        r1 = r110 + 1;
        r2 = r17;
        r0 = r18;
        goto L24
    L30:
        return;
    L22:
        r15 = null;
        goto L23
    L8:
        if ((r21 & 2) != 0) goto L10;
        r6.nextFragmentDecodeTime = parseTfdt(r11.data);
        r6.nextFragmentDecodeTimeIncludesMoov = true;
        goto L11
    }

    private static Pair<Integer, DefaultSampleValues> parseTrex(ParsableByteArray r7) {
        r7.setPosition(12);
        return Pair.create(Integer.valueOf(r7.readInt()), new DefaultSampleValues(r7.readInt() - 1, r7.readInt(), r7.readInt(), r7.readInt()));
    }

    private static int parseTrun(TrackBundle r37, int r38, int r39, ParsableByteArray r40, int r41) throws ParserException {
        r40.setPosition(8);
        int r3 = BoxParser.parseFullBoxFlags(r40.readInt());
        Track r4 = r37.moovSampleTable.track;
        TrackFragment r5 = r37.fragment;
        DefaultSampleValues r6 = (DefaultSampleValues) Util.castNonNull(r5.header);
        r5.trunLength[r38] = r40.readUnsignedIntToInt();
        r5.trunDataPosition[r38] = r5.dataPosition;
        if ((r3 & 1) == 0) goto L6;
        long[] r7 = r5.trunDataPosition;
        r7[r38] = r7[r38] + ((long) r40.readInt());
    L6:
        if ((r3 & 4) == 0) goto L8;
        boolean r8 = true;
    L9:
        int r10 = r6.flags;
        if (r8 == false) goto L13;
        r10 = r40.readInt();
    L13:
        if ((r3 & 256) == 0) goto L15;
        boolean r11 = true;
    L17:
        if ((r3 & 512) == 0) goto L19;
        boolean r12 = true;
    L21:
        if ((r3 & 1024) == 0) goto L23;
        boolean r13 = true;
    L25:
        if ((r3 & 2048) == 0) goto L27;
        boolean r14 = true;
    L28:
        long r15 = 0;
        if (isEdtsListDurationForEntireMediaTimeline(r4) == false) goto L31;
        boolean r17 = false;
        r15 = ((long[]) Util.castNonNull(r4.editListMediaTimes))[0];
    L32:
        int[] r9 = r5.sampleSizeTable;
        long[] r16 = r5.samplePresentationTimesUs;
        boolean[] r1 = r5.sampleIsSyncFrameTable;
        if (r4.type == 2) goto L35;
    L37:
        boolean r2 = r17;
    L38:
        int r18 = r41 + r5.trunLength[r38];
        boolean r27 = r8;
        long r19 = r4.timescale;
        long r20 = r5.nextFragmentDecodeTime;
        boolean r31 = r2;
        int r21 = r41;
    L39:
        if (r21 >= r18) goto L68;
        if (r11 == false) goto L42;
        int r22 = r40.readInt();
    L43:
        int r23 = checkNonNegative(r22);
        if (r12 == false) goto L46;
        int r24 = r40.readInt();
    L47:
        int r25 = checkNonNegative(r24);
        if (r13 == false) goto L50;
        int r26 = r40.readInt();
    L54:
        int r28 = r26;
        if (r14 == false) goto L57;
        int r29 = r40.readInt();
    L58:
        DefaultSampleValues r36 = r6;
        long r210 = r19;
        r16[r21] = Util.scaleLargeTimestamp((((long) r29) + r20) - r15, 1000000, r210);
        if (r5.nextFragmentDecodeTimeIncludesMoov == true) goto L61;
        r16[r21] = r16[r21] + r37.moovSampleTable.durationUs;
    L61:
        r9[r21] = r25;
        if (((r28 >> 16) & 1) != 0) goto L66;
        if (r31 == false) goto L65;
        if (r21 != 0) goto L66;
    L65:
        boolean r30 = true;
    L67:
        r1[r21] = r30;
        r20 = r20 + ((long) r23);
        r21 = r21 + 1;
        r19 = r210;
        r18 = r18;
        r6 = r36;
    L66:
        r30 = r17;
        goto L67
    L57:
        r29 = 0;
        goto L58
    L50:
        if (r21 != 0) goto L53;
        if (r27 == false) goto L53;
        r26 = r10;
    L53:
        r26 = r6.flags;
        goto L54
    L46:
        r24 = r6.size;
        goto L47
    L42:
        r22 = r6.duration;
        goto L43
    L68:
        int r33 = r18;
        r5.nextFragmentDecodeTime = r20;
        return r33;
    L35:
        if ((r39 & 1) == 0) goto L37;
        r2 = true;
        goto L38
    L31:
        r17 = false;
        goto L32
    L27:
        r14 = false;
        goto L28
    L23:
        r13 = false;
        goto L25
    L19:
        r12 = false;
        goto L21
    L15:
        r11 = false;
        goto L17
    L8:
        r8 = false;
        goto L9
    }

    private static void parseTruns(Mp4Box.ContainerBox r11, TrackBundle r12, int r13) throws ParserException {
        int r0 = 0;
        int r1 = 0;
        List<Mp4Box.LeafBox> r2 = r11.leafChildren;
        int r3 = r2.size();
        int r4 = 0;
    L4:
        if (r4 >= r3) goto L11;
        Mp4Box.LeafBox r6 = r2.get(r4);
        if (r6.type != 1953658222) goto L10;
        ParsableByteArray r5 = r6.data;
        r5.setPosition(12);
        int r7 = r5.readUnsignedIntToInt();
        if (r7 <= 0) goto L10;
        r1 = r1 + r7;
        r0 = r0 + 1;
    L10:
        r4 = r4 + 1;
        goto L4
    L11:
        r12.currentTrackRunIndex = 0;
        r12.currentSampleInTrackRun = 0;
        r12.currentSampleIndex = 0;
        r12.fragment.initTables(r0, r1);
        int r8 = 0;
        int r9 = 0;
        int r10 = 0;
    L12:
        if (r10 >= r3) goto L17;
        Mp4Box.LeafBox r14 = r2.get(r10);
        if (r14.type != 1953658222) goto L16;
        int r15 = r8 + 1;
        r9 = parseTrun(r12, r8, r13, r14.data, r9);
        r8 = r15;
    L16:
        r10 = r10 + 1;
        goto L12
    }

    private static void parseUuid(ParsableByteArray r2, TrackFragment r3, byte[] r4) throws ParserException {
        r2.setPosition(8);
        r2.readBytes(r4, 0, 16);
        if (Arrays.equals(r4, PIFF_SAMPLE_ENCRYPTION_BOX_EXTENDED_TYPE) == true) goto L5;
        return;
    L5:
        parseSenc(r2, 16, r3);
    }

    private void processAtomEnded(long r3) throws ParserException {
    L3:
        if (this.containerAtoms.isEmpty() == true) goto L7;
        if (this.containerAtoms.peek().endPosition != r3) goto L7;
        onContainerAtomRead(this.containerAtoms.pop());
    L7:
        enterReadingAtomHeaderState();
    }

    private boolean readAtomHeader(ExtractorInput r12) throws IOException {
        if (this.atomHeaderBytesRead != 0) goto L9;
        if (r12.readFully(this.atomHeader.getData(), 0, 8, true) == true) goto L7;
        return false;
    L7:
        this.atomHeaderBytesRead = 8;
        this.atomHeader.setPosition(0);
        this.atomSize = this.atomHeader.readUnsignedInt();
        this.atomType = this.atomHeader.readInt();
    L9:
        if (this.atomSize != 1) goto L12;
        r12.readFully(this.atomHeader.getData(), 8, 8);
        this.atomHeaderBytesRead += 8;
        this.atomSize = this.atomHeader.readUnsignedLongToLong();
    L23:
        if (this.atomSize < this.atomHeaderBytesRead) goto L65;
        long r4 = r12.getPosition() - ((long) this.atomHeaderBytesRead);
        if (this.atomType == 1836019558) goto L29;
        if (this.atomType == 1835295092) goto L29;
    L32:
        if (this.atomType != 1836019558) goto L37;
        int r0 = this.trackBundles.size();
        int r7 = 0;
    L34:
        if (r7 >= r0) goto L37;
        TrackFragment r8 = this.trackBundles.valueAt(r7).fragment;
        r8.atomPosition = r4;
        r8.auxiliaryDataPosition = r4;
        r8.dataPosition = r4;
        r7 = r7 + 1;
    L37:
        if (this.atomType != 1835295092) goto L41;
        this.currentTrackBundle = null;
        this.endOfMdatPosition = this.atomSize + r4;
        this.parserState = 2;
        return true;
    L41:
        if (shouldParseContainerAtom(this.atomType) == false) goto L48;
        long r1 = (r12.getPosition() + this.atomSize) - 8;
        this.containerAtoms.push(new Mp4Box.ContainerBox(this.atomType, r1));
        if (this.atomSize != this.atomHeaderBytesRead) goto L45;
        processAtomEnded(r1);
    L61:
        return true;
    L45:
        enterReadingAtomHeaderState();
        goto L61
    L48:
        if (shouldParseLeafAtom(this.atomType) == false) goto L59;
        if (this.atomHeaderBytesRead != 8) goto L57;
        if (this.atomSize > 2147483647L) goto L55;
        ParsableByteArray r2 = new ParsableByteArray((int) this.atomSize);
        System.arraycopy(this.atomHeader.getData(), 0, r2.getData(), 0, 8);
        this.atomData = r2;
        this.parserState = 1;
        goto L61
    L55:
        throw ParserException.createForUnsupportedContainerFeature("Leaf atom with length > 2147483647 (unsupported).");
    L57:
        throw ParserException.createForUnsupportedContainerFeature("Leaf atom defines extended atom size (unsupported).");
    L59:
        if (this.atomSize > 2147483647L) goto L63;
        this.atomData = null;
        this.parserState = 1;
        goto L61
    L63:
        throw ParserException.createForUnsupportedContainerFeature("Skipping atom with length > 2147483647 (unsupported).");
    L29:
        if (this.haveOutputSeekMap == true) goto L32;
        this.extractorOutput.seekMap(new SeekMap.Unseekable(this.durationUs, r4));
        this.haveOutputSeekMap = true;
        goto L32
    L65:
        throw ParserException.createForUnsupportedContainerFeature("Atom size less than header length (unsupported).");
    L12:
        if (this.atomSize != 0) goto L23;
        long r5 = r12.getLength();
        if (r5 != (-1)) goto L19;
        if (this.containerAtoms.isEmpty() == true) goto L19;
        r5 = this.containerAtoms.peek().endPosition;
    L19:
        if (r5 == (-1)) goto L23;
        this.atomSize = (r5 - r12.getPosition()) + ((long) this.atomHeaderBytesRead);
        goto L23
    }

    private void readAtomPayload(ExtractorInput r6) throws IOException {
        int r0 = ((int) this.atomSize) - this.atomHeaderBytesRead;
        ParsableByteArray r1 = this.atomData;
        if (r1 == null) goto L5;
        r6.readFully(r1.getData(), 8, r0);
        onLeafAtomRead(new Mp4Box.LeafBox(this.atomType, r1), r6.getPosition());
    L6:
        processAtomEnded(r6.getPosition());
        return;
    L5:
        r6.skipFully(r0);
        goto L6
    }

    private void readEncryptionData(ExtractorInput r9) throws IOException {
        TrackBundle r0 = null;
        long r1 = Long.MAX_VALUE;
        int r3 = this.trackBundles.size();
        int r4 = 0;
    L3:
        if (r4 >= r3) goto L10;
        TrackFragment r5 = this.trackBundles.valueAt(r4).fragment;
        if (r5.sampleEncryptionDataNeedsFill == false) goto L9;
        if (r5.auxiliaryDataPosition >= r1) goto L9;
        r1 = r5.auxiliaryDataPosition;
        r0 = this.trackBundles.valueAt(r4);
    L9:
        r4 = r4 + 1;
        goto L3
    L10:
        if (r0 != null) goto L13;
        this.parserState = 3;
        return;
    L13:
        int r6 = (int) (r1 - r9.getPosition());
        if (r6 < 0) goto L18;
        r9.skipFully(r6);
        r0.fragment.fillEncryptionData(r9);
        return;
    L18:
        throw ParserException.createForMalformedContainer("Offset to encryption data was negative.", null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v0 */
    /* JADX WARN: Type inference failed for: r9v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r9v2 */
    /* JADX WARN: Type inference failed for: r9v3 */
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
    private boolean readSample(ExtractorInput r19) throws IOException {
        TrackBundle r2 = this.currentTrackBundle;
        int r4 = 0;
        if (r2 != null) goto L16;
        r2 = getNextTrackBundle(this.trackBundles);
        if (r2 != null) goto L12;
        int r5 = (int) (this.endOfMdatPosition - r19.getPosition());
        if (r5 < 0) goto L11;
        r19.skipFully(r5);
        enterReadingAtomHeaderState();
        return false;
    L11:
        throw ParserException.createForMalformedContainer("Offset to end of mdat was negative.", null);
    L12:
        int r7 = (int) (r2.getCurrentSampleOffset() - r19.getPosition());
        if (r7 >= 0) goto L15;
        Log.m48w(TAG, "Ignoring negative offset to sample data.");
        r7 = 0;
    L15:
        r19.skipFully(r7);
        this.currentTrackBundle = r2;
    L16:
        int r8 = 4;
        ?? r9 = 1;
        if (this.parserState != 3) goto L40;
        this.sampleSize = r2.getCurrentSampleSize();
        if ((this.flags & 64) != 0) goto L21;
    L24:
        boolean r6 = true;
    L25:
        this.isSampleDependedOn = r6;
        if (r2.currentSampleIndex >= r2.firstSampleToOutputIndex) goto L33;
        r19.skipFully(this.sampleSize);
        r2.skipSampleEncryptionData();
        if (r2.next() == true) goto L30;
        this.currentTrackBundle = null;
    L30:
        this.parserState = 3;
        return true;
    L33:
        if (r2.moovSampleTable.track.sampleTransformation != 1) goto L36;
        this.sampleSize -= 8;
        r19.skipFully(8);
    L36:
        if (MimeTypes.AUDIO_AC4.equals(r2.moovSampleTable.track.format.sampleMimeType) == false) goto L38;
        this.sampleBytesWritten = r2.outputSampleEncryptionData(this.sampleSize, 7);
        Ac4Util.getAc4SampleHeader(this.sampleSize, this.scratch);
        r2.output.sampleData(this.scratch, 7);
        this.sampleBytesWritten += 7;
    L39:
        this.sampleSize += this.sampleBytesWritten;
        this.parserState = 4;
        this.sampleCurrentNalBytesRemaining = 0;
        goto L40
    L38:
        this.sampleBytesWritten = r2.outputSampleEncryptionData(this.sampleSize, 0);
        goto L39
    L21:
        if (Objects.equals(r2.moovSampleTable.track.format.sampleMimeType, MimeTypes.VIDEO_H264) == false) goto L24;
        r6 = false;
    L40:
        Track r10 = r2.moovSampleTable.track;
        TrackOutput r11 = r2.output;
        long r12 = r2.getCurrentSamplePresentationTimeUs();
        if (this.timestampAdjuster == null) goto L44;
        r12 = this.timestampAdjuster.adjustSampleTimestamp(r12);
    L44:
        if (r10.nalUnitLengthFieldLength == 0) goto L93;
        byte[] r13 = this.nalPrefix.getData();
        r13[0] = 0;
        r13[1] = 0;
        r13[2] = 0;
        int r14 = r10.nalUnitLengthFieldLength + 1;
        int r15 = 4 - r10.nalUnitLengthFieldLength;
    L47:
        if (this.sampleBytesWritten >= this.sampleSize) goto L92;
        if (this.sampleCurrentNalBytesRemaining == 0) goto L50;
        int r16 = r8;
        if (this.processSeiNalUnitPayload == false) goto L90;
        this.nalBuffer.reset(this.sampleCurrentNalBytesRemaining);
        r19.readFully(this.nalBuffer.getData(), r4, this.sampleCurrentNalBytesRemaining);
        r11.sampleData(this.nalBuffer, this.sampleCurrentNalBytesRemaining);
        int r3 = this.sampleCurrentNalBytesRemaining;
        int r17 = NalUnitUtil.unescapeStream(this.nalBuffer.getData(), this.nalBuffer.limit());
        ParsableByteArray r18 = this.nalBuffer;
        boolean r110 = r9 == true ? 1 : 0;
        if (Objects.equals(r10.format.sampleMimeType, MimeTypes.VIDEO_H265) == false) goto L74;
    L77:
        int r20 = r110 ? 1 : 0;
    L78:
        r18.setPosition(r20);
        this.nalBuffer.setLimit(r17);
        if (r10.format.maxNumReorderSamples != (-1)) goto L84;
        if (this.reorderingSeiMessageQueue.getMaxSize() == 0) goto L86;
        this.reorderingSeiMessageQueue.setMaxSize(0);
    L86:
        this.reorderingSeiMessageQueue.add(r12, this.nalBuffer);
        boolean r111 = r110;
        if ((r2.getCurrentSampleFlags() & 4) == 0) goto L91;
        this.reorderingSeiMessageQueue.flush();
        r111 = r110;
    L91:
        this.sampleBytesWritten += r3;
        this.sampleCurrentNalBytesRemaining -= r3;
        r8 = r16;
        r9 = r111;
        r4 = 0;
        goto L47
    L84:
        if (this.reorderingSeiMessageQueue.getMaxSize() == r10.format.maxNumReorderSamples) goto L86;
        this.reorderingSeiMessageQueue.setMaxSize(r10.format.maxNumReorderSamples);
        goto L86
    L74:
        if (MimeTypes.containsCodecsCorrespondingToMimeType(r10.format.codecs, MimeTypes.VIDEO_H265) == true) goto L77;
        r20 = 0;
        goto L78
    L90:
        r111 = r9 == true ? 1 : 0;
        r3 = r11.sampleData(r19, this.sampleCurrentNalBytesRemaining, false);
        goto L91
    L50:
        r19.readFully(r13, r15, r14);
        this.nalPrefix.setPosition(r4);
        int r21 = this.nalPrefix.readInt();
        if (r21 < r9) goto L68;
        this.sampleCurrentNalBytesRemaining = r21 - 1;
        this.nalStartCode.setPosition(r4);
        r11.sampleData(this.nalStartCode, r8);
        r11.sampleData(this.nalPrefix, r9 == true ? 1 : 0);
        if (this.ceaTrackOutputs.length <= 0) goto L57;
        int r112 = r8;
        if (NalUnitUtil.isNalUnitSei(r10.format, r13[r112]) == false) goto L58;
        boolean r22 = r9 == true ? 1 : 0;
    L59:
        this.processSeiNalUnitPayload = r22;
        this.sampleBytesWritten += 5;
        this.sampleSize += r15;
        if (this.isSampleDependedOn == false) goto L62;
    L66:
        r8 = r112;
        goto L47
    L62:
        if (Objects.equals(r2.moovSampleTable.track.format.sampleMimeType, MimeTypes.VIDEO_H264) == false) goto L66;
        if (NalUnitUtil.isH264NalUnitDependedOn(r13[r112]) == false) goto L66;
        this.isSampleDependedOn = r9;
    L58:
        r22 = r4;
        goto L59
    L57:
        r112 = r8;
        goto L58
    L68:
        throw ParserException.createForMalformedContainer("Invalid NAL length", null);
    L92:
        boolean r113 = r9 == true ? 1 : 0;
    L97:
        int r23 = r2.getCurrentSampleFlags();
        if ((this.flags & 64) != 0) goto L100;
    L102:
        int r114 = r23;
    L103:
        TrackEncryptionBox r24 = r2.getEncryptionBoxIfEncrypted();
        if (r24 == null) goto L106;
        TrackOutput.CryptoData r115 = r24.cryptoData;
    L107:
        r11.sampleMetadata(r12, r114, this.sampleSize, 0, r115);
        outputPendingMetadataSamples(r12);
        if (r2.next() == true) goto L110;
        this.currentTrackBundle = null;
    L110:
        this.parserState = 3;
        return r113;
    L106:
        r115 = null;
        goto L107
    L100:
        if (this.isSampleDependedOn == true) goto L102;
        r114 = r23 | C0180C.BUFFER_FLAG_NOT_DEPENDED_ON;
        goto L103
    L93:
        r113 = true;
    L95:
        if (this.sampleBytesWritten >= this.sampleSize) goto L97;
        this.sampleBytesWritten += r11.sampleData(r19, this.sampleSize - this.sampleBytesWritten, false);
        goto L95
    }

    private static boolean shouldParseContainerAtom(int r1) {
        if (r1 != 1836019574) goto L5;
    L22:
        return true;
    L5:
        if (r1 == 1953653099) goto L22;
        if (r1 == 1835297121) goto L22;
        if (r1 == 1835626086) goto L22;
        if (r1 == 1937007212) goto L22;
        if (r1 == 1836019558) goto L22;
        if (r1 == 1953653094) goto L22;
        if (r1 == 1836475768) goto L22;
        if (r1 == 1701082227) goto L22;
        return false;
    }

    private static boolean shouldParseLeafAtom(int r1) {
        if (r1 != 1751411826) goto L5;
    L60:
        return true;
    L5:
        if (r1 == 1835296868) goto L60;
        if (r1 == 1836476516) goto L60;
        if (r1 == 1936286840) goto L60;
        if (r1 == 1937011556) goto L60;
        if (r1 == 1937011827) goto L60;
        if (r1 == 1668576371) goto L60;
        if (r1 == 1937011555) goto L60;
        if (r1 == 1937011578) goto L60;
        if (r1 == 1937013298) goto L60;
        if (r1 == 1937007471) goto L60;
        if (r1 == 1668232756) goto L60;
        if (r1 == 1937011571) goto L60;
        if (r1 == 1952867444) goto L60;
        if (r1 == 1952868452) goto L60;
        if (r1 == 1953196132) goto L60;
        if (r1 == 1953654136) goto L60;
        if (r1 == 1953658222) goto L60;
        if (r1 == 1886614376) goto L60;
        if (r1 == 1935763834) goto L60;
        if (r1 == 1935763823) goto L60;
        if (r1 == 1936027235) goto L60;
        if (r1 == 1970628964) goto L60;
        if (r1 == 1935828848) goto L60;
        if (r1 == 1936158820) goto L60;
        if (r1 == 1701606260) goto L60;
        if (r1 == 1835362404) goto L60;
        if (r1 == 1701671783) goto L60;
        return false;
    }

    @Override // androidx.media3.extractor.Extractor
    public ImmutableList<SniffFailure> getSniffFailureDetails() {
        return this.lastSniffFailures;
    }

    @Override // androidx.media3.extractor.Extractor
    public /* bridge */ /* synthetic */ List getSniffFailureDetails() {
        return getSniffFailureDetails();
    }

    @Override // androidx.media3.extractor.Extractor
    public /* synthetic */ Extractor getUnderlyingImplementation() {
        return Extractor.CC.$default$getUnderlyingImplementation(this);
    }

    @Override // androidx.media3.extractor.Extractor
    public void init(ExtractorOutput r14) {
        if ((this.flags & 32) != 0) goto L5;
        ExtractorOutput r0 = new SubtitleTranscodingExtractorOutput(r14, this.subtitleParserFactory);
    L6:
        this.extractorOutput = r0;
        enterReadingAtomHeaderState();
        initExtraTracks();
        if (this.sideloadedTrack == null) goto L10;
        TrackBundle r1 = new TrackBundle(this.extractorOutput.track(0, this.sideloadedTrack.type), new TrackSampleTable(this.sideloadedTrack, new long[0], new int[0], 0, new long[0], new int[0], 0), new DefaultSampleValues(0, 0, 0, 0));
        this.trackBundles.put(0, r1);
        this.extractorOutput.endTracks();
        return;
    L10:
        return;
    L5:
        r0 = r14;
        goto L6
    }

    /* JADX INFO: renamed from: lambda$new$2$androidx-media3-extractor-mp4-FragmentedMp4Extractor */
    /* synthetic */ void m167x40e97494(long r2, ParsableByteArray r4) {
        CeaUtil.consume(r2, r4, this.ceaTrackOutputs);
    }

    protected Track modifyTrack(Track r1) {
        return r1;
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r2, PositionHolder r3) throws IOException {
    L3:
        switch(this.parserState) {
            case 0: goto L11;
            case 1: goto L9;
            case 2: goto L8;
            default: goto L5;
        };
    L8:
        readEncryptionData(r2);
        goto L3
    L9:
        readAtomPayload(r2);
        goto L3
    L11:
        if (readAtomHeader(r2) == true) goto L3;
        this.reorderingSeiMessageQueue.flush();
        return -1;
    L5:
        if (readSample(r2) == false) goto L3;
        return 0;
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r4, long r6) {
        int r0 = this.trackBundles.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        this.trackBundles.valueAt(r1).resetFragmentInfo();
        r1 = r1 + 1;
        goto L3
    L5:
        this.pendingMetadataSampleInfos.clear();
        this.pendingMetadataSampleBytes = 0;
        this.reorderingSeiMessageQueue.flush();
        this.pendingSeekTimeUs = r6;
        this.containerAtoms.clear();
        enterReadingAtomHeaderState();
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r3) throws IOException {
        SniffFailure r0 = Sniffer.sniffFragmented(r3);
        if (r0 == null) goto L5;
        ImmutableList<SniffFailure> r1 = ImmutableList.m213of(r0);
    L6:
        this.lastSniffFailures = r1;
        if (r0 != null) goto L9;
        return true;
    L9:
        return false;
    L5:
        r1 = ImmutableList.m212of();
        goto L6
    }
}
