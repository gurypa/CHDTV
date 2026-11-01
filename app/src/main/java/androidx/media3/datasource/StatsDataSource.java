package androidx.media3.datasource;

import android.net.Uri;
import androidx.media3.common.util.Assertions;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class StatsDataSource implements DataSource {
    private long bytesRead;
    private final DataSource dataSource;
    private Uri lastOpenedUri;
    private Map<String, List<String>> lastResponseHeaders;

    public StatsDataSource(DataSource r2) {
        this.dataSource = (DataSource) Assertions.checkNotNull(r2);
        this.lastOpenedUri = Uri.EMPTY;
        this.lastResponseHeaders = Collections.emptyMap();
    }

    @Override // androidx.media3.datasource.DataSource
    public void addTransferListener(TransferListener r2) {
        Assertions.checkNotNull(r2);
        this.dataSource.addTransferListener(r2);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws IOException {
        this.dataSource.close();
    }

    public long getBytesRead() {
        return this.bytesRead;
    }

    public Uri getLastOpenedUri() {
        return this.lastOpenedUri;
    }

    public Map<String, List<String>> getLastResponseHeaders() {
        return this.lastResponseHeaders;
    }

    @Override // androidx.media3.datasource.DataSource
    public Map<String, List<String>> getResponseHeaders() {
        return this.dataSource.getResponseHeaders();
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        return this.dataSource.getUri();
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r5) throws IOException {
        this.lastOpenedUri = r5.uri;
        this.lastResponseHeaders = Collections.emptyMap();
        long r0 = this.dataSource.open(r5);     // Catch: Throwable -> L9
        Uri r2 = getUri();
        if (r2 == null) goto L7;
        this.lastOpenedUri = r2;
    L7:
        this.lastResponseHeaders = getResponseHeaders();
        return r0;
    L9:
        th = move-exception;
        Uri r1 = getUri();
        if (r1 == null) goto L13;
        this.lastOpenedUri = r1;
    L13:
        this.lastResponseHeaders = getResponseHeaders();
        throw th;
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r6, int r7, int r8) throws IOException {
        int r0 = this.dataSource.read(r6, r7, r8);
        if (r0 == (-1)) goto L5;
        this.bytesRead += (long) r0;
    L5:
        return r0;
    }

    public void resetBytesRead() {
        this.bytesRead = 0;
    }
}
