package androidx.media3.exoplayer.source;

import android.net.Uri;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.TransferListener;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
final class IcyDataSource implements DataSource {
    private int bytesUntilMetadata;
    private final Listener listener;
    private final int metadataIntervalBytes;
    private final byte[] metadataLengthByteHolder;
    private final DataSource upstream;

    public interface Listener {
        void onIcyMetadata(ParsableByteArray r1);
    }

    public IcyDataSource(DataSource r3, int r4, Listener r5) {
        if (r4 <= 0) goto L5;
        boolean r1 = true;
    L6:
        Assertions.checkArgument(r1);
        this.upstream = r3;
        this.metadataIntervalBytes = r4;
        this.listener = r5;
        this.metadataLengthByteHolder = new byte[1];
        this.bytesUntilMetadata = r4;
        return;
    L5:
        r1 = false;
        goto L6
    }

    private boolean readMetadata() throws IOException {
        if (this.upstream.read(this.metadataLengthByteHolder, 0, 1) != (-1)) goto L5;
        return false;
    L5:
        int r4 = (this.metadataLengthByteHolder[0] & 255) << 4;
        if (r4 != 0) goto L8;
        return true;
    L8:
        int r5 = 0;
        int r6 = r4;
        byte[] r7 = new byte[r4];
    L9:
        if (r6 <= 0) goto L14;
        int r0 = this.upstream.read(r7, r5, r6);
        if (r0 == (-1)) goto L12;
        r5 = r5 + r0;
        r6 = r6 - r0;
        goto L9
    L12:
        return false;
    L14:
        if (r4 <= 0) goto L18;
        if (r7[r4 - 1] != 0) goto L18;
        r4 = r4 - 1;
    L18:
        if (r4 <= 0) goto L20;
        this.listener.onIcyMetadata(new ParsableByteArray(r7, r4));
    L20:
        return true;
    }

    @Override // androidx.media3.datasource.DataSource
    public void addTransferListener(TransferListener r2) {
        Assertions.checkNotNull(r2);
        this.upstream.addTransferListener(r2);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() {
        throw new UnsupportedOperationException();
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
    public long open(DataSpec r2) {
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r4, int r5, int r6) throws IOException {
        if (this.bytesUntilMetadata == 0) goto L5;
    L8:
        int r0 = this.upstream.read(r4, r5, Math.min(this.bytesUntilMetadata, r6));
        if (r0 == (-1)) goto L11;
        this.bytesUntilMetadata -= r0;
    L11:
        return r0;
    L5:
        if (readMetadata() == false) goto L7;
        this.bytesUntilMetadata = this.metadataIntervalBytes;
        goto L8
    L7:
        return -1;
    }
}
