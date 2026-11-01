package androidx.media3.extractor.text;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.IndexSeekMap;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.TrackOutput;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SubtitleExtractor implements Extractor {
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private static final int STATE_CREATED = 0;
    private static final int STATE_EXTRACTING = 2;
    private static final int STATE_FINISHED = 4;
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_RELEASED = 5;
    private static final int STATE_SEEKING = 3;
    private int bytesRead;
    private final CueEncoder cueEncoder;
    private final Format format;
    private final List<Sample> samples;
    private final ParsableByteArray scratchSampleArray;
    private long seekTimeUs;
    private int state;
    private byte[] subtitleData;
    private final SubtitleParser subtitleParser;
    private long[] timestamps;
    private TrackOutput trackOutput;

    /* JADX INFO: renamed from: androidx.media3.extractor.text.SubtitleExtractor$1 */
    static /* synthetic */ class C04431 {
    }

    private static class Sample implements Comparable<Sample> {
        private final byte[] data;
        private final long timeUs;

        private Sample(long r1, byte[] r3) {
            this.timeUs = r1;
            this.data = r3;
        }

        /* synthetic */ Sample(long r1, byte[] r3, C04431 r4) {
            this(r1, r3);
        }

        static /* synthetic */ long access$000(Sample r2) {
            return r2.timeUs;
        }

        static /* synthetic */ byte[] access$100(Sample r1) {
            return r1.data;
        }

        /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
        public int compareTo2(Sample r5) {
            return Long.compare(this.timeUs, r5.timeUs);
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(Sample r1) {
            return compareTo2(r1);
        }
    }

    public SubtitleExtractor(SubtitleParser r3, Format r4) {
        this.subtitleParser = r3;
        this.cueEncoder = new CueEncoder();
        this.subtitleData = Util.EMPTY_BYTE_ARRAY;
        this.scratchSampleArray = new ParsableByteArray();
        this.format = r4.buildUpon().setSampleMimeType(MimeTypes.APPLICATION_MEDIA3_CUES).setCodecs(r4.sampleMimeType).setCueReplacementBehavior(r3.getCueReplacementBehavior()).build();
        this.samples = new ArrayList();
        this.state = 0;
        this.timestamps = Util.EMPTY_LONG_ARRAY;
        this.seekTimeUs = C0180C.TIME_UNSET;
    }

    private void parseAndWriteToOutput() throws IOException {
    L12:
        e = move-exception;
        throw ParserException.createForMalformedContainer("SubtitleParser failed.", e);
    L3:
        if (this.seekTimeUs == C0180C.TIME_UNSET) goto L5;
        SubtitleParser.OutputOptions r0 = SubtitleParser.OutputOptions.cuesAfterThenRemainingCuesBefore(this.seekTimeUs);     // Catch: RuntimeException -> L12
    L6:
        SubtitleParser r1 = this.subtitleParser;     // Catch: RuntimeException -> L12
        byte[] r2 = this.subtitleData;     // Catch: RuntimeException -> L12
        int r4 = this.bytesRead;     // Catch: RuntimeException -> L12
        r1.parse(r2, 0, r4, r0, new SubtitleExtractor$$ExternalSyntheticLambda0(this));     // Catch: RuntimeException -> L12
        Collections.sort(this.samples);     // Catch: RuntimeException -> L12
        this.timestamps = new long[this.samples.size()];     // Catch: RuntimeException -> L12
        int r3 = 0;
    L8:
        if (r3 >= this.samples.size()) goto L10;
        this.timestamps[r3] = Sample.access$000(this.samples.get(r3));     // Catch: RuntimeException -> L12
        r3 = r3 + 1;     // Catch: RuntimeException -> L12
        goto L8
    L10:
        this.subtitleData = Util.EMPTY_BYTE_ARRAY;     // Catch: RuntimeException -> L12
        return;
    L5:
        r0 = SubtitleParser.OutputOptions.allCues();     // Catch: RuntimeException -> L12
        goto L6
    }

    private boolean readFromInput(ExtractorInput r7) throws IOException {
        if (this.subtitleData.length != this.bytesRead) goto L5;
        this.subtitleData = Arrays.copyOf(this.subtitleData, this.subtitleData.length + 1024);
    L5:
        int r0 = r7.read(this.subtitleData, this.bytesRead, this.subtitleData.length - this.bytesRead);
        if (r0 == (-1)) goto L8;
        this.bytesRead += r0;
    L8:
        long r2 = r7.getLength();
        if (r2 != (-1)) goto L11;
    L12:
        if (r0 != (-1)) goto L14;
    L13:
        return true;
    L14:
        return false;
    L11:
        if (this.bytesRead == r2) goto L13;
        goto L12
    }

    private boolean skipInput(ExtractorInput r5) throws IOException {
        if (r5.getLength() == (-1)) goto L5;
        int r0 = Ints.checkedCast(r5.getLength());
    L7:
        if (r5.skip(r0) != (-1)) goto L9;
        return true;
    L9:
        return false;
    L5:
        r0 = 1024;
        goto L7
    }

    private void writeToOutput() {
        if (this.seekTimeUs != C0180C.TIME_UNSET) goto L5;
        int r0 = 0;
    L6:
        int r1 = r0;
    L8:
        if (r1 >= this.samples.size()) goto L10;
        writeToOutput(this.samples.get(r1));
        r1 = r1 + 1;
        goto L8
    L10:
        return;
    L5:
        r0 = Util.binarySearchFloor(this.timestamps, this.seekTimeUs, true, true);
        goto L6
    }

    private void writeToOutput(Sample r9) {
        Assertions.checkStateNotNull(this.trackOutput);
        int r5 = Sample.access$100(r9).length;
        this.scratchSampleArray.reset(Sample.access$100(r9));
        this.trackOutput.sampleData(this.scratchSampleArray, r5);
        this.trackOutput.sampleMetadata(Sample.access$000(r9), 1, r5, 0, null);
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
    public void init(ExtractorOutput r8) {
        if (this.state != 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        this.trackOutput = r8.track(0, 3);
        this.trackOutput.format(this.format);
        r8.endTracks();
        r8.seekMap(new IndexSeekMap(new long[]{0}, new long[]{0}, C0180C.TIME_UNSET));
        this.state = 1;
        return;
    L5:
        r0 = false;
        goto L6
    }

    /* JADX INFO: renamed from: lambda$parseAndWriteToOutput$0$androidx-media3-extractor-text-SubtitleExtractor */
    /* synthetic */ void m168xdbba10ad(CuesWithTiming r8) {
        Sample r0 = new Sample(r8.startTimeUs, this.cueEncoder.encode(r8.cues, r8.durationUs), null);
        this.samples.add(r0);
        if (this.seekTimeUs != C0180C.TIME_UNSET) goto L5;
    L6:
        writeToOutput(r0);
        return;
    L5:
        if (r8.startTimeUs >= this.seekTimeUs) goto L6;
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r7, PositionHolder r8) throws IOException {
        if (this.state != 0) goto L5;
    L7:
        boolean r0 = false;
    L8:
        Assertions.checkState(r0);
        if (this.state != 1) goto L19;
        if (r7.getLength() == (-1)) goto L13;
        int r1 = Ints.checkedCast(r7.getLength());
    L15:
        if (r1 <= this.subtitleData.length) goto L17;
        this.subtitleData = new byte[r1];
    L17:
        this.bytesRead = 0;
        this.state = 2;
        goto L19
    L13:
        r1 = 1024;
    L19:
        if (this.state != 2) goto L24;
        if (readFromInput(r7) == false) goto L24;
        parseAndWriteToOutput();
        this.state = 4;
    L24:
        if (this.state != 3) goto L29;
        if (skipInput(r7) == false) goto L29;
        writeToOutput();
        this.state = 4;
    L29:
        if (this.state != 4) goto L32;
        return -1;
    L32:
        return 0;
    L5:
        if (this.state == 5) goto L7;
        r0 = true;
        goto L8
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
        if (this.state != 5) goto L5;
        return;
    L5:
        this.subtitleParser.reset();
        this.state = 5;
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r4, long r6) {
        if (this.state != 0) goto L5;
    L7:
        boolean r0 = false;
    L8:
        Assertions.checkState(r0);
        this.seekTimeUs = r6;
        if (this.state != 2) goto L12;
        this.state = 1;
    L12:
        if (this.state != 4) goto L15;
        this.state = 3;
        return;
    L15:
        return;
    L5:
        if (this.state == 5) goto L7;
        r0 = true;
        goto L8
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r2) throws IOException {
        return true;
    }
}
