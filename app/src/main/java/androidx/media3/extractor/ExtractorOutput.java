package androidx.media3.extractor;

/* JADX INFO: loaded from: classes.dex */
public interface ExtractorOutput {
    public static final ExtractorOutput PLACEHOLDER = null;

    static {
        PLACEHOLDER = new C04051();
    }

    void endTracks();

    void seekMap(SeekMap r1);

    TrackOutput track(int r1, int r2);
}
