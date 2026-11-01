package androidx.media3.extractor.text;

import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.PositionHolder;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class SubtitleTranscodingExtractor implements Extractor {
    private final Extractor delegate;
    private final SubtitleParser.Factory subtitleParserFactory;
    private SubtitleTranscodingExtractorOutput transcodingExtractorOutput;

    public SubtitleTranscodingExtractor(Extractor r1, SubtitleParser.Factory r2) {
        this.delegate = r1;
        this.subtitleParserFactory = r2;
    }

    @Override // androidx.media3.extractor.Extractor
    public /* synthetic */ List getSniffFailureDetails() {
        return Extractor.CC.$default$getSniffFailureDetails(this);
    }

    @Override // androidx.media3.extractor.Extractor
    public Extractor getUnderlyingImplementation() {
        return this.delegate;
    }

    @Override // androidx.media3.extractor.Extractor
    public void init(ExtractorOutput r3) {
        this.transcodingExtractorOutput = new SubtitleTranscodingExtractorOutput(r3, this.subtitleParserFactory);
        this.delegate.init(this.transcodingExtractorOutput);
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r2, PositionHolder r3) throws IOException {
        return this.delegate.read(r2, r3);
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
        this.delegate.release();
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r2, long r4) {
        if (this.transcodingExtractorOutput == null) goto L5;
        this.transcodingExtractorOutput.resetSubtitleParsers();
    L5:
        this.delegate.seek(r2, r4);
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r2) throws IOException {
        return this.delegate.sniff(r2);
    }
}
