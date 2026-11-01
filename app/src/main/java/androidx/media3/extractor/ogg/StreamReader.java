package androidx.media3.extractor.ogg;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.TrackOutput;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
abstract class StreamReader {
    private static final int STATE_END_OF_INPUT = 3;
    private static final int STATE_READ_HEADERS = 0;
    private static final int STATE_READ_PAYLOAD = 2;
    private static final int STATE_SKIP_HEADERS = 1;
    private long currentGranule;
    private ExtractorOutput extractorOutput;
    private boolean formatSet;
    private long lengthOfReadPacket;
    private final OggPacket oggPacket;
    private OggSeeker oggSeeker;
    private long payloadStartPosition;
    private int sampleRate;
    private boolean seekMapSet;
    private SetupData setupData;
    private int state;
    private long targetGranule;
    private TrackOutput trackOutput;

    /* JADX INFO: renamed from: androidx.media3.extractor.ogg.StreamReader$1 */
    static /* synthetic */ class C04411 {
    }

    static class SetupData {
        Format format;
        OggSeeker oggSeeker;

        SetupData() {
        }
    }

    private static final class UnseekableOggSeeker implements OggSeeker {
        private UnseekableOggSeeker() {
        }

        /* synthetic */ UnseekableOggSeeker(C04411 r1) {
            this();
        }

        @Override // androidx.media3.extractor.ogg.OggSeeker
        public SeekMap createSeekMap() {
            return new SeekMap.Unseekable(C0180C.TIME_UNSET);
        }

        @Override // androidx.media3.extractor.ogg.OggSeeker
        public long read(ExtractorInput r3) {
            return -1;
        }

        @Override // androidx.media3.extractor.ogg.OggSeeker
        public void startSeek(long r1) {
        }
    }

    public StreamReader() {
        this.oggPacket = new OggPacket();
        this.setupData = new SetupData();
    }

    @EnsuresNonNull({"trackOutput", "extractorOutput"})
    private void assertInitialized() {
        Assertions.checkStateNotNull(this.trackOutput);
        Util.castNonNull(this.extractorOutput);
    }

    @EnsuresNonNullIf(expression = {"setupData.format"}, result = true)
    private boolean readHeaders(ExtractorInput r5) throws IOException {
    L3:
        if (this.oggPacket.populate(r5) == false) goto L4;
        this.lengthOfReadPacket = r5.getPosition() - this.payloadStartPosition;
        if (readHeaders(this.oggPacket.getPayload(), this.payloadStartPosition, this.setupData) == false) goto L9;
        this.payloadStartPosition = r5.getPosition();
        goto L3
    L9:
        return true;
    L4:
        this.state = 3;
        return false;
    }

    @RequiresNonNull({"trackOutput"})
    private int readHeadersAndUpdateState(ExtractorInput r15) throws IOException {
        if (readHeaders(r15) == true) goto L6;
        return -1;
    L6:
        this.sampleRate = this.setupData.format.sampleRate;
        boolean r1 = true;
        if (this.formatSet == true) goto L10;
        this.trackOutput.format(this.setupData.format);
        this.formatSet = true;
    L10:
        if (this.setupData.oggSeeker == null) goto L13;
        this.oggSeeker = this.setupData.oggSeeker;
        StreamReader r4 = this;
    L20:
        r4.state = 2;
        r4.oggPacket.trimPayload();
        return 0;
    L13:
        if (r15.getLength() != (-1)) goto L15;
        this.oggSeeker = new UnseekableOggSeeker(null);
        r4 = this;
        goto L20
    L15:
        OggPageHeader r0 = this.oggPacket.getPageHeader();
        if ((r0.type & 4) != 0) goto L19;
        r1 = false;
    L19:
        r4 = this;
        r4.oggSeeker = new DefaultOggSeeker(r4, this.payloadStartPosition, r15.getLength(), r0.headerSize + r0.bodySize, r0.granulePosition, r1);
        goto L20
    }

    @RequiresNonNull({"trackOutput", "oggSeeker", "extractorOutput"})
    private int readPayload(ExtractorInput r20, PositionHolder r21) throws IOException {
        long r2 = this.oggSeeker.read(r20);
        if (r2 < 0) goto L7;
        r21.position = r2;
        return 1;
    L7:
        if (r2 >= (-1)) goto L10;
        onSeekEnd(-(2 + r2));
    L10:
        if (this.seekMapSet == true) goto L13;
        this.extractorOutput.seekMap((SeekMap) Assertions.checkStateNotNull(this.oggSeeker.createSeekMap()));
        this.seekMapSet = true;
    L13:
        if (this.lengthOfReadPacket <= 0) goto L15;
    L19:
        this.lengthOfReadPacket = 0;
        ParsableByteArray r7 = this.oggPacket.getPayload();
        long r10 = preparePayload(r7);
        if (r10 >= 0) goto L22;
    L24:
        this.currentGranule += r10;
        return 0;
    L22:
        if ((this.currentGranule + r10) < this.targetGranule) goto L24;
        long r13 = convertGranuleToTime(this.currentGranule);
        this.trackOutput.sampleData(r7, r7.limit());
        this.trackOutput.sampleMetadata(r13, 1, r7.limit(), 0, null);
        this.targetGranule = -1;
        goto L24
    L15:
        if (this.oggPacket.populate(r20) == true) goto L19;
        this.state = 3;
        return -1;
    }

    protected long convertGranuleToTime(long r5) {
        return (1000000 * r5) / ((long) this.sampleRate);
    }

    protected long convertTimeToGranule(long r5) {
        return (((long) this.sampleRate) * r5) / 1000000;
    }

    void init(ExtractorOutput r2, TrackOutput r3) {
        this.extractorOutput = r2;
        this.trackOutput = r3;
        reset(true);
    }

    protected void onSeekEnd(long r1) {
        this.currentGranule = r1;
    }

    protected abstract long preparePayload(ParsableByteArray r1);

    final int read(ExtractorInput r3, PositionHolder r4) throws IOException {
        assertInitialized();
        switch(this.state) {
            case 0: goto L13;
            case 1: goto L10;
            case 2: goto L8;
            case 3: goto L6;
            default: goto L5;
        };
    L6:
        return -1;
    L8:
        Util.castNonNull(this.oggSeeker);
        return readPayload(r3, r4);
    L10:
        r3.skipFully((int) this.payloadStartPosition);
        this.state = 2;
        return 0;
    L5:
        throw new IllegalStateException();
    L13:
        return readHeadersAndUpdateState(r3);
    }

    @EnsuresNonNullIf(expression = {"#3.format"}, result = false)
    protected abstract boolean readHeaders(ParsableByteArray r1, long r2, SetupData r4) throws IOException;

    protected void reset(boolean r5) {
        if (r5 == false) goto L5;
        this.setupData = new SetupData();
        this.payloadStartPosition = 0;
        this.state = 0;
    L6:
        this.targetGranule = -1;
        this.currentGranule = 0;
        return;
    L5:
        this.state = 1;
        goto L6
    }

    final void seek(long r4, long r6) {
        this.oggPacket.reset();
        if (r4 != 0) goto L6;
        reset(!this.seekMapSet);
        return;
    L6:
        if (this.state == 0) goto L10;
        this.targetGranule = convertTimeToGranule(r6);
        ((OggSeeker) Util.castNonNull(this.oggSeeker)).startSeek(this.targetGranule);
        this.state = 2;
        return;
    }
}
