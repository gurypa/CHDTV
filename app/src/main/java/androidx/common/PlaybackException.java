package androidx.media3.common;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.Util;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public class PlaybackException extends Exception {
    public static final int CUSTOM_ERROR_CODE_BASE = 1000000;
    public static final int ERROR_CODE_AUDIO_TRACK_INIT_FAILED = 5001;
    public static final int ERROR_CODE_AUDIO_TRACK_OFFLOAD_INIT_FAILED = 5004;
    public static final int ERROR_CODE_AUDIO_TRACK_OFFLOAD_WRITE_FAILED = 5003;
    public static final int ERROR_CODE_AUDIO_TRACK_WRITE_FAILED = 5002;
    public static final int ERROR_CODE_AUTHENTICATION_EXPIRED = -102;
    public static final int ERROR_CODE_BAD_VALUE = -3;
    public static final int ERROR_CODE_BEHIND_LIVE_WINDOW = 1002;
    public static final int ERROR_CODE_CONCURRENT_STREAM_LIMIT = -104;
    public static final int ERROR_CODE_CONTENT_ALREADY_PLAYING = -110;
    public static final int ERROR_CODE_DECODER_INIT_FAILED = 4001;
    public static final int ERROR_CODE_DECODER_QUERY_FAILED = 4002;
    public static final int ERROR_CODE_DECODING_FAILED = 4003;
    public static final int ERROR_CODE_DECODING_FORMAT_EXCEEDS_CAPABILITIES = 4004;
    public static final int ERROR_CODE_DECODING_FORMAT_UNSUPPORTED = 4005;
    public static final int ERROR_CODE_DECODING_RESOURCES_RECLAIMED = 4006;
    public static final int ERROR_CODE_DISCONNECTED = -100;
    public static final int ERROR_CODE_DRM_CONTENT_ERROR = 6003;
    public static final int ERROR_CODE_DRM_DEVICE_REVOKED = 6007;
    public static final int ERROR_CODE_DRM_DISALLOWED_OPERATION = 6005;
    public static final int ERROR_CODE_DRM_LICENSE_ACQUISITION_FAILED = 6004;
    public static final int ERROR_CODE_DRM_LICENSE_EXPIRED = 6008;
    public static final int ERROR_CODE_DRM_PROVISIONING_FAILED = 6002;
    public static final int ERROR_CODE_DRM_SCHEME_UNSUPPORTED = 6001;
    public static final int ERROR_CODE_DRM_SYSTEM_ERROR = 6006;
    public static final int ERROR_CODE_DRM_UNSPECIFIED = 6000;
    public static final int ERROR_CODE_END_OF_PLAYLIST = -109;
    public static final int ERROR_CODE_FAILED_RUNTIME_CHECK = 1004;
    public static final int ERROR_CODE_INVALID_STATE = -2;
    public static final int ERROR_CODE_IO_BAD_HTTP_STATUS = 2004;
    public static final int ERROR_CODE_IO_CLEARTEXT_NOT_PERMITTED = 2007;
    public static final int ERROR_CODE_IO_FILE_NOT_FOUND = 2005;
    public static final int ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE = 2003;
    public static final int ERROR_CODE_IO_NETWORK_CONNECTION_FAILED = 2001;
    public static final int ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT = 2002;
    public static final int ERROR_CODE_IO_NO_PERMISSION = 2006;
    public static final int ERROR_CODE_IO_READ_POSITION_OUT_OF_RANGE = 2008;
    public static final int ERROR_CODE_IO_UNSPECIFIED = 2000;
    public static final int ERROR_CODE_NOT_AVAILABLE_IN_REGION = -106;
    public static final int ERROR_CODE_NOT_SUPPORTED = -6;
    public static final int ERROR_CODE_PARENTAL_CONTROL_RESTRICTED = -105;
    public static final int ERROR_CODE_PARSING_CONTAINER_MALFORMED = 3001;
    public static final int ERROR_CODE_PARSING_CONTAINER_UNSUPPORTED = 3003;
    public static final int ERROR_CODE_PARSING_MANIFEST_MALFORMED = 3002;
    public static final int ERROR_CODE_PARSING_MANIFEST_UNSUPPORTED = 3004;
    public static final int ERROR_CODE_PERMISSION_DENIED = -4;
    public static final int ERROR_CODE_PREMIUM_ACCOUNT_REQUIRED = -103;
    public static final int ERROR_CODE_REMOTE_ERROR = 1001;
    public static final int ERROR_CODE_SETUP_REQUIRED = -108;
    public static final int ERROR_CODE_SKIP_LIMIT_REACHED = -107;
    public static final int ERROR_CODE_TIMEOUT = 1003;
    public static final int ERROR_CODE_UNSPECIFIED = 1000;
    public static final int ERROR_CODE_VIDEO_FRAME_PROCESSING_FAILED = 7001;
    public static final int ERROR_CODE_VIDEO_FRAME_PROCESSOR_INIT_FAILED = 7000;
    private static final String FIELD_BUNDLE_EXTRAS = null;
    protected static final int FIELD_CUSTOM_ID_BASE = 1000;
    private static final String FIELD_INT_ERROR_CODE = null;
    private static final String FIELD_LONG_TIMESTAMP_MS = null;
    private static final String FIELD_STRING_CAUSE_CLASS_NAME = null;
    private static final String FIELD_STRING_CAUSE_MESSAGE = null;
    private static final String FIELD_STRING_MESSAGE = null;
    public final int errorCode;
    public final Bundle extras;
    public final long timestampMs;

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorCode {
    }

    static {
        FIELD_INT_ERROR_CODE = Util.intToStringMaxRadix(0);
        FIELD_LONG_TIMESTAMP_MS = Util.intToStringMaxRadix(1);
        FIELD_STRING_MESSAGE = Util.intToStringMaxRadix(2);
        FIELD_STRING_CAUSE_CLASS_NAME = Util.intToStringMaxRadix(3);
        FIELD_STRING_CAUSE_MESSAGE = Util.intToStringMaxRadix(4);
        FIELD_BUNDLE_EXTRAS = Util.intToStringMaxRadix(5);
    }

    protected PlaybackException(Bundle r9) {
        this(r9.getString(FIELD_STRING_MESSAGE), getCauseFromBundle(r9), r9.getInt(FIELD_INT_ERROR_CODE, 1000), getExtrasFromBundle(r9), r9.getLong(FIELD_LONG_TIMESTAMP_MS, SystemClock.elapsedRealtime()));
    }

    public PlaybackException(String r8, Throwable r9, int r10) {
        this(r8, r9, r10, Bundle.EMPTY, Clock.DEFAULT.elapsedRealtime());
    }

    public PlaybackException(String r9, Throwable r10, int r11, Bundle r12) {
        this(r9, r10, r11, r12, Clock.DEFAULT.elapsedRealtime());
    }

    protected PlaybackException(String r1, Throwable r2, int r3, Bundle r4, long r5) {
        super(r1, r2);
        this.errorCode = r3;
        this.extras = r4;
        this.timestampMs = r5;
    }

    private static RemoteException createRemoteException(String r1) {
        return new RemoteException(r1);
    }

    private static Throwable createThrowable(Class<?> r4, String r5) throws Exception {
        return (Throwable) r4.getConstructor(new Class[]{String.class}).newInstance(new Object[]{r5});
    }

    public static PlaybackException fromBundle(Bundle r1) {
        return new PlaybackException(r1);
    }

    private static Throwable getCauseFromBundle(Bundle r5) {
        String r0 = r5.getString(FIELD_STRING_CAUSE_CLASS_NAME);
        String r1 = r5.getString(FIELD_STRING_CAUSE_MESSAGE);
        Throwable r2 = null;
        if (TextUtils.isEmpty(r0) == true) goto L16;
        Class<?> r3 = Class.forName(r0, true, PlaybackException.class.getClassLoader());     // Catch: Throwable -> L10
        if (Throwable.class.isAssignableFrom(r3) == false) goto L8;
        r2 = createThrowable(r3, r1);     // Catch: Throwable -> L10
    L8:
        if (r2 == null) goto L13;
        return r2;
    L13:
        return createRemoteException(r1);
    L11:
        if (0 == 0) goto L13;
        return null;
    L16:
        return null;
    }

    public static String getErrorCodeName(int r1) {
        switch(r1) {
            case -110: goto L111;
            case -109: goto L109;
            case -108: goto L107;
            case -107: goto L105;
            case -106: goto L103;
            case -105: goto L101;
            case -104: goto L99;
            case -103: goto L97;
            case -102: goto L95;
            case -100: goto L93;
            case -6: goto L91;
            case -4: goto L89;
            case -3: goto L87;
            case -2: goto L85;
            case 1000: goto L83;
            case 1001: goto L81;
            case 1002: goto L79;
            case 1003: goto L77;
            case 1004: goto L75;
            case 2000: goto L73;
            case 2001: goto L71;
            case 2002: goto L69;
            case 2003: goto L67;
            case 2004: goto L65;
            case 2005: goto L63;
            case 2006: goto L61;
            case 2007: goto L59;
            case 2008: goto L57;
            case 3001: goto L55;
            case 3002: goto L53;
            case 3003: goto L51;
            case 3004: goto L49;
            case 4001: goto L47;
            case 4002: goto L45;
            case 4003: goto L43;
            case 4004: goto L41;
            case 4005: goto L39;
            case 4006: goto L37;
            case 5001: goto L35;
            case 5002: goto L33;
            case 5003: goto L31;
            case 5004: goto L29;
            case 6000: goto L27;
            case 6001: goto L25;
            case 6002: goto L23;
            case 6003: goto L21;
            case 6004: goto L19;
            case 6005: goto L17;
            case 6006: goto L15;
            case 6007: goto L13;
            case 6008: goto L11;
            case 7000: goto L9;
            case 7001: goto L7;
            default: goto L4;
        };
    L7:
        return "ERROR_CODE_VIDEO_FRAME_PROCESSING_FAILED";
    L9:
        return "ERROR_CODE_VIDEO_FRAME_PROCESSOR_INIT_FAILED";
    L11:
        return "ERROR_CODE_DRM_LICENSE_EXPIRED";
    L13:
        return "ERROR_CODE_DRM_DEVICE_REVOKED";
    L15:
        return "ERROR_CODE_DRM_SYSTEM_ERROR";
    L17:
        return "ERROR_CODE_DRM_DISALLOWED_OPERATION";
    L19:
        return "ERROR_CODE_DRM_LICENSE_ACQUISITION_FAILED";
    L21:
        return "ERROR_CODE_DRM_CONTENT_ERROR";
    L23:
        return "ERROR_CODE_DRM_PROVISIONING_FAILED";
    L25:
        return "ERROR_CODE_DRM_SCHEME_UNSUPPORTED";
    L27:
        return "ERROR_CODE_DRM_UNSPECIFIED";
    L29:
        return "ERROR_CODE_AUDIO_TRACK_OFFLOAD_INIT_FAILED";
    L31:
        return "ERROR_CODE_AUDIO_TRACK_OFFLOAD_WRITE_FAILED";
    L33:
        return "ERROR_CODE_AUDIO_TRACK_WRITE_FAILED";
    L35:
        return "ERROR_CODE_AUDIO_TRACK_INIT_FAILED";
    L37:
        return "ERROR_CODE_DECODING_RESOURCES_RECLAIMED";
    L39:
        return "ERROR_CODE_DECODING_FORMAT_UNSUPPORTED";
    L41:
        return "ERROR_CODE_DECODING_FORMAT_EXCEEDS_CAPABILITIES";
    L43:
        return "ERROR_CODE_DECODING_FAILED";
    L45:
        return "ERROR_CODE_DECODER_QUERY_FAILED";
    L47:
        return "ERROR_CODE_DECODER_INIT_FAILED";
    L49:
        return "ERROR_CODE_PARSING_MANIFEST_UNSUPPORTED";
    L51:
        return "ERROR_CODE_PARSING_CONTAINER_UNSUPPORTED";
    L53:
        return "ERROR_CODE_PARSING_MANIFEST_MALFORMED";
    L55:
        return "ERROR_CODE_PARSING_CONTAINER_MALFORMED";
    L57:
        return "ERROR_CODE_IO_READ_POSITION_OUT_OF_RANGE";
    L59:
        return "ERROR_CODE_IO_CLEARTEXT_NOT_PERMITTED";
    L61:
        return "ERROR_CODE_IO_NO_PERMISSION";
    L63:
        return "ERROR_CODE_IO_FILE_NOT_FOUND";
    L65:
        return "ERROR_CODE_IO_BAD_HTTP_STATUS";
    L67:
        return "ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE";
    L69:
        return "ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT";
    L71:
        return "ERROR_CODE_IO_NETWORK_CONNECTION_FAILED";
    L73:
        return "ERROR_CODE_IO_UNSPECIFIED";
    L75:
        return "ERROR_CODE_FAILED_RUNTIME_CHECK";
    L77:
        return "ERROR_CODE_TIMEOUT";
    L79:
        return "ERROR_CODE_BEHIND_LIVE_WINDOW";
    L81:
        return "ERROR_CODE_REMOTE_ERROR";
    L83:
        return "ERROR_CODE_UNSPECIFIED";
    L85:
        return "ERROR_CODE_INVALID_STATE";
    L87:
        return "ERROR_CODE_BAD_VALUE";
    L89:
        return "ERROR_CODE_PERMISSION_DENIED";
    L91:
        return "ERROR_CODE_NOT_SUPPORTED";
    L93:
        return "ERROR_CODE_DISCONNECTED";
    L95:
        return "ERROR_CODE_AUTHENTICATION_EXPIRED";
    L97:
        return "ERROR_CODE_PREMIUM_ACCOUNT_REQUIRED";
    L99:
        return "ERROR_CODE_CONCURRENT_STREAM_LIMIT";
    L101:
        return "ERROR_CODE_PARENTAL_CONTROL_RESTRICTED";
    L103:
        return "ERROR_CODE_NOT_AVAILABLE_IN_REGION";
    L105:
        return "ERROR_CODE_SKIP_LIMIT_REACHED";
    L107:
        return "ERROR_CODE_SETUP_REQUIRED";
    L109:
        return "ERROR_CODE_END_OF_PLAYLIST";
    L111:
        return "ERROR_CODE_CONTENT_ALREADY_PLAYING";
    L4:
        if (r1 < 1000000) goto L113;
        return "custom error code";
    L113:
        return "invalid error code";
    }

    private static Bundle getExtrasFromBundle(Bundle r2) {
        Bundle r0 = r2.getBundle(FIELD_BUNDLE_EXTRAS);
        if (r0 == null) goto L6;
        return r0;
    L6:
        return Bundle.EMPTY;
    }

    public boolean errorInfoEquals(PlaybackException r9) {
        if (this != r9) goto L6;
        return true;
    L6:
        if (r9 != null) goto L8;
    L32:
        return false;
    L8:
        if (getClass() != r9.getClass()) goto L32;
        Throwable r2 = getCause();
        Throwable r3 = r9.getCause();
        if (r2 == null) goto L19;
        if (r3 == null) goto L19;
        if (Util.areEqual(r2.getMessage(), r3.getMessage()) == true) goto L17;
        return false;
    L17:
        if (Util.areEqual(r2.getClass(), r3.getClass()) == true) goto L23;
        return false;
    L23:
        if (this.errorCode != r9.errorCode) goto L30;
        if (Util.areEqual(getMessage(), r9.getMessage()) == false) goto L30;
        if (this.timestampMs != r9.timestampMs) goto L30;
        return true;
    L30:
        return false;
    L19:
        if (r2 != null) goto L31;
        if (r3 == null) goto L23;
    L31:
        return false;
    }

    public final String getErrorCodeName() {
        return getErrorCodeName(this.errorCode);
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        r0.putInt(FIELD_INT_ERROR_CODE, this.errorCode);
        r0.putLong(FIELD_LONG_TIMESTAMP_MS, this.timestampMs);
        r0.putString(FIELD_STRING_MESSAGE, getMessage());
        r0.putBundle(FIELD_BUNDLE_EXTRAS, this.extras);
        Throwable r1 = getCause();
        if (r1 == null) goto L5;
        r0.putString(FIELD_STRING_CAUSE_CLASS_NAME, r1.getClass().getName());
        r0.putString(FIELD_STRING_CAUSE_MESSAGE, r1.getMessage());
    L5:
        return r0;
    }
}
