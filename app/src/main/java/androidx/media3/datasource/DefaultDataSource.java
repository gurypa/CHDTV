package androidx.media3.datasource;

import android.content.Context;
import android.net.Uri;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultDataSource implements DataSource {
    private static final String SCHEME_ANDROID_RESOURCE = "android.resource";
    private static final String SCHEME_ASSET = "asset";
    private static final String SCHEME_CONTENT = "content";
    private static final String SCHEME_DATA = "data";
    private static final String SCHEME_RAW = "rawresource";
    private static final String SCHEME_RTMP = "rtmp";
    private static final String SCHEME_UDP = "udp";
    private static final String TAG = "DefaultDataSource";
    private DataSource assetDataSource;
    private final DataSource baseDataSource;
    private DataSource contentDataSource;
    private final Context context;
    private DataSource dataSchemeDataSource;
    private DataSource dataSource;
    private DataSource fileDataSource;
    private DataSource rawResourceDataSource;
    private DataSource rtmpDataSource;
    private final List<TransferListener> transferListeners;
    private DataSource udpDataSource;

    public static final class Factory implements DataSource.Factory {
        private final DataSource.Factory baseDataSourceFactory;
        private final Context context;
        private TransferListener transferListener;

        public Factory(Context r2) {
            this(r2, new DefaultHttpDataSource.Factory());
        }

        public Factory(Context r2, DataSource.Factory r3) {
            this.context = r2.getApplicationContext();
            this.baseDataSourceFactory = (DataSource.Factory) Assertions.checkNotNull(r3);
        }

        @Override // androidx.media3.datasource.DataSource.Factory
        public /* bridge */ /* synthetic */ DataSource createDataSource() {
            return createDataSource();
        }

        @Override // androidx.media3.datasource.DataSource.Factory
        public DefaultDataSource createDataSource() {
            DefaultDataSource r0 = new DefaultDataSource(this.context, this.baseDataSourceFactory.createDataSource());
            if (this.transferListener == null) goto L5;
            r0.addTransferListener(this.transferListener);
        L5:
            return r0;
        }

        public Factory setTransferListener(TransferListener r1) {
            this.transferListener = r1;
            return this;
        }
    }

    public DefaultDataSource(Context r2, DataSource r3) {
        this.context = r2.getApplicationContext();
        this.baseDataSource = (DataSource) Assertions.checkNotNull(r3);
        this.transferListeners = new ArrayList();
    }

    public DefaultDataSource(Context r2, String r3, int r4, int r5, boolean r6) {
        this(r2, new DefaultHttpDataSource.Factory().setUserAgent(r3).setConnectTimeoutMs(r4).setReadTimeoutMs(r5).setAllowCrossProtocolRedirects(r6).createDataSource());
    }

    public DefaultDataSource(Context r7, String r8, boolean r9) {
        this(r7, r8, 8000, 8000, r9);
    }

    public DefaultDataSource(Context r7, boolean r8) {
        this(r7, null, 8000, 8000, r8);
    }

    private void addListenersToDataSource(DataSource r3) {
        int r0 = 0;
    L4:
        if (r0 >= this.transferListeners.size()) goto L6;
        r3.addTransferListener(this.transferListeners.get(r0));
        r0 = r0 + 1;
        goto L4
    }

    private DataSource getAssetDataSource() {
        if (this.assetDataSource != null) goto L6;
        this.assetDataSource = new AssetDataSource(this.context);
        addListenersToDataSource(this.assetDataSource);
    L6:
        return this.assetDataSource;
    }

    private DataSource getContentDataSource() {
        if (this.contentDataSource != null) goto L6;
        this.contentDataSource = new ContentDataSource(this.context);
        addListenersToDataSource(this.contentDataSource);
    L6:
        return this.contentDataSource;
    }

    private DataSource getDataSchemeDataSource() {
        if (this.dataSchemeDataSource != null) goto L6;
        this.dataSchemeDataSource = new DataSchemeDataSource();
        addListenersToDataSource(this.dataSchemeDataSource);
    L6:
        return this.dataSchemeDataSource;
    }

    private DataSource getFileDataSource() {
        if (this.fileDataSource != null) goto L6;
        this.fileDataSource = new FileDataSource();
        addListenersToDataSource(this.fileDataSource);
    L6:
        return this.fileDataSource;
    }

    private DataSource getRawResourceDataSource() {
        if (this.rawResourceDataSource != null) goto L6;
        this.rawResourceDataSource = new RawResourceDataSource(this.context);
        addListenersToDataSource(this.rawResourceDataSource);
    L6:
        return this.rawResourceDataSource;
    }

    private DataSource getRtmpDataSource() {
        if (this.rtmpDataSource != null) goto L15;
        this.rtmpDataSource = (DataSource) Class.forName("androidx.media3.datasource.rtmp.RtmpDataSource").getConstructor(new Class[0]).newInstance(new Object[0]);     // Catch: Exception -> L6 ClassNotFoundException -> L9
        addListenersToDataSource(this.rtmpDataSource);     // Catch: Exception -> L6 ClassNotFoundException -> L9
    L12:
        if (this.rtmpDataSource != null) goto L15;
        this.rtmpDataSource = this.baseDataSource;
    L6:
        e = move-exception;
        throw new RuntimeException("Error instantiating RTMP extension", e);
    L10:
        Log.m48w(TAG, "Attempting to play RTMP stream without depending on the RTMP extension");
    L15:
        return this.rtmpDataSource;
    }

    private DataSource getUdpDataSource() {
        if (this.udpDataSource != null) goto L6;
        this.udpDataSource = new UdpDataSource();
        addListenersToDataSource(this.udpDataSource);
    L6:
        return this.udpDataSource;
    }

    private void maybeAddListenerToDataSource(DataSource r1, TransferListener r2) {
        if (r1 == null) goto L5;
        r1.addTransferListener(r2);
        return;
    }

    @Override // androidx.media3.datasource.DataSource
    public void addTransferListener(TransferListener r2) {
        Assertions.checkNotNull(r2);
        this.baseDataSource.addTransferListener(r2);
        this.transferListeners.add(r2);
        maybeAddListenerToDataSource(this.fileDataSource, r2);
        maybeAddListenerToDataSource(this.assetDataSource, r2);
        maybeAddListenerToDataSource(this.contentDataSource, r2);
        maybeAddListenerToDataSource(this.rtmpDataSource, r2);
        maybeAddListenerToDataSource(this.udpDataSource, r2);
        maybeAddListenerToDataSource(this.dataSchemeDataSource, r2);
        maybeAddListenerToDataSource(this.rawResourceDataSource, r2);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws IOException {
        if (this.dataSource != null) goto L11;
        return;
    L11:
        this.dataSource.close();     // Catch: Throwable -> L7
        this.dataSource = null;
        return;
    L7:
        th = move-exception;
        this.dataSource = null;
        throw th;
    }

    @Override // androidx.media3.datasource.DataSource
    public Map<String, List<String>> getResponseHeaders() {
        if (this.dataSource != null) goto L6;
        return Collections.emptyMap();
    L6:
        return this.dataSource.getResponseHeaders();
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        if (this.dataSource != null) goto L6;
        return null;
    L6:
        return this.dataSource.getUri();
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r4) throws IOException {
        if (this.dataSource != null) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        String r1 = r4.uri.getScheme();
        if (Util.isLocalFileUri(r4.uri) == false) goto L16;
        String r2 = r4.uri.getPath();
        if (r2 != null) goto L11;
    L13:
        this.dataSource = getFileDataSource();
    L38:
        return this.dataSource.open(r4);
    L11:
        if (r2.startsWith("/android_asset/") == false) goto L13;
        this.dataSource = getAssetDataSource();
        goto L38
    L16:
        if (SCHEME_ASSET.equals(r1) == false) goto L19;
        this.dataSource = getAssetDataSource();
        goto L38
    L19:
        if (SCHEME_CONTENT.equals(r1) == false) goto L22;
        this.dataSource = getContentDataSource();
        goto L38
    L22:
        if (SCHEME_RTMP.equals(r1) == false) goto L25;
        this.dataSource = getRtmpDataSource();
        goto L38
    L25:
        if (SCHEME_UDP.equals(r1) == false) goto L28;
        this.dataSource = getUdpDataSource();
        goto L38
    L28:
        if ("data".equals(r1) == false) goto L31;
        this.dataSource = getDataSchemeDataSource();
        goto L38
    L31:
        if ("rawresource".equals(r1) == false) goto L33;
    L36:
        this.dataSource = getRawResourceDataSource();
        goto L38
    L33:
        if (SCHEME_ANDROID_RESOURCE.equals(r1) == true) goto L36;
        this.dataSource = this.baseDataSource;
        goto L38
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r2, int r3, int r4) throws IOException {
        return ((DataSource) Assertions.checkNotNull(this.dataSource)).read(r2, r3, r4);
    }
}
