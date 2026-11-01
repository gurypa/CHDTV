package androidx.media3.extractor.webp;

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
public final class WebpExtractor implements Extractor {
    private static final int FILE_SIGNATURE_SEGMENT_LENGTH = 4;
    private static final int RIFF_FILE_SIGNATURE = 1380533830;
    private static final int WEBP_FILE_SIGNATURE = 1464156752;
    private final SingleSampleExtractor imageExtractor;
    private final ParsableByteArray scratch;

    public WebpExtractor() {
        this.scratch = new ParsableByteArray(4);
        this.imageExtractor = new SingleSampleExtractor(-1, -1, MimeTypes.IMAGE_WEBP);
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
    public boolean sniff(ExtractorInput r8) throws IOException {
        this.scratch.reset(4);
        r8.peekFully(this.scratch.getData(), 0, 4);
        if (this.scratch.readUnsignedInt() == 1380533830) goto L5;
        return false;
    L5:
        r8.advancePeekPosition(4);
        this.scratch.reset(4);
        r8.peekFully(this.scratch.getData(), 0, 4);
        if (this.scratch.readUnsignedInt() != 1464156752) goto L9;
        return true;
    L9:
        return false;
    }
}
