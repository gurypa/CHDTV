package androidx.media3.extractor.text;

import androidx.media3.common.C0180C;
import androidx.media3.common.DataReader;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.TrackOutput;
import java.io.EOFException;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
final class SubtitleTranscodingTrackOutput implements TrackOutput {
    private final CueEncoder cueEncoder;
    private Format currentFormat;
    private SubtitleParser currentSubtitleParser;
    private final TrackOutput delegate;
    private final ParsableByteArray parsableScratch;
    private byte[] sampleData;
    private int sampleDataEnd;
    private int sampleDataStart;
    private final SubtitleParser.Factory subtitleParserFactory;

    public SubtitleTranscodingTrackOutput(TrackOutput r2, SubtitleParser.Factory r3) {
        this.delegate = r2;
        this.subtitleParserFactory = r3;
        this.cueEncoder = new CueEncoder();
        this.sampleDataStart = 0;
        this.sampleDataEnd = 0;
        this.sampleData = Util.EMPTY_BYTE_ARRAY;
        this.parsableScratch = new ParsableByteArray();
    }

    private void ensureSampleDataCapacity(int r7) {
        if ((this.sampleData.length - this.sampleDataEnd) < r7) goto L5;
        return;
    L5:
        int r0 = this.sampleDataEnd - this.sampleDataStart;
        int r1 = Math.max(r0 * 2, r0 + r7);
        if (r1 > this.sampleData.length) goto L8;
        byte[] r2 = this.sampleData;
    L9:
        System.arraycopy(this.sampleData, this.sampleDataStart, r2, 0, r0);
        this.sampleDataStart = 0;
        this.sampleDataEnd = r0;
        this.sampleData = r2;
        return;
    L8:
        r2 = new byte[r1];
        goto L9
    }

    private void outputSample(CuesWithTiming r11, long r12, int r14) {
        Assertions.checkStateNotNull(this.currentFormat);
        byte[] r0 = this.cueEncoder.encode(r11.cues, r11.durationUs);
        this.parsableScratch.reset(r0);
        this.delegate.sampleData(this.parsableScratch, r0.length);
        if (r11.startTimeUs != C0180C.TIME_UNSET) goto L10;
        if (this.currentFormat.subsampleOffsetUs != Long.MAX_VALUE) goto L7;
        boolean r1 = true;
    L8:
        Assertions.checkState(r1);
        long r4 = r12;
    L13:
        this.delegate.sampleMetadata(r4, r14, r0.length, 0, null);
        return;
    L7:
        r1 = false;
        goto L8
    L10:
        if (this.currentFormat.subsampleOffsetUs != Long.MAX_VALUE) goto L12;
        r4 = r11.startTimeUs + r12;
        goto L13
    L12:
        r4 = r11.startTimeUs + this.currentFormat.subsampleOffsetUs;
        goto L13
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void format(Format r5) {
        Assertions.checkNotNull(r5.sampleMimeType);
        if (MimeTypes.getTrackType(r5.sampleMimeType) != 3) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        if (r5.equals(this.currentFormat) == true) goto L14;
        this.currentFormat = r5;
        if (this.subtitleParserFactory.supportsFormat(r5) == false) goto L11;
        SubtitleParser r1 = this.subtitleParserFactory.create(r5);
    L12:
        this.currentSubtitleParser = r1;
        goto L14
    L11:
        r1 = null;
    L14:
        if (this.currentSubtitleParser != null) goto L16;
        this.delegate.format(r5);
        return;
    L16:
        this.delegate.format(r5.buildUpon().setSampleMimeType(MimeTypes.APPLICATION_MEDIA3_CUES).setCodecs(r5.sampleMimeType).setSubsampleOffsetUs(Long.MAX_VALUE).setCueReplacementBehavior(this.subtitleParserFactory.getCueReplacementBehavior(r5)).build());
        return;
    L5:
        r0 = false;
        goto L6
    }

    /* JADX INFO: renamed from: lambda$sampleMetadata$0$androidx-media3-extractor-text-SubtitleTranscodingTrackOutput */
    /* synthetic */ void m169xa18018cd(long r1, int r3, CuesWithTiming r4) {
        outputSample(r4, r1, r3);
    }

    public void resetSubtitleParser() {
        if (this.currentSubtitleParser == null) goto L6;
        this.currentSubtitleParser.reset();
        return;
    }

    @Override // androidx.media3.extractor.TrackOutput
    public /* synthetic */ int sampleData(DataReader r1, int r2, boolean r3) {
        return TrackOutput.CC.$default$sampleData(this, r1, r2, r3);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public int sampleData(DataReader r3, int r4, boolean r5, int r6) throws IOException {
        if (this.currentSubtitleParser == null) goto L5;
        ensureSampleDataCapacity(r4);
        int r0 = r3.read(this.sampleData, this.sampleDataEnd, r4);
        if (r0 != (-1)) goto L12;
        if (r5 == false) goto L11;
        return -1;
    L11:
        throw new EOFException();
    L12:
        this.sampleDataEnd += r0;
        return r0;
    L5:
        return this.delegate.sampleData(r3, r4, r5, r6);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public /* synthetic */ void sampleData(ParsableByteArray r1, int r2) {
        TrackOutput.CC.$default$sampleData(this, r1, r2);
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void sampleData(ParsableByteArray r3, int r4, int r5) {
        if (this.currentSubtitleParser != null) goto L6;
        this.delegate.sampleData(r3, r4, r5);
        return;
    L6:
        ensureSampleDataCapacity(r4);
        r3.readBytes(this.sampleData, this.sampleDataEnd, r4);
        this.sampleDataEnd += r4;
    }

    @Override // androidx.media3.extractor.TrackOutput
    public void sampleMetadata(final long r9, final int r11, int r12, int r13, TrackOutput.CryptoData r14) {
        if (this.currentSubtitleParser != null) goto L7;
        this.delegate.sampleMetadata(r9, r11, r12, r13, r14);
        return;
    L7:
        if (r14 != null) goto L9;
        boolean r10 = true;
    L10:
        Assertions.checkArgument(r10, "DRM on subtitles is not supported");
        int r15 = (this.sampleDataEnd - r13) - r12;
        this.currentSubtitleParser.parse(this.sampleData, r15, r12, SubtitleParser.OutputOptions.allCues(), new SubtitleTranscodingTrackOutput$$ExternalSyntheticLambda0(this, r9, r11));
        this.sampleDataStart = r15 + r12;
        if (this.sampleDataStart != this.sampleDataEnd) goto L14;
        this.sampleDataStart = 0;
        this.sampleDataEnd = 0;
        return;
    L14:
        return;
    L9:
        r10 = false;
        goto L10
    }
}
