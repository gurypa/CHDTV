package androidx.media3.extractor.flac;

import androidx.media3.extractor.BinarySearchSeeker;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.FlacFrameReader;
import androidx.media3.extractor.FlacStreamMetadata;
import java.io.IOException;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
final class FlacBinarySearchSeeker extends BinarySearchSeeker {

    /* JADX INFO: renamed from: androidx.media3.extractor.flac.FlacBinarySearchSeeker$1 */
    static /* synthetic */ class C04081 {
    }

    private static final class FlacTimestampSeeker implements BinarySearchSeeker.TimestampSeeker {
        private final FlacStreamMetadata flacStreamMetadata;
        private final int frameStartMarker;
        private final FlacFrameReader.SampleNumberHolder sampleNumberHolder;

        private FlacTimestampSeeker(FlacStreamMetadata r2, int r3) {
            this.flacStreamMetadata = r2;
            this.frameStartMarker = r3;
            this.sampleNumberHolder = new FlacFrameReader.SampleNumberHolder();
        }

        /* synthetic */ FlacTimestampSeeker(FlacStreamMetadata r1, int r2, C04081 r3) {
            this(r1, r2);
        }

        private long findNextFrame(ExtractorInput r7) throws IOException {
        L3:
            if (r7.getPeekPosition() >= (r7.getLength() - 6)) goto L8;
            if (FlacFrameReader.checkFrameHeaderFromPeek(r7, this.flacStreamMetadata, this.frameStartMarker, this.sampleNumberHolder) == true) goto L8;
            r7.advancePeekPosition(1);
        L8:
            if (r7.getPeekPosition() < (r7.getLength() - 6)) goto L12;
            r7.advancePeekPosition((int) (r7.getLength() - r7.getPeekPosition()));
            return this.flacStreamMetadata.totalSamples;
        L12:
            return this.sampleNumberHolder.sampleNumber;
        }

        @Override // androidx.media3.extractor.BinarySearchSeeker.TimestampSeeker
        public /* synthetic */ void onSeekFinished() {
            BinarySearchSeeker.TimestampSeeker.CC.$default$onSeekFinished(this);
        }

        @Override // androidx.media3.extractor.BinarySearchSeeker.TimestampSeeker
        public BinarySearchSeeker.TimestampSearchResult searchForTimestamp(ExtractorInput r12, long r13) throws IOException {
            long r0 = r12.getPosition();
            long r2 = findNextFrame(r12);
            long r4 = r12.getPeekPosition();
            r12.advancePeekPosition(Math.max(6, this.flacStreamMetadata.minFrameSize));
            long r6 = findNextFrame(r12);
            long r8 = r12.getPeekPosition();
            if (r2 > r13) goto L9;
            if (r6 <= r13) goto L9;
            return BinarySearchSeeker.TimestampSearchResult.targetFoundResult(r4);
        L9:
            if (r6 > r13) goto L13;
            return BinarySearchSeeker.TimestampSearchResult.underestimatedResult(r6, r8);
        L13:
            return BinarySearchSeeker.TimestampSearchResult.overestimatedResult(r2, r0);
        }
    }

    public FlacBinarySearchSeeker(final FlacStreamMetadata r18, int r19, long r20, long r22) {
        Objects.requireNonNull(r18);
        super(new FlacBinarySearchSeeker$$ExternalSyntheticLambda0(r18), new FlacTimestampSeeker(r18, r19, null), r18.getDurationUs(), 0, r18.totalSamples, r20, r22, r18.getApproxBytesPerFrame(), Math.max(6, r18.minFrameSize));
    }
}
