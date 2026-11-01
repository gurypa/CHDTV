package androidx.media3.extractor;

import androidx.media3.common.Metadata;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.metadata.id3.Id3Decoder;
import java.io.EOFException;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class Id3Peeker {
    private final ParsableByteArray scratch;

    public Id3Peeker() {
        this.scratch = new ParsableByteArray(10);
    }

    public Metadata peekId3Data(ExtractorInput r9, Id3Decoder.FramePredicate r10) throws IOException {
        int r0 = 0;
        Metadata r1 = null;
    L15:
        r9.peekFully(this.scratch.getData(), 0, 10);     // Catch: EOFException -> L12
        this.scratch.setPosition(0);
        if (this.scratch.readUnsignedInt24() != 4801587) goto L13;
        this.scratch.skipBytes(3);
        int r2 = this.scratch.readSynchSafeInt();
        int r5 = r2 + 10;
        if (r1 != null) goto L10;
        byte[] r6 = new byte[r5];
        System.arraycopy(this.scratch.getData(), 0, r6, 0, 10);
        r9.peekFully(r6, 10, r2);
        r1 = new Id3Decoder(r10).decode(r6, r5);
    L11:
        r0 = r0 + r5;
        goto L15
    L10:
        r9.advancePeekPosition(r2);
    L13:
        r9.resetPeekPosition();
        r9.advancePeekPosition(r0);
        return r1;
    }
}
