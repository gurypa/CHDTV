package androidx.media3.extractor.heif;

import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SingleSampleExtractor;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class HeifExtractor implements Extractor {
    private static final int FILE_SIGNATURE_SEGMENT_LENGTH = 4;
    private static final int HEIF_FILE_SIGNATURE_PART_1 = 1718909296;
    private static final int HEIF_FILE_SIGNATURE_PART_2 = 1751476579;
    private final SingleSampleExtractor imageExtractor;
    private final ParsableByteArray scratch;

    public HeifExtractor() {
        this.scratch = new ParsableByteArray(4);
        this.imageExtractor = new SingleSampleExtractor(-1, -1, MimeTypes.IMAGE_HEIF);
    }

    private boolean readAndCompareFourBytes(ExtractorInput r6, int r7) throws IOException {
        this.scratch.reset(4);
        r6.peekFully(this.scratch.getData(), 0, 4);
        if (this.scratch.readUnsignedInt() != r7) goto L6;
        return true;
    L6:
        return false;
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
    public void init(ExtractorOutput r2) {
        this.imageExtractor.init(r2);
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r2, PositionHolder r3) throws IOException {
        return this.imageExtractor.read(r2, r3);
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r2, long r4) {
        this.imageExtractor.seek(r2, r4);
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r2) throws IOException {
        r2.advancePeekPosition(4);
        if (readAndCompareFourBytes(r2, 1718909296) == true) goto L5;
    L7:
        return false;
    L5:
        if (readAndCompareFourBytes(r2, 1751476579) == false) goto L7;
        return true;
    }
}
