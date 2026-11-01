package androidx.media3.extractor;

/* JADX INFO: loaded from: classes.dex */
public final class NoOpExtractorOutput implements ExtractorOutput {
    public NoOpExtractorOutput() {
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public void endTracks() {
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public void seekMap(SeekMap r1) {
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public TrackOutput track(int r2, int r3) {
        return new DiscardingTrackOutput();
    }
}
