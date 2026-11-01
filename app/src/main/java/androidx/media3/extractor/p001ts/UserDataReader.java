package androidx.media3.extractor.p001ts;

import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.CeaUtil;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class UserDataReader {
    private static final int USER_DATA_START_CODE = 434;
    private final List<Format> closedCaptionFormats;
    private final TrackOutput[] outputs;

    public UserDataReader(List<Format> r2) {
        this.closedCaptionFormats = r2;
        this.outputs = new TrackOutput[r2.size()];
    }

    public void consume(long r5, ParsableByteArray r7) {
        if (r7.bytesLeft() >= 9) goto L5;
        return;
    L5:
        int r0 = r7.readInt();
        int r1 = r7.readInt();
        int r2 = r7.readUnsignedByte();
        if (r0 == USER_DATA_START_CODE) goto L8;
        return;
    L8:
        if (r1 == 1195456820) goto L10;
        return;
    L10:
        if (r2 != 3) goto L15;
        CeaUtil.consumeCcData(r5, r7, this.outputs);
        return;
    }

    public void createTracks(ExtractorOutput r8, TsPayloadReader.TrackIdGenerator r9) {
        int r0 = 0;
    L4:
        if (r0 >= this.outputs.length) goto L13;
        r9.generateNewId();
        TrackOutput r1 = r8.track(r9.getTrackId(), 3);
        Format r2 = this.closedCaptionFormats.get(r0);
        String r3 = r2.sampleMimeType;
        if (MimeTypes.APPLICATION_CEA608.equals(r3) == false) goto L8;
    L11:
        boolean r4 = true;
    L12:
        Assertions.checkArgument(r4, "Invalid closed caption MIME type provided: " + r3);
        r1.format(new Format.Builder().setId(r9.getFormatId()).setSampleMimeType(r3).setSelectionFlags(r2.selectionFlags).setLanguage(r2.language).setAccessibilityChannel(r2.accessibilityChannel).setInitializationData(r2.initializationData).build());
        this.outputs[r0] = r1;
        r0 = r0 + 1;
        goto L4
    L8:
        if (MimeTypes.APPLICATION_CEA708.equals(r3) == true) goto L11;
        r4 = false;
        goto L12
    }
}
