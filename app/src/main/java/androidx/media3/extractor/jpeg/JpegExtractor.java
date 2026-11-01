package androidx.media3.extractor.jpeg;

import androidx.media3.common.MimeTypes;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SingleSampleExtractor;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class JpegExtractor implements Extractor {
    public static final int FLAG_READ_IMAGE = 1;
    private static final int JPEG_FILE_SIGNATURE = 65496;
    private static final int JPEG_FILE_SIGNATURE_LENGTH = 2;
    private final Extractor extractor;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    public JpegExtractor() {
        this(0);
    }

    public JpegExtractor(int r5) {
        if ((r5 & 1) == 0) goto L5;
        this.extractor = new SingleSampleExtractor(JPEG_FILE_SIGNATURE, 2, MimeTypes.IMAGE_JPEG);
        return;
    L5:
        this.extractor = new JpegMotionPhotoExtractor();
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
        this.extractor.init(r2);
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r2, PositionHolder r3) throws IOException {
        return this.extractor.read(r2, r3);
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
        this.extractor.release();
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r2, long r4) {
        this.extractor.seek(r2, r4);
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r2) throws IOException {
        return this.extractor.sniff(r2);
    }
}
