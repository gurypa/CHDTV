package androidx.media3.extractor.ogg;

import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.SeekMap;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
interface OggSeeker {
    SeekMap createSeekMap();

    long read(ExtractorInput r1) throws IOException;

    void startSeek(long r1);
}
