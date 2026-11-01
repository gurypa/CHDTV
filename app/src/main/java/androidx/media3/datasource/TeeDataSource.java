package androidx.media3.datasource;

import android.net.Uri;
import androidx.media3.common.util.Assertions;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class TeeDataSource implements DataSource {
    private long bytesRemaining;
    private final DataSink dataSink;
    private boolean dataSinkNeedsClosing;
    private final DataSource upstream;

    public TeeDataSource(DataSource r2, DataSink r3) {
        this.upstream = (DataSource) Assertions.checkNotNull(r2);
        this.dataSink = (DataSink) Assertions.checkNotNull(r3);
    }

    @Override // androidx.media3.datasource.DataSource
    public void addTransferListener(TransferListener r2) {
        Assertions.checkNotNull(r2);
        this.upstream.addTransferListener(r2);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws IOException {
        this.upstream.close();     // Catch: Throwable -> L8
        if (this.dataSinkNeedsClosing == false) goto L15;
        this.dataSinkNeedsClosing = false;
        this.dataSink.close();
        return;
    L15:
        return;
    L8:
        th = move-exception;
        if (this.dataSinkNeedsClosing == false) goto L12;
        this.dataSinkNeedsClosing = false;
        this.dataSink.close();
    L12:
        throw th;
    }

    @Override // androidx.media3.datasource.DataSource
    public Map<String, List<String>> getResponseHeaders() {
        return this.upstream.getResponseHeaders();
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        return this.upstream.getUri();
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r7) throws IOException {
        this.bytesRemaining = this.upstream.open(r7);
        if (this.bytesRemaining != 0) goto L6;
        return 0;
    L6:
        if (r7.length == (-1)) goto L8;
    L10:
        this.dataSinkNeedsClosing = true;
        this.dataSink.open(r7);
        return this.bytesRemaining;
    L8:
        if (this.bytesRemaining == (-1)) goto L10;
        r7 = r7.subrange(0, this.bytesRemaining);
        goto L10
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r6, int r7, int r8) throws IOException {
        if (this.bytesRemaining != 0) goto L6;
        return -1;
    L6:
        int r0 = this.upstream.read(r6, r7, r8);
        if (r0 <= 0) goto L11;
        this.dataSink.write(r6, r7, r0);
        if (this.bytesRemaining == (-1)) goto L11;
        this.bytesRemaining -= (long) r0;
    L11:
        return r0;
    }
}
