package androidx.media3.exoplayer.source.chunk;

import android.util.SparseArray;
import androidx.media3.common.C0180C;
import androidx.media3.common.DataReader;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.extractor.ChunkIndex;
import androidx.media3.extractor.DiscardingTrackOutput;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.jpeg.JpegExtractor;
import androidx.media3.extractor.mkv.MatroskaExtractor;
import androidx.media3.extractor.mp4.FragmentedMp4Extractor;
import androidx.media3.extractor.png.PngExtractor;
import androidx.media3.extractor.text.DefaultSubtitleParserFactory;
import androidx.media3.extractor.text.SubtitleExtractor;
import androidx.media3.extractor.text.SubtitleParser;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class BundledChunkExtractor implements ExtractorOutput, ChunkExtractor {
    public static final Factory FACTORY = null;
    private static final PositionHolder POSITION_HOLDER = null;
    private final SparseArray<BindingTrackOutput> bindingTrackOutputs;
    private long endTimeUs;
    private final Extractor extractor;
    private boolean extractorInitialized;
    private final Format primaryTrackManifestFormat;
    private final int primaryTrackType;
    private Format[] sampleFormats;
    private SeekMap seekMap;
    private ChunkExtractor.TrackOutputProvider trackOutputProvider;

    private static final class BindingTrackOutput implements TrackOutput {
        private long endTimeUs;
        private final DiscardingTrackOutput fakeTrackOutput;

        /* JADX INFO: renamed from: id */
        private final int f47id;
        private final Format manifestFormat;
        public Format sampleFormat;
        private TrackOutput trackOutput;
        private final int type;

        public BindingTrackOutput(int r2, int r3, Format r4) {
            this.f47id = r2;
            this.type = r3;
            this.manifestFormat = r4;
            this.fakeTrackOutput = new DiscardingTrackOutput();
        }

        public void bind(ChunkExtractor.TrackOutputProvider r3, long r4) {
            if (r3 != null) goto L5;
            this.trackOutput = this.fakeTrackOutput;
            return;
        L5:
            this.endTimeUs = r4;
            this.trackOutput = r3.track(this.f47id, this.type);
            if (this.sampleFormat == null) goto L9;
            this.trackOutput.format(this.sampleFormat);
            return;
        }

        @Override // androidx.media3.extractor.TrackOutput
        public void format(Format r3) {
            if (this.manifestFormat == null) goto L5;
            Format r0 = r3.withManifestFormatInfo(this.manifestFormat);
        L6:
            this.sampleFormat = r0;
            ((TrackOutput) Util.castNonNull(this.trackOutput)).format(this.sampleFormat);
            return;
        L5:
            r0 = r3;
            goto L6
        }

        @Override // androidx.media3.extractor.TrackOutput
        public /* synthetic */ int sampleData(DataReader r1, int r2, boolean r3) {
            return TrackOutput.CC.$default$sampleData(this, r1, r2, r3);
        }

        @Override // androidx.media3.extractor.TrackOutput
        public int sampleData(DataReader r2, int r3, boolean r4, int r5) throws IOException {
            return ((TrackOutput) Util.castNonNull(this.trackOutput)).sampleData(r2, r3, r4);
        }

        @Override // androidx.media3.extractor.TrackOutput
        public /* synthetic */ void sampleData(ParsableByteArray r1, int r2) {
            TrackOutput.CC.$default$sampleData(this, r1, r2);
        }

        @Override // androidx.media3.extractor.TrackOutput
        public void sampleData(ParsableByteArray r2, int r3, int r4) {
            ((TrackOutput) Util.castNonNull(this.trackOutput)).sampleData(r2, r3);
        }

        @Override // androidx.media3.extractor.TrackOutput
        public void sampleMetadata(long r9, int r11, int r12, int r13, TrackOutput.CryptoData r14) {
            if (this.endTimeUs != C0180C.TIME_UNSET) goto L5;
        L7:
            ((TrackOutput) Util.castNonNull(this.trackOutput)).sampleMetadata(r9, r11, r12, r13, r14);
            return;
        L5:
            if (r9 < this.endTimeUs) goto L7;
            this.trackOutput = this.fakeTrackOutput;
            goto L7
        }
    }

    public static final class Factory implements ChunkExtractor.Factory {
        private boolean parseSubtitlesDuringExtraction;
        private boolean parseWithinGopSampleDependencies;
        private SubtitleParser.Factory subtitleParserFactory;

        public Factory() {
            this.subtitleParserFactory = new DefaultSubtitleParserFactory();
        }

        @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor.Factory
        public ChunkExtractor createProgressiveMediaExtractor(int r10, Format r11, boolean r12, List<Format> r13, TrackOutput r14, PlayerId r15) {
            String r0 = r11.containerMimeType;
            if (MimeTypes.isText(r0) == false) goto L10;
            if (this.parseSubtitlesDuringExtraction == true) goto L8;
            return null;
        L8:
            Extractor r1 = new SubtitleExtractor(this.subtitleParserFactory.create(r11), r11);
        L33:
            return new BundledChunkExtractor(r1, r10, r11);
        L10:
            if (MimeTypes.isMatroska(r0) == false) goto L16;
            int r2 = 1;
            if (this.parseSubtitlesDuringExtraction == true) goto L14;
            r2 = 1 | 2;
        L14:
            r1 = new MatroskaExtractor(this.subtitleParserFactory, r2);
            goto L33
        L16:
            if (Objects.equals(r0, MimeTypes.IMAGE_JPEG) == false) goto L19;
            r1 = new JpegExtractor(1);
            goto L33
        L19:
            if (Objects.equals(r0, MimeTypes.IMAGE_PNG) == false) goto L21;
            r1 = new PngExtractor();
            goto L33
        L21:
            int r3 = 0;
            if (r12 == false) goto L25;
            r3 = 0 | 4;
        L25:
            if (this.parseSubtitlesDuringExtraction == true) goto L28;
            r3 = r3 | 32;
        L28:
            if (this.parseWithinGopSampleDependencies == false) goto L30;
            int r4 = r3 | 64;
        L31:
            r1 = new FragmentedMp4Extractor(this.subtitleParserFactory, r4, null, null, r13, r14);
            goto L33
        L30:
            r4 = r3;
            goto L31
        }

        @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor.Factory
        public Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
            this.parseSubtitlesDuringExtraction = r1;
            return this;
        }

        @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor.Factory
        public /* bridge */ /* synthetic */ ChunkExtractor.Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
            return experimentalParseSubtitlesDuringExtraction(r1);
        }

        public Factory experimentalParseWithinGopSampleDependencies(boolean r1) {
            this.parseWithinGopSampleDependencies = r1;
            return this;
        }

        @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor.Factory
        public Format getOutputTextFormat(Format r5) {
            if (this.parseSubtitlesDuringExtraction == true) goto L5;
        L12:
            return r5;
        L5:
            if (this.subtitleParserFactory.supportsFormat(r5) == false) goto L12;
            Format.Builder r0 = r5.buildUpon().setSampleMimeType(MimeTypes.APPLICATION_MEDIA3_CUES).setCueReplacementBehavior(this.subtitleParserFactory.getCueReplacementBehavior(r5));
            StringBuilder r1 = new StringBuilder().append(r5.sampleMimeType);
            if (r5.codecs == null) goto L9;
            String r2 = " " + r5.codecs;
        L11:
            return r0.setCodecs(r1.append(r2).toString()).setSubsampleOffsetUs(Long.MAX_VALUE).build();
        L9:
            r2 = "";
            goto L11
        }

        @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor.Factory
        public Factory setSubtitleParserFactory(SubtitleParser.Factory r2) {
            this.subtitleParserFactory = (SubtitleParser.Factory) Assertions.checkNotNull(r2);
            return this;
        }

        @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor.Factory
        public /* bridge */ /* synthetic */ ChunkExtractor.Factory setSubtitleParserFactory(SubtitleParser.Factory r1) {
            return setSubtitleParserFactory(r1);
        }
    }

    static {
        FACTORY = new Factory();
        POSITION_HOLDER = new PositionHolder();
    }

    public BundledChunkExtractor(Extractor r2, int r3, Format r4) {
        this.extractor = r2;
        this.primaryTrackType = r3;
        this.primaryTrackManifestFormat = r4;
        this.bindingTrackOutputs = new SparseArray();
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public void endTracks() {
        Format[] r0 = new Format[this.bindingTrackOutputs.size()];
        int r1 = 0;
    L4:
        if (r1 >= this.bindingTrackOutputs.size()) goto L6;
        r0[r1] = (Format) Assertions.checkStateNotNull(this.bindingTrackOutputs.valueAt(r1).sampleFormat);
        r1 = r1 + 1;
        goto L4
    L6:
        this.sampleFormats = r0;
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor
    public ChunkIndex getChunkIndex() {
        if ((this.seekMap instanceof ChunkIndex) == true) goto L7;
        return null;
    L7:
        return (ChunkIndex) this.seekMap;
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor
    public Format[] getSampleFormats() {
        return this.sampleFormats;
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor
    public void init(ChunkExtractor.TrackOutputProvider r6, long r7, long r9) {
        this.trackOutputProvider = r6;
        this.endTimeUs = r9;
        if (this.extractorInitialized == true) goto L8;
        this.extractor.init(this);
        if (r7 == C0180C.TIME_UNSET) goto L7;
        this.extractor.seek(0, r7);
    L7:
        this.extractorInitialized = true;
        return;
    L8:
        Extractor r0 = this.extractor;
        if (r7 != C0180C.TIME_UNSET) goto L11;
        long r1 = 0;
    L12:
        r0.seek(0, r1);
        int r2 = 0;
    L14:
        if (r2 >= this.bindingTrackOutputs.size()) goto L18;
        this.bindingTrackOutputs.valueAt(r2).bind(r6, r9);
        r2 = r2 + 1;
        goto L14
    L18:
        return;
    L11:
        r1 = r7;
        goto L12
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor
    public boolean read(ExtractorInput r5) throws IOException {
        int r0 = this.extractor.read(r5, POSITION_HOLDER);
        if (r0 == 1) goto L5;
        boolean r3 = true;
    L6:
        Assertions.checkState(r3);
        if (r0 == 0) goto L9;
        return false;
    L9:
        return true;
    L5:
        r3 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor
    public void release() {
        this.extractor.release();
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public void seekMap(SeekMap r1) {
        this.seekMap = r1;
    }

    @Override // androidx.media3.extractor.ExtractorOutput
    public TrackOutput track(int r5, int r6) {
        BindingTrackOutput r0 = this.bindingTrackOutputs.get(r5);
        if (r0 == null) goto L5;
        return r0;
    L5:
        if (this.sampleFormats != null) goto L7;
        boolean r1 = true;
    L8:
        Assertions.checkState(r1);
        if (r6 != this.primaryTrackType) goto L11;
        Format r2 = this.primaryTrackManifestFormat;
    L12:
        BindingTrackOutput r3 = new BindingTrackOutput(r5, r6, r2);
        r3.bind(this.trackOutputProvider, this.endTimeUs);
        this.bindingTrackOutputs.put(r5, r3);
        return r3;
    L11:
        r2 = null;
        goto L12
    L7:
        r1 = false;
        goto L8
    }
}
