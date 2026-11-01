package androidx.media3.extractor;

import androidx.media3.common.DataReader;
import androidx.media3.common.Format;
import androidx.media3.common.util.ParsableByteArray;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class DummyTrackOutput implements TrackOutput {
    private final DiscardingTrackOutput discardingTrackOutput;

    public DummyTrackOutput() {
        this.discardingTrackOutput = new DiscardingTrackOutput();
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void format(Format r2) {
        this.discardingTrackOutput.format(r2);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public int sampleData(DataReader r2, int r3, boolean r4) throws IOException {
        return this.discardingTrackOutput.sampleData(r2, r3, r4);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public int sampleData(DataReader r2, int r3, boolean r4, int r5) throws IOException {
        return this.discardingTrackOutput.sampleData(r2, r3, r4, r5);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void sampleData(ParsableByteArray r2, int r3) {
        this.discardingTrackOutput.sampleData(r2, r3);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void sampleData(ParsableByteArray r2, int r3, int r4) {
        this.discardingTrackOutput.sampleData(r2, r3, r4);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void sampleMetadata(long r8, int r10, int r11, int r12, TrackOutput.CryptoData r13) {
        this.discardingTrackOutput.sampleMetadata(r8, r10, r11, r12, r13);
    }
}
