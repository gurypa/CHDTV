package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.BinarySearchSeeker;
import androidx.media3.extractor.ExtractorInput;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
final class PsBinarySearchSeeker extends BinarySearchSeeker {
    private static final int MINIMUM_SEARCH_RANGE_BYTES = 1000;
    private static final long SEEK_TOLERANCE_US = 100000;
    private static final int TIMESTAMP_SEARCH_BYTES = 20000;

    /* JADX INFO: renamed from: androidx.media3.extractor.ts.PsBinarySearchSeeker$1 */
    static /* synthetic */ class C04491 {
    }

    private static final class PsScrSeeker implements BinarySearchSeeker.TimestampSeeker {
        private final ParsableByteArray packetBuffer;
        private final TimestampAdjuster scrTimestampAdjuster;

        private PsScrSeeker(TimestampAdjuster r2) {
            this.scrTimestampAdjuster = r2;
            this.packetBuffer = new ParsableByteArray();
        }

        /* synthetic */ PsScrSeeker(TimestampAdjuster r1, C04491 r2) {
            this(r1);
        }

        private BinarySearchSeeker.TimestampSearchResult searchForScrValueInBuffer(ParsableByteArray r17, long r18, long r20) {
            int r3 = -1;
            int r4 = -1;
            long r5 = C0180C.TIME_UNSET;
        L4:
            if (r17.bytesLeft() < 4) goto L26;
            if (PsBinarySearchSeeker.access$100(r17.getData(), r17.getPosition()) != 442) goto L7;
            r17.skipBytes(4);
            long r11 = PsDurationReader.readScrValueFromPack(r17);
            if (r11 == C0180C.TIME_UNSET) goto L24;
            long r13 = this.scrTimestampAdjuster.adjustTsTimestamp(r11);
            if (r13 > r18) goto L13;
            if ((100000 + r13) > r18) goto L21;
            r5 = r13;
            r3 = r17.getPosition();
            goto L24
        L21:
            return BinarySearchSeeker.TimestampSearchResult.targetFoundResult(((long) r17.getPosition()) + r20);
        L13:
            if (r5 != C0180C.TIME_UNSET) goto L17;
            return BinarySearchSeeker.TimestampSearchResult.overestimatedResult(r13, r20);
        L17:
            return BinarySearchSeeker.TimestampSearchResult.targetFoundResult(((long) r3) + r20);
        L24:
            skipToEndOfCurrentPack(r17);
            r4 = r17.getPosition();
            goto L4
        L7:
            r17.skipBytes(1);
            goto L4
        L26:
            if (r5 == C0180C.TIME_UNSET) goto L30;
            return BinarySearchSeeker.TimestampSearchResult.underestimatedResult(r5, ((long) r4) + r20);
        L30:
            return BinarySearchSeeker.TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
        }

        private static void skipToEndOfCurrentPack(ParsableByteArray r7) {
            int r0 = r7.limit();
            if (r7.bytesLeft() >= 10) goto L6;
            r7.setPosition(r0);
            return;
        L6:
            r7.skipBytes(9);
            int r1 = r7.readUnsignedByte() & 7;
            if (r7.bytesLeft() >= r1) goto L10;
            r7.setPosition(r0);
            return;
        L10:
            r7.skipBytes(r1);
            if (r7.bytesLeft() >= 4) goto L15;
            r7.setPosition(r0);
            return;
        L15:
            if (PsBinarySearchSeeker.access$100(r7.getData(), r7.getPosition()) != 443) goto L22;
            r7.skipBytes(4);
            int r4 = r7.readUnsignedShort();
            if (r7.bytesLeft() >= r4) goto L20;
            r7.setPosition(r0);
            return;
        L20:
            r7.skipBytes(r4);
        L22:
            if (r7.bytesLeft() < 4) goto L36;
            int r2 = PsBinarySearchSeeker.access$100(r7.getData(), r7.getPosition());
            if (r2 == 442) goto L43;
            if (r2 == 441) goto L44;
            if ((r2 >>> 8) != 1) goto L45;
            r7.skipBytes(4);
            if (r7.bytesLeft() < 2) goto L33;
            int r5 = r7.readUnsignedShort();
            r7.setPosition(Math.min(r7.limit(), r7.getPosition() + r5));
            goto L22
        L33:
            r7.setPosition(r0);
            return;
        L45:
            return;
        L44:
            return;
        L43:
            return;
        }

        @Override // androidx.media3.extractor.BinarySearchSeeker.TimestampSeeker
        public void onSeekFinished() {
            this.packetBuffer.reset(Util.EMPTY_BYTE_ARRAY);
        }

        @Override // androidx.media3.extractor.BinarySearchSeeker.TimestampSeeker
        public BinarySearchSeeker.TimestampSearchResult searchForTimestamp(ExtractorInput r8, long r9) throws IOException {
            long r4 = r8.getPosition();
            int r6 = (int) Math.min(20000, r8.getLength() - r4);
            this.packetBuffer.reset(r6);
            r8.peekFully(this.packetBuffer.getData(), 0, r6);
            return searchForScrValueInBuffer(this.packetBuffer, r9, r4);
        }
    }

    public PsBinarySearchSeeker(TimestampAdjuster r17, long r18, long r20) {
        super(new BinarySearchSeeker.DefaultSeekTimestampConverter(), new PsScrSeeker(r17, null), r18, 0, r18 + 1, 0, r20, 188, 1000);
    }

    static /* synthetic */ int access$100(byte[] r1, int r2) {
        return peekIntAtPosition(r1, r2);
    }

    private static int peekIntAtPosition(byte[] r2, int r3) {
        return ((((r2[r3] & 255) << 24) | ((r2[r3 + 1] & 255) << 16)) | ((r2[r3 + 2] & 255) << 8)) | (r2[r3 + 3] & 255);
    }
}
