package androidx.media3.exoplayer.hls;

import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public interface HlsMediaChunkExtractor {
    void init(ExtractorOutput r1);

    boolean isPackedAudioExtractor();

    boolean isReusable();

    void onTruncatedSegmentParsed();

    boolean read(ExtractorInput r1) throws IOException;

    HlsMediaChunkExtractor recreate();
}
