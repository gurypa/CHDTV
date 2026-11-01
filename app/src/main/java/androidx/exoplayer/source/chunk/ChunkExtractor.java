package androidx.media3.exoplayer.source.chunk;

import androidx.media3.common.Format;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.extractor.ChunkIndex;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.text.SubtitleParser;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface ChunkExtractor {

    public interface Factory {

        /* JADX INFO: renamed from: androidx.media3.exoplayer.source.chunk.ChunkExtractor$Factory$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static Factory $default$experimentalParseSubtitlesDuringExtraction(Factory r0, boolean r1) {
                return r0;
            }

            public static Format $default$getOutputTextFormat(Factory r0, Format r1) {
                return r1;
            }

            public static Factory $default$setSubtitleParserFactory(Factory r0, SubtitleParser.Factory r1) {
                return r0;
            }
        }

        ChunkExtractor createProgressiveMediaExtractor(int r1, Format r2, boolean r3, List<Format> r4, TrackOutput r5, PlayerId r6);

        Factory experimentalParseSubtitlesDuringExtraction(boolean r1);

        Format getOutputTextFormat(Format r1);

        Factory setSubtitleParserFactory(SubtitleParser.Factory r1);
    }

    public interface TrackOutputProvider {
        TrackOutput track(int r1, int r2);
    }

    ChunkIndex getChunkIndex();

    Format[] getSampleFormats();

    void init(TrackOutputProvider r1, long r2, long r4);

    boolean read(ExtractorInput r1) throws IOException;

    void release();
}
