package androidx.media3.exoplayer.upstream;

import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Assertions;
import com.google.common.collect.ImmutableListMultimap;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public final class CmcdConfiguration {
    public static final String CMCD_QUERY_PARAMETER_KEY = "CMCD";
    public static final String KEY_BITRATE = "br";
    public static final String KEY_BUFFER_LENGTH = "bl";
    public static final String KEY_BUFFER_STARVATION = "bs";
    public static final String KEY_CMCD_OBJECT = "CMCD-Object";
    public static final String KEY_CMCD_REQUEST = "CMCD-Request";
    public static final String KEY_CMCD_SESSION = "CMCD-Session";
    public static final String KEY_CMCD_STATUS = "CMCD-Status";
    public static final String KEY_CONTENT_ID = "cid";
    public static final String KEY_DEADLINE = "dl";
    public static final String KEY_MAXIMUM_REQUESTED_BITRATE = "rtp";
    public static final String KEY_MEASURED_THROUGHPUT = "mtp";
    public static final String KEY_NEXT_OBJECT_REQUEST = "nor";
    public static final String KEY_NEXT_RANGE_REQUEST = "nrr";
    public static final String KEY_OBJECT_DURATION = "d";
    public static final String KEY_OBJECT_TYPE = "ot";
    public static final String KEY_PLAYBACK_RATE = "pr";
    public static final String KEY_SESSION_ID = "sid";
    public static final String KEY_STARTUP = "su";
    public static final String KEY_STREAMING_FORMAT = "sf";
    public static final String KEY_STREAM_TYPE = "st";
    public static final String KEY_TOP_BITRATE = "tb";
    public static final String KEY_VERSION = "v";
    public static final int MAX_ID_LENGTH = 64;
    public static final int MODE_QUERY_PARAMETER = 1;
    public static final int MODE_REQUEST_HEADER = 0;
    public final String contentId;
    public final int dataTransmissionMode;
    public final RequestConfig requestConfig;
    public final String sessionId;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface CmcdKey {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface DataTransmissionMode {
    }

    public interface Factory {
        public static final Factory DEFAULT = null;

        /* JADX INFO: renamed from: androidx.media3.exoplayer.upstream.CmcdConfiguration$Factory$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            static {
                Factory r0 = Factory.DEFAULT;
            }

            public static /* synthetic */ CmcdConfiguration lambda$static$0(MediaItem r4) {
                String r1 = UUID.randomUUID().toString();
                if (r4.mediaId == null) goto L5;
                String r2 = r4.mediaId;
            L7:
                return new CmcdConfiguration(r1, r2, new C03681());
            L5:
                r2 = "";
                goto L7
            }
        }

        static {
            DEFAULT = new CmcdConfiguration$Factory$$ExternalSyntheticLambda0();
        }

        CmcdConfiguration createCmcdConfiguration(MediaItem r1);
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface HeaderKey {
    }

    public interface RequestConfig {

        /* JADX INFO: renamed from: androidx.media3.exoplayer.upstream.CmcdConfiguration$RequestConfig$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static ImmutableListMultimap $default$getCustomData(RequestConfig r1) {
                return ImmutableListMultimap.m225of();
            }

            public static int $default$getRequestedMaximumThroughputKbps(RequestConfig r1, int r2) {
                return C0180C.RATE_UNSET_INT;
            }

            public static boolean $default$isKeyAllowed(RequestConfig r1, String r2) {
                return true;
            }
        }

        ImmutableListMultimap<String, String> getCustomData();

        int getRequestedMaximumThroughputKbps(int r1);

        boolean isKeyAllowed(String r1);
    }

    public CmcdConfiguration(String r2, String r3, RequestConfig r4) {
        this(r2, r3, r4, 0);
    }

    public CmcdConfiguration(String r5, String r6, RequestConfig r7, int r8) {
        boolean r0 = false;
        if (r5 != null) goto L5;
    L8:
        boolean r3 = true;
    L9:
        Assertions.checkArgument(r3);
        if (r6 != null) goto L12;
    L13:
        r0 = true;
    L14:
        Assertions.checkArgument(r0);
        Assertions.checkNotNull(r7);
        this.sessionId = r5;
        this.contentId = r6;
        this.requestConfig = r7;
        this.dataTransmissionMode = r8;
        return;
    L12:
        if (r6.length() > 64) goto L14;
    L5:
        if (r5.length() <= 64) goto L8;
        r3 = false;
        goto L9
    }

    public boolean isBitrateLoggingAllowed() {
        return this.requestConfig.isKeyAllowed("br");
    }

    public boolean isBufferLengthLoggingAllowed() {
        return this.requestConfig.isKeyAllowed(KEY_BUFFER_LENGTH);
    }

    public boolean isBufferStarvationLoggingAllowed() {
        return this.requestConfig.isKeyAllowed(KEY_BUFFER_STARVATION);
    }

    public boolean isContentIdLoggingAllowed() {
        return this.requestConfig.isKeyAllowed(KEY_CONTENT_ID);
    }

    public boolean isDeadlineLoggingAllowed() {
        return this.requestConfig.isKeyAllowed(KEY_DEADLINE);
    }

    public boolean isMaximumRequestThroughputLoggingAllowed() {
        return this.requestConfig.isKeyAllowed(KEY_MAXIMUM_REQUESTED_BITRATE);
    }

    public boolean isMeasuredThroughputLoggingAllowed() {
        return this.requestConfig.isKeyAllowed(KEY_MEASURED_THROUGHPUT);
    }

    public boolean isNextObjectRequestLoggingAllowed() {
        return this.requestConfig.isKeyAllowed(KEY_NEXT_OBJECT_REQUEST);
    }

    public boolean isNextRangeRequestLoggingAllowed() {
        return this.requestConfig.isKeyAllowed(KEY_NEXT_RANGE_REQUEST);
    }

    public boolean isObjectDurationLoggingAllowed() {
        return this.requestConfig.isKeyAllowed("d");
    }

    public boolean isObjectTypeLoggingAllowed() {
        return this.requestConfig.isKeyAllowed(KEY_OBJECT_TYPE);
    }

    public boolean isPlaybackRateLoggingAllowed() {
        return this.requestConfig.isKeyAllowed(KEY_PLAYBACK_RATE);
    }

    public boolean isSessionIdLoggingAllowed() {
        return this.requestConfig.isKeyAllowed(KEY_SESSION_ID);
    }

    public boolean isStartupLoggingAllowed() {
        return this.requestConfig.isKeyAllowed(KEY_STARTUP);
    }

    public boolean isStreamTypeLoggingAllowed() {
        return this.requestConfig.isKeyAllowed(KEY_STREAM_TYPE);
    }

    public boolean isStreamingFormatLoggingAllowed() {
        return this.requestConfig.isKeyAllowed(KEY_STREAMING_FORMAT);
    }

    public boolean isTopBitrateLoggingAllowed() {
        return this.requestConfig.isKeyAllowed("tb");
    }
}
