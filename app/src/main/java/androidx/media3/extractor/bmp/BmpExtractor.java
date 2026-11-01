package androidx.media3.extractor.bmp;

import androidx.media3.common.MimeTypes;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SingleSampleExtractor;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class BmpExtractor implements Extractor {
    private static final int BMP_FILE_SIGNATURE = 16973;
    private static final int BMP_FILE_SIGNATURE_LENGTH = 2;
    private final SingleSampleExtractor imageExtractor;

    public BmpExtractor() {
        this.imageExtractor = new SingleSampleExtractor(BMP_FILE_SIGNATURE, 2, MimeTypes.IMAGE_BMP);
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
        return this.imageExtractor.sniff(r2);
    }
}
