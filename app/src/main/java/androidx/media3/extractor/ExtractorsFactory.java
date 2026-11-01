package androidx.media3.extractor;

import android.net.Uri;
import androidx.media3.extractor.text.SubtitleParser;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface ExtractorsFactory {
    public static final ExtractorsFactory EMPTY = null;

    /* JADX INFO: renamed from: androidx.media3.extractor.ExtractorsFactory$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static Extractor[] $default$createExtractors(ExtractorsFactory r1, Uri r2, Map r3) {
            return r1.createExtractors();
        }

        @Deprecated
        public static ExtractorsFactory $default$experimentalSetTextTrackTranscodingEnabled(ExtractorsFactory r0, boolean r1) {
            return r0;
        }

        public static ExtractorsFactory $default$setSubtitleParserFactory(ExtractorsFactory r0, SubtitleParser.Factory r1) {
            return r0;
        }

        static {
            ExtractorsFactory r0 = ExtractorsFactory.EMPTY;
        }

        public static /* synthetic */ Extractor[] lambda$static$0() {
            return new Extractor[0];
        }
    }

    static {
        EMPTY = new ExtractorsFactory$$ExternalSyntheticLambda0();
    }

    Extractor[] createExtractors();

    Extractor[] createExtractors(Uri r1, Map<String, List<String>> r2);

    @Deprecated
    ExtractorsFactory experimentalSetTextTrackTranscodingEnabled(boolean r1);

    ExtractorsFactory setSubtitleParserFactory(SubtitleParser.Factory r1);
}
