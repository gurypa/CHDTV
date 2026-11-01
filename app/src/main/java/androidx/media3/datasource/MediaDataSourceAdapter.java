package androidx.media3.datasource;

import android.media.MediaDataSource;
import android.net.Uri;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class MediaDataSourceAdapter extends BaseDataSource {
    private long bytesRemaining;
    private final MediaDataSource mediaDataSource;
    private boolean opened;
    private long position;
    private Uri uri;

    public MediaDataSourceAdapter(MediaDataSource r1, boolean r2) {
        super(r2);
        this.mediaDataSource = r1;
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws IOException {
        this.uri = null;
        if (this.opened == false) goto L6;
        this.opened = false;
        transferEnded();
        return;
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        return this.uri;
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r7) throws IOException {
        this.uri = r7.uri;
        this.position = r7.position;
        transferInitializing(r7);
        if (this.mediaDataSource.getSize() == (-1)) goto L10;
        if (this.position <= this.mediaDataSource.getSize()) goto L10;
        throw new DataSourceException(2008);
    L10:
        if (this.mediaDataSource.getSize() != (-1)) goto L12;
        this.bytesRemaining = -1;
    L14:
        if (r7.length != (-1)) goto L16;
    L20:
        this.opened = true;
        transferStarted(r7);
        if (r7.length == (-1)) goto L24;
        return r7.length;
    L24:
        return this.bytesRemaining;
    L16:
        if (this.bytesRemaining != (-1)) goto L18;
        long r0 = r7.length;
    L19:
        this.bytesRemaining = r0;
        goto L20
    L18:
        r0 = Math.min(this.bytesRemaining, r7.length);
        goto L19
    L12:
        this.bytesRemaining = this.mediaDataSource.getSize() - this.position;
        goto L14
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r13, int r14, int r15) throws DataSourceException {
        if (r15 != 0) goto L6;
        return 0;
    L6:
        if (this.bytesRemaining != 0) goto L9;
        return -1;
    L9:
        if (this.bytesRemaining != (-1)) goto L11;
        int r0 = r15;
    L29:
        int r16 = this.mediaDataSource.readAt(this.position, r13, r14, r0);     // Catch: IOException -> L23
        if (r16 != (-1)) goto L18;
        return -1;
    L18:
        this.position += (long) r16;
        if (this.bytesRemaining == (-1)) goto L21;
        this.bytesRemaining -= (long) r16;
    L21:
        bytesTransferred(r16);
        return r16;
    L23:
        e = move-exception;
        IOException r17 = e;
    L28:
        throw new DataSourceException(r17, 2000);
    L25:
        e = move-exception;
        r17 = e;
        goto L28
    L11:
        r0 = (int) Math.min(this.bytesRemaining, r15);
        goto L29
    }
}
