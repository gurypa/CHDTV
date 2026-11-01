package androidx.media3.exoplayer.source.mediaparser;

import android.media.MediaFormat;
import android.media.MediaParser;
import android.media.metrics.LogSessionId;
import androidx.media3.common.Format;
import androidx.media3.exoplayer.analytics.PlayerId;

/* JADX INFO: loaded from: classes.dex */
public final class MediaParserUtil {
    public static final String PARAMETER_EAGERLY_EXPOSE_TRACK_TYPE = "android.media.mediaparser.eagerlyExposeTrackType";
    public static final String PARAMETER_EXPOSE_CAPTION_FORMATS = "android.media.mediaParser.exposeCaptionFormats";
    public static final String PARAMETER_EXPOSE_CHUNK_INDEX_AS_MEDIA_FORMAT = "android.media.mediaParser.exposeChunkIndexAsMediaFormat";
    public static final String PARAMETER_EXPOSE_DUMMY_SEEK_MAP = "android.media.mediaparser.exposeDummySeekMap";
    public static final String PARAMETER_IGNORE_TIMESTAMP_OFFSET = "android.media.mediaparser.ignoreTimestampOffset";
    public static final String PARAMETER_INCLUDE_SUPPLEMENTAL_DATA = "android.media.mediaparser.includeSupplementalData";
    public static final String PARAMETER_IN_BAND_CRYPTO_INFO = "android.media.mediaparser.inBandCryptoInfo";
    public static final String PARAMETER_OVERRIDE_IN_BAND_CAPTION_DECLARATIONS = "android.media.mediaParser.overrideInBandCaptionDeclarations";

    private static final class Api31 {
        private Api31() {
        }

        public static void setLogSessionIdOnMediaParser(MediaParser r2, PlayerId r3) {
            LogSessionId r0 = r3.getLogSessionId();
            if (r0.equals(LogSessionId.LOG_SESSION_ID_NONE) == true) goto L6;
            r2.setLogSessionId(r0);
            return;
        }
    }

    private MediaParserUtil() {
    }

    public static void setLogSessionIdOnMediaParser(MediaParser r0, PlayerId r1) {
        Api31.setLogSessionIdOnMediaParser(r0, r1);
    }

    public static MediaFormat toCaptionsMediaFormat(Format r3) {
        MediaFormat r0 = new MediaFormat();
        r0.setString("mime", r3.sampleMimeType);
        if (r3.accessibilityChannel == (-1)) goto L5;
        r0.setInteger("caption-service-number", r3.accessibilityChannel);
    L5:
        return r0;
    }
}
