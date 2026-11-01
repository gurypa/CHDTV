package androidx.media3.extractor;

import androidx.media3.common.DataReader;
import androidx.media3.common.Format;
import androidx.media3.common.util.ParsableByteArray;
import java.io.EOFException;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class DiscardingTrackOutput implements TrackOutput {
    private final byte[] readBuffer;

    public DiscardingTrackOutput() {
        this.readBuffer = new byte[4096];
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void format(Format r1) {
    }

    @Override // androidx.media3.extractor.TrackOutput
    public /* synthetic */ int sampleData(DataReader r1, int r2, boolean r3) {
        return TrackOutput.CC.$default$sampleData(this, r1, r2, r3);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public int sampleData(DataReader r4, int r5, boolean r6, int r7) throws IOException {
        int r0 = Math.min(this.readBuffer.length, r5);
        int r1 = r4.read(this.readBuffer, 0, r0);
        if (r1 != (-1)) goto L8;
        if (r6 == false) goto L7;
        return -1;
    L7:
        throw new EOFException();
    L8:
        return r1;
    }

    @Override // androidx.media3.extractor.TrackOutput
    public /* synthetic */ void sampleData(ParsableByteArray r1, int r2) {
        TrackOutput.CC.$default$sampleData(this, r1, r2);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void sampleData(ParsableByteArray r1, int r2, int r3) {
        r1.skipBytes(r2);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void sampleMetadata(long r1, int r3, int r4, int r5, TrackOutput.CryptoData r6) {
    }
}
