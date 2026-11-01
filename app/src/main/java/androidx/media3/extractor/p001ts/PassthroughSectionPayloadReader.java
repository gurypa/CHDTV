package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class PassthroughSectionPayloadReader implements SectionPayloadReader {
    private Format format;
    private TrackOutput output;
    private TimestampAdjuster timestampAdjuster;

    public PassthroughSectionPayloadReader(String r2) {
        this.format = new Format.Builder().setSampleMimeType(r2).build();
    }

    @EnsuresNonNull({"timestampAdjuster", "output"})
    private void assertInitialized() {
        Assertions.checkStateNotNull(this.timestampAdjuster);
        Util.castNonNull(this.output);
    }

    @Override // androidx.media3.extractor.p001ts.SectionPayloadReader
    public void consume(ParsableByteArray r11) {
        assertInitialized();
        long r2 = this.timestampAdjuster.getLastAdjustedTimestampUs();
        long r8 = this.timestampAdjuster.getTimestampOffsetUs();
        if (r2 != C0180C.TIME_UNSET) goto L5;
        return;
    L5:
        if (r8 != C0180C.TIME_UNSET) goto L8;
        return;
    L8:
        if (r8 == this.format.subsampleOffsetUs) goto L10;
        this.format = this.format.buildUpon().setSubsampleOffsetUs(r8).build();
        this.output.format(this.format);
    L10:
        int r5 = r11.bytesLeft();
        this.output.sampleData(r11, r5);
        this.output.sampleMetadata(r2, 1, r5, 0, null);
    }

    @Override // androidx.media3.extractor.p001ts.SectionPayloadReader
    public void init(TimestampAdjuster r3, ExtractorOutput r4, TsPayloadReader.TrackIdGenerator r5) {
        this.timestampAdjuster = r3;
        r5.generateNewId();
        this.output = r4.track(r5.getTrackId(), 5);
        this.output.format(this.format);
    }
}
