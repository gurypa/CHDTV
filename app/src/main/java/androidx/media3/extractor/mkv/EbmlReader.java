package androidx.media3.extractor.mkv;

import androidx.media3.extractor.ExtractorInput;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
interface EbmlReader {
    void init(EbmlProcessor r1);

    boolean read(ExtractorInput r1) throws IOException;

    void reset();
}
