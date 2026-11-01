package androidx.media3.exoplayer.drm;

import androidx.media3.decoder.CryptoConfig;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public interface DrmSession {
    public static final int STATE_ERROR = 1;
    public static final int STATE_OPENED = 3;
    public static final int STATE_OPENED_WITH_KEYS = 4;
    public static final int STATE_OPENING = 2;
    public static final int STATE_RELEASED = 0;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.drm.DrmSession$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static boolean $default$playClearSamplesWithoutKeys(DrmSession r1) {
            return false;
        }

        public static void replaceSession(DrmSession r1, DrmSession r2) {
            if (r1 != r2) goto L5;
            return;
        L5:
            if (r2 == null) goto L7;
            r2.acquire(null);
        L7:
            if (r1 == null) goto L10;
            r1.release(null);
            return;
        }
    }

    public static class DrmSessionException extends IOException {
        public final int errorCode;

        public DrmSessionException(Throwable r1, int r2) {
            super(r1);
            this.errorCode = r2;
        }
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    void acquire(DrmSessionEventListener.EventDispatcher r1);

    CryptoConfig getCryptoConfig();

    DrmSessionException getError();

    byte[] getOfflineLicenseKeySetId();

    UUID getSchemeUuid();

    int getState();

    boolean playClearSamplesWithoutKeys();

    Map<String, String> queryKeyStatus();

    void release(DrmSessionEventListener.EventDispatcher r1);

    boolean requiresSecureDecoder(String r1);
}
