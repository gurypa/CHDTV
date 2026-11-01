package androidx.media3.extractor.p001ts;

import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.container.ReorderingSeiMessageQueue;
import androidx.media3.extractor.CeaUtil;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class SeiReader {
    private final List<Format> closedCaptionFormats;
    private final TrackOutput[] outputs;
    private final ReorderingSeiMessageQueue reorderingSeiMessageQueue;

    public SeiReader(List<Format> r3) {
        this.closedCaptionFormats = r3;
        this.outputs = new TrackOutput[r3.size()];
        this.reorderingSeiMessageQueue = new ReorderingSeiMessageQueue(new SeiReader$$ExternalSyntheticLambda0(this));
    }

    public void consume(long r2, ParsableByteArray r4) {
        this.reorderingSeiMessageQueue.add(r2, r4);
    }

    public void createTracks(ExtractorOutput r8, TsPayloadReader.TrackIdGenerator r9) {
        int r0 = 0;
    L4:
        if (r0 >= this.outputs.length) goto L17;
        r9.generateNewId();
        TrackOutput r1 = r8.track(r9.getTrackId(), 3);
        Format r2 = this.closedCaptionFormats.get(r0);
        String r3 = r2.sampleMimeType;
        if (MimeTypes.APPLICATION_CEA608.equals(r3) == false) goto L8;
    L11:
        boolean r4 = true;
    L12:
        Assertions.checkArgument(r4, "Invalid closed caption MIME type provided: " + r3);
        if (r2.f26id == null) goto L15;
        String r5 = r2.f26id;
    L16:
        r1.format(new Format.Builder().setId(r5).setSampleMimeType(r3).setSelectionFlags(r2.selectionFlags).setLanguage(r2.language).setAccessibilityChannel(r2.accessibilityChannel).setInitializationData(r2.initializationData).build());
        this.outputs[r0] = r1;
        r0 = r0 + 1;
        goto L4
    L15:
        r5 = r9.getFormatId();
        goto L16
    L8:
        if (MimeTypes.APPLICATION_CEA708.equals(r3) == true) goto L11;
        r4 = false;
        goto L12
    }

    public void flush() {
        this.reorderingSeiMessageQueue.flush();
    }

    /* JADX INFO: renamed from: lambda$new$0$androidx-media3-extractor-ts-SeiReader, reason: not valid java name */
    /* synthetic */ void m483lambda$new$0$androidxmedia3extractortsSeiReader(long r2, ParsableByteArray r4) {
        CeaUtil.consume(r2, r4, this.outputs);
    }

    public void setReorderingQueueSize(int r2) {
        this.reorderingSeiMessageQueue.setMaxSize(r2);
    }
}
