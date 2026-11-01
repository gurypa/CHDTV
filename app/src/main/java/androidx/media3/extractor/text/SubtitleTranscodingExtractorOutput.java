package androidx.media3.extractor.text;

import android.util.SparseArray;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.TrackOutput;

/* JADX INFO: loaded from: classes.dex */
public final class SubtitleTranscodingExtractorOutput implements ExtractorOutput {
    private final ExtractorOutput delegate;
    private final SubtitleParser.Factory subtitleParserFactory;
    private final SparseArray<SubtitleTranscodingTrackOutput> textTrackOutputs;

    public SubtitleTranscodingExtractorOutput(ExtractorOutput r2, SubtitleParser.Factory r3) {
        this.delegate = r2;
        this.subtitleParserFactory = r3;
        this.textTrackOutputs = new SparseArray();
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public void endTracks() {
        this.delegate.endTracks();
    }

    public void resetSubtitleParsers() {
        int r0 = 0;
    L4:
        if (r0 >= this.textTrackOutputs.size()) goto L6;
        this.textTrackOutputs.valueAt(r0).resetSubtitleParser();
        r0 = r0 + 1;
        goto L4
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public void seekMap(SeekMap r2) {
        this.delegate.seekMap(r2);
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public TrackOutput track(int r5, int r6) {
        if (r6 != 3) goto L5;
        SubtitleTranscodingTrackOutput r0 = this.textTrackOutputs.get(r5);
        if (r0 == null) goto L9;
        return r0;
    L9:
        SubtitleTranscodingTrackOutput r1 = new SubtitleTranscodingTrackOutput(this.delegate.track(r5, r6), this.subtitleParserFactory);
        this.textTrackOutputs.put(r5, r1);
        return r1;
    L5:
        return this.delegate.track(r5, r6);
    }
}
