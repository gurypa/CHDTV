package androidx.media3.exoplayer.hls;

import android.net.Uri;
import androidx.media3.common.Format;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.text.SubtitleParser;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface HlsExtractorFactory {

    @Deprecated
    public static final HlsExtractorFactory DEFAULT = null;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.hls.HlsExtractorFactory$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static HlsExtractorFactory $default$experimentalParseSubtitlesDuringExtraction(HlsExtractorFactory r0, boolean r1) {
            return r0;
        }

        public static Format $default$getOutputTextFormat(HlsExtractorFactory r0, Format r1) {
            return r1;
        }

        public static HlsExtractorFactory $default$setSubtitleParserFactory(HlsExtractorFactory r0, SubtitleParser.Factory r1) {
            return r0;
        }
    }

    static {
        DEFAULT = new DefaultHlsExtractorFactory();
    }

    HlsMediaChunkExtractor createExtractor(Uri r1, Format r2, List<Format> r3, TimestampAdjuster r4, Map<String, List<String>> r5, ExtractorInput r6, PlayerId r7) throws IOException;

    HlsExtractorFactory experimentalParseSubtitlesDuringExtraction(boolean r1);

    Format getOutputTextFormat(Format r1);

    HlsExtractorFactory setSubtitleParserFactory(SubtitleParser.Factory r1);
}
