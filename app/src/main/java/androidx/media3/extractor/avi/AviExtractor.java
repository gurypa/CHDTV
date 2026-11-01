package androidx.media3.extractor.avi;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.NoOpExtractorOutput;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.text.SubtitleParser;
import androidx.media3.extractor.text.SubtitleTranscodingExtractorOutput;
import com.google.common.collect.UnmodifiableIterator;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class AviExtractor implements Extractor {
    private static final int AVIIF_KEYFRAME = 16;
    public static final int FLAG_EMIT_RAW_SUBTITLE_DATA = 1;
    public static final int FOURCC_AVI_ = 541677121;
    public static final int FOURCC_JUNK = 1263424842;
    public static final int FOURCC_LIST = 1414744396;
    public static final int FOURCC_RIFF = 1179011410;
    public static final int FOURCC_auds = 1935963489;
    public static final int FOURCC_avih = 1751742049;
    public static final int FOURCC_hdrl = 1819436136;
    public static final int FOURCC_idx1 = 829973609;
    public static final int FOURCC_movi = 1769369453;
    public static final int FOURCC_strf = 1718776947;
    public static final int FOURCC_strh = 1752331379;
    public static final int FOURCC_strl = 1819440243;
    public static final int FOURCC_strn = 1852994675;
    public static final int FOURCC_txts = 1937012852;
    public static final int FOURCC_vids = 1935960438;
    private static final long RELOAD_MINIMUM_SEEK_DISTANCE = 262144;
    private static final int STATE_FINDING_IDX1_HEADER = 4;
    private static final int STATE_FINDING_MOVI_HEADER = 3;
    private static final int STATE_READING_HDRL_BODY = 2;
    private static final int STATE_READING_HDRL_HEADER = 1;
    private static final int STATE_READING_IDX1_BODY = 5;
    private static final int STATE_READING_SAMPLES = 6;
    private static final int STATE_SKIPPING_TO_HDRL = 0;
    private static final String TAG = "AviExtractor";
    private AviMainHeaderChunk aviHeader;
    private final ChunkHeaderHolder chunkHeaderHolder;
    private ChunkReader[] chunkReaders;
    private ChunkReader currentChunkReader;
    private long durationUs;
    private ExtractorOutput extractorOutput;
    private int hdrlSize;
    private int idx1BodySize;
    private long moviEnd;
    private long moviStart;
    private final boolean parseSubtitlesDuringExtraction;
    private long pendingReposition;
    private final ParsableByteArray scratch;
    private boolean seekMapHasBeenOutput;
    private int state;
    private final SubtitleParser.Factory subtitleParserFactory;

    /* JADX INFO: renamed from: androidx.media3.extractor.avi.AviExtractor$1 */
    static /* synthetic */ class C04071 {
    }

    private class AviSeekMap implements SeekMap {
        private final long durationUs;
        final /* synthetic */ AviExtractor this$0;

        public AviSeekMap(AviExtractor r1, long r2) {
            this.this$0 = r1;
            this.durationUs = r2;
        }

        @Override // androidx.media3.extractor.SeekMap
        public long getDurationUs() {
            return this.durationUs;
        }

        @Override // androidx.media3.extractor.SeekMap
        public SeekMap.SeekPoints getSeekPoints(long r8) {
            SeekMap.SeekPoints r0 = AviExtractor.access$100(this.this$0)[0].getSeekPoints(r8);
            int r1 = 1;
        L4:
            if (r1 >= AviExtractor.access$100(this.this$0).length) goto L9;
            SeekMap.SeekPoints r2 = AviExtractor.access$100(this.this$0)[r1].getSeekPoints(r8);
            if (r2.first.position >= r0.first.position) goto L8;
            r0 = r2;
        L8:
            r1 = r1 + 1;
            goto L4
        L9:
            return r0;
        }

        @Override // androidx.media3.extractor.SeekMap
        public boolean isSeekable() {
            return true;
        }
    }

    private static class ChunkHeaderHolder {
        public int chunkType;
        public int listType;
        public int size;

        private ChunkHeaderHolder() {
        }

        /* synthetic */ ChunkHeaderHolder(C04071 r1) {
            this();
        }

        public void populateFrom(ParsableByteArray r2) {
            this.chunkType = r2.readLittleEndianInt();
            this.size = r2.readLittleEndianInt();
            this.listType = 0;
        }

        public void populateWithListHeaderFrom(ParsableByteArray r3) throws ParserException {
            populateFrom(r3);
            if (this.chunkType != 1414744396) goto L7;
            this.listType = r3.readLittleEndianInt();
            return;
        L7:
            throw ParserException.createForMalformedContainer("LIST expected, found: " + this.chunkType, null);
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    @Deprecated
    public AviExtractor() {
        this(1, SubtitleParser.Factory.UNSUPPORTED);
    }

    public AviExtractor(int r4, SubtitleParser.Factory r5) {
        this.subtitleParserFactory = r5;
        if ((r4 & 1) != 0) goto L5;
        boolean r0 = true;
    L6:
        this.parseSubtitlesDuringExtraction = r0;
        this.scratch = new ParsableByteArray(12);
        this.chunkHeaderHolder = new ChunkHeaderHolder(null);
        this.extractorOutput = new NoOpExtractorOutput();
        this.chunkReaders = new ChunkReader[0];
        this.moviStart = -1;
        this.moviEnd = -1;
        this.hdrlSize = -1;
        this.durationUs = C0180C.TIME_UNSET;
        return;
    L5:
        r0 = false;
        goto L6
    }

    static /* synthetic */ ChunkReader[] access$100(AviExtractor r1) {
        return r1.chunkReaders;
    }

    private static void alignInputToEvenPosition(ExtractorInput r4) throws IOException {
        if ((r4.getPosition() & 1) != 1) goto L6;
        r4.skipFully(1);
        return;
    }

    private ChunkReader getChunkReader(int r6) {
        ChunkReader[] r0 = this.chunkReaders;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L8;
        ChunkReader r3 = r0[r2];
        if (r3.handlesChunkId(r6) == true) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        return r3;
    L8:
        return null;
    }

    private void parseHdrlBody(ParsableByteArray r9) throws IOException {
        ListChunk r1 = ListChunk.parseFrom(FOURCC_hdrl, r9);
        if (r1.getType() != 1819436136) goto L21;
        AviMainHeaderChunk r0 = (AviMainHeaderChunk) r1.getChild(AviMainHeaderChunk.class);
        if (r0 == null) goto L19;
        this.aviHeader = r0;
        this.durationUs = ((long) r0.totalFrames) * ((long) r0.frameDurationUs);
        ArrayList r2 = new ArrayList();
        int r3 = 0;
        UnmodifiableIterator<AviChunk> r4 = r1.children.iterator();
    L8:
        if (r4.hasNext() == false) goto L16;
        AviChunk r5 = r4.next();
        if (r5.getType() != 1819440243) goto L8;
        int r7 = r3 + 1;
        ChunkReader r6 = processStreamList((ListChunk) r5, r3);
        if (r6 == null) goto L14;
        r2.add(r6);
    L14:
        r3 = r7;
        goto L8
    L16:
        this.chunkReaders = (ChunkReader[]) r2.toArray(new ChunkReader[0]);
        this.extractorOutput.endTracks();
        return;
    L19:
        throw ParserException.createForMalformedContainer("AviHeader not found", null);
    L21:
        throw ParserException.createForMalformedContainer("Unexpected header list type " + r1.getType(), null);
    }

    private void parseIdx1Body(ParsableByteArray r12) {
        long r0 = peekSeekOffset(r12);
    L3:
        int r3 = 0;
        boolean r4 = false;
        if (r12.bytesLeft() < 16) goto L12;
        int r2 = r12.readLittleEndianInt();
        int r6 = r12.readLittleEndianInt();
        long r7 = ((long) r12.readLittleEndianInt()) + r0;
        r12.readLittleEndianInt();
        ChunkReader r9 = getChunkReader(r2);
        if (r9 == null) goto L3;
        if ((r6 & 16) != 16) goto L11;
        r4 = true;
    L11:
        r9.appendIndexChunk(r7, r4);
        goto L3
    L12:
        ChunkReader[] r5 = this.chunkReaders;
        int r8 = r5.length;
    L13:
        if (r3 >= r8) goto L15;
        r5[r3].compactIndex();
        r3 = r3 + 1;
        goto L13
    L15:
        this.seekMapHasBeenOutput = true;
        this.extractorOutput.seekMap(new AviSeekMap(this, this.durationUs));
    }

    private long peekSeekOffset(ParsableByteArray r9) {
        long r2 = 0;
        if (r9.bytesLeft() >= 16) goto L5;
        return 0;
    L5:
        int r0 = r9.getPosition();
        r9.skipBytes(8);
        if (r9.readLittleEndianInt() > this.moviStart) goto L9;
        r2 = this.moviStart + 8;
    L9:
        r9.setPosition(r0);
        return r2;
    }

    private ChunkReader processStreamList(ListChunk r15, int r16) {
        AviStreamHeaderChunk r8 = (AviStreamHeaderChunk) r15.getChild(AviStreamHeaderChunk.class);
        StreamFormatChunk r9 = (StreamFormatChunk) r15.getChild(StreamFormatChunk.class);
        if (r8 != null) goto L6;
        Log.m48w(TAG, "Missing Stream Header");
        return null;
    L6:
        if (r9 != null) goto L9;
        Log.m48w(TAG, "Missing Stream Format");
        return null;
    L9:
        long r4 = r8.getDurationUs();
        Format r10 = r9.format;
        Format.Builder r11 = r10.buildUpon();
        r11.setId(r16);
        int r12 = r8.suggestedBufferSize;
        if (r12 == 0) goto L12;
        r11.setMaxInputSize(r12);
    L12:
        StreamNameChunk r13 = (StreamNameChunk) r15.getChild(StreamNameChunk.class);
        if (r13 == null) goto L15;
        r11.setLabel(r13.name);
    L15:
        int r1 = MimeTypes.getTrackType(r10.sampleMimeType);
        if (r1 != 1) goto L18;
    L21:
        TrackOutput r7 = this.extractorOutput.track(r16, r1);
        r7.format(r11.build());
        ChunkReader r2 = new ChunkReader(r16, r1, r4, r8.length, r7);
        this.durationUs = Math.max(this.durationUs, r4);
        return r2;
    L18:
        if (r1 == 2) goto L21;
        return null;
    }

    private int readMoviChunks(ExtractorInput r9) throws IOException {
        if (r9.getPosition() < this.moviEnd) goto L7;
        return -1;
    L7:
        if (this.currentChunkReader != null) goto L9;
        alignInputToEvenPosition(r9);
        int r2 = 12;
        r9.peekFully(this.scratch.getData(), 0, 12);
        this.scratch.setPosition(0);
        int r0 = this.scratch.readLittleEndianInt();
        if (r0 != 1414744396) goto L19;
        this.scratch.setPosition(8);
        if (this.scratch.readLittleEndianInt() == 1769369453) goto L17;
        r2 = 8;
    L17:
        r9.skipFully(r2);
        r9.resetPeekPosition();
        return 0;
    L19:
        int r3 = this.scratch.readLittleEndianInt();
        if (r0 != 1263424842) goto L23;
        this.pendingReposition = (r9.getPosition() + ((long) r3)) + 8;
        return 0;
    L23:
        r9.skipFully(8);
        r9.resetPeekPosition();
        ChunkReader r4 = getChunkReader(r0);
        if (r4 != null) goto L27;
        this.pendingReposition = r9.getPosition() + ((long) r3);
        return 0;
    L27:
        r4.onChunkStart(r3);
        this.currentChunkReader = r4;
    L28:
        return 0;
    L9:
        if (this.currentChunkReader.onChunkData(r9) == false) goto L28;
        this.currentChunkReader = null;
        goto L28
    }

    private boolean resolvePendingReposition(ExtractorInput r10, PositionHolder r11) throws IOException {
        boolean r0 = false;
        if (this.pendingReposition == (-1)) goto L11;
        long r1 = r10.getPosition();
        if (this.pendingReposition >= r1) goto L7;
    L10:
        r11.position = this.pendingReposition;
        r0 = true;
        goto L11
    L7:
        if (this.pendingReposition > (262144 + r1)) goto L10;
        r10.skipFully((int) (this.pendingReposition - r1));
    L11:
        this.pendingReposition = -1;
        return r0;
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
        this.state = 0;
        if (this.parseSubtitlesDuringExtraction == false) goto L5;
        ExtractorOutput r0 = new SubtitleTranscodingExtractorOutput(r3, this.subtitleParserFactory);
    L6:
        this.extractorOutput = r0;
        this.pendingReposition = -1;
        return;
    L5:
        r0 = r3;
        goto L6
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r14, PositionHolder r15) throws IOException {
        if (resolvePendingReposition(r14, r15) == false) goto L6;
        return 1;
    L6:
        switch(this.state) {
            case 0: goto L53;
            case 1: goto L46;
            case 2: goto L44;
            case 3: goto L19;
            case 4: goto L13;
            case 5: goto L11;
            case 6: goto L10;
            default: goto L8;
        };
    L11:
        ParsableByteArray r0 = new ParsableByteArray(this.idx1BodySize);
        r14.readFully(r0.getData(), 0, this.idx1BodySize);
        parseIdx1Body(r0);
        this.state = 6;
        this.pendingReposition = this.moviStart;
        return 0;
    L13:
        r14.readFully(this.scratch.getData(), 0, 8);
        this.scratch.setPosition(0);
        int r1 = this.scratch.readLittleEndianInt();
        int r2 = this.scratch.readLittleEndianInt();
        if (r1 != 829973609) goto L16;
        this.state = 5;
        this.idx1BodySize = r2;
    L17:
        return 0;
    L16:
        this.pendingReposition = r14.getPosition() + ((long) r2);
        goto L17
    L44:
        int r3 = this.hdrlSize - 4;
        ParsableByteArray r4 = new ParsableByteArray(r3);
        r14.readFully(r4.getData(), 0, r3);
        parseHdrlBody(r4);
        this.state = 3;
        return 0;
    L46:
        r14.readFully(this.scratch.getData(), 0, 12);
        this.scratch.setPosition(0);
        this.chunkHeaderHolder.populateWithListHeaderFrom(this.scratch);
        if (this.chunkHeaderHolder.listType != 1819436136) goto L51;
        this.hdrlSize = this.chunkHeaderHolder.size;
        this.state = 2;
        return 0;
    L51:
        throw ParserException.createForMalformedContainer("hdrl expected, found: " + this.chunkHeaderHolder.listType, null);
    L8:
        throw new AssertionError();
    L10:
        return readMoviChunks(r14);
    L19:
        if (this.moviStart != (-1)) goto L21;
    L24:
        r14.peekFully(this.scratch.getData(), 0, 12);
        r14.resetPeekPosition();
        this.scratch.setPosition(0);
        this.chunkHeaderHolder.populateFrom(this.scratch);
        int r5 = this.scratch.readLittleEndianInt();
        if (this.chunkHeaderHolder.chunkType != 1179011410) goto L29;
        r14.skipFully(12);
        return 0;
    L29:
        if (this.chunkHeaderHolder.chunkType == 1414744396) goto L31;
    L42:
        this.pendingReposition = (r14.getPosition() + ((long) this.chunkHeaderHolder.size)) + 8;
        return 0;
    L31:
        if (r5 != 1769369453) goto L42;
        this.moviStart = r14.getPosition();
        this.moviEnd = (this.moviStart + ((long) this.chunkHeaderHolder.size)) + 8;
        if (this.seekMapHasBeenOutput == false) goto L36;
    L40:
        this.pendingReposition = r14.getPosition() + 12;
        this.state = 6;
        return 0;
    L36:
        if (((AviMainHeaderChunk) Assertions.checkNotNull(this.aviHeader)).hasIndex() == false) goto L39;
        this.state = 4;
        this.pendingReposition = this.moviEnd;
        return 0;
    L39:
        this.extractorOutput.seekMap(new SeekMap.Unseekable(this.durationUs));
        this.seekMapHasBeenOutput = true;
        goto L40
    L21:
        if (r14.getPosition() == this.moviStart) goto L24;
        this.pendingReposition = this.moviStart;
        return 0;
    L53:
        if (sniff(r14) == false) goto L57;
        r14.skipFully(12);
        this.state = 1;
        return 0;
    L57:
        throw ParserException.createForMalformedContainer("AVI Header List not found", null);
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r6, long r8) {
        this.pendingReposition = -1;
        this.currentChunkReader = null;
        ChunkReader[] r0 = this.chunkReaders;
        int r1 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L6;
        r0[r3].seekToPosition(r6);
        r3 = r3 + 1;
        goto L3
    L6:
        if (r6 == 0) goto L8;
        this.state = 6;
        return;
    L8:
        if (this.chunkReaders.length != 0) goto L10;
        this.state = 0;
        return;
    L10:
        this.state = 3;
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r4) throws IOException {
        r4.peekFully(this.scratch.getData(), 0, 12);
        this.scratch.setPosition(0);
        if (this.scratch.readLittleEndianInt() == 1179011410) goto L5;
        return false;
    L5:
        this.scratch.skipBytes(4);
        if (this.scratch.readLittleEndianInt() != 541677121) goto L9;
        return true;
    L9:
        return false;
    }
}
