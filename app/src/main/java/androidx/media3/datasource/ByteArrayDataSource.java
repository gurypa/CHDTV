package androidx.media3.datasource;

import android.net.Uri;
import androidx.media3.common.util.Assertions;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class ByteArrayDataSource extends BaseDataSource {
    private int bytesRemaining;
    private byte[] data;
    private boolean opened;
    private int readPosition;
    private Uri uri;
    private final UriResolver uriResolver;

    public interface UriResolver {
        byte[] resolve(Uri r1) throws IOException;
    }

    public ByteArrayDataSource(UriResolver r2) {
        super(false);
        this.uriResolver = (UriResolver) Assertions.checkNotNull(r2);
    }

    public ByteArrayDataSource(final byte[] r2) {
        this(new ByteArrayDataSource$$ExternalSyntheticLambda0(r2));
        if (r2.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        return;
    L5:
        r0 = false;
        goto L6
    }

    static /* synthetic */ byte[] lambda$new$0(byte[] r0, Uri r1) throws IOException {
        return r0;
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() {
        if (this.opened == false) goto L5;
        this.opened = false;
        transferEnded();
    L5:
        this.uri = null;
        this.data = null;
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        return this.uri;
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r7) throws IOException {
        transferInitializing(r7);
        this.uri = r7.uri;
        this.data = this.uriResolver.resolve(this.uri);
        if (r7.position > this.data.length) goto L13;
        this.readPosition = (int) r7.position;
        this.bytesRemaining = this.data.length - ((int) r7.position);
        if (r7.length == (-1)) goto L7;
        this.bytesRemaining = (int) Math.min(this.bytesRemaining, r7.length);
    L7:
        this.opened = true;
        transferStarted(r7);
        if (r7.length == (-1)) goto L11;
        return r7.length;
    L11:
        return this.bytesRemaining;
    L13:
        throw new DataSourceException(2008);
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r3, int r4, int r5) {
        if (r5 != 0) goto L6;
        return 0;
    L6:
        if (this.bytesRemaining != 0) goto L9;
        return -1;
    L9:
        int r6 = Math.min(r5, this.bytesRemaining);
        System.arraycopy(Assertions.checkStateNotNull(this.data), this.readPosition, r3, r4, r6);
        this.readPosition += r6;
        this.bytesRemaining -= r6;
        bytesTransferred(r6);
        return r6;
    }
}
