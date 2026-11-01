package androidx.media3.exoplayer.hls;

import android.net.Uri;
import androidx.media3.common.util.Assertions;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSourceInputStream;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.TransferListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes.dex */
class Aes128DataSource implements DataSource {
    private CipherInputStream cipherInputStream;
    private final byte[] encryptionIv;
    private final byte[] encryptionKey;
    private final DataSource upstream;

    public Aes128DataSource(DataSource r1, byte[] r2, byte[] r3) {
        this.upstream = r1;
        this.encryptionKey = r2;
        this.encryptionIv = r3;
    }

    @Override // androidx.media3.datasource.DataSource
    public final void addTransferListener(TransferListener r2) {
        Assertions.checkNotNull(r2);
        this.upstream.addTransferListener(r2);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws IOException {
        if (this.cipherInputStream == null) goto L6;
        this.cipherInputStream = null;
        this.upstream.close();
        return;
    }

    protected Cipher getCipherInstance() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance("AES/CBC/PKCS7Padding");
    }

    @Override // androidx.media3.datasource.DataSource
    public final Map<String, List<String>> getResponseHeaders() {
        return this.upstream.getResponseHeaders();
    }

    @Override // androidx.media3.datasource.DataSource
    public final Uri getUri() {
        return this.upstream.getUri();
    }

    @Override // androidx.media3.datasource.DataSource
    public final long open(DataSpec r7) throws IOException {
        Cipher r0 = getCipherInstance();     // Catch: NoSuchPaddingException -> L12 Throwable -> L14
        r0.init(2, new SecretKeySpec(this.encryptionKey, "AES"), new IvParameterSpec(this.encryptionIv));     // Catch: Throwable -> L7 InvalidKeyException -> L9
        DataSourceInputStream r3 = new DataSourceInputStream(this.upstream, r7);
        this.cipherInputStream = new CipherInputStream(r3, r0);
        r3.open();
        return -1;
    L7:
        e = move-exception;
        throw new RuntimeException(e);
    L14:
        e = move-exception;
        throw new RuntimeException(e);
    }

    @Override // androidx.media3.common.DataReader
    public final int read(byte[] r3, int r4, int r5) throws IOException {
        Assertions.checkNotNull(this.cipherInputStream);
        int r0 = this.cipherInputStream.read(r3, r4, r5);
        if (r0 >= 0) goto L6;
        return -1;
    L6:
        return r0;
    }
}
