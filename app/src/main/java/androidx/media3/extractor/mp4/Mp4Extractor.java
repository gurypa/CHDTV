package androidx.media3.extractor.mp4;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.container.MdtaMetadataEntry;
import androidx.media3.container.Mp4Box;
import androidx.media3.container.NalUnitUtil;
import androidx.media3.extractor.Ac4Util;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.GaplessInfoHolder;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.SeekPoint;
import androidx.media3.extractor.SniffFailure;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.TrueHdSampleRechunker;
import androidx.media3.extractor.metadata.mp4.MotionPhotoMetadata;
import androidx.media3.extractor.text.SubtitleParser;
import androidx.media3.extractor.text.SubtitleTranscodingExtractorOutput;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class Mp4Extractor implements Extractor, SeekMap {

    @Deprecated
    public static final ExtractorsFactory FACTORY = null;
    private static final int FILE_TYPE_HEIC = 2;
    private static final int FILE_TYPE_MP4 = 0;
    private static final int FILE_TYPE_QUICKTIME = 1;
    public static final int FLAG_EMIT_RAW_SUBTITLE_DATA = 16;
    public static final int FLAG_MARK_FIRST_VIDEO_TRACK_WITH_MAIN_ROLE = 8;
    public static final int FLAG_READ_EDITABLE_VIDEO_TRACKS = 64;
    public static final int FLAG_READ_MOTION_PHOTO_METADATA = 2;
    public static final int FLAG_READ_SEF_DATA = 4;
    public static final int FLAG_READ_WITHIN_GOP_SAMPLE_DEPENDENCIES = 32;
    public static final int FLAG_WORKAROUND_IGNORE_EDIT_LISTS = 1;
    private static final long MAXIMUM_READ_AHEAD_BYTES_STREAM = 10485760;
    private static final long RELOAD_MINIMUM_SEEK_DISTANCE = 262144;
    private static final int STATE_READING_ATOM_HEADER = 0;
    private static final int STATE_READING_ATOM_PAYLOAD = 1;
    private static final int STATE_READING_SAMPLE = 2;
    private static final int STATE_READING_SEF = 3;
    private long[][] accumulatedSampleSizes;
    private ParsableByteArray atomData;
    private final ParsableByteArray atomHeader;
    private int atomHeaderBytesRead;
    private long atomSize;
    private int atomType;
    private final ArrayDeque<Mp4Box.ContainerBox> containerAtoms;
    private long durationUs;
    private long edvdAtomOffset;
    private ExtractorOutput extractorOutput;
    private int fileType;
    private int firstVideoTrackIndex;
    private final int flags;
    private boolean isSampleDependedOn;
    private ImmutableList<SniffFailure> lastSniffFailures;
    private MotionPhotoMetadata motionPhotoMetadata;
    private final ParsableByteArray nalPrefix;
    private final ParsableByteArray nalStartCode;
    private int parserState;
    private boolean readingEditableVideoTracks;
    private int sampleBytesRead;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;
    private long sampleOffsetForEditableVideoTracks;
    private int sampleTrackIndex;
    private final ParsableByteArray scratch;
    private boolean seekToEdvdAtom;
    private boolean seenFtypAtom;
    private final SefReader sefReader;
    private final List<Metadata.Entry> slowMotionMetadataEntries;
    private final SubtitleParser.Factory subtitleParserFactory;
    private Mp4Track[] tracks;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    private static final class Mp4Track {
        public int sampleIndex;
        public final TrackSampleTable sampleTable;
        public final Track track;
        public final TrackOutput trackOutput;
        public final TrueHdSampleRechunker trueHdSampleRechunker;

        public Mp4Track(Track r3, TrackSampleTable r4, TrackOutput r5) {
            this.track = r3;
            this.sampleTable = r4;
            this.trackOutput = r5;
            if (MimeTypes.AUDIO_TRUEHD.equals(r3.format.sampleMimeType) == false) goto L5;
            TrueHdSampleRechunker r0 = new TrueHdSampleRechunker();
        L6:
            this.trueHdSampleRechunker = r0;
            return;
        L5:
            r0 = null;
            goto L6
        }
    }

    static {
        FACTORY = new Mp4Extractor$$ExternalSyntheticLambda2();
    }

    @Deprecated
    public Mp4Extractor() {
        this(SubtitleParser.Factory.UNSUPPORTED, 16);
    }

    @Deprecated
    public Mp4Extractor(int r2) {
        this(SubtitleParser.Factory.UNSUPPORTED, r2);
    }

    public Mp4Extractor(SubtitleParser.Factory r2) {
        this(r2, 0);
    }

    public Mp4Extractor(SubtitleParser.Factory r4, int r5) {
        this.subtitleParserFactory = r4;
        this.flags = r5;
        this.lastSniffFailures = ImmutableList.m212of();
        boolean r1 = false;
        if ((r5 & 4) == 0) goto L5;
        int r0 = 3;
    L6:
        this.parserState = r0;
        this.sefReader = new SefReader();
        this.slowMotionMetadataEntries = new ArrayList();
        this.atomHeader = new ParsableByteArray(16);
        this.containerAtoms = new ArrayDeque();
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalPrefix = new ParsableByteArray(5);
        this.scratch = new ParsableByteArray();
        this.sampleTrackIndex = -1;
        this.extractorOutput = ExtractorOutput.PLACEHOLDER;
        this.tracks = new Mp4Track[0];
        if ((r5 & 32) != 0) goto L9;
        r1 = true;
    L9:
        this.isSampleDependedOn = r1;
        return;
    L5:
        r0 = 0;
        goto L6
    }

    private static int brandToFileType(int r1) {
        switch(r1) {
            case 1751476579: goto L7;
            case 1903435808: goto L5;
            default: goto L3;
        };
    L3:
        return 0;
    L5:
        return 1;
    L7:
        return 2;
    }

    private static long[][] calculateAccumulatedSampleSizes(Mp4Track[] r14) {
        long[][] r0 = new long[r14.length][];
        int[] r1 = new int[r14.length];
        long[] r2 = new long[r14.length];
        boolean[] r3 = new boolean[r14.length];
        int r4 = 0;
    L4:
        if (r4 >= r14.length) goto L6;
        r0[r4] = new long[r14[r4].sampleTable.sampleCount];
        r2[r4] = r14[r4].sampleTable.timestampsUs[0];
        r4 = r4 + 1;
        goto L4
    L6:
        long r5 = 0;
        int r6 = 0;
    L8:
        if (r6 >= r14.length) goto L23;
        long r7 = Long.MAX_VALUE;
        int r9 = -1;
        int r10 = 0;
    L11:
        if (r10 >= r14.length) goto L18;
        if (r3[r10] == true) goto L17;
        if (r2[r10] > r7) goto L17;
        r9 = r10;
        r7 = r2[r10];
    L17:
        r10 = r10 + 1;
        goto L11
    L18:
        int r11 = r1[r9];
        r0[r9][r11] = r5;
        r5 = r5 + ((long) r14[r9].sampleTable.sizes[r11]);
        int r12 = r11 + 1;
        r1[r9] = r12;
        if (r12 < r0[r9].length) goto L20;
        r3[r9] = true;
        r6 = r6 + 1;
        goto L8
    L20:
        r2[r9] = r14[r9].sampleTable.timestampsUs[r12];
        goto L8
    L23:
        return r0;
    }

    private void enterReadingAtomHeaderState() {
        this.parserState = 0;
        this.atomHeaderBytesRead = 0;
    }

    private List<Integer> getAuxiliaryTrackTypesForEditableVideoTracks(Metadata r7) {
        List<Integer> r1 = ((MdtaMetadataEntry) Assertions.checkStateNotNull(MetadataUtil.findMdtaMetadataEntryWithKey(r7, MdtaMetadataEntry.KEY_EDITABLE_TRACKS_MAP))).getEditableTrackTypesFromMap();
        ArrayList r2 = new ArrayList(r1.size());
        int r3 = 0;
    L4:
        if (r3 >= r1.size()) goto L13;
        switch(r1.get(r3).intValue()) {
            case 0: goto L11;
            case 1: goto L10;
            case 2: goto L9;
            case 3: goto L8;
            default: goto L7;
        };
    L7:
        int r4 = 0;
    L12:
        r2.add(Integer.valueOf(r4));
        r3 = r3 + 1;
        goto L4
    L8:
        r4 = 4;
        goto L12
    L9:
        r4 = 3;
        goto L12
    L10:
        r4 = 2;
        goto L12
    L11:
        r4 = 1;
        goto L12
    L13:
        return r2;
    }

    private static int getSynchronizationSampleIndex(TrackSampleTable r2, long r3) {
        int r0 = r2.getIndexOfEarlierOrEqualSynchronizationSample(r3);
        if (r0 == (-1)) goto L5;
        return r0;
    L5:
        return r2.getIndexOfLaterOrEqualSynchronizationSample(r3);
    }

    private int getTrackIndexOfNextReadSample(long r24) {
        long r1 = Long.MAX_VALUE;
        boolean r3 = true;
        int r4 = -1;
        long r5 = Long.MAX_VALUE;
        long r7 = Long.MAX_VALUE;
        boolean r9 = true;
        int r10 = -1;
        int r11 = 0;
    L4:
        if (r11 >= this.tracks.length) goto L26;
        Mp4Track r12 = this.tracks[r11];
        int r13 = r12.sampleIndex;
        if (r13 == r12.sampleTable.sampleCount) goto L24;
        long r15 = r12.sampleTable.offsets[r13];
        long r17 = ((long[][]) Util.castNonNull(this.accumulatedSampleSizes))[r11][r13];
        long r19 = r15 - r24;
        if (r19 >= 0) goto L11;
    L14:
        boolean r14 = true;
    L15:
        if (r14 == true) goto L17;
        if (r3 == false) goto L17;
    L20:
        r3 = r14;
        r1 = r19;
        r4 = r11;
        r5 = r17;
    L22:
        if (r17 >= r7) goto L24;
        r7 = r17;
        r9 = r14;
        r10 = r11;
    L17:
        if (r14 != r3) goto L22;
        if (r19 >= r1) goto L22;
    L11:
        if (r19 >= 262144) goto L14;
        r14 = false;
    L24:
        r11 = r11 + 1;
        goto L4
    L26:
        if (r7 == Long.MAX_VALUE) goto L33;
        if (r9 == false) goto L33;
        if (r5 < (MAXIMUM_READ_AHEAD_BYTES_STREAM + r7)) goto L33;
        return r10;
    L33:
        return r4;
    }

    static /* synthetic */ Extractor[] lambda$newFactory$0(SubtitleParser.Factory r3) {
        return new Extractor[]{new Mp4Extractor(r3)};
    }

    static /* synthetic */ Track lambda$processMoovAtom$2(Track r0) {
        return r0;
    }

    static /* synthetic */ Extractor[] lambda$static$1() {
        return new Extractor[]{new Mp4Extractor(SubtitleParser.Factory.UNSUPPORTED, 16)};
    }

    private static long maybeAdjustSeekOffset(TrackSampleTable r6, long r7, long r9) {
        int r0 = getSynchronizationSampleIndex(r6, r7);
        if (r0 != (-1)) goto L6;
        return r9;
    L6:
        return Math.min(r6.offsets[r0], r9);
    }

    private void maybeSetDefaultSampleOffsetForEditableVideoTracks(Metadata r6) {
        MdtaMetadataEntry r0 = MetadataUtil.findMdtaMetadataEntryWithKey(r6, MdtaMetadataEntry.KEY_EDITABLE_TRACKS_SAMPLES_LOCATION);
        if (r0 != null) goto L5;
        return;
    L5:
        if (r0.value[0] != 0) goto L9;
        this.sampleOffsetForEditableVideoTracks = this.edvdAtomOffset + 16;
        return;
    }

    private void maybeSkipRemainingMetaAtomHeaderBytes(ExtractorInput r4) throws IOException {
        this.scratch.reset(8);
        r4.peekFully(this.scratch.getData(), 0, 8);
        BoxParser.maybeSkipRemainingMetaBoxHeaderBytes(this.scratch);
        r4.skipFully(this.scratch.getPosition());
        r4.resetPeekPosition();
    }

    public static ExtractorsFactory newFactory(final SubtitleParser.Factory r1) {
        return new Mp4Extractor$$ExternalSyntheticLambda0(r1);
    }

    private void processAtomEnded(long r5) throws ParserException {
    L3:
        if (this.containerAtoms.isEmpty() == true) goto L16;
        if (this.containerAtoms.peek().endPosition != r5) goto L16;
        Mp4Box.ContainerBox r0 = this.containerAtoms.pop();
        if (r0.type == 1836019574) goto L8;
        if (this.containerAtoms.isEmpty() == true) goto L3;
        this.containerAtoms.peek().add(r0);
        goto L3
    L8:
        processMoovAtom(r0);
        this.containerAtoms.clear();
        if (this.seekToEdvdAtom == true) goto L3;
        this.parserState = 2;
    L16:
        if (this.parserState == 2) goto L30;
        enterReadingAtomHeaderState();
        return;
    }

    private void processEndOfStreamReadingAtomHeader() {
        if (this.fileType == 2) goto L5;
        return;
    L5:
        if ((this.flags & 2) == 0) goto L13;
        TrackOutput r0 = this.extractorOutput.track(0, 4);
        if (this.motionPhotoMetadata != null) goto L9;
        Metadata r1 = null;
    L10:
        r0.format(new Format.Builder().setMetadata(r1).build());
        this.extractorOutput.endTracks();
        this.extractorOutput.seekMap(new SeekMap.Unseekable(C0180C.TIME_UNSET));
        return;
    L9:
        r1 = new Metadata(new Metadata.Entry[]{this.motionPhotoMetadata});
        goto L10
    }

    private static int processFtypAtom(ParsableByteArray r3) {
        r3.setPosition(8);
        int r1 = brandToFileType(r3.readInt());
        if (r1 == 0) goto L5;
        return r1;
    L5:
        r3.skipBytes(4);
    L7:
        if (r3.bytesLeft() <= 0) goto L11;
        int r2 = brandToFileType(r3.readInt());
        if (r2 == 0) goto L7;
        return r2;
    L11:
        return 0;
    }

    private void processMoovAtom(Mp4Box.ContainerBox r32) throws ParserException {
        Mp4Box.ContainerBox r9 = r32.getContainerBoxOfType(Mp4Box.TYPE_meta);
        ArrayList r3 = new ArrayList();
        if (r9 == null) goto L12;
        Metadata r2 = BoxParser.parseMdtaFromMeta(r9);
        if (this.readingEditableVideoTracks == false) goto L8;
        Assertions.checkStateNotNull(r2);
        maybeSetDefaultSampleOffsetForEditableVideoTracks(r2);
        Metadata r11 = r2;
        List<Integer> r12 = getAuxiliaryTrackTypesForEditableVideoTracks(r2);
    L13:
        int r13 = -1;
        long r14 = C0180C.TIME_UNSET;
        ArrayList r4 = new ArrayList();
        if (this.fileType != 1) goto L16;
        boolean r7 = true;
    L17:
        GaplessInfoHolder r5 = new GaplessInfoHolder();
        Mp4Box.LeafBox r16 = r32.getLeafBoxOfType(Mp4Box.TYPE_udta);
        if (r16 == null) goto L20;
        Metadata r6 = BoxParser.parseUdta(r16);
        r5.setFromMetadata(r6);
        Metadata r17 = r6;
    L21:
        Metadata r8 = new Metadata(new Metadata.Entry[]{BoxParser.parseMvhd(((Mp4Box.LeafBox) Assertions.checkNotNull(r32.getLeafBoxOfType(Mp4Box.TYPE_mvhd))).data)});
        if ((this.flags & 1) == 0) goto L24;
        boolean r10 = true;
    L25:
        GaplessInfoHolder r15 = r5;
        ArrayList r20 = r4;
        List<TrackSampleTable> r18 = BoxParser.parseTraks(r32, r15, C0180C.TIME_UNSET, null, r10, r7, new Mp4Extractor$$ExternalSyntheticLambda1());
        if (this.readingEditableVideoTracks == true) goto L28;
        int r22 = 0;
    L33:
        int r1 = 0;
        int r19 = 0;
    L35:
        if (r19 >= r18.size()) goto L81;
        TrackSampleTable r21 = r18.get(r19);
        if (r21.sampleCount != 0) goto L39;
        int r23 = r19;
        ArrayList r24 = r20;
    L80:
        int r25 = r23 + 1;
        r20 = r24;
        r18 = r18;
        r10 = r10;
        r7 = r7;
        r19 = r25;
        r15 = r15;
        goto L35
    L39:
        Track r110 = r21.track;
        int r26 = r19;
        if (r110.durationUs == C0180C.TIME_UNSET) goto L42;
        long r27 = r110.durationUs;
    L43:
        r14 = Math.max(r14, r27);
        int r28 = r1 + 1;
        Mp4Track r29 = new Mp4Track(r110, r21, this.extractorOutput.track(r1, r110.type));
        if (MimeTypes.AUDIO_TRUEHD.equals(r110.format.sampleMimeType) == false) goto L46;
        int r30 = r21.maximumSize * 16;
    L47:
        Format.Builder r31 = r110.format.buildUpon();
        r31.setMaxInputSize(r30);
        if (r110.type != 2) goto L69;
        int r33 = r110.format.roleFlags;
        if ((this.flags & 8) == 0) goto L57;
        if (r13 != (-1)) goto L54;
        int r34 = 1;
    L55:
        r33 = r33 | r34;
        goto L57
    L54:
        r34 = 2;
    L57:
        if (r110.format.frameRate != (-1.0f)) goto L65;
        if (r27 <= 0) goto L65;
        if (r21.sampleCount <= 0) goto L65;
        r31.setFrameRate(r21.sampleCount / (r27 / 1000000.0f));
    L65:
        if (this.readingEditableVideoTracks == false) goto L67;
        r23 = r26;
        r31.setAuxiliaryTrackType(r12.get(r23).intValue());
        int r35 = r33 | 32768;
    L68:
        r31.setRoleFlags(r35);
    L70:
        MetadataUtil.setFormatGaplessInfo(r110.type, r15, r31);
        int r36 = r110.type;
        Metadata[] r37 = new Metadata[3];
        if (this.slowMotionMetadataEntries.isEmpty() == false) goto L73;
        Metadata r38 = null;
    L74:
        r37[r22] = r38;
        r37[1] = r17;
        r37[2] = r8;
        MetadataUtil.setFormatMetadata(r36, r11, r31, r37);
        r29.trackOutput.format(r31.build());
        if (r110.type == 2) goto L77;
    L79:
        r24 = r20;
        r24.add(r29);
        r1 = r28;
        goto L80
    L77:
        if (r13 != (-1)) goto L79;
        r13 = r20.size();
        goto L79
    L73:
        r38 = new Metadata(this.slowMotionMetadataEntries);
        goto L74
    L67:
        r23 = r26;
        r35 = r33;
        goto L68
    L69:
        r23 = r26;
        goto L70
    L46:
        r30 = r21.maximumSize + 30;
        goto L47
    L42:
        r27 = r21.durationUs;
        goto L43
    L81:
        this.firstVideoTrackIndex = r13;
        this.durationUs = r14;
        this.tracks = (Mp4Track[]) r20.toArray(new Mp4Track[r22]);
        this.accumulatedSampleSizes = calculateAccumulatedSampleSizes(this.tracks);
        this.extractorOutput.endTracks();
        this.extractorOutput.seekMap(this);
        return;
    L28:
        if (r12.size() != r18.size()) goto L30;
        boolean r39 = true;
    L31:
        r22 = 0;
        Assertions.checkState(r39, String.format(Locale.US, "The number of auxiliary track types from metadata (%d) is not same as the number of editable video tracks (%d)", new Object[]{Integer.valueOf(r12.size()), Integer.valueOf(r18.size())}));
        goto L33
    L30:
        r39 = false;
        goto L31
    L24:
        r10 = false;
        goto L25
    L20:
        r17 = null;
        goto L21
    L16:
        r7 = false;
        goto L17
    L8:
        if (shouldSeekToEdvdAtom(r2) == false) goto L11;
        this.seekToEdvdAtom = true;
        return;
    L11:
        r11 = r2;
        r12 = r3;
        goto L13
    L12:
        r11 = null;
        r12 = r3;
        goto L13
    }

    private void processUnparsedAtom(long r14) {
        if (this.atomType != 1836086884) goto L6;
        this.motionPhotoMetadata = new MotionPhotoMetadata(0, r14, C0180C.TIME_UNSET, r14 + ((long) this.atomHeaderBytesRead), this.atomSize - ((long) this.atomHeaderBytesRead));
        return;
    }

    private boolean readAtomHeader(ExtractorInput r11) throws IOException {
        if (this.atomHeaderBytesRead != 0) goto L10;
        if (r11.readFully(this.atomHeader.getData(), 0, 8, true) == true) goto L8;
        processEndOfStreamReadingAtomHeader();
        return false;
    L8:
        this.atomHeaderBytesRead = 8;
        this.atomHeader.setPosition(0);
        this.atomSize = this.atomHeader.readUnsignedInt();
        this.atomType = this.atomHeader.readInt();
    L10:
        if (this.atomSize != 1) goto L13;
        r11.readFully(this.atomHeader.getData(), 8, 8);
        this.atomHeaderBytesRead += 8;
        this.atomSize = this.atomHeader.readUnsignedLongToLong();
    L24:
        if (this.atomSize < this.atomHeaderBytesRead) goto L51;
        if (shouldParseContainerAtom(this.atomType) == false) goto L38;
        long r0 = (r11.getPosition() + this.atomSize) - ((long) this.atomHeaderBytesRead);
        if (this.atomSize != this.atomHeaderBytesRead) goto L30;
    L32:
        this.containerAtoms.push(new Mp4Box.ContainerBox(this.atomType, r0));
        if (this.atomSize != this.atomHeaderBytesRead) goto L35;
        processAtomEnded(r0);
    L49:
        return true;
    L35:
        enterReadingAtomHeaderState();
        goto L49
    L30:
        if (this.atomType != 1835365473) goto L32;
        maybeSkipRemainingMetaAtomHeaderBytes(r11);
        goto L32
    L38:
        if (shouldParseLeafAtom(this.atomType) == true) goto L40;
        processUnparsedAtom(r11.getPosition() - ((long) this.atomHeaderBytesRead));
        this.atomData = null;
        this.parserState = 1;
        goto L49
    L40:
        if (this.atomHeaderBytesRead != 8) goto L42;
        boolean r1 = true;
    L43:
        Assertions.checkState(r1);
        if (this.atomSize > 2147483647L) goto L46;
        boolean r2 = true;
    L47:
        Assertions.checkState(r2);
        ParsableByteArray r3 = new ParsableByteArray((int) this.atomSize);
        System.arraycopy(this.atomHeader.getData(), 0, r3.getData(), 0, 8);
        this.atomData = r3;
        this.parserState = 1;
        goto L49
    L46:
        r2 = false;
        goto L47
    L42:
        r1 = false;
        goto L43
    L51:
        throw ParserException.createForUnsupportedContainerFeature("Atom size less than header length (unsupported).");
    L13:
        if (this.atomSize != 0) goto L24;
        long r4 = r11.getLength();
        if (r4 != (-1)) goto L20;
        Mp4Box.ContainerBox r5 = this.containerAtoms.peek();
        if (r5 == null) goto L20;
        r4 = r5.endPosition;
    L20:
        if (r4 == (-1)) goto L24;
        this.atomSize = (r4 - r11.getPosition()) + ((long) this.atomHeaderBytesRead);
        goto L24
    }

    private boolean readAtomPayload(ExtractorInput r12, PositionHolder r13) throws IOException {
        long r0 = this.atomSize - ((long) this.atomHeaderBytesRead);
        long r2 = r12.getPosition() + r0;
        boolean r4 = false;
        ParsableByteArray r5 = this.atomData;
        if (r5 == null) goto L11;
        r12.readFully(r5.getData(), this.atomHeaderBytesRead, (int) r0);
        if (this.atomType != 1718909296) goto L8;
        this.seenFtypAtom = true;
        this.fileType = processFtypAtom(r5);
    L19:
        processAtomEnded(r2);
        if (this.seekToEdvdAtom == false) goto L22;
        this.readingEditableVideoTracks = true;
        r13.position = this.edvdAtomOffset;
        r4 = true;
        this.seekToEdvdAtom = false;
    L22:
        if (r4 == false) goto L27;
        if (this.parserState == 2) goto L27;
        return true;
    L27:
        return false;
    L8:
        if (this.containerAtoms.isEmpty() == true) goto L19;
        this.containerAtoms.peek().add(new Mp4Box.LeafBox(this.atomType, r5));
        goto L19
    L11:
        if (this.seenFtypAtom == true) goto L16;
        if (this.atomType != 1835295092) goto L16;
        this.fileType = 1;
    L16:
        if (r0 >= 262144) goto L18;
        r12.skipFully((int) r0);
        goto L19
    L18:
        r13.position = r12.getPosition() + r0;
        r4 = true;
        goto L19
    }

    private int readSample(ExtractorInput r27, PositionHolder r28) throws IOException {
        long r2 = r27.getPosition();
        if (this.sampleTrackIndex != (-1)) goto L7;
        this.sampleTrackIndex = getTrackIndexOfNextReadSample(r2);
        if (this.sampleTrackIndex != (-1)) goto L7;
        return -1;
    L7:
        Mp4Track r4 = this.tracks[this.sampleTrackIndex];
        TrackOutput r6 = r4.trackOutput;
        int r14 = r4.sampleIndex;
        long r8 = r4.sampleTable.offsets[r14] + this.sampleOffsetForEditableVideoTracks;
        int r7 = r4.sampleTable.sizes[r14];
        TrueHdSampleRechunker r10 = r4.trueHdSampleRechunker;
        TrackOutput r16 = r6;
        long r11 = (r8 - r2) + ((long) this.sampleBytesRead);
        if (r11 < 0) goto L65;
        if (r11 >= 262144) goto L65;
        if (r4.track.sampleTransformation != 1) goto L15;
        r11 = r11 + 8;
        r7 = r7 - 8;
    L15:
        r27.skipFully((int) r11);
        if (Objects.equals(r4.track.format.sampleMimeType, MimeTypes.VIDEO_H264) == true) goto L19;
        this.isSampleDependedOn = true;
    L19:
        if (r4.track.nalUnitLengthFieldLength == 0) goto L39;
        byte[] r5 = this.nalPrefix.getData();
        r5[0] = 0;
        r5[1] = 0;
        r5[2] = 0;
        int r13 = r4.track.nalUnitLengthFieldLength + 1;
        int r9 = 4 - r4.track.nalUnitLengthFieldLength;
    L22:
        if (this.sampleBytesWritten >= r7) goto L38;
        if (this.sampleCurrentNalBytesRemaining == 0) goto L25;
        long r22 = r2;
        int r3 = r16.sampleData(r27, this.sampleCurrentNalBytesRemaining, false);
        this.sampleBytesRead += r3;
        this.sampleBytesWritten += r3;
        this.sampleCurrentNalBytesRemaining -= r3;
        r2 = r22;
        goto L22
    L25:
        r27.readFully(r5, r9, r13);
        this.sampleBytesRead += r13;
        long r23 = r2;
        this.nalPrefix.setPosition(0);
        int r12 = this.nalPrefix.readInt();
        if (r12 < 1) goto L36;
        this.sampleCurrentNalBytesRemaining = r12 - 1;
        this.nalStartCode.setPosition(0);
        TrackOutput r15 = r16;
        r15.sampleData(this.nalStartCode, 4);
        r15.sampleData(this.nalPrefix, 1);
        this.sampleBytesWritten += 5;
        r7 = r7 + r9;
        if (this.isSampleDependedOn == true) goto L33;
        char r21 = 4;
        if (NalUnitUtil.isH264NalUnitDependedOn(r5[4]) == false) goto L34;
        this.isSampleDependedOn = true;
    L34:
        r16 = r15;
        r2 = r23;
        goto L22
    L33:
        r21 = 4;
        goto L34
    L36:
        throw ParserException.createForMalformedContainer("Invalid NAL length", null);
    L38:
        TrackOutput r17 = r16;
        TrackOutput.CryptoData r18 = null;
    L50:
        long r19 = r4.sampleTable.timestampsUs[r14];
        int r20 = r4.sampleTable.flags[r14];
        if (this.isSampleDependedOn == true) goto L53;
        r20 = r20 | C0180C.BUFFER_FLAG_NOT_DEPENDED_ON;
    L53:
        if (r10 == null) goto L57;
        int r110 = r7;
        TrackOutput.CryptoData r24 = r18;
        boolean r111 = true;
        r10.sampleMetadata(r17, r19, r20, r110, 0, null);
        if ((r14 + 1) != r4.sampleTable.sampleCount) goto L58;
        r10.outputPendingSampleMetadata(r17, r24);
    L58:
        r4.sampleIndex++;
        this.sampleTrackIndex = -1;
        this.sampleBytesRead = 0;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        if ((this.flags & 32) != 0) goto L61;
        boolean r25 = r111;
    L62:
        this.isSampleDependedOn = r25;
        return 0;
    L61:
        r25 = false;
        goto L62
    L57:
        r111 = true;
        r17.sampleMetadata(r19, r20, r7, 0, null);
        goto L58
    L39:
        r17 = r16;
        r18 = null;
        if (MimeTypes.AUDIO_AC4.equals(r4.track.format.sampleMimeType) == true) goto L42;
        if (r10 == null) goto L48;
        r10.startSample(r27);
    L48:
        if (this.sampleBytesWritten >= r7) goto L50;
        int r26 = r17.sampleData(r27, r7 - this.sampleBytesWritten, false);
        this.sampleBytesRead += r26;
        this.sampleBytesWritten += r26;
        this.sampleCurrentNalBytesRemaining -= r26;
        goto L48
    L42:
        if (this.sampleBytesWritten != 0) goto L44;
        Ac4Util.getAc4SampleHeader(r7, this.scratch);
        r17.sampleData(this.scratch, 7);
        this.sampleBytesWritten += 7;
    L44:
        r7 = r7 + 7;
    L65:
        r28.position = r8;
        return 1;
    }

    private int readSefData(ExtractorInput r6, PositionHolder r7) throws IOException {
        int r0 = this.sefReader.read(r6, r7, this.slowMotionMetadataEntries);
        if (r0 == 1) goto L5;
    L7:
        return r0;
    L5:
        if (r7.position != 0) goto L7;
        enterReadingAtomHeaderState();
        goto L7
    }

    private static boolean shouldParseContainerAtom(int r1) {
        if (r1 != 1836019574) goto L5;
    L20:
        return true;
    L5:
        if (r1 == 1953653099) goto L20;
        if (r1 == 1835297121) goto L20;
        if (r1 == 1835626086) goto L20;
        if (r1 == 1937007212) goto L20;
        if (r1 == 1701082227) goto L20;
        if (r1 == 1835365473) goto L20;
        if (r1 == 1701082724) goto L20;
        return false;
    }

    private static boolean shouldParseLeafAtom(int r1) {
        if (r1 != 1835296868) goto L5;
    L40:
        return true;
    L5:
        if (r1 == 1836476516) goto L40;
        if (r1 == 1751411826) goto L40;
        if (r1 == 1937011556) goto L40;
        if (r1 == 1937011827) goto L40;
        if (r1 == 1937011571) goto L40;
        if (r1 == 1668576371) goto L40;
        if (r1 == 1701606260) goto L40;
        if (r1 == 1937011555) goto L40;
        if (r1 == 1937011578) goto L40;
        if (r1 == 1937013298) goto L40;
        if (r1 == 1937007471) goto L40;
        if (r1 == 1668232756) goto L40;
        if (r1 == 1953196132) goto L40;
        if (r1 == 1718909296) goto L40;
        if (r1 == 1969517665) goto L40;
        if (r1 == 1801812339) goto L40;
        if (r1 == 1768715124) goto L40;
        return false;
    }

    private boolean shouldSeekToEdvdAtom(Metadata r7) {
        if (r7 != null) goto L6;
        return false;
    L6:
        if ((this.flags & 64) == 0) goto L13;
        MdtaMetadataEntry r1 = MetadataUtil.findMdtaMetadataEntryWithKey(r7, MdtaMetadataEntry.KEY_EDITABLE_TRACKS_OFFSET);
        if (r1 == null) goto L13;
        long r2 = new ParsableByteArray(r1.value).readUnsignedLongToLong();
        if (r2 <= 0) goto L13;
        this.edvdAtomOffset = r2;
        return true;
    L13:
        return false;
    }

    private void updateSampleIndex(Mp4Track r4, long r5) {
        TrackSampleTable r0 = r4.sampleTable;
        int r1 = r0.getIndexOfEarlierOrEqualSynchronizationSample(r5);
        if (r1 != (-1)) goto L5;
        r1 = r0.getIndexOfLaterOrEqualSynchronizationSample(r5);
    L5:
        r4.sampleIndex = r1;
    }

    @Override // androidx.media3.extractor.SeekMap
    public long getDurationUs() {
        return this.durationUs;
    }

    public long[] getSampleTimestampsUs(int r2) {
        if (this.tracks.length > r2) goto L7;
        return new long[0];
    L7:
        return this.tracks[r2].sampleTable.timestampsUs;
    }

    @Override // androidx.media3.extractor.SeekMap
    public SeekMap.SeekPoints getSeekPoints(long r2) {
        return getSeekPoints(r2, -1);
    }

    public SeekMap.SeekPoints getSeekPoints(long r22, int r24) {
        if (this.tracks.length == 0) goto L5;
        long r4 = C0180C.TIME_UNSET;
        long r6 = -1;
        if (r24 == (-1)) goto L9;
        int r9 = r24;
    L10:
        if (r9 == (-1)) goto L24;
        TrackSampleTable r10 = this.tracks[r9].sampleTable;
        int r11 = getSynchronizationSampleIndex(r10, r22);
        if (r11 == (-1)) goto L14;
        long r13 = r10.timestampsUs[r11];
        long r17 = r10.offsets[r11];
        if (r13 < r22) goto L18;
    L23:
        long r12 = r13;
    L26:
        if (r24 != (-1)) goto L36;
        int r8 = 0;
        long r14 = r17;
    L28:
        long r19 = C0180C.TIME_UNSET;
        if (r8 >= this.tracks.length) goto L37;
        if (r8 == this.firstVideoTrackIndex) goto L35;
        TrackSampleTable r15 = this.tracks[r8].sampleTable;
        r14 = maybeAdjustSeekOffset(r15, r12, r14);
        if (r4 == C0180C.TIME_UNSET) goto L35;
        r6 = maybeAdjustSeekOffset(r15, r4, r6);
    L35:
        r8 = r8 + 1;
    L37:
        SeekPoint r16 = new SeekPoint(r12, r14);
        if (r4 != r19) goto L42;
        return new SeekMap.SeekPoints(r16);
    L42:
        return new SeekMap.SeekPoints(r16, new SeekPoint(r4, r6));
    L36:
        r19 = C0180C.TIME_UNSET;
        r14 = r17;
        goto L37
    L18:
        if (r11 >= (r10.sampleCount - 1)) goto L23;
        int r18 = r10.getIndexOfLaterOrEqualSynchronizationSample(r22);
        if (r18 == (-1)) goto L23;
        if (r18 == r11) goto L23;
        r4 = r10.timestampsUs[r18];
        r6 = r10.offsets[r18];
        goto L23
    L14:
        return new SeekMap.SeekPoints(SeekPoint.START);
    L24:
        r17 = Long.MAX_VALUE;
        r12 = r22;
        goto L26
    L9:
        r9 = this.firstVideoTrackIndex;
        goto L10
    L5:
        return new SeekMap.SeekPoints(SeekPoint.START);
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
    public void init(ExtractorOutput r3) {
        if ((this.flags & 16) != 0) goto L5;
        ExtractorOutput r0 = new SubtitleTranscodingExtractorOutput(r3, this.subtitleParserFactory);
    L6:
        this.extractorOutput = r0;
        return;
    L5:
        r0 = r3;
        goto L6
    }

    @Override // androidx.media3.extractor.SeekMap
    public boolean isSeekable() {
        return true;
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r2, PositionHolder r3) throws IOException {
    L3:
        switch(this.parserState) {
            case 0: goto L15;
            case 1: goto L11;
            case 2: goto L9;
            case 3: goto L7;
            default: goto L5;
        };
    L5:
        throw new IllegalStateException();
    L7:
        return readSefData(r2, r3);
    L9:
        return readSample(r2, r3);
    L15:
        if (readAtomHeader(r2) == true) goto L3;
        return -1;
    L11:
        if (readAtomPayload(r2, r3) == false) goto L3;
        return 1;
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r6, long r8) {
        this.containerAtoms.clear();
        int r0 = 0;
        this.atomHeaderBytesRead = 0;
        this.sampleTrackIndex = -1;
        this.sampleBytesRead = 0;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        if ((this.flags & 32) != 0) goto L5;
        boolean r1 = true;
    L6:
        this.isSampleDependedOn = r1;
        if (r6 == 0) goto L9;
        Mp4Track[] r2 = this.tracks;
        int r3 = r2.length;
    L13:
        if (r0 >= r3) goto L23;
        Mp4Track r4 = r2[r0];
        updateSampleIndex(r4, r8);
        if (r4.trueHdSampleRechunker == null) goto L17;
        r4.trueHdSampleRechunker.reset();
    L17:
        r0 = r0 + 1;
        goto L13
    L23:
        return;
    L9:
        if (this.parserState == 3) goto L11;
        enterReadingAtomHeaderState();
        return;
    L11:
        this.sefReader.reset();
        this.slowMotionMetadataEntries.clear();
        return;
    L5:
        r1 = false;
        goto L6
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r5) throws IOException {
        if ((this.flags & 2) == 0) goto L5;
        boolean r0 = true;
    L6:
        SniffFailure r1 = Sniffer.sniffUnfragmented(r5, r0);
        if (r1 == null) goto L9;
        ImmutableList<SniffFailure> r3 = ImmutableList.m213of(r1);
    L10:
        this.lastSniffFailures = r3;
        if (r1 != null) goto L14;
        return true;
    L14:
        return false;
    L9:
        r3 = ImmutableList.m212of();
        goto L10
    L5:
        r0 = false;
        goto L6
    }
}
