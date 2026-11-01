package androidx.media3.datasource;

import android.net.Uri;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.text.TextUtils;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.io.IOException;
import java.io.RandomAccessFile;

/* JADX INFO: loaded from: classes.dex */
public final class FileDataSource extends BaseDataSource {
    private long bytesRemaining;
    private RandomAccessFile file;
    private boolean opened;
    private Uri uri;

    public static final class Factory implements DataSource.Factory {
        private TransferListener listener;

        public Factory() {
        }

        @Override // androidx.media3.datasource.DataSource.Factory
        public /* bridge */ /* synthetic */ DataSource createDataSource() {
            return createDataSource();
        }

        @Override // androidx.media3.datasource.DataSource.Factory
        public FileDataSource createDataSource() {
            FileDataSource r0 = new FileDataSource();
            if (this.listener == null) goto L5;
            r0.addTransferListener(this.listener);
        L5:
            return r0;
        }

        public Factory setListener(TransferListener r1) {
            this.listener = r1;
            return this;
        }
    }

    public static class FileDataSourceException extends DataSourceException {
        @Deprecated
        public FileDataSourceException(Exception r2) {
            super(r2, 2000);
        }

        @Deprecated
        public FileDataSourceException(String r2, IOException r3) {
            super(r2, r3, 2000);
        }

        public FileDataSourceException(String r1, Throwable r2, int r3) {
            super(r1, r2, r3);
        }

        public FileDataSourceException(Throwable r1, int r2) {
            super(r1, r2);
        }
    }

    public FileDataSource() {
        super(false);
    }

    private static RandomAccessFile openLocalFile(Uri r7) throws FileDataSourceException {
        int r0 = PlaybackException.ERROR_CODE_IO_NO_PERMISSION;
        return new RandomAccessFile((String) Assertions.checkNotNull(r7.getPath()), "r");
    L11:
        e = move-exception;
        if (TextUtils.isEmpty(r7.getQuery()) == false) goto L25;
        if (TextUtils.isEmpty(r7.getFragment()) == false) goto L25;
        if ((e.getCause() instanceof ErrnoException) == true) goto L19;
    L21:
        r0 = PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND;
    L23:
        throw new FileDataSourceException(e, r0);
    L19:
        if (((ErrnoException) e.getCause()).errno != OsConstants.EACCES) goto L21;
    L25:
        throw new FileDataSourceException(String.format("uri has query and/or fragment, which are not supported. Did you call Uri.parse() on a string containing '?' or '#'? Use Uri.fromFile(new File(path)) to avoid this. path=%s,query=%s,fragment=%s", new Object[]{r7.getPath(), r7.getQuery(), r7.getFragment()}), e, 1004);
    L8:
        e = move-exception;
        throw new FileDataSourceException(e, PlaybackException.ERROR_CODE_IO_NO_PERMISSION);
    L5:
        e = move-exception;
        throw new FileDataSourceException(e, 2000);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws FileDataSourceException {
        this.uri = null;
    L10:
        th = move-exception;
        this.file = null;
        if (this.opened == false) goto L18;
        this.opened = false;
        transferEnded();
    L18:
        throw th;
    L12:
        e = move-exception;
        throw new FileDataSourceException(e, 2000);     // Catch: Throwable -> L10
    L4:
        if (this.file == null) goto L6;
        this.file.close();     // Catch: Throwable -> L10 IOException -> L12
    L6:
        this.file = null;
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
    public long open(DataSpec r6) throws FileDataSourceException {
        Uri r0 = r6.uri;
        this.uri = r0;
        transferInitializing(r6);
        this.file = openLocalFile(r0);
        this.file.seek(r6.position);     // Catch: IOException -> L14
        if (r6.length != (-1)) goto L6;
        long r1 = this.file.length() - r6.position;     // Catch: IOException -> L14
    L7:
        this.bytesRemaining = r1;     // Catch: IOException -> L14
        if (this.bytesRemaining < 0) goto L13;
        this.opened = true;
        transferStarted(r6);
        return this.bytesRemaining;
    L13:
        throw new FileDataSourceException(null, null, 2008);
    L6:
        r1 = r6.length;     // Catch: IOException -> L14
    L14:
        e = move-exception;
        throw new FileDataSourceException(e, 2000);
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r6, int r7, int r8) throws FileDataSourceException {
        if (r8 != 0) goto L6;
        return 0;
    L6:
        if (this.bytesRemaining != 0) goto L16;
        return -1;
    L16:
        int r0 = ((RandomAccessFile) Util.castNonNull(this.file)).read(r6, r7, (int) Math.min(this.bytesRemaining, r8));     // Catch: IOException -> L13
        if (r0 <= 0) goto L12;
        this.bytesRemaining -= (long) r0;
        bytesTransferred(r0);
    L12:
        return r0;
    L13:
        e = move-exception;
        throw new FileDataSourceException(e, 2000);
    }
}
