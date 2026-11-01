package androidx.media3.extractor.flv;

import androidx.media3.common.ParserException;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.TrackOutput;

/* JADX INFO: loaded from: classes.dex */
abstract class TagPayloadReader {
    protected final TrackOutput output;

    public static final class UnsupportedFormatException extends ParserException {
        public UnsupportedFormatException(String r4) {
            super(r4, null, false, 1);
        }
    }

    protected TagPayloadReader(TrackOutput r1) {
        this.output = r1;
    }

    public final boolean consume(ParsableByteArray r2, long r3) throws ParserException {
        if (parseHeader(r2) == true) goto L5;
    L7:
        return false;
    L5:
        if (parsePayload(r2, r3) == false) goto L7;
        return true;
    }

    protected abstract boolean parseHeader(ParsableByteArray r1) throws ParserException;

    protected abstract boolean parsePayload(ParsableByteArray r1, long r2) throws ParserException;

    public abstract void seek();
}
