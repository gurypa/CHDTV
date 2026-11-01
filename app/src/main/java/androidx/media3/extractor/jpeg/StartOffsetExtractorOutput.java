package androidx.media3.extractor.jpeg;

import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.TrackOutput;

/* JADX INFO: loaded from: classes.dex */
public final class StartOffsetExtractorOutput implements ExtractorOutput {
    private final ExtractorOutput extractorOutput;
    private final long startOffset;

    public StartOffsetExtractorOutput(long r1, ExtractorOutput r3) {
        this.startOffset = r1;
        this.extractorOutput = r3;
    }

    static /* synthetic */ long access$000(StartOffsetExtractorOutput r2) {
        return r2.startOffset;
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public void endTracks() {
        this.extractorOutput.endTracks();
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public void seekMap(final SeekMap r3) {
        this.extractorOutput.seekMap(new C04091(this, r3, r3));
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public TrackOutput track(int r2, int r3) {
        return this.extractorOutput.track(r2, r3);
    }
}
