package androidx.media3.datasource;

import android.net.http.UploadDataProvider;
import android.net.http.UploadDataSink;
import java.io.IOException;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
final class ByteArrayUploadDataProvider extends UploadDataProvider {
    private final byte[] data;
    private int position;

    public ByteArrayUploadDataProvider(byte[] r1) {
        this.data = r1;
    }

    @Override // android.net.http.UploadDataProvider
    public long getLength() {
        return this.data.length;
    }

    @Override // android.net.http.UploadDataProvider
    public void read(UploadDataSink r4, ByteBuffer r5) throws IOException {
        int r0 = Math.min(r5.remaining(), this.data.length - this.position);
        r5.put(this.data, this.position, r0);
        this.position += r0;
        r4.onReadSucceeded(false);
    }

    @Override // android.net.http.UploadDataProvider
    public void rewind(UploadDataSink r2) throws IOException {
        this.position = 0;
        r2.onRewindSucceeded();
    }
}
