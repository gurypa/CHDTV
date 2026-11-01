package androidx.media3.extractor.ogg;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.audio.SilenceSkippingAudioProcessor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorUtil;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.SeekPoint;
import java.io.EOFException;
import java.io.IOException;
import java.math.BigInteger;

/* JADX INFO: loaded from: classes.dex */
final class DefaultOggSeeker implements OggSeeker {
    private static final int DEFAULT_OFFSET = 30000;
    private static final int MATCH_BYTE_RANGE = 100000;
    private static final int MATCH_RANGE = 72000;
    private static final int STATE_IDLE = 4;
    private static final int STATE_READ_LAST_PAGE = 1;
    private static final int STATE_SEEK = 2;
    private static final int STATE_SEEK_TO_END = 0;
    private static final int STATE_SKIP = 3;
    private long end;
    private long endGranule;
    private final OggPageHeader pageHeader;
    private final long payloadEndPosition;
    private final long payloadStartPosition;
    private long positionBeforeSeekToEnd;
    private long start;
    private long startGranule;
    private int state;
    private final StreamReader streamReader;
    private long targetGranule;
    private long totalGranules;

    /* JADX INFO: renamed from: androidx.media3.extractor.ogg.DefaultOggSeeker$1 */
    static /* synthetic */ class C04401 {
    }

    private final class OggSeekMap implements SeekMap {
        final /* synthetic */ DefaultOggSeeker this$0;

        private OggSeekMap(DefaultOggSeeker r1) {
            this.this$0 = r1;
        }

        /* synthetic */ OggSeekMap(DefaultOggSeeker r1, C04401 r2) {
            this(r1);
        }

        @Override // androidx.media3.extractor.SeekMap
        public long getDurationUs() {
            return DefaultOggSeeker.access$100(this.this$0).convertGranuleToTime(DefaultOggSeeker.access$300(this.this$0));
        }

        @Override // androidx.media3.extractor.SeekMap
        public SeekMap.SeekPoints getSeekPoints(long r13) {
            long r0 = DefaultOggSeeker.access$100(this.this$0).convertTimeToGranule(r13);
            return new SeekMap.SeekPoints(new SeekPoint(r13, Util.constrainValue((DefaultOggSeeker.access$200(this.this$0) + BigInteger.valueOf(r0).multiply(BigInteger.valueOf(DefaultOggSeeker.access$400(this.this$0) - DefaultOggSeeker.access$200(this.this$0))).divide(BigInteger.valueOf(DefaultOggSeeker.access$300(this.this$0))).longValue()) - 30000, DefaultOggSeeker.access$200(this.this$0), DefaultOggSeeker.access$400(this.this$0) - 1)));
        }

        @Override // androidx.media3.extractor.SeekMap
        public boolean isSeekable() {
            return true;
        }
    }

    public DefaultOggSeeker(StreamReader r5, long r6, long r8, long r10, long r12, boolean r14) {
        if (r6 >= 0) goto L5;
    L7:
        boolean r0 = false;
    L8:
        Assertions.checkArgument(r0);
        this.streamReader = r5;
        this.payloadStartPosition = r6;
        this.payloadEndPosition = r8;
        if (r10 == (r8 - r6)) goto L13;
        if (r14 == true) goto L13;
        this.state = 0;
    L14:
        this.pageHeader = new OggPageHeader();
        return;
    L13:
        this.totalGranules = r12;
        this.state = 4;
        goto L14
    L5:
        if (r8 <= r6) goto L7;
        r0 = true;
        goto L8
    }

    static /* synthetic */ StreamReader access$100(DefaultOggSeeker r1) {
        return r1.streamReader;
    }

    static /* synthetic */ long access$200(DefaultOggSeeker r2) {
        return r2.payloadStartPosition;
    }

    static /* synthetic */ long access$300(DefaultOggSeeker r2) {
        return r2.totalGranules;
    }

    static /* synthetic */ long access$400(DefaultOggSeeker r2) {
        return r2.payloadEndPosition;
    }

    private long getNextSeekPosition(ExtractorInput r25) throws IOException {
        if (this.start != this.end) goto L5;
        return -1;
    L5:
        long r5 = r25.getPosition();
        if (this.pageHeader.skipToNextPage(r25, this.end) == false) goto L8;
        this.pageHeader.populate(r25, false);
        r25.resetPeekPosition();
        long r7 = this.targetGranule - this.pageHeader.granulePosition;
        int r2 = this.pageHeader.headerSize + this.pageHeader.bodySize;
        if (0 > r7) goto L19;
        if (r7 >= 72000) goto L19;
        return -1;
    L19:
        if (r7 >= 0) goto L21;
        this.end = r5;
        this.endGranule = this.pageHeader.granulePosition;
    L23:
        if ((this.end - this.start) >= SilenceSkippingAudioProcessor.DEFAULT_MINIMUM_SILENCE_DURATION_US) goto L26;
        this.end = this.start;
        return this.start;
    L26:
        long r3 = r2;
        if (r7 > 0) goto L29;
        long r12 = 2;
    L31:
        return Util.constrainValue((r25.getPosition() - (r3 * r12)) + (((this.end - this.start) * r7) / (this.endGranule - this.startGranule)), this.start, this.end - 1);
    L29:
        r12 = 1;
        goto L31
    L21:
        this.start = r25.getPosition() + ((long) r2);
        this.startGranule = this.pageHeader.granulePosition;
        goto L23
    L8:
        if (this.start == r5) goto L12;
        return this.start;
    L12:
        throw new IOException("No ogg page can be found.");
    }

    private void skipToPageOfTargetGranule(ExtractorInput r5) throws IOException {
    L2:
        this.pageHeader.skipToNextPage(r5);
        this.pageHeader.populate(r5, false);
        if (this.pageHeader.granulePosition > this.targetGranule) goto L4;
        r5.skipFully(this.pageHeader.headerSize + this.pageHeader.bodySize);
        this.start = r5.getPosition();
        this.startGranule = this.pageHeader.granulePosition;
        goto L2
    L4:
        r5.resetPeekPosition();
    }

    @Override // androidx.media3.extractor.ogg.OggSeeker
    public /* bridge */ /* synthetic */ SeekMap createSeekMap() {
        return createSeekMap();
    }

    @Override // androidx.media3.extractor.ogg.OggSeeker
    public OggSeekMap createSeekMap() {
        C04401 r1 = null;
        if (this.totalGranules != 0) goto L5;
        return null;
    L5:
        return new OggSeekMap(this, r1);
    }

    @Override // androidx.media3.extractor.ogg.OggSeeker
    public long read(ExtractorInput r7) throws IOException {
        switch(this.state) {
            case 0: goto L13;
            case 1: goto L16;
            case 2: goto L7;
            case 3: goto L11;
            case 4: goto L6;
            default: goto L5;
        };
    L6:
        return -1;
    L7:
        long r4 = getNextSeekPosition(r7);
        if (r4 == (-1)) goto L10;
        return r4;
    L10:
        this.state = 3;
    L11:
        skipToPageOfTargetGranule(r7);
        this.state = 4;
        return -(this.startGranule + 2);
    L13:
        this.positionBeforeSeekToEnd = r7.getPosition();
        this.state = 1;
        long r0 = this.payloadEndPosition - 65307;
        if (r0 <= this.positionBeforeSeekToEnd) goto L16;
        return r0;
    L16:
        this.totalGranules = readGranuleOfLastPage(r7);
        this.state = 4;
        return this.positionBeforeSeekToEnd;
    L5:
        throw new IllegalStateException();
    }

    long readGranuleOfLastPage(ExtractorInput r7) throws IOException {
        this.pageHeader.reset();
        if (this.pageHeader.skipToNextPage(r7) == false) goto L20;
        this.pageHeader.populate(r7, false);
        r7.skipFully(this.pageHeader.headerSize + this.pageHeader.bodySize);
        long r0 = this.pageHeader.granulePosition;
    L6:
        if ((this.pageHeader.type & 4) == 4) goto L18;
        if (this.pageHeader.skipToNextPage(r7) == false) goto L18;
        if (r7.getPosition() >= this.payloadEndPosition) goto L18;
        if (this.pageHeader.populate(r7, true) == false) goto L17;
        if (ExtractorUtil.skipFullyQuietly(r7, this.pageHeader.headerSize + this.pageHeader.bodySize) == false) goto L17;
        r0 = this.pageHeader.granulePosition;
    L17:
        return r0;
    L18:
        return r0;
    L20:
        throw new EOFException();
    }

    @Override // androidx.media3.extractor.ogg.OggSeeker
    public void startSeek(long r11) {
        this.targetGranule = Util.constrainValue(r11, 0, this.totalGranules - 1);
        this.state = 2;
        this.start = this.payloadStartPosition;
        this.end = this.payloadEndPosition;
        this.startGranule = 0;
        this.endGranule = this.totalGranules;
    }
}
