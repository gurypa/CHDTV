package androidx.media3.extractor.ogg;

import androidx.media3.common.ParserException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.TrackOutput;
import java.io.IOException;
import java.util.List;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;

/* JADX INFO: loaded from: classes.dex */
public class OggExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = null;
    private static final int MAX_VERIFICATION_BYTES = 8;
    private ExtractorOutput output;
    private StreamReader streamReader;
    private boolean streamReaderInitialized;

    static {
        FACTORY = new OggExtractor$$ExternalSyntheticLambda0();
    }

    public OggExtractor() {
    }

    static /* synthetic */ Extractor[] lambda$static$0() {
        return new Extractor[]{new OggExtractor()};
    }

    private static ParsableByteArray resetPosition(ParsableByteArray r1) {
        r1.setPosition(0);
        return r1;
    }

    @EnsuresNonNullIf(expression = {"streamReader"}, result = true)
    private boolean sniffInternal(ExtractorInput r7) throws IOException {
        OggPageHeader r0 = new OggPageHeader();
        if (r0.populate(r7, true) == true) goto L5;
    L18:
        return false;
    L5:
        if ((r0.type & 2) != 2) goto L18;
        int r2 = Math.min(r0.bodySize, 8);
        ParsableByteArray r4 = new ParsableByteArray(r2);
        r7.peekFully(r4.getData(), 0, r2);
        if (FlacReader.verifyBitstreamType(resetPosition(r4)) == false) goto L11;
        this.streamReader = new FlacReader();
    L16:
        return true;
    L11:
        if (VorbisReader.verifyBitstreamType(resetPosition(r4)) == false) goto L14;
        this.streamReader = new VorbisReader();
        goto L16
    L14:
        if (OpusReader.verifyBitstreamType(resetPosition(r4)) == false) goto L17;
        this.streamReader = new OpusReader();
        goto L16
    L17:
        return false;
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
    public void init(ExtractorOutput r1) {
        this.output = r1;
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r5, PositionHolder r6) throws IOException {
        Assertions.checkStateNotNull(this.output);
        if (this.streamReader != null) goto L10;
        if (sniffInternal(r5) == false) goto L8;
        r5.resetPeekPosition();
        goto L10
    L8:
        throw ParserException.createForMalformedContainer("Failed to determine bitstream type", null);
    L10:
        if (this.streamReaderInitialized == true) goto L13;
        TrackOutput r0 = this.output.track(0, 1);
        this.output.endTracks();
        this.streamReader.init(this.output, r0);
        this.streamReaderInitialized = true;
    L13:
        return this.streamReader.read(r5, r6);
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r2, long r4) {
        if (this.streamReader == null) goto L6;
        this.streamReader.seek(r2, r4);
        return;
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r3) throws IOException {
        return sniffInternal(r3);
    L5:
        return false;
    }
}
