package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;

/* JADX INFO: loaded from: classes.dex */
public final class Id3Reader implements ElementaryStreamReader {
    private static final String TAG = "Id3Reader";
    private final ParsableByteArray id3Header;
    private TrackOutput output;
    private int sampleBytesRead;
    private int sampleSize;
    private long sampleTimeUs;
    private boolean writingSample;

    public Id3Reader() {
        this.id3Header = new ParsableByteArray(10);
        this.sampleTimeUs = C0180C.TIME_UNSET;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void consume(ParsableByteArray r8) {
        Assertions.checkStateNotNull(this.output);
        if (this.writingSample == true) goto L5;
        return;
    L5:
        int r0 = r8.bytesLeft();
        if (this.sampleBytesRead >= 10) goto L19;
        int r1 = Math.min(r0, 10 - this.sampleBytesRead);
        System.arraycopy(r8.getData(), r8.getPosition(), this.id3Header.getData(), this.sampleBytesRead, r1);
        if ((this.sampleBytesRead + r1) != 10) goto L19;
        this.id3Header.setPosition(0);
        if (73 == this.id3Header.readUnsignedByte()) goto L12;
    L17:
        Log.m48w(TAG, "Discarding invalid ID3 tag");
        this.writingSample = false;
        return;
    L12:
        if (68 != this.id3Header.readUnsignedByte()) goto L17;
        if (51 != this.id3Header.readUnsignedByte()) goto L17;
        this.id3Header.skipBytes(3);
        this.sampleSize = this.id3Header.readSynchSafeInt() + 10;
    L19:
        int r2 = Math.min(r0, this.sampleSize - this.sampleBytesRead);
        this.output.sampleData(r8, r2);
        this.sampleBytesRead += r2;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void createTracks(ExtractorOutput r4, TsPayloadReader.TrackIdGenerator r5) {
        r5.generateNewId();
        this.output = r4.track(r5.getTrackId(), 5);
        this.output.format(new Format.Builder().setId(r5.getFormatId()).setSampleMimeType(MimeTypes.APPLICATION_ID3).build());
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void packetFinished(boolean r10) {
        Assertions.checkStateNotNull(this.output);
        if (this.writingSample == true) goto L5;
        return;
    L5:
        if (this.sampleSize != 0) goto L7;
        return;
    L7:
        if (this.sampleBytesRead == this.sampleSize) goto L10;
        return;
    L10:
        if (this.sampleTimeUs == C0180C.TIME_UNSET) goto L12;
        boolean r0 = true;
    L13:
        Assertions.checkState(r0);
        this.output.sampleMetadata(this.sampleTimeUs, 1, this.sampleSize, 0, null);
        this.writingSample = false;
        return;
    L12:
        r0 = false;
        goto L13
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void packetStarted(long r2, int r4) {
        if ((r4 & 4) != 0) goto L5;
        return;
    L5:
        this.writingSample = true;
        this.sampleTimeUs = r2;
        this.sampleSize = 0;
        this.sampleBytesRead = 0;
    }

    @Override // androidx.media3.extractor.p001ts.ElementaryStreamReader
    public void seek() {
        this.writingSample = false;
        this.sampleTimeUs = C0180C.TIME_UNSET;
    }
}
