package androidx.media3.datasource;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;

/* JADX INFO: loaded from: classes.dex */
public final class ContentDataSource extends BaseDataSource {
    private AssetFileDescriptor assetFileDescriptor;
    private long bytesRemaining;
    private FileInputStream inputStream;
    private boolean opened;
    private final ContentResolver resolver;
    private Uri uri;

    public static class ContentDataSourceException extends DataSourceException {
        @Deprecated
        public ContentDataSourceException(IOException r2) {
            this(r2, 2000);
        }

        public ContentDataSourceException(IOException r1, int r2) {
            super(r1, r2);
        }
    }

    public ContentDataSource(Context r2) {
        super(false);
        this.resolver = r2.getContentResolver();
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws ContentDataSourceException {
        this.uri = null;
    L24:
        th = move-exception;
        this.inputStream = null;
    L37:
        th = move-exception;
        this.assetFileDescriptor = null;
        if (this.opened == false) goto L45;
        this.opened = false;
        transferEnded();
    L45:
        throw th;
    L39:
        e = move-exception;
        throw new ContentDataSourceException(e, 2000);     // Catch: Throwable -> L37
    L31:
        if (this.assetFileDescriptor == null) goto L33;
        this.assetFileDescriptor.close();     // Catch: Throwable -> L37 IOException -> L39
    L33:
        this.assetFileDescriptor = null;
        if (this.opened == false) goto L36;
        this.opened = false;
        transferEnded();
    L36:
        throw th;
    L26:
        e = move-exception;
        throw new ContentDataSourceException(e, 2000);     // Catch: Throwable -> L24
    L4:
        if (this.inputStream == null) goto L6;
        this.inputStream.close();     // Catch: Throwable -> L24 IOException -> L26
    L6:
        this.inputStream = null;
    L17:
        e = move-exception;
        throw new ContentDataSourceException(e, 2000);     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        this.assetFileDescriptor = null;
        if (this.opened == false) goto L23;
        this.opened = false;
        transferEnded();
    L23:
        throw th;
    L8:
        if (this.assetFileDescriptor == null) goto L10;
        this.assetFileDescriptor.close();     // Catch: Throwable -> L15 IOException -> L17
    L10:
        this.assetFileDescriptor = null;
        if (this.opened == false) goto L53;
        this.opened = false;
        transferEnded();
        return;
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        return this.uri;
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r27) throws ContentDataSourceException {
        Uri r0 = r27.uri.normalizeScheme();     // Catch: IOException -> L54 ContentDataSourceException -> L62
        this.uri = r0;     // Catch: IOException -> L54 ContentDataSourceException -> L62
        transferInitializing(r27);     // Catch: IOException -> L54 ContentDataSourceException -> L62
        if ("content".equals(r0.getScheme()) == false) goto L6;
        Bundle r4 = new Bundle();     // Catch: IOException -> L54 ContentDataSourceException -> L62
        r4.putBoolean("android.provider.extra.ACCEPT_ORIGINAL_MEDIA_FORMAT", true);     // Catch: IOException -> L54 ContentDataSourceException -> L62
        AssetFileDescriptor r6 = this.resolver.openTypedAssetFileDescriptor(r0, "*/*", r4);     // Catch: IOException -> L54 ContentDataSourceException -> L62
    L7:
        this.assetFileDescriptor = r6;     // Catch: IOException -> L54 ContentDataSourceException -> L62
        if (r6 == null) goto L49;
        long r7 = r6.getLength();     // Catch: IOException -> L54 ContentDataSourceException -> L62
        FileInputStream r5 = new FileInputStream(r6.getFileDescriptor());     // Catch: IOException -> L54 ContentDataSourceException -> L62
        this.inputStream = r5;     // Catch: IOException -> L54 ContentDataSourceException -> L62
        long r9 = -1;
        if (r7 != (-1)) goto L12;
    L16:
        long r14 = r6.getStartOffset();     // Catch: IOException -> L54 ContentDataSourceException -> L62
        long r8 = r5.skip(r27.position + r14) - r14;     // Catch: IOException -> L54 ContentDataSourceException -> L62
        if (r8 != r27.position) goto L47;
        if (r7 != (-1)) goto L28;
        FileChannel r3 = r5.getChannel();     // Catch: IOException -> L54 ContentDataSourceException -> L62
        long r20 = r3.size();     // Catch: IOException -> L54 ContentDataSourceException -> L62
        if (r20 != 0) goto L23;
        this.bytesRemaining = -1;     // Catch: IOException -> L54 ContentDataSourceException -> L62
    L33:
        if (r27.length != r9) goto L35;
    L39:
        this.opened = true;
        transferStarted(r27);
        if (r27.length == r9) goto L43;
        return r27.length;
    L43:
        return this.bytesRemaining;
    L35:
        if (this.bytesRemaining != r9) goto L37;
        long r10 = r27.length;
    L38:
        this.bytesRemaining = r10;
        goto L39
    L37:
        r10 = Math.min(this.bytesRemaining, r27.length);
        goto L38
    L23:
        this.bytesRemaining = r20 - r3.position();     // Catch: IOException -> L54 ContentDataSourceException -> L62
        if (this.bytesRemaining >= 0) goto L33;
        throw new ContentDataSourceException(null, 2008);     // Catch: IOException -> L54 ContentDataSourceException -> L62
    L28:
        r9 = -1;
        this.bytesRemaining = r7 - r8;     // Catch: IOException -> L54 ContentDataSourceException -> L62
        if (this.bytesRemaining >= 0) goto L33;
        throw new ContentDataSourceException(null, 2008);     // Catch: IOException -> L54 ContentDataSourceException -> L62
    L47:
        throw new ContentDataSourceException(null, 2008);     // Catch: IOException -> L54 ContentDataSourceException -> L62
    L12:
        if (r27.position <= r7) goto L16;
        throw new ContentDataSourceException(null, 2008);     // Catch: IOException -> L54 ContentDataSourceException -> L62
    L49:
        int r11 = 2000;
    L52:
        e = e;
    L57:
        if ((e instanceof FileNotFoundException) == false) goto L59;
        int r12 = PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND;
    L61:
        throw new ContentDataSourceException(e, r12);
    L59:
        r12 = r11;
        goto L61
    L51:
        throw new ContentDataSourceException(new IOException("Could not open file descriptor for: " + r0), 2000);     // Catch: IOException -> L52 ContentDataSourceException -> L62
    L6:
        r6 = this.resolver.openAssetFileDescriptor(r0, "r");     // Catch: IOException -> L54 ContentDataSourceException -> L62
    L62:
        e = move-exception;
        throw e;
    L54:
        e = e;
        r11 = 2000;
        goto L57
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r9, int r10, int r11) throws ContentDataSourceException {
        if (r11 != 0) goto L6;
        return 0;
    L6:
        if (this.bytesRemaining != 0) goto L23;
        return -1;
    L23:
    L20:
        e = move-exception;
        throw new ContentDataSourceException(e, 2000);
    L9:
        if (this.bytesRemaining != (-1)) goto L11;
        int r0 = r11;
    L12:
        int r2 = ((FileInputStream) Util.castNonNull(this.inputStream)).read(r9, r10, r0);     // Catch: IOException -> L20
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
