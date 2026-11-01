package androidx.media3.extractor;

import androidx.media3.common.DataReader;
import androidx.media3.common.Format;
import androidx.media3.common.util.ParsableByteArray;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class ForwardingTrackOutput implements TrackOutput {
    private final TrackOutput trackOutput;

    public ForwardingTrackOutput(TrackOutput r1) {
        this.trackOutput = r1;
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void format(Format r2) {
        this.trackOutput.format(r2);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public int sampleData(DataReader r2, int r3, boolean r4) throws IOException {
        return this.trackOutput.sampleData(r2, r3, r4);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public int sampleData(DataReader r2, int r3, boolean r4, int r5) throws IOException {
        return this.trackOutput.sampleData(r2, r3, r4, r5);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void sampleData(ParsableByteArray r2, int r3) {
        this.trackOutput.sampleData(r2, r3);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void sampleData(ParsableByteArray r2, int r3, int r4) {
        this.trackOutput.sampleData(r2, r3, r4);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void sampleMetadata(long r8, int r10, int r11, int r12, TrackOutput.CryptoData r13) {
        this.trackOutput.sampleMetadata(r8, r10, r11, r12, r13);
    }
}
