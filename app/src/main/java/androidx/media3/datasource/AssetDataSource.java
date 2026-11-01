package androidx.media3.datasource;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public final class AssetDataSource extends BaseDataSource {
    private final AssetManager assetManager;
    private long bytesRemaining;
    private InputStream inputStream;
    private boolean opened;
    private Uri uri;

    public static final class AssetDataSourceException extends DataSourceException {
        @Deprecated
        public AssetDataSourceException(IOException r2) {
            super(r2, 2000);
        }

        public AssetDataSourceException(Throwable r1, int r2) {
            super(r1, r2);
        }
    }

    public AssetDataSource(Context r2) {
        super(false);
        this.assetManager = r2.getAssets();
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws AssetDataSourceException {
        this.uri = null;
    L10:
        th = move-exception;
        this.inputStream = null;
        if (this.opened == false) goto L18;
        this.opened = false;
        transferEnded();
    L18:
        throw th;
    L12:
        e = move-exception;
        throw new AssetDataSourceException(e, 2000);     // Catch: Throwable -> L10
    L4:
        if (this.inputStream == null) goto L6;
        this.inputStream.close();     // Catch: Throwable -> L10 IOException -> L12
    L6:
        this.inputStream = null;
        if (this.opened == false) goto L21;
        this.opened = false;
        transferEnded();
        return;
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        return this.uri;
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r12) throws AssetDataSourceException {
        this.uri = r12.uri;     // Catch: IOException -> L20 AssetDataSourceException -> L27
        String r0 = (String) Assertions.checkNotNull(this.uri.getPath());     // Catch: IOException -> L20 AssetDataSourceException -> L27
        if (r0.startsWith("/android_asset/") == false) goto L6;
        r0 = r0.substring(15);     // Catch: IOException -> L20 AssetDataSourceException -> L27
    L8:
        transferInitializing(r12);     // Catch: IOException -> L20 AssetDataSourceException -> L27
        this.inputStream = this.assetManager.open(r0, 1);     // Catch: IOException -> L20 AssetDataSourceException -> L27
        if (this.inputStream.skip(r12.position) < r12.position) goto L19;
        if (r12.length == (-1)) goto L13;
        this.bytesRemaining = r12.length;     // Catch: IOException -> L20 AssetDataSourceException -> L27
    L16:
        this.opened = true;
        transferStarted(r12);
        return this.bytesRemaining;
    L13:
        this.bytesRemaining = this.inputStream.available();     // Catch: IOException -> L20 AssetDataSourceException -> L27
        if (this.bytesRemaining != 2147483647L) goto L16;
        this.bytesRemaining = -1;     // Catch: IOException -> L20 AssetDataSourceException -> L27
        goto L16
    L19:
        throw new AssetDataSourceException(null, 2008);     // Catch: IOException -> L20 AssetDataSourceException -> L27
    L6:
        if (r0.startsWith("/") == false) goto L8;
        r0 = r0.substring(1);     // Catch: IOException -> L20 AssetDataSourceException -> L27
    L27:
        e = move-exception;
        throw e;
    L20:
        e = move-exception;
        if ((e instanceof FileNotFoundException) == false) goto L24;
        int r2 = PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND;
    L26:
        throw new AssetDataSourceException(e, r2);
    L24:
        r2 = 2000;
        goto L26
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r9, int r10, int r11) throws AssetDataSourceException {
        if (r11 != 0) goto L6;
        return 0;
    L6:
        if (this.bytesRemaining != 0) goto L23;
        return -1;
    L23:
    L20:
        e = move-exception;
        throw new AssetDataSourceException(e, 2000);
    L9:
        if (this.bytesRemaining != (-1)) goto L11;
        int r0 = r11;
    L12:
        int r2 = ((InputStream) Util.castNonNull(this.inputStream)).read(r9, r10, r0);     // Catch: IOException -> L20
        if (r2 != (-1)) goto L16;
        return -1;
    L16:
        if (this.bytesRemaining == (-1)) goto L18;
        this.bytesRemaining -= (long) r2;
    L18:
        bytesTransferred(r2);
        return r2;
    L11:
        r0 = (int) Math.min(this.bytesRemaining, r11);     // Catch: IOException -> L20
        goto L12
    }
}
