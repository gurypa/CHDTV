package androidx.media3.extractor;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import java.io.IOException;
import java.util.List;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class SingleSampleExtractor implements Extractor {
    private static final int FIXED_READ_LENGTH = 1024;
    public static final int IMAGE_TRACK_ID = 1024;
    private static final int STATE_ENDED = 2;
    private static final int STATE_READING = 1;
    private ExtractorOutput extractorOutput;
    private final int fileSignature;
    private final int fileSignatureLength;
    private final String sampleMimeType;
    private int size;
    private int state;
    private TrackOutput trackOutput;

    public SingleSampleExtractor(int r1, int r2, String r3) {
        this.fileSignature = r1;
        this.fileSignatureLength = r2;
        this.sampleMimeType = r3;
    }

    @RequiresNonNull({"this.extractorOutput"})
    private void outputImageTrackAndSeekMap(String r5) {
        this.trackOutput = this.extractorOutput.track(1024, 4);
        this.trackOutput.format(new Format.Builder().setSampleMimeType(r5).build());
        this.extractorOutput.endTracks();
        this.extractorOutput.seekMap(new SingleSampleSeekMap(C0180C.TIME_UNSET));
        this.state = 1;
    }

    private void readSegment(ExtractorInput r10) throws IOException {
        int r0 = ((TrackOutput) Assertions.checkNotNull(this.trackOutput)).sampleData(r10, 1024, true);
        if (r0 != (-1)) goto L5;
        this.state = 2;
        this.trackOutput.sampleMetadata(0, 1, this.size, 0, null);
        this.size = 0;
        return;
    L5:
        this.size += r0;
    }

    @Override // androidx.media3.extractor.Extractor
    public /* synthetic */ List getSniffFailureDetails() {
        return Extractor.CC.$default$getSniffFailureDetails(this);
    }

    @Override // androidx.media3.extractor.Extractor
    public /* synthetic */ Extractor getUnderlyingImplementation() {
        return Extractor.CC.$default$getUnderlyingImplementation(this);
    }

    @Override // androidx.media3.extractor.Extractor
    public void init(ExtractorOutput r2) {
        this.extractorOutput = r2;
        outputImageTrackAndSeekMap(this.sampleMimeType);
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r2, PositionHolder r3) throws IOException {
        switch(this.state) {
            case 1: goto L8;
            case 2: goto L6;
            default: goto L5;
        };
    L6:
        return -1;
    L8:
        readSegment(r2);
        return 0;
    L5:
        throw new IllegalStateException();
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r3, long r5) {
        if (r3 != 0) goto L5;
    L6:
        this.state = 1;
        this.size = 0;
        return;
    L5:
        if (this.state == 1) goto L6;
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r6) throws IOException {
        if (this.fileSignature != (-1)) goto L5;
    L7:
        boolean r0 = false;
    L8:
        Assertions.checkState(r0);
        ParsableByteArray r1 = new ParsableByteArray(this.fileSignatureLength);
        r6.peekFully(r1.getData(), 0, this.fileSignatureLength);
        if (r1.readUnsignedShort() != this.fileSignature) goto L12;
        return true;
    L12:
        return false;
    L5:
        if (this.fileSignatureLength == (-1)) goto L7;
        r0 = true;
        goto L8
    }
}
