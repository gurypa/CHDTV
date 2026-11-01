package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.BinarySearchSeeker;
import androidx.media3.extractor.ExtractorInput;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
final class TsBinarySearchSeeker extends BinarySearchSeeker {
    private static final int MINIMUM_SEARCH_RANGE_BYTES = 940;
    private static final long SEEK_TOLERANCE_US = 100000;

    private static final class TsPcrSeeker implements BinarySearchSeeker.TimestampSeeker {
        private final ParsableByteArray packetBuffer;
        private final int pcrPid;
        private final TimestampAdjuster pcrTimestampAdjuster;
        private final int timestampSearchBytes;

        public TsPcrSeeker(int r2, TimestampAdjuster r3, int r4) {
            this.pcrPid = r2;
            this.pcrTimestampAdjuster = r3;
            this.timestampSearchBytes = r4;
            this.packetBuffer = new ParsableByteArray();
        }

        private BinarySearchSeeker.TimestampSearchResult searchForPcrValueInBuffer(ParsableByteArray r22, long r23, long r25) {
            int r4 = r22.limit();
            long r5 = -1;
            long r7 = -1;
            long r9 = C0180C.TIME_UNSET;
        L4:
            if (r22.bytesLeft() < 188) goto L25;
            int r11 = TsUtil.findSyncBytePosition(r22.getData(), r22.getPosition(), r4);
            int r12 = r11 + TsExtractor.TS_PACKET_SIZE;
            if (r12 > r4) goto L7;
            long r13 = TsUtil.readPcrFromPacket(r22, r11, this.pcrPid);
            if (r13 == C0180C.TIME_UNSET) goto L24;
            long r19 = r5;
            long r6 = this.pcrTimestampAdjuster.adjustTsTimestamp(r13);
            if (r6 > r23) goto L13;
            if ((100000 + r6) > r23) goto L21;
            r5 = r11;
            r9 = r6;
            goto L24
        L21:
            return BinarySearchSeeker.TimestampSearchResult.targetFoundResult(r25 + ((long) r11));
        L13:
            if (r9 != C0180C.TIME_UNSET) goto L17;
            return BinarySearchSeeker.TimestampSearchResult.overestimatedResult(r6, r25);
        L17:
            return BinarySearchSeeker.TimestampSearchResult.targetFoundResult(r25 + r19);
        L24:
            r22.setPosition(r12);
            r7 = r12;
            r4 = r4;
            goto L4
        L7:
            long r16 = C0180C.TIME_UNSET;
        L27:
            if (r9 == r16) goto L31;
            return BinarySearchSeeker.TimestampSearchResult.underestimatedResult(r9, r25 + r7);
        L31:
            return BinarySearchSeeker.TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
        L25:
            r16 = C0180C.TIME_UNSET;
            goto L27
        }

        @Override // androidx.media3.extractor.BinarySearchSeeker.TimestampSeeker
        public void onSeekFinished() {
            this.packetBuffer.reset(Util.EMPTY_BYTE_ARRAY);
        }

        @Override // androidx.media3.extractor.BinarySearchSeeker.TimestampSeeker
        public BinarySearchSeeker.TimestampSearchResult searchForTimestamp(ExtractorInput r8, long r9) throws IOException {
            long r4 = r8.getPosition();
            int r6 = (int) Math.min(this.timestampSearchBytes, r8.getLength() - r4);
            this.packetBuffer.reset(r6);
            r8.peekFully(this.packetBuffer.getData(), 0, r6);
            return searchForPcrValueInBuffer(this.packetBuffer, r9, r4);
        }
    }

    public TsBinarySearchSeeker(TimestampAdjuster r17, long r18, long r20, int r22, int r23) {
        super(new BinarySearchSeeker.DefaultSeekTimestampConverter(), new TsPcrSeeker(r22, r17, r23), r18, 0, r18 + 1, 0, r20, 188, MINIMUM_SEARCH_RANGE_BYTES);
    }
}
