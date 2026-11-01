package androidx.media3.extractor.p001ts;

import androidx.media3.common.ParserException;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.ExtractorOutput;

/* JADX INFO: loaded from: classes.dex */
public interface ElementaryStreamReader {
    void consume(ParsableByteArray r1) throws ParserException;

    void createTracks(ExtractorOutput r1, TsPayloadReader.TrackIdGenerator r2);

    void packetFinished(boolean r1);

    void packetStarted(long r1, int r3);

    void seek();
}
