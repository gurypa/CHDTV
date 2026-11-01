package androidx.media3.datasource;

import android.net.Uri;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class AesCipherDataSource implements DataSource {
    private AesFlushingCipher cipher;
    private final byte[] secretKey;
    private final DataSource upstream;

    public AesCipherDataSource(byte[] r1, DataSource r2) {
        this.upstream = r2;
        this.secretKey = r1;
    }

    @Override // androidx.media3.datasource.DataSource
    public void addTransferListener(TransferListener r2) {
        Assertions.checkNotNull(r2);
        this.upstream.addTransferListener(r2);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws IOException {
        this.cipher = null;
        this.upstream.close();
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
    public long open(DataSpec r11) throws IOException {
        long r0 = this.upstream.open(r11);
        this.cipher = new AesFlushingCipher(2, this.secretKey, r11.key, r11.uriPositionOffset + r11.position);
        return r0;
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r3, int r4, int r5) throws IOException {
        if (r5 != 0) goto L5;
        return 0;
    L5:
        int r0 = this.upstream.read(r3, r4, r5);
        if (r0 != (-1)) goto L8;
        return -1;
    L8:
        ((AesFlushingCipher) Util.castNonNull(this.cipher)).updateInPlace(r3, r4, r0);
        return r0;
    }
}
