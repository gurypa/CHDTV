package androidx.media3.exoplayer.source;

import android.media.MediaParser;
import android.net.Uri;
import android.util.Pair;
import androidx.media3.common.DataReader;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.source.mediaparser.InputReaderAdapterV30;
import androidx.media3.exoplayer.source.mediaparser.MediaParserUtil;
import androidx.media3.exoplayer.source.mediaparser.OutputConsumerAdapterV30;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.PositionHolder;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class MediaParserExtractorAdapter implements ProgressiveMediaExtractor {

    @Deprecated
    public static final ProgressiveMediaExtractor.Factory FACTORY = null;
    private final InputReaderAdapterV30 inputReaderAdapter;
    private final MediaParser mediaParser;
    private final OutputConsumerAdapterV30 outputConsumerAdapter;
    private String parserName;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.MediaParserExtractorAdapter$1 */
    static /* synthetic */ class C03351 {
    }

    public static final class Factory implements ProgressiveMediaExtractor.Factory {
        private static final Map<String, Object> parameters = null;

        static {
            parameters = new HashMap();
        }

        public Factory() {
        }

        @Override // androidx.media3.exoplayer.source.ProgressiveMediaExtractor.Factory
        public MediaParserExtractorAdapter createProgressiveMediaExtractor(PlayerId r4) {
            return new MediaParserExtractorAdapter(r4, parameters, null);
        }

        @Override // androidx.media3.exoplayer.source.ProgressiveMediaExtractor.Factory
        public /* bridge */ /* synthetic */ ProgressiveMediaExtractor createProgressiveMediaExtractor(PlayerId r1) {
            return createProgressiveMediaExtractor(r1);
        }

        public void setConstantBitrateSeekingEnabled(boolean r7) {
            if (r7 == false) goto L5;
            parameters.put("android.media.mediaparser.adts.enableCbrSeeking", true);
            parameters.put("android.media.mediaparser.amr.enableCbrSeeking", true);
            parameters.put("android.media.mediaparser.mp3.enableCbrSeeking", true);
            return;
        L5:
            parameters.remove("android.media.mediaparser.adts.enableCbrSeeking");
            parameters.remove("android.media.mediaparser.amr.enableCbrSeeking");
            parameters.remove("android.media.mediaparser.mp3.enableCbrSeeking");
        }
    }

    static {
        FACTORY = new MediaParserExtractorAdapter$$ExternalSyntheticLambda0();
    }

    @Deprecated
    public MediaParserExtractorAdapter(PlayerId r2) {
        this(r2, ImmutableMap.m231of());
    }

    private MediaParserExtractorAdapter(PlayerId r6, Map<String, Object> r7) {
        this.outputConsumerAdapter = new OutputConsumerAdapterV30();
        this.inputReaderAdapter = new InputReaderAdapterV30();
        this.mediaParser = MediaParser.create(this.outputConsumerAdapter, new String[0]);
        this.mediaParser.setParameter(MediaParserUtil.PARAMETER_EAGERLY_EXPOSE_TRACK_TYPE, true);
        this.mediaParser.setParameter(MediaParserUtil.PARAMETER_IN_BAND_CRYPTO_INFO, true);
        this.mediaParser.setParameter(MediaParserUtil.PARAMETER_INCLUDE_SUPPLEMENTAL_DATA, true);
        Iterator<Map.Entry<String, Object>> r0 = r7.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Map.Entry<String, Object> r1 = r0.next();
        this.mediaParser.setParameter(r1.getKey(), r1.getValue());
        goto L4
    L6:
        this.parserName = "android.media.mediaparser.UNKNOWN";
        if (Util.SDK_INT < 31) goto L11;
        MediaParserUtil.setLogSessionIdOnMediaParser(this.mediaParser, r6);
        return;
    }

    /* synthetic */ MediaParserExtractorAdapter(PlayerId r1, Map r2, C03351 r3) {
        this(r1, r2);
    }

    static /* synthetic */ ProgressiveMediaExtractor lambda$static$0(PlayerId r2) {
        return new MediaParserExtractorAdapter(r2, ImmutableMap.m231of());
    }

    @Override // androidx.media3.exoplayer.source.ProgressiveMediaExtractor
    public void disableSeekingOnMp3Streams() {
        if ("android.media.mediaparser.Mp3Parser".equals(this.parserName) == false) goto L6;
        this.outputConsumerAdapter.disableSeeking();
        return;
    }

    @Override // androidx.media3.exoplayer.source.ProgressiveMediaExtractor
    public long getCurrentInputPosition() {
        return this.inputReaderAdapter.getPosition();
    }

    @Override // androidx.media3.exoplayer.source.ProgressiveMediaExtractor
    public void init(DataReader r4, Uri r5, Map<String, List<String>> r6, long r7, long r9, ExtractorOutput r11) throws IOException {
        this.outputConsumerAdapter.setExtractorOutput(r11);
        this.inputReaderAdapter.setDataReader(r4, r9);
        this.inputReaderAdapter.setCurrentPosition(r7);
        String r0 = this.mediaParser.getParserName();
        if ("android.media.mediaparser.UNKNOWN".equals(r0) == false) goto L6;
        this.mediaParser.advance(this.inputReaderAdapter);
        this.parserName = this.mediaParser.getParserName();
        this.outputConsumerAdapter.setSelectedParserName(this.parserName);
        return;
    L6:
        if (r0.equals(this.parserName) == true) goto L10;
        this.parserName = this.mediaParser.getParserName();
        this.outputConsumerAdapter.setSelectedParserName(this.parserName);
        return;
    }

    @Override // androidx.media3.exoplayer.source.ProgressiveMediaExtractor
    public int read(PositionHolder r6) throws IOException {
        boolean r0 = this.mediaParser.advance(this.inputReaderAdapter);
        r6.position = this.inputReaderAdapter.getAndResetSeekPosition();
        if (r0 == true) goto L6;
        return -1;
    L6:
        if (r6.position == (-1)) goto L8;
        return 1;
    L8:
        return 0;
    }

    @Override // androidx.media3.exoplayer.source.ProgressiveMediaExtractor
    public void release() {
        this.mediaParser.release();
    }

    @Override // androidx.media3.exoplayer.source.ProgressiveMediaExtractor
    public void seek(long r5, long r7) {
        this.inputReaderAdapter.setCurrentPosition(r5);
        Pair<MediaParser.SeekPoint, MediaParser.SeekPoint> r0 = this.outputConsumerAdapter.getSeekPoints(r7);
        MediaParser r1 = this.mediaParser;
        if (((MediaParser.SeekPoint) r0.second).position != r5) goto L5;
        Object r2 = r0.second;
    L6:
        r1.seek((MediaParser.SeekPoint) r2);
        return;
    L5:
        r2 = r0.first;
        goto L6
    }
}
