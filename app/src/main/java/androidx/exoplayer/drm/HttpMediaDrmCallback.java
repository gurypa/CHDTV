package androidx.media3.exoplayer.drm;

import android.net.Uri;
import android.text.TextUtils;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HttpHeaders;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public final class HttpMediaDrmCallback implements MediaDrmCallback {
    private final DataSource.Factory dataSourceFactory;
    private final String defaultLicenseUrl;
    private final boolean forceDefaultLicenseUrl;
    private final Map<String, String> keyRequestProperties;

    public HttpMediaDrmCallback(String r2, DataSource.Factory r3) {
        this(r2, false, r3);
    }

    public HttpMediaDrmCallback(String r2, boolean r3, DataSource.Factory r4) {
        if (r3 == true) goto L5;
    L8:
        boolean r0 = true;
    L9:
        Assertions.checkArgument(r0);
        this.dataSourceFactory = r4;
        this.defaultLicenseUrl = r2;
        this.forceDefaultLicenseUrl = r3;
        this.keyRequestProperties = new HashMap();
        return;
    L5:
        if (TextUtils.isEmpty(r2) == false) goto L8;
        r0 = false;
        goto L9
    }

    public void clearAllKeyRequestProperties() {
        Map<String, String> r0 = this.keyRequestProperties;
        monitor-enter(r0);
        this.keyRequestProperties.clear();     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public void clearKeyRequestProperty(String r3) {
        Assertions.checkNotNull(r3);
        Map<String, String> r0 = this.keyRequestProperties;
        monitor-enter(r0);
        this.keyRequestProperties.remove(r3);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.drm.MediaDrmCallback
    public byte[] executeKeyRequest(UUID r13, ExoMediaDrm.KeyRequest r14) throws MediaDrmCallbackException {
        String r0 = r14.getLicenseServerUrl();
        if (this.forceDefaultLicenseUrl == false) goto L5;
    L8:
        String r1 = this.defaultLicenseUrl;
    L10:
        if (TextUtils.isEmpty(r1) == true) goto L31;
        HashMap r2 = new HashMap();
        if (C0180C.PLAYREADY_UUID.equals(r13) == false) goto L15;
        String r3 = "text/xml";
    L18:
        r2.put(HttpHeaders.CONTENT_TYPE, r3);
        if (C0180C.PLAYREADY_UUID.equals(r13) == false) goto L21;
        r2.put("SOAPAction", "http://schemas.microsoft.com/DRM/2007/03/protocols/AcquireLicense");
    L21:
        Map<String, String> r4 = this.keyRequestProperties;
        monitor-enter(r4);
        r2.putAll(this.keyRequestProperties);     // Catch: Throwable -> L27
        monitor-exit(r4);     // Catch: Throwable -> L27
        return DrmUtil.executePost(this.dataSourceFactory.createDataSource(), r1, r14.getData(), r2);
    L27:
        th = move-exception;
        throw th;
    L15:
        if (C0180C.CLEARKEY_UUID.equals(r13) == false) goto L17;
        r3 = "application/json";
        goto L18
    L17:
        r3 = "application/octet-stream";
        goto L18
    L31:
        throw new MediaDrmCallbackException(new DataSpec.Builder().setUri(Uri.EMPTY).build(), Uri.EMPTY, ImmutableMap.m231of(), 0, new IllegalStateException("No license URL"));
    L5:
        if (TextUtils.isEmpty(r0) == true) goto L8;
        r1 = r0;
        goto L10
    }

    @Override // androidx.media3.exoplayer.drm.MediaDrmCallback
    public byte[] executeProvisionRequest(UUID r5, ExoMediaDrm.ProvisionRequest r6) throws MediaDrmCallbackException {
        return DrmUtil.executePost(this.dataSourceFactory.createDataSource(), r6.getDefaultUrl() + "&signedRequest=" + Util.fromUtf8Bytes(r6.getData()), null, Collections.emptyMap());
    }

    public void setKeyRequestProperty(String r3, String r4) {
        Assertions.checkNotNull(r3);
        Assertions.checkNotNull(r4);
        Map<String, String> r0 = this.keyRequestProperties;
        monitor-enter(r0);
        this.keyRequestProperties.put(r3, r4);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }
}
