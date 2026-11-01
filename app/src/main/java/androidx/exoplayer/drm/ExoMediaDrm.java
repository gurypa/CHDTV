package androidx.media3.exoplayer.drm;

import android.media.DeniedByServerException;
import android.media.MediaCryptoException;
import android.media.MediaDrmException;
import android.media.NotProvisionedException;
import android.os.PersistableBundle;
import androidx.media3.common.DrmInitData;
import androidx.media3.decoder.CryptoConfig;
import androidx.media3.exoplayer.analytics.PlayerId;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public interface ExoMediaDrm {
    public static final int EVENT_KEY_EXPIRED = 3;
    public static final int EVENT_KEY_REQUIRED = 2;
    public static final int EVENT_PROVISION_REQUIRED = 1;
    public static final int KEY_TYPE_OFFLINE = 2;
    public static final int KEY_TYPE_RELEASE = 3;
    public static final int KEY_TYPE_STREAMING = 1;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.drm.ExoMediaDrm$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static List $default$getOfflineLicenseKeySetIds(ExoMediaDrm r1) {
            throw new UnsupportedOperationException();
        }

        public static void $default$removeOfflineLicense(ExoMediaDrm r1, byte[] r2) {
            throw new UnsupportedOperationException();
        }

        public static void $default$setPlayerIdForSession(ExoMediaDrm r0, byte[] r1, PlayerId r2) {
        }
    }

    public static final class AppManagedProvider implements Provider {
        private final ExoMediaDrm exoMediaDrm;

        public AppManagedProvider(ExoMediaDrm r1) {
            this.exoMediaDrm = r1;
        }

        @Override // androidx.media3.exoplayer.drm.ExoMediaDrm.Provider
        public ExoMediaDrm acquireExoMediaDrm(UUID r2) {
            this.exoMediaDrm.acquire();
            return this.exoMediaDrm;
        }
    }

    public static final class KeyRequest {
        public static final int REQUEST_TYPE_INITIAL = 0;
        public static final int REQUEST_TYPE_NONE = 3;
        public static final int REQUEST_TYPE_RELEASE = 2;
        public static final int REQUEST_TYPE_RENEWAL = 1;
        public static final int REQUEST_TYPE_UNKNOWN = Integer.MIN_VALUE;
        public static final int REQUEST_TYPE_UPDATE = 4;
        private final byte[] data;
        private final String licenseServerUrl;
        private final int requestType;

        @Target({ElementType.TYPE_USE})
        @Documented
        @Retention(RetentionPolicy.SOURCE)
        public @interface RequestType {
        }

        public KeyRequest(byte[] r2, String r3) {
            this(r2, r3, Integer.MIN_VALUE);
        }

        public KeyRequest(byte[] r1, String r2, int r3) {
            this.data = r1;
            this.licenseServerUrl = r2;
            this.requestType = r3;
        }

        public byte[] getData() {
            return this.data;
        }

        public String getLicenseServerUrl() {
            return this.licenseServerUrl;
        }

        public int getRequestType() {
            return this.requestType;
        }
    }

    public static final class KeyStatus {
        private final byte[] keyId;
        private final int statusCode;

        public KeyStatus(int r1, byte[] r2) {
            this.statusCode = r1;
            this.keyId = r2;
        }

        public byte[] getKeyId() {
            return this.keyId;
        }

        public int getStatusCode() {
            return this.statusCode;
        }
    }

    public interface OnEventListener {
        void onEvent(ExoMediaDrm r1, byte[] r2, int r3, int r4, byte[] r5);
    }

    public interface OnExpirationUpdateListener {
        void onExpirationUpdate(ExoMediaDrm r1, byte[] r2, long r3);
    }

    public interface OnKeyStatusChangeListener {
        void onKeyStatusChange(ExoMediaDrm r1, byte[] r2, List<KeyStatus> r3, boolean r4);
    }

    public interface Provider {
        ExoMediaDrm acquireExoMediaDrm(UUID r1);
    }

    public static final class ProvisionRequest {
        private final byte[] data;
        private final String defaultUrl;

        public ProvisionRequest(byte[] r1, String r2) {
            this.data = r1;
            this.defaultUrl = r2;
        }

        public byte[] getData() {
            return this.data;
        }

        public String getDefaultUrl() {
            return this.defaultUrl;
        }
    }

    void acquire();

    void closeSession(byte[] r1);

    CryptoConfig createCryptoConfig(byte[] r1) throws MediaCryptoException;

    int getCryptoType();

    KeyRequest getKeyRequest(byte[] r1, List<DrmInitData.SchemeData> r2, int r3, HashMap<String, String> r4) throws NotProvisionedException;

    PersistableBundle getMetrics();

    List<byte[]> getOfflineLicenseKeySetIds();

    byte[] getPropertyByteArray(String r1);

    String getPropertyString(String r1);

    ProvisionRequest getProvisionRequest();

    byte[] openSession() throws MediaDrmException;

    byte[] provideKeyResponse(byte[] r1, byte[] r2) throws NotProvisionedException, DeniedByServerException;

    void provideProvisionResponse(byte[] r1) throws DeniedByServerException;

    Map<String, String> queryKeyStatus(byte[] r1);

    void release();

    void removeOfflineLicense(byte[] r1);

    boolean requiresSecureDecoder(byte[] r1, String r2);

    void restoreKeys(byte[] r1, byte[] r2);

    void setOnEventListener(OnEventListener r1);

    void setOnExpirationUpdateListener(OnExpirationUpdateListener r1);

    void setOnKeyStatusChangeListener(OnKeyStatusChangeListener r1);

    void setPlayerIdForSession(byte[] r1, PlayerId r2);

    void setPropertyByteArray(String r1, byte[] r2);

    void setPropertyString(String r1, String r2);
}
