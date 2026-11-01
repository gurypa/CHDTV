package androidx.media3.datasource;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class ByteArrayDataSink implements DataSink {
    private ByteArrayOutputStream stream;

    public ByteArrayDataSink() {
    }

    @Override // androidx.media3.datasource.DataSink
    public void close() throws IOException {
        ((ByteArrayOutputStream) Util.castNonNull(this.stream)).close();
    }

    public byte[] getData() {
        if (this.stream != null) goto L6;
        return null;
    L6:
        return this.stream.toByteArray();
    }

    @Override // androidx.media3.datasource.DataSink
    public void open(DataSpec r5) {
        if (r5.length != (-1)) goto L6;
        this.stream = new ByteArrayOutputStream();
        return;
    L6:
        if (r5.length > 2147483647L) goto L8;
        boolean r0 = true;
    L9:
        Assertions.checkArgument(r0);
        this.stream = new ByteArrayOutputStream((int) r5.length);
        return;
    L8:
        r0 = false;
        goto L9
    }

    @Override // androidx.media3.datasource.DataSink
    public void write(byte[] r2, int r3, int r4) {
        ((ByteArrayOutputStream) Util.castNonNull(this.stream)).write(r2, r3, r4);
    }
}
