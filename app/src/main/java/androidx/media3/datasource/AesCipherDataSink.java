package androidx.media3.datasource;

import androidx.media3.common.util.Util;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class AesCipherDataSink implements DataSink {
    private AesFlushingCipher cipher;
    private final byte[] scratch;
    private final byte[] secretKey;
    private final DataSink wrappedDataSink;

    public AesCipherDataSink(byte[] r2, DataSink r3) {
        this(r2, r3, null);
    }

    public AesCipherDataSink(byte[] r1, DataSink r2, byte[] r3) {
        this.wrappedDataSink = r2;
        this.secretKey = r1;
        this.scratch = r3;
    }

    @Override // androidx.media3.datasource.DataSink
    public void close() throws IOException {
        this.cipher = null;
        this.wrappedDataSink.close();
    }

    @Override // androidx.media3.datasource.DataSink
    public void open(DataSpec r10) throws IOException {
        this.wrappedDataSink.open(r10);
        this.cipher = new AesFlushingCipher(1, this.secretKey, r10.key, r10.uriPositionOffset + r10.position);
    }

    @Override // androidx.media3.datasource.DataSink
    public void write(byte[] r10, int r11, int r12) throws IOException {
        if (this.scratch != null) goto L5;
        ((AesFlushingCipher) Util.castNonNull(this.cipher)).updateInPlace(r10, r11, r12);
        this.wrappedDataSink.write(r10, r11, r12);
        return;
    L5:
        int r0 = 0;
    L6:
        if (r0 >= r12) goto L9;
        int r6 = Math.min(r12 - r0, this.scratch.length);
        byte[] r4 = r10;
        ((AesFlushingCipher) Util.castNonNull(this.cipher)).update(r4, r11 + r0, r6, this.scratch, 0);
        this.wrappedDataSink.write(this.scratch, 0, r6);
        r0 = r0 + r6;
        r10 = r4;
        goto L6
    }
}
