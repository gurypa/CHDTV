package androidx.media3.extractor.p001ts;

import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.extractor.ExtractorOutput;

/* JADX INFO: loaded from: classes.dex */
public interface SectionPayloadReader {
    void consume(ParsableByteArray r1);

    void init(TimestampAdjuster r1, ExtractorOutput r2, TsPayloadReader.TrackIdGenerator r3);
}
