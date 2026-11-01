package androidx.media3.exoplayer.source.chunk;

import android.media.MediaParser;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.source.mediaparser.InputReaderAdapterV30;
import androidx.media3.exoplayer.source.mediaparser.MediaParserUtil;
import androidx.media3.exoplayer.source.mediaparser.OutputConsumerAdapterV30;
import androidx.media3.extractor.ChunkIndex;
import androidx.media3.extractor.DiscardingTrackOutput;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.text.DefaultSubtitleParserFactory;
import androidx.media3.extractor.text.SubtitleExtractor;
import androidx.media3.extractor.text.SubtitleParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class MediaParserChunkExtractor implements ChunkExtractor {
    public static final ChunkExtractor.Factory FACTORY = null;
    private static final String TAG = "MediaPrsrChunkExtractor";
    private final DiscardingTrackOutput discardingTrackOutput;
    private final InputReaderAdapterV30 inputReaderAdapter;
    private final MediaParser mediaParser;
    private final OutputConsumerAdapterV30 outputConsumerAdapter;
    private long pendingSeekUs;
    private Format[] sampleFormats;
    private ChunkExtractor.TrackOutputProvider trackOutputProvider;
    private final TrackOutputProviderAdapter trackOutputProviderAdapter;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.chunk.MediaParserChunkExtractor$1 */
    static /* synthetic */ class C03521 {
    }

    public static class Factory implements ChunkExtractor.Factory {
        private boolean parseSubtitlesDuringExtraction;
        private SubtitleParser.Factory subtitleParserFactory;

        public Factory() {
            this.subtitleParserFactory = new DefaultSubtitleParserFactory();
        }

        @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor.Factory
        public ChunkExtractor createProgressiveMediaExtractor(int r4, Format r5, boolean r6, List<Format> r7, TrackOutput r8, PlayerId r9) {
            if (MimeTypes.isText(r5.containerMimeType) == true) goto L7;
            return new MediaParserChunkExtractor(r4, r5, r7, r9);
        L7:
            if (this.parseSubtitlesDuringExtraction == true) goto L11;
            return null;
        L11:
            return new BundledChunkExtractor(new SubtitleExtractor(this.subtitleParserFactory.create(r5), r5), r4, r5);
        }

        @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor.Factory
        public /* bridge */ /* synthetic */ ChunkExtractor.Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
            return experimentalParseSubtitlesDuringExtraction(r1);
        }

        @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor.Factory
        public Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
            this.parseSubtitlesDuringExtraction = r1;
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
        public /* bridge */ /* synthetic */ ChunkExtractor.Factory setSubtitleParserFactory(SubtitleParser.Factory r1) {
            return setSubtitleParserFactory(r1);
        }

        @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor.Factory
        public Factory setSubtitleParserFactory(SubtitleParser.Factory r2) {
            this.subtitleParserFactory = (SubtitleParser.Factory) Assertions.checkNotNull(r2);
            return this;
        }
    }

    private class TrackOutputProviderAdapter implements ExtractorOutput {
        final /* synthetic */ MediaParserChunkExtractor this$0;

        private TrackOutputProviderAdapter(MediaParserChunkExtractor r1) {
            this.this$0 = r1;
        }

        /* synthetic */ TrackOutputProviderAdapter(MediaParserChunkExtractor r1, C03521 r2) {
            this(r1);
        }

        @Override // androidx.media3.extractor.ExtractorOutput
        public void endTracks() {
            MediaParserChunkExtractor.access$302(this.this$0, MediaParserChunkExtractor.access$400(this.this$0).getSampleFormats());
        }

        @Override // androidx.media3.extractor.ExtractorOutput
        public void seekMap(SeekMap r1) {
        }

        @Override // androidx.media3.extractor.ExtractorOutput
        public TrackOutput track(int r2, int r3) {
            if (MediaParserChunkExtractor.access$100(this.this$0) == null) goto L6;
            return MediaParserChunkExtractor.access$100(this.this$0).track(r2, r3);
        L6:
            return MediaParserChunkExtractor.access$200(this.this$0);
        }
    }

    static {
        FACTORY = new Factory();
    }

    public MediaParserChunkExtractor(int r6, Format r7, List<Format> r8, PlayerId r9) {
        this.outputConsumerAdapter = new OutputConsumerAdapterV30(r7, r6, true);
        this.inputReaderAdapter = new InputReaderAdapterV30();
        if (MimeTypes.isMatroska((String) Assertions.checkNotNull(r7.containerMimeType)) == false) goto L5;
        String r1 = "android.media.mediaparser.MatroskaParser";
    L6:
        this.outputConsumerAdapter.setSelectedParserName(r1);
        this.mediaParser = MediaParser.createByName(r1, this.outputConsumerAdapter);
        this.mediaParser.setParameter("android.media.mediaparser.matroska.disableCuesSeeking", true);
        this.mediaParser.setParameter(MediaParserUtil.PARAMETER_IN_BAND_CRYPTO_INFO, true);
        this.mediaParser.setParameter(MediaParserUtil.PARAMETER_INCLUDE_SUPPLEMENTAL_DATA, true);
        this.mediaParser.setParameter(MediaParserUtil.PARAMETER_EAGERLY_EXPOSE_TRACK_TYPE, true);
        this.mediaParser.setParameter(MediaParserUtil.PARAMETER_EXPOSE_DUMMY_SEEK_MAP, true);
        this.mediaParser.setParameter(MediaParserUtil.PARAMETER_EXPOSE_CHUNK_INDEX_AS_MEDIA_FORMAT, true);
        this.mediaParser.setParameter(MediaParserUtil.PARAMETER_OVERRIDE_IN_BAND_CAPTION_DECLARATIONS, true);
        ArrayList r2 = new ArrayList();
        int r3 = 0;
    L8:
        if (r3 >= r8.size()) goto L10;
        r2.add(MediaParserUtil.toCaptionsMediaFormat(r8.get(r3)));
        r3 = r3 + 1;
        goto L8
    L10:
        this.mediaParser.setParameter(MediaParserUtil.PARAMETER_EXPOSE_CAPTION_FORMATS, r2);
        if (Util.SDK_INT < 31) goto L13;
        MediaParserUtil.setLogSessionIdOnMediaParser(this.mediaParser, r9);
    L13:
        this.outputConsumerAdapter.setMuxedCaptionFormats(r8);
        this.trackOutputProviderAdapter = new TrackOutputProviderAdapter(this, null);
        this.discardingTrackOutput = new DiscardingTrackOutput();
        this.pendingSeekUs = C0180C.TIME_UNSET;
        return;
    L5:
        r1 = "android.media.mediaparser.FragmentedMp4Parser";
        goto L6
    }

    static /* synthetic */ ChunkExtractor.TrackOutputProvider access$100(MediaParserChunkExtractor r1) {
        return r1.trackOutputProvider;
    }

    static /* synthetic */ DiscardingTrackOutput access$200(MediaParserChunkExtractor r1) {
        return r1.discardingTrackOutput;
    }

    static /* synthetic */ Format[] access$302(MediaParserChunkExtractor r0, Format[] r1) {
        r0.sampleFormats = r1;
        return r1;
    }

    static /* synthetic */ OutputConsumerAdapterV30 access$400(MediaParserChunkExtractor r1) {
        return r1.outputConsumerAdapter;
    }

    private void maybeExecutePendingSeek() {
        MediaParser.SeekMap r0 = this.outputConsumerAdapter.getDummySeekMap();
        if (this.pendingSeekUs == C0180C.TIME_UNSET) goto L7;
        if (r0 == null) goto L8;
        this.mediaParser.seek((MediaParser.SeekPoint) r0.getSeekPoints(this.pendingSeekUs).first);
        this.pendingSeekUs = C0180C.TIME_UNSET;
        return;
    L8:
        return;
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor
    public ChunkIndex getChunkIndex() {
        return this.outputConsumerAdapter.getChunkIndex();
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor
    public Format[] getSampleFormats() {
        return this.sampleFormats;
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor
    public void init(ChunkExtractor.TrackOutputProvider r3, long r4, long r6) {
        this.trackOutputProvider = r3;
        this.outputConsumerAdapter.setSampleTimestampUpperLimitFilterUs(r6);
        this.outputConsumerAdapter.setExtractorOutput(this.trackOutputProviderAdapter);
        this.pendingSeekUs = r4;
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor
    public boolean read(ExtractorInput r4) throws IOException {
        maybeExecutePendingSeek();
        this.inputReaderAdapter.setDataReader(r4, r4.getLength());
        return this.mediaParser.advance(this.inputReaderAdapter);
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkExtractor
    public void release() {
        this.mediaParser.release();
    }
}
