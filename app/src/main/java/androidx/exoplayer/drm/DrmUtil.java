package androidx.media3.exoplayer.drm;

import android.media.DeniedByServerException;
import android.media.MediaDrm;
import android.media.MediaDrmResetException;
import android.media.NotProvisionedException;
import android.net.Uri;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSourceInputStream;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.datasource.StatsDataSource;
import com.google.common.net.HttpHeaders;
import com.google.common.p003io.ByteStreams;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class DrmUtil {
    public static final int ERROR_SOURCE_EXO_MEDIA_DRM = 1;
    public static final int ERROR_SOURCE_LICENSE_ACQUISITION = 2;
    public static final int ERROR_SOURCE_PROVISIONING = 3;
    private static final int MAX_MANUAL_REDIRECTS = 5;

    private static final class Api23 {
        private Api23() {
        }

        public static boolean isMediaDrmResetException(Throwable r1) {
            return r1 instanceof MediaDrmResetException;
        }
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorSource {
    }

    private DrmUtil() {
    }

    public static byte[] executePost(DataSource r10, String r11, byte[] r12, Map<String, String> r13) throws MediaDrmCallbackException {
        StatsDataSource r0 = new StatsDataSource(r10);
        DataSpec r2 = new DataSpec.Builder().setUri(r11).setHttpRequestHeaders(r13).setHttpMethod(2).setHttpBody(r12).setFlags(1).build();
        DataSpec r3 = r2;
        int r4 = 0;
    L24:
        DataSourceInputStream r1 = new DataSourceInputStream(r0, r3);     // Catch: Exception -> L21
        byte[] r5 = ByteStreams.toByteArray(r1);     // Catch: Throwable -> L8 HttpDataSource.InvalidResponseCodeException -> L10
    L6:
        Util.closeQuietly(r1);     // Catch: Exception -> L21
        return r5;
    L10:
        e = move-exception;
        String r6 = getRedirectUrl(e, r4);     // Catch: Throwable -> L8
        if (r6 == null) goto L18;
        r4 = r4 + 1;     // Catch: Throwable -> L8
        r3 = r3.buildUpon().setUri(r6).build();     // Catch: Throwable -> L8
        Util.closeQuietly(r1);     // Catch: Exception -> L21
        goto L24
    L18:
        throw e;     // Catch: Throwable -> L8
    L8:
        th = move-exception;
        Util.closeQuietly(r1);     // Catch: Exception -> L21
        throw th;     // Catch: Exception -> L21
    L21:
        e = move-exception;
        throw new MediaDrmCallbackException(r2, (Uri) Assertions.checkNotNull(r0.getLastOpenedUri()), r0.getResponseHeaders(), r0.getBytesRead(), e);
    }

    public static int getErrorCodeForMediaDrmException(Throwable r3, int r4) {
        if ((r3 instanceof MediaDrm.MediaDrmStateException) == false) goto L7;
        return Util.getErrorCodeForMediaDrmErrorCode(Util.getErrorCodeFromPlatformDiagnosticsInfo(((MediaDrm.MediaDrmStateException) r3).getDiagnosticInfo()));
    L7:
        if (Util.SDK_INT < 23) goto L12;
        if (Api23.isMediaDrmResetException(r3) == false) goto L12;
        return PlaybackException.ERROR_CODE_DRM_SYSTEM_ERROR;
    L12:
        if ((r3 instanceof NotProvisionedException) == false) goto L14;
    L44:
        return PlaybackException.ERROR_CODE_DRM_PROVISIONING_FAILED;
    L14:
        if (isFailureToConstructNotProvisionedException(r3) == true) goto L44;
        if ((r3 instanceof DeniedByServerException) == false) goto L21;
        return PlaybackException.ERROR_CODE_DRM_DEVICE_REVOKED;
    L21:
        if ((r3 instanceof UnsupportedDrmException) == false) goto L25;
        return PlaybackException.ERROR_CODE_DRM_SCHEME_UNSUPPORTED;
    L25:
        if ((r3 instanceof DefaultDrmSessionManager.MissingSchemeDataException) == false) goto L29;
        return PlaybackException.ERROR_CODE_DRM_CONTENT_ERROR;
    L29:
        if ((r3 instanceof KeysExpiredException) == false) goto L33;
        return PlaybackException.ERROR_CODE_DRM_LICENSE_EXPIRED;
    L33:
        if (r4 != 1) goto L36;
        return PlaybackException.ERROR_CODE_DRM_SYSTEM_ERROR;
    L36:
        if (r4 != 2) goto L40;
        return PlaybackException.ERROR_CODE_DRM_LICENSE_ACQUISITION_FAILED;
    L40:
        if (r4 != 3) goto L43;
        return PlaybackException.ERROR_CODE_DRM_PROVISIONING_FAILED;
    L43:
        throw new IllegalArgumentException();
    }

    private static String getRedirectUrl(HttpDataSource.InvalidResponseCodeException r6, int r7) {
        if (r6.responseCode == 307) goto L7;
        if (r6.responseCode == 308) goto L7;
    L9:
        boolean r0 = false;
    L11:
        if (r0 == true) goto L13;
        return null;
    L13:
        Map<String, List<String>> r3 = r6.headerFields;
        if (r3 == null) goto L21;
        List<String> r4 = r3.get(HttpHeaders.LOCATION);
        if (r4 == null) goto L21;
        if (r4.isEmpty() == true) goto L21;
        return r4.get(0);
    L21:
        return null;
    L7:
        if (r7 >= 5) goto L9;
        r0 = true;
        goto L11
    }

    public static boolean isFailureToConstructNotProvisionedException(Throwable r2) {
        if (Util.SDK_INT == 34) goto L5;
    L11:
        return false;
    L5:
        if ((r2 instanceof NoSuchMethodError) == false) goto L11;
        if (r2.getMessage() == null) goto L11;
        if (r2.getMessage().contains("Landroid/media/NotProvisionedException;.<init>(") == false) goto L11;
        return true;
    }

    public static boolean isFailureToConstructResourceBusyException(Throwable r2) {
        if (Util.SDK_INT == 34) goto L5;
    L11:
        return false;
    L5:
        if ((r2 instanceof NoSuchMethodError) == false) goto L11;
        if (r2.getMessage() == null) goto L11;
        if (r2.getMessage().contains("Landroid/media/ResourceBusyException;.<init>(") == false) goto L11;
        return true;
    }
}
