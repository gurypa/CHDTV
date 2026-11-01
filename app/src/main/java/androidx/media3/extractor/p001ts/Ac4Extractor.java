package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.Ac4Util;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class Ac4Extractor implements Extractor {
    public static final ExtractorsFactory FACTORY = null;
    private static final int FRAME_HEADER_SIZE = 7;
    private static final int MAX_SNIFF_BYTES = 8192;
    private static final int READ_BUFFER_SIZE = 16384;
    private final Ac4Reader reader;
    private final ParsableByteArray sampleData;
    private boolean startedPacket;

    static {
        FACTORY = new Ac4Extractor$$ExternalSyntheticLambda0();
    }

    public Ac4Extractor() {
        this.reader = new Ac4Reader();
        this.sampleData = new ParsableByteArray(16384);
    }

    static /* synthetic */ Extractor[] lambda$static$0() {
        return new Extractor[]{new Ac4Extractor()};
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
    public void init(ExtractorOutput r5) {
        this.reader.createTracks(r5, new TsPayloadReader.TrackIdGenerator(0, 1));
        r5.endTracks();
        r5.seekMap(new SeekMap.Unseekable(C0180C.TIME_UNSET));
    }

    @Override // androidx.media3.extractor.Extractor
    public int read(ExtractorInput r7, PositionHolder r8) throws IOException {
        int r0 = r7.read(this.sampleData.getData(), 0, 16384);
        if (r0 != (-1)) goto L5;
        return -1;
    L5:
        this.sampleData.setPosition(0);
        this.sampleData.setLimit(r0);
        if (this.startedPacket == true) goto L8;
        this.reader.packetStarted(0, 4);
        this.startedPacket = true;
    L8:
        this.reader.consume(this.sampleData);
        return 0;
    }

    @Override // androidx.media3.extractor.Extractor
    public void release() {
    }

    @Override // androidx.media3.extractor.Extractor
    public void seek(long r2, long r4) {
        this.startedPacket = false;
        this.reader.seek();
    }

    @Override // androidx.media3.extractor.Extractor
    public boolean sniff(ExtractorInput r9) throws IOException {
        ParsableByteArray r0 = new ParsableByteArray(10);
        int r2 = 0;
    L3:
        r9.peekFully(r0.getData(), 0, 10);
        r0.setPosition(0);
        if (r0.readUnsignedInt24() != 4801587) goto L5;
        r0.skipBytes(3);
        int r3 = r0.readSynchSafeInt();
        r2 = r2 + (r3 + 10);
        r9.advancePeekPosition(r3);
        goto L3
    L5:
        r9.resetPeekPosition();
        r9.advancePeekPosition(r2);
        int r1 = r2;
        int r4 = 0;
    L6:
        r9.peekFully(r0.getData(), 0, 7);
        r0.setPosition(0);
        int r5 = r0.readUnsignedShort();
        if (r5 == 44096) goto L14;
        if (r5 == 44097) goto L14;
        r4 = 0;
        r9.resetPeekPosition();
        r1 = r1 + 1;
        if ((r1 - r2) >= 8192) goto L12;
        r9.advancePeekPosition(r1);
        goto L6
    L12:
        return false;
    L14:
        r4 = r4 + 1;
        if (r4 >= 4) goto L16;
        int r6 = Ac4Util.parseAc4SyncframeSize(r0.getData(), r5);
        if (r6 == (-1)) goto L20;
        r9.advancePeekPosition(r6 - 7);
        goto L6
    L20:
        return false;
    L16:
        return true;
    }
}
