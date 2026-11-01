package androidx.media3.exoplayer.hls;

import androidx.media3.common.Format;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.mp3.Mp3Extractor;
import androidx.media3.extractor.mp4.FragmentedMp4Extractor;
import androidx.media3.extractor.p001ts.Ac3Extractor;
import androidx.media3.extractor.p001ts.Ac4Extractor;
import androidx.media3.extractor.p001ts.AdtsExtractor;
import androidx.media3.extractor.p001ts.TsExtractor;
import androidx.media3.extractor.text.SubtitleParser;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class BundledHlsMediaChunkExtractor implements HlsMediaChunkExtractor {
    private static final PositionHolder POSITION_HOLDER = null;
    final Extractor extractor;
    private final Format multivariantPlaylistFormat;
    private final boolean parseSubtitlesDuringExtraction;
    private final SubtitleParser.Factory subtitleParserFactory;
    private final TimestampAdjuster timestampAdjuster;

    static {
        POSITION_HOLDER = new PositionHolder();
    }

    public BundledHlsMediaChunkExtractor(Extractor r7, Format r8, TimestampAdjuster r9) {
        this(r7, r8, r9, SubtitleParser.Factory.UNSUPPORTED, false);
    }

    BundledHlsMediaChunkExtractor(Extractor r1, Format r2, TimestampAdjuster r3, SubtitleParser.Factory r4, boolean r5) {
        this.extractor = r1;
        this.multivariantPlaylistFormat = r2;
        this.timestampAdjuster = r3;
        this.subtitleParserFactory = r4;
        this.parseSubtitlesDuringExtraction = r5;
    }

    @Override // androidx.media3.exoplayer.hls.HlsMediaChunkExtractor
    public void init(ExtractorOutput r2) {
        this.extractor.init(r2);
    }

    @Override // androidx.media3.exoplayer.hls.HlsMediaChunkExtractor
    public boolean isPackedAudioExtractor() {
        Extractor r0 = this.extractor.getUnderlyingImplementation();
        if ((r0 instanceof AdtsExtractor) == false) goto L5;
    L12:
        return true;
    L5:
        if ((r0 instanceof Ac3Extractor) == true) goto L12;
        if ((r0 instanceof Ac4Extractor) == true) goto L12;
        if ((r0 instanceof Mp3Extractor) == true) goto L12;
        return false;
    }

    @Override // androidx.media3.exoplayer.hls.HlsMediaChunkExtractor
    public boolean isReusable() {
        Extractor r0 = this.extractor.getUnderlyingImplementation();
        if ((r0 instanceof TsExtractor) == false) goto L5;
    L8:
        return true;
    L5:
        if ((r0 instanceof FragmentedMp4Extractor) == true) goto L8;
        return false;
    }

    @Override // androidx.media3.exoplayer.hls.HlsMediaChunkExtractor
    public void onTruncatedSegmentParsed() {
        this.extractor.seek(0, 0);
    }

    @Override // androidx.media3.exoplayer.hls.HlsMediaChunkExtractor
    public boolean read(ExtractorInput r3) throws IOException {
        if (this.extractor.read(r3, POSITION_HOLDER) != 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.exoplayer.hls.HlsMediaChunkExtractor
    public HlsMediaChunkExtractor recreate() {
        boolean r1 = true;
        Assertions.checkState(!isReusable());
        if (this.extractor.getUnderlyingImplementation() == this.extractor) goto L6;
        r1 = false;
    L6:
        Assertions.checkState(r1, "Can't recreate wrapped extractors. Outer type: " + this.extractor.getClass());
        if ((this.extractor instanceof WebvttExtractor) == false) goto L10;
        Extractor r2 = new WebvttExtractor(this.multivariantPlaylistFormat.language, this.timestampAdjuster, this.subtitleParserFactory, this.parseSubtitlesDuringExtraction);
    L22:
        return new BundledHlsMediaChunkExtractor(r2, this.multivariantPlaylistFormat, this.timestampAdjuster, this.subtitleParserFactory, this.parseSubtitlesDuringExtraction);
    L10:
        if ((this.extractor instanceof AdtsExtractor) == false) goto L13;
        r2 = new AdtsExtractor();
        goto L22
    L13:
        if ((this.extractor instanceof Ac3Extractor) == false) goto L16;
        r2 = new Ac3Extractor();
        goto L22
    L16:
        if ((this.extractor instanceof Ac4Extractor) == false) goto L19;
        r2 = new Ac4Extractor();
        goto L22
    L19:
        if ((this.extractor instanceof Mp3Extractor) == false) goto L24;
        r2 = new Mp3Extractor();
        goto L22
    L24:
        throw new IllegalStateException("Unexpected extractor type for recreation: " + this.extractor.getClass().getSimpleName());
    }
}
