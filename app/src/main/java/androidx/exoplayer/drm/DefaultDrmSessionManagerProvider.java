package androidx.media3.exoplayer.drm;

import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Ints;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultDrmSessionManagerProvider implements DrmSessionManagerProvider {
    private MediaItem.DrmConfiguration drmConfiguration;
    private DataSource.Factory drmHttpDataSourceFactory;
    private LoadErrorHandlingPolicy drmLoadErrorHandlingPolicy;
    private final Object lock;
    private DrmSessionManager manager;
    private String userAgent;

    public DefaultDrmSessionManagerProvider() {
        this.lock = new Object();
    }

    private DrmSessionManager createManager(MediaItem.DrmConfiguration r7) {
        if (this.drmHttpDataSourceFactory == null) goto L5;
        DataSource.Factory r0 = this.drmHttpDataSourceFactory;
    L7:
        if (r7.licenseUri != null) goto L9;
        String r2 = null;
    L10:
        HttpMediaDrmCallback r1 = new HttpMediaDrmCallback(r2, r7.forceDefaultLicenseUri, r0);
        UnmodifiableIterator<Map.Entry<String, String>> r3 = r7.licenseRequestHeaders.entrySet().iterator();
    L12:
        if (r3.hasNext() == false) goto L14;
        Map.Entry<String, String> r4 = r3.next();
        r1.setKeyRequestProperty(r4.getKey(), r4.getValue());
        goto L12
    L14:
        DefaultDrmSessionManager.Builder r5 = new DefaultDrmSessionManager.Builder().setUuidAndExoMediaDrmProvider(r7.scheme, FrameworkMediaDrm.DEFAULT_PROVIDER).setMultiSession(r7.multiSession).setPlayClearSamplesWithoutKeys(r7.playClearContentWithoutKey).setUseDrmSessionsForClearContent(Ints.toArray(r7.forcedSessionTrackTypes));
        if (this.drmLoadErrorHandlingPolicy == null) goto L17;
        r5.setLoadErrorHandlingPolicy(this.drmLoadErrorHandlingPolicy);
    L17:
        DefaultDrmSessionManager r6 = r5.build(r1);
        r6.setMode(0, r7.getKeySetId());
        return r6;
    L9:
        r2 = r7.licenseUri.toString();
        goto L10
    L5:
        r0 = new DefaultHttpDataSource.Factory().setUserAgent(this.userAgent);
        goto L7
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionManagerProvider
    public DrmSessionManager get(MediaItem r4) {
        Assertions.checkNotNull(r4.localConfiguration);
        MediaItem.DrmConfiguration r0 = r4.localConfiguration.drmConfiguration;
        if (r0 == null) goto L5;
        Object r1 = this.lock;
        monitor-enter(r1);
    L14:
        th = move-exception;
        throw th;
    L9:
        if (Util.areEqual(r0, this.drmConfiguration) == true) goto L11;
        this.drmConfiguration = r0;     // Catch: Throwable -> L14
        this.manager = createManager(r0);     // Catch: Throwable -> L14
    L11:
        DrmSessionManager r2 = (DrmSessionManager) Assertions.checkNotNull(this.manager);     // Catch: Throwable -> L14
        monitor-exit(r1);     // Catch: Throwable -> L14
        return r2;
    L5:
        return DrmSessionManager.DRM_UNSUPPORTED;
    }

    public void setDrmHttpDataSourceFactory(DataSource.Factory r1) {
        this.drmHttpDataSourceFactory = r1;
    }

    public void setDrmLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r1) {
        this.drmLoadErrorHandlingPolicy = r1;
    }

    @Deprecated
    public void setDrmUserAgent(String r1) {
        this.userAgent = r1;
    }
}
